

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.resources;

import co.edu.uniandes.csw.empleos.dtos.TarjetaDeCreditoDTO;
import co.edu.uniandes.csw.empleos.ejb.TarjetaDeCreditoLogic;
import co.edu.uniandes.csw.empleos.ejb.TokenLogic;
import co.edu.uniandes.csw.empleos.entities.TarjetaDeCreditoEntity;
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
 * @author Miguel Angel Ramos
 */
@Path("tarjetas")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped

public class TarjetaDeCreditoResource {

    private static final String NO_EXISTE = " no existe.";
    private static final String RECURSO = "El recurso /tarjetas/";
    private static final String CONTRATISTA = "Contratista";

    @Inject
    private TarjetaDeCreditoLogic tarjetaLogic; // Variable para acceder a la lógica de la aplicación. Es una inyección de dependencias.

    @Inject
    private TokenLogic tokenLogic;

    /**
     * Crea una nueva tarjeta de credito con la informacion que se recibe en el
     * cuerpo de la petición y se regresa un objeto identico con un id
     * auto-generado por la base de datos.
     *
     * @param tarjeta {@link TarjetaDeCreditoDTO} - La tarjeta que se desea
     * guardar.
     * @return JSON {@link TarjetaDeCreditoDTO} - La tarjeta guardado con el
     * atributo id autogenerado.
     * @throws BusinessLogicException {@link BusinessLogicExceptionMapper}
     */
    @POST
    public TarjetaDeCreditoDTO createTarjeta(TarjetaDeCreditoDTO tarjeta) throws BusinessLogicException {

        String token = tarjeta.getToken();
        TokenEntity tok = tokenLogic.getTokenByToken(token);
        if (tok.getTipo().equals(CONTRATISTA)) {
            return new TarjetaDeCreditoDTO(tarjetaLogic.createTarjetaDeCredito(tarjeta.toEntity()));

        } else {
            throw new BusinessLogicException("No se le tiene permitido acceder a este recurso");
        }

    }

    @GET
    public List<TarjetaDeCreditoDTO> getTarjetas() {

        return listEntity2DTO(tarjetaLogic.getTarjetas());

    }

    /**
     * Busca la tarjeta con el id asociado recibido en la URL y lo devuelve.
     *
     * @param tarjetaId Identificador de la tarjeta que se esta buscando. Este
     * debe ser una cadena de dígitos.
     * @return JSON {@link TarjetaDeCreditoDTO} - El libro buscado
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra la tarjeta.
     */
    @GET
    @Path("{tarjetasId: \\d+}")
    public TarjetaDeCreditoDTO getTarjeta(@PathParam("tarjetasId") Long tarjetaId) throws BusinessLogicException {

        TarjetaDeCreditoEntity entity = tarjetaLogic.getTarjetaCredito(tarjetaId);

        if (entity == null) {
            throw new WebApplicationException(RECURSO + tarjetaId + NO_EXISTE, 404);
        }

        TarjetaDeCreditoDTO tarjetaDTO = new TarjetaDeCreditoDTO(entity);

        String token = tarjetaDTO.getToken();
        TokenEntity tok = tokenLogic.getTokenByToken(token);
        if (tok == null) {

            throw new BusinessLogicException("No se encuentra Registrado");
        }

        return tarjetaDTO;

    }

    /**
     * Actualiza una tarjeta con la informacion que se recibe en el cuerpo de la
     * petición y se regresa el objeto actualizado.
     *
     * @param tarjetaId El ID del libro del cual se guarda la reseña
     * @param tarjeta {@link TarjetaDeCreditoDTO} - La reseña que se desea
     * guardar.
     * @return JSON {@link TarjetaDeCreditoDTO} - La reseña actualizada.
     * @throws BusinessLogicException {@link BusinessLogicExceptionMapper} -
     * Error de lógica que se genera cuando ya existe la tarjeta.
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra la tarjeta.
     */
    @PUT
    @Path("{tarjetasId: \\d+}")
    public TarjetaDeCreditoDTO updateTarjeta(@PathParam("tarjetasId") Long tarjetaId, TarjetaDeCreditoDTO tarjeta) throws BusinessLogicException {
        String token = tarjeta.getToken();
        TokenEntity tok = tokenLogic.getTokenByToken(token);
        if (tok.getTipo().equals(CONTRATISTA)) {
            tarjeta.setId(tarjetaId);
            TarjetaDeCreditoEntity entity = tarjetaLogic.getTarjetaCredito(tarjetaId);
            if (entity == null) {
                throw new WebApplicationException(RECURSO + tarjetaId + NO_EXISTE, 404);
            }
            return new TarjetaDeCreditoDTO(tarjetaLogic.updateTarjetaCredito(tarjetaId, tarjeta.toEntity()));
            

        } else {
            throw new WebApplicationException("No tiene permitido acceder a "+RECURSO);
        }

    }

    /**
     * Borra la tarjeta con el id asociado recibido en la URL.
     *
     * @param tarjetaId El ID del libro del cual se va a eliminar la reseña.
     * @throws BusinessLogicException {@link BusinessLogicExceptionMapper} -
     * Error de lógica que se genera cuando no se puede eliminar la reseña.
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra la reseña.
     */
    @DELETE
    @Path("{tarjetasId: \\d+}")
    public void deleteTarjeta(@QueryParam("token") String pToken, @PathParam("tarjetasId") Long tarjetaId) throws BusinessLogicException {

        TarjetaDeCreditoEntity entity = tarjetaLogic.getTarjetaCredito(tarjetaId);
        

        if (entity == null) {
            throw new WebApplicationException(RECURSO + tarjetaId + NO_EXISTE, 404);
        }

        String token = pToken;
        TokenEntity tok = tokenLogic.getTokenByToken(token);
        if (tok == null) {

            throw new BusinessLogicException("No se encuentra Registrado");
        }
        if (!tok.getTipo().equals(CONTRATISTA)) {

            throw new BusinessLogicException("No tiene permiso para esto");
        }

        tarjetaLogic.deleteTarjetaCredito(tarjetaId);
    }

    /**
     * Lista de entidades a DTO.
     *
     * Este método convierte una lista de objetos TarjetaDeCreditoEntity a una
     * lista de objetos TarjetaDeCreditoDTO (json)
     *
     * @param entityList corresponde a la lista de tarjetas de tipo Entity que
     * vamos a convertir a DTO.
     * @return la lista de tarjetas en forma DTO (json)
     */
    private List<TarjetaDeCreditoDTO> listEntity2DTO(List<TarjetaDeCreditoEntity> entityList) {
        List<TarjetaDeCreditoDTO> list = new ArrayList<>();
        for (TarjetaDeCreditoEntity entity : entityList) {
            list.add(new TarjetaDeCreditoDTO(entity));
        }
        return list;
    }
}
