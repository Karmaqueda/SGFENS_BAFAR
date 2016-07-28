/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SgfensProveedorProducto;
import com.tsp.sct.dao.exceptions.SgfensProveedorProductoDaoException;
import com.tsp.sct.dao.jdbc.SgfensProveedorProductoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */
public class SGProveedorProductoBO {
    
    private SgfensProveedorProducto sgfensProveedorProducto = null;

    public SgfensProveedorProducto getSgfensProveedorProducto() {
        return sgfensProveedorProducto;
    }

    public void setSgfensProveedorProducto(SgfensProveedorProducto sgfensProveedorProducto) {
        this.sgfensProveedorProducto = sgfensProveedorProducto;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SGProveedorProductoBO(Connection conn){
        this.conn = conn;
    }
    
    public SGProveedorProductoBO(int idSgfensProveedorProducto, Connection conn){        
        this.conn = conn;
        try{
            SgfensProveedorProductoDaoImpl SgfensProveedorProductoDaoImpl = new SgfensProveedorProductoDaoImpl(this.conn);
            this.sgfensProveedorProducto = SgfensProveedorProductoDaoImpl.findByPrimaryKey(idSgfensProveedorProducto);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public SgfensProveedorProducto findSgfensProveedorProductobyId(int idSgfensProveedorProducto) throws Exception{
        SgfensProveedorProducto SgfensProveedorProducto = null;
        
        try{
            SgfensProveedorProductoDaoImpl SgfensProveedorProductoDaoImpl = new SgfensProveedorProductoDaoImpl(this.conn);
            SgfensProveedorProducto = SgfensProveedorProductoDaoImpl.findByPrimaryKey(idSgfensProveedorProducto);
            if (SgfensProveedorProducto==null){
                throw new Exception("No se encontro ninguna SgfensProveedorProducto que corresponda con los parámetros específicados.");
            }
            if (SgfensProveedorProducto.getIdProveedorProducto()<=0){
                throw new Exception("No se encontro ninguna SgfensProveedorProducto que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la SgfensProveedorProducto del usuario. Error: " + e.getMessage());
        }
        
        return SgfensProveedorProducto;
    }
    
    public SgfensProveedorProducto getSgfensProveedorProductoGenericoByEmpresa(int idEmpresa) throws Exception{
        SgfensProveedorProducto sgfensProveedorProducto = null;
        
        try{
            SgfensProveedorProductoDaoImpl sgfensProveedorProductoDaoImpl = new SgfensProveedorProductoDaoImpl(this.conn);
            sgfensProveedorProducto = sgfensProveedorProductoDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (sgfensProveedorProducto==null){
                throw new Exception("La empresa no tiene creada alguna SgfensProveedorProducto");
            }
        }catch(SgfensProveedorProductoDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada alguna SgfensProveedorProducto");
        }
        
        return sgfensProveedorProducto;
    }
    
    /**
     * Realiza una búsqueda por ID SgfensProveedorProducto en busca de
     * coincidencias
     * @param idSgfensProveedorProducto ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar sgfensProveedorProductos, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensProveedorProducto
     */
    public SgfensProveedorProducto[] findSgfensProveedorProductos(int idSgfensProveedorProducto, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SgfensProveedorProducto[] sgfensProveedorProductoDto = new SgfensProveedorProducto[0];
        SgfensProveedorProductoDaoImpl sgfensProveedorProductoDao = new SgfensProveedorProductoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSgfensProveedorProducto>0){
                sqlFiltro ="ID_PROVEEDOR_PRODUCTO=" + idSgfensProveedorProducto + " ";
            }else{
                sqlFiltro ="ID_PROVEEDOR_PRODUCTO>0 ";
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
            
            sgfensProveedorProductoDto = sgfensProveedorProductoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_PROVEEDOR_PRODUCTO ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return sgfensProveedorProductoDto;
    }
    
}
