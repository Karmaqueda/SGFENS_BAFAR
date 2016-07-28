<%-- 
    Document   : catCallCenter_list
    Created on : 23/02/2016, 05:01:51 PM
    Author     : leonardo
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.CallCenterSeguimientoBO"%>
<%@page import="com.tsp.sct.dao.dto.CallCenterSeguimiento"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.CallCenter"%>
<%@page import="com.tsp.sct.bo.CallCenterBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String buscar_idcliente = request.getParameter("q_idcliente")!=null?request.getParameter("q_idcliente"):"";
    String ticketsId = request.getParameter("ticketsId")!=null?request.getParameter("ticketsId"):"";
    String tipoTicket = request.getParameter("tipoTicket")!=null?request.getParameter("tipoTicket"):"";
    
    String filtroBusqueda = "";
        
    if (!buscar.trim().equals(""))
        filtroBusqueda += " AND (NUMERO_TICKET LIKE '%" + buscar + "%') ";
    
    int idCallCenter = -1;
    try{ idCallCenter = Integer.parseInt(request.getParameter("idCallCenter")); }catch(NumberFormatException e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
     /*
    * Paginación
    */
    int paginaActual = 1;
    double registrosPagina = 20;
    double limiteRegistros = 0;
    int paginasTotales = 0;
    int numeroPaginasAMostrar = 5;

    
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
            strWhereRangoFechas="(CAST(FECHA_CREACION AS DATE) BETWEEN '"+buscar_fechamin+" 00:00:00' AND '"+buscar_fechamax+" 23:59:59')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax)+"&q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas="(FECHA_CREACION  >= '"+buscar_fechamin+" 00:00:00')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas="(FECHA_CREACION  <= '"+buscar_fechamax+" 23:59:59')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax);
        }
    }
    
    
    if (!strWhereRangoFechas.trim().equals("")){
        filtroBusqueda += " AND " + strWhereRangoFechas;
    }
    
    if (!buscar_idcliente.trim().equals("")){
        filtroBusqueda += " AND ID_CLIENTE='" + buscar_idcliente +"' ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q_idcliente="+buscar_idcliente;
    }    
    
    
    if (!ticketsId.trim().equals("")){
        if(ticketsId.equals("1")){//todos los tickets
            filtroBusqueda += " AND ESTADO > 0 ";
        }else if(ticketsId.equals("2")){//todos los tickets abiertos
            //filtroBusqueda += " AND ESTADO='" + ticketsId +"' ";
            filtroBusqueda += " AND ESTADO = 1 ";
        }else if(ticketsId.equals("3")){//todos los tickets cerrados
            filtroBusqueda += " AND ESTADO = 2 ";
        }else if(ticketsId.equals("4")){//mis tickets abiertos
             filtroBusqueda += " AND ESTADO = 1 AND ID_USUARIO = " + user.getUser().getIdUsuarios();
        }else if(ticketsId.equals("5")){//mis tickets CERRADOS
             filtroBusqueda += " AND ESTADO = 2 AND ID_USUARIO = " + user.getUser().getIdUsuarios();
        }        
        
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="ticketsId="+ticketsId;
    }else{
        //verificamos que rol tiene el usuario, para ver si mostramos todos o solo los suyos:
        if(user.getUser().getIdRoles() == RolesBO.ROL_CALL_CENTER  || user.getUser().getIdRoles() == RolesBO.ROL_ALMACENISTA){
            filtroBusqueda = " AND ID_USUARIO = " + user.getUser().getIdUsuarios();
        }
    }
    
    if (!tipoTicket.trim().equals("")){
        filtroBusqueda += " AND TIPO ='" + tipoTicket +"' ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="tipoTicket="+tipoTicket;
    }
    
   
    try{
        paginaActual = Integer.parseInt(request.getParameter("pagina"));
    }catch(Exception e){}

    try{
        registrosPagina = Integer.parseInt(request.getParameter("registros_pagina"));
    }catch(Exception e){}
    
     CallCenterBO callCenterBO = new CallCenterBO(user.getConn());
     CallCenter[] callCentersDto = new CallCenter[0];
     try{
         limiteRegistros = callCenterBO.findCallCenters(idCallCenter, idEmpresa , 0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        callCentersDto = callCenterBO.findCallCenters(idCallCenter, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     //obtenemos los totales, cerrados, abiertos y los levantados durante el día:
     CallCenter[] callCentersLevantadosDia = new CallCenter[0];
     CallCenter[] callCentersCerrados = new CallCenter[0];
     CallCenter[] callCentersAbiertos = new CallCenter[0];
     
     callCentersCerrados = callCenterBO.findCallCenters(0, idEmpresa , 0, 0, " AND ESTADO = 2 ");
     callCentersAbiertos = callCenterBO.findCallCenters(0, idEmpresa , 0, 0, " AND ESTADO = 1 ");
     callCentersLevantadosDia = callCenterBO.findCallCenters(0, idEmpresa , 0, 0, " AND (CAST(FECHA_CREACION AS DATE) BETWEEN '"+DateManage.formatDateToSQL(new Date())+" 00:00:00' AND '"+DateManage.formatDateToSQL(new Date())+" 23:59:59') ");
     
     
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catCallCenter/catCallCenter_form.jsp";
    String urlToA = "../catCallCenter/catCallCenterSeguimiento_form.jsp";
    String urlTo2 = "../catCallCenter/catCallCenterSeguimiento_list.jsp";
    String paramName = "idCallCenter";
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
                    <h1>Call Center</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>
                                        
                    <div class=twocolumn>
                      <div class="column_left">
                        <div class="header">
                            <span>
                                Busqueda Avanzada &dArr;
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content" style="display: none;">
                            <form action="catCallCenter_list.jsp" id="search_form_advance" name="search_form_advance" method="post">
                                <p>
                                    Por Fecha de Creación &raquo;&nbsp;&nbsp;
                                    <label>Desde:</label>
                                    <input maxlength="15" type="text" id="q_fh_min" name="q_fh_min" style="width:80px"
                                            value="" readonly/>
                                    &nbsp; &laquo; &mdash; &raquo; &nbsp;
                                    <label>Hasta:</label>
                                    <input maxlength="15" type="text" id="q_fh_max" name="q_fh_max" style="width:80px"
                                        value="" readonly/>
                                </p>
                                <br/>
                                
                                <p>
                                <label>Cliente:</label><br/>
                                <select id="q_idcliente" name="q_idcliente" class="flexselect">
                                    <option></option>
                                    <%= new ClienteBO(user.getConn()).getClientesByIdHTMLCombo(idEmpresa, -1," AND ID_ESTATUS <> 2 " + (user.getUser().getIdRoles() == RolesBO.ROL_OPERADOR? " AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM SGFENS_CLIENTE_VENDEDOR WHERE ID_USUARIO_VENDEDOR="+user.getUser().getIdUsuarios()+")" : "") ) %>
                                </select>
                                </p>
                                <br/>
                                
                                <p>
                                    <label>Tickets:</label><br/>
                                    <select size="1" id="ticketsId" name="ticketsId" >
                                            <option value="" >Selecciona</option>
                                            <option value="1" >Todos los tickets</option>
                                            <option value="2" >Tickets abiertos </option>
                                            <option value="3" >Tickets cerrados </option>
                                            <option value="4" >Mis tickets abiertos</option>
                                            <option value="5" >Mis tickets cerrados</option>
                                        </select> 
                                </p>
                                <br/>
                                
                                <p>
                                    <label>Tipo:</label><br/>
                                    <select size="1" id="tipoTicket" name="tipoTicket" >
                                            <option value="" >Selecciona</option>
                                            <option value="1" >Solicitud</option>
                                            <option value="2" >Queja</option>
                                            <option value="3" >Sugerencia</option>
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
                    
                    <div class="column_right">
                        <div class="header">
                            <span>
                                Tickets
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <span style="font-size: 16px;">
                                Tickets totales:<label title="Tickets que se levantaron en el día"><%= callCentersLevantadosDia.length %></label>
                                <br/>
                                Tickets Resueltos: <label title="Tickets totales que estan cerrados/resueltos"><%= callCentersCerrados.length %></label>
                                <br/>
                                Tickets Pendientes: <label title="Tickets totales que estan pendientes"><%= callCentersAbiertos.length %></label>
                            </span>
                            <br/>
                        </div>
                      </div>
                    </div>
                            
                    <br class="clear"/>
                    <br class="clear"/>

                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_callCenter.png" alt="icon"/>
                                Call Center
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                            <tr>
                                                <td>
                                                    <div id="search">
                                                    <form action="catCallCenter_list.jsp" id="search_form" name="search_form" method="get">
                                                            <input type="text" id="q" name="q" title="Buscar por número de ticket" class="" style="width: 300px; float: left; "
                                                                    value="<%=buscar%>"/>
                                                            <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                    </form>
                                                    </div>
                                                </td>
                                                <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                                <td>
                                                    <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Crear Nuevo" 
                                                            style="float: right; width: 100px;" onclick="location.href='<%=urlTo%>'"/>
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
                                            <th>Ticket</th>
                                            <th>Descripción</th>
                                            <th>Fecha/Hora creación</th>
                                            <th>Cliente</th>
                                            <th>Último seguimiento</th>
                                            <th>Tipo</th>
                                            <th>Estado</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            CallCenterSeguimientoBO callCenterSeguimientoBO = new CallCenterSeguimientoBO(user.getConn());
                                            for (CallCenter item:callCentersDto){
                                                try{
                                                    String nombreCliente = "";
                                                    if(item.getIdCliente() > 0){
                                                        try{
                                                            Cliente cli = new ClienteBO(item.getIdCliente(), user.getConn()).getCliente();
                                                            nombreCliente = cli.getNombreCliente() + " " + cli.getApellidoPaternoCliente() + " " + cli.getApellidoMaternoCliente();
                                                        }catch(Exception e){} 
                                                    }else{
                                                        nombreCliente = item.getNombre() + " " + item.getApellidoPaterno() + " " + item.getApellidoMaterno();
                                                    }
                                                    String seguimiento = "sin seguimiento";
                                                    if(item.getConSeguimiento() == 1){
                                                        try{
                                                            CallCenterSeguimiento ccs = callCenterSeguimientoBO.findCallCenterSeguimientos(0, 0, 0, " AND ID_CALL_CENTER = " + item.getIdCallCenter())[0];
                                                            seguimiento =  ccs.getDescripcion();
                                                        }catch(Exception e){}
                                                        
                                                        
                                                    }

                                        %>
                                        <tr>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdCallCenter() %></td>
                                            <td><%=item.getNumeroTicket() %></td>
                                            <td><%=item.getDescripcion()%></td>                                            
                                            <td><%=DateManage.formatDateTimeToNormalMinutes(item.getFechaCreacion())%></td>
                                            <td><%=nombreCliente%></td> 
                                            <td><%=seguimiento%></td>
                                            <td><%=item.getTipo() == 1?"Solicitud":item.getTipo() == 2?"Queja":item.getTipo() == 3?"Sugerencia":""%></td> 
                                            <td><%=item.getEstado() == 1?"Abierto":item.getEstado() == 2?"cerrado":item.getEstado() == 3?"Re-abierto":""%></td> 
                                            <td>
                                                <%if(item.getEstado() == 1){%>
                                                <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdCallCenter()%>&acc=2&pagina=<%=paginaActual%>"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>
                                                &nbsp;&nbsp;
                                                <%}%>
                                                <a href="<%=urlTo2%>?<%=paramName%>=<%=item.getIdCallCenter()%>&acc=1&pagina=<%=paginaActual%>"><img src="../../images/icon_callCenterSeguimiento.png" alt="seguimiento" class="help" title="Seguimiento"/></a>
                                                &nbsp;&nbsp;
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