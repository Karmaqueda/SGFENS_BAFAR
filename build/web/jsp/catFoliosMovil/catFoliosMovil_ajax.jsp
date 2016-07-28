<%-- 
    Document   : catFoliosMovil_ajax
    Created on : 21/12/2015, 04:13:14 PM
    Author     : ISC Cesar Martinez
--%>

<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.FoliosMovilEmpleadoBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.FoliosMovilEmpleadoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.FoliosMovilEmpleado"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idFolios = -1;
    String serieCInterno ="";
    int rangoInicioCInterno = -1;
    int rangoFinalCInterno = 9999999;
    int folioActualCInterno = 0;
    int tipoFolios = 1; // 1= Pedidos
    int estatus = 1;//1=activo, 2=deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idFolios = Integer.parseInt(request.getParameter("idFolios"));
    }catch(Exception ex){}
    serieCInterno = request.getParameter("serieCInterno")!=null?new String(request.getParameter("serieCInterno").getBytes("ISO-8859-1"),"UTF-8"):"";
    try{
        rangoInicioCInterno = Integer.parseInt(request.getParameter("rangoInicioCInterno"));
    }catch(Exception ex){}    
    try{
        rangoFinalCInterno = Integer.parseInt(request.getParameter("rangoFinalCInterno"));
    }catch(Exception ex){}    
    try{
        folioActualCInterno = Integer.parseInt(request.getParameter("folioActualCInterno"));
    }catch(Exception ex){}  
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(Exception ex){} 

    try{
        tipoFolios = Integer.parseInt(request.getParameter("tipo_folios"));
    }catch(Exception ex){}
    
    folioActualCInterno = rangoInicioCInterno - 1;
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator(); 
    if(idFolios<0){      
        if(rangoInicioCInterno <= 0 )
            msgError += "<ul>El dato 'rango de inicio' es requerido.";
        if(rangoFinalCInterno <= 0 )
            msgError += "<ul>El dato 'rango final' es requerido"; 
        if (rangoInicioCInterno >= rangoFinalCInterno)
            msgError += "<ul>El Rango de Inicio no puede ser mayor o igual al Rango Final";
        if( folioActualCInterno < 0 )
            msgError += "<ul>El dato 'folio Actual' es requerido";       
        if(idFolios <= 0 && (!mode.equals("")))
            msgError += "<ul>El dato ID 'Folio' es requerido para una edición";
    }
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idFolios>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                FoliosMovilEmpleadoDaoImpl foliosDaoImpl = new FoliosMovilEmpleadoDaoImpl(user.getConn());
                
                FoliosMovilEmpleadoBO foliosBO = new FoliosMovilEmpleadoBO(idFolios, user.getConn());
                FoliosMovilEmpleado foliosDto = foliosBO.getFoliosMovilEmpleado();

                foliosDto.setIdEmpresa(idEmpresa);
                foliosDto.setFolioDesde(rangoInicioCInterno);
                foliosDto.setFolioHasta(rangoFinalCInterno);
                //foliosDto.setUltimoFolio(folioActualCInterno);
                foliosDto.setSerie(serieCInterno);                                                             
                foliosDto.setIdEstatus(estatus);
                foliosDto.setTipoFolioMovil(tipoFolios);

                try{
                    new FoliosMovilEmpleadoDaoImpl(user.getConn()).update(foliosDto.createPk(), foliosDto);
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
                FoliosMovilEmpleado foliosDto = new FoliosMovilEmpleado();
                FoliosMovilEmpleadoDaoImpl foliosDaoImpl = new FoliosMovilEmpleadoDaoImpl(user.getConn());
                
                foliosDto.setIdEmpresa(idEmpresa);
                foliosDto.setFolioDesde(rangoInicioCInterno);
                foliosDto.setFolioHasta(rangoFinalCInterno);
                foliosDto.setUltimoFolio(folioActualCInterno);
                foliosDto.setSerie(serieCInterno);                                                             
                foliosDto.setIdEstatus(estatus);
                foliosDto.setFechaGeneracion(new Date());
                foliosDto.setTipoFolioMovil(tipoFolios);

                /**
                 * Realizamos el insert
                 */
                foliosDaoImpl.insert(foliosDto);

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
