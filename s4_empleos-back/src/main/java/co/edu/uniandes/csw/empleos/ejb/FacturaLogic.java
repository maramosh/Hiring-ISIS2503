/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.ejb;

import co.edu.uniandes.csw.empleos.entities.FacturaEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.FacturaPersistence;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Nicolas Munar
 */
@Stateless
public class FacturaLogic {
    
    @Inject
    private FacturaPersistence persistence;
    
    /**
     * guarada una nueva factura
     * @param factura la Entidad de tipo factura a persistir
     * @return La entidaad luego de persistirla
     * @throws BusinessLogicException Si Alguna regla de negocio se incumple
     */
    public FacturaEntity createFactura(FacturaEntity factura) throws BusinessLogicException{
        
        
        if(factura.getValor()<0)
        {
            throw new BusinessLogicException ("El valor de la factura no puede tener valores negativos");
        }
        if(factura.getFecha()==null)
        {
            throw new BusinessLogicException("La fecha no puede ser una valor nulo");
        }

        factura = persistence.create(factura);
        return factura;
    }
    
    /**
     * Devuelve todas las facturas que hay en la base de datos.
     * @return  Lista de las entidades del tipo factura.
     */
    public List<FacturaEntity> getFacturas()
    {
       
       return persistence.findAll();
    }
    
    /**
     * Busca Una factura por ID
     * @param facturaId El id de la factura a buscar.
     * @return La factura encontrada, null si no se encuentra.
     */
    public FacturaEntity getFactura(Long facturaId)
    {
        return  persistence.find(facturaId);
    }
    
     /**
     * Actualizar una factura por ID
     *
     * @param facturaId El ID de la facura a actualizar
     * @param facturaEntity La entidad de la factura con los cambios deseados
     * @return La entidad de la factura luego de actualizarla
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException
     */
    public FacturaEntity updateFactura(Long facturaId, FacturaEntity facturaEntity)throws BusinessLogicException {
        
        
       if(facturaEntity.getValor()<0)
        {
            throw new BusinessLogicException ("El valor de la factura no puede tener valores negativos");
        }
        if(facturaEntity.getFecha()==null)
        {
            throw new BusinessLogicException("La fecha no puede ser una valor nulo");
        }
        

        return persistence.update(facturaEntity);
    }
    
    /**
     * Eliminar una factura por Id
     * @param facturaId El ID de la factura a borrar.
     */
    public void deleteFactura(Long facturaId)
    {
        persistence.delete(facturaId);
    }
    
}
