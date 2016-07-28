<%-- 
    Document   : catCrFormularioEvento_cambioEstatus
    Created on : 12/07/2016, 01:21:53 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.dao.dto.CrFrmEventoSolicitud"%>
<%@page import="com.tsp.sct.dao.jdbc.CrFrmEventoSolicitudDaoImpl"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.CrEstadoSolicitudBO"%>
<%@page import="com.tsp.sct.dao.jdbc.CrEstadoSolicitudDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrEstadoSolicitud"%>
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
    int idCrEstadoSolicitud = 0;
    try {
        idCrEstadoSolicitud = Integer.parseInt(request.getParameter("idCrEstadoSolicitud"));
    } catch (NumberFormatException e) {}
    int idCrFormularioEvento = 0;
    try {
        idCrFormularioEvento = Integer.parseInt(request.getParameter("idCrFormularioEvento"));
    } catch (NumberFormatException e) {}

    /*
     *   0/"" = nuevo
     *   1 = editar/consultar
     *   2 = eliminar  
     */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";

    CrEstadoSolicitudBO crEstadoSolicitudBO = new CrEstadoSolicitudBO(user.getConn());
    CrEstadoSolicitud crEstadoSolicitudDto = null;
    
    CrFrmEventoSolicitud crFrmEventoSolicitudDto = null;
    CrFrmEventoSolicitudDaoImpl crFrmEventoSolicitudDaoImpl = new CrFrmEventoSolicitudDaoImpl(user.getConn());
    if (idCrEstadoSolicitud > 0){
        crEstadoSolicitudBO = new CrEstadoSolicitudBO(idCrEstadoSolicitud, user.getConn());
        crEstadoSolicitudDto = crEstadoSolicitudBO.getCrEstadoSolicitud();
        try{
            crFrmEventoSolicitudDto = crFrmEventoSolicitudDaoImpl.findWhereIdFormularioEventoEquals(idCrFormularioEvento)[0];
        }catch(Exception e){}
    }
    
    String urlTo = "../../jsp/catCrFormularioCapturaInfo/catCrFormularioCapturaInfo_form.jsp";
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
            
            function siguiente(){
                idEstatusSeleccionado = $("#idEstatusSeleccionado").val();
                if(validar(idEstatusSeleccionado)){
                    $.ajax({
                        type: "POST",
                        url: "catCrFormularioEvento_ajax.jsp",
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
                                            window.parent.location.href= "../../jsp/catCrFormularioEvento/catCrFormularioEvento_list.jsp";
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

            function validar(idEstatusSeleccionado){
                if(idEstatusSeleccionado <= 0){
                    apprise('<center><img src=../../images/warning.png> <br/>Seleccione un estatus</center>',{'animate':true});
                    return false;
                }
                comentarioEstatusSeleccionado = $("#comentarioEstatusSeleccionado").val();
                if(comentarioEstatusSeleccionado.trim() == ""){
                    apprise('<center><img src=../../images/warning.png> <br/>Capture un comentario</center>',{'animate':true});
                    return false;
                }
                return true;
            }      
            
            
        </script>
    </head>
    <body>
        <div class="content_wrapper">


            <!-- Inicio de Contenido -->
            <div>

                <div class="nobg">
                    <h1>Solicitud con ID <%=idCrFormularioEvento%></h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="onecolumn">
                        <div >
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_estatusSolicitud.png" alt="icon"/>
                                    Cambio de Estatus de la Solicitud
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    
                                    <input type="hidden" id="mode" name="mode" value="solicitudCambioEstatus" />                                    
                                    <input type="hidden" id="idCrFormularioEvento" name="idCrFormularioEvento" value="<%=idCrFormularioEvento%>" /> 
                                    <p>
                                        <label>*Estatus:</label><br/>
                                        <select required= "required" id="idEstatusSeleccionado" name="idEstatusSeleccionado" style="width: 350px;">
                                            <option value="-1">Selecciona</option>
                                            <%= new CrEstadoSolicitudBO(user.getConn()).getCrEstadoSolicitudsByIdHTMLCombo(0, (crFrmEventoSolicitudDto!=null?crFrmEventoSolicitudDto.getIdEstadoSolicitud(): -1), 0, 0, " AND id_estatus = 1 AND id_estado_solicitud NOT IN (7,8,10)") %>
                                        </select>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Comentario:</label><br/>
                                        <input maxlength="500" type="text" id="comentarioEstatusSeleccionado" name="comentarioEstatusSeleccionado" style="width:350px"
                                               value=""/>
                                    </p>
                                    
                                    
                                    <br/><br/>
                                    
                                    <div id="action_buttons">
                                        <p>
                                            <input type="button" id="enviar" value="siguiente" onclick="siguiente();"/>
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