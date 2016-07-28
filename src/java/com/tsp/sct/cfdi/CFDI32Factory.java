/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.cfdi;

import com.tsp.sct.bo.NominaBancoBO;
import com.tsp.sct.bo.NominaDepartamentoBO;
import com.tsp.sct.bo.NominaPuestoBO;
import com.tsp.sct.bo.NominaRegimenFiscalBO;
import com.tsp.sct.bo.NominaRiesgoPuestoBO;
import com.tsp.sct.bo.UsuarioBO;
import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.dto.Empresa;

import com.tsp.sct.dao.dto.FormaPago;
import com.tsp.sct.dao.dto.NominaBanco;
import com.tsp.sct.dao.dto.NominaDepartamento;
import com.tsp.sct.dao.dto.NominaEmpleado;
import com.tsp.sct.dao.dto.NominaPuesto;
import com.tsp.sct.dao.dto.NominaRegimenFiscal;
import com.tsp.sct.dao.dto.NominaRiesgoPuesto;
import com.tsp.sct.dao.dto.TipoPago;
import com.tsp.sct.dao.dto.Ubicacion;
import com.tsp.sct.dao.dto.VentaMetodoPago;
import com.tsp.sct.util.DateManage;
import static com.tsp.sct.util.DateManage.dateToXMLGregorianCalendar;
import static com.tsp.sct.util.DateManage.dateToXMLGregorianCalendar2;
import com.tsp.sct.util.StringManage;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;

import mx.bigdata.sat.cfdi.schema.v32.Comprobante;
import mx.bigdata.sat.cfdi.schema.v32.Comprobante.Complemento;
import mx.bigdata.sat.cfdi.schema.v32.ObjectFactory;
import mx.bigdata.sat.cfdi.schema.v32.TUbicacion;
import mx.bigdata.sat.cfdi.schema.v32.TUbicacionFiscal;
import mx.bigdata.sat.cfdi.schema.v32.Comprobante.Conceptos;
import mx.bigdata.sat.cfdi.schema.v32.Comprobante.Emisor;
import mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos;
import mx.bigdata.sat.cfdi.schema.v32.Comprobante.Receptor;
import mx.bigdata.sat.cfdi.schema.v32.Comprobante.Conceptos.Concepto;
import mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones;
import mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion;
import mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados;
import mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado;
import mx.bigdata.sat.complementos.schema.implocal.ImpuestosLocales;
import mx.bigdata.sat.complementos.schema.nomina.Nomina;
import mx.bigdata.sat.complementos.schema.nomina.Nomina.Deducciones.Deduccion;
import mx.bigdata.sat.complementos.schema.nomina.Nomina.Percepciones.Percepcion;

/**
 *
 * @author ISCesarMartinez
 */
public class CFDI32Factory {
    
