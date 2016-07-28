<%--
    Document   : main
    Created on : 16/08/2011, 11:41:27 PM
    Author     : ISC Cesar Martinez poseidon24@hotmail.com
--%>

<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.util.GenericMethods"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.bo.PretoLicenciaBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user==null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
}else{
    EmpresaBO empresaBO = new EmpresaBO(user.getConn());
    EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());     
    
    String verificadorSesionGuiaCerrada = "0";
    try{
        if(session.getAttribute("sesionCerrada")!= null){
            verificadorSesionGuiaCerrada = (String)session.getAttribute("sesionCerrada");
        }
    }catch(Exception e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
    ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
    int cantidadConceptosSinNombreDesencriptado = conceptoBO.findCantidadConceptos(-1, idEmpresa, 0, 0, " AND NOMBRE!='' AND (NOMBRE_DESENCRIPTADO IS NULL OR NOMBRE_DESENCRIPTADO = '')");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/keyWordSEO.jsp" />

        <title><jsp:include page="../../jsp/include/titleApp.jsp" /></title>
        
        <jsp:include page="../../jsp/include/skinCSS.jsp" />

        <jsp:include page="../../jsp/include/jsFunctions.jsp"/>
        
        
        <script type="text/javascript">
            
            function desencriptaNombreConceptosRestantes(){
                $.ajax({
                    type: "POST",
                    url: "../catConceptos/catConceptos_ajax.jsp",
                    data: { mode: 'desencriptarConceptos', todos : 'false' },
                    beforeSend: function(objeto){
                        $("#overlay").fadeIn("slow");
                    },
                    success: function(datos){
                        if(datos.indexOf("--EXITO-->", 0)>0){
                           $("#overlay_popup").html(datos);
                           location.reload();
                       }else{
                           $("#overlay").fadeOut("slow");
                           $("#overlay_popup").html(datos);
                           $("#ajax_loading").html(datos);
                       }
                    }
                });
            }
            
        </script>
        
    </head>
    <body>
        <%if( empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0") ){%>
        <a href="http://movilpyme.com/evc_semi/evc_semi.html?reload" id="guiaInteractiva" name="guiaInteractiva" title="Mostrar guia interactiva" class="modalbox_guiaInteractiva">
            
        </a>
        <%}%>
        <div class="content_wrapper">
            
            <jsp:include page="../include/header.jsp" flush="true"/>
            
            <jsp:include page="../include/leftContent.jsp"/>
            
            <!-- Inicio de Contenido -->
            <div id="content">
                
                <div class="inner">
                    <!--<h1>Panel de Inicio</h1>-->
                    
                    <% if (cantidadConceptosSinNombreDesencriptado>0) { %>
                    
                        <div id="overlay" style="display: block;"></div>
                        <div id="ajax_loading" class="alert_info overlay_popup" style="z-index: 99999; padding: 30px;">
                            <h1>
                            Espere...<br/>
                            <img src="../../images/ajax_loader.gif" /> <br/>
                            Estamos actualizando información de los productos en su cuenta.
                            </h1>
                        </div>
                        <script>
                            desencriptaNombreConceptosRestantes();
                        </script>
                    
                    <% }else{ %>                    
                            <% if (GenericMethods.datoEnColeccion(user.getUser().getIdRoles(), new Integer[]{RolesBO.ROL_ADMINISTRADOR, RolesBO.ROL_GERENTE, RolesBO.ROL_MESA_DE_CONTROL, RolesBO.ROL_VERIFICADOR, RolesBO.ROL_PROMOTOR}) ) { %>                   
                            <jsp:include page="../Dashboard_Indicadores/Dashboard.jsp" flush="true"/>
                            <% } else if (GenericMethods.datoEnColeccion(user.getUser().getIdRoles(), new Integer[]{RolesBO.ROL_ADMINISTRADOR, RolesBO.ROL_GESTOR}) ) { %>
                             <!-- Dashboard para Gestor de Credito -->
                            <% } %>
                    <% } %>
                    
                </div>
                
                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>
    </body>
    <!--<%if( empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0") ){%>    
    <script type="text/javascript">            
            function guiaInteractiva(){
                $("#guiaInteractiva").click();
            }            
        setTimeout("guiaInteractiva()",1000);
     </script>
    <%}%>-->
</html>
<%}%>