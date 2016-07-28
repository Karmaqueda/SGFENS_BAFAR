<%-- 
    Document   : catPaquetes_form
    Created on : 25/09/2014, 11:40:26 AM
    Author     : leonardo
--%>

<%@page import="java.math.RoundingMode"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.tsp.sct.dao.dto.PaqueteRelacionConcepto"%>
<%@page import="com.tsp.sct.dao.jdbc.PaqueteRelacionConceptoDaoImpl"%>
<%@page import="com.tsp.sct.bo.PaqueteRelacionConceptoBO"%>
<%@page import="com.tsp.sct.dao.dto.Paquete"%>
<%@page import="com.tsp.sct.bo.PaqueteBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el cliente tiene acceso a este topico
    if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
        response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
        response.flushBuffer();
    } else {
        
        int paginaActual = 1;
        try{
            paginaActual = Integer.parseInt(request.getParameter("pagina"));
        }catch(Exception e){}

        int idEmpresa = user.getUser().getIdEmpresa();
        
        /*
         * Parámetros
         */
        int idPaquete = 0;
        try {
            idPaquete = Integer.parseInt(request.getParameter("idPaquete"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        String newRandomPass = "";
        
        PaqueteBO paqueteBO = new PaqueteBO(user.getConn());
        Paquete paquetesDto = null;
        BigDecimal montoTotalPaquete = BigDecimal.ZERO;
        if (idPaquete > 0){
            paqueteBO = new PaqueteBO(idPaquete,user.getConn());
            paquetesDto = paqueteBO.getPaquete();
            
            //obenemos los productos de la tabla PAQUETE_RELACION_CONCEPTO para obtener el monto total de dicho paquete
            PaqueteRelacionConcepto[] conceptos = new PaqueteRelacionConcepto[0];
            conceptos = new PaqueteRelacionConceptoDaoImpl(user.getConn()).findWhereIdPaqueteEquals(idPaquete);
            for(PaqueteRelacionConcepto con : conceptos){                
                montoTotalPaquete = montoTotalPaquete.add(BigDecimal.valueOf(con.getPrecio()).setScale(2, RoundingMode.HALF_UP));
            }
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
            
            function grabar(){
                if(validar()){
                    $.ajax({
                        type: "POST",
                        url: "catPaquetes_ajax.jsp",
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
                                            location.href = "catPaquetes_list.jsp?pagina="+"<%=paginaActual%>";
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
            }

            function validar(){
                /*
                if(jQuery.trim($("#nombre").val())==""){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "nombre de contacto" es requerido</center>',{'animate':true});
                    $("#nombre_contacto").focus();
                    return false;
                }
                */
                return true;
            }
            
        </script>
    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Almácen</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_paquetes.png" alt="icon"/>
                                    <% if(paquetesDto!=null){%>
                                    Editar Paquete <%=paquetesDto!=null?paquetesDto.getNombre():"" %>
                                    <%}else{%>
                                    Paquete
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idPaquete" name="idPaquete" value="<%=paquetesDto!=null?paquetesDto.getIdPaquete():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>*Nombre:</label><br/>
                                        <input maxlength="25" type="text" id="nombrePaquete" name="nombrePaquete" style="width:300px"
                                               value="<%=paquetesDto!=null?paqueteBO.getPaquete().getNombre():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Descripción:</label><br/>
                                        <input maxlength="250" type="text" id="descripcion" name="descripcion" style="width:300px"
                                               value="<%=paquetesDto!=null?paqueteBO.getPaquete().getDescripcion():"" %>"/>
                                    </p>
                                    <%if(montoTotalPaquete.compareTo(BigDecimal.ZERO) != 0){%>
                                    <br/>                                    
                                    <p>
                                        <label>Precio Paquete:</label><br/>
                                        <input maxlength="250" type="text" id="descripcion" name="descripcion" style="width:300px"
                                               value="<%=montoTotalPaquete%>" readonly/>
                                    </p>
                                    <%}%>
                                    <br/>
                                    <br/>
                                    
                                    
                                    <a href="catPaquetesConceptosRelacionados_list.jsp?<%="idPaquete"%>=<%=paquetesDto!=null?paqueteBO.getPaquete().getIdPaquete():""%>" id="consultaImpuestos" title="Asignar Productos" class="modalbox_iframe"  >
                                        <img src="../../images/icon_agregar.png" alt="Nuevo Concepto" class="help" title="Consultar"/>Productos del paquete<br/>
                                    </a>
                                    <br/>        
                                                                        
                                    <br/>        
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=paquetesDto!=null?(paquetesDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
                                    </p>
                                    <br/><br/>
                                    
                                    <div id="action_buttons">
                                        <p>
                                            <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                        </p>
                                    </div>
                                    
                            </div>
                        </div>
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
    <%session.setAttribute("listaProducto", null);%>
</html>
<%}%>