<%-- 
    Document   : mapaCirculo
    Created on : 2/05/2013, 04:59:23 PM
    Author     : Leonardo
Referencias: http://jsfiddle.net/kaiser/wzcst/
                http://jsfiddle.net/vF7u2/

--%>

<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="com.tsp.sct.dao.dto.Geocerca"%>
<%@page import="com.tsp.sct.bo.GeocercaBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<!DOCTYPE html>

<% int idGeocerca = 0;
        try {
            idGeocerca = Integer.parseInt(request.getParameter("idGeocerca"));
        } catch (NumberFormatException e) {
        }
String forma = request.getParameter("forma")!=null? new String(request.getParameter("forma").getBytes("ISO-8859-1"),"UTF-8") :"";


System.out.println("---- DATOS, ID GEOCERCA, FORMA: "+idGeocerca+", "+forma);

GeocercaBO geocercaBO = new GeocercaBO(idGeocerca,user.getConn());
Geocerca geocerca = geocercaBO.getGeocerca();
//CIRCULO
String latitudCentro = "";
String LogitudCentro = "";
String radio = "";
//CUADRADO
String puntoInferiorIzquierdoLatitud = "";
String puntoInferiorIzquierdoLongitud = "";
String puntoSuperiorDerechoLatitud = "";
String puntoSuperiorDerechoLongitud = "";
//POLIGONO
String[] puntosRuta = null;
String ptsRuta = "";


        	StringTokenizer tokens = new StringTokenizer(geocerca.getCoordenadas(),",");
                if(forma.equals("1")){
                    radio = tokens.nextToken().intern();
                    latitudCentro = tokens.nextToken().intern();
                    LogitudCentro = tokens.nextToken().intern();
                }else if(forma.equals("2")){
                    puntoInferiorIzquierdoLatitud = tokens.nextToken().intern();
                    puntoInferiorIzquierdoLongitud = tokens.nextToken().intern();
                    puntoSuperiorDerechoLatitud = tokens.nextToken().intern();
                    puntoSuperiorDerechoLongitud = tokens.nextToken().intern();
                }else if(forma.equals("3")){
                    //String puntosPolygon = geocerca.getCoordenadas();
                    
                    /*puntosPolygon =  puntosPolygon.replaceAll("\\),\\(", "|");
                    puntosPolygon =  puntosPolygon.replaceAll("\\)", "");
                    puntosPolygon =  puntosPolygon.replaceAll("\\(", "");*/
                    
                  //  System.out.println("......................" + puntosPolygon); 
                                       
                    //puntosRuta = puntosPolygon.split("\\|");
                    while(tokens.hasMoreTokens()){                
                        
                        ptsRuta += tokens.nextToken();
                        ptsRuta += ",";
                        ptsRuta += tokens.nextToken();
                        ptsRuta += "|";
                        
                    }
                    
                    System.out.println("ptsRuta ::: " + ptsRuta);
                    puntosRuta = ptsRuta.split("\\|");
                }
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?v=3&sensor=true"></script>
        <script type="text/javascript">                       
            function init() {
                
                    <%if(forma.equals("1")){%>
               
                   
                    var circle;
                    var centro = new google.maps.LatLng(<%=latitudCentro%>,<%=LogitudCentro%>);
                    var radio = <%=radio%>;
                    
                    var mapOptions = {                        
                        center: centro,
                        zoom: 14,
                        mapTypeId: google.maps.MapTypeId.ROADMAP
                    };
                    
                    var map = new google.maps.Map(document.getElementById("map"), mapOptions);
                    
                   
                    
                          
                     
                    var populationOptions = {
                              fillColor: '#fff',
                              fillOpacity: .6,
                              strokeColor: '#313131',
                              strokeOpacity: .4,
                              strokeWeight: .8,
                              map: map,
                              center: centro,
                              radius: radio
                        };
                        // Add the circle for this city to the map.
                        circle = new google.maps.Circle(populationOptions);                  
                    
                  
                <%}else if(forma.equals("2")){%>
                    var latLngCenter = new google.maps.LatLng(<%=puntoInferiorIzquierdoLatitud%>, <%=puntoSuperiorDerechoLongitud%>)
                    var mapOptions = {                        
                        center: latLngCenter,
                        zoom: 14,
                        mapTypeId: google.maps.MapTypeId.ROADMAP
                    };
                    var map = new google.maps.Map(document.getElementById("map"), mapOptions);

                    var rect = new google.maps.Rectangle({
                      bounds: new google.maps.LatLngBounds(
                        new google.maps.LatLng(<%=puntoInferiorIzquierdoLatitud%>,<%=puntoInferiorIzquierdoLongitud%>),
                        new google.maps.LatLng(<%=puntoSuperiorDerechoLatitud%>,<%=puntoSuperiorDerechoLongitud%>)
                      ),
                      map: map, 
                      fillColor: '#fff',
                      fillOpacity: .6,
                      strokeColor: '#313131',
                      strokeOpacity: .4,
                      strokeWeight: .8
                    })
                    /*var rect2 = new google.maps.Rectangle({
                      bounds: new google.maps.LatLngBounds(
                        new google.maps.LatLng(-30,-30),
                        new google.maps.LatLng(-20,-20)
                      ),
                      map: map 
                    })*/
                <%}else if(forma.equals("3")){  
                    
                    
                        for(String puntoCentrar:puntosRuta){                             
                            String[] latLng = puntoCentrar.split(","); 
                    %>
                            var latLngCenter = new google.maps.LatLng(<%=latLng[0]%>,<%=latLng[1]%>);                        
                    <%    
                            break;
                    } 
                    %>
                
                    //var latLngCenter = new google.maps.LatLng(puntosRuta,101)
                    var mapOptions = {                        
                        center: latLngCenter,
                        zoom: 14,
                        mapTypeId: google.maps.MapTypeId.ROADMAP
                    };
                    var map = new google.maps.Map(document.getElementById("map"), mapOptions);

                     // Define the LatLng coordinates for the polygon's path.
                    var polygono;
                    var polyCoordenadas;
                    
                    polyCoordenadas = [
                    <% 
                    for(String datosPunto:puntosRuta){ 
                        String[] latLng = datosPunto.split(","); 
                    %>
                        creaLatLng(<%=latLng[0]%>,<%=latLng[1]%>),                        
                    <%    
                    } 
                    %>
                    ];            
                                
                   
                    // Construct the polygon.
                     polygono = new google.maps.Polygon({
                        paths: polyCoordenadas,
                        strokeColor: '#313131',
                        strokeOpacity: 0.4,
                        strokeWeight: 0.8,
                        fillColor: '#fff',
                        fillOpacity: 0.6
                     });

                      polygono.setMap(map);
                <%                    
                }                                
                %>
                        
                //Ponemos marcador ultima posición empleado        
                dibujaMarcador(map);        
                        
            };

            function creaLatLng(lat, lng){
                return new google.maps.LatLng(lat,lng);
            }
            
            function dibujaMarcador(map){
                
                
                <%
                String idEmpleado = request.getParameter("idEmpleado")!=null? new String(request.getParameter("idEmpleado").getBytes("ISO-8859-1"),"UTF-8") :"";   
                
                if(!idEmpleado.equals("")){
                    EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
                    Empleado empleadoDto = empleadoBO.findEmpleadobyId(Integer.parseInt(idEmpleado));
                    
                    //if((empleadoDto.getLatitud()!=0) && (empleadoDto.getLongitud()!=0)){
                    
                %>
                        
                        
                        var coordenadas = new google.maps.LatLng(<%=empleadoDto.getLatitud() %>,<%=empleadoDto.getLongitud()%>);
                        var marker;


                        marker = new google.maps.Marker({                            
                                animation: google.maps.Animation.DROP,
                                position: coordenadas,
                                title: "Empleado ID: " + <%=empleadoDto.getIdEmpleado()%>
                             });


                        marker.setMap(map);
                <%
                   // }else{
                %>
                   // alert("El empleado no tiene una posicion valida.");
                <%
                        
                   // }
                }
                %>
             }

                
        </script>

    </head>
    <body onload="init()">
        <!--<h1>Localización!</h1>        -->
        
        <!--<div id="map2" style="width:360px;height:200px;border:2px solid green;"></div>-->
        <!--<div id="map_canvas" style="width:900px;height:400px;border:2px solid green; margin: 0px auto;"></div>-->
        <div id="map" style="width:540px;height:318px;border:2px solid green; "></div>
        
    </body>
</html>
</html>
