package com.tsp.sgfens.ws.request;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 17-may-2013 
 */
public class CrearFacturaDePedidoRequest {
    
    private int idServerPedido;
    
    private boolean enviarCorreoRepresentacionImpresa=false;
    private String[] listaCorreosDestinatarios= new String[0];

    public int getIdServerPedido() {
        return idServerPedido;
    }

    public void setIdServerPedido(int idServerPedido) {
        this.idServerPedido = idServerPedido;
    }

    public boolean isEnviarCorreoRepresentacionImpresa() {
        return enviarCorreoRepresentacionImpresa;
    }

    public void setEnviarCorreoRepresentacionImpresa(boolean enviarCorreoRepresentacionImpresa) {
        this.enviarCorreoRepresentacionImpresa = enviarCorreoRepresentacionImpresa;
    }

    public String[] getListaCorreosDestinatarios() {
        return listaCorreosDestinatarios;
    }

    public void setListaCorreosDestinatarios(String[] listaCorreosDestinatarios) {
        this.listaCorreosDestinatarios = listaCorreosDestinatarios;
    }
    

}
