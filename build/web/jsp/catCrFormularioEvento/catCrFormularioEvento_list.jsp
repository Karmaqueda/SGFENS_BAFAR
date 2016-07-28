<%-- 
    Document   : catCrFormularioEvento_list
    Created on : 28/06/2016, 06:17:33 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.dao.dto.CrEstadoSolicitud"%>
<%@page import="com.tsp.sct.bo.CrEstadoSolicitudBO"%>
<%@page import="com.tsp.sct.util.GenericMethods"%>
<%@page import="com.tsp.sct.dao.jdbc.CrProductoCreditoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrProductoCredito"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.dao.dto.DatosUsuario"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensProspectoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProspecto"%>
<%@page import="com.tsp.sct.dao.jdbc.CrFrmEventoSolicitudDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrFrmEventoSolicitud"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.bo.CrFormularioBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.dao.dto.CrFormularioEvento"%>
<%@page import="com.tsp.sct.bo.CrFormularioEventoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String buscar_isMostrarSoloActivos = request.getParameter("inactivos")!=null?request.getParameter("inactivos"):"0";
    
    String filtroBusqueda = "";
    
    int solicitudEstado = 0;
    String filtroDashboard = "";
    try{
        solicitudEstado = Integer.parseInt(request.getParameter("solicitud"));
    }catch(Exception e){}  
    if(solicitudEstado > 0){//filtrado desde el dashboard
        if(solicitudEstado == 2){
            if(user.getUser().getIdRoles() == RolesBO.ROL_MESA_DE_CONTROL){
                filtroDashboard = " AND id_formulario_evento IN (SELECT id_formulario_evento FROM cr_frm_evento_solicitud WHERE id_estado_solicitud in (2,5))";
            }else if(user.getUser().getIdRoles() == RolesBO.ROL_VERIFICADOR){
                filtroDashboard = " AND id_formulario_evento IN (SELECT id_formulario_evento FROM cr_frm_evento_solicitud WHERE id_estado_solicitud in (2,4))";
            }else{
                filtroDashboard = " AND id_formulario_evento IN (SELECT id_formulario_evento FROM cr_frm_evento_solicitud WHERE id_estado_solicitud in (2,4,5))";
            }
        }else if(solicitudEstado == 9){
            System.out.println("+++++++++++++++++++++ SOLICITUD: " + solicitudEstado);
            filtroDashboard = " AND id_formulario_evento IN (SELECT id_formulario_evento FROM cr_frm_evento_solicitud WHERE id_estado_solicitud in (1,9))";
        }else{
            filtroDashboard = " AND id_formulario_evento IN (SELECT id_formulario_evento FROM cr_frm_evento_solicitud WHERE id_estado_solicitud in ("+solicitudEstado+"))";
        }
    }
    
    //motrar solicitudes en base a roles
    if(user.getUser().getIdRoles() == RolesBO.ROL_MESA_DE_CONTROL){
        filtroBusqueda += " AND id_formulario_evento IN (SELECT id_formulario_evento FROM cr_frm_evento_solicitud WHERE id_estado_solicitud in (2,3,5,6,7,10,8) ) ";
    }else if(user.getUser().getIdRoles() == RolesBO.ROL_PROMOTOR){
        filtroBusqueda += " AND id_formulario_evento IN (SELECT id_formulario_evento FROM cr_frm_evento_solicitud WHERE id_estado_solicitud in (2,3,6,99) ) ";
    }else if(user.getUser().getIdRoles() == RolesBO.ROL_VERIFICADOR){
        filtroBusqueda += " AND id_formulario_evento IN (SELECT id_formulario_evento FROM cr_frm_evento_solicitud WHERE id_estado_solicitud in (2,3,4) ) ";
    }
    
    if(!filtroDashboard.trim().equals("")){
        filtroBusqueda += filtroDashboard;
    }
    
    if(user.getUser().getIdRoles() == RolesBO.ROL_PROMOTOR){
        filtroBusqueda += " AND id_usuario_capturo = " + user.getUser().getIdUsuarios();
    }
    
    if (!buscar.trim().equals(""))
        filtroBusqueda += " AND (nombre LIKE '%" + buscar + "%' OR descripcion LIKE '%" +buscar+"%')";
    
    if (buscar_isMostrarSoloActivos.trim().equals("1")){
        filtroBusqueda += " AND ID_ESTATUS != 1 ";
    }else{
        filtroBusqueda += " AND ID_ESTATUS = 1 ";
    }
    
    int idCrFormularioEvento = -1;
    try{ idCrFormularioEvento = Integer.parseInt(request.getParameter("idCrFormularioEvento")); }catch(NumberFormatException e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Paginación
    */
    int paginaActual = 1;
    double registrosPagina = new ParametrosBO(user.getConn()).getParametroDouble("app.preto.paginacion.registrosPorPagina"); //10;
    double limiteRegistros = 0;
    int paginasTotales = 0;
    int numeroPaginasAMostrar = 5;

    try{
        paginaActual = Integer.parseInt(request.getParameter("pagina"));
    }catch(Exception e){}

    try{
        registrosPagina = Integer.parseInt(request.getParameter("registros_pagina"));
    }catch(Exception e){}
    
     CrFormularioEventoBO crFormularioEventoBO = new CrFormularioEventoBO(user.getConn());
     CrFormularioEvento[] crFormularioEventoDto = new CrFormularioEvento[0];
     try{
         limiteRegistros = crFormularioEventoBO.findCantidadCrFormularioEventos(idCrFormularioEvento, idEmpresa , 0, 0, filtroBusqueda);
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        crFormularioEventoDto = crFormularioEventoBO.findCrFormularioEventos(idCrFormularioEvento, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catCrFormularioCapturaInfo/catCrFormularioCapturaInfo_form.jsp";
    String paramName = "idCrFormularioEvento";
    String paramName2 = "idGrupoFormulario";
    String paramName3 = "formularioVerificacion";
    String parametrosPaginacion="q="+buscar+"&inactivos="+buscar_isMostrarSoloActivos+"&solicitud="+solicitudEstado;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/keyWordSEO.jsp" />

        <title><jsp:include page="../include/titleApp.jsp" /></title>

        <jsp:include page="../include/skinCSS.jsp" />

        <jsp:include page="../include/jsFunctions2.jsp"/>
        
        <script type="text/javascript">
            
            function inactiv(){               
                if($("#inactivos").attr("checked")){
                    $("#inactivos").val("1");
                }else{
                     $("#inactivos").val("0");
                }
            }
            
            $(function(){ 
                $('.modalbox_iframe_detalle').fancybox({
                    padding: 0, 
                    titleShow: false, 
                    overlayColor: '#333333', 
                    overlayOpacity: .5,
                    type: 'iframe',
                    autoScale:   true,
                    width: 1000,
                    height: 600
                });
            });
            
            function registraSolicitudSAP(idFormularioEvento){
                $.ajax({
                    type: "POST",
                    url: "catCrFormularioEvento_list_ajax.jsp",
                    data: { mode: '1', idFormularioEvento : idFormularioEvento},
                    beforeSend: function(objeto){
                        //$("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#ajax_message").html(datos);
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").fadeIn("slow");
                           apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                function(r){                                         
                                    //location.reload();
                                    location.href = "../catCrFormularioEvento/catCrFormularioEvento_list.jsp?pagina="+"<%=paginaActual%>";
                                });
                       }else{
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").html(datos);
                           $("#ajax_message").fadeIn("slow");
                           //$("#action_buttons").fadeIn("slow");
                           $.scrollTo('.inner',800);
                       }
                    }
                });
            }
            
            function confirmarCancelarSolicitud(idCrFormularioEvento, mode){
                
                var letrero1 = "";
                var letrero2 = "";
                
                if(mode == "cancelarSolicitud"){
                    letrero1 = "cancelar";
                    letrero2 = "de la cancelación";
                }else if(mode == "rechazarSolicitud"){
                    letrero1 = "rechazar";
                    letrero2 = "del rechazo";
                }else if(mode == "aprobarVerificadorSolicitud"){
                    letrero1 = "aprobar";
                    letrero2 = "de la aprobación";
                }else if(mode == "rechazarVerificadorSolicitud"){
                    letrero1 = "rechazar";
                    letrero2 = "del rechazo";
                }
               
                    apprise("<center><b>¿Está seguro de querer "+letrero1+" la solicitud?:</b>"
                        + "<br/><br/><i>*Coloque un comentario "+letrero2+":</i></center>", 
                        {'input':true, 'animate':true}, 
                        function(r){
                            if(r) {
                                //$("#skuConcepto").val(r);
                                // Usuario dio click 'Yes'
                                cancelarSolicitud(idCrFormularioEvento, r, mode);
                                 
                            }
                        }
                    );   
               
            }
            
            function cancelarSolicitud(idCrFormularioEvento,respuestaComentario, mode){
                if(idCrFormularioEvento>=0){
                    $.ajax({
                        type: "POST",
                        url: "catCrFormularioEvento_ajax.jsp",
                        data: { mode: mode, idCrFormularioEvento : idCrFormularioEvento , respuestaComentario : respuestaComentario},
                        beforeSend: function(objeto){
                            //$("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style="">ESPERE, procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               location.href = "../catCrFormularioEvento/catCrFormularioEvento_list.jsp?pagina="+"<%=paginaActual%>";
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               //$("#ajax_message").html(datos);
                               //$("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                           }
                        }
                    });
                }
            }
            
            function confirmarCambiarEstatusSolicitud(idCrFormularioEvento, mode){
               
                    apprise("<center><b>¿Está seguro de querer cancelar la solicitud?:</b>"
                        + "<br/><br/><i>*Coloque un comentario de la cancelación:</i></center>", 
                        {'input':true, 'animate':true}, 
                        function(r){
                            if(r) {
                                //$("#skuConcepto").val(r);
                                // Usuario dio click 'Yes'
                                cancelarSolicitud(idCrFormularioEvento, r, mode);
                                 
                            }
                        }
                    );   
               
            }
            
            function aceptaContratoSAP(idFormularioEvento){
                $.ajax({
                    type: "POST",
                    url: "catCrFormularioEvento_list_ajax.jsp",
                    data: { mode: '2', idFormularioEvento : idFormularioEvento},
                    beforeSend: function(objeto){
                        //$("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#ajax_message").html(datos);
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").fadeIn("slow");
                           apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                function(r){                                         
                                    location.href = "../catCrFormularioEvento/catCrFormularioEvento_list.jsp?pagina="+"<%=paginaActual%>";
                                });
                       }else{
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").html(datos);
                           $("#ajax_message").fadeIn("slow");
                           //$("#action_buttons").fadeIn("slow");
                           $.scrollTo('.inner',800);
                       }
                    }
                });
            }
            
            function consultaDipersionSAP(idFormularioEvento){
                $.ajax({
                    type: "POST",
                    url: "catCrFormularioEvento_list_ajax.jsp",
                    data: { mode: '3', idFormularioEvento : idFormularioEvento},
                    beforeSend: function(objeto){
                        //$("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#ajax_message").html(datos);
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").fadeIn("slow");
                           apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                function(r){                                         
                                    location.href = "../catCrFormularioEvento/catCrFormularioEvento_list.jsp?pagina="+"<%=paginaActual%>";
                                });
                       }else{
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").html(datos);
                           $("#ajax_message").fadeIn("slow");
                           //$("#action_buttons").fadeIn("slow");
                           $.scrollTo('.inner',800);
                       }
                    }
                });
            }
            
        </script>

    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Solicitudes</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                Busqueda Avanzada &dArr;
                            </span>
                        </div>
                        <br class="clear"/>
                        
                    </div>
                    
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_crFormularioEvento.png" alt="icon"/>
                                Eventos - Solicitudes
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td>
                                                <div id="search">
                                                <form action="catCrFormularioEvento_list.jsp" id="search_form" name="search_form" method="get">
                                                    <input type="text" id="q" name="q" title="Buscar por Nombre/Descripción" class="" style="width: 300px; float: left; "
                                                           value="<%=buscar%>"/>
                                                    <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                </form>
                                                </div>
                                            </td>
                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                            <%if(RolAutorizacionBO.permisoNuevoElemento(user.getUser().getIdRoles()) || user.getUser().getIdRoles() == RolesBO.ROL_PROMOTOR){%>
                                            <td>                                                
                                                <a href="catCrFormulario_seleccionProducto.jsp" id="crearNuevo" title="Crear Nuevo" class="modalbox_iframe">
                                                    <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Crear Nuevo" 
                                                   style="float: right; width: 100px;"/>
                                                </a>
                                                
                                            </td>
                                            <%}%>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <form id="form_data" name="form_data" action="" method="post">
                                <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Fecha y Hora<%=user.getUser().getIdRoles() == RolesBO.ROL_VERIFICADOR?" de Registro a Verificación":""%></th>
                                            <%if(user.getUser().getIdRoles() == RolesBO.ROL_PROMOTOR){%>
                                                <th>Fecha y Hora de última actualización</th>
                                            <%}%>
                                            <th>Prospecto</th>                                            
                                            <%if(user.getUser().getIdRoles() == RolesBO.ROL_VERIFICADOR || user.getUser().getIdRoles() == RolesBO.ROL_MESA_DE_CONTROL || user.getUser().getIdRoles() == RolesBO.ROL_PROMOTOR){%>
                                                <th>Producto</th>
                                            <%}%>
                                            <th>Promotor</th>
                                            <th>Verificador</th>
                                            <%if(user.getUser().getIdRoles() == RolesBO.ROL_VERIFICADOR || user.getUser().getIdRoles() == RolesBO.ROL_MESA_DE_CONTROL){%>
                                                <th>Estatus de Mesa de Control</th>
                                                <th>Estatus de Verificación</th>                                                
                                            <%}else{%>
                                                <th>Estatus</th>
                                            <%}%>
                                            <th>SAP</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            CrFormularioBO crFormularioBO = new CrFormularioBO(user.getConn());
                                            CrFrmEventoSolicitud crFrmEventoSolicitud = null; //si no esta en la tabla cr_frm_evento_solicitud, es que aun se puede modificar
                                            //CrFrmEventoSolicitudDaoImpl crFrmEventoSolicitudDaoImpl = new CrFrmEventoSolicitudDaoImpl(user.getConn());
                                            SgfensProspecto sgfensProspecto = null;
                                            SgfensProspectoDaoImpl sgfensProspectoDaoImpl = new SgfensProspectoDaoImpl(user.getConn());
                                            CrProductoCredito crProductoCredito = null;
                                            CrProductoCreditoDaoImpl crProductoCreditoDaoImpl = new CrProductoCreditoDaoImpl(user.getConn());
                                            CrEstadoSolicitud crEstadoSolicitud = null;
                                            CrEstadoSolicitudBO crEstadoSolicitudBO = new CrEstadoSolicitudBO(user.getConn());
                                            for (CrFormularioEvento item : crFormularioEventoDto){
                                                String promotor = "";
                                                String verificadorN = "";
                                                //verificamos si el evento se encuentra en la tabla "cr_frm_evento_solicitud", esto para ver si se puede o no editar
                                                crFormularioEventoBO = new CrFormularioEventoBO(user.getConn());
                                                crFormularioEventoBO.setCrFormularioEvento(item);
                                                crFrmEventoSolicitud = null;
                                                
                                                try{
                                                    //crFrmEventoSolicitud = crFrmEventoSolicitudDaoImpl.findByDynamicWhere("id_formulario_evento = " + item.getIdFormularioEvento(), null)[0];
                                                    crFrmEventoSolicitud = crFormularioEventoBO.getFrmEventoSolicitud();
                                                }catch(Exception e){}
                                                sgfensProspecto = null;
                                                try{
                                                    if(item.getTipoEntidadRespondio().equals("PROSPECTO")){
                                                        sgfensProspecto = sgfensProspectoDaoImpl.findByPrimaryKey(item.getIdEntidadRespondio());
                                                    }
                                                }catch(Exception e){}
                                                try{
                                                    DatosUsuario datosUsuarioPromotor = new UsuarioBO(item.getIdUsuarioCapturo()).getDatosUsuario();
                                                    if(datosUsuarioPromotor != null){
                                                        promotor = datosUsuarioPromotor.getNombre() +" " + datosUsuarioPromotor.getApellidoPat();
                                                    }
                                                }catch(Exception e){}
                                                crEstadoSolicitud = null;
                                                try{
                                                    if(crFrmEventoSolicitud!=null){
                                                        crEstadoSolicitud = crEstadoSolicitudBO.findCrEstadoSolicitudbyId(crFrmEventoSolicitud.getIdEstadoSolicitud());
                                                        
                                                        DatosUsuario datosUsuarioVerificador = new UsuarioBO(crFrmEventoSolicitud.getIdUsuarioVerificador()).getDatosUsuario();
                                                        if(datosUsuarioVerificador != null){
                                                            verificadorN = datosUsuarioVerificador.getNombre() +" " + datosUsuarioVerificador.getApellidoPat();
                                                        }
                                                    }
                                                }catch(Exception e){}
                                                
                                                try{
                                                    
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%= item.getIdFormularioEvento()%></td>                                            
                                            <td><%= DateManage.formatDateTimeToNormalMinutes(item.getFechaHrCreacion()) %></td>
                                            <%if(user.getUser().getIdRoles() == RolesBO.ROL_PROMOTOR){%>
                                                <td><%= item.getFechaHrEdicion()!=null?DateManage.formatDateTimeToNormalMinutes(item.getFechaHrEdicion()):"" %></td>
                                            <%}%>
                                            <td><%= sgfensProspecto!=null?sgfensProspecto.getRazonSocial():"" %></td>                                            
                                            <%if(user.getUser().getIdRoles() == RolesBO.ROL_VERIFICADOR || user.getUser().getIdRoles() == RolesBO.ROL_MESA_DE_CONTROL || user.getUser().getIdRoles() == RolesBO.ROL_PROMOTOR){
                                                crProductoCredito = null;
                                                if(crFrmEventoSolicitud!=null){
                                                    crProductoCredito = crProductoCreditoDaoImpl.findByPrimaryKey(crFrmEventoSolicitud.getIdProductoCredito());
                                                }
                                            %>
                                                <td><%= crProductoCredito!=null?crProductoCredito.getNombre():"" %></td>
                                            <%}%>                                            
                                            <td><%= promotor %></td>
                                            <td><%= verificadorN %></td>
                                            
                                             <%if(user.getUser().getIdRoles() == RolesBO.ROL_VERIFICADOR || user.getUser().getIdRoles() == RolesBO.ROL_MESA_DE_CONTROL){%>
                                                <!-- Estatus Mesa de Control -->
                                                <td><%= (crFrmEventoSolicitud!=null?(crFrmEventoSolicitud.getIdEstadoSolicitud()==3?"En Revisión":crFrmEventoSolicitud.getIdEstadoSolicitud()==4?"Aprobada":crFrmEventoSolicitud.getIdEstadoSolicitud()==5?"Por Revisar":crFrmEventoSolicitud.getIdEstadoSolicitud()==6?"Aprobada":(crEstadoSolicitud!=null?"<b>"+crEstadoSolicitud.getNombre()+"</b>":"")):"") %></td>
                                                <!-- Verificador -->
                                                <td><%= (crFrmEventoSolicitud!=null?(crFrmEventoSolicitud.getIdEstadoSolicitud()==3?"En Revisión":crFrmEventoSolicitud.getIdEstadoSolicitud()==4?"Por Revisar":crFrmEventoSolicitud.getIdEstadoSolicitud()==5?"Aprobada":crFrmEventoSolicitud.getIdEstadoSolicitud()==6?"Aprobada":(crEstadoSolicitud!=null?"<b>"+crEstadoSolicitud.getNombre()+"</b>":"")):"") %></td>
                                            <%}else{%>
                                                <!-- Estatus general -->
                                                <td><%= (crEstadoSolicitud!=null?crEstadoSolicitud.getNombre():"") %></td>
                                            <%}%>
                                            
                                            <!-- SAP -->
                                            <td>
                                                <% if (crFrmEventoSolicitud!=null && StringManage.getValidString(crFrmEventoSolicitud.getSapNoContrato()).length()>0 ) { %>
                                                        <ul>BP: <%= StringManage.getValidString(crFrmEventoSolicitud.getSapBp()) %></ul>
                                                        <ul>No. Contrato: <%= StringManage.getValidString(crFrmEventoSolicitud.getSapNoContrato()) %></ul>    
                                                <%  
                                                }
                                                %>
                                            </td>
                                            <td>
                                                
                                            <!--PROMOTOR-->
                                                <% if ( GenericMethods.datoEnColeccion(user.getUser().getIdRoles(), new Integer[]{RolesBO.ROL_PROMOTOR,RolesBO.ROL_ADMINISTRADOR} ) ) {%>
                                                
                                                    <% if( (crFrmEventoSolicitud==null || (crFrmEventoSolicitud!=null && crFrmEventoSolicitud.getIdEstadoSolicitud() == CrEstadoSolicitudBO.S_BORRADOR ) )){%>
                                                    <!-- Editar (estatus Borrador) -->
                                                        <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdFormularioEvento()%>&<%=paramName2%>=<%=item.getIdGrupoFormulario()%>&acc=1&pagina=<%=paginaActual%>"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>
                                                        &nbsp;&nbsp;
                                                    <%}%>

                                                    <%if( crFrmEventoSolicitud!=null && crFrmEventoSolicitud.getIdEstadoSolicitud() == CrEstadoSolicitudBO.S_EN_REVISION ) {%>
                                                    <!-- En Revisión - rol Promotor -->
                                                        <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdFormularioEvento()%>&<%=paramName2%>=<%=item.getIdGrupoFormulario()%>&soloCamposRevision=1&acc=1&pagina=<%=paginaActual%>"><img src="../../images/icon_enRevision.png" alt="enRevision" class="help" title="En Revisión"/></a>
                                                        &nbsp;&nbsp;
                                                    <%}%>

                                                    <%if( crFrmEventoSolicitud!=null  && crFrmEventoSolicitud.getIdEstadoSolicitud() == CrEstadoSolicitudBO.S_POR_REVISAR ) {%>
                                                    <!-- Visualizar - rol Promotor -->
                                                        <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdFormularioEvento()%>&<%=paramName2%>=<%=item.getIdGrupoFormulario()%>&soloConsulta=1&acc=1&pagina=<%=paginaActual%>"><img src="../../images/icon_solicitudVer.png" alt="visualizar" class="help" title="Visualizar"/></a>
                                                        &nbsp;&nbsp;
                                                    <%}%>
                                                
                                                <%}%>
                                            <!-- FIN PROMOTOR-->
                                                
                                            <!-- MESA DE CONTROL -->
                                                <!-- Ver Solicitud para marcar Verificaciones o Aceptar -->
                                                <%if(user.getUser().getIdRoles() == RolesBO.ROL_MESA_DE_CONTROL){%>
                                                    <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdFormularioEvento()%>&<%=paramName2%>=<%=item.getIdGrupoFormulario()%>&<%=paramName3%>=1&acc=1&pagina=<%=paginaActual%>"><img src="../../images/icon_verificacion.png" alt="verSolicitud" class="help" title="Ver Solicitud"/></a>
                                                        &nbsp;&nbsp;
                                                    <% if ( GenericMethods.datoEnColeccion(crFrmEventoSolicitud.getIdEstadoSolicitud(), new Integer[]{CrEstadoSolicitudBO.S_APROBADA, CrEstadoSolicitudBO.S_IMPRESION_LIB} ) ) { %>
                                                    <!-- Cancelar Mesa de Control -->
                                                    <a href="#" onclick="confirmarCancelarSolicitud(<%=item.getIdFormularioEvento()%>, '<%="cancelarSolicitud"%>');"><img src="../../images/icon_delete.png" alt="cancelar" class="help" title="Cancelar" /></a>
                                                    &nbsp;&nbsp;
                                                    <% } %>
                                                    <% if ( GenericMethods.datoEnColeccion(crFrmEventoSolicitud.getIdEstadoSolicitud(), new Integer[]{CrEstadoSolicitudBO.S_POR_REVISAR, CrEstadoSolicitudBO.S_EN_REVISION, CrEstadoSolicitudBO.S_APROBADA_MESAC, CrEstadoSolicitudBO.S_APROBADA_VERIF} ) ) { %>
                                                    <!-- Rechazar Mesa de Control -->
                                                    <a href="#" onclick="confirmarCancelarSolicitud(<%=item.getIdFormularioEvento()%>, '<%="rechazarSolicitud"%>');"><img src="../../images/icon_rechazarSolicitud.png" alt="rechazar" class="help" title="Rechazar" /></a>
                                                    &nbsp;&nbsp;        
                                                    <% } %>
                                                <%}%>
                                            <!-- FIN MESA DE CONTROL -->
                                                
                                            <!-- GERENTE -->
                                                <%if(user.getUser().getIdRoles() == RolesBO.ROL_GERENTE){%>
                                                    <% 
                                                    // Permitimos cambiar estatus mientras NO este en estatus: Impresion liberada, Por Dispersar o Dispersado 
                                                    if ( !GenericMethods.datoEnColeccion(crFrmEventoSolicitud.getIdEstadoSolicitud(), new Integer[]{ CrEstadoSolicitudBO.S_IMPRESION_LIB, CrEstadoSolicitudBO.S_POR_DISPERSAR, CrEstadoSolicitudBO.S_DISPERSADA} ) ) { %>
                                                    <!-- Cambiar Estatus -->
                                                    <a href="catCrFormularioEvento_cambioEstatus.jsp?<%=paramName%>=<%=item.getIdFormularioEvento()%>&pagina=<%=paginaActual%>" title="Estatus"
                                                        class="modalbox_iframe">
                                                        <img src="../../images/icon_estatusSolicitud.png" alt="Estatus" class="help" title="Estatus"/>
                                                    </a>
                                                    <%}%>
                                                <%}%>
                                            <!-- FIN GERENTE -->
                                                
                                            <!-- COMPARTIDAS POR DIFERENTES ROLES -->
                                                <!-- Todos los Roles -->
                                                <%if( crFrmEventoSolicitud!=null  
                                                    && GenericMethods.datoEnColeccion(crFrmEventoSolicitud.getIdEstadoSolicitud(), new Integer[]{CrEstadoSolicitudBO.S_IMPRESION_LIB, CrEstadoSolicitudBO.S_POR_DISPERSAR, CrEstadoSolicitudBO.S_DISPERSADA} )  ){%>
                                                <!-- Imprimibles -->
                                                    <a href="../../jsp/catCrDocImprimible/catCrDocImprimible_byFrmEvento_list.jsp?<%=paramName%>=<%=item.getIdFormularioEvento()%>&pagina=<%=paginaActual%>" title="Imprimibles"
                                                        class="modalbox_iframe_detalle">
                                                        <img src="../../images/icon_crDocImprimible.png" alt="Imprimibles" class="help" title="Imprimibles"/>
                                                    </a>
                                                    &nbsp;&nbsp;
                                                <%}%>
                                                
                                                <!-- Gerente y Mesa de Control -->
                                                <% if ( GenericMethods.datoEnColeccion(user.getUser().getIdRoles(), new Integer[]{RolesBO.ROL_GERENTE,RolesBO.ROL_MESA_DE_CONTROL} ) 
                                                        && crFrmEventoSolicitud!=null){ %>
                                                        
                                                    <% if( crFrmEventoSolicitud.getIdEstadoSolicitud()== CrEstadoSolicitudBO.S_APROBADA ){ //Estatus 6 - Aprobada (por Mesa de control y Verificador) %>
                                                    <!-- Registrar en SAP -->
                                                        <a href="#" onclick="registraSolicitudSAP(<%= item.getIdFormularioEvento() %>);"><img src="../../images/icon_crConectarSAP.png" alt="Registrar en SAP" class="help" title="Registrar en SAP"/></a> 
                                                    &nbsp;&nbsp;
                                                    <%}%>
                                                        
                                                    <% if( crFrmEventoSolicitud.getIdEstadoSolicitud()== CrEstadoSolicitudBO.S_IMPRESION_LIB ){ //Estatus 7 - Impresion Liberada%>
                                                    <!-- Aceptar Contrato en SAP -->
                                                        <a href="#" onclick="aceptaContratoSAP(<%= item.getIdFormularioEvento() %>);"><img src="../../images/icon_crAceptaContrato2.png" alt="Aceptar Contrato SAP" class="help" title="Aceptar Contrato - SAP"/></a> 
                                                        &nbsp;&nbsp;
                                                    <%}%>
                                                    
                                                    <% if( crFrmEventoSolicitud.getIdEstadoSolicitud()== CrEstadoSolicitudBO.S_POR_DISPERSAR ){ //Estatus 10 - Por Dispersar %>
                                                    <!-- Consultar Dispersion individual en SAP-->
                                                    <a href="#" onclick="consultaDipersionSAP(<%= item.getIdFormularioEvento() %>);"><img src="../../images/icon_crConsultaDispersion.png" alt="Consulta Dispersion SAP" class="help" title="Consultar Dispersion - SAP"/></a> 
                                                        &nbsp;&nbsp;
                                                    <%}%>
                                                    
                                                <% } %>
                                                
                                            <!-- FIN COMPARTIDAS POR DIFERENTES ROLES -->
                                            
                                            <!-- VERIFICADOR -->
                                            <%if(user.getUser().getIdRoles() == RolesBO.ROL_VERIFICADOR){%>
                                                    <a href="#" onclick="confirmarCancelarSolicitud(<%=item.getIdFormularioEvento()%>, '<%="aprobarVerificadorSolicitud"%>');"><img src="../../images/icon_aprobarSolicitud.png" alt="aprobarVerificador" class="help" title="Aprobar" /></a>
                                                        &nbsp;&nbsp;
                                                    <a href="#" onclick="confirmarCancelarSolicitud(<%=item.getIdFormularioEvento()%>, '<%="rechazarVerificadorSolicitud"%>');"><img src="../../images/icon_rechazarSolicitud.png" alt="rechazarVerificador" class="help" title="Rechazar" /></a>
                                                        &nbsp;&nbsp;
                                                <%}%>
                                                
                                            </td>
                                        </tr>
                                        <%      }catch(Exception ex){
                                                    ex.printStackTrace();
                                                }
                                            } 
                                        %>
                                    </tbody>
                                </table>
                            </form>
                                    
                            <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <jsp:param name="idReport" value="<%= ReportBO.CR_GRUPOS_FORMULARIO_REPORT %>" />
                                <jsp:param name="parametrosCustom" value="<%= filtroBusquedaEncoded %>" />
                            </jsp:include>
                            <!-- FIN INCLUDE OPCIONES DE EXPORTACIÓN-->

                            <jsp:include page="../include/listPagination.jsp">
                                <jsp:param name="paginaActual" value="<%=paginaActual%>" />
                                <jsp:param name="numeroPaginasAMostrar" value="<%=numeroPaginasAMostrar%>" />
                                <jsp:param name="paginasTotales" value="<%=paginasTotales%>" />
                                <jsp:param name="url" value="<%=request.getRequestURI() %>" />
                                <jsp:param name="parametrosAdicionales" value="<%=parametrosPaginacion%>" />
                            </jsp:include>
                            
                        </div>
                    </div>

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>

        <script>
            $("select.flexselect").flexselect();
        </script>
    </body>
</html>
<%}%>
