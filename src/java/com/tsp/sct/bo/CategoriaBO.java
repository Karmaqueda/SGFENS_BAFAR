/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Categoria;
import com.tsp.sct.dao.exceptions.CategoriaDaoException;
import com.tsp.sct.dao.jdbc.CategoriaDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class CategoriaBO {
    
    private Categoria categoria = null;

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public CategoriaBO(Connection conn){
        this.conn = conn;
    }
    
    public CategoriaBO(int idCategoria, Connection conn){ 
        this.conn = conn;
        try{
            CategoriaDaoImpl CategoriaDaoImpl = new CategoriaDaoImpl(this.conn);
            this.categoria = CategoriaDaoImpl.findByPrimaryKey(idCategoria);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Categoria findMarcabyId(int idCategoria) throws Exception{
        Categoria Categoria = null;
        
        try{
            CategoriaDaoImpl CategoriaDaoImpl = new CategoriaDaoImpl(this.conn);
            Categoria = CategoriaDaoImpl.findByPrimaryKey(idCategoria);
            if (Categoria==null){
                throw new Exception("No se encontro ningun Categoria que corresponda con los parámetros específicados.");
            }
            if (Categoria.getIdCategoria()<=0){
                throw new Exception("No se encontro ningun Categoria que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Embalaje del usuario. Error: " + e.getMessage());
        }
        
        return Categoria;
    }
    
    /**
     * Realiza una búsqueda por ID Categoria en busca de
     * coincidencias
     * @param idCategoria ID de la Categoria para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public Categoria[] findCategorias(int idCategoria, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        Categoria[] categoriaDto = new Categoria[0];
        CategoriaDaoImpl categoriaDao = new CategoriaDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idCategoria>0){
                sqlFiltro ="id_categoria=" + idCategoria + " AND ";
            }else{
                sqlFiltro ="id_categoria>0 AND";
            }
            if (idEmpresa>0){                
                sqlFiltro += " ( ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ") OR ID_EMPRESA = 0 )";
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
            
            categoriaDto = categoriaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY nombre_categoria ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return categoriaDto;
    }
    
    public String getCategoriasByIdHTMLCombo(int idEmpresa, int idSeleccionado, String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            Categoria[] categoriasDto = findCategorias(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 " + filtroBusqueda);
            
            for (Categoria categoria:categoriasDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==categoria.getIdCategoria()){
                        selectedStr = " selected ";
                    }/*else if(categoria.getNombreCategoria().equals("GENERAL")){
                        selectedStr = " selected ";    
                    }*/
                        

                    strHTMLCombo += "<option value='"+categoria.getIdCategoria()+"' "
                            + selectedStr
                            + "title='"+categoria.getDescripcionCategoria()+"'>"
                            + categoria.getNombreCategoria()
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
    
    
    
    /**
     * Realiza una búsqueda por Categoria en busca de
     * coincidencias   
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro    
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado     
     * @return  String elemento de lista
     */
    public Categoria[] getCategoriasByLevelList(int idEmpresa, String filtroBusqueda){
       Categoria[] categoriasDto = new Categoria[0];

        try{            
            categoriasDto = findCategorias(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 " + filtroBusqueda);                         
        }catch(Exception e){
            e.printStackTrace();
        }

        return categoriasDto;
    }
    
    
}
