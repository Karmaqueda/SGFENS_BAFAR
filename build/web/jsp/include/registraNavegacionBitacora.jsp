<%-- 
    Document   : registraNavegacionBitacora
    Created on : 25/10/2011, 12:35:49 PM
    Author     : ISC Cesar Martinez poseidon24@hotmail.com
--%>
<%@page import="com.tsp.sct.bo.SGAccionBitacoraBO"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
try{
    if (user != null || user.getUser() != null) {
        /*
        *   Registramos navegación en la bitácora de acciones
        */
        SGAccionBitacoraBO accionBitacoraBO = new SGAccionBitacoraBO(user.getConn());
        accionBitacoraBO.insertAccionNavegacion(user.getUser().getIdUsuarios(), "Navegación hacia: " + request.getRequestURL()+"?"+request.getQueryString(), request.getRequestURI().replace(request.getContextPath(),""));
        
        System.out.println("Usuario " + user.getUser().getUserName());
        System.out.println("Navegación hacia: " + request.getRequestURL()+"?"+request.getQueryString());
    }
}catch(Exception ex){
    System.out.println("Error al intentar registro de navegación en la bitácora del sistema. Error: " + ex.getMessage());
}
%>
