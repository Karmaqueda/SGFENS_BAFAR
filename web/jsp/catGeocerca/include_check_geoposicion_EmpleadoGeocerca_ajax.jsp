<%-- 
    Document   : include_check_geoposicion_EmpleadoGeocerca_ajax
    Created on : 4/06/2013, 06:30:33 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.dao.jdbc.MovilMensajeDaoImpl"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.dao.dto.MovilMensaje"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>

<%@page import="com.tsp.sct.bo.MovilMensajeBO"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.bo.MovilMensajeBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false&libraries=geometry"></script>
        <script type="text/javascript">            
            //var sevilla = new google.maps.LatLng(37.377222, -5.986944);
            //var buenos_aires = new google.maps.LatLng(-34.608333, -58.371944);
            //var distancia = google.maps.geometry.spherical.computeDistanceBetween(new google.maps.LatLng(37.377222, -5.986944), new google.maps.LatLng(-34.608333, -58.371944));    
            var
                    latLngCenter = new google.maps.LatLng(19.36767, -99.18006),
                    latLngCMarker = new google.maps.LatLng(19.36767, -99.18006),
                    distancia = google.maps.geometry.spherical.computeDistanceBetween(new google.maps.LatLng(37.377222, -5.986944), new google.maps.LatLng(-34.608333, -58.371944));
            //var distancia = "425";
           // alert("saluidos");
            <%System.out.println("*************DISTANCIA: ");%>
        </script>
    </head>
    <body>
    </body>
</html>

<%
if (user == null || user.getUser() == null) {
    //Redirección por javascript (cliente)
    out.print("<script>document.location.href='../../jsp/inicio/login.jsp?action=loginRequired&urlSource="+request.getRequestURI()+"?"+request.getQueryString()+"'</script>");
    //Redirección por java (servidor) ***No Funciona correctamente
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource="+request.getRequestURI()+"?"+request.getQueryString());
    response.flushBuffer();
}else{
    try{
        String mensajeAMostrar="";
        
        boolean isFiltroComunicacionConsola = false;
        int filtroIdReceptor = 0;
        int filtroIdEmisor=0; //No filtramos por emisor, recibir de todos
        
        MovilMensajeBO movilMensajeBO = new MovilMensajeBO(user.getConn());
        //si tiene Rol administrador mostramos unicamente mensajes a consola
        if (user.getUser().getIdRoles()==RolesBO.ROL_ADMINISTRADOR_DE_SUCURSAL
                || user.getUser().getIdRoles()==RolesBO.ROL_ADMINISTRADOR){
            isFiltroComunicacionConsola = true;
            movilMensajeBO.setUserConsola(true);
        }
        
        Empleado empleado = new EmpleadoBO(user.getConn()).findEmpleadoByUsuario(user.getUser().getIdUsuarios());
        if (empleado!=null)
            filtroIdReceptor = (int)empleado.getIdEmpleado();
        
        MovilMensaje[] movilMensajeDtoLista = movilMensajeBO.getMovilMensajesByFilter(
                        true, //los que se han recibido
                        null, //filtro de fecha min
                        null,  //filtro de fecha max
                        true, //filtro para mostrar solo los que no se ha visto
                        isFiltroComunicacionConsola, //filtro solo mensajes para Consola
                        filtroIdReceptor, //filtroReceptor
                        filtroIdEmisor); //filtroEmisor
        
        for (MovilMensaje msg : movilMensajeDtoLista){
            mensajeAMostrar += "<ul>Nuevo mensaje: <b>" + msg.getMensaje() + "</b><i> el "+ DateManage.formatDateTimeToNormalMinutes(msg.getFechaEmision()) +"</i>";

            //Actualizamos registro a leido
            msg.setRecibido(1);
            msg.setFechaRecepcion(new Date());
            new MovilMensajeDaoImpl(user.getConn()).update(msg.createPk(), msg);
        }
        
        if (mensajeAMostrar.trim().length()>0){
%>
            <script>
                apprise('<center><b>Nuevos mensajes recibidos</b></center> <br/><%=mensajeAMostrar %>',{'animate':true});
            </script>    
<%
       }
    }catch(Exception ex){
%>
        <script>
            apprise('Error al intentar sincronizar modulo de mensajes: <%=ex.toString()%> ',{'animate':true});
        </script>
<%
        ex.printStackTrace();
    }
}
%>
<!--<script>
                apprise('<center><b>DISTANCIA</b></center> <br/>'+distancia,{'animate':true});
            </script>-->