package co.edu.uniandes.csw.empleos.test.persistence;

import co.edu.uniandes.csw.empleos.entities.FacturaEntity;
import co.edu.uniandes.csw.empleos.entities.OfertaEntity;
import co.edu.uniandes.csw.empleos.entities.TrabajoEntity;
import co.edu.uniandes.csw.empleos.persistence.TrabajoPersistence;
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
 *
 * @author David Dominguez
 */
@RunWith(Arquillian.class)
public class TrabajoPersistenceTest {

    // Se relaciona la base de datos con el entitymanager
    @PersistenceContext(unitName = "empleosPU")
    public EntityManager em;

    //Guarda la lista de trabajos que Podam genere
    private ArrayList<TrabajoEntity> trabajos;

    // Se relaciona Arquilliean con las clases que se pobrarán
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(TrabajoEntity.class.getPackage())
                .addPackage(TrabajoPersistence.class.getPackage())
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }

    // Atributo que representa al objeto encargado de manejar la persistencia de Estudiante
    @Inject
    TrabajoPersistence tp;

    @Inject
    UserTransaction u;

    @Before
    public void setup() {
        try {
            trabajos = new ArrayList<TrabajoEntity>();
            u.begin();
            em.joinTransaction();
            em.createQuery("delete from TrabajoEntity").executeUpdate();
            PodamFactory factory = new PodamFactoryImpl();
            for (int i = 0; i < 5; i++) {
                TrabajoEntity entity = factory.manufacturePojo(TrabajoEntity.class);
                trabajos.add(entity);
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

    // Se prueba que se cree y guarde exitosamente el trabajo
    @Test
    public void crearTrabajoTest() {
        PodamFactory factory = new PodamFactoryImpl();
        TrabajoEntity trabajo = factory.manufacturePojo(TrabajoEntity.class);
        TrabajoEntity result = tp.create(trabajo);

        Assert.assertNotNull(result);

        TrabajoEntity t = em.find(TrabajoEntity.class, result.getId());
        Assert.assertEquals(t.getId(), result.getId());
    }

    // Se prueba que se actualice un trabajo
    @Test
    public void updateTest() {
        TrabajoEntity trabajo = trabajos.get(0);
        PodamFactory factory = new PodamFactoryImpl();
        TrabajoEntity nuevoTrabajo = factory.manufacturePojo(TrabajoEntity.class);
        nuevoTrabajo.setId(trabajo.getId());
        tp.update(nuevoTrabajo);
        TrabajoEntity resp = em.find(TrabajoEntity.class, trabajo.getId());
        Assert.assertEquals(nuevoTrabajo.isVerificado(), resp.isVerificado());
        Assert.assertEquals(nuevoTrabajo.isCumplido(), resp.isCumplido());
    }

    // Se prueba que se obtengan todos los trabajos
    @Test
    public void findAllTest() {
        List<TrabajoEntity> mTrabajos = tp.findAll();
        Assert.assertEquals(trabajos.size(), mTrabajos.size());
        for (TrabajoEntity e : mTrabajos) {
            boolean found = false;
            for (TrabajoEntity entity : trabajos) {
                if (e.getId().equals(entity.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    /**
     * Se prueba que se obtenga un trabajo
     */
    @Test
    public void getTrabajoTest() {
        TrabajoEntity resp = trabajos.get(2);
        TrabajoEntity nuevoTrabajo = tp.read(resp.getId());
        Assert.assertNotNull(nuevoTrabajo);
        Assert.assertEquals(nuevoTrabajo.isVerificado(), resp.isVerificado());
        Assert.assertEquals(nuevoTrabajo.isCumplido(), resp.isCumplido());
    }

    /**
     * Se prueba que se elimine correctamente un trabajo
     */
    @Test
    public void deleteTest() {
        TrabajoEntity entity = trabajos.get(0);
        tp.delete(entity.getId());
        TrabajoEntity deleted = em.find(TrabajoEntity.class, entity.getId());
        Assert.assertNull(deleted);

        TrabajoEntity entity2 = trabajos.get(0);
        tp.delete(entity2);
        TrabajoEntity deleted2 = em.find(TrabajoEntity.class, entity2.getId());
        Assert.assertNull(deleted2);
    }

    // Se prueba que se cree y guarde exitosamente el atributo verificado de un trabajo
    @Test
    public void verificarTest() {
        PodamFactory factory = new PodamFactoryImpl();
        TrabajoEntity trabajo = factory.manufacturePojo(TrabajoEntity.class);
        TrabajoEntity result = tp.create(trabajo);

        Assert.assertNotNull(result);

        TrabajoEntity t = em.find(TrabajoEntity.class, result.getId());
        Assert.assertEquals(t.isVerificado(), result.isVerificado());
    }

    // Se prueba que se cree y guarde exitosamente el atributo cumplido de un trabajo
    @Test
    public void cumplidoTest() {
        PodamFactory factory = new PodamFactoryImpl();
        TrabajoEntity trabajo = factory.manufacturePojo(TrabajoEntity.class);
        TrabajoEntity result = tp.create(trabajo);

        Assert.assertNotNull(result);

        TrabajoEntity t = em.find(TrabajoEntity.class, result.getId());
        Assert.assertEquals(t.isCumplido(), result.isCumplido());
    }

    @Test
    public void verificarAsociacionesFactura() {
        try {
            u.begin();
            em.joinTransaction();
            PodamFactory factory = new PodamFactoryImpl();
            TrabajoEntity trabajo = factory.manufacturePojo(TrabajoEntity.class);
            long id = trabajo.getId();
            FacturaEntity factura = factory.manufacturePojo(FacturaEntity.class);
            trabajo.setFactura(factura);
            em.persist(factura);
            em.persist(trabajo);
            TrabajoEntity result = tp.create(trabajo);

            TrabajoEntity e = tp.read(id);
            Assert.assertEquals(e.getFactura().getValor(), factura.getValor());
            Assert.assertEquals(e.getFactura().getFecha(), factura.getFecha());
            Assert.assertEquals(e.getFactura().getId(), factura.getId());
            u.commit();
        } catch (Exception exx) {
            Assert.fail("No debería haber lanzado excepción");
            exx.printStackTrace();
            try {
                u.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Test
    public void verificarAsociacionesOferta() {
        try {
            u.begin();
            em.joinTransaction();
            PodamFactory factory = new PodamFactoryImpl();
            TrabajoEntity trabajo = factory.manufacturePojo(TrabajoEntity.class);
            long id = trabajo.getId();
            OfertaEntity oferta = factory.manufacturePojo(OfertaEntity.class);
            trabajo.setOferta(oferta);
            em.persist(oferta);
            em.persist(trabajo);
            TrabajoEntity result = tp.create(trabajo);

            TrabajoEntity e = tp.read(id);
            Assert.assertEquals(e.getOferta().getCategoria(), oferta.getCategoria());
            Assert.assertEquals(e.getOferta().getCategoria(), oferta.getCategoria());
            Assert.assertEquals(e.getOferta().getCategoria(), oferta.getCategoria());
            u.commit();
        } catch (Exception exx) {
            Assert.fail("No debería haber lanzado excepción");
            exx.printStackTrace();
            try {
                u.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
