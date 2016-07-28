/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.microsip.bo;

import com.tsp.microsip.bean.ViaEmbarqueMicrosip;
import com.tsp.microsip.bean.ControlBean;
import com.tsp.sct.bo.ViaEmbarqueBO;
import com.tsp.sct.dao.dto.ViaEmbarque;
import com.tsp.sct.dao.dto.ViaEmbarquePk;
import com.tsp.sct.dao.dto.QuartzViaEmbarque;
import com.tsp.sct.dao.jdbc.ViaEmbarqueDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzViaEmbarqueDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.util.Encrypter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class ViaEmbarqueMicrosipBO {
    
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
            
            filtroBusqueda += " AND ID_ESTATUS = 1 AND ID_VIA_EMBARQUE > 0 ";
            
            ViaEmbarqueBO viaEmbarqueBO = new ViaEmbarqueBO(conn);
            cantidad = viaEmbarqueBO.findCantidadViaEmbarques(-1, idEmpresa, 0, 0, filtroBusqueda);  
        }catch(Exception e){
            e.printStackTrace();
            cantidad =  0;            
        }finally{
            try{ if (conn!=null) conn.close(); }catch(Exception ex){}
        }
        return cantidad;  
    }
    
    public List<ViaEmbarqueMicrosip> getRegistros(int tipo, int idEmpresa){
        List<ViaEmbarqueMicrosip> listaRegistros = new ArrayList<ViaEmbarqueMicrosip>();
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
            filtroBusqueda += " AND ID_ESTATUS = 1 AND ID_VIA_EMBARQUE > 0 ";
            
            ViaEmbarqueBO viaEmbarqueBO = new ViaEmbarqueBO(conn);
            ViaEmbarque[] registrosEncontrados = viaEmbarqueBO.findViaEmbarques(-1, idEmpresa, 0, 0, filtroBusqueda);  
            
            for(ViaEmbarque item : registrosEncontrados ){
                
                ViaEmbarqueMicrosip wsBean = new ViaEmbarqueMicrosip();
                
                if(item.getClave() != null){                    
                    wsBean.setClave(item.getClave());
                }
                
                wsBean.setViaEmbarque(item);
                if(tipo == 2){//si es una actualización, envamos el id o clave del sistema de microsip (identificador)
                    try{
                        QuartzViaEmbarque qc = new QuartzViaEmbarqueDaoImpl(conn).findByDynamicWhere(" ID_VIA_EMBARQUE_EVC = " + item.getIdViaEmbarque()  + " AND ID_EMPRESA = " + idEmpresa+ " ORDER BY ID_QUARTZ DESC ", null)[0]; //buscamos el registro correspondiente
                        if(qc != null){
                            if(qc.getIdViaEmbarqueSistemTercero() > 0){
                                wsBean.setIdMicrosip(qc.getIdViaEmbarqueSistemTercero());
                            }else if(qc.getClave() != null && !qc.getClave().trim().equals("")){
                                //wsBean.setClave(qc.getClave());
                            }
                        }
                    }catch(Exception e){/*e.printStackTrace();*/}                
                }            
                listaRegistros.add(wsBean);
            }
            
        }catch(Exception e){
            e.printStackTrace();           
        }finally{
            try{ if (conn!=null) conn.close(); }catch(Exception ex){}
        }
        return listaRegistros;  
    }
    
    public int setViaEmbarquesIdentificadores(List<QuartzViaEmbarque> quartzDto, int idEmpresa){
        int insertarActualizacion = 0;
        
        Connection conn = null;
        try{
            conn = ResourceManager.getConnection();
            ViaEmbarqueDaoImpl viaEmbarqueDao = new ViaEmbarqueDaoImpl(conn);
            List<QuartzViaEmbarque> quartzIdSincronizacion = new ArrayList<QuartzViaEmbarque>();
            QuartzViaEmbarqueDaoImpl quartzViaEmbarqueDaoImpl = new QuartzViaEmbarqueDaoImpl(conn);
            for(QuartzViaEmbarque quaC : quartzDto){
                //Vemos si ya existe el registro:
                QuartzViaEmbarque quartzViaEmbarque = null;
                if(quaC.getIdViaEmbarqueSistemTercero() > 0){ //verificamos si se buscara por id o por clave
                    try{quartzViaEmbarque = quartzViaEmbarqueDaoImpl.findByDynamicWhere( " ID_VIA_EMBARQUE_EVC = " + quaC.getIdViaEmbarqueEvc() + " AND ID_VIA_EMBARQUE_SISTEM_TERCERO = " + quaC.getIdViaEmbarqueSistemTercero()  + " AND ID_EMPRESA = " + idEmpresa , null)[0];}catch(Exception e){/*e.printStackTrace();*/}
                }else if(quaC.getClave() != null && !quaC.getClave().equals("")){
                    try{quartzViaEmbarque = quartzViaEmbarqueDaoImpl.findByDynamicWhere( " ID_VIA_EMBARQUE_EVC = " + quaC.getIdViaEmbarqueEvc() + " AND CLAVE = " + quaC.getClave()  + " AND ID_EMPRESA = " + idEmpresa , null)[0];}catch(Exception e){/*e.printStackTrace();*/}
                }

                if(quartzViaEmbarque == null){//si no existe lo insertamos
                    quartzViaEmbarque = new QuartzViaEmbarque();
                    quartzViaEmbarque.setIdViaEmbarqueEvc(quaC.getIdViaEmbarqueEvc());
                    if(quaC.getIdViaEmbarqueSistemTercero() > 0){
                        quartzViaEmbarque.setIdViaEmbarqueSistemTercero(quaC.getIdViaEmbarqueSistemTercero());
                    }else{
                        quartzViaEmbarque.setIdViaEmbarqueSistemTercero(0);
                    }
                    if(quaC.getClave() != null && !quaC.getClave().equals("")){
                        quartzViaEmbarque.setClave(quaC.getClave());
                    }else{
                        quartzViaEmbarque.setClave("");
                    }
                    quartzViaEmbarque.setIdEmpresa(idEmpresa);
                    quartzIdSincronizacion.add(quartzViaEmbarque);
                }else{//si existe, borramos e insertamos el registro para que sea el ultimo registro insertado y así al recuperar los ID sea el mas actual.
                    try{quartzViaEmbarqueDaoImpl.delete(quartzViaEmbarque.createPk());
                    QuartzViaEmbarque quartzViaEmbarqueClon = new QuartzViaEmbarque();
                    quartzViaEmbarqueClon.setIdViaEmbarqueEvc(quartzViaEmbarque.getIdViaEmbarqueEvc());
                    quartzViaEmbarqueClon.setIdViaEmbarqueSistemTercero(quartzViaEmbarque.getIdViaEmbarqueSistemTercero());
                    quartzViaEmbarqueClon.setClave(quartzViaEmbarque.getClave());
                    quartzViaEmbarqueClon.setIdSistemaTercero(quartzViaEmbarque.getIdSistemaTercero());
                    quartzViaEmbarqueClon.setIdEmpresa(quartzViaEmbarque.getIdEmpresa());
                    quartzViaEmbarqueDaoImpl.insert(quartzViaEmbarqueClon);
                    }catch(Exception e){System.out.println("------------ Error al actualizar Identificadores de quartz de viaEmbarque: "+e.getMessage());}
                }
                
                //actualizamos la bandera de viaEmbarques a sincronizado:
                try{
                    ViaEmbarque viaEmbarque = new ViaEmbarqueBO(conn).findViaEmbarquebyId(quaC.getIdViaEmbarqueEvc());
                    viaEmbarque.setSincronizacionMicrosip(1);
                    viaEmbarqueDao.update(viaEmbarque.createPk(), viaEmbarque);
                }catch(Exception e){e.printStackTrace();}
                
            }
            insertarActualizacion = 0;
            try{
                QuartzViaEmbarqueDaoImpl qcDaoImp = new QuartzViaEmbarqueDaoImpl(conn);
                for(QuartzViaEmbarque insertDto : quartzIdSincronizacion){//insertamos los registros:
                    qcDaoImp.insert(insertDto);
                }
                insertarActualizacion = 1;
            }catch(Exception e){
                e.printStackTrace();
            }
            
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
            insertarActualizacion = 0;
        }finally{
            try{ if (conn!=null) conn.close(); }catch(Exception ex){}
        }
        
        return insertarActualizacion;
    }
    
    public ControlBean setViaEmbarqueMicrosip(List<ViaEmbarqueMicrosip> viaEmbarquesMicrosipBean, int idEmpresa){
        ControlBean controlBean = new ControlBean();
        String mensajeViaEmbarque = "";
        Connection conn = null;
        try{            
            conn = ResourceManager.getConnection();
            ViaEmbarqueDaoImpl viaEmbarquesDaoImpl = new ViaEmbarqueDaoImpl(conn);
            QuartzViaEmbarqueDaoImpl viaEmbarqueDaoImpl = new QuartzViaEmbarqueDaoImpl(conn);
            QuartzViaEmbarque viaEmbarqueExistente = null;
            
            int contadorRegistrosNuevos = 0; //variable auxiliar para el numero de registros insertados
            int contadorRegistrosActualizados = 0; //variable auxiliar para el numero de registros insertados
            
            Encrypter encripter = new Encrypter();          
            
            for(ViaEmbarqueMicrosip viaEmbarqueMicrosip : viaEmbarquesMicrosipBean){
                viaEmbarqueExistente = null;
                //validamos si el viaEmbarque existe o no en el evc
                try{
                    //viaEmbarqueExistente = viaEmbarqueDaoImpl.findWhereIdViaEmbarqueSistemTerceroEquals(viaEmbarqueMicrosip.getIdViaEmbarqueMicrosip())[0];
                    viaEmbarqueExistente = viaEmbarqueDaoImpl.findByDynamicWhere("ID_VIA_EMBARQUE_SISTEM_TERCERO = " + viaEmbarqueMicrosip.getIdMicrosip() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                    
                }catch(Exception e1){viaEmbarqueExistente = null;}
                if(viaEmbarqueExistente == null){//viaEmbarque nuevo
                    try{
                        ViaEmbarque viaEmbarqueMicroSip = viaEmbarqueMicrosip.getViaEmbarque();
                        //ViaEmbarque ultimoRegistroViaEmbarques = viaEmbarquesDaoImpl.findLast();
                        //int idViaEmbarqueNuevo = ultimoRegistroViaEmbarques.getIdViaEmbarque() + 1;            
                        //viaEmbarqueMicroSip.setIdViaEmbarque(idViaEmbarqueNuevo);
                        
                        viaEmbarqueMicroSip.setIdEstatus(1);
                        viaEmbarqueMicroSip.setSincronizacionMicrosip(1);
                        
                        //--/-/-/
                        ViaEmbarque viaEmbarqueEvc = new ViaEmbarque();
                                              
                        //if(viaEmbarqueMicroSip.getIdEmpresa() > 0)
                        viaEmbarqueEvc.setIdEmpresa(idEmpresa);
                        viaEmbarqueEvc.setIdEstatus(1);
                        viaEmbarqueEvc.setNombre(viaEmbarqueMicroSip.getNombre());
                        viaEmbarqueEvc.setDescripcion(viaEmbarqueMicroSip.getDescripcion());
                        if(viaEmbarqueMicrosip.getClave() != null){
                            viaEmbarqueEvc.setClave(viaEmbarqueMicrosip.getClave());                            
                        }
                        viaEmbarqueEvc.setSincronizacionMicrosip(1);
                        //--/-/-/ 
                        
                        ViaEmbarquePk viaEmbarquePk = viaEmbarquesDaoImpl.insert(viaEmbarqueEvc);

                        ////insertamos la relacion con los Id's
                        QuartzViaEmbarque qc = new QuartzViaEmbarque();
                        qc.setClave( (viaEmbarqueMicrosip.getClave()!=null?viaEmbarqueMicrosip.getClave():null) );
                        qc.setIdViaEmbarqueEvc(viaEmbarquePk.getIdViaEmbarque());
                        qc.setIdViaEmbarqueSistemTercero(viaEmbarqueMicrosip.getIdMicrosip());
                        qc.setIdSistemaTercero(1);
                        qc.setIdEmpresa(idEmpresa);
                        viaEmbarqueDaoImpl.insert(qc);
                        contadorRegistrosNuevos++;
                    }catch(Exception e){
                        mensajeViaEmbarque += " Error al insertar viaEmbarque con ID: "+viaEmbarqueMicrosip.getIdMicrosip() + "; ERROR: " + e.getMessage() + " , ";
                    }  
                }else{//actualizar
                    try{
                        ViaEmbarque viaEmbarqueMicroSip = viaEmbarqueMicrosip.getViaEmbarque();                     
                        viaEmbarqueMicroSip.setIdViaEmbarque(viaEmbarqueExistente.getIdViaEmbarqueEvc());
                        viaEmbarqueMicroSip.setSincronizacionMicrosip(1);
                        viaEmbarqueMicroSip.setIdEstatus(1);
                        //--/-/-/
                        ViaEmbarque viaEmbarqueEvc = new ViaEmbarque();                        
                        viaEmbarqueEvc.setIdViaEmbarque(viaEmbarqueMicroSip.getIdViaEmbarque());                        
                        viaEmbarqueEvc.setIdEmpresa(idEmpresa);
                        viaEmbarqueEvc.setIdEstatus(1);
                        viaEmbarqueEvc.setNombre(viaEmbarqueMicroSip.getNombre());
                        viaEmbarqueEvc.setDescripcion(viaEmbarqueMicroSip.getDescripcion());
                        if(viaEmbarqueMicrosip.getClave() != null){
                            viaEmbarqueEvc.setClave(viaEmbarqueMicrosip.getClave());                            
                        }
                        viaEmbarqueEvc.setSincronizacionMicrosip(1);
                        if(viaEmbarqueMicrosip.getClave() != null){
                            viaEmbarqueEvc.setClave(viaEmbarqueMicrosip.getClave());                            
                        }
                        //--/-/-/ 
                        viaEmbarquesDaoImpl.update(viaEmbarqueEvc.createPk(), viaEmbarqueEvc);
                        contadorRegistrosActualizados++;
                    }catch(Exception e2){
                         mensajeViaEmbarque += " Error al actualizar viaEmbarque con ID: "+viaEmbarqueMicrosip.getIdMicrosip() + "; ERROR: " + e2.getMessage() + " , ";
                    }
        
                }
            }
            
            if(mensajeViaEmbarque.trim().equals("")){
                controlBean.setExito(true);
            }
            controlBean.setMensajeError(mensajeViaEmbarque);
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
