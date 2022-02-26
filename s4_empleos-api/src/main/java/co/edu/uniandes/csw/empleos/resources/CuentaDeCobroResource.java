package co.edu.uniandes.csw.empleos.resources;

import co.edu.uniandes.csw.empleos.dtos.CalificacionDTO;
import co.edu.uniandes.csw.empleos.dtos.CuentaDeCobroDTO;
import co.edu.uniandes.csw.empleos.ejb.CuentaDeCobroLogic;
import co.edu.uniandes.csw.empleos.ejb.TokenLogic;
import co.edu.uniandes.csw.empleos.entities.CuentaDeCobroEntity;
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
 * @author Santiago Tangarife Rincón
 */
@Path("cuentasDeCobro")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class CuentaDeCobroResource {

    private static final String NO_EXISTE = " no existe.";
    private static final String RECURSO = "El recurso /cuentasDeCobro/";
    private static final String NO_ENCUENTRA ="No se encuentra Registrado";
    @Inject
    private TokenLogic tokenLogic;

    @Inject
    private CuentaDeCobroLogic cuentaDeCobroLogic;

    @POST
    public CuentaDeCobroDTO createCuentaDeCobro(CuentaDeCobroDTO cuenta) throws BusinessLogicException {

        String token = cuenta.getToken();
        TokenEntity tok = tokenLogic.getTokenByToken(token);
        if (tok.getTipo().equals("Contratista")) {
            return new CuentaDeCobroDTO(cuentaDeCobroLogic.createCuentaDeCobro(cuenta.toEntity()));

        } else {
            throw new BusinessLogicException("No se le tiene permitido acceder a este recurso");
        }

    }

    /**
     * Busca la tarjeta con el id asociado recibido en la URL y lo devuelve.
     *
     * @param cuentaId Identificador de la tarjeta que se esta buscando. Este
     * debe ser una cadena de dígitos.
     * @return JSON {@link TarjetaDeCreditoDTO} - El libro buscado
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra la tarjeta.
     */
    @GET
    @Path("{cuentasId: \\d+}")
    public CuentaDeCobroDTO getCuentaDeCobro(@QueryParam("token")String token, @PathParam("cuentasId") Long cuentaId) throws BusinessLogicException {

        CuentaDeCobroEntity entity = cuentaDeCobroLogic.getCuenta(cuentaId);
        if (entity == null) {
            throw new WebApplicationException("El recurso /cuentas/" + cuentaId + "no existe.", 404);
        }
        CuentaDeCobroDTO cuentaDTO = new CuentaDeCobroDTO(entity);

        TokenEntity tok = tokenLogic.getTokenByToken(token);
        if (tok == null) {
            throw new BusinessLogicException(NO_ENCUENTRA);

        }


        return cuentaDTO;

    }

    /**
     * Busca y devuelve todas las editoriales que existen en la aplicacion.
     *
     * @return JSONArray {@link EditorialDetailDTO} - Las editoriales
     * encontradas en la aplicación. Si no hay ninguna retorna una lista vacía.
     */
    @GET
    public List<CuentaDeCobroDTO> getCuentasDeCobro() {

        return listEntity2DTO(cuentaDeCobroLogic.getCuentassDeCobro());
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
    @Path("{cuentasId: \\d+}")
    public CuentaDeCobroDTO updateCuentaDeCobro(@PathParam("cuentasId") Long calId, CuentaDeCobroDTO calif) throws BusinessLogicException {

        String token = calif.getToken();
        TokenEntity tok = tokenLogic.getTokenByToken(token);
        if (tok.getTipo().equals("Contratista")) {
            calif.setId(calId);
            if (cuentaDeCobroLogic.getCuenta(calId) == null) {
                throw new WebApplicationException(RECURSO + calId + NO_EXISTE, 404);
            }
            return new CuentaDeCobroDTO(cuentaDeCobroLogic.updateCuentaDeCobro(calId, calif.toEntity()));

        } else {
           throw new WebApplicationException("No tiene permitido acceder a "+RECURSO);
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
    @Path("{cuentasId: \\d+}")
    public void deleteCuentaDeCobro(@QueryParam("token") String token, @PathParam("cuentasId") Long calId) throws BusinessLogicException {
        CuentaDeCobroEntity calEntity = cuentaDeCobroLogic.getCuenta(calId);

        if (calEntity == null) {
            throw new WebApplicationException(RECURSO + calId + NO_EXISTE, 404);
        }
      
        TokenEntity tok = tokenLogic.getTokenByToken(token);
        if (tok == null) {

            throw new WebApplicationException(NO_ENCUENTRA);
        }
        if (tok.getTipo().equals("Estudiante")) {

           throw new WebApplicationException("No tiene permitido acceder a "+RECURSO);
        }
        cuentaDeCobroLogic.deleteCuentaDeCobro(calId);
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
    private List<CuentaDeCobroDTO> listEntity2DTO(List<CuentaDeCobroEntity> entityList) {
        List<CuentaDeCobroDTO> list = new ArrayList<>();
        for (CuentaDeCobroEntity entity : entityList) {
            list.add(new CuentaDeCobroDTO(entity));
        }
        return list;
    }

}
