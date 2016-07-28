<%-- 
    Document   : catCategorias_ajax
    Created on : 21/11/2012, 11:20:27 AM
    Author     : Leonardo
--%>

<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.CategoriaBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.CategoriaDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Categoria"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idCategoria = -1;
    String nombreCategoria ="";
    String descripcion ="";
    int idCategoriaPadre = -1;
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
    }catch(NumberFormatException ex){}
    nombreCategoria = request.getParameter("nombreCategoria")!=null?new String(request.getParameter("nombreCategoria").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";        
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    try{
        idCategoriaPadre = Integer.parseInt(request.getParameter("idCategoriaPadre"));
    }catch(NumberFormatException ex){}
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombreCategoria, 1, 30))
        msgError += "<ul>El dato 'nombre' es requerido.";
    if(!gc.isValidString(descripcion, 1, 100))
        msgError += "<ul>El dato 'descripción' es requerido";   
    if(idCategoria <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'Categoria' es requerido";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idCategoria>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                CategoriaBO categoriaBO = new CategoriaBO(idCategoria,user.getConn());
                Categoria categoriaDto = categoriaBO.getCategoria();
                
                categoriaDto.setIdEstatus(estatus);
                categoriaDto.setNombreCategoria(nombreCategoria);
                categoriaDto.setDescripcionCategoria(descripcion);
                categoriaDto.setIdCategoriaPadre(idCategoriaPadre);
               
                
                try{
                    new CategoriaDaoImpl(user.getConn()).update(categoriaDto.createPk(), categoriaDto);

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
                Categoria categoriaDto = new Categoria();
                CategoriaDaoImpl categoriasDaoImpl = new CategoriaDaoImpl(user.getConn());
                
                categoriaDto.setIdEstatus(estatus);
                categoriaDto.setNombreCategoria(nombreCategoria);
                categoriaDto.setDescripcionCategoria(descripcion);                              
                categoriaDto.setIdEmpresa(idEmpresa);
                categoriaDto.setIdCategoriaPadre(idCategoriaPadre);

                /**
                 * Realizamos el insert
                 */
                categoriasDaoImpl.insert(categoriaDto);

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