/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.persistence;

import co.edu.uniandes.csw.empleos.entities.CalificacionEntity;
import co.edu.uniandes.csw.empleos.entities.CuentaBancariaEntity;
import co.edu.uniandes.csw.empleos.entities.EstudianteEntity;
import co.edu.uniandes.csw.empleos.entities.OfertaEntity;
import co.edu.uniandes.csw.empleos.persistence.CalificacionPersistence;
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
 *Clase de los test de que corresponden a las pruebas realizadas sobre los metodos de la Clase CalificacionPersistnence
 * @author Estudiante
 */
@RunWith(Arquillian.class)
public class CalificacionPersistenceTest {
    
       
    @PersistenceContext(unitName = "empleosPU")
    protected EntityManager em;
     
    @Inject
    CalificacionPersistence cp;    
    
    @Inject
    UserTransaction utx;
    
    private List<CalificacionEntity> data = new ArrayList<CalificacionEntity>();
    
      /**
     * @return Devuelve el jar que Arquillian va a desplegar en Payara embebido.
     * El jar contiene las clases, el descriptor de la base de datos y el
     * archivo beans.xml para resolver la inyección de dependencias.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(CalificacionEntity.class.getPackage())
                .addPackage(CalificacionPersistence.class.getPackage())
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
        em.createQuery("delete from CalificacionEntity").executeUpdate();     
    }
    
    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {
        PodamFactory factory = new PodamFactoryImpl();

        for (int i = 0; i < 3; i++) {
            CalificacionEntity entity = factory.manufacturePojo(CalificacionEntity.class);
                                  
            em.persist(entity);
            data.add(entity);
        }
    }
    
    /**
     * Prueba para crear una Calificacion.
     */
    @Test
    public void createCalificacionTest() {
        PodamFactory factory = new PodamFactoryImpl();
        CalificacionEntity calificacion = factory.manufacturePojo(CalificacionEntity.class);
        CalificacionEntity result = cp.create(calificacion);

        Assert.assertNotNull(result);

        CalificacionEntity entity = em.find(CalificacionEntity.class, result.getId());

       /**
        * Prueba para crear el comentario y encontrarlo
        */
        Assert.assertEquals(calificacion.getComentario(), entity.getComentario());
        
        /**
         * Pueba para crear la nota y enrlacontrarla
         */
        Assert.assertEquals(calificacion.getNota(), entity.getNota());
        //Assert.assertEquals(calificacion.getEstudiante(),entity.getEstudiante());
 
    }
    
    /**
     * Prueba para consultar una lista de Calificaciones.
     */
    @Test
    public void getCalificacionesTest() {
        
        
        List<CalificacionEntity> list = cp.findAll();

        Assert.assertEquals(data.size(), list.size());

        for(CalificacionEntity ent : list){
            boolean found = false;
            for(CalificacionEntity entity : data){
          
            if(ent.getId().equals(entity.getId())){
                found = true;
                }
            }
            Assert.assertTrue(found);
        }

    }
    
    /**
     * Prueba para consultar una Calificacion.
     */
    @Test
    public void getCalificacionTest() {
        CalificacionEntity entity = data.get(0);
        CalificacionEntity newEntity = cp.find(entity.getId());
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(entity.getNota(), newEntity.getNota());
        Assert.assertEquals(entity.getComentario(), newEntity.getComentario());
        //Assert.assertEquals(entity.getEstudiante(), newEntity.getEstudiante());
    }
    
    /**
     * Prueba para eliminar una Calificacion.
     */
    @Test
    public void deleteCalificaionTest() {
        CalificacionEntity entity = data.get(0);
        cp.delete(entity.getId());
        CalificacionEntity deleted = em.find(CalificacionEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }
    
    /**
     * Prueba para actualizar una Calificacion.
     */
    @Test
    public void updateCalificacionTest() {
        CalificacionEntity entity = data.get(0);
        PodamFactory factory = new PodamFactoryImpl();
        CalificacionEntity newEntity = factory.manufacturePojo(CalificacionEntity.class);

        newEntity.setId(entity.getId());

        cp.update(newEntity);

        CalificacionEntity resp = em.find(CalificacionEntity.class, entity.getId());

        Assert.assertEquals(newEntity.getNota(), resp.getNota());
        Assert.assertEquals(newEntity.getComentario(), resp.getComentario());
        //Assert.assertEquals(newEntity.getEstudiante(), resp.getEstudiante());
    }
}
