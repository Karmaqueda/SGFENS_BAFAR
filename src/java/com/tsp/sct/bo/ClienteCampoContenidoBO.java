/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.ClienteCampoContenido;
import com.tsp.sct.dao.jdbc.ClienteCampoContenidoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Ing. Roberto Trejo
 */
public class ClienteCampoContenidoBO {
    
    protected ClienteCampoContenido clienteCampoContenido = null;

    /**
     * @return the clienteCampoContenido
     */
    public ClienteCampoContenido getClienteCampoContenido() {
        return clienteCampoContenido;
    }

    /**
     * @param clienteCampoContenido the clienteCampoContenido to set
     */
    public void setClienteCampoContenido(ClienteCampoContenido clienteCampoContenido) {
        this.clienteCampoContenido = clienteCampoContenido;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public ClienteCampoContenidoBO(Connection conn){
        this.conn = conn;
    }
    
    public ClienteCampoContenidoBO(int idClienteCampoContenido,Connection conn){
        this.conn = conn;
        try{
            ClienteCampoContenidoDaoImpl campoContenidoDaoImpl = new ClienteCampoContenidoDaoImpl(this.conn);
            this.clienteCampoContenido = campoContenidoDaoImpl.findByPrimaryKey(idClienteCampoContenido);
        }catch(Exception e){
            e.printStackTrace();
        } 
    }
    
    public ClienteCampoContenido findClienteCampoContenidobyId(int idClienteCampoContenido) throws Exception{
        ClienteCampoContenido ClienteCampoContenido = null;
        try{
            ClienteCampoContenidoDaoImpl campoContenidoDaoImpl = new ClienteCampoContenidoDaoImpl(this.conn);
            ClienteCampoContenido = campoContenidoDaoImpl.findByPrimaryKey(idClienteCampoContenido);
            if(ClienteCampoContenido == null){
                throw new Exception("No se encontro ningun ClienteCampoContenido que corresponda con los parámetros específicados.");
            }
            if(ClienteCampoContenido.getIdContenido() <= 0){
                throw new Exception("No se encontro ningun ClienteCampoContenido que corresponda con los parámetros específicados.");
            }
        }catch(Exception ex){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del ClienteCampoContenidol del usuario. Error: " + ex.getMessage());
        }
        return ClienteCampoContenido;
    }
    
    public ClienteCampoContenido[] findClienteCampoContenido(int idCliente, int idClienteCampo, int minLimit,int maxLimit, String filtroBusqueda){
        ClienteCampoContenido[] camposContenidoDto = new ClienteCampoContenido[0];
        ClienteCampoContenidoDaoImpl campoContenidoDaoImpl = new ClienteCampoContenidoDaoImpl(this.conn);
        try{
            String sqlFiltro="";
            if (idCliente>0){
                sqlFiltro +=" ID_CLIENTE=" + idCliente + " AND ";
            }else{
                sqlFiltro +=" ID_CLIENTE>0 AND ";
            }
            if (idClienteCampo>0){
                sqlFiltro +=" ID_CLIENTE_CAMPO=" + idClienteCampo + " ";
            }else{
                sqlFiltro +=" ID_CLIENTE_CAMPO>0 ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            camposContenidoDto = campoContenidoDaoImpl.findByDynamicWhere( 
                    sqlFiltro
                   // + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
        }catch(Exception ex){
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        return camposContenidoDto;
    }
    
}
