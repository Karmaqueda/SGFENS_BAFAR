<%-- 
    Document   : catCronometro
    Created on : 5/01/2016, 01:11:21 PM
    Author     : HpPyme
--%>

<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.dao.dto.Cronometro"%>
<%@page import="com.tsp.sct.bo.CronometroBO"%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.DatosUsuario"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.dao.dto.CatalogoGastos"%>
<%@page import="com.tsp.sct.bo.CatalogoGastosBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.tsp.sct.dao.dto.GastosEvc"%>
<%@page import="com.tsp.sct.bo.GastosEvcBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
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
    String buscar_idvendedor = request.getParameter("q_idvendedor")!=null?request.getParameter("q_idvendedor"):"";
    String buscar_idcliente = request.getParameter("q_idcliente")!=null?request.getParameter("q_idcliente"):"";
    
    String filtroBusqueda = "";   
    
    if (!buscar.trim().equals("")){
        filtroBusqueda += " AND (ID_EMPLEADO IN (SELECT ID_EMPLEADO FROM EMPLEADO WHERE NOMBRE LIKE '%" + buscar + 
                            "%' OR APELLIDO_PATERNO LIKE '%" + buscar +
                            "%' OR APELLIDO_MATERNO LIKE '%" +buscar + "%' )) ";
    
    }
    
    
    /* //Si se integra desde list Empleados
    int idEmpleado = -1;
    try{ idEmpleado = Integer.parseInt(request.getParameter("idEmpleado")); }catch(NumberFormatException e){}    
     */
    
    int idCronometro = -1;
    try{ idCronometro = Integer.parseInt(request.getParameter("idCronometro")); }catch(NumberFormatException e){}
    
    
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
            strWhereRangoFechas="(CAST(FECHA_INICIO AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax)+"&q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas="(CAST(FECHA_INICIO AS DATE)  >= '"+buscar_fechamin+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas="(CAST(FECHA_INICIO AS DATE)  <= '"+buscar_fechamax+"')";
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
    
    /*if (idEmpleado > 0){ //Si se integra desde list Empleados
               
        filtroBusqueda += " AND ID_EMPLEADO=" + idEmpleado + " ";
        
      if(!parametrosPaginacion.equals(""))
                parametrosPaginacion+="&";
        parametrosPaginacion+="idEmpleado="+idEmpleado;
        
    }else*/ 
    if (!buscar_idvendedor.trim().equals("")){
        
         empleadoDto = new EmpleadoBO(user.getConn()).findEmpleadoByUsuario(Integer.parseInt(buscar_idvendedor));             
        
        filtroBusqueda += " AND ID_EMPLEADO=" + empleadoDto.getIdEmpleado() +" ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q_idvendedor="+buscar_idvendedor;
    }
        
    
    if (!buscar_idcliente.trim().equals("")){
        filtroBusqueda += " AND ID_CLIENTE='" + buscar_idcliente +"' ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="buscar_idcliente="+buscar_idcliente;
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
    
     CronometroBO cronometroBO = new CronometroBO(user.getConn());
     Cronometro[] cronometroDto = new Cronometro[0];
     try{
         limiteRegistros = cronometroBO.findCronometros(idCronometro,idEmpresa,0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        cronometroDto = cronometroBO.findCronometros(idCronometro,idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
  
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");    
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss" );
    DecimalFormat decimales = new DecimalFormat("0.00");  
    
    String urlTo = "../catCronometro/catCronometro_list.jsp";
    String urlTo2 = "catCronometro_localizacion.jsp";
    String param ="idCronometro";
    
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
                        //changeYear:true,
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
            
            
            function recargar(pag){
                location.href = "catEmpleados_Gastos_list.jsp?pagina="+pag;
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
                    <h1>Pretoriano Móvil</h1>
                    
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
                            <form action="catCronometro_list.jsp" id="search_form_advance" name="search_form_advance" method="post">
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
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo((int)idEmpresa, RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL, 0) %>                         
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo((int)idEmpresa, RolesBO.ROL_CONDUCTOR_VENDEDOR, 0) %>
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo((int)idEmpresa, RolesBO.ROL_COBRADOR, 0) %>
                                </select>
                                </p>   
                                <br/>
                                <% } %>   
                                
                                <p>
                                <label>Cliente:</label><br/>
                                <select id="q_idcliente" name="q_idcliente" class="flexselect">
                                    <option></option>
                                    <%= new ClienteBO(user.getConn()).getClientesByIdHTMLCombo(idEmpresa, -1," AND ID_ESTATUS<>2 " + (user.getUser().getIdRoles() == RolesBO.ROL_CONDUCTOR_VENDEDOR? " AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM SGFENS_CLIENTE_VENDEDOR WHERE ID_USUARIO_VENDEDOR="+user.getUser().getIdUsuarios()+")" : "") ) %>
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
                                <img src="../../images/cronometro_16.png" alt="icon"/> 
                                Cronómetro
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                            <tr>
                                                <td>
                                                    <div id="search">
                                                        <form action="catCronometro_list.jsp" id="search_form" name="search_form" method="get">
                                                            
                                                            <input type="text" id="q" name="q" title="Buscar por Empleado" class="" style="width: 300px; float: left; "
                                                                       value="<%=buscar%>"/>
                                                            <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>    
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
                                            <th>ID</th>
                                            <th>Empleado</th>
                                            <th>Cliente</th>
                                            <th>Inicio</th>
                                            <th>Fin</th>
                                            <th>Tiempo (min)</th>  
                                            <th>Acciones</th>                                           
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%          
                                            
                                           
                                            long totalMinutos = 0;
                                            for(Cronometro item : cronometroDto){
                                                
                                                
                                                try{          
                                                    
                                                    
                                                    
                                                    empleadoBO = new EmpleadoBO(item.getIdEmpleado(),user.getConn());
                                                    empleadoDto = empleadoBO.getEmpleado();
                                                    
                                                    Cliente clienteDto = new ClienteBO(item.getIdCliente(),user.getConn()).getCliente();
                                                  
                                                    String nombreEmpleado = "";
                                                    String clienteStr ="";
                                                    
                                                    if (clienteDto!=null){
                                                        clienteStr = clienteDto.getRazonSocial();
                                                    }
                                                        
                                                    if(empleadoDto!=null){
                                                        nombreEmpleado += empleadoDto.getNombre()!=null?empleadoDto.getNombre():"";
                                                        nombreEmpleado += " " + (empleadoDto.getApellidoPaterno()!=null?empleadoDto.getApellidoPaterno():"");
                                                        nombreEmpleado += " " + (empleadoDto.getApellidoMaterno()!=null?empleadoDto.getApellidoMaterno():"");
                                                    }
                                                    
                                                //Tiempo cronometrado en min
                                                long ini = item.getFechaInicio().getTime();
                                                long fin = item.getFechaFin().getTime(); 

                                                long diferencia = (fin - ini)/(1000*60);    
                                                 
                                                totalMinutos+= diferencia;     

                                        %>
                                        <tr>                         
                                            <td><%=item.getIdCronometro()%></td>   
                                            <td><%=nombreEmpleado%></td>   
                                            <td><%=clienteStr%></td>   
                                            <td><%=format.format(item.getFechaInicio())%></td>                                                                                                                             
                                            <td><%=format.format(item.getFechaFin())%></td> 
                                            <td><%=diferencia%></td>      
                                            <td>
                                                <a href="<%=urlTo2%>?<%=param%>=<%=item.getIdCronometro()%>"><img src="../../images/icon_movimiento.png" alt="localización" class="help" title="Localización"/></a>                                                
                                            </td>                                              
                                        </tr>
                                        <%      }catch(Exception ex){
                                                    ex.printStackTrace();
                                                }
                                            } 
                                        %>
                                        <tr>                                            
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td><b>Min Totales:</b></td>
                                            <td style="color: #0000cc; text-align: left;"><%=totalMinutos%></td>
                                            <td></td> 
                                        </tr>                                        
                                    </tbody>
                                </table>
                            </form>
                            <br/>
                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                            
                            <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <!--<jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <jsp:param name="idReport" value="<%= ReportBO.CRONOMETRO_REPORT %>" />
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