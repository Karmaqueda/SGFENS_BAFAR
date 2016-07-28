/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.TipoComprobante;
import com.tsp.sct.dao.jdbc.TipoComprobanteDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 26-dic-2012 
 */
public class TipoComprobanteBO {
    
    TipoComprobante tipoComprobante = null;
    
    public static final int TIPO_FACTURA = 2;
    public static final int TIPO_RECIBO_HONORARIOS = 3;
    public static final int TIPO_RECIBO_ARRENDAMIENTO = 4;
    public static final int TIPO_NOTA_CREDITO = 5;
    public static final int TIPO_NOTA_DEBITO = 6;
    public static final int TIPO_NOMINA = 40;
    public static final int TIPO_FACTURA_IMPUESTO_X_CONCEPTO = 41;
    
    public static final int TIPO_EXPRESS_CON_DESGLOSE = 38;
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public TipoComprobanteBO(Connection conn) {
        this.conn = conn;
    }
    
    public TipoComprobanteBO(int idTipoComprobante, Connection conn) {
        this.conn = conn;
        try{
            this.tipoComprobante = new TipoComprobanteDaoImpl(this.conn).findByPrimaryKey(idTipoComprobante);
        }catch(Exception ex){
            
        }
    }
    
    public static String getTipoComprobanteNombreCarpeta(int idTipoComprobante){
        String nombreCarpeta = "";
        switch (idTipoComprobante){
            case TIPO_FACTURA:
                nombreCarpeta = "facturas";
                break;
            case TIPO_EXPRESS_CON_DESGLOSE:
                nombreCarpeta = "facturasExpress";
                break;
            case TIPO_NOTA_CREDITO:
                nombreCarpeta = "notasCredito";
                break;
            case TIPO_NOTA_DEBITO:
                nombreCarpeta = "notasDebito";
                break;
            case TIPO_NOMINA:
                nombreCarpeta = "nomina";
                break;
        }
        return nombreCarpeta;
    }
    
    public TipoComprobante getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(TipoComprobante tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

}
