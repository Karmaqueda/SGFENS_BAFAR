package com.tsp.sgfens.ws.request;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 14-jun-2013 
 */
public class ReenviaComprobantesRequest {
    
    private boolean enviarPedido = true;
    private boolean enviarComprobanteFiscal = true;
    private boolean enviarCobranzaAbono = true;
    private boolean enviarBancoOperacion = true;
    
    private long idServerPedido;
    private long idServerComprobanteFiscal;
    private long idServerCobranzaAbono;
    private long idServerBancoOperacion;
    
    private String[] listaCorreosDestinatarios= new String[0];

    public boolean isEnviarPedido() {
        return enviarPedido;
    }

    public void setEnviarPedido(boolean enviarPedido) {
        this.enviarPedido = enviarPedido;
    }

    public String[] getListaCorreosDestinatarios() {
        return listaCorreosDestinatarios;
    }

    public void setListaCorreosDestinatarios(String[] listaCorreosDestinatarios) {
        this.listaCorreosDestinatarios = listaCorreosDestinatarios;
    }

    public boolean isEnviarBancoOperacion() {
        return enviarBancoOperacion;
    }

    public void setEnviarBancoOperacion(boolean enviarBancoOperacion) {
        this.enviarBancoOperacion = enviarBancoOperacion;
    }

    public boolean isEnviarCobranzaAbono() {
        return enviarCobranzaAbono;
    }

    public void setEnviarCobranzaAbono(boolean enviarCobranzaAbono) {
        this.enviarCobranzaAbono = enviarCobranzaAbono;
    }

    public long getIdServerBancoOperacion() {
        return idServerBancoOperacion;
    }

    public void setIdServerBancoOperacion(long idServerBancoOperacion) {
        this.idServerBancoOperacion = idServerBancoOperacion;
    }

    public long getIdServerCobranzaAbono() {
        return idServerCobranzaAbono;
    }

    public void setIdServerCobranzaAbono(long idServerCobranzaAbono) {
        this.idServerCobranzaAbono = idServerCobranzaAbono;
    }

    public long getIdServerComprobanteFiscal() {
        return idServerComprobanteFiscal;
    }

    public void setIdServerComprobanteFiscal(long idServerComprobanteFiscal) {
        this.idServerComprobanteFiscal = idServerComprobanteFiscal;
    }

    public long getIdServerPedido() {
        return idServerPedido;
    }

    public void setIdServerPedido(long idServerPedido) {
        this.idServerPedido = idServerPedido;
    }

    public boolean isEnviarComprobanteFiscal() {
        return enviarComprobanteFiscal;
    }

    public void setEnviarComprobanteFiscal(boolean enviarComprobanteFiscal) {
        this.enviarComprobanteFiscal = enviarComprobanteFiscal;
    }

}
