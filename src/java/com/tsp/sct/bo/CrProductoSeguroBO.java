/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CrProductoSeguro;
import com.tsp.sct.dao.jdbc.CrProductoSeguroDaoImpl;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar
 */
public class CrProductoSeguroBO {
    private CrProductoSeguro crProductoSeguro = null;
    private String orderBy = null;

    public CrProductoSeguro getCrProductoSeguro() {
        return crProductoSeguro;
    }

    public void setCrProductoSeguro(CrProductoSeguro crProductoSeguro) {
        this.crProductoSeguro = crProductoSeguro;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CrProductoSeguroBO(Connection conn){
        this.conn = conn;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    
    public CrProductoSeguroBO(int idCrProductoSeguro, Connection conn){        
        this.conn = conn; 
        try{
           CrProductoSeguroDaoImpl CrProductoSeguroDaoImpl = new CrProductoSeguroDaoImpl(this.conn);
            this.crProductoSeguro = CrProductoSeguroDaoImpl.findByPrimaryKey(idCrProductoSeguro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CrProductoSeguro findCrProductoSegurobyId(int idCrProductoSeguro) throws Exception{
        CrProductoSeguro CrProductoSeguro = null;
        
        try{
            CrProductoSeguroDaoImpl CrProductoSeguroDaoImpl = new CrProductoSeguroDaoImpl(this.conn);
            CrProductoSeguro = CrProductoSeguroDaoImpl.findByPrimaryKey(idCrProductoSeguro);
            if (CrProductoSeguro==null){
                throw new Exception("No se encontro ningun CrProductoSeguro que corresponda con los parámetros específicados.");
            }
            if (CrProductoSeguro.getIdProductoSeguro()<=0){
                throw new Exception("No se encontro ningun CrProductoSeguro que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CrProductoSeguro del usuario. Error: " + e.getMessage());
        }
        
        return CrProductoSeguro;
    }
    
    /**
     * Realiza una búsqueda por ID CrProductoSeguro en busca de
     * coincidencias
     * @param idCrProductoSeguro ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CrProductoSeguro
     */
    public CrProductoSeguro[] findCrProductoSeguros(int idCrProductoSeguro, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CrProductoSeguro[] crProductoSeguroDto = new CrProductoSeguro[0];
        CrProductoSeguroDaoImpl crProductoSeguroDao = new CrProductoSeguroDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCrProductoSeguro>0){
                sqlFiltro ="id_producto_seguro=" + idCrProductoSeguro + " AND ";
            }else{
                sqlFiltro ="id_producto_seguro>0 AND";
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
            
            crProductoSeguroDto = crProductoSeguroDao.findByDynamicWhere( 
                    sqlFiltro
                    + " " + (StringManage.getValidString(orderBy).length()>0? orderBy : "ORDER BY id_producto_seguro desc")
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return crProductoSeguroDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID CrProductoSeguro y otros filtros
     * @param idCrProductoSeguro ID Del CrProductoSeguro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return int cantidad de registros coincidentes
     */
    public int findCantidadCrProductoSeguros(int idCrProductoSeguro, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        CrProductoSeguroDaoImpl crProductoSeguroDao = new CrProductoSeguroDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrProductoSeguro>0){
                sqlFiltro ="id_producto_seguro=" + idCrProductoSeguro + " AND ";
            }else{
                sqlFiltro ="id_producto_seguro>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_producto_seguro) as cantidad FROM " + crProductoSeguroDao.getTableName() +  " WHERE " + 
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
