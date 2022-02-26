/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.resources;

import co.edu.uniandes.csw.empleos.dtos.TokenDTO;
import co.edu.uniandes.csw.empleos.ejb.TokenLogic;
import co.edu.uniandes.csw.empleos.entities.TokenEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author Nicolas Munar */
@Path("tokens")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class TokenResource {
    
    private static final String NO_EXISTE = " no existe.";
    private static final String RECURSO = "El recurso /tokens/";
    
       @Inject
    private TokenLogic tokenLogic; // Variable para acceder a la lógica de la aplicación. Es una inyección de dependencias. 
  
    
    @POST
    public TokenDTO createToken(TokenDTO token) throws BusinessLogicException {
        
        return new TokenDTO(tokenLogic.createToken(token.toEntity()));
    }
    
    
    /**
     * Busca la calificaciòn con el id asociado recibido en la URL y lo devuelve.
     *
     * @param tokenId Identificador del libro que se esta buscando. Este debe
     * ser una cadena de dígitos.
     * @return JSON {@link BookDTO} - El libro buscado
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra el libro.
     */
    @GET
    @Path("{tokensId: \\d+}")
    public TokenDTO getToken(@PathParam("tokensId") Long tokenId) {
        TokenEntity calEntity = tokenLogic.getToken(tokenId);
        if (calEntity == null) {
            throw new WebApplicationException(RECURSO + tokenId + NO_EXISTE, 404);
        }
       
        return new TokenDTO(calEntity);
    }
    
        /**
     * Busca y devuelve todos los tokens que existen en la aplicacion.
     *
     * @return JSONArray {@link EditorialDetailDTO} - Los tokens
     * encontradas en la aplicación. Si no hay ninguna retorna una lista vacía.
     */
    @GET
    public List<TokenDTO> getTokens() {
        
        return listEntity2DTO(tokenLogic.getTokens());
    }
    
     /**
     * Actualiza la calificacion con el id recibido en la URL con la información que se
     * recibe en el cuerpo de la petición.
     *
     * @param tkoId Identificador de la Calificacion que se desea actualizar. Este debe
     * ser una cadena de dígitos.
     * @param tok {@link CalificacionDTO} La Calificacion que se desea guardar.
     * @return JSON {@link CalificacionDTO} - La Calificacion guardada.
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra la Calificacion a
     * actualizar.
     * @throws BusinessLogicException {@link BusinessLogicExceptionMapper} -
     * Error de lógica que se genera cuando no se puede actualizar la Calificacion.
     */
    @PUT
    @Path("{tokensId: \\d+}")
    public TokenDTO updateToken(@PathParam("tokensId") Long tkoId, TokenDTO tok) throws BusinessLogicException {
        tok.setId(tkoId);
        if (tokenLogic.getToken(tkoId) == null) {
            throw new WebApplicationException(RECURSO + tkoId + NO_EXISTE, 404);
        }
        
        return new TokenDTO(tokenLogic.updateToken(tkoId, tok.toEntity()));
    }
    
           /**
     * Convierte una lista de entidades a DTO.
     *
     * Este método convierte una lista de objetos BookEntity a una lista de
     * objetos BookDetailDTO (json)
     *
     * @param entityList corresponde a la lista de libros de tipo Entity que
     * vamos a convertir a DTO.
     * @return la lista de libros en forma DTO (json)
     */
    private List<TokenDTO> listEntity2DTO(List<TokenEntity> entityList) {
        List<TokenDTO> list = new ArrayList<>();
        for (TokenEntity entity : entityList) {
            list.add(new TokenDTO(entity));
        }
        return list;
    }

}
