/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.HorarioDetalle;
import com.tsp.sct.dao.jdbc.HorarioDetalleDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Cesar Martinez
 */
public class HorarioDetalleBO {
    
    private Connection conn = null;
    private HorarioDetalle horarioDetalle = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public HorarioDetalle getHorarioDetalle() {
        return horarioDetalle;
    }

    public void setHorarioDetalle(HorarioDetalle horarioDetalle) {
        this.horarioDetalle = horarioDetalle;
    }
    
    

    public HorarioDetalleBO(Connection conn) {
        this.conn = conn;
    }
    
    
    public HorarioDetalleBO(int idHorarioDetalle, Connection conn){        
        this.conn = conn;
        try{
            HorarioDetalleDaoImpl HorarioDetalleDao = new HorarioDetalleDaoImpl(this.conn);
            this.horarioDetalle = HorarioDetalleDao.findByPrimaryKey(idHorarioDetalle);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
   public HorarioDetalle[] findHorarioDetallebyId(int idHorario) throws Exception{
        HorarioDetalle[] horarioDetalle = new HorarioDetalle[0];
        
        try{
            HorarioDetalleDaoImpl HorarioDetalleDaoImpl = new HorarioDetalleDaoImpl(this.conn);
            horarioDetalle = HorarioDetalleDaoImpl.findWhereIdHorarioEquals(idHorario);
            if (horarioDetalle==null){
                throw new Exception("No se encontro ninguna Detalle de Horario que corresponda con los parámetros específicados.");
            }
            if (horarioDetalle.length<=0){
                throw new Exception("No se encontro ninguna Detalle de Horario que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la Marca del usuario. Error: " + e.getMessage());
        }
        
        return horarioDetalle;
    }
    
    
    /**
     * Realiza una búsqueda por ID Horario en busca de
     * coincidencias
     * @param idHorario ID Del horario para filtrar, -1 para mostrar todos los registros    
     * @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public HorarioDetalle[] findHorarioDetalle(int idHorario, int minLimit,int maxLimit, String filtroBusqueda) {
        HorarioDetalle[] horarioDetalle = new HorarioDetalle[0];
        HorarioDetalleDaoImpl horarioDetalleDao = new HorarioDetalleDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idHorario>0){
                sqlFiltro ="ID_HORARIO=" + idHorario + "  ";
            }else{
                sqlFiltro ="ID_HORARIO>0 ";
            }            
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            horarioDetalle = horarioDetalleDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_DETALLE_HORARIO ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return horarioDetalle;
    }
    
    
    
}
