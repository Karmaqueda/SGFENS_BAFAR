<%-- 
    Document   : catCrProductoCredito_ajax
    Created on : 27/06/2016, 12:27:02 PM
    Author     : ISCesar
--%>

<%@page import="com.tsp.sct.dao.jdbc.CrProductoSeguroDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrProductoSeguro"%>
<%@page import="com.tsp.sct.dao.dto.CrDocImprimible"%>
<%@page import="com.tsp.sct.bo.CrDocImprimibleBO"%>
<%@page import="com.tsp.sct.dao.jdbc.CrProductoCreditoDaoImpl"%>
<%@page import="com.tsp.sct.util.GenericMethods"%>
<%@page import="com.tsp.sct.dao.dto.CrProductoRegla"%>
<%@page import="com.tsp.sct.bo.CrProductoCreditoBO"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.dto.CrProductoCredito"%>
<%@page import="com.tsp.sct.bo.CrProductoReglaBO"%>
<%@page import="com.tsp.sct.dao.dto.CrProductoPuntajeMonto"%>
<%@page import="com.tsp.sgfens.sesion.CrProductoCreditoSesion"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="crProdCredito" scope="session" class="com.tsp.sgfens.sesion.CrProductoCreditoSesion"/>
<%
    String msgError="";
    String msgExitoExtra="";
    
    /*
    *   Modos:
    *       -1  =  No se eligio (ERROR)
    * 
    *        1 = Recupera de BD Producto Credito a Sesion
    *        2 = Recupera Relacion Puntaje-Monto
    *        3 = Agrega Relacion Puntaje-Monto
    *        4 = Elimina Relacion Puntaje-Monto
    *        5 = Guardar Padre Form+Sesion a BD
    *        6 = Guardar SubProducto de Form a BD
    *        7 = Recupera Relacion Documentos Imprimibles
    *        8 = Agrega Relacion Documentos Imprimibles
    *        9 = Elimina Relacion Documentos Imprimibles
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
        //1 = Recupera de BD Producto Credito Padre a Sesion
        int idCrProductoCredito = -1;
        try{ idCrProductoCredito = Integer.parseInt(request.getParameter("idCrProductoCredito")); }catch(Exception e){}
        
        if (idCrProductoCredito>0){
            // recuperamos de bd a sesion
            CrProductoCreditoBO crProductoCreditoBO = new CrProductoCreditoBO(user.getConn());
            try{
                crProdCredito = crProductoCreditoBO.recuperarProductoCreditoPadreSesion(idCrProductoCredito);
            }catch(Exception ex){
                msgError += "<ul>Error al recuperar información de Producto Padre." + GenericMethods.exceptionStackTraceToString(ex);
            }
        }else{
            crProdCredito.getListaProductoPuntajeMonto().clear();
            crProdCredito.getListaReglas().clear();
            crProdCredito = new CrProductoCreditoSesion(user.getConn());
        }
        
        request.getSession().setAttribute("crProdCredito", crProdCredito);
    }else if (mode==2){
        //2 = Recupera Relacion Puntaje-Monto
        if (crProdCredito == null)
            crProdCredito = new CrProductoCreditoSesion(user.getConn());
    }else if (mode==3){
        //3 = Agrega Relacion Puntaje-Monto
        int puntaje_min = -1;
        int puntaje_max = -1;
        int pct_monto = -1;
        
        try{ puntaje_min = Integer.parseInt(request.getParameter("puntaje_min")); }catch(Exception e){}
        try{ puntaje_max = Integer.parseInt(request.getParameter("puntaje_max")); }catch(Exception e){}
        try{ pct_monto = Integer.parseInt(request.getParameter("pct_monto")); }catch(Exception e){}
        
        if(puntaje_min<=0)
            msgError += "<ul>El dato 'Puntaje mínimo' es requerido y debe ser mayor a 0.";
        if(puntaje_max<=0)
            msgError += "<ul>El dato 'Puntaje máximo' es requerido y debe ser mayor a 0.";
        if(pct_monto<=0 || pct_monto>100)
            msgError += "<ul>El dato 'Porcentaje monto' es requerido y debe ser un valor entre 1 y 100.";
        
        if (puntaje_min > puntaje_max)
            msgError += "<ul>El dato 'puntaje mínimo' no puede ser mayor al dato 'puntaje maximo'.";
        
        if (msgError.equals("")){
            CrProductoPuntajeMonto crProductoPuntajeMonto = new CrProductoPuntajeMonto();
            crProductoPuntajeMonto.setRangoMin(puntaje_min);
            crProductoPuntajeMonto.setRangoMax(puntaje_max);
            crProductoPuntajeMonto.setPctAutorizado(pct_monto);
            crProdCredito.getListaProductoPuntajeMonto().add(crProductoPuntajeMonto);
        }
        
    }else if (mode==4){
        //4 = Elimina Relacion Puntaje-Monto
        int indexListaProdPuntajeMonto = -1;
        try{ indexListaProdPuntajeMonto = Integer.parseInt(request.getParameter("index_lista_puntaje_monto")); }catch(Exception e){}
        
        if(indexListaProdPuntajeMonto<0)
            msgError += "<ul>No seleccionó un registro válido a eliminar.";
        if(indexListaProdPuntajeMonto>=crProdCredito.getListaProductoPuntajeMonto().size())
            msgError += "<ul>El elemento de la lista que desea eliminar no es válido.";
        
        if (msgError.equals(""))
            crProdCredito.getListaProductoPuntajeMonto().remove(indexListaProdPuntajeMonto);
        
    }else if (mode==5){
        //5 = Guardar Padre Form+Sesion a BD
        
        //Parametros
        //int idCrProductoCredito = -1;
        //int idProductoCrPadre = -1;
        int id_grupo_formulario_solic = -1;
        int id_grupo_formulario_verif = -1;
        //int id_grupo_formulario_fotos = -1;
        int id_score = -1;
        int estatus = 2; // desactivado 
        String nombre = "";
        String descripcion = "";
        String tipo_amortizacion = ""; 
        String garantias_descripcion = "";
        
        // Recibimos parametros
        //try{ idCrProductoCredito = Integer.parseInt(request.getParameter("idProductoCrPadre")); }catch(Exception e){}
        //try{ idProductoCrPadre = Integer.parseInt(request.getParameter("idProductoCrPadre")); }catch(Exception e){}
        try{ id_grupo_formulario_solic = Integer.parseInt(request.getParameter("id_grupo_formulario_solic")); }catch(Exception e){}
        try{ id_grupo_formulario_verif = Integer.parseInt(request.getParameter("id_grupo_formulario_verif")); }catch(Exception e){}
        //try{ id_grupo_formulario_fotos = Integer.parseInt(request.getParameter("id_grupo_formulario_fotos")); }catch(Exception e){}
        try{ id_score = Integer.parseInt(request.getParameter("id_score")); }catch(Exception e){}
        try{ estatus = Integer.parseInt(request.getParameter("estatus")); }catch(Exception e){}
        
        nombre = request.getParameter("nombre")!=null?new String(request.getParameter("nombre").getBytes("ISO-8859-1"),"UTF-8"):"";    
        descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";    
        tipo_amortizacion = request.getParameter("tipo_amortizacion")!=null?new String(request.getParameter("tipo_amortizacion").getBytes("ISO-8859-1"),"UTF-8"):"";    
        garantias_descripcion = request.getParameter("garantias_descripcion")!=null?new String(request.getParameter("garantias_descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";    

        // validamos
        if(!gc.isValidString(nombre, 1, 50))
            msgError += "<ul>El dato 'nombre' es requerido, debe tener máximo 50 caracteres."; 
        if(!gc.isValidString(descripcion, 1, 500))
            msgError += "<ul>El dato 'descripción' es requerido, debe tener máximo 500 caracteres."; 
        if (id_score<=0)
            msgError += "<ul>El dato 'Score / Paramétrico' es requerido.";
        if (id_grupo_formulario_solic<=0)
            msgError += "<ul>El dato 'Grupo Formulario Solicitud' es requerido.";
        if (id_grupo_formulario_verif<=0)
            msgError += "<ul>El dato 'Grupo Formulario Verificación' es requerido.";
        //if (id_grupo_formulario_fotos<=0)
        //    msgError += "<ul>El dato 'Grupo Formulario Evidencia Fotográfica' es requerido.";
        if (tipo_amortizacion.equals(""))
            msgError += "<ul>El dato 'Tipo de Amortización' es requerido.";
    
        // procesamos
        if (msgError.equals("")){
            // Datos de Producto Credito
            if (crProdCredito.getCrProductoCredito()==null){
                crProdCredito.setCrProductoCredito(new CrProductoCredito());
                crProdCredito.getCrProductoCredito().setFechaHrCreacion(new Date());
            }else{
                crProdCredito.getCrProductoCredito().setFechaHrUltimaEdicion(new Date());
            }
            
            crProdCredito.getCrProductoCredito().setIdProductoCreditoPadreNull(true);
            crProdCredito.getCrProductoCredito().setNombre(nombre);
            crProdCredito.getCrProductoCredito().setDescripcion(descripcion);
            crProdCredito.getCrProductoCredito().setIdUsuarioEdicion(user.getUser().getIdUsuarios());
            crProdCredito.getCrProductoCredito().setIdScore(id_score);
            crProdCredito.getCrProductoCredito().setIdGrupoFormularioSolic(id_grupo_formulario_solic);
            crProdCredito.getCrProductoCredito().setIdGrupoFormularioVerif(id_grupo_formulario_verif);
            //crProdCredito.getCrProductoCredito().setIdGrupoFormularioFotos(id_grupo_formulario_fotos);
            crProdCredito.getCrProductoCredito().setTipoAmortizacion(tipo_amortizacion);
            //datos para sub-producto
            crProdCredito.getCrProductoCredito().setMonto(0);
            crProdCredito.getCrProductoCredito().setPlazo(0);
            crProdCredito.getCrProductoCredito().setTasaInteresAnual(0);
            crProdCredito.getCrProductoCredito().setTasaInteresMora(0);
            crProdCredito.getCrProductoCredito().setGastosCobranza(0);
            // fin datos para sub-producto
            crProdCredito.getCrProductoCredito().setGarantiasDescripcion(garantias_descripcion);
            crProdCredito.getCrProductoCredito().setIdEstatus(estatus);
            crProdCredito.getCrProductoCredito().setIdEmpresa(idEmpresa);
            
            //Datos de Reglas aplicadas a producto
            for (String regla : CrProductoReglaBO.reglas){
                CrProductoReglaBO.AuxReglaGenerica auxReglaGenerica = CrProductoReglaBO.getValorReglaGenerica(regla);
                
                double valorRegla = -1;
                try{ valorRegla = new Double(request.getParameter(auxReglaGenerica.getClave())); }catch(Exception e){}
                
                System.out.println("------ Regla " + regla + ", clave: "  + auxReglaGenerica.getClave() + " valor: " + valorRegla);
                
                if (valorRegla>0){
                    CrProductoRegla crProductoRegla = new CrProductoRegla();
                
                    crProductoRegla.setClaveTipoRegla(auxReglaGenerica.getClave());
                    crProductoRegla.setEtiqueta(auxReglaGenerica.getEtiqueta());
                    if (auxReglaGenerica.getValorTipo()==CrProductoReglaBO.ValorTipo.RANGO_MAX){
                        crProductoRegla.setRangoMax(valorRegla);
                    }else if (auxReglaGenerica.getValorTipo()==CrProductoReglaBO.ValorTipo.RANGO_MIN){
                        crProductoRegla.setRangoMin(valorRegla);
                    }else if (auxReglaGenerica.getValorTipo()==CrProductoReglaBO.ValorTipo.VALOR_EXACTO){
                        crProductoRegla.setValorExacto(valorRegla);
                    }
                    crProductoRegla.setIsReglaAplicacionScore(auxReglaGenerica.getReglaTipo()==CrProductoReglaBO.ReglaTipo.APLICACION_SCORE ? 1 : 0 );
                    crProductoRegla.setIsReglaRechazo(auxReglaGenerica.getReglaTipo()==CrProductoReglaBO.ReglaTipo.RECHAZO ? 1 : 0 );
                    crProductoRegla.setIdEmpresa(idEmpresa);
                    crProductoRegla.setIdEstatus(1);

                    crProdCredito.getListaReglas().add(crProductoRegla);
                }
                
            }
            
            CrProductoCreditoBO crProductoCreditoBO = new CrProductoCreditoBO(user.getConn());
            try{
                crProductoCreditoBO.guardarProductoCreditoPadreSesion(crProdCredito, user);
                msgExitoExtra += "Registro almacenado exitosamente.";
            }catch(Exception ex){
                msgError += "<ul>Error inesperado al guardar Producto Credito Padre en base de datos: " + GenericMethods.exceptionStackTraceToString(ex);
            }
            crProdCredito = null;
            request.getSession().setAttribute("crProdCredito", crProdCredito);
        }
    }else if (mode==6){
        //6 = Guardar SubProducto de Form a BD
        String sub_nombre = "";
        String sub_descripcion = "";
        int sub_estatus = 2; // desactivado 
        int idProductoSub = -1;
        int idProductoPadre = -1;
        double sub_monto = -1;
        double sub_plazo = -1;
        double sub_tasa_interes_anual = -1;
        double sub_tasa_interes_mora = -1;
        double sub_gastos_cobranza = -1;
        double sub_cat = -1;
        int seguro_obligatorio = -1;
        String seguro_tipo = "";
        String seguro_aseguradora = "";
        
        sub_nombre = request.getParameter("sub_nombre")!=null?new String(request.getParameter("sub_nombre").getBytes("ISO-8859-1"),"UTF-8"):"";    
        sub_descripcion = request.getParameter("sub_descripcion")!=null?new String(request.getParameter("sub_descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";    
        seguro_tipo = request.getParameter("seguro_tipo")!=null?new String(request.getParameter("seguro_tipo").getBytes("ISO-8859-1"),"UTF-8"):"";    
        seguro_aseguradora = request.getParameter("seguro_aseguradora")!=null?new String(request.getParameter("seguro_aseguradora").getBytes("ISO-8859-1"),"UTF-8"):"";    
        try{ sub_estatus = Integer.parseInt(request.getParameter("sub_estatus")); }catch(Exception e){}
        try{ idProductoSub = Integer.parseInt(request.getParameter("idProductoSub")); }catch(Exception e){}
        try{ idProductoPadre = Integer.parseInt(request.getParameter("idProductoPadre")); }catch(Exception e){}
        try{ sub_monto = new Double(request.getParameter("sub_monto")); }catch(Exception e){}
        try{ sub_plazo = new Double(request.getParameter("sub_plazo")); }catch(Exception e){}
        try{ sub_tasa_interes_anual = new Double(request.getParameter("sub_tasa_interes_anual")); }catch(Exception e){}
        try{ sub_tasa_interes_mora = new Double(request.getParameter("sub_tasa_interes_mora")); }catch(Exception e){}
        try{ sub_gastos_cobranza = new Double(request.getParameter("sub_gastos_cobranza")); }catch(Exception e){}
        try{ sub_cat = new Double(request.getParameter("sub_cat")); }catch(Exception e){}
        try{ seguro_obligatorio = Integer.parseInt(request.getParameter("seguro_obligatorio")); }catch(Exception e){}
        
        
        if (idProductoPadre<=0)
            msgError += "<ul>El dato 'Producto Padre' es requerido para crear o editar un Sub-Producto.";
        if(!gc.isValidString(sub_nombre, 1, 50))
            msgError += "<ul>El dato 'nombre' es requerido, debe tener máximo 50 caracteres."; 
        if(!gc.isValidString(sub_descripcion, 1, 500))
            msgError += "<ul>El dato 'descripción' es requerido, debe tener máximo 500 caracteres."; 
        if (sub_monto<=0)
            msgError += "<ul>El dato 'Monto' es requerido.";
        if (sub_plazo<=0)
            msgError += "<ul>El dato 'Plazo' es requerido.";
        if (sub_tasa_interes_anual<=0)
            msgError += "<ul>El dato 'Taza de interes anual' es requerido.";
        if (sub_tasa_interes_mora<=0)
            msgError += "<ul>El dato 'Taza de interes moratoria' es requerido.";
        if (sub_gastos_cobranza<=0)
            msgError += "<ul>El dato 'Gastos de cobranza' es requerido.";
        if (sub_cat<=0)
            msgError += "<ul>El dato 'CAT' es requerido.";
        if (seguro_obligatorio<0)
            msgError += "<ul>El dato 'Seguro es obligatorio?' es requerido, indique una opción.";
        if(!gc.isValidString(seguro_tipo, 0, 100))
            msgError += "<ul>El dato 'Seguro - Tipo' es opcional y debe tener máximo 100 caracteres."; 
        if(!gc.isValidString(seguro_aseguradora, 0, 100))
            msgError += "<ul>El dato 'Seguro - Aseguradora' es opcional y debe tener máximo 100 caracteres."; 
        
        if (msgError.equals("")){
            CrProductoCredito crProductoCredito = null;
            if (idProductoSub>0){
                //edicion
                crProductoCredito = new CrProductoCreditoBO(idProductoSub, user.getConn()).getCrProductoCredito();
                crProductoCredito.setFechaHrUltimaEdicion(new Date());
            }else{
                // nuevo
                crProductoCredito = new CrProductoCredito();
                crProductoCredito.setFechaHrCreacion(new Date());
            }
            
            crProductoCredito.setIdProductoCreditoPadre(idProductoPadre);
            crProductoCredito.setNombre(sub_nombre);
            crProductoCredito.setDescripcion(sub_descripcion);
            crProductoCredito.setIdUsuarioEdicion(user.getUser().getIdUsuarios());
            // datos de producto padre
            crProductoCredito.setIdScore(0);
            crProductoCredito.setIdGrupoFormularioSolic(0);
            crProductoCredito.setIdGrupoFormularioVerif(0);
            crProductoCredito.setTipoAmortizacion(null);
            //datos para sub-producto
            crProductoCredito.setMonto(sub_monto);
            crProductoCredito.setPlazo(sub_plazo);
            crProductoCredito.setTasaInteresAnual(sub_tasa_interes_anual);
            crProductoCredito.setTasaInteresMora(sub_tasa_interes_mora);
            crProductoCredito.setGastosCobranza(sub_gastos_cobranza);
            crProductoCredito.setCostoAnualTotal(sub_cat);
            // fin datos para sub-producto
            crProductoCredito.setIdEstatus(sub_estatus);
            crProductoCredito.setIdEmpresa(idEmpresa);
            
            CrProductoCreditoDaoImpl crProductoCreditoDaoImpl = new CrProductoCreditoDaoImpl(user.getConn());
            try{
                if (crProductoCredito.getIdProductoCredito()>0){
                    //edicion
                    crProductoCreditoDaoImpl.update(crProductoCredito.createPk(), crProductoCredito);
                }else{
                    //nuevo
                    crProductoCreditoDaoImpl.insert(crProductoCredito);
                }
                
                //creamos registro de cr_producto_seguro
                try{
                    CrProductoSeguroDaoImpl crProductoSeguroDao = new CrProductoSeguroDaoImpl(user.getConn());
                    CrProductoSeguro crProductoSeguro = new CrProductoSeguro();
                    crProductoSeguro.setIdProductoCredito(crProductoCredito.getIdProductoCredito());
                    crProductoSeguro.setIsObligatorio(seguro_obligatorio);
                    crProductoSeguro.setTipoSeguro(seguro_tipo);
                    crProductoSeguro.setAseguradoraNombre(seguro_aseguradora);
                    crProductoSeguro.setIdEstatus(1);
                    crProductoSeguro.setIdEmpresa(crProductoCredito.getIdEmpresa());
                    
                    //Eliminamos los anteriores relacionados
                    CrProductoSeguro[] cpsPrevios = crProductoSeguroDao.findWhereIdProductoCreditoEquals(crProductoCredito.getIdProductoCredito());
                    for (CrProductoSeguro cps : cpsPrevios){
                        crProductoSeguroDao.delete(cps.createPk());
                    }
                    
                    //Creamos nuevo
                    crProductoSeguroDao.insert(crProductoSeguro);
                }catch(Exception ex){
                    
                }
                
                msgExitoExtra += "Registro almacenado exitosamente.";
            }catch(Exception ex){
                msgError += "<ul>Error inesperado al guardar Sub Producto Credito en base de datos: " + GenericMethods.exceptionStackTraceToString(ex);
            }
            
        }
        
    }else if (mode==7){
        //7 = Recupera Relacion Documento Imprimible
        if (crProdCredito == null)
            crProdCredito = new CrProductoCreditoSesion(user.getConn());
    }else if (mode==8){
        //8 = Agrega Relacion Documento Imprimible
        int idDocImprimible = -1;
        
        try{ idDocImprimible = Integer.parseInt(request.getParameter("id_doc_imprimible")); }catch(Exception e){}
        
        if(idDocImprimible<=0)
            msgError += "<ul>El dato 'Documento Imprimible' es requerido.";
        if (crProdCredito.getListaIdDocImprimibles().contains(new Integer(idDocImprimible)))
            msgError += "<ul>El Documento elegido ya esta seleccionado, no puede agregarse 2 veces el mismo Imprimible.";
        
        if (msgError.equals("")){
            crProdCredito.getListaIdDocImprimibles().add(idDocImprimible);
        }
        
    }else if (mode==9){
        //9 = Elimina Relacion Documento Imprimible
        int indexListaProdDocImp = -1;
        try{ indexListaProdDocImp = Integer.parseInt(request.getParameter("index_lista_doc_imprimible")); }catch(Exception e){}
        
        if(indexListaProdDocImp<0)
            msgError += "<ul>No seleccionó un registro válido a eliminar.";
        if(indexListaProdDocImp>=crProdCredito.getListaIdDocImprimibles().size())
            msgError += "<ul>El elemento de la lista que desea eliminar no es válido.";
        
        if (msgError.equals(""))
            crProdCredito.getListaIdDocImprimibles().remove(indexListaProdDocImp);
        
    }
    
    if (msgError.equals("") && mode!=0){
        out.print("<!--EXITO-->" + msgExitoExtra);
    }
%>
<%  
    if (msgError.equals("") && (mode==2 || mode==3 || mode==4)){
        int indexListaProdPuntajeMonto = 0;
        for (CrProductoPuntajeMonto crProductoPuntajeMonto : crProdCredito.getListaProductoPuntajeMonto()){
%>
        <tr>
            <td><%= crProductoPuntajeMonto.getRangoMin() %></td>
            <td><%= crProductoPuntajeMonto.getRangoMax() %></td>
            <td><%= crProductoPuntajeMonto.getPctAutorizado() %></td>
            <td><a href="#" onclick="eliminaRelacionPuntajeMonto(<%=indexListaProdPuntajeMonto%>);"><img src="../../images/icon_delete.png" /></a></td>
        </tr>
<%
            indexListaProdPuntajeMonto++;
        }
    }
%>
<%  
    if (msgError.equals("") && (mode==7 || mode==8 || mode==9)){
        int indexListaProdDocImp = 0;
        CrDocImprimibleBO crDocImprimibleBO = new CrDocImprimibleBO(user.getConn());
        for (Integer idDocImprimible : crProdCredito.getListaIdDocImprimibles()){
            CrDocImprimible docImprimible = crDocImprimibleBO.findCrDocImprimiblebyId(idDocImprimible);
%>
        <tr>
            <td><%= docImprimible.getTipoImprimible()%></td>
            <td><%= docImprimible.getNombre()%></td>
            <td><a href="#" onclick="eliminaRelacionImprimible(<%=indexListaProdDocImp%>);"><img src="../../images/icon_delete.png" /></a></td>
        </tr>
<%
            indexListaProdDocImp++;
        }
    }
%>
<%
    if (!msgError.equals("")){
        out.print("<!--ERROR-->"+msgError);
    }
%>