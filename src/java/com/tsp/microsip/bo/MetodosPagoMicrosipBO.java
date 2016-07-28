/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.microsip.bo;

import com.tsp.microsip.bean.SgfensCobranzaMetodoPagoMicrosipBean;
import com.tsp.sct.dao.dto.QuartzSgfensMetodoPago;
import com.tsp.sct.dao.dto.SgfensCobranzaMetodoPago;
import com.tsp.sct.dao.jdbc.QuartzSgfensMetodoPagoDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.dao.jdbc.SgfensCobranzaMetodoPagoDaoImpl;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class MetodosPagoMicrosipBO {
    
    public int obtenerCantidadRegistros(int tipo, int idEmpresa){
        try{
            //validamos si la busqueda es para metodosPagos nuevo o actualizados        
            String filtroBusqueda = "";
            if(tipo == 1){//metodosPagos nuevos
                filtroBusqueda = " SINCRONIZACION_MICROSIP = 0 ";
            }else if(tipo == 2){//metodosPagos actualizados
                filtroBusqueda = " SINCRONIZACION_MICROSIP = 2 ";
            }

            /*if(idEmpresa > 0){
                filtroBusqueda += " AND ID_EMPRESA = " +idEmpresa;
            }
            
            filtroBusqueda += " AND ID_ESTATUS = 1 AND ID_CLIENTE > 0 ";
            */
            Connection conn = ResourceManager.getConnection();
            //MetodosPagoBO cliBO = new MetodosPagoBO(conn);
            int cantidad = new SgfensCobranzaMetodoPagoDaoImpl(conn).findByDynamicWhere(filtroBusqueda, null).length;
            conn.close();
            System.out.println(" ++++++++ metodosPagos a retornar: "+cantidad);
            return cantidad;    
        }catch(Exception e){
            e.printStackTrace();
            return 0;            
        }
    }
    
    public List<SgfensCobranzaMetodoPagoMicrosipBean> obtenerMetodosPago(int tipo, int idEmpresa){
        try{
            //validamos si la busqueda es para sgfensCobranzaMetodoPago nuevo o actualizados        
            String filtroBusqueda = " ";
            if(tipo == 1){//sgfensCobranzaMetodoPago nuevos
                filtroBusqueda += " SINCRONIZACION_MICROSIP = 0 ";
            }else if(tipo == 2){//sgfensCobranzaMetodoPago actualizados
                filtroBusqueda += " SINCRONIZACION_MICROSIP = 2 ";
            }
            
            //filtroBusqueda += " AND ID_ESTATUS = 1 AND ID_CLIENTE > 0 ";
            
            Connection conn = ResourceManager.getConnection();
            SgfensCobranzaMetodoPago[] metodosPago = new SgfensCobranzaMetodoPagoDaoImpl(conn).findByDynamicWhere(filtroBusqueda, null);

            List<SgfensCobranzaMetodoPagoMicrosipBean> metoPagoCompartidos = new ArrayList<SgfensCobranzaMetodoPagoMicrosipBean>();

            for(SgfensCobranzaMetodoPago pago : metodosPago ){
                //--/-/-/
                com.tsp.microsip.bean.SgfensCobranzaMetodoPago metoPagoMicro = new com.tsp.microsip.bean.SgfensCobranzaMetodoPago();
                metoPagoMicro.setIdCobranzaMetodoPago(pago.getIdCobranzaMetodoPago());
                metoPagoMicro.setNombreMetodoPago(pago.getNombreMetodoPago());
                metoPagoMicro.setDescripcionMetodoPago(pago.getDescripcionMetodoPago());
                metoPagoMicro.setSincronizacionMicrosip(pago.getSincronizacionMicrosip());
                
                
                //--/-/-/                
                SgfensCobranzaMetodoPagoMicrosipBean metPagoMiBO = new SgfensCobranzaMetodoPagoMicrosipBean();
                metPagoMiBO.setMetodosPago(metoPagoMicro);
                if(tipo == 2){//si es una actualización, envamos el id o clave del sistema de microsip (identificador)
                    QuartzSgfensMetodoPago qc = null;
                    try{qc = new QuartzSgfensMetodoPagoDaoImpl(conn).findByDynamicWhere(" ID_METODO_PAGO_EVC = " + metoPagoMicro.getIdCobranzaMetodoPago() + " ORDER BY ID_QUARTZ DESC ", null)[0]; //buscamos el registro correspondiente
                        if(qc != null){
                            if(qc.getIdMetodoPagoSistemTercero() > 0){
                                metPagoMiBO.setIdMetodosPagoMicrosip(qc.getIdMetodoPagoSistemTercero());
                            }else if(qc.getClave() != null && !qc.getClave().trim().equals("")){
                                metPagoMiBO.setClave(qc.getClave());
                            }
                        }
                    }catch(Exception e){/*e.printStackTrace();*/}                
                }            
                metoPagoCompartidos.add(metPagoMiBO);
            }        
            conn.close();
            return metoPagoCompartidos; 
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public int setSgfensCobranzaMetodoPagoIdentificadores(List<QuartzSgfensMetodoPago> quartzSgfensMetodoPagos, int idEmpresa){
        try{
            System.out.println("Tamaño de la lista de SgfensCobranzaMetodoPago Identificadores: "+quartzSgfensMetodoPagos.size());
            for(QuartzSgfensMetodoPago qqq : quartzSgfensMetodoPagos){
                try{
                    System.out.println("- Clave: "+qqq.getClave());
                }catch(Exception e2){}
                System.out.println("- IdSgfensCobranzaMetodoPagoEVC: "+qqq.getIdMetodoPagoEvc());
                System.out.println("- getIdSgfensCobranzaMetodoPagoSistemTercero: "+qqq.getIdMetodoPagoSistemTercero());
                System.out.println("- getIdSistemaTercero: "+qqq.getIdSistemaTercero());                
            }        
        }catch(Exception e){e.printStackTrace();}
        
        
        try{
            Connection conn = ResourceManager.getConnection();
            SgfensCobranzaMetodoPagoDaoImpl sgfensCobranzaMetodoPagoDaoImpl = new SgfensCobranzaMetodoPagoDaoImpl(conn);
            List<QuartzSgfensMetodoPago> sgfensCobranzaMetodoPagoIdSincronizacion = new ArrayList<QuartzSgfensMetodoPago>();
            QuartzSgfensMetodoPagoDaoImpl quartzSgfensMetodoPagoDaoImpl = new QuartzSgfensMetodoPagoDaoImpl(conn);
            for(QuartzSgfensMetodoPago quaC : quartzSgfensMetodoPagos){
                //Vemos si ya existe el registro:
                QuartzSgfensMetodoPago quartzSgfensMetodoPago = null;
                if(quaC.getIdMetodoPagoSistemTercero() > 0){ //verificamos si se buscara por id o por clave
                    System.out.println("** Es mayor a cero el dato  getIdMetodoPagoSistemTercero");
                    try{quartzSgfensMetodoPago = quartzSgfensMetodoPagoDaoImpl.findByDynamicWhere( " ID_METODO_PAGO_EVC = " + quaC.getIdMetodoPagoEvc() + " AND ID_METODO_PAGO_SISTEM_TERCERO = " + quaC.getIdMetodoPagoSistemTercero() + " AND ID_EMPRESA = " + idEmpresa , null)[0];}catch(Exception e){/*e.printStackTrace();*/}
                }else if(quaC.getClave() != null && !quaC.getClave().equals("")){
                    System.out.println("** entro a este else if -----");
                    try{quartzSgfensMetodoPago = quartzSgfensMetodoPagoDaoImpl.findByDynamicWhere( " ID_METODO_PAGO_EVC = " + quaC.getIdMetodoPagoEvc() + " AND CLAVE = " + quaC.getClave() + " AND ID_EMPRESA = " + idEmpresa, null)[0];}catch(Exception e){/*e.printStackTrace();*/}
                }

                if(quartzSgfensMetodoPago == null){//si no existe lo insertamos
                    System.out.println("--- se crea el registro para insertarlo");
                    quartzSgfensMetodoPago = new QuartzSgfensMetodoPago();
                    quartzSgfensMetodoPago.setIdMetodoPagoEvc(quaC.getIdMetodoPagoEvc());
                    if(quaC.getIdMetodoPagoSistemTercero() > 0){
                        quartzSgfensMetodoPago.setIdMetodoPagoSistemTercero(quaC.getIdMetodoPagoSistemTercero());
                    }else{
                        quartzSgfensMetodoPago.setIdMetodoPagoSistemTercero(0);
                    }
                    if(quaC.getClave() != null && !quaC.getClave().equals("")){
                        quartzSgfensMetodoPago.setClave(quaC.getClave());
                    }else{
                        quartzSgfensMetodoPago.setClave("");
                    }
                    quartzSgfensMetodoPago.setIdEmpresa(idEmpresa);
                    //quartzSgfensMetodoPago.setIdEmpresa(idEmpresa);
                    sgfensCobranzaMetodoPagoIdSincronizacion.add(quartzSgfensMetodoPago);
                }else{//si existe, borramos e insertamos el registro para que sea el ultimo registro insertado y así al recuperar los ID sea el mas actual.
                    try{quartzSgfensMetodoPagoDaoImpl.delete(quartzSgfensMetodoPago.createPk());
                    QuartzSgfensMetodoPago quartzMetodoPagoClon = new QuartzSgfensMetodoPago();
                    quartzMetodoPagoClon.setIdMetodoPagoEvc(quartzSgfensMetodoPago.getIdMetodoPagoEvc());
                    quartzMetodoPagoClon.setIdMetodoPagoSistemTercero(quartzSgfensMetodoPago.getIdMetodoPagoSistemTercero());
                    quartzMetodoPagoClon.setClave(quartzSgfensMetodoPago.getClave());
                    quartzMetodoPagoClon.setIdSistemaTercero(quartzSgfensMetodoPago.getIdSistemaTercero());
                    quartzMetodoPagoClon.setIdEmpresa(idEmpresa);
                    //quartzMetodoPagoClon.setIdEmpresa(quartzSgfensMetodoPago.getIdEmpresa());                    
                    quartzSgfensMetodoPagoDaoImpl.insert(quartzMetodoPagoClon);
                    }catch(Exception e){System.out.println("------------ Error al actualizar Identificadores de quartz de almacen: "+e.getMessage());}
                }
                
                //actualizamos la bandera de sgfensCobranzaMetodoPago a sincronizado:
                try{
                    SgfensCobranzaMetodoPago sgfensCobranzaMetodoPago = new SgfensCobranzaMetodoPagoDaoImpl(conn).findByPrimaryKey(quaC.getIdMetodoPagoEvc());
                    sgfensCobranzaMetodoPago.setSincronizacionMicrosip(1);
                    sgfensCobranzaMetodoPagoDaoImpl.update(sgfensCobranzaMetodoPago.createPk(), sgfensCobranzaMetodoPago);
                    System.out.println("+++ actualizando bandera de sincronizacion . . . ");
                }catch(Exception e){e.printStackTrace();}
                
            }
            int insetarActualizacion = 0;
            try{
                QuartzSgfensMetodoPagoDaoImpl qcDaoImp = new QuartzSgfensMetodoPagoDaoImpl(conn);
                System.out.println(" //// INSERTANDO REGISTROS . . . ");
                for(QuartzSgfensMetodoPago insetCli : sgfensCobranzaMetodoPagoIdSincronizacion){//insertamos los registros:
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
    
}
