<%-- 
    Document   : catCrScoreDetalle_list
    Created on : 10/06/2016, 12:55:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.dao.jdbc.CrScoreDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrScore"%>
<%@page import="com.tsp.sct.bo.CrScoreBO"%>
<%@page import="com.tsp.sct.bo.CrFormularioCampoBO"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.bo.CrScoreDetalleBO"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.CrScoreDetalleDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrScoreDetalle"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String msgError="";
    String msgExitoExtra="";
    
    /*
    *   Modos:
    *       -1  =  No se eligio (ERROR)
    * 
    *        0 = Guardar Nuevo Score Detalle
    *        1 = Editar Score Detalle
    *        2 =  //reservado
    *        3 = Consultar Campos de Formulario para Select dinámico
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
    }else if (mode==0){
        // 0 = Guardar Nuevo Score Detalle
        
        //Parámetros
        int idFormularioCampo = -1;
        int idScore = -1;
        String valorExacto ="";
        double rangoMin = 0;
        double rangoMax = 0;
        int puntosScore = 0;
        int estatus = 2;//deshabilitado
        
        try{ idFormularioCampo = Integer.parseInt(request.getParameter("id_formulario_campo"));}catch(Exception ex){} 
        try{ idScore = Integer.parseInt(request.getParameter("id_score"));}catch(Exception ex){} 
        valorExacto = request.getParameter("valor_exacto")!=null?new String(request.getParameter("valor_exacto").getBytes("ISO-8859-1"),"UTF-8"):"";    
        try{ puntosScore = Integer.parseInt(request.getParameter("puntos_score"));}catch(Exception ex){} 
        try{ rangoMin = Double.parseDouble(request.getParameter("rango_min"));}catch(Exception ex){} 
        try{ rangoMax = Double.parseDouble(request.getParameter("rango_max"));}catch(Exception ex){} 
        try{ estatus = Integer.parseInt(request.getParameter("estatus"));}catch(Exception ex){} 
        
        if(idFormularioCampo<=0)
            msgError += "<ul>El dato 'Campo a comparar' es requerido, debe elegir uno válido."; 
        if(idScore<=0)
            msgError += "<ul>El dato 'Score' es requerido, debe elegir uno válido."; 
        if(puntosScore==0)
            msgError += "<ul>El dato 'Puntos Score' es requerido, debe ser un valor positivo o negativo."; 
        if (StringManage.getValidString(valorExacto).length()<=0){
            // si no se especifico un valor exacto, entonces debe tener un rango
            if (rangoMin==0 && rangoMax==0){
                // no se especifico tampoco ningun dato de rango
                msgError += "<ul>Es requerido especificar un criterio, ya sea valor exacto o un rango de valores numericos permitidos.";
            } 
        }else{
            // si se especifico un Valor exacto, no deberia tener un rango
            if (rangoMin>0 || rangoMax>0){
                // no se especifico tampoco ningun dato de rango
                msgError += "<ul>Solo debe existir un criterio, ya sea valor exacto o un rango de valores numericos permitidos. No se permiten ambos.";
            } 
        }
        
        CrScore crScoreDto = new CrScoreBO(idScore, user.getConn()).getCrScore();
        if (crScoreDto==null){
            msgError += "<ul>El Score padre seleccionado no existe en base de datos.";
        }
    
        if (msgError.equals("")){
            CrScoreDetalle crScoreDetalleDto = new CrScoreDetalle();
            CrScoreDetalleDaoImpl crScoreDetalleDaoImpl = new CrScoreDetalleDaoImpl(user.getConn());

            crScoreDetalleDto.setIdFormularioCampo(idFormularioCampo);
            crScoreDetalleDto.setIdScore(idScore);
            crScoreDetalleDto.setValorExacto(StringManage.getValidString(valorExacto));
            crScoreDetalleDto.setRangoMin(rangoMin);
            crScoreDetalleDto.setRangoMax(rangoMax);
            crScoreDetalleDto.setPuntosScore(puntosScore);
            crScoreDetalleDto.setIdEmpresa(idEmpresa);
            crScoreDetalleDto.setIdEstatus(estatus);

            try{
                crScoreDetalleDaoImpl.insert(crScoreDetalleDto); 
                
                crScoreDto.setFechaHrUltimaEdicion(new Date());
                crScoreDto.setIdUsuarioEdicion(user.getUser().getIdUsuarios());
                new CrScoreDaoImpl(user.getConn()).update(crScoreDto.createPk(), crScoreDto);

                msgExitoExtra += "Registro creado satisfactoriamente";
            }catch(Exception ex){
                msgError += "No se pudo crear el registro. Informe del error al administrador del sistema: " + ex.toString();
                ex.printStackTrace();
            }
        }
    }else if (mode==1){
        // 1 = Editar Score Detalle
        
        //Parámetros
        int idCrScoreDetalle = -1;
        int idFormularioCampo = -1;
        int idScore = -1;
        String valorExacto ="";
        double rangoMin = 0;
        double rangoMax = 0;
        int puntosScore = 0;
        int estatus = 2;//deshabilitado
        
        try{
            idCrScoreDetalle = Integer.parseInt(request.getParameter("idCrScoreDetalle"));
        }catch(Exception ex){}
        try{ idFormularioCampo = Integer.parseInt(request.getParameter("id_formulario_campo"));}catch(Exception ex){} 
        try{ idScore = Integer.parseInt(request.getParameter("id_score"));}catch(Exception ex){} 
        valorExacto = request.getParameter("valor_exacto")!=null?new String(request.getParameter("valor_exacto").getBytes("ISO-8859-1"),"UTF-8"):"";    
        try{ puntosScore = Integer.parseInt(request.getParameter("puntos_score"));}catch(Exception ex){} 
        try{ rangoMin = Double.parseDouble(request.getParameter("rango_min"));}catch(Exception ex){} 
        try{ rangoMax = Double.parseDouble(request.getParameter("rango_max"));}catch(Exception ex){} 
        try{ estatus = Integer.parseInt(request.getParameter("estatus"));}catch(Exception ex){} 
        
        if(idCrScoreDetalle <= 0 )
            msgError += "<ul>El dato ID 'CrScoreDetalle' es requerido para ediciones";
        if(idFormularioCampo<=0)
            msgError += "<ul>El dato 'Campo a comparar' es requerido, debe elegir uno válido."; 
        if(idScore<=0)
            msgError += "<ul>El dato 'Score' es requerido, debe elegir uno válido."; 
        if(puntosScore<=0)
            msgError += "<ul>El dato 'Puntos Score' es requerido, debe ser un valor positivo o negativo."; 
        if (StringManage.getValidString(valorExacto).length()<=0){
            // si no se especifico un valor exacto, entonces debe tener un rango
            if (rangoMin==0 && rangoMax==0){
                // no se especifico tampoco ningun dato de rango
                msgError += "<ul>Es requerido especificar un criterio, ya sea valor exacto o un rango de valores numericos permitidos.";
            } 
        }else{
            // si se especifico un Valor exacto, no deberia tener un rango
            if (rangoMin>0 || rangoMax>0){
                // no se especifico tampoco ningun dato de rango
                msgError += "<ul>Solo debe existir un criterio, ya sea valor exacto o un rango de valores numericos permitidos. No se permiten ambos.";
            } 
        }
        
        CrScore crScoreDto = new CrScoreBO(idScore, user.getConn()).getCrScore();
        if (crScoreDto==null){
            msgError += "<ul>El Score padre seleccionado no existe en base de datos.";
        }

        if (msgError.equals("")){
            CrScoreDetalleBO crScoreDetalleBO = new CrScoreDetalleBO(idCrScoreDetalle,user.getConn());
            CrScoreDetalle crScoreDetalleDto = crScoreDetalleBO.getCrScoreDetalle();

            crScoreDetalleDto.setIdFormularioCampo(idFormularioCampo);
            crScoreDetalleDto.setIdScore(idScore);
            crScoreDetalleDto.setValorExacto(StringManage.getValidString(valorExacto));
            crScoreDetalleDto.setRangoMin(rangoMin);
            crScoreDetalleDto.setRangoMax(rangoMax);
            crScoreDetalleDto.setPuntosScore(puntosScore);
            crScoreDetalleDto.setIdEmpresa(idEmpresa);
            crScoreDetalleDto.setIdEstatus(estatus);

            try{
                new CrScoreDetalleDaoImpl(user.getConn()).update(crScoreDetalleDto.createPk(), crScoreDetalleDto);

                crScoreDto.setFechaHrUltimaEdicion(new Date());
                crScoreDto.setIdUsuarioEdicion(user.getUser().getIdUsuarios());
                new CrScoreDaoImpl(user.getConn()).update(crScoreDto.createPk(), crScoreDto);
                
                msgExitoExtra += "Registro actualizado satisfactoriamente.";
            }catch(Exception ex){
                msgError += "No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString();
                ex.printStackTrace();
            }
        }
    }else if (mode==3){
        // 3 = Consultar Campos de Formulario para Select dinámico
        int idFormulario = -1;
        
        try{ idFormulario = Integer.parseInt(request.getParameter("id_formulario"));}catch(Exception ex){} 
        
        if (idFormulario>0){
            CrFormularioCampoBO crFormularioCampoBO = new CrFormularioCampoBO(user.getConn());
            String opcionesSelect = crFormularioCampoBO.getCrFormularioCamposByIdHTMLCombo(idEmpresa, -1, 0, 0, " AND id_formulario=" + idFormulario);
            
            msgExitoExtra += opcionesSelect;
        }else{
            msgError+="<ul>No se específico un Formulario válido para recuperar sus Campos.";
        }
        
    }
    
    if (msgError.equals("")){
        out.print("<!--EXITO-->" + msgExitoExtra);
    }
%>
<%
    if (!msgError.equals("")){
        out.print("<!--ERROR-->"+msgError);
    }
%>