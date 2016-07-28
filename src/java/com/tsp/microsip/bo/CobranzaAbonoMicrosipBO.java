/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.microsip.bo;

import com.tsp.microsip.bean.CobranzaAbonoMicrosip;
import com.tsp.microsip.bean.ControlBean;
import com.tsp.sct.bo.ClienteBO;
import com.tsp.sct.bo.SGCobranzaAbonoBO;
import com.tsp.sct.bo.SGPedidoBO;
import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.dto.Empleado;
import com.tsp.sct.dao.dto.QuartzCliente;
import com.tsp.sct.dao.dto.QuartzCobranzaAbono;
import com.tsp.sct.dao.dto.QuartzEmpleado;
import com.tsp.sct.dao.dto.QuartzPedido;
import com.tsp.sct.dao.dto.QuartzSgfensMetodoPago;
import com.tsp.sct.dao.dto.SgfensCobranzaAbono;
import com.tsp.sct.dao.dto.SgfensCobranzaAbonoPk;
import com.tsp.sct.dao.dto.SgfensCobranzaMetodoPago;
import com.tsp.sct.dao.dto.SgfensCobranzaMetodoPagoPk;
import com.tsp.sct.dao.dto.SgfensPedido;
import com.tsp.sct.dao.dto.Usuarios;
import com.tsp.sct.dao.jdbc.ClienteDaoImpl;
import com.tsp.sct.dao.jdbc.EmpleadoDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzClienteDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzCobranzaAbonoDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzEmpleadoDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzPedidoDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzSgfensMetodoPagoDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.dao.jdbc.SgfensCobranzaAbonoDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensCobranzaMetodoPagoDaoImpl;
import com.tsp.sct.dao.jdbc.UsuariosDaoImpl;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author ISCesar poseidon24@hotmail.com
 * @since 4/05/2015 4/05/2015 11:53:31 AM
 */
public class CobranzaAbonoMicrosipBO {

    /**
     * Busca la cantidad de coincidencias
     * de registros nuevos o actualizados, segun parametros
     * @param tipo tipo de busqueda, 1 para nuevos, 2 para modificados
     * @param idEmpresa
     * @return 
     */
    public int getCantidad(int tipo, int idEmpresa){
        int cantidad = 0;
        Connection conn = null;
        try{
            conn = ResourceManager.getConnection();
            //validamos si la busqueda es para registros nuevos o actualizados        
            String filtroBusqueda = "";
            if(tipo == 1){//nuevos
                filtroBusqueda = " AND SINCRONIZACION_MICROSIP = 0 ";
            }else if(tipo == 2){//actualizados
                filtroBusqueda = " AND SINCRONIZACION_MICROSIP = 2 ";
            }

            //filtrar por sucursal específica
            if(idEmpresa > 0){
                filtroBusqueda += " AND ID_EMPRESA = " +idEmpresa;
            }
            
            SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(conn);
            cantidad = cobranzaAbonoBO.findCantidadCobranzaAbono(-1, idEmpresa, 0, 0, filtroBusqueda);  
        }catch(Exception e){
            e.printStackTrace();
            cantidad =  0;            
        }finally{
            try{ if (conn!=null) conn.close(); }catch(Exception ex){}
        }
        return cantidad;  
    }
    
