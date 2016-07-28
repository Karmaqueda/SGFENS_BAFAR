/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ws.pos.bo;

import com.tsp.sct.bo.BitacoraCreditosOperacionBO;
import com.tsp.sct.bo.EmpleadoBO;
import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.dto.SgfensClienteVendedor;
import com.tsp.sct.dao.jdbc.ClienteDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.dao.jdbc.SgfensClienteVendedorDaoImpl;
import com.tsp.sct.util.GenericValidator;
import com.tsp.sct.util.StringManage;
import com.tsp.sgfens.ws.pos.request.PosEmpleadoDtoRequest;
import com.tsp.sgfens.ws.request.CrearClienteRequest;
import com.tsp.sgfens.ws.response.WSResponseInsert;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author ISCesar poseidon24@hotmail.com
 * @since 15/06/2015 15/06/2015 10:30:58 AM
 */
public class InsertaActualizaWsBO {

    private final GenericValidator gc = new GenericValidator();
    private Connection conn = null;

    public Connection getConn() {
        if (this.conn==null){
            try {
                this.conn= ResourceManager.getConnection();
            } catch (SQLException ex) {}
        }else{
            try {
                if (this.conn.isClosed()){
                    this.conn = ResourceManager.getConnection();
                }
            } catch (SQLException ex) {}
        }
        return this.conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
     /**
     * Crea un nuevo registro de Cliente, a partir de los datos recabados
     * desde el formulario en aplicación punto de venta.
     * @param posEmpleadoDtoRequest EmpleadoDtoRequest Datos del empleado.
     * @param datosCliente CrearClienteRequest Datos del Cliente a crear.
     * @return Objeto de respuesta generico para inserciones vía Web Service
     */
    public WSResponseInsert insertaActualizaClienteByEmpleado(PosEmpleadoDtoRequest posEmpleadoDtoRequest, CrearClienteRequest datosCliente){
        WSResponseInsert response = new WSResponseInsert();
        
        int idCreado;
        int idEmpresa;
        
        Cliente clienteDto = new Cliente();
        ClienteDaoImpl clienteDaoImpl = new ClienteDaoImpl(getConn());
        
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
            if (empleadoBO.login(posEmpleadoDtoRequest.getEmpleadoUsuario(), posEmpleadoDtoRequest.getEmpleadoPassword())){
                idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
            }else{
                response.setError(true);
                response.setNumError(901);
                response.setMsgError("El usuario y/o contraseña del Empleado son inválidos.");
                return response;
            }
            
            //Antes de procesar, verificamos si no fue registrado previamente,
            // de acuerdo al dato: Folio Cliente Movil
            try{
                Cliente[] clientesExistentes = new Cliente[0];
                if (!StringManage.getValidString(datosCliente.getFolioClienteMovil()).equals("")){
                    clientesExistentes = clienteDaoImpl.findWhereFolioClienteMovilEquals(datosCliente.getFolioClienteMovil());
                }
                    
                if (clientesExistentes.length>0){
                    if (clientesExistentes[0].getIdCliente()>0){
                        response.setIdObjetoCreado(clientesExistentes[0].getIdCliente());


                        //Actualizamos registro de Cliente con datos modificados
                        clientesExistentes[0].setNombreCliente(datosCliente.getNombreCliente());
                        clientesExistentes[0].setApellidoMaternoCliente(datosCliente.getApellidoMaternoCliente());
                        clientesExistentes[0].setApellidoPaternoCliente(datosCliente.getApellidoPaternoCliente());
                        clientesExistentes[0].setCalle(datosCliente.getCalle());
                        clientesExistentes[0].setCelular(datosCliente.getCelular());
                        clientesExistentes[0].setCodigoPostal(datosCliente.getCodigoPostal());
                        clientesExistentes[0].setColonia(datosCliente.getColonia());
                        clientesExistentes[0].setContacto(datosCliente.getContacto());
                        clientesExistentes[0].setCorreo(datosCliente.getCorreo());
                        clientesExistentes[0].setEstado(datosCliente.getEstado());
                        clientesExistentes[0].setExtension(datosCliente.getExtension());
                        clientesExistentes[0].setLada(datosCliente.getLada());
                        clientesExistentes[0].setMunicipio(datosCliente.getMunicipio());            
                        clientesExistentes[0].setNumero(datosCliente.getNumero());
                        clientesExistentes[0].setNumeroInterior(datosCliente.getNumeroInterior());
                        clientesExistentes[0].setPais(datosCliente.getPais());
                        clientesExistentes[0].setRazonSocial(datosCliente.getRazonSocial());
                        clientesExistentes[0].setRfcCliente(datosCliente.getRfcCliente());
                        clientesExistentes[0].setTelefono(datosCliente.getTelefono());
                        clientesExistentes[0].setLatitud(datosCliente.getLatitud());
                        clientesExistentes[0].setLongitud(datosCliente.getLongitud());
                        clientesExistentes[0].setDiasVisita(datosCliente.getDiasVisita());
                        clientesExistentes[0].setPerioricidad(datosCliente.getPerioricidad());


                        //Si tiene Estatus no disponible para facturar/mostrar 
                        if (clientesExistentes[0].getIdEstatus()==3){ //Estatus 3 : Cliente Expres
                            //Verificamos si se le han asignado los datos básicos para facturar
                            if (gc.isRFC(clientesExistentes[0].getRfcCliente())
                                    && gc.isValidString(clientesExistentes[0].getPais(), 1, 200)){
                                clientesExistentes[0].setIdEstatus(1);
                            }
                        }

                        clienteDaoImpl.update(clientesExistentes[0].createPk(), clientesExistentes[0]);

                        response.setMsgError("actualizado:ok");

                        return response;
                    }
                }
            }catch(Exception ex){
                //ex.printStackTrace();
                throw new Exception("Error al intentar actualizar Cliente existente.");
            }
            
            Cliente lastCliente = clienteDaoImpl.findLast();
            idCreado = lastCliente.getIdCliente() + 1;
            
            clienteDto.setIdCliente(idCreado);
            clienteDto.setIdEmpresa(idEmpresa);
            
            clienteDto.setNombreCliente(datosCliente.getNombreCliente());
            clienteDto.setApellidoMaternoCliente(datosCliente.getApellidoMaternoCliente());
            clienteDto.setApellidoPaternoCliente(datosCliente.getApellidoPaternoCliente());
            clienteDto.setCalle(datosCliente.getCalle());
            clienteDto.setCelular(datosCliente.getCelular());
            clienteDto.setCodigoPostal(datosCliente.getCodigoPostal());
            clienteDto.setColonia(datosCliente.getColonia());
            clienteDto.setContacto(datosCliente.getContacto());
            clienteDto.setCorreo(datosCliente.getCorreo());
            clienteDto.setEstado(datosCliente.getEstado());
            clienteDto.setExtension(datosCliente.getExtension());
            clienteDto.setLada(datosCliente.getLada());
            clienteDto.setMunicipio(datosCliente.getMunicipio());            
            clienteDto.setNumero(datosCliente.getNumero());
            clienteDto.setNumeroInterior(datosCliente.getNumeroInterior());
            clienteDto.setPais(datosCliente.getPais());
            clienteDto.setRazonSocial(datosCliente.getRazonSocial());
            clienteDto.setRfcCliente(datosCliente.getRfcCliente());
            clienteDto.setTelefono(datosCliente.getTelefono());
            clienteDto.setLatitud(datosCliente.getLatitud());
            clienteDto.setLongitud(datosCliente.getLongitud());
            clienteDto.setFolioClienteMovil(datosCliente.getFolioClienteMovil());
            clienteDto.setDiasVisita(datosCliente.getDiasVisita());
            clienteDto.setPerioricidad(datosCliente.getPerioricidad());
            
            if (datosCliente.getRfcCliente().equals("AAA010101")){
                clienteDto.setIdEstatus(3); //Estatus no disponible para facturar/mostrar
            }else{
                clienteDto.setIdEstatus(1); //Estatus activo
            }
            
            clienteDaoImpl.insert(clienteDto);
            
            //Relacion Cliente-Vendedor 1-1
            try{
                SgfensClienteVendedor clienteVendedor = new SgfensClienteVendedor();
                clienteVendedor.setIdCliente(idCreado);
                clienteVendedor.setIdUsuarioVendedor(empleadoBO.getUsuarioBO().getUser().getIdUsuarios());
                new SgfensClienteVendedorDaoImpl(getConn()).insert(clienteVendedor);
            }catch(Exception ex){
                throw new Exception("Error al generar relación de Cliente con Vendedor." + ex.getLocalizedMessage());
            }
            
            idCreado = clienteDto.getIdCliente();
            
            response.setIdObjetoCreado(idCreado);
            
            //Consumo de Creditos Operacion
            try{
                BitacoraCreditosOperacionBO bcoBO = new BitacoraCreditosOperacionBO(getConn());
                bcoBO.registraDescuento(empleadoBO.getUsuarioBO().getUser(), 
                        BitacoraCreditosOperacionBO.CONSUMO_ACCION_WS_REGISTRO_CLIENTE, 
                        null, idCreado, 0, 0, 
                        "Registro Cliente WS Punto de Venta", null, true);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
        }catch(Exception e){
            e.printStackTrace();
            response.setError(true);
            response.setNumError(902);
            response.setMsgError("Error inesperado al crear un nuevo Cliente. " + e.getLocalizedMessage());
        }finally{   
            try{ if (this.conn!=null) getConn().close(); }catch(Exception ex){}
        }
        
        return response;
    }
}
