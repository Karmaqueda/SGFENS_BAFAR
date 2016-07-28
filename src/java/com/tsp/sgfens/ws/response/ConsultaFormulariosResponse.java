/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ing. Roberto Trejo
 */
public class ConsultaFormulariosResponse extends WSResponse implements Serializable {
    
    protected List<WsItemTipoCampo> wsItemTipoCampo;
    protected List<WsItemFormularioValidacion> wsItemFormularioValidacion;
    protected List<WsItemGrupoFormulario> wsItemGrupoFormulario;

    /**
     * @return the wsItemTipoCampo
     */
    public List<WsItemTipoCampo> getWsItemTipoCampo() {
        if(wsItemTipoCampo == null)
            wsItemTipoCampo = new ArrayList<WsItemTipoCampo>();
        return wsItemTipoCampo;
    }

    /**
     * @param wsItemTipoCampo the wsItemTipoCampo to set
     */
    public void setWsItemTipoCampo(List<WsItemTipoCampo> wsItemTipoCampo) {
        this.wsItemTipoCampo = wsItemTipoCampo;
    }

    /**
     * @return the wsItemFormularioValidacion
     */
    public List<WsItemFormularioValidacion> getWsItemFormularioValidacion() {
        if(wsItemFormularioValidacion == null)
            wsItemFormularioValidacion = new ArrayList<WsItemFormularioValidacion>();
        return wsItemFormularioValidacion;
    }

    /**
     * @param wsItemFormularioValidacion the wsItemFormularioValidacion to set
     */
    public void setWsItemFormularioValidacion(List<WsItemFormularioValidacion> wsItemFormularioValidacion) {
        this.wsItemFormularioValidacion = wsItemFormularioValidacion;
    }

    /**
     * @return the wsItemGrupoFormulario
     */
    public List<WsItemGrupoFormulario> getWsItemGrupoFormulario() {
        if(wsItemGrupoFormulario == null)
            wsItemGrupoFormulario = new ArrayList<WsItemGrupoFormulario>();
        return wsItemGrupoFormulario;
    }

    /**
     * @param wsItemGrupoFormulario the wsItemGrupoFormulario to set
     */
    public void setWsItemGrupoFormulario(List<WsItemGrupoFormulario> wsItemGrupoFormulario) {
        this.wsItemGrupoFormulario = wsItemGrupoFormulario;
    }
    
}
