/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.microsip.bo;

import com.tsp.microsip.bean.ControlBean;
import com.tsp.microsip.bean.ImpuestosMicrosipBean;
import com.tsp.sct.bo.ImpuestoBO;
import com.tsp.sct.dao.dto.Impuesto;
import com.tsp.sct.dao.dto.ImpuestoPk;
import com.tsp.sct.dao.dto.QuartzImpuesto;
import com.tsp.sct.dao.jdbc.ImpuestoDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzImpuestoDaoImpl;
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
public class ImpuestosMicrosipBO {
    
    public int obtenerCantidadRegistros(int tipo, int idEmpresa){
                
        try{
            //validamos si la busqueda es para impuestos nuevos o actualizados        
            String filtroBusqueda = " ID_IMPUESTO > 0 ";
            if(tipo == 1){//impuestos nuevos
                filtroBusqueda += " AND SINCRONIZACION_MICROSIP = 0 ";
            }else if(tipo == 2){//impuestos actualizados
                filtroBusqueda += " AND SINCRONIZACION_MICROSIP = 2 ";
            }

            if(idEmpresa > 0){
                filtroBusqueda += " AND ID_EMPRESA = " +idEmpresa;
            }
            Connection conn = ResourceManager.getConnection();
            ImpuestoBO impBO = new ImpuestoBO(conn);
            int cantidad = impBO.getCantidadByImpuesto(filtroBusqueda);
            conn.close();
            return cantidad;    
        }catch(Exception e){
            e.printStackTrace();
            return 0;            
        }
    }
    
