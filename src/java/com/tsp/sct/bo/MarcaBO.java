/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Marca;
import com.tsp.sct.dao.exceptions.MarcaDaoException;
import com.tsp.sct.dao.jdbc.MarcaDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 19-dic-2012 
 */
public class MarcaBO {
 private Marca marca = null;

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public MarcaBO(Connection conn){
        this.conn = conn;
    }
    
    public MarcaBO(int idMarca, Connection conn){        
        this.conn = conn;
        try{
            MarcaDaoImpl MarcaDaoImpl = new MarcaDaoImpl(this.conn);
            this.marca = MarcaDaoImpl.findByPrimaryKey(idMarca);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Marca findMarcabyId(int idMarca) throws Exception{
        Marca Marca = null;
        
        try{
            MarcaDaoImpl MarcaDaoImpl = new MarcaDaoImpl(this.conn);
            Marca = MarcaDaoImpl.findByPrimaryKey(idMarca);
            if (Marca==null){
                throw new Exception("No se encontro ninguna Marca que corresponda con los parámetros específicados.");
            }
            if (Marca.getIdMarca()<=0){
                throw new Exception("No se encontro ninguna Marca que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la Marca del usuario. Error: " + e.getMessage());
        }
        
        return Marca;
    }
    
    public Marca getMarcaGenericoByEmpresa(int idEmpresa) throws Exception{
        Marca marca = null;
        
        try{
            MarcaDaoImpl marcaDaoImpl = new MarcaDaoImpl(this.conn);
            marca = marcaDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (marca==null){
                throw new Exception("La empresa no tiene creada alguna Marca");
            }
        }catch(MarcaDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada alguna Marca");
        }
        
        return marca;
    }
    
    /**
     * Realiza una búsqueda por ID Marca en busca de
     * coincidencias
     * @param idMarca ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public Marca[] findMarcas(int idMarca, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        Marca[] marcaDto = new Marca[0];
        MarcaDaoImpl marcaDao = new MarcaDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idMarca>0){
                sqlFiltro ="ID_MARCA=" + idMarca + " AND ";
            }else{
                sqlFiltro ="ID_MARCA>0 AND";
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
            
            marcaDto = marcaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return marcaDto;
    }
    
    /**
     * Realiza una búsqueda por ID Marca en busca de
     * coincidencias
     * @param idMarca ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro     
     * @return String de cada una de las marcas
     */
    
        public String getMarcasByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            Marca[] marcasDto = findMarcas(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (Marca marca:marcasDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==marca.getIdMarca())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+marca.getIdMarca()+"' "
                            + selectedStr
                            + "title='"+marca.getNombre()+"'>"
                            + marca.getNombre()
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
