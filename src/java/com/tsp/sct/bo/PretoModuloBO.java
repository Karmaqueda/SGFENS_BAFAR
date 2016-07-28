/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.PretoModulo;
import com.tsp.sct.dao.exceptions.PretoModuloDaoException;
import com.tsp.sct.dao.jdbc.PretoModuloDaoImpl;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */
public class PretoModuloBO {
    
    private PretoModulo pretoModulo = null;

    public PretoModulo getPretoModulo() {
        return pretoModulo;
    }

    public void setPretoModulo(PretoModulo pretoModulo) {
        this.pretoModulo = pretoModulo;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public PretoModuloBO(Connection conn){
        this.conn = conn;
    }
       
    
    public PretoModuloBO(int idPretoModulo, Connection conn){
        this.conn = conn;
        try{
            PretoModuloDaoImpl PretoModuloDaoImpl = new PretoModuloDaoImpl(this.conn);
            this.pretoModulo = PretoModuloDaoImpl.findByPrimaryKey(idPretoModulo);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public PretoModulo findPretoModulobyId(int idPretoModulo) throws Exception{
        PretoModulo PretoModulo = null;
        
        try{
            PretoModuloDaoImpl PretoModuloDaoImpl = new PretoModuloDaoImpl(this.conn);
            PretoModulo = PretoModuloDaoImpl.findByPrimaryKey(idPretoModulo);
            if (PretoModulo==null){
                throw new Exception("No se encontro ningun PretoModulo que corresponda con los parámetros específicados.");
            }
            if (PretoModulo.getIdPretoModulo()<=0){
                throw new Exception("No se encontro ningun PretoModulo que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del PretoModulo del usuario. Error: " + e.getMessage());
        }
        
        return PretoModulo;
    }
    
    /**
     * Realiza una búsqueda por ID PretoModulo en busca de
     * coincidencias
     * @param idPretoModulo ID Del PretoModulo para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public PretoModulo[] findPretoModulos(int idPretoModulo, int minLimit,int maxLimit, String filtroBusqueda) {
        PretoModulo[] pretoModuloDto = new PretoModulo[0];
        PretoModuloDaoImpl pretoModuloDao = new PretoModuloDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idPretoModulo>0){
                sqlFiltro ="ID_PRETO_MODULO=" + idPretoModulo + " AND ";
            }else{
                sqlFiltro ="ID_PRETO_MODULO>0 AND";
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
            
            pretoModuloDto = pretoModuloDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_PRETO_MODULO ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return pretoModuloDto;
    }
    
}
    

