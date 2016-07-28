/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.MetasEmpleado;
import com.tsp.sct.dao.jdbc.MetasEmpleadoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author HpPyme
 */
public class MetasEmpleadoBO {
    
     private Connection conn = null;
     private MetasEmpleado metaEmpleado = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public MetasEmpleado getMetaEmpleado() {
        return metaEmpleado;
    }

    public void setMetaEmpleado(MetasEmpleado metaEmpleado) {
        this.metaEmpleado = metaEmpleado;
    }

    public MetasEmpleadoBO(Connection conn) {
         this.conn = conn;
    }

    /**
     * Constructor, crea un objeto User a tráves de su ID almacenado en la Base de Datos
     * @param idUser Identificador único autoincrementable de empleado
     */
    
    public MetasEmpleadoBO(int idMetaEmpleado, Connection conn) {
        this.conn = conn;
        try {
            metaEmpleado = new MetasEmpleadoDaoImpl(this.conn).findByPrimaryKey(idMetaEmpleado);
        } catch (Exception ex) {
          
            System.out.println("Se intento buscar una meta con el ID: " + idMetaEmpleado + " y no fue encontrada en la BD");
            ex.printStackTrace();
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
    public MetasEmpleado[] findMetasEmpleado(int idMetaVenta,int idEmpleado, long idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        MetasEmpleado[] metaEmpDto = new MetasEmpleado[0];
       MetasEmpleadoDaoImpl metaEmpDao = new MetasEmpleadoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idMetaVenta>0){
                sqlFiltro ="ID_META_VENTA=" + idMetaVenta + " AND ";
            }else{
                sqlFiltro ="ID_META_VENTA>0 AND ";
            }
            
            if (idEmpleado>0){
                sqlFiltro ="ID_EMPLEADO=" + idEmpleado + " AND ";
            }else{
                sqlFiltro ="ID_EMPLEADO>0 AND ";
            }
            
            if (idEmpresa>0){                
                sqlFiltro += "ID_EMPLEADO IN (SELECT ID_EMPLEADO FROM EMPLEADO WHERE ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")) ";
            }else{
                sqlFiltro += "ID_EMPLEADO IN (SELECT ID_EMPLEADO FROM EMPLEADO WHERE ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA > 0 )) ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            metaEmpDto = metaEmpDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY FECHA_INICIO DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return metaEmpDto;
    }
     
     
     
    
}
