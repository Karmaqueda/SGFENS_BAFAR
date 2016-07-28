/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.cfdi.CFDI32Factory;
import com.tsp.sct.cfdi.Cfd32BO;
import com.tsp.sct.config.Configuration;
import com.tsp.sct.dao.dto.CertificadoDigital;
import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.dto.ComprobanteFiscal;
import com.tsp.sct.dao.dto.DatosUsuario;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.dao.dto.Folios;
import com.tsp.sct.dao.dto.FormaPago;
import com.tsp.sct.dao.dto.NominaComprobanteDescripcion;
import com.tsp.sct.dao.dto.NominaComprobanteDescripcionPercepcionDeduccion;
import com.tsp.sct.dao.dto.NominaEmpleado;
import com.tsp.sct.dao.dto.NominaRegistroPatronal;
import com.tsp.sct.dao.dto.SgfensPedido;
import com.tsp.sct.dao.dto.TipoDeMoneda;
import com.tsp.sct.dao.dto.TipoPago;
import com.tsp.sct.dao.dto.Ubicacion;
import com.tsp.sct.dao.dto.Usuarios;
import com.tsp.sct.dao.exceptions.NominaComprobanteDescripcionDaoException;
import com.tsp.sct.dao.jdbc.FoliosDaoImpl;
import com.tsp.sct.dao.jdbc.NominaComprobanteDescripcionDaoImpl;
import com.tsp.sct.dao.jdbc.NominaComprobanteDescripcionPercepcionDeduccionDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensPedidoDaoImpl;
import com.tsp.sct.dao.jdbc.TipoPagoDaoImpl;
import com.tsp.sgfens.sesion.ComprobanteFiscalSesion;
import com.tsp.sgfens.sesion.DeduccionSesion;
import com.tsp.sgfens.sesion.PercepcionSesion;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.bigdata.sat.cfdi.schema.v32.Comprobante;

/**
 *
 * @author leonardo
 * 
 * Clase para generar el xml y almacenado a base de datos de todo el contenido del comprobante correspondiente. Genera un comprobante nuevo a traves de otro
 */
public class NominaAutomaticaGenerarComprobanteFiscaBO {
    
    public String mensajeDeError = "";
    public int idComprobanteGenerado = 0;
    
