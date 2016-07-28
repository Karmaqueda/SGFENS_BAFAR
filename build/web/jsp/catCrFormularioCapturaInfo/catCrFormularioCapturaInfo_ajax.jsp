<%-- 
    Document   : catCrFormularioCapturaInfo_ajax
    Created on : 27/06/2016, 05:52:28 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.dao.jdbc.CrSolicitudBitacoraDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrSolicitudBitacora"%>
<%@page import="com.tsp.sct.dao.jdbc.CrFrmEventoSolicitudDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrFrmEventoSolicitud"%>
<%@page import="com.tsp.sct.dao.jdbc.CrProductoCreditoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrProductoCredito"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProspecto"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensProspectoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProspectoPk"%>
<%@page import="com.tsp.sct.cr.CrearProspecto"%>
<%@page import="com.tsp.sct.cr.RevisionesCr"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="com.tsp.sct.bo.CrFormularioValidacionBO"%>
<%@page import="com.tsp.sct.dao.jdbc.CrFormularioValidacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrFormularioValidacion"%>
<%@page import="com.tsp.sct.dao.jdbc.CrFormularioRespuestaDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrFormularioRespuesta"%>
<%@page import="com.tsp.sct.dao.dto.CrFormularioEventoPk"%>
<%@page import="com.tsp.sct.dao.jdbc.CrFormularioEventoDaoImpl"%>
<%@page import="com.tsp.sct.bo.ZonaHorariaBO"%>
<%@page import="com.tsp.sct.dao.dto.CrFormularioEvento"%>
<%@page import="com.tsp.sct.dao.dto.CrFormularioCampo"%>
<%@page import="com.tsp.sct.bo.CrFormularioCampoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.CrFormularioDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrFormulario"%>
<%@page import="com.tsp.sct.bo.CrFormularioBO"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.bo.CrGrupoFormularioBO"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.CrGrupoFormularioDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CrGrupoFormulario"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idCrGrupoFormulario = -1;
    int idCrFormularioEvento = 0;
    int id_producto_solicitado = 0;
    
    String tipoTratoSolicitud = "";//variable para saber si el usuario dio clic en enviar ya la solicitud a mesa de control o que trato se le va a dar
    int soloCamposRevision = 0;//variable para saber que un promotor va a revisar y hacer correcciones de los campos de la solicitud que se envio a revision
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    tipoTratoSolicitud = request.getParameter("tipoTratoSolicitud")!=null?request.getParameter("tipoTratoSolicitud"):"";
    try{
        idCrGrupoFormulario = Integer.parseInt(request.getParameter("idCrGrupoFormulario"));
    }catch(Exception ex){}
    try{
        idCrFormularioEvento = Integer.parseInt(request.getParameter("idCrFormularioEvento"));
    }catch(Exception ex){}
    try{
        id_producto_solicitado = Integer.parseInt(request.getParameter("id_producto_solicitado"));
    }catch(Exception ex){}    
    try {
        soloCamposRevision = Integer.parseInt(request.getParameter("soloCamposRevision"));
    } catch (NumberFormatException e) {}
    String queryModificado = "";//variable de ayuda para filtrar con query
    if(soloCamposRevision == 1){
        queryModificado = " AND id_formulario_campo IN (SELECT id_formulario_campo FROM cr_formulario_respuesta WHERE revisar = 1) ";
    }
    //nombre = request.getParameter("nombre")!=null?new String(request.getParameter("nombre").getBytes("ISO-8859-1"),"UTF-8"):"";    
    //descripcion = request.getParameter("descripcion")!=null?new String(request.getParameter("descripcion").getBytes("ISO-8859-1"),"UTF-8"):"";    
    /*try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(Exception ex){} 
    */
    
    //obtengo todos los formularios del grupo:
    CrFormularioBO crFormularioBO = new CrFormularioBO(user.getConn());    
    CrFormulario[] crFormularioDto = new CrFormulario[0];
    if (idCrGrupoFormulario > 0){
        //crFormularioDto = new CrFormularioDaoImpl(user.getConn()).findByDynamicWhere(" id_grupo_formulario = " + idGrupoFormulario + " AND id_estatus = 1 " , null);
        crFormularioBO.setOrderBy("ORDER BY orden_grupo ASC");
        crFormularioDto = crFormularioBO.findCrFormularios(-1, idEmpresa, 0, 0, " AND id_grupo_formulario = " + idCrGrupoFormulario + " AND id_estatus = 1");
    }
    
    //CrFormularioValidacion[] crFormularioValidacion = new CrFormularioValidacionBO(user.getConn()).findCrFormularioValidacions(0, idEmpresa, 0, 0, "");
    CrFormularioValidacionDaoImpl crFormularioValidacionDaoImpl = new CrFormularioValidacionDaoImpl(user.getConn());
        
    //variables para el control y consulta en Buro, Listas Negras y Cliente previo.
    String rfcValidar = "";
    String curpValidar = "";
    String nombre1Validar = "";
    String nombre2Validar = "";
    String apellidoPaternoValidar = "";
    String apellidoMaternoValidar = "";
    String razonSocial_o_nombreCompleto = "";
    ///---
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    //obtenemos todas las variables necesarias, en relacion al formulario dinamico
    for(CrFormulario formularios : crFormularioDto){
        CrFormularioCampoBO crFormularioCampoBO = new CrFormularioCampoBO(user.getConn());
        crFormularioCampoBO.setOrderBy(" ORDER BY orden_formulario ASC ");
        CrFormularioCampo[] crFormularioCampo = crFormularioCampoBO.findCrFormularioCampos(0, idEmpresa, 0, 0, (" AND id_formulario = " + formularios.getIdFormulario() + " AND id_estatus = 1 " + queryModificado ));
        for(CrFormularioCampo formulario : crFormularioCampo){
            String valorCampoPersonalizado = request.getParameter("id_formulario_campo_valor_"+formulario.getIdFormularioCampo())!=null?new String(request.getParameter("id_formulario_campo_valor_"+formulario.getIdFormularioCampo()).getBytes("ISO-8859-1"),"UTF-8"):"";            
            if(user.getUser().getIdRoles() == RolesBO.ROL_PROMOTOR){
                if(formulario.getIsRequerido()==1 && valorCampoPersonalizado.trim().equals("")){//es requerido
                    msgError += "<ul>El dato '"+formulario.getEtiqueta()+"' es requerido.";
                }
            }
            //if(formulario.getIdFormularioValidacion() == 1 && !valorCampoPersonalizado.trim().equals("")){
            if(user.getUser().getIdRoles() == RolesBO.ROL_PROMOTOR){
                if(formulario.getIdFormularioValidacion() > 0){
                    CrFormularioValidacion crFormularioValidacion = crFormularioValidacionDaoImpl.findByPrimaryKey(formulario.getIdFormularioValidacion());
                        Pattern pat = null;
                        Matcher mat = null;        
                        pat = Pattern.compile(crFormularioValidacion.getRegexJava());
                        mat = pat.matcher(valorCampoPersonalizado);
                        if (mat.find()) {
                            System.out.println("[" + mat.group() + "]");                        
                        }else{
                            msgError += "<ul>El dato '"+formulario.getEtiqueta()+"' es erroneo; " + crFormularioValidacion.getDescripcion() +".";
                        }               
                /*}else if(formulario.getIdFormularioValidacion() == 2 && !valorCampoPersonalizado.trim().equals("")){

                }*/
                }
            }
            if(mode.equals("")){//prospecto nuevo, solo si es nuevo cargamos los datos de las validaciones en buro, listas negras y cliente previo                    
                if(formulario.getVariableFormula() != null && formulario.getVariableFormula().indexOf("SOL_RFC") > -1){
                    rfcValidar = valorCampoPersonalizado;
                }else if(formulario.getVariableFormula() != null && formulario.getVariableFormula().indexOf("SOL_CURP") > -1){
                    curpValidar = valorCampoPersonalizado;
                }else if(formulario.getVariableFormula() != null && formulario.getVariableFormula().indexOf("SOL_NOMBRE_PRIMER") > -1){
                    nombre1Validar = valorCampoPersonalizado;
                }else if(formulario.getVariableFormula() != null && formulario.getVariableFormula().indexOf("SOL_NOMBRE_SEGUNDO") > -1){
                    nombre2Validar = valorCampoPersonalizado;
                }else if(formulario.getVariableFormula() != null && formulario.getVariableFormula().indexOf("SOL_APELLIDO_PAT") > -1){
                    apellidoPaternoValidar = valorCampoPersonalizado;
                }else if(formulario.getVariableFormula() != null && formulario.getVariableFormula().indexOf("SOL_APELLIDO_MAT") > -1){
                    apellidoMaternoValidar = valorCampoPersonalizado;
                }else if(formulario.getVariableFormula() != null && formulario.getVariableFormula().indexOf("SOL_RAZON_SOCIAL") > -1){
                    razonSocial_o_nombreCompleto = valorCampoPersonalizado;
                }
            }
        }
    }
        
    if(mode.equals("")){//prospecto nuevo, solo si es nuevo realizamos las validaciones en buro, listas negras y cliente previo 
        RevisionesCr revisionesCr = new RevisionesCr();
        boolean aprobado = revisionesCr.BuroCredito(rfcValidar, curpValidar, nombre1Validar, nombre2Validar, apellidoPaternoValidar, apellidoMaternoValidar, razonSocial_o_nombreCompleto);
        if(!aprobado){
            msgError += "<ul>El prospecto con RFC '"+rfcValidar+"' no fue aprobado para un crédito.";
        }
    }
    
