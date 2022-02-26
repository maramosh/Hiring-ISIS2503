/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.logic;

import co.edu.uniandes.csw.empleos.ejb.CuentaDeCobroContratistaLogic;
import co.edu.uniandes.csw.empleos.ejb.CuentaDeCobroLogic;
import co.edu.uniandes.csw.empleos.entities.ContratistaEntity;
import co.edu.uniandes.csw.empleos.entities.CuentaDeCobroEntity;
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
 * @author Santiago Tangarife Rincón
 */
@RunWith(Arquillian.class)
public class CuentaDeCobroContratistaLogicTest {
    
    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private CuentaDeCobroLogic cuentaLogic;
    @Inject
    private CuentaDeCobroContratistaLogic cuentaContratistaLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private List<ContratistaEntity> data = new ArrayList<ContratistaEntity>();

    private List<CuentaDeCobroEntity> cuentasData = new ArrayList();

    /**
     * @return Devuelve el jar que Arquillian va a desplegar en Payara embebido.
     * El jar contiene las clases, el descriptor de la base de datos y el
     * archivo beans.xml para resolver la inyección de dependencias.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(ContratistaEntity.class.getPackage())
                .addPackage(CuentaDeCobroLogic.class.getPackage())
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
        em.createQuery("delete from CuentaDeCobroEntity").executeUpdate();
        em.createQuery("delete from ContratistaEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            CuentaDeCobroEntity cuentas = factory.manufacturePojo(CuentaDeCobroEntity.class);
            em.persist(cuentas);
            cuentasData.add(cuentas);
        }
        for (int i = 0; i < 3; i++) {
            ContratistaEntity entity = factory.manufacturePojo(ContratistaEntity.class);
            em.persist(entity);
            data.add(entity);
            if (i == 0) {
                cuentasData.get(i).setContratista(entity);
            }
        }
    }
    
   /**
     * Prueba para remplazar las instancias de Cuentas asociadas a una instancia
     * de Contratista.
     */
    @Test
    public void replaceContratistaTest() {
        CuentaDeCobroEntity entity = cuentasData.get(0);
        cuentaContratistaLogic.replaceContratista(entity.getId(), data.get(1).getId());
        entity = cuentaLogic.getCuenta(entity.getId());
        Assert.assertEquals(entity.getContratista(), data.get(1));
    }

    /**
     * Prueba para desasociar una CuentaDeCobro existente de un Contratista existente
     *
     * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
     */
    @Test
    public void removeCuentasTest() throws BusinessLogicException {
        cuentaContratistaLogic.removeContratista(cuentasData.get(0).getId());
        CuentaDeCobroEntity deleted = em.find(CuentaDeCobroEntity.class, cuentasData.get(0).getId());
        
        Assert.assertNull(deleted);

    }
}
