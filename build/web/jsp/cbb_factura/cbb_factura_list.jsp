<%-- 
    Document   : cbb_factura_list
    Created on : 28/05/2013, 10:00:48 AM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.bo.ParametrosBO"%>
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
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String buscar_idcliente = request.getParameter("q_idcliente")!=null?request.getParameter("q_idcliente"):"";
    String buscar_idestatuscomprobanteFiscal = request.getParameter("q_idestatuscomprobanteFiscal")!=null?request.getParameter("q_idestatuscomprobanteFiscal"):"";
    String buscar_idsucursal = request.getParameter("q_idsucursal")!=null?request.getParameter("q_idsucursal"):"";
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
            strWhereRangoFechas="(CAST(FECHA_CAPTURA AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')";
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas="(FECHA_CAPTURA  >= '"+buscar_fechamin+"')";
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas="(FECHA_CAPTURA  <= '"+buscar_fechamax+"')";
        }
    }
    
    String filtroBusqueda = "";
    
    //Solo mostraremos Facturas con Estatus 3 (entregada) y 4 (cancelada), 5 (cancelacion)
    filtroBusqueda = " AND (ID_ESTATUS='3' OR ID_ESTATUS ='4' OR ID_ESTATUS ='5')";
    
    if (!buscar_idestatuscomprobanteFiscal.trim().equals("")){
        filtroBusqueda = " AND ID_ESTATUS='" + buscar_idestatuscomprobanteFiscal +"' ";
    }
    
    //Ademas solo Facturas de Tipo FACTURA
    filtroBusqueda += " AND ID_TIPO_COMPROBANTE='13' ";
    
    if (!buscar.trim().equals(""))
        filtroBusqueda += " AND (FOLIO_GENERADO LIKE '%" + buscar + "%' OR ID_COMPROBANTE_FISCAL LIKE '%" + buscar + "%' OR UUID LIKE '%" + buscar + "%' )";
    
    if (!buscar_idcliente.trim().equals("")){
        filtroBusqueda += " AND ID_CLIENTE='" + buscar_idcliente +"' ";
    }else{
        filtroBusqueda += " AND ID_CLIENTE>=0 ";
    }
    
    if (!buscar_idsucursal.trim().equals("")){
        filtroBusqueda += " AND ID_EMPRESA='" + buscar_idsucursal +"' ";
    }
    
    if (!strWhereRangoFechas.trim().equals("")){
        filtroBusqueda += " AND " + strWhereRangoFechas;
    }
       
    
    
    int idComprobanteFiscal = -1;
    try{ idComprobanteFiscal = Integer.parseInt(request.getParameter("idComprobanteFiscal")); }catch(NumberFormatException e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
    EmpresaBO empresaBO = new EmpresaBO(user.getConn());
    
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
    
     ComprobanteFiscalBO comprobanteFiscalBO = new ComprobanteFiscalBO(user.getConn());
     ComprobanteFiscal[] comprobanteFiscalDto = new ComprobanteFiscal[0];
     try{
         limiteRegistros = comprobanteFiscalBO.findComprobanteFiscal(idComprobanteFiscal, idEmpresa , 0, 0, filtroBusqueda).length;
         
          if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        comprobanteFiscalDto = comprobanteFiscalBO.findComprobanteFiscal(idComprobanteFiscal, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "cbb_factura_form.jsp";
    String paramName = "idComprobanteFiscal";
    String parametrosPaginacion="q="+buscar+"&q_idcliente="+buscar_idcliente+"&q_idestatuscomprobanteFiscal="+buscar_idestatuscomprobanteFiscal+"&q_idsucursal="+buscar_idsucursal+"&q_fh_min="+buscar_fechamin+"&q_fh_max="+buscar_fechamax;// "idEmpresa="+idEmpresa;
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
            
            function editarComprobanteFiscal(idComprobanteFiscal){
                if(idComprobanteFiscal>=0){
                    $.ajax({
                        type: "POST",
                        url: "cbb_factura_ajax.jsp",
                        data: { mode: '7', id_comprobanteFiscal : idComprobanteFiscal },
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
                               location.href = "cbb_factura_form.jsp?acc=2&idComprobanteFiscal="+idComprobanteFiscal;
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
            
            function confirmarCancelarComprobanteFiscal(idComprobanteFiscal){
                apprise('¿Esta seguro que desea cancelar el Comprobante Fiscal?', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
                    function(r){
                        if(r){
                            // Usuario dio click 'Yes'
                            cancelarComprobanteFiscal(idComprobanteFiscal);
                        }
                });
                
            }
            
            function cancelarComprobanteFiscal(idComprobanteFiscal){
                if(idComprobanteFiscal>=0){
                    $.ajax({
                        type: "POST",
                        url: "cbb_factura_ajax.jsp",
                        data: { mode: '22', id_comprobanteFiscal : idComprobanteFiscal },
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
                                        location.href = "cbb_factura_list.jsp?idComprobanteFiscal="+idComprobanteFiscal;
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
            
            function mostrarMensajeCobranzaPedido(idPedidoPadre, folioPedido){
                apprise('<center>La factura esta relacionada a un pedido, la cobranza se debe registrar en él.'
                    + '<br/> ¿Desea ir al detalle del pedido?'
                    + '<br/><br/><b>Folio Pedido: ' + folioPedido + '</b></center>', 
                    {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
                    function(r){
                        if(r){
                            // Usuario dio click 'Yes'
                            location.href='../../jsp/pedido/pedido_list.jsp?idPedido='+idPedidoPadre;
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
                    <h1>Facturación CBB</h1>
                    
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
                            <form action="cbb_factura_list.jsp" id="search_form_advance" name="search_form_advance" method="post">
                                <p>
                                    Por Fecha de Captura &raquo;&nbsp;&nbsp;
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
                                <select id="q_idestatuscomprobanteFiscal" name="q_idestatuscomprobanteFiscal">
                                    <option></option>
                                    <option value="3">Entregada</option>
                                    <option value="4">Cancelada</option>
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
                                <img src="../../images/icon_cfdi_1.png" alt="icon"/>
                                Facturas (CBB)
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td>
                                                <div id="search">
                                                <form action="cbb_factura_list.jsp" id="search_form" name="search_form" method="get">
                                                        <input type="text" id="q" name="q" title="Buscar por ID/Folio" class="" style="width: 300px; float: left; "
                                                                value="<%=buscar%>"/>
                                                        <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                </form>
                                                </div>
                                            </td>
                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                            <td>
                                                <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Nuevo CBB" 
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
                                            <th>Ver</th>
                                            <th>Folio</th>
                                            <th>Cliente</th>
                                            <th>Fecha Captura</th>
                                            <th>Estado</th>
                                            <th>Total</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            for (ComprobanteFiscal item:comprobanteFiscalDto){
                                                try{
                                                    Cliente cliente = null;
                                                    try{ cliente = new ClienteBO(item.getIdCliente(),user.getConn()).getCliente(); }catch(Exception ex){}
                                                    
                                                    boolean isFacturaFromPedido = false;
                                                    int idPedidoParent = -1;
                                                    String folioPedidoParent ="";
                                                    {
                                                        SgfensPedido[] facturaFromPedidos = new SgfensPedidoDaoImpl(user.getConn()).findWhereIdComprobanteFiscalEquals(item.getIdComprobanteFiscal());
                                                        if (facturaFromPedidos!=null){
                                                            if (facturaFromPedidos.length>0){
                                                                isFacturaFromPedido=true;
                                                                idPedidoParent = facturaFromPedidos[0].getIdPedido();
                                                                folioPedidoParent = facturaFromPedidos[0].getFolioPedido();
                                                            }
                                                        }
                                                    }
                                                    
                                        %>
                                        <tr>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdComprobanteFiscal() %></td>                                            
                                            <td>
                                                 <a href="../../jsp/reportesExportar/previewCBBpdf.jsp?idComprobanteFiscal=<%=item.getIdComprobanteFiscal() %>&versionCfd=111&accion=imprimeFacturaCBB" id="btn_show_cfdi" title="Previsualizar CBB"
                                                class="modalbox_iframe">                                                      
                                                    <img src="../../images/icon_consultar.png" alt="Mostrar CBB" class="help" title="Previsualizar CBB"/><br/>
                                                </a>
                                                
                                            </td>
                                            <td><%=item.getFolioGenerado() %></td>
                                            <td>
                                                <%
                                                    if (item.getIdCliente()>0 && cliente!=null){
                                                     //Cliente
                                                        out.print(cliente.getRazonSocial());
                                                    }else{
                                                        out.print("Sin cliente asignado.");
                                                    }
                                                %>
                                            </td>
                                            <td><%= DateManage.formatDateToNormal(item.getFechaCaptura()) %></td>
                                            <td><%= EstatusComprobanteBO.getEstatusName(item.getIdEstatus()) %></td>
                                            <td><%=item.getImporteNeto() %></td>
                                            <td>
                                                <!--<a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdComprobanteFiscal()%>&acc=1"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>-->
                                                <!--<a href="#" onclick="editarComprobanteFiscal(<%=item.getIdComprobanteFiscal()%>);"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar" /></a>
                                                &nbsp;&nbsp;-->
                                                <% if (item.getIdEstatus()==EstatusComprobanteBO.ESTATUS_ENTREGADA) {%>
                                                <a href="#" onclick="confirmarCancelarComprobanteFiscal(<%=item.getIdComprobanteFiscal()%>);"><img src="../../images/icon_delete.png" alt="cancelar" class="help" title="Cancelar" /></a>
                                                &nbsp;&nbsp;
                                                <%}%>
                                                <% if (isFacturaFromPedido){ %>
                                                <a href="#" onclick="mostrarMensajeCobranzaPedido(<%=idPedidoParent %>,'<%=folioPedidoParent %>');"><img src="../../images/icon_ventas1.png" alt="consultar cobranza" class="help" title="Cobranza" /></a>
                                                <% }else {%>
                                                <a href="../catCobranzaAbono/catCobranzaAbono_list.jsp?idComprobanteFiscal=<%=item.getIdComprobanteFiscal() %>"><img src="../../images/icon_ventas1.png" alt="consultar cobranza" class="help" title="Cobranza" /></a>
                                                <% } %>
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
                                <jsp:param name="idReport" value="<%= ReportBO.FACTURA_REPORT %>" />
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