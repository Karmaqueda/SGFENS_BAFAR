<%-- 
    Document   : catNotificacionesHttpPostBanwire
    Created on : 24/02/2015, 04:46:16 PM
    Author     : leonardo

    Cada vez que se recibe un pago vía OXXO, el sistema enviará una notificación vía HTTP POST a 
    la URL establecida en data-notify-url (HTML) o notifyUrl (Javascript) con las siguientes variables:
    event, status, auth_code, reference, id, hash, total.
    
--%>

<%@page import="com.tsp.sct.dao.jdbc.BancoOperacionDaoImpl"%>
<%@page import="com.tsp.sct.bo.BancoOperacionBO"%>
<%@page import="com.tsp.sct.dao.dto.BancoOperacion"%>
<%@page import="com.tsp.sct.dao.jdbc.ResourceManager"%>
<%@page import="java.sql.Connection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%      
    System.out.println("-------------------------------------------");
    System.out.println("-------------------------------------------");
    System.out.println("ATRIBUTOS ENVIADOS POR HTTP POST. PROCESANDO . . .");
        
    //Atributos que se regresan por Http Post del servicio de Banwire cuando existe alguno notificación (Pago en oxxo, Pago pediente, Pago exitoso, Abono, Pago declinado, etc.)
    String event = request.getParameter("event")!=null?new String(request.getParameter("event").getBytes("ISO-8859-1"),"UTF-8"):"";
    String status = request.getParameter("status")!=null?new String(request.getParameter("status").getBytes("ISO-8859-1"),"UTF-8"):"";
    String auth_code = request.getParameter("auth_code")!=null?new String(request.getParameter("auth_code").getBytes("ISO-8859-1"),"UTF-8"):"";
    String reference = request.getParameter("reference")!=null?new String(request.getParameter("reference").getBytes("ISO-8859-1"),"UTF-8"):"";
    String id = request.getParameter("id")!=null?new String(request.getParameter("id").getBytes("ISO-8859-1"),"UTF-8"):"";
    String total = request.getParameter("total")!=null?new String(request.getParameter("total").getBytes("ISO-8859-1"),"UTF-8"):"";
    String hash = request.getParameter("hash")!=null?new String(request.getParameter("hash").getBytes("ISO-8859-1"),"UTF-8"):"";
    
    System.out.println("event: "+event);
    System.out.println("status: "+status);
    System.out.println("auth_code: "+auth_code);
    System.out.println("reference: "+reference);
    System.out.println("id: "+id);
    System.out.println("total: "+total);
    System.out.println("hash: "+hash);
    
    Connection conn = ResourceManager.getConnection();
    
    if(event.equals("oxxo") && status.equals("paid")){ //si es un pago recibido de oxxo, validamos que este para cambiarlo de estatus
        System.out.println(" Se realizó un pago en oxxo");
        BancoOperacion operacion = new BancoOperacionBO(conn).findBancoOperacionbyOrderId(reference); //LO BUSCAMOS POR REFERENCIA       
        if(operacion != null){
            operacion.setBancoAuth("Pagado Oxxo");
            operacion.setIdEstatus(4);
            new BancoOperacionDaoImpl(conn).update(operacion.createPk(), operacion);
        }
    }
    
    if(event.equals("card") && status.equals("challenge")){
        System.out.println(" Un pago se envia a revision");
        BancoOperacion operacion = new BancoOperacionBO(conn).findBancoOperacionbyOrderId(id); //LO BUSCAMOS POR ORDER ID
        if(operacion != null){            
            operacion.setIdEstatus(5);
            new BancoOperacionDaoImpl(conn).update(operacion.createPk(), operacion);
        }
    }    
    conn.close();
    System.out.println("-------------------------------------------");
    
%>