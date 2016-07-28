/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.TipoUnidad;
import com.tsp.sct.dao.exceptions.TipoUnidadDaoException;
import com.tsp.sct.dao.jdbc.TipoUnidadDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISCesarMartinez
 */
public class UnidadBO {
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public UnidadBO(Connection conn){
        this.conn = conn;
    }
    
    public TipoUnidad findTipoUnidadbyId(int idTipoUnidad) throws Exception{
        TipoUnidad TipoUnidad = null;
        
        try{
            TipoUnidadDaoImpl TipoUnidadDaoImpl = new TipoUnidadDaoImpl(this.conn);
            TipoUnidad = TipoUnidadDaoImpl.findByPrimaryKey(idTipoUnidad);
            if (TipoUnidad==null){
                throw new Exception("No se encontro ningun TipoUnidad que corresponda con los parámetros específicados.");
            }
            if (TipoUnidad.getIdTipoUnidad()<=0){
                throw new Exception("No se encontro ningun TipoUnidad que corresponda con los parámetros específicados.");
            }
        }catch(TipoUnidadDaoException e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de TipoUnidad del usuario. Error: " + e.getMessage());
        }
        
        return TipoUnidad;
    }
    
    public String getUnidadesHTMLCombo(int idSeleccionado){
        String strHTMLCombo ="";

        try{
            TipoUnidad[] UnidadsDto = new TipoUnidadDaoImpl(this.conn).findAll();
            
            for (TipoUnidad itemUnidad:UnidadsDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado==itemUnidad.getIdTipoUnidad())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+itemUnidad.getIdTipoUnidad()+"' "
                            + selectedStr
                            + "title='"+itemUnidad.getDescTipoUnidad()+"'>"
                            + itemUnidad.getDescTipoUnidad()
                            +"</option>";
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return strHTMLCombo;
    }
}
