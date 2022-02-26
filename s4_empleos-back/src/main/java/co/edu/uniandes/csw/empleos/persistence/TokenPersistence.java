/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.persistence;

import co.edu.uniandes.csw.empleos.entities.TokenEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Nicolas Munar
 */
@Stateless
public class TokenPersistence {
    
      /**
     * Portero, quien admistra quien entra y quien sale del PersistenceContext
     */
    @PersistenceContext(unitName= "empleosPU")
    protected EntityManager em;
    
    /**
     * Busca si hay alguna token con el id que de envía por argumento
     * @param id idCorrespondiente a la calificacion buscada.
     * @return Una calificacion
     */
    public TokenEntity find (Long id)
    {
      return em.find(TokenEntity.class, id);
    }
    
    /**
     * Busca si hay alguna Credencial con el id que se envía de argumento
        * @param correo
     * @return un cuenta credencial.
     */
    public TokenEntity findByToken( String token) {
      
    
   
        TypedQuery query = em.createQuery("Select e From TokenEntity e where e.token = :token", TokenEntity.class);
        // Se remplaza el placeholder ":token" con el valor del argumento 
        query = query.setParameter("token", token);
        // Se invoca el query se obtiene la lista resultado
        List<TokenEntity> sameToken = query.getResultList();
        TokenEntity result;
        if (sameToken == null) {
            result = null;
        } else if (sameToken.isEmpty()) {
            result = null;
        } else {
            result = sameToken .get(0);
        }
    
        return result;
        
    }
    
        /**
     * Método para persistir la entidad en la base de datos
     * @param tokenEntity objeto que se creará en la base de datos
     * @return La entidad creada con un id dado por la base de datos.
     */
    public TokenEntity create(TokenEntity tokenEntity)
    {
        em.persist(tokenEntity);
        return tokenEntity;
    }
    
    /**
     * Devuelve todos los Token de la base de datos
     * @return  Una lista con todas las Tokens que se encuentren en la base de datos
     */
    public List<TokenEntity> findAll()
    {
        TypedQuery<TokenEntity> query  = em.createQuery("select u from TokenEntity u", TokenEntity.class);
        return query.getResultList();
    }
    
     /**
     * Actualiza un Token
     * @param tokenEntity El Token que viene con los nuevos cambios
     * @return Una token con los cambios aplicados
     */
    public TokenEntity update(TokenEntity tokenEntity)
    {
        return em.merge(tokenEntity);
    }
    
    /**
     * Borra un Token de la base de datos recibiendo un Id por parametero correspondiente al elemento  que se quiere eliminar 
     * @param tokenId id correspondiente al elemento que se quiere borrar.
     */
    public void delete(Long tokenId)
    {
        TokenEntity entity = em.find(TokenEntity.class, tokenId);
        em.remove(entity);
    }
    
}
