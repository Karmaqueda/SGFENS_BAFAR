/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.TipoReporte;
import com.tsp.sct.dao.exceptions.TipoReporteDaoException;
import com.tsp.sct.dao.jdbc.TipoReporteDaoImpl;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */
public class TipoReporteBO {
    
    private TipoReporte tipoReporte = null;

    public TipoReporte getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(TipoReporte tipoReporte) {
        this.tipoReporte = tipoReporte;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public TipoReporteBO(){
        
    }
    
    public TipoReporteBO(Connection conn){
        this.conn = conn;
    }
    
    /*public TipoReporteBO(int idReporte, Connection conn){       
        this.conn = conn;
        try{
            TipoReporteDaoImpl TipoReporteDaoImpl = new TipoReporteDaoImpl(this.conn);
            this.tipoReporte = TipoReporteDaoImpl.findByPrimaryKey(idReporte);
        }catch(Exception e){
            e.printStackTrace();
        }
    }*/
    
    public TipoReporteBO(int idTipoReporte, Connection conn){       
        this.conn = conn;
        try{
            TipoReporteDaoImpl TipoReporteDaoImpl = new TipoReporteDaoImpl(this.conn);
            this.tipoReporte = TipoReporteDaoImpl.findWhereIdTipoReporteEquals(idTipoReporte)[0];
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public TipoReporte findTipoReportebyId(int idTipoReporte) throws Exception{
        TipoReporte TipoReporte = null;
        
        try{
            TipoReporteDaoImpl TipoReporteDaoImpl = new TipoReporteDaoImpl(this.conn);
            TipoReporte = TipoReporteDaoImpl.findByPrimaryKey(idTipoReporte);
            if (TipoReporte==null){
                throw new Exception("No se encontro ningun Tipo de Reporte que corresponda con los parámetros específicados.");
            }
            if (TipoReporte.getIdTipoReporte()<=0){
                throw new Exception("No se encontro ningun Tipo de Reporte que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la Tipo de Reporte del usuario. Error: " + e.getMessage());
        }
        
        return TipoReporte;
    }
      
    /**
     * Realiza una búsqueda por ID TipoReporte en busca de
     * coincidencias
     * @param idTipoReporte ID Del TipoReporte para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar tipoReportes, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO TipoReporte
     */
    public TipoReporte[] findTipoReportes(int idTipoReporte, int minLimit,int maxLimit, String filtroBusqueda) {
        TipoReporte[] tipoReporteDto = new TipoReporte[0];
        TipoReporteDaoImpl tipoReporteDao = new TipoReporteDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idTipoReporte>0){
                sqlFiltro ="ID_REPORTE=" + idTipoReporte + " AND VISIBLE = 1 AND ID_ESTATUS = 1 ";
            }else{
                sqlFiltro ="ID_REPORTE>0 AND VISIBLE = 1 AND ID_ESTATUS = 1 ";
            }
            /*if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro +=" ID_EMPRESA>0";
            }*/
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            tipoReporteDto = tipoReporteDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_REPORTE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return tipoReporteDto;
    }
    
    /**
     * Realiza una búsqueda por ID TipoReporte en busca de
     * coincidencias
     * @param idTipoReporte ID Del TipoReporte para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar tipoReportes, -1 para evitar filtro     
     * @return String de cada una de las tipoReportes
     */
    
        public String getTipoReporteCbbByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            TipoReporte[] tipoReporteDto = findTipoReportes(-1, 0, 0, "");
            
            for (TipoReporte tipoReporte:tipoReporteDto){
                try{                    
                    String selectedStr="";

                    if (idSeleccionado==tipoReporte.getIdTipoReporte())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+tipoReporte.getIdTipoReporte()+"' "
                            + selectedStr
                            + "title='"+tipoReporte.getDescripcion()+"'>"
                            + tipoReporte.getDescripcion()
                            +"</option>";
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return strHTMLCombo;
    }
            
}
