/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo.test;

import com.tsp.sct.bo.ConceptoBO;
import com.tsp.sct.dao.dto.Concepto;
import com.tsp.sct.dao.jdbc.ResourceManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author leonardo
 */
public class aaaaConceptoTest {
    
    public static void main(String args[]) throws IOException{
        ConceptoBO conceptoBO = new ConceptoBO(getConn());
        Concepto[] conceptosDto = new Concepto[0];
        conceptosDto = conceptoBO.findConceptos(-1, 1 , 0, 0, "");
        
        for (Concepto item:conceptosDto){
            System.out.println(item.getIdConcepto() +": "+conceptoBO.desencripta(item.getNombre()));
        }
        
        System.out.println("--------------------------------------------");
        System.out.println("--------------------------------------------");
        System.out.println("--------------------------------------------");
        System.out.println("--------------------------------------------");
        
        for (Concepto item:conceptosDto){
            item.setNombre(conceptoBO.desencripta(item.getNombre()));
            item.setNombre(item.getNombre().trim());
        }
        
        java.util.Arrays.sort(conceptosDto);
        for (Concepto item:conceptosDto){
            System.out.println(item.getIdConcepto() +": "+item.getNombre());
        }
    }
    
    
    
    
    private static Connection conn = null;
    
    public static Connection getConn() {
        if (aaaaConceptoTest.conn==null){
            try {
                aaaaConceptoTest.conn = ResourceManager.getConnection();
            } catch (SQLException ex) {}
        }else{
            try {
                if (aaaaConceptoTest.conn.isClosed()){
                    aaaaConceptoTest.conn = ResourceManager.getConnection();
                }
            } catch (SQLException ex) {}
        }
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
}
