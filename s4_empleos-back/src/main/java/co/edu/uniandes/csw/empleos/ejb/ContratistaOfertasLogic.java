/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.ejb;

import co.edu.uniandes.csw.empleos.entities.ContratistaEntity;
import co.edu.uniandes.csw.empleos.entities.OfertaEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.ContratistaPersistence;
import co.edu.uniandes.csw.empleos.persistence.OfertaPersistence;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author je.hernandezr
 */
@Stateless
public class ContratistaOfertasLogic {

    @Inject
    private ContratistaPersistence contratistaPersistence;
    @Inject
    private OfertaPersistence ofertaPersistance;

    /**
     * Asocia una oferta existente a un contratista
     *
     * @param contratistaId
     * @param ofertasId
     * @return Instancia de OfertaEntity asociada a un contratista.
     */
    public OfertaEntity addOferta(Long contratistaId, Long ofertasId) throws BusinessLogicException {

        ContratistaEntity contratistaEntity = contratistaPersistence.find(contratistaId);
        if(contratistaEntity==null){
        throw  new BusinessLogicException("el contratista debe existir");
        }
OfertaEntity ofertaEntity = ofertaPersistance.find(ofertasId);
        contratistaEntity.getOfertas().add(ofertaEntity);
        ofertaEntity.setContratista(contratistaEntity);
        return ofertaEntity;
    }

    /**
     * Retorna todos las ofertas asociadas a un contratista.
     *
     * @param contratistaId
     * @return La lista de ofertas de un contratista.
     */
    public List<OfertaEntity> getOfertas(Long contratistaId) {

        return contratistaPersistence.find(contratistaId).getOfertas();
    }

    /**
     * Retorna una oferta asociada a un contratista
     *
     * @param editorialsId
     * @param ofertaId
     * @return la oferta encontrada dentro del contratista
     * @throws BusinessLogicException Si la oferta no se encuentra en el
     * contratista.
     */
    public OfertaEntity getOferta(Long editorialsId, Long ofertaId) throws BusinessLogicException {

        List<OfertaEntity> ofertas = contratistaPersistence.find(editorialsId).getOfertas();
        OfertaEntity ofertaEntity = ofertaPersistance.find(ofertaId);
        int index = ofertas.indexOf(ofertaEntity);

        if (index >= 0) {
            return ofertas.get(index);
        }
        throw new BusinessLogicException("la oferta no esta asociada al contratista");
    }

    /**
     * Remplazar ofertas de un contratista
     *
     * @param contratistaId
     * @param ofertas
     * @return La de las ofertas actualizada.
     */
    public List<OfertaEntity> replaceOfertas(Long contratistaId) {

        ContratistaEntity contratistaEntity = contratistaPersistence.find(contratistaId);
        List<OfertaEntity> ofertaList = ofertaPersistance.findAll();
        for (OfertaEntity oferta : ofertaList) {
            if (ofertaList.contains(oferta)) {
                oferta.setContratista(contratistaEntity);
            } else if (oferta.getContratista() != null && oferta.getContratista().equals(contratistaEntity)) {
                oferta.setContratista(null);
            }
        }

        return ofertaList;
    }

}
