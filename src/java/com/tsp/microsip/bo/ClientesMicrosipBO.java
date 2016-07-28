/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.microsip.bo;

import com.tsp.microsip.bean.ClientesMicrosipBean;
import com.tsp.microsip.bean.ControlBean;
import com.tsp.sct.bo.ClienteBO;
import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.dto.ClientePk;
import com.tsp.sct.dao.dto.QuartzCliente;
import com.tsp.sct.dao.jdbc.ClienteDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzClienteDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 *
 * @author leonardo
 */
public class ClientesMicrosipBO {
    
    public int obtenerCantidadRegistros(int tipo, int idEmpresa){
        try{
            //validamos si la busqueda es para clientes nuevo o actualizados        
            String filtroBusqueda = "";
            if(tipo == 1){//clientes nuevos
                filtroBusqueda = " SINCRONIZACION_MICROSIP = 0 ";
            }else if(tipo == 2){//clientes actualizados
                filtroBusqueda = " SINCRONIZACION_MICROSIP = 2 ";
            }

            if(idEmpresa > 0){
                filtroBusqueda += " AND ID_EMPRESA = " +idEmpresa;
            }
            
            filtroBusqueda += " AND ID_ESTATUS = 1 AND ID_CLIENTE > 0 ";
            
            Connection conn = ResourceManager.getConnection();
            ClienteBO cliBO = new ClienteBO(conn);
            int cantidad = cliBO.getCantidadByCliente(filtroBusqueda);
            conn.close();
            System.out.println(" ++++++++ clientes a retornar: "+cantidad);
            return cantidad;    
        }catch(Exception e){
            e.printStackTrace();
            return 0;            
        }
    }
    
