<%-- 
    Document   : cxc_frame_facturas_list.jsp
    Created on : 18-may-2015, 12:13:49
    Author     : ISCesarMartinez poseidon24@hotmail.com
--%>
<%@page import="com.tsp.sct.bo.SGEstatusPedidoBO"%>
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
            strWhereRangoFechas="(CAST(FECHA_PEDIDO AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"' )";
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas="(FECHA_PEDIDO  >= '"+buscar_fechamin+"')";
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas="(FECHA_PEDIDO  <= '"+buscar_fechamax+"')";
        }
    }
    
    String filtroBusqueda = "";
    
    //Solo mostraremos Pedidos entregados
    //filtroBusqueda = " AND (ID_ESTATUS_PEDIDO ='2')";
    
    //Ademas solo Facturas de Tipo FACTURA
    //filtroBusqueda += " AND (ID_TIPO_COMPROBANTE='2' OR ID_TIPO_COMPROBANTE='38' OR ID_TIPO_COMPROBANTE='41') ";
    
    if (!buscar_idestatus.trim().equals("")){
        if (buscar_idestatus.trim().equals("1")){
            //Activa
            filtroBusqueda += " AND (ID_ESTATUS_PEDIDO!=3 AND (TOTAL - ADELANTO - SALDO_PAGADO)>0) ";
        }
        if (buscar_idestatus.trim().equals("2")){
            //Cancelada
            filtroBusqueda += " AND (ID_ESTATUS_PEDIDO=3) ";
        }
        if (buscar_idestatus.trim().equals("3")){
            //Pagada
            filtroBusqueda += " AND (ID_ESTATUS_PEDIDO!=3 AND (TOTAL - ADELANTO - SALDO_PAGADO)<=0) ";
        }
    }
    
    if (!buscar_folio.trim().equals("")){
        filtroBusqueda += " AND (FOLIO_PEDIDO LIKE '%" + buscar_folio +"%') ";
    }
    
    if (!buscar_idcliente.trim().equals("")){
        filtroBusqueda += " AND ID_CLIENTE='" + buscar_idcliente +"' ";
    }
    
    if (!buscar_idsucursal.trim().equals("")){
        filtroBusqueda += " AND ID_EMPRESA='" + buscar_idsucursal +"' ";
    }
    
    if (!strWhereRangoFechas.trim().equals("")){
        filtroBusqueda += " AND " + strWhereRangoFechas;
    }
       
    
    
    int idPedido = -1;
    try{ idPedido = Integer.parseInt(request.getParameter("idPedido")); }catch(NumberFormatException e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
    EmpresaBO empresaBO = new EmpresaBO(user.getConn());
    
    /*
    * Paginación
    */
    int paginaActual = 1;
    double registrosPagina = 5;
    double limiteRegistros = 0;
    int paginasTotales = 0;
    int numeroPaginasAMostrar = 5;

    try{
        paginaActual = Integer.parseInt(request.getParameter("pagina"));
    }catch(Exception e){}

    try{
        registrosPagina = Integer.parseInt(request.getParameter("registros_pagina"));
    }catch(Exception e){}
    
    SGPedidoBO pedidoBO = new SGPedidoBO(user.getConn());
    SgfensPedido[] pedidoDto = new SgfensPedido[0];
    try{
        limiteRegistros = pedidoBO.findCantidadPedido(idPedido, idEmpresa , 0, 0, filtroBusqueda);
         
        paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        pedidoDto = pedidoBO.findPedido(idPedido, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

    }catch(Exception ex){
        ex.printStackTrace();
    }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../pedido/pedido_form.jsp";
    String paramName = "idPedido";
    String parametrosPaginacion="q_idcliente="+buscar_idcliente+"&q_idestatus="+buscar_idestatus+"&q_idsucursal="+buscar_idsucursal+"&q_fh_min="+request.getParameter("q_fh_min")+"&q_fh_max="+request.getParameter("q_fh_max")+"&q_folio="+buscar_folio;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    
    Empresa empresaActual =  new EmpresaBO(idEmpresa, user.getConn()).getEmpresa();
    
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
            
            $(document).ready(function(){
               initToggleCheckId();
           });
            
            function confirmarCancelarPedido(idPedido, folioPedido){
                parent.apprise('¿Esta seguro que desea cancelar el pedido con Folio '+folioPedido+'?', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
                    function(r){
                        if(r){
                            // Usuario dio click 'Yes'
                            cancelarPedido(idPedido);
                        }
                });
                
            }
            
            function cancelarPedido(idPedido){
                if(idPedido>=0){
                    $.ajax({
                        type: "POST",
                        url: "../pedido/pedido_ajax.jsp",
                        data: { mode: '23', id_pedido : idPedido },
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
                               parent.apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                    function(r){
                                        location.href = "cxc_frame_pedidos_list.jsp?idPedido="+idPedido;
                                });
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               //$("#ajax_message").html(datos);
                               //$("#ajax_message").fadeIn("slow");
                               parent.apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                           }
                        }
                    });
                }
            }
            
            function abrirLinkEnParent(url){
                window.top.location.href = url; 
            }
            
            /**
            * Funcion para invocar la descarga de XML en archivo ZIP
            * Recorre todos los checkbox con nombre: check_id_factura
            * y toma su valor para enviarlos como parametros GET separados por coma
            * a la JSP de descarga de archivos ZIP
            */
           function descargaZipFiles(){

               var query_string = '../../jsp/file/downloadFilesZip.jsp?mode=3';

               var idString ="";

               $("input[name=check_id_pedido]").each(function(){
                   if(this.checked){
                       idString += this.value + ",";
                   }
               });
               
               if (idString!==""){
                   query_string += "&id_pedido_array="+idString;

                   window.open(query_string, "Descarga de archivos", "location=0,status=0,scrollbars=0,menubar=0,resizable=0,width=400,height=400");
               }else{
                   parent.apprise('No se selecciono ningun archivo a descargar',{
                       'warning':true,
                       'animate':true
                   });
               }

           }

            /**
            * Asigna una funcion para el checkbox maestro que hara
            * que un conjunto de checkbox se active o desactive
            */
           function initToggleCheckId(){
               //Checkbox
               $("input[name=check_master_id]").change(function(){
                   
                   $('input[name=check_id_pedido]').each( function() {
                       if($("input[name=check_master_id]:checked").length === 1){
                           this.checked = true;
                       } else {
                           this.checked = false;
                       }
                   });
                   
               });
           }
           
           /**
            * Para iniciar el FancyBox en el Parent (esta pagina debe ir dentro de un iframe)
            * @param {type} url Link que se abrira en el FancyBox
            * @returns {undefined}
            */
            function modalboxOnParent(url){
                parent.$.fancybox({
                            'padding'       : 0, 
                            'titleShow'     : false, 
                            'overlayColor'  : '#333333', 
                            'overlayOpacity': .5,
                            'type'          : 'iframe',
                            'autoScale'     :   true,
                            'href'          : url
                    });
            }
           
        </script>
    </head>
    <body class="nobg">
        <div class="content_wrapper" style="width: 99%;">

            <!-- Inicio de Contenido -->
            <div>

                <div class="inner">
                    <!--<h1>Cuentas por Cobrar (Pedidos)</h1>-->
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>
                    
                    <!--<br class="clear"/>-->
                    
                    <div class="onecolumn">
                        <!--
                        <div class="header">
                            <span>
                                <img src="../../images/icon_cxc.png" alt="icon"/>
                                CxC Pedidos
                            </span>                            
                        </div>
                        <br class="clear"/>
                        -->
                        <div class="content">
                            <form id="form_data" name="form_data" action="" method="post">
                                <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th><input type="checkbox" name="check_master_id" value="" /></th>
                                            <th>ID</th>
                                            <th>Folio</th>
                                            <!--<th>Serie</th>-->
                                            <th>Fecha Emisión</th>
                                            <th>Cliente</th>
                                            <th align="right">Importe</th>
                                            <th align="right">Saldo</th>
                                            <th align="right">Días Crédito</th>
                                            <th>Pago</th>
                                            <th align="center">Semáforo</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(user.getConn());
                                            double sumaImporte = 0;
                                            double sumaSaldo = 0;
                                            for (SgfensPedido item : pedidoDto){
                                                try{
                                                    int id = item.getIdPedido();
                                                    String folio = "";
                                                    String serie = " - ";
                                                    String fechaEmision = "";
                                                    String clienteNombre = "Sin cliente asignado";
                                                    String importeStr = "";
                                                    String saldoStr = "";
                                                    long diasCredito = 0;
                                                    long diasMora = 0;
                                                    double porcentajeTiempoTranscurridoCredito = 0;
                                                    
                                                    pedidoBO.setPedido(item);
                                                    
                                                    Cliente cliente = null;
                                                    try{ cliente = new ClienteBO(item.getIdCliente(),user.getConn()).getCliente(); }catch(Exception ex){}
                                                    if (cliente!=null)
                                                        clienteNombre = cliente.getRazonSocial();
                                                    
                                                    double saldo = 0;
                                                    double importe = 0;
                                                    {
                                                        folio = StringManage.getValidString(item.getFolioPedido());
                                                        fechaEmision =  DateManage.formatDateToNormal(item.getFechaPedido());
                                                        importe = item.getTotal();
                                                        saldo = item.getBonificacionDevolucion()>0? (item.getTotal() - item.getAdelanto() - item.getSaldoPagado()):(item.getTotal() + Math.abs(item.getBonificacionDevolucion()) - item.getAdelanto()  - item.getSaldoPagado());
                                                        diasCredito = pedidoBO.calculaDiasCredito();
                                                        diasMora = pedidoBO.calculaDiasMora();
                                                        porcentajeTiempoTranscurridoCredito = pedidoBO.calculaPorcentajeTranscurridoCredito();
                                                    }
                                                    
                                                    if (saldo<0 && saldo>-0.001)
                                                        saldo = 0;
                                                    if (saldo==0){
                                                        diasMora = 0;
                                                        porcentajeTiempoTranscurridoCredito = -1;
                                                    }
                                                    importeStr = formatMoneda.format(importe);
                                                    saldoStr = formatMoneda.format(saldo);
                                                    
                                                    String estatusFinanciero = "Activa";
                                                    if (saldo<=0){
                                                        estatusFinanciero = "Pagada";
                                                    }
                                                    
                                                    String colorSemaforo = "green";
                                                    String nombreSemaforo =  !estatusFinanciero.equals("Pagada")?"En Tiempo":"Pagado";
                                                    if (item.getIdEstatusPedido()==SGEstatusPedidoBO.ESTATUS_CANCELADO){
                                                        //Si esta cancelada
                                                        colorSemaforo = "grey";
                                                        nombreSemaforo = "Cancelada";
                                                        estatusFinanciero = "Cancelada";
                                                    }else{
                                                        if (diasMora>0){
                                                            //Si excede del plazo de crédito y tiene dias de mora
                                                            colorSemaforo = "red";
                                                            nombreSemaforo = "Plazo Vencido";
                                                        }else if (porcentajeTiempoTranscurridoCredito>=80){
                                                            //Si resta 20% para que se venza el plazo de crédito
                                                            colorSemaforo = "yellow";
                                                            nombreSemaforo = "Por Vencer";
                                                        }
                                                        sumaSaldo += saldo;
                                                        sumaImporte += importe;
                                                    }
                                        %>
                                        <tr>
                                            <td>
                                                <input type="checkbox" name="check_id_pedido" value="<%= id %>" />
                                            </td>
                                            <td><%= id %></td>
                                            <td><%= folio %></td>
                                            <!--<td><%= serie %></td>-->
                                            <td><%= fechaEmision %></td>
                                            <td><%= clienteNombre %></td>
                                            <td align="right"><%= importeStr %></td>
                                            <td align="right"><%= saldoStr %></td>
                                            <td align="right"><%= diasCredito %></td>
                                            <td><%= estatusFinanciero %></td>
                                            <td align="center">
                                                <img src="../../images/semaforo/icon_s_<%=colorSemaforo%>.png" width="20px" height="20px"
                                                     alt="<%=nombreSemaforo%>" class="help" title="<%=nombreSemaforo%>"/>
                                            </td>
                                            <td>
                                                
                                                <!--Cobranza-->
                                                <a href="#" onclick="abrirLinkEnParent('../catCobranzaAbono/catCobranzaAbono_list.jsp?idPedido=<%= id %>')" ><img src="../../images/icon_ventas1.png" alt="consultar cobranza" class="help" title="Cobranza" /></a>
                                                &nbsp;&nbsp;
                                                
                                                <!-- PDF / Vista Previa -->
                                                <!--<a href="../../jsp/reportesExportar/previewPedidoPDF.jsp?idPedido=<%= id %>" id="btn_show_cfdi" title="Previsualizar Pedido" class="modalbox_iframe"> -->
                                                <a href="#" onclick="modalboxOnParent('../../jsp/reportesExportar/previewPedidoPDF.jsp?idPedido=<%= id %>');" id="btn_show_cfdi" title="Previsualizar Pedido">
                                                    <img src="../../images/icon_consultar.png" alt="Mostrar Pedido" class="help" title="Previsualizar Pedido"/>
                                                </a>
                                                &nbsp;&nbsp;
                                                
                                                <!-- Adjuntos -->
                                                <a href="#" onclick="modalboxOnParent('../../jsp/adjuntos/consultaAdjuntos.jsp?idPedido=<%= id %>&idComprobanteFiscal=<%= item.getIdComprobanteFiscal() %>');" title="Adjuntos" >
                                                    <img src="../../images/sar_adjunto_varios.png" alt="Adjuntos" class="help" title="Adjuntos"/>
                                                </a>
                                                &nbsp;&nbsp;
                                                
                                                <!-- Log Notas/Comentarios -->
                                                <!--<a href="../../jsp/notas/notas_list.jsp?idPedido=<%= id %>&idComprobanteFiscal=<%= item.getIdComprobanteFiscal() %>" title="Log Notas" class="modalbox_iframe">-->
                                                <a href="#" onclick="modalboxOnParent('../../jsp/notas/notas_list.jsp?idPedido=<%= id %>&idComprobanteFiscal=<%= item.getIdComprobanteFiscal() %>');" title="Log Notas">
                                                    <img src="../../images/icon_notas.png" alt="Log Notas" class="help" title="Log Notas"/>
                                                </a>
                                                &nbsp;&nbsp;
                                                    
                                                <% if (item.getIdEstatusPedido()!=SGEstatusPedidoBO.ESTATUS_CANCELADO) {%>
                                                <!--Cancelar-->
                                                <a href="#" onclick="confirmarCancelarPedido(<%= id %>,'<%=item.getFolioPedido() %>');"><img src="../../images/icon_delete.png" alt="cancelar" class="help" title="Cancelar" /></a>
                                                &nbsp;&nbsp;
                                                <%}%>
                                                
                                            </td>
                                        </tr>
                                        <%      }catch(Exception ex){
                                                    ex.printStackTrace();
                                                }
                                            } 
                                        %>
                                        <tr style="font-size: 14;">
                                            <td colspan="5" style="text-align: right;"><b>Subtotales: </b></td>
                                            <td style="color: #0000cc; text-align: right;"><%=formatMoneda.format(sumaImporte)%> </td>
                                            <td style="color: #0000cc; text-align: right;"><%=formatMoneda.format(sumaSaldo)%> </td>
                                            <td colspan="4">&nbsp;</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </form>

                            <br class="clear"/>
                            <div id="div_descarga" name="div_descarga" class="switch" style="float: left;">
                                <input type="button" id="descarga" name="descarga" class="left_switch" value="Descarga XML" 
                                    style="float: left; "
                                    onclick="descargaZipFiles()"/>
                            </div>
                            
                            <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <jsp:param name="idReport" value="<%= ReportBO.CXC_PEDIDOS_LIST_REPORT %>" />
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

            </div>
            <!-- Fin de Contenido-->
        </div>

        <script>
            $("select.flexselect").flexselect();
        </script>
    </body>
</html>
<%}%>