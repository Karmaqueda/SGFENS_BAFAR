/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 17-ene-2013 
 */
public class LoginEmpleadoMovilResponse extends WSResponse{
    /**
     * Objeto con Datos de Empleado
     */
    private WsItemEmpleado wsItemEmpleado;
    
    /**
     * Objeto con datos de empresa-sucursal a la que pertenece el empleado
     */
    private WsItemSucursal wsItemSucursal;
    
    /**
     * Objeto con información sobre el tipo de servicio
     * contratado en TPV moviles
     */
    private WsItemTipoServicio wsItemTipoServicio;
    
    private int tiempoEstatus;
    private int tiempoRecordatorio;
    
    private List<WsItemHorarioDetalle> wsItemHorarioDetalle;
    
    private List<WsItemFoliosMovilEmpleado> wsItemFoliosMovilEmpleado;
    
    private WsItemRegion wsItemRegion;
    
    private Date fechaHoraUltimoInventarioInicial;
    
    private int intervaloUbicacionSegundos;
    
    public WsItemEmpleado getWsItemEmpleado() {
        return wsItemEmpleado;
    }

    public void setWsItemEmpleado(WsItemEmpleado wsItemEmpleado) {
        this.wsItemEmpleado = wsItemEmpleado;
    }
    
    public WsItemSucursal getWsItemSucursal() {
        return wsItemSucursal;
    }

    public void setWsItemSucursal(WsItemSucursal wsItemSucursal) {
        this.wsItemSucursal = wsItemSucursal;
    }

    public WsItemTipoServicio getWsItemTipoServicio() {
        return wsItemTipoServicio;
    }

    public void setWsItemTipoServicio(WsItemTipoServicio wsItemTipoServicio) {
        this.wsItemTipoServicio = wsItemTipoServicio;
    }

    /**
     * @return the tiempoEstatus
     */
    public int getTiempoEstatus() {
        return tiempoEstatus;
    }

    /**
     * @param tiempoEstatus the tiempoEstatus to set
     */
    public void setTiempoEstatus(int tiempoEstatus) {
        this.tiempoEstatus = tiempoEstatus;
    }

    public int getTiempoRecordatorio() {
        return tiempoRecordatorio;
    }

    public void setTiempoRecordatorio(int tiempoRecordatorio) {
        this.tiempoRecordatorio = tiempoRecordatorio;
    }

    public List<WsItemHorarioDetalle> getWsItemHorarioDetalle() {
        if (wsItemHorarioDetalle==null)
            wsItemHorarioDetalle = new ArrayList<WsItemHorarioDetalle>();
        return wsItemHorarioDetalle;
    }

    public void setWsItemHorarioDetalle(List<WsItemHorarioDetalle> wsItemHorarioDetalle) {
        this.wsItemHorarioDetalle = wsItemHorarioDetalle;
    }

    public List<WsItemFoliosMovilEmpleado> getWsItemFoliosMovilEmpleado() {
        if (wsItemFoliosMovilEmpleado==null)
            wsItemFoliosMovilEmpleado =  new ArrayList<WsItemFoliosMovilEmpleado>();
        return wsItemFoliosMovilEmpleado;
    }

    public void setWsItemFoliosMovilEmpleado(List<WsItemFoliosMovilEmpleado> wsItemFoliosMovilEmpleado) {
        this.wsItemFoliosMovilEmpleado = wsItemFoliosMovilEmpleado;
    }

    public WsItemRegion getWsItemRegion() {
        return wsItemRegion;
    }

    public void setWsItemRegion(WsItemRegion wsItemRegion) {
        this.wsItemRegion = wsItemRegion;
    }

    public Date getFechaHoraUltimoInventarioInicial() {
        return fechaHoraUltimoInventarioInicial;
    }

    public void setFechaHoraUltimoInventarioInicial(Date fechaHoraUltimoInventarioInicial) {
        this.fechaHoraUltimoInventarioInicial = fechaHoraUltimoInventarioInicial;
    }

    public int getIntervaloUbicacionSegundos() {
        return intervaloUbicacionSegundos;
    }

    public void setIntervaloUbicacionSegundos(int intervaloUbicacionSegundos) {
        this.intervaloUbicacionSegundos = intervaloUbicacionSegundos;
    }
    
}
