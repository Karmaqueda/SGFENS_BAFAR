/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo.test;

import com.tsp.sct.util.Converter;
import java.math.BigDecimal;
import java.text.ParseException;

/**
 *
 * @author ISCesar
 */
public class GenericTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        GenericTest test = new GenericTest();
        
        test.testStringToDouble();
        test.testStringToInt();
    }
 
    
    public void testStringToDouble() throws Exception{
        //String data = "2,700.00";
        String data = "2.";
        
        double doub =  Converter.stringToDoubleFormatMexico(data);
        
        System.out.println(""  + doub);
    }
    
     public void testStringToInt() throws Exception{
        //String data = "2,700.00";
        String data = "2";
        
        int i = new Double(Converter.stringToDoubleFormatMexico(data)).intValue();
        
        System.out.println(""  + i);
    }
}
