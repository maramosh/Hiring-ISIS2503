package co.edu.uniandes.csw.empleos.entities;


import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Clase que representa una oferta para poder ser guardado en la base de datos.
 *
 * @author je.berdugo10
 */
@Entity
public class OfertaEntity extends BaseEntity implements  Serializable{

    /**
     * Contratista que creo la oferta.
     */
    @PodamExclude
    @ManyToOne(cascade = CascadeType.PERSIST )
    private ContratistaEntity contratista;

    /**
     * Variable que representa el tipo de oferta (Normal o Express).
     */
    private Integer tipoOferta;

    /**
     * Variable que representa sel numero de vacantes de una oferta.
     * numeroDeVacantes>1
     */
    private Integer numeroDeVacantes;

    /**
     * Variable que representa el pago por hora de la oferta.
     */
    private Double pagoPorHora;

    /**
     * Variable que representa el nombre de la oferta.
     */
    private String nombre;

    /**
     * Variable que representa la descripcion de la oferta.
     */
    private String descripcion;

    /**
     * Variable que representa a categoria de empleo de la oferta.
     */
    private String categoria;

    /**
     * Variable que representa el tiempo maximo de aplicacion de una oferta
     * Express.
     */
    private Integer tiempoMaximoAplicacion;

    /**
     * Variable que representa el porcentaje de pago extra de una oferta
     * Express.
     */
    private Integer porcentajePagoAdicional;

    /**
     * Variable que si una oferta esta abierta al publico.
     *
     */
    private Boolean estaAbierta;

    /**
     * Variable que representa los requisitos de un aplicante (Separados por un
     * guion (-)).
     *
     */
    private String requisitos;

    /**
     * Variable que representa los horarios de trabajo. Franjas
     * DDS:HH:MM-DDS:HH:MM donde DDS es el dia de la semana: LUN-lunes
     * MAR-martes MIE-miercoles JUE-jueves VIE-viernes
     */
    private String horario;

    /**
     * Variable que representa las horas totales de trabajo, calculadas con el
     * horario.
     *
     */
    private Double horasDeTrabajo;

    /**
     * Variable que representa la ruta de la imagen principal de la oferta.
     *
     */
    private String rutaImagen;

    // Atributo que representa las ofertas a las que ha aplicado el estudiante 
    @PodamExclude
    @javax.persistence.ManyToMany(
            mappedBy = "ofertas",
            fetch = javax.persistence.FetchType.LAZY
    )
    private List<EstudianteEntity> estudiantes;

