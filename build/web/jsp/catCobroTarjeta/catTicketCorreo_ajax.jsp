<%-- 
    Document   : catTicketCorreo
    Created on : 30/04/2013, 06:29:23 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.bo.TarjetaBienesAutorizadosBO"%>
<%@page import="com.tsp.ws.GpWsResponse"%>
<%@page import="com.tsp.ws.GpWsCard"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.BancoOperacionBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.BancoOperacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.BancoOperacion"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
   
    String correoDestinatario = "";    
    int idOperacionBancaria = -1;
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    
     try{
        idOperacionBancaria = Integer.parseInt(request.getParameter("idOperacionBancaria"));
    }catch(Exception e){}
    
    
    /*
    * Validaciones del servidor
    */
    
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(mode.equals("ticketCorreo")){
        correoDestinatario = request.getParameter("correoDestinoTicket")!=null?new String(request.getParameter("correoDestinoTicket").getBytes("ISO-8859-1"),"UTF-8"):"";        
        if(gc.isEmail(correoDestinatario) == false){
            //msgError += "<ul> Correo no valido. ";
        }
    }
    
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
            if(mode.equals("ticketCorreo")){
                  BancoOperacionBO bancoBO = new BancoOperacionBO(user.getConn());
                  bancoBO.enviarTicket(correoDestinatario, idOperacionBancaria, idEmpresa);
                  out.print("<!--EXITO-->Correo enviado");
            }else{
                out.print("<!--ERROR-->Acción no válida.");
            }        
    }else{
        out.print("<!--ERROR-->"+msgError);
    }

%>