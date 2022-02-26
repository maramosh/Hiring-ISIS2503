/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.resources;

import co.edu.uniandes.csw.empleos.dtos.EstudianteDTO;
import co.edu.uniandes.csw.empleos.dtos.EstudianteDetailDTO;
import co.edu.uniandes.csw.empleos.ejb.EstudianteCalificacionesLogic;
import co.edu.uniandes.csw.empleos.ejb.EstudianteCuentaBancariaLogic;
import co.edu.uniandes.csw.empleos.ejb.EstudianteLogic;
import co.edu.uniandes.csw.empleos.ejb.EstudianteOfertasLogic;
import co.edu.uniandes.csw.empleos.ejb.TokenLogic;
import co.edu.uniandes.csw.empleos.entities.EstudianteEntity;
import co.edu.uniandes.csw.empleos.entities.TokenEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
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
 * @author David Dominguez
 */
@Path("estudiantes")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class EstudianteResource {

    private static final String NO_EXISTE = " no existe.";
    private static final String RECURSO = "El recurso /estudiantes/";

    // Variable para acceder a la lógica de la aplicación. Es una inyección de dependencias.
    @Inject
    private EstudianteLogic estudianteLogic;

    // Variable para acceder a la lógica de la aplicación. Es una inyección de dependencias.
    @Inject
    private EstudianteCalificacionesLogic estudianteCalificacionesLogic;

    // Variable para acceder a la lógica de la aplicación. Es una inyección de dependencias.
    @Inject
    private EstudianteOfertasLogic estudianteOfertasLogic;

    // Variable para acceder a la lógica de la aplicación. Es una inyección de dependencias.
    @Inject
    private EstudianteCuentaBancariaLogic estudianteCuentaBancariaLogic;

    @Inject
    private TokenLogic tokenLogic;

    @POST
    public EstudianteDTO createEstudiante(EstudianteDTO estudiante) throws BusinessLogicException {
       
        return new EstudianteDTO(estudianteLogic.crearEstudiante(estudiante.toEntity()));
    }

    /**
     * Busca el estudiante con el id asociado recibido en la URL y lo devuelve.
  
     * @param estudianteId Identificador del estudiante que se esta buscando.
     * Este debe ser una cadena de dígitos.
     * @return JSON {@link EstudianteDTO} - El estudiante buscado
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra el estudiante.
     */
    @GET
    @Path("{estudiantesId: \\d+}")
    public EstudianteDetailDTO getEstudiante(@QueryParam("token") String token,@PathParam("estudiantesId") Long estudianteId) throws BusinessLogicException {
        EstudianteEntity calEntity = estudianteLogic.getEstudiante(estudianteId);

        if (calEntity == null) {
            throw new WebApplicationException(RECURSO + estudianteId + NO_EXISTE, 404);
        }

        EstudianteDetailDTO calDTO = new EstudianteDetailDTO(calEntity);

        TokenEntity tok = tokenLogic.getTokenByToken(token);
        if (tok == null) {

            throw new BusinessLogicException("No se encuentra Registrado");
        }

        return calDTO;

    }

    /**
     * Busca y devuelve todos los estudiantes que existen en la aplicacion.
     *
     * @return JSONArray {@link EstudianteDTO} - Los estudiantes encontrados en
     * la aplicación. Si no hay ninguna retorna una lista vacía.
     */
    @GET
    public List<EstudianteDetailDTO> getEstudiantes() {

        return listEntity2DTO(estudianteLogic.getEstudiantes());
    }

    /**
     * Actualiza el estudiante con el id recibido en la URL con la información
     * que se recibe en el cuerpo de la petición.
     *
     * @param estudianteId Identificador del estudiante que se desea actualizar.
     * Este debe ser una cadena de dígitos.
     * @param estudiante {@link EstudianteDTO} El estudiante que se desea
     * guardar.
     * @return JSON {@link EstudianteDTO} - El estudiante guardada.
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra el estudiante a
     * actualizar.
     * @throws BusinessLogicException {@link BusinessLogicExceptionMapper} -
     * Error de lógica que se genera cuando no se puede actualizar al
     * estudiante.
     */
    @PUT
    @Path("{estudiantesId: \\d+}")
    public EstudianteDetailDTO updateEstudiante(@PathParam("estudiantesId") Long estudianteId, EstudianteDetailDTO estudiante) throws BusinessLogicException {
        estudiante.setId(estudianteId);

        String token = estudiante.getToken();
        TokenEntity tok = tokenLogic.getTokenByToken(token);
        if (tok.getTipo().equals("Estudiante")) {
            estudiante.setId(estudianteId);
            if (estudianteLogic.getEstudiante(estudianteId) == null) {
                throw new WebApplicationException(RECURSO + estudianteId + NO_EXISTE, 404);
            }
            return new EstudianteDetailDTO(estudianteLogic.updateEstudiante(estudiante.toEntity()));
            

        } else {
            throw new WebApplicationException("No tiene permitido acceder a "+RECURSO);
        }

    }

    /**
     * Borra el Estudiante con el id asociado recibido en la URL.
     *
     * @param estudianteId Identificador del estudiante que se desea borrar.
     * Este debe ser una cadena de dígitos
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra al estudiante.
     */
    @DELETE
    @Path("{estudianteId: \\d+}")
    public void deleteEstudiante(@QueryParam("token") String token, @PathParam("estudianteId") Long estudianteId) throws BusinessLogicException {

        
        if (estudianteLogic.getEstudiante(estudianteId) == null) {
            throw new WebApplicationException(RECURSO + estudianteId + NO_EXISTE, 404);
        }

        TokenEntity tok = tokenLogic.getTokenByToken(token);
        if (tok == null) {

            throw new BusinessLogicException("No se encuentra Registrado");
        }
        if (!(tok.getTipo().equals("Estudiante") || tok.getTipo().equals("Administrador"))) {

            throw new WebApplicationException("No tiene permitido acceder a "+RECURSO);
        }

        estudianteLogic.deleteEstudiante(estudianteId);

    }

    /**
     * Convierte una lista de entidades a DTO.
     *
     * Este método convierte una lista de objetos EstudianteEntity a una lista
     * de objetos EstudianteDTO (json)
     *
     * @param entityList corresponde a la lista de estudiantes de tipo Entity
     * que vamos a convertir a DTO.
     * @return la lista de estudiantes en forma DTO (json)
     */
    private List<EstudianteDetailDTO> listEntity2DTO(List<EstudianteEntity> entityList) {
        List<EstudianteDetailDTO> list = new ArrayList<>();
        for (EstudianteEntity entity : entityList) {
            list.add(new EstudianteDetailDTO(entity));
        }
        return list;
    }

}
