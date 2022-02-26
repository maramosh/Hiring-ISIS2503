/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.logic;

import co.edu.uniandes.csw.empleos.ejb.ContratistaLogic;
import co.edu.uniandes.csw.empleos.entities.ContratistaEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
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
 * Pruebas de logica de Contratistas
 *
 * @contratista ISIS2603
 */
@RunWith(Arquillian.class)
public class ContratistaLogicTest {
    
    

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private ContratistaLogic contratistaLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private List<ContratistaEntity> data = new ArrayList<>();

    /**
     * @return Devuelve el jar que Arquillian va a desplegar en Payara embebido.
     * El jar contiene las clases, el descriptor de la base de datos y el
     * archivo beans.xml para resolver la inyección de dependencias.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(ContratistaEntity.class.getPackage())
                .addPackage(ContratistaLogic.class.getPackage())
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
        for (int i = 0; i < 3; i++) {
            ContratistaEntity entity = factory.manufacturePojo(ContratistaEntity.class);
            em.persist(entity);
            
            data.add(entity);
        }
        
        
        
    }

    /**
     * Prueba para crear un Contratista.
     */
    @Test
    public void createContratistaTest() throws BusinessLogicException {
        ContratistaEntity newEntity = factory.manufacturePojo(ContratistaEntity.class);
        newEntity.setEmail("jek@hotmail.com");
        ContratistaEntity result = contratistaLogic.createContratista(newEntity);
        result.setEmail("jek@hotmail.com");
        
        Assert.assertNotNull(result);
        ContratistaEntity entity = em.find(ContratistaEntity.class, result.getId());
        Assert.assertEquals(newEntity.getId(), entity.getId());
        Assert.assertEquals(newEntity.getEmail(), entity.getEmail());
        Assert.assertEquals(newEntity.getEsExterno(), entity.getEsExterno());
        Assert.assertEquals(newEntity.getNombre(), entity.getNombre());
        Assert.assertEquals(newEntity.getRutaImagen(), entity.getRutaImagen());
    }

    /**
     * Prueba para verificar la generacion de la excepcion al crear un contratista con nombre nulo.
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createContratistaNombreNullTest() throws BusinessLogicException {
        ContratistaEntity newEntity = factory.manufacturePojo(ContratistaEntity.class);
        newEntity.setNombre(null);
         contratistaLogic.createContratista(newEntity);
    }
    
    
    /**
     * Prueba para verificar la generacion de la excepcion al crear un contratista con una contraseña mayor a 5 caracteres.
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createContratistaCorreoInvalidoTest() throws BusinessLogicException {
        ContratistaEntity newEntity = factory.manufacturePojo(ContratistaEntity.class);
        newEntity.setEmail("asdfghj");
         contratistaLogic.createContratista(newEntity);
    }
    /**
     * Prueba para consultar la lista de Contratistas.
     */
    @Test
    public void getContratistasTest() {
        List<ContratistaEntity> list = contratistaLogic.getContratistas();
        Assert.assertEquals(data.size(), list.size());
        for (ContratistaEntity entity : list) {
            boolean found = false;
            for (ContratistaEntity storedEntity : data) {
                if (entity.getId().equals(storedEntity.getId())) {
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
    public void getContratistaTest() throws BusinessLogicException {
        ContratistaEntity entity = data.get(0);
        ContratistaEntity resultEntity = contratistaLogic.getContratista(entity.getId());
        Assert.assertNotNull(resultEntity);
        
        Assert.assertEquals(entity.getId(), resultEntity.getId());
       
        Assert.assertEquals(entity.getEmail(), resultEntity.getEmail());
        Assert.assertEquals(entity.getEsExterno(), resultEntity.getEsExterno());
        Assert.assertEquals(entity.getNombre(), resultEntity.getNombre());
        Assert.assertEquals(entity.getRutaImagen(), resultEntity.getRutaImagen());
    }

    /**
     * Prueba para actualizar un Contratista.
     */
    @Test
    public void updateContratistaTest() {
        ContratistaEntity entity = data.get(0);
        ContratistaEntity pojoEntity = factory.manufacturePojo(ContratistaEntity.class);

        pojoEntity.setId(entity.getId());

        contratistaLogic.updateContratista(pojoEntity.getId(), pojoEntity);

        ContratistaEntity resp = em.find(ContratistaEntity.class, entity.getId());

        Assert.assertEquals(pojoEntity.getId(), resp.getId());
       
        Assert.assertEquals(pojoEntity.getEmail(), resp.getEmail());
        Assert.assertEquals(pojoEntity.getEsExterno(), resp.getEsExterno());
        Assert.assertEquals(pojoEntity.getNombre(), resp.getNombre());
        Assert.assertEquals(pojoEntity.getRutaImagen(), resp.getRutaImagen());
    }
    

    /**
     * Prueba para eliminar un Contratista
     *
     * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
     */
    @Test
    public void deleteContratistaTest() throws BusinessLogicException {
        ContratistaEntity entity = data.get(0);
        contratistaLogic.deleteContratista(entity.getId());
        ContratistaEntity deleted = em.find(ContratistaEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

   
    
    
}
