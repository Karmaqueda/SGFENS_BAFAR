/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.EmpleadoArqueo;
import com.tsp.sct.dao.jdbc.EmpleadoArqueoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author 578
 */
public class EmpleadoArqueoBO {
    
    private EmpleadoArqueo empleadoArqueo = null;
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public EmpleadoArqueo getEmpleadoArqueo() {
        return empleadoArqueo;
    }

    public void setEmpleadoArqueo(EmpleadoArqueo empleadoArqueo) {
        this.empleadoArqueo = empleadoArqueo;
    }    

    public EmpleadoArqueoBO(Connection conn) {
        this.conn = conn;
    }
    
       
    
    
     public EmpleadoArqueoBO(long idEmpleadoArqueo, Connection conn) {
        this.conn = conn;
        try{
            this.empleadoArqueo = new EmpleadoArqueoDaoImpl(this.conn).findByPrimaryKey((int)idEmpleadoArqueo);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public EmpleadoArqueoBO(int idEmpleadoArqueo, Connection conn) {
        this.conn = conn;
        try{
            this.empleadoArqueo = new EmpleadoArqueoDaoImpl(this.conn).findByPrimaryKey(idEmpleadoArqueo);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    
    public EmpleadoArqueo findEmpleadoEmpleadoArqueoId(int idEmpleadoArqueo) throws Exception{
        EmpleadoArqueo EmpleadoArqueo = null;
        
        try{
            EmpleadoArqueoDaoImpl EmpleadoArqueoDaoImpl = new EmpleadoArqueoDaoImpl(this.conn);
            EmpleadoArqueo = EmpleadoArqueoDaoImpl.findByPrimaryKey(idEmpleadoArqueo);
            if (EmpleadoArqueo==null){
                throw new Exception("No se encontro ningun registro que corresponda con los parámetros específicados.");
            }
            if (EmpleadoArqueo.getIdArqueo()<=0){
                throw new Exception("No se encontro ningun registro que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Arqueo del usuario. Error: " + e.getMessage());
        }
        
        return EmpleadoArqueo;
    }
    
    
    /**
     * Realiza una búsqueda por ID EMPLEADO en busca de
     * coincidencias
     * @param idEmpleadoArqueo ID Del Arqueo para filtrar, -1 para mostrar todos los registros
     * @param idEmpleado ID del empleado a filtrar inventario, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public EmpleadoArqueo[] findEmpleadoArqueo(int idArqueo, int idEmpresa, int idEmpleado, int minLimit,int maxLimit, String filtroBusqueda) {
        EmpleadoArqueo[] empleadoArqueoDto = new EmpleadoArqueo[0];
        EmpleadoArqueoDaoImpl empleadoArqueoDao = new EmpleadoArqueoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idArqueo>0){
                sqlFiltro ="ID_ARQUEO =" + idArqueo + " AND ID_ESTATUS = 1 AND ";
            }else{
                sqlFiltro ="ID_ARQUEO>0 AND ID_ESTATUS = 1 AND ";
            }
            if (idEmpleado>0){                
                sqlFiltro += " ID_EMPLEADO = " + idEmpleado + "";
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
            
            empleadoArqueoDto = empleadoArqueoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_ARQUEO DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return empleadoArqueoDto;
    }
    
    
}
