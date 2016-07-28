/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.EmpresaConfig;
import com.tsp.sct.dao.jdbc.EmpresaConfigDaoImpl;
import java.sql.Connection;

/**
 *
 * @author 578
 */
public class EmpresaConfigBO {
    private EmpresaConfig empresaConfig = null;
    private Connection conn = null;

    public EmpresaConfig getEmpresaConfig() {
        return empresaConfig;
    }

    public void setEmpresaConfig(EmpresaConfig empresaConfig) {
        this.empresaConfig = empresaConfig;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public EmpresaConfigBO(Connection conn) {
         this.conn = conn;
    }
    
    
    
    
    public EmpresaConfigBO(int idEmpresa, Connection conn){
        this.conn = conn;
        try{
            EmpresaConfigDaoImpl EmpresaConfigDaoImpl = new EmpresaConfigDaoImpl(this.conn);
            this.empresaConfig = EmpresaConfigDaoImpl.findByPrimaryKey(idEmpresa);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public EmpresaConfig findEmpresaConfigbyIdEMpresa(int idEmpresa) throws Exception{
        EmpresaConfig empresaConfig = null;
        
        try{
            EmpresaConfigDaoImpl empresaConfigDaoImpl = new EmpresaConfigDaoImpl(this.conn);
            empresaConfig = empresaConfigDaoImpl.findByPrimaryKey(idEmpresa);
            if (empresaConfig==null){
                throw new Exception("No se encontro ninguna configuración según los parámetros específicados.");
            }
            if (empresaConfig.getIdEmpresa()<=0){
                throw new Exception("No se encontro ninguna configuración que corresponda al usuario según los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de Configuración. Error: " + e.getMessage());
        }
        
        return empresaConfig;
    }
    
}
