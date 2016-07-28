<%--
    Document   : login
    Created on : 15/08/2011, 04:45:10 PM
    Author     : ISC Cesar Martinez poseidon24@hotmail.com
--%>


<%@page import="java.util.Calendar"%>
<%@page import="com.tsp.sct.dao.jdbc.UsuariosDaoImpl"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.tsp.sct.bo.BitacoraCreditosOperacionBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.bo.SGAccionBitacoraBO"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean id="intentosFallidos" scope="session" class="java.lang.String"/>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%

    String login="";
    String pwd = "";
    String mensajeUsuario = "";

    String urlRedir ="main.jsp";

    int intentosFallidosInt = 0;
    String logSCT = "0";

    try {
        intentosFallidosInt = Integer.parseInt(intentosFallidos);
    }
    catch (Exception e) {
        intentosFallidos = "0";
        intentosFallidosInt = 0;
    }


    String action = request.getParameter("action")==null?"":request.getParameter("action");

    if(action.trim().equalsIgnoreCase("loginRequired")){
        urlRedir = request.getParameter("urlSource")==null?urlRedir:request.getParameter("urlSource");
    }

 
    if (action.trim().equalsIgnoreCase("logout")) {
        if (user!=null && user.getUser()!=null){
            SGAccionBitacoraBO accionBitacora = new SGAccionBitacoraBO(user.getConn());
            accionBitacora.insertAccionLogout(user.getUser().getIdUsuarios(), "");
                //actualizamos la bandera del cierre de sesion para que quede una sesion libre de acceso de usuario
                user.getUser().setLogin((short)0);
                new UsuariosDaoImpl(user.getConn()).update(user.getUser().createPk(), user.getUser());
        }
        
        request.getSession().setAttribute("user", null);
        user=null;
        //mensajeUsuario="<script>apprise('<center><img src=../../images/candado.png> <br/>Sesión finalizada. <br/>Que tengas un excelente día, vuelve pronto!</center>',{'animate':true});</script>";
        mensajeUsuario = "<script> $('#login_info').css('display', 'block');</script> <center>Sesión finalizada.</center>";
    }else if (action.trim().equalsIgnoreCase("logoutInactiveSession")) {
        if (user!=null && user.getUser()!=null){
            SGAccionBitacoraBO accionBitacora = new SGAccionBitacoraBO(user.getConn());
            accionBitacora.insertAccionLogout(user.getUser().getIdUsuarios(), "Cierre de sesión automática por inactividad del usuario.");            
        }
            try{
                //actualizamos la bandera del cierre de sesion para que quede una sesion libre de acceso de usuario
                user.getUser().setLogin((short)0);
                new UsuariosDaoImpl(user.getConn()).update(user.getUser().createPk(), user.getUser());
            }catch(Exception e){}
        request.getSession().setAttribute("user", null);
        user=null;
        
        //mensajeUsuario="<script>apprise('<center><img src=../../images/candado.png> <br/>Sesión finalizada por inactividad del usuario.</center>',{'animate':true});</script>";
        mensajeUsuario = "<script> $('#login_info').css('display', 'block');</script> <center>Sesión finalizada por inactividad del usuario.</center>";
    }
    else if (request.getParameter("username")!=null) {
        login = request.getParameter("username")==null?"":request.getParameter("username");
        pwd = request.getParameter("password")==null?"":request.getParameter("password");
 
        logSCT = request.getParameter("logSCT")==null?"0":request.getParameter("logSCT");
        
        //Validamos el Login
        try{
            if(logSCT.equals("0")){
                System.out.println("login normal  -> " + login + ":" + pwd + ":" + logSCT);
                if (user.login(login, pwd)) {
                    SGAccionBitacoraBO accionBitacora = new SGAccionBitacoraBO(user.getConn());
                    accionBitacora.insertAccionLogin(user.getUser().getIdUsuarios(), "");

                    request.getSession().setAttribute("user", user);

                    /*if (user.requirePasswordChange()){
                        urlRedir = "../user/perfil.jsp?mode=1";
                    }*/

                    //Consumo de Creditos Operacion
                    int accionBCO = 0;
                    try{
                        BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(user.getConn());
                        accionBCO = bcoBO.operacionLoginConsola(user);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    
                    //de ser que existe validamos el tiempo 
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.HOUR, -1);//hace una hora era
                        Date date = c.getTime();
                        //if(date.after(user.getFechaAccesoAnterior())){
                    
                    //validamos que solo exista una sola sesion activa de usuario y si existe por no salir bien validamos por tiempo
                    if(user.getUser().getLogin() == (short)0 || date.after(user.getFechaAccesoAnterior())){
                        //actualizamos el valor de  login, de que entro una sesion activa de usuario
                        user.getUser().setLogin((short)1);
                        user.getUser().setFechaUltimoAcceso(new Date());                        
                        new UsuariosDaoImpl(user.getConn()).update(user.getUser().createPk(), user.getUser());
                        
                        if (accionBCO==0){
                            //Login normal (Tipo de servicio post-pago o pre-pago con créditos disponibles)
                            response.sendRedirect(urlRedir);
                        }else if (accionBCO == 1){
                            //Tipo de servicio pre-pago (on demand) sin créditos y menos de 3 intentos login
                            mensajeUsuario = "<script>apprise('<center><img src=../../images/warning.png> <br/> <b>" + BitacoraCreditosOperacionBO.MSG_ACCION_LOGIN_1 + "</b></center>',{'animate':true},function(r){location.href = '"+urlRedir+"';});</script>";
                        }else if (accionBCO == 2){
                            //Tipo de servicio pre-pago (on demand) sin créditos y mas de 3 intentos login
                            mensajeUsuario = "<script>apprise('<center><img src=../../images/warning.png> <br/> <b>" + BitacoraCreditosOperacionBO.MSG_ACCION_LOGIN_2 + "</b></center>',{'animate':true},function(r){location.href = '"+urlRedir+"';});</script>";
                            user.setPermisoVerMenu(false);
                        }
                    }else{
                        mensajeUsuario = "<script> $('#login_info').css('display', 'block');</script> Ya cuenta con una sesión activa, no es posible ingresar 2 veces con el mismo usuario, <br/>o si cerro la ventana del sistema, favor de esperar 20 minutos, para intentarlo nuevamente.";
                    }
                }else{
                    //mensajeUsuario = "<script>apprise('Usuario o contraseña inválidos',{'animate':true}); $('username').focus();</script>";
                    mensajeUsuario = "<script> $('#login_info').css('display', 'block');</script> Usuario o contraseña inválidos";
                }            
            }else{//login desde sct
                
                
                if (user.login(URLDecoder.decode(login),URLDecoder.decode(pwd),1)) { // se envia cualquier entero solo para llamar al metodo sobrecargado
                    System.out.println("login sct  -> " + login + ":" + pwd + ":" + logSCT);
                    SGAccionBitacoraBO accionBitacora = new SGAccionBitacoraBO(user.getConn());
                    accionBitacora.insertAccionLogin(user.getUser().getIdUsuarios(), "");

                    request.getSession().setAttribute("user", user);

                    /*if (user.requirePasswordChange()){
                        urlRedir = "../user/perfil.jsp?mode=1";
                    }*/

                    //Consumo de Creditos Operacion
                    int accionBCO = 0;
                    try{
                        BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(user.getConn());
                        accionBCO = bcoBO.operacionLoginConsola(user);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }

                    if (accionBCO==0){
                        //Login normal (Tipo de servicio post-pago o pre-pago con créditos disponibles)
                        response.sendRedirect(urlRedir);
                    }else if (accionBCO == 1){
                        //Tipo de servicio pre-pago (on demand) sin créditos y menos de 3 intentos login
                        mensajeUsuario = "<script>apprise('<center><img src=../../images/warning.png> <br/> " + BitacoraCreditosOperacionBO.MSG_ACCION_LOGIN_1 + "</center>',{'animate':true},function(r){location.href = '"+urlRedir+"';});</script>";
                    }else if (accionBCO == 2){
                        //Tipo de servicio pre-pago (on demand) sin créditos y mas de 3 intentos login
                        mensajeUsuario = "<script>apprise('<center><img src=../../images/warning.png> <br/> " + BitacoraCreditosOperacionBO.MSG_ACCION_LOGIN_2 + "</center>',{'animate':true},function(r){location.href = '"+urlRedir+"';});</script>";
                        user.setPermisoVerMenu(false);
                    }
                }else{
                    //mensajeUsuario = "<script>apprise('Usuario o contraseña inválidos',{'animate':true}); $('username').focus();</script>";
                    mensajeUsuario = "<script> $('#login_info').css('display', 'block');</script> Usuario o contraseña inválidos";
                }
                
            }
            
       }catch(Exception ex){
            mensajeUsuario = "<script> $('#login_info').css('display', 'block');</script> " + ex.getMessage();
       }
        
     }
 
    
        
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="shortcut icon" href="../../images/favicon.ico">
    
    <title><jsp:include page="../../jsp/include/titleApp.jsp" /></title>
    <jsp:include page="../include/keyWordSEO.jsp" />

    <jsp:include page="../../jsp/include/skinCSS_login.jsp" />

    <jsp:include page="../../jsp/include/jsFunctions.jsp" />

    <script type="text/javascript" charset="utf-8"> 
        $(function(){ 
            // find all the input elements with title attributes
            $('input[title!=""]').hint();
            $('#login_info').click(function(){
                        $(this).fadeOut('fast');
                });
        });
        
        function submitFormulario(){
            $('#login_info').click(function(){
                        $(this).fadeOut('fast');
                });
            $('#form_login').submit();
        }
        
    </script>
    <!---->
    <!--<script language="javascript" type="text/javascript">
        $(document).ready(function(){ $(":input:first").focus(); });
    </script>-->
    <!---->

