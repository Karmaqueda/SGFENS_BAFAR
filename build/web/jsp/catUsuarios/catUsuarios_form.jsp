<%-- 
    Document   : catUsuarios_form.jsp
    Created on : 26-oct-2012, 12:13:49
    Author     : ISCesarMartinez poseidon24@hotmail.com
--%>

<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.dao.dto.SgfensVendedorDatos"%>
<%@page import="com.tsp.sct.bo.PasswordBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.dao.jdbc.UsuariosDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Usuarios"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el usuario tiene acceso a este topico
    if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
        response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
        response.flushBuffer();
    } else {
        
        /*
         * Parámetros
         */
        
        int paginaActual = 1;
        try{
            paginaActual = Integer.parseInt(request.getParameter("pagina"));
        }catch(Exception e){}
        
        int idUsuarios = 0;
        try {
            idUsuarios = Integer.parseInt(request.getParameter("idUsuarios"));
        } catch (NumberFormatException e) {
        }
        
        int idEmpresa = user.getUser().getIdEmpresa();

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        String newRandomPass = "";
        
        UsuarioBO usuarioBO = new UsuarioBO();
        Usuarios usuariosDto = null;
        SgfensVendedorDatos usuarioDatosVendedor = null;
        if (idUsuarios > 0){
            usuarioBO = new UsuarioBO(idUsuarios);
            usuariosDto = usuarioBO.getUser();
            usuarioDatosVendedor = usuarioBO.getDatosVendedor();
        }else{
            newRandomPass = new PasswordBO().generateValidPassword();
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
                        url: "catUsuarios_ajax.jsp",
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
                                            location.href = "catUsuarios_list.jsp?pagina="+"<%=paginaActual%>";
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
                    <h1>Administración</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_users.png" alt="icon"/>
                                    <% if(usuariosDto!=null){%>
                                    Editar Usuario ID <%=usuariosDto!=null?usuariosDto.getIdUsuarios():"" %>
                                    <%}else{%>
                                    Usuario
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                <form action="" method="post" id="frm_action">
                                    <input type="hidden" id="idUsuarios" name="idUsuarios" value="<%=usuariosDto!=null?usuariosDto.getIdUsuarios():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    
                                    <% if(usuariosDto!=null){%>
                                        <label>Usuario:</label>
                                        <h2><i><%=usuariosDto!=null?usuariosDto.getUserName():"" %></i></h2><br/>
                                    <%}else{%>
                                        <p>
                                            <label>Usuario:</label><br/>
                                            <input maxlength="20" type="text" id="usuario" name="usuario" style="width:300px"
                                                   value="<%=usuariosDto!=null?usuariosDto.getUserName():"" %>"/>
                                        </p>
                                        <br/>
                                    <%}%>
                                    
                                    <% if (usuariosDto==null) {%>
                                    <p>
                                        <label>Contraseña: &nbsp;&nbsp;&nbsp;&nbsp;*aleatorio</label><br/>
                                        <input type="text" id="password" name="password" style="width:300px"
                                               value="<%=newRandomPass%>"/>
                                    </p>
                                    <br/>
                                    <%}%>
                                    <p>
                                        <label>Nombre:</label><br/>
                                        <input maxlength="100" type="text" id="nombre" name="nombre" style="width:300px"
                                               value="<%=usuariosDto!=null?usuarioBO.getDatosUsuario().getNombre():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Apellido Paterno:</label><br/>
                                        <input maxlength="100" type="text" id="apaterno" name="apaterno" style="width:300px"
                                               value="<%=usuariosDto!=null?usuarioBO.getDatosUsuario().getApellidoPat():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Apellido Materno:</label><br/>
                                        <input maxlength="100" type="text" id="amaterno" name="amaterno" style="width:300px"
                                               value="<%=usuariosDto!=null?usuarioBO.getDatosUsuario().getApellidoMat():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Dirección:</label><br/>
                                        <input maxlength="100" type="text" id="direccion" name="direccion" style="width:300px"
                                               value="<%=usuariosDto!=null?usuarioBO.getDatosUsuario().getDireccion():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>                                        
                                        <label>*Lada - *Teléfono:</label><br/>
                                        <input maxlength="3" type="text" id="lada" name="lada" style="width:25px"
                                                value="<%=usuariosDto!=null?usuarioBO.getDatosUsuario().getLada():"" %>"
                                                onkeypress="return validateNumber(event);"/> -
                                        <input maxlength="8" type="text" id="telefono" name="telefono" style="width:255px"
                                                value="<%=usuariosDto!=null?usuarioBO.getDatosUsuario().getTelefono():"" %>"
                                                onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Celular:</label><br/>
                                        <input maxlength="11" type="text" id="celular" name="celular" style="width:300px"
                                               value="<%=usuariosDto!=null?usuarioBO.getDatosUsuario().getCelular():"" %>"
                                               onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Correo Electrónico:</label><br/>
                                        <input maxlength="100" type="text" id="email" name="email" style="width:300px"
                                               value="<%=usuariosDto!=null?usuarioBO.getDatosUsuario().getCorreo():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Sucursal</label><br/>
                                        <select id="idSucursal" name="idSucursal" class="flexselect" style="width: 300px;">
                                            <option></option>
                                            <%= new EmpresaBO(user.getConn()).getEmpresasByIdHTMLCombo(idEmpresa, (usuariosDto!=null?usuariosDto.getIdEmpresa():idEmpresa), " AND ID_ESTATUS!=2 ") %>
                                        </select>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Rol:</label>
                                        <select id="idRol" name="idRol">
                                            <optgroup label="Rol">
                                                <option value="2" <%=usuariosDto!=null?(usuariosDto.getIdRoles()==2?"selected":""):"selected" %>>Administrador</option>
                                                <option value="40" <%=usuariosDto!=null?(usuariosDto.getIdRoles()==40?"selected":""):"" %>>Gerente</option>
                                                <option value="17" <%=usuariosDto!=null?(usuariosDto.getIdRoles()==17?"selected":""):"" %>>Promotor</option>
                                                <option value="29" <%=usuariosDto!=null?(usuariosDto.getIdRoles()==29?"selected":""):"" %>>Gestor de crédito</option>
                                                <option value="41" <%=usuariosDto!=null?(usuariosDto.getIdRoles()==41?"selected":""):"" %>>Verificador</option>
                                                <option value="42" <%=usuariosDto!=null?(usuariosDto.getIdRoles()==42?"selected":""):"" %>>Mesa de control</option>
                                                <option value="43" <%=usuariosDto!=null?(usuariosDto.getIdRoles()==43?"selected":""):"" %>>Dirección</option>
                                            </optgroup>
                                        </select>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Sueldo mensual:</label><br/>
                                        <input maxlength="14" type="text" id="sueldo_mensual" name="sueldo_mensual" style="width:300px"
                                                value="<%=usuarioDatosVendedor!=null?usuarioDatosVendedor.getSueldoMensual():"0" %>"
                                                onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Porcentaje comisiones (%):</label><br/>
                                        <input maxlength="5" type="text" id="porcentaje_comisiones" name="porcentaje_comisiones" style="width:300px"
                                                value="<%=usuarioDatosVendedor!=null?usuarioDatosVendedor.getPorcentajeComisiones():"0" %>"
                                                onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=usuariosDto!=null?(usuariosDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
                                    </p>
                                    <br/><br/>
                                    
                                    <div id="action_buttons">
                                        <p>
                                            <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                        </p>
                                    </div>
                                    
                                </form>
                            </div>
                        </div>
                        <!-- End left column window -->
                    </div>
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