<%-- 
    Document   : catCrFormulario_disenar_ajax
    Created on : 13/06/2016, 07:30:45 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.bo.CrFormularioBO"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.util.GenericMethods"%>
<%@page import="com.tsp.sgfens.sesion.CrFormularioCampoSesion"%>
<%@page import="com.tsp.sgfens.sesion.CrFormularioDisenoSesion"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="frmDiseno" scope="session" class="com.tsp.sgfens.sesion.CrFormularioDisenoSesion"/>
<%
    String msgError="";
    String msgExitoExtra="";
    
    /*
    *   Modos:
    *       -1  =  No se eligio (ERROR)
    * 
    *        1 = Agregar Campo
    *        2 = Inicializar/Recuperar Diseño Formulario
    *        3 = Recuperar datos campo especifico
    *        4 = Quitar campo
    *        5 = Guardar Formulario
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
        // 1 = agregar campo
        
        if (frmDiseno == null)
            frmDiseno = new CrFormularioDisenoSesion(user.getConn()); 
        
        //Parametros - Definición
        int tipoCampo=-1;
        String nombreIdCampo = "";
        int identificadorCampoSesion = -1;
        int orden = -1;
        int idFormularioCampo = -1;
        String etiqueta = "";
        String descripcion = "";
        String sugerencia = "";
        boolean isRequerido = false;
        int idFormularioValidacion = -1;
        String opciones = "";
        String valorDefecto = "";
        String variableFormula = "";
        
        // Parametros - Recuperación
        try{ identificadorCampoSesion = Integer.parseInt(request.getParameter("identificador_campo_sesion")); }catch(Exception e){}
        try{ orden = Integer.parseInt(request.getParameter("orden")); }catch(Exception e){}
        try{ idFormularioCampo = Integer.parseInt(request.getParameter("id_formulario_campo")); }catch(Exception e){}
        try{ idFormularioValidacion = Integer.parseInt(request.getParameter("id_formulario_validacion")); }catch(Exception e){}
        try{ tipoCampo = Integer.parseInt(request.getParameter("tipo_campo")); }catch(Exception e){}
        nombreIdCampo = request.getParameter("nombre_id_campo")!=null?new String(request.getParameter("nombre_id_campo").getBytes("ISO-8859-1"),"UTF-8"):"";
        etiqueta = request.getParameter("etiqueta")!=null?new String(request.getParameter("etiqueta").getBytes("ISO-8859-1"),"UTF-8"):"";
        descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";
        sugerencia = request.getParameter("sugerencia")!=null?new String(request.getParameter("sugerencia").getBytes("ISO-8859-1"),"UTF-8"):"";
        opciones = request.getParameter("opciones")!=null?new String(request.getParameter("opciones").getBytes("ISO-8859-1"),"UTF-8"):"";
        valorDefecto = request.getParameter("valor_defecto")!=null?new String(request.getParameter("valor_defecto").getBytes("ISO-8859-1"),"UTF-8"):"";
        variableFormula = request.getParameter("variable_formula")!=null?new String(request.getParameter("variable_formula").getBytes("ISO-8859-1"),"UTF-8"):"";
        String isRequeridoAux = request.getParameter("is_requerido")!=null?new String(request.getParameter("is_requerido").getBytes("ISO-8859-1"),"UTF-8") : "";
        isRequerido = isRequeridoAux.equals("true") || isRequeridoAux.equals("1");
        
        try{
            int indexExistente = -1;
            if (identificadorCampoSesion>0){
                indexExistente = frmDiseno.buscaIndexCampoSesion(idFormularioCampo, identificadorCampoSesion);
            }
            
            if (orden<=0){
                orden = frmDiseno.getUltimoOrdenCampoSesion() + 1;
            }

            // Verificamos si el campo ya existe en sesion
            if (indexExistente>=0){
            // Ya existe en sesion, la accion a seguir es actualizarlo
                
                if (etiqueta.length()<=0){
                    msgError += "<ul>El dato etiqueta es obligatorio.";
                }else{
                    CrFormularioCampoSesion campoSesion = frmDiseno.getListaCampoSesion().get(indexExistente);

                    campoSesion.setOrden(orden);
                    campoSesion.setEtiqueta(etiqueta);
                    campoSesion.setDescripcion(descripcion);
                    campoSesion.setSugerencia(sugerencia);
                    campoSesion.setIsRequerido(isRequerido);
                    campoSesion.setIdFormularioValidacion(idFormularioValidacion);
                    campoSesion.setOpciones(opciones);//opciones o formula
                    campoSesion.setValorDefecto(valorDefecto);
                    if (StringManage.getValidString(variableFormula).length()>0){
                        campoSesion.setVariableFormula(StringManage.getValidString(variableFormula));
                    }

                    frmDiseno.getListaCampoSesion().set(indexExistente, campoSesion);
                }
            }else{
            // No existe en sesion, la accion a seguir es crearlo
                CrFormularioCampoSesion campoSesion = new CrFormularioCampoSesion();
                campoSesion.setIdentificadorCampoSesion(frmDiseno.getUltimoIdentificadorCampoSesion() + 1);
                campoSesion.setOrden(orden);
                campoSesion.setIdTipoCampo(tipoCampo);
                campoSesion.setIdFormularioCampo(0); // es nuevo para BD, no edicion
                campoSesion.setNombreIdCampo(nombreIdCampo);
                
                campoSesion.asignaValoresDefecto();
                if (etiqueta.length()>0)
                    campoSesion.setEtiqueta(etiqueta);
                if (descripcion.length()>0)
                    campoSesion.setDescripcion(descripcion);
                campoSesion.setSugerencia(sugerencia);
                campoSesion.setIsRequerido(isRequerido);
                campoSesion.setIdFormularioValidacion(idFormularioValidacion);
                if (opciones.length()>0)
                    campoSesion.setOpciones(opciones);//opciones o formula
                campoSesion.setValorDefecto(valorDefecto);
                if (StringManage.getValidString(variableFormula).length()>0)
                    campoSesion.setVariableFormula(StringManage.getValidString(variableFormula));

                frmDiseno.getListaCampoSesion().add(campoSesion);
            }
            
            //ordenamos lista de campos por atributo Orden
            java.util.Collections.sort(frmDiseno.getListaCampoSesion());
            
        }catch(Exception ex){
            msgError+= "<ul>Error inesperado: " + GenericMethods.exceptionStackTraceToString(ex);
        }
    }else if (mode==2){
        // 2 = inicializar / recuperar diseño de formulario
        frmDiseno.getListaCampoSesion().clear();
        frmDiseno = null;
        frmDiseno = new CrFormularioDisenoSesion(user.getConn()); 
        
        int idCrFormulario = -1;
        try{ idCrFormulario = Integer.parseInt(request.getParameter("idCrFormulario")); }catch(Exception e){}
        
        if (idCrFormulario>0){
            //Edición
            //Recuperamos todos los datos de bd a sesion
            try{
                frmDiseno = new CrFormularioBO(user.getConn()).recuperarFormularioSesion(idCrFormulario);
            }catch(Exception ex){
                msgError += "<ul>" + GenericMethods.exceptionStackTraceToString(ex);
            }
        }else{
            msgError += "<ul> No se indico Formulario para inicializar Diseño.";
        }
        
        request.getSession().setAttribute("frmDiseno", frmDiseno);
        
    }else if (mode==3){
        // 3 = Recuperar datos campo especifico
        int identificadorCampoSesion = -1;
        try{ identificadorCampoSesion = Integer.parseInt(request.getParameter("identificador_campo_sesion")); }catch(Exception e){}
        
        if (identificadorCampoSesion>0){            
            int indexExistente = -1;
            indexExistente = frmDiseno.buscaIndexCampoSesion(0, identificadorCampoSesion);
            if (indexExistente>=0){
                CrFormularioCampoSesion campoSesion = frmDiseno.getListaCampoSesion().get(indexExistente);
                //Usamos JSON para retornar el objeto respuesta
                String jsonOutput = gson.toJson(campoSesion);
                msgExitoExtra = jsonOutput;
            }else{
                msgError+="<ul>Campo no encontrado en sesion.";
            }
        }else{
            msgError+="<ul>Campo no existente en sesion.";
        }
        
    }else if (mode==4){
        // 4 = Quitar campo
        int identificadorCampoSesion = -1;
        try{ identificadorCampoSesion = Integer.parseInt(request.getParameter("identificador_campo_sesion")); }catch(Exception e){}
        
        if (identificadorCampoSesion>0){            
            int indexExistente = -1;
            indexExistente = frmDiseno.buscaIndexCampoSesion(0, identificadorCampoSesion);
            if (indexExistente>=0){
                frmDiseno.getListaCampoSesion().remove(indexExistente);
            }else{
                msgError+="<ul>Campo no encontrado en sesion.";
            }
        }else{
            msgError+="<ul>Campo no existente en sesion.";
        }
    }else if (mode==5){
        // 5 = Guardar Formulario
        CrFormularioBO crFormularioBO = new CrFormularioBO(user.getConn());
        try{
            crFormularioBO.guardarFormularioSesion(frmDiseno, user);
            msgExitoExtra += "Diseño almacenado exitosamente.";
        }catch(Exception ex){
            msgError += "<ul>" + GenericMethods.exceptionStackTraceToString(ex);
        }
        
    }
    
    if (msgError.equals("") && mode!=0){
        out.print("<!--EXITO-->" + msgExitoExtra);
    }
    
    
    if (msgError.equals("") && (mode==1 || mode==2 || mode==4) ){ //retornar campos
        int ordenAjustado = 1;
        for (CrFormularioCampoSesion campoSesion : frmDiseno.getListaCampoSesion() ) {
            campoSesion.setOrden(ordenAjustado);
%>
        <div id="div_container_campo_<%= campoSesion.getIdentificadorCampoSesion() %>" class="wrapper highlight" style="width: 100%">
            <div id="div_container_campo_column1_<%= campoSesion.getIdentificadorCampoSesion() %>" class="column-1" style="width: 85%">
<%          
            if (campoSesion.getIdTipoCampo()==1){ //texto
%>
                <div class="wrapper" style="width: 100%">
                    <div class="column-1" style="width: 30%">
                        <label><%= StringManage.getValidString(campoSesion.getEtiqueta()).length()>0? StringManage.getValidString(campoSesion.getEtiqueta()) :"Campo de Texto" %> <%= campoSesion.isIsRequerido()?"*":"" %></label>
                        <span style="font-size: 11px; color: #5691a8;"><i><%= StringManage.getValidString(campoSesion.getVariableFormula()).length()>0? "<br/>" + StringManage.getValidString(campoSesion.getVariableFormula()) :"" %></i></span>
                    </div>
                    <div class="column-2" style="width: 70%">
                        <input type="text" id="<%=campoSesion.getNombreIdCampo() %>" name="<%=campoSesion.getNombreIdCampo()%>" 
                               placeholder="<%= StringManage.getValidString(campoSesion.getSugerencia()) %>"   />
                        <br/>
                        <i><%= StringManage.getValidString(campoSesion.getDescripcion()) %></i>
                    </div>
                </div>
<%
            }else if (campoSesion.getIdTipoCampo()==2){ //Opción multiple
%>
                <div class="wrapper" style="width: 100%">
                    <div class="column-1" style="vertical-align: top; width: 30%">
                        <label><%= StringManage.getValidString(campoSesion.getEtiqueta()).length()>0? StringManage.getValidString(campoSesion.getEtiqueta()) :"Opción Múltiple" %> <%= campoSesion.isIsRequerido()?"*":"" %></label>
                        <span style="font-size: 11px; color: #5691a8;"><i><%= StringManage.getValidString(campoSesion.getVariableFormula()).length()>0? "<br/>" + StringManage.getValidString(campoSesion.getVariableFormula()) :"" %></i></span>
                    </div>
                    <div class="column-2" style="width: 70%">
                        <% 
                            int j = 0;
                            for (String opcion : campoSesion.getOpciones()) { 
                        %>
                        <input type="checkbox" id="<%=campoSesion.getNombreIdCampo()+""+j%>" name="<%=campoSesion.getNombreIdCampo()%>"  value="<%= opcion %>"/> <label for="<%=campoSesion.getNombreIdCampo()+""+j%>"><%= opcion %></label>
                        <br/>
                        <%
                                j++;
                            }
                        %>
                        <i><%= StringManage.getValidString(campoSesion.getDescripcion()) %></i>
                    </div>
                </div>
<%
            }else if (campoSesion.getIdTipoCampo()==3){ //Opción unica
%>
                <div class="wrapper" style="width: 100%">
                    <div class="column-1" style="vertical-align: top; width: 30%">
                        <label><%= StringManage.getValidString(campoSesion.getEtiqueta()).length()>0? StringManage.getValidString(campoSesion.getEtiqueta()) :"Opción Única" %> <%= campoSesion.isIsRequerido()?"*":"" %></label>
                        <span style="font-size: 11px; color: #5691a8;"><i><%= StringManage.getValidString(campoSesion.getVariableFormula()).length()>0? "<br/>" + StringManage.getValidString(campoSesion.getVariableFormula()) :"" %></i></span>
                    </div>
                    <div class="column-2" style="width: 70%">
                        <% 
                            int j = 0;
                            for (String opcion : campoSesion.getOpciones()) { 
                        %>
                        <input type="radio" id="<%=campoSesion.getNombreIdCampo()+""+j%>" name="<%=campoSesion.getNombreIdCampo()%>"  value="<%= opcion %>"/> <label for="<%=campoSesion.getNombreIdCampo()+""+j%>"><%= opcion %></label>
                        <br/>
                        <%
                                j++;
                            }
                        %>
                        <i><%= StringManage.getValidString(campoSesion.getDescripcion()) %></i>
                    </div>
                </div>
<%          
            }else if (campoSesion.getIdTipoCampo()==4){ //Lista de opciones
%>
                <div class="wrapper" style="width: 100%">
                    <div class="column-1" style="vertical-align: top; width: 30%">
                        <label><%= StringManage.getValidString(campoSesion.getEtiqueta()).length()>0? StringManage.getValidString(campoSesion.getEtiqueta()) :"Lista de Opciones" %> <%= campoSesion.isIsRequerido()?"*":"" %></label>
                        <span style="font-size: 11px; color: #5691a8;"><i><%= StringManage.getValidString(campoSesion.getVariableFormula()).length()>0? "<br/>" + StringManage.getValidString(campoSesion.getVariableFormula()) :"" %></i></span>
                    </div>
                    <div class="column-2" style="width: 70%">
                        <select size="1" id="<%=campoSesion.getNombreIdCampo()%>" name="<%=campoSesion.getNombreIdCampo()%>" >
                            <% 
                                for (String opcion : campoSesion.getOpciones()) { 
                            %>
                            <option value="<%= opcion %>"><%= opcion %></option>
                            <%
                                }
                            %>
                        </select>
                        <br/>
                        <i><%= StringManage.getValidString(campoSesion.getDescripcion()) %></i>
                    </div>
                </div>
<%
            }else if (campoSesion.getIdTipoCampo()==5){ //Subtitulo
%>
                <div class="wrapper" style="width: 100%">
                    <span id="<%=campoSesion.getNombreIdCampo()%>" name="<%=campoSesion.getNombreIdCampo()%>">
                        <h2><%= StringManage.getValidString(campoSesion.getEtiqueta()).length()>0? StringManage.getValidString(campoSesion.getEtiqueta()) :"Agrega un subtítulo" %> </h2>
                    </span>
                </div>
<%
            }else if (campoSesion.getIdTipoCampo()==6){ //Fotografía
%>
                <div class="wrapper" style="width: 100%">
                    <div class="column-1" style="vertical-align: top; width: 30%">
                        <label><%= StringManage.getValidString(campoSesion.getEtiqueta()).length()>0? StringManage.getValidString(campoSesion.getEtiqueta()) :"Capturar Fotografía" %> <%= campoSesion.isIsRequerido()?"*":"" %></label>
                    </div>
                    <div class="column-2" style="width: 70%">
                        <img src="../../images/img_fotografia.png" height="40px" width="40px" />
                        <br/>
                        <i><%= StringManage.getValidString(campoSesion.getDescripcion()).length()>0? StringManage.getValidString(campoSesion.getDescripcion()) :"Descripción" %></i>
                    </div>
                </div>
<%
            }else if (campoSesion.getIdTipoCampo()==7){ //Firma
%>
                <div class="wrapper" style="width: 100%">
                    <div class="column-1" style="vertical-align: top; width: 30%">
                        <label><%= StringManage.getValidString(campoSesion.getEtiqueta()).length()>0? StringManage.getValidString(campoSesion.getEtiqueta()) :"Firma" %> <%= campoSesion.isIsRequerido()?"*":"" %></label>
                    </div>
                    <div class="column-2" style="width: 70%">
                        <img src="../../images/img_firma.png" height="40px" width="40px" />
                        <br/>
                        <i><%= StringManage.getValidString(campoSesion.getDescripcion()).length()>0? StringManage.getValidString(campoSesion.getDescripcion()) :"Pon tu firma" %></i>
                    </div>
                </div>
<%
            }else if (campoSesion.getIdTipoCampo()==8){ //Calendario
%>
                <div class="wrapper" style="width: 100%">
                    <div class="column-1" style="vertical-align: top; width: 30%">
                        <label><%= StringManage.getValidString(campoSesion.getEtiqueta()).length()>0? StringManage.getValidString(campoSesion.getEtiqueta()) :"Calendario" %> <%= campoSesion.isIsRequerido()?"*":"" %></label>
                        <span style="font-size: 11px; color: #5691a8;"><i><%= StringManage.getValidString(campoSesion.getVariableFormula()).length()>0? "<br/>" + StringManage.getValidString(campoSesion.getVariableFormula()) :"" %></i></span>
                    </div>
                    <div class="column-2" style="width: 70%">
                        <input type="date" id="<%=campoSesion.getNombreIdCampo() %>" name="<%=campoSesion.getNombreIdCampo()%>" />
                        <br/>
                        <i><%= StringManage.getValidString(campoSesion.getDescripcion()) %></i>
                    </div>
                </div>
<%
            }else if (campoSesion.getIdTipoCampo()==9){ //Hora del día
%>
                <div class="wrapper" style="width: 100%">
                    <div class="column-1" style="vertical-align: top; width: 30%">
                        <label><%= StringManage.getValidString(campoSesion.getEtiqueta()).length()>0? StringManage.getValidString(campoSesion.getEtiqueta()) :"Hora" %> <%= campoSesion.isIsRequerido()?"*":"" %></label>
                        <span style="font-size: 11px; color: #5691a8;"><i><%= StringManage.getValidString(campoSesion.getVariableFormula()).length()>0? "<br/>" + StringManage.getValidString(campoSesion.getVariableFormula()) :"" %></i></span>
                    </div>
                    <div class="column-2" style="width: 70%">
                        <input type="time" id="<%=campoSesion.getNombreIdCampo() %>" name="<%=campoSesion.getNombreIdCampo()%>" />
                        <br/>
                        <i><%= StringManage.getValidString(campoSesion.getDescripcion()) %></i>
                    </div>
                </div>
<%
            }else if (campoSesion.getIdTipoCampo()==10){ //Fórmula
%>
                <div class="wrapper" style="width: 100%">
                    <div class="column-1" style="vertical-align: top; width: 30%">
                        <label><%= StringManage.getValidString(campoSesion.getEtiqueta()).length()>0? StringManage.getValidString(campoSesion.getEtiqueta()) :"Fórmula" %> </label>
                        <span style="font-size: 11px; color: #5691a8;"><i><%= StringManage.getValidString(campoSesion.getVariableFormula()).length()>0? "<br/>" + StringManage.getValidString(campoSesion.getVariableFormula()) :"" %></i></span>
                    </div>
                    <div class="column-2" style="width: 70%">
                        <h3><%= StringManage.getValidString(campoSesion.getOpcionesTexto()).length()>0? StringManage.getValidString(campoSesion.getOpcionesTexto()) :"Definir Fórmula - Expresión algebraica." %></h3>
                        <br/>
                        <i><%= StringManage.getValidString(campoSesion.getDescripcion()) %></i>
                    </div>
                </div>
<%
            }else{
                out.print("<br/>Tipo de Campo no implementado.");
            }
%>
            </div>
            <div id="div_container_campo_column2_<%= campoSesion.getIdentificadorCampoSesion() %>" class="column-1" style="width: 15%">
                <a href="#" onclick="muestraConfiguracionCampo(<%= campoSesion.getIdentificadorCampoSesion() %>);"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar"/></a>
                &nbsp;&nbsp;
                <a href="#" onclick="quitaCampo(<%= campoSesion.getIdentificadorCampoSesion() %>);"><img src="../../images/icon_delete.png" alt="quitar" class="help" title="Quitar"/></a>
            </div>
        </div>
        <br/>
<%
            ordenAjustado++;
        }
    }
%>
<%
    if (!msgError.equals("")){
        out.print("<!--ERROR-->"+msgError);
    }
%>