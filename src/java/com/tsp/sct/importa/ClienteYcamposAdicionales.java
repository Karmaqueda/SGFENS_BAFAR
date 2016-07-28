/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.importa;

import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.dto.ClienteCampoContenido;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class ClienteYcamposAdicionales extends Cliente{
        
    //listado de campos adicionales
    public List<ClienteCampoContenido> listClienteCampoContenido = new ArrayList<ClienteCampoContenido>();
    
    /**
     * @return the listClienteCampoContenido
     */
    public List<ClienteCampoContenido> getListClienteCampoContenido() {
        return listClienteCampoContenido;
    }

    /**
     * @param listClienteCampoContenido the listClienteCampoContenido to set
     */
    public void setListClienteCampoContenido(List<ClienteCampoContenido> listClienteCampoContenido) {
        this.listClienteCampoContenido = listClienteCampoContenido;
    }
    
    
    
}
