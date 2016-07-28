/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.microsip.bean;

import com.tsp.sct.dao.dto.ExistenciaAlmacen;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author ISCesar poseidon24@hotmail.com
 * @since 4/05/2015 4/05/2015 11:53:10 AM
 */
public class AlmacenMicrosip {

    private int idMicrosip;
    private String clave = "";
    private Almacen almacen = null;
    private List<ExistenciaAlmacen> listRelacProdAlmacen = new ArrayList<ExistenciaAlmacen>();

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

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    /**
     * @return the listRelacProdAlmacen
     */
    public List<ExistenciaAlmacen> getListRelacProdAlmacen() {
        return listRelacProdAlmacen;
    }

    /**
     * @param listRelacProdAlmacen the listRelacProdAlmacen to set
     */
    public void setListRelacProdAlmacen(List<ExistenciaAlmacen> listRelacProdAlmacen) {
        this.listRelacProdAlmacen = listRelacProdAlmacen;
    }

    
    
}
