/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

/**
 *
 * @author 578
 */
public class CrearCotizacionResponse extends WSResponseInsert {
    
    private String folioCotizacionCreado;

    public String getFolioCotizacionCreado() {
        return folioCotizacionCreado;
    }

    public void setFolioCotizacionCreado(String folioCotizacionCreado) {
        this.folioCotizacionCreado = folioCotizacionCreado;
    }
        
    
}
