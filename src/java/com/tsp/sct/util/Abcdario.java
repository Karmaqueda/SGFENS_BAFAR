/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.util;

/**
 *
 * @author leonardo
 */
public class Abcdario {
    
     public char[] createAbcdario() {
        char[] s;
        s=new char[26];
        for ( int i=0; i<26; i++) {
            s[i] = (char) ('A' + i );             
        }
        return s;
    }
     
    public String[] createAbcdario2(int cantidadPuntos) {
        String[] ss;
        ss=new String[cantidadPuntos];
        
        char[] s;
        s=new char[26];
        
        double cantidadBloques = cantidadPuntos/26;
        
        int entero = (int)cantidadBloques;
        
         int incremento = 1;
        int posicion = 0;
        while(entero >= 0){
        
            for ( int i=0; i<26; i++) {
                s[i] = (char) ('A' + i );             
                System.out.println(incremento + "" + s[i] + "");
                if(posicion >= cantidadPuntos){
                    break;
                }
                ss[posicion] = incremento + "" + s[i] + "";
                posicion++;
                
            }
            entero--;
            incremento++;
        }
        return ss;
    } 
    
}
