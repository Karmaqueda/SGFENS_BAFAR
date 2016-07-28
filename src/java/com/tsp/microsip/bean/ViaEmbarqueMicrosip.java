/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.microsip.bean;

import com.tsp.sct.dao.dto.ViaEmbarque;

/**
 *
 * @author leonardo
 */
public class ViaEmbarqueMicrosip {
    
    private int idMicrosip;
    private String clave = "";
    private ViaEmbarque viaEmbarque = null;

    public int getIdMicrosip() {
        return idMicrosip;
    }

    public void setIdMicrosip(int idMicrosip) {
        this.idMicrosip = idMicrosip;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public ViaEmbarque getViaEmbarque() {
        return viaEmbarque;
    }

    public void setViaEmbarque(ViaEmbarque viaEmbarque) {
        this.viaEmbarque = viaEmbarque;
    }
    
}
