/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws;

import com.google.gson.Gson;
import com.tsp.sgfens.ws.bo.ConsultaWsBO;
import com.tsp.sgfens.ws.bo.CotizacionWsBO;
import com.tsp.sgfens.ws.bo.InsertaActualizaWsBO;
import com.tsp.sgfens.ws.bo.ModuloCrWsBO;
import com.tsp.sgfens.ws.bo.PaquetesWsBO;
import com.tsp.sgfens.ws.bo.PedidoWsBO;
import com.tsp.sgfens.ws.request.UsuarioNuevoDtoRequest;
import com.tsp.sgfens.ws.response.*;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author ISCesarMartinez
 Servicio Web para interconexión optimizado para consumo de sistemas móviles.
 */
@WebService(serviceName = "WSConexionMovil")
public class WSConexionMovil {

    /**
     * Método para verificar credenciales de acceso
     * para usuario desde dispositivo móvil (empleado)
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @return String en formato JSON representando un objeto tipo LoginEmpleadoMovilResponse
     */
    @WebMethod(operationName = "loginByEmpleado", action="loginByEmpleado")
    public String loginByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson ) {
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        System.out.println("METODO: loginByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        //Efectuamos operación
        LoginEmpleadoMovilResponse response = consultaWsBO.loginByEmpleado(empleadoDtoRequestJson);
                
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    /**
     * Método para recibir la actualización de ubicación
     * geográfica del empleado, usando latitud y longitud.
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @return String con formato JSON representando un objeto de tipo WSResponse
     */
    @WebMethod(operationName = "actualizarUbicacionEmpleado", action="actualizarUbicacionEmpleado")
    public String actualizarUbicacionEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson ) {
        InsertaActualizaWsBO insertaWsBO = new InsertaActualizaWsBO();
        
        System.out.println("METODO: actualizarUbicacionEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        //Efectuamos operación
        WSResponse response = insertaWsBO.actualizarUbicacionEmpleado(empleadoDtoRequestJson);
                
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    @WebMethod(operationName = "getMediaByConcepto", action="getMediaByConcepto")
    public String getMediaByConcepto(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson ,
            @WebParam(name = "conceptoDtoRequestJson") String conceptoDtoRequestJson ){
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        ConsultaConceptosResponse response = consultaWsBO.getMediaByConcepto(empleadoDtoRequestJson, conceptoDtoRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    /**
     * Método para consultar el listado de Conceptos de una empresa
     * haciendo autenticación por empleado desde un dispositivo móvil
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @return String con formato JSON representando un objeto de tipo ConsultaConceptosResponse
     */
    @WebMethod(operationName = "getCatalogoConceptosByEmpleado", action="getCatalogoConceptosByEmpleado")
    public String getCatalogoConceptosByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson ) {        
        System.out.println("METODO: getCatalogoConceptosByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        ConsultaConceptosResponse response = consultaWsBO.getCatalogoConceptosByEmpleado(empleadoDtoRequestJson);
                
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    /**
     * Método para consultar la bitacora de créditos de operación
     * haciendo autenticación por empleado desde un dispositivo móvil
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @return String con formato JSON representando un objeto de tipo ConsultaBitacoraResponse
     */
    @WebMethod(operationName = "getBitacoraCreditosOperacionByEmpleado", action="getBitacoraCreditosOperacionByEmpleado")
     public String getBitacoraCreditosOperacionByEmpleado(
        @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson ) {        
        System.out.println("METODO: getCatalogoConceptosByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        ConsultaBitacoraCreditosResponse response = consultaWsBO.getListaBitacoraCreditosByEmpleado(empleadoDtoRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    /**
     * Método para consultar el listado de Conceptos de una empresa
     * haciendo autenticación por empleado desde un dispositivo móvil
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @return String con formato JSON representando un objeto de tipo ConsultaConceptosResponse
     */
    @WebMethod(operationName = "getCatalogoConceptosByEmpleadoWiFi", action="getCatalogoConceptosByEmpleadoWiFi")
    public String getCatalogoConceptosByEmpleadoWiFi(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson ) {        
        System.out.println("METODO: getCatalogoConceptosByEmpleadoWiFi \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        ConsultaConceptosResponse response = consultaWsBO.getCatalogoConceptosByEmpleadoWiFi(empleadoDtoRequestJson);
                
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    /**
     * Método para obtener el catalogo de Clientes de una Empresa
     * haciendo autenticación por empleado desde un dispositivo móvil
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo ConsultaConceptosResponse
     * @return String con formato JSON representando un objeto de tipo ConsultaConceptosResponse
     */
    @WebMethod(operationName = "getCatalogoClientesByEmpleado", action="getCatalogoClientesByEmpleado")
    public String getCatalogoClientesByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson ) {
        
        System.out.println("METODO: getCatalogoClientesByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        ConsultaClientesResponse response = consultaWsBO.getCatalogoClientesByEmpleado(empleadoDtoRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    @WebMethod(operationName = "getCatalogoProveedoresByEmpleado", action = "getCatalogoProveedoresByEmpleado")
    public String getCatalogoProveedoresByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson){
        System.out.println("METODO: getCatalogoProveedoresByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        ConsultaProveedoresResponse response = consultaWsBO.getCatalogoProveedores(empleadoDtoRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
     @WebMethod(operationName = "insertProveedor", action = "insertProveedor")
    public String insertProveedor(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "registroProveedorDtoRequestJson") String registroProveedorDtoRequestJson){
        
        System.out.println("METODO: insertProveedor \n");
        System.out.println("REQUEST JSON: \n" + registroProveedorDtoRequestJson);
        
        InsertaActualizaWsBO insertDev = new InsertaActualizaWsBO();
        
        Gson gson = new Gson();
        
        WSResponseInsert response = insertDev.insertRegistroProveedorResponse(empleadoDtoRequestJson, registroProveedorDtoRequestJson);
        
        //Transformamos de objeto a formato JSON
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
     @WebMethod(operationName = "insertProveedorProducto", action = "insertProveedorProducto")
    public String insertProveedorProducto(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "registroProveedorProductoDtoRequestJson") String registroProveedorProductoDtoRequestJson){
        
        System.out.println("METODO: insertProveedorProducto \n");
        System.out.println("REQUEST JSON: \n" + registroProveedorProductoDtoRequestJson);
        
        InsertaActualizaWsBO insertDev = new InsertaActualizaWsBO();
        
        Gson gson = new Gson();
        
        WSResponseInsert response = insertDev.insertRegistroProveedorProductoResponse(empleadoDtoRequestJson, registroProveedorProductoDtoRequestJson);
        
        //Transformamos de objeto a formato JSON
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    @WebMethod(operationName = "insertaHistorial", action = "insertaHistorial")
    public String insertaHistorial(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "insertaHistorialDtoRequestJson") String insertaHistorialDtoRequestJson){
        System.out.println("METODO: insertaHistorial \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        System.out.println("\t" + insertaHistorialDtoRequestJson);
        
        InsertaActualizaWsBO insertaWsBO = new InsertaActualizaWsBO();
        
        HistorialResponse response = insertaWsBO.insertaHistorialByEmpleado(empleadoDtoRequestJson, insertaHistorialDtoRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    /**
     * Agrega un nuevo cliente en el catalogo de una empresa utilizando
     * las credenciales de autenticación de un empleado desde dispositivo móvil.
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @param crearClienteRequestJson String con formato JSON representando un objeto de tipo CrearClienteRequest
     * @return String con formato JSON representando un objeto de tipo WSResponseInsert
     */
    @WebMethod(operationName = "insertaClienteSCTByEmpleado", action="insertaClienteSCTByEmpleado")
    public String insertaClienteSCTByEmpleado(
        @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
        @WebParam(name = "crearClienteRequestJson") String crearClienteRequestJson
            ) {
        
        System.out.println("METODO: insertaClienteSCTByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        System.out.println("\t" + crearClienteRequestJson);
        
        InsertaActualizaWsBO insertaWsBO = new InsertaActualizaWsBO();
        
        WSResponseInsert response = insertaWsBO.insertaClienteByEmpleado(empleadoDtoRequestJson, crearClienteRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    @WebMethod(operationName = "insertaActualizaGastoByEmpleado", action = "insertaActualizaGastoByEmpleado")
    public String insertaActualizaGastoByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "gastoDtoRequestJson") String gastoDtoRequestJson){
        InsertaActualizaWsBO insertaActualizaWsBO = new InsertaActualizaWsBO();
        Gson gson = new Gson();
        System.out.println("METODO: insertaActualizaGastoByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        System.out.println("\t" + gastoDtoRequestJson);
        
        WSResponseInsert wSResponseInsert = insertaActualizaWsBO.insertaActualizaGastoByEmpleado(empleadoDtoRequestJson, gastoDtoRequestJson);
                
        //Transformamos de objeto a formato JSON
        String jsonResponse = gson.toJson(wSResponseInsert);
        
        return jsonResponse;
    }
    
    @WebMethod(operationName = "insertaActualizaProspecto", action="insertaActualizaProspecto")
    public String insertaActualizaProspecto(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "prospectoDtoRequestJson") String prospectoDtoRequestJson
            ) {
        InsertaActualizaWsBO insertaActualizaWsBO = new InsertaActualizaWsBO();
        
        Gson gson = new Gson();
        
        System.out.println("METODO: insertaActualizaProspecto \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        System.out.println("\t" + prospectoDtoRequestJson);
        
        //Efectuamos operación
        WSResponseInsert wSResponseInsert = insertaActualizaWsBO.insertaActualizaProspecto(empleadoDtoRequestJson,prospectoDtoRequestJson);
                
        //Transformamos de objeto a formato JSON
        String jsonResponse = gson.toJson(wSResponseInsert);
        
        return jsonResponse;
        
    }
    
    
    //METODOS PARA PEDIDOS, FACTURACIÓN Y ABONOS
    //-------------------------------------------------------------------------------
    
    @WebMethod(operationName = "creaPedidoByEmpleado", action="creaPedidoByEmpleado")
    public String creaPedidoByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "crearPedidoRequestJson") String crearPedidoRequestJson
            ) {
        
        System.out.println("METODO: creaPedidoByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        System.out.println("\t" + crearPedidoRequestJson);
        
        PedidoWsBO pedidoWsBO = new PedidoWsBO();
        
        CrearPedidoResponse response = pedidoWsBO.crearPedidoByEmpleado(empleadoDtoRequestJson, crearPedidoRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    @WebMethod(operationName = "modificaPedido", action="modificaPedido")
    public String modificaPedido(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "modificarPedidoRequestJson") String modificarPedidoRequestJson
            ) {
        
        System.out.println("METODO: modificaPedido \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        System.out.println("\t" + modificarPedidoRequestJson);
        
        PedidoWsBO pedidoWsBO = new PedidoWsBO();
        
        ModificarPedidoResponse response = pedidoWsBO.modificaPedido(empleadoDtoRequestJson, modificarPedidoRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    @WebMethod(operationName = "cancelarPedido", action="cancelarPedido")
    public String cancelarPedido(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "cancelarPedidoRequestJson") String cancelarPedidoRequestJson
            ) {
        
        System.out.println("METODO: cancelarPedido \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        System.out.println("\t" + cancelarPedidoRequestJson);
        
        PedidoWsBO pedidoWsBO = new PedidoWsBO();
        
        WSResponse response = pedidoWsBO.cancelarPedido(empleadoDtoRequestJson, cancelarPedidoRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    @WebMethod(operationName = "registraAbonoPedido", action="registraAbonoPedido")
    public String registraAbonoPedido(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "registrarAbonoRequestJson") String registrarAbonoRequestJson
            ) {
        
        System.out.println("METODO: registraAbonoPedido \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        System.out.println("\t" + registrarAbonoRequestJson);
        
        PedidoWsBO pedidoWsBO = new PedidoWsBO();
        
        CrearAbonoPedidoResponse response = pedidoWsBO.registraAbonoPedido(empleadoDtoRequestJson, registrarAbonoRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    @WebMethod(operationName = "cancelaAbonoPedido", action="cancelaAbonoPedido")
    public String cancelaAbonoPedido(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "cancelarAbonoRequestJson") String cancelarAbonoRequestJson
            ) {
        
        System.out.println("METODO: cancelaAbonoPedido \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        System.out.println("\t" + cancelarAbonoRequestJson);
        
        PedidoWsBO pedidoWsBO = new PedidoWsBO();
        
        CancelarAbonoPedidoResponse response = pedidoWsBO.cancelaAbonoPedido(empleadoDtoRequestJson, cancelarAbonoRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    @WebMethod(operationName = "crearFacturaDePedido", action="crearFacturaDePedido")
    public String crearFacturaDePedido(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "crearFacturaDePedidoRequestJson") String crearFacturaDePedidoRequestJson
            ) {
        
        System.out.println("METODO: crearFacturaDePedido \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        System.out.println("\t" + crearFacturaDePedidoRequestJson);
        
        PedidoWsBO pedidoWsBO = new PedidoWsBO();
        
        CrearFacturaResponse response = pedidoWsBO.crearFacturaDePedido(empleadoDtoRequestJson, crearFacturaDePedidoRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    @WebMethod(operationName = "reenviarCorreoPedidoFactura", action="reenviarCorreoPedidoFactura")
    public String reenviarCorreoPedidoFactura(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "reenviaComprobantesRequestJson") String reenviaComprobantesRequestJson
            ) {
        
        System.out.println("METODO: reenviarPedidoFactura \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        System.out.println("\t" + reenviaComprobantesRequestJson);
        
        PedidoWsBO pedidoWsBO = new PedidoWsBO();
        
        WSResponse response = pedidoWsBO.reenviarCorreoPedidoFactura(empleadoDtoRequestJson, reenviaComprobantesRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    
    @WebMethod(operationName = "reenviarCorreoComprobanteAbonoBoucher", action="reenviarCorreoComprobanteAbonoBoucher")
    public String reenviarCorreoComprobanteAbonoBoucher(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "reenviaComprobantesRequestJson") String reenviaComprobantesRequestJson
            ) {
        
        System.out.println("METODO: reenviarCorreoComprobanteAbonoBoucher \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        System.out.println("\t" + reenviaComprobantesRequestJson);
        
        PedidoWsBO pedidoWsBO = new PedidoWsBO();
        
        WSResponse response = pedidoWsBO.reenviarCorreoComprobanteAbonoBoucher(empleadoDtoRequestJson, reenviaComprobantesRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    
    
    //FIN METODOS PARA PEDIDOS, FACTURACIÓN Y ABONOS
    //-------------------------------------------------------------------------------
    
    
    //------------------------------------------------------------------------------
    //METODOS PARA MENSAJERIA
    //------------------------------------------------------------------------------
    
    /**
     * Consulta los mensajes recibidos para cierto empleado
     * asociado a un dispositivo móvil.
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @param getMensajesMovilRequestJson String con formato JSON representando un objeto de tipo MensajesMovilRequest
     * @return String con formato JSON representando un objeto de tipo ConsultaMensajesMovilResponse
     */
    @WebMethod(operationName = "mensajesConsultaRecibidos", action="mensajesConsultaRecibidos")
    public String mensajesConsultaRecibidos(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "getMensajesMovilRequestJson") String getMensajesMovilRequestJson){
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        System.out.println("METODO: mensajesConsultaRecibidos \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson + "\n" + getMensajesMovilRequestJson);
        
        //Efectuamos operación
        ConsultaMensajesMovilResponse response = consultaWsBO.getMensajesMovilRecibidos(empleadoDtoRequestJson,getMensajesMovilRequestJson);
                
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    /**
     * Consulta los mensajes enviados desde cierto empleado
     * asociado a un dispositivo móvil.
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @param getMensajesMovilRequestJson String con formato JSON representando un objeto de tipo MensajesMovilRequest
     * @return String con formato JSON representando un objeto de tipo ConsultaMensajesMovilResponse
     */
    @WebMethod(operationName = "mensajesConsultaEnviados", action="mensajesConsultaEnviados")
    public String mensajesConsultaEnviados(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "getMensajesMovilRequestJson") String getMensajesMovilRequestJson){
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
                
        System.out.println("METODO: mensajesConsultaEnviados \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson + "\n" + getMensajesMovilRequestJson);
        
        //Efectuamos operación
        ConsultaMensajesMovilResponse response = consultaWsBO.getMensajesMovilEnviados(empleadoDtoRequestJson,getMensajesMovilRequestJson);
                
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    /**
     * Crea un nuevo mensaje de comunicación interna,
     * usando los datos de un empleado emisor y receptor específicos.
     * <p/>
     * Puede ser tambien enviado hacia la consola central.
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @param sendMensajesMovilRequestJson String con formato JSON representando un objeto de tipo SendMensajesMovilRequest
     * @return String con formato JSON representando un objeto de tipo SendMensajesMovilResponse
     */
    @WebMethod(operationName = "mensajesEnviarNuevo", action="mensajesEnviarNuevo")
    public String mensajesEnviarNuevo(
        @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
        @WebParam(name = "sendMensajesMovilRequestJson") String sendMensajesMovilRequestJson
            ) {
        
        InsertaActualizaWsBO insertaActualizaWsBO = new InsertaActualizaWsBO();
        
        System.out.println("METODO: mensajesEnviarNuevo \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson + "\n" +sendMensajesMovilRequestJson);
        
        //Efectuamos operación
        SendMensajesMovilResponse response = insertaActualizaWsBO.insertaMensajeMovilByEmpleado(empleadoDtoRequestJson,sendMensajesMovilRequestJson);
                
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    /**
     * Obtiene la lista de empleados que corresponden a la misma
     * empresa que el usuario/empleado que hace la consulta.
     * <br/>
     * Es decir, sus compañeros de trabajo. Esto es, para poder establecer comunicación mediante mensajeria.
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @return String con formato JSON representando un objeto de tipo ConsultaEmpleadosResponse
     */
    @WebMethod(operationName = "getEmpleadosByEmpleado", action="getEmpleadosByEmpleado")
    public String getEmpleadosByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson
            ) {
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        System.out.println("METODO: getEmpleadosByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        //Efectuamos operación
        ConsultaEmpleadosResponse response = consultaWsBO.getEmpleadosByEmpleado(empleadoDtoRequestJson);
                
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    /**
     * Crea un nuevo estatus proveniente de un empleado-dispositivo móvil
     * para informar sobre las acciones que lleva a cabo en el momento de notificación.
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @param sendEstatusMensajesMovilRequestJson String con formato JSON representando un objeto de tipo SendEstatusMensajesMovilRequest
     * @return String con formato JSON representando un objeto de tipo SendEstatusMensajesMovilResponse
     */
    @WebMethod(operationName = "mensajesEstatusEnviarNuevo", action="mensajesEstatusEnviarNuevo")
    public String mensajesEstatusEnviarNuevo(
        @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
        @WebParam(name = "sendEstatusMensajesMovilRequestJson") String sendEstatusMensajesMovilRequestJson
            ) {
        
        InsertaActualizaWsBO insertaActualizaWsBO = new InsertaActualizaWsBO();
        
        System.out.println("METODO: mensajesEstatusEnviarNuevo \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson + "\n" +sendEstatusMensajesMovilRequestJson);
        
        //Efectuamos operación
        SendEstatusMensajesMovilResponse response = insertaActualizaWsBO.insertaEstatusMensajeMovilByEmpleado(empleadoDtoRequestJson,sendEstatusMensajesMovilRequestJson);
                
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    //------------------------------------------------------------------------------
    // FIN METODOS PARA MENSAJERIA
    //------------------------------------------------------------------------------
    
    
    @WebMethod(operationName = "getRutasByEmpleado", action="getRutasByEmpleado")
    public String getRutasByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson) {
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        Gson gson = new Gson();
        
        System.out.println("METODO: getRutasByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        //Efectuamos operación
        ConsultaRutasResponse response = consultaWsBO.getRutasByEmpleado(empleadoDtoRequestJson);
                
        //Transformamos de objeto a formato JSON
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
        
    }
    
    @WebMethod(operationName = "registrarRutaMarcadoresVisitados", action="registrarRutaMarcadoresVisitados")
    public String registrarRutaMarcadoresVisitados(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "rutaMarcadoresIDs") int[] rutaMarcadoresIDs) {
        InsertaActualizaWsBO insertaWsBO = new InsertaActualizaWsBO();
        
        Gson gson = new Gson();
        
        System.out.println("METODO: registrarRutaMarcadoresVisitados \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        System.out.println("IDs Marcadores visitados: \n" + rutaMarcadoresIDs);
        
        //Efectuamos operación
        WSResponse response = insertaWsBO.registrarRutaMarcadoresVisitados(empleadoDtoRequestJson,rutaMarcadoresIDs);
                
        //Transformamos de objeto a formato JSON
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
        
    }
    
    @WebMethod(operationName = "getGeocercasByEmpleado", action="getGeocercasByEmpleado")
    public String getGeocercasByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson) {
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        Gson gson = new Gson();
        
        System.out.println("METODO: getGeocercasByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        //Efectuamos operación
        ConsultaGeocercasResponse response = consultaWsBO.getGeocercasByEmpleado(empleadoDtoRequestJson);
                
        //Transformamos de objeto a formato JSON
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
        
    }
    
        
    /**
     * Método para consultar el listado de Paquetes de una empresa
     * haciendo autenticación por empleadodesde un dispositivo móvil
     */
    @WebMethod(operationName = "getCatalogoPaquetesByEmpleado", action="getCatalogoPaquetesByEmpleado")
    public String getCatalogoPaquetesByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson){
        System.out.println("METODO: getCatalogoPaquetesByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        PaquetesWsBO paquetesBO = new PaquetesWsBO();
        
        ConsultaPaquetesResponse response = paquetesBO.getPaquetesByEmpleado(empleadoDtoRequestJson);
        
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    @WebMethod(operationName = "getPedidosByEmpleado", action="getPedidosByEmpleado")
    public String getPedidosByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "consultaPedidosRequestJson") String consultaPedidosRequestJson) {
        
        System.out.println("METODO: getPedidosByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        System.out.println(consultaPedidosRequestJson);
        
        PedidoWsBO pedidoWsBO = new PedidoWsBO();
        
        ConsultaPedidosResponse response = pedidoWsBO.getPedidosByEmpleado(empleadoDtoRequestJson, consultaPedidosRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    @WebMethod(operationName = "getInventarioInicialByEmpleado", action = "getInventarioInicialByEmpleado")
    public String getInventarioInicialByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson){
        System.out.println("METODO: getInventarioInicialByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        Gson gson = new Gson();
        ConsultaInventarioInicialResponse resp = consultaWsBO.getInventarioInicialByEmpleado(empleadoDtoRequestJson);
        
        String jsonResponse = gson.toJson(resp);
        
        return jsonResponse;
    }
    
    @WebMethod(operationName = "getAgendaByEmpleado", action = "getAgendaByEmpleado")
    public String getAgendaByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson){
        System.out.println("METODO: getAgendaByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        Gson gson = new Gson();
        
        ConsultaAgendaResponse resp = consultaWsBO.getAgendaByEmpleado(empleadoDtoRequestJson);
        
        String jsonResponse = gson.toJson(resp);
        
        return jsonResponse;
    }
    
    @WebMethod(operationName = "updateAgendaEstatus", action = "updateAgendaEstatus")
    public String updateAgendaEstatus(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "agendaDtoRequestJson") String agendaDtoRequestJson){
        System.out.println("METODO: updateAgendaEstatus \n");
        System.out.println("REQUEST JSON: \n" + agendaDtoRequestJson);
        
        InsertaActualizaWsBO updateAgenda = new InsertaActualizaWsBO();
        
        Gson gson = new Gson();
        
        UpdateAgendaResponse response = updateAgenda.updateAgendaEstatusResponse(empleadoDtoRequestJson,agendaDtoRequestJson);
        
        //Transformamos de objeto a formato JSON
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    @WebMethod(operationName = "insertRegistroVisitaCliente", action = "insertRegistroVisitaCliente")
    public String insertRegistroVisitaCliente(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "registroVisitaDtoRequestJson") String registroVisitaDtoRequestJson){
        
        System.out.println("METODO: insertRegistroVisitaCliente \n");
        System.out.println("REQUEST JSON: \n" + registroVisitaDtoRequestJson);
        
        InsertaActualizaWsBO insertDev = new InsertaActualizaWsBO();
        
        Gson gson = new Gson();
        
        InsertRegistroVisitaClienteResponse response = insertDev.insertRegistroVisitaClienteResponse(empleadoDtoRequestJson, registroVisitaDtoRequestJson);
        
        //Transformamos de objeto a formato JSON
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    /**
     *
     * @param empleadoDtoRequestJson
     * @param devolucionesDtoRequestJson
     * @return
     */
    @WebMethod(operationName = "insertPedidosDevolucionesVentas", action = "insertPedidosDevolucionesVentas")
    public String insertPedidosDevolucionesVentas(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "devolucionesDtoRequestJson") String devolucionesDtoRequestJson){
        
        System.out.println("METODO: insertPedidoDevolucionesCambios \n");
        System.out.println("REQUEST JSON: \n" + devolucionesDtoRequestJson);
        
        InsertaActualizaWsBO insertDev = new InsertaActualizaWsBO();
        
        Gson gson = new Gson();
        
        WSResponse response = insertDev.insertPedidosDevolucionesVentas(empleadoDtoRequestJson, devolucionesDtoRequestJson);
        
        //Transformamos de objeto a formato JSON
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    /**
     * Agrega un nuevo usuario en el catalogo de una empresa, ya sea creando la empresa si no hay cupo para 5 usuarios.
     * las credenciales de autenticación de un empleado desde dispositivo móvil.
     * @param usuarioDtoRequestJson String con formato JSON representando un objeto de tipo usuarioDtoRequestJson    
     * @return String con formato JSON representando un objeto de tipo WSResponseInsert
     */
    @WebMethod(operationName = "insertaNuevoUsuario", action="insertaNuevoUsuario")
    public String insertaNuevoUsuario(
        @WebParam(name = "usuarioDtoRequestJson") String usuarioDtoRequestJson
            ) {
        
        System.out.println("METODO: insertaNuevoUsuario \n");
        System.out.println("REQUEST JSON: \n" + usuarioDtoRequestJson);
                
        InsertaActualizaWsBO insertaWsBO = new InsertaActualizaWsBO();
        
        UsuarioNuevoDtoRequest usuarioNuevoDtoRequest = insertaWsBO.insertaUsuarioNuevo(usuarioDtoRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(usuarioNuevoDtoRequest);
        
        return jsonResponse;
    }
    
    /**
     * Agrega una nueva cotizacion.
     * Credenciales de autenticación de un empleado desde dispositivo móvil.
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo empleadoDtoRequestJson    
     * @param crearCotizacionRequestJson String con formato JSON representando un objeto de tipo crearPedidoRequestJson  
     * @return 
     */
    @WebMethod(operationName = "creaCotizacionByEmpleado", action="creaCotizacionByEmpleado")
    public String creaCotizacionByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "crearCotizacionRequestJson") String crearCotizacionRequestJson
            ) {
        
        System.out.println("METODO: creaCotizacionByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        System.out.println("\t" + crearCotizacionRequestJson);
        
        CotizacionWsBO cotizacionWsBO = new CotizacionWsBO();
        
        CrearCotizacionResponse response = cotizacionWsBO.crearCotizacionByEmpleado(empleadoDtoRequestJson, crearCotizacionRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }    
    
    
    @WebMethod(operationName = "getCotizacionesByEmpleado", action="getCotizacionesByEmpleado")
    public String getCotizacionesByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "consultaCotizacionesRequestJson") String consultaCotizacionesRequestJson) {
        
        System.out.println("METODO: getCotizacionesByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        System.out.println(consultaCotizacionesRequestJson);
        
        CotizacionWsBO cotizacionWsBO = new CotizacionWsBO();
        
        ConsultaCotizacionesResponse response = cotizacionWsBO.getCotizacionesByEmpleado(empleadoDtoRequestJson, consultaCotizacionesRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    /**
     * Método para registrar Bitacora de Creditos Operacion
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @return String con formato JSON representando un objeto de tipo WSResponse
     */
    @WebMethod(operationName = "creaRegistroBitacoraCreditosOperacion", action="creaRegistroBitacoraCreditosOperacion")
    public String creaRegistroBitacoraCreditosOperacion(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "registroBCODataJSON") String registroBCODataJSON) {
        InsertaActualizaWsBO insertaWsBO = new InsertaActualizaWsBO();
        
        System.out.println("METODO: creaRegistroBitacoraCreditosOperacion \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson
                        + "\n " + registroBCODataJSON);
        
        //Efectuamos operación
        RegistrarBitacoraCreditoOperacionResponse response = insertaWsBO.insertRegistroBitacoraCreditoOperacion(empleadoDtoRequestJson,registroBCODataJSON);
                
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    
    /**
     * Método para obtener el total del catalogo de Conceptos de una Empresa
     * haciendo autenticación por empleado desde un dispositivo móvil
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo ConsultaConceptosResponse
     * @return String con formato JSON representando un objeto de tipo ConsultaConceptosResponse
     */
    @WebMethod(operationName = "getTotalCatalogoConceptosByEmpleado", action="getTotalCatalogoConceptosByEmpleado")
    public String getTotalCatalogoConceptosByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson ) {
        
        System.out.println("METODO: getTotalCatalogoConceptosByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        ConsultaConceptosResponse response = consultaWsBO.getTotalCatalogoConceptosByEmpleado(empleadoDtoRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    @WebMethod(operationName = "getClienteCategoriaByEmpleado", action = "getClienteCategoriaByEmpleado")
    public String getClienteCategoriaByEmpleado(@WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson){
        System.out.println("METODO: getClienteCategoriaByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        ConsultaClienteCategoriaResponse response = consultaWsBO.getClienteCategoriaByEmpleado(empleadoDtoRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    @WebMethod(operationName = "getCategoriasProveedores", action = "getCategoriasProveedores")
    public String getCategoriasProveedores(@WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson){
        System.out.println("METODO: getCategoriasProveedores \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        ConsultaCategoriasProveedorResponse response = consultaWsBO.getCategoriasProveedores(empleadoDtoRequestJson);
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    /**
     * Método para consultar el listado de Conceptos/servicios de provvedores de una empresa
     * haciendo autenticación por empleado desde un dispositivo móvil
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @return String con formato JSON representando un objeto de tipo ConsultaConceptosResponse
     */
    @WebMethod(operationName = "getCatalogoConceptosServiciosProveedores", action="getCatalogoConceptosServiciosProveedores")
    public String getCatalogoConceptosServiciosProveedores(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson ) {        
        System.out.println("METODO: getCatalogoConceptosServiciosProveedores \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        ConsultaConceptosServiciosResponse response = consultaWsBO.getCatalogoConceptosServiciosProveedores(empleadoDtoRequestJson);
                
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    /**
     * Método para consultar el listado de Conceptos/servicos de proveedores de una empresa
     * haciendo autenticación por empleado desde un dispositivo móvil
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @param limMin int Limite inferior de consulta
     * @param limMax int Limite inferior de consulta
     * @return String con formato JSON representando un objeto de tipo ConsultaConceptosResponse
     */
    @WebMethod(operationName = "getCatalogoConceptosServiciosProveedoresWifiWithLimits", action="getCatalogoConceptosServiciosProveedoresWifiWithLimits")
    public String getCatalogoConceptosServiciosProveedoresWithLimits(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "limMin") int limMin, 
            @WebParam(name = "limMax") int limMax ) {        
        System.out.println("METODO: getCatalogoConceptosServiciosProveedoresWifiWithLimits \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        ConsultaConceptosServiciosResponse response = consultaWsBO.getCatalogoConceptosServiciosProveedoresWifiWithLimits(empleadoDtoRequestJson,limMin,limMax);
               
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    /**
     * Método para consultar el listado de Conceptos/servicios de proveedores
     * haciendo autenticación por empleado desde un dispositivo móvil
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @return String con formato JSON representando un objeto de tipo ConsultaConceptosResponse
     */
    @WebMethod(operationName = "getCatalogoConceptosServiciosProveedoresByProveedor", action="getCatalogoConceptosServiciosProveedoresByProveedor")
    public String getCatalogoConceptosServiciosProveedoresByProveedor(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson ) {
        System.out.println("METODO: getCatalogoConceptosServiciosProveedoresByProveedor \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        ConsultaProveedorConceptosServiciosResponse response = consultaWsBO.getCatalogoConceptosServiciosByProveedor(empleadoDtoRequestJson);
                
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    /**
     * Método para consultar el listado de Conceptos/servicos de proveedores de una empresa
     * haciendo autenticación por empleado desde un dispositivo móvil
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @param limMin int Limite inferior de consulta
     * @param limMax int Limite inferior de consulta
     * @return String con formato JSON representando un objeto de tipo ConsultaConceptosResponse
     */
    @WebMethod(operationName = "getCatalogoConceptosServiciosProveedoresByProveedorWifiWithLimits", action="getCatalogoConceptosServiciosProveedoresByProveedorWifiWithLimits")
    public String getCatalogoConceptosServiciosProveedoresByProveedorWifiWithLimits(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "limMin") int limMin, 
            @WebParam(name = "limMax") int limMax ) {        
        System.out.println("METODO: getCatalogoConceptosServiciosProveedoresByProveedorWifiWithLimits \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        ConsultaProveedorConceptosServiciosResponse response = consultaWsBO.getCatalogoConceptosServiciosProveedoresByProveedorWifiWithLimits(empleadoDtoRequestJson,limMin,limMax);
               
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    /**
     * Método para consultar el listado de Conceptos de una empresa
     * haciendo autenticación por empleado desde un dispositivo móvil
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @param limMin int Limite inferior de consulta
     * @param limMax int Limite inferior de consulta
     * @return String con formato JSON representando un objeto de tipo ConsultaConceptosResponse
     */
    @WebMethod(operationName = "getCatalogoConceptosByEmpleadoWithLimits", action="getCatalogoConceptosByEmpleadoWithLimits")
    public String getCatalogoConceptosByEmpleadoWithLimits(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "limMin") int limMin, 
            @WebParam(name = "limMax") int limMax ) {        
        System.out.println("METODO: getCatalogoConceptosByEmpleadoWithLimits \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        ConsultaConceptosResponse response = consultaWsBO.getCatalogoConceptosByEmpleadoWithLimits(empleadoDtoRequestJson,limMin,limMax);
               
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    
    /**
     * Método para consultar el listado de Conceptos de una empresa
     * haciendo autenticación por empleado desde un dispositivo móvil
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @param limMin int Limite inferior de consulta
     * @param limMax int Limite inferior de consulta
     * @return String con formato JSON representando un objeto de tipo ConsultaConceptosResponse
     */
    @WebMethod(operationName = "getCatalogoConceptosByEmpleadoWiFiWithLimits", action="getCatalogoConceptosByEmpleadoWiFiWithLimits")
    public String getCatalogoConceptosByEmpleadoWiFiWithLimits(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "limMin") int limMin, 
            @WebParam(name = "limMax") int limMax ) {        
        System.out.println("METODO: getCatalogoConceptosByEmpleadoWiFiWithLimits \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        ConsultaConceptosResponse response = consultaWsBO.getCatalogoConceptosByEmpleadoWiFiWithLimits(empleadoDtoRequestJson,limMin,limMax);
               
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    /**
     * Método para consultar el listado de Conceptos de una empresa
     * haciendo autenticación por empleado desde un dispositivo móvil
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo EmpleadoDtoRequest  
     * @return String con formato JSON representando un objeto de tipo ConsultaConceptosResponse
     */    
    @WebMethod(operationName = "insertaCuentaDinero", action = "insertaCuentaDinero")
    public String insertaCuentaDinero(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "cuentaDineroDtoRequestJson") String cuentaDineroDtoRequestJson){
        System.out.println("METODO: updateAgendaEstatus \n");
        System.out.println("REQUEST JSON: \n" + cuentaDineroDtoRequestJson);
        
        InsertaActualizaWsBO insertaCuentaDinero = new InsertaActualizaWsBO();
        
        Gson gson = new Gson();
        
        WsItemCuentaDineroResponse response = insertaCuentaDinero.insertaCuentaDinero(empleadoDtoRequestJson,cuentaDineroDtoRequestJson);
        
        //Transformamos de objeto a formato JSON
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    
    /**
     * Método para obtener catalogo de Conceptos de una Empresa
     * haciendo autenticación por empleado desde un dispositivo móvil
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo ConsultaConceptosResponse
     * @return String con formato JSON representando un objeto de tipo ConsultaConceptosResponse
     */
    @WebMethod(operationName = "getCatalogoGastos", action="getCatalogoGastos")
    public String getCatalogoGastos(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson ) {
        
        System.out.println("METODO: getCatalogoGastos \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        ConsultaCatalogoGastosResponse response = consultaWsBO.getCatalogoGastos(empleadoDtoRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    
    /**
     * Método para obtener los Gatsos de un Empleado
     * haciendo autenticación por empleado desde un dispositivo móvil
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo ConsultaConceptosResponse
     * @return String con formato JSON representando un objeto de tipo ConsultaConceptosResponse
     */
    @WebMethod(operationName = "getGastosByEmpleado", action="getGastosByEmpleado")
    public String getGastosByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson ) {
        
        System.out.println("METODO: getGastosByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        ConsultaGastosResponse response = consultaWsBO.getGastosByEmpleado(empleadoDtoRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    } 
    
    
    /**
     * Método para obtener el total del catalogo de Pedidos de una Empleado
     * haciendo autenticación por empleado desde un dispositivo móvil
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo ConsultaConceptosResponse
     * @return String con formato JSON representando un objeto de tipo ConsultaConceptosResponse
     */
    @WebMethod(operationName = "getTotalPedidosByEmpleado", action="getTotalPedidosByEmpleado")
    public String getTotaPedidosEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "consultaPedidosRequestJson") String consultaPedidosRequestJson) {
        
        System.out.println("METODO: getTotaPedidosEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        System.out.println(consultaPedidosRequestJson);
        
        PedidoWsBO pedidoWsBO = new PedidoWsBO();
        
        ConsultaPedidosResponse response = pedidoWsBO.getTotalPedidosByEmpleado(empleadoDtoRequestJson,consultaPedidosRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    
    @WebMethod(operationName = "getPedidosByEmpleadoWithLimits", action="getPedidosByEmpleadoWithLimits")
    public String getPedidosByEmpleadoWithLimits(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "consultaPedidosRequestJson") String consultaPedidosRequestJson,
            @WebParam(name = "limMin") int limMin , 
            @WebParam(name = "limMax") int limMax ) {
        
        System.out.println("METODO: getPedidosByEmpleadoWithLimits \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        System.out.println(consultaPedidosRequestJson);
        
        PedidoWsBO pedidoWsBO = new PedidoWsBO();
        
        ConsultaPedidosResponse response = pedidoWsBO.getPedidosByEmpleadoWithLimits(empleadoDtoRequestJson, consultaPedidosRequestJson,limMin,limMax);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    
    @WebMethod(operationName = "insertaActualizaCronometroByEmpleado", action = "insertaActualizaCronometroByEmpleado")
    public String insertaActualizaCronometroByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "cronometroDtoRequestJson") String cronometroDtoRequestJson){
        InsertaActualizaWsBO insertaActualizaWsBO = new InsertaActualizaWsBO();
        Gson gson = new Gson();
        System.out.println("METODO: insertaActualizaCronometroByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        System.out.println("\t" + cronometroDtoRequestJson);
        
        WSResponseInsert wSResponseInsert = insertaActualizaWsBO.insertaActualizaCronometroByEmpleado(empleadoDtoRequestJson, cronometroDtoRequestJson);
                
        //Transformamos de objeto a formato JSON
        String jsonResponse = gson.toJson(wSResponseInsert);
        
        return jsonResponse;
    }
    
    public String getPuntosInteresByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson){
        System.out.println("METODO: getPuntosInteresByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        ConsultaPuntosInteresResponse response = consultaWsBO.getPuntosByEmpleado(empleadoDtoRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    public String insertaActualizaPuntoInteres(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "puntoInteresDtoRequestJson") String puntoInteresDtoRequestJson){
        System.out.println("METODO: getPuntosInteresByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + puntoInteresDtoRequestJson);
        
        InsertaActualizaWsBO actualizaWsBO = new InsertaActualizaWsBO();
        
        WSResponseInsert response = actualizaWsBO.insertaActualizaPuntoInteres(empleadoDtoRequestJson, puntoInteresDtoRequestJson);
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    /**
     * Método para obtener los Almacenes correspondientes a la empresa de un Empleado
     * haciendo autenticación por empleado desde un dispositivo móvil
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo ConsultaConceptosResponse
     * @return String con formato JSON representando un objeto de tipo ConsultaConceptosResponse
     */
    @WebMethod(operationName = "getAlmacenesByEmpleado", action="getAlmacenesByEmpleado")
    public String getAlmacenesByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson ) {
        
        System.out.println("METODO: getAlmacenesByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        ConsultaAlmacenesResponse response = consultaWsBO.getAlmacenesByEmpleado(empleadoDtoRequestJson);
        
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    } 
    
    public String insertaAutoServicioInventarioByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "autoServicioInventarioDtoRequestJson") String autoServicioInventarioDtoRequestJson){
        System.out.println("METODO: insertaAutoServicioInventarioByEmpleado \n");
        System.out.println("REQUEST JSON: \n" + autoServicioInventarioDtoRequestJson);
        
        InsertaActualizaWsBO actualizaWsBO = new InsertaActualizaWsBO();
        
        WSResponse response = actualizaWsBO.insertaAutoServicioInventarioByEmpleado(empleadoDtoRequestJson, autoServicioInventarioDtoRequestJson);
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    public String getFormualriosByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson){
        
        System.out.println("METODO: getFormualriosByEmpleado");
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        ConsultaFormulariosResponse respone = consultaWsBO.getFormulariosByEmpleado(empleadoDtoRequestJson);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(respone);
        return jsonResponse;
    }
    
    public String getScoreByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson){
        System.out.println("METODO: getScoreByEmpleado");
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        ConsultaScoreResponse response = consultaWsBO.getScoreByEmpleado(empleadoDtoRequestJson);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        return jsonResponse;
    }
    
    public String getProductosCreditoByEmpleado(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson){
        System.out.println("METODO: getScoreByEmpleado");
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        ConsultaProductosCreditoResponse response = consultaWsBO.getProductoCreditoByEmpleado(empleadoDtoRequestJson);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        return jsonResponse;
    }
    
    public String actualizaEventoSolicitud(@WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson){
        System.out.println("METODO: actualizaEventoSolicitud \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson);
        
        ModuloCrWsBO moduloCrWsBO = new ModuloCrWsBO();
        
        ConsultaCrFrmEventoSolicitudResponse response = moduloCrWsBO.actualizaEventoSolicitud(empleadoDtoRequestJson);
        
        Gson gson = new Gson();
        
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
    public String insertaCrFormularioEvento(
            @WebParam(name = "empleadoDtoRequestJson") String empleadoDtoRequestJson,
            @WebParam(name = "crearCrFormularioEventoRequestJson") String crearCrFormularioEventoRequestJson){
        System.out.println("METODO: insertaCrFormularioEvento \n");
        System.out.println("REQUEST JSON: \n" + empleadoDtoRequestJson
                            + "\n" + crearCrFormularioEventoRequestJson);
        
        ModuloCrWsBO moduloCrWsBO = new ModuloCrWsBO();
        
        CrearCrFormularioEventoResponse response = moduloCrWsBO.insertaCrFormularioEvento(empleadoDtoRequestJson, crearCrFormularioEventoRequestJson);
        //Transformamos de objeto a formato JSON
        Gson gson = new Gson();
        
        String jsonResponse = gson.toJson(response);
        
        return jsonResponse;
    }
    
}