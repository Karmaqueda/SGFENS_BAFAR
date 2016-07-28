<%-- 
    Document   : catServicios_ajax
    Created on : 9/11/2012, 04:04:01 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.ServicioBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.ServicioDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Servicio"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idServicio = -1;
    String nombreServicio ="";
    String descripcion =""; 
    float precio = -1;
    String skuServicio = "";
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idServicio = Integer.parseInt(request.getParameter("idServicio"));
    }catch(NumberFormatException ex){}
    nombreServicio = request.getParameter("nombreServicio")!=null?new String(request.getParameter("nombreServicio").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        precio = Float.parseFloat(request.getParameter("precioServicio")!=null?new String(request.getParameter("precioServicio").getBytes("ISO-8859-1"),"UTF-8"):"");    
    }catch(Exception ex){}
    skuServicio = request.getParameter("skuServicio")!=null?new String(request.getParameter("skuServicio").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombreServicio, 1, 30))
        msgError += "<ul>El dato 'nombre' es requerido.";
    if(!gc.isValidString(descripcion, 1, 100))
        msgError += "<ul>El dato 'descripción' es requerido";   
    if(precio < 0)
        msgError += "<ul>El dato 'precio' es requerido y debe ser un valor positivo."; 
    if(idServicio <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'servicio' es requerido";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idServicio>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                ServicioBO servicioBO = new ServicioBO(idServicio,user.getConn());
                Servicio servicioDto = servicioBO.getServicio();
                
                servicioDto.setIdEstatus(estatus);
                servicioDto.setNombre(nombreServicio);
                servicioDto.setDescripcion(descripcion);
                servicioDto.setPrecio(precio);
                servicioDto.setSku(skuServicio);               
                
                try{
                    new ServicioDaoImpl(user.getConn()).update(servicioDto.createPk(), servicioDto);

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
                 * Creamos el registro de Servicio
                 */
                Servicio servicioDto = new Servicio();
                ServicioDaoImpl serviciosDaoImpl = new ServicioDaoImpl(user.getConn());
                
                servicioDto.setIdEstatus(estatus);
                servicioDto.setNombre(nombreServicio);
                servicioDto.setDescripcion(descripcion);                              
                servicioDto.setIdEmpresa(idEmpresa);
                servicioDto.setPrecio(precio);
                servicioDto.setSku(skuServicio); 

                /**
                 * Realizamos el insert
                 */
                serviciosDaoImpl.insert(servicioDto);

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