/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.podam;

import java.util.Random;
import uk.co.jemos.podam.common.AttributeStrategy;

/**
 *
 * @author Miguel Angel Ramos Hurtado
 */
public class NumeroTarjetaStrategy implements AttributeStrategy<String>
{   
    Random r = new Random();
    @Override
    public String getValue()
    {
        StringBuilder bld = new StringBuilder();
        for(int i = 0 ; i < 16 ; i++)
        {
             
             int numero = (r.nextInt(9));
             bld.append(String.format("%d", numero));
             
        }
        return bld.toString();
    }
}
