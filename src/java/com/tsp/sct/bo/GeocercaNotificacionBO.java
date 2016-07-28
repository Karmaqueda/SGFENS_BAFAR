/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.GeocercasNotificaciones;
import com.tsp.sct.dao.exceptions.GeocercasNotificacionesDaoException;
import com.tsp.sct.dao.jdbc.GeocercasNotificacionesDaoImpl;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */
public class GeocercaNotificacionBO {
    
    private GeocercasNotificaciones geocerca = null;

    public GeocercasNotificaciones getGeocercasNotificaciones() {
        return geocerca;
    }

    public void setGeocercasNotificaciones(GeocercasNotificaciones geocerca) {
        this.geocerca = geocerca;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public GeocercaNotificacionBO(Connection conn){
        this.conn = conn;
    }
    
    public GeocercaNotificacionBO(int idGeocercasNotificaciones, Connection conn){       
        this.conn = conn;
        try{
            GeocercasNotificacionesDaoImpl GeocercasNotificacionesDaoImpl = new GeocercasNotificacionesDaoImpl(this.conn);
            this.geocerca = GeocercasNotificacionesDaoImpl.findByPrimaryKey(idGeocercasNotificaciones);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public GeocercasNotificaciones findGeocercasNotificacionesbyId(int idGeocercasNotificaciones) throws Exception{
        GeocercasNotificaciones GeocercasNotificaciones = null;
        
        try{
            GeocercasNotificacionesDaoImpl GeocercasNotificacionesDaoImpl = new GeocercasNotificacionesDaoImpl(this.conn);
            GeocercasNotificaciones = GeocercasNotificacionesDaoImpl.findByPrimaryKey(idGeocercasNotificaciones);
            if (GeocercasNotificaciones==null){
                throw new Exception("No se encontro ninguna GeocercasNotificaciones que corresponda con los parámetros específicados.");
            }
            if (GeocercasNotificaciones.getIdGeoNotificacion()<=0){
                throw new Exception("No se encontro ninguna GeocercasNotificaciones que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la GeocercasNotificaciones del usuario. Error: " + e.getMessage());
        }
        
        return GeocercasNotificaciones;
    }
    
    public GeocercasNotificaciones getGeocercasNotificacionesGenericoByEmpresa(int idEmpresa) throws Exception{
        GeocercasNotificaciones geocerca = null;
        
        try{
            GeocercasNotificacionesDaoImpl geocercaDaoImpl = new GeocercasNotificacionesDaoImpl(this.conn);
            geocerca = geocercaDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (geocerca==null){
                throw new Exception("La empresa no tiene creada alguna GeocercasNotificaciones");
            }
        }catch(GeocercasNotificacionesDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada alguna GeocercasNotificaciones");
        }
        
        return geocerca;
    }
    
    public GeocercasNotificaciones getGeocercasNotificacionesGenericoByEmpresa2(int idEmpresa) throws Exception{
        GeocercasNotificaciones geocerca = null;
        
        try{
            GeocercasNotificacionesDaoImpl geocercaDaoImpl = new GeocercasNotificacionesDaoImpl(this.conn);
            geocerca = geocercaDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " ", new Object[0])[0];
            if (geocerca==null){
                throw new Exception("La empresa no tiene creada alguna GeocercasNotificaciones");
            }
        }catch(GeocercasNotificacionesDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada alguna GeocercasNotificaciones");
        }
        
        return geocerca;
    }
    
    /**
     * Realiza una búsqueda por ID GeocercasNotificaciones en busca de
     * coincidencias
     * @param idGeocercasNotificaciones ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar geocercas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO GeocercasNotificaciones
     */
    public GeocercasNotificaciones[] findGeocercasNotificacioness(int idGeocercasNotificaciones, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        GeocercasNotificaciones[] geocercaDto = new GeocercasNotificaciones[0];
        GeocercasNotificacionesDaoImpl geocercaDao = new GeocercasNotificacionesDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idGeocercasNotificaciones>0){
                sqlFiltro ="ID_GEO_NOTIFICACION=" + idGeocercasNotificaciones + " AND ";
            }else{
                sqlFiltro ="ID_GEO_NOTIFICACION>0 AND";
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
            
            geocercaDto = geocercaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_GEO_NOTIFICACION DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return geocercaDto;
    }
    
}
