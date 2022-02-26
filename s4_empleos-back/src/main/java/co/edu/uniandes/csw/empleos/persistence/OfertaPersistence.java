/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.persistence;


import co.edu.uniandes.csw.empleos.entities.OfertaEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Clase que maneja la persitencia de una oferta. 
 * Manager de javax.persistance con la base de datos SQL.
 * @author je.berdugo10
 */
@Stateless
public class OfertaPersistence {
    
    @PersistenceContext(unitName = "empleosPU")
    protected EntityManager em;
    
    private static final Logger LOGGER = Logger.getLogger(OfertaPersistence.class.getName());
    
     /**
     * Busca si hay alguna oferta con el id que se envía de argumento
     *
     * @param id: identificadir correspondiente a la oferta buscada.
     * @return una oferta.
     */
    public OfertaEntity find(Long id){

       return em.find(OfertaEntity.class , id);
    }

    /**
     * Crea una oferta en la base de datos.
     *
     * @param ofertaEntity objeto oferta que se creará en la base de datos
     * @return devuelve la entidad creada con un id dado por la base de datos.
     */
    public OfertaEntity create(OfertaEntity ofertaEntity){
        LOGGER.log(Level.INFO,"Creando una nueva oferta");
        em.persist(ofertaEntity);
        LOGGER.log(Level.INFO,"Oferta creada");
    
       return ofertaEntity;
    }
    
     /**
     * Devuelve todas las ofertas de la base de datos.
     *
     * @return una lista con todas las ofertas que encuentre en la base de
     * datos.
     */
    public List<OfertaEntity> findAll() {
        LOGGER.log(Level.INFO, "Consultando todas las ofertas");
        
        TypedQuery query;
        query = em.createQuery("select u from OfertaEntity u", OfertaEntity.class);
        return query.getResultList();
    }
    
    /**
     * Devuelve todas las ofertas de la base de datos.
     *
     * @return una lista con todas las ofertas que encuentre en la base de
     * datos.
     */
    public List<OfertaEntity> findAllPalabra(String palabra) {
        LOGGER.log(Level.INFO, "Consultando todas las ofertas con {0}",palabra);
        
        TypedQuery query;
        query = em.createQuery("select e from OfertaEntity e where e.descripcion like :palabra OR e.nombre like :palabra1 OR e.categoria like :palabra2 OR e.requisitos like :palabra3", OfertaEntity.class);
        query = query.setParameter("palabra", "%" + palabra + "%");
        query = query.setParameter("palabra1", "%" + palabra + "%");
        query = query.setParameter("palabra2", "%" + palabra + "%");
        query = query.setParameter("palabra3", "%" + palabra + "%");
        
        return query.getResultList();
    }
    
    
   
    /**
     * Actualiza una oferta.
     *
     * @param ofertaEntity: el contratista que viene con los nuevos cambios. Por
     * ejemplo el nombre pudo cambiar. En ese caso, se haria uso del método
     * update.
     * @return una oferta con los cambios aplicados.
     */
    public OfertaEntity update(OfertaEntity ofertaEntity) {
        LOGGER.log(Level.INFO, "Actualizando la oferta con id={0}", ofertaEntity.getId());       
        return em.merge(ofertaEntity);
    }

    /**
     * Borra una oferta de la base de datos recibiendo como argumento el id de
     * ela oferta
     *
     * @param id: identificador correspondiente a la oferta a borrar.
     */
    public void delete(Long id) {
        LOGGER.log(Level.INFO, "Borrando la oferta con id={0}", id);        
        OfertaEntity ofertaEntity = em.find(OfertaEntity.class, id);        
        em.remove(ofertaEntity);
    }
    
}
