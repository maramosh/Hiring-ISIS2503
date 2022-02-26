/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.persistence;

import co.edu.uniandes.csw.empleos.entities.CalificacionEntity;
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
public class CalificacionPersistence {
     
    /**
     * Portero, quien admistra quien entra y quien sale del PersistenceContext
     */
    @PersistenceContext(unitName= "empleosPU")
    protected EntityManager em;
    
    /**
     * Busca si hay alguna calificacion con el id que de envía por argumento
     * @param id idCorrespondiente a la calificacion buscada.
     * @return Una calificacion
     */
    public CalificacionEntity find (Long id)
    {
      return em.find(CalificacionEntity.class, id);
    }
    
    /**
     * Método para persistir la entidad en la base de datos
     * @param calificacionEntity objeto que se creará en la base de datos
     * @return La entidad creada con un id dado por la base de datos.
     */
    public CalificacionEntity create(CalificacionEntity calificacionEntity)
    {
        em.persist(calificacionEntity);
        return calificacionEntity;
    }
    
    /**
     * Devuelve todas las calificaciones de la base de datos
     * @return  Una lista con todas las calificaciones que se encuentren en la base de datos
     */
    public List<CalificacionEntity> findAll()
    {
        TypedQuery<CalificacionEntity> query  = em.createQuery("select u from CalificacionEntity u", CalificacionEntity.class);
        return query.getResultList();
    }
    
    /**
     * Actualiza una calificacion
     * @param calificacionEntity La calificacion que viene con los nuevos cambios
     * @return Una calificacion con los cambios aplicados
     */
    public CalificacionEntity update(CalificacionEntity calificacionEntity)
    {
        return em.merge(calificacionEntity);
    }
    
    /**
     * Borra una calificacion de la base de datos recibiendo un Id por parametero correspondiente al elemento  que se quiere eliminar 
     * @param calificacionId id correspondiente al elemento que se quiere borrar.
     */
    public void delete(Long calificacionId)
    {
        CalificacionEntity entity = em.find(CalificacionEntity.class, calificacionId);
        em.remove(entity);
    }
    
  
}
