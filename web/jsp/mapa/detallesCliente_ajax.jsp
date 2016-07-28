<%-- 
    Document   : detallesCliente_ajax
    Created on : 11/01/2013, 08:27:48 PM
    Author     : Luis
--%>

<%@page import="com.tsp.sct.dao.factory.EstatusDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.Estatus"%>
<%@page import="com.tsp.sct.dao.factory.ClienteDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    long id = 0l;
    try{
        id = Long.parseLong(request.getParameter("id"));
    }catch(Exception e){}
    if(id > 0){
        Cliente clienteDto = null;
        try{
            clienteDto = ClienteDaoFactory.create(user.getConn()).findByPrimaryKey(Integer.parseInt(request.getParameter("id")));
        }catch(Exception e){}
        if(clienteDto!=null){
            Estatus estatusDto = null;
            try{
                estatusDto = EstatusDaoFactory.create(user.getConn()).findByPrimaryKey(clienteDto.getIdEstatus());
            }catch(Exception e){}
            
            
            String nombrecliente ="";
            if(clienteDto.getApellidoPaternoCliente()!=null&&!clienteDto.getApellidoPaternoCliente().toUpperCase().equals("NULL")&&!clienteDto.getApellidoPaternoCliente().toUpperCase().equals("Campo por llenar")){
                nombrecliente += clienteDto.getApellidoPaternoCliente();
            }
            if(clienteDto.getApellidoMaternoCliente()!=null&&!clienteDto.getApellidoMaternoCliente().toUpperCase().equals("NULL")&&!clienteDto.getApellidoMaternoCliente().toUpperCase().equals("Campo por llenar")){
                nombrecliente += " " + clienteDto.getApellidoPaternoCliente();
            }
            if(clienteDto.getNombreCliente()!=null){
                nombrecliente += " " + clienteDto.getNombreCliente();
            }
            
            out.print(""
                    + "<p>"
                    + "      <label>Nombre:</label><br/>" 
                    + "      " + nombrecliente
                    + "</p>"
                    + "<p>"
                    + "      <label>Estado:</label><br/>" 
                    + "      " + (estatusDto!=null?estatusDto.getIdEstatus()!=3?estatusDto.getNombre():"ACTIVO</br>(No disponible para facturar)":"")
                    + "</p>"
                    
                    + "<!--EXITO-->");
        }else{
            out.print("<!--ERROR-->No se encontr&oacute; informaci&oacute;n.");
        }
    }else{
        out.print("<!--ERROR-->No se encontr&oacute; informaci&oacute;n.");
    }
    
%>