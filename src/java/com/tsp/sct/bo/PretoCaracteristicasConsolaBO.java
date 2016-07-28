/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.PretoCaracteristicasConsola;
import com.tsp.sct.dao.jdbc.PretoCaracteristicasConsolaDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class PretoCaracteristicasConsolaBO {
    
    private PretoCaracteristicasConsola pretoCaracteristicasConsola;
    
    private PretoCaracteristicasConsola[] pretoCaracteristicasDeConsola;
    
    public static final int TPV_GRATIS = 1;
    public static final int TPV_EMPRENDEDOR = 2;
    public static final int TPV_COMERCIANTE = 3;
    public static final int TPV_MIPYME = 4;
    public static final int EVC = 5;
    public static final int PRETORIANO_ERP = 6;
    public static final int CBB = 7;
    public int tpv_gratis = 0;
    public int tpv_emprendedor = 0;
    public int tpv_comerciante = 0;
    public int tpv_mipyme = 0;
    public int evc = 0;
    public int pretoriano_erp = 0;
    public int cbb = 0;
    private Connection conn = null;

    public int getTpv_gratis() {
        return tpv_gratis;
    }
    public void setTpv_gratis(int tpv_gratis) {
        this.tpv_gratis = tpv_gratis;
    }
    public int getTpv_emprendedor() {
        return tpv_emprendedor;
    }    
    public void setTpv_emprendedor(int tpv_emprendedor) {
        this.tpv_emprendedor = tpv_emprendedor;
    }
    public int getTpv_comerciante() {
        return tpv_comerciante;
    }
    public void setTpv_comerciante(int tpv_comerciante) {
        this.tpv_comerciante = tpv_comerciante;
    }
    public int getTpv_mipyme() {
        return tpv_mipyme;
    }
    public void setTpv_mipyme(int tpv_mipyme) {
        this.tpv_mipyme = tpv_mipyme;
    }
    public int getEvc() {
        return evc;
    }
    public void setEvc(int evc) {
        this.evc = evc;
    }
    public int getPretoriano_erp() {
        return pretoriano_erp;
    }
    public void setPretoriano_erp(int pretoriano_erp) {
        this.pretoriano_erp = pretoriano_erp;
    }

    public int getCbb() {
        return cbb;
    }

    public void setCbb(int cbb) {
        this.cbb = cbb;
    }
    
    
    
    public void recuperadorCaracteristicasPorPaquete(int productoAdquirido){
        if(productoAdquirido == TPV_GRATIS){            
        }else if(productoAdquirido == TPV_EMPRENDEDOR){            
        }else if(productoAdquirido == TPV_COMERCIANTE){            
        }else if(productoAdquirido == TPV_MIPYME){            
        }else if(productoAdquirido == EVC){            
        }else if(productoAdquirido == PRETORIANO_ERP){            
        }else{            
        }
    }

    public PretoCaracteristicasConsola getPretoCaracteristicasConsola() {
        return pretoCaracteristicasConsola;
    }

    public void setPretoCaracteristicasConsola(PretoCaracteristicasConsola pretoCaracteristicasConsola) {
        this.pretoCaracteristicasConsola = pretoCaracteristicasConsola;
    }
    
    
    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public PretoCaracteristicasConsolaBO(Connection conn){
        this.conn = conn;
    }
    
    public PretoCaracteristicasConsolaBO(String nombreCaracteristica, Connection conn){
        this.conn = conn;
        int idCaracteristica = 0;
        if(nombreCaracteristica.equals("PRODUCTO ADQUIRIDO")){
            idCaracteristica = 1;
        }else if(nombreCaracteristica.equals("CERTIFICADOS PARA CFDI")){
            idCaracteristica = 2;
        }else if(nombreCaracteristica.equals("LOGO")){
            idCaracteristica = 3;
        }else if(nombreCaracteristica.equals("USUARIOS")){
            idCaracteristica = 4;
        }else if(nombreCaracteristica.equals("SUCURSALES")){
            idCaracteristica = 5;
        }else if(nombreCaracteristica.equals("DISPOSITIVOS")){
            idCaracteristica = 6;
        }else if(nombreCaracteristica.equals("GATEWAY DE PAGOS")){
            idCaracteristica = 7;
        }else if(nombreCaracteristica.equals("LICENCIAS DE USUARIOS MOVILES")){
            idCaracteristica = 8;
        }else if(nombreCaracteristica.equals("PERSONALIZACION DE PLANTILLA DE BOUCHER Y PEDIDOS")){
            idCaracteristica = 9;
        }else if(nombreCaracteristica.equals("CLIENTES")){
            idCaracteristica = 10;
        }else if(nombreCaracteristica.equals("IMPUESTOS")){
            idCaracteristica = 11;
        }else if(nombreCaracteristica.equals("PROVEEDORES")){
            idCaracteristica = 12;
        }else if(nombreCaracteristica.equals("ALMACENES")){
            idCaracteristica = 13;
        }else if(nombreCaracteristica.equals("CATEGORIAS")){
            idCaracteristica = 14;
        }else if(nombreCaracteristica.equals("EMBALAJES")){
            idCaracteristica = 15;
        }else if(nombreCaracteristica.equals("MARCAS")){
            idCaracteristica = 16;
        }else if(nombreCaracteristica.equals("MOVIMIENTOS")){
            idCaracteristica = 17;
        }else if(nombreCaracteristica.equals("PRODUCTOS")){
            idCaracteristica = 18;
        }else if(nombreCaracteristica.equals("SERVICIOS")){
            idCaracteristica = 19;
        }else if(nombreCaracteristica.equals("COBRANZA")){
            idCaracteristica = 20;
        }else if(nombreCaracteristica.equals("COTIZACIONES")){
            idCaracteristica = 21;
        }else if(nombreCaracteristica.equals("PEDIDOS")){
            idCaracteristica = 22;
        }else if(nombreCaracteristica.equals("PROSPECTOS")){
            idCaracteristica = 23;
        }else if(nombreCaracteristica.equals("FACTURACION CFDI")){
            idCaracteristica = 24;
        }else if(nombreCaracteristica.equals("FACTURACION CBB")){
            idCaracteristica = 25;
        }else if(nombreCaracteristica.equals("DASHBOARD")){
            idCaracteristica = 26;
        }else if(nombreCaracteristica.equals("MENSAJERIA")){
            idCaracteristica = 27;
        }else if(nombreCaracteristica.equals("SEGUIMIENTO")){
            idCaracteristica = 28;
        }else if(nombreCaracteristica.equals("VISOR DE MAPAS")){
            idCaracteristica = 29;
        }else if(nombreCaracteristica.equals("HISTORIAL DE GEOPOSICION")){
            idCaracteristica = 30;
        }else if(nombreCaracteristica.equals("GEOCERCAS")){
            idCaracteristica = 31;
        }else if(nombreCaracteristica.equals("CREACION DE RUTAS")){
            idCaracteristica = 32;
        }else if(nombreCaracteristica.equals("REPORTES")){
            idCaracteristica = 33;
        }else if(nombreCaracteristica.equals("COBRO")){
            idCaracteristica = 34;
        }else if(nombreCaracteristica.equals("CANCELACION")){
            idCaracteristica = 35;
        }else if(nombreCaracteristica.equals("VISTA DE MOVIMIENTOS")){
            idCaracteristica = 36;
        }else if(nombreCaracteristica.equals("RENTA MENSUAL")){
            idCaracteristica = 37;
        }else {
            System.out.println("NO HAY CARACTERISTICA CON ESE NOMBRE !!!!!!!!");
        }
        
        try{
            PretoCaracteristicasConsolaDaoImpl pretoCaracteristicasConsolaDaoImpl = new PretoCaracteristicasConsolaDaoImpl(this.conn);
            this.pretoCaracteristicasConsola = pretoCaracteristicasConsolaDaoImpl.findByPrimaryKey(idCaracteristica);            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public PretoCaracteristicasConsola[] findPrestoCaracteristicas(){
        PretoCaracteristicasConsola[] caracteristicasConsolas = new PretoCaracteristicasConsola[0];
        PretoCaracteristicasConsolaDaoImpl caracteristicasConsolaDaoImpl = new PretoCaracteristicasConsolaDaoImpl(this.conn);
        
        try {
            String sqlFiltro="";
                sqlFiltro = "ID_CARACTERISTICA > 0 ";            
            caracteristicasConsolas = caracteristicasConsolaDaoImpl.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_CARACTERISTICA ASC"                    
                    , new Object[0]);            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }        
        return caracteristicasConsolas;
    }
    
    public PretoCaracteristicasConsola[] findPrestoCaracteristicasPaquete(int productoAdquirido){
        PretoCaracteristicasConsola[] caracteristicasConsolas = new PretoCaracteristicasConsola[0];
        PretoCaracteristicasConsolaDaoImpl caracteristicasConsolaDaoImpl = new PretoCaracteristicasConsolaDaoImpl(this.conn);
        try{
            String sqlFiltro="";
            if(productoAdquirido == TPV_GRATIS){                
                sqlFiltro = "SELECT ID_CARACTERISTICA, NOMBRE_CARACTERISTICA, TPV_GRATIS";
            }else if(productoAdquirido == TPV_EMPRENDEDOR){
                sqlFiltro = "SELECT ID_CARACTERISTICA, NOMBRE_CARACTERISTICA, TPV_EMPRENDEDOR";
            }else if(productoAdquirido == TPV_COMERCIANTE){
                sqlFiltro = "SELECT ID_CARACTERISTICA, NOMBRE_CARACTERISTICA, TPV_COMERCIANTE";
            }else if(productoAdquirido == TPV_MIPYME){
                sqlFiltro = "SELECT ID_CARACTERISTICA, NOMBRE_CARACTERISTICA, TPV_MIPYME";
            }else if(productoAdquirido == EVC){
                sqlFiltro = "SELECT ID_CARACTERISTICA, NOMBRE_CARACTERISTICA, EVC";
            }else if(productoAdquirido == PRETORIANO_ERP){
                sqlFiltro = "SELECT ID_CARACTERISTICA, NOMBRE_CARACTERISTICA, PRETORIANO_ERP";
            }else if(productoAdquirido == CBB){
                sqlFiltro = "SELECT ID_CARACTERISTICA, NOMBRE_CARACTERISTICA, CBB";
            }else{
                sqlFiltro = "";
            }   
            sqlFiltro = sqlFiltro + " FROM PRETO_CARACTERISTICAS_CONSOLA ";
            caracteristicasConsolas = caracteristicasConsolaDaoImpl.findByDynamicSelect(sqlFiltro, null);
        }catch(Exception e){
            System.out.println("Error de consulta en la Base de Datos para obtener los datos del paquete adquirido: "+e.toString());
            e.printStackTrace();
        }
        
        return caracteristicasConsolas;
    }
    
}
