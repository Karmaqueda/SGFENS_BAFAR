/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.microsip.bo;

import com.tsp.sct.dao.dto.EmpresaPermisoAplicacion;
import com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author leonardo
 */
public class AccesoSincronizacionBO {
    
    public boolean acceso(int idEmpresa, String token){
        
        EmpresaPermisoAplicacion epa = null;
        boolean accesoPermitido = false;
        
        try{
            Connection conn = ResourceManager.getConnection();
            try{
                epa = new EmpresaPermisoAplicacionDaoImpl(conn).findByPrimaryKey(idEmpresa);            
            }catch(Exception e){}        
        
            if(epa != null){
                if(epa.getWsSincronizacionAcceso() == 1 && epa.getWsSincronizacionToken().trim().equals(token))
                {
                   accesoPermitido = true;
                }else if(epa.getWsSincronizacionAcceso() == 1 && !epa.getWsSincronizacionToken().trim().equals(token) ){
                    //throw new Exception("token incorrecto, favor de verificarlo");
                }
            }
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return accesoPermitido;
    }
    
}
