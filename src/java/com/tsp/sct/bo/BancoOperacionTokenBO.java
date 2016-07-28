/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.BancoOperacionToken;
import com.tsp.sct.dao.exceptions.BancoOperacionTokenDaoException;
import com.tsp.sct.dao.jdbc.BancoOperacionTokenDaoImpl;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author leonardo
 */
public class BancoOperacionTokenBO {
    private BancoOperacionToken bancoOperacionToken = null;

    public BancoOperacionToken getBancoOperacionToken() {
        return bancoOperacionToken;
    }

    public void setBancoOperacionToken(BancoOperacionToken bancoOperacionToken) {
        this.bancoOperacionToken = bancoOperacionToken;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public BancoOperacionTokenBO(Connection conn){
        this.conn = conn;
    }
       
    
    public BancoOperacionTokenBO(int idBancoOperacionToken, Connection conn){
        this.conn = conn;
        try{
            BancoOperacionTokenDaoImpl BancoOperacionTokenDaoImpl = new BancoOperacionTokenDaoImpl(this.conn);
            this.bancoOperacionToken = BancoOperacionTokenDaoImpl.findByPrimaryKey(idBancoOperacionToken);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public BancoOperacionToken findBancoOperacionTokenId(int idBancoOperacionToken) throws Exception{
        BancoOperacionToken BancoOperacionToken = null;
        
        try{
            BancoOperacionTokenDaoImpl BancoOperacionTokenDaoImpl = new BancoOperacionTokenDaoImpl(this.conn);
            BancoOperacionToken = BancoOperacionTokenDaoImpl.findByPrimaryKey(idBancoOperacionToken);
            if (BancoOperacionToken==null){
                throw new Exception("No se encontro ningun BancoOperacionToken que corresponda con los parámetros específicados.");
            }
            if (BancoOperacionToken.getIdBancoOperacion()<=0){
                throw new Exception("No se encontro ningun BancoOperacionToken que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del BancoOperacionToken del usuario. Error: " + e.getMessage());
        }
        
        return BancoOperacionToken;
    }
       
    /**
     * Realiza una búsqueda por ID BancoOperacionToken en busca de
     * coincidencias
     * @param idBancoOperacionToken ID Del BancoOperacionToken para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public BancoOperacionToken[] findBancoOperacionTokens(int idBancoOperacionToken, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        BancoOperacionToken[] bancoOperacionTokenDto = new BancoOperacionToken[0];
        BancoOperacionTokenDaoImpl bancoOperacionTokenDao = new BancoOperacionTokenDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idBancoOperacionToken>0){
                sqlFiltro ="ID_BANCO_OPERACION=" + idBancoOperacionToken + " AND ";
            }else{
                sqlFiltro ="ID_BANCO_OPERACION>0 AND";
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
            
            bancoOperacionTokenDto = bancoOperacionTokenDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_BANCO_OPERACION DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return bancoOperacionTokenDto;
    }
    
    public BancoOperacionToken operacionToken(String token, int idEmpresa){
        BancoOperacionToken operacionToken = null;
        try {
            operacionToken = new BancoOperacionTokenDaoImpl().findByDynamicWhere(" ID_EMPRESA = "+idEmpresa + " AND TOKEN_GENERADO = '"+token+"' AND ID_ESTATUS = 1 ", null)[0];
        } catch (Exception e) {
            System.out.println("----No se encontro un token con esas caracteristicas, mensaje de error: "+e.getMessage());
        }
        
        return operacionToken;
    }
        
}
    

