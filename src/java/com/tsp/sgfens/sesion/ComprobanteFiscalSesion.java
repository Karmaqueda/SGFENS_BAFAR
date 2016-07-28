/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.sesion;

import com.tsp.sct.bo.ComprobanteDescripcionPersonalizadaBO;
import com.tsp.sct.bo.ConceptoBO;
import com.tsp.sct.bo.ImpuestoPorConceptoBO;
import com.tsp.sct.bo.ServicioBO;
import com.tsp.sct.bo.UsuarioBO;
import com.tsp.sct.dao.dto.Impuesto;
import com.tsp.sct.dao.dto.ImpuestoPorConcepto;
import com.tsp.sct.dao.dto.Servicio;
import com.tsp.sct.dao.dto.TipoPago;
import com.tsp.sct.dao.jdbc.ImpuestoDaoImpl;
import com.tsp.sct.util.DateManage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mx.bigdata.sat.cfdi.schema.v32.Comprobante;
import mx.bigdata.sat.complementos.schema.implocal.ImpuestosLocales;
import mx.bigdata.sat.complementos.schema.nomina.Nomina;

/**
 *
 * @author ISCesarMartinez
 */
public class ComprobanteFiscalSesion extends FormatoSesion {
    
    private int idComprobanteFiscal = -1;
    
    /*
     * Tipo Comprobante: ver contenido de tabla tipo_comprobante
     * para conocer los posibles valores
     *      2 :  Factura  (por defecto)
     *      5 :  Nota Crédito
     *      6 :  Nota Débito
     */
    private int id_tipo_comprobante = 2;
    
    /**
     * Estatus de Comprobante Fiscal: ver contenido de tabla estatus_comprobante
     * para conocer los valores posibles
     *      1 : Nueva (solo en sesion)
     *      2 : Cotizacion (usado solo en SCT)
     *      3 : Entregada (una vez generada y almacenada)
     *      4 : Cancelada (se cambia el estatus de 3 a 4 para cancelar)
     *      5 : Cancelacion (por cada cancelada, estatus 4, se debe insertar un registro con estatus 5)
     */
    private int id_estatus=1;
    
    /**
     * Relación con tabla "folios"
     */
    private int id_folio=0;
    /**
     * Siguiente folio en la serie, determinado por el registro de la tabla "folios"
     * Debe tener formato 0000
     */
    private String folio_generado = "";
    
    /**
     * Forma de Pago:
     *      1 = Pago en una sola exhibicion
     *      2 = Parcialidades
     */
    private int id_forma_pago = 1;
    /**
     * En caso de forma pago Parcialidades, debe expresarse un valor
     * con el formato: n/x . P. ej:  2/10 , que expresa: parcialidad 2 de 10
     */
    private String parcialidad = "";
    
    private TipoPago tipo_pago = new TipoPago();
    
    private Date fecha_pago = null;
    
    /**
     * Nombre del archivo cfd, incluyendo extension
     * Sin ruta, solo el nombre del archivo
     */
    private String archivo_cfd = "";
    
    private String cadena_original = "";
    
    /**
     * Sello digital de emisor
     */
    private String sello_digital ="";
    
    /**
     * Tipo Moneda: ver contenido de tabla tipo_de_moneda
     *      -1 : MXN   - PESOS
     *       1 : USD   - DOLARES
     *       2 : EUR   - EUROS
     *       3 : JPY   - YENS
     */
    private int tipo_moneda_int = -1;
    private double tipo_cambio = 1;
    
    private String uuid = "";
    
    private String condicionesPagoDescripcion = "";
    
    /**
     * Sello digital timbre PAC
     */
    private String sello_sat = "";
    
    private double descuento_importe = 0;
    
    /**
     * ID del Pedido a facturar
     */
    private int idPedido = -1;
    
    private String noCertificadoSAT = "";
    
    private Date fechaTimbrado = null;
    
    private int guardarEnImpuestosXconcepto = 0 ;
    
    private List<ComprobanteDescripcionPersonalizadaBO.CampoPersonalizado>
            listaCamposPersonalizados =  new ArrayList<ComprobanteDescripcionPersonalizadaBO.CampoPersonalizado>();

    public int getGuardarEnImpuestosXconcepto() {
        return guardarEnImpuestosXconcepto;
    }

    public void setGuardarEnImpuestosXconcepto(int guardarEnImpuestosXconcepto) {
        this.guardarEnImpuestosXconcepto = guardarEnImpuestosXconcepto;
    }
    
    public String getNoCertificadoSAT() {
        return noCertificadoSAT;
    }

    public void setNoCertificadoSAT(String noCertificadoSAT) {
        this.noCertificadoSAT = noCertificadoSAT;
    }

    public Date getFechaTimbrado() {
        return fechaTimbrado;
    }

    public void setFechaTimbrado(Date fechaTimbrado) {
        this.fechaTimbrado = fechaTimbrado;
    }    

    public ComprobanteFiscalSesion() {
        super();
    }

    public String getArchivo_cfd() {
        return archivo_cfd;
    }

    public void setArchivo_cfd(String archivo_cfd) {
        this.archivo_cfd = archivo_cfd;
    }

    public String getCadena_original() {
        return cadena_original;
    }

    public void setCadena_original(String cadena_original) {
        this.cadena_original = cadena_original;
    }

    public Date getFecha_pago() {
        return fecha_pago;
    }

    public void setFecha_pago(Date fecha_pago) {
        this.fecha_pago = fecha_pago;
    }

    public String getFolio_generado() {
        return folio_generado;
    }

    public void setFolio_generado(String folio_generado) {
        this.folio_generado = folio_generado;
    }

    public int getIdComprobanteFiscal() {
        return idComprobanteFiscal;
    }

    public void setIdComprobanteFiscal(int idComprobanteFiscal) {
        this.idComprobanteFiscal = idComprobanteFiscal;
    }

    public int getId_estatus() {
        return id_estatus;
    }

