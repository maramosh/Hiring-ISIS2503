/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.dtos;

import co.edu.uniandes.csw.empleos.entities.OfertaEntity;
import java.io.Serializable;

/**
 *
 * @oferta Estudiante
 */
public class OfertaDTO implements Serializable {

    /**
     * Variable que representa los horarios de trabajo. Franjas
     * DDS:HH:MM-DDS:HH:MM donde DDS es el dia de la semana: LUN-lunes
     * MAR-martes MIE-miercoles JUE-jueves VIE-viernes
     */
    private String horario;

    /**
     * Variable que representa el tipo de oferta (Normal o Express).
     */
    private Integer tipoOferta;

    /**
     * Variable que representa el pago por hora de la oferta.
     */
    private Double pagoPorHora;

    /**
     * Variable que representa sel numero de vacantes de una oferta.
     * numeroDeVacantes>1
     */
    private Integer numeroDeVacantes;
    /**
     * Variable que representa el nombre de la oferta.
     */
    private String nombre;

    /**
     * Variable que si una oferta esta abierta al publico.
     *
     */
    private Boolean estaAbierta;

    /**
     * Variable que representa a categoria de empleo de la oferta.
     */
    private String categoria;

    /**
     * Variable que representa el porcentaje de pago extra de una oferta
     * Express.
     */
    private Integer porcentajePagoAdicional;

    /**
     * Variable que representa el tiempo maximo de aplicacion de una oferta
     * Express.
     */
    private Integer tiempoMaximoAplicacion;

    /**
     * Variable que representa los requisitos de un aplicante (Separados por un
     * guion (-)).
     *
     */
    private String requisitos;

    /**
     * Variable que representa la descripcion de la oferta.
     */
    private String descripcion;

    /**
     * Variable que representa las horas totales de trabajo, calculadas con el
     * horario.
     *
     */
    private Double horasDeTrabajo;

    /**
     * Token que definirá el inicio de sesión
     */
    private String token;

    /**
     * Variable que representa la ruta de la imagen principal de la oferta.
     *
     */
    private String rutaImagen;

    public OfertaDTO() {

    }

    /**
     * Convierte un objeto OfertaDTO a OfertaEntity.
     *
     * @return Nueva objeto OfertaEntity.
     *
     */
    public OfertaEntity toEntity() {
        OfertaEntity ofertaEntity = new OfertaEntity();
        ofertaEntity.setId(this.getId());
        ofertaEntity.setNombre(this.getNombre());
        ofertaEntity.setCategoria(this.getCategoria());
        ofertaEntity.setDescripcion(this.descripcion);
        ofertaEntity.setEstaAbierta(this.isEstaAbierta());
        ofertaEntity.setHorario(this.getHorario());
        ofertaEntity.setHorasDeTrabajo(this.getHorasDeTrabajo());
        ofertaEntity.setNumeroDeVacantes(this.getNumeroDeVacantes());
        ofertaEntity.setPagoPorHora(this.getPagoPorHora());
        ofertaEntity.setPorcentajePagoAdicional(this.getPorcentajePagoAdicional());
        ofertaEntity.setRequisitos(this.getRequisitos());
        ofertaEntity.setRutaImagen(this.getRutaImagen());
        ofertaEntity.setTiempoMaximoAplicacion(this.getTiempoMaximoAplicacion());
        ofertaEntity.setTipoOferta(this.getTipoOferta());
        return ofertaEntity;
    }

