/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.microsip.ws;

import com.tsp.microsip.bean.AlmacenMicrosip;
import com.tsp.microsip.bean.ConceptoMicrosip;
import com.tsp.microsip.bo.AlmacenMicrosipBO;
import com.tsp.microsip.bean.ClientesMicrosipBean;
import com.tsp.microsip.bean.CobranzaAbonoMicrosip;
import com.tsp.microsip.bean.ControlBean;
import com.tsp.microsip.bean.EmpleadosMicrosipBean;
import com.tsp.microsip.bean.EstatusPedidoSistemaTerceroMicrosip;
import com.tsp.microsip.bean.ExistenciaAlmacenBean;
import com.tsp.microsip.bean.ExistenciaAlmacenMicrosip;
import com.tsp.microsip.bo.ConceptoMicrosipBO;
import com.tsp.microsip.bo.ImpuestosMicrosipBO;
import com.tsp.microsip.bean.ImpuestosMicrosipBean;
import com.tsp.microsip.bean.MovimientosMicrosipBean;
import com.tsp.microsip.bean.SgfensCobranzaMetodoPagoMicrosipBean;
import com.tsp.microsip.bean.SgfensPedidosMicrosipBean;
import com.tsp.microsip.bean.ViaEmbarqueMicrosip;
import com.tsp.microsip.bo.ClientesMicrosipBO;
import com.tsp.microsip.bo.CobranzaAbonoMicrosipBO;
import com.tsp.microsip.bo.EmpleadosMicrosipBO;
import com.tsp.microsip.bo.EstatusPedidosMicrosipBO;
import com.tsp.microsip.bo.ExistenciaAlmacenMicrosipBO;
import com.tsp.microsip.bo.MetodosPagoMicrosipBO;
import com.tsp.microsip.bo.MovimientosMicrosipBO;
import com.tsp.microsip.bo.PedidosMicrosipBO;
import com.tsp.microsip.bo.ViaEmbarqueMicrosipBO;

import com.tsp.sct.dao.dto.QuartzAlmacen;
import com.tsp.sct.dao.dto.QuartzCliente;
import com.tsp.sct.dao.dto.QuartzCobranzaAbono;
import com.tsp.sct.dao.dto.QuartzConcepto;
import com.tsp.sct.dao.dto.QuartzEmpleado;
import com.tsp.sct.dao.dto.QuartzImpuesto;
import com.tsp.sct.dao.dto.QuartzMovimiento;
import com.tsp.sct.dao.dto.QuartzPedido;
import com.tsp.sct.dao.dto.QuartzSgfensMetodoPago;
import com.tsp.sct.dao.dto.QuartzViaEmbarque;
import com.tsp.sct.dao.dto.ViaEmbarque;

import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author leonardo
 */
@WebService(serviceName = "InterconectaMicrosipWs")
public class InterconectaMicrosipWs {

    /**
     * This is a sample web service operation
     */
    /*@WebMethod(operationName = "hello", action="hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }*/
    
