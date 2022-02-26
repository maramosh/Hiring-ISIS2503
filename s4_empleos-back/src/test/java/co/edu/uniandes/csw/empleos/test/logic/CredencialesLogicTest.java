/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.logic;

import co.edu.uniandes.csw.empleos.ejb.CredencialesLogic;
import co.edu.uniandes.csw.empleos.entities.CredencialesEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
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
public class CredencialesLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private CredencialesLogic credencialLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private List<CredencialesEntity> data = new ArrayList<CredencialesEntity>();

    @Deployment
    private static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(CredencialesEntity.class.getPackage())
                .addPackage(CredencialesLogic.class.getPackage())
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

        for (int i = 0; i < 3; i++) {
            CredencialesEntity entity = factory.manufacturePojo(CredencialesEntity.class);
            em.persist(entity);
            data.add(entity);
        }

    }

    /**
     * Se verifica que se haya creado correctamente un token.
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test
    public void createCredencial() throws BusinessLogicException {
        CredencialesEntity newEntity = factory.manufacturePojo(CredencialesEntity.class);
        CredencialesEntity result = credencialLogic.createCredencial(newEntity);
        Assert.assertNotNull(result);

        CredencialesEntity entity = em.find(CredencialesEntity.class, result.getId());
        Assert.assertEquals(entity.getTipo(), result.getTipo());
        Assert.assertEquals(entity.getCorreo(), result.getCorreo());
        Assert.assertEquals(entity.getContrasenia(), result.getContrasenia());
    }

    /**
     * Se crea una token con un Tipo no nulo
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test(expected = BusinessLogicException.class)
    public void createCredencialTipoNotNull() throws BusinessLogicException {

        CredencialesEntity newEntity = factory.manufacturePojo(CredencialesEntity.class);
        newEntity.setTipo(null);
        CredencialesEntity result = credencialLogic.createCredencial(newEntity);
    }

    /**
     * Se crea una token con un Tipo no nulo
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test(expected = BusinessLogicException.class)
    public void createCredencialCorreoNotNull() throws BusinessLogicException {

        CredencialesEntity newEntity = factory.manufacturePojo(CredencialesEntity.class);
        newEntity.setCorreo(null);
        CredencialesEntity result = credencialLogic.createCredencial(newEntity);
    }

    /**
     * Se crea una token con un Tipo no nulo
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test(expected = BusinessLogicException.class)
    public void createCredencialContrasenaNotNull() throws BusinessLogicException {

        CredencialesEntity newEntity = factory.manufacturePojo(CredencialesEntity.class);
        newEntity.setContrasenia(null);
        CredencialesEntity result = credencialLogic.createCredencial(newEntity);
    }

    /**
     * Se crea una token con un Tipo no vacio
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test(expected = BusinessLogicException.class)
    public void createCredencialTipoNoVacio() throws BusinessLogicException {

        CredencialesEntity newEntity = factory.manufacturePojo(CredencialesEntity.class);
        newEntity.setTipo("");
        CredencialesEntity result = credencialLogic.createCredencial(newEntity);
    }

    /**
     * Se crea una token con un Tipo no vacio
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test(expected = BusinessLogicException.class)
    public void createoCredencialCorreoNoVacio() throws BusinessLogicException {

        CredencialesEntity newEntity = factory.manufacturePojo(CredencialesEntity.class);
        newEntity.setCorreo("");
        CredencialesEntity result = credencialLogic.createCredencial(newEntity);
    }

    /**
     * Se crea una token con un Tipo no vacio
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test(expected = BusinessLogicException.class)
    public void createoCredencialContrasenaNoVacio() throws BusinessLogicException {

        CredencialesEntity newEntity = factory.manufacturePojo(CredencialesEntity.class);
        newEntity.setContrasenia("");
        CredencialesEntity result = credencialLogic.createCredencial(newEntity);
    }

    /**
     * Prueba para consultar la lista de Calificaciones.
     */
    @Test
    public void getCredencialesTest() {
        List<CredencialesEntity> list = credencialLogic.getCredenciales();
        Assert.assertEquals(data.size(), list.size());
        for (CredencialesEntity entity : list) {
            boolean found = false;
            for (CredencialesEntity storedEntity : data) {
                if (entity.getId().equals(storedEntity.getId())) {
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
        CredencialesEntity resultEntity = credencialLogic.getCredencial(entity.getId());
        Assert.assertNotNull(resultEntity);
        Assert.assertEquals(entity.getId(), resultEntity.getId());
        Assert.assertEquals(entity.getCorreo(), resultEntity.getCorreo());
        Assert.assertEquals(entity.getTipo(), resultEntity.getTipo());

    }

    /**
     * Prueba para actualizar una Calificacion.
     *
     * @throws BusinessLogicException
     */
    @Test
    public void updateCredencial() throws BusinessLogicException {
        CredencialesEntity entity = data.get(0);
        CredencialesEntity pojoEntity = factory.manufacturePojo(CredencialesEntity.class);

        pojoEntity.setId(entity.getId());
        credencialLogic.updateCredencial(pojoEntity.getId(), pojoEntity);

        CredencialesEntity resp = em.find(CredencialesEntity.class, entity.getId());

        Assert.assertEquals(pojoEntity.getTipo(), resp.getTipo());

        Assert.assertEquals(pojoEntity.getCorreo(), resp.getCorreo());

        Assert.assertEquals(pojoEntity.getContrasenia(), resp.getContrasenia());
    }

}
