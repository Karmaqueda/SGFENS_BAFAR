<%-- 
    Document   : catZonaHoraria_ajax
    Created on : 4/02/2016, 06:00:36 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.dao.jdbc.ZonaHorariaLogDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.ZonaHorariaLog"%>
<%@page import="com.tsp.sct.dao.jdbc.ZonaHorariaDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.ZonaHoraria"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.ZonaHorariaCatalogoBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.ZonaHorariaCatalogoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.ZonaHorariaCatalogo"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idZonaHorariaCatalogo = -1;   
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idZonaHorariaCatalogo = Integer.parseInt(request.getParameter("idZonaHorariaCatalogo"));
    }catch(NumberFormatException ex){}
      
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
   
    if(msgError.equals("")){
        if(idZonaHorariaCatalogo>0){            
            /*
            * Asignar Zona Horaria
            */                
                ZonaHoraria zonaHorariaDto = null;
                ZonaHorariaDaoImpl zonaHorariaDaoImpl = new ZonaHorariaDaoImpl(user.getConn());
                
                try{
                zonaHorariaDto = zonaHorariaDaoImpl.findByDynamicWhere(" ID_EMPRESA = " + idEmpresa , null)[0];}catch(Exception e){}
                
                if(zonaHorariaDto == null){//insertamos nueva
                    zonaHorariaDto = new ZonaHoraria();
                    zonaHorariaDto.setIdEmpresa(idEmpresa);
                    zonaHorariaDto.setIdZonaHorariaCatalogo(idZonaHorariaCatalogo);
                    zonaHorariaDto.setFecha(new Date());
                    try{
                        zonaHorariaDaoImpl.insert(zonaHorariaDto);                        
                        ZonaHorariaLog horariaLog = new ZonaHorariaLog();
                        horariaLog.setIdEmpresa(idEmpresa);
                        horariaLog.setIdUsuarios(user.getUser().getIdUsuarios());
                        horariaLog.setDescripcion("Alta de Zona Horaria, la zona horaria seleccionada es la de ID "+ idZonaHorariaCatalogo);
                        horariaLog.setFecha(new Date());
                        new ZonaHorariaLogDaoImpl(user.getConn()).insert(horariaLog);
                        
                        out.print("<!--EXITO-->Registro creado satisfactoriamente");
                    }catch(Exception ex){
                        out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                        ex.printStackTrace();
                    }
                }else{//actualizamos los datos si ya existia
                    zonaHorariaDto.setIdEmpresa(idEmpresa);
                    zonaHorariaDto.setIdZonaHorariaCatalogo(idZonaHorariaCatalogo);
                    zonaHorariaDto.setFecha(new Date());
                    try{
                        zonaHorariaDaoImpl.update(zonaHorariaDto.createPk(), zonaHorariaDto);
                        ZonaHorariaLog horariaLog = new ZonaHorariaLog();
                        horariaLog.setIdEmpresa(idEmpresa);
                        horariaLog.setIdUsuarios(user.getUser().getIdUsuarios());
                        horariaLog.setDescripcion("Actualizacion de Zona Horaria, la zona horaria seleccionada es la de ID "+ idZonaHorariaCatalogo);
                        horariaLog.setFecha(new Date());
                        new ZonaHorariaLogDaoImpl(user.getConn()).insert(horariaLog);
                        out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
                    }catch(Exception ex){
                        out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                        ex.printStackTrace();
                    }
                }               
                
        }else{
            /*
            *  borrar zona horaria
            */
            
            try {                
                
                /**
                 * Creamos el registro de Cliente
                 */
                ZonaHoraria zonaHorariaDto = null;
                ZonaHorariaDaoImpl zonaHorariaDaoImpl = new ZonaHorariaDaoImpl(user.getConn());
                
                try{
                zonaHorariaDto = zonaHorariaDaoImpl.findByDynamicWhere(" ID_EMPRESA = " + idEmpresa , null)[0];}catch(Exception e){}
                
                if(zonaHorariaDto != null){
                    zonaHorariaDaoImpl.delete(zonaHorariaDto.createPk());// crear la llave primaria sea ID de empresa para poder eliminar el registro
                    //insertamos el log
                    ZonaHorariaLog horariaLog = new ZonaHorariaLog();
                    horariaLog.setIdEmpresa(idEmpresa);
                    horariaLog.setIdUsuarios(user.getUser().getIdUsuarios());
                    horariaLog.setDescripcion("Eliminacion de Zona Horaria, el usuario con ID " + user.getUser().getIdUsuarios() + " elimino la zona horaria");
                    horariaLog.setFecha(new Date());
                    new ZonaHorariaLogDaoImpl(user.getConn()).insert(horariaLog);
                }
                
                /**
                 * Realizamos el insert
                 */
                
                out.print("<!--EXITO-->Registro actualizado satisfactoriamente.<br/>");
            
            }catch(Exception e){
                e.printStackTrace();
                msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
            }
        }
    }else{
        out.print("<!--ERROR-->"+msgError);
    }

%>