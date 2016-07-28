/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.NominaRegimenFiscal;
import com.tsp.sct.dao.exceptions.NominaRegimenFiscalDaoException;
import com.tsp.sct.dao.jdbc.NominaRegimenFiscalDaoImpl;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class NominaRegimenFiscalBO {
    
    private NominaRegimenFiscal nominaRegimenFiscal = null;

    public NominaRegimenFiscal getNominaRegimenFiscal() {
        return nominaRegimenFiscal;
    }

    public void setNominaRegimenFiscal(NominaRegimenFiscal nominaRegimenFiscal) {
        this.nominaRegimenFiscal = nominaRegimenFiscal;
    }
    
    private Connection conn = null;
    
    public NominaRegimenFiscalBO(Connection conn){
        this.conn = conn;
    }       
    
    public NominaRegimenFiscalBO(int idNominaRegimenFiscal, Connection conn){
        this.conn = conn;
        try{
            NominaRegimenFiscalDaoImpl NominaRegimenFiscalDaoImpl = new NominaRegimenFiscalDaoImpl(this.conn);
            this.nominaRegimenFiscal = NominaRegimenFiscalDaoImpl.findByPrimaryKey(idNominaRegimenFiscal);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public NominaRegimenFiscal findMarcabyId(int idNominaRegimenFiscal) throws Exception{
        NominaRegimenFiscal NominaRegimenFiscal = null;
        
        try{
            NominaRegimenFiscalDaoImpl NominaRegimenFiscalDaoImpl = new NominaRegimenFiscalDaoImpl(this.conn);
            NominaRegimenFiscal = NominaRegimenFiscalDaoImpl.findByPrimaryKey(idNominaRegimenFiscal);
            if (NominaRegimenFiscal==null){
                throw new Exception("No se encontro ningun Regimen Fiscal que corresponda con los parámetros específicados.");
            }
            if (NominaRegimenFiscal.getIdRegimen()<=0){
                throw new Exception("No se encontro ningun Regimen Fiscal que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Regimen Fiscal del usuario. Error: " + e.getMessage());
        }
        
        return NominaRegimenFiscal;
    }    
  
    /**
     * Realiza una búsqueda por ID NominaRegimenFiscal en busca de
     * coincidencias
     * @param idMarca ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public NominaRegimenFiscal[] findNominaRegimenFiscals(int idNominaRegimenFiscal, int minLimit,int maxLimit, String filtroBusqueda) {
        NominaRegimenFiscal[] nominaRegimenFiscalDto = new NominaRegimenFiscal[0];
        NominaRegimenFiscalDaoImpl nominaRegimenFiscalDao = new NominaRegimenFiscalDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idNominaRegimenFiscal>0){
                sqlFiltro ="ID_REGIMEN=" + idNominaRegimenFiscal + " ";
            }else{
                sqlFiltro ="ID_REGIMEN>0 ";
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
            
            nominaRegimenFiscalDto = nominaRegimenFiscalDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY REGIMEN_FISCAL ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return nominaRegimenFiscalDto;
    }
    
    /**
     * Realiza una búsqueda por ID NominaRegimenFiscal en busca de
     * coincidencias
     * @param idNominaRegimenFiscal ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar nominaRegimenFiscals, -1 para evitar filtro     
     * @return String de cada uno de los nominaRegimenFiscals
     */
    
        public String getNominaRegimenFiscalsByIdHTMLCombo(int idSeleccionado){
        String strHTMLCombo ="";

        try{
            NominaRegimenFiscal[] nominaRegimenFiscalsDto = findNominaRegimenFiscals(-1, 0, 0, "");
            
            for (NominaRegimenFiscal nominaRegimenFiscal:nominaRegimenFiscalsDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==nominaRegimenFiscal.getIdRegimen())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+nominaRegimenFiscal.getIdRegimen()+"' "
                            + selectedStr
                            + "title='"+nominaRegimenFiscal.getClave()+"'>"
                            + nominaRegimenFiscal.getRegimenFiscal()
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
