<%-- 
    Document   : mapa_visor
    Created on : 4/01/2013, 06:12:13 PM
    Author     : Luis
    Edited on  : 04/05/2016
    Last Editor: ISCesar
--%>

<%@page import="com.tsp.sct.bo.SGPedidoEntregaBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedidoEntrega"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.tsp.sct.bo.EmpleadoAgendaBO"%>
<%@page import="com.tsp.sct.bo.SGEstatusPedidoBO"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.bo.SGVisitaClienteBO"%>
<%@page import="com.tsp.sct.bo.SGCobranzaAbonoBO"%>
<%@page import="com.tsp.sct.bo.SGPedidoDevolucionesCambioBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedidoDevolucionCambio"%>
<%@page import="com.tsp.sct.dao.dto.PuntosInteres"%>
<%@page import="com.tsp.sct.dao.dto.SgfensVisitaCliente"%>
<%@page import="com.tsp.sct.dao.dto.SgfensCobranzaAbono"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.bo.SGPedidoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedido"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoBitacoraPosicionDaoImpl"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.bo.SGProspectoBO"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.EstadoEmpleado"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.factory.EstadoEmpleadoDaoFactory"%>
<%@page import="com.tsp.sct.dao.factory.EmpleadoBitacoraPosicionDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoBitacoraPosicion"%>
<%@page import="com.tsp.sct.dao.factory.EmpleadoDaoFactory"%>
<%@page import="com.tsp.sct.dao.factory.SgfensProspectoDaoFactory"%>
<%@page import="com.tsp.sct.dao.factory.ClienteDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProspecto"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<!---------<//%@page import="com.tsp.microfinancieras.report.ReportBO"%>-->
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    int idEstatusPedido = -1;
    Date fechaMin=null;
    Date fechaMax=null;
    //try{ fechaMin = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("filtro_fecha_min"));}catch(Exception e){}
    //try{ fechaMax = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("filtro_fecha_max"));}catch(Exception e){}
    
        ///RECUPERAMOS LOS DATOS DE LA SESION
       
        String buscarTipoEnte = (String)request.getSession().getAttribute("buscarTipoEnte");
        String lat = (String)request.getSession().getAttribute("lat");
        String lng = (String)request.getSession().getAttribute("lng");
        String tipo = (String)request.getSession().getAttribute("tipo");
        String aux = (String)request.getSession().getAttribute("aux");
        String promId = (String)request.getSession().getAttribute("promId");
        String promTipo = (String)request.getSession().getAttribute("promTipo");
        String cooredenadasPromotor = (String)request.getSession().getAttribute("cooredenadasPromotor");
        String promotorTipo = (String)request.getSession().getAttribute("promotorDetalles");
        String latCl = (String)request.getSession().getAttribute("latCl");
        String lngCl = (String)request.getSession().getAttribute("lngCl");
        String cooredenadasCliente = (String)request.getSession().getAttribute("cooredenadasCliente");
        String clieId = (String)request.getSession().getAttribute("clieId");
        String clieTipo = (String)request.getSession().getAttribute("clieTipo");
        String clienteDetalles = (String)request.getSession().getAttribute("clienteDetalles");
        String cooredenadasPros = (String)request.getSession().getAttribute("cooredenadasPros");
        String prosId = (String)request.getSession().getAttribute("prosId");
        String prosTipo = (String)request.getSession().getAttribute("prosTipo");
        String prosDetalles = (String)request.getSession().getAttribute("prosDetalles");
        String latPros = (String)request.getSession().getAttribute("latPros");
        String lngPros = (String)request.getSession().getAttribute("lngPros");
        String strFechaMin = (String)request.getSession().getAttribute("fechaMin");
        String strFechaMax = (String)request.getSession().getAttribute("fechaMax");
        String chkClientes = (String)request.getSession().getAttribute("s_clientes");
        String chkProspectos = (String)request.getSession().getAttribute("s_prospectos");
        String chkPromotores = (String)request.getSession().getAttribute("s_promotores");
        String chkVehiculos = (String)request.getSession().getAttribute("s_vehiculos");
        String chkPuntos = (String)request.getSession().getAttribute("s_puntos");
        String chkPedidos = (String)request.getSession().getAttribute("s_pedidos");
        String chkCobranzas = (String)request.getSession().getAttribute("s_cobranzas");
        String chkVisitas = (String)request.getSession().getAttribute("s_visitas");
        String chkDevoluciones = (String)request.getSession().getAttribute("s_devoluciones");
        String chkEntregas = (String)request.getSession().getAttribute("s_entregas");
        try{ idEstatusPedido = Integer.parseInt((String)request.getSession().getAttribute("idEstatusPedido")); }catch(Exception ex){}  
        
        if (fechaMin==null && fechaMax == null){
            if (StringManage.getValidString(strFechaMin).length()>0)
                try{ fechaMin = new SimpleDateFormat("dd/MM/yyyy").parse(strFechaMin); }catch(Exception ex){}
            if (StringManage.getValidString(strFechaMax).length()>0)
                try{ fechaMax = new SimpleDateFormat("dd/MM/yyyy").parse(strFechaMax);}catch(Exception ex){}
        }
        
        //VACIAMOS LOS DATOS DE SESION       
        request.getSession().setAttribute("buscarTipoEnte","");
        request.getSession().setAttribute("lat","");
        request.getSession().setAttribute("lng","");
        request.getSession().setAttribute("promotores","");
        request.getSession().setAttribute("aux","");
        request.getSession().setAttribute("promId","");
        request.getSession().setAttribute("promTipo","");
        request.getSession().setAttribute("cooredenadasPromotor","");
        request.getSession().setAttribute("promotorDetalles","");       
        request.getSession().setAttribute("cooredenadasCliente","");
        request.getSession().setAttribute("clienteDetalles","");
        request.getSession().setAttribute("clieId","");
        request.getSession().setAttribute("clieTipo","");
        request.getSession().setAttribute("clienteDetalles","");
        request.getSession().setAttribute("cooredenadasProspecto","");
        request.getSession().setAttribute("prosDetalles","");
        request.getSession().setAttribute("prosId","");
        request.getSession().setAttribute("prosTipo","");
        //request.getSession().setAttribute("fechaMin","");
        //request.getSession().setAttribute("fechaMax","");
      
        ///
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String filtroBusqueda = "";
    if (!buscar.trim().equals(""))
        filtroBusqueda = " AND (NOMBRE_COMERCIAL LIKE '%" + buscar + "%' )";
         
    //int idEmpresa = -1;
    //try{ idEmpresa = Integer.parseInt(request.getParameter("idEmpresa")); }catch(NumberFormatException e){}
    int idEmpresa = user.getUser().getIdEmpresa();
    
    String filtroBusquedaPedido = "";
    
    String strWhereRangoFechasPedido="";
    String strWhereRangoFechasCobranza="";
    String strWhereRangoFechasVisita="";
    String strWhereRangoFechasDevolucion="";
    String strWhereRangoFechasEntrega="";
    {
        String buscar_fechamin ="";
        String buscar_fechamax ="";
        try{
            if (fechaMin==null)
                fechaMin = new Date();
            buscar_fechamin = DateManage.formatDateToSQL(fechaMin);
        }catch(Exception e){}
        try{
            if (fechaMax==null)
                fechaMax = new Date();
            buscar_fechamax = DateManage.formatDateToSQL(fechaMax);
        }catch(Exception e){}

        /*Filtro por rango de fechas*/
        if (fechaMin!=null && fechaMax!=null){
            strWhereRangoFechasPedido="(CAST(FECHA_PEDIDO AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')";
            strWhereRangoFechasCobranza="(CAST(FECHA_ABONO AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')";
            strWhereRangoFechasVisita="(CAST(FECHA_HORA AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')";
            strWhereRangoFechasDevolucion="(CAST(FECHA AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')";
            strWhereRangoFechasEntrega="(CAST(FECHA_HORA AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')";
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechasPedido="(FECHA_PEDIDO >= '"+buscar_fechamin+"')";
            strWhereRangoFechasCobranza="(FECHA_ABONO >= '"+buscar_fechamin+"')";
            strWhereRangoFechasVisita="(FECHA_HORA >= '"+buscar_fechamin+"')";
            strWhereRangoFechasDevolucion="(FECHA >= '"+buscar_fechamin+"')";
            strWhereRangoFechasEntrega="(FECHA_HORA >= '"+buscar_fechamin+"')";
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechasPedido="(FECHA_PEDIDO <= '"+buscar_fechamax+"')";
            strWhereRangoFechasCobranza="(FECHA_ABONO <= '"+buscar_fechamax+"')";
            strWhereRangoFechasVisita="(FECHA_HORA <= '"+buscar_fechamax+"')";
            strWhereRangoFechasDevolucion="(FECHA <= '"+buscar_fechamax+"')";
            strWhereRangoFechasEntrega="(FECHA_HORA <= '"+buscar_fechamax+"')";
        }
        
        if (StringManage.getValidString(strWhereRangoFechasPedido).length()>0)
            filtroBusquedaPedido += " AND " + strWhereRangoFechasPedido;
    }
    
    if (idEstatusPedido>0)
        filtroBusquedaPedido += " AND ID_ESTATUS_PEDIDO=" + idEstatusPedido ;
    
//    Cliente[] clientesDto = null;
//    SgfensProspecto[] prospectosDto = null;
//    Empleado[] empleadosDto = null;
    Empresa empresa = new Empresa();   
