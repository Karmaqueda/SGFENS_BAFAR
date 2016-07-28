/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CrTipoCampo;
import com.tsp.sct.dao.jdbc.CrTipoCampoDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar
 */
public class CrTipoCampoBO {
    private CrTipoCampo crTipoCampo = null;

    public CrTipoCampo getCrTipoCampo() {
        return crTipoCampo;
    }

    public void setCrTipoCampo(CrTipoCampo crTipoCampo) {
        this.crTipoCampo = crTipoCampo;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CrTipoCampoBO(Connection conn){
        this.conn = conn;
    }
    
     public CrTipoCampoBO(int idCrTipoCampo, Connection conn){        
        this.conn = conn; 
        try{
           CrTipoCampoDaoImpl CrTipoCampoDaoImpl = new CrTipoCampoDaoImpl(this.conn);
            this.crTipoCampo = CrTipoCampoDaoImpl.findByPrimaryKey(idCrTipoCampo);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CrTipoCampo findCrTipoCampobyId(int idCrTipoCampo) throws Exception{
        CrTipoCampo CrTipoCampo = null;
        
        try{
            CrTipoCampoDaoImpl CrTipoCampoDaoImpl = new CrTipoCampoDaoImpl(this.conn);
            CrTipoCampo = CrTipoCampoDaoImpl.findByPrimaryKey(idCrTipoCampo);
            if (CrTipoCampo==null){
                throw new Exception("No se encontro ningun CrTipoCampo que corresponda con los parámetros específicados.");
            }
            if (CrTipoCampo.getIdTipoCampo()<=0){
                throw new Exception("No se encontro ningun CrTipoCampo que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CrTipoCampo del usuario. Error: " + e.getMessage());
        }
        
        return CrTipoCampo;
    }
    
    /**
     * Realiza una búsqueda por ID CrTipoCampo en busca de
     * coincidencias
     * @param idCrTipoCampo ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CrTipoCampo
     */
    public CrTipoCampo[] findCrTipoCampos(int idCrTipoCampo, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CrTipoCampo[] crTipoCampoDto = new CrTipoCampo[0];
        CrTipoCampoDaoImpl crTipoCampoDao = new CrTipoCampoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCrTipoCampo>0){
                sqlFiltro ="id_tipo_campo=" + idCrTipoCampo + " AND ";
            }else{
                sqlFiltro ="id_tipo_campo>0 AND";
            }
            if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro += " is_creado_sistema=1 ";//" ID_EMPRESA>0";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            crTipoCampoDto = crTipoCampoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY id_tipo_campo asc"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return crTipoCampoDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID CrTipoCampo y otros filtros
     * @param idCrTipoCampo ID Del CrTipoCampo para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return int cantidad de registros coincidentes
     */
    public int findCantidadCrTipoCampos(int idCrTipoCampo, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        CrTipoCampoDaoImpl crTipoCampoDao = new CrTipoCampoDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrTipoCampo>0){
                sqlFiltro ="id_tipo_campo=" + idCrTipoCampo + " AND ";
            }else{
                sqlFiltro ="id_tipo_campo>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_tipo_campo) as cantidad FROM " + crTipoCampoDao.getTableName() +  " WHERE " + 
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
        
    public String getCrTipoCamposByIdHTMLCombo(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            CrTipoCampo[] crTipoCamposDto = findCrTipoCampos(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (CrTipoCampo crTipoCampo : crTipoCamposDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == crTipoCampo.getIdTipoCampo())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+crTipoCampo.getIdTipoCampo()+"' "
                            + selectedStr
                            + "title='"+crTipoCampo.getDescripcion()+"'>"
                            + crTipoCampo.getNombre()
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
