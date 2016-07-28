<%-- 
    Document   : catPaquetes_ajax
    Created on : 25/09/2014, 11:40:34 AM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.dao.dto.PaquetePk"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.tsp.sct.bo.PaqueteRelacionConceptoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.PaqueteRelacionConceptoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.PaqueteRelacionConcepto"%>
<%@page import="com.tsp.sct.dao.jdbc.PaqueteDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Paquete"%>
<%@page import="com.tsp.sct.bo.PaqueteBO"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idPaquete = -1;
    String nombrePaquete ="";
    String descripcion ="";  
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idPaquete = Integer.parseInt(request.getParameter("idPaquete"));
    }catch(NumberFormatException ex){}
    nombrePaquete = request.getParameter("nombrePaquete")!=null?new String(request.getParameter("nombrePaquete").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";    
    descripcion = descripcion.trim();
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    
    if(!mode.equals("3")){
        GenericValidator gc = new GenericValidator();    
        if(!gc.isValidString(nombrePaquete, 1, 30) && (!mode.equals("2")) )
            msgError += "<ul>El dato 'nombre' es requerido.";
/*        if(!gc.isValidString(descripcion, 1, 100) && (!mode.equals("2")))
            msgError += "<ul>El dato 'descripción' es requerido";   */
        if(idPaquete <= 0 && (!mode.equals("")) && (!mode.equals("2")))
            msgError += "<ul>El dato ID 'paquete' es requerido";
    }
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idPaquete>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                PaqueteBO paqueteBO = new PaqueteBO(idPaquete,user.getConn());
                Paquete paqueteDto = paqueteBO.getPaquete();
                
                paqueteDto.setIdEstatus(estatus);
                paqueteDto.setNombre(nombrePaquete);
                paqueteDto.setDescripcion(descripcion);
               
                
                try{
                    new PaqueteDaoImpl(user.getConn()).update(paqueteDto.createPk(), paqueteDto);

                    out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }
                
            }else if(mode.equals("2")){//editando un paquete para agregar mas productos:
                //recuperamos los datos del form y hacemos las validaciones:
                //String nombrePaquete = request.getParameter("nombrePaquete")!=null?new String(request.getParameter("nombrePaquete").getBytes("ISO-8859-1"),"UTF-8"):"";
                int producto_select = -1;
                try{
                    producto_select = Integer.parseInt(request.getParameter("producto_select"));
                }catch(NumberFormatException ex){}
                int cantidadConceptoPaquete = 0;
                try{
                    cantidadConceptoPaquete = Integer.parseInt(request.getParameter("cantidadConceptoPaquete"));
                }catch(Exception e){}
                double precioConceptoPaquete = 0;
                try{
                    precioConceptoPaquete = Double.parseDouble(request.getParameter("precioConceptoPaquete"));
                }catch(Exception e){}
                
                String msgValidacion = "";
                if(producto_select < 0){
                    msgValidacion += "<ul>Seleccione un concepto";
                }
                
                if(msgValidacion.trim().equals("")){
                    //validamos si el concepto ya esta asignado al paquete:
                    boolean coincidenciaPrevia=false;
                    PaqueteRelacionConcepto[] paqueteConceptos = new PaqueteRelacionConceptoBO(user.getConn()).findPaqueteRelacionConceptos(0, 0, 0, 0, " AND ID_PAQUETE = "+idPaquete + " AND ID_ESTATUS != 2 ");
                    for (PaqueteRelacionConcepto itemProducto : paqueteConceptos){
                        if (itemProducto.getIdConcepto() == producto_select){
                            //Ya se había seleccionado este producto
                            coincidenciaPrevia=true;
                            break;
                        }
                    }                    
                    if (!coincidenciaPrevia){
                        PaqueteRelacionConcepto concepto = new PaqueteRelacionConcepto();
                        concepto.setIdEstatus(1);
                        concepto.setIdPaquete(idPaquete);
                        concepto.setIdConcepto(producto_select);
                        concepto.setCantidad(cantidadConceptoPaquete);
                        concepto.setPrecio(precioConceptoPaquete);

                        new PaqueteRelacionConceptoDaoImpl(user.getConn()).insert(concepto);
                        out.print("<!--EXITO-->Producto agregado al paquete satisfactoriamente.<br/>");
                    }else{
                        out.print("<!--ERROR-->El producto ya fue seleccionado previamente. Modifique los valores o elimine el producto del listado.");
                    }
                }else{
                    out.print("<!--ERROR-->"+msgValidacion);
                }
                
            }else if(mode.equals("3")){ //borramos un producto de la lista del paquete
                int idPaqueteRelacionConcepto = -1;
                try{
                    idPaqueteRelacionConcepto = Integer.parseInt(request.getParameter("idPaqueteRelacionConcepto"));
                }catch(NumberFormatException ex){}
                PaqueteRelacionConcepto relacion = null;
                relacion = new PaqueteRelacionConceptoBO(idPaqueteRelacionConcepto, user.getConn()).getPaqueteRelacionConcepto();
                if(relacion != null){
                    relacion.setIdEstatus(2);
                    new PaqueteRelacionConceptoDaoImpl(user.getConn()).update(relacion.createPk(), relacion);
                    out.print("<!--EXITO-->Registro borrado satisfactoriamente.<br/>");
                }else{
                    out.print("<!--ERROR-->No se encontro elemento a borrar");
                }
            }else{
                out.print("<!--ERROR-->Acción no válida.");
            }
            
        }else{
            /*
            *  Nuevo
            */
            
            if(mode.equals("2")){//si no existe ID de paquete (paquete nuevo), pero se van a agregar nuevos productos a un paquete que se esta creando
                int producto_select = -1;
                try{
                    producto_select = Integer.parseInt(request.getParameter("producto_select"));
                }catch(NumberFormatException ex){}
                int cantidadConceptoPaquete = 0;
                try{
                    cantidadConceptoPaquete = Integer.parseInt(request.getParameter("cantidadConceptoPaquete"));
                }catch(Exception e){}
                double precioConceptoPaquete = 0;
                try{
                    precioConceptoPaquete = Double.parseDouble(request.getParameter("precioConceptoPaquete"));
                }catch(Exception e){}

                String msgValidacion = "";
                if(producto_select < 0){//validamos que selecciono un producto
                    msgValidacion += "<ul>Seleccione un concepto";
                }

                if(msgValidacion.trim().equals("")){ //validamos si no hay mensaje de error
                    ArrayList<PaqueteRelacionConcepto> listaProducto = (ArrayList<PaqueteRelacionConcepto>)session.getAttribute("listaProducto");

                    if (listaProducto==null){
                        listaProducto = new ArrayList<PaqueteRelacionConcepto>();                
                    }
                    boolean coincidenciaPrevia=false;
                    for (PaqueteRelacionConcepto itemProducto:listaProducto){
                        if (itemProducto.getIdConcepto() == producto_select){
                            //Ya se había seleccionado este producto
                            coincidenciaPrevia=true;
                            break;
                        }
                    }
                    if (!coincidenciaPrevia){
                        PaqueteRelacionConcepto nuevoProductoCompraSesion = new PaqueteRelacionConcepto();
                        nuevoProductoCompraSesion.setCantidad(cantidadConceptoPaquete);
                        nuevoProductoCompraSesion.setIdConcepto(producto_select);                    
                        nuevoProductoCompraSesion.setPrecio(precioConceptoPaquete);
                        nuevoProductoCompraSesion.setIdPaqueteRelacionConcepto(producto_select);
                        nuevoProductoCompraSesion.setIdEstatus(1);
                        listaProducto.add(nuevoProductoCompraSesion);
                        session.setAttribute("listaProducto", listaProducto);
                        out.print("<!--EXITO-->Producto agregado al paquete satisfactoriamente.<br/>");
                    }else{
                        msgValidacion +="<ul> El producto ya fue seleccionado previamente. Modifique los valores o elimine el producto del listado.";
                        out.print("<!--ERROR-->"+msgValidacion);
                    }
                }else{
                    out.print("<!--ERROR-->"+msgValidacion);
                }
            }else if(mode.equals("3")){ //borramos un producto de la lista del paquete
                int idPaqueteRelacionConcepto = -1;
                try{
                    idPaqueteRelacionConcepto = Integer.parseInt(request.getParameter("idPaqueteRelacionConcepto"));
                }catch(NumberFormatException ex){}
                ArrayList<PaqueteRelacionConcepto> listaProducto = (ArrayList<PaqueteRelacionConcepto>)session.getAttribute("listaProducto");
                try{
                    int positionElementoBorrar = -1;
                    boolean coincidenciaPrevia=false;
                    for (PaqueteRelacionConcepto itemProducto : listaProducto){
                        positionElementoBorrar++;
                        if (itemProducto.getIdConcepto() == idPaqueteRelacionConcepto){ //comparamos si es el elemento a borrar                           
                            coincidenciaPrevia=true;
                            break;
                        }
                    }
                    if(coincidenciaPrevia){//validamos si se encontro el elemento a borrar
                        listaProducto.remove((positionElementoBorrar));
                        session.setAttribute("listaProducto", listaProducto);
                        out.print("<!--EXITO-->Registro borrado satisfactoriamente.<br/>");
                    }else{
                        out.print("<!--ERROR-->No se encontro elemento a borrar");
                    }
                }catch(Exception e){
                    out.print("<!--ERROR-->No se encontro elemento a borrar");
                    e.printStackTrace();
                }
                
            }else{
                try {                

                    /**
                     * Creamos el registro de Cliente
                     */
                    Paquete paqueteDto = new Paquete();
                    PaqueteDaoImpl paquetesDaoImpl = new PaqueteDaoImpl(user.getConn());

                    paqueteDto.setIdEstatus(estatus);
                    paqueteDto.setNombre(nombrePaquete);
                    paqueteDto.setDescripcion(descripcion);                              
                    paqueteDto.setIdEmpresa(idEmpresa);

                    /**
                     * Realizamos el insert
                     */
                    PaquetePk paquetePk = paquetesDaoImpl.insert(paqueteDto);
                    
                    //***ahora insertamos los productos que fueron agregados
                    ArrayList<PaqueteRelacionConcepto> listaProducto = (ArrayList<PaqueteRelacionConcepto>)session.getAttribute("listaProducto");
                    if(listaProducto != null){        
                        PaqueteRelacionConceptoDaoImpl daoImpl = new PaqueteRelacionConceptoDaoImpl(user.getConn());
                        for (PaqueteRelacionConcepto itemProducto : listaProducto){
                            itemProducto.setIdPaquete(paquetePk.getIdPaquete());
                            itemProducto.setIdPaqueteRelacionConceptoModified(false);
                            daoImpl.insert(itemProducto);
                        }                    
                    
                    }
                    //***

                    out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");

                }catch(Exception e){
                    e.printStackTrace();
                    msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
                }
            }
        }
    }else{
        out.print("<!--ERROR-->"+msgError);
    }

%>