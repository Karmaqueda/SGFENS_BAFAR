/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SgfensVisitaCliente;
import com.tsp.sct.dao.exceptions.SgfensVisitaClienteDaoException;
import com.tsp.sct.dao.jdbc.SgfensVisitaClienteDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author leonardo
 */
public class SGVisitaClienteBO {
    
    private SgfensVisitaCliente sgfensVisitaCliente = null;

    public SgfensVisitaCliente getSgfensVisitaCliente() {
        return sgfensVisitaCliente;
    }

    public void setSgfensVisitaCliente(SgfensVisitaCliente sgfensVisitaCliente) {
        this.sgfensVisitaCliente = sgfensVisitaCliente;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SGVisitaClienteBO(Connection conn){
        this.conn = conn;
    }
    
    public SGVisitaClienteBO(int idSgfensVisitaCliente, Connection conn){        
        this.conn = conn;
        try{
            SgfensVisitaClienteDaoImpl SgfensVisitaClienteDaoImpl = new SgfensVisitaClienteDaoImpl(this.conn);
            this.sgfensVisitaCliente = SgfensVisitaClienteDaoImpl.findByPrimaryKey(idSgfensVisitaCliente);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public SgfensVisitaCliente findSgfensVisitaClientebyId(int idSgfensVisitaCliente) throws Exception{
        SgfensVisitaCliente SgfensVisitaCliente = null;
        
        try{
            SgfensVisitaClienteDaoImpl SgfensVisitaClienteDaoImpl = new SgfensVisitaClienteDaoImpl(this.conn);
            SgfensVisitaCliente = SgfensVisitaClienteDaoImpl.findByPrimaryKey(idSgfensVisitaCliente);
            if (SgfensVisitaCliente==null){
                throw new Exception("No se encontro ninguna SgfensVisitaCliente que corresponda con los parámetros específicados.");
            }
            if (SgfensVisitaCliente.getIdVisita()<=0){
                throw new Exception("No se encontro ninguna SgfensVisitaCliente que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la SgfensVisitaCliente del usuario. Error: " + e.getMessage());
        }
        
        return SgfensVisitaCliente;
    }
    
    public SgfensVisitaCliente getSgfensVisitaClienteGenericoByEmpresa(int idEmpresa) throws Exception{
        SgfensVisitaCliente sgfensVisitaCliente = null;
        
        try{
            SgfensVisitaClienteDaoImpl sgfensVisitaClienteDaoImpl = new SgfensVisitaClienteDaoImpl(this.conn);
            sgfensVisitaCliente = sgfensVisitaClienteDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (sgfensVisitaCliente==null){
                throw new Exception("La empresa no tiene creada alguna SgfensVisitaCliente");
            }
        }catch(SgfensVisitaClienteDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada alguna SgfensVisitaCliente");
        }
        
        return sgfensVisitaCliente;
    }
    
    /**
     * Realiza una búsqueda por ID SgfensVisitaCliente en busca de
     * coincidencias
     * @param idSgfensVisitaCliente ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar sgfensVisitaClientes, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensVisitaCliente
     */
    public SgfensVisitaCliente[] findSgfensVisitaClientes(int idSgfensVisitaCliente, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SgfensVisitaCliente[] sgfensVisitaClienteDto = new SgfensVisitaCliente[0];
        SgfensVisitaClienteDaoImpl sgfensVisitaClienteDao = new SgfensVisitaClienteDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSgfensVisitaCliente>0){
                sqlFiltro ="ID_VISITA=" + idSgfensVisitaCliente + " AND ";
            }else{
                sqlFiltro ="ID_VISITA>0 AND";
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
            
            sgfensVisitaClienteDto = sgfensVisitaClienteDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_VISITA DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return sgfensVisitaClienteDto;
    }
    
    /**
     * Realiza una búsqueda por ID SgfensVisitaCliente en busca de
     * coincidencias
     * @param idSgfensVisitaCliente ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar sgfensVisitaClientes, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensVisitaCliente
     */
    public int findCantidadSgfensVisitaClientes(int idSgfensVisitaCliente, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        try {
            String sqlFiltro="";
            if (idSgfensVisitaCliente>0){
                sqlFiltro ="ID_VISITA=" + idSgfensVisitaCliente + " AND ";
            }else{
                sqlFiltro ="ID_VISITA>0 AND";
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
            
            Statement stmt = this.conn.createStatement();
            String query = "SELECT COUNT(ID_VISITA) as cantidad FROM sgfens_visita_cliente WHERE " + 
                    sqlFiltro
                    + sqlLimit;
            //System.out.println(":::QUERY A EJECUTAR: " + query);
            ResultSet rs = stmt.executeQuery(query);
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
