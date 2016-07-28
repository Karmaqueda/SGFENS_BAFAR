<%-- 
    Document   : leftContent
    Created on : 19-oct-2012, 19:48:19
    Author     : ISCesarMartinez poseidon24@hotmail.com
--%>

<%@page import="com.tsp.sct.util.GenericMethods"%>
<%@page import="com.tsp.sct.dao.jdbc.NominaRegistroPatronalDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.NominaRegistroPatronal"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaModulosOcultosDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaModulosOcultos"%>
<%@page import="com.tsp.sct.bo.PretoModuloBeanBO"%>
<%@page import="com.tsp.sct.bo.PretoLicenciaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    EmpresaBO empresaBO = new EmpresaBO(user.getConn());
    EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());     
    
    PretoModuloBeanBO pretoModuloBeanBO = new PretoModuloBeanBO();
    pretoModuloBeanBO.cargaModulos(user.getConn());
    
    EmpresaModulosOcultos modulosOcultos = null;
                try{
                    modulosOcultos = new EmpresaModulosOcultosDaoImpl(user.getConn()).findWhereIdEmpresaEquals(empresaPermisoAplicacionDto.getIdEmpresa())[0];
                }catch(Exception e){}
    String verificadorSesionGuiaCerrada = "0";
    try{
        if(session.getAttribute("sesionCerrada")!= null){
            verificadorSesionGuiaCerrada = (String)session.getAttribute("sesionCerrada");
        }
    }catch(Exception e){}
%>
<!-- Begin left panel -->
<a href="javascript:;" id="show_menu">&raquo;</a>
    <script type="text/javascript">
        function validaRegistroPatronal(){
            <%
            Empresa empresa = new EmpresaBO(user.getUser().getIdEmpresa(), user.getConn()).getEmpresa();
            NominaRegistroPatronal[] registrosPatronales = new NominaRegistroPatronalDaoImpl(user.getConn()).findWhereIdEmpresaEquals(user.getUser().getIdEmpresa());
            if (registrosPatronales.length<=0){
            //if(empresa.getRegistroPatronal().trim().equals("")){
            %>
                apprise('<center><img src=../../images/warning.png> <br/>Da de alta al menos un Registro Patronal</center>',{'animate':true});
            <%}%>
        }
        
        function modoTipoAjax(mode, modulo){
           $.ajax({
                type: "POST",
                url: "../guiaInteractiva/guiaDinamica_ajax.jsp",
                data: { mode: mode , modulo : modulo},                       
                beforeSend: function(objeto){

                },
                success: function(datos){
                    if(datos.indexOf("--EXITO-->", 0)>0){                                                  

                   }else{

                   }
                }
            });
       }
       
    </script>
