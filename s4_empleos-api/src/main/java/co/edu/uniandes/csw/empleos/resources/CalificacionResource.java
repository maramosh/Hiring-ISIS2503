/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.resources;

import co.edu.uniandes.csw.empleos.dtos.CalificacionDTO;
import co.edu.uniandes.csw.empleos.ejb.CalificacionEstudianteLogic;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.ejb.CalificacionLogic;
import co.edu.uniandes.csw.empleos.ejb.TokenLogic;
import co.edu.uniandes.csw.empleos.entities.CalificacionEntity;
import co.edu.uniandes.csw.empleos.entities.TokenEntity;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author Nicolas Munar
 */
@Path("calificaciones")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class CalificacionResource {

    private static final String CONTRATISTA = "Contratista";
    private static final String NO_EXISTE = " no existe.";
    private static final String RECURSO = "El recurso /calificaciones/";

    @Inject
    private CalificacionLogic calificacionLogic; // Variable para acceder a la lógica de la aplicación. Es una inyección de dependencias.

    @Inject
    private CalificacionEstudianteLogic calificacionEstudianteLogic; // Variable para acceder a la lógica de la aplicación. Es una inyección de dependencias.

    @Inject
    private TokenLogic tokenLogic;

    @POST
    public CalificacionDTO createCalificacion(CalificacionDTO calificacion) throws BusinessLogicException {

        String token = calificacion.getToken();
        TokenEntity tok = tokenLogic.getTokenByToken(token);
        if (tok.getTipo().equals(CONTRATISTA)) {
            CalificacionEntity cl = calificacionLogic.createCalificacion(calificacion.toEntity());
            
            return new CalificacionDTO(cl);

        } else {
            throw new BusinessLogicException("No se le tiene permitido acceder a este recurso");
        }

    }

    /**
     * Busca la calificaciòn con el id asociado recibido en la URL y lo
     * devuelve.
     *
     * @param token
     * @param calificacionId Identificador del libro que se esta buscando. Este
     * debe ser una cadena de dígitos.
     * @return JSON {@link BookDTO} - El libro buscado
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra el libro.
     */
    @GET
    @Path("{calificacionesId: \\d+}")
    public CalificacionDTO getCalificacion(@QueryParam("token")String token ,@PathParam("calificacionesId") Long calificacionId) throws BusinessLogicException {
        
        CalificacionEntity calEntity = calificacionLogic.getCalificacion(calificacionId);

       if (calEntity == null) {
            throw new WebApplicationException(RECURSO + calificacionId + NO_EXISTE, 404);
        }
    
        TokenEntity tok = tokenLogic.getTokenByToken(token);
        if (tok == null) {
            throw new BusinessLogicException("No se encuentra Registrado");

        }

        return new CalificacionDTO(calEntity);

    }

    /**
     * Busca y devuelve todas las editoriales que existen en la aplicacion.
     *
     * @return JSONArray {@link EditorialDetailDTO} - Las editoriales
     * encontradas en la aplicación. Si no hay ninguna retorna una lista vacía.
     */
    @GET
    public List<CalificacionDTO> getCalificaciones() {
        return listEntity2DTO(calificacionLogic.getCalificaciones());
    }

    /**
     * Actualiza la calificacion con el id recibido en la URL con la información
     * que se recibe en el cuerpo de la petición.
     *
     * @param calId Identificador de la Calificacion que se desea actualizar.
     * Este debe ser una cadena de dígitos.
     * @param calif {@link CalificacionDTO} La Calificacion que se desea
     * guardar.
     * @return JSON {@link CalificacionDTO} - La Calificacion guardada.
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra la Calificacion a
     * actualizar.
     * @throws BusinessLogicException {@link BusinessLogicExceptionMapper} -
     * Error de lógica que se genera cuando no se puede actualizar la
     * Calificacion.
     */
    @PUT
    @Path("{calificacionesId: \\d+}")
    public CalificacionDTO updateCalificacion(@PathParam("calificacionesId") Long calId, CalificacionDTO calif) throws BusinessLogicException {

        String token = calif.getToken();
        TokenEntity tok = tokenLogic.getTokenByToken(token);
        if (tok.getTipo().equals(CONTRATISTA)) {
            calif.setId(calId);
            if (calificacionLogic.getCalificacion(calId) == null) {
                throw new WebApplicationException(RECURSO + calId + NO_EXISTE, 404);
            }
            return new CalificacionDTO(calificacionLogic.updateCalificacion(calId, calif.toEntity()));

        } else {
            throw new WebApplicationException("No se encuentra registrado");
        }

    }

    /**
     * Borra La Calificacion con el id asociado recibido en la URL.
     *
     * @param token
     * @param calId Identificador del La Calificacion que se desea borrar. Este
     * debe ser una cadena de dígitos
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra La Calificacion.
     */
    @DELETE
    @Path("{calificacionesId: \\d+}")
    public void deleteCalificacion(@QueryParam("token")String token, @PathParam("calificacionesId") Long calId) throws BusinessLogicException {

        CalificacionEntity calEntity = calificacionLogic.getCalificacion(calId);
        if (calEntity == null) {

            throw new WebApplicationException(RECURSO + calId + NO_EXISTE, 404);
        }

        TokenEntity tok = tokenLogic.getTokenByToken(token);
        if (tok == null) {

            throw new WebApplicationException("No se encuentra registrado");
        }
        if (tok.getTipo().equals("Estudiante")) {

           throw new WebApplicationException("No tiene permitido acceder a "+RECURSO);
        }
        if(tok.getTipo().equals(CONTRATISTA)){
        calificacionLogic.deleteCalificacion(calId);
        }
        
        
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
    private List<CalificacionDTO> listEntity2DTO(List<CalificacionEntity> entityList) {
        List<CalificacionDTO> list = new ArrayList<>();
        for (CalificacionEntity entity : entityList) {
            list.add(new CalificacionDTO(entity));
        }
        return list;
    }

}
