<%-- 
    Document   : mapa_visor_ajax
    Created on : 2/05/2016, 10:45:54 AM
    Author     : ISCesar
--%>
<%@page import="com.tsp.sct.util.Converter"%>
<%@page import="com.tsp.sct.bo.EmpleadoBitacoraPosicionBO"%>
<%@page import="com.tsp.sct.bo.SGPedidoEntregaBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedidoEntrega"%>
<%@page import="com.tsp.sct.bo.SGPedidoDevolucionesCambioBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedidoDevolucionCambio"%>
<%@page import="com.tsp.sct.bo.SGVisitaClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensVisitaCliente"%>
<%@page import="com.tsp.sct.bo.SGCobranzaAbonoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensCobranzaAbono"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.bo.SGPedidoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedido"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProspecto"%>
<%@page import="com.tsp.sct.bo.SGProspectoBO"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.util.GenericMethods"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoBitacoraPosicion"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoBitacoraPosicionDaoImpl"%>
<%@page import="com.tsp.sct.dao.jdbc.EstadoEmpleadoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.EstadoEmpleado"%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.tsp.sct.util.MapUtil"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String msgError="";
    String msgExitoExtra="";
    
    /*
    *   Modos:
    *       -1 = No se eligio (ERROR)
    * 
    *        1 = Cargar ubicacion clientes
    *        2 = Cargar ubicacion prospectos
    *        3 = Cargar ubicacion empleados/promotores
    *        4 = Cargar ubicacion vehiculos
    *        5 = Cargar ubicacion puntos de interes
    *        6 = Cargar ubicacion pedidos
    *        7 = Cargar ubicacion cobranzas
    *        8 = Cargar ubicacion visitas
    *        9 = Cargar ubicacion devoluciones
    *        10 = Cargar ubicacion entregas
    */
    
    String mode = request.getParameter("mode")!=null?new String(request.getParameter("mode").getBytes("ISO-8859-1"),"UTF-8"):"-1";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    EmpresaBO empresaBO = new EmpresaBO(user.getConn());    
    Empresa empresa = empresaBO.findEmpresabyId(idEmpresa); 
     
    GenericValidator gc = new GenericValidator();
    Gson gson = new Gson();    
    
    //String contenido = request.getParameter("contenido")!=null?new String(request.getParameter("contenido").getBytes("ISO-8859-1"),"UTF-8"):"";
    //try{ clientes = Integer.parseInt(request.getParameter("clientes")); }catch(NumberFormatException ex){}
    
    /*
     * Procesamiento
     */
    if (mode.equals("-1")){
        msgError = "No se eligio una acción correcta. Reintente el llenado de los datos.";
    }else if (mode.equals("clientes")){
    // 1 = Cargar ubicacion clientes
        try{
            int idClienteEspecifico = -1;
            try{
                idClienteEspecifico = Integer.parseInt(request.getParameter("extra1"));
            }catch(Exception e){}
            
            ClienteBO clienteBO = new ClienteBO(user.getConn());
            Cliente[] clientesDto = clienteBO.findClientes(idClienteEspecifico, idEmpresa , 0, 0, " AND (LATITUD <> 0 AND LONGITUD <> 0 AND LATITUD <> 'null' AND LONGITUD <> 'null' AND LATITUD IS NOT NULL AND LONGITUD IS NOT NULL ) AND ID_ESTATUS <> 2 ");            
        
            MapUtil.MarcadorMapa[] data = new MapUtil.MarcadorMapa[0];
            List<MapUtil.MarcadorMapa> marcadores = new ArrayList<MapUtil.MarcadorMapa>();
            for(Cliente clienteDto:clientesDto){
                String nombreCliente = clienteDto.getRazonSocial();
                String dialogoMarcador = ""
                     + "<div class='map_dialog'>"
                     + "   <b>Cliente:</b><br/>" + nombreCliente.replaceAll("\\\"", "&quot;") + "<br/><br/>"
                     + "   <li> <a title='Detalles' onclick='muestraDetallesCliente(" + clienteDto.getIdCliente() + ",1)'>Detalles </a> </li> <br/>"
                     + "   <li> <a title='Pagos' onclick='muestraDetallesCliente(" + clienteDto.getIdCliente() + ",2)'>Pagos </a> </li> <br/>"
                     + " </div>";
                    
                MapUtil.MarcadorMapa marcadorMapa = new MapUtil.MarcadorMapa();
                marcadorMapa.setTipo("clientes");
                marcadorMapa.setLatitud( clienteDto.getLatitud()!=null?(!clienteDto.getLatitud().equals("")?clienteDto.getLatitud():""+empresa.getLatitud()):"0" );
                marcadorMapa.setLongitud(clienteDto.getLongitud()!=null?(!clienteDto.getLongitud().equals("")?clienteDto.getLongitud():""+empresa.getLongitud()):"0");
                marcadorMapa.setTitulo(nombreCliente.replaceAll("\\\"", "&quot;"));
                marcadorMapa.setContenidoDialogo(dialogoMarcador);
                marcadorMapa.setRutaRelativaIcono("../../images/maps/map_marker_cte.png");

                marcadores.add(marcadorMapa);
            }
            if (marcadores.size()>0){
                data = marcadores.toArray(data); //convertimos a Array
                marcadores = null; //vaciamos list
            }

            //Usamos JSON para retornar el objeto respuesta
            String jsonOutput = gson.toJson(data);
            msgExitoExtra = jsonOutput;
        }catch(Exception ex){
            msgError+="<ul>Ha ocurrido un error inesperado mientras se consultaban registros de Clientes: <br/>" + GenericMethods.exceptionStackTraceToString(ex);
        }
    }else if (mode.equals("prospectos")){
    // 2 = Cargar ubicacion prospectos
        try{
            int idProspectoEspecifico = -1;
            try{
                idProspectoEspecifico = Integer.parseInt(request.getParameter("extra1"));
            }catch(Exception e){}
            
            SGProspectoBO prospectoBO = new SGProspectoBO(user.getConn());
            SgfensProspecto[] prospectosDto = prospectoBO.findProspecto(idProspectoEspecifico, idEmpresa, 0, 0, " AND (LATITUD <> 0 AND LONGITUD <> 0 AND LATITUD IS NOT NULL AND LONGITUD IS NOT NULL ) ");           
            
            MapUtil.MarcadorMapa[] data = new MapUtil.MarcadorMapa[0];
            List<MapUtil.MarcadorMapa> marcadores = new ArrayList<MapUtil.MarcadorMapa>();
            for(SgfensProspecto prospectoDto:prospectosDto){
                String nombreMarcador = prospectoDto.getContacto();
                String dialogoMarcador = ""
                    + "<div class='map_dialog'>"
                    + "    <b>Prospecto:</b><br/>" + nombreMarcador.replaceAll("\\\"", "&quot;") + "<br/><br/>"
                    + "    <li> <a title='Detalles' onclick='muestraDetallesProspecto(" + prospectoDto.getIdProspecto() + ",1)'>Detalles </a> </li><br/>"
                    + "</div>";
                
                MapUtil.MarcadorMapa marcadorMapa = new MapUtil.MarcadorMapa();
                marcadorMapa.setTipo("prospectos");
                marcadorMapa.setLatitud( "" + (prospectoDto.getLatitud()!=0?prospectoDto.getLatitud():empresa.getLatitud())  );
                marcadorMapa.setLongitud( "" + (prospectoDto.getLongitud()!=0?prospectoDto.getLongitud():empresa.getLongitud()) );
                marcadorMapa.setTitulo(nombreMarcador.replaceAll("\\\"", "&quot;"));
                marcadorMapa.setContenidoDialogo(dialogoMarcador);
                marcadorMapa.setRutaRelativaIcono("../../images/maps/map_marker_pros.png");

                marcadores.add(marcadorMapa);
            }
            if (marcadores.size()>0){
                data = marcadores.toArray(data); //convertimos a Array
                marcadores = null; //vaciamos list
            }

            //Usamos JSON para retornar el objeto respuesta
            String jsonOutput = gson.toJson(data);
            msgExitoExtra = jsonOutput;    
        }catch(Exception ex){
            msgError+="<ul>Ha ocurrido un error inesperado mientras se consultaban registros de Prospectos : <br/>" + GenericMethods.exceptionStackTraceToString(ex);
        }  
    }else if (mode.equals("promotores")){
    //3 = Cargar ubicacion promotores / empleados
        try{
            int idPromotorEspecifico = -1;
            try{
                idPromotorEspecifico = Integer.parseInt(request.getParameter("extra1"));
            }catch(Exception e){}
            
            EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
            Empleado[] empleadosDto = empleadoBO.findEmpleados(idPromotorEspecifico, idEmpresa, 0, 0, " AND ID_DISPOSITIVO >0  AND (LATITUD != 0 AND LONGITUD != 0 AND LATITUD IS NOT NULL AND LONGITUD IS NOT NULL ) ");

            MapUtil.MarcadorMapa[] data = new MapUtil.MarcadorMapa[0];
            List<MapUtil.MarcadorMapa> marcadores = new ArrayList<MapUtil.MarcadorMapa>();
            for (Empleado empleadoDto : empleadosDto){
                MapUtil.MarcadorMapa marcadorMapa = new MapUtil.MarcadorMapa();

                EstadoEmpleado estadoDto = new EstadoEmpleadoDaoImpl(user.getConn()).findByPrimaryKey(empleadoDto.getIdEstado());
                String nombreMarcador = StringManage.getValidString(empleadoDto.getApellidoPaterno()) + " " + StringManage.getValidString(empleadoDto.getApellidoMaterno()) +  " " + StringManage.getValidString(empleadoDto.getNombre());
                String dialogoMarcador = ""
                            + "<div class='map_dialog'><b>";
                            if(empleadoDto.getIdMovilEmpleadoRol() == 2 || empleadoDto.getIdMovilEmpleadoRol() == 25)
                                dialogoMarcador += "Vendedor";
                            else if(empleadoDto.getIdMovilEmpleadoRol() == 31)
                                dialogoMarcador += "Conductor Vendedor";
                            else if(empleadoDto.getIdMovilEmpleadoRol() == 32)
                                dialogoMarcador += "Cobrador";
                            else if(empleadoDto.getIdMovilEmpleadoRol() == 26)
                                dialogoMarcador += "Conductor";
                            dialogoMarcador += ":</b></br>" + nombreMarcador.replaceAll("\\\"", "&quot;") + "<br/>"
                            + "    <b>Estado:</b> " + (estadoDto!=null?estadoDto.getNombre():"") + "<br/>"
                            + "    <li> <a title='Detalles' onclick='muestraDetallesPromotor("+empleadoDto.getIdEmpleado()+",1);'>Detalles </a> </li> <br/>"
                            + "    <li> <a title='Hist&oacute;rico' onclick='muestraDetallesPromotor("+empleadoDto.getIdEmpleado()+",2);'>Hist&oacute;rico </a> </li><br/>"
                            + "    <li> <a title='Cobros' onclick='muestraDetallesPromotor("+empleadoDto.getIdEmpleado()+",3);'>Cobros </a> </li> <br/>"
                            + "    <li> <a title='Mensaje' onclick='muestraDetallesPromotor("+empleadoDto.getIdEmpleado()+",4);'>Mensaje </a> </li> <br/>"
                            + "    <li> <a title='Rutas' onclick='muestraDetallesPromotor("+empleadoDto.getIdEmpleado()+",5);'>Rutas </a> </li><br/>"
                            + "    <li> <a title='Pedidos' onclick='muestraDetallesPromotor("+empleadoDto.getIdEmpleado()+",6);'>Pedidos </a> </li><br/>"
                            + "</div>";
                String rutaRelativaIcono;

                EmpleadoBitacoraPosicionDaoImpl bitacoraDao = new EmpleadoBitacoraPosicionDaoImpl(user.getConn());
                long s = (new Date()).getTime();
                long d = 0; 
                try{
                    String filtro = " ID_EMPLEADO = "+ empleadoDto.getIdEmpleado() + " ORDER BY ID_BITACORA_POSICION DESC LIMIT 0,1";
                    EmpleadoBitacoraPosicion bitEmp = bitacoraDao.findByDynamicWhere(filtro,null)[0];

                    d = bitEmp.getFecha().getTime();
                }catch(Exception e){}
                long diferencia = s - d;
                //System.out.println("-------DIFERENCIA: "+diferencia);
                if(diferencia < 300000){
                    rutaRelativaIcono = empleadoDto.getRepartidor() == 1? "../../images/estatusEmpleado/icon_activoTrabajando.png" : "../../images/maps/40px-verde-camion.png";
                }else{
                    rutaRelativaIcono = empleadoDto.getRepartidor() == 1? "../../images/estatusEmpleado/icon_desactivado.png" :  "../../images/maps/40px-gris-camion.png";
                }

                marcadorMapa.setTipo("promotores");
                marcadorMapa.setLatitud("" + (empleadoDto.getLatitud()!=0?empleadoDto.getLatitud():empresa.getLatitud()) );
                marcadorMapa.setLongitud("" + (empleadoDto.getLongitud()!=0?empleadoDto.getLongitud():empresa.getLongitud()) );
                marcadorMapa.setTitulo(nombreMarcador);
                marcadorMapa.setContenidoDialogo(dialogoMarcador);
                marcadorMapa.setRutaRelativaIcono(rutaRelativaIcono);

                marcadores.add(marcadorMapa);
            }
            if (marcadores.size()>0){
                data = marcadores.toArray(data); //convertimos a Array
                marcadores = null; //vaciamos list
            }

            //Usamos JSON para retornar el objeto respuesta
            String jsonOutput = gson.toJson(data);
            msgExitoExtra = jsonOutput;
        }catch(Exception ex){
            msgError+="<ul>Ha ocurrido un error inesperado mientras se consultaban registros de Empleados: <br/>" + GenericMethods.exceptionStackTraceToString(ex);
        }
    }else if (mode.equals("vehiculos")){
    // 4 = Cargar ubicacion vehiculos
        msgError+="<ul>Vehiculos - Consulta de mapa No implementada";
    }else if (mode.equals("puntos")){
    // 5 = Cargar ubicacion puntos de interes
        msgError+="<ul>Puntos de interes - Consulta de mapa No implementada";
    }else if (mode.equals("pedidos")){
    // 6 = Cargar ubicacion pedidos
        try{
            String filtroBusquedaPedido = request.getParameter("extra1")!=null?new String(request.getParameter("extra1").getBytes("ISO-8859-1"),"UTF-8"):"";
            
            SGPedidoBO pedidoBO = new SGPedidoBO(user.getConn());
            SgfensPedido[] pedidosDto = pedidoBO.findPedido(0, idEmpresa , 0, 0, " AND (LATITUD <> 0 AND LONGITUD <> 0 AND LATITUD IS NOT NULL AND LONGITUD IS NOT NULL ) " + filtroBusquedaPedido);
            
            MapUtil.MarcadorMapa[] data = new MapUtil.MarcadorMapa[0];
            List<MapUtil.MarcadorMapa> marcadores = new ArrayList<MapUtil.MarcadorMapa>();
            for(SgfensPedido pedidoDto : pedidosDto){
                String nombreMarcador = "" + pedidoDto.getIdPedido();
                String dialogoMarcador = ""
                     + "<div class='map_dialog'>"
                     + "   <b>Pedido ID:</b>" + pedidoDto.getIdPedido() + "<br/>"
                     + "   <b>Fecha/Hr:</b><br/>" + DateManage.dateTimeToStringEspanol(pedidoDto.getFechaPedido()) + "<br/><br/>"
                     + "   <li> <a title='Detalles' onclick='muestraDetallesPedido(" + pedidoDto.getIdPedido() + ",1)'>Ver Detalles </a> </li> <br/>"
                     + " </div>";
                
                MapUtil.MarcadorMapa marcadorMapa = new MapUtil.MarcadorMapa();
                marcadorMapa.setTipo("pedidos");
                marcadorMapa.setLatitud( "" + (pedidoDto.getLatitud()!=0?pedidoDto.getLatitud() : empresa.getLatitud())  );
                marcadorMapa.setLongitud( "" + (pedidoDto.getLongitud()!=0?pedidoDto.getLongitud(): empresa.getLongitud()) );
                marcadorMapa.setTitulo(nombreMarcador.replaceAll("\\\"", "&quot;"));
                marcadorMapa.setContenidoDialogo(dialogoMarcador);
                marcadorMapa.setRutaRelativaIcono("../../images/maps/map_marker_cart.png");

                marcadores.add(marcadorMapa);
            }
            if (marcadores.size()>0){
                data = marcadores.toArray(data); //convertimos a Array
                marcadores = null; //vaciamos list
            }

            //Usamos JSON para retornar el objeto respuesta
            String jsonOutput = gson.toJson(data);
            msgExitoExtra = jsonOutput;    
        }catch(Exception ex){
            msgError+="<ul>Ha ocurrido un error inesperado mientras se consultaban registros de Prospectos : <br/>" + GenericMethods.exceptionStackTraceToString(ex);
        }      
    }else if (mode.equals("cobranzas")){
    // 7 = Cargar ubicacion cobranzas
        try{
            String strWhereRangoFechasCobranza = request.getParameter("extra1")!=null?new String(request.getParameter("extra1").getBytes("ISO-8859-1"),"UTF-8"):"";
            
            SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(user.getConn());
            SgfensCobranzaAbono[] cobranzasDto = cobranzaAbonoBO.findCobranzaAbono(0, idEmpresa , 0, 0, " AND (LATITUD <> 0 AND LONGITUD <> 0 AND LATITUD IS NOT NULL AND LONGITUD IS NOT NULL ) AND " + strWhereRangoFechasCobranza);
            
            MapUtil.MarcadorMapa[] data = new MapUtil.MarcadorMapa[0];
            List<MapUtil.MarcadorMapa> marcadores = new ArrayList<MapUtil.MarcadorMapa>();
            for(SgfensCobranzaAbono cobranzaDto : cobranzasDto){
                String nombreMarcador = "" +cobranzaDto.getIdCobranzaAbono();
                String dialogoMarcador = ""
                    + "<div class='map_dialog'>"
                    + "   <b>Cobranza ID:</b>" + cobranzaDto.getIdCobranzaAbono()+ "<br/>"
                    + "   <b>Fecha/Hr:</b><br/>" + DateManage.dateTimeToStringEspanol(cobranzaDto.getFechaAbono()) + "<br/><br/>"
                    + "   <li> <a title='Detalles' onclick='muestraDetallesCobranza(" + cobranzaDto.getIdCobranzaAbono()+ ",1)'>Ver Detalles </a> </li> <br/>"
                    + " </div>";
                
                MapUtil.MarcadorMapa marcadorMapa = new MapUtil.MarcadorMapa();
                marcadorMapa.setTipo("cobranzas");
                marcadorMapa.setLatitud( "" + (cobranzaDto.getLatitud()!=0?cobranzaDto.getLatitud() : empresa.getLatitud())  );
                marcadorMapa.setLongitud( "" + (cobranzaDto.getLongitud()!=0?cobranzaDto.getLongitud(): empresa.getLongitud()) );
                marcadorMapa.setTitulo(nombreMarcador.replaceAll("\\\"", "&quot;"));
                marcadorMapa.setContenidoDialogo(dialogoMarcador);
                marcadorMapa.setRutaRelativaIcono("../../images/maps/map_marker_cobra.png");

                marcadores.add(marcadorMapa);
            }
            if (marcadores.size()>0){
                data = marcadores.toArray(data); //convertimos a Array
                marcadores = null; //vaciamos list
            }

            //Usamos JSON para retornar el objeto respuesta
            String jsonOutput = gson.toJson(data);
            msgExitoExtra = jsonOutput;    
        }catch(Exception ex){
            msgError+="<ul>Ha ocurrido un error inesperado mientras se consultaban registros de Cobranzas : <br/>" + GenericMethods.exceptionStackTraceToString(ex);
        }      
    }else if (mode.equals("visitas")){
    // 8 = Cargar ubicacion visitas
        try{
            String strWhereRangoFechasVisita = request.getParameter("extra1")!=null?new String(request.getParameter("extra1").getBytes("ISO-8859-1"),"UTF-8"):"";
            
            SGVisitaClienteBO visitaClienteBO = new SGVisitaClienteBO(user.getConn());
            SgfensVisitaCliente[] visitasDto = visitaClienteBO.findSgfensVisitaClientes(0, idEmpresa , 0, 0, " AND (LATITUD <> 0 AND LONGITUD <> 0 AND LATITUD IS NOT NULL AND LONGITUD IS NOT NULL ) AND " + strWhereRangoFechasVisita);
            
            MapUtil.MarcadorMapa[] data = new MapUtil.MarcadorMapa[0];
            List<MapUtil.MarcadorMapa> marcadores = new ArrayList<MapUtil.MarcadorMapa>();
            for(SgfensVisitaCliente visitaDto : visitasDto){
                String nombreMarcador = "" + visitaDto.getIdVisita();
                String dialogoMarcador = ""
                    + "<div class='map_dialog'>"
                    + "   <b>Visita ID:</b>" + visitaDto.getIdVisita() + "<br/>"
                    + "   <b>Fecha/Hr:</b><br/>" + DateManage.dateTimeToStringEspanol(visitaDto.getFechaHora()) + "<br/><br/>"
                    + "   <li> <a title='Detalles' onclick='muestraDetallesVisita(" + visitaDto.getIdVisita() + ",1)'>Ver Detalles </a> </li> <br/>"
                    + " </div>";
                
                MapUtil.MarcadorMapa marcadorMapa = new MapUtil.MarcadorMapa();
                marcadorMapa.setTipo("visitas");
                marcadorMapa.setLatitud( "" + (visitaDto.getLatitud()!=0?visitaDto.getLatitud() : empresa.getLatitud())  );
                marcadorMapa.setLongitud( "" + (visitaDto.getLongitud()!=0?visitaDto.getLongitud(): empresa.getLongitud()) );
                marcadorMapa.setTitulo(nombreMarcador.replaceAll("\\\"", "&quot;"));
                marcadorMapa.setContenidoDialogo(dialogoMarcador);
                marcadorMapa.setRutaRelativaIcono("../../images/maps/map_marker_seg.png");

                marcadores.add(marcadorMapa);
            }
            if (marcadores.size()>0){
                data = marcadores.toArray(data); //convertimos a Array
                marcadores = null; //vaciamos list
            }

            //Usamos JSON para retornar el objeto respuesta
            String jsonOutput = gson.toJson(data);
            msgExitoExtra = jsonOutput;    
        }catch(Exception ex){
            msgError+="<ul>Ha ocurrido un error inesperado mientras se consultaban registros de Visitas : <br/>" + GenericMethods.exceptionStackTraceToString(ex);
        } 
    }else if (mode.equals("devoluciones")){
    // 9 = Cargar ubicacion devoluciones
        try{
            String strWhereRangoFechasDevolucion = request.getParameter("extra1")!=null?new String(request.getParameter("extra1").getBytes("ISO-8859-1"),"UTF-8"):"";
            
            SGPedidoBO pedidoBO = new SGPedidoBO(user.getConn());
            SGPedidoDevolucionesCambioBO devolucionesCambioBO = new SGPedidoDevolucionesCambioBO(user.getConn());
            SgfensPedidoDevolucionCambio[] devolucionesDto = devolucionesCambioBO.findSgfensPedidoDevolucionCambios(0, idEmpresa , 0, 0, (StringManage.getValidString(strWhereRangoFechasDevolucion).length()>0? " AND " + strWhereRangoFechasDevolucion : "") );
            
            MapUtil.MarcadorMapa[] data = new MapUtil.MarcadorMapa[0];
            List<MapUtil.MarcadorMapa> marcadores = new ArrayList<MapUtil.MarcadorMapa>();
            for(SgfensPedidoDevolucionCambio devolucionDto : devolucionesDto){
                    double latitud = 0;
                    double longitud = 0;
                    try {
                        SgfensPedido pedidoDto = pedidoBO.findPedidobyId(devolucionDto.getIdPedido());
                        if (pedidoDto!=null){
                            latitud = pedidoDto.getLatitud();
                            longitud = pedidoDto.getLongitud();
                        }
                    }catch(Exception ex){}
                    String nombreMarcador = "" + devolucionDto.getIdPedidoDevolCambio();
                    String dialogoMarcador = ""
                         + "<div class='map_dialog'>"
                         + "   <b>Devolucion ID:</b>" + devolucionDto.getIdPedidoDevolCambio()+ "<br/>"
                         + "   <b>Fecha/Hr:</b><br/>" + DateManage.dateTimeToStringEspanol(devolucionDto.getFecha()) + "<br/><br/>"
                         + "   <li> <a title='Detalles' onclick='muestraDetallesDevolucion(" + devolucionDto.getIdPedidoDevolCambio() + ",1)'>Ver Detalles </a> </li> <br/>"
                         + " </div>";
                
                MapUtil.MarcadorMapa marcadorMapa = new MapUtil.MarcadorMapa();
                marcadorMapa.setTipo("devoluciones");
                marcadorMapa.setLatitud( "" + (latitud!=0?latitud : empresa.getLatitud())  );
                marcadorMapa.setLongitud( "" + (longitud!=0? longitud : empresa.getLongitud()) );
                marcadorMapa.setTitulo(nombreMarcador.replaceAll("\\\"", "&quot;"));
                marcadorMapa.setContenidoDialogo(dialogoMarcador);
                marcadorMapa.setRutaRelativaIcono("../../images/maps/map_marker_devs.png");

                marcadores.add(marcadorMapa);
            }
            if (marcadores.size()>0){
                data = marcadores.toArray(data); //convertimos a Array
                marcadores = null; //vaciamos list
            }

            //Usamos JSON para retornar el objeto respuesta
            String jsonOutput = gson.toJson(data);
            msgExitoExtra = jsonOutput;    
        }catch(Exception ex){
            msgError+="<ul>Ha ocurrido un error inesperado mientras se consultaban registros de Visitas : <br/>" + GenericMethods.exceptionStackTraceToString(ex);
        }
    }else if (mode.equals("entregas")){
    // 10 = Cargar ubicacion entregas
        try{
            String strWhereRangoFechasEntrega = request.getParameter("extra1")!=null?new String(request.getParameter("extra1").getBytes("ISO-8859-1"),"UTF-8"):"";
            
            SGPedidoBO pedidoBO = new SGPedidoBO(user.getConn());
            SGPedidoEntregaBO pedidoEntregaBO = new SGPedidoEntregaBO(user.getConn());
            SgfensPedidoEntrega[] entregasDto = pedidoEntregaBO.findSgfensPedidoEntregas(0, idEmpresa , 0, 0, (StringManage.getValidString(strWhereRangoFechasEntrega).length()>0? " AND " + strWhereRangoFechasEntrega : "") );
            
            MapUtil.MarcadorMapa[] data = new MapUtil.MarcadorMapa[0];
            List<MapUtil.MarcadorMapa> marcadores = new ArrayList<MapUtil.MarcadorMapa>();
            for(SgfensPedidoEntrega entregaDto : entregasDto){
                    double latitud = 0;
                    double longitud = 0;
                    try {
                        SgfensPedido pedidoDto = pedidoBO.findPedidobyId(entregaDto.getIdPedido());
                        if (pedidoDto!=null){
                            latitud = pedidoDto.getLatitud();
                            longitud = pedidoDto.getLongitud();
                        }
                    }catch(Exception ex){}
                    String nombreMarcador = "" + entregaDto.getIdPedidoEntrega();
                    String dialogoMarcador = ""
                         + "<div class='map_dialog'>"
                         + "   <b>Entrega ID:</b>" + entregaDto.getIdPedidoEntrega()+ "<br/>"
                         + "   <b>Fecha/Hr:</b><br/>" + DateManage.dateTimeToStringEspanol(entregaDto.getFechaHora()) + "<br/><br/>"
                         + "   <li> <a title='Detalles' onclick='muestraDetallesEntrega(" + entregaDto.getIdPedidoEntrega() + ",1)'>Ver Detalles </a> </li> <br/>"
                         + " </div>";
                
                MapUtil.MarcadorMapa marcadorMapa = new MapUtil.MarcadorMapa();
                marcadorMapa.setTipo("entregas");
                marcadorMapa.setLatitud( "" + (latitud!=0?latitud : empresa.getLatitud())  );
                marcadorMapa.setLongitud( "" + (longitud!=0? longitud : empresa.getLongitud()) );
                marcadorMapa.setTitulo(nombreMarcador.replaceAll("\\\"", "&quot;"));
                marcadorMapa.setContenidoDialogo(dialogoMarcador);
                marcadorMapa.setRutaRelativaIcono("../../images/maps/40px-verde-camion.png");

                marcadores.add(marcadorMapa);
            }
            if (marcadores.size()>0){
                data = marcadores.toArray(data); //convertimos a Array
                marcadores = null; //vaciamos list
            }

            //Usamos JSON para retornar el objeto respuesta
            String jsonOutput = gson.toJson(data);
            msgExitoExtra = jsonOutput;    
        }catch(Exception ex){
            msgError+="<ul>Ha ocurrido un error inesperado mientras se consultaban registros de Visitas : <br/>" + GenericMethods.exceptionStackTraceToString(ex);
        }
    }else if (mode.equals("censar_ubica_empleado")){
        
        int idEmpleado = 0;
        try{
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
        }catch(NumberFormatException ex){}
        String strWhereExtra = request.getParameter("extra1")!=null?new String(request.getParameter("extra1").getBytes("ISO-8859-1"),"UTF-8"):"";
        
        if (idEmpleado>0){
            EmpleadoBitacoraPosicionBO empleadoBitacoraPosicionBO = new EmpleadoBitacoraPosicionBO(user.getConn());
            empleadoBitacoraPosicionBO.setQueryOrderBy(" FECHA DESC ");
            EmpleadoBitacoraPosicion[] empleadoBitacoraPosicions = empleadoBitacoraPosicionBO.findEmpleadoBitacoraPosicions(-1, idEmpleado, 0, 1, strWhereExtra);
            
            if (empleadoBitacoraPosicions.length>0){
                EmpleadoBitacoraPosicion ebp =  empleadoBitacoraPosicions[0];
                
                MapUtil.MarcadorMapa marcadorMapa = new MapUtil.MarcadorMapa();
                marcadorMapa.setTipo("empleado_seguimiento");
                marcadorMapa.setLatitud( "" + ebp.getLatitud()  );
                marcadorMapa.setLongitud( "" + ebp.getLongitud() );
                marcadorMapa.setTitulo("Seguimiento a Empleado ID " + idEmpleado);
                marcadorMapa.setContenidoDialogo("");
                marcadorMapa.setRutaRelativaIcono("../../images/maps/car_1.png");
                marcadorMapa.setFechaHr(DateManage.timeToSQLDateTime(ebp.getFecha()));
                marcadorMapa.setVelocidadKmHr(Converter.meterSecToKmHr(ebp.getVelocidadMXSeg()));
                marcadorMapa.setDireccionGrados(ebp.getDireccionAvance());

                //Usamos JSON para retornar el objeto respuesta
                String jsonOutput = gson.toJson(marcadorMapa);//data);
                msgExitoExtra = jsonOutput;
            }
        }else{
           msgError += "<ul>Debe especificar un empleado válido."; 
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