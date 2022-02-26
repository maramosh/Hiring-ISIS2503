/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.dtos;

import co.edu.uniandes.csw.empleos.entities.CredencialesEntity;
import java.io.Serializable;

/**
 *
 * @author David Dominguez
 */
public class CredencialDTO implements Serializable {

    private String correo;
    private String contrasenia;
    private String tipo;

    public CredencialDTO() {
        //Constructor vacío Token
    }

    public CredencialDTO(CredencialesEntity e) {
        if (e != null) {
            this.correo = e.getCorreo();
            this.contrasenia = e.getContrasenia();
            this.tipo = e.getTipo();

        }
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
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the contrasenia
     */
    public String getContrasenia() {
        return contrasenia;
    }

    /**
     * @param contrasenia the contrasenia to set
     */
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public CredencialesEntity toEntity() {
        CredencialesEntity e = new CredencialesEntity();
        e.setContrasenia(contrasenia);
        e.setTipo(tipo);
        e.setCorreo(correo);
        return e;
    }
}
