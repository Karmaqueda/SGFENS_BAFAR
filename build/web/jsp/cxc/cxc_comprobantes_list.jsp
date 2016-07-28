<%-- 
    Document   : cxc_comprobantes_list.jsp
    Created on : 06-apr-2015, 12:13:49
    Author     : ISCesarMartinez poseidon24@hotmail.com
--%>
<%@page import="com.tsp.sct.bo.VistaCxcBO"%>
<%@page import="com.tsp.sct.bo.SGPedidoBO"%>
<%@page import="com.tsp.sct.bo.SGCobranzaAbonoBO"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.tsp.sgfens.sesion.ComprobanteFiscalSesion"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.dao.dto.Folios"%>
<%@page import="com.tsp.sct.bo.FoliosBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensPedidoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedido"%>
<%@page import="com.tsp.sct.bo.EstatusComprobanteBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.dao.dto.DatosUsuario"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.dao.dto.ComprobanteFiscal"%>
<%@page import="com.tsp.sct.bo.ComprobanteFiscalBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    //String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String buscar_folio = request.getParameter("q_folio")!=null? new String(request.getParameter("q_folio").getBytes("ISO-8859-1"),"UTF-8") :"";
    String buscar_idcliente = request.getParameter("q_idcliente")!=null?request.getParameter("q_idcliente"):"";
    String buscar_idestatus = request.getParameter("q_idestatus")!=null?request.getParameter("q_idestatus"):"";
    String buscar_idsucursal = request.getParameter("q_idsucursal")!=null?request.getParameter("q_idsucursal"):"";
    String getQfechamin = request.getParameter("q_fh_min")!=null?request.getParameter("q_fh_min"):"";
    String getQfechamax = request.getParameter("q_fh_max")!=null?request.getParameter("q_fh_max"):"";
    String buscar_fechamin = "";
    String buscar_fechamax = "";
    Date fechaMin=null;
    Date fechaMax=null;
    String strWhereRangoFechas_factura="";
    String strWhereRangoFechas_pedido="";
    
    {
        try{
            fechaMin = new SimpleDateFormat("dd/MM/yyyy").parse(getQfechamin);
            buscar_fechamin = DateManage.formatDateToSQL(fechaMin);
        }catch(Exception e){}
        try{
            fechaMax = new SimpleDateFormat("dd/MM/yyyy").parse(getQfechamax);
            buscar_fechamax = DateManage.formatDateToSQL(fechaMax);
        }catch(Exception e){}
        
        if (fechaMin==null && fechaMax==null){
            //Si no se establecio ningun filtro de fechas específicas,
            // usamos por defecto, los ultimos 90 dias
            fechaMax = new Date();
            fechaMin = DateManage.sumaRestaDias(fechaMax, -90);
            buscar_fechamin = DateManage.formatDateToSQL(fechaMin);
            buscar_fechamax = DateManage.formatDateToSQL(fechaMax);
            getQfechamin = DateManage.formatDateToNormal(fechaMin);
            getQfechamax = DateManage.formatDateToNormal(fechaMax);
        }

    }
    
    String filtroBusqueda_factura = "";
    {
        
        /*Filtro por rango de fechas*/
        if (fechaMin!=null && fechaMax!=null){
            strWhereRangoFechas_factura="(CAST(FECHA_CAPTURA AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')";
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas_factura="(FECHA_CAPTURA  >= '"+buscar_fechamin+"')";
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas_factura="(FECHA_CAPTURA  <= '"+buscar_fechamax+"')";
        }
        
        //Solo facturas que no tengan relacion a pedidos
        filtroBusqueda_factura += " AND id_comprobante_fiscal NOT IN (SELECT id_comprobante_fiscal FROM sgfens_pedido WHERE id_comprobante_fiscal>0) ";
        
        //Solo mostraremos Facturas con Estatus 3 (entregada) y 4 (cancelada), 5 (cancelacion)
        //filtroBusqueda_factura = " AND (ID_ESTATUS='3' OR ID_ESTATUS ='4' OR ID_ESTATUS ='5')";
        filtroBusqueda_factura += " AND (ID_ESTATUS='3' OR ID_ESTATUS ='4')";

        //Ademas solo Facturas de Tipo FACTURA
        filtroBusqueda_factura += " AND (ID_TIPO_COMPROBANTE='2' OR ID_TIPO_COMPROBANTE='38' OR ID_TIPO_COMPROBANTE='41') ";

        if (!buscar_idestatus.trim().equals("")){
            if (buscar_idestatus.trim().equals("1")){
                //Activa
                filtroBusqueda_factura += " AND (ID_ESTATUS='3' AND (IMPORTE_NETO-SALDO_PAGADO)>0) ";
            }
            if (buscar_idestatus.trim().equals("2")){
                //Cancelada
                filtroBusqueda_factura += " AND (ID_ESTATUS=4 OR ID_ESTATUS=5) ";
            }
            if (buscar_idestatus.trim().equals("3")){
                //Pagada
                filtroBusqueda_factura += " AND (ID_ESTATUS='3' AND (IMPORTE_NETO-SALDO_PAGADO)<=0) ";
            }
        }

        if (!buscar_folio.trim().equals("")){
            filtroBusqueda_factura += " AND (FOLIO_GENERADO LIKE '%" + buscar_folio +"%') ";
        }

        if (!buscar_idcliente.trim().equals("")){
            filtroBusqueda_factura += " AND ID_CLIENTE='" + buscar_idcliente +"' ";
        }

        if (!buscar_idsucursal.trim().equals("")){
            filtroBusqueda_factura += " AND ID_EMPRESA='" + buscar_idsucursal +"' ";
        }

        if (!strWhereRangoFechas_factura.trim().equals("")){
            filtroBusqueda_factura += " AND " + strWhereRangoFechas_factura;
        }
    }
    
    String filtroBusqueda_pedido = "";
    {
        /*Filtro por rango de fechas*/
        if (fechaMin!=null && fechaMax!=null){
            strWhereRangoFechas_pedido="(CAST(FECHA_PEDIDO AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"' )";
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas_pedido="(FECHA_PEDIDO  >= '"+buscar_fechamin+"')";
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas_pedido="(FECHA_PEDIDO  <= '"+buscar_fechamax+"')";
        }
        
        if (!buscar_idestatus.trim().equals("")){
            if (buscar_idestatus.trim().equals("1")){
                //Activa
                filtroBusqueda_pedido += " AND (ID_ESTATUS_PEDIDO!=3 AND (TOTAL + ADELANTO - SALDO_PAGADO)>0) ";
            }
            if (buscar_idestatus.trim().equals("2")){
                //Cancelada
                filtroBusqueda_pedido += " AND (ID_ESTATUS_PEDIDO=3) ";
            }
            if (buscar_idestatus.trim().equals("3")){
                //Pagada
                filtroBusqueda_pedido += " AND (ID_ESTATUS_PEDIDO!=3 AND (TOTAL + ADELANTO - SALDO_PAGADO)<=0) ";
            }
        }

        if (!buscar_folio.trim().equals("")){
            filtroBusqueda_pedido += " AND (FOLIO_PEDIDO LIKE '%" + buscar_folio +"%') ";
        }

        if (!buscar_idcliente.trim().equals("")){
            filtroBusqueda_pedido += " AND ID_CLIENTE='" + buscar_idcliente +"' ";
        }

        if (!buscar_idsucursal.trim().equals("")){
            filtroBusqueda_pedido += " AND ID_EMPRESA='" + buscar_idsucursal +"' ";
        }

        if (!strWhereRangoFechas_pedido.trim().equals("")){
            filtroBusqueda_pedido += " AND " + strWhereRangoFechas_pedido;
        }
    }
       
    
    
    int idComprobanteFiscal = -1;
    try{ idComprobanteFiscal = Integer.parseInt(request.getParameter("idComprobanteFiscal")); }catch(NumberFormatException e){}
    int idPedido = -1;
    try{ idPedido = Integer.parseInt(request.getParameter("idPedido")); }catch(NumberFormatException e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
    EmpresaBO empresaBO = new EmpresaBO(user.getConn());
    
    NumberFormat formatMoneda = new DecimalFormat("$ ###,###,###,##0.00");
    ComprobanteFiscalBO comprobanteFiscalBO = new ComprobanteFiscalBO(user.getConn());
    SGPedidoBO pedidoBO = new SGPedidoBO(user.getConn());
    double totalPendientePorCobrar = 0;
    double pendientePorCobrarFacturas = 0;
    double pendientePorCobrarPedidos = 0;
    try{
        String strPendienteFacturas = comprobanteFiscalBO.findGroupValorUnicoComprobanteFiscal(idComprobanteFiscal, idEmpresa , 0, 0, filtroBusqueda_factura,
                " SUM(IMPORTE_NETO - SALDO_PAGADO) AS saldo ");
        if (StringManage.getValidString(strPendienteFacturas).length()>0)
                try{ pendientePorCobrarFacturas =  Double.parseDouble(strPendienteFacturas); }catch(Exception ex){}
        
        String strPendientePedidos = pedidoBO.findGroupValorUnicoPedido(idPedido, idEmpresa , 0, 0, filtroBusqueda_pedido,
                " SUM(TOTAL + ADELANTO - SALDO_PAGADO) AS saldo ");
        if (StringManage.getValidString(strPendientePedidos).length()>0)
                try{ pendientePorCobrarPedidos =  Double.parseDouble(strPendientePedidos); }catch(Exception ex){}

    }catch(Exception ex){
        ex.printStackTrace();
    }
    totalPendientePorCobrar = pendientePorCobrarFacturas + pendientePorCobrarPedidos;
    if (totalPendientePorCobrar<0)
        totalPendientePorCobrar = 0;
    String totalPendientePorCobrarStr = formatMoneda.format(totalPendientePorCobrar);
     
     /*
    * Datos de catálogo
    */
    String parametrosPaginacion="q_idcliente="+buscar_idcliente+"&q_idestatus="+buscar_idestatus+"&q_idsucursal="+buscar_idsucursal+"&q_fh_min="+getQfechamin+"&q_fh_max="+getQfechamax+"&q_folio="+buscar_folio;
    
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
                    <h1>Cuentas por Cobrar</h1>
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
                            <form action="cxc_comprobantes_list.jsp" id="search_form_advance" name="search_form_advance" method="post">
                                <p>
                                    Por Fecha de Emision <br/>
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
                                    <label>Folio:</label>
                                    <input type="text" style="width: 100px;" id="q_folio" name="q_folio"/>
                                </p>
                                <br/>
                                
                                <p>
                                <label>Cliente:</label><br/>
                                <select id="q_idcliente" name="q_idcliente" class="flexselect" style="width: 300px;">
                                    <option></option>
                                    <%= new ClienteBO(user.getConn()).getClientesByIdHTMLCombo(idEmpresa, -1," AND ID_ESTATUS=1 " + (user.getUser().getIdRoles() == RolesBO.ROL_OPERADOR? " AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM SGFENS_CLIENTE_VENDEDOR WHERE ID_USUARIO_VENDEDOR="+user.getUser().getIdUsuarios()+")" : "") ) %>
                                </select>
                                </p>
                                <br/>
                                
                                <p>
                                <label>Sucursal</label><br/>
                                <select id="q_idsucursal" name="q_idsucursal" class="flexselect" style="width: 300px;">
                                    <option></option>
                                    <%= empresaBO.getEmpresasByIdHTMLCombo(idEmpresa, -1 ) %>
                                </select>
                                </p>
                                <br/>
                                
                                <p>
                                <label>Estatus:</label><br/>
                                <select id="q_idestatus" name="q_idestatus">
                                    <option></option>
                                    <option value="1">Activa</option>
                                    <option value="2">Cancelada</option>
                                    <option value="3">Pagada</option>
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
                                Finanzas
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <span style="font-size: 16px;">
                                Total Pendientes por Cobrar (Filtro): 
                                <label><%= totalPendientePorCobrarStr %></label>
                                <br/>
                                Período consultado: <label><%= getQfechamin %> - <%= getQfechamax %></label>
                            </span>
                            <br/>
                        </div>
                      </div>
                      <!-- End right column window -->
                    </div>
                    <br class="clear"/>
                    <br class="clear"/>
                    
                    <!-- Facturas -->
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_cxc.png" alt="icon"/>
                                CxC Comprobantes Fiscales (Facturas)
                            </span>                            
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <iframe id="frame_facturas" src="cxc_frame_facturas_list.jsp?<%= parametrosPaginacion %>" 
                                    height="300" width="100%" 
                                    style="border:0;">        
                                <p>Tu navegador no acepta el uso de iframes.</p>
                            </iframe>
                        </div>
                    </div>
                    <!-- Fin Facturas-->
                    
                    <!-- Pedidos -->
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_cxc.png" alt="icon"/>
                                CxC Pedidos
                            </span>                            
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <iframe id="frame_facturas" src="cxc_frame_pedidos_list.jsp?<%= parametrosPaginacion %>" 
                                    height="300" width="100%" 
                                    style="border:0;">        
                                <p>Tu navegador no acepta el uso de iframes.</p>
                            </iframe>
                        </div>
                    </div>
                    <!-- Fin Pedidos-->
                    
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