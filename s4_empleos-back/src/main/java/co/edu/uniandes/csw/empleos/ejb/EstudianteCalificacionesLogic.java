/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package co.edu.uniandes.csw.empleos.ejb;

import co.edu.uniandes.csw.empleos.entities.CalificacionEntity;
import co.edu.uniandes.csw.empleos.entities.EstudianteEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.CalificacionPersistence;
import co.edu.uniandes.csw.empleos.persistence.EstudiantePersistence;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Nicolas Munar
 */
@Stateless
public class EstudianteCalificacionesLogic {
    
    
    @Inject
    private CalificacionPersistence calificacionPersistence;
    
    @Inject
    private EstudiantePersistence estudiantePersistence;
    
    
    /**
     * Asocia un Estudiante existente a una Calificacion
     *
     * @param calId Identificador de la instancia de Calificacion
     * @param estId Identificador de la instancia de Estudiante
     * @return Instancia de EstudianteEntity que fue asociada a Calificacion
     */
    public CalificacionEntity addCalificacion(Long calId, Long estId) {
        
        EstudianteEntity estudianteEntity = estudiantePersistence.find(estId);
        CalificacionEntity calificacionEntity = calificacionPersistence.find(calId);
        calificacionEntity.setEstudiante(estudianteEntity);
        return calificacionEntity;
    }
    
    
    /**
     * Obtiene una colección de instancias de CalificacionEntity asociadas a una
     * instancia de Estudiante
     *
     * @param estId Identificador de la instancia de Estudiante
     * @return Colección de instancias de CalificacionEntity asociadas a la instancia
     * de Estudiante
     */
    public List<CalificacionEntity> getCalificaciones(Long estId) {
        return estudiantePersistence.find(estId).getCalificaciones();
    }
    
    /**
     * Obtiene una instancia de EstudianteEntity asociada a una instancia de calificacion
     *
     * @param estId Identificador de la instancia de Estudiante
     * @param calId Identificador de la instancia de Calificacion
     * @return La entidad del Calificacion asociada al Estudiante
     */
    public CalificacionEntity getCalificacion(Long estId, Long calId)throws BusinessLogicException {
        List<CalificacionEntity> cals = estudiantePersistence.find(estId).getCalificaciones();
        CalificacionEntity calificacionEntity = calificacionPersistence.find(calId);
        int index = cals.indexOf(calificacionEntity);
        if (index >= 0) {
            return cals.get(index);
        }
        throw new BusinessLogicException("La calificacion no está asociada al estudiante");
    }
    
    
    /**
     * Remplaza las instancias de Calificacion asociadas a una instancia de Estudiante
     *
     * @param estId Identificador de la instancia de Estudiante
     * @param list Colección de instancias de CalificacionEntity a asociar a instancia
     * de Estudiante
     * @return Nueva colección de CalificacionEntity asociada a la instancia de Estudiante
     */
    public List<CalificacionEntity> replaceCalificaciones(Long estId, List<CalificacionEntity> list) {
        EstudianteEntity estudianteEntity = estudiantePersistence.find(estId);
        List<CalificacionEntity> calificacionList = calificacionPersistence.findAll();
        for (CalificacionEntity book : calificacionList) {
            if (calificacionList.contains(book)) {
                book.setEstudiante(estudianteEntity);
            } else if (book.getEstudiante()!= null && book.getEstudiante().equals(estudianteEntity)) {
                book.setEstudiante(null);
            }
        }
        return calificacionList;
        
    }
    
    /**
     * Desasocia una calificacion existente de un Estudiante existente
     *
     * @param estudiantesId Identificador de la instancia de Estudiante
     * @param calificacionId Identificador de la instancia de calificacion
     */
    public void removeCalificacion(Long estudiantesId, Long calificacionId) {
        EstudianteEntity estudianteEntity = estudiantePersistence.find(estudiantesId);
        CalificacionEntity calificacionEntity = calificacionPersistence.find(calificacionId);
        estudianteEntity.getCalificaciones().remove(calificacionEntity);
        calificacionEntity.setEstudiante(null);
    }
    
    /**
     * Desasocia todas las calificaciones existentes de un Estudiante existente
     *
     * @param estudiantesId Identificador de la instancia de Estudiante
     */
    public void removeCalificaciones(Long estudiantesId) {
        EstudianteEntity estudianteEntity = estudiantePersistence.find(estudiantesId);
        if(estudianteEntity != null)
            for(CalificacionEntity calificacionEntity : estudianteEntity.getCalificaciones()) {
                estudianteEntity.getCalificaciones().remove(calificacionEntity);
                calificacionEntity.setEstudiante(null);
            }
    }
}
