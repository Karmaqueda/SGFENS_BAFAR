<%-- 
    Document   : catSmsAgendaCargaMasiva_form
    Created on : 14/03/2016, 05:13:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.bo.SmsAgendaGrupoBO"%>
<%@page import="com.tsp.sct.config.Configuration"%>
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
        <link rel="shortcut icon" href="../../images/favicon.ico">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/keyWordSEO.jsp" />

        <title><jsp:include page="../include/titleApp.jsp" /></title>

        <jsp:include page="../include/skinCSS.jsp" />

        <jsp:include page="../include/jsFunctions.jsp"/>
        
        <script type="text/javascript">
            
            function grabar(){                
                $.ajax({
                    type: "POST",
                    url: "catSmsAgendaCargaMasiva_ajax.jsp",
                    data: $("#frm_action").serialize(),
                    beforeSend: function(objeto){
                        $("#ajax_message").fadeOut("slow");
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           //$("#ajax_message").html(datos);
                           //$("#ajax_loading").fadeOut("slow");
                           //$("#ajax_message").fadeIn("slow");
                           apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                    function(r){
                                        location.href = "catSmsAgendaDestinatarios_list.jsp";
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

            function recuperarNombreArchivoXLS(nombreArchivo){
                $('#nombreArchivo').val('' + nombreArchivo);
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
                                    <img src="../../images/icon_excel.png" alt="icon"/>
                                    Destinatarios
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">                                    
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />                                                                                                       
                                    
                                    <fieldset style="border: 2px groove; padding-left: 1em; padding-right: 0.75em;">
                                        <legend>Manual</legend>
                                        <div id="info_2" class="alert_info">
                                            Ej. 4491073763,David,generico 1,generico 2,generico 3,generico 4; 4457667777,Omar,generico 1,generico 2,generico 3,generico 4; o sin nombres 4491073763; 4465442343; 44987773662;
                                        </div>
                                        <p>
                                            <label>*Agregar Destinatarios:</label><br/>
                                            <textarea id="destinatarios_manual" name="destinatarios_manual"
                                                cols="55" rows="4"
                                                onKeyPress="return blockEnter(event);"
                                                onKeyUp="return textArea_maxLen(this,2000);"
                                                onChange="limpiarEnter(this);"
                                                ></textarea>
                                        </p>
                                        <br/>
                                        
                                    </fieldset>
                                    <br/>
                                    <fieldset style="border: 2px groove; padding-left: 1em; padding-right: 0.75em;">
                                        <legend>Layout Excel&reg;</legend>
                                        
                                        <div id="info_2" class="alert_info">
                                        Debe ser un archivo generado en Microsoft Excel (.xls) con la siguiente estructura: teléfono, nombre, campo1, campo2, campo3, campo4. Cada registro en un renglón.
                                        <br/>
                                        Los renglones deberán tener siempre el campo TELEFONO a 10 dígitos y sin caracteres raros (!”#$%&/()=?¡)
                                        <br/>
                                        NOTA: Deben de ser bloques menores o igual a la cantidad de 10,000 registros.
                                        </div>
                                        <%
                                            //Si se esta en modo para agregar un nuevo registro se muestra el upload
                                            if (mode.equals("")){
                                        %>
                                        <p>
                                            <label>Subir Archivo (.xls)</label><br/>
                                            <iframe src="../file/uploadSingleForm.jsp?id=archivoLayout&validate=xls,XLS&queryCustom=isXlsSmsDestinatarios=1" 
                                                    id="iframe_archivos_jpg" 
                                                    name="iframe_archivos_jpg" 
                                                    style="border: none" scrolling="no"
                                                    height="80" width="400">
                                                <p>Tu navegador no soporta iframes y son necesarios para el buen funcionamiento del aplicativo</p>
                                            </iframe>
                                        </p>
                                        <% } %>

                                        <p>
                                            <label>Nombre Archivo:</label><br/>
                                            <input maxlength="30" type="text" id="nombreArchivo" name="nombreArchivo" style="width:300px"
                                                   readonly />
                                        </p>                                   
                                        <p>                                                                                                          
                                            <img src="../../images/icon_excel.png"/>
                                            <a href="../recursos/Layout_SMS_Destinatarios.xls">Descargar Plantilla SMS Destinatarios</a>
                                        <p>
                                    </fieldset>
                                    <br/>
                                    <p>
                                        <label>Grupo:</label><br/>
                                        <select size="1" id="id_sms_agenda_grupo" name="id_sms_agenda_grupo" style="width:350px" 
                                                class="flexselect">
                                            <option value="-1"></option>
                                            <%
                                                out.print(new SmsAgendaGrupoBO(user.getConn()).getSmsAgendaGruposByIdHTMLCombo(idEmpresa, -1, 0,0, ""));
                                            %>
                                        </select>
                                    </p>
                                    <br/>
                                    
                                    <div id="action_buttons">
                                        <p>
                                            <% if (mode.equals("")){ %>
                                            <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                                           <%}%>
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
            $("select.flexselect").flexselect();
        </script>
    </body>
</html>
<%}%>