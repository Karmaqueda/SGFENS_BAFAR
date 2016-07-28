<%-- 
    Document   : catReportesConfigurables_ajax
    Created on : 22/05/2014, 12:03:26 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.ReporteConfigurableDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.ReporteConfigurable"%>
<%@page import="com.tsp.sct.bo.ReporteConfigurableBO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idTipoReporteSeleccion = -1;  //id del tipo de reporte a configurar
    int idReporteConfigurable = -1; //id del reporte confogurable
    String correosGeoNotificacion ="";  
    int domingoReporte = -1;
    int lunesReporte = -1;
    int martesReporte = -1;
    int miercolesReporte = -1;
    int juevesReporte = -1;
    int viernesReporte = -1;
    int sabadoReporte = -1;
    int diarioReporte = -1;
    int idCliente = -1;
    //int idSucursal = -1;
    int idVendedor = -1;
    String maxTiempoAtras = "D-1";
        
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idReporteConfigurable = Integer.parseInt(request.getParameter("idReporteConfigurable"));
    }catch(NumberFormatException ex){} 
    try{
        idTipoReporteSeleccion = Integer.parseInt(request.getParameter("idTipoReporteSeleccion"));
    }catch(NumberFormatException ex){}    
    correosGeoNotificacion = request.getParameter("correosReporteDestino")!=null?new String(request.getParameter("correosReporteDestino").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        domingoReporte = Integer.parseInt(request.getParameter("domingoReporte"));
    }catch(NumberFormatException ex){}
    try{
        lunesReporte = Integer.parseInt(request.getParameter("lunesReporte"));
    }catch(NumberFormatException ex){}
    try{
        martesReporte = Integer.parseInt(request.getParameter("martesReporte"));
    }catch(NumberFormatException ex){}
    try{
        miercolesReporte = Integer.parseInt(request.getParameter("miercolesReporte"));
    }catch(NumberFormatException ex){}
    try{
        juevesReporte = Integer.parseInt(request.getParameter("juevesReporte"));
    }catch(NumberFormatException ex){}
    try{
        viernesReporte = Integer.parseInt(request.getParameter("viernesReporte"));
    }catch(NumberFormatException ex){}
    try{
        sabadoReporte = Integer.parseInt(request.getParameter("sabadoReporte"));
    }catch(NumberFormatException ex){}
    try{
        diarioReporte = Integer.parseInt(request.getParameter("diarioReporte"));
    }catch(NumberFormatException ex){}
    
    Date fechaInicialfiltro = null;
    String fechaInicialfiltroStr = null;
        try{
            fechaInicialfiltro = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fechaInicialfiltro"));
            fechaInicialfiltroStr = request.getParameter("fechaInicialfiltro");
        }catch(Exception e){}
    Date fechaFinalFiltro = null;
    String fechaFinalFiltroStr = null;
        try{
            fechaFinalFiltro = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fechaFinalFiltro"));
            fechaFinalFiltroStr = request.getParameter("fechaFinalFiltro");
        }catch(Exception e){}
        
    try{
        idCliente = Integer.parseInt(request.getParameter("q_idcliente"));
    }catch(NumberFormatException e){}
    try{
        idVendedor = Integer.parseInt(request.getParameter("q_idvendedor"));
    }catch(NumberFormatException e){}
    
    maxTiempoAtras = request.getParameter("max_tiempo_atras")!=null?new String(request.getParameter("max_tiempo_atras").getBytes("ISO-8859-1"),"UTF-8"):"D-1";    
    
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();  
         
    System.out.println("_____________MODO: "+mode);
    System.out.println("idTipoReporteSeleccion: "+idTipoReporteSeleccion);
    
    String[] correos = new String[0];
    ArrayList<String> listCorreosValidos = new ArrayList<String>();
    boolean enviarCorreo = false;
    if(!mode.equals("2")){//para borrar el tipo de reporte configurable
        System.out.println("_____________ENTRO A VALIDAR");
        if (!correosGeoNotificacion.trim().equals("")){
                correos = correosGeoNotificacion.split(",");

                for (String itemCorreo: correos){
                    try { itemCorreo = itemCorreo.trim(); } catch(Exception ex){}
                    if (gc.isEmail(itemCorreo)){
                        listCorreosValidos.add(itemCorreo);
                    }
                }

                if (listCorreosValidos.size()<=0)
                    msgError += "Ninguno de los correos proporcionados es válido";

                if (listCorreosValidos.size()>0)
                    enviarCorreo = true;
            }   

        if(idTipoReporteSeleccion <= 0)
            msgError += "<ul>El dato ID 'De reporte' es requerido";
        if(!gc.isValidString(correosGeoNotificacion, 10, 110))
            msgError += "<ul>El dato 'correos' es requerido.";
        
        ///validamos las fechas y que sea le permita aplicar filtro por fecha:
        //if(idTipoReporteSeleccion == 30){//REPORTES A LOS CUALES SE LES APLICA EL FILTRO POR FECHA
            //if(fechaInicialfiltro == null && fechaFinalFiltro == null){
            //   msgError += "<ul>Debe seleccionar una fecha final y/o inicial";
            //}
        if(fechaInicialfiltro != null){
            if(fechaFinalFiltro != null){
                if(fechaFinalFiltro.before(fechaInicialfiltro))
                    msgError += "<ul>La fecha Final es anterior a la Inicial";
            }
        }
        //}
        
        /*if(fechaFinalFiltro != null){
            if(fechaInicialfiltro != null){
                if(fechaFinalFiltro.before(fechaInicialfiltro))
                    msgError += "<ul>La fecha Inicial es anterior a la final";
            }
        }*/
        
        if (idTipoReporteSeleccion==ReportBO.EMPLEADO_INVENTARIO_REPORT){
            if (idVendedor<=0){
                msgError += "<ul>Para este tipo de reporte es obligatorio elegir un Empleado.";
            }
        }
        
    }

    if(msgError.equals("")){
        if(idReporteConfigurable>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                ReporteConfigurableBO reporteConfigurableBO = new ReporteConfigurableBO(idReporteConfigurable,user.getConn());
                ReporteConfigurable reporteConfigurableDto = reporteConfigurableBO.getReporteConfigurable();
                                
                reporteConfigurableDto.setIdTipoReporte(idTipoReporteSeleccion);
                /////*ARMAMOS EL STRING DE LOS DIAS A ENVIAR EL REPORTE:
                String diasEnviarNotificaciones = "";
                if(domingoReporte > 0)
                    diasEnviarNotificaciones += domingoReporte+",";
                if(lunesReporte > 0)
                    diasEnviarNotificaciones += lunesReporte+",";
                if(martesReporte > 0)
                    diasEnviarNotificaciones += martesReporte+",";
                if(miercolesReporte > 0)
                    diasEnviarNotificaciones += miercolesReporte+",";
                if(juevesReporte > 0)
                    diasEnviarNotificaciones += juevesReporte+",";
                if(viernesReporte > 0)
                    diasEnviarNotificaciones += viernesReporte+",";
                if(sabadoReporte > 0)
                    diasEnviarNotificaciones += sabadoReporte+",";
                if(diarioReporte > 0)
                    diasEnviarNotificaciones += diarioReporte+",";                
                /////*
                reporteConfigurableDto.setIdDias(diasEnviarNotificaciones);
                ////PARA RECUPERAR LOS CORREOS VALIDOS
                int contador = 0;
                String correosGuardar = "";
                for(String cor : listCorreosValidos ){
                    if(contador > 0){
                        correosGuardar += ",";
                    }
                    correosGuardar += cor;
                    contador++;
                }
                reporteConfigurableDto.setCorreos(correosGuardar);
                ////
                //////--CARGAMOS LA FECHA DEL FILTRO
                String filtroAplicable = null;
                if(idTipoReporteSeleccion == 30 || idTipoReporteSeleccion == 27 || idTipoReporteSeleccion == 21 || idTipoReporteSeleccion == 40 || idTipoReporteSeleccion == 45){//REPORTES A LOS CUALES SE LES APLICA EL FILTRO POR FECHA                    
                    if(fechaInicialfiltro != null){
                        filtroAplicable = fechaInicialfiltroStr+"";
                    }else{
                        filtroAplicable = "NA";
                    }
                    if(fechaFinalFiltro != null){
                        filtroAplicable += ","+fechaFinalFiltroStr+"";
                    }else{
                        filtroAplicable += ",NA";
                    }
                    if(idVendedor > 0){
                        filtroAplicable += ","+idVendedor+"";
                    }else{
                        filtroAplicable += ",NA";
                    }
                    if(idCliente > 0){
                        filtroAplicable += ","+idCliente+"";
                    }else{
                        filtroAplicable += ",NA";
                    }
                }
                //////--
                reporteConfigurableDto.setFiltros(filtroAplicable);
                reporteConfigurableDto.setMaxTiempoAtras(maxTiempoAtras);
               
                
                try{
                    new ReporteConfigurableDaoImpl(user.getConn()).update(reporteConfigurableDto.createPk(), reporteConfigurableDto);

                    out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }
                
            }else if(mode.equals("2")){//para borrar el tipo de reporte configurable
                ReporteConfigurableBO reporteConfigurableBO = new ReporteConfigurableBO(idReporteConfigurable,user.getConn());
                ReporteConfigurable reporteConfigurableDto = reporteConfigurableBO.getReporteConfigurable();                                
                reporteConfigurableDto.setIdEstatus(2);                
                try{
                    new ReporteConfigurableDaoImpl(user.getConn()).update(reporteConfigurableDto.createPk(), reporteConfigurableDto);

                    out.print("<!--EXITO-->Registro Eliminado satisfactoriamente");
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo eliminar el registro. Informe del error al administrador del sistema: " + ex.toString());
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
                
                //validamos si ya existe el reporte configurado
                int reporteExistente = 0;
                ReporteConfigurable reporteConfigurableDtoExistente = null;
                try{
                    if (idTipoReporteSeleccion!=ReportBO.EMPLEADO_INVENTARIO_REPORT){
                        reporteConfigurableDtoExistente = new ReporteConfigurableDaoImpl(user.getConn()).findByDynamicWhere(" ID_TIPO_REPORTE = "+idTipoReporteSeleccion + " AND ID_EMPRESA = "+idEmpresa, null)[0];
                        if(reporteConfigurableDtoExistente != null){
                            if(reporteConfigurableDtoExistente.getIdEstatus() == 1)
                                reporteExistente = 1; //si esta existente y esta activo
                            else if(reporteConfigurableDtoExistente.getIdEstatus() == 2)
                                reporteExistente = 2; //si esta existente pero esta inactivo
                        }     
                    }
                }catch(Exception e){}
                
                if(reporteExistente == 1){
                    out.print("<!--ERROR-->Reporte Configurado Previamente.<br/>");
                }else{
                    /**
                     * Creamos el registro de reporte
                     */
                    
                    ReporteConfigurable reporteConfigurableDto = null;
                    if(reporteExistente == 2){//si existe y esta inactivo solo recuperamos su valores para activarlo                        
                        reporteConfigurableDto = reporteConfigurableDtoExistente;
                    }else if(reporteExistente == 0){//creamos uno nuevo
                        reporteConfigurableDto = new ReporteConfigurable();
                    }
                    
                    
                    ReporteConfigurableDaoImpl reporteConfigurablesDaoImpl = new ReporteConfigurableDaoImpl(user.getConn());

                    reporteConfigurableDto.setIdEmpresa(idEmpresa);
                    reporteConfigurableDto.setIdEstatus(1);
                    reporteConfigurableDto.setIdTipoReporte(idTipoReporteSeleccion);
                    /////*ARMAMOS EL STRING DE LOS DIAS A ENVIAR EL REPORTE:
                    String diasEnviarNotificaciones = "";
                    if(domingoReporte > 0)
                        diasEnviarNotificaciones += domingoReporte+",";
                    if(lunesReporte > 0)
                        diasEnviarNotificaciones += lunesReporte+",";
                    if(martesReporte > 0)
                        diasEnviarNotificaciones += martesReporte+",";
                    if(miercolesReporte > 0)
                        diasEnviarNotificaciones += miercolesReporte+",";
                    if(juevesReporte > 0)
                        diasEnviarNotificaciones += juevesReporte+",";
                    if(viernesReporte > 0)
                        diasEnviarNotificaciones += viernesReporte+",";
                    if(sabadoReporte > 0)
                        diasEnviarNotificaciones += sabadoReporte+",";
                    if(diarioReporte > 0)
                        diasEnviarNotificaciones += diarioReporte+",";                
                    /////*
                    reporteConfigurableDto.setIdDias(diasEnviarNotificaciones);
                    ////PARA RECUPERAR LOS CORREOS VALIDOS
                    int contador = 0;
                    String correosGuardar = "";
                    for(String cor : listCorreosValidos ){
                        if(contador > 0){
                            correosGuardar += ",";
                        }
                        correosGuardar += cor;
                        contador++;
                    }
                    reporteConfigurableDto.setCorreos(correosGuardar);
                    ////
                    
                     ////
                    //////--CARGAMOS LA FECHA DEL FILTRO
                    String filtroAplicable = null;
                    if(idTipoReporteSeleccion == 30 || idTipoReporteSeleccion == 27 || idTipoReporteSeleccion == 21 || idTipoReporteSeleccion == 40 || idTipoReporteSeleccion == 45){//REPORTES A LOS CUALES SE LES APLICA EL FILTRO POR FECHA                    
                        if(fechaInicialfiltro != null){
                            filtroAplicable = fechaInicialfiltroStr+"";
                        }else{
                            filtroAplicable = "NA";
                        }
                        if(fechaFinalFiltro != null){
                            filtroAplicable += ","+fechaFinalFiltroStr+"";
                        }else{
                            filtroAplicable += ",NA";
                        }
                        if(idVendedor > 0){
                            filtroAplicable += ","+idVendedor+"";
                        }else{
                            filtroAplicable += ",NA";
                        }
                        if(idCliente > 0){
                            filtroAplicable += ","+idCliente+"";
                        }else{
                            filtroAplicable += ",NA";
                        }
                    }
                    //////--
                    reporteConfigurableDto.setFiltros(filtroAplicable);
                    reporteConfigurableDto.setMaxTiempoAtras(maxTiempoAtras);


                    if(reporteExistente == 2){//si existe y esta inactivo solo recuperamos su valores para activarlo
                        reporteConfigurablesDaoImpl.update(reporteConfigurableDto.createPk(), reporteConfigurableDto);
                        out.print("<!--EXITO-->Registro actualizado satisfactoriamente.<br/>");
                    }else if(reporteExistente == 0){//creamos uno nuevo
                        /**
                         * Realizamos el insert
                         */
                        reporteConfigurablesDaoImpl.insert(reporteConfigurableDto);
                        out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
                    }
                }
            
            }catch(Exception e){
                e.printStackTrace();
                msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
            }
        }
    }else{
        out.print("<!--ERROR-->"+msgError);
    }

%>