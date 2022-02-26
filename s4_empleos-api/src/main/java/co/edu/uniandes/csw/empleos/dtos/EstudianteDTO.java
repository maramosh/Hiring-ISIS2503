/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.dtos;

import co.edu.uniandes.csw.empleos.entities.EstudianteEntity;
import java.io.Serializable;

/**
 *
 * @author David Domínguez
 */
public class EstudianteDTO implements Serializable {

    private Long id;
    private Long idMedioDepago;
    private String nombre;
    private String carrera;
    private String correo;
    private Double calificacionPromedio;
    private Integer semestre;
    private String horarioDeTrabajo;
    private String token;

    /**
     * Constructor por defecto
     */
    public EstudianteDTO() {
        //Vacio
    }

    /**
     * Conviertir Entity a DTO (Crea un nuevo DTO con los valores que recibe en
     * la entidad que viene de argumento.
     *
     * @param estudianteEntity: Es la entidad que se va a convertir a DTO
     */
    public EstudianteDTO(EstudianteEntity estudianteEntity) {
        if (estudianteEntity != null) {
            this.id = estudianteEntity.getId();
            this.idMedioDepago = estudianteEntity.getIdMedioDepago();
            this.nombre = estudianteEntity.getNombre();
            this.carrera = estudianteEntity.getCarrera();
            this.calificacionPromedio = estudianteEntity.getCalificacionPromedio();
            this.semestre = estudianteEntity.getSemestre();
            this.horarioDeTrabajo = estudianteEntity.getHorarioDeTrabajo();
            this.idMedioDepago = estudianteEntity.getIdMedioDepago();
            this.correo = estudianteEntity.getCorreo();
            this.token = null;
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
     * @return the horarioDeTrabajo
     */
    public String getHorarioDeTrabajo() {
        return horarioDeTrabajo;
    }

    /**
     * @return the idMedioDepago
     */
    public Long getIdMedioDepago() {
        return idMedioDepago;
    }

    /**
     * @param id the idMedioDepago to set
     */
    public void setIdMedioDepago(Long idMedioDepago) {
        this.idMedioDepago = idMedioDepago;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

     /**
     * @param calificacionPromedio the calificacionPromedio to set
     */
    public void setCalificacionPromedio(Double calificacionPromedio) {
        this.calificacionPromedio = calificacionPromedio;
    }
    
    /**
     * @return the carrera
     */
    public String getCarrera() {
        return carrera;
    }

    /**
     * @param carrera the carrera to set
     */
    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    /**
     * @return the correo
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * @param correo the correo to set
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * @return the calificacionPromedio
     */
    public Double getCalificacionPromedio() {
        return calificacionPromedio;
    }

   

   

    /**
     * @param semestre the semestre to set
     */
    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

   

    /**
     * @param horarioDeTrabajo the horarioDeTrabajo to set
     */
    public void setHorarioDeTrabajo(String horarioDeTrabajo) {
        this.horarioDeTrabajo = horarioDeTrabajo;
    }

    /**
     * Convertir DTO a Entity
     *
     * @return Un Entity con los valores del DTO
     */
    public EstudianteEntity toEntity() {

        EstudianteEntity estudianteEntity = new EstudianteEntity();
        estudianteEntity.setId(this.id);
        estudianteEntity.setIdMedioDepago(this.idMedioDepago);
        estudianteEntity.setNombre(this.nombre);
        estudianteEntity.setCarrera(this.carrera);
        estudianteEntity.setCorreo(this.correo);
        estudianteEntity.setCalificacionPromedio(this.calificacionPromedio);
        estudianteEntity.setHorarioDeTrabajo(this.horarioDeTrabajo);
        estudianteEntity.setSemestre(this.semestre);
        estudianteEntity.setIdMedioDepago(this.idMedioDepago);
        return estudianteEntity;
    }
    
     /**
     * @return the semestre
     */
    public Integer getSemestre() {
        return semestre;
    }

}
