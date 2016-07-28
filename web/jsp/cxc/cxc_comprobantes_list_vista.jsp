<%-- 
    Document   : cxc_comprobantes_list.jsp
    Created on : 06-apr-2015, 12:13:49
    Author     : ISCesarMartinez poseidon24@hotmail.com
--%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.SGEstatusPedidoBO"%>
<%@page import="com.tsp.sct.dao.dto.VistaCxc"%>
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
    String buscar_tipo = request.getParameter("q_tipo")!=null?request.getParameter("q_tipo"):"";
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
            strWhereRangoFechas="(CAST(FECHA_CAPTURA AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"' ";
            strWhereRangoFechas+= " OR CAST(FECHA_PEDIDO AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"' )";
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas="(FECHA_CAPTURA  >= '"+buscar_fechamin+"' OR FECHA_PEDIDO >= '"+buscar_fechamin+"')";
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas="(FECHA_CAPTURA  <= '"+buscar_fechamax+"' OR FECHA_PEDIDO <= '"+buscar_fechamax+"')";
        }
    }
    
    String filtroBusqueda = "";
    
    //Solo mostraremos Registros con estatus de Comprobante Fiscal validos
    filtroBusqueda = " AND IF(ID_PEDIDO IS NOT NULL, TRUE, C_ID_ESTATUS='3' OR ID_ESTATUS_PEDIDO ='4' )";
    
    //Ademas solo Comprobantes Fiscales de Tipo FACTURA
    filtroBusqueda += " AND IF(C_ID_COMPROBANTE_FISCAL IS NOT NULL, ID_TIPO_COMPROBANTE='2' OR ID_TIPO_COMPROBANTE='41', TRUE ) ";
    
    if (!buscar_folio.trim().equals("")){
        filtroBusqueda += " AND (FOLIO_GENERADO LIKE '%" + buscar_folio +"%' OR FOLIO_PEDIDO LIKE '%" + buscar_folio +"%') ";
    }
    
    if (!buscar_idcliente.trim().equals("")){
        filtroBusqueda += " AND (C_ID_CLIENTE='" + buscar_idcliente +"' OR ID_CLIENTE='" + buscar_idcliente +"') ";
    }
    
    if (!buscar_idsucursal.trim().equals("")){
        filtroBusqueda += " AND (C_ID_EMPRESA=" + buscar_idsucursal +" OR ID_EMPRESA=" + buscar_idsucursal +") ";
    }
    
    if (!buscar_tipo.trim().equals("")){
        if (buscar_tipo.trim().equals("1")){
            //Comprobante Fiscal
            filtroBusqueda += " AND C_ID_COMPROBANTE_FISCAL IS NOT NULL ";
        }
        if (buscar_tipo.trim().equals("2")){
            //Pedido
            filtroBusqueda += " AND ID_PEDIDO IS NOT NULL ";
        }
    }
    
    if (!buscar_idestatus.trim().equals("")){
        if (buscar_idestatus.trim().equals("1")){
            //Activa
            filtroBusqueda += " AND ((C_ID_ESTATUS!=4 AND C_ID_ESTATUS!=5) OR ID_ESTATUS_PEDIDO!=3) ";
        }
        if (buscar_idestatus.trim().equals("2")){
            //Cancelada
            filtroBusqueda += " AND ((C_ID_ESTATUS=4 OR C_ID_ESTATUS=5) OR ID_ESTATUS_PEDIDO=3) ";
        }
    }
    
    if (!strWhereRangoFechas.trim().equals("")){
        filtroBusqueda += " AND " + strWhereRangoFechas;
    }
       
    
    
    int idComprobanteFiscal = -1;
    try{ idComprobanteFiscal = Integer.parseInt(request.getParameter("idComprobanteFiscal")); }catch(NumberFormatException e){}
    int idPedido = -1;
    try{ idPedido = Integer.parseInt(request.getParameter("idPedido")); }catch(NumberFormatException e){}
    
    boolean filtroSoloConAdeudo = false;
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
    
    VistaCxcBO vistaCxCBO = new VistaCxcBO(user.getConn());
    VistaCxc[] vistaCxcDto = new VistaCxc[0];
    try{
        limiteRegistros = vistaCxCBO.findCxc(idComprobanteFiscal, idPedido, idEmpresa , 0, 0, filtroSoloConAdeudo, filtroBusqueda).length;
         
        paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        vistaCxcDto = vistaCxCBO.findCxc(idComprobanteFiscal, idPedido, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroSoloConAdeudo, filtroBusqueda);

    }catch(Exception ex){
        ex.printStackTrace();
    }
     
     /*
    * Datos de catálogo
    */
    String paramName = "idComprobanteFiscal";
    String paramName2 = "idPedido";
    String parametrosPaginacion="q_idcliente="+buscar_idcliente+"&q_idestatus="+buscar_idestatus+"&q_idsucursal="+buscar_idsucursal+"&q_fh_min="+fechaMin+"&q_fh_max="+fechaMax+"&q_folio="+buscar_folio+"&q_tipo="+buscar_tipo;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    
    Empresa empresaActual =  new EmpresaBO(idEmpresa, user.getConn()).getEmpresa();
    //EmpresaPermisoAplicacion permisoAplicacion = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaActual.getIdEmpresaPadre());
    
    NumberFormat formatMoneda = new DecimalFormat("$ ###,###,###,##0.00");
    
    double totalPendientePorCobrar = vistaCxCBO.getTotalPorCobrar(idEmpresa);
    String totalPendientePorCobrarStr = formatMoneda.format(totalPendientePorCobrar);
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
            
            function confirmarCancelarComprobanteFiscal(idComprobanteFiscal, UUID){
                apprise('¿Esta seguro que desea cancelar el Comprobante Fiscal con UUID '+UUID+'?', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
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
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                    function(r){
                                        location.href = "cxc_comprobantes_list.jsp?idComprobanteFiscal="+idComprobanteFiscal;
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
            
            function confirmarCancelarPedido(idPedido, folioPedido){
                apprise('¿Esta seguro que desea cancelar el pedido con Folio '+folioPedido+'?', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
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
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                    function(r){
                                        location.href = "cxc_comprobantes_list.jsp?idPedido="+idPedido;
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
                apprise('<center>El registro esta relacionado a un pedido, la cobranza se debe registrar en él.'
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
            
            /**
            * Funcion para invocar la descarga de XML en archivo ZIP
            * Recorre todos los checkbox con nombre: check_id_factura
            * y toma su valor para enviarlos como parametros GET separados por coma
            * a la JSP de descarga de archivos ZIP
            */
           function descargaZipFiles(){
               //alert("Se descarga zip");

               var query_string = '../../jsp/file/downloadFilesZip.jsp?mode=3';

               var idString ="";
               var idString2 ="";

               $("input[name=check_id_factura]").each(function(){
                   //$('input[type=checkbox]').each( function() {
                   if(this.checked){
                       idString += this.value + ",";
                   }
               });
               $("input[name=check_id_pedido]").each(function(){
                   if(this.checked){
                       idString2 += this.value + ",";
                   }
               });
               if (idString!=="" || idString2!==""){
                   if (idString!=="")
                        query_string += "&xml=true&id_comprobante_array="+idString;
                   if (idString2!=="")
                        query_string += "&id_pedido_array="+idString2;
                   //alert("cadena: "+query_string);

                   window.open(query_string, "Descarga de archivos", "location=0,status=0,scrollbars=0,menubar=0,resizable=0,width=400,height=400");
               }else{
                   apprise('No se selecciono ningun archivo a descargar',{
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
                   
                   $('input[name=check_id_pedido]').each( function() {
                       if($("input[name=check_master_id]:checked").length === 1){
                           this.checked = true;
                       } else {
                           this.checked = false;
                       }
                   });
                   
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
                                    Por Fecha de Emisión <br/>
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
                                    <label>Tipo Comprobante:</label><br/>
                                    <select id="q_tipo" name="q_tipo">
                                        <option>Todos</option>
                                        <option value="1">CF - Comprobante Fiscal</option>
                                        <option value="2">P - Pedido</option>
                                    </select>
                                </p>
                                <br/>
                                
                                <p>
                                <label>Estatus:</label><br/>
                                <select id="q_idestatus" name="q_idestatus">
                                    <option></option>
                                    <option value="1">Activa</option>
                                    <option value="2">Cancelada</option>
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
                                Total Pendientes por Cobrar (Saldo): 
                                <label><%= totalPendientePorCobrarStr %></label>
                            </span>
                            <br/>
                        </div>
                      </div>
                      <!-- End right column window -->
                    </div>
                    <br class="clear"/>
                    
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_cxc.png" alt="icon"/>
                                Cuentas por Cobrar [CxC]
                            </span>
                            
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <form id="form_data" name="form_data" action="" method="post">
                                <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th><input type="checkbox" name="check_master_id" value="" /></th>
                                            <th>Tipo</th>
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
                                            for (VistaCxc item : vistaCxcDto) {
                                                try{
                                                    String tipo = "";
                                                    int id = 0;
                                                    String folio = "";
                                                    String serie = " - ";
                                                    String fechaEmision = "";
                                                    String clienteNombre = "Sin cliente asignado";
                                                    String importeStr = "";
                                                    String saldoStr = "";
                                                    long diasCredito = 0;
                                                    long diasMora = 0;
                                                    double porcentajeTiempoTranscurridoCredito = 0;
                                                    
                                                    int idFoliosSerie = 0;
                                                    int idCliente = 0;
                                                    double importe = 0;
                                                    double saldo = 0;
                                                    if (item.getCIdComprobanteFiscal()>0){
                                                        //Comprobante Fiscal
                                                        compBO = new ComprobanteFiscalBO(item.getCIdComprobanteFiscal(), user.getConn());
                                                        tipo = "CF";
                                                        id = item.getCIdComprobanteFiscal();
                                                        folio = StringManage.getValidString(item.getFolioGenerado());
                                                        idFoliosSerie = item.getIdFolio();
                                                        fechaEmision =  DateManage.formatDateToNormal(item.getFechaCaptura());
                                                        idCliente = item.getCIdCliente();
                                                        importe = item.getImporteNeto();
                                                        if (item.getIdPedido()>0){
                                                            //Comprobante Fiscal dependiente de Pedido
                                                            tipo ="CF+P";
                                                            pedidoBO = new SGPedidoBO(item.getIdPedido(), user.getConn());
                                                            saldo = item.getBonificacionDevolucion()>0? (item.getTotal() - item.getSaldoPagado()):(item.getTotal() + Math.abs(item.getBonificacionDevolucion()) - item.getSaldoPagado());
                                                            diasCredito = pedidoBO.calculaDiasMora();
                                                            diasMora = pedidoBO.calculaDiasMora();
                                                            porcentajeTiempoTranscurridoCredito = pedidoBO.calculaPorcentajeTranscurridoCredito();
                                                        }else{
                                                            //Comprobante Fiscal SIN pedido
                                                            saldo = item!=null?(item.getImporteNeto() - cobranzaAbonoBO.getSaldoPagadoComprobanteFiscal(item.getCIdComprobanteFiscal()).doubleValue()):0;
                                                            diasCredito = compBO.calculaDiasMora();
                                                            diasMora = compBO.calculaDiasMora();
                                                            porcentajeTiempoTranscurridoCredito = compBO.calculaPorcentajeTranscurridoCredito();
                                                        }
                                                    }else if (item.getIdPedido()>0){
                                                        //Pedido sin CompFiscal
                                                        pedidoBO = new SGPedidoBO(item.getIdPedido(), user.getConn());
                                                        tipo = "P";
                                                        id = item.getIdPedido();
                                                        folio = StringManage.getValidString(item.getFolioPedido());
                                                        idFoliosSerie = 0; //Los Pedidos no tienen Series
                                                        fechaEmision =  DateManage.formatDateToNormal(item.getFechaPedido());
                                                        idCliente = item.getIdCliente();
                                                        importe = item.getTotal();
                                                        saldo = item.getBonificacionDevolucion()>0? (item.getTotal() - item.getSaldoPagado()):(item.getTotal() + Math.abs(item.getBonificacionDevolucion()) - item.getSaldoPagado());
                                                        diasCredito = pedidoBO.calculaDiasMora();
                                                        diasMora = pedidoBO.calculaDiasMora();
                                                        porcentajeTiempoTranscurridoCredito = pedidoBO.calculaPorcentajeTranscurridoCredito();
                                                    }
                                                    
                                                    if (idFoliosSerie>0){
                                                        Folios foliosDto = null;
                                                        foliosDto = new FoliosBO(idFoliosSerie, user.getConn()).getFolios();
                                                        if (foliosDto!=null)
                                                            serie = foliosDto.getSerie();
                                                    }
                                                    
                                                    if (idCliente>0){
                                                        Cliente cliente = null;
                                                        try{ cliente = new ClienteBO(idCliente,user.getConn()).getCliente(); }catch(Exception ex){}
                                                        if (cliente!=null)
                                                            clienteNombre = cliente.getRazonSocial();
                                                    }
                                                    
                                                    importeStr = formatMoneda.format(importe);
                                                    saldoStr = formatMoneda.format(saldo);
                                                    
                                                    String estatusFinanciero = "Activa";
                                                    if (saldo<=0){
                                                        estatusFinanciero = "Pagada";
                                                    }
                                                    
                                                    String colorSemaforo = "green";
                                                    String nombreSemaforo =  !estatusFinanciero.equals("Pagada")?"En Tiempo":"Pagado";
                                                    boolean cancelada = false;
                                                    if (item.getCIdEstatus()==EstatusComprobanteBO.ESTATUS_CANCELACION
                                                            || item.getCIdEstatus()==EstatusComprobanteBO.ESTATUS_CANCELADA
                                                            || item.getIdEstatusPedido()==SGEstatusPedidoBO.ESTATUS_CANCELADO){
                                                        //Si esta cancelada
                                                        colorSemaforo = "grey";
                                                        nombreSemaforo = "Cancelada";
                                                        estatusFinanciero = "Cancelada";
                                                        cancelada = true;
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
                                                        sumaImporte += importe;
                                                        sumaSaldo += saldo;
                                                    }
                                                    
                                                    String getQuery;
                                                    if (tipo.equals("CF")){
                                                        getQuery = "idComprobanteFiscal=" + item.getCIdComprobanteFiscal();
                                                    }else if (tipo.equals("CF+P")){
                                                        getQuery = "idComprobanteFiscal=" + item.getCIdComprobanteFiscal()
                                                                +  "&idPedido="+item.getIdPedido();
                                                    }else {
                                                        getQuery = "idPedido="+item.getIdPedido();
                                                    }
                                        %>
                                        <tr>
                                            <td>
                                                <% if (item.getCIdComprobanteFiscal()>0){ %>
                                                <input type="checkbox" name="check_id_factura" value="<%=item.getCIdComprobanteFiscal()%>" />
                                                <% }else{%>
                                                <input type="checkbox" name="check_id_pedido" value="<%=item.getIdPedido()%>" />
                                                <% }%>
                                            </td>
                                            <td align="right">
                                                <% if (tipo.equals("CF")){ %>
                                                <span alt="Comprobante Fiscal" class="help" title="Comprobante Fiscal"><%=tipo%></span>
                                                <% }else if (tipo.equals("CF+P")){ %>
                                                <span alt="Pedido con Factura" class="help" title="Pedido con Factura"><%=tipo%></span>
                                                <% }else {%>
                                                <span alt="Pedido" class="help" title="Pedido"><%=tipo%></span>
                                                <% }%>
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
                                                <% if (item.getIdPedido()>0){ %>
                                                <a href="#" onclick="mostrarMensajeCobranzaPedido(<%=item.getIdPedido() %>,'<%=folio %>');"><img src="../../images/icon_ventas1.png" alt="consultar cobranza" class="help" title="Cobranza" /></a>
                                                <% }else {%>
                                                <a href="../catCobranzaAbono/catCobranzaAbono_list.jsp?idComprobanteFiscal=<%=item.getCIdComprobanteFiscal() %>"><img src="../../images/icon_ventas1.png" alt="consultar cobranza" class="help" title="Cobranza" /></a>
                                                <% } %>
                                                &nbsp;&nbsp;
                                                
                                                <!-- PDF / Vista Previa -->
                                                <% if (item.getCIdComprobanteFiscal()>0){ %>
                                                <a href="../../jsp/reportesExportar/previewCfdPdf.jsp?idComprobanteFiscal=<%=item.getCIdComprobanteFiscal() %>&versionCfd=30.2" id="btn_show_cfdi" title="Previsualizar CFDI"
                                                class="modalbox_iframe">
                                                    <img src="../../images/icon_consultar.png" alt="Mostrar CFDI" class="help" title="Previsualizar CFDI"/>
                                                </a>
                                                <% }else{ %>
                                                <a href="../../jsp/reportesExportar/previewPedidoPDF.jsp?idPedido=<%=item.getIdPedido() %>" id="btn_show_cfdi" title="Previsualizar Pedido"
                                                class="modalbox_iframe">
                                                    <img src="../../images/icon_consultar.png" alt="Mostrar Pedido" class="help" title="Previsualizar Pedido"/>
                                                </a>
                                                <% } %>
                                                &nbsp;&nbsp;
                                                
                                                <!-- Adjuntos -->
                                                <a href="../../jsp/adjuntos/consultaAdjuntos.jsp?<%=getQuery %>" title="Adjuntos"
                                                    class="modalbox_iframe">
                                                    <img src="../../images/sar_adjunto_varios.png" alt="Adjuntos" class="help" title="Adjuntos"/>
                                                </a>
                                                &nbsp;&nbsp;
                                                
                                                <!-- Log Notas/Comentarios -->
                                                <a href="../../jsp/notas/notas_list.jsp?<%=getQuery %>" title="Log Notas"
                                                    class="modalbox_iframe">
                                                    <img src="../../images/icon_notas.png" alt="Log Notas" class="help" title="Log Notas"/>
                                                </a>
                                                &nbsp;&nbsp;
                                                    
                                                <% if (!cancelada) {%>
                                                <!--Cancelar-->
                                                <% if (item.getCIdComprobanteFiscal()>0){ %>
                                                <a href="#" onclick="confirmarCancelarComprobanteFiscal(<%=item.getIdComprobanteFiscal()%>,'<%=item.getUuid() %>');"><img src="../../images/icon_delete.png" alt="cancelar" class="help" title="Cancelar" /></a>
                                                <% } else { %>
                                                <a href="#" onclick="confirmarCancelarPedido(<%=item.getIdPedido()%>,'<%=item.getFolioPedido() %>');"><img src="../../images/icon_delete.png" alt="cancelar" class="help" title="Cancelar" /></a>
                                                <% }%>
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
                                            <td colspan="7" style="text-align: right;"><b>Subtotales: </b></td>
                                            <td style="color: #0000cc; text-align: right;"><%=formatMoneda.format(sumaImporte)%> </td>
                                            <td style="color: #0000cc; text-align: right;"><%=formatMoneda.format(sumaSaldo)%> </td>
                                            <td colspan="4">&nbsp;</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </form>

                            <br class="clear"/>
                            <div id="div_descarga" name="div_descarga" class="switch" style="float: left;">
                                <input type="button" id="descarga" name="descarga" class="left_switch" value="Descarga Archivos" 
                                    style="float: left; "
                                    onclick="descargaZipFiles()"/>
                            </div>
                            
                            <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <jsp:param name="idReport" value="<%= ReportBO.CXC_LIST_REPORT %>" />
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