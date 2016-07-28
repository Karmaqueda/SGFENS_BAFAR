/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CrProductoPuntajeMonto;
import com.tsp.sct.dao.jdbc.CrProductoPuntajeMontoDaoImpl;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar
 */
public class CrProductoPuntajeMontoBO {
    private CrProductoPuntajeMonto crProductoPuntajeMonto = null;
    private String orderBy = null;

    public CrProductoPuntajeMonto getCrProductoPuntajeMonto() {
        return crProductoPuntajeMonto;
    }

    public void setCrProductoPuntajeMonto(CrProductoPuntajeMonto crProductoPuntajeMonto) {
        this.crProductoPuntajeMonto = crProductoPuntajeMonto;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CrProductoPuntajeMontoBO(Connection conn){
        this.conn = conn;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    
    public CrProductoPuntajeMontoBO(int idCrProductoPuntajeMonto, Connection conn){        
        this.conn = conn; 
        try{
           CrProductoPuntajeMontoDaoImpl CrProductoPuntajeMontoDaoImpl = new CrProductoPuntajeMontoDaoImpl(this.conn);
            this.crProductoPuntajeMonto = CrProductoPuntajeMontoDaoImpl.findByPrimaryKey(idCrProductoPuntajeMonto);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CrProductoPuntajeMonto findCrProductoPuntajeMontobyId(int idCrProductoPuntajeMonto) throws Exception{
        CrProductoPuntajeMonto CrProductoPuntajeMonto = null;
        
        try{
            CrProductoPuntajeMontoDaoImpl CrProductoPuntajeMontoDaoImpl = new CrProductoPuntajeMontoDaoImpl(this.conn);
            CrProductoPuntajeMonto = CrProductoPuntajeMontoDaoImpl.findByPrimaryKey(idCrProductoPuntajeMonto);
            if (CrProductoPuntajeMonto==null){
                throw new Exception("No se encontro ningun CrProductoPuntajeMonto que corresponda con los parámetros específicados.");
            }
            if (CrProductoPuntajeMonto.getIdProductoPuntajeMonto()<=0){
                throw new Exception("No se encontro ningun CrProductoPuntajeMonto que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CrProductoPuntajeMonto del usuario. Error: " + e.getMessage());
        }
        
        return CrProductoPuntajeMonto;
    }
    
    /**
     * Realiza una búsqueda por ID CrProductoPuntajeMonto en busca de
     * coincidencias
     * @param idCrProductoPuntajeMonto ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CrProductoPuntajeMonto
     */
    public CrProductoPuntajeMonto[] findCrProductoPuntajeMontos(int idCrProductoPuntajeMonto, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CrProductoPuntajeMonto[] crProductoPuntajeMontoDto = new CrProductoPuntajeMonto[0];
        CrProductoPuntajeMontoDaoImpl crProductoPuntajeMontoDao = new CrProductoPuntajeMontoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCrProductoPuntajeMonto>0){
                sqlFiltro ="id_producto_puntaje_monto=" + idCrProductoPuntajeMonto + " AND ";
            }else{
                sqlFiltro ="id_producto_puntaje_monto>0 AND";
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
            
            crProductoPuntajeMontoDto = crProductoPuntajeMontoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " " + (StringManage.getValidString(orderBy).length()>0? orderBy : "ORDER BY id_producto_puntaje_monto desc")
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return crProductoPuntajeMontoDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID CrProductoPuntajeMonto y otros filtros
     * @param idCrProductoPuntajeMonto ID Del CrProductoPuntajeMonto para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return int cantidad de registros coincidentes
     */
    public int findCantidadCrProductoPuntajeMontos(int idCrProductoPuntajeMonto, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        CrProductoPuntajeMontoDaoImpl crProductoPuntajeMontoDao = new CrProductoPuntajeMontoDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrProductoPuntajeMonto>0){
                sqlFiltro ="id_producto_puntaje_monto=" + idCrProductoPuntajeMonto + " AND ";
            }else{
                sqlFiltro ="id_producto_puntaje_monto>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_producto_puntaje_monto) as cantidad FROM " + crProductoPuntajeMontoDao.getTableName() +  " WHERE " + 
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
