/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.VentaMetodoPago;
import com.tsp.sct.dao.jdbc.VentaMetodoPagoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISCesarMartinez
 */
public class VentaMetodoPagoBO {
    
    public static final int METODO_PAGO_TDC=1;
    public static final int METODO_PAGO_EFECTIVO=2;
    public static final int METODO_PAGO_DOCUMENTO=3;
    public static final int METODO_PAGO_CREDITO=4;
    public static final int METODO_PAGO_BONIFICACION=6;
    public static final int METODO_PAGO_DEVOLUCION_EFE=10;

    public static final String METODO_PAGO_TDC_STR = "Tarjeta de Crédito/Débito";
    public static final String METODO_PAGO_EFECTIVO_STR = "Efectivo";
    public static final String METODO_PAGO_DOCUMENTO_STR = "Documento (Cheque/Vale)";
    public static final String METODO_PAGO_CREDITO_STR = "Crédito";
    public static final String METODO_PAGO_BONIFICACION_STR = "Bonificación";
    public static final String METODO_PAGO_DEVOLUCION_EFE_STR = "Devolución Efectivo";
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
 
    
    public VentaMetodoPago findMetodoPagoById(int idTipoPago) throws Exception{
        VentaMetodoPago metodoPago = null;
        
        try{
            VentaMetodoPagoDaoImpl ventaMetodoPagoDao = new VentaMetodoPagoDaoImpl(this.conn);
            metodoPago = ventaMetodoPagoDao.findByPrimaryKey(idTipoPago);
            if (metodoPago==null){
                throw new Exception("No se encontro ningun Tipo de Pago que corresponda con los parámetros específicados.");
            }
            if (metodoPago.getIdVentaMetodoPago()<=0){
                throw new Exception("No se encontro ningun Tipo de Pago que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de Tipo de Pago. Error: " + e.getMessage());
        }
        
        return metodoPago;
    }
}
