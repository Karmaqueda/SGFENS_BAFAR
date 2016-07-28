/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.sesion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ISCesar
 */
public class CrFormularioDisenoSesion {
    
    private Connection conn;
    private List<CrFormularioCampoSesion> listaCampoSesion = null;
    
    private int idFormulario;

    public CrFormularioDisenoSesion() {
    }

    public CrFormularioDisenoSesion(Connection conn) {
        this.conn = conn;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public List<CrFormularioCampoSesion> getListaCampoSesion() {
        if (listaCampoSesion==null)
            listaCampoSesion = new ArrayList<CrFormularioCampoSesion>();
        return listaCampoSesion;
    }

    public void setListaCampoSesion(List<CrFormularioCampoSesion> listaCampoSesion) {
        this.listaCampoSesion = listaCampoSesion;
    }

    public int getIdFormulario() {
        return idFormulario;
    }

    public void setIdFormulario(int idFormulario) {
        this.idFormulario = idFormulario;
    }
    
    public int getUltimoIdentificadorCampoSesion(){
        int ultimoIdentificadorCampoSesion = 0;
        for (CrFormularioCampoSesion item : getListaCampoSesion()){
            if (item.getIdentificadorCampoSesion() > ultimoIdentificadorCampoSesion){
                ultimoIdentificadorCampoSesion = item.getIdentificadorCampoSesion();
            }
        }
        return ultimoIdentificadorCampoSesion;
    }
    
    public int getUltimoOrdenCampoSesion(){
        int ultimoOrden = 0;
        for (CrFormularioCampoSesion item : getListaCampoSesion()){
            if (item.getOrden()> ultimoOrden){
                ultimoOrden = item.getOrden();
            }
        }
        return ultimoOrden;
    }
    
    public int buscaIndexCampoSesion(int idFormularioCampo, int identificadorCampoSesion){
        int indexCampoEnLista = -1;
        int i = 0;
        for (CrFormularioCampoSesion item : getListaCampoSesion()){
            if (idFormularioCampo>0){
                // Ya existia en bd, es una edición
                // buscamos por IdFormularioCampo
                if (item.getIdFormularioCampo() == idFormularioCampo){
                    indexCampoEnLista = i;
                }
            }else if (identificadorCampoSesion>0 && item.getIdentificadorCampoSesion()==identificadorCampoSesion){
                // es un formulario o campo nuevo
                // buscamos a traves del identificador de campo en sesion
                indexCampoEnLista = i;
            }
            i++;
        }
        return indexCampoEnLista;
    }
    
}
