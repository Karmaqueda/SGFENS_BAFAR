<%-- 
    Document   : logistica_eliminar_ajax
    Created on : 17/01/2013, 09:38:35 PM
    Author     : Luis
--%>

<%@page import="com.tsp.sct.dao.dao.RutaMarcadorDao"%>
<%@page import="com.tsp.sct.dao.factory.RutaMarcadorDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.RutaMarcador"%>
<%@page import="com.tsp.sct.dao.dto.Ruta"%>
<%@page import="com.tsp.sct.dao.factory.RutaDaoFactory"%>
<%@page import="com.tsp.sct.dao.dao.RutaDao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
    int idRuta = 0;
    try{
        idRuta = Integer.parseInt(request.getParameter("id"));
    }catch(Exception e){}
    if(idRuta>0){
    
        String mode = "";
        mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
        
        if(mode.equals("eliminarAsignacion")){
            RutaDao rutaDao = RutaDaoFactory.create(user.getConn());
            Ruta rutaDto = rutaDao.findByPrimaryKey(idRuta);
            rutaDto.setIdEmpleado(0);
            try{
                rutaDao.update(rutaDto.createPk(), rutaDto);
                out.print("<!--EXITO-->Registro borrado satisfactoriamente");
            }catch(Exception e){
                out.print("<!--ERROR-->No se pudo borrar el registro. Informe del error al administrador del sistema: " + e.toString());                
            }
        }else{
            RutaDao rutaDao = RutaDaoFactory.create(user.getConn());
            try{
                RutaMarcadorDao rutaMarcadorDao = RutaMarcadorDaoFactory.create(user.getConn());
                RutaMarcador[] rutaMarcadorDto = rutaMarcadorDao.findWhereIdRutaEquals(idRuta);
                for(RutaMarcador marcador:rutaMarcadorDto){
                    rutaMarcadorDao.delete(marcador.createPk());
                }

                Ruta rutaDto = rutaDao.findByPrimaryKey(idRuta);
                rutaDao.delete(rutaDto.createPk());

                out.print("<!--EXITO-->Registro borrado satisfactoriamente");

            }catch(Exception e){
                out.print("<!--ERROR-->No se pudo borrar el registro. Informe del error al administrador del sistema: " + e.toString());
            }
        }
    }else{
        out.print("<!--ERROR-->No se pudo borrar el registro. No se recibió un objeto.");
    }
%>
