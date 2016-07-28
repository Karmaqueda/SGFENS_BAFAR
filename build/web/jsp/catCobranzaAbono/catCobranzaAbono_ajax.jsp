<%-- 
    Document   : catCobranzaAbono_ajax
    Created on : 07-ene-2013, 13:28:43
    Author     : ISCesarMartinez
--%>

<%@page import="com.tsp.sct.bo.ZonaHorariaBO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.dao.jdbc.ClienteDaoImpl"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensPedidoDaoImpl"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensCobranzaAbonoDaoImpl"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.dto.SgfensCobranzaAbono"%>
<%@page import="com.tsp.sct.bo.SGCobranzaAbonoBO"%>
<%@page import="com.tsp.sct.bo.ComprobanteFiscalBO"%>
<%@page import="com.tsp.sct.bo.SGPedidoBO"%>
<%@page import="com.tsp.sct.dao.dto.ComprobanteFiscal"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedido"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String msgError="";
    /*
    *   Modos:
    *       -1  =  No se eligio (ERROR)
    *        1  =  Guardar Nuevo Abono
    *        2  =  
    */
    int mode=-1;
    try{
        mode = Integer.parseInt(request.getParameter("mode"));
    }catch(NumberFormatException e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    GenericValidator genericValidator = new GenericValidator();
    
    /*
     * Procesamiento
     */
    if (mode==-1){
        msgError = "No se eligio una acción correcta. Reintente el llenado de los datos.";
    }else if (mode==1){
        //1 = Guardar Nuevo Abono
        
        //Parametros
        int idPedido = -1;
        int idComprobanteFiscal = -1;
        double monto = 0;
        int idMetodoPago =-1;
        String identificadorOperacion = request.getParameter("identificador_operacion")!=null? new String(request.getParameter("identificador_operacion").getBytes("ISO-8859-1"),"UTF-8") :"";
        String comentarios = request.getParameter("comentarios")!=null? new String(request.getParameter("comentarios").getBytes("ISO-8859-1"),"UTF-8") :"";
        
        try{ idPedido = Integer.parseInt(request.getParameter("idPedido")); }catch(Exception e){}
        try{ idComprobanteFiscal = Integer.parseInt(request.getParameter("idComprobanteFiscal")); }catch(Exception e){}
        try{ monto = Double.parseDouble(request.getParameter("monto")); }catch(Exception e){}
        try{ idMetodoPago = Integer.parseInt(request.getParameter("id_metodo_pago")); }catch(Exception e){}
        
        Date cobranza_fecha_generacion = null;
        try{ cobranza_fecha_generacion = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("cobranza_fecha_generacion"));}catch(Exception e){}
        
        //Validaciones
        if (idPedido<=0 && idComprobanteFiscal<=0)
            msgError = "<ul>No se indico correctamente a que Pedido o Factura aplicará el abono.";
        if (idMetodoPago<=0)
            msgError = "<ul>Debe seleccionar el Método de Pago, es obligatorio.";
        if (monto<=0)
            msgError = "<ul>El abono debe ser mayor a 0.";
        if (!genericValidator.isValidString(identificadorOperacion, 0, 100))
            msgError = "<ul>El identificador de la operación debe tener máximo 100 caracteres.";
        if (!genericValidator.isValidString(comentarios, 0, 250))
            msgError = "<ul>El campo 'comentarios' debe tener máximo 250 caracteres.";
        
        if (msgError.equals("")){
           boolean saldoSuficiente = true;
            try{
                SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(user.getConn());
                SgfensPedido pedidoDto = new SGPedidoBO(idPedido,user.getConn()).getPedido();
                ComprobanteFiscal comprobanteFiscalDto = new ComprobanteFiscalBO(idComprobanteFiscal, user.getConn()).getComprobanteFiscal();
                Cliente  clienteDto = null;
                if (pedidoDto!=null){
                    clienteDto = new ClienteBO(pedidoDto.getIdCliente(),user.getConn()).getCliente();
                }else if (comprobanteFiscalDto!=null){
                    clienteDto = new ClienteBO(comprobanteFiscalDto.getIdCliente(),user.getConn()).getCliente();
                }
                
                if(idMetodoPago==6){                    
                    
                    if(monto>clienteDto.getSaldoCliente()){
                        saldoSuficiente = false;
                        msgError+="<ul>Saldo del cliente Insuficiente.";
                    }
                    
                }
                
                if(saldoSuficiente){    
                    cobranzaAbonoBO.registrarAbono(user.getUser(), idPedido, 
                            idComprobanteFiscal, monto, idMetodoPago, 
                            identificadorOperacion, comentarios,
                            0, //En este parametro va el ID en caso de operacion bancaria (pago con TDC)
                            0,0, //Latitud,Longitud
                            null,//Archivo de imagen de firma
                            null, //Folio de cobranza movil - No aplica en consola
                            //new Date(), //Fecha y hr de creación de la cobranza (actual)
                            cobranza_fecha_generacion, //Fecha y hr de creación de la cobranza (actual)
                            true,
                            identificadorOperacion,
                            (ZonaHorariaBO.DateZonaHorariaByIdEmpresa(user.getConn(), new Date(), (int)idEmpresa).getTime()) ); //Registro capturado en consola (true)
                    
                    if(idMetodoPago==6){
                        double nuevoSaldoCliente = clienteDto.getSaldoCliente() - monto;
                        clienteDto.setSaldoCliente(nuevoSaldoCliente);                        
                        new ClienteDaoImpl(user.getConn()).update(clienteDto.createPk(), clienteDto);
                    }
                    
                }
                
            }catch(Exception ex){
                msgError+="<ul>Error al registrar abono: " + ex.toString();
            }
            
            if (msgError.equals(""))
                out.print("<!--EXITO-->El abono ha sido registrado exitosamente.");
        }
        
    }else if(mode == 2){
        
        //Parametros
        int idPedido = -1;
        
        try{ idPedido = Integer.parseInt(request.getParameter("idPedido")); }catch(Exception e){}
        
        //Validaciones
        if (idPedido<=0)
            msgError = "<ul>El pedido no existe.";
        
        if (msgError.equals("")){
            String strHTML = "" ;
            try{                
                
                SgfensPedido pedidoDto = new SGPedidoBO(idPedido,user.getConn()).getPedido();
                Cliente  clienteDto = new ClienteBO(pedidoDto.getIdCliente(),user.getConn()).getCliente();
                
                strHTML+= "<p>"
                       +"<label>"
                       +"Saldo actual a favor del cliente:"
                       +"</label>"
                       +"</br>"
                       +"<input type='text' "
                       +"value ='"+clienteDto.getSaldoCliente()+"' "  
                       +"style='width:300px' "
                       +"readonly />"
                       +"</p>"
                       +"</br>";
                out.print("<!--EXITO-->" + strHTML);
                
            }catch(Exception ex){
                msgError+="<ul>Error al encontrar información del pedido." + ex.toString();
            }            
            
        }
        
    }
    
    if (msgError.equals("") && mode!=1){
        out.print("<!--EXITO-->");
    }
    
    if (!msgError.equals("")){
        out.print("<!--ERROR-->"+msgError);
    }
 %>
