<%-- 
    Created on : 30/11/2015, 02:49:13 PM
    Author     : Cesar
--%>


<%@page import="com.tsp.sct.bo.SGCobranzaMetodoPagoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensCobranzaMetodoPago"%>
<%@page import="com.tsp.sct.bo.SGCobranzaAbonoBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.dao.dto.DatosUsuario"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.dao.factory.SgfensCobranzaAbonoDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.SgfensCobranzaAbono"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    long id = 0l;
    try{
        id = Long.parseLong(request.getParameter("id"));
    }catch(Exception e){}
    if(id > 0){
        SgfensCobranzaAbono cobranzaDto = null;
        try{
            cobranzaDto = SgfensCobranzaAbonoDaoFactory.create(user.getConn()).findByPrimaryKey(Integer.parseInt(request.getParameter("id")));
        }catch(Exception e){}
        if(cobranzaDto!=null){
            String vendedorStr ="-No Asignado-";
            String clienteStr ="";
            String metodoPagoStr="No identificado";
            DatosUsuario datosUsuarioVendedorDto = new UsuarioBO(cobranzaDto.getIdUsuarioVendedor()).getDatosUsuario();
            Cliente clienteDto = new ClienteBO(cobranzaDto.getIdCliente(),user.getConn()).getCliente();
            SgfensCobranzaMetodoPago metodoPagoDto = new SGCobranzaMetodoPagoBO(cobranzaDto.getIdCobranzaMetodoPago(),user.getConn()).getCobranzaMetodoPago();

            if (datosUsuarioVendedorDto!=null)
                vendedorStr = datosUsuarioVendedorDto.getNombre() + " " + datosUsuarioVendedorDto.getApellidoPat();
            if (clienteDto!=null)
                clienteStr = clienteDto.getRazonSocial();
            if (metodoPagoDto!=null)
                metodoPagoStr = metodoPagoDto.getNombreMetodoPago();
                                                        
            //out.print("");      
%>
                <h3>Detalles de Cobranza</h3>
                
                <label>ID : </label>
                <%= cobranzaDto.getIdCobranzaAbono()%>
                <br/>
                <label>Fecha Registro : </label>
                <%= new SimpleDateFormat("dd/MM/yyyy HH:mm").format(cobranzaDto.getFechaAbono())%>
                <br/>
                <label>Cliente : </label>
                <%= clienteStr %>
                <br/>
                <label>Vendedor : </label>
                <%= vendedorStr %>
                <br/>
                <label>Monto total : </label>
                <%= cobranzaDto.getMontoAbono()%>
                <br/>
                <label>Método Pago : </label>
                <%= metodoPagoStr %>
                <br/>
                <label>Estado : </label>
                <%= (cobranzaDto.getIdEstatus()==1)?"Activo":"Cancelado" %>
                
<%            
            
            out.print("<!--EXITO-->");
        }else{
            out.print("<!--ERROR-->No se encontr&oacute; informaci&oacute;n.");
        }
    }else{
        out.print("<!--ERROR-->No se encontr&oacute; informaci&oacute;n.");
    }
    
%>
