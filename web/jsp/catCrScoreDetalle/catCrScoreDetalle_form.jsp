<%-- 
    Document   : catCrScoreDetalle_list
    Created on : 20/06/2016, 12:55:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.bo.CrFormularioCampoBO"%>
<%@page import="com.tsp.sct.dao.dto.CrFormularioCampo"%>
<%@page import="com.tsp.sct.bo.CrFormularioBO"%>
<%@page import="com.tsp.sct.bo.CrScoreBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.CrScoreDetalleBO"%>
<%@page import="com.tsp.sct.dao.jdbc.CrScoreDetalleDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrScoreDetalle"%>
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
    int idCrScoreDetalle = 0;
    try {
        idCrScoreDetalle = Integer.parseInt(request.getParameter("idCrScoreDetalle"));
    } catch (NumberFormatException e) {
    }
    
    int idCrScore = -1;
    try{ idCrScore = Integer.parseInt(request.getParameter("idCrScore")); }catch(NumberFormatException e){}

    /*
     *   0/"" = nuevo
     *   1 = editar/consultar
     *   2 = eliminar  
     */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "0";

    CrScoreDetalleBO crScoreDetalleBO = new CrScoreDetalleBO(user.getConn());
    CrScoreDetalle crScoreDetalleDto = null;
    CrFormularioCampo crFormularioCampo = null;
    if (idCrScoreDetalle > 0){
        crScoreDetalleBO = new CrScoreDetalleBO(idCrScoreDetalle, user.getConn());
        crScoreDetalleDto = crScoreDetalleBO.getCrScoreDetalle();
        crFormularioCampo = new CrFormularioCampoBO(crScoreDetalleDto.getIdFormularioCampo(), user.getConn()).getCrFormularioCampo();
        idCrScore = crScoreDetalleDto.getIdScore();
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

        <jsp:include page="../include/jsFunctions2.jsp"/>
        
        <script type="text/javascript">
            
            function grabar(){
                if(validar()){
                    $.ajax({
                        type: "POST",
                        url: "catCrScoreDetalle_ajax.jsp",
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
                                            location.href = "catCrScoreDetalle_list.jsp?pagina=<%=paginaActual%>&idCrScore=<%= idCrScore %>";
                                        });
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               $("#action_buttons").fadeIn("slow");
                               $.scrollTo('.inner',800);
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
            
            function cargaCamposFormulario(idFormulario){
                $.ajax({
                    type: "POST",
                    url: "catCrScoreDetalle_ajax.jsp",
                    data: { mode: '3', id_formulario : idFormulario },
                    beforeSend: function(objeto){
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                        $("#p_formulario_campo").fadeOut("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#ajax_loading").fadeOut("slow");
                           //vaciamos opciones de Select
                            $('#id_formulario_campo').empty();
                            //llenamos con nuevas opciones
                            $('#id_formulario_campo').append(datos);
                            // mostramos contenedor de campo
                            $("#p_formulario_campo").fadeIn("slow");
                            $("#action_buttons").fadeIn("slow");
                       }else{
                           $("#p_formulario_campo").fadeOut("slow");
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").html(datos);
                           $("#ajax_message").fadeIn("slow");
                           $("#action_buttons").fadeIn("slow");
                           $.scrollTo('.inner',800);
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
                    <h1>Score</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_crScore.png" alt="icon"/>
                                    <% if(crScoreDetalleDto!=null){%>
                                    Editar Score Detalle ID <%=crScoreDetalleDto!=null?crScoreDetalleDto.getIdScoreDetalle():"" %>
                                    <%}else{%>
                                    Nuevo Score Detalle
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idCrScoreDetalle" name="idCrScoreDetalle" value="<%=crScoreDetalleDto!=null?crScoreDetalleDto.getIdScoreDetalle():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    
                                    <p>
                                        <label>Score:</label><br/>
                                        <select id="id_score" name="id_score" style="width: 350px;">
                                            <option value="-1"></option>
                                            <%= new CrScoreBO(user.getConn()).getCrScoresByIdHTMLCombo(idEmpresa, idCrScore, 0, 0, (idCrScore>0?" AND id_score="+idCrScore:"") ) %>
                                        </select>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Buscar Campo en Formulario:</label><br/>
                                        <select id="id_formulario" name="id_formulario" style="width: 350px;" onchange="cargaCamposFormulario(this.value);">
                                            <option value="-1"></option>
                                            <%= new CrFormularioBO(user.getConn()).getCrFormulariosByIdHTMLCombo(idEmpresa, (crFormularioCampo!=null?crFormularioCampo.getIdFormulario():-1), 0, 0, "") %>
                                        </select>
                                    </p>
                                    <br/>
                                    <p id="p_formulario_campo" style="display: <%=crFormularioCampo!=null?"block":"none"%>;">
                                        <label>Campo a comparar:</label><br/>
                                        <select id="id_formulario_campo" name="id_formulario_campo" style="width: 350px;">
                                            <option value="-1"></option>
                                            <%= new CrFormularioCampoBO(user.getConn()).getCrFormularioCamposByIdHTMLCombo(idEmpresa, (crScoreDetalleDto!=null?crScoreDetalleDto.getIdFormularioCampo():-1), 0, 0, "") %>
                                        </select>
                                    </p>
                                    <br/>
                                    
                                    <fieldset style="border: 2px groove; padding: 1em;">
                                        <legend>Criterio</legend>
                                        <p>
                                            <label>Igual a:</label><br/>
                                            <input maxlength="500" type="text" id="valor_exacto" name="valor_exacto" style="width:350px"
                                                   value="<%=crScoreDetalleDto!=null?crScoreDetalleDto.getValorExacto(): "" %>"/>
                                        </p>
                                        <h2>ó</h2>
                                        <p>
                                            <label>Rango - Mínimo (Mayor-Igual que):</label><br/>
                                            <input maxlength="5" type="number" id="rango_min" name="rango_min" style="width:50px"
                                                   min="0" max="99999"
                                                   value="<%=crScoreDetalleDto!=null?crScoreDetalleDto.getRangoMin(): "" %>"/>
                                        </p>
                                        <br/>
                                        <p>
                                            <label>Rango - Máximo (Menor-Igual que):</label><br/>
                                            <input maxlength="5" type="number" id="rango_max" name="rango_max" style="width:50px"
                                                   min="0" max="99999"
                                                   value="<%=crScoreDetalleDto!=null?crScoreDetalleDto.getRangoMax(): "" %>"/>
                                        </p>
                                    </fieldset>
                                    <br/>
                                    <p>
                                        <label>Puntos Score (+/-):</label><br/>
                                        <input maxlength="3" type="number" id="puntos_score" name="puntos_score" style="width:50px"
                                               min="-500" max="500"
                                               value="<%=crScoreDetalleDto!=null?crScoreDetalleDto.getPuntosScore(): "" %>"/>
                                    </p>
                                    <br/>                                                               
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=crScoreDetalleDto!=null?(crScoreDetalleDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
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
            $("select.flexselect").flexselect();
        </script>
    </body>
</html>
<%}%>