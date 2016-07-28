<%-- 
    Document   : catSmsPlantillas_list
    Created on : 10/03/2016, 12:55:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.bo.ZonaHorariaBO"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.SmsPlantillaBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.SmsPlantillaDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SmsPlantilla"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idSmsPlantilla = -1;
    String asunto ="";
    String contenido =""; 
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idSmsPlantilla = Integer.parseInt(request.getParameter("idSmsPlantilla"));
    }catch(NumberFormatException ex){}
    asunto = request.getParameter("asunto")!=null?new String(request.getParameter("asunto").getBytes("ISO-8859-1"),"UTF-8"):"";
    contenido = request.getParameter("contenido")!=null?new String(request.getParameter("contenido").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(asunto, 1, 50))
        msgError += "<ul>El dato 'asunto' es requerido.";
    if(!gc.isValidString(contenido, 1, 480))
        msgError += "<ul>El dato 'contenido' es requerido";   
    if(idSmsPlantilla <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'SmsPlantilla' es requerido para ediciones";

    if(msgError.equals("")){
        if(idSmsPlantilla>0){
            if (mode.equals("1")){
                /*
                * Editar
                */
                SmsPlantillaBO smsPlantillaBO = new SmsPlantillaBO(idSmsPlantilla,user.getConn());
                SmsPlantilla smsPlantillaDto = smsPlantillaBO.getSmsPlantilla();

                smsPlantillaDto.setAsunto(asunto);
                smsPlantillaDto.setContenido(contenido);
                smsPlantillaDto.setIdEstatus(estatus);
                //smsPlantillaDto.setAliasPlantilla("");
                smsPlantillaDto.setIdEmpresa(idEmpresa);
                smsPlantillaDto.setIsFijoSistema(0);
                smsPlantillaDto.setIdUsuarioPreto(user.getUser().getIdUsuarios());
                smsPlantillaDto.setIdUsuarioVentas(0);
                //smsPlantillaDto.setFechaHrCreado(new Date());

                try{
                    new SmsPlantillaDaoImpl(user.getConn()).update(smsPlantillaDto.createPk(), smsPlantillaDto);

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
            SmsPlantilla smsPlantillaDto = new SmsPlantilla();
            SmsPlantillaDaoImpl smsPlantillasDaoImpl = new SmsPlantillaDaoImpl(user.getConn());

            smsPlantillaDto.setAsunto(asunto);
            smsPlantillaDto.setContenido(contenido);
            smsPlantillaDto.setIdEstatus(estatus);
            //smsPlantillaDto.setAliasPlantilla("");
            smsPlantillaDto.setIdEmpresa(idEmpresa);
            smsPlantillaDto.setIsFijoSistema(0);
            smsPlantillaDto.setIdUsuarioPreto(user.getUser().getIdUsuarios());
            smsPlantillaDto.setIdUsuarioVentas(0);
            try{
                smsPlantillaDto.setFechaHrCreado(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
            }catch(Exception e){
                smsPlantillaDto.setFechaHrCreado(new Date());
            }

            try{
                smsPlantillasDaoImpl.insert(smsPlantillaDto); 

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