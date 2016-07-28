<%-- 
    Document   : leftContent
    Created on : 19-oct-2012, 19:48:19
    Author     : ISCesarMartinez poseidon24@hotmail.com
--%>

<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.PretoCaracteristicasConsolaBO"%>
<%@page import="com.tsp.sct.dao.dto.PretoCaracteristicasConsola"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    EmpresaBO empresaBO = new EmpresaBO(user.getConn());
    EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());     
    PretoCaracteristicasConsola consola = new PretoCaracteristicasConsola();
    PretoCaracteristicasConsolaBO caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO(user.getConn());
%>
<!-- Begin left panel -->
<a href="javascript:;" id="show_menu">&raquo;</a>
    <script type="text/javascript">
        function validaRegistroPatronal(){
            <%
            Empresa empresa = new EmpresaBO(user.getUser().getIdEmpresa(), user.getConn()).getEmpresa();
            if(empresa.getRegistroPatronal().trim().equals("")){
            %>
                apprise('<center><img src=../../images/warning.png> <br/>De de alta su registro patronal</center>',{'animate':true});
            <%}%>
        }
    </script>
<div id="left_menu">
        <a href="javascript:;" id="hide_menu">&laquo;</a>
        <ul id="main_menu">
                <% if (user.isPermisoVerMenu()){ %>
                <li><a href="../../jsp/inicio/main.jsp"><img src="../../images/icon_home.png" alt="Inicio"/>Inicio</a></li>
                <% if (user.getUser().getIdRoles() == RolesBO.ROL_DESARROLLO 
                                || user.getUser().getIdRoles() == RolesBO.ROL_ADMINISTRADOR
                                || user.getUser().getIdRoles() == RolesBO.ROL_ADMINISTRADOR_DE_SUCURSAL) { %>
                <li>
                        <a href=""><img src="../../images/icon_admin.png" alt="Administración"/>Administración</a>
                        <ul>
                            <%caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("CERTIFICADOS PARA CFDI",user.getConn());
                            consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                            if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp())) || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){%>
                                    <li><a href="../../jsp/user/perfil.jsp"><img src="../../images/user_male_16.png"/>Mi Perfil</a></li>
                                    <li><a href="../../jsp/catCertificados/catCertificados_list.jsp"><img src="../../images/icon_certificados.png"/>Certificados</a></li>                                    
                            <%}}%>
                                <%caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("LOGO",user.getConn());
                                consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                                if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                    if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp())) || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){%>
                                        <li><a href="../../jsp/catImagenPersonal/catImagenPersonal_list.jsp"><img src="../../images/icon_imagenPersonal.png"/>Logo</a></li>
                                        <li><a href="../../jsp/catLicencias/catLicencias_list.jsp"><img src="../../images/license.png"/>Licencias</a></li>
                                        
                                <%}}%>
                                <li><a href="../../jsp/catCreditosOperacion/catCreditosOperacion_list.jsp"><img src="../../images/icon_miCuenta.png"/>Mi Cuenta</a></li>
                                <li><a href="../../jsp/catUsuarios/catUsuarios_list.jsp"><img src="../../images/icon_users.png"/>Usuarios</a></li>
                             <% if (user.getUser().getIdRoles() != RolesBO.ROL_ADMINISTRADOR_DE_SUCURSAL ) {
                                 if(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa() == user.getUser().getIdEmpresa()){%>
                                <li><a href="../../jsp/catSucursales/catSucursales_list.jsp"><img src="../../images/icon_sucursales.png"/>Sucursales</a></li>
                             <% }} %>
                                <li><a href="../../jsp/catFolios/catFoliosControl_list.jsp"><img src="../../images/icon_folio.png"/>Folios Control Interno</a></li>
                            <%caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("ALMACENES",user.getConn());
                            consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                            if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp())) || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){%>                                    
                                <!-- <li><a href="../../jsp/catReportesConfigurables/catReportesConfigurables_list.jsp"><img src="../../images/icon_reporte.png"/>Reportes Configurables</a></li> -->
                            <%}}%>
                            <%if(empresaPermisoAplicacionDto.getAccesoSgfensPretomovil()==1){%>
                            <!--<li><a href="../../jsp/catConfigEmpresa/catConfigEmpresa_CorteInventarioEmpAuto.jsp"><img src="../../images/inventario_16.png"/>Inventario Inicial Automatico</a></li>-->
                            <% } %>
                            
                            <% if( user.getUser().getIdRoles() == RolesBO.ROL_DESARROLLO ) { %>                                 
                                <li><a href="../../jsp/catHistorialMovil/catHistorialMovil_list.jsp"><img src="../../images/log_16.png"/>Log Móviles</a></li>
                            <% } %>
                            
                        </ul>	
                </li>
                <%}%>
                <li>
                        <a id="menu_pages" href=""><img src="../../images/icon_pages.png" alt="Catálogos"/>Catálogos</a>
                        <ul>
                                <li><a href="../../jsp/catClientes/catClientes_list.jsp"><img src="../../images/icon_cliente.png"/>Clientes</a></li>
                                <%caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("IMPUESTOS",user.getConn());
                                consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                                if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                    if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp())) || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){%>
                                        <li><a href="../../jsp/catImpuestos/catImpuestos_list.jsp"><img src="../../images/icon_impuesto.png"/>Impuestos</a></li>
                                <%}}%>
                                <li><a href="../../jsp/catProveedores/catProveedores_list.jsp"><img src="../../images/icon_proveedor.png"/>Proveedores</a></li>
                                <!--<li><a href="../../jsp/catFolios/catFoliosCBB_list.jsp"><img src="../../images/icon_info.png"/>Folios CBB</a></li>
                                <li><a href="../../jsp/catQrCBB/catQrCBB_list.jsp"><img src="../../images/icon_info.png"/>QR para CBB</a></li>-->
                                <li><a href="../../jsp/catProspectos/catProspectos_list.jsp"><img src="../../images/icon_prospecto.png"/>Prospectos</a></li>
                                <%caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("COBRANZA",user.getConn());
                                consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                                if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                    if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){%>
                                    <li><a href="../../jsp/catBancos/catBanco_list.jsp"><img src="../../images/icon_banco.png"/>Bancos</a></li>                                    
                                <%}}%>
                        </ul>
                </li>
                <li>
                        <a href=""><img src="../../images/icon_almacen1.png" alt="Almacén"/>Almacén</a>
                        <ul>
                            <%caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("ALMACENES",user.getConn());
                                consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                                if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                    if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp())) || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){%>
                                        <li><a href="../../jsp/catAlmacenes/catAlmacenes_list.jsp"><img src="../../images/icon_almacen.png"/>Almacenes</a></li>
                                <%}}%>
                                <%caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("CATEGORIAS",user.getConn());
                                consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                                if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                    if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp())) || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){%>
                                        <li><a href="../../jsp/catCategorias/catCategorias_list.jsp"><img src="../../images/icon_categoria.png"/>Categorías</a></li>
                                <%}}%>
                                <%caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("EMBALAJES",user.getConn());
                                consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                                if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                    if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){%>
                                        <li><a href="../../jsp/catEmbalajes/catEmbalajes_list.jsp"><img src="../../images/icon_embalaje.png"/>Embalaje</a></li>
                                <%}}%>
                                <%caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("MARCAS",user.getConn());
                                consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                                if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                    if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){%>
                                        <li><a href="../../jsp/catMarcas/catMarcas_list.jsp"><img src="../../images/icon_marca.png"/>Marcas</a></li>
                                <%}}%>
                                <%caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("MOVIMIENTOS",user.getConn());
                                consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                                if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                    if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){%>
                                        <li><a href="../../jsp/catMovimientos/catMovimientos_list.jsp"><img src="../../images/icon_movimiento.png"/>Movimientos</a></li>
                                <%}}%>
                                <li><a href="../../jsp/catConceptos/catConceptos_list.jsp"><img src="../../images/icon_producto.png"/>Productos</a></li>
                                <li><a href="../../jsp/catPaquetes/catPaquetes_list.jsp"><img src="../../images/icon_paquetes.png"/>Paquetes</a></li>
                                <li><a href="../../jsp/catServicios/catServicios_list.jsp"><img src="../../images/icon_servicio.png"/>Servicios</a></li>                                
                                
                                <%caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("ALMACENES",user.getConn());
                                consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                                if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                    if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp())) || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){%>
                                    <li><a href="../../jsp/catConceptos/catConceptos_Inventario_list.jsp"><img src="../../images/icon_inventario.png"/>Inventario</a></li>
                                    <!--<li><a href="../../jsp/catReportesConfigurables/catReportesConfigurables_list.jsp"><img src="../../images/icon_reporte.png"/>Reportes Configurables</a></li>-->
                                <%}}%>
                                <% 
                                if (empresaPermisoAplicacionDto!=null){
                                    if(empresaPermisoAplicacionDto.getAccesoSgfensPretomovil()==1){ %>
                                        <li><a href="../../jsp/catConceptosOferta/catConceptos_listCnDscnto.jsp" title="Productos que tienen descuento, solo es reflejado en los dispositivos móviles"><img src="../../images/icon_producto.png"/>Productos con Descuento para Moviles</a></li>                                
                                <%
                                    }
                                }%>        
                        </ul>
                </li>
                <li>
                        <a href=""><img src="../../images/icon_ventas.png" alt="Ventas"/>Ventas</a>
                        <ul>
                                <%caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("PEDIDOS",user.getConn());
                                consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                                if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                    if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){%>                                
                                        <li><a href="../../jsp/pedido/pedido_list.jsp"><img src="../../images/icon_ventas3.png"/>Pedidos</a></li>                                        
                                <%}}%>
                                <%caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("COBRANZA",user.getConn());
                                consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                                if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                    if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){%>
                                        <li><a href="../../jsp/catCobranzaAbono/catCobranzaAbono_list.jsp"><img src="../../images/icon_ventas1.png"/>Cobranza</a></li>
                                        <li><a href="../../jsp/catConceptos/catConceptosVendidos_list.jsp"><img src="../../images/icon_producto.png"/>Productos Vendidos</a></li>
                                <%}}%>
                                <li><a href="../../jsp/catDevolucionesCambios/catDevolucionesCambios_list.jsp"><img src="../../images/icon_devCam.png"/>Gestión Devoluciones y Cambios</a></li>
                                <%if (empresaPermisoAplicacionDto!=null){
                                    if(empresaPermisoAplicacionDto.getAccesoSgfensPretomovil()==1){ %>
                                         <li><a href="../../jsp/catClientesVisitas/catClientesVisitas_list.jsp"><img src="../../images/icon_mapa.png"/>Visita Cliente</a></li>
                                <%}}%>
                                <%caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("COTIZACIONES",user.getConn());
                                consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                                if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                    if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){%>
                                        <li><a href="../../jsp/cotizacion/cotizacion_list.jsp"><img src="../../images/icon_cotizacion.png"/>Cotizaciones</a></li>
                                <%}}%>
                                <%caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("PEDIDOS",user.getConn());
                                consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                                if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                    if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){%>                                
                                        <li><a href="../../jsp/catCobranzaAbono/catCobranzaCorteCaja.jsp"><img src="../../images/corte_caja_icon.png"/>Corte de Caja</a></li>                                        
                                        <li><a href="../../jsp/catArqueoCaja/catArqueoCaja_list.jsp"><img src="../../images/coins_icon_16.png"/>Arqueo de Caja</a></li>
                                <%}}%>                                
                                
                                
                                <%caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("COBRANZA",user.getConn());
                                consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                                if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                    if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){%>                                    
                                    <li><a href="../../jsp/catBancos/catDepositosBancarios_list.jsp"><img src="../../images/icon_deposito.png"/>Depósitos Bancarios</a></li>
                                <%}}%>
                                <!--<li><a href=""><img src="../../images/icon_precio.png"/>Tipos de Precio</a></li>-->
                        </ul>	
                </li>
                <%/*caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("FACTURACION CFDI",user.getConn());
                                consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                                if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                    if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){ */%>                                
               <%if (empresaPermisoAplicacionDto!=null){
                    if(empresaPermisoAplicacionDto.getAccesoSgfensFacturacion()== 1){ %>                     
                                        <li>
                                            <a href=""><img src="../../images/icon_cfdi_1.png" alt="Facturación"/>Facturación CFDI</a>
                                                <ul>
                                                        <li><a href="../../jsp/cfdi_factura/cfdi_factura_list.jsp"><img src="../../images/icon_cfdi_1.png"/>Facturas</a></li>
                                                        <!--<li><a href=""><img src="../../images/icon_cfdi_2.png"/>Notas Crédito</a></li>
                                                        <li><a href=""><img src="../../images/icon_cfdi_3.png"/>Notas Débito</a></li>-->
                                                        <% if ( empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa() >= 1){ //373 PROAS %>
                                                        <li><a href="../../jsp/cfdi_factura_express_desglose/cfdi_factura_list.jsp"><img src="../../images/icon_cfdi_4.png"/>Express c/Desglose</a></li>
                                                        <% } %>                                                        
                                                        
                                                        <li><a href="../../jsp/cfdi_factura/cfdi_nota_list.jsp?tipoComp=5"><img src="../../images/icon_cfdi_2.png"/>Notas Crédito</a></li>
                                                        <li><a href="../../jsp/cfdi_factura/cfdi_nota_list.jsp?tipoComp=6"><img src="../../images/icon_cfdi_3.png"/>Notas Débito</a></li>
                                                </ul>                                                
                                        </li>
                <%}}%>
                <%if (empresaPermisoAplicacionDto!=null){
                    if(empresaPermisoAplicacionDto.getAccesoModuloSar()== 1){ %>                     
                                        <li>
                                            <a href=""><img src="../../images/icon_sar_modulo.png" alt="Conexión SAR"/>Facturación SAR</a>
                                                <ul>
                                                    <li><a href="../../jsp/cfdi_sar/cfdi_sar_config_user_list.jsp"><img src="../../images/icon_sar_config.png"/>Configuración</a></li>
                                                    <li><a href="../../jsp/cfdi_sar/cfdi_sar_factura_list.jsp"><img src="../../images/icon_cfdi_1.png"/>Facturas SAR</a></li>
                                                </ul>                                                
                                        </li>
                <%}}%>
                <%
                if (empresaPermisoAplicacionDto!=null){
                    if(empresaPermisoAplicacionDto.getAccesoSgfensCbb()== 1){ %>
                <li>
                    <a href=""><img src="../../images/icon_cfdi_2.png" alt="CBB"/>Facturación CBB</a>
                    <ul>
                        <li><a href="../../jsp/cbb_factura/cbb_factura_list.jsp"><img src="../../images/icon_cfdi_2.png"/>Facturas</a></li>
                    </ul>
                </li>
                <%}}%>
                
                <li>
                    <a href=""><img src="../../images/icon_finanzas.png" alt="Conexión SAR"/>Finanzas (CxP/CxC)</a>
                        <ul>
                            <li><a href="../../jsp/cxc/index.jsp"><img src="../../images/icon_dashboard.png"/>Dashboard</a></li>
                            <li><a href="../../jsp/cxc/cxc_comprobantes_list.jsp"><img src="../../images/icon_cxc.png"/>Cuentas por Cobrar</a></li>
                            <li><a href="../../jsp/cxp/cxp_comprobantes_list.jsp"><img src="../../images/icon_cxp.png"/>Cuentas por Pagar</a></li>
                        </ul>                                                
                </li>
                
                <%if (empresaPermisoAplicacionDto!=null){
                    if(empresaPermisoAplicacionDto.getAccesoSgfensCobrotarjeta()== 1){ %> 
                <% /*     if (empresaPermisoAplicacionDto!=null){
                            if(empresaPermisoAplicacionDto.getAccesoSgfensPretoriano()==2 || empresaPermisoAplicacionDto.getAccesoSgfensPretoriano()==3){*/%>
                            <%/*caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("COBRO",user.getConn());
                                    consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                                    if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                        if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){*/%>                                            
                                            <li>
                                                <a href=""><img src="../../images/icon_tarjetas.png" alt="PagoTarjeta"/>Pago con Tarjeta</a>
                                                    <ul>
                                                        <li><a href="../../jsp/catCobroTarjeta/catCobroTarjeta_list.jsp"><img src="../../images/icon_tarjetas.png"/>Pago con tarjeta</a></li>
                                                        <li><a href="../../jsp/catGatewayPagos/catGatewayPagos_list.jsp"><img src="../../images/icon_gateway.png"/>Gateway de Pagos</a></li>
                                                            <!--<li><a href=""><img src="../../images/icon_cfdi_2.png"/>Notas Crédito</a></li>
                                                            <li><a href=""><img src="../../images/icon_cfdi_3.png"/>Notas Débito</a></li>-->
                                                    </ul>	
                                            </li>
                                    <%//}}%>
                <%}}%>
                
                <% 
                        if (empresaPermisoAplicacionDto!=null){
                            //if(empresaPermisoAplicacionDto.getAccesoSgfensPretoriano()==2 || empresaPermisoAplicacionDto.getAccesoSgfensPretoriano()==3){
                            if(empresaPermisoAplicacionDto.getAccesoSgfensPretomovil()==1){
                %>
                        <li>
                            <a href=""><img src="../../images/icon_phone.png" alt="PretorianoMovil"/>Pretoriano Movil</a>                            
                                <ul>
                                    <%caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("DASHBOARD",user.getConn());
                                    consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                                    if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                        if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){%>
                                        <li><a href="../../jsp/ModuloPretorianoMovil/dashboard.jsp"><img src="../../images/company_16.png"/>Localización</a></li>                                    
                                        <li><a href="../../jsp/catDispositivosMoviles/catDispositivosMoviles_list.jsp"><img src="../../images/icon_phone.png"/>Dispositivos Moviles</a></li>
                                        <li><a href="../../jsp/catEmpleados/catEmpleados_list.jsp"><img src="../../images/icon_users.png"/>Empleados</a></li>
                                        <li><a href="../../jsp/catEmpleados/catEmpleado_Clientes_List.jsp"><img src="../../images/cliemp_16.png"/>Clientes Asignados</a></li>
                                        <li><a href="../../jsp/catEmpleados/catEmpleado_Mensajes_form.jsp?acc=mensajeMasivo"><img src="../../images/icon_mensajes.png"/>Mensajes</a></li>
                                        <li><a href="../../jsp/catRegiones/catRegiones_list.jsp"><img src="../../images/zone2_16.png"/>Zonas</a></li>
                                         
                                        
                                    <%}}%>
                                    <%caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("SEGUIMIENTO",user.getConn());
                                    consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                                    if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                        if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){%>                                        
                                        <li><a href="../../jsp/catSeguimiento/catSeguimiento_list.jsp"><img src="../../images/icon_seguimiento.png"/>Seguimiento</a></li>
                                    <%}}%>
                                           
                     <!--           </ul>	
                        </li>
                        
                        <li>
                            <a href=""><img src="../../images/icon_mapa_1.png" alt="Mapas"/>Mapas</a>
                            <ul>    -->
                                <%caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("VISOR DE MAPAS",user.getConn());
                                    consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                                    if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                        if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp())) || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){%>                                    
                                            <li><a href="../../jsp/mapa/mapa_visor.jsp"><img src="../../images/icon_mapa.png"/>Visor de mapa</a></li>
                                    <%}}%>
                                <%caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("CREACION DE RUTAS",user.getConn());
                                    consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                                    if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                        if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp())) || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb()))  ){%>                                
                                            <li><a href="../../jsp/mapa/logistica.jsp"><img src="../../images/icon_logistica.png"/>Log&iacute;stica</a></li>
                                    <%}}%>
                                <%caracteristicasConsolaBO = new PretoCaracteristicasConsolaBO("GEOCERCAS",user.getConn());
                                    consola = caracteristicasConsolaBO.getPretoCaracteristicasConsola();
                                    if(empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP || empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB){
                                        if(( (empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_GRATIS) && "X".equals(consola.getTpvGratis()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_EMPRENDEDOR) && "X".equals(consola.getTpvEmprendedor()))  ||   ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_COMERCIANTE) && "X".equals(consola.getTpvComerciante()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.TPV_MIPYME) && "X".equals(consola.getTpvMipyme()))  ||  ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.EVC) && "X".equals(consola.getEvc()))  || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.PRETORIANO_ERP) && "X".equals(consola.getPretorianoErp())) || ((empresaPermisoAplicacionDto.getIdPretoCaracteristicaProductoAdquirido() == PretoCaracteristicasConsolaBO.CBB) && "X".equals(consola.getCbb())) ){%>                                
                                            <li><a href="../../jsp/catEmpleados/catEmpleados_RutaDia.jsp"><img src="../../images/icon_calendar.png"/>Ruta Empleado</a></li>
                                            <li><a href="../../jsp/catGeocerca/catGeocercas_list.jsp"><img src="../../images/icon_mapa_1.png"/>Geocercas</a></li>                                            
                                    <%}}%>
                                <%if (empresaPermisoAplicacionDto!=null){
                                    if(empresaPermisoAplicacionDto.getAccesoSgfensPretomovil()==1){ %>
                                         <li><a href="../../jsp/catClientesVisitas/catClientesVisitas_list.jsp"><img src="../../images/abrir-icono-6168-16.png"/>Visita Cliente</a></li>
                                <%}}%>
                            </ul>	
                        </li>
                        
                        <!--<li>
                            <a href="" onclick="validaRegistroPatronal();"><img src="../../images/icon_impuesto.png" alt="NOMINA"/>Nómina</a>
                            <ul>
                                <li><a href="../../jsp/catNominas/catRegistroPatronal.jsp"><img src="../../images/icon_cfdi_2.png"/>Registro Patronal</a></li>
                                <li><a href="../../jsp/catNominaDepartamentos/catDepartamentos_list.jsp"><img src="../../images/icon_cfdi_2.png"/>Departamentos</a></li>
                                <li><a href="../../jsp/catNominaPuestos/catPuestos_list.jsp"><img src="../../images/icon_cfdi_2.png"/>Puestos</a></li>
                                <li><a href="../../jsp/catNominaJornadas/catJornadas_list.jsp"><img src="../../images/icon_cfdi_2.png"/>Jornadas</a></li>
                                <li><a href="../../jsp/catNominaContratos/catContratos_list.jsp"><img src="../../images/icon_cfdi_2.png"/>Contratos</a></li>
                                <li><a href="../../jsp/catNominaPeriodicidadPagos/catPeriodoPagos_list.jsp"><img src="../../images/icon_cfdi_2.png"/>Periodicidad Pagos</a></li>
                                <li><a href="../../jsp/catNominaEmpleados/catEmpleados_list.jsp"><img src="../../images/icon_cfdi_2.png"/>Empleados</a></li>
                                <li><a href="../../jsp/catNominaPercepciones/catPercepciones_list.jsp"><img src="../../images/icon_cfdi_2.png"/>Percepciones</a></li>
                                <li><a href="../../jsp/catNominaDeducciones/catDeducciones_list.jsp"><img src="../../images/icon_cfdi_2.png"/>Deducciones</a></li>
                                <li><a href="../../jsp/catNominaCFDI_factura/cfdi_factura_list.jsp"><img src="../../images/icon_cfdi_2.png"/>Nómina CFDI</a></li>
                            </ul>
                        </li>-->
                <%}}%>
                <% 
                   if (empresaPermisoAplicacionDto!=null){ %>
                       <% if (user.getUser().getIdRoles() == RolesBO.ROL_DESARROLLO 
                                || user.getUser().getIdRoles() == RolesBO.ROL_ADMINISTRADOR
                                || user.getUser().getIdRoles() == RolesBO.ROL_ADMINISTRADOR_DE_SUCURSAL) { %>
                       <li>
                            <a href=""><img src="../../images/clipboard_report_bar_16_ns.png" alt="Reportes"/>Reportes</a>                            
                            <ul>
                               <li><a href="../../jsp/reporte/reporte_menu.jsp"><img src="../../images/report_gral.png"/>Reportes Generales</a></li>
                               <li><a href="../../jsp/catReportesConfigurables/catReportesConfigurables_list.jsp"><img src="../../images/icon_reporte.png"/>Reportes Configurables</a></li>                               
                            </ul>                            
                       </li> 
                <% }}%>
                <%if (empresaPermisoAplicacionDto!=null){
                    if(empresaPermisoAplicacionDto.getAccesoSgfensNomina() == 1){%>
                        <li>
                            <a href="" onclick="validaRegistroPatronal();"><img src="../../images/icon_impuesto.png" alt="NOMINA"/>Nómina</a>
                            <ul>
                                <li><a href="../../jsp/catNominas/catRegistroPatronal.jsp"><img src="../../images/icon_registroPatronal.png"/>Registro Patronal</a></li>
                                <li><a href="../../jsp/catNominaDepartamentos/catDepartamentos_list.jsp"><img src="../../images/icon_departamento.png"/>Departamentos</a></li>
                                <li><a href="../../jsp/catNominaPuestos/catPuestos_list.jsp"><img src="../../images/icon_puesto.png"/>Puestos</a></li>
                                <li><a href="../../jsp/catNominaJornadas/catJornadas_list.jsp"><img src="../../images/icon_jornada.png"/>Jornadas</a></li>
                                <li><a href="../../jsp/catNominaContratos/catContratos_list.jsp"><img src="../../images/icon_contrato.png"/>Contratos</a></li>
                                <li><a href="../../jsp/catNominaPeriodicidadPagos/catPeriodoPagos_list.jsp"><img src="../../images/icon_periodicidad.png"/>Periodicidad Pagos</a></li>
                                <li><a href="../../jsp/catNominaEmpleados/catEmpleados_list.jsp"><img src="../../images/icon_empleadoNomina.png"/>Empleados</a></li>
                                <li><a href="../../jsp/catNominaPercepciones/catPercepciones_list.jsp"><img src="../../images/icon_percepcion.png"/>Percepciones</a></li>
                                <li><a href="../../jsp/catNominaDeducciones/catDeducciones_list.jsp"><img src="../../images/icon_deduccion.png"/>Deducciones</a></li>
                                <li><a href="../../jsp/catNominaCFDI_factura/cfdi_factura_list.jsp"><img src="../../images/icon_nominaCFDI.png"/>Nómina CFDI</a></li>
                            </ul>
                        </li>
                <%}}%>
                <%
                if (empresaPermisoAplicacionDto!=null){
                    if(empresaPermisoAplicacionDto.getAccesoSgfensValidacionxml()== 1){ %>
                <li>
                    <a href="../../jsp/catValidaXML/catValidaXML_list.jsp"><img src="../../images/icon_validaXML.png" alt="ValidaXMLV3.2"/>Validación XML</a>
                    <!--<ul>
                        <li><a href="../../jsp/catValidaXML/catValidaXML_form.jsp"><img src="../../images/icon_validaXML.png"/>Registro Patronal</a></li>                        
                    </ul>-->
                </li>
                <%}}%>
        </ul>
        <% } %>
        <br class="clear"/>

        <!-- Begin left panel calendar -->
        <!--<div id="calendar"></div>-->
        <!-- End left panel calendar -->

</div>
<!-- End left panel -->