/*    if(!gc.isValidString(nombre, 1, 50))
        msgError += "<ul>El dato 'nombre' es requerido, debe tener máximo 50 caracteres."; 
    if(!gc.isValidString(descripcion, 1, 500))
        msgError += "<ul>El dato 'descripción' es requerido, debe tener máximo 500 caracteres."; 
    if(idCrGrupoFormulario <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'CrGrupoFormulario' es requerido para ediciones";
*/    
    if(msgError.equals("")){
        if(idCrGrupoFormulario>0){
            if (mode.equals("1") && idCrFormularioEvento > 0){
                /*
                * Editar
                */
                try{
                    //Actualizamos el registro de CrFormularioEvento
                    CrFormularioEvento crFormularioEvento = new CrFormularioEventoDaoImpl(user.getConn()).findByPrimaryKey(idCrFormularioEvento);
                    crFormularioEvento.setFechaHrEdicion(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                    //crFormularioEvento.setIdUsuarioCapturo(user.getUser().getIdUsuarios());//no editamos el usuario que modifico
                    new CrFormularioEventoDaoImpl(user.getConn()).update(crFormularioEvento.createPk(), crFormularioEvento);

                    //actualizamos los registros de la tabla respuesta
                    //obtenemos todas las variables necesarias, en relacion al formulario dinamico y cargamos datos en el objeto para almacenarlo
                    CrFormularioRespuestaDaoImpl crFormularioRespuestaDaoImpl = new CrFormularioRespuestaDaoImpl(user.getConn());
                    ////---actualizamos tambien el registro de prospecto en la tabla de SgfensProspecto
                    CrearProspecto crearProspecto = new CrearProspecto();
                    if(crFormularioEvento.getIdEntidadRespondio() > 0){
                        try{
                            SgfensProspecto prospecto = new SgfensProspectoDaoImpl(user.getConn()).findByPrimaryKey(crFormularioEvento.getIdEntidadRespondio());
                            crearProspecto.setProspecto(prospecto);
                        }catch(Exception e){}
                    }
                    ////---
                    int cantidadRegistroNoCalificado = 0;//variable para saber cuantos registros tienen activo el campo de Revision, esto para mesa de control                    
                        for(CrFormulario formularios : crFormularioDto){
                            CrFormularioCampoBO crFormularioCampoBO = new CrFormularioCampoBO(user.getConn());
                            crFormularioCampoBO.setOrderBy(" ORDER BY orden_formulario ASC ");
                            CrFormularioCampo[] crFormularioCampo = crFormularioCampoBO.findCrFormularioCampos(0, idEmpresa, 0, 0, (" AND id_formulario = " + formularios.getIdFormulario() + " AND id_estatus = 1 " + queryModificado ));
                            for(CrFormularioCampo formulario : crFormularioCampo){
                                String valorCampo = request.getParameter("id_formulario_campo_valor_"+formulario.getIdFormularioCampo())!=null?new String(request.getParameter("id_formulario_campo_valor_"+formulario.getIdFormularioCampo()).getBytes("ISO-8859-1"),"UTF-8"):"";
                                int valorCampoValidar = 0;//para recuperar el valor del checkbox de revision
                                try{ valorCampoValidar = Integer.parseInt(request.getParameter("revisar_"+formulario.getIdFormularioCampo()));}catch(Exception e){}
                                String revisarComentario = request.getParameter("revisar_comen_"+formulario.getIdFormularioCampo())!=null?new String(request.getParameter("revisar_comen_"+formulario.getIdFormularioCampo()).getBytes("ISO-8859-1"),"UTF-8"):"";
                                if(valorCampoValidar > 0){
                                    cantidadRegistroNoCalificado++;
                                }

                                crearProspecto.cargaDatos(formulario.getVariableFormula()!=null?formulario.getVariableFormula().trim():"", valorCampo);//carga de datos del prospecto
                                
                                CrFormularioRespuesta crFormularioRespuesta = null;
                                try{                                
                                    crFormularioRespuesta = new CrFormularioRespuestaDaoImpl(user.getConn()).findByDynamicWhere(" id_formulario_evento = " + idCrFormularioEvento + " AND id_formulario_campo = " + formulario.getIdFormularioCampo(), null)[0];
                                }catch(Exception e){}
                                if(crFormularioRespuesta != null){
                                    crFormularioRespuesta.setIdFormularioEvento(crFormularioEvento.getIdFormularioEvento());
                                    crFormularioRespuesta.setIdFormulario(formulario.getIdFormulario());
                                    crFormularioRespuesta.setIdFormularioCampo(formulario.getIdFormularioCampo());
                                    crFormularioRespuesta.setValor(valorCampo);
                                    crFormularioRespuesta.setIdEmpresa(idEmpresa);
                                    crFormularioRespuesta.setIdEstatus(1);
                                    crFormularioRespuesta.setRevisar(valorCampoValidar);
                                    crFormularioRespuesta.setRevisarComentario(revisarComentario);
                                    crFormularioRespuestaDaoImpl.update(crFormularioRespuesta.createPk(), crFormularioRespuesta);
                                }else{
                                    crFormularioRespuesta = new CrFormularioRespuesta();
                                    crFormularioRespuesta.setIdFormularioEvento(crFormularioEvento.getIdFormularioEvento());
                                    crFormularioRespuesta.setIdFormulario(formulario.getIdFormulario());
                                    crFormularioRespuesta.setIdFormularioCampo(formulario.getIdFormularioCampo());
                                    crFormularioRespuesta.setValor(valorCampo);
                                    crFormularioRespuesta.setIdEmpresa(idEmpresa);
                                    crFormularioRespuesta.setIdEstatus(1);
                                    crFormularioRespuesta.setRevisar(valorCampoValidar);
                                    crFormularioRespuesta.setRevisarComentario(revisarComentario);
                                    crFormularioRespuestaDaoImpl.insert(crFormularioRespuesta);
                                }


                            }
                        }  
                        //actualizar el propecto
                        crearProspecto.modificarProspecto(idEmpresa, user.getDatosUsuario().getIdDatosUsuario(), user.getConn());
                        
                        ///---validaciones de ver que credito es aceptable para el prospecto                            
                            double montoTotal = 0; 
                            double primerTotal = 0;
                            double segundoTotal = 0;
                            double tercerTotal = 0;
                            double ingresosEgresosCapacidadPago = 0; //variable para la capacidad de Pago
                            double montoSolicitado = 0;
                            double edad = 0;
                            double montoMensualConyugue = 0;
                            double montoMensualSueldo = 0;
                            double montoMensualFamiliar = 0;
                            int contadorTotales= 0;//variable para saber cuantos totales hay, asi como para independizar cada total
                            if(!tipoTratoSolicitud.equals("guardarCorreccionSolicitud")){//validacion para ver si se trato solo de una correccion de campos por revisar, de ser que no validamos datos de balance
                                CrFormularioCampo[] crFormularioCampo = new CrFormularioCampo[0];
                                for(CrFormulario formularios : crFormularioDto){
                                    montoTotal=0;
                                    CrFormularioCampoBO crFormularioCampoBO = new CrFormularioCampoBO(user.getConn());
                                                            crFormularioCampoBO.setOrderBy(" ORDER BY orden_formulario ASC ");
                                                            crFormularioCampo = crFormularioCampoBO.findCrFormularioCampos(0, idEmpresa, 0, 0, (" AND id_formulario = " + formularios.getIdFormulario() + " AND id_estatus = 1 " ));
                                                            for(CrFormularioCampo formulario : crFormularioCampo){
                                                                CrFormularioRespuesta crFormularioRespuesta = null;
                                                                try{
                                                                    crFormularioRespuesta = new CrFormularioRespuestaDaoImpl(user.getConn()).findByDynamicWhere(" id_formulario_evento = " + idCrFormularioEvento + " AND id_formulario_campo = " + formulario.getIdFormularioCampo(), null)[0];
                                                                }catch(Exception e){}
                                                                if(crFormularioRespuesta!=null){
                                                                    if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("LAB_SUELDO_NETO") > -1){
                                                                        try{montoMensualSueldo = Double.parseDouble(crFormularioRespuesta.getValor());}catch(Exception e){}
                                                                    }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("CNY_LAB_SUELDO") > -1){
                                                                        try{montoMensualConyugue = Double.parseDouble(crFormularioRespuesta.getValor());}catch(Exception e){}
                                                                    }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("OTIN_UTILIDAD_MENS") > -1){
                                                                        try{montoMensualFamiliar = Double.parseDouble(crFormularioRespuesta.getValor());}catch(Exception e){}
                                                                    }
                                                                }                                                                
                                                                if(formularios.getNombre().trim().toUpperCase().indexOf("BALANCE") > -1){
                                                                    try{montoTotal += (crFormularioRespuesta!=null?(Double.parseDouble(crFormularioRespuesta.getValor())):0.00);}catch(Exception e){}

                                                                    if(formulario.getVariableFormula()!=null && formulario.getVariableFormula().trim().toUpperCase().indexOf("GFAM_MONTO_SOLICITADO") > -1){
                                                                        try{montoSolicitado += (crFormularioRespuesta!=null?(Double.parseDouble(crFormularioRespuesta.getValor())):0.00);}catch(Exception e){}
                                                                    }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("GFAM_INGRESOS_MENOS_EGRESOS") > -1){
                                                                        System.out.println("---- montoMensualSueldo: " + montoMensualSueldo);
                                                                        System.out.println("---- montoMensualConyugue: " + montoMensualConyugue);
                                                                        System.out.println("---- montoMensualFamiliar: " + montoMensualFamiliar);
                                                                        System.out.println("---- primerTotal: " + primerTotal);
                                                                        System.out.println("---- Resultado de ingresos menos egresos: " + ((montoMensualSueldo + montoMensualConyugue + montoMensualFamiliar) - primerTotal));
                                                                        //ingresosEgresosCapacidadPago = (segundoTotal - primerTotal);
                                                                        ingresosEgresosCapacidadPago = ((montoMensualSueldo + montoMensualConyugue + montoMensualFamiliar) - primerTotal);
                                                                    }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("GFAM_TOTAL_EGRESOS") > -1){
                                                                        
                                                                            primerTotal = montoTotal;
                                                                        
                                                                        contadorTotales++;
                                                                        montoTotal=0;//reiniciamos el monto del total, para la sumatoria de los siguientes registros                                                                            
                                                                    }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("GFAM_TOTAL_INGRESOS") > -1){
                                                                        System.out.println("---- TOTAL: " + montoTotal);
                                                                        
                                                                            segundoTotal = (montoMensualSueldo + montoMensualConyugue + montoMensualFamiliar);
                                                                        
                                                                        contadorTotales++;
                                                                        montoTotal=0;//reiniciamos el monto del total, para la sumatoria de los siguientes registros                                                                            
                                                                    }
                                                                }else{
                                                                    if(formulario.getVariableFormula()!=null && formulario.getVariableFormula().trim().toUpperCase().indexOf("SOL_EDAD") > -1){
                                                                        edad = (crFormularioRespuesta!=null?(Double.parseDouble(crFormularioRespuesta.getValor())):0);
                                                                    }
                                                                }
                                                            }
                                }
                            }
                            //validaciones de montos, reglas de rechazo. 
                            RevisionesCr revisionesCr = new RevisionesCr();
                            if(tipoTratoSolicitud.equals("guardarEnviar")){
                                if(ingresosEgresosCapacidadPago > 0){
                                    String mensajeRechazo = "";                                    
                                    mensajeRechazo = revisionesCr.reglasRechazo(user.getConn(), idEmpresa, id_producto_solicitado, ingresosEgresosCapacidadPago, montoSolicitado, edad);
                                    if(mensajeRechazo.equals("")){
                                        String mensajeRevision = revisionesCr.creditoAplicable(ingresosEgresosCapacidadPago, user.getConn(), idEmpresa);
                                        if(mensajeRevision.equals("")){//si no hay mensaje es que se registra con el producto seleccionado
                                            revisionesCr.almacenaRegistroSolicitudBitacora(user.getConn(), idCrFormularioEvento, id_producto_solicitado, idEmpresa, user.getUser().getIdUsuarios(), 2, "Aprobado con producto seleccionado, pasa a mesa de control.", false);
                                            out.print("<!--EXITO-->Registro actualizado satisfactoriamente y enviado a mesa de control");
                                        }else{//si hay mensaje de error, entonces se busco un producto posible
                                            if(revisionesCr.idProductoAceptado > 0){//si hay ID de producto lo enviamos a vista de que producto será el marcado
                                                revisionesCr.almacenaRegistroSolicitudBitacora(user.getConn(), idCrFormularioEvento, id_producto_solicitado, idEmpresa, user.getUser().getIdUsuarios(), 2, "Aprobado con producto propuesto por sistema, pasa a mesa de control.", false);
                                                out.print("<!--EXITO_OTRO_PRODUCTO-->El producto seleccionado no cumple con el monto de cuota mensual, el producto propuesto que cumple es: " + revisionesCr.crProductoCreditoPropuesto.getNombre() + " y se usará como producto seleccionado.");
                                            }else{//si no hay producto sugerido, lo marcamos como rechazado
                                                //almacenamos el registro en "cr_frm_evento_solicitud" con estatus de rechazado, asi como en "cr_solicitud_bitacora"
                                                revisionesCr.almacenaRegistroSolicitudBitacora(user.getConn(), idCrFormularioEvento, id_producto_solicitado, idEmpresa, user.getUser().getIdUsuarios(), 1, "Ingresos insuficientes.", false);
                                                out.print("<!--ERROR_NO_APROBADO-->"+"Ingresos insuficientes.");
                                            }
                                        }
                                    }else{
                                        //almacenamos el registro en "cr_frm_evento_solicitud" con estatus de rechazado, asi como en "cr_solicitud_bitacora"
                                        revisionesCr.almacenaRegistroSolicitudBitacora(user.getConn(), idCrFormularioEvento, id_producto_solicitado, idEmpresa, user.getUser().getIdUsuarios(), 1, mensajeRechazo.replaceAll("</br>", " "), false);                                            
                                        out.print("<!--ERROR_NO_APROBADO-->"+mensajeRechazo);

                                    }
                                }else{
                                    out.print("<!--ERROR-->El monto de egresos es mayor que sus ingresos");
                                } 
                            }else if(tipoTratoSolicitud.equals("guardarSoloBorrador")){
                                revisionesCr.almacenaRegistroSolicitudBitacora(user.getConn(), idCrFormularioEvento, id_producto_solicitado, idEmpresa, user.getUser().getIdUsuarios(), 99, "Borrador", false);
                                out.print("<!--EXITO-->Registro actualizado satisfactoriamente.");
                            }else if(tipoTratoSolicitud.equals("guardarVerificacionMesaControl")){
                                if(cantidadRegistroNoCalificado > 0){//si se marco al menos un registro de campo para revision
                                   revisionesCr.almacenaRegistroSolicitudBitacora(user.getConn(), idCrFormularioEvento, id_producto_solicitado, idEmpresa, user.getUser().getIdUsuarios(), 3, "Marcada para ser revisada, presenta algún detalle por corregir", false); 
                                   out.print("<!--EXITO-->Solicitud enviada a Revisión.");
                                }else{
                                   revisionesCr.almacenaRegistroSolicitudBitacora(user.getConn(), idCrFormularioEvento, id_producto_solicitado, idEmpresa, user.getUser().getIdUsuarios(), 4, "Aprobada por Mesa de control", false);  
                                   out.print("<!--EXITO-->Solicitud aprobada por Mesa de Control.");
                                }   
                            }else if(tipoTratoSolicitud.equals("guardarRechazoMesaControl")){
                                revisionesCr.almacenaRegistroSolicitudBitacora(user.getConn(), idCrFormularioEvento, id_producto_solicitado, idEmpresa, user.getUser().getIdUsuarios(), 1, "Rechazada por Mesa de control", false);  
                            }else if(tipoTratoSolicitud.equals("guardarCorreccionSolicitud")){
                                revisionesCr.almacenaRegistroSolicitudBitacora(user.getConn(), idCrFormularioEvento, id_producto_solicitado, idEmpresa, user.getUser().getIdUsuarios(), 2, "Actualización de datos por revisar", false);
                                out.print("<!--EXITO-->Registro actualizado satisfactoriamente.");
                            }
                            ///---fin                    
                        
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }


            }else if(mode.equals("")){
                /*
                *  Nuevo
                */
                
                //Creamos el registro de evento
                try{
                    CrFormularioEvento crFormularioEvento = new CrFormularioEvento();
                    crFormularioEvento.setIdGrupoFormulario(idCrGrupoFormulario);
                    crFormularioEvento.setFechaHrCreacion(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                    crFormularioEvento.setIdUsuarioCapturo(user.getUser().getIdUsuarios());
                    crFormularioEvento.setTipoEntidadRespondio("PROSPECTO");
                    crFormularioEvento.setIdEntidadRespondio(0);//validar correctamente si se envia Cero
                    crFormularioEvento.setIdEmpresa(idEmpresa);
                    crFormularioEvento.setIdEstatus(1);

                    CrFormularioEventoPk crFormularioEventoPk = new CrFormularioEventoDaoImpl(user.getConn()).insert(crFormularioEvento);

                    idCrFormularioEvento = crFormularioEventoPk.getIdFormularioEvento();
                    
                    CrFormularioRespuestaDaoImpl crFormularioRespuestaDaoImpl = new CrFormularioRespuestaDaoImpl(user.getConn());
                    ////---crearemos tambien el registro de prospecto en la tabla de SgfensProspecto
                    CrearProspecto crearProspecto = new CrearProspecto();
                    ////---
                    //obtenemos todas las variables necesarias, en relacion al formulario dinamico y cargamos datos en el objeto para almacenarlo
                    int cantidadRegistroNoCalificado = 0;//variable para saber cuantos registros tienen activo el campo de Revision, esto para mesa de control
                    for(CrFormulario formularios : crFormularioDto){
                        CrFormularioCampoBO crFormularioCampoBO = new CrFormularioCampoBO(user.getConn());
                        crFormularioCampoBO.setOrderBy(" ORDER BY orden_formulario ASC ");
                        CrFormularioCampo[] crFormularioCampo = crFormularioCampoBO.findCrFormularioCampos(0, idEmpresa, 0, 0, (" AND id_formulario = " + formularios.getIdFormulario()));
                        for(CrFormularioCampo formulario : crFormularioCampo){
                            String valorCampo = request.getParameter("id_formulario_campo_valor_"+formulario.getIdFormularioCampo())!=null?new String(request.getParameter("id_formulario_campo_valor_"+formulario.getIdFormularioCampo()).getBytes("ISO-8859-1"),"UTF-8"):"";
                            int valorCampoValidar = 0;//para recuperar el valor del checkbox de revision
                            try{ valorCampoValidar = Integer.parseInt(request.getParameter("revisar_"+formulario.getIdFormularioCampo()));}catch(Exception e){}
                            String revisarComentario = request.getParameter("revisar_comen_"+formulario.getIdFormularioCampo())!=null?new String(request.getParameter("revisar_comen_"+formulario.getIdFormularioCampo()).getBytes("ISO-8859-1"),"UTF-8"):"";
                            if(valorCampoValidar > 0){
                                cantidadRegistroNoCalificado++;
                            }

                            crearProspecto.cargaDatos(formulario.getVariableFormula()!=null?formulario.getVariableFormula().trim():"", valorCampo);//carga de datos del prospecto
                                    
                            CrFormularioRespuesta crFormularioRespuesta = new CrFormularioRespuesta();
                            crFormularioRespuesta.setIdFormularioEvento(crFormularioEventoPk.getIdFormularioEvento());
                            crFormularioRespuesta.setIdFormulario(formulario.getIdFormulario());
                            crFormularioRespuesta.setIdFormularioCampo(formulario.getIdFormularioCampo());
                            crFormularioRespuesta.setValor(valorCampo);
                            crFormularioRespuesta.setIdEmpresa(idEmpresa);
                            crFormularioRespuesta.setIdEstatus(1);
                            crFormularioRespuesta.setRevisar(valorCampoValidar);
                            crFormularioRespuesta.setRevisarComentario(revisarComentario);

                            crFormularioRespuestaDaoImpl.insert(crFormularioRespuesta);

                        }
                    }
                    
                    //crear el propecto
                    SgfensProspectoPk crearProspectoPk = crearProspecto.creaProspecto(idEmpresa, user.getDatosUsuario().getIdDatosUsuario(), user.getConn());
                    //solo actualizamos de new el registro de evento, enviadole el Id del prospecto creado
                    if(crearProspectoPk != null){
                        CrFormularioEventoDaoImpl crFormularioEventoDaoImpl = new CrFormularioEventoDaoImpl(user.getConn());
                        CrFormularioEvento crFormularioEventoActualizar = crFormularioEventoDaoImpl.findByPrimaryKey(crFormularioEventoPk.getIdFormularioEvento());
                        crFormularioEventoActualizar.setIdEntidadRespondio(crearProspectoPk.getIdProspecto());
                        crFormularioEventoDaoImpl.update(crFormularioEventoActualizar.createPk(), crFormularioEventoActualizar);
                    }
                    
                    ///---validaciones de ver que credito es aceptable para el prospecto
                    double montoTotal = 0; 
                    double primerTotal = 0;
                    double segundoTotal = 0;
                    double tercerTotal = 0;
                    double ingresosEgresosCapacidadPago = 0; //variable para la capacidad de Pago
                    double montoSolicitado = 0;
                    double edad = 0;
                    double montoMensualConyugue = 0;
                    double montoMensualSueldo = 0;
                    double montoMensualFamiliar = 0;
                    int contadorTotales= 0;//variable para saber cuantos totales hay, asi como para independizar cada total
                    for(CrFormulario formularios : crFormularioDto){
                        montoTotal=0;
                        CrFormularioCampoBO crFormularioCampoBO = new CrFormularioCampoBO(user.getConn());
                                                crFormularioCampoBO.setOrderBy(" ORDER BY orden_formulario ASC ");
                                                CrFormularioCampo[] crFormularioCampo = crFormularioCampoBO.findCrFormularioCampos(0, idEmpresa, 0, 0, (" AND id_formulario = " + formularios.getIdFormulario() + " AND id_estatus = 1 " ));
                                                for(CrFormularioCampo formulario : crFormularioCampo){
                                                    CrFormularioRespuesta crFormularioRespuesta = null;
                                                    try{
                                                        crFormularioRespuesta = new CrFormularioRespuestaDaoImpl(user.getConn()).findByDynamicWhere(" id_formulario_evento = " + crFormularioEventoPk.getIdFormularioEvento() + " AND id_formulario_campo = " + formulario.getIdFormularioCampo(), null)[0];
                                                    }catch(Exception e){}
                                                    if(crFormularioRespuesta!=null){
                                                        if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("LAB_SUELDO_NETO") > -1){
                                                            try{montoMensualSueldo = Double.parseDouble(crFormularioRespuesta.getValor());}catch(Exception e){}
                                                        }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("CNY_LAB_SUELDO") > -1){
                                                            try{montoMensualConyugue = Double.parseDouble(crFormularioRespuesta.getValor());}catch(Exception e){}
                                                        }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("OTIN_UTILIDAD_MENS") > -1){
                                                            try{montoMensualFamiliar = Double.parseDouble(crFormularioRespuesta.getValor());}catch(Exception e){}
                                                        }
                                                    }
                                                    if(formularios.getNombre().trim().toUpperCase().indexOf("BALANCE") > -1){
                                                        try{montoTotal += (crFormularioRespuesta!=null?(Double.parseDouble(crFormularioRespuesta.getValor())):0.00);}catch(Exception e){}

                                                        if(formulario.getVariableFormula()!=null && formulario.getVariableFormula().trim().toUpperCase().indexOf("GFAM_MONTO_SOLICITADO") > -1){
                                                            try{montoSolicitado += (crFormularioRespuesta!=null?(Double.parseDouble(crFormularioRespuesta.getValor())):0.00);}catch(Exception e){}
                                                        }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("GFAM_INGRESOS_MENOS_EGRESOS") > -1){
                                                            System.out.println("---- Resultado de ingresos menos egresos: " + ((montoMensualSueldo + montoMensualConyugue + montoMensualFamiliar) - primerTotal));
                                                            //ingresosEgresosCapacidadPago = (segundoTotal - primerTotal);
                                                            ingresosEgresosCapacidadPago = ((montoMensualSueldo + montoMensualConyugue + montoMensualFamiliar) - primerTotal);
                                                        }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("GFAM_TOTAL_EGRESOS") > -1){

                                                                primerTotal = montoTotal;

                                                            contadorTotales++;
                                                            montoTotal=0;//reiniciamos el monto del total, para la sumatoria de los siguientes registros                                                                            
                                                        }else if(formulario.getVariableFormula()!= null && formulario.getVariableFormula().trim().toUpperCase().indexOf("GFAM_TOTAL_INGRESOS") > -1){
                                                            System.out.println("---- TOTAL: " + montoTotal);

                                                                segundoTotal = (montoMensualSueldo + montoMensualConyugue + montoMensualFamiliar);

                                                            contadorTotales++;
                                                            montoTotal=0;//reiniciamos el monto del total, para la sumatoria de los siguientes registros                                                                            
                                                        }
                                                    }else{
                                                        if(formulario.getEtiqueta().trim().toUpperCase().indexOf("SOL_EDAD") > -1){
                                                            edad = (crFormularioRespuesta!=null?(Double.parseDouble(crFormularioRespuesta.getValor())):0);
                                                        }
                                                    }
                                                }
                    }                    
                    //validaciones de montos, reglas de rechazo. 
                            RevisionesCr revisionesCr = new RevisionesCr();
                            if(tipoTratoSolicitud.equals("guardarEnviar")){
                                if(ingresosEgresosCapacidadPago > 0){
                                    String mensajeRechazo = "";                                    
                                    mensajeRechazo = revisionesCr.reglasRechazo(user.getConn(), idEmpresa, id_producto_solicitado, ingresosEgresosCapacidadPago, montoSolicitado, edad);
                                    if(mensajeRechazo.equals("")){
                                        String mensajeRevision = revisionesCr.creditoAplicable(ingresosEgresosCapacidadPago, user.getConn(), idEmpresa);
                                        if(mensajeRevision.equals("")){//si no hay mensaje es que se registra con el producto seleccionado
                                            revisionesCr.almacenaRegistroSolicitudBitacora(user.getConn(), idCrFormularioEvento, id_producto_solicitado, idEmpresa, user.getUser().getIdUsuarios(), 2, "Aprobado con producto seleccionado, pasa a mesa de control.", true);
                                            out.print("<!--EXITO-->Registro insertado satisfactoriamente y enviado a mesa de control");
                                        }else{//si hay mensaje de error, entonces se busco un producto posible
                                            if(revisionesCr.idProductoAceptado > 0){//si hay ID de producto lo enviamos a vista de que producto será el marcado
                                                revisionesCr.almacenaRegistroSolicitudBitacora(user.getConn(), idCrFormularioEvento, id_producto_solicitado, idEmpresa, user.getUser().getIdUsuarios(), 2, "Aprobado con producto propuesto por sistema, pasa a mesa de control.", true);
                                                out.print("<!--EXITO_OTRO_PRODUCTO-->El producto seleccionado no cumple con el monto de cuota mensual, el producto propuesto que cumple es: " + revisionesCr.crProductoCreditoPropuesto.getNombre() + " y se usará como producto seleccionado.");
                                            }else{//si no hay producto sugerido, lo marcamos como rechazado
                                                //almacenamos el registro en "cr_frm_evento_solicitud" con estatus de rechazado, asi como en "cr_solicitud_bitacora"
                                                revisionesCr.almacenaRegistroSolicitudBitacora(user.getConn(), idCrFormularioEvento, id_producto_solicitado, idEmpresa, user.getUser().getIdUsuarios(), 1, "Ingresos insuficientes.", true);
                                                out.print("<!--ERROR_NO_APROBADO-->"+"Ingresos insuficientes.");
                                            }
                                        }
                                    }else{
                                        //almacenamos el registro en "cr_frm_evento_solicitud" con estatus de rechazado, asi como en "cr_solicitud_bitacora"
                                        revisionesCr.almacenaRegistroSolicitudBitacora(user.getConn(), idCrFormularioEvento, id_producto_solicitado, idEmpresa, user.getUser().getIdUsuarios(), 1, mensajeRechazo.replaceAll("</br>", " "), true);                                            
                                        out.print("<!--ERROR_NO_APROBADO-->"+mensajeRechazo);

                                    }
                                }else{
                                    out.print("<!--ERROR-->El monto de egresos es mayor que sus ingresos");
                                } 
                            }else if(tipoTratoSolicitud.equals("guardarSoloBorrador")){
                                revisionesCr.almacenaRegistroSolicitudBitacora(user.getConn(), idCrFormularioEvento, id_producto_solicitado, idEmpresa, user.getUser().getIdUsuarios(), 99, "Borrador", true);
                                out.print("<!--EXITO-->Registro insertado satisfactoriamente.");
                            }else{  
                                revisionesCr.almacenaRegistroSolicitudBitacora(user.getConn(), idCrFormularioEvento, id_producto_solicitado, idEmpresa, user.getUser().getIdUsuarios(), 99, "Borrador", true);
                                out.print("<!--EXITO-->Registro insertado...");
                            }
                            ///---fin                      
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo crear el registro. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }
                
       
            }else{
                out.print("<!--ERROR-->Acción no válida.");
            }
        }else{
            

        }
    } else {
        out.print("<!--ERROR-->"+msgError);
    }

%>