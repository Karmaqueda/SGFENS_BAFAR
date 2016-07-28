<%-- 
    Document   : dashboard
    Created on : 24/04/2013, 06:26:35 PM
    Author     : Leonardo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user==null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
}else{
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
        
    </head>
    <body>
        <div class="content_wrapper">
            
            <jsp:include page="../include/header.jsp" flush="true"/>
            
            <jsp:include page="../include/leftContent.jsp"/>
            
            <!-- Inicio de Contenido -->
            <div id="content">
                
                <div class="inner">
                    <!--<h1>Panel de Inicio</h1>-->
                    
                    <jsp:include page="menu.jsp"/>
                    
                    <!-- Begin one column -->
                    <!--
                    <div class="onecolumn">
                        
                            <div class="header">
                                    <span>Redactar</span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <textarea id="wysiwyg"></textarea>
                            </div>
                         
                    </div>
                    -->
                    <!--
                    <div class="alert_info">
                        <br class="clear"/>
                        <h4>
                        Este sistema es 100% compatible solo con Google Chrome, 
                        puedes descargarlo <a href="https://www.google.com/chrome?hl=es" target="_blank">aquí</a>
                        </h4>
                        <br class="clear"/>
                    </div>
                    -->
                </div>
                
                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>


    </body>
</html>
<%}%>