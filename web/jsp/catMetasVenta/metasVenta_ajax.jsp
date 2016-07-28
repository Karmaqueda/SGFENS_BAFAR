<%-- 
    Document   : metasVenta_ajax
    Created on : 9/09/2015, 11:02:53 AM
    Author     : HpPyme
--%>

<%@page import="com.tsp.sct.bo.MetasEmpleadoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.MetasEmpleadoDaoImpl"%>
<%@page import="com.tsp.sct.bo.MetasVentaBO"%>
<%@page import="com.tsp.sct.dao.dto.MetasVentaPk"%>
<%@page import="com.tsp.sct.dao.jdbc.MetasVentaDaoImpl"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.tsp.sct.dao.dto.MetasVenta"%>
<%@page import="com.tsp.sct.dao.jdbc.UsuariosDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Usuarios"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoMetas"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sgfens.sesion.MetaEmpleadoSesion"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.tsp.sct.dao.dto.MetasEmpleado"%>
<%@page import="java.util.List"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="metasEmpleadosSesion" scope="session" class="com.tsp.sgfens.sesion.MetaEmpleadoSesion"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    
    String nombreMeta ="";
    int tipoMeta = -1;
    int periodo =  -1;
    Date fechaIni = null;
    Date fechaFin = null;
    int duracion = -1;
    int indexEdicion = -1;
    
    
    /* 
    * Recargar datos
    */
    
    boolean recargaMetas = false;
    boolean muestraTableAdd = false;
    
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    
    nombreMeta = request.getParameter("nombreMeta")!=null?new String(request.getParameter("nombreMeta").getBytes("ISO-8859-1"),"UTF-8"):"";
  
  
    
    try{
        tipoMeta = Integer.parseInt(request.getParameter("tipoMeta"));
    }catch(NumberFormatException ex){}    
    try{
        periodo = Integer.parseInt(request.getParameter("periodo"));
    }catch(NumberFormatException ex){}
    try{ fechaIni = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fechaIni"));}catch(Exception e){}
    try{ fechaFin = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fechaFin"));}catch(Exception e){}
    try{
        duracion = Integer.parseInt(request.getParameter("duracion"));
    }catch(NumberFormatException ex){} 
    
    
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
   
    System.out.println("new date--------" + format.format(new Date()));
    
    /*
    * Validaciones del servidor
    */
    String msgError = "";
    GenericValidator gc = new GenericValidator();      
    Date hoy = new Date();
    
    if(!gc.isValidString(nombreMeta, 1,100))
        msgError += "<ul>El dato 'Nombre Meta' es requerido.";    
    if(tipoMeta <= 0)
        msgError += "<ul>El dato 'Tipo Meta' es requerido";
    if(periodo<=0)
        msgError += "<ul>El dato 'Periodo' es requerido.";                   
    if (fechaIni == null){
        msgError+="<ul>El dato 'Fecha Inicio' es requerido";             
    }else{
        if(fechaIni.before(new Date())){
          //  msgError+="<ul>Debe seleccionar una Fecha válida. ";   
        }
    }    
    
    if (periodo == 1){
        if(fechaFin == null){
           msgError+="<ul>Debe seleccionar una Fecha de Fin válida. ";   
        }     
        
        duracion = 1;
    }
    
    if(duracion <= 0){
        msgError += "<ul>El dato 'Duración' es requerido.";
    }
         
    
    if(mode.equals("deleteMetaEmpleado")){
      msgError ="";//Para evitar validaciones principales  
    }
    
    if(msgError.equals("")){
        
        if (mode.equals("cargaPeriodos")){
            
            out.print("<!--EXITO-->");
            
            if (metasEmpleadosSesion==null){
                metasEmpleadosSesion = new MetaEmpleadoSesion();
            }
            
            
            metasEmpleadosSesion.setNombre(nombreMeta);
            metasEmpleadosSesion.setPeriodo(periodo);
            metasEmpleadosSesion.setTipo(tipoMeta);
            metasEmpleadosSesion.setDuracion(duracion);  
            metasEmpleadosSesion.setFechaIni(fechaIni);
            metasEmpleadosSesion.setFechaFin(fechaFin);

            
            muestraTableAdd  = true;

        }else if (mode.equals("addMetaEmpleado")){
            
            if (metasEmpleadosSesion==null){
                metasEmpleadosSesion = new MetaEmpleadoSesion();
            }
            
            EmpleadoMetas empMetas = new EmpleadoMetas();
            ArrayList<Double> metasEmpleadosValores = new ArrayList<Double>();// lista de valores metas 
            
            
            
            int idEmpleado = -1;
            double meta = 0;
           
            
            try{
                idEmpleado = Integer.parseInt(request.getParameter("idUserEmpleado"));
            }catch(NumberFormatException ex){} 
            
            if(idEmpleado <= 0){
                msgError += "<ul>Seleccione un Empleado.";
            }
          
           for(int i=1; i <= duracion;i++){                 
               
                try{
                    meta = Double.parseDouble(request.getParameter("meta_"+i));
                }catch(NumberFormatException ex){} 
                
                if(meta <= 0){
                    msgError += "<ul>Llene todos las metas del Empleado.";
                    metasEmpleadosValores.clear();
                    break;
                }else{
                    metasEmpleadosValores.add(meta);
                }
           }
              
           if(msgError.equals("")){
           
               empMetas.setIdempleado(idEmpleado);
               empMetas.setListaMetas(metasEmpleadosValores);


               boolean existe = false;
               for(int index = 0; index < metasEmpleadosSesion.getListaMetas().size();index++){

                     //si lo encuentra recuperamos ya no lo agregamos nuevamnete
                     if(metasEmpleadosSesion.getListaMetas().get(index).getIdempleado() == idEmpleado){                    
                         msgError += "<ul>El Empleado ya se encuentra en la lista";
                         existe = true;
                         break;
                     }                 

               }

               if(!existe){               

                      request.getSession().setAttribute("metasEmpleadosSesion", metasEmpleadosSesion);

                      muestraTableAdd = true;           
                      recargaMetas = true;
                      metasEmpleadosSesion.getListaMetas().add(empMetas);

                      out.print("<!--EXITO-->");


               }
          
           }
           
        }else if (mode.equals("activaEditarMeta")){
            
            //Solo habilitamos campos para editar ese registro en particular
            
            indexEdicion =  -1;
            
          
            try{ indexEdicion =  Integer.parseInt(request.getParameter("index_lista_metas")); }catch(Exception e){}
            
            
            if (indexEdicion<=0){
                msgError = "<ul>Registro inválido. Si persiste el problema intente recargar esta página.";
            }else{
                out.print("<!--EXITO-->");
                //refrescamos list
                muestraTableAdd = true;
                recargaMetas = true; 
            }
                
            
            
        
        }else if (mode.equals("editarMeta")){
            
            
            //Quitar empleado de lista
            if (metasEmpleadosSesion==null){
                metasEmpleadosSesion = new MetaEmpleadoSesion();
            }
                        
            //int indexEncontrado = -1;
            
            int index_lista_metas=-1;
            try{ index_lista_metas = Integer.parseInt(request.getParameter("index_lista_metas")); }catch(Exception e){}
            
            if (index_lista_metas<0)
                msgError = "<ul>Registro inválido. Si persiste el problema intente recargar esta página.";

            if (msgError.equals("")){
                try{                   
                    
                    
                    //recuperamos valores
                    int idEmpleado = -1;
                    double meta = 0;                    
                    ArrayList<Double> metasEmpleadosValores = new ArrayList<Double>();// lsita de valores metas 
                    
                    
                    try{
                        idEmpleado = Integer.parseInt(request.getParameter("idUserMeta"));
                    }catch(NumberFormatException ex){} 

                     //recuperamos fin

                   for(int i=1; i <= metasEmpleadosSesion.getDuracion();i++){                 

                        try{
                            meta = Double.parseDouble(request.getParameter("meta_"+index_lista_metas+"_"+i));
                        }catch(NumberFormatException ex){} 

                        metasEmpleadosValores.add(meta);
                   }
                    
                                           
                     EmpleadoMetas empMetas = null;
                    
                     for(int index = 0; index < metasEmpleadosSesion.getListaMetas().size();index++){
                         
                         //si lo encuentra recuperamos el index creamos obj y lo eliminamos de la lista
                         if(metasEmpleadosSesion.getListaMetas().get(index).getIdempleado() == idEmpleado){
                            
                             
                             empMetas = metasEmpleadosSesion.getListaMetas().get(index);
                             
                             empMetas.setIdempleado(idEmpleado);
                             empMetas.setListaMetas(metasEmpleadosValores);
                             
                             metasEmpleadosSesion.getListaMetas().set(index,empMetas);
                             
                             out.print("<!--EXITO-->");
                             
                             break;
                         }
                     }               
                  
                }catch(Exception ex){
                    msgError+="El registro especificado no se encontro en el listado. Intente de nuevo.";
                    ex.printStackTrace();
                }
                request.getSession().setAttribute("metasEmpleadosSesion", metasEmpleadosSesion);

                //refrescamos list
                muestraTableAdd = true;
                recargaMetas = true;               
                
            }
            
            
        }else if (mode.equals("borrarMeta")){
            
            //Quitar empleado de lista
            if (metasEmpleadosSesion==null){
                metasEmpleadosSesion = new MetaEmpleadoSesion();
            }
                        
            int indexEncontrado = -1;
            
            int idUserMeta=-1;
            try{ idUserMeta = Integer.parseInt(request.getParameter("idUserMeta")); }catch(Exception e){}
            
            if (idUserMeta<0)
                msgError = "<ul>Registro inválido. Si persiste el problema intente recargar esta página.";

            if (msgError.equals("")){
                try{                   
                             
                     EmpleadoMetas empMetas = null;
                    
                     for(int index = 0; index < metasEmpleadosSesion.getListaMetas().size();index++){
                         
                         //si lo encuentra recuperamos el index creamos obj y lo eliminamos de la lista
                         if(metasEmpleadosSesion.getListaMetas().get(index).getIdempleado() == idUserMeta){
                             empMetas = metasEmpleadosSesion.getListaMetas().get(index);
                             metasEmpleadosSesion.getListaMetas().remove(empMetas);
                             
                             //out.print("<!--EXITO-->Elemento eliminado de la lista.");
                             out.print("<!--EXITO-->");
                             request.getSession().setAttribute("metasEmpleadosSesion", metasEmpleadosSesion);

                            //refrescamos list
                            muestraTableAdd = true;
                            recargaMetas = true;  
                             
                             break;
                         }
                         
                     }               
                  
                }catch(Exception ex){
                    msgError+="El registro especificado no se encontro en el listado. Intente de nuevo.";
                    ex.printStackTrace();
                }
                             
                
            }
            
            
        }else if (mode.equals("guardarMeta")){
            
            msgError = "";          
                     
            if (metasEmpleadosSesion==null){
                metasEmpleadosSesion = new MetaEmpleadoSesion();
            }
            
            
            if(metasEmpleadosSesion.getListaMetas().size()>0){
                
                //creo meta table principal
                
                MetasVenta metaVenta = new MetasVenta();
                metaVenta.setIdEmpresa(idEmpresa);
                metaVenta.setFechaCreacion(hoy);
                metaVenta.setNombreMeta(nombreMeta);
                metaVenta.setIdComponenteMeta(tipoMeta);
                metaVenta.setPeriodo(periodo);
                metaVenta.setDuracion(duracion);               
                metaVenta.setIdEstatus(1);
                //Fin meta principal
                
                try{
                    
                    //inserta meta principal
                   MetasVentaPk metaInsertadaPk =  new MetasVentaDaoImpl(user.getConn()).insert(metaVenta);
                   MetasVenta metaInsertada = new MetasVentaBO(metaInsertadaPk.getIdMetaVenta(),user.getConn()).getMetaVenta();
                   
                   
                   for(EmpleadoMetas item:metasEmpleadosSesion.getListaMetas()){
                       
                       int i=0;
                       Calendar fechas = Calendar.getInstance();
                       fechas.setTime(metasEmpleadosSesion.getFechaIni());
                       
                       Calendar fechafin = null;
                       if(metasEmpleadosSesion.getFechaFin()!=null){
                            fechafin = Calendar.getInstance();
                            fechafin.setTime(metasEmpleadosSesion.getFechaFin());
                       }

                       for(Double objetivo: item.getListaMetas()){
                           
                           i++;
                           
                           int idEmpleado = 0;
                           try{
                              EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
                              idEmpleado = empleadoBO.findEmpleadoByUsuario(item.getIdempleado()).getIdEmpleado();
                           }catch(Exception e){e.printStackTrace();}
                           
                           
                           MetasEmpleado metaEmpleado =  new MetasEmpleado();
                           metaEmpleado.setIdMetaVenta(metaInsertada.getIdMetaVenta());
                           metaEmpleado.setIdEmpleado(idEmpleado);  
                           metaEmpleado.setObjetivoMeta(objetivo);                          
                           metaEmpleado.setIdEstatus(1);
                           metaEmpleado.setCicloMetaEmpleado(i);
                           
                           System.out.println("------> " + item.getIdempleado());
                           
                           switch(metaInsertada.getPeriodo()){
                               case 1: //Periodo
                                        metaEmpleado.setFechaInicio(fechas.getTime());
                                        metaEmpleado.setFechaFin(fechafin.getTime());                                   
                                   break;
                               case 2 :   //Diario ->  Dias + 1
                                       metaEmpleado.setFechaInicio(fechas.getTime());
                                       fechas.add(Calendar.DAY_OF_YEAR, 1);     
                                       metaEmpleado.setFechaFin(fechas.getTime());
                                   break;
                               case 3 :   //Semanas ->  Dias + 7
                                       metaEmpleado.setFechaInicio(fechas.getTime());
                                       fechas.add(Calendar.DAY_OF_YEAR, 7);     
                                       metaEmpleado.setFechaFin(fechas.getTime());
                                   break;
                               case 4 :   //Quincenal ->  Dias + 15
                                       metaEmpleado.setFechaInicio(fechas.getTime());
                                       fechas.add(Calendar.DAY_OF_YEAR, 15);     
                                       metaEmpleado.setFechaFin(fechas.getTime());
                                   break;
                               case 5 :   //Mensual ->  Mes + 1
                                       metaEmpleado.setFechaInicio(fechas.getTime());
                                       fechas.add(Calendar.MONTH, 1);     
                                       metaEmpleado.setFechaFin(fechas.getTime());
                                   break;
                               case 6 :   //Bimestral ->  Mes + 2
                                       metaEmpleado.setFechaInicio(fechas.getTime());
                                       fechas.add(Calendar.MONTH, 2);     
                                       metaEmpleado.setFechaFin(fechas.getTime());
                                   break;
                               case 7 :   //trimestral ->  Mes + 3
                                       metaEmpleado.setFechaInicio(fechas.getTime());
                                       fechas.add(Calendar.MONTH, 3);     
                                       metaEmpleado.setFechaFin(fechas.getTime());
                                   break;
                               case 8 :   //Anual ->  Año + 1
                                       metaEmpleado.setFechaInicio(fechas.getTime());
                                       fechas.add(Calendar.YEAR, 1);     
                                       metaEmpleado.setFechaFin(fechas.getTime());
                                   break;
                           }
                           
                           
                           try{
                               //insertamos metas                               
                               new MetasEmpleadoDaoImpl(user.getConn()).insert(metaEmpleado);
                                
                               
                               
                           }catch(Exception e){
                               e.printStackTrace();
                                
                           }
                           
                           
                       } 

                   }
                    out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
                    
                }catch(Exception e){
                    e.printStackTrace();
                    msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
                }
                
                
                
                
            }else{
                msgError +="Agregue mínimo un empleado.";
            }
               
            
                
             if (msgError.equals("")){
                
                //Borramos sesión
                request.getSession().setAttribute("metasEmpleadosSesion", null);
                
            }
             
             
        }else if (mode.equals("recargaLista")){
            
             out.print("<!--EXITO-->");
            //refrescamos list
            muestraTableAdd = true;
            recargaMetas = true;              
           
            
        }else if (mode.equals("deleteMetaEmpleado")){
            //borra meta de lista activas
            
            int idMetaEmpleado = -1;
            
            try{
                idMetaEmpleado = Integer.parseInt(request.getParameter("idMetaEmpleado"));
            }catch(NumberFormatException ex){}        
                    
            
            if (idMetaEmpleado>0){
                 System.out.println("1");
               MetasEmpleado metasEmpleadoDto =  new MetasEmpleadoBO(idMetaEmpleado,user.getConn()).getMetaEmpleado();
                
               try{
                   System.out.println("2");
                   metasEmpleadoDto.setIdEstatus(2);
                    System.out.println("3");
                   new MetasEmpleadoDaoImpl(user.getConn()).update(metasEmpleadoDto.createPk(), metasEmpleadoDto);
                    System.out.println("4");
                   out.print("<!--EXITO-->Meta eliminada satisfactoriamente.");
               }catch(Exception e){
                   e.printStackTrace();
                   msgError += "Ocurrio un error al eliminar el registro: " + e.toString() ;
               }
            }
            
            
        }else{
            out.print("<!--ERROR-->Acción no válida.");
        }
        
    }
    
    
    
    
    if (msgError.equals("") && (mode.equals("addMetaEmpleado"))){//Mensaje exito
        out.print("<!--EXITO-->");
    }else if(!msgError.equals("")){//Mensaje error
        out.print("<!--ERROR-->"+msgError);
    }
    
   
    if(muestraTableAdd){
        
        
        %>
            <!-- original  -->
        <table class="data" width="100%" cellpadding="0" cellspacing="0">
            <thead>
                <tr>
                    <th>Empleado</th>
                    <%for(int i=1; i <= metasEmpleadosSesion.getDuracion();i++){%>
                    <th>Periodo <%=i%></th>
                    <%
                    }%>
                    <th>Total</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>
                        <select id="idUserEmpleado" name="idUserEmpleado" class="flexselect">
                            <option></option>
                            <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL, 0) %> 
                            <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR_VENDEDOR, 0) %>
                            <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_COBRADOR, 0) %>
                        </select>
                    </td>
                    <%for(int i=1; i <= metasEmpleadosSesion.getDuracion();i++){%>
                        <td>
                            <input type="text" maxlength="16" name="meta_<%=i%>" id="meta_<%=i%>"
                            style="text-align: right;" size="12" value="0" 
                            onkeypress="return validateNumber(event);"/>
                        </td>
                    <%
                    }%>
                    <td><input type="text" maxlength="16" name="metaTotal" id="metaTotal"
                            style="text-align: right;" size="12" value="0" readonly
                            onkeypress="return validateNumber(event);"/>
                    </td>
                    <td><a onclick="addMetaEmpleadoSesion();"><img src="../../images/icon_agregar.png" alt="icon" alt="Agregar" class="help" title="Agregar"/></a></td>
                </tr>                                                
            </tbody>
        </table>
        
        <%
        
    }
    
    
    if(recargaMetas){
        
        
        %>
        <table class="data" width="100%" cellpadding="0" cellspacing="0"> 
            <tbody>
                <tr>
                    <td colspan="<%=(metasEmpleadosSesion.getListaMetas().size()+3)%>"><center><h4>Seleccionados</h4></center></td>
                </tr>
        
        
        <%
        int i = 0;
        
        //Listamos metas de lista en sesion
        for(EmpleadoMetas item:metasEmpleadosSesion.getListaMetas()){
            i++;
            
            
            %>
                       
             <!-- sesion  -->
            
             <tr <%=indexEdicion==i?"style='background: #F3E2A9'":""%>  >
                            <td>
                                <select id="idUserEmpleado_<%=i%>" name="idUserEmpleado_<%=i%>" disabled>
                                    <option></option>
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL, item.getIdempleado()) %> 
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR_VENDEDOR, item.getIdempleado()) %>
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_COBRADOR, item.getIdempleado()) %>
                                </select>
                            </td>
                            <%
                            int j = 0;
                            double totalMetas = 0;
                            for(Double meta:item.getListaMetas()){
                                j++;
                            %>
                                <td>
                                    <input type="text" maxlength="16" name="meta_<%=i%>_<%=j%>" id="meta_<%=i%>_<%=j%>"
                                           style="text-align: right;" size="12" value="<%=meta%>" <%=indexEdicion==i?"":"readonly"%>
                                    onkeypress="return validateNumber(event);"/>
                                </td>
                            <%
                            
                                totalMetas += meta;
                            }
                            
                            %>
                            <td><input type="text" maxlength="16" name="metaTotal_<%=i%>" id="metaTotal_<%=i%>"
                                    style="text-align: right;" size="12" value="<%=totalMetas%>"  readonly 
                                    onkeypress="return validateNumber(event);"/>
                            </td>
                            &nbsp;&nbsp;&nbsp;
                            <td>
                            <%if(indexEdicion==i){%>
                                <a onclick="editMeta(<%=i%>,<%=item.getIdempleado()%>);"><img src="../../images/disk.png" alt="icon" alt="Guardar" class="help" title="Guardar"/></a>                                
                            <%}else{
                              %>    
                                <a onclick="activaEditMeta(<%=i%>,<%=item.getIdempleado()%>);"><img src="../../images/icon_edit.png" alt="icon" alt="Editar" class="help" title="Editar"/></a>
                                <a onclick="deleteMeta(<%=item.getIdempleado()%>);"><img src="../../images/icon_delete.png" alt="icon" alt="Eliminar" class="help" title="Eliminar"/></a></td>
                            <%
                             }%>
                            </td>
                        </tr>                      
                   
            
            
            <%
            
            
        }
        %>
            </tbody>
        </table>
        <%
        
    }
    

%>