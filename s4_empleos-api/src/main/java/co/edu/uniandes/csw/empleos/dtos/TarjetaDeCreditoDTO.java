/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.dtos;

import co.edu.uniandes.csw.empleos.entities.TarjetaDeCreditoEntity;
import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author Estudiante
 */
public class TarjetaDeCreditoDTO implements Serializable {
    
    private Long id;
    private String numero;
    private String cvc;
    private String fecha;
    private String token;
    
    public TarjetaDeCreditoDTO()
    {
        //Constructor vacío para evitar fallos en compilacion. Se asignan valores a los parámetros a través de los metodos set
    }
    /**
     * Constructor a partir de la entidad
     *
     * @param tarjetaEntity La entidad del libro
     */
    public TarjetaDeCreditoDTO(TarjetaDeCreditoEntity tarjetaEntity) {
        
        if (tarjetaEntity != null) {
            this.id = tarjetaEntity.getId();
            this.numero = tarjetaEntity.getNumero();
            this.cvc = tarjetaEntity.getCVC();
            this.fecha = tarjetaEntity.getFecha();
            this.token = null;
        }
    }

    /**
     * Método para transformar el DTO a una entidad.
     *
     * @return La entidad del libro asociado.
     */
    public TarjetaDeCreditoEntity toEntity() {
        
        TarjetaDeCreditoEntity tarjetaEntity = new TarjetaDeCreditoEntity();
        tarjetaEntity.setId(this.id);
        tarjetaEntity.setNumero(this.numero);
        tarjetaEntity.setCVC(this.cvc);
        tarjetaEntity.setFecha(this.fecha);
        
        
        return tarjetaEntity;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String id) {
        this.token = id;
    }
    
    
}