    public void setId_estatus(int id_estatus) {
        this.id_estatus = id_estatus;
    }

    public int getId_folio() {
        return id_folio;
    }

    public void setId_folio(int id_folio) {
        this.id_folio = id_folio;
    }

    public int getId_forma_pago() {
        return id_forma_pago;
    }

    public void setId_forma_pago(int id_forma_pago) {
        this.id_forma_pago = id_forma_pago;
    }

    public int getId_tipo_comprobante() {
        return id_tipo_comprobante;
    }

    public void setId_tipo_comprobante(int id_tipo_comprobante) {
        this.id_tipo_comprobante = id_tipo_comprobante;
    }

    public String getParcialidad() {
        return parcialidad;
    }

    public void setParcialidad(String parcialidad) {
        this.parcialidad = parcialidad;
    }

    public String getSello_digital() {
        return sello_digital;
    }

    public void setSello_digital(String sello_digital) {
        this.sello_digital = sello_digital;
    }

    public String getSello_sat() {
        return sello_sat;
    }

    public void setSello_sat(String sello_sat) {
        this.sello_sat = sello_sat;
    }

    public double getTipo_cambio() {
        return tipo_cambio;
    }

    public void setTipo_cambio(double tipo_cambio) {
        this.tipo_cambio = tipo_cambio;
    }

    public int getTipo_moneda_int() {
        return tipo_moneda_int;
    }

    public void setTipo_moneda_int(int tipo_moneda_int) {
        this.tipo_moneda_int = tipo_moneda_int;
    }

    public TipoPago getTipo_pago() {
        return tipo_pago;
    }

