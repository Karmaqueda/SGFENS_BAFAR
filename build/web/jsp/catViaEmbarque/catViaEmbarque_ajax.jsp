<%-- 
    Document   : catViaEmbarque_ajax
    Created on : 14/08/2015, 05:40:39 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.util.test.qryAlmacenProductos"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.ViaEmbarqueBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.ViaEmbarqueDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.ViaEmbarque"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idViaEmbarque = -1;
    String nombreViaEmbarque ="";
    String descripcion ="";  
    int estatus = 2;//deshabilitado
    String clave ="";
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idViaEmbarque = Integer.parseInt(request.getParameter("idViaEmbarque"));
    }catch(NumberFormatException ex){}
    nombreViaEmbarque = request.getParameter("nombreViaEmbarque")!=null?new String(request.getParameter("nombreViaEmbarque").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";    
    clave = request.getParameter("claveViaEmbarque")!=null?new String(request.getParameter("claveViaEmbarque").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombreViaEmbarque, 1, 30))
        msgError += "<ul>El dato 'nombre' es requerido.";
    if(!gc.isValidString(descripcion, 1, 100))
        msgError += "<ul>El dato 'descripción' es requerido";   
    if(idViaEmbarque <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'ViaEmbarque' es requerido";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idViaEmbarque>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                ViaEmbarqueBO viaEmbarqueBO = new ViaEmbarqueBO(idViaEmbarque,user.getConn());
                ViaEmbarque viaEmbarqueDto = viaEmbarqueBO.getViaEmbarque();
                
                viaEmbarqueDto.setIdEstatus(estatus);
                viaEmbarqueDto.setNombre(nombreViaEmbarque);
                viaEmbarqueDto.setDescripcion(descripcion);
                viaEmbarqueDto.setClave(clave);
                if(viaEmbarqueDto.getSincronizacionMicrosip() == 1)
                    viaEmbarqueDto.setSincronizacionMicrosip(2);
               
                
                try{
                    new ViaEmbarqueDaoImpl(user.getConn()).update(viaEmbarqueDto.createPk(), viaEmbarqueDto);

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
                
                // Peligro!!!
                // No quitar comentario , esto se ejecutara solamente una vez
                //cuando se libere lo de almacenes, posteriormente se elmininara    02/07/2015
                
                //qryAlmacenProductos.queryprods();
                
                //------------------------------------
                
                
                /**
                 * Creamos el registro de Cliente
                 */
                ViaEmbarque viaEmbarqueDto = new ViaEmbarque();
                ViaEmbarqueDaoImpl viaEmbarquesDaoImpl = new ViaEmbarqueDaoImpl(user.getConn());
                
                viaEmbarqueDto.setIdEstatus(estatus);
                viaEmbarqueDto.setNombre(nombreViaEmbarque);
                viaEmbarqueDto.setDescripcion(descripcion);                              
                viaEmbarqueDto.setIdEmpresa(idEmpresa);
                viaEmbarqueDto.setClave(clave);

                /**
                 * Realizamos el insert
                 */
                viaEmbarquesDaoImpl.insert(viaEmbarqueDto);

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