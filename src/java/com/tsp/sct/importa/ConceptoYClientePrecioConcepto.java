/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.importa;

import com.tsp.sct.dao.dto.ClientePrecioConcepto;
import com.tsp.sct.dao.dto.Concepto;

/**
 *
 * @author leonardo
 */
public class ConceptoYClientePrecioConcepto extends Concepto{
    
    ClientePrecioConcepto clientePrecioConcepto = new ClientePrecioConcepto();

    public ClientePrecioConcepto getClientePrecioConcepto() {
        return clientePrecioConcepto;
    }

    public void setClientePrecioConcepto(ClientePrecioConcepto clientePrecioConcepto) {
        this.clientePrecioConcepto = clientePrecioConcepto;
    }
    
    
    
}
