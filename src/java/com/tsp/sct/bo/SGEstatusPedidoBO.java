/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.SgfensEstatusPedido;
import com.tsp.sct.dao.jdbc.SgfensEstatusPedidoDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 14-dic-2012 
 */
public class SGEstatusPedidoBO {

    public static final int ESTATUS_PENDIENTE = 1;
    public static final int ESTATUS_ENTREGADO = 2;
    public static final int ESTATUS_CANCELADO = 3;
    public static final int ESTATUS_ENTREGADO_PARCIAL = 4;
    public static final int ESTATUS_CONSIGNA = 5;//Consigna Entregada
    public static final int ESTATUS_CONSIGNA_PARCIAL = 6;//Consigna Pendiente por Entregar
    
    public static final String ESTATUS_PENDIENTE_NAME = "Pendiente por Entregar";
    public static final String ESTATUS_ENTREGADO_NAME = "Entregado";
    public static final String ESTATUS_CANCELADO_NAME = "Cancelado";
    public static final String ESTATUS_ENTREGADO_PARCIAL_NAME = "Entregado Parcialmente";
    public static final String ESTATUS_CONSIGNA_NAME = "Entregada";
    public static final String ESTATUS_CONSIGNA_PARCIAL_NAME = "Pendiente por Entregar";
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public SGEstatusPedidoBO() {
    }
    
    public SGEstatusPedidoBO(Connection conn) {
        this.conn = conn;
    }
    
     /**
     * Obtiene el nombre escrito (cadena) de un Estatus de Pedido por medio de su ID
     * @return Cadena con el nombre descriptivo del Estatus de Pedido
     */
    public static String getEstatusName(int idEstatusPedido){
        String estatusName="";
        switch (idEstatusPedido){
            case ESTATUS_PENDIENTE:
                return ESTATUS_PENDIENTE_NAME;
            case ESTATUS_ENTREGADO:
                return ESTATUS_ENTREGADO_NAME;
            case ESTATUS_CANCELADO:
                return ESTATUS_CANCELADO_NAME;
            case ESTATUS_ENTREGADO_PARCIAL:
                return ESTATUS_ENTREGADO_PARCIAL_NAME;
            case ESTATUS_CONSIGNA:
                return ESTATUS_CONSIGNA_NAME;
            case ESTATUS_CONSIGNA_PARCIAL:
                return ESTATUS_CONSIGNA_PARCIAL_NAME;    
            default:
                estatusName ="Estatus de pedido no Existente";
        }
        return estatusName;
    }    
    
    public String getEstatusPedidosByIdHTMLCombo(int idSeleccionado){
        String strHTMLCombo ="";

        try{
            SgfensEstatusPedido[] estatus = new SgfensEstatusPedidoDaoImpl(conn).findAll();
            
            for (SgfensEstatusPedido itemEstatusPedido : estatus){
                try{
                    String selectedStr="";

                    if (idSeleccionado==itemEstatusPedido.getIdEstatusPedido())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+itemEstatusPedido.getIdEstatusPedido()+"' "
                            + selectedStr
                            + "title='"+itemEstatusPedido.getDescripcion()+"'>"
                            + itemEstatusPedido.getNombre()
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
