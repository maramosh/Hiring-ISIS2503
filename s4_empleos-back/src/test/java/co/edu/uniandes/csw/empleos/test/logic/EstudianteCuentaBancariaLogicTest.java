package co.edu.uniandes.csw.empleos.test.logic;

import co.edu.uniandes.csw.empleos.ejb.EstudianteCuentaBancariaLogic;
import co.edu.uniandes.csw.empleos.ejb.EstudianteLogic;
import co.edu.uniandes.csw.empleos.ejb.CuentaBancariaLogic;
import co.edu.uniandes.csw.empleos.entities.EstudianteEntity;
import co.edu.uniandes.csw.empleos.entities.CuentaBancariaEntity;
import co.edu.uniandes.csw.empleos.entities.OfertaEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.EstudiantePersistence;
import co.edu.uniandes.csw.empleos.persistence.CuentaBancariaPersistence;
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
public class EstudianteCuentaBancariaLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private EstudianteLogic estudianteLogic;
    @Inject
    private CuentaBancariaLogic cuentaBancariaLogic;

    @Inject
    private EstudianteCuentaBancariaLogic estudianteCuentaBancariaLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private List<EstudianteEntity> caldata = new ArrayList<EstudianteEntity>();
    private List<CuentaBancariaEntity> data = new ArrayList<CuentaBancariaEntity>();

    /**
     * @return Devuelve el jar que Arquillian va a desplegar en Payara embebido.
     * El jar contiene las clases, el descriptor de la base de datos y el
     * archivo beans.xml para resolver la inyección de dependencias.
     */
    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(EstudianteEntity.class.getPackage())
                .addPackage(EstudianteLogic.class.getPackage())
                .addPackage(EstudiantePersistence.class.getPackage())
                .addPackage(CuentaBancariaEntity.class.getPackage())
                .addPackage(CuentaBancariaPersistence.class.getPackage())
                .addPackage(CuentaBancariaLogic.class.getPackage())
                .addPackage(EstudianteCuentaBancariaLogic.class.getPackage())
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
        em.createQuery("delete from EstudianteEntity").executeUpdate();
        em.createQuery("delete from CuentaBancariaEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            EstudianteEntity est = factory.manufacturePojo(EstudianteEntity.class);
            em.persist(est);
            caldata.add(est);
        }

        for (int i = 0; i < 3; i++) {
            CuentaBancariaEntity entity = factory.manufacturePojo(CuentaBancariaEntity.class);
            em.persist(entity);
            data.add(entity);
            if (i == 0) {
                caldata.get(0).setCuentaBancaria(data.get(0));
            }
        }

    }

    /**
     * Prueba para remplazar las instancias de Books asociadas a una instancia
     * de Editorial.
     */
    @Test
    public void replaceCuentaBancariaTest() throws BusinessLogicException {
        try {
            utx.begin();
            em.joinTransaction();
            EstudianteEntity entity = caldata.get(0);
            CuentaBancariaEntity c = data.get(0);
            estudianteCuentaBancariaLogic.replaceCuentaBancaria(entity.getId(), data.get(0).getId());
            entity = estudianteLogic.getEstudiante(entity.getId());
            Assert.assertEquals(entity.getCuentaBancaria(), c);
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
     * Prueba para desasociar una cuentaBancaria existente de un Estudiante existente
     *
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    @Test
    public void removeCuentaBancariaTest() throws BusinessLogicException {
        estudianteCuentaBancariaLogic.removeCuentaBancaria(caldata.get(0).getId());
        EstudianteEntity response = estudianteLogic.getEstudiante(caldata.get(0).getId());
        Assert.assertNull(response.getCuentaBancaria());
    }
    
    
    
    /**
     * Prueba para asociar una cuenta bancaria a un estudiante
     *
     *
     * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
     */
    @Test
    public void addCuentaBancariaTest() throws BusinessLogicException {
        EstudianteEntity estudiante = caldata.get(0);
        estudiante.setCuentaBancaria(null);
        estudiante.setCorreo("akjwd@uniandes.edu.co");
        estudiante.setSemestre(Math.min(Math.abs(estudiante.getSemestre()) + 1, 12));
        estudiante.setNombre(estudiante.getNombre() + "a");
        estudiante.setCarrera(estudiante.getCarrera() + "a");
        estudianteLogic.updateEstudiante(estudiante);
        CuentaBancariaEntity cuenta = factory.manufacturePojo(CuentaBancariaEntity.class);
        cuenta.setTipoCuenta("Ahorros");
        cuenta.setEstudiante(estudiante);
        cuentaBancariaLogic.createCuentaBancaria(cuenta);
        CuentaBancariaEntity cuentaEntity = estudianteCuentaBancariaLogic.addCuentaBancaria(estudiante.getId(), cuenta.getId());
        Assert.assertNotNull(cuentaEntity);

        Assert.assertEquals(cuentaEntity.getId(), cuenta.getId());
        Assert.assertEquals(cuentaEntity.getNumeroCuenta(), cuenta.getNumeroCuenta());
        Assert.assertEquals(cuentaEntity.getNombreBanco(), cuenta.getNombreBanco());
    }

}
