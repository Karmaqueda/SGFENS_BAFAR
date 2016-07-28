<%-- 
    Document   : catHistorialMovil_logDescrpcion
    Created on : 6/01/2015, 10:10:48 AM
    Author     : 578
--%>

<%@page import="com.tsp.sct.dao.jdbc.HistorialMovilDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.HistorialMovil"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    int idHistorial = -1;
    try{ idHistorial = Integer.parseInt(request.getParameter("idHistorial")); }catch(NumberFormatException e){}
    
    
    HistorialMovilDaoImpl hMovilDao = new HistorialMovilDaoImpl();
    HistorialMovil hMovil = null;
    String descrip = "";
    if(idHistorial > 0){
       descrip = hMovilDao.findByPrimaryKey(idHistorial).getDescripcion();
    }    
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">       
    </head>
    <body style="background: #ffffff" > 
        <%=descrip%>
    </body>
</html>
<%}%>