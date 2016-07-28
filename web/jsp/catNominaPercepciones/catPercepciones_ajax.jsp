<%-- 
    Document   : catPercepciones_ajax
    Created on : 7/02/2014, 11:58:47 AM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page import="com.tsp.sct.dao.jdbc.NominaPercepcionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.NominaPercepcion"%>
<%@page import="com.tsp.sct.bo.NominaPercepcionBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idNominaPercepcion = -1;
    String claveNominaPercepcion ="";
    String descripcionNominaPercepcion ="";
    double impoGravadoNominaPercepcion = 0, impoExentoNominaPercepcion = 0;
    int estatus = 2, idTipoPercepcionNominaEmpleado = -1;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idNominaPercepcion = Integer.parseInt(request.getParameter("idNominaPercepcion"));
    }catch(NumberFormatException ex){}
    claveNominaPercepcion = request.getParameter("claveNominaPercepcion")!=null?new String(request.getParameter("claveNominaPercepcion").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcionNominaPercepcion = request.getParameter("descripcionNominaPercepcion")!=null?new String(request.getParameter("descripcionNominaPercepcion").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}
    try{
        impoGravadoNominaPercepcion = Double.parseDouble(request.getParameter("impoGravadoNominaPercepcion"));
    }catch(NumberFormatException ex){}
    try{
        impoExentoNominaPercepcion = Double.parseDouble(request.getParameter("impoExentoNominaPercepcion"));
    }catch(NumberFormatException ex){}
    try{
        idTipoPercepcionNominaEmpleado = Integer.parseInt(request.getParameter("idTipoPercepcionNominaEmpleado"));
    }catch(NumberFormatException ex){}
    
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(claveNominaPercepcion, 3, 15))
        msgError += "<ul>El dato 'clave' es requerido.";
    if(!gc.isValidString(descripcionNominaPercepcion, 1, 300))
        msgError += "<ul>El dato 'descripción' es requerido";
    if(idTipoPercepcionNominaEmpleado<=0)
        msgError += "<ul>El dato 'Tipo de percepcion' es requerido";
    if(idNominaPercepcion <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'percepcion' es requerido";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idNominaPercepcion>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                NominaPercepcionBO nominaPercepcionBO = new NominaPercepcionBO(idNominaPercepcion,user.getConn());
                NominaPercepcion nominaPercepcionDto = nominaPercepcionBO.getNominaPercepcion();
                                
                nominaPercepcionDto.setIdEstatus(estatus);
                nominaPercepcionDto.setIdNominaTipoPercepcion(idTipoPercepcionNominaEmpleado);
                nominaPercepcionDto.setClave(claveNominaPercepcion);
                nominaPercepcionDto.setConcepto(descripcionNominaPercepcion);
                nominaPercepcionDto.setImporteGravado(impoGravadoNominaPercepcion);
                nominaPercepcionDto.setImporteExcepto(impoExentoNominaPercepcion);
               
                
                try{
                    new NominaPercepcionDaoImpl(user.getConn()).update(nominaPercepcionDto.createPk(), nominaPercepcionDto);

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
                NominaPercepcion nominaPercepcionDto = new NominaPercepcion();
                NominaPercepcionDaoImpl nominaPercepcionsDaoImpl = new NominaPercepcionDaoImpl(user.getConn());
                
                nominaPercepcionDto.setIdEmpresa(idEmpresa);
                nominaPercepcionDto.setIdEstatus(estatus);
                nominaPercepcionDto.setIdNominaTipoPercepcion(idTipoPercepcionNominaEmpleado);
                nominaPercepcionDto.setClave(claveNominaPercepcion);
                nominaPercepcionDto.setConcepto(descripcionNominaPercepcion);
                nominaPercepcionDto.setImporteGravado(impoGravadoNominaPercepcion);
                nominaPercepcionDto.setImporteExcepto(impoExentoNominaPercepcion);                                

                /**
                 * Realizamos el insert
                 */
                nominaPercepcionsDaoImpl.insert(nominaPercepcionDto);

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