/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.EmergenciaCredito;
import com.tsp.sct.dao.exceptions.EmergenciaCreditoDaoException;
import com.tsp.sct.dao.jdbc.EmergenciaCreditoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */
public class EmergenciaCreditoBO {
    
    private EmergenciaCredito emergenciaCredito = null;
    
    public boolean conRegistroContadorVigente = false;

    public EmergenciaCredito getEmergenciaCredito() {
        return emergenciaCredito;
    }

    public void setEmergenciaCredito(EmergenciaCredito emergenciaCredito) {
        this.emergenciaCredito = emergenciaCredito;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public EmergenciaCreditoBO(Connection conn){
        this.conn = conn;
    }
       
    
    public EmergenciaCreditoBO(int idEmergenciaCredito, Connection conn){
        this.conn = conn;
        try{
            EmergenciaCreditoDaoImpl EmergenciaCreditoDaoImpl = new EmergenciaCreditoDaoImpl(this.conn);
            this.emergenciaCredito = EmergenciaCreditoDaoImpl.findByPrimaryKey(idEmergenciaCredito);
        }catch(Exception e){
            e.printStackTrace();
        }
    }   
    
    public EmergenciaCredito getEmergenciaCreditoRegistroContador(int idEmpresa){
        EmergenciaCredito emergenciaCredito = null;
        
        try{
            EmergenciaCreditoDaoImpl emergenciaCreditoDaoImpl = new EmergenciaCreditoDaoImpl(this.conn);
            emergenciaCredito = emergenciaCreditoDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND FECHA_PAGO IS NULL AND MONTO_PAGADO = 0 ", new Object[0])[0];
            conRegistroContadorVigente = true;
            if (emergenciaCredito==null){
                conRegistroContadorVigente = false;
                //throw new Exception("La empresa no tiene creada algun EmergenciaCredito");
            }
        }catch(Exception  e){
            conRegistroContadorVigente = false;
            //e.printStackTrace();
            //throw new Exception("La empresa no tiene creada algun EmergenciaCredito");
        }
        
        return emergenciaCredito;
    }
    
    /**
     * Realiza una búsqueda por ID EmergenciaCredito en busca de
     * coincidencias
     * @param idEmergenciaCredito ID Del EmergenciaCredito para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public EmergenciaCredito[] findEmergenciaCreditos(int idEmergenciaCredito, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        EmergenciaCredito[] emergenciaCreditoDto = new EmergenciaCredito[0];
        EmergenciaCreditoDaoImpl emergenciaCreditoDao = new EmergenciaCreditoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idEmergenciaCredito>0){
                sqlFiltro ="ID_EMERGENCIA=" + idEmergenciaCredito + " AND ";
            }else{
                sqlFiltro ="ID_EMERGENCIA>0 AND";
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
            
            emergenciaCreditoDto = emergenciaCreditoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_EMERGENCIA DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return emergenciaCreditoDto;
    }
        
}
    


