/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import uk.co.jemos.podam.common.PodamExclude;

/**
 *
 * @author Estudiante
 */
@Entity
public class CalificacionEntity extends BaseEntity implements  Serializable{

    /**
     * Atributo de la nota que será puesta a un estudiante.
     */
    private Double nota;

    /**
     * Atributo que será el comentario a la calificacion puesta.
     */
    private String comentario;

    @PodamExclude
    @ManyToOne(fetch = FetchType.LAZY)
    private EstudianteEntity estudiante;

    /**
     * Contrctor de la Clase Calificación para inicualizar los atributos
     */
    public CalificacionEntity() {
        //Constructor vacío para evitar fallos en compilacion. Se asignan valores a los parámetros a través de los metodos set
    }

    /**
     * Metodo que da el resultado del comentario.
     *
     * @return Comentario asociado a la nota asignada. return != null
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * Metodo que retorna la nota asociada a un estudainte
     *
     * @return Nota en un valor entre 0.0 y 5.0
     */
    public Double getNota() {
        return nota;
    }

    /**
     * Metodo que retorna al estudiante correspondiente
     *
     * @return Estudiante al que le pertenece la calificai
     */
    public EstudianteEntity getEstudiante() {
        return estudiante;
    }

    /**
     * Metodo que permite modificar el comentario asignado a la nota.
     *
     * @param comentario Es el nuevo mensaje por el cual se va a reemplzar el
     * atributo inicializado previamente.
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    /**
     * Metodo que permite modificar el valor de la nota inicializado
     * previamente.
     *
     * @param nota Nota en un valor entre 0.0 y 5.0
     */
    public void setNota(Double nota) {
        this.nota = nota;
    }

    /**
     * Metodo que permite modicar el estudainte al cual se le asigno la
     * calificación
     *
     * @param e Estudiante a asiganr la calificacion
     */
    public void setEstudiante(EstudianteEntity e) {
        this.estudiante = e;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
