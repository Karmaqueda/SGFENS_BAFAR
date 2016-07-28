/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.MetasVenta;
import com.tsp.sct.dao.jdbc.MetasVentaDaoImpl;
import java.sql.Connection;

/**
 *
 * @author HpPyme
 */
public class MetasVentaBO {
    
    MetasVenta metaVenta = null;
    private Connection conn = null;

    public MetasVenta getMetaVenta() {
        return metaVenta;
    }

    public void setMetaVenta(MetasVenta metaVenta) {
        this.metaVenta = metaVenta;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    

    public MetasVentaBO(Connection conn) {
         this.conn = conn;
    }

    public MetasVentaBO(int idMetaVenta ,Connection conn) {
        
        this.conn = conn;
        try{
            MetasVentaDaoImpl metaVentaDaoImpl = new MetasVentaDaoImpl(this.conn);
            this.metaVenta = metaVentaDaoImpl.findByPrimaryKey(idMetaVenta);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * Realiza una búsqueda por ID Marca en busca de
     * coincidencias
     * @param idMetaVenta ID de la meta para filtrar , -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public MetasVenta[] findMetasVenta(int idMetaVenta, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        MetasVenta[] metaDto = new MetasVenta[0];
        MetasVentaDaoImpl metaDao = new MetasVentaDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idMetaVenta>0){
                sqlFiltro ="ID_META_VENTA=" + idMetaVenta + " AND ";
            }else{
                sqlFiltro ="ID_META_VENTA>0 AND";
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
            
            metaDto = metaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_META_VENTA DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return metaDto;
    }
    
    
    
}
