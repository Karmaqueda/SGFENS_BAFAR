<%-- 
    Document   : catJornadas_ajax
    Created on : 10/12/2013, 04:32:12 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.bo.NominaTipoJornadaBO"%>
<%@page import="com.tsp.sct.dao.jdbc.NominaTipoJornadaDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.NominaTipoJornada"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idNominaTipoJornada = -1;
    String nombreNominaTipoJornada ="";
    String descripcion ="";  
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idNominaTipoJornada = Integer.parseInt(request.getParameter("idNominaTipoJornada"));
    }catch(NumberFormatException ex){}
    nombreNominaTipoJornada = request.getParameter("nombreNominaTipoJornada")!=null?new String(request.getParameter("nombreNominaTipoJornada").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcion = request.getParameter("descripcionNominaTipoJornada")!=null?new String(request.getParameter("descripcionNominaTipoJornada").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombreNominaTipoJornada, 1, 30))
        msgError += "<ul>El dato 'nombre' es requerido.";
    if(!gc.isValidString(descripcion, 1, 100))
        msgError += "<ul>El dato 'descripción' es requerido";   
    if(idNominaTipoJornada <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'Jornada' es requerido";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idNominaTipoJornada>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                NominaTipoJornadaBO nominaTipoJornadaBO = new NominaTipoJornadaBO(idNominaTipoJornada);
                NominaTipoJornada nominaTipoJornadaDto = nominaTipoJornadaBO.getNominaTipoJornada();
                
                nominaTipoJornadaDto.setIdEstatus(estatus);
                nominaTipoJornadaDto.setNombre(nombreNominaTipoJornada);
                nominaTipoJornadaDto.setDescripcion(descripcion);
               
                
                try{
                    new NominaTipoJornadaDaoImpl(user.getConn()).update(nominaTipoJornadaDto.createPk(), nominaTipoJornadaDto);

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
                NominaTipoJornada nominaTipoJornadaDto = new NominaTipoJornada();
                NominaTipoJornadaDaoImpl nominaTipoJornadasDaoImpl = new NominaTipoJornadaDaoImpl(user.getConn());
                
                nominaTipoJornadaDto.setIdEstatus(estatus);
                nominaTipoJornadaDto.setNombre(nombreNominaTipoJornada);
                nominaTipoJornadaDto.setDescripcion(descripcion);                              
                nominaTipoJornadaDto.setIdEmpresa(idEmpresa);

                /**
                 * Realizamos el insert
                 */
                nominaTipoJornadasDaoImpl.insert(nominaTipoJornadaDto);

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