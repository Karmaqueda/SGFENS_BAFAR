<%-- 
    Document   : catSmsEnvio_ajax
    Created on : 24/03/2016, 01:27:38 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.dao.jdbc.SmsEmpresaConfigDaoImpl"%>
<%@page import="com.tsp.sct.bo.SmsEmpresaConfigBO"%>
<%@page import="com.tsp.sct.dao.dto.SmsEmpresaConfig"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaDaoImpl"%>
<%@page import="com.tsp.sgfens.sesion.SmsEnvioSesion"%>
<%@page import="com.tsp.sgfens.sesion.SmsEnvioDestinatariosSesion"%>
<%@page import="com.tsp.sct.util.GenericMethods"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.SmsEnvioDetalleDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SmsEnvioDetalle"%>
<%@page import="com.tsp.sct.dao.jdbc.SmsEnvioLoteDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SmsEnvioLote"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="smsEnvioSesion" scope="session" class="com.tsp.sgfens.sesion.SmsEnvioSesion"/>
<%
    String msgError="";
    String msgExitoExtra="";
    
    /*
    *   Modos:
    *       -1  =  No se eligio (ERROR)
    * 
    *        1 = Guardar
    */
    
    int mode=-1;
    try{
        mode = Integer.parseInt(request.getParameter("mode"));
    }catch(NumberFormatException e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
     
    GenericValidator gc = new GenericValidator();
    Gson gson = new Gson();    
    
    /*
     * Procesamiento
     */
    if (mode==-1){
        msgError = "No se eligio una acción correcta. Reintente el llenado de los datos.";
    }else if (mode==1){
        //1 = Guardar
        Empresa empresaMatrizDto = new EmpresaBO(user.getConn()).getEmpresaMatriz(idEmpresa);
        SmsEmpresaConfigBO smsEmpresaConfigBO = new SmsEmpresaConfigBO(user.getConn());
        SmsEmpresaConfigDaoImpl smsEmpresaConfigDao = new SmsEmpresaConfigDaoImpl(user.getConn());
        SmsEmpresaConfig smsEmpresaConfigDto = null;
        if (empresaMatrizDto !=null ){
            smsEmpresaConfigBO = new SmsEmpresaConfigBO(user.getConn());
            SmsEmpresaConfig[] smsEmpresaConfigDtos = smsEmpresaConfigBO.findSmsEmpresaConfigs(-1, empresaMatrizDto.getIdEmpresa(), 0, 0, "");
            if (smsEmpresaConfigDtos.length>0)
                smsEmpresaConfigDto = smsEmpresaConfigDtos[0];
        }else{
            msgError+="<ul>No se encontro la empresa Matriz a la que pertenece el usuario.";
        }
        
        
        //Parametros
        int mensaje_venta = 0;//por defecto desactivado
        int mensaje_venta_fijo = 1;//sin default
        String mensaje_venta_libre = "";
        
        try{
            mensaje_venta = Integer.parseInt(request.getParameter("mensaje_venta"));
        }catch(NumberFormatException ex){}
        try{
            mensaje_venta_fijo = Integer.parseInt(request.getParameter("mensaje_venta_fijo"));
        }catch(NumberFormatException ex){}
        mensaje_venta_libre = request.getParameter("mensaje_venta_libre")!=null?new String(request.getParameter("mensaje_venta_libre").getBytes("ISO-8859-1"),"UTF-8"):"";
        
        int mensaje_abono = 0;//por defecto desactivado
        int mensaje_abono_fijo = 1;//1 por defecto, 1=Fijo
        String mensaje_abono_libre = "";
        
        try{
            mensaje_abono = Integer.parseInt(request.getParameter("mensaje_abono"));
        }catch(NumberFormatException ex){}
        try{
            mensaje_abono_fijo = Integer.parseInt(request.getParameter("mensaje_abono_fijo"));
        }catch(NumberFormatException ex){}
        mensaje_abono_libre = request.getParameter("mensaje_abono_libre")!=null?new String(request.getParameter("mensaje_abono_libre").getBytes("ISO-8859-1"),"UTF-8"):"";
        
        int mensaje_factura = 0;//por defecto desactivado
        int mensaje_factura_fijo = 1;//1 por defecto, 1=Fijo
        String mensaje_factura_libre = "";
        
        try{
            mensaje_factura = Integer.parseInt(request.getParameter("mensaje_factura"));
        }catch(NumberFormatException ex){}
        try{
            mensaje_factura_fijo = Integer.parseInt(request.getParameter("mensaje_factura_fijo"));
        }catch(NumberFormatException ex){}
        mensaje_factura_libre = request.getParameter("mensaje_factura_libre")!=null?new String(request.getParameter("mensaje_factura_libre").getBytes("ISO-8859-1"),"UTF-8"):"";
        
        int mensaje_cancela_fac = 0;//por defecto desactivado
        int mensaje_cancela_fac_fijo = 1;//1 por defecto, 1=Fijo
        String mensaje_cancela_fac_libre = "";
        
        try{
            mensaje_cancela_fac = Integer.parseInt(request.getParameter("mensaje_cancela_fac"));
        }catch(NumberFormatException ex){}
        try{
            mensaje_cancela_fac_fijo = Integer.parseInt(request.getParameter("mensaje_cancela_fac_fijo"));
        }catch(NumberFormatException ex){}
        mensaje_cancela_fac_libre = request.getParameter("mensaje_cancela_fac_libre")!=null?new String(request.getParameter("mensaje_cancela_fac_libre").getBytes("ISO-8859-1"),"UTF-8"):"";
        
        int mensaje_callcenter = 0;//por defecto desactivado
        int mensaje_callcenter_fijo = 1;//1 por defecto, 1=Fijo
        String mensaje_callcenter_libre = "";
        
        try{
            mensaje_callcenter = Integer.parseInt(request.getParameter("mensaje_callcenter"));
        }catch(NumberFormatException ex){}
        try{
            mensaje_callcenter_fijo = Integer.parseInt(request.getParameter("mensaje_callcenter_fijo"));
        }catch(NumberFormatException ex){}
        mensaje_callcenter_libre = request.getParameter("mensaje_callcenter_libre")!=null?new String(request.getParameter("mensaje_callcenter_libre").getBytes("ISO-8859-1"),"UTF-8"):"";
        
        int permiso_sms_empleados = 0;
        try{
            permiso_sms_empleados = Integer.parseInt(request.getParameter("permiso_sms_empleados"));
        }catch(NumberFormatException ex){}
        
        //Validaciones
        if(!gc.isValidString(mensaje_venta_libre, 0, 100))
            msgError += "<ul>El dato 'Texto Libre' para Pedidos es inválido, máximo 100 caracteres.";
        if(!gc.isValidString(mensaje_abono_libre, 0, 100))
            msgError += "<ul>El dato 'Texto Libre' para Abonos es inválido, máximo 100 caracteres.";
        if(!gc.isValidString(mensaje_factura_libre, 0, 100))
            msgError += "<ul>El dato 'Texto Libre' para Facturas es inválido, máximo 100 caracteres.";
        if(!gc.isValidString(mensaje_cancela_fac_libre, 0, 100))
            msgError += "<ul>El dato 'Texto Libre' para Cancelar Facturas es inválido, máximo 100 caracteres.";
        if(!gc.isValidString(mensaje_callcenter_libre, 0, 100))
            msgError += "<ul>El dato 'Texto Libre' para Call-Center es inválido, máximo 100 caracteres.";
        
        if (msgError.equals("")){
            try {
                if (smsEmpresaConfigDto!=null){
                    //Edicion

                    smsEmpresaConfigDto.setPermisoSmsEmpleados(permiso_sms_empleados);
                    if (smsEmpresaConfigDto.getMensajeVenta()<=1){//modificable, de otro modo estaria limitado por la consola principal de ventas
                        smsEmpresaConfigDto.setMensajeVenta(mensaje_venta);
                    }
                    smsEmpresaConfigDto.setMensajeVentaFijo(mensaje_venta_fijo);
                    smsEmpresaConfigDto.setMensajeVentaLibre(mensaje_venta_libre);
                    if (smsEmpresaConfigDto.getMensajeAbono()<=1){//modificable, de otro modo estaria limitado por la consola principal de ventas
                        smsEmpresaConfigDto.setMensajeAbono(mensaje_abono);
                    }
                    smsEmpresaConfigDto.setMensajeAbonoFijo(mensaje_abono_fijo);
                    smsEmpresaConfigDto.setMensajeAbonoLibre(mensaje_abono_libre);
                    if (smsEmpresaConfigDto.getMensajeFactura()<=1){//modificable, de otro modo estaria limitado por la consola principal de ventas
                        smsEmpresaConfigDto.setMensajeFactura(mensaje_factura);
                    }
                    smsEmpresaConfigDto.setMensajeFacturaFijo(mensaje_factura_fijo);
                    smsEmpresaConfigDto.setMensajeFacturaLibre(mensaje_factura_libre);
                    if (smsEmpresaConfigDto.getMensajeCancelaFac()<=1){//modificable, de otro modo estaria limitado por la consola principal de ventas
                        smsEmpresaConfigDto.setMensajeCancelaFac(mensaje_cancela_fac);
                    }
                    smsEmpresaConfigDto.setMensajeCancelaFacFijo(mensaje_cancela_fac_fijo);
                    smsEmpresaConfigDto.setMensajeCancelaFacLibre(mensaje_cancela_fac_libre);
                    if (smsEmpresaConfigDto.getMensajeCallcenter()<=1){//modificable, de otro modo estaria limitado por la consola principal de ventas
                        smsEmpresaConfigDto.setMensajeCallcenter(mensaje_callcenter);
                    }
                    smsEmpresaConfigDto.setMensajeCallcenterFijo(mensaje_callcenter_fijo);
                    smsEmpresaConfigDto.setMensajeCallcenterLibre(mensaje_callcenter_libre);

                    smsEmpresaConfigDao.update(smsEmpresaConfigDto.createPk(), smsEmpresaConfigDto);
                    
                    msgExitoExtra+="<ul>Registro actualizado exitosamente.";
                }else{
                    //Crear registro de configuracion
                    smsEmpresaConfigDto = new SmsEmpresaConfig();

                    smsEmpresaConfigDto.setIdEmpresa(empresaMatrizDto.getIdEmpresa());
                    smsEmpresaConfigDto.setPermisoSmsEmpleados(permiso_sms_empleados);
                    smsEmpresaConfigDto.setMensajeVenta(mensaje_venta);
                    smsEmpresaConfigDto.setMensajeVentaFijo(mensaje_venta_fijo);
                    smsEmpresaConfigDto.setMensajeVentaLibre(mensaje_venta_libre);
                    smsEmpresaConfigDto.setMensajeAbono(mensaje_abono);
                    smsEmpresaConfigDto.setMensajeAbonoFijo(mensaje_abono_fijo);
                    smsEmpresaConfigDto.setMensajeAbonoLibre(mensaje_abono_libre);
                    smsEmpresaConfigDto.setMensajeFactura(mensaje_factura);
                    smsEmpresaConfigDto.setMensajeFacturaFijo(mensaje_factura_fijo);
                    smsEmpresaConfigDto.setMensajeFacturaLibre(mensaje_factura_libre);
                    smsEmpresaConfigDto.setMensajeCancelaFac(mensaje_cancela_fac);
                    smsEmpresaConfigDto.setMensajeCancelaFacFijo(mensaje_cancela_fac_fijo);
                    smsEmpresaConfigDto.setMensajeCancelaFacLibre(mensaje_cancela_fac_libre);
                    smsEmpresaConfigDto.setMensajeCallcenter(mensaje_callcenter);
                    smsEmpresaConfigDto.setMensajeCallcenterFijo(mensaje_callcenter_fijo);
                    smsEmpresaConfigDto.setMensajeCallcenterLibre(mensaje_callcenter_libre);

                    smsEmpresaConfigDao.insert(smsEmpresaConfigDto);
                    
                    msgExitoExtra+="<ul>Registro creado exitosamente.";
                }
            }catch(Exception ex){
                msgError+= "<ul>Error inesperado al almacenar cambios: " + GenericMethods.exceptionStackTraceToString(ex);
            }
        }
        
    }
    
    if (msgError.equals("") && mode!=0){
        out.print("<!--EXITO-->" + msgExitoExtra);
    }
%>
<%
    if (!msgError.equals("")){
        out.print("<!--ERROR-->"+msgError);
    }
%>