package co.edu.uniandes.csw.empleos.entities;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Esta clase modela la entidad Estudiante.
 *
 * @author David Dominguez
 */
@Entity
public class EstudianteEntity extends BaseEntity implements Serializable {

    // Atributo que representa el nombre del estudiante
    private String nombre;
    // Atributo que representa el ID del medio de pago del estudiante
    private Long idMedioDepago;
    // Atributo que representa la carrera que el estudiante cursa
    private String carrera;
    // Atributo que representa el correo del estudiante
    private String correo;
    // Atributo que representa la calificación promedio del estudiante en lso trabajos que ha hecho
    private Double calificacionPromedio;
    // Atributo que representa el horario de trabajo disponible del estudiante
    private String horarioDeTrabajo;
    // Atributo que representa el semestre que cursa el estudiante

    private Integer semestre;

    // Atributo que representa las ofertas a las que ha aplicado el estudiante 
    @PodamExclude
    @javax.persistence.ManyToMany(
            fetch = javax.persistence.FetchType.LAZY
    )
    private List<OfertaEntity> ofertas;

    // Atributo que representa las calificaciones que tiene el estudiante 

    @PodamExclude
    @OneToMany(mappedBy = "estudiante")
    private List<CalificacionEntity> calificaciones = new ArrayList<>();


    // Atributo que representa la cuenta bancaria que tiene el estudiante
    @PodamExclude
    @javax.persistence.OneToOne( cascade = CascadeType.PERSIST ,fetch = FetchType.LAZY)
    private CuentaBancariaEntity cuentaBancaria;

  

    // Constructor vacío
    public EstudianteEntity() {
        //Constructor vacío para evitar fallos en compilacion. Se asignan valores a los parámetros a través de los metodos set
    }

    /**
     * @return el nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the idMedioDepago
     */
    public Long getIdMedioDepago() {
        return this.idMedioDepago;
    }

    /**
     * @param idMedioDepago the idMedioDepago to set
     */
    public void setIdMedioDepago(Long idMedioDepago) {
        this.idMedioDepago = idMedioDepago;
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
        return this.calificacionPromedio;
    }

    /**
     * @param calificacionPromedio the calificacionPromedio to set
     */
    public void setCalificacionPromedio(Double calificacionPromedio) {
        this.calificacionPromedio = calificacionPromedio;
    }

    /**
     * @return the horarioDeTrabajo
     */
    public String getHorarioDeTrabajo() {
        return horarioDeTrabajo;
    }

    /**
     * @param horarioDeTrabajo the horarioDeTrabajo to set
     */
    public void setHorarioDeTrabajo(String horarioDeTrabajo) {
        this.horarioDeTrabajo = horarioDeTrabajo;
    }

    /**
     * @return the semestre
     */
    public Integer getSemestre() {
        return this.semestre;
    }

    /**
     * @param semestre the semestre to set
     */
    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    /**
     * @return the ofertas
     */
    public List<OfertaEntity> getOfertas() {
        return ofertas;
    }

    /**
     * @param ofertas the ofertas to set
     */
    public void setOfertas(List<OfertaEntity> ofertas) {
        this.ofertas = ofertas;
    }

    /**
     * @return the calificaciones
     */
    public List<CalificacionEntity> getCalificaciones() {
        return calificaciones;
    }

    /**
     * @param calificaciones the calificaciones to set
     */
    public void setCalificaciones(List<CalificacionEntity> calificaciones) {
        this.calificaciones = calificaciones;
    }

    /**
     * @return the cuentaBancaria
     */
    public CuentaBancariaEntity getCuentaBancaria() {
        return cuentaBancaria;
    }

    /**
     * @param cuentaBancaria the cuentaBancaria to set
     */
    public void setCuentaBancaria(CuentaBancariaEntity cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj);
    }
    
    @Override
    public int hashCode()
    {
        return super.hashCode();
    }
}
