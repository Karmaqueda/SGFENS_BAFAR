<%-- 
    Document   : catHorarios_ajax
    Created on : 02/12/2015, 11:33:07 AM
    Author     : Cesar Martinez
--%>

<%@page import="com.tsp.sct.dao.dto.HorarioPk"%>
<%@page import="com.tsp.sct.dao.jdbc.HorarioDetalleDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.HorarioDetalle"%>
<%@page import="com.tsp.sct.bo.HorarioDetalleBO"%>
<%@page import="com.tsp.sct.dao.jdbc.HorarioDaoImpl"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page import="com.tsp.sct.dao.dto.Horario"%>
<%@page import="com.tsp.sct.bo.HorarioBO"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idHorario = -1;
    String nombreHorario ="";   
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idHorario = Integer.parseInt(request.getParameter("idHorario"));
    }catch(NumberFormatException ex){}
    nombreHorario = request.getParameter("nombreHorario")!=null?new String(request.getParameter("nombreHorario").getBytes("ISO-8859-1"),"UTF-8"):"";    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(nombreHorario, 1, 30))
        msgError += "<ul>El dato 'Nombre' es requerido.";     
    if(idHorario <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'Horario' es requerido";
   

        
    for(int i= 1;i<=7;i++){  
        
        String dia ="";
        int tolerancia = -1;
        int periodo = -1;
        String horaEntrada = "";
        String horaSalida = "";
        String salidaComida = "";
        String entradaComida = "";
        int diaDescanso = 0;

        if(i==1)
            dia = "LUNES";
        else if(i==2)
            dia = "MARTES";
        else if(i==3)
            dia = "MIERCOLES";
        else if(i==4)
            dia = "JUEVES";
        else if(i==5)
            dia = "VIERNES";
        else if(i==6)
            dia = "SABADO";
        else if(i==7)
            dia = "DOMINGO";
                
        try{
            tolerancia = Integer.parseInt(request.getParameter("tolerancia_"+dia));
        }catch(NumberFormatException ex){}
        try{
            periodo = Integer.parseInt(request.getParameter("periodo_"+dia));
        }catch(NumberFormatException ex){}
        
        horaEntrada = request.getParameter("entrada_"+dia)!=null?new String(request.getParameter("entrada_"+dia).getBytes("ISO-8859-1"),"UTF-8"):"";   
        horaSalida = request.getParameter("salida_"+dia)!=null?new String(request.getParameter("salida_"+dia).getBytes("ISO-8859-1"),"UTF-8"):"";   
        salidaComida = request.getParameter("salidaComida_"+dia)!=null?new String(request.getParameter("salidaComida_"+dia).getBytes("ISO-8859-1"),"UTF-8"):"";   
        entradaComida = request.getParameter("entradaComida_"+dia)!=null?new String(request.getParameter("entradaComida_"+dia).getBytes("ISO-8859-1"),"UTF-8"):"";   
                
        
        try{
            diaDescanso = Integer.parseInt(request.getParameter("descanso_"+dia));
        }catch(NumberFormatException ex){}
        
        if(diaDescanso==0){
        
            if (horaEntrada.equals("")) {
                //msgError = "<ul>El dato 'Hora de Entrada' es obligatorio. ";
                msgError = "Todos los datos son Obligatorios.";

            }        
            if (horaSalida.equals("")) {
               // msgError = "<ul>El dato 'Hora de Salida' es obligatorio. ";
                 msgError = "Todos los datos son Obligatorios.";
            }        
            if (tolerancia < 0) {
                //msgError = "<ul>El dato 'Tolerancia' debe ser cero o mayor a cero. ";
                 msgError = "Todos los datos son Obligatorios.";
            }        
            if (salidaComida.equals("")) {
               // msgError = "<ul>El dato 'Salida Comida'  es obligatorio. ";
                 msgError = "Todos los datos son Obligatorios.";
            }        
            if (entradaComida.equals("")) {
                //msgError = "<ul>El dato 'Entrada Comida' es obligatorio. ";
                 msgError = "Todos los datos son Obligatorios.";
            }        
            if (periodo < 0) {
               // msgError = "<ul>El dato 'Periodo' debe ser cero o mayor a cero. ";
                 msgError = "Todos los datos son Obligatorios.";
            }
        }
                    
    }
    
    if(msgError.equals("")){
        if(idHorario>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                try{
                    
                    
                    HorarioBO horarioBO = new HorarioBO(idHorario,user.getConn());
                    Horario horarioDto = horarioBO.getHorario();                
                
                
                    horarioDto.setNombreHorario(nombreHorario); 
                    horarioDto.setIdEstatus(estatus);               
                
                
                    new HorarioDaoImpl(user.getConn()).update(horarioDto.createPk(), horarioDto);
                    
                    //Detalle horario 
                
                    for(int i= 1;i<=7;i++){  

                        String dia ="";
                        int tolerancia = 0;
                        int periodo = 0;
                        int diaDescanso = 0;
                        String horaEntrada = "";
                        String horaSalida = "";
                        String salidaComida = "";
                        String entradaComida = "";
                        int idHorarioDetalle = 0;


                        if(i==1)
                            dia = "LUNES";
                        else if(i==2)
                            dia = "MARTES";
                        else if(i==3)
                            dia = "MIERCOLES";
                        else if(i==4)
                            dia = "JUEVES";
                        else if(i==5)
                            dia = "VIERNES";
                        else if(i==6)
                            dia = "SABADO";
                        else if(i==7)
                            dia = "DOMINGO";
                        
                        
                        try{
                            diaDescanso = Integer.parseInt(request.getParameter("descanso_"+dia));
                        }catch(NumberFormatException ex){}
                        try{
                            tolerancia = Integer.parseInt(request.getParameter("tolerancia_"+dia));
                        }catch(NumberFormatException ex){}
                        try{
                            periodo = Integer.parseInt(request.getParameter("periodo_"+dia));
                        }catch(NumberFormatException ex){}
                        try{
                            idHorarioDetalle = Integer.parseInt(request.getParameter("idHorarioDetalle_"+dia));
                        }catch(NumberFormatException ex){}                        

                        horaEntrada = request.getParameter("entrada_"+dia)!=null?new String(request.getParameter("entrada_"+dia).getBytes("ISO-8859-1"),"UTF-8"):"";   
                        horaSalida = request.getParameter("salida_"+dia)!=null?new String(request.getParameter("salida_"+dia).getBytes("ISO-8859-1"),"UTF-8"):"";   
                        salidaComida = request.getParameter("salidaComida_"+dia)!=null?new String(request.getParameter("salidaComida_"+dia).getBytes("ISO-8859-1"),"UTF-8"):"";   
                        entradaComida = request.getParameter("entradaComida_"+dia)!=null?new String(request.getParameter("entradaComida_"+dia).getBytes("ISO-8859-1"),"UTF-8"):"";   


                        try{

                             DateFormat sdf = new SimpleDateFormat("HH:mm:ss");

                             HorarioDetalleBO horarioDetalleBO = new HorarioDetalleBO(idHorarioDetalle,user.getConn());
                             HorarioDetalle horarioDetalle = horarioDetalleBO.getHorarioDetalle();
                             
                             
                             horarioDetalle.setDia(dia);                            
                             horarioDetalle.setTolerancia(tolerancia);                             
                             horarioDetalle.setPeriodoComida(periodo);
                             horarioDetalle.setDiaDescanso(diaDescanso);
                             try{
                                 horarioDetalle.setHoraEntrada(sdf.parse(horaEntrada+":00"));
                                 horarioDetalle.setHoraSalida(sdf.parse(horaSalida+":00"));
                                 horarioDetalle.setComidaSalida(sdf.parse(salidaComida+":00"));
                                 horarioDetalle.setComidaEntrada(sdf.parse(entradaComida+":00"));
                             }catch(Exception e){}
                             
                             
                             new HorarioDetalleDaoImpl(user.getConn()).update(horarioDetalle.createPk(),horarioDetalle);

                        }catch(Exception e){
                              System.out.println("Error al parsear hora");
                               throw new Exception("No se pudo parsear la hora al formato especificado");
                        }

                    }

                    //Fin detalle 
                    
                    
                    
                    

                    out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
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
                 * Creamos el registro
                 */
                Horario horarioDto = new Horario();
                HorarioDaoImpl horarioDaoImpl = new HorarioDaoImpl(user.getConn());
                
               
                horarioDto.setNombreHorario(nombreHorario);                                            
                horarioDto.setIdEmpresa(idEmpresa);
                horarioDto.setIdEstatus(estatus);

                 /**
                 * Realizamos el insert
                 */
                HorarioPk horarioPk = horarioDaoImpl.insert(horarioDto);
                                
                
                
                //Detalle horario 
                
                for(int i= 1;i<=7;i++){  
        
                    String dia ="";
                    int tolerancia = 0;
                    int periodo = 0;
                    int diaDescanso = 0;
                    String horaEntrada = "";
                    String horaSalida = "";
                    String salidaComida = "";
                    String entradaComida = "";


                    if(i==1)
                        dia = "LUNES";
                    else if(i==2)
                        dia = "MARTES";
                    else if(i==3)
                        dia = "MIERCOLES";
                    else if(i==4)
                        dia = "JUEVES";
                    else if(i==5)
                        dia = "VIERNES";
                    else if(i==6)
                        dia = "SABADO";
                    else if(i==7)
                        dia = "DOMINGO";

                    try{
                        tolerancia = Integer.parseInt(request.getParameter("tolerancia_"+dia));
                    }catch(NumberFormatException ex){}
                    try{
                        periodo = Integer.parseInt(request.getParameter("periodo_"+dia));
                    }catch(NumberFormatException ex){}
                    try{
                        diaDescanso = Integer.parseInt(request.getParameter("descanso_"+dia));
                    }catch(NumberFormatException ex){}

                    horaEntrada = request.getParameter("entrada_"+dia)!=null?new String(request.getParameter("entrada_"+dia).getBytes("ISO-8859-1"),"UTF-8"):"";   
                    horaSalida = request.getParameter("salida_"+dia)!=null?new String(request.getParameter("salida_"+dia).getBytes("ISO-8859-1"),"UTF-8"):"";   
                    salidaComida = request.getParameter("salidaComida_"+dia)!=null?new String(request.getParameter("salidaComida_"+dia).getBytes("ISO-8859-1"),"UTF-8"):"";   
                    entradaComida = request.getParameter("entradaComida_"+dia)!=null?new String(request.getParameter("entradaComida_"+dia).getBytes("ISO-8859-1"),"UTF-8"):"";   

                    
                    try{
                                               
                         DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        

                         HorarioDetalle horarioDetalle = new HorarioDetalle();
                         horarioDetalle.setIdHorario(horarioPk.getIdHorario());
                         horarioDetalle.setDia(dia);                         
                         horarioDetalle.setTolerancia(tolerancia);             
                         horarioDetalle.setPeriodoComida(periodo);
                         horarioDetalle.setDiaDescanso(diaDescanso);
                         try{
                             horarioDetalle.setHoraEntrada(sdf.parse(horaEntrada+":00"));
                             horarioDetalle.setHoraSalida(sdf.parse(horaSalida+":00"));
                             horarioDetalle.setComidaSalida(sdf.parse(salidaComida+":00"));
                             horarioDetalle.setComidaEntrada(sdf.parse(entradaComida+":00"));
                         }catch(Exception e){}
                         
                         
                         new HorarioDetalleDaoImpl(user.getConn()).insert(horarioDetalle);
                        
                    }catch(Exception e){
                          System.out.println("Error al parsear hora");
                           throw new Exception("No se pudo parsear la hora al formato especificado");
                    }

                }
                
                //Fin detalle 
                
                              

                out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
            
            }catch(Exception e){
                e.printStackTrace();
                msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
            }
        }
    }else{
        out.print("<!--ERROR-->"+msgError);
    }

%>