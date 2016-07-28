/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.NominaTipoDeduccion;
import com.tsp.sct.dao.exceptions.NominaTipoDeduccionDaoException;
import com.tsp.sct.dao.jdbc.NominaTipoDeduccionDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class NominaTipoDeduccionBO {
    
    private NominaTipoDeduccion nominaTipoDeduccion = null;

    public NominaTipoDeduccion getNominaTipoDeduccion() {
        return nominaTipoDeduccion;
    }

    public void setNominaTipoDeduccion(NominaTipoDeduccion nominaTipoDeduccion) {
        this.nominaTipoDeduccion = nominaTipoDeduccion;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public NominaTipoDeduccionBO(Connection conn){
        this.conn = conn;
    }
       
    
    public NominaTipoDeduccionBO(int idNominaTipoDeduccion, Connection conn){
        this.conn = conn;
        try{
            NominaTipoDeduccionDaoImpl NominaTipoDeduccionDaoImpl = new NominaTipoDeduccionDaoImpl(this.conn);
            this.nominaTipoDeduccion = NominaTipoDeduccionDaoImpl.findByPrimaryKey(idNominaTipoDeduccion);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public NominaTipoDeduccion findNominaTipoDeduccionbyId(int idNominaTipoDeduccion) throws Exception{
        NominaTipoDeduccion NominaTipoDeduccion = null;
        
        try{
            NominaTipoDeduccionDaoImpl NominaTipoDeduccionDaoImpl = new NominaTipoDeduccionDaoImpl(this.conn);
            NominaTipoDeduccion = NominaTipoDeduccionDaoImpl.findByPrimaryKey(idNominaTipoDeduccion);
            if (NominaTipoDeduccion==null){
                throw new Exception("No se encontro ningun Tipo Deduccion que corresponda con los parámetros específicados.");
            }
            if (NominaTipoDeduccion.getIdTipoDeduccion()<=0){
                throw new Exception("No se encontro ningun Tipo Deduccion que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Tipo Deduccion del usuario. Error: " + e.getMessage());
        }
        
        return NominaTipoDeduccion;
    }
    
    /**
     * Realiza una búsqueda por ID NominaTipoDeduccion en busca de
     * coincidencias
     * @param idNominaTipoDeduccion ID Del NominaTipoDeduccion para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public NominaTipoDeduccion[] findNominaTipoDeduccions(int idNominaTipoDeduccion, int minLimit,int maxLimit) {
        NominaTipoDeduccion[] nominaTipoDeduccionDto = new NominaTipoDeduccion[0];
        NominaTipoDeduccionDaoImpl nominaTipoDeduccionDao = new NominaTipoDeduccionDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idNominaTipoDeduccion>0){
                sqlFiltro ="ID_TIPO_DEDUCCION=" + idNominaTipoDeduccion + " ";
            }else{
                sqlFiltro ="ID_TIPO_DEDUCCION>0 ";
            }
            /*if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro +=" ID_EMPRESA>0";
            }*/
            
            /*if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }*/
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            nominaTipoDeduccionDto = nominaTipoDeduccionDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY DESCRIPCION ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return nominaTipoDeduccionDto;
    }
    
    /**
     * Realiza una búsqueda por ID NominaTipoDeduccion en busca de
     * coincidencias
     * @param idNominaTipoDeduccion ID Del NominaTipoDeduccion para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar nominaTipoDeduccions, -1 para evitar filtro     
     * @return String de cada uno de los nominaTipoDeduccions
     */
    
        public String getNominaTipoDeduccionsByIdHTMLCombo(int idSeleccionado){
        String strHTMLCombo ="";

        try{
            NominaTipoDeduccion[] nominaTipoDeduccionsDto = findNominaTipoDeduccions(-1, 0, 0);
            
            for (NominaTipoDeduccion nominaTipoDeduccion:nominaTipoDeduccionsDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==nominaTipoDeduccion.getIdTipoDeduccion())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+nominaTipoDeduccion.getClave()+"' "
                            + selectedStr
                            + "title='"+nominaTipoDeduccion.getDescripcion()+"'>"
                            + nominaTipoDeduccion.getDescripcion()
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
