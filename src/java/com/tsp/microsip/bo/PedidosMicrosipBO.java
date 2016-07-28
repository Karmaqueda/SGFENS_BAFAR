/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.microsip.bo;

import com.tsp.microsip.bean.ControlBean;
import com.tsp.microsip.bean.SgfensPedidosMicrosipBean;
import com.tsp.sct.bo.ConceptoBO;
import com.tsp.sct.bo.SGEstatusPedidoBO;
import com.tsp.sct.bo.SGPedidoBO;
import com.tsp.sct.dao.dto.Concepto;
import com.tsp.sct.dao.dto.ConceptoPk;
import com.tsp.sct.dao.dto.Empleado;
import com.tsp.sct.dao.dto.Impuesto;
import com.tsp.sct.dao.dto.QuartzAlmacen;
import com.tsp.sct.dao.dto.QuartzCliente;
import com.tsp.sct.dao.dto.QuartzConcepto;
import com.tsp.sct.dao.dto.QuartzConceptoPk;
import com.tsp.sct.dao.dto.QuartzEmpleado;
import com.tsp.sct.dao.dto.QuartzEstatusPedido;
import com.tsp.sct.dao.dto.QuartzImpuesto;
import com.tsp.sct.dao.dto.QuartzPedido;
import com.tsp.sct.dao.dto.QuartzViaEmbarque;
import com.tsp.sct.dao.dto.SgfensPedido;
import com.tsp.sct.dao.dto.SgfensPedidoDevolucionCambio;
import com.tsp.sct.dao.dto.SgfensPedidoImpuesto;
import com.tsp.sct.dao.dto.SgfensPedidoPk;
import com.tsp.sct.dao.dto.SgfensPedidoProducto;
import com.tsp.sct.dao.jdbc.ConceptoDaoImpl;
import com.tsp.sct.dao.jdbc.EmpleadoDaoImpl;
import com.tsp.sct.dao.jdbc.ImpuestoDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzAlmacenDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzClienteDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzConceptoDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzEmpleadoDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzEstatusPedidoDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzImpuestoDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzPedidoDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzViaEmbarqueDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.dao.jdbc.SgfensPedidoDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensPedidoDevolucionCambioDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensPedidoImpuestoDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensPedidoProductoDaoImpl;
import com.tsp.sct.util.StringManage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author leonardo
 */
public class PedidosMicrosipBO {
    
    public int obtenerCantidadRegistros(int tipo, int idEmpresa){
        try{
            //validamos si la busqueda es para sgfensPedidos nuevos o actualizados        
            String filtroBusqueda = "";
            if(tipo == 1){//sgfensPedidos nuevos
                filtroBusqueda = " SINCRONIZACION_MICROSIP = 0 ";
            }else if(tipo == 2){//sgfensPedidos actualizados
                filtroBusqueda = " SINCRONIZACION_MICROSIP = 2 ";
            }

            if(idEmpresa > 0){
                filtroBusqueda += " AND ID_EMPRESA = " +idEmpresa;
            }
            Connection conn = ResourceManager.getConnection();
            SGPedidoBO pedidoBO = new SGPedidoBO(conn);
            int cantidad = pedidoBO.getCantidadBySgfensPedido(filtroBusqueda);
            conn.close();
            return cantidad;    
        }catch(Exception e){
            e.printStackTrace();
            return 0;            
        }
    }
    
