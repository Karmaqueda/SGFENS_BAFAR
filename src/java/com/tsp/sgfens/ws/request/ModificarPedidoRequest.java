package com.tsp.sgfens.ws.request;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 13-jun-2013 
 */
public class ModificarPedidoRequest {

    private int idServerPedido;
    private int idEstatusPedido;
    private WsItemConceptoRequest[] wsItemConceptoRequest;

    public int getIdEstatusPedido() {
        return idEstatusPedido;
    }

    public void setIdEstatusPedido(int idEstatusPedido) {
        this.idEstatusPedido = idEstatusPedido;
    }

    public int getIdServerPedido() {
        return idServerPedido;
    }

    public void setIdServerPedido(int idServerPedido) {
        this.idServerPedido = idServerPedido;
    }

    public WsItemConceptoRequest[] getWsItemConceptoRequest() {
        return wsItemConceptoRequest;
    }

    public void setWsItemConceptoRequest(WsItemConceptoRequest[] wsItemConceptoRequest) {
        this.wsItemConceptoRequest = wsItemConceptoRequest;
    }
        
}