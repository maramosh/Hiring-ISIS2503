/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.persistence;

import co.edu.uniandes.csw.empleos.entities.TokenEntity;
import co.edu.uniandes.csw.empleos.persistence.TokenPersistence;
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
 * @author Nicolas Munar
 */
@RunWith(Arquillian.class)
public class TokenPersistenceTest {
    @PersistenceContext(unitName = "empleosPU")
    protected EntityManager em;
     
    @Inject
    TokenPersistence cp;    
    
    @Inject
    UserTransaction utx;
    
    private List<TokenEntity> data = new ArrayList<TokenEntity>();
    
      /**
     * @return Devuelve el jar que Arquillian va a desplegar en Payara embebido.
     * El jar contiene las clases, el descriptor de la base de datos y el
     * archivo beans.xml para resolver la inyección de dependencias.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(TokenEntity.class.getPackage())
                .addPackage(TokenPersistence.class.getPackage())
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
        em.createQuery("delete from TokenEntity").executeUpdate();     
    }
    
    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {
        PodamFactory factory = new PodamFactoryImpl();

        for (int i = 0; i < 3; i++) {
            TokenEntity entity = factory.manufacturePojo(TokenEntity.class);
                                  
            em.persist(entity);
            data.add(entity);
        }
    }
    
        /**
     * Prueba para crear una Calificacion.
     */
    @Test
    public void createTokenTest() {
        PodamFactory factory = new PodamFactoryImpl();
        TokenEntity token = factory.manufacturePojo(TokenEntity.class);
        TokenEntity result = cp.create(token);

        Assert.assertNotNull(result);

        TokenEntity entity = em.find(TokenEntity.class, result.getId());

        Assert.assertEquals(token.getTipo(), entity.getTipo());
        Assert.assertEquals(token.getToken(), entity.getToken()); 
    }
   
    /**
     * Prueba para consultar una lista de Tokens.
     */
    @Test
    public void getTokensTest() {
        
        
        List<TokenEntity> list = cp.findAll();

        Assert.assertEquals(data.size(), list.size());

        for(TokenEntity ent : list){
            boolean found = false;
            for(TokenEntity entity : data){
          
            if(ent.getToken().equals(entity.getToken())){
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
    public void getTokenTest() {
        TokenEntity entity = data.get(0);
        TokenEntity newEntity = cp.find(entity.getId());
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(entity.getTipo(), newEntity.getTipo());
        Assert.assertEquals(entity.getToken(), newEntity.getToken());
    }
    
        /**
     * Prueba para consultar una Calificacion.
     */
    @Test
    public void getTokenByTokenTest() {
        TokenEntity entity = data.get(0);
        TokenEntity newEntity = cp.findByToken(entity.getToken());
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(entity.getTipo(), newEntity.getTipo());
        Assert.assertEquals(entity.getToken(), newEntity.getToken());
    }
    
    /**
     * Prueba para eliminar una Calificacion.
     */
    @Test
    public void deleteTokenTest() {
        TokenEntity entity = data.get(0);
        cp.delete(entity.getId());
        TokenEntity deleted = em.find(TokenEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }
    
    /**
     * Prueba para actualizar una Token.
     */
    @Test
    public void updateTokenTest() {
        TokenEntity entity = data.get(0);
        PodamFactory factory = new PodamFactoryImpl();
        TokenEntity newEntity = factory.manufacturePojo(TokenEntity.class);

        newEntity.setId(entity.getId());

        cp.update(newEntity);

        TokenEntity resp = em.find(TokenEntity.class, entity.getId());

        Assert.assertEquals(newEntity.getToken(), resp.getToken());
        Assert.assertEquals(newEntity.getTipo(), resp.getTipo());
    }
    
}
