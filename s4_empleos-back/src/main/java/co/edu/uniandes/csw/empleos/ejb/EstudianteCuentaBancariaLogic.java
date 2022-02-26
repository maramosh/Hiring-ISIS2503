package co.edu.uniandes.csw.empleos.ejb;

import co.edu.uniandes.csw.empleos.entities.CuentaBancariaEntity;
import co.edu.uniandes.csw.empleos.entities.EstudianteEntity;
import co.edu.uniandes.csw.empleos.persistence.CuentaBancariaPersistence;
import co.edu.uniandes.csw.empleos.persistence.EstudiantePersistence;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class EstudianteCuentaBancariaLogic {

    @Inject
    private EstudiantePersistence estudiantePersistence;

    @Inject
    private CuentaBancariaPersistence cuentaBancariaPersistence;
    
     /**
     * Remplazar la cuentaBancaria de un estudiante.
     *
     * @param calId .
     * @param estId .
     * @return .
     */
    public EstudianteEntity replaceCuentaBancaria(Long estId, Long cuentaId) {
        CuentaBancariaEntity cuentaBancariaEntity = cuentaBancariaPersistence.find(cuentaId);
        EstudianteEntity estudianteEntity = estudiantePersistence.find(estId);
        estudianteEntity.setCuentaBancaria(cuentaBancariaEntity);
        return estudianteEntity;
    }
    
    
    /**
     * Borrar una cuentaBancaria de un Estudiante Este metodo se utiliza para borrar la
     * relacion de una cuentaBancaria.
     *
     * @param calId El libro que se desea borrar de la editorial.
     */
    public void removeCuentaBancaria(Long estId) {
        EstudianteEntity estudianteEntity = estudiantePersistence.find(estId);
        if(estudianteEntity != null && estudianteEntity.getCuentaBancaria() != null) {
            CuentaBancariaEntity cuentaBancariaEntity = cuentaBancariaPersistence.find(estudianteEntity.getCuentaBancaria().getId());
            estudianteEntity.setCuentaBancaria(null);
            cuentaBancariaEntity.setEstudiante(null);
        }
    }
    
    /**
     * Asocia una cuentaBancaria a un Estudiante
     *
     * @param estudiantesId Identificador de la instancia de Estudiante
     * @param cuentaBancariaId
     * @return Instancia de BookEntity que fue asociada a Author
     */
    public CuentaBancariaEntity addCuentaBancaria(Long estudiantesId, Long cuentaBancariaId) {
        EstudianteEntity estudianteEntity = estudiantePersistence.find(estudiantesId);
        CuentaBancariaEntity cuentaBancariaEntity = cuentaBancariaPersistence.find(cuentaBancariaId);
        estudianteEntity.setCuentaBancaria(cuentaBancariaEntity);
        cuentaBancariaEntity.setEstudiante(estudianteEntity);
        return cuentaBancariaPersistence.find(cuentaBancariaId);
    }
    
        /**
     * Obtiene una instancia de CuentaBancariaEntity asociada a una
     * instancia de Estudiante
     *
     * @param estudianteId Identificador de la instancia de Estudiante
     * @return instancia de CuentaBancariaEntity asociadas a la instancia de estudiante
     * 
     */
    public CuentaBancariaEntity getCuentaBancaria(Long estudianteId) {
        return estudiantePersistence.find(estudianteId).getCuentaBancaria();
    }
    
}
