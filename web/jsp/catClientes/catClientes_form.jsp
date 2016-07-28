<%-- 
    Document   : catClientes_form.jsp
    Created on : 26-oct-2012, 12:13:49
    Author     : ISCesarMartinez poseidon24@hotmail.com
--%>

<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.ClienteCampoContenido"%>
<%@page import="com.tsp.sct.dao.jdbc.ClienteCampoContenidoDaoImpl"%>
<%@page import="com.tsp.sct.bo.ClienteCampoAdicionalBO"%>
<%@page import="com.tsp.sct.bo.ClienteCampoAdicionalBO"%>
<%@page import="com.tsp.sct.dao.dto.ClienteCampoAdicional"%>
<%@page import="com.tsp.sct.dao.dto.ClienteCategoria"%>
<%@page import="com.tsp.sct.bo.ClienteCategoriaBO"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensProspectoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProspecto"%>
<%@page import="com.tsp.sct.bo.SGProspectoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensClienteVendedor"%>
<%@page import="com.tsp.sct.bo.SGClienteVendedorBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.PasswordBO"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.jdbc.ClienteDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
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
        int idCliente = 0;
        try {
            idCliente = Integer.parseInt(request.getParameter("idCliente"));
        } catch (NumberFormatException e) {
        }
        
        int idProspecto=-1;
        try {
            idProspecto = Integer.parseInt(request.getParameter("idProspecto"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         *   3 = nuevo (modalidad PopUp [cotizaciones, pedidos, facturas]) 
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        String newRandomPass = "";
        
        String lunes = "";
        String martes = "";
        String miercoles = "";
        String jueves = "";
        String viernes = "";
        String sabado = "";
        String domingo = "";
        String todos = "";
        
        ClienteBO clienteBO = new ClienteBO(user.getConn());
        Cliente clientesDto = null;
        if (idCliente > 0){
            clienteBO = new ClienteBO(idCliente, user.getConn());
            clientesDto = clienteBO.getCliente();
            
            StringTokenizer tokensDias = new StringTokenizer(StringManage.getValidString(clientesDto.getDiasVisita()),",");
            String seleccion = "";
            while (tokensDias.hasMoreTokens()) {
                System.out.println("_______recupetando tokens");
                seleccion = "";
                seleccion = tokensDias.nextToken().intern().trim();
                if(seleccion.equals("DOM")){
                    domingo = seleccion;                
                }else if(seleccion.equals("LUN")){
                    lunes = seleccion;
                }else if(seleccion.equals("MAR")){
                    martes = seleccion;
                }else if(seleccion.equals("MIE")){
                    miercoles = seleccion;
                }else if(seleccion.equals("JUE")){
                    jueves = seleccion;
                }else if(seleccion.equals("VIE")){
                    viernes = seleccion;
                }else if(seleccion.equals("SAB")){
                    sabado = seleccion;
                }else if(seleccion.equals("8")){
                    todos = seleccion;
                }
            } 
            
        }else{
            newRandomPass = new PasswordBO().generateValidPassword();
        }
                
        
        SGClienteVendedorBO clienteVendedorBO = null;
        SgfensClienteVendedor clienteVendedorDto = null;
        if (clientesDto!=null){
            clienteVendedorBO = new SGClienteVendedorBO(idCliente, user.getConn());
            clienteVendedorDto = clienteVendedorBO.getClienteVendedor();
        }
        
        String calleProspecto = "";
        String ciudadProspecto = "";
        String estadoProspecto = "";
        String paisProspecto = "";
        
        SgfensProspecto prospectoDto = null;
        if (idProspecto>0){
            prospectoDto = new SGProspectoBO(idProspecto, user.getConn()).getSgfensProspecto();
            try{
                prospectoDto.setIdEstatus(2); //Deshabilitado
                new SgfensProspectoDaoImpl(user.getConn()).update(prospectoDto.createPk(),prospectoDto);
            }catch(Exception ex){ ex.printStackTrace();}
            
            //REPARAMOS LOS DATOS DE LA DIRECCION:
            if(!prospectoDto.getDireccion().equals("No registrada")){//validamos que tenga registrada una dirección
		StringTokenizer tokens = new StringTokenizer(prospectoDto.getDireccion(),",");
                try{//si existen los 4 tokens se recuperara la información completa (info recuperada automáticamente por e movil).
                    calleProspecto = tokens.nextToken().intern();
                    ciudadProspecto = tokens.nextToken().intern();
                    estadoProspecto = tokens.nextToken().intern();
                    paisProspecto = tokens.nextToken().intern();
                }catch(Exception e){}
		
            }
        }
        
        ClienteCategoriaBO clienteCategoriaBO = new ClienteCategoriaBO(user.getConn());
        ClienteCategoria[] clientesCategorias = clienteCategoriaBO.findClienteCategorias(0, idEmpresa, 0, 0, " AND ID_ESTATUS = 1 ");
        
        //campos adicionales:
        ClienteCampoAdicional[] clienteCampoAdicionalsDto = new ClienteCampoAdicional[0];
        ClienteCampoAdicionalBO clienteCampoAdicionalBO = new ClienteCampoAdicionalBO(user.getConn());
        try{
            clienteCampoAdicionalsDto = clienteCampoAdicionalBO.findClienteCampoAdicionals(0, idEmpresa , 0, 0, " AND ID_ESTATUS = 1 ");            
        }catch(Exception e){}
        
        EmpresaBO empresaBO = new EmpresaBO(user.getConn());
        EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());
        
        String verificadorSesionGuiaCerrada = "0";
        String cssDatosObligatorios = "border:3px solid red;";//variable para colocar el css del recuadro que encierra al input para la guia interactiva
        try{
            if(session.getAttribute("sesionCerrada")!= null){
                verificadorSesionGuiaCerrada = (String)session.getAttribute("sesionCerrada");
            }
        }catch(Exception e){}
        
    
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
                        url: "catClientes_ajax.jsp",
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
                                            <% if (!mode.equals("3")) {%>
                                                location.href = "catClientes_list.jsp?pagina="+"<%=paginaActual%>";
                                            <%}else{%>
                                                parent.recargarSelectClientes();
                                                parent.$.fancybox.close();
                                            <%}%>
                                            
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
            
            function grabarExpress(){
                    $.ajax({
                        type: "POST",
                        url: "catClientes_ajax.jsp?mode=express",
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
                                            <% if (!mode.equals("3")) {%>
                                                location.href = "catClientes_list.jsp";
                                            <%}else{%>
                                                parent.recargarSelectClientes();
                                                parent.$.fancybox.close();
                                            <%}%>
                                            
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
            
            function diasMarcado(){
                if ($('#domingoReporte').attr('checked') && $('#lunesReporte').attr('checked') && $('#martesReporte').attr('checked') && $('#miercolesReporte').attr('checked') && $('#juevesReporte').attr('checked') && $('#viernesReporte').attr('checked') && $('#sabadoReporte').attr('checked') )
                    $('#diarioReporte').attr('checked', true);
                else
                    $('#diarioReporte').attr('checked', false);
            }
            
            function todosDiasMarcados(){
                if ($('#diarioReporte').attr('checked')) {
                    //como esta marcado, desmarcamos todos los demas                
                    $('#domingoReporte').attr('checked', true);
                    $('#lunesReporte').attr('checked', true);
                    $('#martesReporte').attr('checked', true);
                    $('#miercolesReporte').attr('checked', true);
                    $('#juevesReporte').attr('checked', true);
                    $('#viernesReporte').attr('checked', true);
                    $('#sabadoReporte').attr('checked', true);                    
                }else{
                    //como esta marcado, desmarcamos todos los demas                
                    $('#domingoReporte').attr('checked', false);
                    $('#lunesReporte').attr('checked', false);
                    $('#martesReporte').attr('checked', false);
                    $('#miercolesReporte').attr('checked', false);
                    $('#juevesReporte').attr('checked', false);
                    $('#viernesReporte').attr('checked', false);
                    $('#sabadoReporte').attr('checked', false); 
                }                            
            }
            
            function mostrarInputsCredito(){
                if ($('#ventaCredito').attr('checked')) {                    
                    document.getElementById("divCreditoData").style.display="block";
                }else{
                    document.getElementById("divCreditoData").style.display="none";                    
                    $('#comisionCli').val(0);//reseteamos el valor a cero
                }
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

                <div class="inner <%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?"expose":""%>" id="leito">
                    <h1>Catálogos</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_cliente.png" alt="icon"/>
                                    <% if(clientesDto!=null){%>
                                    Editar Cliente ID <%=clientesDto!=null?clientesDto.getIdCliente():"" %>
                                    <%}else{%>
                                    Cliente
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idCliente" name="idCliente" value="<%=clientesDto!=null?clientesDto.getIdCliente():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>RFC<%=empresaPermisoAplicacionDto.getRfcPorNipCodigo() == 1?"/NIP":empresaPermisoAplicacionDto.getRfcPorNipCodigo() == 2?"/RUC":""%>:</label><br/>
                                        <input maxlength="13" type="text" id="rfc" name="rfc" style="width:300px;<%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getRfcCliente():"" %>" placeholder="XAXX010101000" <%=clientesDto!=null?(clientesDto.getIdEstatus()==3?"":"readonly"):"" %>/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Razón Social:</label><br/>
                                        <input maxlength="200" type="text" id="razonSocial" name="razonSocial" style="width:300px;<%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getRazonSocial():(prospectoDto!=null?prospectoDto.getRazonSocial():"") %>"/>
                                    </p>
                                    <br/>
                                    <%if(clientesCategorias.length > 0){%>
                                    <p>
                                        <label>Tipo:</label><br/>
                                        <select id="idClienteCategoria" name="idClienteCategoria">
                                            <option value="0">Selecciona Tipo de Categoría</option>
                                            <%
                                                out.print(clienteCategoriaBO.getClienteCategoriasByIdHTMLCombo(idEmpresa, (clientesDto!=null?clientesDto.getIdClienteCategoria():-1) ));
                                            %>
                                        </select>
                                    </p>
                                    <br/>
                                    <%}%>
                                    
                                    <p>
                                        <label>Nombre Comercial:</label><br/>
                                        <input maxlength="200" type="text" id="nombreComercial" name="nombreComercial" style="width:300px"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getNombreComercial():""%>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Nombre:</label><br/>
                                        <input maxlength="100" type="text" id="nombre" name="nombre" style="width:300px;<%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getNombreCliente():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Apellido Paterno:</label><br/>
                                        <input maxlength="100" type="text" id="apaterno" name="apaterno" style="width:300px"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getApellidoPaternoCliente():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Apellido Materno:</label><br/>
                                        <input maxlength="100" type="text" id="amaterno" name="amaterno" style="width:300px"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getApellidoMaternoCliente():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Clave:</label><br/>
                                        <input maxlength="40" type="text" id="claveCliente" name="claveCliente" style="width:300px"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getClave():"" %>"/>
                                    </p>
                                    <br/>
                                    
                                    <%ClienteCampoContenidoDaoImpl clienteCampoContenidoDaoImpl = new ClienteCampoContenidoDaoImpl(user.getConn());
                                    for(ClienteCampoAdicional cliCampo: clienteCampoAdicionalsDto){
                                        ClienteCampoContenido contenido = null;
                                        if(clientesDto!=null){
                                            try{
                                                contenido = clienteCampoContenidoDaoImpl.findByDynamicWhere(" ID_CLIENTE_CAMPO = " + cliCampo.getIdClienteCampo() + " AND ID_CLIENTE = " + clientesDto.getIdCliente(), null)[0];
                                            }catch(Exception e){}
                                        }
                                        
                                    %>
                                        <p>
                                            <label><%=cliCampo.getLabelNombre()%>:</label><br/>
                                            <input type="hidden" id="idCliCampAdi_<%=cliCampo.getIdClienteCampo()%>" name="idCliCampAdi_<%=cliCampo.getIdClienteCampo()%>" value="<%=cliCampo.getIdClienteCampo()%>" />
                                            <input maxlength="100" type="text" id="<%=cliCampo.getLabelNombre()%>" name="<%=cliCampo.getLabelNombre()%>" style="width:300px"
                                                   value="<%=contenido!=null?contenido.getValorLabel():""%>" <%=cliCampo.getTipoLabel()==1?"onkeypress=\"return validateNumber(event);\"":""%>/>
                                        </p>
                                        <br/>
                                    <%}%>
                                    
                                    
                                    
                                    <p>
                                        <label>Lada - Teléfono:</label><br/>
                                        <input maxlength="3" type="text" id="lada" name="lada" style="width:25px"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getLada():(prospectoDto!=null?prospectoDto.getLada():"") %>"
                                               onkeypress="return validateNumber(event);"/> -
                                        <input maxlength="8" type="text" id="telefono" name="telefono" style="width:255px"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getTelefono():(prospectoDto!=null?prospectoDto.getTelefono():"") %>"
                                               onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Extensión:</label><br/>
                                        <input maxlength="5" type="text" id="extension" name="extension" style="width:40px"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getExtension():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Celular:</label><br/>
                                        <input maxlength="11" type="text" id="celular" name="celular" style="width:300px"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getCelular():(prospectoDto!=null?prospectoDto.getCelular():"") %>"
                                               onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Correo Electrónico:</label><br/>
                                        <input maxlength="100" type="text" id="email" name="email" style="width:300px"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getCorreo():(prospectoDto!=null?prospectoDto.getCorreo():"") %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Contacto (Nombre completo):</label><br/>
                                        <input maxlength="100" type="text" id="contacto" name="contacto" style="width:300px"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getContacto():(prospectoDto!=null?prospectoDto.getContacto():"") %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Vendedor :</label><br/>
                                        <select id="idVendedor" name="idVendedor">
                                            <option value="-1">Selecciona vendedor...</option>
                                            <%
                                                UsuariosBO usuariosBO = new UsuariosBO();
                                                out.print(usuariosBO.getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL, clienteVendedorDto!=null?clienteVendedorDto.getIdUsuarioVendedor():0));
                                                out.print(usuariosBO.getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR_VENDEDOR, clienteVendedorDto!=null?clienteVendedorDto.getIdUsuarioVendedor():0));
                                                out.print(usuariosBO.getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_COBRADOR, clienteVendedorDto!=null?clienteVendedorDto.getIdUsuarioVendedor():0));
                                            %>
                                        </select>
                                    </p>
                                    <br/>
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=clientesDto!=null?(clientesDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
                                    </p>
                                    <br/><br/>
                                    
                                    <% if (!mode.equals("3")) {%>
                                        <div id="action_buttons">
                                            <p>
                                                <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                                                <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                                
                                                <input type="button" id="enviar" value="Guardar como Express" onclick="grabarExpress();" title="Puede permitir el guardar al cliente con solo tener nombre/razon social"/>
                                                
                                                <%if(clientesDto!=null){%>
                                                    <input type="button" src="../../images/icon_movimiento.png" value="Asignar Ubicación" onclick="window.location.href = 'catClientes_formMapa.jsp?idCliente='+<%=clientesDto!=null?clientesDto.getIdCliente():"" %>+'&acc=Mapa'">
                                                <%}%>
                                            </p>
                                        </div>
                                    <%}else{
                                        //En caso de ser Formulario en modo PopUp
                                    %>
                                        <div id="action_buttons">
                                            <p>
                                                <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                                                <input type="button" id="regresar" value="Cerrar" onclick="parent.$.fancybox.close();"/>
                                            </p>
                                        </div>
                                    <%}%>
                                    
                            </div>
                        </div>
                        <!-- End left column window -->
                        
                        <!--Columna derecha-->
                        <div class="column_right">
                            <div class="header">
                                <span>
                                    Domicilio
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <p>
                                        <label>*Calle:</label><br/>
                                        <input maxlength="100" type="text" id="calle" name="calle" style="width:300px;<%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getCalle():(prospectoDto!=null?calleProspecto:"") %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Numero exterior:</label><br/>
                                        <input maxlength="30" type="text" id="numero" name="numero" style="width:300px;<%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getNumero():(prospectoDto!=null?(prospectoDto.getDirNumeroExterior()!=null?prospectoDto.getDirNumeroExterior():""):"") %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Numero interior:</label><br/>
                                        <input maxlength="30" type="text" id="numeroInt" name="numeroInt" style="width:300px"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getNumeroInterior():(prospectoDto!=null?(prospectoDto.getDirNumeroExterior()!=null?prospectoDto.getDirNumeroInterior():""):"") %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*<%=empresaPermisoAplicacionDto.getRfcPorNipCodigo() != 2?"Colonia":"Parroquia"%>:</label><br/>
                                        <input maxlength="100" type="text" id="colonia" name="colonia" style="width:300px;<%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getColonia():(prospectoDto!=null?(prospectoDto.getDirNumeroExterior()!=null?prospectoDto.getDirColonia():""):"") %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label><%=empresaPermisoAplicacionDto.getRfcPorNipCodigo() == 0?"*":""%>Código Postal:</label><br/>
                                        <input maxlength="5" type="text" id="cp" name="cp" style="width:300px;<%=empresaPermisoAplicacionDto.getRfcPorNipCodigo() == 0 && empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getCodigoPostal():(prospectoDto!=null?(prospectoDto.getDirNumeroExterior()!=null?prospectoDto.getDirCodigoPostal():""):"") %>"
                                               onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*<%=empresaPermisoAplicacionDto.getRfcPorNipCodigo() != 2?"Municipio/Delegación":"Cantón"%>:</label><br/>
                                        <input maxlength="100" type="text" id="municipio" name="municipio" style="width:300px;<%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getMunicipio():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*<%=empresaPermisoAplicacionDto.getRfcPorNipCodigo() != 2?"Estado":"Provincia"%>:</label><br/>
                                        <input maxlength="100" type="text" id="estado" name="estado" style="width:300px;<%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getEstado():(prospectoDto!=null?estadoProspecto:"") %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*País:</label><br/>
                                        <input maxlength="100" type="text" id="pais" name="pais" style="width:300px;<%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getPais():(prospectoDto!=null?paisProspecto:"México") %>"/>                                        
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Referencia:</label><br/>
                                        <input maxlength="110" type="text" id="referenciaDir" name="referenciaDir" style="width:300px"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getReferencia():"" %>"/>
                                    </p>
                                    
                                    <br/>
                                    <p>
                                        <label>Días de Visita:</label><br/>
                                        <input type="checkbox" class="checkbox" <%=clientesDto!=null?(domingo.equals("DOM")?"checked":""):"" %> id="domingoReporte" name="domingoReporte" value="DOM" onclick="diasMarcado();"> <label for="domingoReporte">Domingo</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" <%=clientesDto!=null?(lunes.equals("LUN")?"checked":""):"" %> id="lunesReporte" name="lunesReporte" value="LUN" onclick="diasMarcado();"> <label for="lunesReporte">Lunes</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" <%=clientesDto!=null?(martes.equals("MAR")?"checked":""):"" %> id="martesReporte" name="martesReporte" value="MAR" onclick="diasMarcado();"> <label for="martesReporte">Martes</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" <%=clientesDto!=null?(miercoles.equals("MIE")?"checked":""):"" %> id="miercolesReporte" name="miercolesReporte" value="MIE" onclick="diasMarcado();"> <label for="miercolesReporte">Miércoles</label>
                                        &nbsp;&nbsp;&nbsp;
                                    </p>
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=clientesDto!=null?(jueves.equals("JUE")?"checked":""):"" %> id="juevesReporte" name="juevesReporte" value="JUE" onclick="diasMarcado();"> <label for="juevesReporte">Jueves</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" <%=clientesDto!=null?(viernes.equals("VIE")?"checked":""):"" %> id="viernesReporte" name="viernesReporte" value="VIE" onclick="diasMarcado();"> <label for="viernesReporte">Viernes</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" <%=clientesDto!=null?(sabado.equals("SAB")?"checked":""):"" %> id="sabadoReporte" name="sabadoReporte" value="SAB" onclick="diasMarcado();"> <label for="sabadoReporte">Sábado</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" id="diarioReporte" name="diarioReporte" value="8" onclick="todosDiasMarcados();"> <label for="diarioReporte">Marcar Todos</label>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Periodo:</label><br/>
                                        <input maxlength="30" type="text" id="periodo" name="periodo" style="width:300px"
                                               value="<%=clientesDto!=null?clienteBO.getCliente().getPerioricidad():"" %>" onkeypress="return validateNumber(event);" /> Semanas
                                    </p>
                                    <br/>
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=clientesDto!=null?(clientesDto.getPermisoVentaCredito()==1?"checked":""):"" %> id="ventaCredito" name="ventaCredito" value="1" onclick="mostrarInputsCredito();"> <label for="ventaCredito">Venta Crédito</label>
                                        
                                    </p>
                                    <br/>
                                    <div id="divCreditoData" style="display: <%=clientesDto!=null?(clientesDto.getPermisoVentaCredito()==1?"block":"none"):"none" %>">
                                        <p>
                                            <label>Comisión:</label><br/>
                                            <input maxlength="6" type="text" id="comisionCli" name="comisionCli" style="width:300px;"
                                                   value="<%=clientesDto!=null?clienteBO.getCliente().getComisionConsigna():"0" %>"
                                                   onkeypress="return validateNumber(event);"/>
                                        </p>
                                        <br/>
                                        <p>
                                            <label>Monto Máximo Crédito:</label><br/>
                                            <input maxlength="10" type="text" id="creditoMontoMax" name="creditoMontoMax" style="width:300px;"
                                                   value="<%=clientesDto!=null?clienteBO.getCliente().getCreditoMontoMax():"0" %>"
                                                   onkeypress="return validateNumber(event);"/>
                                        </p>
                                        <br/>
                                        <p>
                                            <label>Máximo Días Crédito:</label><br/>
                                            <input maxlength="3" type="text" id="creditoDiasMax" name="creditoDiasMax" style="width:300px;"
                                                   value="<%=clientesDto!=null?clienteBO.getCliente().getCreditoDiasMax():"0" %>"
                                                   onkeypress="return validateNumber(event);"/>
                                        </p>
                                        <br/>
                                    </div>
                                    <input type="hidden" id="latitud" name="latitud" value="<%=clientesDto!=null?clienteBO.getCliente().getLatitud():(prospectoDto!=null?prospectoDto.getLatitud():"")%>"/>
                                    <input type="hidden" id="longitud" name="longitud" value="<%=clientesDto!=null?clienteBO.getCliente().getLongitud():(prospectoDto!=null?prospectoDto.getLongitud():"")%>"/>
                                    <br/>
                                    
                                                                        
                            </div>
                        </div>
                        <!--Fin Columna derecha-->
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