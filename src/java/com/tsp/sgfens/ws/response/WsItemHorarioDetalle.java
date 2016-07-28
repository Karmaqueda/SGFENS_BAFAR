/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

import java.util.Date;

/**
 *
 * @author ISCesar
 */
public class WsItemHorarioDetalle {

    private int idDetalleHorario;
    private int idHorario;
    private String dia;
    private Date horaEntrada;
    private Date horaSalida;
    private int diaDescanso;
    private Date comidaSalida;
    private Date comidaEntrada;
    private int periodoComida;
    private int tolerancia;

    public int getIdDetalleHorario() {
        return idDetalleHorario;
    }

    public void setIdDetalleHorario(int idDetalleHorario) {
        this.idDetalleHorario = idDetalleHorario;
    }

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public Date getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Date horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Date getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Date horaSalida) {
        this.horaSalida = horaSalida;
    }

    public int getDiaDescanso() {
        return diaDescanso;
    }

    public void setDiaDescanso(int diaDescanso) {
        this.diaDescanso = diaDescanso;
    }

    public Date getComidaSalida() {
        return comidaSalida;
    }

    public void setComidaSalida(Date comidaSalida) {
        this.comidaSalida = comidaSalida;
    }

    public Date getComidaEntrada() {
        return comidaEntrada;
    }

    public void setComidaEntrada(Date comidaEntrada) {
        this.comidaEntrada = comidaEntrada;
    }

    public int getPeriodoComida() {
        return periodoComida;
    }

    public void setPeriodoComida(int periodoComida) {
        this.periodoComida = periodoComida;
    }

    public int getTolerancia() {
        return tolerancia;
    }

    public void setTolerancia(int tolerancia) {
        this.tolerancia = tolerancia;
    }

    
}
