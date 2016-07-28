/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.ClienteCampoAdicional;
import com.tsp.sct.dao.exceptions.ClienteCampoContenidoDaoException;
import com.tsp.sct.dao.jdbc.ClienteCampoAdicionalDaoImpl;
import com.tsp.sct.dao.jdbc.ClienteCampoContenidoDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author leonardo
 */
public class ClienteCampoAdicionalBO {
    
    private ClienteCampoAdicional clienteCampoAdicional = null;

    public ClienteCampoAdicional getClienteCampoAdicional() {
        return clienteCampoAdicional;
    }

    public void setClienteCampoAdicional(ClienteCampoAdicional clienteCampoAdicional) {
        this.clienteCampoAdicional = clienteCampoAdicional;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public ClienteCampoAdicionalBO(Connection conn){
        this.conn = conn;
    }
       
    
    public ClienteCampoAdicionalBO(int idClienteCampoAdicional, Connection conn){
        this.conn = conn;
        try{
            ClienteCampoAdicionalDaoImpl ClienteCampoAdicionalDaoImpl = new ClienteCampoAdicionalDaoImpl(this.conn);
            this.clienteCampoAdicional = ClienteCampoAdicionalDaoImpl.findByPrimaryKey(idClienteCampoAdicional);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ClienteCampoAdicional findClienteCampoAdicionalbyId(int idClienteCampoAdicional) throws Exception{
        ClienteCampoAdicional ClienteCampoAdicional = null;
        
        try{
            ClienteCampoAdicionalDaoImpl ClienteCampoAdicionalDaoImpl = new ClienteCampoAdicionalDaoImpl(this.conn);
            ClienteCampoAdicional = ClienteCampoAdicionalDaoImpl.findByPrimaryKey(idClienteCampoAdicional);
            if (ClienteCampoAdicional==null){
                throw new Exception("No se encontro ningun ClienteCampoAdicional que corresponda con los parámetros específicados.");
            }
            if (ClienteCampoAdicional.getIdClienteCampo()<=0){
                throw new Exception("No se encontro ningun ClienteCampoAdicional que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del ClienteCampoAdicional del usuario. Error: " + e.getMessage());
        }
        
        return ClienteCampoAdicional;
    }
   
    
    /**
     * Realiza una búsqueda por ID ClienteCampoAdicional en busca de
     * coincidencias
     * @param idMarca ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public ClienteCampoAdicional[] findClienteCampoAdicionals(int idClienteCampoAdicional, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        ClienteCampoAdicional[] clienteCampoAdicionalDto = new ClienteCampoAdicional[0];
        ClienteCampoAdicionalDaoImpl clienteCampoAdicionalDao = new ClienteCampoAdicionalDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idClienteCampoAdicional>0){
                sqlFiltro ="ID_CLIENTE_CAMPO=" + idClienteCampoAdicional + " AND ";
            }else{
                sqlFiltro ="ID_CLIENTE_CAMPO>0 AND ";
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
            
            clienteCampoAdicionalDto = clienteCampoAdicionalDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_CLIENTE_CAMPO ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return clienteCampoAdicionalDto;
    }
    
    public void deleteCamposAdicionalesCliente(int idCliente) throws ClienteCampoContenidoDaoException
	{
            ClienteCampoContenidoDaoImpl campoContenidoDaoImpl = new ClienteCampoContenidoDaoImpl(this.conn);
		long t1 = System.currentTimeMillis();
		// declare variables
		final boolean isConnSupplied = (this.conn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		String SQL_DELETE = "DELETE FROM "+campoContenidoDaoImpl.getTableName() + " WHERE ID_CLIENTE = ? ";
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? this.conn : ResourceManager.getConnection();
		
			System.out.println( "Executing " + SQL_DELETE);
			stmt = conn.prepareStatement( SQL_DELETE );
			stmt.setInt( 1, idCliente );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new ClienteCampoContenidoDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}
        
}
    

