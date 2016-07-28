/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CrSolicitudBitacora;
import com.tsp.sct.dao.jdbc.CrSolicitudBitacoraDaoImpl;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar
 */
public class CrSolicitudBitacoraBO {
    private CrSolicitudBitacora crSolicitudBitacora = null;
    private String orderBy = null;

    public CrSolicitudBitacora getCrSolicitudBitacora() {
        return crSolicitudBitacora;
    }

    public void setCrSolicitudBitacora(CrSolicitudBitacora crSolicitudBitacora) {
        this.crSolicitudBitacora = crSolicitudBitacora;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CrSolicitudBitacoraBO(Connection conn){
        this.conn = conn;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    
    public CrSolicitudBitacoraBO(int idCrSolicitudBitacora, Connection conn){        
        this.conn = conn; 
        try{
           CrSolicitudBitacoraDaoImpl CrSolicitudBitacoraDaoImpl = new CrSolicitudBitacoraDaoImpl(this.conn);
            this.crSolicitudBitacora = CrSolicitudBitacoraDaoImpl.findByPrimaryKey(idCrSolicitudBitacora);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CrSolicitudBitacora findCrSolicitudBitacorabyId(int idCrSolicitudBitacora) throws Exception{
        CrSolicitudBitacora CrSolicitudBitacora = null;
        
        try{
            CrSolicitudBitacoraDaoImpl CrSolicitudBitacoraDaoImpl = new CrSolicitudBitacoraDaoImpl(this.conn);
            CrSolicitudBitacora = CrSolicitudBitacoraDaoImpl.findByPrimaryKey(idCrSolicitudBitacora);
            if (CrSolicitudBitacora==null){
                throw new Exception("No se encontro ningun CrSolicitudBitacora que corresponda con los parámetros específicados.");
            }
            if (CrSolicitudBitacora.getIdSolicitudBitacora()<=0){
                throw new Exception("No se encontro ningun CrSolicitudBitacora que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CrSolicitudBitacora del usuario. Error: " + e.getMessage());
        }
        
        return CrSolicitudBitacora;
    }
    
    /**
     * Realiza una búsqueda por ID CrSolicitudBitacora en busca de
     * coincidencias
     * @param idCrSolicitudBitacora ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CrSolicitudBitacora
     */
    public CrSolicitudBitacora[] findCrSolicitudBitacoras(int idCrSolicitudBitacora, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CrSolicitudBitacora[] crSolicitudBitacoraDto = new CrSolicitudBitacora[0];
        CrSolicitudBitacoraDaoImpl crSolicitudBitacoraDao = new CrSolicitudBitacoraDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCrSolicitudBitacora>0){
                sqlFiltro ="id_solicitud_bitacora=" + idCrSolicitudBitacora + " AND ";
            }else{
                sqlFiltro ="id_solicitud_bitacora>0 AND";
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
            
            crSolicitudBitacoraDto = crSolicitudBitacoraDao.findByDynamicWhere( 
                    sqlFiltro
                    + " " + (StringManage.getValidString(orderBy).length()>0? orderBy : "ORDER BY id_solicitud_bitacora desc")
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return crSolicitudBitacoraDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID CrSolicitudBitacora y otros filtros
     * @param idCrSolicitudBitacora ID Del CrSolicitudBitacora para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return int cantidad de registros coincidentes
     */
    public int findCantidadCrSolicitudBitacoras(int idCrSolicitudBitacora, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        CrSolicitudBitacoraDaoImpl crSolicitudBitacoraDao = new CrSolicitudBitacoraDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrSolicitudBitacora>0){
                sqlFiltro ="id_solicitud_bitacora=" + idCrSolicitudBitacora + " AND ";
            }else{
                sqlFiltro ="id_solicitud_bitacora>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_solicitud_bitacora) as cantidad FROM " + crSolicitudBitacoraDao.getTableName() +  " WHERE " + 
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
