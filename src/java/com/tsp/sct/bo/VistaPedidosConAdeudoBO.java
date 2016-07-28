/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.VistaPedidosConAdeudos;
import com.tsp.sct.dao.jdbc.VistaPedidosConAdeudosDaoImpl;
import java.sql.Connection;

/**
 *
 * @author 578
 */
public class VistaPedidosConAdeudoBO {
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public VistaPedidosConAdeudoBO(Connection conn) {
        this.conn = conn;
    }
    
    /**
     * Realiza una búsqueda por ID SgfensPedido en busca de
     * coincidencias
     * @param idSgfensPedido ID De la pedido para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar pedido, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensPedido
     */
    public VistaPedidosConAdeudos[] findPedidosConAdeudo(int idSgfensPedido, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        VistaPedidosConAdeudos[] pedidosDto = new VistaPedidosConAdeudos[0];
        VistaPedidosConAdeudosDaoImpl pedidoDao = new VistaPedidosConAdeudosDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSgfensPedido>0){
                sqlFiltro ="ID_PEDIDO=" + idSgfensPedido + " AND ";
            }else{
                sqlFiltro ="ID_PEDIDO>0 AND ";
            }
            if (idEmpresa>0){
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ") ";
            }else{
                sqlFiltro +=" ID_EMPRESA>0 ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            pedidosDto = pedidoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY FECHA_ULTIMO_ABONO ASC, FECHA_PEDIDO "
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return pedidosDto;
    }
    
}
