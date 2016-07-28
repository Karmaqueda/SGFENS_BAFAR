<%-- 
    Document   : interLoginQPayUtilAjax
    Created on : 20/01/2016, 06:33:10 PM
    Author     : ISCesar
--%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.util.HttpConnManage"%>
<%@page import="javax.net.ssl.HttpsURLConnection"%>
<%@page import="java.net.URL"%>
<%@page import="com.tsp.sct.util.UtilSecurity"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%

    String mode = "";
    
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    
    if (mode.equals("obtenerNumSesionQPay")){
        //Parametros
        String uQPay = request.getParameter("uQPay")!=null?new String(request.getParameter("uQPay").getBytes("ISO-8859-1"),"UTF-8"):"";    
        
        try{
            Configuration appConfig = new Configuration();
            String hashU = UtilSecurity.getHashSHA256(uQPay);
            
            //Procesamiento
            String url = appConfig.getQpayWsGetNumeroSesionUrl(); //"https://sandbox.qpay.mx/qpay/sdk/ws/qpNumeroSesion";
            URL wsURL = new URL(url);
            HttpsURLConnection conn= (HttpsURLConnection) wsURL.openConnection();//abrir la conexión


            String respQPay =  HttpConnManage.postHTTPS(
                    url,   //URL
                    conn,         //HttpsURLConnection
                    "{\"pID\":\""+hashU+ "\"}",      //Contenido
                    10,           //Tiempo maximo de espera de respuesta en segundos
                    null,         //Accept Encoding Header
                    "application/x-www-form-urlencoded");        //Content Type Header

            respQPay = StringManage.getValidString(respQPay);

            if (respQPay.equals("0")){
                out.print("<!--ERROR-->Usuario QPay inválido, contacte a su proveedor" );
            }else{
                long numSesion = 0;
                try{ numSesion =  Long.parseLong(respQPay);  }catch(Exception ex){}
                if (numSesion!=0){
                    out.print("<!--EXITO-->" + numSesion);
                }else{
                    out.print("<!--ERROR-->Error retornado por QPay: " + respQPay );
                }
            }
        
        }catch(Exception ex){
            out.print("<!--ERROR-->Error al calcular información para QPAY (paso 1): " + ex.toString());
        }
        
    }else if (mode.equals("hashSesionQPay")){
        String uQPay = "";
        String pQPay = "";
        String nSesion = "";
        
        uQPay = request.getParameter("uQPay")!=null?new String(request.getParameter("uQPay").getBytes("ISO-8859-1"),"UTF-8"):"";    
        pQPay = request.getParameter("pQPay")!=null?new String(request.getParameter("pQPay").getBytes("ISO-8859-1"),"UTF-8"):"";    
        nSesion = request.getParameter("nSesion")!=null?new String(request.getParameter("nSesion").getBytes("ISO-8859-1"),"UTF-8"):"";    
        
        try{
            String hashPass = UtilSecurity.getHashSHA256(pQPay);
            String msgSesion = uQPay.toLowerCase() + hashPass + nSesion;
            String hashSesion = UtilSecurity.getHashSHA256(msgSesion);
        
            out.print("<!--EXITO-->" + hashSesion);
        }catch(Exception ex){
            out.print("<!--ERROR-->Error al calcular información para QPAY (paso 2): " + ex.toString());
        }
        
    }else{
        out.print("<!--ERROR-->No se selecciono un modo correcto.");
    }
%>