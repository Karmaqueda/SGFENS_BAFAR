<%-- 
    Document   : envioMensajePromotor_ajax
    Created on : 15/01/2013, 02:45:12 PM
    Author     : Luis
--%>

<%@page import="com.tsp.sct.dao.factory.MovilMensajeDaoFactory"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.dto.MovilMensaje"%>
<%@page import="com.tsp.sct.dao.factory.EmpleadoDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
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
            
            String mensaje = request.getParameter("txt_mensaje");
            if(mensaje!=null && !mensaje.trim().equals("")){
            
                MovilMensaje mensajeDto = new MovilMensaje();
                mensajeDto.setEmisorTipo(2);
                mensajeDto.setFechaEmision(new Date());
                mensajeDto.setIdEmpleadoEmisorNull(true);
                mensajeDto.setIdEmpleadoReceptor(Integer.parseInt(""+id));
                mensajeDto.setReceptorTipo(1);
                mensajeDto.setMensaje(mensaje);
                
                try{
                    MovilMensajeDaoFactory.create().insert(mensajeDto);
                    out.print("<!--EXITO-->Mensaje enviado satisfactoriamente");
                }catch(Exception e){
                    out.print("<!--ERROR-->Ocurri&oacute; un error al almacenar el mensaje.");
                }
                
            }else{
                out.print("<!--ERROR-->Debe indicar el mensaje a enviar.");
            }
            
        }else{
            out.print("<!--ERROR-->No se encontr&oacute; informaci&oacute;n.");
        }
    }else{
        out.print("<!--ERROR-->No se encontr&oacute; informaci&oacute;n.");
    }
%>