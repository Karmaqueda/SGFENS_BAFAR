/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.NominaPuesto;
import com.tsp.sct.dao.exceptions.NominaPuestoDaoException;
import com.tsp.sct.dao.jdbc.NominaPuestoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class NominaPuestoBO {
    
    private NominaPuesto nominaPuesto = null;

    public NominaPuesto getNominaPuesto() {
        return nominaPuesto;
    }

    public void setNominaPuesto(NominaPuesto nominaPuesto) {
        this.nominaPuesto = nominaPuesto;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public NominaPuestoBO(Connection conn){
        this.conn = conn;
    }
    
    public NominaPuestoBO(int idNominaPuesto, Connection conn){
        this.conn = conn;
        try{
            NominaPuestoDaoImpl NominaPuestoDaoImpl = new NominaPuestoDaoImpl(this.conn);
            this.nominaPuesto = NominaPuestoDaoImpl.findByPrimaryKey(idNominaPuesto);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public NominaPuesto findNominaPuestobyId(int idNominaPuesto) throws Exception{
        NominaPuesto NominaPuesto = null;
        
        try{
            NominaPuestoDaoImpl NominaPuestoDaoImpl = new NominaPuestoDaoImpl(this.conn);
            NominaPuesto = NominaPuestoDaoImpl.findByPrimaryKey(idNominaPuesto);
            if (NominaPuesto==null){
                throw new Exception("No se encontro ninguna Puesto que corresponda con los parámetros específicados.");
            }
            if (NominaPuesto.getIdPuesto()<=0){
                throw new Exception("No se encontro ninguna Puesto que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Puesto del usuario. Error: " + e.getMessage());
        }
        
        return NominaPuesto;
    }
    
    public NominaPuesto getNominaPuestoGenericoByEmpresa(int idEmpresa) throws Exception{
        NominaPuesto nominaPuesto = null;
        
        try{
            NominaPuestoDaoImpl nominaPuestoDaoImpl = new NominaPuestoDaoImpl(this.conn);
            nominaPuesto = nominaPuestoDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (nominaPuesto==null){
                throw new Exception("La empresa no tiene creada alguna NominaPuesto");
            }
        }catch(NominaPuestoDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada alguna NominaPuesto");
        }
        
        return nominaPuesto;
    }
    
    /**
     * Realiza una búsqueda por ID Puesto en busca de
     * coincidencias
     * @param idNominaPuesto ID Del puesto para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar nominaPuestos, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO NominaPuesto
     */
    public NominaPuesto[] findNominaPuestos(int idNominaPuesto, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        NominaPuesto[] nominaPuestoDto = new NominaPuesto[0];
        NominaPuestoDaoImpl nominaPuestoDao = new NominaPuestoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idNominaPuesto>0){
                sqlFiltro ="ID_PUESTO=" + idNominaPuesto + " AND ";
            }else{
                sqlFiltro ="ID_PUESTO>0 AND";
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
            
            nominaPuestoDto = nominaPuestoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return nominaPuestoDto;
    }
    
    /**
     * Realiza una búsqueda por ID NominaPuesto en busca de
     * coincidencias
     * @param idNominaPuesto ID Del Puesto para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar nominaPuestos, -1 para evitar filtro     
     * @return String de cada una de las nominaPuestos
     */
    
        public String getNominaPuestosByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            NominaPuesto[] nominaPuestosDto = findNominaPuestos(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (NominaPuesto nominaPuesto:nominaPuestosDto){
                try{                    
                    String selectedStr="";

                    if (idSeleccionado==nominaPuesto.getIdPuesto())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+nominaPuesto.getIdPuesto()+"' "
                            + selectedStr
                            + "title='"+nominaPuesto.getNombre()+"'>"
                            + nominaPuesto.getNombre()
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
