/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

/**
 *
 * @author MOVILPYME
 */
public class WsItemPedidosDevoluciones extends WSResponseInsert {
    
    private int idConcepto;
    private String folioMovil;

    /**
     * @return the idConceto
     */
    public int getIdConcepto() {
        return idConcepto;
    }

    /**
     * @param idConceto the idConceto to set
     */
    public void setIdConcepto(int idConcepto) {
        this.idConcepto = idConcepto;
    }

    public String getFolioMovil() {
        return folioMovil;
    }

    public void setFolioMovil(String folioMovil) {
        this.folioMovil = folioMovil;
    }
    
}
