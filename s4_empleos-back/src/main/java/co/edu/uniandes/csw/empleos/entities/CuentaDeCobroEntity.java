/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.entities;

import co.edu.uniandes.csw.empleos.podam.DateStrategy;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import uk.co.jemos.podam.common.PodamExclude;
import uk.co.jemos.podam.common.PodamLongValue;
import uk.co.jemos.podam.common.PodamStrategyValue;

/**
 *
 * @author Santiago Tangarife Rincón
 */
@Entity
public class CuentaDeCobroEntity extends BaseEntity implements Serializable {

    /**
     * numeroCuentaDeCobro
     */
    @PodamLongValue(minValue = 1)
    private Integer numeroCuentaDeCobro;


    /**
    *Contratista de la cuenta de cobro
    */
    @PodamExclude
    @ManyToOne
    private ContratistaEntity contratista;

    /**
    *fecha de la cuenta de cobro
     */
    @Temporal(TemporalType.DATE)
    @PodamStrategyValue(DateStrategy.class)
    private Date fecha;

    /**
     * Valor que de debe.
     */
    @PodamLongValue(minValue = 1)
    private Integer valor;
    
    /**
     * Nombre del nombreEstudiante a quien se le debe el valor
     */
    private String nombreEstudiante;
    
    /**
     *Concepto  
     */
    private String concepto;
    
    /**
     * Constructor
     */
    public CuentaDeCobroEntity() {
       //Constructor vacío para evitar fallos en compilacion. Se asignan valores a los parámetros a través de los metodos set
    }

    //-------------------------------------------------
    //GETTERS & SETTERS
    //-------------------------------------------------
    
    /**
     * @param contratista the contratista to set
     */
    public void setContratista(ContratistaEntity contratista) {
        this.contratista = contratista;
    }
    
    /**
     *
     * @return numeroCuentaDeCobro de la tarjeta
     */
    public Integer getNumeroCuentaDeCobro() {
        return numeroCuentaDeCobro;
    }

     @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj);
    }
    
    

    /**
     * @param fecha the Fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return el valor de la cuenta de cobro 
     */
    public Integer getValor() {
        return valor;
    }

    /**
     * @return nombreEstudiante de la cuenta de cobro
     */
    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    /**
     * @return concepto de la cuenta de cobro
     */
    public String getConcepto() {
        return concepto;
    }

    /**
     * @param valor de la cuenta de cobro
     */
    public void setValor(Integer valor) {
        this.valor = valor;
    }

    /**
     * @param nombreEstudiante de la cuenta de cobro
     */
    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    /**
     * @param concepto de la cuenta de cobro
     */
    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    /**
     * @return the contratista
     */
    public ContratistaEntity getContratista() {
        return contratista;
    }
/**
     * @return the Fecha
     */
    public Date getFecha() {
        return fecha;
    }
    

   
    
    @Override
    public int hashCode()
    {
        return super.hashCode();
    }
    
    /**
     * cambia el numeroCuentaDeCobro de la tarjeta al ingresado por parametro
     *
     * @param numeroCuentaDeCobro nuevo numeroCuentaDeCobro de la tarjeta
     */
    public void setNumeroCuentaDeCobro(Integer numeroCuentaDeCobro) {
        this.numeroCuentaDeCobro = numeroCuentaDeCobro;
    }
    
}
