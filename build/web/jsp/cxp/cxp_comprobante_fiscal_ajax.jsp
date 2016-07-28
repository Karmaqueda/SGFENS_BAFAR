<%-- 
    Document   : cxp_comprobante_fiscal_ajax
    Created on : 16/04/2015, 16/04/2015 10:04:10 AM
    Author     : ISCesar
--%>
<%@page import="com.tsp.sct.dao.jdbc.CxpComprobanteFiscalDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CxpComprobanteFiscal"%>
<%@page import="com.tsp.sct.bo.CxpComprobanteFiscalBO"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idCxPComprobanteFiscal = -1;
    int idValidacion = -1;
    Date fechaPago = null;
    String serie ="";
    String folio ="";
    String emisorRfc ="";
    String emisorNombre ="";
    double total = -1;  
    Date fechaHrSello = null;
    String selloEmisor ="";
    String uuid ="";
    int estatus = 1;//habilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idCxPComprobanteFiscal = Integer.parseInt(request.getParameter("id_cxp_comprobante_fiscal"));
    }catch(NumberFormatException ex){}
    try{
        idValidacion = Integer.parseInt(request.getParameter("id_validacion"));
    }catch(NumberFormatException ex){}
    try{ fechaPago = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fecha_pago"));}catch(Exception e){}
    serie = request.getParameter("serie")!=null?new String(request.getParameter("serie").getBytes("ISO-8859-1"),"UTF-8"):"";
    folio = request.getParameter("folio")!=null?new String(request.getParameter("folio").getBytes("ISO-8859-1"),"UTF-8"):"";
    emisorRfc = request.getParameter("emisor_rfc")!=null?new String(request.getParameter("emisor_rfc").getBytes("ISO-8859-1"),"UTF-8"):"";
    emisorNombre = request.getParameter("emisor_nombre")!=null?new String(request.getParameter("emisor_nombre").getBytes("ISO-8859-1"),"UTF-8"):"";
    try{
        total = Double.parseDouble(request.getParameter("total"));
    }catch(NumberFormatException ex){}
    try{ fechaHrSello = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(request.getParameter("fecha_hr_sello"));}catch(Exception e){}
    selloEmisor = request.getParameter("sello_emisor")!=null?new String(request.getParameter("sello_emisor").getBytes("ISO-8859-1"),"UTF-8"):"";
    uuid = request.getParameter("uuid")!=null?new String(request.getParameter("uuid").getBytes("ISO-8859-1"),"UTF-8"):"";
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    //GenericValidator gc = new GenericValidator();    
    if (idValidacion<=0)
        msgError+="<ul> No se recibio el ID de Validación previa del Comprobante Fiscal. Es requerido. ";
    if (fechaPago==null)
        msgError+="<ul> Debe seleccionar una fecha tentativa de pago. ";
    if (fechaHrSello==null)
        msgError+="<ul> El comprobante no tiene una fecha de emisión (sello emisor) válida. ";
    if(idCxPComprobanteFiscal <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'CxP Comprobante Fiscal' es requerido en ediciones.";

    if(msgError.equals("")){
        if(idCxPComprobanteFiscal>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                CxpComprobanteFiscalBO cxpComprobanteFiscalBO = new CxpComprobanteFiscalBO(idCxPComprobanteFiscal,user.getConn());
                CxpComprobanteFiscal cxpComprobanteFiscalDto = cxpComprobanteFiscalBO.getCxpComprobanteFiscal();
                
                cxpComprobanteFiscalDto.setFechaTentativaPago(fechaPago);
                cxpComprobanteFiscalDto.setIdEstatus(estatus);
                
                try{
                    new CxpComprobanteFiscalDaoImpl(user.getConn()).update(cxpComprobanteFiscalDto.createPk(), cxpComprobanteFiscalDto);

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
                CxpComprobanteFiscal cxpComprobanteFiscalDto = new CxpComprobanteFiscal();
                CxpComprobanteFiscalDaoImpl cxpComprobanteFiscalDao = new CxpComprobanteFiscalDaoImpl(user.getConn());
                
                if (msgError.equals("")){
                    cxpComprobanteFiscalDto.setIdEmpresa(idEmpresa);
                    cxpComprobanteFiscalDto.setIdValidacion(idValidacion);
                    cxpComprobanteFiscalDto.setEmisorRfc(emisorRfc);
                    cxpComprobanteFiscalDto.setEmisorNombre(emisorNombre);
                    cxpComprobanteFiscalDto.setSerie(serie);
                    cxpComprobanteFiscalDto.setFolio(folio);
                    cxpComprobanteFiscalDto.setTotal(total);
                    cxpComprobanteFiscalDto.setImportePagado(0);
                    cxpComprobanteFiscalDto.setFechaHoraCaptura(new Date());
                    cxpComprobanteFiscalDto.setFechaHoraSello(fechaHrSello);
                    cxpComprobanteFiscalDto.setFechaTentativaPago(fechaPago);
                    cxpComprobanteFiscalDto.setIdEstatus(estatus);
                    cxpComprobanteFiscalDto.setSelloEmisor(selloEmisor);
                    cxpComprobanteFiscalDto.setCfdiUuid(uuid);

                    /**
                     * Realizamos el insert
                     */
                    cxpComprobanteFiscalDao.insert(cxpComprobanteFiscalDto);

                    out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
                }
            
            }catch(Exception e){
                e.printStackTrace();
                msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
            }
        }
    }
    
    if (!msgError.equals("")){
        out.print("<!--ERROR-->"+msgError);
    }

%>