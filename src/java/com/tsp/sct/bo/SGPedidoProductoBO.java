/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SgfensCobranzaAbono;
import com.tsp.sct.dao.dto.SgfensPedidoProducto;
import com.tsp.sct.dao.jdbc.SgfensCobranzaAbonoDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensPedidoProductoDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author 578
 */
public class SGPedidoProductoBO {
    
    private Connection conn = null;
    private SgfensPedidoProducto sgfensPedidoProducto = null;
    

    public SGPedidoProductoBO(Connection conn) {
        this.conn = conn;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public SgfensPedidoProducto getSgfensPedidoProducto() {
        return sgfensPedidoProducto;
    }

    public void setSgfensPedidoProducto(SgfensPedidoProducto sgfensPedidoProducto) {
        this.sgfensPedidoProducto = sgfensPedidoProducto;
    }
    
    
    /**
     * Realiza una búsqueda por idPedido en busca de
     * coincidencias
     * @param idPedido ID del pedido para filtrar, -1 para mostrar todos los registros     
     * @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensCobranzaAbono
     */
    public SgfensPedidoProducto[] findByIdPedido(int idPedido, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SgfensPedidoProducto[] pedidoProductoDto = new SgfensPedidoProducto[0];
        SgfensPedidoProductoDaoImpl pedidoProductoDao = new  SgfensPedidoProductoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idPedido>0){
                sqlFiltro ="ID_PEDIDO =" + idPedido + " AND ";
            }else{
                sqlFiltro ="ID_PEDIDO>0 AND ";
            }            
            if (idEmpresa>0){
                sqlFiltro +="ID_PEDIDO IN (SELECT ID_PEDIDO FROM SGFENS_PEDIDO WHERE ID_EMPRESA IN  (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA=" + idEmpresa + " ))";
            }else{
                sqlFiltro +="ID_PEDIDO IN (SELECT ID_PEDIDO FROM SGFENS_PEDIDO WHERE ID_EMPRESA IN  (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE > 0 OR ID_EMPRESA > 0 ))";
            }
                       
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            


            pedidoProductoDto = pedidoProductoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_PEDIDO DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return pedidoProductoDto;
    }
    
