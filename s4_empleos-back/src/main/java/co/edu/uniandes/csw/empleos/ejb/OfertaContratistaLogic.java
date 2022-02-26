/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.ejb;

import co.edu.uniandes.csw.empleos.entities.ContratistaEntity;
import co.edu.uniandes.csw.empleos.entities.OfertaEntity;
import co.edu.uniandes.csw.empleos.persistence.ContratistaPersistence;
import co.edu.uniandes.csw.empleos.persistence.OfertaPersistence;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author je.hernandezr
 */
@Stateless
public class OfertaContratistaLogic {
    @Inject
    private OfertaPersistence ofertaPerssitance;
    @Inject
    private ContratistaPersistence contratistaPersistence;
    
    
    /**
     * Remplazar lel contratista de una oferta.
     *
     * @param ofertasId   
     * @param contratistaId   
     * @return la nueva oferta.
     */
    public OfertaEntity replaceContratista(Long ofertasId, Long contratistaId) {
      
        ContratistaEntity contratistaEntity = contratistaPersistence.find(contratistaId);
        OfertaEntity ofertaEntity = ofertaPerssitance.find(ofertasId);
        ofertaEntity.setContratista(contratistaEntity);
        return ofertaEntity;
    }
    
    /**
     * Borrar una oferta de un contratista. Este metodo se utiliza para borrar la
     * relacion de una oferta.
     * @param ofertaId
     */
    public void removeContratista(Long ofertaId) {
       
        OfertaEntity ofertaEntity = ofertaPerssitance.find(ofertaId);
        ContratistaEntity contratistaEntity = contratistaPersistence.find(ofertaEntity.getContratista().getId());
        ofertaEntity.setContratista(null);
        ofertaPerssitance.update(ofertaEntity);
        contratistaEntity.getOfertas().remove(ofertaEntity);
        contratistaPersistence.update(contratistaEntity);
                
        
    }
}
