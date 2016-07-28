<%-- 
    Document   : catSmsEnvio_ajax
    Created on : 24/03/2016, 01:27:38 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.bo.ZonaHorariaBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaDaoImpl"%>
<%@page import="com.tsp.sgfens.sesion.SmsEnvioSesion"%>
<%@page import="com.tsp.sgfens.sesion.SmsEnvioDestinatariosSesion"%>
<%@page import="com.tsp.sct.util.GenericMethods"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.SmsEnvioDetalleDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SmsEnvioDetalle"%>
<%@page import="com.tsp.sct.dao.jdbc.SmsEnvioLoteDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SmsEnvioLote"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="smsEnvioSesion" scope="session" class="com.tsp.sgfens.sesion.SmsEnvioSesion"/>
<%
    String msgError="";
    String msgExitoExtra="";
    
    /*
    *   Modos:
    *       -1  =  No se eligio (ERROR)
    * 
    *        1 = Guardar de Sesion a BD
    */
    
    int mode=-1;
    try{
        mode = Integer.parseInt(request.getParameter("mode"));
    }catch(NumberFormatException e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
     
    GenericValidator gc = new GenericValidator();
    Gson gson = new Gson();    
    
    /*
     * Procesamiento
     */
    if (mode==-1){
        msgError = "No se eligio una acción correcta. Reintente el llenado de los datos.";
    }else if (mode==1){
        //1 = Guardar de Sesion a BD
        EmpresaBO empresaBO = new EmpresaBO(user.getConn());
        Empresa empresaMatrizDto = empresaBO.getEmpresaMatriz(idEmpresa);
        
        int creditosSmsRequeridos = smsEnvioSesion!=null?smsEnvioSesion.calculaCreditosSMS():0;
        if (smsEnvioSesion==null){
            msgError+= "<ul>No hay datos de envío SMS en sesión. Vuelva a capturar el mensaje y destinatarios.";
        }else{
            //validamos creditos de la empresa
            if (creditosSmsRequeridos > empresaMatrizDto.getCreditosSms()){
                msgError += "<ul>No cuentas con créditos SMS suficientes para completar esta acción. Créditos SMS disponibles: " + empresaMatrizDto.getCreditosSms();
                empresaBO.enviaNotificacionEmpresaSinCreditosSMS(empresaMatrizDto);
            }
        }
        
            
        if (msgError.equals("")){
            SmsEnvioLoteDaoImpl smsEnvioLoteDao = new SmsEnvioLoteDaoImpl(user.getConn());
            SmsEnvioDetalleDaoImpl smsEnvioDetalleDao = new SmsEnvioDetalleDaoImpl(user.getConn());
            
            //Primero creamos Lote padre
            SmsEnvioLote smsEnvioLote = new SmsEnvioLote();
            smsEnvioLote.setIdEmpresa(idEmpresa);
            smsEnvioLote.setIdEstatus(1);
            smsEnvioLote.setIdSmsPlantilla(smsEnvioSesion.getIdPlantilla());
            try{
                smsEnvioLote.setFechaHrCaptura(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
            }catch(Exception e){
                smsEnvioLote.setFechaHrCaptura(new Date());
            }
            smsEnvioLote.setFechaHrProgramaEnvio(smsEnvioSesion.getFechaHoraEnvioProgramado());
            smsEnvioLote.setCantidadDestinatarios(smsEnvioSesion.getListaDestinatarios().size());
            smsEnvioLote.setCantidadCreditosSms(creditosSmsRequeridos);
            smsEnvioLote.setEnvioInmediato(smsEnvioSesion.isEnvioInmediato()?1:0);
            smsEnvioLote.setIsSmsSistema(0);//no es auto-creado (ventas, cobranza, factura, etc.)
            smsEnvioLote.setIdUsuarioPretoriano(user.getUser().getIdUsuarios());
            //smsEnvioLote.setIdUsuarioVentas(0);//Solo para sistema de Ventas
            // se usara un Dispositivo Movil especifico si la Empresa matriz tiene uno configurado
            smsEnvioLote.setIdSmsDispositivoMovil(empresaMatrizDto.getIdSmsDispositivoMovil());
            smsEnvioLote.setAsunto(smsEnvioSesion.getAsunto());
            smsEnvioLote.setMensaje(smsEnvioSesion.creaContenidoSMS());
            
            try{
                smsEnvioLoteDao.insert(smsEnvioLote);
                
                //Después creamos los Detalles (mensajes especificos para cada destinatario) del lote
                for (SmsEnvioDestinatariosSesion smsDest : smsEnvioSesion.getListaDestinatarios()){
                    try{
                        SmsEnvioDetalle smsEnvioDetalleDto = new SmsEnvioDetalle();
                        smsEnvioDetalleDto.setIdSmsEnvioLote(smsEnvioLote.getIdSmsEnvioLote());
                        smsEnvioDetalleDto.setFechaHrCreacion(smsEnvioLote.getFechaHrCaptura());
                        //smsEnvioDetalleDto.setFechaHrEnvio(null);//no se ha enviado, por lo tanto se deja en null
                        smsEnvioDetalleDto.setEnviado(0);
                        smsEnvioDetalleDto.setIntentosFallidos(0);
                        smsEnvioDetalleDto.setAsunto(smsEnvioSesion.getAsunto());
                        //En lugar de guardar el mismo mensaje en todos los detalles, marcamos que lo herede del Lote padre
                        //smsEnvioDetalleDto.setMensaje(smsEnvioSesion.creaContenidoSMS());
                        smsEnvioDetalleDto.setHeredarMensajeLote(1);
                        smsEnvioDetalleDto.setNumPartesSms(smsEnvioSesion.calculaPartesMensaje());
                        smsEnvioDetalleDto.setNumeroCelular(smsDest.getNumCelular());
                        smsEnvioDetalleDto.setDestIdCliente(smsDest.getIdCliente());
                        smsEnvioDetalleDto.setDestIdProspecto(smsDest.getIdProspecto());
                        smsEnvioDetalleDto.setDestIdEmpleado(smsDest.getIdEmpleado());
                        smsEnvioDetalleDto.setDestIdEmpresa(smsDest.getIdEmpresa());
                        smsEnvioDetalleDto.setDestIdSmsAgendaDest(smsDest.getIdAgendaDest());
                        smsEnvioDetalleDto.setIdEmpresa(smsEnvioLote.getIdEmpresa());
                        smsEnvioDetalleDto.setIdEstatus(1);

                        smsEnvioDetalleDao.insert(smsEnvioDetalleDto);
                    }catch(Exception ex){
                        ex.printStackTrace();
                        msgExitoExtra+= "<ul>Problema al almacenar detalle de envío: " + ex.toString();
                    }
                }
                
                //Restamos créditos a cuenta de empresa matriz
                empresaBO.restaCreditosSMSMatriz(idEmpresa, creditosSmsRequeridos);
                /*int saldoCreditoSms = empresaMatrizDto.getCreditosSms() - creditosSmsRequeridos;
                if (saldoCreditoSms<0)
                    saldoCreditoSms = 0;
                empresaMatrizDto.setCreditosSms(saldoCreditoSms);
                try{
                    / TODO : Metodo que actualice creditos y si llego a 0, envie notificaciones a cliente y administradores
                    // para comprar mas creditos de modulo SMS
                    new EmpresaDaoImpl(user.getConn()).update(empresaMatrizDto.createPk(), empresaMatrizDto);
                }catch(Exception ex){
                    ex.printStackTrace();
                }*/
                
                msgExitoExtra += "Mensaje registrado exitosamente.";
                
                //si todo fue exitoso, borramos sesion
                smsEnvioSesion = new SmsEnvioSesion();
                smsEnvioSesion = null;
                request.getSession().setAttribute("smsEnvioSesion", smsEnvioSesion);
                
            }catch(Exception ex){
                ex.printStackTrace();
                msgError+="<ul>Error inesperado al guardar Lote SMS: " + GenericMethods.exceptionStackTraceToString(ex);
            }
            
        }
        
    }
    
    if (msgError.equals("") && mode!=0){
        out.print("<!--EXITO-->" + msgExitoExtra);
    }
%>
<%
    if (!msgError.equals("")){
        out.print("<!--ERROR-->"+msgError);
    }
%>