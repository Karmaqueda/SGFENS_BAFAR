/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.util;

/**
 *
 * @author 578
 */
public class codec {
    
    
     // convert Hex to ASCII  	
    public  String hexToASCII(String hexa){ 
        int[] text = new int [hexa.length() / 2]; 
        int j = 0; 
        String ascii = ""; 	 	 
        StringBuilder s = new StringBuilder(hexa); 
 	 	 
        // remove all space within hexa String  	
        for (int i=0; i < s.length(); i++)  	 	
            if (s.charAt(i) == ' ') 
                 s.deleteCharAt(i); 
 	 	 
      // assign new non-space string back to hexa   
        hexa = s.toString(); 
 	 	 
 	for (int i = 0; i < hexa.length(); i += 2) 
            text[j++] = Integer.parseInt(hexa.substring(i, i + 2), 16); 
 	 	 
            for (int i=0; i<text.length; i++) 
            { 
                    ascii += (char)text[i]; 
                    // print ascii text to screen too! 
                    //System.out.print((char)text[i] + " "); 
            } 
 	 	 
         return ascii; 
    } 
 
 	// convert Hex to Decimal (number)  	
    public  int hexToDec(String hex) { 
	 return Integer.parseInt(hex, 16); 
    } 
    
    // convert Hex to Binary (number), restore zero at front too  	
    public String hexToBin(String hex){ 
	 String s = Integer.toBinaryString(hexToDec(hex));  	 	 
 	 	// restore zero  
         while (s.length() < 8) 
             s = "0" + s; 
 	 	 
	 return s; 
    }      

}
