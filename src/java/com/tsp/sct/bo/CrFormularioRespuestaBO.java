/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CrFormularioRespuesta;
import com.tsp.sct.dao.jdbc.CrFormularioRespuestaDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar
 */
public class CrFormularioRespuestaBO {
    private CrFormularioRespuesta crFormularioRespuesta = null;

    public CrFormularioRespuesta getCrFormularioRespuesta() {
        return crFormularioRespuesta;
    }

    public void setCrFormularioRespuesta(CrFormularioRespuesta crFormularioRespuesta) {
        this.crFormularioRespuesta = crFormularioRespuesta;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CrFormularioRespuestaBO(Connection conn){
        this.conn = conn;
    }
    
     public CrFormularioRespuestaBO(int idCrFormularioRespuesta, Connection conn){        
        this.conn = conn; 
        try{
           CrFormularioRespuestaDaoImpl CrFormularioRespuestaDaoImpl = new CrFormularioRespuestaDaoImpl(this.conn);
            this.crFormularioRespuesta = CrFormularioRespuestaDaoImpl.findByPrimaryKey(idCrFormularioRespuesta);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public CrFormularioRespuesta findCrFormularioRespuestabyId(int idCrFormularioRespuesta) throws Exception{
        CrFormularioRespuesta CrFormularioRespuesta = null;
        
        try{
            CrFormularioRespuestaDaoImpl CrFormularioRespuestaDaoImpl = new CrFormularioRespuestaDaoImpl(this.conn);
            CrFormularioRespuesta = CrFormularioRespuestaDaoImpl.findByPrimaryKey(idCrFormularioRespuesta);
            if (CrFormularioRespuesta==null){
                throw new Exception("No se encontro ningun CrFormularioRespuesta que corresponda con los parámetros específicados.");
            }
            if (CrFormularioRespuesta.getIdFormularioRespuesta()<=0){
                throw new Exception("No se encontro ningun CrFormularioRespuesta que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del CrFormularioRespuesta del usuario. Error: " + e.getMessage());
        }
        
        return CrFormularioRespuesta;
    }
    
    /**
     * Realiza una búsqueda por ID CrFormularioRespuesta en busca de
     * coincidencias
     * @param idCrFormularioRespuesta ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO CrFormularioRespuesta
     */
    public CrFormularioRespuesta[] findCrFormularioRespuestas(int idCrFormularioRespuesta, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CrFormularioRespuesta[] crFormularioRespuestaDto = new CrFormularioRespuesta[0];
        CrFormularioRespuestaDaoImpl crFormularioRespuestaDao = new CrFormularioRespuestaDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCrFormularioRespuesta>0){
                sqlFiltro ="id_formulario_respuesta=" + idCrFormularioRespuesta + " AND ";
            }else{
                sqlFiltro ="id_formulario_respuesta>0 AND";
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
            
            crFormularioRespuestaDto = crFormularioRespuestaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY id_formulario_respuesta desc"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return crFormularioRespuestaDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID CrFormularioRespuesta y otros filtros
     * @param idCrFormularioRespuesta ID Del CrFormularioRespuesta para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return int cantidad de registros coincidentes
     */
    public int findCantidadCrFormularioRespuestas(int idCrFormularioRespuesta, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        CrFormularioRespuestaDaoImpl crFormularioRespuestaDao = new CrFormularioRespuestaDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idCrFormularioRespuesta>0){
                sqlFiltro ="id_formulario_respuesta=" + idCrFormularioRespuesta + " AND ";
            }else{
                sqlFiltro ="id_formulario_respuesta>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_formulario_respuesta) as cantidad FROM " + crFormularioRespuestaDao.getTableName() +  " WHERE " + 
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
        
    public String getCrFormularioRespuestasByIdHTMLCombo(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            CrFormularioRespuesta[] crFormularioRespuestasDto = findCrFormularioRespuestas(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (CrFormularioRespuesta crFormularioRespuesta : crFormularioRespuestasDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == crFormularioRespuesta.getIdFormularioRespuesta())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+crFormularioRespuesta.getIdFormularioRespuesta()+"' "
                            + selectedStr
                            + "title='"+crFormularioRespuesta.getDescripcion()+"'>"
                            + crFormularioRespuesta.getValor()
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
