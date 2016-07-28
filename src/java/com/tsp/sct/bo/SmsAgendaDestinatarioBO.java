/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SmsAgendaDestinatario;
import com.tsp.sct.dao.jdbc.SmsAgendaDestinatarioDaoImpl;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar
 */
public class SmsAgendaDestinatarioBO {
    private SmsAgendaDestinatario smsAgendaDestinatario = null;

    public SmsAgendaDestinatario getSmsAgendaDestinatario() {
        return smsAgendaDestinatario;
    }

    public void setSmsAgendaDestinatario(SmsAgendaDestinatario smsAgendaDestinatario) {
        this.smsAgendaDestinatario = smsAgendaDestinatario;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SmsAgendaDestinatarioBO(Connection conn){
        this.conn = conn;
    }
    
     public SmsAgendaDestinatarioBO(int idSmsAgendaDestinatario, Connection conn){        
        this.conn = conn; 
        try{
           SmsAgendaDestinatarioDaoImpl SmsAgendaDestinatarioDaoImpl = new SmsAgendaDestinatarioDaoImpl(this.conn);
            this.smsAgendaDestinatario = SmsAgendaDestinatarioDaoImpl.findByPrimaryKey(idSmsAgendaDestinatario);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public SmsAgendaDestinatario findSmsAgendaDestinatariobyId(int idSmsAgendaDestinatario) throws Exception{
        SmsAgendaDestinatario SmsAgendaDestinatario = null;
        
        try{
            SmsAgendaDestinatarioDaoImpl SmsAgendaDestinatarioDaoImpl = new SmsAgendaDestinatarioDaoImpl(this.conn);
            SmsAgendaDestinatario = SmsAgendaDestinatarioDaoImpl.findByPrimaryKey(idSmsAgendaDestinatario);
            if (SmsAgendaDestinatario==null){
                throw new Exception("No se encontro ningun SmsAgendaDestinatario que corresponda con los parámetros específicados.");
            }
            if (SmsAgendaDestinatario.getIdSmsAgendaDest()<=0){
                throw new Exception("No se encontro ningun SmsAgendaDestinatario que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del SmsAgendaDestinatario del usuario. Error: " + e.getMessage());
        }
        
        return SmsAgendaDestinatario;
    }
    
    /**
     * Realiza una búsqueda por ID SmsAgendaDestinatario en busca de
     * coincidencias
     * @param idSmsAgendaDestinatario ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public SmsAgendaDestinatario[] findSmsAgendaDestinatarios(int idSmsAgendaDestinatario, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SmsAgendaDestinatario[] smsAgendaDestinatarioDto = new SmsAgendaDestinatario[0];
        SmsAgendaDestinatarioDaoImpl smsAgendaDestinatarioDao = new SmsAgendaDestinatarioDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSmsAgendaDestinatario>0){
                sqlFiltro ="id_sms_agenda_dest=" + idSmsAgendaDestinatario + " AND ";
            }else{
                sqlFiltro ="id_sms_agenda_dest>0 AND";
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
            
            smsAgendaDestinatarioDto = smsAgendaDestinatarioDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY id_sms_agenda_dest desc"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return smsAgendaDestinatarioDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID SmsAgendaDestinatario y otros filtros
     * @param idSmsAgendaDestinatario ID Del SmsAgendaDestinatario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public int findCantidadSmsAgendaDestinatarios(int idSmsAgendaDestinatario, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        SmsAgendaDestinatarioDaoImpl smsAgendaDestinatarioDao = new SmsAgendaDestinatarioDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idSmsAgendaDestinatario>0){
                sqlFiltro ="id_sms_agenda_dest=" + idSmsAgendaDestinatario + " AND ";
            }else{
                sqlFiltro ="id_sms_agenda_dest>0 AND ";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_sms_agenda_dest) as cantidad FROM " + smsAgendaDestinatarioDao.getTableName() +  " WHERE " + 
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
        
    public String getSmsAgendaDestinatariosByIdHTMLCombo(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            SmsAgendaDestinatario[] smsAgendaDestinatarioesDto = findSmsAgendaDestinatarios(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (SmsAgendaDestinatario smsAgendaDestinatario : smsAgendaDestinatarioesDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == smsAgendaDestinatario.getIdSmsAgendaDest())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+smsAgendaDestinatario.getIdSmsAgendaDest()+"' "
                            + selectedStr
                            + "title='"+smsAgendaDestinatario.getNombre()+"'>"
                            + StringManage.getValidString(smsAgendaDestinatario.getNombre()) 
                            + " ( " + smsAgendaDestinatario.getNumeroCelular() + ")"
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
