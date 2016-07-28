/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo.test;

import com.sap.bafar.ws.creacontrato.Bdcmsgcoll;
import com.tsp.microsip.bean.Almacen;
import com.tsp.sct.util.GenericMethods;
import com.tsp.sgfens.ws.request.WsItemAgenda;

/**
 *
 * @author ISCesar
 */
public class GenericMethodsTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GenericMethodsTest test = new GenericMethodsTest();
        
        test.testObjectToString();
    }
    
    private void testObjectToString(){
        WsItemAgenda agenda = new WsItemAgenda();
        agenda.setDescripcionTarea("Hello World!");
        System.out.println(GenericMethods.objectToString(agenda));
        
        
        Bdcmsgcoll bdcmsgcoll = new Bdcmsgcoll();
        bdcmsgcoll.setMsgid("Hola mundo!");
        System.out.println(bdcmsgcoll.toString());
        
        System.out.println(GenericMethods.objectToString(bdcmsgcoll));
        
    }
    
}
