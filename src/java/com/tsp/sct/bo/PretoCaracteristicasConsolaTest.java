/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.PretoCaracteristicasConsola;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Leonardo
 */
public class PretoCaracteristicasConsolaTest {
    
    private Connection conn = null;    
    static UsuariosBO usu = new UsuariosBO();
    
    public static void main(String []args) throws SQLException{
        int paquete = 1;
        PretoCaracteristicasConsolaBO bo = new PretoCaracteristicasConsolaBO(usu.getConn());
        PretoCaracteristicasConsola[] caracteristicasConsola = new PretoCaracteristicasConsola[0];
        caracteristicasConsola = bo.findPrestoCaracteristicas();
        /*for (PretoCaracteristicasConsola caracteristicas : caracteristicasConsola) {
            System.out.println("ID: "+caracteristicas.getIdCaracteristica());
            System.out.println("NOMBRE: "+caracteristicas.getNombreCaracteristica());
            System.out.println("GRATIS: "+caracteristicas.getTpvGratis());
            System.out.println("EMPRENDEDOR: "+caracteristicas.getTpvEmprendedor());
            System.out.println("COMERCIANTE: "+caracteristicas.getTpvComerciante());
            System.out.println("PYME: "+caracteristicas.getTpvMipyme());
            System.out.println("EVC: "+caracteristicas.getEvc());
            System.out.println("PRETORIANO: "+caracteristicas.getPretorianoErp());
        }*/
        
        for (PretoCaracteristicasConsola caracteristicas : caracteristicasConsola) {            
            System.out.println("NOMBRE: "+caracteristicas.getNombreCaracteristica());
            System.out.println("GRATIS: "+caracteristicas.getTpvGratis());
            System.out.println("EMPRENDEDOR: "+caracteristicas.getTpvEmprendedor());
            System.out.println("COMERCIANTE: "+caracteristicas.getTpvComerciante());
            System.out.println("PYME: "+caracteristicas.getTpvMipyme());
            System.out.println("EVC: "+caracteristicas.getEvc());
            System.out.println("PRETORIANO: "+caracteristicas.getPretorianoErp());
        }
        
        usu.getConn().close();
               
    }
    
}
