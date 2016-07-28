<%-- 
    Document   : catCrDocImpParametro_list
    Created on : 30/06/2016, 12:55:48 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.dao.jdbc.CrDocImprimibleDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrDocImprimible"%>
<%@page import="com.tsp.sct.bo.CrDocImprimibleBO"%>
<%@page import="com.tsp.sct.bo.CrFormularioCampoBO"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.bo.CrDocImpParametroBO"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.CrDocImpParametroDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrDocImpParametro"%>
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
    *        0 = Guardar Nuevo DocImprimible Detalle
    *        1 = Editar DocImprimible Detalle
    *        2 =  //reservado
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
        // 0 = Guardar Nuevo DocImprimible Detalle
        
        //Parámetros
        int idDocImprimible = -1;
        String parametroClave ="";
        String descripcion = "";
        String asociaVariableFormula = "";
        String valorDefecto = "";
        int estatus = 2;//deshabilitado
        
        try{ idDocImprimible = Integer.parseInt(request.getParameter("id_doc_imprimible"));}catch(Exception ex){} 
        parametroClave = request.getParameter("parametro_clave")!=null?new String(request.getParameter("parametro_clave").getBytes("ISO-8859-1"),"UTF-8"):"";    
        descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";    
        valorDefecto = request.getParameter("valor_defecto")!=null?new String(request.getParameter("valor_defecto").getBytes("ISO-8859-1"),"UTF-8"):"";    
        asociaVariableFormula = request.getParameter("asocia_variable_formula")!=null?new String(request.getParameter("asocia_variable_formula").getBytes("ISO-8859-1"),"UTF-8"):"";    
        try{ estatus = Integer.parseInt(request.getParameter("estatus"));}catch(Exception ex){} 
        
        if(idDocImprimible<=0)
            msgError += "<ul>El dato 'Documento Imprimible' es requerido, debe elegir uno válido."; 
        if(!gc.isValidString(parametroClave, 1, 50))
            msgError += "<ul>El dato 'Nombre de Parámetro' es requerido, debe tener máximo 50 caracteres ."; 
        
        CrDocImprimible crDocImprimibleDto = new CrDocImprimibleBO(idDocImprimible, user.getConn()).getCrDocImprimible();
        if (crDocImprimibleDto==null){
            msgError += "<ul>El Documento Imprimible padre seleccionado no existe en base de datos.";
        }
    
        if (msgError.equals("")){
            CrDocImpParametro crDocImpParametroDto = new CrDocImpParametro();
            CrDocImpParametroDaoImpl crDocImpParametroDaoImpl = new CrDocImpParametroDaoImpl(user.getConn());

            crDocImpParametroDto.setIdDocImprimible(idDocImprimible);
            crDocImpParametroDto.setParametroClave(parametroClave);
            crDocImpParametroDto.setDescripcion(descripcion);
            crDocImpParametroDto.setValorDefecto(valorDefecto);
            crDocImpParametroDto.setAsociaVariableFormula(asociaVariableFormula);
            crDocImpParametroDto.setIdEmpresa(idEmpresa);
            crDocImpParametroDto.setIdEstatus(estatus);

            try{
                crDocImpParametroDaoImpl.insert(crDocImpParametroDto); 
                
                crDocImprimibleDto.setFechaHrUltimaEdicion(new Date());
                crDocImprimibleDto.setIdUsuarioEdicion(user.getUser().getIdUsuarios());
                new CrDocImprimibleDaoImpl(user.getConn()).update(crDocImprimibleDto.createPk(), crDocImprimibleDto);

                msgExitoExtra += "Registro creado satisfactoriamente";
            }catch(Exception ex){
                msgError += "No se pudo crear el registro. Informe del error al administrador del sistema: " + ex.toString();
                ex.printStackTrace();
            }
        }
    }else if (mode==1){
        // 1 = Editar DocImprimible Detalle
        
        //Parámetros
        int idCrDocImpParametro = -1;
        int idDocImprimible = -1;
        String parametroClave ="";
        String descripcion = "";
        String asociaVariableFormula = "";
        String valorDefecto = "";
        int estatus = 2;//deshabilitado
        
        try{
            idCrDocImpParametro = Integer.parseInt(request.getParameter("idCrDocImpParametro"));
        }catch(Exception ex){}
        try{ idDocImprimible = Integer.parseInt(request.getParameter("id_doc_imprimible"));}catch(Exception ex){} 
        parametroClave = request.getParameter("parametro_clave")!=null?new String(request.getParameter("parametro_clave").getBytes("ISO-8859-1"),"UTF-8"):"";    
        descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";    
        valorDefecto = request.getParameter("valor_defecto")!=null?new String(request.getParameter("valor_defecto").getBytes("ISO-8859-1"),"UTF-8"):"";    
        asociaVariableFormula = request.getParameter("asocia_variable_formula")!=null?new String(request.getParameter("asocia_variable_formula").getBytes("ISO-8859-1"),"UTF-8"):"";
        try{ estatus = Integer.parseInt(request.getParameter("estatus"));}catch(Exception ex){} 
        
        if(idCrDocImpParametro <= 0 )
            msgError += "<ul>El dato ID 'CrDocImpParametro' es requerido para ediciones";
        if(idDocImprimible<=0)
            msgError += "<ul>El dato 'Documento Imprimible' es requerido, debe elegir uno válido."; 
        if(!gc.isValidString(parametroClave, 1, 50))
            msgError += "<ul>El dato 'Nombre de Parámetro' es requerido, debe tener máximo 50 caracteres ."; 
        
        CrDocImprimible crDocImprimibleDto = new CrDocImprimibleBO(idDocImprimible, user.getConn()).getCrDocImprimible();
        if (crDocImprimibleDto==null){
            msgError += "<ul>El Documento Imprimible padre seleccionado no existe en base de datos.";
        }

        if (msgError.equals("")){
            CrDocImpParametroBO crDocImpParametroBO = new CrDocImpParametroBO(idCrDocImpParametro,user.getConn());
            CrDocImpParametro crDocImpParametroDto = crDocImpParametroBO.getCrDocImpParametro();

            crDocImpParametroDto.setIdDocImprimible(idDocImprimible);
            crDocImpParametroDto.setParametroClave(parametroClave);
            crDocImpParametroDto.setDescripcion(descripcion);
            crDocImpParametroDto.setValorDefecto(valorDefecto);
            crDocImpParametroDto.setAsociaVariableFormula(asociaVariableFormula);
            crDocImpParametroDto.setIdEmpresa(idEmpresa);
            crDocImpParametroDto.setIdEstatus(estatus);

            try{
                new CrDocImpParametroDaoImpl(user.getConn()).update(crDocImpParametroDto.createPk(), crDocImpParametroDto);

                crDocImprimibleDto.setFechaHrUltimaEdicion(new Date());
                crDocImprimibleDto.setIdUsuarioEdicion(user.getUser().getIdUsuarios());
                new CrDocImprimibleDaoImpl(user.getConn()).update(crDocImprimibleDto.createPk(), crDocImprimibleDto);
                
                msgExitoExtra += "Registro actualizado satisfactoriamente.";
            }catch(Exception ex){
                msgError += "No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString();
                ex.printStackTrace();
            }
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