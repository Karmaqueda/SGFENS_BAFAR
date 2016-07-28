<%-- 
    Document   : catCallCenter_ajax
    Created on : 23/02/2016, 05:02:10 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.ZonaHorariaBO"%>
<%@page import="com.tsp.sct.bo.SmsEnvioLoteBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.dao.jdbc.CallCenterSeguimientoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CallCenterSeguimiento"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.jdbc.CallCenterFoliadoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CallCenterFoliado"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.CallCenterBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.CallCenterDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CallCenter"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idCallCenter = -1;
    int idCliente = -1;
    int idTipoCallCenter = -1;
    String nombreCallCenter ="";
    String aPaternoCallCenter = "";
    String aMaternoCallCenter = "";
    String telefonoCallCenter = "";
    String correoCallCenter = "";
    String descripcion ="";  
    int noEsCliente = -1;
    String descripcionSeguimientoCallCenter = "";
    int cerrarTicket = -1;
    int reabrirTicket = -1;
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idCallCenter = Integer.parseInt(request.getParameter("idCallCenter"));
    }catch(NumberFormatException ex){}
    try{
        idCliente = Integer.parseInt(request.getParameter("q_idcliente"));
    }catch(NumberFormatException ex){}
    try{
        idTipoCallCenter = Integer.parseInt(request.getParameter("idTipoCallCenter"));
    }catch(NumberFormatException ex){}
    nombreCallCenter = request.getParameter("nombreCallCenter")!=null?new String(request.getParameter("nombreCallCenter").getBytes("ISO-8859-1"),"UTF-8"):"";
    aPaternoCallCenter = request.getParameter("aPaternoCallCenter")!=null?new String(request.getParameter("aPaternoCallCenter").getBytes("ISO-8859-1"),"UTF-8"):"";
    aMaternoCallCenter = request.getParameter("aMaternoCallCenter")!=null?new String(request.getParameter("aMaternoCallCenter").getBytes("ISO-8859-1"),"UTF-8"):"";
    telefonoCallCenter = request.getParameter("telefonoCallCenter")!=null?new String(request.getParameter("telefonoCallCenter").getBytes("ISO-8859-1"),"UTF-8"):"";
    correoCallCenter = request.getParameter("correoCallCenter")!=null?new String(request.getParameter("correoCallCenter").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcion = request.getParameter("descripcionCallCenter")!=null?new String(request.getParameter("descripcionCallCenter").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        noEsCliente = Integer.parseInt(request.getParameter("noEsCliente"));
    }catch(NumberFormatException ex){}   
    
    descripcionSeguimientoCallCenter = request.getParameter("descripcionSeguimientoCallCenter")!=null?new String(request.getParameter("descripcionSeguimientoCallCenter").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        cerrarTicket = Integer.parseInt(request.getParameter("cerrarTicket"));
    }catch(NumberFormatException ex){}
    try{
        reabrirTicket = Integer.parseInt(request.getParameter("reabrirTicket"));
    }catch(NumberFormatException ex){}
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();   
    
    if(idCallCenter < 0){
        if(idCliente < 0 && noEsCliente > 0){
            if(!gc.isValidString(nombreCallCenter, 1, 40))
                msgError += "<ul>El dato 'nombre' es requerido.";
            if(!gc.isValidString(aPaternoCallCenter, 1, 40))
                msgError += "<ul>El dato 'apellido paterno' es requerido";
            if(!gc.isValidString(telefonoCallCenter, 1, 50))
                msgError += "<ul>El dato 'teléfono' es requerido";
        }else if(idCliente < 0 ){
            msgError += "<ul>El dato 'cliente' es requerido.";
        }

        if(!gc.isValidString(descripcion, 1, 450))
                msgError += "<ul>El dato 'descripción' es requerido"; 

        if(idCallCenter <= 0 && (!mode.equals("")))
            msgError += "<ul>El dato ID 'callCenter' es requerido";
    }else if(mode.equals("2")){ //edicion del ticket original
        if(!gc.isValidString(descripcion, 1, 450))
                msgError += "<ul>El dato 'descripcion' es requerido.";
    }else{
        if(!gc.isValidString(descripcionSeguimientoCallCenter, 1, 450))
                msgError += "<ul>El dato 'descripcion' es requerido.";
    }

    if(msgError.equals("")){
        if(idCallCenter>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                CallCenterBO callCenterBO = new CallCenterBO(idCallCenter,user.getConn());
                CallCenter callCenterDto = callCenterBO.getCallCenter();
                
                //le decimos que ya se ha registrado almenos un seguimiento:
                callCenterDto.setConSeguimiento(1);
                
                if(cerrarTicket == 1){
                    callCenterDto.setEstado(2);
                }
                
                if(reabrirTicket == 1){
                    callCenterDto.setEstado(3);
                }
                
                
                
                
                //aqui para editar vamos a usarlo cuando se crea un seguimiento al ticket principal 
                //creamos el registro de seguimiento
                CallCenterSeguimiento seguimiento = new CallCenterSeguimiento();
                seguimiento.setIdCallCenter(idCallCenter);
                seguimiento.setIdUsuario(user.getUser().getIdUsuarios());
                try{
                    seguimiento.setFechaCreacion(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                }catch(Exception e){
                    seguimiento.setFechaCreacion(new Date());
                }
                seguimiento.setDescripcion(descripcionSeguimientoCallCenter);
                
                try{
                    new CallCenterSeguimientoDaoImpl(user.getConn()).insert(seguimiento);
                }catch(Exception e){}
                
                //Crear Notificacion SMS
                try{
                    EmpresaPermisoAplicacion empresaPermisoAplicacion 
                        = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(new EmpresaBO(user.getConn()).getEmpresaMatriz(callCenterDto.getIdEmpresa()).getIdEmpresa()); 
                    if (empresaPermisoAplicacion!=null && empresaPermisoAplicacion.getAccesoModuloSms()==1){
                    // Solo si la empresa matriz tiene contratado el modulo SMS
                        SmsEnvioLoteBO smsEnvioLoteBO = new SmsEnvioLoteBO(user.getConn());
                        if(cerrarTicket == 1){
                            smsEnvioLoteBO.crearSmsNotificacionSistema(SmsEnvioLoteBO.TipoNotificaSistema.CALLCENTER_CIERRE, callCenterDto.getIdEmpresa(), callCenterDto.getIdCallCenter());
                        }else{
                            smsEnvioLoteBO.crearSmsNotificacionSistema(SmsEnvioLoteBO.TipoNotificaSistema.CALLCENTER_CAMBIO, callCenterDto.getIdEmpresa(), callCenterDto.getIdCallCenter());
                        }
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                
                try{
                    new CallCenterDaoImpl(user.getConn()).update(callCenterDto.createPk(), callCenterDto);

                    out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }
                
            }else if (mode.equals("2")){ //editamos el ticket original
                /*
            * Editar
            */
                CallCenterBO callCenterBO = new CallCenterBO(idCallCenter,user.getConn());
                CallCenter callCenterDto = callCenterBO.getCallCenter();
                callCenterDto.setDescripcion(descripcion);
                
                try{
                    new CallCenterDaoImpl(user.getConn()).update(callCenterDto.createPk(), callCenterDto);

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
                CallCenter callCenterDto = new CallCenter();
                CallCenterDaoImpl callCentersDaoImpl = new CallCenterDaoImpl(user.getConn());
                
                callCenterDto.setIdEmpresa(idEmpresa);
                
                if(idCliente < 0 && noEsCliente > 0){//no es un cliente
                    callCenterDto.setNombre(nombreCallCenter );
                    callCenterDto.setApellidoPaterno(aPaternoCallCenter );
                    callCenterDto.setApellidoMaterno(aMaternoCallCenter );
                    callCenterDto.setTelefono(telefonoCallCenter );
                    callCenterDto.setCorreo(correoCallCenter );                    
                }else{
                    callCenterDto.setIdCliente(idCliente );
                }
                
                callCenterDto.setTipo(idTipoCallCenter);
                callCenterDto.setEstado(1);
                callCenterDto.setDescripcion(descripcion);                              
                callCenterDto.setIdUsuario(user.getUser().getIdUsuarios());
                
                //obtenemos el numero de ticket que va a ser asignado
                CallCenterFoliado callCentarFoliado = null;
                EmpresaBO empresaBO = new EmpresaBO(user.getConn());
                try{
                    callCentarFoliado = new CallCenterFoliadoDaoImpl(user.getConn()).findWhereIdEmpresaEquals(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa())[0];
                }catch(Exception e){}
                
                if(callCentarFoliado == null){// si no hay foliado es porque va a ser nuevo el folio y será el primero
                    callCenterDto.setNumeroTicket(1 + "");
                    //creamos el registro de ticket:
                    callCentarFoliado = new CallCenterFoliado();
                    callCentarFoliado.setIdEmpresa(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());
                    callCentarFoliado.setIdEstatus(1);
                    callCentarFoliado.setUltimoFolio(1);
                    new CallCenterFoliadoDaoImpl(user.getConn()).insert(callCentarFoliado);
                }else{
                    callCenterDto.setNumeroTicket( (callCentarFoliado.getUltimoFolio()+1) + ""); 
                    //actualizamos el registro de ticket
                    callCentarFoliado.setUltimoFolio((callCentarFoliado.getUltimoFolio()+1));
                    new CallCenterFoliadoDaoImpl(user.getConn()).update(callCentarFoliado.createPk(), callCentarFoliado);
                }
                try{
                    callCenterDto.setFechaCreacion(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                }catch(Exception e){
                    callCenterDto.setFechaCreacion(new Date());
                }
                callCenterDto.setConSeguimiento(0);//sin seguimiento

                /**
                 * Realizamos el insert
                 */
                callCentersDaoImpl.insert(callCenterDto);
                
                //Crear Notificacion SMS
                try{
                    EmpresaPermisoAplicacion empresaPermisoAplicacion 
                        = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(new EmpresaBO(user.getConn()).getEmpresaMatriz(callCenterDto.getIdEmpresa()).getIdEmpresa()); 
                    if (empresaPermisoAplicacion!=null && empresaPermisoAplicacion.getAccesoModuloSms()==1){
                    // Solo si la empresa matriz tiene contratado el modulo SMS
                        SmsEnvioLoteBO smsEnvioLoteBO = new SmsEnvioLoteBO(user.getConn());
                        smsEnvioLoteBO.crearSmsNotificacionSistema(SmsEnvioLoteBO.TipoNotificaSistema.CALLCENTER_NUEVO, callCenterDto.getIdEmpresa(), callCenterDto.getIdCallCenter());
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }

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