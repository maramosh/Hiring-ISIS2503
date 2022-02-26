package co.edu.uniandes.csw.empleos.ejb;

import co.edu.uniandes.csw.empleos.entities.EstudianteEntity;
import co.edu.uniandes.csw.empleos.persistence.EstudiantePersistence;
import co.edu.uniandes.csw.empleos.persistence.OfertaPersistence;
import co.edu.uniandes.csw.empleos.entities.OfertaEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class EstudianteOfertasLogic {

    @Inject
    private EstudiantePersistence estudiantePersistence;

    @Inject
    private OfertaPersistence ofertaPersistence;
    
    
    
    
        /**
     * Asocia una oferta existente a un Estudiante
     *
     * @param estudiantesId Identificador de la instancia de Estudiante
     * @param ofertasId Identificador de la instancia de Oferta
     * @return Instancia de BookEntity que fue asociada a Author
     */
    public OfertaEntity addOferta(Long estudiantesId, Long ofertasId) {
        EstudianteEntity estudianteEntity = estudiantePersistence.find(estudiantesId);
        OfertaEntity ofertaEntity = ofertaPersistence.find(ofertasId);
        ofertaEntity.getEstudiantes().add(estudianteEntity);
        estudianteEntity.getOfertas().add(ofertaEntity);
        return ofertaPersistence.find(ofertasId);
    }
    
        /**
     * Obtiene una colección de instancias de OfertaEntity asociadas a una
     * instancia de Estudiante
     *
     * @param estudianteId Identificador de la instancia de Estudiante
     * @return Colección de instancias de OfertaEntity asociadas a la instancia de
     * Author
     */
    public List<OfertaEntity> getOfertas(Long estudianteId) {
        return estudiantePersistence.find(estudianteId).getOfertas();
    }
    
        /**
     * Obtiene una instancia de OfertaEntity asociada a una instancia de Estudiante
     *
     * @param estudianteId Identificador de la instancia de Estudiante
     * @param ofertasId Identificador de la instancia de Oferta
     * @return La entidadd de Oferta del estudiante
     * @throws BusinessLogicException Si la Oferta no está asociado al estudiante
     */
    public OfertaEntity getOferta(Long estudianteId, Long ofertasId) throws BusinessLogicException {
        List<OfertaEntity> books = estudiantePersistence.find(estudianteId).getOfertas();
        OfertaEntity ofertaEntity = ofertaPersistence.find(ofertasId);
        int index = books.indexOf(ofertaEntity);
        if (index >= 0) {
            return books.get(index);
        }
        throw new BusinessLogicException("La ofeta no está asociado al estudiante");
    }
    
         /**
     * Remplazar la oferta de un estudiante.
     * @param estudianteId Identificador de la instancia de Author
     * @param ofettas Colección de instancias de BookEntity a asociar a instancia
     * de Author
     * @return Nueva colección de OfertaEntity asociada a la instancia de Author
     */
    public List<OfertaEntity> replaceOfertas(Long estudianteId, List<OfertaEntity> ofettas) {
        EstudianteEntity estEntity = estudiantePersistence.find(estudianteId);
        List<OfertaEntity> ofertaList = ofertaPersistence.findAll();
        for (OfertaEntity oferta : ofertaList) {
            if (ofettas.contains(oferta)) {
                if (!oferta.getEstudiantes().contains(estEntity)) {
                    oferta.getEstudiantes().add(estEntity);
                }
            } else {
                oferta.getEstudiantes().remove(estEntity);
            }
        }
        estEntity.setOfertas(ofettas);
        return estEntity.getOfertas();
    }
    
    /**
     * Desasocia una Oferta existente de un Estudiante existente
     *
     * @param estudiantesId Identificador de la instancia de Estudiante
     * @param ofertasId Identificador de la instancia de Oferta
     */
    public void removeOferta(Long estudiantesId, Long ofertasId) {
        EstudianteEntity estudainteEntity = estudiantePersistence.find(estudiantesId);
        OfertaEntity ofertaEntity = ofertaPersistence.find(ofertasId);
        ofertaEntity.getEstudiantes().remove(estudainteEntity);
        estudainteEntity.getOfertas().remove(ofertaEntity);
    }
    
    /**
     * Desasocia todas las Oferta existentes de un Estudiante existente
     *
     * @param estudiantesId Identificador de la instancia de Estudiante
     */
    public void removeOfertas(Long estudianteId) {
        EstudianteEntity estudianteEntity = estudiantePersistence.find(estudianteId);
        if(estudianteEntity != null)
            for(OfertaEntity oferta : estudianteEntity.getOfertas()) {
                estudianteEntity.getOfertas().remove(oferta);
                oferta.getEstudiantes().remove(estudianteEntity);
            }
    }
}