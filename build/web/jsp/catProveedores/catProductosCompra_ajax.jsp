<%-- 
    Document   : catProductosCompra_ajax
    Created on : 11/08/2015, 05:46:27 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.util.test.qryAlmacenProductos"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.SGProductoServicioBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensProductoServicioDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProductoServicio"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idSgfensProductoServicio = -1;
    String nombreSgfensProductoServicio ="";
    String descripcion ="";  
    int estatus = 2;//deshabilitado
    
    String unidadSgfensProductoServicio = "";
    double precioMenudeoSgfensProductoServicio = 0;
    double hastaMenudeoSgfensProductoServicio = 0;
    double precioMedioMayoreoSgfensProductoServicio = 0;
    double desdeMedioMayoreoSgfensProductoServicio = 0;
    double hastaMedioMayoreoSgfensProductoServicio = 0;
    double precioMayoreoSgfensProductoServicio = 0;
    double desdeMayoreoSgfensProductoServicio = 0;
    int idCategoriaSgfensProductoServicio = 0;
    String nombreImagenSgfensProductoServicio = "";
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idSgfensProductoServicio = Integer.parseInt(request.getParameter("idSgfensProductoServicio"));
    }catch(NumberFormatException ex){}
    nombreSgfensProductoServicio = request.getParameter("nombreSgfensProductoServicio")!=null?new String(request.getParameter("nombreSgfensProductoServicio").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    unidadSgfensProductoServicio = request.getParameter("unidadSgfensProductoServicio")!=null?new String(request.getParameter("unidadSgfensProductoServicio").getBytes("ISO-8859-1"),"UTF-8"):"";
    try {
        precioMenudeoSgfensProductoServicio = Double.parseDouble(request.getParameter("precioMenudeoSgfensProductoServicio") != null ? new String(request.getParameter("precioMenudeoSgfensProductoServicio").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (Exception ex) {}
    try {
        hastaMenudeoSgfensProductoServicio = Double.parseDouble(request.getParameter("hastaMenudeoSgfensProductoServicio") != null ? new String(request.getParameter("hastaMenudeoSgfensProductoServicio").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (Exception ex) {}
    try {
        precioMedioMayoreoSgfensProductoServicio = Double.parseDouble(request.getParameter("precioMedioMayoreoSgfensProductoServicio") != null ? new String(request.getParameter("precioMedioMayoreoSgfensProductoServicio").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (Exception ex) {}
    try {
        desdeMedioMayoreoSgfensProductoServicio = Double.parseDouble(request.getParameter("desdeMedioMayoreoSgfensProductoServicio") != null ? new String(request.getParameter("desdeMedioMayoreoSgfensProductoServicio").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (Exception ex) {}
    try {
        hastaMedioMayoreoSgfensProductoServicio = Double.parseDouble(request.getParameter("hastaMedioMayoreoSgfensProductoServicio") != null ? new String(request.getParameter("hastaMedioMayoreoSgfensProductoServicio").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (Exception ex) {}
    try {
        precioMayoreoSgfensProductoServicio = Double.parseDouble(request.getParameter("precioMayoreoSgfensProductoServicio") != null ? new String(request.getParameter("precioMayoreoSgfensProductoServicio").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (Exception ex) {}
    try {
        desdeMayoreoSgfensProductoServicio = Double.parseDouble(request.getParameter("desdeMayoreoSgfensProductoServicio") != null ? new String(request.getParameter("desdeMayoreoSgfensProductoServicio").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (Exception ex) {}
    try{
        idCategoriaSgfensProductoServicio = Integer.parseInt(request.getParameter("idCategoriaSgfensProductoServicio"));
    }catch(NumberFormatException ex){}
    
    nombreImagenSgfensProductoServicio = request.getParameter("nombreImagenSgfensProductoServicio")!=null?new String(request.getParameter("nombreImagenSgfensProductoServicio").getBytes("ISO-8859-1"),"UTF-8"):"";
    
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombreSgfensProductoServicio, 1, 30))
        msgError += "<ul>El dato 'nombre' es requerido.";
    if(!gc.isValidString(descripcion, 1, 100))
        msgError += "<ul>El dato 'descripción' es requerido";   
    if(idSgfensProductoServicio <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'SgfensProductoServicio' es requerido";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idSgfensProductoServicio>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                SGProductoServicioBO sgfensProductoServicioBO = new SGProductoServicioBO(idSgfensProductoServicio,user.getConn());
                SgfensProductoServicio sgfensProductoServicioDto = sgfensProductoServicioBO.getSgfensProductoServicio();
                
                sgfensProductoServicioDto.setIdEstatus(estatus);
                sgfensProductoServicioDto.setNombre(nombreSgfensProductoServicio);
                sgfensProductoServicioDto.setDescripcion(descripcion);
                
                sgfensProductoServicioDto.setUnidad(unidadSgfensProductoServicio);
                sgfensProductoServicioDto.setPrecioMenudeo(precioMenudeoSgfensProductoServicio);                
                sgfensProductoServicioDto.setMaxMenudeo(hastaMenudeoSgfensProductoServicio);
                sgfensProductoServicioDto.setPrecioMedioMayoreo(precioMedioMayoreoSgfensProductoServicio);
                sgfensProductoServicioDto.setMinMedioMayoreo(desdeMedioMayoreoSgfensProductoServicio);
                sgfensProductoServicioDto.setMaxMedioMayoreo(hastaMedioMayoreoSgfensProductoServicio);
                sgfensProductoServicioDto.setPrecioMayoreo(precioMayoreoSgfensProductoServicio);
                sgfensProductoServicioDto.setMinMayoreo(desdeMayoreoSgfensProductoServicio);
                sgfensProductoServicioDto.setNombreImagenProductoServicio(nombreImagenSgfensProductoServicio);
                sgfensProductoServicioDto.setIdCategoria(idCategoriaSgfensProductoServicio);
                
               
                
                try{
                    new SgfensProductoServicioDaoImpl(user.getConn()).update(sgfensProductoServicioDto.createPk(), sgfensProductoServicioDto);

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
                SgfensProductoServicio sgfensProductoServicioDto = new SgfensProductoServicio();
                SgfensProductoServicioDaoImpl sgfensProductoServiciosDaoImpl = new SgfensProductoServicioDaoImpl(user.getConn());
                
                sgfensProductoServicioDto.setIdEstatus(estatus);
                sgfensProductoServicioDto.setNombre(nombreSgfensProductoServicio);
                sgfensProductoServicioDto.setDescripcion(descripcion);                              
                sgfensProductoServicioDto.setIdEmpresa(idEmpresa);
                
                sgfensProductoServicioDto.setUnidad(unidadSgfensProductoServicio);
                sgfensProductoServicioDto.setPrecioMenudeo(precioMenudeoSgfensProductoServicio);                
                sgfensProductoServicioDto.setMaxMenudeo(hastaMenudeoSgfensProductoServicio);
                sgfensProductoServicioDto.setPrecioMedioMayoreo(precioMedioMayoreoSgfensProductoServicio);
                sgfensProductoServicioDto.setMinMedioMayoreo(desdeMedioMayoreoSgfensProductoServicio);
                sgfensProductoServicioDto.setMaxMedioMayoreo(hastaMedioMayoreoSgfensProductoServicio);
                sgfensProductoServicioDto.setPrecioMayoreo(precioMayoreoSgfensProductoServicio);
                sgfensProductoServicioDto.setMinMayoreo(desdeMayoreoSgfensProductoServicio);
                sgfensProductoServicioDto.setNombreImagenProductoServicio(nombreImagenSgfensProductoServicio);
                sgfensProductoServicioDto.setIdCategoria(idCategoriaSgfensProductoServicio);

                /**
                 * Realizamos el insert
                 */
                sgfensProductoServiciosDaoImpl.insert(sgfensProductoServicioDto);

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