</head>
<body class="login">
    <!--<div id="adholder">
         <div id="adinner">
           <div class="adright"></div>
        </div>
    </div>-->
<div>
    <!-- Inicio de ventana de login -->

    <div align="center" id="login_wrapper">
            <div id="login_info" class="alert_warning noshadow" 
                 style="width:350px;margin:0;padding:auto; display: none;">
                <p><%=mensajeUsuario%></p>
            </div>
            <br class="clear"/>
            <div id="login_top_window">
                <div class="loginLinePretoriano">&nbsp;</div>
            </div>

            <!-- Inicio de contenido -->
            <div id="login_body_window">
                    <div class="inner">
                        <center>
                            <img src="../../images/login/logo.png" alt="logo"/>
                        </center>
                            
                            <form action="login.jsp?<%out.print((request.getQueryString()!=null)&&(!action.trim().equalsIgnoreCase("logout") && !action.trim().equalsIgnoreCase("logoutInactiveSession"))?request.getQueryString():"action=login");%>" method="post" id="form_login" name="form_login">
                                    <p>
                                        <input type="text" id="username" name="username" style="width:285px" title="Usuario"/>
                                    </p>
                                    <p>
                                        <input type="password" id="password" name="password" style="width:285px" title="******"/>
                                    </p>
                                    &nbsp;&nbsp;&nbsp;<a href="../inicio/restorePassword.jsp" class="forgot_pass">Olvide mi Contraseña</a>
                                    <p>
                                        <!--<input type="submit" id="submit" name="submit" value="Entrar" class="Login" style="margin-right:15px"/>-->
                                        <div id="login_submit_text">
                                            <a href="#" onclick="submitFormulario();" class="forgot_pass">Entrar</a>
                                        </div>
                                    </p>
                            </form>
                    </div>
            </div>
            <!-- FIN de contenido -->
            <div id="login_footer_window">
                <div class="loginLinePretoriano">&nbsp;</div>
            </div>
            <div id="login_right_bg">
                &nbsp;
            </div>
    </div>
     <!-- FIN de ventana de login --> 
    
    <!--coliseo-->
    <div style="display:inline-block; width:45%" id="coliseo">
        <img src="../../images/login/coliseo.png" width="120%" height="120%" />
    </div>

    <!--coliseo-->
</div> 
</body>
</html>
