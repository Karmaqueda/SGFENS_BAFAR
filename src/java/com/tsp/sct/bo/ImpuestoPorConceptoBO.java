/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.ImpuestoPorConcepto;
import com.tsp.sct.dao.jdbc.ImpuestoPorConceptoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class ImpuestoPorConceptoBO {
    
    private Connection conn = null;
    
    public Connection getConn() {
        return conn;
    }
    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    private ImpuestoPorConcepto impuestoPorConcepto = null;

    public ImpuestoPorConcepto getImpuestoPorConcepto() {
        return impuestoPorConcepto;
    }

    public void setImpuestoPorConcepto(ImpuestoPorConcepto impuestoPorConcepto) {
        this.impuestoPorConcepto = impuestoPorConcepto;
    }
    
    public ImpuestoPorConceptoBO(Connection conn){
        this.conn =conn;
    }
       
    
    public ImpuestoPorConceptoBO(int idImpuestoPorConcepto, Connection conn){
        this.conn = conn;
        try{
            ImpuestoPorConceptoDaoImpl ImpuestoPorConceptoDaoImpl = new ImpuestoPorConceptoDaoImpl(this.conn);
            this.impuestoPorConcepto = ImpuestoPorConceptoDaoImpl.findByPrimaryKey(idImpuestoPorConcepto);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ImpuestoPorConcepto findImpuestoPorConceptoId(int idImpuestoPorConcepto) throws Exception{
        ImpuestoPorConcepto ImpuestoPorConcepto = null;
        
        try{
            ImpuestoPorConceptoDaoImpl ImpuestoPorConceptoDaoImpl = new ImpuestoPorConceptoDaoImpl(this.conn);
            ImpuestoPorConcepto = ImpuestoPorConceptoDaoImpl.findByPrimaryKey(idImpuestoPorConcepto);
            if (ImpuestoPorConcepto==null){
                throw new Exception("No se encontro ningun ImpuestoPorConcepto que corresponda con los parámetros específicados.");
            }
            if (ImpuestoPorConcepto.getIdImpuestoPorConcepto()<=0){
                throw new Exception("No se encontro ningun ImpuestoPorConcepto que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del ImpuestoPorConcepto del usuario. Error: " + e.getMessage());
        }
        
        return ImpuestoPorConcepto;
    }
    
    /**
     * Realiza una búsqueda por ID ImpuestoPorConcepto en busca de
     * coincidencias
     * @param idImpuestoPorConcepto ID Del ImpuestoPorConcepto para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public ImpuestoPorConcepto[] findImpuestoPorConceptos(int idImpuestoPorConcepto, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        ImpuestoPorConcepto[] impuestoPorConceptoDto = new ImpuestoPorConcepto[0];
        ImpuestoPorConceptoDaoImpl impuestoPorConceptoDao = new ImpuestoPorConceptoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idImpuestoPorConcepto>0){
                sqlFiltro ="ID_IMPUESTO_POR_CONCEPTO=" + idImpuestoPorConcepto + " ";
            }else{
                sqlFiltro ="ID_IMPUESTO_POR_CONCEPTO>0 ";
            }
            /*if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro +=" ID_EMPRESA>0";
            }*/
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            impuestoPorConceptoDto = impuestoPorConceptoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_IMPUESTO_POR_CONCEPTO ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return impuestoPorConceptoDto;
    }  
    
}