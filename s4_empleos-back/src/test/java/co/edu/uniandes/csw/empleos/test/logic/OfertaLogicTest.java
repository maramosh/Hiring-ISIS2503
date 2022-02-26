/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.logic;
import co.edu.uniandes.csw.empleos.ejb.OfertaLogic;
import co.edu.uniandes.csw.empleos.entities.OfertaEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.OfertaPersistence;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.runner.RunWith;
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
 * Pruebas de logica de Ofertas
 *
 * @oferta ISIS2603
 */
@RunWith(Arquillian.class)
public class OfertaLogicTest {
    
    
    

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private OfertaLogic ofertaLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private final List<OfertaEntity> data = new ArrayList<>();

    /**
     * @return Devuelve el jar que Arquillian va a desplegar en Payara embebido.
     * El jar contiene las clases, el descriptor de la base de datos y el
     * archivo beans.xml para resolver la inyección de dependencias.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(OfertaEntity.class.getPackage())
                .addPackage(OfertaLogic.class.getPackage())
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
        for (int i = 0; i < 3; i++) {
            OfertaEntity entity = factory.manufacturePojo(OfertaEntity.class);
            em.persist(entity);
            
            data.add(entity);
        }
        
        
        
    }

    /**
     * Prueba para crear un Oferta.
     */
    @Test
    public void createOfertaTest() throws BusinessLogicException {
        OfertaEntity newEntity = factory.manufacturePojo(OfertaEntity.class);
        newEntity.setHorasDeTrabajo(3.0);
        newEntity.setPagoPorHora(5000.0);
        newEntity.setNumeroDeVacantes(2);
        newEntity.setTipoOferta(1);
        
        
        OfertaEntity result = ofertaLogic.createOferta(newEntity);
        Assert.assertNotNull(result);
        OfertaEntity entity = em.find(OfertaEntity.class, result.getId());
        Assert.assertEquals(newEntity.getId(), entity.getId());
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
     * Prueba para verificar la generacion de la excepcion al crear una oferta con un nombre nulo.
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createOfertaNombreNullTest() throws BusinessLogicException {
        OfertaEntity newEntity = factory.manufacturePojo(OfertaEntity.class);
        newEntity.setNombre(null);
         ofertaLogic.createOferta(newEntity);
    }
    /**
     * Prueba para verificar la generacion de la excepcion al crear una oferta con una categoria nula.
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createOfertaCategoriaNullTest() throws BusinessLogicException {
        OfertaEntity newEntity = factory.manufacturePojo(OfertaEntity.class);
        newEntity.setCategoria(null);
         ofertaLogic.createOferta(newEntity);
    }
    
     /**
     * Prueba para verificar la generacion de la excepcion al crear una oferta con una descripcion nula.
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createOfertaDescripcionNullTest() throws BusinessLogicException {
        OfertaEntity newEntity = factory.manufacturePojo(OfertaEntity.class);
        newEntity.setDescripcion(null);
         ofertaLogic.createOferta(newEntity);
    }
    
     /**
     * Prueba para verificar la generacion de la excepcion al crear una oferta con unas horas de trabajo invalidas.
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createOfertaHorasDeTrabajoInvalidaTest() throws BusinessLogicException {
        OfertaEntity newEntity = factory.manufacturePojo(OfertaEntity.class);
        newEntity.setHorasDeTrabajo(0.0);
         ofertaLogic.createOferta(newEntity);
    }
    
     /**
     * Prueba para verificar la generacion de la excepcion al crear una oferta con unas horas de trabajo invalidas.
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createOfertaNumeroDeVacantesInvalidaTest() throws BusinessLogicException {
        OfertaEntity newEntity = factory.manufacturePojo(OfertaEntity.class);
        newEntity.setNumeroDeVacantes(0);
         ofertaLogic.createOferta(newEntity);
    }

     /**
     * Prueba para verificar la generacion de la excepcion al crear una oferta con pago por hora invalido.
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createOfertaPagoPorHoraInvalidoTest() throws BusinessLogicException {
        OfertaEntity newEntity = factory.manufacturePojo(OfertaEntity.class);
        newEntity.setPagoPorHora(2000.0);
         ofertaLogic.createOferta(newEntity);
    }
    
    /**
     * Prueba para verificar la generacion de la excepcion al crear una oferta con un tipo invalido.
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createOfertaTipoInvalidoTest() throws BusinessLogicException {
        OfertaEntity newEntity = factory.manufacturePojo(OfertaEntity.class);
        newEntity.setTipoOferta(10);
         ofertaLogic.createOferta(newEntity);
    }
    /**
     * Prueba para consultar la lista de Ofertas.
     */
    @Test
    public void getOfertasTest() {
        List<OfertaEntity> list = ofertaLogic.getOfertas();
        Assert.assertEquals(data.size(), list.size());
        for (OfertaEntity entity : list) {
            boolean found = false;
            for (OfertaEntity storedEntity : data) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    /**
     * Prueba para consultar un Oferta.
     */
    @Test
    public void getOfertaTest() throws BusinessLogicException {
        OfertaEntity entity = data.get(0);
        OfertaEntity resultEntity = ofertaLogic.getOferta(entity.getId());
        Assert.assertNotNull(resultEntity);
        
        Assert.assertEquals(entity.getId(), resultEntity.getId());
        Assert.assertEquals(entity.getNombre(), resultEntity.getNombre());
        Assert.assertEquals(entity.getCategoria(), resultEntity.getCategoria());
        Assert.assertEquals(entity.getDescripcion(), resultEntity.getDescripcion());
        Assert.assertEquals(entity.getEstaAbierta(), resultEntity.getEstaAbierta());
        Assert.assertEquals(entity.getHorario(), resultEntity.getHorario());
        Assert.assertEquals(entity.getHorasDeTrabajo(), resultEntity.getHorasDeTrabajo());
        Assert.assertEquals(entity.getNumeroDeVacantes(), resultEntity.getNumeroDeVacantes());
        Assert.assertEquals(entity.getPagoPorHora(), resultEntity.getPagoPorHora());
        Assert.assertEquals(entity.getPorcentajePagoAdicional(), resultEntity.getPorcentajePagoAdicional());
        Assert.assertEquals(entity.getRequisitos(), resultEntity.getRequisitos());
        Assert.assertEquals(entity.getRutaImagen(), resultEntity.getRutaImagen());
        Assert.assertEquals(entity.getTiempoMaximoAplicacion(), resultEntity.getTiempoMaximoAplicacion());
        Assert.assertEquals(entity.getTipoOferta(), resultEntity.getTipoOferta());
    }

    /**
     * Prueba para actualizar un Oferta.
     */
    @Test
    public void updateOfertaTest() {
        OfertaEntity entity = data.get(0);
        OfertaEntity pojoEntity = factory.manufacturePojo(OfertaEntity.class);

        pojoEntity.setId(entity.getId());

        ofertaLogic.updateOferta(pojoEntity.getId(), pojoEntity);

        OfertaEntity resp = em.find(OfertaEntity.class, entity.getId());

        Assert.assertEquals(pojoEntity.getId(), resp.getId());
        Assert.assertEquals(pojoEntity.getNombre(), resp.getNombre());
        Assert.assertEquals(pojoEntity.getCategoria(), resp.getCategoria());
        Assert.assertEquals(pojoEntity.getDescripcion(), resp.getDescripcion());
        Assert.assertEquals(pojoEntity.getEstaAbierta(), resp.getEstaAbierta());
        Assert.assertEquals(pojoEntity.getHorario(), resp.getHorario());
        Assert.assertEquals(pojoEntity.getHorasDeTrabajo(), resp.getHorasDeTrabajo());
        Assert.assertEquals(pojoEntity.getNumeroDeVacantes(), resp.getNumeroDeVacantes());
        Assert.assertEquals(pojoEntity.getPagoPorHora(), resp.getPagoPorHora());
        Assert.assertEquals(pojoEntity.getPorcentajePagoAdicional(), resp.getPorcentajePagoAdicional());
        Assert.assertEquals(pojoEntity.getRequisitos(), resp.getRequisitos());
        Assert.assertEquals(pojoEntity.getRutaImagen(), resp.getRutaImagen());
        Assert.assertEquals(pojoEntity.getTiempoMaximoAplicacion(), resp.getTiempoMaximoAplicacion());
        Assert.assertEquals(pojoEntity.getTipoOferta(), resp.getTipoOferta());
    }
    

    /**
     * Prueba para eliminar un Oferta
     *
     * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
     */
    @Test
    public void deleteOfertaTest() throws BusinessLogicException {
        OfertaEntity entity = data.get(0);
        ofertaLogic.deleteOferta(entity.getId());
        OfertaEntity deleted = em.find(OfertaEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

   
    
    
}
