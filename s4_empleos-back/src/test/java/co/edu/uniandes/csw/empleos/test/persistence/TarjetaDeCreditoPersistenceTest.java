/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.persistence;

import co.edu.uniandes.csw.empleos.entities.TarjetaDeCreditoEntity;
import co.edu.uniandes.csw.empleos.persistence.TarjetaDeCreditoPersistence;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
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

public class TarjetaDeCreditoPersistenceTest {
    
   @Inject
   private TarjetaDeCreditoPersistence tarjetaPersistence;
   
   @PersistenceContext
   private EntityManager em;
   
   @Inject
   UserTransaction utx;
   
   private List<TarjetaDeCreditoEntity> data = new ArrayList<TarjetaDeCreditoEntity>();
   
   
   /**
    * @return Devuelve el jar Arquillian va a desplegar en Payara embebido.
    */
   @Deployment
   public static JavaArchive createDeployment()
   {
       return ShrinkWrap.create(JavaArchive.class)
               .addPackage(TarjetaDeCreditoEntity.class.getPackage())
               .addPackage(TarjetaDeCreditoPersistence.class.getPackage())
               .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
               .addAsManifestResource("META-INF/beans.xml", "beans.xml");
   }
    
   /**
    * Configuracion inicial de la prueba.
    */
   @Before
   public void configTest() {
       try{
           utx.begin();
           em.joinTransaction();
           clearData();
           insertData();
           utx.commit();
       }
       catch (Exception e)
       {
           e.printStackTrace();
           try {
               utx.rollback();
           }
           catch (Exception e1)
           {
               e1.printStackTrace();
           }
       }
   }
   
   /**
    * Limpia las tablas que están implicadas en la prueba.
    */
   private void clearData(){
       em.createQuery("delete from TarjetaDeCreditoEntity").executeUpdate();
   }
   
   /**
    * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
    */
   private void insertData(){
       PodamFactory factory = new PodamFactoryImpl();
       
       for( int i = 0 ; i < 3 ; i++)
       {
           TarjetaDeCreditoEntity entity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
           
           entity.setId(Long.MIN_VALUE);
           
           em.persist(entity);
           
           data.add(entity);
       }
   }
   
   /**
    * Prueba para crear una tarjeta de credito.
    */
   @Test
   public void createTarjetaDeCredito()
   {
       PodamFactory factory = new PodamFactoryImpl();
       TarjetaDeCreditoEntity newEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
       
       newEntity.setId(Long.MIN_VALUE);
        
       TarjetaDeCreditoEntity result = tarjetaPersistence.create(newEntity);
       
       Assert.assertNotNull(result);
       
       TarjetaDeCreditoEntity entity = em.find(TarjetaDeCreditoEntity.class, result.getId());
       
       Assert.assertEquals(newEntity.getNumero(), entity.getNumero());  
   }
   
   /**
    * Prueba para consultar la lista de premios.
    */
   @Test
   public void getTarjetaDeCreditoTest(){
       List<TarjetaDeCreditoEntity> list = tarjetaPersistence.findAll();
       Assert.assertEquals(data.size(), list.size());
       for ( TarjetaDeCreditoEntity ent : list )
       {
           boolean found = false;
           
           for ( TarjetaDeCreditoEntity entity : data )
           {
               if ( ent.getId().equals(entity.getId())) {
                   found = true;
               }
           }
         Assert.assertTrue(found);
       }
   }
   
   /**
    * Prueba para consultar una tarjeta de crédito.
    */
   @Test
   public void getTarjetaTest(){
       TarjetaDeCreditoEntity entity = data.get(0);
       TarjetaDeCreditoEntity newEntity = tarjetaPersistence.find(entity.getId());
       Assert.assertNotNull(newEntity);
       Assert.assertEquals(entity.getId(), newEntity.getId());
   }
   
   /**
    * Prueba para eliminar una tarjeta de credito.
    */
   @Test
   public void deleteTarjetaCreditoTest(){
       TarjetaDeCreditoEntity entity = data.get(0);
       tarjetaPersistence.delete(entity.getId());
       TarjetaDeCreditoEntity deleted = em.find(TarjetaDeCreditoEntity.class, entity.getId());
       Assert.assertNull(deleted);
   }
   
   /**
    * Prueba para actualizar una tarjeta de credito.
    */
   @Test
   public void updateTarjetaCreditoTest(){
       TarjetaDeCreditoEntity entity = data.get(0);
       PodamFactory factory = new PodamFactoryImpl();
       TarjetaDeCreditoEntity newEntity = factory.manufacturePojo(TarjetaDeCreditoEntity.class);
       
       newEntity.setId(entity.getId());
       
       tarjetaPersistence.update(newEntity);
       
       TarjetaDeCreditoEntity resp = em.find(TarjetaDeCreditoEntity.class, entity.getId());
       
       Assert.assertEquals(newEntity.getId(), resp.getId());
       
   }
}

