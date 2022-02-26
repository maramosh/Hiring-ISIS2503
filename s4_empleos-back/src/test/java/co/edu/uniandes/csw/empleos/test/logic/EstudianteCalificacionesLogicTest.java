/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.logic;

import co.edu.uniandes.csw.empleos.ejb.EstudianteCalificacionesLogic;
import co.edu.uniandes.csw.empleos.ejb.CalificacionLogic;
import co.edu.uniandes.csw.empleos.ejb.EstudianteLogic;
import co.edu.uniandes.csw.empleos.entities.CalificacionEntity;
import co.edu.uniandes.csw.empleos.entities.EstudianteEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.CalificacionPersistence;
import co.edu.uniandes.csw.empleos.persistence.EstudiantePersistence;
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
 * @author Nicolàs Munar
 */
@RunWith(Arquillian.class)
public class EstudianteCalificacionesLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private CalificacionLogic calificacionLogic;
    @Inject
    private EstudianteLogic estudianteLogic;

    @Inject
    private EstudianteCalificacionesLogic estudianteCalificacionesLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private List<CalificacionEntity> caldata = new ArrayList();
    private List<EstudianteEntity> data = new ArrayList<EstudianteEntity>();

    /**
     * @return Devuelve el jar que Arquillian va a desplegar en Payara embebido.
     * El jar contiene las clases, el descriptor de la base de datos y el
     * archivo beans.xml para resolver la inyección de dependencias.
     */
    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(EstudianteEntity.class.getPackage())
                .addPackage(EstudianteLogic.class.getPackage())
                .addPackage(EstudiantePersistence.class.getPackage())
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
        em.createQuery("delete from EstudianteEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {

        for (int i = 0; i < 3; i++) {
            CalificacionEntity calificaciones = factory.manufacturePojo(CalificacionEntity.class);
            em.persist(calificaciones);
            caldata.add(calificaciones);
        }

        for (int i = 0; i < 3; i++) {
            EstudianteEntity entity = factory.manufacturePojo(EstudianteEntity.class);
            em.persist(entity);
            data.add(entity);
            if (i == 0) {
                caldata.get(i).setEstudiante(entity);
            }
        }

    }

    /**
     * Prueba para asociar una calificaiocn existente a un Estudiante
     */
    @Test
    public void addCalificacionesTest() {
        EstudianteEntity entity = data.get(0);
        CalificacionEntity calificacionEntity = caldata.get(0);
        CalificacionEntity response = estudianteCalificacionesLogic.addCalificacion(calificacionEntity.getId(), entity.getId());

        Assert.assertNotNull(response);
        Assert.assertEquals(calificacionEntity.getId(), response.getId());
    }

    /**
     * Prueba para obtener una colección de instancias de Calificaciones
     * asociadas a una instancia Estudiante.
     */
    @Test
    public void getCalificaionesTest() {
        List<CalificacionEntity> list = estudianteCalificacionesLogic.getCalificaciones(data.get(0).getId());
        Assert.assertEquals(1, list.size());
    }

    /**
     * Prueba para obtener una instancia de Calificacion asociada a una
     * instancia Estudiante.
     *
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test
    public void getCalificacionTest() throws BusinessLogicException {
        EstudianteEntity entity = data.get(0);
        CalificacionEntity calEntity = caldata.get(0);
        CalificacionEntity response = estudianteCalificacionesLogic.getCalificacion(entity.getId(), calEntity.getId());

        Assert.assertEquals(calEntity.getId(), response.getId());
        Assert.assertEquals(calEntity.getComentario(), response.getComentario());
        Assert.assertEquals(calEntity.getNota(), response.getNota());

    }

    /**
     * Prueba para obtener una instancia de Calificaiones asociada a una
     * instancia Estudiante que no le pertenece.
     *
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void getCalificacionNoAsociadaTest() throws BusinessLogicException {
        EstudianteEntity entity = data.get(0);
        CalificacionEntity calEntity = caldata.get(1);
        estudianteCalificacionesLogic.getCalificacion(entity.getId(), calEntity.getId());
    }

    /**
     * Prueba para remplazar las instancias de Calificaciones asociadas a una
     * instancia de Estudiante.
     */
    @Test
    public void replaceCalificacionesTest() {
        EstudianteEntity entity = data.get(0);
        List<CalificacionEntity> list = caldata.subList(1, 3);
        estudianteCalificacionesLogic.replaceCalificaciones(entity.getId(), list);

        entity = estudianteLogic.getEstudiante(entity.getId());
        Assert.assertTrue(entity.getCalificaciones().contains(caldata.get(1)));
        Assert.assertTrue(entity.getCalificaciones().contains(caldata.get(2)));
    }

    /**
     * Prueba para desasociar una calificaci{on existente de un Estudiante
     * existente
     *
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test
    public void removeCalificacion() throws BusinessLogicException {
        try {
            utx.begin();
            em.joinTransaction();
            insertData();
            EstudianteEntity estudiante = data.get(0);
            long id1 = estudiante.getId();
            estudiante.setCalificaciones(caldata);
            long id2 = estudiante.getCalificaciones().get(0).getId();
            estudiante.setCorreo("akjwd@uniandes.edu.co");
            estudiante.setSemestre(Math.min(Math.abs(estudiante.getSemestre()) + 1, 12));
            estudiante.setNombre(estudiante.getNombre() + "a");
            estudiante.setCarrera(estudiante.getCarrera() + "a");
            estudianteLogic.updateEstudiante(estudiante);

            estudianteCalificacionesLogic.removeCalificacion(id1, id2);
            EstudianteEntity response = estudianteLogic.getEstudiante(id1);
            CalificacionEntity cal = null;
            for (CalificacionEntity e : response.getCalificaciones()) {
                if (e.getId() == id2) {
                    cal = e;
                }
            }
            Assert.assertNull(cal);
        } catch (Exception exx) {
            Assert.fail("No debería haber lanzado excepción");
            exx.printStackTrace();
            System.out.println("EXCEPCION:");
            System.out.println(exx.getMessage());
            try {
                utx.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