    public boolean generarComprobante(ComprobanteFiscalSesion cfdiSesion, int intTipoComprobante, String stringTipoComprobante, Connection conn, Date fechaPago, Date fechaPago_Inicial, Date fechaPago_Final){
        idComprobanteGenerado = 0;
        boolean generadaCorrectamente = false;
        
        //recuperamos toda la info de la base de datos de comprobante fiscal:
        //--
        ComprobanteFiscal cf = new ComprobanteFiscalBO(cfdiSesion.getIdComprobanteFiscal(), conn).getComprobanteFiscal();
        TipoPago tipoPago = null;
        NominaComprobanteDescripcion nominaComprobanteDescripcion = null;
        NominaEmpleado empleadoNomina = null;
        if (cf.getIdComprobanteFiscal() > 0){            
            empleadoNomina = new NominaEmpleadoBO(conn, cf.getIdCliente()).getNominaEmpleado();
            tipoPago = new TipoPagoBO(cf.getIdTipoPago(),conn).getTipoPago();
            
            NominaComprobanteDescripcion[] ncd = new NominaComprobanteDescripcion[0];
            try {
                ncd = new NominaComprobanteDescripcionDaoImpl().findWhereIdCromprobanteFiscalEquals(cf.getIdComprobanteFiscal());
            } catch (NominaComprobanteDescripcionDaoException ex) {
                ex.printStackTrace();
            }
            nominaComprobanteDescripcion = ncd[0];
        }       
        
        //19 =  Guardar ComprobanteFiscal (Sesion -> BD)        
        //boolean enviarCorreo = false;
        
        //Parametros
        String tipoComprobante = "egreso"; //ingreso = factura normal, egreso = salida de dinero. stringTipoComprobante
        int idTipoComprobante = 40; // tipoComprobante
        
        Date fecha_pago = null;  
        int idCliente = -1;
        int idFolios = -1;
        int idFormaPago = -1;
        Date parcialidadFechaOrig = null;
        int tipoMonedaInt = -1;
        double tipoCambio = 1;
        String parcialidadFormat = null; //Parcialidad 1 de X
        
        String comentarios = cfdiSesion.getComentarios();
        //String tipo_moneda = request.getParameter("tipo_moneda")!=null?new String(request.getParameter("tipo_moneda").getBytes("ISO-8859-1"),"UTF-8"):"MXN";
        tipoMonedaInt = cf.getIdTipoMoneda();
        String descuento_motivo = "";
        //String lista_correos = request.getParameter("lista_correos")!=null?new String(request.getParameter("lista_correos").getBytes("ISO-8859-1"),"UTF-8"):"";
        idCliente = cf.getIdCliente();
        //try{ fecha_pago = new SimpleDateFormat("dd/MM/yyyy").parse(fechaPago.toString());}catch(Exception e){}
        fecha_pago = fechaPago;
        
        idFolios = cf.getIdFolio();
        String tipoPagoDescripcion = tipoPago.getDescTipoPago();
        String metodoPago = tipoPago.getClaveMetodoSat();
        String tipoPagoNumCuenta = "";
        if(tipoPago!=null)
            tipoPagoNumCuenta = tipoPago.getNumeroCuenta();
        idFormaPago = cf.getIdFormaPago();
        String parcialidad = "";
        String parcialidadFolioOrig = "";
        String parcialidadSerieOrig = "";
        String parcialidadFechaOrigStr = "";        
        String parcialidadMontoOrig = "";
        tipoCambio = cf.getTipoDeCambio();
        
        
        //*DATOS NECESARIOS PARA LA NOMINA:
        Date fechaPago2 = fechaPago;
        Date fechaPagoInicial = fechaPago_Inicial;
        Date fechaPagoFinal = fechaPago_Final;
        int numDiasPago = 0;
        int idNominaRegistroPatronal = -1;
        String registroPatronal = null;
        
        //try{ fechaPago2 = new SimpleDateFormat("dd/MM/yyyy").parse(fechaPago.toString());}catch(Exception e){e.printStackTrace();}
        //try{ fechaPagoInicial = new SimpleDateFormat("dd/MM/yyyy").parse(fechaPago_Inicial.toString());}catch(Exception e){e.printStackTrace();}
        //try{ fechaPagoFinal = new SimpleDateFormat("dd/MM/yyyy").parse(fechaPago_Final.toString());}catch(Exception e){e.printStackTrace();}
        numDiasPago = (int)nominaComprobanteDescripcion.getNumDiasPagados();
        idNominaRegistroPatronal = nominaComprobanteDescripcion.getIdNominaRegPatronal();
        
        double montoISR = 0;
        double pocentajeISR = 0;
        
        montoISR = nominaComprobanteDescripcion.getIsrMontoImpuesto();
        pocentajeISR = nominaComprobanteDescripcion.getIsrImpuestoPorcentaje();         
                        
                //Validaciones de la Factura
                if (cfdiSesion.getListaProducto().size()<=0 && cfdiSesion.getListaServicio().size()<=0){
                    System.out.println("Debe seleccionar al menos un producto o servicio para generar el Comprobante Fiscal. Lista de Productos y Servicios vacia. ");
                    mensajeDeError += " Debe seleccionar al menos un producto o servicio para generar el Comprobante Fiscal. Lista de Productos y Servicios vacia.";
                }
                        
                
                String tipoMonedaStr=null;
                int folioGenerado = -1;
                String folioGeneradoStr = null;
                String serie = null;
                
                Cfd32BO cfd32BO = null;
                File archivoXML = null;
                File archivoPDF = null;

                
                
                if (mensajeDeError.equals("")){
                    //Almacenar Nuevo Tipo de Pago--------------
                    TipoPagoDaoImpl tipoPagoDao = new TipoPagoDaoImpl(conn);                   

                    TipoPago tipoPagoDto = new TipoPago();
                    try{
                        int nextIdTipoPago = tipoPagoDao.findLast().getIdTipoPago() + 1;
                        tipoPagoDto.setClaveMetodoSat(metodoPago);
                        tipoPagoDto.setDescTipoPago(tipoPagoDescripcion);
                        tipoPagoDto.setNumeroCuenta(tipoPagoNumCuenta);
                        tipoPagoDto.setIdTipoPago(nextIdTipoPago);
                        
                        //Insertamos registro
                        tipoPagoDao.insert(tipoPagoDto);                        
                        cfdiSesion.setTipo_pago(tipoPagoDto);
                    }catch(Exception ex){
                        mensajeDeError+="<ul> No se pudo almacenar los datos del Tipo de Pago en un nuevo registro: " + ex.toString();
                        System.out.println(" No se pudo almacenar los datos del Tipo de Pago en un nuevo registro: " + ex.toString());
                    }
                    //Fin Almacenar Nuevo Tipo de Pago----------
                    
                    //Series - Folios-------------------------
                    //En caso de estar seleccionada una Serie, calcular nuevo folio--------
                    if (idFolios>0){
                        try{
                            Folios foliosDto = new FoliosBO(idFolios,conn).getFolios();
                            if (foliosDto!=null){
                                folioGenerado = foliosDto.getUltimoFolio() + 1;
                                //Revisamos que el nuevo folio cumpla con el minimo y el maximo establecido en la serie
                                if (folioGenerado > foliosDto.getFolioHasta()){
                                    mensajeDeError += " La serie elegida a llegado a su folio máximo ("+foliosDto.getFolioHasta()+"). Elija otra o genera una nueva.";
                                    System.out.println("La serie elegida a llegado a su folio máximo ("+foliosDto.getFolioHasta()+"). Elija otra o genera una nueva.");
                                }
                                if (folioGenerado < foliosDto.getFolioDesde()){
                                    mensajeDeError += "La serie elegida esta mal configurada, tiene establecido un valor minimo (desde) mayor al ultimo folio generado.";
                                    System.out.println("La serie elegida esta mal configurada, tiene establecido un valor minimo (desde) mayor al ultimo folio generado.");
                                }
                                
                                if (mensajeDeError.trim().equals("")){
                                    folioGeneradoStr = new DecimalFormat("#0000").format(folioGenerado);
                                    serie = foliosDto.getSerie();
                                    
                                    foliosDto.setUltimoFolio(folioGenerado);
                                    try{
                                        new FoliosDaoImpl(conn).update(foliosDto.createPk(), foliosDto);
                                    }catch(Exception ex){
                                        
                                    }
                                }
                            }else{
                                mensajeDeError += "<ul> No se pudo recuperar la información de la Serie elegida, compruebe los datos e intente de nuevo. ";
                                System.out.println(" No se pudo recuperar la información de la Serie elegida, compruebe los datos e intente de nuevo. ");
                            }
                        }catch(Exception ex){
                            mensajeDeError += "<ul> Ocurrio un error al recuperar los datos de la Serie elegida y calculo del nuevo folio: " + ex.toString();
                            System.out.println(" Ocurrio un error al recuperar los datos de la Serie elegida y calculo del nuevo folio: " + ex.toString());
                        }
                    }
                    //Fin Series - Folios-------------------------
                    
                    //tipo Moneda
                    try{
                        TipoDeMoneda tipoDeMonedaDto = new TipoDeMonedaBO(tipoMonedaInt,conn).getTipoMoneda();
                        tipoMonedaStr = tipoDeMonedaDto.getClave();
                    }catch(Exception ex){
                        mensajeDeError += "No se pudo recuperar los datos del tipo de moneda elegido: " + ex.toString();
                        System.out.println(" No se pudo recuperar los datos del tipo de moneda elegido: " + ex.toString());
                    }
                    //fin tipo Moneda
                    
                    //Registro patronal
                    try{
                        NominaRegistroPatronal nominaRegistroPatronal = new NominaRegistroPatronalBO(idNominaRegistroPatronal, conn).getNominaRegistroPatronal();
                        registroPatronal = nominaRegistroPatronal.getRegistroPatronal();
                    }catch(Exception ex){
                        mensajeDeError += "No se pudo recuperar los datos del Registro Patronal elegido: " + ex.toString();
                        System.out.println(" No se pudo recuperar los datos del Registro Patronal elegido: " + ex.toString());
                    }
                    //Fin Registro Patronal
                }
                
                Empresa empresaDto = null;
                if (mensajeDeError.equals("")){
                    //---------GENERACION DE XML

                        try{
                        
                            FormaPago formaPagoDto = new FormaPagoBO(conn).findFormaPagoById(idFormaPago);
                            empresaDto = new EmpresaBO(conn).findEmpresabyId(cf.getIdEmpresa());
                            Ubicacion ubicacionDto = new UbicacionBO(conn).findUbicacionbyId(empresaDto.getIdUbicacionFiscal());
                            NominaEmpleado empleadoDto = new NominaEmpleadoBO(conn).findNominaEmpleadobyId(idCliente);
                            
                            //convertimos el empleado en cliente:
                            Cliente clienteDto = new Cliente();
                            clienteDto.setRfcCliente(empleadoDto.getRfc());
                            clienteDto.setNombreCliente(empleadoDto.getNombre());
                            clienteDto.setApellidoPaternoCliente(empleadoDto.getApellidoPaterno());
                            clienteDto.setApellidoMaternoCliente(empleadoDto.getApellidoMaterno());
                            clienteDto.setRazonSocial(empleadoDto.getNombre().trim() +" "+empleadoDto.getApellidoPaterno().trim() +" "+ empleadoDto.getApellidoMaterno().trim());                            
                            clienteDto.setPais("MEXICO");
                            
                            ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Conceptos.Concepto> listaConceptos = cfdiSesion.getSchemaConceptos();
                            ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado> listaTraslados = cfdiSesion.getSchemaImpuestosTraslados();
                            //ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion> listaRetenciones = cfdiSesion.getSchemaImpuestosRetenidos();
                            ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion> listaRetenciones = cfdiSesion.getSchemaImpuestosRetenidosNomina();
                            
                            ////*PARA EL COMPLEMENTO DE NOMINA:
                            //LLAMAMOS AL METODO QUE NOS GENERA PERCEPCIONES Y DEDUCCIONES:
                            cfdiSesion.getSchemaNomina();
                            
                            ArrayList<mx.bigdata.sat.complementos.schema.nomina.Nomina.Percepciones.Percepcion> percepciones = cfdiSesion.getPercepciones();
                            ArrayList<mx.bigdata.sat.complementos.schema.nomina.Nomina.Deducciones.Deduccion> deducciones = cfdiSesion.getDeducciones();
                            
                            ////*

                            Comprobante comprobanteFiscal = CFDI32Factory.createComprobante(formaPagoDto, 
                                    cfdiSesion.getTipo_pago(), 
                                    empresaDto, ubicacionDto ,  
                                    clienteDto, 
                                    listaConceptos, 
                                    listaTraslados, 
                                    listaRetenciones, 
                                    cfdiSesion.calculaSubTotal().doubleValue(), 
                                    cfdiSesion.calculaTotal().doubleValue(), 
                                    tipoComprobante,
                                    parcialidadFormat,
                                    percepciones,
                                    deducciones,
                                    empleadoDto,
                                    fechaPago2,
                                    fechaPagoInicial,
                                    fechaPagoFinal,
                                    numDiasPago,
                                    registroPatronal);

                            if (comprobanteFiscal!=null){
                                if (idFormaPago==2){
                                    //Parcialidades
                                    if (parcialidadFolioOrig.trim().length()>0)
                                        comprobanteFiscal.setFolioFiscalOrig(parcialidadFolioOrig);
                                    if (parcialidadSerieOrig.trim().length()>0)
                                        comprobanteFiscal.setSerieFolioFiscalOrig(parcialidadSerieOrig);
                                    if (parcialidadFechaOrig != null)
                                        comprobanteFiscal.setFechaFolioFiscalOrig(parcialidadFechaOrig);
                                    if (parcialidadMontoOrig.trim().length()>0)
                                        comprobanteFiscal.setMontoFolioFiscalOrig(new BigDecimal(parcialidadMontoOrig));
                                }
                                //Descuento
                                if (cfdiSesion.getDescuento_importe() >0){
                                    comprobanteFiscal.setDescuento(cfdiSesion.getDescuentoImporte());
                                    comprobanteFiscal.setMotivoDescuento(descuento_motivo);
                                }
                                if (idFolios>0 && serie!=null){
                                    comprobanteFiscal.setSerie(serie);
                                    comprobanteFiscal.setFolio(folioGeneradoStr);
                                }
                                comprobanteFiscal.setMoneda(tipoMonedaStr);
                                if (tipoCambio!=1)
                                    comprobanteFiscal.setTipoCambio(new BigDecimal(tipoCambio).setScale(2, RoundingMode.HALF_UP).toString());
                            }else{
                                mensajeDeError +="<ul>No se pudo generar el Comprobante Fiscal (XML)";
                                System.out.println("---------------------- No se pudo generar el Comprobante Fiscal (XML)");
                            }
                            
                            if (mensajeDeError.equals("")){
                                CertificadoDigitalBO certificadoDigitalBO = new CertificadoDigitalBO(conn);
                                CertificadoDigital certificadoDigitalDto = certificadoDigitalBO.findCertificadoByEmpresa(empresaDto.getIdEmpresa());
                                
                                Configuration appConfig = new Configuration();

                                String rutaArchivosUsuario = appConfig.getApp_content_path() + empresaDto.getRfc() + "/";
                                //String rutaArchivosFacturaCliente = rutaArchivosUsuario + "cfd/emitidos/facturas/" + clienteDto.getRfcCliente() + "/";

                                String rutaCerEmisor = rutaArchivosUsuario + certificadoDigitalDto.getNombreCer();
                                String rutaKeyEmisor = rutaArchivosUsuario + certificadoDigitalDto.getNombreKey();
                                String passwordKeyEmisor = certificadoDigitalDto.getPassword();
                                
                                cfd32BO = new Cfd32BO(comprobanteFiscal);
                                //Sellamos comprobante y validamos
                                cfd32BO.sellarComprobante(rutaCerEmisor, rutaKeyEmisor, passwordKeyEmisor);
                                //Timbramos comprobante
                                System.out.println("*-*-*-*-*-*--*-*-*-*--*-*-*-*-*--*-**--AAAAAAAAAAAA IDENTIFICADOR: "+idTipoComprobante+", -*-*-*--: "+tipoComprobante);
                                cfd32BO.timbrarComprobante();
                                System.out.println("*-*-*-*-*-*--*-*-*-*--*-*-*-*-*--*-**--BBBBBBBBBBBB IDENTIFICADOR: "+idTipoComprobante+", -*-*-*--: "+tipoComprobante);
                                
                                //Asignamos id Empresa para poder obtener el logo en el PDF
                                cfd32BO.setComprobanteFiscalDto(new ComprobanteFiscal());
                                cfd32BO.getComprobanteFiscalDto().setIdEmpresa(empresaDto.getIdEmpresa());
                                
                                archivoXML = cfd32BO.guardarComprobante(empresaDto, clienteDto, TipoComprobanteBO.getTipoComprobanteNombreCarpeta(idTipoComprobante) );//"facturas");
                                
                                //archivoPDF = cfd32BO.guardarRepresentacionImpresa(empresaDto, clienteDto, TipoComprobanteBO.getTipoComprobanteNombreCarpeta(idTipoComprobante) );//"facturas");
                                
                                //Copiado de XML a cron de envío, lo hacemos hasta que todo haya concluido correctamente
                                //cfd32BO.copiarCFDIenvioAsincrono(archivoXML);
                                
                            }
                            
                        }catch(Exception ex){
                            ex.printStackTrace();
                            if (ex.getCause()!=null){
                                mensajeDeError +="<ul>Ocurrio un error al generar XML: " + ex.getCause().toString();
                                System.out.println(" Ocurrio un error al generar XML: " + ex.getCause().toString());
                            }else{
                                mensajeDeError +="<ul>Ocurrio un error al generar XML: " + ex.toString();
                                System.out.println(" Ocurrio un error al generar XML: " + ex.toString());
                            }
                        }
                            
                    //---------FIN GENERACION XML
                }              
                
                ComprobanteFiscal comprobanteFiscalDto = null;
                ComprobanteFiscalBO comprobanteFiscalBO = new ComprobanteFiscalBO(conn);
                if (mensajeDeError.equals("") && archivoXML!=null && cfd32BO!=null){
                    cfdiSesion.setComentarios(comentarios);
                    cfdiSesion.setDescuento_motivo(descuento_motivo);
                    cfdiSesion.setFecha_pago(fecha_pago);
                    cfdiSesion.setId_estatus(EstatusComprobanteBO.ESTATUS_ENTREGADA);//Entregado
                    
                    //si se selecciono folio
                    if (idFolios>0 && folioGenerado>0 && folioGeneradoStr!=null){
                        cfdiSesion.setId_folio(idFolios);
                        cfdiSesion.setFolio_generado(folioGeneradoStr);
                    }
                    
                    cfdiSesion.setId_forma_pago(idFormaPago);
                    if (idFormaPago==2 && parcialidadFormat!=null){
                        cfdiSesion.setParcialidad(parcialidadFormat);
                    }
                    
                    cfdiSesion.setId_tipo_comprobante(idTipoComprobante);
                    cfdiSesion.setArchivo_cfd(archivoXML.getName());
                    try {
                        //*****CADENA SIN ENCRIPTAR******
                        cfdiSesion.setCadena_original(cfd32BO.getCfd().getCadenaOriginal());
                    } catch (Exception ex) {ex.printStackTrace();}
                    cfdiSesion.setSello_digital(cfd32BO.getComprobanteFiscal().getSello());
                    cfdiSesion.setTipo_moneda_int(tipoMonedaInt);
                    cfdiSesion.setTipo_moneda(tipoMonedaStr);
                    cfdiSesion.setTipo_cambio(tipoCambio);
                    cfdiSesion.setUuid(cfd32BO.getTimbreFiscalDigital().getUUID());
                    cfdiSesion.setSello_sat(cfd32BO.getTimbreFiscalDigital().getSelloSAT());
                    cfdiSesion.setNoCertificadoSAT(cfd32BO.getTimbreFiscalDigital().getNoCertificadoSAT());
                    cfdiSesion.setFechaTimbrado(cfd32BO.getTimbreFiscalDigital().getFechaTimbrado());                    
                    
                    try{
                        if (idCliente>0){
                            cfdiSesion.setCliente(new ClienteBO(idCliente, conn).getCliente());
                        }
                    }catch(Exception ex){ ex.printStackTrace();}
                    
                    //Para generar un nuevo comprobanteFiscal en cada Guardar
                    //Deshabilitar en caso de querer actualizar en lugar de generar una nueva
                    cfdiSesion.setIdComprobanteFiscal(0);
                    
                    if (mensajeDeError.equals("")){
                        //request.getSession().setAttribute("cfdiSesion", cfdiSesion);
                        //comprobanteFiscalAux = cfdiSesion;
                        try{
                            UsuarioBO user = new UsuarioBO();
                            user.setUser(new Usuarios());
                            user.getUser().setIdEmpresa(empresaDto.getIdEmpresa());
                            
                            comprobanteFiscalDto = comprobanteFiscalBO.creaActualizaComprobanteFiscal(cfdiSesion, user);
                            
                            cfd32BO.copiarCFDIenvioAsincrono(archivoXML);
                        }catch(Exception ex){
                            //En caso de error, borramos archivo XML ya que no se registrara en BD
                            try{
                                for (int k=0;k<10;k++)
                                    archivoXML.delete();
                            }catch(Exception e2){}
                            try{
                                Folios foliosDto = new FoliosBO(idFolios,conn).getFolios();
                                folioGenerado = foliosDto.getUltimoFolio() - 1;
                                foliosDto.setUltimoFolio(folioGenerado);
                                new FoliosDaoImpl(conn).update(foliosDto.createPk(), foliosDto);
                            }catch(Exception ex2){}
                            mensajeDeError+= "<ul>Ocurrio un error al almacenar el registro de comprobanteFiscal: <br/>" + ex.toString();
                            System.out.println(" Ocurrio un error al almacenar el registro de comprobanteFiscal: <br/>" + ex.toString());
                            
                        }
                        
                        //ALMACENAMOS LOS DATOS DE NOMINA_COMPROBANTE_DESCRIPCION
                        try{
                            NominaComprobanteDescripcion ncd = new NominaComprobanteDescripcion();
                            ncd.setIdCromprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal());
                            ncd.setFechaPago(fechaPago2);
                            ncd.setFechaInicialPago(fechaPagoInicial);
                            ncd.setFechaFinPago(fechaPagoFinal);
                            ncd.setNumDiasPagados(numDiasPago);
                            ncd.setIsrImpuestoPorcentaje(pocentajeISR);
                            ncd.setIsrMontoImpuesto(montoISR);
                            ncd.setIdNominaRegPatronal(idNominaRegistroPatronal);
                            new NominaComprobanteDescripcionDaoImpl(conn).insert(ncd);
                        }catch(Exception e){}
                        //ALMACENAMOS LOS DATOS DE DESCRIPCION_HORAS_EXTRA
                        
                        //ALMACENAMOS LOS DATOS DE COMPROBANTE_DESCRIPCION_INCAPACIDAD
                        
                        //ALMACENAMOS LOS DATOS DE COMPROBANTE_DESCRIPCION_PERCEPCION_DEDUCCION
                        try{
                            NominaComprobanteDescripcionPercepcionDeduccion ncdpd = new NominaComprobanteDescripcionPercepcionDeduccion();
                            if(cfdiSesion.getListaDeduccion()!=null && cfdiSesion.getListaDeduccion().size()>0){
                                for(DeduccionSesion itemDeduccion : cfdiSesion.getListaDeduccion()){
                                    ncdpd.setIdCromprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal());
                                    ncdpd.setIdPercepcionDeduccion(2);
                                    if(String.valueOf(itemDeduccion.getIdNominaTipoDeduccion()).length()==1)
                                        ncdpd.setTipoClave("00"+itemDeduccion.getIdNominaTipoDeduccion());
                                    else if(String.valueOf(itemDeduccion.getIdNominaTipoDeduccion()).length()==2)
                                        ncdpd.setTipoClave("0"+itemDeduccion.getIdNominaTipoDeduccion());
                                    else if(String.valueOf(itemDeduccion.getIdNominaTipoDeduccion()).length()==3)
                                        ncdpd.setTipoClave(""+itemDeduccion.getIdNominaTipoDeduccion());                                        
                                    ncdpd.setClavePatron(itemDeduccion.getClave());
                                    ncdpd.setConceptoDescripcion(itemDeduccion.getDescripcionConceptoAlterna());
                                    ncdpd.setImporteGravado(itemDeduccion.getImporteGravado());
                                    ncdpd.setImporteExcepto(itemDeduccion.getImporteExento());
                                    ncdpd.setIdDeLaPercepcionDeduccion(itemDeduccion.getIdDeduccion());
                                    new NominaComprobanteDescripcionPercepcionDeduccionDaoImpl(conn).insert(ncdpd);
                                }
                            }
                        }catch(Exception e){}
                        try{
                            NominaComprobanteDescripcionPercepcionDeduccion ncdpd2 = new NominaComprobanteDescripcionPercepcionDeduccion();
                            if(cfdiSesion.getListaPercepcion()!=null && cfdiSesion.getListaPercepcion().size()>0){
                                for(PercepcionSesion itemPercepcion : cfdiSesion.getListaPercepcion()){
                                    ncdpd2.setIdCromprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal());
                                    ncdpd2.setIdPercepcionDeduccion(1);
                                    if(String.valueOf(itemPercepcion.getIdNominaTipoPercepcion()).length()==1)
                                        ncdpd2.setTipoClave("00"+itemPercepcion.getIdNominaTipoPercepcion());
                                    else if(String.valueOf(itemPercepcion.getIdNominaTipoPercepcion()).length()==2)
                                        ncdpd2.setTipoClave("0"+itemPercepcion.getIdNominaTipoPercepcion());
                                    else if(String.valueOf(itemPercepcion.getIdNominaTipoPercepcion()).length()==3)
                                        ncdpd2.setTipoClave(""+itemPercepcion.getIdNominaTipoPercepcion());                                        
                                    ncdpd2.setClavePatron(itemPercepcion.getClave());
                                    ncdpd2.setConceptoDescripcion(itemPercepcion.getDescripcionConceptoAlterna());
                                    ncdpd2.setImporteGravado(itemPercepcion.getImporteGravado());
                                    ncdpd2.setImporteExcepto(itemPercepcion.getImporteExento());
                                    ncdpd2.setIdDeLaPercepcionDeduccion(itemPercepcion.getIdPercepcion());
                                    new NominaComprobanteDescripcionPercepcionDeduccionDaoImpl(conn).insert(ncdpd2);
                                }
                            }
                        }catch(Exception e){}
                        
                    }

                }
                
                /*if (mensajeDeError.equals("")){
                    //Si la factura se genero desde un pedido, se actualiza el pedido
                    if (cfdiSesion.getIdPedido()>0){
                        try{
                                SgfensPedido pedidoDto = new SGPedidoBO(cfdiSesion.getIdPedido(),conn).getPedido();
                                if (pedidoDto!=null){
                                    pedidoDto.setIdComprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal());

                                    new SgfensPedidoDaoImpl(conn).update(pedidoDto.createPk(), pedidoDto);
                                }else{
                                    mensajeDeError += "Comprobante generado con incidencias: No se pudo recuperar la información del Pedido asociado a la Factura que se desea generar. Avise a su proveedor.";
                                    System.out.println(" Comprobante generado con incidencias: No se pudo recuperar la información del Pedido asociado a la Factura que se desea generar. Avise a su proveedor.");
                                }

                        }catch(Exception ex){
                            mensajeDeError+= "Comprobante generado con incidencias: No se pudo actualizar el registro de Pedido asociado a la factura." + ex.getCause().toString();
                            System.out.println(" Comprobante generado con incidencias: No se pudo actualizar el registro de Pedido asociado a la factura." + ex.getCause().toString());
                        }
                    }
                }*/
                
                if (mensajeDeError.equals("")){
                    System.out.println("ComprobanteFiscal almacenado satisfactoriamente. <br/><br/><b> UUID de ComprobanteFiscal: " + comprobanteFiscalDto.getUuid() + "</b>");
                    mensajeDeError += "Almacenado correctamente, con ID: "+cf.getIdComprobanteFiscal();
                    generadaCorrectamente = true;
                    idComprobanteGenerado = comprobanteFiscalDto.getIdComprobanteFiscal(); //regresamos el ID de comprobante generado, esto para cargarlo en sesion y que podamos listar unicamente los comprobantes nuevos.
                    //cfdiSesion = new ComprobanteFiscalSesion();
                    //request.getSession().setAttribute("cfdiSesion", cfdiSesion);
                }
                
                /*if (mensajeDeError.equals("")){
                    try{
                        if (enviarCorreo){
                            //ComprobanteFiscalBO comprobanteFiscalBO = new ComprobanteFiscalBO(comprobanteFiscalDto.getIdComprobanteFiscal());
                            out.print("<ul>"+ comprobanteFiscalBO.enviaNotificacionNuevaComprobanteFiscal(listCorreosValidos,archivoXML, archivoPDF));
                        }
                    }catch(Exception ex){
                        out.print("<ul> No se pudo enviar el correo de notificación.");
                        ex.printStackTrace();
                    }
                }*/
            return generadaCorrectamente;
        }
    
    }

