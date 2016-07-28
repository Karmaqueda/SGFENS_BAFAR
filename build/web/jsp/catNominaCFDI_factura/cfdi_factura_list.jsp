<%-- 
    Document   : cfdi_factura_list.jsp
    Created on : 13-dic-2012, 12:13:49
    Author     : ISCesarMartinez poseidon24@hotmail.com
--%>


<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.bo.NominaRegistroPatronalBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.tsp.sgfens.sesion.NominaComprobanteIdsSesion"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="com.tsp.sct.bo.NominaComprobanteDescripcionBO"%>
<%@page import="com.tsp.sct.dao.dto.NominaEmpleado"%>
<%@page import="com.tsp.sct.bo.NominaEmpleadoBO"%>
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
<jsp:useBean id="nominaIDsSesion" scope="session" class="com.tsp.sgfens.sesion.NominaComprobanteIdsSesion"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    //limpiamos de sesion los datos de id a generar nómina automática:
    //request.getSession().setAttribute("nominaIDsSesion", null);
    nominaIDsSesion.getIdsComprobantesNew();
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String buscar_idcliente = request.getParameter("q_idcliente")!=null?request.getParameter("q_idcliente"):"";
    String buscar_idestatuscomprobanteFiscal = request.getParameter("q_idestatuscomprobanteFiscal")!=null?request.getParameter("q_idestatuscomprobanteFiscal"):"";
    String buscar_idsucursal = request.getParameter("q_idsucursal")!=null?request.getParameter("q_idsucursal"):"";
    String buscar_idRegistroPatronal = request.getParameter("q_registro_patronal")!=null?request.getParameter("q_registro_patronal"):"";
    String buscar_fechamin = "";
    String buscar_fechamax = "";
    
    String periodoPago = request.getParameter("periodoPago")!=null? new String(request.getParameter("periodoPago").getBytes("ISO-8859-1"),"UTF-8") :"";
    
    System.out.println("_____________________periodo pago: "+periodoPago);    
    
    /*Filtro por rango de fechas de pago de inicio y fin*/
    String periodoPagoWhereRango = "";
    if(!periodoPago.trim().equals("") && !periodoPago.trim().equals("-1")){
        //separamos los rangos de fechas:
        StringTokenizer tokens = new StringTokenizer(periodoPago, " al ");        
        periodoPagoWhereRango += " AND (ID_COMPROBANTE_FISCAL IN (SELECT ID_CROMPROBANTE_FISCAL FROM NOMINA_COMPROBANTE_DESCRIPCION WHERE (FECHA_INICIAL_PAGO = '"+ tokens.nextToken() +"' AND FECHA_FIN_PAGO = '"+ tokens.nextToken() +"' ) ))";
    }      
    
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
    //filtroBusqueda = " AND (ID_ESTATUS='3' OR ID_ESTATUS ='4' OR ID_ESTATUS ='5')";
    filtroBusqueda = " AND (ID_ESTATUS='3' OR ID_ESTATUS ='4')";
    
    if (!buscar_idestatuscomprobanteFiscal.trim().equals("")){
        filtroBusqueda = " AND ID_ESTATUS='" + buscar_idestatuscomprobanteFiscal +"' ";
    }
    
    //Ademas solo Facturas de Tipo FACTURA
    filtroBusqueda += " AND ID_TIPO_COMPROBANTE='40' ";
    
    if (!buscar.trim().equals("")){
        String buscarNombreCompletoNominaEmpleado = buscar.replaceAll("\\s", "%");        
        filtroBusqueda += " AND (FOLIO_GENERADO LIKE '%" + buscar + "%' OR ID_COMPROBANTE_FISCAL LIKE '%" + buscar + "%' OR UUID LIKE '%" + buscar +
                "%' OR (ID_CLIENTE IN (SELECT ID_EMPLEADO FROM NOMINA_EMPLEADO WHERE CONCAT(NOMBRE, APELLIDO_PATERNO, APELLIDO_MATERNO) LIKE '%"+buscarNombreCompletoNominaEmpleado+"%' ))"+
                ")";
    }
        
    if (!buscar_idcliente.trim().equals("")){
        filtroBusqueda += " AND ID_CLIENTE='" + buscar_idcliente +"' ";
    }else{
        filtroBusqueda += " AND ID_CLIENTE>=0 ";
    }
    
    if (!buscar_idsucursal.trim().equals("")){
        filtroBusqueda += " AND ID_EMPRESA='" + buscar_idsucursal +"' ";
    }
    
    //Filtro por Registro Patronal
    if (!buscar_idRegistroPatronal.trim().equals("")){
        filtroBusqueda = " AND (ID_COMPROBANTE_FISCAL IN (SELECT ID_CROMPROBANTE_FISCAL FROM NOMINA_COMPROBANTE_DESCRIPCION WHERE ID_NOMINA_REG_PATRONAL="+buscar_idRegistroPatronal +")) ";
    }
    
    if (!strWhereRangoFechas.trim().equals("")){
        filtroBusqueda += " AND " + strWhereRangoFechas;
    }
    
    if (!periodoPagoWhereRango.trim().equals("")){
        filtroBusqueda += periodoPagoWhereRango;
    }
    
    //---validamos si se va a listar los comprobantes que se generaron a traves de la generación automática de los comprobantes de nómina:
    List<Integer> listaNominasNuevas = null;
    try{
        listaNominasNuevas = (ArrayList<Integer>)request.getSession().getAttribute("iDsComproNuevos");
        System.out.println("____________________recuperando el listado del request");        
    }catch(Exception e){System.out.println("____________________NOOOOOOOO HAY IDs EN SESION.");}
    if(listaNominasNuevas != null){
        filtroBusqueda += " AND (ID_COMPROBANTE_FISCAL = 0 ";
        for(int idCompNom : listaNominasNuevas){
            filtroBusqueda += " OR ID_COMPROBANTE_FISCAL = " + idCompNom;
        }
        filtroBusqueda += ")";
    }
    
    request.getSession().setAttribute("iDsComproNuevos", null);
    //---
    
    /*if(!buscar.trim().equals("")){
        String buscarNombreCompletoNominaEmpleado = buscar.replaceAll("\\s", "%");
        filtroBusqueda += " AND "
    }*/
    
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
     
     if(!periodoPago.trim().equals("") && !periodoPago.trim().equals("-1")){//PARA LA NÓMINA AUTOMÁTICA MOSTRAR TODOS LOS REGISTROS DE COMPROBANTES SIN PAGINACIÓN
         System.out.println("-----------------SIN PAGINACION");
         try{
            comprobanteFiscalDto = comprobanteFiscalBO.findComprobanteFiscal(idComprobanteFiscal, idEmpresa, 0, 0, filtroBusqueda);            
            //agregamos el listado de los id's de los comprobantes que se utilizaran para generar las nóminas automáticas:
            if (nominaIDsSesion==null){
                nominaIDsSesion = new NominaComprobanteIdsSesion();
                System.out.println("________CREANDO NUEVO OBJETO EN AGREGAR en factura list.jsp");
            }
            for (ComprobanteFiscal item : comprobanteFiscalDto){
                nominaIDsSesion.agregarIDs(item.getIdComprobanteFiscal());
            }
            request.getSession().setAttribute("nominaIDsSesion", nominaIDsSesion);            
         }catch(Exception e){e.printStackTrace();}
     }else{
         System.out.println("-----------------CON PAGINACION");
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
   }
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../catNominaCFDI_factura/cfdi_factura_form.jsp";
    String urlTo2 = "../catNominaCFDI_factura/cfdi_factura_proceso_automatico.jsp";
    String paramName = "idComprobanteFiscal";
    //String parametrosPaginacion="";// "idEmpresa="+idEmpresa;
    String parametrosPaginacion="q="+buscar+"&q_idcliente="+buscar_idcliente+"&q_idestatuscomprobanteFiscal="+buscar_idestatuscomprobanteFiscal+"&q_idsucursal="+buscar_idsucursal+"&q_fh_min="+fechaMin+"&q_fh_max="+fechaMax+"&periodoPago="+periodoPago+"&q_registro_patronal="+buscar_idRegistroPatronal;// "idEmpresa="+idEmpresa;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    
    NominaEmpleadoBO empledo = new NominaEmpleadoBO(user.getConn());
    
    Empresa empresaActual =  new EmpresaBO(idEmpresa, user.getConn()).getEmpresa();
    EmpresaPermisoAplicacion permisoAplicacion = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaActual.getIdEmpresaPadre());
    
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
                        url: "cfdi_factura_ajax.jsp",
                        data: { mode: '29', id_comprobanteFiscal : idComprobanteFiscal },
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
                               location.href = "cfdi_factura_form.jsp?acc=2&idComprobanteFiscal="+idComprobanteFiscal;
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
                        url: "cfdi_factura_ajax.jsp",
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
                                        location.href = "cfdi_factura_list.jsp?idComprobanteFiscal="+idComprobanteFiscal;
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
            
            function desmarcarMarcarID(idComprobante){
                //document.getElementById("comprobante_id_"+idComprobante).style.visibility = "hidden";
                if(idComprobante>0){
                    var agregraQuitar = "";
                    if($("#comprobante_id_"+idComprobante).is(':checked')){
                        //alert(idComprobante + " marcado");
                        agregraQuitar = "32";
                    }else{
                        //alert(idComprobante + " desmarcado");
                        agregraQuitar = "33";
                    }
                    
                    
                    $.ajax({
                        type: "POST",
                        url: "cfdi_factura_ajax.jsp",
                        data: { mode: agregraQuitar , idAgregarQuitar : idComprobante},
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
                               /*apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                    function(r){
                                        //location.href = "cfdi_factura_list.jsp?idComprobanteFiscal="+idComprobanteFiscal;
                                });*/
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               //$("#ajax_message").html(datos);
                               //$("#ajax_message").fadeIn("slow");
                               //apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
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
                    
                    <h1>Facturación CFDI 
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        Créditos Disponibles: <%=empresaActual.getFoliosDisponibles()<0?"0":empresaActual.getFoliosDisponibles()%>
                    </h1>
                    <%if(permisoAplicacion.getAccesoCreditosEmergencia() == 1){%>
                        <h2 style="color: red">
                        Créditos de Emergencia Usados:  <%=permisoAplicacion.getCreditosEmergenciaXPagar()%>
                        </h2>
                    <%}%>
                    
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
                            <form action="cfdi_factura_list.jsp" id="search_form_advance" name="search_form_advance" method="post">
                                <!--<p>
                                    Por Fecha de Captura &raquo;&nbsp;&nbsp;
                                    <label>Desde:</label>
                                    <input maxlength="15" type="text" id="q_fh_min" name="q_fh_min" style="width:100px"
                                            value="" readonly/>
                                    &nbsp; &laquo; &mdash; &raquo; &nbsp;
                                    <label>Hasta:</label>
                                    <input maxlength="15" type="text" id="q_fh_max" name="q_fh_max" style="width:100px"
                                        value="" readonly/>
                                </p>-->
                                <p>
                                    <label>Periodo de Pago:</label><br/>
                                    <select size="1" id="periodoPago" name="periodoPago">
                                        <option value="-1">Selecciona un rango (YYYY-MM-DD)</option>
                                            <%
                                                out.print(new NominaComprobanteDescripcionBO(user.getConn()).getNominaComprobanteDescripcionByIdHTMLComboFechaInicialFinal(idEmpresa, -1));
                                            %>
                                    </select>                                        
                                </p>
                                <br/>
                                
                                <p>
                                <label>Empleado:</label><br/>
                                <select id="q_idcliente" name="q_idcliente" class="flexselect" style="width: 300px;">
                                    <option></option>
                                    <%= empledo.getNominaEmpleadosByIdHTMLCombo(idEmpresa, -1," AND ID_ESTATUS=1" ) %>
                                </select>
                                </p>
                                <br/>
                                
                                <p>
                                    <label>Registro Patronal:</label><br/>
                                    <select name="q_registro_patronal" id="q_registro_patronal" >
                                        <option></option>
                                        <%= new NominaRegistroPatronalBO(user.getConn()).getNominaRegistroPatronalByIdHTMLCombo(idEmpresa, -1, "" ) %>
                                    </select>
                                </p>
                                                <br/>
                                
                                <!--<p>
                                <label>Sucursal</label><br/>
                                <select id="q_idsucursal" name="q_idsucursal" class="flexselect" style="width: 300px;">
                                    <option></option>
                                    <//%= empresaBO.getEmpresasByIdHTMLCombo(idEmpresa, -1 ) %>
                                </select>
                                </p>
                                <br/>-->
                                
                                <!--<p>
                                <label>Estatus:</label><br/>
                                <select id="q_idestatuscomprobanteFiscal" name="q_idestatuscomprobanteFiscal">
                                    <option></option>
                                    <option value="3">Entregada</option>
                                    <option value="4">Cancelada</option>
                                </select>
                                </p>
                                <br/>-->
                                
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
                                <img src="../../images/icon_nominaCFDI.png" alt="icon"/>
                                Facturas (CFDI)
                            </span>
                            <div class="switch" style="width:500px">
                                <table width="500px" cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td>
                                                <div id="search">
                                                <form action="cfdi_factura_list.jsp" id="search_form" name="search_form" method="get">
                                                        <input type="text" id="q" name="q" title="Buscar por ID/Folio/UUID/Empleado" class="" style="width: 300px; float: left; "
                                                                value="<%=buscar%>" autocomplete="off"/>
                                                        <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                </form>
                                                </div>
                                            </td>
                                            <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                            <td>
                                                <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Nuevo CFDI" 
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
                                            <th>UUID</th>
                                            <th>Ver</th>
                                            <th>Folio</th>
                                            <th>Empleado</th>
                                            <th># Empleado</th>
                                            <th>Sueldo Neto</th>
                                            <th>Fecha Captura</th>
                                            <th>Estado</th>
                                            <% if(!periodoPago.trim().equals("") && !periodoPago.trim().equals("-1")){ %>
                                                <th>Generar Nómina</th>
                                            <% } %>
                                            <!--<th>Total</th>-->
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            for (ComprobanteFiscal item:comprobanteFiscalDto){
                                                try{
                                                    NominaEmpleado empleado = null;
                                                    try{ empleado = new NominaEmpleadoBO(user.getConn(), item.getIdCliente()).getNominaEmpleado(); }catch(Exception ex){}
                                                    
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
                                            <td><%=item.getUuid() %></td>
                                            <td>
                                                 <a href="../../jsp/reportesExportar/previewCfdPdf.jsp?idComprobanteFiscal=<%=item.getIdComprobanteFiscal() %>&versionCfd=30.2" id="btn_show_cfdi" title="Previsualizar CFDI"
                                                class="modalbox_iframe">
                                                    <img src="../../images/icon_consultar.png" alt="Mostrar CFDI" class="help" title="Previsualizar CFDI"/><br/>
                                                </a>
                                            </td>
                                            <td><%=item.getFolioGenerado() %></td>
                                            <td>
                                                <%
                                                    if (item.getIdCliente()>0 && empleado!=null){
                                                     //Empleado
                                                        out.print(empleado.getNombre() +" "+ empleado.getApellidoPaterno() +" "+ empleado.getApellidoMaterno());
                                                    }else{
                                                        out.print("Sin empleado asignado.");
                                                    }
                                                %>
                                            </td>
                                            <td>
                                                <%
                                                    if (item.getIdCliente()>0 && empleado!=null){
                                                     //Empleado
                                                        out.print(empleado.getNumEmpleado());
                                                    }else{
                                                        out.print("Sin número");
                                                    }
                                                %>
                                            </td>
                                            <!--<td>
                                                <%
                                                    if (item.getIdCliente()>0 && empleado!=null){
                                                     //Empleado
                                                        out.print(empleado.getSalarioBaseCotApor());
                                                    }else{
                                                        out.print("0.0");
                                                    }
                                                %>
                                            </td>-->
                                            <td><%=item.getImporteNeto() %></td>
                                            
                                            <td><%= DateManage.formatDateToNormal(item.getFechaCaptura()) %></td>
                                            <td><%= EstatusComprobanteBO.getEstatusName(item.getIdEstatus()) %></td>
                                            
                                            <% if(!periodoPago.trim().equals("") && !periodoPago.trim().equals("-1")){ %>
                                                <td>
                                                    <input type="checkbox" id="comprobante_id_<%=item.getIdComprobanteFiscal()%>" name="comprobante_id_<%=item.getIdComprobanteFiscal()%>" 
                                                           value="<%=item.getIdComprobanteFiscal()%>" checked onclick="desmarcarMarcarID(this.value);"/>
                                                </td>
                                            <% } %>
                                            
                                            <td>
                                                <!--<a href="<%=urlTo%>?<%=paramName%>=<%=item.getIdComprobanteFiscal()%>&acc=1"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>-->
                                                <a href="#" onclick="editarComprobanteFiscal(<%=item.getIdComprobanteFiscal()%>);" title="Genera un nuevo recibo mediante esta plantilla"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Generar Nueva" /></a>
                                                &nbsp;&nbsp;
                                                <% if (item.getIdEstatus()==EstatusComprobanteBO.ESTATUS_ENTREGADA) {%>
                                                <a href="#" onclick="confirmarCancelarComprobanteFiscal(<%=item.getIdComprobanteFiscal()%>,'<%=item.getUuid() %>');"><img src="../../images/icon_delete.png" alt="cancelar" class="help" title="Cancelar" /></a>
                                                &nbsp;&nbsp;
                                                <%}%>
                                                <!--<% if (isFacturaFromPedido){ %>
                                                <a href="#" onclick="mostrarMensajeCobranzaPedido(<%=idPedidoParent %>,'<%=folioPedidoParent %>');"><img src="../../images/icon_ventas1.png" alt="consultar cobranza" class="help" title="Cobranza" /></a>
                                                <% }else {%>
                                                <a href="../catCobranzaAbono/catCobranzaAbono_list.jsp?idComprobanteFiscal=<%=item.getIdComprobanteFiscal() %>"><img src="../../images/icon_ventas1.png" alt="consultar cobranza" class="help" title="Cobranza" /></a>
                                                <% } %>-->
                                                <a href="../../jsp/reportesExportar/previewCfdPdfCorreo.jsp?idComprobanteFiscal=<%=item.getIdComprobanteFiscal() %>&versionCfd=111&accion=imprimeFacturaCFDI" id="btn_show_cfdi" title="Enviar por Correo"
                                                class="modalbox_iframe">                                                      
                                                    <img src="../../images/icon_email.png" alt="Mostrar CFDI JASPER" class="help" title="Enviar por Correo"/><br/>
                                                </a>
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
                                    
                           <% if(!periodoPago.trim().equals("") && !periodoPago.trim().equals("-1")){ %>
                                </br>
                                <td>
                                    <a href="<%=urlTo2%>?acc=1" id="todoProcesarNominaForma" title="Procesar Nómina" class="modalbox_iframe" style="width: 640px; height: 540px;">                                          
                                           <input type="button" id="todoProcesarNomina" name="todoProcesarNomina" class="right_switch" value="Procesar Nómina"
                                           style="float:right; width: 104px;" />
                                    </a>   
                                </td>
                                </br>
                                </br>
                           <% } %>

                            <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <jsp:param name="idReport" value="<%= ReportBO.FACTURA_NOMINA_REPORT %>" />
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