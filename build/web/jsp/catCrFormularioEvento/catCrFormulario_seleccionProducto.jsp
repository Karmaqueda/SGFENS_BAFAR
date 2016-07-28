<%-- 
    Document   : catCrFormulario_seleccionProducto
    Created on : 28/06/2016, 06:48:12 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.CrProductoCreditoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.CrProductoCreditoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrProductoCredito"%>
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
    int idCrProductoCredito = 0;
    try {
        idCrProductoCredito = Integer.parseInt(request.getParameter("idCrProductoCredito"));
    } catch (NumberFormatException e) {
    }

    /*
     *   0/"" = nuevo
     *   1 = editar/consultar
     *   2 = eliminar  
     */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";

    CrProductoCreditoBO crProductoCreditoBO = new CrProductoCreditoBO(user.getConn());
    CrProductoCredito crProductoCreditoDto = null;
    if (idCrProductoCredito > 0){
        crProductoCreditoBO = new CrProductoCreditoBO(idCrProductoCredito, user.getConn());
        crProductoCreditoDto = crProductoCreditoBO.getCrProductoCredito();
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
                id_producto_solicitado = $("#id_producto_solicitado").val();
                if(validar(id_producto_solicitado)){
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
                                            window.parent.location.href= "../../jsp/catCrFormularioCapturaInfo/catCrFormularioCapturaInfo_form.jsp?id_producto_solicitado="+id_producto_solicitado;
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

            function validar(id_producto_solicitado){
                if(id_producto_solicitado <= 0){
                    apprise('<center><img src=../../images/warning.png> <br/>Seleccione un Producto</center>',{'animate':true});
                    return false;
                }else{
                    return true;
                }
            }      
            
            function siguienteXs(){
                id_producto_solicitado = $("#id_producto_solicitado").val();
                if(id_producto_solicitado <= 0){
                    apprise('<center><img src=../../images/warning.png> <br/>Seleccione un Producto</center>',{'animate':true});
                    return false;
                }else{                    
                    //window.locationf="../../jsp/catCrFormularioCapturaInfo/catCrFormularioCapturaInfo_form.jsp";
                    //location.href="../../jsp/catCrFormularioCapturaInfo/catCrFormularioCapturaInfo_form.jsp";
                    window.parent.location.href="../../jsp/catCrFormularioCapturaInfo/catCrFormularioCapturaInfo_form.jsp?idGrupoFormulario="+id_producto_solicitado;
                }
            }
            
        </script>
    </head>
    <body>
        <div class="content_wrapper">


            <!-- Inicio de Contenido -->
            <div>

                <div class="nobg">
                    <h1>Producto</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="onecolumn">
                        <div >
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_crProductoCredito.png" alt="icon"/>
                                    Nuevo Prospecto
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />                                    
                                    <p>
                                        <label>*Producto:</label><br/>
                                        <select required= "required" id="id_producto_solicitado" name="id_producto_solicitado" style="width: 350px;">
                                            <option value="-1">Selecciona</option>
                                            <%= new CrProductoCreditoBO(user.getConn()).getCrProductoCreditosByIdHTMLCombo(idEmpresa, (crProductoCreditoDto!=null?crProductoCreditoDto.getIdProductoCredito(): -1), 0, 0, " AND id_producto_credito != id_producto_credito_padre AND id_producto_credito_padre IS NOT NULL ") %>
                                        </select>
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