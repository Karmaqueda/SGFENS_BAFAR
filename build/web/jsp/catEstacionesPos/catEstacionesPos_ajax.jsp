<%-- 
    Document   : catPosEstacions_ajax
    Created on : 23/06/2015, 11:32:52 AM
    Author     : Cesar Martinez
--%>

<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.PosEstacionBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.PosEstacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.PosEstacion"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idPosEstacion = -1;
    String nombre ="";
    String identificacion ="";  
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idPosEstacion = Integer.parseInt(request.getParameter("idPosEstacion"));
    }catch(NumberFormatException ex){}
    nombre = request.getParameter("nombre")!=null?new String(request.getParameter("nombre").getBytes("ISO-8859-1"),"UTF-8"):"";
    identificacion = request.getParameter("identificacion")!=null?new String(request.getParameter("identificacion").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(identificacion, 2, 15))
        msgError += "<ul>El dato 'identificacion' es requerido, mínimo 2 caracteres, máximo 15.";
    if (!gc.validarConRegex("^[a-zA-Z0-9_]*$", identificacion))
        msgError += "<ul>El dato 'identificacion' es inválido, debe contener solo letras, numeros y guion bajo, sin espacios en blanco.";
    if(!gc.isValidString(nombre, 2, 50))
        msgError += "<ul>El dato 'nombre' es requerido. Mínimo 2 caracteres, máximo 50.";   
    if(idPosEstacion <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'posEstacion' es requerido";

    if(msgError.equals("")){
        if(idPosEstacion>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                PosEstacionBO posEstacionBO = new PosEstacionBO(idPosEstacion,user.getConn());
                PosEstacion posEstacionDto = posEstacionBO.getPosEstacion();
                
                posEstacionDto.setIdEstatus(estatus);
                posEstacionDto.setNombre(nombre);
                posEstacionDto.setIdentificacion(identificacion);
               
                try{
                    new PosEstacionDaoImpl(user.getConn()).update(posEstacionDto.createPk(), posEstacionDto);

                    out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }
                
            }else{
                out.print("<!--ERROR-->Acción no válida.");
            }
        }else{
            /*
            *  Nuevo
            */
            
            try {                
                
                /**
                 * Creamos el registro
                 */
                PosEstacion posEstacionDto = new PosEstacion();
                PosEstacionDaoImpl posEstacionsDaoImpl = new PosEstacionDaoImpl(user.getConn());
                
                posEstacionDto.setIdEstatus(estatus);
                posEstacionDto.setNombre(nombre);
                posEstacionDto.setIdentificacion(identificacion);                              
                posEstacionDto.setIdEmpresa(idEmpresa);

                /**
                 * Realizamos el insert
                 */
                posEstacionsDaoImpl.insert(posEstacionDto);

                out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
            
            }catch(Exception e){
                e.printStackTrace();
                msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
            }
        }
    }else{
        out.print("<!--ERROR-->"+msgError);
    }

%>