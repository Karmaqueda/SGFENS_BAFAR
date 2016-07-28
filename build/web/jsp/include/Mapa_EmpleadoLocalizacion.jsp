<%-- 
    Document   : Mapa
    Created on : 10/01/2013, 11:35:33 AM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.dao.dto.EmpleadoBitacoraPosicion"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoBitacoraPosicionDaoImpl"%>
<%@page import="com.tsp.sct.dao.factory.EstadoEmpleadoDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.EstadoEmpleado"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.dao.dto.Ubicacion"%>
<%@page import="com.tsp.sct.bo.UbicacionBO"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.dao.dto.Usuarios"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<!DOCTYPE html>

<% 
    /*
    * Recepción de valores
    */
    //long idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
    int idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
    Empleado empleado = new Empleado();
    EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
    empleado = empleadoBO.findEmpleadobyId(idEmpleado);
    Empresa empresa = new Empresa();
    EmpresaBO ebo = new EmpresaBO(empleado.getIdEmpleado(),user.getConn());
    empresa = ebo.getEmpresa();
    
    double lat = 0;
    double lan = 0;
    try{
        EmpleadoBitacoraPosicionDaoImpl bitacoraDao = new EmpleadoBitacoraPosicionDaoImpl();
        String filtro = " ID_EMPLEADO = "+ empleado.getIdEmpleado() + " ORDER BY FECHA DESC LIMIT 0,1";
        EmpleadoBitacoraPosicion bitEmp = bitacoraDao.findByDynamicWhere(filtro,null)[0];
        
        lat = bitEmp.getLatitud();
        lan =  bitEmp.getLongitud();
    }catch(Exception e){
        lat = empleado.getLatitud();
        lan =  empleado.getLongitud();
    }
    
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <!--<script src="http://maps.google.com/maps/api/js?sensor=true" type="text/javascript"></script>       
        <script type="text/javascript">
            //<![CDATA[   
            google.load('maps', '2', {callback:simple2});var map;
            function simple2(){	
            if (GBrowserIsCompatible()) { 
            var map = new GMap2(document.getElementById("map2"));
            map.addControl(new GLargeMapControl());
            map.addControl(new GMapTypeControl());	  
            map.setCenter(new GLatLng(22.5200,-82.1100),7);}}
            window.onload=function(){simple2();}
            //]]>
        </script> -->
        
        <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
	<script type="text/javascript">
          var map;
            
	  function initialize() {
	    var latlng = new google.maps.LatLng(<%=lat%>,<%=lan%>);
	    var myOptions = {
	      zoom: 15,
	      center: latlng,
        	mapTypeControl: true,
		mapTypeControlOptions: {style: google.maps.MapTypeControlStyle.DROPDOWN_MENU},
      		navigationControl: true,
      		navigationControlOptions: {style: google.maps.NavigationControlStyle.SMALL},
	        mapTypeId: google.maps.MapTypeId.ROADMAP
	    };
	    map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
            
            map.controls[google.maps.ControlPosition.TOP_RIGHT].push(
                FullScreenControl(map, 'Pantalla Completa',
                'Salir Pantalla Completa'));  
 
//	    var contentString = 
//	    '<h2 id="firstHeading" class="firstHeading">Alpina Motors</h2>'+
//	    '<div id="bodyContent">'+
//	    '<p>Dirección del lugar<br>'+
//	    '(01) 1234-5678<br>'+
//		'Palermo<br>'+
//		'Ciudad de Buenos Aires<br>'+
//		'Argentina</p>'+
//	    '</div>';

                <%
                EstadoEmpleado estadoDto = EstadoEmpleadoDaoFactory.create().findByPrimaryKey(empleado.getIdEstado());
                String nombreMarcador = empleado.getApellidoPaterno() + (empleado.getApellidoMaterno()!=null?" " + empleado.getApellidoMaterno():"") + " " + empleado.getNombre();

               %>       
                var contentString = ""
                        + "<div class='map_dialog'>"
                        + "    <b>Vendedor:</b></br>" + "<%=nombreMarcador.replaceAll("\\\"", "&quot;")%>"  + "<br/>"
                        + "    <b>Estado:</b> " + "<%=(estadoDto!=null?estadoDto.getNombre():"")%>" + "<br/>"
                        + "    <li> <a title='Detalles' onclick='muestraDetallesPromotor("+"<%=empleado.getIdEmpleado()%>"+",1);'>Detalles </a> </li> <br/>"
                        + "    <li> <a title='Hist&oacute;rico' onclick='muestraDetallesPromotor("+"<%=empleado.getIdEmpleado()%>"+",2);'>Hist&oacute;rico </a> </li><br/>"
                        + "    <li> <a title='Cobros' onclick='muestraDetallesPromotor("+"<%=empleado.getIdEmpleado()%>"+",3);'>Cobros </a> </li> <br/>"
                        + "    <li> <a title='Mensaje' onclick='muestraDetallesPromotor("+"<%=empleado.getIdEmpleado()%>"+",4);'>Mensaje </a> </li> <br/>"
                        + "    <li> <a title='Rutas' onclick='muestraDetallesPromotor("+"<%=empleado.getIdEmpleado()%>"+",5);'>Rutas </a> </li><br/>"
                        + "    <li> <a title='Pedidos' onclick='muestraDetallesPromotor("+"<%=empleado.getIdEmpleado()%>"+",6);'>Pedidos </a> </li><br/>"
                        + "</div>";
 
		/*var infowindow = new google.maps.InfoWindow({
		    content: contentString
		});	*/	
                var icon = "";
                var title = "";
                <%if(empleado.getRepartidor() == 1){// si no es repartidor                            
                    
                    long s = (new Date()).getTime();
                        long d = 0; 
                        try{
                            EmpleadoBitacoraPosicionDaoImpl bitacoraDao = new EmpleadoBitacoraPosicionDaoImpl();
                            String filtro = " ID_EMPLEADO = "+ empleado.getIdEmpleado() + " ORDER BY ID_BITACORA_POSICION DESC LIMIT 0,1";
                            EmpleadoBitacoraPosicion bitEmp = bitacoraDao.findByDynamicWhere(filtro,null)[0];
                            
                            d = bitEmp.getFecha().getTime();
                            //d = empleado.getFechaHora().getTime();
                        }catch(Exception e){}
                        long diferencia = s - d;
                        System.out.println("-------DIFERENCIA: "+diferencia);
                        if(diferencia < 300000){
                        %>
                            icon = "../../images/estatusEmpleado/icon_activoTrabajando.png",
                        <%}else{%>
                            icon = "../../images/estatusEmpleado/icon_desactivado.png",
                            <%}%>                    
                    
                    title = "Vendedor"; <%
                    
                    }else{// si es repartidor
                        
                        long s = (new Date()).getTime();
                            long d = 0; 
                            try{
                                EmpleadoBitacoraPosicionDaoImpl bitacoraDao = new EmpleadoBitacoraPosicionDaoImpl();
                                String filtro = " ID_EMPLEADO = "+ empleado.getIdEmpleado() + " ORDER BY ID_BITACORA_POSICION DESC LIMIT 0,1";
                                EmpleadoBitacoraPosicion bitEmp = bitacoraDao.findByDynamicWhere(filtro,null)[0];

                                d = bitEmp.getFecha().getTime();
                                //d = empleado.getFechaHora().getTime();
                            }catch(Exception e){}
                            long diferencia = s - d;
                            System.out.println("-------DIFERENCIA: "+diferencia);
                            if(diferencia < 300000){
                            %>
                                icon = "../../images/maps/40px-verde-camion.png",
                            <%}else{%>
                                icon = "../../images/maps/40px-gris-camion.png",
                                <%}%>
                        title = "Vendedor"; <%

                    }%>
    
 
		var marker = new google.maps.Marker({
                  position: latlng, 
                  map: map, 
                  title:title,
                  icon: icon
		});
 
		//infowindow.open(map,marker);
                
                
                var infoBubble = new InfoBubble({
                  map: map,
                  content: '<div class="infoBubble">'+contentString+'</div>',
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
    
                
		google.maps.event.addListener(marker, 'click', function() {
		  infoBubble.open(map,marker);
		});
                
                infoBubble.open(map,marker);
		
	  }
          
          
          function ubicaMapaHistorico(latitud, longitud) {
	    var latlng = new google.maps.LatLng(latitud,longitud);
	    var myOptions = {
	      zoom: 15,
	      center: latlng,
        	mapTypeControl: true,
		mapTypeControlOptions: {style: google.maps.MapTypeControlStyle.DROPDOWN_MENU},
      		navigationControl: true,
      		navigationControlOptions: {style: google.maps.NavigationControlStyle.SMALL},
	        mapTypeId: google.maps.MapTypeId.ROADMAP
	    };
	    var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
 
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
                    '<h2 id="firstHeading" class="firstHeading"> <%=empleado.getNumEmpleado()%></h2>'+
                    '<div id="bodyContent">'+
        	    '<p>Empleado<br>'+
        	    '<%=empleado.getNombre()+" "+ empleado.getApellidoPaterno() +" "+empleado.getApellidoMaterno()%></p>'+                    
        	    '</div>';
 
		var infowindow = new google.maps.InfoWindow({
		    content: contentString
		});		
 
		var marker = new google.maps.Marker({
	      position: latlng, 
	      map: map, 
	      title:"Título del Globo"
		});
 
		infowindow.open(map,marker);
 
		/*	
		google.maps.event.addListener(marker, 'click', function() {
		  infowindow.open(map,marker);
		});
		*/
               
               
               
               
               
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
          
	</script>
        
    </head>
    <body onload="initialize()">
        <!--<h1>Localización!</h1>        -->
        
        <!--<div id="map2" style="width:360px;height:200px;border:2px solid green;"></div>-->
        <div id="map_canvas" style="width:500px;height:500px;border:2px solid green;"></div>
        
    </body>
</html>
