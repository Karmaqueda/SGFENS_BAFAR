/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CrEstadoSolicitud;
import com.tsp.sct.dao.jdbc.CrEstadoSolicitudDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author leonardo
 */
public class CrEstadoSolicitudBO {
    private CrEstadoSolicitud crEstadoSolicitud = null;
    
    public final static int S_RECHAZADA = 1;
    public final static int S_CANCELADA = 9;
    public final static int S_BORRADOR = 99;
    public final static int S_POR_REVISAR = 2;
    public final static int S_EN_REVISION = 3;
    public final static int S_APROBADA_MESAC = 4;
    public final static int S_APROBADA_VERIF = 5;
    public final static int S_APROBADA = 6;
    public final static int S_IMPRESION_LIB = 7;
    public final static int S_POR_DISPERSAR = 10;
    public final static int S_DISPERSADA = 8;

    public CrEstadoSolicitud getCrEstadoSolicitud() {
        return crEstadoSolicitud;
    }

    public void setCrEstadoSolicitud(CrEstadoSolicitud crEstadoSolicitud) {
        this.crEstadoSolicitud = crEstadoSolicitud;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CrEstadoSolicitudBO(Connection conn){
        this.conn = conn;
    }
    
     public CrEstadoSolicitudBO(int idCrEstadoSolicitud, Connection conn){        
        this.conn = conn; 
        try{
           CrEstadoSolicitudDaoImpl CrEstadoSolicitudDaoImpl = new CrEstadoSolicitudDaoImpl(this.conn);
            this.crEstadoSolicitud = CrEstadoSolicitudDaoImpl.findByPrimaryKey(idCrEstadoSolicitud);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CrEstadoSolicitud findCrEstadoSolicitudbyId(int idCrEstadoSolicitud) throws Exception{
        CrEstadoSolicitud CrEstadoSolicitud = null;
        
        try{
            CrEstadoSolicitudDaoImpl CrEstadoSolicitudDaoImpl = new CrEstadoSolicitudDaoImpl(this.conn);
            CrEstadoSolicitud = CrEstadoSolicitudDaoImpl.findByPrimaryKey(idCrEstadoSolicitud);
            if (CrEstadoSolicitud==null){
                throw new Exception("No se encontro ningun CrEstadoSolicitud que corresponda con los parámetros específicados.");
            }
            if (CrEstadoSolicitud.getIdEstadoSolicitud()<=0){
                throw new Exception("No se encontro ningun CrEstadoSolicitud que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CrEstadoSolicitud del usuario. Error: " + e.getMessage());
        }
        
        return CrEstadoSolicitud;
    }
    
    /**
     * Realiza una búsqueda por ID CrEstadoSolicitud en busca de
     * coincidencias
     * @param idCrEstadoSolicitud ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CrEstadoSolicitud
     */
    public CrEstadoSolicitud[] findCrEstadoSolicituds(int idCrEstadoSolicitud, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CrEstadoSolicitud[] crEstadoSolicitudDto = new CrEstadoSolicitud[0];
        CrEstadoSolicitudDaoImpl crEstadoSolicitudDao = new CrEstadoSolicitudDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCrEstadoSolicitud>0){
                sqlFiltro ="id_estado_solicitud=" + idCrEstadoSolicitud + " AND ";
            }else{
                sqlFiltro ="id_estado_solicitud>0 AND";
            }
            if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro += " is_creado_sistema=1 ";//" ID_EMPRESA>0";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            crEstadoSolicitudDto = crEstadoSolicitudDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY nombre asc"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return crEstadoSolicitudDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID CrEstadoSolicitud y otros filtros
     * @param idCrEstadoSolicitud ID Del CrEstadoSolicitud para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return int cantidad de registros coincidentes
     */
    public int findCantidadCrEstadoSolicituds(int idCrEstadoSolicitud, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        CrEstadoSolicitudDaoImpl crEstadoSolicitudDao = new CrEstadoSolicitudDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrEstadoSolicitud>0){
                sqlFiltro ="id_estado_solicitud=" + idCrEstadoSolicitud + " AND ";
            }else{
                sqlFiltro ="id_estado_solicitud>0 AND";
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
            
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_estado_solicitud) as cantidad FROM " + crEstadoSolicitudDao.getTableName() +  " WHERE " + 
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
        
    public String getCrEstadoSolicitudsByIdHTMLCombo(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            CrEstadoSolicitud[] crEstadoSolicitudsDto = findCrEstadoSolicituds(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (CrEstadoSolicitud crEstadoSolicitud : crEstadoSolicitudsDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == crEstadoSolicitud.getIdEstadoSolicitud())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+crEstadoSolicitud.getIdEstadoSolicitud()+"' "
                            + selectedStr
                            + "title='"+crEstadoSolicitud.getDescripcion()+"'>"
                            + crEstadoSolicitud.getNombre()
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
