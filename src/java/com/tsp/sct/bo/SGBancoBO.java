/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SgfensBanco;
import com.tsp.sct.dao.exceptions.SgfensBancoDaoException;
import com.tsp.sct.dao.jdbc.SgfensBancoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */
public class SGBancoBO {
    
    private SgfensBanco sgfensBanco = null;

    public SgfensBanco getSgfensBanco() {
        return sgfensBanco;
    }

    public void setSgfensBanco(SgfensBanco sgfensBanco) {
        this.sgfensBanco = sgfensBanco;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SGBancoBO(Connection conn){
        this.conn = conn;
    }
    
    public SGBancoBO(int idSgfensBanco, Connection conn){
        this.conn = conn;
        try{
            SgfensBancoDaoImpl SgfensBancoDaoImpl = new SgfensBancoDaoImpl(this.conn);
            this.sgfensBanco = SgfensBancoDaoImpl.findByPrimaryKey(idSgfensBanco);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public SgfensBanco findSgfensBancobyId(int idSgfensBanco) throws Exception{
        SgfensBanco SgfensBanco = null;
        
        try{
            SgfensBancoDaoImpl SgfensBancoDaoImpl = new SgfensBancoDaoImpl(this.conn);
            SgfensBanco = SgfensBancoDaoImpl.findByPrimaryKey(idSgfensBanco);
            if (SgfensBanco==null){
                throw new Exception("No se encontro ninguna SgfensBanco que corresponda con los parámetros específicados.");
            }
            if (SgfensBanco.getIdBanco()<=0){
                throw new Exception("No se encontro ninguna SgfensBanco que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la SgfensBanco del usuario. Error: " + e.getMessage());
        }
        
        return SgfensBanco;
    }
    
    public SgfensBanco getSgfensBancoGenericoByEmpresa(int idEmpresa) throws Exception{
        SgfensBanco sgfensBanco = null;
        
        try{
            SgfensBancoDaoImpl sgfensBancoDaoImpl = new SgfensBancoDaoImpl(this.conn);
            sgfensBanco = sgfensBancoDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (sgfensBanco==null){
                throw new Exception("La empresa no tiene creada alguna SgfensBanco");
            }
        }catch(SgfensBancoDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada alguna SgfensBanco");
        }
        
        return sgfensBanco;
    }
    
    /**
     * Realiza una búsqueda por ID SgfensBanco en busca de
     * coincidencias
     * @param idSgfensBanco ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar sgfensBancos, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO SgfensBanco
     */
    public SgfensBanco[] findSgfensBancos(long idSgfensBanco, long idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SgfensBanco[] sgfensBancoDto = new SgfensBanco[0];
        SgfensBancoDaoImpl sgfensBancoDao = new SgfensBancoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idSgfensBanco>0){
                sqlFiltro ="ID_BANCO=" + idSgfensBanco + " AND ID_ESTATUS = 1 AND ";
            }else{
                sqlFiltro ="ID_BANCO>0 AND ID_ESTATUS = 1 AND ";
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
            
            sgfensBancoDto = sgfensBancoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE_BANCO ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return sgfensBancoDto;
    }
    
    /**
     * Realiza una búsqueda por ID SgfensBanco en busca de
     * coincidencias
     * @param idSgfensBanco ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar sgfensBancos, -1 para evitar filtro     
     * @return String de cada una de las sgfensBancos
     */
    
        public String getSgfensBancosByIdHTMLCombo(long idEmpresa, long idSeleccionado){
        String strHTMLCombo ="";

        try{
            //SgfensBanco[] sgfensBancosDto = findSgfensBancos(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            SgfensBanco[] sgfensBancosDto = findSgfensBancos(-1, idEmpresa, 0, 0, "");
            
            for (SgfensBanco sgfensBanco:sgfensBancosDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==sgfensBanco.getIdBanco())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+sgfensBanco.getIdBanco()+"' "
                            + selectedStr
                            + "title='"+sgfensBanco.getComentarios()+"'>"
                            + sgfensBanco.getNombreBanco()
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
