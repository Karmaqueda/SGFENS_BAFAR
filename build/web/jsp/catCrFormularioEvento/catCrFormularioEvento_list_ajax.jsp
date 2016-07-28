<%-- 
    Document   : catCrFormularioEvento_list_ajax
    Created on : 12/07/2016, 12:22:31 PM
    Author     : ISCesar
--%>
<%@page import="com.tsp.sct.bo.CrEstadoSolicitudBO"%>
<%@page import="com.tsp.sct.cr.CrConsultaDispersionSapBafarResponse"%>
<%@page import="com.tsp.sct.cr.RevisionesCr"%>
<%@page import="com.tsp.sct.cr.CrAceptaContratoSapBafarResponse"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.util.GenericMethods"%>
<%@page import="com.tsp.sct.dao.jdbc.CrFrmEventoSolicitudDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrFrmEventoSolicitud"%>
<%@page import="com.tsp.sct.cr.CrCreaInterlocutorSapBafarResponse"%>
<%@page import="com.tsp.sct.cr.CrUtilConectaSAPBafar"%>
<%@page import="com.tsp.sct.bo.CrFormularioEventoBO"%>
<%@page import="com.tsp.sct.dao.dto.CrFormularioEvento"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page import="com.google.gson.Gson"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String msgError="";
    String msgExitoExtra="";
    
    /*
    *   Modos:
    *       -1  =  No se eligio (ERROR)
    * 
    *        1 = Registra solicitud contrato en SAP (genera BP y contrato)
    *        2 = Acepta contrato en SAP
    *        3 = Consulta Dispersion individual en SAP
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
        // 1 = Registra solicitud contrato en SAP (genera BP y contrato)
        int idFormularioEvento = -1;
        try{ idFormularioEvento = Integer.parseInt(request.getParameter("idFormularioEvento")); }catch(Exception e){}
        
        if (idFormularioEvento>0){
            CrFormularioEventoBO crFormularioEventoBO = new CrFormularioEventoBO(idFormularioEvento, user.getConn());
            CrFormularioEvento crFormularioEvento = crFormularioEventoBO.getCrFormularioEvento();            
            
            if (crFormularioEvento!=null){
                try{
                    CrCreaInterlocutorSapBafarResponse resp = crFormularioEventoBO.registraSolicitudEnSAP(crFormularioEvento, user);
                    
                    msgExitoExtra += "<ul>Contrato creado en SAP exitosamente."
                                     + "<ul>BP: " + resp.getBusinessPartner()
                                     + "<ul>No. contrato: " + resp.getNoContrato();
                }catch(Exception ex){
                    msgError += "<ul>Error en SAP durante la creación:" 
                                + "<ul>" + GenericMethods.exceptionStackTraceToString(ex);
                }
               
            }else{
                msgError+="<ul>Solicitud no existente en BD (Formulario Evento no encontrado). ID: " + idFormularioEvento;
            }
        }else{
            msgError+="<ul>No selecciono un Formulario Evento válido.";
        }
    }else if(mode == 2){
        // 2 = Acepta contrato en SAP
        
        int idFormularioEvento = -1;
        try{ idFormularioEvento = Integer.parseInt(request.getParameter("idFormularioEvento")); }catch(Exception e){}
        
        if (idFormularioEvento>0){
            CrFormularioEventoBO crFormularioEventoBO = new CrFormularioEventoBO(idFormularioEvento, user.getConn());
            CrFormularioEvento crFormularioEvento = crFormularioEventoBO.getCrFormularioEvento();
            if (crFormularioEvento!=null){
               CrUtilConectaSAPBafar crUtilConectaSAPBafar = new CrUtilConectaSAPBafar(user.getConn());
               
               CrFrmEventoSolicitud crFrmEventoSolicitud = crFormularioEventoBO.getFrmEventoSolicitud();
               String sapNoContrato = StringManage.getValidString(crFrmEventoSolicitud.getSapNoContrato());
               // consumimos web service
               CrAceptaContratoSapBafarResponse resp = crUtilConectaSAPBafar.registraAceptacionCreditoSAP(sapNoContrato);
               
                if (!resp.isError()){
                    try{
                        // Cambiamos de estatus
                        int idEstatusDestino = CrEstadoSolicitudBO.S_POR_DISPERSAR; // 10 = Por Dispersar
                        RevisionesCr revisionesCr = new RevisionesCr();            
                        revisionesCr.almacenaRegistroSolicitudBitacora(user.getConn(), idFormularioEvento, 0, idEmpresa, user.getUser().getIdUsuarios(), idEstatusDestino, "Firmas Cotejadas y Contrato Aceptado en SAP.", false);
                        
                        msgExitoExtra += "<ul>Contrato Aceptado en SAP exitosamente.";
                        
                    }catch(Exception ex){
                        msgError += "<ul>Error durante la actualización del estatus del crédito con los datos retornados por SAP. Detalle: " + GenericMethods.exceptionStackTraceToString(ex) ;
                    }
                }else{
                   msgError += "<ul>Error en SAP durante la creación:" 
                                + "<ul>" + resp.getNumError()
                                + "<ul>" + resp.getMsgError();
                }
               
            }else{
                msgError+="<ul>Solicitud no existente en BD (Formulario Evento no encontrado). ID: " + idFormularioEvento;
            }
        }else{
            msgError+="<ul>No selecciono un Formulario Evento válido.";
        }
        
    }else if(mode==3){
        // 3 = Consulta Dispersion individual en SAP
        
        
        int idFormularioEvento = -1;
        try{ idFormularioEvento = Integer.parseInt(request.getParameter("idFormularioEvento")); }catch(Exception e){}
        
        if (idFormularioEvento>0){
            CrFormularioEventoBO crFormularioEventoBO = new CrFormularioEventoBO(idFormularioEvento, user.getConn());
            CrFormularioEvento crFormularioEvento = crFormularioEventoBO.getCrFormularioEvento();
            if (crFormularioEvento!=null){
                
                try{
                    CrConsultaDispersionSapBafarResponse resp = crFormularioEventoBO.consultaDispersionEnSAPUnico(crFormularioEvento, user);
                    
                    if (!resp.isError()){
                        msgExitoExtra += "<ul>SAP indica dispersion realizada." + resp.getMsgExito();
                    }else{
                       msgError += "<ul>SAP:" 
                                + "<ul>" + resp.getNumError()
                                + "<ul>" + resp.getMsgError(); 
                    }
                    
                }catch(Exception ex){
                    msgError += "<ul>Error durante la consulta de dispersion: " 
                                + "<ul>" + GenericMethods.exceptionStackTraceToString(ex);
                }
               
            }else{
                msgError+="<ul>Solicitud no existente en BD (Formulario Evento no encontrado). ID: " + idFormularioEvento;
            }
        }else{
            msgError+="<ul>No selecciono un Formulario Evento válido.";
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