    /**
     * Crea un objeto AuthorDTO a partir de un objeto AuthorEntity.
     *
     * @param ofertaEntity Entidad AuthorEntity desde la cual se va a crear el
     * nuevo objeto.
     *
     */
    public OfertaDTO(OfertaEntity ofertaEntity) {
        if (ofertaEntity != null) {
            this.id = ofertaEntity.getId();
            this.nombre = ofertaEntity.getNombre();
            this.categoria = ofertaEntity.getCategoria();
            this.descripcion = ofertaEntity.getDescripcion();
            this.estaAbierta = ofertaEntity.getEstaAbierta();
            this.horario = ofertaEntity.getHorario();
            this.horasDeTrabajo = ofertaEntity.getHorasDeTrabajo();
            this.numeroDeVacantes = ofertaEntity.getNumeroDeVacantes();
            this.pagoPorHora = ofertaEntity.getPagoPorHora();
            this.porcentajePagoAdicional = ofertaEntity.getPorcentajePagoAdicional();
            this.requisitos = ofertaEntity.getRequisitos();
            this.rutaImagen = ofertaEntity.getRutaImagen();
            this.tiempoMaximoAplicacion = ofertaEntity.getTiempoMaximoAplicacion();
            this.tipoOferta = ofertaEntity.getTipoOferta();
            this.token = null;

        }
    }

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the tipoOferta
     */
    public Integer getTipoOferta() {
        return tipoOferta;
    }

    /**
     * @param tipoOferta the tipoOferta to set
     */
    public void setTipoOferta(Integer tipoOferta) {
        this.tipoOferta = tipoOferta;
    }

    /**
     * @param numeroDeVacantes the numeroDeVacantes to set
     */
    public void setNumeroDeVacantes(Integer numeroDeVacantes) {
        this.numeroDeVacantes = numeroDeVacantes;
    }

    /**
     * @return the pagoPorHora
     */
    public Double getPagoPorHora() {
        return pagoPorHora;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param requisitos the requisitos to set
     */
    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    /**
     * @return the horario
     */
    public String getHorario() {
        return horario;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the categoria
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * @return the tiempoMaximoAplicacion
     */
    public Integer getTiempoMaximoAplicacion() {
        return tiempoMaximoAplicacion;
    }

    /**
     * @param pagoPorHora the pagoPorHora to set
     */
    public void setPagoPorHora(Double pagoPorHora) {
        this.pagoPorHora = pagoPorHora;
    }

    /**
     * @param rutaImagen the rutaImagen to set
     */
    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    /**
     * @return the porcentajePagoAdicional
     */
    public Integer getPorcentajePagoAdicional() {
        return porcentajePagoAdicional;
    }

    /**
     * @param tiempoMaximoAplicacion the tiempoMaximoAplicacion to set
     */
    public void setTiempoMaximoAplicacion(Integer tiempoMaximoAplicacion) {
        this.tiempoMaximoAplicacion = tiempoMaximoAplicacion;
    }

    /**
     * @param porcentajePagoAdicional the porcentajePagoAdicional to set
     */
    public void setPorcentajePagoAdicional(Integer porcentajePagoAdicional) {
        this.porcentajePagoAdicional = porcentajePagoAdicional;
    }

    /**
     * @return the estaAbierta
     */
    public Boolean isEstaAbierta() {
        return estaAbierta;
    }

    /**
     * @param estaAbierta the estaAbierta to set
     */
    public void setEstaAbierta(Boolean estaAbierta) {
        this.estaAbierta = estaAbierta;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String id) {
        this.token = id;
    }

    /**
     * @return the requisitos
     */
    public String getRequisitos() {
        return requisitos;
    }

    /**
     * @param horario the horario to set
     */
    public void setHorario(String horario) {
        this.horario = horario;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @return the horasDeTrabajo
     */
    public Double getHorasDeTrabajo() {
        return horasDeTrabajo;
    }

    /**
     * @param horasDeTrabajo the horasDeTrabajo to set
     */
    public void setHorasDeTrabajo(Double horasDeTrabajo) {
        this.horasDeTrabajo = horasDeTrabajo;
    }

    /**
     * @return the rutaImagen
     */
    public String getRutaImagen() {
        return rutaImagen;
    }

    /**
     * @return the numeroDeVacantes
     */
    public Integer getNumeroDeVacantes() {
        return numeroDeVacantes;
    }

    /**
     * @param categoria the categoria to set
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

}
