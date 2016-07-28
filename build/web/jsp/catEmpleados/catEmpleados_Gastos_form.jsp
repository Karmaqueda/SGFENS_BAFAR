<%-- 
    Document   : catEmpleados_Gastos_form
    Created on : 29/07/2015, 06:50:43 PM
    Author     : HpPyme
--%>

<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.bo.CatalogoGastosBO"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.bo.GastosEvcBO"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.dao.dto.GastosEvc"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    int idGasto = -1;
    try{ idGasto = Integer.parseInt(request.getParameter("idGastos")); }catch(NumberFormatException e){}    
    
    String filtroBusqueda = "";     
    
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    GastosEvc gastoDto = null ;
    Empleado empleadoDto =  null;
    String empledoNombre = "" ;
    int idUsuarioGasto = 0;
    int idUserGasto = 0;
    if(idGasto > 0){        
        gastoDto = new GastosEvcBO(idGasto,user.getConn()).getGastos();
        if(gastoDto!= null){
            
            empleadoDto =  new EmpleadoBO(gastoDto.getIdEmpleado(), user.getConn()).getEmpleado();
            
            empledoNombre = empleadoDto!=null?((empleadoDto.getNombre()!=null?empleadoDto.getNombre():"") 
                            + (empleadoDto.getApellidoPaterno()!=null?" " + empleadoDto.getApellidoPaterno():"") 
                            + (empleadoDto.getApellidoMaterno()!=null?" " + empleadoDto.getApellidoMaterno():"")):"";
            
        }
        
        
        if(gastoDto.getIdEmpleado()>0){
            idUsuarioGasto  = empleadoDto.getIdUsuarios();
            
            try{
                EmpleadoBO empleadoBO = new EmpleadoBO(gastoDto.getIdEmpleado(),user.getConn());
                idUserGasto =  empleadoBO.getUsuarioBO().getUser().getIdUsuarios();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        
        
    }   
    
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    DecimalFormat decimales = new DecimalFormat("0.00");  
    
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
            
        /// Mapa
            var coordenada;
            var marker;
            var map;
            
            <%if(gastoDto!=null){%>
            function initialize() {
                
                var coordenada = new google.maps.LatLng(<%=gastoDto!=null?gastoDto.getLatitud():0 %>,<%=gastoDto!=null?gastoDto.getLongitud():0%>);
                
                var mapOptions = {
                    zoom: 14,
                    center: coordenada
                };

                 map = new google.maps.Map(document.getElementById('map_canvas'),
                          mapOptions);
                          
                 map.controls[google.maps.ControlPosition.TOP_RIGHT].push(
                FullScreenControl(map, 'Pantalla Completa',
                'Salir Pantalla Completa'));

                 marker = new google.maps.Marker({
                    map:map,
                    animation: google.maps.Animation.DROP,
                    position: coordenada,
                    title: "Gasto ID: " + <%=gastoDto!=null?gastoDto.getIdGastos():0 %>
                 });
                
                 /* google.maps.event.addListener(marker, 'click', function() {
                    infowindow.open(map,marker);  
                });*/
                
               
                                
            }
        
            function loadScript() {
                var script = document.createElement('script');
                script.type = 'text/javascript';
                script.src = 'https://maps.googleapis.com/maps/api/js?libraries=geometry,drawing&sensor=false&' +
                    'callback=initialize';
                document.body.appendChild(script);
            }
            
            window.onload = loadScript;
            <%}%>
            
            /// Fin Mapa               
            
            
            
            function grabar(){
                
                    $.ajax({
                        type: "POST",
                        url: "catEmpleados_ajax.jsp?mode=guardaGasto",
                        data: $("#frm_action").serialize(),
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
                                            location.href = "catEmpleados_Gastos_list.jsp?idGastos="+<%=idGasto%>;
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
            
        </script>
    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Gastos</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <!--Inicio columna derecha -->
                        <div class="column_left">
                            <div class="header">
                                <span>             
                                    <img src="../../images/icon_ventas.png"/>
                                    <%=!empledoNombre.equals("")?"  Empleado  " + empledoNombre:"Gastos" %>  
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                <input type="hidden" id="idGastos" name="idGastos" value="<%=gastoDto!=null?gastoDto.getIdGastos():"" %>" /> 
                                   <p>
                                        <label>Fecha y Hora:</label><br/>
                                        <input maxlength="50" type="text" id="fechaGasto" name="fechaGasto" style="width:300px"
                                               value="<%=gastoDto != null ? format.format(gastoDto.getFecha()) : DateManage.dateTimeToStringEspanol(new Date()) %>" 
                                               <%=gastoDto != null ? "readonly" : "readonly"%> />
                                    </p>
                                    <br/>      
                                    <p>
                                    <label>Empleado:</label><br/>
                                    <select id="q_idvendedor" name="q_idvendedor" class="flexselect" >
                                    <option></option>                 
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo((int)idEmpresa, RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL,gastoDto != null ? gastoDto.getIdEmpleado() : 0) %>                         
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo((int)idEmpresa, RolesBO.ROL_CONDUCTOR_VENDEDOR, idUsuarioGasto) %>
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo((int)idEmpresa, RolesBO.ROL_COBRADOR, idUsuarioGasto) %>
                                    </select>
                                    </p>
                                    <br/>  
                                    <p>
                                        <label>Concepto Gasto:</label><br/>
                                        <select size="1" id="motivoGasto" name="motivoGasto" style="width:310px" <%=gastoDto != null ? "disabled" : ""%> >
                                            <option value="-1">Selecciona un Concepto</option>
                                            <%
                                                out.print(new CatalogoGastosBO(user.getConn()).getGastosMotivoByIdHTMLCombo(idEmpresa, (gastoDto != null ? gastoDto.getIdConcepto(): 0)));
                                            %>
                                        </select>                                        
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Monto:</label><br/>
                                        <input maxlength="50" type="text" id="monto" name="monto" style="width:300px" 
                                               onkeypress="return validateNumber(event);"
                                               value="<%=gastoDto != null ? decimales.format(gastoDto.getMonto()) : ""%>" <%=gastoDto != null ? "readonly" : ""%> />
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Comentarios:</label><br/>
                                        <textarea maxlength="50" type="text" id="comentarios" name="comentarios" style="width:300px"
                                                <%=gastoDto != null ? "readonly" : ""%>  /><%=gastoDto != null ? gastoDto.getComentario() : ""%></textarea>
                                    </p>
                                    <br/>
                                    
                                    <br/><br/><br/><br/>
                                    <% if (gastoDto == null){ %>
                                            <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                                    <% } %>
                                   <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                            </div>
                        </div>
                        <!-- Fin columna Derecha -->
                        
                       
                        <%
                        if (gastoDto!=null){
                            if((gastoDto.getLatitud() != 0)&& (gastoDto.getLongitud() != 0) )
                            {          
                            %>    
                            <div class="column_right">                            
                                <div class="header">
                                    <span>
                                        <img src="../../images/icon_mapa_1.png" alt="icon"/>
                                        Ubicación
                                    </span>
                                </div>
                                <div class="content">
                                    <div id="map_canvas" style="height: 230px; width: 100%">
                                        <img src="../../images/maps/ajax-loader.gif" alt="Cargando" style="margin: auto;"/>
                                    </div>     
                                </div>
                            </div>  
                            <% 
                            }   
                        }
                        %>
                        
                        
                        
                        
                    </div>
                    </form>
                    <!--TODO EL CONTENIDO VA AQUÍ-->

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>

         <script>                          
            $("select.flexselect").flexselect();
        </script>
    </body>
</html>
<%}%>
