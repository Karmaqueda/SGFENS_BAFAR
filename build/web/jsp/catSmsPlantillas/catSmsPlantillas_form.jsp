<%-- 
    Document   : catSmsPlantillas_list
    Created on : 10/03/2016, 12:55:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.dao.dto.SgfensClienteVendedor"%>
<%@page import="com.tsp.sct.bo.SGClienteVendedorBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.PasswordBO"%>
<%@page import="com.tsp.sct.bo.SmsPlantillaBO"%>
<%@page import="com.tsp.sct.dao.jdbc.SmsPlantillaDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SmsPlantilla"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el cliente tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {

    int paginaActual = 1;
    try{
        paginaActual = Integer.parseInt(request.getParameter("pagina"));
    }catch(Exception e){}

    int idEmpresa = user.getUser().getIdEmpresa();

    /*
     * Parámetros
     */
    int idSmsPlantilla = 0;
    try {
        idSmsPlantilla = Integer.parseInt(request.getParameter("idSmsPlantilla"));
    } catch (NumberFormatException e) {
    }

    /*
     *   0/"" = nuevo
     *   1 = editar/consultar
     *   2 = eliminar  
     */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";

    SmsPlantillaBO smsPlantillaBO = new SmsPlantillaBO(user.getConn());
    SmsPlantilla smsPlantillasDto = null;
    if (idSmsPlantilla > 0){
        smsPlantillaBO = new SmsPlantillaBO(idSmsPlantilla, user.getConn());
        smsPlantillasDto = smsPlantillaBO.getSmsPlantilla();
    }
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
            var limiteCaracteresSMS = 160;
            
            function grabar(){
                if(validar()){
                    $.ajax({
                        type: "POST",
                        url: "catSmsPlantillas_ajax.jsp",
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
                                            location.href = "catSmsPlantillas_list.jsp?pagina="+"<%=paginaActual%>";
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
            
            function blockEnter(event) {
                if (event.keyCode === 13) {
                    event.preventDefault();
                    return false;
                }
                return true;
            }
            
            function limpiarEnter(control){
                control.value = control.value.replace(/\r?\n/g, '');
            }
            
            function revisarCaracteresMensaje(control){
                var str = control.value;
                var res = str.match(/[ÁÉÍÓÚáéíóú!"·$%&'¡ºª|`^+*¨´{}ç;?¿()\/\[\]àèìòù]/g);
                if(res!==null){
                    $("#ajax_message").html('El texto contiene los siguientes caracteres que pueden afectar la entrega de tu mensaje: '+res);
                    $("#ajax_message").fadeIn("slow");
                    return true;
                }else{
                    $("#ajax_message").html('');
                    $("#ajax_message").fadeOut("slow");
                    return false;
                }
           }
           
            function cuentaCaracteres(control){
                /*
                var txt = control.value;
                var cnt = txt.length;	

                //veficamos si tiene simbolos especiales, estos ocupan 3 espacios y no solo 1
                var tempArray = txt.split("");
                for (x in tempArray) {
                    if (tempArray[x] == "`" || tempArray[x] == ";" || tempArray[x] == ":" || tempArray[x] == "@" || tempArray[x] == "\u0026" || tempArray[x] == "=" || tempArray[x] == "+" || tempArray[x] == "$" || tempArray[x] == "," || tempArray[x] == "/" || tempArray[x] == "?" || tempArray[x] == "%" || tempArray[x] == "#" || tempArray[x] == "[" || tempArray[x] == "]") {
                        cnt += 2; 
                    }
                }  
                
                var numeroMensajes = Math.ceil(cnt / limiteCaracteresSMS);//redondeo a entero arriba
                if (numeroMensajes<=0)
                    numeroMensajes = 1;
                
                var caracteresRestantesParteActual = (numeroMensajes * limiteCaracteresSMS) - cnt ;
                
                //if (caracteresRestantesParteActual<=0 && numeroMensajes===1)
                //    caracteresRestantesParteActual = limiteCaracteresSMS;
                
                $("#caracteres_restantes").html(''+caracteresRestantesParteActual);
                $("#partes").html(''+numeroMensajes);
                */
               
                var smsCount = SmsCounter.count(control.value);
                console.log(smsCount);
                console.log(smsCount['encoding']);
                console.log(smsCount['length']);
                console.log(smsCount['per_message']);
                $("#caracteres_restantes").html(''+smsCount['remaining']);
                $("#partes").html(''+smsCount['messages']);
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
                    <h1>SMS Masivos</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_smsPlantilla.png" alt="icon"/>
                                    <% if(smsPlantillasDto!=null){%>
                                    Editar Plantilla SMS ID <%=smsPlantillasDto!=null?smsPlantillasDto.getIdSmsPlantilla():"" %>
                                    <%}else{%>
                                    Nueva Plantilla SMS
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idSmsPlantilla" name="idSmsPlantilla" value="<%=smsPlantillasDto!=null?smsPlantillasDto.getIdSmsPlantilla():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>*Asunto:</label><br/>
                                        <input maxlength="50" type="text" id="asunto" name="asunto" style="width:360px"
                                               value="<%=smsPlantillasDto!=null?smsPlantillaBO.getSmsPlantilla().getAsunto():"" %>"/>
                                    </p>
                                    <br/>
                                    
                                    <div id="info_sms" class="alert_info">
                                    Algunos teléfonos no despliegan correctamente caracteres especiales (acentos y signos) o rechazan los mensajes que los contienen, te sugerimos utilizarlos lo menos posible para que tu mensaje no tenga problema.
                                    </div>
                                    <br/>
                                    <p>
                                        <label>*Contenido:</label><br/>
                                        <textarea id="contenido" name="contenido"
                                            cols="55" rows="6"
                                            onKeyPress="return blockEnter(event);"
                                            onKeyUp="cuentaCaracteres(this);revisarCaracteresMensaje(this); return textArea_maxLen(this,480);"
                                            onKeyDown="cuentaCaracteres(this);"
                                            onChange="limpiarEnter(this);"
                                            ><%=smsPlantillasDto!=null?smsPlantillaBO.getSmsPlantilla().getContenido():"" %></textarea>
                                        <span id="caracteres_restantes">160</span> / <span id="partes">1</span>
                                    </p>
                                    <br/>                                                                        
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=smsPlantillasDto!=null?(smsPlantillasDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
                                    </p>
                                    <br/><br/>
                                    
                                    <div id="action_buttons">
                                        <p>
                                            <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                        </p>
                                    </div>
                                    
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

        <script>
            $(document).ready(function() {
                cuentaCaracteres(document.getElementById("contenido"));
            });
        </script>
    </body>
</html>
<%}%>