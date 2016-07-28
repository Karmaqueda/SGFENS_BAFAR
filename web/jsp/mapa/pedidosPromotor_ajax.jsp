<%-- 
    Document   : cobrosPromotor_ajax
    Created on : 10/01/2013, 11:19:55 PM
    Author     : Luis
--%>

<%@page import="com.tsp.sct.dao.factory.SgfensPedidoDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedido"%>
<%@page import="com.tsp.sct.dao.factory.ClienteDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="java.text.SimpleDateFormat"%>
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
            
            SgfensPedido[] pedidosDto = SgfensPedidoDaoFactory.create().findByDynamicWhere(""
                + "ID_USUARIO_VENDEDOR = ? ORDER BY FECHA_PEDIDO DESC", new Object[]{empleadoDto.getIdUsuarios()});
            
            if(pedidosDto.length > 0){
                
                String html = "<table class=\"data\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">"
                        + "  <thead>"
                        + "        <tr>"
                        + "            <th>Pedido</th>"
                        + "            <th>Cliente</th>"
                        + "            <th>Fecha</th>"
                        + "            <th>Total</th>"
                        + "            <th>Ubicar</th>"
                        + "        </tr>"
                        + "    </thead>";

                html +=  "    <tbody>";

                for(SgfensPedido pedidoDto:pedidosDto){
                    
                    Cliente clienteDto = null;
                    try{
                        clienteDto = ClienteDaoFactory.create().findByPrimaryKey(pedidoDto.getIdCliente());
                    }catch(Exception e){}
                    
                    if(clienteDto!=null){
                        
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
                    
                        html += "<tr>"  
                            + "<td>"
                            + pedidoDto.getFolioPedido()
                            + "</td>"
                            + "<td>"
                            + nombrecliente
                            + "</td>"
                            + "<td>"
                            + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(pedidoDto.getFechaPedido())
                            + "</td>"
                            + "<td>"
                            + "$ " + pedidoDto.getTotal()
                            + "</td>"
                            + "<td>"
                            + "<a href='javascript:void(0)' "
                            + "onclick='agregaMarcador("
                            + pedidoDto.getLatitud()+ ","
                            + pedidoDto.getLongitud()+","
                            + "\"Pedido: "+ pedidoDto.getFolioPedido()+"\")' "
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
