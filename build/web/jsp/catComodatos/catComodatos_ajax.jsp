<%-- 
    Document   : catComodatos_ajax
    Created on : 3/03/2016, 04:09:13 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.ZonaHorariaBO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.dao.jdbc.ComodatoMantenimientoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.ComodatoMantenimiento"%>
<%@page import="com.tsp.sct.dao.dto.ComodatoProducto"%>
<%@page import="com.tsp.sct.bo.ComodatoProductoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.ComodatoProductoDaoImpl"%>
<%@page import="com.tsp.sct.util.test.qryAlmacenProductos"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.ComodatoBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.ComodatoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Comodato"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idComodato = -1;
    String nombreComodato ="";
    String descripcion ="";  
    String noSerieComodato ="";
    String marcaComodato = "";
    String modeloComodato = "";
    String tipoComodato = "";
    String capacidadComodato = "";
    int estadoComodato = 0;
    int idAlmacenComodato = 0;
    int q_idcliente = 0;
    String direccionComodato = "";
    String nombreRecibeComodato = "";
    int q_idvendedor = 0;
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idComodato = Integer.parseInt(request.getParameter("idComodato"));
    }catch(NumberFormatException ex){}
    nombreComodato = request.getParameter("nombreComodato")!=null?new String(request.getParameter("nombreComodato").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";    
    noSerieComodato = request.getParameter("noSerieComodato")!=null?new String(request.getParameter("noSerieComodato").getBytes("ISO-8859-1"),"UTF-8"):"";    
    marcaComodato = request.getParameter("marcaComodato")!=null?new String(request.getParameter("marcaComodato").getBytes("ISO-8859-1"),"UTF-8"):"";    
    modeloComodato = request.getParameter("modeloComodato")!=null?new String(request.getParameter("modeloComodato").getBytes("ISO-8859-1"),"UTF-8"):"";    
    tipoComodato = request.getParameter("tipoComodato")!=null?new String(request.getParameter("tipoComodato").getBytes("ISO-8859-1"),"UTF-8"):"";    
    capacidadComodato = request.getParameter("capacidadComodato")!=null?new String(request.getParameter("capacidadComodato").getBytes("ISO-8859-1"),"UTF-8"):"";    
    direccionComodato = request.getParameter("direccionComodato")!=null?new String(request.getParameter("direccionComodato").getBytes("ISO-8859-1"),"UTF-8"):"";    
    nombreRecibeComodato = request.getParameter("nombreRecibeComodato")!=null?new String(request.getParameter("nombreRecibeComodato").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estadoComodato = Integer.parseInt(request.getParameter("estadoComodato"));
    }catch(NumberFormatException ex){}  
    try{
        idAlmacenComodato = Integer.parseInt(request.getParameter("idAlmacenComodato"));
    }catch(NumberFormatException ex){}
    try{
        q_idcliente = Integer.parseInt(request.getParameter("q_idcliente"));
    }catch(NumberFormatException ex){}
    try{
        q_idvendedor = Integer.parseInt(request.getParameter("q_idvendedor"));
    }catch(NumberFormatException ex){}
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator(); 
    if(!mode.equals("asignar") && !mode.equals("eliminarComodato") &&  !mode.equals("contrato") && !mode.equals("eliminarComodatoProducto") && !mode.equals("producto")&& !mode.equals("mantenimiento")){ 
        if(!gc.isValidString(nombreComodato, 1, 120))
            msgError += "<ul>El dato 'nombre' es requerido.";
        if(!gc.isValidString(descripcion, 1, 500))
            msgError += "<ul>El dato 'descripción' es requerido"; 
        if(!gc.isValidString(noSerieComodato, 1, 500))
            msgError += "<ul>El dato 'Número de Serie' es requerido"; 
        if(idComodato <= 0 && (!mode.equals("")))
            msgError += "<ul>El dato ID 'Comodato' es requerido";
    }
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idComodato>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                ComodatoBO comodatoBO = new ComodatoBO(idComodato,user.getConn());
                Comodato comodatoDto = comodatoBO.getComodato();
                
                comodatoDto.setNombre(nombreComodato);
                comodatoDto.setDescripcion(descripcion);
                comodatoDto.setNumeroSerie(noSerieComodato );
                comodatoDto.setMarca(marcaComodato );
                comodatoDto.setModelo(modeloComodato );
                comodatoDto.setTipo(tipoComodato );
                comodatoDto.setCapacidad(capacidadComodato );                
                comodatoDto.setEstado(estadoComodato);
                comodatoDto.setEstatus(2);//quiere decir disponible el 2
                comodatoDto.setIdAlmacen(idAlmacenComodato );
                comodatoDto.setIdCliente(q_idcliente);
                if(q_idcliente > 0 ){
                    comodatoDto.setEstatus(1);//como dato/asignado a cliente, el 1s
                    try{
                        comodatoDto.setFechaAsignacionCliente(new Date());
                    }catch(Exception e){
                        comodatoDto.setFechaAsignacionCliente(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                    }
                }
                comodatoDto.setNombreRecibe("");
                comodatoDto.setDireccionResguardo(direccionComodato);
               
                
                try{
                    new ComodatoDaoImpl(user.getConn()).update(comodatoDto.createPk(), comodatoDto);

                    out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }
                
            }else if (mode.equals("asignar")){//para asignar el equipo al comodato
                ComodatoBO comodatoBO = new ComodatoBO(idComodato,user.getConn());
                Comodato comodatoDto = comodatoBO.getComodato();
                try{
                    comodatoDto.setFechaAsignacionCliente(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                }catch(Exception e){
                    comodatoDto.setFechaAsignacionCliente(new Date());
                }
                comodatoDto.setIdCliente(q_idcliente);
                comodatoDto.setNombreRecibe(nombreRecibeComodato);
                comodatoDto.setDireccionResguardo(direccionComodato);
                comodatoDto.setIdUsuarioVendedor(q_idvendedor);
                if(q_idcliente > 0){
                    comodatoDto.setEstatus(1);//comodato, asignado a cliente
                }else{
                    comodatoDto.setEstatus(2);//comodato, asignado a cliente
                }
                
                
                try{
                    new ComodatoDaoImpl(user.getConn()).update(comodatoDto.createPk(), comodatoDto);
                    out.print("<!--EXITO-->Registro asignado satisfactoriamente");
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo asignar el registro. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }
            
            }else if(mode.equals("eliminarComodato")){
                
                ComodatoBO comodatoBO = new ComodatoBO(idComodato,user.getConn());
                Comodato comodatoDto = comodatoBO.getComodato();
                comodatoDto.setEstatus(3);//3 = eliminar/inactivo
                    
                try{
                    new ComodatoDaoImpl(user.getConn()).update(comodatoDto.createPk(), comodatoDto);
                    out.print("<!--EXITO-->Registro eliminado satisfactoriamente");
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo eliminar el registro. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }   
                    
            }else if(mode.equals("contrato")){
                
                String nombreArchivoImagen = request.getParameter("nombreArchivoImagen")!=null?new String(request.getParameter("nombreArchivoImagen").getBytes("ISO-8859-1"),"UTF-8"):"";    
                ComodatoBO comodatoBO = new ComodatoBO(idComodato,user.getConn());
                Comodato comodatoDto = comodatoBO.getComodato();
                comodatoDto.setContratoNombreArchivo(nombreArchivoImagen);
                try{
                    comodatoDto.setFechaSubidaContrato(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                }catch(Exception e){
                    comodatoDto.setFechaSubidaContrato(new Date());
                }
                    
                try{
                    new ComodatoDaoImpl(user.getConn()).update(comodatoDto.createPk(), comodatoDto);
                    out.print("<!--EXITO-->Registro cargado satisfactoriamente");
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo cargar el registro. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }             
            }else if(mode.equals("eliminarComodatoProducto")){
                System.out.println("....... elimiinar producto...");
                int idComodatoProducto = 0;
                try{
                    idComodatoProducto = Integer.parseInt(request.getParameter("idComodatoProducto"));
                }catch(NumberFormatException ex){}
                ComodatoProductoBO comodatoBO = new ComodatoProductoBO(idComodatoProducto,user.getConn());
                ComodatoProducto comodatoDto = comodatoBO.getComodatoProducto();
                
                try{
                    new ComodatoProductoDaoImpl().delete(comodatoDto.createPk());
                    out.print("<!--EXITO-->Registro eliminado satisfactoriamente");
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo eliminar el registro. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }           
            }else if(mode.equals("mantenimiento")){
                int idComodatoMantenimiento = 0;
                try{
                    idComodatoMantenimiento = Integer.parseInt(request.getParameter("idComodatoMantenimiento"));
                }catch(NumberFormatException ex){}
                int estatusComodatoMantenimiento = 0;
                try{
                    estatusComodatoMantenimiento = Integer.parseInt(request.getParameter("estatusComodatoMantenimiento"));
                }catch(NumberFormatException ex){}
                String tecnicoComodatoMantenimiento = request.getParameter("tecnicoComodatoMantenimiento")!=null?new String(request.getParameter("tecnicoComodatoMantenimiento").getBytes("ISO-8859-1"),"UTF-8"):"";    
                String descripcionComodatoMantenimiento = request.getParameter("descripcionComodatoMantenimiento")!=null?new String(request.getParameter("descripcionComodatoMantenimiento").getBytes("ISO-8859-1"),"UTF-8"):"";    
                String nombreAtendioComodatoMantenimiento = request.getParameter("nombreAtendioComodatoMantenimiento")!=null?new String(request.getParameter("nombreAtendioComodatoMantenimiento").getBytes("ISO-8859-1"),"UTF-8"):"";    
                Date fechaMantenimiento = null;
                try{ fechaMantenimiento = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fechaMantenimiento"));}catch(Exception e){}
                Date fechaProxMantenimiento = null;
                try{ fechaProxMantenimiento = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fechaProxMantenimiento"));}catch(Exception e){}
                double costoComodatoMantenimiento = 0;
                try{
                    costoComodatoMantenimiento = Double.parseDouble(request.getParameter("costoComodatoMantenimiento"));
                }catch(Exception e){}
                
                if(!gc.isValidString(descripcionComodatoMantenimiento, 1, 550))
                msgError += "<ul>El dato 'descripcion' es requerido.";
                
                if(msgError.equals("")){
                    ComodatoMantenimiento comodatoMantenimiento = null;

                    ComodatoBO comodatoBO = new ComodatoBO(idComodato,user.getConn());
                    Comodato comodatoDto = comodatoBO.getComodato();
                    if(estatusComodatoMantenimiento == 2){//quiere decir que esta descompuesto:
                        comodatoDto.setEstatus(4);
                    }else {//verificamos si lo dejamos comodato o bien como disponible
                        if(comodatoDto.getIdCliente() > 0){
                            comodatoDto.setEstatus(1);
                        }else{
                            comodatoDto.setEstatus(2);
                        }
                    }
                    new ComodatoDaoImpl(user.getConn()).update(comodatoDto.createPk(), comodatoDto);

                    if(idComodatoMantenimiento > 0){//editamos el mantenimiento                    
                        try{
                            comodatoMantenimiento = new ComodatoMantenimientoDaoImpl(user.getConn()).findByPrimaryKey(idComodatoMantenimiento);

                            comodatoMantenimiento.setIdCliente(comodatoDto.getIdCliente());
                            comodatoMantenimiento.setIdEmpresa(idEmpresa);
                            comodatoMantenimiento.setTecnico(tecnicoComodatoMantenimiento);
                            comodatoMantenimiento.setDescripcion(descripcionComodatoMantenimiento);
                            comodatoMantenimiento.setEstatus(estatusComodatoMantenimiento);
                            comodatoMantenimiento.setNombreAtendio(nombreAtendioComodatoMantenimiento);
                            comodatoMantenimiento.setFechaRealizacionMantenimiento(fechaMantenimiento);
                            comodatoMantenimiento.setFechaProxMantenimiento(fechaProxMantenimiento);
                            comodatoMantenimiento.setCosto(costoComodatoMantenimiento);

                            new ComodatoMantenimientoDaoImpl(user.getConn()).update(comodatoMantenimiento.createPk(), comodatoMantenimiento);
                            out.print("<!--EXITO-->Registro modificado satisfactoriamente.<br/>");
                        }catch(Exception e){
                            out.print("<!--ERROR-->No se pudo modificar el registro. Informe del error al administrador del sistema: " + e.toString());
                        }
                    }else{//mantenimiento nuevo
                        comodatoMantenimiento = new ComodatoMantenimiento();
                        comodatoMantenimiento.setIdComodato(idComodato);
                        comodatoMantenimiento.setIdCliente(comodatoDto.getIdCliente());
                        comodatoMantenimiento.setIdEmpresa(idEmpresa);
                        comodatoMantenimiento.setTecnico(tecnicoComodatoMantenimiento);
                        comodatoMantenimiento.setDescripcion(descripcionComodatoMantenimiento);
                        comodatoMantenimiento.setEstatus(estatusComodatoMantenimiento);
                        comodatoMantenimiento.setNombreAtendio(nombreAtendioComodatoMantenimiento);
                        comodatoMantenimiento.setFechaRealizacionMantenimiento(fechaMantenimiento);
                        comodatoMantenimiento.setFechaProxMantenimiento(fechaProxMantenimiento);
                        comodatoMantenimiento.setCosto(costoComodatoMantenimiento);
                        try{
                            new ComodatoMantenimientoDaoImpl(user.getConn()).insert(comodatoMantenimiento);
                        out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
                        }catch(Exception e){
                            out.print("<!--ERROR-->No se pudo crear el registro. Informe del error al administrador del sistema: " + e.toString());       
                        }                
                    }
                }else{
                    out.print("<!--ERROR-->"+msgError);
                }
            }else if(mode.equals("producto")){
                int idConceptoRelacionado = 0;
                try{
                    idConceptoRelacionado = Integer.parseInt(request.getParameter("idConceptoRelacionado"));
                }catch(NumberFormatException ex){}
                ComodatoProducto comodatoDto = new ComodatoProducto();
                comodatoDto.setIdComodato(idComodato);
                comodatoDto.setIdConcepto(idConceptoRelacionado);
                
                try{
                    new ComodatoProductoDaoImpl().insert(comodatoDto);
                    out.print("<!--EXITO-->Registro creado satisfactoriamente");
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo crear el registro. Informe del error al administrador del sistema: " + ex.toString());
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
                Comodato comodatoDto = new Comodato();
                ComodatoDaoImpl comodatosDaoImpl = new ComodatoDaoImpl(user.getConn());
                
                comodatoDto.setIdEmpresa(idEmpresa);
                try{
                    comodatoDto.setFechaCreacion(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                }catch(Exception e){
                    comodatoDto.setFechaCreacion(new Date());
                }
                comodatoDto.setNombre(nombreComodato);
                comodatoDto.setDescripcion(descripcion);
                comodatoDto.setNumeroSerie(noSerieComodato );
                comodatoDto.setMarca(marcaComodato );
                comodatoDto.setModelo(modeloComodato );
                comodatoDto.setTipo(tipoComodato );
                comodatoDto.setCapacidad(capacidadComodato );                
                comodatoDto.setEstado(estadoComodato);
                comodatoDto.setEstatus(2);//quiere decir disponible el 2
                comodatoDto.setIdAlmacen(idAlmacenComodato );
                comodatoDto.setIdCliente(q_idcliente);
                if(q_idcliente > 0 ){
                    comodatoDto.setEstatus(1);//como dato/asignado a cliente, el 1s
                    try{
                        comodatoDto.setFechaAsignacionCliente(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                    }catch(Exception e){
                        comodatoDto.setFechaAsignacionCliente(new Date());
                    }
                }
                comodatoDto.setNombreRecibe("");
                comodatoDto.setDireccionResguardo(direccionComodato);

                /**
                 * Realizamos el insert
                 */
                comodatosDaoImpl.insert(comodatoDto);

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