/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.NominaRegistroPatronal;
import com.tsp.sct.dao.jdbc.NominaRegistroPatronalDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISCesarMartinez
 */
public class NominaRegistroPatronalBO {
    private NominaRegistroPatronal nominaRegistroPatronal  = null;

    public NominaRegistroPatronal getNominaRegistroPatronal() {
        return nominaRegistroPatronal;
    }

    public void setNominaRegistroPatronal(NominaRegistroPatronal nominaRegistroPatronal) {
        this.nominaRegistroPatronal = nominaRegistroPatronal;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public NominaRegistroPatronalBO(Connection conn){
        this.conn = conn;
    }
    
    public NominaRegistroPatronalBO(int idNominaRegistroPatronal, Connection conn){        
        this.conn = conn;
        try{
            NominaRegistroPatronalDaoImpl NominaRegistroPatronalDaoImpl = new NominaRegistroPatronalDaoImpl(this.conn);
            this.nominaRegistroPatronal = NominaRegistroPatronalDaoImpl.findByPrimaryKey(idNominaRegistroPatronal);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public NominaRegistroPatronal findNominaRegistroPatronalbyId(int idNominaRegistroPatronal) throws Exception{
        NominaRegistroPatronal nominaRegistroPatronal = null;
        
        try{
            NominaRegistroPatronalDaoImpl nominaRegistroPatronalDaoImpl = new NominaRegistroPatronalDaoImpl(this.conn);
            nominaRegistroPatronal = nominaRegistroPatronalDaoImpl.findByPrimaryKey(idNominaRegistroPatronal);
            if (nominaRegistroPatronal==null){
                throw new Exception("No se encontro ningun nominaRegistroPatronal que corresponda según los parámetros específicados.");
            }
            if (nominaRegistroPatronal.getIdNominaRegPatronal()<=0){
                throw new Exception("No se encontro ningun nominaRegistroPatronal que corresponda según los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de NominaRegistroPatronal del usuario. Error: " + e.getMessage());
        }
        
        return nominaRegistroPatronal;
    }
    
    
    /**
     * Realiza una búsqueda por ID NominaRegistroPatronal en busca de
     * coincidencias
     * @param idNominaRegistroPatronal ID Del NominaRegistroPatronal para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar nominaRegistroPatronal, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO NominaRegistroPatronal
     */
    public NominaRegistroPatronal[] findNominaRegistroPatronal(int idNominaRegistroPatronal, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        NominaRegistroPatronal[] nominaRegistroPatronalDto = new NominaRegistroPatronal[0];
        NominaRegistroPatronalDaoImpl nominaRegistroPatronalDao = new NominaRegistroPatronalDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idNominaRegistroPatronal>0){
                sqlFiltro ="ID_NOMINA_REG_PATRONAL=" + idNominaRegistroPatronal + " AND ";
            }else{
                sqlFiltro ="ID_NOMINA_REG_PATRONAL>0 AND";
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
            
            nominaRegistroPatronalDto = nominaRegistroPatronalDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_NOMINA_REG_PATRONAL DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return nominaRegistroPatronalDto;
    }
    
    public String getNominaRegistroPatronalByIdHTMLCombo(int idEmpresa, int idSeleccionado, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            NominaRegistroPatronal[] nominaRegistrosPatronalDto = findNominaRegistroPatronal(-1, idEmpresa, 0, 0, filtroBusqueda);
            
            for (NominaRegistroPatronal nominaRegistroPatronal : nominaRegistrosPatronalDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado==nominaRegistroPatronal.getIdNominaRegPatronal())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+nominaRegistroPatronal.getIdNominaRegPatronal()+"' "
                            + selectedStr
                            + "title='"+nominaRegistroPatronal.getRegistroPatronal()+"'>"
                            + nominaRegistroPatronal.getRegistroPatronal()
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
