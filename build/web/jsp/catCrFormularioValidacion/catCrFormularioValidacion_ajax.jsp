<%-- 
    Document   : catCrFormularioValidacion_list
    Created on : 10/06/2016, 12:55:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.bo.CrFormularioValidacionBO"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.CrFormularioValidacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrFormularioValidacion"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idCrFormularioValidacion = -1;
    String nombre ="";
    String descripcion ="";
    String regexJava = "";
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idCrFormularioValidacion = Integer.parseInt(request.getParameter("idCrFormularioValidacion"));
    }catch(Exception ex){}
    nombre = request.getParameter("nombre")!=null?new String(request.getParameter("nombre").getBytes("ISO-8859-1"),"UTF-8"):"";    
    descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";    
    regexJava = request.getParameter("regex_java")!=null?new String(request.getParameter("regex_java").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(Exception ex){} 
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombre, 1, 50))
        msgError += "<ul>El dato 'nombre' es requerido, debe tener máximo 50 caracteres."; 
    if(!gc.isValidString(descripcion, 0, 500))
        msgError += "<ul>El dato 'descripción' es opcional, debe tener máximo 500 caracteres."; 
    if(!gc.isValidString(regexJava, 1, 500))
        msgError += "<ul>El dato 'Expresión Regular JAVA' es requerido, debe tener máximo 500 caracteres."; 
    if(idCrFormularioValidacion <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'CrFormularioValidacion' es requerido para ediciones";
    
    if(msgError.equals("")){
        if(idCrFormularioValidacion>0){
            if (mode.equals("1")){
                /*
                * Editar
                */
                CrFormularioValidacionBO crFormularioValidacionBO = new CrFormularioValidacionBO(idCrFormularioValidacion,user.getConn());
                CrFormularioValidacion crFormularioValidacionDto = crFormularioValidacionBO.getCrFormularioValidacion();

                crFormularioValidacionDto.setNombre(nombre);
                crFormularioValidacionDto.setDescripcion(descripcion);
                crFormularioValidacionDto.setRegexJava(regexJava);
                crFormularioValidacionDto.setRegexLenguajeExt(null);
                //crFormularioValidacionDto.setIsCreadoSistema(0);
                crFormularioValidacionDto.setIdEmpresa(idEmpresa);
                crFormularioValidacionDto.setIdEstatus(estatus);

                try{
                    new CrFormularioValidacionDaoImpl(user.getConn()).update(crFormularioValidacionDto.createPk(), crFormularioValidacionDto);

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
            CrFormularioValidacion crFormularioValidacionDto = new CrFormularioValidacion();
            CrFormularioValidacionDaoImpl crFormularioValidacionDaoImpl = new CrFormularioValidacionDaoImpl(user.getConn());

            crFormularioValidacionDto.setNombre(nombre);
            crFormularioValidacionDto.setDescripcion(descripcion);
            crFormularioValidacionDto.setRegexJava(regexJava);
            crFormularioValidacionDto.setRegexLenguajeExt(null);
            crFormularioValidacionDto.setIsCreadoSistema(0);
            crFormularioValidacionDto.setIdEmpresa(idEmpresa);
            crFormularioValidacionDto.setIdEstatus(estatus);

            try{
                crFormularioValidacionDaoImpl.insert(crFormularioValidacionDto); 

                out.print("<!--EXITO-->Registro creado satisfactoriamente");
            }catch(Exception ex){
                out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                ex.printStackTrace();
            }

        }
    } else {
        out.print("<!--ERROR-->"+msgError);
    }

%>