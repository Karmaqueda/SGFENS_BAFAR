/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws.bo;

import com.tsp.sct.bo.BancoOperacionBO;
import com.tsp.sct.bo.ComprobanteFiscalBO;
import com.tsp.sct.bo.EmpleadoBO;
import com.tsp.sct.bo.EmpresaBO;
import com.tsp.sct.bo.EstatusComprobanteBO;
import com.tsp.sct.bo.SGCobranzaAbonoBO;
import com.tsp.sct.bo.SGCobranzaMetodoPagoBO;
import com.tsp.sct.bo.SGPedidoBO;
import com.tsp.sct.bo.UsuarioBO;
import com.tsp.sct.bo.VentaMetodoPagoBO;
import com.tsp.sct.config.Configuration;
import com.tsp.sct.dao.dto.BancoOperacion;
import com.tsp.sct.dao.dto.ComprobanteFiscal;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.dao.dto.SgfensCobranzaAbono;
import com.tsp.sct.dao.dto.SgfensCobranzaMetodoPago;
import com.tsp.sct.dao.dto.SgfensPedido;
import com.tsp.sct.dao.dto.TipoPago;
import com.tsp.sct.dao.jdbc.SgfensPedidoDaoImpl;
import com.tsp.sct.dao.jdbc.TipoPagoDaoImpl;
import com.tsp.sct.util.DateManage;
import com.tsp.sct.util.GenericValidator;
import com.tsp.sgfens.sesion.ComprobanteFiscalSesion;
import com.tsp.sgfens.sesion.PedidoSesion;
import com.tsp.sgfens.ws.request.CrearFacturaDePedidoRequest;
import com.tsp.sgfens.ws.request.EmpleadoDtoRequest;
import com.tsp.sgfens.ws.response.CrearFacturaResponse;
import java.sql.Connection;

/**
 *
 * @author Leonardo
 */
public class PedidoWsBOLEO {
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
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
    /*public CrearFacturaResponse crearFacturaDePedidoCBB(String empleadoDtoRequestJSON, String crearFacturaDePedidoRequestJSON){
        CrearFacturaResponse response;
                
        EmpleadoDtoRequest empleadoDtoRequest;
        CrearFacturaDePedidoRequest crearFacturaDePedidoRequest;
        try{
            //Transformamos de formato JSON a objeto
            empleadoDtoRequest = gson.fromJson(empleadoDtoRequestJSON, EmpleadoDtoRequest.class);
            crearFacturaDePedidoRequest = gson.fromJson(crearFacturaDePedidoRequestJSON, CrearFacturaDePedidoRequest.class);
            
            response = this.crearFacturaDePedidoCBB(empleadoDtoRequest,crearFacturaDePedidoRequest);
        }catch(Exception ex){
            response = new CrearFacturaResponse();
            response.setError(true);
            response.setNumError(901);
            response.setMsgError("Los datos enviados no corresponden a los requeridos. No se pudo transformar de un cadena JSON a objetos. " + ex.toString());
            ex.printStackTrace();
        }
        
        return response;
    }*/
    
