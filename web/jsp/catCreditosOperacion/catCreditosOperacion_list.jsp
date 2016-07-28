<%-- 
    Document   : catCreditosOperacion_list
    Created on : 11/05/2015, 11/05/2015 11:57:30 AM
    Author     : ISCesar
--%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.BitacoraCreditosOperacion"%>
<%@page import="com.tsp.sct.bo.BitacoraCreditosOperacionBO"%>
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
    String buscar_idempleado = request.getParameter("q_idempleado")!=null?request.getParameter("q_idempleado"):"";
    String buscar_idsucursal = request.getParameter("q_idsucursal")!=null?request.getParameter("q_idsucursal"):"";
    String buscar_tipo = request.getParameter("q_tipo")!=null?request.getParameter("q_tipo"):"";
    String buscar_accion = request.getParameter("q_accion")!=null? new String(request.getParameter("q_accion").getBytes("ISO-8859-1"),"UTF-8") :"";
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
        
        if (fechaMin==null && fechaMax==null){
            //Si no se establecio ningun filtro de fechas específicas,
            // usamos por defecto, los ultimos 7 dias
            fechaMax = new Date();
            fechaMin = DateManage.sumaRestaDias(fechaMax, -7);
            buscar_fechamin = DateManage.formatDateToSQL(fechaMin);
            buscar_fechamax = DateManage.formatDateToSQL(fechaMax);
        }
        
        /*Filtro por rango de fechas*/        
        if (fechaMin!=null && fechaMax!=null){
            strWhereRangoFechas="(CAST(FECHA_HORA AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"') ";
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas="(FECHA_HORA  >= '"+buscar_fechamin+"')";
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas="(FECHA_HORA  <= '"+buscar_fechamax+"')";
        }
    }
    
    String filtroBusqueda = "";
    //if (!buscar.trim().equals(""))
    //    filtroBusqueda = " AND (NOMBRE LIKE '%" + buscar + "%' OR AREA_ALMACEN LIKE '%" +buscar+"%' OR RESPONSABLE LIKE '%" +buscar+"%')";
    
    
    
    if (!buscar_idcliente.trim().equals("")){
        filtroBusqueda += " AND ID_CLIENTE=" + buscar_idcliente +" ";
    }
    
    if (!buscar_idempleado.trim().equals("")){
        filtroBusqueda += " AND ID_USER_REGISTRA IN (SELECT ID_USUARIOS FROM empleado WHERE id_empleado = " + buscar_idempleado +") ";
    }
    
    if (!buscar_idsucursal.trim().equals("")){
        filtroBusqueda += " AND ID_EMPRESA=" + buscar_idsucursal +" ";
    }
    
    if (!buscar_tipo.trim().equals("")){
        if (!buscar_tipo.equals("-1"))
            filtroBusqueda += " AND TIPO=" + buscar_tipo +" ";
    }/*else{
        //Por defecto, si no se específica, se buscan solo Consumos/Descuentos
        filtroBusqueda += " AND TIPO=2 "; 
    }*/
    
    if (!buscar_accion.trim().equals("")){
        filtroBusqueda += " AND COMENTARIOS = '" + buscar_accion +"' ";
    }
    
    if (!strWhereRangoFechas.trim().equals("")){
        filtroBusqueda += " AND " + strWhereRangoFechas;
    }
    
    int idBitacoraCreditosOperacion = -1;
    try{ idBitacoraCreditosOperacion = Integer.parseInt(request.getParameter("idBitacoraCreditosOperacion")); }catch(NumberFormatException e){}
    
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
    
     BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(user.getConn());
     BitacoraCreditosOperacion[] bcosDto = new BitacoraCreditosOperacion[0];
     try{
         limiteRegistros = bcoBO.findCantidadBitacoraCreditosOperacion(idBitacoraCreditosOperacion, idEmpresa , 0, 0, filtroBusqueda);
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        bcosDto = bcoBO.findBitacoraCreditosOperacion(idBitacoraCreditosOperacion, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    //String urlTo = "../catBitacoraCreditosOperaciones/catBitacoraCreditosOperaciones_form.jsp";
    String paramName = "idBitacoraCreditosOperacion";
    String parametrosPaginacion="q="+buscar+"&q_idcliente="+buscar_idcliente+"&q_fh_min="+request.getParameter("q_fh_min")+"&q_fh_max="+request.getParameter("q_fh_max")+"&q_idempleado="+buscar_idempleado+"&q_idsucursal="+buscar_idsucursal+"&q_tipo="+buscar_tipo+"&q_accion="+buscar_accion+"&idBitacoraCreditosOperacion="+idBitacoraCreditosOperacion;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    
    
    Empresa empresaMatriz = new EmpresaBO(user.getConn()).getEmpresaMatriz(idEmpresa);
    
    int sumaDescuentosBitacora = 0;
    int sumaAbonosBitacora = 0;
    try{
        String filtroSumaDescuentos = filtroBusqueda + " AND TIPO=2 ";
        sumaDescuentosBitacora = bcoBO.findSumaCantidadBitacoraCreditosOperacion(idBitacoraCreditosOperacion, idEmpresa , 0, 0, filtroSumaDescuentos);
    }catch(Exception ex){
        ex.printStackTrace();
    }
    try{
        String filtroSumaAbonos = filtroBusqueda + " AND TIPO=1 ";
        sumaAbonosBitacora = bcoBO.findSumaCantidadBitacoraCreditosOperacion(idBitacoraCreditosOperacion, idEmpresa , 0, 0, filtroSumaAbonos);
    }catch(Exception ex){
        ex.printStackTrace();
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
				var option = this.id === "q_fh_min" ? "minDate" : "maxDate",
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
                    <h1>Mi Cuenta</h1>
                    
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
                            <form action="catCreditosOperacion_list.jsp" id="search_form_advance" name="search_form_advance" method="post">
                                <p>
                                    Por Fecha <br/>
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
                                <label>Cliente:</label><br/>
                                <select id="q_idcliente" name="q_idcliente" class="flexselect" style="width: 300px;">
                                    <option></option>
                                    <%= new ClienteBO(user.getConn()).getClientesByIdHTMLCombo(idEmpresa, -1," AND ID_ESTATUS=1 " ) %>
                                </select>
                                </p>
                                <br/>
                                
                                <p>
                                <label>Empleado:</label><br/>
                                <select id="q_idempleado" name="q_idempleado" class="flexselect" style="width: 300px;">
                                    <option></option>
                                    <%= new EmpleadoBO(user.getConn()).getEmpleadosByIdHTMLCombo(idEmpresa, -1," AND ID_ESTATUS=1 AND ID_USUARIOS>0 " ) %>
                                </select>
                                </p>
                                <br/>
                                
                                <p>
                                <label>Sucursal:</label><br/>
                                <select id="q_idsucursal" name="q_idsucursal" class="flexselect" style="width: 300px;">
                                    <option></option>
                                    <%= new EmpresaBO(user.getConn()).getEmpresasByIdHTMLCombo(idEmpresa, -1 ) %>
                                </select>
                                </p>
                                <br/>
                                
                                <p>
                                    <label>Tipo:</label><br/>
                                    <select id="q_tipo" name="q_tipo">
                                        <option value="-1" selected>Todos</option>
                                        <option value="1">Abono/Compra</option>
                                        <option value="2">Descuento/Consumo</option>
                                    </select>
                                </p>
                                <br/>
                                
                                <p>
                                <label>Acción:</label><br/>
                                <select id="q_accion" name="q_accion" class="flexselect" style="width: 300px;">
                                    <option></option>
                                    <%= new BitacoraCreditosOperacionBO(user.getConn()).getListaAccionesByIdHTMLCombo(idEmpresa, -1," AND ID_ESTATUS=1 " ) %>
                                </select>
                                </p>
                                <br/>
                                
                                <br/>
                                <div id="action_buttons">
                                    <p>
                                        <input type="button" id="buscar" value="Buscar" onclick="$('#search_form_advance').submit();"/>
                                    </p>
                                </div>
                                
                            </form>
                        </div>
                      </div>
                      <!-- End left column window -->
                        
                      <div class="column_right">
                        <div class="header">
                            <span>
                                Información General
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <span style="font-size: 16px;">
                                Créditos de Operación Disponibles: 
                                <label><%= empresaMatriz.getCreditosOperacion() %></label>
                                <br/>
                                Período consultado: <label><%= DateManage.formatDateToNormal(fechaMin) %> - <%= DateManage.formatDateToNormal(fechaMax) %></label>
                                <br/>
                                Créditos consumidos (filtro):  <%= sumaDescuentosBitacora %>
                                <br/>
                                Créditos comprados (filtro):  <%= sumaAbonosBitacora %>
                            </span>
                            <br/>
                        </div>
                      </div>
                      <!-- End right column window -->
                    </div>
                    <br class="clear"/>
                    
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_miCuenta.png" alt="icon"/>
                                Bitacora de Creditos de Operaci&oacute;n
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
                                                            <td></td>
                                                            <!--
                                                            <td>
                                                                <div id="search">
                                                                <form action="catCreditosOperacion_list.jsp" id="search_form" name="search_form" method="get">
                                                                        <input type="text" id="q" name="q" title="Buscar por Nombre BitacoraCreditosOperacion / Área / Nombre del Responsable" class="" style="width: 300px; float: left; "
                                                                               value="<%=buscar%>"/>
                                                                        <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                                </form>
                                                                </div>
                                                            </td>
                                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                                            -->
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
                                            <th>Empleado</th>
                                            <th>Cliente</th>                                            
                                            <th>Fecha-Hr</th>
                                            <th>Monto ($)</th>
                                            <th>Créditos</th>
                                            <th>Info</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            NumberFormat formatMoneda = new DecimalFormat("$ ###,###,###,##0.00");
                                            for (BitacoraCreditosOperacion item : bcosDto){
                                                try{
                                                    String nombreEmpleado = "";
                                                    String nombreCliente = "";
                                                    String fechaHora = "";
                                                    String monto = "";
                                                    String creditos = "";
                                                    
                                                    if (item.getIdUserRegistra()>0){
                                                        EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
                                                        Empleado  empleadoDto = empleadoBO.findEmpleadoByUsuario(item.getIdUserRegistra());
                                                        if (empleadoDto!=null){
                                                            nombreEmpleado = StringManage.getValidString(empleadoDto.getNombre()) + " "
                                                                    + StringManage.getValidString(empleadoDto.getApellidoPaterno()) +  " "
                                                                    + StringManage.getValidString(empleadoDto.getApellidoMaterno());
                                                        }
                                                    }
                                                    if (item.getIdCliente()>0){
                                                        ClienteBO clienteBO = new ClienteBO(item.getIdCliente(), user.getConn());
                                                        Cliente clienteDto = clienteBO.getCliente();
                                                        if (StringManage.getValidString(clienteDto.getRazonSocial()).length()>0){
                                                            nombreCliente = StringManage.getValidString(clienteDto.getRazonSocial());
                                                        }else{
                                                            nombreCliente =  StringManage.getValidString(clienteDto.getNombreCliente()) + " "
                                                                    + StringManage.getValidString(clienteDto.getApellidoPaternoCliente()) +  " "
                                                                    + StringManage.getValidString(clienteDto.getApellidoMaternoCliente());
                                                        }
                                                    }
                                                    fechaHora =  DateManage.formatDateTimeToNormalMinutes(item.getFechaHora());
                                                    if (item.getMontoOperacion()!=0)
                                                        monto = formatMoneda.format(item.getMontoOperacion());
                                                    if (item.getTipo()==2){
                                                        //Descuento/Consumo
                                                        creditos = "- ";
                                                    }
                                                    creditos += item.getCantidad();
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>
                                            <td><%=item.getIdBitacoraCreditosOperacion() %></td>
                                            <td><%= nombreEmpleado %></td>
                                            <td><%= nombreCliente %></td>                                            
                                            <td><%= fechaHora %></td>
                                            <td><%= monto %></td>
                                            <td <%= item.getTipo()==2?"style='color: red;'":""%>><%= creditos %></td>
                                            <td><%= StringManage.getValidString(item.getComentarios()) %></td>
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
                                <jsp:param name="idReport" value="<%= ReportBO.BITACORA_CR_OPERACION_REPORT %>" />
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