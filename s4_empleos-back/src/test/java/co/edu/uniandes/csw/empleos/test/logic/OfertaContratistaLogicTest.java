package co.edu.uniandes.csw.empleos.test.logic;

import co.edu.uniandes.csw.empleos.ejb.OfertaContratistaLogic;
import co.edu.uniandes.csw.empleos.ejb.OfertaLogic;
import co.edu.uniandes.csw.empleos.entities.ContratistaEntity;
import co.edu.uniandes.csw.empleos.entities.OfertaEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.ContratistaPersistence;
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
public class OfertaContratistaLogicTest {
    
     private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private OfertaLogic ofertaLogic;
    @Inject
    private OfertaContratistaLogic ofertaContratistaLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private List<ContratistaEntity> data = new ArrayList<ContratistaEntity>();

    private List<OfertaEntity> ofertasData = new ArrayList();

    /**
     * @return Devuelve el jar que Arquillian va a desplegar en Payara embebido.
     * El jar contiene las clases, el descriptor de la base de datos y el
     * archivo beans.xml para resolver la inyección de dependencias.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(ContratistaEntity.class.getPackage())
                .addPackage(OfertaLogic.class.getPackage())
                .addPackage(ContratistaPersistence.class.getPackage())
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
            em.joinTransaction();
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
        em.createQuery("delete from ContratistaEntity").executeUpdate();
        
        
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            OfertaEntity oferta = factory.manufacturePojo(OfertaEntity.class);
            em.persist(oferta);
            ofertasData.add(oferta);
        }
        for (int i = 0; i < 3; i++) {
            ContratistaEntity entity = factory.manufacturePojo(ContratistaEntity.class);
            em.persist(entity);
            data.add(entity);
            if (i == 0) {
                ofertasData.get(i).setContratista(entity);
            }
        }
    }

   
    /**
     * Prueba para obtener una colección de instancias de ofertas asociadas a
     * una instancia Contratista.
     *
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test
    public void replaceContratistaTest() throws BusinessLogicException {
        OfertaEntity entity = ofertasData.get(0);
        System.out.println(entity.getDescripcion());
        ofertaContratistaLogic.replaceContratista(entity.getId(), data.get(0).getId());
        entity = ofertaLogic.getOferta(entity.getId());
        Assert.assertEquals(entity.getContratista(), data.get(0));
    }

    /**
     * Prueba para desasociar una oferta existente de un Contratista existente
     *
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test
    public void removeOfertasTest() throws BusinessLogicException {
        ofertaContratistaLogic.removeContratista(ofertasData.get(0).getId());
        OfertaEntity response = ofertaLogic.getOferta(ofertasData.get(0).getId());
        Assert.assertNull(response.getContratista());
    }
    
}
