<%-- 
    Document   : catSmsAgendaDestinatarios_list
    Created on : 10/03/2016, 12:55:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.bo.SmsAgendaDestinatarioBO"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.SmsAgendaDestinatarioDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SmsAgendaDestinatario"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idSmsAgendaDestinatario = -1;
    String numero_celular ="";
    String nombre =""; 
    String campo_extra_1 = "";
    String campo_extra_2 = "";
    String campo_extra_3 = "";
    String campo_extra_4 = "";
    int id_sms_agenda_grupo = 0;
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idSmsAgendaDestinatario = Integer.parseInt(request.getParameter("idSmsAgendaDestinatario"));
    }catch(Exception ex){}
    numero_celular = request.getParameter("numero_celular")!=null?new String(request.getParameter("numero_celular").getBytes("ISO-8859-1"),"UTF-8"):"";
    nombre = request.getParameter("nombre")!=null?new String(request.getParameter("nombre").getBytes("ISO-8859-1"),"UTF-8"):"";    
    campo_extra_1 = request.getParameter("campo_extra_1")!=null?new String(request.getParameter("campo_extra_1").getBytes("ISO-8859-1"),"UTF-8"):"";    
    campo_extra_2 = request.getParameter("campo_extra_2")!=null?new String(request.getParameter("campo_extra_2").getBytes("ISO-8859-1"),"UTF-8"):"";    
    campo_extra_3 = request.getParameter("campo_extra_3")!=null?new String(request.getParameter("campo_extra_3").getBytes("ISO-8859-1"),"UTF-8"):"";    
    campo_extra_4 = request.getParameter("campo_extra_4")!=null?new String(request.getParameter("campo_extra_4").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(Exception ex){} 
    try{
        id_sms_agenda_grupo = Integer.parseInt(request.getParameter("id_sms_agenda_grupo"));
    }catch(Exception ex){} 
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(numero_celular, 1, 10))
        msgError += "<ul>El dato 'numero celular' es requerido y debe tener máximo 10 dígitos.";
    if(!gc.validarConRegex("[0-9]{10}", numero_celular))
        msgError += "<ul>El dato 'numero celular' debe contener solo numeros, deben ser 10 digitos, y no debe incluir ningun simbolo, puntuación, ni espacio en blanco.";
    if (!StringManage.getValidString(nombre).equals("")){
        if(!gc.isValidString(nombre, 1, 100))
            msgError += "<ul>El dato 'nombre' es opcional, debe tener máximo 100 caracteres."; 
    }
    if (!StringManage.getValidString(campo_extra_1).equals("")){
        if(!gc.isValidString(campo_extra_1, 1, 100))
            msgError += "<ul>El dato 'Campo 1' es opcional, debe tener máximo 100 caracteres."; 
    }
    if (!StringManage.getValidString(campo_extra_2).equals("")){
        if(!gc.isValidString(campo_extra_2, 1, 100))
            msgError += "<ul>El dato 'campo 2' es opcional, debe tener máximo 100 caracteres."; 
    }
    if (!StringManage.getValidString(campo_extra_3).equals("")){
        if(!gc.isValidString(campo_extra_3, 1, 100))
            msgError += "<ul>El dato 'Campo 3' es opcional, debe tener máximo 100 caracteres."; 
    }
    if (!StringManage.getValidString(campo_extra_4).equals("")){
        if(!gc.isValidString(campo_extra_4, 1, 100))
            msgError += "<ul>El dato 'Campo 4' es opcional, debe tener máximo 100 caracteres."; 
    }
    if(idSmsAgendaDestinatario <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'SmsAgendaDestinatario' es requerido para ediciones";

    //Validacion de Celular que no este repetido en la misma empresa (solo al crear uno nuevo)
    /*
    if(msgError.equals("") && idSmsAgendaDestinatario <= 0 && mode.equals("")){
        SmsAgendaDestinatarioBO smsAgendaDestinatarioBO = new SmsAgendaDestinatarioBO(user.getConn());
        int coincidenciasDeCeularEnEmpresa = smsAgendaDestinatarioBO.findCantidadSmsAgendaDestinatarios(-1, idEmpresa, 0, 0, " AND numero_celular='" + numero_celular + "'");
        if (coincidenciasDeCeularEnEmpresa>0){
            msgError += "<ul>El numéro de celular que intenta registrar ya existe en la base de datos de esta Empresa (Matriz y/o Sucursales)";
        }
    }
    */
    
    if(msgError.equals("")){
        if(idSmsAgendaDestinatario>0){
            if (mode.equals("1")){
                /*
                * Editar
                */
                SmsAgendaDestinatarioBO smsAgendaDestinatarioBO = new SmsAgendaDestinatarioBO(idSmsAgendaDestinatario,user.getConn());
                SmsAgendaDestinatario smsAgendaDestinatarioDto = smsAgendaDestinatarioBO.getSmsAgendaDestinatario();

                smsAgendaDestinatarioDto.setNumeroCelular(numero_celular);
                smsAgendaDestinatarioDto.setNombre(nombre);
                smsAgendaDestinatarioDto.setCampoExtra1(campo_extra_1);
                smsAgendaDestinatarioDto.setCampoExtra2(campo_extra_2);
                smsAgendaDestinatarioDto.setCampoExtra3(campo_extra_3);
                smsAgendaDestinatarioDto.setCampoExtra4(campo_extra_4);
                smsAgendaDestinatarioDto.setIdSmsAgendaGrupo(id_sms_agenda_grupo);
                smsAgendaDestinatarioDto.setIdEmpresa(idEmpresa);
                smsAgendaDestinatarioDto.setIdEstatus(estatus);

                try{
                    new SmsAgendaDestinatarioDaoImpl(user.getConn()).update(smsAgendaDestinatarioDto.createPk(), smsAgendaDestinatarioDto);

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
            SmsAgendaDestinatario smsAgendaDestinatarioDto = new SmsAgendaDestinatario();
            SmsAgendaDestinatarioDaoImpl smsAgendaDestinatariosDaoImpl = new SmsAgendaDestinatarioDaoImpl(user.getConn());

            smsAgendaDestinatarioDto.setNumeroCelular(numero_celular);
            smsAgendaDestinatarioDto.setNombre(nombre);
            smsAgendaDestinatarioDto.setCampoExtra1(campo_extra_1);
            smsAgendaDestinatarioDto.setCampoExtra2(campo_extra_2);
            smsAgendaDestinatarioDto.setCampoExtra3(campo_extra_3);
            smsAgendaDestinatarioDto.setCampoExtra4(campo_extra_4);
            smsAgendaDestinatarioDto.setIdSmsAgendaGrupo(id_sms_agenda_grupo);
            smsAgendaDestinatarioDto.setIdEmpresa(idEmpresa);
            smsAgendaDestinatarioDto.setIdEstatus(estatus);

            try{
                smsAgendaDestinatariosDaoImpl.insert(smsAgendaDestinatarioDto); 

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