<%-- 
    Document   : catCodigoBarras_form
    Created on : 13/07/2015, 12:29:06 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.util.GenericMethods"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProveedor"%>
<%@page import="com.tsp.sct.bo.SGProveedorBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensClienteVendedor"%>
<%@page import="com.tsp.sct.bo.SGClienteVendedorBO"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.bo.PasswordBO"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.jdbc.ClienteDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>

<%
//Verifica si el cliente tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
        
        class DataCodigoBarras{
            String codigoBarras=""; //colocar datos para Codigo de barras aqui
            String dataExtra1="";//opcional: colocar etiqueta con informacion adicional (legible por humanos)
            String dataExtra2="";//opcional: colocar etiqueta con informacion adicional (legible por humanos)
        }
        
        int paginaActual = 1;
        try{
            paginaActual = Integer.parseInt(request.getParameter("pagina"));
        }catch(Exception e){}

        int idEmpresa = user.getUser().getIdEmpresa();
        
        /*
         * Parámetros
         */
                
        //tipoCodigo :   "bar","qr"   . Por defecto "qr"
        String tipoCodigo = request.getParameter("tipoCodigo")!=null?new String(request.getParameter("tipoCodigo").getBytes("ISO-8859-1"),"UTF-8"):"qr";
        
        int idCliente = 0;
        try {
            idCliente = Integer.parseInt(request.getParameter("idCliente"));
        } catch (NumberFormatException e) {
        }
        int idProveedor = 0;
        try {
            idProveedor = Integer.parseInt(request.getParameter("idProveedor"));
        } catch (NumberFormatException e) {
        }
        int idConcepto = 0;
        try {
            idConcepto = Integer.parseInt(request.getParameter("idConcepto"));
        } catch (NumberFormatException e) {
        }
        String idsProveedores = request.getParameter("idsProveedores")!=null?new String(request.getParameter("idsProveedores").getBytes("ISO-8859-1"),"UTF-8"):"";
        String idsClientes = request.getParameter("idsClientes")!=null?new String(request.getParameter("idsClientes").getBytes("ISO-8859-1"),"UTF-8"):"";
        String idsConceptos = request.getParameter("idsConceptos")!=null?new String(request.getParameter("idsConceptos").getBytes("ISO-8859-1"),"UTF-8"):"";
        

        /*
         *   0/"" = nuevo
         *   1 = editar/consultar
         *   2 = eliminar  
         */
        String mode = request.getParameter("acc") != null ? request.getParameter("acc") : "";
        
        String codigoGenerar = "";
        String tamanoCodigo = "85";
        List<DataCodigoBarras> listDataCodigoBarras = new ArrayList<DataCodigoBarras>();
        
        ClienteBO clienteBO = new ClienteBO(user.getConn());
        SGProveedorBO proveedorBO = new SGProveedorBO(user.getConn());
        ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
        
        //Cliente clientesDto = null;
        
        
        List<Cliente> clientesDto = new ArrayList<Cliente>();
        if (idCliente > 0){
            clienteBO = new ClienteBO(idCliente,user.getConn());
            clientesDto.add(clienteBO.getCliente());
            //codigoGenerar = clientesDto.getIdCliente()+"-"+clientesDto.getIdEmpresa();
        }
        
        if (StringManage.getValidString(idsClientes).length()>0){
            String[] arrayData = new String[0];
            try{
                arrayData = idsClientes.split(",");
            }catch(Exception ex){}
            if (arrayData.length>0){
                for (String strIdCliente : arrayData){
                    try{
                        if (StringManage.getValidString(strIdCliente).length()>0){
                            int idClienteInt = Integer.parseInt(strIdCliente);
                            clientesDto.add(new ClienteBO(idClienteInt, user.getConn()).getCliente());
                        }
                     }catch(Exception ex){ ex.printStackTrace(); }   
                }
            }
        }
        
        if (clientesDto.size()>0){
            for (Cliente cliente : clientesDto){
                if (cliente!=null){
                    DataCodigoBarras dcb = new DataCodigoBarras();
                    //[IDCliente]-[IDEmpresa]
                    dcb.codigoBarras =  cliente.getIdCliente()+"-"+cliente.getIdEmpresa();
                    dcb.dataExtra1 = (cliente.getRazonSocial()!=null?(!cliente.getRazonSocial().trim().equals("")?cliente.getRazonSocial():(cliente.getNombreCliente() + " " + cliente.getApellidoPaternoCliente() + " " + cliente.getApellidoMaternoCliente())  ): (cliente.getNombreCliente() + " " + cliente.getApellidoPaternoCliente() + " " + cliente.getApellidoMaternoCliente()) ) ;
                    listDataCodigoBarras.add(dcb); 
                }
            }
        }
        
        
        List<SgfensProveedor> proveedoresDto = new ArrayList<SgfensProveedor>();
        if (idProveedor > 0){
            //un solo proveedor
            proveedoresDto.add(new SGProveedorBO(idProveedor, user.getConn()).getSgfensProveedor());
        }
        
        if (StringManage.getValidString(idsProveedores).length()>0){
            String[] arrayData = new String[0];
            try{
                arrayData = idsProveedores.split(",");
            }catch(Exception ex){}
            if (arrayData.length>0){
                for (String strIdProveedor : arrayData){
                    try{
                        if (StringManage.getValidString(strIdProveedor).length()>0){
                            int idProveedorInt = Integer.parseInt(strIdProveedor);
                            proveedoresDto.add(new SGProveedorBO(idProveedorInt, user.getConn()).getSgfensProveedor());
                        }
                     }catch(Exception ex){ ex.printStackTrace(); }   
                }
            }
        }
        
        if (proveedoresDto.size()>0){
            for (SgfensProveedor proveedor : proveedoresDto){
                if (proveedor!=null){
                    DataCodigoBarras dcb = new DataCodigoBarras();
                    //[IDProveedor]-[IDEmpresa]
                    dcb.codigoBarras =  proveedor.getIdProveedor()+"-"+proveedor.getIdEmpresa();
                    dcb.dataExtra1 = proveedor.getRazonSocial();
                    listDataCodigoBarras.add(dcb); 
                }
            }
        }
        
        List<Concepto> conceptosDto = new ArrayList<Concepto>();
        if (idConcepto > 0){
            //un solo concepto
            conceptosDto.add(new ConceptoBO(idConcepto, user.getConn()).getConcepto());
        }
        
        if (StringManage.getValidString(idsConceptos).length()>0){
            String[] arrayData = new String[0];
            try{
                arrayData = idsConceptos.split(",");
            }catch(Exception ex){}
            if (arrayData.length>0){
                for (String strIdConcepto : arrayData){
                    try{
                        if (StringManage.getValidString(strIdConcepto).length()>0){
                            int idConceptoInt = Integer.parseInt(strIdConcepto);
                            conceptosDto.add(new ConceptoBO(idConceptoInt, user.getConn()).getConcepto());
                        }
                     }catch(Exception ex){ ex.printStackTrace(); }   
                }
            }
        }
        
        if (conceptosDto.size()>0){
            for (Concepto concepto : conceptosDto){
                //Para conceptos, no solo debe ser diferente de null, si no que debe tener el campo
                //  Identificación asignado, si no, no se genera su código de barras
                if (concepto!=null && StringManage.getValidString(concepto.getIdentificacion()).length()>0){
                    DataCodigoBarras dcb = new DataCodigoBarras();
                    dcb.codigoBarras =  concepto.getIdentificacion();
                    dcb.dataExtra1 = concepto.getNombreDesencriptado();
                    listDataCodigoBarras.add(dcb); 
                }
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
                       
        </script>
                                        
        <script language="Javascript">
            function imprSelec(nombre)
            {
                var ficha = document.getElementById(nombre);
                var ventimp = window.open(' ', 'popimpr');
                ventimp.document.write(ficha.innerHTML);
                ventimp.document.close();
                
                /*var css = ventimp.document.createElement("link");
                css.setAttribute("href", "../../css/tipsy.css");
                css.setAttribute("rel", "stylesheet");
                css.setAttribute("type", "text/css");
                css.setAttribute("media", "all");
                ventimp.document.head.appendChild(css);*/                
                                
                ventimp.print( );                               
                ventimp.close();
                
                //$("#impresion").printArea();
                
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
                    <h1>Códigos de Barras</h1>

                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <!--TODO EL CONTENIDO VA AQUÍ-->
                    <form action="" method="post" id="frm_action">
                    <div class="twocolumn">
                        
                        <!-- End left column window -->
                        <div class="column_left">
                            <div class="header">
                                <span>
                                    <img src="../../images/icon_barras.png" alt="icon"/>
                                    <% if(clientesDto!=null && clientesDto.size()>0){%>
                                    Código de Barras de Clientes
                                    <%}else if(proveedoresDto!=null && proveedoresDto.size()>0){%>
                                    Código de Barras de Proveedores
                                    <%}else if(conceptosDto!=null && conceptosDto.size()>0){%>
                                    Código de Barras de Conceptos
                                    <%}else{%>
                                    Código de Barras
                                    <%}%>
                                </span>
                            </div>
                            <br class="clear"/>
                            <br>
                            <a href="javascript:imprSelec('impresion')" >Imprimir Código(s) de Barra&nbsp;<img src="../../images/printer.png"/></a>
                            <br><br>
                            <div id="impresion" style="border: 1px solid #24748c;" >
                                <table>
                                    <br/>
                                    <!--<td style="padding:10px; text-align:center; font-size:15px; font-family:Arial,Helvetica;">
                                      <a href="http://www.tec-it.com" title="Programa generador de código de barras de TEC-IT">
                                        <img src="http://www.tec-it.com/pics/banner/web/TEC-IT_Banner_120x42.gif" alt="Programa generador de código de barras de TEC-IT" border="0" />
                                      </a>
                                      <br/>
                                      <a href="http://www.tec-it.com" title="Programa generador de códigos de barras de TEC-IT">Programa de código de barras</a>
                                    </td>-->
                                    <% if (!codigoGenerar.equals("")) {%>
                                    <tr>
                                      <td>
                                        <% if (tipoCodigo.equals("bar")){ %>
                                        <!-- Code 128 -->
                                        <img src="http://barcode.tec-it.com/barcode.ashx?code=Code128&modulewidth=fit&data=<%=codigoGenerar%>&dpi=<%=tamanoCodigo%>&imagetype=gif&rotation=0&color=&bgcolor=&fontcolor=&quiet=0&qunit=mm" alt="Código de Barras 128"/>
                                        <% }else{  %>
                                        <!-- QR -->
                                        <img src='http://barcode.tec-it.com/barcode.ashx?data=<%=codigoGenerar%>&code=QRCode&unit=Fit&dpi=<%=tamanoCodigo%>&imagetype=Gif&rotation=0&color=000000&bgcolor=FFFFFF&qunit=Mm&quiet=0&eclevel=L' alt='Código QR'/>
                                        <% } %>
                                      </td>
                                    </tr>
                                    <%
                                    }else if (listDataCodigoBarras.size()>0){
                                        for (DataCodigoBarras dcb : listDataCodigoBarras){
                                    %>
                                    <tr>
                                        <td style="padding: 10px;">
                                            <% if (tipoCodigo.equals("bar")){ %>
                                            <!-- Code 128 -->
                                            <img src="http://barcode.tec-it.com/barcode.ashx?code=Code128&modulewidth=fit&data=<%= dcb.codigoBarras %>&dpi=<%=tamanoCodigo%>&imagetype=gif&rotation=0&color=&bgcolor=&fontcolor=&quiet=0&qunit=mm" alt="Código de Barra 128"/>
                                            <% }else{  %>
                                            <!-- QR -->
                                            <img src='http://barcode.tec-it.com/barcode.ashx?data=<%= dcb.codigoBarras %>&code=QRCode&unit=Fit&dpi=<%=tamanoCodigo%>&imagetype=Gif&rotation=0&color=000000&bgcolor=FFFFFF&qunit=Mm&quiet=0&eclevel=L' alt='Código QR'/>
                                            <% } %>
                                            &nbsp;&nbsp; 
                                        </td>
                                        <td style="height: 100%; vertical-align: middle;">
                                            <b><%= dcb.dataExtra1 %></b> <i><%= dcb.dataExtra2 %></i>
                                        </td>
                                    </tr>
                                    <% 
                                        }
                                    } 
                                    %>                                
                                </table> 
                                </br>
                            </div>
                        </div>
                        
                        
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