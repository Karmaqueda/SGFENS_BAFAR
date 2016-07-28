/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.request;

/**
 *
 * @author leonardo
 */
public class UsuarioNuevoDtoRequest {
    
    private int idUsuario = 0;
    private String userAcceso = "";
    private String passAcceso = "";
    
    private String nombre;
    private String aPaterno;
    private String aMaterno;
    private String nombreEmpresa;
    private String eMail;
    private String telefono;
    private String imei;
    private String marcaCel;
    private String modeloCel;
    
    private boolean error = false;
    private int numError = 0;
    private String msgError = "";
    

    
    public UsuarioNuevoDtoRequest(){}
    /**
     * @return the idUsuario
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * @param idUsuario the idUsuario to set
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * @return the userAcceso
     */
    public String getUserAcceso() {
        return userAcceso;
    }

    /**
     * @param userAcceso the userAcceso to set
     */
    public void setUserAcceso(String userAcceso) {
        this.userAcceso = userAcceso;
    }

    /**
     * @return the passAcceso
     */
    public String getPassAcceso() {
        return passAcceso;
    }

    /**
     * @param passAcceso the passAcceso to set
     */
    public void setPassAcceso(String passAcceso) {
        this.passAcceso = passAcceso;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the aPaterno
     */
    public String getaPaterno() {
        return aPaterno;
    }

    /**
     * @param aPaterno the aPaterno to set
     */
    public void setaPaterno(String aPaterno) {
        this.aPaterno = aPaterno;
    }

    /**
     * @return the aMaterno
     */
    public String getaMaterno() {
        return aMaterno;
    }

    /**
     * @param aMaterno the aMaterno to set
     */
    public void setaMaterno(String aMaterno) {
        this.aMaterno = aMaterno;
    }

    /**
     * @return the nombreEmpresa
     */
    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    /**
     * @param nombreEmpresa the nombreEmpresa to set
     */
    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    /**
     * @return the eMail
     */
    public String geteMail() {
        return eMail;
    }

    /**
     * @param eMail the eMail to set
     */
    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the imei
     */
    public String getImei() {
        return imei;
    }

    /**
     * @param imei the imei to set
     */
    public void setImei(String imei) {
        this.imei = imei;
    }

    /**
     * @return the marcaCel
     */
    public String getMarcaCel() {
        return marcaCel;
    }

    /**
     * @param marcaCel the marcaCel to set
     */
    public void setMarcaCel(String marcaCel) {
        this.marcaCel = marcaCel;
    }

    /**
     * @return the modeloCel
     */
    public String getModeloCel() {
        return modeloCel;
    }

    /**
     * @param modeloCel the modeloCel to set
     */
    public void setModeloCel(String modeloCel) {
        this.modeloCel = modeloCel;
    }

    /**
     * @return the error
     */
    public boolean isError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * @return the numError
     */
    public int getNumError() {
        return numError;
    }

    /**
     * @param numError the numError to set
     */
    public void setNumError(int numError) {
        this.numError = numError;
    }

    /**
     * @return the msgError
     */
    public String getMsgError() {
        return msgError;
    }

    /**
     * @param msgError the msgError to set
     */
    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }
    
    
}
