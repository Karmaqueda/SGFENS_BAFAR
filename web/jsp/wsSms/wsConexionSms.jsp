<%-- 
    Document   : wsConexionSms
    Created on : 31/03/2016, 10:50:38 AM
    Author     : ISCesar
--%>
<%@page import="com.tsp.sgfens.ws.response.WSResponseInsert"%>
<%@page import="com.tsp.sgfens.ws.sms.response.*"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.tsp.sct.dao.jdbc.ResourceManager"%>
<%@page import="com.tsp.sgfens.ws.sms.bo.InsertaActualizaWsBO"%>
<%@page import="com.tsp.sct.util.GenericMethods"%>
<%@page import="com.tsp.sgfens.ws.response.WSResponse"%>
<%@page import="com.tsp.sgfens.ws.sms.bo.ConsultaWsBO"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String metodoWs = request.getParameter("metodoWs")!=null?new String(request.getParameter("metodoWs").getBytes("ISO-8859-1"),"UTF-8"):"";
    
    Gson gson = new Gson();
    String jsonResponse = "";
    if (!metodoWs.equals("")){
        try{
            //Iniciamos conexion a Base de datos
            Connection conn = ResourceManager.getConnection();
            
            //Iniciamos instancias de Clases controladoras de WS SMS
            ConsultaWsBO consultaWsBO = new ConsultaWsBO(conn);
            InsertaActualizaWsBO insertaActualizaWsBO = new InsertaActualizaWsBO(conn);
            
            //Dto de Request generico, deben llevarlo todos los metodos
            String loginSmsMovilRequestJson = request.getParameter("loginSmsMovilRequestJson")!=null?new String(request.getParameter("loginSmsMovilRequestJson").getBytes("ISO-8859-1"),"UTF-8"):"";
            System.out.println("\nMETODO: " + metodoWs + "");
            System.out.println("REQUEST JSON: \n" + loginSmsMovilRequestJson + "\n");

            //Procesamos por metodo de WS invocado
            if (metodoWs.equals("loginByDispositivoMovilSMS")){ //Login
                //Efectuamos operación
                LoginSmsMovilResponse responseWS = consultaWsBO.loginByDispositivoMovilSMS(loginSmsMovilRequestJson);

                //Transformamos de objeto a formato JSON
                jsonResponse = gson.toJson(responseWS);
            }else if (metodoWs.equals("consultaSmsPorEnviar")){ //consulta de SMS por enviar en dispositivo
                //Efectuamos operación
                ConsultaSmsPorEnviarResponse responseWS = consultaWsBO.getSmsPorEnviar(loginSmsMovilRequestJson);

                //Transformamos de objeto a formato JSON
                jsonResponse = gson.toJson(responseWS);
            }else if (metodoWs.equals("registraProcesoSMS")){ //consulta de SMS por enviar en dispositivo
                //Efectuamos operación
                String registroSmsProcesoRequestJson = request.getParameter("registroSmsProcesoRequestJson")!=null?new String(request.getParameter("registroSmsProcesoRequestJson").getBytes("ISO-8859-1"),"UTF-8"):"";
                RegistroSmsProcesoResponse responseWS = insertaActualizaWsBO.registrarSmsProcesados(loginSmsMovilRequestJson, registroSmsProcesoRequestJson);

                //Transformamos de objeto a formato JSON
                jsonResponse = gson.toJson(responseWS);
            }else if (metodoWs.equals("pushSms")){ //Registra un SMS para ser enviado posteriormente
                //Efectuamos operación
                String dest = request.getParameter("dest")!=null?new String(request.getParameter("dest").getBytes("ISO-8859-1"),"UTF-8"):"";
                String msg = request.getParameter("msg")!=null?new String(request.getParameter("msg").getBytes("ISO-8859-1"),"UTF-8"):"";
                String rtype = request.getParameter("rtype")!=null?new String(request.getParameter("rtype").getBytes("ISO-8859-1"),"UTF-8"):"";
                WSResponseInsert responseWS = insertaActualizaWsBO.crearSms(true, dest, msg, "", "Demo SMS");

                if (rtype.equals("") || rtype.equals("json")){
                    //Transformamos de objeto a formato JSON
                    jsonResponse = gson.toJson(responseWS);
                }else if (rtype.equals("HTML")){
                    if (!responseWS.isError()){
                        jsonResponse = "<h1>Mensaje registrado, en breve será enviado. Movil Pyme agradece su preferencia.</h1>";
                    }else{
                        jsonResponse = "<h2>Error en mensaje: " + responseWS.getMsgError() + "</h2>";
                    }
                }
            }else{
                //No se invoco ningun metodo de WS valido
                WSResponse responseWS = new WSResponse();
                responseWS.setError(true);
                responseWS.setNumError(901);//error de usuario/implementador
                responseWS.setMsgError("No se envio un parametro 'metodoWs' existente.");
                jsonResponse = gson.toJson(responseWS);
            }

            //Nos aseguramos de cerrar conexion a base de datos
            if (conn!=null && !conn.isClosed())
                ResourceManager.close(conn);
            
        }catch(Exception ex){
            ex.printStackTrace();
            WSResponse responseWS = new WSResponse();
            responseWS.setError(true);
            responseWS.setNumError(902);//error de servidor/inesperado
            responseWS.setMsgError("Error inesperado en el servidor: " + GenericMethods.exceptionStackTraceToString(ex));
            jsonResponse = gson.toJson(responseWS);
        }
    }else{
        WSResponse responseWS = new WSResponse();
        responseWS.setError(true);
        responseWS.setNumError(901);//error de usuario/implementador
        responseWS.setMsgError("No se envio el parametro 'metodoWs'");
        jsonResponse = gson.toJson(responseWS);
    }
    out.print(jsonResponse);
%>