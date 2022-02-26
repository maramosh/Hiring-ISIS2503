/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.ejb;

import co.edu.uniandes.csw.empleos.entities.ContratistaEntity;
import co.edu.uniandes.csw.empleos.entities.CuentaDeCobroEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.ContratistaPersistence;
import co.edu.uniandes.csw.empleos.persistence.CuentaDeCobroPersistence;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Santiago Tangarife Rincón
 */
@Stateless
public class ContratistaCuentasLogic {
    private static final Logger LOGGER = Logger.getLogger(ContratistaCuentasLogic.class.getName());

    @Inject
    private CuentaDeCobroPersistence cuentaPersistence;

    @Inject
    private ContratistaPersistence contratistaPersistence;

    /**
     * Agregar una cuenta al contratista
     *
     * @param cuentasId El id de la cuenta a guardar
     * @param contratistasId El id del contratista en la cual se va a guardar la
     * cuenta
     * @return la cuenta creada.
     */
    public CuentaDeCobroEntity addCuenta(Long cuentasId, Long contratistasId) {
        LOGGER.log(Level.INFO, "Inicia proceso de agregarle una cuenta al contratista con id = {0}", contratistasId);
        ContratistaEntity contratistaEntity = contratistaPersistence.find(contratistasId);
        CuentaDeCobroEntity cuentaEntity = cuentaPersistence.find(cuentasId);
        cuentaEntity.setContratista(contratistaEntity);
        LOGGER.log(Level.INFO, "Termina proceso de agregarle una cuenta al contratista con id = {0}", contratistasId);
        return cuentaEntity;
    }

    /**
     * Retorna todos las cuentas asociadas a un contratista
     *
     * @param contratistasId El ID del contratista buscado
     * @return La lista de cuentas del contratista
     */
    public List<CuentaDeCobroEntity> getCuentas(Long contratistasId) {
        LOGGER.log(Level.INFO, "Inicia proceso de consultar las cuentas asociadas al contratista con id = {0}", contratistasId);
        return contratistaPersistence.find(contratistasId).getCuentasDeCobro();
    }

    /**
     * Retorna una cuenta asociada a un contratista
     *
     * @param contratistasId El id del contratista a buscar.
     * @param cuentaId El id de la cuenta a buscar
     * @return La cuenta encontrada dentro del contratista.
     * @throws BusinessLogicException Si la cuenta no se encuentra en el
     * contratista
     */
    public CuentaDeCobroEntity getCuenta(Long contratistasId, Long cuentaId) throws BusinessLogicException {
        LOGGER.log(Level.INFO, String.format("Inicia proceso de consultar la cuenta con id = {0} de el contratista con id = %s", contratistasId), cuentaId);
        List<CuentaDeCobroEntity> cuentas = contratistaPersistence.find(contratistasId).getCuentasDeCobro();
        CuentaDeCobroEntity cuentaEntity = cuentaPersistence.find(cuentaId);
        int index = cuentas.indexOf(cuentaEntity);
        LOGGER.log(Level.INFO, String.format("Termina proceso de consultar la cuenta con id = {0} de el contratista con id = %s", contratistasId), cuentaId);
        if (index >= 0) {
            return cuentas.get(index);
        }
        throw new BusinessLogicException("La cuenta no está asociado al contratista.");
    }

    /**
     * Remplazar cuentas de un contratista
     *
     * @param cuentas Lista de cuentass que serán del contratista.
     * @param contratistasId El id del contratista que se quiere actualizar.
     * @return La lista de cuentas actualizada.
     */
    public List<CuentaDeCobroEntity> replaceCuentas(Long contratistasId, List<CuentaDeCobroEntity> cuentas) {
        LOGGER.log(Level.INFO, "Inicia proceso de actualizar el contratista con id = {0}", contratistasId);
        ContratistaEntity contratistaEntity = contratistaPersistence.find(contratistasId);
        List<CuentaDeCobroEntity> cuentaList = cuentaPersistence.findAll();
        for (CuentaDeCobroEntity cuenta : cuentaList) {
            if (cuentas.contains(cuenta)) {
                cuenta.setContratista(contratistaEntity);
            } else if (cuenta.getContratista()!= null && cuenta.getContratista().equals(contratistaEntity)) {
                cuenta.setContratista(null);
            }
        }
        LOGGER.log(Level.INFO, "Termina proceso de actualizar el contratista con id = {0}", contratistasId);
        return cuentas;
    }
}
