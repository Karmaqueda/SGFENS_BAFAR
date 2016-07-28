<%-- 
    Document   : include_check_messages_ajax
    Created on : 14-feb-2013, 18:28:13
    Author     : ISCesarMartinez
--%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoDaoImpl"%>
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
        if (user.getUser().getIdRoles()==RolesBO.ROL_ADMINISTRADOR ||  user.getUser().getIdRoles()==RolesBO.ROL_DESARROLLO){
            isFiltroComunicacionConsola = true;
            movilMensajeBO.setUserConsola(true);
        }
        
        Empleado empleado = new EmpleadoBO(user.getConn()).findEmpleadoByUsuario(user.getUser().getIdUsuarios());
        if (empleado!=null)
            filtroIdReceptor = (int)empleado.getIdEmpleado();
        
        filtroIdEmisor = user.getUser().getIdEmpresa();
        MovilMensaje[] movilMensajeDtoLista = movilMensajeBO.getMovilMensajesByFilter(
                        true, //los que se han recibido
                        null, //filtro de fecha min
                        null,  //filtro de fecha max
                        true, //filtro para mostrar solo los que no se ha visto
                        isFiltroComunicacionConsola, //filtro solo mensajes para Consola
                        filtroIdReceptor, //filtroReceptor
                        filtroIdEmisor); //filtroEmisor
        
        Empleado emple = null;
        EmpleadoDaoImpl empleadoDaoImpl = new EmpleadoDaoImpl(user.getConn());
        for (MovilMensaje msg : movilMensajeDtoLista){
            try{
            emple = empleadoDaoImpl.findByPrimaryKey(msg.getIdEmpleadoEmisor());}catch(Exception e){}
            if(emple != null){
                mensajeAMostrar += "<ul>Nuevo mensaje de " + (emple.getNombre()!=null?emple.getNombre().trim():"") + " " + (emple.getApellidoPaterno()!=null?emple.getApellidoPaterno().trim():"") + " " + (emple.getApellidoMaterno()!=null?emple.getApellidoMaterno().trim():"") +": <b>" + msg.getMensaje() + "</b><i> el "+ DateManage.formatDateTimeToNormalMinutes(msg.getFechaEmision()) +"</i>";
            }else{
                mensajeAMostrar += "<ul>Nuevo mensaje: <b>" + msg.getMensaje() + "</b><i> el "+ DateManage.formatDateTimeToNormalMinutes(msg.getFechaEmision()) +"</i>";
            }            
            mensajeAMostrar += "<a href=\"../catEmpleados/catEmpleado_Mensajes_form.jsp?idEmpleado="+ msg.getIdEmpleadoEmisor() +"\" id=\"consultaForma\" title=\"Consultar\" class=\"modalbox_iframe\"> responder</a>";
                        
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