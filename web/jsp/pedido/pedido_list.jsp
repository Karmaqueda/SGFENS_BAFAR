<%-- 
    Document   : catPedidos_list.jsp
    Created on : 13-dic-2012, 12:13:49
    Author     : ISCesarMartinez poseidon24@hotmail.com
--%>


<%@page import="com.tsp.sct.bo.ParametrosBO"%>
<%@page import="com.tsp.sct.dao.dto.ClienteCategoria"%>
<%@page import="com.tsp.sct.bo.ClienteCategoriaBO"%>
<%@page import="java.lang.Math.*"%>
<%@page import="com.tsp.sct.dao.jdbc.SgfensPedidoProductoDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedidoProducto"%>
<%@page import="com.tsp.sct.bo.RolAutorizacionBO"%>
<%@page import="com.tsp.sct.bo.SGCobranzaAbonoBO"%>
<%@page import="com.tsp.sct.dao.dto.SgfensCobranzaAbono"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.tsp.sgfens.report.ReportBO"%>
<%@page import="com.tsp.sct.bo.SGEstatusPedidoBO"%>
<%@page import="com.tsp.sct.bo.UsuariosBO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.bo.RolesBO"%>
<%@page import="com.tsp.sct.bo.ClienteBO"%>
<%@page import="com.tsp.sct.dao.dto.Cliente"%>
<%@page import="com.tsp.sct.bo.UsuarioBO"%>
<%@page import="com.tsp.sct.dao.dto.DatosUsuario"%>
<%@page import="com.tsp.sct.util.DateManage"%>
<%@page import="com.tsp.sct.dao.dto.SgfensPedido"%>
<%@page import="com.tsp.sct.bo.SGPedidoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="pedidoSesion" scope="session" class="com.tsp.sgfens.sesion.PedidoSesion"/>
<%
//Verifica si el usuario tiene acceso a este topico
if (user == null || !user.permissionToTopicByURL(request.getRequestURI().replace(request.getContextPath(), ""))) {
    response.sendRedirect("../../jsp/inicio/login.jsp?action=loginRequired&urlSource=" + request.getRequestURI() + "?" + request.getQueryString());
    response.flushBuffer();
} else {
    
    pedidoSesion = null;
    request.getSession().setAttribute("pedidoSesion", pedidoSesion);
    
    NumberFormat formatMoneda = new DecimalFormat("###,###,###,##0.00");
    String infoTitle  = "";
    
    String buscar = request.getParameter("q")!=null? new String(request.getParameter("q").getBytes("ISO-8859-1"),"UTF-8") :"";
    String buscar_idcliente = request.getParameter("q_idcliente")!=null?request.getParameter("q_idcliente"):"";
    int idClienteCategoria = -1;
    try{
        idClienteCategoria = Integer.parseInt(request.getParameter("idClienteCategoria"));
    }catch(NumberFormatException ex){}
    String buscar_idvendedor = request.getParameter("q_idvendedor")!=null?request.getParameter("q_idvendedor"):"";
    String buscar_idestatuspedido = request.getParameter("q_idestatuspedido")!=null?request.getParameter("q_idestatuspedido"):"";
    String buscar_adeudo = request.getParameter("q_adeudo")!=null? new String(request.getParameter("q_adeudo").getBytes("ISO-8859-1"),"UTF-8") :""; 
    String buscar_consigna = request.getParameter("q_consigna")!=null? new String(request.getParameter("q_consigna").getBytes("ISO-8859-1"),"UTF-8") :""; 
    
    String buscar_fechamin = "";
    String buscar_fechamax = "";
    Date fechaMin=null;
    Date fechaMax=null;
    String strWhereRangoFechas="";
    String filtroBusqueda = " AND (ID_ESTATUS_PEDIDO != 5 AND ID_ESTATUS_PEDIDO != 6) ";
    String parametrosPaginacion = "";
    
    {
        try{
            fechaMin = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("q_fh_min"));
            buscar_fechamin = DateManage.formatDateToSQL(fechaMin);
        }catch(Exception e){}
        try{
            fechaMax = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("q_fh_max"));
            buscar_fechamax = DateManage.formatDateToSQL(fechaMax);
        }catch(Exception e){}

        /*Filtro por rango de fechas*/
        if (fechaMin!=null && fechaMax!=null){
            strWhereRangoFechas="(CAST(FECHA_PEDIDO AS DATE) BETWEEN '"+buscar_fechamin+"' AND '"+buscar_fechamax+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax)+"&q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin!=null && fechaMax==null){
            strWhereRangoFechas="(FECHA_PEDIDO  >= '"+buscar_fechamin+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_min="+DateManage.formatDateToNormal(fechaMin);
        }
        if (fechaMin==null && fechaMax!=null){
            strWhereRangoFechas="(FECHA_PEDIDO  <= '"+buscar_fechamax+"')";
            if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
            parametrosPaginacion+="q_fh_max="+DateManage.formatDateToNormal(fechaMax);
        }
    }
    
    
    
    //Si es vendedor, filtramos para solo mostrar sus pedidos
    if (user.getUser().getIdRoles() == RolesBO.ROL_OPERADOR)
        filtroBusqueda += " AND ID_USUARIO_VENDEDOR ='" + user.getUser().getIdUsuarios() + "' ";
        
    if (!buscar.trim().equals("")){
        filtroBusqueda += " AND (FOLIO_PEDIDO LIKE '%" + buscar + "%' OR FOLIO_PEDIDO_MOVIL LIKE '%" + buscar + "%')";
        if(!parametrosPaginacion.equals(""))
                        parametrosPaginacion+="&";
        parametrosPaginacion+="q="+buscar;
    }
    
    if (!buscar_idcliente.trim().equals("")){
        filtroBusqueda += " AND ID_CLIENTE='" + buscar_idcliente +"' ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q_idcliente="+buscar_idcliente;
        
        //Solo para dirección el reporte cuando filtra por cliente
        try{
            Cliente filtroCliDto = new ClienteBO(Integer.parseInt(buscar_idcliente),user.getConn()).getCliente();
            
            
            infoTitle  = ""+
            "DOMICILIO CLIENTE: \n" + (filtroCliDto!=null?"Calle: "+filtroCliDto.getCalle():"") + " " +
            (filtroCliDto!=null?filtroCliDto.getNumero():"") + " " + (filtroCliDto!=null?filtroCliDto.getNumeroInterior():"") +
            (filtroCliDto!=null?" Col: "+filtroCliDto.getColonia():"") +
            (filtroCliDto!=null?" \nDeleg./Municipio: " +filtroCliDto.getMunicipio():"") + 
            (filtroCliDto!=null?" Estado: " + filtroCliDto.getEstado():"") +
            (filtroCliDto!=null?" \nC.P. " + filtroCliDto.getCodigoPostal():"") +
            "\n";
            
        }catch(Exception e){}
        
    }else{
        filtroBusqueda += " AND ID_CLIENTE>=0 ";
    }
    
    if (!buscar_idvendedor.trim().equals("")){
        filtroBusqueda += " AND (ID_USUARIO_VENDEDOR='" + buscar_idvendedor +"' OR ID_USUARIO_CONDUCTOR_ASIGNADO='" + buscar_idvendedor +"' OR ID_USUARIO_VENDEDOR_ASIGNADO='" + buscar_idvendedor +"' OR ID_USUARIO_VENDEDOR_REASIGNADO='" + buscar_idvendedor +"')";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q_idvendedor="+buscar_idvendedor;
    }
    
    if (!buscar_idestatuspedido.trim().equals("")){
        filtroBusqueda += " AND ID_ESTATUS_PEDIDO='" + buscar_idestatuspedido +"' ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q_idestatuspedido="+buscar_idestatuspedido;
    }
    
    if (!strWhereRangoFechas.trim().equals("")){
        filtroBusqueda += " AND " + strWhereRangoFechas;
    }
    
        
    if (buscar_adeudo.trim().equals("0")){       
        filtroBusqueda += " AND (TOTAL - SALDO_PAGADO) <= 0 ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q_adeudo="+buscar_adeudo;
    }else if(buscar_adeudo.trim().equals("1")){       
        //filtroBusqueda += " AND (TOTAL - SALDO_PAGADO) > 0 ";
        filtroBusqueda += " AND (saldo_pagado + (CASE WHEN  BONIFICACION_DEVOLUCION<0 THEN BONIFICACION_DEVOLUCION ELSE 0 END)) < total ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q_adeudo="+buscar_adeudo;
    }
    
    if (buscar_consigna.trim().equals("0")){        
        filtroBusqueda += " AND CONSIGNA = 0 ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q_consigna="+buscar_consigna;
    }else if(buscar_consigna.trim().equals("1")){       
        //filtroBusqueda += " AND (TOTAL - SALDO_PAGADO) > 0 ";
        filtroBusqueda += " AND CONSIGNA = 1 ";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="q_consigna="+buscar_consigna;
    }
       
    if(idClienteCategoria > 0){
        filtroBusqueda += " AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM CLIENTE WHERE ID_CLIENTE_CATEGORIA = " + idClienteCategoria + ")";
        if(!parametrosPaginacion.equals(""))
                    parametrosPaginacion+="&";
        parametrosPaginacion+="idClienteCategoria="+idClienteCategoria;
    }     
    
    int idPedido = -1;
    try{ idPedido = Integer.parseInt(request.getParameter("idPedido")); }catch(NumberFormatException e){}
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Paginación
    */
    int paginaActual = 1;
    double registrosPagina = new ParametrosBO(user.getConn()).getParametroDouble("app.preto.paginacion.registrosPorPagina"); //10;
    double limiteRegistros = 0;
    int paginasTotales = 0;
    int numeroPaginasAMostrar = 5;

    try{
        paginaActual = Integer.parseInt(request.getParameter("pagina"));
    }catch(Exception e){}

    try{
        registrosPagina = Integer.parseInt(request.getParameter("registros_pagina"));
    }catch(Exception e){}
    
     SGPedidoBO pedidoBO = new SGPedidoBO(user.getConn());
     SgfensPedido[] pedidoDto = new SgfensPedido[0];
     try{
         limiteRegistros = pedidoBO.findCantidadPedido(idPedido, idEmpresa , 0, 0, filtroBusqueda);
         
          if (!buscar.trim().equals(""))
             registrosPagina = limiteRegistros;
         
         paginasTotales = (int)Math.ceil(limiteRegistros / registrosPagina);

        if(paginaActual<0)
            paginaActual = 1;
        else if(paginaActual>paginasTotales)
            paginaActual = paginasTotales;

        pedidoDto = pedidoBO.findPedido(idPedido, idEmpresa,
                ((paginaActual - 1) * (int)registrosPagina), (int)registrosPagina,
                filtroBusqueda);

     }catch(Exception ex){
         ex.printStackTrace();
     }
     
     
     // Totales 
            double ventasTotales = 0;
            double pagosTotales = 0;
            double adeudosTotales  = 0;
            double comisionTotalVendedor = 0;
            double utilidadTotal = 0;
            SgfensPedido[] pedidoDtoTotales = new SgfensPedido[0];
            pedidoDtoTotales = pedidoBO.findPedido(idPedido, idEmpresa , 0, 0, filtroBusqueda);
                                        
            for (SgfensPedido item:pedidoDtoTotales){
                
                int cantidadProductos = 0;
                double comisionVendedor = 0;
                double utilidadNeta = 0;                                                   
                double ventaTotal = 0;
                double adeudo = 0;
                
                
                if(item.getIdEstatusPedido()!=3 || buscar_idestatuspedido.equals("3")){
                    
                
                    ventasTotales += item.getTotal();
                    pagosTotales += item.getSaldoPagado();
                    adeudosTotales  += (item.getTotal()- item.getSaldoPagado());



                    double costoVentaProducto = 0;
                    if(item.getBonificacionDevolucion()<0){
                        ventasTotales += Math.abs(item.getBonificacionDevolucion());
                        adeudosTotales  += Math.abs(item.getBonificacionDevolucion());
                    }


                    try{                                  
                        SgfensPedidoProducto[] spp = new SgfensPedidoProductoDaoImpl(user.getConn()).findWhereIdPedidoEquals(item.getIdPedido());
                        cantidadProductos = spp.length;
                        for(SgfensPedidoProducto product : spp){
                            comisionVendedor += ( product.getPorcentajeComisionEmpleado() * 0.01 * product.getSubtotal() );
                            costoVentaProducto += (product.getCostoUnitario() * product.getCantidad() );// + comisionVendedor);
                        }
                        comisionTotalVendedor += comisionVendedor;
                    }catch(Exception e){}

                    if(item.getBonificacionDevolucion()>0){
                       utilidadNeta = (item.getTotal() - costoVentaProducto - comisionVendedor);
                       ventaTotal = item.getTotal();
                       adeudo = item.getTotal() - item.getSaldoPagado();
                    }else{
                       utilidadNeta = ((item.getTotal() + Math.abs(item.getBonificacionDevolucion())) - costoVentaProducto - comisionVendedor);
                       ventaTotal = item.getTotal() + Math.abs(item.getBonificacionDevolucion());
                       adeudo = (item.getTotal() + Math.abs(item.getBonificacionDevolucion())) - item.getSaldoPagado();
                    }

                    if(item.getIdEstatusPedido() == 3){//pedido cancelado, no sumamos la utilidad
                        utilidadNeta = 0;
                    }

                    utilidadTotal += utilidadNeta;
                    
                }
                
            }
     
     
     ///
     
     ClienteCategoriaBO clienteCategoriaBO = new ClienteCategoriaBO(user.getConn());
     ClienteCategoria[] clientesCategorias = clienteCategoriaBO.findClienteCategorias(0, idEmpresa, 0, 0, " AND ID_ESTATUS = 1 ");
     
     /*
    * Datos de catálogo
    */
    String urlTo = "../pedido/pedido_form.jsp";
    String paramName = "idPedido";
    //String parametrosPaginacion="";// "idEmpresa="+idEmpresa;
    String filtroBusquedaEncoded = java.net.URLEncoder.encode(filtroBusqueda, "UTF-8");   
    String infoTitleEncoded = java.net.URLEncoder.encode(infoTitle , "UTF-8");
    
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/keyWordSEO.jsp" />

        <title><jsp:include page="../include/titleApp.jsp" /></title>

        <jsp:include page="../include/skinCSS.jsp" />

        <jsp:include page="../include/jsFunctions.jsp"/>
        
        <script type="text/javascript">
            
            function mostrarCalendario(){
                //fh_min
                //fh_max

                var dates = $('#q_fh_min, #q_fh_max').datepicker({
                        //minDate: 0,
			changeMonth: true,
			//numberOfMonths: 2,
                        //beforeShow: function() {$('#fh_min').css("z-index", 9999); },
                        beforeShow: function(input, datepicker) {
                            setTimeout(function() {
                                    $(datepicker.dpDiv).css('zIndex', 998);
                            }, 500)},
			onSelect: function( selectedDate ) {
				var option = this.id == "q_fh_min" ? "minDate" : "maxDate",
					instance = $( this ).data( "datepicker" ),
					date = $.datepicker.parseDate(
						instance.settings.dateFormat ||
						$.datepicker._defaults.dateFormat,
						selectedDate, instance.settings );
				dates.not( this ).datepicker( "option", option, date );
			}
		});

            }
            
            function editarPedido(idPedido){
                if(idPedido>=0){
                    $.ajax({
                        type: "POST",
                        url: "pedido_ajax.jsp",
                        data: { mode: '7', id_pedido : idPedido },
                        beforeSend: function(objeto){
                            //$("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style="">ESPERE, procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               location.href = "pedido_form.jsp?acc=2&idPedido="+idPedido+"&pagina="+"<%=paginaActual%>";
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               //$("#ajax_message").html(datos);
                               //$("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                           }
                        }
                    });
                }
            }
            
            function editarPedidoEditarAlmacen(idPedido){
                if(idPedido>=0){
                    $.ajax({
                        type: "POST",
                        url: "pedido_ajax.jsp",
                        data: { mode: '7', id_pedido : idPedido, editAlmacenOrigen : "editAlmacenOrigen" },
                        beforeSend: function(objeto){
                            //$("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style="">ESPERE, procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               location.href = "pedido_form.jsp?acc=2&idPedido="+idPedido+"&editAlmacenOrigen=editAlmacenOrigen&pagina="+"<%=paginaActual%>";
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               //$("#ajax_message").html(datos);
                               //$("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                           }
                        }
                    });
                }
            }
            
            
            function facturar(idPedido,tipoFactura){            
                //tipoFactura :  1- Normal , 2- Con Impuestos 
                      
                if(idPedido>=0){
                    $.ajax({
                        type: "POST",
                        url: "pedido_ajax.jsp",
                        data: { mode: '22', id_pedido : idPedido,tipoFactura:tipoFactura },
                        beforeSend: function(objeto){
                            //$("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style="">ESPERE, procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               if(tipoFactura==1){//Factura normal
                                   location.href = "../cfdi_factura/cfdi_factura_form.jsp";
                               }else if(tipoFactura==2){//Factura con impuestos
                                   location.href = "../cfdi_factura/cfdi_fctra_form.jsp";
                               }
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               //$("#ajax_message").html(datos);
                               //$("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                           }
                        }
                    });
                }

            }

            function confirmarCancelarPedido(idPedido, folioPedido){
                apprise('¿Esta seguro que desea cancelar el pedido con Folio '+folioPedido+'?', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
                    function(r){
                        if(r){
                            // Usuario dio click 'Yes'
                            cancelarPedido(idPedido);
                        }
                });

            }

            function cancelarPedido(idPedido){
                if(idPedido>=0){
                    $.ajax({
                        type: "POST",
                        url: "pedido_ajax.jsp",
                        data: { mode: '23', id_pedido : idPedido },
                        beforeSend: function(objeto){
                            //$("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style="">ESPERE, procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               location.href = "pedido_list.jsp?idPedido="+idPedido;
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               //$("#ajax_message").html(datos);
                               //$("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                           }
                        }
                    });
                }
            }
            
            function confirmarEntregaPedido(idPedido, folioPedido){
                apprise('¿Esta seguro que desea marcar como entregado el pedido con Folio '+folioPedido+'?', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
                    function(r){
                        if(r){
                            // Usuario dio click 'Yes'
                            //marcarEntregaPedido(idPedido);
                            confirmarEntregaPedidoModificarOrigenAlmacen(idPedido, folioPedido);
                        }
                });
                
            }
            
            function confirmarEntregaPedidoModificarOrigenAlmacen(idPedido, folioPedido){
                apprise('¿Desea modificar el origen de los productos de este pedido con Folio '+folioPedido+'?', {'verify':true, 'animate':true, 'textYes':'Si', 'textNo':'No'}, 
                    function(r){
                        if(r){
                            // Usuario dio click 'Yes'
                            //location.href = "pedido_form.jsp?acc=2&idPedido="+idPedido+"&editAlmacenOrigen=editAlmacenOrigen";
                            editarPedidoEditarAlmacen(idPedido);
                        }else{
                            marcarEntregaPedido(idPedido);
                        }
                });
                
            }
            
            function marcarEntregaPedido(idPedido){
                if(idPedido>=0){
                    $.ajax({
                        type: "POST",
                        url: "pedido_ajax.jsp",
                        data: { mode: '24', id_pedido : idPedido },
                        beforeSend: function(objeto){
                            //$("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style="">ESPERE, procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){
                            if(datos.indexOf("--EXITO-->", 0)>0){
                               $("#ajax_loading").fadeOut("slow");
                               $("#ajax_message").html(datos);
                               $("#ajax_message").fadeIn("slow");
                               location.href = "pedido_list.jsp?idPedido="+idPedido;
                           }else{
                               $("#ajax_loading").fadeOut("slow");
                               //$("#ajax_message").html(datos);
                               //$("#ajax_message").fadeIn("slow");
                               apprise('<center><img src=../../images/warning.png> <br/>'+ datos +'</center>',{'animate':true});
                           }
                        }
                    });
                }
            }
            
            //Verifica si los conceptos del pedido tienen 
            //impuestos relacionados para facturar con impuestos
            function validaFactImp(idPedido){
                
                var retorno = false;
                if(idPedido>=0){
                    $.ajax({
                        type: "POST",
                        url: "pedido_ajax.jsp",
                        data: { mode: '27', id_pedido : idPedido },
                        beforeSend: function(objeto){
                            //$("#action_buttons").fadeOut("slow");
                            $("#ajax_loading").html('<div style="">ESPERE, procesando...<br/><img src="../../images/ajax_loader.gif" alt="Cargando.." /></div>');
                            $("#ajax_loading").fadeIn("slow");
                        },
                        success: function(datos){                            
                            if(datos.indexOf("--EXITO-->", 0)>0){   
                                alert("ok");
                               $("#ajax_loading").fadeOut("slow");                                
                               retorno = true;                               
                           }
                        }
                    });                    
                    return retorno;
                }else{
                    return retorno;
                }
            }
            
        </script>

    </head>
    <body>
        <div class="content_wrapper">

            <jsp:include page="../include/header.jsp" flush="true"/>

            <jsp:include page="../include/leftContent.jsp"/>

            <!-- Inicio de Contenido -->
            <div id="content">

                <div class="inner">
                    <h1>Ventas</h1>
                    
                    <div id="ajax_loading" class="alert_info" style="display: none;"></div>
                    <div id="ajax_message" class="alert_warning" style="display: none;"></div>

                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                Búsqueda Avanzada &dArr;
                            </span>
                        </div>
                        <br class="clear"/>
                        <div class="content" style="display: none;">
                            <form action="pedido_list.jsp" id="search_form_advance" name="search_form_advance" method="post">
                                <p>
                                    Por Fecha de Entrega &raquo;&nbsp;&nbsp;
                                    <label>Desde:</label>
                                    <input maxlength="15" type="text" id="q_fh_min" name="q_fh_min" style="width:100px"
                                            value="" readonly/>
                                    &nbsp; &laquo; &mdash; &raquo; &nbsp;
                                    <label>Hasta:</label>
                                    <input maxlength="15" type="text" id="q_fh_max" name="q_fh_max" style="width:100px"
                                        value="" readonly/>
                                </p>
                                <br/>
                                
                                <p>
                                <label>Cliente:</label><br/>
                                <select id="q_idcliente" name="q_idcliente" class="flexselect">
                                    <option></option>
                                    <%= new ClienteBO(user.getConn()).getClientesByIdHTMLCombo(idEmpresa, -1," AND ID_ESTATUS <> 2 " + (user.getUser().getIdRoles() == RolesBO.ROL_OPERADOR? " AND ID_CLIENTE IN (SELECT ID_CLIENTE FROM SGFENS_CLIENTE_VENDEDOR WHERE ID_USUARIO_VENDEDOR="+user.getUser().getIdUsuarios()+")" : "") ) %>
                                </select>
                                </p>
                                <br/>
                                
                                <%if(clientesCategorias.length > 0){%>
                                    <p>
                                        <label>Tipo:</label><br/>
                                        <select id="idClienteCategoria" name="idClienteCategoria">
                                            <option value="0">Selecciona Tipo de Categoría de Cliente</option>
                                            <%
                                                out.print(clienteCategoriaBO.getClienteCategoriasByIdHTMLCombo(idEmpresa, -1));
                                            %>
                                        </select>
                                    </p>
                                    <br/>
                                <%}%>
                                
                                <% if (user.getUser().getIdRoles() != RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL){%>
                                <p>
                                <label>Vendedor:</label><br/>
                                <select id="q_idvendedor" name="q_idvendedor" class="flexselect">
                                    <option></option>
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_OPERADOR_VENDEDOR_MOVIL, 0) %> 
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_OPERADOR, 0) %>
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR, 0) %>
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_CONDUCTOR_VENDEDOR, 0) %>
                                    <%= new UsuariosBO().getUsuariosByRolHTMLCombo(idEmpresa, RolesBO.ROL_COBRADOR, 0) %>
                                </select>
                                </p>
                                <br/>
                                <% } %>
                                
                                <p>
                                <label>Estado:</label><br/>
                                <select id="q_idestatuspedido" name="q_idestatuspedido">
                                    <option></option>
                                    <option value="<%= SGEstatusPedidoBO.ESTATUS_PENDIENTE %>"> 
                                        <%= SGEstatusPedidoBO.ESTATUS_PENDIENTE_NAME %>
                                    </option>
                                    <option value="<%= SGEstatusPedidoBO.ESTATUS_ENTREGADO %>">
                                        <%= SGEstatusPedidoBO.ESTATUS_ENTREGADO_NAME %>
                                    </option>
                                    <option value="<%= SGEstatusPedidoBO.ESTATUS_CANCELADO %>">
                                        <%= SGEstatusPedidoBO.ESTATUS_CANCELADO_NAME %>
                                    </option>
                                </select>
                                </p>
                                <br/>
                                <p>
                                    <input type="radio" class="checkbox" id="sinAdeudo" name="q_adeudo" value="0" > <label for="sinAdeudo">Sin Adeudo</label>
                                    <input type="radio" class="checkbox" id="conAdeudo" name="q_adeudo" value="1" > <label for="conAdeudo">Con Adeudo</label>                                                   
                                </p>    
                                <br/>
                                <p>
                                    <input type="radio" class="checkbox" id="noConsigna" name="q_consigna" value="0" > <label for="noConsigna">Clientes Normales</label>
                                    <input type="radio" class="checkbox" id="siConsigna" name="q_consigna" value="1" > <label for="siConsigna">En Consigna</label>                                                   
                                </p>    
                                <br/>
                                <div id="action_buttons">
                                    <p>
                                        <input type="button" id="buscar" value="Buscar" onclick="$('#search_form_advance').submit();"/>
                                    </p>
                                </div>
                                
                            </form>
                        </div>
                    </div>
                    
                    <div class="onecolumn">
                        <div class="header">
                            <span>
                                <img src="../../images/icon_ventas3.png" alt="icon"/>
                                Pedidos
                            </span>
                            <div class="switch" style="width:650px">
                                <table width="650px" cellpadding="0" cellspacing="0">
                                        <tbody>
                                                <tr>
                                                    <!--<td style="width: 50px">
                                                    <a href="#" id="consultaMapa" title="Grafica" class="modalbox_iframe">                                           
                                                    <img src="../../images/icon_mapa.png" alt="Gráfica" class="help" title="Mapa de Pedidos"/>    
                                                    </td>-->
                                                    <td>
                                                        <div id="search">                                                                
                                                        <form action="pedido_list.jsp" id="search_form" name="search_form" method="get">
                                                                <input type="text" id="q" name="q" title="Buscar Folio Pedido Servidor / Movil (p.ej: P0000)" class="" style="width: 300px; float: left; "
                                                                       value="<%=buscar%>"/>
                                                                <input type="image" src="../../images/Search-32_2.png" id="buscar" name="buscar"  value="" style="cursor: pointer; width: 30px; height: 25px; float: left"/>
                                                        </form>
                                                        </div>
                                                    </td>
                                                    <td class="clear">&nbsp;&nbsp;&nbsp;</td>
                                                    <td>
                                                        <input type="button" id="nuevo" name="nuevo" class="right_switch" value="Nuevo Pedido" 
                                                               style="float: right; width: 100px;" onclick="location.href='<%=urlTo%>?acc=1'"/>
                                                    </td>
                                                </tr>
                                        </tbody>
                                </table>
                            </div>
                        </div>
                        <br class="clear"/>
                        <div class="content">
                            <form id="form_data" name="form_data" action="" method="post">
                                <table class="data" width="100%" cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Folio</th>
                                            <th>Ver</th>
                                            <th>Vendedor</th>                    
                                            <th>Cliente</th>
                                            <th>Fecha/Hora Pedido</th>
                                            <th>Fecha Entrega</th>
                                            <th>Estado</th>
                                            <th>Cantidad Productos</th>
                                            <th>Días Crédito</th>
                                            <th>Venta Total</th>
                                            <th>Pagado</th>
                                            <th>Adeudo</th>
                                            <th>Comisión Vendedor</th>
                                            <th>Utilidad</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                        
                                            
                                            Date hoy = new Date();//fecha de hoy
                                            final long MILLSECS_POR_DAY = 24 * 60 * 60 * 1000; //Milisegundos al día 
                                            String vendedorCreadorAsignado = "";
                                            for (SgfensPedido item:pedidoDto){
                                                vendedorCreadorAsignado = "Sin vendedor asignado";
                                                try{
                                                    try{
                                                        DatosUsuario datosUsuarioVendedor = new UsuarioBO(item.getIdUsuarioVendedor()).getDatosUsuario();
                                                        if(datosUsuarioVendedor != null){
                                                            vendedorCreadorAsignado = datosUsuarioVendedor.getNombre() +" " + datosUsuarioVendedor.getApellidoPat();
                                                        }}catch(Exception e){}
                                                    try{
                                                        if(item.getIdUsuarioVendedor() != item.getIdUsuarioVendedorAsignado()){
                                                            DatosUsuario datosUsuarioVendedorAsignado = new UsuarioBO(item.getIdUsuarioVendedorAsignado()).getDatosUsuario();
                                                            vendedorCreadorAsignado += " / Vendedor Asignado: " + datosUsuarioVendedorAsignado.getNombre() +" " + datosUsuarioVendedorAsignado.getApellidoPat();
                                                        }}catch(Exception e){}
                                                    try{
                                                        if(item.getIdUsuarioVendedor() != item.getIdUsuarioVendedorAsignado() && item.getIdUsuarioVendedor() != item.getIdUsuarioConductorAsignado() && item.getIdUsuarioVendedorAsignado() != item.getIdUsuarioConductorAsignado()){
                                                            DatosUsuario datosUsuarioConductorAsignado = new UsuarioBO(item.getIdUsuarioConductorAsignado()).getDatosUsuario();
                                                            vendedorCreadorAsignado += " / Conductor Asignado: " + datosUsuarioConductorAsignado.getNombre() +" " + datosUsuarioConductorAsignado.getApellidoPat();
                                                        }}catch(Exception e){}
                                                    
                                                    Cliente cliente = null;
                                                    try{ cliente = new ClienteBO(item.getIdCliente(),user.getConn()).getCliente(); }catch(Exception ex){}
                                                    
                                                    //recuperamos el ultimo abono del pedido para ver cuantos dias han transcurrido del ultimo pago que se considera como días de crédito.
                                                    long diasDeCredito = 0;
                                                    if(item.getTotal() > (item.getAdelanto() + item.getSaldoPagado()) ){//validamos si aun existe monto por pagar, para calcular los dias de crédito
                                                        //obtenemos el ultimo abono del pedido:
                                                        try{
                                                            SgfensCobranzaAbono abono = new SGCobranzaAbonoBO(user.getConn()).findCobranzaAbono(0, 0, 0, 1, " AND ID_PEDIDO = " + item.getIdPedido())[0];
                                                            diasDeCredito = (hoy.getTime() - abono.getFechaAbono().getTime())/MILLSECS_POR_DAY;
                                                        }catch(Exception e){//si no hay un abono aun, calculamos los dias transcurridos de la fecha realizada del pedido al día de hoy
                                                            diasDeCredito = (hoy.getTime() - item.getFechaPedido().getTime())/MILLSECS_POR_DAY;
                                                        }
                                                    }
                                                    
                                                    //Verificamos la cantidad de productos que tiene el pedido
                                                    int cantidadProductos = 0;
                                                    double comisionVendedor = 0;
                                                    double utilidadNeta = 0;
                                                    double costoVentaProducto = 0;
                                                    double ventaTotal = 0;
                                                    double adeudo = 0;
                                                    
                                                    try{
                                                        SgfensPedidoProducto[] spp = new SgfensPedidoProductoDaoImpl(user.getConn()).findWhereIdPedidoEquals(item.getIdPedido());
                                                        cantidadProductos = spp.length;
                                                        for(SgfensPedidoProducto product : spp){
                                                            comisionVendedor += ( product.getPorcentajeComisionEmpleado() * 0.01 * product.getSubtotal() );
                                                            costoVentaProducto += (product.getCostoUnitario() * product.getCantidad() );// + comisionVendedor);
                                                        }
                                                        //comisionTotalVendedor += comisionVendedor;
                                                    }catch(Exception e){}
                                                    
                                                    
                                                    if(item.getBonificacionDevolucion()>0){
                                                       utilidadNeta = (item.getTotal() - costoVentaProducto - comisionVendedor);
                                                       ventaTotal = item.getTotal();
                                                       adeudo = item.getTotal() - item.getSaldoPagado();
                                                    }else{
                                                       utilidadNeta = ((item.getTotal() + Math.abs(item.getBonificacionDevolucion())) - costoVentaProducto - comisionVendedor);
                                                       ventaTotal = item.getTotal() + Math.abs(item.getBonificacionDevolucion());
                                                       adeudo = (item.getTotal() + Math.abs(item.getBonificacionDevolucion())) - item.getSaldoPagado();
                                                    }
                                                    
                                                    if(item.getIdEstatusPedido() == 3){//pedido cancelado, no sumamos la utilidad
                                                        utilidadNeta = 0;
                                                    }
                                                    
                                                    
                                                    //utilidadNeta = (item.getTotal() - costoVentaProducto - comisionVendedor);
                                                    //utilidadTotal += utilidadNeta;
                                                    
                                        %>
                                        <tr>
                                            <!--<td><input type="checkbox"/></td>-->
                                            <td><%=item.getIdPedido() %></td>
                                            <td><%=item.getFolioPedido() %></td>
                                            <td>
                                                <a href="../../jsp/reportesExportar/previewPedidoPDF.jsp?idPedido=<%=item.getIdPedido() %>" id="btn_show_cfdi" title="Previsualizar Pedido"
                                                class="modalbox_iframe">
                                                    <img src="../../images/icon_consultar.png" alt="Mostrar Pedido" class="help" title="Previsualizar Pedido"/><br/>
                                                </a>
                                            </td>
                                            <td><%= vendedorCreadorAsignado %></td>                                            
                                            <td>
                                                <%
                                                    if (item.getIdCliente()>0 && cliente!=null){
                                                        if(item.getConsigna()==1){
                                                            //Cliente consigna
                                                            out.print(cliente.getRazonSocial() + (cliente.getClave()!=null&&!cliente.getClave().trim().equals("")?(", Clave: "+cliente.getClave()):"") +" <i>[Cliente Consigna]</i>");
                                                        }else{
                                                            //Cliente
                                                            out.print(cliente.getRazonSocial() + (cliente.getClave()!=null&&!cliente.getClave().trim().equals("")?(", Clave: "+cliente.getClave()):"") +" <i>[Cliente]</i>");
                                                        }
                                                    }else{
                                                        out.print("Sin cliente asignado.");
                                                    }
                                                %>
                                            </td>
                                            <td><%= DateManage.formatDateTimeToNormalMinutes(item.getFechaPedido()) %></td>
                                            <td><%= DateManage.formatDateToNormal(item.getFechaEntrega()) %></td>
                                            <td><%= SGEstatusPedidoBO.getEstatusName(item.getIdEstatusPedido()) %></td>
                                            
                                            <td><%=cantidadProductos%></td>
                                            
                                            <td><%=diasDeCredito%></td>
                                            
                                            <td><%=formatMoneda.format(ventaTotal)%></td>
                                            <td><%=formatMoneda.format(item.getSaldoPagado())%></td>
                                            <td><%=formatMoneda.format(adeudo)%></td>
                                            <td><%=formatMoneda.format(comisionVendedor)%></td>
                                            <td><%=formatMoneda.format(utilidadNeta)%></td>
                                            <td>
                                                <%if(RolAutorizacionBO.permisoAccionesElemento(user.getUser().getIdRoles())){%>
                                                
                                                <%if ((item.getIdEstatusPedido()!=SGEstatusPedidoBO.ESTATUS_CANCELADO && item.getFolioPedidoMovil()==null) || ( item.getFolioPedidoMovil()!=null && item.getIdEstatusPedido()== SGEstatusPedidoBO.ESTATUS_PENDIENTE)){%>
                                                         <a href="#" onclick="editarPedido(<%=item.getIdPedido()%>);"><img src="../../images/icon_edit.png" alt="editar" class="help" title="Editar" /></a>
                                                         &nbsp;      
                                                 <% }  %>
                                                
                                                <%if (item.getIdEstatusPedido()!=SGEstatusPedidoBO.ESTATUS_CANCELADO){%>                                                    
                                                    <% if (item.getConsigna()==0){ 
                                                            if (item.getIdComprobanteFiscal()<=0){ 
                                                                if(cliente.getIdEstatus() == 1){%>
                                                                    <a href="#" onclick="facturar(<%=item.getIdPedido()%>,1);"><img src="../../images/icon_convert_factura.png" alt="facturar" class="help" title="Facturar" /></a>
                                                                    &nbsp;
                                                                    <a href="#" onclick="facturar(<%=item.getIdPedido()%>,2);"><img src="../../images/icon_convert_factura_imp.png" alt="facturar" class="help" title="Factura con Impuestos" /></a>
                                                        <%      }
                                                            }else{ %>
                                                    <a href="../cfdi_factura/cfdi_factura_list.jsp?idComprobanteFiscal=<%=item.getIdComprobanteFiscal() %>"><img src="../../images/icon_consultar.png" alt="consultar factura" class="help" title="Ver Factura" /></a>
                                                         <% }
                                                    }%>
                                                    &nbsp;&nbsp;
                                                    <% if (  ((item.getIdEstatusPedido()==SGEstatusPedidoBO.ESTATUS_PENDIENTE || item.getIdEstatusPedido()==SGEstatusPedidoBO.ESTATUS_ENTREGADO)&& item.getFolioPedidoMovil()==null) //si es de consola
                                                            || (item.getIdEstatusPedido()==SGEstatusPedidoBO.ESTATUS_PENDIENTE && item.getFolioPedidoMovil()!=null)  ){   //si es de movil y no se ha entregado%>  
                                                        <a href="#" onclick="confirmarCancelarPedido(<%=item.getIdPedido()%>,'<%=item.getFolioPedido() %>');"><img src="../../images/icon_delete.png" alt="cancelar" class="help" title="Cancelar" /></a>
                                                    <%}%>
                                                    &nbsp;&nbsp;
                                                    <a href="../catCobranzaAbono/catCobranzaAbono_list.jsp?idPedido=<%=item.getIdPedido() %>"><img src="../../images/icon_ventas1.png" alt="consultar cobranza" class="help" title="Cobranza" /></a>
                                                    &nbsp;&nbsp;
                                                    <% if (item.getIdEstatusPedido()==SGEstatusPedidoBO.ESTATUS_PENDIENTE  || item.getIdEstatusPedido()==SGEstatusPedidoBO.ESTATUS_ENTREGADO_PARCIAL ) {%>
                                                        <a href="#" onclick="confirmarEntregaPedido(<%=item.getIdPedido()%>,'<%=item.getFolioPedido() %>');"><img src="../../images/icon_ok.png" alt="entregar" class="help" title="Marcar como Entregado" /></a>
                                                    <%}%>
                                                    
                                                    <%if (item.getConsigna()==0){ %>
                                                        &nbsp;&nbsp;
                                                        <a href="pedido_AsignarPedido_form.jsp?idPedido=<%=item.getIdPedido() %>" id="asignarPedido" title="Asignar Pedido" class="modalbox_iframe">
                                                        <img src="../../images/icon_prospecto.png" alt="Asignar Pedido" class="help" title="Asignar Pedido"/>
                                                        </a>
                                                    <%}%>
                                                    
                                                    <%if (item.getConsigna()==0){ %>
                                                    &nbsp;&nbsp;
                                                    <a href="../pedido/pedido_pedidoLocalizacion.jsp?idPedido=<%=item.getIdPedido() %>"><img src="../../images/icon_movimiento.png" alt="Localización" class="help" title="Localización" /></a>
                                                    <%}%>                                                     
                                                <%}%>
                                                <%}%>
                                                <!--<a href=""><img src="images/icon_delete.png" alt="delete" class="help" title="Delete"/></a>-->
                                            </td>
                                        </tr>                                      
                                        
                                                                                
                                        <%      }catch(Exception ex){
                                                    ex.printStackTrace();
                                                }
                                            } 
                                        %>
                                        
                                        <tr>
                                            <td colspan="10" style="text-align: right;"><b>Totales:</b></td>
                                            <td style="color: #0000cc; text-align: left;"><%=formatMoneda.format(ventasTotales)%></td>
                                            <td style="color: #0000cc; text-align: left;"><%=formatMoneda.format(pagosTotales)%></td>
                                            <td style="color: #0000cc; text-align: left;"><%=formatMoneda.format(adeudosTotales)%></td>
                                            <td style="color: #0000cc; text-align: left;"><%=formatMoneda.format(comisionTotalVendedor)%></td>
                                            <td style="color: #0000cc; text-align: left;"><%=formatMoneda.format(utilidadTotal)%></td>                                            
                                            <td></td>
                                        </tr>
                                        
                                    </tbody>
                                </table>
                            </form>

                            <!-- INCLUDE OPCIONES DE EXPORTACIÓN-->
                            <jsp:include page="../include/reportExportOptions.jsp" flush="true">
                                <jsp:param name="idReport" value="<%= ReportBO.PEDIDO_REPORT %>" />
                                <jsp:param name="parametrosCustom" value="<%= filtroBusquedaEncoded %>" />
                                <jsp:param name="infoTitle" value="<%=infoTitleEncoded%>" />
                            </jsp:include>
                            <!-- FIN INCLUDE OPCIONES DE EXPORTACIÓN-->
                                                        
                            
                                    
                            <jsp:include page="../include/listPagination.jsp">
                                <jsp:param name="paginaActual" value="<%=paginaActual%>" />
                                <jsp:param name="numeroPaginasAMostrar" value="<%=numeroPaginasAMostrar%>" />
                                <jsp:param name="paginasTotales" value="<%=paginasTotales%>" />
                                <jsp:param name="url" value="<%=request.getRequestURI() %>" />
                                <jsp:param name="parametrosAdicionales" value="<%=parametrosPaginacion%>" />
                            </jsp:include>
                            
                            <input type="button" id="regresar" value="Regresar" onclick="history.back();"/>
                            
                        </div>
                    </div>

                </div>

                <jsp:include page="../include/footer.jsp"/>
            </div>
            <!-- Fin de Contenido-->
        </div>

        <script>
            mostrarCalendario();
            $("select.flexselect").flexselect();
        </script>
    </body>
</html>
<%}%>