    public List<ImpuestosMicrosipBean> obtenerImpuestos(int tipo, int idEmpresa){
        try{
            //validamos si la busqueda es para impuestos nuevo o actualizados        
            String filtroBusqueda = " AND ";
            if(tipo == 1){//impuestos nuevos
                filtroBusqueda += " SINCRONIZACION_MICROSIP = 0 ";
            }else if(tipo == 2){//impuestos actualizados
                filtroBusqueda += " SINCRONIZACION_MICROSIP = 2 ";
            }
            Connection conn = ResourceManager.getConnection();
            ImpuestoBO impBO = new ImpuestoBO(conn);
            Impuesto[] impuesto = impBO.findImpuestos(0, idEmpresa, 0, 0, filtroBusqueda);

            List<ImpuestosMicrosipBean> impuestosCompartidos = new ArrayList<ImpuestosMicrosipBean>();

            for(Impuesto imp : impuesto ){
                
                //--/-/-/
                com.tsp.microsip.bean.Impuesto impuestoMicro = new com.tsp.microsip.bean.Impuesto();
                impuestoMicro.setIdImpuesto(imp.getIdImpuesto());
                impuestoMicro.setIdEmpresa(imp.getIdEmpresa());
                impuestoMicro.setNombre(imp.getNombre());
                impuestoMicro.setDescripcion(imp.getDescripcion());
                impuestoMicro.setPorcentaje(imp.getPorcentaje());
                impuestoMicro.setTrasladado(imp.getTrasladado());
                impuestoMicro.setIdEstatus(imp.getIdEstatus());
                impuestoMicro.setImpuestoLocal(imp.getImpuestoLocal());
                impuestoMicro.setSincronizacionMicrosip(imp.getSincronizacionMicrosip());
                //--/-/-/
                
                ImpuestosMicrosipBean impMiBO = new ImpuestosMicrosipBean();
                impMiBO.setImpuesto(impuestoMicro);
                if(tipo == 2){//si es una actualización, enviamos el id o clave del sistema de microsip (identificador)
                    QuartzImpuesto qi = null;
                    try{qi = new QuartzImpuestoDaoImpl(conn).findByDynamicWhere(" ID_IMPUESTO_EVC = " + imp.getIdImpuesto() + " AND ID_EMPRESA = " + idEmpresa+ " ORDER BY ID_QUARTZ DESC ", null)[0]; //buscamos el registro correspondiente
                        if(qi != null){
                            if(qi.getIdImpuestoSistemTercero() > 0){
                                impMiBO.setIdImpuestoMicrosip(qi.getIdImpuestoSistemTercero());
                            }else if(qi.getClave() != null && !qi.getClave().trim().equals("")){
                                impMiBO.setClave(qi.getClave());
                            }
                        }
                    }catch(Exception e){/*e.printStackTrace();*/}                
                }            
                impuestosCompartidos.add(impMiBO);
            }        
            conn.close();
            return impuestosCompartidos; 
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public int setImpuestosIdentificadores(List<QuartzImpuesto> quartzImpuestos, int idEmpresa){
        try{
            Connection conn = ResourceManager.getConnection();
            ImpuestoDaoImpl impuestoDaoImpl = new ImpuestoDaoImpl(conn);
            List<QuartzImpuesto> impuestosIdSincronizacion = new ArrayList<QuartzImpuesto>();
            QuartzImpuestoDaoImpl quartzImpuestoDaoImpl = new QuartzImpuestoDaoImpl(conn);
            for(QuartzImpuesto quaI : quartzImpuestos){
                //Vemos si ya existe el registro:
                QuartzImpuesto quartzImpuesto = null;
                if(quaI.getIdImpuestoSistemTercero() > 0){ //verificamos si se buscara por id o por clave
                    try{quartzImpuesto = quartzImpuestoDaoImpl.findByDynamicWhere( " ID_IMPUESTO_EVC = " + quaI.getIdImpuestoEvc() + " AND ID_IMPUESTO_SISTEM_TERCERO = " + quaI.getIdImpuestoSistemTercero() + " AND ID_EMPRESA = " + idEmpresa , null)[0];}catch(Exception e){/*e.printStackTrace();*/}
                }else if(quaI.getClave() != null && !quaI.getClave().equals("")){
                    try{quartzImpuesto = quartzImpuestoDaoImpl.findByDynamicWhere( " ID_IMPUESTO_EVC = " + quaI.getIdImpuestoEvc() + " AND CLAVE = " + quaI.getClave() + " AND ID_EMPRESA = " + idEmpresa , null)[0];}catch(Exception e){/*e.printStackTrace();*/}
                }

                if(quartzImpuesto == null){//si no existe lo insertamos
                    quartzImpuesto = new QuartzImpuesto();
                    quartzImpuesto.setIdImpuestoEvc(quaI.getIdImpuestoEvc());
                    if(quaI.getIdImpuestoSistemTercero() > 0){
                        quartzImpuesto.setIdImpuestoSistemTercero(quaI.getIdImpuestoSistemTercero());
                    }else{
                        quartzImpuesto.setIdImpuestoSistemTercero(0);
                    }
                    if(quaI.getClave() != null && !quaI.getClave().equals("")){
                        quartzImpuesto.setClave(quaI.getClave());
                    }else{
                        quartzImpuesto.setClave("");
                    }
                    quartzImpuesto.setIdEmpresa(idEmpresa);
                    impuestosIdSincronizacion.add(quartzImpuesto);
                }else{//si existe, borramos e insertamos el registro para que sea el ultimo registro insertado y así al recuperar los ID sea el mas actual.
                    try{quartzImpuestoDaoImpl.delete(quartzImpuesto.createPk());
                    QuartzImpuesto quartzImpuestoClon = new QuartzImpuesto();
                    quartzImpuestoClon.setIdImpuestoEvc(quartzImpuesto.getIdImpuestoEvc());
                    quartzImpuestoClon.setIdImpuestoSistemTercero(quartzImpuesto.getIdImpuestoSistemTercero());
                    quartzImpuestoClon.setClave(quartzImpuesto.getClave());
                    quartzImpuestoClon.setIdSistemaTercero(quartzImpuesto.getIdSistemaTercero());
                    quartzImpuestoClon.setIdEmpresa(quartzImpuesto.getIdEmpresa());                    
                    quartzImpuestoDaoImpl.insert(quartzImpuestoClon);
                    }catch(Exception e){System.out.println("------------ Error al actualizar Identificadores de quartz de impuesto: "+e.getMessage());}
                }
                
                //actualizamos la bandera de impuestos a sincronizado:
                try{
                    Impuesto impuesto = new ImpuestoBO(conn).findImpuestobyId(quaI.getIdImpuestoEvc());
                    impuesto.setSincronizacionMicrosip(1);
                    impuestoDaoImpl.update(impuesto.createPk(), impuesto);
                }catch(Exception e){e.printStackTrace();}
                
            }
            int insetarActualizacion = 0;
            try{
                QuartzImpuestoDaoImpl qiDaoImp = new QuartzImpuestoDaoImpl(conn);
                for(QuartzImpuesto insetImp : impuestosIdSincronizacion){//insertamos los registros:
                    qiDaoImp.insert(insetImp);
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
    
    public ControlBean setImpuestoMicrosip(List<ImpuestosMicrosipBean> impuestosMicrosipBean, int idEmpresa, String acceso){
        ControlBean controlBean = new ControlBean();
        String mensajeImpuesto = "";
        Connection conn = null;
        try{            
            conn = ResourceManager.getConnection();
            ImpuestoDaoImpl impuestosDaoImpl = new ImpuestoDaoImpl(conn);
            QuartzImpuestoDaoImpl impuestoDaoImpl = new QuartzImpuestoDaoImpl(conn);
            QuartzImpuesto impuestoExistente = null;
            
            int contadorRegistrosNuevos = 0; //variable auxiliar para el numero de registros insertados
            int contadorRegistrosActualizados = 0; //variable auxiliar para el numero de registros insertados
            
            
            int idSistematercero  = 0; //Se obtiene del token acceso 1er token
           
            try{                
                
                StringTokenizer st = new StringTokenizer(acceso, "|");
                if(st.hasMoreElements()){
                    idSistematercero = Integer.parseInt(st.nextElement().toString().trim());                
                }
            }catch(Exception e){
                System.out.println("Error al obtener IdSitemaTercero desde Token");
                
            }
            
            
            for(ImpuestosMicrosipBean impuestoMicrosip : impuestosMicrosipBean){
                //validamos si el impuesto existe o no en el evc
                try{
                    //impuestoExistente = impuestoDaoImpl.findByDynamicWhere("ID_IMPUESTO_SISTEM_TERCERO = " + impuestoMicrosip.getIdImpuestoMicrosip() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                    String queryOpcional = "";
                    if (impuestoMicrosip.getIdImpuestoMicrosip()>0){
                        queryOpcional = " ID_IMPUESTO_SISTEM_TERCERO = " + impuestoMicrosip.getIdImpuestoMicrosip();
                    }else{
                        if (StringManage.getValidString(impuestoMicrosip.getClave()).length()>0)
                            queryOpcional = " CLAVE='" + StringManage.getValidString(impuestoMicrosip.getClave()) + "'";
                    }
                    impuestoExistente = impuestoDaoImpl.findByDynamicWhere(queryOpcional +" AND ID_EMPRESA = " + idEmpresa, null)[0];
                }catch(Exception e1){impuestoExistente = null;}
                if(impuestoExistente == null){//impuesto nuevo
                    try{
                        com.tsp.microsip.bean.Impuesto impuestoMicroSip = impuestoMicrosip.getImpuesto();
                        Impuesto ultimoRegistroImpuestos = impuestosDaoImpl.findLast();
                        int idImpuestoNuevo = ultimoRegistroImpuestos.getIdImpuesto() + 1;            
                        impuestoMicroSip.setIdImpuesto(idImpuestoNuevo);
                        
                        impuestoMicroSip.setIdEstatus(1);
                        impuestoMicroSip.setSincronizacionMicrosip(1);
                        
                        //--/-/-/
                        Impuesto impuestoEvc = new Impuesto();
                        if(impuestoMicroSip.getIdImpuesto() > 0)
                            impuestoEvc.setIdImpuesto(impuestoMicroSip.getIdImpuesto());                        
                        //if(impuestoMicroSip.getIdEmpresa() > 0)
                        impuestoEvc.setIdEmpresa(idEmpresa);                        
                        if(impuestoMicroSip.getNombre() != null)
                            impuestoEvc.setNombre(impuestoMicroSip.getNombre());
                        else
                            impuestoEvc.setNombre("");
                        if(impuestoMicroSip.getDescripcion() != null)
                            impuestoEvc.setDescripcion(impuestoMicroSip.getDescripcion());
                        else
                            impuestoEvc.setDescripcion("");
                        impuestoEvc.setPorcentaje(impuestoMicroSip.getPorcentaje());
                        impuestoEvc.setTrasladado(impuestoMicroSip.getTrasladado());
                        impuestoEvc.setIdEstatus(impuestoMicroSip.getIdEstatus());
                        impuestoEvc.setImpuestoLocal(impuestoMicroSip.getImpuestoLocal());
                        impuestoEvc.setSincronizacionMicrosip(impuestoMicroSip.getSincronizacionMicrosip());
                        
                        //--/-/-/ 
                        
                        ImpuestoPk impuestoPk = impuestosDaoImpl.insert(impuestoEvc);

                        ////insertamos la relacion con los Id's
                        QuartzImpuesto qc = new QuartzImpuesto();
                        qc.setClave( (impuestoMicrosip.getClave()!=null?impuestoMicrosip.getClave():null) );
                        qc.setIdImpuestoEvc(impuestoPk.getIdImpuesto());
                        qc.setIdImpuestoSistemTercero(impuestoMicrosip.getIdImpuestoMicrosip());
                        qc.setIdSistemaTercero(idSistematercero);
                        qc.setIdEmpresa(idEmpresa);
                        impuestoDaoImpl.insert(qc);
                        contadorRegistrosNuevos++;
                    }catch(Exception e){
                        mensajeImpuesto += " Error al insertar impuesto con ID: "+impuestoMicrosip.getIdImpuestoMicrosip() + "; ERROR: " + e.getMessage() + " , ";
                    }  
                }else{//actualizar
                    try{
                        com.tsp.microsip.bean.Impuesto impuestoMicroSip = impuestoMicrosip.getImpuesto();                     
                        impuestoMicroSip.setIdImpuesto(impuestoExistente.getIdImpuestoEvc());
                        impuestoMicroSip.setSincronizacionMicrosip(1);
                        impuestoMicroSip.setIdEstatus(1);
                        //--/-/-/
                        Impuesto impuestoEvc = new Impuesto();
                        if(impuestoMicroSip.getIdImpuesto() > 0)
                            impuestoEvc.setIdImpuesto(impuestoMicroSip.getIdImpuesto());                        
                        impuestoEvc.setIdEmpresa(idEmpresa);
                        if(impuestoMicroSip.getNombre() != null)
                            impuestoEvc.setNombre(impuestoMicroSip.getNombre());
                        else
                            impuestoEvc.setNombre("");
                        if(impuestoMicroSip.getDescripcion() != null)
                            impuestoEvc.setDescripcion(impuestoMicroSip.getDescripcion());
                        else
                            impuestoEvc.setDescripcion("");
                        impuestoEvc.setPorcentaje(impuestoMicroSip.getPorcentaje());
                        impuestoEvc.setTrasladado(impuestoMicroSip.getTrasladado());
                        impuestoEvc.setIdEstatus(impuestoMicroSip.getIdEstatus());
                        impuestoEvc.setImpuestoLocal(impuestoMicroSip.getImpuestoLocal());
                        impuestoEvc.setSincronizacionMicrosip(impuestoMicroSip.getSincronizacionMicrosip());
                        //--/-/-/ 
                        impuestosDaoImpl.update(impuestoEvc.createPk(), impuestoEvc);
                        contadorRegistrosActualizados++;
                    }catch(Exception e2){
                         mensajeImpuesto += " Error al actualizar impuesto con ID: "+impuestoMicrosip.getIdImpuestoMicrosip() + "; ERROR: " + e2.getMessage() + " , ";
                    }
        
                }
            }
            
            if(mensajeImpuesto.trim().equals("")){
                controlBean.setExito(true);
            }
            controlBean.setMensajeError(mensajeImpuesto);
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
