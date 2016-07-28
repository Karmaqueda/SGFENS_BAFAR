<%-- 
    Document   : catComodatosContrato_form
    Created on : 7/03/2016, 06:56:10 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.bo.ImagenPersonalBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.dao.dto.Comodato"%>
<%@page import="com.tsp.sct.bo.ComodatoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el cliente tiene acceso a este topico
    if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
        response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
        response.flushBuffer();
    } else {
               
        /*
         * Parámetros
         */
        
        /*
         * Parámetros
         */
        int idComodato = 0;
        try {
            idComodato = Integer.parseInt(request.getParameter("idComodato"));
        } catch (NumberFormatException e) {
        }
        
        int idImagenPersonal = 0;
        try {
            idImagenPersonal = Integer.parseInt(request.getParameter("idImagenPersonal"));
        } catch (NumberFormatException e) {}
        
        ComodatoBO comodatoBO = new ComodatoBO(user.getConn());
        Comodato comodatosDto = null;
        Empresa empresaDto = null;
        if (idComodato > 0){
            comodatoBO = new ComodatoBO(idComodato,user.getConn());
            comodatosDto = comodatoBO.getComodato();
            empresaDto = new EmpresaBO(comodatosDto.getIdEmpresa(),user.getConn()).getEmpresa();
        }
        
        Configuration appConfig = new Configuration();

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        
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
            
            function grabarDocumento(){
                if(validar()){
                    $.ajax({
                        type: "POST",
                        url: "catComodatos_ajax.jsp",
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
                                            location.href = "catComodatos_list.jsp";
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
            
            function recuperarNombreArchivoImagen(nombreImagen){
                $('#nombreArchivoImagen').val('' + nombreImagen);
            }
                       
            /*function mostrarCalendario(){
                $( "#fecha_Pagos" ).datepicker({
                        minDate: 0,
                        gotoCurrent: true,
                        changeMonth: true,
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 100);
                            }, 500)}
                });
            }*/
            
        </script>
    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

            <div class="inner" style="width: 100%;">

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_comodatoContrato.png" alt="icon"/>                                    
                                    Contrato <%=comodatosDto!=null?" del comodato "+comodatosDto.getNombre():""%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idImagenPersonal" name="idImagenPersonal" value="" />
                                    <input type="hidden" id="mode" name="mode" value="contrato" />                                    
                                    <input type="hidden" id="idComodato" name="idComodato" value="<%=idComodato%>" />
                                    
                                    <%
                                        //Si se esta en modo para agregar un nuevo registro se muestra el upload
                                        if (mode.equals("") || mode.equals("contrato")){
                                    %>
                                    <p>
                                        <label>*Subir Archivo(.jpg)</label><br/>
                                        <iframe src="../file/uploadSingleForm.jsp?id=archivoImagen&validate=jpg&queryCustom=isImagenPDFContrato=1" 
                                                id="iframe_archivos_jpg" 
                                                name="iframe_archivos_jpg" 
                                                style="border: none" scrolling="no"
                                                height="80" width="400">
                                            <p>Tu navegador no soporta iframes y son necesarios para el buen funcionamiento del aplicativo</p>
                                        </iframe>
                                    </p>
                                    <p>
                                        <label>*Subir Archivo (.pdf)</label><br/>
                                        <iframe src="../file/uploadSingleForm.jsp?id=archivoImagen&validate=pdf&queryCustom=isImagenPDFContrato=1" 
                                                id="iframe_archivos_jpg" 
                                                name="iframe_archivos_jpg" 
                                                style="border: none" scrolling="no"
                                                height="80" width="400">
                                            <p>Tu navegador no soporta iframes y son necesarios para el buen funcionamiento del aplicativo</p>
                                        </iframe>
                                    </p>
                                    <% } %>
                                    
                                    <p>
                                        <label>*Nombre Archivo:</label><br/>
                                        <input maxlength="30" type="text" id="nombreArchivoImagen" name="nombreArchivoImagen" style="width:300px"
                                               readonly value="<%=comodatosDto!=null?(comodatoBO.getComodato().getContratoNombreArchivo()!=null?comodatoBO.getComodato().getContratoNombreArchivo():""):"" %>"/>
                                    </p>
                                    <br/>
                                    
                                    <% if (comodatosDto!=null && empresaDto!=null && comodatosDto.getContratoNombreArchivo()!=null && !comodatosDto.getContratoNombreArchivo().trim().equals("") ) {
                                        String rutaArchivoImagenPersonal = appConfig.getApp_content_path() + empresaDto.getRfc() + "/ContratoComodato/" + comodatosDto.getContratoNombreArchivo();
                                        String rutaArchivoImgEncoded = java.net.URLEncoder.encode(rutaArchivoImagenPersonal, "UTF-8");
                                    %>
                                    <p>
                                        <label>Archivo</label>
                                        <div style="display: inline" >
                                            <a href="../file/download.jsp?ruta_archivo=<%=rutaArchivoImgEncoded%>">Descargar</a>
                                        </div>
                                    </p>
                                    <%}%>
                                   
                                    <div id="action_buttons">
                                        <p>
                                            </% if (mode.equals("")){ %>
                                            <input type="button" id="enviar" value="Guardar" onclick="grabarDocumento();"/>
                                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                           </%}%>                                            
                                        </p
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

        <!--<script>
            mostrarCalendario();
        </script>-->
    </body>
</html>
<%}%>