<%-- 
    Document   : Mapa_CrCredClienteLocalizacion
    Created on : 22/07/2016, 12:33:32 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.bo.CrCredClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.CrCredCliente"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<!DOCTYPE html>

<% 
    /*
    * Recepción de valores
    */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
    
    System.out.println(">>>" + mode);
    
    int idCrCredCliente = Integer.parseInt(request.getParameter("idCrCredCliente"));
    CrCredCliente crCredCliente = new CrCredCliente();
    CrCredClienteBO crCredClienteBO = new CrCredClienteBO(idCrCredCliente, user.getConn());
    crCredCliente = crCredClienteBO.getCrCredCliente();
        
    Empresa empresaDto = null;
    int idEmpresa = user.getUser().getIdEmpresa();
    if (idEmpresa > 0) {
        EmpresaBO empresaBo = new EmpresaBO(user.getConn());
        empresaDto = empresaBo.findEmpresabyId(idEmpresa);
    }

    String latitudCli = "0";
    String longitudCli = "0";

    try {
        if (empresaDto.getLatitud() != 0) {
            latitudCli = "" + empresaDto.getLatitud();
        }

    } catch (Exception e) {
        latitudCli = "0";
    }

    try {
        if (empresaDto.getLongitud() != 0) {
            longitudCli = "" + empresaDto.getLongitud();
        }
    } catch (Exception e) {
        longitudCli = "0";
    }

    if (crCredCliente != null) {

        try {
            if (crCredCliente.getLatitud()!=0){ 
                latitudCli = "" + crCredCliente.getLatitud();
            }
        } catch (Exception e) {
            latitudCli = "" + empresaDto.getLatitud();
        }

        try {
            if (crCredCliente.getLongitud()!=0) {
                longitudCli = "" + crCredCliente.getLongitud();
            }
        } catch (Exception e) {
            longitudCli = "" + empresaDto.getLongitud();
        }
    }
    
    Configuration appConfig = new Configuration();
    String mapApiKey = appConfig.getGoogleApiKeyMapJs();
    
%>

