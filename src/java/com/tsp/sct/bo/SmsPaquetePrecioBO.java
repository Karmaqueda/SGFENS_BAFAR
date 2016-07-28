/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SmsPaquetePrecio;
import com.tsp.sct.dao.jdbc.SmsPaquetePrecioDaoImpl;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ISCesar
 */
public class SmsPaquetePrecioBO {
    private SmsPaquetePrecio smsPaquetePrecio = null;

    public SmsPaquetePrecio getSmsPaquetePrecio() {
        return smsPaquetePrecio;
    }

    public void setSmsPaquetePrecio(SmsPaquetePrecio smsPaquetePrecio) {
        this.smsPaquetePrecio = smsPaquetePrecio;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SmsPaquetePrecioBO(Connection conn){
        this.conn = conn;
    }
    
     public SmsPaquetePrecioBO(int idSmsPaquetePrecio, Connection conn){        
        this.conn = conn; 
        try{
           SmsPaquetePrecioDaoImpl SmsPaquetePrecioDaoImpl = new SmsPaquetePrecioDaoImpl(this.conn);
            this.smsPaquetePrecio = SmsPaquetePrecioDaoImpl.findByPrimaryKey(idSmsPaquetePrecio);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public SmsPaquetePrecio findSmsPaquetePreciobyId(int idSmsPaquetePrecio) throws Exception{
        SmsPaquetePrecio SmsPaquetePrecio = null;
        
        try{
            SmsPaquetePrecioDaoImpl SmsPaquetePrecioDaoImpl = new SmsPaquetePrecioDaoImpl(this.conn);
            SmsPaquetePrecio = SmsPaquetePrecioDaoImpl.findByPrimaryKey(idSmsPaquetePrecio);
            if (SmsPaquetePrecio==null){
                throw new Exception("No se encontro ningun SmsPaquetePrecio que corresponda con los parámetros específicados.");
            }
            if (SmsPaquetePrecio.getIdSmsPaquetePrecio()<=0){
                throw new Exception("No se encontro ningun SmsPaquetePrecio que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del SmsPaquetePrecio del usuario. Error: " + e.getMessage());
        }
        
        return SmsPaquetePrecio;
    }
    
    /**
     * Realiza una búsqueda por ID SmsPaquetePrecio en busca de
     * coincidencias
     * @param idSmsPaquetePrecio ID Del registro para filtrar, -1 para mostrar todos los registros
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public SmsPaquetePrecio[] findSmsPaquetePrecios(int idSmsPaquetePrecio, int minLimit,int maxLimit, String filtroBusqueda) {
        SmsPaquetePrecio[] smsPaquetePrecioDto = new SmsPaquetePrecio[0];
        SmsPaquetePrecioDaoImpl smsPaquetePrecioDao = new SmsPaquetePrecioDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idSmsPaquetePrecio>0){
                sqlFiltro ="id_sms_paquete_precio=" + idSmsPaquetePrecio + "  ";
            }else{
                sqlFiltro ="id_sms_paquete_precio>0 ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            smsPaquetePrecioDto = smsPaquetePrecioDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY id_sms_paquete_precio asc"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return smsPaquetePrecioDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID SmsPaquetePrecio y otros filtros
     * @param idSmsPaquetePrecio ID Del SmsPaquetePrecio para filtrar, -1 para mostrar todos los registros
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public int findCantidadSmsPaquetePrecios(int idSmsPaquetePrecio, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        SmsPaquetePrecioDaoImpl smsPaquetePrecioDao = new SmsPaquetePrecioDaoImpl(this.conn);
        try {
            String sqlFiltro;
            if (idSmsPaquetePrecio>0){
                sqlFiltro ="id_sms_paquete_precio=" + idSmsPaquetePrecio + " ";
            }else{
                sqlFiltro ="id_sms_paquete_precio>0 ";
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
            ResultSet rs = stmt.executeQuery("SELECT COUNT(id_sms_paquete_precio) as cantidad FROM " + smsPaquetePrecioDao.getTableName() +  " WHERE " + 
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
        
    public String getSmsPaquetePreciosByIdHTMLCombo(int idSeleccionado ,int minLimit,int maxLimit, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            SmsPaquetePrecio[] smsPaquetePrecioesDto = findSmsPaquetePrecios(-1, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (SmsPaquetePrecio smsPaquetePrecio : smsPaquetePrecioesDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado == smsPaquetePrecio.getIdSmsPaquetePrecio())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+smsPaquetePrecio.getIdSmsPaquetePrecio()+"' "
                            + selectedStr
                            + "title='"+smsPaquetePrecio.getNombrePaquete()+"'>"
                            + StringManage.getValidString(smsPaquetePrecio.getNombrePaquete()) 
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
