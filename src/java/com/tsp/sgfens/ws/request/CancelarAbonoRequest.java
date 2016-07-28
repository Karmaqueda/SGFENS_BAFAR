package com.tsp.sgfens.ws.request;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 01-jun-2013 
 */
public class CancelarAbonoRequest {

    private int idServerCobranzaAbono;
    private int idServerBancoOperacion;
    
    /**
     * Datos necesarios por si se requiere actualizar los datos
     * del cobro una vez cancelados.
     */
    private WsBancoOperacionRequest bancoOperacion;

    public WsBancoOperacionRequest getBancoOperacion() {
        return bancoOperacion;
    }

    public void setBancoOperacion(WsBancoOperacionRequest bancoOperacion) {
        this.bancoOperacion = bancoOperacion;
    }

    public int getIdServerBancoOperacion() {
        return idServerBancoOperacion;
    }

    public void setIdServerBancoOperacion(int idServerBancoOperacion) {
        this.idServerBancoOperacion = idServerBancoOperacion;
    }

    public int getIdServerCobranzaAbono() {
        return idServerCobranzaAbono;
    }

    public void setIdServerCobranzaAbono(int idServerCobranzaAbono) {
        this.idServerCobranzaAbono = idServerCobranzaAbono;
    }    
    
}
