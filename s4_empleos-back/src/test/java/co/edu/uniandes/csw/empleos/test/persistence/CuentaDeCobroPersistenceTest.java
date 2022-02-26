/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.persistence;

import co.edu.uniandes.csw.empleos.entities.CuentaDeCobroEntity;
import co.edu.uniandes.csw.empleos.persistence.CuentaDeCobroPersistence;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 *
 * @author Santiago Tangarife
 */
@RunWith(Arquillian.class)
public class CuentaDeCobroPersistenceTest {

    @Inject
    private CuentaDeCobroPersistence persistance;

    @PersistenceContext
    private EntityManager em;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class).addPackage(CuentaDeCobroEntity.class.getPackage())
                .addPackage(CuentaDeCobroPersistence.class.getPackage())
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }

    /**
     * Prueba el metodo create con una cuenta de cobro aleatoria
     */
    @Test
    public void createCuentaDeCobroEntity() {
        PodamFactory factory = new PodamFactoryImpl();
        CuentaDeCobroEntity cuenta = factory.manufacturePojo(CuentaDeCobroEntity.class);

        CuentaDeCobroEntity cu = persistance.create(cuenta);

        Assert.assertNotNull(cu);

        CuentaDeCobroEntity entity = em.find(CuentaDeCobroEntity.class, cu.getId());

        Assert.assertEquals(entity.getNumeroCuentaDeCobro(), cu.getNumeroCuentaDeCobro());
        Assert.assertEquals(entity.getContratista(), cu.getContratista());
        Assert.assertEquals(entity.getFecha(), cu.getFecha());
        Assert.assertEquals(entity.getConcepto(), cu.getConcepto());
        Assert.assertEquals(entity.getNombreEstudiante(), cu.getNombreEstudiante());
        Assert.assertEquals(entity.getValor(), cu.getValor());
    }

    /**
     * prueba el metodo find de Cuenta de cobro
     */
    @Test
    public void findCuentaDeCobroEntity() {
        PodamFactory factory = new PodamFactoryImpl();
        CuentaDeCobroEntity cuenta = factory.manufacturePojo(CuentaDeCobroEntity.class);

        //Crea una cuenta de cobro aleatoria
        CuentaDeCobroEntity cu = persistance.create(cuenta);
        Assert.assertNotNull(cu);

        Long id = cu.getId();
        //crea una cuenta nula, suponiendo que el id +1 no existe
        CuentaDeCobroEntity nula = persistance.find(id + 1);
        Assert.assertNull(nula);
        //busca una a cuenta por su id
        CuentaDeCobroEntity esta = persistance.find(id);
        Assert.assertNotNull(esta);

        Assert.assertEquals(cu.getContratista(), esta.getContratista());
        Assert.assertEquals(cu.getNumeroCuentaDeCobro(), esta.getNumeroCuentaDeCobro());
        Assert.assertEquals(cu.getFecha(), esta.getFecha());
        Assert.assertEquals(esta.getConcepto(), cu.getConcepto());
        Assert.assertEquals(esta.getNombreEstudiante(), cu.getNombreEstudiante());
        Assert.assertEquals(esta.getValor(), cu.getValor());
    }

    /**
     * prueba el metodo findAll de Cuenta de cobro
     */
    @Test
    public void findAllCuentaDeCobroEntity() {
        PodamFactory factory = new PodamFactoryImpl();
        CuentaDeCobroEntity cuenta = factory.manufacturePojo(CuentaDeCobroEntity.class);

        //Crea una cuenta de cobro aleatoria
        CuentaDeCobroEntity c1 = persistance.create(cuenta);
        Assert.assertNotNull(c1);

        CuentaDeCobroEntity c2 = persistance.create(cuenta);
        Assert.assertNotNull(c2);

        CuentaDeCobroEntity c3 = persistance.create(cuenta);
        Assert.assertNotNull(c3);

        //busca una a cuenta por su id
        List<CuentaDeCobroEntity> cuentas = persistance.findAll();
        for (int i = 0; i < cuentas.size(); i++) {
            CuentaDeCobroEntity esta = cuentas.get(i);

            if (esta.getId().equals(c1.getId())) {
                Assert.assertEquals(esta.getContratista(), c1.getContratista());
                Assert.assertEquals(esta.getNumeroCuentaDeCobro(), c1.getNumeroCuentaDeCobro());
                Assert.assertEquals(esta.getFecha(), c1.getFecha());
                Assert.assertEquals(esta.getConcepto(), c1.getConcepto());
                Assert.assertEquals(esta.getNombreEstudiante(), c1.getNombreEstudiante());
                Assert.assertEquals(esta.getValor(), c1.getValor());
            }
            else if (esta.getId().equals(c2.getId())) {
                Assert.assertEquals(esta.getContratista(), c2.getContratista());
                Assert.assertEquals(esta.getNumeroCuentaDeCobro(), c2.getNumeroCuentaDeCobro());
                Assert.assertEquals(esta.getFecha(), c2.getFecha());
                Assert.assertEquals(esta.getConcepto(), c2.getConcepto());
                Assert.assertEquals(esta.getNombreEstudiante(), c2.getNombreEstudiante());
                Assert.assertEquals(esta.getValor(), c2.getValor());
            }
            else if (esta.getId().equals(c3.getId())) {
                Assert.assertEquals(esta.getContratista(), c3.getContratista());
                Assert.assertEquals(esta.getNumeroCuentaDeCobro(), c3.getNumeroCuentaDeCobro());
                Assert.assertEquals(esta.getFecha(), c3.getFecha());
                Assert.assertEquals(esta.getConcepto(), c3.getConcepto());
                Assert.assertEquals(esta.getNombreEstudiante(), c3.getNombreEstudiante());
                Assert.assertEquals(esta.getValor(), c3.getValor());
            }
        }
    }
    
    /**
     * Prueba el metodo update de cuenta de cobro
     */
    @Test
    public void updateCuentaDeCobroEntity()
    {
        PodamFactory factory = new PodamFactoryImpl();
        CuentaDeCobroEntity cuenta = factory.manufacturePojo(CuentaDeCobroEntity.class);
        
        CuentaDeCobroEntity cu= persistance.create(cuenta);
        Assert.assertNotNull(cu);
        
       //se hace una copia y se la modifica
        CuentaDeCobroEntity cn= cu;

        cn.setConcepto("Trabajo nuevo");
        cn.setNombreEstudiante("Losarig");
        cn.setValor(875643);
        cn.setFecha(new Date());
        cn.setNumeroCuentaDeCobro(51920);
        //Se la actualiza
        CuentaDeCobroEntity cn2= persistance.update(cn);
        Assert.assertNotNull(cn2);
        
        Assert.assertEquals(cn.getContratista(), cn2.getContratista());
        Assert.assertEquals(cn.getFecha(), cn2.getFecha());
        Assert.assertEquals(cn.getNumeroCuentaDeCobro(), cn2.getNumeroCuentaDeCobro());
        Assert.assertEquals(cn.getConcepto(), cn2.getConcepto());
        Assert.assertEquals(cn.getNombreEstudiante(), cn2.getNombreEstudiante());
        Assert.assertEquals(cn.getValor(), cn2.getValor());
    }
    
    
}
