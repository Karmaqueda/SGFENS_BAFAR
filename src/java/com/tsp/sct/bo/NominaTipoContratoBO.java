/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.NominaTipoContrato;
import com.tsp.sct.dao.exceptions.NominaTipoContratoDaoException;
import com.tsp.sct.dao.jdbc.NominaTipoContratoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class NominaTipoContratoBO {
    
    private NominaTipoContrato nominaTipoContrato = null;

    public NominaTipoContrato getNominaTipoContrato() {
        return nominaTipoContrato;
    }

    public void setNominaTipoContrato(NominaTipoContrato nominaTipoContrato) {
        this.nominaTipoContrato = nominaTipoContrato;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public NominaTipoContratoBO(Connection conn){
        this.conn = conn;
    }
       
    
    public NominaTipoContratoBO(int idNominaTipoContrato, Connection conn){  
        this.conn = conn;
        try{
            NominaTipoContratoDaoImpl NominaTipoContratoDaoImpl = new NominaTipoContratoDaoImpl(this.conn);
            this.nominaTipoContrato = NominaTipoContratoDaoImpl.findByPrimaryKey(idNominaTipoContrato);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public NominaTipoContrato findMarcabyId(int idNominaTipoContrato) throws Exception{
        NominaTipoContrato NominaTipoContrato = null;
        
        try{
            NominaTipoContratoDaoImpl NominaTipoContratoDaoImpl = new NominaTipoContratoDaoImpl(this.conn);
            NominaTipoContrato = NominaTipoContratoDaoImpl.findByPrimaryKey(idNominaTipoContrato);
            if (NominaTipoContrato==null){
                throw new Exception("No se encontro ningun Contrato que corresponda con los parámetros específicados.");
            }
            if (NominaTipoContrato.getIdTipoContrato()<=0){
                throw new Exception("No se encontro ningun Contrato que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Contrato del usuario. Error: " + e.getMessage());
        }
        
        return NominaTipoContrato;
    }
    
    public NominaTipoContrato getNominaTipoContratoGenericoByEmpresa(int idEmpresa) throws Exception{
        NominaTipoContrato nominaTipoContrato = null;
        
        try{
            NominaTipoContratoDaoImpl nominaTipoContratoDaoImpl = new NominaTipoContratoDaoImpl(this.conn);
            nominaTipoContrato = nominaTipoContratoDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (nominaTipoContrato==null){
                throw new Exception("La empresa no tiene creada algun Contrato");
            }
        }catch(NominaTipoContratoDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada algun Contrato");
        }
        
        return nominaTipoContrato;
    }
    
    /**
     * Realiza una búsqueda por ID NominaTipoContrato en busca de
     * coincidencias
     * @param idContrato ID Del Contrato para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public NominaTipoContrato[] findNominaTipoContratos(int idNominaTipoContrato, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        NominaTipoContrato[] nominaTipoContratoDto = new NominaTipoContrato[0];
        NominaTipoContratoDaoImpl nominaTipoContratoDao = new NominaTipoContratoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idNominaTipoContrato>0){
                sqlFiltro ="ID_TIPO_CONTRATO=" + idNominaTipoContrato + " AND ";
            }else{
                sqlFiltro ="ID_TIPO_CONTRATO>0 AND";
            }
            if (idEmpresa>0){                
                sqlFiltro += " ( (ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")) OR ID_EMPRESA = 0 )";
            }else{
                sqlFiltro +=" ID_EMPRESA>-1";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            nominaTipoContratoDto = nominaTipoContratoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return nominaTipoContratoDto;
    }
    
    /**
     * Realiza una búsqueda por ID NominaTipoContrato en busca de
     * coincidencias
     * @param idNominaTipoContrato ID Del Contrato para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar nominaTipoContratos, -1 para evitar filtro     
     * @return String de cada uno de los nominaTipoContratos
     */
    
        //public String getNominaTipoContratosByIdHTMLCombo(int idEmpresa, int idSeleccionado){
    public String getNominaTipoContratosByIdHTMLCombo(int idEmpresa, String idSeleccionado){
        String strHTMLCombo ="";

        try{
            NominaTipoContrato[] nominaTipoContratosDto = findNominaTipoContratos(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (NominaTipoContrato nominaTipoContrato:nominaTipoContratosDto){
                try{                    
                    String selectedStr="";

                    if (idSeleccionado.equals(nominaTipoContrato.getNombre()))
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+nominaTipoContrato.getNombre()+"' "
                            + selectedStr
                            + "title='"+nominaTipoContrato.getDescripcion()+"'>"
                            + nominaTipoContrato.getNombre()
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
