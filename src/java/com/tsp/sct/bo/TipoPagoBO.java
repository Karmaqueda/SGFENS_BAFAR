/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.TipoPago;
import com.tsp.sct.dao.jdbc.TipoPagoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISCesarMartinez
 */
public class TipoPagoBO {
    
    TipoPago tipoPago = null;
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public TipoPagoBO(Connection conn) {
        this.conn = conn;
    }
    
    public TipoPagoBO(int idTipoPago, Connection conn) {
        this.conn = conn;
        try{
            this.tipoPago = new TipoPagoDaoImpl(this.conn).findByPrimaryKey(idTipoPago);
        }catch(Exception ex){
            ex.printStackTrace();
        }
                
    }

    public TipoPago getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(TipoPago tipoPago) {
        this.tipoPago = tipoPago;
    }
    
    
    
    public TipoPago findTipoPagoById(int idTipoPago) throws Exception{
        TipoPago tipoPago = null;
        
        try{
            TipoPagoDaoImpl empresaDaoImpl = new TipoPagoDaoImpl(this.conn);
            tipoPago = empresaDaoImpl.findByPrimaryKey(idTipoPago);
            if (tipoPago==null){
                throw new Exception("No se encontro ningun Tipo de Pago que corresponda con los parámetros específicados.");
            }
            if (tipoPago.getIdTipoPago()<=0){
                throw new Exception("No se encontro ningun Tipo de Pago que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de Tipo de Pago. Error: " + e.getMessage());
        }
        
        return tipoPago;
    }
    
}
