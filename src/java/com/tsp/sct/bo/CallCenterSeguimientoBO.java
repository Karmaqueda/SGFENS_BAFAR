/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CallCenterSeguimiento;
import com.tsp.sct.dao.exceptions.CallCenterSeguimientoDaoException;
import com.tsp.sct.dao.jdbc.CallCenterSeguimientoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class CallCenterSeguimientoBO {
    private CallCenterSeguimiento callCenterSeguimiento = null;

    public CallCenterSeguimiento getCallCenterSeguimiento() {
        return callCenterSeguimiento;
    }

    public void setCallCenterSeguimiento(CallCenterSeguimiento callCenterSeguimiento) {
        this.callCenterSeguimiento = callCenterSeguimiento;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CallCenterSeguimientoBO(Connection conn){
        this.conn = conn;
    }
       
    
    public CallCenterSeguimientoBO(int idCallCenterSeguimiento, Connection conn){
        this.conn = conn;
        try{
            CallCenterSeguimientoDaoImpl CallCenterSeguimientoDaoImpl = new CallCenterSeguimientoDaoImpl(this.conn);
            this.callCenterSeguimiento = CallCenterSeguimientoDaoImpl.findByPrimaryKey(idCallCenterSeguimiento);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CallCenterSeguimiento findCallCenterSeguimientobyId(int idCallCenterSeguimiento) throws Exception{
        CallCenterSeguimiento CallCenterSeguimiento = null;
        
        try{
            CallCenterSeguimientoDaoImpl CallCenterSeguimientoDaoImpl = new CallCenterSeguimientoDaoImpl(this.conn);
            CallCenterSeguimiento = CallCenterSeguimientoDaoImpl.findByPrimaryKey(idCallCenterSeguimiento);
            if (CallCenterSeguimiento==null){
                throw new Exception("No se encontro ningun CallCenterSeguimiento que corresponda con los parámetros específicados.");
            }
            if (CallCenterSeguimiento.getIdCallCenterSeguimiento()<=0){
                throw new Exception("No se encontro ningun CallCenterSeguimiento que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CallCenterSeguimiento del usuario. Error: " + e.getMessage());
        }
        
        return CallCenterSeguimiento;
    }
        
    /**
     * Realiza una búsqueda por ID CallCenterSeguimiento en busca de
     * coincidencias
     * @param idCallCenterSeguimiento ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public CallCenterSeguimiento[] findCallCenterSeguimientos(int idCallCenterSeguimiento, int minLimit,int maxLimit, String filtroBusqueda) {
        CallCenterSeguimiento[] callCenterSeguimientoDto = new CallCenterSeguimiento[0];
        CallCenterSeguimientoDaoImpl callCenterSeguimientoDao = new CallCenterSeguimientoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCallCenterSeguimiento>0){
                sqlFiltro ="ID_CALL_CENTER_SEGUIMIENTO=" + idCallCenterSeguimiento +  " ";
            }else{
                sqlFiltro ="ID_CALL_CENTER_SEGUIMIENTO>0 ";
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
            
            callCenterSeguimientoDto = callCenterSeguimientoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_CALL_CENTER_SEGUIMIENTO DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return callCenterSeguimientoDto;
    }
        
}
    

