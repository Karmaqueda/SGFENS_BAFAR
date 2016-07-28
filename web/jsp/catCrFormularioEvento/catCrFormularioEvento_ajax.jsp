<%-- 
    Document   : catCrFormularioEvento_ajax
    Created on : 28/06/2016, 06:17:54 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.cr.RevisionesCr"%>
<%@page import="com.tsp.sct.dao.jdbc.CrProductoCreditoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrProductoCredito"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int id_producto_solicitado = -1;
    int idCrFormularioEvento = 0;
    String respuestaComentario = ""; //variable para saber porque se cancela o rechaza la solicitud.
    int idEstatusSeleccionado = 0;
    String comentarioEstatusSeleccionado = "";
    
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        id_producto_solicitado = Integer.parseInt(request.getParameter("id_producto_solicitado"));
    }catch(Exception ex){}
    try{
        idCrFormularioEvento = Integer.parseInt(request.getParameter("idCrFormularioEvento"));
    }catch(Exception ex){}
    try{
        idEstatusSeleccionado = Integer.parseInt(request.getParameter("idEstatusSeleccionado"));
    }catch(Exception ex){}
    
    respuestaComentario = request.getParameter("respuestaComentario")!=null?new String(request.getParameter("respuestaComentario").getBytes("ISO-8859-1"),"UTF-8"):"";    
    comentarioEstatusSeleccionado = request.getParameter("comentarioEstatusSeleccionado")!=null?new String(request.getParameter("comentarioEstatusSeleccionado").getBytes("ISO-8859-1"),"UTF-8"):"";    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    /*if(!gc.isValidString(nombre, 1, 50))
        msgError += "<ul>El dato 'nombre' es requerido, debe tener máximo 50 caracteres."; 
    if(!gc.isValidString(descripcion, 1, 500))
        msgError += "<ul>El dato 'descripción' es requerido, debe tener máximo 500 caracteres."; 
    if(idCrGrupoFormulario <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'CrGrupoFormulario' es requerido para ediciones";*/
    
    if(msgError.equals("")){
        if(id_producto_solicitado>0){
            if (mode.equals("1")){
                /*
                * Editar
                */
                
            }else{
                /*
                *  Nuevo
                */
                CrProductoCredito crProductoCreditoDto = null;
                CrProductoCredito crProductoCreditoPadreDto = null;
                CrProductoCreditoDaoImpl crProductoCreditoDaoImpl = new CrProductoCreditoDaoImpl(user.getConn());

                try{
                    crProductoCreditoDto = crProductoCreditoDaoImpl.findByPrimaryKey(id_producto_solicitado);
                }catch(Exception e){}

                if(crProductoCreditoDto != null){
                    crProductoCreditoPadreDto = crProductoCreditoDaoImpl.findByPrimaryKey(crProductoCreditoDto.getIdProductoCreditoPadre());                
                }

                if(crProductoCreditoPadreDto != null && crProductoCreditoPadreDto.getIdGrupoFormularioSolic() > 0 ){
                    out.print("<!--EXITO-->Producto con formulario relacionado");
                }else{
                    out.print("<!--ERROR-->El producto elegido no tiene relacionado un formulario de solicitud");
                }

            }
        }else if(mode.equals("cancelarSolicitud")){
            RevisionesCr revisionesCr = new RevisionesCr();            
            revisionesCr.almacenaRegistroSolicitudBitacora(user.getConn(), idCrFormularioEvento, id_producto_solicitado, idEmpresa, user.getUser().getIdUsuarios(), 9, respuestaComentario, false);
            out.print("<!--EXITO-->Solicitud cancelada satisfactoriamente");
        }else if(mode.equals("rechazarSolicitud")){
            RevisionesCr revisionesCr = new RevisionesCr();            
            revisionesCr.almacenaRegistroSolicitudBitacora(user.getConn(), idCrFormularioEvento, id_producto_solicitado, idEmpresa, user.getUser().getIdUsuarios(), 1, respuestaComentario, false);
            out.print("<!--EXITO-->Solicitud rechazada satisfactoriamente");
        }else if(mode.equals("solicitudCambioEstatus")){
            RevisionesCr revisionesCr = new RevisionesCr();            
            revisionesCr.almacenaRegistroSolicitudBitacora(user.getConn(), idCrFormularioEvento, id_producto_solicitado, idEmpresa, user.getUser().getIdUsuarios(), idEstatusSeleccionado, comentarioEstatusSeleccionado, false);
            out.print("<!--EXITO-->Modificado el estatus de la solicitud satisfactoriamente");
        }else if(mode.equals("aprobarVerificadorSolicitud")){
            RevisionesCr revisionesCr = new RevisionesCr();            
            revisionesCr.almacenaRegistroSolicitudBitacora(user.getConn(), idCrFormularioEvento, id_producto_solicitado, idEmpresa, user.getUser().getIdUsuarios(), 5, respuestaComentario, false);
            out.print("<!--EXITO-->Solicitud aprobada satisfactoriamente");
        }else if(mode.equals("rechazarVerificadorSolicitud")){
            RevisionesCr revisionesCr = new RevisionesCr();            
            revisionesCr.almacenaRegistroSolicitudBitacora(user.getConn(), idCrFormularioEvento, id_producto_solicitado, idEmpresa, user.getUser().getIdUsuarios(), 1, respuestaComentario, false);
            out.print("<!--EXITO-->Solicitud rechazada satisfactoriamente");
        }else {
            out.print("<!--ERROR-->No hay ningun producto valido");
        }
    }else {
        out.print("<!--ERROR-->"+msgError);
    }

%>