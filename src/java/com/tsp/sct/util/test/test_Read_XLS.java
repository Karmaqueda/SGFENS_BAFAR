/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.util.test;

import com.tsp.sct.util.ReadExcel;
import jxl.*; 
import java.io.*; 


/**
 *
 * @author HpPyme
 */
public class test_Read_XLS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ReadExcel excel = new ReadExcel(); 
        excel.leerArchivoExcel("C:/temp/Layout Carga Clientes.xls"); 
        
    }
    
}
