/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.ejb;

import co.edu.uniandes.csw.empleos.entities.ContratistaEntity;
import co.edu.uniandes.csw.empleos.entities.CuentaDeCobroEntity;
import co.edu.uniandes.csw.empleos.persistence.ContratistaPersistence;
import co.edu.uniandes.csw.empleos.persistence.CuentaDeCobroPersistence;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Santiago Tangarife Rincón
 */
 @Stateless
public class CuentaDeCobroContratistaLogic {
    
     private static final Logger LOGGER = Logger.getLogger(CuentaDeCobroContratistaLogic.class.getName());

    @Inject
    private CuentaDeCobroPersistence cuentaPersistence;

    @Inject
    private ContratistaPersistence contratistaPersistence;

    /**
     * Remplazar el contratista de una cuenta.
     *
     * @param cuentasId id de la cuenta que se quiere actualizar.
     * @param contratistasId El id de la editorial que se será del libro.
     * @return el nuevo libro.
     */
    public CuentaDeCobroEntity replaceContratista(Long cuentasId, Long contratistasId) {
        LOGGER.log(Level.INFO, "Inicia proceso de actualizar cuenta con id = {0}", cuentasId);
        ContratistaEntity contratistaEntity = contratistaPersistence.find(contratistasId);
        CuentaDeCobroEntity cuentaEntity = cuentaPersistence.find(cuentasId);
        cuentaEntity.setContratista(contratistaEntity);
        LOGGER.log(Level.INFO, "Termina proceso de actualizar cuenta con id = {0}", cuentaEntity.getId());
        return cuentaEntity;
    }

    /**
     * Borrar una cuenta de un contratista. Este metodo se utiliza para borrar la
     * relacion de una cuenta.
     *
     * @param cuentasId La cuenta que se desea borrar del contratista.
     */
    public void removeContratista(Long cuentasId) {
        LOGGER.log(Level.INFO, "Inicia proceso de borrar el contratista de la cuenta con id = {0}", cuentasId);
        CuentaDeCobroEntity cuentaEntity = cuentaPersistence.find(cuentasId);
        ContratistaEntity contratistaEntity = contratistaPersistence.find(cuentaEntity.getContratista().getId());
        cuentaEntity.setContratista(null);
        contratistaEntity.getCuentasDeCobro().remove(cuentaEntity);
        LOGGER.log(Level.INFO, "Termina proceso de borrar la Editorial del libro con id = {0}", cuentaEntity.getId());
    }
}