<html>
    <head>
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>        
        
        <style type="text/css">
            #map_canvas{
                width: 400px; 
                height: 300px;
            }
            .contextmenu{
                visibility:hidden;
                background:#ffffff;
                border:1px solid green;
                z-index: 10;  
                position: relative;
                width: 140px;
            }
            .contextmenu div{
                padding-left: 5px
                }
        </style>
        
        
        
        <script type="text/javascript" src="http://maps.google.com/maps/api/js?key=<%= mapApiKey %>"></script>
	<script type="text/javascript">
            var latitudCrCredCliente = <%=latitudCli%>;
            var longitudCrCredCliente = <%=longitudCli%>
            var idCrCredClienteActualizar = <%=crCredCliente.getIdCredCliente()%>
            
            var mapita;
            var contextmenuDir;
            
	  function initialize() {
	    var latlng = new google.maps.LatLng(latitudCrCredCliente,longitudCrCredCliente);
	    var myOptions = {
	      zoom: 15,
	      center: latlng,
        	mapTypeControl: true,
		mapTypeControlOptions: {style: google.maps.MapTypeControlStyle.DROPDOWN_MENU},
      		navigationControl: true,
      		navigationControlOptions: {style: google.maps.NavigationControlStyle.SMALL},
	        mapTypeId: google.maps.MapTypeId.ROADMAP
	    };
	    mapita = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
            
            
            mapita.controls[google.maps.ControlPosition.TOP_RIGHT].push(
                FullScreenControl(mapita, 'Pantalla Completa',
                'Salir Pantalla Completa'));  

                <%
                    String clientName  = "";
                    if(crCredCliente!=null){
                        
                        clientName += crCredClienteBO.getNombreCompleto(true);
                        
                    }
                    
                
                %>

                var contentString =
                    '<h2 id="firstHeading" class="firstHeading"></br><%=crCredCliente.getRfc()%></h2>'+
                    '<div id="bodyContent">'+
        	    '<p><b>Cliente:</b><br>'+
        	    '<%=clientName%></p>'+                        
        	    '</div>';
 
		var infowindow = new google.maps.InfoWindow({
		    content: contentString
		});
 
		var marker = new google.maps.Marker({
                  position: latlng, 
                  map: mapita, 
                  icon : "../../images/maps/map_marker_cte.png",
                  title:"Cliente Crédito"
		});
 
		infowindow.open(mapita,marker);
    
                google.maps.event.addListener(marker, 'click', function() {
		  infowindow.open(mapita,marker);
		});
		
               
               //VAMOS A RECUPERAR LAS COORDENADAS /////////////////////
               //Creo un evento asociado a "mapa" cuando se hace "click" sobre el
                google.maps.event.addListener(mapita, "rightclick", function(evento) {
                    
                    showContextMenu(evento.latLng,evento.latLng.lat(),evento.latLng.lng());    
                       
                
                }); //Fin del evento
                /////////////////////
                
                 
                google.maps.event.addListener(mapita, "click", function(evento) {
                    contextmenuDir.style.visibility = "hidden";
                }); //Fin del evento
                
	  }

          function actualizarCoordenadasCrCredCliente(latitudCrCredCliente,longitudCrCredCliente,idCrCredCliente){
                if (idCrCredCliente>0){
                    
                    $.ajax({
                        type: "POST",
                        url: "../include/Mapa_ajax.jsp",
                        data: {mode : "4", latitudActualizada : latitudCrCredCliente, longitudActualizada : longitudCrCredCliente, idActualizar : idCrCredCliente},
                        beforeSend: function(objeto){                            
                            //$("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               //$("#ajax_message").html(datos);
                               $("#ajax_loading").fadeOut("slow");
                               //$("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                        function(r){
                                            location.href = "catCrCredCliente_formMapa.jsp?acc=<%= mode %>&idCrCredCliente="+idCrCredCliente+""
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
                    mapita.setOptions(mapOptions);
                    mapita.fitBounds(results[0].geometry.viewport);
                   
                                       
                } else {
                    alert("Geocoding no tuvo éxito debido a: " + status);
                }
            }
           
           /*------------context menu----------------*/
           
           function showContextMenu(caurrentLatLng, lat , lng ) {
                var projection;
                
                projection = mapita.getProjection() ;
                $('.contextmenu').remove();
                    contextmenuDir = document.createElement("div");
                  contextmenuDir.className  = 'contextmenu';
                  contextmenuDir.innerHTML = "<a id='menu1' onclick='codeLatLng(" + lat + " , " + lng + ");'><div class=context><div class=context><img src='../../images/icon_movimiento.png'/> Obtener Dirección<\/div><\/a>\n\
                        <a id='menu2' onclick='asignarUbicacion(" + lat + " , " + lng + ");'><div class=context><img src='../../images/maps/map_marker_otro_small.png'/> Asignar Ubicación<\/div><\/a>";
                $(mapita.getDiv()).append(contextmenuDir);

                setMenuXY(caurrentLatLng);

                contextmenuDir.style.visibility = "visible";
           }
           
           
           function setMenuXY(caurrentLatLng){
                var mapWidth = $('#map_canvas').width();
                var mapHeight = $('#map_canvas').height();
                var menuWidth = $('.contextmenu').width();
                var menuHeight = $('.contextmenu').height();
                var clickedPosition = getCanvasXY(caurrentLatLng);
                var x = clickedPosition.x ;
                var y = clickedPosition.y ;

                 if((mapWidth - x ) < menuWidth)
                     x = x - menuWidth;
                if((mapHeight - y ) < menuHeight)
                    y = y - menuHeight;

                $('.contextmenu').css('left',x  );
                $('.contextmenu').css('top',y );
            };
           
           function getCanvasXY(caurrentLatLng){
              var scale = Math.pow(2, mapita.getZoom());
             var nw = new google.maps.LatLng(
                 mapita.getBounds().getNorthEast().lat(),
                 mapita.getBounds().getSouthWest().lng()
             );
             var worldCoordinateNW = mapita.getProjection().fromLatLngToPoint(nw);
             var worldCoordinate = mapita.getProjection().fromLatLngToPoint(caurrentLatLng);
             var caurrentLatLngOffset = new google.maps.Point(
                 Math.floor((worldCoordinate.x - worldCoordinateNW.x) * scale),
                 Math.floor((worldCoordinate.y - worldCoordinateNW.y) * scale)
             );
             return caurrentLatLngOffset;
          }
          
          
          
          function asignarUbicacion(lat,lng){
                //ACTUALIZAMOS LAS COORDENADAS
                actualizarCoordenadasCrCredCliente(lat,lng,idCrCredClienteActualizar);
          }   
          
          
          var geocoder;
          var infowindow = new google.maps.InfoWindow();
          var marker;
          geocoder =  new google.maps.Geocoder();
            
            function codeLatLng(lat,lng) {
                
                  
                  var latlng = new google.maps.LatLng(lat, lng);
                  geocoder.geocode({'latLng': latlng}, function(results, status) {
                    if (status == google.maps.GeocoderStatus.OK) {
                      if (results[1]) {
                        $('#direccion').val(results[1].formatted_address);
                            
                
                      } else {
                        alert('No se encontraron resultados');
                      }
                    } else {
                      alert('Geocoding no tuvo éxito debido a: ": ' + status);
                    }
                  });
        }
          
          
          
            
	</script>
        
    </head>
    <body onload="initialize()">
        <!--<h1>Localización!</h1>        -->
        
        <img src="../../images/maps/tool_mostrar_ocultar.png" id="btn_map_buscar" value="Buscar" title="Mostrar/Ocultar buscador de direcciones" onclick="mostrarOcultarDirecciones();"/>
        
        <div id="div_map_tool_buscar" style="display: none;">
            <input type="text" id="txt_direccion" name="txt_direccion" title="Ingresa la dirección a encontrar" style="width:200px"/>
            <img src="../../images/maps/tool_mostrar_ocultar.png" title="Buscar dirección" onclick="buscarDireccion();" value="Buscar"/>
            <br>             

        </div>
        
        <!--<div id="map2" style="width:360px;height:200px;border:2px solid green;"></div>-->
        <div id="map_canvas" class="formDiv" style="width:400px;height:400px;border:2px solid green; margin: 0 auto;"></div>
        
    </body>
</html>
