/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 20-dic-2012 
 */
public class EstatusComprobanteBO {

    public static final int ESTATUS_NUEVA = 1;
    public static final int ESTATUS_COTIZACION = 2;
    public static final int ESTATUS_ENTREGADA = 3;
    public static final int ESTATUS_CANCELADA = 4;
    public static final int ESTATUS_CANCELACION = 5;
    
    public static final String ESTATUS_NUEVA_NAME = "Nueva";
    public static final String ESTATUS_COTIZACION_NAME = "Cotizacion";
    public static final String ESTATUS_ENTREGADA_NAME = "Entregada";
    public static final String ESTATUS_CANCELADA_NAME = "Cancelada";
    public static final String ESTATUS_CANCELACION_NAME = "Cancelacion";
    
     /**
     * Obtiene el nombre escrito (cadena) de un Estatus de ComprobanteFiscal por medio de su ID
     * @return Cadena con el nombre descriptivo del Estatus de ComprobanteFiscal
     */
    public static String getEstatusName(int idEstatusComprobanteFiscal){
        String estatusName="";
        switch (idEstatusComprobanteFiscal){
            case ESTATUS_NUEVA:
                return ESTATUS_NUEVA_NAME;
            case ESTATUS_COTIZACION:
                return ESTATUS_COTIZACION_NAME;
            case ESTATUS_ENTREGADA:
                return ESTATUS_ENTREGADA_NAME;
            case ESTATUS_CANCELADA:
                return ESTATUS_CANCELADA_NAME;
            case ESTATUS_CANCELACION:
                return ESTATUS_CANCELACION_NAME;
            default:
                estatusName ="Estatus de Comprobante Fiscal no Existente";
        }
        return estatusName;
    }    
    
}
