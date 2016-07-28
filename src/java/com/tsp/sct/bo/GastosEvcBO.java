/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.GastosEvc;
import com.tsp.sct.dao.jdbc.GastosEvcDaoImpl;
import java.sql.Connection;

/**
 *
 * @author HpPyme
 */
public class GastosEvcBO {
    
    
    private Connection conn = null;
    private GastosEvc gastos = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public GastosEvc getGastos() {
        return gastos;
    }

    public void setGastos(GastosEvc gastos) {
        this.gastos = gastos;
    }

    public GastosEvcBO(Connection conn) {
        this.conn = conn;
    }

    public GastosEvcBO(int idGastosEvc, Connection conn){ 
        this.conn = conn;
        try{
            GastosEvcDaoImpl gastosEvcDaoImpl = new GastosEvcDaoImpl(this.conn);
            this.gastos = gastosEvcDaoImpl.findByPrimaryKey(idGastosEvc);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
  
    /**
     * Realiza una búsqueda por ID Usuario en busca de
     * coincidencias
     * @param idGastos ID Del Gatos para filtrar, -1 para mostrar todos los registros     
     * @param idEmpresa ID de la Empresa para filtrar, -1 para mostrar todos los registros 
     * @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO GastosEvc
     */
    public GastosEvc[] findGastosEvc(int idGastos, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        GastosEvc[] gastosEvcDto = new GastosEvc[0];
        GastosEvcDaoImpl gastosEvcDao = new GastosEvcDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idGastos>0){
                sqlFiltro =" ID_GASTOS =" + idGastos + "  AND ";
            }else{
                sqlFiltro =" ID_GASTOS > 0 AND ";
            }            
            
            if (idEmpresa>0){                
                sqlFiltro += " (ID_EMPLEADO IN (SELECT ID_EMPLEADO FROM empleado WHERE ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ") )"
                        + " OR (ID_EMPLEADO IS NULL AND ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + "))) ";
            }else{
                sqlFiltro +=" (ID_EMPLEADO IN (SELECT ID_EMPLEADO FROM empleado WHERE ID_EMPRESA > 0 ) OR (ID_EMPLEADO IS NULL AND ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA > 0 ))) ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            gastosEvcDto = gastosEvcDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_GASTOS DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return gastosEvcDto;
    }
    
    
}
