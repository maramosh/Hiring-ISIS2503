/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.ejb;

import co.edu.uniandes.csw.empleos.entities.TokenEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.TokenPersistence;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Nicolás Munar
 */
@Stateless
public class TokenLogic {
    
    private static final String VACIO = "El campo no puede ser vacìo";
    private static final String NULO = "El campo no puede ser nulo";
    
     @Inject
    private TokenPersistence persistence;
    
       /**
     * Guarda una token
     * @param token la entidad de tipo Token a persistir
     * @return entidad luego de persistirla
     * @throws BusinessLogicException Si alguna regla de negocio se rompe.
     */
    public TokenEntity createToken(TokenEntity token) throws BusinessLogicException{
       if(token!=null){
           
           if(token.getTipo()==null){
            throw new BusinessLogicException(NULO);
        }
        if(token.getToken()==null){
            throw new BusinessLogicException(NULO);
        
        }
        if(token.getTipo().equals("")){
            throw new BusinessLogicException(VACIO);
        }
        if(token.getToken().equals("")){
            throw new BusinessLogicException(VACIO);
        }
        
               

       }
       
        return persistence.create(token);
    }
    
        /**
     * Devuelve todas las token que hay en la base de datos.
     * @return  Lista de las entidades del tipo token.
     */
    public List<TokenEntity> getTokens()
    {
       
       return persistence.findAll();
    }
    
        /**
     * Busca Una calificacion por ID
     * @param tokenId El id de la token a buscar.
     * @return La token encontrada, null si no se encuentra.
     */
    public TokenEntity getToken(Long tokenId)
    {
        
        
        return  persistence.find(tokenId);
    }
    
      public TokenEntity getTokenByToken(String token)
    {
        TokenEntity tokenEntityRet = null;
        List<TokenEntity> tokenEntity = persistence.findAll();
        for (TokenEntity tokenEntity1 : tokenEntity) {
           if(tokenEntity1.getToken().equals(token)) 
              {
                  tokenEntityRet=tokenEntity1;
              }
               
        }
        return  tokenEntityRet;
    }
    
         /**
     * Actualizar una calificacion por ID
     *
     * @param tokenId El ID de la calificacion a actualizar
     * @param tokenEntity La entidad de la Calificacion con los cambios deseados
     * @return La entidad de la calificacion luego de actualizarla
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    public TokenEntity updateToken(Long tokenId, TokenEntity tokenEntity)throws BusinessLogicException {
        
         if(tokenEntity.getTipo()==null){
            throw new BusinessLogicException(NULO);
        }
        if(tokenEntity.getToken()==null){
            throw new BusinessLogicException(NULO);
        
        }
        if(tokenEntity.getTipo().equals("")){
            throw new BusinessLogicException(VACIO);
        }
        if(tokenEntity.getToken().equals("")){
            throw new BusinessLogicException(VACIO);
        }
        
        
        
        return persistence.update(tokenEntity);
    }
     
}
