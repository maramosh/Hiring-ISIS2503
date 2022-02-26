/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.logic;

import co.edu.uniandes.csw.empleos.ejb.TrabajoOfertaLogic;
import co.edu.uniandes.csw.empleos.ejb.TrabajoLogic;
import co.edu.uniandes.csw.empleos.ejb.OfertaLogic;
import co.edu.uniandes.csw.empleos.entities.TrabajoEntity;
import co.edu.uniandes.csw.empleos.entities.CalificacionEntity;
import co.edu.uniandes.csw.empleos.entities.OfertaEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.OfertaPersistence;
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
 * @author David Domínguez
 */
@RunWith(Arquillian.class)
public class TrabajoOfertasLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private TrabajoLogic trabajoLogic;
    @Inject
    private OfertaLogic ofertaLogic;

    @Inject
    private TrabajoOfertaLogic trabajoOfertaLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private List<TrabajoEntity> caldata = new ArrayList<TrabajoEntity>();
    private List<OfertaEntity> data = new ArrayList<OfertaEntity>();

    /**
     * @return Devuelve el jar que Arquillian va a desplegar en Payara embebido.
     * El jar contiene las clases, el descriptor de la base de datos y el
     * archivo beans.xml para resolver la inyección de dependencias.
     */
    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(TrabajoEntity.class.getPackage())
                .addPackage(TrabajoLogic.class.getPackage())
                .addPackage(TrabajoPersistence.class.getPackage())
                .addPackage(OfertaEntity.class.getPackage())
                .addPackage(OfertaPersistence.class.getPackage())
                .addPackage(OfertaLogic.class.getPackage())
                .addPackage(TrabajoOfertaLogic.class.getPackage())
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
        em.createQuery("delete from TrabajoEntity").executeUpdate();
        em.createQuery("delete from OfertaEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            TrabajoEntity trabajos = factory.manufacturePojo(TrabajoEntity.class);
            em.persist(trabajos);
            caldata.add(trabajos);
        }
        for (int i = 0; i < 3; i++) {
            OfertaEntity entity = factory.manufacturePojo(OfertaEntity.class);
            em.persist(entity);
            data.add(entity);
            if (i == 0) {
                caldata.get(i).setOferta(entity);
            }
        }

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
            TrabajoEntity entity = caldata.get(0);
            trabajoOfertaLogic.replaceOferta(entity.getId(), data.get(0).getId());
            entity = trabajoLogic.getTrabajo(entity.getId());
            Assert.assertEquals(entity.getOferta().getId(), data.get(0).getId());
        } catch (Exception exx) {
            Assert.fail("No debería haber lanzado excepción");
            exx.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
    
    
    /**
     * Prueba para desasociar una Oferta existente de un Trabajo existente
     *
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test
    public void removeOfertaTest() throws BusinessLogicException {
        trabajoOfertaLogic.removeOferta(caldata.get(0).getId());
        TrabajoEntity response = trabajoLogic.getTrabajo(caldata.get(0).getId());
        Assert.assertNull(response.getOferta());
    }
    
    /**
     * Prueba para asociar una cuenta factura a un trabajo
     *
     *
     * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
     */
    @Test
    public void addOfertaTest() throws BusinessLogicException {
        TrabajoEntity trabajo = caldata.get(0);
        OfertaEntity oferta = factory.manufacturePojo(OfertaEntity.class);
        oferta.setHorasDeTrabajo((double) 5);
        oferta.setNumeroDeVacantes(5);
        oferta.setPagoPorHora((double) 3600);
        oferta.setTipoOferta(1);
        ofertaLogic.createOferta(oferta);
        OfertaEntity ofertaEntity = trabajoOfertaLogic.addOferta(trabajo.getId(), oferta.getId());
        Assert.assertNotNull(ofertaEntity);
        Assert.assertEquals(ofertaEntity.getId(), oferta.getId());
    }

}
