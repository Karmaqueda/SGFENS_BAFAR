/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SgfensPedidoDevolucionCambio;
import com.tsp.sct.dao.jdbc.SgfensPedidoDevolucionCambioDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author MOVILPYME
 */
public class SGPedidoDevolucionesCambioBO {
    private SgfensPedidoDevolucionCambio devolucion = null;

    public static int ID_CLASIFICACION_NO_SOLICITADO_CLIENTE = 1;
    public static int ID_CLASIFICACION_NO_VENDIDO = 2;
    public static int ID_CLASIFICACION_OTRO = 3;
    public static int ID_CLASIFICACION_CADUCO = 4;
    public static int ID_CLASIFICACION_MAL_ESTADO = 5;
    public static int ID_CLASIFICACION_SOLICITADO_CLIENTE = 6;

    public static int ID_TIPO_DEVOLUCION = 1;
    public static int ID_TIPO_CAMBIO = 2;
        
    /**
     * @return the devolucion
     */
    public SgfensPedidoDevolucionCambio getSgfensPedidoDevolucionCambio() {
        return devolucion;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SGPedidoDevolucionesCambioBO(Connection conn){
        this.conn = conn;
    }
    
    public SGPedidoDevolucionesCambioBO(int idSgfensPedidoDevolucionCambio, Connection conn){        
        this.conn = conn;
        try{
            SgfensPedidoDevolucionCambioDaoImpl SgfensProspectoDaoImpl = new SgfensPedidoDevolucionCambioDaoImpl(this.conn);
            this.devolucion = SgfensProspectoDaoImpl.findByPrimaryKey(idSgfensPedidoDevolucionCambio);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public SgfensPedidoDevolucionCambio findProspectobyId(int idPedidoDevolucionCambio) throws Exception{
        SgfensPedidoDevolucionCambio devolucion = null;
        
        try{
            SgfensPedidoDevolucionCambioDaoImpl pedidoDevolucionCambioDao = new SgfensPedidoDevolucionCambioDaoImpl(this.conn);
            devolucion = pedidoDevolucionCambioDao.findByPrimaryKey(idPedidoDevolucionCambio);
            if(devolucion == null){
                throw new Exception("No se encontro ninguna devolución o cambio que corresponda según los parámetros específicados.");
            }
            if(devolucion.getIdPedidoDevolCambio() <= 0){
                throw new Exception("No se encontro ninguna devolución o cambio que corresponda según los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de Prospecto del usuario. Error: " + e.getMessage());
        }
        
        return devolucion;
    }
    
    /*
    Metodo para obetener un array de cambio/devolucion de un pedido
    */
    
    public SgfensPedidoDevolucionCambio[] findCambioDevByIdPedido(int idPedido, int tipoMovimiento, Connection conn) throws Exception{
        SgfensPedidoDevolucionCambio[] devoluciones = null;
        
        try{
            SgfensPedidoDevolucionCambioDaoImpl pedidoDevolucionCambioDao = new SgfensPedidoDevolucionCambioDaoImpl(this.conn);
            devoluciones = pedidoDevolucionCambioDao.findByDynamicWhere("ID_PEDIDO = "+idPedido+ " AND ID_TIPO =" + tipoMovimiento,null);
            if(devoluciones == null){
                throw new Exception("No se encontro ninguna devolución o cambio que corresponda según los parámetros específicados.");
            }           
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de Prospecto del usuario. Error: " + e.getMessage());
        }
        
        return devoluciones;
    }
    
    
    /*
    Metodo para obtener un array de cambio/devolucion de un pedido
    */
    
    public SgfensPedidoDevolucionCambio[] findCambioDevByIdPedido(int idPedido , Connection conn) throws Exception{
        SgfensPedidoDevolucionCambio[] devoluciones = null;
        
        try{
            SgfensPedidoDevolucionCambioDaoImpl pedidoDevolucionCambioDao = new SgfensPedidoDevolucionCambioDaoImpl(this.conn);
            devoluciones = pedidoDevolucionCambioDao.findByDynamicWhere("ID_PEDIDO = "+idPedido + " AND ID_ESTATUS = 1 " ,null);
            if(devoluciones == null){
                throw new Exception("No se encontro ninguna devolución o cambio que corresponda según los parámetros específicados.");
            }           
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de Prospecto del usuario. Error: " + e.getMessage());
        }
        
        return devoluciones;
    }
    
    
    
     /*
    Metodo para obtener un array de cambio/devolucion de un pedido
    */
    
    public SgfensPedidoDevolucionCambio[] findCambioDevByEmpleado(Connection conn, int idEmpleado , String filtroBusqueda ) throws Exception{
        SgfensPedidoDevolucionCambio[] devoluciones = null;
        
        try{
            SgfensPedidoDevolucionCambioDaoImpl pedidoDevolucionCambioDao = new SgfensPedidoDevolucionCambioDaoImpl(this.conn);
            devoluciones = pedidoDevolucionCambioDao.findByDynamicWhere("ID_EMPLEADO = "+idEmpleado + "  " + filtroBusqueda ,null);
            if(devoluciones == null){
                throw new Exception("No se encontro ninguna devolución o cambio que corresponda según los parámetros específicados.");
            }           
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de Prospecto del usuario. Error: " + e.getMessage());
        }
        
        return devoluciones;
    }
    
    /**
     * Realiza una búsqueda por ID SgfensPedidoDevolucionCambio en busca de
     * coincidencias
     * @param idSgfensPedidoDevolucionCambio ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar sgfensPedidoDevolucionCambios, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensPedidoDevolucionCambio
     */
    public SgfensPedidoDevolucionCambio[] findSgfensPedidoDevolucionCambios(int idSgfensPedidoDevolucionCambio, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SgfensPedidoDevolucionCambio[] sgfensPedidoDevolucionCambioDto = new SgfensPedidoDevolucionCambio[0];
        SgfensPedidoDevolucionCambioDaoImpl sgfensPedidoDevolucionCambioDao = new SgfensPedidoDevolucionCambioDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSgfensPedidoDevolucionCambio>0){
                sqlFiltro ="ID_PEDIDO_DEVOL_CAMBIO=" + idSgfensPedidoDevolucionCambio + " AND ";
            }else{
                sqlFiltro ="ID_PEDIDO_DEVOL_CAMBIO>0 AND";
            }
            if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro +=" ID_EMPRESA>0";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            sgfensPedidoDevolucionCambioDto = sgfensPedidoDevolucionCambioDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_PEDIDO_DEVOL_CAMBIO DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return sgfensPedidoDevolucionCambioDto;
    }
    
    /**
     * Realiza una búsqueda por ID SgfensPedidoDevolucionCambio en busca de
     * coincidencias
     * @param idSgfensPedidoDevolucionCambio ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar sgfensPedidoDevolucionCambios, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensPedidoDevolucionCambio
     */
    public int findCantidadSgfensPedidoDevolucionCambios(int idSgfensPedidoDevolucionCambio, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        try {
            String sqlFiltro="";
            if (idSgfensPedidoDevolucionCambio>0){
                sqlFiltro ="ID_PEDIDO_DEVOL_CAMBIO=" + idSgfensPedidoDevolucionCambio + " AND ";
            }else{
                sqlFiltro ="ID_PEDIDO_DEVOL_CAMBIO>0 AND";
            }
            if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro +=" ID_EMPRESA>0";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(ID_PEDIDO_DEVOL_CAMBIO) as cantidad FROM sgfens_pedido_devolucion_cambio WHERE " + 
                    sqlFiltro
                    + sqlLimit);
            if(rs.next()){
                cantidad = rs.getInt("cantidad");
            }
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return cantidad;
    }
}
