/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.logic;

import co.edu.uniandes.csw.empleos.ejb.CalificacionLogic;
import co.edu.uniandes.csw.empleos.entities.CalificacionEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
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
 *
 * @author Nicolas Munar
 */
@RunWith(Arquillian.class)
public class CalificacionLogicTest {

    @PersistenceContext
    private EntityManager em;
    @Inject
    private UserTransaction utx;
    @Inject
    private CalificacionLogic calificacionLogic;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<CalificacionEntity> data = new ArrayList<CalificacionEntity>();

    @Deployment
    private static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(CalificacionEntity.class.getPackage())
                .addPackage(CalificacionLogic.class.getPackage())
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

        for (int i = 0; i < 3; i++) {
            CalificacionEntity entity = factory.manufacturePojo(CalificacionEntity.class);
            em.persist(entity);
            data.add(entity);
        }

    }

    /**
     * Se verifica que se haya creado correctamente una calificacion.
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test
    public void createCalificacion() throws BusinessLogicException {
        CalificacionEntity newEntity = factory.manufacturePojo(CalificacionEntity.class);
        if (newEntity.getNota() < 0) {
            newEntity.setNota(newEntity.getNota() * -1);
        }
        CalificacionEntity result = calificacionLogic.createCalificacion(newEntity);
        Assert.assertNotNull(result);

        CalificacionEntity entity = em.find(CalificacionEntity.class, result.getId());
        Assert.assertEquals(entity.getNota(), result.getNota());
        Assert.assertEquals(entity.getComentario(), result.getComentario());
    }

    /**
     * Se crea una calificacion con una nota menor a 0
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createCalificacionNotaNoMenoraCero() throws BusinessLogicException {

        CalificacionEntity newEntity = factory.manufacturePojo(CalificacionEntity.class);
        newEntity.setNota(-1.0);
        CalificacionEntity result = calificacionLogic.createCalificacion(newEntity);
    }

    /**
     * Se crea una calificacion con una nota mayor a 5
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createCalificacionNotaNoMayorCinco() throws BusinessLogicException {

        CalificacionEntity newEntity = factory.manufacturePojo(CalificacionEntity.class);
        newEntity.setNota(5.5);
        CalificacionEntity result = calificacionLogic.createCalificacion(newEntity);
    }

    /**
     * Se crea una calificacion con un Comentario no nulo
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test(expected = BusinessLogicException.class)
    public void createCalificacionComentarioNotNull() throws BusinessLogicException {

        CalificacionEntity newEntity = factory.manufacturePojo(CalificacionEntity.class);
        newEntity.setComentario(null);
        calificacionLogic.createCalificacion(newEntity);
    }

    /**
     * Se crea una calificacion con un Comentario no vacio
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test(expected = BusinessLogicException.class)
    public void createCalificacionComentarioNotVacio() throws BusinessLogicException {

        CalificacionEntity newEntity = factory.manufacturePojo(CalificacionEntity.class);
        newEntity.setComentario("");
        calificacionLogic.createCalificacion(newEntity);
    }

    /**
     * Prueba para consultar la lista de Calificaciones.
     */
    @Test
    public void getCalificacionesTest() {
        List<CalificacionEntity> list = calificacionLogic.getCalificaciones();
        Assert.assertEquals(data.size(), list.size());
        for (CalificacionEntity entity : list) {
            boolean found = false;
            for (CalificacionEntity storedEntity : data) {
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
    public void getCalificacionTest() {
        CalificacionEntity entity = data.get(0);
        CalificacionEntity resultEntity = calificacionLogic.getCalificacion(entity.getId());
        Assert.assertNotNull(resultEntity);
        Assert.assertEquals(entity.getId(), resultEntity.getId());
        Assert.assertEquals(entity.getComentario(), resultEntity.getComentario());
        Assert.assertEquals(entity.getNota(), resultEntity.getNota());

    }

    /**
     * Prueba para actualizar una Calificacion.
     *
     * @throws BusinessLogicException
     */
    @Test
    public void updateCalificacion() throws BusinessLogicException {
        CalificacionEntity entity = data.get(0);
        CalificacionEntity pojoEntity = factory.manufacturePojo(CalificacionEntity.class);
        if (pojoEntity.getNota() < 0) {
            pojoEntity.setNota(pojoEntity.getNota() * -1);
        }

        pojoEntity.setId(entity.getId());
        calificacionLogic.updateCalificacion(pojoEntity.getId(), pojoEntity);

        CalificacionEntity resp = em.find(CalificacionEntity.class, entity.getId());

        Assert.assertEquals(pojoEntity.getComentario(), resp.getComentario());

        Assert.assertEquals(pojoEntity.getNota(), resp.getNota());

        Assert.assertEquals(pojoEntity.getEstudiante(), resp.getEstudiante());
    }

    /**
     * Prueba para actualizar una Calificacion.
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void updateCalificacionValorNoMenoraCero() throws BusinessLogicException {
        CalificacionEntity entity = data.get(0);
        CalificacionEntity pojoEntity = factory.manufacturePojo(CalificacionEntity.class);
        pojoEntity.setNota(-1.0);
        pojoEntity.setId(entity.getId());
        calificacionLogic.updateCalificacion(pojoEntity.getId(), pojoEntity);
    }

    /**
     * Prueba para actualizar una Calificacion.
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void updateCalificacionValorNoMayorCinco() throws BusinessLogicException {
        CalificacionEntity entity = data.get(0);
        CalificacionEntity pojoEntity = factory.manufacturePojo(CalificacionEntity.class);
        pojoEntity.setNota(6.0);
        pojoEntity.setId(entity.getId());
        calificacionLogic.updateCalificacion(pojoEntity.getId(), pojoEntity);
    }

    /**
     * Prueba para actualizar una Calificacion.
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void updateCalificacionFechaNotNull() throws BusinessLogicException {
        CalificacionEntity entity = data.get(0);
        CalificacionEntity pojoEntity = factory.manufacturePojo(CalificacionEntity.class);
        pojoEntity.setComentario(null);
        pojoEntity.setId(entity.getId());
        calificacionLogic.updateCalificacion(pojoEntity.getId(), pojoEntity);
    }

    /**
     * Prueba para actualizar una Calificacion.
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void updateCalificacionFechaNotVacio() throws BusinessLogicException {
        CalificacionEntity entity = data.get(0);
        CalificacionEntity pojoEntity = factory.manufacturePojo(CalificacionEntity.class);
        pojoEntity.setComentario("");
        pojoEntity.setId(entity.getId());
        calificacionLogic.updateCalificacion(pojoEntity.getId(), pojoEntity);
    }

    /**
     * Prueba para eliminar una calificacion.
     *
     * @throws BusinessLogicException
     */
    @Test
    public void deleteCalificacionTest() throws BusinessLogicException {
        CalificacionEntity entity = data.get(0);
        calificacionLogic.deleteCalificacion(entity.getId());
        CalificacionEntity deleted = em.find(CalificacionEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }
}
