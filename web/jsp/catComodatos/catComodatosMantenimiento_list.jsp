<%-- 
    Document   : catComodatoMantenimientosMantenimiento_list
    Created on : 4/03/2016, 05:11:43 PM
    Author     : leonardo
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.bo.ComodatoBO"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.dao.jdbc.ComodatoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Comodato"%>
<%@page import="com.tsp.sct.dao.jdbc.ClienteDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.dao.jdbc.AlmacenDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Almacen"%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.ComodatoMantenimiento"%>
<%@page import="com.tsp.sct.bo.ComodatoMantenimientoBO"%>
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
    if (!buscar.trim().equals(""))
        filtroBusqueda = " AND (NOMBRE_ATENDIO LIKE '%" + buscar + "%' OR DESCRIPCION LIKE '%" +buscar+"%' OR TECNICO LIKE '%" +buscar+"%')";
    
    int idComodatoMantenimiento = -1;
    try{ idComodatoMantenimiento = Integer.parseInt(request.getParameter("idComodatoMantenimiento")); }catch(NumberFormatException e){}
    
    int idComodato = -1;
    try{ idComodato = Integer.parseInt(request.getParameter("idComodato")); }catch(NumberFormatException e){}
    
    if(idComodato > 0){
        filtroBusqueda += " AND ID_COMODATO = " + idComodato; 
    }
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    String buscar_fechamin = "";
    String buscar_fechamax = "";
    Date fechaMin=null;
    Date fechaMax=null;
    String strWhereRangoFechas="";    
    String parametrosPaginacion = "";
    
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
            strWhereRangoFechas="(CAST(FECHA_REALIZACION_MANTENIMIENTO AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax)+"&q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas="(FECHA_REALIZACION_MANTENIMIENTO  >= '"+buscar_fechamin+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas="(FECHA_REALIZACION_MANTENIMIENTO  <= '"+buscar_fechamax+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax);
        }
    }
    
    String buscar_fechamin2 = "";
    String buscar_fechamax2 = "";
    Date fechaMin2=null;
    Date fechaMax2=null;
    String strWhereRangoFechas2="";
    
    {
        try{
            fechaMin2 = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("q_fh_min2"));
            buscar_fechamin2 = DateManage.formatDateToSQL(fechaMin2);
        }catch(Exception e){}
        try{
            fechaMax2 = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("q_fh_max2"));
            buscar_fechamax2 = DateManage.formatDateToSQL(fechaMax2);
        }catch(Exception e){}

        /*Filtro por rango de fechas*/
        if (fechaMin2!=null && fechaMax2!=null){
            strWhereRangoFechas2="(CAST(FECHA_PROX_MANTENIMIENTO AS DATE) BETWEEN '"+buscar_fechamin2+"' AND '"+buscar_fechamax2+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max2="+DateManage.formatDateToNormal(fechaMax2)+"&q_fh_min2="+DateManage.formatDateToNormal(fechaMin2);
        }
        if (fechaMin2!=null && fechaMax2==null){
            strWhereRangoFechas2="(FECHA_PROX_MANTENIMIENTO  >= '"+buscar_fechamin2+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_min2="+DateManage.formatDateToNormal(fechaMin2);
        }
        if (fechaMin2==null && fechaMax2!=null){
            strWhereRangoFechas2="(FECHA_PROX_MANTENIMIENTO  <= '"+buscar_fechamax2+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max2="+DateManage.formatDateToNormal(fechaMax2);
        }
    }
    
    if (!strWhereRangoFechas.trim().equals("")){
        filtroBusqueda += " AND " + strWhereRangoFechas;
    }
    
    if (!strWhereRangoFechas2.trim().equals("")){
        filtroBusqueda += " AND " + strWhereRangoFechas2;
    }
    
    String buscar_idcliente = request.getParameter("q_idcliente")!=null?request.getParameter("q_idcliente"):"";
    
    if (!buscar_idcliente.trim().equals("")){
        filtroBusqueda += " AND ID_CLIENTE='" + buscar_idcliente +"' ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q_idcliente="+buscar_idcliente;
    }
    
    int idComodatoBusqueda = -1;
    try{ idComodatoBusqueda = Integer.parseInt(request.getParameter("idComodatoBusqueda")); }catch(NumberFormatException e){}
    
    if(idComodatoBusqueda > 0){
        filtroBusqueda += " AND ID_COMODATO=" + idComodatoBusqueda +" ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="idComodatoBusqueda="+idComodatoBusqueda;
    }
    
    /*
    * Paginación
    */
    int paginaActual = 1;
    double registrosPagina = 20;
    double limiteRegistros = 0;
    int paginasTotales = 0;
    int numeroPaginasAMostrar = 5;

    try{
        paginaActual = Integer.parseInt(request.getParameter("pagina"));
    }catch(Exception e){}

    try{
        registrosPagina = Integer.parseInt(request.getParameter("registros_pagina"));
    }catch(Exception e){}
    
     ComodatoMantenimientoBO comodatoMantenimientoBO = new ComodatoMantenimientoBO(user.getConn());
     ComodatoMantenimiento[] comodatoMantenimientosDto = new ComodatoMantenimiento[0];
     try{
         limiteRegistros = comodatoMantenimientoBO.findComodatoMantenimientos(idComodatoMantenimiento, idComodato, idEmpresa , 0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        comodatoMantenimientosDto = comodatoMantenimientoBO.findComodatoMantenimientos(idComodatoMantenimiento, idComodato, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catComodatos/catComodatosMantenimiento_form.jsp";
    String paramName = "idComodatoMantenimiento";
    //String parametrosPaginacion="";// "idEmpresa="+idEmpresa;
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
                
                var dates2 = $('#q_fh_min2, #q_fh_max2').datepicker({
                        //minDate: 0,
			changeMonth: true,
			//numberOfMonths: 2,
                        //beforeShow: function() {$('#fh_min').css("z-index", 9999); },
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 998);
                            }, 500)},
			onSelect: function( selectedDate ) {
				var option = this.id == "q_fh_min2" ? "minDate" : "maxDate",
					instance = $( this ).data( "datepicker" ),
					date = $.datepicker.parseDate(
						instance.settings.dateFormat ||
						$.datepicker._defaults.dateFormat,
						selectedDate, instance.settings );
				dates2.not( this ).datepicker( "option", option, date );
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
                    <h1>Comodato Mantenimientos</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                Búsqueda Avanzada &dArr;
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content" style="display: none;">
                            <form action="catComodatosMantenimiento_list.jsp" id="search_form_advance" name="search_form_advance" method="post">
                                <p>
                                    Por Fecha de Realización &raquo;&nbsp;&nbsp;
                                    <label>Desde:</label>
                                    <input maxlength="15" type="text" id="q_fh_min" name="q_fh_min" style="width:100px"
                                            value="" readonly/>
                                    &nbsp; &laquo; &mdash; &raquo; &nbsp;
                                    <label>Hasta:</label>
                                    <input maxlength="15" type="text" id="q_fh_max" name="q_fh_max" style="width:100px"
                                        value="" readonly/>
                                </p>
                                <br/>
                                
                                <p>
                                    Por Fecha de Próximo Matenimiento &raquo;&nbsp;&nbsp;
                                    <label>Desde:</label>
                                    <input maxlength="15" type="text" id="q_fh_min2" name="q_fh_min2" style="width:100px"
                                            value="" readonly/>
                                    &nbsp; &laquo; &mdash; &raquo; &nbsp;
                                    <label>Hasta:</label>
                                    <input maxlength="15" type="text" id="q_fh_max2" name="q_fh_max2" style="width:100px"
                                        value="" readonly/>
                                </p>
                                <br/>
                                
                                <p>
                                <label>Cliente:</label><br/>
                                <select id="q_idcliente" name="q_idcliente" class="flexselect">
                                    <option></option>
                                    <%= new ClienteBO(user.getConn()).getClientesByIdHTMLCombo(idEmpresa, -1," AND ID_ESTATUS <> 2 " ) %>
                                </select>
                                </p>
                                <br/>
                                
                                <label>Equipo:</label><br/>
                                <select id="idComodatoBusqueda" name="idComodatoBusqueda" class="flexselect">
                                    <option></option>
                                    <%= new ComodatoBO(user.getConn()).getComodatosByIdHTMLCombo(idEmpresa, idComodato ) %>
                                </select>
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
                                <img src="../../images/icon_comodatoMantenimiento.png" alt="icon"/>
                                Comodato Mantenimientos
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
                                                            <td>
                                                                <div id="search">
                                                                <form action="catComodatosMantenimiento_list.jsp" id="search_form" name="search_form" method="get">
                                                                        <input type="text" id="q" name="q" title="Buscar por Descripción / Nombre atendió / Técnico"  class="" style="width: 300px; float: left; "
                                                                               value="<%=buscar%>"/>
                                                                        <input type="hidden" id="acc" name="acc" value="<%=mode%>"/>
                                                                        <input type="hidden" id="idComodato" name="idComodato" value="<%=idComodato%>"/>
                                                                        <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                                </form>
                                                                </div>
                                                            </td>
                                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                                            
                                                            <td>
                                                                <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Crear Nuevo" 
                                                                       style="float: right; width: 100px;" onclick="location.href='<%=urlTo%>?acc=<%=mode%>&idComodato=<%=idComodato%>'"/>
                                                            </td>
                                                            
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
                                            <th>Nombre de Cliente</th>
                                            <th>Equipo</th>
                                            <th>Fecha Realización</th>
                                            <th>Técnico</th>
                                            <th>Fecha Proximo Mantenimiento</th>                                                                                   
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%              
                                            Cliente cliente = null;
                                            ClienteDaoImpl clienteDaoImpl = new ClienteDaoImpl(user.getConn());
                                            ComodatoDaoImpl comodatoDaoImpl = new ComodatoDaoImpl(user.getConn());
                                            String nombreCliente = "";
                                            Comodato comodato = null;
                                            for (ComodatoMantenimiento item:comodatoMantenimientosDto){
                                                cliente = null;
                                                nombreCliente = "";
                                                comodato = null;
                                                try{
                                                    cliente = clienteDaoImpl.findByPrimaryKey(item.getIdCliente());
                                                    nombreCliente = cliente.getRazonSocial();
                                                }catch(Exception e){}
                                                try{
                                                    comodato = comodatoDaoImpl.findByPrimaryKey(item.getIdComodato());
                                                }catch(Exception e){}
                                                try{
                                        %>
                                        <tr>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdComodatoMantenimiento() %></td>
                                            <td><%=nombreCliente %></td>
                                            <td><%=comodato!=null?comodato.getNombre():""%></td>
                                            <td><%=item.getFechaRealizacionMantenimiento()!=null?DateManage.formatDateToNormal(item.getFechaRealizacionMantenimiento()):""%></td>
                                            <td><%=item.getTecnico()%></td>
                                            <td><%=item.getFechaProxMantenimiento()!=null?DateManage.formatDateToNormal(item.getFechaProxMantenimiento()):""%></td>                                            
                                            <td>
                                                <%if(RolAutorizacionBO.permisoAccionesElemento(user.getUser().getIdRoles())){%>
                                                <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdComodatoMantenimiento()%>&acc=mantenimiento&idComodato=<%=idComodato>0?idComodato:item.getIdComodato()%>&pagina=<%=paginaActual%>"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>
                                                &nbsp;&nbsp;                                                
                                                <!--<a href=""><img src="images/icon_delete.png" alt="delete" class="help" title="Delete"/></a>-->
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
                                <jsp:param name="idReport" value="<%= ReportBO.COMODATO_MANTENIMIENTO_REPORT %>" />
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
                            
                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                            
                        </div>
                    </div>
                                    
                                   

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>

        <script>
            mostrarCalendario();
        </script>
    </body>
</html>
<%}%>