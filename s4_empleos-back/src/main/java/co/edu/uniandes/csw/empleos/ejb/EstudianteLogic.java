/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.ejb;
import co.edu.uniandes.csw.empleos.entities.EstudianteEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.CuentaBancariaPersistence;
import co.edu.uniandes.csw.empleos.persistence.EstudiantePersistence;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author David Domínguez
 */
@Stateless
public class EstudianteLogic {
    
    @Inject
    private EstudiantePersistence persistence;
    @Inject
    private CuentaBancariaPersistence cuentaBancariaPersistence;
    
    private void verificarReglasNegocio(EstudianteEntity entity) throws BusinessLogicException{
        if(!entity.getCorreo().toLowerCase().endsWith("@uniandes.edu.co")) throw new BusinessLogicException("El correo no era de uniandes");
        if(entity.getNombre().contains("\"") || entity.getNombre().contains("'")|| entity.getNombre().equals("")) throw new BusinessLogicException("No es un nombre válido");
        if(entity.getCalificacionPromedio() > 5 || entity.getCalificacionPromedio() < 0) throw new BusinessLogicException("No es una calificación válida");
        if(entity.getSemestre() > 12 || entity.getSemestre() < 1) throw new BusinessLogicException("No es un semestre válido");
        if(entity.getCarrera().equals("")) throw new BusinessLogicException("No es una carrera válida");
        if(entity.getHorarioDeTrabajo().equals("")) throw new BusinessLogicException("No es un horario válido");
    }
    
    // Intenta crear al estudiante
    public EstudianteEntity crearEstudiante(EstudianteEntity entity) throws BusinessLogicException {
            verificarReglasNegocio(entity);
            entity = persistence.create(entity);
            return entity;    
    }
    
    //Intenta actualizar al estudiante
    public EstudianteEntity updateEstudiante(EstudianteEntity entity) throws BusinessLogicException {
         
            verificarReglasNegocio(entity);
            entity = persistence.update(entity);
            return entity;
         
    }
    
    //No hay reglas de negocio sobre leer un estudiante
    public EstudianteEntity getEstudiante(long id) {
        return persistence.read(id);
    }
    
    //No hay reglas de negocio sobre leer un estudiante
    public List<EstudianteEntity> getEstudiantes() {
        return persistence.findAll();
    }
    
    //No hay reglas de negocio sobre borrar un estudiante
    public void deleteEstudiante(long id) {
        persistence.delete(id);
    }

}