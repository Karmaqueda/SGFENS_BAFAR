/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.PaqueteRelacionConcepto;
import com.tsp.sct.dao.exceptions.PaqueteRelacionConceptoDaoException;
import com.tsp.sct.dao.jdbc.PaqueteRelacionConceptoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */
public class PaqueteRelacionConceptoBO {
    
    private PaqueteRelacionConcepto paqueteRelacionConcepto = null;

    public PaqueteRelacionConcepto getPaqueteRelacionConcepto() {
        return paqueteRelacionConcepto;
    }

    public void setPaqueteRelacionConcepto(PaqueteRelacionConcepto paqueteRelacionConcepto) {
        this.paqueteRelacionConcepto = paqueteRelacionConcepto;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public PaqueteRelacionConceptoBO(Connection conn){
        this.conn = conn;
    }
    
    public PaqueteRelacionConceptoBO(int idPaqueteRelacionConcepto, Connection conn){
        this.conn = conn;
        try{
            PaqueteRelacionConceptoDaoImpl PaqueteRelacionConceptoDaoImpl = new PaqueteRelacionConceptoDaoImpl(this.conn);
            this.paqueteRelacionConcepto = PaqueteRelacionConceptoDaoImpl.findByPrimaryKey(idPaqueteRelacionConcepto);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public PaqueteRelacionConcepto findPaqueteRelacionConceptobyId(int idPaqueteRelacionConcepto) throws Exception{
        PaqueteRelacionConcepto PaqueteRelacionConcepto = null;
        
        try{
            PaqueteRelacionConceptoDaoImpl PaqueteRelacionConceptoDaoImpl = new PaqueteRelacionConceptoDaoImpl(this.conn);
            PaqueteRelacionConcepto = PaqueteRelacionConceptoDaoImpl.findByPrimaryKey(idPaqueteRelacionConcepto);
            if (PaqueteRelacionConcepto==null){
                throw new Exception("No se encontro ninguna PaqueteRelacionConcepto que corresponda con los parámetros específicados.");
            }
            if (PaqueteRelacionConcepto.getIdPaqueteRelacionConcepto()<=0){
                throw new Exception("No se encontro ninguna PaqueteRelacionConcepto que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de la PaqueteRelacionConcepto del usuario. Error: " + e.getMessage());
        }
        
        return PaqueteRelacionConcepto;
    }
    
    public PaqueteRelacionConcepto getPaqueteRelacionConceptoGenericoByEmpresa(int idEmpresa) throws Exception{
        PaqueteRelacionConcepto paqueteRelacionConcepto = null;
        
        try{
            PaqueteRelacionConceptoDaoImpl paqueteRelacionConceptoDaoImpl = new PaqueteRelacionConceptoDaoImpl(this.conn);
            paqueteRelacionConcepto = paqueteRelacionConceptoDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND ID_ESTATUS = 1", new Object[0])[0];
            if (paqueteRelacionConcepto==null){
                throw new Exception("La empresa no tiene creada alguna PaqueteRelacionConcepto");
            }
        }catch(PaqueteRelacionConceptoDaoException  e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creada alguna PaqueteRelacionConcepto");
        }
        
        return paqueteRelacionConcepto;
    }
    
    /**
     * Realiza una búsqueda por ID PaqueteRelacionConcepto en busca de
     * coincidencias
     * @param idPaqueteRelacionConcepto ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar paqueteRelacionConceptos, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO PaqueteRelacionConcepto
     */
    public PaqueteRelacionConcepto[] findPaqueteRelacionConceptos(long idPaqueteRelacionConcepto, long idConcepto, int minLimit,int maxLimit, String filtroBusqueda) {
        PaqueteRelacionConcepto[] paqueteRelacionConceptoDto = new PaqueteRelacionConcepto[0];
        PaqueteRelacionConceptoDaoImpl paqueteRelacionConceptoDao = new PaqueteRelacionConceptoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idPaqueteRelacionConcepto>0){
                sqlFiltro ="ID_PAQUETE_RELACION_CONCEPTO=" + idPaqueteRelacionConcepto + " AND ID_ESTATUS = 1 AND ";
            }else{
                sqlFiltro ="ID_PAQUETE_RELACION_CONCEPTO>0 AND ID_ESTATUS = 1 AND ";
            }
            if (idConcepto>0){                
                sqlFiltro += " ID_CONCEPTO = " + idConcepto + " ";
            }else{
                sqlFiltro +=" ID_CONCEPTO>0";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            paqueteRelacionConceptoDto = paqueteRelacionConceptoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_PAQUETE_RELACION_CONCEPTO ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return paqueteRelacionConceptoDto;
    }
    
    /**
     * Realiza una búsqueda por ID PaqueteRelacionConcepto en busca de
     * coincidencias
     * @param idPaqueteRelacionConcepto ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar paqueteRelacionConceptos, -1 para evitar filtro     
     * @return String de cada una de las paqueteRelacionConceptos
     */
    
        public String getPaqueteRelacionConceptosByIdHTMLCombo(long idEmpresa, long idSeleccionado){
        String strHTMLCombo ="";

        try{
            //PaqueteRelacionConcepto[] paqueteRelacionConceptosDto = findPaqueteRelacionConceptos(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            PaqueteRelacionConcepto[] paqueteRelacionConceptosDto = findPaqueteRelacionConceptos(-1, idEmpresa, 0, 0, "");
            
            for (PaqueteRelacionConcepto paqueteRelacionConcepto:paqueteRelacionConceptosDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==paqueteRelacionConcepto.getIdPaqueteRelacionConcepto())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+paqueteRelacionConcepto.getIdPaqueteRelacionConcepto()+"' "
                            + selectedStr
                            + "title='"+paqueteRelacionConcepto.getCantidad()+"'>"
                            + paqueteRelacionConcepto.getIdPaqueteRelacionConcepto()
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
