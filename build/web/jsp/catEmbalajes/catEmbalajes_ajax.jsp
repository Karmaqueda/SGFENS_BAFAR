<%-- 
    Document   : catEmbalajes_ajax
    Created on : 9/11/2012, 10:40:20 AM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.util.test.qryAlmacenProductos"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.EmbalajeBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.EmbalajeDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Embalaje"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idEmbalaje = -1;
    String nombreEmbalaje ="";
    String descripcion ="";  
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idEmbalaje = Integer.parseInt(request.getParameter("idEmbalaje"));
    }catch(NumberFormatException ex){}
    nombreEmbalaje = request.getParameter("nombreEmbalaje")!=null?new String(request.getParameter("nombreEmbalaje").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombreEmbalaje, 1, 30))
        msgError += "<ul>El dato 'nombre' es requerido.";
    if(!gc.isValidString(descripcion, 1, 100))
        msgError += "<ul>El dato 'descripción' es requerido";   
    if(idEmbalaje <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'Embalaje' es requerido";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idEmbalaje>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                EmbalajeBO embalajeBO = new EmbalajeBO(idEmbalaje,user.getConn());
                Embalaje embalajeDto = embalajeBO.getEmbalaje();
                
                embalajeDto.setIdEstatus(estatus);
                embalajeDto.setNombre(nombreEmbalaje);
                embalajeDto.setDescripcion(descripcion);
               
                
                try{
                    new EmbalajeDaoImpl(user.getConn()).update(embalajeDto.createPk(), embalajeDto);

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
                Embalaje embalajeDto = new Embalaje();
                EmbalajeDaoImpl embalajesDaoImpl = new EmbalajeDaoImpl(user.getConn());
                
                embalajeDto.setIdEstatus(estatus);
                embalajeDto.setNombre(nombreEmbalaje);
                embalajeDto.setDescripcion(descripcion);                              
                embalajeDto.setIdEmpresa(idEmpresa);

                /**
                 * Realizamos el insert
                 */
                embalajesDaoImpl.insert(embalajeDto);

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