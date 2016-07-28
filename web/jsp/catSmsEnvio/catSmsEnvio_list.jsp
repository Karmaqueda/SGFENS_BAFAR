<%-- 
    Document   : catSmsPlantillas_list
    Created on : 10/03/2016, 12:55:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.util.Converter"%>
<%@page import="com.tsp.sct.bo.SmsEnvioDetalleBO"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.dao.dto.SmsPlantilla"%>
<%@page import="com.tsp.sct.bo.SmsPlantillaBO"%>
<%@page import="com.tsp.sct.bo.SmsEnvioLoteBO"%>
<%@page import="com.tsp.sct.dao.dto.SmsEnvioLote"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String buscar_isMostrarInactivos = request.getParameter("inactivos")!=null?request.getParameter("inactivos"):"0";
    String q_fh_min = request.getParameter("q_fh_min")!=null? new String(request.getParameter("q_fh_min").getBytes("ISO-8859-1"),"UTF-8") :"";
    String q_fh_max = request.getParameter("q_fh_max")!=null? new String(request.getParameter("q_fh_max").getBytes("ISO-8859-1"),"UTF-8") :"";
    String q_fh_envio = request.getParameter("q_fh_envio")!=null? new String(request.getParameter("q_fh_envio").getBytes("ISO-8859-1"),"UTF-8") :"";
    String q_asunto = request.getParameter("q_asunto")!=null? new String(request.getParameter("q_asunto").getBytes("ISO-8859-1"),"UTF-8") :"";
    
    String filtroBusqueda = "";
    if (!buscar.trim().equals(""))
        filtroBusqueda += " AND (asunto LIKE '%" + buscar + "%' OR mensaje LIKE '%" +buscar+"%')";
    
    if (buscar_isMostrarInactivos.trim().equals("1")){
        filtroBusqueda += " AND (ID_ESTATUS != 1 ) ";
    }else{
        filtroBusqueda += " AND (ID_ESTATUS = 1 ) ";
    }
    
    if (!q_asunto.trim().equals("")){
        filtroBusqueda += " AND asunto LIKE '%" +q_asunto+"%'";
    }
    
    if (!q_fh_envio.trim().equals("")){
        try{
            Date fechaEnvio = new SimpleDateFormat("dd/MM/yyyy").parse(q_fh_envio);
            String buscar_fechaEnvio = DateManage.formatDateToSQL(fechaEnvio);
            filtroBusqueda += " AND DATE(fecha_hr_programa_envio)='" + buscar_fechaEnvio + "'";
        }catch(Exception e){}
    }
    
    String buscar_fechamin = "";
    String buscar_fechamax = "";
    Date fechaMin=null;
    Date fechaMax=null;
    String strWhereRangoFechas="";
    //String parametrosPaginacion = "";
    
    {
        try{
            fechaMin = new SimpleDateFormat("dd/MM/yyyy").parse(q_fh_min);
            buscar_fechamin = DateManage.formatDateToSQL(fechaMin);
        }catch(Exception e){}
        try{
            fechaMax = new SimpleDateFormat("dd/MM/yyyy").parse(q_fh_max);
            buscar_fechamax = DateManage.formatDateToSQL(fechaMax);
        }catch(Exception e){}

        /*Filtro por rango de fechas*/
        if (fechaMin!=null && fechaMax!=null){
            strWhereRangoFechas="(CAST(fecha_hr_captura AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')";
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas="(fecha_hr_captura  >= '"+buscar_fechamin+"')";
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas="(fecha_hr_captura  <= '"+buscar_fechamax+"')";
        }
        
        if (!strWhereRangoFechas.trim().equals("")){
            filtroBusqueda += " AND " + strWhereRangoFechas;
        }
    }
    
    int idSmsEnvioLote = -1;
    try{ idSmsEnvioLote = Integer.parseInt(request.getParameter("idSmsEnvioLote")); }catch(NumberFormatException e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Paginación
    */
    int paginaActual = 1;
    double registrosPagina = 20;//new ParametrosBO(user.getConn()).getParametroDouble("app.preto.paginacion.registrosPorPagina"); //10;
    double limiteRegistros = 0;
    int paginasTotales = 0;
    int numeroPaginasAMostrar = 5;

    try{
        paginaActual = Integer.parseInt(request.getParameter("pagina"));
    }catch(Exception e){}

    try{
        registrosPagina = Integer.parseInt(request.getParameter("registros_pagina"));
    }catch(Exception e){}
    
     SmsEnvioLoteBO smsEnvioLoteBO = new SmsEnvioLoteBO(user.getConn());
     SmsEnvioLote[] smsEnvioLotesDto = new SmsEnvioLote[0];
     try{
         limiteRegistros = smsEnvioLoteBO.findCantidadSmsEnvioLotes(idSmsEnvioLote, idEmpresa , 0, 0, filtroBusqueda);
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        smsEnvioLotesDto = smsEnvioLoteBO.findSmsEnvioLotes(idSmsEnvioLote, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
    Empresa empresaMatriz = new EmpresaBO(user.getConn()).getEmpresaMatriz(idEmpresa);
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catSmsEnvio/catSmsEnvio_form.jsp";
    String urlTo2 = "../catSmsEnvio/catSmsEnvioDetalle_list.jsp";
    String paramName = "idSmsEnvioLote";
    String parametrosPaginacion="q="+buscar+"&inactivos="+buscar_isMostrarInactivos+"&q_fh_max="+q_fh_max+"&q_fh_min="+q_fh_min+"&q_fh_envio="+q_fh_envio+"&q_asunto="+q_asunto;// "idEmpresa="+idEmpresa;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    
    int totalCreditosVista = 0;
    int totalCreditosDia = 0;
    try{
        String strTotalDia = smsEnvioLoteBO.findGroupValorUnicoEnvioLote(-1, idEmpresa, 0, 0, " AND DATE(fecha_hr_captura)=CURDATE() ", " SUM(cantidad_creditos_sms) ");
        if (StringManage.getValidString(strTotalDia).length()>0)
            totalCreditosDia = Integer.parseInt(strTotalDia);
    }catch(Exception ex){
        System.out.println("No se pudo obtener el total de Créditos Usados el día de hoy." + ex.toString());
    }
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
				var option = this.id === "q_fh_min" ? "minDate" : "maxDate",
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
            
            function muestraMensajeEnAlert(contenidoMensaje){
                apprise('<center><h3>Contenido del Mensaje:</h3><div style="width: 250px; word-wrap: break-word;">' + contenidoMensaje + '</div></center>',{'animate':true});
            }
            
            function setValorHtml(idControl, valor){
                $('#' + idControl).html(valor);
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
                    <div class="wrapper">
                        <div id="column-1" style="width: 35%;">
                            <h1>SMS Masivos</h1>
                        </div>
                        <div id="column-2" style="width: 33%">
                            Créditos SMS Restantes: <b><%= empresaMatriz.getCreditosSms() %></b>
                            <br/>
                            Créditos SMS Usados Hoy: <b><%= totalCreditosDia %></b>
                        </div>
                        <div id="column-3" style="width: 33%">
                            <!--Créditos SMS Usados (Filtro Vista): <b><span id="creditos_usados_filtro">0</span></b>-->
                        </div>
                    </div>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                Busqueda Avanzada &dArr;
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content" style="display: none;">
                            <form action="catSmsEnvio_list.jsp" id="search_form_advance" name="search_form_advance" method="post">  
                                
                                <p>
                                    Por Fecha de Captura (Rango) &raquo;&nbsp;&nbsp;
                                    <label>Desde:</label>
                                    <input maxlength="15" type="text" id="q_fh_min" name="q_fh_min" style="width:100px"
                                            value="<%= q_fh_min %>" readonly/>
                                    &nbsp; &laquo; &mdash; &raquo; &nbsp;
                                    <label>Hasta:</label>
                                    <input maxlength="15" type="text" id="q_fh_max" name="q_fh_max" style="width:100px"
                                        value="<%= q_fh_max %>" readonly/>
                                </p>
                                <br/>
                                
                                <p>
                                    <label>Asunto:</label>
                                    <input maxlength="50" type="text" id="q_asunto" name="q_asunto" style="width:250px"
                                            value="<%= q_asunto %>"/>
                                </p>
                                <br/>
                                
                                <p>
                                    <input type="checkbox" class="checkbox" id="inactivos" name="inactivos" value="1"  onchange="inactiv();" > <label for="inactivos">Mostrar Inactivos</label>                                   
                                </p>
                                <br/>
                                
                                <p>
                                    <label>Por Fecha Programada de Envío:</label>
                                    <input maxlength="15" type="text" id="q_fh_envio" name="q_fh_envio" style="width:100px"
                                            value="<%= q_fh_envio %>" readonly/>
                                </p>
                                <br/>
                            
                                <div id="action_buttons">
                                    <p>
                                        <input type="button" id="buscar" value="Buscar" onclick="$('#search_form_advance').submit();"/>
                                    </p>
                                </div>
                                
                            </form>
                        </div>
                    </div>
                    
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_smsAgendaEnvio.png" alt="icon"/>
                                Listado de Lotes de Env&iacute;os SMS
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td>
                                                <div id="search">
                                                <form action="catSmsEnvio_list.jsp" id="search_form" name="search_form" method="get">
                                                    <input type="text" id="q" name="q" title="Buscar por Asunto/Mensaje" class="" style="width: 300px; float: left; "
                                                           value="<%=buscar%>"/>
                                                    <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                </form>
                                                </div>
                                            </td>
                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                            <%if(RolAutorizacionBO.permisoNuevoElemento(user.getUser().getIdRoles())){%>
                                            <td>
                                                <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Crear Nuevo" 
                                                       style="float: right; width: 100px;" onclick="location.href='<%=urlTo%>'"/>
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
                                            <th>Asunto</th>
                                            <th>Fecha Captura</th>                                            
                                            <th>Envío Programado</th>
                                            <th>Créditos SMS</th>
                                            <th>Destinatarios</th>
                                            <th>Progreso</th>
                                            <th>De Sistema</th>
                                            <th>Plantilla</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            SmsPlantillaBO smsPlantillaBO = new SmsPlantillaBO(user.getConn());
                                            SmsEnvioDetalleBO smsEnvioDetalleBO = new SmsEnvioDetalleBO(user.getConn());
                                            for (SmsEnvioLote item:smsEnvioLotesDto){
                                                totalCreditosVista += item.getCantidadCreditosSms();
                                                try{
                                                    String plantilla = "";
                                                    if (item.getIdSmsPlantilla()>0){
                                                        plantilla = "(" + item.getIdSmsPlantilla() + ") ";
                                                        SmsPlantilla smsPlantilla = smsPlantillaBO.findSmsPlantillabyId(item.getIdSmsPlantilla());
                                                        plantilla += StringManage.getValidString(smsPlantilla.getAliasPlantilla());
                                                    }
                                                    String progreso = "0%";// (0/" + item.getCantidadDestinatarios()+ ")";
                                                    try {
                                                        int enviadosLote = smsEnvioDetalleBO.findCantidadSmsEnvioDetalles(0, idEmpresa, 0, 0, " AND enviado = 1 AND id_sms_envio_lote=" + item.getIdSmsEnvioLote());
                                                        double porcentajeEnvio = (enviadosLote * 100) / item.getCantidadDestinatarios();
                                                        progreso = Converter.doubleToStringFormatMexico(porcentajeEnvio) + "%";// (" + enviadosLote + "/" + item.getCantidadDestinatarios() + ")";
                                                    }catch(Exception ex){
                                                        ex.printStackTrace();
                                                    }
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%= item.getIdSmsEnvioLote() %></td>
                                            <td><%= item.getAsunto() %></td>
                                            <td><%= DateManage.formatDateTimeToNormalMinutes(item.getFechaHrCaptura()) %></td>                                            
                                            <td><%= item.getEnvioInmediato()==1?"Inmediato":DateManage.formatDateTimeToNormalMinutes(item.getFechaHrProgramaEnvio()) %></td>
                                            <td><%= item.getCantidadCreditosSms() %></td>
                                            <td><%= item.getCantidadDestinatarios() %></td>
                                            <td><%= progreso %></td>
                                            <td><%= item.getIsSmsSistema()==1?"SI":"" %></td>
                                            <td>
                                                <% if (item.getIdSmsPlantilla()>0){ %>
                                                <a href="../catSmsPlantillas/catSmsPlantillas_form.jsp?idSmsPlantilla=<%= item.getIdSmsPlantilla() %>" target="_blank">Consultar <%= plantilla %></a>
                                                <% } %>
                                            </td>
                                            <td>
                                                <%if(RolAutorizacionBO.permisoAccionesElemento(user.getUser().getIdRoles())){%>
                                                <!--
                                                <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdSmsEnvioLote()%>&acc=1&pagina=<%=paginaActual%>"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>
                                                &nbsp;&nbsp;
                                                -->
                                                <%}%>
                                                <a href="#" onclick="muestraMensajeEnAlert('<%= StringManage.getValidString(item.getMensaje()) %>')">
                                                    <img src="../../images/icon_consultar.png" alt="Ver Mensaje" class="help2" title="<%= StringManage.getValidString(item.getMensaje()) %>"/>
                                                </a>
                                                &nbsp;&nbsp;
                                                <a href="<%=urlTo2%>?<%=paramName%>=<%=item.getIdSmsEnvioLote()%>" id="accion_ver_detalles" title="Detalles de Lote" 
                                                   class="modalbox_iframe_detalle">
                                                    <img src="../../images/icon_detalle.png" alt="Ver Detalles" class="help" title="Ver Detalles"/>
                                                </a>
                                                <!--<a href=""><img src="images/icon_delete.png" alt="delete" class="help" title="Delete"/></a>-->
                                            </td>
                                        </tr>
                                        <%      }catch(Exception ex){
                                                    ex.printStackTrace();
                                                }
                                            } 
                                        %>
                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <td colspan="4" style="text-align: right;">
                                                <i><b>TOTAL:</b></i>
                                            </td>
                                            <td>
                                                <b><%= totalCreditosVista %></b>
                                            </td>
                                            <td colspan="5">
                                                
                                            </td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </form>
                                    
                            <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <!--
                            < jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                < jsp:param name="idReport" value="<%= ReportBO.SMS_PLANTILLAS_REPORT %>" />
                                < jsp:param name="parametrosCustom" value="<%= filtroBusquedaEncoded %>" />
                            < /jsp:include>
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
        </div>

        <script>
            mostrarCalendario();
            $("select.flexselect").flexselect();
            $('.help2').tipsy({gravity: 'e'});
            //setValorHtml('creditos_usados_filtro', '<%= totalCreditosVista %>');
        </script>
    </body>
</html>
<%}%>