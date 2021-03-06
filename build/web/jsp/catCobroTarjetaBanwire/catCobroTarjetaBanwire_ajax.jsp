<%-- 
    Document   : catCobroTarjetaBanwire_ajax
    Created on : 19/02/2015, 03:41:50 PM
    Author     : leonardo
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
    int idBancoOperacion = -1;
    String numTarjetaOperacion ="";
    String nombreTitularOperacion ="";  
    //String fechaVencimientoOperacion = "";
    int idMesOperacion = 01;
    int idAnoOperacion = 25;
    String cvvOperacion = "000";
    String orderIdOperacion = "";
    double montoPagadoOperacion = 0 ;
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idBancoOperacion = Integer.parseInt(request.getParameter("idBancoOperacion"));
    }catch(NumberFormatException ex){}
    try{
        idMesOperacion = Integer.parseInt(request.getParameter("idMesOperacion"));        
    }catch(NumberFormatException ex){}
    try{
        idAnoOperacion = Integer.parseInt(request.getParameter("idAnoOperacion"));        
    }catch(NumberFormatException ex){}
    
    numTarjetaOperacion = request.getParameter("numTarjetaOperacion")!=null?new String(request.getParameter("numTarjetaOperacion").getBytes("ISO-8859-1"),"UTF-8"):"";
    nombreTitularOperacion = request.getParameter("nombreTitularOperacion")!=null?new String(request.getParameter("nombreTitularOperacion").getBytes("ISO-8859-1"),"UTF-8"):"";    
    //fechaVencimientoOperacion = request.getParameter("fechaVencimientoOperacion")!=null?new String(request.getParameter("fechaVencimientoOperacion").getBytes("ISO-8859-1"),"UTF-8"):"";    
    cvvOperacion = request.getParameter("cvvOperacion")!=null?new String(request.getParameter("cvvOperacion").getBytes("ISO-8859-1"),"UTF-8"):"000";
    orderIdOperacion = request.getParameter("orderIdOperacion")!=null?new String(request.getParameter("orderIdOperacion").getBytes("ISO-8859-1"),"UTF-8"):"";
    try{
        montoPagadoOperacion = Double.parseDouble(request.getParameter("montoPagadoOperacion"));
    }catch(Exception ex){}   
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    String conektaTokenId = "";
    conektaTokenId = request.getParameter("conektaTokenId")!=null?new String(request.getParameter("conektaTokenId").getBytes("ISO-8859-1"),"UTF-8"):"";
    
    String correoTarjetaOperacion = request.getParameter("correoTarjetaOperacion")!=null?new String(request.getParameter("correoTarjetaOperacion").getBytes("ISO-8859-1"),"UTF-8"):"";
    String telefonoTarjetaOperacion = request.getParameter("telefonoTarjetaOperacion")!=null?new String(request.getParameter("telefonoTarjetaOperacion").getBytes("ISO-8859-1"),"UTF-8"):"";
    
    String idBanwire = request.getParameter("idBanwire")!=null?new String(request.getParameter("idBanwire").getBytes("ISO-8859-1"),"UTF-8"):"";
    String authCodeBanwire = request.getParameter("authCodeBanwire")!=null?new String(request.getParameter("authCodeBanwire").getBytes("ISO-8859-1"),"UTF-8"):"";
    String referenceBanwire = request.getParameter("referenceBanwire")!=null?new String(request.getParameter("referenceBanwire").getBytes("ISO-8859-1"),"UTF-8"):"";
    String totalBanwire = request.getParameter("totalBanwire")!=null?new String(request.getParameter("totalBanwire").getBytes("ISO-8859-1"),"UTF-8"):"";
    String eventBanwire = request.getParameter("eventBanwire")!=null?new String(request.getParameter("eventBanwire").getBytes("ISO-8859-1"),"UTF-8"):"";
   
    /*
    * Validaciones del servidor
    */
    
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(numTarjetaOperacion, 1, 16))
        msgError += "<ul>El dato 'Número de Tarjeta' es requerido.";
    /*if(idMesOperacion <= 0)
        msgError += "<ul>El dato 'fecha de Vencimiento, Mes' es requerido";*/
    /*if(idAnoOperacion <= 0)
        msgError += "<ul>El dato 'fecha de Vencimiento, Año' es requerido";*/       
    /*if(!gc.isValidString(cvvOperacion, 1, 3))
        msgError += "<ul>El dato 'CVV' es requerido";*/
    if(montoPagadoOperacion <= 0)
        msgError += "<ul>El dato 'Monto' debe se mayor a 0";
    if(idBancoOperacion <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'BancoOperacion' es requerido";    
    //VALIDACION DE TARJETA (PREFIJO)
    /*if(!gc.isCreditCardNumber(numTarjetaOperacion))
        msgError += "<ul> Número de Tarjeta Invalida. Por favor Verifique.";*/
    //TarjetaBienesAutorizadosBO tbaBO = new TarjetaBienesAutorizadosBO(user.getConn());
    /*try{
    if(tbaBO.validacion(numTarjetaOperacion.substring(0, 6)).equals("invalida"))
        msgError += "<ul> Número de Tarjeta Invalida. Por favor Verifique.";
    }catch(Exception e){
        msgError += "<ul> Número de Tarjeta Invalida. Por favor Verifique.";
    }*/    
    
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idBancoOperacion>0){
            if (mode.equals("1")){
                System.out.println("CANCELAR PAGO!!!!!!!!");                
            /*
            * Editar
            */               
            
            }else{
                out.print("<!--ERROR-->Acción no válida.");
            }
        }else{
            /*
            *  Nuevo
            */
            System.out.println("NUEVO PAGO!!!!!!!!");           
            try {
                
                /**
                 * Creamos el registro de Cliente
                 */                
                BancoOperacionBO bancoOperacionsBO = new BancoOperacionBO(user.getConn());
                GpWsCard tarjeta = new GpWsCard();
                tarjeta.setNumber(numTarjetaOperacion);
                if(idMesOperacion < 10){
                    tarjeta.setExpires("0"+idMesOperacion+"/"+idAnoOperacion);                    
                }else{
                    tarjeta.setExpires(idMesOperacion+"/"+idAnoOperacion);                    
                }                
                tarjeta.setCvv2Val(cvvOperacion);
                tarjeta.setOrderId(idBanwire);
                                        
                tarjeta.setTotal(Double.toString(montoPagadoOperacion)); 
                                
                tarjeta.setOriginalTransType("lnrd"+"|"+authCodeBanwire+"|"+idBanwire);//identificador de que el pago fue procesado ya por Banwire y enviamos el número de autorización
                
                GpWsResponse resultado = bancoOperacionsBO.pagoLinea(user.getUser().getIdUsuarios(), tarjeta, idEmpresa, nombreTitularOperacion);
                                
                out.print("<!--EXITO-->"+resultado.getText()+".<br/>");
            
            }catch(Exception e){
                e.printStackTrace();
                msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
            }
        }
    }else{
        out.print("<!--ERROR-->"+msgError);
    }

%>