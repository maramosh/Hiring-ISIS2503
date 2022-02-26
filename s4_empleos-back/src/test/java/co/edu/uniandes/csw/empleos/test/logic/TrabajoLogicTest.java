package co.edu.uniandes.csw.empleos.test.logic;

import co.edu.uniandes.csw.empleos.entities.TrabajoEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.ejb.TrabajoLogic;
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
 *
 * @author David Dominguez
 */
@RunWith(Arquillian.class)
public class TrabajoLogicTest {

    @Inject
    private TrabajoLogic trabajoLogic;

    @PersistenceContext
    private EntityManager em;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<TrabajoEntity> datos = new ArrayList<TrabajoEntity>();

    @Inject
    private UserTransaction utx;

    @Deployment
    private static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(TrabajoEntity.class.getPackage())
                .addPackage(TrabajoLogic.class.getPackage())
                .addPackage(TrabajoPersistence.class.getPackage())
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
     * Se verifica que se haya creado correctamente un trabajo.
     *
     * @throws BusinessLogicException Excepcion untilizada para representar
     * errores en la lógica del negocio.
     */
    @Test
    public void crearTrabajo() {
        TrabajoEntity newEntity = factory.manufacturePojo(TrabajoEntity.class);
        TrabajoEntity result = trabajoLogic.crearTrabajo(newEntity);
        Assert.assertNotNull(result);
        TrabajoEntity entity = em.find(TrabajoEntity.class, result.getId());
        Assert.assertEquals(entity.isVerificado(), result.isVerificado());
        Assert.assertEquals(entity.isCumplido(), result.isCumplido());
    }

    @Test
    public void updateTrabajo() {
        TrabajoEntity newEntity = factory.manufacturePojo(TrabajoEntity.class);
        boolean cumplido = newEntity.isCumplido();
        boolean verificado = newEntity.isVerificado();
        long id = newEntity.getId();
        trabajoLogic.crearTrabajo(newEntity);
        newEntity.setVerificado(!newEntity.isVerificado());
        newEntity.setCumplido(!newEntity.isCumplido());
        newEntity = trabajoLogic.updateTrabajo(newEntity);
        Assert.assertEquals(newEntity.isVerificado(), !verificado);
        Assert.assertEquals(newEntity.isCumplido(), !cumplido);
    }

    @Test
    public void readTrabajo() {
        TrabajoEntity newEntity = factory.manufacturePojo(TrabajoEntity.class);
        trabajoLogic.crearTrabajo(newEntity);
        TrabajoEntity e = trabajoLogic.getTrabajo(newEntity.getId());
        Assert.assertEquals(e.getId(), newEntity.getId());
    }

    @Test
    public void deleteTrabajo() {
        try {
            utx.begin();
            clearData();
            TrabajoEntity newEntity = factory.manufacturePojo(TrabajoEntity.class);
            long id = newEntity.getId();
            newEntity = trabajoLogic.crearTrabajo(newEntity);
            trabajoLogic.deleteTrabajo(id);
            TrabajoEntity e = trabajoLogic.getTrabajo(id);
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

    //Esta clase no tiene reglas de negocio
}
