/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.resources;

import co.edu.uniandes.csw.empleos.dtos.ContratistaDTO;
import co.edu.uniandes.csw.empleos.dtos.CredencialDTO;
import co.edu.uniandes.csw.empleos.dtos.EstudianteDTO;
import co.edu.uniandes.csw.empleos.dtos.TokenDTO;
import co.edu.uniandes.csw.empleos.dtos.UsuarioDTO;
import co.edu.uniandes.csw.empleos.ejb.ContratistaLogic;
import co.edu.uniandes.csw.empleos.ejb.CredencialesLogic;
import co.edu.uniandes.csw.empleos.ejb.EstudianteLogic;
import co.edu.uniandes.csw.empleos.ejb.TokenLogic;
import co.edu.uniandes.csw.empleos.ejb.Tokenizer;
import co.edu.uniandes.csw.empleos.entities.ContratistaEntity;
import co.edu.uniandes.csw.empleos.entities.CredencialesEntity;
import co.edu.uniandes.csw.empleos.entities.EstudianteEntity;
import co.edu.uniandes.csw.empleos.entities.TokenEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 *
 * @author David Dominguez
 */
@Path("credenciales")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class CredencialesResource {

    // Variable para acceder a la lógica de la aplicación. Es una inyección de dependencias.
    @Inject
    private CredencialesLogic credencialLogic;
    
    // Variable para acceder a la lógica de la aplicación. Es una inyección de dependencias.
    @Inject
    private EstudianteLogic estudianteLogic;
    
    // Variable para acceder a la lógica de la aplicación. Es una inyección de dependencias.
    @Inject
    private ContratistaLogic contratistaLogic;

    @Inject
    private TokenLogic tokenLogic;
    
    TokenDTO fooDTO(String m) {
        TokenDTO t = new TokenDTO();
        t.setToken(m);
        return t;
    }

    @POST
    public TokenDTO createCredencial(UsuarioDTO usuario, @QueryParam("correo") String correo, @QueryParam("pass") String pass, @QueryParam("tipo") String type) throws BusinessLogicException {
        CredencialDTO credencial = new CredencialDTO();
        credencial.setCorreo(correo);
        credencial.setTipo(type);
        credencial.setContrasenia(pass);
        
        CredencialDTO c = new CredencialDTO(credencialLogic.createCredencial(credencial.toEntity()));

        String tipo = c.getTipo();
        String token = Tokenizer.getToken();

        TokenEntity tokenE = new TokenEntity();
        tokenE.setTipo(tipo);
        tokenE.setToken(token);
        
        TokenDTO nuevoTokenDTO = new TokenDTO(tokenLogic.createToken(tokenE));
        
        if(type.equals("Estudiante")) {
            EstudianteDTO estudiante = new EstudianteDTO();
            estudiante.setId(usuario.getId());
            estudiante.setIdMedioDepago(usuario.getIdMedioDepago());
            estudiante.setNombre(usuario.getNombre());
            estudiante.setCarrera(usuario.getCarrera());
            estudiante.setCalificacionPromedio(usuario.getCalificacionPromedio());
            estudiante.setSemestre(usuario.getSemestre());
            estudiante.setHorarioDeTrabajo(usuario.getHorarioDeTrabajo()); 
            estudiante.setCorreo(usuario.getEmail());
            EstudianteEntity ee = estudianteLogic.crearEstudiante(estudiante.toEntity());
            tokenE.setIdLog(ee.getId());
            nuevoTokenDTO.setIdLog(tokenE.getIdLog());
        } else if (type.equals("Contratista")) {
            ContratistaDTO contratista = new ContratistaDTO();
            contratista.setId(usuario.getId());
            contratista.setEsExterno(usuario.getEsExterno());
            contratista.setNombre(usuario.getNombre());
            contratista.setEmail(usuario.getEmail());
            contratista.setRutaImagen(usuario.getRutaImagen());
            ContratistaEntity e = contratistaLogic.createContratista(contratista.toEntity());
            tokenE.setIdLog(e.getId());
            nuevoTokenDTO.setIdLog(tokenE.getIdLog());
        }
        
        return nuevoTokenDTO;

    }

    @GET
    public TokenDTO autenticar(@QueryParam("correo") String correo, @QueryParam("pass") String pass) throws BusinessLogicException {        
        List<CredencialesEntity> c = credencialLogic.getCredenciales();
        CredencialesEntity credencialUsuario = null;
        Boolean found = false;
        for (CredencialesEntity credencial : c) {
            if(credencial != null) {
               String cCorreo = credencial.getCorreo();
                String cPass = credencial.getContrasenia();
                if(cCorreo != null && cPass != null && cCorreo.equals(correo) && cPass.equals(pass)) {
                        found = true;
                        credencialUsuario = credencial;
                    
                }
            } 
        }

        if (!found) {
            return null;
        } else {
            String tipo = credencialUsuario.getTipo();
            long id = -1;
            if(tipo.equals("Estudiante")) {
                List<EstudianteEntity> ee = estudianteLogic.getEstudiantes();
                for(EstudianteEntity e : ee) if (e.getCorreo().equals(correo)) {
                    id = e.getId();
                    break;
                }
            } else {
                List<ContratistaEntity> ee = contratistaLogic.getContratistas();
                for(ContratistaEntity e : ee) if (e.getEmail().equals(correo)) {
                    id = e.getId();
                    break;
                }
            }
            String token = Tokenizer.getToken();

            TokenEntity tokenE = new TokenEntity();
            tokenE.setTipo(tipo);
            tokenE.setToken(token);
            tokenE.setIdLog(id);
            return new TokenDTO(tokenLogic.createToken(tokenE));
 
        }
    }


}
