package co.edu.uniandes.csw.empleos.persistence;

import co.edu.uniandes.csw.empleos.entities.TrabajoEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Esta clase se encarga de crear y persistir trabajos
 * @author David Dominguez
 */
@Stateless
public class TrabajoPersistence {
    
    //Logger que nos permitirá crear registros de lo que hacemos
    private static final Logger LOGGER = Logger.getLogger(TrabajoPersistence.class.getName());
    
    // Se declara el manejador de entidades y se relaciona con la base de datos
    @PersistenceContext(unitName = "empleosPU")
    public EntityManager em;
    
    /* 
     * Método que crea añade el trabajo que llega por parámetro a la base de datos
     * @param 
           t El trabajo que se desea añadir a la base de datos.
     * @return 
           El trabajo que se añadió a la base de datos.
    */
    public TrabajoEntity create(TrabajoEntity t) {
        LOGGER.log(Level.INFO, "Creando un trabajo nuevo");
        em.persist(t);
        LOGGER.log(Level.INFO, "Nuevo trabajo creado");
        return t;
    }
    
    public TrabajoEntity read(long id){
        LOGGER.log(Level.INFO, "Leyendo trabajo {0}", id);
        return em.find(TrabajoEntity.class, id);
    }
    
    public TrabajoEntity delete(TrabajoEntity e) {
        LOGGER.log(Level.INFO, "Eliminando trabajo {0}", e.getId());
        em.remove(e);
        return e;
    }
    
     public TrabajoEntity update(TrabajoEntity trabajoEntity) {
        LOGGER.log(Level.INFO, "Merging trabajo {0}", trabajoEntity.getId());       
        return em.merge(trabajoEntity);
    }
    
     public void delete(Long id) {
        LOGGER.log(Level.INFO, "Se eliminar\u00e1 el trabajo {0}", id); 
        
        TrabajoEntity e = em.find(TrabajoEntity.class, id);
        em.remove(e);
        LOGGER.log(Level.INFO, "Se eliminó el trabajo {0}", id); 
        
    }
     
     public List<TrabajoEntity> findAll() {
        LOGGER.log(Level.INFO, "Obteniendo trabajos");
        TypedQuery query = em.createQuery("select u from TrabajoEntity u", TrabajoEntity.class);
        return query.getResultList();
    }
     
}
