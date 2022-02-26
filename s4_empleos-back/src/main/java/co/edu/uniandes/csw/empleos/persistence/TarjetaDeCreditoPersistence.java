/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.persistence;

import co.edu.uniandes.csw.empleos.entities.TarjetaDeCreditoEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Miguel Angel Ramos Hurtado
 */
@Stateless
public class TarjetaDeCreditoPersistence {
    
    @PersistenceContext(unitName="empleosPU")
    protected EntityManager em;
    
     /**
     * Busca si hay alguna tarjeta con el id proporcionado.
     *
     * @param pId: id correspondiente a la tarjeta de crédito buscada.
     * @return la tarjeta de crédito con el id asociado.
     */
    public TarjetaDeCreditoEntity find(Long pId) {
        return em.find(TarjetaDeCreditoEntity.class, pId);
    }
    
    
    /**
     * Método para persisitir la entidad en la base de datos.
     *
     * @param tarjeta objeto tarjetaDeCredito que se creará en la base de datos
     * @return devuelve la entidad creada con un id dado por la base de datos.
     */
    public TarjetaDeCreditoEntity create (TarjetaDeCreditoEntity tarjeta)
    {
        em.persist(tarjeta);
        return tarjeta;
    }
    
    /**
     * Devuelve todas las tarjetas de credito de la base de datos.
     * 
     * @return una lista con todas las tarjetas de credito que encuentre en la base de datos.
     */
    public List<TarjetaDeCreditoEntity> findAll() {
        Query q = em.createQuery("select u from TarjetaDeCreditoEntity u");
        return q.getResultList();
    }
    
    /**
     * Actualiza la tarjeta de credito.
     * 
     * @param tarjetaCredito: Es el objeto de tipo tarjeta de credito que viene con las actualizaciones.
     * @return una tarjeta de credito con los cambios aplicados.
     */
    public TarjetaDeCreditoEntity update(TarjetaDeCreditoEntity tarjetaCredito)
    {
        return em.merge(tarjetaCredito);
    }
    
    /**
     * Borra la tarjeta de credito de la base de datos recibiendo como parametro el id de 
     * la tarjeta.
     * 
     * @param pId: id correspondiente a la tarjeta de credito a borrar.
     */
    public void delete(Long pId)
    {
        TarjetaDeCreditoEntity entity = em.find(TarjetaDeCreditoEntity.class, pId);
        em.remove(entity);
    }
}
