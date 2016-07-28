<%-- 
    Document   : catMovimientos_list
    Created on : 23/11/2012, 06:14:15 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.bo.AlmacenBO"%>
<%@page import="com.tsp.sct.dao.dto.Almacen"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sct.dao.dto.Movimiento"%>
<%@page import="com.tsp.sct.bo.MovimientoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String tipoMovimiento = request.getParameter("tipoMovimiento")!=null? new String(request.getParameter("tipoMovimiento").getBytes("ISO-8859-1"),"UTF-8") :"";
    String buscar_idvendedor = request.getParameter("q_idvendedor")!=null?request.getParameter("q_idvendedor"):"";
    
    
    String filtroBusqueda = "";
    String parametrosPaginacion = "";
    
    if (!buscar.trim().equals(""))
        filtroBusqueda += " AND (TIPO_MOVIMIENTO LIKE '%" + buscar + "%' OR NOMBRE_PRODUCTO LIKE '%" +buscar+"%' OR CONTABILIDAD LIKE '%" +buscar+"%')";
    
    
    if(!tipoMovimiento.trim().equals("")){
        filtroBusqueda += " AND (TIPO_MOVIMIENTO = '" + tipoMovimiento +  "')" ;
        
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="tipoMovimiento="+tipoMovimiento;
    }
    
    int idAlmacen = -1;
    try{ idAlmacen = Integer.parseInt(request.getParameter("idAlmacen")); }catch(NumberFormatException e){}
    
    if(idAlmacen > 0  ){
        filtroBusqueda += " AND (ID_ALMACEN = " + idAlmacen +  " OR ID_ALMACEN_DESTINO = " + idAlmacen +  " )" ;
        
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="idAlmacen="+idAlmacen;
    }
    
    int idMovimiento = -1;
    try{ idMovimiento = Integer.parseInt(request.getParameter("idMovimiento")); }catch(NumberFormatException e){}
    
    
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
            strWhereRangoFechas="(CAST(FECHA_REGISTRO AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax)+"&q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas="(CAST(FECHA_REGISTRO AS DATE)  >= '"+buscar_fechamin+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas="(CAST(FECHA_REGISTRO AS DATE)  <= '"+buscar_fechamax+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax);
        }
    }
    
    if (!strWhereRangoFechas.trim().equals("")){
        filtroBusqueda += " AND " + strWhereRangoFechas;
    }
    
    EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
    Empleado empleadoDto = null;
    if (!buscar_idvendedor.trim().equals("")){
        
         empleadoDto = new EmpleadoBO(user.getConn()).findEmpleadoByUsuario(Integer.parseInt(buscar_idvendedor));             
        
        filtroBusqueda += " AND idEmpleado =" + empleadoDto.getIdEmpleado() +" ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q_idvendedor="+buscar_idvendedor;
    }
    
        
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
    
     MovimientoBO movimientoBO = new MovimientoBO(user.getConn());
     Movimiento[] movimientosDto = new Movimiento[0];
     try{
         limiteRegistros = movimientoBO.findMovimientos(idMovimiento, idEmpresa , 0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        movimientosDto = movimientoBO.findMovimientos(idMovimiento, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catMovimientos/catMovimientos_form.jsp";
    String urlTo2 = "../catMovimientos/catMovimientos_form_barcode.jsp";
    String paramName = "idMovimiento";
    //String parametrosPaginacion="";// idEmpresa="+idEmpresa;
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
                    <h1>Catálogos</h1>
                    
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
                            <form action="catMovimientos_list.jsp" id="search_form_advance" name="search_form_advance" method="post">
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
                                <label>Tipo de Movimiento</label><br/>
                                <select id="tipoMovimiento" name="tipoMovimiento" class="flexselect">                                    
                                        <option value=""></option>
                                        <option value="ENTRADA">ENTRADA</option>
                                        <option value="SALIDA">SALIDA</option>
                                        <option value="TRASPASO">TRASPASO</option>
                                    
                                </select>
                                <br/><br/> 
                                <p>
                                    <label>Almacén:</label> <br/>
                                    <select size="1" id="idAlmacen" name="idAlmacen" class="flexselect" >
                                    <option value="-1"></option>
                                            <%
                                                out.print(new AlmacenBO(user.getConn()).getAlmacenesByIdHTMLCombo(idEmpresa, -1));
                                            %>
                                    </select>                                       
                                </p>
                                <br/>
                                <% if (user.getUser().getIdRoles() != RolesBO.ROL_OPERADOR){%>
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
                                <% } %> 
                                <br/><br/>
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
                                <img src="../../images/icon_movimiento.png" alt="icon"/>
                                Catálogo de Movimientos
                            </span>
                            <div class="switch" style="width:600px">
                                <table width="600px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td>
                                                <div id="search">
                                                <form action="catMovimientos_list.jsp" id="search_form" name="search_form" method="get">
                                                        <input type="text" id="q" name="q" title="Buscar por Tipo Movimiento / Nombre Producto / Cantidad" class="" style="width: 300px; float: left; "
                                                               value="<%=buscar%>"/>
                                                        <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                </form>
                                                </div>
                                            </td>
                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                            <%if(RolAutorizacionBO.permisoNuevoElemento(user.getUser().getIdRoles()) || (user.getUser().getIdRoles() == RolesBO.ROL_ALMACENISTA) ){%>
                                            <td>
                                                <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Crear Nuevo" 
                                                       style="float: right; width: 100px;" onclick="location.href='<%=urlTo%>'"/>
                                            </td>
                                            <td>
                                                <input type="button" id="nuevo_lectora" name="nuevo_lectora" class="right_switch" value="Nuevo (Lectora)" 
                                                       style="float: right; width: 100px;" onclick="location.href='<%=urlTo2%>'"/>
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
                                            <th>Fecha</th>
                                            <th>Tipo de Movimiento</th>
                                            <th>Nombre Producto</th>                                            
                                            <th>Cantidad del Movimiento</th>
                                            <th>Almacén Origen</th>
                                            <th>Almacén Destino</th>
                                            <th>Empleado</th>
                                            <th>Motivo</th>
                                            <!-- <th>Acciones</th> -->
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            empleadoBO = new EmpleadoBO(user.getConn());
                                            empleadoDto = null;
                                            String nombreEmpleado = "";
                                                            
                                            for (Movimiento item:movimientosDto){
                                                try{
                                                    nombreEmpleado = "";
                                                    if (item.getIdEmpleado() > 0){
                                                        empleadoBO = new EmpleadoBO(item.getIdEmpleado(),user.getConn());
                                                        empleadoDto = empleadoBO.getEmpleado();         
                                                        
                                                        nombreEmpleado = empleadoDto!=null?((empleadoDto.getNombre()!=null?empleadoDto.getNombre():"") 
                                                        + (empleadoDto.getApellidoPaterno()!=null?" " + empleadoDto.getApellidoPaterno():"") 
                                                        + (empleadoDto.getApellidoMaterno()!=null?" " + empleadoDto.getApellidoMaterno():"")):"";

                                                    }   
                                                    
                                        %>
                                        <tr>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdMovimiento() %></td>
                                            <td><%=item.getFechaRegistro() %></td>
                                            <td><%=item.getTipoMovimiento() %></td>
                                            <td><%=item.getNombreProducto() %></td>                                            
                                            <td><%=item.getContabilidad() %></td>
                                            <% Almacen almacenDto =  new AlmacenBO(item.getIdAlmacen(),user.getConn()).getAlmacen();
                                               Almacen almacenDtoDestino =  new AlmacenBO(item.getIdAlmacenDestino(),user.getConn()).getAlmacen();
                                            %>
                                            <td><%=almacenDto!=null?almacenDto.getNombre():""%></td>
                                            <td><%=almacenDtoDestino!=null?almacenDtoDestino.getNombre():""%></td>
                                            <td><%=nombreEmpleado%></td>
                                            <td><%=item.getConceptoMovimiento() %></td>
                                            <!-- <td>
                                                <a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdMovimiento()%>&acc=1"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>
                                                &nbsp;&nbsp;
                                                <!--<a href=""><img src="images/icon_delete.png" alt="delete" class="help" title="Delete"/></a>-->
                                           <!-- </td> -->
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
                                <jsp:param name="idReport" value="<%= ReportBO.PRODUCTO_MOVIMIENTOS %>" />
                                <jsp:param name="parametrosCustom" value="<%=filtroBusquedaEncoded %>" />                                
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