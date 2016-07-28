/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.TipoDeMoneda;
import com.tsp.sct.dao.jdbc.TipoDeMonedaDaoImpl;
import java.sql.Connection;


/**
 *
 * @author ISCesarMartinez
 */
public class TipoDeMonedaBO {
    
    private TipoDeMoneda tipoMoneda = null;
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public TipoDeMonedaBO(Connection conn) {
        this.conn = conn;
        tipoMoneda = new TipoDeMoneda();
        tipoMoneda.setIdTipoMoneda(-1);
        tipoMoneda.setClave("MXN");
        tipoMoneda.setDescripcion("PESOS");
    }
    
    public TipoDeMonedaBO(int idTipoMoneda, Connection conn) {
        this.conn = conn;
        try{
            this.tipoMoneda = new TipoDeMonedaDaoImpl(this.conn).findByPrimaryKey(idTipoMoneda);
        }catch(Exception ex){
            ex.printStackTrace();
        }
                
    }

    public TipoDeMoneda getTipoMoneda() {
        return tipoMoneda;
    }

    public void setTipoMoneda(TipoDeMoneda tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }
    
}
