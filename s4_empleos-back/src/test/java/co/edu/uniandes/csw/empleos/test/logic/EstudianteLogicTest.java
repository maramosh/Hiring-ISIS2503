package co.edu.uniandes.csw.empleos.test.logic;

import co.edu.uniandes.csw.empleos.entities.EstudianteEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.ejb.EstudianteLogic;
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
 * @author David Dominguez
 */
@RunWith(Arquillian.class)
public class EstudianteLogicTest {

    @Inject
    private EstudianteLogic estudianteLogic;

    @PersistenceContext
    private EntityManager em;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<EstudianteEntity> datos = new ArrayList<EstudianteEntity>();

    @Inject
    private UserTransaction utx;

    @Deployment
    private static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(EstudianteEntity.class.getPackage())
                .addPackage(EstudianteLogic.class.getPackage())
                .addPackage(EstudiantePersistence.class.getPackage())
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }

    @Before
    public void configTest() {
        try {
            utx.begin();
            clearData();
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

    private void clearData() {
        em.createQuery("delete from FacturaEntity").executeUpdate();
    }

    /**
     * Se verifica que se haya creado correctamente un estudiante.
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test
    public void crearEstudiante() throws BusinessLogicException {
        EstudianteEntity newEntity = factory.manufacturePojo(EstudianteEntity.class);
        newEntity.setCorreo("algo@uniandes.edu.co");
        newEntity.setSemestre(2);
        EstudianteEntity result = estudianteLogic.crearEstudiante(newEntity);
        Assert.assertNotNull(result);
        EstudianteEntity entity = em.find(EstudianteEntity.class, result.getId());
        Assert.assertEquals(entity.getNombre(), result.getNombre());
        Assert.assertEquals(entity.getSemestre(), result.getSemestre());
        Assert.assertEquals(entity.getIdMedioDepago(), result.getIdMedioDepago());
        Assert.assertEquals(entity.getHorarioDeTrabajo(), result.getHorarioDeTrabajo());
        Assert.assertEquals(entity.getCorreo(), result.getCorreo());
        Assert.assertEquals(entity.getCarrera(), result.getCarrera());
        Assert.assertEquals(entity.getCalificacionPromedio(), result.getCalificacionPromedio(), 0.0001);
    }

    /**
     * Se crea un estudiante con una carrera vacía
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void crearEstudianteCarreraVacia() throws BusinessLogicException {
        EstudianteEntity newEntity = factory.manufacturePojo(EstudianteEntity.class);
        newEntity.setCarrera("");
        EstudianteEntity result = estudianteLogic.crearEstudiante(newEntity);
    }

    /**
     * Se crea un estudiante con una calificacion promedio mayor a cero
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test(expected = BusinessLogicException.class)
    public void crearCalificacionPromedioMenorCero() throws BusinessLogicException {
        EstudianteEntity newEntity = factory.manufacturePojo(EstudianteEntity.class);
        newEntity.setCalificacionPromedio(-1d);
        EstudianteEntity result = estudianteLogic.crearEstudiante(newEntity);
    }

    /**
     * Se crea un estudiante con una calificacion promedio menor o igual a cinco
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test(expected = BusinessLogicException.class)
    public void crearCalificacionPromedioMayorCinco() throws BusinessLogicException {
        EstudianteEntity newEntity = factory.manufacturePojo(EstudianteEntity.class);
        newEntity.setCalificacionPromedio(new Double(6));
        EstudianteEntity result = estudianteLogic.crearEstudiante(newEntity);
    }

    /**
     * Se crea un estudiante con un correo no uniandino
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test(expected = BusinessLogicException.class)
    public void crearCorreoNoUniandes() throws BusinessLogicException {
        EstudianteEntity newEntity = factory.manufacturePojo(EstudianteEntity.class);
        newEntity.setCorreo("akwjdand@gmail.com");
        EstudianteEntity result = estudianteLogic.crearEstudiante(newEntity);
    }

    /**
     * Se crea un estudiante con un correo vacío
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test(expected = BusinessLogicException.class)
    public void crearCorreoVacio() throws BusinessLogicException {
        EstudianteEntity newEntity = factory.manufacturePojo(EstudianteEntity.class);
        newEntity.setCorreo("");
        EstudianteEntity result = estudianteLogic.crearEstudiante(newEntity);
    }

    /**
     * Se crea un estudiante con un horario de trabajo vacío
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test(expected = BusinessLogicException.class)
    public void crearHorarioDeTrabajoVacio() throws BusinessLogicException {
        EstudianteEntity newEntity = factory.manufacturePojo(EstudianteEntity.class);
        newEntity.setHorarioDeTrabajo("");
        EstudianteEntity result = estudianteLogic.crearEstudiante(newEntity);
    }

    /**
     * Se crea un estudiante con un nombre vacío
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test(expected = BusinessLogicException.class)
    public void crearNombreVacio() throws BusinessLogicException {
        EstudianteEntity newEntity = factory.manufacturePojo(EstudianteEntity.class);
        newEntity.setNombre("");
        EstudianteEntity result = estudianteLogic.crearEstudiante(newEntity);
    }

    /**
     * Se crea un estudiante con un semestre mayor a 12
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test(expected = BusinessLogicException.class)
    public void crearSemestreMayor12() throws BusinessLogicException {
        EstudianteEntity newEntity = factory.manufacturePojo(EstudianteEntity.class);
        newEntity.setSemestre(13);
        EstudianteEntity result = estudianteLogic.crearEstudiante(newEntity);
    }

    /**
     * Se crea un estudiante con un semestre menos a 1
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test(expected = BusinessLogicException.class)
    public void crearSemestreMenor1() throws BusinessLogicException {
        EstudianteEntity newEntity = factory.manufacturePojo(EstudianteEntity.class);
        newEntity.setSemestre(0);
        EstudianteEntity result = estudianteLogic.crearEstudiante(newEntity);
    }

    @Test
    public void deleteEstudianteTest() {
        EstudianteEntity newEntity = factory.manufacturePojo(EstudianteEntity.class);
        newEntity.setCorreo("algo@uniandes.edu.co");
        newEntity.setSemestre(5);
        long id = newEntity.getId();
        try {
            newEntity = estudianteLogic.crearEstudiante(newEntity);
        } catch (BusinessLogicException e) {
            Assert.fail("Debería dejar crear un estudiante");
        }

        try {
            utx.begin();
            clearData();
            estudianteLogic.deleteEstudiante(id);
            EstudianteEntity e = estudianteLogic.getEstudiante(id);
            Assert.assertNull(e);
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

    @Test
    public void readEstudianteTest() {
        EstudianteEntity newEntity = factory.manufacturePojo(EstudianteEntity.class);
        newEntity.setCorreo("algo@uniandes.edu.co");
        newEntity.setSemestre(5);
        try {
            estudianteLogic.crearEstudiante(newEntity);
            EstudianteEntity e = estudianteLogic.getEstudiante(newEntity.getId());
            Assert.assertEquals(e.getId(), newEntity.getId());
        } catch (BusinessLogicException e) {
            Assert.fail("Se esperaba poder crear al estudiante");
        }
    }

    @Test
    public void updateEstudianteTest() {
        EstudianteEntity newEntity = factory.manufacturePojo(EstudianteEntity.class);
        newEntity.setCorreo("algo@uniandes.edu.co");
        newEntity.setSemestre(5);
        long id = newEntity.getId();
        try {
            estudianteLogic.crearEstudiante(newEntity);
        } catch (BusinessLogicException e) {
            Assert.fail("Se esperaba poder crear al estudiante");
        }

        newEntity = factory.manufacturePojo(EstudianteEntity.class);
        newEntity.setSemestre(5);
        newEntity.setId(id);
        try {
            estudianteLogic.updateEstudiante(newEntity);
            Assert.fail("No debería permitir crear un estudiante con un correo diferente a uniandes");
        } catch (BusinessLogicException e) {
            Assert.assertEquals( "El correo no era de uniandes", e.getMessage() );
        }

        newEntity = factory.manufacturePojo(EstudianteEntity.class);
        newEntity.setCorreo("algo@uniandes.edu.co");
        newEntity.setSemestre(0);
        newEntity.setId(id);
        try {
            estudianteLogic.updateEstudiante(newEntity);
            Assert.fail("No debería permitir crear un estudiante con un semestre menor a 1");
        } catch (BusinessLogicException e) {
            Assert.assertEquals( "No es un semestre válido", e.getMessage());
        }

        newEntity = factory.manufacturePojo(EstudianteEntity.class);
        newEntity.setCorreo("algo@uniandes.edu.co");
        newEntity.setSemestre(13);
        newEntity.setId(id);
        try {
            estudianteLogic.updateEstudiante(newEntity);
            Assert.fail("No debería permitir crear un estudiante con un semestre mayor a 12");
        } catch (BusinessLogicException e) {
            Assert.assertEquals("No es un semestre válido", e.getMessage());
        }

        newEntity = factory.manufacturePojo(EstudianteEntity.class);
        newEntity.setCorreo("algo@uniandes.edu.co");
        newEntity.setSemestre(5);
        newEntity.setCarrera("");
        newEntity.setId(id);
        try {
            estudianteLogic.updateEstudiante(newEntity);
            Assert.fail("No debería permitir crear un estudiante con una carrera vacía");
        } catch (BusinessLogicException e) {
            Assert.assertEquals( "No es una carrera válida", e.getMessage());
        }

        newEntity = factory.manufacturePojo(EstudianteEntity.class);
        newEntity.setCorreo("algo@uniandes.edu.co");
        newEntity.setSemestre(5);
        newEntity.setId(id);
        newEntity.setCalificacionPromedio(-1d);
        try {
            estudianteLogic.updateEstudiante(newEntity);
            Assert.fail("No debería permitir crear un estudiante con una calificación promedio menor a 0");
        } catch (BusinessLogicException e) {
            Assert.assertEquals("No es una calificación válida", e.getMessage());
        }

        newEntity = factory.manufacturePojo(EstudianteEntity.class);
        newEntity.setCorreo("algo@uniandes.edu.co");
        newEntity.setSemestre(5);
        newEntity.setId(id);
        newEntity.setCalificacionPromedio(6d);
        try {
            estudianteLogic.updateEstudiante(newEntity);
            Assert.fail("No debería permitir crear un estudiante con una calificación promedio mayor a 5");
        } catch (BusinessLogicException e) {
            Assert.assertEquals("No es una calificación válida", e.getMessage());
        }

        newEntity = factory.manufacturePojo(EstudianteEntity.class);
        newEntity.setCorreo("algo@uniandes.edu.co");
        newEntity.setSemestre(5);
        newEntity.setId(id);
        newEntity.setNombre("");
        try {
            estudianteLogic.updateEstudiante(newEntity);
            Assert.fail("No debería permitir crear un estudiante con un nombre vacío");
        } catch (BusinessLogicException e) {
            Assert.assertEquals("No es un nombre válido", e.getMessage());
        }

        newEntity = factory.manufacturePojo(EstudianteEntity.class);
        newEntity.setCorreo("algo@uniandes.edu.co");
        newEntity.setSemestre(5);
        newEntity.setId(id);
        newEntity.setHorarioDeTrabajo("");
        try {
            estudianteLogic.updateEstudiante(newEntity);
            Assert.fail("No debería permitir crear un estudiante con un horario de trabajo vacío");
        } catch (BusinessLogicException e) {
            Assert.assertEquals("No es un horario válido", e.getMessage());
        }
    }
}
