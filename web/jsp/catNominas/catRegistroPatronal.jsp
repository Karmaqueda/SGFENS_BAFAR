<%-- 
    Document   : catRegistroPatronal
    Created on : 5/12/2013, 11:02:44 AM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.dao.dto.NominaRegistroPatronal"%>
<%@page import="com.tsp.sct.bo.NominaRegistroPatronalBO"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.dao.dto.SgfensClienteVendedor"%>
<%@page import="com.tsp.sct.bo.SGClienteVendedorBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.PasswordBO"%>
<%@page import="com.tsp.sct.bo.EmbalajeBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmbalajeDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Embalaje"%>
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
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        
        int idRegistroPatronal = 0;
        try{ idRegistroPatronal = Integer.parseInt(request.getParameter("idRegistroPatronal")); }catch(NumberFormatException e){}
        
        Empresa empresaDto = new Empresa();
        EmpresaBO empresaBO = new EmpresaBO(idEmpresa, user.getConn());
        empresaDto = empresaBO.getEmpresa();
        //empresaDto = empresaBO.getEmpresaMatriz(idEmpresa);
        
        NominaRegistroPatronalBO nominaRegistroPatronalBO = new NominaRegistroPatronalBO(user.getConn());
        NominaRegistroPatronal nomRegPatronalDto = null;
        if (idRegistroPatronal>0){
            nominaRegistroPatronalBO = new NominaRegistroPatronalBO(idRegistroPatronal, user.getConn());
            nomRegPatronalDto = nominaRegistroPatronalBO.getNominaRegistroPatronal();
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
            
            function grabar(){
                if(validar()){
                    $.ajax({
                        type: "POST",
                        url: "catRegistroPatronal_ajax.jsp",
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
                                            location.href = "catRegistroPatronal_list.jsp?pagina="+"<%=paginaActual%>";
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
                if(jQuery.trim($("#registroPatronal").val())===""){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "Registro Patronal" es requerido</center>',{'animate':true});
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
                    <h1>Registro Patronal</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_registroPatronal.png" alt="icon"/>
                                    <% if(nomRegPatronalDto!=null){%>
                                    Editar Registro Patronal ID <%= nomRegPatronalDto!=null?nomRegPatronalDto.getIdNominaRegPatronal() : "" %>
                                    <%}else{%>
                                    Nuevo Registro Patronal
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idRegistroPatronal" name="idRegistroPatronal" value="<%=nomRegPatronalDto!=null?nomRegPatronalDto.getIdNominaRegPatronal() : "" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>Empresa/Sucursal:</label><br/>
                                        <input maxlength="100" type="text" id="razonSocialPadre" name="razonSocialPadre" style="width:300px"
                                               value="<%=empresaDto!=null?empresaDto.getNombreComercial():"" %>" readonly/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Registro Patronal:</label><br/>
                                        <input maxlength="11" type="text" id="registroPatronal" name="registroPatronal" style="width:300px"
                                               value="<%=nomRegPatronalDto!=null?nomRegPatronalDto.getRegistroPatronal(): "" %>"/>
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


    </body>
</html>
<%}%>