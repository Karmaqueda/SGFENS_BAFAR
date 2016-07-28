/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.DatosPersonalizados;
import com.tsp.sct.dao.exceptions.DatosPersonalizadosDaoException;
import com.tsp.sct.dao.jdbc.DatosPersonalizadosDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class DatosPersonalizadosBO {
    
    private DatosPersonalizados datosPersonalizados = null;

    public DatosPersonalizados getDatosPersonalizados() {
        return datosPersonalizados;
    }

    public void setDatosPersonalizados(DatosPersonalizados datosPersonalizados) {
        this.datosPersonalizados = datosPersonalizados;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public DatosPersonalizadosBO(){
        
    }
    
    public DatosPersonalizadosBO(Connection conn){
        this.conn = conn;
    }
    
    public DatosPersonalizadosBO(int idDatosPersonalizados, Connection conn){       
        this.conn = conn;
        try{
            DatosPersonalizadosDaoImpl DatosPersonalizadosDaoImpl = new DatosPersonalizadosDaoImpl(this.conn);
            this.datosPersonalizados = DatosPersonalizadosDaoImpl.findByPrimaryKey(idDatosPersonalizados);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public DatosPersonalizados findDatosPersonalizadosbyId(int idDatosPersonalizados) throws Exception{
        DatosPersonalizados DatosPersonalizados = null;
        
        try{
            DatosPersonalizadosDaoImpl DatosPersonalizadosDaoImpl = new DatosPersonalizadosDaoImpl(this.conn);
            DatosPersonalizados = DatosPersonalizadosDaoImpl.findByPrimaryKey(idDatosPersonalizados);
            if (DatosPersonalizados==null){
                throw new Exception("No se encontro ninguna DatosPersonalizados que corresponda con los parámetros específicados.");
            }
            if (DatosPersonalizados.getIdDatosPersonalizados()<=0){
                throw new Exception("No se encontro ninguna DatosPersonalizados que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la DatosPersonalizados del usuario. Error: " + e.getMessage());
        }
        
        return DatosPersonalizados;
    }
    
    /**
     * Realiza una búsqueda por ID DatosPersonalizados en busca de
     * coincidencias
     * @param idDatosPersonalizados ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar datosPersonalizadoss, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO DatosPersonalizados
     */
    public DatosPersonalizados[] findDatosPersonalizados(int idDatosPersonalizados, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        DatosPersonalizados[] datosPersonalizadosDto = new DatosPersonalizados[0];
        DatosPersonalizadosDaoImpl datosPersonalizadosDao = new DatosPersonalizadosDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idDatosPersonalizados>0){
                sqlFiltro ="ID_DATOS_PERSONALIZADOS=" + idDatosPersonalizados + " AND ";
            }else{
                sqlFiltro ="ID_DATOS_PERSONALIZADOS>0 AND";
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
            
            datosPersonalizadosDto = datosPersonalizadosDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY VARIABLE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return datosPersonalizadosDto;
    }
        
}
