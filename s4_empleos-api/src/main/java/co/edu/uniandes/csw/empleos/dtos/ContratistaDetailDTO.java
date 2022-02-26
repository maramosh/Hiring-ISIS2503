/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.dtos;

import co.edu.uniandes.csw.empleos.entities.ContratistaEntity;
import co.edu.uniandes.csw.empleos.entities.CuentaDeCobroEntity;
import co.edu.uniandes.csw.empleos.entities.OfertaEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author Contratista
 */
public class ContratistaDetailDTO extends ContratistaDTO implements Serializable {

    private TarjetaDeCreditoDTO tarjetaDeCredito;

    private List<CuentaDeCobroDTO> cuentaDeCobro;

    private List<OfertaDTO> ofertas;

    /**
     * Constructor vacio
     *
     */
    public ContratistaDetailDTO() {
        //Vacio
    }

    /**
     * Constructor para transformar un Entity a un DTO
     *
     * @param contratistaEntity La entidad del contratista para transformar a
     * DTO.
     */
    public ContratistaDetailDTO(ContratistaEntity contratistaEntity) {
        super(contratistaEntity);
        if (contratistaEntity != null) {
            if (contratistaEntity.getTarjetaCredito() != null) {
                tarjetaDeCredito = new TarjetaDeCreditoDTO(contratistaEntity.getTarjetaCredito());

            }

            if (contratistaEntity.getOfertas() != null) {
                ofertas = new ArrayList<>();
                for (OfertaEntity entityBook : contratistaEntity.getOfertas()) {
                    ofertas.add(new OfertaDTO(entityBook));
                }
            }

            if (contratistaEntity.getCuentasDeCobro() != null) {
                cuentaDeCobro = new ArrayList<>();
                for (CuentaDeCobroEntity entityBook : contratistaEntity.getCuentasDeCobro()) {
                    cuentaDeCobro.add(new CuentaDeCobroDTO(entityBook));
                }
            }
        }
    }

    /**
     * Transformar un DTO a un Entity
     *
     * @return El DTO de la editorial para transformar a Entity
     */
    @Override
    public ContratistaEntity toEntity() {
        ContratistaEntity contratistaEntity = super.toEntity();
        if (cuentaDeCobro != null) {
            List<CuentaDeCobroEntity> cuentaDeCobroEntity = new ArrayList<>();
            for (CuentaDeCobroDTO dtoCuentas : cuentaDeCobro) {
                cuentaDeCobroEntity.add(dtoCuentas.toEntity());
            }
            contratistaEntity.setCuentasDeCobro(cuentaDeCobroEntity);
        }
        if (ofertas != null) {
            List<OfertaEntity> ofertasEntity = new ArrayList<>();
            for (OfertaDTO dtoOferta : ofertas) {
                ofertasEntity.add(dtoOferta.toEntity());
            }
            contratistaEntity.setOfertas(ofertasEntity);
        }
        if (tarjetaDeCredito != null) {
            contratistaEntity.setTarjetaCredito(tarjetaDeCredito.toEntity());
        }
        return contratistaEntity;
    }

    /**
     * @return the ofertas
     */
    public List<OfertaDTO> getOfertas() {
        return ofertas;
    }

    /**
     * @param ofertas the ofertas to set
     */
    public void setOfertas(List<OfertaDTO> ofertas) {
        this.ofertas = ofertas;
    }

    /**
     * @return the tarjetaDeCredito
     */
    public TarjetaDeCreditoDTO getTarjetaDeCredito() {
        return tarjetaDeCredito;
    }

    /**
     * @param tarjetaDeCredito the tarjetaDeCredito to set
     */
    public void setTarjetaDeCredito(TarjetaDeCreditoDTO tarjetaDeCredito) {
        this.tarjetaDeCredito = tarjetaDeCredito;
    }

    /**
     * @return the cuentaDeCobro
     */

    public List<CuentaDeCobroDTO> getCuentasDeCobro() {
        return cuentaDeCobro;
    }

    /**
     * @param cuentaDeCobro the cuentaDeCobro to set
     */

    public void setCuentasDeCobro(List<CuentaDeCobroDTO> cuentaDeCobro) {
        this.cuentaDeCobro = cuentaDeCobro;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
   
    
}
