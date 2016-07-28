<%-- 
    Document   : catComodatosInformacion_form
    Created on : 9/03/2016, 02:04:47 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.bo.ComodatoMantenimientoBO"%>
<%@page import="com.tsp.sct.dao.dto.ComodatoMantenimiento"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.dao.jdbc.ClienteDaoImpl"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.ConceptoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.dao.jdbc.ComodatoProductoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.ComodatoProducto"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.bo.AlmacenBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensClienteVendedor"%>
<%@page import="com.tsp.sct.bo.SGClienteVendedorBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.ComodatoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.ComodatoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Comodato"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el cliente tiene acceso a este topico
    if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
        response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
        response.flushBuffer();
    } else {
        
        
        int paginaActual = 1;
        try{
            paginaActual = Integer.parseInt(request.getParameter("pagina"));
        }catch(Exception e){}

        int idEmpresa = user.getUser().getIdEmpresa();
        
        /*
         * Parámetros
         */
        int idComodato = 0;
        try {
            idComodato = Integer.parseInt(request.getParameter("idComodato"));
        } catch (NumberFormatException e) {
        }

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        String newRandomPass = "";
        
        ComodatoBO comodatoBO = new ComodatoBO(user.getConn());
        Comodato comodatosDto = null;
        Empresa empresaDto = null;
        if (idComodato > 0){
            comodatoBO = new ComodatoBO(idComodato,user.getConn());
            comodatosDto = comodatoBO.getComodato();
            empresaDto = new EmpresaBO(comodatosDto.getIdEmpresa(),user.getConn()).getEmpresa();
        }
        
        Configuration appConfig = new Configuration();
        
    //obtenemos los productos relacionados:
    ComodatoProducto[] comodatoProductos = null;
    String productosRelacionados = "";
    try{
        comodatoProductos = new ComodatoProductoDaoImpl(user.getConn()).findWhereIdComodatoEquals(idComodato);
        Concepto concepto = null;
        ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
        ConceptoDaoImpl conceptoDaoImpl = new ConceptoDaoImpl(user.getConn());
        for(ComodatoProducto cp : comodatoProductos){
            concepto = conceptoDaoImpl.findByPrimaryKey(cp.getIdConcepto());
            productosRelacionados += conceptoBO.desencripta(concepto.getNombre()) + ", ";
        }
    }catch(Exception e){}

    //obtenemos el ultimo mantenimiento que se le dio:
    ComodatoMantenimientoBO comodatoMantenimientoBO = new ComodatoMantenimientoBO(user.getConn());
    ComodatoMantenimiento comodatoMantenimientosDto = null;
     try{
         comodatoMantenimientosDto = comodatoMantenimientoBO.findComodatoMantenimientos(0, idComodato, idEmpresa , 0, 0, "")[0];
     }catch(Exception e){}

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
                if(validar()){
                    $.ajax({
                        type: "POST",
                        url: "catComodatos_ajax.jsp",
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
                                            location.href = "catComodatos_list.jsp?pagina="+"<%=paginaActual%>";
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
                }
            }

            function validar(){
                /*
                if(jQuery.trim($("#nombre").val())==""){
                    apprise('<center><img src=../../images/warning.png> <br/>El dato "nombre de contacto" es requerido</center>',{'animate':true});
                    $("#nombre_contacto").focus();
                    return false;
                }
                */
                return true;
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
                    <h1>Comodatos</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_comodato.png" alt="icon"/>
                                    <% if(comodatosDto!=null){%>
                                    Comodato ID <%=comodatosDto!=null?comodatosDto.getIdComodato():"" %>
                                    <%}else{%>
                                    Comodato
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idComodato" name="idComodato" value="<%=comodatosDto!=null?comodatosDto.getIdComodato():"" %>" />
                                    <input type="hidden" id="mode" name="mode" value="<%=mode%>" />
                                    
                                    <label>Nombre:</label>
                                        <h2><i><%=comodatosDto!=null?comodatoBO.getComodato().getNombre():"" %></i></h2>
                                    
                                    <label>Descripción:</label>
                                        <h2><i><%=comodatosDto!=null?comodatoBO.getComodato().getDescripcion():"" %></i></h2>
                                        
                                    <label>Número de Serie:</label>
                                        <h2><i><%=comodatosDto!=null?comodatoBO.getComodato().getNumeroSerie():"" %></i></h2>
                                    
                                    <label>Marca:</label>
                                        <h2><i><%=comodatosDto!=null?comodatoBO.getComodato().getMarca():"" %></i></h2>
                                        
                                    <label>Modelo:</label>
                                        <h2><i><%=comodatosDto!=null?comodatoBO.getComodato().getModelo():"" %></i></h2>
                                                                        
                                    <label>Producto relacionado:</label>
                                        <h2><i><%=comodatosDto!=null?productosRelacionados:"" %></i></h2>
                                        
                                    <label>Tipo:</label>
                                        <h2><i><%=comodatosDto!=null?comodatoBO.getComodato().getTipo():"" %></i></h2>
                                        
                                    <label>Capacidad:</label>
                                        <h2><i><%=comodatosDto!=null?comodatoBO.getComodato().getCapacidad():"" %></i></h2>
                                        
                                    <label>Estado:</label>
                                        <h2><i><%=comodatosDto!=null?(comodatoBO.getComodato().getEstado()==1?"Nuevo":comodatoBO.getComodato().getEstado()==2?"Re-Uso":""):"" %></i></h2>
                                        
                                    <label>Estatus:</label>
                                        <h2><i><%=comodatosDto!=null?(comodatoBO.getComodato().getEstatus()==1?"Comodato":comodatoBO.getComodato().getEstatus()==2?"Disponible":comodatoBO.getComodato().getEstatus()==3?"Inactivo":comodatoBO.getComodato().getEstatus()==4?"Descompuesto":""):"" %></i></h2>
                                        
                                    <%if(comodatosDto!=null && comodatosDto.getEstatus()==1){
                                        Cliente cli = null;                                    
                                        try{
                                            cli = new ClienteDaoImpl(user.getConn()).findByPrimaryKey(comodatosDto.getIdCliente());
                                        }catch(Exception e){}
                                        Empleado vendedor = null;
                                        try{
                                            vendedor = new EmpleadoDaoImpl(user.getConn()).findWhereIdUsuariosEquals(comodatosDto.getIdUsuarioVendedor())[0];
                                        }catch(Exception e){}
                                    
                                    %>
                                        <label>Cliente:</label>
                                            <h2><i><%=cli!=null?cli.getRazonSocial():"" %></i></h2>
                                        
                                        <label>Nombre de quien recibió:</label>
                                            <h2><i><%=comodatosDto!=null?comodatoBO.getComodato().getNombreRecibe():"" %></i></h2>
                                        
                                        <label>Dirección Resguardo:</label>
                                            <h2><i><%=comodatosDto!=null?comodatoBO.getComodato().getDireccionResguardo():"" %></i></h2>
                                        
                                        <label>Vendedor:</label>
                                            <h2><i><%=vendedor!=null?vendedor.getNombre() + " " + vendedor.getApellidoPaterno() + " " + vendedor.getApellidoMaterno():"" %></i></h2>                                        
                                    <%}%>
                                    
                                   <!--
                                    <p>
                                        <label>Almacén de resguardo:</label> <br/>
                                        <select size="1" id="idAlmacenComodato" name="idAlmacenComodato" > 
                                            <option value="0" ></option>
                                            <%
                                                //out.print(new AlmacenBO(user.getConn()).getAlmacenesByIdHTMLCombo(idEmpresa, comodatosDto!=null?(comodatoBO.getComodato().getIdAlmacen()>0?comodatoBO.getComodato().getIdAlmacen():-1):-1,-1,-1,""));
                                            %>
                                        </select>
                                    </p>
                                    <br/>-->
                                                                        
                                    <br/><br/>
                                    
                                    <div id="action_buttons">
                                        <p>
                                            
                                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                                        </p>
                                    </div>
                                    
                            </div>
                        </div>
                        <!-- End left column window -->
                        <div class="column_right">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_comodato.png" alt="icon"/>                                    
                                    
                                </span>
                            </div>
                            <br class="clear"/>
                            <div class="content">
                                    <input type="hidden" id="idImagenPersonal" name="idImagenPersonal" value="" />
                                    <input type="hidden" id="mode" name="mode" value="contrato" />                                    
                                    <input type="hidden" id="idComodato" name="idComodato" value="<%=idComodato%>" />
                                             
                                    <%if(comodatoMantenimientosDto!=null){%>
                                        <h2><i>Mantenimiento:</i></h2>                                            
                                        
                                        <label>Técnico:</label>
                                            <h2><i><%=comodatosDto!=null?comodatoMantenimientosDto.getTecnico():"" %></i></h2>
                                        
                                        <label>Descripción:</label>
                                            <h2><i><%=comodatosDto!=null?comodatoMantenimientosDto.getDescripcion():"" %></i></h2>
                                        
                                        <label>Estatus:</label>
                                        <h2><i><%=comodatosDto!=null?(comodatoMantenimientosDto.getEstatus()==1?"Mantenimiento Exitoso":comodatoMantenimientosDto.getEstatus()==2?"Descompuesto":comodatoMantenimientosDto.getEstatus()==3?"Pendiente":""):"" %></i></h2>
                                            
                                        <label>Nombre de la Persona que Atendió:</label>
                                            <h2><i><%=comodatosDto!=null?comodatoMantenimientosDto.getNombreAtendio():"" %></i></h2>
                                            
                                        <label>Fecha del Mantenimiento:</label>
                                            <h2><i><%=comodatoMantenimientosDto!=null?(comodatoMantenimientosDto.getFechaRealizacionMantenimiento()!=null?(!comodatoMantenimientosDto.getFechaRealizacionMantenimiento().toString().trim().equals("")?DateManage.formatDateToNormal(comodatoMantenimientosDto.getFechaRealizacionMantenimiento()):""):""):""%></i></h2>
                                        
                                        <label>Fecha Próximo Mantenimiento:</label>
                                            <h2><i><%=comodatoMantenimientosDto!=null?(comodatoMantenimientosDto.getFechaProxMantenimiento()!=null?(!comodatoMantenimientosDto.getFechaProxMantenimiento().toString().trim().equals("")?DateManage.formatDateToNormal(comodatoMantenimientosDto.getFechaProxMantenimiento()):""):""):""%></i></h2>
                                            
                                        <label>Costo:</label>
                                            <h2><i><%=comodatosDto!=null?comodatoMantenimientosDto.getCosto():"" %></i></h2>
                                                                            
                                    <%}%>
                                    
                                    <label>Nombre Archivo:</label>
                                            <h2><i><%=comodatosDto!=null?(comodatoBO.getComodato().getContratoNombreArchivo()!=null?comodatoBO.getComodato().getContratoNombreArchivo():""):"" %></i></h2>
                                    
                                    
                                    <% if (comodatosDto!=null && empresaDto!=null && comodatosDto.getContratoNombreArchivo()!=null && !comodatosDto.getContratoNombreArchivo().trim().equals("") ) {
                                        String rutaArchivoImagenPersonal = appConfig.getApp_content_path() + empresaDto.getRfc() + "/ContratoComodato/" + comodatosDto.getContratoNombreArchivo();
                                        String rutaArchivoImgEncoded = java.net.URLEncoder.encode(rutaArchivoImagenPersonal, "UTF-8");
                                    %>
                                    <p>
                                        <label>Archivo</label>
                                        <div style="display: inline" >
                                            <a href="../file/download.jsp?ruta_archivo=<%=rutaArchivoImgEncoded%>">Descargar</a>
                                        </div>
                                    </p>
                                    <%}%>
                                                                       
                            </div>
                        </div>
                        <!-- End left column window -->
                        
                        
                    </div>
                        
                    </div>
                    </form>
                    <!--TODO EL CONTENIDO VA AQUÍ-->

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>

        <script>
            $("select.flexselect").flexselect();
        </script>
    </body>
</html>
<%}%>
