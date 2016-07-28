<%-- 
    Document   : cxp_vale_azul_ajax
    Created on : 13/04/2015, 13/04/2015 05:16:16 PM
    Author     : ISCesar
--%>
<%@page import="com.tsp.sct.dao.jdbc.FoliosDaoImpl"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.tsp.sct.bo.FoliosBO"%>
<%@page import="com.tsp.sct.dao.dto.Folios"%>
<%@page import="com.tsp.sct.dao.jdbc.CxpValeAzulDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.CxpValeAzul"%>
<%@page import="com.tsp.sct.bo.CxpValeAzulBO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idCxPValeAzul = -1;
    int idFolios = -1;
    String concepto ="";
    double importe = -1;  
    Date fecha_pago = null;
    Date fecha_control = null;
    int estatus = 2;//deshabilitado
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idCxPValeAzul = Integer.parseInt(request.getParameter("id_cxp_vale_azul"));
    }catch(NumberFormatException ex){}
    try{
        idFolios = Integer.parseInt(request.getParameter("id_serie"));
    }catch(NumberFormatException ex){}
    concepto = request.getParameter("concepto")!=null?new String(request.getParameter("concepto").getBytes("ISO-8859-1"),"UTF-8"):"";
    try{
        importe = Double.parseDouble(request.getParameter("importe"));
    }catch(NumberFormatException ex){}
    try{ fecha_pago = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fecha_pago"));}catch(Exception e){}
    try{ fecha_control = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fecha_control"));}catch(Exception e){}
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}   
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();    
    if(!gc.isValidString(concepto, 1, 500))
        msgError += "<ul>El dato 'concepto' es requerido. Máximo 500 caracteres.";
    if(idFolios <= 0)
        msgError += "<ul>El dato 'Serie' es requerido"; 
    if(importe <= 0)
        msgError += "<ul>El dato 'Importe' es requerido, y debe ser un valor positivo."; 
    if (fecha_pago==null)
           msgError+="<ul> Debe seleccionar una fecha de pago. ";
    if (fecha_control==null)
           msgError+="<ul> Debe seleccionar una fecha de aplicación (control). ";
    if(idCxPValeAzul <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'Vale Azul' es requerido en ediciones.";

    if(msgError.equals("")){
        if(idCxPValeAzul>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                CxpValeAzulBO valeAzulBO = new CxpValeAzulBO(idCxPValeAzul,user.getConn());
                CxpValeAzul cxpValeAzulDto = valeAzulBO.getCxpValeAzul();
                
                //cxpValeAzulDto.setIdEmpresa(idEmpresa);
                cxpValeAzulDto.setImporte(importe);
                cxpValeAzulDto.setConcepto(concepto);
                //cxpValeAzulDto.setFechaHoraCaptura(new Date());
                cxpValeAzulDto.setFechaHoraControl(fecha_control);
                cxpValeAzulDto.setFechaTentativaPago(fecha_pago);
                cxpValeAzulDto.setIdEstatus(estatus);
                
                try{
                    new CxpValeAzulDaoImpl(user.getConn()).update(cxpValeAzulDto.createPk(), cxpValeAzulDto);

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
                 * Creamos el registro de Cliente
                 */
                CxpValeAzul cxpValeAzulDto = new CxpValeAzul();
                CxpValeAzulDaoImpl valeAzulDao = new CxpValeAzulDaoImpl(user.getConn());
                
                //Series - Folios-------------------------
                //En caso de estar seleccionada una Serie, calcular nuevo folio--------
                int folioGenerado = -1;
                String folioGeneradoStr = null;
                String serie = null;
                try{
                    Folios foliosDto = new FoliosBO(idFolios,user.getConn()).getFolios();
                    if (foliosDto!=null){
                        folioGenerado = foliosDto.getUltimoFolio() + 1;
                        //Revisamos que el nuevo folio cumpla con el minimo y el maximo establecido en la serie
                        if (folioGenerado > foliosDto.getFolioHasta()){
                            out.print("La serie elegida a llegado a su folio máximo ("+foliosDto.getFolioHasta()+"). Elija otra o genera una nueva.");
                        }
                        if (folioGenerado < foliosDto.getFolioDesde()){
                            out.print("La serie elegida esta mal configurada, tiene establecido un valor minimo (desde) mayor al ultimo folio generado.");
                        }

                        if (msgError.trim().equals("")){
                            folioGeneradoStr = new DecimalFormat("#0000").format(folioGenerado);
                            serie = foliosDto.getSerie();

                            foliosDto.setUltimoFolio(folioGenerado);
                            try{
                                new FoliosDaoImpl(user.getConn()).update(foliosDto.createPk(), foliosDto);
                            }catch(Exception ex){

                            }
                        }
                    }else{
                        out.print("<ul> No se pudo recuperar la información de la Serie elegida, compruebe los datos e intente de nuevo. ");
                    }
                }catch(Exception ex){
                    out.print("<ul> Ocurrio un error al recuperar los datos de la Serie elegida y calculo del nuevo folio: " + ex.toString());
                }
                //Fin Series - Folios-------------------------
                
                if (msgError.equals("")){
                    cxpValeAzulDto.setIdEmpresa(idEmpresa);
                    cxpValeAzulDto.setIdFolio(idFolios);
                    cxpValeAzulDto.setFolioGenerado(folioGeneradoStr);
                    cxpValeAzulDto.setImporte(importe);
                    cxpValeAzulDto.setConcepto(concepto);
                    cxpValeAzulDto.setFechaHoraCaptura(new Date());
                    cxpValeAzulDto.setFechaHoraControl(fecha_control);
                    cxpValeAzulDto.setFechaTentativaPago(fecha_pago);
                    cxpValeAzulDto.setIdEstatus(estatus);

                    /**
                     * Realizamos el insert
                     */
                    valeAzulDao.insert(cxpValeAzulDto);

                    out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
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