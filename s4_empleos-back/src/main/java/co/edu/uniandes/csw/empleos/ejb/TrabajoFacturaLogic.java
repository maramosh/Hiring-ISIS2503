package co.edu.uniandes.csw.empleos.ejb;

import co.edu.uniandes.csw.empleos.entities.FacturaEntity;
import co.edu.uniandes.csw.empleos.entities.TrabajoEntity;
import co.edu.uniandes.csw.empleos.persistence.FacturaPersistence;
import co.edu.uniandes.csw.empleos.persistence.TrabajoPersistence;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Estudiante
 */
@Stateless
public class TrabajoFacturaLogic {

    @Inject
    private TrabajoPersistence trabajoPersistence;

    @Inject
    private FacturaPersistence facturaPersistence;
    
     /**
     * Remplazar la factura de un trabajo.
     *
     * @param calId .
     * @param estId .
     * @return .
     */
    public TrabajoEntity replaceFactura(Long calId, Long estId) {
        FacturaEntity facturaEntity = facturaPersistence.find(estId);
        TrabajoEntity trabajoEntity = trabajoPersistence.read(calId);
        trabajoEntity.setFactura(facturaEntity);
        return trabajoEntity;
    }
    
    
    
    /**
     * Borrar una factura de un Trabajo Este metodo se utiliza para borrar la
     * relacion de una factura.
     *
     * @param calId El libro que se desea borrar de la editorial.
     */
    public void removeFactura(Long calId) {
        TrabajoEntity trabajoEntity = trabajoPersistence.read(calId);
        FacturaEntity facturaEntity = facturaPersistence.find(trabajoEntity.getFactura().getId());
        trabajoEntity.setFactura(null);
        facturaEntity.setTrabajo(null);
    }
    
    /**
     * Asocia una oferta a un trabajo
     *
     * @param trabajoId Identificador de la instancia de trabajo
     * @param facturaId Identificador de la instancia de Factura
     * @return Instancia de BookEntity que fue asociada a Author
     */
    public FacturaEntity addFactura(Long trabajoId, Long facturaId) {
        TrabajoEntity trabajoEntity = trabajoPersistence.read(trabajoId);
        FacturaEntity facturaEntity = facturaPersistence.find(facturaId);
        trabajoEntity.setFactura(facturaEntity);
        facturaEntity.setTrabajo(trabajoEntity);
        return facturaPersistence.find(facturaId);
    }
    
        /**
     * Obtiene una instancia de CuentaBancariaEntity asociada a una
     * instancia de Factura
     *
     * @param trabajoId Identificador de la instancia de trabajo
     * @return instancia de oferta asociada a la instancia de estudiante
     * 
     */
    public FacturaEntity getFactura(Long trabajoId) {
        return trabajoPersistence.read(trabajoId).getFactura();
    }

}
