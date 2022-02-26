/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.persistence;

import co.edu.uniandes.csw.empleos.entities.FacturaEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Estudiante
 */
@Stateless
public class FacturaPersistence {
    
    /**
     * Portero, quien admistra quien entra y quien sale del PersistenceContext
     */
    @PersistenceContext(unitName = "empleosPU")
    protected EntityManager em;
    
    /**
     * Busca si hay alguna factura que se envía por argumento
     * @param id id correspondiente a la factura buscada.
     * @return Una factura
     */
    public FacturaEntity find (Long id)
    {
        return em.find(FacturaEntity.class, id);
    }
    
    /**
     * Método para persistir la entidad en la base de datos
     * @param facturaEntity objeto que se creará en la base de datos
     * @return La entidad creada con un id dado por la base de datos.
     */
    public FacturaEntity create (FacturaEntity facturaEntity)
    {
        em.persist(facturaEntity);
        return facturaEntity;
    }
    
    /**
     * Devuelve todas las facturas de la base de datos
     * @return  Una lista con todas las facturas que se encuentren en la base de datos
     */
    public List<FacturaEntity> findAll()
    {
        TypedQuery<FacturaEntity> query  = em.createQuery("select u from FacturaEntity u", FacturaEntity.class);
        return query.getResultList();
    }
    
    /**
     * Actualiza una factura
     * @param facturaEntity La factura que viene con los nuevos cambios
     * @return Una factura con cambios aplicados
     */
    public FacturaEntity update(FacturaEntity facturaEntity)
    {
        return em.merge(facturaEntity);
    }
    
    /**
     * Borra una factura de la base de datos recibiendo un Id por parametero correspondiente al elemento  que se quiere eliminar 
     * @param facturaId id correspondiente al elemento que se quiere borrar.
     */
    public void delete(Long facturaId)
    {
        FacturaEntity entity = em.find(FacturaEntity.class, facturaId);
        em.remove(entity);
    }
    
}
