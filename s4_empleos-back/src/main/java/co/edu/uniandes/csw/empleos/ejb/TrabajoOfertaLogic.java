/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.ejb;


import co.edu.uniandes.csw.empleos.entities.OfertaEntity;
import co.edu.uniandes.csw.empleos.entities.TrabajoEntity;
import co.edu.uniandes.csw.empleos.persistence.OfertaPersistence;
import co.edu.uniandes.csw.empleos.persistence.TrabajoPersistence;
import javax.ejb.Stateless;
import javax.inject.Inject;
/**
 *
 * @author Estudiante
 */
@Stateless
public class TrabajoOfertaLogic {
    
    @Inject
    private TrabajoPersistence trabajoPersistence;

    @Inject
    private OfertaPersistence ofertaPersistence;
    
     /**
     * Remplazar la oferta de un trabajo.
     *
     * @param calId .
     * @param estId .
     * @return .
     */
    public TrabajoEntity replaceOferta(Long calId, Long estId) {
        OfertaEntity ofertaEntity = ofertaPersistence.find(estId);
        TrabajoEntity trabajoEntity = trabajoPersistence.read(calId);
        trabajoEntity.setOferta(ofertaEntity);
        return trabajoEntity;
    }
    
    
    
    /**
     * Borrar una oferta de una trabajo Este metodo se utiliza para borrar la
     * relacion de una oferta.
     *
     * @param trabajoId El libro que se desea borrar de la editorial.
     */
    public void removeOferta(Long trabajoId) {
        TrabajoEntity trabajoEntity = trabajoPersistence.read(trabajoId);
        OfertaEntity ofertaEntity = ofertaPersistence.find(trabajoEntity.getOferta().getId());
        trabajoEntity.setOferta(null);
        ofertaEntity.setTrabajo(null);
    }
    
    /**
     * Asocia una oferta a un trabajo
     *
     * @param trabajoId Identificador de la instancia de trabajo
     * @param ofertaId Identificador de la instancia de Oferta
     * @return Instancia de BookEntity que fue asociada a Author
     */
    public OfertaEntity addOferta(Long trabajoId, Long ofertaId) {
        TrabajoEntity trabajoEntity = trabajoPersistence.read(trabajoId);
        OfertaEntity ofertaEntity = ofertaPersistence.find(ofertaId);
        trabajoEntity.setOferta(ofertaEntity);
        ofertaEntity.setTrabajo(trabajoEntity);
        return ofertaPersistence.find(ofertaId);
    }
    
        /**
     * Obtiene una instancia de CuentaBancariaEntity asociada a una
     * instancia de Estudiante
     *
     * @param trabajoId Identificador de la instancia de trabajo
     * @return instancia de oferta asociada a la instancia de estudiante
     * 
     */
    public OfertaEntity getOferta(Long trabajoId) {
        return trabajoPersistence.read(trabajoId).getOferta();
    }

}
