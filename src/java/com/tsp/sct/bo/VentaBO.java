/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Venta;
import com.tsp.sct.dao.dto.VentaAbono;
import com.tsp.sct.dao.dto.VentaDetalle;
import com.tsp.sct.dao.jdbc.VentaAbonoDaoImpl;
import com.tsp.sct.dao.jdbc.VentaDaoImpl;
import com.tsp.sct.dao.jdbc.VentaDetalleDaoImpl;
import java.sql.Connection;
import java.text.DecimalFormat;

/**
 *
 * @author ISCesarMartinez
 */
public class VentaBO {
    
    //private Venta venta=null;
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    
    public VentaBO(Connection conn){
        this.conn = conn;
    }
    
    /*
    public VentaBO(int idVenta, Connection conn){
        try{
            venta = new VentaDaoImpl(this.conn).findByPrimaryKey(idVenta);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }*/
    
    public Venta findVentabyId(int idVenta) throws Exception{
        Venta venta = null;
        
        try{
            VentaDaoImpl ventaDaoImpl = new VentaDaoImpl(this.conn);
            venta = ventaDaoImpl.findByPrimaryKey(idVenta);
            if (venta==null){
                throw new Exception("No se encontro ninguna venta que corresponda según los parámetros específicados.");
            }
            if (venta.getIdVenta()<=0){
                throw new Exception("No se encontro ninguna venta que corresponda según los parámetros específicados.");
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la Venta. Error: " + e.toString());
        }
        
        return venta;
    }
    
    /*
    public VentaDetalle[] getDetalles(){
        return getDetalles(this.venta!=null?this.venta.getIdVenta():0);
    }*/
    
    public VentaDetalle[] getDetalles(int idVenta){
        VentaDetalle[] detalles = new VentaDetalle[0];
        
        try{
            detalles = new VentaDetalleDaoImpl(this.conn).findWhereIdVentaEquals(idVenta);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return detalles;
    }
    
    public void updateBD(Venta ventaDto){
        try{
            new VentaDaoImpl(this.conn).update(ventaDto.createPk(), ventaDto);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public double calculaAdeudo(int idVenta) throws Exception{
        VentaAbono[] abonos = new VentaAbono[0];
        double adeudo=0;
        
        Venta venta = findVentabyId(idVenta);
        if (venta.getPagadaTotal()!=1){
        
            double totalVenta = venta.getTotal();
            adeudo = Math.round(totalVenta * 100.0)/100.0;
            abonos = new VentaAbonoDaoImpl(this.conn).findWhereIdVentaEquals(idVenta);
            
            for (VentaAbono abono:abonos){
                adeudo -= abono.getMontoAbono();
                
                adeudo = Math.round(adeudo * 100.0)/100.0;
                
            }
            
            if (adeudo<0)
                adeudo = 0;
            
        }
        
        return adeudo;
    }
}
