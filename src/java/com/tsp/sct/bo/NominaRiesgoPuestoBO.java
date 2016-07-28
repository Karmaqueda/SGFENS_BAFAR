/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.NominaRiesgoPuesto;
import com.tsp.sct.dao.jdbc.NominaRiesgoPuestoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class NominaRiesgoPuestoBO {
    
    private NominaRiesgoPuesto nominaRiesgoPuesto = null;

    public NominaRiesgoPuesto getNominaRiesgoPuesto() {
        return nominaRiesgoPuesto;
    }

    public void setNominaRiesgoPuesto(NominaRiesgoPuesto nominaRiesgoPuesto) {
        this.nominaRiesgoPuesto = nominaRiesgoPuesto;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public NominaRiesgoPuestoBO(Connection conn){
        this.conn = conn;
    }
    
    public NominaRiesgoPuestoBO(int idNominaRiesgoPuesto, Connection conn){
        this.conn = conn;
        try{
            NominaRiesgoPuestoDaoImpl NominaRiesgoPuestoDaoImpl = new NominaRiesgoPuestoDaoImpl(this.conn);
            this.nominaRiesgoPuesto = NominaRiesgoPuestoDaoImpl.findByPrimaryKey(idNominaRiesgoPuesto);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public NominaRiesgoPuesto findNominaRiesgoPuestobyId(int idNominaRiesgoPuesto) throws Exception{
        NominaRiesgoPuesto NominaRiesgoPuesto = null;
        
        try{
            NominaRiesgoPuestoDaoImpl NominaRiesgoPuestoDaoImpl = new NominaRiesgoPuestoDaoImpl(this.conn);
            NominaRiesgoPuesto = NominaRiesgoPuestoDaoImpl.findByPrimaryKey(idNominaRiesgoPuesto);
            if (NominaRiesgoPuesto==null){
                throw new Exception("No se encontro ninguna Riesgo Puesto que corresponda con los parámetros específicados.");
            }
            if (NominaRiesgoPuesto.getIdRiesgoPuesto()<=0){
                throw new Exception("No se encontro ninguna Riesgo Puesto que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Riesgo Puesto del usuario. Error: " + e.getMessage());
        }
        
        return NominaRiesgoPuesto;
    }
          
    /**
     * Realiza una búsqueda por ID Puesto en busca de
     * coincidencias
     * @param idNominaRiesgoPuesto ID Del puesto para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar nominaRiesgoPuesto, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO NominaRiesgoPuesto
     */
    public NominaRiesgoPuesto[] findNominaRiesgoPuestos(int idNominaRiesgoPuesto, int minLimit,int maxLimit, String filtroBusqueda) {
        NominaRiesgoPuesto[] nominaRiesgoPuestoDto = new NominaRiesgoPuesto[0];
        NominaRiesgoPuestoDaoImpl nominaRiesgoPuestoDao = new NominaRiesgoPuestoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idNominaRiesgoPuesto>0){
                sqlFiltro ="ID_RIESGO_PUESTO=" + idNominaRiesgoPuesto + " ";
            }else{
                sqlFiltro ="ID_RIESGO_PUESTO>0 ";
            }
            /*if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro +=" ID_EMPRESA>0";
            }*/
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            nominaRiesgoPuestoDto = nominaRiesgoPuestoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_RIESGO_PUESTO ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return nominaRiesgoPuestoDto;
    }
    
    /**
     * Realiza una búsqueda por ID NominaRiesgoPuesto en busca de
     * coincidencias
     * @param idNominaRiesgoPuesto ID Del Puesto para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar nominaRiesgoPuesto, -1 para evitar filtro     
     * @return String de cada una de las nominaRiesgoPuesto
     */
    
        public String getNominaRiesgoPuestosByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            NominaRiesgoPuesto[] nominaRiesgoPuestoDto = findNominaRiesgoPuestos(-1, 0, 0, "");
            
            for (NominaRiesgoPuesto nominaRiesgoPuesto:nominaRiesgoPuestoDto){
                try{                    
                    String selectedStr="";

                    if (idSeleccionado==nominaRiesgoPuesto.getIdRiesgoPuesto())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+nominaRiesgoPuesto.getIdRiesgoPuesto()+"' "
                            + selectedStr
                            + "title='"+nominaRiesgoPuesto.getNombre()+"'>"
                            + nominaRiesgoPuesto.getNombre()
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