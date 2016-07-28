<%-- 
    Document   : catCrScore_list
    Created on : 10/06/2016, 12:55:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.bo.CrScoreBO"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.CrScoreDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrScore"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idCrScore = -1;
    String nombre ="";
    String descripcion ="";
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idCrScore = Integer.parseInt(request.getParameter("idCrScore"));
    }catch(Exception ex){}
    nombre = request.getParameter("nombre")!=null?new String(request.getParameter("nombre").getBytes("ISO-8859-1"),"UTF-8"):"";    
    descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";    
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
    if(!gc.isValidString(descripcion, 1, 500))
        msgError += "<ul>El dato 'descripción' es requerido, debe tener máximo 500 caracteres."; 
    if(idCrScore <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'CrScore' es requerido para ediciones";
    
    if(msgError.equals("")){
        if(idCrScore>0){
            if (mode.equals("1")){
                /*
                * Editar
                */
                CrScoreBO crScoreBO = new CrScoreBO(idCrScore,user.getConn());
                CrScore crScoreDto = crScoreBO.getCrScore();

                crScoreDto.setNombre(nombre);
                crScoreDto.setDescripcion(descripcion);
                crScoreDto.setFechaHrUltimaEdicion(new Date());
                crScoreDto.setIdUsuarioEdicion(user.getUser().getIdUsuarios());
                crScoreDto.setIdEmpresa(idEmpresa);
                crScoreDto.setIdEstatus(estatus);

                try{
                    new CrScoreDaoImpl(user.getConn()).update(crScoreDto.createPk(), crScoreDto);

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
            CrScore crScoreDto = new CrScore();
            CrScoreDaoImpl crScoreDaoImpl = new CrScoreDaoImpl(user.getConn());

            crScoreDto.setNombre(nombre);
            crScoreDto.setDescripcion(descripcion);
            crScoreDto.setFechaHrCreacion(new Date());
            crScoreDto.setIdUsuarioEdicion(user.getUser().getIdUsuarios());
            crScoreDto.setIdEmpresa(idEmpresa);
            crScoreDto.setIdEstatus(estatus);

            try{
                crScoreDaoImpl.insert(crScoreDto); 

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