/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.NominaDepartamento;
import com.tsp.sct.dao.exceptions.NominaDepartamentoDaoException;
import com.tsp.sct.dao.jdbc.NominaDepartamentoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class NominaDepartamentoBO {
    
    private NominaDepartamento nominaDepartamento = null;

    public NominaDepartamento getNominaDepartamento() {
        return nominaDepartamento;
    }

    public void setNominaDepartamento(NominaDepartamento nominaDepartamento) {
        this.nominaDepartamento = nominaDepartamento;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public NominaDepartamentoBO(Connection conn){
        this.conn = conn;
    }
       
    
    public NominaDepartamentoBO(int idNominaDepartamento, Connection conn){
        this.conn = conn;
        try{
            NominaDepartamentoDaoImpl nominaDepartamentoDaoImpl = new NominaDepartamentoDaoImpl(this.conn);
            this.nominaDepartamento = nominaDepartamentoDaoImpl.findByPrimaryKey(idNominaDepartamento);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public NominaDepartamento findDepartamentoId(int idNominaDepartamento) throws Exception{
        NominaDepartamento NominaDepartamento = null;
        
        try{
            NominaDepartamentoDaoImpl nominaDepartamentoDaoImpl = new NominaDepartamentoDaoImpl(this.conn);
            NominaDepartamento = nominaDepartamentoDaoImpl.findByPrimaryKey(idNominaDepartamento);
            if (NominaDepartamento==null){
                throw new Exception("No se encontro ningun Departamento que corresponda con los parámetros específicados.");
            }
            if (NominaDepartamento.getIdDepartamento()<=0){
                throw new Exception("No se encontro ningun Departamento que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Departamento del usuario. Error: " + e.getMessage());
        }
        
        return NominaDepartamento;
    }
    
    public NominaDepartamento getNominaDepartamentoGenericoByEmpresa(int idEmpresa) throws Exception{
        NominaDepartamento nominaDepartamento = null;
        
        try{
            NominaDepartamentoDaoImpl nominaDepartamentoDaoImpl = new NominaDepartamentoDaoImpl(this.conn);
            nominaDepartamento = nominaDepartamentoDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (nominaDepartamento==null){
                throw new Exception("La empresa no tiene creada algun NominaDepartamento");
            }
        }catch(NominaDepartamentoDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada algun NominaDepartamento");
        }
        
        return nominaDepartamento;
    }
    
    /**
     * Realiza una búsqueda por ID NominaDepartamento en busca de
     * coincidencias
     * @param idDepartamento ID Del Departament para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public NominaDepartamento[] findNominaDepartamentos(int idNominaDepartamento, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        NominaDepartamento[] nominaDepartamentoDto = new NominaDepartamento[0];
        NominaDepartamentoDaoImpl nominaDepartamentoDao = new NominaDepartamentoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idNominaDepartamento>0){
                sqlFiltro ="ID_DEPARTAMENTO=" + idNominaDepartamento + " AND ";
            }else{
                sqlFiltro ="ID_DEPARTAMENTO>0 AND";
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
            
            nominaDepartamentoDto = nominaDepartamentoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return nominaDepartamentoDto;
    }
    
    /**
     * Realiza una búsqueda por ID NominaDepartamento en busca de
     * coincidencias
     * @param idNominaDepartamento ID Del Departamento para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar nominaDepartamentos, -1 para evitar filtro     
     * @return String de cada uno de los nominaDepartamentos
     */
    
        public String getNominaDepartamentosByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            NominaDepartamento[] nominaDepartamentosDto = findNominaDepartamentos(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (NominaDepartamento nominaDepartamento:nominaDepartamentosDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==nominaDepartamento.getIdDepartamento())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+nominaDepartamento.getIdDepartamento()+"' "
                            + selectedStr
                            + "title='"+nominaDepartamento.getDescripcion()+"'>"
                            + nominaDepartamento.getNombre()
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
