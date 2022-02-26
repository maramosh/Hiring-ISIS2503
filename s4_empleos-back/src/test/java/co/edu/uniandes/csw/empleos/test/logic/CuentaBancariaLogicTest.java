/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.logic;

import co.edu.uniandes.csw.empleos.ejb.CuentaBancariaLogic;
import co.edu.uniandes.csw.empleos.entities.CuentaBancariaEntity;
import co.edu.uniandes.csw.empleos.entities.EstudianteEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.ejb.EstudianteLogic;
import co.edu.uniandes.csw.empleos.persistence.CuentaBancariaPersistence;
import com.sun.xml.rpc.util.Debug;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 *
 * @author je.hernadezr
 */
@RunWith(Arquillian.class)
public class CuentaBancariaLogicTest {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private CuentaBancariaLogic cuentaBancariaLogic;

    @Inject
    private EstudianteLogic estudianteLogic;

    private List<CuentaBancariaEntity> data = new ArrayList<CuentaBancariaEntity>();

    @Deployment
    public static JavaArchive createDeploytment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(CuentaBancariaEntity.class.getPackage())
                .addPackage(CuentaBancariaLogic.class.getPackage())
                .addPackage(CuentaBancariaPersistence.class.getPackage())
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }

    /**
     * Configuración inicial de la prueba.
     */
    @Before
    public void configTest() {
        try {
            utx.begin();
            em.joinTransaction();
            clearData();
            insertData();
            utx.commit();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Limpia las tablas que están implicadas en la prueba.
     */
    private void clearData() {
        em.createQuery("delete from EstudianteEntity").executeUpdate();
        em.createQuery("delete from CuentaBancariaEntity").executeUpdate();

    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {
        PodamFactory factory = new PodamFactoryImpl();
        for (int i = 0; i < 3; i++) {
            CuentaBancariaEntity entity = factory.manufacturePojo(CuentaBancariaEntity.class);
            EstudianteEntity estudianteEntity = factory.manufacturePojo(EstudianteEntity.class);

            entity.setEstudiante(estudianteEntity);
            estudianteEntity.setCuentaBancaria(entity);
            estudianteEntity.setCuentaBancaria(entity);
            em.persist(entity);
            em.persist(estudianteEntity);
            data.add(entity);
        }
    }

    /**
     * Prueba para crear una cuenta bancaria.
     *
     */
    @Test
    public void createCuentaBancariaTest() throws BusinessLogicException {
        CuentaBancariaEntity newEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        newEntity.setNumeroCuenta(Long.MAX_VALUE + "");
        SecureRandom num = new SecureRandom();
        int numero = Math.abs(num.nextInt(3) + 2);
        String cuenta = numero == 2 ? "Ahorros" : "Corriente";
        newEntity.setTipoCuenta(cuenta);
        CuentaBancariaEntity result = cuentaBancariaLogic.createCuentaBancaria(newEntity);
        Assert.assertNotNull(result);

        CuentaBancariaEntity entity = em.find(CuentaBancariaEntity.class, result.getId());
        Assert.assertEquals(newEntity.getId(), entity.getId());
        Assert.assertEquals(newEntity.getNumeroCuenta(), entity.getNumeroCuenta());
        Assert.assertEquals(newEntity.getNombreBanco(), entity.getNombreBanco());
        Assert.assertEquals(newEntity.getTipoCuenta(), entity.getTipoCuenta());
        data.add(entity);
    }

    /**
     * Prueba la verificacion de una regla de negocio
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createCuentaBancariaNumeroNullTest() throws BusinessLogicException {
        CuentaBancariaEntity newEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        newEntity.setNumeroCuenta(null);
        CuentaBancariaEntity result = cuentaBancariaLogic.createCuentaBancaria(newEntity);
        Assert.assertNull(result.getNumeroCuenta());
    }

    /**
     * Pruba la verificacion de una regla de negocio
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createCuentaBancariaNumeroExistente() throws BusinessLogicException {
        CuentaBancariaEntity newEntity = factory.manufacturePojo(CuentaBancariaEntity.class);

        CuentaBancariaEntity result = cuentaBancariaLogic.createCuentaBancaria(newEntity);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getNumeroCuenta(), newEntity.getNumeroCuenta());

        Assert.assertNull(cuentaBancariaLogic.createCuentaBancaria(newEntity));

    }

    /**
     * Pruba la verificacion de una regla de negocio
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createCuentaBancariaNumeroCeroTest() throws BusinessLogicException {
        CuentaBancariaEntity newEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        newEntity.setNumeroCuenta("0");
        CuentaBancariaEntity result = cuentaBancariaLogic.createCuentaBancaria(newEntity);
        Assert.assertEquals(0 + "", result.getNumeroCuenta());
        Assert.assertNull(result);

    }

    /**
     * Pruba la verificacion de una regla de negocio
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createCuentaBancariaNumeroNegativoTest() throws BusinessLogicException {
        CuentaBancariaEntity newEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        newEntity.setNumeroCuenta("-" + newEntity.getNumeroCuenta());
        CuentaBancariaEntity result = cuentaBancariaLogic.createCuentaBancaria(newEntity);
        Assert.assertEquals("-" + newEntity.getNumeroCuenta(), result.getNumeroCuenta());

    }

    @Test(expected = BusinessLogicException.class)
    public void createCuentaBancariaNumeroNegativo2Test() throws BusinessLogicException {
        CuentaBancariaEntity newEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        newEntity.setNumeroCuenta("-1");
        CuentaBancariaEntity result = cuentaBancariaLogic.createCuentaBancaria(newEntity);
    }

    /**
     * Pruba la verificacion de una regla de negocio
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createCuentaBancariaNumeroCortoTest() throws BusinessLogicException {
        CuentaBancariaEntity newEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        SecureRandom num = new SecureRandom();
        int numero = Math.abs(num.nextInt());
        newEntity.setNumeroCuenta(numero + "");
        Assert.assertEquals(numero, Integer.parseInt(newEntity.getNumeroCuenta()));

        CuentaBancariaEntity result = cuentaBancariaLogic.createCuentaBancaria(newEntity);
        Assert.assertTrue(Integer.parseInt(result.getNumeroCuenta()) <= 20);

    }

    /**
     * Pruba la verificacion de una regla de negocio
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createCuentaBancariaNumeroNoNaturalTest() throws BusinessLogicException {

        CuentaBancariaEntity newEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        char[] c = newEntity.getNumeroCuenta().toCharArray();
        String cadena = ",";

        for (int i = 1; i < c.length; i++) {
            cadena += c[i];

        }
        newEntity.setNumeroCuenta(cadena);
        Assert.assertTrue(newEntity.getNumeroCuenta().contains(","));
        CuentaBancariaEntity result = cuentaBancariaLogic.createCuentaBancaria(newEntity);

    }

    /**
     * Pruba la verificacion de una regla de negocio
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createCuentaBancariaNumeroNoNatural2Test() throws BusinessLogicException {

        CuentaBancariaEntity newEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        char[] c = newEntity.getNumeroCuenta().toCharArray();
        String cadena = "-";

        for (int i = 1; i < c.length - 1; i++) {
            cadena += c[i];

        }
        cadena += ".";
        newEntity.setNumeroCuenta(cadena);
        CuentaBancariaEntity result = cuentaBancariaLogic.createCuentaBancaria(newEntity);
        Assert.assertTrue(newEntity.getNumeroCuenta().contains("."));
        Assert.assertTrue(newEntity.getNumeroCuenta().contains("-"));

    }

    /**
     * Pruba la verificacion de una regla de negocio
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createCuentaBancariaTipoCuentaMalTest() throws BusinessLogicException {
        CuentaBancariaEntity newEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        Assert.assertFalse(newEntity.getTipoCuenta().equals("Ahorros"));
        Assert.assertFalse(newEntity.getTipoCuenta().equals("Corriente"));
        CuentaBancariaEntity result = cuentaBancariaLogic.createCuentaBancaria(newEntity);

    }

    /**
     * Pruba la verificacion de una regla de negocio
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createCuentaBancariaNombreBancoNullTest() throws BusinessLogicException {
        CuentaBancariaEntity newEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        newEntity.setNombreBanco(null);
        CuentaBancariaEntity result = cuentaBancariaLogic.createCuentaBancaria(newEntity);
        Assert.assertNull(newEntity.getNombreBanco());
    }

    /**
     * Pruba la verificacion de una regla de negocio
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createCuentaBancariaNombreBancoVacioTest() throws BusinessLogicException {
        CuentaBancariaEntity newEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        newEntity.setNombreBanco("");
        CuentaBancariaEntity result = cuentaBancariaLogic.createCuentaBancaria(newEntity);
        Assert.assertTrue(newEntity.getNombreBanco().equals(""));

    }

    @Test(expected = BusinessLogicException.class)
    public void createCuentaBancariaNumeroLenght9() throws BusinessLogicException {
        CuentaBancariaEntity newEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        newEntity.setNumeroCuenta("12345678");
        CuentaBancariaEntity result = cuentaBancariaLogic.createCuentaBancaria(newEntity);

    }

    @Test(expected = BusinessLogicException.class)
    public void createCuentaBancariaNombreVacio() throws BusinessLogicException {
        CuentaBancariaEntity newEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        newEntity.setNombreBanco("");
        CuentaBancariaEntity result = cuentaBancariaLogic.createCuentaBancaria(newEntity);
    }

    /**
     * Pruba que verifica que si se actualizo una cuenta bancaria correctamente.
     *
     * @throws BusinessLogicException
     */
    @Test
    public void updateCuentaBancariaTest() throws BusinessLogicException {
        CuentaBancariaEntity entity = data.get(0);
        CuentaBancariaEntity pojoEntity = factory.manufacturePojo(CuentaBancariaEntity.class);

        EstudianteEntity newEstudiante = factory.manufacturePojo(EstudianteEntity.class);
        newEstudiante.setCorreo(newEstudiante.getCorreo() + "@uniandes.edu.co");
        newEstudiante.setSemestre(1);
        newEstudiante = estudianteLogic.crearEstudiante(newEstudiante);

        pojoEntity.setEstudiante(newEstudiante);

        pojoEntity.setId(entity.getId());
        pojoEntity.setNumeroCuenta("0002568797");

        pojoEntity.setTipoCuenta("Ahorros");
        cuentaBancariaLogic.updateCuentaBancaria(pojoEntity.getId(), pojoEntity);

        CuentaBancariaEntity resp = em.find(CuentaBancariaEntity.class, entity.getId());

        Assert.assertEquals(pojoEntity.getNumeroCuenta(), resp.getNumeroCuenta());

        Assert.assertEquals(pojoEntity.getNombreBanco(), resp.getNombreBanco());

        Assert.assertEquals(pojoEntity.getTipoCuenta(), resp.getTipoCuenta());
    }

    /**
     * Pruba la verificacion de una regla de negocio
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void updateCuentaBancariaTipoCuentaMalTest() throws BusinessLogicException {
        CuentaBancariaEntity entity = data.get(0);
        CuentaBancariaEntity pojoEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        pojoEntity.setId(entity.getId());

        cuentaBancariaLogic.updateCuentaBancaria(pojoEntity.getId(), pojoEntity);
        Assert.assertFalse(pojoEntity.getTipoCuenta().equals("Ahorros"));
        Assert.assertFalse(pojoEntity.getTipoCuenta().equals("Corriente"));

    }

    /**
     * Pruba la verificacion de una regla de negocio
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void updateCuentaBancariaNumeroNullTest() throws BusinessLogicException {
        CuentaBancariaEntity entity = data.get(0);
        CuentaBancariaEntity pojoEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setNumeroCuenta(null);
        cuentaBancariaLogic.updateCuentaBancaria(pojoEntity.getId(), pojoEntity);
        Assert.assertNull(pojoEntity.getNumeroCuenta());

    }

    @Test(expected = BusinessLogicException.class)
    public void updateCuentaBancariaNumeroLenghTest() throws BusinessLogicException {
        CuentaBancariaEntity entity = data.get(0);
        CuentaBancariaEntity pojoEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setNumeroCuenta("000000782228789456788");
        Assert.assertTrue(pojoEntity.getNumeroCuenta().length() > 20);
        cuentaBancariaLogic.updateCuentaBancaria(pojoEntity.getId(), pojoEntity);

    }

    @Test(expected = BusinessLogicException.class)
    public void updateCuentaBancariaNumeroNegativoTest() throws BusinessLogicException {
        CuentaBancariaEntity entity = data.get(0);
        CuentaBancariaEntity pojoEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setNumeroCuenta("-12");
        cuentaBancariaLogic.updateCuentaBancaria(pojoEntity.getId(), pojoEntity);
        Assert.assertTrue(Long.parseLong(pojoEntity.getNumeroCuenta()) < 0);

    }

    @Test(expected = BusinessLogicException.class)
    public void updateCuentaBancariaNombreNuloTest() throws BusinessLogicException {
        CuentaBancariaEntity entity = data.get(0);
        CuentaBancariaEntity pojoEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setNombreBanco(null);
        cuentaBancariaLogic.updateCuentaBancaria(pojoEntity.getId(), pojoEntity);
        Assert.assertNull(pojoEntity.getNombreBanco());

    }

    @Test(expected = BusinessLogicException.class)
    public void updateCuentaBancariaNombreVacioTest() throws BusinessLogicException {
        CuentaBancariaEntity entity = data.get(0);
        CuentaBancariaEntity pojoEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setNombreBanco("");
        cuentaBancariaLogic.updateCuentaBancaria(pojoEntity.getId(), pojoEntity);
        Assert.assertTrue(pojoEntity.getNombreBanco().equals(""));
    }

    /**
     * Pruba la verificacion de una regla de negocio
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void updateCuentaBancariaNoExisteTest() throws BusinessLogicException {
        CuentaBancariaEntity pojoEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        pojoEntity.setId(Long.MAX_VALUE);
        cuentaBancariaLogic.updateCuentaBancaria(pojoEntity.getId(), pojoEntity);

    }

    /**
     * Prueba la verificacion de una regla de negocio
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void updateCuentaBancariaNumeroCeroTest() throws BusinessLogicException {
        CuentaBancariaEntity entity = data.get(0);
        CuentaBancariaEntity pojoEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setNumeroCuenta("0");
        CuentaBancariaEntity f = cuentaBancariaLogic.updateCuentaBancaria(pojoEntity.getId(), pojoEntity);

        Assert.assertEquals(0, Integer.parseInt(f.getNumeroCuenta()));
    }

    /**
     * Pruba la verificacion de una regla de negocio
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void updateCuentaBancariaNumeroNegativoTest2() throws BusinessLogicException {
        CuentaBancariaEntity entity = data.get(0);
        CuentaBancariaEntity pojoEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setNumeroCuenta("-" + pojoEntity.getNumeroCuenta());
        cuentaBancariaLogic.updateCuentaBancaria(pojoEntity.getId(), pojoEntity);

        Assert.assertEquals("-" + entity.getNumeroCuenta(), pojoEntity.getNumeroCuenta());
    }

    /**
     * Pruba la verificacion de una regla de negocio
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void updateCuentaBancariaNumeroCortoTest() throws BusinessLogicException {
        CuentaBancariaEntity entity = data.get(0);
        CuentaBancariaEntity pojoEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setNumeroCuenta("1234");
        Assert.assertTrue(pojoEntity.getNumeroCuenta().length() < 9);
        cuentaBancariaLogic.updateCuentaBancaria(pojoEntity.getId(), pojoEntity);

    }

    /**
     * Pruba la verificacion de una regla de negocio
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void updateCuentaBancariaNumeroNonaturalTest() throws BusinessLogicException {
        CuentaBancariaEntity entity = data.get(0);
        CuentaBancariaEntity pojoEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        pojoEntity.setId(entity.getId());
        char[] c = pojoEntity.getNumeroCuenta().toCharArray();
        String cadena = ",";

        for (int i = 1; i < c.length; i++) {
            cadena += c[i];

        }

        pojoEntity.setNumeroCuenta(cadena);
        cuentaBancariaLogic.updateCuentaBancaria(pojoEntity.getId(), pojoEntity);

        Assert.assertTrue(pojoEntity.getNumeroCuenta().contains(","));

    }

    /**
     * Pruba la verificacion de una regla de negocio
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void updateCuentaBancariaNumeroNonatural2Test() throws BusinessLogicException {
        CuentaBancariaEntity entity = data.get(0);
        CuentaBancariaEntity pojoEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        pojoEntity.setId(entity.getId());
        char[] c = pojoEntity.getNumeroCuenta().toCharArray();
        String cadena = "-";

        for (int i = 1; i < c.length - 2; i++) {
            cadena += c[i];

        }
        cadena += ",.";
        pojoEntity.setNumeroCuenta(cadena);
        cuentaBancariaLogic.updateCuentaBancaria(pojoEntity.getId(), pojoEntity);

        Assert.assertTrue(pojoEntity.getNumeroCuenta().contains("."));

        Assert.assertTrue(pojoEntity.getNumeroCuenta().contains("-"));

    }

    /**
     * Pruba la verificacion de una regla de negocio
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void updateCuentaBancariaNombreNullTest() throws BusinessLogicException {
        CuentaBancariaEntity entity = data.get(0);
        CuentaBancariaEntity pojoEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setNombreBanco(null);
        cuentaBancariaLogic.updateCuentaBancaria(pojoEntity.getId(), pojoEntity);
        Assert.assertNull(pojoEntity.getNombreBanco());

    }

    /**
     * Pruba la verificacion de una regla de negocio
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void updateCuentaBancariaNombreVacioTest2() throws BusinessLogicException {
        CuentaBancariaEntity entity = data.get(0);
        CuentaBancariaEntity pojoEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setNombreBanco("");
        Assert.assertTrue(pojoEntity.getNombreBanco().equals(""));
        cuentaBancariaLogic.updateCuentaBancaria(pojoEntity.getId(), pojoEntity);

    }

    /**
     * Prueba para consultar la lista de cuentas de Bancos.
     */
    @Test
    public void getCuentasBancariasTest() {
        List<CuentaBancariaEntity> list = cuentaBancariaLogic.getCuentasBancarias();
        Assert.assertEquals(data.size(), list.size());
        for (CuentaBancariaEntity entity : list) {
            boolean found = false;
            for (CuentaBancariaEntity storedEntity : data) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    /**
     * Prueba para consultar una CuentaBancaria.
     *
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test
    public void getCuentaBancariaTest() throws BusinessLogicException {
        CuentaBancariaEntity entity = data.get(0);
        CuentaBancariaEntity resultEntity = cuentaBancariaLogic.getCuentaBancaria(entity.getId());
        Assert.assertNotNull(resultEntity);
        Assert.assertEquals(entity.getId(), resultEntity.getId());
        Assert.assertEquals(entity.getNumeroCuenta(), resultEntity.getNumeroCuenta());
    }

    @Test(expected = BusinessLogicException.class)
    public void getCuentaBancariaIDMalTest() throws BusinessLogicException {
        CuentaBancariaEntity entity = data.get(0);
        CuentaBancariaEntity resultEntity = cuentaBancariaLogic.getCuentaBancaria(entity.getId() * 2);
        Assert.assertNull(resultEntity);

    }

    /**
     * Prueba para eliminar una Cuentabancaria
     *
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test
    public void deleteCuentabancariaTest() throws BusinessLogicException {

        CuentaBancariaEntity entity = data.get(0);
        EstudianteEntity est = estudianteLogic.getEstudiante(cuentaBancariaLogic.getCuentaBancaria(entity.getId()).getEstudiante().getId());

        estudianteLogic.deleteEstudiante(est.getId());
        cuentaBancariaLogic.delete(entity.getId());
        CuentaBancariaEntity deleted = em.find(CuentaBancariaEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }
}
