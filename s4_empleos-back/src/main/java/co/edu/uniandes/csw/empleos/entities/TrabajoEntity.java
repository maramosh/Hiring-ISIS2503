package co.edu.uniandes.csw.empleos.entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Esta clase modela la entidad de trabajo, que será guardada en la base de datos.
 * @author David Dominguez
 */
@Entity
public class TrabajoEntity extends BaseEntity implements Serializable {
    
    //Atributo que representa si un trabajo ya fue terminado
    private Boolean cumplido;
    //Atributo que representa si un trabajo ya ha sido "aprobado" por el contratista
    private Boolean verificado;

    @PodamExclude
    @OneToOne(cascade = CascadeType.PERSIST)
    private FacturaEntity factura;   
   
    @PodamExclude
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private OfertaEntity oferta;
    
    //Constructor vacío.
    public TrabajoEntity() {
        //Constructor vacío para evitar fallos en compilacion. Se asignan valores a los parámetros a través de los metodos set
    }

    /**
     * @return the cumplido
     */
    public Boolean isCumplido() {
        return cumplido;
    }

    /**
     * @param cumplido the cumplido to set
     */
    public void setCumplido(Boolean cumplido) {
        this.cumplido = cumplido;
    }

    /**
     * @return the verificado
     */
    public Boolean isVerificado() {
        return verificado;
    }

    /**
     * @param verificado the verificado to set
     */
    public void setVerificado(Boolean verificado) {
        this.verificado = verificado;
    }

    /**
     * @return the factura
     */
    public FacturaEntity getFactura() {
        return factura;
    }

    /**
     * @param factura the factura to set
     */
    public void setFactura(FacturaEntity factura) {
        this.factura = factura;
    }

    /**
     * @return the oferta
     */
    public OfertaEntity getOferta() {
        return oferta;
    }

    /**
     * @param oferta the oferta to set
     */
    public void setOferta(OfertaEntity oferta) {
        this.oferta = oferta;
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