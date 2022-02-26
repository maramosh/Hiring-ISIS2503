/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.persistence;

import co.edu.uniandes.csw.empleos.entities.CredencialesEntity;
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
public class CredencialesPersistence {
    
    @PersistenceContext(unitName = "empleosPU")
    protected EntityManager em;
    
    /**
     * Busca si hay alguna Credencial con el id que se envía de argumento
        * @param credencialId
     * @return un cuenta credencial.
     */
    public CredencialesEntity find( Long credencialId) {
      
        return em.find(CredencialesEntity.class, credencialId);
        
    }
  
/**
     * Busca si hay alguna Credencial con el id que se envía de argumento
        * @param correo
     * @return un cuenta credencial.
     */
    public CredencialesEntity findByCorreo( String correo) {
      
    
   
        TypedQuery query = em.createQuery("Select e From CredencialesEntity e where e.correo = :correo", CredencialesEntity.class);
        // Se remplaza el placeholder ":numeroCuenta" con el valor del argumento 
        query = query.setParameter("correo", correo);
        // Se invoca el query se obtiene la lista resultado
        List<CredencialesEntity> sameCorreo = query.getResultList();
        CredencialesEntity result;
        if (sameCorreo == null) {
            result = null;
        } else if (sameCorreo.isEmpty()) {
            result = null;
        } else {
            result = sameCorreo .get(0);
        }
    
        return result;
        
    }
    
     /**
     * Devuelve todas las credenciales de la base de datos.
     *
     * @return una lista con todos las credenciales Entities que encuentre en la base de
     * datos, "select u from CredencialesEntity u" es como un "select * from
     * CredencialesEntity;" - "SELECT * FROM table_name" en SQL.
     */
    public List<CredencialesEntity> findAll() {
        
        
        Query q = em.createQuery("select u from CredencialesEntity u");
        return q.getResultList();
    }

    /**
     * Método para persisitir la entidad en la base de datos.
     *
   
     * @return devuelve la entidad creada con un id dado por la base de datos.
     */
    public CredencialesEntity create(CredencialesEntity credencialEntity) {
      
        em.persist(credencialEntity);
        
        return credencialEntity;
    }

    /**
     * Actualiza una credencial
     *
     * @param credencialesEntity
     * @return una CuentaBancaria con los cambios aplicados.
     */
    public CredencialesEntity update(CredencialesEntity credencialesEntity) {

        return em.merge(credencialesEntity);
    }

    /**
     *
     * Borra  una credencial de la base de datos recibiendo como argumento el id del
     * premio
     *
     * @param id: id correspondiente a la cuentaBancaria a borrar.
     */
    public void delete(Long id) {
        CredencialesEntity entity = em.find(CredencialesEntity.class, id);
        em.remove(entity);
    }
    
}
