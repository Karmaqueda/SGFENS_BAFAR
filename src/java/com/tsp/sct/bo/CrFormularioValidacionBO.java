/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CrFormularioValidacion;
import com.tsp.sct.dao.jdbc.CrFormularioValidacionDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar
 */
public class CrFormularioValidacionBO {
    private CrFormularioValidacion crFormularioValidacion = null;

    public CrFormularioValidacion getCrFormularioValidacion() {
        return crFormularioValidacion;
    }

    public void setCrFormularioValidacion(CrFormularioValidacion crFormularioValidacion) {
        this.crFormularioValidacion = crFormularioValidacion;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CrFormularioValidacionBO(Connection conn){
        this.conn = conn;
    }
    
     public CrFormularioValidacionBO(int idCrFormularioValidacion, Connection conn){        
        this.conn = conn; 
        try{
           CrFormularioValidacionDaoImpl CrFormularioValidacionDaoImpl = new CrFormularioValidacionDaoImpl(this.conn);
            this.crFormularioValidacion = CrFormularioValidacionDaoImpl.findByPrimaryKey(idCrFormularioValidacion);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CrFormularioValidacion findCrFormularioValidacionbyId(int idCrFormularioValidacion) throws Exception{
        CrFormularioValidacion CrFormularioValidacion = null;
        
        try{
            CrFormularioValidacionDaoImpl CrFormularioValidacionDaoImpl = new CrFormularioValidacionDaoImpl(this.conn);
            CrFormularioValidacion = CrFormularioValidacionDaoImpl.findByPrimaryKey(idCrFormularioValidacion);
            if (CrFormularioValidacion==null){
                throw new Exception("No se encontro ningun CrFormularioValidacion que corresponda con los parámetros específicados.");
            }
            if (CrFormularioValidacion.getIdFormularioValidacion()<=0){
                throw new Exception("No se encontro ningun CrFormularioValidacion que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CrFormularioValidacion del usuario. Error: " + e.getMessage());
        }
        
        return CrFormularioValidacion;
    }
    
    /**
     * Realiza una búsqueda por ID CrFormularioValidacion en busca de
     * coincidencias
     * @param idCrFormularioValidacion ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CrFormularioValidacion
     */
    public CrFormularioValidacion[] findCrFormularioValidacions(int idCrFormularioValidacion, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CrFormularioValidacion[] crFormularioValidacionDto = new CrFormularioValidacion[0];
        CrFormularioValidacionDaoImpl crFormularioValidacionDao = new CrFormularioValidacionDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCrFormularioValidacion>0){
                sqlFiltro ="id_formulario_validacion=" + idCrFormularioValidacion + " AND ";
            }else{
                sqlFiltro ="id_formulario_validacion>0 AND";
            }
            if (idEmpresa>0){                
                sqlFiltro += " (ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ") OR is_creado_sistema=1)";
            }else{
                sqlFiltro +=" (ID_EMPRESA>0 OR is_creado_sistema=1)";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            crFormularioValidacionDto = crFormularioValidacionDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY id_formulario_validacion desc"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return crFormularioValidacionDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID CrFormularioValidacion y otros filtros
     * @param idCrFormularioValidacion ID Del CrFormularioValidacion para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return int cantidad de registros coincidentes
     */
    public int findCantidadCrFormularioValidacions(int idCrFormularioValidacion, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        CrFormularioValidacionDaoImpl crFormularioValidacionDao = new CrFormularioValidacionDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrFormularioValidacion>0){
                sqlFiltro ="id_formulario_validacion=" + idCrFormularioValidacion + " AND ";
            }else{
                sqlFiltro ="id_formulario_validacion>0 AND";
            }
            if (idEmpresa>0){                
                sqlFiltro += " (ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ") OR is_creado_sistema=1) ";
            }else{
                sqlFiltro +=" (ID_EMPRESA>0 OR is_creado_sistema=1) ";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_formulario_validacion) as cantidad FROM " + crFormularioValidacionDao.getTableName() +  " WHERE " + 
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
        
    public String getCrFormularioValidacionsByIdHTMLCombo(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            CrFormularioValidacion[] crFormularioValidacionesDto = findCrFormularioValidacions(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (CrFormularioValidacion crFormularioValidacion : crFormularioValidacionesDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == crFormularioValidacion.getIdFormularioValidacion())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+crFormularioValidacion.getIdFormularioValidacion()+"' "
                            + selectedStr
                            + "title='"+crFormularioValidacion.getDescripcion()+"'>"
                            + crFormularioValidacion.getNombre()
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
