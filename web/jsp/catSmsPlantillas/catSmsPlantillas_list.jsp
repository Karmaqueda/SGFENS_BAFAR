<%-- 
    Document   : catSmsPlantillas_list
    Created on : 10/03/2016, 12:55:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.dao.dto.SmsEnvioDetalle"%>
<%@page import="com.tsp.sct.bo.SmsEnvioDetalleBO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.SmsPlantilla"%>
<%@page import="com.tsp.sct.bo.SmsPlantillaBO"%>
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
    String q_fh_min = request.getParameter("q_fh_min")!=null? new String(request.getParameter("q_fh_min").getBytes("ISO-8859-1"),"UTF-8") :"";
    String q_fh_max = request.getParameter("q_fh_max")!=null? new String(request.getParameter("q_fh_max").getBytes("ISO-8859-1"),"UTF-8") :"";
    String q_fh_envio = request.getParameter("q_fh_envio")!=null? new String(request.getParameter("q_fh_envio").getBytes("ISO-8859-1"),"UTF-8") :"";
    String q_asunto = request.getParameter("q_asunto")!=null? new String(request.getParameter("q_asunto").getBytes("ISO-8859-1"),"UTF-8") :"";
    
    String filtroBusqueda = "";
    if (!buscar.trim().equals(""))
        filtroBusqueda += " AND (alias_plantilla LIKE '%" + buscar + "%' OR asunto LIKE '%" +buscar+"%')";
    
    if (buscar_isMostrarSoloActivos.trim().equals("1")){
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
            // TODO :  Hacer busqueda por ultima fecha de envio con subquery
            //filtroBusqueda += " AND DATE('fecha_envio')='" + buscar_fechaEnvio + "'";
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
            strWhereRangoFechas="(CAST(fecha_hr_creado AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')";
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas="(fecha_hr_creado  >= '"+buscar_fechamin+"')";
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas="(fecha_hr_creado  <= '"+buscar_fechamax+"')";
        }
        
        if (!strWhereRangoFechas.trim().equals("")){
            filtroBusqueda += " AND " + strWhereRangoFechas;
        }
    }
    
    int idSmsPlantilla = -1;
    try{ idSmsPlantilla = Integer.parseInt(request.getParameter("idSmsPlantilla")); }catch(NumberFormatException e){}
    
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
    
     SmsPlantillaBO smsPlantillaBO = new SmsPlantillaBO(user.getConn());
     SmsPlantilla[] smsPlantillasDto = new SmsPlantilla[0];
     try{
         limiteRegistros = smsPlantillaBO.findCantidadSmsPlantillas(idSmsPlantilla, idEmpresa , 0, 0, filtroBusqueda);
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        smsPlantillasDto = smsPlantillaBO.findSmsPlantillas(idSmsPlantilla, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catSmsPlantillas/catSmsPlantillas_form.jsp";
    String paramName = "idSmsPlantilla";
    String parametrosPaginacion="q="+buscar+"&inactivos="+buscar_isMostrarSoloActivos+"&q_fh_max="+q_fh_max+"&q_fh_min="+q_fh_min+"&q_fh_envio="+q_fh_envio+"&q_asunto="+q_asunto;// "idEmpresa="+idEmpresa;
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
            
        </script>

    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>SMS Masivos</h1>
                    
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
                            <form action="catSmsPlantillas_list.jsp" id="search_form_advance" name="search_form_advance" method="post">  
                                
                                <p>
                                    Por Fecha de Creación (Rango) &raquo;&nbsp;&nbsp;
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
                                    <label>Fecha de último envío:</label>
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
                                <img src="../../images/icon_smsPlantilla.png" alt="icon"/>
                                Catálogo de Plantillas SMS
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td>
                                                <div id="search">
                                                <form action="catSmsPlantillas_list.jsp" id="search_form" name="search_form" method="get">
                                                    <input type="text" id="q" name="q" title="Buscar por Asunto" class="" style="width: 300px; float: left; "
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
                                            <th>Fecha Creación</th>                                            
                                            <th>Último envío</th>
                                            <th># Veces enviado</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            SmsEnvioDetalleBO smsEnvioDetalleBO = new SmsEnvioDetalleBO(user.getConn());
                                            for (SmsPlantilla item : smsPlantillasDto){
                                                try{
                                                    int enviadosPlantilla = smsEnvioDetalleBO.findCantidadSmsEnvioDetalles(0, idEmpresa, 0, 0, " AND enviado = 1 AND id_sms_envio_lote IN (SELECT DISTINCT id_sms_envio_lote FROM sms_envio_lote WHERE id_sms_plantilla = " + item.getIdSmsPlantilla() + ")");
                                                    SmsEnvioDetalle[] ultimoEnviadoPlantilla = smsEnvioDetalleBO.findSmsEnvioDetalles(0, idEmpresa, 0, 1, " AND enviado = 1 AND id_sms_envio_lote IN (SELECT DISTINCT id_sms_envio_lote FROM sms_envio_lote WHERE id_sms_plantilla = " + item.getIdSmsPlantilla() + ")");
                                                    Date fechaHrUltimoEnvioPlantilla = null;
                                                    if (ultimoEnviadoPlantilla.length>0)
                                                        fechaHrUltimoEnvioPlantilla = ultimoEnviadoPlantilla[0].getFechaHrEnvio();
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdSmsPlantilla() %></td>
                                            <td><%=item.getAsunto()%></td>
                                            <td><%= DateManage.formatDateTimeToNormalMinutes(item.getFechaHrCreado()) %></td>                                            
                                            <td><%= fechaHrUltimoEnvioPlantilla!=null ? DateManage.formatDateTimeToNormalMinutes(fechaHrUltimoEnvioPlantilla) : "-" %> </td>
                                            <td><%= enviadosPlantilla %></td>
                                            <td>
                                                <%if(RolAutorizacionBO.permisoAccionesElemento(user.getUser().getIdRoles())){%>
                                                <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdSmsPlantilla()%>&acc=1&pagina=<%=paginaActual%>"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>
                                                &nbsp;&nbsp;
                                                <%}%>
                                                <!--<a href=""><img src="images/icon_delete.png" alt="delete" class="help" title="Delete"/></a>-->
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
                                <jsp:param name="idReport" value="<%= ReportBO.SMS_PLANTILLAS_REPORT %>" />
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
            mostrarCalendario();
            $("select.flexselect").flexselect();
        </script>
    </body>
</html>
<%}%>