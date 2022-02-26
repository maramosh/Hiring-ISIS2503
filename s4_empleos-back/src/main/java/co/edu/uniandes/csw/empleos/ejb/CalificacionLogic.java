/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.ejb;

import co.edu.uniandes.csw.empleos.entities.CalificacionEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.CalificacionPersistence;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Nicolás Munar
 */
@Stateless
public class CalificacionLogic {
    
     @Inject
    private CalificacionPersistence persistence;
    
    /**
     * Guarda una calificacion
     * @param calificacion la entidad de tipo Calificacion a persistir
     * @return entidad luego de persistirla
     * @throws BusinessLogicException Si alguna regla de negocio se rompe.
     */
    public CalificacionEntity createCalificacion(CalificacionEntity calificacion) throws BusinessLogicException{
        if(calificacion.getNota()<0){
            throw new BusinessLogicException("La nota del estudiante no puede tener valores negativos");
        }
        if(calificacion.getComentario()==null){
            throw new BusinessLogicException("El comentario de un estudainte no puede ser nulo");
        }
        if(calificacion.getNota()>5){
            throw new BusinessLogicException("La nota es en una escala de 1-5");
        }
        if(calificacion.getComentario().equals("")){
            throw new BusinessLogicException("El comentario no puede ser vacìo");
        }
        
        calificacion = persistence.create(calificacion);
        return calificacion;
    }
    
    /**
     * Devuelve todas las calificaciones que hay en la base de datos.
     * @return  Lista de las entidades del tipo calificacion.
     */
    public List<CalificacionEntity> getCalificaciones()
    {      
       return persistence.findAll();
    }
    
    /**
     * Busca Una calificacion por ID
     * @param calificacionId El id de la calificacion a buscar.
     * @return La calificacion encontrada, null si no se encuentra.
     */
    public CalificacionEntity getCalificacion(Long calificacionId)
    {
        
        
        return  persistence.find(calificacionId);
    }
    
     /**
     * Actualizar una calificacion por ID
     *
     * @param calificacionId El ID de la calificacion a actualizar
     * @param calificacionEntity La entidad de la Calificacion con los cambios deseados
     * @return La entidad de la calificacion luego de actualizarla
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    public CalificacionEntity updateCalificacion(Long calificacionId, CalificacionEntity calificacionEntity)throws BusinessLogicException {
        
        
       if(calificacionEntity.getNota()<0){
            throw new BusinessLogicException("La nota del estudiante no puede tener valores negativos");
        }
       if(calificacionEntity.getComentario()==null){
            throw new BusinessLogicException("El comentario de un estudainte no puede ser nulo");
        }
       if(calificacionEntity.getNota()>5){
            throw new BusinessLogicException("La nota es en una escala de 1-5");
        }
       if(calificacionEntity.getComentario().equals("")){
            throw new BusinessLogicException("El comentario no puede ser vacìo");
        }
        
        return persistence.update(calificacionEntity);
    }
    
    /**
     * Eliminar una calificacion por Id
     * @param calificacionId El ID de la calificacion a borrar.
     */
    public void deleteCalificacion(Long calificacionId)
    {
        persistence.delete(calificacionId);
    }
    
}
