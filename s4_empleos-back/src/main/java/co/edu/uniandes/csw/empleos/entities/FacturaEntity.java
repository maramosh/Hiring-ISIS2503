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
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import uk.co.jemos.podam.common.PodamExclude;
import uk.co.jemos.podam.common.PodamStrategyValue;

/**
 *
 * @author Estudiante
 */
@Entity
public class FacturaEntity extends BaseEntity implements  Serializable{
    
    /**
     * La fecha que se le atribuirá a una factura. De esta manera se tiene un control por fechas
     */
    @Temporal(TemporalType.DATE)
    @PodamStrategyValue(DateStrategy.class)
    private Date fecha;
    
   @PodamExclude  
    @OneToOne(
        mappedBy = "factura",
    	fetch = FetchType.LAZY
    )
    private TrabajoEntity trabajo;
    
    /**
     * Valor que se registrará a una factura.
     */
    private Integer valor;
    
    /**
     * Constructor de la clase 
     */
    public FacturaEntity()
    {
       //Constructor vacío para evitar fallos en compilacion. Se asignan valores a los parámetros a través de los metodos set
    }

    /**
     * Este metodo da el valor de la fecha que está asociada a una factura
     * @return Fecha Un valor en un formato acordado. fecha != null && != fecha""
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Este metodo da el valor de la factura creada
     * @return Valor >0  
     */
    public Integer getValor() {
        return valor;
    }

    /**
     * Se asigna un valor nuevo a la fecha de la factura creada
     * @param fecha Nueva fecha por la cual se quiere modificar el valor de la actual.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Se asigna un nuevo valor a la factura creada-
     * @param valor valor > 0 que reemplazara el valor existente
     */
    public void setValor(Integer valor) {
        this.valor = valor;
    }

    /**
     * @return the trabajo
     */
    public TrabajoEntity getTrabajo() {
        return trabajo;
    }

    /**
     * @param trabajo the trabajo to set
     */
    public void setTrabajo(TrabajoEntity trabajo) {
        this.trabajo = trabajo;
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
