/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.logic;

import co.edu.uniandes.csw.empleos.ejb.ContratistaLogic;
import co.edu.uniandes.csw.empleos.ejb.ContratistaOfertasLogic;
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
 * @author je.hernandezr
 */
@RunWith(Arquillian.class)
public class ContratistaOfertasLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private ContratistaLogic contratistaLogic;

    @Inject
    private ContratistaOfertasLogic contratistaOfertasLogic;

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
                .addPackage(ContratistaLogic.class.getPackage())
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
     * Prueba para asociar una oferta existente a un contratista.
     */
    @Test
    public void addOfertasTest() throws BusinessLogicException {
        ContratistaEntity entity = data.get(0);
        OfertaEntity ofertaEntity = ofertasData.get(1);
        OfertaEntity response = contratistaOfertasLogic.addOferta(entity.getId(),ofertaEntity.getId() );

        Assert.assertNotNull(response);
        Assert.assertEquals(ofertaEntity.getId(), response.getId());
    }

    /**
     * Prueba para obtener una colección de instancias de ofertas asociadas a
     * una instancia Contratista.
     */
    @Test
    public void getOfertasTest() {
        List<OfertaEntity> list = contratistaOfertasLogic.getOfertas(data.get(0).getId());
        Assert.assertEquals(1, list.size());
    }

    /**
     * Prueba para obtener una colección de instancias de ofertas asociadas a
     * una instancia Contratista.
     *
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test
    public void getOfertaTest() throws BusinessLogicException {
        ContratistaEntity entity = data.get(0);
        OfertaEntity ofertaEntity = ofertasData.get(0);
        OfertaEntity response = contratistaOfertasLogic.getOferta(entity.getId(), ofertaEntity.getId());

        Assert.assertEquals(ofertaEntity.getId(), response.getId());
        Assert.assertEquals(ofertaEntity.getCategoria(), response.getCategoria());
        Assert.assertEquals(ofertaEntity.getNombre(), response.getNombre());

    }

    /**
     * Prueba para obtener una colección de instancias de ofertas asociadas a
     * una instancia Contratista.
     *
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void getOfertaNoAsociadoTest() throws BusinessLogicException {
        ContratistaEntity entity = data.get(0);
        OfertaEntity ofertaEntity = ofertasData.get(1);
        contratistaOfertasLogic.getOferta(entity.getId(), ofertaEntity.getId());
    }

    
    /**
     * Prueba para obtener una colección de instancias de ofertas asociadas a
     * una instancia Contratista.
     *
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test
    public void replaceOfertasTest() throws BusinessLogicException {
        ContratistaEntity entity = data.get(0);
        List<OfertaEntity> list = ofertasData.subList(1, 3);
        contratistaOfertasLogic.replaceOfertas(entity.getId());

        entity = contratistaLogic.getContratista(entity.getId());
        
        Assert.assertTrue(entity.getOfertas().contains(ofertasData.get(1)));
        Assert.assertTrue(entity.getOfertas().contains(ofertasData.get(2)));
    }

}
