/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SgfensCobranzaRegistroDepositoBanco;
import com.tsp.sct.dao.exceptions.SgfensCobranzaRegistroDepositoBancoDaoException;
import com.tsp.sct.dao.jdbc.SgfensCobranzaRegistroDepositoBancoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */
public class SGCobranzaRegistroDepositoBancoBO {
    private SgfensCobranzaRegistroDepositoBanco sgfensCobranzaRegistroDepositoBanco = null;

    public SgfensCobranzaRegistroDepositoBanco getSgfensCobranzaRegistroDepositoBanco() {
        return sgfensCobranzaRegistroDepositoBanco;
    }

    public void setSgfensCobranzaRegistroDepositoBanco(SgfensCobranzaRegistroDepositoBanco sgfensCobranzaRegistroDepositoBanco) {
        this.sgfensCobranzaRegistroDepositoBanco = sgfensCobranzaRegistroDepositoBanco;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SGCobranzaRegistroDepositoBancoBO(Connection conn){
        this.conn = conn;
    }
       
    
    public SGCobranzaRegistroDepositoBancoBO(int idSgfensCobranzaRegistroDepositoBanco, Connection conn){
        this.conn = conn;
        try{
            SgfensCobranzaRegistroDepositoBancoDaoImpl SgfensCobranzaRegistroDepositoBancoDaoImpl = new SgfensCobranzaRegistroDepositoBancoDaoImpl(this.conn);
            this.sgfensCobranzaRegistroDepositoBanco = SgfensCobranzaRegistroDepositoBancoDaoImpl.findByPrimaryKey(idSgfensCobranzaRegistroDepositoBanco);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public SgfensCobranzaRegistroDepositoBanco findDepositoId(int idSgfensCobranzaRegistroDepositoBanco) throws Exception{
        SgfensCobranzaRegistroDepositoBanco SgfensCobranzaRegistroDepositoBanco = null;
        
        try{
            SgfensCobranzaRegistroDepositoBancoDaoImpl SgfensCobranzaRegistroDepositoBancoDaoImpl = new SgfensCobranzaRegistroDepositoBancoDaoImpl(this.conn);
            SgfensCobranzaRegistroDepositoBanco = SgfensCobranzaRegistroDepositoBancoDaoImpl.findByPrimaryKey(idSgfensCobranzaRegistroDepositoBanco);
            if (SgfensCobranzaRegistroDepositoBanco==null){
                throw new Exception("No se encontro ningun SgfensCobranzaRegistroDepositoBanco que corresponda con los parámetros específicados.");
            }
            if (SgfensCobranzaRegistroDepositoBanco.getIdDeposito()<=0){
                throw new Exception("No se encontro ningun SgfensCobranzaRegistroDepositoBanco que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del SgfensCobranzaRegistroDepositoBanco del usuario. Error: " + e.getMessage());
        }
        
        return SgfensCobranzaRegistroDepositoBanco;
    }
    
    public SgfensCobranzaRegistroDepositoBanco getSgfensCobranzaRegistroDepositoBancoGenericoByEmpresa(int idEmpresa) throws Exception{
        SgfensCobranzaRegistroDepositoBanco sgfensCobranzaRegistroDepositoBanco = null;
        
        try{
            SgfensCobranzaRegistroDepositoBancoDaoImpl sgfensCobranzaRegistroDepositoBancoDaoImpl = new SgfensCobranzaRegistroDepositoBancoDaoImpl(this.conn);
            sgfensCobranzaRegistroDepositoBanco = sgfensCobranzaRegistroDepositoBancoDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (sgfensCobranzaRegistroDepositoBanco==null){
                throw new Exception("La empresa no tiene creada algun SgfensCobranzaRegistroDepositoBanco");
            }
        }catch(SgfensCobranzaRegistroDepositoBancoDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada algun SgfensCobranzaRegistroDepositoBanco");
        }
        
        return sgfensCobranzaRegistroDepositoBanco;
    }
    
    /**
     * Realiza una búsqueda por ID SgfensCobranzaRegistroDepositoBanco en busca de
     * coincidencias
     * @param idMarca ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public SgfensCobranzaRegistroDepositoBanco[] findSgfensCobranzaRegistroDepositoBancos(int idSgfensCobranzaRegistroDepositoBanco, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SgfensCobranzaRegistroDepositoBanco[] sgfensCobranzaRegistroDepositoBancoDto = new SgfensCobranzaRegistroDepositoBanco[0];
        SgfensCobranzaRegistroDepositoBancoDaoImpl sgfensCobranzaRegistroDepositoBancoDao = new SgfensCobranzaRegistroDepositoBancoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSgfensCobranzaRegistroDepositoBanco>0){
                sqlFiltro ="ID_DEPOSITO=" + idSgfensCobranzaRegistroDepositoBanco + " AND ";
            }else{
                sqlFiltro ="ID_DEPOSITO>0 AND";
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
            
            sgfensCobranzaRegistroDepositoBancoDto = sgfensCobranzaRegistroDepositoBancoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_DEPOSITO DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return sgfensCobranzaRegistroDepositoBancoDto;
    }    
    
}