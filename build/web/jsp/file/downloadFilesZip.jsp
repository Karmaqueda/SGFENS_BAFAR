<%-- 
    Document   : downloadFilesZip
    Created on : 17/10/2011, 01:01:11 PM
    Author     : ISC Cesar Martinez poseidon24@hotmail.com
--%>
<%@page import="com.tsp.sct.bo.VistaCxcBO"%>
<%@page import="com.tsp.sct.bo.VistaCxpBO"%>
<%@page import="com.tsp.sct.bo.SGAccionBitacoraBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.bo.ComprobanteFiscalBO"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String msgError = "";
    /**
     * Modos: 
     *      1: Descargar ZIP Comprobantes Fiscales 
     *      2: Descargar ZIP CxP (CFDI y Vales Azules)
     *      3: Descargar ZIP CxC (CFDI y Pedidos)
     */
    int mode = -1;
    try {
        mode = Integer.parseInt(request.getParameter("mode"));
    } catch (NumberFormatException e) {
    }

    //Gson gson = new Gson();
    //GenericValidator genericValidator = new GenericValidator();
    //Configuration appConfig = new Configuration();
    //int idEmpresa = user.getUser().getIdEmpresa();

    /*
     * Procesamiento
     */
    if (mode == -1) {
        msgError = "No se eligio una acción correcta. Reintente el llenado de los datos.";
    } else if (mode == 1) {
        //1: Descargar ZIP Comprobantes Fiscales
        String idComprobanteStrArray = "";
        boolean descargaXML = false;
        boolean descargaPDF = false;
        try {
            idComprobanteStrArray = request.getParameter("id_comprobante_array") != null ? request.getParameter("id_comprobante_array") : "";
        } catch (Exception e) {}
        try {  descargaXML = (request.getParameter("xml") != null); } catch (Exception e) {}
        try {  descargaPDF = (request.getParameter("pdf") != null); } catch (Exception e) {}
        
        
        if (!idComprobanteStrArray.equals("")) {

            String[] idArchivoArray = idComprobanteStrArray.split(",");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            if (idArchivoArray.length > 0) {
                try {
                    ComprobanteFiscalBO compFiscalBO = new ComprobanteFiscalBO(user.getConn());
                    baos = compFiscalBO.getZipFromFiles(idArchivoArray, descargaXML, descargaPDF);

                    if (baos.size()>0){
                        // setting the content type
                        response.setContentType("application/zip");
                        response.setHeader("Content-disposition",
                                "attachment; filename=archivosZip_" + DateManage.getDateHourString() + ".zip");

                        SGAccionBitacoraBO accionBitacoraBO = new SGAccionBitacoraBO(user.getConn());
                        accionBitacoraBO.insertAccionDescarga(user.getUser().getIdUsuarios(), "Descarga de archivo ZIP con multiples comprobantes. Solicitado a partir de: " + request.getRequestURL());

                        try {
                            ServletOutputStream output = response.getOutputStream();
                            // the contentlength
                            response.setContentLength(baos.size());
                            // write ByteArrayOutputStream to the ServletOutputStream
                            baos.writeTo(output);
                            output.flush();
                            //output.close();

                        } finally {
                            baos.close();
                            //respOut.close();
                        }
                    }else{
                        msgError += "El archivo ZIP esta vacío, no se encontro posiblemente ningún archivo a descargar.";
                    }
                } catch (Exception e) {
                    msgError += e.getMessage();
                }
            } else {
                msgError += "No se establecio ningun archivo a descargar.";
            }
        } else {
            msgError += "No se indico ningun archivo a descargar.";
        }

    }else if (mode == 2) {
        //2: Descargar ZIP Cuentas por Pagar
        String idComprobanteStrArray = "";
        String idValeAzulStrArray = "";
        boolean descargaXML = false;
        boolean descargaPDF = false;
        try {
            idComprobanteStrArray = request.getParameter("id_comprobante_array") != null ? request.getParameter("id_comprobante_array") : "";
        } catch (Exception e) {}
        try {
            idValeAzulStrArray = request.getParameter("id_vale_azul_array") != null ? request.getParameter("id_vale_azul_array") : "";
        } catch (Exception e) {}
        try {  descargaXML = (request.getParameter("xml") != null); } catch (Exception e) {}
        try {  descargaPDF = (request.getParameter("pdf") != null); } catch (Exception e) {}
        
        
        if (!idComprobanteStrArray.equals("") || !idValeAzulStrArray.equals("")) {

            String[] idComprobanteFiscalArray = idComprobanteStrArray.split(",");
            String[] idValeAzulArray = idValeAzulStrArray.split(",");
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            if (idComprobanteFiscalArray.length > 0 || idValeAzulArray.length>0) {
                try {
                    VistaCxpBO vistaCxpBO = new VistaCxpBO(user.getConn());
                    baos = vistaCxpBO.getZipFromFiles(idComprobanteFiscalArray, idValeAzulArray, descargaXML, descargaPDF);

                    if (baos.size()>0){
                        // setting the content type
                        response.setContentType("application/zip");
                        response.setHeader("Content-disposition",
                                "attachment; filename=archivosZip_" + DateManage.getDateHourString() + ".zip");

                        SGAccionBitacoraBO accionBitacoraBO = new SGAccionBitacoraBO(user.getConn());
                        accionBitacoraBO.insertAccionDescarga(user.getUser().getIdUsuarios(), "Descarga de archivo ZIP (CxP) con multiples comprobantes. Solicitado a partir de: " + request.getRequestURL());

                        try {
                            ServletOutputStream output = response.getOutputStream();
                            // the contentlength
                            response.setContentLength(baos.size());
                            // write ByteArrayOutputStream to the ServletOutputStream
                            baos.writeTo(output);
                            output.flush();
                            //output.close();

                        } finally {
                            baos.close();
                            //respOut.close();
                        }
                    }else{
                        msgError += "El archivo ZIP esta vacío, no se encontro posiblemente ningún archivo a descargar.";
                    }
                } catch (Exception e) {
                    msgError += e.getMessage();
                }
            } else {
                msgError += "No se establecio ningun archivo a descargar.";
            }
        } else {
            msgError += "No se indico ningun archivo a descargar.";
        }

    }else if (mode == 3) {
        //3: Descargar ZIP CxC
        String idComprobanteStrArray = "";
        String idPedidoStrArray = "";
        boolean descargaXML = false;
        boolean descargaPDF = false;
        try {
            idComprobanteStrArray = request.getParameter("id_comprobante_array") != null ? request.getParameter("id_comprobante_array") : "";
        } catch (Exception e) {}
        try {  descargaXML = (request.getParameter("xml") != null); } catch (Exception e) {}
        try {  descargaPDF = (request.getParameter("pdf") != null); } catch (Exception e) {}
        try {
            idPedidoStrArray = request.getParameter("id_pedido_array") != null ? request.getParameter("id_pedido_array") : "";
        } catch (Exception e) {}
        
        if (!idComprobanteStrArray.equals("") || !idPedidoStrArray.equals("")) {
            String[] idComprobanteArray = idComprobanteStrArray.split(",");
            String[] idPedidoArray = idPedidoStrArray.split(",");
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            if (idComprobanteArray.length > 0 || idPedidoArray.length > 0) {
                try {
                    VistaCxcBO vistaCxcBO = new VistaCxcBO(user.getConn());
                    baos = vistaCxcBO.getZipFromFiles(idComprobanteArray, idPedidoArray, descargaXML, descargaPDF);

                    if (baos.size()>0){
                        // setting the content type
                        response.setContentType("application/zip");
                        response.setHeader("Content-disposition",
                                "attachment; filename=archivosZip_" + DateManage.getDateHourString() + ".zip");

                        SGAccionBitacoraBO accionBitacoraBO = new SGAccionBitacoraBO(user.getConn());
                        accionBitacoraBO.insertAccionDescarga(user.getUser().getIdUsuarios(), "Descarga de archivo ZIP con multiples comprobantes. Solicitado a partir de: " + request.getRequestURL());

                        try {
                            ServletOutputStream output = response.getOutputStream();
                            // the contentlength
                            response.setContentLength(baos.size());
                            // write ByteArrayOutputStream to the ServletOutputStream
                            baos.writeTo(output);
                            output.flush();
                            //output.close();

                        } finally {
                            baos.close();
                            //respOut.close();
                        }
                    }else{
                        msgError += "El archivo ZIP esta vacío, no se encontro posiblemente ningún archivo a descargar.";
                    }
                } catch (Exception e) {
                    msgError += e.getMessage();
                }
            } else {
                msgError += "No se establecio ningun archivo a descargar.";
            }
        } else {
            msgError += "No se indico ningun archivo a descargar.";
        }

    }

    if (msgError.equals("") && mode!=1 
            && mode!=2
            && mode!=3) {
        out.print("<!--EXITO-->");
    }
%>
<%
    if (!msgError.equals("")) {
        out.print("<!--ERROR-->" + msgError);
    }
%>