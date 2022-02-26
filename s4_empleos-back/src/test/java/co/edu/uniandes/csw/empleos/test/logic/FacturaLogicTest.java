/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.logic;

import co.edu.uniandes.csw.empleos.ejb.FacturaLogic;
import co.edu.uniandes.csw.empleos.entities.FacturaEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.FacturaPersistence;
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
 * @author Nicolas Munar
 */

@RunWith(Arquillian.class)
public class FacturaLogicTest {
    
    @Inject
    private UserTransaction utx;
    
    @Inject
    private FacturaLogic facturaLogic;
   
    @PersistenceContext
    private EntityManager em;
    
    private PodamFactory factory = new PodamFactoryImpl();
    
    private List<FacturaEntity> data = new ArrayList<FacturaEntity>();
    
    @Deployment
    private static JavaArchive createDeployment()
    {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(FacturaEntity.class.getPackage())
                .addPackage(FacturaLogic.class.getPackage())
                .addPackage(FacturaPersistence.class.getPackage())
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
        em.createQuery("delete from FacturaEntity").executeUpdate();
    }
    
    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {
        
        for (int i = 0; i < 3; i++) {
            FacturaEntity entity = factory.manufacturePojo(FacturaEntity.class);
            em.persist(entity);
            data.add(entity);
        }
        
    }
    
     /**
     * Se verifica que se haya creado correctamente una factura.
     * @throws BusinessLogicException Excepcion untilizada para representar errores en la lógica del negocio.
     */
    @Test

    public void createFactura()throws BusinessLogicException{
      FacturaEntity newEntity = factory.manufacturePojo(FacturaEntity.class);
      if(newEntity.getValor()<0)
      {
          newEntity.setValor(newEntity.getValor()*-1);
      }
      FacturaEntity result = facturaLogic.createFactura(newEntity);
      Assert.assertNotNull(result);
      
      FacturaEntity entity = em.find(FacturaEntity.class,result.getId());
      Assert.assertEquals(entity.getValor(),result.getValor());
      Assert.assertEquals(entity.getFecha(),result.getFecha());
    }
         
    /**
     * Se crea una factura con un valor menor a 0
     * @throws BusinessLogicException 
     */
    @Test(expected = BusinessLogicException.class)
    public void createFacturaValorNoMenoraCero()throws BusinessLogicException{
        
      FacturaEntity newEntity = factory.manufacturePojo(FacturaEntity.class);
      newEntity.setValor(-1);
      FacturaEntity result = facturaLogic.createFactura(newEntity);
    }
    
    /**
     * Se crea una factura con una fecha no nula
     * @throws BusinessLogicException Excepcion untilizada para representar errores en la lógica del negocio.
     */
    @Test(expected = BusinessLogicException.class)
    public void createFacturaFechaNotNull()throws BusinessLogicException{
        
      FacturaEntity newEntity = factory.manufacturePojo(FacturaEntity.class);
      newEntity.setFecha(null);
      FacturaEntity result = facturaLogic.createFactura(newEntity);
    }

    
    /**
     * Prueba para consultar la lista de Facturas.
     */
    @Test
    public void getFacturasTest() {
        List<FacturaEntity> list = facturaLogic.getFacturas();
        Assert.assertEquals(data.size(), list.size());
        for (FacturaEntity entity : list) {
            boolean found = false;
            for (FacturaEntity storedEntity : data) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }
    
     /**
     * Prueba para consultar una Factura.
     */
    @Test
    public void getFacturaTest() {
        FacturaEntity entity = data.get(0);
        FacturaEntity resultEntity = facturaLogic.getFactura(entity.getId());
        Assert.assertNotNull(resultEntity);
        Assert.assertEquals(entity.getId(), resultEntity.getId());
        Assert.assertEquals(entity.getFecha(), resultEntity.getFecha());
        Assert.assertEquals(entity.getValor(), resultEntity.getValor());
       
    }
    
    /**
     * Prueba para actualizar una Factura.
     *
     * @throws BusinessLogicException
     */
    @Test
    public void updateFactura() throws BusinessLogicException {
        FacturaEntity entity = data.get(0);
        FacturaEntity pojoEntity = factory.manufacturePojo(FacturaEntity.class);

    

        pojoEntity.setId(entity.getId());
        if(pojoEntity.getValor()<0){
            pojoEntity.setValor(pojoEntity.getValor()*-1);
        }
        facturaLogic.updateFactura(pojoEntity.getId(), pojoEntity);

        FacturaEntity resp = em.find(FacturaEntity.class, entity.getId());

        Assert.assertEquals(pojoEntity.getFecha(), resp.getFecha());

        Assert.assertEquals(pojoEntity.getTrabajo(), resp.getTrabajo());
        
        Assert.assertEquals(pojoEntity.getValor(), resp.getValor());

    }
    
    /**
     * Prueba para actualizar una Factura.
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void updateFacturaValorNoMenoraCero() throws BusinessLogicException {
        FacturaEntity entity = data.get(0);
        FacturaEntity pojoEntity = factory.manufacturePojo(FacturaEntity.class);
        pojoEntity.setValor(-1);
        pojoEntity.setId(entity.getId());
        facturaLogic.updateFactura(pojoEntity.getId(), pojoEntity);
    }
    
    /**
     * Prueba para actualizar una Factura.
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void updateFacturaFechaNotNull() throws BusinessLogicException {
        FacturaEntity entity = data.get(0);
        FacturaEntity pojoEntity = factory.manufacturePojo(FacturaEntity.class);
        pojoEntity.setFecha(null);
        pojoEntity.setId(entity.getId());
        facturaLogic.updateFactura(pojoEntity.getId(), pojoEntity);
    }
    
    /**
     * Prueba para eliminar una Factura.
     *
     * @throws BusinessLogicException
     */
    @Test
    public void deleteFacturaTest() throws BusinessLogicException {
        FacturaEntity entity = data.get(0);
        facturaLogic.deleteFactura(entity.getId());
        FacturaEntity deleted = em.find(FacturaEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }
    
}
