<%-- 
    Document   : menu
    Created on : 24/04/2013, 06:30:13 PM
    Author     : Leonardo
--%>

<!-- ---------- <//%@page import="com.tsp.microfinancieras.bo.RolesBO"%> -->
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<!-- Begin shortcut menu -->
<ul id="shortcut">
   
</ul>
<!-- End shortcut menu -->

<!-- Begin shortcut notification -->
<div id="shortcut_notifications">
    <!--<span class="notification" rel="shortcut_home">1</span>-->
   <% if (user.requirePasswordChange()) {%>
    <span class="notification" rel="shortcut_perfil">!</span>
   <% } %>
</div>
<!-- End shortcut noficaton -->

<div class="inner" style="position: relative;">
    <h1>Localización de Empresa/Sucursal</h1>     
    <jsp:include page="Mapa.jsp"/>                    
</div>

<br class="clear"/>