    @PodamExclude
    @OneToOne(
            mappedBy = "oferta",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    private TrabajoEntity trabajo;

    /**
     * Constructor de la oferta
     */
    public OfertaEntity() {
        //Constructor vacio.
    }

    /**
     * Devuelve el tipoDeOferta del contratista.
     *
     * @return 1 si es NORMAL o 2 si es EXPRESS
     */
    public Integer getTipoOferta() {
        return tipoOferta;
    }

    /**
     * Actualiza lel tipo de la oferta.
     *
     * @param tipoOferta ruta de la imagen a modificar
     */
    public void setTipoOferta(Integer tipoOferta) {
        this.tipoOferta = tipoOferta;
    }

    public ContratistaEntity getContratista() {
        return contratista;
    }

    public void setContratista(ContratistaEntity contratista) {
        this.contratista = contratista;
    }

    /**
     * Devuelve numero de vacantes disponibles en el momento.
     *
     * @return el numero de vanates disponibles.
     */
    public Integer getNumeroDeVacantes() {
        return numeroDeVacantes;
    }

    /**
     * Actualiza el numero de vacantes de la oferta.
     *
     * @param numeroDeVacantes numero de vacantes a modificar
     */
    public void setNumeroDeVacantes(Integer numeroDeVacantes) {
        this.numeroDeVacantes = numeroDeVacantes;
    }

    /**
     * Devuelve el pago por hora en pesos de la oferta.
     *
     * @return el pago por hora de la oferta.
     */
    public Double getPagoPorHora() {
        return pagoPorHora;
    }

    /**
     * Actualiza el pago por hora de la oferta.
     *
     * @param pagoPorHora pago por hora a modificar
     */
    public void setPagoPorHora(Double pagoPorHora) {
        this.pagoPorHora = pagoPorHora;
    }

    /**
     * Devuelve el nombre de la oferta.
     *
     * @return true si el contratista es externo y false si es Integererno.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Actualiza el nombre de la oferta.
     *
     * @param nombre ruta de la imagen a modificar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve la descripcion de la oferta.
     *
     * @return la descripcion de la oferta.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Actualiza la descripcion de la oferta.
     *
     * @param descripcion ruta de la imagen a modificar
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve la categoria de la oferta.
     *
     * @return la categoria de la oferta.
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Actualiza la categoria de la oferta.
     *
     * @param categoria ruta de la imagen a modificar
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Devuelve el tiempo maximo de aplicacion de una oferta Express.
     *
     * @return el tiempo maximo de aplicacion de una oferta Express.
     */
    public Integer getTiempoMaximoAplicacion() {
        return tiempoMaximoAplicacion;
    }

    /**
     * Actualiza el tiempo maximo de aplicacion de una oferta Express.
     *
     * @param tiempoMaximoAplicacion el tiempo maximo de aplicacion de una
     * oferta Express.
     */
    public void setTiempoMaximoAplicacion(Integer tiempoMaximoAplicacion) {
        this.tiempoMaximoAplicacion = tiempoMaximoAplicacion;
    }

    /**
     * Devuelve el porcentaje de pago adicional de una oferta Express.
     *
     * @return el tiempo maximo de aplicacion de una oferta Express.
     */
    public Integer getPorcentajePagoAdicional() {
        return porcentajePagoAdicional;
    }

    /**
     * Actualiza el porcentaje de pago adicional de una oferta Express.
     *
     * @param porcentajePagoAdicional el porcentaje de pago adicional de una
     * oferta Express.
     */
    public void setPorcentajePagoAdicional(Integer porcentajePagoAdicional) {
        this.porcentajePagoAdicional = porcentajePagoAdicional;
    }

    /**
     * Devuelve el estado de una oferta.
     *
     * @return estado de la oferta.
     */
    public Boolean getEstaAbierta() {
        return estaAbierta;
    }

    /**
     * Actualiza el estado de una oferta.
     *
     * @param estaAbierta estado a modificar
     */
    public void setEstaAbierta(Boolean estaAbierta) {
        this.estaAbierta = estaAbierta;
    }

    /**
     * Devuelve los requisitos de una oferta.
     *
     * @return requisitos de la oferta.
     */
    public String getRequisitos() {
        return requisitos;
    }

    /**
     * Actualiza los requisitos de una oferta.
     *
     * @param requisitos a modificar
     */
    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    /**
     * Devuelve el horario de una oferta.
     *
     * @return horario de la oferta.
     */
    public String getHorario() {
        return horario;
    }

    /**
     * Actualiza el horario de una oferta.
     *
     * @param horario a modificar
     */
    public void setHorario(String horario) {
        this.horario = horario;
    }

    /**
     * Devuelve el numero de horas de trabajo de una oferta.
     *
     * @return estado de la oferta.
     */
    public Double getHorasDeTrabajo() {
        return horasDeTrabajo;
    }

    /**
     * Actualiza las horas de trabajo de la oferta.
     *
     * @param horasDeTrabajo horas de trabajo a modificar
     */
    public void setHorasDeTrabajo(Double horasDeTrabajo) {
        this.horasDeTrabajo = horasDeTrabajo;
    }

    /**
     * Obtener la ruta de la imagen de la oferta.
     *
     * @return ruta de la imagen del contratista
     */
    public String getRutaImagen() {
        return rutaImagen;
    }

    /**
     * Actualiza la ruta de la imagen dela oferta.
     *
     * @param rutaImagen ruta de la imagen a modificar
     */
    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    /**
     * @return the trabajos
     */
    public TrabajoEntity getTrabajo() {
        return trabajo;
    }

    /**
     * @param ptrabajo the trabajos to set
     */
    public void setTrabajo(TrabajoEntity ptrabajo) {
        this.trabajo = ptrabajo;
    }

    /**
     * @return the estudiantes
     */
    public List<EstudianteEntity> getEstudiantes() {
        return estudiantes;
    }

    /**
     * @param estudiantes the estudiantes to set
     */
    public void setEstudiantes(List<EstudianteEntity> estudiantes) {
        this.estudiantes = estudiantes;
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
