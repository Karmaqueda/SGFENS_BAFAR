<%-- 
    Document   : catCargaManual_ajax
    Created on : 14/03/2015, 01:45:08 PM
    Author     : ISCesar
--%>



<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.tsp.sct.dao.jdbc.SmsAgendaDestinatarioDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SmsAgendaDestinatario"%>
<%@page import="com.tsp.sct.importa.ReadDestinatariosSMS"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.config.Configuration"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    int idUsuario = user.getUser().getIdUsuarios();
    String nombreArchivoDefault = "cargaSMSDestinatarios.xls";
    
    /*
    Parametros
    */
    String nombreArchivo = "";
    int id_sms_agenda_grupo = 0;
    String destinatarios_manual = "";
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";    
    nombreArchivo = request.getParameter("nombreArchivo")!=null?new String(request.getParameter("nombreArchivo").getBytes("ISO-8859-1"),"UTF-8"):"";
    destinatarios_manual = request.getParameter("destinatarios_manual")!=null?new String(request.getParameter("destinatarios_manual").getBytes("ISO-8859-1"),"UTF-8"):"";
    try{
        id_sms_agenda_grupo = Integer.parseInt(request.getParameter("id_sms_agenda_grupo"));
    }catch(Exception ex){} 
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    
    if(StringManage.getValidString(nombreArchivo).length()<=0
            && StringManage.getValidString(destinatarios_manual).length()<=0){
        msgError += "<ul>Debes subir un archivo con el formato adecuado o capturar en el campo Manual.";
    }
                       
    if(msgError.equals("")){    
         
        try {                

            /**
             * Creamos el registro de Carga del Archivo
             */
            //CargaXls cargaXlsDto = new CargaXls();
            //CargaXlsDaoImpl cargaXlsDaoImpl = new CargaXlsDaoImpl(user.getConn());             
            ReadDestinatariosSMS readXls = new ReadDestinatariosSMS(user);         


            //cargaXlsDto.setIdEmpresa((int)idEmpresa);
            //cargaXlsDto.setIdUsuario((int)idUsuario);
            //cargaXlsDto.setNombreArchivo(nombreArchivo);
            //cargaXlsDto.setFecha(new Date());

            //Carga 1- CARGA COMPLETA, 2- CARGA INCOMPLETA (CON ERRORES), 3- OTRO
            int estatuscarga = 3;        
            String logErroresInsertado = "";     
            String logErroresLeer = "";     
            String msgUsuario = "";

            //Leemos archivo de excel
            if (StringManage.getValidString(nombreArchivo).length()>0){
                try{                
                    Empresa empresaDto = new EmpresaBO(user.getUser().getIdEmpresa(),user.getConn()).getEmpresa();

                    Configuration appConfig = new Configuration();
                    String ourRepositoryDirectory = appConfig.getApp_content_path();

                    String path = ourRepositoryDirectory += empresaDto.getRfc() + "/" + nombreArchivoDefault;

                    //Leemos e insertamos nuevos datos
                    logErroresLeer = readXls.leerArchivoExcelDestinatariosSMS(path, (int)idEmpresa); 

                    if(logErroresLeer.equals("")){

                        //insertar datos  
                        logErroresInsertado = readXls.insertaDestinatariosSMSExcel(idEmpresa, id_sms_agenda_grupo);

                          //errores al guardar
                        if(!logErroresInsertado.equals("")){ 
                            estatuscarga = 2;
                            msgUsuario = "Carga de archivo con errores , revisar Log.";
                        }else if(logErroresInsertado.equals("")){
                            estatuscarga = 1;
                            msgUsuario = "Carga de archivo realizada sin errores.";
                        }else{
                            estatuscarga = 3;
                        }                        
                    }else{//errores al leer

                      if(!logErroresLeer.equals("")){ 
                            estatuscarga = 2;
                            msgUsuario = " Lectura de archivo con errores , revisar Log.";
                        }else if(logErroresLeer.equals("")){
                            estatuscarga = 1;
                            msgUsuario = " Lectura de archivo realizada sin errores. ";
                        }else{
                            estatuscarga = 3;
                        }  

                        //solo para insertar en log
                        logErroresInsertado = logErroresLeer;
                    }

                }catch(Exception e){
                    System.out.println("Error al insertar datos desde archivo Excel");
                    e.printStackTrace();
                }


                //cargaXlsDto.setIdEstatusCarga(estatuscarga);   
                //cargaXlsDto.setLogText(logErroresInsertado);
                //cargaXlsDto.setIdTipoCarga(1); //1-> Conceptos

                /**
                 * Realizamos el insert
                 */
               //cargaXlsDaoImpl.insert(cargaXlsDto);
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
                                errorLecturaManual += " Registro no creado: " + ex.toString();
                            }
                            col++;
                        }
                        
                        if (id_sms_agenda_grupo>0) //si selecciono un grupo, lo asignamos a el registro
                            smsAgendaDestinatario.setIdSmsAgendaGrupo(id_sms_agenda_grupo);
                        
                        if (!errorRegistro && smsAgendaDestinatario.getNumeroCelular()!=null){
                            listAgendaDestinatariosManual.add(smsAgendaDestinatario);
                        }
                        
                    }
                }
                
                //Si todo fue bien, registramos
                if (StringManage.getValidString(errorLecturaManual).equals("")){
                    for (SmsAgendaDestinatario item : listAgendaDestinatariosManual ){
                        smsAgendaDestinatarioDao.insert(item);
                    }
                    msgUsuario += "<ul>Carga de campo Manual realizado con éxito.";
                }else{
                    logErroresInsertado += errorLecturaManual;
                }
            }

            if (logErroresInsertado.equals("")){
                out.print("<!--EXITO-->Estatus Proceso:<br/>"+ msgUsuario +"<br/>");
            }else{
                out.print("<!--ERROR-->"+logErroresInsertado);
            }

        }catch(Exception e){
            e.printStackTrace();
            msgError += "Ocurrio un error al importar los datos: " + e.toString() ;
        }
        
    }else{
        out.print("<!--ERROR-->"+msgError);
    }

%>
