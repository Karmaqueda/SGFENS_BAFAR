<%-- 
    Document   : catCotizacions_list.jsp
    Created on : 256-oct-2012, 12:13:49
    Author     : ISCesarMartinez poseidon24@hotmail.com
--%>


<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.dao.dto.ClienteCategoria"%>
<%@page import="com.tsp.sct.bo.ClienteCategoriaBO"%>
<%@page import="com.tsp.sct.dao.jdbc.ClienteDaoImpl"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.SGProspectoBO"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProspecto"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.dao.dto.DatosUsuario"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.dao.dto.SgfensCotizacion"%>
<%@page import="com.tsp.sct.bo.SGCotizacionBO"%>
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
    int idClienteCategoria = -1;
    try{
        idClienteCategoria = Integer.parseInt(request.getParameter("idClienteCategoria"));
    }catch(NumberFormatException ex){}
    String buscar_idprospecto = request.getParameter("q_idprospecto")!=null?request.getParameter("q_idprospecto"):"";
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
            strWhereRangoFechas="(CAST(FECHA_COTIZACION AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')";
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas="(FECHA_COTIZACION  >= '"+buscar_fechamin+"')";
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas="(FECHA_COTIZACION  <= '"+buscar_fechamax+"')";
        }
    }
    
    String filtroBusqueda = "";
    
    //Si es vendedor, filtramos para solo mostrar sus cotizaciones
    if (user.getUser().getIdRoles() == RolesBO.ROL_OPERADOR)
        filtroBusqueda += " AND ID_USUARIO_VENDEDOR ='" + user.getUser().getIdUsuarios() + "' ";
    
    if (!buscar.trim().equals(""))
        filtroBusqueda = " AND FOLIO_COTIZACION LIKE '%" + buscar + "%' ";
    
    if (!buscar_idcliente.trim().equals("")){
        filtroBusqueda += " AND ID_CLIENTE='" + buscar_idcliente +"' ";
    }else{
        filtroBusqueda += " AND ID_CLIENTE>=0 ";
    }
    
    if (!buscar_idprospecto.trim().equals("")){
        filtroBusqueda += " AND ID_PROSPECTO='" + buscar_idprospecto +"' ";
    }else{
        filtroBusqueda += " AND ID_PROSPECTO>=0 ";
    }
    
    if (!strWhereRangoFechas.trim().equals("")){
        filtroBusqueda += " AND " + strWhereRangoFechas;
    }
    
    if(idClienteCategoria > 0){
        filtroBusqueda += " AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM CLIENTE WHERE ID_CLIENTE_CATEGORIA = " + idClienteCategoria + ")";
    }   
    
    
    int idCotizacion = -1;
    try{ idCotizacion = Integer.parseInt(request.getParameter("idCotizacion")); }catch(NumberFormatException e){}
    
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
    
     SGCotizacionBO cotizacionBO = new SGCotizacionBO(user.getConn());
     SgfensCotizacion[] cotizacionDto = new SgfensCotizacion[0];
     try{
         limiteRegistros = cotizacionBO.findCotizacion(idCotizacion, idEmpresa , 0, 0, filtroBusqueda).length;
         
          if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        cotizacionDto = cotizacionBO.findCotizacion(idCotizacion, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
          
     ClienteCategoriaBO clienteCategoriaBO = new ClienteCategoriaBO(user.getConn());
     ClienteCategoria[] clientesCategorias = clienteCategoriaBO.findClienteCategorias(0, idEmpresa, 0, 0, " AND ID_ESTATUS = 1 ");
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../cotizacion/cotizacion_form.jsp";
    String paramName = "idCotizacion";
    String parametrosPaginacion="q_fh_min="+fechaMin+"&q_fh_max="+fechaMax+"&q="+buscar+"&q_idcliente="+buscar_idcliente+"&q_idprospecto="+buscar_idprospecto;// "idEmpresa="+idEmpresa;
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
            
            function editarCotizacion(idCotizacion){
                if(idCotizacion>=0){
                    $.ajax({
                        type: "POST",
                        url: "cotizacion_ajax.jsp",
                        data: { mode: '7', id_cotizacion : idCotizacion },
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
                               location.href = "cotizacion_form.jsp?acc=2&idCotizacion="+idCotizacion;
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
            
            
            function convertirAFactura(idCotizacion){
                if(idCotizacion>=0){
                    $.ajax({
                        type: "POST",
                        url: "cotizacion_ajax.jsp",
                        data: { mode: '24', id_cotizacion : idCotizacion },
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
                               location.href = "../cfdi_factura/cfdi_factura_form.jsp";
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
            
            function convertirAPedido(idCotizacion){
                if(idCotizacion>=0){
                    $.ajax({
                        type: "POST",
                        url: "cotizacion_ajax.jsp",
                        data: { mode: '25', id_cotizacion : idCotizacion },
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
                               location.href = "../pedido/pedido_form.jsp";
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
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                Busqueda Avanzada &dArr;
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content" style="display: none;">
                            <form action="cotizacion_list.jsp" id="search_form_advance" name="search_form_advance" method="post">
                                <p>
                                    Buscar por Fechas &raquo;&nbsp;&nbsp;
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
                                    <%= new ClienteBO(user.getConn()).getClientesByIdHTMLCombo(idEmpresa, -1," AND ID_ESTATUS=1 " + (user.getUser().getIdRoles() == RolesBO.ROL_OPERADOR? " AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM SGFENS_CLIENTE_VENDEDOR WHERE ID_USUARIO_VENDEDOR="+user.getUser().getIdUsuarios()+")" : "") ) %>
                                </select>
                                </p>
                                <br/>
                                
                                <%if(clientesCategorias.length > 0){%>
                                    <p>
                                        <label>Tipo:</label><br/>
                                        <select id="idClienteCategoria" name="idClienteCategoria">
                                            <option value="0">Selecciona Tipo de Categoría de Cliente</option>
                                            <%
                                                out.print(clienteCategoriaBO.getClienteCategoriasByIdHTMLCombo(idEmpresa, -1));
                                            %>
                                        </select>
                                    </p>
                                    <br/>
                                <%}%>
                                
                                <p>
                                <label>Prospecto</label><br/>
                                <select id="q_idprospecto" name="q_idprospecto" class="flexselect">
                                    <option></option>
                                    <%= new SGProspectoBO(user.getConn()).getProspectosByIdHTMLCombo(idEmpresa, -1) %>
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
                    
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_cotizacion.png" alt="icon"/>
                                Cotizaciones
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
                                                            <td>
                                                                <div id="search">
                                                                <form action="cotizacion_list.jsp" id="search_form" name="search_form" method="get">
                                                                        <input type="text" id="q" name="q" title="Buscar por Folio Cotización (p.ej: C0000)" class="" style="width: 300px; float: left; "
                                                                               value="<%=buscar%>"/>
                                                                        <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                                </form>
                                                                </div>
                                                            </td>
                                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                                            <td>
                                                                <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Nueva Cotización" 
                                                                       style="float: right; width: 100px;" onclick="location.href='<%=urlTo%>?acc=1'"/>
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
                                            <th>Folio</th>
                                            <th>Ver</th>
                                            <th>Vendedor</th>
                                            <th>Cliente/Prospecto</th>
                                            <th>Fecha</th>
                                            <th>Total</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            for (SgfensCotizacion item:cotizacionDto){
                                                try{
                                                    DatosUsuario datosUsuarioVendedor = new UsuarioBO(item.getIdUsuarioVendedor()).getDatosUsuario();
                                                    Cliente cliente = null;
                                                    SgfensProspecto prospecto = null;
                                                    try{ cliente = new ClienteBO(item.getIdCliente(),user.getConn()).getCliente(); }catch(Exception ex){}
                                                    try{ prospecto = new SGProspectoBO(item.getIdProspecto(),user.getConn()).getSgfensProspecto(); }catch(Exception ex){}
                                        %>
                                        <tr>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdCotizacion() %></td>
                                            <td><%=item.getFolioCotizacion() %></td>
                                            <td>
                                                <a href="../../jsp/reportesExportar/previewCotizacionPDF.jsp?idCotizacion=<%=item.getIdCotizacion() %>" id="btn_show_cfdi" title="Previsualizar Cotización"
                                                class="modalbox_iframe">
                                                    <img src="../../images/icon_consultar.png" alt="Mostrar Cotización" class="help" title="Previsualizar Cotización"/><br/>
                                                </a>
                                            </td>
                                            <td><%= datosUsuarioVendedor!=null? (datosUsuarioVendedor.getNombre() +" " + datosUsuarioVendedor.getApellidoPat()) :"Sin vendedor asignado" %></td>
                                            <td>
                                                <%
                                                    if (item.getIdCliente()>0 && cliente!=null){
                                                     //Cliente
                                                        out.print(cliente.getRazonSocial() + " <i>[Cliente]</i>");
                                                    }else if (item.getIdProspecto()>0 && prospecto!=null){
                                                        out.print(prospecto.getRazonSocial() + " <i>[Prospecto]</i>");
                                                    }else{
                                                        out.print("Sin cliente o prospecto asignado.");
                                                    }
                                                %>
                                            </td>
                                            <td><%= DateManage.formatDateToNormal(item.getFechaCotizacion()) %></td>
                                            <td><%=item.getTotal() %></td>
                                            <td>
                                                <!--<a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdCotizacion()%>&acc=1"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>-->
                                                <a href="#" onclick="editarCotizacion(<%=item.getIdCotizacion()%>);"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar" /></a>
                                                &nbsp;&nbsp;
                                                <a href="#" onclick="convertirAPedido(<%=item.getIdCotizacion()%>);"><img src="../../images/icon_convert_pedido.png" alt="toPedido" class="help" title="Convertir a Pedido" /></a>
                                                <%if(cliente!=null){%>
                                                &nbsp;&nbsp;
                                                <!--<a href="#" onclick="convertirAFactura(<%=item.getIdCotizacion()%>);"><img src="../../images/icon_convert_factura.png" alt="facturar" class="help" title="Convertir a Factura" /></a>-->                                                
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
                                <jsp:param name="idReport" value="<%= ReportBO.COTIZACION_REPORT %>" />
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