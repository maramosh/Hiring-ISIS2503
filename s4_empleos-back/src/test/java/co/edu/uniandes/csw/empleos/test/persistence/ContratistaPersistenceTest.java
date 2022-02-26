/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.persistence;

import co.edu.uniandes.csw.empleos.entities.ContratistaEntity;
import co.edu.uniandes.csw.empleos.persistence.ContratistaPersistence;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.junit.Assert;
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
 * Pruebas de persistencia de Contratista
 *
 * @author ISIS2603
 */
@RunWith(Arquillian.class)
public class ContratistaPersistenceTest {
    @Inject
    private ContratistaPersistence contratistaPersistence;

    @PersistenceContext
    private EntityManager em;
    

    @Inject
    UserTransaction utx;
    
    private List<ContratistaEntity> data = new ArrayList<ContratistaEntity>();

    /**
     * @return Devuelve el jar que Arquillian va a desplegar en Payara embebido.
     * El jar contiene las clases, el descriptor de la base de datos y el
     * archivo beans.xml para resolver la inyección de dependencias.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(ContratistaEntity.class.getPackage())
                .addPackage(ContratistaPersistence.class.getPackage())
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
        em.createQuery("delete from ContratistaEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {
        PodamFactory factory = new PodamFactoryImpl();
        for (int i = 0; i < 3; i++) {
            ContratistaEntity entity = factory.manufacturePojo(ContratistaEntity.class);

            em.persist(entity);
            data.add(entity);
        }
    }

    /**
     * Prueba para crear un Book.
     */
    @Test
    public void createContratistaTest() {
        PodamFactory factory = new PodamFactoryImpl();
        ContratistaEntity newEntity = factory.manufacturePojo(ContratistaEntity.class);
        ContratistaEntity result = contratistaPersistence.create(newEntity);
        

        Assert.assertNotNull(result);

        ContratistaEntity entity = em.find(ContratistaEntity.class, result.getId());

        Assert.assertEquals(newEntity.getNombre(), entity.getNombre());
        Assert.assertEquals(newEntity.getEmail(), entity.getEmail());
        Assert.assertEquals(newEntity.getEsExterno(), entity.getEsExterno());
        Assert.assertEquals(newEntity.getRutaImagen(), entity.getRutaImagen());
        
    }

    /**
     * Prueba para consultar la lista de Contratistas.
     */
    @Test
    public void getContratistasTest() {
        List<ContratistaEntity> list = contratistaPersistence.findAll();
        Assert.assertEquals(data.size(), list.size());
        for (ContratistaEntity ent : list) {
            boolean found = false;
            for (ContratistaEntity entity : data) {
                if (ent.getId().equals(entity.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    /**
     * Prueba para consultar un Contratista.
     */
    @Test
    public void getContratistaTest() {
        ContratistaEntity entity = data.get(0);
        ContratistaEntity newEntity = contratistaPersistence.find(entity.getId());
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(entity.getNombre(),newEntity.getNombre());
        Assert.assertEquals(entity.getEmail(), newEntity.getEmail());
        Assert.assertEquals(entity.getEsExterno(), newEntity.getEsExterno());
        Assert.assertEquals(entity.getRutaImagen(), newEntity.getRutaImagen());
        
    }

    /**
     * Prueba para eliminar un Contratista.
     */
    @Test
    public void deleteContratistaTest() {
        ContratistaEntity entity = data.get(0);
        contratistaPersistence.delete(entity.getId());
        ContratistaEntity deleted = em.find(ContratistaEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

    /**
     * Prueba para actualizar un Contratista.
     */
    @Test
    public void updateContratistaTest() {
        ContratistaEntity entity = data.get(0);
        PodamFactory factory = new PodamFactoryImpl();
        ContratistaEntity newEntity = factory.manufacturePojo(ContratistaEntity.class);

        newEntity.setId(entity.getId());

        contratistaPersistence.update(newEntity);

        ContratistaEntity resp = em.find(ContratistaEntity.class, entity.getId());

        Assert.assertEquals(newEntity.getNombre(), resp.getNombre());
        Assert.assertEquals(newEntity.getEmail(), resp.getEmail());
        Assert.assertEquals(newEntity.getEsExterno(), resp.getEsExterno());
        Assert.assertEquals(newEntity.getRutaImagen(), resp.getRutaImagen());
       
    }

    
    
}
