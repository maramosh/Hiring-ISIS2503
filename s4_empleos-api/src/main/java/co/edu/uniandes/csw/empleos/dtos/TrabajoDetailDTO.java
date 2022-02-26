/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.dtos;

import co.edu.uniandes.csw.empleos.entities.TrabajoEntity;
import java.io.Serializable;

/**
 *
 * @author David Domínguez
 */
public class TrabajoDetailDTO extends TrabajoDTO implements Serializable {
   
    private FacturaDTO factura;
    private OfertaDTO oferta;
    
    public TrabajoDetailDTO()
    {
        //Constructor vacio
    }

    /**
     * Constructor para transformar un Entity a un DTO
     *
     * @param trabajoEntity La entidad del trabajo para transformar a DTO.
     */
    public TrabajoDetailDTO(TrabajoEntity trabajoEntity) {
        super(trabajoEntity);
        if (trabajoEntity != null) {
            if (trabajoEntity.getOferta() != null)
                oferta = new OfertaDTO(trabajoEntity.getOferta());
            
            if (trabajoEntity.getFactura() != null)
                factura = new FacturaDTO(trabajoEntity.getFactura());
        }
    }
    
    
        /**
     * Transformar un DTO a un Entity
     *
     * @return El DTO de trabajo para transformar a Entity
     */
    @Override
    public TrabajoEntity toEntity() {
        TrabajoEntity trabajoEntity = super.toEntity();
        if (factura != null) trabajoEntity.setFactura(factura.toEntity());
        if (oferta != null) trabajoEntity.setOferta(oferta.toEntity());
        return trabajoEntity;
    }

    /**
     * @return the factura
     */
    public FacturaDTO getFactura() {
        return factura;
    }

    /**
     * @param factura the factura to set
     */
    public void setFactura(FacturaDTO factura) {
        this.factura = factura;
    }
    
    /**
     * @return the oferta
     */
    public OfertaDTO getOferta() {
        return oferta;
    }

    /**
     * @param oferta the oferta to set
     */
    public void setFactura(OfertaDTO oferta) {
        this.oferta = oferta;
    }
}
