<%-- 
    Document   : catFoliosCBB_ajax
    Created on : 24/05/2013, 06:17:57 PM
    Author     : Leonardo
--%>

<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.FoliosBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.FoliosDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Folios"%>
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
    String serieCBB ="";
    int rangoInicioCBB = -1;
    int rangoFinalCBB = -1;
    int folioActualCBB = -1;
    String secofiCBB ="";
    String aprobacionFechaCBB ="";
    
    int estatus = 2;//deshabilitado
    int facturarMovil = 0;
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idFolios = Integer.parseInt(request.getParameter("idFolios"));
    }catch(NumberFormatException ex){}
    serieCBB = request.getParameter("serieCBB")!=null?new String(request.getParameter("serieCBB").getBytes("ISO-8859-1"),"UTF-8"):"";
    try{
        rangoInicioCBB = Integer.parseInt(request.getParameter("rangoInicioCBB"));
    }catch(NumberFormatException ex){}    
    try{
        rangoFinalCBB = Integer.parseInt(request.getParameter("rangoFinalCBB"));
    }catch(NumberFormatException ex){}    
    try{
        folioActualCBB = Integer.parseInt(request.getParameter("folioActualCBB"));
    }catch(NumberFormatException ex){}     
    secofiCBB = request.getParameter("secofiCBB")!=null?new String(request.getParameter("secofiCBB").getBytes("ISO-8859-1"),"UTF-8"):"";    
    aprobacionFechaCBB = request.getParameter("aprobacionFechaCBB")!=null?new String(request.getParameter("aprobacionFechaCBB").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){} 

    try{
        facturarMovil = Integer.parseInt(request.getParameter("facturarMovil"));
    }catch(NumberFormatException ex){}             
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator(); 
    if(idFolios<0){      
        if(rangoInicioCBB <= 0 )
            msgError += "<ul>El dato 'rango de inicio' es requerido.";
        if(rangoFinalCBB <= 0 )
            msgError += "<ul>El dato 'rango final' es requerido"; 
        if( folioActualCBB <= 0 )
            msgError += "<ul>El dato 'folio Actual' es requerido"; 
        if(!gc.isValidString(secofiCBB, 1, 20))
            msgError += "<ul>El dato 'Número de Secofi' es requerido"; 
        if(!gc.isValidString(aprobacionFechaCBB, 1, 15))
            msgError += "<ul>El dato 'fecha de aprobación' es requerido";

        if(idFolios <= 0 && (!mode.equals("")))
            msgError += "<ul>El dato ID 'Folios' es requerido";
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
                if(facturarMovil>0){
                    //Buscamos los folios de dicha sucursal o empresa y solo dejamos uno solo con el atributo defacturacionMovil = 1
                    Folios[] fs = new Folios[0];
                    FoliosDaoImpl foliosDaoImpl = new FoliosDaoImpl(user.getConn());
                    fs = foliosDaoImpl.findWhereIdEmpresaEquals(idEmpresa);     
                    if(fs != null){
                        for(Folios f : fs){
                            f.setFacturacionMovil(0);
                            new FoliosDaoImpl(user.getConn()).update(f.createPk(), f);
                        }
                    }                    
                    FoliosBO foliosBO = new FoliosBO(idFolios, user.getConn());
                    Folios foliosDto = foliosBO.getFolios();
                    /*foliosDto.setIdEmpresa(idEmpresa);
                    foliosDto.setFolioDesde(rangoInicioCBB);
                    foliosDto.setFolioHasta(rangoFinalCBB);
                    foliosDto.setUltimoFolio(folioActualCBB);
                    foliosDto.setIdTipoComprobante(13);
                    foliosDto.setSerie(serieCBB);                                                             
                    foliosDto.setIdEstatus(1);
                    foliosDto.setFechaGeneracion(new Date());
                    foliosDto.setFechaVigencia(aprobacionFechaCBB);
                    foliosDto.setSecofi(secofiCBB);*/
                    foliosDto.setFacturacionMovil(1);
                    
                    try{
                        new FoliosDaoImpl(user.getConn()).update(foliosDto.createPk(), foliosDto);
                        out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
                    }catch(Exception ex){
                        out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                        ex.printStackTrace();
                    }
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
                Folios foliosDto = new Folios();
                FoliosDaoImpl foliosDaoImpl = new FoliosDaoImpl(user.getConn());
                
                int idFolioNuevo;

                Folios ultimoRegistroFolios = foliosDaoImpl.findLast();
                idFolioNuevo = ultimoRegistroFolios.getIdFolio() + 1;
                
                foliosDto.setIdFolio(idFolioNuevo);
                foliosDto.setIdEmpresa(idEmpresa);
                foliosDto.setFolioDesde(rangoInicioCBB);
                foliosDto.setFolioHasta(rangoFinalCBB);
                foliosDto.setUltimoFolio(folioActualCBB);
                foliosDto.setIdTipoComprobante(13);
                foliosDto.setSerie(serieCBB);                                                             
                foliosDto.setIdEstatus(1);
                foliosDto.setFechaGeneracion(new Date());
                foliosDto.setFechaVigencia(aprobacionFechaCBB);
                foliosDto.setSecofi(secofiCBB);

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
