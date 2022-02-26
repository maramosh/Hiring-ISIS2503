/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.resources;

import co.edu.uniandes.csw.empleos.dtos.CalificacionDTO;
import co.edu.uniandes.csw.empleos.ejb.CalificacionLogic;
import co.edu.uniandes.csw.empleos.ejb.EstudianteCalificacionesLogic;
import co.edu.uniandes.csw.empleos.entities.CalificacionEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import javax.enterprise.context.RequestScoped;
import java.util.ArrayList;
import java.util.List;
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
 * Clase que implementa el recurso "estudiantes/{id}/calificaciones".
 *
 * @author Nicolas Munar
 */
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
class EstudianteCalificacionesResource {

    @Inject
    private EstudianteCalificacionesLogic estudianteCalificacionesLogic;

    @Inject
    private CalificacionLogic calificacionLogic;
    
    
    private static final String NO_EXISTE = " no existe.";

    /**
     * Guarda un libro dentro de una editorial con la informacion que recibe el
     * la URL. Se devuelve el libro que se guarda en la editorial.
     *
     * @param estudiantesId Identificador de la editorial que se esta
     * actualizando. Este debe ser una cadena de dígitos.
     * @param calificacionesId Identificador del libro que se desea guardar.
     * Este debe ser una cadena de dígitos.
     * @return JSON {@link BookDTO} - El libro guardado en la editorial.
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra el libro.
     */
    @POST
    @Path("{calificacionesId: \\d+}")
    public CalificacionDTO addCalificacion(@PathParam("estudiantesId") Long estudiantesId, @PathParam("calificacionesId") Long calificacionesId) {
        if (calificacionLogic.getCalificacion(calificacionesId) == null) {
            throw new WebApplicationException("El recurso /calificaciones/" + calificacionesId + NO_EXISTE, 404);
        }
        return new CalificacionDTO(estudianteCalificacionesLogic.addCalificacion(calificacionesId, estudiantesId));
    }

    /**
     * Busca y devuelve todos los libros que existen en la editorial.
     *
     * @param estudiantesId Identificador de la editorial que se esta buscando.
     * Este debe ser una cadena de dígitos.
     * @return JSONArray {@link BookDetailDTO} - Los libros encontrados en la
     * editorial. Si no hay ninguno retorna una lista vacía.
     */
    @GET
    public List<CalificacionDTO> getCalificaciones(@PathParam("estudiantesId") Long estudiantesId) {
        return calificacionesListEntity2DTO(estudianteCalificacionesLogic.getCalificaciones(estudiantesId));
        
    }

    /**
     * Busca el libro con el id asociado dentro de la editorial con id asociado.
     *
     * @param estudiantesId Identificador de la editorial que se esta buscando.
     * Este debe ser una cadena de dígitos.
     * @param calificacionesId Identificador del libro que se esta buscando.
     * Este debe ser una cadena de dígitos.
     * @return JSON {@link BookDetailDTO} - El libro buscado
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra el libro.
     * @throws BusinessLogicException {@link BusinessLogicExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra el libro en la
     * editorial.
     */
    @GET
    @Path("{calificacionesId: \\d+}")
    public CalificacionDTO getCalificacion(@PathParam("estudiantesId") Long estudiantesId, @PathParam("calificacionesId") Long calificacionesId) throws BusinessLogicException {
        if (calificacionLogic.getCalificacion(calificacionesId) == null) {
            throw new WebApplicationException("El recurso /estudaintes/" + estudiantesId + "/calificaciones/" + calificacionesId + NO_EXISTE, 404);
        }
        return new CalificacionDTO(estudianteCalificacionesLogic.getCalificacion(estudiantesId, calificacionesId));
    }

    /**
     * Remplaza las instancias de Book asociadas a una instancia de Editorial
     *
     * @param estudiantesId Identificador de la editorial que se esta
     * remplazando. Este debe ser una cadena de dígitos.
     * @param calificaciones JSONArray {@link BookDTO} El arreglo de libros
     * nuevo para la editorial.
     * @return JSON {@link BookDTO} - El arreglo de libros guardado en la
     * editorial.
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra el libro.
     */
    @PUT
    public List<CalificacionDTO> replaceCalificaciones(@PathParam("estudiantesId") Long estudiantesId, List<CalificacionDTO> calificaciones) {
        for (CalificacionDTO calificacion : calificaciones) {
            if (calificacionLogic.getCalificacion(calificacion.getId()) == null) {
                throw new WebApplicationException("El recurso /calificaciones/" + calificacion.getId() + NO_EXISTE, 404);
            }

        }
        return calificacionesListEntity2DTO(estudianteCalificacionesLogic.replaceCalificaciones(estudiantesId, calificacionesListDTO2Entity(calificaciones)));
        
    }
    
    /**
     * Convierte una lista de BookEntity a una lista de BookDetailDTO.
     *
     * @param entityList Lista de BookEntity a convertir.
     * @return Lista de BookDTO convertida.
     */
    private List<CalificacionDTO> calificacionesListEntity2DTO(List<CalificacionEntity> entityList) {
        List<CalificacionDTO> list = new ArrayList();
        for (CalificacionEntity entity : entityList) {
            list.add(new CalificacionDTO(entity));
        }
        return list;
    }

    /**
     * Convierte una lista de BookDetailDTO a una lista de BookEntity.
     *
     * @param dtos Lista de BookDetailDTO a convertir.
     * @return Lista de BookEntity convertida.
     */
    private List<CalificacionEntity> calificacionesListDTO2Entity(List<CalificacionDTO> dtos) {
        List<CalificacionEntity> list = new ArrayList<>();
        for (CalificacionDTO dto : dtos) {
            list.add(dto.toEntity());
        }
        return list;
    }

}
