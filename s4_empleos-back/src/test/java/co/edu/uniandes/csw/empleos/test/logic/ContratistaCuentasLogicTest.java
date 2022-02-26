package co.edu.uniandes.csw.empleos.test.logic;

import co.edu.uniandes.csw.empleos.ejb.ContratistaCuentasLogic;
import co.edu.uniandes.csw.empleos.ejb.ContratistaLogic;
import co.edu.uniandes.csw.empleos.entities.ContratistaEntity;
import co.edu.uniandes.csw.empleos.entities.CuentaDeCobroEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.ContratistaPersistence;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class ContratistaCuentasLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private ContratistaLogic contratistaLogic;
    @Inject
    private ContratistaCuentasLogic contratistaCuentasLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private List<ContratistaEntity> data = new ArrayList<>();

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
     * Prueba para asociar una cuenta existente a un Contratista.
     */
    @Test
    public void addCuentaTest() {
        ContratistaEntity entity = data.get(0);
        CuentaDeCobroEntity cuentaEntity = cuentasData.get(1);
        CuentaDeCobroEntity response = contratistaCuentasLogic.addCuenta(cuentaEntity.getId(), entity.getId());

        Assert.assertNotNull(response);
        Assert.assertEquals(cuentaEntity.getId(), response.getId());
    }

    /**
     * Prueba para obtener una colección de instancias de Cuentas asociadas a
     * una instancia Contratista.
     */
    @Test
    public void getCuentasTest() {
        try {
            CuentaDeCobroEntity cuenta = factory.manufacturePojo(CuentaDeCobroEntity.class);
            utx.begin();
            em.persist(cuenta);
            utx.commit();
            List<CuentaDeCobroEntity> list = contratistaCuentasLogic.getCuentas(cuenta.getId());

            Assert.assertEquals(1, list.size());
        } catch (Exception ex) {
            Logger.getLogger(ContratistaCuentasLogicTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Prueba para obtener una instancia de Cuenta asociada a una instancia
     * Contratista.
     *
     * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
     */
    @Test
    public void getCuentaTest() throws BusinessLogicException {
        ContratistaEntity entity = factory.manufacturePojo(ContratistaEntity.class);
        CuentaDeCobroEntity cuentaEntity = factory.manufacturePojo(CuentaDeCobroEntity.class);
        cuentaEntity.setContratista(entity);
        entity.setCuentasDeCobro(cuentasData);
        try {
            utx.begin();
            em.persist(cuentaEntity);
            utx.commit();
        } catch (Exception ex) {
            Logger.getLogger(ContratistaCuentasLogicTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        entity.getCuentasDeCobro().add(cuentaEntity);
        try {
            utx.begin();
            em.persist(entity);
            utx.commit();
            CuentaDeCobroEntity response = contratistaCuentasLogic.getCuenta(entity.getId(), cuentaEntity.getId());

            Assert.assertEquals(cuentaEntity.getId(), response.getId());
            Assert.assertEquals(cuentaEntity.getConcepto(), response.getConcepto());
            Assert.assertEquals(cuentaEntity.getContratista(), response.getContratista());
            Assert.assertEquals(cuentaEntity.getFecha(), response.getFecha());
            Assert.assertEquals(cuentaEntity.getNombreEstudiante(), response.getNombreEstudiante());
            Assert.assertEquals(cuentaEntity.getNumeroCuentaDeCobro(), response.getNumeroCuentaDeCobro());
            Assert.assertEquals(cuentaEntity.getValor(), response.getValor());
        } catch (Exception ex) {
            Logger.getLogger(ContratistaCuentasLogicTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Prueba para obtener una instancia de CuentaDeCobro asociada a una
     * instancia Contratista que no le pertenece.
     *
     * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void getCuentaNoAsociadaTest() throws BusinessLogicException {
        ContratistaEntity entity = data.get(0);
        CuentaDeCobroEntity cuentaEntity = cuentasData.get(1);
        contratistaCuentasLogic.getCuenta(entity.getId(), cuentaEntity.getId());
    }

    /**
     * Prueba para remplazar las instancias de Cuentas asociadas a una instancia
     * de Contratista.
     */
    @Test
    public void replaceBooksTest() {
        try {
            ContratistaEntity entity = data.get(0);
            List<CuentaDeCobroEntity> list = cuentasData.subList(1, 3);
            contratistaCuentasLogic.replaceCuentas(entity.getId(), list);

            entity = contratistaLogic.getContratista(entity.getId());
            Assert.assertFalse(entity.getCuentasDeCobro().contains(cuentasData.get(0)));
        } catch (BusinessLogicException ex) {
            Logger.getLogger(ContratistaCuentasLogicTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
