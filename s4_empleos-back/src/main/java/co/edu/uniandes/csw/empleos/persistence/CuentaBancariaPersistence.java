
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.persistence;

import co.edu.uniandes.csw.empleos.entities.CuentaBancariaEntity;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author je.hernandezr
 */
@Stateless
public class CuentaBancariaPersistence {

    @PersistenceContext(unitName = "empleosPU")
    protected EntityManager em;
   

     /**
     * Busca si hay alguna cuentaBancaria con el id que se envía de argumento
        * @param cuentaId
     * @return un cuenta bancaria.
     */
    public CuentaBancariaEntity find( Long cuentaId) {
      
        return em.find(CuentaBancariaEntity.class, cuentaId);
        
    }
  
    /**
     * Busca si hay alguna cuentaBancaria con el numero de cuenta que se envía de argumento
     * @param numeroCuenta
     * @return una cuenta bancaria.
     */
    public CuentaBancariaEntity findByNumero( String numeroCuenta) {
      
    
   
        TypedQuery query = em.createQuery("Select e From CuentaBancariaEntity e where e.numeroCuenta = :numeroCuenta", CuentaBancariaEntity.class);
        // Se remplaza el placeholder ":numeroCuenta" con el valor del argumento 
        query = query.setParameter("numeroCuenta", numeroCuenta);
        // Se invoca el query se obtiene la lista resultado
        List<CuentaBancariaEntity> sameNumeroCuenta = query.getResultList();
        CuentaBancariaEntity result;
        if (sameNumeroCuenta  == null) {
            result = null;
        } else if (sameNumeroCuenta .isEmpty()) {
            result = null;
        } else {
            result = sameNumeroCuenta .get(0);
        }
    
        return result;
        
    }
    
     /**
     * Devuelve todas las cuentas Bancarias de la base de datos.
     *
     * @return una lista con todos las cuentas bancarias Entities que encuentre en la base de
     * datos, "select u from CuentaBancariaEntity u" es como un "select * from
     * CuentaBancariaEntity;" - "SELECT * FROM table_name" en SQL.
     */
    public List<CuentaBancariaEntity> findAll() {
        
        
        Query q = em.createQuery("select u from CuentaBancariaEntity u");
        return q.getResultList();
    }

    /**
     * Método para persisitir la entidad en la base de datos.
     *
     * @param cuentaBancariaEntity
     * @return devuelve la entidad creada con un id dado por la base de datos.
     */
    public CuentaBancariaEntity create(CuentaBancariaEntity cuentaBancariaEntity) {
      
        em.persist(cuentaBancariaEntity);
        
        return cuentaBancariaEntity;
    }

    /**
     * Actualiza una cuenta Bancaria.
     *
     * @param cuentaBancariaEntity: la cuentaBancaria que viene con los nuevos cambios. Por
     * ejemplo el nombre pudo cambiar. En ese caso, se haria uso del método
     * update.
     * @return una CuentaBancaria con los cambios aplicados.
     */
    public CuentaBancariaEntity update(CuentaBancariaEntity cuentaBancariaEntity) {

        return em.merge(cuentaBancariaEntity);
    }

    /**
     *
     * Borra  una cuenta bancaria de la base de datos recibiendo como argumento el id del
     * premio
     *
     * @param id: id correspondiente a la cuentaBancaria a borrar.
     */
    public void delete(Long id) {
        CuentaBancariaEntity entity = em.find(CuentaBancariaEntity.class, id);
        em.remove(entity);
    }

    
}
