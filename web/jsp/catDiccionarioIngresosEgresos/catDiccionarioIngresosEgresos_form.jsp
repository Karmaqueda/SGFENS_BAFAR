<%-- 
    Document   : catDiccionarioIngresosEgresos_form
    Created on : 24/06/2015, 11:32:52 AM
    Author     : Cesar Martinez
--%>

<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.dao.dto.SgfensClienteVendedor"%>
<%@page import="com.tsp.sct.bo.DiccionarioIngresosEgresosBO"%>
<%@page import="com.tsp.sct.dao.jdbc.DiccionarioIngresosEgresosDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.DiccionarioIngresosEgresos"%>
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
    int idDiccionarioIngresosEgresos = 0;
    try {
        idDiccionarioIngresosEgresos = Integer.parseInt(request.getParameter("idDiccionarioIngresosEgresos"));
    } catch (NumberFormatException e) {
    }

    /*
     *   0/"" = nuevo
     *   1 = editar/consultar
     *   2 = eliminar  
     */
    String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";

    DiccionarioIngresosEgresosBO diccionarioIngresosEgresosBO = new DiccionarioIngresosEgresosBO(user.getConn());
    DiccionarioIngresosEgresos diccionarioIngresosEgresossDto = null;
    if (idDiccionarioIngresosEgresos > 0){
        diccionarioIngresosEgresosBO = new DiccionarioIngresosEgresosBO(idDiccionarioIngresosEgresos,user.getConn());
        diccionarioIngresosEgresossDto = diccionarioIngresosEgresosBO.getDiccionarioIngresosEgresos();
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
                        url: "catDiccionarioIngresosEgresos_ajax.jsp",
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
                                        location.href = "catDiccionarioIngresosEgresos_list.jsp?pagina="+"<%=paginaActual%>";
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
                    <h1>Diccionario Ingresos y Egresos</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_diccionario_ie.png" alt="icon"/>
                                    <% if(diccionarioIngresosEgresossDto!=null){%>
                                    Editar Registro de Diccionario ID <%=diccionarioIngresosEgresossDto!=null?diccionarioIngresosEgresossDto.getIdDiccionarioIE():"" %>
                                    <%}else{%>
                                    Nuevo Registro de Diccionario
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idDiccionarioIngresosEgresos" name="idDiccionarioIngresosEgresos" value="<%=diccionarioIngresosEgresossDto!=null?diccionarioIngresosEgresossDto.getIdDiccionarioIE():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>*Nombre:</label><br/>
                                        <input maxlength="50" type="text" id="nombre" name="nombre" style="width:300px"
                                               value="<%=diccionarioIngresosEgresossDto!=null?StringManage.getValidString(diccionarioIngresosEgresossDto.getNombre()):"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Descripción:</label><br/>
                                        <input maxlength="250" type="text" id="descripcion" name="descripcion" style="width:300px"
                                               value="<%=diccionarioIngresosEgresossDto!=null?StringManage.getValidString(diccionarioIngresosEgresossDto.getDescripcion()):"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <input id="es_ingreso" name="es_ingreso" type="checkbox" class="checkbox" <%=diccionarioIngresosEgresossDto!=null?(diccionarioIngresosEgresossDto.getEsIngreso()==1?"checked":""):"checked" %> value="1">
                                        <label for="estatus">Es Ingreso</label>
                                    </p>
                                    <br/>
                                    <% if (user.getUser().getIdRoles()==RolesBO.ROL_DESARROLLO){ %>
                                    <p>
                                        <input id="es_general" name="es_general" type="checkbox" class="checkbox" <%=diccionarioIngresosEgresossDto!=null?(diccionarioIngresosEgresossDto.getEsGeneral()==1?"checked":""):"" %> value="1">
                                        <label for="estatus">Es General</label>
                                    </p>
                                    <br/>
                                    <% } %>
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=diccionarioIngresosEgresossDto!=null?(diccionarioIngresosEgresossDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
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
                                               value="<%=diccionarioIngresosEgresossDto!=null?StringManage.getValidString(diccionarioIngresosEgresossDto.getFolioMovil()):"" %>"
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