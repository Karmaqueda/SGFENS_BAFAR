<%-- 
    Document   : catDeducciones_ajax
    Created on : 7/02/2014, 12:00:05 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page import="com.tsp.sct.dao.jdbc.NominaDeduccionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.NominaDeduccion"%>
<%@page import="com.tsp.sct.bo.NominaDeduccionBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idNominaDeduccion = -1;
    String claveNominaDeduccion ="";
    String descripcionNominaDeduccion ="";
    double impoGravadoNominaDeduccion = 0, impoExentoNominaDeduccion = 0;
    int estatus = 2, idTipoDeduccionNominaEmpleado = -1;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idNominaDeduccion = Integer.parseInt(request.getParameter("idNominaDeduccion"));
    }catch(NumberFormatException ex){}
    claveNominaDeduccion = request.getParameter("claveNominaDeduccion")!=null?new String(request.getParameter("claveNominaDeduccion").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcionNominaDeduccion = request.getParameter("descripcionNominaDeduccion")!=null?new String(request.getParameter("descripcionNominaDeduccion").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}
    try{
        impoGravadoNominaDeduccion = Double.parseDouble(request.getParameter("impoGravadoNominaDeduccion"));
    }catch(NumberFormatException ex){}
    try{
        impoExentoNominaDeduccion = Double.parseDouble(request.getParameter("impoExentoNominaDeduccion"));
    }catch(NumberFormatException ex){}
    try{
        idTipoDeduccionNominaEmpleado = Integer.parseInt(request.getParameter("idTipoDeduccionNominaEmpleado"));
    }catch(NumberFormatException ex){}
    
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(claveNominaDeduccion, 3, 15))
        msgError += "<ul>El dato 'clave' es requerido.";
    if(!gc.isValidString(descripcionNominaDeduccion, 1, 300))
        msgError += "<ul>El dato 'descripción' es requerido";
    if(idTipoDeduccionNominaEmpleado<=0)
        msgError += "<ul>El dato 'Tipo de percepcion' es requerido";
    if(idNominaDeduccion <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'percepcion' es requerido";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idNominaDeduccion>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                NominaDeduccionBO nominaDeduccionBO = new NominaDeduccionBO(idNominaDeduccion,user.getConn());
                NominaDeduccion nominaDeduccionDto = nominaDeduccionBO.getNominaDeduccion();
                                
                nominaDeduccionDto.setIdEstatus(estatus);
                nominaDeduccionDto.setIdNominaTipoDeduccion(idTipoDeduccionNominaEmpleado);
                nominaDeduccionDto.setClave(claveNominaDeduccion);
                nominaDeduccionDto.setConcepto(descripcionNominaDeduccion);
                nominaDeduccionDto.setImporteGravado(impoGravadoNominaDeduccion);
                nominaDeduccionDto.setImporteExcepto(impoExentoNominaDeduccion);
               
                
                try{
                    new NominaDeduccionDaoImpl(user.getConn()).update(nominaDeduccionDto.createPk(), nominaDeduccionDto);

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
                NominaDeduccion nominaDeduccionDto = new NominaDeduccion();
                NominaDeduccionDaoImpl nominaDeduccionsDaoImpl = new NominaDeduccionDaoImpl(user.getConn());
                
                nominaDeduccionDto.setIdEmpresa(idEmpresa);
                nominaDeduccionDto.setIdEstatus(estatus);
                nominaDeduccionDto.setIdNominaTipoDeduccion(idTipoDeduccionNominaEmpleado);
                nominaDeduccionDto.setClave(claveNominaDeduccion);
                nominaDeduccionDto.setConcepto(descripcionNominaDeduccion);
                nominaDeduccionDto.setImporteGravado(impoGravadoNominaDeduccion);
                nominaDeduccionDto.setImporteExcepto(impoExentoNominaDeduccion);                                

                /**
                 * Realizamos el insert
                 */
                nominaDeduccionsDaoImpl.insert(nominaDeduccionDto);

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