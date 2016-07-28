/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.EmergenciaCreditoFacturaRealizada;
import com.tsp.sct.dao.jdbc.EmergenciaCreditoFacturaRealizadaDaoImpl;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */
public class EmergenciaCreditoFacturaRealizadaBO {
    
    private EmergenciaCreditoFacturaRealizada emergenciaCreditoFacturaRealizada = null;

    public EmergenciaCreditoFacturaRealizada getEmergenciaCreditoFacturaRealizada() {
        return emergenciaCreditoFacturaRealizada;
    }

    public void setEmergenciaCreditoFacturaRealizada(EmergenciaCreditoFacturaRealizada emergenciaCreditoFacturaRealizada) {
        this.emergenciaCreditoFacturaRealizada = emergenciaCreditoFacturaRealizada;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public EmergenciaCreditoFacturaRealizadaBO(Connection conn){
        this.conn = conn;
    }
       
    
    public EmergenciaCreditoFacturaRealizadaBO(int idEmergenciaCreditoFacturaRealizada, Connection conn){
        this.conn = conn;
        try{
            EmergenciaCreditoFacturaRealizadaDaoImpl EmergenciaCreditoFacturaRealizadaDaoImpl = new EmergenciaCreditoFacturaRealizadaDaoImpl(this.conn);
            this.emergenciaCreditoFacturaRealizada = EmergenciaCreditoFacturaRealizadaDaoImpl.findByPrimaryKey(idEmergenciaCreditoFacturaRealizada);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
        
    /**
     * Realiza una búsqueda por ID EmergenciaCreditoFacturaRealizada en busca de
     * coincidencias
     * @param idEmergenciaCreditoFacturaRealizada ID Del EmergenciaCreditoFacturaRealizada para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public EmergenciaCreditoFacturaRealizada[] findEmergenciaCreditoFacturaRealizadas(int idEmergenciaCreditoFacturaRealizada, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        EmergenciaCreditoFacturaRealizada[] emergenciaCreditoFacturaRealizadaDto = new EmergenciaCreditoFacturaRealizada[0];
        EmergenciaCreditoFacturaRealizadaDaoImpl emergenciaCreditoFacturaRealizadaDao = new EmergenciaCreditoFacturaRealizadaDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idEmergenciaCreditoFacturaRealizada>0){
                sqlFiltro ="ID_EMERGENCIA_CREDITO_FACTURA_REALIZADA=" + idEmergenciaCreditoFacturaRealizada + " AND ";
            }else{
                sqlFiltro ="ID_EMERGENCIA_CREDITO_FACTURA_REALIZADA>0 AND";
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
            
            emergenciaCreditoFacturaRealizadaDto = emergenciaCreditoFacturaRealizadaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_EMERGENCIA_CREDITO_FACTURA_REALIZADA DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return emergenciaCreditoFacturaRealizadaDto;
    }
    
}
    


