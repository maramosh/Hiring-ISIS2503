/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.persistence;


import co.edu.uniandes.csw.empleos.entities.OfertaEntity;
import co.edu.uniandes.csw.empleos.persistence.OfertaPersistence;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.junit.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de persistencia de Oferta
 *
 * @author ISIS2603
 */
@RunWith(Arquillian.class)
public class OfertaPersistenceTest {
    
    @Inject
    private OfertaPersistence ofertaPersistence;

    @PersistenceContext
    private EntityManager em;
    

    @Inject
    UserTransaction utx;
    
    private List<OfertaEntity> data = new ArrayList<OfertaEntity>();

    /**
     * @return Devuelve el jar que Arquillian va a desplegar en Payara embebido.
     * El jar contiene las clases, el descriptor de la base de datos y el
     * archivo beans.xml para resolver la inyección de dependencias.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(OfertaEntity.class.getPackage())
                .addPackage(OfertaPersistence.class.getPackage())
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
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {
        PodamFactory factory = new PodamFactoryImpl();
        for (int i = 0; i < 3; i++) {
            OfertaEntity entity = factory.manufacturePojo(OfertaEntity.class);

            em.persist(entity);
            data.add(entity);
        }
    }

    /**
     * Prueba para crear una oferta.
     */
    @Test
    public void createOfertaTest() {
        PodamFactory factory = new PodamFactoryImpl();
        OfertaEntity newEntity = factory.manufacturePojo(OfertaEntity.class);
        OfertaEntity result = ofertaPersistence.create(newEntity);
        

        Assert.assertNotNull(result);

        OfertaEntity entity = em.find(OfertaEntity.class, result.getId());

        Assert.assertEquals(newEntity.getNombre(), entity.getNombre());
        Assert.assertEquals(newEntity.getCategoria(), entity.getCategoria());
        Assert.assertEquals(newEntity.getDescripcion(), entity.getDescripcion());
        Assert.assertEquals(newEntity.getEstaAbierta(), entity.getEstaAbierta());
        Assert.assertEquals(newEntity.getHorario(), entity.getHorario());
        Assert.assertEquals(newEntity.getHorasDeTrabajo(), entity.getHorasDeTrabajo());
        Assert.assertEquals(newEntity.getNumeroDeVacantes(), entity.getNumeroDeVacantes());
        Assert.assertEquals(newEntity.getPagoPorHora(), entity.getPagoPorHora());
        Assert.assertEquals(newEntity.getPorcentajePagoAdicional(), entity.getPorcentajePagoAdicional());
        Assert.assertEquals(newEntity.getRequisitos(), entity.getRequisitos());
        Assert.assertEquals(newEntity.getRutaImagen(), entity.getRutaImagen());
        Assert.assertEquals(newEntity.getTiempoMaximoAplicacion(), entity.getTiempoMaximoAplicacion());
        Assert.assertEquals(newEntity.getTipoOferta(), entity.getTipoOferta());
        
        
        
    }

    /**
     * Prueba para consultar la lista de Ofertas.
     */
    @Test
    public void getOfertasTest() {
        List<OfertaEntity> list = ofertaPersistence.findAll();
        Assert.assertEquals(data.size(), list.size());
        for (OfertaEntity ent : list) {
            boolean found = false;
            for (OfertaEntity entity : data) {
                if (ent.getId().equals(entity.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    /**
     * Prueba para consultar un Contratista.
     */
    @Test
    public void getOfertaTest() {
        OfertaEntity entity = data.get(0);
        OfertaEntity newEntity = ofertaPersistence.find(entity.getId());
        Assert.assertNotNull(newEntity);
        
        
        Assert.assertEquals(entity.getNombre(), newEntity.getNombre());
        Assert.assertEquals(entity.getCategoria(), newEntity.getCategoria());
        Assert.assertEquals(entity.getDescripcion(), newEntity.getDescripcion());
        Assert.assertEquals(entity.getEstaAbierta(), newEntity.getEstaAbierta());
        Assert.assertEquals(entity.getHorario(), newEntity.getHorario());
        Assert.assertEquals(entity.getHorasDeTrabajo(), newEntity.getHorasDeTrabajo());
        Assert.assertEquals(entity.getNumeroDeVacantes(), newEntity.getNumeroDeVacantes());
        Assert.assertEquals(entity.getPagoPorHora(), newEntity.getPagoPorHora());
        Assert.assertEquals(entity.getPorcentajePagoAdicional(), newEntity.getPorcentajePagoAdicional());
        Assert.assertEquals(entity.getRequisitos(), newEntity.getRequisitos());
        Assert.assertEquals(entity.getRutaImagen(), newEntity.getRutaImagen());
        Assert.assertEquals(entity.getTiempoMaximoAplicacion(), newEntity.getTiempoMaximoAplicacion());
        Assert.assertEquals(entity.getTipoOferta(), newEntity.getTipoOferta());
    }

    /**
     * Prueba para eliminar una Oferta.
     */
    @Test
    public void deleteOfertaTest() {
        OfertaEntity entity = data.get(0);
        ofertaPersistence.delete(entity.getId());
        OfertaEntity deleted = em.find(OfertaEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

    /**
     * Prueba para actualizar una Oferta.
     */
    @Test
    public void updateOfertaTest() {
        OfertaEntity entity = data.get(0);
        PodamFactory factory = new PodamFactoryImpl();
        OfertaEntity newEntity = factory.manufacturePojo(OfertaEntity.class);

        newEntity.setId(entity.getId());

        ofertaPersistence.update(newEntity);

        OfertaEntity resp = em.find(OfertaEntity.class, entity.getId());

        
        
        Assert.assertEquals(newEntity.getNombre(), resp.getNombre());
        Assert.assertEquals(newEntity.getCategoria(), resp.getCategoria());
        Assert.assertEquals(newEntity.getDescripcion(), resp.getDescripcion());
        Assert.assertEquals(newEntity.getEstaAbierta(), resp.getEstaAbierta());
        Assert.assertEquals(newEntity.getHorario(), resp.getHorario());
        Assert.assertEquals(newEntity.getHorasDeTrabajo(), resp.getHorasDeTrabajo());
        Assert.assertEquals(newEntity.getNumeroDeVacantes(), resp.getNumeroDeVacantes());
        Assert.assertEquals(newEntity.getPagoPorHora(), resp.getPagoPorHora());
        Assert.assertEquals(newEntity.getPorcentajePagoAdicional(), resp.getPorcentajePagoAdicional());
        Assert.assertEquals(newEntity.getRequisitos(), resp.getRequisitos());
        Assert.assertEquals(newEntity.getRutaImagen(), resp.getRutaImagen());
        Assert.assertEquals(newEntity.getTiempoMaximoAplicacion(), resp.getTiempoMaximoAplicacion());
        Assert.assertEquals(newEntity.getTipoOferta(), resp.getTipoOferta());
    }

    
    
}
