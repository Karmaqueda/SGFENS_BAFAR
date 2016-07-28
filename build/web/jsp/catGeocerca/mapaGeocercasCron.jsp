<%-- 
    Document   : mapaGeocercasCron; Verifica si los promotores estan fuera de su geocerca asignada y emitira una alerta.
    Created on : 5/05/2014, 01:20:22 PM
    Author     : leonardo

Referencias: http://jsfiddle.net/kaiser/wzcst/
                http://jsfiddle.net/vF7u2/

--%>

<%@page import="com.tsp.sct.dao.factory.EstadoEmpleadoDaoFactory"%>
<%@page import="com.tsp.sct.dao.factory.EmpleadoDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.EstadoEmpleado"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="com.tsp.sct.dao.dto.Geocerca"%>
<%@page import="com.tsp.sct.bo.GeocercaBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<!DOCTYPE html>
<jsp:include page="../include/jsFunctions.jsp"/>
<% 
    int idEmpresa = user.getUser().getIdEmpresa();
    int idGeocerca = 0;
        try {
            idGeocerca = Integer.parseInt(request.getParameter("idGeocerca"));
        } catch (NumberFormatException e) {
        }
String forma = request.getParameter("forma")!=null? new String(request.getParameter("forma").getBytes("ISO-8859-1"),"UTF-8") :"";


    
    GeocercaBO geoBO = new GeocercaBO(user.getConn());
    Geocerca[] geocercasTodas = geoBO.findGeocercas(0, idEmpresa, 0, 0, " AND ID_ESTATUS = 1 ");
    //String forma = "";
    //for(Geocerca geoCer : geocercasTodas){
        //idGeocerca = geoCer.getIdGeocerca();
        //forma = String.valueOf(geoCer.getTipoGeocerca());
        GeocercaBO geocercaBO = new GeocercaBO(idGeocerca,user.getConn());
        Geocerca geocerca = geocercaBO.getGeocerca();
        System.out.println("---- DATOS, ID GEOCERCA, FORMA: "+idGeocerca+", "+forma);
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
            var map;
            var promotores = [];
            var figura;
                
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
                    
                    map = new google.maps.Map(document.getElementById("map"), mapOptions);
                    
                   
                    
                          
                     
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
                        figura = circle; //variable nueva para mapear el tipo de figura y mostrar los promotores que se encuentran dentro de esa geocerca ----
                  
                <%}else if(forma.equals("2")){%>
                    var latLngCenter = new google.maps.LatLng(<%=puntoInferiorIzquierdoLatitud%>, <%=puntoSuperiorDerechoLongitud%>)
                    var mapOptions = {                        
                        center: latLngCenter,
                        zoom: 14,
                        mapTypeId: google.maps.MapTypeId.ROADMAP
                    };
                    map = new google.maps.Map(document.getElementById("map"), mapOptions);

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
                    figura = rect; //variable nueva para mapear el tipo de figura y mostrar los promotores que se encuentran dentro de esa geocerca ----

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
                    map = new google.maps.Map(document.getElementById("map"), mapOptions);

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
                      
                      figura = polygono; //variable nueva para mapear el tipo de figura y mostrar los promotores que se encuentran dentro de esa geocerca ----
                <%                    
                }                                
                %>
                
                //cargamos los marcadores y datos de empleados/promotores, para cargarlos los que estan dentro de la geocerca ----
                fillMap();                
            };

            function creaLatLng(lat, lng){
                return new google.maps.LatLng(lat,lng);
            }
            
           

             function fillMap(){                 
                <%
                Empleado[] empleadosDto = new EmpleadoBO(user.getConn()).findEmpleados(0, idEmpresa, 0, 0, " AND ID_GEOCERCA = "+idGeocerca);
                
                //Empleado[] empleadosDto = EmpleadoDaoFactory.create().findWhereIdEmpresaEquals(idEmpresa);
                for(Empleado empleadoDto:empleadosDto){
                    System.out.println("Empleado: "+empleadoDto.getNumEmpleado());
                
                    //EstadoEmpleado estadoDto = EstadoEmpleadoDaoFactory.create().findByPrimaryKey(empleadoDto.getIdEstado());
                    String nombreMarcador = empleadoDto.getApellidoPaterno() + (empleadoDto.getApellidoMaterno()!=null?" " + empleadoDto.getApellidoMaterno():"") + " " + empleadoDto.getNombre();
                    String dialogoMarcador = ""/*
                        + "<div class='map_dialog'>"
                        + "    <b>Promotor:</b> " + nombreMarcador.replaceAll("\\\"", "&quot;") + "<br/>"
                        + "    <b>Estado:</b> " + (estadoDto!=null?estadoDto.getNombre():"") + "<br/>"
                        + "    <input type='button' value='Detalles' onclick='muestraDetallesPromotor("+empleadoDto.getIdEmpleado()+",1);' /><br/>"
                        + "    <input type='button' value='Hist&oacute;rico' onclick='muestraDetallesPromotor("+empleadoDto.getIdEmpleado()+",2);' /><br/>"
                        + "    <input type='button' value='Cobros' onclick='muestraDetallesPromotor("+empleadoDto.getIdEmpleado()+",3);' /><br/>"
                        + "    <input type='button' value='Mensaje' onclick='muestraDetallesPromotor("+empleadoDto.getIdEmpleado()+",4);' /><br/>"
                        + "    <input type='button' value='Rutas' onclick='muestraDetallesPromotor("+empleadoDto.getIdEmpleado()+",5);' /><br/>"
                        + "</div>"*/;
                    %>
                    promotores.push(
                        creaMarcador(
                            <%=empleadoDto.getLatitud() %>,
                            <%=empleadoDto.getLongitud() %>,
                            "<%=nombreMarcador.replaceAll("\\\"", "&quot;") %>",                                                           
                            "../../images/estatusEmpleado/icon_desactivado.png",
                            "<%=dialogoMarcador %>"
                        )
                    );
                    <%
                }
                %> 
                        verificaMarcadores(figura.getBounds());
            }
            
            function verificaMarcadores(bounds){ //function nueva para recuperar los promotores que estan dentro de la geocerca ----
                for(var i = 0, marcador; marcador = promotores[i]; i ++){
                    if(bounds.contains(marcador.getPosition())){                        
                        marcador.setMap(map);
                                             
                    }else{                        
                        marcador.setMap(null);
                        
                        //EMPEZAMOS A ARMAR LA NOTIFICACION DE ENVIO DE ALERTA, SALIO DE LA GEOCERCA ASIGNADA.                        
                        generandoAlerta('<%=idGeocerca%>', marcador.getTitle());  
                    }                    
                }
            }
            
            function creaMarcador(lat,lng,title,img,content){ //funtion nueva para crear los marcadores de los promotores que estaran dentro de la geocerca                
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
                return marcador;
            }
            
            function generandoAlerta(idGeocerca, promotor){
               //ya no tuvo funcionamiento                
            }
                
        </script>

    </head>
    <body onload="init(); " >
        <!--<h1>Localización!</h1>        -->
        
        <!--<div id="map2" style="width:360px;height:200px;border:2px solid green;"></div>-->
        <!--<div id="map_canvas" style="width:900px;height:400px;border:2px solid green; margin: 0px auto;"></div>-->
        <div id="map" style="width:540px;height:318px;border:2px solid green; "></div>
        
    </body>
    
</html>


