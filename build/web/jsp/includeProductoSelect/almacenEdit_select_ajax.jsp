<%-- 
    Document   : almacenEdit_select_ajax
    Created on : 27/04/2016, 01:49:05 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sgfens.sesion.ProductoSesion"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="pedidoSesion" scope="session" class="com.tsp.sgfens.sesion.PedidoSesion"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int index_lista_producto = -1;
    String nombreViaEmbarque ="";
    
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        index_lista_producto = Integer.parseInt(request.getParameter("index_lista_producto"));
    }catch(NumberFormatException ex){}
    //nombreViaEmbarque = request.getParameter("nombreViaEmbarque")!=null?new String(request.getParameter("nombreViaEmbarque").getBytes("ISO-8859-1"),"UTF-8"):"";
    
    int idAlmacen=-1;    
    try{ idAlmacen = Integer.parseInt(request.getParameter("idAlmacen")); }catch(Exception e){}
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    
    

    if(msgError.equals("")){        
            if (mode.equals("1")){//modificar el almacen de salida de producto
            /*
            * modificar el almacen de salida de producto
            */
            
                ProductoSesion productoSesion = null;
                if (pedidoSesion!=null){
                    if (pedidoSesion.getListaProducto()!=null){
                        if (pedidoSesion.getListaProducto().size()>0){
                            try{
                                productoSesion = pedidoSesion.getListaProducto().get(index_lista_producto);
                            }catch(Exception e){
                                msgError += "<ul>El producto que selecciono no existe en sesion. Intente recargar la página.";
                            }
                        }else{
                            msgError += "<ul>La lista de Productos esta vacía. Intente recargar la página";
                        }
                    }else{
                        msgError += "<ul>La lista de Productos esta vacía. Intente recargar la página";
                    }
                }

                if(productoSesion!=null){
                    productoSesion.setIdAlmacen(idAlmacen);
                    pedidoSesion.getListaProducto().set(index_lista_producto, productoSesion);
                    request.getSession().setAttribute("pedidoSesion", pedidoSesion);
                }
                
                
            }else{
                out.print("<!--ERROR-->Acción no válida.");
            }
        
    }else{
        out.print("<!--ERROR-->"+msgError);
    }

%>