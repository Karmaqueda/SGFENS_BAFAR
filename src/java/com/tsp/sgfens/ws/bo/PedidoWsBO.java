/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.bo;

import com.google.gson.Gson;
import com.tsp.sct.bo.*;
import com.tsp.sct.cfdi.CFDI32Factory;
import com.tsp.sct.cfdi.Cfd32BO;
import com.tsp.sct.config.Configuration;
import com.tsp.sct.dao.dto.*;
import com.tsp.sct.dao.jdbc.BancoOperacionDaoImpl;
import com.tsp.sct.dao.jdbc.ClienteDaoImpl;
import com.tsp.sct.dao.jdbc.FoliosMovilEmpleadoDaoImpl;
import com.tsp.sct.dao.jdbc.ImpuestoDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.dao.jdbc.SgfensCobranzaAbonoDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensCobranzaMetodoPagoDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensPedidoDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensPedidoEntregaDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensPedidoImpuestoDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensPedidoProductoDaoImpl;
import com.tsp.sct.dao.jdbc.TipoPagoDaoImpl;
import com.tsp.sct.util.Base64;
import com.tsp.sct.util.DateManage;
import com.tsp.sct.util.FileManage;
import com.tsp.sct.util.GenericMethods;
import com.tsp.sct.util.GenericValidator;
import com.tsp.sct.util.StringManage;
import com.tsp.sgfens.sesion.ComprobanteFiscalSesion;
import com.tsp.sgfens.sesion.ImpuestoSesion;
import com.tsp.sgfens.sesion.PedidoSesion;
import com.tsp.sgfens.sesion.ProductoSesion;
import com.tsp.sgfens.ws.request.*;
import com.tsp.sgfens.ws.response.*;
import com.tsp.sgfens.ws.response.comprobante.WSComprobanteFiscalDigital;
import com.tsp.sgfens.ws.response.comprobante.WSTimbreFiscalDigital;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import mx.bigdata.sat.cfdi.schema.v32.Comprobante;

/**
 *
 * @author ISCesarMartinez
 */
public class PedidoWsBO {
    
    private final Gson gson = new Gson();
    private Connection conn = null;
    