    public List<SgfensPedidosMicrosipBean> obtenerSgfensPedidos(int tipo, int idEmpresa){
        try{
            //validamos si la busqueda es para clientes nuevo o actualizados        
            String filtroBusqueda = " AND ";
            if(tipo == 1){//clientes nuevos
                filtroBusqueda += " SINCRONIZACION_MICROSIP = 0 ";
            }else if(tipo == 2){//clientes actualizados
                filtroBusqueda += " SINCRONIZACION_MICROSIP = 2 ";
            }
            Connection conn = ResourceManager.getConnection();
            SGPedidoBO pedidoBO = new SGPedidoBO(conn);
            SgfensPedido[] sgfensPedido = pedidoBO.findPedido(0, idEmpresa, 0, 0, filtroBusqueda);

            List<SgfensPedidosMicrosipBean> sgfensPedidosCompartidos = new ArrayList<SgfensPedidosMicrosipBean>();

            QuartzClienteDaoImpl qcldi = new QuartzClienteDaoImpl(conn);
            QuartzEmpleadoDaoImpl qedi = new QuartzEmpleadoDaoImpl(conn);
            QuartzViaEmbarqueDaoImpl qvedi = new QuartzViaEmbarqueDaoImpl(conn);
            EmpleadoDaoImpl edi = new EmpleadoDaoImpl(conn);
            for(SgfensPedido ped : sgfensPedido ){
                ////----Actualizamos los ID de Empleado y de Cliente por los del sistema microsip
                String claveClienteSistemaTercero = "";
                String claveEmpleadoSistemaTercero = "";
                try{
                    QuartzCliente quartzCliente = qcldi.findByDynamicWhere(" ID_CLIENTE_EVC = " + ped.getIdCliente() + " ORDER BY ID_QUARTZ DESC ", null)[0];
                    ped.setIdCliente(quartzCliente.getIdClienteSistemTercero());
                    claveClienteSistemaTercero = StringManage.getValidString(quartzCliente.getClave());
                }catch(Exception e){
                    //e.printStackTrace();
                    System.out.println("El Cliente con ID en pretoriano " + ped.getIdCliente()+ " no ha sido sincronizado en el sistema de terceros.\n" + e.toString());
                    ped.setIdCliente(-1);//Asignamos por defecto -1 por si no encontramos el correspondiente del sistema tercero
                }
                
                try{
                    Empleado emp = edi.findWhereIdUsuariosEquals(ped.getIdUsuarioVendedor())[0];
                    QuartzEmpleado quartzEmpleado = qedi.findByDynamicWhere(" ID_EMPLEADO_EVC = " + emp.getIdEmpleado() + " ORDER BY ID_QUARTZ DESC ", null)[0];
                    ped.setIdUsuarioVendedor(quartzEmpleado.getIdEmpleadoSistemTercero());
                    claveEmpleadoSistemaTercero = StringManage.getValidString(quartzEmpleado.getClave());
                }catch(Exception e){
                    //e.printStackTrace();
                    System.out.println("El Empleado con ID Usuario en pretoriano " + ped.getIdUsuarioVendedor()+ " no ha sido sincronizado en el sistema de terceros.\n" + e.toString());
                    ped.setIdUsuarioVendedor(-1);//Asignamos por defecto -1 por si no encontramos el correspondiente del sistema tercero
                }
                
                try{
                    QuartzViaEmbarque qc = qvedi.findByDynamicWhere(" ID_VIA_EMBARQUE_EVC = " + ped.getIdViaEmbarque()  + " AND ID_EMPRESA = " + idEmpresa+ " ORDER BY ID_QUARTZ DESC ", null)[0]; //buscamos el registro correspondiente
                    ped.setIdViaEmbarque(qc.getIdViaEmbarqueSistemTercero());
                }catch(Exception e){
                    /*e.printStackTrace();*/
                    System.out.println("La Via de embarque con ID Usuario en pretoriano " + ped.getIdViaEmbarque()+ " no ha sido sincronizado en el sistema de terceros.\n" + e.toString());
                    ped.setIdViaEmbarque(-1);
                } 
                ////----
                
                
                //--/-/-/ cargamos el objeto correspondiente de pedido
                com.tsp.microsip.bean.SgfensPedido sgfensPedidosMicro = new com.tsp.microsip.bean.SgfensPedido();
                sgfensPedidosMicro.setIdPedido(ped.getIdPedido());
                sgfensPedidosMicro.setIdUsuarioVendedor(ped.getIdUsuarioVendedor());
                sgfensPedidosMicro.setIdEmpresa(ped.getIdEmpresa());
                sgfensPedidosMicro.setIdCliente(ped.getIdCliente());
                sgfensPedidosMicro.setConsecutivoPedido(ped.getConsecutivoPedido());
                sgfensPedidosMicro.setFolioPedido(ped.getFolioPedido());
                sgfensPedidosMicro.setFechaPedido(ped.getFechaPedido());
                sgfensPedidosMicro.setTipoMoneda(ped.getTipoMoneda());
                sgfensPedidosMicro.setTiempoEntregaDias(ped.getTiempoEntregaDias());
                sgfensPedidosMicro.setComentarios(ped.getComentarios());
                sgfensPedidosMicro.setDescuentoTasa(ped.getDescuentoTasa());
                sgfensPedidosMicro.setDescuentoMonto(ped.getDescuentoMonto());
                sgfensPedidosMicro.setSubtotal(ped.getSubtotal());
                sgfensPedidosMicro.setTotal(ped.getTotal());
                sgfensPedidosMicro.setDescuentoMotivo(ped.getDescuentoMotivo());
                sgfensPedidosMicro.setFechaEntrega(ped.getFechaEntrega());
                sgfensPedidosMicro.setFechaTentativaPago(ped.getFechaTentativaPago());
                sgfensPedidosMicro.setSaldoPagado(ped.getSaldoPagado());
                sgfensPedidosMicro.setAdelanto(ped.getAdelanto());
                sgfensPedidosMicro.setIdComprobanteFiscal(ped.getIdComprobanteFiscal());
                sgfensPedidosMicro.setIdEstatusPedido(ped.getIdEstatusPedido());
                sgfensPedidosMicro.setLatitud(ped.getLatitud());
                sgfensPedidosMicro.setLongitud(ped.getLongitud());
                sgfensPedidosMicro.setFolioPedidoMovil(ped.getFolioPedidoMovil());
                sgfensPedidosMicro.setNombreImagenFirma(ped.getNombreImagenFirma());
                sgfensPedidosMicro.setIsModificadoConsola(ped.getIsModificadoConsola());
                sgfensPedidosMicro.setBonificacionDevolucion(ped.getBonificacionDevolucion());
                sgfensPedidosMicro.setIdUsuarioConductorAsignado(ped.getIdUsuarioConductorAsignado());
                sgfensPedidosMicro.setIdUsuarioVendedorAsignado(ped.getIdUsuarioVendedorAsignado());
                sgfensPedidosMicro.setFechaLimiteReasigancion(ped.getFechaLimiteReasigancion());
                sgfensPedidosMicro.setSincronizacionMicrosip(ped.getSincronizacionMicrosip());
                sgfensPedidosMicro.setConsigna(ped.getConsigna());
                sgfensPedidosMicro.setIdViaEmbarque(ped.getIdViaEmbarque());
                sgfensPedidosMicro.setClaveClienteSistemaTercero(claveClienteSistemaTercero);
                sgfensPedidosMicro.setClaveEmpleadoSistemaTercero(claveEmpleadoSistemaTercero);
                //--/-/-/
                
                SgfensPedidosMicrosipBean pedMiBO = new SgfensPedidosMicrosipBean();
                
                //if(tipo == 2){//si es una actualización, enviamos el id o clave del sistema de microsip (identificador)
                    QuartzPedido qpe = null;
                    try{qpe = new QuartzPedidoDaoImpl(conn).findByDynamicWhere(" ID_PEDIDO_EVC = " + ped.getIdPedido() + " AND ID_EMPRESA = " + idEmpresa+ " ORDER BY ID_QUARTZ DESC ", null)[0];}catch(Exception e){} //buscamos el registro correspondiente
                    try{
                        if(qpe != null){
                            if(qpe.getIdPedidoSistemTercero() > 0){
                                //pedMiBO.setIdPedidosMicrosip(qpe.getIdPedidoSistemTercero());
                                sgfensPedidosMicro.setIdPedidosMicrosip(qpe.getIdPedidoSistemTercero());
                            }else if(qpe.getClave() != null && !qpe.getClave().trim().equals("")){
                                pedMiBO.setClave(qpe.getClave());
                            }
                        }
                    }catch(Exception e){/*e.printStackTrace();*/}                
                //}
                
                pedMiBO.setPedidoDto(sgfensPedidosMicro);
                
                ///***Recuperamos todos los productos del pedido:
                List<com.tsp.microsip.bean.SgfensPedidoProducto> listProductos = new ArrayList<com.tsp.microsip.bean.SgfensPedidoProducto>();
                SgfensPedidoProducto[] productos = new SgfensPedidoProductoDaoImpl(conn).findWhereIdPedidoEquals(ped.getIdPedido());
                if(productos != null){
                    QuartzConceptoDaoImpl qcdi = new QuartzConceptoDaoImpl(conn);
                    QuartzAlmacenDaoImpl qadi = new QuartzAlmacenDaoImpl(conn);
                    
                    //////++++RECUPERAMOS SI SE HA REALIZADO UNA DEVOLUCION O UN CAMBIO:
                    SgfensPedidoDevolucionCambio[] devolucionCambios = null;
                    try{
                        devolucionCambios = new SgfensPedidoDevolucionCambioDaoImpl(conn).findWhereIdPedidoEquals(ped.getIdPedido());
                    }catch(Exception e){}                    
                    //////++++
                    
                    boolean cantidadDevueltaIgualCantidadPedida = false;
                    for(SgfensPedidoProducto p : productos){
                        cantidadDevueltaIgualCantidadPedida = false;
                        String claveConceptoSistemaTercero = "";
                        String claveAlmacenSistemaTercero = "";
                        //////++++verificamos si existe dentro de una devolucion o cambio
                        
                        if(devolucionCambios != null){////////////
                            for(SgfensPedidoDevolucionCambio devoCambio : devolucionCambios){
                                if(devoCambio.getIdPedido() == p.getIdPedido() && devoCambio.getIdConcepto() == p.getIdConcepto()){
                                    if(devoCambio.getIdTipo() == 1){//si es devolucion, solo vemos cuantos se regresaron:
                                        double cantidadDevuelta = devoCambio.getAptoParaVenta() + devoCambio.getNoAptoParaVenta();
                                        if(cantidadDevuelta == p.getCantidad()){
                                            cantidadDevueltaIgualCantidadPedida = true;
                                            p.setCantidad(0);
                                            p.setSubtotal(0);
                                        }else if(cantidadDevuelta < p.getCantidad()){
                                            cantidadDevueltaIgualCantidadPedida = false;
                                            p.setCantidad( new BigDecimal(p.getCantidad() - cantidadDevuelta).setScale(2, RoundingMode.HALF_UP).doubleValue() );
                                            p.setSubtotal( new BigDecimal(p.getCantidad()*p.getPrecioUnitario()).setScale(2, RoundingMode.HALF_UP).doubleValue() );
                                        }else if(cantidadDevuelta > p.getCantidad()){
                                            
                                        }
                                    }else if(devoCambio.getIdTipo() == 2){//si es un cambio, vemos por que productos se cambio y de que cantidad
                                        double cantidadCambiada = devoCambio.getAptoParaVenta() + devoCambio.getNoAptoParaVenta();
                                        if(cantidadCambiada == p.getCantidad()){                                                                                        
                                            Concepto concepto = null;
                                            try{
                                                concepto = new ConceptoBO(devoCambio.getIdConceptoEntregado(), conn).getConcepto();
                                            }catch(Exception e){}
                                            if(concepto != null){                                                
                                                p.setCantidad(devoCambio.getCantidadDevuelta());
                                                p.setSubtotal( new BigDecimal(devoCambio.getCantidadDevuelta() * concepto.getPrecio()).setScale(2, RoundingMode.HALF_UP).doubleValue() );
                                                p.setPrecioUnitario(new BigDecimal(concepto.getPrecio()).setScale(2, RoundingMode.HALF_UP).doubleValue() );
                                            }else{//si no lo cambiaron por algun producto no se agrega en el pedido
                                                cantidadDevueltaIgualCantidadPedida = true;
                                                p.setCantidad(0);
                                                p.setSubtotal(0);
                                            }
                                        }else if(cantidadCambiada < p.getCantidad()){
                                            p.setCantidad( new BigDecimal(p.getCantidad() - cantidadCambiada).setScale(2, RoundingMode.HALF_UP).doubleValue() );
                                            p.setSubtotal( new BigDecimal(p.getCantidad()*p.getPrecioUnitario()).setScale(2, RoundingMode.HALF_UP).doubleValue() );
                                            ///--- NEGOCIO PARA AGREGAR UN NUEVO PRODUCTO POR EL CUAL FUE CAMBIADO EL PRODUCTO:
                                                //--/-/-/ objeto bean independiente de microsip SgfensPedidoProducto
                                                com.tsp.microsip.bean.SgfensPedidoProducto productoMicroAdicional = new com.tsp.microsip.bean.SgfensPedidoProducto();
                                                
                                                Concepto concepto = null;
                                                try{
                                                    concepto = new ConceptoBO(devoCambio.getIdConceptoEntregado(), conn).getConcepto();
                                                    productoMicroAdicional.setIdentificacion(concepto.getIdentificacion());
                                                    productoMicroAdicional.setPrecioUnitario(concepto.getPrecio());
                                                    productoMicroAdicional.setSubtotal( new BigDecimal(concepto.getPrecio()*cantidadCambiada).setScale(2, RoundingMode.HALF_UP).doubleValue() );
                                                }catch(Exception e){}
                                                try{
                                                    QuartzConcepto qc = qcdi.findWhereIdConceptoEvcEquals(concepto.getIdConcepto())[0];
                                                    productoMicroAdicional.setIdConcepto(qc.getIdConceptoSistemTercero());
                                                    productoMicroAdicional.setClaveConceptoSistemaTercero(StringManage.getValidString(qc.getClave()));
                                                }catch(Exception e){
                                                    productoMicroAdicional.setIdConcepto(-1);
                                                    e.printStackTrace();
                                                }  
                                                //--obtenemos el ID del sistema teercero del almacen al que pertenece el producto:
                                                if(p.getIdAlmacenOrigen() > 0){//solo si existe un almacen asignado, lo buscamos.
                                                    try{
                                                        QuartzAlmacen qa = qadi.findWhereIdAlmacenEvcEquals(p.getIdAlmacenOrigen())[0];
                                                        p.setIdAlmacenOrigen(qa.getIdAlmacenSistemTercero());
                                                        claveAlmacenSistemaTercero = StringManage.getValidString(qa.getClave());
                                                    }catch(Exception e){
                                                        p.setIdAlmacenOrigen(-1);
                                                    }
                                                }
                                                //--
                                                productoMicroAdicional.setIdPedido(p.getIdPedido());                                                
                                                productoMicroAdicional.setDescripcion(p.getDescripcion());
                                                productoMicroAdicional.setUnidad(p.getUnidad());                                                
                                                productoMicroAdicional.setCantidad(cantidadCambiada);                                                
                                                productoMicroAdicional.setDescuentoPorcentaje(0);
                                                productoMicroAdicional.setDescuentoMonto(0);                                                
                                                productoMicroAdicional.setCostoUnitario(0);
                                                productoMicroAdicional.setPorcentajeComisionEmpleado(p.getPorcentajeComisionEmpleado());
                                                productoMicroAdicional.setCantidadEntregada(cantidadCambiada);
                                                productoMicroAdicional.setFechaEntrega(p.getFechaEntrega());
                                                productoMicroAdicional.setEstatus(p.getEstatus());
                                                productoMicroAdicional.setIdAlmacenOrigen(p.getIdAlmacenOrigen());
                                                productoMicroAdicional.setClaveAlmacenSistemaTercero(claveAlmacenSistemaTercero);
                                                listProductos.add(productoMicroAdicional); 
                                            ///---
                                        }
                                    }
                                }
                            }
                        }
                        //////++++
                        if(!cantidadDevueltaIgualCantidadPedida){
                            /////----Recuperamos el id del producto, pero el de microsip:
                            try{
                                QuartzConcepto qc = qcdi.findByDynamicWhere(" ID_CONCEPTO_EVC = " + p.getIdConcepto() + " ORDER BY ID_QUARTZ DESC ", null)[0];
                                p.setIdConcepto(qc.getIdConceptoSistemTercero());
                                claveConceptoSistemaTercero = StringManage.getValidString(qc.getClave());
                            }catch(Exception e){
                                //e.printStackTrace();
                                System.out.println("El Concepto con ID en pretoriano " + p.getIdConcepto()+ " no ha sido sincronizado en el sistema de terceros.\n" + e.toString());
                                p.setIdConcepto(-1);//Asignamos por defecto -1 por si no encontramos el correspondiente del sistema tercero
                            }
                            /////----                            
                            //--obtenemos el ID del sistema teercero del almacen al que pertenece el producto:
                            if(p.getIdAlmacenOrigen() > 0){//solo si existe un almacen asignado, lo buscamos.
                                try{
                                    QuartzAlmacen qa = qadi.findWhereIdAlmacenEvcEquals(p.getIdAlmacenOrigen())[0];
                                    p.setIdAlmacenOrigen(qa.getIdAlmacenSistemTercero());
                                    claveAlmacenSistemaTercero = StringManage.getValidString(qa.getClave());
                                }catch(Exception e){
                                    p.setIdAlmacenOrigen(-1);
                                }
                            }
                            //--                            
                            //--/-/-/ objeto bean independiente de microsip SgfensPedidoProducto
                            com.tsp.microsip.bean.SgfensPedidoProducto productoMicro = new com.tsp.microsip.bean.SgfensPedidoProducto();
                            productoMicro.setIdPedido(p.getIdPedido());
                            productoMicro.setIdConcepto(p.getIdConcepto());
                            productoMicro.setDescripcion(p.getDescripcion());
                            productoMicro.setUnidad(p.getUnidad());
                            productoMicro.setIdentificacion(p.getIdentificacion());
                            productoMicro.setCantidad(p.getCantidad());
                            productoMicro.setPrecioUnitario(p.getPrecioUnitario());
                            productoMicro.setDescuentoPorcentaje(p.getDescuentoPorcentaje());
                            productoMicro.setDescuentoMonto(p.getDescuentoMonto());
                            productoMicro.setSubtotal(p.getSubtotal());
                            productoMicro.setCostoUnitario(p.getCostoUnitario());
                            productoMicro.setPorcentajeComisionEmpleado(p.getPorcentajeComisionEmpleado());
                            productoMicro.setCantidadEntregada(p.getCantidadEntregada());
                            productoMicro.setFechaEntrega(p.getFechaEntrega());
                            productoMicro.setEstatus(p.getEstatus());
                            productoMicro.setIdAlmacenOrigen(p.getIdAlmacenOrigen());
                            productoMicro.setClaveConceptoSistemaTercero(claveConceptoSistemaTercero);
                            productoMicro.setClaveAlmacenSistemaTercero(claveAlmacenSistemaTercero);
                            //--/-/-/
                            listProductos.add(productoMicro); 
                        }
                    }
                }
                pedMiBO.setPedidoListProductos(listProductos);                
                ///***
                
                ////--- Cargamos los impuestos involucrados en el pedido:
                List<com.tsp.microsip.bean.SgfensPedidoImpuesto> listImpuestos = new ArrayList<com.tsp.microsip.bean.SgfensPedidoImpuesto>();
                SgfensPedidoImpuesto[] impuestos = new SgfensPedidoImpuestoDaoImpl(conn).findWhereIdPedidoEquals(ped.getIdPedido());
                if(impuestos != null){
                    QuartzImpuestoDaoImpl qidi = new QuartzImpuestoDaoImpl(conn);
                    for(SgfensPedidoImpuesto i : impuestos){
                        //Recuperamos registro de impuesto en pretoriano
                        Impuesto impuestoDto = new ImpuestoDaoImpl(conn).findByPrimaryKey(i.getIdImpuesto());
                        /////----Recuperamos el id del impuesto, pero el de microsip:
                        try{
                            QuartzImpuesto qi = qidi.findByDynamicWhere(" ID_IMPUESTO_EVC = " + i.getIdImpuesto() + " ORDER BY ID_QUARTZ DESC ", null)[0];
                            i.setIdImpuesto(qi.getIdImpuestoSistemTercero());
                        }catch(Exception e){
                            //e.printStackTrace();
                            System.out.println("El Impuesto con ID en pretoriano " + i.getIdImpuesto()+ " no ha sido sincronizado en el sistema de terceros.\n" + e.toString());
                            i.setIdImpuesto(-1);//Asignamos por defecto -1 por si no encontramos el correspondiente del sistema tercero
                        }
                        //--/-/-/ objeto bean independiente de microsip SgfensPedidoImpuesto
                        com.tsp.microsip.bean.SgfensPedidoImpuesto impuestoMicro = new com.tsp.microsip.bean.SgfensPedidoImpuesto();
                        impuestoMicro.setIdPedido(i.getIdPedido());
                        impuestoMicro.setIdImpuesto(i.getIdImpuesto());
                        //datos adicionales para sistemas terceros que no usan un catalogo de impuestos y requieren la info cada vez que se usa un pedido
                        if (impuestoDto!=null){
                            impuestoMicro.setNombre(impuestoDto.getNombre());
                            impuestoMicro.setPorcentaje(impuestoDto.getPorcentaje());
                            impuestoMicro.setTrasladado(impuestoDto.getTrasladado());//1=traslado, 0=retencion
                            impuestoMicro.setImpuestoLocal(impuestoDto.getImpuestoLocal());//0=federal, 1=local
                        }
                        
                        listImpuestos.add(impuestoMicro);
                    }
                }
                
                pedMiBO.setPedidoListImpuestos(listImpuestos);
                ////---
                
                
                sgfensPedidosCompartidos.add(pedMiBO);
                
            }        
            conn.close();
            return sgfensPedidosCompartidos; 
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public int setSgfensPedidosIdentificadores(List<QuartzPedido> quartzSgfensPedidos , int idEmpresa, String acceso){
        try{
            
            
            int idSistematercero  = 0; //Se obtiene del token acceso 1er token
           
            try{                
                
                StringTokenizer st = new StringTokenizer(acceso, "|");
                if(st.hasMoreElements()){
                    idSistematercero = Integer.parseInt(st.nextElement().toString().trim());                
                }
            }catch(Exception e){
                System.out.println("Error al obtener IdSitemaTercero desde Token");
                
            }
            
            
            Connection conn = ResourceManager.getConnection();
            SgfensPedidoDaoImpl sgfensPedidoDaoImpl = new SgfensPedidoDaoImpl(conn);
            List<QuartzPedido> sgfensPedidosIdSincronizacion = new ArrayList<QuartzPedido>();
            List<QuartzPedido> sgfensPedidosIdSincronizacionPorActualizar = new ArrayList<QuartzPedido>();
            QuartzPedidoDaoImpl quartzPedidoDaoImpl = new QuartzPedidoDaoImpl(conn);
            for(QuartzPedido quaPe : quartzSgfensPedidos){
                //Vemos si ya existe el registro:
                QuartzPedido quartzSgfensPedido = null;
                if(quaPe.getIdPedidoSistemTercero() > 0){ //verificamos si se buscara por id o por clave
                    //try{quartzSgfensPedido = new QuartzPedidoDaoImpl(conn).findByDynamicWhere( " ID_PEDIDO_EVC = " + quaPe.getIdPedidoEvc() + " AND ID_PEDIDO_SISTEM_TERCERO = " + quaPe.getIdPedidoSistemTercero(), null)[0];}catch(Exception e){/*e.printStackTrace();*/}
                    try{quartzSgfensPedido = quartzPedidoDaoImpl.findByDynamicWhere( " ID_PEDIDO_EVC = " + quaPe.getIdPedidoEvc() + " AND ID_EMPRESA = " + idEmpresa, null)[0];}catch(Exception e){/*e.printStackTrace();*/}
                }else if(quaPe.getClave() != null && !quaPe.getClave().equals("")){
                    try{quartzSgfensPedido = quartzPedidoDaoImpl.findByDynamicWhere( " ID_PEDIDO_EVC = " + quaPe.getIdPedidoEvc() + " AND CLAVE = '" + quaPe.getClave()  + "' AND ID_EMPRESA = " + idEmpresa , null)[0];}catch(Exception e){/*e.printStackTrace();*/}
                }

                if(quartzSgfensPedido == null){//si no existe lo insertamos
                    quartzSgfensPedido = new QuartzPedido();
                    quartzSgfensPedido.setIdPedidoEvc(quaPe.getIdPedidoEvc());
                    if(quaPe.getIdPedidoSistemTercero() > 0){
                        quartzSgfensPedido.setIdPedidoSistemTercero(quaPe.getIdPedidoSistemTercero());
                    }else{
                        quartzSgfensPedido.setIdPedidoSistemTercero(0);
                    }
                    if(quaPe.getClave() != null && !quaPe.getClave().equals("")){
                        quartzSgfensPedido.setClave(quaPe.getClave());
                    }else{
                        quartzSgfensPedido.setClave("");
                    }
                    quartzSgfensPedido.setIdEmpresa(idEmpresa);
                    sgfensPedidosIdSincronizacion.add(quartzSgfensPedido);
                }else{//si existe actualizamos los Identificadores IDs
                    /*quartzSgfensPedido.setIdPedidoEvc(quaPe.getIdPedidoEvc());
                    quartzSgfensPedido.setIdPedidoSistemTercero(quaPe.getIdPedidoSistemTercero());
                    quartzSgfensPedido.setIdEmpresa(idEmpresa);
                    sgfensPedidosIdSincronizacionPorActualizar.add(quartzSgfensPedido); */
                    
                    try{quartzPedidoDaoImpl.delete(quartzSgfensPedido.createPk());
                    QuartzPedido quartzPedidoClon = new QuartzPedido();
                    quartzPedidoClon.setIdPedidoEvc(quartzSgfensPedido.getIdPedidoEvc());
                    quartzPedidoClon.setIdPedidoSistemTercero(quaPe.getIdPedidoSistemTercero());
                    quartzPedidoClon.setClave(quartzSgfensPedido.getClave());
                    quartzPedidoClon.setIdSistemaTercero(idSistematercero);
                    quartzPedidoClon.setIdEmpresa(quartzSgfensPedido.getIdEmpresa());
                    quartzPedidoDaoImpl.insert(quartzPedidoClon);
                    }catch(Exception e){System.out.println("------------ Error al actualizar Identificadores de quartz de pedido: "+e.getMessage());}
                }
                
                //actualizamos la bandera de pedidos a sincronizado:
                try{
                    SgfensPedido sgfensPedido = new SGPedidoBO(conn).findPedidobyId(quaPe.getIdPedidoEvc());
                    sgfensPedido.setSincronizacionMicrosip(1);
                    sgfensPedidoDaoImpl.update(sgfensPedido.createPk(), sgfensPedido);
                }catch(Exception e){e.printStackTrace();}
                
            }
            int insetarActualizacion = 0;
            try{
                QuartzPedidoDaoImpl qpeDaoImp = new QuartzPedidoDaoImpl(conn);
                for(QuartzPedido insetImp : sgfensPedidosIdSincronizacion){//insertamos los registros:
                    qpeDaoImp.insert(insetImp);
                }
                try{
                for(QuartzPedido actualizaImp : sgfensPedidosIdSincronizacionPorActualizar){//actualizamos los registros
                    qpeDaoImp.update(actualizaImp.createPk(), actualizaImp);
                }}catch(Exception e2){}
                insetarActualizacion = 1;
            }catch(Exception e){e.printStackTrace();}
            
            conn.close();
            return insetarActualizacion;    
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }
    
    public ControlBean setPedidoMicrosip(List<SgfensPedidosMicrosipBean> pedidosMicrosipBean, int idEmpresa, String acceso){
        ControlBean controlBean = new ControlBean();
        String mensajePedido = "";
        Connection conn = null;
        try{            
            conn = ResourceManager.getConnection();
            SgfensPedidoDaoImpl pedidosDaoImpl = new SgfensPedidoDaoImpl(conn);
            SgfensPedidoProductoDaoImpl pedidosProductoDaoImpl = new SgfensPedidoProductoDaoImpl(conn);
            SgfensPedidoImpuestoDaoImpl pedidosImpuestoDaoImpl = new SgfensPedidoImpuestoDaoImpl(conn);
            QuartzPedidoDaoImpl pedidoDaoImpl = new QuartzPedidoDaoImpl(conn);
            QuartzPedido pedidoExistente = null;
            QuartzEmpleado quartzEmpleado = null;
            QuartzEmpleadoDaoImpl quartzEmpleadoDaoImpl = new QuartzEmpleadoDaoImpl(conn);
            QuartzCliente quartzCliente = null;
            QuartzClienteDaoImpl quartzClienteDaoImpl = new QuartzClienteDaoImpl(conn);
            QuartzImpuestoDaoImpl quartzImpuestoDaoImpl = new QuartzImpuestoDaoImpl(conn);
            QuartzConceptoDaoImpl quartzConceptoDaoImpl = new QuartzConceptoDaoImpl(conn);
            QuartzAlmacenDaoImpl quartzAlmacenDaoImpl = new QuartzAlmacenDaoImpl(conn);
            QuartzViaEmbarqueDaoImpl quartzViaEmbarqueDaoImpl = new QuartzViaEmbarqueDaoImpl(conn);
            QuartzViaEmbarque quartzViaEmbarque = null;
            QuartzEstatusPedidoDaoImpl quartzEstatusPedidoTerceroDaoImpl =  new QuartzEstatusPedidoDaoImpl(conn);
            
            
            
            
            int idSistematercero  = 0; //Se obtiene del token acceso 1er token
           
            try{                
                
                StringTokenizer st = new StringTokenizer(acceso, "|");
                if(st.hasMoreElements()){
                    idSistematercero = Integer.parseInt(st.nextElement().toString().trim());                
                }
            }catch(Exception e){
                System.out.println("Error al obtener IdSitemaTercero desde Token");
                
            }
            
            
            int contadorRegistrosNuevos = 0; //variable auxiliar para el numero de registros insertados
            int contadorRegistrosActualizados = 0; //variable auxiliar para el numero de registros insertados
            
           
                         
            for(SgfensPedidosMicrosipBean pedidoMicrosip : pedidosMicrosipBean){
                             
                
                pedidoExistente = null;
                String identificadorPedidoSistemaTercero = (pedidoMicrosip.getPedidoDto().getIdPedidosMicrosip()>0? ""+pedidoMicrosip.getPedidoDto().getIdPedidosMicrosip() : (StringManage.getValidString(pedidoMicrosip.getClave()).length()>0?pedidoMicrosip.getClave():""));
                //validamos si el pedido existe o no en el evc
                try{
                    try{//verificamos si se trata del mismo pedido, primero por medio del ID del sistema tercero
                        //pedidoExistente = pedidoDaoImpl.findByDynamicWhere("ID_PEDIDO_SISTEM_TERCERO = " + pedidoMicrosip.getPedidoDto().getIdPedidosMicrosip() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                        String queryOpcional = "";
                        if (pedidoMicrosip.getPedidoDto().getIdPedidosMicrosip()>0){
                            queryOpcional = " ID_PEDIDO_SISTEM_TERCERO = " + pedidoMicrosip.getPedidoDto().getIdPedidosMicrosip();
                        }else{
                            if (StringManage.getValidString(pedidoMicrosip.getClave()).length()>0)
                                queryOpcional = " CLAVE='" + StringManage.getValidString(pedidoMicrosip.getClave()) + "'";
                        }
                        pedidoExistente = pedidoDaoImpl.findByDynamicWhere(queryOpcional +" AND ID_EMPRESA = " + idEmpresa, null)[0];
                    }catch(Exception e){//verificamos ahora si existe el pedido por medio del ID del Pretoriano EVC.
                        try{
                            if (pedidoMicrosip.getPedidoDto().getIdPedido()>0)
                                pedidoExistente = pedidoDaoImpl.findByDynamicWhere("ID_PEDIDO_EVC = " + pedidoMicrosip.getPedidoDto().getIdPedido() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                        }catch(Exception e2){
                            if(StringManage.getValidString(pedidoMicrosip.getPedidoDto().getFolioPedido()).length()>0){
                                try{
                                    pedidoExistente = pedidoDaoImpl.findByDynamicWhere("ID_EMPRESA = " + idEmpresa + " AND ID_PEDIDO_EVC IN (SELECT ID_PEDIDO FROM SGFENS_PEDIDO WHERE FOLIO_PEDIDO = '"+ pedidoMicrosip.getPedidoDto().getFolioPedido().trim()+ "') ", null)[0];
                                }catch(Exception e3){pedidoExistente = null;}
                            }
                        }
                    }
                    
                }catch(Exception e1){}
                if(pedidoExistente == null){//pedido nuevo
                    
                 
                    try{
                        com.tsp.microsip.bean.SgfensPedido pedidoMicroSip = pedidoMicrosip.getPedidoDto();
                        //Pedido ultimoRegistroPedidos = pedidosDaoImpl.findLast();
                        //int idPedidoNuevo = ultimoRegistroPedidos.getIdPedido() + 1;            
                        //pedidoMicroSip.setIdPedido(idPedidoNuevo);
                        quartzEmpleado = null;
                        
                        try{//identificadore de vendedor-usuario
                            if (pedidoMicroSip.getIdUsuarioVendedor()>0){
                                quartzEmpleado = quartzEmpleadoDaoImpl.findByDynamicWhere( "ID_EMPLEADO_SISTEM_TERCERO = " + pedidoMicroSip.getIdUsuarioVendedor() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                            }else if(StringManage.getValidString(pedidoMicroSip.getClaveEmpleadoSistemaTercero()).length()>0){
                                quartzEmpleado = quartzEmpleadoDaoImpl.findByDynamicWhere( "CLAVE ='" + pedidoMicroSip.getIdUsuarioVendedor() + "' AND ID_EMPRESA = " + idEmpresa, null)[0];
                            }
                            Empleado empleado = new EmpleadoDaoImpl(conn).findByPrimaryKey(quartzEmpleado.getIdEmpleadoEvc());
                            pedidoMicroSip.setIdUsuarioVendedor(empleado.getIdUsuarios());
                        }catch(Exception e){
                            mensajePedido += " Error al relacionar el empleado con ID: "+pedidoMicroSip.getIdUsuarioVendedor()+" (puede que no exista o este sincronizado), relacionado al pedido con ID De Sistema Tercero: "+ identificadorPedidoSistemaTercero + "; ERROR: " + e.getMessage() + " , ";
                            controlBean.setExito(false);
                            break;
                        }
                           
                        quartzCliente = null;
                        try{//identificadores de cliente
                            //quartzCliente = quartzClienteDaoImpl.findByDynamicWhere( "ID_CLIENTE_SISTEM_TERCERO = " + pedidoMicroSip.getIdCliente() + " AND ID_EMPRESA = " + idEmpresa, null )[0];
                            if (pedidoMicroSip.getIdCliente()>0){
                                quartzCliente = quartzClienteDaoImpl.findByDynamicWhere( "ID_CLIENTE_SISTEM_TERCERO = " + pedidoMicroSip.getIdCliente() + " AND ID_EMPRESA = " + idEmpresa, null )[0];
                            }else if (StringManage.getValidString(pedidoMicroSip.getClaveClienteSistemaTercero()).length()>0){
                                quartzCliente = quartzClienteDaoImpl.findByDynamicWhere( "CLAVE ='" + pedidoMicroSip.getClaveClienteSistemaTercero() + "' AND ID_EMPRESA = " + idEmpresa, null )[0];
                            }
                            pedidoMicroSip.setIdCliente(quartzCliente.getIdClienteEvc());
                        }catch(Exception e){
                            mensajePedido += " Error al relacionar el cliente con ID: "+pedidoMicroSip.getIdCliente()+" (puede que no exista o este sincronizado), relacionado al pedido con ID de Sistema Tercero: "+identificadorPedidoSistemaTercero + "; ERROR: " + e.getMessage() + " , ";
                            controlBean.setExito(false);
                            break;
                        }
                        
                        quartzViaEmbarque = null;                        
                        try{
                            if (pedidoMicroSip.getIdViaEmbarque()>0){
                                quartzViaEmbarque = quartzViaEmbarqueDaoImpl.findByDynamicWhere( "ID_VIA_EMBARQUE_SISTEM_TERCERO = " + pedidoMicroSip.getIdViaEmbarque() + " AND ID_EMPRESA = " + idEmpresa, null)[0];                            
                                pedidoMicroSip.setIdViaEmbarque(quartzViaEmbarque.getIdViaEmbarqueEvc());
                            }
                        }catch(Exception e){
                            mensajePedido += " Error al relacionar la via de embarque con ID: "+pedidoMicroSip.getIdViaEmbarque()+" (puede que no exista o este sincronizado), relacionado al pedido con ID De Sistema Tercero: "+identificadorPedidoSistemaTercero + "; ERROR: " + e.getMessage() + " , ";
                            controlBean.setExito(false);
                            break;
                        }
                        
                        //Si el sistema es Magento   
                        if(idSistematercero == 5){                          
                            //Obtenemos equivalencia de pedido Magento vs EVC                            
                            pedidoMicroSip.setIdEstatusPedido(getEstatusPedidoOnlyMagento(pedidoMicroSip.getClaveEstatusPedidoSistemaTercero())); 
                        }else{
                            pedidoMicroSip.setIdEstatusPedido(pedidoMicroSip.getIdEstatusPedido()==0?2:pedidoMicroSip.getIdEstatusPedido());
                        }
                        //Fin sistema es Magento   
                        
                        
                        
                        //Estatus sistema tercero(Opcional)
                        int idEstatusPedidoTercero = 0;
                        try{
                             QuartzEstatusPedido quartzEstatusPedidoTercero = null;                               
                             
                            if (pedidoMicroSip.getIdEstatusPedidoSistemaTercero()>0){
                                quartzEstatusPedidoTercero = quartzEstatusPedidoTerceroDaoImpl.findByDynamicWhere( "ID_ESTATUS_SISTEMA_TERCERO = " + pedidoMicroSip.getIdCliente() + " AND ID_EMPRESA = " + idEmpresa, null )[0];                                
                            }else if(StringManage.getValidString(pedidoMicroSip.getClaveEstatusPedidoSistemaTercero().trim()).length()>0){
                                quartzEstatusPedidoTercero = quartzEstatusPedidoTerceroDaoImpl.findByDynamicWhere( "CLAVE ='" + pedidoMicroSip.getClaveEstatusPedidoSistemaTercero().trim() + "' AND ID_EMPRESA = " + idEmpresa, null)[0];
                            }                           
                            
                            idEstatusPedidoTercero = quartzEstatusPedidoTercero.getIdEstatusTerceroEvc();

                        }catch(Exception e){}
                        
                        
                        pedidoMicroSip.setSincronizacionMicrosip(1);
                        
                        
                        
                        //--/-/-/
                        SgfensPedido pedidoEvc = new SgfensPedido();
                        pedidoEvc.setIdUsuarioVendedor(pedidoMicroSip.getIdUsuarioVendedor());
                        pedidoEvc.setIdEmpresa(idEmpresa);
                        pedidoEvc.setIdCliente(pedidoMicroSip.getIdCliente());
                        
                        //Buscamos el último consecutivo de pedido para la empresa y aumentamos 1
                        int consecutivoPedidoEmpresa=1;
                        try{
                            SgfensPedido lastPedidoConsec = pedidosDaoImpl.findByDynamicWhere("ID_EMPRESA=" + idEmpresa + " ORDER BY CONSECUTIVO_PEDIDO DESC LIMIT 0,1 ", null)[0];
                            consecutivoPedidoEmpresa = lastPedidoConsec.getConsecutivoPedido() + 1;
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                        pedidoEvc.setConsecutivoPedido(consecutivoPedidoEmpresa);
                        pedidoEvc.setFolioPedido(pedidoMicroSip.getFolioPedido());
                        try{pedidoEvc.setFechaPedido(pedidoMicroSip.getFechaPedido());}catch(Exception e){}
                        pedidoEvc.setTipoMoneda("MXN");
                        pedidoEvc.setTiempoEntregaDias(pedidoMicroSip.getTiempoEntregaDias());
                        pedidoEvc.setComentarios(pedidoMicroSip.getComentarios());
                        pedidoEvc.setDescuentoTasa(new BigDecimal( pedidoMicroSip.getDescuentoTasa()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                        pedidoEvc.setDescuentoMonto(new BigDecimal( pedidoMicroSip.getDescuentoMonto()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                        pedidoEvc.setSubtotal(new BigDecimal( pedidoMicroSip.getSubtotal()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                        pedidoEvc.setTotal(new BigDecimal(pedidoMicroSip.getTotal()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                        pedidoEvc.setDescuentoMotivo(pedidoMicroSip.getDescuentoMotivo());
                        try{pedidoEvc.setFechaEntrega(pedidoMicroSip.getFechaEntrega());}catch(Exception e){}
                        try{pedidoEvc.setFechaTentativaPago(pedidoMicroSip.getFechaTentativaPago());}catch(Exception e){}
                        pedidoEvc.setSaldoPagado(pedidoMicroSip.getSaldoPagado());
                        pedidoEvc.setAdelanto(pedidoMicroSip.getAdelanto());
                        pedidoEvc.setSincronizacionMicrosip(pedidoMicroSip.getSincronizacionMicrosip());
                        pedidoEvc.setIdEstatusPedido(pedidoMicroSip.getIdEstatusPedido());
                        pedidoEvc.setIdViaEmbarque(pedidoMicroSip.getIdViaEmbarque());
                        pedidoEvc.setIdEstatusPedidoSistemaTercero(idEstatusPedidoTercero);                        
                        //--/-/-/ 
                        
                        SgfensPedidoPk pedidoPk = pedidosDaoImpl.insert(pedidoEvc);
                        
                        /////////++++++++++++++ insertamos los productos e impuestos relacionados a pedido:
                        QuartzConcepto quartzConcepto = null;                       
                        for(com.tsp.microsip.bean.SgfensPedidoProducto productoPedido : pedidoMicrosip.getPedidoListProductos()){
                            String identificadorConceptoSistemaTercero =  productoPedido.getIdConcepto()>0?""+productoPedido.getIdConcepto():(StringManage.getValidString(productoPedido.getClaveConceptoSistemaTercero()).length()>0?productoPedido.getClaveConceptoSistemaTercero():"");
                            try{
                                
                                try{//Lo ponemos en try-catch para que crear el concepto si no existe    
                                    //quartzConcepto = quartzConceptoDaoImpl.findByDynamicWhere("ID_CONCEPTO_SISTEM_TERCERO = " + productoPedido.getIdConcepto() + " AND ID_EMPRESA = " + idEmpresa, null)[0];

                                    if (productoPedido.getIdConcepto()>0){
                                        quartzConcepto = quartzConceptoDaoImpl.findByDynamicWhere("ID_CONCEPTO_SISTEM_TERCERO = " + productoPedido.getIdConcepto() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                                    }else if (StringManage.getValidString(productoPedido.getClaveConceptoSistemaTercero()).length()>0){
                                        quartzConcepto = quartzConceptoDaoImpl.findByDynamicWhere("CLAVE ='" + productoPedido.getClaveConceptoSistemaTercero() + "' AND ID_EMPRESA = " + idEmpresa, null)[0];
                                    }
                                }catch(Exception e){
                                    quartzConcepto = null;
                                }
                                
                                if(quartzConcepto==null){//Conceptos Genericos                                    
                                    
                                    try{
                                        Concepto conceptoGenerico = new Concepto();
                                        conceptoGenerico.setIdEmpresa(idEmpresa);
                                        conceptoGenerico.setNombre(new ConceptoBO(conn).encripta(productoPedido.getDescripcion()));  
                                        conceptoGenerico.setDescripcion(productoPedido.getDescripcion());
                                        conceptoGenerico.setNombreDesencriptado(productoPedido.getDescripcion());                                    
                                        conceptoGenerico.setPrecio(new BigDecimal(productoPedido.getPrecioUnitario()).setScale(2, RoundingMode.HALF_UP).floatValue());
                                        conceptoGenerico.setIdentificacion(productoPedido.getIdentificacion());   
                                        conceptoGenerico.setObservaciones("Producto creado como REFERENCIA de Sitema Tercero");
                                        conceptoGenerico.setSincronizacionMicrosip(1);
                                        conceptoGenerico.setIdEstatus(2);                                     
                                    
                                        ConceptoPk conGenerico  = new ConceptoDaoImpl(conn).insert(conceptoGenerico);
                                                                                
                                        QuartzConcepto conQuartzGenerico = new QuartzConcepto();
                                        conQuartzGenerico.setIdConceptoSistemTercero(productoPedido.getIdConcepto());
                                        conQuartzGenerico.setIdConceptoEvc(conGenerico.getIdConcepto());
                                        conQuartzGenerico.setClave(productoPedido.getIdentificacion());
                                        conQuartzGenerico.setIdEmpresa(idEmpresa);
                                        conQuartzGenerico.setIdSistemaTercero(idSistematercero);
                                        new QuartzConceptoDaoImpl(conn).insert(conQuartzGenerico);
                                        
                                        
                                        //BUSCAMOS NUEVAMENTE EL CONCEPTO UNA VEZ CREADO
                                        if (productoPedido.getIdConcepto()>0){
                                            quartzConcepto = quartzConceptoDaoImpl.findByDynamicWhere("ID_CONCEPTO_SISTEM_TERCERO = " + productoPedido.getIdConcepto() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                                        }else if (StringManage.getValidString(productoPedido.getClaveConceptoSistemaTercero()).length()>0){
                                            quartzConcepto = quartzConceptoDaoImpl.findByDynamicWhere("CLAVE ='" + productoPedido.getClaveConceptoSistemaTercero() + "' AND ID_EMPRESA = " + idEmpresa, null)[0];
                                        }                                        
                                        
                                    }catch(Exception e){
                                    
                                    }
                                    
                                }
                                
                                SgfensPedidoProducto producto = new SgfensPedidoProducto();
                                producto.setIdPedido(pedidoPk.getIdPedido());
                                producto.setIdConcepto(quartzConcepto.getIdConceptoEvc());
                                
                                boolean productoDuplicadoEnPedido = false;
                                try {
                                    SgfensPedidoProducto productoAnterior = pedidosProductoDaoImpl.findByPrimaryKey(producto.createPk());
                                    if (productoAnterior!=null){
                                        productoDuplicadoEnPedido = true;
                                        producto = productoAnterior;
                                    }
                                }catch(Exception ex){}
                                
                                producto.setCantidad(producto.getCantidad() + productoPedido.getCantidad());
                                producto.setSubtotal(producto.getSubtotal() + productoPedido.getSubtotal());
                                //producto.setCantidadEntregada(producto.getCantidadEntregada() + productoPedido.getCantidadEntregada());
                                producto.setDescuentoMonto(producto.getDescuentoMonto() + productoPedido.getDescuentoMonto());
                                if (!productoDuplicadoEnPedido){
                                    
                                    producto.setDescripcion(productoPedido.getDescripcion());
                                    producto.setUnidad(productoPedido.getUnidad());
                                    producto.setIdentificacion(productoPedido.getIdentificacion());
                                    producto.setPrecioUnitario(productoPedido.getPrecioUnitario());
                                    producto.setDescuentoPorcentaje(productoPedido.getDescuentoPorcentaje());
                                    producto.setCostoUnitario(productoPedido.getCostoUnitario());
                                    producto.setPorcentajeComisionEmpleado(productoPedido.getPorcentajeComisionEmpleado());
                                    producto.setFechaEntrega(productoPedido.getFechaEntrega());
                                    producto.setEstatus((short)1);
                                }
                                
                                
                                //Si el sistema es Magento   
                                if(idSistematercero == 5){   
                                    //Si el pedido es pendiente de entrega, ponemos 0 en prod entregado
                                    if(pedidoMicroSip.getIdEstatusPedido()==SGEstatusPedidoBO.ESTATUS_PENDIENTE){ 
                                        producto.setCantidadEntregada(0);
                                        producto.setFechaEntrega(null);
                                    }else{         
                                        producto.setCantidadEntregada(producto.getCantidadEntregada() + productoPedido.getCantidadEntregada());
                                    }
                                }else{
                                    producto.setCantidadEntregada(producto.getCantidadEntregada() + productoPedido.getCantidadEntregada());
                                }
                                
                                
                                
                                //--obtenemos el ID del sistema teercero del almacen al que pertenece el producto:
                                if(productoPedido.getIdAlmacenOrigen() > 0
                                        || StringManage.getValidString(productoPedido.getClaveAlmacenSistemaTercero()).length()>0 ){//solo si existe un almacen asignado, lo buscamos.
                                    try{
                                        QuartzAlmacen qa = null;
                                        if(productoPedido.getIdAlmacenOrigen() > 0){
                                            qa = quartzAlmacenDaoImpl.findByDynamicWhere("ID_ALMACEN_SISTEM_TERCERO = " + productoPedido.getIdAlmacenOrigen() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                                        }else if (StringManage.getValidString(productoPedido.getClaveAlmacenSistemaTercero()).length()>0){
                                            qa = quartzAlmacenDaoImpl.findByDynamicWhere("CLAVE = '" + productoPedido.getClaveAlmacenSistemaTercero() + "' AND ID_EMPRESA = " + idEmpresa, null)[0];
                                        }
                                        productoPedido.setIdAlmacenOrigen(qa.getIdAlmacenEvc());
                                    }catch(Exception e){
                                        productoPedido.setIdAlmacenOrigen(-1);
                                    }
                                }
                                //--
                                producto.setIdAlmacenOrigen(productoPedido.getIdAlmacenOrigen());
                                if (!productoDuplicadoEnPedido){
                                    pedidosProductoDaoImpl.insert(producto);
                                }else{
                                    //ya existia la tupla de pedido-producto, se le sumo una tupla similar
                                    pedidosProductoDaoImpl.update(producto.createPk(), producto);
                                }
                            }catch(Exception e){
                                //IMPORTANTE: NO QUITAR DE RESPUESTA SIMBOLOS PIPES '|' NI SIMBOLOS DE CORCHETE '[]' NI LA PALABRA 'ADVERTENCIA' YA QUE SE USA COMO TOKEN EN EL CLIENTE
                                if (pedidoMicrosip.getPedidoDto().getIdPedidosMicrosip()>0){
                                    mensajePedido += " -ADVERTENCIA-["+pedidoMicrosip.getPedidoDto().getIdPedidosMicrosip()+"] Error al insertar el concepto con ID: "+identificadorConceptoSistemaTercero+" (puede que no exista o este sincronizado), relacionado al pedido con ID: "+pedidoMicrosip.getPedidoDto().getIdPedidosMicrosip() + "; ERROR: " + e.getMessage() + " | ";
                                }else if ( StringManage.getValidString(pedidoMicrosip.getClave()).length()>0 ){
                                    mensajePedido += " -ADVERTENCIA-["+StringManage.getValidString(pedidoMicrosip.getClave())+"] Error al insertar el concepto con ID: "+identificadorConceptoSistemaTercero+" (puede que no exista o este sincronizado), relacionado al pedido con CLAVE: "+StringManage.getValidString(pedidoMicrosip.getClave()) + "; ERROR: " + e.getMessage() + " | ";
                                }
                                controlBean.setExito(false);
                            }
                        }
                        /////////++++++++++++++
                        
                        /////////--------------insertamos los impuestos relacionados
                        QuartzImpuesto quartzImpuesto = null;
                        SgfensPedidoImpuestoDaoImpl spidi = new SgfensPedidoImpuestoDaoImpl();
                        for(com.tsp.microsip.bean.SgfensPedidoImpuesto impuestoPedido : pedidoMicrosip.getPedidoListImpuestos()){
                            try{
                               
                                quartzImpuesto = quartzImpuestoDaoImpl.findByDynamicWhere("ID_IMPUESTO_SISTEM_TERCERO = "+ impuestoPedido.getIdImpuesto() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                                SgfensPedidoImpuesto impuesto = new SgfensPedidoImpuesto();
                                impuesto.setIdPedido(pedidoPk.getIdPedido());
                                impuesto.setIdImpuesto(quartzImpuesto.getIdImpuestoEvc());
                                //quartzImpuestoDaoImpl.insert(quartzImpuesto);
                                spidi.insert(impuesto);
                            }catch(Exception e){
                                mensajePedido += " Error al insertar el impuesto con ID: "+impuestoPedido.getIdImpuesto()+" (puede que no exista o este sincronizado), relacionado al pedido con ID: "+identificadorPedidoSistemaTercero + "; ERROR: " + e.getMessage() + " , ";
                                controlBean.setExito(false);
                            }
                        }
                        /////////--------------

                        ////insertamos la relacion con los Id's
                        QuartzPedido qc = new QuartzPedido();
                        qc.setClave( (pedidoMicrosip.getClave()!=null?pedidoMicrosip.getClave():null) );
                        qc.setIdPedidoEvc(pedidoPk.getIdPedido());
                        qc.setIdPedidoSistemTercero(pedidoMicrosip.getPedidoDto().getIdPedidosMicrosip());
                        qc.setIdSistemaTercero(idSistematercero);
                        qc.setIdEmpresa(idEmpresa);
                        pedidoDaoImpl.insert(qc);
                        contadorRegistrosNuevos++;
                    }catch(Exception e){
                        mensajePedido += " Error al insertar pedido con ID: "+identificadorPedidoSistemaTercero + "; ERROR: " + e.getMessage() + " , ";
                    }  
                }else{//actualizar
                    
                    try{
                        com.tsp.microsip.bean.SgfensPedido pedidoMicroSip = pedidoMicrosip.getPedidoDto();
                        pedidoMicroSip.setIdPedido(pedidoExistente.getIdPedidoEvc());
                        pedidoMicroSip.setSincronizacionMicrosip(1);
                        pedidoMicroSip.setIdEstatusPedido(pedidoMicroSip.getIdEstatusPedido()==0?2:pedidoMicroSip.getIdEstatusPedido());
                       
                        quartzEmpleado = null;
                        
                        try{//identificadore de vendedor-usuario
                            //quartzEmpleado = quartzEmpleadoDaoImpl.findByDynamicWhere( "ID_EMPLEADO_SISTEM_TERCERO = " + pedidoMicroSip.getIdUsuarioVendedor() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                            if (pedidoMicroSip.getIdUsuarioVendedor()>0){
                                quartzEmpleado = quartzEmpleadoDaoImpl.findByDynamicWhere( "ID_EMPLEADO_SISTEM_TERCERO = " + pedidoMicroSip.getIdUsuarioVendedor() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                            }else if(StringManage.getValidString(pedidoMicroSip.getClaveEmpleadoSistemaTercero()).length()>0){
                                quartzEmpleado = quartzEmpleadoDaoImpl.findByDynamicWhere( "CLAVE ='" + pedidoMicroSip.getClaveEmpleadoSistemaTercero() + "' AND ID_EMPRESA = " + idEmpresa, null)[0];
                            }
                            Empleado empleado = new EmpleadoDaoImpl(conn).findByPrimaryKey(quartzEmpleado.getIdEmpleadoEvc());
                            pedidoMicroSip.setIdUsuarioVendedor(empleado.getIdUsuarios());
                        }catch(Exception e){
                            mensajePedido += " Error al relacionar el empleado con ID: "+pedidoMicroSip.getIdUsuarioVendedor()+" (puede que no exista o este sincronizado), relacionado al pedido con ID: "+identificadorPedidoSistemaTercero + "; ERROR: " + e.getMessage() + " , ";
                            controlBean.setExito(false);
                            break;
                        }
                           
                        quartzCliente = null;
                        try{//identificadores de cliente
                            //quartzCliente = quartzClienteDaoImpl.findByDynamicWhere( "ID_CLIENTE_SISTEM_TERCERO = " + pedidoMicroSip.getIdCliente() + " AND ID_EMPRESA = " + idEmpresa, null )[0];
                            if (pedidoMicroSip.getIdCliente()>0){
                                quartzCliente = quartzClienteDaoImpl.findByDynamicWhere( "ID_CLIENTE_SISTEM_TERCERO = " + pedidoMicroSip.getIdCliente() + " AND ID_EMPRESA = " + idEmpresa, null )[0];
                            }else if (StringManage.getValidString(pedidoMicroSip.getClaveClienteSistemaTercero()).length()>0){
                                quartzCliente = quartzClienteDaoImpl.findByDynamicWhere( "CLAVE ='" + pedidoMicroSip.getClaveClienteSistemaTercero() + "' AND ID_EMPRESA = " + idEmpresa, null )[0];
                            }
                            pedidoMicroSip.setIdCliente(quartzCliente.getIdClienteEvc());
                        }catch(Exception e){
                            mensajePedido += " Error al relacionar el cliente con ID: "+pedidoMicroSip.getIdCliente()+" (puede que no exista o este sincronizado), relacionado al pedido con ID: "+identificadorPedidoSistemaTercero + "; ERROR: " + e.getMessage() + " , ";
                            controlBean.setExito(false);
                            break;
                        }
                        
                        quartzViaEmbarque = null;                        
                        try{
                            if (pedidoMicroSip.getIdViaEmbarque()>0){
                                quartzViaEmbarque = quartzViaEmbarqueDaoImpl.findByDynamicWhere( "ID_VIA_EMBARQUE_SISTEM_TERCERO = " + pedidoMicroSip.getIdViaEmbarque() + " AND ID_EMPRESA = " + idEmpresa, null)[0];                            
                                pedidoMicroSip.setIdViaEmbarque(quartzViaEmbarque.getIdViaEmbarqueEvc());
                            }
                        }catch(Exception e){
                            mensajePedido += " Error al relacionar la via de embarque con ID: "+pedidoMicroSip.getIdViaEmbarque()+" (puede que no exista o este sincronizado), relacionado al pedido con ID De Sistema Tercero: "+identificadorPedidoSistemaTercero + "; ERROR: " + e.getMessage() + " , ";
                            controlBean.setExito(false);
                            break;
                        }
                        
                        
                        //Si el sistema es Magento   
                        if(idSistematercero == 5){                          
                            //Obtenemos equivalencia de pedido Magento vs EVC                            
                            pedidoMicroSip.setIdEstatusPedido(getEstatusPedidoOnlyMagento(pedidoMicroSip.getClaveEstatusPedidoSistemaTercero())); 
                        }else{
                            pedidoMicroSip.setIdEstatusPedido(pedidoMicroSip.getIdEstatusPedido()==0?2:pedidoMicroSip.getIdEstatusPedido());
                        }
                        //Fin sistema es Magento   
                        
                        
                        //Estatus sistema tercero(Opcional)
                        int idEstatusPedidoTercero = 0;
                        try{
                             QuartzEstatusPedido quartzEstatusPedidoTercero = null;                               
                             
                            if (pedidoMicroSip.getIdEstatusPedidoSistemaTercero()>0){
                                quartzEstatusPedidoTercero = quartzEstatusPedidoTerceroDaoImpl.findByDynamicWhere( "ID_ESTATUS_SISTEMA_TERCERO = " + pedidoMicroSip.getIdCliente() + " AND ID_EMPRESA = " + idEmpresa, null )[0];                                
                            }else if(StringManage.getValidString(pedidoMicroSip.getClaveEstatusPedidoSistemaTercero().trim()).length()>0){
                                quartzEstatusPedidoTercero = quartzEstatusPedidoTerceroDaoImpl.findByDynamicWhere( "CLAVE ='" + pedidoMicroSip.getClaveEstatusPedidoSistemaTercero().trim() + "' AND ID_EMPRESA = " + idEmpresa, null)[0];
                            }                           
                            
                            idEstatusPedidoTercero = quartzEstatusPedidoTercero.getIdEstatusTerceroEvc();

                        }catch(Exception e){}
                        
                        
                        
                        //--/-/-/
                        SgfensPedido pedidoEvc = new SgfensPedido();
                        pedidoEvc.setIdPedido(pedidoMicroSip.getIdPedido());
                        pedidoEvc.setIdUsuarioVendedor(pedidoMicroSip.getIdUsuarioVendedor());
                        pedidoEvc.setIdEmpresa(idEmpresa);
                        pedidoEvc.setIdCliente(pedidoMicroSip.getIdCliente());
                                                
                        //Buscamos el último consecutivo de pedido para la empresa y aumentamos 1
                        /*int consecutivoPedidoEmpresa=1;
                        try{
                            SgfensPedido lastPedidoConsec = pedidosDaoImpl.findByDynamicWhere("ID_EMPRESA=" + idEmpresa + " ORDER BY CONSECUTIVO_PEDIDO DESC LIMIT 0,1 ", null)[0];
                            consecutivoPedidoEmpresa = lastPedidoConsec.getConsecutivoPedido() + 1;
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }*/
                        pedidoEvc.setConsecutivoPedido(pedidoMicroSip.getConsecutivoPedido());
                        pedidoEvc.setFolioPedido(pedidoMicroSip.getFolioPedido());
                        try{pedidoEvc.setFechaPedido(pedidoMicroSip.getFechaPedido());}catch(Exception e){}
                        pedidoEvc.setTipoMoneda("MXN");
                        pedidoEvc.setTiempoEntregaDias(pedidoMicroSip.getTiempoEntregaDias());
                        pedidoEvc.setComentarios(pedidoMicroSip.getComentarios());
                        pedidoEvc.setDescuentoTasa(pedidoMicroSip.getDescuentoTasa());
                        pedidoEvc.setDescuentoMonto(pedidoMicroSip.getDescuentoMonto());
                        pedidoEvc.setSubtotal(pedidoMicroSip.getSubtotal());
                        pedidoEvc.setTotal(pedidoMicroSip.getTotal());
                        pedidoEvc.setDescuentoMotivo(pedidoMicroSip.getDescuentoMotivo());
                        try{pedidoEvc.setFechaEntrega(pedidoMicroSip.getFechaEntrega());}catch(Exception e){}
                        try{pedidoEvc.setFechaTentativaPago(pedidoMicroSip.getFechaTentativaPago());}catch(Exception e){}
                        pedidoEvc.setSaldoPagado(pedidoMicroSip.getSaldoPagado());
                        pedidoEvc.setAdelanto(pedidoMicroSip.getAdelanto());
                        pedidoEvc.setSincronizacionMicrosip(pedidoMicroSip.getSincronizacionMicrosip());
                        pedidoEvc.setIdEstatusPedido(pedidoMicroSip.getIdEstatusPedido());
                        pedidoEvc.setIdViaEmbarque(pedidoMicroSip.getIdViaEmbarque());
                        pedidoEvc.setIdEstatusPedidoSistemaTercero(idEstatusPedidoTercero);
                        
                        //--/-/-/ 
                        pedidosDaoImpl.update(pedidoEvc.createPk(), pedidoEvc);
                        contadorRegistrosActualizados++;
                        
                        /////////++++++++++++++ insertamos/actualizamos los productos e impuestos relacionados a pedido:
                        //borramos los productos del pedido y los insertamos de nuevo:
                        SgfensPedidoProducto[] productosActualesBorrar = pedidosProductoDaoImpl.findWhereIdPedidoEquals(pedidoMicroSip.getIdPedido());
                        try{                        
                            for(SgfensPedidoProducto productoActualBorrar : productosActualesBorrar){
                                pedidosProductoDaoImpl.delete(productoActualBorrar.createPk());                            
                            }
                        }catch(Exception e){System.out.println("----ERROR: Problemas en el borraro de registros de conceptos de pedidos: "+e.getMessage());}
                                                
                        QuartzConcepto quartzConcepto = null;                        
                        for(com.tsp.microsip.bean.SgfensPedidoProducto productoPedido : pedidoMicrosip.getPedidoListProductos()){
                            String identificadorConceptoSistemaTercero =  productoPedido.getIdConcepto()>0?""+productoPedido.getIdConcepto():(StringManage.getValidString(productoPedido.getClaveConceptoSistemaTercero()).length()>0?productoPedido.getClaveConceptoSistemaTercero():"");
                            try{
                                                                
                                try{//Lo ponemos en try-catch para que crear el concepto si no existe    
                                    //quartzConcepto = quartzConceptoDaoImpl.findByDynamicWhere("ID_CONCEPTO_SISTEM_TERCERO = " + productoPedido.getIdConcepto() + " AND ID_EMPRESA = " + idEmpresa, null)[0];

                                    if (productoPedido.getIdConcepto()>0){
                                        quartzConcepto = quartzConceptoDaoImpl.findByDynamicWhere("ID_CONCEPTO_SISTEM_TERCERO = " + productoPedido.getIdConcepto() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                                    }else if (StringManage.getValidString(productoPedido.getClaveConceptoSistemaTercero()).length()>0){
                                        quartzConcepto = quartzConceptoDaoImpl.findByDynamicWhere("CLAVE ='" + productoPedido.getClaveConceptoSistemaTercero() + "' AND ID_EMPRESA = " + idEmpresa, null)[0];
                                    }
                                }catch(Exception e){
                                    quartzConcepto = null;
                                }
                                
                                if(quartzConcepto==null){//Conceptos Genericos                                    
                                    
                                    try{
                                        Concepto conceptoGenerico = new Concepto();
                                        conceptoGenerico.setIdEmpresa(idEmpresa);
                                        conceptoGenerico.setNombre(new ConceptoBO(conn).encripta(productoPedido.getDescripcion()));  
                                        conceptoGenerico.setDescripcion(productoPedido.getDescripcion());
                                        conceptoGenerico.setNombreDesencriptado(productoPedido.getDescripcion());                                    
                                        conceptoGenerico.setPrecio(new BigDecimal(productoPedido.getPrecioUnitario()).setScale(2, RoundingMode.HALF_UP).floatValue());
                                        conceptoGenerico.setIdentificacion(productoPedido.getIdentificacion());   
                                        conceptoGenerico.setObservaciones("Producto creado como REFERENCIA de Sitema Tercero");
                                        conceptoGenerico.setSincronizacionMicrosip(1);
                                        conceptoGenerico.setIdEstatus(1);                                     
                                    
                                        ConceptoPk conGenerico  = new ConceptoDaoImpl(conn).insert(conceptoGenerico);
                                                                                
                                        QuartzConcepto conQuartzGenerico = new QuartzConcepto();
                                        conQuartzGenerico.setIdConceptoSistemTercero(productoPedido.getIdConcepto());
                                        conQuartzGenerico.setIdConceptoEvc(conGenerico.getIdConcepto());
                                        conQuartzGenerico.setClave(productoPedido.getIdentificacion());
                                        conQuartzGenerico.setIdEmpresa(idEmpresa);
                                        conQuartzGenerico.setIdSistemaTercero(idSistematercero);
                                        new QuartzConceptoDaoImpl(conn).insert(conQuartzGenerico);
                                        
                                        
                                        //BUSCAMOS NUEVAMENTE EL CONCEPTO UNA VEZ CREADO
                                        if (productoPedido.getIdConcepto()>0){
                                            quartzConcepto = quartzConceptoDaoImpl.findByDynamicWhere("ID_CONCEPTO_SISTEM_TERCERO = " + productoPedido.getIdConcepto() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                                        }else if (StringManage.getValidString(productoPedido.getClaveConceptoSistemaTercero()).length()>0){
                                            quartzConcepto = quartzConceptoDaoImpl.findByDynamicWhere("CLAVE ='" + productoPedido.getClaveConceptoSistemaTercero() + "' AND ID_EMPRESA = " + idEmpresa, null)[0];
                                        }                                        
                                        
                                    }catch(Exception e){}
                                    
                                }
                                                               
                                
                                
                                SgfensPedidoProducto producto = new SgfensPedidoProducto();
                                producto.setIdPedido(pedidoMicroSip.getIdPedido());
                                producto.setIdConcepto(quartzConcepto.getIdConceptoEvc());
                                
                                boolean productoDuplicadoEnPedido = false;
                                try {
                                    SgfensPedidoProducto productoAnterior = pedidosProductoDaoImpl.findByPrimaryKey(producto.createPk());
                                    if (productoAnterior!=null){
                                        productoDuplicadoEnPedido = true;
                                        producto = productoAnterior;
                                    }
                                }catch(Exception ex){}
                                producto.setCantidad(producto.getCantidad() + productoPedido.getCantidad());
                                producto.setSubtotal(producto.getSubtotal() + productoPedido.getSubtotal());
                                //producto.setCantidadEntregada(producto.getCantidadEntregada() + productoPedido.getCantidadEntregada());
                                producto.setDescuentoMonto(producto.getDescuentoMonto() + productoPedido.getDescuentoMonto());
                                if (!productoDuplicadoEnPedido){
                                    producto.setDescripcion(productoPedido.getDescripcion());
                                    producto.setUnidad(productoPedido.getUnidad());
                                    producto.setIdentificacion(productoPedido.getIdentificacion()); 
                                    producto.setPrecioUnitario(productoPedido.getPrecioUnitario());                                   
                                    producto.setDescuentoPorcentaje(productoPedido.getDescuentoPorcentaje());
                                    producto.setCostoUnitario(productoPedido.getCostoUnitario());
                                    producto.setPorcentajeComisionEmpleado(productoPedido.getPorcentajeComisionEmpleado());
                                    producto.setFechaEntrega(productoPedido.getFechaEntrega());
                                    producto.setEstatus((short)1);
                                }
                                
                                
                                //Si el sistema es Magento   
                                if(idSistematercero == 5){   
                                    //Si el pedido es pendiente de entrega, ponemos 0 en prod entregado
                                    if(pedidoMicroSip.getIdEstatusPedido()==SGEstatusPedidoBO.ESTATUS_PENDIENTE){ 
                                        producto.setCantidadEntregada(0);
                                        producto.setFechaEntrega(null);
                                    }else{         
                                        producto.setCantidadEntregada(producto.getCantidadEntregada() + productoPedido.getCantidadEntregada());
                                    }
                                }else{
                                    producto.setCantidadEntregada(producto.getCantidadEntregada() + productoPedido.getCantidadEntregada());
                                }
                                
                                
                                //--obtenemos el ID del sistema teercero del almacen al que pertenece el producto:
                                if(productoPedido.getIdAlmacenOrigen() > 0
                                        || StringManage.getValidString(productoPedido.getClaveAlmacenSistemaTercero()).length()>0 ){//solo si existe un almacen asignado, lo buscamos.
                                    try{
                                        QuartzAlmacen qa = null;
                                        if(productoPedido.getIdAlmacenOrigen() > 0){
                                            qa = quartzAlmacenDaoImpl.findByDynamicWhere("ID_ALMACEN_SISTEM_TERCERO = " + productoPedido.getIdAlmacenOrigen() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                                        }else if (StringManage.getValidString(productoPedido.getClaveAlmacenSistemaTercero()).length()>0){
                                            qa = quartzAlmacenDaoImpl.findByDynamicWhere("CLAVE = '" + productoPedido.getClaveAlmacenSistemaTercero() + "' AND ID_EMPRESA = " + idEmpresa, null)[0];
                                        }
                                        productoPedido.setIdAlmacenOrigen(qa.getIdAlmacenEvc());
                                    }catch(Exception e){
                                        productoPedido.setIdAlmacenOrigen(-1);
                                    }
                                }
                                //--
                                producto.setIdAlmacenOrigen(productoPedido.getIdAlmacenOrigen());
                                if (!productoDuplicadoEnPedido){
                                    pedidosProductoDaoImpl.insert(producto);
                                }else{
                                    //ya existia la tupla de pedido-producto, se le sumo una tupla similar
                                    pedidosProductoDaoImpl.update(producto.createPk(), producto);
                                }
                            }catch(Exception e){
                                //IMPORTANTE: NO QUITAR DE RESPUESTA SIMBOLOS PIPES '|' NI SIMBOLOS DE CORCHETE '[]' NI LA PALABRA 'ADVERTENCIA' YA QUE SE USA COMO TOKEN EN EL CLIENTE
                                if (pedidoMicrosip.getPedidoDto().getIdPedidosMicrosip()>0){
                                    mensajePedido += " -ADVERTENCIA-["+pedidoMicrosip.getPedidoDto().getIdPedidosMicrosip()+"] Error al insertar/actualizar el concepto con ID: "+identificadorConceptoSistemaTercero+" (puede que no exista o este sincronizado), relacionado al pedido con ID: "+pedidoMicrosip.getPedidoDto().getIdPedidosMicrosip() + "; ERROR: " + e.getMessage() + " | ";
                                }else if ( StringManage.getValidString(pedidoMicrosip.getClave()).length()>0 ){
                                    mensajePedido += " -ADVERTENCIA-["+StringManage.getValidString(pedidoMicrosip.getClave())+"] Error al insertar/actualizar el concepto con ID: "+identificadorConceptoSistemaTercero+" (puede que no exista o este sincronizado), relacionado al pedido con CLAVE: "+StringManage.getValidString(pedidoMicrosip.getClave()) + "; ERROR: " + e.getMessage() + " | ";
                                }
                                 controlBean.setExito(false);
                            }
                        }
                        /////////++++++++++++++
                        
                        /////////--------------insertamos los impuestos relacionados
                        //borramos los registros de impuestos del pedido para insertarlos de nuevo:
                        SgfensPedidoImpuesto[] impuestoActualesBorrar = pedidosImpuestoDaoImpl.findWhereIdPedidoEquals(pedidoMicroSip.getIdPedido());
                        try{                        
                            for(SgfensPedidoImpuesto impuestoActualBorrar : impuestoActualesBorrar){
                                pedidosImpuestoDaoImpl.delete(impuestoActualBorrar.createPk());                            
                            }
                        }catch(Exception e){System.out.println("----ERROR: Problemas en el borraro de registros de impuestos de pedidos: "+e.getMessage());}
                         
                        
                        QuartzImpuesto quartzImpuesto = null;
                        SgfensPedidoImpuestoDaoImpl spidi = new SgfensPedidoImpuestoDaoImpl();
                        for(com.tsp.microsip.bean.SgfensPedidoImpuesto impuestoPedido : pedidoMicrosip.getPedidoListImpuestos()){
                            try{
                                //quartzImpuesto = quartzImpuestoDaoImpl.findWhereIdImpuestoSistemTerceroEquals(impuestoPedido.getIdImpuesto())[0];
                                quartzImpuesto = quartzImpuestoDaoImpl.findByDynamicWhere("ID_IMPUESTO_SISTEM_TERCERO = "+ impuestoPedido.getIdImpuesto() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                                SgfensPedidoImpuesto impuesto = new SgfensPedidoImpuesto();
                                impuesto.setIdPedido(pedidoMicroSip.getIdPedido());
                                impuesto.setIdImpuesto(quartzImpuesto.getIdImpuestoEvc());
                                //quartzImpuestoDaoImpl.insert(quartzImpuesto);
                                spidi.insert(impuesto);
                            }catch(Exception e){
                                mensajePedido += " Error al insertar/actualizar el impuesto con ID: "+impuestoPedido.getIdImpuesto()+" (puede que no exista o este sincronizado), relacionado al pedido con ID: "+identificadorPedidoSistemaTercero + "; ERROR: " + e.getMessage() + " , ";
                                controlBean.setExito(false);
                            }
                        }
                        /////////--------------
                        
                        ////actualizamos los ID
                        try{pedidoDaoImpl.delete(pedidoExistente.createPk());
                            QuartzPedido quartzPedidoClon = new QuartzPedido();
                            quartzPedidoClon.setIdPedidoEvc(pedidoExistente.getIdPedidoEvc());
                            quartzPedidoClon.setIdPedidoSistemTercero(pedidoMicrosip.getPedidoDto().getIdPedidosMicrosip());
                            quartzPedidoClon.setClave(pedidoMicrosip.getClave());
                            quartzPedidoClon.setIdSistemaTercero(pedidoExistente.getIdSistemaTercero());
                            quartzPedidoClon.setIdEmpresa(idEmpresa);
                            pedidoDaoImpl.insert(quartzPedidoClon);
                        }catch(Exception e){System.out.println("------------ Error al actualizar Identificadores de quartz de pedido: "+e.getMessage());
                            mensajePedido += " Error al actualizar IDs de pedido con ID: "+identificadorPedidoSistemaTercero + "; ERROR: " + e.getMessage() + " , ";
                        }
                        ////
                        
                    }catch(Exception e2){
                         mensajePedido += " Error al insertar pedido con ID: "+identificadorPedidoSistemaTercero + "; ERROR: " + e2.getMessage() + " , ";
                    }
        
                }
            }
            
            if(mensajePedido.trim().equals("")){
                controlBean.setExito(true);
            }
            controlBean.setMensajeError(mensajePedido);
            controlBean.setRegistradosNuevos(contadorRegistrosNuevos);            
            controlBean.setRegistradosActualizados(contadorRegistrosActualizados);
            
        }catch(Exception e){
            controlBean.setExito(false);
            controlBean.setMensajeError(e.getMessage());
        }finally{
            try {
                conn.close();
            } catch (Exception ex) {}
        }
        
        return controlBean;
    }
    
    
    public short getEstatusPedidoOnlyMagento(String estatusMagento){
       
        short idEstatus = 0;
                
        if(estatusMagento.equals("pending") 
           || estatusMagento.equals("pending_payment")
           || estatusMagento.equals("holded")
           || estatusMagento.equals("processing")){//PENDIENTE -> 1
           idEstatus  = SGEstatusPedidoBO.ESTATUS_PENDIENTE;     
            
        }else if(estatusMagento.equals("complete")){//ENTREGADO -> 2
            
           idEstatus  = SGEstatusPedidoBO.ESTATUS_ENTREGADO;  
           
        }else if(estatusMagento.equals("canceled")||
                estatusMagento.equals("fraud")){//CANCELADO -> 3
            
            idEstatus  = SGEstatusPedidoBO.ESTATUS_CANCELADO;     
            
        }else{
            /* Eststus pendiente(1): waiting_authorozation, processing_ogone, pending_paypal , processed_ogone,
            pending_ogone, paypal_reversed, paypal_canceled_reversal,payment_review,decline_ogone,
            cancel_ogone */
            idEstatus  = SGEstatusPedidoBO.ESTATUS_PENDIENTE;     
        }
        
        
        return idEstatus;
    }
    
    
}
