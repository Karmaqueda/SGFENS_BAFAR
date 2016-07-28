<%-- 
    Document   : catNominaRegistroPatronalControl_ajax
    Created on : 21/03/2014, 04:12:49 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.NominaRegistroPatronalBO"%>
<%@page import="com.tsp.sct.dao.jdbc.NominaRegistroPatronalDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.NominaRegistroPatronal"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idRegistroPatronal = -1;
    String registroPatronal ="";
        
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idRegistroPatronal = Integer.parseInt(request.getParameter("idRegistroPatronal"));
    }catch(NumberFormatException ex){}
    registroPatronal = request.getParameter("registroPatronal")!=null?new String(request.getParameter("registroPatronal").getBytes("ISO-8859-1"),"UTF-8"):"";
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){} 

    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator(); 
    if(!gc.isValidString(registroPatronal, 11, 11))
        msgError += "<ul>El dato 'Registro Patronal' no es de longitud valida (11 caracteres).";
    if(!gc.isRegistroPatronal(registroPatronal))
        msgError += "<ul>El dato 'Registro Patronal' no cumple con el estandar.";
    if(idRegistroPatronal <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'NominaRegistroPatronal' es requerido para edicion.";

    if(msgError.equals("")){
        if(idRegistroPatronal>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                NominaRegistroPatronalBO nominaRegistroPatronalBO = new NominaRegistroPatronalBO(idRegistroPatronal,user.getConn());
                NominaRegistroPatronal nomRegistroPatronalDto = nominaRegistroPatronalBO.getNominaRegistroPatronal();

                //nomRegistroPatronalDto.setIdEmpresa(idEmpresa);
                nomRegistroPatronalDto.setRegistroPatronal(registroPatronal);
                //nominaRegistroPatronalDto.setIdEstatus(estatus);


                try{
                    new NominaRegistroPatronalDaoImpl(user.getConn()).update(nomRegistroPatronalDto.createPk(), nomRegistroPatronalDto);

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
                 * Creamos el registro de Cliente
                 */
                NominaRegistroPatronal nominaRegistroPatronalDto = new NominaRegistroPatronal();
                NominaRegistroPatronalDaoImpl nominaRegistroPatronalDaoImpl = new NominaRegistroPatronalDaoImpl(user.getConn());
                
                nominaRegistroPatronalDto.setIdEmpresa(idEmpresa);
                nominaRegistroPatronalDto.setRegistroPatronal(registroPatronal);
                //nominaRegistroPatronalDto.setIdEstatus(estatus);

                /**
                 * Realizamos el insert
                 */
                nominaRegistroPatronalDaoImpl.insert(nominaRegistroPatronalDto);

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
