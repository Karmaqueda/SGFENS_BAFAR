<%-- 
    Document   : testPushSms
    Created on : 19/05/2016, 12:43:03 PM
    Author     : ISCesar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Prueba envio Mensaje SMS</title>
    </head>
    <body>
        <h1>Prueba</h1>
        
        <form action="http://localhost:8085/SGFENS_V2/jsp/wsSms/wsConexionSms.jsp" method="post" target="_blank">
            <input type="hidden" name="metodoWs" value="pushSms"/>
            <input type="hidden" name="rtype" value="HTML"/>
            Celular: <input type="number" name="dest" value=""/>
            <br/>
            Mensaje: <input type="text" name="msg" value="" maxlength="35"/>
            <br/>
            <input type="submit" value="Enviar">
        </form>
    </body>
</html>