    public List<CobranzaAbonoMicrosip> getRegistros(int tipo, int idEmpresa){
        List<CobranzaAbonoMicrosip> listaRegistros = new ArrayList<CobranzaAbonoMicrosip>();
        Connection conn = null;
        try{
            conn = ResourceManager.getConnection();
            //validamos si la busqueda es para registros nuevos o actualizados        
            String filtroBusqueda = "";
            if(tipo == 1){//nuevos
                filtroBusqueda = " AND SINCRONIZACION_MICROSIP = 0 ";
            }else if(tipo == 2){//actualizados
                filtroBusqueda = " AND SINCRONIZACION_MICROSIP = 2 ";
            }

            //filtrar por sucursal específica
            if(idEmpresa > 0){
                filtroBusqueda += " AND ID_EMPRESA = " +idEmpresa;
            }
            
            SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(conn);
            SgfensCobranzaAbono[] registrosEncontrados = cobranzaAbonoBO.findCobranzaAbono(-1, idEmpresa, 0, 0, filtroBusqueda);  
            
            QuartzClienteDaoImpl qcldi = new QuartzClienteDaoImpl(conn);
            QuartzPedidoDaoImpl qpedi = new QuartzPedidoDaoImpl(conn);
            QuartzEmpleadoDaoImpl qedi = new QuartzEmpleadoDaoImpl(conn);
            QuartzSgfensMetodoPagoDaoImpl qmetpago = new QuartzSgfensMetodoPagoDaoImpl(conn);
            EmpleadoDaoImpl edi = new EmpleadoDaoImpl(conn);
            for(SgfensCobranzaAbono item : registrosEncontrados ){
                
                ///***Buscamos los ID correspondientes de cliente, empleado y de pedido en el sistema de microsip
                String claveClienteSistemaTercero = "";
                String claveEmpleadoSistemaTercero = "";
                String clavePedidoSistemaTercero =  "";
                try{
                    QuartzCliente quartzCliente = qcldi.findWhereIdClienteEvcEquals(item.getIdCliente())[0];
                    item.setIdCliente(quartzCliente.getIdClienteSistemTercero());
                    claveClienteSistemaTercero = StringManage.getValidString(quartzCliente.getClave());
                }catch(Exception e){
                    System.out.println("El Cliente con ID en pretoriano " + item.getIdCliente()+ " no ha sido sincronizado en el sistema de terceros.\n" + e.toString());
                    item.setIdCliente(-1);
                    //e.printStackTrace();
                }                
                try{
                    QuartzPedido quartzPedido = qpedi.findWhereIdPedidoEvcEquals(item.getIdPedido())[0];
                    item.setIdPedido(quartzPedido.getIdPedidoSistemTercero());
                    clavePedidoSistemaTercero = StringManage.getValidString(quartzPedido.getClave());
                }catch(Exception e){
                    System.out.println("El Pedido con ID en pretoriano " + item.getIdPedido()+ " no ha sido sincronizado en el sistema de terceros.\n" + e.toString());
                    item.setIdPedido(-1);
                    //e.printStackTrace();
                }
                
                try{
                    Empleado emp = edi.findWhereIdUsuariosEquals(item.getIdUsuarioVendedor())[0];
                    QuartzEmpleado quartzEmpleado = qedi.findWhereIdEmpleadoEvcEquals(emp.getIdEmpleado())[0];
                    item.setIdUsuarioVendedor(quartzEmpleado.getIdEmpleadoSistemTercero());
                    claveEmpleadoSistemaTercero = StringManage.getValidString(quartzEmpleado.getClave());
                }catch(Exception e){
                    System.out.println("El Usuario Vendedor con ID en pretoriano " + item.getIdUsuarioVendedor()+ " no ha sido sincronizado en el sistema de terceros.\n" + e.toString());
                    item.setIdUsuarioVendedor(-1);
                    //e.printStackTrace();
                }
                
                ///***
                
                com.tsp.microsip.bean.SgfensCobranzaAbono cobranzaMicro = new com.tsp.microsip.bean.SgfensCobranzaAbono();
                cobranzaMicro.setIdCobranzaAbono(item.getIdCobranzaAbono());
                cobranzaMicro.setIdEmpresa(item.getIdEmpresa());
                cobranzaMicro.setIdPedido(item.getIdPedido());
                cobranzaMicro.setIdComprobanteFiscal(item.getIdComprobanteFiscal());
                cobranzaMicro.setIdUsuarioVendedor(item.getIdUsuarioVendedor());
                cobranzaMicro.setIdCliente(item.getIdCliente());
                cobranzaMicro.setFechaAbono(item.getFechaAbono());
                cobranzaMicro.setMontoAbono(item.getMontoAbono());
                cobranzaMicro.setIdEstatus(item.getIdEstatus());
                cobranzaMicro.setIdCobranzaMetodoPago(item.getIdCobranzaMetodoPago());
                cobranzaMicro.setIdentificadorOperacion(item.getIdentificadorOperacion());
                cobranzaMicro.setComentarios(item.getComentarios());
                cobranzaMicro.setIdOperacionBancaria(item.getIdOperacionBancaria());
                cobranzaMicro.setLatitud(item.getLatitud());
                cobranzaMicro.setLongitud(item.getLongitud());
                cobranzaMicro.setNombreArchivoImgFirma(item.getNombreArchivoImgFirma());
                cobranzaMicro.setFolioCobranzaMovil(item.getFolioCobranzaMovil());
                cobranzaMicro.setIdDeposito(item.getIdDeposito()); 
                cobranzaMicro.setClaveClienteSistemaTercero(claveClienteSistemaTercero);
                cobranzaMicro.setClaveEmpleadoSistemaTercero(claveEmpleadoSistemaTercero);
                cobranzaMicro.setClavePedidoSistemaTercero(clavePedidoSistemaTercero);
                                
                CobranzaAbonoMicrosip wsBean = new CobranzaAbonoMicrosip();
                wsBean.setCobranzaAbono(cobranzaMicro);
                if(tipo == 2){//si es una actualización, envamos el id o clave del sistema de microsip (identificador)
                    try{
                        QuartzCobranzaAbono qc = new QuartzCobranzaAbonoDaoImpl(conn).findByDynamicWhere(" ID_COBRANZA_EVC = " + item.getIdCobranzaAbono() + " AND ID_EMPRESA = " + idEmpresa+ " ORDER BY ID_QUARTZ DESC ", null)[0]; //buscamos el registro correspondiente
                        if(qc != null){
                            if(qc.getIdCobranzaSistemTercero() > 0){
                                wsBean.setIdMicrosip(qc.getIdCobranzaSistemTercero());
                            }else if(qc.getClave() != null && !qc.getClave().trim().equals("")){
                                wsBean.setClave(qc.getClave());
                            }
                        }
                    }catch(Exception e){/*e.printStackTrace();*/}                
                }
                
                //cargamos el metodo de pago al objeto:
                try{
                    SgfensCobranzaMetodoPago scmp = new SgfensCobranzaMetodoPagoDaoImpl(conn).findByPrimaryKey(item.getIdCobranzaMetodoPago());
                    
                    com.tsp.microsip.bean.SgfensCobranzaMetodoPago metodoPagoMicro = new com.tsp.microsip.bean.SgfensCobranzaMetodoPago();
                    ///***Buscamos los ID correspondientes de metodo pago en el sistema de microsip
                    try{
                        QuartzSgfensMetodoPago quartzMetodoPago = qmetpago.findWhereIdMetodoPagoEvcEquals(item.getIdCobranzaMetodoPago())[0];
                        item.setIdCobranzaMetodoPago(quartzMetodoPago.getIdMetodoPagoSistemTercero());
                        scmp.setIdCobranzaMetodoPago(quartzMetodoPago.getIdMetodoPagoSistemTercero());
                    }catch(Exception e){
                        System.out.println("El Metodo de Pago con ID en pretoriano " + item.getIdCobranzaMetodoPago()+ " no ha sido sincronizado en el sistema de terceros.\n" + e.toString());
                        scmp.setIdCobranzaMetodoPago(-1);//No ha sido sincronizado en sistema tercero
                        //e.printStackTrace();
                    }

                    metodoPagoMicro.setIdCobranzaMetodoPago(scmp.getIdCobranzaMetodoPago());
                    metodoPagoMicro.setNombreMetodoPago(scmp.getNombreMetodoPago());
                    metodoPagoMicro.setDescripcionMetodoPago(scmp.getDescripcionMetodoPago());
                    metodoPagoMicro.setSincronizacionMicrosip(scmp.getSincronizacionMicrosip());
                        
                        ///***
                    
                    wsBean.setMetodoPago(metodoPagoMicro);
                }catch(Exception e){}
                
                listaRegistros.add(wsBean);
            }
            
        }catch(Exception e){
            e.printStackTrace();           
        }finally{
            try{ if (conn!=null) conn.close(); }catch(Exception ex){}
        }
        return listaRegistros;  
    }
    
