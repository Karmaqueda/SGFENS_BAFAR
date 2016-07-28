<%-- 
    Document   : Mapa
    Created on : 3/01/2013, 10:08:33 AM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.dao.dto.EmpleadoBitacoraPosicion"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoBitacoraPosicionDaoImpl"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.bo.SGProspectoBO"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.factory.SgfensProspectoDaoFactory"%>
<%@page import="com.tsp.sct.dao.factory.ClienteDaoFactory"%>
<%@page import="com.tsp.sct.dao.factory.EmpleadoDaoFactory"%>
<%@page import="com.tsp.sct.dao.factory.EstadoEmpleadoDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.EstadoEmpleado"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProspecto"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.dto.Ubicacion"%>
<%@page import="com.tsp.sct.bo.UbicacionBO"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.dao.dto.Usuarios"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<!DOCTYPE html>

<% 
    Usuarios usuario = user.getUser();         
    Empresa empresa = new Empresa();    
    EmpresaBO empresaBo = new EmpresaBO(user.getConn());    
    empresa = empresaBo.findEmpresabyId(usuario.getIdEmpresa()); 
    Ubicacion ubicacion = new Ubicacion();
    UbicacionBO ubicacionBO = new UbicacionBO(user.getConn());
    ubicacion = ubicacionBO.findUbicacionbyId(empresa.getIdUbicacionFiscal());
    
    //String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String filtroBusqueda = " AND ID_EMPRESA <> ID_EMPRESA_PADRE";
    
    
    
    
    
    Cliente[] clientesDto = null;
    SgfensProspecto[] prospectosDto = null; 
    Empleado[] empleadosDto = null;
   
    if(empresa.getIdEmpresa() > 0){                 
                    
        try{
            ClienteBO clienteBO = new ClienteBO(user.getConn());
            clientesDto = clienteBO.findClientes(0, empresa.getIdEmpresa() , 0, 0, " AND (LATITUD <> 0 AND LONGITUD <> 0 AND LATITUD <> 'null' AND LONGITUD <> 'null' AND LATITUD IS NOT NULL AND LONGITUD IS NOT NULL ) AND ID_ESTATUS <> 2");            
        }catch(Exception e){}
        try{
            SGProspectoBO prospectoBO = new SGProspectoBO(user.getConn());
            prospectosDto = prospectoBO.findProspecto(0, empresa.getIdEmpresa(), 0, 0, " AND (LATITUD <> 0 AND LONGITUD <> 0 AND LATITUD IS NOT NULL AND LONGITUD IS NOT NULL ) ");           
        }catch(Exception e){}
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
            empleadosDto = empleadoBO.findEmpleados(0, empresa.getIdEmpresa(), 0, 0, " AND ID_DISPOSITIVO != -1  AND (LATITUD <> 0 AND LONGITUD <> 0 AND LATITUD IS NOT NULL AND LONGITUD IS NOT NULL ) ");
        }catch(Exception e){}
        
         
    }
    
    
    
    /*
    * Paginación
    */
    int paginaActual = 1;
    double registrosPagina = 10;
    double limiteRegistros = 0;
    int paginasTotales = 0;
    int numeroPaginasAMostrar = 5;

    try{
        paginaActual = Integer.parseInt(request.getParameter("pagina"));
    }catch(Exception e){}

    try{
        registrosPagina = Integer.parseInt(request.getParameter("registros_pagina"));
    }catch(Exception e){}
    
     EmpresaBO empresaBO = new EmpresaBO(user.getConn());
     Empresa[] empresasDto = new Empresa[0];
     try{
         limiteRegistros = empresaBO.findEmpresas(empresa.getIdEmpresa(), empresa.getIdEmpresa() , 0, 0, filtroBusqueda).length;
         
         //if (!buscar.trim().equals(""))
            // registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        /*empresasDto = empresaBO.findEmpresas(empresa.getIdEmpresa(), empresa.getIdEmpresa(),
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);*/
        empresasDto = empresaBO.findEmpresas(empresa.getIdEmpresa(), empresa.getIdEmpresa() , 0, 0, filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
    
      String parametrosPaginacion="";
    
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
            var global = [];
            var clientes = [];
            var prospectos = [];
            var promotores = [];
            
            
            var latitudEmpresa = <%=empresa.getLatitud()%>;
            var longitudEmpresa = <%=empresa.getLongitud()%>
            var idEmpresaActualizar = <%=empresa.getIdEmpresa()%>
	  function initialize() {
	    var latlng = new google.maps.LatLng(latitudEmpresa,longitudEmpresa);
	    var myOptions = {
	      zoom: 7,
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
                    '<h2 id="firstHeading" class="firstHeading"> <%=empresa.getRazonSocial()%></h2>'+
                    '<div id="bodyContent">'+
        	    '<p>Dirección<br>'+
        	    '<%=ubicacion.getCalle()+" "+ ubicacion.getNumExt()%> <br>'+
                    '<%=ubicacion.getColonia()+", "+ubicacion.getCodigoPostal()%><br>'+
                    '<%=ubicacion.getMunicipio()+", "+ubicacion.getEstado()%><br>'+
                    '<%=ubicacion.getPais()%></p>'+
        	    '</div>';
 
		/*var infowindow = new google.maps.InfoWindow({
		    content: contentString
		});*/
                
                var infoBubble = new InfoBubble({
                  map: mapita,
                  content: '<div class="infoBubble">'+contentString+'</div>',
                  //position: new google.maps.LatLng(-32.0, 149.0),             
                  shadowStyle: 1,
                  padding: 2,
                  backgroundColor: '#CCFFFF',
                  borderRadius: 7,
                  arrowSize: 10,
                  borderWidth: 2,
                  borderColor: '#319AF6',
                  disableAutoPan: true,
                  hideCloseButton: false,
                  arrowPosition: 30,
                  //backgroundClassName: 'transparent',
                  arrowStyle: 1
                });
        
                
 
		var marker = new google.maps.Marker({
                  position: latlng,                  
                  map:mapita,
                  icon: "../../images/Company_4_32.png",
                  title:"Sucursal"
		});
 
		//infowindow.open(mapita,marker);
              
               infoBubble.open(mapita, marker);
               
               
 
			
		google.maps.event.addListener(marker, 'click', function() {
		  infoBubble.open(mapita, marker);
		});
		
               
               //VAMOS A RECUPERAR LAS COORDENADAS /////////////////////
               //Creo un evento asociado a "mapa" cuando se hace "click" sobre el
                google.maps.event.addListener(mapita, "dblclick", function(evento) {
                //Obtengo las coordenadas separadas
                //var latitud = evento.latLng.lat();
                //var longitud = evento.latLng.lng();
                latitudEmpresa = evento.latLng.lat();
                longitudEmpresa = evento.latLng.lng();
                
                //LIMPIAMOS EL MAPA Y LO MOSTRMOS DE NUEVO CON LAS NUEVAS COORDENADAS
                initialize();
                //ACTUALIZAMOS LAS COORDENADAS
                actualizarCoordenadasEmpresa(latitudEmpresa,longitudEmpresa,idEmpresaActualizar);
                //Puedo unirlas en una unica variable si asi lo prefiero
                //var coordenadas = evento.latLng.lat() + ", " + evento.latLng.lng();

               //Las muestro con un popup
                //alert(coordenadas);
                
                //Creo un marcador utilizando las coordenadas obtenidas y almacenadas por separado en "latitud" y "longitud"
                //var coordenadas = new google.maps.LatLng(latitud, longitud); /* Debo crear un punto geografico utilizando google.maps.LatLng */
                //var marcador = new google.maps.Marker({position: coordenadas,map: mapita, animation: google.maps.Animation.DROP, title:"Un marcador cualquiera"});
                }); //Fin del evento
                /////////////////////
                
                fillMap();
                
	  }      
          
          function actualizarCoordenadasEmpresa(latitudEmpresa,longitudEmpresa,idEmpresa){
                if (idEmpresa>0){                    
                    $.ajax({
                        type: "POST",
                        url: "Mapa_ajax.jsp",
                        data: {latitudActualizada : latitudEmpresa, longitudActualizada : longitudEmpresa, idEmpresaActualizar : idEmpresa},
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
                                            //location.href = "catEmpleados_list.jsp";
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
          
          
            function agregaMarcador(lat, lng, title){
                //alert(lat +"--"+lng+"--"+title);
                var crear = 0;
                if(global.length > 0){
                    for(var i = 0, marcador; marcador = global[i]; i ++){
                        var posicion = marcador.getPosition();
                        var posicion2 = new google.maps.LatLng(lat,lng);
                        if(posicion.lat()==posicion2.lat() && posicion.lng()==posicion2.lng()){
                            crear = 0;
                            if(marcador.getMap()==null){
                                marcador.setMap(mapita);                                  
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
            
                if(crear == 1){
                    var marcadorBasico = creaMarcadorBasico(
                            lat,
                            lng,
                            title
                        )
                    global.push(
                      marcadorBasico  
                    );
                    marcadorBasico.setMap(mapita);
                }
            }
            
            
            function creaMarcadorBasico(lat,lng,title){
                var marcador = new google.maps.Marker({
                        position: new google.maps.LatLng(lat,lng),
                        animation: google.maps.Animation.DROP,
                        icon: "../../images/Company_4_32.png",
                        title:title
                    });
                    
                var infoBubbleM = new InfoBubble({
                  map: mapita,
                  content: '<div class="infoBubble">'+title+'</div>',
                  //position: new google.maps.LatLng(-32.0, 149.0),             
                  shadowStyle: 1,
                  padding: 2,
                  backgroundColor: '#CCFFFF',
                  borderRadius: 7,
                  arrowSize: 10,
                  borderWidth: 2,
                  borderColor: '#319AF6',
                  disableAutoPan: true,
                  hideCloseButton: false,
                  arrowPosition: 30,
                  //backgroundClassName: 'transparent',
                  arrowStyle: 1
                });
                
                google.maps.event.addListener(marcador, 'click', function() {
		  infoBubbleM.open(mapita, marcador);
		});
                
                infoBubbleM.open(mapita, marcador);
                
                return marcador;
            }
            
            
            function muestraMarcador(chk){
                switch(chk.id){
                    case "clientes":
                        if(chk.checked){
                            for(var i = 0, marcador; marcador = clientes[i]; i ++){
                                marcador.setMap(mapita);
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
                                marcador.setMap(mapita);
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
                                marcador.setMap(mapita);
                            }
                        }else{
                            for(var i = 0, marcador; marcador = promotores[i]; i ++){
                                marcador.setMap(null);
                            }
                        }
                        break;
                }             
            }
            
            
            function fillMap(){
                
                <%
                for(Cliente clienteDto:clientesDto){
                    String nombreCliente = clienteDto.getRazonSocial();
                    String dialogoCliente = ""
                         + "<div class='map_dialog'>"
                         + "   <b>Cliente:</b><br/>" + nombreCliente.replaceAll("\\\"", "&quot;") + "<br/><br/>"                         
                         + " </div>";
                    %>
                     clientes.push(
                         creaMarcador(
                             <%=clienteDto.getLatitud()!=null?(!clienteDto.getLatitud().equals("")?clienteDto.getLatitud():empresa.getLatitud()):"0" %>,
                             <%=clienteDto.getLongitud()!=null?(!clienteDto.getLongitud().equals("")?clienteDto.getLongitud():empresa.getLongitud()):"0" %>,
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
                        + "    <b>Prospecto:</b><br/>" + nombreCliente.replaceAll("\\\"", "&quot;") + "<br/><br/>"
                        + "</div>";
                    %>
                    prospectos.push(
                        creaMarcador(
                            <%=prospectoDto.getLatitud()!=0?prospectoDto.getLatitud():empresa.getLatitud() %>,
                            <%=prospectoDto.getLongitud()!=0?prospectoDto.getLongitud():empresa.getLongitud()%>,
                            "<%=nombreCliente.replaceAll("\\\"", "&quot;") %>",
                            "../../images/maps/map_marker_pros.png",
                            "<%=dialogoCliente %>"
                        )
                    );         
                    <%
                }
                %>
                <%
                
                for(Empleado empleadoDto:empleadosDto){
                    EstadoEmpleado estadoDto = EstadoEmpleadoDaoFactory.create().findByPrimaryKey(empleadoDto.getIdEstado());
                    String nombreMarcador = empleadoDto.getApellidoPaterno() + (empleadoDto.getApellidoMaterno()!=null?" " + empleadoDto.getApellidoMaterno():"") + " " + empleadoDto.getNombre();
                    String dialogoMarcador = ""
                        + "<div class='map_dialog'>"
                        + "    <b>Vendedor:</b></br>" + nombreMarcador.replaceAll("\\\"", "&quot;") + "<br/>"
                        + "    <b>Estado:</b> " + (estadoDto!=null?estadoDto.getNombre():"") + "<br/>"                        
                        + "</div>";
                    %>
                    promotores.push(
                        creaMarcador(
                            <%=empleadoDto.getLatitud()!=0?empleadoDto.getLatitud():empresa.getLatitud()%>,
                            <%=empleadoDto.getLongitud()!=0?empleadoDto.getLongitud():empresa.getLongitud()%>,
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
                            
                            }else{ // si es repartidor
                                
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
                            
                                                        
                            <%}%>
                                
                            "<%=dialogoMarcador %>"
                        )
                    );
                    <%
                }
                %>
                                    
                //Mostramos marcadores al cargar
                for(var i = 0, marcador; marcador = clientes[i]; i ++){
                                marcador.setMap(mapita);
                } 
                for(var i = 0, marcador; marcador = prospectos[i]; i ++){
                                marcador.setMap(mapita);
                }
                for(var i = 0, marcador; marcador = promotores[i]; i ++){
                                marcador.setMap(mapita);
                }
            
        }
        
        
        function creaMarcador(lat,lng,title,img,content){
                var marcador = new google.maps.Marker({
                        position: new google.maps.LatLng(lat,lng), 
                        title:title,
                        animation: google.maps.Animation.DROP,
                        icon:img
                    });
                
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
                    infoBubble.open(mapita, marcador);
                });
                
        
                return marcador;
            }
         
	</script>
        
    </head>
    <body onload="initialize()">
        
        <div class="twocolumn">
            <div class="column_left">
                <div class="header">
                    <span>          
                       Mapa
                    </span>
                </div>
                <div class="content">                 
                    <br><br>
                    <div id="map_canvas" style="width:480px;height:550px"></div>
                    </br>
                    <label><b>Mostrar en Mapa:</b></label>                   
                        <input id="clientes" type="checkbox" onclick="muestraMarcador(this);"/>Clientes
                        <input id="prospectos" type="checkbox" onclick="muestraMarcador(this);"/>Prospectos
                        <input id="promotores" type="checkbox" onclick="muestraMarcador(this);"/>Vendedores                    
                </div>
            </div>
            <div class="column_right">
                <div class="header">
                    <span>          
                       Sucursales
                    </span>
                </div>
                <div class="content"  style="width: 500px; height: 580px;overflow: scroll;">        
                    <form id="form_data" name="form_data" action="" method="post">
                                <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>RFC</th>
                                            <th>Razón Social</th> 
                                            <th>Nombre Comercial</th>                                            
                                            <th>Ubicar</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                            for (Empresa item:empresasDto){
                                                try{
                                        %>
                                        <tr <%=(item.getIdEstatus()!=1)?"class='inactive'":""%>>                                           
                                            <td><%=item.getIdEmpresa() %></td>
                                            <td><%=item.getRfc() %></td>
                                            <td><%=item.getRazonSocial() %></td>                                            
                                            <td><%=item.getNombreComercial() %></td>
                                            <td><a href="javascript:void(0)" onclick="agregaMarcador(<%=item.getLatitud()%>,<%=item.getLongitud()%>,'<%=item.getNombreComercial()%>');" ><img src="../../images/icon_movimiento.png" alt="Ubicar"/></a></td> 
                                        </tr>
                                        <%      }catch(Exception ex){
                                                    ex.printStackTrace();
                                                }
                                            } 
                                        %>
                                    </tbody>
                                </table>
                            </form>                           
                    
                </div>
            </div>
        <div>
        
    </body>
</html>
