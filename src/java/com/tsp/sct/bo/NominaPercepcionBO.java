/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.NominaPercepcion;
import com.tsp.sct.dao.jdbc.NominaPercepcionDaoImpl;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */
public class NominaPercepcionBO {
    
    private NominaPercepcion nominaPercepcion = null;

    public NominaPercepcion getNominaPercepcion() {
        return nominaPercepcion;
    }

    public void setNominaPercepcion(NominaPercepcion nominaPercepcion) {
        this.nominaPercepcion = nominaPercepcion;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public NominaPercepcionBO(Connection conn){
        this.conn = conn;
    }
    
    public NominaPercepcionBO(int idNominaPercepcion, Connection conn){
        this.conn = conn;
        try{
            NominaPercepcionDaoImpl NominaPercepcionDaoImpl = new NominaPercepcionDaoImpl(this.conn);
            this.nominaPercepcion = NominaPercepcionDaoImpl.findByPrimaryKey(idNominaPercepcion);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public NominaPercepcion findNominaPercepcionbyId(int idNominaPercepcion) throws Exception{
        NominaPercepcion NominaPercepcion = null;
        
        try{
            NominaPercepcionDaoImpl NominaPercepcionDaoImpl = new NominaPercepcionDaoImpl(this.conn);
            NominaPercepcion = NominaPercepcionDaoImpl.findByPrimaryKey(idNominaPercepcion);
            if (NominaPercepcion==null){
                throw new Exception("No se encontro ninguna percepcion que corresponda con los parámetros específicados.");
            }
            if (NominaPercepcion.getIdNominaPercepcion()<=0){
                throw new Exception("No se encontro ninguna percepcion que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la percepcion del usuario. Error: " + e.getMessage());
        }
        
        return NominaPercepcion;
    }
     
    /**
     * Realiza una búsqueda por ID percepcion en busca de
     * coincidencias
     * @param idNominaPercepcion ID Del percepcion para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar nominaPercepcions, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO NominaPercepcion
     */
    public NominaPercepcion[] findNominaPercepcions(int idNominaPercepcion, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        NominaPercepcion[] nominaPercepcionDto = new NominaPercepcion[0];
        NominaPercepcionDaoImpl nominaPercepcionDao = new NominaPercepcionDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idNominaPercepcion>0){
                sqlFiltro ="ID_NOMINA_PERCEPCION=" + idNominaPercepcion + " AND ";
            }else{
                sqlFiltro ="ID_NOMINA_PERCEPCION>0 AND";
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
            
            nominaPercepcionDto = nominaPercepcionDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY CLAVE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return nominaPercepcionDto;
    }
    
    /**
     * Realiza una búsqueda por ID NominaPercepcion en busca de
     * coincidencias
     * @param idNominaPercepcion ID Del Puesto para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar nominaPercepcions, -1 para evitar filtro     
     * @return String de cada una de las nominaPercepcions
     */
    
        public String getNominaPercepcionsByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            NominaPercepcion[] nominaPercepcionsDto = findNominaPercepcions(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (NominaPercepcion nominaPercepcion:nominaPercepcionsDto){
                try{                    
                    String selectedStr="";

                    if (idSeleccionado==nominaPercepcion.getIdNominaPercepcion())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+nominaPercepcion.getIdNominaPercepcion()+"' "
                            + selectedStr
                            + "title='"+nominaPercepcion.getConcepto()+"'>"
                            + nominaPercepcion.getClave()
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