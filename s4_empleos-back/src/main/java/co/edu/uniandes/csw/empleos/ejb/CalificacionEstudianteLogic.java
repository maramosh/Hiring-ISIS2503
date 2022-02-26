/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.ejb;

import co.edu.uniandes.csw.empleos.entities.CalificacionEntity;
import co.edu.uniandes.csw.empleos.entities.EstudianteEntity;
import co.edu.uniandes.csw.empleos.persistence.CalificacionPersistence;
import co.edu.uniandes.csw.empleos.persistence.EstudiantePersistence;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Nicolas Munar
 */
@Stateless
public class CalificacionEstudianteLogic {

    @Inject
    private CalificacionPersistence calificacionPersistence;

    @Inject
    private EstudiantePersistence estudiantePersistence;
    
     /**
     * Remplazar el estudiante de una calificacion.
     *
     * @param calId .
     * @param estId .
     * @return .
     */
    public CalificacionEntity replaceEstudiante(Long calId, Long estId) {
        EstudianteEntity estudianteEntity = estudiantePersistence.find(estId);
        CalificacionEntity calificacionEntity = calificacionPersistence.find(calId);
        calificacionEntity.setEstudiante(estudianteEntity);
        return calificacionEntity;
    }
    
    
    
    /**
     * Borrar un calificacion de un Estudiante Este metodo se utiliza para borrar la
     * relacion de un estudiante.
     *
     * @param calId El libro que se desea borrar de la editorial.
     */
    public void removeEstudiante(Long calId) {
        CalificacionEntity calificacionEntity = calificacionPersistence.find(calId);
        EstudianteEntity estudianteEntity = estudiantePersistence.find(calificacionEntity.getEstudiante().getId());
        calificacionEntity.setEstudiante(null);
        estudianteEntity.getCalificaciones().remove(calificacionEntity);
    }
    
    

}
