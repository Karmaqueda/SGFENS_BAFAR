/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.sesion;

import com.tsp.sct.bo.SmsPaquetePrecioBO;
import com.tsp.sct.dao.dto.SmsPaquetePrecio;
import java.sql.Connection;

/**
 *
 * @author ISCesar
 */
public class SmsCompraCreditosSesion {
    
    private int idPaquete = 0;
    private int creditos = 0;
    
    private Connection conn;

    public SmsCompraCreditosSesion() {
    }

    public SmsCompraCreditosSesion(Connection conn) {
        this.conn = conn;
    }
    
    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public int getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(int idPaquete) {
        this.idPaquete = idPaquete;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }
    
    public double calcularSubtotal(){
        double subtotal = 0;
        SmsPaquetePrecio paquetePrecio = null;
        SmsPaquetePrecioBO smsPaquetePrecioBO = new SmsPaquetePrecioBO(conn);
        try{
            if (idPaquete>0){
                paquetePrecio = smsPaquetePrecioBO.findSmsPaquetePreciobyId(idPaquete);
                if (paquetePrecio.getPaqueteCantidad()>0 && paquetePrecio.getPaquetePrecioTotal()>0)
                    subtotal = paquetePrecio.getPaquetePrecioTotal();
            }
            
            if (subtotal<=0){
                paquetePrecio = smsPaquetePrecioBO.findSmsPaquetePrecios(-1, 0, 0, " AND id_estatus=1 "
                        + "AND ( (rango_min<=" + creditos + " AND rango_max>=" + creditos +")"
                        + "     OR (rango_min<=" +creditos + " AND rango_max<=0) "
                        + ")")[0];
                idPaquete = paquetePrecio.getIdSmsPaquetePrecio();
                subtotal = creditos * paquetePrecio.getRangoPrecioUnitario();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return subtotal;
    }
    
    public double calcularIVA(){
        return calcularSubtotal() * 0.16;
    }
    
    public double calcularTotal(){
        return calcularSubtotal() + calcularIVA();
    }
    
}
