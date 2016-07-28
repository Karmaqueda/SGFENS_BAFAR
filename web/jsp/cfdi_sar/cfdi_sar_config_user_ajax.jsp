<%-- 
    Document   : cfdi_sar_config_user_ajax
    Created on : 19/03/2015, 10:10:41 AM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.dao.jdbc.EmpresaDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="java.util.List"%>
<%@page import="com.tsp.sct.dao.jdbc.SarAreaEntregaDaoImpl"%>
<%@page import="com.tsp.sct.bo.SarAreaEntregaBO"%>
<%@page import="com.tsp.sct.dao.dto.SarAreaEntrega"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.bo.ConexionSARBO"%>
<%@page import="com.tsp.sct.dao.jdbc.SarCamposAdicionalesDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SarCamposAdicionales"%>
<%@page import="com.tsp.sct.dao.dto.SarCamposAdicionales"%>
<%@page import="com.tsp.sct.dao.jdbc.SarClienteEntregaDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SarClienteEntrega"%>
<%@page import="com.tsp.sct.dao.dto.SarUsuarioProveedorPk"%>
<%@page import="com.tsp.sct.dao.jdbc.SarUsuarioProveedorDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SarUsuarioProveedor"%>
<%@page import="com.tsp.sct.bo.SarUsuarioProveedorBO"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idSarUsuario = -1;
    int idEmpresaPadre = -1;
    String sarUsuario ="";
    String sarPass =""; 
    int estatus = 1;//habilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idSarUsuario = Integer.parseInt(request.getParameter("id_sar_usuario"));
    }catch(NumberFormatException ex){}
    try{
        idEmpresaPadre = Integer.parseInt(request.getParameter("idEmpresaPadre"));
    }catch(NumberFormatException ex){}
    sarUsuario = request.getParameter("sar_usuario")!=null?new String(request.getParameter("sar_usuario").getBytes("ISO-8859-1"),"UTF-8"):"";
    sarPass = request.getParameter("sar_pass")!=null?new String(request.getParameter("sar_pass").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(sarUsuario, 1, 100))
        msgError += "<ul>El dato 'Usuario SAR' es requerido.";
    if(!gc.isValidString(sarPass, 1, 100))
        msgError += "<ul>El dato 'Contrase&ntilde;a SAR' es requerido";   
    if(idSarUsuario <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'SAR Usuario' es requerido al editar";
    if(idEmpresaPadre <= 0)
        msgError += "<ul>El dato ID 'Empresa Padre' es requerido, revise la configuración de su sucursal";

    int extSarIdUsuario = 0;
    int extSarIdProveedor = 0;
    String extSarRfcProveedor = null;
    int extSarIdCliente = 0;
    String extSarRfcCliente = null;
    String extSarRazonSocialCliente = null;
    int extSarRelacionCodProvArea = 0;
    int extReqOrdenCompra = 0;
    List<com.tsp.workflow.ws.WsItemArea> areasWS = null;
    String datosRecuperadosWS = "";

    if(msgError.equals("")){
        //Conectamos a web service de consulta SAR
        com.tsp.workflow.ws.AccesoRequest accesoRequest = new com.tsp.workflow.ws.AccesoRequest();
        accesoRequest.setUsuario(sarUsuario);
        accesoRequest.setContrasena(sarPass);

        try{
            com.tsp.workflow.ws.ConsultaDatosGralProveedorResponse responseWS = ConexionSARBO.getDatosGeneralProveedor(accesoRequest);
            if (responseWS.isError()){
                msgError += ("Error retornado por Sistema SAR: " + responseWS.getNumError() + " - " + responseWS.getMsgError());
            }else{
                try{
                    //extSarIdUsuario = responseWS.getWsItemProveedor().getId();
                    extSarIdProveedor = responseWS.getWsItemProveedor().getId();
                    extSarRfcProveedor = responseWS.getWsItemProveedor().getRfc();
                    extSarIdCliente = responseWS.getWsItemCliente().getId();
                    extSarRfcCliente = responseWS.getWsItemCliente().getRfc();
                    extSarRazonSocialCliente = responseWS.getWsItemCliente().getRazonSocial();
                    extSarRelacionCodProvArea = responseWS.getWsItemCliente().isRelacionCodigoProvXArea()?1:0;
                    extReqOrdenCompra = responseWS.getWsItemProveedor().isRequerirOC()?1:0;
                    areasWS = responseWS.getListaWsItemArea();

                    datosRecuperadosWS = ConexionSARBO.parseHTMLDatosGralProveedorResponse(responseWS);
                }catch(Exception ex){
                    msgError += "<ul>Cuenta no configurada correctamente en SAR. " + ex.toString();
                }
                if (extSarIdProveedor<=0 || extSarIdCliente<=0 
                        || StringManage.getValidString(extSarRfcCliente).length()<=0 ){
                    msgError += "<ul>Cuenta no configurada correctamente en SAR. Faltan datos importantes como ID de Proveedor, ID y/o RFC Cliente";
                }
            }

        }catch(Exception ex){
            msgError += "<ul>Existe un problema de conexión al servicio SAR: " + ex.toString();
        }
        //Fin conexión web service consulta SAR

        if(msgError.equals("")){
            //Verificamos que el RFC registrado en Pretoriano, sea igual al registrado como proveedor en SAR
            Empresa empresa = new EmpresaDaoImpl(user.getConn()).findByPrimaryKey(idEmpresa);
            if (!empresa.getRfc().equals(extSarRfcProveedor)
                    || StringManage.getValidString(empresa.getRfc()).length()<=0 ){
                msgError += "<ul>El RFC registrado en Pretoriano no corresponde al registrado como proveedor en el SAR:"
                        + "<br/>RFC en Pretoriano: '" + empresa.getRfc() + "'"
                        + "<br/>RFC en SAR: '" + extSarRfcProveedor + "'";
            }
            
            //Buscamos por ID externo de Proveedor en SAR
            if(idSarUsuario==0){
                //Si intenta crear uno nuevo y ya esta registrado mostramos alerta
                SarUsuarioProveedorBO supBO = new SarUsuarioProveedorBO(user.getConn());
                SarUsuarioProveedor[] coincidenciasPrevias = supBO.findSarUsuarioProveedors(-1, idEmpresa, 0, 0, " AND EXT_SAR_ID_PROVEEDOR=" + extSarIdProveedor);
                if (coincidenciasPrevias.length>0){
                    msgError += "<ul>El usuario y password del sistema SAR ya fueron registrados previamente en una cuenta del sistema Pretoriano, no es posible tener 2 cuentas iguales. Edite o elimine la original.";
                }
            }
        }
    }
    
    
    if(msgError.equals("")){
        if(idSarUsuario>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                SarUsuarioProveedorBO supBO = new SarUsuarioProveedorBO(idSarUsuario,user.getConn());
                SarUsuarioProveedor dto = supBO.getSarUsuarioProveedor();
                SarClienteEntrega sarClienteDto = supBO.getSarClienteEntrega();
                SarCamposAdicionales sarCamposAdicionalesDto = supBO.getSarCamposAdicionales();
                
                dto.setIdEmpresa(idEmpresa);
                dto.setExtSarIdUsuario(extSarIdUsuario);
                dto.setExtSarUsuario(sarUsuario);
                dto.setExtSarPass(sarPass);
                dto.setExtSarIdProveedor(extSarIdProveedor);
                dto.setExtSarRfcProveedor(extSarRfcProveedor);
                dto.setIdEstatus(estatus);
                
                boolean editaCliente = true;
                if (sarClienteDto==null){
                    sarClienteDto = new SarClienteEntrega();
                    sarClienteDto.setIdSarUsuario(dto.getIdSarUsuario());
                    editaCliente = false;
                }
                sarClienteDto.setExtSarIdCliente(extSarIdCliente);
                sarClienteDto.setExtSarRfc(extSarRfcCliente);
                sarClienteDto.setExtSarRazonSocial(extSarRazonSocialCliente);
                sarClienteDto.setExtSarRelacionCodprovArea(extSarRelacionCodProvArea);
                
                boolean editaCamposAd = true;
                if (sarCamposAdicionalesDto==null){
                    sarCamposAdicionalesDto = new SarCamposAdicionales();
                    sarCamposAdicionalesDto.setIdSarUsuario(dto.getIdSarUsuario());
                    editaCamposAd = false;
                }
                sarCamposAdicionalesDto.setExtReqOrdenCompra(extReqOrdenCompra);
                
                try{
                    //Actualizamos registro principal
                    new SarUsuarioProveedorDaoImpl(user.getConn()).update(dto.createPk(), dto);
                    
                    //Actualizamos tablas relacionadas (cliente entrega, campos adicionales)
                    if (editaCliente){
                        new SarClienteEntregaDaoImpl(user.getConn()).update(sarClienteDto.createPk(), sarClienteDto);
                    }else{
                        new SarClienteEntregaDaoImpl(user.getConn()).insert(sarClienteDto);
                    }
                    if (editaCamposAd){
                        new SarCamposAdicionalesDaoImpl(user.getConn()).update(sarCamposAdicionalesDto.createPk(), sarCamposAdicionalesDto);
                    }else{
                        new SarCamposAdicionalesDaoImpl(user.getConn()).insert(sarCamposAdicionalesDto);
                    }
                    
                    //actualizamos registros de Areas de Entrega relacionadas
                    if (areasWS!=null){
                        SarAreaEntregaBO sarAreaBO = new SarAreaEntregaBO(user.getConn());
                        SarAreaEntregaDaoImpl sarAreaDao = new SarAreaEntregaDaoImpl(user.getConn());
                        SarAreaEntrega sarAreaDto;
                        
                        //Primero desactivamos todas las que esten asociadas al usuario SAR en local
                        SarAreaEntrega[] areasExistentesDelUsuario = sarAreaBO.findSarAreaEntregas(-1, dto.getIdSarUsuario(), 0, 0, "");
                        for (SarAreaEntrega areaExistente : areasExistentesDelUsuario){
                            areaExistente.setIdEstatus(2);
                            sarAreaDao.update(areaExistente.createPk(), areaExistente);
                        }
                            
                        //Luego actualizamos(y activar) o insertamos segun sea el caso
                        for (com.tsp.workflow.ws.WsItemArea areaWS : areasWS){
                            SarAreaEntrega[] coincidenciasAreas = sarAreaBO.findSarAreaEntregas(-1, dto.getIdSarUsuario(), 0, 0, " AND EXT_SAR_ID_AREA=" + areaWS.getId());
                            boolean edicionArea = true;
                            if (coincidenciasAreas.length>0){
                                sarAreaDto = coincidenciasAreas[0];
                            }else{
                                sarAreaDto = new SarAreaEntrega();
                                edicionArea = false;
                            }
                            sarAreaDto.setIdSarUsuario(dto.getIdSarUsuario());
                            sarAreaDto.setExtSarIdArea(areaWS.getId());
                            sarAreaDto.setExtSarNombre(areaWS.getNombre());
                            sarAreaDto.setExtSarCodprovArea(StringManage.getValidString(areaWS.getCodigoProvXArea()));
                            sarAreaDto.setIdEstatus(1);
                            if (edicionArea){
                                sarAreaDao.update(sarAreaDto.createPk(), sarAreaDto);
                            }else{
                                sarAreaDao.insert(sarAreaDto);
                            }
                        }
                    }
                    
                    out.print("<!--EXITO-->Registro actualizado satisfactoriamente.");
                    out.print("</br>Detalle de Información recuperada de SAR: </br>" + datosRecuperadosWS);
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudieron actualizar los registro relacionados (SarClienteEntrega/SarCamposAdicionales). Informe del error al administrador del sistema: " + ex.toString());
                    out.print("</br>Detalle de Información recuperada de SAR: </br>" + datosRecuperadosWS);
                    ex.printStackTrace();
                }
                
            }else{
                out.print("<!--ERROR-->Acción no válida.");
            }
        }else{
            /*
            *  Nuevo
            */
            
            try {                
                
                /**
                 * Creamos el registro Principal
                 */
                SarUsuarioProveedor supDto = new SarUsuarioProveedor();
                SarUsuarioProveedorDaoImpl supDao = new SarUsuarioProveedorDaoImpl(user.getConn());
                
                supDto.setIdEmpresa(idEmpresa);
                supDto.setExtSarIdUsuario(extSarIdUsuario);
                supDto.setExtSarUsuario(sarUsuario);
                supDto.setExtSarPass(sarPass);
                supDto.setExtSarIdProveedor(extSarIdProveedor);
                supDto.setExtSarRfcProveedor(extSarRfcProveedor);
                supDto.setIdEstatus(estatus);

                /**
                 * Realizamos el insert
                 */
                supDao.insert(supDto);
                
                SarClienteEntrega sarClienteDto = new SarClienteEntrega();
                sarClienteDto.setIdSarUsuario(supDto.getIdSarUsuario());
                sarClienteDto.setExtSarIdCliente(extSarIdCliente);
                sarClienteDto.setExtSarRfc(extSarRfcCliente);
                sarClienteDto.setExtSarRazonSocial(extSarRazonSocialCliente);
                sarClienteDto.setExtSarRelacionCodprovArea(extSarRelacionCodProvArea);
                
                SarCamposAdicionales sarCamposAdicionalesDto = new SarCamposAdicionales();
                sarCamposAdicionalesDto.setIdSarUsuario(supDto.getIdSarUsuario());
                sarCamposAdicionalesDto.setExtReqOrdenCompra(extReqOrdenCompra);

                //insertamos tambien Registros relacionados de otras tablas
                new SarClienteEntregaDaoImpl(user.getConn()).insert(sarClienteDto);
                new SarCamposAdicionalesDaoImpl(user.getConn()).insert(sarCamposAdicionalesDto);
                
                //insertamos registros de Areas de Entrega relacionadas
                if (areasWS!=null){
                    SarAreaEntregaBO sarAreaBO = new SarAreaEntregaBO(user.getConn());
                    SarAreaEntregaDaoImpl sarAreaDao = new SarAreaEntregaDaoImpl(user.getConn());
                    SarAreaEntrega sarAreaDto;
                    for (com.tsp.workflow.ws.WsItemArea areaWS : areasWS){
                        sarAreaDto = new SarAreaEntrega();
                        sarAreaDto.setIdSarUsuario(supDto.getIdSarUsuario());
                        sarAreaDto.setExtSarIdArea(areaWS.getId());
                        sarAreaDto.setExtSarNombre(areaWS.getNombre());
                        sarAreaDto.setExtSarCodprovArea(StringManage.getValidString(areaWS.getCodigoProvXArea()));
                        sarAreaDto.setIdEstatus(1);
                        sarAreaDao.insert(sarAreaDto);
                    }
                }
                
                out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
                out.print("</br>Detalle de Información recuperada de SAR: </br>" + datosRecuperadosWS);
            
            }catch(Exception e){
                e.printStackTrace();
                out.print("Ocurrio un error inesperado al guardar el registro: " + e.toString()) ;
            }
        }
    }else{
        out.print("<!--ERROR-->"+msgError);
    }

%>