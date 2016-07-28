/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CrFormularioCampo;
import com.tsp.sct.dao.jdbc.CrFormularioCampoDaoImpl;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar
 */
public class CrFormularioCampoBO {
    private CrFormularioCampo    crFormularioCampo = null;
    private String orderBy = null;

    public CrFormularioCampo getCrFormularioCampo() {
        return crFormularioCampo;
    }

    public void setCrFormularioCampo(CrFormularioCampo crFormularioCampo) {
        this.crFormularioCampo = crFormularioCampo;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    
    public CrFormularioCampoBO(Connection conn){
        this.conn = conn;
    }
    
     public CrFormularioCampoBO(int idCrFormularioCampo, Connection conn){        
        this.conn = conn; 
        try{
           CrFormularioCampoDaoImpl CrFormularioCampoDaoImpl = new CrFormularioCampoDaoImpl(this.conn);
            this.crFormularioCampo = CrFormularioCampoDaoImpl.findByPrimaryKey(idCrFormularioCampo);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CrFormularioCampo findCrFormularioCampobyId(int idCrFormularioCampo) throws Exception{
        CrFormularioCampo CrFormularioCampo = null;
        
        try{
            CrFormularioCampoDaoImpl CrFormularioCampoDaoImpl = new CrFormularioCampoDaoImpl(this.conn);
            CrFormularioCampo = CrFormularioCampoDaoImpl.findByPrimaryKey(idCrFormularioCampo);
            if (CrFormularioCampo==null){
                throw new Exception("No se encontro ningun CrFormularioCampo que corresponda con los parámetros específicados.");
            }
            if (CrFormularioCampo.getIdFormularioCampo()<=0){
                throw new Exception("No se encontro ningun CrFormularioCampo que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CrFormularioCampo del usuario. Error: " + e.getMessage());
        }
        
        return CrFormularioCampo;
    }
    
    /**
     * Realiza una búsqueda por ID CrFormularioCampo en busca de
     * coincidencias
     * @param idCrFormularioCampo ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CrFormularioCampo
     */
    public CrFormularioCampo[] findCrFormularioCampos(int idCrFormularioCampo, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CrFormularioCampo[] crFormularioCampoDto = new CrFormularioCampo[0];
        CrFormularioCampoDaoImpl crFormularioCampoDao = new CrFormularioCampoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCrFormularioCampo>0){
                sqlFiltro ="id_formulario_campo=" + idCrFormularioCampo + " AND ";
            }else{
                sqlFiltro ="id_formulario_campo>0 AND";
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
            
            crFormularioCampoDto = crFormularioCampoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " " + (StringManage.getValidString(orderBy).length()>0? orderBy :  " ORDER BY id_formulario_campo desc")
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return crFormularioCampoDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID CrFormularioCampo y otros filtros
     * @param idCrFormularioCampo ID Del CrFormularioCampo para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return int cantidad de registros coincidentes
     */
    public int findCantidadCrFormularioCampos(int idCrFormularioCampo, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        CrFormularioCampoDaoImpl crFormularioCampoDao = new CrFormularioCampoDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrFormularioCampo>0){
                sqlFiltro ="id_formulario_campo=" + idCrFormularioCampo + " AND ";
            }else{
                sqlFiltro ="id_formulario_campo>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_formulario_campo) as cantidad FROM " + crFormularioCampoDao.getTableName() +  " WHERE " + 
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
        
    public String getCrFormularioCamposByIdHTMLCombo(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            CrFormularioCampo[] crFormularioCamposDto = findCrFormularioCampos(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (CrFormularioCampo crFormularioCampo : crFormularioCamposDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == crFormularioCampo.getIdFormularioCampo())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+crFormularioCampo.getIdFormularioCampo()+"' "
                            + selectedStr
                            + "title='"+crFormularioCampo.getDescripcion()+"'>"
                            + crFormularioCampo.getEtiqueta()
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
    
    public String getCrFormularioCamposByIdHTMLComboVariable(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            CrFormularioCampo[] crFormularioCamposDto = findCrFormularioCampos(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 AND variable_formula<>'' " + filtroBusqueda);
            
            for (CrFormularioCampo crFormularioCampo : crFormularioCamposDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == crFormularioCampo.getIdFormularioCampo())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+crFormularioCampo.getVariableFormula()+"' "
                            + selectedStr
                            + ">"
                            + crFormularioCampo.getEtiqueta()
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
