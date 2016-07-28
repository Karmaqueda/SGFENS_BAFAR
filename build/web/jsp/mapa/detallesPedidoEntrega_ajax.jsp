<%-- 
    Created on : 29/01/2016, 02:49:13 PM
    Author     : Cesar
--%>


<%@page import="com.tsp.sct.bo.SGPedidoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedido"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.SGPedidoEntregaBO"%>
<%@page import="com.tsp.sct.dao.factory.SgfensPedidoEntregaDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedidoEntrega"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.dao.dto.DatosUsuario"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    long id = 0l;
    try{
        id = Long.parseLong(request.getParameter("id"));
    }catch(Exception e){}
    if(id > 0){
        SgfensPedidoEntrega entregaDto = null;
        try{
            entregaDto = SgfensPedidoEntregaDaoFactory.create(user.getConn()).findByPrimaryKey(Integer.parseInt(request.getParameter("id")));
        }catch(Exception e){}
        if(entregaDto!=null){
            
            Empleado emp = null;
            String nombreEmpleado = "";
            try{
                EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
                emp = empleadoBO.findEmpleadoByUsuario(entregaDto.getIdUsuarioVendedor());
                nombreEmpleado = emp.getNombre() + " " + emp.getApellidoPaterno() + " " + emp.getApellidoMaterno();
            }catch(Exception e){}
            
            Cliente cliente = null;
            SGPedidoBO pedidoBO = new SGPedidoBO(user.getConn());
            SgfensPedido pedidoDto = null;
            double montoPedido = 0;
            try{
                pedidoDto = pedidoBO.findPedidobyId(entregaDto.getIdPedido());
                if (pedidoDto!=null){
                    cliente = new ClienteBO(pedidoDto.getIdCliente(),user.getConn()).getCliente();
                    montoPedido = pedidoDto.getTotal();
                }
            }catch(Exception ex){}
            
                                                        
            //out.print("");      
%>
                <h3>Información de Entrega</h3>
                
                <label>ID : </label>
                <%= entregaDto.getIdPedidoEntrega()%>
                <br/>
                <label>Fecha Registro : </label>
                <%= new SimpleDateFormat("dd/MM/yyyy HH:mm").format(entregaDto.getFechaHora())%>
                <br/>
                <label>Cliente : </label>
                <%= cliente!=null?cliente.getRazonSocial() : ""%>
                <br/>
                <label>Vendedor : </label>
                <%= nombreEmpleado %>
                <br/>
                <label>Cantidad productos entregados : </label>
                <%= entregaDto.getTotalCantEntregada() %>
                <br/>
                <label>ID Pedido : </label>
                <%= entregaDto.getIdPedido() %>
                <br/>
                <label>Monto Pedido Original ($) : </label>
                <%= montoPedido %>
                <br/>
                
<%            
            
            out.print("<!--EXITO-->");
        }else{
            out.print("<!--ERROR-->No se encontr&oacute; informaci&oacute;n.");
        }
    }else{
        out.print("<!--ERROR-->No se encontr&oacute; informaci&oacute;n.");
    }
    
%>
