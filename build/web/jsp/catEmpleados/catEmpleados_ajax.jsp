<%-- 
    Document   : catEmpleados_ajax
    Created on : 09-01-2013, 05:52:45 PM
    Author     : Leonardo
--%>

<%@page import="com.tsp.sct.bo.ZonaHorariaBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedido"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedidoDevolucionCambio"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensPedidoProductoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedidoProducto"%>
<%@page import="com.tsp.sct.bo.SGPedidoDevolucionesCambioBO"%>
<%@page import="com.tsp.sct.bo.SGPedidoProductoBO"%>
<%@page import="com.tsp.sct.bo.SGPedidoBO"%>
<%@page import="com.tsp.sct.dao.jdbc.InventarioHistoricoVendedorDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.InventarioHistoricoVendedor"%>
<%@page import="com.tsp.sct.dao.jdbc.PosMovilEstatusParametrosDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.PosMovilEstatusParametros"%>
<%@page import="com.tsp.sct.bo.AlmacenBO"%>
<%@page import="com.tsp.sct.dao.dto.ConceptoPk"%>
<%@page import="com.tsp.sct.bo.EmpleadoDatosInventarioAsignacionSesion"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.tsp.sct.dao.jdbc.GastosEvcDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.GastosEvc"%>
<%@page import="com.tsp.sct.bo.GastosEvcBO"%>
<%@page import="com.tsp.sct.dao.jdbc.MovimientoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Movimiento"%>
<%@page import="com.tsp.sct.dao.jdbc.ExistenciaAlmacenDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.ExistenciaAlmacen"%>
<%@page import="com.tsp.sct.bo.ExistenciaAlmacenBO"%>
<%@page import="com.tsp.sct.bo.BitacoraCreditosOperacionBO"%>
<%@page import="com.tsp.sct.dao.jdbc.ClienteDaoImpl"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="java.text.DateFormat"%>
<%@page import="com.tsp.sct.bo.SGClienteVendedorBO"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensClienteVendedorDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SgfensClienteVendedor"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.jdbc.InventarioInicialVendedorDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.InventarioInicialVendedor"%>
<%@page import="com.tsp.sct.bo.InventarioInicialVendedorBO"%>
<%@page import="com.tsp.sct.bo.EmpleadoAgendaBO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoAgenda"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoAgendaDaoImpl"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.tsp.sct.dao.jdbc.ConceptoDaoImpl"%>
<%@page import="com.tsp.sct.bo.ConceptoBO"%>
<%@page import="com.tsp.sct.dao.dto.Concepto"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoInventarioRepartidorDaoImpl"%>
<%@page import="com.tsp.sct.bo.EmpleadoInventarioRepartidorBO"%>
<%@page import="com.tsp.sct.dao.dto.EmpleadoInventarioRepartidor"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.PretoCaracteristicasConsola"%>
<%@page import="com.tsp.sct.dao.dto.EmpresaPermisoAplicacion"%>
<%@page import="com.tsp.sct.dao.jdbc.DispositivoMovilDaoImpl"%>
<%@page import="com.tsp.sct.bo.DispositivoMovilBO"%>
<%@page import="com.tsp.sct.dao.dto.DispositivoMovil"%>
<%@page import="com.tsp.sct.bo.DatosUsuarioBO"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.dao.dto.UsuariosPk"%>
<%@page import="com.tsp.sct.dao.jdbc.UsuariosDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Usuarios"%>
<%@page import="com.tsp.sct.dao.dto.DatosUsuarioPk"%>
<%@page import="com.tsp.sct.dao.jdbc.DatosUsuarioDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.DatosUsuario"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpresaDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Empresa"%>
<%@page import="com.tsp.sct.bo.EmpresaBO"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.tsp.sct.bo.EmpleadoBO"%>
<%@page import="com.tsp.sct.mail.TspMailBO"%>
<%@page import="com.tsp.sct.util.Encrypter"%>
<%@page import="com.tsp.sct.dao.jdbc.LdapDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Ldap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.EmpleadoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    //OBTENERMOS EL ID_EMPRESA_PADRE
    Empresa empresaDatosPadre = new Empresa();
    EmpresaBO empresaBOPadre = new EmpresaBO(user.getConn());
    try{
        empresaDatosPadre = empresaBOPadre.findEmpresabyId(idEmpresa);
    }catch(Exception ex){}
    
    boolean recargarListaInventarioEmpleadoSesion=false;
    
    boolean modoAdicionInventarioInicial = session.getAttribute("modoAdicionInventarioInicial")!=null? (Boolean) session.getAttribute("modoAdicionInventarioInicial") : false;
    
    /*
    * Parámetros
    */
    int idEmpleado = -1;
    int idCliente = -1;
    String numEmpleado ="";   
    String nombreEmpleado ="";
    String apellidoPaternoEmpleado="";
    String apellidoMaternoEmpleado="";
    String telefonoEmpleado="";
    String extension="";
    String celular="";
    String emailEmpleado="";
    String contacto="";
    String direccionEmpleado = "";
    String usuarioEmpleado = "";
    String contrasenaEmpleado = "";
    String recordatorioEmpleado = "";
    
    Date fecha = null;
    
    int idDispositivoMovilEmpleado = -1;
    int idRolEmpleado = -1;
    int idAutomovilEmpleado = -1;
    int idSucursalEmpresaAsignado = -1;
    int estatus = 2;//deshabilitado
    int permisoVentaRapida = 2;//deshabilitado
    int permisoVentaCredito = 2;//deshabilitado
    int repartidorEmpleado = 0; //no es repartidor
    int idRegion = -1; //no es repartidor
    
    int idPeriodo = -1;
    double sueldoEmpleado = 0;
    double comisionEmpleado = 0;
    int ventaConsigna = 0;// 0=  deshabilitado , 1 habilitado
    
    int trabajarFueraLinea = 2;//deshabilitado
    int clientesBarras = 2;//deshabilitado
    int precioCompra = 2;//deshabilitado
    double distanciaObligaEmple = 0;
    int permisoAccionesClientes = 3;//crear y editar
    
    int idHorario = 0;
    int tiempoMinutosActualiza = 0;
    int tiempoMinutosRecordatorio = 0;
    int idFoliosMovilEmpleado = 0;
    int intervaloUbicacionSegundos = 600; //10 minutos default
    
    int permisoDevoluciones = 0;
    int permisoAutoServicioInventario = 0;
    
    int noCobroParcial = 0;//deshabilitado
    int verProveedores = 0;//habiitado
   
    /*
    * Recepción de valores
    */
    
    String msgError = "";
    GenericValidator gc = new GenericValidator();
    
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
     if (mode.equals("2")){ //SI ES 2 ES PARA BORRAR
           //todo codigo paracambiar estatus           
           try{
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
           }catch(NumberFormatException ex){}           
            EmpleadoBO empleadoBO = new EmpleadoBO(idEmpleado,user.getConn());
            Empleado empleadoDto = empleadoBO.getEmpleado();
            empleadoDto.setIdEstatus(2);
            try{
                new EmpleadoDaoImpl(user.getConn()).update(empleadoDto.createPk(), empleadoDto);
                out.print("<!--EXITO-->Registro borrado satisfactoriamente");
            }catch(Exception ex){
                out.print("<!--ERROR-->No se pudo borrar el registro. Informe del error al administrador del sistema: " + ex.toString());
                ex.printStackTrace();
            }    
            try{
                //ACTUALIZAMOS EL NUMERO DE LICENCIAS DISPONIBLES
                //Empresa empresa = new Empresa();
                //EmpresaBO empresaBO = new EmpresaBO(user.getConn());
                //empresa = empresaBO.findEmpresabyId(idEmpresa);
                //empresa.setNumLicenciasDisponibles((empresa.getNumLicenciasDisponibles()+1));                                    
                Empresa empresaMatriz = new Empresa();
                EmpresaBO empresaMatrizBO = new EmpresaBO(user.getConn());
                empresaMatriz = empresaMatrizBO.findEmpresabyId(empresaDatosPadre.getIdEmpresaPadre());
                //empresaMatriz.setNumLicenciasDisponibles((empresaMatriz.getNumLicenciasDisponibles()+1));
                new EmpresaDaoImpl(user.getConn()).update(empresaMatriz.createPk(), empresaMatriz);
                //ADEMAS DESACTIVAMOS AL USUARIO
                UsuarioBO usuarioBO = new UsuarioBO(empleadoDto.getIdUsuarios());
                usuarioBO.getUser().setIdEstatus(2);
                new UsuariosDaoImpl(user.getConn()).update(usuarioBO.getUser().createPk(), usuarioBO.getUser());
            }catch(Exception ex){
                out.print("<!--ERROR-->Existio un problema con la actualización de las licencias. Informe del error al administrador del sistema: " + ex.toString());
                ex.printStackTrace();
            }    
                                                
    }else if(mode.equals("Estado")){//PARA CAMBIAR EL ESTADO DEL EMPLEADO
        int idEstadoEmpleado = -1;
        String msgError2 = "";
        try{
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
        }catch(NumberFormatException ex){}         
        try{
            idEstadoEmpleado = Integer.parseInt(request.getParameter("idEstadoEmpleado"));
        }catch(NumberFormatException ex){}
        
        System.out.println("-------------");
        System.out.println("ESTADO SELECCIONADO: "+idEstadoEmpleado);
        System.out.println("-------------");
        
        if(idEstadoEmpleado==-1)
        msgError2 += "<ul>El 'Estado' es requerido";
        
        if(msgError2.equals("")){//VALIDAMOS SI SE SELECCIONO UN ESTADO
            EmpleadoBO empleadoBO = new EmpleadoBO(idEmpleado,user.getConn());
            Empleado empleadoDto = empleadoBO.getEmpleado();
            empleadoDto.setIdEstado(idEstadoEmpleado);
            try{
                new EmpleadoDaoImpl(user.getConn()).update(empleadoDto.createPk(), empleadoDto);
                out.print("<!--EXITO-->Registro Actualizado satisfactoriamente");
            }catch(Exception ex){
                out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                ex.printStackTrace();
            }    
        }else{
            out.print("<!--ERROR-->"+msgError2);
        }
                     
         
    } else if(mode.equals("asignarGeo")){
        int idGeocerca = -1;
        String horaInicio = "";
        String horaFin = "";
        try{
            idGeocerca = Integer.parseInt(request.getParameter("idGeocerca"));
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
        }catch(NumberFormatException ex){}
        
        horaInicio = request.getParameter("inicio_hora")!=null?new String(request.getParameter("inicio_hora").getBytes("ISO-8859-1"),"UTF-8"):"";   
        horaFin = request.getParameter("fin_hora")!=null?new String(request.getParameter("fin_hora").getBytes("ISO-8859-1"),"UTF-8"):""; 
        DateFormat sdf = new SimpleDateFormat("HH:mm:ss");        
        
        if(idEmpleado>0){
            System.out.println("GEOCERCA ID: "+idGeocerca);
            System.out.println("EMPLEADO ID: "+idEmpleado);
            Empleado empl = new Empleado();
            EmpleadoBO emplBO = new EmpleadoBO(idEmpleado,user.getConn());
            if(idGeocerca>0){
                try{
                    empl = emplBO.getEmpleado();
                    empl.setIdGeocerca(idGeocerca);
                    if(!horaInicio.trim().equals("")){
                        empl.setHoraInicio(sdf.parse(horaInicio+":00"));
                        if(horaFin.trim().equals("")){
                            out.print("<!--ERROR-->"+"Debe colocar hora de fin de la geocerca.");
                            return;
                        }
                    }
                    if(!horaFin.trim().equals("")){
                        empl.setHoraFin(sdf.parse(horaFin+":00"));
                        if(horaInicio.trim().equals("")){
                            out.print("<!--ERROR-->"+"Debe colocar hora de inicio de la geocerca.");
                            return;
                        }
                    }
                    new EmpleadoDaoImpl(user.getConn()).update(empl.createPk(), empl);
                    out.print("<!--EXITO-->Geocerca asignada satisfactoriamente");
                }catch(Exception e){out.print("<!--ERROR-->"+"No se puede Asignar una geocerca en este momento");}
                return;
            }else{
                out.print("<!--ERROR-->"+"seleccione una geocerca");
                return;
            }
        }else{
            out.print("<!--ERROR-->"+"seleccione un empleado");
        }
        
    } else if(mode.equals("quitarInventario")){ //para quitar un producto del inventario
        int idConcepto = 0;
        try{
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
        }catch(NumberFormatException ex){}
        try{
            idConcepto = Integer.parseInt(request.getParameter("idConcepto"));
        }catch(NumberFormatException ex){}
        
        //desactivamos el inventario correspondiente:        
        EmpleadoInventarioRepartidor[] inventarios = new EmpleadoInventarioRepartidorDaoImpl(user.getConn()).findByDynamicWhere("ID_EMPLEADO = "+idEmpleado+" AND ID_CONCEPTO = "+idConcepto, null);
        EmpleadoInventarioRepartidor inventario = null;
        double cantidadDeArticulosDevueltos = 0;
        double pesoArticulosDevueltos = 0;
        ExistenciaAlmacen almPrincipal = null;
        ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(user.getConn());

        if(inventarios != null){
            if(inventarios.length > 0){
                inventario = inventarios[0];
                inventario.setIdEstatus(2);
                System.out.println("**************INVENTARIO ACTUAL TIENE: "+inventario.getCantidad());
                cantidadDeArticulosDevueltos = inventario.getCantidad();//(int)inventario.getCantidad();
                pesoArticulosDevueltos = inventario.getExistenciaGranel();
                System.out.println("**************cantidad a devolver: "+cantidadDeArticulosDevueltos);
                inventario.setCantidad(0);
                try{
                new EmpleadoInventarioRepartidorDaoImpl().update(inventario.createPk(), inventario);                
                //ademas de desactivarlo, le regresamos la cantidad de piezas que tenia el inventario al alamcen
                Concepto concepto = new ConceptoBO(idConcepto, user.getConn()).getConcepto();
                
                almPrincipal = exisAlmBO.getExistenciaProductoAlmacenPrincipal(concepto.getIdEmpresa(), concepto.getIdConcepto());
                
                if(almPrincipal!=null){
                    BigDecimal numArticulosDisponibles = (new BigDecimal(almPrincipal!=null?almPrincipal.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal cantDeArticulosDevueltos = (new BigDecimal(cantidadDeArticulosDevueltos)).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal stockTotal = numArticulosDisponibles.add(cantDeArticulosDevueltos);

                    if (pesoArticulosDevueltos>0){
                        BigDecimal pesoDisponibles = (new BigDecimal(almPrincipal!=null?almPrincipal.getExistenciaPeso():0)).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal cantPesoArticulosDevueltos = (new BigDecimal(pesoArticulosDevueltos)).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal stockPesoTotal = pesoDisponibles.add(cantPesoArticulosDevueltos);
                        almPrincipal.setExistenciaPeso(stockPesoTotal.doubleValue() );
                    }

                    //Actualizamos registro único de concepto                                            
                    almPrincipal.setExistencia(stockTotal.doubleValue());
                    new ExistenciaAlmacenBO(user.getConn()).updateBD(almPrincipal);
                
                }else{//si no existe en el alm se crea
                    int idAlmPrincipal = new AlmacenBO(user.getConn()).getAlmacenPrincipalByEmpresa(idEmpresa).getIdAlmacen();
                    
                    almPrincipal = new ExistenciaAlmacen();
                    almPrincipal.setIdAlmacen(idAlmPrincipal);
                    almPrincipal.setIdConcepto(idConcepto);
                    almPrincipal.setEstatus(1);
                    almPrincipal.setExistencia((new BigDecimal(cantidadDeArticulosDevueltos)).setScale(2, RoundingMode.HALF_UP).doubleValue());
                    if (pesoArticulosDevueltos>0){
                        almPrincipal.setExistenciaPeso( (new BigDecimal(pesoArticulosDevueltos)).setScale(2, RoundingMode.HALF_UP).doubleValue() );
                    }else{
                         almPrincipal.setExistenciaPeso(0);
                    }
                    
                    new ExistenciaAlmacenDaoImpl(user.getConn()).insert(almPrincipal);
                }
                
                System.out.println("**************AHORA HAY NUEVAMENTE DISPONIBLES: "+ (almPrincipal!=null?almPrincipal.getExistencia():0) );
                 //concepto.setNumArticulosDisponibles( stockTotal.doubleValue() );
                //new ConceptoDaoImpl(user.getConn()).update(concepto.createPk(), concepto);
                
                out.print("<!--EXITO-->El concepto se quito del inventario satisfactoriamente.");
                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo dar de baja del inventario. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }  
                
            }
        }
        

    }else if(mode.equals("agregarInventarioSesionTemporal")){
        System.out.println("_________AGREGANDO INVENTARIO EN SESION");
        int idConceptoInventario = -1;
        double numArticulosInventarioAsignar = -1;
        boolean prodExistenteEmpleado = false;
        int idAlmacenOrigen = -1;
        
        try{
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
        }catch(NumberFormatException ex){}
        try{
            idConceptoInventario = Integer.parseInt(request.getParameter("idConceptoInventario"));
        }catch(NumberFormatException ex){}
        try{
            numArticulosInventarioAsignar = Double.parseDouble(request.getParameter("numArticulosInventarioAsignar"));
        }catch(NumberFormatException ex){}
        try{
            idAlmacenOrigen = Integer.parseInt(request.getParameter("idAlmacen"));
        }catch(NumberFormatException ex){}
        
        double pesoArticulosInventarioAsignar = 0;
        try{
            pesoArticulosInventarioAsignar = Double.parseDouble(request.getParameter("pesoArticulosInventarioAsignar"));
        }catch(NumberFormatException ex){}catch(Exception e){}
        
        double pesoTotalGranelAsignar = 0;
        try{
            pesoTotalGranelAsignar = numArticulosInventarioAsignar * pesoArticulosInventarioAsignar;
        }catch(Exception e){}
        
        String aGranel = request.getParameter("aGranel")!=null?new String(request.getParameter("aGranel").getBytes("ISO-8859-1"),"UTF-8"):"";
        
        if(idAlmacenOrigen > 0){
            if(idConceptoInventario > 0){//verificamos si se selecciono un concepto
                //verificamos si es mayor o igual a cero la cantidad a asignar al inventario
                if(numArticulosInventarioAsignar > 0 || pesoArticulosInventarioAsignar > 0){
                    Concepto concepto = new ConceptoBO(idConceptoInventario, user.getConn()).getConcepto();

                    ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(user.getConn()); 
                    ExistenciaAlmacen almPrincipal = exisAlmBO.getExistenciaProductoAlmacen(idAlmacenOrigen, concepto.getIdConcepto());


                    System.out.println("____CONCEPTO A BUSCAR: "+idConceptoInventario+", cantidad actual(almacén): "+ (almPrincipal!=null?almPrincipal.getExistencia():0) +", cantidad solicitada: "+numArticulosInventarioAsignar);
                    System.out.println("____CONCEPTO A BUSCAR: "+idConceptoInventario+", peso actual(almacén): "+ (almPrincipal!=null?almPrincipal.getExistenciaPeso():0) +", peso solicitada: "+pesoTotalGranelAsignar);
                        //verificamos si el numero solicitado es igual o menor al que existe en bodega
                    if((almPrincipal!=null?almPrincipal.getExistencia():0) >= numArticulosInventarioAsignar
                            && ( (almPrincipal!=null?almPrincipal.getExistenciaPeso():0) >= pesoTotalGranelAsignar ) ){
                                
                        List<EmpleadoDatosInventarioAsignacionSesion> listaObjetosInventario = null;
                        try{
                            listaObjetosInventario = (ArrayList<EmpleadoDatosInventarioAsignacionSesion>)session.getAttribute("empleadoDatosInventarioSesion");
                            System.out.println("_________ empleadoDatosInventarioSesion CARGADO DE SESION");
                        }catch(Exception e){
                            listaObjetosInventario = new ArrayList<EmpleadoDatosInventarioAsignacionSesion>();
                            System.out.println("_________ empleadoDatosInventarioSesion CREADO COMO NUEVO");
                        }

                        if(listaObjetosInventario == null){
                            listaObjetosInventario = new ArrayList<EmpleadoDatosInventarioAsignacionSesion>();
                        }
                        
                        double numArticulosInventarioAsignarAyuda = numArticulosInventarioAsignar; //variable de ayuda para ver si el inventario es suficiente a los articulos del mismo tipo en sesion
                        for(EmpleadoDatosInventarioAsignacionSesion articulosSesion : listaObjetosInventario){
                            if(articulosSesion.getIdConceptoInventario() == idConceptoInventario){
                                numArticulosInventarioAsignarAyuda += articulosSesion.getNumArticulosInventarioAsignar();
                            }
                        }
                        if(numArticulosInventarioAsignarAyuda > almPrincipal.getExistencia()){
                            out.print("<!--ERROR-->La sumatoria de la cantidad del producto solicitado es mayor a la que existe en Almacen. Verifique que la sumatoria del inventario asignado sea menor a la que se encuentra en almacen.");
                        }else{                        
                            EmpleadoDatosInventarioAsignacionSesion inventarioSesion = new EmpleadoDatosInventarioAsignacionSesion();
                            inventarioSesion.setIdConceptoInventario(idConceptoInventario);
                            inventarioSesion.setNumArticulosInventarioAsignar(numArticulosInventarioAsignar);
                            inventarioSesion.setProdExistenteEmpleado(prodExistenteEmpleado);
                            inventarioSesion.setIdAlmacenOrigen(idAlmacenOrigen);
                            inventarioSesion.setIdEmpleado(idEmpleado);        
                            inventarioSesion.setPesoArticulosInventarioAsignar(pesoArticulosInventarioAsignar);        
                            listaObjetosInventario.add(inventarioSesion);

                            session.setAttribute("empleadoDatosInventarioSesion", listaObjetosInventario);
                            recargarListaInventarioEmpleadoSesion = true;
                            out.print("<!--EXISESION-->");
                        }
                    }else{//si no hay articulos suficientes para asignar mandamos mensaje de alerta:
                        out.print("<!--ERROR-->La cantidad (o peso) solicitada es mayor a la que existe en Almacen.");
                    }
                }else if(numArticulosInventarioAsignar <= 0){//si es esta regresando mercancia
                    out.print("<!--ERROR-->La cantidad debe ser mayor a 0.");
                }
            }else{
                out.print("<!--ERROR-->Seleccione un Concepto.");
            }        
        }else{
            out.print("<!--ERROR-->Seleccione un Almacén.");
        }        
    }else if(mode.equals("quitarInventarioSesionTemporal")){
        int index_lista_producto = 0;
        try{
            index_lista_producto = Integer.parseInt(request.getParameter("index_lista_producto"));
        }catch(NumberFormatException ex){}        
        List<EmpleadoDatosInventarioAsignacionSesion> listaObjetosInventario = null;
        try{
            listaObjetosInventario = (ArrayList<EmpleadoDatosInventarioAsignacionSesion>)session.getAttribute("empleadoDatosInventarioSesion");            
        }catch(Exception e){
            listaObjetosInventario = new ArrayList<EmpleadoDatosInventarioAsignacionSesion>();            
        }
        if(listaObjetosInventario != null){            
            listaObjetosInventario.remove(index_lista_producto);         
        }
        recargarListaInventarioEmpleadoSesion = true;
        out.print("<!--EXITO-->");
    }else if(mode.equals("agregarInventario")){
        System.out.println("_________AGREGANDO INVENTARIO");
        int idConceptoInventario = -1;
        double numArticulosInventarioAsignar = -1;
        boolean prodExistenteEmpleado = false;
        int idAlmacenOrigen = -1;
        
        try{
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
        }catch(NumberFormatException ex){}
        try{
            idConceptoInventario = Integer.parseInt(request.getParameter("idConceptoInventario"));
        }catch(NumberFormatException ex){}
        try{
            numArticulosInventarioAsignar = Double.parseDouble(request.getParameter("numArticulosInventarioAsignar"));
        }catch(NumberFormatException ex){}
        try{
            idAlmacenOrigen = Integer.parseInt(request.getParameter("idAlmacen"));
        }catch(NumberFormatException ex){}
        
        double pesoArticulosInventarioAsignar = 0;
        try{
            pesoArticulosInventarioAsignar = Double.parseDouble(request.getParameter("pesoArticulosInventarioAsignar"));
        }catch(NumberFormatException ex){}catch(Exception e){}
        
        String aGranel = request.getParameter("aGranel")!=null?new String(request.getParameter("aGranel").getBytes("ISO-8859-1"),"UTF-8"):"";
        
        if(idAlmacenOrigen > 0){
        
            if(idConceptoInventario > 0){//verificamos si se selecciono un concepto
                //verificamos si es mayor o igual a cero la cantidad a asignar al inventario
                if(numArticulosInventarioAsignar > 0 || pesoArticulosInventarioAsignar > 0){
                    Concepto concepto = new ConceptoBO(idConceptoInventario, user.getConn()).getConcepto();

                    ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(user.getConn()); 
                    ExistenciaAlmacen almPrincipal = exisAlmBO.getExistenciaProductoAlmacen(idAlmacenOrigen, concepto.getIdConcepto());


                    System.out.println("____CONCEPTO A BUSCAR: "+idConceptoInventario+", cantidad actual(almacén): "+ (almPrincipal!=null?almPrincipal.getExistencia():0) +", cantidad solicitada: "+numArticulosInventarioAsignar);
                    if((almPrincipal!=null?almPrincipal.getExistencia():0) >= numArticulosInventarioAsignar){//verificamos si el numero solicitado es igual o menor al que existe en bodega
                        //verificamos si ya existe en inventario el articulo
                        EmpleadoInventarioRepartidor[] inventarios = new EmpleadoInventarioRepartidorDaoImpl(user.getConn()).findByDynamicWhere("ID_EMPLEADO = "+idEmpleado+" AND ID_CONCEPTO = "+idConceptoInventario, null);
                        if(inventarios.length > 0 && !aGranel.equals("aGranel")){//si existe lo actualizamos                        
                            EmpleadoInventarioRepartidor inventarioExistente = inventarios[0];
                            //Si esta activo avisamos que ya se encuentra en el inventario del repartidor, cuando es nuevo, pero si se esta modificando la cantidad, vemos de que producto se actualiza:
                            if(inventarioExistente.getIdEstatus() == 1){
                                int idInventarioAsignado = -1;
                                 try{
                                        idInventarioAsignado = Integer.parseInt(request.getParameter("idInventarioAsignado"));
                                    }catch(NumberFormatException ex){}
                                if(idInventarioAsignado > 0){
                                    inventarioExistente.setIdEstatus(1);
                                    inventarioExistente.setCantidad( (inventarioExistente.getCantidad() + numArticulosInventarioAsignar) );
                                    inventarioExistente.setPeso(pesoArticulosInventarioAsignar);
                                    new EmpleadoInventarioRepartidorDaoImpl(user.getConn()).update(inventarioExistente.createPk(), inventarioExistente);
                                    out.print("<!--EXITO-->Inventario actualizado satisfactoriamente");

                                    //Registramos movimiento de almacen
                                    try{
                                        Movimiento movimientoDto = new Movimiento(); 
                                        MovimientoDaoImpl movimientosDaoImpl = new MovimientoDaoImpl(user.getConn());

                                        movimientoDto.setIdEmpresa(idEmpresa);
                                        movimientoDto.setTipoMovimiento("SALIDA");
                                        movimientoDto.setNombreProducto(concepto.getNombreDesencriptado());
                                        movimientoDto.setContabilidad(numArticulosInventarioAsignar);
                                        movimientoDto.setIdProveedor(-1);
                                        movimientoDto.setOrdenCompra("");
                                        movimientoDto.setNumeroGuia("");                             
                                        movimientoDto.setIdAlmacen(idAlmacenOrigen);                
                                        movimientoDto.setConceptoMovimiento("Asignación de Producto a Vendedor" + (modoAdicionInventarioInicial ? " - ADICIÓN A INVENTARIO INICIAL.": ""));                
                                        try{
                                            movimientoDto.setFechaRegistro(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                                        }catch(Exception e){
                                            movimientoDto.setFechaRegistro(new Date());
                                        }
                                        movimientoDto.setIdConcepto(concepto.getIdConcepto());
                                        movimientoDto.setIdEmpleado(idEmpleado);                                    

                                        //Insert
                                        movimientosDaoImpl.insert(movimientoDto);

                                    }catch(Exception e){                                    
                                    }

                                }else{
                                    prodExistenteEmpleado = true;
                                    out.print("<!--ERROR-->El concepto ya existe en el inventario del repartidor. Editelo.");
                                }
                            }else{//Si esta inactivo:                        
                                inventarioExistente.setIdEstatus(1);
                                inventarioExistente.setCantidad(numArticulosInventarioAsignar);
                                inventarioExistente.setPeso(pesoArticulosInventarioAsignar);
                                new EmpleadoInventarioRepartidorDaoImpl(user.getConn()).update(inventarioExistente.createPk(), inventarioExistente);
                                out.print("<!--EXITO-->Inventario actualizado satisfactoriamente");
                            }
                        }else{//si no existe lo creamos
                            EmpleadoInventarioRepartidor inventario = new EmpleadoInventarioRepartidor();
                            inventario.setIdEmpleado(idEmpleado);
                            inventario.setIdConcepto(idConceptoInventario);
                            inventario.setTipoProductoServicio(1);//0 es servicio, 1 concepto.
                            inventario.setCantidad(numArticulosInventarioAsignar);
                            inventario.setIdEstatus(1);
                            inventario.setPeso(pesoArticulosInventarioAsignar);
                            new EmpleadoInventarioRepartidorDaoImpl(user.getConn()).insert(inventario);
                            out.print("<!--EXITO-->Inventario asignado satisfactoriamente");

                            //Registramos movimiento de almacen
                            try{
                                Movimiento movimientoDto = new Movimiento(); 
                                MovimientoDaoImpl movimientosDaoImpl = new MovimientoDaoImpl(user.getConn());

                                movimientoDto.setIdEmpresa(idEmpresa);
                                movimientoDto.setTipoMovimiento("SALIDA");
                                movimientoDto.setNombreProducto(concepto.getNombreDesencriptado());
                                movimientoDto.setContabilidad(numArticulosInventarioAsignar);
                                movimientoDto.setIdProveedor(-1);
                                movimientoDto.setOrdenCompra("");
                                movimientoDto.setNumeroGuia("");                             
                                movimientoDto.setIdAlmacen(idAlmacenOrigen);                
                                movimientoDto.setConceptoMovimiento("Asignación de Producto a Vendedor" + (modoAdicionInventarioInicial ? " - ADICIÓN A INVENTARIO INICIAL.": ""));
                                
                                try{
                                    movimientoDto.setFechaRegistro(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                                }catch(Exception e){
                                    movimientoDto.setFechaRegistro(new Date());
                                }
                                movimientoDto.setIdConcepto(concepto.getIdConcepto());
                                movimientoDto.setIdEmpleado(idEmpleado);                                    

                                //Insert
                                movimientosDaoImpl.insert(movimientoDto);

                            }catch(Exception e){                                    
                            }
                            
                        }
                        
                        if(numArticulosInventarioAsignar > 0 && !prodExistenteEmpleado){//si se repartio cantidad de almacen la restamos del almacen
                            //actualizamos el inventario del almacen:
                            BigDecimal numArticulosDisponibles = (new BigDecimal(almPrincipal!=null?almPrincipal.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal numArtInventarioAsignar = (new BigDecimal(numArticulosInventarioAsignar)).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal stockTotal = numArticulosDisponibles.subtract(numArtInventarioAsignar);

                            almPrincipal.setExistencia(stockTotal.doubleValue() );
                            new ExistenciaAlmacenBO(user.getConn()).updateBD(almPrincipal);
                            //new ConceptoDaoImpl(user.getConn()).update(concepto.createPk(), concepto);
                            
                             //Registramos en caso de Modo "adicion a inventario inicial" los cambios
                            try{
                                if (modoAdicionInventarioInicial){                                            
                                    InventarioInicialVendedorBO inventarioInicialVendedorBO = new InventarioInicialVendedorBO(user.getConn());
                                    try{
                                        inventarioInicialVendedorBO.adicionInventarioInicial(idEmpleado, idEmpresa, concepto.getIdConcepto(), numArticulosInventarioAsignar);
                                        //Inventario Inicial actualizado satisfactoriamente
                                    }catch(Exception ex){
                                        ex.printStackTrace();
                                    }
                                }
                            }catch(Exception ex){
                                ex.printStackTrace();
                            }
                            
                        }
                    }else{//si no hay articulos suficientes para asignar mandamos mensaje de alerta:
                        out.print("<!--ERROR-->La cantidad solicitada es mayor a la que existe en Almacen.");
                    }
                }else if(numArticulosInventarioAsignar <= 0){//si es esta regresando mercancia
                    out.print("<!--ERROR-->La cantidad debe ser mayor a 0.");

                }
            }else{
                out.print("<!--ERROR-->Seleccione un Concepto.");
            }
        
        }else{
            out.print("<!--ERROR-->Seleccione un Almacén.");
        }
    }else if(mode.equals("devolverInventario"))  { 
        
        System.out.println("_________DEVOLVIENDO INVENTARIO");
        int idConceptoInventario = -1;
        double numArticulosInventarioAsignar = -1;
        int idAlmacenDestino = -1;
        boolean nuevo = false;
        try{
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
        }catch(NumberFormatException ex){}
        try{
            idConceptoInventario = Integer.parseInt(request.getParameter("idConceptoInventario"));
        }catch(NumberFormatException ex){}
        try{
            numArticulosInventarioAsignar = Double.parseDouble(request.getParameter("numArticulosInventarioAsignar"));
        }catch(NumberFormatException ex){}
        try{
            idAlmacenDestino = Integer.parseInt(request.getParameter("idAlmacen"));
        }catch(NumberFormatException ex){}
        
        if(idAlmacenDestino > 0){
        
            if(idConceptoInventario > 0){//verificamos si se selecciono un concepto
                //verificamos si es mayor o igual a cero la cantidad a asignar al inventario
                if(numArticulosInventarioAsignar >= 0){


                    Concepto concepto = new ConceptoBO(idConceptoInventario, user.getConn()).getConcepto();

                    ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(user.getConn()); 
                    ExistenciaAlmacen almPrincipal = exisAlmBO.getExistenciaProductoAlmacen(idAlmacenDestino, concepto.getIdConcepto());                

                    System.out.println("____CONCEPTO A BUSCAR: "+idConceptoInventario+", cantidad actual : "+ (almPrincipal!=null?almPrincipal.getExistencia():0) +", cantidad solicitada: "+numArticulosInventarioAsignar);

                        //verificamos si ya existe en inventario el articulo
                        EmpleadoInventarioRepartidor[] inventarios = new EmpleadoInventarioRepartidorDaoImpl(user.getConn()).findByDynamicWhere("ID_EMPLEADO = "+idEmpleado+" AND ID_CONCEPTO = "+idConceptoInventario, null);
                        if(inventarios.length > 0){//si existe lo actualizamos                        
                            EmpleadoInventarioRepartidor inventarioExistente = inventarios[0];
                            //Si esta activo avisamos que ya se encuentra en el inventario del repartidor, cuando es nuevo, pero si se esta modificando la cantidad, vemos de que producto se actualiza:
                            if(inventarioExistente.getIdEstatus() == 1){
                                int idInventarioAsignado = -1;
                                 try{
                                        idInventarioAsignado = Integer.parseInt(request.getParameter("idInventarioAsignado"));
                                    }catch(NumberFormatException ex){}
                                if(idInventarioAsignado > 0){
                                        if(inventarioExistente.getCantidad() >= Math.abs(numArticulosInventarioAsignar)){
                                            inventarioExistente.setIdEstatus(1);
                                            inventarioExistente.setCantidad( (inventarioExistente.getCantidad() - numArticulosInventarioAsignar) ) ;// Se queda con signo mas para restar al stock de repoartidor  -> regla   + (- ) = -
                                            new EmpleadoInventarioRepartidorDaoImpl(user.getConn()).update(inventarioExistente.createPk(), inventarioExistente);


                                            //Actualizamos stock gral
                                            if(numArticulosInventarioAsignar > 0){ //si se repartio cantidad de almacen la restamos del almacen

                                                //actualizamos el inventario del almacen:
                                                BigDecimal numArticulosDisponibles = (new BigDecimal(almPrincipal!=null?almPrincipal.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                                                BigDecimal numArtInventarioAsignar = (new BigDecimal(numArticulosInventarioAsignar)).setScale(2, RoundingMode.HALF_UP);
                                                BigDecimal stockTotal = numArticulosDisponibles.add(numArtInventarioAsignar);



                                                if(almPrincipal==null){
                                                    nuevo = true;
                                                    almPrincipal = new ExistenciaAlmacen();
                                                    almPrincipal.setIdAlmacen(idAlmacenDestino);
                                                    almPrincipal.setIdConcepto(concepto.getIdConcepto());
                                                    almPrincipal.setExistencia(stockTotal.doubleValue());
                                                    almPrincipal.setEstatus(1);
                                                }else{
                                                    almPrincipal.setExistencia( stockTotal.doubleValue());
                                                }

                                                if(nuevo){
                                                    new ExistenciaAlmacenDaoImpl().insert(almPrincipal); 
                                                }else{
                                                    new ExistenciaAlmacenBO(user.getConn()).updateBD(almPrincipal);
                                                }


                                            }


                                            out.print("<!--EXITO-->Inventario actualizado satisfactoriamente");


                                            //Registramos movimiento de almacen
                                            try{
                                                Movimiento movimientoDto = new Movimiento(); 
                                                MovimientoDaoImpl movimientosDaoImpl = new MovimientoDaoImpl(user.getConn());

                                                movimientoDto.setIdEmpresa(idEmpresa);
                                                movimientoDto.setTipoMovimiento("ENTRADA");
                                                movimientoDto.setNombreProducto(concepto.getNombreDesencriptado());
                                                movimientoDto.setContabilidad(numArticulosInventarioAsignar);
                                                movimientoDto.setIdProveedor(-1);
                                                movimientoDto.setOrdenCompra("");
                                                movimientoDto.setNumeroGuia("");                             
                                                movimientoDto.setIdAlmacen(idAlmacenDestino);                
                                                movimientoDto.setConceptoMovimiento("Retorno de Producto desde Vendedor");                
                                                
                                                try{
                                                    movimientoDto.setFechaRegistro(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                                                }catch(Exception e){
                                                    movimientoDto.setFechaRegistro(new Date());
                                                }
                                                movimientoDto.setIdConcepto(concepto.getIdConcepto());
                                                movimientoDto.setIdEmpleado(idEmpleado);                                    

                                                //Insert
                                                movimientosDaoImpl.insert(movimientoDto);

                                            }catch(Exception e){                                    
                                            }


                                        }else{
                                             out.print("<!--EXITO-->No puede regresar mas cantidad de la que tiene asignada.");
                                        }

                                }else{
                                    out.print("<!--ERROR-->El concepto ya existe en el inventario del repartidor. Editelo.");
                                }
                            }else{//Si esta inactivo:                        

                                out.print("<!--EXITO-->Concepto no Existente en inventario de Repartidor");
                            }
                        }


                }else if(numArticulosInventarioAsignar < 0){//si es esta regresando mercancia
                    out.print("<!--ERROR-->La cantidad debe ser mayor a 0."); 

                }
            }else{
                out.print("<!--ERROR-->Seleccione un Concepto.");
            }
        }else{
            out.print("<!--ERROR-->Seleccione un Almacén.");
        }
        
        
    }else if(mode.equals("agregarInventarioGranel")){
        System.out.println("_________AGREGANDO INVENTARIO");
        
        List<EmpleadoDatosInventarioAsignacionSesion> listaObjetosInventario = null;
        try{
            listaObjetosInventario = (ArrayList<EmpleadoDatosInventarioAsignacionSesion>)session.getAttribute("empleadoDatosInventarioSesion");
        }catch(Exception e){}
        
        if(listaObjetosInventario != null){
            for(EmpleadoDatosInventarioAsignacionSesion inventarioSesion : listaObjetosInventario){
                int idConceptoInventario = -1;
                double numArticulosInventarioAsignar = -1;
                boolean prodExistenteEmpleado = false;
                int idAlmacenOrigen = -1;

                try{
                    idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
                }catch(NumberFormatException ex){}
                try{
                    idConceptoInventario = inventarioSesion.getIdConceptoInventario();
                }catch(NumberFormatException ex){}
                try{
                    numArticulosInventarioAsignar = inventarioSesion.getNumArticulosInventarioAsignar();
                }catch(NumberFormatException ex){}
                try{
                    idAlmacenOrigen = inventarioSesion.getIdAlmacenOrigen();
                }catch(NumberFormatException ex){}

                double pesoArticulosInventarioAsignar = 0;
                try{
                    pesoArticulosInventarioAsignar = inventarioSesion.getPesoArticulosInventarioAsignar();
                }catch(NumberFormatException ex){}catch(Exception e){}
                double pesoTotalGranelAsignar = 0;
                try{
                    pesoTotalGranelAsignar = inventarioSesion.getNumArticulosInventarioAsignar() * inventarioSesion.getPesoArticulosInventarioAsignar();
                }catch(Exception e){}

                String aGranel = request.getParameter("aGranel")!=null?new String(request.getParameter("aGranel").getBytes("ISO-8859-1"),"UTF-8"):"";

                if(idAlmacenOrigen > 0){

                    if(idConceptoInventario > 0){//verificamos si se selecciono un concepto
                        //verificamos si es mayor o igual a cero la cantidad a asignar al inventario
                        if(numArticulosInventarioAsignar > 0 || pesoArticulosInventarioAsignar > 0){
                            Concepto conceptoPadre = new ConceptoBO(idConceptoInventario, user.getConn()).getConcepto();

                            ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(user.getConn()); 
                            ExistenciaAlmacen almPrincipal = exisAlmBO.getExistenciaProductoAlmacen(idAlmacenOrigen, conceptoPadre.getIdConcepto());


                            System.out.println("____CONCEPTO A BUSCAR: "+idConceptoInventario+", cantidad actual(almacén): "+ (almPrincipal!=null?almPrincipal.getExistencia():0) +", cantidad solicitada: "+numArticulosInventarioAsignar);
                            System.out.println("___________________________, Peso stock actual(almacén): "+ (almPrincipal!=null?almPrincipal.getExistenciaPeso():0) +", Peso solicitado: "+pesoTotalGranelAsignar);
                            //verificamos si el numero solicitado es igual o menor al que existe en bodega
                            if((almPrincipal!=null?almPrincipal.getExistencia():0) >= numArticulosInventarioAsignar
                                    && ( (almPrincipal!=null?almPrincipal.getExistenciaPeso():0) >= pesoTotalGranelAsignar ) ){ 
                                //verificamos si ya existe en inventario el articulo
                                //EmpleadoInventarioRepartidor[] inventarios = new EmpleadoInventarioRepartidorDaoImpl(user.getConn()).findByDynamicWhere("ID_EMPLEADO = "+idEmpleado+" AND ID_CONCEPTO = "+idConceptoInventario, null);
                                EmpleadoInventarioRepartidor[] inventarios = new EmpleadoInventarioRepartidorDaoImpl(user.getConn()).findByDynamicWhere(
                                        "ID_EMPLEADO = "+idEmpleado+" AND PESO = " + pesoArticulosInventarioAsignar
                                                + " AND ID_CONCEPTO IN  (SELECT ID_CONCEPTO FROM CONCEPTO WHERE ID_CONCEPTO_PADRE="+idConceptoInventario + ")", null);
                                if(inventarios.length > 0){ //&& !aGranel.equals("aGranel")){//si existe lo actualizamos                        
                                    EmpleadoInventarioRepartidor inventarioExistente = inventarios[0];
                                    //Si esta activo avisamos que ya se encuentra en el inventario del repartidor, cuando es nuevo, pero si se esta modificando la cantidad, vemos de que producto se actualiza:
                                    if(inventarioExistente.getIdEstatus() == 1){
                                        int idInventarioAsignado = -1;
                                         try{
                                                idInventarioAsignado = Integer.parseInt(request.getParameter("idInventarioAsignado"));
                                            }catch(NumberFormatException ex){}
                                         
                                        //if(idInventarioAsignado > 0){
                                            inventarioExistente.setIdEstatus(1);
                                            inventarioExistente.setCantidad( (inventarioExistente.getCantidad() + numArticulosInventarioAsignar) );
                                            inventarioExistente.setPeso(pesoArticulosInventarioAsignar);
                                            inventarioExistente.setExistenciaGranel( inventarioExistente.getExistenciaGranel() + pesoTotalGranelAsignar );
                                            new EmpleadoInventarioRepartidorDaoImpl(user.getConn()).update(inventarioExistente.createPk(), inventarioExistente);
                                            out.print("<!--EXITO-->Inventario actualizado satisfactoriamente");

                                            //Registramos movimiento de almacen
                                            try{
                                                Movimiento movimientoDto = new Movimiento(); 
                                                MovimientoDaoImpl movimientosDaoImpl = new MovimientoDaoImpl(user.getConn());

                                                movimientoDto.setIdEmpresa(idEmpresa);
                                                movimientoDto.setTipoMovimiento("SALIDA");
                                                movimientoDto.setNombreProducto(conceptoPadre.getNombreDesencriptado());
                                                movimientoDto.setContabilidad(numArticulosInventarioAsignar);
                                                movimientoDto.setIdProveedor(-1);
                                                movimientoDto.setOrdenCompra("");
                                                movimientoDto.setNumeroGuia("");                             
                                                movimientoDto.setIdAlmacen(idAlmacenOrigen);                
                                                movimientoDto.setConceptoMovimiento("Asignación de Producto a Vendedor");                
                                                
                                                try{
                                                    movimientoDto.setFechaRegistro(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                                                }catch(Exception e){
                                                    movimientoDto.setFechaRegistro(new Date());
                                                }
                                                movimientoDto.setIdConcepto(conceptoPadre.getIdConcepto());
                                                movimientoDto.setIdEmpleado(idEmpleado);
                                                movimientoDto.setContabilidadPeso(pesoTotalGranelAsignar);

                                                //Insert
                                                movimientosDaoImpl.insert(movimientoDto);

                                            }catch(Exception e){                                    
                                            }
                                            
                                            if(idInventarioAsignado <= 0){
                                                out.print("<br/> El concepto ya existia en el inventario del repartidor, se sumaron las cantidades.");
                                            }
                                        //}else{
                                        //    prodExistenteEmpleado = true;
                                        //    out.print("<!--ERROR-->El concepto ya existe en el inventario del repartidor.");
                                        //}
                                    }else{//Si esta inactivo:                        
                                        inventarioExistente.setIdEstatus(1);
                                        inventarioExistente.setCantidad(numArticulosInventarioAsignar);
                                        inventarioExistente.setPeso(pesoArticulosInventarioAsignar);
                                        inventarioExistente.setExistenciaGranel( pesoTotalGranelAsignar );
                                        new EmpleadoInventarioRepartidorDaoImpl(user.getConn()).update(inventarioExistente.createPk(), inventarioExistente);
                                        //out.print("<!--EXITO-->Inventario actualizado satisfactoriamente");
                                    }
                                }else{//si no existe o es a Granel lo creamos
                                    
                                    if (aGranel.equals("aGranel")){
                                        //fue capturado a Granel, por lo tanto debemos
                                        // - crear nuevo registro de producto igual al original con algunos datos diferentes (nombre, desc, peso, id_concepto_padre)
                                        // - el nuevo registro de EmpleadoInventarioRepartidor
                                        ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
                                        int idConceptoPadre = conceptoPadre.getIdConcepto();
                                        
                                        //Primero buscamos un Concepto Hijo con las mismas caracteristicas
                                        // es decir que pertenezca al mismo Producto Padre y que tenga el mismo Peso Unitario que esta solicitando el usuario
                                        Concepto conceptoHijo = null;
                                        try{
                                            Concepto[] conceptosCoincidencia = conceptoBO.findConceptos(-1, idEmpresa, 0, 0, 
                                                    " AND ID_CONCEPTO_PADRE = " + idConceptoPadre + " AND PESO="+pesoArticulosInventarioAsignar);
                                            if (conceptosCoincidencia.length>0)
                                                conceptoHijo = conceptosCoincidencia[0];
                                        }catch(Exception ex){ ex.printStackTrace(); }
                                        
                                        if (conceptoHijo == null){
                                            // si no existe entonces clonamos el Padre y lo creamos nuevo
                                            conceptoHijo = conceptoBO.clonar(conceptoPadre);
                                            conceptoHijo.setIdConcepto(0);
                                            conceptoHijo.setIdConceptoModified(false);
                                            conceptoHijo.setIdConceptoPadre(idConceptoPadre);
                                            String nombre = conceptoBO.desencripta(conceptoPadre.getNombre()) + " ("+pesoArticulosInventarioAsignar + " Kg)";
                                            conceptoHijo.setNombre(conceptoBO.encripta(nombre));
                                            conceptoHijo.setNombreDesencriptado(nombre);
                                            conceptoHijo.setDescripcion(conceptoPadre.getDescripcion()+ " (Venta a Granel, Piezas de "+pesoArticulosInventarioAsignar + " Kg)");
                                            conceptoHijo.setPeso(pesoArticulosInventarioAsignar);
                                            
                                            new ConceptoDaoImpl(user.getConn()).insert(conceptoHijo);
                                        }
                                        
                                        //El nuevo registro de EmpleadoInventarioRepartidor debe apuntar hacia el concepto hijo
                                        idConceptoInventario = conceptoHijo.getIdConcepto();
                                    }
                                    
                                    EmpleadoInventarioRepartidor inventario = new EmpleadoInventarioRepartidor();
                                    inventario.setIdEmpleado(idEmpleado);
                                    inventario.setIdConcepto(idConceptoInventario);
                                    inventario.setTipoProductoServicio(1);//0 es servicio, 1 concepto.
                                    inventario.setCantidad(numArticulosInventarioAsignar);
                                    inventario.setIdEstatus(1);
                                    inventario.setPeso(pesoArticulosInventarioAsignar);
                                    inventario.setExistenciaGranel( pesoTotalGranelAsignar );
                                    new EmpleadoInventarioRepartidorDaoImpl(user.getConn()).insert(inventario);                                    
                                    //out.print("<!--EXITO-->Inventario actualizado satisfactoriamente");
                                    //Registramos movimiento de almacen
                                    try{
                                        Movimiento movimientoDto = new Movimiento(); 
                                        MovimientoDaoImpl movimientosDaoImpl = new MovimientoDaoImpl(user.getConn());

                                        movimientoDto.setIdEmpresa(idEmpresa);
                                        movimientoDto.setTipoMovimiento("SALIDA");
                                        movimientoDto.setNombreProducto(conceptoPadre.getNombreDesencriptado());
                                        movimientoDto.setContabilidad(numArticulosInventarioAsignar);
                                        movimientoDto.setIdProveedor(-1);
                                        movimientoDto.setOrdenCompra("");
                                        movimientoDto.setNumeroGuia("");                             
                                        movimientoDto.setIdAlmacen(idAlmacenOrigen);                
                                        movimientoDto.setConceptoMovimiento("Asignación de Producto a Vendedor");                
                                        
                                        try{
                                            movimientoDto.setFechaRegistro(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                                        }catch(Exception e){
                                            movimientoDto.setFechaRegistro(new Date());
                                        }
                                        movimientoDto.setIdConcepto(conceptoPadre.getIdConcepto());
                                        movimientoDto.setIdEmpleado(idEmpleado); 
                                        movimientoDto.setContabilidadPeso(pesoTotalGranelAsignar);

                                        //Insert
                                        movimientosDaoImpl.insert(movimientoDto);

                                    }catch(Exception e){                                    
                                    }

                                }
                                
                                if(numArticulosInventarioAsignar > 0 && !prodExistenteEmpleado){//si se repartio cantidad de almacen la restamos del almacen
                                    //actualizamos el inventario del almacen:
                                    BigDecimal numArticulosDisponibles = (new BigDecimal(almPrincipal!=null?almPrincipal.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                                    BigDecimal numArtInventarioAsignar = (new BigDecimal(numArticulosInventarioAsignar)).setScale(2, RoundingMode.HALF_UP);
                                    BigDecimal stockTotal = numArticulosDisponibles.subtract(numArtInventarioAsignar);

                                    almPrincipal.setExistencia(stockTotal.doubleValue() );
                                    
                                    if (pesoTotalGranelAsignar>0){
                                        BigDecimal pesoDisponibles = (new BigDecimal(almPrincipal!=null?almPrincipal.getExistenciaPeso():0)).setScale(2, RoundingMode.HALF_UP);
                                        BigDecimal contaPesoAsignar = (new BigDecimal(pesoTotalGranelAsignar)).setScale(2, RoundingMode.HALF_UP);
                                        BigDecimal stockPesoTotal = pesoDisponibles.subtract(contaPesoAsignar);
                                        almPrincipal.setExistenciaPeso(stockPesoTotal.doubleValue() );
                                    }
                                    
                                    new ExistenciaAlmacenBO(user.getConn()).updateBD(almPrincipal);
                                    //new ConceptoDaoImpl(user.getConn()).update(concepto.createPk(), concepto);
                                }
                            }else{//si no hay articulos suficientes para asignar mandamos mensaje de alerta:
                                out.print("<!--ERROR-->La cantidad solicitada es mayor a la que existe en Almacen.");
                                if (pesoTotalGranelAsignar>0)
                                    out.print("<br/>O el Peso Neto solicitado es mayor a lo que existe en Almacen.");
                            }
                        }else if(numArticulosInventarioAsignar <= 0){//si es esta regresando mercancia
                            out.print("<!--ERROR-->La cantidad debe ser mayor a 0.");

                        }
                    }else{
                        out.print("<!--ERROR-->Seleccione un Concepto.");
                    }

                }else{
                    out.print("<!--ERROR-->Seleccione un Almacén.");
                }
            }
            //out.print("<!--EXITO-->Inventario asignado satisfactoriamente");
            out.print("<!--EXITO-->Continuar");
        }
    }else if(mode.equals("devolverInventarioGranel"))  { 
        
        System.out.println("_________DEVOLVIENDO INVENTARIO GRANEL");
        int idSubProducto = -1;
        int idAlmacenDestino = -1;
        boolean nuevo = false;
        double numArticulosInventarioAsignar = 0;
        double pesoArticulosInventarioAsignar = 0;
        double pesoTotalGranelAsignar = 0;
        try{
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
        }catch(NumberFormatException ex){}
        try{
            idSubProducto = Integer.parseInt(request.getParameter("idConceptoInventario"));
        }catch(NumberFormatException ex){}
        try{
            idAlmacenDestino = Integer.parseInt(request.getParameter("idAlmacen"));
        }catch(NumberFormatException ex){}
        try{
            numArticulosInventarioAsignar = Double.parseDouble(request.getParameter("numArticulosInventarioAsignar"));
        }catch(NumberFormatException ex){}
        try{
            pesoArticulosInventarioAsignar = Double.parseDouble(request.getParameter("pesoArticulosInventarioAsignar"));
        }catch(NumberFormatException ex){}catch(Exception e){}
        try{
            pesoTotalGranelAsignar = numArticulosInventarioAsignar * pesoArticulosInventarioAsignar;
        }catch(Exception e){}
        
        if (idAlmacenDestino <= 0)
            msgError+="<ul>Debe seleccionar un almácen al cual regresara el Producto";
        if (idSubProducto <= 0)
            msgError+="<ul>Debe seleccionar un SubProducto de tipo Granel, para realizar la acción de Devolución a Almácen de Produto Padre.";
        if(numArticulosInventarioAsignar <= 0)
            msgError+="<ul>El dato 'Piezas (Unidad) a Devolver' es obligatorio. En él, debe indicar la cantidad de piezas a devolver del mismo peso exacto, incluso si no estan completos debe indicar numeros enteros.";
        if(pesoArticulosInventarioAsignar <= 0)
            msgError+="<ul>El dato 'Peso de cada Pieza a Devolver (kg)' es obligatorio. En él, debe indicar el peso Unitario de cada pieza a devolver, si son de distintos pesos deberá repetir varias veces el procedimiento. ";
        
        if (!msgError.equals("")){
            out.print("<!--ERROR-->" + msgError); 
        }else{
            Concepto conceptoSubProducto = new ConceptoBO(idSubProducto, user.getConn()).getConcepto();
            Concepto conceptoProductoPadre = new ConceptoBO(conceptoSubProducto.getIdConceptoPadre(), user.getConn()).getConcepto();

            ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(user.getConn()); 
            ExistenciaAlmacen existenciaAlmacenProductoPadre = exisAlmBO.getExistenciaProductoAlmacen(idAlmacenDestino, conceptoProductoPadre.getIdConcepto());                

            System.out.println("____CONCEPTO A BUSCAR: "+idSubProducto+", cantidad actual producto padre(almacén): "+ (existenciaAlmacenProductoPadre!=null?existenciaAlmacenProductoPadre.getExistencia():0) +", cantidad solicitada: "+numArticulosInventarioAsignar);
            System.out.println("___________________________, Peso stock actual producto padre(almacén): "+ (existenciaAlmacenProductoPadre!=null?existenciaAlmacenProductoPadre.getExistenciaPeso():0) +", Peso solicitado: "+pesoTotalGranelAsignar);

            //verificamos si existe en inventario de Empleado repartidor el articulo
            EmpleadoInventarioRepartidor[] inventarios = new EmpleadoInventarioRepartidorDaoImpl(user.getConn()).findByDynamicWhere("ID_EMPLEADO = "+idEmpleado+" AND ID_CONCEPTO = "+idSubProducto, null);
            if(inventarios.length > 0){
                EmpleadoInventarioRepartidor inventarioEmpleado = inventarios[0];
                if(inventarioEmpleado.getIdEstatus() == 1){
                    
                    if ( pesoTotalGranelAsignar <= inventarioEmpleado.getExistenciaGranel()  ){
                        
                        // En stock de empleado, se maneja el producto de acuerdo al peso específico por pieza
                        // pero al retornarlo a almácen, no se regresara a ese sub-producto, si no que,
                        // se sumara al producto padre (original) en el almacen seleccionado por el usuario.
                        // Y al regresarlo no importa el "peso unitario" de esas piezas, si no el peso total.
                        // Por ello, es necesario hacer ciertas equivalencias al restar de Inventario Repartidor
                        
                        //Actualizamos Stock de Empleado Repartidor -> Subproducto Granel
                        {
                            //no. piezas = peso total / peso unitario
                            double equivalentePiezasSubProducto = new BigDecimal(pesoTotalGranelAsignar).divide(new BigDecimal(inventarioEmpleado.getPeso()), 2, RoundingMode.HALF_UP).doubleValue();
                            
                            inventarioEmpleado.setIdEstatus(1);
                            inventarioEmpleado.setCantidad( inventarioEmpleado.getCantidad() - equivalentePiezasSubProducto ) ;
                            inventarioEmpleado.setExistenciaGranel(inventarioEmpleado.getExistenciaGranel() - pesoTotalGranelAsignar);
                            new EmpleadoInventarioRepartidorDaoImpl(user.getConn()).update(inventarioEmpleado.createPk(), inventarioEmpleado);
                        }

                        //Actualizamos stock de Almacen seleccionado para devolver a Producto Padre
                        BigDecimal numArticulosDisponibles = (new BigDecimal(existenciaAlmacenProductoPadre!=null?existenciaAlmacenProductoPadre.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal numArtInventarioAsignar = (new BigDecimal(numArticulosInventarioAsignar)).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal stockTotal = numArticulosDisponibles.add(numArtInventarioAsignar);

                        BigDecimal pesoDisponibles = (new BigDecimal(existenciaAlmacenProductoPadre!=null?existenciaAlmacenProductoPadre.getExistenciaPeso():0)).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal contaPesoAsignar = (new BigDecimal(pesoTotalGranelAsignar)).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal stockPesoTotal = pesoDisponibles.add(contaPesoAsignar);
                        

                        if(existenciaAlmacenProductoPadre==null){
                            nuevo = true;
                            existenciaAlmacenProductoPadre = new ExistenciaAlmacen();
                            existenciaAlmacenProductoPadre.setIdAlmacen(idAlmacenDestino);
                            existenciaAlmacenProductoPadre.setIdConcepto(conceptoProductoPadre.getIdConcepto());
                        }
                        
                        existenciaAlmacenProductoPadre.setEstatus(1);
                        existenciaAlmacenProductoPadre.setExistencia(stockTotal.doubleValue());
                        existenciaAlmacenProductoPadre.setExistenciaPeso(stockPesoTotal.doubleValue() );

                        if(nuevo){
                            new ExistenciaAlmacenDaoImpl().insert(existenciaAlmacenProductoPadre); 
                        }else{
                            new ExistenciaAlmacenBO(user.getConn()).updateBD(existenciaAlmacenProductoPadre);
                        }

                        //Registramos movimiento de almacen
                        try{
                            Movimiento movimientoDto = new Movimiento(); 
                            MovimientoDaoImpl movimientosDaoImpl = new MovimientoDaoImpl(user.getConn());

                            movimientoDto.setIdEmpresa(idEmpresa);
                            movimientoDto.setTipoMovimiento("ENTRADA");
                            movimientoDto.setNombreProducto(conceptoProductoPadre.getNombreDesencriptado());
                            movimientoDto.setContabilidad(numArticulosInventarioAsignar);
                            movimientoDto.setIdProveedor(-1);
                            movimientoDto.setOrdenCompra("");
                            movimientoDto.setNumeroGuia("");                             
                            movimientoDto.setIdAlmacen(idAlmacenDestino);                
                            movimientoDto.setConceptoMovimiento("Retorno de Producto GRANEL desde Vendedor");                
                            
                            try{
                                movimientoDto.setFechaRegistro(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime());
                            }catch(Exception e){
                                movimientoDto.setFechaRegistro(new Date());
                            }
                            movimientoDto.setIdConcepto(conceptoProductoPadre.getIdConcepto());
                            movimientoDto.setIdEmpleado(idEmpleado);
                            movimientoDto.setContabilidadPeso(pesoTotalGranelAsignar);

                            //Insert
                            movimientosDaoImpl.insert(movimientoDto);

                        }catch(Exception e){                                    
                        }

                        out.print("<!--EXITO-->Inventario actualizado satisfactoriamente");

                    }else{
                         out.print("<!--ERROR-->No puede regresar mayor Peso Total (cantidad X peso unitario) de lo que tiene asignado.");
                    }
                    
                }else{//Si esta inactivo:                        
                    out.print("<!--ERROR-->Concepto no Activo en inventario de Repartidor");
                }
            }else{
                out.print("<!--ERROR-->Concepto no Existente en inventario de Repartidor");
            }

        }
        
    }else if(mode.equals("creaInputs")){
        int idConcepto = 0;
        try{
            idConcepto = Integer.parseInt(request.getParameter("idConcep"));
        }catch(NumberFormatException ex){ }
        int idAlmacen = 0;
        try{
            idAlmacen = Integer.parseInt(request.getParameter("idAlmacen"));
        }catch(NumberFormatException ex){ }
        
        Concepto concepto = new ConceptoBO(idConcepto, user.getConn()).getConcepto();
        
        ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(user.getConn()); 
        ExistenciaAlmacen almPrincipal = exisAlmBO.getExistenciaProductoAlmacen(idAlmacen, concepto.getIdConcepto());
                
        out.print("<br><p>"
                + "<label>Código:</label><br/>"
                + "<input type='text' maxlength='100' id='sku' name='sku' value='"+concepto.getIdentificacion()+"' />"
                + "</p><br>"
                + "<p>"
                + "<label>Disponibles:</label><br/>"
                + "<input type='text' maxlength='100' id='diponibles' name='disponibles' value='"+ (almPrincipal!=null?almPrincipal.getExistencia():0) +"' />"
                + "</p>"
                );   
    
    }else if(mode.equals("agendaNuevaTarea")){
        
        
        try{
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
        }catch(NumberFormatException ex){}
        
        try{
            idCliente = Integer.parseInt(request.getParameter("idCliente"));
        }catch(NumberFormatException ex){}
        
        int estatusAgenda = 1; //Activa por default
        /*try{
            estatusAgenda = Integer.parseInt(request.getParameter("estatusAgenda"));
        }catch(NumberFormatException ex){}*/        
        
        String nombreTareaEmpleadoAgenda = request.getParameter("nombreTareaEmpleadoAgenda")!=null?new String(request.getParameter("nombreTareaEmpleadoAgenda").getBytes("ISO-8859-1"),"UTF-8"):"";    
        String descripcionEmpleadoAgenda = request.getParameter("descripcionEmpleadoAgenda")!=null?new String(request.getParameter("descripcionEmpleadoAgenda").getBytes("ISO-8859-1"),"UTF-8"):"";    
        Date agenda_fecha_realiza = null;
        try{ agenda_fecha_realiza = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("agenda_fecha_realiza"));}catch(Exception e){}
        
        
        if(nombreTareaEmpleadoAgenda.length()<=0)
            msgError += "<ul>El dato 'Nombre' es requerido";
        if(descripcionEmpleadoAgenda.length()<=0)
            msgError += "<ul>El dato 'Descripción' es requerido";
        if(agenda_fecha_realiza == null)
            msgError += "<ul>El dato 'Fecha' es requerido";
        
        if(msgError.equals("")){
        
            EmpleadoAgenda emAgenda = new EmpleadoAgenda();
            emAgenda.setIdEmpleado(idEmpleado);
            emAgenda.setIdEstatus(estatusAgenda);
            emAgenda.setFechaCreacion(new Date());
            emAgenda.setFechaProgramada(agenda_fecha_realiza);
            emAgenda.setFechaEjecucion(null);
            emAgenda.setNombreTarea(nombreTareaEmpleadoAgenda);
            emAgenda.setDescripcionTarea(descripcionEmpleadoAgenda);
            if(idCliente > 0){
                emAgenda.setIdCliente(idCliente);
            }
            
            try{
                new EmpleadoAgendaDaoImpl(user.getConn()).insert(emAgenda);
                out.print("<!--EXITO-->Tarea agregada satisfactoriamente");
            }catch(Exception e){
                out.print("<!--ERROR-->No se puede almacenar la tarea.");
            }
            
        
        }else{
            out.print("<!--ERROR-->"+msgError);
        }
            
        
    
    }else if(mode.equals("agendaBorrarTarea")){
        int idAgenda = -1;
        try{
            idAgenda = Integer.parseInt(request.getParameter("idAgenda"));
        }catch(NumberFormatException ex){}
        
        EmpleadoAgenda emAgenda = new EmpleadoAgendaBO(idAgenda, user.getConn()).getEmpleadoAgenda();
        emAgenda.setIdEstatus(2);
        try{
            new EmpleadoAgendaDaoImpl(user.getConn()).update(emAgenda.createPk(), emAgenda);
            out.print("<!--EXITO-->Tarea eliminada satisfactoriamente");
        }catch(Exception e){
            out.print("<!--ERROR-->No se puede eliminar la tarea.");
        }
    }else if(mode.equals("inventarioInicial")){
        
        try{
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
       }catch(NumberFormatException ex){}
        
        
        // 26/02/2016: Cambiamos todo el código
        // por un método para que sea mas legible y reutilizable para Web Service
        InventarioInicialVendedorBO inventarioInicialVendedorBO = new InventarioInicialVendedorBO(user.getConn());
        try{
            inventarioInicialVendedorBO.registraInventarioInicial(idEmpleado, idEmpresa);
            out.print("<!--EXITO-->Inventario Inicial registrado satisfactoriamente.");
        }catch(Exception ex){
            out.print("<!--ERROR-->" + ex.toString());
        }
    
    }else if(mode.equals("activarAdicionInvInicial")){
        
        modoAdicionInventarioInicial = true;
        session.setAttribute("modoAdicionInventarioInicial", modoAdicionInventarioInicial);
        out.print("<!--EXITO-->Modo Adición a Inventario Inicial, INICIADO. <br/> Todo lo que agregue a inventario de Empleado en este modo, se agregara tambien al Inventario Inicial existente.");
        
    }else if(mode.equals("desactivarAdicionInvInicial")){
        
        //quitamos variable de sesion para terminar Modo de Adicion
        session.setAttribute("modoAdicionInventarioInicial", false);
        session.removeAttribute("modoAdicionInventarioInicial");
        out.print("<!--EXITO-->Modo Adición a Inventario Inicial, FINALIZADO.");
        
    }else if(mode.equals("compartirAgenda")){
        
        Date fechaLimite = null;
        int idEmpleadoOrigen = -1;
        int idEmpleadoDestino = -1;
        
        String domingo = request.getParameter("domingo")!=null?new String(request.getParameter("domingo").getBytes("ISO-8859-1"),"UTF-8"):""; 
        String lunes = request.getParameter("lunes")!=null?new String(request.getParameter("lunes").getBytes("ISO-8859-1"),"UTF-8"):""; 
        String martes = request.getParameter("martes")!=null?new String(request.getParameter("martes").getBytes("ISO-8859-1"),"UTF-8"):""; 
        String miercoles = request.getParameter("miercoles")!=null?new String(request.getParameter("miercoles").getBytes("ISO-8859-1"),"UTF-8"):""; 
        String jueves = request.getParameter("jueves")!=null?new String(request.getParameter("jueves").getBytes("ISO-8859-1"),"UTF-8"):""; 
        String viernes = request.getParameter("viernes")!=null?new String(request.getParameter("viernes").getBytes("ISO-8859-1"),"UTF-8"):""; 
        String sabado = request.getParameter("sabado")!=null?new String(request.getParameter("sabado").getBytes("ISO-8859-1"),"UTF-8"):"";     
        String tipoAsignacion = request.getParameter("tipoAsignacion")!=null?new String(request.getParameter("tipoAsignacion").getBytes("ISO-8859-1"),"UTF-8"):"";     
        
        
        try{
            fechaLimite = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("q_fh_min"));            
        }catch(Exception e){}
        
        try{
            idEmpleadoDestino = Integer.parseInt(request.getParameter("q_idvendedorDestino"));
        }catch(NumberFormatException ex){}
        
        try{
            idEmpleadoOrigen = Integer.parseInt(request.getParameter("idEmpleadoOrigen"));
        }catch(NumberFormatException ex){}
        
        if(idEmpleadoDestino<0)
            msgError += "<ul>El dato 'vendedor' es requerido";
        if(fechaLimite == null)
            msgError += "<ul>El dato 'fecha limite' es requerido";      
        if(domingo.equals("")&&lunes.equals("")&&martes.equals("")&&miercoles.equals("")&&jueves.equals("")&&viernes.equals("")&&tipoAsignacion.equals(""))
            msgError += "<ul>Selecciona la opción de clientes para compartir";        
        if(idEmpleadoOrigen<0)
            msgError += "<ul>El dato 'vendedor de origen' es requerido";
                
        if(msgError.equals("")){        
                 
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            
            
            ClienteBO clienteBO = new ClienteBO(user.getConn());               
            Cliente[] clientesDto = null;
            String filtro ="";
            
            EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
            if(tipoAsignacion.equals("CLIENTES")){
                domingo = "DOM";
                lunes = "LUN";
                martes = "MAR";
                miercoles = "MIE";
                jueves = "JUE";
                viernes = "VIE";
                sabado = "SAB";
            }        
                    
                    if(domingo.trim().equals("DOM")){                        
                        filtro = " AND  ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR="+ idEmpleadoOrigen +")"
                                + " AND DIAS_VISITA LIKE '%DOM%'";
                        clientesDto = clienteBO.findClientes(-1, idEmpresa, -1, -1, filtro);
                        for(Cliente item: clientesDto){
                            //Relacion con vendedor

                                    SGClienteVendedorBO clienteVendedorBO = new SGClienteVendedorBO(item.getIdCliente(),user.getConn());
                                    SgfensClienteVendedor clienteVendedorDto = clienteVendedorBO.getClienteVendedor();
                                    // actualizamos registro
                                    if(clienteVendedorDto!=null){
                                    clienteVendedorDto.setIdUsuarioVendedorReasignado(idEmpleadoDestino);
                                        if(fechaLimite!=null){
                                            clienteVendedorDto.setFechaLimiteReasigancion(fechaLimite);
                                        }else{
                                            clienteVendedorDto.setFechaLimiteReasigancion(new Date());
                                        }

                                        new SgfensClienteVendedorDaoImpl(user.getConn()).update(clienteVendedorDto.createPk(), clienteVendedorDto);                        
                                    }    
                                    
                                    //Creamos tareas para cada cliente 
                                    
                                    Empleado empleadoTarea =  empleadoBO.findEmpleadoByUsuario(idEmpleadoDestino);
                                    String nombreCliente = item.getNombreCliente()!=null?item.getNombreCliente():"" + " " + item.getApellidoPaternoCliente()!=null?item.getApellidoPaternoCliente():"" + " " + item.getApellidoMaternoCliente()!=null?item.getApellidoMaternoCliente():"";                                    
                                    
                                    EmpleadoAgendaDaoImpl empleadoAgendaDao = new EmpleadoAgendaDaoImpl();
                                    EmpleadoAgenda[] emAgendas = empleadoAgendaDao.findByDynamicWhere(" ID_ESTATUS <> 2 AND ID_CLIENTE = " + item.getIdCliente() + " AND FECHA_PROGRAMADA = '" + df.format(new Date()) + "'", null);
                                    
                                    Date fechaProg = null;                                    
                                    Calendar c = Calendar.getInstance(); //OBTENGO LA FECHA DE HOY 
                                    Date hoy = c.getTime();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String date = sdf.format(hoy); 
                                    hoy = sdf.parse(date);
                                    
                                    if(!fechaLimite.before(hoy)&&hoy.getDay() == 0){  
                                         
                                        if(emAgendas.length == 0){
                                            EmpleadoAgenda emAgenda = new EmpleadoAgenda();
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());


                                            try{
                                                new EmpleadoAgendaDaoImpl(user.getConn()).insert(emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                        }else{                                       

                                            EmpleadoAgenda emAgenda = emAgendas[0];
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());

                                            try{
                                                new EmpleadoAgendaDaoImpl(user.getConn()).update(emAgenda.createPk(),emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }

                                        }
                                    }

                                    
                        }
                    }   
                    if(lunes.trim().equals("LUN")){
                       filtro = " AND  ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR="+ idEmpleadoOrigen +")"
                                + " AND DIAS_VISITA LIKE '%LUN%'";
                        clientesDto = clienteBO.findClientes(-1, idEmpresa, -1, -1, filtro);
                        for(Cliente item: clientesDto){
                            //Relacion con vendedor

                                    SGClienteVendedorBO clienteVendedorBO = new SGClienteVendedorBO(item.getIdCliente(),user.getConn());
                                    SgfensClienteVendedor clienteVendedorDto = clienteVendedorBO.getClienteVendedor();
                                    // actualizamos registro
                                    if(clienteVendedorDto!=null){
                                    clienteVendedorDto.setIdUsuarioVendedorReasignado(idEmpleadoDestino);
                                        if(fechaLimite!=null){
                                            clienteVendedorDto.setFechaLimiteReasigancion(fechaLimite);
                                        }else{
                                            clienteVendedorDto.setFechaLimiteReasigancion(new Date());
                                        }

                                        new SgfensClienteVendedorDaoImpl(user.getConn()).update(clienteVendedorDto.createPk(), clienteVendedorDto);                        
                                    }     
                                    
                                    
                                    //Creamos tareas para cada cliente 
                                    
                                    Empleado empleadoTarea =  empleadoBO.findEmpleadoByUsuario(idEmpleadoDestino);
                                    String nombreCliente = item.getNombreCliente()!=null?item.getNombreCliente():"" + " " + item.getApellidoPaternoCliente()!=null?item.getApellidoPaternoCliente():"" + " " + item.getApellidoMaternoCliente()!=null?item.getApellidoMaternoCliente():"";                                    
                                    
                                    EmpleadoAgendaDaoImpl empleadoAgendaDao = new EmpleadoAgendaDaoImpl();
                                    EmpleadoAgenda[] emAgendas = empleadoAgendaDao.findByDynamicWhere(" ID_ESTATUS <> 2 AND ID_CLIENTE = " + item.getIdCliente() + " AND FECHA_PROGRAMADA = '" + df.format(new Date()) + "'", null);
                                    
                                    Date fechaProg = null;                                    
                                    Calendar c = Calendar.getInstance(); //OBTENGO LA FECHA DE HOY 
                                    Date hoy = c.getTime();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String date = sdf.format(hoy); 
                                    hoy = sdf.parse(date);
                                    
                                    if(!fechaLimite.before(hoy)&&hoy.getDay() == 1){  
                                
                                        if(emAgendas.length == 0){
                                            EmpleadoAgenda emAgenda = new EmpleadoAgenda();
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());


                                            try{
                                                new EmpleadoAgendaDaoImpl(user.getConn()).insert(emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                        }else{                                       

                                            EmpleadoAgenda emAgenda = emAgendas[0];
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());

                                            try{
                                                new EmpleadoAgendaDaoImpl(user.getConn()).update(emAgenda.createPk(),emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                        }
                    }  
                    if(martes.trim().equals("MAR")){
                        filtro = " AND  ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR="+ idEmpleadoOrigen +")"
                                + " AND DIAS_VISITA LIKE '%MAR%'";
                        clientesDto = clienteBO.findClientes(-1, idEmpresa, -1, -1, filtro);
                        for(Cliente item: clientesDto){
                            //Relacion con vendedor

                                    SGClienteVendedorBO clienteVendedorBO = new SGClienteVendedorBO(item.getIdCliente(),user.getConn());
                                    SgfensClienteVendedor clienteVendedorDto = clienteVendedorBO.getClienteVendedor();
                                    // actualizamos registro
                                    if(clienteVendedorDto!=null){
                                    clienteVendedorDto.setIdUsuarioVendedorReasignado(idEmpleadoDestino);
                                        if(fechaLimite!=null){
                                            clienteVendedorDto.setFechaLimiteReasigancion(fechaLimite);
                                        }else{
                                            clienteVendedorDto.setFechaLimiteReasigancion(new Date());
                                        }

                                        new SgfensClienteVendedorDaoImpl(user.getConn()).update(clienteVendedorDto.createPk(), clienteVendedorDto);                        
                                    }    
                                    
                                    //Creamos tareas para cada cliente 
                                    
                                    Empleado empleadoTarea =  empleadoBO.findEmpleadoByUsuario(idEmpleadoDestino);
                                    String nombreCliente = item.getNombreCliente()!=null?item.getNombreCliente():"" + " " + item.getApellidoPaternoCliente()!=null?item.getApellidoPaternoCliente():"" + " " + item.getApellidoMaternoCliente()!=null?item.getApellidoMaternoCliente():"";                                    
                                    
                                    EmpleadoAgendaDaoImpl empleadoAgendaDao = new EmpleadoAgendaDaoImpl();
                                    EmpleadoAgenda[] emAgendas = empleadoAgendaDao.findByDynamicWhere(" ID_ESTATUS <> 2 AND ID_CLIENTE = " + item.getIdCliente() + " AND FECHA_PROGRAMADA = '" + df.format(new Date()) + "'", null);
                                    
                                    Date fechaProg = null;                                    
                                    Calendar c = Calendar.getInstance(); //OBTENGO LA FECHA DE HOY 
                                    Date hoy = c.getTime();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String date = sdf.format(hoy); 
                                    hoy = sdf.parse(date);
                                    
                                    if(!fechaLimite.before(hoy)&&hoy.getDay() == 2){  
                                        
                                        if(emAgendas.length == 0){
                                            EmpleadoAgenda emAgenda = new EmpleadoAgenda();
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());


                                            try{
                                                new EmpleadoAgendaDaoImpl(user.getConn()).insert(emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                        }else{                                       

                                            EmpleadoAgenda emAgenda = emAgendas[0];
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());

                                            try{
                                                new EmpleadoAgendaDaoImpl(user.getConn()).update(emAgenda.createPk(),emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                    
                        }
                    }           
                    if(miercoles.trim().equals("MIE")){
                       filtro = " AND  ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR="+ idEmpleadoOrigen +")"
                                + " AND DIAS_VISITA LIKE '%MIE%'";
                        clientesDto = clienteBO.findClientes(-1, idEmpresa, -1, -1, filtro);
                        for(Cliente item: clientesDto){
                            //Relacion con vendedor

                                    SGClienteVendedorBO clienteVendedorBO = new SGClienteVendedorBO(item.getIdCliente(),user.getConn());
                                    SgfensClienteVendedor clienteVendedorDto = clienteVendedorBO.getClienteVendedor();
                                    // actualizamos registro
                                    if(clienteVendedorDto!=null){
                                    clienteVendedorDto.setIdUsuarioVendedorReasignado(idEmpleadoDestino);
                                        if(fechaLimite!=null){
                                            clienteVendedorDto.setFechaLimiteReasigancion(fechaLimite);
                                        }else{
                                            clienteVendedorDto.setFechaLimiteReasigancion(new Date());
                                        }

                                        new SgfensClienteVendedorDaoImpl(user.getConn()).update(clienteVendedorDto.createPk(), clienteVendedorDto);                        
                                    }     
                                    
                                    //Creamos tareas para cada cliente 
                                    
                                    Empleado empleadoTarea =  empleadoBO.findEmpleadoByUsuario(idEmpleadoDestino);
                                    String nombreCliente = item.getNombreCliente()!=null?item.getNombreCliente():"" + " " + item.getApellidoPaternoCliente()!=null?item.getApellidoPaternoCliente():"" + " " + item.getApellidoMaternoCliente()!=null?item.getApellidoMaternoCliente():"";                                    
                                    
                                    EmpleadoAgendaDaoImpl empleadoAgendaDao = new EmpleadoAgendaDaoImpl();
                                    EmpleadoAgenda[] emAgendas = empleadoAgendaDao.findByDynamicWhere(" ID_ESTATUS <> 2 AND ID_CLIENTE = " + item.getIdCliente() + " AND FECHA_PROGRAMADA = '" + df.format(new Date()) + "'", null);
                                    
                                    Date fechaProg = null;                                    
                                    Calendar c = Calendar.getInstance(); //OBTENGO LA FECHA DE HOY 
                                    Date hoy = c.getTime();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String date = sdf.format(hoy); 
                                    hoy = sdf.parse(date);
                                    
                                    if(!fechaLimite.before(hoy)&&hoy.getDay() == 3){  
                                        
                                        if(emAgendas.length == 0){
                                            EmpleadoAgenda emAgenda = new EmpleadoAgenda();
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());


                                            try{
                                                new EmpleadoAgendaDaoImpl(user.getConn()).insert(emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                        }else{                                       

                                            EmpleadoAgenda emAgenda = emAgendas[0];
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());

                                            try{
                                                new EmpleadoAgendaDaoImpl(user.getConn()).update(emAgenda.createPk(),emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                    
                        }
                    }            
                    if(jueves.trim().equals("JUE")){
                       filtro = " AND  ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR="+ idEmpleadoOrigen +")"
                                + " AND DIAS_VISITA LIKE '%JUE%'";
                        clientesDto = clienteBO.findClientes(-1, idEmpresa, -1, -1, filtro);
                        for(Cliente item: clientesDto){
                            //Relacion con vendedor

                                    SGClienteVendedorBO clienteVendedorBO = new SGClienteVendedorBO(item.getIdCliente(),user.getConn());
                                    SgfensClienteVendedor clienteVendedorDto = clienteVendedorBO.getClienteVendedor();
                                    // actualizamos registro
                                    if(clienteVendedorDto!=null){
                                    clienteVendedorDto.setIdUsuarioVendedorReasignado(idEmpleadoDestino);
                                        if(fechaLimite!=null){
                                            clienteVendedorDto.setFechaLimiteReasigancion(fechaLimite);
                                        }else{
                                            clienteVendedorDto.setFechaLimiteReasigancion(new Date());
                                        }

                                        new SgfensClienteVendedorDaoImpl(user.getConn()).update(clienteVendedorDto.createPk(), clienteVendedorDto);                        
                                    }    
                                    
                                    //Creamos tareas para cada cliente 
                                    
                                    Empleado empleadoTarea =  empleadoBO.findEmpleadoByUsuario(idEmpleadoDestino);
                                    String nombreCliente = item.getNombreCliente()!=null?item.getNombreCliente():"" + " " + item.getApellidoPaternoCliente()!=null?item.getApellidoPaternoCliente():"" + " " + item.getApellidoMaternoCliente()!=null?item.getApellidoMaternoCliente():"";                                    
                                    
                                    EmpleadoAgendaDaoImpl empleadoAgendaDao = new EmpleadoAgendaDaoImpl();
                                    EmpleadoAgenda[] emAgendas = empleadoAgendaDao.findByDynamicWhere(" ID_ESTATUS <> 2 AND ID_CLIENTE = " + item.getIdCliente() + " AND FECHA_PROGRAMADA = '" + df.format(new Date()) + "'", null);
                                    
                                    Date fechaProg = null;                                    
                                    Calendar c = Calendar.getInstance(); //OBTENGO LA FECHA DE HOY 
                                    Date hoy = c.getTime();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String date = sdf.format(hoy); 
                                    hoy = sdf.parse(date);
                                    
                                    if(!fechaLimite.before(hoy)&&hoy.getDay() == 4){  
                                        
                                        if(emAgendas.length == 0){
                                            EmpleadoAgenda emAgenda = new EmpleadoAgenda();
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());


                                            try{
                                                new EmpleadoAgendaDaoImpl(user.getConn()).insert(emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                        }else{                                       

                                            EmpleadoAgenda emAgenda = emAgendas[0];
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());

                                            try{
                                                new EmpleadoAgendaDaoImpl(user.getConn()).update(emAgenda.createPk(),emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                    
                        }
                    }            
                    if(viernes.trim().equals("VIE")){
                       filtro = " AND  ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR="+ idEmpleadoOrigen +")"
                                + " AND DIAS_VISITA LIKE '%VIE%'";
                        clientesDto = clienteBO.findClientes(-1, idEmpresa, -1, -1, filtro);
                        for(Cliente item: clientesDto){
                            //Relacion con vendedor

                                    SGClienteVendedorBO clienteVendedorBO = new SGClienteVendedorBO(item.getIdCliente(),user.getConn());
                                    SgfensClienteVendedor clienteVendedorDto = clienteVendedorBO.getClienteVendedor();
                                    // actualizamos registro
                                    if(clienteVendedorDto!=null){
                                    clienteVendedorDto.setIdUsuarioVendedorReasignado(idEmpleadoDestino);
                                        if(fechaLimite!=null){
                                            clienteVendedorDto.setFechaLimiteReasigancion(fechaLimite);
                                        }else{
                                            clienteVendedorDto.setFechaLimiteReasigancion(new Date());
                                        }

                                        new SgfensClienteVendedorDaoImpl(user.getConn()).update(clienteVendedorDto.createPk(), clienteVendedorDto);                        
                                    }    
                                    
                                    
                                    //Creamos tareas para cada cliente 
                                    
                                    Empleado empleadoTarea =  empleadoBO.findEmpleadoByUsuario(idEmpleadoDestino);
                                    String nombreCliente = item.getNombreCliente()!=null?item.getNombreCliente():"" + " " + item.getApellidoPaternoCliente()!=null?item.getApellidoPaternoCliente():"" + " " + item.getApellidoMaternoCliente()!=null?item.getApellidoMaternoCliente():"";                                    
                                    
                                    EmpleadoAgendaDaoImpl empleadoAgendaDao = new EmpleadoAgendaDaoImpl();
                                    EmpleadoAgenda[] emAgendas = empleadoAgendaDao.findByDynamicWhere(" ID_ESTATUS <> 2 AND ID_CLIENTE = " + item.getIdCliente() + " AND FECHA_PROGRAMADA = '" + df.format(new Date()) + "'", null);
                                    
                                    Date fechaProg = null;                                    
                                    Calendar c = Calendar.getInstance(); //OBTENGO LA FECHA DE HOY 
                                    Date hoy = c.getTime();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String date = sdf.format(hoy); 
                                    hoy = sdf.parse(date);
                                    
                                    if(!fechaLimite.before(hoy)&&hoy.getDay() == 5){  
                                        
                                        if(emAgendas.length == 0){
                                            EmpleadoAgenda emAgenda = new EmpleadoAgenda();
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());


                                            try{
                                                new EmpleadoAgendaDaoImpl(user.getConn()).insert(emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                        }else{                                       

                                            EmpleadoAgenda emAgenda = emAgendas[0];
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());

                                            try{
                                                new EmpleadoAgendaDaoImpl(user.getConn()).update(emAgenda.createPk(),emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                    
                        }
                    }            
                    if(sabado.trim().equals("SAB")){
                        filtro = " AND  ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR="+ idEmpleadoOrigen +")"
                                + " AND DIAS_VISITA LIKE '%SAB%'";
                        clientesDto = clienteBO.findClientes(-1, idEmpresa, -1, -1, filtro);
                        for(Cliente item: clientesDto){
                            //Relacion con vendedor

                                    SGClienteVendedorBO clienteVendedorBO = new SGClienteVendedorBO(item.getIdCliente(),user.getConn());
                                    SgfensClienteVendedor clienteVendedorDto = clienteVendedorBO.getClienteVendedor();
                                    // actualizamos registro
                                    if(clienteVendedorDto!=null){
                                    clienteVendedorDto.setIdUsuarioVendedorReasignado(idEmpleadoDestino);
                                        if(fechaLimite!=null){
                                            clienteVendedorDto.setFechaLimiteReasigancion(fechaLimite);
                                        }else{
                                            clienteVendedorDto.setFechaLimiteReasigancion(new Date());
                                        }

                                        new SgfensClienteVendedorDaoImpl(user.getConn()).update(clienteVendedorDto.createPk(), clienteVendedorDto);                        
                                    }    
                                    
                                    
                                    //Creamos tareas para cada cliente 
                                    
                                    Empleado empleadoTarea =  empleadoBO.findEmpleadoByUsuario(idEmpleadoDestino);
                                    String nombreCliente = item.getNombreCliente()!=null?item.getNombreCliente():"" + " " + item.getApellidoPaternoCliente()!=null?item.getApellidoPaternoCliente():"" + " " + item.getApellidoMaternoCliente()!=null?item.getApellidoMaternoCliente():"";                                    
                                    
                                    EmpleadoAgendaDaoImpl empleadoAgendaDao = new EmpleadoAgendaDaoImpl();
                                    EmpleadoAgenda[] emAgendas = empleadoAgendaDao.findByDynamicWhere(" ID_ESTATUS <> 2 AND ID_CLIENTE = " + item.getIdCliente() + " AND FECHA_PROGRAMADA = '" + df.format(new Date()) + "'", null);
                                    
                                    Date fechaProg = null;                                    
                                    Calendar c = Calendar.getInstance(); //OBTENGO LA FECHA DE HOY 
                                    Date hoy = c.getTime();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String date = sdf.format(hoy); 
                                    hoy = sdf.parse(date);
                                    
                                    if(!fechaLimite.before(hoy)&&hoy.getDay() == 6){  
                                        
                                        if(emAgendas.length == 0){
                                            EmpleadoAgenda emAgenda = new EmpleadoAgenda();
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());


                                            try{
                                                new EmpleadoAgendaDaoImpl(user.getConn()).insert(emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                        }else{                                       

                                            EmpleadoAgenda emAgenda = emAgendas[0];
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());

                                            try{
                                                new EmpleadoAgendaDaoImpl(user.getConn()).update(emAgenda.createPk(),emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                        }

                    }
                    
                    if(tipoAsignacion.equals("CLIENTES")) {                        
                      
                       
                       filtro = " AND  ID_CLIENTE IN (SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR="+ idEmpleadoOrigen +")"
                                + " AND DIAS_VISITA = '' ";
                                
                        clientesDto = clienteBO.findClientes(-1, idEmpresa, -1, -1, filtro);
                        for(Cliente item: clientesDto){
                            //Relacion con vendedor

                                    SGClienteVendedorBO clienteVendedorBO = new SGClienteVendedorBO(item.getIdCliente(),user.getConn());
                                    SgfensClienteVendedor clienteVendedorDto = clienteVendedorBO.getClienteVendedor();
                                    // actualizamos registro
                                    if(clienteVendedorDto!=null){
                                    clienteVendedorDto.setIdUsuarioVendedorReasignado(idEmpleadoDestino);
                                        if(fechaLimite!=null){
                                            clienteVendedorDto.setFechaLimiteReasigancion(fechaLimite);
                                        }else{
                                            clienteVendedorDto.setFechaLimiteReasigancion(new Date());
                                        }

                                        new SgfensClienteVendedorDaoImpl(user.getConn()).update(clienteVendedorDto.createPk(), clienteVendedorDto);                        
                                    }     
                                    
                                    //Creamos tareas para cada cliente 
                                    
                                    Empleado empleadoTarea =  empleadoBO.findEmpleadoByUsuario(idEmpleadoDestino);
                                    String nombreCliente = item.getNombreCliente()!=null?item.getNombreCliente():"" + " " + item.getApellidoPaternoCliente()!=null?item.getApellidoPaternoCliente():"" + " " + item.getApellidoMaternoCliente()!=null?item.getApellidoMaternoCliente():"";                                    
                                    
                                    EmpleadoAgendaDaoImpl empleadoAgendaDao = new EmpleadoAgendaDaoImpl();
                                    EmpleadoAgenda[] emAgendas = empleadoAgendaDao.findByDynamicWhere(" ID_ESTATUS <> 2 AND ID_CLIENTE = " + item.getIdCliente() + " AND FECHA_PROGRAMADA = '" + df.format(new Date()) + "'", null);
                                    
                                    Date fechaProg = null;                                    
                                    Calendar c = Calendar.getInstance(); //OBTENGO LA FECHA DE HOY 
                                    Date hoy = c.getTime();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String date = sdf.format(hoy); 
                                    hoy = sdf.parse(date);
                                    
                                    if(!fechaLimite.before(hoy)){    
                                        
                                        if(emAgendas.length == 0){
                                            EmpleadoAgenda emAgenda = new EmpleadoAgenda();
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());


                                            try{
                                                new EmpleadoAgendaDaoImpl(user.getConn()).insert(emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                        }else{                                       

                                            EmpleadoAgenda emAgenda = emAgendas[0];
                                            emAgenda.setIdEmpleado(empleadoTarea.getIdEmpleado());
                                            emAgenda.setIdEstatus(1);
                                            emAgenda.setFechaCreacion(new Date());
                                            emAgenda.setFechaProgramada(new Date());
                                            emAgenda.setFechaEjecucion(null);
                                            emAgenda.setNombreTarea("Visita a Cliente");
                                            emAgenda.setDescripcionTarea("Realizar Visita al Cliente: " + nombreCliente);
                                            emAgenda.setIdCliente(item.getIdCliente());

                                            try{
                                                new EmpleadoAgendaDaoImpl(user.getConn()).update(emAgenda.createPk(),emAgenda);                                        
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                    
                        }
                    } 

                    out.print("<!--EXITO-->Los clientes se han compartido satisfactoriamente");             
                    

        }else{
        out.print("<!--ERROR-->"+msgError);
        }        
        
    }else if(mode.equals("eliminarAgenda")){        
       
        try{
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
        }catch(NumberFormatException ex){}        
         
            //Relacion con vendedor

                    //SGClienteVendedorBO clienteVendedorBO = new SGClienteVendedorBO(user.getConn());
                    SgfensClienteVendedorDaoImpl clienteVendedorDaoImpl =  new SgfensClienteVendedorDaoImpl();
                    SgfensClienteVendedor[] clienteVendedorDtos = clienteVendedorDaoImpl.findByDynamicWhere(" ID_USUARIO_VENDEDOR_REASIGNADO = " + idEmpleado , null);
                    // actualizamos registro
                    EmpleadoBO  empleadoBO =  new EmpleadoBO(user.getConn());
                    
                    for(SgfensClienteVendedor clienteVendedorDto : clienteVendedorDtos){
                        clienteVendedorDto.setIdUsuarioVendedorReasignado(0);
                        clienteVendedorDto.setFechaLimiteReasigancion(null);

                        new SgfensClienteVendedorDaoImpl(user.getConn()).update(clienteVendedorDto.createPk(), clienteVendedorDto);   
                        
                        
                        
                        //eliminamos tareas  ID_ESTATUS = 2
                        
                        Empleado empleadoDto = empleadoBO.findEmpleadoByUsuario(idEmpleado);
                        EmpleadoAgendaDaoImpl empleadoAgendaDao = new EmpleadoAgendaDaoImpl();
                        EmpleadoAgenda[] tareasAgenda = empleadoAgendaDao.findByDynamicWhere(" ID_ESTATUS <> 2 AND ID_CLIENTE = " + clienteVendedorDto.getIdCliente() + " AND ID_EMPLEADO = " + empleadoDto.getIdEmpleado() , null);
                        
                        for(EmpleadoAgenda emAgenda :  tareasAgenda){
                            
                            emAgenda.setIdEstatus(2);//Eliminar tarea
                            
                            try{
                                new EmpleadoAgendaDaoImpl(user.getConn()).update(emAgenda.createPk(),emAgenda);                                        
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            
                            
                        }
                        
                        
                    }         
                           
                    
        out.print("<!--EXITO-->Los clientes se han dejado de compartir satisfactoriamente");
    
    }else if(mode.equals("limpiaCarrito")){
    
        HttpSession sesion = request.getSession();   
        sesion.setAttribute("carrito", null);
        
        out.print("<!--EXITO-->Carrito Vacio");
    
    }else if(mode.equals("quitarZona")){    
       //quitar la zona           
           try{
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
           }catch(NumberFormatException ex){}           
            EmpleadoBO empleadoBO = new EmpleadoBO(idEmpleado,user.getConn());
            Empleado empleadoDto = empleadoBO.getEmpleado();
            empleadoDto.setIdRegion(0);
            try{
                new EmpleadoDaoImpl(user.getConn()).update(empleadoDto.createPk(), empleadoDto);
                out.print("<!--EXITO-->Zona desasignada satisfactoriamente");
            }catch(Exception ex){
                out.print("<!--ERROR-->No se pudo desasignar el registro. Informe del error al administrador del sistema: " + ex.toString());
                ex.printStackTrace();
            }        
    
    }else if(mode.equals("quitarGeocerca")){    
       //quitar la zona           
           try{
            idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
           }catch(NumberFormatException ex){}           
            EmpleadoBO empleadoBO = new EmpleadoBO(idEmpleado,user.getConn());
            Empleado empleadoDto = empleadoBO.getEmpleado();
            empleadoDto.setIdGeocerca(0);
            try{
                new EmpleadoDaoImpl(user.getConn()).update(empleadoDto.createPk(), empleadoDto);
                out.print("<!--EXITO-->Geocerca desasignada satisfactoriamente");
            }catch(Exception ex){
                out.print("<!--ERROR-->No se pudo desasignar el registro. Informe del error al administrador del sistema: " + ex.toString());
                ex.printStackTrace();
            }        
    
    }else if(mode.equals("validaGasto")){

        int idGastos = -1;
        int idValidaGasto = -1;
        
        try{
         idGastos = Integer.parseInt(request.getParameter("idGastos"));
        }catch(NumberFormatException ex){}      
        
        try{
         idValidaGasto = Integer.parseInt(request.getParameter("idValidacion"));
        }catch(NumberFormatException ex){} 
        
        if(idGastos>0){
            
            if(idValidaGasto>=0){
                
                try{
                  GastosEvcBO gastosEvcBO = new GastosEvcBO(idGastos,user.getConn());
                  GastosEvc GastosEvcDto = gastosEvcBO.getGastos();
                  
                  GastosEvcDto.setValidacion(idValidaGasto);
                  
                  new GastosEvcDaoImpl().update(GastosEvcDto.createPk(), GastosEvcDto);
                  out.print("<!--EXITO-->Registro actualizado satisfactoriamente.");
                }catch(Exception e){
                    e.printStackTrace();
                    out.print("<!--ERROR-->Ocurrio un error al actualizar el Gasto");
                }
            }else{
                out.print("<!--ERROR-->Seleccione un estatus para el Gasto");
            }
            
        }else{
             out.print("<!--ERROR-->No se ha seleccionado un Gasto");
        }
        
    
      
    }else if(mode.equals("guardaGasto")){
        
        
        msgError ="";
        
        int idGastos = -1;
        int idUsuarioVendedor = -1;
        Empleado empleadoDto = null;
        Date fechaActual = new Date();
        double monto = 0;
        int motivoGasto = -1;
        String comentarios ="";
        
        try{
         idGastos = Integer.parseInt(request.getParameter("idGastos"));
        }catch(NumberFormatException ex){} 
        try{
         idUsuarioVendedor = Integer.parseInt(request.getParameter("q_idvendedor"));
        }catch(NumberFormatException ex){}
        try{
         monto = Double.parseDouble(request.getParameter("monto"));
        }catch(NumberFormatException ex){}
        try{
         motivoGasto = Integer.parseInt(request.getParameter("motivoGasto"));
        }catch(NumberFormatException ex){}
        
        comentarios = request.getParameter("comentarios")!=null?new String(request.getParameter("comentarios").getBytes("ISO-8859-1"),"UTF-8"):"";
        
        if(idUsuarioVendedor>0){
            try{
                EmpleadoBO empleadoBO = new EmpleadoBO(user.getConn());
                empleadoDto =  empleadoBO.findEmpleadoByUsuario(idUsuarioVendedor);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        if(idGastos>0){//modifica
            
            
        }else{//nuevo
            
            if (motivoGasto<=0)
                msgError += "<ul>El dato 'Concepto' es requerido. ";
            if (monto<=0)
                msgError += "<ul>El dato 'Monto' debe ser mayor a cero. ";
            if(idUsuarioVendedor<0){
                if (comentarios.equals(""))
                    msgError += "<ul>El dato 'Comentario' es requerido. ";
            }
            
            if(msgError.equals("")){
               GastosEvc gastosEvcDto = new GastosEvc();
               gastosEvcDto.setFecha(fechaActual);
               gastosEvcDto.setIdConcepto(motivoGasto);
               gastosEvcDto.setIdEmpresa(idEmpresa);
               if(empleadoDto!=null){
                   gastosEvcDto.setIdEmpleado(empleadoDto.getIdEmpleado());
               }
               gastosEvcDto.setComentario(comentarios);
               gastosEvcDto.setValidacion(0);
               gastosEvcDto.setIdEstatus(1);
               gastosEvcDto.setMonto(monto);

               try{              

                  new GastosEvcDaoImpl().insert(gastosEvcDto);
                  out.print("<!--EXITO-->Registro almacenado satisfactoriamente.");
                }catch(Exception e){
                    e.printStackTrace();
                    out.print("<!--ERROR-->Ocurrio un error al guardar el Gasto");
                }
            }else{
               out.print("<!--ERROR-->"+msgError);
            }
           
        }
        
        
        
    }else{
    
    try{
        idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
    }catch(NumberFormatException ex){}
    numEmpleado = request.getParameter("numEmpleado")!=null?new String(request.getParameter("numEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";    
    nombreEmpleado = request.getParameter("nombreEmpleado")!=null?new String(request.getParameter("nombreEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";
    apellidoPaternoEmpleado = request.getParameter("apellidoPaternoEmpleado")!=null?new String(request.getParameter("apellidoPaternoEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";
    apellidoMaternoEmpleado = request.getParameter("apellidoMaternoEmpleado")!=null?new String(request.getParameter("apellidoMaternoEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";
    telefonoEmpleado = request.getParameter("telefonoEmpleado")!=null?new String(request.getParameter("telefonoEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";
    emailEmpleado = request.getParameter("emailEmpleado")!=null?new String(request.getParameter("emailEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";
    direccionEmpleado = request.getParameter("direccionEmpleado")!=null?new String(request.getParameter("direccionEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";
    usuarioEmpleado = request.getParameter("usuarioEmpleado")!=null?new String(request.getParameter("usuarioEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";
    contrasenaEmpleado = request.getParameter("contrasenaEmpleado")!=null?new String(request.getParameter("contrasenaEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";
    recordatorioEmpleado = request.getParameter("recordatorioEmpleado")!=null?new String(request.getParameter("recordatorioEmpleado").getBytes("ISO-8859-1"),"UTF-8"):"";
     
    try{
        estatus = Integer.parseInt(request.getParameter("estatus"));
    }catch(NumberFormatException ex){}
    try{
        idDispositivoMovilEmpleado = Integer.parseInt(request.getParameter("idDispositivoMovilEmpleado"));
    }catch(NumberFormatException ex){}
    try{
        idRolEmpleado = Integer.parseInt(request.getParameter("idRolEmpleado"));
    }catch(NumberFormatException ex){}
    try{
        idAutomovilEmpleado = Integer.parseInt(request.getParameter("idAutomovilEmpleado"));
    }catch(NumberFormatException ex){}
    try{
        idSucursalEmpresaAsignado = Integer.parseInt(request.getParameter("idSucursalEmpresaAsignado"));
    }catch(NumberFormatException ex){}
    try{
        repartidorEmpleado = Integer.parseInt(request.getParameter("repartidorEmpleado"));
    }catch(NumberFormatException ex){}
    try{
        idRegion = Integer.parseInt(request.getParameter("idZona"));
    }catch(NumberFormatException ex){}
    
    try{
        idPeriodo = Integer.parseInt(request.getParameter("idPeriodo"));
    }catch(NumberFormatException ex){}
    try{
        sueldoEmpleado = Double.parseDouble(request.getParameter("sueldoEmpleado"));
    }catch(Exception ex){}
    try{
        comisionEmpleado = Double.parseDouble(request.getParameter("comisionEmpleado"));
    }catch(Exception ex){}
    try{
        permisoVentaRapida = Integer.parseInt(request.getParameter("ventaRapida"));
    }catch(NumberFormatException ex){}
    try{
        ventaConsigna = Integer.parseInt(request.getParameter("ventaConsigna"));
    }catch(NumberFormatException ex){}
    
    try{
        permisoVentaCredito = Integer.parseInt(request.getParameter("ventaCredito"));
    }catch(NumberFormatException ex){}
        
    try{
        trabajarFueraLinea = Integer.parseInt(request.getParameter("trabajarFueraLinea"));
    }catch(NumberFormatException ex){}
    try{
        clientesBarras = Integer.parseInt(request.getParameter("clientesBarras"));
    }catch(NumberFormatException ex){}
    try{
        precioCompra = Integer.parseInt(request.getParameter("precioCompra"));
    }catch(NumberFormatException ex){}
    try{
        distanciaObligaEmple = Double.parseDouble(request.getParameter("distanciaObligaEmple"));
    }catch(Exception ex){}
    
    try{
        permisoAccionesClientes = Integer.parseInt(request.getParameter("permisoAccionesClientes"));
    }catch(NumberFormatException ex){}
    
    try{
        idHorario = Integer.parseInt(request.getParameter("idhorarioAsignado"));
    }catch(NumberFormatException ex){}
    try{
        tiempoMinutosActualiza = Integer.parseInt(request.getParameter("tiempo_minutos_actualiza"));
    }catch(NumberFormatException ex){}
    try{
        tiempoMinutosRecordatorio = Integer.parseInt(request.getParameter("tiempo_minutos_recordatorio"));
    }catch(NumberFormatException ex){}
    try{
        idFoliosMovilEmpleado = Integer.parseInt(request.getParameter("id_folio_movil_empleado"));
    }catch(NumberFormatException ex){}
    try{
        permisoDevoluciones = Integer.parseInt(request.getParameter("permisoDevoluciones"));
    }catch(NumberFormatException ex){}
    try{
        permisoAutoServicioInventario = Integer.parseInt(request.getParameter("permisoAutoServicioInventario"));
    }catch(NumberFormatException ex){}
    try{
        noCobroParcial = Integer.parseInt(request.getParameter("noCobroParcial"));
    }catch(NumberFormatException ex){}
    try{
        verProveedores = Integer.parseInt(request.getParameter("verProveedores"));
    }catch(NumberFormatException ex){}
    try{
        intervaloUbicacionSegundos = Integer.parseInt(request.getParameter("intervalo_ubicacion_seg"));
    }catch(NumberFormatException ex){}
    
    /*
    * Validaciones del servidor
    */
    /*String msgError = "";
    GenericValidator gc = new GenericValidator();   */ 
    
    if(!gc.isValidString(numEmpleado, 1, 50))
        msgError += "<ul>El dato 'Numero de Empleado' es requerido.";
    if(!gc.isValidString(nombreEmpleado, 1, 70))
        msgError += "<ul>El dato 'nombre' es requerido";
    if(!gc.isValidString(apellidoPaternoEmpleado, 1, 70))
        msgError += "<ul>El dato 'apellido paterno' es requerido";
    if(!gc.isValidString(apellidoMaternoEmpleado, 1, 70))
        msgError += "<ul>El dato 'apellido materno' es requerido";
    if(!emailEmpleado.equals("")){
        if(!gc.isEmail(emailEmpleado))
            msgError += "<ul>El dato 'Correo electr&oacute;nico' es incorrecto. <br/>";
       }
    if(idRolEmpleado==-1)
        msgError += "<ul>El 'Rol' es requerido";
    if(!gc.isValidString(usuarioEmpleado, 1, 50)){
        msgError += "<ul>El dato 'Usuario' es requerido.";  
    }
    if (tiempoMinutosActualiza<0)
        msgError += "<ul>El dato 'Solicitar Estatus cada' debe ser un valor positivo o cero";
    if (tiempoMinutosRecordatorio<0)
        msgError += "<ul>El dato 'Recordatorio cada' debe ser un valor positivo o cero";
    //verificamos si existe el nombre de usuario
    UsuariosDaoImpl usersDaoImpl = new UsuariosDaoImpl();
    Usuarios[] usuariosDtos = usersDaoImpl.findWhereUserNameEquals(usuarioEmpleado.trim());
    //Validación nombre se usuario existente
   if(mode.equals("1")){
    
       EmpleadoBO empleado2BO = new EmpleadoBO(user.getConn());
       Empleado empleadoActualizarDto = empleado2BO.findEmpleadobyId(idEmpleado);

       UsuarioBO usuario2BO = new UsuarioBO(empleadoActualizarDto.getIdUsuarios());
       Usuarios usuarioActualizarDto = usuario2BO.getUser();


       System.out.println(empleadoActualizarDto.getUsuario()+","+usuarioEmpleado);
        if(!usuarioActualizarDto.getUserName().equals(usuarioEmpleado)){
            if(usuariosDtos.length > 0){
                    msgError += "<ul>El Nombre de 'Usuario' ya esta siendo utilizado por otra persona.";     
            }
        }
   }else{
   
        UsuariosDaoImpl usersDaoImpl2 = new UsuariosDaoImpl();
        Usuarios[] usuariosDtos2 = usersDaoImpl2.findWhereUserNameEquals(usuarioEmpleado.trim());
        if(usuariosDtos2.length > 0){
                msgError += "<ul>El Nombre de 'Usuario' ya esta siendo utilizado por otra persona.";     
        }
   
   }  
    /*if(!gc.isPasswordSeguro(contrasenaEmpleado))        
        msgError += "<ul>El dato 'Contraseña' no es correcto.";*/
    if(!gc.isValidString(recordatorioEmpleado, 0, 100))
        msgError += "<ul>El dato ID 'Recordatorio de Contraseña' es requerido.";
    if(idEmpleado <= 0 && (!mode.equals("")))
        msgError += "<ul>El dato ID 'Empleado' es requerido";  
    
    

    if(msgError.equals("")){
        if(idEmpleado>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
                EmpleadoBO empleadoBO = new EmpleadoBO(idEmpleado,user.getConn());
                Empleado empleadoDto = empleadoBO.getEmpleado();
                               
                empleadoDto.setIdEstatus(estatus);
                empleadoDto.setRepartidor(repartidorEmpleado);
                
                if(idSucursalEmpresaAsignado<0)
                    empleadoDto.setIdEmpresa(idEmpresa);
                else
                    empleadoDto.setIdEmpresa(idSucursalEmpresaAsignado);
                
                if(idDispositivoMovilEmpleado>0){
                    //VERIFICAMOS QUE TIPO DE PAQUETE TIENE
                    empleadoDto.setIdDispositivo(idDispositivoMovilEmpleado);
                }else{
                    empleadoDto.setIdDispositivo(-1);
                }
                    
                empleadoDto.setIdMovilEmpleadoRol(idRolEmpleado);
                if(idSucursalEmpresaAsignado<0)
                    empleadoDto.setIdSucursal(idEmpresa);
                else
                    empleadoDto.setIdSucursal(idSucursalEmpresaAsignado);                 
                //empleadoDto.setIdSucursal(idEmpresa);
                empleadoDto.setNombre(nombreEmpleado);                
                empleadoDto.setApellidoPaterno(apellidoPaternoEmpleado);
                empleadoDto.setApellidoMaterno(apellidoMaternoEmpleado);                                                                
                empleadoDto.setTelefonoLocal(telefonoEmpleado);                
                empleadoDto.setNumEmpleado(numEmpleado);
                empleadoDto.setCorreoElectronico(emailEmpleado); 
                empleadoDto.setIdRegion(idRegion);
                empleadoDto.setIdPeriodoPago(idPeriodo);
                empleadoDto.setPorcentajeComision(comisionEmpleado);
                empleadoDto.setSueldo(sueldoEmpleado);
                empleadoDto.setUsuario(usuarioEmpleado);
                if(!contrasenaEmpleado.trim().equals("")){
                    Encrypter encriptacion = new Encrypter();//ENCRIPTAMOS EL PASS
                    encriptacion.setMd5(true);
                    empleadoDto.setPassword(encriptacion.encodeString2(contrasenaEmpleado));
                
                }
                empleadoDto.setPermisoVentaRapida(permisoVentaRapida);
                empleadoDto.setVentaConsigna(ventaConsigna);
                empleadoDto.setPermisoVentaCredito(permisoVentaCredito);
                
                empleadoDto.setTrabajarFueraLinea(trabajarFueraLinea);
                empleadoDto.setClientesCodigoBarras(clientesBarras);
                empleadoDto.setPrecioDeCompra(precioCompra);
                empleadoDto.setDistanciaObligatorio(distanciaObligaEmple);
                empleadoDto.setPermisoAccionesCliente(permisoAccionesClientes);
                empleadoDto.setPermisoDevoluciones(permisoDevoluciones);
                empleadoDto.setPermisoAutoServInventario(permisoAutoServicioInventario);
                
                empleadoDto.setPermisoNoCobroParcial(noCobroParcial);
                empleadoDto.setPermisoVerProveedores(verProveedores);
                
                empleadoDto.setIdFolioMovilEmpleado(idFoliosMovilEmpleado);
                empleadoDto.setIntervaloUbicacionSeg(intervaloUbicacionSegundos);
                //Asignamos horario
                empleadoDto.setIdHorario(idHorario);
                //Si hay un valor valido en tiempoMinutosActualiza, creamos o modificamos el registro de Parametros Estatus
                if (tiempoMinutosActualiza>=0){
                    boolean noExisteYDatosVacios = false;
                    PosMovilEstatusParametrosDaoImpl pmepDao = new PosMovilEstatusParametrosDaoImpl(user.getConn());
                    PosMovilEstatusParametros posMovilEstatusParametros = null;
                    PosMovilEstatusParametros[] coinPosMovilEstatusParametros = pmepDao.findWhereIdEmpleadoEquals(empleadoDto.getIdEmpleado());
                    if (coinPosMovilEstatusParametros.length>0){
                        posMovilEstatusParametros =  coinPosMovilEstatusParametros[0];
                    }else if( tiempoMinutosActualiza<=0 && tiempoMinutosRecordatorio<=0 ){
                        //si no existe un registro previo asociado al empleado
                        // y ademas los valores no fueron diferentes a 0 y 0, 
                        // NO es necesario insertar el registro
                        noExisteYDatosVacios = true;
                    }

                    if (!noExisteYDatosVacios){
                        if (posMovilEstatusParametros==null)
                            posMovilEstatusParametros = new PosMovilEstatusParametros();

                        posMovilEstatusParametros.setIdEmpresa(idEmpresa);
                        posMovilEstatusParametros.setIdEmpleado(idEmpleado);
                        posMovilEstatusParametros.setTiempoMinutosActualiza(tiempoMinutosActualiza);
                        posMovilEstatusParametros.setTMinutosRecordatorio(tiempoMinutosRecordatorio);

                        if (posMovilEstatusParametros.getIdEstatusParametro()>0){
                            pmepDao.update(posMovilEstatusParametros.createPk(), posMovilEstatusParametros);
                        }else{
                            pmepDao.insert(posMovilEstatusParametros);
                        }
                    }
                }
                
                //Si selecciono un dispositivo Movil lo actualizamos a asignado.  
                if(idDispositivoMovilEmpleado>0){  
                    DispositivoMovilBO movilBO = new DispositivoMovilBO(idDispositivoMovilEmpleado,user.getConn());
                    DispositivoMovil movil = new DispositivoMovil();
                    movil = movilBO.getDispositivoMovil();
                    short x = 1;
                    movil.setAsignado(x);
                    DispositivoMovilDaoImpl dispositivoMovilDaoImpl = new DispositivoMovilDaoImpl(user.getConn());
                    try{
                        dispositivoMovilDaoImpl.update(movil.createPk(), movil);
                    }catch(Exception ex){
                        out.print("<!--ERROR-->No se pudo actualizar el registro del movil. Informe del error al administrador del sistema: " + ex.toString());
                        ex.printStackTrace();
                    }
                }
                ////////////
                               
                //verificamos si se desactiva
                if(empleadoDto.getIdEstatus()==2){
                                     
                    Empresa empresaMatriz = new Empresa();
                    EmpresaBO empresaMatrizBO = new EmpresaBO(user.getConn());
                    empresaMatriz = empresaMatrizBO.findEmpresabyId(empresaDatosPadre .getIdEmpresaPadre());
                    //empresaMatriz.setNumLicenciasDisponibles((empresaMatriz.getNumLicenciasDisponibles()+1));
                    new EmpresaDaoImpl(user.getConn()).update(empresaMatriz.createPk(), empresaMatriz);
                    
                    //ADEMAS DESACTIVAMOS AL USUARIO
                    UsuarioBO usuarioBO = new UsuarioBO(empleadoDto.getIdUsuarios());
                    usuarioBO.getUser().setIdEstatus(2);
                    new UsuariosDaoImpl(user.getConn()).update(usuarioBO.getUser().createPk(), usuarioBO.getUser());
                    
                                        
                    
                }else{
                   
                    UsuarioBO usuarioBO = new UsuarioBO(empleadoDto.getIdUsuarios());
                    usuarioBO.getUser().setIdEstatus(1);
                    usuarioBO.getUser().setIdRoles(idRolEmpleado);
                    usuarioBO.getUser().setUserName(usuarioEmpleado);
                     
                     if(idSucursalEmpresaAsignado<0)
                        usuarioBO.getUser().setIdEmpresa(idEmpresa);
                    else
                        usuarioBO.getUser().setIdEmpresa(idSucursalEmpresaAsignado);   
                     
                    new UsuariosDaoImpl(user.getConn()).update(usuarioBO.getUser().createPk(), usuarioBO.getUser());
                    
                    
                                        
                    //DATOS PARA LA TABLA LDAP
                    Ldap ldap = usuarioBO.getLdap();
                    LdapDaoImpl ldapDaoImpl = new LdapDaoImpl(user.getConn());                
                    ldap.setUsuario(usuarioEmpleado);                
                    Encrypter encriptacion = new Encrypter();//ENCRIPTAMOS EL PASS
                    encriptacion.setMd5(true);
                    if(!contrasenaEmpleado.trim().equals(""))
                        ldap.setPassword(encriptacion.encodeString2(contrasenaEmpleado));
                    ldap.setEmail(emailEmpleado);                
                    //ldap.setRecordatorioContrasena(recordatorioEmpleado);
                    try{
                        ldapDaoImpl.update(ldap.createPk(), ldap);
                    }catch(Exception ex){
                        out.print("<!--ERROR-->No se pudo actualizar el registro de cambio de password. Informe del error al administrador del sistema: " + ex.toString());
                        ex.printStackTrace();
                    } 
                    
                    
                    //DATOS PARA LA TABLA DATOS_USUARIO
                    try{
                        DatosUsuario datosUsuario = new DatosUsuario();
                        DatosUsuarioDaoImpl datosUsuarioDaoImpl = new DatosUsuarioDaoImpl(user.getConn()); 

                        datosUsuario = datosUsuarioDaoImpl.findByPrimaryKey(usuarioBO.getDatosUsuario().getIdDatosUsuario());

                        datosUsuario.setNombre(nombreEmpleado);
                        datosUsuario.setApellidoPat(apellidoPaternoEmpleado);
                        datosUsuario.setApellidoMat(apellidoMaternoEmpleado);
                        datosUsuario.setDireccion(direccionEmpleado);
                        datosUsuario.setTelefono(telefonoEmpleado);
                        datosUsuario.setCorreo(emailEmpleado);
                        
                        

                        datosUsuarioDaoImpl.update(datosUsuario.createPk(),datosUsuario);//INSERTAMOS EL REGISTRO DE DATOS USUARIO Y RECUPERAMOS EL OBJETO PARA TENER EL ID ASIGNADO 
                    
                    }catch(Exception e){System.out.println("Error al actualizar datos del usuario");}
                    
                    
                }
                    
                
                try{
                    new EmpleadoDaoImpl(user.getConn()).update(empleadoDto.createPk(), empleadoDto);
                    
                    
                    
                    
                    
                    /****************************Edita Cliente vendedor consigna
                     * 
                     */                     
                        ClienteBO clienteBO = new ClienteBO(idCliente,user.getConn());
                        Cliente[] clientesDto = clienteBO.findClientebyIdEmpleado(empleadoDto.getIdEmpleado());
                        Cliente clienteDto = null;
                        
                        if(clientesDto.length > 0){
                            clienteDto = clientesDto[0];//seteamos cliente al primero encontrado
                            
                            if(empleadoDto.getIdEstatus()==2){
                                clienteDto.setIdEstatus(2);
                            }else{
                                clienteDto.setIdEstatus(estatus);
                            }
                            clienteDto.setRfcCliente("AAA010101");
                            clienteDto.setRazonSocial(nombreEmpleado + " " + apellidoPaternoEmpleado + " " +  apellidoMaternoEmpleado);
                            clienteDto.setNombreCliente(nombreEmpleado);
                            clienteDto.setApellidoPaternoCliente(apellidoPaternoEmpleado);
                            clienteDto.setApellidoMaternoCliente(apellidoMaternoEmpleado);
                            clienteDto.setCalle("Campo por llenar");
                            clienteDto.setNumero("0");
                            clienteDto.setNumeroInterior("");
                            clienteDto.setCodigoPostal("11111");
                            clienteDto.setColonia("Campo por llenar");
                            clienteDto.setMunicipio("Campo por llenar");
                            clienteDto.setEstado("Campo por llenar");
                            clienteDto.setPais("México");
                            clienteDto.setLada("01");
                            clienteDto.setTelefono("1234567890");
                            clienteDto.setExtension("");
                            clienteDto.setCelular("0000000000");
                            clienteDto.setCorreo(emailEmpleado);
                            clienteDto.setContacto("Campo por llenar");
                            clienteDto.setGenerico(0);
                            clienteDto.setIdEmpresa(idEmpresa); 
                            clienteDto.setDiasVisita("");
                            clienteDto.setPerioricidad(0);
                            clienteDto.setIdVendedorConsigna(empleadoDto.getIdEmpleado());//Empleado insertado



                            try{
                                new ClienteDaoImpl(user.getConn()).update(clienteDto.createPk(), clienteDto);

                                //out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
                            }catch(Exception ex){
                                out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                                ex.printStackTrace();
                            }
                    
                        }else{// cuando inicialmente no tenia este permiso y posteriormente se lo asigna crea al cliente
                            
                            if(ventaConsigna == 1){
                               try{ 
                                     //nuevo express
                                    clienteDto = new Cliente();
                                    ClienteDaoImpl clientesDaoImpl = new ClienteDaoImpl(user.getConn());

                                    int idClienteNuevo;
                                    Cliente ultimoRegistroClientes = clientesDaoImpl.findLast();
                                    idClienteNuevo = ultimoRegistroClientes.getIdCliente() + 1;
                                    clienteDto.setIdCliente(idClienteNuevo);

                                    clienteDto.setIdEstatus(3);
                                    clienteDto.setRfcCliente("AAA010101");
                                    clienteDto.setRazonSocial(nombreEmpleado + " " + apellidoPaternoEmpleado + " " +  apellidoMaternoEmpleado);
                                    clienteDto.setNombreCliente(nombreEmpleado);
                                    clienteDto.setApellidoPaternoCliente(apellidoPaternoEmpleado);
                                    clienteDto.setApellidoMaternoCliente(apellidoMaternoEmpleado);
                                    clienteDto.setCalle("Campo por llenar");
                                    clienteDto.setNumero("0");
                                    clienteDto.setNumeroInterior("");
                                    clienteDto.setCodigoPostal("11111");
                                    clienteDto.setColonia("Campo por llenar");
                                    clienteDto.setMunicipio("Campo por llenar");
                                    clienteDto.setEstado("Campo por llenar");
                                    clienteDto.setPais("México");
                                    clienteDto.setLada("01");
                                    clienteDto.setTelefono("1234567890");
                                    clienteDto.setExtension("");
                                    clienteDto.setCelular("0000000000");
                                    clienteDto.setCorreo(emailEmpleado);
                                    clienteDto.setContacto("Campo por llenar");
                                    clienteDto.setGenerico(0);
                                    clienteDto.setIdEmpresa(idEmpresa); 
                                    clienteDto.setDiasVisita("");
                                    clienteDto.setPerioricidad(0);
                                    clienteDto.setIdVendedorConsigna(empleadoDto.getIdEmpleado());//Empleado insertado

                                    /**
                                     * Realizamos el insert
                                     */
                                    clientesDaoImpl.insert(clienteDto);


                                    //out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");                   


                                }catch(Exception e){
                                    e.printStackTrace();
                                    msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
                                }
                           }
                            
                        }
                     /* 
                     * 
                     ********************************/
                    
                    
                    
                    
                    
                    
                    
                    

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
                 * Creamos el registro de Empleado
                 */                
                
                 //////////VALIDAMOS CUANTOS REGISTROS TIENE PARA VER SI LO DEJAMOS CREAR O NO, DEPENDIENDO DEL PAQUETE:
                EmpresaBO empresaBO = new EmpresaBO(user.getConn());
                EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(user.getConn()).findByPrimaryKey(empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa());
                
                //RECUPERAMOS LA LONGITUD DEL NUMERO DE RESGITROS QUE SE ENCUANTRAN CREADOS CON ESTATUS ACTIVO
                /*DispositivoMovilBO ubo = new DispositivoMovilBO(user.getConn());
                DispositivoMovil[] lista = new DispositivoMovil[0];
                lista = ubo.findDispositivosMoviles(0, empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa(), 0, 0, "");*/
                
                UsuariosBO usuariosBO = new UsuariosBO();
                Usuarios[] listaUsuarios = new Usuarios[0];
                listaUsuarios = usuariosBO.findUsuarios(-1, empresaBO.getEmpresaMatriz(user.getUser().getIdEmpresa()).getIdEmpresa(), 0, 0, " AND ID_ESTATUS <> 2 ");

                //System.out.println("---------------DISPOSITIVOS MOVILES: "+lista.length);
                System.out.println("---------------USUARIOS: "+listaUsuarios.length);

                //int licenciasUsadas = 0;
                //licenciasUsadas = lista.length + listaUsuarios.length;


                //System.out.println("---------------LICENCIAS USADAS: "+licenciasUsadas);
                
                //if(licenciasUsadas < empresaPermisoAplicacionDto.getAccesoSgfensNumLicenciasMoviles()){
                    if (listaUsuarios.length < empresaPermisoAplicacionDto.getMaxNumUsuarios()){
                
                        Empleado empleadoDto = new Empleado();
                        EmpleadoDaoImpl empleadosDaoImpl = new EmpleadoDaoImpl(user.getConn());

                        empleadoDto.setIdEstatus(estatus);
                        empleadoDto.setRepartidor(repartidorEmpleado);
                        if(idSucursalEmpresaAsignado<0)
                            empleadoDto.setIdEmpresa(idEmpresa);
                        else
                            empleadoDto.setIdEmpresa(idSucursalEmpresaAsignado);                
                        empleadoDto.setIdEstado(2);
                        //empleadoDto.setIdVehiculo(idAutomovilEmpleado);              


                        if(idDispositivoMovilEmpleado>0){
                            //VERIFICAMOS QUE TIPO DE PAQUETE TIENE
                            empleadoDto.setIdDispositivo(idDispositivoMovilEmpleado);
                        }else{
                            empleadoDto.setIdDispositivo(-1);
                        }                
                        //empleadoDto.setIdDispositivo(idDispositivoMovilEmpleado);
                        empleadoDto.setIdMovilEmpleadoRol(idRolEmpleado);
                        if(idSucursalEmpresaAsignado<0)
                            empleadoDto.setIdSucursal(idEmpresa);
                        else
                            empleadoDto.setIdSucursal(idSucursalEmpresaAsignado);                 
                        //empleadoDto.setIdSucursal(idEmpresa);
                        empleadoDto.setNombre(nombreEmpleado);                
                        empleadoDto.setApellidoPaterno(apellidoPaternoEmpleado);
                        empleadoDto.setApellidoMaterno(apellidoMaternoEmpleado);                                                                
                        empleadoDto.setTelefonoLocal(telefonoEmpleado);                
                        empleadoDto.setNumEmpleado(numEmpleado);
                        empleadoDto.setCorreoElectronico(emailEmpleado);
                        empleadoDto.setIdRegion(idRegion);

                        empleadoDto.setIdPeriodoPago(idPeriodo);
                        empleadoDto.setPorcentajeComision(comisionEmpleado);
                        empleadoDto.setSueldo(sueldoEmpleado);

                        if(!contrasenaEmpleado.trim().equals("")){
                            Encrypter encriptacion = new Encrypter();//ENCRIPTAMOS EL PASS
                            encriptacion.setMd5(true);
                            empleadoDto.setPassword(encriptacion.encodeString2(contrasenaEmpleado));
                        }
                        empleadoDto.setUsuario(usuarioEmpleado);
                        empleadoDto.setPermisoVentaRapida(permisoVentaRapida);
                        empleadoDto.setVentaConsigna(ventaConsigna);
                        empleadoDto.setPermisoVentaCredito(permisoVentaCredito);
                        
                        empleadoDto.setTrabajarFueraLinea(trabajarFueraLinea);
                        empleadoDto.setClientesCodigoBarras(clientesBarras);
                        empleadoDto.setPrecioDeCompra(precioCompra);
                        empleadoDto.setDistanciaObligatorio(distanciaObligaEmple);
                        empleadoDto.setPermisoAccionesCliente(permisoAccionesClientes);
                        //Asignamos horario
                        empleadoDto.setIdHorario(idHorario);
                        empleadoDto.setIdFolioMovilEmpleado(idFoliosMovilEmpleado);
                        empleadoDto.setIntervaloUbicacionSeg(intervaloUbicacionSegundos);
                        
                        empleadoDto.setPermisoDevoluciones(permisoDevoluciones);
                        empleadoDto.setPermisoAutoServInventario(permisoAutoServicioInventario);
                        
                        empleadoDto.setPermisoNoCobroParcial(noCobroParcial);
                        empleadoDto.setPermisoVerProveedores(verProveedores);

                        Empresa empresaLocal = new Empresa();
                        //EmpresaBO empresaBOPadre = new EmpresaBO(user.getConn());
                        try{
                            empresaLocal = empresaBOPadre.findEmpresabyId(idEmpresa);
                            empleadoDto.setLatitud(empresaLocal.getLatitud());
                            empleadoDto.setLongitud(empresaLocal.getLongitud());
                        }catch(Exception ex){}

                        //DATOS PARA LA TABLA DATOS_USUARIO
                        DatosUsuario datosUsuario = new DatosUsuario();
                        DatosUsuarioDaoImpl datosUsuarioDaoImpl = new DatosUsuarioDaoImpl(user.getConn()); 

                        DatosUsuario ultimoRegistroDatosUsuarios = datosUsuarioDaoImpl.findLast();
                        int idDatosUsuarioNuevo = ultimoRegistroDatosUsuarios.getIdDatosUsuario() + 1;

                        datosUsuario.setIdDatosUsuario(idDatosUsuarioNuevo);
                        datosUsuario.setNombre(nombreEmpleado);
                        datosUsuario.setApellidoPat(apellidoPaternoEmpleado);
                        datosUsuario.setApellidoMat(apellidoMaternoEmpleado);
                        datosUsuario.setDireccion(direccionEmpleado);
                        datosUsuario.setTelefono(telefonoEmpleado);

                        DatosUsuarioPk datosUsuarioPk = datosUsuarioDaoImpl.insert(datosUsuario);//INSERTAMOS EL REGISTRO DE DATOS USUARIO Y RECUPERAMOS EL OBJETO PARA TENER EL ID ASIGNADO               

                        //DATOS PARA LA TABLA USUARIO
                        Usuarios usuario = new Usuarios();
                        UsuariosDaoImpl usuariosDaoImpl = new UsuariosDaoImpl(user.getConn());

                        Usuarios ultimoRegistroUsuarios = usuariosDaoImpl.findLast();
                        int idUsuariosNuevo = ultimoRegistroUsuarios.getIdUsuarios() + 1;

                        usuario.setIdUsuarios(idUsuariosNuevo);
                        if(idSucursalEmpresaAsignado<0)
                            usuario.setIdEmpresa(idEmpresa);
                        else
                            usuario.setIdEmpresa(idSucursalEmpresaAsignado);                 
                        //usuario.setIdEmpresa(idEmpresa);
                        usuario.setIdDatosUsuario(datosUsuarioPk.getIdDatosUsuario());
                        usuario.setIdEstatus(1);
                        //usuario.setTspRoleIdRole(idRolEmpleado);
                        usuario.setIdRoles(idRolEmpleado);
                        usuario.setUserName(usuarioEmpleado);
                        usuario.setFechaAlta(new Date());

                        UsuariosPk usuarioPk = usuariosDaoImpl.insert(usuario);//INSERTAMOS EL REGISTRO DE USAURIO Y RECUPERAMOS EL ID GENERADO DE USUARIO
                        empleadoDto.setIdUsuarios(usuarioPk.getIdUsuarios());

                        //DATOS PARA LA TABLA LDAP
                        Ldap ldap = new Ldap();
                        LdapDaoImpl ldapDaoImpl = new LdapDaoImpl(user.getConn());
                        //ldap.setIdUsuarios(usuarioPk.getIdUsuarios());
                        ldap.setUsuario(usuarioEmpleado);

                        Encrypter encriptacion = new Encrypter();//ENCRIPTAMOS EL PASS
                        encriptacion.setMd5(true);
                        ldap.setPassword(encriptacion.encodeString2(contrasenaEmpleado));
                        ldap.setEmail(emailEmpleado);
                        ldap.setFirmado(1);
                        //ldap.setRecordatorioContrasena(recordatorioEmpleado);                
                        ldapDaoImpl.insert(ldap);

                        //Si selecciono un dispositivo Movil lo actualizamos a asignado.                                
                        if(idDispositivoMovilEmpleado>0){
                            DispositivoMovilBO movilBO = new DispositivoMovilBO(idDispositivoMovilEmpleado,user.getConn());
                            DispositivoMovil movil = new DispositivoMovil();
                            movil = movilBO.getDispositivoMovil();
                            short x = 1;
                            movil.setAsignado(x);
                            DispositivoMovilDaoImpl dispositivoMovilDaoImpl = new DispositivoMovilDaoImpl(user.getConn());
                            try{
                                dispositivoMovilDaoImpl.update(movil.createPk(), movil);
                            }catch(Exception ex){
                                out.print("<!--ERROR-->No se pudo actualizar el registro del movil. Informe del error al administrador del sistema: " + ex.toString());
                                ex.printStackTrace();
                            }                          
                        }

                        //VALIDAMOS QUE TENGA LICENCIAS PARA INSERTAR UN NUEVO EMPLEADO
                        //Empresa empresa = new Empresa();
                        //EmpresaBO empresaBO = new EmpresaBO(user.getConn());
                        //empresa = empresaBO.findEmpresabyId(idEmpresa);

                        Empresa empresaMatriz = new Empresa();
                        EmpresaBO empresaMatrizBO = new EmpresaBO(user.getConn());
                        empresaMatriz = empresaMatrizBO.findEmpresabyId(empresaDatosPadre .getIdEmpresaPadre());                
                        //if(empresaMatriz.getNumLicenciasDisponibles()>0){
                            /**
                            * Realizamos el insert
                            */
                            empleadosDaoImpl.insert(empleadoDto);
                            //ACTUALIZAMOS EL NUMERO DE LICENCIAS DISPONIBLES
                            //empresaMatriz.setNumLicenciasDisponibles((empresaMatriz.getNumLicenciasDisponibles()-1));                    
                            //new EmpresaDaoImpl(user.getConn()).update(empresaMatriz.createPk(), empresaMatriz);


                            
                            //Si hay un valor valido en tiempoMinutosActualiza, creamos o modificamos el registro de Parametros Estatus
                            if (tiempoMinutosActualiza>0){
                                PosMovilEstatusParametrosDaoImpl pmepDao = new PosMovilEstatusParametrosDaoImpl(user.getConn());
                                PosMovilEstatusParametros posMovilEstatusParametros = null;
                                PosMovilEstatusParametros[] coinPosMovilEstatusParametros = pmepDao.findWhereIdEmpleadoEquals(empleadoDto.getIdEmpleado());
                                if (coinPosMovilEstatusParametros.length>0)
                                    posMovilEstatusParametros =  coinPosMovilEstatusParametros[0];

                                if (posMovilEstatusParametros==null)
                                    posMovilEstatusParametros = new PosMovilEstatusParametros();

                                posMovilEstatusParametros.setIdEmpresa(idEmpresa);
                                posMovilEstatusParametros.setIdEmpleado(empleadoDto.getIdEmpleado());
                                posMovilEstatusParametros.setTiempoMinutosActualiza(tiempoMinutosActualiza);
                                posMovilEstatusParametros.setTMinutosRecordatorio(tiempoMinutosRecordatorio);

                                if (posMovilEstatusParametros.getIdEstatusParametro()>0){
                                    pmepDao.update(posMovilEstatusParametros.createPk(), posMovilEstatusParametros);
                                }else{
                                    pmepDao.insert(posMovilEstatusParametros);
                                }
                            }

                            ///***************Cliente Vendedor Consigna
                           if(ventaConsigna == 1){
                               try{ 
                                     //nuevo express
                                    Cliente clienteDto = new Cliente();
                                    ClienteDaoImpl clientesDaoImpl = new ClienteDaoImpl(user.getConn());

                                    int idClienteNuevo;
                                    Cliente ultimoRegistroClientes = clientesDaoImpl.findLast();
                                    idClienteNuevo = ultimoRegistroClientes.getIdCliente() + 1;
                                    clienteDto.setIdCliente(idClienteNuevo);

                                    clienteDto.setIdEstatus(3);
                                    clienteDto.setRfcCliente("AAA010101");
                                    clienteDto.setRazonSocial(nombreEmpleado + " " + apellidoPaternoEmpleado + " " +  apellidoMaternoEmpleado);
                                    clienteDto.setNombreCliente(nombreEmpleado);
                                    clienteDto.setApellidoPaternoCliente(apellidoPaternoEmpleado);
                                    clienteDto.setApellidoMaternoCliente(apellidoMaternoEmpleado);
                                    clienteDto.setCalle("Campo por llenar");
                                    clienteDto.setNumero("0");
                                    clienteDto.setNumeroInterior("");
                                    clienteDto.setCodigoPostal("11111");
                                    clienteDto.setColonia("Campo por llenar");
                                    clienteDto.setMunicipio("Campo por llenar");
                                    clienteDto.setEstado("Campo por llenar");
                                    clienteDto.setPais("México");
                                    clienteDto.setLada("01");
                                    clienteDto.setTelefono("1234567890");
                                    clienteDto.setExtension("");
                                    clienteDto.setCelular("0000000000");
                                    clienteDto.setCorreo(emailEmpleado);
                                    clienteDto.setContacto("Campo por llenar");
                                    clienteDto.setGenerico(0);
                                    clienteDto.setIdEmpresa(idEmpresa); 
                                    clienteDto.setDiasVisita("");
                                    clienteDto.setPerioricidad(0);
                                    clienteDto.setIdVendedorConsigna(empleadoDto.getIdEmpleado());//Empleado insertado

                                    /**
                                     * Realizamos el insert
                                     */
                                    clientesDaoImpl.insert(clienteDto);


                                    //out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");                   


                                }catch(Exception e){
                                    e.printStackTrace();
                                    msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
                                }
                           }


                           ///***************Cliente Vendedor Consigna

                            //Consumo de Creditos Operacion
                            try{
                                BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(user.getConn());
                                bcoBO.registraDescuento(user.getUser(), 
                                        BitacoraCreditosOperacionBO.CONSUMO_ACCION_REGISTRO_EMPLEADO, 
                                        null, 0, 0, 0, 
                                        "Registro de Empleado", null, true);
                            }catch(Exception ex){
                                ex.printStackTrace();
                            }

                            out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");                   
                        //}else{
                          //  out.print("<!--NO REGISTRADO-->No contiene licencias suficientes para crear un nuevo empleado.<br/>Le sugerimos contactar y adquirir mas licencias para crear nuevos empleados.<br/>");                   
                        //}
                    }else{                             
                        out.print("<!--ERROR-->No se permite crear mas usuarios. El límite de tu empresa Matriz son: " + empresaPermisoAplicacionDto.getMaxNumUsuarios() + " usuarios.");
                    }
               /* }else{
                    out.print("<!--ERROR-->No se permite crear mas empleados. Ya no tiene licencias disponibles.");
                }*/

               
            
            }catch(Exception e){
                e.printStackTrace();
                msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
            }
        }
    }else{
        out.print("<!--ERROR-->"+msgError);
    }
  }
     
     
  if(recargarListaInventarioEmpleadoSesion){
      if (msgError.equals("")){
  
%>
<table class="data" width="100%" cellpadding="0" cellspacing="0">
                    <thead>
                        <tr>
                            <td colspan="7"><center>Seleccionados</center></td>
                        </tr>
                    </thead>
                    <tbody>
                        
<%
        List<EmpleadoDatosInventarioAsignacionSesion> listaObjetosInventario = null;
        try{
            listaObjetosInventario = (ArrayList<EmpleadoDatosInventarioAsignacionSesion>)session.getAttribute("empleadoDatosInventarioSesion");
        }catch(Exception e){System.out.println("-------------------- NO HAY DATOS EN SESION DE INVENTARIO A GRANEL DE EMPLEADO . . .");}
        
        if(listaObjetosInventario != null){
            %>
        <tr>
            <td>Id Producto</td>
            <td>Piezas</td>                                            
            <td>Peso (Kg)</td>
            <td>Total (Kg)</td>
            <td>Acciones</td>
        </tr>
        <%    
            ConceptoBO conceptoBO = new ConceptoBO(user.getConn());
            int contadorDePosicion = 0; 
            for(EmpleadoDatosInventarioAsignacionSesion inven : listaObjetosInventario ){
                Concepto concepto = conceptoBO.findConceptobyId(inven.getIdConceptoInventario());
        %>         
        <tr>
            <td>
                <input type="text" name="producto_id_posicion<%=contadorDePosicion%>" id="producto_id_posicion<%=contadorDePosicion%>" readonly value="<%=contadorDePosicion%>"/>                                       
                <input type="text" maxlength="100" name="producto_nombre_<%=contadorDePosicion%>" id="producto_nombre_<%=contadorDePosicion%>"  readonly
                       disabled value="<%=concepto.getNombreDesencriptado() + " " +inven.getPesoArticulosInventarioAsignar() + " Kg"  %>"/>
            </td>                            
            <td>
                <input type="text" maxlength="100" name="producto_cantidad_<%=contadorDePosicion%>" id="producto_cantidad_<%=contadorDePosicion%>"  readonly
                       disabled value="<%=inven.getNumArticulosInventarioAsignar() %>"/>
            </td>
            <td>
                <input type="text" maxlength="10" name="producto_peso_<%=contadorDePosicion%>" id="producto_peso_<%=contadorDePosicion%>"   readonly
                       disabled value="<%=inven.getPesoArticulosInventarioAsignar() %>"/>
            </td>
            <td>
                <input type="text" maxlength="10" name="producto_peso_total_<%=contadorDePosicion%>" id="producto_peso_total_<%=contadorDePosicion%>"   readonly
                       disabled value="<%= (inven.getNumArticulosInventarioAsignar() * inven.getPesoArticulosInventarioAsignar()) %>"/>
            </td>
            <td>
                <a href="javascript:void(0);" onclick="subProducto(<%=contadorDePosicion%>);" id="sub_producto_action_<%=contadorDePosicion%>" title="Quitar Producto">
                    <img src="../../images/minus.png" alt="Quitar Producto" height="20" width="20" title="Quitar Producto" class="help"/>
                </a>
                &nbsp;&nbsp;                                
            </td>
        </tr>        
        
        <%
        contadorDePosicion++;
            }
        }
        %>
        <tr>        
            <td>
                <input type="button" id="enviar" value="Guardar" onclick="grabar('agregar');"/>
            </td>        
        </tr>
        <tr></br></tr>
        <tr></br></tr>
        </tbody>
        </table>
        <%
      }
}
%> 
