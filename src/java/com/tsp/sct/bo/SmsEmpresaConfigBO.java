/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SmsEmpresaConfig;
import com.tsp.sct.dao.jdbc.SmsEmpresaConfigDaoImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar
 */
public class SmsEmpresaConfigBO {
    private SmsEmpresaConfig smsEmpresaConfig = null;

    public SmsEmpresaConfig getSmsEmpresaConfig() {
        return smsEmpresaConfig;
    }

    public void setSmsEmpresaConfig(SmsEmpresaConfig smsEmpresaConfig) {
        this.smsEmpresaConfig = smsEmpresaConfig;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SmsEmpresaConfigBO(Connection conn){
        this.conn = conn;
    }
    
     public SmsEmpresaConfigBO(int idSmsEmpresaConfig, Connection conn){        
        this.conn = conn; 
        try{
           SmsEmpresaConfigDaoImpl SmsEmpresaConfigDaoImpl = new SmsEmpresaConfigDaoImpl(this.conn);
            this.smsEmpresaConfig = SmsEmpresaConfigDaoImpl.findByPrimaryKey(idSmsEmpresaConfig);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public SmsEmpresaConfig findSmsEmpresaConfigbyId(int idSmsEmpresaConfig) throws Exception{
        SmsEmpresaConfig SmsEmpresaConfig = null;
        
        try{
            SmsEmpresaConfigDaoImpl SmsEmpresaConfigDaoImpl = new SmsEmpresaConfigDaoImpl(this.conn);
            SmsEmpresaConfig = SmsEmpresaConfigDaoImpl.findByPrimaryKey(idSmsEmpresaConfig);
            if (SmsEmpresaConfig==null){
                throw new Exception("No se encontro ningun SmsEmpresaConfig que corresponda con los parámetros específicados.");
            }
            if (SmsEmpresaConfig.getIdSmsEmpresaConfig()<=0){
                throw new Exception("No se encontro ningun SmsEmpresaConfig que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del SmsEmpresaConfig del usuario. Error: " + e.getMessage());
        }
        
        return SmsEmpresaConfig;
    }
    
    /**
     * Realiza una búsqueda por ID SmsEmpresaConfig en busca de
     * coincidencias
     * @param idSmsEmpresaConfig ID Del registro para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public SmsEmpresaConfig[] findSmsEmpresaConfigs(int idSmsEmpresaConfig, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SmsEmpresaConfig[] smsEmpresaConfigDto = new SmsEmpresaConfig[0];
        SmsEmpresaConfigDaoImpl smsEmpresaConfigDao = new SmsEmpresaConfigDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSmsEmpresaConfig>0){
                sqlFiltro ="id_sms_empresa_config=" + idSmsEmpresaConfig + " AND ";
            }else{
                sqlFiltro ="id_sms_empresa_config>0 AND";
            }
            if (idEmpresa>0){                
                //sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
                sqlFiltro += " ID_EMPRESA= " + idEmpresa + " ";
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
            
            smsEmpresaConfigDto = smsEmpresaConfigDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY id_sms_empresa_config desc"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return smsEmpresaConfigDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID SmsEmpresaConfig y otros filtros
     * @param idSmsEmpresaConfig ID Del SmsEmpresaConfig para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public int findCantidadSmsEmpresaConfigs(int idSmsEmpresaConfig, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        SmsEmpresaConfigDaoImpl smsEmpresaConfigDao = new SmsEmpresaConfigDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idSmsEmpresaConfig>0){
                sqlFiltro ="id_sms_empresa_config=" + idSmsEmpresaConfig + " AND ";
            }else{
                sqlFiltro ="id_sms_empresa_config>0 AND";
            }
            if (idEmpresa>0){                
                //sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
                sqlFiltro += " ID_EMPRESA= " + idEmpresa + " ";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_sms_empresa_config) as cantidad FROM " + smsEmpresaConfigDao.getTableName() +  " WHERE " + 
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
        
    public String getSmsEmpresaConfigesByIdHTMLCombo(int idEmpresa, int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            SmsEmpresaConfig[] smsEmpresaConfigesDto = findSmsEmpresaConfigs(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (SmsEmpresaConfig smsEmpresaConfig : smsEmpresaConfigesDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == smsEmpresaConfig.getIdSmsEmpresaConfig())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+smsEmpresaConfig.getIdSmsEmpresaConfig()+"' "
                            + selectedStr
                            + "title='"+smsEmpresaConfig.getIdSmsEmpresaConfig()+"'>"
                            + smsEmpresaConfig.getIdSmsEmpresaConfig()
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
