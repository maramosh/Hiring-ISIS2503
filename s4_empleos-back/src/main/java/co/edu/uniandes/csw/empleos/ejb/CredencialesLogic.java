/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.ejb;

import co.edu.uniandes.csw.empleos.entities.CredencialesEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.CredencialesPersistence;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author je.hernandezr
 */
@Stateless
public class CredencialesLogic {

    @Inject
    private CredencialesPersistence persistence;

    private static final String VACIO = "El campo no puede ser vacìo";
    private static final String NULO = "El campo no puede ser nulo";

    /**
     *
     * Guarda una token
     *
     * @param credencial la entidad de tipo Token a persistir
     * @return entidad luego de persistirla
     * @throws BusinessLogicException Si alguna regla de negocio se rompe.
     */
    public CredencialesEntity createCredencial(CredencialesEntity credencial) throws BusinessLogicException {
        if (credencial != null) {

            if (credencial.getTipo() == null) {
                throw new BusinessLogicException("a"+NULO);
            }
            if (credencial.getCorreo() == null) {
                throw new BusinessLogicException("b"+NULO);

            }
            if (credencial.getContrasenia() == null) {
                throw new BusinessLogicException("c"+NULO);

            }
            if (credencial.getTipo().equals("")) {
                throw new BusinessLogicException(VACIO);
            }
            if (credencial.getCorreo().equals("")) {
                throw new BusinessLogicException(VACIO);
            }
            if (credencial.getContrasenia().equals("")) {
                throw new BusinessLogicException(VACIO);
            }

            credencial = persistence.create(credencial);
        }

        return credencial;
    }

    /**
     * Devuelve todas las token que hay en la base de datos.
     *
     * @return Lista de las entidades del tipo token.
     */
    public List<CredencialesEntity> getCredenciales() {
        return persistence.findAll();
    }

    /**
     * Busca Una calificacion por ID
     *
     * @param tokenId El id de la token a buscar.
     * @return La token encontrada, null si no se encuentra.
     */
    public CredencialesEntity getCredencial(Long tokenId) {
        return persistence.find(tokenId);
    }

    /**
     * Actualizar una calificacion por ID
     *
     * @param credencialId El ID de la calificacion a actualizar
     * @param credencialEntity La entidad de la Calificacion con los cambios
     * deseados
     * @return La entidad de la calificacion luego de actualizarla
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    public CredencialesEntity updateCredencial(Long credencialId, CredencialesEntity credencialEntity) throws BusinessLogicException {
        CredencialesEntity newEntity = null;
        if (credencialEntity != null) {
            if (credencialEntity.getTipo() == null) {
                throw new BusinessLogicException(NULO);
            }
            if (credencialEntity.getCorreo() == null) {
                throw new BusinessLogicException(NULO);

            }
            if (credencialEntity.getContrasenia() == null) {
                throw new BusinessLogicException(NULO);

            }

            if (credencialEntity.getTipo().equals("")) {
                throw new BusinessLogicException(VACIO);
            }
            if (credencialEntity.getCorreo().equals("")) {
                throw new BusinessLogicException(VACIO);
            }
            if (credencialEntity.getContrasenia().equals("")) {
                throw new BusinessLogicException(VACIO);
            }
            newEntity = persistence.update(credencialEntity);

        }
        return newEntity;

    }

}
