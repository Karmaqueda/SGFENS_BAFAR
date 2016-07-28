/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.ComodatoMantenimiento;
import com.tsp.sct.dao.exceptions.ComodatoMantenimientoDaoException;
import com.tsp.sct.dao.jdbc.ComodatoMantenimientoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class ComodatoMantenimientoBO {
    private ComodatoMantenimiento comodatoMantenimiento = null;

    public ComodatoMantenimiento getComodatoMantenimiento() {
        return comodatoMantenimiento;
    }

    public void setComodatoMantenimiento(ComodatoMantenimiento comodatoMantenimiento) {
        this.comodatoMantenimiento = comodatoMantenimiento;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public ComodatoMantenimientoBO(Connection conn){
        this.conn = conn;
    }
       
    
    public ComodatoMantenimientoBO(int idComodatoMantenimiento, Connection conn){
        this.conn = conn;
        try{
            ComodatoMantenimientoDaoImpl ComodatoMantenimientoDaoImpl = new ComodatoMantenimientoDaoImpl(this.conn);
            this.comodatoMantenimiento = ComodatoMantenimientoDaoImpl.findByPrimaryKey(idComodatoMantenimiento);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ComodatoMantenimiento findComodatoMantenimientobyId(int idComodatoMantenimiento) throws Exception{
        ComodatoMantenimiento ComodatoMantenimiento = null;
        
        try{
            ComodatoMantenimientoDaoImpl ComodatoMantenimientoDaoImpl = new ComodatoMantenimientoDaoImpl(this.conn);
            ComodatoMantenimiento = ComodatoMantenimientoDaoImpl.findByPrimaryKey(idComodatoMantenimiento);
            if (ComodatoMantenimiento==null){
                throw new Exception("No se encontro ningun ComodatoMantenimiento que corresponda con los parámetros específicados.");
            }
            if (ComodatoMantenimiento.getIdComodatoMantenimiento()<=0){
                throw new Exception("No se encontro ningun ComodatoMantenimiento que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del ComodatoMantenimiento del usuario. Error: " + e.getMessage());
        }
        
        return ComodatoMantenimiento;
    }
   
    
    /**
     * Realiza una búsqueda por ID ComodatoMantenimiento en busca de
     * coincidencias
     * @param idComodatoMantenimiento ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public ComodatoMantenimiento[] findComodatoMantenimientos(int idComodatoMantenimiento, int idComodato, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        ComodatoMantenimiento[] comodatoMantenimientoDto = new ComodatoMantenimiento[0];
        ComodatoMantenimientoDaoImpl comodatoMantenimientoDao = new ComodatoMantenimientoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idComodatoMantenimiento>0){
                sqlFiltro ="ID_COMODATO_MANTENIMIENTO=" + idComodatoMantenimiento + " AND ";
            }else{
                sqlFiltro ="ID_COMODATO_MANTENIMIENTO>0 AND ";
            }
            
            if (idComodato>0){
                sqlFiltro ="ID_COMODATO=" + idComodato + " AND ";
            }else{
                sqlFiltro ="ID_COMODATO>0 AND ";
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
            
            comodatoMantenimientoDto = comodatoMantenimientoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_COMODATO_MANTENIMIENTO DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return comodatoMantenimientoDto;
    }
    
    /**
     * Realiza una búsqueda por ID ComodatoMantenimiento en busca de
     * coincidencias
     * @param idComodatoMantenimiento ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar comodatoMantenimientos, -1 para evitar filtro     
     * @return String de cada uno de los comodatoMantenimientos
     */
    
        public String getComodatoMantenimientosByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            ComodatoMantenimiento[] comodatoMantenimientosDto = findComodatoMantenimientos(-1, -1, idEmpresa, 0, 0, "");
            
            for (ComodatoMantenimiento comodatoMantenimiento:comodatoMantenimientosDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==comodatoMantenimiento.getIdComodatoMantenimiento())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+comodatoMantenimiento.getIdComodatoMantenimiento()+"' "
                            + selectedStr
                            + "title='"+comodatoMantenimiento.getDescripcion()+"'>"
                            + comodatoMantenimiento.getIdComodato()
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
    

