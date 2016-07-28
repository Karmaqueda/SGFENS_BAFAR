<%-- 
    Document   : catEmpleadosXEstacionPos_list
    Created on : 23/06/2015, 23/06/2015 06:55:57 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.PosEstacion"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.bo.PosEstacionBO"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoXPosEstacion"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoXPosEstacionDaoImpl"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    long idEmpresa = user.getUser().getIdEmpresa();
    
    int idPosEstacion = -1;
    try{ idPosEstacion = Integer.parseInt(request.getParameter("idPosEstacion")); }catch(NumberFormatException e){}
    
    PosEstacionBO posEstacionBO = new PosEstacionBO(idPosEstacion, user.getConn());
    PosEstacion posEstacion = posEstacionBO.getPosEstacion();
    Empresa sucursalEstacion = new EmpresaBO(posEstacion.getIdEmpresa(), user.getConn()).getEmpresa();
    EmpleadoXPosEstacion[] empleadosXEstacion = new EmpleadoXPosEstacion[0];
    try {  empleadosXEstacion = posEstacionBO.findEmpleadosXEstacion(); }catch(Exception ex){}
    
    EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
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
            
             $(document).ready(function() {
                mostrarFormPopUpMode();
                iniciarFlexSelect();
            });
            
            function mostrarFormPopUpMode(){
		$('body').addClass('nobg');
            }
            
            function iniciarFlexSelect(){
                /*
                $("#id_cliente").flexselect({
                    jsFunction:  function(id) { selectCliente(id); }
                });
                */
                $("select.flexselect").flexselect();
            }
            
            function agregarEmpleado(){
                $.ajax({
                    type: "POST",
                    url: "catEmpleadosXEstacionPos_ajax.jsp",
                    data: $("#frm_action").serialize(),
                    beforeSend: function(objeto){
                        //$("#action_buttons").fadeOut("slow");
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
                                    location.reload();
                                });
                       }else{
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").html(datos);
                           $("#ajax_message").fadeIn("slow");
                           //$("#action_buttons").fadeIn("slow");
                           //$.scrollTo('#inner',800);
                       }
                    }
                });
            }
            
            function desvincularEmpleado(idEmpleado){
                $.ajax({
                    type: "POST",
                    url: "catEmpleadosXEstacionPos_ajax.jsp",
                    data: { mode: '2', id_empleado : idEmpleado, idPosEstacion : '<%=idPosEstacion%>' },//data: $("#frm_action").serialize(),
                    beforeSend: function(objeto){
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
                                    location.reload();
                                });
                       }else{
                           $("#ajax_loading").fadeOut("slow");
                           $("#ajax_message").html(datos);
                           $("#ajax_message").fadeIn("slow");
                       }
                    }
                });
            }
            
        </script>
    </head>
    <body class="nobg">
        <div id="ajax_loading" class="alert_info" style="display: none;"></div>
        <div id="ajax_message" class="alert_warning" style="display: none;"></div>
        
        <div id="info_gral" class="alert_info">
            Solo se pueden elegir Empleados con Rol Cajero o Administrador de Sucursal y que esten Activos.
            <br/>Deben estar registrados en la misma Sucursal de la Estación Punto de Venta:<br/>'<%= sucursalEstacion!=null?sucursalEstacion.getRazonSocial() + " - " + sucursalEstacion.getNombreComercial() : "(NO SE ELIGIO UNA ESTACION)" %>'
        </div>
        
        <% if (sucursalEstacion!=null){  %>
        <form id="frm_action" name="frm_action" action="" method="post" >
            <input type="hidden" id="mode" name="mode" value="1"/>
            <input type="hidden" id="idPosEstacion" name="idPosEstacion" value="<%=idPosEstacion %>" />
            <label for="id_empleado">Agregar Empleado:</label>
            <select id="id_empleado" name="id_empleado" class="flexselect" style="width: 250px;">
                <option value="-1"></option>
                <%= empleadoBO.getEmpleadosByIdHTMLCombo(sucursalEstacion.getIdEmpresa(), -1, " AND ID_ESTATUS=1 AND ID_MOVIL_EMPLEADO_ROL IN (2,27,23) ") %>
            </select>
            <input onclick="agregarEmpleado();" type="button" id="nuevo" name="nuevo" class="right_switch" value="AGREGAR EMPLEADO" 
                style="float: right; "/>
        </form>
        <%}%>
        
        <table class="data" width="100%" cellpadding="0" cellspacing="0">
            <thead>
                <tr>
                    <th colspan="6" style="text-align: center;">Estación Punto de Venta '<%= posEstacion!=null?posEstacion.getIdentificacion()+" - "+posEstacion.getNombre():"(NO SE ELIGIO UNA ESTACIÓN)" %>'</th>
                </tr>
                <tr>
                    <th>ID</th>
                    <th>Num Empleado</th>
                    <th>Nombre</th>
                    <th>Rol</th>
                    <th>Estatus</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <% 
                for ( EmpleadoXPosEstacion item : empleadosXEstacion ) {
                    Empleado empleadoDto = null;
                    try {
                        empleadoDto = empleadoBO.findEmpleadobyId(item.getIdEmpleado());
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    String rolName = empleadoDto!=null?RolesBO.getRolName(empleadoDto.getIdMovilEmpleadoRol()):"";
                    
                %>
                <tr>
                    <td><%= item.getIdEmpleado() %></td>
                    <td><%= empleadoDto!=null ?  StringManage.getValidString(empleadoDto.getNumEmpleado()) : ""%></td>
                    <td><%= empleadoDto!=null ? (StringManage.getValidString(empleadoDto.getNombre())+" "+StringManage.getValidString(empleadoDto.getApellidoPaterno()) ) : ""%></td>
                    <td><%= rolName %></td>
                    <td><%= empleadoDto.getIdEstatus()==1?"Activo":"Desactivado" %></td>
                    <td>
                        <a href="#" onclick="desvincularEmpleado('<%= item.getIdEmpleado() %>');"><img src="../../images/icon_delete.png" alt="Eliminar de Estacion" class="help" title="Eliminar de Estacion"/></a>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
        <script>
           iniciarFlexSelect();
        </script>
    </body>
</html>
