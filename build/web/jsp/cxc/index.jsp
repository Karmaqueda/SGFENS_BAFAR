<%--
    Document   : index
    Created on : 23/04/2015, 11:41:27 PM
    Author     : ISC Cesar Martinez poseidon24@hotmail.com
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
                    
                    <!--<//jsp:include page="../include/menu.jsp"/>-->
                    <jsp:include page="../Dashboard/DashboardFinanzas.jsp"/>
                    
                </div>
                
                <jsp:include page="../include/footer.jsp"/>
            </div>
        </div>


    </body>
</html>
<%}%>