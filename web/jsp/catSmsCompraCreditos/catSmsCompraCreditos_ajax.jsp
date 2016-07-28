<%-- 
    Document   : catSmsEnvio_ajax
    Created on : 24/03/2016, 01:27:38 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.dao.jdbc.EmpresaDaoImpl"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.dao.jdbc.SmsCompraBitacoraDaoImpl"%>
<%@page import="com.tsp.sct.dao.jdbc.BancoOperacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SmsCompraBitacora"%>
<%@page import="com.tsp.sct.dao.dto.BancoOperacion"%>
<%@page import="com.tsp.sct.util.Converter"%>
<%@page import="com.tsp.sgfens.sesion.SmsCompraCreditosSesion"%>
<%@page import="com.tsp.sct.util.GenericMethods"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="java.util.Date"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="smsCompraSesion" scope="session" class="com.tsp.sgfens.sesion.SmsCompraCreditosSesion"/>
<%
    String msgError="";
    String msgExitoExtra="";
    
    /*
    *   Modos:
    *       -1  =  No se eligio (ERROR)
    * 
    *        2 = Calcular total
    *        3 = Guardar compra Exitosa, realizada con BanWire
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
    }else if (mode==2){
        //1 = Calcular total
        
        if (smsCompraSesion==null)
            smsCompraSesion = new SmsCompraCreditosSesion(user.getConn());
        
        //Limpiamos Sesion
        smsCompraSesion.setIdPaquete(0);
        smsCompraSesion.setCreditos(0);
        
        //Parametros
        int cantidadCreditos = 0;//por defecto desactivado
        
        try{
            cantidadCreditos = Integer.parseInt(request.getParameter("cantidad_creditos"));
        }catch(NumberFormatException ex){}
        
        if (cantidadCreditos<100){
            msgError += "<ul>La compra mínima es por 100 créditos.";
        }
       
        if (msgError.equals("")){
            smsCompraSesion.setCreditos(cantidadCreditos);
            if (smsCompraSesion.calcularTotal()<=0){
                msgError += "<ul>Existe un problema de configuración interna, posiblemente la cantidad de creditos solicitada, no esta dentro de algun rango existente. Contacte a un Asesor de Ventas.";
            }else{
                Object[] data = new Object[4];
                data[0] =  Converter.doubleToStringFormatMexico(smsCompraSesion.calcularSubtotal());
                data[1] =  Converter.doubleToStringFormatMexico(smsCompraSesion.calcularIVA());
                data[2] =  Converter.doubleToStringFormatMexico(smsCompraSesion.calcularTotal());
                data[3] =  Converter.roundDouble(smsCompraSesion.calcularTotal());
                
                //Usamos JSON para retornar el objeto respuesta
                String jsonOutput = gson.toJson(data);
                msgExitoExtra = jsonOutput;
            }
        }
        
    }else if(mode==3){
        // 3 = Guardar compra Exitosa, realizada con BanWire
        
        int cantidadCreditos = smsCompraSesion.getCreditos();
        double bwTotal =  0; 
        String bwReference = "";
        String bwAuthCode = "";
        String bwId = "";
        
        try{
            bwTotal = Double.parseDouble(request.getParameter("bw_total"));
        }catch(NumberFormatException ex){}
        bwReference = request.getParameter("bw_reference")!=null?new String(request.getParameter("bw_reference").getBytes("ISO-8859-1"),"UTF-8"):"";
        bwAuthCode = request.getParameter("bw_auth_code")!=null?new String(request.getParameter("bw_auth_code").getBytes("ISO-8859-1"),"UTF-8"):"";
        bwId = request.getParameter("bw_id")!=null?new String(request.getParameter("bw_id").getBytes("ISO-8859-1"),"UTF-8"):"";
        
        BancoOperacionDaoImpl bancoOperacionDao = new BancoOperacionDaoImpl(user.getConn());
        SmsCompraBitacoraDaoImpl smsCompraBitacoraDao = new SmsCompraBitacoraDaoImpl(user.getConn());
        BancoOperacion bancoOperacion = new BancoOperacion();
        SmsCompraBitacora smsCompraBitacora = new SmsCompraBitacora();
        
        try{
            Date now = new Date();
            bancoOperacion.setIdEmpresa(idEmpresa);
            bancoOperacion.setNoTarjeta("0000000000000000");
            bancoOperacion.setNombreTitular("" + user.getDatosUsuario().getNombre() + " " + user.getDatosUsuario().getApellidoPat());
            bancoOperacion.setMonto(bwTotal);
            bancoOperacion.setBancoAuth(bwAuthCode);
            bancoOperacion.setBancoOrderId(bwId);
            bancoOperacion.setBancoOperFecha(DateManage.dateToSQLDateTime(now));
            bancoOperacion.setBancoOperType("BANWIRE TARJETA CREDITO");
            bancoOperacion.setBancoOperIssuingBank("");
            bancoOperacion.setIdEstatus(1);
            bancoOperacion.setDataRef(bwReference);
            //insertamos bancoOperacion
            bancoOperacionDao.insert(bancoOperacion);
            
            
            smsCompraBitacora.setIsAutoServicio(1);
            smsCompraBitacora.setIdEmpresa(idEmpresa);
            smsCompraBitacora.setFechaHr(now);
            smsCompraBitacora.setIdSmsPaquetePrecio(0);
            smsCompraBitacora.setCantidadAgregada(cantidadCreditos);
            smsCompraBitacora.setObservaciones("Auto-Servicio compra de Créditos.");
            smsCompraBitacora.setIdUsuarioPreto(user.getUser().getIdUsuarios());
            smsCompraBitacora.setIdBancoOperacion(bancoOperacion.getIdOperacionBancaria());
            smsCompraBitacora.setDatoPago1(bwAuthCode);
            smsCompraBitacora.setDatoPago2(bwReference);
            smsCompraBitacora.setDatoPago3(bwId);
            
            smsCompraBitacoraDao.insert(smsCompraBitacora);
        }catch(Exception ex){
            msgError+="<ul>Error inesperado al guardar registro de compra." + ex.toString();
        }
        
        try{
            Empresa empresaMatrizDto = new EmpresaBO(user.getConn()).getEmpresaMatriz(idEmpresa);
            empresaMatrizDto.setCreditosSms(empresaMatrizDto.getCreditosSms() + cantidadCreditos);
            
            new EmpresaDaoImpl(user.getConn()).update(empresaMatrizDto.createPk(), empresaMatrizDto);
        }catch(Exception ex){
            msgError+="<ul>Error inesperado al asignar creditos SMS a empresa." + ex.toString();
        }
        
        if (msgError.equals(""))
            msgExitoExtra += "Créditos SMS aplicados.";
        
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