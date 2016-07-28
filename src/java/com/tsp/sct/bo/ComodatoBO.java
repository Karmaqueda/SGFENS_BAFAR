/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Comodato;
import com.tsp.sct.dao.exceptions.ComodatoDaoException;
import com.tsp.sct.dao.jdbc.ComodatoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class ComodatoBO {
    private Comodato comodato = null;

    public Comodato getComodato() {
        return comodato;
    }

    public void setComodato(Comodato comodato) {
        this.comodato = comodato;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public ComodatoBO(Connection conn){
        this.conn = conn;
    }
       
    
    public ComodatoBO(int idComodato, Connection conn){
        this.conn = conn;
        try{
            ComodatoDaoImpl ComodatoDaoImpl = new ComodatoDaoImpl(this.conn);
            this.comodato = ComodatoDaoImpl.findByPrimaryKey(idComodato);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Comodato findComodatobyId(int idComodato) throws Exception{
        Comodato Comodato = null;
        
        try{
            ComodatoDaoImpl ComodatoDaoImpl = new ComodatoDaoImpl(this.conn);
            Comodato = ComodatoDaoImpl.findByPrimaryKey(idComodato);
            if (Comodato==null){
                throw new Exception("No se encontro ningun Comodato que corresponda con los parámetros específicados.");
            }
            if (Comodato.getIdComodato()<=0){
                throw new Exception("No se encontro ningun Comodato que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Comodato del usuario. Error: " + e.getMessage());
        }
        
        return Comodato;
    }
   
    
    /**
     * Realiza una búsqueda por ID Comodato en busca de
     * coincidencias
     * @param idComodato ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public Comodato[] findComodatos(int idComodato, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        Comodato[] comodatoDto = new Comodato[0];
        ComodatoDaoImpl comodatoDao = new ComodatoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idComodato>0){
                sqlFiltro ="ID_COMODATO=" + idComodato + " AND ";
            }else{
                sqlFiltro ="ID_COMODATO>0 AND";
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
            
            comodatoDto = comodatoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return comodatoDto;
    }
    
    /**
     * Realiza una búsqueda por ID Comodato en busca de
     * coincidencias
     * @param idComodato ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar comodatos, -1 para evitar filtro     
     * @return String de cada uno de los comodatos
     */
    
        public String getComodatosByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            Comodato[] comodatosDto = findComodatos(-1, idEmpresa, 0, 0, " AND ESTATUS!=3 ");
            
            for (Comodato comodato:comodatosDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==comodato.getIdComodato())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+comodato.getIdComodato()+"' "
                            + selectedStr
                            + "title='"+comodato.getDescripcion()+"'>"
                            + comodato.getNombre()
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
    

