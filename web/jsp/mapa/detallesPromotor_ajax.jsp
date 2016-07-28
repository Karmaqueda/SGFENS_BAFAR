<%-- 
    Document   : detallesPromotor_ajax
    Created on : 9/01/2013, 07:37:43 PM
    Author     : Luis
--%>

<%@page import="com.tsp.sct.dao.factory.EstatusDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.Estatus"%>
<%@page import="com.tsp.sct.dao.factory.EmpleadoDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    int id = 0;
    try{
        id = Integer.parseInt(request.getParameter("id"));
    }catch(Exception e){}
    if(id > 0){
        Empleado empleadoDto = null;
        try{
            empleadoDto = EmpleadoDaoFactory.create(user.getConn()).findByPrimaryKey(id);
        }catch(Exception e){}
        if(empleadoDto!=null){
            Estatus estatusDto = null;
            try{
                estatusDto = EstatusDaoFactory.create(user.getConn()).findByPrimaryKey(empleadoDto.getIdEstatus());
            }catch(Exception e){}
            out.print(""
                    + "<p>"
                    + "      <label>Nombre:</label><br/>" 
                    + "      " + empleadoDto.getApellidoPaterno() + " " + empleadoDto.getApellidoMaterno() + " " + empleadoDto.getNombre() 
                    + "</p>"
                    + "<p>"
                    + "      <label>Estado:</label><br/>" 
                    + "      " + (estatusDto!=null?estatusDto.getNombre():"")
                    + "</p>"
                    + "<p>"
                    + "      <label>No. Empleado:</label><br/>" 
                    + "      " + empleadoDto.getNumEmpleado()
                    + "</p>"
                    + "<p>"
                    + "      <label>Tel&eacute;fono local:</label><br/>" 
                    + "      " + empleadoDto.getTelefonoLocal()
                    + "</p>"
                    + "<p>"
                    + "      <label>Correo:</label><br/>" 
                    + "      " + empleadoDto.getCorreoElectronico()
                    + "</p>"
                    + "<!--EXITO-->");
        }else{
            out.print("<!--ERROR-->No se encontr&oacute; informaci&oacute;n.");
        }
    }else{
        out.print("<!--ERROR-->No se encontr&oacute; informaci&oacute;n.");
    }
    
%>