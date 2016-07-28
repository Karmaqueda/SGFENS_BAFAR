<%-- 
    Document   : catSmsEnvioDetalles_list
    Created on : 10/03/2016, 12:55:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.bo.SmsEnvioLoteBO"%>
<%@page import="com.tsp.sct.dao.dto.SmsEnvioLote"%>
<%@page import="com.tsp.sct.bo.SmsAgendaDestinatarioBO"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.bo.SGProspectoBO"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.SmsEnvioDetalle"%>
<%@page import="com.tsp.sct.bo.SmsEnvioDetalleBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String filtroBusqueda = "";
    
    int idSmsEnvioDetalle = -1;
    try{ idSmsEnvioDetalle = Integer.parseInt(request.getParameter("idSmsEnvioDetalle")); }catch(NumberFormatException e){}
    
    int idSmsEnvioLote = -1;
    try{ idSmsEnvioLote = Integer.parseInt(request.getParameter("idSmsEnvioLote")); }catch(NumberFormatException e){}
    
    if (idSmsEnvioLote>0){
        filtroBusqueda += " AND id_sms_envio_lote=" + idSmsEnvioLote;
    }
    
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
    
    SmsEnvioLote smsEnvioLoteDto = null;
    if (idSmsEnvioLote>0){
        smsEnvioLoteDto = new SmsEnvioLoteBO(idSmsEnvioLote, user.getConn()).getSmsEnvioLote();
    }
     SmsEnvioDetalleBO smsEnvioDetalleBO = new SmsEnvioDetalleBO(user.getConn());
     SmsEnvioDetalle[] smsEnvioDetallesDto = new SmsEnvioDetalle[0];
     try{
         limiteRegistros = smsEnvioDetalleBO.findCantidadSmsEnvioDetalles(idSmsEnvioDetalle, idEmpresa, 0, 0, filtroBusqueda);
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        smsEnvioDetallesDto = smsEnvioDetalleBO.findSmsEnvioDetalles(idSmsEnvioDetalle, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    //String urlTo = "../catSmsEnvio/catSmsEnvioDetalles_form.jsp";
    String paramName = "idSmsEnvioDetalle";
    String parametrosPaginacion="q="+buscar;
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

        <jsp:include page="../include/jsFunctions.jsp"/>
        
        <script type="text/javascript">
            
            function inactiv(){               
                if($("#inactivos").attr("checked")){
                    $("#inactivos").val("1");
                }else{
                     $("#inactivos").val("0");
                }
            }
            
            /*
            function mostrarCalendario(){
                //rangos
                var dates = $('#q_fh_min, #q_fh_max').datepicker({
                        //minDate: 0,
			changeMonth: true,
			//numberOfMonths: 2,
                        //beforeShow: function() {$('#fh_min').css("z-index", 9999); },
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 998);
                            }, 500)},
			onSelect: function( selectedDate ) {
				var option = this.id == "q_fh_min" ? "minDate" : "maxDate",
					instance = $( this ).data( "datepicker" ),
					date = $.datepicker.parseDate(
						instance.settings.dateFormat ||
						$.datepicker._defaults.dateFormat,
						selectedDate, instance.settings );
				dates.not( this ).datepicker( "option", option, date );
			}
		});
                
                //un solo campo
                $( "#q_fh_envio" ).datepicker({
                        //minDate: 0,
                        gotoCurrent: true,
                        changeMonth: true,
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 100);
                                }, 500);
                        }
                });

            }
            */
           
           function inicializarPopup(){
                $('#left_menu').hide();
                $('body').addClass('nobg');
		$('#content').css('marginLeft', 30);
            }
            
        </script>

    </head>
    <body class="nobg">
        <!--<div class="content_wrapper">-->

            <div id="left_menu">
            </div>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <!--<h1>SMS Masivos</h1>-->
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>
                    
                    <% if (smsEnvioLoteDto!=null && StringManage.getValidString(smsEnvioLoteDto.getMensaje()).length()>0 ){ %>
                    <p>
                        <label>Mensaje:</label><br/>
                        <div id="msg" class="message from"><%= StringManage.getValidString(smsEnvioLoteDto.getMensaje()) %></div>
                    </p>
                    <% } %>
                                
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_detalle.png" alt="icon"/>
                                Detalles de Lote <%= idSmsEnvioLote>0?""+idSmsEnvioLote:"" %>
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td>
                                                <!--
                                                <div id="search">
                                                <form action="catSmsEnvioDetalle_list.jsp" id="search_form" name="search_form" method="get">
                                                    <input type="text" id="q" name="q" title="Buscar por Asunto" class="" style="width: 300px; float: left; "
                                                           value="<%=buscar%>"/>
                                                    <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                </form>
                                                </div>
                                                -->
                                            </td>
                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                            <%if(RolAutorizacionBO.permisoNuevoElemento(user.getUser().getIdRoles())){%>
                                            <td>
                                                
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
                                            <th>Destinatario</th>
                                            <th>Asunto</th>
                                            <th>Partes</th>
                                            <th>Fecha Creación</th>
                                            <th>Enviado</th>
                                            <th>Intentos Fallidos</th>
                                            <!--<th>Acciones</th>-->
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            ClienteBO clienteBO = new ClienteBO(user.getConn());
                                            SGProspectoBO prospectoBO = new SGProspectoBO(user.getConn());
                                            EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
                                            EmpresaBO empresaBO = new EmpresaBO(user.getConn());
                                            SmsAgendaDestinatarioBO smsAgendaDestinatarioBO = new SmsAgendaDestinatarioBO(user.getConn());
                                            for (SmsEnvioDetalle item : smsEnvioDetallesDto){
                                                try{
                                                    String destinatario = "<b>" + item.getNumeroCelular() + "</b>";
                                                    
                                                    try{
                                                        if (item.getDestIdCliente()>0){
                                                            destinatario += "<br/><i>Cliente: " + StringManage.getValidString(clienteBO.findClientebyId(item.getDestIdCliente()).getRazonSocial()) + "</i>";
                                                        }
                                                        if (item.getDestIdProspecto()>0){
                                                            destinatario += "<br/><i>Prospecto: " + StringManage.getValidString(prospectoBO.findProspectobyId(item.getDestIdProspecto()).getRazonSocial()) + "</i>";
                                                        }
                                                        if (item.getDestIdEmpleado()>0){
                                                            destinatario += "<br/><i>Empleado: " + StringManage.getValidString(empleadoBO.findEmpleadobyId(item.getDestIdEmpleado()).getNombre()) + "</i>";
                                                        }
                                                        if (item.getDestIdEmpresa()>0){
                                                            destinatario += "<br/><i>Empresa: " + StringManage.getValidString(empresaBO.findEmpresabyId(item.getDestIdEmpresa()).getRazonSocial()) + "</i>";
                                                        }
                                                        if (item.getDestIdSmsAgendaDest()>0){
                                                            destinatario += "<br/><i>Contacto SMS: " + StringManage.getValidString(smsAgendaDestinatarioBO.findSmsAgendaDestinatariobyId(item.getDestIdSmsAgendaDest()).getNombre()) + "</i>";
                                                        }
                                                    }catch(Exception ex){
                                                        destinatario += " (error al recuperar info de destinatario relacionado)";
                                                        ex.printStackTrace();
                                                    }
                                        %>
                                        <tr>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdSmsEnvioDetalle() %></td>
                                            <td><%= destinatario %></td>
                                            <td><%= StringManage.getValidString(item.getAsunto()) %></td>
                                            <td><%= item.getNumPartesSms() %></td>
                                            <td><%= DateManage.formatDateTimeToNormalMinutes(item.getFechaHrCreacion()) %></td>
                                            <td>
                                                <% if (item.getEnviado()==1){ %>
                                                <img src="../../images/icon_accept.png" /> &nbsp;
                                                <%= DateManage.formatDateTimeToNormalMinutes(item.getFechaHrEnvio()) %>
                                                <% }else{ %>
                                                
                                                <% } %>
                                            </td>
                                            <td>
                                                <% if (item.getIntentosFallidos()>0){ %>
                                                <img src="../../images/icon_warning.png" />&nbsp;<b><%= item.getIntentosFallidos() %></b>
                                                <% }else{ out.print("0"); }%>
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
                            <!--
                            < jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                < jsp:param name="idReport" value="<%= ReportBO.SMS_PLANTILLAS_REPORT %>" />
                                < jsp:param name="parametrosCustom" value="<%= filtroBusquedaEncoded %>" />
                            </ jsp:include>
                            -->
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
        <!--</div>-->

        <script>
            inicializarPopup();
            //mostrarCalendario();
            //$("select.flexselect").flexselect();
        </script>
    </body>
</html>
<%}%>