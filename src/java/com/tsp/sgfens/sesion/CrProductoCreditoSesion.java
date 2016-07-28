/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.sesion;

import com.tsp.sct.dao.dto.CrProductoCredito;
import com.tsp.sct.dao.dto.CrProductoPuntajeMonto;
import com.tsp.sct.dao.dto.CrProductoRegla;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ISCesar
 */
public class CrProductoCreditoSesion implements Serializable{
    
    private Connection conn;
    private CrProductoCredito crProductoCredito;
    private List<CrProductoRegla> listaReglas;
    private List<CrProductoPuntajeMonto> listaProductoPuntajeMonto;  
    private List<Integer> listaIdDocImprimibles;

    public CrProductoCreditoSesion() {
    }

    public CrProductoCreditoSesion(Connection conn) {
        this.conn = conn;
    }
    
    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public CrProductoCredito getCrProductoCredito() {
        return crProductoCredito;
    }

    public void setCrProductoCredito(CrProductoCredito crProductoCredito) {
        this.crProductoCredito = crProductoCredito;
    }

    public List<CrProductoRegla> getListaReglas() {
        if (listaReglas==null)
            listaReglas = new ArrayList<CrProductoRegla>();
        return listaReglas;
    }

    public void setListaReglas(List<CrProductoRegla> listaReglas) {
        this.listaReglas = listaReglas;
    }

    public List<CrProductoPuntajeMonto> getListaProductoPuntajeMonto() {
        if (listaProductoPuntajeMonto==null)
            listaProductoPuntajeMonto = new ArrayList<CrProductoPuntajeMonto>();
        return listaProductoPuntajeMonto;
    }

    public void setListaProductoPuntajeMonto(List<CrProductoPuntajeMonto> listaProductoPuntajeMonto) {
        this.listaProductoPuntajeMonto = listaProductoPuntajeMonto;
    }

    public List<Integer> getListaIdDocImprimibles() {
        if (listaIdDocImprimibles==null)
            listaIdDocImprimibles = new ArrayList<Integer>();
        return listaIdDocImprimibles;
    }

    public void setListaIdDocImprimibles(List<Integer> listaIdDocImprimibles) {
        this.listaIdDocImprimibles = listaIdDocImprimibles;
    }
    
    
    public int buscarIndexListaReglas(String claveTipoRegla){
        int index = -1;
        int i = 0;
        for (CrProductoRegla crProductoRegla : listaReglas){
            if (crProductoRegla.getClaveTipoRegla().equalsIgnoreCase(claveTipoRegla)){
                index = i;
                break;
            }
            i++;
        }
        return index;
    }
    
    
}