    public List<ClientesMicrosipBean> obtenerClientes(int tipo, int idEmpresa){
        try{
            //validamos si la busqueda es para clientes nuevo o actualizados        
            String filtroBusqueda = " AND ";
            if(tipo == 1){//clientes nuevos
                filtroBusqueda += " SINCRONIZACION_MICROSIP = 0 ";
            }else if(tipo == 2){//clientes actualizados
                filtroBusqueda += " SINCRONIZACION_MICROSIP = 2 ";
            }
            
            filtroBusqueda += " AND ID_ESTATUS = 1 AND ID_CLIENTE > 0 ";
            
            Connection conn = ResourceManager.getConnection();
            ClienteBO cliBO = new ClienteBO(conn);
            Cliente[] cliente = cliBO.findClientes(0, idEmpresa, 0, 0, filtroBusqueda);

            List<ClientesMicrosipBean> clientesCompartidos = new ArrayList<ClientesMicrosipBean>();

            for(Cliente cli : cliente ){
                //--/-/-/
                com.tsp.microsip.bean.Cliente clienMicro = new com.tsp.microsip.bean.Cliente();
                clienMicro.setIdCliente(cli.getIdCliente());
                clienMicro.setIdEmpresa(cli.getIdEmpresa());
                clienMicro.setRfcCliente(cli.getRfcCliente());
                clienMicro.setNombreCliente(cli.getNombreCliente());
                clienMicro.setApellidoPaternoCliente(cli.getApellidoPaternoCliente());
                clienMicro.setApellidoMaternoCliente(cli.getApellidoMaternoCliente());
                clienMicro.setRazonSocial(cli.getRazonSocial());
                clienMicro.setCalle(cli.getCalle());
                clienMicro.setNumero(cli.getNumero());
                clienMicro.setNumeroInterior(cli.getNumeroInterior());
                clienMicro.setColonia(cli.getColonia());
                clienMicro.setCodigoPostal(cli.getCodigoPostal());
                clienMicro.setPais(cli.getPais());
                clienMicro.setEstado(cli.getEstado());
                clienMicro.setMunicipio(cli.getMunicipio());
                clienMicro.setLada(cli.getLada());
                clienMicro.setTelefono(cli.getTelefono());
                clienMicro.setExtension(cli.getExtension());
                clienMicro.setCelular(cli.getCelular());
                clienMicro.setCorreo(cli.getCorreo());
                clienMicro.setContacto(cli.getContacto());
                clienMicro.setIdEstatus(cli.getIdEstatus());
                clienMicro.setLatitud(cli.getLatitud());
                clienMicro.setLongitud(cli.getLongitud());
                clienMicro.setGenerico(cli.getGenerico());
                clienMicro.setFolioClienteMovil(cli.getFolioClienteMovil());
                clienMicro.setDiasVisita(cli.getDiasVisita());
                clienMicro.setSaldoCliente(cli.getSaldoCliente());
                clienMicro.setPerioricidad(cli.getPerioricidad());
                clienMicro.setFechaUltimaVisita(cli.getFechaUltimaVisita());
                clienMicro.setSincronizacionMicrosip(cli.getSincronizacionMicrosip());
                clienMicro.setIdVendedorConsigna(cli.getIdVendedorConsigna());
                clienMicro.setAccesoConsolaXmlPdf(cli.getAccesoConsolaXmlPdf());
                clienMicro.setPermisoVentaCredito(cli.getPermisoVentaCredito());
                clienMicro.setNombreComercial(cli.getNombreComercial());
                
                //--/-/-/                
                ClientesMicrosipBean cliMiBO = new ClientesMicrosipBean();
                cliMiBO.setCliente(clienMicro);
                cliMiBO.setClave(cli.getClave());
                if(tipo == 2){//si es una actualización, envamos el id o clave del sistema de microsip (identificador)
                    QuartzCliente qc = null;
                    try{qc = new QuartzClienteDaoImpl(conn).findByDynamicWhere(" ID_CLIENTE_EVC = " + clienMicro.getIdCliente() + " AND ID_EMPRESA = " + idEmpresa+ " ORDER BY ID_QUARTZ DESC ", null)[0]; //buscamos el registro correspondiente
                        if(qc != null){
                            if(qc.getIdClienteSistemTercero() > 0){
                                cliMiBO.setIdClienteMicrosip(qc.getIdClienteSistemTercero());
                            }else if(qc.getClave() != null && !qc.getClave().trim().equals("")){
                                //cliMiBO.setClave(qc.getClave());
                            }
                        }
                    }catch(Exception e){/*e.printStackTrace();*/}                
                }            
                clientesCompartidos.add(cliMiBO);
            }        
            conn.close();
            return clientesCompartidos; 
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public int setClientesIdentificadores(List<QuartzCliente> quartzClientes, int idEmpresa, String acceso){
        /*try{
            System.out.println("Tamaño de la lista de Clientes Identificadores: "+quartzClientes.size());
            for(QuartzCliente qqq : quartzClientes){
                try{
                    System.out.println("- Clave: "+qqq.getClave());
                }catch(Exception e2){}
                System.out.println("- IdClienteEVC: "+qqq.getIdClienteEvc());
                System.out.println("- getIdClienteSistemTercero: "+qqq.getIdClienteSistemTercero());
                System.out.println("- getIdSistemaTercero: "+qqq.getIdSistemaTercero());                
            }        
        }catch(Exception e){e.printStackTrace();}*/
        
        int idSistematercero  = 0; //Se obtiene del token acceso 1er token
           
        try{                

            StringTokenizer st = new StringTokenizer(acceso, "|");
            if(st.hasMoreElements()){
                idSistematercero = Integer.parseInt(st.nextElement().toString().trim());                
            }
        }catch(Exception e){
            System.out.println("Error al obtener IdSitemaTercero desde Token");

        }
        
        
        
        try{
            Connection conn = ResourceManager.getConnection();
            ClienteDaoImpl clienteDaoImpl = new ClienteDaoImpl(conn);
            List<QuartzCliente> clientesIdSincronizacion = new ArrayList<QuartzCliente>();
            QuartzClienteDaoImpl quartzClienteDaoImpl = new QuartzClienteDaoImpl(conn);
            for(QuartzCliente quaC : quartzClientes){
                //Vemos si ya existe el registro:
                QuartzCliente quartzCliente = null;
                if(quaC.getIdClienteSistemTercero() > 0){ //verificamos si se buscara por id o por clave
                    System.out.println("** Es mayor a cero el dato  getIdClienteSistemTercero");
                    try{quartzCliente = quartzClienteDaoImpl.findByDynamicWhere( " ID_CLIENTE_EVC = " + quaC.getIdClienteEvc() + " AND ID_CLIENTE_SISTEM_TERCERO = " + quaC.getIdClienteSistemTercero() + " AND ID_EMPRESA = " + idEmpresa , null)[0];}catch(Exception e){/*e.printStackTrace();*/}
                }else if(quaC.getClave() != null && !quaC.getClave().equals("")){
                    System.out.println("** entro a este else if -----");
                    try{quartzCliente = quartzClienteDaoImpl.findByDynamicWhere( " ID_CLIENTE_EVC = " + quaC.getIdClienteEvc() + " AND CLAVE = " + quaC.getClave()  + " AND ID_EMPRESA = " + idEmpresa , null)[0];}catch(Exception e){/*e.printStackTrace();*/}
                }

                if(quartzCliente == null){//si no existe lo insertamos
                    System.out.println("--- se crea el registro para insertarlo");
                    quartzCliente = new QuartzCliente();
                    quartzCliente.setIdClienteEvc(quaC.getIdClienteEvc());
                    if(quaC.getIdClienteSistemTercero() > 0){
                        quartzCliente.setIdClienteSistemTercero(quaC.getIdClienteSistemTercero());
                    }else{
                        quartzCliente.setIdClienteSistemTercero(0);
                    }
                    if(quaC.getClave() != null && !quaC.getClave().equals("")){
                        quartzCliente.setClave(quaC.getClave());
                    }else{
                        quartzCliente.setClave("");
                    }
                    quartzCliente.setIdEmpresa(idEmpresa);
                    clientesIdSincronizacion.add(quartzCliente);
                }else{//si existe, borramos e insertamos el registro para que sea el ultimo registro insertado y así al recuperar los ID sea el mas actual.
                    try{quartzClienteDaoImpl.delete(quartzCliente.createPk());
                    QuartzCliente quartzClienteClon = new QuartzCliente();
                    quartzClienteClon.setIdClienteEvc(quartzCliente.getIdClienteEvc());
                    quartzClienteClon.setIdClienteSistemTercero(quartzCliente.getIdClienteSistemTercero());
                    quartzClienteClon.setClave(quartzCliente.getClave());
                    quartzClienteClon.setIdSistemaTercero(idSistematercero);
                    quartzClienteClon.setIdEmpresa(quartzCliente.getIdEmpresa());
                    quartzCliente.setIdQuartz(idEmpresa);
                    quartzClienteDaoImpl.insert(quartzClienteClon);
                    }catch(Exception e){System.out.println("------------ Error al actualizar Identificadores de quartz de cliente: "+e.getMessage());}
                    
                }
                
                //actualizamos la bandera de clientes a sincronizado:
                try{
                    Cliente cliente = new ClienteBO(conn).findClientebyId(quaC.getIdClienteEvc());
                    cliente.setSincronizacionMicrosip(1);
                    if (StringManage.getValidString(quaC.getClave()).length()>0)
                        cliente.setClave(StringManage.getValidString(quaC.getClave()));
                    clienteDaoImpl.update(cliente.createPk(), cliente);
                    System.out.println("+++ actualizando bandera de sincronizacion . . . ");
                }catch(Exception e){e.printStackTrace();}
                
            }
            int insetarActualizacion = 0;
            try{
                QuartzClienteDaoImpl qcDaoImp = new QuartzClienteDaoImpl(conn);
                System.out.println(" //// INSERTANDO REGISTROS . . . ");
                for(QuartzCliente insetCli : clientesIdSincronizacion){//insertamos los registros:
                    qcDaoImp.insert(insetCli);
                    System.out.println("//// ... REGISTRO INSERTADO");
                }
                insetarActualizacion = 1;
            }catch(Exception e){e.printStackTrace();}
            
            conn.close();
            return insetarActualizacion;    
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }
    
    public ControlBean setClienteMicrosip(List<ClientesMicrosipBean> clientesMicrosipBean, int idEmpresa , String acceso){
        ControlBean controlBean = new ControlBean();
        String mensajeCliente = "";
        Connection conn = null;
        try{            
            conn = ResourceManager.getConnection();
            ClienteDaoImpl clientesDaoImpl = new ClienteDaoImpl(conn);
            QuartzClienteDaoImpl clienteDaoImpl = new QuartzClienteDaoImpl(conn);
            QuartzCliente clienteExistente = null;
            
            int contadorRegistrosNuevos = 0; //variable auxiliar para el numero de registros insertados
            int contadorRegistrosActualizados = 0; //variable auxiliar para el numero de registros insertados
            
            try{System.out.println("------------------");
                System.out.println("------------------");
                System.out.println("------------------Cantidad de clientes, en la lista: "+clientesMicrosipBean.size());
                System.out.println("------------------");
                System.out.println("------------------");
            }catch(Exception e){System.out.println("------------------ error al colocar la cantidad en la lista: "+e.getMessage());}
            
            int idSistematercero  = 0; //Se obtiene del token acceso 1er token
           
            try{                
                
                StringTokenizer st = new StringTokenizer(acceso, "|");
                if(st.hasMoreElements()){
                    idSistematercero = Integer.parseInt(st.nextElement().toString().trim());                
                }
            }catch(Exception e){
                System.out.println("Error al obtener IdSitemaTercero desde Token");
                
            }
            
            for(ClientesMicrosipBean clienteMicrosip : clientesMicrosipBean){
                
                try{
                   System.out.println(".................."); 
                   System.out.println(".................."); 
                   try{System.out.println("..................CLAVE: "+clienteMicrosip.getClave());}catch(Exception e1){System.out.println("-clave error: "+e1.getMessage());}
                   try{System.out.println("..................ID MICROSIP: "+clienteMicrosip.getIdClienteMicrosip());}catch(Exception e1){System.out.println("-id microsip error: "+e1.getMessage());}
                   try{System.out.println("..................NOMBRE CLIENTE: "+clienteMicrosip.getCliente().getNombreCliente());}catch(Exception e1){System.out.println("-nombre error: "+e1.getMessage());} 
                   try{System.out.println("..................RAZON SOCIAL: "+clienteMicrosip.getCliente().getRazonSocial()); }catch(Exception e1){System.out.println("-error razon social: "+e1.getMessage());}
                   System.out.println(".................."); 
                   System.out.println(".................."); 
                }catch(Exception e){System.out.println("-----ERROR AL LISTA DATO: "+e.getMessage());}
                
                //validamos si el cliente existe o no en el evc
                clienteExistente = null;
                try{
                    String queryOpcional = "";
                    if (clienteMicrosip.getIdClienteMicrosip()>0){
                        queryOpcional = " ID_CLIENTE_SISTEM_TERCERO = " + clienteMicrosip.getIdClienteMicrosip();
                    }else{
                        if (StringManage.getValidString(clienteMicrosip.getClave()).length()>0)
                            queryOpcional = " CLAVE='" + StringManage.getValidString(clienteMicrosip.getClave()) + "'";
                    }
                    clienteExistente = clienteDaoImpl.findByDynamicWhere(queryOpcional +" AND ID_EMPRESA = " + idEmpresa, null)[0];
                }catch(Exception e1){clienteExistente = null;}
                if(clienteExistente == null){//cliente nuevo
                    System.out.println("+++++++ CLIENTE NUEVO");
                    try{
                        com.tsp.microsip.bean.Cliente clienteMicroSip = clienteMicrosip.getCliente();
                        Cliente ultimoRegistroClientes = clientesDaoImpl.findLast();
                        int idClienteNuevo = ultimoRegistroClientes.getIdCliente() + 1;            
                        clienteMicroSip.setIdCliente(idClienteNuevo);
                        if(clienteMicrosip.getCliente().getRfcCliente().length() == 13
                                && StringManage.getValidString(clienteMicroSip.getNombreCliente()).length()>0){ //si es persona fisica llenamos el datos de razon social
                            clienteMicroSip.setRazonSocial( clienteMicroSip.getNombreCliente() + " " +clienteMicroSip.getApellidoPaternoCliente() + " " +clienteMicroSip.getApellidoMaternoCliente() );
                        }
                        clienteMicroSip.setIdEstatus(1);
                        clienteMicroSip.setSincronizacionMicrosip(1);
                        
                        //--/-/-/
                        Cliente clienteEvc = new Cliente();
                        if(clienteMicroSip.getIdCliente() > 0)
                            clienteEvc.setIdCliente(clienteMicroSip.getIdCliente());                        
                        //if(clienteMicroSip.getIdEmpresa() > 0)
                            clienteEvc.setIdEmpresa(idEmpresa);
                        if(clienteMicroSip.getRfcCliente() != null )
                            clienteEvc.setRfcCliente(clienteMicroSip.getRfcCliente());
                        if(clienteMicroSip.getNombreCliente() != null)
                            clienteEvc.setNombreCliente(clienteMicroSip.getNombreCliente());
                        else
                            clienteEvc.setNombreCliente("");
                        if(clienteMicroSip.getApellidoPaternoCliente() != null)
                            clienteEvc.setApellidoPaternoCliente(clienteMicroSip.getApellidoPaternoCliente());
                        else
                            clienteEvc.setApellidoPaternoCliente("");
                        if(clienteMicroSip.getApellidoMaternoCliente() != null)
                            clienteEvc.setApellidoMaternoCliente(clienteMicroSip.getApellidoMaternoCliente());
                        else
                            clienteEvc.setApellidoMaternoCliente("");
                        if(clienteMicroSip.getRazonSocial() != null)
                            clienteEvc.setRazonSocial(clienteMicroSip.getRazonSocial());
                        if(clienteMicroSip.getCalle() != null)
                            clienteEvc.setCalle(clienteMicroSip.getCalle());
                        if(clienteMicroSip.getNumero() != null)
                            clienteEvc.setNumero(clienteMicroSip.getNumero());
                        if(clienteMicroSip.getNumeroInterior() != null)
                            clienteEvc.setNumeroInterior(clienteMicroSip.getNumeroInterior());
                        if(clienteMicroSip.getColonia() != null)
                            clienteEvc.setColonia(clienteMicroSip.getColonia());
                        if(clienteMicroSip.getCodigoPostal() != null)
                            clienteEvc.setCodigoPostal(clienteMicroSip.getCodigoPostal());
                        if(clienteMicroSip.getPais() != null)
                            clienteEvc.setPais(clienteMicroSip.getPais());
                        if(clienteMicroSip.getEstado() != null)
                            clienteEvc.setEstado(clienteMicroSip.getEstado());
                        if(clienteMicroSip.getMunicipio() != null)
                            clienteEvc.setMunicipio(clienteMicroSip.getMunicipio());
                        if(clienteMicroSip.getLada() != null)
                            clienteEvc.setLada(clienteMicroSip.getLada());
                        if(clienteMicroSip.getTelefono() != null)
                            clienteEvc.setTelefono(clienteMicroSip.getTelefono());
                        if(clienteMicroSip.getExtension() != null)
                            clienteEvc.setExtension(clienteMicroSip.getExtension());
                        if(clienteMicroSip.getCelular() != null)
                            clienteEvc.setCelular(clienteMicroSip.getCelular());
                        if(clienteMicroSip.getCorreo() != null)
                            clienteEvc.setCorreo(clienteMicroSip.getCorreo());
                        if(clienteMicroSip.getContacto() != null)
                            clienteEvc.setContacto(clienteMicroSip.getContacto());
                        if(clienteMicroSip.getIdEstatus() > 0)
                            clienteEvc.setIdEstatus(clienteMicroSip.getIdEstatus());
                        if(clienteMicroSip.getLatitud() != null)
                            clienteEvc.setLatitud(clienteMicroSip.getLatitud());
                        if(clienteMicroSip.getLongitud() != null)
                            clienteEvc.setLongitud(clienteMicroSip.getLongitud());
                        if(clienteMicroSip.getGenerico() > 0)
                            clienteEvc.setGenerico(clienteMicroSip.getGenerico());
                        if(clienteMicroSip.getFolioClienteMovil() != null)
                            clienteEvc.setFolioClienteMovil(clienteMicroSip.getFolioClienteMovil());
                        if(clienteMicroSip.getDiasVisita() != null)
                            clienteEvc.setDiasVisita(clienteMicroSip.getDiasVisita());
                        if(clienteMicroSip.getSaldoCliente() > 0)
                            clienteEvc.setSaldoCliente(clienteMicroSip.getSaldoCliente());
                        if(clienteMicroSip.getPerioricidad() > 0)
                            clienteEvc.setPerioricidad(clienteMicroSip.getPerioricidad());
                        if(clienteMicroSip.getFechaUltimaVisita() != null)
                            clienteEvc.setFechaUltimaVisita(clienteMicroSip.getFechaUltimaVisita());
                        if(clienteMicroSip.getSincronizacionMicrosip() > 0)
                            clienteEvc.setSincronizacionMicrosip(clienteMicroSip.getSincronizacionMicrosip());
                        if(clienteMicroSip.getIdVendedorConsigna() > 0)
                            clienteEvc.setIdVendedorConsigna(clienteMicroSip.getIdVendedorConsigna());
                        if(clienteMicroSip.getAccesoConsolaXmlPdf() > 0)
                            clienteEvc.setAccesoConsolaXmlPdf(clienteMicroSip.getAccesoConsolaXmlPdf());
                        if(clienteMicroSip.getPermisoVentaCredito() > 0)
                            clienteEvc.setPermisoVentaCredito(clienteMicroSip.getPermisoVentaCredito());
                        if(clienteMicroSip.getNombreComercial() != null)
                            clienteEvc.setNombreComercial(clienteMicroSip.getNombreComercial());
                        
                        clienteEvc.setClave((clienteMicrosip.getClave()!=null?clienteMicrosip.getClave():""));
                        //--/-/-/ 
                        
                        ClientePk clientePk = clientesDaoImpl.insert(clienteEvc);

                        ////insertamos la relacion con los Id's
                        QuartzCliente qc = new QuartzCliente();
                        qc.setClave( (clienteMicrosip.getClave()!=null?clienteMicrosip.getClave():null) );
                        qc.setIdClienteEvc(clientePk.getIdCliente());
                        qc.setIdClienteSistemTercero(clienteMicrosip.getIdClienteMicrosip());
                        qc.setIdSistemaTercero(idSistematercero);
                        qc.setIdEmpresa(idEmpresa);
                        clienteDaoImpl.insert(qc);
                        contadorRegistrosNuevos++;
                    }catch(Exception e){
                        mensajeCliente += " Error al insertar cliente con ID: "+clienteMicrosip.getIdClienteMicrosip() + "; ERROR: " + e.getMessage() + " , ";
                        e.printStackTrace();
                    }  
                }else{//actualizar
                    System.out.println("+++++++ CLIENTE ACTUALIZANDO");
                    try{
                        com.tsp.microsip.bean.Cliente clienteMicroSip = clienteMicrosip.getCliente();                     
                        clienteMicroSip.setIdCliente(clienteExistente.getIdClienteEvc());
                        clienteMicroSip.setSincronizacionMicrosip(1);
                        clienteMicroSip.setIdEstatus(1);
                        //--/-/-/
                        Cliente clienteEvc = new Cliente();
                        if(clienteMicroSip.getIdCliente() > 0)
                            clienteEvc.setIdCliente(clienteMicroSip.getIdCliente());                        
                        if(clienteMicroSip.getIdEmpresa() > 0)
                            clienteEvc.setIdEmpresa(clienteMicroSip.getIdEmpresa());
                        if(clienteMicroSip.getRfcCliente() != null )
                            clienteEvc.setRfcCliente(clienteMicroSip.getRfcCliente());
                        if(clienteMicroSip.getNombreCliente() != null)
                            clienteEvc.setNombreCliente(clienteMicroSip.getNombreCliente());
                        if(clienteMicroSip.getApellidoPaternoCliente() != null)
                            clienteEvc.setApellidoPaternoCliente(clienteMicroSip.getApellidoPaternoCliente());
                        if(clienteMicroSip.getApellidoMaternoCliente() != null)
                            clienteEvc.setApellidoMaternoCliente(clienteMicroSip.getApellidoMaternoCliente());
                        if(clienteMicroSip.getRazonSocial() != null)
                            clienteEvc.setRazonSocial(clienteMicroSip.getRazonSocial());
                        if(clienteMicroSip.getCalle() != null)
                            clienteEvc.setCalle(clienteMicroSip.getCalle());
                        if(clienteMicroSip.getNumero() != null)
                            clienteEvc.setNumero(clienteMicroSip.getNumero());
                        if(clienteMicroSip.getNumeroInterior() != null)
                            clienteEvc.setNumeroInterior(clienteMicroSip.getNumeroInterior());
                        if(clienteMicroSip.getColonia() != null)
                            clienteEvc.setColonia(clienteMicroSip.getColonia());
                        if(clienteMicroSip.getCodigoPostal() != null)
                            clienteEvc.setCodigoPostal(clienteMicroSip.getCodigoPostal());
                        if(clienteMicroSip.getPais() != null)
                            clienteEvc.setPais(clienteMicroSip.getPais());
                        if(clienteMicroSip.getEstado() != null)
                            clienteEvc.setEstado(clienteMicroSip.getEstado());
                        if(clienteMicroSip.getMunicipio() != null)
                            clienteEvc.setMunicipio(clienteMicroSip.getMunicipio());
                        if(clienteMicroSip.getLada() != null)
                            clienteEvc.setLada(clienteMicroSip.getLada());
                        if(clienteMicroSip.getTelefono() != null)
                            clienteEvc.setTelefono(clienteMicroSip.getTelefono());
                        if(clienteMicroSip.getExtension() != null)
                            clienteEvc.setExtension(clienteMicroSip.getExtension());
                        if(clienteMicroSip.getCelular() != null)
                            clienteEvc.setCelular(clienteMicroSip.getCelular());
                        if(clienteMicroSip.getCorreo() != null)
                            clienteEvc.setCorreo(clienteMicroSip.getCorreo());
                        if(clienteMicroSip.getContacto() != null)
                            clienteEvc.setContacto(clienteMicroSip.getContacto());
                        if(clienteMicroSip.getIdEstatus() > 0)
                            clienteEvc.setIdEstatus(clienteMicroSip.getIdEstatus());
                        if(clienteMicroSip.getLatitud() != null)
                            clienteEvc.setLatitud(clienteMicroSip.getLatitud());
                        if(clienteMicroSip.getLongitud() != null)
                            clienteEvc.setLongitud(clienteMicroSip.getLongitud());
                        if(clienteMicroSip.getGenerico() > 0)
                            clienteEvc.setGenerico(clienteMicroSip.getGenerico());
                        if(clienteMicroSip.getFolioClienteMovil() != null)
                            clienteEvc.setFolioClienteMovil(clienteMicroSip.getFolioClienteMovil());
                        if(clienteMicroSip.getDiasVisita() != null)
                            clienteEvc.setDiasVisita(clienteMicroSip.getDiasVisita());
                        if(clienteMicroSip.getSaldoCliente() > 0)
                            clienteEvc.setSaldoCliente(clienteMicroSip.getSaldoCliente());
                        if(clienteMicroSip.getPerioricidad() > 0)
                            clienteEvc.setPerioricidad(clienteMicroSip.getPerioricidad());
                        if(clienteMicroSip.getFechaUltimaVisita() != null)
                            clienteEvc.setFechaUltimaVisita(clienteMicroSip.getFechaUltimaVisita());
                        if(clienteMicroSip.getSincronizacionMicrosip() > 0)
                            clienteEvc.setSincronizacionMicrosip(clienteMicroSip.getSincronizacionMicrosip());
                        if(clienteMicroSip.getIdVendedorConsigna() > 0)
                            clienteEvc.setIdVendedorConsigna(clienteMicroSip.getIdVendedorConsigna());
                        if(clienteMicroSip.getAccesoConsolaXmlPdf() > 0)
                            clienteEvc.setAccesoConsolaXmlPdf(clienteMicroSip.getAccesoConsolaXmlPdf());
                        if(clienteMicroSip.getPermisoVentaCredito() > 0)
                            clienteEvc.setPermisoVentaCredito(clienteMicroSip.getPermisoVentaCredito());
                        if(clienteMicroSip.getNombreComercial() != null)
                            clienteEvc.setNombreComercial(clienteMicroSip.getNombreComercial());
                        
                        clienteEvc.setClave( StringManage.getValidString(clienteMicrosip.getClave()) );//(clienteMicrosip.getClave()!=null?clienteMicrosip.getClave():""));
                        //--/-/-/ 
                        clientesDaoImpl.update(clienteEvc.createPk(), clienteEvc);
                        contadorRegistrosActualizados++;
                    }catch(Exception e2){
                         mensajeCliente += " Error al actualizar cliente con ID: "+clienteMicrosip.getIdClienteMicrosip() + "; ERROR: " + e2.getMessage() + " , ";
                         e2.printStackTrace();
                    }
        
                }
            }
            
            if(mensajeCliente.trim().equals("")){
                controlBean.setExito(true);
            }
            controlBean.setMensajeError(mensajeCliente);
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
    
}