   public static Comprobante createComprobante(FormaPago formaPagoDto, TipoPago tipoPagoDto, 
           Empresa empresaDto, Ubicacion ubicacionDto ,  Cliente clienteDto, 
           ArrayList<Concepto> listaConceptos, ArrayList<Traslado> listaImpuestosTraslado, 
           ArrayList<Retencion> listaImpuestosRetencion, double subtotal, double total,
           String tipoComprobante, String parcialidad) throws Exception {
    ObjectFactory of = new ObjectFactory();
    Comprobante comp = of.createComprobante();
    
    comp.setVersion("3.2");
    //Date date = new GregorianCalendar(2011, 11, 30, 20, 38, 12).getTime();
    Date date = new Date();
    
    comp.setFecha(date);
    comp.setFormaDePago(formaPagoDto.getDescFormaPago());//"PAGO EN UNA SOLA EXHIBICION" , "PARCIALIDADES"
    if (formaPagoDto.getParcialidad()==(short)1 && parcialidad!=null){
        //PARCIALIDADES
        comp.setFormaDePago(parcialidad);//"PARCIALIDAD 1 de X");
    }
    
    //"EFECTIVO" //"CHEQUE" //...
    comp.setMetodoDePago(tipoPagoDto.getClaveMetodoSat());//tipoPagoDto.getDescTipoPago()); 
    if (tipoPagoDto.getNumeroCuenta()!=null){
        if (tipoPagoDto.getNumeroCuenta().trim().length()>=4)
            comp.setNumCtaPago(tipoPagoDto.getNumeroCuenta());
    }
    
    comp.setTipoDeComprobante(tipoComprobante); //ingreso, egreso, traslado
    
    try{
        comp.setLugarExpedicion( ubicacionDto.getPais() + ", " + ubicacionDto.getEstado()); //México, Guanajuato
    }catch(Exception ex){
        comp.setLugarExpedicion("México");
        ex.printStackTrace();
    }
    
    comp.setEmisor(createEmisor(of, empresaDto, ubicacionDto));
    comp.setReceptor(createReceptor(of, clienteDto));
    comp.setConceptos(createConceptos(of, listaConceptos));
    comp.setImpuestos(createImpuestos(of, listaImpuestosTraslado, listaImpuestosRetencion));
    
    comp.setSubTotal(new BigDecimal(subtotal).setScale(2, RoundingMode.HALF_UP));
    comp.setTotal(new BigDecimal(total).setScale(2, RoundingMode.HALF_UP));
    
    return comp;
  }
   
public static Comprobante createComprobante(FormaPago formaPagoDto, TipoPago tipoPagoDto, 
           Empresa empresaDto, Ubicacion ubicacionDto ,  Cliente clienteDto, 
           ArrayList<Concepto> listaConceptos, ArrayList<Traslado> listaImpuestosTraslado, 
           ArrayList<Retencion> listaImpuestosRetencion, double subtotal, double total,
           String tipoComprobante, String parcialidad, ArrayList<ImpuestosLocales.TrasladosLocales> listaTrasladosLocales , ArrayList<ImpuestosLocales.RetencionesLocales> listaRetencionesLocales) throws Exception {
    ObjectFactory of = new ObjectFactory();
    Comprobante comp = of.createComprobante();
    
    comp.setVersion("3.2");
    //Date date = new GregorianCalendar(2011, 11, 30, 20, 38, 12).getTime();
    Date date = new Date();
    
    comp.setFecha(date);
    comp.setFormaDePago(formaPagoDto.getDescFormaPago());//"PAGO EN UNA SOLA EXHIBICION" , "PARCIALIDADES"
    if (formaPagoDto.getParcialidad()==(short)1 && parcialidad!=null){
        //PARCIALIDADES
        comp.setFormaDePago(parcialidad);//"PARCIALIDAD 1 de X");
    }
    
    //"EFECTIVO" //"CHEQUE" //...
    comp.setMetodoDePago(tipoPagoDto.getClaveMetodoSat());//tipoPagoDto.getDescTipoPago()); 
    if (tipoPagoDto.getNumeroCuenta()!=null){
        if (tipoPagoDto.getNumeroCuenta().trim().length()>=4)
            comp.setNumCtaPago(tipoPagoDto.getNumeroCuenta());
    }
    
    comp.setTipoDeComprobante(tipoComprobante); //ingreso, egreso, traslado
    
    try{
        comp.setLugarExpedicion( ubicacionDto.getPais() + ", " + ubicacionDto.getEstado()); //México, Guanajuato
    }catch(Exception ex){
        comp.setLugarExpedicion("México");
        ex.printStackTrace();
    }
    
    comp.setEmisor(createEmisor(of, empresaDto, ubicacionDto));
    comp.setReceptor(createReceptor(of, clienteDto));
    comp.setConceptos(createConceptos(of, listaConceptos));
    comp.setImpuestos(createImpuestos(of, listaImpuestosTraslado, listaImpuestosRetencion));
    
    //*comp.setSubTotal(new BigDecimal(subtotal).setScale(2, RoundingMode.HALF_UP));
    comp.setSubTotal(BigDecimal.valueOf(subtotal).setScale(2, RoundingMode.HALF_UP));
    comp.setTotal(new BigDecimal(total).setScale(2, RoundingMode.HALF_UP));
        
    ////**SI HAY COMPLEMENTO IMPUESTO LOCAL:        
    if(listaTrasladosLocales == null){
        listaTrasladosLocales = new ArrayList<ImpuestosLocales.TrasladosLocales>();
        System.out.println("++++++++++ ENTRO A NEW DE TRASLADOS LOCALES");
    }
    if(listaRetencionesLocales == null){
        listaRetencionesLocales = new ArrayList<ImpuestosLocales.RetencionesLocales>();
        System.out.println("++++++++++ ENTRO A NEW DE RETENCIONES LOCALES");
    }
    
    if(listaRetencionesLocales.size()>0||listaTrasladosLocales.size()>0){
        comp.setComplemento(new Complemento());
        comp.getComplemento().getAny().add(createImpuestosLocales(listaTrasladosLocales, listaRetencionesLocales));
    }
    ////**
    
    return comp;
  }
   