    /**
     * Realiza una búsqueda por idPedido en busca de
     * coincidencias
     * @param idPedido ID del pedido para filtrar, -1 para mostrar todos los registros     
     * @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensCobranzaAbono
     */
    public int findCantidadByIdPedido(int idPedido, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        SgfensPedidoProductoDaoImpl pedidoProductoDao = new  SgfensPedidoProductoDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idPedido>0){
                sqlFiltro ="ID_PEDIDO =" + idPedido + " AND ";
            }else{
                sqlFiltro ="ID_PEDIDO>0 AND ";
            }            
            if (idEmpresa>0){
                sqlFiltro +="ID_PEDIDO IN (SELECT ID_PEDIDO FROM SGFENS_PEDIDO WHERE ID_EMPRESA IN  (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA=" + idEmpresa + " ))";
            }else{
                sqlFiltro +="ID_PEDIDO IN (SELECT ID_PEDIDO FROM SGFENS_PEDIDO WHERE ID_EMPRESA IN  (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE > 0 OR ID_EMPRESA > 0 ))";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as cantidad FROM " + pedidoProductoDao.getTableName() +  " WHERE " + 
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
    
    public SgfensPedidoProducto[] findByIdPedido(int idPedido, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda, int identificadorMasMenosVendidos, String ascDesc) {
        SgfensPedidoProducto[] pedidoProductoDto = new SgfensPedidoProducto[0];
        SgfensPedidoProductoDaoImpl pedidoProductoDao = new  SgfensPedidoProductoDaoImpl(this.conn);
        
        if(identificadorMasMenosVendidos == 0){
        
            try {
                String sqlFiltro="";
                if (idPedido>0){
                    sqlFiltro ="ID_PEDIDO =" + idPedido + " AND ";
                }else{
                    sqlFiltro ="ID_PEDIDO>0 AND ";
                }            
                if (idEmpresa>0){
                    sqlFiltro +="ID_PEDIDO IN (SELECT ID_PEDIDO FROM SGFENS_PEDIDO WHERE ID_EMPRESA IN  (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA=" + idEmpresa + " ))";
                }else{
                    sqlFiltro +="ID_PEDIDO IN (SELECT ID_PEDIDO FROM SGFENS_PEDIDO WHERE ID_EMPRESA IN  (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE > 0 OR ID_EMPRESA > 0 ))";
                }

                if (!filtroBusqueda.trim().equals("")){
                    sqlFiltro += filtroBusqueda;
                }

                if (minLimit<0)
                    minLimit=0;

                String sqlLimit="";
                if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                    sqlLimit = " LIMIT " + minLimit + "," + maxLimit;



                pedidoProductoDto = pedidoProductoDao.findByDynamicWhere( 
                        sqlFiltro
                        + " ORDER BY ID_PEDIDO DESC"
                        + sqlLimit
                        , new Object[0]);

            } catch (Exception ex) {
                System.out.println("Error de consulta a Base de Datos: " + ex.toString());
                ex.printStackTrace();
            }
        }else{
             try {
                //String sqlFiltro="SELECT ID_PEDIDO, ID_CONCEPTO , DESCRIPCION, UNIDAD, IDENTIFICACION, SUM(CANTIDAD) AS CANTIDAD, PRECIO_UNITARIO, DESCUENTO_PORCENTAJE, DESCUENTO_MONTO, SUBTOTAL, SUM(COSTO_UNITARIO) AS COSTO_UNITARIO, PORCENTAJE_COMISION_EMPLEADO, CANTIDAD_ENTREGADA, FECHA_ENTREGA,ESTATUS,id_Almacen_Origen,PESO_UNITARIO,CANTIDAD_PESO,CANTIDAD_ENTREGADA_PESO,PRECIO_UNITARIO_GRANEL"
                        //+ " FROM sgfens_pedido_producto WHERE ";
                String sqlFiltro="SELECT ID_PEDIDO, ID_CONCEPTO , DESCRIPCION, UNIDAD, IDENTIFICACION, SUM(CANTIDAD) AS CANTIDAD, PRECIO_UNITARIO, DESCUENTO_PORCENTAJE, DESCUENTO_MONTO, SUBTOTAL, COSTO_UNITARIO, PORCENTAJE_COMISION_EMPLEADO, CANTIDAD_ENTREGADA, FECHA_ENTREGA,ESTATUS,id_Almacen_Origen,PESO_UNITARIO,CANTIDAD_PESO,CANTIDAD_ENTREGADA_PESO,PRECIO_UNITARIO_GRANEL"
                        + " FROM sgfens_pedido_producto WHERE ";

                if (idPedido>0){
                    sqlFiltro +="ID_PEDIDO =" + idPedido + " AND ";
                }else{
                    sqlFiltro +="ID_PEDIDO>0 AND ";
                }            
                if (idEmpresa>0){
                    sqlFiltro +="ID_PEDIDO IN (SELECT ID_PEDIDO FROM SGFENS_PEDIDO WHERE ID_EMPRESA IN  (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA=" + idEmpresa + " ))";
                }else{
                    sqlFiltro +="ID_PEDIDO IN (SELECT ID_PEDIDO FROM SGFENS_PEDIDO WHERE ID_EMPRESA IN  (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE > 0 OR ID_EMPRESA > 0 ))";
                }

                if (!filtroBusqueda.trim().equals("")){
                    sqlFiltro += filtroBusqueda;
                }

                if (minLimit<0)
                    minLimit=0;

                String sqlLimit="";
                if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                    sqlLimit = " LIMIT " + minLimit + "," + maxLimit;


                pedidoProductoDto = pedidoProductoDao.findByDynamicSelect(sqlFiltro
                        //+ " GROUP BY ID_CONCEPTO "
                        + " GROUP BY ID_CONCEPTO, COSTO_UNITARIO "
                        + "ORDER BY CANTIDAD " + ascDesc
                        //+ "LIMIT 0,10"
                        +sqlLimit
                        , new Object[0]);

                /*pedidoProductoDto = pedidoProductoDao.findByDynamicWhere( 
                        sqlFiltro
                        + " ORDER BY ID_PEDIDO DESC"
                        + sqlLimit
                        , new Object[0]);*/

            } catch (Exception ex) {
                System.out.println("Error de consulta a Base de Datos: " + ex.toString());
                ex.printStackTrace();
            }
        }
        
        return pedidoProductoDto;
    }
        
    /**
     * Realiza una búsqueda por idConcepto en busca de
     * coincidencias
     * @param idConcepto ID del concepto para filtrar, -1 para mostrar todos los registros     
     * @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensCobranzaAbono
     */
    public SgfensPedidoProducto[] findByIdConcepto(int idConcepto, int minLimit,int maxLimit, String filtroBusqueda) {
        SgfensPedidoProducto[] pedidoProductoDto = new SgfensPedidoProducto[0];
        SgfensPedidoProductoDaoImpl pedidoProductoDao = new  SgfensPedidoProductoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idConcepto>0){
                sqlFiltro ="ID_CONCEPTO =" + idConcepto + " AND ";
            }else{
                sqlFiltro ="ID_CONCEPTO>0 AND";
            }
                       
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            


            pedidoProductoDto = pedidoProductoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_CONCEPTO DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return pedidoProductoDto;
    }
    
       
    
}
