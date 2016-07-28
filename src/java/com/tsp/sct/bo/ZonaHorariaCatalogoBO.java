/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.ZonaHorariaCatalogo;
import com.tsp.sct.dao.exceptions.ZonaHorariaCatalogoDaoException;
import com.tsp.sct.dao.jdbc.ZonaHorariaCatalogoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */    

public class ZonaHorariaCatalogoBO {
 private ZonaHorariaCatalogo zonaHorariaCatalogo = null;

    public ZonaHorariaCatalogo getZonaHorariaCatalogo() {
        return zonaHorariaCatalogo;
    }

    public void setZonaHorariaCatalogo(ZonaHorariaCatalogo zonaHorariaCatalogo) {
        this.zonaHorariaCatalogo = zonaHorariaCatalogo;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public ZonaHorariaCatalogoBO(Connection conn){
        this.conn = conn;
    }
    
    public ZonaHorariaCatalogoBO(int idZonaHorariaCatalogo, Connection conn){        
        this.conn = conn;
        try{
            ZonaHorariaCatalogoDaoImpl ZonaHorariaCatalogoDaoImpl = new ZonaHorariaCatalogoDaoImpl(this.conn);
            this.zonaHorariaCatalogo = ZonaHorariaCatalogoDaoImpl.findByPrimaryKey(idZonaHorariaCatalogo);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ZonaHorariaCatalogo findZonaHorariaCatalogobyId(int idZonaHorariaCatalogo) throws Exception{
        ZonaHorariaCatalogo ZonaHorariaCatalogo = null;
        
        try{
            ZonaHorariaCatalogoDaoImpl ZonaHorariaCatalogoDaoImpl = new ZonaHorariaCatalogoDaoImpl(this.conn);
            ZonaHorariaCatalogo = ZonaHorariaCatalogoDaoImpl.findByPrimaryKey(idZonaHorariaCatalogo);
            if (ZonaHorariaCatalogo==null){
                throw new Exception("No se encontro ninguna ZonaHorariaCatalogo que corresponda con los parámetros específicados.");
            }
            if (ZonaHorariaCatalogo.getIdZonaHorariaCatalogo()<=0){
                throw new Exception("No se encontro ninguna ZonaHorariaCatalogo que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la ZonaHorariaCatalogo del usuario. Error: " + e.getMessage());
        }
        
        return ZonaHorariaCatalogo;
    }
        
    /**
     * Realiza una búsqueda por ID ZonaHorariaCatalogo en busca de
     * coincidencias
     * @param idZonaHorariaCatalogo ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar zonaHorariaCatalogos, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO ZonaHorariaCatalogo
     */
    public ZonaHorariaCatalogo[] findZonaHorariaCatalogos(int idZonaHorariaCatalogo, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        ZonaHorariaCatalogo[] zonaHorariaCatalogoDto = new ZonaHorariaCatalogo[0];
        ZonaHorariaCatalogoDaoImpl zonaHorariaCatalogoDao = new ZonaHorariaCatalogoDaoImpl(this.conn);
        try {
            String sqlFiltro=" ID_ESTATUS = 1 AND ";
            if (idZonaHorariaCatalogo>0){
                sqlFiltro ="ID_ZONA_HORARIA_CATALOGO=" + idZonaHorariaCatalogo + " ";
            }else{
                sqlFiltro ="ID_ZONA_HORARIA_CATALOGO>0 ";
            } 
            /*if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro +=" ID_EMPRESA>0";
            }*/
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            zonaHorariaCatalogoDto = zonaHorariaCatalogoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ZONA_HORARIA ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return zonaHorariaCatalogoDto;
    }
    
    /**
     * Realiza una búsqueda por ID ZonaHorariaCatalogo en busca de
     * coincidencias
     * @param idZonaHorariaCatalogo ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar zonaHorariaCatalogos, -1 para evitar filtro     
     * @return String de cada una de las zonaHorariaCatalogos
     */
    
        public String getZonaHorariaCatalogosByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            ZonaHorariaCatalogo[] zonaHorariaCatalogosDto = findZonaHorariaCatalogos(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (ZonaHorariaCatalogo zonaHorariaCatalogo:zonaHorariaCatalogosDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==zonaHorariaCatalogo.getIdZonaHorariaCatalogo())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+zonaHorariaCatalogo.getIdZonaHorariaCatalogo()+"' "
                            + selectedStr
                            + "title='"+zonaHorariaCatalogo.getZonaHoraria()+"'>"
                            + zonaHorariaCatalogo.getZonaHoraria()
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
