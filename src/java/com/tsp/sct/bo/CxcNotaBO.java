/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CxcNota;
import com.tsp.sct.dao.jdbc.CxcNotaDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISCesarMartinez
 */
public class CxcNotaBO {
    private CxcNota cxcNota  = null;

    public CxcNota getCxcNota() {
        return cxcNota;
    }

    public void setCxcNota(CxcNota cxcNota) {
        this.cxcNota = cxcNota;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CxcNotaBO(Connection conn){
        this.conn = conn;
    }
    
    public CxcNotaBO(int idCxcNota, Connection conn){        
        this.conn = conn;
        try{
            CxcNotaDaoImpl CxcNotaDaoImpl = new CxcNotaDaoImpl(this.conn);
            this.cxcNota = CxcNotaDaoImpl.findByPrimaryKey(idCxcNota);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public CxcNota findCxbyId(int idCxc) throws Exception{
        CxcNota cxcNota = null;
        
        try{
            CxcNotaDaoImpl cxcDaoImpl = new CxcNotaDaoImpl(this.conn);
            cxcNota = cxcDaoImpl.findByPrimaryKey(idCxc);
            if (cxcNota==null){
                throw new Exception("No se encontro ningun cxcNota que corresponda según los parámetros específicados.");
            }
            if (cxcNota.getIdCxcNota()<=0){
                throw new Exception("No se encontro ningun cxcNota que corresponda según los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de CxcNota del usuario. Error: " + e.getMessage());
        }
        
        return cxcNota;
    }
    
    
    /**
     * Realiza una búsqueda por ID CxcNota en busca de
     * coincidencias
     * @param idCxcNota ID Del CxcNota para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar cxcNota, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CxcNota
     */
    public CxcNota[] findCxp(int idCxcNota, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CxcNota[] cxcDto = new CxcNota[0];
        CxcNotaDaoImpl cxcDao = new CxcNotaDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCxcNota>0){
                sqlFiltro ="ID_CXC_NOTA=" + idCxcNota + " AND ";
            }else{
                sqlFiltro ="ID_CXC_NOTA>0 AND";
            }
            if (idEmpresa>0){
                //sqlFiltro +=" ID_EMPRESA=" + idEmpresa;
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
            
            cxcDto = cxcDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_CXC_NOTA DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return cxcDto;
    }
    
}
