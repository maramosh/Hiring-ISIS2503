/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.ejb;

import co.edu.uniandes.csw.empleos.entities.TarjetaDeCreditoEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.TarjetaDeCreditoPersistence;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Miguel Angel Ramos Hurtado
 */
@Stateless
public class TarjetaDeCreditoLogic {

    @Inject
    private TarjetaDeCreditoPersistence persistence;
    
    public TarjetaDeCreditoEntity createTarjetaDeCredito(TarjetaDeCreditoEntity tarjetaCredito) throws BusinessLogicException {
       
        for (TarjetaDeCreditoEntity tarjetaDeCreditoEntity : persistence.findAll()) {
            if(tarjetaDeCreditoEntity.getNumero().equals(tarjetaCredito.getNumero()))
            {
                throw new BusinessLogicException("La tarjeta de credito ya existe");
            }
        }
        
        if (tarjetaCredito.getNumero() == null ) 
        {
            throw new BusinessLogicException("El número de la tarjeta de crédito no puede ser nulo.");
        }
        else if(tarjetaCredito.getFecha() == null  )
        {
            throw new BusinessLogicException("La fecha no debe estar nula");
        }
        else if(tarjetaCredito.getFecha().equals(""))
        {
            throw new BusinessLogicException("La fecha no debe estar vacia");
        }
        else if(tarjetaCredito.getFecha().length() != 5 )
        {
            throw new BusinessLogicException("La fecha debe contener el día y el año en el formato dd/aa");
        }
         else if(tarjetaCredito.getCVC() == null  )
        {
            throw new BusinessLogicException("El cvc no debe estar nulo");
        }
        else if (tarjetaCredito.getNumero().equals(""))
        {
            throw new BusinessLogicException("El número de la tarjeta de crédito no puede ser vacío.");
        }
        else if(tarjetaCredito.getCVC().length() != 3)
        {
            throw new BusinessLogicException("El cvc debe contener 3 digitos");
        }
        else if( !tarjetaCredito.getNumero().startsWith("4") && !tarjetaCredito.getNumero().startsWith("5") )
        {      
            throw new BusinessLogicException("Ingrese un numero valido para una tarjeta MasterCard o VISA");
        }
       else if (tarjetaCredito.getNumero().length() != 16)
        {
            throw new BusinessLogicException("La tarjeta de credito debe contener 16 números");
        }
       else if(tarjetaCredito.getFecha().charAt(2) != '/')
        {
            throw new BusinessLogicException("El formato de la fecha debe ser dd/aa.");
        }
        else if(tarjetaCredito.getCVC().equals(""))
        {
            throw new BusinessLogicException("El cvc no debe estar vacio");
        }
            

        
        
        
        
        tarjetaCredito = persistence.create(tarjetaCredito);
        return tarjetaCredito;
    }

     public List<TarjetaDeCreditoEntity> getTarjetas() {
        
        
        
        return persistence.findAll();
    }
    /**
     * Obtiene los datos de una instancia de Tarjeta de Credito a partir de su ID.
     *
     * @param pId Identificador de la instancia a consultar
     * @return Instancia de TarjetaDeCreditoEntity con los datos de la tarjeta consultada.
     * @throws co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException 
     */
    
    public TarjetaDeCreditoEntity getTarjetaCredito(Long pId) throws BusinessLogicException
    {
        TarjetaDeCreditoEntity tarjetaEntity = persistence.find(pId);
        if(tarjetaEntity == null)
        {
            throw new BusinessLogicException("La tarjeta con el id: "+pId+" no existe.");
        }   
        return tarjetaEntity;
    }
    
    /**
     * Actualiza la informacion de la instancia de tarjeta de credito.
     * 
     * @param pId Id de la tarjeta a actualizar.
     * @param pTarjeta Instancia de la tarjeta con los datos a actualizar.
     * @return Instancia de la tarjeta con los datos actualizados.
     */
    public TarjetaDeCreditoEntity updateTarjetaCredito(Long pId, TarjetaDeCreditoEntity tarjetaCredito) throws BusinessLogicException
    {
       
        getTarjetaCredito(pId);
        
                
        if (tarjetaCredito.getNumero() == null ) 
        {
            throw new BusinessLogicException("El número de la tarjeta de crédito no puede ser vacío1.");
        }
        else if (tarjetaCredito.getNumero().equals(""))
        {
            throw new BusinessLogicException("El número de la tarjeta de crédito no puede ser vacío1.");
        }
        else if (tarjetaCredito.getNumero().length() != 16)
        {
            throw new BusinessLogicException("La tarjeta de credito debe contener 16 números1");
        }
        else if( !tarjetaCredito.getNumero().startsWith("4") && !tarjetaCredito.getNumero().startsWith("5") )
        {      
            throw new BusinessLogicException("Ingrese un numero valido para una tarjeta MasterCard o VISA1");
        }
        else if(tarjetaCredito.getCVC() == null  )
        {
            throw new BusinessLogicException("El cvc no debe estar vacio1");
        }
        else if(tarjetaCredito.getCVC().equals(""))
        {
            throw new BusinessLogicException("El cvc no debe estar vacio1");
        }
        else if(tarjetaCredito.getCVC().length() != 3)
        {
            throw new BusinessLogicException("El cvc debe contener 3 digitos1");
        }    
        else if(tarjetaCredito.getFecha() == null  )
        {
            throw new BusinessLogicException("La fecha no debe estar vacia1");
        }
        else if(tarjetaCredito.getFecha().equals(""))
        {
            throw new BusinessLogicException("La fecha no debe estar vacia1");
        }
        else if(tarjetaCredito.getFecha().charAt(2) != '/')
        {
            throw new BusinessLogicException("El formato de la fecha debe ser dd/aa.1");
        }
        else if(tarjetaCredito.getFecha().length() != 5 )
        {
            throw new BusinessLogicException("La fecha debe contener el día y el año en el formato dd/aa1");
        }
      
        
        
        return persistence.update(tarjetaCredito);
    }
    
    /**
     * Borra la instancia de tarjeta de credito con el id dado.
     * @param pId Es el id de la tarjeta de credito a borrar.
     */
    public void deleteTarjetaCredito(Long pId)
    {
        persistence.delete(pId);
    }
}