    @WebMethod(operationName = "cantidadClientes", action="cantidadClientes")
    public int cantidadClientes(        
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "tipo") int tipo,
        @WebParam(name = "idEmpresa") int idEmpresa) {            
        
        ClientesMicrosipBO microsipBO = new ClientesMicrosipBO();
        int cantidad = microsipBO.obtenerCantidadRegistros(tipo, idEmpresa);
        return cantidad;  
    }
    
    @WebMethod(operationName = "getClientes", action="getClientes")
    public List<ClientesMicrosipBean> getClientes(
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "tipo") int tipo,
        @WebParam(name = "idEmpresa") int idEmpresa) {            
        
        List<ClientesMicrosipBean> clientesCompartidos = new ArrayList<ClientesMicrosipBean>();
        ClientesMicrosipBO microsipBO = new ClientesMicrosipBO();
        clientesCompartidos = microsipBO.obtenerClientes(tipo, idEmpresa);
        
        return clientesCompartidos;
    }
    
    @WebMethod(operationName = "setClientesIdentificadores", action="setClientesIdentificadores")
    public int setClientesIdentificadores(        
        @WebParam(name = "acceso") String acceso,        
        @WebParam(name = "quartzCliente") List<QuartzCliente> quartzClientes, 
        @WebParam(name = "idEmpresa") int idEmpresa) {  
        
        ClientesMicrosipBO microsipBO = new ClientesMicrosipBO();
        int exitoso = microsipBO.setClientesIdentificadores(quartzClientes, idEmpresa , acceso );
        return exitoso;
    }
    
    @WebMethod(operationName = "cantidadImpuestos", action="cantidadImpuestos")
    public int cantidadImpuestos(        
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "tipo") int tipo,
        @WebParam(name = "idEmpresa") int idEmpresa) {            
        
        ImpuestosMicrosipBO microsipBO = new ImpuestosMicrosipBO();
        int cantidad = microsipBO.obtenerCantidadRegistros(tipo, idEmpresa);
        return cantidad;        
    }
    
    @WebMethod(operationName = "getImpuestos", action="getImpuestos")
    public List<ImpuestosMicrosipBean> getImpuestos(
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "tipo") int tipo,
        @WebParam(name = "idEmpresa") int idEmpresa) {            
        
        List<ImpuestosMicrosipBean> impuestosCompartidos = new ArrayList<ImpuestosMicrosipBean>();
        ImpuestosMicrosipBO microsipBO = new ImpuestosMicrosipBO();
        impuestosCompartidos = microsipBO.obtenerImpuestos(tipo, idEmpresa);
        
        return impuestosCompartidos;
    }
    
    @WebMethod(operationName = "setImpuestosIdentificadores", action="setImpuestosIdentificadores")
    public int setImpuestosIdentificadores(        
        @WebParam(name = "acceso") String acceso,        
        @WebParam(name = "quartzImpuesto") List<QuartzImpuesto> quartzImpuestos,
        @WebParam(name = "idEmpresa") int idEmpresa) { 
        
        ImpuestosMicrosipBO microsipBO = new ImpuestosMicrosipBO();
        int exitoso = microsipBO.setImpuestosIdentificadores(quartzImpuestos, idEmpresa);
        return exitoso;
        
    }
    
    
    @WebMethod(operationName = "cantidadConceptos", action="cantidadConceptos")
    public int cantidadConceptos(        
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "tipo") int tipo,
        @WebParam(name = "idEmpresa") int idEmpresa) {            
        
        ConceptoMicrosipBO microsipBO = new ConceptoMicrosipBO();
        int cantidad = microsipBO.getCantidad(tipo, idEmpresa);
        return cantidad;        
    }
    
    @WebMethod(operationName = "getConceptos", action="getConceptos")
    public List<ConceptoMicrosip> getConceptos(
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "tipo") int tipo,
        @WebParam(name = "idEmpresa") int idEmpresa) {            
        
        List<ConceptoMicrosip> conceptoCompartidos = new ArrayList<ConceptoMicrosip>();
        ConceptoMicrosipBO microsipBO = new ConceptoMicrosipBO();
        conceptoCompartidos = microsipBO.getRegistros(tipo, idEmpresa);
        
        return conceptoCompartidos;
    }
    
    @WebMethod(operationName = "setConceptoIdentificadores", action="setConceptoIdentificadores")
    public int setConceptoIdentificadores(        
        @WebParam(name = "acceso") String acceso,        
        @WebParam(name = "quartzConcepto") List<QuartzConcepto> quartzConcepto,
        @WebParam(name = "idEmpresa") int idEmpresa) { 
        
        ConceptoMicrosipBO microsipBO = new ConceptoMicrosipBO();
        int exitoso = microsipBO.setIdentificadores(quartzConcepto, idEmpresa, acceso);
        return exitoso;
        
    }
    
    @WebMethod(operationName = "cantidadAlmacenes", action="cantidadAlmacenes")
    public int cantidadAlmacenes(        
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "tipo") int tipo,
        @WebParam(name = "idEmpresa") int idEmpresa) {            
        
        AlmacenMicrosipBO microsipBO = new AlmacenMicrosipBO();
        int cantidad = microsipBO.getCantidad(tipo, idEmpresa);
        return cantidad;        
    }
    
    @WebMethod(operationName = "getAlmacenes", action="getAlmacenes")
    public List<AlmacenMicrosip> getAlmacenes(
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "tipo") int tipo,
        @WebParam(name = "idEmpresa") int idEmpresa) {            
        
        List<AlmacenMicrosip> almacenCompartidos;
        AlmacenMicrosipBO microsipBO = new AlmacenMicrosipBO();
        almacenCompartidos = microsipBO.getRegistros(tipo, idEmpresa);
        
        return almacenCompartidos;
    }
    
    @WebMethod(operationName = "setAlmacenIdentificadores", action="setAlmacenIdentificadores")
    public int setAlmacenIdentificadores(        
        @WebParam(name = "acceso") String acceso,        
        @WebParam(name = "quartzAlmacen") List<QuartzAlmacen> quartzAlmacen,
        @WebParam(name = "idEmpresa") int idEmpresa) { 
        
        AlmacenMicrosipBO microsipBO = new AlmacenMicrosipBO();
        int exitoso = microsipBO.setIdentificadores(quartzAlmacen, idEmpresa);
        return exitoso;
        
    }
    
    @WebMethod(operationName = "cantidadEmpleados", action="cantidadEmpleados")
    public int cantidadEmpleados(        
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "tipo") int tipo,
        @WebParam(name = "idEmpresa") int idEmpresa) {            
        
        EmpleadosMicrosipBO microsipBO = new EmpleadosMicrosipBO();
        int cantidad = microsipBO.obtenerCantidadRegistros(tipo, idEmpresa);
        return cantidad;        
    }
    
    @WebMethod(operationName = "getEmpleados", action="getEmpleados")
    public List<EmpleadosMicrosipBean> getEmpleados(
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "tipo") int tipo,
        @WebParam(name = "idEmpresa") int idEmpresa) {            
        
        List<EmpleadosMicrosipBean> impuestosCompartidos = new ArrayList<EmpleadosMicrosipBean>();
        EmpleadosMicrosipBO microsipBO = new EmpleadosMicrosipBO();
        impuestosCompartidos = microsipBO.obtenerEmpleados(tipo, idEmpresa);
        
        return impuestosCompartidos;
    }
    
    @WebMethod(operationName = "setEmpleadosIdentificadores", action="setEmpleadosIdentificadores")
    public int setEmpleadosIdentificadores(        
        @WebParam(name = "acceso") String acceso,        
        @WebParam(name = "quartzEmpleado") List<QuartzEmpleado> quartzEmpleados,
        @WebParam(name = "idEmpresa") int idEmpresa) { 
        
        EmpleadosMicrosipBO microsipBO = new EmpleadosMicrosipBO();
        int exitoso = microsipBO.setEmpleadosIdentificadores(quartzEmpleados, idEmpresa);
        return exitoso;
        
    }
    
    @WebMethod(operationName = "cantidadCobranzas", action="cantidadCobranzas")
    public int cantidadCobranzas(        
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "tipo") int tipo,
        @WebParam(name = "idEmpresa") int idEmpresa) {            
        
        CobranzaAbonoMicrosipBO microsipBO = new CobranzaAbonoMicrosipBO();
        int cantidad = microsipBO.getCantidad(tipo, idEmpresa);
        return cantidad;        
    }
    
    @WebMethod(operationName = "getCobranzas", action="getCobranzas")
    public List<CobranzaAbonoMicrosip> getCobranzas(
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "tipo") int tipo,
        @WebParam(name = "idEmpresa") int idEmpresa) {            
        
        List<CobranzaAbonoMicrosip> cobranzaCompartidos;
        CobranzaAbonoMicrosipBO microsipBO = new CobranzaAbonoMicrosipBO();
        cobranzaCompartidos = microsipBO.getRegistros(tipo, idEmpresa);
        
        return cobranzaCompartidos;
    }
    
    @WebMethod(operationName = "setCobranzaIdentificadores", action="setCobranzaIdentificadores")
    public int setCobranzaIdentificadores(        
        @WebParam(name = "acceso") String acceso,        
        @WebParam(name = "quartzCobranzaAbono") List<QuartzCobranzaAbono> quartzCobranzaAbono,
        @WebParam(name = "idEmpresa") int idEmpresa) { 
        
        CobranzaAbonoMicrosipBO microsipBO = new CobranzaAbonoMicrosipBO();
        int exitoso = microsipBO.setIdentificadores(quartzCobranzaAbono, idEmpresa);
        return exitoso;
        
    }
    
    @WebMethod(operationName = "cantidadPedidos", action="cantidadPedidos")
    public int cantidadPedidos(        
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "tipo") int tipo,
        @WebParam(name = "idEmpresa") int idEmpresa) {            
        
        PedidosMicrosipBO microsipBO = new PedidosMicrosipBO();
        int cantidad = microsipBO.obtenerCantidadRegistros(tipo, idEmpresa);
        return cantidad;        
    }
    
    @WebMethod(operationName = "getPedidos", action="getPedidos")
    public List<SgfensPedidosMicrosipBean> getPedidos(
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "tipo") int tipo,
        @WebParam(name = "idEmpresa") int idEmpresa) {            
        
        List<SgfensPedidosMicrosipBean> pedidosCompartidos = new ArrayList<SgfensPedidosMicrosipBean>();
        PedidosMicrosipBO microsipBO = new PedidosMicrosipBO();
        pedidosCompartidos = microsipBO.obtenerSgfensPedidos(tipo, idEmpresa);
        
        return pedidosCompartidos;
    }
    
    @WebMethod(operationName = "setPedidosIdentificadores", action="setPedidosIdentificadores")
    public int setPedidosIdentificadores(
        @WebParam(name = "acceso") String acceso,        
        @WebParam(name = "quartzPedido") List<QuartzPedido> quartzPedidos,
        @WebParam(name = "idEmpresa") int idEmpresa) { 
        
        PedidosMicrosipBO microsipBO = new PedidosMicrosipBO();
        int exitoso = microsipBO.setSgfensPedidosIdentificadores(quartzPedidos, idEmpresa , acceso);
        return exitoso;
        
    }
    
    @WebMethod(operationName = "cantidadMetodosPago", action="cantidadMetodosPago")
    public int cantidadMetodosPago(        
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "tipo") int tipo,
        @WebParam(name = "idEmpresa") int idEmpresa) {            
        
        MetodosPagoMicrosipBO microsipBO = new MetodosPagoMicrosipBO();
        int cantidad = microsipBO.obtenerCantidadRegistros(tipo, idEmpresa);
        return cantidad;  
    }
    
    @WebMethod(operationName = "getMetodosPago", action="getMetodosPago")
    public List<SgfensCobranzaMetodoPagoMicrosipBean> getMetodosPago(
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "tipo") int tipo,
        @WebParam(name = "idEmpresa") int idEmpresa) {
        
        List<SgfensCobranzaMetodoPagoMicrosipBean> metodosPagoCompartidos = new ArrayList<SgfensCobranzaMetodoPagoMicrosipBean>();
        MetodosPagoMicrosipBO microsipBO = new MetodosPagoMicrosipBO();
        metodosPagoCompartidos = microsipBO.obtenerMetodosPago(tipo, idEmpresa);
        
        return metodosPagoCompartidos;
    }
    
    @WebMethod(operationName = "setMetodosPagoIdentificadores", action="setMetodosPagoIdentificadores")
    public int setMetodosPagoIdentificadores(        
        @WebParam(name = "acceso") String acceso,        
        @WebParam(name = "quartzSgfensMetodoPago") List<QuartzSgfensMetodoPago> quartzMetodosPago, 
        @WebParam(name = "idEmpresa") int idEmpresa) {  
        
        MetodosPagoMicrosipBO microsipBO = new MetodosPagoMicrosipBO();
        int exitoso = microsipBO.setSgfensCobranzaMetodoPagoIdentificadores(quartzMetodosPago, idEmpresa);
        return exitoso;
    }
    
    // -------------------- sincronizacion de Microsip a EVC
    
    @WebMethod(operationName = "setClientes", action="setClientes")
    public ControlBean setClientes(
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "idEmpresa") int idEmpresa,
        @WebParam(name = "clientesMicrosipBean") List<ClientesMicrosipBean> clientesMicrosipBean) {
                
        ClientesMicrosipBO microsipBO = new ClientesMicrosipBO();
        ControlBean controlBean = microsipBO.setClienteMicrosip(clientesMicrosipBean, idEmpresa, acceso );
        
        return controlBean;
    }
    
    @WebMethod(operationName = "setImpuestos", action="setImpuestos") 
    public ControlBean setImpuestos(
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "idEmpresa") int idEmpresa,
        @WebParam(name = "impuestosMicrosipBean") List<ImpuestosMicrosipBean> clientesMicrosipBean) {
                
        ImpuestosMicrosipBO microsipBO = new ImpuestosMicrosipBO();
        ControlBean controlBean = microsipBO.setImpuestoMicrosip(clientesMicrosipBean, idEmpresa, acceso);
        
        return controlBean;
    }
    
    @WebMethod(operationName = "setConceptos", action="setConceptos") 
    public ControlBean setConceptos(
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "idEmpresa") int idEmpresa,
        @WebParam(name = "conceptosMicrosipBean") List<ConceptoMicrosip> conceptosMicrosipBean) {
                
        ConceptoMicrosipBO microsipBO = new ConceptoMicrosipBO();
        ControlBean controlBean = microsipBO.setConceptoMicrosip(conceptosMicrosipBean, idEmpresa, acceso);
        
        return controlBean;
    }
    
    @WebMethod(operationName = "setAlmacenes", action="setAlmacenes") 
    public ControlBean setAlmacenes(
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "idEmpresa") int idEmpresa,
        @WebParam(name = "almacenesMicrosipBean") List<AlmacenMicrosip> almacenesMicrosipBean) {
                
        AlmacenMicrosipBO microsipBO = new AlmacenMicrosipBO();
        ControlBean controlBean = microsipBO.setAlmacenMicrosip(almacenesMicrosipBean, idEmpresa, acceso);
        
        return controlBean;
    }
    
    @WebMethod(operationName = "setEmpleados", action="setEmpleados") 
    public ControlBean setEmpleados(
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "idEmpresa") int idEmpresa,
        @WebParam(name = "empleadosMicrosipBean") List<EmpleadosMicrosipBean> empleadosMicrosipBean) {
                
        EmpleadosMicrosipBO microsipBO = new EmpleadosMicrosipBO();
        ControlBean controlBean = microsipBO.setEmpleadoMicrosip(empleadosMicrosipBean, idEmpresa);
        
        return controlBean;
    }
    
    @WebMethod(operationName = "setPedidos", action="setPedidos") 
    public ControlBean setPedidos(
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "idEmpresa") int idEmpresa,
        @WebParam(name = "pedidosMicrosipBean") List<SgfensPedidosMicrosipBean> pedidosMicrosipBean) {
                
        PedidosMicrosipBO microsipBO = new PedidosMicrosipBO();
        ControlBean controlBean = microsipBO.setPedidoMicrosip(pedidosMicrosipBean, idEmpresa, acceso);
        
        return controlBean;
    }
    
    @WebMethod(operationName = "setCobranzas", action="setCobranzas") 
    public ControlBean setCobranzas(
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "idEmpresa") int idEmpresa,
        @WebParam(name = "cobranzasMicrosipBean") List<CobranzaAbonoMicrosip> cobranzasMicrosipBean) {
                
        CobranzaAbonoMicrosipBO microsipBO = new CobranzaAbonoMicrosipBO();
        ControlBean controlBean = microsipBO.setCobranzaMicrosip(cobranzasMicrosipBean, idEmpresa, acceso);
        
        return controlBean;
    }
    
    @WebMethod(operationName = "cantidadMovimientos", action="cantidadMovimientos")
    public int cantidadMovimientos(        
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "tipo") int tipo,
        @WebParam(name = "idEmpresa") int idEmpresa,
        @WebParam(name = "tipoMovimiento") int tipoMovimiento) {            
        
        MovimientosMicrosipBO microsipBO = new MovimientosMicrosipBO();
        int cantidad = microsipBO.obtenerCantidadRegistros(tipo, idEmpresa, tipoMovimiento);
        return cantidad;  
    }
    
    @WebMethod(operationName = "getMovimientos", action="getMovimientos")
    public List<MovimientosMicrosipBean> getMovimientos(
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "tipo") int tipo,
        @WebParam(name = "idEmpresa") int idEmpresa,
        @WebParam(name = "tipoMovimiento") int tipoMovimiento) {            
        
        List<MovimientosMicrosipBean> movimientosCompartidos = new ArrayList<MovimientosMicrosipBean>();
        MovimientosMicrosipBO microsipBO = new MovimientosMicrosipBO();
        movimientosCompartidos = microsipBO.obtenerMovimientos(tipo, idEmpresa, tipoMovimiento);
        
        return movimientosCompartidos;
    }
    
    @WebMethod(operationName = "setMovimientosIdentificadores", action="setMovimientosIdentificadores")
    public int setMovimientosIdentificadores(        
        @WebParam(name = "acceso") String acceso,        
        @WebParam(name = "quartzMovimiento") List<QuartzMovimiento> quartzMovimientos, 
        @WebParam(name = "idEmpresa") int idEmpresa) {  
        
        MovimientosMicrosipBO microsipBO = new MovimientosMicrosipBO();
        int exitoso = microsipBO.setMovimientosIdentificadores(quartzMovimientos, idEmpresa);
        return exitoso;
    }
    
    @WebMethod(operationName = "setMovimientos", action="setMovimientos")
    public ControlBean setMovimientos(
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "idEmpresa") int idEmpresa,
        @WebParam(name = "movimientosMicrosipBean") List<MovimientosMicrosipBean> movimientosMicrosipBean) {
                
        MovimientosMicrosipBO microsipBO = new MovimientosMicrosipBO();
        ControlBean controlBean = microsipBO.setMovimientoMicrosip(movimientosMicrosipBean, idEmpresa);
        
        return controlBean;
    }
    
    @WebMethod(operationName = "cantidadViaEmbarques", action="cantidadViaEmbarques")
    public int cantidadViaEmbarques(        
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "tipo") int tipo,
        @WebParam(name = "idEmpresa") int idEmpresa) {            
        
        ViaEmbarqueMicrosipBO microsipBO = new ViaEmbarqueMicrosipBO();
        int cantidad = microsipBO.getCantidad(tipo, idEmpresa);
        return cantidad;  
    }
    
    @WebMethod(operationName = "getViaEmbarques", action="getViaEmbarques")
    public List<ViaEmbarqueMicrosip> getViaEmbarques(
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "tipo") int tipo,
        @WebParam(name = "idEmpresa") int idEmpresa) {            
        
        List<ViaEmbarqueMicrosip> viaEmbarquesCompartidos = new ArrayList<ViaEmbarqueMicrosip>();
        ViaEmbarqueMicrosipBO microsipBO = new ViaEmbarqueMicrosipBO();
        viaEmbarquesCompartidos = microsipBO.getRegistros(tipo, idEmpresa);
        
        return viaEmbarquesCompartidos;
    }
    
    @WebMethod(operationName = "setViaEmbarquesIdentificadores", action="setViaEmbarquesIdentificadores")
    public int setViaEmbarquesIdentificadores(        
        @WebParam(name = "acceso") String acceso,        
        @WebParam(name = "quartzViaEmbarque") List<QuartzViaEmbarque> quartzViaEmbarques, 
        @WebParam(name = "idEmpresa") int idEmpresa) {  
        
        ViaEmbarqueMicrosipBO microsipBO = new ViaEmbarqueMicrosipBO();
        int exitoso = microsipBO.setViaEmbarquesIdentificadores(quartzViaEmbarques, idEmpresa);
        return exitoso;
    }
    
    @WebMethod(operationName = "setViaEmbarques", action="setViaEmbarques")
    public ControlBean setViaEmbarques(
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "idEmpresa") int idEmpresa,
        @WebParam(name = "viaEmbarquesMicrosipBean") List<ViaEmbarqueMicrosip> viaEmbarquesMicrosipBean) {
                
        ViaEmbarqueMicrosipBO microsipBO = new ViaEmbarqueMicrosipBO();
        ControlBean controlBean = microsipBO.setViaEmbarqueMicrosip(viaEmbarquesMicrosipBean, idEmpresa);
        
        return controlBean;
    }
    
    
    //No se necesita la tabla quartz ya que se podra ejecutar en cualquie momento que se solicite,
    // Este metodo usa los conceptos ya sincronzados
    @WebMethod(operationName = "setInventarioActual", action="setInventarioActual")
    public ControlBean setInventarioActual(
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "idEmpresa") int idEmpresa,
        @WebParam(name = "InventarioActual") List<ExistenciaAlmacenMicrosip> existenciaAlmacenMicrosipBean) {
                
        ExistenciaAlmacenMicrosipBO microsipBO = new ExistenciaAlmacenMicrosipBO();
        ControlBean controlBean = microsipBO.setExistenciaAlmacenMicrosip(existenciaAlmacenMicrosipBean, idEmpresa, acceso);
        
        return controlBean;
    }
    
    
    @WebMethod(operationName = "setEstatusPedidoTercero", action="setEstatusPedidoTercero")
    public ControlBean setEstatusPedidoTercero(
        @WebParam(name = "acceso") String acceso,
        @WebParam(name = "idEmpresa") int idEmpresa,
        @WebParam(name = "EstatusPedidoTercero") List<EstatusPedidoSistemaTerceroMicrosip> EstatusPedidoSistemaTerceroBean) {
                
        EstatusPedidosMicrosipBO microsipBO = new EstatusPedidosMicrosipBO();
        ControlBean controlBean = microsipBO.setEstatusPedidosMicrosip(EstatusPedidoSistemaTerceroBean, idEmpresa, acceso);
        
        return controlBean;
    }
    
}
