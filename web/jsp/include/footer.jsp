<%-- 
    Document   : footer
    Created on : 19-oct-2012, 19:35:57
    Author     : ISCesarMartinez poseidon24@hotmail.com
--%>

<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<!--<br class="clear"/><br class="clear"/>-->

<head>
    <script type="text/javascript">
    function obscureseteseme(){
        $("#juanGuiaDinamica").click();
    }
    </script>
</head>

<div class="clearfooter" ></div>

<!-- Begin footer -->
<div id="footer">
      <!--SGFenS | &copy; Copyright 2011-2013 by <a href="http://facturaensegundos.com">FacturaEnSegundos</a>-->
      Pretoriano Soft. | &copy; Copyright 2011-2016
</div>

<%
//verificamos si va a aparecer la guia dinamica y si la empresa aun tiene disponible la guia
EmpresaBO empresaBO = new EmpresaBO(user.getConn());
EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());
  
String verificadorSesionGuiaCerrada = "0";
try{
    if(session.getAttribute("sesionCerrada")!= null){
        verificadorSesionGuiaCerrada = (String)session.getAttribute("sesionCerrada");
    }
}catch(Exception e){}
//termina simulacion

if( empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0") ){%>
    <div id="juanGuiaDinamica" class="expose">            
        <%@include file="../guiaInteractiva/guiaDinamica.jsp" %>
    </div>
        <div id="overlay"></div>
<%}%>
<script>
    obscureseteseme();
</script>
<!-- End footer -->