/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.dtos;

import co.edu.uniandes.csw.empleos.entities.CalificacionEntity;
import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author Estudiante
 */
public class CalificacionDTO implements Serializable{
    
    /**
     * id de la calificacion
    */
    private Long id;
    
    /**
     * nota asociada a la calificacion
    */
    private Double nota;
    
    /**
     * comentario asociado a la calificacion
    */
    private String comentario;
    
    /*
    * Relación a un estudiante  
    * dado que esta tiene cardinalidad 1.
     */
    private EstudianteDTO estudiante;
    
    
    /**
     * Token que definirá el inicio de sesión
     */
    private String token;

    public CalificacionDTO()
    {
        //Constructor vacio
    }
    
        /**
     * Constructor a partir de la entidad
     *
     * @param calificacionEntity La entidad del libro
     */
    public CalificacionDTO(CalificacionEntity calificacionEntity) {
        
        if (calificacionEntity != null) {
            this.id = calificacionEntity.getId();
            this.comentario = calificacionEntity.getComentario();
            this.nota = calificacionEntity.getNota();
            this.token=null;
       }
    }
    
    /**
     * Método para transformar el DTO a una entidad.
     *
     * @return La entidad del libro asociado.
     */
    public CalificacionEntity toEntity() {
        
        CalificacionEntity calificacionEntity = new CalificacionEntity();
        calificacionEntity.setId(this.id);
        calificacionEntity.setNota(this.getNota());
        calificacionEntity.setComentario(this.getComentario());      
        
        return calificacionEntity;
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
     * @return the nota
     */
    public Double getNota() {
        return nota;
    }

    /**
     * @param nota the nota to set
     */
    public void setNota(Double nota) {
        this.nota = nota;
    }

    /**
     * @return the comnetario
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * @param comentario the comnetario to set
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    /**
     * Devuelve el estudiante asociada a esta calificacion.
     *
     * @return the estudiante
     */
    public EstudianteDTO getEstudiante() {
        return estudiante;
    }

    /**
     * Modifica el estudiante asociado a esta calificacion.
     *
     * @param est the editorial to set
     */
    public void setEstudiante(EstudianteDTO est) {
        this.estudiante = est;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
    
}
