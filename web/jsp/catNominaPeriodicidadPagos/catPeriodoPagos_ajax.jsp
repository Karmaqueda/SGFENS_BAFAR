<%-- 
    Document   : catPeriodoPagos_ajax
    Created on : 11/12/2013, 10:58:31 AM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.bo.NominaPeriodicidadPagoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.NominaPeriodicidadPagoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.NominaPeriodicidadPago"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idNominaPeriodicidadPago = -1;
    String nombreNominaPeriodicidadPago ="";
    String descripcion ="";  
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idNominaPeriodicidadPago = Integer.parseInt(request.getParameter("idNominaPeriodicidadPago"));
    }catch(NumberFormatException ex){}
    nombreNominaPeriodicidadPago = request.getParameter("nombreNominaPeriodicidadPago")!=null?new String(request.getParameter("nombreNominaPeriodicidadPago").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcion = request.getParameter("descripcionNominaPeriodicidadPago")!=null?new String(request.getParameter("descripcionNominaPeriodicidadPago").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombreNominaPeriodicidadPago, 1, 30))
        msgError += "<ul>El dato 'nombre' es requerido.";
    if(!gc.isValidString(descripcion, 1, 100))
        msgError += "<ul>El dato 'descripción' es requerido";   
    if(idNominaPeriodicidadPago <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'Pago' es requerido";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idNominaPeriodicidadPago>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                NominaPeriodicidadPagoBO nominaPeriodicidadPagoBO = new NominaPeriodicidadPagoBO(idNominaPeriodicidadPago,user.getConn());
                NominaPeriodicidadPago nominaPeriodicidadPagoDto = nominaPeriodicidadPagoBO.getNominaPeriodicidadPago();
                
                nominaPeriodicidadPagoDto.setIdEstatus(estatus);
                nominaPeriodicidadPagoDto.setNombre(nombreNominaPeriodicidadPago);
                nominaPeriodicidadPagoDto.setDescripcion(descripcion);
               
                
                try{
                    new NominaPeriodicidadPagoDaoImpl(user.getConn()).update(nominaPeriodicidadPagoDto.createPk(), nominaPeriodicidadPagoDto);

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
                NominaPeriodicidadPago nominaPeriodicidadPagoDto = new NominaPeriodicidadPago();
                NominaPeriodicidadPagoDaoImpl nominaPeriodicidadPagosDaoImpl = new NominaPeriodicidadPagoDaoImpl(user.getConn());
                
                nominaPeriodicidadPagoDto.setIdEstatus(estatus);
                nominaPeriodicidadPagoDto.setNombre(nombreNominaPeriodicidadPago);
                nominaPeriodicidadPagoDto.setDescripcion(descripcion);                              
                nominaPeriodicidadPagoDto.setIdEmpresa(idEmpresa);

                /**
                 * Realizamos el insert
                 */
                nominaPeriodicidadPagosDaoImpl.insert(nominaPeriodicidadPagoDto);

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