    /**
     * Crea un Comprobante Fiscal a partir de un Pedido
     * previamente registrado en el sistema.
     * @param empleadoDtoRequest Datos de acceso del empleado
     * @param crearFacturaDePedidoRequest Información y datos para procesar facturación
     * @return CrearFacturaResponse objeto con datos sobre la facturación realizada
     */
    public CrearFacturaResponse crearFacturaDePedidoCBB(EmpleadoDtoRequest empleadoDtoRequest, CrearFacturaDePedidoRequest crearFacturaDePedidoRequest){
        CrearFacturaResponse response = new CrearFacturaResponse();
        
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(this.conn);
            if (empleadoBO.login(empleadoDtoRequest.getEmpleadoUsuario(), 
                    empleadoDtoRequest.getEmpleadoPassword())) {
                
                int idFormaPago = 1; //Pago en una sola exhibición
                String tipoComprobante = "ingreso";
                int idTipoComprobante = 13; //Factura CBB
                int tipoMonedaInt = -1;
                String tipoMonedaStr ="MXN";

                Configuration appConfig = new Configuration();
                GenericValidator validator = new GenericValidator();
                UsuarioBO usuarioBO = empleadoBO.getUsuarioBO();
                int idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
                
                Empresa empresaMatrizDto = null;
                String rfcEmpresaMatriz ="";
                try{
                    empresaMatrizDto = new EmpresaBO(this.conn).getEmpresaMatriz(idEmpresa);
                    rfcEmpresaMatriz = empresaMatrizDto.getRfc();
                }catch(Exception ex){
                    response.setError(true);
                    response.setNumError(901);
                    response.setMsgError("No se pudo recuperar la información de la empresa matriz del empleado. Verifique con su administrador de sistema." + ex.toString());
                    return response;
                }

                SGPedidoBO pedidoSesionBO = new SGPedidoBO(crearFacturaDePedidoRequest.getIdServerPedido(),this.conn);
                if (pedidoSesionBO.getPedido()!=null){
                    PedidoSesion pedidoSesion = pedidoSesionBO.getSessionFromPedidoDB();
                    if (pedidoSesion!=null){
                        if (pedidoSesion.getIdComprobanteFiscal()>0){
                            response.setError(true);
                            response.setNumError(901);
                            response.setMsgError("Este pedido ya fue facturado, el ID de la factura en servidor es: " + pedidoSesion.getIdComprobanteFiscal());
                            response.setIdObjetoCreado(pedidoSesion.getIdComprobanteFiscal());
                            return response;
                        }else{
                            
                            ComprobanteFiscalBO comprobanteFiscalBO = new ComprobanteFiscalBO(this.conn);
                            //Cfd32BO cfd32BO = null;
                            //File archivoXML = null;
                            //File archivoPDF = null;

                            ComprobanteFiscalSesion cfdiSesion = pedidoSesionBO.convertirAComprobanteFiscalSesion(pedidoSesion);
                            
                            //Almacenar Nuevo Tipo de Pago--------------
                            TipoPagoDaoImpl tipoPagoDao = new TipoPagoDaoImpl(this.conn);
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
                                    SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(this.conn);
                                    SgfensCobranzaAbono primerAbonoDto = cobranzaAbonoBO.getPrimerAbonoPedido(pedidoSesion.getIdPedido());
                                    
                                    if (primerAbonoDto!=null){
                                        idTipoPago = primerAbonoDto.getIdCobranzaMetodoPago();
                                        if (idTipoPago == VentaMetodoPagoBO.METODO_PAGO_TDC){
                                            
                                            BancoOperacion bancoOperacionDto = new BancoOperacionBO(primerAbonoDto.getIdOperacionBancaria(),this.conn).getBancoOperacion();
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
                                SgfensCobranzaMetodoPago cobranzaMetodoPago = new SGCobranzaMetodoPagoBO(idTipoPago,this.conn).getCobranzaMetodoPago();
                                
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

                        ComprobanteFiscal comprobanteFiscalDto = null;
                       // if (archivoXML!=null && cfd32BO!=null){
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
                            //cfdiSesion.setArchivo_cfd(archivoXML.getName());
                            //*****CADENA SIN ENCRIPTAR******
                            //cfdiSesion.setCadena_original(cfd32BO.getCfd().getCadenaOriginal());
                            //cfdiSesion.setSello_digital(cfd32BO.getComprobanteFiscal().getSello());
                            cfdiSesion.setTipo_moneda_int(tipoMonedaInt);
                            cfdiSesion.setTipo_moneda(cfdiSesion.getTipo_moneda());
                            cfdiSesion.setTipo_cambio(1);
                            //cfdiSesion.setUuid(cfd32BO.getTimbreFiscalDigital().getUUID());
                            //cfdiSesion.setSello_sat(cfd32BO.getTimbreFiscalDigital().getSelloSAT());

                            //Para generar un nuevo comprobanteFiscal en cada Guardar
                            //Deshabilitar en caso de querer actualizar en lugar de generar una nueva
                            //cfdiSesion.setIdComprobanteFiscal(0);

                            /*try{
                                comprobanteFiscalDto = comprobanteFiscalBO.creaActualizaComprobanteFiscal(cfdiSesion, usuarioBO);

                               // cfd32BO.copiarCFDIenvioAsincrono(archivoXML);
                                
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
                            }*/

                        //}

                        //Si la factura se genero desde un pedido, se actualiza el pedido
                        if (cfdiSesion.getIdPedido()>0){
                            try{
                                    SgfensPedido pedidoDto = new SGPedidoBO(cfdiSesion.getIdPedido(),this.conn).getPedido();
                                    if (pedidoDto!=null){
                                        pedidoDto.setIdComprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal());

                                        new SgfensPedidoDaoImpl(this.conn).update(pedidoDto.createPk(), pedidoDto);
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
                            /*if (crearFacturaDePedidoRequest.isEnviarCorreoRepresentacionImpresa()){
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
                                            
                                            String msgErrorCorreo = comprobanteFiscalBO.enviaNotificacionNuevaComprobanteFiscal(destinatarios,archivoXML, archivoPDF);
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
                            }*/

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
        }
        
        return response;
    }
    
}
