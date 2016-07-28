<%-- 
    Document   : catEmpleados_ValidaGastos_form
    Created on : 19/09/2015, 12:46:04 AM
    Author     : HpPyme
--%>

<%@page import="com.tsp.sct.bo.GastosEvcBO"%>
<%@page import="com.tsp.sct.dao.dto.GastosEvc"%>
<%@page import="com.tsp.sct.dao.jdbc.GastosEvcDaoImpl"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    int idGastos = -1;
    try{ idGastos = Integer.parseInt(request.getParameter("idGastos")); }catch(NumberFormatException e){}
    
    
    int paginaActual = 1;
    try {
        paginaActual = Integer.parseInt(request.getParameter("pagina"));
    } catch (Exception e) {
    }
    
    GastosEvcDaoImpl gastosEvcDao = new GastosEvcDaoImpl();
    GastosEvc gastosEvcDto = null;
    GastosEvcBO gastosEvcBO = new GastosEvcBO(idGastos,user.getConn());
    
    gastosEvcDto = gastosEvcBO.getGastos();
     
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">            
     
        <jsp:include page="../include/keyWordSEO.jsp" />

        <title><jsp:include page="../include/titleApp.jsp" /></title>

        <jsp:include page="../include/skinCSS.jsp" />

        <jsp:include page="../include/jsFunctions.jsp"/>
        <script type="text/javascript">
            
            function grabar(){
             
                    $.ajax({
                        type: "POST",
                        url: "catEmpleados_ajax.jsp",
                        data: $("#frm_action").serialize()+ "&mode=validaGasto",
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_message").html(datos);
                               $("#ajax_loading").fadeOut("slow");                               
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                        function(r){
                                            //location.href = "catEmpleadosGastos_list.jsp?pagina="+"<%=paginaActual%>";
                                        parent.recargar(<%=paginaActual%>);
                                        parent.$.fancybox.close();
                                        });
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               $("#action_buttons").fadeIn("slow");
                               $.scrollTo('#inner',800);
                           }
                        }
                    });
               
            }

          
            
        </script>
    </head>
    <body class="nobg"> 
        <div class="content_wrapper">        

           
            <!-- Inicio de Contenido -->
            

                <div class="inner">                   

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="onecolumn">
                     
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_seguimiento.png" />
                                    Validación de Gastos
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                <input type="hidden" id="idGastos" name="idGastos" value="<%=gastosEvcDto!=null?gastosEvcDto.getIdGastos():"" %>" /> 
                                <p>
                                   <label>Estatus Aprobación:</label><br/>
                                    <select size="1" id="idValidacion" name="idValidacion" class="flexselect">
                                            <option value="-1"></option>
                                            <option value="0">Pendiente</option>
                                            <option value="1">Aceptado</option>
                                            <option value="2">Rechazado</option>
                                    </select> 
                                </p>
                                <br><br>
                                <div id="action_buttons">
                                    <p>
                                        <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                                        <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                    </p>
                                </div>
                                    
                           
                        </div>
                        <!-- End left column window -->
                        
                        
                    </div>
                    </form>
                    <!--TODO EL CONTENIDO VA AQUÍ-->

                </div>

               
            <!-- Fin de Contenido-->
        </div>        
        <script>
          
           $("select.flexselect").flexselect();
        </script>  
    </body>
</html>
<%}%>
