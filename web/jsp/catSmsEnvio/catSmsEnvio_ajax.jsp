<%-- 
    Document   : catSmsEnvio_ajax
    Created on : 24/03/2016, 01:27:38 PM
    Author     : ISCesar
--%>
<%@page import="java.util.Calendar"%>
<%@page import="com.tsp.sct.dao.jdbc.SmsAgendaDestinatarioDaoImpl"%>
<%@page import="com.tsp.sct.importa.ReadDestinatariosSMS"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.dao.dto.SmsAgendaDestinatario"%>
<%@page import="com.tsp.sct.bo.SmsAgendaDestinatarioBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProspecto"%>
<%@page import="com.tsp.sct.bo.SGProspectoBO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.tsp.sct.util.SmsLengthCalculator"%>
<%@page import="com.tsp.sct.util.GenericMethods"%>
<%@page import="com.tsp.sct.dao.dto.VistaPedidosConAdeudos"%>
<%@page import="com.tsp.sct.bo.VistaPedidosConAdeudoBO"%>
<%@page import="com.tsp.sgfens.sesion.SmsEnvioDestinatariosSesion"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sgfens.sesion.SmsEnvioSesion"%>
<%@page import="com.tsp.sct.dao.dto.SmsPlantilla"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.bo.SmsPlantillaBO"%>
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
    *        1 = Seleccionar/Consultar Plantilla
    *        2 = Contabiliza contenido SMS
    *        3 = Valida y Guarda Sesion de Envio SMS
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
        //1 = Seleccionar/Consultar Plantilla
        int idPlantilla = -1;
        try{ idPlantilla = Integer.parseInt(request.getParameter("id_plantilla")); }catch(Exception e){}
        
        SmsPlantilla plantillaDto = new SmsPlantillaBO(idPlantilla,user.getConn()).getSmsPlantilla();
        
        System.out.println("\nID Plantilla consultar: " +idPlantilla);
        
        String jsonOutput = gson.toJson(plantillaDto);
        msgExitoExtra = jsonOutput;
    }else if (mode==2){
        //2 = contabiliza contenido de SMS
        String contenido = request.getParameter("contenido")!=null?new String(request.getParameter("contenido").getBytes("ISO-8859-1"),"UTF-8"):"";
        SmsLengthCalculator smsLengthCalculator = new SmsLengthCalculator();
        int partesSms = smsLengthCalculator.getPartCount(contenido);
        int restantesParteActual = 0;
        
        int[] data = new int[2];
        data[0] =  restantesParteActual;
        data[1] =  partesSms;
        
        //Usamos JSON para retornar el objeto respuesta
        String jsonOutput = gson.toJson(data);
        msgExitoExtra = jsonOutput;
    }else if (mode==3){
        // 3 = Valida y Guarda Sesion de Envio SMS
        if (smsEnvioSesion==null)
            smsEnvioSesion = new SmsEnvioSesion(user.getConn());
        
        //limpiamos lista de destinatarios
        smsEnvioSesion.getListaDestinatarios().clear();
        
        //Parametros
        int clientes = -1;
        String ids_clientes_manual = "";
        int prospectos = -1;
        String ids_prospectos_manual = "";
        
        int select_agenda =-1;
        String ids_smsdestinatarios_manual = "";
        int idSmsGrupoDest = -1;
        
        String nombreArchivo = "";
        String destinatarios_manual = "";
        
        String tipo_envio = "";
        Date fechaHoraEnvioProgramado = null;
        String hora_envio = "";
        
        int idSmsPlantilla = -1;
        String asunto ="";
        String contenido =""; 
        
        try{
            clientes = Integer.parseInt(request.getParameter("clientes"));
        }catch(NumberFormatException ex){}
        ids_clientes_manual = request.getParameter("ids_clientes_manual")!=null?new String(request.getParameter("ids_clientes_manual").getBytes("ISO-8859-1"),"UTF-8"):"";
        try{
            prospectos = Integer.parseInt(request.getParameter("prospectos"));
        }catch(NumberFormatException ex){}
        ids_prospectos_manual = request.getParameter("ids_prospectos_manual")!=null?new String(request.getParameter("ids_prospectos_manual").getBytes("ISO-8859-1"),"UTF-8"):"";
        try{
            select_agenda = Integer.parseInt(request.getParameter("select_agenda"));
        }catch(NumberFormatException ex){}
        ids_smsdestinatarios_manual = request.getParameter("ids_smsdestinatarios_manual")!=null?new String(request.getParameter("ids_smsdestinatarios_manual").getBytes("ISO-8859-1"),"UTF-8"):"";
        try{
            idSmsGrupoDest = Integer.parseInt(request.getParameter("select_grupo_dest_sms"));
        }catch(NumberFormatException ex){}
        nombreArchivo = request.getParameter("nombreArchivo")!=null?new String(request.getParameter("nombreArchivo").getBytes("ISO-8859-1"),"UTF-8"):"";
        destinatarios_manual = request.getParameter("destinatarios_manual")!=null?new String(request.getParameter("destinatarios_manual").getBytes("ISO-8859-1"),"UTF-8"):"";
        tipo_envio = request.getParameter("tipo_envio")!=null?new String(request.getParameter("tipo_envio").getBytes("ISO-8859-1"),"UTF-8"):"";
        try{ fechaHoraEnvioProgramado = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fecha_envio"));}catch(Exception e){}
        hora_envio = request.getParameter("hora_envio")!=null?new String(request.getParameter("hora_envio").getBytes("ISO-8859-1"),"UTF-8"):"";
        try{
            idSmsPlantilla = Integer.parseInt(request.getParameter("id_plantilla"));
        }catch(NumberFormatException ex){}
        asunto = request.getParameter("asunto")!=null?new String(request.getParameter("asunto").getBytes("ISO-8859-1"),"UTF-8"):"";
        contenido = request.getParameter("contenido")!=null?new String(request.getParameter("contenido").getBytes("ISO-8859-1"),"UTF-8"):"";    
        
        //Validaciones y procesos basicos
        if (clientes==3){
            if (StringManage.getValidString(ids_clientes_manual).length()<=0){
                msgError+="<ul>Seleccionó Agregar Clientes Manualmente pero no eligió alguno. Cambie la opción a 'Ninguno' o agregue al menos uno.";
            }
        }
        if (prospectos==3){
            if (StringManage.getValidString(ids_prospectos_manual).length()<=0){
                msgError+="<ul>Seleccionó Agregar Prospectos Manualmente pero no eligió alguno. Cambie la opción a 'Ninguno' o agregue al menos uno.";
            }
        }
        if (select_agenda==3){
            if (StringManage.getValidString(ids_smsdestinatarios_manual).length()<=0){
                msgError+="<ul>Seleccionó Agregar Contactos de Agenda Manualmente pero no eligió alguno. Cambie la opción a 'Ninguno' o agregue al menos uno.";
            }
        }
        if (select_agenda==1){
            if (idSmsGrupoDest<=0){
                msgError+="<ul>Seleccionó Agregar Grupo de Agenda pero no eligió alguno. Cambie la opción a 'Ninguno' o seleccione un Grupo.";
            }
        }
        if (tipo_envio.length()>0){
            if (tipo_envio.equals("programado")){
                if (fechaHoraEnvioProgramado==null){
                    msgError+="<ul>No indico una Fecha válida para programar el envío del mensaje.";
                }
                if (!gc.validarConRegex("(?:[01]\\d|2[0123]):(?:[012345]\\d)", hora_envio)){
                    msgError+="<ul>No indico una Hora y Minutos válidos para programar el envío del mensaje. Debe tener formato de 24 horas hh:mm .";
                }
            }
        }else{
            msgError+="<ul>Debe seleccionar un tipo de Envío: Inmediato o Programado.";
        }
        if(!gc.isValidString(asunto, 1, 50))
            msgError += "<ul>El dato 'asunto' es requerido.";
        if(!gc.isValidString(contenido, 1, 480))
            msgError += "<ul>El dato 'contenido' es requerido.";
        
        //Guardar
        if (msgError.equals("")){
            try{
            
                //adjuntamos Hora a fecha programada en caso de ser usada
                if (tipo_envio.equals("programado") && fechaHoraEnvioProgramado!=null){
                    String[] horaPartes = hora_envio.split(":"); // hh:mm
                    Calendar calAux = Calendar.getInstance();
                    calAux.setTime(fechaHoraEnvioProgramado);
                    calAux.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horaPartes[0])); //asignamos hora
                    calAux.set(Calendar.MINUTE, Integer.parseInt(horaPartes[1])); //asignamos minutos
                    
                    fechaHoraEnvioProgramado = calAux.getTime();
                }
                
                String msgAdvertencia = "";
                if (clientes>0){
                    ClienteBO clienteBO = new ClienteBO(user.getConn());
                    Cliente[] clientesDto = new Cliente[0];
                    switch (clientes) {
                        case 1: //Todos los clientes con celular registrado
                            clientesDto = clienteBO.findClientes(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 AND (CELULAR IS NOT NULL AND CELULAR!='' AND CELULAR!='0000000000' )");
                            break;
                        case 2: //Con Adeudo
                            List<Cliente> clientesListAux = new ArrayList<Cliente>();
                            VistaPedidosConAdeudoBO vistaPedidoBO = new VistaPedidosConAdeudoBO(user.getConn());
                            VistaPedidosConAdeudos[] vistaPedidoDto = vistaPedidoBO.findPedidosConAdeudo(-1, idEmpresa,0, 0,"");
                            for (VistaPedidosConAdeudos vistaPedidoDeuda : vistaPedidoDto){
                                if (vistaPedidoDeuda.getIdCliente()>0){
                                    Cliente clienteDto = clienteBO.findClientebyId(vistaPedidoDeuda.getIdCliente());
                                    if (StringManage.getValidString(clienteDto.getCelular()).length()>0 && !clienteDto.getCelular().equals("0000000000") )
                                        clientesListAux.add(clienteDto);
                                }
                            }
                            if (clientesListAux.size()>0){ //pasamos lista a Array
                                clientesDto = new Cliente[clientesListAux.size()];
                                clientesDto = clientesListAux.toArray(new Cliente[0]);
                            }
                            break;
                        case 3: //Seleccion Manual
                            if (StringManage.getValidString(ids_clientes_manual).length()>0){
                                //quitamos ultima coma, para poder usarlo en query
                                if (ids_clientes_manual.endsWith(",")){
                                    ids_clientes_manual = ids_clientes_manual.substring(0, ids_clientes_manual.length()-1);
                                }
                                clientesDto = clienteBO.findClientes(-1, idEmpresa, 0, 0, " AND ID_CLIENTE IN ( " + StringManage.getValidString(ids_clientes_manual) + ") ");
                            }
                            break;
                    }
                    if (clientesDto!=null && clientesDto.length>0){
                        for (Cliente clienteDto : clientesDto){
                            if (gc.isCelularMexico(clienteDto.getCelular())){
                                SmsEnvioDestinatariosSesion smsEnvioDestinatariosSesion = new SmsEnvioDestinatariosSesion();
                                smsEnvioDestinatariosSesion.setNombre(StringManage.getValidString(clienteDto.getRazonSocial()));
                                smsEnvioDestinatariosSesion.setNumCelular(StringManage.getValidString(clienteDto.getCelular()));
                                smsEnvioDestinatariosSesion.setIdCliente(clienteDto.getIdCliente());
                                //revisar funcion
                                if (!smsEnvioSesion.getListaDestinatarios().contains(smsEnvioDestinatariosSesion)){
                                    //solo si no ha sido agregado previamente exactamente el mismo registro
                                    smsEnvioSesion.getListaDestinatarios().add(smsEnvioDestinatariosSesion);
                                }
                            }else{
                                msgAdvertencia += "<ul>El cliente '"+StringManage.getValidString(clienteDto.getRazonSocial()) +"' tiene un celular inválido: " + StringManage.getValidString(clienteDto.getCelular()
                                                + ". <a target='_blank' href='../catClientes/catClientes_form.jsp?acc=1&idCliente=" + clienteDto.getIdCliente() + "'>Corregir</a>");
                            }
                        }
                    }
                }
                
                if (prospectos>0){
                    SGProspectoBO prospectoBO = new SGProspectoBO(user.getConn());
                    SgfensProspecto[] prospectosDto = new SgfensProspecto[0];
                    switch (prospectos) {
                        case 1: //Todos los prospectos con celular registrado
                            prospectosDto = prospectoBO.findProspecto(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 AND (CELULAR IS NOT NULL AND CELULAR!='' AND CELULAR!='0000000000' )");
                            break;
                        case 3: //Seleccion Manual
                            if (StringManage.getValidString(ids_prospectos_manual).length()>0){
                                //quitamos ultima coma, para poder usarlo en query
                                if (ids_prospectos_manual.endsWith(",")){
                                    ids_prospectos_manual = ids_prospectos_manual.substring(0, ids_prospectos_manual.length()-1);
                                }
                                prospectosDto = prospectoBO.findProspecto(-1, idEmpresa, 0, 0, " AND ID_PROSPECTO IN ( " + StringManage.getValidString(ids_prospectos_manual) + ") ");
                            }
                            break;
                    }
                    if (prospectosDto!=null && prospectosDto.length>0){
                        for (SgfensProspecto prospectoDto : prospectosDto){
                            if (gc.isCelularMexico(prospectoDto.getCelular())){
                                SmsEnvioDestinatariosSesion smsEnvioDestinatariosSesion = new SmsEnvioDestinatariosSesion();
                                smsEnvioDestinatariosSesion.setNombre(StringManage.getValidString(prospectoDto.getRazonSocial()));
                                smsEnvioDestinatariosSesion.setNumCelular(StringManage.getValidString(prospectoDto.getCelular()));
                                smsEnvioDestinatariosSesion.setIdProspecto(prospectoDto.getIdProspecto());
                                //revisar funcion
                                if (!smsEnvioSesion.getListaDestinatarios().contains(smsEnvioDestinatariosSesion)){
                                    //solo si no ha sido agregado previamente exactamente el mismo registro
                                    smsEnvioSesion.getListaDestinatarios().add(smsEnvioDestinatariosSesion);
                                }
                            }else{
                                msgAdvertencia += "<ul>El prospecto '"+StringManage.getValidString(prospectoDto.getRazonSocial()) +"' tiene un celular inválido: " + StringManage.getValidString(prospectoDto.getCelular()
                                                + ". <a target='_blank' href='../catProspectos/catProspectos_form.jsp?acc=1&idProspecto=" + prospectoDto.getIdProspecto() + "'>Corregir</a>");
                            }
                        }
                    }
                }
                
                if (select_agenda>0){
                    SmsAgendaDestinatarioBO smsAgendaDestinatarioBO = new SmsAgendaDestinatarioBO(user.getConn());
                    SmsAgendaDestinatario[] smsAgendaDestinatariosDto = new SmsAgendaDestinatario[0];
                    switch (select_agenda) {
                        case 1: //Grupo
                            if (idSmsGrupoDest>0)
                                smsAgendaDestinatariosDto = smsAgendaDestinatarioBO.findSmsAgendaDestinatarios(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 AND id_sms_agenda_grupo="+idSmsGrupoDest);
                            break;
                        case 3: //Seleccion Manual
                            if (StringManage.getValidString(ids_smsdestinatarios_manual).length()>0){
                                //quitamos ultima coma, para poder usarlo en query
                                if (ids_smsdestinatarios_manual.endsWith(",")){
                                    ids_smsdestinatarios_manual = ids_smsdestinatarios_manual.substring(0, ids_smsdestinatarios_manual.length()-1);
                                }
                                smsAgendaDestinatariosDto = smsAgendaDestinatarioBO.findSmsAgendaDestinatarios(-1, idEmpresa, 0, 0, " AND id_sms_agenda_dest IN ( " + StringManage.getValidString(ids_smsdestinatarios_manual) + ") ");
                            }
                            break;
                    }
                    if (smsAgendaDestinatariosDto!=null && smsAgendaDestinatariosDto.length>0){
                        for (SmsAgendaDestinatario smsAgendaDestinatarioDto : smsAgendaDestinatariosDto){
                            if (gc.isCelularMexico(smsAgendaDestinatarioDto.getNumeroCelular())){
                                SmsEnvioDestinatariosSesion smsEnvioDestinatariosSesion = new SmsEnvioDestinatariosSesion();
                                smsEnvioDestinatariosSesion.setNombre(StringManage.getValidString(smsAgendaDestinatarioDto.getNombre()));
                                smsEnvioDestinatariosSesion.setNumCelular(StringManage.getValidString(smsAgendaDestinatarioDto.getNumeroCelular()));
                                smsEnvioDestinatariosSesion.setIdAgendaDest(smsAgendaDestinatarioDto.getIdSmsAgendaDest());
                                //revisar funcion
                                if (!smsEnvioSesion.getListaDestinatarios().contains(smsEnvioDestinatariosSesion)){
                                    //solo si no ha sido agregado previamente exactamente el mismo registro
                                    smsEnvioSesion.getListaDestinatarios().add(smsEnvioDestinatariosSesion);
                                }
                            }else{
                                msgAdvertencia += "<ul>El Contacto de Agenda SMS '"+StringManage.getValidString(smsAgendaDestinatarioDto.getNombre()) +"' tiene un celular inválido: " + StringManage.getValidString(smsAgendaDestinatarioDto.getNumeroCelular()
                                                + ". <a target='_blank' href='../catSmsAgenda/catSmsAgendaDestinatarios_form.jsp?acc=1&idSmsAgendaDestinatario=" + smsAgendaDestinatarioDto.getIdSmsAgendaDest()+ "'>Corregir</a>");
                            }
                        }
                    }
                }
                
                //----------------------
                        
                String nombreArchivoDefault = "cargaSMSDestinatarios.xls";
                String logErroresInsertado = "";     
                String logErroresLeer = "";     
                String msgUsuario = "";
                ReadDestinatariosSMS readXls = new ReadDestinatariosSMS(user);

                //Leemos archivo de excel
                if (StringManage.getValidString(nombreArchivo).length()>0){
                    try{                
                        Empresa empresaDto = new EmpresaBO(user.getUser().getIdEmpresa(),user.getConn()).getEmpresa();

                        Configuration appConfig = new Configuration();
                        String ourRepositoryDirectory = appConfig.getApp_content_path();

                        String path = ourRepositoryDirectory + empresaDto.getRfc() + "/" + nombreArchivoDefault;

                        //Leemos e insertamos nuevos datos
                        logErroresLeer = readXls.leerArchivoExcelDestinatariosSMS(path, (int)idEmpresa); 

                        if(logErroresLeer.equals("")){
                            //Por el momento NO se inserta en BD el contenido del archivo
                            
                            //insertar datos  
                            //logErroresInsertado = readXls.insertaDestinatariosSMSExcel(idEmpresa, idGrupo);
                            //errores al guardar
                            //if(!logErroresInsertado.equals("")){ 
                            //    msgUsuario = "Carga de archivo con errores , revisar Log.";
                            //}else if(logErroresInsertado.equals("")){
                            //    msgUsuario = "Carga de archivo realizada sin errores.";
                                for (SmsAgendaDestinatario item : readXls.getListAgendaDestinatarios()){
                                    SmsEnvioDestinatariosSesion smsEnvioDestinatariosSesion = new SmsEnvioDestinatariosSesion();
                                    smsEnvioDestinatariosSesion.setNombre(StringManage.getValidString(item.getNombre()));
                                    smsEnvioDestinatariosSesion.setNumCelular(StringManage.getValidString(item.getNumeroCelular()));
                                    smsEnvioDestinatariosSesion.setIdAgendaDest(item.getIdSmsAgendaDest());
                                    smsEnvioSesion.getListaDestinatarios().add(smsEnvioDestinatariosSesion);
                                }
                            //}                       
                        }else{//errores al leer

                            if(!logErroresLeer.equals("")){ 
                                msgUsuario += " <ul>Lectura de archivo con errores , revisar Log.";
                            }else if(logErroresLeer.equals("")){
                                msgUsuario += " <ul>Lectura de archivo realizada sin errores. ";
                            }

                            //solo para insertar en log
                            logErroresInsertado = logErroresLeer;
                        }

                    }catch(Exception e){
                        System.out.println("Error al cargar datos desde archivo Excel");
                        e.printStackTrace();
                    }

                }

                //Ahora validamos e insertamos registros de Destinataros Manual
                if (StringManage.getValidString(destinatarios_manual).length()>0){
                    SmsAgendaDestinatarioDaoImpl smsAgendaDestinatarioDao = new SmsAgendaDestinatarioDaoImpl(user.getConn());
                    List<SmsAgendaDestinatario> listAgendaDestinatariosManual = new ArrayList<SmsAgendaDestinatario>();
                    String errorLecturaManual = "";

                    //Primero solo validmos y agregamos a lista
                    String[] registros = destinatarios_manual.split(";");
                    for (String row : registros){
                        if (StringManage.getValidString(row).length()>0){

                            boolean errorRegistro = false;

                            SmsAgendaDestinatario smsAgendaDestinatario = new SmsAgendaDestinatario();
                            smsAgendaDestinatario.setIdEstatus(1);
                            smsAgendaDestinatario.setIdEmpresa(idEmpresa);

                            String[] column = row.split(",");
                            int col = 0;
                            for (String data : column ){
                                try{
                                    readXls.validaAsignaCelda(col, new String(), data, smsAgendaDestinatario);
                                }catch(Exception ex){
                                    errorRegistro = true;
                                    errorLecturaManual += "<ul> Destinatario Manual, registro con error: " + ex.getMessage();
                                }
                                col++;
                            }

                            //if (id_sms_agenda_grupo>0) //si selecciono un grupo, lo asignamos a el registro
                            //    smsAgendaDestinatario.setIdSmsAgendaGrupo(id_sms_agenda_grupo);

                            if (!errorRegistro && smsAgendaDestinatario.getNumeroCelular()!=null){
                                listAgendaDestinatariosManual.add(smsAgendaDestinatario);
                            }

                        }
                    }

                    //Si todo fue bien, registramos
                    if (StringManage.getValidString(errorLecturaManual).equals("")){
                        for (SmsAgendaDestinatario item : listAgendaDestinatariosManual ){
                            //Por el momento NO insertamos en BD los capturados manualmente
                            //smsAgendaDestinatarioDao.insert(item);
                            
                            SmsEnvioDestinatariosSesion smsEnvioDestinatariosSesion = new SmsEnvioDestinatariosSesion();
                            smsEnvioDestinatariosSesion.setNombre(StringManage.getValidString(item.getNombre()));
                            smsEnvioDestinatariosSesion.setNumCelular(StringManage.getValidString(item.getNumeroCelular()));
                            smsEnvioDestinatariosSesion.setIdAgendaDest(item.getIdSmsAgendaDest());
                            smsEnvioSesion.getListaDestinatarios().add(smsEnvioDestinatariosSesion);
                        }
                        msgUsuario += "<ul>Carga de campo Manual realizado con éxito.";
                    }else{
                        logErroresInsertado += errorLecturaManual;
                    }
                }
                
                if (!logErroresInsertado.equals("")){
                    msgAdvertencia += logErroresInsertado;
                }
                
                //------------------

                if (msgAdvertencia.length()>0)
                    msgError += msgAdvertencia;

                if (smsEnvioSesion.getListaDestinatarios().size()<=0){
                    msgError+= "<ul>No hay ningún destinatario válido.";
                }

                if (msgError.equals("")){
                    //asignamos datos restantes a objeto de sesion
                    smsEnvioSesion.setAsunto(asunto);
                    smsEnvioSesion.setContenido(contenido);
                    smsEnvioSesion.setIdEmpresaRemitente(idEmpresa);
                    if (idSmsPlantilla>0){
                        smsEnvioSesion.setIdPlantilla(idSmsPlantilla);
                    }
                    if (fechaHoraEnvioProgramado!=null){
                        smsEnvioSesion.setEnvioInmediato(false);
                        smsEnvioSesion.setFechaHoraEnvioProgramado(fechaHoraEnvioProgramado);
                    }else{
                        smsEnvioSesion.setEnvioInmediato(true);
                    }
                    //request.getSession().setAttribute("smsEnvioSesion", smsEnvioSesion);
                    msgExitoExtra = "Continuar.";
                }else{
                    //limpiamos listado de destinatarios en caso de un minimo error
                    smsEnvioSesion.getListaDestinatarios().clear();
                }
                
            }catch(Exception ex){
                msgError+= "<ul>Error inesperado: " + GenericMethods.exceptionStackTraceToString(ex);
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