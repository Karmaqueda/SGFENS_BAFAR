/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.ClienteCategoria;
import com.tsp.sct.dao.exceptions.ClienteCategoriaDaoException;
import com.tsp.sct.dao.jdbc.ClienteCategoriaDaoImpl;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */
public class ClienteCategoriaBO {
    
    private ClienteCategoria clienteCategoria = null;
    
 public ClienteCategoria getClienteCategoria() {
        return clienteCategoria;
    }
 
    public void setClienteCategoria(ClienteCategoria clienteCategoria) {
        this.clienteCategoria = clienteCategoria;
    }

    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public ClienteCategoriaBO(Connection conn){
        this.conn = conn;
    }
    
    
    public ClienteCategoriaBO(int idClienteCategoria, Connection conn){
        this.conn = conn;
        try{
            ClienteCategoriaDaoImpl ClienteCategoriaDaoImpl = new ClienteCategoriaDaoImpl(this.conn);
            this.clienteCategoria = ClienteCategoriaDaoImpl.findByPrimaryKey(idClienteCategoria);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
     public ClienteCategoria findClienteCategoriaById(int idClienteCategoria) throws Exception{
        ClienteCategoria ClienteCategoria = null;
        
        try{
            ClienteCategoriaDaoImpl ClienteCategoriaDaoImpl = new ClienteCategoriaDaoImpl(this.conn);
            ClienteCategoria = ClienteCategoriaDaoImpl.findByPrimaryKey(idClienteCategoria);
            if (ClienteCategoria==null){
                throw new Exception("No se encontro ningun Cliente Categoria que corresponda con los parámetros específicados.");
            }
            if (ClienteCategoria.getIdCategoria()<=0){
                throw new Exception("No se encontro ningun Cliente Categoria que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del ClienteCategoria del usuario. Error: " + e.getMessage());
        }
        
        return ClienteCategoria;
    }
    
    /**
     * Realiza una búsqueda por ID ClienteCategoria en busca de
     * coincidencias
     * @param idCategoria ID De la categoria para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public ClienteCategoria[] findClienteCategorias(int idClienteCategoria, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        ClienteCategoria[] clienteCategoriaDto = new ClienteCategoria[0];
        ClienteCategoriaDaoImpl clienteCategoriaDao = new ClienteCategoriaDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idClienteCategoria>0){
                sqlFiltro ="ID_CATEGORIA=" + idClienteCategoria + " AND ";
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
            
            clienteCategoriaDto = clienteCategoriaDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE_CLASIFICACION ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return clienteCategoriaDto;
    }
    
    /**
     * Realiza una búsqueda por ID ClienteCategoria en busca de
     * coincidencias
     * @param idClienteCategoria ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar clienteCategorias, -1 para evitar filtro     
     * @return String de cada uno de los clienteCategorias
     */
    
        public String getClienteCategoriasByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";

        try{
            ClienteCategoria[] clienteCategoriasDto = findClienteCategorias(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (ClienteCategoria clienteCategoria:clienteCategoriasDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==clienteCategoria.getIdCategoria())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+clienteCategoria.getIdCategoria()+"' "
                            + selectedStr
                            + "title='"+clienteCategoria.getDescripcion()+"'>"
                            + clienteCategoria.getNombreClasificacion()
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


