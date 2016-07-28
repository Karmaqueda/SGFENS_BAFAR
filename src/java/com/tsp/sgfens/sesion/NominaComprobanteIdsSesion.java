/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.sesion;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class NominaComprobanteIdsSesion {
    
    private List<Integer> idsComprobantes = new ArrayList<Integer>();

    public List<Integer> getIdsComprobantes() {
        return idsComprobantes;
    }
    
    public List<Integer> getIdsComprobantesNew() {
        idsComprobantes = new ArrayList<Integer>();
        return idsComprobantes;
    }

    public void setIdsComprobantes(List<Integer> idsComprobantes) {
        this.idsComprobantes = idsComprobantes;
    }   
    
    public List<Integer> agregarIDs(int idComprobante){
        System.out.println("ID a agregar: "+idComprobante);
        idsComprobantes.add(idComprobante);   
        
        System.out.println("IDs que estan en la lista (size) "+idsComprobantes.size()+": ");
        //*
        for(int i : idsComprobantes){
            System.out.println(i);
        }
        //*
        
        return idsComprobantes;
    }
    
    public List<Integer> quitarIDs(int idComprobante){
        List<Integer> idsComprobantesNew = new ArrayList<Integer>();
        
        System.out.println("ID a quitar: "+idComprobante);
        for(int id : idsComprobantes){
            if(id != idComprobante){
                idsComprobantesNew.add(id);
            }
        }
        
        ///*
        System.out.println("IDs que quedan en la lista (size) "+idsComprobantesNew.size()+": ");
        for(int i : idsComprobantesNew){
            System.out.println(i);
        }
        ///*
        idsComprobantes = idsComprobantesNew;
        return idsComprobantesNew;
    }
    
/*    public static void main(String args[]){
        NominaComprobanteIdsSesion nIds = new NominaComprobanteIdsSesion();
        
        
        //agragamos unos ids:
        nIds.agregarIDs(123);
        nIds.agregarIDs(124);
        nIds.agregarIDs(126);
        nIds.agregarIDs(128);
        nIds.agregarIDs(131);
        nIds.agregarIDs(133);
        nIds.agregarIDs(136);
        nIds.agregarIDs(140);
        nIds.agregarIDs(145);
        nIds.agregarIDs(156);
        
        //recuperamos la lista:
        List<Integer> IDS = nIds.getIdsComprobantes();
        
        //imprimimos la lista:
        System.out.println("______________________________");
        System.out.println("VALORES QUE ESTAN EN LA LISTA (size "+IDS.size()+"): ");
        for(int j : IDS){
            System.out.println(j);            
        }
        System.out.println("______________________________");
        
        //QUITAMOS ALGUNOS IDs:
        nIds.quitarIDs(126);
        
        
    }
*/    
}
