<%-- 
    Document   : catComodatosContrato_list
    Created on : 1/04/2016, 12:32:41 PM
    Author     : leonardo
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.dao.jdbc.ClienteDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.dao.jdbc.AlmacenDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Almacen"%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.Comodato"%>
<%@page import="com.tsp.sct.bo.ComodatoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String filtroBusqueda = " AND ESTATUS != 3 AND CONTRATO_NOMBRE_ARCHIVO IS NOT NULL ";
    if (!buscar.trim().equals(""))
        filtroBusqueda = " AND (NOMBRE LIKE '%" + buscar + "%' OR NUMERO_SERIE LIKE '%" +buscar+"%' OR MARCA LIKE '%" +buscar+"%')";
    
    int idComodato = -1;
    try{ idComodato = Integer.parseInt(request.getParameter("idComodato")); }catch(NumberFormatException e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
        
    ////////// busqueda avanzada
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
            strWhereRangoFechas="(CAST(FECHA_ASIGNACION_CLIENTE AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+" 23:59:59')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax)+"&q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas="(FECHA_ASIGNACION_CLIENTE  >= '"+buscar_fechamin+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas="(FECHA_ASIGNACION_CLIENTE  <= '"+buscar_fechamax+" 23:59:59')";
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
            strWhereRangoFechas2="(CAST(FECHA_SUBIDA_CONTRATO AS DATE) BETWEEN '"+buscar_fechamin2+"' AND '"+buscar_fechamax2+" 23:59:59')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max2="+DateManage.formatDateToNormal(fechaMax2)+"&q_fh_min2="+DateManage.formatDateToNormal(fechaMin2);
        }
        if (fechaMin2!=null && fechaMax2==null){
            strWhereRangoFechas2="(FECHA_SUBIDA_CONTRATO  >= '"+buscar_fechamin2+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_min2="+DateManage.formatDateToNormal(fechaMin2);
        }
        if (fechaMin2==null && fechaMax2!=null){
            strWhereRangoFechas2="(FECHA_SUBIDA_CONTRATO  <= '"+buscar_fechamax2+" 23:59:59')";
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
    ////////// fin busqueda avanzada
    
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
    
     ComodatoBO comodatoBO = new ComodatoBO(user.getConn());
     Comodato[] comodatosDto = new Comodato[0];
     try{
         limiteRegistros = comodatoBO.findComodatos(idComodato, idEmpresa , 0, 0, filtroBusqueda).length;
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        comodatosDto = comodatoBO.findComodatos(idComodato, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catComodatos/catComodatos_form.jsp";
    String urlTo2 = "../catComodatos/catComodatosAsignarCliente_form.jsp";
    String urlTo3 = "../catComodatos/catComodatosMantenimiento_list.jsp";
    String urlTo4 = "../catComodatos/catComodatosContrato_form.jsp";
    String urlTo5 = "../catComodatos/catComodatosProductos_list.jsp";
    String urlTo6 = "../catComodatos/catComodatosInformacion_form.jsp";
    
    String paramName = "idComodato";
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
            
            function confirmarEliminarComodato(idComodato){
                apprise('¿Esta seguro que desea eliminar el Comodato?', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
                    function(r){
                        if(r){
                            // Usuario dio click 'Yes'
                            confirmarEliminar(idComodato);
                        }
                });
                
            }
            
            function confirmarEliminar(idComodato){
                if(idComodato>=0){
                    $.ajax({
                        type: "POST",
                        url: "catComodatos_ajax.jsp",
                        data: { mode: 'eliminarComodato', idComodato : idComodato },
                        beforeSend: function(objeto){
                            //$("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style="">ESPERE, procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                    function(r){
                                        location.href = "catComodatos_list.jsp";
                                });
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               //$("#ajax_message").html(datos);
                               //$("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                           }
                        }
                    });
                }
            }
            
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
                    <h1>Comodato</h1>
                    
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
                            <form action="catComodatosContrato_list.jsp" id="search_form_advance" name="search_form_advance" method="post">
                                <p>
                                    Por Fecha de Asignación &raquo;&nbsp;&nbsp;
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
                                    Por Fecha de Subida de Contrato &raquo;&nbsp;&nbsp;
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
                                <img src="../../images/icon_comodato.png" alt="icon"/>
                                Comodatos
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
                                                            <td>
                                                                <div id="search">
                                                                <!--<form action="catComodatos_list.jsp" id="search_form" name="search_form" method="get">
                                                                        <input type="text" id="q" name="q" title="Buscar por Nombre/Serial/Marca" class="" style="width: 300px; float: left; "
                                                                               value="<%=buscar%>"/>
                                                                        <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                                </form>-->
                                                                </div>
                                                            </td>
                                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                                            
                                                            <!--<td>
                                                                <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Crear Nuevo" 
                                                                       style="float: right; width: 100px;" onclick="location.href='<%=urlTo%>'"/>
                                                            </td>-->
                                                            
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
                                            <th>Cliente</th>
                                            <th>Comodato</th>
                                            <th>Fecha Asignación</th>
                                            <th>Estatus</th>                                           
                                            <th>Fecha Subida Contrato</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            Cliente cliente = null;
                                            ClienteDaoImpl clienteDaoImpl = new ClienteDaoImpl(user.getConn());
                                            String nombreCliente = "";
                                            for (Comodato item:comodatosDto){
                                                cliente = null;
                                                nombreCliente = "";
                                                try{
                                                    cliente = clienteDaoImpl.findByPrimaryKey(item.getIdCliente());
                                                    nombreCliente = cliente.getRazonSocial();
                                                }catch(Exception e){}
                                                try{
                                        %>
                                        <tr>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdComodato() %></td>
                                            <td><%=nombreCliente %></td>
                                            <td><%=item.getNombre() %></td>
                                            <td><%=item.getFechaAsignacionCliente()!=null?DateManage.formatDateToNormal(item.getFechaAsignacionCliente()):"" %></td>
                                            <td><%=item.getEstatus()==1?"Comodato":item.getEstatus()==2?"Disponible":item.getEstatus()==3?"Inactivo":item.getEstatus()==4?"Descompuesto":"No Definido"%></td>
                                            <td><%=item.getFechaSubidaContrato()!=null?DateManage.formatDateToNormal(item.getFechaSubidaContrato()):"" %></td>
                                            <td>
                                                <%if(RolAutorizacionBO.permisoAccionesElemento(user.getUser().getIdRoles())){%>
                                                <a href="<%=urlTo4%>?<%=paramName%>=<%=item.getIdComodato()%>&acc=contrato&pagina=<%=paginaActual%>"><img src="../../images/icon_comodatoContrato.png" alt="agregarContrato" class="help" title="Contrato"/></a>
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
                                <jsp:param name="idReport" value="<%= ReportBO.COMODATO_CONTRATO_REPORT %>" />
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
        </script>
    </body>
</html>
<%}%>