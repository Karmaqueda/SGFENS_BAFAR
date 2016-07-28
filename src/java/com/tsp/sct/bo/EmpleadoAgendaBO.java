/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.EmpleadoAgenda;
import com.tsp.sct.dao.exceptions.EmpleadoAgendaDaoException;
import com.tsp.sct.dao.jdbc.EmpleadoAgendaDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author leonardo
 */
public class EmpleadoAgendaBO {
    
    private EmpleadoAgenda empleadoAgenda = null;

    public EmpleadoAgenda getEmpleadoAgenda() {
        return empleadoAgenda;
    }

    public void setEmpleadoAgenda(EmpleadoAgenda empleadoAgenda) {
        this.empleadoAgenda = empleadoAgenda;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public EmpleadoAgendaBO(Connection conn){
        this.conn = conn;
    }
    
    public EmpleadoAgendaBO(int idEmpleadoAgenda, Connection conn){        
        this.conn = conn;
        try{
            EmpleadoAgendaDaoImpl EmpleadoAgendaDaoImpl = new EmpleadoAgendaDaoImpl(this.conn);
            this.empleadoAgenda = EmpleadoAgendaDaoImpl.findByPrimaryKey(idEmpleadoAgenda);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public EmpleadoAgenda findEmpleadoAgendabyId(int idEmpleadoAgenda) throws Exception{
        EmpleadoAgenda EmpleadoAgenda = null;
        
        try{
            EmpleadoAgendaDaoImpl EmpleadoAgendaDaoImpl = new EmpleadoAgendaDaoImpl(this.conn);
            EmpleadoAgenda = EmpleadoAgendaDaoImpl.findByPrimaryKey(idEmpleadoAgenda);
            if (EmpleadoAgenda==null){
                throw new Exception("No se encontro ninguna EmpleadoAgenda que corresponda con los parámetros específicados.");
            }
            if (EmpleadoAgenda.getIdAgenda()<=0){
                throw new Exception("No se encontro ninguna EmpleadoAgenda que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la EmpleadoAgenda del usuario. Error: " + e.getMessage());
        }
        
        return EmpleadoAgenda;
    }
       
    /**
     * Realiza una búsqueda por ID EmpleadoAgenda en busca de
     * coincidencias
     * @param idEmpleadoAgenda ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar empleadoAgendas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO EmpleadoAgenda
     */
    public EmpleadoAgenda[] findEmpleadoAgendas(int idEmpleadoAgenda, int idEmpleado, int minLimit,int maxLimit, String filtroBusqueda) {
        EmpleadoAgenda[] empleadoAgendaDto = new EmpleadoAgenda[0];
        EmpleadoAgendaDaoImpl empleadoAgendaDao = new EmpleadoAgendaDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idEmpleadoAgenda>0){
                sqlFiltro ="ID_AGENDA=" + idEmpleadoAgenda + " AND ";
            }else{
                sqlFiltro ="ID_AGENDA>0 AND";
            }
            if (idEmpleado>0){                
                sqlFiltro += " ID_EMPLEADO = " + idEmpleado + " ";
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
            
            empleadoAgendaDto = empleadoAgendaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_AGENDA DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return empleadoAgendaDto;
    }
    
    /**
     * Realiza una búsqueda por ID EmpleadoAgenda en busca de
     * coincidencias
     * @param idEmpleadoAgenda ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar empleadoAgendas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO EmpleadoAgenda
     */
    public int findCantidadEmpleadoAgendas(int idEmpleadoAgenda, int idEmpleado, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        try {
            String sqlFiltro="";
            if (idEmpleadoAgenda>0){
                sqlFiltro ="ID_AGENDA=" + idEmpleadoAgenda + " AND ";
            }else{
                sqlFiltro ="ID_AGENDA>0 AND";
            }
            if (idEmpleado>0){                
                sqlFiltro += " ID_EMPLEADO = " + idEmpleado + " ";
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
            
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(ID_AGENDA) as cantidad FROM empleado_agenda WHERE " + 
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
