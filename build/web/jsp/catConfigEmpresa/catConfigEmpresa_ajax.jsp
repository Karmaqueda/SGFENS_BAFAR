<%-- 
    Document   : catConfigEmpresa_ajax
    Created on : 10/12/2014, 01:11:16 PM
    Author     : 578
--%>

<%@page import="com.tsp.sgfens.ogro.cron.Programacion3"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaConfigDaoImpl"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.bo.EmpresaConfigBO"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaConfig"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    /*
     * mode:
     * 1: Edición de numero de Registro patronal.
     * 
     */
    
   
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
   
    String domingo = request.getParameter("domingo")!=null?new String(request.getParameter("domingo").getBytes("ISO-8859-1"),"UTF-8"):""; 
    String lunes = request.getParameter("lunes")!=null?new String(request.getParameter("lunes").getBytes("ISO-8859-1"),"UTF-8"):""; 
    String martes = request.getParameter("martes")!=null?new String(request.getParameter("martes").getBytes("ISO-8859-1"),"UTF-8"):""; 
    String miercoles = request.getParameter("miercoles")!=null?new String(request.getParameter("miercoles").getBytes("ISO-8859-1"),"UTF-8"):""; 
    String jueves = request.getParameter("jueves")!=null?new String(request.getParameter("jueves").getBytes("ISO-8859-1"),"UTF-8"):""; 
    String viernes = request.getParameter("viernes")!=null?new String(request.getParameter("viernes").getBytes("ISO-8859-1"),"UTF-8"):""; 
    String sabado = request.getParameter("sabado")!=null?new String(request.getParameter("sabado").getBytes("ISO-8859-1"),"UTF-8"):"";     
    
    String hora = request.getParameter("hora")!=null?new String(request.getParameter("hora").getBytes("ISO-8859-1"),"UTF-8"):"";     
        
    int idEmpresaConfig = -1 ;
    int estatus = 0;
    try{        
        EmpresaConfigBO empresaBO = new EmpresaConfigBO(user.getConn());
        EmpresaConfig empresaConfig = empresaBO.findEmpresaConfigbyIdEMpresa(idEmpresa);
        idEmpresaConfig = empresaConfig.getIdEmpresa();
    }catch(Exception e){
    }
    
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();  
    if(!gc.isValidString(hora, 1, 8))
        msgError += "<ul>El dato 'Hora' es requerido";
    if(!gc.isTime(hora))
        msgError += "<ul>Introducir Hora Formato HH:MM:SS";

    if(msgError.equals("")){
        if(idEmpresaConfig>0){
            if (mode.equals("horaCorteAuto")){
            /*
            * Editar
            */
                //System.out.println("EDITAR");
                EmpresaConfigBO empresaBO = new EmpresaConfigBO(idEmpresaConfig,user.getConn());
                EmpresaConfig empresaConfig = empresaBO.getEmpresaConfig();
                
                
                
                String diasCorte="";               
                 
                if(!domingo.trim().equals(""))
                    diasCorte += domingo+",";
                if(!lunes.trim().equals(""))
                    diasCorte += lunes+",";
                if(!martes.trim().equals(""))
                    diasCorte += martes+",";
                if(!miercoles.trim().equals(""))
                    diasCorte += miercoles+",";
                if(!jueves.trim().equals(""))
                    diasCorte += jueves+",";
                if(!viernes.trim().equals(""))
                    diasCorte += viernes+",";
                if(!sabado.trim().equals(""))
                    diasCorte += sabado+",";
                
                java.sql.Time timeValue = new java.sql.Time(formatter.parse(hora).getTime());
                
                empresaConfig.setDiasCorte(diasCorte);
                empresaConfig.setHoraCorte(timeValue);
                empresaConfig.setInventarioInicialAuto(estatus);
                
                
                try{
                    new EmpresaConfigDaoImpl(user.getConn()).update(empresaConfig.createPk(), empresaConfig); 
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
                 //System.out.println("NUEVO");
                /**
                 * Creamos el registro 
                 */
                EmpresaConfigBO empresaBO = new EmpresaConfigBO(user.getConn());
                EmpresaConfig empresaConfig = new EmpresaConfig();
                
                
                String diasCorte="";
               
                 
                if(!domingo.trim().equals(""))
                    diasCorte += domingo+",";
                if(!lunes.trim().equals(""))
                    diasCorte += lunes+",";
                if(!martes.trim().equals(""))
                    diasCorte += martes+",";
                if(!miercoles.trim().equals(""))
                    diasCorte += miercoles+",";
                if(!jueves.trim().equals(""))
                    diasCorte += jueves+",";
                if(!viernes.trim().equals(""))
                    diasCorte += viernes+",";
                if(!sabado.trim().equals(""))
                    diasCorte += sabado+",";
                
                
                java.sql.Time timeValue = new java.sql.Time(formatter.parse(hora).getTime());
               
                empresaConfig.setIdEmpresa(idEmpresa);
                empresaConfig.setDiasCorte(diasCorte);
                empresaConfig.setHoraCorte(timeValue);
                empresaConfig.setInventarioInicialAuto(estatus);
                
                /**
                 * Realizamos el insert
                 */
                new EmpresaConfigDaoImpl(user.getConn()).insert(empresaConfig);

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
