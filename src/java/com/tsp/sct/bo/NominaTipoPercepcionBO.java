/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.NominaTipoPercepcion;
import com.tsp.sct.dao.jdbc.NominaTipoPercepcionDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class NominaTipoPercepcionBO {
    
    private NominaTipoPercepcion nominaTipoPercepcion = null;

    public NominaTipoPercepcion getNominaTipoPercepcion() {
        return nominaTipoPercepcion;
    }

    public void setNominaTipoPercepcion(NominaTipoPercepcion nominaTipoPercepcion) {
        this.nominaTipoPercepcion = nominaTipoPercepcion;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public NominaTipoPercepcionBO(Connection conn){
        this.conn = conn;
    }
       
    
    public NominaTipoPercepcionBO(int idNominaTipoPercepcion, Connection conn){
        this.conn = conn;
        try{
            NominaTipoPercepcionDaoImpl NominaTipoPercepcionDaoImpl = new NominaTipoPercepcionDaoImpl(this.conn);
            this.nominaTipoPercepcion = NominaTipoPercepcionDaoImpl.findByPrimaryKey(idNominaTipoPercepcion);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public NominaTipoPercepcion findNominaTipoPercepcionbyId(int idNominaTipoPercepcion) throws Exception{
        NominaTipoPercepcion NominaTipoPercepcion = null;
        
        try{
            NominaTipoPercepcionDaoImpl NominaTipoPercepcionDaoImpl = new NominaTipoPercepcionDaoImpl(this.conn);
            NominaTipoPercepcion = NominaTipoPercepcionDaoImpl.findByPrimaryKey(idNominaTipoPercepcion);
            if (NominaTipoPercepcion==null){
                throw new Exception("No se encontro ningun Tipo Percepcion que corresponda con los parámetros específicados.");
            }
            if (NominaTipoPercepcion.getIdTipoPrecepcion()<=0){
                throw new Exception("No se encontro ningun Tipo Percepcion que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Tipo Percepcion del usuario. Error: " + e.getMessage());
        }
        
        return NominaTipoPercepcion;
    }
    
    /**
     * Realiza una búsqueda por ID NominaTipoPercepcion en busca de
     * coincidencias
     * @param idNominaTipoPercepcion ID Del NominaTipoPercepcion para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public NominaTipoPercepcion[] findNominaTipoPercepcions(int idNominaTipoPercepcion, int minLimit,int maxLimit) {
        NominaTipoPercepcion[] nominaTipoPercepcionDto = new NominaTipoPercepcion[0];
        NominaTipoPercepcionDaoImpl nominaTipoPercepcionDao = new NominaTipoPercepcionDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idNominaTipoPercepcion>0){
                sqlFiltro ="ID_TIPO_PRECEPCION=" + idNominaTipoPercepcion + " ";
            }else{
                sqlFiltro ="ID_TIPO_PRECEPCION>0 ";
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
            
            nominaTipoPercepcionDto = nominaTipoPercepcionDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY DESCRIPCION ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return nominaTipoPercepcionDto;
    }
    
    /**
     * Realiza una búsqueda por ID NominaTipoPercepcion en busca de
     * coincidencias
     * @param idNominaTipoPercepcion ID Del NominaTipoPercepcion para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar nominaTipoPercepcions, -1 para evitar filtro     
     * @return String de cada uno de los nominaTipoPercepcions
     */
    
        public String getNominaTipoPercepcionsByIdHTMLCombo(int idSeleccionado){
        String strHTMLCombo ="";

        try{
            NominaTipoPercepcion[] nominaTipoPercepcionsDto = findNominaTipoPercepcions(-1, 0, 0);
            
            for (NominaTipoPercepcion nominaTipoPercepcion:nominaTipoPercepcionsDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==nominaTipoPercepcion.getIdTipoPrecepcion())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+nominaTipoPercepcion.getClave()+"' "
                            + selectedStr
                            + "title='"+nominaTipoPercepcion.getDescripcion()+"'>"
                            + nominaTipoPercepcion.getDescripcion()
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