    public Connection getConn() {
        if (this.conn==null){
            try {
                this.conn = ResourceManager.getConnection();
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
    
    public CrearPedidoResponse crearPedidoByEmpleado(String empleadoDtoRequestJSON, String crearPedidoRequestJSON){
        CrearPedidoResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest;
        CrearPedidoRequest crearPedidoRequest;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            crearPedidoRequest = gson.fromJson(crearPedidoRequestJSON, CrearPedidoRequest.class);
            
            response = this.crearPedidoByEmpleado(empleadoDtoRequest,crearPedidoRequest);
        }catch(Exception ex){
            response = new CrearPedidoResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    public CrearPedidoResponse crearPedidoByEmpleado(EmpleadoDtoRequest empleadoDtoRequest, CrearPedidoRequest crearPedidoRequest){
        CrearPedidoResponse response = new CrearPedidoResponse();
        try{
            String validacion = validaDatosRequest(empleadoDtoRequest, crearPedidoRequest);
            Configuration appConfig = new Configuration();
            GenericValidator validator = new GenericValidator();
            if (validacion.equals("")){
                EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
                if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), 
                        empleadoDtoRequest.getEmpleadoPassword())) {
                    int idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                    
                    String rfcEmpresaMatriz ="";
                    try{
                        Empresa empresaMatrizDto = new EmpresaBO(getConn()).getEmpresaMatriz(idEmpresa);
                        rfcEmpresaMatriz = empresaMatrizDto.getRfc();
                    }catch(Exception ex){
                        response.setError(true);
                        response.setNumError(901);
                        response.setMsgError("No se pudo recuperar la información de la empresa matriz del empleado. Verifique con su administrador de sistema." + ex.toString());
                        return response;
                    }
                    
                    //Antes de procesar, verificamos si no fue registrado previamente,
                    // de acuerdo al dato: Folio Pedido Movil
                    SgfensPedidoDaoImpl pedidoDao = new SgfensPedidoDaoImpl(getConn());
                    try{
                        SgfensPedido[] pedidoDtoExistentes = pedidoDao.findWhereFolioPedidoMovilEquals(crearPedidoRequest.getFolioPedidoMovil());
                        if (pedidoDtoExistentes.length>0){
                            if (pedidoDtoExistentes[0].getIdPedido()>0){
                                                                  
                                System.out.println("________Actualiza");
                                //Estos se actualizaran al enviar el Abono aparte de forma asincrona
                                    //response.setIdAbonoCreado();
                                    //response.setIdBancoOperacionCreado();
                                response.setIdObjetoCreado(pedidoDtoExistentes[0].getIdPedido());
                                response.setFolioPedidoCreado(pedidoDtoExistentes[0].getFolioPedido());
                                
                                //Verificamos si cambio el estatus a Entregado
                                /*if (crearPedidoRequest.getEntregado() == SGEstatusPedidoBO.ESTATUS_ENTREGADO
                                        && ((pedidoDtoExistentes[0].getIdEstatusPedido()==(short)SGEstatusPedidoBO.ESTATUS_PENDIENTE)
                                        || (pedidoDtoExistentes[0].getIdEstatusPedido()==(short)SGEstatusPedidoBO.ESTATUS_ENTREGADO_PARCIAL)  )){*/
                                    //Indica que el pedido ha sido modificado de estatus,
                                    // se marco como Entregado en el movil
                                    // por lo cual se da salida al stock del empleado
                                    SGPedidoBO sGPedidoBO = new SGPedidoBO(pedidoDtoExistentes[0], getConn());
                                    PedidoSesion pedidoSesion = sGPedidoBO.getSessionFromPedidoDB();
                                    //pedidoSesion.salidaAlmacenProductosInventarioEmpleado(empleadoBO.getEmpleado().getIdEmpleado());
                                    
                                                                       
                                    double eventoEntregaTotalCantidad = 0;
                                    double eventoEntregaTotalPeso = 0;
                                    Date fechaEventoEntrega = null;
                                    try{
                                        fechaEventoEntrega = (ZonaHorariaBO.DateZonaHorariaByIdEmpresa(getConn(), new Date(), (int)empleadoBO.getEmpleado().getIdEmpresa()).getTime());
                                    }catch(Exception e){
                                        fechaEventoEntrega = new Date();
                                    }
                                    pedidoSesion.setListaProducto(new ArrayList<ProductoSesion>());
                                    //asignamos todos los conceptos
                                    for (WsItemConceptoRequest wsConcepto : crearPedidoRequest.getWsItemConceptoRequest()){
                                        ProductoSesion nuevoProductoCompraSesion = new ProductoSesion();
                                        nuevoProductoCompraSesion.setCantidad(wsConcepto.getCantidad());
                                        //if (crearPedidoRequest.isVentaGenerica()){
                                        //    ConceptoBO conceptoBO = new ConceptoBO(getConn());
                                        //   Concepto conceptoGenerico = conceptoBO.getConceptoGenericoByEmpresa(idEmpresa);
                                        //    nuevoProductoCompraSesion.setIdProducto(conceptoGenerico.getIdConcepto());
                                        //}else{
                                            nuevoProductoCompraSesion.setIdProducto(wsConcepto.getIdConcepto());
                                        //}
                                        nuevoProductoCompraSesion.setMonto(wsConcepto.getMonto());
                                        nuevoProductoCompraSesion.setPrecio(wsConcepto.getPrecioUnitario());
                                        nuevoProductoCompraSesion.setUnidad(wsConcepto.getUnidad());
                                        if (wsConcepto.getDescripcionAlternativa()!=null){
                                            if (wsConcepto.getDescripcionAlternativa().trim().length()>0)
                                                nuevoProductoCompraSesion.setDescripcionAlternativa(wsConcepto.getDescripcionAlternativa());
                                        }

                                        if(crearPedidoRequest.getEntregado()== SGEstatusPedidoBO.ESTATUS_ENTREGADO //Parcial --> Entregado
                                             && pedidoDtoExistentes[0].getIdEstatusPedido()== SGEstatusPedidoBO.ESTATUS_ENTREGADO_PARCIAL){
                                            nuevoProductoCompraSesion.setCantidadEntregada(wsConcepto.getCantidadEntregada());
                                            nuevoProductoCompraSesion.setCantidadEntregadaPeso(wsConcepto.getCantidadEntregadaPeso());
                                            fechaEventoEntrega = wsConcepto.getFechaEntrega();
                                        }else if(crearPedidoRequest.getEntregado()== SGEstatusPedidoBO.ESTATUS_ENTREGADO //Pendiente --> Entregado
                                             && (pedidoDtoExistentes[0].getIdEstatusPedido()==(short)SGEstatusPedidoBO.ESTATUS_PENDIENTE)){
                                            nuevoProductoCompraSesion.setCantidadEntregada(wsConcepto.getCantidadEntregada());
                                            nuevoProductoCompraSesion.setCantidadEntregadaPeso(wsConcepto.getCantidadEntregadaPeso());
                                            fechaEventoEntrega = wsConcepto.getFechaEntrega();
                                        }else if(crearPedidoRequest.getEntregado()== SGEstatusPedidoBO.ESTATUS_CONSIGNA //Pendiente --> Entregado
                                             && (pedidoDtoExistentes[0].getIdEstatusPedido()==(short)SGEstatusPedidoBO.ESTATUS_CONSIGNA_PARCIAL)){
                                            nuevoProductoCompraSesion.setCantidadEntregada(wsConcepto.getCantidadEntregada());
                                            nuevoProductoCompraSesion.setCantidadEntregadaPeso(wsConcepto.getCantidadEntregadaPeso());
                                            fechaEventoEntrega = wsConcepto.getFechaEntrega();
                                        }else if(crearPedidoRequest.getEntregado()== SGEstatusPedidoBO.ESTATUS_ENTREGADO //Pendiente --> Entregado
                                             && (pedidoDtoExistentes[0].getIdEstatusPedido()==(short)SGEstatusPedidoBO.ESTATUS_CONSIGNA)){
                                            nuevoProductoCompraSesion.setCantidadEntregada(wsConcepto.getCantidadEntregada());
                                            nuevoProductoCompraSesion.setCantidadEntregadaPeso(wsConcepto.getCantidadEntregadaPeso());
                                            fechaEventoEntrega = wsConcepto.getFechaEntrega();
                                        }else{
                                            nuevoProductoCompraSesion.setCantidadEntregada(0);
                                            nuevoProductoCompraSesion.setCantidadEntregadaPeso(0);
                                        }                                        
                                        
                                        nuevoProductoCompraSesion.setFechaEntrega(wsConcepto.getFechaEntrega());
                                        nuevoProductoCompraSesion.setEstatus(wsConcepto.getEstatus());
                                        
                                        //variables para agranel
                                        nuevoProductoCompraSesion.setCantidadPeso(wsConcepto.getCantidadPeso());
                                        nuevoProductoCompraSesion.setIdInventarioEmpleado(wsConcepto.getIdInventarioEmpleado());

                                        pedidoSesion.getListaProducto().add(nuevoProductoCompraSesion);
                                        
                                        //para registro de Evento de Entrega
                                        eventoEntregaTotalCantidad += nuevoProductoCompraSesion.getCantidadEntregada();
                                        eventoEntregaTotalPeso += nuevoProductoCompraSesion.getCantidadEntregadaPeso();
                                    }
                                    //Actualizamos partidas de pedido
                                    SGPedidoBO pedidoBO = new SGPedidoBO(getConn());
                                    UsuarioBO usuarioBO = empleadoBO.getUsuarioBO();
                                    pedidoBO.actualizaProductosPedido(pedidoSesion, usuarioBO);
                                    
                                    //29/01/2016 Cambio Cesar
                                    //Para registrar el Evento de Entrega, solo en caso de aplicar
                                    if (eventoEntregaTotalCantidad>0){
                                        SgfensPedidoEntrega pedidoEntrega = new SgfensPedidoEntrega();
                                        pedidoEntrega.setIdEmpresa(idEmpresa);
                                        pedidoEntrega.setIdEstatus(1);
                                        pedidoEntrega.setIdPedido(pedidoDtoExistentes[0].getIdPedido());
                                        pedidoEntrega.setFechaHora(fechaEventoEntrega);
                                        pedidoEntrega.setIdUsuarioVendedor(empleadoBO.getEmpleado().getIdUsuarios() );
                                        pedidoEntrega.setTotalCantEntregada(eventoEntregaTotalCantidad);
                                        pedidoEntrega.setTotalCantEntregadaPeso(eventoEntregaTotalPeso);
                                        
                                        try{
                                            new SgfensPedidoEntregaDaoImpl(getConn()).insert(pedidoEntrega);
                                        }catch(Exception ex){
                                            System.out.println("Error al crear registro de Evento Entrega: " + GenericMethods.exceptionStackTraceToString(ex) );
                                        }
                                    }
                                    
                                     boolean stockSuficiente = true;// Estatus -> Estatus de pedido , Entregado-> Para control Inventario
                                    
                                   
                                    if (crearPedidoRequest.getEntregado()== SGEstatusPedidoBO.ESTATUS_ENTREGADO //Pendiente --> Entregado
                                             && (pedidoDtoExistentes[0].getIdEstatusPedido()==(short)SGEstatusPedidoBO.ESTATUS_PENDIENTE)){
                                        
                                        stockSuficiente = pedidoSesion.salidaAlmacenProductosInventarioEmpleado(empleadoBO.getEmpleado().getIdEmpleado()); 
                                    }else if(crearPedidoRequest.getEntregado()== SGEstatusPedidoBO.ESTATUS_ENTREGADO //Parcial --> Entregado
                                             && pedidoDtoExistentes[0].getIdEstatusPedido()== SGEstatusPedidoBO.ESTATUS_ENTREGADO_PARCIAL){
                                       
                                        stockSuficiente = pedidoSesion.salidaParcialAlmacenProductosInventarioEmpleado(empleadoBO.getEmpleado().getIdEmpleado()); 
                                    }else if((crearPedidoRequest.getIdEstatus()== SGEstatusPedidoBO.ESTATUS_CANCELADO)
                                            && (pedidoDtoExistentes[0].getIdEstatusPedido()==(short)SGEstatusPedidoBO.ESTATUS_ENTREGADO) ){ //entregado --> cancelado)
                                          
                                            pedidoBO = new SGPedidoBO(pedidoSesion.getIdPedido(), getConn());
                                            pedidoBO.cancelarPedido(true, false);
                                            //regresamos productos a stock de empleado                                           
                                            pedidoSesion.cancelaSalidaAlmacenProductosInventarioEmpleado(empleadoBO.getEmpleado().getIdEmpleado());
                                            
                                    }else if((crearPedidoRequest.getIdEstatus()== SGEstatusPedidoBO.ESTATUS_CANCELADO)
                                            && (pedidoDtoExistentes[0].getIdEstatusPedido()==(short)SGEstatusPedidoBO.ESTATUS_ENTREGADO_PARCIAL) ){ //Parcial --> cancelado)
                                          
                                            pedidoBO = new SGPedidoBO(pedidoSesion.getIdPedido(), getConn());
                                            pedidoBO.cancelarPedido(true, false);
                                            //regresamos productos a stock de empleado                                            
                                            pedidoSesion.cancelaSalidaAlmacenProductosInventarioEmpleado(empleadoBO.getEmpleado().getIdEmpleado());
                                            
                                    }else if(crearPedidoRequest.getEntregado()== SGEstatusPedidoBO.ESTATUS_CONSIGNA //Parcial --> Entregado
                                             && pedidoDtoExistentes[0].getIdEstatusPedido()== SGEstatusPedidoBO.ESTATUS_CONSIGNA_PARCIAL){                                       
                                        stockSuficiente = pedidoSesion.salidaParcialAlmacenProductosInventarioEmpleado(empleadoBO.getEmpleado().getIdEmpleado()); 
                                    }else if(crearPedidoRequest.getEntregado()== SGEstatusPedidoBO.ESTATUS_ENTREGADO //Parcial --> Entregado
                                             && pedidoDtoExistentes[0].getIdEstatusPedido()== SGEstatusPedidoBO.ESTATUS_CONSIGNA){                                       
                                        stockSuficiente = pedidoSesion.salidaParcialAlmacenProductosInventarioEmpleado(empleadoBO.getEmpleado().getIdEmpleado()); 
                                    }
                                    
                                    /*else{
                                        response.setError(true);
                                        response.setNumError(901);
                                        response.setMsgError("Tipo de Entrega no valida Actualiza. IdEstatus: " + crearPedidoRequest.getIdEstatus() );
                                        return response;
                                    }*/
                                    
                                    if(!stockSuficiente){
                                        response.setError(true);
                                        response.setNumError(901);
                                        response.setMsgError("El stock en Inventario del Vendedor es Insuficiente. Contacte a su Administrador.");

                                        return response;
                                    }
                                    
                                    if(crearPedidoRequest.getIdEstatus()!= SGEstatusPedidoBO.ESTATUS_CANCELADO){
                                        //Actualizamos registro de Pedido a estatus
                                        pedidoDtoExistentes[0].setIdEstatusPedido((short)crearPedidoRequest.getEntregado());
                                        pedidoDao.update(pedidoDtoExistentes[0].createPk(), pedidoDtoExistentes[0]);
                                        response.setMsgError("entregado:ok");
                                    }
                                    
                                    ///////MODIFICACION DE TOTALES SOLO PARA CONSIGNA
                                    if(crearPedidoRequest.getIdEstatus() == SGEstatusPedidoBO.ESTATUS_CONSIGNA || crearPedidoRequest.getIdEstatus() == SGEstatusPedidoBO.ESTATUS_CONSIGNA_PARCIAL || ( crearPedidoRequest.getEntregado()== SGEstatusPedidoBO.ESTATUS_ENTREGADO && pedidoDtoExistentes[0].getConsigna() == 2 )){
                                        pedidoDtoExistentes[0].setSubtotal(crearPedidoRequest.getSubtotal());
                                        pedidoDtoExistentes[0].setTotal(crearPedidoRequest.getTotal());
                                        //pedidoDtoExistentes[0].setIdEstatusPedido((short)crearPedidoRequest.getEntregado());
                                        pedidoDao.update(pedidoDtoExistentes[0].createPk(), pedidoDtoExistentes[0]);
                                    }
                                    ///////
                                    
                                    
                                    if(pedidoSesion.getSincronizacionMicrosip()==1){
                                        pedidoDtoExistentes[0].setSincronizacionMicrosip(2);
                                    }

                                    //}
                                
                                //Si cambio el dato de bonificacion devolucion actualizamos
                                if (crearPedidoRequest.getBonificacionDevolucion() != pedidoDtoExistentes[0].getBonificacionDevolucion()){
                                    
                                    //Si el monto a aplicar es una retención (cantidad negativa) no se aplica al saldo
                                    // del cliente, ya que se deberá cobrar en el pedido
                                    if (crearPedidoRequest.getBonificacionDevolucion()>0){
                                        
                                        //actualizamos saldo de cliente
                                        ClienteBO clienteBO = new ClienteBO(pedidoDtoExistentes[0].getIdCliente(), conn);
                                        ClienteDaoImpl clienteDao = new ClienteDaoImpl(conn);
                                        if (clienteBO.getCliente()!=null){
                                            //restaurar saldo anterior (antes de bonificacion por devolucion anterior)
                                            double saldoClienteActual = clienteBO.getCliente().getSaldoCliente();
                                            double saldoClienteAnterior = saldoClienteActual;
                                            if (pedidoDtoExistentes[0].getBonificacionDevolucion()>0){
                                                //restamos a saldo
                                                saldoClienteAnterior = saldoClienteAnterior - pedidoDtoExistentes[0].getBonificacionDevolucion();
                                            }else if (pedidoDtoExistentes[0].getBonificacionDevolucion()<0){
                                                //sumamos a saldo
                                                saldoClienteAnterior = saldoClienteAnterior + pedidoDtoExistentes[0].getBonificacionDevolucion();
                                            }

                                            //aplicamos nueva bonificacion
                                            double saldoClienteNuevo = saldoClienteAnterior + crearPedidoRequest.getBonificacionDevolucion();
                                            clienteBO.getCliente().setSaldoCliente(saldoClienteNuevo);
                                            clienteDao.update(clienteBO.getCliente().createPk(), clienteBO.getCliente());
                                        }
                                        
                                    }
                                    
                                    pedidoDtoExistentes[0].setBonificacionDevolucion(crearPedidoRequest.getBonificacionDevolucion());
                                    pedidoDao.update(pedidoDtoExistentes[0].createPk(), pedidoDtoExistentes[0]);
                                    
                                    response.setMsgError(response.getMsgError() + "|" 
                                            + "bonificaDev:ok");
                                }
                                
                                return response;
                            }
                        }                     
                    }catch(Exception ex){
                        ex.printStackTrace();
                        throw new Exception("Error al intentar actualizar Pedido existente.");
                    }
                     System.out.println("________Nuevo");
                    //Almacenamos Operacion bancaria en caso de que existiera en la venta
                    int idOperacionBancaria = -1;
                    //double totalCobrado = 0;
                    if (crearPedidoRequest.getWsDatosMetodoPagoRequest()!=null){
                        if (crearPedidoRequest.getBancoOperacion()!=null 
                                && crearPedidoRequest.getWsDatosMetodoPagoRequest()
                                        .getMetodoPagoId()==VentaMetodoPagoBO.METODO_PAGO_TDC){
                            try{
                                BancoOperacionDaoImpl bancoOperacionDao = new BancoOperacionDaoImpl(getConn());
                                BancoOperacion bancoOperacionDto = new BancoOperacion();

                                WsBancoOperacionRequest wsBancoOperacionRequest = crearPedidoRequest.getBancoOperacion();
                                if (wsBancoOperacionRequest!=null){

                                    bancoOperacionDto.setBancoAuth(wsBancoOperacionRequest.getBancoAuth());
                                    bancoOperacionDto.setBancoOperFecha(wsBancoOperacionRequest.getBancoOperFecha());
                                    bancoOperacionDto.setBancoOperIssuingBank(wsBancoOperacionRequest.getBancoOperIssuingBank());
                                    bancoOperacionDto.setBancoOperType(wsBancoOperacionRequest.getBancoOperType());
                                    bancoOperacionDto.setBancoOrderId(wsBancoOperacionRequest.getBancoOrderId());
                                    //bancoOperacionDto.setIdEmpresa(wsBancoOperacionRequest.getIdEmpresa());
                                    bancoOperacionDto.setIdEstatus(wsBancoOperacionRequest.getIdEstatus());
                                    //bancoOperacionDto.setIdOperacionBancaria(wsBancoOperacionRequest.getIDO);
                                    bancoOperacionDto.setMonto(wsBancoOperacionRequest.getMonto());
                                    bancoOperacionDto.setNoTarjeta(wsBancoOperacionRequest.getNoTarjeta());
                                    bancoOperacionDto.setNombreTitular(wsBancoOperacionRequest.getNombreTitular());
                                    bancoOperacionDto.setDataArqc(wsBancoOperacionRequest.getDataArqc());
                                    bancoOperacionDto.setDataAid(wsBancoOperacionRequest.getDataAid());
                                    bancoOperacionDto.setDataTsi(wsBancoOperacionRequest.getDataTsi());
                                    bancoOperacionDto.setDataRef(wsBancoOperacionRequest.getDataRef());
                                    bancoOperacionDto.setDataExtra1(wsBancoOperacionRequest.getDataExtra1());
                                    bancoOperacionDto.setDataExtra2(wsBancoOperacionRequest.getDataExtra2());

                                    BancoOperacionBO bancoOperacionBO = new BancoOperacionBO(getConn());
                                    File archivoImagenIFE = bancoOperacionBO.crearArchivoImagenExtraFromBase64(idEmpresa, wsBancoOperacionRequest.getImagenIFEBytesBase64(),true);
                                    File archivoImagenTDC = bancoOperacionBO.crearArchivoImagenExtraFromBase64(idEmpresa, wsBancoOperacionRequest.getImagenTDCBytesBase64(),false);

                                    if (archivoImagenIFE!=null)
                                        bancoOperacionDto.setNombreArchivoImgIfe(archivoImagenIFE.getName());
                                    if (archivoImagenTDC!=null)
                                        bancoOperacionDto.setNombreArchivoImgTdc(archivoImagenTDC.getName());

                                    bancoOperacionDto.setIdEmpresa(idEmpresa);
                                    bancoOperacionDao.insert(bancoOperacionDto);

                                    idOperacionBancaria = bancoOperacionDto.getIdOperacionBancaria();

                                    response.setIdBancoOperacionCreado(idOperacionBancaria);
                                }
                            }catch(Exception ex){
                                System.out.println("***No se pudo almacenar operación bancaria****");
                                ex.toString();
                            }
                        }
                    }
                    
                    ClienteBO clienteBO = new ClienteBO(getConn());
                    int idCliente = -1;
                    if (crearPedidoRequest.isVentaGenerica()){
                        try{
                            idCliente =  clienteBO.getClienteGenericoByEmpresa(idEmpresa).getIdCliente();
                        }catch(Exception ex){
                            response.setError(true);
                            response.setNumError(901);
                            response.setMsgError(ex.toString());
                            System.out.println("Error: " + ex.toString());
                            ex.printStackTrace();
                            return response;
                        }
                    }else{
                        idCliente = crearPedidoRequest.getIdClienteReceptor();
                    }
                    
                    clienteBO = new ClienteBO(idCliente,getConn());
                      
                    //-------------CODIGO NUEVO PARA PRETORIANO MOVIL (TPV) -> PEDIDO
                    UsuarioBO usuarioBO = empleadoBO.getUsuarioBO();
                    PedidoSesion pedidoSesion = new PedidoSesion();
                    pedidoSesion.setConn(getConn());
                    double montoAbonoInicial = crearPedidoRequest.getAbonoInicial();
                    
                    ConceptoBO conceptoBO = new ConceptoBO(getConn());
                    Concepto conceptoGenerico = conceptoBO.getConceptoGenericoByEmpresa(idEmpresa);
                    try{
                        pedidoSesion.setComentarios("Pedido registrado desde aplicación móvil, Folio Pedido Movil: "+ crearPedidoRequest.getFolioPedidoMovil()
                            +". Registrado en servidor: " + (ZonaHorariaBO.DateZonaHorariaByIdEmpresa(getConn(), new Date(), (int)empleadoBO.getEmpleado().getIdEmpresa()).getTime()) );
                    }catch(Exception e){
                    pedidoSesion.setComentarios("Pedido registrado desde aplicación móvil, Folio Pedido Movil: "+ crearPedidoRequest.getFolioPedidoMovil()
                            +". Registrado en servidor: " + new Date());
                    }
                    pedidoSesion.setCliente(clienteBO.getCliente());
                    
                    //pedidoSesion.setListaServicio();
                    //pedidoSesion.setDescuento_motivo();
                    //pedidoSesion.setDescuento_tasa();
                    //pedidoSesion.setTipo_moneda(); //Por defecto "MXN" = pesos mexicanos
                    
                    //pedidoSesion.setIdPedido(); //Auto generado                            
                 //pedidoSesion.setAdelanto(montoAbonoInicial);
                    pedidoSesion.setSaldoPagado(montoAbonoInicial);
                    //pedidoSesion.setIdComprobanteFiscal(); //No se genera con factura en un principio
                    pedidoSesion.setFechaEntrega(crearPedidoRequest.getFechaEntrega());
                    
                    if (crearPedidoRequest.getWsDatosMetodoPagoRequest()!=null){
                        if (crearPedidoRequest.getWsDatosMetodoPagoRequest().getMetodoPagoId()==VentaMetodoPagoBO.METODO_PAGO_CREDITO){
                            pedidoSesion.setFechaTentativaPago(crearPedidoRequest.getWsDatosMetodoPagoRequest().getMetodoPagoCreditoFecha());
                        }else{
                            try{
                                pedidoSesion.setFechaTentativaPago(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(getConn(), new Date(), (int)empleadoBO.getEmpleado().getIdEmpresa()).getTime());
                            }catch(Exception e){
                                pedidoSesion.setFechaTentativaPago(new Date());
                            }
                        }
                    }else{
                        try{
                            pedidoSesion.setFechaTentativaPago(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(getConn(), new Date(), (int)empleadoBO.getEmpleado().getIdEmpresa()).getTime());
                        }catch(Exception e){
                            pedidoSesion.setFechaTentativaPago(new Date());
                        }
                    }
                    
                    if (crearPedidoRequest.getEntregado()==SGEstatusPedidoBO.ESTATUS_ENTREGADO){
                        pedidoSesion.setIdEstatus(SGEstatusPedidoBO.ESTATUS_ENTREGADO);
                    }else if(crearPedidoRequest.getEntregado()==SGEstatusPedidoBO.ESTATUS_ENTREGADO_PARCIAL){
                        pedidoSesion.setIdEstatus(SGEstatusPedidoBO.ESTATUS_ENTREGADO_PARCIAL); 
                    }else if(crearPedidoRequest.getEntregado()==SGEstatusPedidoBO.ESTATUS_CONSIGNA_PARCIAL){
                        pedidoSesion.setIdEstatus(SGEstatusPedidoBO.ESTATUS_CONSIGNA_PARCIAL);
                        pedidoSesion.setConsigna(2);
                    }else if(crearPedidoRequest.getEntregado()==SGEstatusPedidoBO.ESTATUS_CONSIGNA){
                        pedidoSesion.setIdEstatus(SGEstatusPedidoBO.ESTATUS_CONSIGNA); 
                        pedidoSesion.setConsigna(2);
                    }
                    
                    double latitud =0;
                    double longitud =0 ;
                    try{
                        latitud = Double.parseDouble(empleadoDtoRequest.getUbicacionLatitud());
                        longitud =  Double.parseDouble(empleadoDtoRequest.getUbicacionLongitud());
                    }catch(Exception ex){}
                    
                    pedidoSesion.setLatitud(latitud);
                    pedidoSesion.setLongitud(longitud);
                    
                    double eventoEntregaTotalCantidad = 0;
                    double eventoEntregaTotalPeso= 0;
                    //asignamos todos los conceptos
                    for (WsItemConceptoRequest wsConcepto : crearPedidoRequest.getWsItemConceptoRequest()){
                        ProductoSesion nuevoProductoCompraSesion = new ProductoSesion();
                        nuevoProductoCompraSesion.setCantidad(wsConcepto.getCantidad());
                        // MODIFICADO 19-04-2016 SE ELIMINA la condicional para ventas genericas
                        // ya que desde hace mas de 1 año, en el movil, ya no existe la posibilidad
                        // de elegir un producto generico (no existente en BD), por lo tanto
                        // siempre se tienen Productos existenes y no requerimos del uso de uno generico.
                        // Con esto, se logra que el descuento a los productos se realice de manera exitosa
                        // y no se haga el descuento sobre el producto generico.
                        //if (crearPedidoRequest.isVentaGenerica()){
                            //nuevoProductoCompraSesion.setIdProducto(conceptoGenerico.getIdConcepto());
                        //}else{
                            nuevoProductoCompraSesion.setIdProducto(wsConcepto.getIdConcepto());
                        //}
                        nuevoProductoCompraSesion.setMonto(wsConcepto.getMonto());
                        nuevoProductoCompraSesion.setPrecio(wsConcepto.getPrecioUnitario());
                        nuevoProductoCompraSesion.setUnidad(wsConcepto.getUnidad());
                        if (wsConcepto.getDescripcionAlternativa()!=null){
                            if (wsConcepto.getDescripcionAlternativa().trim().length()>0)
                                nuevoProductoCompraSesion.setDescripcionAlternativa(wsConcepto.getDescripcionAlternativa());
                        }
                        
                        nuevoProductoCompraSesion.setCantidadEntregada(wsConcepto.getCantidadEntregada());
                        nuevoProductoCompraSesion.setFechaEntrega(wsConcepto.getFechaEntrega());
                        nuevoProductoCompraSesion.setEstatus(wsConcepto.getEstatus());
                        
                        //variables para agranel
                        nuevoProductoCompraSesion.setCantidadPeso(wsConcepto.getCantidadPeso());
                        nuevoProductoCompraSesion.setCantidadEntregadaPeso(wsConcepto.getCantidadEntregadaPeso());
                        nuevoProductoCompraSesion.setIdInventarioEmpleado(wsConcepto.getIdInventarioEmpleado());
                        
                        pedidoSesion.getListaProducto().add(nuevoProductoCompraSesion);
                        
                        eventoEntregaTotalCantidad += wsConcepto.getCantidadEntregada();
                        eventoEntregaTotalPeso += wsConcepto.getCantidadEntregadaPeso();
                    }
                    
                    //Verificamos si hay impuestos por agregar
                    if (crearPedidoRequest.getListaImpuestos()!=null){
                        
                        /**
                        * BORRAR CUANDO SE IMPLEMENTE CATALOGO DE IMPUESTOS EN MOVIL
                        */
                        if (crearPedidoRequest.getListaImpuestos().length>0){
                            ImpuestoBO impuestoBO = new ImpuestoBO(getConn());
                            Impuesto impuestoIVA16 = impuestoBO.findImpuestoIVA16byEmpresa(idEmpresa);
                            WsItemImpuestoRequest[] wsItemImpuestoRequestArrayTemp = new WsItemImpuestoRequest[1];
                            wsItemImpuestoRequestArrayTemp[0] = new WsItemImpuestoRequest();
                            wsItemImpuestoRequestArrayTemp[0].setDescripcion(impuestoIVA16.getDescripcion());
                            wsItemImpuestoRequestArrayTemp[0].setIdImpuesto(impuestoIVA16.getIdImpuesto());
                            wsItemImpuestoRequestArrayTemp[0].setImplocal(impuestoIVA16.getImpuestoLocal()==(short)1);
                            wsItemImpuestoRequestArrayTemp[0].setNombre(impuestoIVA16.getNombre());
                            wsItemImpuestoRequestArrayTemp[0].setPorcentaje(impuestoIVA16.getPorcentaje());
                            wsItemImpuestoRequestArrayTemp[0].setTrasladado(impuestoIVA16.getTrasladado()==(short)1);

                            crearPedidoRequest.setListaImpuestos(wsItemImpuestoRequestArrayTemp);
                        }
                        //------------BORRAR HASTA AQUI
                        
                        //asignamos todos los impuestos
                        for (WsItemImpuestoRequest wsImpuesto : crearPedidoRequest.getListaImpuestos()){
                            ImpuestoSesion nuevoImpuestoCompraSesion = new ImpuestoSesion();
                            nuevoImpuestoCompraSesion.setIdImpuesto(wsImpuesto.getIdImpuesto());
                            nuevoImpuestoCompraSesion.setNombre(wsImpuesto.getNombre());
                            nuevoImpuestoCompraSesion.setDescripcion(wsImpuesto.getDescripcion());
                            nuevoImpuestoCompraSesion.setPorcentaje(wsImpuesto.getPorcentaje());
                            nuevoImpuestoCompraSesion.setTrasladado(wsImpuesto.isTrasladado());
                            nuevoImpuestoCompraSesion.setImplocal(wsImpuesto.isImplocal());
                            
                            pedidoSesion.getListaImpuesto().add(nuevoImpuestoCompraSesion);
                        }
                    }
                    
                    //Registramos salida de almacen de todos los productos solo en caso de no ser Venta Rápida
                    //if (!crearPedidoRequest.isVentaGenerica())
                        //pedidoSesion.salidaAlmacenProductos();
                    
                    //MODIFICADO 8-Jul-2014: Dabamos salida de almacen general, ahora solo se da de baja del inventario propio del vendedor
                    //Ahora solo damos salida del inventario del vendedor si es que el pedido
                    // NO es una venta genérica y que ademas tenga estatus de entregado o entregado parcial
                    boolean stockSuficiente = true;
                    
                    
                    System.out.println("________estatus:" + crearPedidoRequest.getIdEstatus() );
                    // MODIFICADO 19-04-2016 Ahora, aunque sea venta generica se descuenta de inventario de Empleado
                    //if (!crearPedidoRequest.isVentaGenerica()){
                        if (crearPedidoRequest.getIdEstatus()== SGEstatusPedidoBO.ESTATUS_ENTREGADO ){
                             stockSuficiente = pedidoSesion.salidaAlmacenProductosInventarioEmpleado(empleadoBO.getEmpleado().getIdEmpleado()); 
                        }else if(crearPedidoRequest.getIdEstatus()== SGEstatusPedidoBO.ESTATUS_ENTREGADO_PARCIAL){
                            stockSuficiente = pedidoSesion.salidaParcialAlmacenProductosInventarioEmpleado(empleadoBO.getEmpleado().getIdEmpleado()); 
                        }else if(crearPedidoRequest.getIdEstatus()== SGEstatusPedidoBO.ESTATUS_CONSIGNA){
                            stockSuficiente = pedidoSesion.salidaParcialAlmacenProductosInventarioEmpleado(empleadoBO.getEmpleado().getIdEmpleado()); 
                        }else if(crearPedidoRequest.getIdEstatus()== SGEstatusPedidoBO.ESTATUS_CONSIGNA_PARCIAL){
                            stockSuficiente = pedidoSesion.salidaParcialAlmacenProductosInventarioEmpleado(empleadoBO.getEmpleado().getIdEmpleado()); 
                        }
                    //}
                    
                    if(!stockSuficiente){
                        response.setError(true);
                        response.setNumError(901);
                        response.setMsgError("El stock en Inventario del Vendedor es Insuficiente. Contacte a su Administrador.");
                        
                        return response;
                    }
                    
                    
                    pedidoSesion.setFolioPedidoMovil(crearPedidoRequest.getFolioPedidoMovil());         
                    
                    SGPedidoBO pedidoBO = new SGPedidoBO(getConn());
                    
                    //Se guarda la imagen
                    try{
                        if(crearPedidoRequest.getWsDatosMetodoPagoRequest().getImagenFirmaBytesBase64()!= null)
                        {
                            if(!crearPedidoRequest.getWsDatosMetodoPagoRequest().getImagenFirmaBytesBase64().equals("")){
                                File firmaEntregado = pedidoBO.crearArchivoImagenExtraFromBase64(idEmpresa, crearPedidoRequest.getWsDatosMetodoPagoRequest().getImagenFirmaBytesBase64());
                                System.out.println("RUTA DE LA IMAGEN:\n" + firmaEntregado.getPath());
                                pedidoSesion.setRutaImagen(firmaEntregado.getPath());
                            }
                        }
                    }catch(Exception ex){
                        System.out.println("Error en la imagen: " + ex.getMessage() );
                    }
                    
                    
                    SgfensPedido pedidoDto = pedidoBO.creaActualizaPedido(pedidoSesion, usuarioBO, crearPedidoRequest.getFechaPedido(), false);
                    
                    pedidoDto.setBonificacionDevolucion(crearPedidoRequest.getBonificacionDevolucion());
                    pedidoDto.setIdFolioMovilEmpleado(crearPedidoRequest.getIdServidorFolioMovilEmpleado());
                    pedidoDto.setFolioMovilEmpleadoGenerado(crearPedidoRequest.getFolioMovilEmpleadoGenerado());
                    pedidoDao.update(pedidoDto.createPk(), pedidoDto);
                    
                    //29/01/2016 Cambio Cesar
                    //Para registrar el Evento de Entrega, solo en caso de aplicar
                    if (eventoEntregaTotalCantidad>0){
                        SgfensPedidoEntrega pedidoEntrega = new SgfensPedidoEntrega();
                        pedidoEntrega.setIdEmpresa(idEmpresa);
                        pedidoEntrega.setIdEstatus(1);
                        pedidoEntrega.setIdPedido(pedidoDto.getIdPedido());
                        pedidoEntrega.setFechaHora(pedidoDto.getFechaPedido());
                        pedidoEntrega.setIdUsuarioVendedor(empleadoBO.getEmpleado().getIdUsuarios() );
                        pedidoEntrega.setTotalCantEntregada(eventoEntregaTotalCantidad);
                        pedidoEntrega.setTotalCantEntregadaPeso(eventoEntregaTotalPeso);

                        try{
                            new SgfensPedidoEntregaDaoImpl(getConn()).insert(pedidoEntrega);
                        }catch(Exception ex){
                            System.out.println("Error al crear registro de Evento Entrega: " + GenericMethods.exceptionStackTraceToString(ex) );
                        }
                    }
                    
                    //actualizamos Folio interno Movil empleado
                    try{
                        if (crearPedidoRequest.getIdServidorFolioMovilEmpleado()>0){
                            FoliosMovilEmpleado foliosMovilEmpleadoDto = new FoliosMovilEmpleadoBO(crearPedidoRequest.getIdServidorFolioMovilEmpleado(), getConn()).getFoliosMovilEmpleado();
                            int creadoMovil = Integer.parseInt(crearPedidoRequest.getFolioMovilEmpleadoGenerado());
                            if (creadoMovil > foliosMovilEmpleadoDto.getUltimoFolio()){
                                //solo si es mayor a lo que se tiene actualmente, lo asignamos
                                foliosMovilEmpleadoDto.setUltimoFolio(creadoMovil);
                                new FoliosMovilEmpleadoDaoImpl(getConn()).update(foliosMovilEmpleadoDto.createPk(), foliosMovilEmpleadoDto);
                            }
                        }
                    }catch(Exception ex){
                        System.out.println("Error al actualizar Folio interno para empleado (serie): " + ex.getMessage() );
                    }
                    
                    response.setFolioPedidoCreado(pedidoDto.getFolioPedido());
                    
                     //Enviar notificación con PDF de pedido adjunto
                    if (crearPedidoRequest.isEnviarCorreoPedido()){
                        pedidoBO = new SGPedidoBO(pedidoDto,getConn());
                        try{
                            File pedidoPDF = pedidoBO.guardarRepresentacionImpresa(usuarioBO);
                            File[] adjuntos = new File[1];
                            adjuntos[0] = pedidoPDF;
                            
                            ArrayList<String> listaCorreos = new ArrayList<String>();
                            listaCorreos.addAll(Arrays.asList(crearPedidoRequest.getListaCorreosDestinatarios()));
                            
                            pedidoBO.enviaNotificacionNuevaPedido(listaCorreos,adjuntos,idEmpresa);
                        }catch(Exception ex){
                            System.out.print("No se pudo adjuntar y/o enviar la representación impresa del pedido." + ex.toString());
                            ex.printStackTrace();
                        }
                    }
                    
                    
                    if (crearPedidoRequest.getWsDatosMetodoPagoRequest()!=null){
                        //si el pedido no fue registrado con metodo de pago A Crédito, se registra el primer abono
                        if (crearPedidoRequest.getWsDatosMetodoPagoRequest().getMetodoPagoId()!= VentaMetodoPagoBO.METODO_PAGO_CREDITO){

                            File archivoImagenFirma = null;
                            if (crearPedidoRequest.getWsDatosMetodoPagoRequest().getImagenFirmaBytesBase64()!=null){
                                if (crearPedidoRequest.getWsDatosMetodoPagoRequest().getImagenFirmaBytesBase64().trim().length()>0){
                                    try{
                                        //Convertimos bytes en base64 a una imagen y la almacenamos en servidor
                                        byte[] bytesImagenFirma = Base64.decode(crearPedidoRequest.getWsDatosMetodoPagoRequest().getImagenFirmaBytesBase64());

                                        String ubicacionImagenesFirmaAbonos = appConfig.getApp_content_path() + rfcEmpresaMatriz +"/abonos/images/";
                                        String nombreArchivoImagenAbono = "img_firma_"+DateManage.getDateHourString()+".jpg";

                                        archivoImagenFirma = FileManage.createFileFromByteArray(bytesImagenFirma, ubicacionImagenesFirmaAbonos, nombreArchivoImagenAbono);
                                    }catch(Exception ex){
                                        System.out.println("");
                                        ex.printStackTrace();
                                    }
                                }
                            }

                            SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(getConn());
                            String identificadorOperacion = crearPedidoRequest.getWsDatosMetodoPagoRequest().getMetodoPagoDocumentoNumero();
                            identificadorOperacion = identificadorOperacion!=null?(identificadorOperacion.trim().equals("null")?"":identificadorOperacion.trim()):"";
                            String comentarios = "Abono registrado desde aplicativo móvil, Folio Cobranza Móvil: " + crearPedidoRequest.getAbonoInicialFolioCobranzaMovil() + " .";
                            int idAbono = cobranzaAbonoBO.registrarAbono(usuarioBO.getUser(), 
                                    pedidoDto.getIdPedido(), 
                                    0, //Id Comprobante Fiscal
                                    montoAbonoInicial,  //Monto Abono
                                    crearPedidoRequest.getWsDatosMetodoPagoRequest().getMetodoPagoId(), //ID Metodo Pago
                                    identificadorOperacion,  //Identificador Operacion (pago con documento)
                                    comentarios, //Comentarios
                                    idOperacionBancaria,// ID Operacion Bancaria
                                    latitud, longitud, //Latitud, Longitud
                                    archivoImagenFirma, //Archivo imagen firma 
                                    crearPedidoRequest.getAbonoInicialFolioCobranzaMovil(), //Folio Cobranza Abono inicial
                                    crearPedidoRequest.getFechaPedido(), //Fecha  y Hr de captura de pedido
                                    false,
                                    crearPedidoRequest.getReferencia()!=null?crearPedidoRequest.getReferencia():"",
                                    crearPedidoRequest.getReferencia()!=null?new Date():null); //Capturado en consola? (FALSE)

                            response.setIdAbonoCreado(idAbono);

                            try{
                                //Enviamos correo en caso de pago con tarjeta de credito/debito con boucher y firma
                                if (idOperacionBancaria>0 && crearPedidoRequest.isEnviarCorreoReciboPago()){
                                    BancoOperacionBO bancoOpBO = new BancoOperacionBO(getConn());

                                    String destinatarios = "";
                                    if (crearPedidoRequest.getListaCorreosDestinatarios()!=null){
                                        if (crearPedidoRequest.getListaCorreosDestinatarios().length>0){
                                            for (String dest : crearPedidoRequest.getListaCorreosDestinatarios()){
                                                if (validator.isEmail(dest)){
                                                    destinatarios += dest + ",";
                                                }else{
                                                    response.setMsgError(response.getMsgError() + ". El destinarario '" + dest + "' no es válido.");
                                                }
                                            }

                                            if (destinatarios.trim().length()>0){
                                                bancoOpBO.enviarTicket(destinatarios, idOperacionBancaria, idEmpresa);
                                            }else{
                                                response.setMsgError(response.getMsgError() + ". No se envío boucher de pago "
                                                        + "ya que no se expreso al menos 1 e-mail destinatario correcto.");
                                            }
                                        }
                                    }

                                }
                            }catch(Exception ex){
                                response.setMsgError("El correo de boucher de pago no se pudo enviar." + ex.toString());
                            }

                        }
                    }
                    
                    response.setIdObjetoCreado(pedidoDto.getIdPedido());
                    response.setError(false);
                    
                    
                    //registramos ubicacion
                    try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
                
                } else {
                    response.setError(true);
                    response.setNumError(901);
                    response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                }
            }else{
                response.setError(true);
                response.setNumError(901);
                response.setMsgError(validacion);
            }
        }catch (Exception e) {
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error al intentar almacenar la venta. La descripción del error es: " + e.toString());
            e.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
        
    }
    
    /**
     * Válida los datos Request para solicitar la creación de un Pedido
     * @param empleadoDtoRequest Datos de acceso de empleado
     * @param crearPedidoRequest Datos e información de pedido y abono inicial
     * @return 
     */
    private String validaDatosRequest(EmpleadoDtoRequest empleadoDtoRequest, CrearPedidoRequest crearPedidoRequest){
        String msgError="";
        
        if (empleadoDtoRequest==null)
            msgError = "Datos de empleado nulos. Se requieren datos para autenticar operación.";
        
        if (crearPedidoRequest.getIdClienteReceptor()<=0 && !crearPedidoRequest.isVentaGenerica())
            msgError = "El cliente específicado no es válido";
        
        if (crearPedidoRequest.getWsItemConceptoRequest()==null){
            if (!crearPedidoRequest.isVentaGenerica())
                msgError = "La lista de Productos/Conceptos es nula";
        }else{
            if (crearPedidoRequest.getWsItemConceptoRequest().length<=0)
                msgError =  "La lista de Productos/Conceptos esta vacía. Al menos debe haber un producto.";
        }
        
        //if (crearPedidoRequest.getAbonoInicial() > (crearPedidoRequest.getTotal() + crearPedidoRequest.getVentaGenericaImpuestos()) ){
        if (crearPedidoRequest.getAbonoInicial() > crearPedidoRequest.getTotal() ){
            msgError = "El abono inicial no puede ser mayor al Total de la venta.";
        }
        
        boolean pagoCredito = false;
        boolean sinPagoInicial = false;
        /*
        if (crearPedidoRequest.getWsDatosMetodoPagoRequest()!=null){
            if (crearPedidoRequest.getWsDatosMetodoPagoRequest().getMetodoPagoId() 
                    == VentaMetodoPagoBO.METODO_PAGO_CREDITO){
                pagoCredito = true;
            }else{
                //Pedido levantado, sin surtir, puede ir sin un pago inicial
                if (!crearPedidoRequest.isEntregado()){
                    sinPagoInicial = true;
                }
            }
        }
        
        //Si no es un pago a credito y es Pedido levantado, sin surtir, puede ir sin un pago inicial
        if (!pagoCredito && !crearPedidoRequest.isEntregado()){
            sinPagoInicial = true;
        }*/
        
        if (!sinPagoInicial){
            if (crearPedidoRequest.getWsDatosMetodoPagoRequest()==null){
                //msgError = "La información del método de pago no puede ser nula.";
            }else{
                switch (crearPedidoRequest.getWsDatosMetodoPagoRequest().getMetodoPagoId()) {

                    case VentaMetodoPagoBO.METODO_PAGO_TDC:
                        if (crearPedidoRequest.getBancoOperacion()==null){
                            msgError = "Si eligio pago con Tarjeta, debe específicar los datos de la operación bancaria efectuada Valor nulo.";
                        }
                        break;

                    case VentaMetodoPagoBO.METODO_PAGO_EFECTIVO:
                        break;

                    case VentaMetodoPagoBO.METODO_PAGO_DOCUMENTO:
                        if (crearPedidoRequest.getWsDatosMetodoPagoRequest().getMetodoPagoDocumentoNumero()==null){
                            msgError = "Se eligio pago con documento, y no se específico un numero/identificador de doc. válido. Valor nulo.";
                        }else if(crearPedidoRequest.getWsDatosMetodoPagoRequest().getMetodoPagoDocumentoNumero().trim().equals("")){
                            msgError = "Se eligio pago con documento, y no se específico un numero/identificador de doc. válido. Valor vacío.";
                        }
                        break;

                    case VentaMetodoPagoBO.METODO_PAGO_CREDITO:
                        if (crearPedidoRequest.isVentaGenerica()){
                            msgError = "El pago a crédito no se permite en una venta rápida/génerica.";
                            break;
                        }
                        if (crearPedidoRequest.getWsDatosMetodoPagoRequest().getMetodoPagoCreditoFecha()==null){
                            msgError = "No se específico una fecha válida para el pago del Crédito.";
                            break;
                        }
                        try{
                            //Date fechaPagoCredito = DateManage.stringToDate(crearPedidoRequest.getWsDatosMetodoPagoRequest().getMetodoPagoCreditoFecha());
                            Date fechaPagoCredito = crearPedidoRequest.getWsDatosMetodoPagoRequest().getMetodoPagoCreditoFecha();
                            if (fechaPagoCredito == null){
                                msgError = "La fecha para el pago del Crédito tiene un formato incorrecto. ";
                                break;
                            }

                            /*//Se omite por que puede crear problemas al actualizar pedidos ya existentes
                            if (fechaPagoCredito.before(new Date())){
                                msgError = "La fecha para el pago del crédito específicada no puede ser anterior a la fecha actual.";
                            }*/
                        }catch(Exception ex){
                            msgError = "La fecha para el pago del Crédito tiene un formato incorrecto.";
                            break;
                        }
                        break;
                        
                    case VentaMetodoPagoBO.METODO_PAGO_BONIFICACION:
                        break;
                    
                    default:
                        //msgError = "No se específico un método de pago válido.";
                        break;

                }
            }
        }
        
        return msgError;
    }
    
    
    public WSResponse cancelarPedido(String empleadoDtoRequestJSON, String cancelarPedidoRequestJSON){
        WSResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest;
        CancelarPedidoRequest cancelarPedidoRequest;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            cancelarPedidoRequest = gson.fromJson(cancelarPedidoRequestJSON, CancelarPedidoRequest.class);
            
            response = this.cancelarPedido(empleadoDtoRequest,cancelarPedidoRequest);
        }catch(Exception ex){
            response = new WSResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    public WSResponse cancelarPedido(EmpleadoDtoRequest empleadoDtoRequest, CancelarPedidoRequest cancelarPedidoRequest ){
        WSResponse response = new WSResponse();
        
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), 
                    empleadoDtoRequest.getEmpleadoPassword())) {
                
                if (cancelarPedidoRequest.getIdServerPedido()>0){
                    
                    SGPedidoBO pedidoBO = new SGPedidoBO(cancelarPedidoRequest.getIdServerPedido(),getConn());
                    PedidoSesion pedidoSesion = pedidoBO.getSessionFromPedidoDB();
                    if (pedidoBO.getPedido()!=null){
                        
                        if(pedidoBO.getPedido().getIdEstatusPedido()!=3){//Si no esta cancelado previamnete        
                            
                            pedidoBO.cancelarPedido(cancelarPedidoRequest.isCancelarAbonosPedido(), false);
                            //regresamos productos a stock de empleado
                            pedidoSesion.cancelaSalidaAlmacenProductosInventarioEmpleado(empleadoBO.getEmpleado().getIdEmpleado());
                        
                        }
                            
                    }else{
                        response.setError(true);
                        response.setNumError(902);
                        response.setMsgError("El pedido indicado no existe en el servidor o no se pudo recuperar.");
                        return response; 
                    }
                    
                }else{
                    response.setError(true);
                    response.setNumError(901);
                    response.setMsgError("No se indico el ID del pedido a cancelar en servidor.");
                    return response; 
                }
                
                
            }else{
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                return response;
            }
        }catch(Exception ex){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error al intentar cancelar Pedido. La descripción del error es: " + ex.toString());
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
                
        return response;
    }
    
    
    public ModificarPedidoResponse modificaPedido(String empleadoDtoRequestJSON, String modificarPedidoRequestJSON){
        ModificarPedidoResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest;
        ModificarPedidoRequest modificarPedidoRequest;
        try{
            //Transformamos de formato JSON a objeto 
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            modificarPedidoRequest = gson.fromJson(modificarPedidoRequestJSON, ModificarPedidoRequest.class);
            
            response = this.modificaPedido(empleadoDtoRequest,modificarPedidoRequest);
        }catch(Exception ex){
            response = new ModificarPedidoResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    public ModificarPedidoResponse modificaPedido(EmpleadoDtoRequest empleadoDtoRequest, ModificarPedidoRequest modificarPedidoRequest){
        ModificarPedidoResponse response = new ModificarPedidoResponse();
        
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), 
                    empleadoDtoRequest.getEmpleadoPassword())) {
               
                if (modificarPedidoRequest.getIdServerPedido()<=0){
                    response.setError(true);
                    response.setNumError(901);
                    response.setMsgError("El ID de pedido específicado es inválido. Debe corresponder a uno registrado previamente en el servidor y ser mayor a 0.");
                    return response;
                }
                
                SgfensPedido  pedidoDto = null;
                if (modificarPedidoRequest.getIdEstatusPedido()==SGEstatusPedidoBO.ESTATUS_PENDIENTE
                        || modificarPedidoRequest.getIdEstatusPedido()==SGEstatusPedidoBO.ESTATUS_ENTREGADO
                        || modificarPedidoRequest.getIdEstatusPedido()==SGEstatusPedidoBO.ESTATUS_CANCELADO
                        || modificarPedidoRequest.getIdEstatusPedido()==SGEstatusPedidoBO.ESTATUS_ENTREGADO_PARCIAL){
                    
                    SGPedidoBO pedidoBO = new SGPedidoBO(modificarPedidoRequest.getIdServerPedido(),getConn());
                    
                    if (pedidoBO.getPedido()!=null){       
                        try{
                            /*
                            ArrayList<SgfensPedidoProducto> listaProducto = new ArrayList<SgfensPedidoProducto>();
                                    
                            //asignamos todos los conceptos entregados                   
                            SgfensPedidoProductoDaoImpl pedidoProdDao = new SgfensPedidoProductoDaoImpl();
                            
                            for (WsItemConceptoRequest wsConcepto : modificarPedidoRequest.getWsItemConceptoRequest()){
                                
                                //Buscamos concepto en pedido
                                SgfensPedidoProducto pedidoProductoDto = pedidoProdDao.findByPrimaryKey(modificarPedidoRequest.getIdServerPedido() , wsConcepto.getIdConcepto());
                                //Seteamos nuevos valores
                                double totalEntregado = pedidoProductoDto.getCantidadEntregada() + wsConcepto.getCantidadEntregada();
                                //Si, se excede el total de articulos de la venta
                                if(totalEntregado > pedidoProductoDto.getCantidad()){
                                    response.setError(true);
                                    response.setNumError(901);
                                    response.setMsgError("Ya se han entregado todos los conceptos para este pedido. Id pedido :" + pedidoBO.getPedido().getIdPedido() );
                                    return response;
                                }
                                
                                // valores para pedidoProducto        
                                pedidoProductoDto.setCantidadEntregada(totalEntregado);
                                pedidoProductoDto.setFechaEntrega(wsConcepto.getFechaEntrega());
                                pedidoProductoDto.setEstatus((short)wsConcepto.getEstatus());

                                //Actualizamos bd                            
                                new SgfensPedidoProductoDaoImpl(getConn()).update(pedidoProductoDto.createPk(), pedidoProductoDto);
                                //seteamos nuevamnete para Inventario vendedor y lo agregamoa a la lista , una vez actualizado pedidoProd, 
                                pedidoProductoDto.setCantidadEntregada(wsConcepto.getCantidadEntregada());
                                listaProducto.add(pedidoProductoDto);
                            }

                            //Descontamos de Almacen de vendedor
                            pedidoBO.actualizaSalidaAlmacenProductosInventarioEmpleado(empleadoBO.getEmpleado().getIdEmpleado() , listaProducto);
                            */
                            
                            //actualizamos estatus del pedido
                            pedidoBO.getPedido().setIdEstatusPedido((short)modificarPedidoRequest.getIdEstatusPedido());
                            new SgfensPedidoDaoImpl(getConn()).update(pedidoBO.getPedido().createPk(), pedidoBO.getPedido());
                            
                            response.setPedidoActualizado(true);
                            
                            pedidoDto = pedidoBO.getPedido();
                            
                        }catch(Exception e){
                            response.setError(true);
                            response.setNumError(902);
                            response.setMsgError("Error al intentar actualizar Pedido. La descripción del error es: " + e.toString());
                            e.printStackTrace();
                            return response;
                        };
                        
                    }else{
                        response.setError(true);
                        response.setNumError(901);
                        response.setMsgError("No se encontro ningun pedido con el ID específicado. ID: " + modificarPedidoRequest.getIdServerPedido());
                        return response;
                    }
                    
                }else{
                    response.setError(true);
                    response.setNumError(901);
                    response.setMsgError("El nuevo estatus indicado para el pedido no es válido.");
                    return response;
                }
                
                
                //Obtenemos el nuevo saldo pagado actual del pedido
                try{
                    int idPedido = modificarPedidoRequest.getIdServerPedido();
                    BigDecimal pagadoActual = new SGCobranzaAbonoBO(getConn()).getSaldoPagadoPedido(idPedido);
                    response.setSaldoPagadoPedido(pagadoActual.doubleValue());
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                
                //Consumo de Creditos Operacion
                if (modificarPedidoRequest.getIdEstatusPedido()==SGEstatusPedidoBO.ESTATUS_ENTREGADO){
                    //Solo si el nuevo estatus es Entregado
                    try{
                        BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(getConn());
                        bcoBO.registraDescuento(empleadoBO.getUsuarioBO().getUser(), 
                                BitacoraCreditosOperacionBO.CONSUMO_ACCION_WS_REGISTRO_ENTREGA, 
                                null, pedidoDto.getIdCliente(), 0, pedidoDto.getTotal(), 
                                "Registro WS Entrega de Pedido", null, true);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
                
            }else{
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                return response;
            }
        }catch(Exception ex){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error al intentar modificar Pedido. La descripción del error es: " + ex.toString());
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    public CrearAbonoPedidoResponse registraAbonoPedido(String empleadoDtoRequestJSON, String registrarAbonoRequestJSON){
        CrearAbonoPedidoResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest;
        RegistrarAbonoRequest registrarAbonoRequest;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            registrarAbonoRequest = gson.fromJson(registrarAbonoRequestJSON, RegistrarAbonoRequest.class);
            
            response = this.registraAbonoPedido(empleadoDtoRequest,registrarAbonoRequest);
        }catch(Exception ex){
            response = new CrearAbonoPedidoResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    public CrearAbonoPedidoResponse registraAbonoPedido(EmpleadoDtoRequest empleadoDtoRequest, RegistrarAbonoRequest registrarAbonoRequest){
        CrearAbonoPedidoResponse response = new CrearAbonoPedidoResponse();
        try{
            String validacion = validaDatosAbonoRequest(empleadoDtoRequest, registrarAbonoRequest);
            if (validacion.trim().equals("")){
                EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
                    if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), 
                            empleadoDtoRequest.getEmpleadoPassword())) {
                        int idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                        
                        UsuarioBO usuarioBO = empleadoBO.getUsuarioBO();
                        Configuration appConfig = new Configuration();
                        
                        String rfcEmpresaMatriz ="";
                        try{
                            Empresa empresaMatrizDto = new EmpresaBO(getConn()).getEmpresaMatriz(idEmpresa);
                            rfcEmpresaMatriz = empresaMatrizDto.getRfc();
                        }catch(Exception ex){
                            response.setError(true);
                            response.setNumError(901);
                            response.setMsgError("No se pudo recuperar la información de la empresa matriz del empleado. Verifique con su administrador de sistema." + ex.toString());
                            return response;
                        }


                        SGPedidoBO sgPedidoBO = new SGPedidoBO(getConn());
                        SgfensPedido pedidoDto = sgPedidoBO.findPedidobyId(registrarAbonoRequest.getIdServerPedido());
                        
                        SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(getConn());

                        double adeudo = cobranzaAbonoBO.getSaldoAdeudoPedido(pedidoDto.getIdPedido()).doubleValue();
                        double pagado = cobranzaAbonoBO.getSaldoPagadoPedido(pedidoDto.getIdPedido()).doubleValue();
                        double totalPedido = pedidoDto.getTotal();
                                
                        response.setSaldoPagadoPedido(pagado);

                        //Antes de procesar, verificamos si no fue registrado previamente,
                        // de acuerdo al dato: Folio Cobranza Movil. 
                        //Para dar tolerancia a fallos por desconexion de movil.
                        try{
                            SgfensCobranzaAbonoDaoImpl cobranzaDao = new SgfensCobranzaAbonoDaoImpl(getConn());
                            SgfensCobranzaAbono[] cobranzaDtoExistentes = cobranzaDao.findWhereFolioCobranzaMovilEquals(registrarAbonoRequest.getFolioCobranzaMovil());
                            if (cobranzaDtoExistentes.length>0){
                                if (cobranzaDtoExistentes[0].getIdPedido()>0){
                                      //Ya fue registrado previamente
                                    
                                    // Actualización de registro por referencia ************
                                    if (!StringManage.getValidString(registrarAbonoRequest.getReferencia()).equals("") ){
                                        
                                        cobranzaDtoExistentes[0].setReferencia(registrarAbonoRequest.getReferencia());
                                        try{
                                            cobranzaDtoExistentes[0].setFechaActualizaReferencia(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(getConn(), new Date(), (int)empleadoBO.getEmpleado().getIdEmpresa()).getTime());
                                        }catch(Exception e){
                                            cobranzaDtoExistentes[0].setFechaActualizaReferencia(new Date());
                                        }
                                            

                                        try{ 
                                            cobranzaDao.update(cobranzaDtoExistentes[0].createPk(), cobranzaDtoExistentes[0]); 
                                            response.setMsgError("referencia:ok");
                                        }catch(Exception e){
                                            response.setError(true);
                                            response.setNumError(902);
                                            response.setMsgError("Error en servidor al actualizar registro de cobranza con Referencia." + e.toString());
                                            return response;
                                        }
                                    }
                        
                                    //retornamos obj ya creado
                                    response.setSaldoPagadoPedido(pagado);
                                    response.setIdObjetoCreado(cobranzaDtoExistentes[0].getIdCobranzaAbono());
                                    response.setIdBancoOperacionCreado(cobranzaDtoExistentes[0].getIdOperacionBancaria());
                                    
                                    return response;
                                }
                            }
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                        
                        
                        //Para devolucion de dinero
                       
                        
                        //Si el abono es devolucion de efectivo se registra
                        if(registrarAbonoRequest.getWsDatosMetodoPagoRequest().getMetodoPagoId()!= VentaMetodoPagoBO.METODO_PAGO_DEVOLUCION_EFE ){
                            if (registrarAbonoRequest.getMontoAbono()>adeudo){
                                response.setError(true);
                                response.setNumError(901);
                                response.setMsgError("El abono por $ " + registrarAbonoRequest.getMontoAbono() 
                                        + " que se intenta hacer excede el adeudo restante de la venta. "
                                        + "Adeudo restante: $ " + adeudo);
                                response.setAbonoExcedido(true);
                                return response;
                            }
                        }else if(registrarAbonoRequest.getWsDatosMetodoPagoRequest().getMetodoPagoId()== VentaMetodoPagoBO.METODO_PAGO_DEVOLUCION_EFE ){
                            if (registrarAbonoRequest.getMontoAbono()>totalPedido){
                                response.setError(true);
                                response.setNumError(901);
                                response.setMsgError("la devolución  por $ " + registrarAbonoRequest.getMontoAbono() 
                                        + " que se intenta hacer excede el total de la venta. "
                                        + "Saldo pagado: $ " + pagado);
                                response.setAbonoExcedido(true);
                                return response;
                            }
                        }

                        //Almacenamos Operacion bancaria en caso de que existiera en la venta
                        int idOperacionBancaria = -1;
                        //double totalCobrado = 0;
                        if (registrarAbonoRequest.getBancoOperacion()!=null 
                                && registrarAbonoRequest.getWsDatosMetodoPagoRequest()
                                        .getMetodoPagoId()==VentaMetodoPagoBO.METODO_PAGO_TDC){
                            try{
                                BancoOperacionDaoImpl bancoOperacionDao = new BancoOperacionDaoImpl(getConn());
                                BancoOperacion bancoOperacionDto = new BancoOperacion();
                                WsBancoOperacionRequest wsBancoOperacionRequest = registrarAbonoRequest.getBancoOperacion();
                                if (wsBancoOperacionRequest!=null){

                                    bancoOperacionDto.setBancoAuth(wsBancoOperacionRequest.getBancoAuth());
                                    bancoOperacionDto.setBancoOperFecha(wsBancoOperacionRequest.getBancoOperFecha());
                                    bancoOperacionDto.setBancoOperIssuingBank(wsBancoOperacionRequest.getBancoOperIssuingBank());
                                    bancoOperacionDto.setBancoOperType(wsBancoOperacionRequest.getBancoOperType());
                                    bancoOperacionDto.setBancoOrderId(wsBancoOperacionRequest.getBancoOrderId());
                                    bancoOperacionDto.setIdEmpresa(idEmpresa);
                                    bancoOperacionDto.setIdEstatus(wsBancoOperacionRequest.getIdEstatus());
                                    //bancoOperacionDto.setIdOperacionBancaria(wsBancoOperacionRequest.getIDO);
                                    bancoOperacionDto.setMonto(wsBancoOperacionRequest.getMonto());
                                    bancoOperacionDto.setNoTarjeta(wsBancoOperacionRequest.getNoTarjeta());
                                    bancoOperacionDto.setNombreTitular(wsBancoOperacionRequest.getNombreTitular());
                                    bancoOperacionDto.setDataArqc(wsBancoOperacionRequest.getDataArqc());
                                    bancoOperacionDto.setDataAid(wsBancoOperacionRequest.getDataAid());
                                    bancoOperacionDto.setDataTsi(wsBancoOperacionRequest.getDataTsi());
                                    bancoOperacionDto.setDataRef(wsBancoOperacionRequest.getDataRef());
                                    bancoOperacionDto.setDataExtra1(wsBancoOperacionRequest.getDataExtra1());
                                    bancoOperacionDto.setDataExtra2(wsBancoOperacionRequest.getDataExtra2());
                                    
                                    BancoOperacionBO bancoOperacionBO = new BancoOperacionBO(getConn());
                                    File archivoImagenIFE = bancoOperacionBO.crearArchivoImagenExtraFromBase64(idEmpresa, wsBancoOperacionRequest.getImagenIFEBytesBase64(),true);
                                    File archivoImagenTDC = bancoOperacionBO.crearArchivoImagenExtraFromBase64(idEmpresa, wsBancoOperacionRequest.getImagenTDCBytesBase64(),false);

                                    if (archivoImagenIFE!=null)
                                        bancoOperacionDto.setNombreArchivoImgIfe(archivoImagenIFE.getName());
                                    if (archivoImagenTDC!=null)
                                        bancoOperacionDto.setNombreArchivoImgTdc(archivoImagenTDC.getName());

                                    bancoOperacionDao.insert(bancoOperacionDto);

                                    idOperacionBancaria = bancoOperacionDto.getIdOperacionBancaria();
                                    response.setIdBancoOperacionCreado(idOperacionBancaria);
                                }
                            }catch(Exception ex){
                                response.setMsgError("No se pudo almacenar operación bancaria en servidor. Reportelo a su proveedor " + ex.toString());
                                System.out.println("***No se pudo almacenar operación bancaria****");
                                ex.toString();
                            }
                        }
                        
                        //------**************
                        if (registrarAbonoRequest.getWsDatosMetodoPagoRequest().getMetodoPagoId()!= VentaMetodoPagoBO.METODO_PAGO_CREDITO){
                        
                            File archivoImagenFirma = null;
                            if (registrarAbonoRequest.getWsDatosMetodoPagoRequest().getImagenFirmaBytesBase64()!=null){
                                if (registrarAbonoRequest.getWsDatosMetodoPagoRequest().getImagenFirmaBytesBase64().trim().length()>0){
                                    try{
                                        //Convertimos bytes en base64 a una imagen y la almacenamos en servidor
                                        byte[] bytesImagenFirma = Base64.decode(registrarAbonoRequest.getWsDatosMetodoPagoRequest().getImagenFirmaBytesBase64());

                                        String ubicacionImagenesFirmaAbonos = appConfig.getApp_content_path() + rfcEmpresaMatriz +"/abonos/images/";
                                        String nombreArchivoImagenAbono = "img_firma_"+DateManage.getDateHourString()+".jpg";

                                        archivoImagenFirma = FileManage.createFileFromByteArray(bytesImagenFirma, ubicacionImagenesFirmaAbonos, nombreArchivoImagenAbono);
                                    }catch(Exception ex){
                                        System.out.println("");
                                        ex.printStackTrace();
                                    }
                                }
                            }

                            //SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(getConn());
                            String identificadorOperacion = registrarAbonoRequest.getWsDatosMetodoPagoRequest().getMetodoPagoDocumentoNumero();
                            identificadorOperacion = identificadorOperacion!=null?(identificadorOperacion.trim().equals("null")?"":identificadorOperacion.trim()):"";
                            String comentarios = "Abono registrado desde aplicativo móvil, Folio Cobranza Móvil: " + registrarAbonoRequest.getFolioCobranzaMovil() + " .";
                            int idAbono = cobranzaAbonoBO.registrarAbono(usuarioBO.getUser(), 
                                    pedidoDto.getIdPedido(), 
                                    0, //Id Comprobante Fiscal
                                    registrarAbonoRequest.getMontoAbono(),  //Monto Abono
                                    registrarAbonoRequest.getWsDatosMetodoPagoRequest().getMetodoPagoId(), //ID Metodo Pago
                                    identificadorOperacion,  //Identificador Operacion (pago con documento)
                                    comentarios, //Comentarios
                                    idOperacionBancaria,// ID Operacion Bancaria
                                    registrarAbonoRequest.getLatitud(), registrarAbonoRequest.getLongitud(), //Latitud, Longitud
                                    archivoImagenFirma, //Archivo imagen firma 
                                    registrarAbonoRequest.getFolioCobranzaMovil(), //Folio de Cobranza Movil
                                    registrarAbonoRequest.getFechaCobranza(), //Fecha  y Hr de captura de pedido
                                    false,
                                    registrarAbonoRequest.getReferencia()!=null?registrarAbonoRequest.getReferencia():"",
                                    registrarAbonoRequest.getReferencia()!=null?new Date():null); //Capturado en consola? (FALSE)

                            response.setIdObjetoCreado(idAbono);
                            
                            double pagadoActualizado = cobranzaAbonoBO.getSaldoPagadoPedido(pedidoDto.getIdPedido()).doubleValue();
                                
                            response.setSaldoPagadoPedido(pagadoActualizado);

                            try{
                                if (registrarAbonoRequest.isEnviarCorreoReciboPago()){
                                    ReenviaComprobantesRequest reenviaReq = new ReenviaComprobantesRequest();
                                    reenviaReq.setEnviarCobranzaAbono(true);
                                    reenviaReq.setEnviarComprobanteFiscal(false);
                                    reenviaReq.setEnviarPedido(false);
                                    reenviaReq.setIdServerCobranzaAbono(idAbono);
                                    //reenviaReq.setIdServerComprobanteFiscal();
                                    reenviaReq.setIdServerPedido(registrarAbonoRequest.getIdServerPedido());
                                    reenviaReq.setListaCorreosDestinatarios(registrarAbonoRequest.getListaCorreosDestinatarios());
                                    if (idOperacionBancaria>0){
                                        reenviaReq.setEnviarBancoOperacion(true);
                                        reenviaReq.setIdServerBancoOperacion(idOperacionBancaria);
                                    }else{
                                        reenviaReq.setEnviarBancoOperacion(false);
                                    }
                                    this.reenviarCorreoComprobanteAbonoBoucher(empleadoDtoRequest, reenviaReq);
                                }
                                    
                                //Enviamos correo en caso de pago con tarjeta de credito/debito con boucher y firma
                                /*if (idOperacionBancaria>0 && registrarAbonoRequest.isEnviarCorreoReciboPago()){
                                    BancoOperacionBO bancoOpBO = new BancoOperacionBO(getConn());

                                    String destinatarios = "";
                                    if (registrarAbonoRequest.getListaCorreosDestinatarios()!=null){
                                        if (registrarAbonoRequest.getListaCorreosDestinatarios().length>0){
                                            for (String dest : registrarAbonoRequest.getListaCorreosDestinatarios()){
                                                destinatarios += dest + ",";
                                            }

                                            bancoOpBO.enviarTicket(destinatarios, idOperacionBancaria, idEmpresa);
                                        }
                                    }

                                }*/
                            }catch(Exception ex){
                                response.setMsgError("El correo de boucher de pago no se pudo enviar." + ex.toString());
                            }

                        }

                    }else{
                        response.setError(true);
                        response.setNumError(901);
                        response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                        return response;
                    }
            }else{
                 response.setError(true);
                response.setNumError(901);
                response.setMsgError(validacion);
            }
        }catch(ArithmeticException ex){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error al intentar almacenar abono a venta. La descripción del error es: " + ex.toString());
            response.setAbonoExcedido(true);
            ex.printStackTrace();
        }catch(Exception ex){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error al intentar almacenar abono a venta. La descripción del error es: " + ex.toString());
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    private String validaDatosAbonoRequest(EmpleadoDtoRequest empleadoDtoRequest, RegistrarAbonoRequest registrarAbonoRequest){
        String msgError="";
        
        if (empleadoDtoRequest==null)
            msgError = "Datos de empleado nulos. Se requieren datos para autenticar operación.";
        
        if (registrarAbonoRequest.getIdServerPedido() <= 0 ){
            msgError = "El dato ID Pedido de Servidor no puede estar vacío, se necesita para relacionar al pedido. "
                    + "Revise que el pedido ya este registrado en el servidor.";
        }
        
        if (registrarAbonoRequest.getMontoAbono() <= 0 ){
            msgError = "El abono no puede ser igual o menor a 0 (cero).";
        }
        
        if (registrarAbonoRequest.getWsDatosMetodoPagoRequest()==null){
            msgError = "La información del método de pago no puede ser nula.";
        }else{
            switch (registrarAbonoRequest.getWsDatosMetodoPagoRequest().getMetodoPagoId()) {
                
                case VentaMetodoPagoBO.METODO_PAGO_TDC:
                    if (registrarAbonoRequest.getBancoOperacion()==null){
                        msgError = "Si eligio pago con Tarjeta, debe específicar los datos de la operación bancaria efectuada Valor nulo.";
                    }
                    break;
                    
                case VentaMetodoPagoBO.METODO_PAGO_EFECTIVO:
                    break;
                    
                case VentaMetodoPagoBO.METODO_PAGO_DOCUMENTO:
                    if (registrarAbonoRequest.getWsDatosMetodoPagoRequest().getMetodoPagoDocumentoNumero()==null){
                        msgError = "Se eligio pago con documento, y no se específico un numero/identificador de doc. válido. Valor nulo.";
                    }else if(registrarAbonoRequest.getWsDatosMetodoPagoRequest().getMetodoPagoDocumentoNumero().trim().equals("")){
                        msgError = "Se eligio pago con documento, y no se específico un numero/identificador de doc. válido. Valor vacío.";
                    }
                    break;
                    
                case VentaMetodoPagoBO.METODO_PAGO_CREDITO:
                    msgError = "No se permiten abonos posteriores con método de pago a Crédito.";
                    break;
                
                case VentaMetodoPagoBO.METODO_PAGO_BONIFICACION:
                    break;
                
                case VentaMetodoPagoBO.METODO_PAGO_DEVOLUCION_EFE:
                    break;
                    
                default:
                    msgError = "No se específico un método de pago válido.";
                    break;
                    
             }
        }
        
        return msgError;
    }
    
    
    public CancelarAbonoPedidoResponse cancelaAbonoPedido(String empleadoDtoRequestJSON, String cancelarAbonoRequestJSON){
        CancelarAbonoPedidoResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest;
        CancelarAbonoRequest cancelarAbonoRequest;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            cancelarAbonoRequest = gson.fromJson(cancelarAbonoRequestJSON, CancelarAbonoRequest.class);
            
            response = this.cancelaAbonoPedido(empleadoDtoRequest,cancelarAbonoRequest);
        }catch(Exception ex){
            response = new CancelarAbonoPedidoResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    public CancelarAbonoPedidoResponse cancelaAbonoPedido(EmpleadoDtoRequest empleadoDtoRequest, CancelarAbonoRequest cancelarAbonoRequest){
        CancelarAbonoPedidoResponse response = new CancelarAbonoPedidoResponse();
        
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), 
                    empleadoDtoRequest.getEmpleadoPassword())) {

                String fechaCancelacion = "";
                if (cancelarAbonoRequest.getIdServerBancoOperacion()>0){
                    if (cancelarAbonoRequest.getBancoOperacion()!=null){
                        if (cancelarAbonoRequest.getBancoOperacion().getBancoOperFecha()==null
                                || "".equals(cancelarAbonoRequest.getBancoOperacion().getBancoOperFecha()) ){
                            
                            response.setError(true);
                            response.setNumError(901);
                            response.setMsgError("El dato de la fecha de cancelación en Banco Operación es nulo o esta vacío, es requerido para registrar la acción.");
                            return response;
                            
                        }else{
                            fechaCancelacion = cancelarAbonoRequest.getBancoOperacion().getBancoOperFecha();
                        }
                    }else{
                        response.setError(true);
                        response.setNumError(901);
                        response.setMsgError("Datos incompletos para registro de cancelación de Banco Operación (pago con Tarjetas). Los datos de Banco Operacion son nulos.");
                        return response;
                    }
                }
                
                SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(getConn());
                
                cobranzaAbonoBO.cancelarAbono(cancelarAbonoRequest.getIdServerCobranzaAbono(), 
                        cancelarAbonoRequest.getIdServerBancoOperacion(),
                        fechaCancelacion);
                
                
                //Obtenemos el nuevo saldo pagado actual del pedido
                try{
                    SGCobranzaAbonoBO sGCobranzaAbonoBO = new SGCobranzaAbonoBO(cancelarAbonoRequest.getIdServerCobranzaAbono(),getConn());
                    if (sGCobranzaAbonoBO.getCobranzaAbono()!=null){
                        //Restamos de Pedido o Comprobante Fiscal
                        int idPedido = sGCobranzaAbonoBO.getCobranzaAbono().getIdPedido();
                        BigDecimal pagadoActual = sGCobranzaAbonoBO.getSaldoPagadoPedido(idPedido);
                        response.setSaldoPagadoPedido(pagadoActual.doubleValue());
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                
            }else{
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                return response;
            }
        }catch(Exception ex){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error al intentar cancelar Cobranza Abono. La descripción del error es: " + ex.toString());
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    
    /**
     * Crea un Comprobante Fiscal a partir de un Pedido
     * previamente registrado en el sistema.
     * 
     * Recibe los parametros en formato JSON
     * @param empleadoDtoRequestJSON Objeto EmpleadoDtoRequest en formato JSON: Datos de acceso del empleado.
     * @param crearFacturaDePedidoRequestJSON Objeto CrearFacturaDePedidoRequest en formato JSON: Información y datos para procesar facturación
     * @return CrearFacturaResponse objeto con datos sobre la facturación realizada
     */
    public CrearFacturaResponse crearFacturaDePedido(String empleadoDtoRequestJSON, String crearFacturaDePedidoRequestJSON){
        CrearFacturaResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest;
        CrearFacturaDePedidoRequest crearFacturaDePedidoRequest;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            crearFacturaDePedidoRequest = gson.fromJson(crearFacturaDePedidoRequestJSON, CrearFacturaDePedidoRequest.class);
            
            response = this.crearFacturaDePedido(empleadoDtoRequest,crearFacturaDePedidoRequest);
        }catch(Exception ex){
            response = new CrearFacturaResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    /**
     * Crea un Comprobante Fiscal a partir de un Pedido
     * previamente registrado en el sistema.
     * @param empleadoDtoRequest Datos de acceso del empleado
     * @param crearFacturaDePedidoRequest Información y datos para procesar facturación
     * @return CrearFacturaResponse objeto con datos sobre la facturación realizada
     */
    public CrearFacturaResponse crearFacturaDePedido(EmpleadoDtoRequest empleadoDtoRequest, CrearFacturaDePedidoRequest crearFacturaDePedidoRequest){
        CrearFacturaResponse response = new CrearFacturaResponse();
        
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), 
                    empleadoDtoRequest.getEmpleadoPassword())) {
                
                int idFormaPago = 1; //Pago en una sola exhibición
                String tipoComprobante = "ingreso";
                int idTipoComprobante = 2; //Factura normal
                int tipoMonedaInt = -1;
                String tipoMonedaStr ="MXN";

                Configuration appConfig = new Configuration();
                GenericValidator validator = new GenericValidator();
                UsuarioBO usuarioBO = empleadoBO.getUsuarioBO();
                int idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                
                Empresa empresaMatrizDto;
                String rfcEmpresaMatriz ="";
                try{
                    empresaMatrizDto = new EmpresaBO(getConn()).getEmpresaMatriz(idEmpresa);
                    rfcEmpresaMatriz = empresaMatrizDto.getRfc();
                }catch(Exception ex){
                    response.setError(true);
                    response.setNumError(901);
                    response.setMsgError("No se pudo recuperar la información de la empresa matriz del empleado. Verifique con su administrador de sistema." + ex.toString());
                    return response;
                }

                SGPedidoBO pedidoSesionBO = new SGPedidoBO(crearFacturaDePedidoRequest.getIdServerPedido(),getConn());
                if (pedidoSesionBO.getPedido()!=null){
                    PedidoSesion pedidoSesion = pedidoSesionBO.getSessionFromPedidoDB();
                    if (pedidoSesion!=null){
                        if (pedidoSesion.getIdComprobanteFiscal()>0){
                            
                            //El pedido ya fue facturado previamente, recuperamos la información del CFDI
                            
                            response.setError(true);
                            response.setNumError(307);
                            response.setMsgError("Este pedido ya fue facturado, el ID de la factura en servidor es: " + pedidoSesion.getIdComprobanteFiscal());
                            response.setIdObjetoCreado(pedidoSesion.getIdComprobanteFiscal());
                            try{
                                ComprobanteFiscalBO compFiscalBO = new ComprobanteFiscalBO(pedidoSesion.getIdComprobanteFiscal(), getConn());
                                
                                Empresa empresaDto = new EmpresaBO(compFiscalBO.getComprobanteFiscal().getIdEmpresa(),getConn()).getEmpresa();
                                String rfcDestinatario;
                                Cliente clienteDto = new ClienteBO(compFiscalBO.getComprobanteFiscal().getIdCliente(),getConn()).getCliente();
                                rfcDestinatario = clienteDto.getRfcCliente();

                                String archivoXMLStr = compFiscalBO.getComprobanteFiscal().getArchivoCfd();

                                String rutaArchivo = appConfig.getApp_content_path() + empresaDto.getRfc() + "/cfd/emitidos/"
                                        +TipoComprobanteBO.getTipoComprobanteNombreCarpeta(compFiscalBO.getComprobanteFiscal().getIdTipoComprobante()) 
                                        +"/" + rfcDestinatario
                                        + "/" + archivoXMLStr;
                                
                                File archivoXML = new File(rutaArchivo);
                                
                                Cfd32BO cfd32BO;
                                try {
                                    cfd32BO = new Cfd32BO(archivoXML);
                                    cfd32BO.setComprobanteFiscalDto(compFiscalBO.getComprobanteFiscal());
                                    
                                    ComprobanteFiscalDigitalBO comprobanteFiscalDigitalBO = new ComprobanteFiscalDigitalBO(getConn());
                            
                                    WSTimbreFiscalDigital wSTimbreFiscalDigital = comprobanteFiscalDigitalBO.transformaTimbreAResponseType(cfd32BO.getTimbreFiscalDigital(), cfd32BO.gettFDv1());
                                    WSComprobanteFiscalDigital wSComprobanteFiscalDigital = comprobanteFiscalDigitalBO.transformaAWSComprobanteFiscalDigital(cfd32BO.getComprobanteFiscal(), cfd32BO.getCfd());

                                    response.setComprobanteFiscalDigital(wSComprobanteFiscalDigital);
                                    response.setTimbreFiscalDigitalResponse(wSTimbreFiscalDigital);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                
                            }catch(Exception ex){
                                ex.printStackTrace();
                            }
                            return response;
                        }else{
                            
                            ComprobanteFiscalBO comprobanteFiscalBO = new ComprobanteFiscalBO(getConn());
                            Cfd32BO cfd32BO = null;
                            File archivoXML = null;
                            File archivoPDF = null;

                            ComprobanteFiscalSesion cfdiSesion = pedidoSesionBO.convertirAComprobanteFiscalSesion(pedidoSesion);
                            
                            //Almacenar Nuevo Tipo de Pago--------------
                            TipoPagoDaoImpl tipoPagoDao = new TipoPagoDaoImpl(getConn());
                            int nextIdTipoPago = tipoPagoDao.findLast().getIdTipoPago() + 1;
                            
                            int idTipoPago = VentaMetodoPagoBO.METODO_PAGO_EFECTIVO;
                            String tipoPagoNumCuenta ="";
                            try{
                                //Comparamos la fecha tentativa de pago contra la de captura del pedido
                                // Si no corresponden, indica que fue registrado mediante Metodo Pago a Crédito
                                if (!DateManage.isOnDate(pedidoSesion.getFechaTentativaPago(), pedidoSesionBO.getPedido().getFechaPedido())){
                                    //Método Pago inicial de Pedido = Crédito
                                    idTipoPago = VentaMetodoPagoBO.METODO_PAGO_CREDITO;
                                }else{
                                    SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(getConn());
                                    SgfensCobranzaAbono primerAbonoDto = cobranzaAbonoBO.getPrimerAbonoPedido(pedidoSesion.getIdPedido());
                                    
                                    if (primerAbonoDto!=null){
                                        idTipoPago = primerAbonoDto.getIdCobranzaMetodoPago();
                                        if (idTipoPago == VentaMetodoPagoBO.METODO_PAGO_TDC){
                                            
                                            BancoOperacion bancoOperacionDto = new BancoOperacionBO(primerAbonoDto.getIdOperacionBancaria(),getConn()).getBancoOperacion();
                                            if (bancoOperacionDto!=null){
                                                tipoPagoNumCuenta = bancoOperacionDto.getNoTarjeta();
                                                
                                                if (tipoPagoNumCuenta.length()>=4){
                                                    tipoPagoNumCuenta =  tipoPagoNumCuenta.substring(tipoPagoNumCuenta.length()-4, tipoPagoNumCuenta.length());
                                                }else{
                                                    tipoPagoNumCuenta="";
                                                }
                                                
                                            }
                                            
                                        }
                                    }
                                    
                                }
                                
                            }catch(Exception ex){
                                ex.printStackTrace();
                            }
                            
                            TipoPago tipoPagoDto = new TipoPago();
                            try{
                                SgfensCobranzaMetodoPago cobranzaMetodoPago = new SGCobranzaMetodoPagoBO(idTipoPago,getConn()).getCobranzaMetodoPago();
                                
                                tipoPagoDto.setClaveMetodoSat( cobranzaMetodoPago.getClaveMetodoSat()  );
                                tipoPagoDto.setDescTipoPago(cobranzaMetodoPago.getNombreMetodoPago());
                                tipoPagoDto.setNumeroCuenta(tipoPagoNumCuenta);
                                tipoPagoDto.setIdTipoPago(nextIdTipoPago);

                                //Insertamos registro
                                tipoPagoDao.insert(tipoPagoDto);

                                cfdiSesion.setTipo_pago(tipoPagoDto);
                            }catch(Exception ex){
                                response.setError(true);
                                response.setNumError(902);
                                response.setMsgError("No se pudo almacenar los datos del Tipo de Pago en un nuevo registro: " + ex.toString());
                                return response;
                            }
                            //Fin Almacenar Nuevo Tipo de Pago----------


                            
                            
                        //---------GENERACION DE XML
                        try{

                            FormaPago formaPagoDto = new FormaPagoBO(getConn()).findFormaPagoById(idFormaPago);
                            Empresa empresaDto = new EmpresaBO(getConn()).findEmpresabyId(idEmpresa);
                            Ubicacion ubicacionDto = new UbicacionBO(getConn()).findUbicacionbyId(empresaDto.getIdUbicacionFiscal());
                            Cliente clienteDto =cfdiSesion.getCliente();
                            
                            //Estatus de cliente datos incompletos
                            if (clienteDto.getIdEstatus() == 3
                                    || clienteDto.getRfcCliente().equals("AAA010101")){
                                response.setError(true);
                                response.setNumError(901);
                                response.setMsgError("No se puede facturar al cliente debido a que sus datos no estan completos.");
                                return response;
                            }

                            ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Conceptos.Concepto> listaConceptos = cfdiSesion.getSchemaConceptos();
                            ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Traslados.Traslado> listaTraslados = cfdiSesion.getSchemaImpuestosTraslados();
                            ArrayList<mx.bigdata.sat.cfdi.schema.v32.Comprobante.Impuestos.Retenciones.Retencion> listaRetenciones = cfdiSesion.getSchemaImpuestosRetenidos();

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
                                    null);//parcialidades

                            if (comprobanteFiscal!=null){
                                if (idFormaPago==2){
                                    //Parcialidades
                                    //...
                                }
                                //Descuento
                                if (cfdiSesion.getDescuento_importe() >0){
                                    comprobanteFiscal.setDescuento(cfdiSesion.getDescuentoImporte());
                                    comprobanteFiscal.setMotivoDescuento(cfdiSesion.getDescuento_motivo());
                                }
                                //Folios y Series
                                /*
                                if (idFolios>0 && serie!=null){
                                    comprobanteFiscal.setSerie(serie);
                                    comprobanteFiscal.setFolio(folioGeneradoStr);
                                }
                                */
                                
                                //tipo Moneda
                                //cfdiSesion.getTipo_moneda()
                                try{
                                    TipoDeMoneda tipoDeMonedaDto = new TipoDeMonedaBO(tipoMonedaInt,getConn()).getTipoMoneda();
                                    tipoMonedaStr = tipoDeMonedaDto.getClave();
                                }catch(Exception ex){
                                    
                                    ex.printStackTrace();
                                }
                                //fin tipo Moneda
                                comprobanteFiscal.setMoneda(tipoMonedaStr);
                                
                                /*
                                if (tipoCambio!=1)
                                    comprobanteFiscal.setTipoCambio(new BigDecimal(tipoCambio).setScale(2, RoundingMode.HALF_UP).toString());
                                * 
                                */
                            }else{
                                 response.setError(true);
                                response.setNumError(902);
                                response.setMsgError("No se pudo generar el Comprobante Fiscal (XML)");
                                return response;
                            }

                            
                            
                            
                            CertificadoDigitalBO certificadoDigitalBO = new CertificadoDigitalBO(getConn());
                            CertificadoDigital certificadoDigitalDto = certificadoDigitalBO.findCertificadoByEmpresa(empresaDto.getIdEmpresa());

                            String rutaArchivosUsuario = appConfig.getApp_content_path() + empresaDto.getRfc() + "/";
                            //String rutaArchivosFacturaCliente = rutaArchivosUsuario + "cfd/emitidos/facturas/" + clienteDto.getRfcCliente() + "/";

                            String rutaCerEmisor = rutaArchivosUsuario + certificadoDigitalDto.getNombreCer();
                            String rutaKeyEmisor = rutaArchivosUsuario + certificadoDigitalDto.getNombreKey();
                            String passwordKeyEmisor = certificadoDigitalDto.getPassword();

                            cfd32BO = new Cfd32BO(comprobanteFiscal);
                            //Sellamos comprobante y validamos
                            cfd32BO.sellarComprobante(rutaCerEmisor, rutaKeyEmisor, passwordKeyEmisor);
                            //Timbramos comprobante
                            cfd32BO.timbrarComprobante();

                            //Asignamos id Empresa para poder obtener el logo en el PDF
                            cfd32BO.setComprobanteFiscalDto(new ComprobanteFiscal());
                            cfd32BO.getComprobanteFiscalDto().setIdEmpresa(idEmpresa);

                            archivoXML = cfd32BO.guardarComprobante(empresaDto, clienteDto, TipoComprobanteBO.getTipoComprobanteNombreCarpeta(idTipoComprobante) );//"facturas");

                            archivoPDF = cfd32BO.guardarRepresentacionImpresa(empresaDto, clienteDto, TipoComprobanteBO.getTipoComprobanteNombreCarpeta(idTipoComprobante) );//"facturas");

                            

                        }catch(Exception ex){
                            ex.printStackTrace();
                            String msgError ="";
                            if (ex.getCause()!=null){
                                msgError +="Ocurrio un error al generar XML: " + ex.getCause().toString();
                            }else{
                                msgError +="Ocurrio un error al generar XML: " + ex.toString();
                            }
                            
                            response.setError(true);
                            response.setNumError(902);
                            response.setMsgError(msgError);
                            return response;
                        }

                        //---------FIN GENERACION XML


                        ComprobanteFiscal comprobanteFiscalDto = null;
                        if (archivoXML!=null && cfd32BO!=null){
                            cfdiSesion.setId_estatus(EstatusComprobanteBO.ESTATUS_ENTREGADA);//Entregado

                            //si se selecciono folio
                            /*
                            if (idFolios>0 && folioGenerado>0 && folioGeneradoStr!=null){
                                cfdiSesion.setId_folio(idFolios);
                                cfdiSesion.setFolio_generado(folioGeneradoStr);
                            }
                            */

                            cfdiSesion.setId_forma_pago(idFormaPago);
                            /*
                            if (idFormaPago==2 && parcialidadFormat!=null){
                                cfdiSesion.setParcialidad(parcialidadFormat);
                            }
                            */

                            cfdiSesion.setId_tipo_comprobante(idTipoComprobante);
                            cfdiSesion.setArchivo_cfd(archivoXML.getName());
                            //*****CADENA SIN ENCRIPTAR******
                            cfdiSesion.setCadena_original(cfd32BO.getCfd().getCadenaOriginal());
                            cfdiSesion.setSello_digital(cfd32BO.getComprobanteFiscal().getSello());
                            cfdiSesion.setTipo_moneda_int(tipoMonedaInt);
                            cfdiSesion.setTipo_moneda(cfdiSesion.getTipo_moneda());
                            cfdiSesion.setTipo_cambio(1);
                            cfdiSesion.setUuid(cfd32BO.getTimbreFiscalDigital().getUUID());
                            cfdiSesion.setSello_sat(cfd32BO.getTimbreFiscalDigital().getSelloSAT());

                            //Para generar un nuevo comprobanteFiscal en cada Guardar
                            //Deshabilitar en caso de querer actualizar en lugar de generar una nueva
                            cfdiSesion.setIdComprobanteFiscal(0);

                            try{
                                comprobanteFiscalDto = comprobanteFiscalBO.creaActualizaComprobanteFiscal(cfdiSesion, usuarioBO);

                                cfd32BO.copiarCFDIenvioAsincrono(archivoXML);
                                
                                response.setIdObjetoCreado(comprobanteFiscalDto.getIdComprobanteFiscal());
                            }catch(Exception ex){
                                //En caso de error, borramos archivo XML ya que no se registrara en BD
                                try{
                                    for (int k=0;k<10;k++)
                                        archivoXML.delete();
                                }catch(Exception e2){}

                                ex.printStackTrace();
                                response.setError(true);
                                response.setNumError(902);
                                response.setMsgError("Ocurrio un error al almacenar el registro de comprobanteFiscal: <br/>" + ex.toString());
                                return response;
                            }

                        }
                        
                        //Llenamos objetos de respuesta con datos completos del comprobante fiscal
                        try{
                            ComprobanteFiscalDigitalBO comprobanteFiscalDigitalBO = new ComprobanteFiscalDigitalBO(getConn());
                            
                            WSTimbreFiscalDigital wSTimbreFiscalDigital = comprobanteFiscalDigitalBO.transformaTimbreAResponseType(cfd32BO.getTimbreFiscalDigital(), cfd32BO.gettFDv1());
                            WSComprobanteFiscalDigital wSComprobanteFiscalDigital = comprobanteFiscalDigitalBO.transformaAWSComprobanteFiscalDigital(cfd32BO.getComprobanteFiscal(), cfd32BO.getCfd());
                            
                            response.setComprobanteFiscalDigital(wSComprobanteFiscalDigital);
                            response.setTimbreFiscalDigitalResponse(wSTimbreFiscalDigital);
                        }catch(Exception ex){
                            String msgError ="Error al intentar deserializar información del comprobante y timbre fiscal para la respuesta.";
                            response.setMsgError(msgError + ex.toString());
                            System.out.println(msgError);
                            ex.printStackTrace();
                        }

                        //Si la factura se genero desde un pedido, se actualiza el pedido
                        if (cfdiSesion.getIdPedido()>0){
                            try{
                                    SgfensPedido pedidoDto = new SGPedidoBO(cfdiSesion.getIdPedido(),getConn()).getPedido();
                                    if (pedidoDto!=null){
                                        pedidoDto.setIdComprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal());

                                        new SgfensPedidoDaoImpl(getConn()).update(pedidoDto.createPk(), pedidoDto);
                                    }else{
                                        response.setMsgError("Comprobante generado con incidencias: No se pudo recuperar la información del Pedido asociado a la Factura que se desea generar. Avise a su proveedor.");
                                    }

                            }catch(Exception ex){
                                response.setMsgError("Comprobante generado con incidencias: No se pudo recuperar la información del Pedido asociado a la Factura que se desea generar. Avise a su proveedor.");
                            }
                        }

                            /**
                             * Envío de notificación con CFDI (XML y PDF)
                             */
                            if (crearFacturaDePedidoRequest.isEnviarCorreoRepresentacionImpresa()){
                                //Si se solicito el envío de la representación impresa por correo.

                                List<String> destinatarios = new ArrayList<String>();
                                if (crearFacturaDePedidoRequest.getListaCorreosDestinatarios()!=null){
                                    if (crearFacturaDePedidoRequest.getListaCorreosDestinatarios().length>0){
                                        for (String dest : crearFacturaDePedidoRequest.getListaCorreosDestinatarios()){
                                            if (validator.isEmail(dest)){
                                                destinatarios.add(dest);
                                            }else{
                                                response.setMsgError(response.getMsgError() + ". El destinarario '" + dest + "' no es válido.");
                                            }
                                        }

                                        if (destinatarios.size()>0){
                                            
                                            String msgErrorCorreo = comprobanteFiscalBO.enviaNotificacionNuevaComprobanteFiscal(destinatarios,archivoXML, archivoPDF,idEmpresa);
                                            if (msgErrorCorreo.trim().length()>0)
                                                response.setMsgError(response.getMsgError() + " " + msgErrorCorreo);
                                            
                                        }else{
                                            response.setMsgError(response.getMsgError() + ". No se envío representación impresa "
                                                    + "ya que no se expreso al menos 1 e-mail destinatario correcto.");
                                        }
                                    }else{
                                        response.setMsgError(response.getMsgError() + ". No se envío representación impresa "
                                                    + "ya que no se expreso al menos 1 e-mail destinatario.");
                                    }
                                }else{
                                    response.setMsgError(response.getMsgError() + ". No se envío representación impresa "
                                                    + "ya que no se expreso al menos 1 e-mail destinatario.");
                                }
                            }

                        }
                    }else{
                        response.setError(true);
                        response.setNumError(902);
                        response.setMsgError("La información del pedido no pudo ser recuperada satisfactoriamente a objetos de sesion. Avise a su proveedor.");
                        return response;
                    }
                }else{
                    response.setError(true);
                    response.setNumError(901);
                    response.setMsgError("El pedido indicado no esta registrado en el servidor.");
                    return response;
                }


            }else {
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        }catch (Exception e) {
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error al intentar crear Comprobante Fiscal del pedido. La descripción del error es: " + e.toString());
            e.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    
    public WSResponse reenviarCorreoPedidoFactura(String empleadoDtoRequestJSON, String reenviaComprobantesRequestJSON){
        WSResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest;
        ReenviaComprobantesRequest reenviaComprobantesRequest;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            reenviaComprobantesRequest = gson.fromJson(reenviaComprobantesRequestJSON, ReenviaComprobantesRequest.class);
            
            response = this.reenviarCorreoPedidoFactura(empleadoDtoRequest,reenviaComprobantesRequest);
        }catch(Exception ex){
            response = new WSResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    public WSResponse reenviarCorreoPedidoFactura(EmpleadoDtoRequest empleadoDtoRequest, ReenviaComprobantesRequest reenviaComprobantesRequest){
        WSResponse response = new WSResponse();
        
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), 
                    empleadoDtoRequest.getEmpleadoPassword())) {
                    int idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();     
                
                    String validacion = validaEnvioCorreos(reenviaComprobantesRequest.getListaCorreosDestinatarios());
                    if (!"".equals(validacion) ){
                        response.setError(true);
                        response.setNumError(901);
                        response.setMsgError(validacion);
                        return response;
                    }
                
                    long idPedido = 0;
                    long idComprobanteFiscal = 0;
                    
                    UsuarioBO usuarioBO = empleadoBO.getUsuarioBO();
                    
                    ArrayList<String> listaCorreos = new ArrayList<String>();
                    listaCorreos.addAll(Arrays.asList(reenviaComprobantesRequest.getListaCorreosDestinatarios()));
        
                    SGPedidoBO pedidoBO = null;
                    if (reenviaComprobantesRequest.isEnviarPedido()){
                        
                        if (reenviaComprobantesRequest.getIdServerPedido()<=0){
                            response.setError(true);
                            response.setNumError(901);
                            response.setMsgError("El ID de Pedido indicado es inválido. Debe corresponder a uno existente en el servidor, y ser mayor a 0.");
                            return response;
                        }
                        
                        pedidoBO = new SGPedidoBO(reenviaComprobantesRequest.getIdServerPedido(),getConn());
                        if (pedidoBO.getPedido()!=null){
                            idPedido = pedidoBO.getPedido().getIdPedido();
                            idComprobanteFiscal = pedidoBO.getPedido().getIdComprobanteFiscal();
                        }
                    }
                    
                    if (reenviaComprobantesRequest.isEnviarPedido() && idPedido>0){
                        try{
                            File pedidoPDF = pedidoBO.guardarRepresentacionImpresa(usuarioBO);
                            File[] adjuntos = new File[1];
                            adjuntos[0] = pedidoPDF;

                            String msgErrorCorreo = pedidoBO.enviaNotificacionNuevaPedido(listaCorreos,adjuntos,idEmpresa);
                            if (msgErrorCorreo.trim().length()>0)
                                response.setMsgError(response.getMsgError() + " " + msgErrorCorreo);
                            
                        }catch(Exception ex){
                            System.out.print("No se pudo adjuntar y/o enviar la representación impresa del pedido." + ex.toString());
                            response.setMsgError("No se pudo adjuntar y/o enviar la representación impresa del pedido.");
                            ex.printStackTrace();
                        }
                    }
                    
                    if (reenviaComprobantesRequest.isEnviarComprobanteFiscal() || idComprobanteFiscal>0){
                        
                        if (idComprobanteFiscal<=0){
                            idComprobanteFiscal = reenviaComprobantesRequest.getIdServerComprobanteFiscal();
                        }
                        
                        if (idComprobanteFiscal>0){
                            ComprobanteFiscalBO comprobanteFiscalBO = new ComprobanteFiscalBO(idComprobanteFiscal,getConn());
                            
                            if (comprobanteFiscalBO.getComprobanteFiscal()!=null){
                                File archivoXML = comprobanteFiscalBO.getComprobanteFiscalXML();
                                File archivoPDF = comprobanteFiscalBO.getComprobanteFiscalPDF();

                                String msgErrorCorreo = comprobanteFiscalBO.enviaNotificacionNuevaComprobanteFiscal(listaCorreos,archivoXML, archivoPDF,idEmpresa);
                                if (msgErrorCorreo.trim().length()>0)
                                    response.setMsgError(response.getMsgError() + " " + msgErrorCorreo);
                            }else{
                                response.setError(true);
                                response.setNumError(901);
                                response.setMsgError("El Comprobante Fiscal especificado no se pudo recuperar de la base de datos del servidor.");
                                return response;
                            }
                                    
                            
                        }else{
                            response.setError(true);
                            response.setNumError(901);
                            response.setMsgError("El ID del Comprobante Fiscal indicado es inválido. Debe corresponder a uno existente en el servidor, y ser mayor a 0.");
                            return response;
                        }
                    }
                    
                    
            }else{
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                return response;
            }
        }catch(Exception ex){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error al intentar reenviar Pedido o factura. La descripción del error es: " + ex.toString());
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
        
    }
    
    
    public WSResponse reenviarCorreoComprobanteAbonoBoucher(String empleadoDtoRequestJSON, String reenviaComprobantesRequestJSON){
        WSResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest;
        ReenviaComprobantesRequest reenviaComprobantesRequest;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            reenviaComprobantesRequest = gson.fromJson(reenviaComprobantesRequestJSON, ReenviaComprobantesRequest.class);
            
            response = this.reenviarCorreoComprobanteAbonoBoucher(empleadoDtoRequest,reenviaComprobantesRequest);
        }catch(Exception ex){
            response = new WSResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    public WSResponse reenviarCorreoComprobanteAbonoBoucher(EmpleadoDtoRequest empleadoDtoRequest, ReenviaComprobantesRequest reenviaComprobantesRequest){
        WSResponse response = new WSResponse();
        
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), 
                    empleadoDtoRequest.getEmpleadoPassword())) {
                    int idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();   
                    String validacion = validaEnvioCorreos(reenviaComprobantesRequest.getListaCorreosDestinatarios());
                    if (!"".equals(validacion) ){
                        response.setError(true);
                        response.setNumError(901);
                        response.setMsgError(validacion);
                        return response;
                    }
                
                    long idCobranzaAbono = 0;
                    long idBancoOperacion = 0;
                    
                    UsuarioBO usuarioBO = empleadoBO.getUsuarioBO();
                    
                    ArrayList<String> listaCorreos = new ArrayList<String>();
                    listaCorreos.addAll(Arrays.asList(reenviaComprobantesRequest.getListaCorreosDestinatarios()));
        
                    SGCobranzaAbonoBO sGCobranzaAbonoBO = null;
                    if (reenviaComprobantesRequest.isEnviarCobranzaAbono()){
                        
                        if (reenviaComprobantesRequest.getIdServerCobranzaAbono()<=0){
                            response.setError(true);
                            response.setNumError(901);
                            response.setMsgError("El ID de Cobranza Abono indicado es inválido. Debe corresponder a uno existente en el servidor, y ser mayor a 0.");
                            return response;
                        }
                        
                        sGCobranzaAbonoBO = new SGCobranzaAbonoBO(reenviaComprobantesRequest.getIdServerCobranzaAbono(),getConn());
                        if (sGCobranzaAbonoBO.getCobranzaAbono()!=null){
                            idCobranzaAbono = sGCobranzaAbonoBO.getCobranzaAbono().getIdCobranzaAbono();
                            idBancoOperacion = sGCobranzaAbonoBO.getCobranzaAbono().getIdOperacionBancaria();
                        }
                    }
                    
                    if (reenviaComprobantesRequest.isEnviarCobranzaAbono() && idCobranzaAbono>0){
                        try{
                            File estadoCuentaPDF = sGCobranzaAbonoBO.getFileEstadoCuentaPDF(usuarioBO);
                            File[] adjuntos = new File[1];
                            adjuntos[0] = estadoCuentaPDF;

                            String msgErrorCorreo = sGCobranzaAbonoBO.enviaNotificacionEstadoCuenta(listaCorreos,adjuntos,idEmpresa);
                            if (msgErrorCorreo.trim().length()>0)
                                response.setMsgError(response.getMsgError() + " " + msgErrorCorreo);
                            
                        }catch(Exception ex){
                            System.out.print("No se pudo adjuntar y/o enviar la representación impresa del Comprobante de Pago." + ex.toString());
                            response.setMsgError("No se pudo adjuntar y/o enviar la representación impresa del Comprobante de Pago.");
                            ex.printStackTrace();
                        }
                    }
                    
                    if (reenviaComprobantesRequest.isEnviarBancoOperacion() || idBancoOperacion>0){
                        
                        if (idBancoOperacion<=0){
                            idBancoOperacion = reenviaComprobantesRequest.getIdServerBancoOperacion();
                        }
                        
                        if (idBancoOperacion>0){
                            BancoOperacionBO bancoOperacionBO = new BancoOperacionBO(idBancoOperacion,getConn());
                            
                            if (bancoOperacionBO.getBancoOperacion()!=null){
                                String msgErrorCorreo ="";
                                idEmpresa = bancoOperacionBO.getBancoOperacion().getIdEmpresa();
                                try{
                                    bancoOperacionBO.enviarTicket(listaCorreos.get(0), idBancoOperacion, idEmpresa);
                                }catch(Exception ex){
                                    msgErrorCorreo = ex.toString();
                                }

                                if (msgErrorCorreo.trim().length()>0)
                                    response.setMsgError(response.getMsgError() + " " + msgErrorCorreo);
                            }else{
                                response.setError(true);
                                response.setNumError(901);
                                response.setMsgError("El ID de BancoOperacion especificado no se pudo recuperar de la base de datos del servidor.");
                                return response;
                            }
                                    
                            
                        }else{
                            response.setError(true);
                            response.setNumError(901);
                            response.setMsgError("El ID de BancoOperacion indicado es inválido. Debe corresponder a uno existente en el servidor, y ser mayor a 0.");
                            return response;
                        }
                    }
                    
                    
            }else{
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                return response;
            }
        }catch(Exception ex){
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error al intentar reenviar comprobantes de pago. La descripción del error es: " + ex.toString());
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
        
    }
    
    
    private String validaEnvioCorreos(String[] listaCorreosDestinatarios) {
        String msgError = "";
        GenericValidator validator = new GenericValidator();
        if (listaCorreosDestinatarios != null) {
            if (listaCorreosDestinatarios.length > 0) {
                List<String> destinatarios = new ArrayList<String>();
                for (String dest : listaCorreosDestinatarios){
                    if (validator.isEmail(dest.trim())){
                        destinatarios.add(dest.trim());
                    }else{
                        //msgError += "El destinarario '" + dest + "' no es válido. ";
                    }
                }

                if (destinatarios.size()<=0){
                    msgError +="No se expreso al menos 1 e-mail destinatario correcto. ";
                }
            } else {
                msgError ="No se expreso al menos 1 e-mail destinatario válido.";
            }
        } else {
            msgError ="No se expreso al menos 1 e-mail destinatario válido.";
        }
        
        return msgError;
    }
    
    
    public ConsultaPedidosResponse getPedidosByEmpleado(String empleadoDtoRequestJSON, String consultaPedidosRequestJSON){
        ConsultaPedidosResponse response;
        
        EmpleadoDtoRequest empleadoDtoRequest = null;
        ConsultaPedidosRequest consultaPedidosRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            consultaPedidosRequest = gson.fromJson(consultaPedidosRequestJSON, ConsultaPedidosRequest.class);
            
            response = this.getPedidosByEmpleado(empleadoDtoRequest, consultaPedidosRequest);
        }catch(Exception ex){
            response = new ConsultaPedidosResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    public ConsultaPedidosResponse getPedidosByEmpleado(EmpleadoDtoRequest empleadoDtoRequest, ConsultaPedidosRequest consultaPedidosRequest){
        ConsultaPedidosResponse response = new ConsultaPedidosResponse();
        
        try{
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                int idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                //int idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                int idUsuario = empleadoBO.getEmpleado().getIdUsuarios();
                //Si se encontro el registro buscamos su catalogo de pedidos
                if (idEmpresa > 0) {
                    String filtroAdicional = "";
                    if (consultaPedidosRequest.getListaIdPedidoServerFiltro().size()>0){
                        filtroAdicional += " AND ID_PEDIDO IN (";
                        
                        for (int item : consultaPedidosRequest.getListaIdPedidoServerFiltro())
                            filtroAdicional += item + ",";
                        
                        if (filtroAdicional.endsWith(","))
                            filtroAdicional =  filtroAdicional.substring(0, filtroAdicional.length()-1);
                        
                        filtroAdicional += " )";
                    }
                    if (consultaPedidosRequest.getListaFolioPedidoMovilFiltro().size()>0){
                        filtroAdicional += " AND FOLIO_PEDIDO_MOVIL IN (";
                        
                        for (String item : consultaPedidosRequest.getListaFolioPedidoMovilFiltro())
                            filtroAdicional += "'" + item + "',";
                        
                        if (filtroAdicional.endsWith(","))
                            filtroAdicional =  filtroAdicional.substring(0, filtroAdicional.length()-1);
                        
                        filtroAdicional += " )";
                    }
                    if (consultaPedidosRequest.isFiltroModificadoConsola()){
                        //Filtra los Pedidos del Empleado que fueron modificados en consola
                        // para hacer menor el tamaño de descarga
                        filtroAdicional += " AND IS_MODIFICADO_CONSOLA=1 ";
                    }//Si no se filtra por modificados, entonces se descargaran todos (o aplicando los filtros por ID y folio movil)
                    response.setError(false);
                    response.setWsItemPedido(this.getListaPedidos(idEmpresa, idUsuario, filtroAdicional));
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            } else {
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        }catch (Exception e) {
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al consultar pedidos del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }

    private List<WsItemPedido> getListaPedidos(int idEmpresa, int idUsuario, String filtroAdicional) {
        List<WsItemPedido> listaPedidos = new ArrayList<WsItemPedido>();
        
        SGPedidoBO sgPedidoBO = new SGPedidoBO(getConn());
        try{
            String filtroBusqueda = "";
            //filtro por id_estatus
            //filtroBusqueda += " AND ID_ESTATUS_PEDIDO IN (1)";
            //filtro por saldo_pagado
            //filtroBusqueda += " AND SALDO_PAGADO<TOTAL";
            if (idUsuario>0){ //filtro por usuario vendedor
                //--filtro adicional (realizado por leonardo) para obtener los pedidos de vendedores asignados al usuario
                String filtroVendedoresAsignados = " OR ID_USUARIO_VENDEDOR IN (SELECT ID_USUARIO_EMPLEADO_ASIGNADO FROM SGFENS_ASIGNACION_EMPLEADOS WHERE ID_ESTATUS = 1 AND ID_USUARIO_EMPLEADO = " + idUsuario +" )";                                
                //filtroBusqueda += " AND (ID_USUARIO_VENDEDOR = " + idUsuario + " OR ID_USUARIO_VENDEDOR_REASIGNADO = " + idUsuario + filtroVendedoresAsignados + ")";                
                // 19-04-2016 se cambia query anterior, por el siguiente, para los casos de asignación a empleado desde consola.
                filtroBusqueda += " AND (ID_USUARIO_VENDEDOR = " + idUsuario + " OR ID_USUARIO_CONDUCTOR_ASIGNADO=" + idUsuario + " OR ID_USUARIO_VENDEDOR_ASIGNADO=" + idUsuario + " OR ID_USUARIO_VENDEDOR_REASIGNADO = " + idUsuario + filtroVendedoresAsignados + ")";                
            }
            if (StringManage.getValidString(filtroAdicional).length()>0)
                filtroBusqueda += " " + filtroAdicional;
            SgfensPedido[] pedidosDtoArray = sgPedidoBO.findPedido(-1, idEmpresa, 0, 0, filtroBusqueda);
            
            SgfensPedidoDaoImpl pedidoDao = new SgfensPedidoDaoImpl(getConn());
            SgfensPedidoProductoDaoImpl pedidoProductoDao = new SgfensPedidoProductoDaoImpl(getConn());
            SgfensPedidoImpuestoDaoImpl pedidoImpuestoDao = new SgfensPedidoImpuestoDaoImpl(getConn());
            SgfensCobranzaAbonoDaoImpl cobranzaAbonoDao = new SgfensCobranzaAbonoDaoImpl(getConn());
            ImpuestoDaoImpl impuestoDao = new ImpuestoDaoImpl(getConn());
            BancoOperacionDaoImpl bancoOperacionDao = new BancoOperacionDaoImpl(getConn());
            
            for (SgfensPedido pedidoDto : pedidosDtoArray){
                WsItemPedido wsItemPedido = new WsItemPedido();
                
                //Asignamos valores directos
                wsItemPedido.setAdelanto(pedidoDto.getAdelanto());
                wsItemPedido.setComentarios(pedidoDto.getComentarios());
                wsItemPedido.setConsecutivoPedido(pedidoDto.getConsecutivoPedido());
                wsItemPedido.setDescuentoMonto(pedidoDto.getDescuentoMonto());
                wsItemPedido.setDescuentoMotivo(pedidoDto.getDescuentoMotivo());
                wsItemPedido.setDescuentoTasa(pedidoDto.getDescuentoTasa());
                wsItemPedido.setFechaEntrega(pedidoDto.getFechaEntrega());
                wsItemPedido.setFechaPedido(pedidoDto.getFechaPedido());
                wsItemPedido.setFechaTentativaPago(pedidoDto.getFechaTentativaPago());
                wsItemPedido.setFolioPedido(pedidoDto.getFolioPedido());
                if(StringManage.getValidString(pedidoDto.getFolioPedidoMovil()).length() > 0){
                    wsItemPedido.setFolioPedidoMovil(pedidoDto.getFolioPedidoMovil());
                }else{
                    String folioMovilConsolaAsignado = SGPedidoBO.generaFolioMovil(idEmpresa);
                    try{
                        pedidoDto.setFolioPedidoMovil(folioMovilConsolaAsignado);
                        
                        pedidoDao.update(pedidoDto.createPk(), pedidoDto);
                        wsItemPedido.setFolioPedidoMovil(folioMovilConsolaAsignado);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                wsItemPedido.setIdCliente(pedidoDto.getIdCliente());
                wsItemPedido.setIdComprobanteFiscal(pedidoDto.getIdComprobanteFiscal());
                wsItemPedido.setIdEmpresa(pedidoDto.getIdEmpresa());
                wsItemPedido.setIdEstatusPedido(pedidoDto.getIdEstatusPedido());
                wsItemPedido.setIdPedido(pedidoDto.getIdPedido());
                wsItemPedido.setLatitud(pedidoDto.getLatitud());
                wsItemPedido.setLongitud(pedidoDto.getLongitud());
                wsItemPedido.setSaldoPagado(pedidoDto.getSaldoPagado());
                wsItemPedido.setSubtotal(pedidoDto.getSubtotal());
                wsItemPedido.setTiempoEntregaDias(pedidoDto.getTiempoEntregaDias());
                wsItemPedido.setTipoMoneda(pedidoDto.getTipoMoneda());
                wsItemPedido.setTotal(pedidoDto.getTotal());
                
                //Asignamos objetos relacionados a pedido
                { //Conceptos (productos / servicios)  
                    SgfensPedidoProducto[] pedidoProductosDto = pedidoProductoDao.findWhereIdPedidoEquals(pedidoDto.getIdPedido());
                    for (SgfensPedidoProducto pedidoProdDto : pedidoProductosDto) {
                        WsItemPedidoConcepto wsPConcepto = new WsItemPedidoConcepto();
                        wsPConcepto.setCantidad(pedidoProdDto.getCantidad());
                        wsPConcepto.setDescripcion(pedidoProdDto.getDescripcion());
                        wsPConcepto.setDescuentoMonto(pedidoProdDto.getDescuentoMonto());
                        wsPConcepto.setDescuentoPorcentaje(pedidoProdDto.getDescuentoPorcentaje());
                        wsPConcepto.setIdConcepto(pedidoProdDto.getIdConcepto());
                        wsPConcepto.setIdentificacion(pedidoProdDto.getIdentificacion());
                        wsPConcepto.setPrecioUnitario(pedidoProdDto.getPrecioUnitario());
                        wsPConcepto.setSubtotal(pedidoProdDto.getSubtotal());
                        wsPConcepto.setUnidad(pedidoProdDto.getUnidad());
                        
                        wsPConcepto.setCostoUnitario(pedidoProdDto.getCostoUnitario());
                        wsPConcepto.setPorcentajeComisionEmpleado(pedidoProdDto.getPorcentajeComisionEmpleado());
                        
                        wsItemPedido.getWsItemPedidoConcepto().add(wsPConcepto);
                    }
                }
                
                { //Impuestos
                    SgfensPedidoImpuesto[] pedidoImpuestosDto = pedidoImpuestoDao.findWhereIdPedidoEquals(pedidoDto.getIdPedido());
                    for (SgfensPedidoImpuesto pedidoImpDto : pedidoImpuestosDto ){
                        WsItemPedidoImpuesto wsPImpuesto = new WsItemPedidoImpuesto();
                        
                        Impuesto impuestoDto = impuestoDao.findByPrimaryKey(pedidoImpDto.getIdImpuesto());
                        if (impuestoDto!=null){
                            wsPImpuesto.setDescripcion(impuestoDto.getDescripcion());
                            wsPImpuesto.setIdEstatus(impuestoDto.getIdEstatus());
                            wsPImpuesto.setImpuestoLocal(impuestoDto.getImpuestoLocal());
                            wsPImpuesto.setNombre(impuestoDto.getNombre());
                            wsPImpuesto.setPorcentaje(impuestoDto.getPorcentaje());
                            wsPImpuesto.setTraslado(impuestoDto.getTrasladado());

                            wsItemPedido.getWsItemPedidoImpuesto().add(wsPImpuesto);
                        }
                    }                    
                }
                
                { //Abonos cobranza
                    SgfensCobranzaAbono[] cobranzaAbonosDto = cobranzaAbonoDao.findWhereIdPedidoEquals(pedidoDto.getIdPedido());
                    for (SgfensCobranzaAbono cAbonoDto : cobranzaAbonosDto){
                        WsItemPedidoCobranzaAbono wsCAbono = new WsItemPedidoCobranzaAbono();
                        wsCAbono.setComentarios(cAbonoDto.getComentarios());
                        wsCAbono.setFechaAbono(cAbonoDto.getFechaAbono());
                        wsCAbono.setFolioCobranzaMovil(cAbonoDto.getFolioCobranzaMovil());
                        wsCAbono.setIdCobranzaAbono(cAbonoDto.getIdCobranzaAbono());
                        wsCAbono.setIdCobranzaMetodoPago(cAbonoDto.getIdCobranzaMetodoPago());
                        wsCAbono.setIdEstatus(cAbonoDto.getIdEstatus());
                        wsCAbono.setIdentificadorOperacion(cAbonoDto.getIdentificadorOperacion());
                        wsCAbono.setLatitud(cAbonoDto.getLatitud());
                        wsCAbono.setLongitud(cAbonoDto.getLongitud());
                        wsCAbono.setMontoAbono(cAbonoDto.getMontoAbono());
                        
                        BancoOperacion bancoOperacionDto = bancoOperacionDao.findByPrimaryKey(cAbonoDto.getIdOperacionBancaria());
                        if (bancoOperacionDto!=null){
                            WsItemBancoOperacion wsBOperacion = new WsItemBancoOperacion();
                            
                            wsBOperacion.setBancoAuth(bancoOperacionDto.getBancoAuth());
                            wsBOperacion.setBancoOperFecha(bancoOperacionDto.getBancoOperFecha());
                            wsBOperacion.setBancoOperIssuingBank(bancoOperacionDto.getBancoOperIssuingBank());
                            wsBOperacion.setBancoOperType(bancoOperacionDto.getBancoOperType());
                            wsBOperacion.setBancoOrderId(bancoOperacionDto.getBancoOrderId());
                            wsBOperacion.setIdEstatus(bancoOperacionDto.getIdEstatus());
                            wsBOperacion.setIdOperacionBancaria(bancoOperacionDto.getIdOperacionBancaria());
                            wsBOperacion.setMonto(bancoOperacionDto.getMonto());
                            wsBOperacion.setNoTarjeta(bancoOperacionDto.getNoTarjeta());
                            wsBOperacion.setNombreTitular(bancoOperacionDto.getNombreTitular());
                            wsBOperacion.setDataArqc(bancoOperacionDto.getDataArqc());
                            wsBOperacion.setDataAid(bancoOperacionDto.getDataAid());
                            wsBOperacion.setDataTsi(bancoOperacionDto.getDataTsi());
                            wsBOperacion.setDataRef(bancoOperacionDto.getDataRef());
                            wsBOperacion.setDataExtra1(bancoOperacionDto.getDataExtra1());
                            wsBOperacion.setDataExtra2(bancoOperacionDto.getDataExtra2());
                            
                            wsCAbono.setWsItemBancoOperacion(wsBOperacion);
                        }
                        
                        wsItemPedido.getWsItemCobranzaAbono().add(wsCAbono);
                    }
                }
                
                try{
                    //Después de que se consulta, deja de marcarse como modificado
                    pedidoDto.setIsModificadoConsola((short)0);
                    pedidoDao.update(pedidoDto.createPk(), pedidoDto);
                }catch(Exception ex){}
                
                listaPedidos.add(wsItemPedido);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return listaPedidos;
    }
    
    /**
     * Obtiene el catalogo de Conceptos de una empresa, haciendo login por empleado
     * @param empleadoDtoRequestJSON Datos de autenticacion en formato JSON, correspondientes a objeto EmpleadoDtoRequest
     */
    public ConsultaPedidosResponse getTotalPedidosByEmpleado(String empleadoDtoRequestJSON, String consultaPedidosRequestJSON) {
        ConsultaPedidosResponse response;                
        
        EmpleadoDtoRequest empleadoDtoRequest = null;
        ConsultaPedidosRequest consultaPedidosRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            consultaPedidosRequest = gson.fromJson(consultaPedidosRequestJSON, ConsultaPedidosRequest.class);
            
            response = this.getTotalPedidosByEmpleado(empleadoDtoRequest,consultaPedidosRequest);
        }catch(Exception ex){
            response = new ConsultaPedidosResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
            
        }
        
        return response;
    }
    
    
    
    public ConsultaPedidosResponse getTotalPedidosByEmpleado(EmpleadoDtoRequest empleadoDtoRequest, ConsultaPedidosRequest consultaPedidosRequest){
        ConsultaPedidosResponse response = new ConsultaPedidosResponse();
        
        try{
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                int idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                //int idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                int idUsuario = empleadoBO.getEmpleado().getIdUsuarios();
                //Si se encontro el registro buscamos su catalogo de pedidos
                if (idEmpresa > 0) {
                    String filtroAdicional = "";
                    if (consultaPedidosRequest.getListaIdPedidoServerFiltro().size()>0){
                        filtroAdicional += " AND ID_PEDIDO IN (";
                        
                        for (int item : consultaPedidosRequest.getListaIdPedidoServerFiltro())
                            filtroAdicional += item + ",";
                        
                        if (filtroAdicional.endsWith(","))
                            filtroAdicional =  filtroAdicional.substring(0, filtroAdicional.length()-1);
                        
                        filtroAdicional += " )";
                    }
                    if (consultaPedidosRequest.getListaFolioPedidoMovilFiltro().size()>0){
                        filtroAdicional += " AND FOLIO_PEDIDO_MOVIL IN (";
                        
                        for (String item : consultaPedidosRequest.getListaFolioPedidoMovilFiltro())
                            filtroAdicional += "'" + item + "',";
                        
                        if (filtroAdicional.endsWith(","))
                            filtroAdicional =  filtroAdicional.substring(0, filtroAdicional.length()-1);
                        
                        filtroAdicional += " )";
                    }
                    if (consultaPedidosRequest.isFiltroModificadoConsola()){
                        //Filtra los Pedidos del Empleado que fueron modificados en consola
                        // para hacer menor el tamaño de descarga
                        filtroAdicional += " AND IS_MODIFICADO_CONSOLA=1 ";
                    }//Si no se filtra por modificados, entonces se descargaran todos (o aplicando los filtros por ID y folio movil)
                    response.setError(false);
                    
                    //response.setWsItemPedido(this.getListaPedidos(idEmpresa, idUsuario, filtroAdicional));
                    response.setTotalRegistros(this.getTotalListaPedidos(idEmpresa, idUsuario,-1,-1, filtroAdicional));
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            } else {
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        }catch (Exception e) {
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al consultar pedidos del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    
    /**
     * Consulta un listado de Conceptos por empresa
     * @param idEmpresa ID de la empresa que se va a consultar, en caso de ser sucursal se recuperan tambien lo correspondiente a la empresa padre
     * @return Lista de objetos WsItemConcepto
     */
    public int getTotalListaPedidos(int idEmpresa, int idUsuario , int limMin , int limMax,String filtroAdicional) {

        WsListaPedidos wsListaPedidos = new WsListaPedidos();
        
        int totalPedidos = 0;

        //Buscamos todos los pedidos del empleado
        String filtroBusqueda = "";
        
        if (idUsuario>0){ //filtro por usuario vendedor
                //--filtro adicional (realizado por leonardo) para obtener los pedidos de vendedores asignados al usuario
                String filtroVendedoresAsignados = " OR ID_USUARIO_VENDEDOR IN (SELECT ID_USUARIO_EMPLEADO_ASIGNADO FROM SGFENS_ASIGNACION_EMPLEADOS WHERE ID_ESTATUS = 1 AND ID_USUARIO_EMPLEADO = " + idUsuario +" )";                
                //--
                filtroBusqueda += " AND (ID_USUARIO_VENDEDOR = " + idUsuario + " OR ID_USUARIO_VENDEDOR_REASIGNADO = " + idUsuario + filtroVendedoresAsignados + ")";                
        }
        
       
        if (StringManage.getValidString(filtroAdicional).length()>0){
                filtroBusqueda += " " + filtroAdicional;
        }
        
        SgfensPedido[] arrayPedidoDto;
        SGPedidoBO pedidosBO = new SGPedidoBO(getConn());

        try {
            
            arrayPedidoDto = pedidosBO.findPedido(-1, idEmpresa, limMin, limMax, filtroBusqueda );           
                        
            if (arrayPedidoDto!=null){                
                return arrayPedidoDto.length;
            }
                
                
        } catch (Exception e) {}
        
        return totalPedidos;
    }
    
    
    public ConsultaPedidosResponse getPedidosByEmpleadoWithLimits(String empleadoDtoRequestJSON, String consultaPedidosRequestJSON, int limMin , int limMax){
        ConsultaPedidosResponse response;
        
        EmpleadoDtoRequest empleadoDtoRequest = null;
        ConsultaPedidosRequest consultaPedidosRequest = null;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            consultaPedidosRequest = gson.fromJson(consultaPedidosRequestJSON, ConsultaPedidosRequest.class);
            
            response = this.getPedidosByEmpleadoWithLimits(empleadoDtoRequest, consultaPedidosRequest ,limMin,limMax);
        }catch(Exception ex){
            response = new ConsultaPedidosResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    
    
    public ConsultaPedidosResponse getPedidosByEmpleadoWithLimits(EmpleadoDtoRequest empleadoDtoRequest, ConsultaPedidosRequest consultaPedidosRequest, int limMin , int limMax){
        ConsultaPedidosResponse response = new ConsultaPedidosResponse();
        
        try{
            //Consultamos y obtenemos el ID de Empresa del Usuario
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), empleadoDtoRequest.getEmpleadoPassword())) {
                int idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                //int idEmpleado = empleadoBO.getEmpleado().getIdEmpleado();
                int idUsuario = empleadoBO.getEmpleado().getIdUsuarios();
                //Si se encontro el registro buscamos su catalogo de pedidos
                if (idEmpresa > 0) {
                    String filtroAdicional = "";
                    if (consultaPedidosRequest.getListaIdPedidoServerFiltro().size()>0){
                        filtroAdicional += " AND ID_PEDIDO IN (";
                        
                        for (int item : consultaPedidosRequest.getListaIdPedidoServerFiltro())
                            filtroAdicional += item + ",";
                        
                        if (filtroAdicional.endsWith(","))
                            filtroAdicional =  filtroAdicional.substring(0, filtroAdicional.length()-1);
                        
                        filtroAdicional += " )";
                    }
                    if (consultaPedidosRequest.getListaFolioPedidoMovilFiltro().size()>0){
                        filtroAdicional += " AND FOLIO_PEDIDO_MOVIL IN (";
                        
                        for (String item : consultaPedidosRequest.getListaFolioPedidoMovilFiltro())
                            filtroAdicional += "'" + item + "',";
                        
                        if (filtroAdicional.endsWith(","))
                            filtroAdicional =  filtroAdicional.substring(0, filtroAdicional.length()-1);
                        
                        filtroAdicional += " )";
                    }
                    if (consultaPedidosRequest.isFiltroModificadoConsola()){
                        //Filtra los Pedidos del Empleado que fueron modificados en consola
                        // para hacer menor el tamaño de descarga
                        filtroAdicional += " AND IS_MODIFICADO_CONSOLA=1 ";
                    }//Si no se filtra por modificados, entonces se descargaran todos (o aplicando los filtros por ID y folio movil)
                    response.setError(false);
                    response.setWsItemPedido(this.getListaPedidos(idEmpresa, idUsuario, filtroAdicional,limMin,limMax));
                }
                
                //registramos ubicacion
                try{new InsertaActualizaWsBO(getConn()).actualizarUbicacionEmpleado(empleadoDtoRequest);}catch(Exception ex){}
            } else {
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
            }
        }catch (Exception e) {
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al consultar pedidos del usuario. " + e.getLocalizedMessage());
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
    
    
    
    private List<WsItemPedido> getListaPedidos(int idEmpresa, int idUsuario, String filtroAdicional, int limMin , int limMax) {
        List<WsItemPedido> listaPedidos = new ArrayList<WsItemPedido>();
        
        SGPedidoBO sgPedidoBO = new SGPedidoBO(getConn());
        try{
            String filtroBusqueda = "";
            //filtro por id_estatus
            //filtroBusqueda += " AND ID_ESTATUS_PEDIDO IN (1)";
            //filtro por saldo_pagado
            //filtroBusqueda += " AND SALDO_PAGADO<TOTAL";
            if (idUsuario>0){ //filtro por usuario vendedor
                //--filtro adicional (realizado por leonardo) para obtener los pedidos de vendedores asignados al usuario
                String filtroVendedoresAsignados = " OR ID_USUARIO_VENDEDOR IN (SELECT ID_USUARIO_EMPLEADO_ASIGNADO FROM SGFENS_ASIGNACION_EMPLEADOS WHERE ID_ESTATUS = 1 AND ID_USUARIO_EMPLEADO = " + idUsuario +" )";                
                //filtroBusqueda += " AND (ID_USUARIO_VENDEDOR = " + idUsuario + " OR ID_USUARIO_VENDEDOR_REASIGNADO = " + idUsuario + filtroVendedoresAsignados + ")";                               
                // 19-04-2016 se cambia query anterior, por el siguiente, para los casos de asignación a empleado desde consola.
                filtroBusqueda += " AND (ID_USUARIO_VENDEDOR = " + idUsuario + " OR ID_USUARIO_CONDUCTOR_ASIGNADO=" + idUsuario + " OR ID_USUARIO_VENDEDOR_ASIGNADO=" + idUsuario + " OR ID_USUARIO_VENDEDOR_REASIGNADO = " + idUsuario + filtroVendedoresAsignados + ")";                
            }
            if (StringManage.getValidString(filtroAdicional).length()>0)
                filtroBusqueda += " " + filtroAdicional;
            SgfensPedido[] pedidosDtoArray = sgPedidoBO.findPedido(-1, idEmpresa, limMin, limMax, filtroBusqueda);
            
            SgfensPedidoDaoImpl pedidoDao = new SgfensPedidoDaoImpl(getConn());
            SgfensPedidoProductoDaoImpl pedidoProductoDao = new SgfensPedidoProductoDaoImpl(getConn());
            SgfensPedidoImpuestoDaoImpl pedidoImpuestoDao = new SgfensPedidoImpuestoDaoImpl(getConn());
            SgfensCobranzaAbonoDaoImpl cobranzaAbonoDao = new SgfensCobranzaAbonoDaoImpl(getConn());
            ImpuestoDaoImpl impuestoDao = new ImpuestoDaoImpl(getConn());
            BancoOperacionDaoImpl bancoOperacionDao = new BancoOperacionDaoImpl(getConn());
            
            for (SgfensPedido pedidoDto : pedidosDtoArray){
                WsItemPedido wsItemPedido = new WsItemPedido();
                
                //Asignamos valores directos
                wsItemPedido.setAdelanto(pedidoDto.getAdelanto());
                wsItemPedido.setComentarios(pedidoDto.getComentarios());
                wsItemPedido.setConsecutivoPedido(pedidoDto.getConsecutivoPedido());
                wsItemPedido.setDescuentoMonto(pedidoDto.getDescuentoMonto());
                wsItemPedido.setDescuentoMotivo(pedidoDto.getDescuentoMotivo());
                wsItemPedido.setDescuentoTasa(pedidoDto.getDescuentoTasa());
                wsItemPedido.setFechaEntrega(pedidoDto.getFechaEntrega());
                wsItemPedido.setFechaPedido(pedidoDto.getFechaPedido());
                wsItemPedido.setFechaTentativaPago(pedidoDto.getFechaTentativaPago());
                wsItemPedido.setFolioPedido(pedidoDto.getFolioPedido());
                if(StringManage.getValidString(pedidoDto.getFolioPedidoMovil()).length() > 0){
                    wsItemPedido.setFolioPedidoMovil(pedidoDto.getFolioPedidoMovil());
                }else{
                    String folioMovilConsolaAsignado = SGPedidoBO.generaFolioMovil(idEmpresa);
                    try{
                        pedidoDto.setFolioPedidoMovil(folioMovilConsolaAsignado);
                        
                        pedidoDao.update(pedidoDto.createPk(), pedidoDto);
                        wsItemPedido.setFolioPedidoMovil(folioMovilConsolaAsignado);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                wsItemPedido.setIdCliente(pedidoDto.getIdCliente());
                wsItemPedido.setIdComprobanteFiscal(pedidoDto.getIdComprobanteFiscal());
                wsItemPedido.setIdEmpresa(pedidoDto.getIdEmpresa());
                wsItemPedido.setIdEstatusPedido(pedidoDto.getIdEstatusPedido());
                wsItemPedido.setIdPedido(pedidoDto.getIdPedido());
                wsItemPedido.setLatitud(pedidoDto.getLatitud());
                wsItemPedido.setLongitud(pedidoDto.getLongitud());
                wsItemPedido.setSaldoPagado(pedidoDto.getSaldoPagado());
                wsItemPedido.setSubtotal(pedidoDto.getSubtotal());
                wsItemPedido.setTiempoEntregaDias(pedidoDto.getTiempoEntregaDias());
                wsItemPedido.setTipoMoneda(pedidoDto.getTipoMoneda());
                wsItemPedido.setTotal(pedidoDto.getTotal());
                wsItemPedido.setIdServidorFolioMovilEmpleado(pedidoDto.getIdFolioMovilEmpleado());
                wsItemPedido.setFolioMovilEmpleadoGenerado(pedidoDto.getFolioMovilEmpleadoGenerado());
                
                //Asignamos objetos relacionados a pedido
                { //Conceptos (productos / servicios)  
                    SgfensPedidoProducto[] pedidoProductosDto = pedidoProductoDao.findWhereIdPedidoEquals(pedidoDto.getIdPedido());
                    for (SgfensPedidoProducto pedidoProdDto : pedidoProductosDto) {
                        WsItemPedidoConcepto wsPConcepto = new WsItemPedidoConcepto();
                        wsPConcepto.setCantidad(pedidoProdDto.getCantidad());
                        wsPConcepto.setDescripcion(pedidoProdDto.getDescripcion());
                        wsPConcepto.setDescuentoMonto(pedidoProdDto.getDescuentoMonto());
                        wsPConcepto.setDescuentoPorcentaje(pedidoProdDto.getDescuentoPorcentaje());
                        wsPConcepto.setIdConcepto(pedidoProdDto.getIdConcepto());
                        wsPConcepto.setIdentificacion(pedidoProdDto.getIdentificacion());
                        wsPConcepto.setPrecioUnitario(pedidoProdDto.getPrecioUnitario());
                        wsPConcepto.setSubtotal(pedidoProdDto.getSubtotal());
                        wsPConcepto.setUnidad(pedidoProdDto.getUnidad());
                        
                        wsPConcepto.setCostoUnitario(pedidoProdDto.getCostoUnitario());
                        wsPConcepto.setPorcentajeComisionEmpleado(pedidoProdDto.getPorcentajeComisionEmpleado());
                        
                        wsItemPedido.getWsItemPedidoConcepto().add(wsPConcepto);
                    }
                }
                
                { //Impuestos
                    SgfensPedidoImpuesto[] pedidoImpuestosDto = pedidoImpuestoDao.findWhereIdPedidoEquals(pedidoDto.getIdPedido());
                    for (SgfensPedidoImpuesto pedidoImpDto : pedidoImpuestosDto ){
                        WsItemPedidoImpuesto wsPImpuesto = new WsItemPedidoImpuesto();
                        
                        Impuesto impuestoDto = impuestoDao.findByPrimaryKey(pedidoImpDto.getIdImpuesto());
                        if (impuestoDto!=null){
                            wsPImpuesto.setDescripcion(impuestoDto.getDescripcion());
                            wsPImpuesto.setIdEstatus(impuestoDto.getIdEstatus());
                            wsPImpuesto.setImpuestoLocal(impuestoDto.getImpuestoLocal());
                            wsPImpuesto.setNombre(impuestoDto.getNombre());
                            wsPImpuesto.setPorcentaje(impuestoDto.getPorcentaje());
                            wsPImpuesto.setTraslado(impuestoDto.getTrasladado());

                            wsItemPedido.getWsItemPedidoImpuesto().add(wsPImpuesto);
                        }
                    }                    
                }
                
                { //Abonos cobranza
                    SgfensCobranzaAbono[] cobranzaAbonosDto = cobranzaAbonoDao.findWhereIdPedidoEquals(pedidoDto.getIdPedido());
                    for (SgfensCobranzaAbono cAbonoDto : cobranzaAbonosDto){
                        WsItemPedidoCobranzaAbono wsCAbono = new WsItemPedidoCobranzaAbono();
                        wsCAbono.setComentarios(cAbonoDto.getComentarios());
                        wsCAbono.setFechaAbono(cAbonoDto.getFechaAbono());
                        wsCAbono.setFolioCobranzaMovil(cAbonoDto.getFolioCobranzaMovil());
                        wsCAbono.setIdCobranzaAbono(cAbonoDto.getIdCobranzaAbono());
                        wsCAbono.setIdCobranzaMetodoPago(cAbonoDto.getIdCobranzaMetodoPago());
                        wsCAbono.setIdEstatus(cAbonoDto.getIdEstatus());
                        wsCAbono.setIdentificadorOperacion(cAbonoDto.getIdentificadorOperacion());
                        wsCAbono.setLatitud(cAbonoDto.getLatitud());
                        wsCAbono.setLongitud(cAbonoDto.getLongitud());
                        wsCAbono.setMontoAbono(cAbonoDto.getMontoAbono());
                        
                        BancoOperacion bancoOperacionDto = bancoOperacionDao.findByPrimaryKey(cAbonoDto.getIdOperacionBancaria());
                        if (bancoOperacionDto!=null){
                            WsItemBancoOperacion wsBOperacion = new WsItemBancoOperacion();
                            
                            wsBOperacion.setBancoAuth(bancoOperacionDto.getBancoAuth());
                            wsBOperacion.setBancoOperFecha(bancoOperacionDto.getBancoOperFecha());
                            wsBOperacion.setBancoOperIssuingBank(bancoOperacionDto.getBancoOperIssuingBank());
                            wsBOperacion.setBancoOperType(bancoOperacionDto.getBancoOperType());
                            wsBOperacion.setBancoOrderId(bancoOperacionDto.getBancoOrderId());
                            wsBOperacion.setIdEstatus(bancoOperacionDto.getIdEstatus());
                            wsBOperacion.setIdOperacionBancaria(bancoOperacionDto.getIdOperacionBancaria());
                            wsBOperacion.setMonto(bancoOperacionDto.getMonto());
                            wsBOperacion.setNoTarjeta(bancoOperacionDto.getNoTarjeta());
                            wsBOperacion.setNombreTitular(bancoOperacionDto.getNombreTitular());
                            wsBOperacion.setDataArqc(bancoOperacionDto.getDataArqc());
                            wsBOperacion.setDataAid(bancoOperacionDto.getDataAid());
                            wsBOperacion.setDataTsi(bancoOperacionDto.getDataTsi());
                            wsBOperacion.setDataRef(bancoOperacionDto.getDataRef());
                            wsBOperacion.setDataExtra1(bancoOperacionDto.getDataExtra1());
                            wsBOperacion.setDataExtra2(bancoOperacionDto.getDataExtra2());
                            
                            wsCAbono.setWsItemBancoOperacion(wsBOperacion);
                        }
                        
                        wsItemPedido.getWsItemCobranzaAbono().add(wsCAbono);
                    }
                }
                
                try{
                    //Después de que se consulta, deja de marcarse como modificado
                    pedidoDto.setIsModificadoConsola((short)0);
                    pedidoDao.update(pedidoDto.createPk(), pedidoDto);
                }catch(Exception ex){}
                
                listaPedidos.add(wsItemPedido);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return listaPedidos;
    }
    
}