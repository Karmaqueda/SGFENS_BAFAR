/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CrFrmEventoSolicitud;
import com.tsp.sct.dao.jdbc.CrFrmEventoSolicitudDaoImpl;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar
 */
public class CrFrmEventoSolicitudBO {
    private CrFrmEventoSolicitud crFrmEventoSolicitud = null;
    private String orderBy = null;

    public CrFrmEventoSolicitud getCrFrmEventoSolicitud() {
        return crFrmEventoSolicitud;
    }

    public void setCrFrmEventoSolicitud(CrFrmEventoSolicitud crFrmEventoSolicitud) {
        this.crFrmEventoSolicitud = crFrmEventoSolicitud;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CrFrmEventoSolicitudBO(Connection conn){
        this.conn = conn;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    
    public CrFrmEventoSolicitudBO(int idCrFrmEventoSolicitud, Connection conn){        
        this.conn = conn; 
        try{
           CrFrmEventoSolicitudDaoImpl CrFrmEventoSolicitudDaoImpl = new CrFrmEventoSolicitudDaoImpl(this.conn);
            this.crFrmEventoSolicitud = CrFrmEventoSolicitudDaoImpl.findByPrimaryKey(idCrFrmEventoSolicitud);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CrFrmEventoSolicitud findCrFrmEventoSolicitudbyId(int idCrFrmEventoSolicitud) throws Exception{
        CrFrmEventoSolicitud CrFrmEventoSolicitud = null;
        
        try{
            CrFrmEventoSolicitudDaoImpl CrFrmEventoSolicitudDaoImpl = new CrFrmEventoSolicitudDaoImpl(this.conn);
            CrFrmEventoSolicitud = CrFrmEventoSolicitudDaoImpl.findByPrimaryKey(idCrFrmEventoSolicitud);
            if (CrFrmEventoSolicitud==null){
                throw new Exception("No se encontro ningun CrFrmEventoSolicitud que corresponda con los parámetros específicados.");
            }
            if (CrFrmEventoSolicitud.getIdFrmEventoSolicitud()<=0){
                throw new Exception("No se encontro ningun CrFrmEventoSolicitud que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CrFrmEventoSolicitud del usuario. Error: " + e.getMessage());
        }
        
        return CrFrmEventoSolicitud;
    }
    
    /**
     * Realiza una búsqueda por ID CrFrmEventoSolicitud en busca de
     * coincidencias
     * @param idCrFrmEventoSolicitud ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CrFrmEventoSolicitud
     */
    public CrFrmEventoSolicitud[] findCrFrmEventoSolicituds(int idCrFrmEventoSolicitud, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CrFrmEventoSolicitud[] crFrmEventoSolicitudDto = new CrFrmEventoSolicitud[0];
        CrFrmEventoSolicitudDaoImpl crFrmEventoSolicitudDao = new CrFrmEventoSolicitudDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCrFrmEventoSolicitud>0){
                sqlFiltro ="id_frm_evento_solicitud=" + idCrFrmEventoSolicitud + " ";//AND ";
            }else{
                sqlFiltro ="id_frm_evento_solicitud>0 ";//AND";
            }
            /*
            if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro +=" ID_EMPRESA>0";
            }
            */
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            crFrmEventoSolicitudDto = crFrmEventoSolicitudDao.findByDynamicWhere( 
                    sqlFiltro
                    + " " + (StringManage.getValidString(orderBy).length()>0? orderBy : "ORDER BY id_frm_evento_solicitud desc")
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return crFrmEventoSolicitudDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID CrFrmEventoSolicitud y otros filtros
     * @param idCrFrmEventoSolicitud ID Del CrFrmEventoSolicitud para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return int cantidad de registros coincidentes
     */
    public int findCantidadCrFrmEventoSolicituds(int idCrFrmEventoSolicitud, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        CrFrmEventoSolicitudDaoImpl crFrmEventoSolicitudDao = new CrFrmEventoSolicitudDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrFrmEventoSolicitud>0){
                sqlFiltro ="id_frm_evento_solicitud=" + idCrFrmEventoSolicitud + " ";//AND ";
            }else{
                sqlFiltro ="id_frm_evento_solicitud>0 ";//AND";
            }
            /*
            if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro +=" ID_EMPRESA>0";
            }
            */
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_frm_evento_solicitud) as cantidad FROM " + crFrmEventoSolicitudDao.getTableName() +  " WHERE " + 
                    sqlFiltro
                    + sqlLimit);
            if(rs.next()){
                cantidad = rs.getInt("cantidad");
            }
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return cantidad;
    }        
        
}
