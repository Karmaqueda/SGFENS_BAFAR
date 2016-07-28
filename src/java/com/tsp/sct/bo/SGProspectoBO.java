/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SgfensProspecto;
import com.tsp.sct.dao.jdbc.SgfensProspectoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISCesarMartinez
 */
public class SGProspectoBO {
    private SgfensProspecto prospecto  = null;

    public SgfensProspecto getSgfensProspecto() {
        return prospecto;
    }

    public void setSgfensProspecto(SgfensProspecto prospecto) {
        this.prospecto = prospecto;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SGProspectoBO(Connection conn){
        this.conn = conn;
    }
    
    public SGProspectoBO(int idSgfensProspecto, Connection conn){        
        this.conn = conn;
        try{
            SgfensProspectoDaoImpl SgfensProspectoDaoImpl = new SgfensProspectoDaoImpl(this.conn);
            this.prospecto = SgfensProspectoDaoImpl.findByPrimaryKey(idSgfensProspecto);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public SgfensProspecto findProspectobyId(int idProspecto) throws Exception{
        SgfensProspecto prospecto = null;
        
        try{
            SgfensProspectoDaoImpl prospectoDaoImpl = new SgfensProspectoDaoImpl(this.conn);
            prospecto = prospectoDaoImpl.findByPrimaryKey(idProspecto);
            if (prospecto==null){
                throw new Exception("No se encontro ningun prospecto que corresponda según los parámetros específicados.");
            }
            if (prospecto.getIdProspecto()<=0){
                throw new Exception("No se encontro ningun prospecto que corresponda según los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de Prospecto del usuario. Error: " + e.getMessage());
        }
        
        return prospecto;
    }
    
    
    /**
     * Realiza una búsqueda por ID SgfensProspecto en busca de
     * coincidencias
     * @param idSgfensProspecto ID Del Prospecto para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar prospecto, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensProspecto
     */
    public SgfensProspecto[] findProspecto(int idSgfensProspecto, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SgfensProspecto[] prospectoDto = new SgfensProspecto[0];
        SgfensProspectoDaoImpl prospectoDao = new SgfensProspectoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSgfensProspecto>0){
                sqlFiltro ="ID_PROSPECTO=" + idSgfensProspecto + " AND ";
            }else{
                sqlFiltro ="ID_PROSPECTO>0 AND";
            }
            if (idEmpresa>0){
                //sqlFiltro +=" ID_EMPRESA=" + idEmpresa;
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
            
            prospectoDto = prospectoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY RAZON_SOCIAL ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return prospectoDto;
    }
    
    public String getProspectosByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            SgfensProspecto[] clientesDto = findProspecto(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (SgfensProspecto itemSgfensProspecto:clientesDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado==itemSgfensProspecto.getIdProspecto())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+itemSgfensProspecto.getIdProspecto()+"' "
                            + selectedStr
                            + "title='"+itemSgfensProspecto.getRazonSocial()+"'>"
                            + itemSgfensProspecto.getRazonSocial()
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
