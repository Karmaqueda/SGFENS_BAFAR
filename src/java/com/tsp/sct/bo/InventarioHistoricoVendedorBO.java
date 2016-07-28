/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.InventarioHistoricoVendedor;
import com.tsp.sct.dao.jdbc.InventarioHistoricoVendedorDaoImpl;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */

public class InventarioHistoricoVendedorBO {
    
    public String querySumaCantidad = "SELECT inv.ID_EMPLEADO, inv.ID_CONCEPTO, SUM(inv.CANTIDAD_ASIGNADA) AS CANTIDAD_ASIGNADA, SUM(inv.CANTIDAD_TERMINNO) AS CANTIDAD_TERMINNO, inv.FECHA_REGISTRO, inv.FECHA_INICIAL_CORTE, inv.FECHA_FINAL_CORTE, "
            + " concepto.NOMBRE_DESENCRIPTADO, concepto.IDENTIFICACION, concepto.FECHA_ALTA "
            + " FROM inventario_historico_vendedor inv, concepto ";
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public InventarioHistoricoVendedorBO(Connection conn) {
        this.conn = conn;
    }
    
    /**
     * Realiza una búsqueda por ID EmpleadoInventarioHistorico en busca de
     * coincidencias     
     * @param idEmpleado ID del empleado a filtrar inventario, -1 para evitar filtro
     * @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public InventarioHistoricoVendedor[] findInventarioHistoricoByIdEmpleado(int idEmpleado, int minLimit,int maxLimit, String filtroBusqueda) {
        InventarioHistoricoVendedor[] empleadoInventarioHistoricoDto = new InventarioHistoricoVendedor[0];
        InventarioHistoricoVendedorDaoImpl empleadoInventarioHistoricoDao = new InventarioHistoricoVendedorDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            
            if (idEmpleado>0){                
                sqlFiltro += " ID_EMPLEADO = " + idEmpleado + " ";
            }else{
                sqlFiltro +=" ID_EMPLEADO>0 ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            empleadoInventarioHistoricoDto = empleadoInventarioHistoricoDao.findByDynamicWhere( 
                    sqlFiltro
                    //+ " AND CANTIDAD > 0 "
                    + " ORDER BY CANTIDAD_ASIGNADA DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return empleadoInventarioHistoricoDto;
    }
    
}
