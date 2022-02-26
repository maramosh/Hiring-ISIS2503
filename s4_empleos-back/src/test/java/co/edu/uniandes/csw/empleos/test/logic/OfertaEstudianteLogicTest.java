/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.logic;

import co.edu.uniandes.csw.empleos.ejb.EstudianteLogic;
import co.edu.uniandes.csw.empleos.ejb.OfertaEstudianteLogic;
import co.edu.uniandes.csw.empleos.ejb.OfertaLogic;
import co.edu.uniandes.csw.empleos.entities.EstudianteEntity;
import co.edu.uniandes.csw.empleos.entities.OfertaEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.EstudiantePersistence;
import co.edu.uniandes.csw.empleos.persistence.OfertaPersistence;
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
 * @author Estudiante
 */
@RunWith(Arquillian.class)
public class OfertaEstudianteLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private OfertaEstudianteLogic ofertaEstudianteLogic;

    @Inject
    private EstudianteLogic estudianteLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private OfertaEntity oferta = new OfertaEntity();
    private List<EstudianteEntity> data = new ArrayList<>();

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
                .addPackage(OfertaEntity.class.getPackage())
                .addPackage(OfertaPersistence.class.getPackage())
                .addPackage(OfertaLogic.class.getPackage())
                .addPackage(OfertaEstudianteLogic.class.getPackage())
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
        em.createQuery("delete from OfertaEntity").executeUpdate();
        em.createQuery("delete from EstudianteEntity").executeUpdate();
        
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {

        oferta = factory.manufacturePojo(OfertaEntity.class);
        oferta.setId(1L);
        oferta.setEstudiantes(new ArrayList<>());
        em.persist(oferta);

        for (int i = 0; i < 3; i++) {
            EstudianteEntity entity = factory.manufacturePojo(EstudianteEntity.class);
            entity.setOfertas(new ArrayList<>());
            entity.getOfertas().add(oferta);
            em.persist(entity);
            data.add(entity);
            oferta.getEstudiantes().add(entity);
        }
    }

    /**
     * Prueba para asociar un estudiante a una oferta.
     *
     */
    @Test
    public void addEstudianteTest() throws BusinessLogicException {
        EstudianteEntity newEstudiante = factory.manufacturePojo(EstudianteEntity.class);
        newEstudiante.setCorreo("algo@uniandes.edu.co");
        newEstudiante.setSemestre(2);
        estudianteLogic.crearEstudiante(newEstudiante);
        System.out.println(estudianteLogic.getEstudiante(newEstudiante.getId()).getNombre() +"  oferta "+ oferta.getId());
        EstudianteEntity estudianteEntity = ofertaEstudianteLogic.addEstudiante(oferta.getId(), newEstudiante.getId());
        Assert.assertNotNull(estudianteEntity);
        Assert.assertEquals(estudianteEntity.getId(), newEstudiante.getId());
        Assert.assertEquals(estudianteEntity.getCorreo(), newEstudiante.getCorreo());
        Assert.assertEquals(estudianteEntity.getNombre(), newEstudiante.getNombre());
        Assert.assertEquals(estudianteEntity.getSemestre(), newEstudiante.getSemestre());
       
        EstudianteEntity lastEstudiante = ofertaEstudianteLogic.getEstudiante(oferta.getId(), estudianteEntity.getId());
        
        System.out.println(estudianteEntity.getId() +"  oferta "+ oferta.getId()+" last "+lastEstudiante);
        Assert.assertEquals(lastEstudiante.getId(), estudianteEntity.getId());
        Assert.assertEquals(lastEstudiante.getCorreo(), newEstudiante.getCorreo());
        Assert.assertEquals(lastEstudiante.getNombre(), newEstudiante.getNombre());
        Assert.assertEquals(lastEstudiante.getSemestre(), newEstudiante.getSemestre());
    }

    /**
     * Prueba para consultar la lista de Books de un autor.
     */
    @Test
    public void getEstudiantesTest() {
        List<EstudianteEntity> estudiantesEntity = ofertaEstudianteLogic.getEstudiantes(oferta.getId());

        Assert.assertEquals(data.size(), estudiantesEntity.size());

        for (int i = 0; i < data.size(); i++) {
            Assert.assertTrue(estudiantesEntity.contains(data.get(0)));
        }
    }

    /**
     * Prueba para cpnsultar un libro de un autor.
     *
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test
    public void getEstudianteTest() throws BusinessLogicException {
        EstudianteEntity estudianteEntity = data.get(0);
        EstudianteEntity estudiante = ofertaEstudianteLogic.getEstudiante(oferta.getId(), estudianteEntity.getId());
        Assert.assertNotNull(estudiante);

        Assert.assertEquals(estudianteEntity.getId(), estudiante.getId());
        Assert.assertEquals(estudianteEntity.getCorreo(), estudiante.getCorreo());
        Assert.assertEquals(estudianteEntity.getNombre(), estudiante.getNombre());
        Assert.assertEquals(estudianteEntity.getSemestre(), estudiante.getSemestre());
    }

    /**
     * Prueba para actualizar los libros de un autor.
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test
    public void replaceEstudiantesTest() throws BusinessLogicException {
        List<EstudianteEntity> nuevaLista = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            EstudianteEntity entity = factory.manufacturePojo(EstudianteEntity.class);
            entity.setOfertas(new ArrayList<>());
            entity.setCorreo("jdsh@uniandes.edu.co");
            entity.setSemestre(6);
            entity.getOfertas().add(oferta);
            estudianteLogic.crearEstudiante(entity);
            nuevaLista.add(entity);
        }
        ofertaEstudianteLogic.replaceEstudiantes(oferta.getId(), nuevaLista);
        List<EstudianteEntity> estudianteEntities = ofertaEstudianteLogic.getEstudiantes(oferta.getId());
        for (EstudianteEntity aNuevaLista : nuevaLista) {
            Assert.assertTrue(estudianteEntities.contains(aNuevaLista));
        }
    }

    /**
     * Prueba remover un estudiante de una oferta.
     *
     */
    @Test
    public void removeEstudianteTest() throws BusinessLogicException {
        EstudianteEntity newEstudiante = factory.manufacturePojo(EstudianteEntity.class);
        newEstudiante.setCorreo("algo@uniandes.edu.co");
        newEstudiante.setSemestre(2);
        estudianteLogic.crearEstudiante(newEstudiante);
        System.out.println(estudianteLogic.getEstudiante(newEstudiante.getId()).getNombre() +"  oferta "+ oferta.getId());
        EstudianteEntity estudianteEntity = ofertaEstudianteLogic.addEstudiante(oferta.getId(), newEstudiante.getId());
        
        ofertaEstudianteLogic.removeEstudiante(oferta.getId(), estudianteEntity.getId());
        
            
                
                
            
        
        Assert.assertTrue(ofertaEstudianteLogic.getEstudiantes(oferta.getId()).indexOf(estudianteEntity)>=0);
    }
    
}
