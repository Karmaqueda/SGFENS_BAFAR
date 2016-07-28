/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.ComodatoProducto;
import com.tsp.sct.dao.exceptions.ComodatoProductoDaoException;
import com.tsp.sct.dao.jdbc.ComodatoProductoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class ComodatoProductoBO {
    private ComodatoProducto comodatoProducto = null;

    public ComodatoProducto getComodatoProducto() {
        return comodatoProducto;
    }

    public void setComodatoProducto(ComodatoProducto comodatoProducto) {
        this.comodatoProducto = comodatoProducto;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public ComodatoProductoBO(Connection conn){
        this.conn = conn;
    }
       
    
    public ComodatoProductoBO(int idComodatoProducto, Connection conn){
        this.conn = conn;
        try{
            ComodatoProductoDaoImpl ComodatoProductoDaoImpl = new ComodatoProductoDaoImpl(this.conn);
            this.comodatoProducto = ComodatoProductoDaoImpl.findByPrimaryKey(idComodatoProducto);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ComodatoProducto findComodatoProductobyId(int idComodatoProducto) throws Exception{
        ComodatoProducto ComodatoProducto = null;
        
        try{
            ComodatoProductoDaoImpl ComodatoProductoDaoImpl = new ComodatoProductoDaoImpl(this.conn);
            ComodatoProducto = ComodatoProductoDaoImpl.findByPrimaryKey(idComodatoProducto);
            if (ComodatoProducto==null){
                throw new Exception("No se encontro ningun ComodatoProducto que corresponda con los parámetros específicados.");
            }
            if (ComodatoProducto.getIdComodatoProducto()<=0){
                throw new Exception("No se encontro ningun ComodatoProducto que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del ComodatoProducto del usuario. Error: " + e.getMessage());
        }
        
        return ComodatoProducto;
    }
   
    
    /**
     * Realiza una búsqueda por ID ComodatoProducto en busca de
     * coincidencias
     * @param idComodatoProducto ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public ComodatoProducto[] findComodatoProductos(int idComodatoProducto, int idComodato, int idConcepto, int minLimit,int maxLimit, String filtroBusqueda) {
        ComodatoProducto[] comodatoProductoDto = new ComodatoProducto[0];
        ComodatoProductoDaoImpl comodatoProductoDao = new ComodatoProductoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idComodatoProducto>0){
                sqlFiltro ="ID_COMODATO_PRODUCTO=" + idComodatoProducto + " AND ";
            }else{
                sqlFiltro ="ID_COMODATO_PRODUCTO>0 AND";
            }
            if (idComodato>0){                
                sqlFiltro += " ID_COMODATO = " + idComodato + " AND ";
            }else{
                sqlFiltro +=" ID_COMODATO>0 AND ";
            }
            
            if (idConcepto>0){                
                sqlFiltro += " ID_CONCEPTO = " + idConcepto + " ";
            }else{
                sqlFiltro +=" ID_CONCEPTO>0 ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            comodatoProductoDto = comodatoProductoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_COMODATO_PRODUCTO DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return comodatoProductoDto;
    }
    
}
    

