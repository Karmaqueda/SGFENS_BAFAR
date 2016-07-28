/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.util.test;

import com.tsp.sct.bo.ConceptoBO;
import com.tsp.sct.dao.dto.Concepto;
import com.tsp.sct.dao.exceptions.ConceptoDaoException;
import com.tsp.sct.dao.jdbc.ConceptoDaoImpl;
import java.io.IOException;

/**
 *
 * @author leonardo
 */
public class EncriptaNombreConcepto {
    
    public static void main (String args []) throws ConceptoDaoException, IOException{
        ConceptoDaoImpl conceptoDaoImpl = new ConceptoDaoImpl();
        Concepto[] conceptos = conceptoDaoImpl.findWhereIdEmpresaEquals(2162);
        
        System.out.println("tamaño de arreglo encontrado: "+conceptos.length);
                        
        ConceptoBO conceptoBO = new ConceptoBO(null);
        int contador = 1;
        for(Concepto c : conceptos){
            c.setNombre(conceptoBO.encripta(c.getNombre()));
            conceptoDaoImpl.update(c.createPk(), c);
            System.out.println("Numero actualizado: "+contador);
            contador++;
        }
         
         String encriptado = conceptoBO.encripta("CONSULTORIA");
         System.out.println("dato encriptado: "+encriptado);
    }
    
}
