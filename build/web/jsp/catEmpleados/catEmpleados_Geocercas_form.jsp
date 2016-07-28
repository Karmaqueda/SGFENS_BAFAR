<%-- 
    Document   : catEmpleados_Geocercas_form
    Created on : 9/05/2013, 01:37:08 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<!--------<//%@page import="com.tsp.microfinancieras.dao.dto.SgfensClienteVendedor"%>
<//%@page import="com.tsp.microfinancieras.bo.SGClienteVendedorBO"%>
<//%@page import="com.tsp.microfinancieras.bo.RolesBO"%>-->
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.PasswordBO"%>
<%@page import="com.tsp.sct.bo.GeocercaBO"%>
<%@page import="com.tsp.sct.dao.jdbc.GeocercaDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Geocerca"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el cliente tiene acceso a este topico
    if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
        response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
        response.flushBuffer();
    } else {

        long idEmpresa = user.getUser().getIdEmpresa();
        
        /*
         * Parámetros
         */
        int idGeocerca = 0;
        try {
            idGeocerca = Integer.parseInt(request.getParameter("idGeocerca"));
        } catch (NumberFormatException e) {
        }
        
         int idEmpleado = -1;
        try{ 
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado")); 
        }catch(NumberFormatException e){}
         

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        String newRandomPass = "";
        
        GeocercaBO geocercaBO = new GeocercaBO(user.getConn());
        Geocerca geocercasDto = null;
        if (idGeocerca > 0){
            geocercaBO = new GeocercaBO(idGeocerca,user.getConn());
            geocercasDto = geocercaBO.getGeocerca();
        }else{
            newRandomPass = new PasswordBO().generateValidPassword();
        }
        
        String paramName = "idGeocerca";
        String urlTo2 = "../catGeocerca/mapaFiguras.jsp";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <link rel="shortcut icon" href="../../images/favicon.ico">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/keyWordSEO.jsp" />

        <title><jsp:include page="../include/titleApp.jsp" /></title>

        <jsp:include page="../include/skinCSS.jsp" />

        <jsp:include page="../include/jsFunctions.jsp"/>
        
        <script type="text/javascript">
                        
            function grabar(){
                var idEmpleado = document.getElementById("idEmpleado").value;
                var idGeocerca = document.getElementById("idGeocercaSeleccionada").value;
                var inicio_hora = document.getElementById("inicio_hora").value;
                var fin_hora = document.getElementById("fin_hora").value;
                    $.ajax({
                        type: "POST",
                        url: "catEmpleados_ajax.jsp",
                        data: {mode: 'asignarGeo', idEmpleado : idEmpleado , idGeocerca : idGeocerca, inicio_hora : inicio_hora, fin_hora : fin_hora},
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
                                            location.href = "catEmpleados_list.jsp";
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
            
                
            

            function validar(){
                /*
                if(jQuery.trim($("#nombre").val())==""){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "nombre de contacto" es requerido</center>',{'animate':true});
                    $("#nombre_contacto").focus();
                    return false;
                }
                */
                return true;
            }
            
             $(function() {	
                    $.timepicker.regional['es'] = {
                            timeOnlyTitle: 'Elegir Hora',
                            timeText: '',
                            hourText: 'Horas',
                            minuteText: 'Minutos',
                            secondText: 'Segundos',
                            currentText: 'Ahora',
                            closeText: 'Aceptar'
                        };
                     $.timepicker.setDefaults($.timepicker.regional['es']);  

                    $('.time').timepicker({
                             hourMin: 0,
                             hourMax: 23,
                             addSliderAccess: true,
                             sliderAccessArgs: { touchonly: false },

                     });

              });
  
            
        </script>
    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Mapas</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_marca.png" alt="icon"/>
                                    <% if(geocercasDto!=null){%>
                                    Editar Asignación de Geocerca ID <%=geocercasDto!=null?geocercasDto.getIdGeocerca():"" %>
                                    <%}else{%>
                                    Asignación de Geocerca
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idGeocerca" name="idGeocerca" value="<%=geocercasDto!=null?geocercasDto.getIdGeocerca():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>Empleado al que se le asignara la geocerca:</label><br/>
                                        <select size="1" id="idEmpleado" name="idEmpleado" >
                                            <option value="-1" >Selecciona un Receptor</option>
                                                <%
                                                    //out.print(new EmpleadoBO(user.getConn()).getEmpleadosByIdHTMLCombo(idEmpresa, (geocercasDto!=null?geocercaBO.getGeocerca().getIdEmpleadoReceptor():-1),"" ));
                                                out.print(new EmpleadoBO(user.getConn()).getEmpleadosByIdHTMLCombo(idEmpresa, idEmpleado,"" ));                                                
                                                %>
                                        </select>                                        
                                    </p>  
                                    <br/>
                                    <p>
                                        <label>Geocerca:</label><br/>
                                        <select size="1" id="idGeocercaSeleccionada" name="idGeocercaSeleccionada" >
                                            <option value="-1" >Selecciona una Geocerca</option>
                                                <%
                                                    //out.print(new EmpleadoBO(user.getConn()).getEmpleadosByIdHTMLCombo(idEmpresa, (geocercasDto!=null?geocercaBO.getGeocerca().getIdEmpleadoReceptor():-1),"" ));
                                                out.print(new GeocercaBO(user.getConn()).getGeocercasByIdHTMLCombo((int)idEmpresa, idGeocerca));
                                                %>
                                        </select>
                                    </p>
                                    <br/>   
                                    <p>
                                        <label>Hora de Inicio:</label><br/>
                                        <input maxlength="30" type="text" id="inicio_hora" name="inicio_hora" style="width:100px"
                                               class="time" value="" placeholder="hh:mm" readonly />                                                        
                                    </p>
                                    <br/>   
                                    <p>
                                        <label>Hora de Fin:</label><br/>
                                        <input maxlength="30" type="text" id="fin_hora" name="fin_hora" style="width:100px"
                                               class="time" value="" placeholder="hh:mm" readonly />                                                        
                                    </p>
                                    <br/> 
                                    
                                    <!--<p>
                                        <input type="checkbox" class="checkbox" <//%=geocercasDto!=null?(geocercasDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
                                    </p>-->
                                    <br/><br/>
                                    
                                    <% if (!mode.equals("3")) {%>
                                        <div id="action_buttons">
                                            <p>
                                                <input type="button" id="enviar" value="Enviar" onclick="grabar();"/>
                                                <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                            </p>
                                        </div>
                                    <%}else{
                                        //En caso de ser Formulario en modo PopUp
                                    %>
                                        <div id="action_buttons">
                                            <p>
                                                <input type="button" id="enviar" value="Enviar" onclick="grabar();"/>
                                                <input type="button" id="regresar" value="Cerrar" onclick="parent.$.fancybox.close();"/>
                                            </p>
                                        </div>
                                    <%}%>
                                    
                            </div>
                        </div>
                        <!-- End left column window -->
                        
                        
                    </div>
                    </form>
                    <!--TODO EL CONTENIDO VA AQUÍ-->

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>


    </body>
</html>
<%}%>