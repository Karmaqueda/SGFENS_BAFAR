/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Movimiento;
import com.tsp.sct.dao.exceptions.MovimientoDaoException;
import com.tsp.sct.dao.jdbc.MovimientoDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Leonardo
 */
public class MovimientoBO {
    
    private Movimiento movimiento = null;

    public Movimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public MovimientoBO(Connection conn){
        this.conn = conn;
    }
    
    public MovimientoBO(int idMovimiento, Connection conn){        
        this.conn = conn;
        try{
            MovimientoDaoImpl MovimientoDaoImpl = new MovimientoDaoImpl(this.conn);
            this.movimiento = MovimientoDaoImpl.findByPrimaryKey(idMovimiento);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Movimiento findMovimientobyId(int idMovimiento) throws Exception{
        Movimiento Movimiento = null;
        
        try{
            MovimientoDaoImpl MovimientoDaoImpl = new MovimientoDaoImpl(this.conn);
            Movimiento = MovimientoDaoImpl.findByPrimaryKey(idMovimiento);
            if (Movimiento==null){
                throw new Exception("No se encontro ningun Movimiento que corresponda con los parámetros específicados.");
            }
            if (Movimiento.getIdMovimiento()<=0){
                throw new Exception("No se encontro ningun Movimiento que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Movimiento del usuario. Error: " + e.getMessage());
        }
        
        return Movimiento;
    }
    
    /**
     * Realiza una búsqueda por ID Movimiento en busca de
     * coincidencias
     * @param idMovimiento ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar Movimientos, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Movimiento
     */
    public Movimiento[] findMovimientos(int idMovimiento, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        Movimiento[] movimientoDto = new Movimiento[0];
        MovimientoDaoImpl movimientoDao = new MovimientoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idMovimiento>0){
                sqlFiltro ="ID_MOVIMIENTO=" + idMovimiento + " AND ";
            }else{
                sqlFiltro ="ID_MOVIMIENTO>0 AND";
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
            
            movimientoDto = movimientoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY FECHA_REGISTRO DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return movimientoDto;
    }
    
    public int getCantidadByMovimiento(String filtroBusqueda){
        int cantidad = 0;
        try{
            //Connection conne = ResourceManager.getConnection();
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(ID_MOVIMIENTO) as cantidad FROM MOVIMIENTO WHERE " + filtroBusqueda);
            if(rs.next()){
                cantidad = rs.getInt("cantidad");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return cantidad;
    }
    
}
