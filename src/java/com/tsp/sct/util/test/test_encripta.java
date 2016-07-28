/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.util.test;

import com.tsp.sct.bo.ConceptoBO;
import com.tsp.sct.util.Encrypter;
import java.io.IOException;
import com.tsp.sct.bo.UsuarioBO;
import java.sql.Connection;

/**
 *
 * @author 578
 */
public class test_encripta {

    private static Connection conn = null;
    
    
   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        String password = "Demo";
        Encrypter datoEnc =  new Encrypter();
        datoEnc.setMd5(true);////ESTA PARTE DE CODIGO ES PARA GENERAR EL md5
        String passEncriptado = datoEnc.encodeString2(password);
        System.out.println("Pass encriptado : " + passEncriptado);
        
        ConceptoBO conceptoBO = new ConceptoBO(test_encripta.conn);
        String  name = conceptoBO.desencripta("xB6IChFwnBsl1ixx2dtWxQ==");    
       //System.out.println("Pass desencriptado : " + name);
    }
    
}
