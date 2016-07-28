<%-- 
    Document   : pagosCliente
    Created on : 14/01/2013, 02:41:57 PM
    Author     : Luis
--%>

<%@page import="com.tsp.sct.dao.factory.EmpleadoDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.dao.factory.SgfensCobranzaAbonoDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.SgfensCobranzaAbono"%>
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
            clienteDto = ClienteDaoFactory.create().findByPrimaryKey(Integer.parseInt(request.getParameter("id")));
        }catch(Exception e){}
        if(clienteDto!=null){
            
            SgfensCobranzaAbono[] cobranzaDto = SgfensCobranzaAbonoDaoFactory.create().findByDynamicWhere(""
                + "ID_CLIENTE = "+ clienteDto.getIdCliente()+" ORDER BY FECHA_ABONO DESC", null);
            
            if(cobranzaDto.length > 0){
            
                String html = "<table class=\"data\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" >"
                        + "  <thead>"
                        + "        <tr>"
                        + "            <th>Vendedor</th>"
                        + "            <th>Cliente</th>"
                        + "            <th>Fecha</th>"
                        + "            <th>Monto</th>"
                        + "            <th>Ubicar</th>"
                        + "        </tr>"
                        + "    </thead>";

                html +=  "    <tbody>";

                for(SgfensCobranzaAbono cobroDto:cobranzaDto){
                    
                    Empleado empleadoDto = null;
                    try{                                                
                        empleadoDto = EmpleadoDaoFactory.create().findWhereIdUsuariosEquals(cobroDto.getIdUsuarioVendedor())[0];
                    }catch(Exception e){}
                    
                    if(empleadoDto!=null){
                    
                        html += "<tr>"
                            + "<td>"
                            + empleadoDto.getApellidoPaterno() + " " + empleadoDto.getApellidoMaterno() + " " + empleadoDto.getNombre()
                            + "</td>"
                            + "<td>"
                            + clienteDto.getApellidoPaternoCliente() + " " + clienteDto.getApellidoMaternoCliente() + " " + clienteDto.getNombreCliente()
                            + "</td>"
                            + "<td>"
                            + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(cobroDto.getFechaAbono())
                            + "</td>"
                            + "<td>"
                            + cobroDto.getMontoAbono()
                            + "</td>"
                            + "<td>"
                            + "<a href='javascript:void(0)' "
                            + "onclick='agregaMarcador("+cobroDto.getLatitud()+","+cobroDto.getLongitud()+",\"Pago\")'> "
                            + "<img src='../../images/icon_movimiento.png' alt='Ubicar'/></a> "
                            + "</td>"
                            + "<tr>";
                    }
                    
                }

                html +=  "    </tbody>"
                 + "</table><!--EXITO-->";

                out.print(html);
            
            }else{
                out.print("<!--ERROR-->No se encontr&oacute; informaci&oacute;n.");
            }
            
        }else{
            out.print("<!--ERROR-->No se encontr&oacute; informaci&oacute;n.");
        }
    }else{
        out.print("<!--ERROR-->No se encontr&oacute; informaci&oacute;n.");
    }
    
%>
