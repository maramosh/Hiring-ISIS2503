/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.ejb;

import co.edu.uniandes.csw.empleos.entities.EstudianteEntity;
import co.edu.uniandes.csw.empleos.entities.OfertaEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.EstudiantePersistence;
import co.edu.uniandes.csw.empleos.persistence.OfertaPersistence;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author je.hernandezr
 */
@Stateless
public class OfertaEstudianteLogic {

    @Inject
    private OfertaPersistence ofertaPersistence;

    @Inject
    private EstudiantePersistence estudiantePersistence;

    /**
     * Asocia un Estudiante existente a una Oferta
     *
     * @param ofertasId
     * @param estudianteId
     * @return Instancia de EstudianteEntity que fue asociada a Book
     */
    public EstudianteEntity addEstudiante(Long ofertasId, Long estudianteId) {

        OfertaEntity ofertaEntity = ofertaPersistence.find(ofertasId);
        EstudianteEntity estudianteEntity = estudiantePersistence.find(estudianteId);
        estudianteEntity.getOfertas().add(ofertaEntity);
       
        return estudianteEntity;
    }

    /**
     * Obtiene una colección de instancias de EstudianteEntity asociadas a una
     * instancia de Oferta
     *
     * @param ofertasId
     * @return Colección de instancias de EstudianteEntity asociadas a la
     * instancia de Oferta
     */
    public List<EstudianteEntity> getEstudiantes(Long ofertasId) {

        return ofertaPersistence.find(ofertasId).getEstudiantes();
    }

    /**
     * Obtiene una instancia de AuthorEntity asociada a una instancia de Book
     *
     * @param ofertasId
     * @param estudiantesId
     * @return La entidad del Autor asociada al libro
     */
    public EstudianteEntity getEstudiante(Long ofertasId, Long estudiantesId) throws BusinessLogicException {
         
        List<EstudianteEntity>estudiantes = ofertaPersistence.find(ofertasId).getEstudiantes();
        EstudianteEntity estudianteEntity = estudiantePersistence.find(estudiantesId);
        int index = estudiantes.indexOf(estudianteEntity);
       
        if (index >= 0) {
            return estudiantes.get(index);
        }
        throw new BusinessLogicException("El estudiante no está asociado a la oferta");
    }

    /**
     * Remplaza las instancias de Estudiante asociadas a una instancia de Oferta
     * de Book
     *
     * @param ofertasId
     * @param list
     * @return Nueva colección de AuthorEntity asociada a la instancia de Book
     */
    public List<EstudianteEntity> replaceEstudiantes(Long ofertasId, List<EstudianteEntity> list) {

        OfertaEntity ofertaEntity = ofertaPersistence.find(ofertasId);
        ofertaEntity.setEstudiantes(list);
        return ofertaPersistence.find(ofertasId).getEstudiantes();
    }

    /**
     * Desasocia un Estudiante existente de una Oferta existente
     *
     * @param ofertasId
     * @param estudiantesId
     */
    public void removeEstudiante(Long ofertasId, Long estudiantesId) throws BusinessLogicException {

        EstudianteEntity estudianteEntity = estudiantePersistence.find(estudiantesId);
        OfertaEntity ofertaEntity = ofertaPersistence.find(ofertasId);
        int indice= ofertaEntity.getEstudiantes().indexOf(estudianteEntity);
       if(indice>=0){
           
           ofertaEntity.getEstudiantes().remove(indice);
                      
       }
       else throw new BusinessLogicException("El estudiante no está asociado a la oferta");
    }
}
