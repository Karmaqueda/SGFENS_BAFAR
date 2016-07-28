<%-- 
    Document   : Mapa_ajax
    Created on : 22/04/2013, 02:00:59 PM
    Author     : Leonardo
--%>

<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
System.out.println("------------------- 1");
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    double longitudActualizada = 0;
    double latitudActualizada = 0;
    
    System.out.println("------------------- 1");
    
    /*
    * Recepción de valores
    */
    try{
        idEmpresa = Integer.parseInt(request.getParameter("idEmpresaActualizar"));
    }catch(NumberFormatException ex){}
    try{
        latitudActualizada = Double.parseDouble(request.getParameter("latitudActualizada"));
    }catch(Exception e){}
    try{                             
        longitudActualizada = Double.parseDouble(request.getParameter("longitudActualizada"));
    }catch(Exception e){}
    
    System.out.println("------------------- 2");
    /*
    * Actualizamos coordenadas
    */
        EmpresaBO empresaBO = new EmpresaBO(idEmpresa,user.getConn());
        Empresa empresaDto = empresaBO.getEmpresa();

        empresaDto.setLatitud(latitudActualizada);
        empresaDto.setLongitud(longitudActualizada);
                

        try{
            new EmpresaDaoImpl(user.getConn()).update(empresaDto.createPk(), empresaDto);

            out.print("<!--EXITO-->Cooerdenadas actualizadas satisfactoriamente");
        }catch(Exception ex){
            out.print("<!--ERROR-->No se pudieron actualizar las coordenadas. Informe del error al administrador del sistema: " + ex.toString());
            ex.printStackTrace();
        }
    

%>