/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CallCenter;
import com.tsp.sct.dao.exceptions.CallCenterDaoException;
import com.tsp.sct.dao.jdbc.CallCenterDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class CallCenterBO {
    private CallCenter callCenter = null;

    public CallCenter getCallCenter() {
        return callCenter;
    }

    public void setCallCenter(CallCenter callCenter) {
        this.callCenter = callCenter;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CallCenterBO(Connection conn){
        this.conn = conn;
    }
       
    
    public CallCenterBO(int idCallCenter, Connection conn){
        this.conn = conn;
        try{
            CallCenterDaoImpl CallCenterDaoImpl = new CallCenterDaoImpl(this.conn);
            this.callCenter = CallCenterDaoImpl.findByPrimaryKey(idCallCenter);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CallCenter findCallCenterbyId(int idCallCenter) throws Exception{
        CallCenter CallCenter = null;
        
        try{
            CallCenterDaoImpl CallCenterDaoImpl = new CallCenterDaoImpl(this.conn);
            CallCenter = CallCenterDaoImpl.findByPrimaryKey(idCallCenter);
            if (CallCenter==null){
                throw new Exception("No se encontro ningun CallCenter que corresponda con los parámetros específicados.");
            }
            if (CallCenter.getIdCallCenter()<=0){
                throw new Exception("No se encontro ningun CallCenter que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CallCenter del usuario. Error: " + e.getMessage());
        }
        
        return CallCenter;
    }
    
    /**
     * Realiza una búsqueda por ID CallCenter en busca de
     * coincidencias
     * @param idCallCenter ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public CallCenter[] findCallCenters(int idCallCenter, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CallCenter[] callCenterDto = new CallCenter[0];
        CallCenterDaoImpl callCenterDao = new CallCenterDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCallCenter>0){
                sqlFiltro ="ID_CALL_CENTER=" + idCallCenter + " AND ";
            }else{
                sqlFiltro ="ID_CALL_CENTER>0 AND";
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
            
            callCenterDto = callCenterDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY FECHA_CREACION DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return callCenterDto;
    }
        
}
    

