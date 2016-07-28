<%-- 
    Document   : catCobroTarjetaToken_ajax
    Created on : 10/02/2015, 01:38:28 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.RandomString"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.BancoOperacionTokenDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.BancoOperacionToken"%>
<%@page import="com.tsp.sct.bo.BancoOperacionTokenBO"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idBancoOperacionToken = -1;
    String nombreBancoOperacionToken ="";
    String descripcion ="";  
    int estatus = 1;//deshabilitado
    double montoBancoOperacionToken = 0;
    String ligaGenerada = "http://pretorianosoft.from-la.net/";
    
    String correoBancoOperacionToken = "";
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idBancoOperacionToken = Integer.parseInt(request.getParameter("idBancoOperacionToken"));
    }catch(NumberFormatException ex){}
    nombreBancoOperacionToken = request.getParameter("nombreBancoOperacionToken")!=null?new String(request.getParameter("nombreBancoOperacionToken").getBytes("ISO-8859-1"),"UTF-8"):"";
    descripcion = request.getParameter("descripcionToken")!=null?new String(request.getParameter("descripcionToken").getBytes("ISO-8859-1"),"UTF-8"):"";    
    correoBancoOperacionToken = request.getParameter("correoBancoOperacionToken")!=null?new String(request.getParameter("correoBancoOperacionToken").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){} 
    
    try{
        montoBancoOperacionToken = Double.parseDouble(request.getParameter("montoBancoOperacionToken"));
    }catch(Exception e){}
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombreBancoOperacionToken, 1, 80))
        msgError += "<ul>El dato 'nombre' es requerido.";
    if(!gc.isValidString(descripcion, 1, 150))
        msgError += "<ul>El dato 'descripción' es requerido";   
    if(idBancoOperacionToken <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'BancoOperacionToken' es requerido";
    if(!gc.isEmail(correoBancoOperacionToken))
        msgError += "<ul>El dato 'Correo electr&oacute;nico' es incorrecto. <br/>";
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idBancoOperacionToken>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                BancoOperacionTokenBO bancoOperacionTokenBO = new BancoOperacionTokenBO(idBancoOperacionToken,user.getConn());
                BancoOperacionToken bancoOperacionTokenDto = bancoOperacionTokenBO.getBancoOperacionToken();
                
                bancoOperacionTokenDto.setIdEstatus(estatus);
                
                try{
                    new BancoOperacionTokenDaoImpl(user.getConn()).update(bancoOperacionTokenDto.createPk(), bancoOperacionTokenDto);

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
                BancoOperacionToken bancoOperacionTokenDto = new BancoOperacionToken();
                BancoOperacionTokenDaoImpl bancoOperacionTokensDaoImpl = new BancoOperacionTokenDaoImpl(user.getConn());
                
                bancoOperacionTokenDto.setIdEmpresa(idEmpresa);
                bancoOperacionTokenDto.setIdEstatus(estatus);
                bancoOperacionTokenDto.setIdUsuario(user.getUser().getIdUsuarios());                
                bancoOperacionTokenDto.setNombreTitular(nombreBancoOperacionToken);
                bancoOperacionTokenDto.setConceptoDescripcion(descripcion);
                bancoOperacionTokenDto.setMonto(montoBancoOperacionToken);
                RandomString randomString = new RandomString();
                bancoOperacionTokenDto.setTokenGenerado( ( (new Date().getTime()) + "" + randomString.randomString(5)) );
                ligaGenerada += "jsp/catCobroTarjetaToken/cobroTarjetaPersonal.jsp";
                ligaGenerada += "?nkt="+bancoOperacionTokenDto.getTokenGenerado(); //envio del atributo del token
                ligaGenerada += "&srpmd="+idEmpresa; //envio del atributo de idEmpresa
                
                bancoOperacionTokenDto.setLigaGenerada(ligaGenerada);
                bancoOperacionTokenDto.setFechaGeneracion(new Date());
                bancoOperacionTokenDto.setFechaUso(null);
                bancoOperacionTokenDto.setCorreoDestinoLiga(correoBancoOperacionToken);
                bancoOperacionTokenDto.setIdTablaBancoOperacion(0);
                
                ///inicio de envio por correo
                
                //Validamos si es clienete de EVC
                EmpresaPermisoAplicacion empresaPermisoDto = null;
                try{
                    EmpresaPermisoAplicacionDaoImpl  empresaPermisoDao = new EmpresaPermisoAplicacionDaoImpl();
                    empresaPermisoDto = empresaPermisoDao.findWhereIdEmpresaEquals(idEmpresa)[0];
                }catch(Exception e){e.printStackTrace();}
                
                try{
                    TspMailBO mail = new TspMailBO();
                    
                    if(empresaPermisoDto.getAccesoSgfensPretomovil()==1){
                        mail.setConfigurationMovilpyme();
                        mail.addMessageMovilpyme(""
                            + "<h2>Liga de Acceso para el Pago con Tarjeta</h2><br/>"
                            + "<b>Favor de Realizar su Pago en la siguiente liga: </b></h2><br/>"                           
                            + "Liga de Acceso: <b><a href='"+ligaGenerada+"'>" + ligaGenerada + "</a></b><br/>"                            
                            + "Fecha de solicitud de Pago <b> "+ ( DateManage.formatDateToNormal( (new Date())) )  +"</b><br/>"                            
                            ,1);
                    } else{
                        mail.setConfiguration();            
                        mail.addMessage(""
                            + "<h2>Liga de Acceso para el Pago con Tarjeta</h2><br/>"
                            + "<b>Favor de Realizar su Pago en la siguiente liga: </b></h2><br/>"                           
                            + "Liga de Acceso: <b><a href='"+ligaGenerada+"'>" + ligaGenerada + "</a></b><br/>"                            
                            + "Fecha de solicitud de Pago <b> "+ ( DateManage.formatDateToNormal( (new Date())) )  +"</b><br/>"                            
                            ,1);
                    }
                    
                    
                    try {
                        mail.addTo(correoBancoOperacionToken, correoBancoOperacionToken);
                    }catch(Exception e){}                    
                    mail.setFrom(mail.getUSER(), mail.getFROM_NAME());
                                        
                    mail.send("Pago con Tarjeta");
                                       
                    //out.print("<br/>Un correo de notificaci&oacute;n fue enviado al usuario.");
               }catch(Exception ex){
                   out.print("<br/>No se pudo enviar el correo de notificaci&oacute;n. "
                               + "<br/><b>Copie la informacion y notifiquela al cliente</b>"
                               +"</br>" + ex.toString()); 
                   ex.printStackTrace();
               }
               ///fin de envio por correo 

                /**
                 * Realizamos el insert
                 */
                bancoOperacionTokensDaoImpl.insert(bancoOperacionTokenDto);

                out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>La liga para realizar el pago es: <br/><b>"+ligaGenerada + "</b><br/>Esta liga se envi&oacute; al correo que fue proporcionado.");
            
            }catch(Exception e){
                e.printStackTrace();
                msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
            }
        }
    }else{
        out.print("<!--ERROR-->"+msgError);
    }

%>