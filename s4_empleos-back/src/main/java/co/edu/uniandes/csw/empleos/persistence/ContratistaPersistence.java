/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.persistence;

import co.edu.uniandes.csw.empleos.entities.ContratistaEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Clase que maneja la persitencia de un contratista. 
 * Manager de javax.persistance con la base de datos SQL.
 * @author je.berdugo10
 */
@Stateless
public class ContratistaPersistence {
    @PersistenceContext(unitName = "empleosPU")
    protected EntityManager em;
    
    private static final Logger LOGGER = Logger.getLogger(ContratistaPersistence.class.getName());
    
     /**
     * Busca si hay algun contratista con el id que se envía de argumento
     *
     * @param id: identificadir correspondiente al contratista buscado.
     * @return un contatista.
     */
    public ContratistaEntity find(Long id){

       return em.find(ContratistaEntity.class , id);
    }

    /**
     * Crea un contratista en la base de datos.
     *
     * @param contratistaEntity objeto contratista que se creará en la base de datos
     * @return devuelve la entidad creada con un id dado por la base de datos.
     */
    public ContratistaEntity create(ContratistaEntity contratistaEntity){
        LOGGER.log(Level.INFO,"Creando un contratista nuevo");
        em.persist(contratistaEntity);
        LOGGER.log(Level.INFO,"Contratista creado");
    
       return contratistaEntity;
    }
    
     /**
     * Devuelve todos los contratistas de la base de datos.
     *
     * @return una lista con todos los contratistas que encuentre en la base de
     * datos.
     */
    public List<ContratistaEntity> findAll() {
        LOGGER.log(Level.INFO, "Consultando todos los contratistas");
        
        TypedQuery query;
        query = em.createQuery("select u from ContratistaEntity u", ContratistaEntity.class);
        return query.getResultList();
    }
    
    
   
    /**
     * Actualiza un contratista.
     *
     * @param contratistaEntity: el contratista que viene con los nuevos cambios. Por
     * ejemplo el nombre pudo cambiar. En ese caso, se haria uso del método
     * update.
     * @return un contratista con los cambios aplicados.
     */
    public ContratistaEntity update(ContratistaEntity contratistaEntity) {
        LOGGER.log(Level.INFO, "Actualizando el contratista con id={0}", contratistaEntity.getId());       
        return em.merge(contratistaEntity);
    }

    /**
     * Borra un contratista de la base de datos recibiendo como argumento el id de
     * el contratista
     *
     * @param id: identificador correspondiente al contratista a borrar.
     */
    public void delete(Long id) {
        LOGGER.log(Level.INFO, "Borrando el contratista con id={0}", id);        
        ContratistaEntity contratistaEntity = em.find(ContratistaEntity.class, id);        
        em.remove(contratistaEntity);
    }
}
    




