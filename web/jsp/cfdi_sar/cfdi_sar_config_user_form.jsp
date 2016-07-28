<%-- 
    Document   : cfdi_sar_config_user_form
    Created on : 18/03/2015, 01:52:18 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.dao.dto.SarUsuarioProveedor"%>
<%@page import="com.tsp.sct.bo.SarUsuarioProveedorBO"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
//Verifica si el cliente tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idSarUsuario = 0;
    try {
       idSarUsuario = Integer.parseInt(request.getParameter("id_sar_usuario"));
    } catch (NumberFormatException e) {}
       
    /*
    *   0/"" = nuevo
    *   1 = editar/consultar
    *   2 = eliminar  
    */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";

    SarUsuarioProveedorBO sarUsuarioProveedorBO = new SarUsuarioProveedorBO(user.getConn());
    SarUsuarioProveedor sarUsuarioProveedorDto = null;
    if (idSarUsuario > 0){
        sarUsuarioProveedorBO = new SarUsuarioProveedorBO(idSarUsuario,user.getConn());
        sarUsuarioProveedorDto = sarUsuarioProveedorBO.getSarUsuarioProveedor();
    }
    
    Empresa empresaDto = new Empresa();
    EmpresaBO empresaBO = new EmpresaBO(user.getConn());
    empresaDto = empresaBO.getEmpresaMatriz(idEmpresa);
    
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/keyWordSEO.jsp" />

        <title><jsp:include page="../include/titleApp.jsp" /></title>

        <jsp:include page="../include/skinCSS.jsp" />

        <jsp:include page="../include/jsFunctions.jsp"/>
        
        <!-- SCRIPT CHAT SOPORTE ZOPIM -->
        <script type="text/javascript" src="../../js/zopim/widgetZopimSAR.js"></script>
        
        <script type="text/javascript">
            
            function grabar(){
                if(validar()){
                    $.ajax({
                        type: "POST",
                        url: "cfdi_sar_config_user_ajax.jsp",
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
                                            location.href = "../cfdi_sar/cfdi_sar_config_user_list.jsp";
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
                if(jQuery.trim($("#sar_usuario").val())===""){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "Usuario SAR" es requerido</center>',{'animate':true});
                    $("#nombre_contacto").focus();
                    return false;
                }
                if(jQuery.trim($("#sar_pass").val())===""){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "Contrase&ntilde;a SAR" es requerido</center>',{'animate':true});
                    $("#nombre_contacto").focus();
                    return false;
                }
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
                    <h1>Configuración de Usuario SAR</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_sar_config.png" alt="icon"/>
                                    <% if(sarUsuarioProveedorDto!=null){%>
                                    Editar Configuración Usuario SAR ID <%=sarUsuarioProveedorDto!=null?sarUsuarioProveedorDto.getIdSarUsuario():"" %>
                                    <%}else{%>
                                    Nuevo Registro
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idEmpresaPadre" name="idEmpresaPadre" value="<%=empresaDto!=null?empresaDto.getIdEmpresa():"" %>" />
                                    <input type="hidden" id="id_sar_usuario" name="id_sar_usuario" value="<%=sarUsuarioProveedorDto!=null?sarUsuarioProveedorDto.getIdSarUsuario():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>Usuario SAR:</label><br/>
                                        <input maxlength="100" type="text" id="sar_usuario" name="sar_usuario" style="width:300px"
                                               value="<%=sarUsuarioProveedorDto!=null?sarUsuarioProveedorDto.getExtSarUsuario() : "" %>"
                                               <%=sarUsuarioProveedorDto!=null? "readonly" : "" %> />
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Contrase&ntilde;a SAR:</label><br/>
                                        <input maxlength="100" type="password" id="sar_pass" name="sar_pass" style="width:300px"
                                               value="<%=sarUsuarioProveedorDto!=null?sarUsuarioProveedorDto.getExtSarPass():"" %>"/>
                                    </p>
                                    <br/><br/>
                                    
                                    <div id="action_buttons">
                                        <p>
                                            <input type="button" id="enviar" value="Comprobar Datos y Guardar" onclick="grabar();"/>
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


    </body>
</html>
<%
}
%>