/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Cronometro;
import com.tsp.sct.dao.jdbc.CronometroDaoImpl;
import java.sql.Connection;

/**
 *
 * @author HpPyme
 */
public class CronometroBO {
    
    private Connection conn = null;
    private Cronometro cronometro = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Cronometro getCronometro() {
        return cronometro;
    }

    public void setCronometro(Cronometro cronometro) {
        this.cronometro = cronometro;
    }

    public CronometroBO(Connection conn) {
         this.conn = conn;
    }

    public CronometroBO(Connection conn, int idCronometro){
        this.conn = conn;
        try{
            CronometroDaoImpl cronometroDaoImpl = new CronometroDaoImpl(this.conn);
            this.cronometro = cronometroDaoImpl.findByPrimaryKey(idCronometro);            
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    
    /**
     * Realiza una búsqueda por ID Usuario en busca de
     * coincidencias
     * @param idCronometro ID Del Cronometro para filtrar, -1 para mostrar todos los registros     
     * @param idEmpresa ID de la Empresa para filtrar, -1 para mostrar todos los registros 
     * @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO GastosEvc
     */
    public Cronometro[] findCronometros(int idCronometro, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        Cronometro[] cronometroDto = new Cronometro[0];
        CronometroDaoImpl cronometroDao = new CronometroDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCronometro>0){
                sqlFiltro =" ID_CRONOMETRO =" + idCronometro + "  AND ";
            }else{
                sqlFiltro =" ID_CRONOMETRO > 0 AND ";
            }            
            
            if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro +=" ID_EMPRESA > 0  ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            cronometroDto = cronometroDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_CRONOMETRO DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return cronometroDto;
    }
    
    
}
