            <%@page import="com.tsp.sct.dao.dto.ActualizacionesMovil"%>
<%@page import="com.tsp.sct.dao.jdbc.ActualizacionesMovilDaoImpl"%>
<%-- 
    Document   : notificaciones
    Created on : 21/10/2015, 12:17:27 PM
    Author     : HpPyme
--%>

<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.dao.dto.NotificacionesUsuarios"%>
<%@page import="com.tsp.sct.bo.NotificacionBO"%>
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
    
    
    NotificacionBO notificacionBO = new NotificacionBO(user.getConn());
    NotificacionesUsuarios[] notificacionesDtos = new NotificacionesUsuarios[0];
      
    
    String filtroBusqueda =" AND ID_ESTATUS = 1";
    notificacionesDtos = notificacionBO.findNotificaciones(-1, user.getUser().getIdUsuarios(), -1, -1, filtroBusqueda);
    

    
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">       
        <script type="text/javascript" src="http://www.google.com/jsapi"></script>
        <script src="../../js/Gritter/js/jquery.gritter.js" type="text/javascript"></script>
        <link href="../../js/Gritter/css/jquery.gritter.css" rel="stylesheet" type="text/css" media="all"/>
        <script src="../../js/Gritter/js/jquery.gritter.js" type="text/javascript"></script>
        <link href="../../js/Gritter/css/jquery.gritter.css" rel="stylesheet" type="text/css" media="all"/>      
        
        
         <script>   
             function cierraMsj(idMsj){              
                 
                 $.ajax({
                    type: "POST",
                    url: "../catBroadcast/catBroadcast_ajax.jsp?mode=cierraMsj&idNotificacion="+idMsj,
                    data: $("#frm_action").serialize(),
                    beforeSend: function(objeto) {
                        $("#action_buttons").fadeOut("slow");
                        $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                        $("#ajax_loading").fadeIn("slow");
                    },
                    success: function(datos) {
                        if (datos.indexOf("--EXITO-->", 0) > 0) {
                         /*  $("#ajax_message").html(datos);
                            $("#ajax_loading").fadeOut("slow");
                            $("#ajax_message").fadeIn("slow");*/
                            
                        } else {
                            /*$("#ajax_loading").fadeOut("slow");
                            $("#ajax_message").html(datos);
                            $("#ajax_message").fadeIn("slow");
                            $("#action_buttons").fadeIn("slow");
                            $.scrollTo('#inner', 800);
                            alert("abierot");*/
                        }
                    }
                });                 
                 
             }
             
             function cierraNotif(divItem){                 
              $('div#gritter-item-'+divItem+' .gritter-item .gritter-close').click();
             }
             
             
         </script>
    </head>
    <body>
        <%
        int i= 0;
        for(NotificacionesUsuarios item:notificacionesDtos){
        i++;
            
            String version ="";
            String mejoras= "";
            String fechaLiberacion= "";
            if(item.getIdActualizacion()>0){
                
                ActualizacionesMovil datosActualizacion = new ActualizacionesMovilDaoImpl().findByPrimaryKey(item.getIdActualizacion());
                version = "<b>Versión:  </b>"+datosActualizacion.getNumVersion().trim()+"</br>";
                fechaLiberacion = "<b>Fecha: </b>"+datosActualizacion.getFechaLiberacion()+"</br>";
                mejoras = "<b>Mejoras:  </b></br>"+datosActualizacion.getComentarios().trim()+"</br></br>";
                
            }
        
        %>
                    
        <script>            
            $.gritter.add({
                    // (string | mandatory) the heading of the notification
                    title: '<%=item.getTitulo().trim()%>',
                    // (string | mandatory) the text inside the notification
                    text: '<%=item.getMensaje().trim()%><br/><br/>'                          
                          +'<%=version%><%=fechaLiberacion%><%=mejoras%>'
                          +'<a onclick="cierraMsj(<%=item.getIdNotificacion()%>);cierraNotif(<%=i%>);"><h4>No volver a Mostrar<h4/></a>',                   
                    //icon
                    image: '../../images/notificaciones/<%=item.getImagen()%>',
                    // Stickeh!
                    sticky: true,
                });     
        </script>   
            
        <%}%>
        
        <div id="notificaciones"  style="float: right; margin-top: 12px;">
            <!--<a href="#"><img src="../../images/notifications.png"/></a>-->
        </div>
    </body>
</html>
<%}%>