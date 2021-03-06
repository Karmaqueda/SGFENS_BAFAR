<%-- 
    Document   : viewPdf
    Created on : 7/09/2011, 02:21:14 PM
    Author     : ISC Cesar Martinez poseidon24@hotmail.com
--%>
<%@page import="com.tsp.sct.bo.NominaEmpleadoBO"%>
<%@page import="com.tsp.sct.dao.dto.NominaEmpleado"%>
<%@page import="com.tsp.sct.bo.TipoComprobanteBO"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.dao.dto.ComprobanteFiscal"%>
<%@page import="com.tsp.sct.bo.ComprobanteFiscalBO"%>
<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
    int idComprobanteFiscal=-1;
    String versionCfd;
    try { idComprobanteFiscal = Integer.parseInt( request.getParameter("idComprobanteFiscal")); }catch(Exception ex){}
    versionCfd = request.getParameter("versionCfd");
//    String idFactura2 = idFactura.replaceAll("\\+", "%2B");

    double versionCfdDouble = Double.parseDouble(versionCfd);
    
    String accion = request.getParameter("accion")!=null? new String(request.getParameter("accion").getBytes("ISO-8859-1"),"UTF-8") :"";

    String urlResponse = "";
    
    if (versionCfdDouble==3.2){
         urlResponse = "/ServletCfdi32Pdf"; //Para CFDI v3.2
     }
    if (versionCfdDouble==111){
         urlResponse = "/GeneradorPDFs2"; //Para CFDI v3.2
     }

    ComprobanteFiscalBO comprobanteFiscalBO = null;
    ComprobanteFiscal comprobanteFiscalDto =null;
    String urlDownloadXML ="/jsp/file/download.jsp";
    String archivoXML = null;
    String rutaArchivoEnc = null;
    String rutaArchivoAcuseXMLEnc = null;
    try{
        Configuration appConfig = new Configuration();
        
        comprobanteFiscalBO = new ComprobanteFiscalBO(idComprobanteFiscal, user.getConn());
        comprobanteFiscalDto = comprobanteFiscalBO.getComprobanteFiscal();
        
        Empresa empresaDto = new EmpresaBO(comprobanteFiscalDto.getIdEmpresa(), user.getConn()).getEmpresa();       
        
        String rfcDestinatario = "";
        if(comprobanteFiscalDto.getIdTipoComprobante()==TipoComprobanteBO.TIPO_NOMINA){
            NominaEmpleado nomEmpleadoDto = new NominaEmpleadoBO(user.getConn(), comprobanteFiscalDto.getIdCliente()).getNominaEmpleado();
            rfcDestinatario = nomEmpleadoDto.getRfc();
        }else{
            Cliente clienteDto = new ClienteBO(comprobanteFiscalDto.getIdCliente(),user.getConn()).getCliente();
            rfcDestinatario = clienteDto.getRfcCliente();
        }
        
        archivoXML = comprobanteFiscalBO.getComprobanteFiscal().getArchivoCfd();
        
        rutaArchivoEnc = appConfig.getApp_content_path() + empresaDto.getRfc() + "/cfd/emitidos/"
                +TipoComprobanteBO.getTipoComprobanteNombreCarpeta(comprobanteFiscalDto.getIdTipoComprobante()) 
                +"/" + rfcDestinatario
                + "/" + archivoXML;
        
        rutaArchivoEnc = java.net.URLEncoder.encode(rutaArchivoEnc, "UTF-8");
        
        if (comprobanteFiscalDto.getAcuseCancelacion()!=null){
            if (comprobanteFiscalDto.getAcuseCancelacion().trim().length()>0){
                rutaArchivoAcuseXMLEnc = appConfig.getApp_content_path() + empresaDto.getRfc() + "/cfd/emitidos/" 
                    + comprobanteFiscalDto.getAcuseCancelacion();
                
                File archivoAcuse = new File(rutaArchivoAcuseXMLEnc);
                if (!archivoAcuse.exists()){
                    rutaArchivoAcuseXMLEnc = null;
                }else{
                    rutaArchivoAcuseXMLEnc = java.net.URLEncoder.encode(rutaArchivoAcuseXMLEnc, "UTF-8");
                }
                
            }
        }
        
    }catch(Exception e){
        e.printStackTrace();
    }
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Vista Previa de Comprobante Fiscal Digital</title>        
        
    </head>
    <body scroll="no" style="background-color: white ; ">
            <center>
                
                    
                <% if (comprobanteFiscalDto!=null && archivoXML!=null && rutaArchivoEnc!=null) {%>
                <a href="javascript:;">
                    <img src="../../images/icon_email.png" alt="Enviar Correo">
                </a>
                <%}%>
                
                <br/>
                <object id='pdfObject' type='text/html;charset=UTF-8' data='<%=request.getContextPath()+urlResponse+"?idComprobanteFiscal="+idComprobanteFiscal+"&accion=correo" %>' width='100%' height='300px' >
                No cuenta con la instalación de Adobe Reader
            </object>
            </center>
    </body>
</html>
