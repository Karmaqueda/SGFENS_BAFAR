<%-- 
    Document   : catEmpleado_CuentaEfectivo_list
    Created on : 24/07/2015, 05:04:07 PM
    Author     : HpPyme
--%>

<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.dao.dto.CuentaEfectivo"%>
<%@page import="com.tsp.sct.bo.CuentaEfectivoBO"%>
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
    
    String filtroBusqueda = "";   
    
    if (!buscar.trim().equals(""))
        filtroBusqueda = "";
    
    
    int idEmpleado = -1;
    try{ idEmpleado = Integer.parseInt(request.getParameter("idEmpleado")); }catch(NumberFormatException e){}    
    
    
    
    
    
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
            strWhereRangoFechas="(CAST(FECHA_HORA AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax)+"&q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas="(FECHA_HORA  >= '"+buscar_fechamin+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas="(FECHA_HORA  <= '"+buscar_fechamax+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax);
        }
    }
    
    if (!strWhereRangoFechas.trim().equals("")){
        filtroBusqueda += " AND " + strWhereRangoFechas;
    }
    
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    
    
    EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
    Empleado empleadoDto = null;
    if (idEmpleado > 0){
        empleadoBO = new EmpleadoBO(idEmpleado,user.getConn());
        empleadoDto = empleadoBO.getEmpleado();
        
      if(!parametrosPaginacion.equals(""))
                parametrosPaginacion+="&";
        parametrosPaginacion+="idEmpleado="+idEmpleado;
        
    }
    
    
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
    
     CuentaEfectivoBO cuentaEfectivoBO = new CuentaEfectivoBO(user.getConn());
     CuentaEfectivo[] cuentaEfectivoDto = new CuentaEfectivo[0];
     try{
         limiteRegistros = cuentaEfectivoBO.findCuentaEfectivo(idEmpleado,0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        cuentaEfectivoDto = cuentaEfectivoBO.findCuentaEfectivo(idEmpleado,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catEmpleados/catEmpleado_CuentaEfectivo_form.jsp";
    String paramName = "idEmpleado";
    parametrosPaginacion="idEmpleado="+idEmpleado;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm" );
    DecimalFormat decimales = new DecimalFormat("0.00");  
    
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
                    <h1>Cuenta de Dinero</h1>
                    
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
                            <form action="catEmpleado_CuentaEfectivo_list.jsp" id="search_form_advance" name="search_form_advance" method="post">
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
                                <img src="../../images/coins_icon_16.png" alt="icon"/>
                                Empleado: <%=empleadoDto!=null?((empleadoDto.getNombre()!=null?empleadoDto.getNombre():"") 
                                                        + (empleadoDto.getApellidoPaterno()!=null?" " + empleadoDto.getApellidoPaterno():"") 
                                                        + (empleadoDto.getApellidoMaterno()!=null?" " + empleadoDto.getApellidoMaterno():"")):""%>
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                            <tr>
                                                <td>
                                                    <div id="search">
                                                        <form action="catEmpleado_CuentaEfectivo_list.jsp?idEmpleado=<%=idEmpleado%>" id="search_form" name="search_form" method="get">
                                                            
                                                    </form>
                                                    </div>
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
                                            <th>Fecha y Hora</th>
                                            <th>Billetes</th>
                                            <th>Monedas</th>
                                            <th>Total</th>
                                            <th>Acciones</th>                                           
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                                  
                                            double totalBilletes = 0;
                                            double totalMonedas = 0;
                                            double montoTotal = 0;
                                            
                                            for (CuentaEfectivo item : cuentaEfectivoDto){
                                                try{
                                                    
                                                 totalBilletes = cuentaEfectivoBO.cuentaEfectivoTipo(item.getIdCuentaEfectivo(), "billetes");
                                                 totalMonedas = cuentaEfectivoBO.cuentaEfectivoTipo(item.getIdCuentaEfectivo(), "monedas");
                                                 montoTotal = cuentaEfectivoBO.cuentaEfectivoTipo(item.getIdCuentaEfectivo(), "total");    
                                                    
                                        %>
                                        <tr>                                           
                                            <td><%=format.format(item.getFechaHora())%></td>                                                                                       
                                            <td><%=decimales.format(totalBilletes)%></td>  
                                            <td><%=decimales.format(totalMonedas)%></td>  
                                            <td><%=decimales.format(montoTotal)%></td> 
                                            <td><a href="catEmpleado_CuentaEfectivo_form.jsp?idCuentaEfectivo=<%=item.getIdCuentaEfectivo()%>"><img src="../../images/icon_consultar.png" alt="Consultar" class="help" title="Consultar" /></a></td>                                              
                                        </tr>
                                        <%      }catch(Exception ex){
                                                    ex.printStackTrace();
                                                }
                                            } 
                                        %>
                                    </tbody>
                                </table>
                            </form>
                            <br/>
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
        </script>    
    </body>
</html>
<%}%>