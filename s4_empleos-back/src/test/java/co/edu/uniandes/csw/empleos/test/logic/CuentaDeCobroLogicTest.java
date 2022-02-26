/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.logic;

import co.edu.uniandes.csw.empleos.ejb.ContratistaLogic;
import co.edu.uniandes.csw.empleos.ejb.CuentaDeCobroLogic;
import co.edu.uniandes.csw.empleos.entities.ContratistaEntity;
import co.edu.uniandes.csw.empleos.entities.CuentaDeCobroEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.CuentaDeCobroPersistence;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
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
 * @author Santiago Tangarife Rincón
 */
@RunWith(Arquillian.class)
public class CuentaDeCobroLogicTest {

    @Inject
    private CuentaDeCobroLogic logic;
    
    @Inject
    private ContratistaLogic contratistaLogic;
    
    private PodamFactory factory = new PodamFactoryImpl();

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private List<CuentaDeCobroEntity> cuentas = new ArrayList();

    private List<CuentaDeCobroEntity> data = new ArrayList<CuentaDeCobroEntity>();

    /**
     * @return Devuelve el jar que Arquillian va a desplegar en Payara embebido.
     * El jar contiene las clases, el descriptor de la base de datos y el
     * archivo beans.xml para resolver la inyección de dependencias.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(CuentaDeCobroEntity.class.getPackage())
                .addPackage(CuentaDeCobroLogic.class.getPackage())
                .addPackage(CuentaDeCobroPersistence.class.getPackage())
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
            clearData();
            insertData();
            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Limpia las tablas que están implicadas en la prueba.
     */
    private void clearData() {
        em.createQuery("delete from ContratistaEntity").executeUpdate();
        em.createQuery("delete from CuentaDeCobroEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            CuentaDeCobroEntity cuenta = factory.manufacturePojo(CuentaDeCobroEntity.class);
            em.persist(cuenta);
            cuentas.add(cuenta);
        }
    }

    /**
     * Prueba crear una cuenta de cobro.
     *
     * @throws BusinessLogicException
     */
    @Test
    public void createCuentaDeCobroTest() throws BusinessLogicException {
        CuentaDeCobroEntity newEntity = factory.manufacturePojo(CuentaDeCobroEntity.class);
        newEntity.setNumeroCuentaDeCobro(Math.abs(newEntity.getNumeroCuentaDeCobro()) + 1);
        newEntity.setValor(Math.abs(newEntity.getValor()) + 1);
        ContratistaEntity contratista = factory.manufacturePojo(ContratistaEntity.class);
        contratista.setEmail(contratista.getEmail()+"@hotmail.com");
        ContratistaEntity contratistaGuardado= contratistaLogic.createContratista(contratista);
        newEntity.setContratista(contratistaGuardado);
        newEntity.setValor(Math.abs(newEntity.getValor()));
        CuentaDeCobroEntity result = logic.createCuentaDeCobro(newEntity);
        //Comprueba que la cuenta de cobro retornada del create no sea nula ni sus atributos
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getConcepto());
        Assert.assertNotNull(result.getContratista());
        Assert.assertNotNull(result.getFecha());
        Assert.assertNotNull(result.getNombreEstudiante());
        Assert.assertNotNull(result.getNumeroCuentaDeCobro());
        Assert.assertNotNull(result.getValor());
        
