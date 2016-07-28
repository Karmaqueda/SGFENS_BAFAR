/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;


import com.tsp.sct.dao.dto.VistaCobranzaReferenciaGrupos;
import com.tsp.sct.dao.jdbc.VistaCobranzaReferenciaGruposDaoImpl;
import java.sql.Connection;

/**
 *
 * @author 578
 */
public class VistaCobranzaReferenciaGruposBO {
    
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public VistaCobranzaReferenciaGruposBO(Connection conn){
        this.conn = conn;
    }
    
    
    /**
     * Realiza una búsqueda por Referencia en busca de
     * coincidencias
     * @param referencia ID Del referencia para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar referencias, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Automovil
     */
    public VistaCobranzaReferenciaGrupos[] findVistaCobranzaReferenciaGrupos(long referencia, long idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        VistaCobranzaReferenciaGrupos[] ReferenciaGruposDto = new VistaCobranzaReferenciaGrupos[0];
        VistaCobranzaReferenciaGruposDaoImpl ReferenciaGruposDao = new VistaCobranzaReferenciaGruposDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (referencia>0){
                sqlFiltro ="REFERENCIA='" + referencia + "' AND ";
            }else{
                sqlFiltro ="REFERENCIA IS NOT NULL AND ";
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
            
            ReferenciaGruposDto = ReferenciaGruposDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY FECHA_ABONO DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return ReferenciaGruposDto;
    }
    
    
    
    
}