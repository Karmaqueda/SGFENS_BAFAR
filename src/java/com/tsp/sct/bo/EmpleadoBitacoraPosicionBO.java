/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.EmpleadoBitacoraPosicion;
import com.tsp.sct.dao.exceptions.EmpleadoBitacoraPosicionDaoException;
import com.tsp.sct.dao.jdbc.EmpleadoBitacoraPosicionDaoImpl;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class EmpleadoBitacoraPosicionBO {
    private EmpleadoBitacoraPosicion empleadoBitacoraPosicion  = null;
    private Connection conn = null;
    private String queryOrderBy =  "";

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public EmpleadoBitacoraPosicion getEmpleadoBitacoraPosicion() {
        return empleadoBitacoraPosicion;
    }

    public void setEmpleadoBitacoraPosicion(EmpleadoBitacoraPosicion empleadoBitacoraPosicion) {
        this.empleadoBitacoraPosicion = empleadoBitacoraPosicion;
    }
    
    
    
    public EmpleadoBitacoraPosicionBO(Connection conn){
        this.conn = conn;
    }
    
    public EmpleadoBitacoraPosicionBO(int idEmpleadoBitacoraPosicion, Connection conn){        
        this.conn = conn;
        try{
            EmpleadoBitacoraPosicionDaoImpl EmpleadoBitacoraPosicionDaoImpl = new EmpleadoBitacoraPosicionDaoImpl(this.conn);
            this.empleadoBitacoraPosicion = EmpleadoBitacoraPosicionDaoImpl.findByPrimaryKey(idEmpleadoBitacoraPosicion);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public EmpleadoBitacoraPosicion findEmpleadoBitacoraPosicionbyId(long idEmpleadoBitacoraPosicion) throws Exception{
        EmpleadoBitacoraPosicion EmpleadoBitacoraPosicion = null;
        
        try{
            EmpleadoBitacoraPosicionDaoImpl EmpleadoBitacoraPosicionDaoImpl = new EmpleadoBitacoraPosicionDaoImpl(this.conn);
            EmpleadoBitacoraPosicion = EmpleadoBitacoraPosicionDaoImpl.findByPrimaryKey(idEmpleadoBitacoraPosicion);
            if (EmpleadoBitacoraPosicion==null){
                throw new Exception("No se encontro ningun Empleado Bitacora Posicion que corresponda con los parámetros específicados.");
            }
            if (EmpleadoBitacoraPosicion.getIdBitacoraPosicion()<=0){
                throw new Exception("No se encontro ningun Empleado BitacoraPosicion que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del EmpleadoBitacoraPosicion del usuario. Error: " + e.getMessage());
        }
        
        return EmpleadoBitacoraPosicion;
    }
    
    public EmpleadoBitacoraPosicion getEmpleadoBitacoraPosicionGenericoByEmpresa(long idEmpresa) throws Exception{
        EmpleadoBitacoraPosicion empleadoBitacoraPosicion = null;
        
        try{
            EmpleadoBitacoraPosicionDaoImpl empleadoBitacoraPosicionDaoImpl = new EmpleadoBitacoraPosicionDaoImpl(this.conn);
            empleadoBitacoraPosicion = empleadoBitacoraPosicionDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND GENERICO = 1", new Object[0])[0];
            if (empleadoBitacoraPosicion==null){
                throw new Exception("La empresa no tiene creado un Empleado Bitacora Posicion Génerico");
            }
        }catch(EmpleadoBitacoraPosicionDaoException e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creado un Empleado Bitacora Posicion Génerico");
        }
        
        return empleadoBitacoraPosicion;
    }
    
    /**
     * Realiza una búsqueda por ID EmpleadoBitacoraPosicion en busca de
     * coincidencias
     * @param idEmpleadoBitacoraPosicion ID Del EmpleadoBitacoraPosicion para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar empleadoBitacoraPosicion, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO EmpleadoBitacoraPosicion
     */
    public EmpleadoBitacoraPosicion[] findEmpleadoBitacoraPosicions(long idEmpleadoBitacoraPosicion, long idEmpleado, int minLimit,int maxLimit, String filtroBusqueda) {
        EmpleadoBitacoraPosicion[] empleadoBitacoraPosicionDto = new EmpleadoBitacoraPosicion[0];
        EmpleadoBitacoraPosicionDaoImpl empleadoBitacoraPosicionDao = new EmpleadoBitacoraPosicionDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idEmpleadoBitacoraPosicion>0){
                sqlFiltro ="ID_BITACORA_POSICION=" + idEmpleadoBitacoraPosicion + " AND ";
            }else{
                sqlFiltro ="ID_BITACORA_POSICION>0 AND ";
            }
            if (idEmpleado>0){
                //sqlFiltro +=" ID_EMPRESA=" + idEmpresa;
                sqlFiltro += " ID_EMPLEADO = " + idEmpleado;
            }else{
                sqlFiltro +=" ID_EMPLEADO>0";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            empleadoBitacoraPosicionDto = empleadoBitacoraPosicionDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY " + (StringManage.getValidString(queryOrderBy).length()>0 ? queryOrderBy : " FECHA ASC ")
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return empleadoBitacoraPosicionDto;
    }   

    public String getQueryOrderBy() {
        return queryOrderBy;
    }

    public void setQueryOrderBy(String queryOrderBy) {
        this.queryOrderBy = queryOrderBy;
    }
    
    
    
}