  public static Comprobante createComprobante(FormaPago formaPagoDto, TipoPago tipoPagoDto, 
           Empresa empresaDto, Ubicacion ubicacionDto ,  Cliente clienteDto, 
           ArrayList<Concepto> listaConceptos, ArrayList<Traslado> listaImpuestosTraslado, 
           ArrayList<Retencion> listaImpuestosRetencion, double subtotal, double total,
           String tipoComprobante, String parcialidad, 
           ArrayList<mx.bigdata.sat.complementos.schema.nomina.Nomina.Percepciones.Percepcion> percepciones,
           ArrayList<mx.bigdata.sat.complementos.schema.nomina.Nomina.Deducciones.Deduccion> deducciones,
           NominaEmpleado empleado,
           Date fechaPago2, Date fechaPagoInicial, Date fechaPagoFinal, int numDiasPago,
           String registroPatronal) throws Exception {
    ObjectFactory of = new ObjectFactory();
    Comprobante comp = of.createComprobante();
    
    comp.setVersion("3.2");
    //Date date = new GregorianCalendar(2011, 11, 30, 20, 38, 12).getTime();
    Date date = new Date();
    
    comp.setFecha(date);
    comp.setFormaDePago(formaPagoDto.getDescFormaPago());//"PAGO EN UNA SOLA EXHIBICION" , "PARCIALIDADES"
    if (formaPagoDto.getParcialidad()==(short)1 && parcialidad!=null){
        //PARCIALIDADES
        comp.setFormaDePago(parcialidad);//"PARCIALIDAD 1 de X");
    }
    
    //"EFECTIVO" //"CHEQUE" //...
    comp.setMetodoDePago(tipoPagoDto.getClaveMetodoSat());//tipoPagoDto.getDescTipoPago()); 
    if (tipoPagoDto.getNumeroCuenta()!=null){
        if (tipoPagoDto.getNumeroCuenta().trim().length()>=4)
            comp.setNumCtaPago(tipoPagoDto.getNumeroCuenta());
    }
    
    comp.setTipoDeComprobante(tipoComprobante); //ingreso, egreso, traslado
    
    try{
        comp.setLugarExpedicion( ubicacionDto.getPais() + ", " + ubicacionDto.getEstado()); //México, Guanajuato
    }catch(Exception ex){
        comp.setLugarExpedicion("México");
        ex.printStackTrace();
    }
    
    comp.setEmisor(createEmisor(of, empresaDto, ubicacionDto));
    comp.setReceptor(createReceptor(of, clienteDto));
    comp.setConceptos(createConceptos(of, listaConceptos));
    comp.setImpuestos(createImpuestos(of, listaImpuestosTraslado, listaImpuestosRetencion));
    
    comp.setSubTotal(new BigDecimal(subtotal).setScale(2, RoundingMode.HALF_UP));
    comp.setTotal(new BigDecimal(total).setScale(2, RoundingMode.HALF_UP));
    
    ////***Si hay complemento de nomina:
    if(percepciones == null){
        percepciones = new ArrayList<mx.bigdata.sat.complementos.schema.nomina.Nomina.Percepciones.Percepcion>();
    }
    if(deducciones == null){
        deducciones = new ArrayList<mx.bigdata.sat.complementos.schema.nomina.Nomina.Deducciones.Deduccion>();
    }
    //if(deducciones.size() >0 || percepciones.size() >0){
        comp.setComplemento(new Complemento());
        comp.getComplemento().getAny().add(createNomina(empleado,registroPatronal, percepciones, deducciones, fechaPago2, fechaPagoInicial, fechaPagoFinal, numDiasPago));
    //}
    
    ////***
    
    return comp;
  }
    
