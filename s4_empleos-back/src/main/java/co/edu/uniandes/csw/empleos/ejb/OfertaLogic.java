/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.ejb;

import co.edu.uniandes.csw.empleos.entities.OfertaEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.OfertaPersistence;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Estudiante
 */
@Stateless
public class OfertaLogic {
    
    
    
     private static final Logger LOGGER = Logger.getLogger(OfertaLogic.class.getName());

    @Inject
    private OfertaPersistence persistence;

    /**
     * Se encarga de crear una Oferta en la base de datos.
     *
     * @param ofertaEntity Objeto de OfertaEntity con los datos nuevos
     * @return Objeto de OfertaEntity con los datos nuevos y su ID.
     */
    public OfertaEntity createOferta(OfertaEntity ofertaEntity) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Inicia proceso de creación del oferta");
        if (ofertaEntity.getNombre() == null || ofertaEntity.getNombre().trim().equals("")) {
            throw new BusinessLogicException("El nombre de la oferta está vacío");
        }
        if (ofertaEntity.getCategoria()== null || ofertaEntity.getCategoria().trim().equals("")) {
            throw new BusinessLogicException("la categoria de la oferta está vacía");
        }
        
        if (ofertaEntity.getDescripcion()== null || ofertaEntity.getDescripcion().trim().equals("")) {
            throw new BusinessLogicException("la descripcion de la oferta está vacía");
        }
        
        if ( ofertaEntity.getHorasDeTrabajo()< 1) {
            throw new BusinessLogicException("las horas de trabajo de la oferta debe ser un numero positivo");
        }
        if (ofertaEntity.getNumeroDeVacantes()< 1) {
            throw new BusinessLogicException("el numero de vacantes de la oferta debe ser un numero positivo");
        }
         if (ofertaEntity.getPagoPorHora()< 3500) {
            throw new BusinessLogicException("El pago por hora debe ser minimo el SMLV");
        }
          if (ofertaEntity.getTipoOferta()< 1||ofertaEntity.getTipoOferta()> 2) {
            throw new BusinessLogicException("La oferta debe ser o estandar o express");
        }
        OfertaEntity newOfertaEntity = persistence.create(ofertaEntity);
        LOGGER.log(Level.INFO, "Termina proceso de creación del oferta");
        return newOfertaEntity;
    }
    
     

    /**
     * Obtiene la lista de los registros de oferta.
     *
     * @return Colección de objetos de OfertaEntity.
     */
    public List<OfertaEntity> getOfertas() {
        LOGGER.log(Level.INFO, "Inicia proceso de consultar todos los ofertas");
        List<OfertaEntity> lista = persistence.findAll();
        LOGGER.log(Level.INFO, "Termina proceso de consultar todos los ofertas");
        return lista;
    }
    
    /**
     * Obtiene la lista de los registros de oferta.
     *
     * @return Colección de objetos de OfertaEntity.
     */
    public List<OfertaEntity> getOfertasPalabraClave(String palabra) {
        LOGGER.log(Level.INFO, "Inicia proceso de consultar todos los ofertas por palabra: {0}", palabra);
        List<OfertaEntity> lista = getOfertas();
        List<OfertaEntity> listaPalabra = new ArrayList<>();
        for(OfertaEntity u:lista){
            if(u.getCategoria().toUpperCase().contains(palabra.toUpperCase())||u.getDescripcion().toUpperCase().contains(palabra.toUpperCase())||u.getNombre().toUpperCase().contains(palabra.toUpperCase())||u.getRequisitos().toUpperCase().contains(palabra.toUpperCase())){
                listaPalabra.add(u);
            }
            
        }
        LOGGER.log(Level.INFO, "Termina proceso de consultar todos los ofertas");
        return listaPalabra;
    }

    /**
     * Obtiene los datos de una instancia de Oferta a partir de su ID.
     *
     * @param ofertaId Identificador de la instancia a consultar
     * @return Instancia de OfertaEntity con los datos del Oferta consultado.
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException 
     */
    public OfertaEntity getOferta(Long ofertaId) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Inicia proceso de consultar el oferta con id = {0}", ofertaId);
        OfertaEntity ofertaEntity = persistence.find(ofertaId);
        if (ofertaEntity == null) {
            throw new BusinessLogicException("No existe el oferta con el id"+ofertaId);
        }
        LOGGER.log(Level.INFO, "Termina proceso de consultar del oferta con id = {0}", ofertaId);
        return ofertaEntity;
    }
    
    /**
     * Actualiza la información de una instancia de Oferta.
     *
     * @param ofertaId Identificador de la instancia a actualizar
     * @param ofertaEntity Instancia de OfertaEntity con los nuevos datos.
     * @return Instancia de OfertaEntity con los datos actualizados.
     */
    public OfertaEntity updateOferta(Long ofertaId, OfertaEntity ofertaEntity) {
        LOGGER.log(Level.INFO, "Inicia proceso de actualizar el oferta con id = {0}", ofertaId);
        OfertaEntity newOfertaEntity = persistence.update(ofertaEntity);
        LOGGER.log(Level.INFO, "Termina proceso de actualizar el oferta con id = {0}", ofertaId);
        return newOfertaEntity;
    }

    /**
     * Elimina una instancia de Oferta de la base de datos.
     *
     * @param ofertaId Identificador de la instancia a eliminar.
     */
    public void deleteOferta(Long ofertaId)  {
        LOGGER.log(Level.INFO, "Inicia proceso de borrar el oferta con id = {0}", ofertaId);
       
        persistence.delete(ofertaId);
        LOGGER.log(Level.INFO, "Termina proceso de borrar el oferta con id = {0}", ofertaId);
    }
    
}
