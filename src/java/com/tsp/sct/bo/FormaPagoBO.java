/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.FormaPago;
import com.tsp.sct.dao.exceptions.FormaPagoDaoException;
import com.tsp.sct.dao.jdbc.FormaPagoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISCesarMartinez
 */
public class FormaPagoBO {
    FormaPago formaPago = null;

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public FormaPagoBO(Connection conn) {
        this.conn = conn;
    }
    
    public FormaPagoBO(int idFormaPago, Connection conn) {
        this.conn = conn;
        try{
          this.formaPago = new FormaPagoDaoImpl(this.conn).findByPrimaryKey(idFormaPago);
        }catch(FormaPagoDaoException ex){
            ex.printStackTrace();
        }    
    }
    
    public FormaPago findFormaPagoById(int idFormaPago) throws Exception{
        FormaPago FormaPago = null;
        
        try{
            FormaPagoDaoImpl empresaDaoImpl = new FormaPagoDaoImpl(this.conn);
            FormaPago = empresaDaoImpl.findByPrimaryKey(idFormaPago);
            if (FormaPago==null){
                throw new Exception("No se encontro ningun Forma de Pago que corresponda con los parámetros específicados.");
            }
            if (FormaPago.getIdFormaPago()<=0){
                throw new Exception("No se encontro ningun Forma de Pago que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de Forma de Pago. Error: " + e.getMessage());
        }
        
        return FormaPago;
    }
    
    /**
     * Realiza una búsqueda por ID FormaPago en busca de
     * coincidencias
     * @param idFormaPago ID Del Usuario para filtrar, -1 para mostrar todos los registros
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO FormaPago
     */
    public FormaPago[] findFormaPagos(int idFormaPago, int minLimit,int maxLimit, String filtroBusqueda) {
        FormaPago[] formaPagoDto = new FormaPago[0];
        FormaPagoDaoImpl formaPagoDao = new FormaPagoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idFormaPago>0){
                sqlFiltro ="ID_FORMA_PAGO=" + idFormaPago + " ";
            }else{
                sqlFiltro ="ID_FORMA_PAGO>=-1 ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            formaPagoDto = formaPagoDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_FORMA_PAGO ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return formaPagoDto;
    }
    
    /**
     * Realiza una búsqueda por ID FormaPago en busca de
     * coincidencias
     * @param idFormaPago ID Del Usuario para filtrar, -1 para mostrar todos los registros  
     * @return String de cada una de las FormaPago
     */
    
        public String getFormaPagosByIdHTMLCombo(int idSeleccionado){
        String strHTMLCombo ="";

        try{
            FormaPago[] formaPagosDto = findFormaPagos(-1, 0, 0, "");
            
            for (FormaPago itemFormaPago:formaPagosDto){
                try{
                    String selectedStr="";

                    if (idSeleccionado==itemFormaPago.getIdFormaPago())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+itemFormaPago.getIdFormaPago()+"' "
                            + selectedStr
                            + "title='"+itemFormaPago.getDescFormaPago()+"'>"
                            + itemFormaPago.getDescFormaPago() 
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
