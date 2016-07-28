package com.tsp.sgfens.ws.response;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ISCesarMartinez
 */
public class ConsultaGeocercasResponse extends WSResponse {

    private List<WsItemGeocerca> wsItemGeocerca;

    public List<WsItemGeocerca> getWsItemGeocerca() {
        if (this.wsItemGeocerca==null)
            wsItemGeocerca= new ArrayList<WsItemGeocerca>();
        return wsItemGeocerca;
    }

    public void setWsItemGeocerca(List<WsItemGeocerca> wsItemGeocerca) {
        this.wsItemGeocerca = wsItemGeocerca;
    }
    
}
