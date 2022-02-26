/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.podam;


import java.security.SecureRandom;
import uk.co.jemos.podam.common.AttributeStrategy;

/**
 *
 * @author je.hernandezr
 */
public class NumeroStringStrategy implements AttributeStrategy<String> {

    @Override
    public String getValue() {
        SecureRandom num = new SecureRandom();

        int numero = Math.abs(num.nextInt(12) + 9);
        String generatedString = "";

        for (int i = 0; i < numero; i++) {
            int n = Math.abs(num.nextInt(10));
            generatedString = n + "";
        }

        return (generatedString);
    }

}
