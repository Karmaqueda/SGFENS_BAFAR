<%-- 
    Document   : interLoginQPay
    Created on : 20/01/2016, 05:08:39 AM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.util.UtilSecurity"%>
<%@page import="com.tsp.sct.dao.dto.SgfensBanco"%>
<%@page import="com.tsp.sct.bo.SGBancoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el cliente tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    /*
     *   0/"" = 
     *   1 = 
     *   2 =   
     */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        
    String usuarioQPay = user.getqPayData()!=null ? user.getqPayData().getUser() : "";// "gpuga@movilpyme.com";
    String passQPay = user.getqPayData()!=null ? user.getqPayData().getPass() : "";//"demo";
    String hashUsuario = "";
    //String numeroSesion = "";
    //String hashSesion ="";
    
    Configuration appConfig = new Configuration();
    String urlSingleSignOn = appConfig.getQpayWebSingleSignOnUrl();
    try{
        hashUsuario = UtilSecurity.getHashSHA256(usuarioQPay.toLowerCase());
    }catch(Exception ex){
       ex.printStackTrace();
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
            
            $(document).ready(function() {
                obtenerNumSesionQPay();
            });

            function obtenerNumSesionQPay(){
                if(validar()){
                    $.ajax({
                        type: "POST",
                        url: "interLoginQPayUtilAjax.jsp",
                        data: { mode:'obtenerNumSesionQPay', uQPay : '<%= usuarioQPay %>' },
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><center>Iniciando conexión segura a QPay...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               var data = $.trim(datos.replace('<!--EXITO-->',''));
                               //$("#ajax_message").html(datos);
                               $("#ajax_loading").fadeOut("slow");
                               //$("#ajax_message").fadeIn("slow");

                               calcLoginQPay(data);
                               
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
            
            function calcLoginQPay(ns){
                $.ajax({
                    type: "POST",
                    url: "interLoginQPayUtilAjax.jsp",
                    data: { mode:'hashSesionQPay', uQPay : '<%= usuarioQPay %>' , pQPay : '<%= passQPay %>', nSesion : ns },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Re-dirigiendo a QPay...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           var hashSesion = $.trim(datos.replace('<!--EXITO-->',''));
                           //$("#ajax_message").html(datos);
                           $("#ajax_loading").fadeOut("slow");
                           //$("#ajax_message").fadeIn("slow");
                           
                           //asignamos valor a campo hidden de form
                           $('#validate').val(hashSesion);
                           
                            $("#action_buttons").fadeIn("slow");
                            
                            //enviamos form
                            $('#frm_action').submit();
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
            
        </script>
    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Conexión Segura a QPay</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="<%= urlSingleSignOn %>" method="post" id="frm_action" name="frm_action"
                          target="_blank">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_banco.png" alt="icon"/>
                                    Conectar a consola QPay
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="pIdentificador" name="pIdentificador" value="<%= hashUsuario %>" />
                                    <input type="hidden" id="validate" name="validate" value="" />
                                    
                                    <div id="ajax_loading" class="alert_info" style="">
                                        Permita siempre las ventanas emergentes (pop-up)
                                        <br/>
                                        de este sitio para poder acceder a QPay.
                                    </div>
                                    
                                    <br/>
                                    
                                    <div id="action_buttons" style="display: none;">
                                        <p>
                                            <!--<input type="submit" value="Ir a QPay"/>-->
                                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                            <input type="button" id="reintentar" value="Intentar de Nuevo" onclick="location.reload();"/>
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


    </body>    
</html>
<%}%>