        CuentaDeCobroEntity entity = em.find(CuentaDeCobroEntity.class, result.getId());
        //Comprueba que los valores sean iguales despues de crearla
        Assert.assertEquals(newEntity.getId(), entity.getId());
        Assert.assertEquals(newEntity.getConcepto(), entity.getConcepto());
        Assert.assertEquals(newEntity.getContratista(), entity.getContratista());
        Assert.assertEquals(newEntity.getFecha(), entity.getFecha());
        Assert.assertEquals(newEntity.getNombreEstudiante(), entity.getNombreEstudiante());
        Assert.assertEquals(newEntity.getNumeroCuentaDeCobro(), entity.getNumeroCuentaDeCobro());
        Assert.assertEquals(newEntity.getValor(), entity.getValor());
    }

    @Test(expected = BusinessLogicException.class)
    public void createCuentaDeCobroNula() throws BusinessLogicException {
        logic.createCuentaDeCobro(null);//crea una cuenta nula
    }
    
    @Test(expected = BusinessLogicException.class)
    public void createCuentaDeCobroConceptoNulo() throws BusinessLogicException {
        CuentaDeCobroEntity entity = factory.manufacturePojo(CuentaDeCobroEntity.class);
        entity.setNumeroCuentaDeCobro(Math.abs(entity.getNumeroCuentaDeCobro()) + 1);
        entity.setValor(Math.abs(entity.getValor()) + 1);
        entity.setConcepto(null);
        logic.createCuentaDeCobro(entity);
    }
    
    @Test(expected = BusinessLogicException.class)
    public void createCuentaDeCobroContratistaNulo() throws BusinessLogicException {
        CuentaDeCobroEntity entity = factory.manufacturePojo(CuentaDeCobroEntity.class);
        entity.setNumeroCuentaDeCobro(Math.abs(entity.getNumeroCuentaDeCobro()) + 1);
        entity.setValor(Math.abs(entity.getValor()) + 1);
        entity.setContratista(null);
        logic.createCuentaDeCobro(entity);
    }
    
    @Test(expected = BusinessLogicException.class)
    public void createCuentaDeCobroFechaNula() throws BusinessLogicException {
        CuentaDeCobroEntity entity = factory.manufacturePojo(CuentaDeCobroEntity.class);
        entity.setNumeroCuentaDeCobro(Math.abs(entity.getNumeroCuentaDeCobro()) + 1);
        entity.setValor(Math.abs(entity.getValor()) + 1);
        entity.setFecha(null);
        logic.createCuentaDeCobro(entity);
    }
    
    @Test(expected = BusinessLogicException.class)
    public void createCuentaDeCobroConceptoVacio() throws BusinessLogicException {
        CuentaDeCobroEntity entity = factory.manufacturePojo(CuentaDeCobroEntity.class);
        entity.setNumeroCuentaDeCobro(Math.abs(entity.getNumeroCuentaDeCobro()) + 1);
        entity.setValor(Math.abs(entity.getValor()) + 1);
        entity.setConcepto("");
        logic.createCuentaDeCobro(entity);
    }
    
    @Test(expected = BusinessLogicException.class)
    public void createCuentaDeCobroNombreEstudianteNulo () throws BusinessLogicException {
        CuentaDeCobroEntity entity = factory.manufacturePojo(CuentaDeCobroEntity.class);
        entity.setNumeroCuentaDeCobro(Math.abs(entity.getNumeroCuentaDeCobro()) + 1);
        entity.setValor(Math.abs(entity.getValor()) + 1);
        entity.setNombreEstudiante(null);
        logic.createCuentaDeCobro(entity);
    }
    
    @Test(expected = BusinessLogicException.class)
    public void createCuentaDeCobroNombreEstudianteVacio () throws BusinessLogicException {
        CuentaDeCobroEntity entity = factory.manufacturePojo(CuentaDeCobroEntity.class);
        entity.setNumeroCuentaDeCobro(Math.abs(entity.getNumeroCuentaDeCobro()) + 1);
        entity.setValor(Math.abs(entity.getValor()) + 1);
        entity.setNombreEstudiante("");
        logic.createCuentaDeCobro(entity);
    }
    
    @Test(expected = BusinessLogicException.class)
    public void createCuentaDeCobroNumeroCuentaDeCobroErroneo () throws BusinessLogicException {
        CuentaDeCobroEntity entity = factory.manufacturePojo(CuentaDeCobroEntity.class);
        entity.setValor(Math.abs(entity.getValor()) + 1);
        entity.setNumeroCuentaDeCobro(-1);
        logic.createCuentaDeCobro(entity);
    }
    
    @Test(expected = BusinessLogicException.class)
    public void createCuentaDeCobroValorErroneo () throws BusinessLogicException {
        CuentaDeCobroEntity entity = factory.manufacturePojo(CuentaDeCobroEntity.class);
        entity.setNumeroCuentaDeCobro(Math.abs(entity.getNumeroCuentaDeCobro()) + 1);
        logic.createCuentaDeCobro(entity);
    }
    
     /**
     * Prueba para consultar una cuenta de cobro.
     */
    @Test
    public void getCuentaTest() {
        CuentaDeCobroEntity entity = factory.manufacturePojo(CuentaDeCobroEntity.class);
        entity.setNumeroCuentaDeCobro(Math.abs(entity.getNumeroCuentaDeCobro()) + 1);
        entity.setValor(Math.abs(entity.getValor()) + 1);
        try {
            utx.begin();
            em.persist(entity);
            utx.commit();
        } catch (NotSupportedException ex) {
            Logger.getLogger(CuentaDeCobroLogicTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            Logger.getLogger(CuentaDeCobroLogicTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackException ex) {
            Logger.getLogger(CuentaDeCobroLogicTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicMixedException ex) {
            Logger.getLogger(CuentaDeCobroLogicTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicRollbackException ex) {
            Logger.getLogger(CuentaDeCobroLogicTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(CuentaDeCobroLogicTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(CuentaDeCobroLogicTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        CuentaDeCobroEntity resultEntity = logic.getCuenta(entity.getId());
        Assert.assertNotNull("result nulo",resultEntity);
        Assert.assertEquals("id diferente",entity.getId(), resultEntity.getId());
        Assert.assertEquals("concepto diferente",entity.getConcepto(), resultEntity.getConcepto());
        Assert.assertEquals("contratista diferente",entity.getContratista(), resultEntity.getContratista());
        Assert.assertEquals("fecha diferente",entity.getFecha(), resultEntity.getFecha());
        Assert.assertEquals("nombre estudiante diferente",entity.getNombreEstudiante(), resultEntity.getNombreEstudiante());
        Assert.assertEquals("numero diferente",entity.getNumeroCuentaDeCobro(), resultEntity.getNumeroCuentaDeCobro());
        Assert.assertEquals("valor diferente",entity.getValor(), resultEntity.getValor());
    }
}