/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CrDocImpParametro;
import com.tsp.sct.dao.jdbc.CrDocImpParametroDaoImpl;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar
 */
public class CrDocImpParametroBO {
    private CrDocImpParametro crDocImpParametro = null;
    private String orderBy = null;

    public CrDocImpParametro getCrDocImpParametro() {
        return crDocImpParametro;
    }

    public void setCrDocImpParametro(CrDocImpParametro crDocImpParametro) {
        this.crDocImpParametro = crDocImpParametro;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CrDocImpParametroBO(Connection conn){
        this.conn = conn;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    
    public CrDocImpParametroBO(int idCrDocImpParametro, Connection conn){        
        this.conn = conn; 
        try{
           CrDocImpParametroDaoImpl CrDocImpParametroDaoImpl = new CrDocImpParametroDaoImpl(this.conn);
            this.crDocImpParametro = CrDocImpParametroDaoImpl.findByPrimaryKey(idCrDocImpParametro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CrDocImpParametro findCrDocImpParametrobyId(int idCrDocImpParametro) throws Exception{
        CrDocImpParametro CrDocImpParametro = null;
        
        try{
            CrDocImpParametroDaoImpl CrDocImpParametroDaoImpl = new CrDocImpParametroDaoImpl(this.conn);
            CrDocImpParametro = CrDocImpParametroDaoImpl.findByPrimaryKey(idCrDocImpParametro);
            if (CrDocImpParametro==null){
                throw new Exception("No se encontro ningun CrDocImpParametro que corresponda con los parámetros específicados.");
            }
            if (CrDocImpParametro.getIdDocImpParametro()<=0){
                throw new Exception("No se encontro ningun CrDocImpParametro que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CrDocImpParametro del usuario. Error: " + e.getMessage());
        }
        
        return CrDocImpParametro;
    }
    
    /**
     * Realiza una búsqueda por ID CrDocImpParametro en busca de
     * coincidencias
     * @param idCrDocImpParametro ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CrDocImpParametro
     */
    public CrDocImpParametro[] findCrDocImpParametros(int idCrDocImpParametro, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CrDocImpParametro[] crDocImpParametroDto = new CrDocImpParametro[0];
        CrDocImpParametroDaoImpl crDocImpParametroDao = new CrDocImpParametroDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrDocImpParametro>0){
                sqlFiltro ="id_doc_imp_parametro=" + idCrDocImpParametro + " AND ";
            }else{
                sqlFiltro ="id_doc_imp_parametro>0 AND";
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
            
            crDocImpParametroDto = crDocImpParametroDao.findByDynamicWhere( 
                    sqlFiltro
                    + " " + (StringManage.getValidString(orderBy).length()>0? orderBy : "ORDER BY id_doc_imp_parametro desc")
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return crDocImpParametroDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID CrDocImpParametro y otros filtros
     * @param idCrDocImpParametro ID Del CrDocImpParametro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return int cantidad de registros coincidentes
     */
    public int findCantidadCrDocImpParametros(int idCrDocImpParametro, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        CrDocImpParametroDaoImpl crDocImpParametroDao = new CrDocImpParametroDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrDocImpParametro>0){
                sqlFiltro ="id_doc_imp_parametro=" + idCrDocImpParametro + " AND ";
            }else{
                sqlFiltro ="id_doc_imp_parametro>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_doc_imp_parametro) as cantidad FROM " + crDocImpParametroDao.getTableName() +  " WHERE " + 
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
        
    public String getCrDocImpParametrosByIdHTMLCombo(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            CrDocImpParametro[] crDocImpParametroesDto = findCrDocImpParametros(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (CrDocImpParametro crDocImpParametro : crDocImpParametroesDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == crDocImpParametro.getIdDocImpParametro())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+crDocImpParametro.getIdDocImpParametro()+"' "
                            + selectedStr
                            + "title='"+crDocImpParametro.getDescripcion()+"'>"
                            + crDocImpParametro.getParametroClave()
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
