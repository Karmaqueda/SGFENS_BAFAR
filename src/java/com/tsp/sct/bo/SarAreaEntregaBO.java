/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SarAreaEntrega;
import com.tsp.sct.dao.jdbc.SarAreaEntregaDaoImpl;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;

/**
 *
 * @author ISCesar poseidon24@hotmail.com
 * @since 19/03/2015 12:22:16 PM
 */
public class SarAreaEntregaBO {

    private SarAreaEntrega sarAreaEntrega = null;

    public SarAreaEntrega getSarAreaEntrega() {
        return sarAreaEntrega;
    }

    public void setSarAreaEntrega(SarAreaEntrega sarAreaEntrega) {
        this.sarAreaEntrega = sarAreaEntrega;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SarAreaEntregaBO(Connection conn){
        this.conn = conn;
    }
    
    public SarAreaEntregaBO(int idSarAreaEntrega, Connection conn){
        this.conn = conn;
         try{
            SarAreaEntregaDaoImpl SarAreaEntregaDaoImpl = new SarAreaEntregaDaoImpl(this.conn);
            this.sarAreaEntrega = SarAreaEntregaDaoImpl.findByPrimaryKey(idSarAreaEntrega);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * Realiza una búsqueda por ID SarAreaEntrega en busca de
     * coincidencias
     * @param idSarAreaEntrega ID Clave principal para filtrar, -1 para mostrar todos los registros
     * @param idSarUsuario ID Sar Usuario, -1 para no aplicar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SarAreaEntrega
     */
    public SarAreaEntrega[] findSarAreaEntregas(int idSarAreaEntrega, int idSarUsuario, int minLimit,int maxLimit, String filtroBusqueda) {
        SarAreaEntrega[] sarAreaEntregaDto = new SarAreaEntrega[0];
        SarAreaEntregaDaoImpl sarAreaEntregaDao = new SarAreaEntregaDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSarAreaEntrega>0){
                sqlFiltro ="ID_SAR_AREA_ENTREGA=" + idSarAreaEntrega + " AND ";
            }else{
                sqlFiltro ="ID_SAR_AREA_ENTREGA>0 AND";
            }
            if (idSarUsuario>0){                
                sqlFiltro += " ID_SAR_USUARIO = " + idSarUsuario;
            }else{
                sqlFiltro +=" ID_SAR_USUARIO>0";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            sarAreaEntregaDto = sarAreaEntregaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_SAR_AREA_ENTREGA ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return sarAreaEntregaDto;
    }
    
    public String getSarAreaEntregasByIdHTMLCombo(int idSarUsuario, int idSeleccionado, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            SarAreaEntrega[] sarAreaEntregasDto = findSarAreaEntregas(-1, idSarUsuario, 0, 0, filtroBusqueda);
            
            for (SarAreaEntrega sarAreaEntregaItem:sarAreaEntregasDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado==sarAreaEntregaItem.getIdSarAreaEntrega())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+sarAreaEntregaItem.getIdSarAreaEntrega()+"' "
                            + selectedStr
                            + "title='"+sarAreaEntregaItem.getExtSarNombre()+"'>"
                            + "[" + StringManage.getValidString(sarAreaEntregaItem.getExtSarCodprovArea())+ "] " + sarAreaEntregaItem.getExtSarNombre() 
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
