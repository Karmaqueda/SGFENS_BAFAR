<%-- 
    Document   : mapaBuscador_ajax
    Created on : 8/01/2013, 06:18:58 PM
    Author     : Luis
--%>

<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.factory.VistaPedidosConAdeudosDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.VistaPedidosConAdeudos"%>
<%@page import="com.tsp.sct.dao.factory.SgfensProspectoDaoFactory"%>
<%@page import="com.tsp.sct.dao.factory.EmpleadoDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.Empleado"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProspecto"%>
<%@page import="com.tsp.sct.dao.factory.ClienteDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    /*
    * Datos a buscar
    */
    String txtBuscar = request.getParameter("txt_buscar")!=null?request.getParameter("txt_buscar"):"";
    String txtTipoBuscar = request.getParameter("cmb_tipo_buscar");
    
    ///**Dias de visita
    String domingoReporte = request.getParameter("domingoReporte")!=null?new String(request.getParameter("domingoReporte").getBytes("ISO-8859-1"),"UTF-8"):""; 
    String lunesReporte = request.getParameter("lunesReporte")!=null?new String(request.getParameter("lunesReporte").getBytes("ISO-8859-1"),"UTF-8"):""; 
    String martesReporte = request.getParameter("martesReporte")!=null?new String(request.getParameter("martesReporte").getBytes("ISO-8859-1"),"UTF-8"):""; 
    String miercolesReporte = request.getParameter("miercolesReporte")!=null?new String(request.getParameter("miercolesReporte").getBytes("ISO-8859-1"),"UTF-8"):""; 
    String juevesReporte = request.getParameter("juevesReporte")!=null?new String(request.getParameter("juevesReporte").getBytes("ISO-8859-1"),"UTF-8"):""; 
    String viernesReporte = request.getParameter("viernesReporte")!=null?new String(request.getParameter("viernesReporte").getBytes("ISO-8859-1"),"UTF-8"):""; 
    String sabadoReporte = request.getParameter("sabadoReporte")!=null?new String(request.getParameter("sabadoReporte").getBytes("ISO-8859-1"),"UTF-8"):"";     
    ///**
    
    long idPromotor = 0;
    try{
        idPromotor = Long.parseLong(request.getParameter("cmb_promotor_buscar"));
    }catch(Exception e){}
    
    Cliente[] clientesDto = new Cliente[0];
    SgfensProspecto[] prospectosDto = new SgfensProspecto[0];
    Empleado[] promotoresDto = new Empleado[0];
    VistaPedidosConAdeudos[] cobranzaDto = new VistaPedidosConAdeudos[0];
            
    /*Automovil[] vehiculosDto = new Automovil[0];
    PuntosInteres[] puntosDto = new PuntosInteres[0];*/
    
    if(txtTipoBuscar.equals("todos") || txtTipoBuscar.equals("clientes")){
         System.out.println("clientes") ;
         
         ///**armamos query de consulta por días:
         String diasVisita = "";
         if(!lunesReporte.trim().equals("") || !martesReporte.trim().equals("") || !miercolesReporte.trim().equals("") || !juevesReporte.trim().equals("")
                 || !viernesReporte.trim().equals("") || !sabadoReporte.trim().equals("") || !domingoReporte.trim().equals("")){
         diasVisita = " AND (DIAS_VISITA LIKE '%TODOS%' ";                
            if(!lunesReporte.trim().equals(""))
                diasVisita += " OR DIAS_VISITA LIKE '%"+lunesReporte+"%' ";
            if(!martesReporte.trim().equals(""))
                diasVisita += " OR DIAS_VISITA LIKE '%"+martesReporte+"%' ";
            if(!miercolesReporte.trim().equals(""))
                diasVisita += " OR DIAS_VISITA LIKE '%"+miercolesReporte+"%' ";
            if(!juevesReporte.trim().equals(""))
                diasVisita += " OR DIAS_VISITA LIKE '%"+juevesReporte+"%' ";
            if(!viernesReporte.trim().equals(""))
                diasVisita += " OR DIAS_VISITA LIKE '%"+viernesReporte+"%' ";
            if(!sabadoReporte.trim().equals(""))
                diasVisita += " OR DIAS_VISITA LIKE '%"+sabadoReporte+"%' ";
            if(!domingoReporte.trim().equals(""))
                diasVisita += " OR DIAS_VISITA LIKE '%"+domingoReporte+"%' ";
            
            diasVisita += ") ";
         }
         ///**
        String filtroClientes = "";
        filtroClientes += ""
            + "(RAZON_SOCIAL LIKE '%"+txtBuscar+"%' OR "
            + "NOMBRE_CLIENTE LIKE '%"+txtBuscar+"%' OR "
            + "APELLIDO_PATERNO_CLIENTE LIKE '%"+txtBuscar+"%' OR "
            + "APELLIDO_MATERNO_CLIENTE LIKE '%"+txtBuscar+"%') AND "
            + "(LATITUD IS NOT NULL AND LATITUD <> '0' AND LATITUD <> 'null' AND LATITUD <> '') AND "
            + "(LONGITUD IS NOT NULL AND LONGITUD <> '0' AND LONGITUD <> 'null' AND LONGITUD <> '') AND ("
            + "ID_ESTATUS = 1 OR ID_ESTATUS = 3) AND ID_EMPRESA = ? "
            + diasVisita  ;
        
        if(idPromotor>0){
            filtroClientes += " AND  ID_CLIENTE IN ( SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR = "+ idPromotor + ") ";
        }
        
        clientesDto = ClienteDaoFactory.create(user.getConn()).findByDynamicWhere(  filtroClientes  , new Object[]{user.getUser().getIdEmpresa()});
    }
    
    if(txtTipoBuscar.equals("todos") || txtTipoBuscar.equals("prospectos")){
        System.out.println("prospectos") ;
        prospectosDto = SgfensProspectoDaoFactory.create(user.getConn()).findByDynamicWhere(""
            + "(CONTACTO LIKE '%"+txtBuscar+"%' OR "
            + "DESCRIPCION LIKE '%"+txtBuscar+"%') AND "
            + "(LATITUD IS NOT NULL AND LATITUD <> 0) AND "
            + "(LONGITUD IS NOT NULL AND LONGITUD <> 0) AND "
            + "ID_ESTATUS = 1 AND ID_EMPRESA = ? "
            //+ (idPromotor>0?"AND ID_EMPLEADO_PROMOTOR = " + idPromotor:""), new Object[]{user.getUser().getIdEmpresa()});
            , new Object[]{user.getUser().getIdEmpresa()});
    }
    
    if(txtTipoBuscar.equals("todos") || txtTipoBuscar.equals("promotores")){
        System.out.println("promotores") ;
        promotoresDto = EmpleadoDaoFactory.create(user.getConn()).findByDynamicWhere(""
            + "(NOMBRE LIKE '%"+txtBuscar+"%' OR "
            + "APELLIDO_PATERNO LIKE '%"+txtBuscar+"%' OR "
            + "APELLIDO_MATERNO LIKE '%"+txtBuscar+"%') AND "
            + "ID_ESTATUS = 1 AND ID_EMPRESA = ?", new Object[]{user.getUser().getIdEmpresa()});
    }
    
    
    if(txtTipoBuscar.equals("todos") || txtTipoBuscar.equals("cobranza")){
        System.out.println("cobranza") ;
        
        String filtroClientes = "";
        filtroClientes += ""
            + " ID_CLIENTE IN "
            + "("
            + "SELECT ID_CLIENTE FROM CLIENTE WHERE "
            + "(LATITUD IS NOT NULL AND LATITUD <> '0' AND LATITUD <> 'null' AND LATITUD <> '') AND "
            + "(LONGITUD IS NOT NULL AND LONGITUD <> '0' AND LONGITUD <> 'null' AND LONGITUD <> '') "
            + "AND "
            + "(NOMBRE_CLIENTE LIKE '%"+txtBuscar+"%' OR "
            + "APELLIDO_PATERNO_CLIENTE LIKE '%"+txtBuscar+"%' OR "
            + "APELLIDO_MATERNO_CLIENTE LIKE '%"+txtBuscar+"%') "  
            + ")"
            + " AND ID_EMPRESA = ?  ";
                
                
        
        if(idPromotor>0){
            filtroClientes += " AND  ID_CLIENTE IN ( SELECT ID_CLIENTE FROM sgfens_cliente_vendedor WHERE ID_USUARIO_VENDEDOR = "+ idPromotor + ") ";
        } 
        
        filtroClientes += " GROUP BY ID_CLIENTE";
        
        cobranzaDto = VistaPedidosConAdeudosDaoFactory.create(user.getConn()).findByDynamicWhere( filtroClientes ,new Object[]{user.getUser().getIdEmpresa()});        
    }
    /*if(txtTipoBuscar.equals("todos") || txtTipoBuscar.equals("vehiculos")){
        System.out.println("vehiculos") ;
        vehiculosDto = AutomovilDaoFactory.create().findByDynamicSelect(""
            + "SELECT AUTOMOVIL.*  "
            + "FROM AUTOMOVIL "
            + "INNER JOIN EMPLEADO "
            + "ON AUTOMOVIL.ID_AUTOMOVIL = EMPLEADO.ID_VEHICULO "
            + "WHERE "
            + "(CODIGO LIKE '%"+txtBuscar+"%' OR "
            + "PLACAS LIKE '%"+txtBuscar+"%' OR "
            + "MARCA LIKE '%"+txtBuscar+"%' OR "
            + "MODELO LIKE '%"+txtBuscar+"%') AND "
            + "AUTOMOVIL.ID_EMPRESA = ?", new Object[]{user.getUser().getIdEmpresa()});
    }*/
    
    /*if(txtTipoBuscar.equals("todos") || txtTipoBuscar.equals("puntos")){
        System.out.println("puntos") ;
        puntosDto = PuntosInteresDaoFactory.create().findByDynamicWhere(""
            + "(NOMBRE LIKE '%"+txtBuscar+"%' OR "
            + "DESCRIPCION LIKE '%"+txtBuscar+"%') AND "
            + "ID_EMPRESA = ? ", new String[]{""+user.getUser().getIdEmpresa()});
    }*/
    
    if(clientesDto.length>0 || prospectosDto.length>0 || promotoresDto.length>0 || cobranzaDto.length>0 ){
    
        String html = "<table class=\"data\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">"
                    + "  <thead>"
                    + "        <tr>"
                    + "            <th>Tipo</th>"
                    + "            <th>Descripci&oacute;n</th>"
                    + "            <th>Ubicar</th>"
                    + "        </tr>"
                    + "    </thead>";

        html +=  "    <tbody>";

        for(Cliente clienteDto:clientesDto){
            String nombreCliente = ""; 
            
            if(clienteDto.getRazonSocial() != null && !clienteDto.getRazonSocial().trim().equals("")){
                nombreCliente = clienteDto.getRazonSocial().trim();
            }else{
                if((clienteDto.getApellidoPaternoCliente()!=null)&&(!clienteDto.getApellidoPaternoCliente().toUpperCase().equals("NULL"))&&(!clienteDto.getApellidoPaternoCliente().toUpperCase().equals("CAMPO POR LLENAR"))){
                    nombreCliente += "" + clienteDto.getApellidoPaternoCliente();
                }
                if((clienteDto.getApellidoMaternoCliente()!=null)&&(!clienteDto.getApellidoMaternoCliente().toUpperCase().equals("NULL"))&&(!clienteDto.getApellidoMaternoCliente().toUpperCase().equals("CAMPO POR LLENAR"))){
                    nombreCliente += " " + clienteDto.getApellidoMaternoCliente();
                }
                if(clienteDto.getNombreCliente()!=null){
                    nombreCliente += " " +clienteDto.getNombreCliente();
                }
            }
                    
            html += "<tr>"
                + "<td>"
                + "Cliente"
                + "</td>"
                + "<td>"
                + nombreCliente
                + "</td>"
                + "<td>"
                + "<a href='javascript:void(0)' "
                + "onclick='muestraMarcadorBusqueda("+clienteDto.getLatitud()+","+clienteDto.getLongitud()+",\"clientes\","+clienteDto.getIdCliente()+")'> "
                + "<img src='../../images/icon_movimiento.png' alt='Ubicar'/></a> "
                + "</td>"
                + "<tr>";
        }
        
        for(SgfensProspecto prospectoDto:prospectosDto){
            html += "<tr>"
                + "<td>"
                + "Prospecto"
                + "</td>"
                + "<td>"
                + prospectoDto.getContacto()
                + "</td>"
                + "<td>"
                + "<a href='javascript:void(0)' "
                + "onclick='muestraMarcadorBusqueda("+prospectoDto.getLatitud()+","+prospectoDto.getLongitud()+",\"prospectos\","+prospectoDto.getIdProspecto()+")'> "
                + "<img src='../../images/icon_movimiento.png' alt='Ubicar'/></a> "
                + "</td>"
                + "<tr>";
        }
        
        for(Empleado promotorDto:promotoresDto){
            if((promotorDto.getIdMovilEmpleadoRol() == 2) || (promotorDto.getIdMovilEmpleadoRol() == 25) || (promotorDto.getIdMovilEmpleadoRol() == 31)
                    || (promotorDto.getIdMovilEmpleadoRol() == 32) || (promotorDto.getIdMovilEmpleadoRol() == 26)){
            html += "<tr>"
                + "<td>";
                if(promotorDto.getIdMovilEmpleadoRol() == 2 || promotorDto.getIdMovilEmpleadoRol() == 25)
                    html += "Vendedor";
                else if(promotorDto.getIdMovilEmpleadoRol() == 31)
                    html += "Conductor Vendedor";
                else if(promotorDto.getIdMovilEmpleadoRol() == 32)
                    html += "Cobrador";
                else if(promotorDto.getIdMovilEmpleadoRol() == 26)
                    html += "Conductor";
            
           html += "<td>"
                + promotorDto.getApellidoPaterno() + " " + promotorDto.getApellidoMaterno() + " " + promotorDto.getNombre()
                + "</td>"
                + "<td>"
                + "<a href='javascript:void(0)' "
                + "onclick='muestraMarcadorBusqueda("+promotorDto.getLatitud()+","+promotorDto.getLongitud()+",\"promotores\","+promotorDto.getIdEmpleado()+")'> "
                + "<img src='../../images/icon_movimiento.png' alt='Ubicar'/></a> "
                + "</td>"
                + "<tr>";
            }
            
        }
        
        
        for(VistaPedidosConAdeudos cobroDto:cobranzaDto){       
            
            Cliente clienteDto = new ClienteBO(user.getConn()).findClientebyId(cobroDto.getIdCliente());
            String nombreCliente = ""; 
            if((clienteDto.getApellidoPaternoCliente()!=null)&&(!clienteDto.getApellidoPaternoCliente().toUpperCase().equals("NULL"))&&(!clienteDto.getApellidoPaternoCliente().toUpperCase().equals("CAMPO POR LLENAR"))){
                nombreCliente += "" + clienteDto.getApellidoPaternoCliente();
            }
            if((clienteDto.getApellidoMaternoCliente()!=null)&&(!clienteDto.getApellidoMaternoCliente().toUpperCase().equals("NULL"))&&(!clienteDto.getApellidoMaternoCliente().toUpperCase().equals("CAMPO POR LLENAR"))){
                nombreCliente += " " + clienteDto.getApellidoPaternoCliente();
            }
            if(clienteDto.getNombreCliente()!=null){
                nombreCliente += " " +clienteDto.getNombreCliente();
            }
            
            html += "<tr>"
                + "<td>"
                + "Cobranza"
                + "</td>"
                + "<td>"
                + nombreCliente
                + "</td>"
                + "<td>"
                + "<a href='javascript:void(0)' "
                + "onclick='muestraMarcadorBusqueda("+clienteDto.getLatitud()+","+clienteDto.getLongitud()+",\"clientes\","+clienteDto.getIdCliente()+")'> "
                + "<img src='../../images/icon_movimiento.png' alt='Ubicar'/></a> "
                + "</td>"
                + "<tr>";
            
        }
        
      /*  for(Automovil vehiculoDto:vehiculosDto){
            //Empleado[] empleadoDto = EmpleadoDaoFactory.create().findByDynamicSelect(""
            //    + "SELECT EMPLEADO.* "
            //    + "FROM EMPLEADO "
            //    + "INNER JOIN EMPLEADO_AUTOMOVIL "
            //    + "ON EMPLEADO.ID_EMPLEADO = EMPLEADO_AUTOMOVIL.ID_EMPLEADO "
            //    + "AND EMPLEADO_AUTOMOVIL.ID_AUTOMOVIL = ? "
            //    + "", new Object[]{vehiculoDto.getIdAutomovil()});
			Empleado[] empleadoDto = EmpleadoDaoFactory.create().findByDynamicWhere("ID_VEHICULO = ?",new Object[]{vehiculoDto.getIdAutomovil()});
            if(empleadoDto.length>0){
                html += "<tr>"
                    + "<td>"
                    + "Veh&iacute;culo"
                    + "</td>"
                    + "<td>"
//                    + (vehiculoDto.getDescripcion()!=null?vehiculoDto.getDescripcion() + "<br/>":"")
                    + vehiculoDto.getMarca() + "<br/>"
//                    + vehiculoDto.getModelo() + "<br/>"
                    + vehiculoDto.getPlacas() + "<br/>"
                    + vehiculoDto.getCodigo() + "<br/>"
                    + "<td>"
                    + "<a href='javascript:void(0)' "
                    + "onclick='muestraMarcadorBusqueda("+empleadoDto[0].getLatitud()+","+empleadoDto[0].getLongitud()+",\"vehiculos\","+vehiculoDto.getIdAutomovil()+")'> "
                    + "<img src='../../images/icon_movimiento.png' alt='Ubicar'/></a> "
                    + "</td>"
                    + "</td>"
                    + "<tr>";
            }
        }*/
        
      /*  for(PuntosInteres puntoDto:puntosDto){
            html += "<tr>"
                + "<td>"
                + "Puntos de inter&eacute;s"
                + "</td>"
                + "<td>"
                + puntoDto.getNombre() + "<br/>"
                + (puntoDto.getDescripcion()!=null?puntoDto.getDescripcion() + "<br/>":"")
                + "</td>"
                + "<td>"
                + "<a href='javascript:void(0)' "
                + "onclick='muestraMarcadorBusqueda("+puntoDto.getLatitud()+","+puntoDto.getLongitud()+",\"puntos\","+puntoDto.getIdPunto()+")'> "
                + "<img src='../../images/icon_movimiento.png' alt='Ubicar'/></a> "
                + "</td>"
                + "<tr>";
        }*/

        html +=  "    </tbody>"
             + "</table><!--EXITO-->";
        
        out.print(html);
        
    }else{
        out.print("<!--ERROR-->No se encontraron registros.");
    }
    
%>