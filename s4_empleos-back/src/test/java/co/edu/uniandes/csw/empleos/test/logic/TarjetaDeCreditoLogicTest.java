/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.logic;

import co.edu.uniandes.csw.empleos.ejb.TarjetaDeCreditoLogic;
import co.edu.uniandes.csw.empleos.entities.TarjetaDeCreditoEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.TarjetaDeCreditoPersistence;
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
 * @author Miguel Angel Ramos Hurtado
 */
@RunWith(Arquillian.class)
public class TarjetaDeCreditoLogicTest {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private UserTransaction utx;

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private TarjetaDeCreditoLogic tarjetaCreditoLogic;
    
    private List<TarjetaDeCreditoEntity> data = new ArrayList<TarjetaDeCreditoEntity>();
    


    /**
     * @return Devuelve el jar Arquillian va a desplegar en Payara embebido.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(TarjetaDeCreditoEntity.class.getPackage())
                .addPackage(TarjetaDeCreditoLogic.class.getPackage())
                .addPackage(TarjetaDeCreditoPersistence.class.getPackage())
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
        em.createQuery("delete from TarjetaDeCreditoEntity").executeUpdate();

    }
    
     /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {
        PodamFactory factory = new PodamFactoryImpl();
        for (int i = 0; i < 3; i++) {
            TarjetaDeCreditoEntity entity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
            em.persist(entity);
            data.add(entity);
        }
    }

    @Test
    public void createTarjetaDeCredito() throws BusinessLogicException {
        TarjetaDeCreditoEntity newEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        newEntity.setNumero("4234567891234567");
        newEntity.setCVC("123");
        newEntity.setFecha("12/12");
        TarjetaDeCreditoEntity result = tarjetaCreditoLogic.createTarjetaDeCredito(newEntity);
        Assert.assertNotNull(result);

        TarjetaDeCreditoEntity entity = em.find(TarjetaDeCreditoEntity.class, result.getId());
        Assert.assertEquals(entity.getNumero(), result.getNumero());
    }
    
    @Test(expected = BusinessLogicException.class)
    public void createTarjetaDeCreditoNoVisaNoMaster() throws BusinessLogicException {
        TarjetaDeCreditoEntity newEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        newEntity.setNumero("0234567890123456");
        TarjetaDeCreditoEntity result = tarjetaCreditoLogic.createTarjetaDeCredito(newEntity);
        Assert.assertNotNull(result);
    }
    
    @Test(expected = BusinessLogicException.class)
    public void createTarjetaDeCreditoNumeroRepetido() throws BusinessLogicException {
        
        TarjetaDeCreditoEntity newEntity1 = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        newEntity1.setNumero("4234567891234567");
        tarjetaCreditoLogic.createTarjetaDeCredito(newEntity1);
        Assert.assertNotNull(tarjetaCreditoLogic.getTarjetaCredito(newEntity1.getId()));
        
        TarjetaDeCreditoEntity newEntity2 = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        newEntity2.setNumero("4234567891234567");
        TarjetaDeCreditoEntity result = tarjetaCreditoLogic.createTarjetaDeCredito(newEntity2);
        Assert.assertNull(tarjetaCreditoLogic.getTarjetaCredito(newEntity2.getId()));
    }

    @Test(expected = BusinessLogicException.class)
    public void createTarjetaDeCreditoNumeroNull() throws BusinessLogicException {
        TarjetaDeCreditoEntity newEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        newEntity.setNumero(null);
        TarjetaDeCreditoEntity result = tarjetaCreditoLogic.createTarjetaDeCredito(newEntity);
        Assert.assertNull(result.getNumero());
    }
    
    @Test (expected = BusinessLogicException.class)
    public void createTarjetaDeCreditoLenght() throws BusinessLogicException {
        TarjetaDeCreditoEntity newEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        newEntity.setNumero("42345678912345677");
        TarjetaDeCreditoEntity result = tarjetaCreditoLogic.createTarjetaDeCredito(newEntity);
        Assert.assertTrue(result.getNumero().length() > 16 );
    }
    
    @Test (expected = BusinessLogicException.class)
    public void createTarjetaDeCreditoNumeroVacio() throws BusinessLogicException {
        TarjetaDeCreditoEntity newEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        newEntity.setNumero("");
        TarjetaDeCreditoEntity result = tarjetaCreditoLogic.createTarjetaDeCredito(newEntity);
        Assert.assertTrue(result.getNumero().length() == 0);
    }
    
    @Test 
    public void createTarjetaDeCreditoCVC() throws BusinessLogicException{
        TarjetaDeCreditoEntity newEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        newEntity.setNumero("5234567890123456");
        newEntity.setCVC("132");
        newEntity.setFecha("12/12");
        TarjetaDeCreditoEntity result = tarjetaCreditoLogic.createTarjetaDeCredito(newEntity);
        Assert.assertNotNull(result);

        TarjetaDeCreditoEntity entity = em.find(TarjetaDeCreditoEntity.class, result.getId());
        Assert.assertEquals(entity.getCVC(), result.getCVC());
    }
    
    @Test (expected = BusinessLogicException.class)
    public void createTarjetaDeCreditoNullCVC() throws BusinessLogicException
    {
        TarjetaDeCreditoEntity newEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        newEntity.setNumero("5234567890123456");
        newEntity.setFecha("12/12");
        newEntity.setCVC(null);
        TarjetaDeCreditoEntity result = tarjetaCreditoLogic.createTarjetaDeCredito(newEntity);
        Assert.assertNull(result.getCVC());
    }
    
    
    @Test (expected = BusinessLogicException.class)
    public void createTarjetaDeCreditoMalCVC() throws BusinessLogicException {
        TarjetaDeCreditoEntity newEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        newEntity.setNumero("5234567890123456");
        newEntity.setFecha("12/12");
        newEntity.setCVC("12345");
        TarjetaDeCreditoEntity result = tarjetaCreditoLogic.createTarjetaDeCredito(newEntity);
        Assert.assertTrue(result.getCVC().length() != 3);
    }
    
    @Test (expected = BusinessLogicException.class)
    public void createTarjetaDeCreditoVacioCVC() throws BusinessLogicException {
        TarjetaDeCreditoEntity newEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        newEntity.setNumero("5234567890123456");
        newEntity.setFecha("12/12");
        newEntity.setCVC("");
        TarjetaDeCreditoEntity result = tarjetaCreditoLogic.createTarjetaDeCredito(newEntity);
        Assert.assertTrue(result.getCVC().length() == 0);
    }
    
    @Test
    public void createTarjetaDeCreditoFecha() throws BusinessLogicException {
        TarjetaDeCreditoEntity newEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        newEntity.setNumero("4234567890123456");
        newEntity.setCVC("123");
        newEntity.setFecha("12/12");
        TarjetaDeCreditoEntity result = tarjetaCreditoLogic.createTarjetaDeCredito(newEntity);
        Assert.assertNotNull(result);

        TarjetaDeCreditoEntity entity = em.find(TarjetaDeCreditoEntity.class, result.getId());
        Assert.assertEquals(entity.getFecha(), result.getFecha());
    }
    
    @Test (expected = BusinessLogicException.class)
    public void createTarjetaDeCreditoFechaNull() throws BusinessLogicException
    {
        TarjetaDeCreditoEntity newEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        newEntity.setNumero("4234567890123456");
        newEntity.setCVC("123");
        newEntity.setFecha(null);
        TarjetaDeCreditoEntity result = tarjetaCreditoLogic.createTarjetaDeCredito(newEntity);
        Assert.assertNull(result.getFecha());
    }
    
    @Test (expected = BusinessLogicException.class)
    public void createTarjetaDeCreditoFechaVacia() throws BusinessLogicException
    {
        TarjetaDeCreditoEntity newEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        newEntity.setNumero("4234567890123456");
        newEntity.setCVC("123");
        newEntity.setFecha("");
        TarjetaDeCreditoEntity result = tarjetaCreditoLogic.createTarjetaDeCredito(newEntity);
        Assert.assertTrue(result.getFecha().length() == 0);
    }
    
    @Test (expected = BusinessLogicException.class)
    public void createTarjetaDeCreditoFechaLenght() throws BusinessLogicException
    {
        TarjetaDeCreditoEntity newEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        newEntity.setNumero("4234567890123456");
        newEntity.setCVC("123");
        newEntity.setFecha("12/123");
        TarjetaDeCreditoEntity result = tarjetaCreditoLogic.createTarjetaDeCredito(newEntity);
        Assert.assertTrue(result.getFecha().length() != 5);
    }
    
    @Test
    public void getTarjetaDeCredito() throws BusinessLogicException
    {
        TarjetaDeCreditoEntity entity = data.get(0);
        TarjetaDeCreditoEntity resultEntity = tarjetaCreditoLogic.getTarjetaCredito(entity.getId());
        Assert.assertNotNull(resultEntity);
        
        Assert.assertEquals(entity.getId(), resultEntity.getId());
        Assert.assertEquals(entity.getNumero(), resultEntity.getNumero());
        Assert.assertEquals(entity.getCVC(), resultEntity.getCVC());
        Assert.assertEquals(entity.getFecha(), resultEntity.getFecha());
    }
    
    @Test 
    public void updateTarjetaDeCredito() throws BusinessLogicException{
        TarjetaDeCreditoEntity tarjetaEntity = data.get(0);
        TarjetaDeCreditoEntity pojoEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        
        pojoEntity.setId(tarjetaEntity.getId());
        pojoEntity.setNumero("5234567890123456");
        pojoEntity.setCVC("523");
        pojoEntity.setFecha("12/12");
        tarjetaCreditoLogic.updateTarjetaCredito(pojoEntity.getId(), pojoEntity);
        
        TarjetaDeCreditoEntity result = em.find(TarjetaDeCreditoEntity.class, tarjetaEntity.getId() );
        
        Assert.assertEquals(pojoEntity.getId(), result.getId());
        Assert.assertEquals(pojoEntity.getNumero(), result.getNumero());
        Assert.assertEquals(pojoEntity.getCVC(), result.getCVC());
        Assert.assertEquals(pojoEntity.getFecha(), result.getFecha());
    }
    
    @Test (expected = BusinessLogicException.class)
    public void updateTarjetaDeCreditoNumeroLenght() throws BusinessLogicException{
        TarjetaDeCreditoEntity tarjetaEntity = data.get(0);
        TarjetaDeCreditoEntity pojoEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        
        pojoEntity.setId(tarjetaEntity.getId());
        pojoEntity.setNumero("52345678901234561");
        pojoEntity.setCVC("523");
        pojoEntity.setFecha("12/12");
        tarjetaCreditoLogic.updateTarjetaCredito(pojoEntity.getId(), pojoEntity);
        
        TarjetaDeCreditoEntity result = em.find(TarjetaDeCreditoEntity.class, tarjetaEntity.getId() );
        
        Assert.assertEquals(pojoEntity.getId(), result.getId());
        Assert.assertEquals(pojoEntity.getNumero(), result.getNumero());
        Assert.assertEquals(pojoEntity.getCVC(), result.getCVC());
        Assert.assertEquals(pojoEntity.getFecha(), result.getFecha());
    }
    
    @Test (expected = BusinessLogicException.class)
    public void updateTarjetaDeCreditoNumeroNull() throws BusinessLogicException{
        TarjetaDeCreditoEntity tarjetaEntity = data.get(0);
        TarjetaDeCreditoEntity pojoEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        
        pojoEntity.setId(tarjetaEntity.getId());
        pojoEntity.setNumero(null);
        pojoEntity.setCVC("523");
        pojoEntity.setFecha("12/12");
        tarjetaCreditoLogic.updateTarjetaCredito(pojoEntity.getId(), pojoEntity);
        
        TarjetaDeCreditoEntity result = em.find(TarjetaDeCreditoEntity.class, tarjetaEntity.getId() );
        
        Assert.assertEquals(pojoEntity.getId(), result.getId());
        Assert.assertEquals(pojoEntity.getNumero(), result.getNumero());
        Assert.assertEquals(pojoEntity.getCVC(), result.getCVC());
        Assert.assertEquals(pojoEntity.getFecha(), result.getFecha());
    }
    
    @Test (expected = BusinessLogicException.class)
    public void updateTarjetaDeCreditoNumeroVacio() throws BusinessLogicException{
        TarjetaDeCreditoEntity tarjetaEntity = data.get(0);
        TarjetaDeCreditoEntity pojoEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        
        pojoEntity.setId(tarjetaEntity.getId());
        pojoEntity.setNumero("");
        pojoEntity.setCVC("523");
        pojoEntity.setFecha("12/12");
        tarjetaCreditoLogic.updateTarjetaCredito(pojoEntity.getId(), pojoEntity);
        
        TarjetaDeCreditoEntity result = em.find(TarjetaDeCreditoEntity.class, tarjetaEntity.getId() );
        
        Assert.assertEquals(pojoEntity.getId(), result.getId());
        Assert.assertEquals(pojoEntity.getNumero(), result.getNumero());
        Assert.assertEquals(pojoEntity.getCVC(), result.getCVC());
        Assert.assertEquals(pojoEntity.getFecha(), result.getFecha());
    }
    
    @Test (expected = BusinessLogicException.class)
    public void updateTarjetaDeCreditoNumeroMal() throws BusinessLogicException{
        TarjetaDeCreditoEntity tarjetaEntity = data.get(0);
        TarjetaDeCreditoEntity pojoEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        
        pojoEntity.setId(tarjetaEntity.getId());
        pojoEntity.setNumero("2234567890123456");
        pojoEntity.setCVC("523");
        pojoEntity.setFecha("12/12");
        tarjetaCreditoLogic.updateTarjetaCredito(pojoEntity.getId(), pojoEntity);
        
        TarjetaDeCreditoEntity result = em.find(TarjetaDeCreditoEntity.class, tarjetaEntity.getId() );
        
        Assert.assertEquals(pojoEntity.getId(), result.getId());
        Assert.assertEquals(pojoEntity.getNumero(), result.getNumero());
        Assert.assertEquals(pojoEntity.getCVC(), result.getCVC());
        Assert.assertEquals(pojoEntity.getFecha(), result.getFecha());
    }
    
    @Test (expected = BusinessLogicException.class)
    public void updateTarjetaDeCreditoCVCLenght() throws BusinessLogicException{
        TarjetaDeCreditoEntity tarjetaEntity = data.get(0);
        TarjetaDeCreditoEntity pojoEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        
        pojoEntity.setId(tarjetaEntity.getId());
        pojoEntity.setNumero("5234567890123456");
        pojoEntity.setCVC("5231");
        pojoEntity.setFecha("12/12");
        tarjetaCreditoLogic.updateTarjetaCredito(pojoEntity.getId(), pojoEntity);
        
        TarjetaDeCreditoEntity result = em.find(TarjetaDeCreditoEntity.class, tarjetaEntity.getId() );
        
        Assert.assertEquals(pojoEntity.getId(), result.getId());
        Assert.assertEquals(pojoEntity.getNumero(), result.getNumero());
        Assert.assertEquals(pojoEntity.getCVC(), result.getCVC());
        Assert.assertEquals(pojoEntity.getFecha(), result.getFecha());
    }
    
    @Test (expected = BusinessLogicException.class)
    public void updateTarjetaDeCreditoCVCVacio() throws BusinessLogicException{
        TarjetaDeCreditoEntity tarjetaEntity = data.get(0);
        TarjetaDeCreditoEntity pojoEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        
        pojoEntity.setId(tarjetaEntity.getId());
        pojoEntity.setNumero("5234567890123456");
        pojoEntity.setCVC("");
        pojoEntity.setFecha("12/12");
        tarjetaCreditoLogic.updateTarjetaCredito(pojoEntity.getId(), pojoEntity);
        
        TarjetaDeCreditoEntity result = em.find(TarjetaDeCreditoEntity.class, tarjetaEntity.getId() );
        
        Assert.assertEquals(pojoEntity.getId(), result.getId());
        Assert.assertEquals(pojoEntity.getNumero(), result.getNumero());
        Assert.assertEquals(pojoEntity.getCVC(), result.getCVC());
        Assert.assertEquals(pojoEntity.getFecha(), result.getFecha());
    }
    
    @Test (expected = BusinessLogicException.class)
    public void updateTarjetaDeCreditoCVCNulo() throws BusinessLogicException{
        TarjetaDeCreditoEntity tarjetaEntity = data.get(0);
        TarjetaDeCreditoEntity pojoEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        
        pojoEntity.setId(tarjetaEntity.getId());
        pojoEntity.setNumero("5234567890123456");
        pojoEntity.setCVC(null);
        pojoEntity.setFecha("12/12");
        tarjetaCreditoLogic.updateTarjetaCredito(pojoEntity.getId(), pojoEntity);
        
        TarjetaDeCreditoEntity result = em.find(TarjetaDeCreditoEntity.class, tarjetaEntity.getId() );
        
        Assert.assertEquals(pojoEntity.getId(), result.getId());
        Assert.assertEquals(pojoEntity.getNumero(), result.getNumero());
        Assert.assertEquals(pojoEntity.getCVC(), result.getCVC());
        Assert.assertEquals(pojoEntity.getFecha(), result.getFecha());
    }
    
    @Test (expected = BusinessLogicException.class)
    public void updateTarjetaDeCreditoFechaLenght() throws BusinessLogicException{
        TarjetaDeCreditoEntity tarjetaEntity = data.get(0);
        TarjetaDeCreditoEntity pojoEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        
        pojoEntity.setId(tarjetaEntity.getId());
        pojoEntity.setNumero("5234567890123456");
        pojoEntity.setCVC("523");
        pojoEntity.setFecha("12/121");
        tarjetaCreditoLogic.updateTarjetaCredito(pojoEntity.getId(), pojoEntity);
        
        TarjetaDeCreditoEntity result = em.find(TarjetaDeCreditoEntity.class, tarjetaEntity.getId() );
        
        Assert.assertEquals(pojoEntity.getId(), result.getId());
        Assert.assertEquals(pojoEntity.getNumero(), result.getNumero());
        Assert.assertEquals(pojoEntity.getCVC(), result.getCVC());
        Assert.assertEquals(pojoEntity.getFecha(), result.getFecha());
    }
    
    @Test (expected = BusinessLogicException.class)
    public void updateTarjetaDeCreditoFechaVacia() throws BusinessLogicException{
        TarjetaDeCreditoEntity tarjetaEntity = data.get(0);
        TarjetaDeCreditoEntity pojoEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        
        pojoEntity.setId(tarjetaEntity.getId());
        pojoEntity.setNumero("5234567890123456");
        pojoEntity.setCVC("523");
        pojoEntity.setFecha("");
        tarjetaCreditoLogic.updateTarjetaCredito(pojoEntity.getId(), pojoEntity);
        
        TarjetaDeCreditoEntity result = em.find(TarjetaDeCreditoEntity.class, tarjetaEntity.getId() );
        
        Assert.assertEquals(pojoEntity.getId(), result.getId());
        Assert.assertEquals(pojoEntity.getNumero(), result.getNumero());
        Assert.assertEquals(pojoEntity.getCVC(), result.getCVC());
        Assert.assertEquals(pojoEntity.getFecha(), result.getFecha());
    }
    
    @Test (expected = BusinessLogicException.class)
    public void updateTarjetaDeCreditoFechaNull() throws BusinessLogicException{
        TarjetaDeCreditoEntity tarjetaEntity = data.get(0);
        TarjetaDeCreditoEntity pojoEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        
        pojoEntity.setId(tarjetaEntity.getId());
        pojoEntity.setNumero("5234567890123456");
        pojoEntity.setCVC("523");
        pojoEntity.setFecha(null);
        tarjetaCreditoLogic.updateTarjetaCredito(pojoEntity.getId(), pojoEntity);
        
        TarjetaDeCreditoEntity result = em.find(TarjetaDeCreditoEntity.class, tarjetaEntity.getId() );
        
        Assert.assertEquals(pojoEntity.getId(), result.getId());
        Assert.assertEquals(pojoEntity.getNumero(), result.getNumero());
        Assert.assertEquals(pojoEntity.getCVC(), result.getCVC());
        Assert.assertEquals(pojoEntity.getFecha(), result.getFecha());
    }
    
    @Test (expected = BusinessLogicException.class)
    public void updateTarjetaDeCreditoFechaMal() throws BusinessLogicException{
        TarjetaDeCreditoEntity tarjetaEntity = data.get(0);
        TarjetaDeCreditoEntity pojoEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
        
        pojoEntity.setId(tarjetaEntity.getId());
        pojoEntity.setNumero("5234567890123456");
        pojoEntity.setCVC("523");
        pojoEntity.setFecha("12121");
        tarjetaCreditoLogic.updateTarjetaCredito(pojoEntity.getId(), pojoEntity);
        
        TarjetaDeCreditoEntity result = em.find(TarjetaDeCreditoEntity.class, tarjetaEntity.getId() );
        
        Assert.assertEquals(pojoEntity.getId(), result.getId());
        Assert.assertEquals(pojoEntity.getNumero(), result.getNumero());
        Assert.assertEquals(pojoEntity.getCVC(), result.getCVC());
        Assert.assertEquals(pojoEntity.getFecha(), result.getFecha());
    }
    
    @Test
    public void getTarjetasTest() {
        List<TarjetaDeCreditoEntity> list = tarjetaCreditoLogic.getTarjetas();
        Assert.assertEquals(data.size(), list.size());
        for (TarjetaDeCreditoEntity entity : list) {
            boolean found = false;
            for (TarjetaDeCreditoEntity storedEntity : data) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }
    
    @Test
    public void deleteTarjetaTest() throws BusinessLogicException {
        TarjetaDeCreditoEntity entity = data.get(0);
        tarjetaCreditoLogic.deleteTarjetaCredito(entity.getId());
        TarjetaDeCreditoEntity deleted = em.find(TarjetaDeCreditoEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }
    
            
    

}
