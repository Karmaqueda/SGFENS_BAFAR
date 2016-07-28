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
        *   Registramos navegaci�n en la bit�cora de acciones
        */
        SGAccionBitacoraBO accionBitacoraBO = new SGAccionBitacoraBO(user.getConn());
        accionBitacoraBO.insertAccionNavegacion(user.getUser().getIdUsuarios(), "Navegaci�n hacia: " + request.getRequestURL()+"?"+request.getQueryString(), request.getRequestURI().replace(request.getContextPath(),""));
        
        System.out.println("Usuario " + user.getUser().getUserName());
        System.out.println("Navegaci�n hacia: " + request.getRequestURL()+"?"+request.getQueryString());
    }
}catch(Exception ex){
    System.out.println("Error al intentar registro de navegaci�n en la bit�cora del sistema. Error: " + ex.getMessage());
}
%>
