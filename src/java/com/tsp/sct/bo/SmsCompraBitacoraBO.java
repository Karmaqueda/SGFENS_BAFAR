/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SmsCompraBitacora;
import com.tsp.sct.dao.jdbc.SmsCompraBitacoraDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar
 */
public class SmsCompraBitacoraBO {
    private SmsCompraBitacora smsCompraBitacora = null;

    public SmsCompraBitacora getSmsCompraBitacora() {
        return smsCompraBitacora;
    }

    public void setSmsCompraBitacora(SmsCompraBitacora smsCompraBitacora) {
        this.smsCompraBitacora = smsCompraBitacora;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SmsCompraBitacoraBO(Connection conn){
        this.conn = conn;
    }
    
     public SmsCompraBitacoraBO(int idSmsCompraBitacora, Connection conn){        
        this.conn = conn; 
        try{
           SmsCompraBitacoraDaoImpl SmsCompraBitacoraDaoImpl = new SmsCompraBitacoraDaoImpl(this.conn);
            this.smsCompraBitacora = SmsCompraBitacoraDaoImpl.findByPrimaryKey(idSmsCompraBitacora);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public SmsCompraBitacora findSmsCompraBitacorabyId(int idSmsCompraBitacora) throws Exception{
        SmsCompraBitacora SmsCompraBitacora = null;
        
        try{
            SmsCompraBitacoraDaoImpl SmsCompraBitacoraDaoImpl = new SmsCompraBitacoraDaoImpl(this.conn);
            SmsCompraBitacora = SmsCompraBitacoraDaoImpl.findByPrimaryKey(idSmsCompraBitacora);
            if (SmsCompraBitacora==null){
                throw new Exception("No se encontro ningun SmsCompraBitacora que corresponda con los parámetros específicados.");
            }
            if (SmsCompraBitacora.getIdSmsCompraBitacora()<=0){
                throw new Exception("No se encontro ningun SmsCompraBitacora que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del SmsCompraBitacora del usuario. Error: " + e.getMessage());
        }
        
        return SmsCompraBitacora;
    }
    
    /**
     * Realiza una búsqueda por ID SmsCompraBitacora en busca de
     * coincidencias
     * @param idSmsCompraBitacora ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public SmsCompraBitacora[] findSmsCompraBitacoras(int idSmsCompraBitacora, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SmsCompraBitacora[] smsCompraBitacoraDto = new SmsCompraBitacora[0];
        SmsCompraBitacoraDaoImpl smsCompraBitacoraDao = new SmsCompraBitacoraDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSmsCompraBitacora>0){
                sqlFiltro ="id_sms_compra_bitacora=" + idSmsCompraBitacora + " AND ";
            }else{
                sqlFiltro ="id_sms_compra_bitacora>0 AND";
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
            
            smsCompraBitacoraDto = smsCompraBitacoraDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY id_sms_compra_bitacora desc"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return smsCompraBitacoraDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID SmsCompraBitacora y otros filtros
     * @param idSmsCompraBitacora ID Del SmsCompraBitacora para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public int findCantidadSmsCompraBitacoras(int idSmsCompraBitacora, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        SmsCompraBitacoraDaoImpl smsCompraBitacoraDao = new SmsCompraBitacoraDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idSmsCompraBitacora>0){
                sqlFiltro ="id_sms_compra_bitacora=" + idSmsCompraBitacora + " AND ";
            }else{
                sqlFiltro ="id_sms_compra_bitacora>0 AND ";
            }
            if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ") ";
            }else{
                sqlFiltro +=" ID_EMPRESA>0 ";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_sms_compra_bitacora) as cantidad FROM " + smsCompraBitacoraDao.getTableName() +  " WHERE " + 
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
        
}