//    SgfensPedido[] pedidosDto = null;
//    SgfensCobranzaAbono[] cobranzasDto = null;
//    SgfensVisitaCliente[] visitasDto = null;
//    PuntosInteres[] puntosInteresesDto = null;
//    SgfensPedidoDevolucionCambio[] devolucionesDto = null;
//    SgfensPedidoEntrega[] entregasDto = null;
    /*
    Empleado[] promotoresDto = null;
    Automovil[] automovilesDto = null;
    */
    SGPedidoBO pedidoBO = new SGPedidoBO(user.getConn());
    EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
    SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(user.getConn());
    SGVisitaClienteBO visitaClienteBO = new SGVisitaClienteBO(user.getConn());
    SGPedidoDevolucionesCambioBO devolucionesCambioBO = new SGPedidoDevolucionesCambioBO(user.getConn());
    EmpleadoAgendaBO empleadoAgendaBO = new EmpleadoAgendaBO(user.getConn());
    SGPedidoEntregaBO pedidoEntregaBO = new SGPedidoEntregaBO(user.getConn());
    if(idEmpresa > 0){
                 
        EmpresaBO empresaBo = new EmpresaBO(user.getConn());    
        empresa = empresaBo.findEmpresabyId(idEmpresa); 
            
//        try{
//            ClienteBO clienteBO = new ClienteBO(user.getConn());
//            clientesDto = clienteBO.findClientes(0, idEmpresa , 0, 0, " AND (LATITUD <> 0 AND LONGITUD <> 0 AND LATITUD <> 'null' AND LONGITUD <> 'null' AND LATITUD IS NOT NULL AND LONGITUD IS NOT NULL ) AND ID_ESTATUS <> 2 ");            
//        }catch(Exception e){}
//        try{
//            SGProspectoBO prospectoBO = new SGProspectoBO(user.getConn());
//            prospectosDto = prospectoBO.findProspecto(0, idEmpresa, 0, 0, " AND (LATITUD <> 0 AND LONGITUD <> 0 AND LATITUD IS NOT NULL AND LONGITUD IS NOT NULL ) ");           
//        }catch(Exception e){}
//        try{
//            empleadosDto = empleadoBO.findEmpleados(0, idEmpresa, 0, 0, " AND ID_DISPOSITIVO != -1  AND (LATITUD <> 0 AND LONGITUD <> 0 AND LATITUD IS NOT NULL AND LONGITUD IS NOT NULL ) ");
//        }catch(Exception e){}
//        try{
//            pedidosDto = pedidoBO.findPedido(0, idEmpresa , 0, 0, " AND (LATITUD <> 0 AND LONGITUD <> 0 AND LATITUD IS NOT NULL AND LONGITUD IS NOT NULL ) " + filtroBusquedaPedido);            
//        }catch(Exception e){}
//        try{
//            cobranzasDto = cobranzaAbonoBO.findCobranzaAbono(0, idEmpresa , 0, 0, " AND (LATITUD <> 0 AND LONGITUD <> 0 AND LATITUD IS NOT NULL AND LONGITUD IS NOT NULL ) AND " + strWhereRangoFechasCobranza);            
//        }catch(Exception e){}
//        try{
//            visitasDto = visitaClienteBO.findSgfensVisitaClientes(0, idEmpresa , 0, 0, " AND (LATITUD <> 0 AND LONGITUD <> 0 AND LATITUD IS NOT NULL AND LONGITUD IS NOT NULL ) AND " + strWhereRangoFechasVisita);            
//        }catch(Exception e){}
//        try{
//            devolucionesDto = devolucionesCambioBO.findSgfensPedidoDevolucionCambios(0, idEmpresa , 0, 0, (StringManage.getValidString(strWhereRangoFechasDevolucion).length()>0? " AND " + strWhereRangoFechasDevolucion : "") );            
//        }catch(Exception e){}
//        try{
//            entregasDto = pedidoEntregaBO.findSgfensPedidoEntregas(0, idEmpresa , 0, 0, (StringManage.getValidString(strWhereRangoFechasEntrega).length()>0? " AND " + strWhereRangoFechasEntrega : "") );            
//        }catch(Exception e){}
    }
    
    
    //Para obtener indicadores del día para cabecera
    double indicadorVentaTotal = 0;
    double indicadorCobranzaTotal = 0;
    int indicadorVisitas = 0;
    int indicadorDevolucionesCambios = 0;
    int indicadorClientesPendientes = 0;
    int indicadorEmpleadosActivos = 0;
    int indicadorEmpleadosInactivos = 0;
    int totalEmpleados = 0;
    {   
        Date hoy = new Date();
        Calendar calAhora = Calendar.getInstance();
        calAhora.add(Calendar.MINUTE, -5); //restamos X minutos
        Date hace5Minutos = calAhora.getTime();
        String strHace5Minutos = DateManage.dateToSQLDateTime(hace5Minutos);
        String strHoy = DateManage.formatDateToSQL(hoy);
        String filtroFechaPart1 = " (CAST(";
        String filtroFechaPart2 = " AS DATE) BETWEEN '"+strHoy+"' AND '"+strHoy+"')";
        
        indicadorVisitas = visitaClienteBO.findCantidadSgfensVisitaClientes(-1, idEmpresa, 0, 0, " AND " + filtroFechaPart1 + "FECHA_HORA" + filtroFechaPart2);
        indicadorDevolucionesCambios = devolucionesCambioBO.findCantidadSgfensPedidoDevolucionCambios(-1, idEmpresa, 0, 0, " AND " + filtroFechaPart1 + "FECHA" + filtroFechaPart2);
        indicadorClientesPendientes =  empleadoAgendaBO.findCantidadEmpleadoAgendas(-1, -1, 0, 0,  " AND " + filtroFechaPart1 + "FECHA_PROGRAMADA" + filtroFechaPart2
                        + " AND FECHA_EJECUCION IS NULL AND NOMBRE_TAREA = 'Visita a Cliente' AND ID_EMPLEADO IN (SELECT ID_EMPLEADO FROM empleado WHERE id_empresa=" + idEmpresa + ")");
        
        indicadorEmpleadosActivos = empleadoBO.findCantidadEmpleados(-1, idEmpresa, 0, 0, " AND id_empleado IN (SELECT DISTINCT id_empleado FROM empleado_bitacora_posicion WHERE fecha >= '" + strHace5Minutos + "')");
        totalEmpleados = empleadoBO.findCantidadEmpleados(-1, idEmpresa, 0, 0, "");
        indicadorEmpleadosInactivos = totalEmpleados - indicadorEmpleadosActivos;
        
        try{
            String strTotalPedidos = pedidoBO.findGroupValorUnicoPedido(-1, idEmpresa, 0, 0, " AND " + filtroFechaPart1 + "FECHA_PEDIDO" + filtroFechaPart2, " SUM(TOTAL) ");
            if (StringManage.getValidString(strTotalPedidos).length()>0)
                indicadorVentaTotal = Double.parseDouble(strTotalPedidos);
        }catch(Exception ex){
            System.out.println("No se pudo obtener el total de pedidos del día." + ex.toString());
        }
        
        try{
            String strTotalCobranza = cobranzaAbonoBO.findGroupValorUnicoCobranzaAbono(-1, idEmpresa, 0, 0, " AND " + filtroFechaPart1 + "FECHA_ABONO" + filtroFechaPart2, " SUM(MONTO_ABONO) ");
            if (StringManage.getValidString(strTotalCobranza).length()>0)
                indicadorCobranzaTotal = Double.parseDouble(strTotalCobranza);
        }catch(Exception ex){
            System.out.println("No se pudo obtener el total de pedidos del día." + ex.toString());
        }
    }
       
    //VARIABLE PARA OCULTAR DISEÑOS DEL HTML POR SI SE VA A MOSTRAR EN IFRAME:
    int dashboard = 0;
    try{
        dashboard = Integer.parseInt(request.getParameter("dashboard"));
    }catch(NumberFormatException ex){}
    
    int minutosAutoRecarga = 5;
    int tiempoAutoRecargaMilis = (60 * 1000) * minutosAutoRecarga ;
    int velocidadMaximaPermitida = 70;
    int segundosSeguimiento = 5;
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
        
        <script src="../../js/justgage/raphael-2.1.4.min.js"></script>
        <script src="../../js/justgage/justgage.js"></script>

        <style type="text/css">
            .map_dialog input{
                width: 100%;
            }
            
            #wrapper { position: relative; }
            #over_map { 
                position: absolute; top: 30px; right: 10px; z-index: 99; 
                background-color: #DBDBDB;
                opacity: 0.90;
                border-radius: 0.5em;
            }
            
            .over_map_bottom_left { 
                position: absolute; bottom: 30px; left: 10px; z-index: 99; 
                background-color: #DBDBDB;
                opacity: 0.90;
                border-radius: 1em;
            }
        </style>
        
        <script type="text/javascript">
            var tiempoRecargaMilis = <%= tiempoAutoRecargaMilis %>;
            var minutosRecarga = <%= minutosAutoRecarga %> - 1;
            var velocidadMaxima = <%= velocidadMaximaPermitida %>
            var segundosIntervaloSeguimiento = <%= segundosSeguimiento%>;
            var intervaloSeguimiento;
            var marcadorEmpleadoSeguimiento;
            
            var map;
            var poly;
            
            var medirListener;
            var medirDistance;
            var medirLastPoint;
            
            var directionDisplay;
            var directionsService;
            
            var drawingManager;
            var formaDibujada;
            
            var clientes = [];
            var prospectos = [];
            var promotores = [];
            var vehiculos = []; //no implementado
            var puntos = []; // no implementado
            var pedidos = [];
            var cobranzas =[];
            var visitas=[];
            var devoluciones=[];
            var entregas=[];
            
            var global = [];
            var markerRoute2 = [];
            var markerRoute;
            
            function initialize() {
                
                var rendererOptions = {
                    draggable: false
                };
                
                directionsService = new google.maps.DirectionsService();
                directionsDisplay = new google.maps.DirectionsRenderer(rendererOptions);
                
                //var inicio = new google.maps.LatLng(23.634501,-102.552784);//mexico
                // Creamos mapa con ubicacion inicial centrada en la ubicacion de la sucursal
                var inicio = new google.maps.LatLng(<%=empresa.getLatitud()%>,<%=empresa.getLongitud()%>);
                                
                var mapOptions = {
                  zoom: 15,
                  center: inicio,
                  mapTypeId: google.maps.MapTypeId.ROADMAP
                };

                map = new google.maps.Map(document.getElementById('map_canvas'), mapOptions);
                    
                // controlador Pantalla completa
                map.controls[google.maps.ControlPosition.TOP_RIGHT].push(FullScreenControl(map, 'Pantalla Completa', 'Salir Pantalla Completa'));  
                
                directionsDisplay.setMap(map);
                
                drawingManager = new google.maps.drawing.DrawingManager({
                    drawingControl: false,
                    drawingControlOptions: {
                      position: google.maps.ControlPosition.TOP_CENTER,
                      drawingModes: [
                            google.maps.drawing.OverlayType.CIRCLE,
                            google.maps.drawing.OverlayType.POLYGON,
                            google.maps.drawing.OverlayType.RECTANGLE
                      ]
                    },
                    circleOptions:{
                        editable: true,
                        clickable: true
                    },
                    polygonOptions:{
                        editable: false,
                        clickable: true
                    },
                    rectangleOptions:{
                        editable: true,
                        clickable: true
                    }
                  });
                 drawingManager.setMap(map);
                 
                 google.maps.event.addListener(drawingManager, 'overlaycomplete', function(event) {
                    drawingManager.setOptions({
                        drawingMode: null
                    });
                    formaDibujada = event.overlay;
                    if (event.type == google.maps.drawing.OverlayType.CIRCLE){
                        google.maps.event.addListener(formaDibujada, 'radius_changed', function() {
                            $('#txt_map_radius').val(formaDibujada.getRadius().toFixed(2));
                            verificaMarcadores(formaDibujada.getBounds());
                        });
                        google.maps.event.addListener(formaDibujada, 'center_changed', function() {
                            $('#txt_map_radius').val(formaDibujada.getRadius().toFixed(2));
                            verificaMarcadores(formaDibujada.getBounds());
                        });
                        $('#txt_map_radius').val(formaDibujada.getRadius().toFixed(2));
                        verificaMarcadores(formaDibujada.getBounds());
                    }else if(event.type == google.maps.drawing.OverlayType.RECTANGLE) {
                        google.maps.event.addListener(formaDibujada, 'bounds_changed', function() {
                            verificaMarcadores(formaDibujada.getBounds());
                        });
                        verificaMarcadores(formaDibujada.getBounds());
                    }else if(event.type == google.maps.drawing.OverlayType.POLYGON) {
                        verificaMarcadores(polygonBounds(formaDibujada));
                    }
                  });
                
                // Agregamos Caja de herramientas sobre Google Maps, para que sea visible incluso en Pantalla Completa
                // para mas informacion consultar: https://developers.google.com/maps/documentation/javascript/controls?hl=es#ControlsOverview
                var controlDiv = document.getElementById('over_map');
                map.controls[google.maps.ControlPosition.RIGHT_TOP].push(controlDiv);
                var velocimetro = document.getElementById('velocimetro');
                map.controls[google.maps.ControlPosition.LEFT_BOTTOM].push(velocimetro);
                
//              fillMap(); // se cambia de carga sincrona, a asincrona
                setTimeout("iniciarCargaMapaAsincrono()", 1000); //retrasamos la carga inicial por un segundo
                
            }
            
            function polygonBounds(polygon){
                var bounds = new google.maps.LatLngBounds();
                var path = polygon.getPath();
                for (var i = 0; i < path.getLength(); i++) {
                    var unit = path.getAt(i);
                    bounds.extend(unit);
                    
                }
                return bounds;
            }
            
            var divBuscar = 0
            function mostrarOcultarDirecciones(){
                if(divBuscar==1){
                    $("#div_map_tool_buscar").hide();
                    divBuscar = 0;
                }else{
                    $("#div_map_tool_buscar").show();
                    divBuscar = 1;
                }
            }
            
            function circulov2(){
                if(!formaDibujada){
                    drawingManager.setOptions({
                        drawingMode: google.maps.drawing.OverlayType.CIRCLE
                    });
                    $("#btn_map_medir").hide();
                    $("#btn_map_rectangulo").hide();
                    $("#btn_map_poligono").hide();
                    $("#div_map_buscar").hide();
                    $("#btn_map_buscar").hide();
                    $("#div_map_tool_circulo").show();
                    $("#btn_map_circulo").val('Parar');
                }else{
                    formaDibujada.setMap(null);
                    formaDibujada = null;
                    limpiarMapa();
                    $("#btn_map_medir").show();
                    $("#btn_map_rectangulo").show();
                    $("#btn_map_poligono").show();
                    $("#div_map_buscar").show();
                    $("#btn_map_buscar").show();
                    $("#div_map_tool_circulo").hide();
                    $("#btn_map_circulo").val('Círculo');
                }
            }
            
            function rectangulov2(){
                if(!formaDibujada){
                    drawingManager.setOptions({
                        drawingMode: google.maps.drawing.OverlayType.RECTANGLE
                    });
                    $("#btn_map_medir").hide();
                    $("#btn_map_circulo").hide();
                    $("#btn_map_poligono").hide();
                    $("#div_map_buscar").hide();
                    $("#btn_map_buscar").hide();
                    $("#div_map_tool_rectangulo").show();
                    $("#btn_map_rectangulo").val('Parar');
                }else{
                    formaDibujada.setMap(null);
                    formaDibujada = null;
                    limpiarMapa();
                    $("#btn_map_medir").show();
                    $("#btn_map_circulo").show();
                    $("#btn_map_poligono").show();
                    $("#div_map_buscar").show();
                    $("#btn_map_buscar").show();
                    $("#div_map_tool_rectangulo").hide();
                    $("#btn_map_rectangulo").val('Rectángulo');
                }
            }
            
            function poligonov2(){
                if(!formaDibujada){
                    drawingManager.setOptions({
                        drawingMode: google.maps.drawing.OverlayType.POLYGON
                    });
                    $("#btn_map_medir").hide();
                    $("#btn_map_circulo").hide();
                    $("#btn_map_rectangulo").hide();
                    $("#div_map_buscar").hide();
                    $("#div_map_tool_poligono").show();
                    $("#btn_map_buscar").hide();
                    $("#btn_map_poligono").val('Parar');
                }else{
                    formaDibujada.setMap(null);
                    formaDibujada = null;
                    limpiarMapa();
                    $("#btn_map_medir").show();
                    $("#btn_map_circulo").show();
                    $("#btn_map_rectangulo").show();
                    $("#div_map_buscar").show();
                    $("#btn_map_buscar").show();
                    $("#div_map_tool_poligono").hide();
                    $("#btn_map_poligono").val('Polígono');
                }
            }
       
