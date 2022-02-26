/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.dtos;

import co.edu.uniandes.csw.empleos.entities.CuentaBancariaEntity;
import java.io.Serializable;

/**
 *
 * @author je.hernandezr
 */
public class CuentaBancariaDTO implements Serializable {
    


    private Long id;

    private String numeroCuenta;

    private String nombreBanco;

    private EstudianteDTO estudiante;
    
    private String tipoCuenta;

    private String token;
    
    public CuentaBancariaDTO() {
    }

    public CuentaBancariaDTO(CuentaBancariaEntity cuentaBancariaEntity) {
        if (cuentaBancariaEntity != null) {
            this.id = cuentaBancariaEntity.getId();
            this.nombreBanco = cuentaBancariaEntity.getNombreBanco();
            this.numeroCuenta = cuentaBancariaEntity.getNumeroCuenta();
            this.token=null;
            this.setTipoCuenta(cuentaBancariaEntity.getTipoCuenta());
            this.estudiante= new EstudianteDTO(cuentaBancariaEntity.getEstudiante());
        }
    }

    public String getToken() {
        return token;
    }
    
    public void setToken(String id) {
        this.token = id;
    }
    
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the numeroCuenta
     */
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    /**
     * @param numeroCuenta the numeroCuenta to set
     */
    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    /**
     * @return the nombreBanco
     */
    public String getNombreBanco() {
        return nombreBanco;
    }

    /**
     * @param nombreBanco the nombreBanco to set
     */
    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }

   

    public CuentaBancariaEntity toEntity() {
        CuentaBancariaEntity cuentaBancaria = new CuentaBancariaEntity();
        cuentaBancaria.setId(this.getId());
        cuentaBancaria.setNombreBanco(this.getNombreBanco());
        cuentaBancaria.setNumeroCuenta(this.getNumeroCuenta());
       cuentaBancaria.setTipoCuenta(this.getTipoCuenta());
        if (this.getEstudiante() != null) {
            cuentaBancaria.setEstudiante(this.getEstudiante().toEntity());
        }

        return cuentaBancaria;
    }

    /**
     * @return the estudiante
     */
    public EstudianteDTO getEstudiante() {
        return estudiante;
    }

    /**
     * @param estudiante the estudiante to set
     */
    public void setEstudiante(EstudianteDTO estudiante) {
        this.estudiante = estudiante;
    }

    /**
     * @return the tipoCuenta
     */
    public String getTipoCuenta() {
        return tipoCuenta;
    }

    /**
     * @param tipoCuenta the tipoCuenta to set
     */
    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }
}
