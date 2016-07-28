<%-- 
    Document   : catComodatos_form
    Created on : 3/03/2016, 04:09:04 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.bo.AlmacenBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensClienteVendedor"%>
<%@page import="com.tsp.sct.bo.SGClienteVendedorBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.ComodatoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.ComodatoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Comodato"%>
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
        int idComodato = 0;
        try {
            idComodato = Integer.parseInt(request.getParameter("idComodato"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        String newRandomPass = "";
        
        ComodatoBO comodatoBO = new ComodatoBO(user.getConn());
        Comodato comodatosDto = null;
        if (idComodato > 0){
            comodatoBO = new ComodatoBO(idComodato,user.getConn());
            comodatosDto = comodatoBO.getComodato();
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
                        url: "catComodatos_ajax.jsp",
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
                                            location.href = "catComodatos_list.jsp?pagina="+"<%=paginaActual%>";
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
                    <h1>Comodatos</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_comodato.png" alt="icon"/>
                                    <% if(comodatosDto!=null){%>
                                    Editar Comodato ID <%=comodatosDto!=null?comodatosDto.getIdComodato():"" %>
                                    <%}else{%>
                                    Comodato
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idComodato" name="idComodato" value="<%=comodatosDto!=null?comodatosDto.getIdComodato():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>*Nombre:</label><br/>
                                        <input maxlength="120" type="text" id="nombreComodato" name="nombreComodato" style="width:300px"
                                               value="<%=comodatosDto!=null?comodatoBO.getComodato().getNombre():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Descripción:</label><br/>
                                        <input maxlength="500" type="text" id="descripcion" name="descripcion" style="width:300px"
                                               value="<%=comodatosDto!=null?comodatoBO.getComodato().getDescripcion():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Número de Serie:</label><br/>
                                        <input maxlength="80" type="text" id="noSerieComodato" name="noSerieComodato" style="width:300px"
                                               value="<%=comodatosDto!=null?comodatoBO.getComodato().getNumeroSerie():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Marca:</label><br/>
                                        <input maxlength="100" type="text" id="marcaComodato" name="marcaComodato" style="width:300px"
                                               value="<%=comodatosDto!=null?comodatoBO.getComodato().getMarca():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Modelo:</label><br/>
                                        <input maxlength="100" type="text" id="modeloComodato" name="modeloComodato" style="width:300px"
                                               value="<%=comodatosDto!=null?comodatoBO.getComodato().getModelo():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Tipo:</label><br/>
                                        <input maxlength="100" type="text" id="tipoComodato" name="tipoComodato" style="width:300px"
                                               value="<%=comodatosDto!=null?comodatoBO.getComodato().getTipo():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Capacidad:</label><br/>
                                        <input maxlength="100" type="text" id="capacidadComodato" name="capacidadComodato" style="width:300px"
                                               value="<%=comodatosDto!=null?comodatoBO.getComodato().getCapacidad():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Estado:</label><br/>
                                        <select size="1" id="estadoComodato" name="estadoComodato">                                            
                                            <option value="1" <%=comodatosDto!=null?(comodatoBO.getComodato().getEstado()==1?"selected":""):"selected" %> >Nuevo</option>
                                            <option value="2" <%=comodatosDto!=null?(comodatoBO.getComodato().getEstado()==2?"selected":""):"selected" %> >Re-uso</option>
                                        </select>                                        
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Almacén de resguardo:</label> <br/>
                                        <select size="1" id="idAlmacenComodato" name="idAlmacenComodato" > 
                                            <option value="0" ></option>
                                            <%
                                                out.print(new AlmacenBO(user.getConn()).getAlmacenesByIdHTMLCombo(idEmpresa, comodatosDto!=null?(comodatoBO.getComodato().getIdAlmacen()>0?comodatoBO.getComodato().getIdAlmacen():-1):-1,-1,-1,""));
                                            %>
                                        </select>
                                    </p>
                                    <br/>
                                    <!--<p>
                                        <label>Cliente asignado:</label><br/>
                                        <select id="q_idcliente" name="q_idcliente" class="flexselect">
                                            <option value="-1" ></option>
                                            <%= new ClienteBO(user.getConn()).getClientesByIdHTMLCombo(idEmpresa, comodatosDto!=null?(comodatoBO.getComodato().getIdCliente()>0?comodatoBO.getComodato().getIdCliente():-1):-1," AND ID_ESTATUS <> 2 " ) %>
                                        </select>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Dirección de resguardo:</label> <br/>
                                        <input maxlength="100" type="text" id="direccionComodato" name="direccionComodato" style="width:300px"
                                               value="<%=comodatosDto!=null?comodatoBO.getComodato().getDireccionResguardo():"" %>"/>
                                    </p>
                                    <br/>-->
                                    
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

        <script>
            $("select.flexselect").flexselect();
        </script>
    </body>
</html>
<%}%>
