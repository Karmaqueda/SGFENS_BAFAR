/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;


import com.tsp.sct.dao.dto.CargaXls;
import com.tsp.sct.dao.jdbc.CargaXlsDaoImpl;
import java.sql.Connection;

/**
 *
 * @author HpPyme
 */
public class CargaExcelBO {
    
    
    private Connection conn = null;
    CargaXls cargaExcel = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public CargaXls getCargaEcel() {
        return cargaExcel;
    }

    public void setCargaEcel(CargaXls cargaEcel) {
        this.cargaExcel = cargaEcel;
    }

    public CargaExcelBO(Connection conn) {
         this.conn = conn;
    }
    
    
    public CargaExcelBO(int idCargaExcel, Connection conn){
        this.conn = conn;
         try{
            CargaXlsDaoImpl cargaExcelDaoImpl = new CargaXlsDaoImpl(this.conn);
            this.cargaExcel = cargaExcelDaoImpl.findByPrimaryKey(idCargaExcel);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
     /**
     * Realiza una búsqueda por ID CargaExcel en busca de
     * coincidencias
     * @param idCargaExcel ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar Automovils, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CargaXls
     */
    public CargaXls[] findCargasExcel(int idCargaExcel, long idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CargaXls[] cargaXlsDto = new CargaXls[0];
        CargaXlsDaoImpl cargaXlsDao = new CargaXlsDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCargaExcel>0){
                sqlFiltro ="ID_CARGA=" + idCargaExcel + " AND ";
            }else{
                sqlFiltro ="ID_CARGA>0 AND ";
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
            
            cargaXlsDto = cargaXlsDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_CARGA DESC "
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return cargaXlsDto;
    }
    
    
}
