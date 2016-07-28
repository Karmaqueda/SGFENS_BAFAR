<%-- 
    Document   : catCobranzaAbono_list
    Created on : 07-ene-2013, 13:28:23
    Author     : ISCesarMartinez
--%>

<%@page import="com.tsp.sct.bo.VentaMetodoPagoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensCobranzaMetodoPagoDaoImpl"%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.dao.dto.ClienteCategoria"%>
<%@page import="com.tsp.sct.bo.ClienteCategoriaBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="com.tsp.sct.bo.SGCobranzaMetodoPagoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensCobranzaMetodoPago"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.tsp.sct.bo.ComprobanteFiscalBO"%>
<%@page import="com.tsp.sct.bo.SGPedidoBO"%>
<%@page import="com.tsp.sct.dao.dto.ComprobanteFiscal"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedido"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.dao.dto.DatosUsuario"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensCobranzaAbono"%>
<%@page import="com.tsp.sct.bo.SGCobranzaAbonoBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
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
    String referenciaPago = request.getParameter("referenciaPago") != null ? new String(request.getParameter("referenciaPago").getBytes("ISO-8859-1"), "UTF-8") : "";
    String buscar_idMetodoPago = request.getParameter("idMetodoPago")!=null?request.getParameter("idMetodoPago"):"";
    
    int idClienteCategoria = -1;
    try{
        idClienteCategoria = Integer.parseInt(request.getParameter("idClienteCategoria"));
    }catch(NumberFormatException ex){}
    String buscar_idvendedor = request.getParameter("q_idvendedor")!=null?request.getParameter("q_idvendedor"):"";
    int idComprobanteFiscal = -1;
    int idPedido = -1;
    
    try{ idComprobanteFiscal = Integer.parseInt(request.getParameter("idComprobanteFiscal")); }catch(NumberFormatException e){}
    try{ idPedido = Integer.parseInt(request.getParameter("idPedido")); }catch(NumberFormatException e){}
    String buscar_consigna = request.getParameter("q_consigna")!=null? new String(request.getParameter("q_consigna").getBytes("ISO-8859-1"),"UTF-8") :""; 
    
    String buscar_fechamin = "";
    String buscar_fechamax = "";
    Date fechaMin=null;
    Date fechaMax=null;
    String strWhereRangoFechas="";
    String filtroBusqueda = "";
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
            strWhereRangoFechas="(CAST(FECHA_ABONO AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax)+"&q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas="(FECHA_ABONO  >= '"+buscar_fechamin+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas="(FECHA_ABONO  <= '"+buscar_fechamax+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax);
        }
    }
    
    
    //Si es vendedor, filtramos para solo mostrar sus pedidos
    if (user.getUser().getIdRoles() == RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL){
        filtroBusqueda += " AND ID_USUARIO_VENDEDOR ='" + user.getUser().getIdUsuarios() + "' ";        
    }
        
    if (!buscar.trim().equals("")){
        filtroBusqueda += " AND ID_COBRANZA_ABONO LIKE '%" + buscar + "%' ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q="+buscar;
        
    }
    
    if (!buscar_idcliente.trim().equals("")){
        filtroBusqueda += " AND ID_CLIENTE='" + buscar_idcliente +"' ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="buscar_idcliente="+buscar_idcliente;
    }else{
        filtroBusqueda += " AND ID_CLIENTE>=0 ";
    }
    
    if (idPedido>0){
        filtroBusqueda += " AND ID_PEDIDO=" + idPedido +" ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="idPedido="+idPedido;
    }
    if (idComprobanteFiscal>0){
        filtroBusqueda += " AND ID_COMPROBANTE_FISCAL=" + idComprobanteFiscal +" ";
    }
    
    if (!buscar_idvendedor.trim().equals("")){
        filtroBusqueda += " AND ID_USUARIO_VENDEDOR='" + buscar_idvendedor +"' ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q_idvendedor="+buscar_idvendedor;
    }
    
   
   
    if (buscar_consigna.trim().equals("0")){       
        filtroBusqueda += " AND ID_PEDIDO IN (SELECT ID_PEDIDO FROM sgfens_pedido WHERE CONSIGNA = 0) ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q_consigna="+buscar_consigna;
    }else if(buscar_consigna.trim().equals("1")){ 
        filtroBusqueda += " AND ID_PEDIDO IN (SELECT ID_PEDIDO FROM sgfens_pedido WHERE CONSIGNA = 1) ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q_consigna="+buscar_consigna;
    }
    
       
    
    /*
    if (!buscar_idestatuspedido.trim().equals("")){
        filtroBusqueda += " AND ID_ESTATUS_PEDIDO='" + buscar_idestatuspedido +"' ";
    }*/
    
    if (!strWhereRangoFechas.trim().equals("")){
        filtroBusqueda += " AND " + strWhereRangoFechas;
    }
    
    if(idClienteCategoria > 0){
        filtroBusqueda += " AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM CLIENTE WHERE ID_CLIENTE_CATEGORIA = " + idClienteCategoria + ")";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="idClienteCategoria="+idClienteCategoria;
    }
    
    /* agregamos filtro referencia*/
    if (!referenciaPago.trim().equals("")) {
        filtroBusqueda += " AND REFERENCIA = '" + referenciaPago + "' ";
        if (!parametrosPaginacion.equals("")) {
            parametrosPaginacion += "&";
        }
        parametrosPaginacion += "referenciaPago=" + referenciaPago;
    }
    
    
    /* Fitlro Método Pago*/
    
    if (!buscar_idMetodoPago.trim().equals("")) {
        filtroBusqueda += " AND ID_COBRANZA_METODO_PAGO = '" + buscar_idMetodoPago + "' ";
        if (!parametrosPaginacion.equals("")) {
            parametrosPaginacion += "&";
        }
        parametrosPaginacion += "referenciaPago=" + buscar_idMetodoPago;
    }
    
    
    int idCobranzaAbono = -1;
    try{ idCobranzaAbono = Integer.parseInt(request.getParameter("idCobranzaAbono")); }catch(NumberFormatException e){}
    
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
    
    if (idPedido>0 || idComprobanteFiscal>0)
        registrosPagina = 100;
        
     SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(user.getConn());
     SgfensCobranzaAbono[] cobranzaAbonoDto = new SgfensCobranzaAbono[0];
     SgfensCobranzaAbono[] cobranzaAbonoDtoTodos = new SgfensCobranzaAbono[0];
     try{
         limiteRegistros = cobranzaAbonoBO.findCobranzaAbono(idCobranzaAbono, idEmpresa , 0, 0, filtroBusqueda).length;
         cobranzaAbonoDtoTodos = cobranzaAbonoBO.findCobranzaAbono(idCobranzaAbono, idEmpresa , 0, 0, filtroBusqueda);
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        cobranzaAbonoDto = cobranzaAbonoBO.findCobranzaAbono(idCobranzaAbono, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     SgfensPedido pedidoDto = null;
     ComprobanteFiscal comprobanteFiscalDto = null;
     SGPedidoBO pedidoBO = null;
     ComprobanteFiscalBO comprobanteFiscalBO = null;
     
     String vendedorGralStr ="";
     try{
         if (idPedido>0){
            pedidoBO = new SGPedidoBO(idPedido,user.getConn());
            pedidoDto = pedidoBO.getPedido();
            
            DatosUsuario datosUsuarioVendedorGralDto = new UsuarioBO(pedidoDto.getIdUsuarioVendedor()).getDatosUsuario();
            vendedorGralStr = datosUsuarioVendedorGralDto.getNombre() + " " + datosUsuarioVendedorGralDto.getApellidoPat();
        }
         if (idComprobanteFiscal>0){
             comprobanteFiscalBO = new ComprobanteFiscalBO(idComprobanteFiscal,user.getConn());
             comprobanteFiscalDto = comprobanteFiscalBO.getComprobanteFiscal();
        }
     }catch(Exception ex){
         ex.printStackTrace();
     }
     
    double sumaAbonos = 0;
    double sumaAbonosCancelaciones = 0;
    double sumaEfeDevuelto = 0;
    double sumaEfeDevueltoCancelado = 0;//Solo para futuro uso
    double sumaAbonosActivos = 0;
    
    
    
    for (SgfensCobranzaAbono item:cobranzaAbonoDtoTodos){
        
        
                                                    
        if (item.getIdEstatus()==1){
            if(item.getIdCobranzaMetodoPago()!=VentaMetodoPagoBO.METODO_PAGO_DEVOLUCION_EFE){//Diferente de Devolucion efectivo
                sumaAbonos+=item.getMontoAbono();
                sumaAbonosActivos += item.getMontoAbono();
            }            
            if(item.getIdCobranzaMetodoPago() == VentaMetodoPagoBO.METODO_PAGO_DEVOLUCION_EFE){
                sumaEfeDevuelto += item.getMontoAbono();
            }
            
        }else{
            if(item.getIdCobranzaMetodoPago()!=VentaMetodoPagoBO.METODO_PAGO_DEVOLUCION_EFE){//Diferente de Devolucion efectivo
               sumaAbonosCancelaciones += item.getMontoAbono();
            }
            if(item.getIdCobranzaMetodoPago() == VentaMetodoPagoBO.METODO_PAGO_DEVOLUCION_EFE){
                sumaEfeDevueltoCancelado += item.getMontoAbono();
            }
        }
        
    }
     
     
     ClienteCategoriaBO clienteCategoriaBO = new ClienteCategoriaBO(user.getConn());
     SGCobranzaMetodoPagoBO metodoPagoBO = new SGCobranzaMetodoPagoBO(user.getConn());
     ClienteCategoria[] clientesCategorias = clienteCategoriaBO.findClienteCategorias(0, idEmpresa, 0, 0, " AND ID_ESTATUS = 1 ");
          
     /*
    * Datos de catálogo
    */
    String urlTo = "../catCobranzaAbono/catCobranzaAbono_form.jsp";
    String paramName = "idCobranzaAbono";
    //String parametrosPaginacion="";// "idEmpresa="+idEmpresa;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    
    String nuevoAbonoParam="idPedido="+idPedido+"&idComprobanteFiscal="+idComprobanteFiscal;
    
    NumberFormat formatMoneda = new DecimalFormat("$ ###,###,###,##0.00");
    
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
                    <h1>Ventas - Cobranza</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <% if (pedidoDto==null && comprobanteFiscalDto==null){%>
                    <div id="info_cobranza" class="alert_info">
                        <br/>
                        &nbsp;&nbsp;Para registrar un abono debe ir al apartado de pedidos o facturas y
                        seleccionar del registro deseado el icono 
                        <img src="../../images/icon_ventas1.png"/>
                        (Cobranza)
                        <br/>
                        &nbsp;
                    </div>
                    <%}%>
                    
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                Busqueda Avanzada &dArr;
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content" style="display: none;">
                            <form action="catCobranzaAbono_list.jsp" id="search_form_advance" name="search_form_advance" method="post">
                                <input type="hidden" id="idPedido" name="idPedido" value="<%=idPedido %>"/>
                                <input type="hidden" id="idComprobanteFiscal" name="idComprobanteFiscal" value="<%=idComprobanteFiscal %>"/>
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
                                
                                <p>
                                <label>Cliente:</label><br/>
                                <select id="q_idcliente" name="q_idcliente" class="flexselect">
                                    <option></option>
                                    <%= new ClienteBO(user.getConn()).getClientesByIdHTMLCombo(idEmpresa, -1," AND ID_ESTATUS<>2 " + (user.getUser().getIdRoles() == RolesBO.ROL_CONDUCTOR_VENDEDOR? " AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM SGFENS_CLIENTE_VENDEDOR WHERE ID_USUARIO_VENDEDOR="+user.getUser().getIdUsuarios()+")" : "") ) %>
                                </select>
                                </p>
                                <br/>
                                
                                <%if(clientesCategorias.length > 0){%>
                                    <p>
                                        <label>Tipo:</label><br/>
                                        <select id="idClienteCategoria" name="idClienteCategoria" class="flexselect">
                                            <option value="0"></option>
                                            <%
                                                out.print(clienteCategoriaBO.getClienteCategoriasByIdHTMLCombo(idEmpresa, -1));
                                            %>
                                        </select>
                                    </p>
                                    <br/>
                                <%}%>
                                
                                <% if (user.getUser().getIdRoles() != RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL){%>
                                <p>
                                <label>Vendedor:</label><br/>
                                <select id="q_idvendedor" name="q_idvendedor" class="flexselect">
                                    <option></option>                 
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL, 0) %> 
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_OPERADOR, 0) %>
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR, 0) %>
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR_VENDEDOR, 0) %>
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_COBRADOR, 0) %>
                                </select>
                                </p>
                                <br/>
                                <p>
                                    <label>Método de Pago:</label><br/>
                                    <select id="idMetodoPago" name="idMetodoPago" class="flexselect">
                                        <option value=""></option>
                                        <%
                                            out.print(metodoPagoBO.getMetodoPagoHTMLCombo(-1,idEmpresa, ""));
                                        %>
                                    </select>
                                </p>
                                <br/>
                                
                                <p>
                                    <input type="radio" class="checkbox" id="noConsigna" name="q_consigna" value="0" > <label for="noConsigna">Normales</label>
                                    <input type="radio" class="checkbox" id="siConsigna" name="q_consigna" value="1" > <label for="siConsigna">En Consigna</label>                                                   
                                </p>    
                                
                                <br/>
                                <% } %>
                                
                                <br/>
                                <div id="action_buttons">
                                    <p>
                                        <input type="button" id="buscar" value="Buscar" onclick="$('#search_form_advance').submit();"/>
                                    </p>
                                </div>
                                
                            </form>
                        </div>
                    </div>
                                
                    <% if (pedidoDto!=null || comprobanteFiscalDto!=null){ %>
                    <div id="threecolumn" class="threecolumn">
                        <div class="threecolumn_each" style="float: right;">
                                <div class="header">
                                        <span>Datos Financieros de<%=pedidoDto!=null?"l Pedido":(comprobanteFiscalDto!=null?" la Factura":"")%></span>
                                </div>
                                <br class="clear"/>
                                <div class="content">
                                        <%if (!vendedorGralStr.equals("")){%>
                                            <label>Vendedor: </label>
                                            <%=vendedorGralStr %>
                                            <br/>
                                        <%}%>
                                        <label>Folio de<%=pedidoDto!=null?"l Pedido":(comprobanteFiscalDto!=null?" la Factura":"")%>: </label>
                                            <%=pedidoDto!=null?pedidoDto.getFolioPedido():""%>
                                            <%=comprobanteFiscalDto!=null?"<br/>"+comprobanteFiscalDto.getUuid():""%>
                                        <br/>
                                        <label>Monto Total: </label>
                                            <%= pedidoDto!=null?(pedidoDto.getBonificacionDevolucion()>0?formatMoneda.format(pedidoDto.getTotal()):formatMoneda.format(pedidoDto.getTotal()+ Math.abs(pedidoDto.getBonificacionDevolucion()))):""%>
                                            <%=comprobanteFiscalDto!=null?formatMoneda.format(comprobanteFiscalDto.getImporteNeto()):""%>                                            
                                        <br/>
                                        <label>Monto Cubierto: </label>
                                            <%=pedidoDto!=null?formatMoneda.format(cobranzaAbonoBO.getSaldoPagadoPedido(pedidoDto.getIdPedido()).doubleValue()):""%>
                                            <%=comprobanteFiscalDto!=null?formatMoneda.format(cobranzaAbonoBO.getSaldoPagadoComprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal()).doubleValue()) :"" %>
                                        <br/>
                                         <label>Monto Adeudado: </label>
                                            <%= pedidoDto!=null?(pedidoDto.getBonificacionDevolucion()>0?formatMoneda.format(pedidoDto.getTotal() - pedidoDto.getSaldoPagado()):formatMoneda.format((pedidoDto.getTotal() + Math.abs(pedidoDto.getBonificacionDevolucion())) - pedidoDto.getSaldoPagado())):"" %>
                                            <%=comprobanteFiscalDto!=null?formatMoneda.format(comprobanteFiscalDto.getImporteNeto() - cobranzaAbonoBO.getSaldoPagadoComprobanteFiscal(comprobanteFiscalDto.getIdComprobanteFiscal()).doubleValue()):""%>   
                                        <br/>
                                        <label>Fecha máx de Pago: </label>
                                            <%=pedidoDto!=null?DateManage.dateToStringEspanol(pedidoDto.getFechaTentativaPago()):""%>
                                            <%=comprobanteFiscalDto!=null?DateManage.dateToStringEspanol(comprobanteFiscalDto.getFechaPago()):""%>
                                        <br/>
                                        <label>Días de crédito: </label>
                                            <%=pedidoDto!=null?pedidoBO.calculaDiasCredito():""%>
                                            <%=comprobanteFiscalDto!=null?comprobanteFiscalBO.calculaDiasCredito():""%>
                                        <br/>
                                        <label>Días de atraso: </label>
                                            <%=pedidoDto!=null?pedidoBO.calculaDiasMora():""%>
                                            <%=comprobanteFiscalDto!=null?comprobanteFiscalBO.calculaDiasMora():""%>
                                        <br/>
                                        <a href="../../jsp/reportesExportar/previewCobranzaEdoCuentaPDF.jsp?idPedido=<%=idPedido %>&idComprobanteFiscal=<%=idComprobanteFiscal %>" id="btn_show_cfdi" title="Edo. Cuenta"
                                                    class="modalbox_iframe" style="float: right;">
                                            <img src="../../images/icon_pdf.png" alt="Mostrar Edo. Cuenta" class="help" title="Edo. Cuenta"/>
                                            Edo. Cuenta (PDF)
                                        </a>
                                </div>
                        </div>
                    </div>
                    <br class="clear"/>
                    <%}%>
                                    
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_ventas1.png" alt="icon"/>
                                Cobranza - Abonos&nbsp;
                                <% if (pedidoDto!=null){%>
                                    <a href="../pedido/pedido_list.jsp?idPedido=<%=pedidoDto.getIdPedido()%>">
                                        de Pedido con Folio <%=pedidoDto.getFolioPedido()%>
                                    </a>
                                <%}%>
                                <% if (comprobanteFiscalDto!=null){%>
                                    <a href="../cfdi_factura/cfdi_factura_list.jsp?idComprobanteFiscal=<%=comprobanteFiscalDto.getIdComprobanteFiscal()%>">
                                        de CFDI con ID <%=comprobanteFiscalDto.getIdComprobanteFiscal()%>
                                    </a>
                                <%}%>
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td>
                                                <div id="search">
                                                <form action="catCobranzaAbono_list.jsp" id="search_form" name="search_form" method="get">
                                                    <input type="hidden" id="idPedido" name="idPedido" value="<%=idPedido %>"/>
                                                    <input type="hidden" id="idComprobanteFiscal" name="idComprobanteFiscal" value="<%=idComprobanteFiscal %>"/>
                                                    <input type="text" id="q" name="q" title="Buscar por ID Abono" class="" style="width: 300px; float: left; "
                                                        value="<%=buscar%>"/>
                                                    
                                                    <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                </form>
                                                </div>
                                            </td>
                                            <% if (pedidoDto!=null || comprobanteFiscalDto!=null){ %>
                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                            <%if(RolAutorizacionBO.permisoNuevoElemento(user.getUser().getIdRoles())){%>
                                            <td>
                                                <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Registrar Abono" 
                                                        style="float: right; width: 100px;" onclick="location.href='<%=urlTo%>?acc=1&<%=nuevoAbonoParam%>'"/>
                                            </td>
                                            <%}%>
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
                                            <th>Vendedor</th>
                                            <th>Cliente</th>
                                            <th>Fecha</th>
                                            <th>Método de Pago</th>
                                            <th>Referencia</th>
                                            <th>Estatus</th>
                                            <th><center>Monto Cobrado ($)</center></th>
                                            <th><center>Monto Cubierto Actual ($)</center></th>
                                            <th><center>Venta Total ($)</center></th>
                                            <th>Tipo Pago</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            
                                            for (SgfensCobranzaAbono item:cobranzaAbonoDto){
                                                try{
                                                    
                                                    
                                                    String vendedorStr ="";
                                                    String clienteStr ="";
                                                    String metodoPagoStr="No identificado";
                                                    DatosUsuario datosUsuarioVendedorDto = new UsuarioBO(item.getIdUsuarioVendedor()).getDatosUsuario();
                                                    Cliente clienteDto = new ClienteBO(item.getIdCliente(),user.getConn()).getCliente();
                                                    SgfensCobranzaMetodoPago metodoPagoDto = new SGCobranzaMetodoPagoBO(item.getIdCobranzaMetodoPago(),user.getConn()).getCobranzaMetodoPago();
                                                    
                                                    if (datosUsuarioVendedorDto!=null)
                                                        vendedorStr = datosUsuarioVendedorDto.getNombre() + " " + datosUsuarioVendedorDto.getApellidoPat();
                                                    if (clienteDto!=null)
                                                        clienteStr = clienteDto.getRazonSocial() + (clienteDto.getClave()!=null&&!clienteDto.getClave().trim().equals("")?(", Clave: "+clienteDto.getClave()):"");
                                                    if (metodoPagoDto!=null)
                                                        metodoPagoStr = metodoPagoDto.getNombreMetodoPago();
                                                    
                                                    ///*obtenemos el pedido al que se relaciona:
                                                    SgfensPedido pedido = null;
                                                    double montoCubierto = 0;
                                                    double montoPagarTotal = 0;
                                                    try{
                                                        pedido = new SGPedidoBO(item.getIdPedido(), user.getConn()).getPedido();
                                                        montoCubierto = (pedido.getSaldoPagado() + pedido.getAdelanto());
                                                        montoPagarTotal = pedido.getTotal();
                                                        if(pedido.getBonificacionDevolucion()<0){
                                                            montoPagarTotal+= Math.abs(pedido.getBonificacionDevolucion());
                                                        }
                                                        
                                                    }catch(Exception e){}
                                                    
                                                    ///*
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%> >
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdCobranzaAbono() %></td>
                                            <td><%=vendedorStr %></td>
                                            <td><%=clienteStr %></td>
                                            <td><%=DateManage.dateTimeToStringEspanol(item.getFechaAbono()) %></td>
                                            <td><%=metodoPagoStr %></td>
                                            <td><%=item.getReferencia()%></td>
                                            <td><%=(item.getIdEstatus()==1)?"Activo":"Cancelado"%></td>
                                            <td style="text-align: right;"><%=item.getMontoAbono() %></td>
                                            <td style="text-align: right;"><%=montoCubierto %></td>
                                            <td style="text-align: right;"><%=montoPagarTotal %></td>
                                            <td><%=pedido.getConsigna()==0?"Normal":"Consigna" %></td>
                                            <td>
                                                <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdCobranzaAbono()%>&acc=2"><img src="../../images/icon_consultar.png" alt="consultar" class="help" title="Consultar"/></a>
                                                &nbsp;&nbsp;
                                            </td>
                                        </tr>
                                        <%      }catch(Exception ex){
                                                    ex.printStackTrace();
                                                }
                                            } 
                                        %>
                                        <%if (pedidoDto!=null){
                                            sumaAbonos+=pedidoDto.getAdelanto();
                                        %>
                                        <tr style="font-size: 14;">
                                            <td colspan="6" style="text-align: right;"><b>(+) ADELANTO:</b></td>
                                            <td style="text-align: right;"><%=formatMoneda.format(pedidoDto.getAdelanto())%> </td>
                                            <td>&nbsp;</td>
                                        </tr>
                                        <%}%>
                                        <tr style="font-size: 14;">
                                            <td colspan="6" style="text-align: right;"><b>(+) Subtotal Cobros:</b></td>
                                            <td style="color: #0000cc; text-align: right;"><%=formatMoneda.format(sumaAbonos)%> </td>
                                            <td>&nbsp;</td>
                                        </tr>
                                        <tr style="font-size: 14;">
                                            <td colspan="6" style="text-align: right;"><b>(-) Subtotal Cancelados:</b></td>
                                            <td style="color: #0000cc; text-align: right;"><%=formatMoneda.format(sumaAbonosCancelaciones)%> </td>
                                            <td>&nbsp;</td>
                                        </tr>
                                        <tr style="font-size: 14;">
                                            <td colspan="6" style="text-align: right;"><b>(-) Subtotal Devolución EFE:</b></td>
                                            <td style="color: #0000cc; text-align: right;"><%=formatMoneda.format(sumaEfeDevuelto)%> </td>
                                            <td>&nbsp;</td>
                                        </tr>
                                        <tr style="font-size: 14;">
                                            <td colspan="6" style="text-align: right;"><b>TOTAL:</b></td>
                                            <td style="color: #0000cc; text-align: right;"><%=formatMoneda.format(sumaAbonosActivos-sumaEfeDevuelto)%> </td>
                                            <td>&nbsp;</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </form>
                             
                            <% if (pedidoDto!=null || comprobanteFiscalDto!=null) {
                                //Nada
                               }else{%>
                                <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                                <jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <jsp:param name="idReport" value="<%= ReportBO.COBRANZA_ABONO_REPORT %>" />
                                <jsp:param name="parametrosCustom" value="<%= filtroBusquedaEncoded %>" />
                                </jsp:include>                                
                                <!-- FIN INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <%}%>
                                 
                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                            
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