<%-- 
    Document   : catBroadcast_ajax
    Created on : 21/10/2015, 03:55:04 PM
    Author     : HpPyme
--%>

<%@page import="com.tsp.sct.bo.ZonaHorariaBO"%>
<%@page import="com.tsp.sct.dao.dto.ActualizacionesMovilPk"%>
<%@page import="com.tsp.sct.dao.dto.ActualizacionesMovil"%>
<%@page import="com.tsp.sct.dao.jdbc.ActualizacionesMovilDaoImpl"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.dao.dto.Usuarios"%>
<%@page import="com.tsp.sct.bo.NotificacionBO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.NotificacionesUsuariosDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.NotificacionesUsuarios"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idNotificacion = -1;
    String titulo ="";
    String texto ="";  
    int path =5; 
    int estatus = 0;//deshabilitado
    
    int versionCode = 0;
    String numVersion ="";
    Date fechaLiberacion = null;
    String comentarios ="";
    int idPlataforma = 0;
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idNotificacion = Integer.parseInt(request.getParameter("idNotificacion"));
    }catch(NumberFormatException ex){}
    titulo = request.getParameter("titulo")!=null?new String(request.getParameter("titulo").getBytes("ISO-8859-1"),"UTF-8"):"";
    texto = request.getParameter("texto")!=null?new String(request.getParameter("texto").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        path = Integer.parseInt(request.getParameter("path"));
    }catch(NumberFormatException ex){}
       
    
    
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(titulo.trim(), 1, 80))
        msgError += "<ul>El dato 'Titulo' es requerido.";
    if(!gc.isValidString(texto.trim(), 1, 5000))
        msgError += "<ul>El dato 'Mensaje' es requerido";   
    if(path<=0)
        msgError += "<ul>El dato 'Imagen' es requerido";   
    if(path==1){//si es liberacion valida campos de apk
            
            try{
                versionCode = Integer.parseInt(request.getParameter("versionCode"));
            }catch(NumberFormatException ex){}            
            numVersion = request.getParameter("numVersion")!=null?new String(request.getParameter("numVersion").getBytes("ISO-8859-1"),"UTF-8"):"";    
            try{ fechaLiberacion = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fechaLiberacion"));}catch(Exception e){}
            comentarios = request.getParameter("comentarios")!=null?new String(request.getParameter("comentarios").getBytes("ISO-8859-1"),"UTF-8"):"";    
            try{
                idPlataforma = Integer.parseInt(request.getParameter("idPlataforma"));
            }catch(NumberFormatException ex){}
            
            if(versionCode<=0)
                msgError += "<ul>El dato 'Version Code' es requerido";
            if(!gc.isValidString(titulo.trim(), 1, 80))
                msgError += "<ul>El dato 'Número de Versión' es requerido.";
            if(fechaLiberacion==null)
                msgError += "<ul>El dato 'Fecha de Liberación' es requerido.";
            if(!gc.isValidString(comentarios.trim(), 1, 65000))
                msgError += "<ul>El dato 'Comentarios' es requerido.";
            if(idPlataforma<=0)
                msgError += "<ul>El dato 'Plataforma' es requerido";
    }
    
    if (mode.equals("cierraMsj")){
        msgError ="";
    }
    

    if(msgError.equals("")){
        
        
        if (mode.equals("cierraMsj")){
            try{  
               
                NotificacionesUsuarios notificacionesDto = new NotificacionBO(user.getConn(), idNotificacion).getNotificacion();
                NotificacionesUsuariosDaoImpl notificacionesUsuariosDaoImpl = new NotificacionesUsuariosDaoImpl(user.getConn());

                notificacionesDto.setIdEstatus(2);
           
                notificacionesUsuariosDaoImpl.update(notificacionesDto.createPk(), notificacionesDto);
                
                //out.print("<!--EXITO-->No se mostrara notif<br/>");
                
              
            }catch(Exception e){
                    //msgError += "Ocurrio un error al actualizar el registro: " + e.toString() ;
            }
        
            
        }else{
            
            if(idNotificacion>0){
                /*if (mode.equals("1")){

                    * Editar
                    */
                        
                        /*
                    }else{
                        out.print("<!--ERROR-->Acción no válida.");
                    }*/
                }else{
                    /*
                    *  Nuevo
                    */

                    try {                

                        /**
                         * Creamos el registro de actualizacion
                         */
                       int idActualizacion = 0;
                       if(path==1){
                           try{
                               ActualizacionesMovilDaoImpl actualizacionesDaoImpl = new ActualizacionesMovilDaoImpl(user.getConn());

                               ActualizacionesMovil actualizaDto =  new ActualizacionesMovil();
                               actualizaDto.setVersionCode(versionCode);
                               actualizaDto.setNumVersion(numVersion.trim());
                               actualizaDto.setFechaLiberacion(fechaLiberacion);
                               actualizaDto.setComentarios(comentarios.trim());
                               actualizaDto.setPlataforma(idPlataforma);

                               ActualizacionesMovilPk actualizaPk = actualizacionesDaoImpl.insert(actualizaDto);

                               idActualizacion = actualizaPk.getIdActualizacion();
                           }catch(Exception e){}

                            /**
                             * Creamos el registro de notificacion
                             */
                       }
                        
                        NotificacionesUsuariosDaoImpl notificacionesUsuariosDaoImpl = new NotificacionesUsuariosDaoImpl(user.getConn());

                            
                        Usuarios[] usuariosAdmin = new  Usuarios[0];
                        UsuariosBO usuariosBO = new UsuariosBO(user.getConn());
                                
                        usuariosAdmin = usuariosBO.findUsuariosByRol(-1,2);
                        //usuariosAdmin = usuariosBO.findUsuarios(1, 1, 0, 1, "");
                        
                        if(usuariosAdmin.length>0){
                            for(Usuarios item:usuariosAdmin){

                                NotificacionesUsuarios notificacionesDto = new NotificacionesUsuarios();
                                notificacionesDto.setIdUsuario(item.getIdUsuarios());
                                notificacionesDto.setTitulo(titulo.trim());
                                notificacionesDto.setMensaje(texto.trim());
                                try{
                                    notificacionesDto.setFecha(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                                }catch(Exception e){
                                    notificacionesDto.setFecha(new Date());
                                }
                                notificacionesDto.setIdEstatus(1);
                                notificacionesDto.setIdActualizacion(idActualizacion);

                                if(path==1){
                                    notificacionesDto.setImagen("evc_32x32.png");

                                }else if(path==2){
                                    notificacionesDto.setImagen("notification_green.png");
                                }else if(path==3){
                                    notificacionesDto.setImagen("notification_yellow.png");
                                }else if(path==4){
                                    notificacionesDto.setImagen("notification_red.png");
                                }else if(path==5){
                                    notificacionesDto.setImagen("blue.png");
                                }

                                /**
                                 * Realizamos el insert
                                 */
                                notificacionesUsuariosDaoImpl.insert(notificacionesDto);
                            }

                        }
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