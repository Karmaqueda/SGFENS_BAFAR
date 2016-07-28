/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.DiccionarioIngresosEgresos;
import com.tsp.sct.dao.jdbc.DiccionarioIngresosEgresosDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesarMartinez
 */
public class DiccionarioIngresosEgresosBO {
    private DiccionarioIngresosEgresos diccionarioIngresosEgresos  = null;

    public DiccionarioIngresosEgresos getDiccionarioIngresosEgresos() {
        return diccionarioIngresosEgresos;
    }

    public void setDiccionarioIngresosEgresos(DiccionarioIngresosEgresos diccionarioIngresosEgresos) {
        this.diccionarioIngresosEgresos = diccionarioIngresosEgresos;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public DiccionarioIngresosEgresosBO(Connection conn){
        this.conn = conn;
    }
    
    public DiccionarioIngresosEgresosBO(int idDiccionarioIngresosEgresos, Connection conn){        
        this.conn = conn;
        try{
            DiccionarioIngresosEgresosDaoImpl DiccionarioIngresosEgresosDaoImpl = new DiccionarioIngresosEgresosDaoImpl(this.conn);
            this.diccionarioIngresosEgresos = DiccionarioIngresosEgresosDaoImpl.findByPrimaryKey(idDiccionarioIngresosEgresos);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public DiccionarioIngresosEgresos findDiccionarioIngresosEgresosById(int idDiccionarioIngresosEgresos) throws Exception{
        DiccionarioIngresosEgresos diccionarioIngresosEgresos = null;
        
        try{
            DiccionarioIngresosEgresosDaoImpl diccionarioIngresosEgresosDaoImpl = new DiccionarioIngresosEgresosDaoImpl(this.conn);
            diccionarioIngresosEgresos = diccionarioIngresosEgresosDaoImpl.findByPrimaryKey(idDiccionarioIngresosEgresos);
            if (diccionarioIngresosEgresos==null){
                throw new Exception("No se encontro ningun diccionarioIngresosEgresos que corresponda según los parámetros específicados.");
            }
            if (diccionarioIngresosEgresos.getIdDiccionarioIE()<=0){
                throw new Exception("No se encontro ningun diccionarioIngresosEgresos que corresponda según los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de DiccionarioIngresosEgresos del usuario. Error: " + e.getMessage());
        }
        
        return diccionarioIngresosEgresos;
    }
    
    
    /**
     * Realiza una búsqueda por ID DiccionarioIngresosEgresos en busca de
     * coincidencias
     * @param idDiccionarioIngresosEgresos ID Del DiccionarioIngresosEgresos para filtrar, -1 para mostrar todos los registros
     * @param filtroGenerales  Flag para indicar si se agregaran en la busqueda los registros Generales que aplican a cualquier empresa
     * @param idEmpresa ID de la Empresa a filtrar diccionarioIngresosEgresos, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO DiccionarioIngresosEgresos
     */
    public DiccionarioIngresosEgresos[] findDiccionarioIngresosEgresos(int idDiccionarioIngresosEgresos, boolean filtroGenerales, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        DiccionarioIngresosEgresos[] diccionarioIngresosEgresosDto = new DiccionarioIngresosEgresos[0];
        DiccionarioIngresosEgresosDaoImpl diccionarioIngresosEgresosDao = new DiccionarioIngresosEgresosDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idDiccionarioIngresosEgresos>0){
                sqlFiltro ="ID_DICCIONARIO_I_E=" + idDiccionarioIngresosEgresos + " AND ";
            }else{
                sqlFiltro ="ID_DICCIONARIO_I_E>0 AND";
            }
            if (idEmpresa>0){
                sqlFiltro += " (ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")"
                        + " " + (filtroGenerales?"OR ES_GENERAL=1":"") + ") ";
            }else{
                sqlFiltro +=" (ID_EMPRESA>0 "
                        + " " + (filtroGenerales?"OR ES_GENERAL=1":"") + ") ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            diccionarioIngresosEgresosDto = diccionarioIngresosEgresosDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_DICCIONARIO_I_E DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return diccionarioIngresosEgresosDto;
    }
    
    /**
     * Realiza una búsqueda de la cantidad de coincidencias por ID DiccionarioIngresosEgresos y otros filtros
     * @param idDiccionarioIngresosEgresos ID Del DiccionarioIngresosEgresos para filtrar, -1 para mostrar todos los registros
     * @param filtroGenerales  Flag para indicar si se agregaran en la busqueda los registros Generales que aplican a cualquier empresa
     * @param idEmpresa ID de la Empresa a filtrar diccionarioIngresosEgresos, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO DiccionarioIngresosEgresos
     */
    public int findCantidadDiccionarioIngresosEgresos(int idDiccionarioIngresosEgresos, boolean filtroGenerales, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        try {
            String sqlFiltro="";
            if (idDiccionarioIngresosEgresos>0){
                sqlFiltro ="ID_DICCIONARIO_I_E=" + idDiccionarioIngresosEgresos + " AND ";
            }else{
                sqlFiltro ="ID_DICCIONARIO_I_E>0 AND";
            }
            if (idEmpresa>0){
                sqlFiltro += " (ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")"
                        + " " + (filtroGenerales?"OR ES_GENERAL=1":"") + ") ";
            }else{
                sqlFiltro +=" (ID_EMPRESA>0 "
                        + " " + (filtroGenerales?"OR ES_GENERAL=1":"") + ") ";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(ID_DICCIONARIO_I_E) as cantidad FROM diccionario_ingresos_egresos WHERE " + 
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
    
    public String getDiccionarioIngresosEgresosesByIdHTMLCombo(int idEmpresa, int idSeleccionado, boolean filtroGenerales, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            DiccionarioIngresosEgresos[] coincidencias = findDiccionarioIngresosEgresos(-1, filtroGenerales, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (DiccionarioIngresosEgresos item:coincidencias){
                try{
                    String selectedStr="";

                    if (idSeleccionado==item.getIdDiccionarioIE()){
                        selectedStr = " selected ";
                    }
                        

                    strHTMLCombo += "<option value='"+item.getIdDiccionarioIE()+"' "
                            + selectedStr
                            + "title='"+item.getNombre()+"'>"
                            + item.getNombre()
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