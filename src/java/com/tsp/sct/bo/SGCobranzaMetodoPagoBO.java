/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SgfensCobranzaMetodoPago;
import com.tsp.sct.dao.jdbc.SgfensCobranzaMetodoPagoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 07-ene-2013 
 */
public class SGCobranzaMetodoPagoBO {
    
    SgfensCobranzaMetodoPago cobranzaMetodoPago = null;

    public SgfensCobranzaMetodoPago getCobranzaMetodoPago() {
        return cobranzaMetodoPago;
    }

    public void setCobranzaMetodoPago(SgfensCobranzaMetodoPago cobranzaMetodoPago) {
        this.cobranzaMetodoPago = cobranzaMetodoPago;
    }

    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SGCobranzaMetodoPagoBO(Connection conn) {
        this.conn = conn;
    }

    public SGCobranzaMetodoPagoBO(int idSgfensCobranzaMetodoPago, Connection conn) {
        this.conn = conn;
        try{
            this.cobranzaMetodoPago = new SgfensCobranzaMetodoPagoDaoImpl(this.conn).findByPrimaryKey(idSgfensCobranzaMetodoPago);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * Genera el códigho HTML necesario para las opciones de un select
     * correspondientes a los registros de Metodos de Pago de la cobranza
     * @param idSeleccionado ID del metodo de pago que debe marcarse como seleccionado    
     * @return String de cada una de las marcas
     */
    
        public String getMetodoPagoHTMLCombo(int idSeleccionado){
        String strHTMLCombo ="";

        try{
            SgfensCobranzaMetodoPago[] metodosPago = new SgfensCobranzaMetodoPagoDaoImpl(this.conn).findAll();
            
            for (SgfensCobranzaMetodoPago item:metodosPago){
                try{
                    String selectedStr="";

                    if (idSeleccionado==item.getIdCobranzaMetodoPago())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+item.getIdCobranzaMetodoPago()+"' "
                            + selectedStr
                            + "title='"+item.getDescripcionMetodoPago()+"'>"
                            + item.getNombreMetodoPago()
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
        
        
    public String getTodoMetodoPagoByHTMLCombo(){
        String strHTMLCombo ="";        

        try{
            SgfensCobranzaMetodoPago[] sgfensCobranzaMetodoPagoDto = new SgfensCobranzaMetodoPagoDaoImpl(this.conn).findAll();
            
            for (SgfensCobranzaMetodoPago itemMetodo:sgfensCobranzaMetodoPagoDto){
                try{
                   
                    String selectedStr="";                   

                    strHTMLCombo += "<option value='"+itemMetodo.getIdCobranzaMetodoPago()+"' "
                            + selectedStr
                            + "title='"+itemMetodo.getNombreMetodoPago()+"'>"
                            + itemMetodo.getNombreMetodoPago()
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
     * Genera el códigho HTML necesario para las opciones de un select
     * correspondientes a los registros de Metodos de Pago de la cobranza
     * @param idEmpresa ID del metodo de pago que debe marcarse como seleccionado    
     * @param idSeleccionado ID del metodo de pago que debe marcarse como seleccionado   
     * @param filtroBusqueda filtro adicional para busqueda
     * @return String de cada una de las marcas
     */
    
        public String getMetodoPagoHTMLCombo(int idSeleccionado ,int idEmpresa , String filtroBusqueda){
        String strHTMLCombo ="";

        try{
            SgfensCobranzaMetodoPago[] metodosPago = findMetodosPago(idSeleccionado,idEmpresa,-1,-1,filtroBusqueda);
            
            for (SgfensCobranzaMetodoPago item:metodosPago){
                try{
                    String selectedStr="";

                    if (idSeleccionado==item.getIdCobranzaMetodoPago())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+item.getIdCobranzaMetodoPago()+"' "
                            + selectedStr
                            + "title='"+item.getDescripcionMetodoPago()+"'>"
                            + item.getNombreMetodoPago()
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
     * Realiza una búsqueda en busca de
     * coincidencias
     * @param idMetodoPago ID Del método de pago para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO MetodoPago
     */
    public SgfensCobranzaMetodoPago[] findMetodosPago(int idMetodoPago, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        SgfensCobranzaMetodoPago[] metodosPagoDto = new SgfensCobranzaMetodoPago[0];
        SgfensCobranzaMetodoPagoDaoImpl metodosPagoDao = new SgfensCobranzaMetodoPagoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idMetodoPago>0){
                sqlFiltro ="ID_COBRANZA_METODO_PAGO=" + idMetodoPago + " ";
            }else{
                sqlFiltro ="ID_COBRANZA_METODO_PAGO>0 ";
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
            
            metodosPagoDto = metodosPagoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_COBRANZA_METODO_PAGO ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return metodosPagoDto;
    }

}
