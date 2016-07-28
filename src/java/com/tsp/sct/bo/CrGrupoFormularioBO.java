/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CrGrupoFormulario;
import com.tsp.sct.dao.jdbc.CrGrupoFormularioDaoImpl;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar
 */
public class CrGrupoFormularioBO {
    private CrGrupoFormulario crGrupoFormulario = null;
    private String orderBy = null;

    public CrGrupoFormulario getCrGrupoFormulario() {
        return crGrupoFormulario;
    }

    public void setCrGrupoFormulario(CrGrupoFormulario crGrupoFormulario) {
        this.crGrupoFormulario = crGrupoFormulario;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CrGrupoFormularioBO(Connection conn){
        this.conn = conn;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    
    public CrGrupoFormularioBO(int idCrGrupoFormulario, Connection conn){        
        this.conn = conn; 
        try{
           CrGrupoFormularioDaoImpl CrGrupoFormularioDaoImpl = new CrGrupoFormularioDaoImpl(this.conn);
            this.crGrupoFormulario = CrGrupoFormularioDaoImpl.findByPrimaryKey(idCrGrupoFormulario);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CrGrupoFormulario findCrGrupoFormulariobyId(int idCrGrupoFormulario) throws Exception{
        CrGrupoFormulario CrGrupoFormulario = null;
        
        try{
            CrGrupoFormularioDaoImpl CrGrupoFormularioDaoImpl = new CrGrupoFormularioDaoImpl(this.conn);
            CrGrupoFormulario = CrGrupoFormularioDaoImpl.findByPrimaryKey(idCrGrupoFormulario);
            if (CrGrupoFormulario==null){
                throw new Exception("No se encontro ningun CrGrupoFormulario que corresponda con los parámetros específicados.");
            }
            if (CrGrupoFormulario.getIdGrupoFormulario()<=0){
                throw new Exception("No se encontro ningun CrGrupoFormulario que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CrGrupoFormulario del usuario. Error: " + e.getMessage());
        }
        
        return CrGrupoFormulario;
    }
    
    /**
     * Realiza una búsqueda por ID CrGrupoFormulario en busca de
     * coincidencias
     * @param idCrGrupoFormulario ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CrGrupoFormulario
     */
    public CrGrupoFormulario[] findCrGrupoFormularios(int idCrGrupoFormulario, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CrGrupoFormulario[] crGrupoFormularioDto = new CrGrupoFormulario[0];
        CrGrupoFormularioDaoImpl crGrupoFormularioDao = new CrGrupoFormularioDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCrGrupoFormulario>0){
                sqlFiltro ="id_grupo_formulario=" + idCrGrupoFormulario + " AND ";
            }else{
                sqlFiltro ="id_grupo_formulario>0 AND";
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
            
            crGrupoFormularioDto = crGrupoFormularioDao.findByDynamicWhere( 
                    sqlFiltro
                    + " " + (StringManage.getValidString(orderBy).length()>0? orderBy : "ORDER BY id_grupo_formulario desc")
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return crGrupoFormularioDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID CrGrupoFormulario y otros filtros
     * @param idCrGrupoFormulario ID Del CrGrupoFormulario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return int cantidad de registros coincidentes
     */
    public int findCantidadCrGrupoFormularios(int idCrGrupoFormulario, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        CrGrupoFormularioDaoImpl crGrupoFormularioDao = new CrGrupoFormularioDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrGrupoFormulario>0){
                sqlFiltro ="id_grupo_formulario=" + idCrGrupoFormulario + " AND ";
            }else{
                sqlFiltro ="id_grupo_formulario>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_grupo_formulario) as cantidad FROM " + crGrupoFormularioDao.getTableName() +  " WHERE " + 
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
        
    public String getCrGrupoFormulariosByIdHTMLCombo(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            CrGrupoFormulario[] crGrupoFormularioesDto = findCrGrupoFormularios(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (CrGrupoFormulario crGrupoFormulario : crGrupoFormularioesDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == crGrupoFormulario.getIdGrupoFormulario())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+crGrupoFormulario.getIdGrupoFormulario()+"' "
                            + selectedStr
                            + "title='"+crGrupoFormulario.getDescripcion()+"'>"
                            + crGrupoFormulario.getNombre()
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
