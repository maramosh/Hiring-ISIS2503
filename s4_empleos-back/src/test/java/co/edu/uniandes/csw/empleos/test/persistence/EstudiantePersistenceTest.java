package co.edu.uniandes.csw.empleos.test.persistence;

import co.edu.uniandes.csw.empleos.entities.CalificacionEntity;
import co.edu.uniandes.csw.empleos.entities.CuentaBancariaEntity;
import co.edu.uniandes.csw.empleos.entities.EstudianteEntity;
import co.edu.uniandes.csw.empleos.entities.OfertaEntity;
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
 * Test de persistencia de la clase estudiante
 * @author David Dominguez
 */
@RunWith(Arquillian.class)
public class EstudiantePersistenceTest {
    
    // Se relaciona la base de datos con el entitymanager
    @PersistenceContext(unitName = "empleosPU")
    public EntityManager em;
    
    //Guarda la lista de estudiantes que Podam genere
    private ArrayList<EstudianteEntity> estudiantes;
    
    // Se relaciona Arquilliean con las clases que se pobrarán
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(EstudianteEntity.class.getPackage())
                .addPackage(EstudiantePersistence.class.getPackage())
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }
    
    // Atributo que representa al objeto encargado de manejar la persistencia de Estudiante
    @Inject
    EstudiantePersistence estudiantePersistence;
    
    @Inject
    UserTransaction u;
    
    @Before
    public void setup() {
        try {
            estudiantes = new ArrayList<EstudianteEntity>();
            u.begin();
            em.joinTransaction();
            em.createQuery("delete from EstudianteEntity").executeUpdate();
            PodamFactory factory = new PodamFactoryImpl();
            for (int i = 0; i < 5; i++) {
                EstudianteEntity entity = factory.manufacturePojo(EstudianteEntity.class);
                estudiantes.add(entity);
                em.persist(entity);
            }
            u.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                u.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
    
    // Se prueba que se cree y guarde exitosamente el estudiante
    @Test
    public void crearEstudianteTest() {
        PodamFactory factory = new PodamFactoryImpl();
        EstudianteEntity estudiante = factory.manufacturePojo(EstudianteEntity.class);
        EstudianteEntity result = estudiantePersistence.create(estudiante);
        Assert.assertNotNull(result);
        
        EstudianteEntity e = em.find(EstudianteEntity.class, result.getId());
        Assert.assertEquals(e.getId(), result.getId());
    }
    
    // Se prueba que se actualice un estudiante
    @Test
    public void updateTest() {
        EstudianteEntity estudiante = estudiantes.get(0);
        PodamFactory factory = new PodamFactoryImpl();
        EstudianteEntity nuevoEstudiante = factory.manufacturePojo(EstudianteEntity.class);
        nuevoEstudiante.setId(estudiante.getId());
        estudiantePersistence.update(nuevoEstudiante);
        EstudianteEntity resp = em.find(EstudianteEntity.class, estudiante.getId());
        Assert.assertEquals(nuevoEstudiante.getNombre(), resp.getNombre());
        Assert.assertEquals(nuevoEstudiante.getCorreo(), resp.getCorreo());
        Assert.assertEquals(nuevoEstudiante.getSemestre(), resp.getSemestre());
        Assert.assertEquals(nuevoEstudiante.getCarrera(), resp.getCarrera());
        Assert.assertEquals(nuevoEstudiante.getCalificacionPromedio(), resp.getCalificacionPromedio(), 0.01);
        Assert.assertEquals(nuevoEstudiante.getHorarioDeTrabajo(), resp.getHorarioDeTrabajo());
        Assert.assertEquals(nuevoEstudiante.getIdMedioDepago(), resp.getIdMedioDepago());
    }
    
    // Se prueba que se obtengan todos los estudiantes
    @Test
    public void findAllTest() {
        List<EstudianteEntity> mEstudiantes = estudiantePersistence.findAll();
        Assert.assertEquals(estudiantes.size(), mEstudiantes.size());
        for (EstudianteEntity e : mEstudiantes) {
            boolean found = false;
            for (EstudianteEntity entity : estudiantes) {
                if (e.getId().equals(entity.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    /**
     * Se prueba que se obtenga un estudiante
     */
    @Test
    public void getContratistaTest() {
        EstudianteEntity resp = estudiantes.get(2);
        EstudianteEntity nuevoEstudiante = estudiantePersistence.read(resp.getId());
        Assert.assertNotNull(nuevoEstudiante);
        Assert.assertEquals(nuevoEstudiante.getNombre(), resp.getNombre());
        Assert.assertEquals(nuevoEstudiante.getCorreo(), resp.getCorreo());
        Assert.assertEquals(nuevoEstudiante.getSemestre(), resp.getSemestre());
        Assert.assertEquals(nuevoEstudiante.getCarrera(), resp.getCarrera());
        Assert.assertEquals(nuevoEstudiante.getCalificacionPromedio(), resp.getCalificacionPromedio(), 0.01);
        Assert.assertEquals(nuevoEstudiante.getHorarioDeTrabajo(), resp.getHorarioDeTrabajo());
        Assert.assertEquals(nuevoEstudiante.getIdMedioDepago(), resp.getIdMedioDepago());
    }

    /**
     * Se prueba que se elimine correctamente un estudiante
     */
    @Test
    public void deleteTest() {
        EstudianteEntity entity = estudiantes.get(0);
        estudiantePersistence.delete(entity.getId());
        EstudianteEntity deleted = em.find(EstudianteEntity.class, entity.getId());
        Assert.assertNull(deleted);
        
        EstudianteEntity entity2 = estudiantes.get(0);
        estudiantePersistence.delete(entity2);
        EstudianteEntity deleted2 = em.find(EstudianteEntity.class, entity2.getId());
        Assert.assertNull(deleted2);
    }
    
    
    // Se prueba que el nombre de un estudiante quede correctamente guardado
    @Test
    public void nombreTest() {
        PodamFactory factory = new PodamFactoryImpl();
        EstudianteEntity estudiante = factory.manufacturePojo(EstudianteEntity.class);
        EstudianteEntity result = estudiantePersistence.create(estudiante);
        Assert.assertNotNull(result);
        
        EstudianteEntity e = em.find(EstudianteEntity.class, result.getId());
        Assert.assertEquals(e.getNombre(), result.getNombre());
    }
    
    // Se prueba que el id del medio de pago de un estudiante quede correctamente guardado
    @Test
    public void idMedioDePagoTest() {
        PodamFactory factory = new PodamFactoryImpl();
        EstudianteEntity estudiante = factory.manufacturePojo(EstudianteEntity.class);
        EstudianteEntity result = estudiantePersistence.create(estudiante);
        Assert.assertNotNull(result);
        
        EstudianteEntity e = em.find(EstudianteEntity.class, result.getId());
        Assert.assertEquals(e.getIdMedioDepago(), result.getIdMedioDepago());
    }
    
    // Se prueba que la carrera de un estudiante quede correctamente guardado
    @Test
    public void carreraTest() {
        PodamFactory factory = new PodamFactoryImpl();
        EstudianteEntity estudiante = factory.manufacturePojo(EstudianteEntity.class);
        EstudianteEntity result = estudiantePersistence.create(estudiante);
        Assert.assertNotNull(result);
        
        EstudianteEntity e = em.find(EstudianteEntity.class, result.getId());
        Assert.assertEquals(e.getCarrera(), result.getCarrera());
    }
    
    // Se prueba que el correo de un estudiante quede correctamente guardado
    @Test
    public void correoTest() {
        PodamFactory factory = new PodamFactoryImpl();
        EstudianteEntity estudiante = factory.manufacturePojo(EstudianteEntity.class);
        EstudianteEntity result = estudiantePersistence.create(estudiante);
        Assert.assertNotNull(result);
        
        EstudianteEntity e = em.find(EstudianteEntity.class, result.getId());
        Assert.assertEquals(e.getCorreo(), result.getCorreo());
    }
    
    // Se prueba que la calificación promedio de un estudiante quede correctamente guardado
    @Test
    public void calificacionPromedioTest() {
        PodamFactory factory = new PodamFactoryImpl();
        EstudianteEntity estudiante = factory.manufacturePojo(EstudianteEntity.class);
        EstudianteEntity result = estudiantePersistence.create(estudiante);
        Assert.assertNotNull(result);
        
        EstudianteEntity e = em.find(EstudianteEntity.class, result.getId());
        Assert.assertEquals(e.getCalificacionPromedio(), result.getCalificacionPromedio(), 0.000001);
    }
    
    // Se prueba que el horario de trabajo de un estudiante quede correctamente guardado
    @Test
    public void horarioDeTrabajoTest() {
        PodamFactory factory = new PodamFactoryImpl();
        EstudianteEntity estudiante = factory.manufacturePojo(EstudianteEntity.class);
        EstudianteEntity result = estudiantePersistence.create(estudiante);
        Assert.assertNotNull(result);
        
        EstudianteEntity e = em.find(EstudianteEntity.class, result.getId());
        Assert.assertEquals(e.getNombre(), result.getNombre());
    }
    
    // Se prueba que el semestre que cursa de un estudiante quede correctamente guardado
    @Test
    public void semestreTest() {
       PodamFactory factory = new PodamFactoryImpl();
        EstudianteEntity estudiante = factory.manufacturePojo(EstudianteEntity.class);
        EstudianteEntity result = estudiantePersistence.create(estudiante);
        Assert.assertNotNull(result);
        
        EstudianteEntity e = em.find(EstudianteEntity.class, result.getId());
        Assert.assertEquals(e.getNombre(), result.getNombre());
    }
}
