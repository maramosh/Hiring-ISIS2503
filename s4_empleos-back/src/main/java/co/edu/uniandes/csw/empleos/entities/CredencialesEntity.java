/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.entities;

import java.io.Serializable;
import javax.persistence.Entity;

/**
 *
 * @author je.hernandezr
 */
@Entity
public class CredencialesEntity extends BaseEntity implements  Serializable {

    private String correo;

    private String contrasenia;

    private String tipo;

    public CredencialesEntity() {
        //Constructor vacio
    }

    @Override
    public int hashCode() {
        return super.hashCode();
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
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @return the contrasena
     */

    public String getContrasenia() {
        return contrasenia;

    }

    /**
     * @param contrasenia
     */
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;

    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
