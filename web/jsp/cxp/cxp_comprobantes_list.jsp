<%-- 
    Document   : cxp_comprobantes_list.jsp
    Created on : 13/04/2015 04:58:22 PM
    Author     : ISCesarMartinez poseidon24@hotmail.com
--%>
<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.ValidacionXmlBO"%>
<%@page import="java.io.File"%>
<%@page import="com.tsp.sct.bo.FoliosBO"%>
<%@page import="com.tsp.sct.dao.dto.Folios"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.dao.dto.CxpComprobanteFiscal"%>
<%@page import="com.tsp.sct.dao.dto.CxpValeAzul"%>
<%@page import="com.tsp.sct.bo.CxpComprobanteFiscalBO"%>
<%@page import="com.tsp.sct.bo.CxpValeAzulBO"%>
<%@page import="com.tsp.sct.dao.dto.VistaCxp"%>
<%@page import="com.tsp.sct.bo.VistaCxpBO"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
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
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String buscar_serie = request.getParameter("q_serie")!=null? new String(request.getParameter("q_serie").getBytes("ISO-8859-1"),"UTF-8") :"";
    String buscar_folio = request.getParameter("q_folio")!=null? new String(request.getParameter("q_folio").getBytes("ISO-8859-1"),"UTF-8") :"";
    String buscar_emisor = request.getParameter("q_emisor")!=null? new String(request.getParameter("q_emisor").getBytes("ISO-8859-1"),"UTF-8") :"";
    String buscar_idestatus_pago = request.getParameter("q_idestatus_pago")!=null?request.getParameter("q_idestatus_pago"):"";
    String buscar_tipo = request.getParameter("q_tipo")!=null?request.getParameter("q_tipo"):"";
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
            strWhereRangoFechas="(CAST(FECHA_HORA_SELLO AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"' ";
            strWhereRangoFechas+= " OR CAST(FECHA_HORA_CONTROL AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"' )";
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas="(FECHA_HORA_SELLO  >= '"+buscar_fechamin+"' OR FECHA_HORA_CONTROL >= '"+buscar_fechamin+"')";
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas="(FECHA_HORA_SELLO  <= '"+buscar_fechamax+"' OR FECHA_HORA_CONTROL <= '"+buscar_fechamax+"')";
        }
    }
    
    String filtroBusqueda = "";
    
    if (!buscar.trim().equals(""))
        filtroBusqueda += " AND (ID_CXP_COMPROBANTE_FISCAL LIKE '%" + buscar + "%' OR ID_CXP_VALE_AZUL LIKE '%" + buscar + "%' )";
    
    if (!buscar_serie.trim().equals("")){
        filtroBusqueda += " AND SERIE LIKE '%" + buscar_serie +"%'";
    }
    
    if (!buscar_folio.trim().equals("")){
        filtroBusqueda += " AND (FOLIO LIKE '%" + buscar_folio +"%' OR FOLIO_GENERADO LIKE '%" + buscar_folio +"%') ";
    }
    
    if (!buscar_emisor.trim().equals("")){
        filtroBusqueda += " AND EMISOR_NOMBRE LIKE '%" + buscar_emisor +"%'";
    }
    
    if (!buscar_idsucursal.trim().equals("")){
        filtroBusqueda += " AND (cf_id_empresa=" + buscar_idsucursal +" OR id_empresa=" + buscar_idsucursal +") ";
    }
    
    if (!strWhereRangoFechas.trim().equals("")){
        filtroBusqueda += " AND " + strWhereRangoFechas;
    }
       
    if (!buscar_idestatus_pago.trim().equals("")){
        if (buscar_idestatus_pago.trim().equals("1")){
            //Activa
            filtroBusqueda += " AND ( (total - cf_importe_pagado)>0 OR (importe-importe_pagado)>0 ) ";
            filtroBusqueda += " AND (cf_id_estatus=1 OR ID_ESTATUS=1) ";
        }
        if (buscar_idestatus_pago.trim().equals("2")){
            //Pagada
            filtroBusqueda += " AND ( (total - cf_importe_pagado)<=0 OR (importe-importe_pagado)<=0 ) ";
        }
        if (buscar_idestatus_pago.trim().equals("3")){
            //Cancelada
            filtroBusqueda += " AND (cf_id_estatus=2 OR ID_ESTATUS=2) ";
        }
    }
    if (!buscar_tipo.trim().equals("")){
        if (buscar_tipo.trim().equals("1")){
            //Comprobante Fiscal
            filtroBusqueda += " AND ID_CXP_COMPROBANTE_FISCAL IS NOT NULL ";
        }
        if (buscar_tipo.trim().equals("2")){
            //Vale Azul
            filtroBusqueda += " AND ID_CXP_VALE_AZUL IS NOT NULL ";
        }
    }
    
    int idCxpComprobanteFiscal = -1;
    try{ idCxpComprobanteFiscal = Integer.parseInt(request.getParameter("id_cxp_comprobante_fiscal")); }catch(NumberFormatException e){}
    int idCxpValeAzul = -1;
    try{ idCxpValeAzul = Integer.parseInt(request.getParameter("id_cxp_vale_azul")); }catch(NumberFormatException e){}
    
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
    
     VistaCxpBO vistaCxpBO = new VistaCxpBO(user.getConn());
     VistaCxp[] vistaCxpDtos = new VistaCxp[0];
     VistaCxpBO.VistaCxPTotalPorPagar cxpTotalPorPagar = vistaCxpBO.getTotalesPorPagar(idEmpresa);
     try{
         limiteRegistros = vistaCxpBO.findCxp(idCxpComprobanteFiscal, idCxpValeAzul, idEmpresa , 0, 0, filtroBusqueda).length;
         
          if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        vistaCxpDtos = vistaCxpBO.findCxp(idCxpComprobanteFiscal, idCxpValeAzul, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     /*
    * Datos de catálogo
    */
     //Vale Azul
    String urlTo = "../cxp/cxp_vale_azul_form.jsp";
    String paramName1 = "id_cxp_vale_azul";
     //Comprobante Fiscal
    String urlTo2 = "../catValidaXML/catValidaXML_form.jsp";
    String paramName2 = "id_cxp_comprobante_fiscal";
    
    String parametrosPaginacion="q="+buscar+"&q_serie="+buscar_serie+"&q_folio="+buscar_folio+"&q_emisor="+buscar_emisor+"&q_idestatus_pago="+buscar_idestatus_pago+"&q_tipo="+buscar_tipo+"&q_idsucursal="+buscar_idsucursal+"&q_fh_min="+fechaMin+"&q_fh_max="+fechaMax;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    
    NumberFormat formatMoneda = new DecimalFormat("$ ###,###,###,##0.00");
    
    double porPagarCF = cxpTotalPorPagar.getTotalComprobantesFiscales();
    double porPagarVA = cxpTotalPorPagar.getTotalValesAzules();
    double totalPendientePorPagar = porPagarCF + porPagarVA;
    String totalPendientePorPagarStr = formatMoneda.format(totalPendientePorPagar);
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
				var option = this.id === "q_fh_min" ? "minDate" : "maxDate",
					instance = $( this ).data( "datepicker" ),
					date = $.datepicker.parseDate(
						instance.settings.dateFormat ||
						$.datepicker._defaults.dateFormat,
						selectedDate, instance.settings );
				dates.not( this ).datepicker( "option", option, date );
			}
		});

            }
            
            function confirmarMarcarPagoComprobanteFiscal(idCxpComprobanteFiscal, UUID){
                apprise('¿Esta seguro que desea Marcar como Pagado el Comprobante de Cuentas por Pagar con UUID '+UUID+'?', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
                    function(r){
                        if(r){
                            // Usuario dio click 'Yes'
                            marcarPagoComprobanteFiscal(idCxpComprobanteFiscal);
                        }
                });
                
            }
            
            function marcarPagoComprobanteFiscal(idCxpComprobanteFiscal){
                if(idCxpComprobanteFiscal>=0){
                    $.ajax({
                        type: "POST",
                        url: "../cxp/cxp_comprobantes_list_ajax.jsp",
                        data: { mode: '1', id_cxp_comprobante_fiscal : idCxpComprobanteFiscal },
                        beforeSend: function(objeto){
                            $("#ajax_loading").html('<div style="">ESPERE, procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/info.png> <br/>Comprobante Marcado como Pagado exitosamente</center>',{'animate':true},
                                    function(r){
                                        location.href = "cxp_comprobantes_list.jsp?id_cxp_comprobante_fiscal="+idCxpComprobanteFiscal;
                                });
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                           }
                        }
                    });
                }
            }
            
            function confirmarMarcarPagoValeAzul(idCxpValeAzul, Serie, Folio){
                apprise('¿Esta seguro que desea Marcar como Pagado el Vale Azul de Serie/Folio '+Serie+' - ' +Folio + '?', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
                    function(r){
                        if(r){
                            // Usuario dio click 'Yes'
                            marcarPagoValeAzul(idCxpValeAzul);
                        }
                });
                
            }
            
            function marcarPagoValeAzul(idCxpValeAzul){
                if(idCxpValeAzul>=0){
                    $.ajax({
                        type: "POST",
                        url: "../cxp/cxp_comprobantes_list_ajax.jsp",
                        data: { mode: '2', id_cxp_vale_azul : idCxpValeAzul },
                        beforeSend: function(objeto){
                            $("#ajax_loading").html('<div style="">ESPERE, procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/info.png> <br/>Vale Azul Marcado como Pagado exitosamente</center>',{'animate':true},
                                    function(r){
                                        location.href = "cxp_comprobantes_list.jsp?id_cxp_vale_azul="+idCxpValeAzul;
                                });
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                           }
                        }
                    });
                }
            }
            
            function confirmarCancelarComprobanteFiscal(idCxpComprobanteFiscal, UUID){
                apprise('¿Esta seguro que desea cancelar el Comprobante de Cuentas por Pagar con UUID '+UUID+'?', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
                    function(r){
                        if(r){
                            // Usuario dio click 'Yes'
                            cancelarComprobanteFiscal(idCxpComprobanteFiscal);
                        }
                });
                
            }
            
            function cancelarComprobanteFiscal(idCxpComprobanteFiscal){
                if(idCxpComprobanteFiscal>=0){
                    $.ajax({
                        type: "POST",
                        url: "../cxp/cxp_comprobantes_list_ajax.jsp",
                        data: { mode: '3', id_cxp_comprobante_fiscal : idCxpComprobanteFiscal },
                        beforeSend: function(objeto){
                            $("#ajax_loading").html('<div style="">ESPERE, procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/info.png> <br/>Comprobante Marcado como Cancelado exitosamente</center>',{'animate':true},
                                    function(r){
                                        location.href = "cxp_comprobantes_list.jsp?id_cxp_comprobante_fiscal="+idCxpComprobanteFiscal;
                                });
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                           }
                        }
                    });
                }
            }
            
            function confirmarCancelarValeAzul(idCxpValeAzul, Serie, Folio){
                apprise('¿Esta seguro que desea cancelar el Vale Azul de Serie/Folio '+Serie+' - ' +Folio + '?', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
                    function(r){
                        if(r){
                            // Usuario dio click 'Yes'
                            cancelarValeAzul(idCxpValeAzul);
                        }
                });
                
            }
            
            function cancelarValeAzul(idCxpValeAzul){
                if(idCxpValeAzul>=0){
                    $.ajax({
                        type: "POST",
                        url: "../cxp/cxp_comprobantes_list_ajax.jsp",
                        data: { mode: '4', id_cxp_vale_azul : idCxpValeAzul },
                        beforeSend: function(objeto){
                            $("#ajax_loading").html('<div style="">ESPERE, procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/info.png> <br/>Vale Azul Marcado como Cancelado exitosamente</center>',{'animate':true},
                                    function(r){
                                        location.href = "cxp_comprobantes_list.jsp?id_cxp_vale_azul="+idCxpValeAzul;
                                });
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                           }
                        }
                    });
                }
            }
            
            /**
            * Funcion para invocar la descarga de XML en archivo ZIP
            * Recorre todos los checkbox con nombre: check_id_factura
            * y toma su valor para enviarlos como parametros GET separados por coma
            * a la JSP de descarga de archivos ZIP
            */
           function descargaZipFiles(){
               //alert("Se descarga zip");

               var query_string = '../../jsp/file/downloadFilesZip.jsp?mode=2';

               var idString ="";
               var idString2 ="";

               $("input[name=check_id_cf]").each(function(){
                   if(this.checked){
                       idString += this.value + ",";
                   }
               });
               $("input[name=check_id_vale]").each(function(){
                   if(this.checked){
                       idString2 += this.value + ",";
                   }
               });
               if (idString!=="" || idString2!==""){
                    if (idString!=="")
                        query_string += "&xml=true&id_comprobante_array="+idString;
                    if (idString2!=="")
                        query_string += "&id_vale_azul_array="+idString2;
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
                   
                   $('input[name=check_id_cf]').each( function() {
                       if($("input[name=check_master_id]:checked").length === 1){
                           this.checked = true;
                       } else {
                           this.checked = false;
                       }
                   });
                   
                   $('input[name=check_id_vale]').each( function() {
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
                    <h1>Cuentas por Pagar</h1>
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
                            <form action="cxp_comprobantes_list.jsp" id="search_form_advance" name="search_form_advance" method="post">
                                <p>
                                    Por Fecha de Emisión (CF)/Control (VA) <br/>
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
                                    <label>Serie:</label>
                                    <input type="text" style="width: 100px;" id="q_serie" name="q_serie"/>
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <label>Folio:</label>
                                    <input type="text" style="width: 100px;" id="q_folio" name="q_folio"/>
                                </p>
                                <br/>
                                
                                <p>
                                    <label>Emisor:</label><br/>
                                    <input type="text" style="width: 300px;" id="q_emisor" name="q_emisor"/>
                                </p>
                                <br/>
                                
                                <p>
                                    <label>Sucursal:</label><br/>
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
                                        <option value="2">VA - Vale Azul</option>
                                    </select>
                                </p>
                                <br/>
                                
                                <p>
                                    <label>Estatus Pago:</label><br/>
                                    <select id="q_idestatus_pago" name="q_idestatus_pago">
                                        <option></option>
                                        <option value="1">Activa</option>
                                        <option value="2">Pagada</option>
                                        <option value="3">Cancelada</option>
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
                                Total Pendientes por Pagar (Saldo): 
                                <label><%= totalPendientePorPagarStr %></label>
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
                                <img src="../../images/icon_cxp.png" alt="icon"/>
                                Cuentas por Pagar [CxP]
                            </span>
                            <div class="switch" style="width:600px">
                                <table width="600px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td>
                                                <!--
                                                <div id="search">
                                                <form action="cxp_comprobantes_list.jsp" id="search_form" name="search_form" method="get">
                                                        <input type="text" id="q" name="q" title="Buscar por ID/Folio/UUID" class="" style="width: 300px; float: left; "
                                                                value="<%=buscar%>"/>
                                                        <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                </form>
                                                </div>-->
                                                <span style="width: 300px;">&nbsp;</span>
                                            </td>
                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                            <td>
                                                <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Nuevo CFDI" 
                                                        style="float: right; width: 110px;" onclick="location.href='<%=urlTo2%>?acc=1'"/>
                                            </td>
                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                            <td>
                                                <input type="button" id="nuevo_1" name="nuevo_1" class="right_switch" value="Nuevo Vale Azul" 
                                                        style="float: right; width: 110px;" onclick="location.href='<%=urlTo%>'"/>
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
                                            <th><input type="checkbox" name="check_master_id" value="" /></th>
                                            <th>Tipo</th>
                                            <th>ID</th>
                                            <th>Folio</th>
                                            <th>Serie</th>
                                            <th>Fecha Emisión</th>
                                            <th>Emisor</th>
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
                                            CxpValeAzulBO cxpValeAzulBO = new CxpValeAzulBO(user.getConn());
                                            CxpComprobanteFiscalBO cxpComprobanteFiscalBO = new CxpComprobanteFiscalBO(user.getConn());
                                            double sumaImporte = 0;
                                            double sumaSaldo = 0;
                                            File archivoCfdi = null;
                                            String rutaArchivoCfdiEnc = null;
                                            for (VistaCxp itemVista : vistaCxpDtos){
                                                try{                                                    
                                                    CxpValeAzul cxpValeAzulDto = null;
                                                    CxpComprobanteFiscal cxpComprobanteFiscalDto = null;
                                                    if (itemVista.getIdCxpComprobanteFiscal()>0){
                                                        cxpComprobanteFiscalBO = new CxpComprobanteFiscalBO(itemVista.getIdCxpComprobanteFiscal(), user.getConn());
                                                        cxpComprobanteFiscalDto = cxpComprobanteFiscalBO.getCxpComprobanteFiscal();
                                                        
                                                        if (cxpComprobanteFiscalDto!=null){
                                                            try{
                                                                ValidacionXmlBO validacionXmlBO = new ValidacionXmlBO(cxpComprobanteFiscalDto.getIdValidacion(), user.getConn());
                                                                archivoCfdi = validacionXmlBO.getComprobanteFile();
                                                                rutaArchivoCfdiEnc = java.net.URLEncoder.encode(archivoCfdi.getAbsolutePath(), "UTF-8");
                                                            }catch(Exception ex){
                                                                ex.printStackTrace();
                                                            }
                                                        }
                                                    }else{
                                                        cxpValeAzulBO = new CxpValeAzulBO(itemVista.getIdCxpValeAzul(), user.getConn());
                                                        cxpValeAzulDto = cxpValeAzulBO.getCxpValeAzul();
                                                    }
                                                    
                                                    String saldoStr = "";
                                                    double saldo = 0;
                                                    double importe = 0;
                                                    boolean cancelado = false;
                                                    String folio = "";
                                                    String serie = "";
                                                    int idRow = 0;
                                                    Date fechaEmision = null;
                                                    String nombreEmisor =  "-No identificado-";
                                                    String param = "";
                                                    if (cxpComprobanteFiscalDto!= null){
                                                        idRow = cxpComprobanteFiscalDto.getIdCxpComprobanteFiscal();
                                                        importe = cxpComprobanteFiscalDto.getTotal();
                                                        saldo = cxpComprobanteFiscalDto.getTotal() - cxpComprobanteFiscalDto.getImportePagado();
                                                        cancelado = cxpComprobanteFiscalDto.getIdEstatus() == 2;
                                                        folio = StringManage.getValidString(cxpComprobanteFiscalDto.getFolio());
                                                        serie = StringManage.getValidString(cxpComprobanteFiscalDto.getSerie());
                                                        fechaEmision = cxpComprobanteFiscalDto.getFechaHoraSello();
                                                        nombreEmisor = StringManage.getValidString(cxpComprobanteFiscalDto.getEmisorNombre());
                                                        param = paramName2 + "=" + cxpComprobanteFiscalDto.getIdCxpComprobanteFiscal();
                                                    }else{
                                                        idRow = cxpValeAzulDto.getIdCxpValeAzul();
                                                        importe = cxpValeAzulDto.getImporte();
                                                        saldo = cxpValeAzulDto.getImporte()- cxpValeAzulDto.getImportePagado();
                                                        cancelado = cxpValeAzulDto.getIdEstatus() == 2;
                                                        folio = StringManage.getValidString(cxpValeAzulDto.getFolioGenerado());
                                                        
                                                        //Buscamos información de Serie
                                                        Folios foliosDto = null;
                                                        if (cxpValeAzulDto.getIdFolio()>0){
                                                            foliosDto = new FoliosBO(cxpValeAzulDto.getIdFolio(), user.getConn()).getFolios();
                                                            if (foliosDto!=null)
                                                                serie = StringManage.getValidString(foliosDto.getSerie());
                                                        }
                                                        fechaEmision = cxpValeAzulDto.getFechaHoraControl();
                                                        param = paramName1 + "=" + cxpValeAzulDto.getIdCxpValeAzul();
                                                    }
                                                    
                                                    if (cancelado)//Si esta cancelado no requerimos saber su saldo
                                                            saldo = 0;
                                                    
                                                    if (saldo<0 && saldo>-0.001)
                                                        saldo = 0;
                                                    saldoStr = formatMoneda.format(saldo);
                                                    
                                                    sumaSaldo += saldo;
                                                    sumaImporte += importe;
                                                    
                                                    long diasCredito = 0;
                                                    long diasMora = 0;
                                                    double porcentajeTiempoTranscurridoCredito = 0;
                                                    if (cxpComprobanteFiscalDto!=null){
                                                        diasCredito = cxpComprobanteFiscalBO.calculaDiasCredito();
                                                        diasMora = cxpComprobanteFiscalBO.calculaDiasMora();
                                                        porcentajeTiempoTranscurridoCredito = cxpComprobanteFiscalBO.calculaPorcentajeTranscurridoCredito();
                                                    }else{
                                                        diasCredito = cxpValeAzulBO.calculaDiasCredito();
                                                        diasMora = cxpValeAzulBO.calculaDiasMora();
                                                        porcentajeTiempoTranscurridoCredito = cxpValeAzulBO.calculaPorcentajeTranscurridoCredito();
                                                    }
                                                    
                                                    String estatusFinanciero = "Activa";
                                                    if (saldo<=0){
                                                        estatusFinanciero = "Pagada";
                                                    }
                                                    
                                                    String colorSemaforo = "green";
                                                    String nombreSemaforo =  !estatusFinanciero.equals("Pagada")?"En Tiempo":"Pagado";
                                                    if (cancelado) {
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
                                                <% if (cxpComprobanteFiscalDto!=null){ %>
                                                <input type="checkbox" name="check_id_cf" value="<%=cxpComprobanteFiscalDto.getIdCxpComprobanteFiscal()%>" />
                                                <% }else {%>
                                                <input type="checkbox" name="check_id_vale" value="<%=cxpValeAzulDto.getIdCxpValeAzul() %>" />
                                                <% }%>
                                            </td>
                                            <td>
                                                <% if (cxpComprobanteFiscalDto!=null){ %>
                                                <span alt="Comprobante Fiscal" class="help" title="Comprobante Fiscal">CF</span>
                                                <% }else {%>
                                                <span alt="Vale Azul" class="help" title="Vale Azul">VA</span>
                                                <% }%>
                                            </td>
                                            <td><%= idRow %></td>
                                            <td><%= folio %></td>
                                            <td><%= serie %>
                                            <td><%= DateManage.formatDateToNormal(fechaEmision) %></td>
                                            <td>
                                                <%= nombreEmisor %>
                                            </td>
                                            <td align="right"><%= importe %></td>
                                            <td align="right"><%= saldoStr %></td>
                                            <td align="right"><%= diasCredito %></td>
                                            <td><%= estatusFinanciero %></td>
                                            <td align="center">
                                                <img src="../../images/semaforo/icon_s_<%=colorSemaforo%>.png" width="20px" height="20px"
                                                     alt="<%=nombreSemaforo%>" class="help" title="<%=nombreSemaforo%>"/>
                                            </td>
                                            <td>
                                                <% if (saldo>0 && !cancelado){%>
                                                    <!-- Marcar Pagada -->                                                    
                                                    <% if (cxpComprobanteFiscalDto!=null){ %>
                                                    <a href="#" onclick="confirmarMarcarPagoComprobanteFiscal(<%=cxpComprobanteFiscalDto.getIdCxpComprobanteFiscal()%>,'<%=cxpComprobanteFiscalDto.getCfdiUuid() %>');"><img src="../../images/icon_accept.png" alt="marcar pago" class="help" title="Marcar como Pagada" /></a>
                                                    <%}else{%>
                                                    <a href="#" onclick="confirmarMarcarPagoValeAzul(<%=cxpValeAzulDto.getIdCxpValeAzul()%>,'<%=serie%>','<%=folio%>');"><img src="../../images/icon_accept.png" alt="marcar pago" class="help" title="Marcar como Pagada" /></a>
                                                    <%}%>
                                                <%}else{ out.print("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"); } %>
                                                &nbsp;&nbsp;
                                                
                                                <!-- PDF / Vista Previa -->
                                                <% if (cxpComprobanteFiscalDto!=null && rutaArchivoCfdiEnc!=null){ %>
                                                <a href="../../jsp/reportesExportar/previewExternoCfdPdf.jsp?ruta_archivo=<%=rutaArchivoCfdiEnc%>&versionCfd=3.2" id="btn_show_cfdi" title="Previsualizar CFDI"
                                                    class="modalbox_iframe">
                                                    <img src="../../images/icon_consultar.png" alt="Mostrar CFDI" class="help" title="Previsualizar CFDI"/>
                                                </a>
                                                <%}else{%>
                                                <a href="../../jsp/cxp/previewPDFValeAzul.jsp?<%=paramName1 %>=<%=cxpValeAzulDto.getIdCxpValeAzul() %>" id="btn_show_cfdi" title="PDF Vale Azul"
                                                    class="modalbox_iframe">
                                                    <img src="../../images/icon_consultar.png" alt="PDF Vale Azul" class="help" title="PDF Vale Azul"/>
                                                </a>
                                                <%}%>
                                                &nbsp;&nbsp;
                                                
                                                <!-- Adjuntos -->
                                                <a href="../../jsp/adjuntos/consultaAdjuntos.jsp?<%=param %>" title="Adjuntos"
                                                    class="modalbox_iframe">
                                                    <img src="../../images/sar_adjunto_varios.png" alt="Adjuntos" class="help" title="Adjuntos"/>
                                                </a>
                                                &nbsp;&nbsp;
                                                
                                                <!-- Log Notas/Comentarios -->
                                                <a href="../../jsp/notas/notas_list.jsp?<%=param %>" title="Log Notas"
                                                    class="modalbox_iframe">
                                                    <img src="../../images/icon_notas.png" alt="Log Notas" class="help" title="Log Notas"/>
                                                </a>
                                                &nbsp;&nbsp;
                                                    
                                                <% if (!cancelado) {%>
                                                    <!--Cancelar-->
                                                    <% if (cxpComprobanteFiscalDto!=null){ %>
                                                    <a href="#" onclick="confirmarCancelarComprobanteFiscal(<%=cxpComprobanteFiscalDto.getIdCxpComprobanteFiscal()%>,'<%=cxpComprobanteFiscalDto.getCfdiUuid() %>');"><img src="../../images/icon_delete.png" alt="cancelar" class="help" title="Cancelar" /></a>
                                                    <%}else{%>
                                                    <a href="#" onclick="confirmarCancelarValeAzul(<%=cxpValeAzulDto.getIdCxpValeAzul()%>,'<%=serie%>','<%=folio%>');"><img src="../../images/icon_delete.png" alt="cancelar" class="help" title="Cancelar" /></a>
                                                    <%}%>
                                                    &nbsp;&nbsp;
                                                <%}%>
                                                
                                            </td>
                                        </tr>
                                        <%      }catch(Exception ex){
                                                    ex.printStackTrace();
                                                }
                                            } 
                                        %>
                                        <tr style="font-size: 14px;">
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
                                <jsp:param name="idReport" value="<%= ReportBO.CXP_LIST_REPORT %>" />
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