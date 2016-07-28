/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.HistorialMovil;
import com.tsp.sct.dao.jdbc.HistorialMovilDaoImpl;
import java.sql.Connection;

/**
 *
 * @author 578
 */
public class HistorialMovilBO {
    
    
    private HistorialMovil historialMovil = null;
    private Connection conn = null;

    public HistorialMovil getHistorialMovil() {
        return historialMovil;
    }

    public void setHistorialMovil(HistorialMovil historialMovil) {
        this.historialMovil = historialMovil;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public HistorialMovilBO(Connection conn) {        
         this.conn = conn;
    }
    
    
    /**
     * Realiza una búsqueda por IDMPRESA e IMEI en busca de
     * coincidencias
     * @param idEmpresa ID de la emnpresa para filtrar, -1 para mostrar todos los registros
     * @param imei imei del dispositivo a filtrar empleadoAgendas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO EmpleadoAgenda
     */
    public HistorialMovil[] findLogs(int idEmpresa, String imei, int minLimit,int maxLimit, String filtroBusqueda) {
        HistorialMovil[] historialMovilDto = new HistorialMovil[0];
        HistorialMovilDaoImpl historialMovilDao = new HistorialMovilDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idEmpresa>0){
                sqlFiltro ="ID_EMPRESA=" + idEmpresa + " AND ";
            }else{
                sqlFiltro ="ID_EMPRESA>0 AND";
            }
            if (!imei.trim().equals("")){                
                sqlFiltro += " IMEI = '" + imei + "' ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            historialMovilDto = historialMovilDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_HISTORIAL_MOVIL DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return historialMovilDto;
    }
    
    
}
