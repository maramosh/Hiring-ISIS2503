/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.dtos;

import co.edu.uniandes.csw.empleos.entities.ContratistaEntity;
import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author Contratista
 */
public class ContratistaDTO implements Serializable{
    
     /**
     * Variable que representa la id del contratista.
     */
    private Long id;
    /**
     * Variable que representa si el contratista es externo.
     */
    private Boolean esExterno;
    
    /**
     * Variable que representa el nombre del contratista.
     */
    private String nombre;
    
    /**
     * Variable que representa el nombre del contratista.
     */
    private String email;
    
    
    
  
     /**
     * Variable que representa la ruta de la imagen del contratista.
     */
    private String rutaImagen;
    
     /**
     * Token que definirá el inicio de sesión
     */
    private String token;
    
    
    public ContratistaDTO(){
        
    }
     /**
     * Conviertir Entity a DTO (Crea un nuevo DTO con los valores que recibe en
     * la entidad que viene de argumento.
     *
     * @param contratistaEntity: Es la entidad que se va a convertir a DTO
     */
    public ContratistaDTO(ContratistaEntity contratistaEntity) {
        if (contratistaEntity != null) {
            this.id = contratistaEntity.getId();
            this.nombre = contratistaEntity.getNombre();
            this.esExterno= contratistaEntity.getEsExterno();
            this.email = contratistaEntity.getEmail();
            this.rutaImagen = contratistaEntity.getRutaImagen();
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
     * @return the esExterno
     */
    public Boolean esExterno() {
        return esExterno;
    }

   
 /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

   

   
    /**
     * @param rutaImagen the rutaImagen to set
     */
    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }
    
    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
     /**
     * @param esExterno the esExterno to set
     */
    public void setEsExterno(Boolean esExterno) {
        this.esExterno = esExterno;
    }
    
     
    /**
     * @return the rutaImagen
     */
    public String getRutaImagen() {
        return rutaImagen;
    }

    /**
     * Convertir DTO a Entity
     *
     * @return Un Entity con los valores del DTO
     */
    public ContratistaEntity toEntity() {
    
        ContratistaEntity contratistaEntity = new ContratistaEntity();
        contratistaEntity.setId(this.id);
        contratistaEntity.setNombre(this.nombre);
        contratistaEntity.setEmail(this.email);
        contratistaEntity.setRutaImagen(this.rutaImagen);
        contratistaEntity.setEsExterno(this.esExterno);
        return contratistaEntity;
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
    
      @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
    
}
