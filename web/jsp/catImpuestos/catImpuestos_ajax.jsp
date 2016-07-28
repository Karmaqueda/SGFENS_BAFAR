<%-- 
    Document   : catImpuestos_ajax
    Created on : 28/11/2012, 11:54:52 AM
    Author     : Leonardo
--%>

<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.ImpuestoBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.ImpuestoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Impuesto"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idImpuesto = -1;
    String nombreImpuesto ="";
    String descripcion ="";  
    float porcentaje = -1;
    int estatus = 2;//deshabilitado
    int trasladado = 0;
    int local = 0;
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idImpuesto = Integer.parseInt(request.getParameter("idImpuesto"));
    }catch(NumberFormatException ex){}
    nombreImpuesto = request.getParameter("nombreImpuesto")!=null?new String(request.getParameter("nombreImpuesto").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    try{
        porcentaje = Float.parseFloat(request.getParameter("porcentaje"));
    }catch(NumberFormatException ex){}  
    try{
        trasladado = Integer.parseInt(request.getParameter("trasladado"));
    }catch(NumberFormatException ex){} 
    try{
        local = Integer.parseInt(request.getParameter("local"));
    }catch(NumberFormatException ex){}  
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombreImpuesto, 1, 100))
        msgError += "<ul>El dato 'nombre' es requerido.";
    if(!gc.isValidString(descripcion, 1, 100))
        msgError += "<ul>El dato 'descripción' es requerido";
    if(porcentaje < 0)
        msgError += "<ul>El dato 'porcentaje' es requerido";         
    if(idImpuesto <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'impuesto' es requerido";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idImpuesto>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                ImpuestoBO impuestoBO = new ImpuestoBO(idImpuesto,user.getConn());
                Impuesto impuestoDto = impuestoBO.getImpuesto();
                
                impuestoDto.setIdEstatus(estatus);
                impuestoDto.setNombre(nombreImpuesto);
                impuestoDto.setDescripcion(descripcion);
                
                impuestoDto.setPorcentaje(porcentaje);
                short cero = 0;
                short uno = 1;
                if(trasladado==1){
                    impuestoDto.setTrasladado(uno);
                }else{
                    impuestoDto.setTrasladado(cero);
                }
                if(local==1){
                    impuestoDto.setImpuestoLocal(uno);
                }else{
                    impuestoDto.setImpuestoLocal(cero);
                }
                       
                impuestoDto.setSincronizacionMicrosip(2);
                
                try{
                    new ImpuestoDaoImpl(user.getConn()).update(impuestoDto.createPk(), impuestoDto);

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
                 * Creamos el registro de Impuesto
                 */
                Impuesto impuestoDto = new Impuesto();
                ImpuestoDaoImpl impuestosDaoImpl = new ImpuestoDaoImpl(user.getConn());
                
                int idImpuestoNuevo;

                Impuesto ultimoRegistroImpuestos = impuestosDaoImpl.findLast();
                idImpuestoNuevo = ultimoRegistroImpuestos.getIdImpuesto() + 1;
                
                impuestoDto.setIdImpuesto(idImpuestoNuevo);                
                impuestoDto.setIdEstatus(estatus);
                impuestoDto.setNombre(nombreImpuesto);
                impuestoDto.setDescripcion(descripcion);                              
                impuestoDto.setIdEmpresa(idEmpresa);
                
                impuestoDto.setPorcentaje(porcentaje);
                short cero = 0;
                short uno = 1;
                if(trasladado==1){
                    impuestoDto.setTrasladado(uno);
                }else{
                    impuestoDto.setTrasladado(cero);
                }
                if(local==1){
                    impuestoDto.setImpuestoLocal(uno);
                }else{
                    impuestoDto.setImpuestoLocal(cero);
                }

                /**
                 * Realizamos el insert
                 */
                impuestosDaoImpl.insert(impuestoDto);

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