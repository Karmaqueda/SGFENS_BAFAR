/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Ruta;
import com.tsp.sct.dao.exceptions.RutaDaoException;
import com.tsp.sct.dao.jdbc.RutaDaoImpl;

import java.sql.Connection;

/**
 *
 * @author 578
 */
public class RutaBO {

    private Ruta ruta = null;   
    private Connection conn = null;
    

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    } 
    
    public RutaBO(Connection conn) {
        this.conn = conn;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    
    public Ruta findRutabyId(int idRuta,Connection conn) throws Exception{
        Ruta ruta = null;
        
        try{
            RutaDaoImpl rutaDaoImpl = new RutaDaoImpl(this.conn);
            ruta = rutaDaoImpl.findByPrimaryKey(idRuta);
            if (ruta==null){
                throw new Exception("No se encontro ninguna Ruta que corresponda con los parámetros específicados.");
            }
            if (ruta.getIdRuta()<=0){
                throw new Exception("No se encontro ninguna Ruta que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la Ruta. Error: " + e.getMessage());
        }
        
        return ruta;
    }
    
    
    public Ruta getRutaByIdEmpleado(int idEmpleado, Connection conn) throws Exception{
        Ruta ruta = null;
        
        try{
            RutaDaoImpl rutaDaoImpl = new RutaDaoImpl(this.conn);
            ruta = rutaDaoImpl.findByDynamicWhere("ID_EMPLEADO=" + idEmpleado , new Object[0])[0];
            if (ruta==null){
                throw new Exception("El Empleado no tiene asignada una Ruta.");
            }
            if (ruta.getIdRuta()<=0){
                throw new Exception("No se encontro ninguna Ruta que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la Ruta. Error: " + e.getMessage());
        }
        
        return ruta;
    }
    
    
    
    /**
     * Realiza una búsqueda por ID Ruta en busca de
     * coincidencias
     * @param idRuta ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public Ruta[] findMarcas(int idRuta, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        Ruta[] rutaDto = new Ruta[0];
        RutaDaoImpl rutaDao = new RutaDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idRuta>0){
                sqlFiltro ="ID_RUTA=" + idRuta + " AND ";
            }else{
                sqlFiltro ="ID_RUTA>0 AND";
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
            
            rutaDto = rutaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE_RUTA ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return rutaDto;
    }
    
    
}