  private static Emisor createEmisor(ObjectFactory of, Empresa empresaDto, Ubicacion ubicacionDto) throws Exception {
    Emisor emisor = of.createComprobanteEmisor();
    emisor.setNombre(empresaDto.getRazonSocial());//"PHARMA PLUS SA DE CV");
    emisor.setRfc(empresaDto.getRfc());//"PNU111201U31");
    
    TUbicacionFiscal uf = of.createTUbicacionFiscal();
    uf.setCalle(ubicacionDto.getCalle());//"AV. RIO MIXCOAC");
    uf.setCodigoPostal(ubicacionDto.getCodigoPostal());//"03240");
    uf.setColonia(ubicacionDto.getColonia() !=null?(ubicacionDto.getColonia().trim().length()>0?ubicacionDto.getColonia().trim():null): null );//"ACACIAS"); 
    uf.setEstado(ubicacionDto.getEstado());//"DISTRITO FEDERAL"); 
    uf.setMunicipio(ubicacionDto.getMunicipio());//"BENITO JUAREZ"); 
    uf.setNoExterior(ubicacionDto.getNumExt() !=null?(ubicacionDto.getNumExt().trim().length()>0?ubicacionDto.getNumExt().trim():null): null);//"No. 140"); 
    //uf.setNoInterior("".equals(ubicacionDto.getNumInt())?null:ubicacionDto.getNumInt());
    uf.setNoInterior(ubicacionDto.getNumInt() !=null?(ubicacionDto.getNumInt().trim().length()>0?ubicacionDto.getNumInt().trim():null): null);
    uf.setPais(ubicacionDto.getPais());//"México");
    emisor.setDomicilioFiscal(uf);
    
    //Regimen Fiscal del Emisor
    if (empresaDto.getRegimenFiscal()==null)
        throw new Exception("El Emisor no tiene asignado un regimen fiscal en la base de datos.");
    if (empresaDto.getRegimenFiscal().trim().length()<=0 || empresaDto.getRegimenFiscal().trim().equals("null"))
        throw new Exception("El Emisor no tiene asignado un regimen fiscal en la base de datos.");
    
    List<Emisor.RegimenFiscal> listaRegimenFiscal = new ArrayList<Emisor.RegimenFiscal>();
    Emisor.RegimenFiscal regimenFiscal = new Emisor.RegimenFiscal();
    regimenFiscal.setRegimen(empresaDto.getRegimenFiscal());
    listaRegimenFiscal.add(regimenFiscal);
    
    emisor.setRegimenFiscal(listaRegimenFiscal);
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
    uf.setCalle(clienteDto.getCalle()!=null?(clienteDto.getCalle().trim().length()>0?clienteDto.getCalle().trim():null): null);//"AV UNIVERSIDAD");
    uf.setCodigoPostal(clienteDto.getCodigoPostal()!=null?(clienteDto.getCodigoPostal().trim().length()>0?clienteDto.getCodigoPostal().trim():null): null );//"04360");
    uf.setColonia(clienteDto.getColonia() !=null?(clienteDto.getColonia().trim().length()>0?clienteDto.getColonia().trim():null): null );//"COPILCO UNIVERSIDAD"); 
    uf.setEstado(clienteDto.getEstado() !=null?(clienteDto.getEstado().trim().length()>0?clienteDto.getEstado().trim():null): null);//"DISTRITO FEDERAL"); 
    uf.setMunicipio(clienteDto.getMunicipio() !=null?(clienteDto.getMunicipio().trim().length()>0?clienteDto.getMunicipio().trim():null): null);//"COYOACAN"); 
    uf.setNoExterior(clienteDto.getNumero() !=null?(clienteDto.getNumero().trim().length()>0?clienteDto.getNumero().trim():null): null);//"16 EDF 3"); 
    //uf.setNoInterior("".equals(clienteDto.getNumeroInterior())?null:clienteDto.getNumeroInterior() );//"DPTO 101");
    uf.setNoInterior(clienteDto.getNumeroInterior() !=null?(clienteDto.getNumeroInterior().trim().length()>0?clienteDto.getNumeroInterior().trim():null): null);//"DPTO 101");
    uf.setPais(clienteDto.getPais()); 
    if(clienteDto.getReferencia() != null && !clienteDto.getReferencia().trim().equals(""))
        uf.setReferencia(clienteDto.getReferencia());
    receptor.setDomicilio(uf);
    
    return receptor;
  }

  private static Conceptos createConceptos(ObjectFactory of, List<Concepto> listaConcepto) throws Exception {
    Conceptos cps = of.createComprobanteConceptos();
    
    if (listaConcepto.size()<=0)
        throw new Exception("La lista de conceptos esta vacía. Un CFDI 3.2 no puede ser generado con esta incidencia.");
    
    cps.setConcepto(listaConcepto);
    
    return cps;
  }

