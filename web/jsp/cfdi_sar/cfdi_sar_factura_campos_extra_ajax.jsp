<%-- 
    Document   : cfdi_sar_factura_campos_extra_ajax
    Created on : 18/03/2015, 01:55:09 PM
    Author     : ISCesar
--%>
<%@page import="com.tsp.workflow.ws.EditaComprobanteRequest"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.SarComprobanteAdjuntoBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.util.FileManage"%>
<%@page import="java.io.File"%>
<%@page import="com.tsp.workflow.ws.WsItemArchivoRequest"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.bo.SarAreaEntregaBO"%>
<%@page import="com.tsp.sct.dao.dto.SarAreaEntrega"%>
<%@page import="com.tsp.workflow.ws.RegistraComprobanteRequest"%>
<%@page import="com.tsp.workflow.ws.AccesoRequest"%>
<%@page import="com.tsp.workflow.ws.RegistraComprobanteResponse"%>
<%@page import="com.tsp.sct.bo.ConexionSARBO"%>
<%@page import="com.tsp.sct.dao.jdbc.SarComprobanteDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SarUsuarioProveedor"%>
<%@page import="com.tsp.sct.bo.SarUsuarioProveedorBO"%>
<%@page import="com.tsp.sct.dao.dto.SarComprobante"%>
<%@page import="com.tsp.sct.dao.dto.ComprobanteFiscal"%>
<%@page import="com.tsp.sct.bo.SarComprobanteBO"%>
<%@page import="com.tsp.sct.bo.ComprobanteFiscalBO"%>
<%@page import="com.tsp.sct.dao.jdbc.SarComprobanteAdjuntoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SarComprobanteAdjunto"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page import="com.google.gson.Gson"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%

    String msgError="";
    String msgAdvertencia = "";
    /**
     * Modos:
     *  1: Guardar y Enviar
     *  2: Agregar Adjuntos / Modificar OC
     */
    int mode=-1;
    try{
        mode = Integer.parseInt(request.getParameter("mode"));
    }catch(NumberFormatException e){}
    
    Gson gson = new Gson();
    
    GenericValidator genericValidator = new GenericValidator();
    
    Configuration appConfig = new Configuration();
    
    int idEmpresa = user.getUser().getIdEmpresa();
    Empresa empresaDto = new EmpresaBO(idEmpresa, user.getConn()).getEmpresa();
    
    /*
     * Procesamiento
     */
    if (mode==-1){
        msgError = "No se eligio una acción correcta. Reintente el llenado de los datos.";
    }else if (mode==1){
        //1 = Guardar y Enviar
        
        //Parametros
        int idSarUsuario = 0;
        int idComprobanteFiscal = 0;
        String[] archivosAdjuntos = new String[0];
        String sarOrdenCompra = "";
        
        try{ idComprobanteFiscal = Integer.parseInt(request.getParameter("id_comprobante_fiscal")); }catch(Exception e){}
        try{ idSarUsuario = Integer.parseInt(request.getParameter("id_sar_usuario")); }catch(Exception e){}
        archivosAdjuntos = request.getParameterValues("archivo_adjunto");
        sarOrdenCompra = request.getParameter("sar_orden_compra")!=null?new String(request.getParameter("sar_orden_compra").getBytes("ISO-8859-1"),"UTF-8"):"";
        
        if (idComprobanteFiscal<=0)
            msgError += "<ul>ID de Comprobante Fiscal no recibido, verifique que el comprobante pertenece al módulo de entrega SAR.";
        if (idSarUsuario<=0)
            msgError += "<ul>ID de Usuario SAR no recibido, verifique que el comprobante pertenece al módulo de entrega SAR.";
        
        if (msgError.equals("")){
            ComprobanteFiscalBO comprobanteFiscalBO = new ComprobanteFiscalBO(idComprobanteFiscal, user.getConn());
            SarComprobanteBO sarComprobanteBO = new SarComprobanteBO(idComprobanteFiscal, user.getConn());
            //ComprobanteFiscal comprobanteFiscalDto = comprobanteFiscalBO.getComprobanteFiscal();
            SarComprobante sarComprobanteDto = sarComprobanteBO.getSarComprobante();
            SarUsuarioProveedorBO sarUsuarioProveedorBO = new SarUsuarioProveedorBO(sarComprobanteDto.getIdSarUsuario(), user.getConn());
            SarUsuarioProveedor sarUsuarioProveedorDto = sarUsuarioProveedorBO.getSarUsuarioProveedor();
            SarAreaEntrega sarAreaEntrega = new SarAreaEntregaBO(sarComprobanteDto.getIdSarAreaEntrega(), user.getConn()).getSarAreaEntrega();
            SarComprobanteAdjuntoBO sarComprobanteAdjuntoBO = new SarComprobanteAdjuntoBO(user.getConn());
            
            if (sarUsuarioProveedorBO.getSarCamposAdicionales().getExtReqOrdenCompra()==1){
                if (!genericValidator.isValidString(sarOrdenCompra, 1, 100)){
                    msgError += "<ul>Dato 'Orden de Compra' es requerido por el Cliente receptor en el sistema SAR. Máximo 100 caracteres.";
                }
            }
            
            if (msgError.equals("")){
                try{
                    sarComprobanteDto.setOrdenCompra(StringManage.getValidString(sarOrdenCompra));

                    File archivoCfdiXml = comprobanteFiscalBO.getComprobanteFiscalXML();
                    
                    //Preparamos Request para WS SAR
                    AccesoRequest accesoRequest = new AccesoRequest();
                    accesoRequest.setUsuario(sarUsuarioProveedorDto.getExtSarUsuario());
                    accesoRequest.setContrasena(sarUsuarioProveedorDto.getExtSarPass());

                    RegistraComprobanteRequest registraComprobanteRequest = new RegistraComprobanteRequest();
                    registraComprobanteRequest.setIdArea(sarAreaEntrega.getExtSarIdArea());
                    registraComprobanteRequest.setOrdenCompra(sarComprobanteDto.getOrdenCompra());
                    registraComprobanteRequest.setComentarios("Entregado desde Pretoriano Soft.");
                    
                    WsItemArchivoRequest archivoRequest = new WsItemArchivoRequest();
                    archivoRequest.setNombreArchivo(archivoCfdiXml.getName());
                    archivoRequest.setContenidoArchivo(FileManage.getBytesFromFile(archivoCfdiXml));
                    registraComprobanteRequest.setArchivoComprobante(archivoRequest);
                    
                    if (archivosAdjuntos!=null){
                        for(int i=0;i<archivosAdjuntos.length;i++){
                            try{
                                WsItemArchivoRequest wsAdjunto = new WsItemArchivoRequest();
                                wsAdjunto.setNombreArchivo(archivosAdjuntos[i]);
                                wsAdjunto.setContenidoArchivo(FileManage.getBytesFromFile( sarComprobanteAdjuntoBO.getArchivoDeRepositorioByEmpresa(archivosAdjuntos[i], idEmpresa) ));
                                registraComprobanteRequest.getListaAdjuntos().add(wsAdjunto);
                            }catch(Exception ex){
                                msgAdvertencia += "<ul>El archivo '" + archivosAdjuntos[i] + "' no fue adjuntado, verifique que tenga un nombre corto y valido, sin acentos ni caracteres especiales.";
                            }
                        }
                    }
                    
                    //Llamada a Web Service entrega SAR
                    RegistraComprobanteResponse wsResponse = ConexionSARBO.setComprobante(accesoRequest, registraComprobanteRequest);
                    
                    if (wsResponse.isError()){
                        msgError += "Error en entrega a SAR: " + wsResponse.getNumError() + " - " + wsResponse.getMsgError();
                    }if (wsResponse.getIdEstatus()==1) {
                        //Factura inválida
                        msgError += "El sistema SAR ha marcado como inválido el comprobante, revise la bitacora: " + wsResponse.getUltimoComentarioBitacora();
                        sarComprobanteDto.setExtSarIdFactura((int)wsResponse.getIdFactura());
                        sarComprobanteDto.setExtSarIdEstatus((int)wsResponse.getIdEstatus());
                        sarComprobanteDto.setExtSarDescEstatus(wsResponse.getDescripcionEstatus());
                        sarComprobanteDto.setExtSarUltimoComentario(wsResponse.getUltimoComentarioBitacora());
                        //Actualizamos registro DTO SarComprobante
                        new SarComprobanteDaoImpl(user.getConn()).update(sarComprobanteDto.createPk(), sarComprobanteDto);
                    } else{
                        //Completamos Datos
                        sarComprobanteDto.setExtSarIdFactura((int)wsResponse.getIdFactura());
                        sarComprobanteDto.setExtSarIdEstatus((int)wsResponse.getIdEstatus());
                        sarComprobanteDto.setExtSarDescEstatus(wsResponse.getDescripcionEstatus());
                        sarComprobanteDto.setExtSarUltimoComentario(wsResponse.getUltimoComentarioBitacora());
                        
                        //Escritura de Acuse
                        try{
                            String nombreArchivoAcuse = "acuseSAR_" + DateManage.getDateHourString() + ".pdf";
                            String ourRepositoryDirectory =  appConfig.getApp_content_path() + empresaDto.getRfc() + "/ArchivosSAR/";
                            FileManage.createFileFromByteArray(wsResponse.getArchivoAcuseRecepcion(), ourRepositoryDirectory, nombreArchivoAcuse);
                            
                            sarComprobanteDto.setNombreArchivoAcuse(nombreArchivoAcuse);
                        }catch(Exception ex){
                            ex.printStackTrace();
                            msgAdvertencia += "<ul> No se pudo recuperar el Acuse de sistema SAR: " + ex.toString();
                        }
                        
                        //Actualizamos registro DTO SarComprobante
                        new SarComprobanteDaoImpl(user.getConn()).update(sarComprobanteDto.createPk(), sarComprobanteDto);

                        //Se añaden los archivos adjuntos a BD
                        if(archivosAdjuntos!=null){
                            SarComprobanteAdjunto sarAdjunto;
                            for(int i=0;i<archivosAdjuntos.length;i++){
                                sarAdjunto = new SarComprobanteAdjunto();
                                sarAdjunto.setIdComprobanteFiscal(idComprobanteFiscal);
                                sarAdjunto.setNombreArchivo(archivosAdjuntos[i]);

                                try{
                                    new SarComprobanteAdjuntoDaoImpl().insert(sarAdjunto);
                                }catch(Exception ex){
                                    ex.printStackTrace();
                                    msgAdvertencia += "<ul>Un Archivo adjunto no ha sido almacenado en base de datos exitosamente: " + ex.toString();
                                }
                            }
                        }

                        //Proceso exitoso
                        out.print("<!--EXITO-->Comprobante entregado a sistema SAR, revise el estatus SAR en el listado. ");
                        if (msgAdvertencia.length()>0)
                            out.print("<br/><br/>Hay algunas <b>advertencias</b> en el proceso: " + msgAdvertencia);
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                    msgError+= "Ha ocurrido un error inesperado al procesar el envío y almacenamiento del comprobante SAR." + ex.toString();
                }
            }

        }
        
    }else if (mode==2){
        //2 = Agregar Adjuntos / Modificar OC
        
        //Parametros
        int idSarUsuario = 0;
        int idComprobanteFiscal = 0;
        String[] archivosAdjuntos = new String[0];
        String sarOrdenCompra = "";
        
        try{ idComprobanteFiscal = Integer.parseInt(request.getParameter("id_comprobante_fiscal")); }catch(Exception e){}
        try{ idSarUsuario = Integer.parseInt(request.getParameter("id_sar_usuario")); }catch(Exception e){}
        archivosAdjuntos = request.getParameterValues("archivo_adjunto");
        sarOrdenCompra = request.getParameter("sar_orden_compra")!=null?new String(request.getParameter("sar_orden_compra").getBytes("ISO-8859-1"),"UTF-8"):"";
        
        if (idComprobanteFiscal<=0)
            msgError += "<ul>ID de Comprobante Fiscal no recibido, verifique que el comprobante pertenece al módulo de entrega SAR.";
        if (idSarUsuario<=0)
            msgError += "<ul>ID de Usuario SAR no recibido, verifique que el comprobante pertenece al módulo de entrega SAR.";
        
        if (msgError.equals("")){
            SarComprobanteBO sarComprobanteBO = new SarComprobanteBO(idComprobanteFiscal, user.getConn());
            SarComprobante sarComprobanteDto = sarComprobanteBO.getSarComprobante();
            SarUsuarioProveedorBO sarUsuarioProveedorBO = new SarUsuarioProveedorBO(sarComprobanteDto.getIdSarUsuario(), user.getConn());
            SarUsuarioProveedor sarUsuarioProveedorDto = sarUsuarioProveedorBO.getSarUsuarioProveedor();
            SarComprobanteAdjuntoBO sarComprobanteAdjuntoBO = new SarComprobanteAdjuntoBO(user.getConn());
            
            if (sarUsuarioProveedorBO.getSarCamposAdicionales().getExtReqOrdenCompra()==1){
                if (!genericValidator.isValidString(sarOrdenCompra, 1, 100)){
                    msgError += "<ul>Dato 'Orden de Compra' es requerido por el Cliente receptor en el sistema SAR. Máximo 100 caracteres.";
                }
            }
            
            if (msgError.equals("")){
                try{
                    boolean ocCambio = false;
                    if (!StringManage.getValidString(sarOrdenCompra).equals(sarComprobanteDto.getOrdenCompra())){
                        ocCambio = true;
                        sarComprobanteDto.setOrdenCompra(StringManage.getValidString(sarOrdenCompra));
                    }
                    
                    //Preparamos Request para WS SAR
                    AccesoRequest accesoRequest = new AccesoRequest();
                    accesoRequest.setUsuario(sarUsuarioProveedorDto.getExtSarUsuario());
                    accesoRequest.setContrasena(sarUsuarioProveedorDto.getExtSarPass());

                    EditaComprobanteRequest editaComprobanteRequest = new EditaComprobanteRequest();
                    editaComprobanteRequest.setIdFactura(sarComprobanteDto.getExtSarIdFactura());
                    editaComprobanteRequest.setComentarios("");
                    editaComprobanteRequest.setCancelarComprobante(false);
                    if (ocCambio)
                        editaComprobanteRequest.setOrdenCompra(sarComprobanteDto.getOrdenCompra());
                    
                    if (archivosAdjuntos!=null){
                        for(int i=0;i<archivosAdjuntos.length;i++){
                            try{
                                WsItemArchivoRequest wsAdjunto = new WsItemArchivoRequest();
                                wsAdjunto.setNombreArchivo(archivosAdjuntos[i]);
                                wsAdjunto.setContenidoArchivo(FileManage.getBytesFromFile( sarComprobanteAdjuntoBO.getArchivoDeRepositorioByEmpresa(archivosAdjuntos[i], idEmpresa) ));
                                editaComprobanteRequest.getListaAdjuntos().add(wsAdjunto);
                            }catch(Exception ex){
                                msgAdvertencia += "<ul>El archivo '" + archivosAdjuntos[i] + "' no fue adjuntado, verifique que tenga un nombre corto y valido, sin acentos ni caracteres especiales.";
                            }
                        }
                    }
                    
                    //Llamada a Web Service entrega SAR
                    RegistraComprobanteResponse wsResponse = ConexionSARBO.editComprobante(accesoRequest, editaComprobanteRequest);
                    
                    if (wsResponse.isError()){
                        msgError += "Error al intentar actualizar en SAR: " + wsResponse.getNumError() + " - " + wsResponse.getMsgError();
                    }if (wsResponse.getIdEstatus()==1) {
                        //Factura inválida
                        msgError += "El sistema SAR ha marcado como inválido el comprobante, revise la bitacora: " + wsResponse.getUltimoComentarioBitacora();
                        sarComprobanteDto.setExtSarIdFactura((int)wsResponse.getIdFactura());
                        sarComprobanteDto.setExtSarIdEstatus((int)wsResponse.getIdEstatus());
                        sarComprobanteDto.setExtSarDescEstatus(wsResponse.getDescripcionEstatus());
                        sarComprobanteDto.setExtSarUltimoComentario(wsResponse.getUltimoComentarioBitacora());
                        //Actualizamos registro DTO SarComprobante
                        new SarComprobanteDaoImpl(user.getConn()).update(sarComprobanteDto.createPk(), sarComprobanteDto);
                    } else{
                        //Completamos Datos
                        sarComprobanteDto.setExtSarIdEstatus((int)wsResponse.getIdEstatus());
                        sarComprobanteDto.setExtSarDescEstatus(wsResponse.getDescripcionEstatus());
                        sarComprobanteDto.setExtSarUltimoComentario(wsResponse.getUltimoComentarioBitacora());
                        
                        //Actualizamos registro DTO SarComprobante
                        new SarComprobanteDaoImpl(user.getConn()).update(sarComprobanteDto.createPk(), sarComprobanteDto);

                        //Se añaden los archivos adjuntos a BD
                        if(archivosAdjuntos!=null){
                            SarComprobanteAdjunto sarAdjunto;
                            for(int i=0;i<archivosAdjuntos.length;i++){
                                sarAdjunto = new SarComprobanteAdjunto();
                                sarAdjunto.setIdComprobanteFiscal(idComprobanteFiscal);
                                sarAdjunto.setNombreArchivo(archivosAdjuntos[i]);

                                try{
                                    new SarComprobanteAdjuntoDaoImpl().insert(sarAdjunto);
                                }catch(Exception ex){
                                    ex.printStackTrace();
                                    msgAdvertencia += "<ul>Un Archivo adjunto no ha sido almacenado en base de datos exitosamente: " + ex.toString();
                                }
                            }
                        }

                        //Proceso exitoso
                        out.print("<!--EXITO-->Comprobante actualizado en sistema SAR. ");
                        if (msgAdvertencia.length()>0)
                            out.print("<br/><br/>Hay algunas <b>advertencias</b> en el proceso: " + msgAdvertencia);
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                    msgError+= "Ha ocurrido un error inesperado al procesar la actualización de datos del comprobante SAR." + ex.toString();
                }
            }

        }
        
    }
    
    if (msgError.equals("") && mode!=1
            && mode!=2){
        out.print("<!--EXITO-->");
    }
%>
<%
    if (!msgError.equals("")){
        out.print("<!--ERROR-->"+msgError);
    }
%>