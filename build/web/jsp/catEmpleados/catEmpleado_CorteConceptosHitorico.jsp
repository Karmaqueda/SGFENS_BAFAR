<%-- 
    Document   : catEmpleado_CorteConceptosHitorico
    Created on : 21/12/2015, 07:07:00 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.ConceptoBO.Ordenamiento"%>
<%@page import="com.tsp.sct.util.FormatUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensVisitaClienteDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SgfensVisitaCliente"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="com.tsp.sct.dao.jdbc.InventarioHistoricoVendedorDaoImpl"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.tsp.sct.dao.dto.InventarioHistoricoVendedor"%>
<%@page import="com.tsp.sct.bo.InventarioHistoricoVendedorBO"%>
<%@page import="com.tsp.sct.dao.jdbc.InventarioInicialVendedorDaoImpl"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensPedidoProductoDaoImpl"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedidoDevolucionCambio"%>
<%@page import="com.tsp.sct.bo.SGPedidoDevolucionesCambioBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedidoProducto"%>
<%@page import="com.tsp.sct.bo.SGPedidoProductoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedido"%>
<%@page import="com.tsp.sct.bo.SGPedidoBO"%>
<%@page import="com.tsp.sct.dao.dto.InventarioInicialVendedor"%>
<%@page import="com.tsp.sct.bo.InventarioInicialVendedorBO"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoInventarioRepartidor"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.tsp.sct.bo.EmpleadoInventarioRepartidorBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    NumberFormat formatMoneda = new DecimalFormat("###,###,###,##0.00");
    
    int idEmpleado = 0; 
    try{
        idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
    }catch(NumberFormatException ex){}
    
        
    String filtroBusqueda = " AND ID_EMPLEADO = "+idEmpleado + " ";
    
        
    int idEmpresa = user.getUser().getIdEmpresa();
    
    String buscar_fechamin = "";
    String buscar_fechamax = "";
    Date fechaMin=null;
    Date fechaMax=null;
    String strWhereRangoFechas="";        
    String parametrosPaginacion="idEmpleado="+idEmpleado;// "idEmpresa="+idEmpresa
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
            //strWhereRangoFechas="(CAST(FECHA_ABONO AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax)+"&q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin!=null && fechaMax==null){
            //strWhereRangoFechas="(FECHA_ABONO  >= '"+buscar_fechamin+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin==null && fechaMax!=null){
            //strWhereRangoFechas="(FECHA_ABONO  <= '"+buscar_fechamax+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax);
        }
    }
    /*if (!strWhereRangoFechas.trim().equals("")){
        filtroBusqueda += " AND " + strWhereRangoFechas;
    }*/
    
    /*
    * Paginación
    */
    int paginaActual = 1;
    double registrosPagina = 800;
    double limiteRegistros = 0;
    int paginasTotales = 0;
    int numeroPaginasAMostrar = 5;

    try{
        paginaActual = Integer.parseInt(request.getParameter("pagina"));
    }catch(Exception e){}

    try{
        registrosPagina = Integer.parseInt(request.getParameter("registros_pagina"));
    }catch(Exception e){}
    
     InventarioHistoricoVendedorBO empleadoInventarioHistoricoBO = new InventarioHistoricoVendedorBO(user.getConn());
     InventarioHistoricoVendedor[] empleadoInventarioRepartidorDto = new InventarioHistoricoVendedor[0];
          
   
     //varibles para el filtro de que fecha a que fecha obtenemos el corte de caja
     Date fechaFiltroInicial = fechaMin;
     Date fechaFiltroFinal = fechaMax;
     InventarioInicialVendedorDaoImpl inventarioInicialVendedorDaoImpl = new InventarioInicialVendedorDaoImpl(user.getConn());
     InventarioHistoricoVendedorDaoImpl inventarioHistoricoVendedorDaoImpl = new InventarioHistoricoVendedorDaoImpl(user.getConn());
     try{
         if (user.getOrdenamientoConceptos().getSqlOrderByCorteHistorico()==null){
             // si no tiene una sentencia de Ordenamiento para Cortes, usamos la de defecto
             user.setOrdenamientoConceptos(ConceptoBO.Ordenamiento.CANTIDAD_ASIGNADA_MAYOR_MENOR);
         }
         
         //verificamos cual es el ultimo registro que se ha almacenado y es el que momentaneamente mostraremos en cuestion de la fecha 
         InventarioInicialVendedor inventarioInicialVendedorUltimaFecha = null;
         if(fechaFiltroInicial == null && fechaFiltroFinal == null){
            try{
               inventarioInicialVendedorUltimaFecha = inventarioInicialVendedorDaoImpl.findByDynamicWhere(" ID_EMPLEADO = " + idEmpleado + " ORDER BY FECHA_REGISTRO DESC", null)[0];
               //si existe armamos el filtro de que solo obtenga lo de 5 dias hacia atraz
               fechaFiltroFinal = inventarioInicialVendedorUltimaFecha.getFechaRegistro();            
               Calendar c = Calendar.getInstance();
               c.setTime(fechaFiltroFinal);
               c.add(Calendar.DATE, -5); //de hace 5 días
               fechaFiltroInicial = c.getTime();
               filtroBusqueda += " AND FECHA_INICIAL_CORTE >= '" + DateManage.dateToSQLDateTime(fechaFiltroInicial) + "' AND FECHA_FINAL_CORTE <= '" + DateManage.dateToSQLDateTime(fechaFiltroFinal) + "'";
            }catch(Exception e){e.printStackTrace();}            
         }else if(fechaFiltroInicial != null && fechaFiltroFinal == null){
             filtroBusqueda += " AND FECHA_INICIAL_CORTE >= '" + DateManage.dateToSQLDateTime(fechaFiltroInicial) + "'";
         }else if(fechaFiltroInicial == null && fechaFiltroFinal != null){
             filtroBusqueda += " AND FECHA_FINAL_CORTE <= '" + DateManage.dateToSQLDateTime(fechaFiltroFinal) + "'";
         }else if(fechaFiltroInicial != null && fechaFiltroFinal != null){
             filtroBusqueda += " AND FECHA_INICIAL_CORTE >= '" + DateManage.dateToSQLDateTime(fechaFiltroInicial) + "' AND FECHA_FINAL_CORTE <= '" + DateManage.formatDateToSQL(fechaFiltroFinal) + " 23:59:59'";
         }
         try{
            System.out.println("------------- FECHA MAS VIEJA " + fechaFiltroInicial.toString());}catch(Exception e){}         
         try{
             System.out.println("------------- FECHA MAS RECIENTE: " + fechaFiltroFinal.toString());}catch(Exception e){}
                           
         //limiteRegistros = empleadoInventarioHistoricoBO.findInventarioHistoricoByIdEmpleado(idEmpleado , 0, 0, filtroBusqueda).length;
         /* //Bloque comentado 27/04/2016 Cesar Martinez, por que repetia la misma consulta para obtener solo la cantidad de registros, que de todas maneras no es usado en este momento
         limiteRegistros = inventarioHistoricoVendedorDaoImpl.findByDynamicSelect(empleadoInventarioHistoricoBO.querySumaCantidad + " WHERE ID_EMPLEADO > 0 " + filtroBusqueda + " GROUP BY ID_CONCEPTO, ID_EMPLEADO ORDER BY FECHA_INICIAL_CORTE DESC", null).length;         
         
         if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;
         */

        //empleadoInventarioRepartidorDto = empleadoInventarioHistoricoBO.findInventarioHistoricoByIdEmpleado(idEmpleado,
        //        ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
        //        filtroBusqueda);        
        empleadoInventarioRepartidorDto = inventarioHistoricoVendedorDaoImpl.findByDynamicSelect(
                empleadoInventarioHistoricoBO.querySumaCantidad 
                + " WHERE inv.ID_CONCEPTO = concepto.ID_CONCEPTO AND ID_EMPLEADO > 0 " + filtroBusqueda
                + " GROUP BY ID_CONCEPTO, ID_EMPLEADO"
                + " ORDER BY " + user.getOrdenamientoConceptos().getSqlOrderByCorteHistorico() //FECHA_INICIAL_CORTE DESC"
                , null);
        
System.out.println("-------- CANTIDAD: " + empleadoInventarioRepartidorDto.length);
     }catch(Exception ex){
         ex.printStackTrace();
     }
     String mensajeIntervaloFechas = "";
    if(fechaFiltroInicial != null && fechaFiltroFinal != null){
        if(fechaMin != null && fechaMax != null){
            mensajeIntervaloFechas = " Del " + DateManage.formatDateTimeToNormalMinutes(fechaFiltroInicial) + " al " + DateManage.formatDateToNormal(fechaFiltroFinal) + " 23:59:59";
        }else{
            mensajeIntervaloFechas = " Del " + DateManage.formatDateTimeToNormalMinutes(fechaFiltroInicial) + " al " + DateManage.formatDateTimeToNormalMinutes(fechaFiltroFinal);
        }
    }else if(fechaFiltroInicial == null && fechaFiltroFinal != null){
        mensajeIntervaloFechas = " Hasta el " + DateManage.formatDateToNormal(fechaFiltroFinal) + " 23:59:59";
    }else if(fechaFiltroInicial != null && fechaFiltroFinal == null){
        mensajeIntervaloFechas = " Desde " + DateManage.formatDateTimeToNormalMinutes(fechaFiltroInicial);
    }
     
     //Obtenemos empleado
     Empleado empleadoDto = new Empleado();
     try{
         EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());     
         empleadoDto = empleadoBO.findEmpleadobyId(idEmpleado);
     }catch(Exception e){} 
     
     /*
    * Datos de catálogo
    */       
    
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");
    String strParamsExtra  ="";
    if(fechaMin != null && fechaMax != null){
        strParamsExtra  =""+idEmpleado+"|"+DateManage.formatDateToSQL(fechaFiltroInicial)+"|"+DateManage.formatDateToSQL(fechaFiltroFinal)+"|"+DateManage.formatDateToNormal(fechaFiltroInicial)+"|"+DateManage.formatDateToNormal(fechaFiltroFinal) + " 23:59:59";
    }else{
        strParamsExtra  =""+idEmpleado+"|"+DateManage.formatDateToSQL(fechaFiltroInicial)+"|"+DateManage.formatDateToSQL(fechaFiltroFinal)+"|"+DateManage.formatDateToNormal(fechaFiltroInicial)+"|"+DateManage.formatDateToNormal(fechaFiltroFinal);
    }
    String parametrosExtraEncoded = java.net.URLEncoder.encode(strParamsExtra , "UTF-8");
    String infoTitle  ="Empleado: " + empleadoDto.getNombre() +" "+ empleadoDto.getApellidoPaterno() +" "+ empleadoDto.getApellidoMaterno();
    String infoTitleEncoded = java.net.URLEncoder.encode(infoTitle , "UTF-8");
    
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
            
            function cambiarOrdenamiento(id_ordenamiento){
                $.ajax({
                    type: "POST",
                    url: "../catConceptos/catConceptos_ajax.jsp",
                    data: {mode: 'cambiarOrdenamiento', idOrdenamiento : id_ordenamiento},
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#ajax_message").html(datos);
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").fadeIn("slow");
                           location.reload();
                       }else{
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").html(datos);
                           $("#ajax_message").fadeIn("slow");
                           $("#action_buttons").fadeIn("slow");
                           $.scrollTo('#inner',800);
                       }
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
                            <form action="catEmpleado_CorteConceptosHitorico.jsp" id="search_form_advance" name="search_form_advance" method="post"> 
                                <div class="wrapper">
                                    <div class="column-1" style="width: 50%">
                                        <input type="hidden" id="idEmpleado" name="idEmpleado" value="<%=idEmpleado %>"/>
                                        <p>
                                            Por Fecha &raquo;&nbsp;&nbsp;<br/>
                                            <label>Inicio de Corte:</label>
                                            <input maxlength="15" type="text" id="q_fh_min" name="q_fh_min" style="width:100px"
                                                    value="" readonly/>
                                            &laquo; &mdash; &raquo;
                                            <label>Hasta Fecha Fin de Corte:</label>
                                            <input maxlength="15" type="text" id="q_fh_max" name="q_fh_max" style="width:100px"
                                                value="" readonly/>
                                        </p>
                                        <br/>
                                        <br/>
                                        <div id="action_buttons">
                                            <p>
                                                <input type="button" id="buscar" value="Buscar" onclick="$('#search_form_advance').submit();"/>
                                            </p>
                                        </div>
                                    </div>

                                    <div class="column-2" style="width: 50%">
                                        <p>
                                            <label for="ordenamiento">Ordenar por: </label><br/>
                                            <select id="ordenamiento" name="ordenamiento" onchange="cambiarOrdenamiento(this.value);">
                                                <option value="<%= Ordenamiento.CANTIDAD_ASIGNADA_MAYOR_MENOR.getId() %>" <%= user.getOrdenamientoConceptos()==Ordenamiento.CANTIDAD_ASIGNADA_MAYOR_MENOR?"selected":"" %> >Inventario Total Descendente</option><!-- Cantidad Asignada Descendente -->
                                                <option value="<%= Ordenamiento.ALFABETICO_A_Z.getId() %>" <%= user.getOrdenamientoConceptos()==Ordenamiento.ALFABETICO_A_Z?"selected":"" %> >Alfabetico A-Z</option>
                                                <option value="<%= Ordenamiento.ALFABETICO_Z_A.getId() %>" <%= user.getOrdenamientoConceptos()==Ordenamiento.ALFABETICO_Z_A?"selected":"" %>>Alfabetico Z-A</option>
                                                <option value="<%= Ordenamiento.CODIGO_MAYOR_MENOR.getId() %>" <%= user.getOrdenamientoConceptos()==Ordenamiento.CODIGO_MAYOR_MENOR?"selected":"" %>>Código Descendente</option>
                                                <option value="<%= Ordenamiento.ID_MAYOR_MENOR.getId() %>" <%= user.getOrdenamientoConceptos()==Ordenamiento.ID_MAYOR_MENOR?"selected":"" %> >ID Descendente</option>
                                                <option value="<%= Ordenamiento.FECHA_CREACION_MAS_RECIENTE.getId() %>" <%= user.getOrdenamientoConceptos()==Ordenamiento.FECHA_CREACION_MAS_RECIENTE?"selected":"" %> >Fecha creación producto Descendente</option>
                                                <option value="<%= Ordenamiento.FECHA_INICIAL_CORTE_DESC.getId() %>" <%= user.getOrdenamientoConceptos()==Ordenamiento.FECHA_INICIAL_CORTE_DESC?"selected":"" %> >Fecha Inicial Corte Descendente</option>
                                            </select>
                                        </p>
                                    </div>
                                            
                                </div>
                            </form>
                        </div>
                    </div>
         
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/inventario_16.png" alt="icon"/>
                                Corte De Productos &nbsp;&nbsp;&nbsp;&nbsp;<%=empleadoDto.getNombre() +" "+ empleadoDto.getApellidoPaterno() +" "+ empleadoDto.getApellidoMaterno() %>
                            </span>
                            
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <form id="form_data" name="form_data" action="" method="post">
                                <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Nombre</th>                                            
                                            <th>Descripción</th>                                             
                                            <th>Inventario Inicial</th>
                                            <th>Inventario Asignado</th>
                                            <th>Inventario Total</th>
                                            <th>Cantidad Entregada</th>
                                            <!--<th>Entregado por Pedidos</th>-->
                                            <th>Cantidad Devuelta</th>
                                            <!--<th>Cantidad a Cambio</th>-->
                                            <th>Cambio Mismo Producto</th>
                                            <th>Cambio Producto Distinto</th>
                                            <th>Inventario Final/Apto</th>  
                                            <th>Inventario Físico Total</th>
                                            <th>Cantidad Merma</th>
                                            <th>Devueltos/Cambio Aptos</th>
                                            <th>Pendiente de Entrega</th> 
                                            <th>Entregado por Ventas</th>
                                            <th>Monto de Venta</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                        Encrypter encripDesencri = new Encrypter();
                                        SGPedidoBO pedidosBO = new SGPedidoBO(user.getConn()); 
                                        SGPedidoProductoBO partidasPedidoBO = new SGPedidoProductoBO(user.getConn());
                                        SGPedidoDevolucionesCambioBO camDevBO = new SGPedidoDevolucionesCambioBO(user.getConn());
                                         
                                        double cantidadVendida = 0;
                                        double cantidadDevoluciones = 0;
                                        double cantidadMerma = 0;
                                        double cantidadCambios = 0;
                                        double invFinal = 0;
                                        double totalProdsVendidos = 0;
                                        double totalProdsMerma = 0;
                                        double totalProdsVendidosDinero = 0;
                                        double totalProdsMermaDinero = 0;
                                        String fechaInicial = "";
                                        double cantidadDevolucionAptos = 0;
                                        BigDecimal montoVentaTotalProducto = new BigDecimal("0.0");////
                                        double montoVentaTotalProductoAyuda = 0;
                                        BigDecimal montoVentaTotalProductoTotales = new BigDecimal("0.0");////
                                        
                                        BigDecimal cantidadEntregadoPorPedidos = new BigDecimal("0.0");////
                                        SgfensPedidoProducto[] sgfensPedidoProducto = null;////
                                        SgfensPedidoProductoDaoImpl sgfensPedidoProductoDaoImpl = new SgfensPedidoProductoDaoImpl(user.getConn());////                                        
                                        double cantidadMismoProducto = 0;////
                                        double cantidadDistitoProducto = 0;////
                                        double cantidadPendienteEntrega = 0;/////
                                        int cantidadPedidos = 0;
                                        
                                                                                
                                            for (InventarioHistoricoVendedor item : empleadoInventarioRepartidorDto){//////
                                                if(item.getIdEmpleado() > 0 && item.getIdConcepto() > 0){
                                                    //Limpiamos variables
                                                    cantidadVendida = 0 ;
                                                    cantidadDevoluciones = 0;
                                                    cantidadMerma = 0;
                                                    cantidadCambios = 0;
                                                    invFinal = 0;
                                                    fechaInicial = item.getFechaRegistro().toString();

                                                    cantidadEntregadoPorPedidos = new BigDecimal("0.0");////
                                                    sgfensPedidoProducto = null;////
                                                    cantidadMismoProducto = 0;////
                                                    cantidadDistitoProducto = 0;////
                                                    cantidadDevolucionAptos = 0;/////
                                                    cantidadPendienteEntrega = 0;/////
                                                    montoVentaTotalProducto = new BigDecimal("0.0");/////
                                                    montoVentaTotalProductoAyuda = 0;/////
                                                    cantidadPedidos = 0;/////

                                                    try{////
                                                        sgfensPedidoProducto = sgfensPedidoProductoDaoImpl.findWhereIdConceptoEquals(item.getIdConcepto());}catch(Exception e){e.printStackTrace();}////
                                                    if(sgfensPedidoProducto != null){////
                                                        for(SgfensPedidoProducto productos : sgfensPedidoProducto){////
                                                            cantidadEntregadoPorPedidos = cantidadEntregadoPorPedidos.add(new BigDecimal(productos.getCantidadEntregada()));////
                                                        }////
                                                    }////



                                                    try{                                                    
                                                        Concepto concepto = new ConceptoBO(item.getIdConcepto(),user.getConn()).getConcepto();


                                                        //Ventas
                                                        //Obtenemos Pedidos del vendedor
                                                        System.out.println("******PEDIDOS*******");
                                                        //String filtroPedidosVendedor = " AND ID_USUARIO_VENDEDOR ='" + empleadoDto.getIdUsuarios() + "'  AND FECHA_PEDIDO >= '" +item.getFechaRegistro() + "' AND (ID_ESTATUS_PEDIDO <> 3 AND ID_ESTATUS_PEDIDO <> 1) ";
                                                        //String filtroPedidosVendedor = " AND ID_USUARIO_VENDEDOR ='" + empleadoDto.getIdUsuarios() + "'  AND FECHA_PEDIDO >= '" +DateManage.formatDateToSQL(fechaFiltroInicial) + "' AND FECHA_PEDIDO < '" + DateManage.formatDateToSQL(fechaFiltroFinal) + "' AND (ID_ESTATUS_PEDIDO <> 3 AND ID_ESTATUS_PEDIDO <> 1) ";//////BUSCAMOS LOS PEDIDOS QUE CAEN DENTRO DEL RANGO DEL PERIODO DE BUSQUEDA
                                                        String filtroPedidosVendedor = "";
                                                        if(fechaFiltroInicial != null && fechaFiltroFinal != null){
                                                            filtroPedidosVendedor = " AND ID_USUARIO_VENDEDOR ='" + empleadoDto.getIdUsuarios() + "'  AND FECHA_PEDIDO >= '" +DateManage.formatDateToSQL(fechaFiltroInicial) + "' AND FECHA_PEDIDO < '" + DateManage.formatDateToSQL(fechaFiltroFinal) + "' AND (ID_ESTATUS_PEDIDO <> 3 AND ID_ESTATUS_PEDIDO <> 1) ";//////BUSCAMOS LOS PEDIDOS QUE CAEN DENTRO DEL RANGO DEL PERIODO DE BUSQUEDA
                                                        }else if(fechaFiltroInicial == null && fechaFiltroFinal != null){
                                                            filtroPedidosVendedor = " AND ID_USUARIO_VENDEDOR ='" + empleadoDto.getIdUsuarios() + "'  AND FECHA_PEDIDO < '" + DateManage.formatDateToSQL(fechaFiltroFinal) + "' AND (ID_ESTATUS_PEDIDO <> 3 AND ID_ESTATUS_PEDIDO <> 1) ";//////BUSCAMOS LOS PEDIDOS QUE CAEN DENTRO DEL RANGO DEL PERIODO DE BUSQUEDA
                                                        }else if(fechaFiltroInicial != null && fechaFiltroFinal == null){
                                                            filtroPedidosVendedor = " AND ID_USUARIO_VENDEDOR ='" + empleadoDto.getIdUsuarios() + "'  AND FECHA_PEDIDO >= '" +DateManage.formatDateToSQL(fechaFiltroInicial) + "' AND (ID_ESTATUS_PEDIDO <> 3 AND ID_ESTATUS_PEDIDO <> 1) ";//////BUSCAMOS LOS PEDIDOS QUE CAEN DENTRO DEL RANGO DEL PERIODO DE BUSQUEDA
                                                        }
                                                        SgfensPedido[] pedidos = pedidosBO.findPedido(-1, idEmpresa, -1, -1, filtroPedidosVendedor);
                                                        cantidadPedidos = pedidos.length;
                                                        if(pedidos.length>0){

                                                            for(SgfensPedido pedido : pedidos){
                                                                //Obtenemos partidas del pedido
                                                                System.out.println("******PRODUCTOS*******");
                                                                SgfensPedidoProducto[] partidasPedido = partidasPedidoBO.findByIdConcepto(item.getIdConcepto(), -1, -1," ID_PEDIDO="+pedido.getIdPedido() );
                                                                if(partidasPedido.length>0){
                                                                    for(SgfensPedidoProducto partida:partidasPedido ){
                                                                        //Sumamos cantidades

                                                                        cantidadVendida += partida.getCantidadEntregada();
                                                                        totalProdsVendidosDinero += (partida.getCantidadEntregada()* partida.getPrecioUnitario());
                                                                        cantidadPendienteEntrega += (partida.getCantidad() - partida.getCantidadEntregada() );/////
                                                                        montoVentaTotalProductoAyuda = 0;
                                                                        montoVentaTotalProductoAyuda = ( (partida.getCantidadEntregada() ) * partida.getPrecioUnitario());/////
                                                                        montoVentaTotalProducto =  montoVentaTotalProducto.add(new BigDecimal(montoVentaTotalProductoAyuda));
                                                                    }
                                                                }  
                                                            }                                                       

                                                        }   
                                                        montoVentaTotalProductoTotales = montoVentaTotalProductoTotales.add(montoVentaTotalProducto);


                                                        //Obtenemos devoluciones y cambios del pedido

                                                        System.out.println("******DEVOLUCIONES Y CAMBIOS*******");
                                                        //String filtroDevs  = " AND FECHA  >= '"+ item.getFechaRegistro()+"' ";
                                                        //String filtroDevs  = " AND FECHA  >= '"+ DateManage.formatDateToSQL(fechaFiltroInicial)+"' AND FECHA < '" + DateManage.formatDateToSQL(fechaFiltroFinal) + "'";
                                                        String filtroDevs  = "";
                                                        if(fechaFiltroInicial != null && fechaFiltroFinal != null){
                                                            filtroDevs  = " AND FECHA  >= '"+ DateManage.formatDateToSQL(fechaFiltroInicial)+"' AND FECHA < '" + DateManage.formatDateToSQL(fechaFiltroFinal) + "'";
                                                        }else if(fechaFiltroInicial == null && fechaFiltroFinal != null){
                                                            filtroDevs  = " AND FECHA < '" + DateManage.formatDateToSQL(fechaFiltroFinal) + "'";
                                                        }else if(fechaFiltroInicial != null && fechaFiltroFinal == null){
                                                            filtroDevs  = " AND FECHA  >= '"+ DateManage.formatDateToSQL(fechaFiltroInicial)+"'";
                                                        }
                                                        SgfensPedidoDevolucionCambio[] devoluciones = camDevBO.findCambioDevByEmpleado(user.getConn(), empleadoDto.getIdEmpleado(), filtroDevs);

                                                        if(devoluciones.length>0){
                                                            for(SgfensPedidoDevolucionCambio dev:devoluciones){

                                                                 //Precio de venta del pedido 
                                                                SgfensPedidoProducto[] conceptoPedidoDto = partidasPedidoBO.findByIdPedido(dev.getIdPedido(), -1, -1, -1, " AND ID_CONCEPTO="+dev.getIdConcepto());

                                                                //if(conceptoPedidoDto.length>0){

                                                                    if(item.getIdConcepto()==dev.getIdConcepto()){

                                                                        //cantidadDevoluciones += dev.getAptoParaVenta();
                                                                        cantidadMerma += dev.getNoAptoParaVenta();
                                                                        try{
                                                                            totalProdsMermaDinero += ( conceptoPedidoDto[0].getPrecioUnitario());
                                                                        }catch(Exception e){
                                                                            totalProdsMermaDinero += 0;
                                                                        }
                                                                        if(dev.getIdTipo() == 2){////cambio
                                                                            if(item.getIdConcepto() == dev.getIdConceptoEntregado()){////
                                                                                cantidadMismoProducto += (dev.getAptoParaVenta() + dev.getNoAptoParaVenta());////
                                                                            }else{////
                                                                                cantidadDistitoProducto += (dev.getAptoParaVenta() + dev.getNoAptoParaVenta());////
                                                                            }///                                                                            
                                                                        }else if(dev.getIdTipo() == 1){//devolucion                                                                        
                                                                               cantidadDevoluciones = cantidadDevoluciones + dev.getAptoParaVenta() + dev.getNoAptoParaVenta();                                                                       
                                                                        }
                                                                        cantidadDevolucionAptos += dev.getAptoParaVenta();/////
                                                                    }/*else{
                                                                        if(dev.getIdTipo() == 1){//devolucion                                                                        
                                                                               cantidadDevoluciones = cantidadDevoluciones + dev.getAptoParaVenta() + dev.getNoAptoParaVenta();                                                                       
                                                                        } 
                                                                    }*/     

                                                                    if(item.getIdConcepto()==dev.getIdConceptoEntregado()){
                                                                        if(dev.getCantidadDevuelta()>0){
                                                                            cantidadCambios += dev.getCantidadDevuelta();
                                                                        }
                                                                    }
                                                               // }                                                          

                                                            }
                                                        }

                                                        //cantidadVendida += cantidadMismoProducto;////


                                                        //invFinal = item.getCantidad() - cantidadVendida + cantidadDevoluciones - cantidadCambios + cantidadMerma;
                                                        //invFinal = item.getCantidad() - cantidadVendida + cantidadDevoluciones - cantidadCambios;
                                                        //invFinal = item.getCantidad() - cantidadVendida + (cantidadDevoluciones !=0? cantidadDevoluciones : cantidadMerma) - cantidadMismoProducto + cantidadDistitoProducto;
                                                        if(cantidadMerma != 0){
                                                            //invFinal = item.getCantidad() - cantidadVendida + (cantidadMerma) - cantidadCambios;
                                                            //////invFinal = item.getCantidad() - cantidadVendida + (cantidadMerma) - cantidadCambios;
                                                            //invFinal = item.getCantidad() - cantidadVendida - cantidadCambios + cantidadMismoProducto;                                                        
                                                            //invFinal = item.getCantidad() - cantidadVendida - cantidadCambios;
                                                            invFinal = item.getCantidadAsignada()- (cantidadVendida + cantidadCambios) + cantidadDevoluciones + cantidadMismoProducto + cantidadDistitoProducto;
                                                        }else{
                                                            //invFinal = item.getCantidad() - cantidadVendida - cantidadMismoProducto + cantidadDistitoProducto - cantidadCambios;
                                                            /////////invFinal = item.getCantidad() - cantidadVendida - cantidadCambios + cantidadMismoProducto;                                                        
                                                            invFinal = item.getCantidadAsignada() - (cantidadVendida + cantidadCambios) + cantidadDevoluciones + cantidadMismoProducto + cantidadDistitoProducto;
                                                        }

                                                        totalProdsVendidos += cantidadVendida;
                                                        totalProdsMerma += cantidadMerma;



                                            %>
                                                    <tr>                                            
                                                        <td><%=item.getIdConcepto()%></td>                                            
                                                        <td><%=encripDesencri.decodeString(concepto.getNombre())%></td>
                                                        <td><%=concepto.getDescripcion()%></td> 
                                                        <td><%=item.getCantidadTerminno()%></td>                                                        
                                                        <td><%=(item.getCantidadAsignada() - item.getCantidadTerminno())%></td>
                                                        <td><%=item.getCantidadAsignada()%></td>                                                        
                                                        <td><%=(cantidadVendida + cantidadCambios)%></td> 
                                                        <!--<td><//%=cantidadEntregadoPorPedidos%></td> -->
                                                        <!--<td><%=(cantidadDevoluciones !=0? cantidadDevoluciones : cantidadMerma)%></td> -->
                                                        <td><%=(cantidadDevoluciones)%></td> 
                                                        <!--<td><//%=cantidadCambios%></td> -->
                                                        <td><%=cantidadMismoProducto%></td> 
                                                        <td><%=cantidadDistitoProducto%></td>                                                          
                                                        <td><%=invFinal>=0?invFinal:0%></td> 
                                                        <td><%=invFinal>=0?(invFinal + cantidadMerma):0%></td>
                                                        <td><%=cantidadMerma%></td>
                                                        <td><%=cantidadDevolucionAptos%></td>
                                                        <td><%=cantidadPendienteEntrega%></td>
                                                        <td><%=(cantidadVendida)%></td>
                                                        <td><%=formatMoneda.format(montoVentaTotalProducto.setScale(2, RoundingMode.HALF_UP)) %></td>
                                                        
                                                        
                                                        
                                                    </tr>

                                            <%     
                                                    }catch(Exception ex){
                                                        ex.printStackTrace();
                                                    }
                                                }
                                            } 
                                        %>
                                            <tr>
                                                <td colspan="4" style="text-align: center;"><b>Articulos Vendidos:</b></td>
                                                <td style="color: #0000cc; text-align: left;"><%=totalProdsVendidos%></td>
                                                <td style="text-align: left;"><b>Total:</b></td>
                                                <!--<td style="color: #0000cc; text-align: left;">$ <//%=formatMoneda.format(totalProdsVendidosDinero)%></td> -->
                                                <td style="color: #0000cc; text-align: left;">$ <%=formatMoneda.format(montoVentaTotalProductoTotales)%></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>                                                
                                            </tr>        
                                            <tr>
                                                <td colspan="4" style="text-align: center;"><b>Articulos en Merma:</b></td>
                                                <td style="color: #0000cc; text-align: left;"><%=totalProdsMerma%></td>
                                                <td style="text-align: left;"><b>Total:</b></td>
                                                <td style="color: #0000cc; text-align: left;">$ <%=formatMoneda.format(totalProdsMermaDinero)%></td>   
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                            </tr>
                                            <tr>
                                                <%SgfensVisitaCliente[] sgfensVisitaCliente = new SgfensVisitaCliente[0];
                                                if(fechaFiltroInicial != null && fechaFiltroFinal != null){
                                                    sgfensVisitaCliente = new SgfensVisitaClienteDaoImpl(user.getConn()).findByDynamicWhere("ID_EMPLEADO_VENDEDOR = " + idEmpleado + " AND FECHA_HORA > '" + DateManage.formatDateToSQL(fechaFiltroInicial) + "' AND FECHA_HORA < '" + DateManage.formatDateToSQL(fechaFiltroFinal) + "' " , null);
                                                }else if(fechaFiltroInicial == null && fechaFiltroFinal != null){
                                                    sgfensVisitaCliente = new SgfensVisitaClienteDaoImpl(user.getConn()).findByDynamicWhere("ID_EMPLEADO_VENDEDOR = " + idEmpleado + " AND FECHA_HORA < '" + DateManage.formatDateToSQL(fechaFiltroFinal) + "' " , null);
                                                }else if(fechaFiltroInicial != null && fechaFiltroFinal == null){
                                                    sgfensVisitaCliente = new SgfensVisitaClienteDaoImpl(user.getConn()).findByDynamicWhere("ID_EMPLEADO_VENDEDOR = " + idEmpleado + " AND FECHA_HORA > '" + DateManage.formatDateToSQL(fechaFiltroInicial) + "' " , null);
                                                }
                                                    
                                                %>                                                
                                                <td colspan="4" style="text-align: center;"><b>Visitas NO venta:</b></td>
                                                <td style="color: #0000cc; text-align: left;"><%=sgfensVisitaCliente!=null?sgfensVisitaCliente.length:"0"%></td>
                                                <td></td>
                                                <td></td>   
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                            </tr>
                                            <tr>                                                                                               
                                                <td colspan="4" style="text-align: center;"><b>Cantidad de Ventas:</b></td>
                                                <td style="color: #0000cc; text-align: left;"><%=cantidadPedidos%></td>
                                                <td></td>
                                                <td></td>   
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                            </tr>
                                            <tr>                                                                                               
                                                <td colspan="4" style="text-align: center;"><b>Visitas Realizadas:</b></td>
                                                <td style="color: #0000cc; text-align: left;"><%=((cantidadPedidos + (sgfensVisitaCliente!=null?sgfensVisitaCliente.length:0)))%></td>
                                                <td></td>
                                                <td></td>   
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                            </tr>
                                            <tr>                                                                                               
                                                <td colspan="4" style="text-align: center;"><b>Eficacia:</b></td>
                                                <%try{%>
                                                <td style="color: #0000cc; text-align: left;"><%= FormatUtil.doubleToString(((cantidadPedidos * 100) / ((cantidadPedidos + (sgfensVisitaCliente!=null?sgfensVisitaCliente.length:0)))))+ "%"%></td>
                                                <%}catch(Exception e){%>
                                                <td style="color: #0000cc; text-align: left;"><%= FormatUtil.doubleToString(0) + "%"%></td>
                                                <%}%>
                                                <td></td>
                                                <td></td>   
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                            </tr>
                                            <!--<tr>                                                                                               
                                                <td colspan="4" style="text-align: center;"><b>Venta Total:</b></td>
                                                <td style="color: #0000cc; text-align: left;">$ <//%=formatMoneda.format(montoVentaTotalProductoTotales)%></td>
                                                <td></td>
                                                <td></td>   
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                            </tr>-->
                                            <tr>
                                                <td colspan="2" style="text-align: left;">Periodo de Inventario: </br><%=mensajeIntervaloFechas%></td> 
                                                <td colspan="10"></td>
                                                
                                            </tr> 
                                    </tbody>
                                </table>
                            </form>
                                    
                                    
                                    
                            <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <jsp:param name="idReport" value="<%= ReportBO.EMPLEADO_CORTE_INVENTARIO_HISTORICO_REPORT %>" />
                                <jsp:param name="parametrosCustom" value="<%= filtroBusquedaEncoded %>" />
                                <jsp:param name="parametrosExtra" value="<%=parametrosExtraEncoded%>" />
                                <jsp:param name="infoTitle" value="<%=infoTitleEncoded%>" />
                            </jsp:include>
                            <!-- FIN INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                            
                            <jsp:include page="../include/listPagination.jsp">
                                <jsp:param name="paginaActual" value="<%=paginaActual%>" />
                                <jsp:param name="numeroPaginasAMostrar" value="<%=numeroPaginasAMostrar%>" />
                                <jsp:param name="paginasTotales" value="<%=paginasTotales%>" />
                                <jsp:param name="url" value="<%=request.getRequestURI() %>" />
                                <jsp:param name="parametrosAdicionales" value="<%=parametrosPaginacion%>" />
                            </jsp:include>
                            
                        </div>
                    </div>
                 <!-- Fin de Contenido-->
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