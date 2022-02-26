
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.entities;

import co.edu.uniandes.csw.empleos.podam.NumeroStringStrategy;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import uk.co.jemos.podam.common.PodamExclude;
import uk.co.jemos.podam.common.PodamStrategyValue;

/**
 * @author je.hernandezr
 */
@Entity
public class CuentaBancariaEntity extends BaseEntity implements  Serializable {

    @PodamStrategyValue(NumeroStringStrategy.class)
    private String numeroCuenta;

    private String nombreBanco;

    @PodamExclude
    @OneToOne(mappedBy = "cuentaBancaria", fetch = FetchType.LAZY)
    private EstudianteEntity estudiante;

    private String tipoCuenta;

    public CuentaBancariaEntity() {
        //Constructor vacío para evitar fallos en compilacion. Se asignan valores a los parámetros a través de los metodos set
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
    
    /**
     * @return the tipoCuenta
     */
    public String getTipoCuenta() {
        return tipoCuenta;
    }

    /**
     * @param pTipoCuenta the tipoCuenta to set
     */
    public void setTipoCuenta(String pTipoCuenta) {

        this.tipoCuenta=pTipoCuenta;

    }
    
      /**
     * @return the estudiante
     */
    public EstudianteEntity getEstudiante() {
        return estudiante;
    }

    /**
     * @param pEstudiante estudiante to set
     */
    public void setEstudiante(EstudianteEntity pEstudiante) {
        this.estudiante = pEstudiante;
    }

    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj);
    }
    
    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

}
