/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.dtos;

import co.edu.uniandes.csw.empleos.entities.CalificacionEntity;
import co.edu.uniandes.csw.empleos.entities.EstudianteEntity;
import co.edu.uniandes.csw.empleos.entities.OfertaEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author David Domínguez
 */
public class EstudianteDetailDTO extends EstudianteDTO implements Serializable {

    private List<CalificacionDTO> calificacioness;

    private List<OfertaDTO> ofertas;

    private CuentaBancariaDTO cuentaBancaria;

    public EstudianteDetailDTO() {
        //Constructor vacio
    }

    /**
     * Constructor para transformar un Entity a un DTO
     *
     * @param estudianteEntity La entidad del estudiante para transformar a DTO.
     */
    public EstudianteDetailDTO(EstudianteEntity estudianteEntity) {
        super(estudianteEntity);
        if (estudianteEntity != null) {
            if (estudianteEntity.getCalificaciones() != null) {
                calificacioness = new ArrayList<>();
                for (CalificacionEntity entityBook : estudianteEntity.getCalificaciones()) {
                    calificacioness.add(new CalificacionDTO(entityBook));
                }
            }

            if (estudianteEntity.getOfertas() != null) {
                ofertas = new ArrayList<>();
                for (OfertaEntity entityBook : estudianteEntity.getOfertas()) {
                    ofertas.add(new OfertaDTO(entityBook));
                }
            }

            if (estudianteEntity.getCuentaBancaria() != null) {
                cuentaBancaria = new CuentaBancariaDTO(estudianteEntity.getCuentaBancaria());
            }
        }
    }

    /**
     * Transformar un DTO a un Entity
     *
     * @return El DTO de la editorial para transformar a Entity
     */
    @Override
    public EstudianteEntity toEntity() {
        EstudianteEntity estudianteEntity = super.toEntity();
        if (calificacioness != null) {
            List<CalificacionEntity> calificacionesEntity = new ArrayList<>();
            for (CalificacionDTO dtoCalificacion : calificacioness) {
                calificacionesEntity.add(dtoCalificacion.toEntity());
            }
            estudianteEntity.setCalificaciones(calificacionesEntity);
        }
        if (ofertas != null) {
            List<OfertaEntity> ofertasEntity = new ArrayList<>();
            for (OfertaDTO dtoOferta : ofertas) {
                ofertasEntity.add(dtoOferta.toEntity());
            }
            estudianteEntity.setOfertas(ofertasEntity);
        }
        if (cuentaBancaria != null) {
            estudianteEntity.setCuentaBancaria(cuentaBancaria.toEntity());
        }
        return estudianteEntity;
    }

    /**
     * @return the calificioness
     */
    public List<CalificacionDTO> getCalificaciones() {
        return calificacioness;
    }

    /**
     * @param calificacioness the califics to set
     */
    public void setCalificaciones(List<CalificacionDTO> calificacioness) {
        this.calificacioness = calificacioness;
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
     * @return the cuenta bancaria
     */
    public CuentaBancariaDTO getCuentaBancaria() {
        return cuentaBancaria;
    }

    /**
     * @param cuentaBancaria the cuenta bancaria to set
     */
    public void setCuentaBancaria(CuentaBancariaDTO cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
