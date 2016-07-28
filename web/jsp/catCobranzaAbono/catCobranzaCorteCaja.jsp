<%-- 
    Document   : catEmpleados_list
    Created on : 9/01/2013, 11:12:43 AM
    Author     : Leonardo Montes de Oca, leonarzeta@hotmail.com
--%>

<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.tsp.sct.dao.dto.SgfensCobranzaAbono"%>
<%@page import="com.tsp.sct.bo.SGCobranzaAbonoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedido"%>
<%@page import="com.tsp.sct.bo.SGPedidoBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.Ruta"%>
<%@page import="com.tsp.sct.dao.jdbc.RutaDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Geocerca"%>
<%@page import="com.tsp.sct.bo.GeocercaBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="java.util.Date"%>
<!--------------------<//%@page import="com.tsp.sgfens.report.ReportBO"%>-->
<!--------------------<//%@page import="com.tsp.microfinancieras.bo.RolesBO"%>-->
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {

    int idEmpresa = user.getUser().getIdEmpresa();
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String buscar_idcliente = request.getParameter("q_idcliente")!=null?request.getParameter("q_idcliente"):"";
    String buscar_idvendedor = request.getParameter("q_idvendedor")!=null?request.getParameter("q_idvendedor"):"";    
    
    int idEmpleado = -1;
    try{ idEmpleado = Integer.parseInt(request.getParameter("idEmpleado")); }catch(NumberFormatException e){}
    
    
    String buscar_fechamin = "";
    String buscar_fechamax = "";
    Date fechaMin=null;
    Date fechaMax=null;
    String strWhereRangoFechas= "";
    String strWhereRangoFechasAbonos=  "";
    String filtroBusqueda = "";
    String parametrosPaginacion="";
    
    String fechaMinLink = "";
    String fechaMaxLink = "";
    
    
    try{
        fechaMin = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("q_fh_min"));
        buscar_fechamin = DateManage.formatDateToSQL(fechaMin);        
        fechaMinLink = request.getParameter("q_fh_min");
    }catch(Exception e){}    
    
    try{
        fechaMax = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("q_fh_max"));
        buscar_fechamax = DateManage.formatDateToSQL(fechaMax);   
        fechaMaxLink = request.getParameter("q_fh_max");
    }catch(Exception e){}

    /*Filtro por rango de fechas*/
    if (fechaMin!=null && fechaMax!=null){
        strWhereRangoFechas="(CAST(FECHA_PEDIDO AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')";
        strWhereRangoFechasAbonos ="(CAST(FECHA_ABONO AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')";
        if(!parametrosPaginacion.equals(""))
                parametrosPaginacion+="&";
        parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax)+"&q_fh_min="+DateManage.formatDateToNormal(fechaMin);
    }
    if (fechaMin!=null && fechaMax==null){
        strWhereRangoFechas="(FECHA_PEDIDO  >= '"+buscar_fechamin+"')";
        strWhereRangoFechasAbonos="(FECHA_ABONO  >= '"+buscar_fechamin+"')";
        if(!parametrosPaginacion.equals(""))
                parametrosPaginacion+="&";
        parametrosPaginacion+="q_fh_min="+DateManage.formatDateToNormal(fechaMin);
    }
    if (fechaMin==null && fechaMax!=null){
        strWhereRangoFechas="(FECHA_PEDIDO  <= '"+buscar_fechamax+"')";
        strWhereRangoFechasAbonos="(FECHA_ABONO  <= '"+buscar_fechamax+"')";
        if(!parametrosPaginacion.equals(""))
                parametrosPaginacion+="&";
        parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax);
    }
    
    
    if (!buscar_idvendedor.trim().equals("")){
        filtroBusqueda += " AND ID_USUARIOS='" + buscar_idvendedor +"' ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q_idvendedor="+buscar_idvendedor;
    }
        
    
    if (strWhereRangoFechas.trim().equals("")){//Si no selecciona fechas obetenemos datos de hoy
        Date  now = new Date();         
        strWhereRangoFechas="(CAST(FECHA_PEDIDO AS DATE) BETWEEN '"+ DateManage.formatDateToSQL(now)+"' AND '"+DateManage.formatDateToSQL(now)+"')";
        strWhereRangoFechasAbonos ="(CAST(FECHA_ABONO AS DATE) BETWEEN '"+ DateManage.formatDateToSQL(now)+"' AND '"+DateManage.formatDateToSQL(now)+"')";
        
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        fechaMinLink = df.format(now) ;
        fechaMaxLink = df.format(now) ;
    }
    
    strWhereRangoFechas += " AND (ID_ESTATUS_PEDIDO = 1 OR ID_ESTATUS_PEDIDO = 2) ";
    strWhereRangoFechasAbonos += " AND ID_ESTATUS = 1 ";
    
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
    
     EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
     Empleado[] empleadosDto = new Empleado[0];
     try{
         limiteRegistros = empleadoBO.findEmpleadosActivos(idEmpleado, idEmpresa , 0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        empleadosDto = empleadoBO.findEmpleadosActivos(idEmpleado, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
    
     
     
     /*
     *  Totales pie pag
     *  Obetenemos todos los registros
     */
     
      double totalVendido = 0;
      double totalCobrado = 0;
      double totalAdeudo = 0;
     try{             
        Empleado[] empleadosDto2 = empleadoBO.findEmpleadosActivos(idEmpleado, idEmpresa, 0,(int)(long)limiteRegistros ,filtroBusqueda);  
        
        for (Empleado item:empleadosDto2){
            SGPedidoBO pedidoBO= new SGPedidoBO(user.getConn());
            SgfensPedido[] pedidosDts = pedidoBO.findPedido(-1,(int)(long)idEmpresa , -1, -1 , " AND ID_USUARIO_VENDEDOR ="+item.getIdUsuarios()+ " AND " + strWhereRangoFechas );        
            
             
            
                        
            if(pedidosDts!=null){

                for(SgfensPedido pedido : pedidosDts ){
                    
                    totalVendido += pedido.getTotal();
                    
                    if(pedido.getBonificacionDevolucion()<0){
                        totalVendido += Math.abs(pedido.getBonificacionDevolucion());
                    }

                }
            }

            SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(user.getConn());
            SgfensCobranzaAbono[] abonosDts = cobranzaAbonoBO.findCobranzaAbono(-1, idEmpresa, -1, -1, " AND ID_USUARIO_VENDEDOR ="+item.getIdUsuarios() + " AND " + strWhereRangoFechasAbonos);

            if(abonosDts!=null){
                for(SgfensCobranzaAbono abono : abonosDts ){
                    totalCobrado += abono.getMontoAbono();
                }                                                                
            }


            
            
            System.out.println("+++++++++++++++ total : " + totalAdeudo);
        }   
        
        
        totalAdeudo += (totalVendido - totalCobrado);
		
        if(totalAdeudo<0)
                totalAdeudo =0;
				
     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../pedido/pedido_list.jsp";
    String urlTo2 = "../catCobranzaAbono/catCobranzaAbono_list.jsp";
    String urlTo3 = "../catConceptos/catConceptosVendidos_list.jsp";    
    String paramName = "q_idvendedor";
    String paramName2 = "q_fh_min";
    String paramName3 = "q_fh_max";
    //String parametrosPaginacion="";// "idEmpresa="+idEmpresa;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda , "UTF-8");
    String parametrosExtraEncoded = java.net.URLEncoder.encode(strWhereRangoFechas , "UTF-8");
    NumberFormat formatMoneda = new DecimalFormat("$ ###,###,###,##0.00");
    
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <link rel="shortcut icon" href="../../images/favicon.ico">
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
                    <h1>Ventas</h1>
                    
                    
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                Busqueda Avanzada &dArr;
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content" style="display: none;">
                            <form action="catCobranzaCorteCaja.jsp" id="search_form_advance" name="search_form_advance" method="post">                               
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
                                
                                
                                <% if (user.getUser().getIdRoles() != RolesBO.ROL_OPERADOR){%>
                                <p>
                                <label>Vendedor:</label><br/>
                                <select id="q_idvendedor" name="q_idvendedor" class="flexselect">
                                    <option></option>                                     
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL, 0) %>
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR_VENDEDOR, 0) %>
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_OPERADOR, 0) %>
                                </select>
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
                    
                    
                    
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_users.png" alt="icon"/>
                                Ventas - Corte de Caja
                            </span>                            
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <form id="form_data" name="form_data" action="" method="post">
                                <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Número de Empleado</th>
                                            <th>Nombre</th> 
                                            <th>Total Vendido&nbsp;($)</th> 
                                            <th>Cobranza&nbsp;($)</th> 
                                            <th>Crédito&nbsp;($)</th>                                             
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                        for (Empleado item:empleadosDto){
                                            try{
                                                    
                                                    System.out.println("--------------pedidos------------");
                                                    SGPedidoBO pedidoBO= new SGPedidoBO(user.getConn());
                                                    SgfensPedido[] pedidosDts = pedidoBO.findPedido(-1,(int)(long)idEmpresa , -1, -1 , " AND ID_USUARIO_VENDEDOR ="+item.getIdUsuarios() + " AND " + strWhereRangoFechas );        

                                                    double totalVendidoProm = 0;
                                                    double totalCobradoProm = 0;
                                                    double totalAdeudoProm = 0;
                                                    
                                                    if(pedidosDts!=null){
                                                        
                                                        for(SgfensPedido pedido : pedidosDts ){
                                                            totalVendidoProm += pedido.getTotal();
                                                           
                                                        }
                                                    }
                                                    System.out.println("-------------abonos-----------");
                                                    SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(user.getConn());
                                                    SgfensCobranzaAbono[] abonosDts = cobranzaAbonoBO.findCobranzaAbono(-1, idEmpresa, -1, -1, " AND ID_USUARIO_VENDEDOR ="+item.getIdUsuarios() + " AND " + strWhereRangoFechasAbonos);

                                                    if(abonosDts!=null){
                                                        for(SgfensCobranzaAbono abono : abonosDts ){
                                                            totalCobradoProm += abono.getMontoAbono();
                                                        }                                                                
                                                    }
                                                    
                                                    
                                                    totalAdeudoProm += (totalVendidoProm - totalCobradoProm);
                                                    
                                                    if(totalAdeudoProm <0)
                                                        totalAdeudoProm = 0;
                                                    
                                                    String nombreCompleto = item.getNombre() + " " + item.getApellidoPaterno() + " " + item.getApellidoMaterno();

                                                %>


                                                    <tr <%=(item.getIdEstatus()==2)?"style='background: #B0B1B1'":""%> >
                                                        <!--<td><input type="checkbox"/></td>-->
                                                        <td><%=item.getIdEmpleado() %></td>
                                                        <td><%=item.getNumEmpleado()%></td>
                                                        <td><%=nombreCompleto%></td>                                            
                                                        <td><%=formatMoneda.format(totalVendidoProm)%></td>
                                                        <td><%=formatMoneda.format(totalCobradoProm)%></td>                                          
                                                        <td><%=formatMoneda.format(totalAdeudoProm)%></td> 
                                                        <td>                                               
                                                            <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdUsuarios()%>&<%=paramName2%>=<%=fechaMinLink%>&<%=paramName3%>=<%=fechaMaxLink%>"><img src="../../images/cart.png" alt="Detalle Pedidos" class="help" title="Detalle Pedidos"/></a>
                                                            &nbsp;&nbsp;                                                
                                                            <a href="<%=urlTo2%>?<%=paramName%>=<%=item.getIdUsuarios()%>&<%=paramName2%>=<%=fechaMinLink%>&<%=paramName3%>=<%=fechaMaxLink%>"><img src="../../images/icon_ventas1.png" alt="Detalle Cobros" class="help" title="Detalle Cobros"/></a>
                                                            &nbsp;&nbsp;
                                                            <a href="<%=urlTo3%>?<%=paramName%>=<%=item.getIdUsuarios()%>&<%=paramName2%>=<%=fechaMinLink%>&<%=paramName3%>=<%=fechaMaxLink%>"><img src="../../images/icon_producto.png" alt="Detalle Productos" class="help" title="Detalle Productos"/></a>
                                                            &nbsp;&nbsp;                                                
                                                        </td>
                                                    </tr>
                                        <%      }catch(Exception ex){
                                                    ex.printStackTrace();
                                                }
                                        } 
                                        %>
                                        
                                        <tr style="font-size: 14;">
                                            <td colspan="3" style="text-align: right;"><b>Totales:</b></td>
                                            <td style="text-align: left;color: #0000cc;"><%=formatMoneda.format(totalVendido)%></td>
                                            <td style="text-align: left;color: #0000cc;"><%=formatMoneda.format(totalCobrado)%></td>
                                            <td style="text-align: left;color: #0000cc;"><%=formatMoneda.format(totalAdeudo)%></td>                                            
                                            <td>&nbsp;</td>
                                        </tr>
                                        
                                        
                                    </tbody>
                                </table>
                            </form>
                                    
                            <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <jsp:param name="idReport" value="<%= ReportBO.EMPLEADO_CORTECAJA_REPORT %>" />
                                <jsp:param name="parametrosCustom" value="<%=filtroBusquedaEncoded %>" />
                                <jsp:param name="parametrosExtra" value="<%=parametrosExtraEncoded%>" />
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
