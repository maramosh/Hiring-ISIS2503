/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.logic;

import co.edu.uniandes.csw.empleos.ejb.TokenLogic;
import co.edu.uniandes.csw.empleos.entities.TokenEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
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
public class TokenLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private TokenLogic tokenLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private List<TokenEntity> data = new ArrayList<TokenEntity>();

    @Deployment
    private static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(TokenEntity.class.getPackage())
                .addPackage(TokenLogic.class.getPackage())
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

        for (int i = 0; i < 3; i++) {
            TokenEntity entity = factory.manufacturePojo(TokenEntity.class);
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
    public void createToken() throws BusinessLogicException {
        TokenEntity newEntity = factory.manufacturePojo(TokenEntity.class);
        TokenEntity result = tokenLogic.createToken(newEntity);
        Assert.assertNotNull(result);

        TokenEntity entity = em.find(TokenEntity.class, result.getId());
        Assert.assertEquals(entity.getTipo(), result.getTipo());
        Assert.assertEquals(entity.getToken(), result.getToken());
    }

    /**
     * Se crea una token con un Tipo no nulo
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test(expected = BusinessLogicException.class)
    public void createTokenTipoNotNull() throws BusinessLogicException {

        TokenEntity newEntity = factory.manufacturePojo(TokenEntity.class);
        newEntity.setTipo(null);
        TokenEntity result = tokenLogic.createToken(newEntity);
    }

    /**
     * Se crea una token con un Tipo no nulo
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test(expected = BusinessLogicException.class)
    public void createTokenTokenNotNull() throws BusinessLogicException {

        TokenEntity newEntity = factory.manufacturePojo(TokenEntity.class);
        newEntity.setToken(null);
        TokenEntity result = tokenLogic.createToken(newEntity);
    }

    /**
     * Se crea una token con un Tipo no vacio
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test(expected = BusinessLogicException.class)
    public void createoTokenTipoNotVacio() throws BusinessLogicException {

        TokenEntity newEntity = factory.manufacturePojo(TokenEntity.class);
        newEntity.setTipo("");
        TokenEntity result = tokenLogic.createToken(newEntity);
    }

    /**
     * Se crea una token con un Tipo no vacio
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test(expected = BusinessLogicException.class)
    public void createoTokenTokenNotVacio() throws BusinessLogicException {

        TokenEntity newEntity = factory.manufacturePojo(TokenEntity.class);
        newEntity.setToken("");
        TokenEntity result = tokenLogic.createToken(newEntity);
    }

    /**
     * Prueba para consultar la lista de Calificaciones.
     */
    @Test
    public void getTokensTest() {
        List<TokenEntity> list = tokenLogic.getTokens();
        Assert.assertEquals(data.size(), list.size());
        for (TokenEntity entity : list) {
            boolean found = false;
            for (TokenEntity storedEntity : data) {
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
    public void getTokenByTokenTest() {
        TokenEntity entity = data.get(0);
        TokenEntity resultEntity = tokenLogic.getTokenByToken(entity.getToken());
        Assert.assertNotNull(resultEntity);
        Assert.assertEquals(entity.getId(), resultEntity.getId());
        Assert.assertEquals(entity.getToken(), resultEntity.getToken());
        Assert.assertEquals(entity.getTipo(), resultEntity.getTipo());

    }

    /**
     * Prueba para consultar una Calificacion.
     */
    @Test
    public void getTokenTest() {
        TokenEntity entity = data.get(0);
        TokenEntity resultEntity = tokenLogic.getToken(entity.getId());
        Assert.assertNotNull(resultEntity);
        Assert.assertEquals(entity.getId(), resultEntity.getId());
        Assert.assertEquals(entity.getToken(), resultEntity.getToken());
        Assert.assertEquals(entity.getTipo(), resultEntity.getTipo());

    }

    /**
     * Prueba para actualizar una Calificacion.
     *
     * @throws BusinessLogicException
     */
    @Test
    public void updateToken() throws BusinessLogicException {
        TokenEntity entity = data.get(0);
        TokenEntity pojoEntity = factory.manufacturePojo(TokenEntity.class);

        pojoEntity.setId(entity.getId());
        tokenLogic.updateToken(pojoEntity.getId(), pojoEntity);

        TokenEntity resp = em.find(TokenEntity.class, entity.getId());

        Assert.assertEquals(pojoEntity.getTipo(), resp.getTipo());

        Assert.assertEquals(pojoEntity.getToken(), resp.getToken());
    }

}
