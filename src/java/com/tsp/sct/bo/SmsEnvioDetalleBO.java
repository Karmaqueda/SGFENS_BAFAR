/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SmsEnvioDetalle;
import com.tsp.sct.dao.jdbc.SmsEnvioDetalleDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar
 */
public class SmsEnvioDetalleBO {
    private SmsEnvioDetalle smsEnvioDetalle = null;
    
    public static final int ENV_NO_ENVIADO = 0;
    public static final int ENV_ENVIADO = 1;
    public static final int ENV_PROCESANDO = 2;

    public SmsEnvioDetalle getSmsEnvioDetalle() {
        return smsEnvioDetalle;
    }

    public void setSmsEnvioDetalle(SmsEnvioDetalle smsEnvioDetalle) {
        this.smsEnvioDetalle = smsEnvioDetalle;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SmsEnvioDetalleBO(Connection conn){
        this.conn = conn;
    }
    
     public SmsEnvioDetalleBO(int idSmsEnvioDetalle, Connection conn){        
        this.conn = conn; 
        try{
           SmsEnvioDetalleDaoImpl SmsEnvioDetalleDaoImpl = new SmsEnvioDetalleDaoImpl(this.conn);
            this.smsEnvioDetalle = SmsEnvioDetalleDaoImpl.findByPrimaryKey(idSmsEnvioDetalle);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public SmsEnvioDetalle findSmsEnvioDetallebyId(int idSmsEnvioDetalle) throws Exception{
        SmsEnvioDetalle SmsEnvioDetalle = null;
        
        try{
            SmsEnvioDetalleDaoImpl SmsEnvioDetalleDaoImpl = new SmsEnvioDetalleDaoImpl(this.conn);
            SmsEnvioDetalle = SmsEnvioDetalleDaoImpl.findByPrimaryKey(idSmsEnvioDetalle);
            if (SmsEnvioDetalle==null){
                throw new Exception("No se encontro ningun SmsEnvioDetalle que corresponda con los parámetros específicados.");
            }
            if (SmsEnvioDetalle.getIdSmsEnvioDetalle()<=0){
                throw new Exception("No se encontro ningun SmsEnvioDetalle que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del SmsEnvioDetalle del usuario. Error: " + e.getMessage());
        }
        
        return SmsEnvioDetalle;
    }
    
    /**
     * Realiza una búsqueda por ID SmsEnvioDetalle en busca de
     * coincidencias
     * @param idSmsEnvioDetalle ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public SmsEnvioDetalle[] findSmsEnvioDetalles(int idSmsEnvioDetalle, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SmsEnvioDetalle[] smsEnvioDetalleDto = new SmsEnvioDetalle[0];
        SmsEnvioDetalleDaoImpl smsEnvioDetalleDao = new SmsEnvioDetalleDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSmsEnvioDetalle>0){
                sqlFiltro ="id_sms_envio_detalle=" + idSmsEnvioDetalle + " AND ";
            }else{
                sqlFiltro ="id_sms_envio_detalle>0 AND ";
            }
            if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ") ";
            }else{
                sqlFiltro +=" ID_EMPRESA>=0 ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            smsEnvioDetalleDto = smsEnvioDetalleDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY fecha_hr_envio DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return smsEnvioDetalleDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID SmsEnvioDetalle y otros filtros
     * @param idSmsEnvioDetalle ID Del SmsEnvioDetalle para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public int findCantidadSmsEnvioDetalles(int idSmsEnvioDetalle, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        SmsEnvioDetalleDaoImpl smsEnvioDetalleDao = new SmsEnvioDetalleDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idSmsEnvioDetalle>0){
                sqlFiltro ="id_sms_envio_detalle=" + idSmsEnvioDetalle + " AND ";
            }else{
                sqlFiltro ="id_sms_envio_detalle>0 AND ";
            }
            if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ") ";
            }else{
                sqlFiltro +=" ID_EMPRESA>=0 ";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_sms_envio_detalle) as cantidad FROM " + smsEnvioDetalleDao.getTableName() +  " WHERE " + 
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
        
    public String getSmsEnvioDetalleesByIdHTMLCombo(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            SmsEnvioDetalle[] smsEnvioDetalleesDto = findSmsEnvioDetalles(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (SmsEnvioDetalle smsEnvioDetalle : smsEnvioDetalleesDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == smsEnvioDetalle.getIdSmsEnvioDetalle())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+smsEnvioDetalle.getIdSmsEnvioDetalle()+"' "
                            + selectedStr
                            + "title='"+smsEnvioDetalle.getIdSmsEnvioDetalle()+"'>"
                            + "(" + smsEnvioDetalle.getIdSmsEnvioDetalle() + ") " + smsEnvioDetalle.getAsunto()
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
