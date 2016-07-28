/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.NominaTipoIncapacidad;
import com.tsp.sct.dao.jdbc.NominaTipoIncapacidadDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class NominaTipoIncapacidadBO {
    
    private NominaTipoIncapacidad nominaTipoIncapacidad = null;

    public NominaTipoIncapacidad getNominaTipoIncapacidad() {
        return nominaTipoIncapacidad;
    }

    public void setNominaTipoIncapacidad(NominaTipoIncapacidad nominaTipoIncapacidad) {
        this.nominaTipoIncapacidad = nominaTipoIncapacidad;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public NominaTipoIncapacidadBO(Connection conn){
        this.conn = conn;
    }
       
    
    public NominaTipoIncapacidadBO(int idNominaTipoIncapacidad, Connection conn){
        this.conn = conn;
        try{
            NominaTipoIncapacidadDaoImpl NominaTipoIncapacidadDaoImpl = new NominaTipoIncapacidadDaoImpl(this.conn);
            this.nominaTipoIncapacidad = NominaTipoIncapacidadDaoImpl.findByPrimaryKey(idNominaTipoIncapacidad);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public NominaTipoIncapacidad findNominaTipoIncapacidadbyId(int idNominaTipoIncapacidad) throws Exception{
        NominaTipoIncapacidad NominaTipoIncapacidad = null;
        
        try{
            NominaTipoIncapacidadDaoImpl NominaTipoIncapacidadDaoImpl = new NominaTipoIncapacidadDaoImpl(this.conn);
            NominaTipoIncapacidad = NominaTipoIncapacidadDaoImpl.findByPrimaryKey(idNominaTipoIncapacidad);
            if (NominaTipoIncapacidad==null){
                throw new Exception("No se encontro ningun Tipo Incapacidad que corresponda con los parámetros específicados.");
            }
            if (NominaTipoIncapacidad.getIdTipoIncapacidad()<=0){
                throw new Exception("No se encontro ningun Tipo Incapacidad que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Tipo Incapacidad del usuario. Error: " + e.getMessage());
        }
        
        return NominaTipoIncapacidad;
    }
    
    /**
     * Realiza una búsqueda por ID NominaTipoIncapacidad en busca de
     * coincidencias
     * @param idNominaTipoIncapacidad ID Del NominaTipoIncapacidad para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public NominaTipoIncapacidad[] findNominaTipoIncapacidads(int idNominaTipoIncapacidad, int minLimit,int maxLimit) {
        NominaTipoIncapacidad[] nominaTipoIncapacidadDto = new NominaTipoIncapacidad[0];
        NominaTipoIncapacidadDaoImpl nominaTipoIncapacidadDao = new NominaTipoIncapacidadDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idNominaTipoIncapacidad>0){
                sqlFiltro ="ID_TIPO_INCAPACIDAD=" + idNominaTipoIncapacidad + " ";
            }else{
                sqlFiltro ="ID_TIPO_INCAPACIDAD>0 ";
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
            
            nominaTipoIncapacidadDto = nominaTipoIncapacidadDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY DESCRIPCION ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return nominaTipoIncapacidadDto;
    }
    
    /**
     * Realiza una búsqueda por ID NominaTipoIncapacidad en busca de
     * coincidencias
     * @param idNominaTipoIncapacidad ID Del NominaTipoIncapacidad para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar nominaTipoIncapacidads, -1 para evitar filtro     
     * @return String de cada uno de los nominaTipoIncapacidads
     */
    
        public String getNominaTipoIncapacidadsByIdHTMLCombo(int idSeleccionado){
        String strHTMLCombo ="";

        try{
            NominaTipoIncapacidad[] nominaTipoIncapacidadsDto = findNominaTipoIncapacidads(-1, 0, 0);
            
            for (NominaTipoIncapacidad nominaTipoIncapacidad:nominaTipoIncapacidadsDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==nominaTipoIncapacidad.getClave())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+nominaTipoIncapacidad.getClave()+"' "
                            + selectedStr
                            + "title='"+nominaTipoIncapacidad.getDescripcion()+"'>"
                            + nominaTipoIncapacidad.getDescripcion()
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
