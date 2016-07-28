<%-- 
    Document   : catSmsAgendaGrupos_list
    Created on : 10/03/2016, 12:55:48 PM
    Author     : ISCesar
--%>

<%@page import="java.util.regex.Matcher"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.bo.SmsAgendaGrupoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.SmsAgendaGrupoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SmsAgendaGrupo"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idSmsAgendaGrupo = -1;
    String nombre ="";
    String descripcion =""; 
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idSmsAgendaGrupo = Integer.parseInt(request.getParameter("idSmsAgendaGrupo"));
    }catch(NumberFormatException ex){}
    nombre = request.getParameter("nombre")!=null?new String(request.getParameter("nombre").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombre, 1, 50))
        msgError += "<ul>El dato 'nombre' es requerido.";
    if(!gc.isValidString(descripcion, 1, 250))
        msgError += "<ul>El dato 'descripcion' es requerido";   
    if(idSmsAgendaGrupo <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'SmsAgendaGrupo' es requerido para ediciones";

    if(msgError.equals("")){
        if(idSmsAgendaGrupo>0){
            if (mode.equals("1")){
                /*
                * Editar
                */
                SmsAgendaGrupoBO smsAgendaGrupoBO = new SmsAgendaGrupoBO(idSmsAgendaGrupo,user.getConn());
                SmsAgendaGrupo smsAgendaGrupoDto = smsAgendaGrupoBO.getSmsAgendaGrupo();

                smsAgendaGrupoDto.setNombreGrupo(nombre);
                smsAgendaGrupoDto.setDescripcionGrupo(descripcion);
                smsAgendaGrupoDto.setIdEmpresa(idEmpresa);
                smsAgendaGrupoDto.setIdEstatus(estatus);

                try{
                    new SmsAgendaGrupoDaoImpl(user.getConn()).update(smsAgendaGrupoDto.createPk(), smsAgendaGrupoDto);

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
            SmsAgendaGrupo smsAgendaGrupoDto = new SmsAgendaGrupo();
            SmsAgendaGrupoDaoImpl smsAgendaGruposDaoImpl = new SmsAgendaGrupoDaoImpl(user.getConn());

            smsAgendaGrupoDto.setNombreGrupo(nombre);
            smsAgendaGrupoDto.setDescripcionGrupo(descripcion);
            smsAgendaGrupoDto.setIdEmpresa(idEmpresa);
            smsAgendaGrupoDto.setIdEstatus(estatus);

            try{
                smsAgendaGruposDaoImpl.insert(smsAgendaGrupoDto); 

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