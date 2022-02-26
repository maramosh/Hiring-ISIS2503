/*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.persistence;

import co.edu.uniandes.csw.empleos.entities.EstudianteEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Esta clase se encarga de crear y persistir estudiantes
 * @author David Dominguez
 */
@Stateless
public class EstudiantePersistence {
    
    //Logger que nos permitirá crear registros de lo que hacemos
    private static final Logger LOGGER = Logger.getLogger(EstudiantePersistence.class.getName());
    
    // Se declara el manejador de entidades y se relaciona con la base de datos
    @PersistenceContext(unitName = "empleosPU")
    public EntityManager em;
    
    /* 
     * Método que crea añade el estudiante que llega por parámetro a la base de datos
     * @param 
           e El estudiante que se desea añadir a la base de datos.
     * @return 
           El estudiante que se añadió a la base de datos.
    */
    public EstudianteEntity create(EstudianteEntity e) {
        LOGGER.log(Level.INFO, "Creando un estudiante nuevo");
        em.persist(e);
        LOGGER.log(Level.INFO, "Nuevo estudiante creado");
        return e;
    }
    
    public EstudianteEntity read(long id){
        LOGGER.log(Level.INFO, "Leyendo estudiante {0}", id);
        return em.find(EstudianteEntity.class, id);
    }
    
    public EstudianteEntity delete(EstudianteEntity e) {
        LOGGER.log(Level.INFO, "Eliminando estudiante {0}", e.getId());
        em.remove(e);
        return e;
    }
    
     public EstudianteEntity update(EstudianteEntity estudianteEntity) {
        LOGGER.log(Level.INFO, "Merging estudiante {0}", estudianteEntity.getId());       
        return em.merge(estudianteEntity);
    }
    
     public EstudianteEntity delete(Long id) {
        LOGGER.log(Level.INFO, "Se eliminar\u00e1 el estudiante {0}", id); 
        EstudianteEntity e = read(id);
        em.remove(e);
        LOGGER.log(Level.INFO, "Se eliminó el estudiante {0}", id); 
        return e;
    }
     
     public List<EstudianteEntity> findAll() {
        LOGGER.log(Level.INFO, "Obteniendo estudiantes");
        TypedQuery query = em.createQuery("select u from EstudianteEntity u", EstudianteEntity.class);
        return query.getResultList();
    }
     
     
      public EstudianteEntity find(Long id) {
        LOGGER.log(Level.INFO, "Obteniendo estudiante");
        return em.find(EstudianteEntity.class, id);
    }
}
