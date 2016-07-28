<%-- 
    Created on : 30/11/2015, 02:49:13 PM
    Author     : Cesar
--%>


<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoAgendaDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoAgenda"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.SGVisitaClienteBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.dao.dto.DatosUsuario"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.dao.factory.SgfensVisitaClienteDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.SgfensVisitaCliente"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    long id = 0l;
    try{
        id = Long.parseLong(request.getParameter("id"));
    }catch(Exception e){}
    if(id > 0){
        SgfensVisitaCliente visitaDto = null;
        try{
            visitaDto = SgfensVisitaClienteDaoFactory.create(user.getConn()).findByPrimaryKey(Integer.parseInt(request.getParameter("id")));
        }catch(Exception e){}
        if(visitaDto!=null){
            SGVisitaClienteBO visitaBO = new SGVisitaClienteBO(user.getConn());
            Cliente cliente = null;
            try{ cliente = new ClienteBO(visitaDto.getIdCliente(),user.getConn()).getCliente(); }catch(Exception ex){}
            
            Empleado emp =  null;                                        
            try{ emp = new EmpleadoBO(visitaDto.getIdEmpleadoVendedor(), user.getConn()).getEmpleado(); }catch(Exception ex){} 
            
            EmpleadoAgenda proximaVisitaAgenda = null;
            try{
                if (visitaDto.getIdCliente() > 0)
                    proximaVisitaAgenda = new EmpleadoAgendaDaoImpl(user.getConn()).findByDynamicWhere("id_cliente=" + visitaDto.getIdCliente()+ " AND FECHA_PROGRAMADA > '" + DateManage.formatDateToSQL(visitaDto.getFechaHora()) + "' ORDER BY FECHA_PROGRAMADA ASC LIMIT 0,1", null)[0];
            }catch(Exception ex){
            }
                                                        
            //out.print("");      
%>
                <h3>Detalles de Visita</h3>
                
                <label>ID : </label>
                <%= visitaDto.getIdVisita()%>
                <br/>
                <label>Fecha Registro : </label>
                <%= new SimpleDateFormat("dd/MM/yyyy HH:mm").format(visitaDto.getFechaHora())%>
                <br/>
                <label>Cliente : </label>
                <%= cliente.getRazonSocial()%>
                <br/>
                <label>Vendedor : </label>
                <%= emp!=null?(emp.getNombre()+" "+emp.getApellidoPaterno() + " " + emp.getApellidoMaterno()):"" %>
                <br/>
                <label>Motivo : </label>
                <%= visitaDto.getIdOpcion()==1?"Local Cerrado":visitaDto.getIdOpcion()==2?"Sin Dinero":visitaDto.getIdOpcion()==3?"Aún cuenta con mercancía":visitaDto.getIdOpcion()==4?"Otro : " + visitaDto.getComentarios():"" %>
                <br/>
                <label>Fecha próxima visita : </label>
                <%= proximaVisitaAgenda!=null? proximaVisitaAgenda.getFechaProgramada() : "Sin Visita en Agenda para proximos Días" %>
                
<%            
            
            out.print("<!--EXITO-->");
        }else{
            out.print("<!--ERROR-->No se encontr&oacute; informaci&oacute;n.");
        }
    }else{
        out.print("<!--ERROR-->No se encontr&oacute; informaci&oacute;n.");
    }
    
%>
