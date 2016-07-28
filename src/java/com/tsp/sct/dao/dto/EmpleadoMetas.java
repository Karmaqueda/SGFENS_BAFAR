/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.dao.dto;

import java.util.ArrayList;

/**
 *
 * @author HpPyme
 */
public class EmpleadoMetas {
    
    
   protected int idempleado;
   protected ArrayList<Double> listaMetas = new ArrayList<Double>();

    public EmpleadoMetas() {
    }
  
    public int getIdempleado() {
        return idempleado;
    }

    public void setIdempleado(int idempleado) {
        this.idempleado = idempleado;
    }

    public ArrayList<Double> getListaMetas() {
        return listaMetas;
    }

    public void setListaMetas(ArrayList<Double> listaMetas) {
        this.listaMetas = listaMetas;
    }
   
    
    
}
