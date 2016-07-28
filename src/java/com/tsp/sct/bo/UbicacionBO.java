/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Ubicacion;
import com.tsp.sct.dao.jdbc.UbicacionDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISCesarMartinez
 */
public class UbicacionBO {
    
    private Ubicacion ubicacion = null;

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public UbicacionBO(Connection conn){
        this.conn = conn;
    }
    
     public UbicacionBO(int idUbicacion, Connection conn){        
        this.conn = conn;
        try{
            UbicacionDaoImpl UbicacionDaoImpl = new UbicacionDaoImpl(this.conn);
            this.ubicacion = UbicacionDaoImpl.findByPrimaryKey(idUbicacion);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Ubicacion findUbicacionbyId(int idUbicacion) throws Exception{
        Ubicacion Ubicacion = null;
        
        try{
            UbicacionDaoImpl UbicacionDaoImpl = new UbicacionDaoImpl(this.conn);
            Ubicacion = UbicacionDaoImpl.findByPrimaryKey(idUbicacion);
            if (Ubicacion==null){
                throw new Exception("No se encontro ninguna Ubicacion que corresponda al usuario según los parámetros específicados.");
            }
            if (Ubicacion.getIdUbicacion()<=0){
                throw new Exception("No se encontro ninguna Ubicacion que corresponda al usuario según los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de Ubicacion del usuario. Error: " + e.getMessage());
        }
        
        return Ubicacion;
    }
    
    
}
