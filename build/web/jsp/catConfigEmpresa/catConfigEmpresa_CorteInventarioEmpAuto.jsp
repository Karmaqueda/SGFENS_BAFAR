
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="com.tsp.sct.bo.EmpresaConfigBO"%>
<%@page import="com.tsp.sct.bo.EmpresaConfigBO"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaConfig"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el cliente tiene acceso a este topico
    if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
        response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
        response.flushBuffer();
    } else {

        int idEmpresa = user.getUser().getIdEmpresa();
                      
        String mode = "horaCorteAuto"; // Cambio o nuevo
        
        Empresa empresaDto = new Empresa();
        EmpresaBO empresaBO = new EmpresaBO(user.getConn());
        empresaDto = empresaBO.findEmpresabyId(idEmpresa);
        
        EmpresaConfigBO empresaconfigBO = new EmpresaConfigBO(idEmpresa,user.getConn());
        EmpresaConfig empresaConfigDto = empresaconfigBO.getEmpresaConfig();
        
                
        String lunes = "";
        String martes = "";
        String miercoles = "";
        String jueves = "";
        String viernes = "";
        String sabado = "";
        String domingo = "";
        String todos = "";
        
        StringTokenizer tokensDias = new StringTokenizer(StringManage.getValidString(empresaConfigDto.getDiasCorte()),",");
            String seleccion = "";
            while (tokensDias.hasMoreTokens()) {
                System.out.println("_______recupetando tokens");
                seleccion = "";
                seleccion = tokensDias.nextToken().intern().trim();
                if(seleccion.equals("0")){
                    domingo = seleccion;                
                }else if(seleccion.equals("1")){
                    lunes = seleccion;
                }else if(seleccion.equals("2")){
                    martes = seleccion;
                }else if(seleccion.equals("3")){
                    miercoles = seleccion;
                }else if(seleccion.equals("4")){
                    jueves = seleccion;
                }else if(seleccion.equals("5")){
                    viernes = seleccion;
                }else if(seleccion.equals("6")){
                    sabado = seleccion;
                }
            } 
        
        
        
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
            
            function grabar(){ 
                
                //if (CheckTime()){
                   
                    $.ajax({
                        type: "POST",
                        url: "catConfigEmpresa_ajax.jsp",
                        data: $("#frm_action").serialize(),
                        beforeSend: function(objeto){
                            $("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style=""><center>Procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></center></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_message").html(datos);
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/info.png> <br/>'+ datos +'</center>',{'animate':true},
                                        function(r){
                                            location.href = "../catConfigEmpresa/catConfigEmpresa_CorteInventarioEmpAuto.jsp";
                                        });
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               $("#action_buttons").fadeIn("slow");
                               $.scrollTo('#inner',800);
                           }
                        }
                    });
                //}
            }
            
            
            function todosDiasMarcados(){
                if ($('#diarioReporte').attr('checked')) {
                    //como esta marcado, desmarcamos todos los demas                
                    $('#domingo').attr('checked', true);
                    $('#lunes').attr('checked', true);
                    $('#martes').attr('checked', true);
                    $('#miercoles').attr('checked', true);
                    $('#jueves').attr('checked', true);
                    $('#viernes').attr('checked', true);
                    $('#sabado').attr('checked', true);                    
                }else{
                    //como esta marcado, desmarcamos todos los demas                
                    $('#domingo').attr('checked', false);
                    $('#lunes').attr('checked', false);
                    $('#martes').attr('checked', false);
                    $('#miercoles').attr('checked', false);
                    $('#jueves').attr('checked', false);
                    $('#viernes').attr('checked', false);
                    $('#sabado').attr('checked', false); 
                }                            
            }
            
            
            function CheckTime() 
            {                
                hora=$("#hora").val();
                //if (hora=='') {return} 
                if (hora.length>8) {$("#ajax_message").html("Introdujo una cadena mayor a 8 caracteres");$("#ajax_message").fadeIn("slow");return} 
                if (hora.length!=8) {$("#ajax_message").html("Introducir Hora Formato HH:MM:SS");$("#ajax_message").fadeIn("slow");return}  
                a=hora.charAt(0) //<=2 
                b=hora.charAt(1) //<4 
                c=hora.charAt(2) //: 
                d=hora.charAt(3) //<=5 
                e=hora.charAt(5) //: 
                f=hora.charAt(6) //<=5 
                if ((a==2 && b>3) || (a>2)) {$("#ajax_message").html("El valor que introdujo en la Hora no corresponde, introduzca un digito entre 00 y 23");$("#ajax_message").fadeIn("slow");return}  
                if (d>5) {$("#ajax_message").html("El valor que introdujo en los minutos no corresponde, introduzca un digito entre 00 y 59");$("#ajax_message").fadeIn("slow");return}  
                if (f>5) {$("#ajax_message").html("El valor que introdujo en los segundos no corresponde");$("#ajax_message").fadeIn("slow");return} 
                if (c!=':' || e!=':') {$("#ajax_message").html("Introduzca el caracter ':' para separar la hora, los minutos y los segundos");$("#ajax_message").fadeIn("slow");return} 
                
                return true;
            }  
            
            
            function checks() {
                
                var cDomingo = $("#domingo").attr("checked");
                var cLunes = $("#lunes").attr("checked");
                var cMartes = $("#martes").attr("checked");
                var cMiercoles = $("#miercoles").attr("checked");
                var cJueves = $("#jueves").attr("checked");
                var cViernes = $("#viernes").attr("checked");
                var cSabado = $("#sabado").attr("checked");
                
                
                if(!cDomingo){ $("#domingo").val(""); }else{ $("#domingo2").val("0"); }  
                if(!cLunes){ $("#lunes").val(""); }else{ $("#lunes").val("1"); }
                if(!cMartes){ $("#martes").val(""); }else{ $("#martes").val("2"); }
                if(!cMiercoles){$("#miercoles").val("");}else{ $("#miercoles").val("3");}
                if(!cJueves){ $("#jueves").val(""); }else{ $("#jueves").val("4"); } 
                if(!cViernes){ $("#viernes").val("");}else{ $("#viernes").val("5");}
                if(!cSabado){ $("#sabado").val(""); }else{  $("#sabado").val("6");  }                
                    
                   
            }
            
            
            function checkEstatus() {
                
                var estatus = $("#estatus").attr("checked");
                
                
                
                if(estatus==false){
                    $('#estatus').val("0");
                }else{
                    $('#estatus').val("1");
                }  
                
                    
            }
            
            
            
            
        </script>
    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Administración</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/inventario_16.png" alt="icon"/>
                                    Inventario Inicial Automatico
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">                                    
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    <p>
                                        <label>Días de registro automatico:</label><br/><br/>
                                        <input type="checkbox" class="checkbox" <%=empresaConfigDto!=null?(domingo.equals("0")?"checked":""):"" %> id="domingo" name="domingo" value="0" > <label for="domingo">Domingo</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" <%=empresaConfigDto!=null?(lunes.equals("1")?"checked":""):"" %> id="lunes" name="lunes" value="1"> <label for="lunes">Lunes</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" <%=empresaConfigDto!=null?(martes.equals("2")?"checked":""):"" %> id="martes" name="martes" value="2" > <label for="martes">Martes</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" <%=empresaConfigDto!=null?(miercoles.equals("3")?"checked":""):"" %> id="miercoles" name="miercoles" value="3" > <label for="miercoles">Miércoles</label>
                                        &nbsp;&nbsp;&nbsp;
                                    </p>
                                    <p>
                                        <input type="checkbox" class="checkbox" <%=empresaConfigDto!=null?(jueves.equals("4")?"checked":""):"" %> id="jueves" name="jueves" value="4" > <label for="jueves">Jueves</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" <%=empresaConfigDto!=null?(viernes.equals("5")?"checked":""):"" %> id="viernes" name="viernes" value="5" > <label for="viernes">Viernes</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" <%=empresaConfigDto!=null?(sabado.equals("6")?"checked":""):"" %> id="sabado" name="sabado" value="6" > <label for="sabado">Sábado</label>
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="checkbox" class="checkbox" id="diarioReporte" name="diarioReporte" onclick="todosDiasMarcados();"> <label for="diarioReporte">Marcar Todos</label>
                                    </p>     
                                    <br/>    
                                    <p>
                                        <label>Hora:  (HH:MM:SS)</label><br/>
                                        <input type="text" id="hora" name="hora" 
                                               value="<%=empresaConfigDto!=null?""+empresaConfigDto.getHoraCorte():"" %>"/> Hrs.
                                    </p>
                                    <br/>
                                    <p>
                                    <label>Activo:</label>
                                        <input type="checkbox" name="estatus" id="estatus" value="1" onchange="checkEstatus();"
                                               class="checkbox" <%=empresaConfigDto!=null?(empresaConfigDto.getInventarioInicialAuto()==1?"checked='true'":""):"checked='true'" %> >
                                    </p>
                                    <br/><br/>

                                    <div id="action_buttons">
                                        <p>
                                            <input type="button" id="enviar" value="Guardar" onclick="grabar();"/>
                                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                        </p>
                                    </div>
                                    
                            </div>
                        </div>
                        <!-- End left column window -->
                        
                        
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