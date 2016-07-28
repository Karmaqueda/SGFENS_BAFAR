<%-- 
    Document   : catEmpleados_form.jsp
    Created on : 08-01-2013, 12:13:49
    Author     : Leonardo 
--%>

<%@page import="com.tsp.sct.bo.FoliosMovilEmpleadoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.PosMovilEstatusParametrosDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.PosMovilEstatusParametros"%>
<%@page import="com.tsp.sct.bo.HorarioBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoBitacoraPosicion"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoBitacoraPosicionDaoImpl"%>
<%@page import="com.tsp.sct.bo.RegionBO"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuarioInfoBO"%>
<%@page import="com.tsp.sct.dao.dto.DatosUsuario"%>
<%@page import="com.tsp.sct.bo.DispositivoMovilBO"%>
<%@page import="com.tsp.sct.dao.dto.DispositivoMovil"%>
<%@page import="com.tsp.sct.bo.EmpleadoRolBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.PasswordBO"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el Empleado tiene acceso a este topico
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
        int idEmpleado = 0;
        try {
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
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
        
        EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
        Empleado empleadosDto = null;
        if (idEmpleado > 0){
            empleadoBO = new EmpleadoBO(idEmpleado,user.getConn());
            empleadosDto = empleadoBO.getEmpleado();
        }else{
            newRandomPass = new PasswordBO().generateValidPassword();
        }
        
        //EXTRAEMOS DATOS
        UsuarioInfoBO datosUsuarioBO = null;
        try {
            datosUsuarioBO = new UsuarioInfoBO(empleadosDto.getIdUsuarios(),user.getConn());
        }catch (Exception e) {}
        
        EmpresaBO empresaBO = new EmpresaBO(user.getConn());
        EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());
        
        String verificadorSesionGuiaCerrada = "0";
        String cssDatosObligatorios = "border:3px solid red;";//variable para colocar el css del recuadro que encierra al input para la guia interactiva
        try{
            if(session.getAttribute("sesionCerrada")!= null){
                verificadorSesionGuiaCerrada = (String)session.getAttribute("sesionCerrada");
            }
        }catch(Exception e){}
        
        PosMovilEstatusParametros posMovilEstatusParametros = null;
        if (empleadosDto!=null){
            try { posMovilEstatusParametros = new PosMovilEstatusParametrosDaoImpl(user.getConn()).findWhereIdEmpleadoEquals(empleadosDto.getIdEmpleado())[0]; }catch(Exception ex){}
        }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <link rel="shortcut icon" href="../../images/favicon.ico">
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
                        url: "catEmpleados_ajax.jsp",
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
                                                location.href = "catEmpleados_list.jsp?pagina="+"<%=paginaActual%>";
                                            <%}else{%>
                                                parent.recargarSelectEmpleados();
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
            
            function apareceAutomovil(){   
                var seleccionadoRol = document.getElementById("idRolEmpleado").value;
                if(seleccionadoRol==2){                    
                    document.getElementById("automovilDisplay").style.display="block";
                }else{                    
                    document.getElementById("automovilDisplay").style.display="none";                    
                }
            }
            
            function validaCheckBox(){
                if ($('#repartidorEmpleado').attr('checked')) {                    
                    document.getElementById("consultaInventarioEmpleado").style.display="block";
                }
                else {                    
                    document.getElementById("consultaInventarioEmpleado").style.display="none";
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

                <div class="inner <%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?" expose":""%>" id="leito">
                    <h1>Catálogos</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    
                                    <%if(empleadosDto!=null){
                                    
                                        long s = (new Date()).getTime();
                                        long d = 0; 
                                        try{
                                            EmpleadoBitacoraPosicionDaoImpl bitacoraDao = new EmpleadoBitacoraPosicionDaoImpl();
                                            String filtro = " ID_EMPLEADO = "+ empleadosDto.getIdEmpleado() + " ORDER BY ID_BITACORA_POSICION DESC LIMIT 0,1";
                                            EmpleadoBitacoraPosicion bitEmp = bitacoraDao.findByDynamicWhere(filtro,null)[0];

                                            d = bitEmp.getFecha().getTime();
                                            //d = empleadoDto.getFechaHora().getTime();
                                        }catch(Exception e){}
                                        long diferencia = s - d;
                                        System.out.println("-------DIFERENCIA: "+diferencia);
                                        if(diferencia < 300000){
                                        %>
                                            <img src="../../images/estatusEmpleado/icon_activoTrabajando.png" alt="icon"/>                                            
                                        <%}else{%>
                                            <img src="../../images/estatusEmpleado/icon_desactivado.png" alt="icon"/>                                           
                                        <%}
                                        
                                    }else{%>
                                        <img src="../../images/estatusEmpleado/icon_desactivado.png" alt="icon"/>
                                    <%}
                                    %>
                                    
                                    <% if(empleadosDto!=null){%>
                                    Editar Empleado ID <%=empleadosDto!=null?empleadosDto.getIdEmpleado():"" %>
                                    <%}else{%>
                                    Empleado
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idEmpleado" name="idEmpleado" value="<%=empleadosDto!=null?empleadosDto.getIdEmpleado():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>*Número de Empleado:</label><br/>
                                        <input maxlength="50" type="text" id="numEmpleado" name="numEmpleado" style="width:300px;<%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>"
                                               value="<%=empleadosDto!=null?empleadoBO.getEmpleado().getNumEmpleado():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Nombre:</label><br/>
                                        <input maxlength="70" type="text" id="nombreEmpleado" name="nombreEmpleado" style="width:300px;<%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>"
                                               value="<%=empleadosDto!=null?empleadoBO.getEmpleado().getNombre():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Apellido Paterno:</label><br/>
                                        <input maxlength="70" type="text" id="apellidoPaternoEmpleado" name="apellidoPaternoEmpleado" style="width:300px;<%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>"
                                               value="<%=empleadosDto!=null?empleadoBO.getEmpleado().getApellidoPaterno():"" %>"/>
                                    </p>
                                    <br/>                                    
                                    <p>
                                        <label>*Apellido Materno:</label><br/>
                                        <input maxlength="70" type="text" id="apellidoMaternoEmpleado" name="apellidoMaternoEmpleado" style="width:300px;<%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>"
                                               value="<%=empleadosDto!=null?empleadoBO.getEmpleado().getApellidoMaterno():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Teléfono:</label><br/>                                        
                                        <input maxlength="10" type="text" id="telefonoEmpleado" name="telefonoEmpleado" style="width:300px"
                                               value="<%=empleadosDto!=null?empleadoBO.getEmpleado().getTelefonoLocal():"" %>"
                                               onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>                                    
                                    <p>
                                        <label>Correo Electrónico:</label><br/>
                                        <input maxlength="100" type="text" id="emailEmpleado" name="emailEmpleado" style="width:300px"
                                               value="<%=empleadosDto!=null?empleadoBO.getEmpleado().getCorreoElectronico():"" %>"
                                    </p>
                                    <br/> 
                                    <br/>
                                    <p>
                                        <label>Dirección:</label><br/>
                                        <input maxlength="100" type="text" id="direccionEmpleado" name="direccionEmpleado" style="width:300px"
                                               value="<%=datosUsuarioBO!=null?datosUsuarioBO.getDatosUsuario().getDireccion():"" %>"
                                    </p>                                    
                                    <br/>
                                    <br/>
                                    <p>
                                        <label>Sueldo:</label><br/>
                                        <input maxlength="10" type="text" id="sueldoEmpleado" name="sueldoEmpleado" style="width:300px"
                                               value="<%=empleadosDto!=null?empleadoBO.getEmpleado().getSueldo():"0" %>"
                                    </p>
                                    <br/>
                                    <br/>
                                    <p>
                                        <label>Porcentaje Comisión:</label><br/>
                                        <input maxlength="6" type="text" id="comisionEmpleado" name="comisionEmpleado" style="width:300px"
                                               value="<%=empleadosDto!=null?empleadoBO.getEmpleado().getPorcentajeComision():"0" %>"
                                    </p>
                                    <br/>
                                    <br/>
                                    <p>
                                        <label>Periodo de Pago:</label><br/>
                                        <select size="1" id="idPeriodo" name="idPeriodo" >
                                            <option value="-1" >Selecciona un Periodo</option>
                                            <option value="1" >Diario</option>
                                            <option value="2" >Semanal</option>
                                            <option value="3" >Catorcenal</option>
                                            <option value="4" >Quincenal</option>
                                            <option value="5" >Mensual</option>
                                        </select>                                        
                                    </p>
                                                                        
                                    <br/>
                                    <p>
                                        <label>Zona:</label><br/>
                                        <select size="1" id="idZona" name="idZona" >
                                            <option value="-1" >Selecciona una Zona</option>
                                                <%
                                                    out.print(new RegionBO(user.getConn()).getRegionsByIdHTMLCombo(idEmpresa, (empleadosDto!=null?empleadoBO.getEmpleado().getIdRegion():-1) ));                                                     
                                                %>
                                        </select>                                        
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Dispositivo Movil:</label><br/>
                                        <select size="1" id="idDispositivoMovilEmpleado" name="idDispositivoMovilEmpleado" >
                                            <option value="-1" >Selecciona un Dispositivo</option>
                                                <%
                                                    out.print(new DispositivoMovilBO(user.getConn()).getDispositivosMovilesByIdHTMLCombo(idEmpresa, (empleadosDto!=null?empleadoBO.getEmpleado().getIdDispositivo():-1) ));                                                     
                                                %>
                                        </select>                                        
                                    </p>  
                                    <br/> 
                                    <p>
                                        <label>*Rol Asignado:</label><br/>
                                        <select size="1" id="idRolEmpleado" name="idRolEmpleado" onclick="apareceAutomovil();" onchange="apareceAutomovil();" style="<%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>">
                                            <option value="-1">Selecciona un Rol</option>
                                                <%
                                                   // out.print(new EmpleadoRolBO(user.getConn()).getRolesByIdHTMLCombo(idEmpresa, (empleadosDto!=null?empleadoBO.getEmpleado().getIdMovilEmpleadoRol():-1), user.getUser().getIdRoles() )); //Metodo original
                                                    out.print(new EmpleadoRolBO(user.getConn()).getRolesByIdHTMLComboEVC(idEmpresa, (empleadosDto!=null?empleadoBO.getEmpleado().getIdMovilEmpleadoRol():-1), user.getUser().getIdRoles() )); //Metodo para EVC
                                                %>
                                        </select>                                        
                                    </p> 
                                    <br/>                                     
                                    <% if(user.getUser().getIdRoles() == RolesBO.ROL_ADMINISTRADOR){%>
                                    <p>
                                        <label>Asignar a Empresa/Sucursal:</label><br/>
                                        <select size="1" id="idSucursalEmpresaAsignado" name="idSucursalEmpresaAsignado">
                                            <option value="-1">Selecciona una Sucursal</option>
                                                <%
                                                    out.print(new EmpresaBO(user.getConn()).getEmpresasByIdHTMLCombo(idEmpresa, (empleadosDto!=null?empleadoBO.getEmpleado().getIdEmpresa():-1) ));                                                    
                                                %>
                                        </select>                                        
                                    </p> 
                                    <br/> 
                                    <%}%>
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=empleadosDto!=null?(empleadosDto.getIdEstatus()==1?"checked":""):"checked" %> id="estatus" name="estatus" value="1"> <label for="estatus">Activo</label>
                                    </p>
                                                                       
                                    <br/><br/>
                                    
                                    <% if (!mode.equals("3")) {%>
                                        <div id="action_buttons">
                                            <p>
                                                <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                                                <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
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
                        <!--Inicio columna derecha -->
                        <div class="column_right">
                            <div class="header">
                                <span>                                    
                                    Datos de Acceso
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idEmpleado" name="idEmpleado" value="<%=empleadosDto!=null?empleadosDto.getIdEmpleado():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>*Usuario:</label><br/>
                                        <input maxlength="50" type="text" id="usuarioEmpleado" name="usuarioEmpleado" style="width:300px;<%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>"
                                               value="<%=empleadosDto!=null?empleadoBO.getEmpleado().getUsuario():"" %>"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>*Contraseña:</label><br/>
                                        <input maxlength="20" type="text" id="contrasenaEmpleado" name="contrasenaEmpleado" style="width:300px;<%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?cssDatosObligatorios:""%>"
                                               value=""/>
                                    </p>
                                    <br/>
                                    <!--<p>
                                        <label>*Recordatorio de Contraseña:</label><br/>
                                        <input maxlength="100" type="text" id="recordatorioEmpleado" name="recordatorioEmpleado" style="width:300px"
                                               value="<//%=datosUsuarioBO!=null?datosUsuarioBO.getLdap().getRecordatorioContrasena():"" %>"/>
                                    </p>
                                    <br/>-->
                            </div>
                        </div>
                        <!-- Fin columna Derecha -->
                        
                        <!--Inicio columna derecha -->
                        <div class="column_right">
                            <div class="header">
                                <span>                                    
                                    Configuración
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=empleadosDto!=null?(empleadosDto.getPermisoVentaRapida()==1?"checked":""):"checked" %> id="ventaRapida" name="ventaRapida" value="1"> <label for="ventaRapida">Venta Rápida</label>
                                    </p>
                                    <br/>
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=empleadosDto!=null?(empleadosDto.getVentaConsigna()==1?"checked":""):"" %> id="ventaConsigna" name="ventaConsigna" value="1"> <label for="ventaventaConsignaRapida">Venta Consigna</label>
                                    </p>
                                    <br/>
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=empleadosDto!=null?(empleadosDto.getPermisoVentaCredito()==1?"checked":""):"checked" %> id="ventaCredito" name="ventaCredito" value="1"> <label for="ventaCredito">Venta Crédito</label>
                                    </p>
                                    <br/>
                                    
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=empleadosDto!=null?(empleadosDto.getPermisoDevoluciones()==1?"checked":""):"checked" %> id="permisoDevoluciones" name="permisoDevoluciones" value="1"> <label for="permisoDevoluciones">Devoluciones</label>
                                    </p>
                                    <br/>
                                    
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=empleadosDto!=null?(empleadosDto.getPermisoAutoServInventario()==1?"checked":""):"checked" %> id="permisoAutoServicioInventario" name="permisoAutoServicioInventario" value="1"> <label for="permisoAutoServicioInventario">Auto-Servicio de Inventario</label>
                                    </p>
                                    <br/>
                                    
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=empleadosDto!=null?(empleadosDto.getTrabajarFueraLinea()==1?"checked":""):"" %> id="trabajarFueraLinea" name="trabajarFueraLinea" value="1"> <label for="trabajarFueraLinea">Trabajar Fuera de Línea</label>
                                    </p>
                                    <br/>
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=empleadosDto!=null?(empleadosDto.getClientesCodigoBarras()==1?"checked":""):"" %> id="clientesBarras" name="clientesBarras" value="1"> <label for="clientesBarras">Búsqueda de clientes solo por código de barras</label>
                                    </p>
                                    <br/>
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=empleadosDto!=null?(empleadosDto.getPrecioDeCompra()==1?"checked":""):"" %> id="precioCompra" name="precioCompra" value="1"> <label for="precioCompra">Mostrar precio de compra</label>
                                    </p>
                                    <br/>
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=empleadosDto!=null?(empleadosDto.getPermisoNoCobroParcial()==1?"checked":""):"" %> id="noCobroParcial" name="noCobroParcial" value="1"> <label for="noCobroParcial">No Cobro Parcial</label>
                                    </p>
                                    <br/>
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=empleadosDto!=null? (empleadosDto.getPermisoVerProveedores()==1?"checked":""):"" %> id="verProveedores" name="verProveedores" value="1"> <label for="verProveedores">Acceso a proveedores</label>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Distancia obligatoria (En metros):</label><br/>
                                        <input maxlength="8" type="text" id="distanciaObligaEmple" name="distanciaObligaEmple" style="width:300px"
                                               value="<%=empleadosDto!=null?empleadosDto.getDistanciaObligatorio():"0" %>" onkeypress="return validateNumber(event);"/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Acciones con Clientes:</label>
                                        <select size="1" id="permisoAccionesClientes" name="permisoAccionesClientes">                                           
                                                <%                                                   
                                                    out.print(new EmpleadoBO(user.getConn()).getPermisosEmpleadosByIdHTMLCombo(empleadosDto!=null?empleadosDto.getPermisoAccionesCliente():3)); //Metodo para EVC
                                                %>
                                        </select> 
                                    </p>
                                    <br/>
                                    
                                    <p>
                                        <label>Solicitar Estatus cada (min):</label><br/>
                                        <input maxlength="4" type="text" id="tiempo_minutos_actualiza" name="tiempo_minutos_actualiza" style="width:300px"
                                               value="<%=posMovilEstatusParametros!=null?posMovilEstatusParametros.getTiempoMinutosActualiza():"0" %>" onkeypress="return validateNumber(event);"/>
                                    
                                    </p>  
                                    <br/>
                                    <p>
                                        <label>Recordatorio cada (min):</label><br/>
                                        <input maxlength="4" type="text" id="tiempo_minutos_recordatorio" name="tiempo_minutos_recordatorio" style="width:300px"
                                               value="<%=posMovilEstatusParametros!=null?posMovilEstatusParametros.getTMinutosRecordatorio():"0" %>" onkeypress="return validateNumber(event);"/>
                                    
                                    </p>  
                                    <br/>
                                   <p>
                                    <label>Horario de Trabajo:</label><br/>
                                        <select size="1" id="idhorarioAsignado" name="idhorarioAsignado">
                                            <option value="-1">Selecciona un Horario</option>
                                                <%
                                                out.print(new HorarioBO(user.getConn()).getHorariosByIdHTMLCombo(idEmpresa, (empleadosDto!=null?empleadosDto.getIdHorario():-1) ));                                                    
                                                %>
                                        </select>
                                    </p>  
                                    <br/>
                                    <p>
                                    <label>Serie Pedidos Móviles:</label><br/>
                                        <select size="1" id="id_folio_movil_empleado" name="id_folio_movil_empleado">
                                            <option value="-1">Selecciona una Serie</option>
                                            <%
                                            out.print(new FoliosMovilEmpleadoBO(user.getConn()).getFoliosMovilEmpleadoByIdHTMLCombo(idEmpresa, (empleadosDto!=null?empleadosDto.getIdFolioMovilEmpleado():-1), 
                                                    " AND ID_FOLIO_MOVIL_EMPLEADO NOT IN (SELECT DISTINCT ID_FOLIO_MOVIL_EMPLEADO FROM empleado WHERE ID_EMPLEADO!= " + (empleadosDto!=null?empleadosDto.getIdEmpleado():-1) + ")" ));                                                    
                                            %>
                                        </select>
                                    </p>  
                                    <br/>
                                    <p>
                                        <label>Censar Ubicación cada (seg):</label><br/>
                                        <input maxlength="3" type="text" id="intervalo_ubicacion_seg" name="intervalo_ubicacion_seg" style="width:300px"
                                               value="<%=empleadosDto!=null?empleadosDto.getIntervaloUbicacionSeg() : "600" %>" onkeypress="return validateNumberInteger(event);"/>
                                    
                                    </p>  
                                    <br/>
                                    
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=empleadosDto!=null?(empleadosDto.getRepartidor()==1?"checked":""):"checked" %> id="repartidorEmpleado" name="repartidorEmpleado" value="1" onclick="validaCheckBox();"> <label for="repartidorEmpleado">Repartidor</label>
                                        <% if(empleadosDto!=null){%>                                        
                                            <a href="catEmpleados_Inventario_list.jsp?idEmpleado=<%=empleadosDto!=null?empleadosDto.getIdEmpleado():""%>" id="consultaInventarioEmpleado" title="Asignar Inventario" class="modalbox_iframe" style=" display: <%=empleadosDto!=null?(empleadosDto.getRepartidor()==1?"block":"none"):"none" %> ">
                                                <img src="../../images/icon_producto.png" alt="Asignar Inventario" class="help" title="Asignar Inventario"/><br/>
                                            </a>                                            
                                        <%}else{%>
                                            - Para asignar inventario al vendedor; primero crearlo y despues asignar inventario
                                        <%}%>
                                             
                                    </p>
                                    
                                   
                            </div>
                        </div>
                        <!-- Fin columna Derecha -->
                        
                        
                        
                        
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