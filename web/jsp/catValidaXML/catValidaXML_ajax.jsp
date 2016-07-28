<%-- 
    Document   : catValidaXML_ajax
    Created on : 6/03/2014, 12:27:14 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.BitacoraCreditosOperacionBO"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.tsp.sct.dao.jdbc.ValidacionXmlDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.ValidacionXml"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaDaoImpl"%>
<%@page import="com.tsp.interconecta.ws.WsInformaValidacion"%>
<%@page import="com.tsp.interconecta.ws.WsValidaResp"%>
<%@page import="com.tsp.sct.bo.ValidaXmlBO"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";

    int idEmpresa = user.getUser().getIdEmpresa();
    Empresa empresa = new Empresa();
    EmpresaBO eBO = new EmpresaBO(user.getConn());
    empresa = eBO.getEmpresaMatriz(idEmpresa);//extraemmos la empresa padre para ver si tiene creditos para validar XML
    EmpresaPermisoAplicacion ePermisoApp = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresa.getIdEmpresa());

    boolean porCreditosOperacion = false;
    if (ePermisoApp!=null){
        if (ePermisoApp.getTipoConsumoServicio()==1){
            porCreditosOperacion= true;
        }
    }
    
    /*
    * Parámetros
    */


    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    String nombreArchivoXml = request.getParameter("nombreArchivoXml")!=null?new String(request.getParameter("nombreArchivoXml").getBytes("ISO-8859-1"),"UTF-8"):"";


    /*
    * Validaciones del servidor
    */

    //Primero validamos si tiene créditos suficientes de Validación
    String msgError = "";
    GenericValidator gc = new GenericValidator();
    if (porCreditosOperacion){
        if(empresa.getCreditosOperacion() <= 0){
            msgError += "No tiene Créditos de Operación disponibles. Favor de contactar a su asesor de ventas.";
        }
    }else{
        if(empresa.getCreditoValidaXml() <= 0){
            msgError += "<ul>No tiene Créditos de Validación de comprobantes fiscales. Favor de contactar a su asesor de ventas.";
        }
    }
    
    

    boolean muestraRespuesta = false;
    WsValidaResp resp = null;


    if(msgError.equals("")){
        /*
        *  Nuevo
        */
        ValidaXmlBO validaXmlBO = new ValidaXmlBO();
        try {
            //cargamos la direccion del archivo a validar:
            Configuration configuration = new Configuration();        

            resp = validaXmlBO.validar(configuration.getApp_content_path()+empresa.getRfc()+"/ValidacionXML/"+nombreArchivoXml);

            if(resp != null){
                String errores = "";
                for (WsInformaValidacion item : resp.getListaErrores()){
                    errores += "(" + item.getCodigo() + ") " + item.getNombre() + " : " + item.getValor() +"\n";
                }

                //Si no existio error en el web service actualizamos los credito disponibles para validacion e insetamos el registro de validacion:
                if(!resp.isIsError()){
                    if (porCreditosOperacion){
                        //Por Creditos de Operacion
                        
                        //Se descuentan 2 creditos de operacion por validacion
                        int creditosUsados =  BitacoraCreditosOperacionBO.CONSUMO_ACCION_VALIDAR_XML;

                        //Registramos descuento de Creditos en Bitacora
                        try{
                            BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(user.getConn());
                            bcoBO.registraDescuento(user.getUser(), creditosUsados, null, 
                                    -1, -1, -1, 
                                    "Valida XML", null, true);
                        }catch(Exception e){
                            msgError += "Ocurrio un error al actualizar Bitacora de Creditos de Operacion: " + e.toString() ;
                        }
                    }else{
                        //Por Creditos de validacion
                        // Se descuenta solo un credito
                        empresa.setCreditoValidaXml( (empresa.getCreditoValidaXml() - 1));
                        //Actualizamos registro de empresa
                        new EmpresaDaoImpl(user.getConn()).update(empresa.createPk(), empresa);
                    }

                    //Cargamos la info al objeto de validacion y lo insertamos en la base de datos
                    ValidacionXml validacionXml = new ValidacionXml();
                    validacionXml.setIdEmpresa(idEmpresa);
                    validacionXml.setNombreArchivo(nombreArchivoXml);
                    validacionXml.setVersionComprobante(resp.getVersionComprobante());
                    validacionXml.setCadenaOriginal(resp.getCadenaOriginalComprobante());
                    if(resp.isComprobanteValido())
                        validacionXml.setComprobanteValido(1);
                    else if(!resp.isComprobanteValido())
                        validacionXml.setComprobanteValido(0);

                    if(resp.isSelloEmisorValido())
                        validacionXml.setSelloEmisorValido(1);
                    else if(!resp.isSelloEmisorValido())
                        validacionXml.setSelloEmisorValido(0);

                    validacionXml.setTimbreFiscal(resp.getTimbreFiscalDigital());
                    validacionXml.setMensajesError(errores);

                    try{
                        new ValidacionXmlDaoImpl(user.getConn()).insert(validacionXml);
                        
                        if (mode.equals("")){
                            muestraRespuesta = true;
                            out.print("<!--EXITO--> <br/>");
                        }else if (mode.equals("1")){
                            Gson gson = new Gson();
                            String jsonOutput = gson.toJson(validacionXml);
                            out.print("<!--EXITO-->"+jsonOutput);
                        }
                    }catch(Exception e){
                        msgError += "Ocurrio un error al insertar el registro de validación: " + e.toString() ;
                    }

                }else{
                    msgError += "<ul>Error desde Web Service Validación (CMM): " + resp.getNumError() + " - " + resp.getErrorMessage();
                }
            }else{
                msgError += "<ul>Ocurrio un error " + validaXmlBO.hayProblema;
            }

        }catch(Exception e){
            e.printStackTrace();
            msgError += "Ocurrio un error al procesar el registro: " + e.toString() ;
        }
    }else{
        //System.out.println("_____TIENE MENSAJE");
        out.print("<!--ERROR-->"+msgError);
    }

    if (!msgError.equals("")){
        out.print("<!--ERROR-->"+msgError);
    }

    if(muestraRespuesta){
        if (msgError.equals("")){
%>
        <table class="data" width="100%" cellpadding="0" cellspacing="0" style="table-layout: fixed; width: 100%">
            <thead>
                <tr>
                    <td><center>Respuesta de Validación</center></td>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>
                        <b>Comprobante Válido?:</b>
                        <%=(resp.isComprobanteValido())==true?"Si":"No"%>
                        <%if(resp.isComprobanteValido()){%>
                            <img src="../../images/icon_accept.png" alt="icon"/>
                        <%}else if(!resp.isComprobanteValido()){%>
                            <img src="../../images/icon_error.png" alt="icon"/>
                        <%}%>
                    </td>
                </tr>
                <tr>
                    <td>
                        <b>Version:</b>
                        <%=((resp.getVersionComprobante()!=null?resp.getVersionComprobante():""))%>
                    </td>
                </tr>
                <tr>
                    <td>
                        <b>Sello emisor Válido?:</b>
                        <%=((resp.isSelloEmisorValido())==true?"Si":"No")%>                            
                        <%if(resp.isSelloEmisorValido()){%>
                            <img src="../../images/icon_accept.png" alt="icon"/>                            
                        <%}else if(!resp.isSelloEmisorValido()){%>
                            <img src="../../images/icon_error.png" alt="icon"/>
                        <%}%>
                    </td>                            
                </tr>
                <tr>
                    <td style="word-wrap: break-word;">
                        <b>Cadena original:</b><br/>
                        <%=((resp.getCadenaOriginalComprobante())!=null?resp.getCadenaOriginalComprobante():"")%>
                    </td>
                </tr>
                <tr>
                    <td style="word-wrap: break-word;">
                        <b>Timbre Fiscal:</b><br/>
                        <%= ((resp.getTimbreFiscalDigital()!=null?(resp.getTimbreFiscalDigital().replaceAll("\"", "&#34;").replaceAll("<", "&lt;").replaceAll("\\>", "&gt;")):"")) %>
                    </td>
                </tr>
                <%if(!resp.isComprobanteValido()){%>
                <tr>                        
                    <td>
                        ======== ERRORES =======
                        <img src="../../images/icon_error.png" alt="icon"/>
                    </td>
                </tr>
                <%}%>
<%
            int j=0;
            for(WsInformaValidacion item : resp.getListaErrores()){                                    
%>
                <tr>
                    <td>
                        <%="(" + item.getCodigo() + ") " + item.getNombre() + " : " + item.getValor()%>
                    </td>
                </tr>                        
<%
            }

%>

            </tbody>
        </table>
<%
        }
    }
%>