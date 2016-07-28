/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CrScoreDetalle;
import com.tsp.sct.dao.jdbc.CrScoreDetalleDaoImpl;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar
 */
public class CrScoreDetalleBO {
    private CrScoreDetalle crScoreDetalle = null;
    private String orderBy = null;

    public CrScoreDetalle getCrScoreDetalle() {
        return crScoreDetalle;
    }

    public void setCrScoreDetalle(CrScoreDetalle crScoreDetalle) {
        this.crScoreDetalle = crScoreDetalle;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CrScoreDetalleBO(Connection conn){
        this.conn = conn;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    
    public CrScoreDetalleBO(int idCrScoreDetalle, Connection conn){        
        this.conn = conn; 
        try{
           CrScoreDetalleDaoImpl CrScoreDetalleDaoImpl = new CrScoreDetalleDaoImpl(this.conn);
            this.crScoreDetalle = CrScoreDetalleDaoImpl.findByPrimaryKey(idCrScoreDetalle);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CrScoreDetalle findCrScoreDetallebyId(int idCrScoreDetalle) throws Exception{
        CrScoreDetalle CrScoreDetalle = null;
        
        try{
            CrScoreDetalleDaoImpl CrScoreDetalleDaoImpl = new CrScoreDetalleDaoImpl(this.conn);
            CrScoreDetalle = CrScoreDetalleDaoImpl.findByPrimaryKey(idCrScoreDetalle);
            if (CrScoreDetalle==null){
                throw new Exception("No se encontro ningun CrScoreDetalle que corresponda con los parámetros específicados.");
            }
            if (CrScoreDetalle.getIdScoreDetalle()<=0){
                throw new Exception("No se encontro ningun CrScoreDetalle que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CrScoreDetalle del usuario. Error: " + e.getMessage());
        }
        
        return CrScoreDetalle;
    }
    
    /**
     * Realiza una búsqueda por ID CrScoreDetalle en busca de
     * coincidencias
     * @param idCrScoreDetalle ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CrScoreDetalle
     */
    public CrScoreDetalle[] findCrScoreDetalles(int idCrScoreDetalle, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CrScoreDetalle[] crScoreDetalleDto = new CrScoreDetalle[0];
        CrScoreDetalleDaoImpl crScoreDetalleDao = new CrScoreDetalleDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCrScoreDetalle>0){
                sqlFiltro ="id_score_detalle=" + idCrScoreDetalle + " AND ";
            }else{
                sqlFiltro ="id_score_detalle>0 AND";
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
            
            crScoreDetalleDto = crScoreDetalleDao.findByDynamicWhere( 
                    sqlFiltro
                    + " " + (StringManage.getValidString(orderBy).length()>0? orderBy : "ORDER BY id_score_detalle desc")
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return crScoreDetalleDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID CrScoreDetalle y otros filtros
     * @param idCrScoreDetalle ID Del CrScoreDetalle para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return int cantidad de registros coincidentes
     */
    public int findCantidadCrScoreDetalles(int idCrScoreDetalle, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        CrScoreDetalleDaoImpl crScoreDetalleDao = new CrScoreDetalleDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrScoreDetalle>0){
                sqlFiltro ="id_score_detalle=" + idCrScoreDetalle + " AND ";
            }else{
                sqlFiltro ="id_score_detalle>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_score_detalle) as cantidad FROM " + crScoreDetalleDao.getTableName() +  " WHERE " + 
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
