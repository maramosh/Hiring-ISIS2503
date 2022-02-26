/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.ejb;


import co.edu.uniandes.csw.empleos.entities.CuentaDeCobroEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.CuentaDeCobroPersistence;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Santiago Tangarife Rincón
 */
@Stateless
public class CuentaDeCobroLogic {

    @Inject
    private CuentaDeCobroPersistence persistence;
    
    public CuentaDeCobroEntity createCuentaDeCobro(CuentaDeCobroEntity cuentaDeCobro) throws BusinessLogicException {
        
        if (cuentaDeCobro == null) {
            throw new BusinessLogicException("La cuenta de cobro es nula.");
        }
        else {
            if (cuentaDeCobro.getContratista() == null) {
                throw new BusinessLogicException("La cuenta de cobro no tiene contratista.");
            }
            else if (cuentaDeCobro.getConcepto() == null || cuentaDeCobro.getConcepto().equals("")) {
                throw new BusinessLogicException("La cuenta de cobro no tiene concepto.");
            }
            else if (cuentaDeCobro.getFecha() == null) {
                throw new BusinessLogicException("La cuenta de cobro no tiene fecha.");
            }
            else if (cuentaDeCobro.getNombreEstudiante() == null || cuentaDeCobro.getNombreEstudiante().equals("")) {
                throw new BusinessLogicException("La cuenta de cobro no tiene la información del estudiante.");
            }
            else if (cuentaDeCobro.getNumeroCuentaDeCobro() <= 0) {
                throw new BusinessLogicException("La cuenta de cobro no tiene número o tiene un número erroneo.");
            }
            else if (cuentaDeCobro.getValor() <= 0) {
                throw new BusinessLogicException("La cuenta de cobro no tiene valor o tiene un valor erroneo.");
            } else {
                
                return persistence.create(cuentaDeCobro);
            }
        } 
    }

    /**
     * Busca una cuenta por ID
     *
     * @param cuentaId El id de la cuenta a buscar
     * @return La cuenta encontrada, null si no la encuentra.
     */
    public CuentaDeCobroEntity getCuenta(Long cuentaId) {
        
        return persistence.find(cuentaId);
    }

    /**
     * Devuelve todas las calificaciones que hay en la base de datos.
     *
     * @return Lista de las entidades del tipo calificacion.
     */
    public List<CuentaDeCobroEntity> getCuentassDeCobro() {
    
        return persistence.findAll();
    }

    /**
     * Actualizar una calificacion por ID
     *
     * @param cuentaId El ID de la calificacion a actualizar
     * @param cuentaDeCobroEntity La entidad de la Calificacion con los cambios
     * deseados
     * @return La entidad de la calificacion luego de actualizarla
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    public CuentaDeCobroEntity updateCuentaDeCobro(Long cuentaId, CuentaDeCobroEntity cuentaEntity) throws BusinessLogicException {

        if (cuentaEntity == null) 
        {
            throw new BusinessLogicException("La cuenta de cobro es nula por lo que no se puede actualizar.");
        }
        else {
            if (cuentaEntity.getContratista() == null) {
                throw new BusinessLogicException("La cuenta de cobro no tiene contratista.");
            }
            else if (cuentaEntity.getNumeroCuentaDeCobro() <= 0) {
                throw new BusinessLogicException("La cuenta de cobro no tiene número o tiene un número erroneo.");
            }
            else if (cuentaEntity.getFecha() == null) {
                throw new BusinessLogicException("La cuenta de cobro no tiene fecha.");
            }
            else if (cuentaEntity.getConcepto() == null || cuentaEntity.getConcepto().equals("")) {
                throw new BusinessLogicException("La cuenta de cobro no tiene concepto.");
            }
            else if (cuentaEntity.getNombreEstudiante() == null || cuentaEntity.getNombreEstudiante().equals("")) {
                throw new BusinessLogicException("La cuenta de cobro no tiene la información del estudiante.");
            }
            else if (cuentaEntity.getValor() <= 0) {
                throw new BusinessLogicException("La cuenta de cobro no tiene valor o tiene un valor erroneo.");
            } else {
                
                return persistence.update(cuentaEntity);
            }
        } 

    }

    /**
     * Eliminar una calificacion por Id
     *
     * @param calificacionId El ID de la calificacion a borrar.
     */
    public void deleteCuentaDeCobro(Long cuentaId) {
        persistence.delete(cuentaId);
    }

}
