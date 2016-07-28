/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;


import com.tsp.sct.dao.dto.RutaMarcador;
import com.tsp.sct.dao.jdbc.RutaMarcadorDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author 578
 */
public class RutaMarcadorBO {
    
    
    private RutaMarcador rutaMarcador = null;   
    private Connection conn = null;

    public RutaMarcador getRuta() {
        return rutaMarcador;
    }

    public void setRuta(RutaMarcador ruta) {
        this.rutaMarcador = ruta;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public RutaMarcadorBO(Connection conn) {
        this.conn = conn;
    }
    
    
    
    public RutaMarcador findRutaMarcadorbyId(int idRutaMarcador,Connection conn) throws Exception{
        RutaMarcador rutaMarcador = null;
        
        try{
            RutaMarcadorDaoImpl rutaMarcadorDaoImpl = new RutaMarcadorDaoImpl(this.conn);
            rutaMarcador = rutaMarcadorDaoImpl.findByPrimaryKey(idRutaMarcador);
            if (rutaMarcador==null){
                throw new Exception("No se encontro ningun Marcador que corresponda con los parámetros específicados.");
            }
            if (rutaMarcador.getIdRutaMarcador()<=0){
                throw new Exception("No se encontro ningun Marcador que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Marcador. Error: " + e.getMessage());
        }
        
        return rutaMarcador;
    }
    
    
    /**
     * Realiza una búsqueda por ID RutaMarcador en busca de
     * coincidencias
     * @param idRutaMarcador ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public RutaMarcador[] findRutaMarcadors(int idRutaMarcador, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        RutaMarcador[] rutaMarcadorDto = new RutaMarcador[0];
        RutaMarcadorDaoImpl rutaMarcadorDao = new RutaMarcadorDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idRutaMarcador>0){
                sqlFiltro ="ID_RUTA_MARCADOR=" + idRutaMarcador + " AND ";
            }else{
                sqlFiltro ="ID_RUTA_MARCADOR>0 AND";
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
            
            rutaMarcadorDto = rutaMarcadorDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_RUTA_MARCADOR desc"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return rutaMarcadorDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID RutaMarcador y otros filtros
     * @param idRutaMarcador ID Del RutaMarcador para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public int findCantidadRutaMarcadors(int idRutaMarcador, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        RutaMarcadorDaoImpl rutaMarcadorDao = new RutaMarcadorDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idRutaMarcador>0){
                sqlFiltro ="ID_RUTA_MARCADOR=" + idRutaMarcador + " AND ";
            }else{
                sqlFiltro ="ID_RUTA_MARCADOR>0 AND ";
            }
            if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ") ";
            }else{
                sqlFiltro +=" ID_EMPRESA>0 ";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(ID_RUTA_MARCADOR) as cantidad FROM " + rutaMarcadorDao.getTableName() +  " WHERE " + 
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
