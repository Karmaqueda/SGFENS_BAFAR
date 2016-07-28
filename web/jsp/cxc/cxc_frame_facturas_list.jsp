<%-- 
    Document   : cxc_frame_facturas_list.jsp
    Created on : 18-may-2015, 12:13:49
    Author     : ISCesarMartinez poseidon24@hotmail.com
--%>
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
    
    //Solo facturas que no tengan relacion a pedidos
    filtroBusqueda += " AND id_comprobante_fiscal NOT IN (SELECT id_comprobante_fiscal FROM sgfens_pedido WHERE id_comprobante_fiscal>0) ";
    
    //Solo mostraremos Facturas con Estatus 3 (entregada) y 4 (cancelada), 5 (cancelacion)
    //filtroBusqueda = " AND (ID_ESTATUS='3' OR ID_ESTATUS ='4' OR ID_ESTATUS ='5')";
    filtroBusqueda += " AND (ID_ESTATUS='3' OR ID_ESTATUS ='4')";
    
    //Ademas solo Facturas de Tipo FACTURA
    filtroBusqueda += " AND (ID_TIPO_COMPROBANTE='2' OR ID_TIPO_COMPROBANTE='38' OR ID_TIPO_COMPROBANTE='41') ";
    
    if (!buscar_idestatus.trim().equals("")){
        if (buscar_idestatus.trim().equals("1")){
            //Activa
            filtroBusqueda += " AND (ID_ESTATUS='3' AND (IMPORTE_NETO-SALDO_PAGADO)>0) ";
        }
        if (buscar_idestatus.trim().equals("2")){
            //Cancelada
            filtroBusqueda += " AND (ID_ESTATUS=4 OR ID_ESTATUS=5) ";
        }
        if (buscar_idestatus.trim().equals("3")){
            //Pagada
            filtroBusqueda += " AND (ID_ESTATUS='3' AND (IMPORTE_NETO-SALDO_PAGADO)<=0) ";
        }
    }
    
    if (!buscar_folio.trim().equals("")){
        filtroBusqueda += " AND (FOLIO_GENERADO LIKE '%" + buscar_folio +"%') ";
    }
    
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
    ComprobanteFiscalBO comprobanteFiscalBO = new ComprobanteFiscalBO(user.getConn());
    ComprobanteFiscal[] comprobanteFiscalDto = new ComprobanteFiscal[0];
    try{
        limiteRegistros = comprobanteFiscalBO.findCantidadComprobanteFiscal(idComprobanteFiscal, idEmpresa , 0, 0, filtroBusqueda);
         
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
    String paramName = "idComprobanteFiscal";
    String parametrosPaginacion="q_idcliente="+buscar_idcliente+"&q_idestatus="+buscar_idestatus+"&q_idsucursal="+buscar_idsucursal+"&q_fh_min="+request.getParameter("q_fh_min")+"&q_fh_max="+request.getParameter("q_fh_max")+"&q_folio="+buscar_folio;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    
    Empresa empresaActual =  new EmpresaBO(idEmpresa, user.getConn()).getEmpresa();
    //EmpresaPermisoAplicacion permisoAplicacion = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaActual.getIdEmpresaPadre());
    
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
            
            function confirmarCancelarComprobanteFiscal(idComprobanteFiscal, UUID){
                parent.apprise('¿Esta seguro que desea cancelar el Comprobante Fiscal con UUID '+UUID+'?', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
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
                        url: "../cfdi_factura/cfdi_factura_ajax.jsp",
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
                               parent.apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                    function(r){
                                        location.href = "cxc_frame_facturas_list.jsp?idComprobanteFiscal="+idComprobanteFiscal;
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
            
            function mostrarMensajeCobranzaPedido(idPedidoPadre, folioPedido){
                parent.apprise('<center>La factura esta relacionada a un pedido, la cobranza se debe registrar en él.'
                    + '<br/> ¿Desea ir al detalle del pedido?'
                    + '<br/><br/><b>Folio Pedido: ' + folioPedido + '</b></center>', 
                    {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
                    function(r){
                        if(r){
                            // Usuario dio click 'Yes'
                            //location.href='../../jsp/pedido/pedido_list.jsp?idPedido='+idPedidoPadre;
                            abrirLinkEnParent('../../jsp/pedido/pedido_list.jsp?idPedido='+idPedidoPadre);
                        }
                });
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
               //alert("Se descarga zip");

               var query_string = '../../jsp/file/downloadFilesZip.jsp?mode=1';

               var idString ="";

               $("input[name=check_id_factura]").each(function(){
                   //$('input[type=checkbox]').each( function() {
                   if(this.checked){
                       idString += this.value + ",";
                   }
               });
               if (idString!==""){
                   query_string += "&xml=true&id_comprobante_array="+idString;
                   //alert("cadena: "+query_string);

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
                   $('input[name=check_id_factura]').each( function() {
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
                    <!--<h1>Cuentas por Cobrar (Comprobantes Fiscales)</h1>-->
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>
                    
                    <!--<br class="clear"/>-->
                    
                    <div class="onecolumn">
                        <!--
                        <div class="header">
                            <span>
                                <img src="../../images/icon_cxc.png" alt="icon"/>
                                CxC Comprobantes Fiscales (Facturas)
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
                                            <th>Serie</th>
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
                                            ComprobanteFiscalBO compBO = new ComprobanteFiscalBO(user.getConn());
                                            SGPedidoBO pedidoBO = new SGPedidoBO(user.getConn());
                                            double sumaImporte = 0;
                                            double sumaSaldo = 0;
                                            for (ComprobanteFiscal item:comprobanteFiscalDto){
                                                try{
                                                    int id = item.getIdComprobanteFiscal();
                                                    String folio = "";
                                                    String serie = " - ";
                                                    String fechaEmision = "";
                                                    String clienteNombre = "Sin cliente asignado";
                                                    String importeStr = "";
                                                    String saldoStr = "";
                                                    long diasCredito = 0;
                                                    long diasMora = 0;
                                                    double porcentajeTiempoTranscurridoCredito = 0;
                                                    
                                                    compBO.setComprobanteFiscal(item);
                                                    
                                                    Cliente cliente = null;
                                                    try{ cliente = new ClienteBO(item.getIdCliente(),user.getConn()).getCliente(); }catch(Exception ex){}
                                                    if (cliente!=null)
                                                        clienteNombre = cliente.getRazonSocial();
                                                    
                                                    boolean isFacturaFromPedido = false;
                                                    int idPedidoParent = -1;
                                                    String folioPedidoParent ="";
                                                    SgfensPedido pedidoDto = null;
                                                    {
                                                        SgfensPedido[] facturaFromPedidos = new SgfensPedidoDaoImpl(user.getConn()).findWhereIdComprobanteFiscalEquals(item.getIdComprobanteFiscal());
                                                        if (facturaFromPedidos!=null){
                                                            if (facturaFromPedidos.length>0){
                                                                isFacturaFromPedido=true;
                                                                idPedidoParent = facturaFromPedidos[0].getIdPedido();
                                                                folioPedidoParent = facturaFromPedidos[0].getFolioPedido();
                                                                pedidoDto = facturaFromPedidos[0]; 
                                                                pedidoBO.setPedido(pedidoDto);
                                                            }
                                                        }
                                                    }
                                                    
                                                    Folios foliosDto = null;
                                                    if (item.getIdFolio()>0){
                                                        foliosDto = new FoliosBO(item.getIdFolio(), user.getConn()).getFolios();
                                                        if (foliosDto!=null)
                                                            serie = foliosDto.getSerie();
                                                    }
                                                    folio = StringManage.getValidString(item.getFolioGenerado());
                                                    
                                                    fechaEmision =  DateManage.formatDateToNormal(item.getFechaCaptura());
                                                    
                                                    double saldo = 0;
                                                    double importe = item.getImporteNeto();
                                                    if (isFacturaFromPedido && pedidoDto != null){
                                                        saldo = pedidoDto.getBonificacionDevolucion()>0? (pedidoDto.getTotal() - pedidoDto.getAdelanto() - pedidoDto.getSaldoPagado()):(pedidoDto.getTotal() + Math.abs(pedidoDto.getBonificacionDevolucion()) - pedidoDto.getAdelanto() - pedidoDto.getSaldoPagado());
                                                    }else{
                                                        //saldo = item!=null?(item.getImporteNeto() - cobranzaAbonoBO.getSaldoPagadoComprobanteFiscal(item.getIdComprobanteFiscal()).doubleValue()):0;
                                                        saldo = item!=null?(item.getImporteNeto() - item.getSaldoPagado()):0;
                                                    }
                                                    if (saldo<0 && saldo>-0.001)
                                                        saldo = 0;
                                                    importeStr = formatMoneda.format(importe);
                                                    saldoStr = formatMoneda.format(saldo);
                                                    
                                                    sumaSaldo += saldo;
                                                    sumaImporte += importe;
                                                    
                                                    if (pedidoDto!=null){
                                                        diasCredito = pedidoBO.calculaDiasCredito();
                                                        diasMora = pedidoBO.calculaDiasMora();
                                                        porcentajeTiempoTranscurridoCredito = pedidoBO.calculaPorcentajeTranscurridoCredito();
                                                    }else{
                                                        diasCredito = compBO.calculaDiasCredito();
                                                        diasMora = compBO.calculaDiasMora();
                                                        porcentajeTiempoTranscurridoCredito = compBO.calculaPorcentajeTranscurridoCredito();
                                                    }
                                                    
                                                    String estatusFinanciero = "Activa";
                                                    if (saldo<=0){
                                                        estatusFinanciero = "Pagada";
                                                    }
                                                    
                                                    String colorSemaforo = "green";
                                                    String nombreSemaforo =  !estatusFinanciero.equals("Pagada")?"En Tiempo":"Pagado";
                                                    if (item.getIdEstatus()==EstatusComprobanteBO.ESTATUS_CANCELACION
                                                            || item.getIdEstatus()==EstatusComprobanteBO.ESTATUS_CANCELADA){
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
                                                    }
                                        %>
                                        <tr>
                                            <td>
                                                <input type="checkbox" name="check_id_factura" value="<%= id %>" />
                                            </td>
                                            <td><%= id %></td>
                                            <td><%= folio %></td>
                                            <td><%= serie %></td>
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
                                                <% if (isFacturaFromPedido){ %>
                                                <a href="#" onclick="mostrarMensajeCobranzaPedido(<%=idPedidoParent %>,'<%=folioPedidoParent %>');"><img src="../../images/icon_ventas1.png" alt="consultar cobranza" class="help" title="Cobranza" /></a>
                                                <% }else {%>
                                                <a href="#"
                                                   onclick="abrirLinkEnParent('../catCobranzaAbono/catCobranzaAbono_list.jsp?idComprobanteFiscal=<%=item.getIdComprobanteFiscal() %>');"
                                                   ><img src="../../images/icon_ventas1.png" alt="consultar cobranza" class="help" title="Cobranza" /></a>
                                                <% } %>
                                                &nbsp;&nbsp;
                                                
                                                <!-- PDF / Vista Previa -->
                                                <!--<a href="../../jsp/reportesExportar/previewCfdPdf.jsp?idComprobanteFiscal=<%=item.getIdComprobanteFiscal() %>&versionCfd=30.2" id="btn_show_cfdi" title="Previsualizar CFDI" class="modalbox_iframe">-->
                                                <a href="#" onclick="modalboxOnParent('../../jsp/reportesExportar/previewCfdPdf.jsp?idComprobanteFiscal=<%=item.getIdComprobanteFiscal() %>&versionCfd=30.2');" title="Previsualizar CFDI">
                                                    <img src="../../images/icon_consultar.png" alt="Mostrar CFDI" class="help" title="Previsualizar CFDI"/>
                                                </a>
                                                &nbsp;&nbsp;
                                                
                                                <!-- Adjuntos -->
                                                <!--<a href="../../jsp/adjuntos/consultaAdjuntos.jsp?idComprobanteFiscal=<%=item.getIdComprobanteFiscal() %>" title="Adjuntos" class="modalbox_iframe">-->
                                                <a href="#" onclick="modalboxOnParent('../../jsp/adjuntos/consultaAdjuntos.jsp?idComprobanteFiscal=<%=item.getIdComprobanteFiscal() %>');" title="Adjuntos">
                                                    <img src="../../images/sar_adjunto_varios.png" alt="Adjuntos" class="help" title="Adjuntos"/>
                                                </a>
                                                &nbsp;&nbsp;
                                                
                                                <!-- Log Notas/Comentarios -->
                                                <!--<a href="../../jsp/notas/notas_list.jsp?idComprobanteFiscal=<%=item.getIdComprobanteFiscal() %>" title="Log Notas"
                                                    class="modalbox_iframe">-->
                                                <a href="#" onclick="modalboxOnParent('../../jsp/notas/notas_list.jsp?idComprobanteFiscal=<%=item.getIdComprobanteFiscal() %>');" title="Log Notas">
                                                    <img src="../../images/icon_notas.png" alt="Log Notas" class="help" title="Log Notas"/>
                                                </a>
                                                &nbsp;&nbsp;
                                                    
                                                <% if (item.getIdEstatus()==EstatusComprobanteBO.ESTATUS_ENTREGADA) {%>
                                                <!--Cancelar-->
                                                <a href="#" onclick="confirmarCancelarComprobanteFiscal(<%=item.getIdComprobanteFiscal()%>,'<%=item.getUuid() %>');"><img src="../../images/icon_delete.png" alt="cancelar" class="help" title="Cancelar" /></a>
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
                                            <td colspan="6" style="text-align: right;"><b>Subtotales: </b></td>
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
                                <jsp:param name="idReport" value="<%= ReportBO.CXC_FACTURAS_LIST_REPORT %>" />
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