//------------------        
//  fillMap() <-- antiguo metodo eliminado (04/05/2016)
//------------------        
            function limpiarMapa(){
                
                var rendererOptions = {
                    draggable: true
                };
                directionsDisplay.setMap(null);
                //directionsDisplay = new google.maps.DirectionsRenderer(rendererOptions);
                
                if(markerRoute){
                    for(var i = 0, marcador; marcador = markerRoute[i]; i ++){
                        marcador.setMap(null);
                    }
                    markerRoute = [];
                }
            
                for(var i = 0, marcador; marcador = clientes[i]; i ++){
                    marcador.setMap(null);
                }
            
                for(var i = 0, marcador; marcador = prospectos[i]; i ++){
                    marcador.setMap(null);
                }
                for(var i = 0, marcador; marcador = promotores[i]; i ++){
                    marcador.setMap(null);
                }
                /*for(var i = 0, marcador; marcador = vehiculos[i]; i ++){
                    marcador.setMap(null);
                }*/
                for(var i = 0, marcador; marcador = puntos[i]; i ++){
                    marcador.setMap(null);
                }
                for(var i = 0, marcador; marcador = pedidos[i]; i ++){
                    marcador.setMap(null);
                }
            }

            function loadScript() {
                var script = document.createElement('script');
                script.type = 'text/javascript';
                script.src = 'https://maps.googleapis.com/maps/api/js?libraries=geometry,drawing&' +
                    'callback=initialize';
                document.body.appendChild(script);
            }

            window.onload = loadScript;
            
            function creaMarcador(lat,lng,title,img,content){
                var marcador = new google.maps.Marker({
                        position: new google.maps.LatLng(lat,lng), 
                        title:title,
                        animation: google.maps.Animation.DROP,
                        icon:img
                    });
                    
                var infowindow = new google.maps.InfoWindow({
                    content: content
                });
                google.maps.event.addListener(marcador, 'click', function() {
                    infowindow.open(map,marcador);
                });
                
                /* // 06/05/2016 Dejamos de usar esta libreria para los dialogos de marcadores
                   //    debido a que ocasionaba un retraso enorme y la pausa visible en el explorador
                   //    al usarlo para mas de 20 marcadores.
                var infoBubble = new InfoBubble({
                  map: map,
                  content: '<div class="infoBubble">'+content+'</div>',
                  //position: new google.maps.LatLng(-32.0, 149.0),             
                  shadowStyle: 1,
                  padding: 2,
                  backgroundColor: '#CCFFFF',
                  borderRadius: 7,
                  arrowSize: 10,
                  borderWidth: 2,
                  borderColor: '#319AF6',
                  disableAutoPan: false,
                  hideCloseButton: false,
                  arrowPosition: 30,
                  backgroundClassName: 'transparent',
                  arrowStyle: 1
                });
        
                google.maps.event.addListener(marcador, 'click', function() {
                    infoBubble.open(map, marcador);
                });
                */
                
        
                return marcador;
            }
        
            function creaMarcadorBasico(lat,lng,title){
                var marcador = new google.maps.Marker({
                        position: new google.maps.LatLng(lat,lng), 
                        title:title,
                        animation: google.maps.Animation.DROP
                    });
                return marcador;
            }
            
            function muestraMarcador(chk, noServerRefresh, centrarMapa){
                var obtenerDatosServidor = true;
                var centrarMapaEnMarcador = false;
                if (typeof noServerRefresh !== 'undefined') {
                    if (noServerRefresh != null){
                        obtenerDatosServidor = noServerRefresh;
                    }
                }
                if (typeof centrarMapa !== 'undefined') {
                    if (centrarMapa != null){
                        centrarMapaEnMarcador = centrarMapa;
                    }
                }

                $("#chk_"+chk.id).attr('checked', chk.checked);
                
                switch(chk.id){
                    case "clientes":
                        if(chk.checked || centrarMapaEnMarcador){
                            if (clientes.length <= 0 && obtenerDatosServidor){ //no hay marcadores de clientes, buscamos en servidor
                                cargaDetallesMapaAsincrono('clientes', false);
                            }else{ // tenemos marcadores de clientes, los hacemos visibles en mapa
                                for(var i = 0, marcador; marcador = clientes[i]; i ++){
                                    if (!centrarMapaEnMarcador) {
                                        marcador.setMap(map);
                                    }else{
                                        mostrarCentrarMarcador(marcador);
                                    }
                                }
                            }
                        }else{
                            for(var i = 0, marcador; marcador = clientes[i]; i ++){
                                marcador.setMap(null);
                            }
                        }
                        break;
                    case "prospectos":
                        if(chk.checked || centrarMapaEnMarcador){
                            if (prospectos.length <= 0 && obtenerDatosServidor){ //no hay marcadores de prospectos, buscamos en servidor
                                cargaDetallesMapaAsincrono('prospectos', false);
                            }else{ // tenemos marcadores de prospectos, los hacemos visibles en mapa
                                for(var i = 0, marcador; marcador = prospectos[i]; i ++){
                                    if (!centrarMapaEnMarcador) {
                                        marcador.setMap(map);
                                    }else{
                                        mostrarCentrarMarcador(marcador);
                                    }
                                }
                            }
                        }else{
                            for(var i = 0, marcador; marcador = prospectos[i]; i ++){
                                marcador.setMap(null);
                            }
                        }
                        break;
                    case "promotores":
                        if(chk.checked || centrarMapaEnMarcador){
                            if (promotores.length <= 0 && obtenerDatosServidor){ //no hay marcadores de promotores, buscamos en servidor
                                cargaDetallesMapaAsincrono('promotores', false);
                            }else{ // tenemos marcadores de promotores, los hacemos visibles en mapa
                                for(var i = 0, marcador; marcador = promotores[i]; i ++){
                                    if (!centrarMapaEnMarcador) {
                                        marcador.setMap(map);
                                    }else{
                                        mostrarCentrarMarcador(marcador);
                                    }
                                }
                            }
                        }else{
                            for(var i = 0, marcador; marcador = promotores[i]; i ++){
                                marcador.setMap(null);
                            }
                        }
                        break;
                    case "vehiculos":
                        if(chk.checked){
                            if (vehiculos.length <= 0 && obtenerDatosServidor){ //no hay marcadores de vehiculos, buscamos en servidor
                                cargaDetallesMapaAsincrono('vehiculos', false);
                            }else{ // tenemos marcadores de vehiculos, los hacemos visibles en mapa
                                for(var i = 0, marcador; marcador = vehiculos[i]; i ++){
                                    if (!centrarMapaEnMarcador) {
                                        marcador.setMap(map);
                                    }else{
                                        mostrarCentrarMarcador(marcador);
                                    }
                                }
                            }
                        }else{
                            for(var i = 0, marcador; marcador = vehiculos[i]; i ++){
                                marcador.setMap(null);
                            }
                        }
                        break;
                    case "puntos":
                        if(chk.checked){
                            if (puntos.length <= 0 && obtenerDatosServidor){ //no hay marcadores de puntos, buscamos en servidor
                                cargaDetallesMapaAsincrono('puntos', false);
                            }else{ // tenemos marcadores de puntos, los hacemos visibles en mapa
                                for(var i = 0, marcador; marcador = puntos[i]; i ++){
                                    if (!centrarMapaEnMarcador) {
                                        marcador.setMap(map);
                                    }else{
                                        mostrarCentrarMarcador(marcador);
                                    }
                                }
                            }
                        }else{
                            for(var i = 0, marcador; marcador = puntos[i]; i ++){
                                marcador.setMap(null);
                            }
                        }
                        break;
                    case "pedidos":
                        if(chk.checked){
                            if (pedidos.length <= 0 && obtenerDatosServidor){ //no hay marcadores de pedidos, buscamos en servidor
                                cargaDetallesMapaAsincrono('pedidos', false, "<%= filtroBusquedaPedido %>");
                            }else{ // tenemos marcadores de pedidos, los hacemos visibles en mapa
                                for(var i = 0, marcador; marcador = pedidos[i]; i ++){
                                    if (!centrarMapaEnMarcador) {
                                        marcador.setMap(map);
                                    }else{
                                        mostrarCentrarMarcador(marcador);
                                    }
                                }
                            }
                        }else{
                            for(var i = 0, marcador; marcador = pedidos[i]; i ++){
                                marcador.setMap(null);
                            }
                        }
                        break;
                    case "cobranzas":
                        if(chk.checked){
                            if (cobranzas.length <= 0 && obtenerDatosServidor){ //no hay marcadores de cobranzas, buscamos en servidor
                                cargaDetallesMapaAsincrono('cobranzas', false, "<%= strWhereRangoFechasCobranza %>");
                            }else{ // tenemos marcadores de cobranzas, los hacemos visibles en mapa
                                for(var i = 0, marcador; marcador = cobranzas[i]; i ++){
                                    if (!centrarMapaEnMarcador) {
                                        marcador.setMap(map);
                                    }else{
                                        mostrarCentrarMarcador(marcador);
                                    }
                                }
                            }
                        }else{
                            for(var i = 0, marcador; marcador = cobranzas[i]; i ++){
                                marcador.setMap(null);
                            }
                        }
                        break;
                    case "visitas":
                        if(chk.checked){
                            if (visitas.length <= 0 && obtenerDatosServidor){ //no hay marcadores de visitas, buscamos en servidor
                                cargaDetallesMapaAsincrono('visitas', false, "<%= strWhereRangoFechasVisita %>");
                            }else{ // tenemos marcadores de visitas, los hacemos visibles en mapa
                                for(var i = 0, marcador; marcador = visitas[i]; i ++){
                                    if (!centrarMapaEnMarcador) {
                                        marcador.setMap(map);
                                    }else{
                                        mostrarCentrarMarcador(marcador);
                                    }
                                }
                            }
                        }else{
                            for(var i = 0, marcador; marcador = visitas[i]; i ++){
                                marcador.setMap(null);
                            }
                        }
                        break;
                    case "devoluciones":
                        if(chk.checked){
                            if (devoluciones.length <= 0 && obtenerDatosServidor){ //no hay marcadores de devoluciones, buscamos en servidor
                                cargaDetallesMapaAsincrono('devoluciones', false, "<%= strWhereRangoFechasDevolucion %>");
                            }else{ // tenemos marcadores de devoluciones, los hacemos visibles en mapa
                                for(var i = 0, marcador; marcador = devoluciones[i]; i ++){
                                    if (!centrarMapaEnMarcador) {
                                        marcador.setMap(map);
                                    }else{
                                        mostrarCentrarMarcador(marcador);
                                    }
                                }
                            }
                        }else{
                            for(var i = 0, marcador; marcador = devoluciones[i]; i ++){
                                marcador.setMap(null);
                            }
                        }
                        break;
                    case "entregas":
                        if(chk.checked){
                            if (entregas.length <= 0 && obtenerDatosServidor){ //no hay marcadores de entregas, buscamos en servidor
                                cargaDetallesMapaAsincrono('entregas', false, "<%= strWhereRangoFechasEntrega %>");
                            }else{ // tenemos marcadores de entregas, los hacemos visibles en mapa
                                for(var i = 0, marcador; marcador = entregas[i]; i ++){
                                    if (!centrarMapaEnMarcador) {
                                        marcador.setMap(map);
                                    }else{
                                        mostrarCentrarMarcador(marcador);
                                    }
                                }
                            }
                        }else{
                            for(var i = 0, marcador; marcador = entregas[i]; i ++){
                                marcador.setMap(null);
                            }
                        }
                        break;
                }
                guardarVariableEnSesion('s_'+chk.id , chk.checked?'1':'');
            }
            
            function buscarMarcador(){
                $.ajax({
                    type: "POST",
                    url: "mapaBuscador_ajax.jsp",
                    data: $("#form_mapa_buscador").serialize(),
                    beforeSend: function(objeto){
                        //$("#action_buttons").fadeOut("slow");
                        $("#div_map_resultado_buscar").html("");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#div_map_resultado_buscar").html(datos);
                           $("#ajax_loading").fadeOut("slow");
                           //$("#ajax_message").fadeIn("slow");
                           //apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                           //         function(r){
                           //             location.href = "catSucursales_list.jsp";
                           //         });
                       }else{
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").html(datos);
                           $("#ajax_message").fadeIn("slow");
                           //$("#action_buttons").fadeIn("slow");
                           //$.scrollTo('#inner',800);
                       }
                    }
                });
            }
            
            function muestraMarcadorBusqueda(lat,lng,tipo,aux){
                
                var tipoDatos = "";
                if(tipo == "clientes"){
                    tipoDatos = "clientes";
                }else if(tipo == "promotores"){
                    tipoDatos = "promotores";
                } else if(tipo == "prospectos"){
                    tipoDatos = "prospectos";
                }
                
                cargarSesion2(lat,lng,tipo,aux,tipoDatos);
                var marcadorCoincide = null;
                switch(tipo){
                    case "clientes":
                        for(var i = 0, marcador; marcador = clientes[i]; i ++){
                            var posicion = marcador.getPosition();
                            var posicion2 = new google.maps.LatLng(lat,lng);
                            if(posicion.lat()==posicion2.lat() && posicion.lng()==posicion2.lng()){
                                marcadorCoincide = marcador;
                            }
                        }
                        break;
                    case "prospectos":
                        for(var i = 0, marcador; marcador = prospectos[i]; i ++){
                            var posicion = marcador.getPosition();
                            var posicion2 = new google.maps.LatLng(lat,lng);
                            if(posicion.lat()==posicion2.lat() && posicion.lng()==posicion2.lng()){
                                marcadorCoincide = marcador;
                            }
                        }
                        break;
                    case "promotores":
                        for(var i = 0, marcador; marcador = promotores[i]; i ++){
                            var posicion = marcador.getPosition();
                            var posicion2 = new google.maps.LatLng(lat,lng);
                            if(posicion.lat()==posicion2.lat() && posicion.lng()==posicion2.lng()){
                                marcadorCoincide = marcador;
                            }
                        }
                        break;
                    case "vehiculos":
                        for(var i = 0, marcador; marcador = vehiculos[i]; i ++){
                            var posicion = marcador.getPosition();
                            var posicion2 = new google.maps.LatLng(lat,lng);
                            if(posicion.lat()==posicion2.lat() && posicion.lng()==posicion2.lng()){
                                marcadorCoincide = marcador;
                            }
                        }
                        break;
                    case "puntos":
                        for(var i = 0, marcador; marcador = puntos[i]; i ++){
                            var posicion = marcador.getPosition();
                            var posicion2 = new google.maps.LatLng(lat,lng);
                            if(posicion.lat()==posicion2.lat() && posicion.lng()==posicion2.lng()){
                                marcadorCoincide = marcador;
                            }
                        }
                        break;
                    case "pedidos":
                        for(var i = 0, marcador; marcador = pedidos[i]; i ++){
                            var posicion = marcador.getPosition();
                            var posicion2 = new google.maps.LatLng(lat,lng);
                            if(posicion.lat()===posicion2.lat() && posicion.lng()===posicion2.lng()){
                                marcadorCoincide = marcador;
                            }
                        }
                        break;
                    case "cobranzas":
                        for(var i = 0, marcador; marcador = cobranzas[i]; i ++){
                            var posicion = marcador.getPosition();
                            var posicion2 = new google.maps.LatLng(lat,lng);
                            if(posicion.lat()===posicion2.lat() && posicion.lng()===posicion2.lng()){
                                marcadorCoincide = marcador;
                            }
                        }
                        break;
                    case "visitas":
                        for(var i = 0, marcador; marcador = visitas[i]; i ++){
                            var posicion = marcador.getPosition();
                            var posicion2 = new google.maps.LatLng(lat,lng);
                            if(posicion.lat()===posicion2.lat() && posicion.lng()===posicion2.lng()){
                                marcadorCoincide = marcador;
                            }
                        }
                        break;
                    case "devoluciones":
                        for(var i = 0, marcador; marcador = devoluciones[i]; i ++){
                            var posicion = marcador.getPosition();
                            var posicion2 = new google.maps.LatLng(lat,lng);
                            if(posicion.lat()===posicion2.lat() && posicion.lng()===posicion2.lng()){
                                marcadorCoincide = marcador;
                            }
                        }
                        break;
                    case "entregas":
                        for(var i = 0, marcador; marcador = entregas[i]; i ++){
                            var posicion = marcador.getPosition();
                            var posicion2 = new google.maps.LatLng(lat,lng);
                            if(posicion.lat()===posicion2.lat() && posicion.lng()===posicion2.lng()){
                                marcadorCoincide = marcador;
                            }
                        }
                        break;
                }
                
                if (marcadorCoincide!=null){
                    // El marcador de registro se encontro previamente cargado en vista
                    // solo nos aseguramos de mostrarlo y centrarlo
                    mostrarCentrarMarcador(marcadorCoincide);
                }else if (tipoDatos != "" && aux!=""){
                    // El marcador de registro que se quiere mostrar, no ha sido cargado
                    // en los datos de la vista, por lo tanto intentaremos ahora recuperarlo de servidor
                    //      tipoDatos <-- Contiene el tipo de objeto que se busca (string)
                    //      aux <-- Contiene el ID del objeto de BD que se busca
                    cargaDetallesMapaAsincrono(tipoDatos, true, aux);
                }
                
            }
        
            function muestraDetallesPromotor(id, tipo){
                var url = "";
                switch(tipo){
                    case 1:
                        url = "detallesPromotor_ajax.jsp";
                        break;
                    case 2:
                        url = "historicoPromotor_ajax.jsp";
                        break;
                    case 3:
                        url = "cobrosPromotor_ajax.jsp";
                        break;
                    case 4:
                        //url = "mensajePromotor_ajax.jsp";
                        var htmlForm = ""
                            + "<form method='POST' action='' id='frm_mensaje_promotor'>"
                            + "<input type='hidden' name='id' value='" + id + "'>"
                            + "<p>"
                            + "<label>Enviar mensaje a vendedor:</label><br/>"
                            + "<textarea name='txt_mensaje' id='txt_mensaje' rows='8' cols='60'></textarea>"
                            + "</p>"
                            + "<div id='frm_mensaje_action_buttons'>"
                            + "<input type='button' id='enviar' value='Enviar' onclick='enviarMensaje();'/>"
                            + "</div>"
                            + "</form>";
                        $("#div_map_resultado_buscar").html(htmlForm);
                        break;
                    case 5:
                        url = "rutasPromotor_ajax.jsp";
                        break;
                    case 6:
                    url = "pedidosPromotor_ajax.jsp";
                    break;
                }
                if(url!=""){
                    muestraVentanaDetalles(url,id);
                }
            }
        
            function muestraDetallesCliente(id, tipo){
                
                cargarSesion2(id,tipo,"","","detallesCliente");
                var url = "";
                switch(tipo){
                    case 1:
                        url = "detallesCliente_ajax.jsp";
                        break;
                    case 2:
                        url = "pagosCliente_ajax.jsp";
                        break;
                }
                if(url!=""){
                    muestraVentanaDetalles(url,id);
                }
            }
        
            function muestraDetallesProspecto(id, tipo){
                var url = "";
                switch(tipo){
                    case 1:
                        url = "detallesProspecto_ajax.jsp";
                        break;
                }
                if(url!=""){
                    muestraVentanaDetalles(url,id);
                }
            }
            
            function muestraDetallesPedido(id, tipo){
                var url = "";
                switch(tipo){
                    case 1:
                        url = "detallesPedido_ajax.jsp";
                        break;
                }
                if(url!==""){
                    muestraVentanaDetalles(url,id);
                }
            }
        
            function muestraDetallesCobranza(id, tipo){
                var url = "";
                switch(tipo){
                    case 1:
                        url = "detallesCobranza_ajax.jsp";
                        break;
                }
                if(url!==""){
                    muestraVentanaDetalles(url,id);
                }
            }
            
            function muestraDetallesVisita(id, tipo){
                var url = "";
                switch(tipo){
                    case 1:
                        url = "detallesVisita_ajax.jsp";
                        break;
                }
                if(url!==""){
                    muestraVentanaDetalles(url,id);
                }
            }
            
            function muestraDetallesDevolucion(id, tipo){
                var url = "";
                switch(tipo){
                    case 1:
                        url = "detallesDevolucion_ajax.jsp";
                        break;
                }
                if(url!==""){
                    muestraVentanaDetalles(url,id);
                }
            }
            
            function muestraDetallesEntrega(id, tipo){
                var url = "";
                switch(tipo){
                    case 1:
                        url = "detallesPedidoEntrega_ajax.jsp";
                        break;
                }
                if(url!==""){
                    muestraVentanaDetalles(url,id);
                }
            }
        
            function muestraVentanaDetalles(url, id){
                
                $.ajax({
                    type: "GET",
                    url: url,
                    //data: $("#form_mapa_buscador").serialize(),
                    data: {id:id},
                    beforeSend: function(objeto){
                        //$("#action_buttons").fadeOut("slow");
                        //$("#div_map_resultado_buscar").html("");
                        $("#div_map_detalle").html("");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           //$("#div_map_resultado_buscar").html(datos);
                           $("#div_map_detalle").html(datos);
                           $("#ajax_loading").fadeOut("slow");
                           datePicker();
                           //$("#ajax_message").fadeIn("slow");
                           //apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                           //         function(r){
                           //             location.href = "catSucursales_list.jsp";
                           //         });
                       }else{
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").html(datos);
                           $("#ajax_message").fadeIn("slow");
                           //$("#action_buttons").fadeIn("slow");
                           //$.scrollTo('#inner',800);
                       }
                    }
                });
            }
            
            function actualizarHistorico(id){
                $.ajax({
                    type: "GET",
                    url: "historicoPromotor_ajax.jsp",
                    //data: $("#form_mapa_buscador").serialize(),
                    data: {id:id,fechaDe:$("#txt_fecha_de").val(),fechaA:$("#txt_fecha_a").val()},
                    beforeSend: function(objeto){
                        //$("#action_buttons").fadeOut("slow");
                        $("#div_map_resultado_buscar").html("");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#div_map_resultado_buscar").html(datos);
                           $("#ajax_loading").fadeOut("slow");
                           datePicker();
                           //$("#ajax_message").fadeIn("slow");
                           //apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                           //         function(r){
                           //             location.href = "catSucursales_list.jsp";
                           //         });
                       }else{
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").html(datos);
                           $("#ajax_message").fadeIn("slow");
                           //$("#action_buttons").fadeIn("slow");
                           //$.scrollTo('#inner',800);
                       }
                    }
                });
            }
            
            var timeout;
            
            function reproducirHistorico(i){
                limpiarMapa();
                centrarMapa(i,null);
                
            }
            function cambiaRuta(idRuta){
                location.href = "logistica_consulta.jsp?idRuta="+idRuta;
                
            }
        
            function centrarMapa(i,obj){
                if(obj!=null){
                    obj.setMap(null);
                }
                var arr = document.getElementsByName("marcadores_buscar");
                if(arr.length>0 && i<arr.length){
                   
                   var pos = arr[i].value.split(",");
                   var mapOptions = {
                        center: new google.maps.LatLng(pos[0],pos[1]),
                        mapTypeId: google.maps.MapTypeId.ROADMAP
                    };
                    map.setOptions(mapOptions);
                    
                    var marcador = new google.maps.Marker({
                        position: new google.maps.LatLng(pos[0],pos[1]), 
                        title:"marcador",
                        animation: google.maps.Animation.DROP,
                        map:map
                    });
                   
                   //timeout = setTimeout("centrarMapa("+(i+1)+","+marcador+")",1500);
                   timeout = setTimeout(function(){
                       centrarMapa(i+1,marcador,1);
                   },2000);
                
                }
            }
            
            function agregaMarcador(lat, lng, title){
                var crear = 0;
                if(global.length > 0){
                    for(var i = 0, marcador; marcador = global[i]; i ++){
                        var posicion = marcador.getPosition();
                        var posicion2 = new google.maps.LatLng(lat,lng);
                        if(posicion.lat()==posicion2.lat() && posicion.lng()==posicion2.lng()){
                            crear = 0;
                            if(marcador.getMap()==null){
                                marcador.setMap(map);
                            }else{
                                marcador.setMap(null);
                            }
                        }else{
                            crear = 1;
                        }
                    }
                }else{
                    crear = 1;
                }
            //crear = 1;
                if(crear == 1){
                    var marcadorBasico = creaMarcadorBasico(
                            lat,
                            lng,
                            title
                        )
                    global.push(
                      marcadorBasico  
                    );
                    marcadorBasico.setMap(map);
                }
            }
        
            function medir(){
                if(medirListener){
                    $("#btn_map_circulo").show();
                    $("#btn_map_rectangulo").show();
                    $("#div_map_buscar").show();
                    $("#btn_map_buscar").show();
                    $("#btn_map_medir").val('Medir');
                    $("#div_map_medir_tool").hide();
                    $("#btn_map_poligono").show();
                    medirListener = null;
                    medirDistance = 0;
                    medirLastPoint = null;
                    poly.setMap(null);
                    poly = null;
                    $('#txt_map_distance').val('0');
                    google.maps.event.clearListeners(map, 'click');
                    var mapOptions = {
                        draggableCursor:null
                    };
                    map.setOptions(mapOptions);
                }else{
                    $("#btn_map_circulo").hide();
                    $("#btn_map_rectangulo").hide();
                    $("#btn_map_poligono").hide();
                    $("#div_map_buscar").hide();
                    $("#btn_map_buscar").hide();
                    $("#btn_map_medir").val('Parar');
                    $("#div_map_medir_tool").show();
                    var polyOptions = {
                        strokeColor: '#000000',
                        strokeOpacity: 1.0,
                        strokeWeight: 3,
                        editable:false
                    }
                    poly = new google.maps.Polyline(polyOptions);
                    poly.setMap(map);
                    medirListener = addLatLng;
                    medirDistance = 0;
                    medirLastPoint = null;
                    $('#txt_map_distance').val('0');
                    google.maps.event.addListener(map, 'click', medirListener);
                    var mapOptions = {
                        draggableCursor:'crosshair'
                    };
                    map.setOptions(mapOptions);
                }
                
            }
        
            var circuloListener;
            var circle;
            var markerCircle;
        
            function circulo(){
                if(circuloListener){
                    $("#btn_map_medir").show();
                    $("#div_map_circulo_tool").hide();
                    $("#btn_map_circulo").val('Circulo');
                    circuloListener = null;
                    circle.setMap(null);
                    circle=null;
                    markerCircle.setMap(null);
                    google.maps.event.clearListeners(map, 'click');        
                    limpiarMapa();
                    
                }else{
                    $("#btn_map_medir").hide();
                    $("#btn_map_circulo").val('Parar');
                    circuloListener = fCirculoListener;
                    google.maps.event.addListener(map, 'click', circuloListener);
                }
                
            }
        
            function aumentaCirculo(){
                var radius = circle.getRadius();
                if(radius<<10000){
                    circle.setRadius(radius + 50);
                    verificaMarcadores(circle.getBounds());
                }
            }
        
            function disminuyeCirculo(){
                var radius = circle.getRadius();
                if(radius>500){
                    circle.setRadius(radius - 50);
                    verificaMarcadores(circle.getBounds());
                }
            }
        
            function fCirculoListener(event){
                if(!circle){
                    $("#div_map_circulo_tool").show();
                    markerCircle = new google.maps.Marker({
                        position: event.latLng, 
                        map: map,
                        draggable: true
                    });
                    circle = new google.maps.Circle({
                        map: map,
                        clickable: false,
                        // metres
                        radius: 1000,
                        fillColor: '#fff',
                        fillOpacity: .6,
                        strokeColor: '#313131',
                        strokeOpacity: .4,
                        strokeWeight: .8,
                        center: event.latLng
                    });

                    circle.bindTo('center', markerCircle, 'position');

                    var bounds = circle.getBounds();
                    verificaMarcadores(bounds);

                    google.maps.event.addListener(markerCircle, 'dragend', function() {
                        //latLngCenter = new google.maps.LatLng(markerCircle.position.lat(), markerCircle.position.lng());
                        bounds = circle.getBounds();
                        verificaMarcadores(bounds);
                    });

                }
            }
            
            function verificaMarcadores(bounds){
                for(var i = 0, marcador; marcador = clientes[i]; i ++){
                    if(bounds.contains(marcador.getPosition())){
                        marcador.setMap(map);
                    }else{
                        marcador.setMap(null);
                    }
                }
                
                for(var i = 0, marcador; marcador = prospectos[i]; i ++){
                    if(bounds.contains(marcador.getPosition())){
                        marcador.setMap(map);
                    }else{
                        marcador.setMap(null);
                    }
                }
                
                for(var i = 0, marcador; marcador = promotores[i]; i ++){
                    if(bounds.contains(marcador.getPosition())){
                        marcador.setMap(map);
                    }else{
                        marcador.setMap(null);
                    }
                }
                
                /*for(var i = 0, marcador; marcador = vehiculos[i]; i ++){
                    if(bounds.contains(marcador.getPosition())){
                        marcador.setMap(map);
                    }else{
                        marcador.setMap(null);
                    }
                }*/
                
                for(var i = 0, marcador; marcador = puntos[i]; i ++){
                    if(bounds.contains(marcador.getPosition())){
                        marcador.setMap(map);
                    }else{
                        marcador.setMap(null);
                    }
                }
                
                for(var i = 0, marcador; marcador = pedidos[i]; i ++){
                    if(bounds.contains(marcador.getPosition())){
                        marcador.setMap(map);
                    }else{
                        marcador.setMap(null);
                    }
                }
                
                for(var i = 0, marcador; marcador = cobranzas[i]; i ++){
                    if(bounds.contains(marcador.getPosition())){
                        marcador.setMap(map);
                    }else{
                        marcador.setMap(null);
                    }
                }
                
                for(var i = 0, marcador; marcador = visitas[i]; i ++){
                    if(bounds.contains(marcador.getPosition())){
                        marcador.setMap(map);
                    }else{
                        marcador.setMap(null);
                    }
                }
                
                for(var i = 0, marcador; marcador = devoluciones[i]; i ++){
                    if(bounds.contains(marcador.getPosition())){
                        marcador.setMap(map);
                    }else{
                        marcador.setMap(null);
                    }
                }
                
                for(var i = 0, marcador; marcador = entregas[i]; i ++){
                    if(bounds.contains(marcador.getPosition())){
                        marcador.setMap(map);
                    }else{
                        marcador.setMap(null);
                    }
                }
            }
        
            function addLatLng(event) {

                var path = poly.getPath();

                // Because path is an MVCArray, we can simply append a new coordinate
                // and it will automatically appear
                path.push(event.latLng);

                if(medirLastPoint){
                    var distance = google.maps.geometry.spherical.computeDistanceBetween(medirLastPoint, event.latLng);
                    //medirDistance += (distance/1000) ;
                    medirDistance += (distance) ;
                    $('#txt_map_distance').val(medirDistance.toFixed(2));                
                }
                medirLastPoint = event.latLng;
                
                // Add a new marker at the new plotted point on the polyline.
                /*var marker = new google.maps.Marker({
                  position: event.latLng,
                  title: '#' + path.getLength(),
                  map: map
                });*/
          }
      
          function enviarMensaje(){
              $.ajax({
                    type: "POST",
                    url: "envioMensajePromotor_ajax.jsp",
                    data: $("#frm_mensaje_promotor").serialize(),
                    beforeSend: function(objeto){
                        //$("#action_buttons").fadeOut("slow");
                        //$("#div_map_resultado_buscar").html("");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#div_map_resultado_buscar").html(datos);
                           $("#ajax_loading").fadeOut("slow");
                           //$("#ajax_message").fadeIn("slow");
                           //apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                           //         function(r){
                           //             location.href = "catSucursales_list.jsp";
                           //         });
                       }else{
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").html(datos);
                           $("#ajax_message").fadeIn("slow");
                           //$("#action_buttons").fadeIn("slow");
                           //$.scrollTo('#inner',800);
                       }
                    }
                });
          }
          
          function datePicker() {
            if($( "#txt_fecha_de" )){
                $( "#txt_fecha_de" ).datepicker({
                  changeMonth: true,
                  changeYear: true,
                  dateFormat: "dd/mm/yy"
                });
            }
            if($( "#txt_fecha_a" )){
                $( "#txt_fecha_a" ).datepicker({
                  changeMonth: true,
                  changeYear: true,
                  dateFormat: "dd/mm/yy"
                });
            }
          }
        
          function buscarDireccion(){
                var address = $('#txt_direccion').val();
                var geocoder = new google.maps.Geocoder();
                geocoder.geocode({ 'address': address}, geocodeResult);
            }
        
            function geocodeResult(results, status) {
                if (status == 'OK') {
                    var mapOptions = {
                        center: results[0].geometry.location,
                        mapTypeId: google.maps.MapTypeId.ROADMAP
                    };
                    map.setOptions(mapOptions);
                    map.fitBounds(results[0].geometry.viewport);
                    
                    crearMarcadorBasico(results[0].geometry.location);
                    
                } else {
                    alert("Geocoding no tuvo éxito debido a: " + status);
                }
            }
            function crearMarcadorBasico(location){
                var marker = new google.maps.Marker({
                    position: location, 
                    map: map
                });
                google.maps.event.addListener(marker, 'click', function(event) {
                    quitarMarcador(event.latLng);
                });
                markerRoute2.push(marker);
            }
            function quitarMarcador(location){
                for(var i = 0, marcador; marcador = markerRoute2[i]; i ++){
                    if(marcador.getPosition().lat()==location.lat() && marcador.getPosition().lng()==location.lng()){
                        marcador.setMap(null);
                    }
                }
            }
            
            function guardarVariableEnSesion(nombreVar, valorVar, callback){
                var modeAjax = "guardarVariableEnSesion";
                $.ajax({
                    type: "POST",
                    url: "mapa_busqueda_sesion_ajax.jsp",
                    data: {mode : modeAjax, nombre : nombreVar, valor : valorVar },
                    beforeSend: function(objeto){
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#ajax_message").html(datos);
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").fadeIn("slow");
                       }else{
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").html(datos);
                           $("#ajax_message").fadeIn("slow");
                           $.scrollTo('#inner',800);
                       }
                       // Make sure the callback is a function​
                        if (typeof callback === "function") {
                            // Call it, since we have confirmed it is callable​
                            callback(datos);
                        }
                    }
                });
            }
            
            function aplicarFiltroFechas(){
                var valFechaMin = $('#filtro_fecha_min').val();
                var valFechaMax = $('#filtro_fecha_max').val();
                guardarVariableEnSesion('fechaMin', valFechaMin);
                //despues de aplicar la segunda fecha en sesion, recargamos pagina
                guardarVariableEnSesion('fechaMax', valFechaMax, redireccion);
            }
            
            function mostrarCalendario_Rango(){
                var dates = $('#filtro_fecha_min, #filtro_fecha_max').datepicker({
                        //minDate: 0,
			changeMonth: true,
			//numberOfMonths: 2,
                        //beforeShow: function() {$('#fh_min').css("z-index", 9999); },
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 998);
                            }, 500)},
			onSelect: function( selectedDate ) {
				var option = this.id === "filtro_fecha_min" ? "minDate" : "maxDate",
					instance = $( this ).data( "datepicker" ),
					date = $.datepicker.parseDate(
						instance.settings.dateFormat ||
						$.datepicker._defaults.dateFormat,
						selectedDate, instance.settings );
				dates.not( this ).datepicker( "option", option, date );
			}
		});

            }
            
            function aplicarFiltroPedidoEstatus(control){
                var valEstatusPedido = control.value;
                //despues de asignar variable a sesion, enviamos a metodo 'redireccion()' para recargar pagina
                guardarVariableEnSesion('idEstatusPedido', valEstatusPedido, redireccion);
            }
            
            function cambiaIconoOpcionesFiltros(span){
                if($(span).parent().parent().children('.content').css('display') == 'block'){
                    $(span).find('img:first').attr("src","../../images/icon_plus_2.png");
                    //$("#icon_opciones_filtros").attr("src","../../images/icon_plus_2.png");
                }else{
                    $(span).find('img:first').attr("src","../../images/icon_minus_2.png");
                    //$("#icon_opciones_filtros").attr("src","../../images/icon_minus_2.png");
                }
            }
           
            var auxTimeOut;
            function iniciarCargaMapaAsincrono(){
                // Inicia carga de datos desde servidor, solo si estan marcados los checkbox correspondientes
                if ( $("#clientes").is(':checked') ){ cargaDetallesMapaAsincrono('clientes', false); }
                if ( $("#prospectos").is(':checked') ){ cargaDetallesMapaAsincrono('prospectos', false); }
                if ( $("#promotores").is(':checked') ){ cargaDetallesMapaAsincrono('promotores', false); }
                //if ( $("#vehiculos").is(':checked') ){ cargaDetallesMapaAsincrono('vehiculos', false); }
                //if ( $("#puntos").is(':checked') ){ cargaDetallesMapaAsincrono('puntos', false); }
                if ( $("#pedidos").is(':checked') ){ cargaDetallesMapaAsincrono('pedidos', false, "<%= filtroBusquedaPedido %>"); }
                if ( $("#cobranzas").is(':checked') ){ cargaDetallesMapaAsincrono('cobranzas', false, "<%= strWhereRangoFechasCobranza %>" ); }
                if ( $("#visitas").is(':checked') ){ cargaDetallesMapaAsincrono('visitas', false, "<%= strWhereRangoFechasVisita %>" ); }
                if ( $("#devoluciones").is(':checked') ){ cargaDetallesMapaAsincrono('devoluciones', false, "<%= strWhereRangoFechasDevolucion %>" ); }
                if ( $("#entregas").is(':checked') ){ cargaDetallesMapaAsincrono('entregas', false, "<%= strWhereRangoFechasEntrega %>" ); }
                
                // Dentro de N minutos volvemos a invocar este metodo (refrescar datos de servidor)
                if (auxTimeOut!=null) 
                    clearTimeout(auxTimeOut);
                auxTimeOut = setTimeout("iniciarCargaMapaAsincrono()", tiempoRecargaMilis);
                show3(true); //re-iniciar reloj/cronometro
            }
            
            /**
             * Procesa de forma asincrona (mediante ajax) las ubicaciones
             * actualizadas del tipo de objetos que sean requeridos en el
             * parametro tipo.
             * 
             * Antes de procesar vacia el arreglo de marcadores del tipo especificado.
             * Despues de procesar asigna los marcadores al arreglo correspondiente
             * e invoca el metodo para mostrarlos en mapa.
             */
            function cargaDetallesMapaAsincrono( tipo, centrarMapa, extra1 ){
                // vaciamos el arreglo de marcadores correspondiente
                vaciaArregloMarcadores(tipo);
                
                $.ajax({
                        type: "POST",
                        url: "mapa_visor_ajax.jsp",
                        data: { mode : tipo , extra1 : extra1},
                        beforeSend: function(objeto){
                            $("#ajax_message").html('');
                            $("#ajax_message").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                                var strJSON = $.trim(datos.replace('<!--EXITO-->',''));
                                if (strJSON.length>0){
                                    var objeto = JSON.parse(strJSON); //objeto[0]; <-- array de objetos personalizados
                                    //recorremos array de objetos (tipo Java MapUtil.MarcadorMapa)
                                    // los cargamos en el arreglo de marcadores correspondiente
                                    console.log('Objetos devueltos: ' + objeto.length);
                                    objeto.forEach( 
                                        function (item){
                                            cargaDetalle(item);
                                        }
                                    );
                                    // mostramos marcadores en mapa (si es que el usuario solicito verlos)
                                    muestraMarcador( document.getElementById(tipo), false, centrarMapa );
                                }
                                $("#ajax_loading").fadeOut("slow");
                            }else{
                                $("#ajax_loading").fadeOut("slow");
                                $("#ajax_message").html(datos);
                                $("#ajax_message").fadeIn("slow");
                                $.scrollTo('#inner',800);
                            }
                        }
                    });
            }
            
            /**
             * De acuerdo al parametro (String) enviado, vacia el 
             * arreglo de marcadores correspondiente
             */
            function vaciaArregloMarcadores(tipo){
                switch(tipo){
                    case "clientes":
                        //Ocultamos todos los marcadores de clientes del mapa y vaciamos arreglo
                        for(var i = 0, marcador; marcador = clientes[i]; i ++){
                            marcador.setMap(null);
                        }
                        clientes.length = 0;
                        break;
                    case "prospectos":
                        //Ocultamos todos los marcadores de prospectos del mapa y vaciamos arreglo
                        for(var i = 0, marcador; marcador = prospectos[i]; i ++){
                            marcador.setMap(null);
                        }
                        prospectos.length = 0;
                        break;
                    case "promotores":
                        //Ocultamos todos los marcadores de empleados / promotores del mapa y vaciamos arreglo
                        for(var i = 0, marcador; marcador = promotores[i]; i ++){
                            marcador.setMap(null);
                        }
                        promotores.length = 0;
                        break;
                    case "vehiculos":
                        //vehiculos - NO IMPLEMENTADO
                        break;
                    case "puntos":
                        //puntos de interes - NO IMPLEMENTADO                        
                        break;
                    case "pedidos":
                        //Ocultamos todos los marcadores de pedidos del mapa y vaciamos arreglo
                        for(var i = 0, marcador; marcador = pedidos[i]; i ++){
                            marcador.setMap(null);
                        }
                        pedidos.length = 0;
                        break;
                    case "cobranzas":
                        //Ocultamos todos los marcadores de cobranzas del mapa y vaciamos arreglo
                        for(var i = 0, marcador; marcador = cobranzas[i]; i ++){
                            marcador.setMap(null);
                        }
                        cobranzas.length = 0;
                        break;
                    case "visitas":
                        //Ocultamos todos los marcadores de visitas del mapa y vaciamos arreglo
                        for(var i = 0, marcador; marcador = visitas[i]; i ++){
                            marcador.setMap(null);
                        }
                        visitas.length = 0;
                        break;
                    case "devoluciones":
                        //Ocultamos todos los marcadores de devoluciones del mapa y vaciamos arreglo
                        for(var i = 0, marcador; marcador = devoluciones[i]; i ++){
                            marcador.setMap(null);
                        }
                        devoluciones.length = 0;
                        break;
                    case "entregas":
                        //Ocultamos todos los marcadores de entregas del mapa y vaciamos arreglo
                        for(var i = 0, marcador; marcador = entregas[i]; i ++){
                            marcador.setMap(null);
                        }
                        entregas.length = 0;
                        break;
                }
            }
            
            /**
             * Crea un marcador de google maps a partir de un Objeto 
             * tipo Java MapUtil.MarcadorMapa y lo asigna
             * al arreglo de marcadores correspondientes
             */
            function cargaDetalle(item){ // (Objeto tipo Java MapUtil.MarcadorMapa)
                switch(item.tipo){
                    case "clientes":
                        clientes.push( creaMarcador(item.latitud, item.longitud, item.titulo, item.rutaRelativaIcono, item.contenidoDialogo) );
                        break;
                    case "prospectos":
                        prospectos.push( creaMarcador(item.latitud, item.longitud, item.titulo, item.rutaRelativaIcono, item.contenidoDialogo) );
                        break;
                    case "promotores":
                        promotores.push( creaMarcador(item.latitud, item.longitud, item.titulo, item.rutaRelativaIcono, item.contenidoDialogo) );
                        break;
                    case "vehiculos":
                        vehiculos.push( creaMarcador(item.latitud, item.longitud, item.titulo, item.rutaRelativaIcono, item.contenidoDialogo) );
                        break;
                    case "puntos":
                        puntos.push( creaMarcador(item.latitud, item.longitud, item.titulo, item.rutaRelativaIcono, item.contenidoDialogo) );
                        break;
                    case "pedidos":
                        pedidos.push( creaMarcador(item.latitud, item.longitud, item.titulo, item.rutaRelativaIcono, item.contenidoDialogo) );
                        break;
                    case "cobranzas":
                        cobranzas.push( creaMarcador(item.latitud, item.longitud, item.titulo, item.rutaRelativaIcono, item.contenidoDialogo) );
                        break;
                    case "visitas":
                        visitas.push( creaMarcador(item.latitud, item.longitud, item.titulo, item.rutaRelativaIcono, item.contenidoDialogo) );
                        break;
                    case "devoluciones":
                        devoluciones.push( creaMarcador(item.latitud, item.longitud, item.titulo, item.rutaRelativaIcono, item.contenidoDialogo) );
                        break;
                    case "entregas":
                        entregas.push( creaMarcador(item.latitud, item.longitud, item.titulo, item.rutaRelativaIcono, item.contenidoDialogo) );
                        break;
                }
            }
            
            /**
             * Muestra en el mapa el marcador recibido como parametro
             * y lo centra en su posición.
             * @param {google.maps.Marker} marcador
             */
            function mostrarCentrarMarcador(marcador){
                if (marcador != null){
                    // Centramos en posicion de marcador
                    var mapOptions = { //opciones para centrar en marcador con zoom cercano
                            center: marcador.getPosition(),
                            zoom: 15
                        };
                    map.setOptions(mapOptions);
                    
                    //mostramos marcador si esta oculto
                    if(marcador.getMap()==null){
                        marcador.setMap(map);
                    }
                }
            }
                        
            function mostrarConfiguracionDialogo(){
                $("#config_minutos_recarga").val(minutosRecarga + 1);
                $("#div_config_popup").modal({
                    opacity:80,
                    overlayCss: {backgroundColor:"#fff"}
                });
            }
            
            function aplicarNuevaConfiguracion(){
                var valid = true;
                var valMinutosRecarga  = parseInt( $( "#config_minutos_recarga" ).val() , 10);
                var valVelocidadMaxima  = parseInt( $( "#config_velocidad_maxima" ).val() , 10);
                var valSegundosSeguimiento  = parseInt( $( "#config_segundos_seguimiento" ).val() , 10);
                
                $("#validateTips").html('');
                if (valMinutosRecarga < 1 || $( "#config_minutos_recarga" ).val()==''){
                    valid = false;
                    $("#validateTips").html($("#validateTips").html() + 'El tiempo de recarga no puede ser menor a 1 minuto.');
                    alert('El tiempo de recarga no puede ser menor a 1 minuto.');
                }
                if (valVelocidadMaxima < 5 || valVelocidadMaxima>220 || $( "#config_velocidad_maxima" ).val()==''){
                    valid = false;
                    $("#validateTips").html($("#validateTips").html() + 'La velocidad maxima no puede ser menor a 5 km/h ni mayor a 220 km/h.');
                    alert('La velocidad maxima no puede ser menor a 5 km/h ni mayor a 220 km/h.');
                }
                if (valSegundosSeguimiento < 3 || $( "#config_segundos_seguimiento" ).val()==''){
                    valid = false;
                    $("#validateTips").html($("#validateTips").html() + 'El intervalo de actualización de seguimiento a empleado no puede ser menor a 3 segundos.');
                    alert('El intervalo de actualización de seguimiento a empleado no puede ser menor a 3 segundos.');
                }
                
                if (valid){
                    //actualizamos variables de javascript
                    minutosRecarga = valMinutosRecarga - 1;
                    tiempoRecargaMilis = valMinutosRecarga * (60 * 1000) ;
                    velocidadMaxima = valVelocidadMaxima;
                    segundosIntervaloSeguimiento = valSegundosSeguimiento;
                    
                    //cerramos ventana modal (pop-up)
                    $.modal.close();
                    
                    //modificamos maximo de velocidad en vista
                    modificaMaxVelocimetro(velocidadMaxima);
                    //detenemos seguimiento a empleado si hay uno activo (para forzar a que inicie de nuevo y tome el intervalo en segundos configurado)
                    if (intervaloSeguimiento){
                        $('#a_fin_seguir').click();
                    }
                    //se refresca mapa con nuevo intervalo de Recarga
                    iniciarCargaMapaAsincrono();
                }
                return valid;
            }
            
            function obtenHoraActual(){
                var date = new Date;

                var seconds = date.getSeconds();
                var minutes = date.getMinutes();
                var hour = date.getHours();
                
                return ''+ hour + ':' + minutes + ':' + seconds;
            }
            
            var idEmpleadoSeguir = -1;
            function iniciaSigueEmpleado(idEmpleado){
                if (idEmpleado>0){
                    
                    //nos aseguramos de que no haya un intervalo actual ejecutandose
                    if (intervaloSeguimiento){
                        //si se esta ejecutando, lo detenemos antes de comenzar uno nuevo
                        clearInterval(intervaloSeguimiento);
                    }
                    
                    if ( $("#promotores").is(':checked') ){ 
                        //ocultamos promotores
                        $("#promotores").click();
                    }
                    // deshabilitamos select de empleados
                    $('#seguir_id_empleado').attr("disabled", true);
                    //switcheamos control de reproduccion/detener
                    $('#a_inicio_seguir').fadeOut();
                    $('#a_fin_seguir').fadeIn();
                    //mostramos velocimetro
                    $('#velocimetro').fadeIn();

                    idEmpleadoSeguir = idEmpleado;
                    //Ejecutamos la primera vez de inmediato y luego cada N segundos con un intervalo
                    actualizaSigueEmpleado();
                    intervaloSeguimiento = setInterval(actualizaSigueEmpleado, segundosIntervaloSeguimiento * 1000);
                }else{
                   apprise('<center><img src=../../images/warning.png> <br/>Debes elegir un Empleado para comenzar su seguimiento.</center>',{'animate':true}); 
                }
            }
            
            //var tempIdUbicacion = 6768;
            function actualizaSigueEmpleado(){
                
                //actualizaTituloVelocimetro(obtenHoraActual());
                //actualizaVelocimetro(getRandomInt(0, 100)); // actualizamos velocidad
                
                $.ajax({
                    type: "POST",
                    url: "mapa_visor_ajax.jsp",
                    data: { mode : 'censar_ubica_empleado' , idEmpleado : idEmpleadoSeguir //, 
                            //extra1 : ' AND id_bitacora_posicion = ' + tempIdUbicacion
                        },
                    beforeSend: function(objeto){
                        $("#ajax_message").html('');
                        $("#ajax_message").fadeOut("slow");
                        console.log('Consultando ubicacion - ' + obtenHoraActual());
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                            var strJSON = $.trim(datos.replace('<!--EXITO-->',''));
                            if (strJSON.length>0){
                                var objeto = JSON.parse(strJSON); //objeto[0]; <-- array de objetos personalizados
                                
                                actualizaTituloVelocimetro(objeto.fechaHr); //actualizamos hora y minuto de la ubicacion
                                actualizaVelocimetro(objeto.velocidadKmHr);
                                var latE = objeto.latitud;
                                var longE = objeto.longitud;
                                var direccionGrados = objeto.direccionGrados;
                                
                                
                                var iconSVG = {
                                    path: getImageSVGPath(), //google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
                                    rotation: direccionGrados,
                                    scale: 0.01,
                                    fillColor: '#000000',
                                    fillOpacity: 1
                                }
                                
                                var mapOptionsSeguimiento;
                                // actualizar el marcador en ubicacion, y centrar el mapa en el
                                // mover la imagen del marcador dependiendo de la direccion de avance
                                if (marcadorEmpleadoSeguimiento === undefined || marcadorEmpleadoSeguimiento == null) {
                                    // si no esta definido el marcador de Maps, lo creamos
                                    marcadorEmpleadoSeguimiento = new google.maps.Marker({
                                            position: new google.maps.LatLng(latE,longE), 
                                            title: objeto.titulo,
                                            animation: google.maps.Animation.DROP,
                                            icon: iconSVG,
                                            map: map
                                        });
                                    
                                    mapOptionsSeguimiento = { //opciones para centrar en marcador con zoom cercano
                                            center: marcadorEmpleadoSeguimiento.getPosition(),
                                            zoom: 17
                                        };
                                }else{
                                    //actualizamos ubicacion y direccion del icono marcador
                                    marcadorEmpleadoSeguimiento.setPosition(new google.maps.LatLng(latE,longE));
                                    marcadorEmpleadoSeguimiento.setIcon(iconSVG);
                                    mapOptionsSeguimiento = { //opciones para centrar en marcador con zoom cercano
                                            center: marcadorEmpleadoSeguimiento.getPosition()
                                        }
                                }
                                
                                //centramos mapa en marcador
                                map.setOptions(mapOptionsSeguimiento);
                                
                            }
                        }else{
                            $("#ajax_message").html(datos);
                            $("#ajax_message").fadeIn("slow");
                            $.scrollTo('#inner',800);
                        }
                    }
                });
                //tempIdUbicacion++;
            }
            
            function getImageSVGPath(){
                return 'M805 3831 c9 -16 -14 -23 -114 -37 -53 -7 -116 -17 -141 -22 -45 -9 -125 -46 -136 -63 -3 -5 -20 -16 -37 -25 -64 -30 -86 -45 -115 -78 -29 -34 -30 -37 -36 -173 -8 -172 -13 -203 -31 -203 -35 0 -39 -65 -13 -203 8 -43 8 -80 0 -140 -23 -169 -23 -181 5 -213 l25 -29 -6 -235 c-3 -129 -9 -297 -13 -373 -5 -119 -4 -137 9 -132 8 3 21 -1 28 -11 13 -14 15 -11 25 26 9 36 15 44 47 55 60 20 61 16 56 -248 -6 -234 -19 -334 -54 -399 -8 -14 -14 -31 -14 -37 0 -7 -11 -24 -25 -38 -22 -25 -25 -25 -31 -9 -4 10 -2 125 5 255 17 352 16 382 -16 350 -8 -8 -15 -4 -27 12 -15 21 -16 -2 -18 -321 l-3 -345 -37 -3 c-20 -2 -50 4 -67 12 -45 24 -71 21 -71 -8 0 -30 43 -66 77 -66 29 0 83 -26 83 -41 0 -5 -12 -14 -26 -19 -23 -9 -25 -14 -19 -42 9 -39 9 -429 1 -456 -4 -12 2 -28 18 -45 23 -25 24 -32 19 -97 -4 -46 -1 -85 7 -113 13 -42 55 -97 73 -97 6 0 24 -10 41 -23 17 -13 38 -27 46 -30 8 -4 29 -16 45 -27 17 -11 39 -24 50 -30 34 -16 55 -37 55 -53 0 -9 18 -25 42 -37 40 -19 61 -20 423 -20 362 0 383 1 423 20 24 12 42 28 42 37 0 16 21 37 55 53 11 6 32 18 47 28 15 9 55 35 90 56 35 22 72 49 84 60 33 34 48 96 40 174 -6 64 -5 72 16 96 22 26 23 32 17 149 -6 115 -4 249 6 345 4 38 2 44 -20 52 -14 5 -25 14 -25 19 0 15 54 41 83 41 13 0 36 9 50 20 14 11 30 20 36 20 6 0 11 11 11 25 0 21 -5 25 -30 25 -17 0 -45 -7 -61 -16 -17 -8 -47 -14 -67 -12 l-37 3 -6 330 c-3 182 -6 330 -7 330 -1 0 -16 1 -33 1 l-32 1 6 -156 c4 -86 10 -220 13 -297 4 -78 4 -150 0 -159 -6 -17 -9 -16 -31 5 -14 13 -25 27 -25 31 0 4 -5 20 -12 36 -41 100 -49 157 -55 406 -3 142 -2 252 3 257 12 12 83 -12 95 -31 5 -8 9 -24 9 -36 0 -13 5 -23 10 -23 6 0 10 5 10 10 0 6 10 10 21 10 21 0 21 3 15 93 -3 50 -8 216 -12 367 l-6 275 25 28 c27 32 27 43 6 206 -8 61 -8 103 0 155 22 146 19 196 -13 196 -21 0 -24 18 -32 189 l-7 154 -35 38 c-19 22 -40 39 -47 39 -6 0 -22 9 -35 20 -13 11 -28 20 -35 20 -7 0 -20 9 -30 20 -10 11 -22 20 -27 20 -6 0 -21 9 -35 20 -14 11 -37 20 -51 20 -14 0 -39 4 -56 9 -17 6 -69 14 -115 19 -58 7 -89 15 -97 26 -9 13 -34 16 -143 16 -79 0 -130 -4 -126 -9z m380 -686 c151 -24 254 -66 290 -117 35 -52 4 -332 -41 -365 -15 -11 -21 -10 -36 4 -33 30 -85 45 -243 73 -154 26 -507 5 -603 -36 -18 -8 -38 -14 -44 -14 -7 0 -21 -9 -33 -20 -18 -17 -24 -18 -39 -7 -44 32 -75 315 -41 365 79 111 471 170 790 117z m412 -500 c4 -59 1 -69 -18 -86 -12 -10 -33 -19 -47 -19 -24 0 -25 2 -19 53 3 28 15 71 27 95 17 34 24 41 37 33 13 -7 18 -27 20 -76z m-1264 36 c10 -22 20 -62 24 -90 6 -49 5 -51 -19 -51 -14 0 -35 9 -47 19 -18 16 -21 28 -20 82 2 73 4 79 28 79 10 0 24 -15 34 -39z m5 -197 c22 -15 22 -19 22 -229 l0 -215 -36 -15 c-65 -27 -62 -36 -66 208 -2 123 1 232 6 245 11 26 42 29 74 6z m1262 0 c10 -11 13 -68 11 -237 -1 -122 -5 -225 -9 -229 -4 -4 -25 -1 -47 6 l-40 14 -3 214 c-3 207 -2 213 19 231 26 21 52 22 69 1z m-936 -1095 c100 -13 449 -13 541 0 39 5 96 12 127 15 l57 6 33 -42 c69 -89 107 -171 132 -289 17 -81 11 -91 -70 -112 -280 -72 -826 -70 -1104 3 l-65 17 2 63 c2 42 10 73 23 95 11 18 26 48 33 67 19 55 101 187 117 191 18 4 64 0 174 -14z';
            }
            
            function terminaSigueEmpleado(){
                //reiniciamos variables
                idEmpleadoSeguir = -1;
                //quitamos marcador de mapa
                marcadorEmpleadoSeguimiento.setMap(null);
                marcadorEmpleadoSeguimiento = null;
                //detenemos intervalo de repeticion
                clearInterval(intervaloSeguimiento);
                // habilitamos select de empleados
                $('#seguir_id_empleado').attr("disabled", false);
                //switcheamos control de reproduccion/detener
                $('#a_fin_seguir').fadeOut();
                $('#a_inicio_seguir').fadeIn();
                //ocultamos velocimetro
                actualizaVelocimetro(0);
                $('#velocimetro').fadeOut();
            }
            
            var g;
            function iniciaVelocimetro(){
                g = new JustGage({
                    id: "velocimetro",
                    value: 0,
                    min: 0,
                    max: velocidadMaxima,
                    label: "Km/h",
                    title: "Velocidad",
                    shadowOpacity: 1,
                    shadowSize: 5,
                    shadowVerticalOffset: 10,
                    labelFontColor: 'black',
                    titleFontColor:'black'
                  });
            }
            
            /**
             * Actualiza el valor en el velocimetro
             */
            function actualizaVelocimetro(value){
                //g.refresh(getRandomInt(0, 100));
                g.refresh(value);
            }
            
            /**
             * Actualiza el titulo/etiqueta que se encuentra arriba del velocimetro
             */
            function actualizaTituloVelocimetro(value){
                g.txtTitle.attr({
                    "text": value
                });
            }
            
            /**
             * Actualiza la capacidad maxima del velocimetro
             */
            function modificaMaxVelocimetro(newMax){
                g.refresh(g.originalValue, newMax);
            }
            
      </script>
            
            
      <script language="javascript1.1">
         
          var dn;
          c1=new Image(); c1x="../../images/reloj/c1.png";
          c2=new Image(); c2x="../../images/reloj/c2.png";
          c3=new Image(); c3x="../../images/reloj/c3.png";
          c4=new Image(); c4x="../../images/reloj/c4.png";
          c5=new Image(); c5x="../../images/reloj/c5.png";
          c6=new Image(); c6x="../../images/reloj/c6.png";
          c7=new Image(); c7x="../../images/reloj/c7.png";
          c8=new Image(); c8x="../../images/reloj/c8.png";
          c9=new Image(); c9x="../../images/reloj/c9.png";
          c0=new Image(); c0x="../../images/reloj/c0.png";
          cb=new Image(); cbx="../../images/reloj/c0.png";
          cam=new Image(); camx="../../images/reloj/cam.png";
          cpm=new Image(); cpmx="../../images/reloj/cpm.png";

          function extract(h,m,s,type){
                  if (!document.images)
                          return;
                  if (h<=9){
                          //document.images.a.src=cb.src;
                          document.getElementById("a").src=cbx;
                          document.getElementById("b").src=eval("c"+h+"x");
                  }
                  else {
                          document.getElementById("a").src=eval("c"+Math.floor(h/10)+"x");
                          document.getElementById("b").src=eval("c"+(h%10)+"x");
                  }
                  if (m<=9){
                          document.getElementById("d").src=c0x;
                          document.getElementById("e").src=eval("c"+m+"x");
                  }
                  else {
                          document.getElementById("d").src=eval("c"+Math.floor(m/10)+"x");
                          document.getElementById("e").src=eval("c"+(m%10)+"x");
                  }
                  if (s<=9){
                          document.getElementById("g").src=c0x;
                          document.getElementById("h").src=eval("c"+s+"x");
                  }
                  else {
                          document.getElementById("g").src=eval("c"+Math.floor(s/10)+"x");
                          document.getElementById("h").src=eval("c"+(s%10)+"x");
                  }
                  /*if (dn=="AM") 
                          document.j.src=cam.src;
                  else 
                          document.images.j.src=cpm.src;*/
          }
                               
          var minutes= minutosRecarga;//4
          var seconds=60;
          var primerEjecucion = true;
          function show3(reiniciar){
                if (reiniciar){
                    minutes= minutosRecarga;// 4;
                    seconds=60;
                }
                if (!document.images)
                        return;                  
                var hours=0;

                seconds = seconds - 1;
                if(seconds == 0){
                    seconds=60;
                    minutes = minutes - 1;
                }
                if (minutes < 0) {
                    minutes = minutosRecarga;//4;
                }
                dn="AM" ;                                    
                extract(hours,minutes,seconds,dn);
                if (primerEjecucion || !reiniciar){
                    setTimeout("show3(" + false +")",1000);
                    primerEjecucion = false;
                }
          }

          //-->

      </script>      
    </head>
    <body <%if(dashboard == 1){%>class="nobg"<%}%>>
        <div class="content_wrapper">

            <%if(dashboard == 0){%>
            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>
            <%}%>

            <!-- Inicio de Contenido -->
            <div <%if(dashboard == 0){%>id="content"<%}%>>
                
                <div class="inner">
                    <%if(dashboard == 0){%>
                    <div class="wrapper">
                        <div id="column-1" style="width: 10%">
                            <h1>Mapas</h1>
                        </div>
                        <div id="column-2" style="width: 15%">
                            <!--<img src="../../images/reloj/letrero.png" name="letrero" id="letrero">-->
                            Se actualizará el mapa en:<br/>
                            <img src="../../images/reloj/c0.png" name="a" id="a"><img src="../../images/reloj/c0.png" name="b" id="b"><img src="../../images/reloj/colon.png" name="c" id="c"><img src="../../images/reloj/c0.png" name="d" id="d"><img src="../../images/reloj/c0.png" name="e" id="e"><img src="../../images/reloj/colon.png" name="f" id="f"><img src="../../images/reloj/c0.png" name="g" id="g"><img src="../../images/reloj/c0.png" name="h" id="h">
                            <!--<li><a href="mapa_visor.jsp" title="Refrescar" >Refrescar Página Completa</a></li>-->
                            <br/>
                            <a href="#" onclick="mostrarConfiguracionDialogo();" title="Modificar Configuración" >
                                <img src="../../images/icon_config.png" />
                            </a>
                            &nbsp;&nbsp;
                            <a href="#" onclick="iniciarCargaMapaAsincrono();" title="Refrescar" >Refrescar Mapa Ahora</a>
                        </div>
                        <div id="column-3" style="width: 72%">
                            <table style="width: 90%; margin-left: 2em;">
                                <tr>
                                    <td colspan="5"><b>Información de Hoy (<%= DateManage.dateToStringEspanol(new Date()) %>)</b></td>
                                </tr>
                                <tr>
                                    <td>Venta total: $ <%= indicadorVentaTotal %></td>
                                    <td>Cobranza total: $ <%= indicadorCobranzaTotal %></td>
                                    <td>Visitas NO Venta: <%= indicadorVisitas %></td>
                                    <td>Devoluciones/Cambios: <%= indicadorDevolucionesCambios  %> </td>
                                    <td>Clientes pendientes: <%= indicadorClientesPendientes %> </td>
                                </tr>
                                <tr>
                                    <td>Empleados activos: <%= indicadorEmpleadosActivos %></td>
                                    <td>Empleados inactivos: <%= indicadorEmpleadosInactivos %></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <%}%>

                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--<form id="form_data" name="form_data" action="" method="post">-->
                    <!--<div class="twocolumn" style="margin: 1px 0 1px 0;">
                            <div class="column_left" style="<%if(dashboard == 1){%>visibility: hidden<%}%>">
                    -->
                    <div class="onecolumn" style="margin: 1px 0 0px 0; <%if(dashboard == 1){%>visibility: hidden<%}%>">
                        <div class="header">
                            <span onclick="cambiaIconoOpcionesFiltros(this);">
                                <img id="icon_opciones_filtros" src="../../images/icon_plus_2.png" alt="icon"/>
                                Opciones
                            </span>
                            <div class="switch" style="width:410px"></div>
                        </div>
                        <br class="clear"/>
                        <div class="content" style="display: none;">
                            <!--<div class="twocolumn" style="margin: 1px 0 1px 0;">-->
                            <div id="threecolumn" class="threecolumn">
						
                                <div class="threecolumn_each"><!--<div class="column_left">-->
                                    <label><h4>Mostrar en Mapa:</h4></label>
                                    <div class="wrapper">
                                        <input id="puntos" type="checkbox" onclick="muestraMarcador(this);"/>Puntos de inter&eacute;s :
                                        &nbsp;&nbsp;
                                        <select id="cmb_tipo_punto" name="cmb_tipo_punto">
                                            <option value="todos" selected>Todos</option>
                                            <option value="otros">otros...</option>
                                        </select>
                                        <br/>
                                        <input id="pedidos" type="checkbox" onclick="muestraMarcador(this);"/>Pedidos :
                                        &nbsp;&nbsp;
                                        <select id="cmb_pedido_estatus" name="cmb_pedido_estatus" onchange="aplicarFiltroPedidoEstatus(this);">
                                            <option value="todos" <%= (idEstatusPedido<=0?"selected":"") %> >Todos</option>
                                            <% out.print( new SGEstatusPedidoBO(user.getConn()).getEstatusPedidosByIdHTMLCombo(idEstatusPedido) ); %>
                                        </select>
                                    </div>
                                    <div class="wrapper">
                                        <div id="column-1" style="width: 50%">
                                            <input id="clientes" type="checkbox" onclick="muestraMarcador(this);"/>Clientes<br/>
                                            <input id="prospectos" type="checkbox" onclick="muestraMarcador(this);"/>Prospectos<br/>
                                            <input id="promotores" type="checkbox" onclick="muestraMarcador(this);"/>Empleados<br/>
                                        </div>
                                        <div id="column-2" style="width: 50%">
                                            <input id="visitas" type="checkbox" onclick="muestraMarcador(this);"/>Visitas<br/>
                                            <input id="cobranzas" type="checkbox" onclick="muestraMarcador(this);"/>Cobranza<br/>
                                            <input id="devoluciones" type="checkbox" onclick="muestraMarcador(this);"/>Devoluciones<br/>
                                            <input id="entregas" type="checkbox" onclick="muestraMarcador(this);"/>Entregas<br/>
                                        </div>
                                        <!--<input id="vehiculos" type="checkbox" onclick="muestraMarcador(this);"/>Veh&iacute;culos<br/>-->
                                        <!--<input type="button" onclick="calcRoute();" value="Ruta"/>-->
                                    </div>
                                    <div class="wrapper">
                                        Fechas: 
                                        <input type="text" name="filtro_fecha_min" id="filtro_fecha_min" readonly
                                            value="<%= DateManage.formatDateToNormal(fechaMin != null ? fechaMin : new Date()) %>"
                                            style="width: 80px;"/>                                        
                                        &harr;
                                        <input type="text" name="filtro_fecha_max" id="filtro_fecha_max" readonly
                                            value="<%= DateManage.formatDateToNormal(fechaMax != null ? fechaMax : new Date()) %>"
                                            style="width: 80px;"/>
                                        <input type="button" value="Aplicar" onclick="aplicarFiltroFechas();"/>
                                        <!--
                                        <div id="column-1" style="width: 50%">
                                            
                                        </div>
                                        <div id="column-2" style="width: 50%">
                                            &nbsp;
                                            
                                        </div>
                                        -->
                                    </div>
                                    <p>
                                        <form id="form_mapa_buscador" name="form_mapa_buscador" action="" method="post">
                                            <label><h4>Busqueda Avanzada: <h4></label>
                                            <input maxlength="30" type="text" id="txt_buscar" name="txt_buscar" style="width:300px" value=""/>
                                            <select id="cmb_tipo_buscar" name="cmb_tipo_buscar">
                                                <option value="todos" selected>Todos</option>
                                                <option value="clientes">Clientes</option>
                                                <option value="prospectos">Prospectos</option>
                                                <option value="promotores">Empleados</option>                                                
                                                <!--<option value="vehiculos">Veh&iacute;culos</option>-->
                                                <!--<option value="puntos">Puntos de inter&eacute;s</option>-->
                                            </select>
                                            <!--<input type="image" src="../../images/icon_consultar.png" title="Buscar" onclick="buscarMarcador();"/>-->
                                            <input type="button" value="Buscar" onclick="buscarMarcador();"/>
                                        </form>
                                    </p>
                                </div>
                                <div class="threecolumn_each"><!--<div class="column_right">-->
                                    <div id="div_map_resultado_buscar" style="height: 210px;overflow: scroll;"></div>
                                    <br class="clear"/> 
                                </div>
                                <div class="threecolumn_each" style="height: 210px;">
                                    <div id="div_map_detalle" style="height: 210px;overflow: scroll;">...</div>
                                    <br class="clear"/>
                                </div>
                            </div>
                            
                            <br class="clear"/> 
                        </div>
                    </div>
                    
                        <!--</div>
                    </div>-->
                    
                    <br class="clear"/> 
                    
                    <div class="onecolumn" style="margin: 0px 0 1px 0; <%if(dashboard == 1){%>position: absolute; width: 99.5%;<%}%>" >
                        <div class="header">
                            <span>
                                <img src="../../images/icon_mapa.png" alt="icon"/>
                                Visor
                            </span>
                            <div class="switch" style="width:200px">

                            </div>
                        </div>
                        <br class="clear"/>
                        <div class="content">

                            <input type="image" src="../../images/maps/tool_medir_distancia.png" id="btn_map_medir" value="Medir" title="Medir distancia" onclick="medir();"/>
                            <input type="image" src="../../images/maps/tool_puntos_circulo.png" id="btn_map_circulo" value="C&iacute;rculo" title="Seleccionar puntos por círculo" onclick="circulov2();"/>
                            <input type="image" src="../../images/maps/tool_puntos_rectangulo.png" id="btn_map_rectangulo" value="Rect&aacute;ngulo" title="Seleccionar puntos por rectánculo" onclick="rectangulov2();"/>
                            <input type="image" src="../../images/maps/tool_puntos_poligono.png" id="btn_map_poligono" value="Pol&iacute;gono" title="Seleccionar puntos por polígono" onclick="poligonov2();"/>
                            <input type="image" src="../../images/maps/tool_mostrar_ocultar.png" id="btn_map_buscar" value="Buscar" title="Mostrar/Ocultar buscador de direcciones" onclick="mostrarOcultarDirecciones();"/>

                            <span id="div_map_medir_tool" style="display: none;">
                                Herramienta Medir distancia:<br/>
                                Dibuje una linea sobre el mapa haciendo click en él.<br/>
                                Distancia: <input id="txt_map_distance" type="text" size="5" readonly /> mts.
                            </span>
                            <div id="div_map_tool_circulo" style="display: none;">
                                Herramienta Seleccionar puntos por c&iacute;rculo:<br/>
                                Hacer click sobre el mapa para comenzar a trazar.<br/>
                                Rango de b&uacute;squeda:<input id="txt_map_radius" type="text" size="5" readonly /> mts.
                            </div>
                            <div id="div_map_tool_rectangulo" style="display: none;">
                                Herramienta Seleccionar puntos por rect&aacute;ngulo:<br/>
                                Hacer click sobre el mapa para comenzar a trazar.
                            </div>
                            <div id="div_map_tool_poligono" style="display: none;">
                                Herramienta Seleccionar puntos por pol&iacute;gono:<br/>
                                Hacer click sobre el mapa para comenzar a trazar.
                            </div>
                            <div id="div_map_tool_buscar" style="display: none;">
                                <input type="text" id="txt_direccion" name="txt_direccion" title="Ingresa la dirección a encontrar" style="width:200px"/>
                                <input type="image" src="../../images/maps/tool_mostrar_ocultar.png" title="Buscar dirección" onclick="buscarDireccion();" value="Buscar"/>
                            </div>
                            <!--
                            <input type="button" id="btn_map_medir" value="Medir" onclick="medir();"/>
                            <span id="div_map_medir_tool" style="display: none;">
                                <input id="txt_map_distance" type="text" size="5" readonly /> km
                            </span>
                            <input type="button" id="btn_map_circulo" value="C&iacute;rculo" onclick="circulov2();"/>
                            <input type="button" id="btn_map_rectangulo" value="Rect&aacute;ngulo" onclick="rectangulov2();"/>
                            <input type="button" id="btn_map_poligono" value="Pol&iacute;gono" onclick="poligonov2();"/>
                            -->
                            <!--<input type="button" id="btn_map_circulo" value="Circulo" onclick="circulo();"/>
                            <span id="div_map_circulo_tool" style="display: none;">
                                <input id="circulo_plus" type="button" value="+" onclick="aumentaCirculo();"/>
                                <input id="circulo_less" type="button" value="-" onclick="disminuyeCirculo()"/>
                            </span>-->
                            <!--<input type="button" value="Limpiar" onclick="limpiarMapa();"/>-->
                            &nbsp;&nbsp;&nbsp;
                            <label>Seguir a:</label>
                            <select id="seguir_id_empleado" name="seguir_id_empleado" style="width: 300px;">
                                <option value="-1" selected>Ninguno</option>
                                <%= new EmpleadoBO(user.getConn()).getEmpleadosByIdHTMLCombo(idEmpresa, -1, " AND ID_ESTATUS=1 AND ID_DISPOSITIVO>0 ") %> 
                            </select>
                            &nbsp;
                            <a id="a_inicio_seguir" href="#" onclick="iniciaSigueEmpleado($('#seguir_id_empleado').val());">
                                <img src="../../images/control_play_blue.png" />
                            </a>
                            <a id="a_fin_seguir" href="#" onclick="terminaSigueEmpleado();" style="display: none;">
                                <img src="../../images/control_stop_blue.png" />
                            </a>
                            
                            
                            <div id="wrapper">
                                <div id="google_map">
                                    <div id="map_canvas" style="height: 400px;width: auto">
                                        <img src="../../images/maps/ajax-loader.gif" alt="Cargando" style="margin: auto;"/>
                                    </div>
                                </div>

                                <!-- Caja de herramientas flotante sobre mapa Toolbox -->
                                <div id="over_map">
                                    <table>
                                        <tr>
                                            <td>
                                                <input type="checkbox" id="chk_clientes" title="Ver Clientes" onclick="$('#clientes').click(); return;"><label for="check" title="Ver Clientes"><img src="../../images/maps/map_marker_cte.png" width="16" height="16"></label>
                                            </td>
                                            <td>
                                                <input type="checkbox" id="chk_prospectos" title="Ver Prospectos" onclick="$('#prospectos').click(); return;"><label for="check" title="Ver Prospectos"><img src="../../images/maps/map_marker_pros.png" width="16" height="16"></label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                               <input type="checkbox" id="chk_promotores" title="Ver Empleados" onclick="$('#promotores').click(); return;"><label for="check" title="Ver Empleados"><img src="../../images/estatusEmpleado/icon_activoTrabajando.png" width="16" height="16"></label>    
                                            </td>
                                            <td>
                                                <input type="checkbox" id="chk_pedidos" title="Ver Pedidos" onclick="$('#pedidos').click(); return;"><label for="check" title="Ver Pedidos"><img src="../../images/maps/map_marker_cart.png" width="16" height="16"></label>    
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                               <input type="checkbox" id="chk_cobranzas" title="Ver Cobranzas" onclick="$('#cobranzas').click(); return;"><label for="check" title="Ver Cobranzas"><img src="../../images/maps/map_marker_cobra.png" width="16" height="16"></label>    
                                            </td>
                                            <td>
                                                <input type="checkbox" id="chk_visitas" title="Ver Visitas" onclick="$('#visitas').click(); return;"><label for="check" title="Ver Visitas"><img src="../../images/maps/map_marker_seg.png" width="16" height="16"></label>    
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                               <input type="checkbox" id="chk_devoluciones" title="Ver Devoluciones" onclick="$('#devoluciones').click(); return;"><label for="check" title="Ver Devoluciones"><img src="../../images/maps/map_marker_devs.png" width="16" height="16"></label>    
                                            </td>
                                            <td>
                                                <input type="checkbox" id="chk_entregas" title="Ver Entregas" onclick="$('#entregas').click(); return;"><label for="check" title="Ver Entregas"><img src="../../images/maps/40px-verde-camion.png" width="16" height="16"></label>    
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                                <!-- FIN Caja de herramientas flotante sobre mapa Toolbox -->
                                <div id="velocimetro" class="over_map_bottom_left" style="width: 150px; height: 120px; display: none;"></div>
                             </div>
                        </div>
                    </div>
                    <!--</form>-->
                </div>
                <%if(dashboard == 0){%>
                <jsp:include page="../include/footer.jsp"/>
                <%}%>
            </div>
            <!-- Fin de Contenido-->
        </div>
            
            
        <div id="div_config_popup" title="Configuraciones Refrescar Mapa" 
             style="display:none; width: 500px; padding: 10px;"
             class="threecolumn_each">
            <p id="validateTips">Todos los campos son requeridos.</p>
            <br/>
            <fieldset>
              <label for="config_minutos_recarga">Refrescar cada (minutos):</label>
              <input type="text" name="config_minutos_recarga" id="config_minutos_recarga" value="<%= minutosAutoRecarga %>" class="text ui-widget-content ui-corner-all"
                      onkeypress="return validateNumberInteger(event);">
              <br/>
              <label for="config_velocidad_maxima">Velocidad máxima (Km/h):</label>
              <input type="text" name="config_velocidad_maxima" id="config_velocidad_maxima" value="<%= velocidadMaximaPermitida %>" class="text ui-widget-content ui-corner-all"
                      onkeypress="return validateNumberInteger(event);">
              <br/>
              <label for="config_segundos_seguimiento">Intervalo Seguimiento Empleado (segundos):</label>
              <input type="text" name="config_segundos_seguimiento" id="config_segundos_seguimiento" value="<%= segundosSeguimiento %>" class="text ui-widget-content ui-corner-all"
                      onkeypress="return validateNumberInteger(event);">
              <br/><br/>
              <p>
                  <button onclick="aplicarNuevaConfiguracion();">Aplicar</button>
                  &nbsp;&nbsp;&nbsp;&nbsp;
                  <button class="simplemodal-close">Cerrar</button>
              </p>
            </fieldset>
        </div>
            
    <%if(dashboard == 0){%>
    <script type="text/javascript">
        //Para recargar pagina completa cada N minutos                                                                                                                                                                                                                        
        var pagina = 'mapa_visor.jsp';        
        function redireccion() {
            document.location.href=pagina;
        }
        // setTimeout("redireccion()",300000);//cada 5 minutos
        //setTimeout("show3()",0); //iniciar reloj/cronometro

        function cargarSesion(){
            //var busqueda = document.getElementById("txt_buscar");                                
            $.ajax({
                type: "POST",
                url: "mapa_busqueda_sesion_ajax.jsp",
                data: $("#form_mapa_buscador").serialize(),
                beforeSend: function(objeto){
                    $("#action_buttons").fadeOut("slow");
                    $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                    $("#ajax_loading").fadeIn("slow");
                },
                success: function(datos){
                    if(datos.indexOf("--EXITO-->", 0)>0){                               
                                    //document.location.href=pagina; 
                                    $("#ajax_loading").fadeOut("slow");
                   }else{
                       $("#ajax_loading").fadeOut("slow");
                       if (datos.length>0){
                            $("#ajax_message").html(datos);
                            $("#ajax_message").fadeIn("slow");
                            $.scrollTo('#inner',800);
                        }
                       $("#action_buttons").fadeIn("slow");
                   }
                }
            });              
        }
            
        function cargarSesion2(va1,va2,va3,va4,mode){
             //alert(va1+","+va2+","+va3+","+va4+","+mode);
            //var busqueda = document.getElementById("txt_buscar");                                
            $.ajax({
                type: "POST",
                url: "mapa_busqueda_sesion_ajax.jsp",
                data: {lat : va1, lng : va2, tipo : va3, aux : va4, mode : mode},
                beforeSend: function(objeto){
                    $("#action_buttons").fadeOut("slow");
                    $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                    $("#ajax_loading").fadeIn("slow");
                },
                success: function(datos){
                    if(datos.indexOf("--EXITO-->", 0)>0){                               
                                    document.location.href=pagina; 
                   }else{
                       $("#ajax_loading").fadeOut("slow");
                       if (datos.length>0){
                            $("#ajax_message").html(datos);
                            $("#ajax_message").fadeIn("slow");
                            $.scrollTo('#inner',800);
                       }
                       $("#action_buttons").fadeIn("slow");
                   }
                }
            });              
        }
            
                                   
        buscarMarcador();
        <%if(cooredenadasPromotor!= null && cooredenadasPromotor.equals("si")){%>
            setTimeout("muestraMarcadorBusqueda('<%=lat%>','<%=lng%>','<%=tipo%>','<%=aux%>')", 2000);
        <%}%>
        <%if(promotorTipo != null && promotorTipo.equals("si")){%>
            setTimeout("muestraDetallesPromotor('<%=promId%>','<%=promTipo%>')",5000);
        <%}%>
        <%if(cooredenadasCliente != null && cooredenadasCliente.equals("si")){%>
            setTimeout("muestraMarcadorBusqueda('<%=latCl%>','<%=lngCl%>','<%=tipo%>','<%=aux%>')",2000);
        <%}%>  
        <%if(clienteDetalles != null && clienteDetalles.equals("si")){%>
            setTimeout("muestraDetallesCliente('<%=clieId%>','<%=clieTipo%>')",5000);
        <%}%>   
        <%if(cooredenadasPros != null && cooredenadasPros.equals("si")){%>
            setTimeout("muestraMarcadorBusqueda('<%=latPros%>','<%=lngPros%>','<%=tipo%>','<%=aux%>')",2000);
        <%}%>  
        <%if(prosDetalles != null && prosDetalles.equals("si")){%>
            setTimeout("muestraDetallesProspecto('<%=prosId%>','<%=prosTipo%>')",5000);
        <%}%>
    </script>
    <%}%>
    
    
    
    
    
    </body>
    
    <script type="text/javascript">
        mostrarCalendario_Rango();
        iniciaVelocimetro();
        <%if(dashboard == 1){%>
            function marcaCheckBox(){
                $('#clientes').click();
                $('#prospectos').click();
                $('#promotores').click();                
            }
            setTimeout("marcaCheckBox()",6000);
        <%
        }else{
        %>
          function marcaCheckBoxSesion(){                                                                                                                              
        <%

            if (chkClientes!=null && chkClientes.equals("1"))
                out.print("$('#clientes').click();");
            if (chkProspectos!=null && chkProspectos.equals("1"))
                out.print("$('#prospectos').click();");
            if (chkPromotores!=null && chkPromotores.equals("1"))
                out.print("$('#promotores').click();");
            if (chkVisitas!=null && chkVisitas.equals("1"))
                out.print("$('#visitas').click();");
            if (chkCobranzas!=null && chkCobranzas.equals("1"))
                out.print("$('#cobranzas').click();");
            if (chkDevoluciones!=null && chkDevoluciones.equals("1"))
                out.print("$('#devoluciones').click();");
            if (chkPedidos!=null && chkPedidos.equals("1"))
                out.print("$('#pedidos').click();");
            if (chkPuntos!=null && chkPuntos.equals("1"))
                out.print("$('#puntos').click();");
            if (chkVehiculos!=null && chkVehiculos.equals("1"))
                out.print("$('#vehiculos').click();");
            if (chkEntregas!=null && chkEntregas.equals("1"))
                out.print("$('#entregas').click();");
        %>   
            }                                                                                                                          
            setTimeout("marcaCheckBoxSesion()",2000);
        <% 
         }
        %>
    </script>
</html>
<%}%>