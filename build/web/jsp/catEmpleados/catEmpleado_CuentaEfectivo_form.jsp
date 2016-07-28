<%-- 
    Document   : catEmpleado_CuentaEfectivo_form
    Created on : 27/07/2015, 11:52:11 AM
    Author     : HpPyme
--%>

<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.dao.dto.CuentaEfectivo"%>
<%@page import="com.tsp.sct.bo.CuentaEfectivoBO"%>
<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    int idCuentaEfectivo = -1;
    try{ idCuentaEfectivo = Integer.parseInt(request.getParameter("idCuentaEfectivo")); }catch(NumberFormatException e){}    
    
    String filtroBusqueda = "";     
    
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    CuentaEfectivo cuentaEfeDto = null ;
    Empleado empleadoDto =  null;
    String empledoNombre = "" ;
    
    if(idCuentaEfectivo > 0){        
        cuentaEfeDto = new CuentaEfectivoBO(idCuentaEfectivo,user.getConn()).getCuentaEfectivo();
        if(cuentaEfeDto!= null){
            empleadoDto =  new EmpleadoBO(cuentaEfeDto.getIdEmpleado(), user.getConn()).getEmpleado();
            
            empledoNombre = empleadoDto!=null?(empleadoDto.getNombre()!=null?" " + empleadoDto.getNombre():"" 
                                        + empleadoDto.getApellidoPaterno()!=null?" " + empleadoDto.getApellidoPaterno():"" 
                                        + empleadoDto.getApellidoMaterno()!=null?" " + empleadoDto.getApellidoMaterno():""):"" ;
            
        }
    }   
    
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    DecimalFormat decimales = new DecimalFormat("0.00");  
    
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/keyWordSEO.jsp" />

        <title><jsp:include page="../include/titleApp.jsp" /></title>

        <jsp:include page="../include/skinCSS.jsp" />

        <jsp:include page="../include/jsFunctions.jsp"/>
        
        <script type="text/javascript">            
            
        </script>
    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Cuenta de Efectivo</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <!--Inicio columna derecha -->
                        <div class="column_left">
                            <div class="header">
                                <span>                                    
                                    Billetes
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    
                                    <p>
                                        <label>Billete $ 1000</label><br/>
                                        <input maxlength="50" type="text" id="billete1000" name="billete1000" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getBillete1000():0%>" readonly/>
                                        &nbsp;&nbsp;&nbsp; x 1000 = &nbsp;&nbsp;&nbsp;
                                        <input maxlength="50" type="text" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getBillete1000()*1000:0%>" readonly/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Billete $ 500</label><br/>
                                        <input maxlength="50" type="text" id="billete500" name="billete500" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getBillete500():0%>" readonly/>
                                        &nbsp;&nbsp;&nbsp;&nbsp; x 500 = &nbsp;&nbsp;&nbsp;
                                        <input maxlength="50" type="text" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getBillete500()*500:0%>" readonly/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Billete $ 200</label><br/>
                                        <input maxlength="50" type="text" id="billete200" name="billete200" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getBillete200():0%>" readonly/>
                                        &nbsp;&nbsp;&nbsp;&nbsp; x 200 = &nbsp;&nbsp;&nbsp;
                                        <input maxlength="50" type="text" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getBillete200()*200:0%>" readonly/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Billete $ 100</label><br/>
                                        <input maxlength="50" type="text" id="billete100" name="billete100" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getBillete100():0%>" readonly/>
                                        &nbsp;&nbsp;&nbsp;&nbsp; x 100 = &nbsp;&nbsp;&nbsp;
                                        <input maxlength="50" type="text" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getBillete100()*100:0%>" readonly/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Billete $ 50</label><br/>
                                        <input maxlength="50" type="text" id="billete50" name="billete50" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getBillete50():0%>" readonly/>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; x 50 = &nbsp;&nbsp;&nbsp;
                                        <input maxlength="50" type="text" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getBillete50()*50:0%>" readonly/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Billete $ 20</label><br/>
                                        <input maxlength="50" type="text" id="billete1000" name="billete20" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getBillete20():0%>" readonly/>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; x 20 = &nbsp;&nbsp;&nbsp;
                                        <input maxlength="50" type="text" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getBillete20()*20:0%>" readonly/>
                                    </p>
                                    <br/><br/><br/><br/><br/><br/>
                                   <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                            </div>
                        </div>
                        <!-- Fin columna Derecha -->
                        
                        <!--Inicio columna derecha -->
                        <div class="column_right">
                            <div class="header">
                                <span>                                    
                                    Monedas
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                     <p>
                                        <label>Moneda $ 20</label><br/>
                                        <input maxlength="50" type="text" id="moneda20" name="moneda20" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getMoneda20():0%>" readonly/>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; x 20 = &nbsp;&nbsp;&nbsp;
                                        <input maxlength="50" type="text" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getMoneda20()*20:0%>" readonly/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Moneda $ 10</label><br/>
                                        <input maxlength="50" type="text" id="moneda10" name="moneda10" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getMoneda10():0%>" readonly/>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; x 10 = &nbsp;&nbsp;&nbsp;
                                        <input maxlength="50" type="text" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getMoneda10()*10:0%>" readonly/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Moneda $ 5</label><br/>
                                        <input maxlength="50" type="text" id="moneda5" name="moneda5" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getMoneda5():0%>" readonly/>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; x 5 = &nbsp;&nbsp;&nbsp;
                                        <input maxlength="50" type="text" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getMoneda5()*5:0%>" readonly/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Moneda $ 2</label><br/>
                                        <input maxlength="50" type="text" id="moneda2" name="moneda2" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getMoneda2():0%>" readonly/>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; x 2 = &nbsp;&nbsp;&nbsp;
                                        <input maxlength="50" type="text" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getMoneda2()*2:0%>" readonly/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Moneda $ 1</label><br/>
                                        <input maxlength="50" type="text" id="moneda1" name="moneda1" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getMoneda1():0%>" readonly/>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; x 1 = &nbsp;&nbsp;&nbsp;
                                        <input maxlength="50" type="text" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getMoneda1()*1:0%>" readonly/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Moneda $ 0.50</label><br/>
                                        <input maxlength="50" type="text" id="moneda050" name="moneda050" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getMoneda050():0%>" readonly/>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; x 0.50 = &nbsp;&nbsp;&nbsp;
                                        <input maxlength="50" type="text" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?decimales.format(cuentaEfeDto.getMoneda050()*0.50):0%>" readonly/>
                                    </p>
                                    <br/>                                    
                                    <p>
                                        <label>Moneda $ 0.20</label><br/>
                                        <input maxlength="50" type="text" id="moneda020" name="moneda020" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getMoneda020():0%>" readonly/>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; x 0.20 = &nbsp;&nbsp;&nbsp;
                                        <input maxlength="50" type="text" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?decimales.format(cuentaEfeDto.getMoneda020()*0.20):0%>" readonly/>
                                    </p>
                                    <br/>
                                    <p>
                                        <label>Moneda $ 0.10</label><br/>
                                        <input maxlength="50" type="text" id="moneda010" name="moneda010" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?cuentaEfeDto.getMoneda010():0%>" readonly/>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; x 0.10 = &nbsp;&nbsp;&nbsp;
                                        <input maxlength="50" type="text" style="width:100px"
                                               value="<%=cuentaEfeDto!=null?decimales.format(cuentaEfeDto.getMoneda010()*0.10):0%>" readonly/>
                                    </p>
                                    <br/>
                                                                                                       
                            </div>
                        </div>
                        <!-- Fin columna Derecha -->
                        
                        
                        
                        
                        
                    </div>
                    </form>
                    <!--TODO EL CONTENIDO VA AQUÍ-->

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>


    </body>
</html>
<%}%>
