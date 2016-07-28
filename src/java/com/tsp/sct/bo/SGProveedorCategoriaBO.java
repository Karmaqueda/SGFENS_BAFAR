/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SgfensProveedorCategoria;
import com.tsp.sct.dao.exceptions.SgfensProveedorCategoriaDaoException;
import com.tsp.sct.dao.jdbc.SgfensProveedorCategoriaDaoImpl;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */
public class SGProveedorCategoriaBO {
    
    private SgfensProveedorCategoria sgfensProveedorCategoria = null;

    public SgfensProveedorCategoria getSgfensProveedorCategoria() {
        return sgfensProveedorCategoria;
    }

    public void setSgfensProveedorCategoria(SgfensProveedorCategoria sgfensProveedorCategoria) {
        this.sgfensProveedorCategoria = sgfensProveedorCategoria;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SGProveedorCategoriaBO(Connection conn){
        this.conn = conn;
    }
    
    public SGProveedorCategoriaBO(int idSgfensProveedorCategoria, Connection conn){        
        this.conn = conn;
        try{
            SgfensProveedorCategoriaDaoImpl SgfensProveedorCategoriaDaoImpl = new SgfensProveedorCategoriaDaoImpl(this.conn);
            this.sgfensProveedorCategoria = SgfensProveedorCategoriaDaoImpl.findByPrimaryKey(idSgfensProveedorCategoria);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public SgfensProveedorCategoria findSgfensProveedorCategoriabyId(int idSgfensProveedorCategoria) throws Exception{
        SgfensProveedorCategoria SgfensProveedorCategoria = null;
        
        try{
            SgfensProveedorCategoriaDaoImpl SgfensProveedorCategoriaDaoImpl = new SgfensProveedorCategoriaDaoImpl(this.conn);
            SgfensProveedorCategoria = SgfensProveedorCategoriaDaoImpl.findByPrimaryKey(idSgfensProveedorCategoria);
            if (SgfensProveedorCategoria==null){
                throw new Exception("No se encontro ninguna SgfensProveedorCategoria que corresponda con los parámetros específicados.");
            }
            if (SgfensProveedorCategoria.getIdCategoria()<=0){
                throw new Exception("No se encontro ninguna SgfensProveedorCategoria que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la SgfensProveedorCategoria del usuario. Error: " + e.getMessage());
        }
        
        return SgfensProveedorCategoria;
    }
    
    public SgfensProveedorCategoria getSgfensProveedorCategoriaGenericoByEmpresa(int idEmpresa) throws Exception{
        SgfensProveedorCategoria sgfensProveedorCategoria = null;
        
        try{
            SgfensProveedorCategoriaDaoImpl sgfensProveedorCategoriaDaoImpl = new SgfensProveedorCategoriaDaoImpl(this.conn);
            sgfensProveedorCategoria = sgfensProveedorCategoriaDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (sgfensProveedorCategoria==null){
                throw new Exception("La empresa no tiene creada alguna SgfensProveedorCategoria");
            }
        }catch(SgfensProveedorCategoriaDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada alguna SgfensProveedorCategoria");
        }
        
        return sgfensProveedorCategoria;
    }
    
    /**
     * Realiza una búsqueda por ID SgfensProveedorCategoria en busca de
     * coincidencias
     * @param idSgfensProveedorCategoria ID De Categoria para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar sgfensProveedorCategorias, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensProveedorCategoria
     */
    public SgfensProveedorCategoria[] findSgfensProveedorCategorias(int idSgfensProveedorCategoria, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SgfensProveedorCategoria[] sgfensProveedorCategoriaDto = new SgfensProveedorCategoria[0];
        SgfensProveedorCategoriaDaoImpl sgfensProveedorCategoriaDao = new SgfensProveedorCategoriaDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSgfensProveedorCategoria>0){
                sqlFiltro ="ID_CATEGORIA=" + idSgfensProveedorCategoria + " AND ";
            }else{
                sqlFiltro ="ID_CATEGORIA>0 AND";
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
            
            sgfensProveedorCategoriaDto = sgfensProveedorCategoriaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return sgfensProveedorCategoriaDto;
    }
    
    /**
     * Realiza una búsqueda por ID SgfensProveedorCategoria en busca de
     * coincidencias
     * @param idSgfensProveedorCategoria ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar sgfensProveedorCategorias, -1 para evitar filtro     
     * @return String de cada una de las sgfensProveedorCategorias
     */
    
        public String getSgfensProveedorCategoriasByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            SgfensProveedorCategoria[] sgfensProveedorCategoriasDto = findSgfensProveedorCategorias(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (SgfensProveedorCategoria sgfensProveedorCategoria:sgfensProveedorCategoriasDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==sgfensProveedorCategoria.getIdCategoria())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+sgfensProveedorCategoria.getIdCategoria()+"' "
                            + selectedStr
                            + "title='"+sgfensProveedorCategoria.getNombre()+"'>"
                            + sgfensProveedorCategoria.getNombre()
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
