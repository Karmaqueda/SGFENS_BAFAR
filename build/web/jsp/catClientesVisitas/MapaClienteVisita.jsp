<%-- 
    Document   : MapaClienteVisita
    Created on : 30/10/2014, 03:50:05 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<!DOCTYPE html>

<% 
    /*
    * Recepción de valores
    */
    //long idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
    int idCliente = Integer.parseInt(request.getParameter("idCliente"));
    double latitud = 0;
    double longitud = 0;
    try{ latitud = Double.parseDouble(request.getParameter("latitud"));}catch(Exception e ){}
    try{ longitud = Double.parseDouble(request.getParameter("longitud"));}catch(Exception e ){}
    
    Cliente cliente = new Cliente();
    ClienteBO clienteBO = new ClienteBO(user.getConn());
    cliente = clienteBO.findClientebyId(idCliente);
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
        
        
        
        <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
	<script type="text/javascript">
            var latitudCliente = <%=latitud%>;
            var longitudCliente = <%=longitud%>
            var idClienteActualizar = <%=cliente.getIdCliente()%>
            
            var mapita;
            var contextmenuDir;
            
	  function initialize() {
	    var latlng = new google.maps.LatLng(latitudCliente,longitudCliente);
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
 
//	    var contentString = 
//	    '<h2 id="firstHeading" class="firstHeading">Alpina Motors</h2>'+
//	    '<div id="bodyContent">'+
//	    '<p>Dirección del lugar<br>'+
//	    '(01) 1234-5678<br>'+
//		'Palermo<br>'+
//		'Ciudad de Buenos Aires<br>'+
//		'Argentina</p>'+
//	    '</div>';

                var contentString =
                    '<h4 id="firstHeading" class="firstHeading"> <%=cliente.getRfcCliente()%></h4>'+
                    '<div id="bodyContent">'+
        	    '<p>Visita del Cliente<br>'+
        	    '<%=cliente.getNombreCliente().replace("'","")+" "+ cliente.getApellidoPaternoCliente().replace("'","")+" "+cliente.getApellidoMaternoCliente().replace("'","")%></p>'+                    
        	    '</div>';
 
		var infowindow = new google.maps.InfoWindow({
		    content: contentString
		});		
 
		var marker = new google.maps.Marker({
	      position: latlng, 
	      map: mapita, 
              icon : "../../images/maps/map_marker_cte.png",
	      title:"Lugar de Visita"
		});
 
		infowindow.open(mapita,marker);
 
		
                
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
          
                   
          var geocoder;
          var infowindow = new google.maps.InfoWindow();
          var marker;
          geocoder =  new google.maps.Geocoder();
            
            function codeLatLng(lat,lng) {
                
                  
                  var latlng = new google.maps.LatLng(lat, lng);
                  geocoder.geocode({'latLng': latlng}, function(results, status) {
                    if (status == google.maps.GeocoderStatus.OK) {
                      if (results[1]) {
                        /*mapita.setZoom(11);
                        marker = new google.maps.Marker({
                            position: latlng,
                            map: mapita
                        });
                        infowindow.setContent(results[1].formatted_address);
                        infowindow.open(mapita, marker);*/
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
        <!--<div id="map2" style="width:360px;height:200px;border:2px solid green;"></div>-->
        <div id="map_canvas" class="formDiv" style="width:547px;height:319px;border:2px solid green; margin: 0 auto;"></div>        
    </body>
</html>
