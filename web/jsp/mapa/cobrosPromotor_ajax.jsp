<%-- 
    Document   : cobrosPromotor_ajax
    Created on : 10/01/2013, 11:19:55 PM
    Author     : Luis
--%>

<%@page import="com.tsp.sct.dao.factory.ClienteDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.dao.factory.SgfensCobranzaAbonoDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.SgfensCobranzaAbono"%>
<%@page import="com.tsp.sct.dao.factory.EmpleadoDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%

long id = 0l;
    try{
        id = Long.parseLong(request.getParameter("id"));
    }catch(Exception e){}
    if(id > 0){
        
        Empleado empleadoDto = null;
        try{
            empleadoDto = EmpleadoDaoFactory.create().findByPrimaryKey(Integer.parseInt(request.getParameter("id")));
        }catch(Exception e){}
        if(empleadoDto!=null){
            
            SgfensCobranzaAbono[] cobranzaDto = SgfensCobranzaAbonoDaoFactory.create().findByDynamicWhere(""
                + "ID_USUARIO_VENDEDOR = ? ORDER BY FECHA_ABONO DESC", new Object[]{empleadoDto.getIdEmpleado()});
            
            if(cobranzaDto.length > 0){
                
                String html = "<table class=\"data\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">"
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
                    
                    Cliente clienteDto = null;
                    try{
                        clienteDto = ClienteDaoFactory.create().findByPrimaryKey(cobroDto.getIdCliente());
                    }catch(Exception e){}
                    
                    if(clienteDto!=null){
                    
                        html += "<tr>"
                            + "<td>"
                            + empleadoDto.getApellidoPaterno() + " " + empleadoDto.getApellidoMaterno() + " " + empleadoDto.getNombre()
                            + "</td>"
                            + "<td>"
                            + clienteDto.getApellidoPaternoCliente()+ " " + clienteDto.getApellidoMaternoCliente() + " " + clienteDto.getNombreCliente()
                            + "</td>"
                            + "<td>"
                            + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(cobroDto.getFechaAbono())
                            + "</td>"
                            + "<td>"
                            + cobroDto.getMontoAbono()
                            + "</td>"
                            + "<td>"
                            + "<a href='javascript:void(0)' "
                            + "onclick='agregaMarcador("
                            + cobroDto.getLatitud()+ ","
                            + cobroDto.getLongitud()+","
                            + "\"Cobro\")' "
                            + ")\""
                        + "> "
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
