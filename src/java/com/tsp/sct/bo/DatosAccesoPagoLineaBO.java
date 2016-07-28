/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.DatosAccesoPagoLinea;
import com.tsp.sct.dao.jdbc.DatosAccesoPagoLineaDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class DatosAccesoPagoLineaBO {
    
    
    DatosAccesoPagoLinea datosAccesoPagoLinea = null;

    public DatosAccesoPagoLinea getDatosAccesoPagoLinea() {
        return datosAccesoPagoLinea;
    }

    public void setDatosAccesoPagoLinea(DatosAccesoPagoLinea datosAccesoPagoLinea) {
        this.datosAccesoPagoLinea = datosAccesoPagoLinea;
    }

    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public DatosAccesoPagoLineaBO(Connection conn) {
        this.conn = conn;
    }
    
    public DatosAccesoPagoLineaBO(int idDatosAccesoPagoLinea, Connection conn) {
        this.conn = conn;
        try{
            this.datosAccesoPagoLinea = new DatosAccesoPagoLineaDaoImpl(this.conn).findByPrimaryKey(idDatosAccesoPagoLinea);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void DatosAccesoPagoLineaxEmpresaBO(int idEmpresa) {
        try{
            DatosAccesoPagoLineaDaoImpl datosAccesoPagoLineaDao = new DatosAccesoPagoLineaDaoImpl(this.conn);
            String sqlFiltro ="ID_DATOS_ACCESO>0 AND";                
            //sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            sqlFiltro += " ID_EMPRESA = " + idEmpresa;
            DatosAccesoPagoLinea[] datos = datosAccesoPagoLineaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_DATOS_ACCESO DESC", new Object[0]);
//            DatosAccesoPagoLinea[] datos = new DatosAccesoPagoLineaDaoImpl(this.conn).findWhereIdEmpresaEquals(idDatosAccesoPagoLineaEmpresa);
            try{
                this.datosAccesoPagoLinea = datos[0];
            }catch(Exception es){
                this.datosAccesoPagoLinea = null;
            }
        }catch(Exception ex){
            this.datosAccesoPagoLinea = null;
            ex.printStackTrace();
        }
    }
    
    public void DatosAccesoPagoLineaxUsuarioBO(int idDatosAccesoPagoLineaUsuario) {
        try{
            DatosAccesoPagoLinea[] datos = new DatosAccesoPagoLineaDaoImpl(this.conn).findWhereIdUsuariosEquals(idDatosAccesoPagoLineaUsuario);
            this.datosAccesoPagoLinea = datos[0];
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * Realiza una búsqueda por ID DatosAccesoPagoLinea en busca de
     * coincidencias
     * @param idDatosAccesoPagoLinea ID de la DatosAccesoPagoLinea para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public DatosAccesoPagoLinea[] findDatosAccesoPagoLinea(int idDatosAccesoPagoLinea, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        DatosAccesoPagoLinea[] datosAccesoPagoLineaDto = new DatosAccesoPagoLinea[0];
        DatosAccesoPagoLineaDaoImpl datosAccesoPagoLineaDao = new DatosAccesoPagoLineaDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idDatosAccesoPagoLinea>0){
                sqlFiltro ="ID_DATOS_ACCESO=" + idDatosAccesoPagoLinea + " AND ";
            }else{
                sqlFiltro ="ID_DATOS_ACCESO>0 AND";
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
            
            datosAccesoPagoLineaDto = datosAccesoPagoLineaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_DATOS_ACCESO DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return datosAccesoPagoLineaDto;
    }

}
