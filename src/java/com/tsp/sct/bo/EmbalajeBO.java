/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Embalaje;
import com.tsp.sct.dao.exceptions.EmbalajeDaoException;
import com.tsp.sct.dao.jdbc.EmbalajeDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class EmbalajeBO {
    private Embalaje embalaje = null;

    public Embalaje getEmbalaje() {
        return embalaje;
    }

    public void setEmbalaje(Embalaje embalaje) {
        this.embalaje = embalaje;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public EmbalajeBO(Connection conn){
        this.conn = conn;
    }
       
    
    public EmbalajeBO(int idEmbalaje, Connection conn){
        this.conn = conn;
        try{
            EmbalajeDaoImpl EmbalajeDaoImpl = new EmbalajeDaoImpl(this.conn);
            this.embalaje = EmbalajeDaoImpl.findByPrimaryKey(idEmbalaje);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Embalaje findMarcabyId(int idEmbalaje) throws Exception{
        Embalaje Embalaje = null;
        
        try{
            EmbalajeDaoImpl EmbalajeDaoImpl = new EmbalajeDaoImpl(this.conn);
            Embalaje = EmbalajeDaoImpl.findByPrimaryKey(idEmbalaje);
            if (Embalaje==null){
                throw new Exception("No se encontro ningun Embalaje que corresponda con los parámetros específicados.");
            }
            if (Embalaje.getIdEmbalaje()<=0){
                throw new Exception("No se encontro ningun Embalaje que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Embalaje del usuario. Error: " + e.getMessage());
        }
        
        return Embalaje;
    }
    
    public Embalaje getEmbalajeGenericoByEmpresa(int idEmpresa) throws Exception{
        Embalaje embalaje = null;
        
        try{
            EmbalajeDaoImpl embalajeDaoImpl = new EmbalajeDaoImpl(this.conn);
            embalaje = embalajeDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (embalaje==null){
                throw new Exception("La empresa no tiene creada algun Embalaje");
            }
        }catch(EmbalajeDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada algun Embalaje");
        }
        
        return embalaje;
    }
    
    /**
     * Realiza una búsqueda por ID Embalaje en busca de
     * coincidencias
     * @param idMarca ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public Embalaje[] findEmbalajes(int idEmbalaje, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        Embalaje[] embalajeDto = new Embalaje[0];
        EmbalajeDaoImpl embalajeDao = new EmbalajeDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idEmbalaje>0){
                sqlFiltro ="ID_EMBALAJE=" + idEmbalaje + " AND ";
            }else{
                sqlFiltro ="ID_EMBALAJE>0 AND";
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
            
            embalajeDto = embalajeDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return embalajeDto;
    }
    
    /**
     * Realiza una búsqueda por ID Embalaje en busca de
     * coincidencias
     * @param idEmbalaje ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar embalajes, -1 para evitar filtro     
     * @return String de cada uno de los embalajes
     */
    
        public String getEmbalajesByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            Embalaje[] embalajesDto = findEmbalajes(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (Embalaje embalaje:embalajesDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==embalaje.getIdEmbalaje())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+embalaje.getIdEmbalaje()+"' "
                            + selectedStr
                            + "title='"+embalaje.getNombre()+"'>"
                            + embalaje.getNombre()
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
    

