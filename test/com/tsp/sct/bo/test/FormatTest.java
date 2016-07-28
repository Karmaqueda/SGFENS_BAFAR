/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo.test;

import java.text.DecimalFormat;

/**
 *
 * @author ISCesar
 */
public class FormatTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double porcentaje = 10;
        
        DecimalFormat f = new DecimalFormat( "###.##'%'" );
        System.out.println( f.format( porcentaje ) );
        
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        System.out.println( decimalFormat.format( porcentaje ) );
        
    }
    
}
