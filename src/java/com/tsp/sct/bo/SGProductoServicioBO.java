/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SgfensProductoServicio;
import com.tsp.sct.dao.exceptions.SgfensProductoServicioDaoException;
import com.tsp.sct.dao.jdbc.SgfensProductoServicioDaoImpl;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */
public class SGProductoServicioBO {
    
    private SgfensProductoServicio sgfensProductoServicio = null;

    public SgfensProductoServicio getSgfensProductoServicio() {
        return sgfensProductoServicio;
    }

    public void setSgfensProductoServicio(SgfensProductoServicio sgfensProductoServicio) {
        this.sgfensProductoServicio = sgfensProductoServicio;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SGProductoServicioBO(Connection conn){
        this.conn = conn;
    }
    
    public SGProductoServicioBO(int idSgfensProductoServicio, Connection conn){        
        this.conn = conn;
        try{
            SgfensProductoServicioDaoImpl SgfensProductoServicioDaoImpl = new SgfensProductoServicioDaoImpl(this.conn);
            this.sgfensProductoServicio = SgfensProductoServicioDaoImpl.findByPrimaryKey(idSgfensProductoServicio);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public SgfensProductoServicio findSgfensProductoServiciobyId(int idSgfensProductoServicio) throws Exception{
        SgfensProductoServicio SgfensProductoServicio = null;
        
        try{
            SgfensProductoServicioDaoImpl SgfensProductoServicioDaoImpl = new SgfensProductoServicioDaoImpl(this.conn);
            SgfensProductoServicio = SgfensProductoServicioDaoImpl.findByPrimaryKey(idSgfensProductoServicio);
            if (SgfensProductoServicio==null){
                throw new Exception("No se encontro ninguna SgfensProductoServicio que corresponda con los parámetros específicados.");
            }
            if (SgfensProductoServicio.getIdProductoServicio()<=0){
                throw new Exception("No se encontro ninguna SgfensProductoServicio que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la SgfensProductoServicio del usuario. Error: " + e.getMessage());
        }
        
        return SgfensProductoServicio;
    }
    
    public SgfensProductoServicio getSgfensProductoServicioGenericoByEmpresa(int idEmpresa) throws Exception{
        SgfensProductoServicio sgfensProductoServicio = null;
        
        try{
            SgfensProductoServicioDaoImpl sgfensProductoServicioDaoImpl = new SgfensProductoServicioDaoImpl(this.conn);
            sgfensProductoServicio = sgfensProductoServicioDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (sgfensProductoServicio==null){
                throw new Exception("La empresa no tiene creada alguna SgfensProductoServicio");
            }
        }catch(SgfensProductoServicioDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada alguna SgfensProductoServicio");
        }
        
        return sgfensProductoServicio;
    }
    
    /**
     * Realiza una búsqueda por ID SgfensProductoServicio en busca de
     * coincidencias
     * @param idSgfensProductoServicio ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar sgfensProductoServicios, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensProductoServicio
     */
    public SgfensProductoServicio[] findSgfensProductoServicios(int idSgfensProductoServicio, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SgfensProductoServicio[] sgfensProductoServicioDto = new SgfensProductoServicio[0];
        SgfensProductoServicioDaoImpl sgfensProductoServicioDao = new SgfensProductoServicioDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSgfensProductoServicio>0){
                sqlFiltro ="ID_PRODUCTO_SERVICIO=" + idSgfensProductoServicio + " AND ";
            }else{
                sqlFiltro ="ID_PRODUCTO_SERVICIO>0 AND";
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
            
            sgfensProductoServicioDto = sgfensProductoServicioDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return sgfensProductoServicioDto;
    }
    
    /**
     * Realiza una búsqueda por ID SgfensProductoServicio en busca de
     * coincidencias
     * @param idSgfensProductoServicio ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar sgfensProductoServicios, -1 para evitar filtro     
     * @return String de cada una de las sgfensProductoServicios
     */
    
        public String getSgfensProductoServiciosByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            SgfensProductoServicio[] sgfensProductoServiciosDto = findSgfensProductoServicios(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (SgfensProductoServicio sgfensProductoServicio:sgfensProductoServiciosDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==sgfensProductoServicio.getIdProductoServicio())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+sgfensProductoServicio.getIdProductoServicio()+"' "
                            + selectedStr
                            + "title='"+sgfensProductoServicio.getNombre()+"'>"
                            + sgfensProductoServicio.getNombre()
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
