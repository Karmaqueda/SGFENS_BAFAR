/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.ReporteConfigurable;
import com.tsp.sct.dao.jdbc.ReporteConfigurableDaoImpl;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */
public class ReporteConfigurableBO {
    private ReporteConfigurable reporteConfigurable = null;

    public ReporteConfigurable getReporteConfigurable() {
        return reporteConfigurable;
    }

    public void setReporteConfigurable(ReporteConfigurable reporteConfigurable) {
        this.reporteConfigurable = reporteConfigurable;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public ReporteConfigurableBO(){
        
    }
    
    public ReporteConfigurableBO(Connection conn){
        this.conn = conn;
    }
    
    public ReporteConfigurableBO(int idReporteConfigurable, Connection conn){       
        this.conn = conn;
        try{
            ReporteConfigurableDaoImpl ReporteConfigurableDaoImpl = new ReporteConfigurableDaoImpl(this.conn);
            this.reporteConfigurable = ReporteConfigurableDaoImpl.findByPrimaryKey(idReporteConfigurable);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ReporteConfigurable findReporteConfigurablebyId(int idReporteConfigurable) throws Exception{
        ReporteConfigurable ReporteConfigurable = null;
        
        try{
            ReporteConfigurableDaoImpl ReporteConfigurableDaoImpl = new ReporteConfigurableDaoImpl(this.conn);
            ReporteConfigurable = ReporteConfigurableDaoImpl.findByPrimaryKey(idReporteConfigurable);
            if (ReporteConfigurable==null){
                throw new Exception("No se encontro ningun Reporte Configurable que corresponda con los parámetros específicados.");
            }
            if (ReporteConfigurable.getIdConfiguracion()<=0){
                throw new Exception("No se encontro ningun Reporte Configurable que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de Reporte Configurable del usuario. Error: " + e.getMessage());
        }
        
        return ReporteConfigurable;
    }
      
    /**
     * Realiza una búsqueda por ID ReporteConfigurable en busca de
     * coincidencias
     * @param idReporteConfigurable ID Del ReporteConfigurable para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar reporteConfigurables, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO ReporteConfigurable
     */
    public ReporteConfigurable[] findReporteConfigurables(int idReporteConfigurable, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        ReporteConfigurable[] reporteConfigurableDto = new ReporteConfigurable[0];
        ReporteConfigurableDaoImpl reporteConfigurableDao = new ReporteConfigurableDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idReporteConfigurable>0){
                sqlFiltro ="ID_CONFIGURACION=" + idReporteConfigurable + " AND ID_ESTATUS = 1 AND ";
            }else{
                sqlFiltro ="ID_CONFIGURACION>0 AND ID_ESTATUS = 1 AND ";
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
            
            reporteConfigurableDto = reporteConfigurableDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_CONFIGURACION DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return reporteConfigurableDto;
    }
    
    /**
     * Realiza una búsqueda por ID ReporteConfigurable en busca de
     * coincidencias
     * @param idReporteConfigurable ID Del ReporteConfigurable para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar reporteConfigurables, -1 para evitar filtro     
     * @return String de cada una de las reporteConfigurables
     */
    
        public String getReporteConfigurableCbbByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            ReporteConfigurable[] reporteConfigurableDto = findReporteConfigurables(-1, idEmpresa, 0, 0, "");
            
            for (ReporteConfigurable reporteConfigurable:reporteConfigurableDto){
                try{                    
                    String selectedStr="";

                    if (idSeleccionado==reporteConfigurable.getIdConfiguracion())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+reporteConfigurable.getIdConfiguracion()+"' "
                            + selectedStr
                            + "title='"+reporteConfigurable.getIdTipoReporte()+"'>"
                            + reporteConfigurable.getIdTipoReporte()
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
