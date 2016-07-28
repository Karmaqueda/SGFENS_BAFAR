<%-- 
    Created on : 30/11/2015, 02:49:13 PM
    Author     : Cesar
--%>


<%@page import="com.tsp.sct.bo.SGPedidoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedido"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.SGPedidoDevolucionesCambioBO"%>
<%@page import="com.tsp.sct.dao.factory.SgfensPedidoDevolucionCambioDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedidoDevolucionCambio"%>
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
        SgfensPedidoDevolucionCambio devolucionDto = null;
        try{
            devolucionDto = SgfensPedidoDevolucionCambioDaoFactory.create(user.getConn()).findByPrimaryKey(Integer.parseInt(request.getParameter("id")));
        }catch(Exception e){}
        if(devolucionDto!=null){
            
            Empleado emp = null;
            String nombreEmpleado = "";
            try{
                emp = new EmpleadoBO(devolucionDto.getIdEmpleado(), user.getConn()).getEmpleado();
                nombreEmpleado = emp.getNombre() + " " + emp.getApellidoPaterno() + " " + emp.getApellidoMaterno();
            }catch(Exception e){}

            Concepto con = null;
            ConceptoBO conBO =  null;
            try{
                conBO = new ConceptoBO(devolucionDto.getIdConcepto(), user.getConn());
                con = conBO.getConcepto();                                                        
            }catch(Exception e){}

            Concepto conEntregado = null;
            try{
                conBO = new ConceptoBO(devolucionDto.getIdConceptoEntregado(), user.getConn());
                conEntregado = conBO.getConcepto();
            }catch(Exception e){}
            
            Cliente cliente = null;
            SGPedidoBO pedidoBO = new SGPedidoBO(user.getConn());
            try{
                SgfensPedido pedidoDto = pedidoBO.findPedidobyId(devolucionDto.getIdPedido());
                if (pedidoDto!=null){
                    cliente = new ClienteBO(pedidoDto.getIdCliente(),user.getConn()).getCliente();
                }
            }catch(Exception ex){}
            
                                                        
            //out.print("");      
%>
                <h3>Detalles de Devolucion</h3>
                
                <label>ID : </label>
                <%= devolucionDto.getIdPedidoDevolCambio()%>
                <br/>
                <label>Fecha Registro : </label>
                <%= new SimpleDateFormat("dd/MM/yyyy HH:mm").format(devolucionDto.getFecha())%>
                <br/>
                <label>Cliente : </label>
                <%= cliente!=null?cliente.getRazonSocial() : ""%>
                <br/>
                <label>Vendedor : </label>
                <%= nombreEmpleado %>
                <br/>
                <label>Tipo : </label>
                <%= devolucionDto.getIdTipo()==1 ? "Devolución" : devolucionDto.getIdTipo()==2 ? "Cambio" : "" %>
                <br/>
                <label>Monto Resultante : </label>
                <%= devolucionDto.getMontoResultante() + " " +  (devolucionDto.getDiferenciaFavor()==0?"" : devolucionDto.getDiferenciaFavor()==1?"A Favor de Cliente" : devolucionDto.getDiferenciaFavor()==2?"En Contra de Cliente":"") %>
                <br/>
                <label>Producto Original : </label>
                <%= con!=null?conBO.desencripta(con.getNombre()):"" %>
                <br/>
                <label>Producto a Cambio : </label>
                <%= conEntregado!=null?conBO.desencripta(conEntregado.getNombre()):"" %>
                
<%            
            
            out.print("<!--EXITO-->");
        }else{
            out.print("<!--ERROR-->No se encontr&oacute; informaci&oacute;n.");
        }
    }else{
        out.print("<!--ERROR-->No se encontr&oacute; informaci&oacute;n.");
    }
    
%>