    public void setTipo_pago(TipoPago tipo_pago) {
        this.tipo_pago = tipo_pago;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public double getDescuento_importe() {
        return descuento_importe;
    }

    public void setDescuento_importe(double descuento_importe) {
        this.descuento_importe = descuento_importe;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getCondicionesPagoDescripcion() {
        return condicionesPagoDescripcion;
    }

    public void setCondicionesPagoDescripcion(String condicionesPagoDescripcion) {
        this.condicionesPagoDescripcion = condicionesPagoDescripcion;
    }
        
    @Override
    public BigDecimal getDescuentoImporte(){
        BigDecimal descuentoImporte = new BigDecimal(this.descuento_importe).setScale(2, RoundingMode.HALF_UP);
        return descuentoImporte;
    }

    public List<ComprobanteDescripcionPersonalizadaBO.CampoPersonalizado> getListaCamposPersonalizados() {
        if (listaCamposPersonalizados==null)
            listaCamposPersonalizados =  new ArrayList<ComprobanteDescripcionPersonalizadaBO.CampoPersonalizado>();
        return listaCamposPersonalizados;
    }

    public void setListaCamposPersonalizados(List<ComprobanteDescripcionPersonalizadaBO.CampoPersonalizado> listaCamposPersonalizados) {
        this.listaCamposPersonalizados = listaCamposPersonalizados;
    }

    
    public ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Conceptos.Concepto> getSchemaConceptos() throws Exception {
        ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Conceptos.Concepto> listConceptos = new ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Conceptos.Concepto>();
        
        mx.bigdata.sat.cfdi.schema.v32.Comprobante.Conceptos.Concepto conceptoComprobante;
        
        //Productos -> Conceptos
        ConceptoBO conceptoBO = new ConceptoBO(getConn());
        com.tsp.sct.dao.dto.Concepto  conceptoDto;
        for (ProductoSesion itemProducto:this.getListaProducto()){
            conceptoComprobante = new mx.bigdata.sat.cfdi.schema.v32.Comprobante.Conceptos.Concepto();
            
            conceptoDto = conceptoBO.findConceptobyId(itemProducto.getIdProducto());
            
            conceptoComprobante.setCantidad(new BigDecimal(itemProducto.getCantidad()).setScale(2, RoundingMode.HALF_UP));
            //conceptoComprobante.setComplementoConcepto(null);
            //conceptoComprobante.setCuentaPredial(null);
            conceptoComprobante.setDescripcion( itemProducto.getDescripcionAlternativa()!=null?itemProducto.getDescripcionAlternativa():conceptoDto.getDescripcion());
            conceptoComprobante.setImporte(new BigDecimal(itemProducto.getMonto()).setScale(2, RoundingMode.HALF_UP));
            if (conceptoDto.getIdentificacion()!=null){
                if (conceptoDto.getIdentificacion().trim().length()>0)
                    conceptoComprobante.setNoIdentificacion(conceptoDto.getIdentificacion());
            }
            conceptoComprobante.setUnidad(itemProducto.getUnidad());
            //conceptoComprobante.setValorUnitario(new BigDecimal(itemProducto.getPrecio()).setScale(2, RoundingMode.HALF_UP));
            conceptoComprobante.setValorUnitario(BigDecimal.valueOf(itemProducto.getPrecio()).subtract( (BigDecimal.valueOf(itemProducto.getDescuento_tasa()).multiply(BigDecimal.valueOf(0.01).multiply(BigDecimal.valueOf(itemProducto.getPrecio()))) )).setScale(2, RoundingMode.HALF_UP));

            ////***---Carga de informacion aduanera del concepto
            
            mx.bigdata.sat.cfdi.schema.v32.TInformacionAduanera infoAduana = null;            
            for(com.tsp.sct.dao.dto.Aduana aduana : itemProducto.getAduanas()){
                infoAduana = new mx.bigdata.sat.cfdi.schema.v32.TInformacionAduanera();
                infoAduana.setAduana(aduana.getAduana());
                infoAduana.setNumero(aduana.getNumDocumento());
                infoAduana.setFecha(DateManage.dateToXMLGregorianCalendar2(aduana.getFechaExpedicion()));
                conceptoComprobante.getInformacionAduanera().add(infoAduana);
            }
            
            ////***---
            listConceptos.add(conceptoComprobante);
        }
        
        //Servicios -> Conceptos
        ServicioBO servicioBO = new ServicioBO(getConn());
        Servicio  servicioDto;
        for (ServicioSesion itemServicio:this.getListaServicio()){
            conceptoComprobante = new mx.bigdata.sat.cfdi.schema.v32.Comprobante.Conceptos.Concepto();
            
            servicioDto = servicioBO.findServiciobyId(itemServicio.getIdServicio());
            
            conceptoComprobante.setCantidad(new BigDecimal(itemServicio.getCantidad()).setScale(2, RoundingMode.HALF_UP));
            //conceptoComprobante.setComplementoConcepto(null);
            //conceptoComprobante.setCuentaPredial(null);
            conceptoComprobante.setDescripcion(servicioDto.getDescripcion());
            conceptoComprobante.setImporte(new BigDecimal(itemServicio.getMonto()).setScale(2, RoundingMode.HALF_UP));
            if (servicioDto.getSku()!=null){
                if (servicioDto.getSku().trim().length()>0)
                    conceptoComprobante.setNoIdentificacion(servicioDto.getSku());
            }
            conceptoComprobante.setUnidad(itemServicio.getUnidad());
            conceptoComprobante.setValorUnitario(new BigDecimal(itemServicio.getPrecio()).setScale(2, RoundingMode.HALF_UP));
            
            listConceptos.add(conceptoComprobante);
        }
        
        return listConceptos;
    }
    
    public ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado> getSchemaImpuestosTraslados() throws Exception {
        ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado> listaTraslados = null;
        
        mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado trasladoSchema;
        int i=0;
        for (ImpuestoSesion itemImpuesto:this.getListaImpuesto()){
            
            if (itemImpuesto.isTrasladado()){
                BigDecimal bigDec = this.getMontoImpuesto(i);
                trasladoSchema = new  mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado();
                trasladoSchema.setImporte(bigDec.setScale(2, RoundingMode.HALF_UP));
                trasladoSchema.setImpuesto(itemImpuesto.getNombre());
                trasladoSchema.setTasa(new BigDecimal(itemImpuesto.getPorcentaje()).setScale(2, RoundingMode.HALF_UP));
                
                if (listaTraslados==null)
                    listaTraslados = new ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado>();
                
                listaTraslados.add(trasladoSchema);
            }
            
            i++;
        }
        
        return listaTraslados;
    }
    
    public ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado> getSchemaImpuestosTrasladosRetenidos2() throws Exception {
        //ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado> listaTraslados = null;
        //FEDERALES
        mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion retencionSchema;
        mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado trasladoSchema;
        mx.bigdata.sat.complementos.schema.implocal.ImpuestosLocales.TrasladosLocales trasladosLocales;
        mx.bigdata.sat.complementos.schema.implocal.ImpuestosLocales.RetencionesLocales retencionesLocales;
        
        
        int i=0;
        for (ImpuestoSesion itemImpuesto:this.getListaImpuesto()){
            
            /*if (itemImpuesto.isTrasladado()){
                BigDecimal bigDec = this.getMontoImpuesto(i);
                trasladoSchema = new  mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado();
                trasladoSchema.setImporte(bigDec.setScale(2, RoundingMode.HALF_UP));
                trasladoSchema.setImpuesto(itemImpuesto.getNombre());
                trasladoSchema.setTasa(new BigDecimal(itemImpuesto.getPorcentaje()).setScale(2, RoundingMode.HALF_UP));
                
                if (listaTraslados==null)
                    listaTraslados = new ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado>();
                
                listaTraslados.add(trasladoSchema);
            }*/
            
            
            
            if (!itemImpuesto.isTrasladado()){
                if(!itemImpuesto.isImplocal()){//SI ES UN IMPUESTO FEDERAL
                    retencionSchema = new  mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion();
                    BigDecimal bigDec = this.getMontoImpuesto(i);
                    retencionSchema.setImporte(bigDec.setScale(2, RoundingMode.HALF_UP));
                    //retencionSchema.setImporte(new BigDecimal(itemImpuesto.getMonto()).setScale(2, RoundingMode.HALF_UP));
                    retencionSchema.setImpuesto(itemImpuesto.getNombre());

                    if (listaRetenciones==null)
                        listaRetenciones = new ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion>();

                    listaRetenciones.add(retencionSchema);
                }else{//SI ES UN IMPUESTO LOCAL
                    BigDecimal bigDec = this.getMontoImpuesto(i);
                    retencionesLocales = new ImpuestosLocales.RetencionesLocales();
                    retencionesLocales.setImporte(bigDec.setScale(2, RoundingMode.HALF_UP));
                    //retencionesLocales.setImporte(new BigDecimal(itemImpuesto.getMonto()).setScale(2, RoundingMode.HALF_UP));
                    retencionesLocales.setImpLocRetenido(itemImpuesto.getNombre());
                    retencionesLocales.setTasadeRetencion(new BigDecimal(itemImpuesto.getPorcentaje()).setScale(2, RoundingMode.HALF_UP));
                    
                    if(listaRetencionesLocales == null)
                        listaRetencionesLocales = new ArrayList<ImpuestosLocales.RetencionesLocales>();
                    
                    listaRetencionesLocales.add(retencionesLocales);
                }
            }else if (itemImpuesto.isTrasladado()){
                if(!itemImpuesto.isImplocal()){//SI ES UN IMPUESTO FEDERAL
                        BigDecimal bigDec = this.getMontoImpuesto(i);
                        //trasladoSchema = new  mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado();                        
                    trasladoSchema = new  mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado(); 
                    trasladoSchema.setImporte(bigDec.setScale(2, RoundingMode.HALF_UP));
                    //trasladoSchema.setImporte(new BigDecimal(itemImpuesto.getMonto()).setScale(2, RoundingMode.HALF_UP));
                    trasladoSchema.setImpuesto(itemImpuesto.getNombre());
                    trasladoSchema.setTasa(new BigDecimal(itemImpuesto.getPorcentaje()).setScale(2, RoundingMode.HALF_UP));

                    if (listaTraslados==null)
                        listaTraslados = new ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado>();

                    listaTraslados.add(trasladoSchema);
                }else{//SI ES UN IMPUESTO LOCAL
                    trasladosLocales = new ImpuestosLocales.TrasladosLocales();
                    BigDecimal bigDec = this.getMontoImpuesto(i);
                    trasladosLocales.setImporte(bigDec.setScale(2, RoundingMode.HALF_UP));
                    //trasladosLocales.setImporte(new BigDecimal(itemImpuesto.getMonto()).setScale(2, RoundingMode.HALF_UP));
                    trasladosLocales.setImpLocTrasladado(itemImpuesto.getNombre());
                    trasladosLocales.setTasadeTraslado(new BigDecimal(itemImpuesto.getPorcentaje()).setScale(2, RoundingMode.HALF_UP));
                    
                    if(listaTrasladosLocales == null)
                        listaTrasladosLocales = new ArrayList<ImpuestosLocales.TrasladosLocales>();
                    
                    listaTrasladosLocales.add(trasladosLocales);                    
                }
            }
            
            
            
            
            
            i++;
        }
        
        return listaTraslados;
    }
    
    
    public ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion> getSchemaImpuestosRetenidos() throws Exception {
        ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion> listaRetenciones = null;
        
        mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion retencionSchema;
        int i=0;
        for (ImpuestoSesion itemImpuesto:this.getListaImpuesto()){
            
            if (!itemImpuesto.isTrasladado()){
                retencionSchema = new  mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion();
                retencionSchema.setImporte(this.getMontoImpuesto(i));
                retencionSchema.setImpuesto(itemImpuesto.getNombre());
                
                if (listaRetenciones==null)
                    listaRetenciones = new ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion>();
                
                listaRetenciones.add(retencionSchema);
            }
            
            i++;
        }
        
        return listaRetenciones;
    }
    
    public void getSchemaImpuestosRetenidosTrasladadosFederales() throws Exception {
        
        //FEDERALES
        mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion retencionSchema;
        mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado trasladoSchema;
        mx.bigdata.sat.complementos.schema.implocal.ImpuestosLocales.TrasladosLocales trasladosLocales;
        mx.bigdata.sat.complementos.schema.implocal.ImpuestosLocales.RetencionesLocales retencionesLocales;
        
        //LOCALES (LOS LOCALES YA ESTAN MAPEADOS DENTRO DEL SCHEMA; CLASE CFDI32Factory.java EN EL METODO createImpuestosLocales(...) ).
        
        
        int i=0;
        
        ////*RECUPERAMOS LOS IMPUESTOS DE CADA CONCEPTO
        UsuarioBO usuario = new UsuarioBO();
        //Nueva lista de impuesto para mapearlos
        //ArrayList<ImpuestoSesion> impList = new ArrayList<ImpuestoSesion>();
        //variables para la suma de los montos totales de los impuestos:
        double totalTrasladadoConcepto = 0;
        double totalRetenidoConcepto = 0;
        double totalTrasladadoConceptoLocal = 0;
        double totalRetenidoConceptoLocal = 0;
        UsuarioBO bO = new UsuarioBO();
        
        for(ProductoSesion itemProducto:this.getListaProducto()){
            ImpuestoDaoImpl impuestoDaoImpl = new ImpuestoDaoImpl(usuario.getConn());
            if(itemProducto.getIdImpuesto()>0){//SI ES MAYOR A 0 ES QUE TIENE IMPUESTO RELACIONADOS:
                ///**RECUPERAMOS LOS IMPUESTOS RELACIONADOS A DICHO CONCEPTO                
                ImpuestoPorConceptoBO porConceptoBO = new ImpuestoPorConceptoBO(bO.getConn());
                ImpuestoPorConcepto[] impuestos = porConceptoBO.findImpuestoPorConceptos(0, 0, 0, 0, " AND ID_CONCEPTO = " + itemProducto.getIdProducto() + " AND ID_ESTATUS != 2 ");

                if(impuestos.length>0){
                    for(ImpuestoSesion impuestoDto : this.getListaImpuesto()){
                
                        ImpuestoSesion impuestoConceptos = new ImpuestoSesion();
                        //Impuesto impuestoDto = impuestoDaoImpl.findByPrimaryKey(itemProducto.getIdImpuesto());
                        //impuestoConceptos.setIdImpuesto(itemProducto.getIdImpuesto());
                        //Impuesto impuestoDto = impuestoDaoImpl.findByPrimaryKey(imp.getIdImpuesto());
                        impuestoConceptos.setIdImpuesto(impuestoDto.getIdImpuesto());
                        impuestoConceptos.setNombre(impuestoDto.getNombre());
                        impuestoConceptos.setDescripcion(impuestoDto.getDescripcion());
                        impuestoConceptos.setPorcentaje(impuestoDto.getPorcentaje());
                        impuestoConceptos.setTrasladado(impuestoDto.isTrasladado());
                        impuestoConceptos.setImplocal(impuestoDto.isImplocal());
                        impuestoConceptos.setMontoImpuestoXConcepto((  itemProducto.getMonto() * 0.01 * impuestoDto.getPorcentaje() ));
                        

                        if(!impuestoConceptos.isImplocal()){//si es federal
                            if(impuestoConceptos.isTrasladado()){
                                totalTrasladadoConcepto += impuestoConceptos.getMontoImpuestoXConcepto();
                            }else{
                                totalRetenidoConcepto += impuestoConceptos.getMontoImpuestoXConcepto();
                            }
                        }else{//si es impuesto local
                            if(impuestoConceptos.isTrasladado()){
                                totalTrasladadoConceptoLocal += impuestoConceptos.getMontoImpuestoXConcepto();
                            }else{
                                totalRetenidoConceptoLocal += impuestoConceptos.getMontoImpuestoXConcepto();
                            }
                        }                
                        //impList.add(impuestoConceptos);//agregamos al array los impuestos involucrados
                    }
                }
            }
        }
        bO.getConn().close();
        listaTotalTrasladosLocalesMonto = new BigDecimal(totalTrasladadoConcepto).setScale(2, RoundingMode.HALF_UP);
        listaTotalRetencionesLocalesMonto = new BigDecimal(totalRetenidoConcepto).setScale(2, RoundingMode.HALF_UP);
        
        //Ahora vamos a sumar el monto de los impuestos con ID identico       
        ArrayList<ImpuestoSesion> impListUnicos = new ArrayList<ImpuestoSesion>();
        for (ImpuestoSesion itemImpuestoList:this.getListaImpuesto()){
            if(impListUnicos.size()<=0){//validamos si el tamaño es 0, si lo es lo agregamos
                impListUnicos.add(itemImpuestoList);
            }else{//si ya tiene impuestos comparamos si ya existe dicho impuesto en el array:                
                boolean estaEnList = false;
                boolean noEstaEnList = false;
                
                        for (ImpuestoSesion itemImpuesto:impListUnicos){
                            if (itemImpuesto.getIdImpuesto()==itemImpuestoList.getIdImpuesto()){
                                //Ya se encuentra en el listado este impuesto
                                estaEnList=true;
                                itemImpuesto.setMontoImpuestoXConcepto( (itemImpuesto.getMontoImpuestoXConcepto() + itemImpuestoList.getMontoImpuestoXConcepto()) );
                                //itemImpuesto.setMontoImpuestoXConcepto( (new BigDecimal(itemImpuesto.getMontoImpuestoXConcepto()).setScale(2, RoundingMode.HALF_UP)).doubleValue() );
                            }else{
                                noEstaEnList=true;
                            }
                        }
                        if(estaEnList==false&&noEstaEnList==true){//VALIDAMOS QUE NO ESTA EN LA LISTA.
                            impListUnicos.add(itemImpuestoList);
                        }
            }
        }
        usuario.getConn().close();
        ////*
        
        for (ImpuestoSesion itemImpuesto:impListUnicos){
            
            if (!itemImpuesto.isTrasladado()){
                if(!itemImpuesto.isImplocal()){//SI ES UN IMPUESTO FEDERAL
                    retencionSchema = new  mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion();
                    retencionSchema.setImporte(new BigDecimal(itemImpuesto.getMontoImpuestoXConcepto()).setScale(2, RoundingMode.HALF_UP));
                    retencionSchema.setImpuesto(itemImpuesto.getNombre());

                    if (listaRetenciones==null)
                        listaRetenciones = new ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion>();

                    listaRetenciones.add(retencionSchema);
                }else{//SI ES UN IMPUESTO LOCAL
                    retencionesLocales = new ImpuestosLocales.RetencionesLocales();
                    retencionesLocales.setImporte(new BigDecimal(itemImpuesto.getMontoImpuestoXConcepto()).setScale(2, RoundingMode.HALF_UP));
                    retencionesLocales.setImpLocRetenido(itemImpuesto.getNombre());
                    retencionesLocales.setTasadeRetencion(new BigDecimal(itemImpuesto.getPorcentaje()).setScale(2, RoundingMode.HALF_UP));
                    
                    if(listaRetencionesLocales == null)
                        listaRetencionesLocales = new ArrayList<ImpuestosLocales.RetencionesLocales>();
                    
                    listaRetencionesLocales.add(retencionesLocales);
                }
            }else if (itemImpuesto.isTrasladado()){
                if(!itemImpuesto.isImplocal()){//SI ES UN IMPUESTO FEDERAL
                    trasladoSchema = new  mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado();                    
                    trasladoSchema.setImporte(new BigDecimal(itemImpuesto.getMontoImpuestoXConcepto()).setScale(2, RoundingMode.HALF_UP));
                    trasladoSchema.setImpuesto(itemImpuesto.getNombre());
                    trasladoSchema.setTasa(new BigDecimal(itemImpuesto.getPorcentaje()).setScale(2, RoundingMode.HALF_UP));

                    if (listaTraslados==null)
                        listaTraslados = new ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado>();

                    listaTraslados.add(trasladoSchema);
                }else{//SI ES UN IMPUESTO LOCAL
                    trasladosLocales = new ImpuestosLocales.TrasladosLocales();
                    trasladosLocales.setImporte(new BigDecimal(itemImpuesto.getMontoImpuestoXConcepto()).setScale(2, RoundingMode.HALF_UP));
                    trasladosLocales.setImpLocTrasladado(itemImpuesto.getNombre());
                    trasladosLocales.setTasadeTraslado(new BigDecimal(itemImpuesto.getPorcentaje()).setScale(2, RoundingMode.HALF_UP));
                    
                    if(listaTrasladosLocales == null)
                        listaTrasladosLocales = new ArrayList<ImpuestosLocales.TrasladosLocales>();
                    
                    listaTrasladosLocales.add(trasladosLocales);                    
                }
            }
            
            i++;
        }
        
        //return listaRetenciones;        
    }
    
        public void getSchemaImpuestosRetenidosTrasladadosFederalesLocalesXConcepto() throws Exception {
        
        //FEDERALES
        mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion retencionSchema;
        mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado trasladoSchema;
        mx.bigdata.sat.complementos.schema.implocal.ImpuestosLocales.TrasladosLocales trasladosLocales;
        mx.bigdata.sat.complementos.schema.implocal.ImpuestosLocales.RetencionesLocales retencionesLocales;
        
        //LOCALES (LOS LOCALES YA ESTAN MAPEADOS DENTRO DEL SCHEMA; CLASE CFDI32Factory.java EN EL METODO createImpuestosLocales(...) ).
        
        
        int i=0;
        
        ////*RECUPERAMOS LOS IMPUESTOS DE CADA CONCEPTO
        UsuarioBO usuario = new UsuarioBO();
        //Nueva lista de impuesto para mapearlos
        ArrayList<ImpuestoSesion> impList = new ArrayList<ImpuestoSesion>();
        //variables para la suma de los montos totales de los impuestos:
        double totalTrasladadoConcepto = 0;
        double totalRetenidoConcepto = 0;
        double totalTrasladadoConceptoLocal = 0;
        double totalRetenidoConceptoLocal = 0;
        UsuarioBO bO = new UsuarioBO();
        
        for(ProductoSesion itemProducto:this.getListaProducto()){
            ImpuestoDaoImpl impuestoDaoImpl = new ImpuestoDaoImpl(usuario.getConn());
            if(itemProducto.getIdImpuesto()>0){//SI ES MAYOR A 0 ES QUE TIENE IMPUESTO RELACIONADOS:
                ///**RECUPERAMOS LOS IMPUESTOS RELACIONADOS A DICHO CONCEPTO                
                ImpuestoPorConceptoBO porConceptoBO = new ImpuestoPorConceptoBO(bO.getConn());
                ImpuestoPorConcepto[] impuestos = porConceptoBO.findImpuestoPorConceptos(0, 0, 0, 0, " AND ID_CONCEPTO = " + itemProducto.getIdProducto() + " AND ID_ESTATUS != 2 ");

                if(impuestos.length>0){
                    for(ImpuestoPorConcepto imp : impuestos){
                
                        ImpuestoSesion impuestoConceptos = new ImpuestoSesion();
                        //Impuesto impuestoDto = impuestoDaoImpl.findByPrimaryKey(itemProducto.getIdImpuesto());
                        //impuestoConceptos.setIdImpuesto(itemProducto.getIdImpuesto());
                        Impuesto impuestoDto = impuestoDaoImpl.findByPrimaryKey(imp.getIdImpuesto());
                        impuestoConceptos.setIdImpuesto(imp.getIdImpuesto());
                        impuestoConceptos.setNombre(impuestoDto.getNombre());
                        impuestoConceptos.setDescripcion(impuestoDto.getDescripcion());
                        impuestoConceptos.setPorcentaje(impuestoDto.getPorcentaje());
                        impuestoConceptos.setTrasladado(impuestoDto.getTrasladado()==(short)1?true:false);
                        impuestoConceptos.setImplocal(impuestoDto.getImpuestoLocal()==(short)1?true:false);
                        impuestoConceptos.setMontoImpuestoXConcepto((  itemProducto.getMonto() * 0.01 * impuestoDto.getPorcentaje() ));
                        

                        if(!impuestoConceptos.isImplocal()){//si es federal
                            if(impuestoConceptos.isTrasladado()){
                                totalTrasladadoConcepto += impuestoConceptos.getMontoImpuestoXConcepto();
                            }else{
                                totalRetenidoConcepto += impuestoConceptos.getMontoImpuestoXConcepto();
                            }
                        }else{//si es impuesto local
                            if(impuestoConceptos.isTrasladado()){
                                totalTrasladadoConceptoLocal += impuestoConceptos.getMontoImpuestoXConcepto();
                            }else{
                                totalRetenidoConceptoLocal += impuestoConceptos.getMontoImpuestoXConcepto();
                            }
                        }                
                        impList.add(impuestoConceptos);//agregamos al array los impuestos involucrados
                    }
                }
            }
        }
        bO.getConn().close();
        //*listaTotalTrasladosLocalesMonto = new BigDecimal(totalTrasladadoConcepto).setScale(2, RoundingMode.HALF_UP);
        listaTotalTrasladosLocalesMonto = BigDecimal.valueOf(totalTrasladadoConcepto).setScale(2, RoundingMode.HALF_UP);
        //*listaTotalRetencionesLocalesMonto = new BigDecimal(totalRetenidoConcepto).setScale(2, RoundingMode.HALF_UP);
        listaTotalRetencionesLocalesMonto = BigDecimal.valueOf(totalRetenidoConcepto).setScale(2, RoundingMode.HALF_UP);
        
        //Ahora vamos a sumar el monto de los impuestos con ID identico       
        ArrayList<ImpuestoSesion> impListUnicos = new ArrayList<ImpuestoSesion>();
        for (ImpuestoSesion itemImpuestoList:impList){
            if(impListUnicos.size()<=0){//validamos si el tamaño es 0, si lo es lo agregamos
                impListUnicos.add(itemImpuestoList);
            }else{//si ya tiene impuestos comparamos si ya existe dicho impuesto en el array:                
                boolean estaEnList = false;
                boolean noEstaEnList = false;
                
                        for (ImpuestoSesion itemImpuesto:impListUnicos){
                            if (itemImpuesto.getIdImpuesto()==itemImpuestoList.getIdImpuesto()){
                                //Ya se encuentra en el listado este impuesto
                                estaEnList=true;
                                itemImpuesto.setMontoImpuestoXConcepto( (itemImpuesto.getMontoImpuestoXConcepto() + itemImpuestoList.getMontoImpuestoXConcepto()) );
                                //itemImpuesto.setMontoImpuestoXConcepto( (new BigDecimal(itemImpuesto.getMontoImpuestoXConcepto()).setScale(2, RoundingMode.HALF_UP)).doubleValue() );
                            }else{
                                noEstaEnList=true;
                            }
                        }
                        if(estaEnList==false&&noEstaEnList==true){//VALIDAMOS QUE NO ESTA EN LA LISTA.
                            impListUnicos.add(itemImpuestoList);
                        }
            }
        }
        usuario.getConn().close();
        ////*
        
        for (ImpuestoSesion itemImpuesto:impListUnicos){
            
            if (!itemImpuesto.isTrasladado()){
                if(!itemImpuesto.isImplocal()){//SI ES UN IMPUESTO FEDERAL
                    retencionSchema = new  mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion();
                    //*retencionSchema.setImporte(new BigDecimal(itemImpuesto.getMontoImpuestoXConcepto()).setScale(2, RoundingMode.HALF_UP));
                    retencionSchema.setImporte(BigDecimal.valueOf(itemImpuesto.getMontoImpuestoXConcepto()).setScale(2, RoundingMode.HALF_UP));
                    retencionSchema.setImpuesto(itemImpuesto.getNombre());

                    if (listaRetenciones==null)
                        listaRetenciones = new ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion>();

                    listaRetenciones.add(retencionSchema);
                }else{//SI ES UN IMPUESTO LOCAL
                    retencionesLocales = new ImpuestosLocales.RetencionesLocales();
                    //*retencionesLocales.setImporte(new BigDecimal(itemImpuesto.getMontoImpuestoXConcepto()).setScale(2, RoundingMode.HALF_UP));
                    retencionesLocales.setImporte(BigDecimal.valueOf(itemImpuesto.getMontoImpuestoXConcepto()).setScale(2, RoundingMode.HALF_UP));
                    retencionesLocales.setImpLocRetenido(itemImpuesto.getNombre());
                    //*retencionesLocales.setTasadeRetencion(new BigDecimal(itemImpuesto.getPorcentaje()).setScale(2, RoundingMode.HALF_UP));
                    retencionesLocales.setTasadeRetencion(BigDecimal.valueOf(itemImpuesto.getPorcentaje()));
                    
                    if(listaRetencionesLocales == null)
                        listaRetencionesLocales = new ArrayList<ImpuestosLocales.RetencionesLocales>();
                    
                    listaRetencionesLocales.add(retencionesLocales);
                }
            }else if (itemImpuesto.isTrasladado()){
                if(!itemImpuesto.isImplocal()){//SI ES UN IMPUESTO FEDERAL
                    trasladoSchema = new  mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado();                    
                    //*trasladoSchema.setImporte(new BigDecimal(itemImpuesto.getMontoImpuestoXConcepto()).setScale(2, RoundingMode.HALF_UP));
                    trasladoSchema.setImporte(BigDecimal.valueOf(itemImpuesto.getMontoImpuestoXConcepto()).setScale(2, RoundingMode.HALF_UP));
                    trasladoSchema.setImpuesto(itemImpuesto.getNombre());
                    //*trasladoSchema.setTasa(new BigDecimal(itemImpuesto.getPorcentaje()).setScale(2, RoundingMode.HALF_UP));
                    trasladoSchema.setTasa(BigDecimal.valueOf(itemImpuesto.getPorcentaje()));

                    if (listaTraslados==null)
                        listaTraslados = new ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado>();

                    listaTraslados.add(trasladoSchema);
                }else{//SI ES UN IMPUESTO LOCAL
                    trasladosLocales = new ImpuestosLocales.TrasladosLocales();
                    //*trasladosLocales.setImporte(new BigDecimal(itemImpuesto.getMontoImpuestoXConcepto()).setScale(2, RoundingMode.HALF_UP));
                    trasladosLocales.setImporte(BigDecimal.valueOf(itemImpuesto.getMontoImpuestoXConcepto()).setScale(2, RoundingMode.HALF_UP));
                    trasladosLocales.setImpLocTrasladado(itemImpuesto.getNombre());
                    //*trasladosLocales.setTasadeTraslado(new BigDecimal(itemImpuesto.getPorcentaje()).setScale(2, RoundingMode.HALF_UP));
                    trasladosLocales.setTasadeTraslado(BigDecimal.valueOf(itemImpuesto.getPorcentaje()));
                    
                    if(listaTrasladosLocales == null)
                        listaTrasladosLocales = new ArrayList<ImpuestosLocales.TrasladosLocales>();
                    
                    listaTrasladosLocales.add(trasladosLocales);                    
                }
            }
            
            i++;
        }
        
        //return listaRetenciones;        
    }
    
    //FEDERALES
    private ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion> listaRetenciones = null;
    private ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado> listaTraslados = null;

    public ArrayList<Comprobante.Impuestos.Retenciones.Retencion> getListaRetenciones() {
        return listaRetenciones;
    }
    public void setListaRetenciones(ArrayList<Comprobante.Impuestos.Retenciones.Retencion> listaRetenciones) {
        this.listaRetenciones = listaRetenciones;
    }
    public ArrayList<Comprobante.Impuestos.Traslados.Traslado> getListaTraslados() {
        return listaTraslados;
    }
    public void setListaTraslados(ArrayList<Comprobante.Impuestos.Traslados.Traslado> listaTraslados) {
        this.listaTraslados = listaTraslados;
    }
       
    //LOCALES
    private ArrayList<mx.bigdata.sat.complementos.schema.implocal.ImpuestosLocales.TrasladosLocales> listaTrasladosLocales = null;
    private ArrayList<mx.bigdata.sat.complementos.schema.implocal.ImpuestosLocales.RetencionesLocales> listaRetencionesLocales = null;
    private BigDecimal listaTotalTrasladosLocalesMonto = BigDecimal.ZERO;
    private BigDecimal listaTotalRetencionesLocalesMonto= BigDecimal.ZERO;

    public void setListaRetencionesLocales(ArrayList<ImpuestosLocales.RetencionesLocales> listaRetencionesLocales) {
        this.listaRetencionesLocales = listaRetencionesLocales;
    }
    public ArrayList<ImpuestosLocales.RetencionesLocales> getListaRetencionesLocales() {
        return listaRetencionesLocales;
    }
    public void setListaTrasladosLocales(ArrayList<ImpuestosLocales.TrasladosLocales> listaTrasladosLocales) {
        this.listaTrasladosLocales = listaTrasladosLocales;
    }
    public ArrayList<ImpuestosLocales.TrasladosLocales> getListaTrasladosLocales() {
        return listaTrasladosLocales;
    }
    public BigDecimal getListaTotalRetencionesLocalesMonto() {
        return listaTotalRetencionesLocalesMonto;
    }
    public void setListaTotalRetencionesLocalesMonto(BigDecimal listaTotalRetencionesLocalesMonto) {
        this.listaTotalRetencionesLocalesMonto = listaTotalRetencionesLocalesMonto;
    }
    public BigDecimal getListaTotalTrasladosLocalesMonto() {
        return listaTotalTrasladosLocalesMonto;
    }
    public void setListaTotalTrasladosLocalesMonto(BigDecimal listaTotalTrasladosLocalesMonto) {
        this.listaTotalTrasladosLocalesMonto = listaTotalTrasladosLocalesMonto;
    }
    
    
    
    public ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion> getSchemaImpuestosRetenidosNomina() throws Exception {
        ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion> listaRetenciones = null;
        
        mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion retencionSchema;
        int i=0;
        for (ImpuestoSesion itemImpuesto:this.getListaImpuesto()){
            
            if (!itemImpuesto.isTrasladado()){
                retencionSchema = new  mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion();                
                retencionSchema.setImporte(new BigDecimal(itemImpuesto.getMonto()).setScale(2, RoundingMode.HALF_UP));
                retencionSchema.setImpuesto(itemImpuesto.getNombre());
                
                if (listaRetenciones==null)
                    listaRetenciones = new ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion>();
                
                listaRetenciones.add(retencionSchema);
            }
            
            i++;
        }
        
        return listaRetenciones;
    }
    
    private ArrayList<mx.bigdata.sat.complementos.schema.nomina.Nomina.Percepciones.Percepcion> percepciones = null;
    private ArrayList<mx.bigdata.sat.complementos.schema.nomina.Nomina.Deducciones.Deduccion> deducciones = null;
    private ArrayList<mx.bigdata.sat.complementos.schema.nomina.Nomina.Incapacidades.Incapacidad> incapacidades = null;
    private ArrayList<mx.bigdata.sat.complementos.schema.nomina.Nomina.HorasExtras.HorasExtra> horasExtras = null;

    public ArrayList<Nomina.Percepciones.Percepcion> getPercepciones() {
        return percepciones;
    }
    public void setPercepciones(ArrayList<Nomina.Percepciones.Percepcion> percepciones) {
        this.percepciones = percepciones;
    }
    public ArrayList<Nomina.Deducciones.Deduccion> getDeducciones() {
        return deducciones;
    }
    public void setDeducciones(ArrayList<Nomina.Deducciones.Deduccion> deducciones) {
        this.deducciones = deducciones;
    }
    
    public void getSchemaNomina() throws Exception {       
        
        percepciones = new ArrayList<mx.bigdata.sat.complementos.schema.nomina.Nomina.Percepciones.Percepcion>();
        deducciones = new ArrayList<mx.bigdata.sat.complementos.schema.nomina.Nomina.Deducciones.Deduccion>();
        incapacidades = new ArrayList<mx.bigdata.sat.complementos.schema.nomina.Nomina.Incapacidades.Incapacidad>();
        horasExtras = new ArrayList<mx.bigdata.sat.complementos.schema.nomina.Nomina.HorasExtras.HorasExtra>();
        
        mx.bigdata.sat.complementos.schema.nomina.Nomina.Percepciones.Percepcion percepcion = null;
        mx.bigdata.sat.complementos.schema.nomina.Nomina.Deducciones.Deduccion deduccion = null;
        mx.bigdata.sat.complementos.schema.nomina.Nomina.Incapacidades.Incapacidad incapacidad = null;
        mx.bigdata.sat.complementos.schema.nomina.Nomina.HorasExtras.HorasExtra horaExtra = null;
        
        for(DeduccionSesion itemDeduccion : this.getListaDeduccion()){
            deduccion = new mx.bigdata.sat.complementos.schema.nomina.Nomina.Deducciones.Deduccion();
            deduccion.setTipoDeduccion(itemDeduccion.getIdNominaTipoDeduccion());
            deduccion.setClave(itemDeduccion.getClave());
            deduccion.setConcepto(itemDeduccion.getDescripcionConceptoAlterna());
            deduccion.setImporteGravado(new BigDecimal(itemDeduccion.getImporteGravado()).setScale(2, RoundingMode.HALF_UP));
            deduccion.setImporteExento(new BigDecimal(itemDeduccion.getImporteExento()).setScale(2, RoundingMode.HALF_UP));
            //System.out.println("____________________DEDUCCION TOTAL GRAVADO: "+deduccion.getImporteGravado());
            //System.out.println("____________________DEDUCCION TOTAL EXENTO: "+deduccion.getImporteExento());
            deducciones.add(deduccion);
        }
        
        for(PercepcionSesion itemPercepcion : this.getListaPercepcion()){
            percepcion = new mx.bigdata.sat.complementos.schema.nomina.Nomina.Percepciones.Percepcion();
            percepcion.setTipoPercepcion(itemPercepcion.getIdNominaTipoPercepcion());
            percepcion.setClave(itemPercepcion.getClave());
            percepcion.setConcepto(itemPercepcion.getDescripcionConceptoAlterna());
            percepcion.setImporteGravado(new BigDecimal (itemPercepcion.getImporteGravado()).setScale(2, RoundingMode.HALF_UP));
            percepcion.setImporteExento(new BigDecimal (itemPercepcion.getImporteExento()).setScale(2, RoundingMode.HALF_UP));
            //System.out.println("____________________PERCEPCION TOTAL GRAVADO: "+percepcion.getImporteGravado());
            //System.out.println("____________________PERCEPCION TOTAL EXENTO: "+percepcion.getImporteExento());
            percepciones.add(percepcion);
        }
        
        /////meter aqui for de horas extra e incapacidades . . .        
               
        
    }
    
}
