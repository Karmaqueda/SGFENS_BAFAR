/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.cfdi;

import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.dto.Empresa;

import com.tsp.sct.dao.dto.FormaPago;
import com.tsp.sct.dao.dto.TipoPago;
import com.tsp.sct.dao.dto.Ubicacion;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import mx.bigdata.sat.cfdi.schema.Comprobante;
import mx.bigdata.sat.cfdi.schema.ObjectFactory;
import mx.bigdata.sat.cfdi.schema.TUbicacion;
import mx.bigdata.sat.cfdi.schema.TUbicacionFiscal;
import mx.bigdata.sat.cfdi.schema.Comprobante.Conceptos;
import mx.bigdata.sat.cfdi.schema.Comprobante.Emisor;
import mx.bigdata.sat.cfdi.schema.Comprobante.Impuestos;
import mx.bigdata.sat.cfdi.schema.Comprobante.Receptor;
import mx.bigdata.sat.cfdi.schema.Comprobante.Conceptos.Concepto;
import mx.bigdata.sat.cfdi.schema.Comprobante.Impuestos.Retenciones;
import mx.bigdata.sat.cfdi.schema.Comprobante.Impuestos.Retenciones.Retencion;
import mx.bigdata.sat.cfdi.schema.Comprobante.Impuestos.Traslados;
import mx.bigdata.sat.cfdi.schema.Comprobante.Impuestos.Traslados.Traslado;

/**
 *
 * @author ISCesarMartinez
 */
public class CFDFactory {
    
   public static Comprobante createComprobante(FormaPago formaPagoDto, TipoPago tipoPagoDto, Empresa empresaDto, Ubicacion ubicacionDto ,  Cliente clienteDto, ArrayList<Concepto> listaConceptos, ArrayList<Traslado> listaImpuestosTraslado, ArrayList<Retencion> listaImpuestosRetencion, double subtotal, double total) throws Exception {
    ObjectFactory of = new ObjectFactory();
    Comprobante comp = of.createComprobante();
    
    comp.setVersion("3.0");
    //Date date = new GregorianCalendar(2011, 11, 30, 20, 38, 12).getTime();
    Date date = new Date();
    
    comp.setFecha(date);
    comp.setFormaDePago(formaPagoDto.getDescFormaPago());//"PAGO EN UNA SOLA EXHIBICION"
    comp.setMetodoDePago(tipoPagoDto.getDescTipoPago()); //"EFECTIVO" //"CHEQUE" //...
    comp.setTipoDeComprobante("ingreso");
    
    comp.setEmisor(createEmisor(of, empresaDto, ubicacionDto));
    comp.setReceptor(createReceptor(of, clienteDto));
    comp.setConceptos(createConceptos(of, listaConceptos));
    comp.setImpuestos(createImpuestos(of, listaImpuestosTraslado, listaImpuestosRetencion));
    
    comp.setSubTotal(new BigDecimal(subtotal).setScale(2, RoundingMode.HALF_UP));
    comp.setTotal(new BigDecimal(total).setScale(2, RoundingMode.HALF_UP));
    
    return comp;
  }
    
  private static Emisor createEmisor(ObjectFactory of, Empresa empresaDto, Ubicacion ubicacionDto) {
    Emisor emisor = of.createComprobanteEmisor();
    emisor.setNombre(empresaDto.getRazonSocial());//"PHARMA PLUS SA DE CV");
    emisor.setRfc(empresaDto.getRfc());//"PNU111201U31");
    
    TUbicacionFiscal uf = of.createTUbicacionFiscal();
    uf.setCalle(ubicacionDto.getCalle());//"AV. RIO MIXCOAC");
    uf.setCodigoPostal(ubicacionDto.getCodigoPostal());//"03240");
    uf.setColonia(ubicacionDto.getColonia());//"ACACIAS"); 
    uf.setEstado(ubicacionDto.getEstado());//"DISTRITO FEDERAL"); 
    uf.setMunicipio(ubicacionDto.getMunicipio());//"BENITO JUAREZ"); 
    uf.setNoExterior(ubicacionDto.getNumExt());//"No. 140"); 
    uf.setNoInterior("".equals(ubicacionDto.getNumInt())?null:ubicacionDto.getNumInt());
    uf.setPais(ubicacionDto.getPais());//"México");
    emisor.setDomicilioFiscal(uf);
    
    //Expedido en
    /*
    TUbicacion u = of.createTUbicacion();
    u.setCalle("AV. UNIVERSIDAD");
    u.setCodigoPostal("03910");
    u.setColonia("OXTOPULCO"); 
    u.setEstado("DISTRITO FEDERAL");
    u.setNoExterior("1858");
    u.setPais("México"); 
    emisor.setExpedidoEn(u); 
    */
    
    return emisor;
  }

  private static Receptor createReceptor(ObjectFactory of, Cliente clienteDto) {
    Receptor receptor = of.createComprobanteReceptor();
    receptor.setNombre(clienteDto.getRazonSocial());//"JUAN PEREZ PEREZ");
    receptor.setRfc(clienteDto.getRfcCliente());//"PEPJ8001019Q8");
    
    TUbicacion uf = of.createTUbicacion();
    uf.setCalle(clienteDto.getCalle());//"AV UNIVERSIDAD");
    uf.setCodigoPostal(clienteDto.getCodigoPostal());//"04360");
    uf.setColonia(clienteDto.getColonia());//"COPILCO UNIVERSIDAD"); 
    uf.setEstado(clienteDto.getEstado());//"DISTRITO FEDERAL"); 
    uf.setMunicipio(clienteDto.getMunicipio());//"COYOACAN"); 
    uf.setNoExterior(clienteDto.getNumero());//"16 EDF 3"); 
    uf.setNoInterior("".equals(clienteDto.getNumeroInterior())?null:clienteDto.getNumeroInterior());//"DPTO 101");
    uf.setPais(clienteDto.getPais()); 
    receptor.setDomicilio(uf);
    
    return receptor;
  }

  private static Conceptos createConceptos(ObjectFactory of, List<Concepto> listaConcepto) {
    Conceptos cps = of.createComprobanteConceptos();
    
    cps.setConcepto(listaConcepto);
    
    return cps;
  }

  private static Impuestos createImpuestos(ObjectFactory of, List<Traslado> listaTraslado, List<Retencion> listaRetencion) {
    Impuestos imps = of.createComprobanteImpuestos();
    Traslados trs = of.createComprobanteImpuestosTraslados();
    Retenciones rts = of.createComprobanteImpuestosRetenciones();
   
    trs.setTraslado(listaTraslado);
    rts.setRetencion(listaRetencion);
    
    if (listaTraslado!=null){
        if (listaTraslado.size()>0){
            imps.setTraslados(trs);
        }
    }
    if (listaRetencion!=null){
        if (listaRetencion.size()>0){
            imps.setRetenciones(rts);
        }
    }
    
    return imps;
  }
    
}
