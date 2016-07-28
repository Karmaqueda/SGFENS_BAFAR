/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ws;

import com.tsp.sgfens.ws.pos.bo.*;
import com.tsp.sgfens.ws.pos.response.*;
import com.tsp.sgfens.ws.pos.request.*;
import com.tsp.sgfens.ws.request.CrearClienteRequest;
import com.tsp.sgfens.ws.response.WSResponseInsert;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author ISCesar
 * Servicio Web para interconexion con sistemas de escritorio Punto de Venta
 */
@WebService(serviceName = "WSConexionPuntoDeVenta")
public class WSConexionPuntoDeVenta {
    
    /**
     * Método para verificar credenciales de acceso
     * para usuario desde Punto de Venta
     * @param empleadoDtoRequestJson Objeto de tipo EmpleadoDtoRequest
     * @return Objeto tipo LoginEmpleadoMovilResponse
     */
    @WebMethod(operationName = "posLoginByEmpleado", action="posLoginByEmpleado")
    public LoginEmpleadoResponse posLoginByEmpleado(
            @WebParam(name = "posEmpleadoDtoRequest") PosEmpleadoDtoRequest posEmpleadoDtoRequest ) {
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        System.out.println("METODO: posLoginByEmpleado \n");
        
        //Efectuamos operación
        LoginEmpleadoResponse response = consultaWsBO.loginByEmpleado(posEmpleadoDtoRequest);
        
        return response;
    }
    
    /**
     * Método para obtener el catalogo de Clientes de una Empresa
     * haciendo autenticación por empleado desde un punto de venta
     * @param empleadoDtoRequestJson objeto de tipo ConsultaConceptosResponse
     * @return objeto de tipo ConsultaConceptosResponse
     */
    @WebMethod(operationName = "posGetCatalogoClientesByEmpleado", action="posGetCatalogoClientesByEmpleado")
    public ConsultaClientesResponse posGetCatalogoClientesByEmpleado(
            @WebParam(name = "posEmpleadoDtoRequest") PosEmpleadoDtoRequest posEmpleadoDtoRequest ) {
        
        System.out.println("METODO: posGetCatalogoClientesByEmpleado \n");
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        ConsultaClientesResponse response = consultaWsBO.getCatalogoClientesByEmpleado(posEmpleadoDtoRequest);
        
        return response;
    }
    
    /**
     * Agrega un nuevo cliente en el catalogo de una empresa utilizando
     * las credenciales de autenticación de un empleado desde un punto de venta
     * @param empleadoDtoRequestJson String con formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @param crearClienteRequestJson String con formato JSON representando un objeto de tipo CrearClienteRequest
     * @return String con formato JSON representando un objeto de tipo WSResponseInsert
     */
    @WebMethod(operationName = "posInsertaActualizaClienteByEmpleado", action="posInsertaActualizaClienteByEmpleado")
    public WSResponseInsert posInsertaActualizaClienteByEmpleado(
        @WebParam(name = "posEmpleadoDtoRequest") PosEmpleadoDtoRequest posEmpleadoDtoRequest,
        @WebParam(name = "crearClienteRequest") CrearClienteRequest crearClienteRequest
            ) {
        
        System.out.println("METODO: posInsertaActualizaClienteByEmpleado \n");
        
        InsertaActualizaWsBO insertaWsBO = new InsertaActualizaWsBO();
        
        WSResponseInsert response = insertaWsBO.insertaActualizaClienteByEmpleado(posEmpleadoDtoRequest, crearClienteRequest);
        
        return response;
    }
    
    /**
     * Método para consultar el listado de Conceptos de una empresa
     * haciendo autenticación por empleado desde un punto de venta
     * @param posEmpleadoDtoRequest String con formato JSON representando un objeto de tipo EmpleadoDtoRequest
     * @return objeto de tipo ConsultaConceptosResponse
     */
    @WebMethod(operationName = "posGetCatalogoConceptosByEmpleado", action="posGetCatalogoConceptosByEmpleado")
    public ConsultaConceptosResponse posGetCatalogoConceptosByEmpleado(
            @WebParam(name = "posEmpleadoDtoRequest") PosEmpleadoDtoRequest posEmpleadoDtoRequest ) {        
        System.out.println("\nMETODO: posGetCatalogoConceptosByEmpleado");
        
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        
        ConsultaConceptosResponse response = consultaWsBO.getCatalogoConceptosByEmpleado(posEmpleadoDtoRequest);
        
        return response;
    }
    
    @WebMethod(operationName = "posGetMediaByConcepto", action="posGetMediaByConcepto")
    public ConsultaConceptosResponse posGetMediaByConcepto(
            @WebParam(name = "posEmpleadoDtoRequest") PosEmpleadoDtoRequest posEmpleadoDtoRequest ,
            @WebParam(name = "conceptoDtoRequest") ConceptoDtoRequest conceptoDtoRequest ){
        ConsultaWsBO consultaWsBO = new ConsultaWsBO();
        ConsultaConceptosResponse response = consultaWsBO.getMediaByConcepto(posEmpleadoDtoRequest, conceptoDtoRequest);
        
        return response;
    }
}
