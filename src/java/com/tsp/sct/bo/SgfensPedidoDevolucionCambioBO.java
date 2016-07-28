/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SgfensPedidoDevolucionCambio;
import com.tsp.sct.dao.exceptions.SgfensPedidoDevolucionCambioDaoException;
import com.tsp.sct.dao.jdbc.SgfensPedidoDevolucionCambioDaoImpl;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */
public class SgfensPedidoDevolucionCambioBO extends SgfensPedidoDevolucionCambioDaoImpl{
    
    private SgfensPedidoDevolucionCambio sgfensPedidoDevolucionCambio = null;

    public SgfensPedidoDevolucionCambio getSgfensPedidoDevolucionCambio() {
        return sgfensPedidoDevolucionCambio;
    }

    public void setSgfensPedidoDevolucionCambio(SgfensPedidoDevolucionCambio sgfensPedidoDevolucionCambio) {
        this.sgfensPedidoDevolucionCambio = sgfensPedidoDevolucionCambio;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SgfensPedidoDevolucionCambioBO(Connection conn){
        this.conn = conn;
    }
    
    public SgfensPedidoDevolucionCambioBO(int idSgfensPedidoDevolucionCambio, Connection conn){        
        this.conn = conn;
        try{
            SgfensPedidoDevolucionCambioDaoImpl SgfensPedidoDevolucionCambioDaoImpl = new SgfensPedidoDevolucionCambioDaoImpl(this.conn);
            this.sgfensPedidoDevolucionCambio = SgfensPedidoDevolucionCambioDaoImpl.findByPrimaryKey(idSgfensPedidoDevolucionCambio);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public SgfensPedidoDevolucionCambio findSgfensPedidoDevolucionCambiobyId(int idSgfensPedidoDevolucionCambio) throws Exception{
        SgfensPedidoDevolucionCambio SgfensPedidoDevolucionCambio = null;
        
        try{
            SgfensPedidoDevolucionCambioDaoImpl SgfensPedidoDevolucionCambioDaoImpl = new SgfensPedidoDevolucionCambioDaoImpl(this.conn);
            SgfensPedidoDevolucionCambio = SgfensPedidoDevolucionCambioDaoImpl.findByPrimaryKey(idSgfensPedidoDevolucionCambio);
            if (SgfensPedidoDevolucionCambio==null){
                throw new Exception("No se encontro ninguna SgfensPedidoDevolucionCambio que corresponda con los parámetros específicados.");
            }
            if (SgfensPedidoDevolucionCambio.getIdPedidoDevolCambio()<=0){
                throw new Exception("No se encontro ninguna SgfensPedidoDevolucionCambio que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la SgfensPedidoDevolucionCambio del usuario. Error: " + e.getMessage());
        }
        
        return SgfensPedidoDevolucionCambio;
    }
    
    public SgfensPedidoDevolucionCambio getSgfensPedidoDevolucionCambioGenericoByEmpresa(int idEmpresa) throws Exception{
        SgfensPedidoDevolucionCambio sgfensPedidoDevolucionCambio = null;
        
        try{
            SgfensPedidoDevolucionCambioDaoImpl sgfensPedidoDevolucionCambioDaoImpl = new SgfensPedidoDevolucionCambioDaoImpl(this.conn);
            sgfensPedidoDevolucionCambio = sgfensPedidoDevolucionCambioDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (sgfensPedidoDevolucionCambio==null){
                throw new Exception("La empresa no tiene creada alguna SgfensPedidoDevolucionCambio");
            }
        }catch(SgfensPedidoDevolucionCambioDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada alguna SgfensPedidoDevolucionCambio");
        }
        
        return sgfensPedidoDevolucionCambio;
    }
    
    /**
     * Realiza una búsqueda por ID SgfensPedidoDevolucionCambio en busca de
     * coincidencias
     * @param idSgfensPedidoDevolucionCambio ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar sgfensPedidoDevolucionCambios, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensPedidoDevolucionCambio
     */
    public SgfensPedidoDevolucionCambio[] findSgfensPedidoDevolucionCambios(int idSgfensPedidoDevolucionCambio, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SgfensPedidoDevolucionCambio[] sgfensPedidoDevolucionCambioDto = new SgfensPedidoDevolucionCambio[0];
        SgfensPedidoDevolucionCambioDaoImpl sgfensPedidoDevolucionCambioDao = new SgfensPedidoDevolucionCambioDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSgfensPedidoDevolucionCambio>0){
                sqlFiltro ="ID_PEDIDO_DEVOL_CAMBIO=" + idSgfensPedidoDevolucionCambio + " AND ";
            }else{
                sqlFiltro ="ID_PEDIDO_DEVOL_CAMBIO>0 AND";
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
            
            sgfensPedidoDevolucionCambioDto = sgfensPedidoDevolucionCambioDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_PEDIDO_DEVOL_CAMBIO DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return sgfensPedidoDevolucionCambioDto;
    }
    
    public SgfensPedidoDevolucionCambio[] findSgfensPedidoDevolucionCambiosAgrupadosTipoFechaPedido (int idPedido){
        SgfensPedidoDevolucionCambio[] sgfensPedidoDevolucionCambioDto = new SgfensPedidoDevolucionCambio[0];
        SgfensPedidoDevolucionCambioDaoImpl sgfensPedidoDevolucionCambioDao = new SgfensPedidoDevolucionCambioDaoImpl(this.conn);
        try{
            String sqlPedidoDevolucionCambio = SQL_SELECT;
            sqlPedidoDevolucionCambio = sqlPedidoDevolucionCambio.replaceAll("MONTO_RESULTANTE", "SUM(MONTO_RESULTANTE) MONTO_RESULTANTE");
            sqlPedidoDevolucionCambio += " WHERE ID_PEDIDO = " + idPedido + " GROUP BY ID_PEDIDO, ID_TIPO, FECHA, DIFERENCIA_FAVOR ORDER BY FECHA ASC ";
            sgfensPedidoDevolucionCambioDto = sgfensPedidoDevolucionCambioDao.findByDynamicSelect(sqlPedidoDevolucionCambio, null);
            System.out.println("++++++++++++ tamaño del numero de devoluciones y cambios: " + sgfensPedidoDevolucionCambioDto.length);
        }catch(Exception e){
            System.out.println("Error de consulta a Base de Datos: " + e.toString());
            e.printStackTrace();
        }
        return sgfensPedidoDevolucionCambioDto;
    }
      
}
