/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.dtos;

import java.io.Serializable;

/**
 *
 * @author David Dominguez
 */
public class UsuarioDTO implements Serializable {

    private Long id;
    private Boolean esExterno;
    private String nombre;
    private String email;
    private String rutaImagen;
    private Long idMedioDepago;
    private String carrera;
    private Double calificacionPromedio;
    private Integer semestre;
    private String horarioDeTrabajo;

    public UsuarioDTO() {
        //Constructor vacio
    }
 public Integer getSemestre() {
        return semestre;
    }

 
    public Long getId() {
        return id;
    }

   
    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public Boolean getEsExterno() {
        return esExterno;
    }

    public void setIdMedioDepago(Long idMedioDepago) {
        this.idMedioDepago = idMedioDepago;
    }

    public String getNombre() {
        return nombre;
    }

    public String getHorarioDeTrabajo() {
        return horarioDeTrabajo;
    }

    public String getEmail() {
        return email;
    }

    

    public String getRutaImagen() {
        return rutaImagen;
    }


    public Long getIdMedioDepago() {
        return idMedioDepago;
    }
    
public void setNombre(String nombre) {
        this.nombre = nombre;
    }
   

    public String getCarrera() {
        return carrera;
    }
     public void setEsExterno(Boolean esExterno) {
        this.esExterno = esExterno;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public Double getCalificacionPromedio() {
        return calificacionPromedio;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public void setCalificacionPromedio(Double calificacionPromedio) {
        this.calificacionPromedio = calificacionPromedio;
    }
     public void setId(Long id) {
        this.id = id;
    }

   
    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    

    public void setHorarioDeTrabajo(String horarioDeTrabajo) {
        this.horarioDeTrabajo = horarioDeTrabajo;
    }
}
