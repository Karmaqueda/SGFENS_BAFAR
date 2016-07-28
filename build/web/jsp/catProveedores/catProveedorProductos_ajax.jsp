<%-- 
    Document   : catProveedorProductos_ajax
    Created on : 17/08/2015, 05:58:45 PM
    Author     : leonardo
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.bo.SGProductoServicioBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProductoServicio"%>
<%@page import="com.tsp.sct.util.test.qryAlmacenProductos"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.SGProveedorProductoBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensProveedorProductoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProveedorProducto"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idSgfensProveedorProducto = -1;
    int idProveedor = -1;
    int idConceptoSeleccionado = -1;
    String unidadSgfensProveedorProducto = "";
    double precioMenudeoSgfensProveedorProducto = 0;
    double hastaMenudeoSgfensProveedorProducto = 0;
    double precioMedioMayoreoSgfensProveedorProducto = 0;
    double desdeMedioMayoreoSgfensProveedorProducto = 0;
    double hastaMedioMayoreoSgfensProveedorProducto = 0;
    double precioMayoreoSgfensProveedorProducto = 0;
    double desdeMayoreoSgfensProveedorProducto = 0;
    int idCategoriaSgfensProveedorProducto = 0;
    String nombreImagenSgfensProveedorProducto = "";
    
    Date fecha_caducidad = null;
    double volumenSgfensProveedorProducto = 0;
    Date fecha_disponibilidad = null;
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idSgfensProveedorProducto = Integer.parseInt(request.getParameter("idSgfensProveedorProducto"));
    }catch(NumberFormatException ex){}
    try{
        idConceptoSeleccionado = Integer.parseInt(request.getParameter("idConceptoSeleccionado"));
    }catch(NumberFormatException ex){}
    unidadSgfensProveedorProducto = request.getParameter("unidadSgfensProveedorProducto")!=null?new String(request.getParameter("unidadSgfensProveedorProducto").getBytes("ISO-8859-1"),"UTF-8"):"";
    try {
        precioMenudeoSgfensProveedorProducto = Double.parseDouble(request.getParameter("precioMenudeoSgfensProveedorProducto") != null ? new String(request.getParameter("precioMenudeoSgfensProveedorProducto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (Exception ex) {}
    try {
        hastaMenudeoSgfensProveedorProducto = Double.parseDouble(request.getParameter("hastaMenudeoSgfensProveedorProducto") != null ? new String(request.getParameter("hastaMenudeoSgfensProveedorProducto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (Exception ex) {}
    try {
        precioMedioMayoreoSgfensProveedorProducto = Double.parseDouble(request.getParameter("precioMedioMayoreoSgfensProveedorProducto") != null ? new String(request.getParameter("precioMedioMayoreoSgfensProveedorProducto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (Exception ex) {}
    try {
        desdeMedioMayoreoSgfensProveedorProducto = Double.parseDouble(request.getParameter("desdeMedioMayoreoSgfensProveedorProducto") != null ? new String(request.getParameter("desdeMedioMayoreoSgfensProveedorProducto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (Exception ex) {}
    try {
        hastaMedioMayoreoSgfensProveedorProducto = Double.parseDouble(request.getParameter("hastaMedioMayoreoSgfensProveedorProducto") != null ? new String(request.getParameter("hastaMedioMayoreoSgfensProveedorProducto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (Exception ex) {}
    try {
        precioMayoreoSgfensProveedorProducto = Double.parseDouble(request.getParameter("precioMayoreoSgfensProveedorProducto") != null ? new String(request.getParameter("precioMayoreoSgfensProveedorProducto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (Exception ex) {}
    try {
        desdeMayoreoSgfensProveedorProducto = Double.parseDouble(request.getParameter("desdeMayoreoSgfensProveedorProducto") != null ? new String(request.getParameter("desdeMayoreoSgfensProveedorProducto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (Exception ex) {}
    try{
        idCategoriaSgfensProveedorProducto = Integer.parseInt(request.getParameter("idCategoriaSgfensProveedorProducto"));
    }catch(NumberFormatException ex){}
    
    nombreImagenSgfensProveedorProducto = request.getParameter("nombreImagenSgfensProveedorProducto")!=null?new String(request.getParameter("nombreImagenSgfensProveedorProducto").getBytes("ISO-8859-1"),"UTF-8"):"";
    
    try{
        idProveedor = Integer.parseInt(request.getParameter("idProveedor"));
    }catch(Exception e){}
    try {
        fecha_caducidad = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fecha_caducidad"));
    } catch (Exception e) {}
    try {
        volumenSgfensProveedorProducto = Double.parseDouble(request.getParameter("volumenSgfensProveedorProducto") != null ? new String(request.getParameter("volumenSgfensProveedorProducto").getBytes("ISO-8859-1"), "UTF-8") : "");
    } catch (Exception ex) {}
    try {
        fecha_disponibilidad = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fecha_disponibilidad"));
    } catch (Exception e) {}
    
    SgfensProductoServicio sgfensProductoServicio = null;
    
    if(mode.equals("cargaDatosConcepto")){        
        try{
            idConceptoSeleccionado = Integer.parseInt(request.getParameter("idConceptoSeleccionado"));
        }catch(NumberFormatException ex){}
        sgfensProductoServicio = new SGProductoServicioBO(idConceptoSeleccionado, user.getConn()).getSgfensProductoServicio();
        
    }else if(mode.equals("eliminarProductoDeProveedor")){ 
        int idProveeProducto = -1;
        try{
            idProveeProducto = Integer.parseInt(request.getParameter("idProveeProducto"));
            SgfensProveedorProducto producto = new SGProveedorProductoBO(idProveeProducto, user.getConn()).getSgfensProveedorProducto();
            new SgfensProveedorProductoDaoImpl(user.getConn()).delete(producto.createPk());
            out.print("<!--EXITO-->Registro borrado satisfactoriamente");
        }catch(Exception ex){}            
    }else{ 
        /*
        * Validaciones del servidor
        */
        String msgError = "";
        GenericValidator gc = new GenericValidator();    
        if(idConceptoSeleccionado <= 0)
            msgError += "<ul>Seleccione un producto.";         
        if(idSgfensProveedorProducto <= 0 && (!mode.equals("")))
            msgError += "<ul>El dato ID 'SgfensProveedorProducto' es requerido";
        /*
        if(idVendedor<=0)
            msgError += "<ul>El dato 'Vendedor' es requerido";
     * */

        if(msgError.equals("")){
            if(idSgfensProveedorProducto>0){
                if (mode.equals("1")){
                /*
                * Editar
                */
                    SGProveedorProductoBO sgfensProveedorProductoBO = new SGProveedorProductoBO(idSgfensProveedorProducto,user.getConn());
                    SgfensProveedorProducto sgfensProveedorProductoDto = sgfensProveedorProductoBO.getSgfensProveedorProducto();

                    sgfensProveedorProductoDto.setIdSgfensProductoServicio(idConceptoSeleccionado);
                    sgfensProveedorProductoDto.setIdSgfensProductoProveedor(idProveedor);
                    sgfensProveedorProductoDto.setVolumenDisponible(volumenSgfensProveedorProducto);
                    sgfensProveedorProductoDto.setCaducidad(fecha_caducidad);
                    sgfensProveedorProductoDto.setFechaDisponibilidad(fecha_disponibilidad);
                    sgfensProveedorProductoDto.setUnidad(unidadSgfensProveedorProducto);
                    sgfensProveedorProductoDto.setPrecioMenudeo(precioMenudeoSgfensProveedorProducto);                
                    sgfensProveedorProductoDto.setMaxMenudeo(hastaMenudeoSgfensProveedorProducto);
                    sgfensProveedorProductoDto.setPrecioMedioMayoreo(precioMedioMayoreoSgfensProveedorProducto);
                    sgfensProveedorProductoDto.setMinMedioMayoreo(desdeMedioMayoreoSgfensProveedorProducto);
                    sgfensProveedorProductoDto.setMaxMedioMayoreo(hastaMedioMayoreoSgfensProveedorProducto);
                    sgfensProveedorProductoDto.setPrecioMayoreo(precioMayoreoSgfensProveedorProducto);
                    sgfensProveedorProductoDto.setMinMayoreo(desdeMayoreoSgfensProveedorProducto);
                    sgfensProveedorProductoDto.setNombreImagenProductoServicio(nombreImagenSgfensProveedorProducto);
                    sgfensProveedorProductoDto.setIdCategoria(idCategoriaSgfensProveedorProducto);

                    try{
                        new SgfensProveedorProductoDaoImpl(user.getConn()).update(sgfensProveedorProductoDto.createPk(), sgfensProveedorProductoDto);

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
                    SgfensProveedorProducto sgfensProveedorProductoDto = new SgfensProveedorProducto();
                    SgfensProveedorProductoDaoImpl sgfensProveedorProductosDaoImpl = new SgfensProveedorProductoDaoImpl(user.getConn());

                    sgfensProveedorProductoDto.setIdSgfensProductoServicio(idConceptoSeleccionado);
                    sgfensProveedorProductoDto.setIdSgfensProductoProveedor(idProveedor);
                    sgfensProveedorProductoDto.setVolumenDisponible(volumenSgfensProveedorProducto);
                    sgfensProveedorProductoDto.setCaducidad(fecha_caducidad);
                    sgfensProveedorProductoDto.setFechaDisponibilidad(fecha_disponibilidad);
                    sgfensProveedorProductoDto.setUnidad(unidadSgfensProveedorProducto);
                    sgfensProveedorProductoDto.setPrecioMenudeo(precioMenudeoSgfensProveedorProducto);                
                    sgfensProveedorProductoDto.setMaxMenudeo(hastaMenudeoSgfensProveedorProducto);
                    sgfensProveedorProductoDto.setPrecioMedioMayoreo(precioMedioMayoreoSgfensProveedorProducto);
                    sgfensProveedorProductoDto.setMinMedioMayoreo(desdeMedioMayoreoSgfensProveedorProducto);
                    sgfensProveedorProductoDto.setMaxMedioMayoreo(hastaMedioMayoreoSgfensProveedorProducto);
                    sgfensProveedorProductoDto.setPrecioMayoreo(precioMayoreoSgfensProveedorProducto);
                    sgfensProveedorProductoDto.setMinMayoreo(desdeMayoreoSgfensProveedorProducto);
                    sgfensProveedorProductoDto.setNombreImagenProductoServicio(nombreImagenSgfensProveedorProducto);
                    sgfensProveedorProductoDto.setIdCategoria(idCategoriaSgfensProveedorProducto);

                    /**
                     * Realizamos el insert
                     */
                    sgfensProveedorProductosDaoImpl.insert(sgfensProveedorProductoDto);

                    out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");

                }catch(Exception e){
                    e.printStackTrace();
                    msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
                }
            }
        }else{
            out.print("<!--ERROR-->"+msgError);
        }
    
    }

    
%>

<script>
    //cargamos la info:
    <%if(sgfensProductoServicio != null){%>
        $("#unidadSgfensProveedorProducto").val('<%=sgfensProductoServicio.getUnidad()%>');        
        $("#precioMenudeoSgfensProveedorProducto").val('<%=sgfensProductoServicio.getPrecioMenudeo()%>');
        $("#hastaMenudeoSgfensProveedorProducto").val('<%=sgfensProductoServicio.getMaxMenudeo()%>');
        $("#precioMedioMayoreoSgfensProveedorProducto").val('<%=sgfensProductoServicio.getPrecioMedioMayoreo()%>');
        $("#desdeMedioMayoreoSgfensProveedorProducto").val('<%=sgfensProductoServicio.getMinMedioMayoreo()%>');
        $("#hastaMedioMayoreoSgfensProveedorProducto").val('<%=sgfensProductoServicio.getMaxMedioMayoreo()%>');
        $("#precioMayoreoSgfensProveedorProducto").val('<%=sgfensProductoServicio.getPrecioMayoreo()%>');
        $("#desdeMayoreoSgfensProveedorProducto").val('<%=sgfensProductoServicio.getMinMayoreo()%>');
        $("#idCategoriaSgfensProveedorProducto").val('<%=sgfensProductoServicio.getIdCategoria()%>');
        $("#nombreImagenSgfensProveedorProducto").val('<%=sgfensProductoServicio.getNombreImagenProductoServicio()%>');
        
    <%}else{%>
        $("#unidadSgfensProveedorProducto").val('');        
        $("#precioMenudeoSgfensProveedorProducto").val('0');
        $("#hastaMenudeoSgfensProveedorProducto").val('0');
        $("#precioMedioMayoreoSgfensProveedorProducto").val('0');
        $("#desdeMedioMayoreoSgfensProveedorProducto").val('0');
        $("#hastaMedioMayoreoSgfensProveedorProducto").val('0');
        $("#precioMayoreoSgfensProveedorProducto").val('0');
        $("#desdeMayoreoSgfensProveedorProducto").val('0');
        $("#idCategoriaSgfensProveedorProducto").val('-1');
        $("#nombreImagenSgfensProveedorProducto").val('');
    <%}%>
</script>