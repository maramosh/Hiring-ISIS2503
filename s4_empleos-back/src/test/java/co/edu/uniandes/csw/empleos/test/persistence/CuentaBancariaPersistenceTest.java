
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.persistence;

import co.edu.uniandes.csw.empleos.entities.CuentaBancariaEntity;
import co.edu.uniandes.csw.empleos.entities.EstudianteEntity;
import co.edu.uniandes.csw.empleos.persistence.CuentaBancariaPersistence;
import co.edu.uniandes.csw.empleos.persistence.EstudiantePersistence;
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
 * Pruebas de persistencia de Cuentas
 *
 * @author Estudiante
 */
@RunWith(Arquillian.class)
public class CuentaBancariaPersistenceTest {

    @Inject
    private CuentaBancariaPersistence cuentaBancariaPersistance;

    @Inject
    private EstudiantePersistence estudiantePersistance;

    @PersistenceContext
    private EntityManager em;

    @Inject
    UserTransaction utx;

    private List<CuentaBancariaEntity> data = new ArrayList<CuentaBancariaEntity>();
    private List<EstudianteEntity> dataEst = new ArrayList<EstudianteEntity>();
    /**
     * @return Devuelve el jar que Arquillian va a desplegar en Payara embebido.
     * El jar contiene las clases, el descriptor de la base de datos y el
     * archivo beans.xml para resolver la inyección de dependencias.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(CuentaBancariaEntity.class.getPackage())
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

            estudianteEntity.setCuentaBancaria(entity);
           entity.setEstudiante(estudianteEntity);
            em.persist(entity);
            em.persist(estudianteEntity);
            data.add(entity);
            dataEst.add(estudianteEntity);
        }
    }

    /**
     * Prueba para crear una cuenta bancaria.
     *
     */
    @Test
    public void createCuentaBancariaTest() {
        PodamFactory factory = new PodamFactoryImpl();
        CuentaBancariaEntity newEntity = factory.manufacturePojo(CuentaBancariaEntity.class);
        EstudianteEntity newEstudiante = factory.manufacturePojo(EstudianteEntity.class);
        newEstudiante = estudiantePersistance.create(newEstudiante);
        newEntity.setEstudiante(newEstudiante);
        CuentaBancariaEntity result = cuentaBancariaPersistance.create(newEntity);
        Assert.assertNotNull(result);

        CuentaBancariaEntity entity = em.find(CuentaBancariaEntity.class, result.getId());

        Assert.assertEquals(newEntity.getNumeroCuenta(), entity.getNumeroCuenta());

        Assert.assertEquals(newEntity.getNombreBanco(), entity.getNombreBanco());

        Assert.assertEquals(newEntity.getTipoCuenta(), entity.getTipoCuenta());

    }

    /**
     * Prueba para consultar la lista de cuentas bancarias.
     */
    @Test
    public void getCuentaBancariasTest() {
        List<CuentaBancariaEntity> list = cuentaBancariaPersistance.findAll();
        Assert.assertEquals(data.size(), list.size());
        for (CuentaBancariaEntity ent : list) {
            boolean found = false;
            for (CuentaBancariaEntity entity : data) {
                if (ent.getId().equals(entity.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    /**
     * Prueba para consultar una cuentaBancaria.
     */
    @Test
    public void getCuentaBancariaTest() {
        CuentaBancariaEntity entity = data.get(0);
        CuentaBancariaEntity newEntity = cuentaBancariaPersistance.find(entity.getId());
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(entity.getNumeroCuenta(), newEntity.getNumeroCuenta());
        
    }

    /**
     * Prueba para eliminar una cuentaBancaria.
     */
    @Test
    public void deleteCuentaBancariaTest() {
        CuentaBancariaEntity entity = data.get(0);

        CuentaBancariaEntity x = em.find(CuentaBancariaEntity.class,entity.getId());

        estudiantePersistance.delete(x.getEstudiante().getId());

        cuentaBancariaPersistance.delete(entity.getId());
        CuentaBancariaEntity deleted = em.find(CuentaBancariaEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

    /**
     * Prueba para actualizar una cuentaBancaria.
     */
    @Test
    public void updateCuentaBancariaTest() {
        CuentaBancariaEntity entity = data.get(0);
        PodamFactory factory = new PodamFactoryImpl();
        CuentaBancariaEntity newEntity = factory.manufacturePojo(CuentaBancariaEntity.class);

        newEntity.setId(entity.getId());

        cuentaBancariaPersistance.update(newEntity);

        CuentaBancariaEntity resp = em.find(CuentaBancariaEntity.class, entity.getId());

        Assert.assertEquals(newEntity.getNumeroCuenta(), resp.getNumeroCuenta());

        Assert.assertEquals(newEntity.getNombreBanco(), resp.getNombreBanco());

        Assert.assertEquals(newEntity.getTipoCuenta(), resp.getTipoCuenta());

    }

    /**
     * Prueba para consultasr una CuentaBancaria por numero de cuenta.
     */
    @Test
    public void findCuentaBancariaByNumeroCuentaTest() {
        CuentaBancariaEntity entity = data.get(0);
        CuentaBancariaEntity newEntity = cuentaBancariaPersistance.findByNumero(entity.getNumeroCuenta());
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(entity.getNumeroCuenta(), newEntity.getNumeroCuenta());

        newEntity = cuentaBancariaPersistance.findByNumero(null);
        Assert.assertNull(newEntity);
    }
}
