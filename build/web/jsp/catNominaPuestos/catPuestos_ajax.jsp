<%-- 
    Document   : catPuestos_ajax
    Created on : 9/12/2013, 06:54:21 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.dao.jdbc.NominaPuestoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.NominaPuesto"%>
<%@page import="com.tsp.sct.bo.NominaPuestoBO"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idNominaPuesto = -1;
    String nombreNominaPuesto ="";
    String descripcion ="";  
    int estatus = 2;//deshabilitado
    double sueldo = 0;
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idNominaPuesto = Integer.parseInt(request.getParameter("idNominaPuesto"));
    }catch(NumberFormatException ex){}
    nombreNominaPuesto = request.getParameter("nombreNominaPuesto")!=null?new String(request.getParameter("nombreNominaPuesto").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcion = request.getParameter("descripcionNominaPuesto")!=null?new String(request.getParameter("descripcionNominaPuesto").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        sueldo = Double.parseDouble(request.getParameter("sueldoNominaPuesto"));
    }catch(NumberFormatException ex){}
            
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombreNominaPuesto, 1, 30))
        msgError += "<ul>El dato 'nombre' es requerido.";
    if(!gc.isValidString(descripcion, 1, 450))
        msgError += "<ul>El dato 'descripción' es requerido";   
    if(idNominaPuesto <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'Puesto' es requerido";    
    
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idNominaPuesto>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                NominaPuestoBO nominaPuestoBO = new NominaPuestoBO(idNominaPuesto,user.getConn());
                NominaPuesto nominaPuestoDto = nominaPuestoBO.getNominaPuesto();
                
                nominaPuestoDto.setIdEstatus(estatus);
                nominaPuestoDto.setNombre(nombreNominaPuesto);
                nominaPuestoDto.setDescripcion(descripcion);
                nominaPuestoDto.setSueldoDiarioIntegrado(sueldo);               
                
                try{
                    new NominaPuestoDaoImpl(user.getConn()).update(nominaPuestoDto.createPk(), nominaPuestoDto);

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
                NominaPuesto nominaPuestoDto = new NominaPuesto();
                NominaPuestoDaoImpl nominaPuestosDaoImpl = new NominaPuestoDaoImpl(user.getConn());
                
                nominaPuestoDto.setIdEstatus(estatus);
                nominaPuestoDto.setNombre(nombreNominaPuesto);
                nominaPuestoDto.setDescripcion(descripcion);                              
                nominaPuestoDto.setIdEmpresa(idEmpresa);
                nominaPuestoDto.setSueldoDiarioIntegrado(sueldo);

                /**
                 * Realizamos el insert
                 */
                nominaPuestosDaoImpl.insert(nominaPuestoDto);

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
