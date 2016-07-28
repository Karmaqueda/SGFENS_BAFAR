/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SgfensPedidoEntrega;
import com.tsp.sct.dao.jdbc.SgfensPedidoEntregaDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar
 */
public class SGPedidoEntregaBO {
    private SgfensPedidoEntrega pedidoEntrega = null;

    public SgfensPedidoEntrega getPedidoEntrega() {
        return pedidoEntrega;
    }

    public void setPedidoEntrega(SgfensPedidoEntrega pedidoEntrega) {
        this.pedidoEntrega = pedidoEntrega;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SGPedidoEntregaBO(Connection conn){
        this.conn = conn;
    }
    
    public SGPedidoEntregaBO(int idSgfensPedidoEntrega, Connection conn){        
        this.conn = conn;
        try{
            SgfensPedidoEntregaDaoImpl sgfensPedidoEntregaDaoImpl = new SgfensPedidoEntregaDaoImpl(this.conn);
            this.pedidoEntrega = sgfensPedidoEntregaDaoImpl.findByPrimaryKey(idSgfensPedidoEntrega);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public SgfensPedidoEntrega findPedidoEntregabyId(int idPedidoEntrega) throws Exception{
        SgfensPedidoEntrega pedidoEntrega = null;
        
        try{
            SgfensPedidoEntregaDaoImpl pedidoPedidoEntregaDao = new SgfensPedidoEntregaDaoImpl(this.conn);
            pedidoEntrega = pedidoPedidoEntregaDao.findByPrimaryKey(idPedidoEntrega);
            if(pedidoEntrega == null){
                throw new Exception("No se encontro ninguna pedidoEntrega o cambio que corresponda según los parámetros específicados.");
            }
            if(pedidoEntrega.getIdPedidoEntrega()<= 0){
                throw new Exception("No se encontro ninguna pedidoEntrega o cambio que corresponda según los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de PedidoEntrega del usuario. Error: " + e.getMessage());
        }
        
        return pedidoEntrega;
    }
    
    /*
    Metodo para obetener un array de cambio/pedidoEntrega de un pedido
    */
    public SgfensPedidoEntrega[] findCambioDevByIdPedido(int idPedido, Connection conn) throws Exception{
        SgfensPedidoEntrega[] pedidoEntregaes = null;
        
        try{
            SgfensPedidoEntregaDaoImpl pedidoPedidoEntregaDao = new SgfensPedidoEntregaDaoImpl(this.conn);
            pedidoEntregaes = pedidoPedidoEntregaDao.findByDynamicWhere("ID_PEDIDO = "+idPedido,null);
            if(pedidoEntregaes == null){
                throw new Exception("No se encontro ninguna pedidoEntrega o cambio que corresponda según los parámetros específicados.");
            }           
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de PedidoEntrega del usuario. Error: " + e.getMessage());
        }
        
        return pedidoEntregaes;
    }
    
    
    /*
    Metodo para obtener un array de cambio/pedidoEntrega de un pedido
    */
    
    public SgfensPedidoEntrega[] findEntregaByPedido(int idPedido , Connection conn) throws Exception{
        SgfensPedidoEntrega[] pedidoEntregaes = null;
        
        try{
            SgfensPedidoEntregaDaoImpl pedidoPedidoEntregaDao = new SgfensPedidoEntregaDaoImpl(this.conn);
            pedidoEntregaes = pedidoPedidoEntregaDao.findByDynamicWhere("ID_PEDIDO = "+idPedido + " AND ID_ESTATUS = 1 " ,null);
            if(pedidoEntregaes == null){
                throw new Exception("No se encontro ninguna pedidoEntrega o cambio que corresponda según los parámetros específicados.");
            }           
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de PedidoEntrega del usuario. Error: " + e.getMessage());
        }
        
        return pedidoEntregaes;
    }
    
    
    
     /*
    Metodo para obtener un array de cambio/pedidoEntrega de un pedido
    */
    
    public SgfensPedidoEntrega[] findCambioEntregaByVendedor(Connection conn, int idUsuarioVendedor , String filtroBusqueda ) throws Exception{
        SgfensPedidoEntrega[] pedidoEntregaes = null;
        
        try{
            SgfensPedidoEntregaDaoImpl pedidoPedidoEntregaDao = new SgfensPedidoEntregaDaoImpl(this.conn);
            pedidoEntregaes = pedidoPedidoEntregaDao.findByDynamicWhere("id_usuario_vendedor = "+idUsuarioVendedor + "  " + filtroBusqueda ,null);
            if(pedidoEntregaes == null){
                throw new Exception("No se encontro ninguna pedidoEntrega o cambio que corresponda según los parámetros específicados.");
            }           
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de PedidoEntrega del usuario. Error: " + e.getMessage());
        }
        
        return pedidoEntregaes;
    }
    
    /**
     * Realiza una búsqueda por ID SgfensPedidoEntrega en busca de
     * coincidencias
     * @param idSgfensPedidoEntrega ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar sgfensPedidoEntregas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensPedidoEntrega
     */
    public SgfensPedidoEntrega[] findSgfensPedidoEntregas(int idSgfensPedidoEntrega, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SgfensPedidoEntrega[] sgfensPedidoEntregaDto = new SgfensPedidoEntrega[0];
        SgfensPedidoEntregaDaoImpl sgfensPedidoEntregaDao = new SgfensPedidoEntregaDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSgfensPedidoEntrega>0){
                sqlFiltro ="id_pedido_entrega=" + idSgfensPedidoEntrega + " AND ";
            }else{
                sqlFiltro ="id_pedido_entrega>0 AND";
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
            
            sgfensPedidoEntregaDto = sgfensPedidoEntregaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY id_pedido_entrega DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return sgfensPedidoEntregaDto;
    }
    
    /**
     * Realiza una búsqueda por ID SgfensPedidoEntrega en busca de
     * coincidencias
     * @param idSgfensPedidoEntrega ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar sgfensPedidoEntregas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensPedidoEntrega
     */
    public int findCantidadSgfensPedidoEntregas(int idSgfensPedidoEntrega, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        try {
            String sqlFiltro="";
            if (idSgfensPedidoEntrega>0){
                sqlFiltro ="id_pedido_entrega=" + idSgfensPedidoEntrega + " AND ";
            }else{
                sqlFiltro ="id_pedido_entrega>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_pedido_entrega) as cantidad FROM sgfens_pedido_entrega WHERE " + 
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
