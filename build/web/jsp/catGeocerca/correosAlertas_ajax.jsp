<%-- 
    Document   : correosAlertas_ajax
    Created on : 8/05/2014, 11:45:04 AM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.GeocercaNotificacionBO"%>
<%@page import="com.tsp.sct.dao.jdbc.GeocercasNotificacionesDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.GeocercasNotificaciones"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    Empresa empresaDto = new EmpresaBO(idEmpresa,user.getConn()).getEmpresa();
    
    /*
    * Parámetros
    */
    int idGeoNotificacion = -1;
    String correosGeoNotificacion ="";
    
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idGeoNotificacion = Integer.parseInt(request.getParameter("idGeoNotificacion"));
    }catch(NumberFormatException ex){}
    correosGeoNotificacion = request.getParameter("correosGeoNotificacion")!=null?new String(request.getParameter("correosGeoNotificacion").getBytes("ISO-8859-1"),"UTF-8"):"";
    try{        
        estatus = Integer.parseInt(request.getParameter("estatus"));        
    }catch(NumberFormatException ex){}   
    
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator genericValidator = new GenericValidator(); 
    String[] correos = new String[0];
    ArrayList<String> listCorreosValidos = new ArrayList<String>();
    boolean enviarCorreo = false;
    if (!correosGeoNotificacion.trim().equals("")){
            correos = correosGeoNotificacion.split(",");
            
            for (String itemCorreo: correos){
                try { itemCorreo = itemCorreo.trim(); } catch(Exception ex){}
                if (genericValidator.isEmail(itemCorreo)){
                    listCorreosValidos.add(itemCorreo);
                }
            }
            
            if (listCorreosValidos.size()<=0)
                msgError += "Ninguno de los correos proporcionados es válido";
                
            if (listCorreosValidos.size()>0)
                enviarCorreo = true;
        }   
    
    if(msgError.equals("")){
        if(idGeoNotificacion>0){
            if (mode.equals("2")){
            /*
            * Editar
            */
                GeocercaNotificacionBO geoNotiBO = new GeocercaNotificacionBO(user.getConn());
                GeocercasNotificaciones geoNotificacionDto = geoNotiBO.findGeocercasNotificacionesbyId(idGeoNotificacion);
                
                geoNotificacionDto.setIdEmpresa(idEmpresa);
                geoNotificacionDto.setIdEstatus(estatus);
                int contador = 0;
                String correosGuardar = "";
                for(String cor : listCorreosValidos ){
                    if(contador > 0){
                        correosGuardar += ",";
                    }
                    correosGuardar += cor;
                    contador++;
                }
                geoNotificacionDto.setCorreos(correosGuardar);
                
                try{
                    new GeocercasNotificacionesDaoImpl(user.getConn()).update(geoNotificacionDto.createPk(), geoNotificacionDto);
                    out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }
                //out.print("<!--ERROR-->Acción no válida.");
            }else{
                out.print("<!--ERROR-->Acción no válida.");
            }
        }else{
            /*
            *  Nuevo
            */
            
            try {                
                
                /**
                 * Creamos el registro de Certificado
                 */
                GeocercasNotificaciones geoNotificacionDto = new GeocercasNotificaciones();
                geoNotificacionDto.setIdEmpresa(idEmpresa);
                geoNotificacionDto.setIdEstatus(estatus);
                int contador = 0;
                String correosGuardar = "";
                for(String cor : listCorreosValidos ){
                    if(contador > 0){
                        correosGuardar += ",";
                    }
                    correosGuardar += cor;
                    contador++;
                }
                geoNotificacionDto.setCorreos(correosGuardar);
                
               
                /**
                 * Realizamos el insert
                 */
                if(enviarCorreo){
                    new GeocercasNotificacionesDaoImpl(user.getConn()).insert(geoNotificacionDto);
                    out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
                }
            
            }catch(Exception e){
                e.printStackTrace();
                msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
            }
        }
    }else{
        out.print("<!--ERROR-->"+msgError);
    }

%>