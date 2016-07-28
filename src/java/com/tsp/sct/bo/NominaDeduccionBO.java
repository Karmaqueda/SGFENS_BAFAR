/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.NominaDeduccion;
import com.tsp.sct.dao.jdbc.NominaDeduccionDaoImpl;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */
public class NominaDeduccionBO {
    
    private NominaDeduccion nominaDeduccion = null;

    public NominaDeduccion getNominaDeduccion() {
        return nominaDeduccion;
    }

    public void setNominaDeduccion(NominaDeduccion nominaDeduccion) {
        this.nominaDeduccion = nominaDeduccion;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public NominaDeduccionBO(Connection conn){
        this.conn = conn;
    }
    
    public NominaDeduccionBO(int idNominaDeduccion, Connection conn){
        this.conn = conn;
        try{
            NominaDeduccionDaoImpl NominaDeduccionDaoImpl = new NominaDeduccionDaoImpl(this.conn);
            this.nominaDeduccion = NominaDeduccionDaoImpl.findByPrimaryKey(idNominaDeduccion);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public NominaDeduccion findNominaDeduccionbyId(int idNominaDeduccion) throws Exception{
        NominaDeduccion NominaDeduccion = null;
        
        try{
            NominaDeduccionDaoImpl NominaDeduccionDaoImpl = new NominaDeduccionDaoImpl(this.conn);
            NominaDeduccion = NominaDeduccionDaoImpl.findByPrimaryKey(idNominaDeduccion);
            if (NominaDeduccion==null){
                throw new Exception("No se encontro ninguna deduccion que corresponda con los parámetros específicados.");
            }
            if (NominaDeduccion.getIdNominaDeduccion()<=0){
                throw new Exception("No se encontro ninguna deduccion que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la deduccion del usuario. Error: " + e.getMessage());
        }
        
        return NominaDeduccion;
    }
     
    /**
     * Realiza una búsqueda por ID deduccion en busca de
     * coincidencias
     * @param idNominaDeduccion ID Del deduccion para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar nominaDeduccions, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO NominaDeduccion
     */
    public NominaDeduccion[] findNominaDeduccions(int idNominaDeduccion, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        NominaDeduccion[] nominaDeduccionDto = new NominaDeduccion[0];
        NominaDeduccionDaoImpl nominaDeduccionDao = new NominaDeduccionDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idNominaDeduccion>0){
                sqlFiltro ="ID_NOMINA_DEDUCCION=" + idNominaDeduccion + " AND ";
            }else{
                sqlFiltro ="ID_NOMINA_DEDUCCION>0 AND";
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
            
            nominaDeduccionDto = nominaDeduccionDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY CLAVE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return nominaDeduccionDto;
    }
    
    /**
     * Realiza una búsqueda por ID NominaDeduccion en busca de
     * coincidencias
     * @param idNominaDeduccion ID Del Puesto para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar nominaDeduccions, -1 para evitar filtro     
     * @return String de cada una de las nominaDeduccions
     */
    
        public String getNominaDeduccionsByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            NominaDeduccion[] nominaDeduccionsDto = findNominaDeduccions(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (NominaDeduccion nominaDeduccion:nominaDeduccionsDto){
                try{                    
                    String selectedStr="";

                    if (idSeleccionado==nominaDeduccion.getIdNominaDeduccion())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+nominaDeduccion.getIdNominaDeduccion()+"' "
                            + selectedStr
                            + "title='"+nominaDeduccion.getConcepto()+"'>"
                            + nominaDeduccion.getClave()
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