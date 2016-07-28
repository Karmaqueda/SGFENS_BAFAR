/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.CatalogoGastos;
import com.tsp.sct.dao.jdbc.CatalogoGastosDaoImpl;
import java.sql.Connection;

/**
 *
 * @author HpPyme
 */
public class CatalogoGastosBO {
    
    private CatalogoGastos catalogoGastos = null;
    private Connection conn = null;

    public CatalogoGastos getCatalogoGastos() {
        return catalogoGastos;
    }

    public void setCatalogoGastos(CatalogoGastos catalogoGastos) {
        this.catalogoGastos = catalogoGastos;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public CatalogoGastosBO(Connection conn) {
         this.conn = conn;
    }  
    
    public CatalogoGastosBO(int idCatalogoGasto ,  Connection conn) {        
        this.conn = conn;
        try{
            CatalogoGastosDaoImpl catalogoGastosDaoImpl = new CatalogoGastosDaoImpl(this.conn);
            this.catalogoGastos = catalogoGastosDaoImpl.findByPrimaryKey(idCatalogoGasto);
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
    
    
     /**
     * Realiza una búsqueda por ID Marca en busca de
     * coincidencias
     * @param idCatalogoGasto ID Del Motivo de Gasto para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public CatalogoGastos[] findGastoMotivo(int idCatalogoGasto, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        CatalogoGastos[] catGastoDto = new CatalogoGastos[0];
        CatalogoGastosDaoImpl catGastoDao = new CatalogoGastosDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCatalogoGasto>0){
                sqlFiltro =" ID_GASTOS=" + idCatalogoGasto + " AND ";
            }else{
                sqlFiltro =" ID_GASTOS>0 AND ";
            }
            if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ") OR ID_EMPRESA = 0 ";
            }else{
                sqlFiltro +=" ID_EMPRESA>0 OR ID_EMPRESA = 0  ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            catGastoDto = catGastoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_GASTOS ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return catGastoDto;
    }
    
    
    /**
     * Realiza una búsqueda por ID Catalogo Gasto en busca de
     * coincidencias
     * @param idSeleccionado ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro     
     * @return String de cada una de las marcas
     */
    
        public String getGastosMotivoByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            CatalogoGastos[] catGastoDto = findGastoMotivo(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (CatalogoGastos gastoCatalogo:catGastoDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==gastoCatalogo.getIdGastos())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+gastoCatalogo.getIdGastos()+"' "
                            + selectedStr
                            + "title='"+gastoCatalogo.getNombre()+"'>"
                            + gastoCatalogo.getNombre()
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
