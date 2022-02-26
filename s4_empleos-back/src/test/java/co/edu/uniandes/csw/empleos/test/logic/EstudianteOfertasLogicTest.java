/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.logic;

import co.edu.uniandes.csw.empleos.ejb.EstudianteOfertasLogic;
import co.edu.uniandes.csw.empleos.ejb.EstudianteLogic;
import co.edu.uniandes.csw.empleos.ejb.OfertaLogic;
import co.edu.uniandes.csw.empleos.entities.EstudianteEntity;
import co.edu.uniandes.csw.empleos.entities.OfertaEntity;
import co.edu.uniandes.csw.empleos.entities.TrabajoEntity;
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
 * @author David Dominguez
 */
@RunWith(Arquillian.class)
public class EstudianteOfertasLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private EstudianteLogic estudianteLogic;
    @Inject
    private OfertaLogic ofertaLogic;

    @Inject
    private EstudianteOfertasLogic estudianteOfertasLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private EstudianteEntity estudiante = new EstudianteEntity();

    private List<EstudianteEntity> caldata = new ArrayList<EstudianteEntity>();
    private List<OfertaEntity> data = new ArrayList<OfertaEntity>();

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
                .addPackage(EstudianteOfertasLogic.class.getPackage())
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
        em.createQuery("delete from EstudianteEntity").executeUpdate();
        em.createQuery("delete from OfertaEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {

        estudiante = factory.manufacturePojo(EstudianteEntity.class);
        estudiante.setId(1L);
        estudiante.setOfertas(new ArrayList<>());
        em.persist(estudiante);

        for (int i = 0; i < 3; i++) {
            OfertaEntity entity = factory.manufacturePojo(OfertaEntity.class);
            entity.setEstudiantes(new ArrayList<>());
            entity.getEstudiantes().add(estudiante);
            em.persist(entity);
            data.add(entity);
            estudiante.getOfertas().add(entity);
        }
    }

    /**
     * Prueba para asociar un estudiante a una oferta.
     *
     *
     * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
     */
    @Test
    public void addOfertaTest() throws BusinessLogicException {
        estudiante.setOfertas(null);
        estudiante.setCorreo("akjwd@uniandes.edu.co");
        estudiante.setSemestre(Math.min(Math.abs(estudiante.getSemestre()) + 1, 12));
        estudiante.setNombre(estudiante.getNombre() + "a");
        estudiante.setCarrera(estudiante.getCarrera() + "a");
        estudianteLogic.updateEstudiante(estudiante);
        OfertaEntity newOferta = factory.manufacturePojo(OfertaEntity.class);
        newOferta.setHorasDeTrabajo((double) 5);
        newOferta.setNumeroDeVacantes(5);
        newOferta.setPagoPorHora((double) 3600);
        newOferta.setTipoOferta(1);
        ofertaLogic.createOferta(newOferta);
        OfertaEntity ofertaEntity = estudianteOfertasLogic.addOferta(estudiante.getId(), newOferta.getId());
        Assert.assertNotNull(ofertaEntity);

        Assert.assertEquals(ofertaEntity.getId(), newOferta.getId());
        Assert.assertEquals(ofertaEntity.getTipoOferta(), newOferta.getTipoOferta());
        Assert.assertEquals(ofertaEntity.getEstaAbierta(), newOferta.getEstaAbierta());
        Assert.assertEquals(ofertaEntity.getNombre(), newOferta.getNombre());
        Assert.assertEquals(ofertaEntity.getContratista(), newOferta.getContratista());

        OfertaEntity lasOferta = estudianteOfertasLogic.getOferta(estudiante.getId(), newOferta.getId());

        Assert.assertEquals(lasOferta.getId(), newOferta.getId());
        Assert.assertEquals(lasOferta.getTipoOferta(), newOferta.getTipoOferta());
        Assert.assertEquals(lasOferta.getEstaAbierta(), newOferta.getEstaAbierta());
        Assert.assertEquals(lasOferta.getNombre(), newOferta.getNombre());
        Assert.assertEquals(lasOferta.getContratista(), newOferta.getContratista());
    }

    /**
     * Prueba para remplazar las instancias de Books asociadas a una instancia
     * de Editorial.
     */
    @Test
    public void replaceOfertaTest() throws BusinessLogicException {
        try {
            utx.begin();
            em.joinTransaction();
            EstudianteEntity entity = estudiante;
            OfertaEntity e = data.get(0);

            ArrayList<OfertaEntity> ofertas = new ArrayList<>();
            ofertas.add(data.get(0));

            estudianteOfertasLogic.replaceOfertas(entity.getId(), ofertas);
            entity = estudianteLogic.getEstudiante(entity.getId());

            Assert.assertEquals(entity.getOfertas(), ofertas);
        } catch (Exception exx) {
            Assert.fail(exx.getMessage());
            exx.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Prueba para desasociar una Oferta existente de un Estudiante existente
     *
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test
    public void removeOfertasTest() throws BusinessLogicException {
        try {
            utx.begin();
            em.joinTransaction();
           insertData();
            EstudianteEntity entity = estudiante;
            long id1 = entity.getId();
            entity.setOfertas(data);
            long id2 = entity.getOfertas().get(0).getId();
            entity.setCorreo("akjwd@uniandes.edu.co");
            entity.setSemestre(Math.min(Math.abs(entity.getSemestre()) + 1, 12));
            entity.setNombre(entity.getNombre() + "a");
            entity.setCarrera(entity.getCarrera() + "a");
            estudianteLogic.updateEstudiante(entity);

            estudianteOfertasLogic.removeOferta(id1, id2);
            EstudianteEntity response = estudianteLogic.getEstudiante(id1);
            OfertaEntity of = null;
            for (OfertaEntity e : response.getOfertas()) {
                if (e.getId() == id2) {
                    of = e;
                }
            }
            Assert.assertNull(of);
        } catch (Exception exx) {
            Assert.fail(exx.getMessage());
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