    public int setIdentificadores(List<QuartzCobranzaAbono> quartzDto, int idEmpresa){
        
        System.out.println("-------------------------");
        System.out.println("-------------------------");
        System.out.println("-------------------------");
        System.out.println("INSERTANDO IDENTIFICADORES ...");
        System.out.println("-------------------------");
        System.out.println("-------------------------");
        System.out.println("-------------------------");
        int insertarActualizacion = 0;
        
        Connection conn = null;
        try{
            conn = ResourceManager.getConnection();
            SgfensCobranzaAbonoDaoImpl cobranzaDao = new SgfensCobranzaAbonoDaoImpl(conn);
            //List<QuartzCobranzaAbono> quartzIdSincronizacion = new ArrayList<QuartzCobranzaAbono>();
            QuartzCobranzaAbonoDaoImpl quartzCobranzaAbonoDaoImpl = new QuartzCobranzaAbonoDaoImpl(conn);
            QuartzCobranzaAbonoDaoImpl qcDaoImp = new QuartzCobranzaAbonoDaoImpl(conn);
            
            System.out.println("************************* A");
            System.out.println("************************* TAMANO DE LA LISTA DE IDENTIFICADORES: "+quartzDto.size());
            for(QuartzCobranzaAbono quaC : quartzDto){
                System.out.println("************************* B: "+quaC.getIdCobranzaEvc() + ", "+quaC.getIdCobranzaSistemTercero());
                //Vemos si ya existe el registro:
                QuartzCobranzaAbono quartzCobranza = null;
                if(quaC.getIdCobranzaSistemTercero() > 0){ //verificamos si se buscara por id o por clave
                    try{quartzCobranza = quartzCobranzaAbonoDaoImpl.findByDynamicWhere( " ID_COBRANZA_EVC = " + quaC.getIdCobranzaEvc() + " AND ID_COBRANZA_SISTEM_TERCERO = " + quaC.getIdCobranzaSistemTercero() + " AND ID_EMPRESA = " + idEmpresa , null)[0];}catch(Exception e){
                        /*e.printStackTrace();*/
                        System.out.println("************************* C ES NULO");
                    }
                }else if(quaC.getClave() != null && !quaC.getClave().equals("")){
                    try{quartzCobranza = quartzCobranzaAbonoDaoImpl.findByDynamicWhere( " ID_COBRANZA_EVC = " + quaC.getIdCobranzaEvc() + " AND CLAVE = '" + quaC.getClave() + "' AND ID_COBRANZA_SISTEM_TERCERO = " + quaC.getIdCobranzaSistemTercero() + " AND ID_EMPRESA = " + idEmpresa , null)[0];}catch(Exception e){
                    /*e.printStackTrace();*/
                        System.out.println("************************* D ES CLAVE VACIA");
                    }
                }
                insertarActualizacion = 0;
                if(quartzCobranza == null){//si no existe lo insertamos
                    
                    System.out.println("************************* E es nuuuuuuuuuuuuuuuuuuuuuuull . . .");
                    
                    quartzCobranza = new QuartzCobranzaAbono();
                    quartzCobranza.setIdCobranzaEvc(quaC.getIdCobranzaEvc());
                    if(quaC.getIdCobranzaSistemTercero() > 0){
                        quartzCobranza.setIdCobranzaSistemTercero(quaC.getIdCobranzaSistemTercero());
                    }else{
                        quartzCobranza.setIdCobranzaSistemTercero(0);
                    }
                    System.out.println("************************* E 2 es nuuuuuuuuuuuuuuuuuuuuuuull . . .");
                    if(quaC.getClave() != null && !quaC.getClave().equals("")){
                        quartzCobranza.setClave(quaC.getClave());
                    }else{
                        quartzCobranza.setClave("");
                    }
                    quartzCobranza.setIdEmpresa(idEmpresa);
                    //quartzIdSincronizacion.add(quartzCobranza);
                    System.out.println("************************* E 3 es nuuuuuuuuuuuuuuuuuuuuuuull . . .");
                    qcDaoImp.insert(quartzCobranza);
                    insertarActualizacion = 1;
                }else{//si existe, borramos e insertamos el registro para que sea el ultimo registro insertado y así al recuperar los ID sea el mas actual.
                    System.out.println("************************* F existe el vaaaaaaaaaaaaaalor . . .");
                    try{quartzCobranzaAbonoDaoImpl.delete(quartzCobranza.createPk());
                    QuartzCobranzaAbono quartzCobranzaClon = new QuartzCobranzaAbono();
                    quartzCobranzaClon.setIdCobranzaEvc(quartzCobranza.getIdCobranzaEvc());
                    quartzCobranzaClon.setIdCobranzaSistemTercero(quartzCobranza.getIdCobranzaSistemTercero());
                    quartzCobranzaClon.setClave(quartzCobranza.getClave());
                    quartzCobranzaClon.setIdSistemaTercero(quartzCobranza.getIdSistemaTercero());
                    quartzCobranzaClon.setIdEmpresa(quartzCobranza.getIdEmpresa());
                    quartzCobranzaAbonoDaoImpl.insert(quartzCobranzaClon);
                    insertarActualizacion = 1;
                    }catch(Exception e){System.out.println("------------ Error al actualizar Identificadores de quartz de cobranza: "+e.getMessage());}
                }
                
                //actualizamos la bandera de cobranzas a sincronizado:
                try{
                    SgfensCobranzaAbono cobranza = new SGCobranzaAbonoBO(quaC.getIdCobranzaEvc(), conn).getCobranzaAbono();
                    cobranza.setSincronizacionMicrosip(1);
                    cobranzaDao.update(cobranza.createPk(), cobranza);
                }catch(Exception e){/*e.printStackTrace();*/}
                
            }
            /*
            try{
                
                for(QuartzCobranzaAbono insertDto : quartzIdSincronizacion){//insertamos los registros:
                    qcDaoImp.insert(insertDto);
                }
                insertarActualizacion = 1;
            }catch(Exception e){
                e.printStackTrace();
            }*/
            
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
            insertarActualizacion = 0;
        }finally{
            try{ if (conn!=null) conn.close(); }catch(Exception ex){}
        }
        
        return insertarActualizacion;
    }
    
