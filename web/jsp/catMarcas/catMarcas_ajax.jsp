<%-- 
    Document   : catMarcas_ajax
    Created on : 6/11/2012, 11:32:28 AM
    Author     : Leonardo
--%>


<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.MarcaBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.MarcaDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Marca"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idMarca = -1;
    String nombreMarca ="";
    String descripcion ="";  
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idMarca = Integer.parseInt(request.getParameter("idMarca"));
    }catch(NumberFormatException ex){}
    nombreMarca = request.getParameter("nombreMarca")!=null?new String(request.getParameter("nombreMarca").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombreMarca, 1, 30))
        msgError += "<ul>El dato 'nombre' es requerido.";
    if(!gc.isValidString(descripcion, 1, 100))
        msgError += "<ul>El dato 'descripción' es requerido";   
    if(idMarca <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'marca' es requerido";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idMarca>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                MarcaBO marcaBO = new MarcaBO(idMarca,user.getConn());
                Marca marcaDto = marcaBO.getMarca();
                
                marcaDto.setIdEstatus(estatus);
                marcaDto.setNombre(nombreMarca);
                marcaDto.setDescripcion(descripcion);
               
                
                try{
                    new MarcaDaoImpl(user.getConn()).update(marcaDto.createPk(), marcaDto);

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
                Marca marcaDto = new Marca();
                MarcaDaoImpl marcasDaoImpl = new MarcaDaoImpl(user.getConn());
                
                marcaDto.setIdEstatus(estatus);
                marcaDto.setNombre(nombreMarca);
                marcaDto.setDescripcion(descripcion);                              
                marcaDto.setIdEmpresa(idEmpresa);

                /**
                 * Realizamos el insert
                 */
                marcasDaoImpl.insert(marcaDto);

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