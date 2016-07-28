<%-- 
    Document   : catProveedores_formMapa
    Created on : 18/08/2015, 04:51:48 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.dao.dto.SgfensProveedor"%>
<%@page import="com.tsp.sct.bo.SGProveedorBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProspecto"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensProspectoDaoImpl"%>
<%@page import="java.util.StringTokenizer"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el sgfensProveedor tiene acceso a este topico
    if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
        response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
        response.flushBuffer();
    } else {

        int idEmpresa = user.getUser().getIdEmpresa();
        
        /*
         * Parámetros
         */
        int idSgfensProveedor = 0;
        try {
            idSgfensProveedor = Integer.parseInt(request.getParameter("idProveedor"));
        } catch (NumberFormatException e) {
        }
        
        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         *   3 = nuevo (modalidad PopUp [cotizaciones, pedidos, facturas]) 
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
               
        SGProveedorBO sgfensProveedorBO = new SGProveedorBO(user.getConn());
        SgfensProveedor sgfensProveedorsDto = null;
        if (idSgfensProveedor > 0){
            sgfensProveedorBO = new SGProveedorBO(idSgfensProveedor, user.getConn());
            sgfensProveedorsDto = sgfensProveedorBO.getSgfensProveedor();
        }
        
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/keyWordSEO.jsp" />

        <title><jsp:include page="../include/titleApp.jsp" /></title>

        <jsp:include page="../include/skinCSS.jsp" />

        <jsp:include page="../include/jsFunctions.jsp"/>
        
        <script type="text/javascript">      
            
            $(document).ready(function() {
                //Si se recibio el parametro para que el modo sea en forma de popup
                <%= mode.equals("3")? "mostrarFormPopUpMode();":""%>
            });
            
            function mostrarFormPopUpMode(){
		$('#left_menu').hide();
                $('#header').hide();
		//$('#show_menu').show();
		$('body').addClass('nobg');
		$('#content').css('marginLeft', 30);
		$('#wysiwyg').css('width', '97%');
		setNotifications();
            }
            
            
            function grabar(idSgfensProveedor){
                alert("GRABANDO!!!!");
                    $.ajax({
                        type: "POST",
                        url: "catProveedores_ajax.jsp",
                        data: $("#frm_action").serialize(),
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_message").html(datos);
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                        function(r){
                                            location.href = "catSgfensProveedors_formMapa.jsp?idSgfensProveedor="+idSgfensProveedor+"&acc=Mapa"
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
    <body>
        <div class="content_wrapper">

            <% if (!mode.equals("3")) {%>
                <jsp:include page="../include/header.jsp" flush="true"/>
                <jsp:include page="../include/leftContent.jsp"/>
            <% } %>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Proveedor</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_proveedor.png" alt="icon"/>
                                    <% if(sgfensProveedorsDto!=null){%>
                                    Localización de SgfensProveedor ID <%=sgfensProveedorsDto!=null?sgfensProveedorsDto.getIdProveedor():"" %>
                                    <%}else{%>
                                    SgfensProveedor  Localización
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idProveedor" name="idProveedor" value="<%=sgfensProveedorsDto!=null?sgfensProveedorsDto.getIdProveedor():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="dirMapa" />
                                    <p>
                                        <label>RFC:</label><br/>
                                        <%=sgfensProveedorsDto!=null?sgfensProveedorBO.getSgfensProveedor().getIdProveedor():"" %>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Razón Social/Nombre:</label><br/>
                                        <%=sgfensProveedorsDto!=null?sgfensProveedorBO.getSgfensProveedor().getRazonSocial():"" %>                                    </p>
                                    <br/>
                                                                       
                                    
                            </div>
                        </div>
                            <!------------->
                        
                        
                        <!-- contenido de columna derecha Mapa-->
                        <div class="column_right">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_movimiento.png" alt="icon"/>                                    
                                    Mapa                                     
                                </span>
                            </div>
                            <br class="clear"/>
                            
                            <div class="content">                                                                                 
                                <jsp:include page="../include/Mapa_ProveedorLocalizacion.jsp"/>  
                                <br/>   
                                    <p>
                                    <label>Dirección Seleccionada*:</label><br/>
                                    <input maxlength="100" type="text" id="direccion" name="direccion" style="width:470px" readonly/>
                                    *Si desea usar esta dirección, Copie y Pegue en los campos correspondientes.
                                </p>
                                <br/> 
                            </div>
                        </div>
                        <!-- fin de contenido de columna derecha Mapa-->
                        
                        
                            <div class="column_left">
                            <div class="header">
                                <span>
                                    Domicilio
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <p>
                                        <label>*Calle:</label><br/>
                                        <input maxlength="100" type="text" id="calle" name="calle" style="width:300px"
                                               value="<%=sgfensProveedorsDto!=null?sgfensProveedorBO.getSgfensProveedor().getCalle():""%>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Numero exterior:</label><br/>
                                        <input maxlength="30" type="text" id="numero" name="numero" style="width:300px"
                                               value="<%=sgfensProveedorsDto!=null?sgfensProveedorBO.getSgfensProveedor().getNumero():""%>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Numero interior:</label><br/>
                                        <input maxlength="30" type="text" id="numeroInt" name="numeroInt" style="width:300px"
                                               value="<%=sgfensProveedorsDto!=null?sgfensProveedorBO.getSgfensProveedor().getNumeroInterior():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Colonia:</label><br/>
                                        <input maxlength="100" type="text" id="colonia" name="colonia" style="width:300px"
                                               value="<%=sgfensProveedorsDto!=null?sgfensProveedorBO.getSgfensProveedor().getColonia():""%>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Código Postal:</label><br/>
                                        <input maxlength="5" type="text" id="cp" name="cp" style="width:300px"
                                               value="<%=sgfensProveedorsDto!=null?sgfensProveedorBO.getSgfensProveedor().getCodigoPostal():"" %>"
                                               onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Municipio/Delegación:</label><br/>
                                        <input maxlength="100" type="text" id="municipio" name="municipio" style="width:300px"
                                               value="<%=sgfensProveedorsDto!=null?sgfensProveedorBO.getSgfensProveedor().getMunicipio():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Estado:</label><br/>
                                        <input maxlength="100" type="text" id="estado" name="estado" style="width:300px"
                                               value="<%=sgfensProveedorsDto!=null?sgfensProveedorBO.getSgfensProveedor().getEstado():""%>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*País:</label><br/>
                                        <input maxlength="100" type="text" id="pais" name="pais" style="width:300px"
                                               value="<%=sgfensProveedorsDto!=null?sgfensProveedorBO.getSgfensProveedor().getPais():""%>"/>                                       
                                    </p>                                                                        
                                    <br/>
                                    <div id="action_buttons">
                                    
                                        <p>
                                        <input type="button" id="enviar" value="Guardar" onclick="grabar(<%=idSgfensProveedor%>);"/> 
                                        </p>
                                    </div>
                                                     
                            </div>
                        </div>
                        <!---------------->                                  
                        
                                    
                        <!-- End left column window -->
                        
                    </div>
                    </form>
                    <!--TODO EL CONTENIDO VA AQUÍ-->

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>


    </body>
</html>
<%}%>