    public ControlBean setCobranzaMicrosip(List<CobranzaAbonoMicrosip> cobranzasMicrosipBean, int idEmpresa, String acceso){
        ControlBean controlBean = new ControlBean();
        String mensajeCobranza = "";
        Connection conn = null;
        try{            
            conn = ResourceManager.getConnection();
            SgfensCobranzaAbonoDaoImpl cobranzasDaoImpl = new SgfensCobranzaAbonoDaoImpl(conn);
            QuartzCobranzaAbonoDaoImpl cobranzaDaoImpl = new QuartzCobranzaAbonoDaoImpl(conn);
            QuartzCobranzaAbono cobranzaExistente = null;
            QuartzEmpleadoDaoImpl quartzEmpleadoDaoImpl = new QuartzEmpleadoDaoImpl(conn);
            QuartzCliente quartzCliente = null;
            QuartzClienteDaoImpl quartzClienteDaoImpl = new QuartzClienteDaoImpl(conn);
            
            
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
            
            for(CobranzaAbonoMicrosip cobranzaMicrosip : cobranzasMicrosipBean){
                //validamos si el cobranza existe o no en el evc
                try{
                    //cobranzaExistente = cobranzaDaoImpl.findByDynamicWhere("ID_COBRANZA_SISTEM_TERCERO = " + cobranzaMicrosip.getIdMicrosip() + " AND ID_EMPRESA = " + idEmpresa, null)[0];                    
                    String queryOpcional = "";
                    if (cobranzaMicrosip.getIdMicrosip()>0){
                        queryOpcional = " ID_COBRANZA_SISTEM_TERCERO = " + cobranzaMicrosip.getIdMicrosip();
                    }else{
                        if (StringManage.getValidString(cobranzaMicrosip.getClave()).length()>0)
                            queryOpcional = " CLAVE='" + StringManage.getValidString(cobranzaMicrosip.getClave()) + "'";
                    }
                    cobranzaExistente = cobranzaDaoImpl.findByDynamicWhere(queryOpcional +" AND ID_EMPRESA = " + idEmpresa, null)[0];
                }catch(Exception e1){
                    cobranzaExistente = null;
                }
                
                if (cobranzaExistente==null){
                    //si no la encontramos directamente por ID de Cobranza Sistema Tercero, intentamos con el Folio de Cobranza Movil
                    // si es que este fue enviado
                    try{
                        if(StringManage.getValidString(cobranzaMicrosip.getCobranzaAbono().getFolioCobranzaMovil()).length()>0 ){
                            cobranzaExistente = cobranzaDaoImpl.findByDynamicWhere("ID_EMPRESA = " + idEmpresa + " AND ID_COBRANZA_EVC IN (SELECT ID_COBRANZA_ABONO FROM SGFENS_COBRANZA_ABONO WHERE FOLIO_COBRANZA_MOVIL = '"+ cobranzaMicrosip.getCobranzaAbono().getFolioCobranzaMovil() +"')", null)[0];
                        }
                    }catch(Exception e2){cobranzaExistente = null;}
                }
                
                if(cobranzaExistente == null){//cobranza nuevo
                    try{
                        com.tsp.microsip.bean.SgfensCobranzaAbono cobranzaMicroSip = cobranzaMicrosip.getCobranzaAbono();
                        /*SgfensCobranzaAbono ultimoRegistroCobranzaAbono = cobranzasDaoImpl.findLast();
                        int idCobranzaNuevo = ultimoRegistroCobranzaAbono.getIdCobranza() + 1;            
                        cobranzaMicroSip.setIdCobranza(idCobranzaNuevo);*/
                        
                        cobranzaMicroSip.setIdEstatus(1);
                        cobranzaMicroSip.setSincronizacionMicrosip(1);
                        
                        //--/-/-/
                        SgfensCobranzaAbono cobranzaEvc = new SgfensCobranzaAbono();
                        //if(cobranzaMicroSip.getIdCobranzaAbono() > 0)
                        //cobranzaEvc.setIdCobranzaAbono(cobranzaMicroSip.getIdCobranzaAbono());
                        //if(cobranzaMicroSip.getIdEmpresa() > 0)
                        cobranzaEvc.setIdEmpresa(idEmpresa);
                        //recuperamos los id correspondientes
                        boolean insertarRegistroCliente = false;
                        boolean insertarRegistroPedido = false;
                        try{
                            //QuartzPedido qp = new QuartzPedidoDaoImpl(conn).findWhereIdPedidoSistemTerceroEquals(cobranzaMicroSip.getIdPedido())[0];
                            QuartzPedido qp = new QuartzPedidoDaoImpl(conn).findByDynamicWhere("ID_PEDIDO_SISTEM_TERCERO = "  + cobranzaMicrosip.getCobranzaAbono().getIdPedido() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                            cobranzaEvc.setIdPedido(qp.getIdPedidoEvc());
                            insertarRegistroPedido = true;
                        }catch(Exception e){
                            //IMPORTANTE: NO QUITAR DE RESPUESTA SIMBOLOS PIPES '|', guiones '-' NI SIMBOLOS DE CORCHETE '[]' NI LA PALABRA 'ADVERTENCIA' YA QUE SE USA COMO TOKEN EN EL CLIENTE
                             mensajeCobranza += " -ADVERTENCIA-["+cobranzaMicrosip.getIdMicrosip()+"] Al parecer no esta sincronizado el Pedido con ID: "+cobranzaMicrosip.getCobranzaAbono().getIdPedido() + " y por tanto no se inserto/actualizo la cobranza; ERROR: " + e.getMessage() + " | ";
                             controlBean.setExito(false);
                             insertarRegistroPedido = false;
                            //e.printStackTrace();
                        }
                        
                        //cobranzaEvc.setIdPedido(cobranzaMicroSip.getIdPedido());
                        
                        try{//identificadore de vendedor-usuario
                            //QuartzEmpleado quartzEmpleado = quartzEmpleadoDaoImpl.findWhereIdEmpleadoSistemTerceroEquals(cobranzaMicroSip.getIdUsuarioVendedor())[0];
                            QuartzEmpleado quartzEmpleado = quartzEmpleadoDaoImpl.findByDynamicWhere("ID_EMPLEADO_SISTEM_TERCERO = " + cobranzaMicroSip.getIdUsuarioVendedor() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                            Empleado empleado = new EmpleadoDaoImpl(conn).findByPrimaryKey(quartzEmpleado.getIdEmpleadoEvc());
                            cobranzaEvc.setIdUsuarioVendedor(empleado.getIdUsuarios());
                        }catch(Exception e){
                            mensajeCobranza += " Error al relacionar el empleado con ID: "+cobranzaMicroSip.getIdUsuarioVendedor()+" (puede que no exista o este sincronizado), relacionado a la cobranza con ID: "+cobranzaMicroSip.getIdCobranzaAbono() + "; ERROR: " + e.getMessage() + " , ";                            
                        }
                        quartzCliente = null;
                        try{//identificadores de cliente
                            //quartzCliente = quartzClienteDaoImpl.findWhereIdClienteSistemTerceroEquals(cobranzaMicroSip.getIdCliente())[0];
                            quartzCliente = quartzClienteDaoImpl.findByDynamicWhere("ID_CLIENTE_SISTEM_TERCERO = " + cobranzaMicroSip.getIdCliente() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                            cobranzaEvc.setIdCliente(quartzCliente.getIdClienteEvc());
                            insertarRegistroCliente = true;
                        }catch(Exception e){
                            mensajeCobranza += " Error al relacionar el cliente con ID: "+cobranzaMicroSip.getIdCliente()+" (puede que no exista o este sincronizado), relacionado a la cobranza con ID: "+cobranzaMicroSip.getIdCobranzaAbono() + " y por tanto no se inserto/actualizo la cobranza; ERROR: " + e.getMessage() + " , ";
                            insertarRegistroCliente = false;
                            controlBean.setExito(false);
                        }                       
                        //cobranzaEvc.setIdCliente(cobranzaMicroSip.getIdCliente());
                        cobranzaEvc.setFechaAbono(cobranzaMicroSip.getFechaAbono());
                        cobranzaEvc.setMontoAbono(cobranzaMicroSip.getMontoAbono());                        
                        cobranzaEvc.setIdEstatus(cobranzaMicroSip.getIdEstatus());
                        /*try{
                            //QuartzSgfensMetodoPago qmp = new QuartzSgfensMetodoPagoDaoImpl(conn).findWhereIdMetodoPagoSistemTerceroEquals(cobranzaMicroSip.getIdCobranzaMetodoPago())[0];                            
                            QuartzSgfensMetodoPago qmp = new QuartzSgfensMetodoPagoDaoImpl(conn).findByDynamicWhere("ID_METODO_PAGO_SISTEM_TERCERO = " + cobranzaMicroSip.getIdCobranzaMetodoPago() + " AND ID_EMPRESA = " + idEmpresa, null)[0];                            
                            cobranzaEvc.setIdCobranzaMetodoPago(qmp.getIdMetodoPagoEvc());
                        }catch(Exception e){
                            mensajeCobranza += " Error al relacionar el metodo de pago con ID: "+cobranzaMicroSip.getIdCobranzaMetodoPago()+" (puede que no exista o este sincronizado), relacionado a la cobranza con ID: "+cobranzaMicroSip.getIdCobranzaAbono() + "; ERROR: " + e.getMessage() + " , ";
                        } */
                        
                        QuartzSgfensMetodoPago qmp = null; 
                        try{                           
                            //cobranzaExistente = cobranzaDaoImpl.findByDynamicWhere("ID_COBRANZA_SISTEM_TERCERO = " + cobranzaMicrosip.getIdMicrosip() + " AND ID_EMPRESA = " + idEmpresa, null)[0];                    
                            String queryOpcional = "";
                            if (cobranzaMicroSip.getIdCobranzaMetodoPago()>0){
                                queryOpcional = " ID_METODO_PAGO_SISTEM_TERCERO = " + cobranzaMicroSip.getIdCobranzaMetodoPago();                                                          
                            }else{
                                if (StringManage.getValidString(cobranzaMicrosip.getMetodoPago().getNombreMetodoPago()).length()>0)
                                    queryOpcional = " CLAVE='" + StringManage.getValidString(cobranzaMicrosip.getMetodoPago().getNombreMetodoPago()) + "'";
                            }
                            qmp = new QuartzSgfensMetodoPagoDaoImpl(conn).findByDynamicWhere(queryOpcional +" AND ID_EMPRESA = " + idEmpresa, null)[0];
                                                        
                        }catch(Exception e1){
                            qmp = null;
                        }
                        
                        //Si no existe el metodo de pago lo creamos, posteriormente lo relacionamos, buscandolo de nuevo
                        if(qmp==null){
                            
                            SgfensCobranzaMetodoPago metodoPagoMag = new SgfensCobranzaMetodoPago();
                            metodoPagoMag.setNombreMetodoPago(cobranzaMicrosip.getMetodoPago().getNombreMetodoPago());
                            metodoPagoMag.setDescripcionMetodoPago(cobranzaMicrosip.getMetodoPago().getNombreMetodoPago());
                            metodoPagoMag.setTipoPago("ENTRADA");
                            metodoPagoMag.setSincronizacionMicrosip(1);
                            
                            SgfensCobranzaMetodoPagoPk metodoInsert =  new SgfensCobranzaMetodoPagoDaoImpl(conn).insert(metodoPagoMag);
                            
                            
                            QuartzSgfensMetodoPago qMetodo =  new QuartzSgfensMetodoPago();
                            qMetodo.setClave(cobranzaMicrosip.getMetodoPago().getNombreMetodoPago());
                            qMetodo.setIdMetodoPagoEvc(metodoInsert.getIdCobranzaMetodoPago());
                            qMetodo.setIdEmpresa(idEmpresa);
                            qMetodo.setIdSistemaTercero(idSistematercero);
                            
                            new QuartzSgfensMetodoPagoDaoImpl(conn).insert(qMetodo);
                            
                            try{
                                //Buscamos nuevamente el metodo insertado
                                String queryOpcional = "";
                                if (cobranzaMicroSip.getIdCobranzaMetodoPago()>0){
                                    queryOpcional = " ID_METODO_PAGO_SISTEM_TERCERO = " + cobranzaMicroSip.getIdCobranzaMetodoPago();                                                          
                                }else{
                                    if (StringManage.getValidString(cobranzaMicrosip.getMetodoPago().getNombreMetodoPago()).length()>0)
                                        queryOpcional = " CLAVE='" + StringManage.getValidString(cobranzaMicrosip.getMetodoPago().getNombreMetodoPago()) + "'";
                                }
                                qmp = new QuartzSgfensMetodoPagoDaoImpl(conn).findByDynamicWhere(queryOpcional +" AND ID_EMPRESA = " + idEmpresa, null)[0];
                            
                            }catch(Exception e){
                                mensajeCobranza += " Error al relacionar el metodo de pago con ID: "+cobranzaMicroSip.getIdCobranzaMetodoPago()+" (puede que no exista o este sincronizado), relacionado a la cobranza con ID: "+cobranzaMicroSip.getIdCobranzaAbono() + "; ERROR: " + e.getMessage() + " , ";
                            }
                            
                        }
                        
                        cobranzaEvc.setIdCobranzaMetodoPago(qmp.getIdMetodoPagoEvc());
                        
                        cobranzaEvc.setIdentificadorOperacion(cobranzaMicroSip.getIdentificadorOperacion());
                        cobranzaEvc.setComentarios(cobranzaMicroSip.getComentarios());
                        //cobranzaEvc.setIdCobranzaMetodoPago(cobranzaMicroSip.getIdCobranzaMetodoPago());
                        cobranzaEvc.setIdOperacionBancaria(cobranzaMicroSip.getIdOperacionBancaria());
                        cobranzaEvc.setLatitud(cobranzaMicroSip.getLatitud());
                        cobranzaEvc.setLongitud(cobranzaMicroSip.getLongitud());
                        cobranzaEvc.setFolioCobranzaMovil(cobranzaMicroSip.getFolioCobranzaMovil());
                        cobranzaEvc.setSincronizacionMicrosip(cobranzaMicroSip.getSincronizacionMicrosip());
                        
                        //--/-/-/                         
                        if(insertarRegistroPedido && insertarRegistroCliente){
                            
                            int idCobranzaInsertado = 0;
                            
                            try{
                            //Insistimos buscando si el registro no existe ya en la base de datos, ahora directamente
                            // en la tabla sfgens_cobranza_abono, si ya existe, solo falta crear el registro en quartz_cobranza_abono
                                if(StringManage.getValidString(cobranzaMicrosip.getCobranzaAbono().getFolioCobranzaMovil()).length()>0 ){
                                    idCobranzaInsertado = cobranzasDaoImpl.findByDynamicWhere(" ID_EMPRESA = " + idEmpresa + " AND FOLIO_COBRANZA_MOVIL = '"+ cobranzaMicrosip.getCobranzaAbono().getFolioCobranzaMovil() +"'", null)[0].getIdCobranzaAbono();
                                }
                            }catch(Exception e2){idCobranzaInsertado = 0;}
                            
                            //*/*/*/*/*//
                            boolean saldoSuficiente = true;
                            if (idCobranzaInsertado==0){
                            //si no encontramos coincidencia previa del registro por "folio_cobranza_movil", procedemos a crearlo
                                try{
                                    SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(conn);
                                    SgfensPedido pedidoDto = new SGPedidoBO(cobranzaEvc.getIdPedido(),conn).getPedido();
                                    //ComprobanteFiscal comprobanteFiscalDto = new ComprobanteFiscalBO(idComprobanteFiscal, user.getConn()).getComprobanteFiscal();
                                    Cliente  clienteDto = null;
                                    if (pedidoDto!=null){
                                        clienteDto = new ClienteBO(pedidoDto.getIdCliente(),conn).getCliente();
                                    }/*else if (comprobanteFiscalDto!=null){
                                        clienteDto = new ClienteBO(comprobanteFiscalDto.getIdCliente(),user.getConn()).getCliente();
                                    }*/

                                    if(cobranzaEvc.getIdCobranzaMetodoPago()==6){                    

                                        if(cobranzaEvc.getMontoAbono()>clienteDto.getSaldoCliente()){
                                            saldoSuficiente = false;
                                            mensajeCobranza+="Saldo del cliente Insuficiente. No debería de existir este registro de cobranza.";
                                        }

                                    }

                                    if(saldoSuficiente){                                 
                                        Usuarios[] userValido = new UsuariosDaoImpl(conn).findByDynamicWhere(" ID_EMPRESA = "+ idEmpresa + " AND ID_ESTATUS = 1 ORDER BY ID_USUARIOS ASC ", null);
                                        Usuarios user = userValido[0];
                                        cobranzaAbonoBO.sincronizacionMicrosip = 1;
                                        idCobranzaInsertado = cobranzaAbonoBO.registrarAbono(user, cobranzaEvc.getIdPedido(), 
                                                0, cobranzaEvc.getMontoAbono(), cobranzaEvc.getIdCobranzaMetodoPago(), 
                                                cobranzaEvc.getIdentificadorOperacion()!=null?cobranzaEvc.getIdentificadorOperacion():"", cobranzaEvc.getComentarios(),
                                                0, //En este parametro va el ID en caso de operacion bancaria (pago con TDC)
                                                0,0, //Latitud,Longitud
                                                null,//Archivo de imagen de firma
                                                cobranzaMicroSip.getFolioCobranzaMovil(), //Folio de cobranza movil - No aplica en consola
                                                cobranzaEvc.getFechaAbono()!=null?cobranzaEvc.getFechaAbono():(new Date()), //Fecha y hr de creación del pedido (actual)
                                                true,
                                                "",null); //Registro capturado en consola (true)

                                        if(cobranzaEvc.getIdCobranzaMetodoPago()==6){
                                            double nuevoSaldoCliente = clienteDto.getSaldoCliente() - cobranzaEvc.getMontoAbono();
                                            clienteDto.setSaldoCliente(nuevoSaldoCliente);                        
                                            new ClienteDaoImpl(conn).update(clienteDto.createPk(), clienteDto);
                                        }
                                    }

                                }catch(Exception e){
                                    mensajeCobranza+=" Error al registrar el abono: " + e.getMessage();
                                    if (e.getMessage().contains("no puede exceder al saldo restante")){
                                        //IMPORTANTE: NO QUITAR DE RESPUESTA SIMBOLOS PIPES '|', guiones '-' NI SIMBOLOS DE CORCHETE '[]' NI LA PALABRA 'ADVERTENCIA' YA QUE SE USA COMO TOKEN EN EL CLIENTE
                                        mensajeCobranza += " -ADVERTENCIA-["+cobranzaMicrosip.getIdMicrosip()+"] | ";
                                    }
                                    System.out.println("------- ERROR EN REGISTRAR ABONOS DE MICROSIP: ");
                                    e.printStackTrace();
                                }
                            }
                            //*/*/*/*/*//
                            
                            //SgfensCobranzaAbonoPk cobranzaPk = cobranzasDaoImpl.insert(cobranzaEvc);
                            ////insertamos la relacion con los Id's
                            if(idCobranzaInsertado > 0){
                                QuartzCobranzaAbono qc = new QuartzCobranzaAbono();
                                qc.setClave( (cobranzaMicrosip.getClave()!=null?cobranzaMicrosip.getClave():null) );
                                qc.setIdCobranzaEvc(idCobranzaInsertado);
                                qc.setIdCobranzaSistemTercero(cobranzaMicrosip.getIdMicrosip());
                                qc.setIdSistemaTercero(1);
                                qc.setIdEmpresa(idEmpresa);
                                cobranzaDaoImpl.insert(qc);
                                contadorRegistrosNuevos++;
                            }
                        }

                        
                    }catch(Exception e){
                        mensajeCobranza += " Error al insertar cobranza con ID: "+cobranzaMicrosip.getIdMicrosip() + "; ERROR: " + e.getMessage() + " , ";
                    }  
                }else{//actualizar
                    try{
                        com.tsp.microsip.bean.SgfensCobranzaAbono cobranzaMicroSip = cobranzaMicrosip.getCobranzaAbono();                     
                        cobranzaMicroSip.setIdCobranzaAbono(cobranzaExistente.getIdCobranzaEvc());
                        cobranzaMicroSip.setSincronizacionMicrosip(1);
                        cobranzaMicroSip.setIdEstatus(1);
                        //--/-/-/
                        SgfensCobranzaAbono cobranzaEvc = new SgfensCobranzaAbono();
                        cobranzaEvc.setIdCobranzaAbono(cobranzaMicroSip.getIdCobranzaAbono());                        
                        cobranzaEvc.setIdEmpresa(idEmpresa);
                        //recuperamos los id correspondientes
                        boolean insertarRegistro = false;
                        try{
                            //QuartzPedido qp = new QuartzPedidoDaoImpl(conn).findWhereIdPedidoSistemTerceroEquals(cobranzaMicroSip.getIdPedido())[0];
                            QuartzPedido qp = new QuartzPedidoDaoImpl(conn).findByDynamicWhere("ID_PEDIDO_SISTEM_TERCERO = " + cobranzaMicroSip.getIdPedido() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                            cobranzaEvc.setIdPedido(qp.getIdPedidoEvc());
                            insertarRegistro = true;
                        }catch(Exception e){
                            //IMPORTANTE: NO QUITAR DE RESPUESTA SIMBOLOS PIPES '|', guiones '-' NI SIMBOLOS DE CORCHETE '[]' NI LA PALABRA 'ADVERTENCIA' YA QUE SE USA COMO TOKEN EN EL CLIENTE
                             mensajeCobranza += " -ADVERTENCIA-["+cobranzaMicrosip.getIdMicrosip()+"] Al parecer no esta sincronizado el Pedido con ID: "+cobranzaMicrosip.getIdMicrosip() + " y por tanto no se inserto/actualizo la cobranza; ERROR: " + e.getMessage() + " | ";
                             controlBean.setExito(false);
                             insertarRegistro = false;
                            e.printStackTrace();
                        }
                        
                        //cobranzaEvc.setIdPedido(cobranzaMicroSip.getIdPedido());
                        
                        try{//identificadore de vendedor-usuario
                            //QuartzEmpleado quartzEmpleado = quartzEmpleadoDaoImpl.findWhereIdEmpleadoSistemTerceroEquals(cobranzaMicroSip.getIdUsuarioVendedor())[0];
                            QuartzEmpleado quartzEmpleado = quartzEmpleadoDaoImpl.findByDynamicWhere("ID_EMPLEADO_SISTEM_TERCERO = " + cobranzaMicroSip.getIdUsuarioVendedor() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                            Empleado empleado = new EmpleadoDaoImpl(conn).findByPrimaryKey(quartzEmpleado.getIdEmpleadoEvc());
                            cobranzaEvc.setIdUsuarioVendedor(empleado.getIdUsuarios());
                        }catch(Exception e){
                            mensajeCobranza += " Error al relacionar el empleado con ID: "+cobranzaMicroSip.getIdUsuarioVendedor()+" (puede que no exista o este sincronizado), relacionado a la cobranza con ID: "+cobranzaMicroSip.getIdCobranzaAbono() + "; ERROR: " + e.getMessage() + " , ";                            
                        }
                        quartzCliente = null;
                        try{//identificadores de cliente
                            //quartzCliente = quartzClienteDaoImpl.findWhereIdClienteSistemTerceroEquals(cobranzaMicroSip.getIdCliente())[0];
                            quartzCliente = quartzClienteDaoImpl.findByDynamicWhere("ID_CLIENTE_SISTEM_TERCERO = " + cobranzaMicroSip.getIdCliente() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                            cobranzaEvc.setIdCliente(quartzCliente.getIdClienteEvc());
                            insertarRegistro = true;
                        }catch(Exception e){
                            mensajeCobranza += " Error al relacionar el cliente con ID: "+cobranzaMicroSip.getIdCliente()+" (puede que no exista o este sincronizado), relacionado a la cobranza con ID: "+cobranzaMicroSip.getIdCobranzaAbono() + " y por tanto no se inserto/actualizo la cobranza; ERROR: " + e.getMessage() + " , ";
                            insertarRegistro = false;
                            controlBean.setExito(false);
                        }
                        //cobranzaEvc.setIdCliente(cobranzaMicroSip.getIdCliente());
                        cobranzaEvc.setFechaAbono(cobranzaMicroSip.getFechaAbono());
                        cobranzaEvc.setMontoAbono(cobranzaMicroSip.getMontoAbono());
                        cobranzaEvc.setIdEstatus(cobranzaMicroSip.getIdEstatus());
                        /*try{
                            //QuartzSgfensMetodoPago qmp = new QuartzSgfensMetodoPagoDaoImpl(conn).findWhereIdMetodoPagoSistemTerceroEquals(cobranzaMicroSip.getIdCobranzaMetodoPago())[0];
                            QuartzSgfensMetodoPago qmp = new QuartzSgfensMetodoPagoDaoImpl(conn).findByDynamicWhere("ID_METODO_PAGO_SISTEM_TERCERO = " + cobranzaMicroSip.getIdCobranzaMetodoPago() + " AND ID_EMPRESA = " + idEmpresa, null)[0];                            
                            cobranzaEvc.setIdCobranzaMetodoPago(qmp.getIdMetodoPagoEvc());
                        }catch(Exception e){
                            mensajeCobranza += " Error al relacionar el metodo de pago con ID: "+cobranzaMicroSip.getIdCobranzaMetodoPago()+" (puede que no exista o este sincronizado), relacionado a la cobranza con ID: "+cobranzaMicroSip.getIdCobranzaAbono() + "; ERROR: " + e.getMessage() + " , ";
                        } */
                        
                        QuartzSgfensMetodoPago qmp = null; 
                        try{                           
                            //cobranzaExistente = cobranzaDaoImpl.findByDynamicWhere("ID_COBRANZA_SISTEM_TERCERO = " + cobranzaMicrosip.getIdMicrosip() + " AND ID_EMPRESA = " + idEmpresa, null)[0];                    
                            String queryOpcional = "";
                            if (cobranzaMicroSip.getIdCobranzaMetodoPago()>0){
                                queryOpcional = " ID_METODO_PAGO_SISTEM_TERCERO = " + cobranzaMicroSip.getIdCobranzaMetodoPago();                                                          
                            }else{
                                if (StringManage.getValidString(cobranzaMicrosip.getMetodoPago().getNombreMetodoPago()).length()>0)
                                    queryOpcional = " CLAVE='" + StringManage.getValidString(cobranzaMicrosip.getMetodoPago().getNombreMetodoPago()) + "'";
                            }
                            qmp = new QuartzSgfensMetodoPagoDaoImpl(conn).findByDynamicWhere(queryOpcional +" AND ID_EMPRESA = " + idEmpresa, null)[0];
                                                        
                        }catch(Exception e1){
                            qmp = null;
                        }
                        
                        //Si no existe el metodo de pago lo creamos, posteriormente lo relacionamos, buscandolo de nuevo
                        if(qmp==null){
                            
                            SgfensCobranzaMetodoPago metodoPagoMag = new SgfensCobranzaMetodoPago();
                            metodoPagoMag.setNombreMetodoPago(cobranzaMicrosip.getMetodoPago().getNombreMetodoPago());
                            metodoPagoMag.setDescripcionMetodoPago(cobranzaMicrosip.getMetodoPago().getNombreMetodoPago());
                            metodoPagoMag.setTipoPago("ENTRADA");
                            metodoPagoMag.setSincronizacionMicrosip(1);
                            
                            SgfensCobranzaMetodoPagoPk metodoInsert =  new SgfensCobranzaMetodoPagoDaoImpl(conn).insert(metodoPagoMag);
                            
                            
                            QuartzSgfensMetodoPago qMetodo =  new QuartzSgfensMetodoPago();
                            qMetodo.setClave(cobranzaMicrosip.getMetodoPago().getNombreMetodoPago());
                            qMetodo.setIdMetodoPagoEvc(metodoInsert.getIdCobranzaMetodoPago());
                            qMetodo.setIdEmpresa(idEmpresa);
                            qMetodo.setIdSistemaTercero(idSistematercero);
                            
                            new QuartzSgfensMetodoPagoDaoImpl(conn).insert(qMetodo);
                            
                            try{
                                //Buscamos nuevamente el metodo insertado
                                String queryOpcional = "";
                                if (cobranzaMicroSip.getIdCobranzaMetodoPago()>0){
                                    queryOpcional = " ID_METODO_PAGO_SISTEM_TERCERO = " + cobranzaMicroSip.getIdCobranzaMetodoPago();                                                          
                                }else{
                                    if (StringManage.getValidString(cobranzaMicrosip.getMetodoPago().getNombreMetodoPago()).length()>0)
                                        queryOpcional = " CLAVE='" + StringManage.getValidString(cobranzaMicrosip.getMetodoPago().getNombreMetodoPago()) + "'";
                                }
                                qmp = new QuartzSgfensMetodoPagoDaoImpl(conn).findByDynamicWhere(queryOpcional +" AND ID_EMPRESA = " + idEmpresa, null)[0];
                            
                            }catch(Exception e){
                                mensajeCobranza += " Error al relacionar el metodo de pago con ID: "+cobranzaMicroSip.getIdCobranzaMetodoPago()+" (puede que no exista o este sincronizado), relacionado a la cobranza con ID: "+cobranzaMicroSip.getIdCobranzaAbono() + "; ERROR: " + e.getMessage() + " , ";
                            }
                            
                        }
                        
                        
                        cobranzaEvc.setIdentificadorOperacion(cobranzaMicroSip.getIdentificadorOperacion());
                        cobranzaEvc.setComentarios(cobranzaMicroSip.getComentarios());
                        //cobranzaEvc.setIdCobranzaMetodoPago(cobranzaMicroSip.getIdCobranzaMetodoPago());
                        cobranzaEvc.setIdOperacionBancaria(cobranzaMicroSip.getIdOperacionBancaria());
                        cobranzaEvc.setLatitud(cobranzaMicroSip.getLatitud());
                        cobranzaEvc.setLongitud(cobranzaMicroSip.getLongitud());
                        cobranzaEvc.setFolioCobranzaMovil(cobranzaMicroSip.getFolioCobranzaMovil());
                        cobranzaEvc.setSincronizacionMicrosip(cobranzaMicroSip.getSincronizacionMicrosip());
                        //--/-/-/ 
                        cobranzasDaoImpl.update(cobranzaEvc.createPk(), cobranzaEvc);
                        contadorRegistrosActualizados++;
                    }catch(Exception e2){
                         mensajeCobranza += " Error al actualizar cobranza con ID: "+cobranzaMicrosip.getCobranzaAbono().getIdCobranzaAbono() + "; ERROR: " + e2.getMessage() + " , ";
                    }
        
                }
            }
            
            if(mensajeCobranza.trim().equals("")){
                controlBean.setExito(true);
            }
            
            controlBean.setMensajeError(mensajeCobranza);
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
