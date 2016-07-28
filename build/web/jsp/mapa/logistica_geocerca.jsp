<%-- 
    Document   : logistica_geocerca
    Created on : 20/03/2013, 02:03:01 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.dao.jdbc.EmpleadoBitacoraPosicionDaoImpl"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.factory.EstadoEmpleadoDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.EstadoEmpleado"%>
<%@page import="com.tsp.sct.dao.factory.TipoPuntoInteresDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.TipoPuntoInteres"%>
<%@page import="com.tsp.sct.dao.factory.PuntosInteresDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.PuntosInteres"%>
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
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String filtroBusqueda = "";
    if (!buscar.trim().equals(""))
        filtroBusqueda = " AND (NOMBRE_COMERCIAL LIKE '%" + buscar + "%' )";
         
    //int idEmpresa = -1;
    //try{ idEmpresa = Integer.parseInt(request.getParameter("idEmpresa")); }catch(NumberFormatException e){}
    
    int idGeocerca = -1;
    try{ idGeocerca = Integer.parseInt(request.getParameter("idGeocerca")); }catch(NumberFormatException e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    Cliente[] clientesDto = null;
    SgfensProspecto[] prospectosDto = null;
    Empresa empresa = new Empresa();   
    /*
    Empleado[] promotoresDto = null;
    Automovil[] automovilesDto = null;
    */
    if(idEmpresa > 0){
                 
        EmpresaBO empresaBo = new EmpresaBO(user.getConn());    
        empresa = empresaBo.findEmpresabyId(idEmpresa); 
            
        try{
            clientesDto = ClienteDaoFactory.create().findWhereIdEmpresaEquals(idEmpresa);
        }catch(Exception e){}
        try{
            prospectosDto = SgfensProspectoDaoFactory.create().findWhereIdEmpresaEquals(idEmpresa);
        }catch(Exception e){}
    }
       
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
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
        
        <style type="text/css">
            .map_dialog input{
                width: 100%;
            }
        </style>
        
        <script type="text/javascript">
            
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
            var vehiculos = [];
            var puntos = [];
            var puntosPolygon = [];
            
            var global = [];
            var markerRoute2 = [];
            var markerRoute;
            
            function initialize() {
                
                var rendererOptions = {
                    draggable: false
                };
                
                directionsService = new google.maps.DirectionsService();
                directionsDisplay = new google.maps.DirectionsRenderer(rendererOptions);
                
                //var inicio = new google.maps.LatLng(21.123619,-101.680496);//leon
                //var inicio = new google.maps.LatLng(23.634501,-102.552784);//mexico
                var inicio = new google.maps.LatLng(<%=empresa.getLatitud()%>,<%=empresa.getLongitud()%>);
                                
                var mapOptions = {
                  zoom: 15,
                  center: inicio,
                  mapTypeId: google.maps.MapTypeId.ROADMAP
                };
                
               

                map = new google.maps.Map(document.getElementById('map_canvas'),
                    mapOptions);
                    
                map.controls[google.maps.ControlPosition.TOP_RIGHT].push(
                FullScreenControl(map, 'Pantalla Completa',
                'Salir Pantalla Completa'));    
                    
                
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
                            //alert("Radio: "+formaDibujada.getBounds());
                            //alert("Radio centro: "+formaDibujada.getCenter());
                            //alert("radio INFO CONTENIDA: "+formaDibujada.getRadius().toFixed(2));
                        });
                        google.maps.event.addListener(formaDibujada, 'center_changed', function() {
                            $('#txt_map_radius').val(formaDibujada.getRadius().toFixed(2));
                            verificaMarcadores(formaDibujada.getBounds());
                            //alert("Center: "+formaDibujada.getBounds());
                            //alert("Radio centro: "+formaDibujada.getCenter());
                            //alert("center INFO CONTENIDA: "+formaDibujada.getRadius().toFixed(2));
                            
                        });
                        $('#txt_map_radius').val(formaDibujada.getRadius().toFixed(2));
                        verificaMarcadores(formaDibujada.getBounds());
                        //alert("FINAL: "+formaDibujada.getBounds());
                        //alert("final INFO CONTENIDA: "+formaDibujada.getRadius().toFixed(2));
                    }else if(event.type == google.maps.drawing.OverlayType.RECTANGLE) {
                        google.maps.event.addListener(formaDibujada, 'bounds_changed', function() {
                            verificaMarcadores(formaDibujada.getBounds());
                            //alert("rectangulo rectangulo: "+formaDibujada.getBounds());
                        });
                        verificaMarcadores(formaDibujada.getBounds());
                    }else if(event.type == google.maps.drawing.OverlayType.POLYGON) {                         
                        //verificaMarcadores(polygonBounds(formaDibujada));
                       //alert("poligono: " + polygonBounds(formaDibujada));
                       
                    }
                  });
                
                fillMap();
                
            }
            
            function polygonBounds(polygon){
                var path = polygon.getPath();
                var pathSize = path.getLength();
                
                var bounds = new google.maps.LatLngBounds();
                
                for (var i = 0; i < pathSize; i++) {
                                     
                    var unit = new google.maps.LatLng(path.getAt(i).lat(),path.getAt(i).lng());
                    //alert(unit);
                    puntosPolygon.push(unit);            
                    //bounds.extend(unit);                    
                }
                //return bounds;
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
        
            function fillMap(){
                <%
                for(Cliente clienteDto:clientesDto){
                    String nombreCliente = clienteDto.getRazonSocial();
                    String dialogoCliente = ""
                         + "<div class='map_dialog'>"
                         + "   <b>Cliente:</b></br>" + nombreCliente.replaceAll("\\\"", "&quot;") + "<br/>"
                         + "   <li> <a title='Detalles' onclick='muestraDetallesCliente(" + clienteDto.getIdCliente() + ",1)'/>Detalles</li> <br/>"
                         + "   <li> <a title='Pagos' onclick='muestraDetallesCliente(" + clienteDto.getIdCliente() + ",2)'/>Pagos</li> <br/>"
                         + " </div>";
                    %>
                     clientes.push(
                         creaMarcador(
                             <%=clienteDto.getLatitud()!=null?(!clienteDto.getLatitud().equals("")?clienteDto.getLatitud():"0"):"0" %>,
                             <%=clienteDto.getLongitud()!=null?(!clienteDto.getLongitud().equals("")?clienteDto.getLongitud():"0"):"0" %>,
                             "<%=nombreCliente.replaceAll("\\\"", "&quot;") %>",
                             "../../images/maps/map_marker_cte.png",
                             "<%=dialogoCliente %>"
                         )
                     );
                    <%
                }
                %>
                <%
                for(SgfensProspecto prospectoDto:prospectosDto){
                    String nombreCliente = prospectoDto.getContacto();
                    String dialogoCliente = ""
                        + "<div class='map_dialog'>"
                        + "    <b>Prospecto:</b></br>" + nombreCliente.replaceAll("\\\"", "&quot;") + "<br/>"
                        + "    <li><a title='Detalles' onclick='muestraDetallesProspecto(" + prospectoDto.getIdProspecto() + ",1)'/>Detalles</li><br/>"
                        + "</div>";
                    %>
                    prospectos.push(
                        creaMarcador(
                            <%=prospectoDto.getLatitud() %>,
                            <%=prospectoDto.getLongitud() %>,
                            "<%=nombreCliente.replaceAll("\\\"", "&quot;") %>",
                            "../../images/maps/map_marker_pros.png",
                            "<%=dialogoCliente %>"
                        )
                    );         
                    <%
                }
                %>
                <%
                Empleado[] empleadosDto = EmpleadoDaoFactory.create().findWhereIdEmpresaEquals(idEmpresa);
                for(Empleado empleadoDto:empleadosDto){
                    EstadoEmpleado estadoDto = EstadoEmpleadoDaoFactory.create().findByPrimaryKey(empleadoDto.getIdEstado());
                    String nombreMarcador = empleadoDto.getApellidoPaterno() + (empleadoDto.getApellidoMaterno()!=null?" " + empleadoDto.getApellidoMaterno():"") + " " + empleadoDto.getNombre();
                    String dialogoMarcador = ""
                        + "<div class='map_dialog'>"
                        + "    <b>Vendedor:</b></br>" + nombreMarcador.replaceAll("\\\"", "&quot;") + "<br/>"
                        + "    <b>Estado:</b> " + (estadoDto!=null?estadoDto.getNombre():"") + "<br/>"
                        + "    <li><a title='Detalles' onclick='muestraDetallesPromotor("+empleadoDto.getIdEmpleado()+",1);' />Detalles</li><br/>"
                        + "    <li><a title='Hist&oacute;rico' onclick='muestraDetallesPromotor("+empleadoDto.getIdEmpleado()+",2);' />Hist&oacute;rico</li><br/>"
                        + "    <li><a title='Cobros' onclick='muestraDetallesPromotor("+empleadoDto.getIdEmpleado()+",3);' />Cobros</li><br/>"
                        + "    <li><a title='Mensaje' onclick='muestraDetallesPromotor("+empleadoDto.getIdEmpleado()+",4);' />Mensaje</li><br/>"
                        + "    <li><a title='Rutas' onclick='muestraDetallesPromotor("+empleadoDto.getIdEmpleado()+",5);' />Rutas</li><br/>"
                        + "    <li> <a title='Pedidos' onclick='muestraDetallesPromotor("+empleadoDto.getIdEmpleado()+",6);'>Pedidos </a> </li><br/>"
                        + "</div>";
                    %>
                    promotores.push(
                        creaMarcador(
                            <%=empleadoDto.getLatitud() %>,
                            <%=empleadoDto.getLongitud() %>,
                            "<%=nombreMarcador.replaceAll("\\\"", "&quot;") %>",
                            
                            <%if(empleadoDto.getRepartidor() == 1){// si no es repartidor
                            
                            
                                    long s = (new Date()).getTime();
                                    long d = 0; 
                                    try{
                                        EmpleadoBitacoraPosicionDaoImpl bitacoraDao = new EmpleadoBitacoraPosicionDaoImpl();
                                        String filtro = " ID_EMPLEADO = "+ empleadoDto.getIdEmpleado() + " ORDER BY ID_BITACORA_POSICION DESC LIMIT 0,1";
                                        EmpleadoBitacoraPosicion bitEmp = bitacoraDao.findByDynamicWhere(filtro,null)[0];

                                        d = bitEmp.getFecha().getTime();
                                        //d = empleadoDto.getFechaHora().getTime();
                                    }catch(Exception e){}
                                    long diferencia = s - d;
                                    System.out.println("-------DIFERENCIA: "+diferencia);
                                    if(diferencia < 300000){
                                    %>
                                        "../../images/estatusEmpleado/icon_activoTrabajando.png",
                                    <%}else{%>
                                        "../../images/estatusEmpleado/icon_desactivado.png",
                                    <%}
                                
                                }else{//si es repartidor
                                    long s = (new Date()).getTime();
                                    long d = 0; 
                                    try{
                                        EmpleadoBitacoraPosicionDaoImpl bitacoraDao = new EmpleadoBitacoraPosicionDaoImpl();
                                        String filtro = " ID_EMPLEADO = "+ empleadoDto.getIdEmpleado() + " ORDER BY ID_BITACORA_POSICION DESC LIMIT 0,1";
                                        EmpleadoBitacoraPosicion bitEmp = bitacoraDao.findByDynamicWhere(filtro,null)[0];

                                        d = bitEmp.getFecha().getTime();
                                        //d = empleadoDto.getFechaHora().getTime();
                                    }catch(Exception e){}
                                    long diferencia = s - d;
                                    System.out.println("-------DIFERENCIA: "+diferencia);
                                    if(diferencia < 300000){
                                    %>
                                        "../../images/maps/40px-verde-camion.png",
                                    <%}else{%>
                                        "../../images/maps/40px-gris-camion.png",
                                        <%}%>                                                                 
                                    <%
                                }
                                                        
                            %>
                                
                            "<%=dialogoMarcador %>"
                        )
                    );
                    <%
                }
                %>
                <%
                /*for(Empleado empleadoDto:empleadosDto){
                    //EmpleadoAutomovil[] empleadoAutomovilDto = EmpleadoAutomovilDaoFactory.create().findWhereIdEmpleadoEquals(empleadoDto.getIdEmpleado());
                    //if(empleadoAutomovilDto.length > 0){
                        //Automovil automovilDto = AutomovilDaoFactory.create().findByPrimaryKey(empleadoAutomovilDto[0].getIdAutomovil());
                        Automovil automovilDto = AutomovilDaoFactory.create().findByPrimaryKey(empleadoDto.getIdVehiculo());
                        if(automovilDto!=null){
                            String nombreMarcador = empleadoDto.getApellidoPaterno() + (empleadoDto.getApellidoMaterno()!=null?" " + empleadoDto.getApellidoMaterno():"") + " " + empleadoDto.getNombre();
                            String dialogoMarcador = ""
                                + "<div class='map_dialog'>"
                                + "    Vendedor:<br/>" + nombreMarcador.replaceAll("\\\"", "&quot;") + "<br/><br/>"
                                + "    C&oacute;digo:<br/>" + automovilDto.getCodigo() + "<br/><br/>"
                                + "    Placas:<br/>" + automovilDto.getPlacas() + "<br/><br/>"
                                + "    Marca:<br/>" + automovilDto.getMarca() + "<br/><br/>"
                                + "    Modelo:<br/>" + automovilDto.getModelo() + "<br/><br/>"
                                + "</div>";*/
                            %>
                           /* vehiculos.push(
                                creaMarcador(
                                    </%=empleadoDto.getLatitud() %>,
                                    </%=empleadoDto.getLongitud() %>,
                                    "</%=nombreMarcador.replaceAll("\\\"", "&quot;") %>",
                                    "../../images/maps/map_marker_veh.png",
                                    "</%=dialogoMarcador %>"
                                )
                            );*/
                            <%
                        /*}
                    //}
                }*/
                %>
                <%
                PuntosInteres[] puntosInteresDto = PuntosInteresDaoFactory.create().findByDynamicWhere("ID_EMPRESA = ?", new String[]{"" + idEmpresa});
                if(puntosInteresDto.length > 0){
                    for(PuntosInteres puntoInteres:puntosInteresDto){
                        String dialogoMarcador = ""
                            + "<div class='map_dialog'>"
                            + "    Nombre:<br/>" + puntoInteres.getNombre().replaceAll("\\\"", "&quot;") + "<br/><br/>"
                            + "    Descripción:<br/>" + (puntoInteres.getDescripcion()!=null?puntoInteres.getDescripcion().replaceAll("\\\"", "&quot;"):"") + "<br/><br/>"
                            + "</div>";   
                        TipoPuntoInteres tipoPuntoInteresDto = null;
                        try{
                            tipoPuntoInteresDto = TipoPuntoInteresDaoFactory.create().findByPrimaryKey(puntoInteres.getIdTipoPunto());
                        }catch(Exception e){}
                    %>
                    puntos.push(
                        creaMarcador(
                            <%=puntoInteres.getLatitud() %>,
                            <%=puntoInteres.getLongitud() %>,
                            "<%=puntoInteres.getNombre().replaceAll("\\\"", "'") %>",
                            "../../images/maps/map_marker_<%=tipoPuntoInteresDto.getSufijoImgTipoPunto() %>.png",
                            "<%=dialogoMarcador %>"
                        )
                    );
                    <%
                    }
                }
                %>
            }
        
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
                for(var i = 0, marcador; marcador = vehiculos[i]; i ++){
                    marcador.setMap(null);
                }
                for(var i = 0, marcador; marcador = puntos[i]; i ++){
                    marcador.setMap(null);
                }
            }

            function loadScript() {
                var script = document.createElement('script');
                script.type = 'text/javascript';
                script.src = 'https://maps.googleapis.com/maps/api/js?libraries=geometry,drawing&sensor=false&' +
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
                /*var infowindow = new google.maps.InfoWindow({
                    content: content
                });
                google.maps.event.addListener(marcador, 'click', function() {
                    infowindow.open(map,marcador);
                });*/
                
                
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
            
            function muestraMarcador(chk){
                switch(chk.id){
                    case "clientes":
                        if(chk.checked){
                            for(var i = 0, marcador; marcador = clientes[i]; i ++){
                                marcador.setMap(map);
                            }
                        }else{
                            for(var i = 0, marcador; marcador = clientes[i]; i ++){
                                marcador.setMap(null);
                            }
                        }
                        break;
                    case "prospectos":
                        if(chk.checked){
                            
                            for(var i = 0, marcador; marcador = prospectos[i]; i ++){
                                marcador.setMap(map);
                            }
                        }else{
                            for(var i = 0, marcador; marcador = prospectos[i]; i ++){
                                marcador.setMap(null);
                            }
                        }
                        break;
                    case "promotores":
                        if(chk.checked){
                            
                            for(var i = 0, marcador; marcador = promotores[i]; i ++){
                                marcador.setMap(map);
                            }
                        }else{
                            for(var i = 0, marcador; marcador = promotores[i]; i ++){
                                marcador.setMap(null);
                            }
                        }
                        break;
                    case "vehiculos":
                        if(chk.checked){
                            
                            for(var i = 0, marcador; marcador = vehiculos[i]; i ++){
                                marcador.setMap(map);
                            }
                        }else{
                            for(var i = 0, marcador; marcador = vehiculos[i]; i ++){
                                marcador.setMap(null);
                            }
                        }
                        break;
                    case "puntos":
                        if(chk.checked){
                            
                            for(var i = 0, marcador; marcador = puntos[i]; i ++){
                                marcador.setMap(map);
                            }
                        }else{
                            for(var i = 0, marcador; marcador = puntos[i]; i ++){
                                marcador.setMap(null);
                            }
                        }
                        break;
                }             
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
                switch(tipo){
                    case "clientes":
                        for(var i = 0, marcador; marcador = clientes[i]; i ++){
                            var posicion = marcador.getPosition();
                            var posicion2 = new google.maps.LatLng(lat,lng);
                            if(posicion.lat()==posicion2.lat() && posicion.lng()==posicion2.lng()){
                                if(marcador.getMap()==null){
                                    var mapOptions = {
                                            center: posicion,
                                            zoom: 15,
                                            mapTypeId: google.maps.MapTypeId.ROADMAP
                                    };
                                    map.setOptions(mapOptions);
                                    marcador.setMap(map);
                                }else{
                                    marcador.setMap(null);
                                }
                            }
                        }
                        break;
                    case "prospectos":
                        for(var i = 0, marcador; marcador = prospectos[i]; i ++){
                            var posicion = marcador.getPosition();
                            var posicion2 = new google.maps.LatLng(lat,lng);
                            if(posicion.lat()==posicion2.lat() && posicion.lng()==posicion2.lng()){
                                if(marcador.getMap()==null){
                                    var mapOptions = {
                                            center: posicion,
                                            zoom: 15,
                                            mapTypeId: google.maps.MapTypeId.ROADMAP
                                    };
                                    map.setOptions(mapOptions);
                                    marcador.setMap(map);
                                }else{
                                    marcador.setMap(null);
                                }
                            }
                        }
                        break;
                    case "promotores":
                        for(var i = 0, marcador; marcador = promotores[i]; i ++){
                            var posicion = marcador.getPosition();
                            var posicion2 = new google.maps.LatLng(lat,lng);
                            if(posicion.lat()==posicion2.lat() && posicion.lng()==posicion2.lng()){
                                if(marcador.getMap()==null){
                                    var mapOptions = {
                                            center: posicion,
                                            zoom: 15,
                                            mapTypeId: google.maps.MapTypeId.ROADMAP
                                    };
                                    map.setOptions(mapOptions);
                                    marcador.setMap(map);
                                }else{
                                    marcador.setMap(null);
                                }
                            }
                        }
                        break;
                    case "vehiculos":
                        for(var i = 0, marcador; marcador = vehiculos[i]; i ++){
                            var posicion = marcador.getPosition();
                            var posicion2 = new google.maps.LatLng(lat,lng);
                            if(posicion.lat()==posicion2.lat() && posicion.lng()==posicion2.lng()){
                                if(marcador.getMap()==null){
                                    var mapOptions = {
                                            center: posicion,
                                            zoom: 15,
                                            mapTypeId: google.maps.MapTypeId.ROADMAP
                                    };
                                    map.setOptions(mapOptions);
                                    marcador.setMap(map);
                                }else{
                                    marcador.setMap(null);
                                }
                            }
                        }
                        break;
                    case "puntos":
                        for(var i = 0, marcador; marcador = puntos[i]; i ++){
                            var posicion = marcador.getPosition();
                            var posicion2 = new google.maps.LatLng(lat,lng);
                            if(posicion.lat()==posicion2.lat() && posicion.lng()==posicion2.lng()){
                                if(marcador.getMap()==null){
                                    var mapOptions = {
                                            center: posicion,
                                            zoom: 15,
                                            mapTypeId: google.maps.MapTypeId.ROADMAP
                                    };
                                    map.setOptions(mapOptions);
                                    marcador.setMap(map);
                                }else{
                                    marcador.setMap(null);
                                }
                            }
                        }
                        break;
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
                            + "<label>Enviar mensaje a promotor:</label><br/>"
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
        
            function muestraVentanaDetalles(url, id){
                $.ajax({
                    type: "GET",
                    url: url,
                    //data: $("#form_mapa_buscador").serialize(),
                    data: {id:id},
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
                       // alert("entro a if: "+marcador.getPosition());
                        marcador.setMap(map);
                    }else{
                       // alert("entro a else: "+marcador.getPosition());
                        marcador.setMap(null);
                    }
                }
                
                for(var i = 0, marcador; marcador = vehiculos[i]; i ++){
                    if(bounds.contains(marcador.getPosition())){
                        marcador.setMap(map);
                    }else{
                        marcador.setMap(null);
                    }
                }
                
                for(var i = 0, marcador; marcador = puntos[i]; i ++){
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
            
            function guardarCirculo(){
                //alert("REGISTRO EN FUNCTION, radio: "+formaDibujada.getRadius().toFixed(2) +", Centro: "+formaDibujada.getCenter());
                var radio = formaDibujada.getRadius().toFixed(2);
                var centro = formaDibujada.getCenter();
                    $.ajax({
                        type: "POST",
                        url: "../../jsp/catGeocerca/catGeocercas_ajax.jsp",
                        data: {mode: 'circulo', radio : radio, centro : centro, idGeocerca:<%=idGeocerca%>},
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
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                        function(r){
                                            location.href = "../../jsp/catGeocerca/catGeocercas_list.jsp";
                                        });
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
            
            function guardarCuadrado(){
                //alert("REGISTRO EN FUNCTION, radio: "+formaDibujada.getRadius().toFixed(2) +", Centro: "+formaDibujada.getCenter());                                                
                var coordenadasCuadrado = formaDibujada.getBounds();
                    $.ajax({
                        type: "POST",
                        url: "../../jsp/catGeocerca/catGeocercas_ajax.jsp",
                        data: {mode: 'cuadrado', coordenadasCuadrado : coordenadasCuadrado, idGeocerca:<%=idGeocerca%> },
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
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                        function(r){
                                            location.href = "../../jsp/catGeocerca/catGeocercas_list.jsp";
                                        });
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
            
            
            
            function guardarPoligono(){
                
                    polygonBounds(formaDibujada);//Agrego puntos a arreglo                     
                    $.ajax({
                        type: "POST",
                        url: "../../jsp/catGeocerca/catGeocercas_ajax.jsp",
                        data: {mode: 'poligono',coordenadasPoligono: puntosPolygon, idGeocerca:<%=idGeocerca%>},
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
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                        function(r){
                                            location.href = "../../jsp/catGeocerca/catGeocercas_list.jsp";
                                        });
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
            
            $(document).ready(function() {
                //Si se recibio el parametro para que el modo sea en forma de popup
                <%= mode.equals("consulta")? "mostrarFormPopUpMode();":""%>
            });
            
            function mostrarFormPopUpMode(){
		$('#left_menu').hide();
                $('#header').hide();
		//$('#show_menu').show();
		$('body').addClass('nobg');
		$('#content').css('marginLeft', 30);
		$('#wysiwyg').css('width', '97%');
		setNotifications();
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
                    <% if (!mode.equals("consulta")) {%>
                    <h1>Mapas</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--<form id="form_data" name="form_data" action="" method="post">-->                    
                        <div class="twocolumn">
                            <div class="column_left">                    
                                <div class="header">
                                    <span>
                                        <img src="../../images/icon_mapa_1.png" alt="icon"/>
                                        Selector de capas
                                    </span>
                                    <div class="switch" style="width:410px">

                                    </div>
                                </div>
                                <br class="clear"/>
                                <div class="content">
                                    <p>
                                        <input id="clientes" type="checkbox" onclick="muestraMarcador(this);"/>Clientes<br/>
                                        <input id="prospectos" type="checkbox" onclick="muestraMarcador(this);"/>Prospectos<br/>
                                        <input id="promotores" type="checkbox" onclick="muestraMarcador(this);"/>Vendedores<br/>
                                        <!--<input id="vehiculos" type="checkbox" onclick="muestraMarcador(this);"/>Veh&iacute;culos<br/>-->
                                        <!--<input id="puntos" type="checkbox" onclick="muestraMarcador(this);"/>Puntos de inter&eacute;s-->
                                        <!--<input type="button" onclick="calcRoute();" value="Ruta"/>-->
                                    </p>
                                    <!--<p>
                                        <form id="form_mapa_buscador" name="form_mapa_buscador" action="" method="post">
                                            <label>Buscar:</label><br/>
                                            <input maxlength="30" type="text" id="txt_buscar" name="txt_buscar" style="width:300px" value=""/>
                                            <select id="cmb_tipo_buscar" name="cmb_tipo_buscar">
                                                <option value="todos" selected>Todos</option>
                                                <option value="clientes">Clientes</option>
                                                <option value="prospectos">Prospectos</option>
                                                <option value="promotores">Vendedores</option>
                                                <option value="vehiculos">Veh&iacute;culos</option>
                                                <option value="puntos">Puntos de inter&eacute;s</option>
                                            </select>
                                            <!--<input type="image" src="../../images/icon_consultar.png" title="Buscar" onclick="buscarMarcador();"/>-->
                                    <!--        <input type="button" value="Buscar" onclick="buscarMarcador();"/>
                                        </form>
                                    </p>-->
                                    <p>
                                        <br/>
                                        <div id="div_map_resultado_buscar" style="height: 210px;overflow: scroll;"></div>
                                    </p>
                                </div>
                            </div>                 
                            
                            <div class="column_right">                                
                    <%}else{%>
                            <div class="twocolumn">
                            <div class="column_left">
                    <%}%>                               
                                <div class="header">
                                    <span>
                                        <img src="../../images/icon_mapa.png" alt="icon"/>
                                        Visor
                                    </span>
                                    <div class="switch" style="width:410px">

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
                                        <input title="Guardar" type="submit" id="cuadrado" onclick="guardarCirculo();" class="right_switch" style="font-size: 9px;"/>
                                    </div>
                                    <div id="div_map_tool_rectangulo" style="display: none;">
                                        Herramienta Seleccionar puntos por rect&aacute;ngulo:<br/>
                                        Hacer click sobre el mapa para comenzar a trazar.
                                        <input title="Guardar" type="submit" id="circulo" onclick="guardarCuadrado();" class="right_switch" style="font-size: 9px;"/>
                                    </div>
                                    <div id="div_map_tool_poligono" style="display: none;">
                                        Herramienta Seleccionar puntos por pol&iacute;gono:<br/>
                                        Hacer click sobre el mapa para comenzar a trazar.
                                         <input title="Guardar" type="submit" id="circulo" onclick="guardarPoligono();" class="right_switch" style="font-size: 9px;"/>
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
                                    <div id="map_canvas" style="height: 400px;width: auto">
                                        <img src="../../images/maps/ajax-loader.gif" alt="Cargando" style="margin: auto;"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    <!--</form>-->
                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>


    </body>
</html>
<%}%>