/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SmsAgendaGrupo;
import com.tsp.sct.dao.jdbc.SmsAgendaGrupoDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar
 */
public class SmsAgendaGrupoBO {
    private SmsAgendaGrupo smsAgendaGrupo = null;

    public SmsAgendaGrupo getSmsAgendaGrupo() {
        return smsAgendaGrupo;
    }

    public void setSmsAgendaGrupo(SmsAgendaGrupo smsAgendaGrupo) {
        this.smsAgendaGrupo = smsAgendaGrupo;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SmsAgendaGrupoBO(Connection conn){
        this.conn = conn;
    }
    
     public SmsAgendaGrupoBO(int idSmsAgendaGrupo, Connection conn){        
        this.conn = conn; 
        try{
           SmsAgendaGrupoDaoImpl SmsAgendaGrupoDaoImpl = new SmsAgendaGrupoDaoImpl(this.conn);
            this.smsAgendaGrupo = SmsAgendaGrupoDaoImpl.findByPrimaryKey(idSmsAgendaGrupo);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public SmsAgendaGrupo findSmsAgendaGrupobyId(int idSmsAgendaGrupo) throws Exception{
        SmsAgendaGrupo SmsAgendaGrupo = null;
        
        try{
            SmsAgendaGrupoDaoImpl SmsAgendaGrupoDaoImpl = new SmsAgendaGrupoDaoImpl(this.conn);
            SmsAgendaGrupo = SmsAgendaGrupoDaoImpl.findByPrimaryKey(idSmsAgendaGrupo);
            if (SmsAgendaGrupo==null){
                throw new Exception("No se encontro ningun SmsAgendaGrupo que corresponda con los parámetros específicados.");
            }
            if (SmsAgendaGrupo.getIdSmsAgendaGrupo()<=0){
                throw new Exception("No se encontro ningun SmsAgendaGrupo que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del SmsAgendaGrupo del usuario. Error: " + e.getMessage());
        }
        
        return SmsAgendaGrupo;
    }
    
    /**
     * Realiza una búsqueda por ID SmsAgendaGrupo en busca de
     * coincidencias
     * @param idSmsAgendaGrupo ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public SmsAgendaGrupo[] findSmsAgendaGrupos(int idSmsAgendaGrupo, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SmsAgendaGrupo[] smsAgendaGrupoDto = new SmsAgendaGrupo[0];
        SmsAgendaGrupoDaoImpl smsAgendaGrupoDao = new SmsAgendaGrupoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSmsAgendaGrupo>0){
                sqlFiltro ="id_sms_agenda_grupo=" + idSmsAgendaGrupo + " AND ";
            }else{
                sqlFiltro ="id_sms_agenda_grupo>0 AND";
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
            
            smsAgendaGrupoDto = smsAgendaGrupoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY id_sms_agenda_grupo desc"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return smsAgendaGrupoDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID SmsAgendaGrupo y otros filtros
     * @param idSmsAgendaGrupo ID Del SmsAgendaGrupo para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public int findCantidadSmsAgendaGrupos(int idSmsAgendaGrupo, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        SmsAgendaGrupoDaoImpl smsAgendaGrupoDao = new SmsAgendaGrupoDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idSmsAgendaGrupo>0){
                sqlFiltro ="id_sms_agenda_grupo=" + idSmsAgendaGrupo + " AND ";
            }else{
                sqlFiltro ="id_sms_agenda_grupo>0 AND";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_sms_agenda_grupo) as cantidad FROM " + smsAgendaGrupoDao.getTableName() +  " WHERE " + 
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
        
    public String getSmsAgendaGruposByIdHTMLCombo(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            SmsAgendaGrupo[] smsAgendaGrupoesDto = findSmsAgendaGrupos(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (SmsAgendaGrupo smsAgendaGrupo : smsAgendaGrupoesDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == smsAgendaGrupo.getIdSmsAgendaGrupo())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+smsAgendaGrupo.getIdSmsAgendaGrupo()+"' "
                            + selectedStr
                            + "title='"+smsAgendaGrupo.getDescripcionGrupo()+"'>"
                            + smsAgendaGrupo.getNombreGrupo()
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
