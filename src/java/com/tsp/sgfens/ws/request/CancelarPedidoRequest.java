package com.tsp.sgfens.ws.request;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 03-jun-2013 
 */
public class CancelarPedidoRequest {

    private int idServerPedido;
    private boolean cancelarAbonosPedido = true;
    
    private boolean enviarCorreoNotificacion=false;
    private String[] listaCorreosDestinatarios= new String[0];

    public boolean isCancelarAbonosPedido() {
        return cancelarAbonosPedido;
    }

    public void setCancelarAbonosPedido(boolean cancelarAbonosPedido) {
        this.cancelarAbonosPedido = cancelarAbonosPedido;
    }

    public boolean isEnviarCorreoNotificacion() {
        return enviarCorreoNotificacion;
    }

    public void setEnviarCorreoNotificacion(boolean enviarCorreoNotificacion) {
        this.enviarCorreoNotificacion = enviarCorreoNotificacion;
    }

    public int getIdServerPedido() {
        return idServerPedido;
    }

    public void setIdServerPedido(int idServerPedido) {
        this.idServerPedido = idServerPedido;
    }

    public String[] getListaCorreosDestinatarios() {
        return listaCorreosDestinatarios;
    }

    public void setListaCorreosDestinatarios(String[] listaCorreosDestinatarios) {
        this.listaCorreosDestinatarios = listaCorreosDestinatarios;
    }
    
}
