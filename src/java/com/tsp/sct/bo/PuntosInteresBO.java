/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.PuntosInteres;
import com.tsp.sct.dao.jdbc.PuntosInteresDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Ing. Roberto Trejo
 */
public class PuntosInteresBO {
    private Connection conn = null;
    protected PuntosInteres puntos = null;
    
    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    /**
     * @return the puntos
     */
    public PuntosInteres getPuntos() {
        return puntos;
    }

    /**
     * @param puntos the puntos to set
     */
    public void setPuntos(PuntosInteres puntos) {
        this.puntos = puntos;
    }
    
    public PuntosInteresBO(Connection conn){
        this.conn = conn;
    }
    
    public PuntosInteresBO(int idPunto, Connection conn){
        this.conn = conn;
        try{
            PuntosInteresDaoImpl puntoDao = new PuntosInteresDaoImpl(this.conn);
            this.puntos = puntoDao.findByPrimaryKey(idPunto);
        }catch(Exception e){
            e.printStackTrace();;
        }
    }
    
    public PuntosInteres[] findPuntos(int idPunto, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda){
        PuntosInteres[] puntosInteres = new PuntosInteres[0];
        PuntosInteresDaoImpl puntoDao = new PuntosInteresDaoImpl(this.conn);
        try{
            String sqlFiltro="";
            if(idPunto > 0){
                sqlFiltro =" ID_PUNTO =" + idPunto + "  AND ";
            }else{
                sqlFiltro =" ID_PUNTO > 0 AND ";
            }
            
            if (idEmpresa>0){                
                sqlFiltro += "  ID_EMPRESA= " + idEmpresa + " ";
            }else{
                sqlFiltro +=" ID_EMPRESA > 0 ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            puntosInteres = puntoDao.findByDynamicWhere(
                    sqlFiltro
                    + " ORDER BY ID_PUNTO DESC"
                    + sqlLimit
                    , new Object[0]);
                
        }catch(Exception ex){
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        return puntosInteres;
    }
    
}
