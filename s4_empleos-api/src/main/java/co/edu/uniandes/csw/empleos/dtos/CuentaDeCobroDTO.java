package co.edu.uniandes.csw.empleos.dtos;

import co.edu.uniandes.csw.empleos.entities.CuentaDeCobroEntity;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Jonatan Hernandez
 */
public class CuentaDeCobroDTO implements Serializable {

    /**
     * id de la cuenta de cobro
     */
    private Long id;

    /**
     * número de la cuenta de cobro
     */
    private Integer numeroCuentaDeCobro;

    /**
     * fecha de la cuenta de cobro
     */
    private Date fecha;

    /**
     * Valor que se debe.
     */
    private Integer valor;

    /**
     * Nombre del nombreEstudiante a quien se le debe el valor
     */
    private String nombreEstudiante;

    /**
     * Concepto
     */
    private String concepto;

    /**
     * la asociacion con contratista
     */
    private ContratistaDTO contratista;

    /**
     * Token que definirá el inicio de sesión
     */
    private String token;

    /**
     * Constructor vacio
     */
    public CuentaDeCobroDTO() {
        //Vacio
    }

    public CuentaDeCobroDTO(CuentaDeCobroEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.concepto = entity.getConcepto();
            this.fecha = entity.getFecha();
            this.nombreEstudiante = entity.getNombreEstudiante();
            this.numeroCuentaDeCobro = entity.getNumeroCuentaDeCobro();
            this.valor = entity.getValor();
            this.token = null;
            if (entity.getContratista() != null) {
                this.contratista = new ContratistaDTO(entity.getContratista());
            } else {
                this.contratista = null;
            }
        }
    }

    /**
     * Convertir DTO a Entity
     *
     * @return Un Entity con los valores del DTO
     */
    public CuentaDeCobroEntity toEntity() {

        CuentaDeCobroEntity cuentaDeCobroEntity = new CuentaDeCobroEntity();
        cuentaDeCobroEntity.setId(this.id);
        cuentaDeCobroEntity.setConcepto(this.concepto);
        cuentaDeCobroEntity.setNumeroCuentaDeCobro(this.numeroCuentaDeCobro);
        cuentaDeCobroEntity.setFecha(this.fecha);
        cuentaDeCobroEntity.setNombreEstudiante(this.nombreEstudiante);
        cuentaDeCobroEntity.setValor(this.valor);
       
            cuentaDeCobroEntity.setContratista(this.contratista.toEntity());
      

        return cuentaDeCobroEntity;
    }

    //--------------------------------------------------------------------------------------------
    //Getters && Setters
    //--------------------------------------------------------------------------------------------
    public String getToken() {
        return token;
    }

    public void setNumeroCuentaDeCobro(Integer numeroCuentaDeCobro) {
        this.numeroCuentaDeCobro = numeroCuentaDeCobro;
    }

    public void setToken(String id) {
        this.token = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroCuentaDeCobro() {
        return numeroCuentaDeCobro;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    /**
     * @return the contratista
     */
    public ContratistaDTO getContratista() {
        return contratista;
    }

    /**
     * @param contratista the contratista to set
     */
    public void setContratista(ContratistaDTO contratista) {
        this.contratista = contratista;
    }
}
