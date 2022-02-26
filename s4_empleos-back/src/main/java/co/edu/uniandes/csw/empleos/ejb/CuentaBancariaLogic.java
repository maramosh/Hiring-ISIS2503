/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.ejb;

import co.edu.uniandes.csw.empleos.entities.CuentaBancariaEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.CuentaBancariaPersistence;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author je.hernandezr
 */
@Stateless
public class CuentaBancariaLogic {

    @Inject
    private CuentaBancariaPersistence persistence;

    /**
     * Se encarga de crear un Author en la base de datos.
     *
     * @param cuentaBancaria Objeto de CuentaBancariaEntity con los datos nuevos
     * @return Objeto de CuentaBancariaEntity con los datos nuevos y su ID.
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    public CuentaBancariaEntity createCuentaBancaria(CuentaBancariaEntity cuentaBancaria) throws BusinessLogicException {
        if (cuentaBancaria.getNumeroCuenta() == null) {
            throw new BusinessLogicException("El numero de cuenta está vacío");
        }

        if (cuentaBancaria.getNumeroCuenta().contains(",") || cuentaBancaria.getNumeroCuenta().contains(".") || cuentaBancaria.getNumeroCuenta().contains("-")) {
            throw new BusinessLogicException("El numero de cuenta no puede contener caracteres diferentes  a un numero entero.");
        } else if (cuentaBancaria.getNumeroCuenta().length() < 9 && cuentaBancaria.getNumeroCuenta().length() > 20) {
            throw new BusinessLogicException("El numero de cuenta no cumple con la longitud de una cuenta");
        } else if (Long.parseLong(cuentaBancaria.getNumeroCuenta()) < 0) {
            throw new BusinessLogicException("El numero de cuenta no puede ser negativo");

        }
        CuentaBancariaEntity ent = persistence.findByNumero(cuentaBancaria.getNumeroCuenta());
        if (ent != null) {
            throw new BusinessLogicException("Ya existe una cuenta Bancaria con ese numero de cuenta ");
        } else if (cuentaBancaria.getNombreBanco() == null) {
            throw new BusinessLogicException("el nombre de banco no puede ser null");
        } else if (cuentaBancaria.getNombreBanco().equals("")) {
            throw new BusinessLogicException("el nombre de banco no puede ser vacío");
        }
        if (!((cuentaBancaria.getTipoCuenta().equals("Ahorros")) || (cuentaBancaria.getTipoCuenta().equals("Corriente")))) {
            throw new BusinessLogicException("El tipo de cuenta debe ser Ahorros o Corriente");
        }

        cuentaBancaria = persistence.create(cuentaBancaria);
        return cuentaBancaria;

    }

    /**
     * Obtiene la lista de los registros de CuentaBancaria.
     *
     * @return Colección de objetos de CuentaBancariaEntity.
     */
    public List<CuentaBancariaEntity> getCuentasBancarias() {

        return persistence.findAll();
    }

    /**
     * Obtiene los datos de una instancia de CuentaBancaria a partir de su ID.
     *
     * @param cuentabancariaId Identificador de la instancia a consultar
     * @return Instancia de CuentaBancariaEntity con los datos de la
     * CuentaBancaria consultada.
     */
    public CuentaBancariaEntity getCuentaBancaria(Long cuentabancariaId) throws BusinessLogicException {

        CuentaBancariaEntity cuentabancariaEntity = persistence.find(cuentabancariaId);
        if (cuentabancariaEntity == null) {
            throw new BusinessLogicException("No existe una cuentabacaria Con esa id");
        }

        return cuentabancariaEntity;
    }

    /**
     * Actualiza la información de una instancia de Author.
     *
     * @param cuentaId
     * @param cuentaBancoEntity
     * @return Instancia de AuthorEntity con los datos actualizados.
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    public CuentaBancariaEntity updateCuentaBancaria(Long cuentaId, CuentaBancariaEntity cuentaBancoEntity) throws BusinessLogicException {

        if (persistence.find(cuentaId) == null) {
            throw new BusinessLogicException("No existe la cuenta a ser actualizada");
        }
        if (cuentaBancoEntity.getNumeroCuenta() == null) {
            throw new BusinessLogicException("El numero de cuenta está vacío");
        }

        if (cuentaBancoEntity.getNumeroCuenta().contains(".")) {
            throw new BusinessLogicException("El numero de cuenta no puede contener puntos.");
        }
        if (cuentaBancoEntity.getNumeroCuenta().length() < 9 || cuentaBancoEntity.getNumeroCuenta().length() > 20) {
            throw new BusinessLogicException("El numero de cuenta no cumple con la longitud de una cuenta");
        }
        if (cuentaBancoEntity.getNumeroCuenta().contains(",") || cuentaBancoEntity.getNumeroCuenta().contains("-")) {
            throw new BusinessLogicException("El numero de cuenta no puede contener caracteres diferentes  a un numero entero.");
        }
        if (Long.parseLong(cuentaBancoEntity.getNumeroCuenta()) < 0) {
            throw new BusinessLogicException("El numero de cuenta no puede ser negativo");
        }
        if (!((cuentaBancoEntity.getTipoCuenta().equals("Ahorros")) || (cuentaBancoEntity.getTipoCuenta().equals("Corriente")))) {
            throw new BusinessLogicException("El tipo de cuenta debe ser Ahorros o Corriente");
        }
        if (cuentaBancoEntity.getNombreBanco() == null) {
            throw new BusinessLogicException("el nombre de banco no puede ser null");
        }
        if (cuentaBancoEntity.getNombreBanco().equals("")) {
            throw new BusinessLogicException("el nombre de banco no puede ser vacío");
        }

        return persistence.update(cuentaBancoEntity);
    }

    /**
     *
     * Borra una cuenta bancaria de la base de datos recibiendo como argumento
     * el id del premio
     *
     * @param estudianteId
     * @param cuentaId
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    public void delete(Long cuentaId) {

        persistence.delete(cuentaId);
    }

}
