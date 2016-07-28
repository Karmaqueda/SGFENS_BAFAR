/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CrScore;
import com.tsp.sct.dao.jdbc.CrScoreDaoImpl;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar
 */
public class CrScoreBO {
    private CrScore crScore = null;
    private String orderBy = null;

    public CrScore getCrScore() {
        return crScore;
    }

    public void setCrScore(CrScore crScore) {
        this.crScore = crScore;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CrScoreBO(Connection conn){
        this.conn = conn;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    
    public CrScoreBO(int idCrScore, Connection conn){        
        this.conn = conn; 
        try{
           CrScoreDaoImpl CrScoreDaoImpl = new CrScoreDaoImpl(this.conn);
            this.crScore = CrScoreDaoImpl.findByPrimaryKey(idCrScore);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CrScore findCrScorebyId(int idCrScore) throws Exception{
        CrScore CrScore = null;
        
        try{
            CrScoreDaoImpl CrScoreDaoImpl = new CrScoreDaoImpl(this.conn);
            CrScore = CrScoreDaoImpl.findByPrimaryKey(idCrScore);
            if (CrScore==null){
                throw new Exception("No se encontro ningun CrScore que corresponda con los parámetros específicados.");
            }
            if (CrScore.getIdScore()<=0){
                throw new Exception("No se encontro ningun CrScore que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CrScore del usuario. Error: " + e.getMessage());
        }
        
        return CrScore;
    }
    
    /**
     * Realiza una búsqueda por ID CrScore en busca de
     * coincidencias
     * @param idCrScore ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CrScore
     */
    public CrScore[] findCrScores(int idCrScore, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CrScore[] crScoreDto = new CrScore[0];
        CrScoreDaoImpl crScoreDao = new CrScoreDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrScore>0){
                sqlFiltro ="id_score=" + idCrScore + " AND ";
            }else{
                sqlFiltro ="id_score>0 AND";
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
            
            crScoreDto = crScoreDao.findByDynamicWhere( 
                    sqlFiltro
                    + " " + (StringManage.getValidString(orderBy).length()>0? orderBy : "ORDER BY id_score desc")
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return crScoreDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID CrScore y otros filtros
     * @param idCrScore ID Del CrScore para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return int cantidad de registros coincidentes
     */
    public int findCantidadCrScores(int idCrScore, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        CrScoreDaoImpl crScoreDao = new CrScoreDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrScore>0){
                sqlFiltro ="id_score=" + idCrScore + " AND ";
            }else{
                sqlFiltro ="id_score>0 AND";
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
            
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_score) as cantidad FROM " + crScoreDao.getTableName() +  " WHERE " + 
                    sqlFiltro
                    + sqlLimit);
            if(rs.next()){
                cantidad = rs.getInt("cantidad");
            }
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return cantidad;
    }    
        
    public String getCrScoresByIdHTMLCombo(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            CrScore[] crScoreesDto = findCrScores(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (CrScore crScore : crScoreesDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == crScore.getIdScore())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+crScore.getIdScore()+"' "
                            + selectedStr
                            + "title='"+crScore.getDescripcion()+"'>"
                            + crScore.getNombre()
                            +"</option>";
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return strHTMLCombo;
    }       
        
}