  private static Impuestos createImpuestos(ObjectFactory of, List<Traslado> listaTraslado, List<Retencion> listaRetencion) {
    Impuestos imps = of.createComprobanteImpuestos();
    Traslados trs = of.createComprobanteImpuestosTraslados();
    Retenciones rts = of.createComprobanteImpuestosRetenciones();
   
    trs.setTraslado(listaTraslado);
    rts.setRetencion(listaRetencion);
    
    //*OBTENEMOS LOS TOTALES DE RETENCIONES Y DE TRASLADOS:
        
    BigDecimal totalTrasla = BigDecimal.ZERO;
    if(listaTraslado != null){
        for(Traslado traslados : listaTraslado){
            System.out.println("_________TRASLADO IMPORTE: "+traslados.getImporte());
            totalTrasla = totalTrasla.add(traslados.getImporte());
            System.out.println("_________SUBTOTAL TRASLADO: "+totalTrasla);
        }
    }
    BigDecimal totalReten = BigDecimal.ZERO;
    if(listaRetencion != null){
        for(Retencion retenciones : listaRetencion){
            System.out.println("_________RENTENCION IMPORTE: "+retenciones.getImporte());    
            totalReten = totalReten.add(retenciones.getImporte());
            System.out.println("_________SUBTOTAL RETENCION: "+totalReten);
        }
    }
    totalTrasla = (totalTrasla.setScale(2, RoundingMode.HALF_UP));
    totalReten = (totalReten.setScale(2, RoundingMode.HALF_UP));
    
    System.out.println("_________TOTALES DE TRASLADOS: "+totalTrasla);
    System.out.println("_________TOTALES DE RETENIDOS: "+totalReten);
        
    if(totalTrasla.floatValue() > 0)
        imps.setTotalImpuestosTrasladados(totalTrasla);
        
    if(totalReten.floatValue() > 0)
        imps.setTotalImpuestosRetenidos(totalReten);
    //*
    
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
    
  private static mx.bigdata.sat.complementos.schema.nomina.Nomina createNomina(NominaEmpleado empleado, String registroPatronal, List<mx.bigdata.sat.complementos.schema.nomina.Nomina.Percepciones.Percepcion> percepciones, List<mx.bigdata.sat.complementos.schema.nomina.Nomina.Deducciones.Deduccion> deducciones,
          Date fechaPago2, Date fechaPagoInicial, Date fechaPagoFinal, int numDiasPago){
      
      UsuarioBO ubo = new UsuarioBO();
      NominaEmpleado nominaEmpleado = (NominaEmpleado)empleado;
      NominaRegimenFiscal nrf = new NominaRegimenFiscalBO(nominaEmpleado.getIdNominaRegimenFiscal(), ubo.getConn()).getNominaRegimenFiscal();
      NominaDepartamento nd = new NominaDepartamentoBO(nominaEmpleado.getIdDepartamento(), ubo.getConn()).getNominaDepartamento();
      NominaBanco nb = new NominaBancoBO(nominaEmpleado.getIdNominaBanco(), ubo.getConn()).getNominaBanco();
      NominaPuesto np = new NominaPuestoBO(nominaEmpleado.getIdPuesto(), ubo.getConn()).getNominaPuesto();
      NominaRiesgoPuesto nrp = new NominaRiesgoPuestoBO(nominaEmpleado.getIdRiesgoPuesto(), ubo.getConn()).getNominaRiesgoPuesto();
                  
      mx.bigdata.sat.complementos.schema.nomina.ObjectFactory of = new mx.bigdata.sat.complementos.schema.nomina.ObjectFactory();
      Nomina nomina = of.createNomina();
      
      nomina.setVersion("1.1");
      if(!StringManage.getValidString(registroPatronal).equals(""))
        nomina.setRegistroPatronal(StringManage.getValidString(registroPatronal));
      nomina.setNumEmpleado(nominaEmpleado.getNumEmpleado());
      nomina.setCURP(nominaEmpleado.getCurp());
      nomina.setTipoRegimen(nrf.getClave());
      if(!nominaEmpleado.getNumSeguroSocial().trim().equals(""))
        nomina.setNumSeguridadSocial(nominaEmpleado.getNumSeguroSocial());
      try{
        nomina.setFechaPago(dateToXMLGregorianCalendar2(fechaPago2));
        nomina.setFechaInicialPago(dateToXMLGregorianCalendar2(fechaPagoInicial));
        nomina.setFechaFinalPago(dateToXMLGregorianCalendar2(fechaPagoFinal));
        nomina.setNumDiasPagados(new BigDecimal(numDiasPago).setScale(2, RoundingMode.HALF_UP));
      }catch(Exception e){e.printStackTrace();}
      if(nominaEmpleado.getIdDepartamento() > 0)
          nomina.setDepartamento(nd.getNombre());
      if(!nominaEmpleado.getClabe().trim().equals(""))
          nomina.setCLABE(new BigInteger(nominaEmpleado.getClabe()));
      
      if(nominaEmpleado.getIdNominaBanco() > 0){
          try{
            nomina.setBanco(Integer.parseInt(nb.getClave()));
              //nomina.setBanco(619);
          }catch(Exception e){}
      }
      if(nominaEmpleado.getFechaInicioRelacionLaboral() != null){
          try {
              nomina.setFechaInicioRelLaboral(dateToXMLGregorianCalendar2(nominaEmpleado.getFechaInicioRelacionLaboral()));
            } catch (DatatypeConfigurationException ex) {ex.printStackTrace();}
      }
      //CALCULAR EL NUMERO DE SEMANA QUE HA MANTENIDO RELACION LABORAL CON EL EMPLEADOR
      //if()
      if(nominaEmpleado.getIdPuesto() > 0)
          nomina.setPuesto(np.getNombre());
      if(!nominaEmpleado.getTipoContrato().trim().equals("") && ! nominaEmpleado.getTipoContrato().trim().equals("-1"))
          nomina.setTipoContrato(nominaEmpleado.getTipoContrato());
      if(!nominaEmpleado.getTipoJornada().trim().equals("") && !nominaEmpleado.getTipoJornada().trim().equals("-1"))
          nomina.setTipoJornada(nominaEmpleado.getTipoJornada().trim());
      nomina.setPeriodicidadPago(nominaEmpleado.getPeriodicidadPago());
      if(nominaEmpleado.getSalarioBaseCotApor() > 0)
          nomina.setSalarioBaseCotApor(new BigDecimal(nominaEmpleado.getSalarioBaseCotApor()).setScale(2, RoundingMode.HALF_UP));
      if(nominaEmpleado.getIdRiesgoPuesto() > 0)
          nomina.setRiesgoPuesto(Integer.parseInt(nrp.getDescripcion()));
      if(nominaEmpleado.getSalarioDiarioIntegrado() > 0)
          nomina.setSalarioDiarioIntegrado(new BigDecimal(nominaEmpleado.getSalarioDiarioIntegrado()).setScale(2, RoundingMode.HALF_UP));
      
      if(percepciones != null){
          if(percepciones.size()>0){
            Nomina.Percepciones nPercepciones = of.createNominaPercepciones();

            double totalExentoPercepciones = 0;
            double totalAgravadoPercepciones = 0;
            for(Percepcion perce : percepciones){
                Nomina.Percepciones.Percepcion npp = of.createNominaPercepcionesPercepcion();

                //npp = perce;
                totalExentoPercepciones += perce.getImporteExento().doubleValue();
                totalAgravadoPercepciones += perce.getImporteGravado().doubleValue();
                npp.setTipoPercepcion(perce.getTipoPercepcion());
                npp.setClave(perce.getClave());
                npp.setConcepto(perce.getConcepto());
                npp.setImporteGravado(perce.getImporteGravado());
                npp.setImporteExento(perce.getImporteExento());    
                nPercepciones.getPercepcion().add(npp);
                //nomina.getPercepciones().getPercepcion().add(npp);
            }


            //nomina.getPercepciones().setTotalExento(new BigDecimal(totalExentoPercepciones));
            //nomina.getPercepciones().setTotalGravado(new BigDecimal(totalAgravadoPercepciones));
            nPercepciones.setTotalExento(new BigDecimal(totalExentoPercepciones).setScale(2, RoundingMode.HALF_UP));
            nPercepciones.setTotalGravado(new BigDecimal(totalAgravadoPercepciones).setScale(2, RoundingMode.HALF_UP));
            nomina.setPercepciones(nPercepciones);
          }
      }
      
      if(deducciones != null){
          if(deducciones.size()>0){
            Nomina.Deducciones nDeducciones = of.createNominaDeducciones();

            double totalExentoDeducciones = 0;
            double totalAgravadoDeducciones = 0;      
            for(Deduccion deduc : deducciones){
                Nomina.Deducciones.Deduccion ndd = of.createNominaDeduccionesDeduccion();
                totalExentoDeducciones += deduc.getImporteExento().doubleValue();
                totalAgravadoDeducciones += deduc.getImporteGravado().doubleValue();
                ndd.setTipoDeduccion(deduc.getTipoDeduccion());
                ndd.setClave(deduc.getClave());
                ndd.setConcepto(deduc.getConcepto());
                ndd.setImporteGravado(deduc.getImporteGravado());
                ndd.setImporteExento(deduc.getImporteExento());
                nDeducciones.getDeduccion().add(ndd);
                //nomina.getDeducciones().getDeduccion().add(ndd);
            }

            nDeducciones.setTotalExento(new BigDecimal(totalExentoDeducciones).setScale(2, RoundingMode.HALF_UP));
            nDeducciones.setTotalGravado(new BigDecimal(totalAgravadoDeducciones).setScale(2, RoundingMode.HALF_UP));
            nomina.setDeducciones(nDeducciones);
            //nomina.getDeducciones().setTotalExento(new BigDecimal(totalExentoDeducciones));
            //nomina.getDeducciones().setTotalGravado(new BigDecimal(totalAgravadoDeducciones));
          }
      }
      return nomina;
      
  }
  
  private static ImpuestosLocales createImpuestosLocales(List<ImpuestosLocales.TrasladosLocales> listaTraslado, List<ImpuestosLocales.RetencionesLocales> listaRetencion) {
    mx.bigdata.sat.complementos.schema.implocal.ObjectFactory of = new mx.bigdata.sat.complementos.schema.implocal.ObjectFactory();
    ImpuestosLocales locales = of.createImpuestosLocales();    
    
    locales.setVersion("1.0");
    //BigDecimal tras = BigDecimal.ZERO;
    //BigDecimal rete = BigDecimal.ZERO;
    double tras = 0;
    double rete = 0;
    for(ImpuestosLocales.TrasladosLocales trasLo:listaTraslado){
        ImpuestosLocales.TrasladosLocales trs= of.createImpuestosLocalesTrasladosLocales();
        trs.setImpLocTrasladado(trasLo.getImpLocTrasladado());
        trs.setImporte(trasLo.getImporte());
        trs.setTasadeTraslado(trasLo.getTasadeTraslado());
        locales.getTrasladosLocales().add(trs);
        System.out.println("***MONTO IMPUESTO TRASLADADO: "+trasLo.getImporte());
        //tras.add(trasLo.getImporte());//para el monto total
        tras += trasLo.getImporte().doubleValue();
        //locales.setTotaldeTraslados(tras);
    }
    for(ImpuestosLocales.RetencionesLocales restLo:listaRetencion){
        ImpuestosLocales.RetencionesLocales rts = of.createImpuestosLocalesRetencionesLocales();
        rts.setImpLocRetenido(restLo.getImpLocRetenido());
        rts.setImporte(restLo.getImporte());
        rts.setTasadeRetencion(restLo.getTasadeRetencion());
        locales.getRetencionesLocales().add(rts);
        System.out.println("***MONTO IMPUESTO RETENIDO: "+restLo.getImporte());        
        //rete.add(restLo.getImporte());//para el monto total
        rete += restLo.getImporte().doubleValue();
        //locales.setTotaldeRetenciones(rete);
    }
    //total de traslados y retenidos locales.
    System.out.println("---TOTAL DE LOCALES RETENIDOS antes: "+rete);
    System.out.println("---TOTAL DE LOCALES TRASLADADOS antes: "+tras);
    
    locales.setTotaldeRetenciones(new BigDecimal(rete).setScale(2, RoundingMode.HALF_UP));
    locales.setTotaldeTraslados(new BigDecimal(tras).setScale(2, RoundingMode.HALF_UP));
    System.out.println("---TOTAL DE LOCALES RETENIDOS: "+locales.getTotaldeRetenciones());
    System.out.println("---TOTAL DE LOCALES TRASLADADOS: "+locales.getTotaldeTraslados());
    
    return locales;
  }
  
}
