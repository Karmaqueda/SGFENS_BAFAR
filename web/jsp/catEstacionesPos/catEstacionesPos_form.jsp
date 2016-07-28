<%-- 
    Document   : catPosEstacions_form
    Created on : 23/06/2015, 11:32:52 AM
    Author     : Cesar Martinez
--%>

<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.dao.dto.SgfensClienteVendedor"%>
<%@page import="com.tsp.sct.bo.SGClienteVendedorBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.PosEstacionBO"%>
<%@page import="com.tsp.sct.dao.jdbc.PosEstacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.PosEstacion"%>
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
    int idPosEstacion = 0;
    try {
        idPosEstacion = Integer.parseInt(request.getParameter("idPosEstacion"));
    } catch (NumberFormatException e) {
    }

    /*
     *   0/"" = nuevo
     *   1 = editar/consultar
     *   2 = eliminar  
     */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";

    PosEstacionBO posEstacionBO = new PosEstacionBO(user.getConn());
    PosEstacion posEstacionsDto = null;
    if (idPosEstacion > 0){
        posEstacionBO = new PosEstacionBO(idPosEstacion,user.getConn());
        posEstacionsDto = posEstacionBO.getPosEstacion();
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
                        url: "catEstacionesPos_ajax.jsp",
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
                                        location.href = "catEstacionesPos_list.jsp?pagina="+"<%=paginaActual%>";
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
                if(jQuery.trim($("#identificacion").val())===""){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "identificacion" es requerido</center>',{'animate':true});
                    $("#identificacion").focus();
                    return false;
                }
                if (/\s/.test($("#identificacion").val())) {
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "identificacion" no debe llevar espacios en blanco</center>',{'animate':true});
                    $("#identificacion").focus();
                    return false;
                }
                if(jQuery.trim($("#nombre").val())===""){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "nombre" es requerido</center>',{'animate':true});
                    $("#nombre").focus();
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
                    <h1>Estaci&oacute;n Punto de Venta</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_pos_estacion_3.png" alt="icon"/>
                                    <% if(posEstacionsDto!=null){%>
                                    Editar Estacion Punto de Venta ID <%=posEstacionsDto!=null?posEstacionsDto.getIdPosEstacion():"" %>
                                    <%}else{%>
                                    Nueva Estación Punto de Venta
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idPosEstacion" name="idPosEstacion" value="<%=posEstacionsDto!=null?posEstacionsDto.getIdPosEstacion():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>*Identificación:</label><br/>
                                        <input maxlength="15" type="text" id="identificacion" name="identificacion" style="width:300px"
                                               value="<%=posEstacionsDto!=null?StringManage.getValidString(posEstacionsDto.getIdentificacion()):"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Nombre/Descripción:</label><br/>
                                        <input maxlength="50" type="text" id="nombre" name="nombre" style="width:300px"
                                               value="<%=posEstacionsDto!=null?StringManage.getValidString(posEstacionsDto.getNombre()):"" %>"/>
                                    </p>
                                    <br/>                                                                      
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=posEstacionsDto!=null?(posEstacionsDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
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
                        
                        <div class="column_right">
                            <div class="header">
                                <span>
                                    Información Sincronización
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <p>
                                        <label>Folio Externo:</label><br/>
                                        <input maxlength="30" type="text" id="folio_movil" name="folio_movil" style="width:300px"
                                               value="<%=posEstacionsDto!=null?StringManage.getValidString(posEstacionsDto.getFolioMovil()):"" %>"
                                               readonly/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Fecha y Hr Ultima conexión:</label><br/>
                                        <input maxlength="15" type="text" id="fecha_hr" name="fecha_hr" style="width:300px"
                                               value="<%=posEstacionsDto!=null?StringManage.getValidString(DateManage.formatDateTimeToNormalMinutes(posEstacionsDto.getFechaHrUltimaConexion())):"" %>"
                                               readonly/>
                                    </p>
                                    <br/>
                                    
                            </div>
                        </div>
                        <!-- End right column window -->
                        
                        
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