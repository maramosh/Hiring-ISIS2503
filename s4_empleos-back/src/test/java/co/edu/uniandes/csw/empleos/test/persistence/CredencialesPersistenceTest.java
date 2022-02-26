/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.persistence;

import co.edu.uniandes.csw.empleos.entities.CredencialesEntity;
import co.edu.uniandes.csw.empleos.persistence.CredencialesPersistence;
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
 * @author je.hernandezr
 */
@RunWith(Arquillian.class)
public class CredencialesPersistenceTest {

    @PersistenceContext(unitName = "empleosPU")
    protected EntityManager em;

    @Inject
    CredencialesPersistence cp;

    @Inject
    UserTransaction utx;

    private List<CredencialesEntity> data = new ArrayList<CredencialesEntity>();

    /**
     * @return Devuelve el jar que Arquillian va a desplegar en Payara embebido.
     * El jar contiene las clases, el descriptor de la base de datos y el
     * archivo beans.xml para resolver la inyección de dependencias.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(CredencialesEntity.class.getPackage())
                .addPackage(CredencialesPersistence.class.getPackage())
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
        em.createQuery("delete from CredencialesEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {
        PodamFactory factory = new PodamFactoryImpl();

        for (int i = 0; i < 3; i++) {
            CredencialesEntity entity = factory.manufacturePojo(CredencialesEntity.class);

            em.persist(entity);
            data.add(entity);
        }
    }

    /**
     * Prueba para crear una Credencial
     */
    @Test
    public void createCredencialTest() {
        PodamFactory factory = new PodamFactoryImpl();
        CredencialesEntity credencial = factory.manufacturePojo(CredencialesEntity.class);
        CredencialesEntity result = cp.create(credencial);

        Assert.assertNotNull(result);

        CredencialesEntity entity = em.find(CredencialesEntity.class, result.getId());

        Assert.assertEquals(credencial.getTipo(), entity.getTipo());
        Assert.assertEquals(credencial.getCorreo(), entity.getCorreo());
        Assert.assertEquals(credencial.getContrasenia(), entity.getContrasenia());

    }

    /**
     * Prueba para consultar una lista de Tokens.
     */
    @Test
    public void getCredencialesTest() {

        List<CredencialesEntity> list = cp.findAll();

        Assert.assertEquals(data.size(), list.size());

        for (CredencialesEntity ent : list) {
            boolean found = false;
            for (CredencialesEntity entity : data) {

                if (ent.getCorreo().equals(entity.getCorreo())) {
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
    public void getCredencialTest() {
        CredencialesEntity entity = data.get(0);
        CredencialesEntity newEntity = cp.find(entity.getId());
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(entity.getTipo(), newEntity.getTipo());
        Assert.assertEquals(entity.getCorreo(), newEntity.getCorreo());
    }

    /**
     * Prueba para consultar una Calificacion.
     */
    @Test
    public void getCredencialByCorreoTest() {
        CredencialesEntity entity = data.get(0);
        CredencialesEntity newEntity = cp.findByCorreo(entity.getCorreo());
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(entity.getTipo(), newEntity.getTipo());
    }

    /**
     * Prueba para eliminar una Calificacion.
     */
    @Test
    public void deleteCredencialTest() {
        CredencialesEntity entity = data.get(0);
        cp.delete(entity.getId());
        CredencialesEntity deleted = em.find(CredencialesEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

    /**
     * Prueba para actualizar una Token.
     */
    @Test
    public void updateCredencialTest() {
        CredencialesEntity entity = data.get(0);
        PodamFactory factory = new PodamFactoryImpl();
        CredencialesEntity newEntity = factory.manufacturePojo(CredencialesEntity.class);

        newEntity.setId(entity.getId());

        cp.update(newEntity);

        CredencialesEntity resp = em.find(CredencialesEntity.class, entity.getId());
        Assert.assertEquals(newEntity.getTipo(), resp.getTipo());
        Assert.assertEquals(newEntity.getCorreo(), resp.getCorreo());
    }

}
