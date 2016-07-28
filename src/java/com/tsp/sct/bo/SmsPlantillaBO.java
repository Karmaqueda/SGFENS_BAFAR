/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SmsPlantilla;
import com.tsp.sct.dao.exceptions.SmsPlantillaDaoException;
import com.tsp.sct.dao.jdbc.SmsPlantillaDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar
 */
public class SmsPlantillaBO {
    private SmsPlantilla smsPlantilla = null;

    public SmsPlantilla getSmsPlantilla() {
        return smsPlantilla;
    }

    public void setSmsPlantilla(SmsPlantilla smsPlantilla) {
        this.smsPlantilla = smsPlantilla;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SmsPlantillaBO(Connection conn){
        this.conn = conn;
    }
    
     public SmsPlantillaBO(int idSmsPlantilla, Connection conn){        
        this.conn = conn; 
        try{
           SmsPlantillaDaoImpl SmsPlantillaDaoImpl = new SmsPlantillaDaoImpl(this.conn);
            this.smsPlantilla = SmsPlantillaDaoImpl.findByPrimaryKey(idSmsPlantilla);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public SmsPlantilla findSmsPlantillabyId(int idSmsPlantilla) throws Exception{
        SmsPlantilla SmsPlantilla = null;
        
        try{
            SmsPlantillaDaoImpl SmsPlantillaDaoImpl = new SmsPlantillaDaoImpl(this.conn);
            SmsPlantilla = SmsPlantillaDaoImpl.findByPrimaryKey(idSmsPlantilla);
            if (SmsPlantilla==null){
                throw new Exception("No se encontro ningun SmsPlantilla que corresponda con los parámetros específicados.");
            }
            if (SmsPlantilla.getIdSmsPlantilla()<=0){
                throw new Exception("No se encontro ningun SmsPlantilla que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del SmsPlantilla del usuario. Error: " + e.getMessage());
        }
        
        return SmsPlantilla;
    }
    
    /**
     * Realiza una búsqueda por ID SmsPlantilla en busca de
     * coincidencias
     * @param idSmsPlantilla ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public SmsPlantilla[] findSmsPlantillas(int idSmsPlantilla, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SmsPlantilla[] smsPlantillaDto = new SmsPlantilla[0];
        SmsPlantillaDaoImpl smsPlantillaDao = new SmsPlantillaDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSmsPlantilla>0){
                sqlFiltro ="id_sms_plantilla=" + idSmsPlantilla + " AND ";
            }else{
                sqlFiltro ="id_sms_plantilla>0 AND";
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
            
            smsPlantillaDto = smsPlantillaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY id_sms_plantilla desc"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return smsPlantillaDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID SmsPlantilla y otros filtros
     * @param idSmsPlantilla ID Del SmsPlantilla para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public int findCantidadSmsPlantillas(int idSmsPlantilla, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        SmsPlantillaDaoImpl smsPlantillaDao = new SmsPlantillaDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idSmsPlantilla>0){
                sqlFiltro ="id_sms_plantilla=" + idSmsPlantilla + " AND ";
            }else{
                sqlFiltro ="id_sms_plantilla>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_sms_plantilla) as cantidad FROM " + smsPlantillaDao.getTableName() +  " WHERE " + 
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
        
    public String getSmsPlantillaesByIdHTMLCombo(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            SmsPlantilla[] smsPlantillaesDto = findSmsPlantillas(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (SmsPlantilla smsPlantilla : smsPlantillaesDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == smsPlantilla.getIdSmsPlantilla())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+smsPlantilla.getIdSmsPlantilla()+"' "
                            + selectedStr
                            + "title='"+smsPlantilla.getAliasPlantilla()+"'>"
                            + smsPlantilla.getAsunto()
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
