/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.persistence;

import co.edu.uniandes.csw.empleos.entities.CuentaDeCobroEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Santiago Tangarife
 */
@Stateless
public class CuentaDeCobroPersistence {

    @PersistenceContext(unitName = "empleosPU")
    protected EntityManager em;

    /**
     * Se crea una neva instancia del objeto en la base de datos
     *
     * @param cuentaDeCobroEntity objeto a ser persistido
     * @return el objeto persitido con su id generado por la base de datos
     */
    public CuentaDeCobroEntity create(CuentaDeCobroEntity cuentaDeCobroEntity) {
        em.persist(cuentaDeCobroEntity);
        return cuentaDeCobroEntity;
    }

    /**
     * Devuelve la cuenta de cobro que se buscó por su id
     *
     * @param cuentaDeCobroId id de la cuenta de cobro
     * @return objeto de la entidad correspondiente si la encuentra null si no.
     */
    public CuentaDeCobroEntity find(Long cuentaDeCobroId) {
        return em.find(CuentaDeCobroEntity.class, cuentaDeCobroId);
    }

    /**
     * retona una contenedora con todas las Cuentas de cobro
     *
     * @return retorna la colección de todos los objetos que existen en la base
     * de datos de la entidad correspondiente.
     */
    public List<CuentaDeCobroEntity> findAll() {
        TypedQuery<CuentaDeCobroEntity> query = em.createQuery("select u from CuentaDeCobroEntity u", CuentaDeCobroEntity.class);
        return query.getResultList();
    }

    /**
     * Se ingresa una cuentaDeCobro con la información actualizada y se devuelve
     * la cuenta con la información en la base de datos actualizada.
     * @param cuentaDeCobro el objeto de la entidad que se quiere actualizar.1
     * @return el objeto actualizado
     */
    public CuentaDeCobroEntity update(CuentaDeCobroEntity cuentaDeCobro) {
        return em.merge(cuentaDeCobro);
    }

    /**
     * Se borra un objeto de la base de datos
     *
     * @param cuentaDeCobroId id de la cuenta de cobro que se quiere eliminar
     */
    public void delete(Long cuentaDeCobroId) {
        CuentaDeCobroEntity aBorrar = em.find(CuentaDeCobroEntity.class, cuentaDeCobroId);

        em.remove(aBorrar);
    }
}
