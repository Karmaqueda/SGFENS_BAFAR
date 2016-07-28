/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Cbb;
import com.tsp.sct.dao.jdbc.CbbDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class CbbBO {
    
    Cbb cbb = null;

    public Cbb getCbb() {
        return cbb;
    }

    public void setCbb(Cbb cbb) {
        this.cbb = cbb;
    }

    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
        
    public CbbBO(Connection conn) {
        this.conn = conn;
    }
    
    public CbbBO(int idCbb, Connection conn) {
        this.conn = conn;
        try{
            this.cbb = new CbbDaoImpl(this.conn).findByPrimaryKey(idCbb);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * Realiza una búsqueda por ID Cbb en busca de
     * coincidencias
     * @param idCbb ID de la Cbb para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public Cbb[] findCbb(int idCbb, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        Cbb[] cbbDto = new Cbb[0];
        CbbDaoImpl cbbDao = new CbbDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCbb>0){
                sqlFiltro ="ID_CBB=" + idCbb + " AND ";
            }else{
                sqlFiltro ="ID_CBB>0 AND";
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
            
            cbbDto = cbbDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_CBB DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return cbbDto;
    }    
    
    public Cbb findCbbByEmpresa(int idEmpresa) throws Exception {
        Cbb Cbb = null;
        
        try{
            CbbDaoImpl CbbDaoImpl = new CbbDaoImpl(this.conn);
            //ImagenPersonal = imagenPersonalDaoImpl.findByEmpresa(idEmpresa)[0];
            Cbb = CbbDaoImpl.findByDynamicWhere("ID_EMPRESA=" + idEmpresa + " ORDER BY ID_EMPRESA DESC", null)[0];
            if (Cbb==null){
                throw new Exception("No se encontro ningun CBB que corresponda con los parámetros específicados.");
            }
            if (Cbb.getIdCbb()<=0){
                throw new Exception("No se encontro ningun Cbb que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del ImagenPersonal del usuario. Error: " + e.getMessage());
        }
        
        return Cbb;
    }
    
}
