package com.tsp.sgfens.ws.bo;

import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sgfens.ws.response.comprobante.WSComprobanteFiscalDigital;
import com.tsp.sgfens.ws.response.comprobante.WSTimbreFiscalDigital;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mx.bigdata.sat.cfdi.CFDv32;
import mx.bigdata.sat.cfdi.TFDv1_v32;
import mx.bigdata.sat.cfdi.schema.v32.Comprobante;
import mx.bigdata.sat.cfdi.schema.v32.TimbreFiscalDigital;
import mx.bigdata.sat.schema.binder.DateTimeCustomBinder;

/**
 *
 * @author ISCesarMartinez
 */
public class ComprobanteFiscalDigitalBO {
    
    private WSComprobanteFiscalDigital comprobanteFiscalDigital = null;
    private WSTimbreFiscalDigital timbreFiscalDigitalResponse = null;
    
    private Connection conn = null;

    public Connection getConn() {
        if (this.conn==null){
            try {
                this.conn= ResourceManager.getConnection();
            } catch (SQLException ex) {}
        }else{
            try {
                if (this.conn.isClosed()){
                    this.conn = ResourceManager.getConnection();
                }
            } catch (SQLException ex) {}
        }
        return this.conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public ComprobanteFiscalDigitalBO(Connection conn) {
        this.conn = conn;
    }
    
    public WSComprobanteFiscalDigital transformaAWSComprobanteFiscalDigital(Comprobante comprobante, CFDv32 cfdi){
        comprobanteFiscalDigital = new WSComprobanteFiscalDigital();
        //this.comprobante = comprobante;
        
        WSComprobanteFiscalDigital objetoDestino =  new WSComprobanteFiscalDigital();
        
        objetoDestino.setVersion(comprobante.getVersion());
        objetoDestino.setSerie(comprobante.getSerie());
        objetoDestino.setFolio(comprobante.getFolio());
        objetoDestino.setFecha(comprobante.getFecha());
        objetoDestino.setSello(comprobante.getSello());
        objetoDestino.setFormaDePago(comprobante.getFormaDePago());
        objetoDestino.setNoCertificado(comprobante.getNoCertificado());
        objetoDestino.setCertificado(comprobante.getCertificado());
        objetoDestino.setCondicionesDePago(comprobante.getCondicionesDePago());
        objetoDestino.setSubTotal(comprobante.getSubTotal()!=null?comprobante.getSubTotal().doubleValue():0);
        objetoDestino.setDescuento(comprobante.getDescuento()!=null?comprobante.getDescuento().doubleValue():0);
        objetoDestino.setMotivoDescuento(comprobante.getMotivoDescuento());
        objetoDestino.setTipoCambio(comprobante.getTipoCambio());
        objetoDestino.setMoneda(comprobante.getMoneda());
        objetoDestino.setTotal(comprobante.getTotal()!=null?comprobante.getTotal().doubleValue():0);
        objetoDestino.setTipoDeComprobante(comprobante.getTipoDeComprobante());
        objetoDestino.setMetodoDePago(comprobante.getMetodoDePago());
        objetoDestino.setLugarExpedicion(comprobante.getLugarExpedicion());
        objetoDestino.setNumCtaPago(comprobante.getNumCtaPago());
        objetoDestino.setFolioFiscalOrig(comprobante.getFolioFiscalOrig());
        objetoDestino.setSerieFolioFiscalOrig(comprobante.getSerieFolioFiscalOrig());
        objetoDestino.setFechaFolioFiscalOrig(comprobante.getFechaFolioFiscalOrig());
        objetoDestino.setMontoFolioFiscalOrig(comprobante.getMontoFolioFiscalOrig()!=null?comprobante.getMontoFolioFiscalOrig().doubleValue():0);
        
        
        /**
         * Emisor
         */
        WSComprobanteFiscalDigital.Emisor emisor = new WSComprobanteFiscalDigital.Emisor();
        emisor.setNombre(comprobante.getEmisor().getNombre());
        emisor.setRfc(comprobante.getEmisor().getRfc());
        if (comprobante.getEmisor().getDomicilioFiscal()!=null){
            WSComprobanteFiscalDigital.TUbicacionFiscal ubicacionFiscalEmisor = new WSComprobanteFiscalDigital.TUbicacionFiscal();
            ubicacionFiscalEmisor.setCalle(comprobante.getEmisor().getDomicilioFiscal().getCalle());
            ubicacionFiscalEmisor.setCodigoPostal(comprobante.getEmisor().getDomicilioFiscal().getCodigoPostal());
            ubicacionFiscalEmisor.setColonia(comprobante.getEmisor().getDomicilioFiscal().getColonia());
            ubicacionFiscalEmisor.setEstado(comprobante.getEmisor().getDomicilioFiscal().getEstado());
            ubicacionFiscalEmisor.setLocalidad(comprobante.getEmisor().getDomicilioFiscal().getLocalidad());
            ubicacionFiscalEmisor.setMunicipio(comprobante.getEmisor().getDomicilioFiscal().getMunicipio());
            ubicacionFiscalEmisor.setNoExterior(comprobante.getEmisor().getDomicilioFiscal().getNoExterior());
            ubicacionFiscalEmisor.setNoInterior(comprobante.getEmisor().getDomicilioFiscal().getNoInterior());
            ubicacionFiscalEmisor.setPais(comprobante.getEmisor().getDomicilioFiscal().getPais());
            ubicacionFiscalEmisor.setReferencia(comprobante.getEmisor().getDomicilioFiscal().getReferencia());


            emisor.setDomicilioFiscal(ubicacionFiscalEmisor);
        }
        if (comprobante.getEmisor().getExpedidoEn()!=null){
            WSComprobanteFiscalDigital.TUbicacion ubicacionEmisor = new WSComprobanteFiscalDigital.TUbicacion();
            ubicacionEmisor.setCalle(comprobante.getEmisor().getExpedidoEn().getCalle());
            ubicacionEmisor.setCodigoPostal(comprobante.getEmisor().getExpedidoEn().getCodigoPostal());
            ubicacionEmisor.setColonia(comprobante.getEmisor().getExpedidoEn().getColonia());
            ubicacionEmisor.setEstado(comprobante.getEmisor().getExpedidoEn().getEstado());
            ubicacionEmisor.setLocalidad(comprobante.getEmisor().getExpedidoEn().getLocalidad());
            ubicacionEmisor.setMunicipio(comprobante.getEmisor().getExpedidoEn().getMunicipio());
            ubicacionEmisor.setNoExterior(comprobante.getEmisor().getExpedidoEn().getNoExterior());
            ubicacionEmisor.setNoInterior(comprobante.getEmisor().getExpedidoEn().getNoInterior());
            ubicacionEmisor.setPais(comprobante.getEmisor().getExpedidoEn().getPais());
            ubicacionEmisor.setReferencia(comprobante.getEmisor().getExpedidoEn().getReferencia());

            emisor.setExpedidoEn(ubicacionEmisor);
        }
        if (comprobante.getEmisor().getRegimenFiscal()!=null){
            List<WSComprobanteFiscalDigital.Emisor.RegimenFiscal> listaRegimenFiscal = new ArrayList<WSComprobanteFiscalDigital.Emisor.RegimenFiscal>();
            for (mx.bigdata.sat.cfdi.schema.v32.Comprobante.Emisor.RegimenFiscal regFiscal:comprobante.getEmisor().getRegimenFiscal()){
                WSComprobanteFiscalDigital.Emisor.RegimenFiscal regimenFiscal = new WSComprobanteFiscalDigital.Emisor.RegimenFiscal();
                regimenFiscal.setRegimen(regFiscal.getRegimen());

                listaRegimenFiscal.add(regimenFiscal);
            }
            emisor.setRegimenFiscal(listaRegimenFiscal);
        }
        objetoDestino.setEmisor(emisor);
        
            
       /**
        * Receptor
        */
       WSComprobanteFiscalDigital.Receptor receptor = new WSComprobanteFiscalDigital.Receptor();
       receptor.setNombre(comprobante.getReceptor().getNombre());
       receptor.setRfc(comprobante.getReceptor().getRfc());
       if (comprobante.getReceptor().getDomicilio()!=null){
            WSComprobanteFiscalDigital.TUbicacion ubicacionReceptor = new WSComprobanteFiscalDigital.TUbicacion();
            ubicacionReceptor.setCalle(comprobante.getReceptor().getDomicilio().getCalle());
            ubicacionReceptor.setCodigoPostal(comprobante.getReceptor().getDomicilio().getCodigoPostal());
            ubicacionReceptor.setColonia(comprobante.getReceptor().getDomicilio().getColonia());
            ubicacionReceptor.setEstado(comprobante.getReceptor().getDomicilio().getEstado());
            ubicacionReceptor.setLocalidad(comprobante.getReceptor().getDomicilio().getLocalidad());
            ubicacionReceptor.setMunicipio(comprobante.getReceptor().getDomicilio().getMunicipio());
            ubicacionReceptor.setNoExterior(comprobante.getReceptor().getDomicilio().getNoExterior());
            ubicacionReceptor.setNoInterior(comprobante.getReceptor().getDomicilio().getNoInterior());
            ubicacionReceptor.setPais(comprobante.getReceptor().getDomicilio().getPais());
            ubicacionReceptor.setReferencia(comprobante.getReceptor().getDomicilio().getReferencia());

            receptor.setDomicilio(ubicacionReceptor);
        }
       objetoDestino.setReceptor(receptor);
       
       /**
        * Conceptos
        */
       WSComprobanteFiscalDigital.Conceptos conceptos = new WSComprobanteFiscalDigital.Conceptos();
       List<WSComprobanteFiscalDigital.Conceptos.Concepto> listaConcepto = new ArrayList<WSComprobanteFiscalDigital.Conceptos.Concepto>();
       for (mx.bigdata.sat.cfdi.schema.v32.Comprobante.Conceptos.Concepto schemaConcepto : comprobante.getConceptos().getConcepto() ){
           WSComprobanteFiscalDigital.Conceptos.Concepto concepto = new WSComprobanteFiscalDigital.Conceptos.Concepto();
           concepto.setCantidad(schemaConcepto.getCantidad().doubleValue());
           //concepto.setComplementoConcepto(null);  //FALTA APLICAR CODIGO PARA RECUPERAR DATO
           //concepto.setCuentaPredial(null); //FALTA APLICAR CODIGO PARA RECUPERAR DATO
           concepto.setDescripcion(schemaConcepto.getDescripcion());
           concepto.setImporte(schemaConcepto.getImporte().doubleValue());
           concepto.setNoIdentificacion(schemaConcepto.getNoIdentificacion());
           concepto.setUnidad(schemaConcepto.getUnidad());
           concepto.setValorUnitario(schemaConcepto.getValorUnitario().doubleValue());
           
           listaConcepto.add(concepto);
       }
       conceptos.setConcepto(listaConcepto);
       
       objetoDestino.setConceptos(conceptos);
       
       /**
        * Impuestos
        */
       if (comprobante.getImpuestos()!=null){
            WSComprobanteFiscalDigital.Impuestos impuestos = new WSComprobanteFiscalDigital.Impuestos();

            if (comprobante.getImpuestos().getTraslados()!=null){
                WSComprobanteFiscalDigital.Impuestos.Traslados traslados = new WSComprobanteFiscalDigital.Impuestos.Traslados();
                for (mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado impTraslado : comprobante.getImpuestos().getTraslados().getTraslado()){
                    WSComprobanteFiscalDigital.Impuestos.Traslados.Traslado traslado = new WSComprobanteFiscalDigital.Impuestos.Traslados.Traslado();
                    traslado.setImporte(impTraslado.getImporte().doubleValue());
                    traslado.setImpuesto(impTraslado.getImpuesto());
                    traslado.setTasa(impTraslado.getTasa().doubleValue());

                    traslados.getTraslado().add(traslado);
                }
                impuestos.setTraslados(traslados);
            }

            if (comprobante.getImpuestos().getRetenciones()!=null){
                WSComprobanteFiscalDigital.Impuestos.Retenciones retencions = new WSComprobanteFiscalDigital.Impuestos.Retenciones();
                for (mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion impRetencion : comprobante.getImpuestos().getRetenciones().getRetencion()){
                    WSComprobanteFiscalDigital.Impuestos.Retenciones.Retencion retencion = new WSComprobanteFiscalDigital.Impuestos.Retenciones.Retencion();
                    retencion.setImporte(impRetencion.getImporte().doubleValue());
                    retencion.setImpuesto(impRetencion.getImpuesto());

                    retencions.getRetencion().add(retencion);
                }
                impuestos.setRetenciones(retencions);
            }        

            impuestos.setTotalImpuestosRetenidos(comprobante.getImpuestos().getTotalImpuestosRetenidos()!=null?comprobante.getImpuestos().getTotalImpuestosRetenidos().doubleValue():0);
            impuestos.setTotalImpuestosTrasladados(comprobante.getImpuestos().getTotalImpuestosTrasladados()!=null?comprobante.getImpuestos().getTotalImpuestosTrasladados().doubleValue():0);

            objetoDestino.setImpuestos(impuestos);
       }
       
       /**
        * Complementos
        * NO IMPLEMENTADO
        */
       /**
        * Addendas
        * NO IMPLEMENTADO
        */
        
        
        comprobanteFiscalDigital = objetoDestino;
        
        try{
            comprobanteFiscalDigital.setComprobanteCadenaOriginal(cfdi.getCadenaOriginal());
        }catch(Exception ex){
            System.out.println("No se pudo recuperar la cadena original del comprobante");
            ex.printStackTrace();
        }
        
        
        return comprobanteFiscalDigital;
    }
    
    public WSTimbreFiscalDigital transformaTimbreAResponseType(TimbreFiscalDigital timbreFiscalDigital, TFDv1_v32 tfd){
        timbreFiscalDigitalResponse = new WSTimbreFiscalDigital();
        //this.timbreFiscalDigital = timbreFiscalDigital;
        
        //Pasamos todos los valores de un objeto a otro
        timbreFiscalDigitalResponse.setFechaTimbrado(DateTimeCustomBinder.printDateTime(timbreFiscalDigital.getFechaTimbrado()));
        timbreFiscalDigitalResponse.setNoCertificadoSAT(timbreFiscalDigital.getNoCertificadoSAT());
        timbreFiscalDigitalResponse.setSelloCFD(timbreFiscalDigital.getSelloCFD());
        timbreFiscalDigitalResponse.setSelloSAT(timbreFiscalDigital.getSelloSAT());
        timbreFiscalDigitalResponse.setUUID(timbreFiscalDigital.getUUID());
        timbreFiscalDigitalResponse.setVersion(timbreFiscalDigital.getVersion());
        
        try{
            timbreFiscalDigitalResponse.setTimbreFiscalDigitalCadenaOriginal(tfd.getCadenaOriginal());
        }catch(Exception ex){
            System.out.println("No se pudo recuperar la cadena original del Timbre Fiscal Digital del comprobante");
            ex.printStackTrace();
        }
        
        return timbreFiscalDigitalResponse;
    }

   
}
