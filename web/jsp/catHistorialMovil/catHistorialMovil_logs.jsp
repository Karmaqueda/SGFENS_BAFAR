<%-- 
    Document   : catHistorialMovil_logs
    Created on : 2/01/2015, 01:57:11 PM
    Author     : 578
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.dao.dto.DispositivoMovil"%>
<%@page import="com.tsp.sct.bo.DispositivoMovilBO"%>
<%@page import="com.tsp.sct.bo.HistorialMovilBO"%>
<%@page import="com.tsp.sct.dao.dto.HistorialMovil"%>
<%@page import="com.tsp.sct.bo.EmpleadoAgendaBO"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
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
    String buscar_fechamin = "";
    String buscar_fechamax = "";
    Date fechaMin=null;
    Date fechaMax=null;
    String strWhereRangoFechas="";   
 
    
    {
        try{
            fechaMin = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("q_fh_min"));
            buscar_fechamin = DateManage.formatDateToSQL(fechaMin);
        }catch(Exception e){}
        try{
            fechaMax = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("q_fh_max"));
            buscar_fechamax = DateManage.formatDateToSQL(fechaMax);
        }catch(Exception e){}

        /*Filtro por rango de fechas*/
        if (fechaMin!=null && fechaMax!=null){
            strWhereRangoFechas="(CAST(FECHA AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')";               
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas="(FECHA  >= '"+buscar_fechamin+"')";           
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas="(FECHA  <= '"+buscar_fechamax+"')";            
        }
    }
    
    
    if (!buscar.trim().equals(""))
        filtroBusqueda = " AND (LOCALIZACION LIKE '%" + buscar + "%' OR METODO LIKE '%" +buscar+"%' OR DESCRIPCION LIKE '%" +buscar+"%')";  
    
    int tipoLog = -1;
    try{ tipoLog = Integer.parseInt(request.getParameter("tipoLog")); }catch(NumberFormatException e){}
    
    int idDispositivoMovil = -1;
    try{ idDispositivoMovil = Integer.parseInt(request.getParameter("idDispositivoMovil")); }catch(NumberFormatException e){}
        
        
    if (tipoLog > 0){
        filtroBusqueda += " AND TIPO = " + tipoLog;
    }
    
    
    if (!strWhereRangoFechas.trim().equals("")){
        filtroBusqueda += " AND " + strWhereRangoFechas;
    }
    
    DispositivoMovil dispositivoMovil = null;
    int idEmpresa = 1;
    try{
        DispositivoMovilBO dispositivoMovilBO = new DispositivoMovilBO(user.getConn());
        dispositivoMovil = dispositivoMovilBO.findDispositivoMovilbyId(idDispositivoMovil);    
        
    }catch(Exception e){e.printStackTrace();}
    
    
    
    if(dispositivoMovil!= null){
        idEmpresa = dispositivoMovil.getIdEmpresa();
    }
    /*int idEmpresa = 1;
    try{ idEmpresa = Integer.parseInt(request.getParameter("idEmpresa")); }catch(NumberFormatException e){}*/
    
    /*
    * Paginación
    */
    int paginaActual = 1;
    double registrosPagina = 10;
    double limiteRegistros = 0;
    int paginasTotales = 0;
    int numeroPaginasAMostrar = 5;

    try{
        paginaActual = Integer.parseInt(request.getParameter("pagina"));
    }catch(Exception e){}

    try{
        registrosPagina = Integer.parseInt(request.getParameter("registros_pagina"));
    }catch(Exception e){}
    
     HistorialMovilBO historialMovilBO = new HistorialMovilBO(user.getConn());
     HistorialMovil[] historialMovilDto = new HistorialMovil[0];
     try{
         limiteRegistros = historialMovilBO.findLogs(idEmpresa, dispositivoMovil.getImei().trim() , 0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        historialMovilDto = historialMovilBO.findLogs(idEmpresa, dispositivoMovil.getImei().trim(),
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    
    String paramName = "idHistorial";
    String paramName2 = "idEmpresa"; 
    String parametrosPaginacion="idEmpresa="+idEmpresa+"&idDispositivoMovil="+idDispositivoMovil+"&imei="+dispositivoMovil.getImei().trim()+"q_fh_max="+DateManage.formatDateToNormal(fechaMax)+"&q_fh_min="+DateManage.formatDateToNormal(fechaMin)+"&tipoLog="+tipoLog;
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
            function mostrarCalendario(){
                //fh_min
                //fh_max

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
                    <h1>Log</h1>
                    
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
                            <form action="catHistorialMovil_logs.jsp" id="search_form_advance" name="search_form_advance" method="post">
                                <input type="hidden" id="idDispositivoMovil" name="idDispositivoMovil" value="<%=idDispositivoMovil %>"/>
                                <input type="hidden" id="tipoLog" name="tipoLog" value="<%=tipoLog %>"/>
                                <p>
                                    Por Fecha &raquo;&nbsp;&nbsp;
                                    <label>Desde:</label>
                                    <input maxlength="15" type="text" id="q_fh_min" name="q_fh_min" style="width:100px"
                                            value="" readonly/>
                                    &nbsp; &laquo; &mdash; &raquo; &nbsp;
                                    <label>Hasta:</label>
                                    <input maxlength="15" type="text" id="q_fh_max" name="q_fh_max" style="width:100px"
                                        value="" readonly/>
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
                                <%if(tipoLog==1){%>
                                <img src="../../images/log_gral.png" alt="icon"/>
                                    Log General Movil: <%=dispositivoMovil.getImei().trim()%>
                                <%}else{       %>
                                <img src="../../images/log_error.png" alt="icon"/>
                                    Log de Errores Movil: <%=dispositivoMovil.getImei().trim()%>
                                <%}%>
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <form id="form_data" name="form_data" action="" method="post">
                                <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th>Clase</th>
                                            <th>Método</th>
                                            <!--<th>Descripción</th>-->
                                            <th>Fecha</th>                                            
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%                                             
                                            for (HistorialMovil item : historialMovilDto){                                                                                                    
                                        %>          
                                            <td><%=item.getLocalizacion() %></td>  
                                            <td><%=item.getMetodo()%></td>
                                            <!--<td><%//=item.getDescripcion()%></td> -->
                                            <td><%=item.getFecha()!=null?item.getFecha():"" %></td> 
                                            
                                            <td>
                                                <%
                                                System.out.println(item.getDescripcion()); %>
                                            <a href="../catHistorialMovil/catHistorialMovil_logDescripcion.jsp?<%=paramName%>=<%=item.getIdHistorialMovil()%>&<%=paramName2%>=<%=idEmpresa%>" id="btn_show_cfdi" title="Ver Detalle"
                                                class="modalbox_iframe">
                                                    <img src="../../images/icon_consultar.png" alt="Mostrar Pedido" class="help" title="Ver Detalle"/><br/>
                                            </a>    
                                                
                                            </td>                                           
                                        </tr>
                                        <%    
                                             } 
                                        %>
                                    </tbody>
                                </table>
                            </form>

                            <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <!--<//jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <//jsp:param name="idReport" value="<//%= ReportBO.MARCA_REPORT %>" />
                                <//jsp:param name="parametrosCustom" value="<//%= filtroBusquedaEncoded %>" />
                            <///jsp:include>-->
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