<%-- 
    Document   : catContratos_ajax
    Created on : 10/12/2013, 04:28:56 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.bo.NominaTipoContratoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.NominaTipoContratoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.NominaTipoContrato"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idNominaTipoContrato = -1;
    String nombreNominaTipoContrato ="";
    String descripcion ="";  
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idNominaTipoContrato = Integer.parseInt(request.getParameter("idNominaTipoContrato"));
    }catch(NumberFormatException ex){}
    nombreNominaTipoContrato = request.getParameter("nombreNominaTipoContrato")!=null?new String(request.getParameter("nombreNominaTipoContrato").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcion = request.getParameter("descripcionNominaTipoContrato")!=null?new String(request.getParameter("descripcionNominaTipoContrato").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombreNominaTipoContrato, 1, 30))
        msgError += "<ul>El dato 'nombre' es requerido.";
    if(!gc.isValidString(descripcion, 1, 100))
        msgError += "<ul>El dato 'descripción' es requerido";   
    if(idNominaTipoContrato <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'Contrato' es requerido";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idNominaTipoContrato>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                NominaTipoContratoBO nominaTipoContratoBO = new NominaTipoContratoBO(idNominaTipoContrato,user.getConn());
                NominaTipoContrato nominaTipoContratoDto = nominaTipoContratoBO.getNominaTipoContrato();
                
                nominaTipoContratoDto.setIdEstatus(estatus);
                nominaTipoContratoDto.setNombre(nombreNominaTipoContrato);
                nominaTipoContratoDto.setDescripcion(descripcion);
               
                
                try{
                    new NominaTipoContratoDaoImpl(user.getConn()).update(nominaTipoContratoDto.createPk(), nominaTipoContratoDto);

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
                NominaTipoContrato nominaTipoContratoDto = new NominaTipoContrato();
                NominaTipoContratoDaoImpl nominaTipoContratosDaoImpl = new NominaTipoContratoDaoImpl(user.getConn());
                
                nominaTipoContratoDto.setIdEstatus(estatus);
                nominaTipoContratoDto.setNombre(nombreNominaTipoContrato);
                nominaTipoContratoDto.setDescripcion(descripcion);                              
                nominaTipoContratoDto.setIdEmpresa(idEmpresa);

                /**
                 * Realizamos el insert
                 */
                nominaTipoContratosDaoImpl.insert(nominaTipoContratoDto);

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