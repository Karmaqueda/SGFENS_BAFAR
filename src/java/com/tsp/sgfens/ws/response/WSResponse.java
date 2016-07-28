/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.response;

/**
 *
 * @author ISCesarMartinez poseidon24@hotmail.com
 * @date 17-ene-2013
 */
public class WSResponse {

    /**
     * Variable que indica si el proceso presento errores o fue exitoso.
     * False en caso de éxito, true en caso de uno o varios errores.
     */
    private boolean error = false;
    /**
     * Códigos de Error
     * DE MANEJO DE EXCEPCIONES INTERNAS DEL WS
     * <p/>
     * 901 - Error interno provocado por datos erroneos proporcionados por el
     * cliente
     * <p/>
     * 902 - Error interno no esperado
     * <p/>
     * 903 - Error interno de conexión al SAT
     * <p/>
     * @param numError Número de error estándar
     */
    private int numError;
    /**
     * Descripcion extendida del error. Vacío en caso de no existir error.
     */
    private String msgError = "";

    /**
     * Variable que indica si el proceso presento errores o fue exitoso.
     * False en caso de éxito, true en caso de uno o varios errores.
     */
    public boolean isError() {
        return error;
    }

    /**
     * Variable que indica si el proceso presento errores o fue exitoso.
     * False en caso de éxito, true en caso de uno o varios errores.
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * Descripcion extendida del error. Vacío en caso de no existir error.
     */
    public String getMsgError() {
        return msgError;
    }

    /**
     * Descripcion extendida del error. Vacío en caso de no existir error.
     */
    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    /**
     *
     * Códigos de Error
     * DE MANEJO DE EXCEPCIONES INTERNAS DEL WS
     * <p/>
     * 901 - Error interno provocado por datos erroneos proporcionados por el
     * cliente
     * <p/>
     * 902 - Error interno no esperado
     * <p/>
     * 903 - Error interno de conexión al SAT
     * <p/>
     * @param numError Número de error estándar
     */
    public int getNumError() {
        return numError;
    }

    /**
     *
     * Códigos de Error
     * DE MANEJO DE EXCEPCIONES INTERNAS DEL WS
     * <p/>
     * 901 - Error interno provocado por datos erroneos proporcionados por el
     * cliente
     * <p/>
     * 902 - Error interno no esperado
     * <p/>
     * 903 - Error interno de conexión al SAT
     * <p/>
     * @param numError Número de error estándar
     */
    public void setNumError(int numError) {
        this.numError = numError;
    }
}
