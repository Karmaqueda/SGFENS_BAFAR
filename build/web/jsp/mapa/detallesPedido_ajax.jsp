<%-- 
    Created on : 30/11/2015, 02:49:13 PM
    Author     : Cesar
--%>

<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.dao.dto.DatosUsuario"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensPedidoProductoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedidoProducto"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.bo.SGEstatusPedidoBO"%>
<%@page import="com.tsp.sct.bo.SGPedidoBO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.dao.factory.SgfensPedidoDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedido"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    long id = 0l;
    try{
        id = Long.parseLong(request.getParameter("id"));
    }catch(Exception e){}
    if(id > 0){
        SgfensPedido pedidoDto = null;
        try{
            pedidoDto = SgfensPedidoDaoFactory.create(user.getConn()).findByPrimaryKey(Integer.parseInt(request.getParameter("id")));
        }catch(Exception e){}
        if(pedidoDto!=null){
            SGPedidoBO pedidoBO = new SGPedidoBO(user.getConn());
            String estatus = "";
            try{
                estatus =  SGEstatusPedidoBO.getEstatusName(pedidoDto.getIdEstatusPedido());
            }catch(Exception e){}
            Cliente cliente = null;
            try{ cliente = new ClienteBO(pedidoDto.getIdCliente(),user.getConn()).getCliente(); }catch(Exception ex){}
            SgfensPedidoProducto[] spp = new SgfensPedidoProductoDaoImpl(user.getConn()).findWhereIdPedidoEquals(pedidoDto.getIdPedido());
            int cantidadProductos = spp.length;
            double adeudo = pedidoDto.getTotal() - pedidoDto.getSaldoPagado();
            DatosUsuario datosUsuarioVendedor = new UsuarioBO(pedidoDto.getIdUsuarioVendedor()).getDatosUsuario();
                                                        
            //out.print("");      
%>
                <h3>Detalles de Pedido</h3>
                
                <label>ID : </label>
                <%= pedidoDto.getIdPedido()%>
                <br/>
                <label>Fecha Registro : </label>
                <%= new SimpleDateFormat("dd/MM/yyyy HH:mm").format(pedidoDto.getFechaPedido())%>
                <br/>
                <label>Cliente : </label>
                <%= cliente.getRazonSocial()%>
                <br/>
                <label>Vendedor : </label>
                <%= datosUsuarioVendedor!=null? (datosUsuarioVendedor.getNombre() +" " + datosUsuarioVendedor.getApellidoPat()) :"Sin vendedor asignado" %>
                <br/>
                <label>Monto total : </label>
                <%= pedidoDto.getTotal()%>
                <br/>
                <label>Monto Pagado : </label>
                <%= pedidoDto.getSaldoPagado()%>
                <br/>
                <label>Monto Adeudado : </label>
                <%=  adeudo %>
                <br/>
                <label>Cantidad Productos : </label>
                <%= cantidadProductos %>
                <br/>
                <label>Estado : </label>
                <%= estatus %>
                
<%            
            
            out.print("<!--EXITO-->");
        }else{
            out.print("<!--ERROR-->No se encontr&oacute; informaci&oacute;n.");
        }
    }else{
        out.print("<!--ERROR-->No se encontr&oacute; informaci&oacute;n.");
    }
    
%>
