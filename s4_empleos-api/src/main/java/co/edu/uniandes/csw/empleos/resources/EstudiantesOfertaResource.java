/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.resources;

import co.edu.uniandes.csw.empleos.dtos.EstudianteDTO;
import co.edu.uniandes.csw.empleos.dtos.EstudianteDetailDTO;
import co.edu.uniandes.csw.empleos.ejb.EstudianteLogic;
import co.edu.uniandes.csw.empleos.ejb.OfertaEstudianteLogic;
import co.edu.uniandes.csw.empleos.entities.EstudianteEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * Clase que implementa el recurso "ofertas/{id}/estudiantes".
 *
 * @author ISIS2603
 * @version 1.0
 */
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class EstudiantesOfertaResource {
    
    private static final  String NO_EXISTE = " no existe.";
    private static final  String RECURSO = "El recurso /estudiantes/";
    
    private static final Logger LOGGER = Logger.getLogger(EstudiantesOfertaResource.class.getName());

    @Inject
    private OfertaEstudianteLogic estudiantesOfertaLogic; // Variable para acceder a la lógica de la aplicación. Es una inyección de dependencias.

    @Inject
    private EstudianteLogic estudianteLogic; // Variable para acceder a la lógica de la aplicación. Es una inyección de dependencias.

    /**
     * Guarda un libro dentro de una oferta con la informacion que recibe el
     * la URL. Se devuelve el libro que se guarda en la oferta.
     *
     * @param ofertasId Identificador de la oferta que se esta
     * actualizando. Este debe ser una cadena de dígitos.
     * @param estudiantesId Identificador del libro que se desea guardar. Este debe
     * ser una cadena de dígitos.
     * @return JSON {@link EstudianteDTO} - El libro guardado en la oferta.
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra el libro.
     */
    @POST
    @Path("{estudiantesId: \\d+}")
    public EstudianteDTO addEstudiante(@PathParam("ofertasId") Long ofertasId, @PathParam("estudiantesId") Long estudiantesId) {
        LOGGER.log(Level.INFO, "EstudiantesOfertaResource addEstudiante: input: estudianteID: {0} , estudiantesId: {1}", new Object[]{ofertasId, estudiantesId});
        if (estudianteLogic.getEstudiante(estudiantesId) == null) {
            throw new WebApplicationException(RECURSO + estudiantesId + NO_EXISTE, 404);
        }
        EstudianteDTO estudianteDTO = new EstudianteDTO(estudiantesOfertaLogic.addEstudiante(estudiantesId, ofertasId));
        LOGGER.log(Level.INFO, "EstudiantesOfertaResource addEstudiante: output: {0}", estudianteDTO);
        return estudianteDTO;
    }

    /**
     * Busca y devuelve todos los estudiantes que existen en la oferta.
     *
     * @param ofertasId Identificador de la oferta que se esta buscando.
     * Este debe ser una cadena de dígitos.
     * @return JSONArray {@link EstudianteDetailDTO} - Los libros encontrados en la
     * oferta. Si no hay ninguno retorna una lista vacía.
     */
    @GET
    public List<EstudianteDetailDTO> getEstudiantes(@PathParam("ofertasId") Long ofertasId) {
        LOGGER.log(Level.INFO, "EstudiantesOfertaResource getEstudiantes: input: {0}", ofertasId);
        List<EstudianteDetailDTO> listaDetailDTOs = estudiantesListEntity2DTO(estudiantesOfertaLogic.getEstudiantes(ofertasId));
        LOGGER.log(Level.INFO, "EstudiantesOfertaResource getEstudiantes: output: {0}", listaDetailDTOs);
        return listaDetailDTOs;
    }

    /**
     * Busca el libro con el id asociado dentro de la oferta con id asociado.
     *
     * @param ofertasId Identificador de la oferta que se esta buscando.
     * Este debe ser una cadena de dígitos.
     * @param estudiantesId Identificador del libro que se esta buscando. Este debe
     * ser una cadena de dígitos.
     * @return JSON {@link EstudianteDetailDTO} - El libro buscado
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra el libro.
     * @throws BusinessLogicException {@link BusinessLogicExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra el libro en la
     * oferta.
     */
    @GET
    @Path("{estudiantesId: \\d+}")
    public EstudianteDetailDTO getEstudiante(@PathParam("ofertasId") Long ofertasId, @PathParam("estudiantesId") Long estudiantesId) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "EstudiantesOfertaResource getEstudiante: input: ofertasID: {0} , estudiantesId: {1}", new Object[]{ofertasId, estudiantesId});
        if (estudianteLogic.getEstudiante(estudiantesId) == null) {
            throw new WebApplicationException("El recurso /ofertas/" + ofertasId + "/estudiantes/" + estudiantesId + NO_EXISTE, 404);
        }
        EstudianteDetailDTO estudianteDetailDTO = new EstudianteDetailDTO(estudiantesOfertaLogic.getEstudiante(ofertasId, estudiantesId));
        LOGGER.log(Level.INFO, "EstudiantesOfertaResource getEstudiante: output: {0}", estudianteDetailDTO);
        return estudianteDetailDTO;
    }

    /**
     * Remplaza las instancias de Estudiante asociadas a una instancia de Editorial
     *
     * @param ofertasId Identificador de la oferta que se esta
     * remplazando. Este debe ser una cadena de dígitos.
     * @param estudiantes JSONArray {@link EstudianteDTO} El arreglo de libros nuevo para la
     * oferta.
     * @return JSON {@link EstudianteDTO} - El arreglo de libros guardado en la
     * oferta.
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra el libro.
     */
    @PUT
    public List<EstudianteDetailDTO> replaceEstudiantes(@PathParam("ofertasId") Long ofertasId, List<EstudianteDetailDTO> estudiantes) {
        LOGGER.log(Level.INFO, "EstudiantesOfertaResource replaceEstudiantes: input: ofertasId: {0} , estudiantes: {1}", new Object[]{ofertasId, estudiantes});
        for (EstudianteDetailDTO estudiante : estudiantes) {
            if (estudianteLogic.getEstudiante(estudiante.getId()) == null) {
                throw new WebApplicationException(RECURSO + estudiante.getId() + NO_EXISTE, 404);
            }
        }
        List<EstudianteDetailDTO> listaDetailDTOs = estudiantesListEntity2DTO(estudiantesOfertaLogic.replaceEstudiantes(ofertasId, estudiantesListDTO2Entity(estudiantes)));
        LOGGER.log(Level.INFO, "EstudiantesOfertaResource replaceEstudiantes: output: {0}", listaDetailDTOs);
        return listaDetailDTOs;
    }

    /**
     * Convierte una lista de EstudianteEntity a una lista de EstudianteDetailDTO.
     *
     * @param entityList Lista de EstudianteEntity a convertir.
     * @return Lista de EstudianteDTO convertida.
     */
    private List<EstudianteDetailDTO> estudiantesListEntity2DTO(List<EstudianteEntity> entityList) {
        List<EstudianteDetailDTO> list = new ArrayList();
        for (EstudianteEntity entity : entityList) {
            list.add(new EstudianteDetailDTO(entity));
        }
        return list;
    }

    /**
     * Convierte una lista de EstudianteDetailDTO a una lista de EstudianteEntity.
     *
     * @param dtos Lista de EstudianteDetailDTO a convertir.
     * @return Lista de EstudianteEntity convertida.
     */
    private List<EstudianteEntity> estudiantesListDTO2Entity(List<EstudianteDetailDTO> dtos) {
        List<EstudianteEntity> list = new ArrayList<>();
        for (EstudianteDetailDTO dto : dtos) {
            list.add(dto.toEntity());
        }
        return list;
    }
    
}