<div id="left_menu">
        <a href="javascript:;" id="hide_menu">&laquo;</a>
        <ul id="main_menu">
            <% if (user.isPermisoVerMenu()){                
                if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.inicio.getIdentificador()) < 0) ){
            %>
                    <li><a href="../../jsp/inicio/main.jsp" id="idInicioLeftContent"><img src="../../images/icon_home.png" alt="Inicio"/>Inicio</a></li>
                <%}%>
            
                <% if (user.getUser().getIdRoles() == RolesBO.ROL_DESARROLLO 
                                || user.getUser().getIdRoles() == RolesBO.ROL_ADMINISTRADOR) { %>
                <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.admin.getIdentificador()) < 0) ){%>
                <li style="position: relative; z-index: 99;">
                     <a href="" id="idAdministracionLeftContent"><img src="../../images/icon_admin.png" alt="Administración"/>Administración</a>
                     <ul>
                         <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.adminPerfil.getIdentificador()) < 0) ){%>
                             <li><a href="../../jsp/user/perfil.jsp"><img src="../../images/user_male_16.png"/>Mi Perfil</a></li>
                         <%}%>                        
                         <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.adminiLogo.getIdentificador()) < 0) ){%>
                             <li><a href="../../jsp/catImagenPersonal/catImagenPersonal_list.jsp"><img src="../../images/icon_imagenPersonal.png"/>Logo</a></li>
                         <%}%>
                         <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.adminUsuario.getIdentificador()) < 0) ){%>
                             <li><a href="../../jsp/catUsuarios/catUsuarios_list.jsp"><img src="../../images/icon_users.png"/>Usuarios</a></li>
                         <%}%>
                         <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.adminSucursal.getIdentificador()) < 0) ){%>
                          <% if (user.getUser().getIdRoles() != RolesBO.ROL_ADMINISTRADOR ) {
                              if(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa() == user.getUser().getIdEmpresa()){%>
                             <li><a href="../../jsp/catSucursales/catSucursales_list.jsp"><img src="../../images/icon_sucursales.png"/>Sucursales</a></li>
                          <% }} %>
                         <%}%>                        
                        <li><a href="../../jsp/catZonaHoraria/catZonaHoraria_form.jsp"><img src="../../images/icon_reloj.png"/>Zona Horaria</a></li>
                     </ul>	
                 </li>
                 <%}%>
                <%}%>
                
                <%
                    if (user.getUser().getIdRoles() == RolesBO.ROL_ADMINISTRADOR){
                        if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.cat.getIdentificador()) < 0) ){%>    
                        <li>
                            <a id="idCatalogosLeftContent" href="" <%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?"class='expose'":""%>><img src="../../images/icon_pages.png" alt="Catálogos"/>Catálogos</a>
                            <ul>
                                <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.catCliente.getIdentificador()) < 0) ){%>    
                                    <li><a href="../../jsp/catClientes/catClientes_list.jsp" id="idCatalogosLeftContentCliente" <%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?"class='expose'":""%>><img src="../../images/icon_cliente.png"/>Clientes</a></li>
                                <%}%>                               
                                <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.catProspecto.getIdentificador()) < 0) ){%>    
                                    <li><a href="../../jsp/catProspectos/catProspectos_list.jsp" id="idCatalogosLeftContentProspecto"><img src="../../images/icon_prospecto.png"/>Prospectos</a></li>
                                <%}%>                                
                            </ul>
                        </li>
                <%      }
                    }%>
                   
                <%
                if (empresaPermisoAplicacionDto!=null){
                %>
                    <%
                    if (user.getUser().getIdRoles() == RolesBO.ROL_ADMINISTRADOR){
                        if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.crFrmModulo.getIdentificador()) < 0) ){%>
                            <li>
                                <a href=""><img src="../../images/icon_crGrupoFormulario.png" alt="Módulo Formularios"/>Formularios</a>
                                    <ul>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.crFrmGrupo.getIdentificador()) < 0) ){%>
                                        <li><a href="../../jsp/catCrGrupoFormulario/catCrGrupoFormulario_list.jsp"><img src="../../images/icon_crGrupoFormulario.png"/>Grupos</a></li>
                                        <%}%>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.crFrmFormulario.getIdentificador()) < 0) ){%>
                                        <li><a href="../../jsp/catCrFormulario/catCrFormulario_list.jsp"><img src="../../images/icon_crFormulario.png"/>Formularios</a></li>
                                        <%}%>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.crFrmValidacion.getIdentificador()) < 0) ){%>
                                        <li><a href="../../jsp/catCrFormularioValidacion/catCrFormularioValidacion_list.jsp"><img src="../../images/icon_crFormularioValidacion.png"/>Validaciones</a></li>
                                        <%}%>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.crScore.getIdentificador()) < 0) ){%>
                                        <li><a href="../../jsp/catCrScore/catCrScore_list.jsp"><img src="../../images/icon_crScore.png"/>Score</a></li>
                                        <%}%>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.crImprimible.getIdentificador()) < 0) ){%>
                                        <li><a href="../../jsp/catCrDocImprimible/catCrDocImprimible_list.jsp"><img src="../../images/icon_crDocImprimible.png"/>Doc. Imprimibles</a></li>
                                        <%}%>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.crProductoCredito.getIdentificador()) < 0) ){%>
                                        <li><a href="../../jsp/catCrProductoCredito/catCrProductoCredito_list.jsp"><img src="../../images/icon_crProductoCredito.png"/>Producto Crédito</a></li>
                                        <%}%>
                                    </ul>                                                
                            </li>
                        <% }
                    }   %>
                <% 
                }   
                %>
                
                <%
                if (empresaPermisoAplicacionDto!=null){
                %>
                    <%
                    if (user.getUser().getIdRoles() == RolesBO.ROL_ADMINISTRADOR || user.getUser().getIdRoles() == RolesBO.ROL_VERIFICADOR || user.getUser().getIdRoles() == RolesBO.ROL_MESA_DE_CONTROL || user.getUser().getIdRoles() == RolesBO.ROL_PROMOTOR || user.getUser().getIdRoles() == RolesBO.ROL_GERENTE){
                        if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.crCtrlControl.getIdentificador()) < 0) ){%>
                            <li>
                                <a href=""><img src="../../images/icon_crModuloControl.png" alt="Módulo Control"/>Control</a>
                                    <ul>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.crCtrlSolicitud.getIdentificador()) < 0) ){%>
                                        <li><a href="../../jsp/catCrFormularioEvento/catCrFormularioEvento_list.jsp"><img src="../../images/icon_crFormularioEvento.png"/>Solicitudes</a></li>
                                        <%}%>
                                    </ul>                                                
                            </li>
                        <% }
                    }   %>
                <% 
                }   
                %>
                
                <%
                if (empresaPermisoAplicacionDto!=null){
                %>
                    <%
                    if (GenericMethods.datoEnColeccion(user.getUser().getIdRoles(), new Integer[]{RolesBO.ROL_ADMINISTRADOR, RolesBO.ROL_GERENTE, RolesBO.ROL_GESTOR} ) ){
                        if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.crCobCobranza.getIdentificador()) < 0) ){%>
                            <li>
                                <a href=""><img src="../../images/icon_crModuloCobranza.png" alt="Módulo Cobranza"/>Cobranza</a>
                                    <ul>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.crCobClientes.getIdentificador()) < 0) ){%>
                                        <li><a href="../../jsp/catCrCredCliente/catCrCredCliente_list.jsp"><img src="../../images/icon_crCredCliente.png"/>Clientes</a></li>
                                        <%}%>
                                    </ul>                                                
                            </li>
                        <% }
                    }   %>
                <% 
                }   
                %>
                
                <%
                if (user.getUser().getIdRoles() == RolesBO.ROL_ADMINISTRADOR){
                    if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.finanza.getIdentificador()) < 0) ){%>
                        <li>
                            <a href=""><img src="../../images/icon_finanzas.png" alt="Finanzas"/>Finanzas (CxP/CxC)</a>
                                <ul>
                                    <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.finanzaDashboard.getIdentificador()) < 0) ){%>
                                        <li><a href="../../jsp/cxc/index.jsp"><img src="../../images/icon_dashboard.png"/>Dashboard</a></li>
                                    <%}%>
                                    <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.finanzaCxcobrar.getIdentificador()) < 0) ){%>
                                        <li><a href="../../jsp/cxc/cxc_comprobantes_list.jsp"><img src="../../images/icon_cxc.png"/>Cuentas por Cobrar</a></li>
                                    <%}%>
                                    <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.finanzaCxPagar.getIdentificador()) < 0) ){%>
                                        <li><a href="../../jsp/cxp/cxp_comprobantes_list.jsp"><img src="../../images/icon_cxp.png"/>Cuentas por Pagar</a></li>
                                    <%}%>
                                </ul>                                                
                        </li>
                    <%}
                }
                %>
                
                <% 
                if (empresaPermisoAplicacionDto!=null){
                    //if(empresaPermisoAplicacionDto.getAccesoSgfensPretomovil()==1){
                    if (empresaPermisoAplicacionDto.getIdPretoLicencia()==PretoLicenciaBO.LICENCIA_PRETO_MOVIL
                                || empresaPermisoAplicacionDto.getIdPretoLicencia()==PretoLicenciaBO.LICENCIA_DEMO_MOVIL){
                %>
                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.pretoMov.getIdentificador()) < 0) ){%>
                            <%if (GenericMethods.datoEnColeccion(user.getUser().getIdRoles(), new Integer[]{RolesBO.ROL_ADMINISTRADOR, RolesBO.ROL_GERENTE} ) ){%>
                            <li>
                            
                                <a href="" id="idPretorianoMovilLeftContent" <%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?"class='expose'":""%>><img src="../../images/icon_phone.png" alt="PretorianoMovil"/>Pretoriano Movil</a>                            
                                    <ul>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.pretoMovEmpleado.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/catEmpleados/catEmpleados_list.jsp" id="idPretorianoMovilLeftContentEmpleados" <%=empresaPermisoAplicacionDto.getMostrarGuia() == 1 && verificadorSesionGuiaCerrada.equals("0")?"class='expose'":""%>><img src="../../images/icon_users.png"/>Empleados</a></li>
                                        <%}%>
                                        
                                    <%if (GenericMethods.datoEnColeccion(user.getUser().getIdRoles(), new Integer[]{RolesBO.ROL_ADMINISTRADOR} ) ){%>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.pretoMovLocalizacion.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/ModuloPretorianoMovil/dashboard.jsp"><img src="../../images/company_16.png"/>Localización</a></li>                                    
                                        <%}%>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.pretoMovDispositivo.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/catDispositivosMoviles/catDispositivosMoviles_list.jsp"><img src="../../images/icon_phone.png"/>Dispositivos Moviles</a></li>
                                        <%}%>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.pretoMovClienteAsignado.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/catEmpleados/catEmpleado_Clientes_List.jsp"><img src="../../images/cliemp_16.png"/>Clientes Asignados</a></li>
                                        <%}%>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.pretoMovMensaje.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/catEmpleados/catEmpleado_Mensajes_form.jsp?acc=mensajeMasivo"><img src="../../images/icon_mensajes.png"/>Mensajes</a></li>
                                        <%}%>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.pretoMovZonas.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/catRegiones/catRegiones_list.jsp"><img src="../../images/zone2_16.png"/>Zonas</a></li>
                                        <%}%>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.pretoMovSeguimiento.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/catSeguimiento/catSeguimiento_list.jsp"><img src="../../images/icon_seguimiento.png"/>Seguimiento</a></li>
                                        <%}%>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.pretoMovVisorMapa.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/mapa/mapa_visor.jsp"><img src="../../images/icon_mapa.png"/>Visor de mapa</a></li>
                                        <%}%>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.pretoMovLogistica.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/mapa/logistica.jsp" id="idPretorianoMovilLeftContentLogistica"><img src="../../images/icon_logistica.png"/>Log&iacute;stica</a></li>
                                        <%}%>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.pretoMovRutaEmpleado.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/catEmpleados/catEmpleados_RutaDia.jsp"><img src="../../images/icon_calendar.png"/>Ruta Empleado</a></li>
                                        <%}%>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.pretoMovGeocerca.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/catGeocerca/catGeocercas_list.jsp"><img src="../../images/icon_mapa_1.png"/>Geocercas</a></li>
                                        <%}%>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.pretoMovVisita.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/catClientesVisitas/catClientesVisitas_list.jsp"><img src="../../images/abrir-icono-6168-16.png"/>Visita Cliente</a></li>
                                        <%}%>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.pretoMovGastos.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/catEmpleados/catEmpleados_Gastos_list.jsp"><img src="../../images/icon_ventas.png"/>Gastos</a></li>
                                        <%}%>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.catHorarios.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/catHorarios/catHorarios_list.jsp"><img src="../../images/clock.png"/>Horarios</a></li>
                                        <%}%>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.adminFolioMovil.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/catFoliosMovil/catFoliosMovil_list.jsp"><img src="../../images/icon_folio_movil.png"/>Folios Movil Internos</a></li>
                                        <%}%>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.pretoMovCronometro.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/catCronometro/catCronometro_list.jsp"><img src="../../images/cronometro_16.png"/>Cronómetro</a></li>
                                        <%}%>
                                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.pretoMovConQPay.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/conectaQPay/interLoginQPay.jsp"><img src="../../images/icon_banco.png"/>QPay</a></li>
                                        <%}%>
                                    <% } %>
                                </ul>	
                            </li>
                            <% } %>
                        <%}%>
                <% 
                    }
                }
                %>
                <% 
                if (empresaPermisoAplicacionDto!=null){ 
                    if (user.getUser().getIdRoles() == RolesBO.ROL_DESARROLLO 
                            || user.getUser().getIdRoles() == RolesBO.ROL_ADMINISTRADOR) { %>
                        <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.reporte.getIdentificador()) < 0) ){%>
                            <li>
                                 <a href=""><img src="../../images/clipboard_report_bar_16_ns.png" alt="Reportes"/>Reportes</a>                            
                                 <ul>
                                    <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.reporteGeneral.getIdentificador()) < 0) ){%>
                                        <li><a href="../../jsp/reporte/reporte_menu.jsp"><img src="../../images/report_gral.png"/>Reportes Generales</a></li>
                                    <%}%>
                                    <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.reporteConfig.getIdentificador()) < 0) ){%>
                                        <%if(user.getUser().getIdRoles() == RolesBO.ROL_ADMINISTRADOR){%>
                                            <li><a href="../../jsp/catReportesConfigurables/catReportesConfigurables_list.jsp"><img src="../../images/icon_reporte.png"/>Reportes Configurables</a></li>
                                        <%}%>
                                    <%}%>
                                 </ul>                            
                            </li>
                        <%}%>
                <% }}%>
                
                <%if (empresaPermisoAplicacionDto!=null){
                    if(empresaPermisoAplicacionDto.getAccesoModuloSms() == 1){ %>
                        <%
                        if (user.getUser().getIdRoles() == RolesBO.ROL_ADMINISTRADOR){
                            if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.facturacionSar.getIdentificador()) < 0) ){%>
                                <li>
                                    <a href=""><img src="../../images/icon_sms.png" alt="Módulo SMS"/>SMS</a>
                                        <ul>
                                            <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.smsPlantillas.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/catSmsPlantillas/catSmsPlantillas_list.jsp"><img src="../../images/icon_smsPlantilla.png"/>Plantillas</a></li>
                                            <%}%>
                                            <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.smsAgenda.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/catSmsAgenda/catSmsAgendaDestinatarios_list.jsp"><img src="../../images/icon_smsAgendaDestinatario.png"/>Agenda</a></li>
                                            <%}%>
                                            <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.smsEnvio.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/catSmsEnvio/catSmsEnvio_list.jsp"><img src="../../images/icon_smsAgendaEnvio.png"/>Envíos</a></li>
                                            <%}%>
                                            <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.smsConfig.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/catSmsEmpresaConfig/catSmsEmpresaConfig_form.jsp"><img src="../../images/icon_smsConfiguracion.png"/>Configuración</a></li>
                                            <%}%>
                                            <%if(modulosOcultos == null ||  (modulosOcultos != null && modulosOcultos.getModulosOcultos().indexOf(pretoModuloBeanBO.smsCompra.getIdentificador()) < 0) ){%>
                                            <li><a href="../../jsp/catSmsCompraCreditos/catSmsCompraCreditos_list.jsp"><img src="../../images/icon_smsCompra.png"/>Comprar</a></li>
                                            <%}%>
                                        </ul>                                                
                                </li>
                            <% }
                        }   %>
                    <%  }
                }   
                %>
                
        </ul>
        <% } %>
        <br class="clear"/>

        <!-- Begin left panel calendar -->
        <!--<div id="calendar"></div>-->
        <!-- End left panel calendar -->

</div>
        
<!-- End left panel -->
