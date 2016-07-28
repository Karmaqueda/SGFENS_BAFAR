/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.NominaBanco;
import com.tsp.sct.dao.jdbc.NominaBancoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class NominaBancoBO {
    
    private NominaBanco nominaBanco = null;

    public NominaBanco getNominaBanco() {
        return nominaBanco;
    }

    public void setNominaBanco(NominaBanco nominaBanco) {
        this.nominaBanco = nominaBanco;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public NominaBancoBO(Connection conn){
        this.conn = conn;
    }       
    
    public NominaBancoBO(int idNominaBanco, Connection conn){
        this.conn = conn;
        try{
            NominaBancoDaoImpl NominaBancoDaoImpl = new NominaBancoDaoImpl(this.conn);
            this.nominaBanco = NominaBancoDaoImpl.findByPrimaryKey(idNominaBanco);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public NominaBanco findMarcabyId(int idNominaBanco) throws Exception{
        NominaBanco NominaBanco = null;
        
        try{
            NominaBancoDaoImpl NominaBancoDaoImpl = new NominaBancoDaoImpl(this.conn);
            NominaBanco = NominaBancoDaoImpl.findByPrimaryKey(idNominaBanco);
            if (NominaBanco==null){
                throw new Exception("No se encontro ningun Banco que corresponda con los parámetros específicados.");
            }
            if (NominaBanco.getIdBanco()<=0){
                throw new Exception("No se encontro ningun Banco que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Banco del usuario. Error: " + e.getMessage());
        }
        
        return NominaBanco;
    }    
  
    /**
     * Realiza una búsqueda por ID NominaBanco en busca de
     * coincidencias
     * @param idBanco ID Del Banco para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public NominaBanco[] findNominaBancos(int idNominaBanco, int minLimit,int maxLimit, String filtroBusqueda) {
        NominaBanco[] nominaBancoDto = new NominaBanco[0];
        NominaBancoDaoImpl nominaBancoDao = new NominaBancoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idNominaBanco>0){
                sqlFiltro ="ID_BANCO=" + idNominaBanco + " ";
            }else{
                sqlFiltro ="ID_BANCO>0 ";
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
            
            nominaBancoDto = nominaBancoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return nominaBancoDto;
    }
    
    /**
     * Realiza una búsqueda por ID NominaBanco en busca de
     * coincidencias
     * @param idNominaBanco ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar nominaBancos, -1 para evitar filtro     
     * @return String de cada uno de los nominaBancos
     */
    
        public String getNominaBancosByIdHTMLCombo(int idSeleccionado){
        String strHTMLCombo ="";

        try{
            NominaBanco[] nominaBancosDto = findNominaBancos(-1, 0, 0, "");
            
            for (NominaBanco nominaBanco:nominaBancosDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==nominaBanco.getIdBanco())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+nominaBanco.getIdBanco()+"' "
                            + selectedStr
                            + "title='"+nominaBanco.getNombreORazonSocial()+"'>"
                            + nominaBanco.getNombre()
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
