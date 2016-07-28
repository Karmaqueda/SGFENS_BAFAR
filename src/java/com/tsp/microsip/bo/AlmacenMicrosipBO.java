/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.microsip.bo;

import com.tsp.microsip.bean.AlmacenMicrosip;
import com.tsp.microsip.bean.ControlBean;
import com.tsp.sct.bo.AlmacenBO;
import com.tsp.sct.dao.dto.Almacen;
import com.tsp.sct.dao.dto.AlmacenPk;
import com.tsp.sct.dao.dto.ExistenciaAlmacen;
import com.tsp.sct.dao.dto.QuartzAlmacen;
import com.tsp.sct.dao.dto.QuartzConcepto;
import com.tsp.sct.dao.jdbc.AlmacenDaoImpl;
import com.tsp.sct.dao.jdbc.ExistenciaAlmacenDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzAlmacenDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzConceptoDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author ISCesar poseidon24@hotmail.com
 * @since 4/05/2015 4/05/2015 11:53:31 AM
 */
public class AlmacenMicrosipBO {

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
            
            filtroBusqueda += " AND ID_ESTATUS = 1 AND ID_ALMACEN > 0 ";
            
            AlmacenBO almacenBO = new AlmacenBO(conn);
            cantidad = almacenBO.findCantidadAlmacens(-1, idEmpresa, 0, 0, filtroBusqueda);  
        }catch(Exception e){
            e.printStackTrace();
            cantidad =  0;            
        }finally{
            try{ if (conn!=null) conn.close(); }catch(Exception ex){}
        }
        return cantidad;  
    }
    
    public List<AlmacenMicrosip> getRegistros(int tipo, int idEmpresa){
        List<AlmacenMicrosip> listaRegistros = new ArrayList<AlmacenMicrosip>();
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
            
            filtroBusqueda += " AND ID_ESTATUS = 1 AND ID_ALMACEN > 0 ";
            
            AlmacenBO almacenBO = new AlmacenBO(conn);
            Almacen[] registrosEncontrados = almacenBO.findAlmacens(-1, idEmpresa, 0, 0, filtroBusqueda);
            ExistenciaAlmacenDaoImpl existenciaAlmacenDaoImpl = new ExistenciaAlmacenDaoImpl(conn);
            QuartzConceptoDaoImpl quartzConceptoDaoImpl = new QuartzConceptoDaoImpl(conn);
            QuartzAlmacenDaoImpl quartzAlmacenDaoImpl = new QuartzAlmacenDaoImpl(conn);
            int idAlmacen = -1;
            for(Almacen item : registrosEncontrados ){
                idAlmacen = -1;                
                //--/-/-/
                com.tsp.microsip.bean.Almacen almacenMicro = new com.tsp.microsip.bean.Almacen();
                almacenMicro.setIdAlmacen(item.getIdAlmacen());
                almacenMicro.setIdEmpresa(item.getIdEmpresa());
                almacenMicro.setIdEstatus(item.getIdEstatus());
                almacenMicro.setNombre(item.getNombre());
                almacenMicro.setDireccion(item.getDireccion());
                almacenMicro.setAreaAlmacen(item.getAreaAlmacen());
                almacenMicro.setResponsable(item.getResponsable());
                almacenMicro.setPuesto(item.getPuesto());
                almacenMicro.setTelefono(item.getTelefono());
                almacenMicro.setCorreo(item.getCorreo());
                almacenMicro.setSincronizacionMicrosip(item.getSincronizacionMicrosip());                
                //--/-/-/
                
                AlmacenMicrosip wsBean = new AlmacenMicrosip();
                wsBean.setAlmacen(almacenMicro);
                if(tipo == 2){//si es una actualización, envamos el id o clave del sistema de microsip (identificador)
                    try{
                        QuartzAlmacen qc = quartzAlmacenDaoImpl.findByDynamicWhere(" ID_ALMACEN_EVC = " + item.getIdAlmacen() + " AND ID_EMPRESA = " + idEmpresa + " ORDER BY ID_QUARTZ DESC ", null)[0]; //buscamos el registro correspondiente
                        if(qc != null){
                            if(qc.getIdAlmacenSistemTercero() > 0){
                                wsBean.setIdMicrosip(qc.getIdAlmacenSistemTercero());
                                idAlmacen = qc.getIdAlmacenSistemTercero();
                            }else if(qc.getClave() != null && !qc.getClave().trim().equals("")){
                                wsBean.setClave(qc.getClave());
                            }
                        }else{
                            wsBean.setIdMicrosip(-1);
                            idAlmacen = -1;
                        }
                    }catch(Exception e){wsBean.setIdMicrosip(-1); e.printStackTrace();}                
                } 
                
                /////-- Lista de relacion de almacen y productos:
                ExistenciaAlmacen[] existencias = null;
                try{
                    existencias = existenciaAlmacenDaoImpl.findByDynamicWhere(" ID_ALMACEN = " + item.getIdAlmacen() + " AND ESTATUS = 1 ", null);
                    if(existencias != null){
                        for(ExistenciaAlmacen relacion : existencias){
                            //verificamos los Id del sistema tercero para enviar esos:
                                            
                            try{
                                QuartzConcepto qc = quartzConceptoDaoImpl.findByDynamicWhere(" ID_CONCEPTO_EVC = " + relacion.getIdConcepto() + " ORDER BY ID_QUARTZ DESC ", null)[0]; //buscamos el registro correspondiente
                                if(qc != null){
                                    if(qc.getIdConceptoSistemTercero() > 0){
                                        relacion.setIdConcepto(qc.getIdConceptoSistemTercero());
                                    }else if(qc.getClave() != null && !qc.getClave().trim().equals("")){
                                        //wsBean.setClave(qc.getClave());
                                    }
                                }else{
                                    relacion.setIdConcepto(-1);
                                }
                            }catch(Exception e){
                            /*e.printStackTrace();*/
                                relacion.setIdConcepto(-1);
                            }
                            if(tipo == 2){
                                relacion.setIdAlmacen(idAlmacen);
                            }
                            wsBean.getListRelacProdAlmacen().add(relacion);
                        }
                    }
                }catch(Exception e){}
                /////--
                
                listaRegistros.add(wsBean);
            }
            
        }catch(Exception e){
            e.printStackTrace();           
        }finally{
            try{ if (conn!=null) conn.close(); }catch(Exception ex){}
        }
        return listaRegistros;  
    }
    
    public int setIdentificadores(List<QuartzAlmacen> quartzDto, int idEmpresa){
        int insertarActualizacion = 0;
        
        Connection conn = null;
        try{
            conn = ResourceManager.getConnection();
            AlmacenDaoImpl almacenDao = new AlmacenDaoImpl(conn);
            List<QuartzAlmacen> quartzIdSincronizacion = new ArrayList<QuartzAlmacen>();
            QuartzAlmacenDaoImpl quartzAlmacenDaoImpl = new QuartzAlmacenDaoImpl(conn);
            for(QuartzAlmacen quaC : quartzDto){
                //Vemos si ya existe el registro:
                QuartzAlmacen quartzAlmacen = null;
                if(quaC.getIdAlmacenSistemTercero() > 0){ //verificamos si se buscara por id o por clave
                    try{quartzAlmacen = quartzAlmacenDaoImpl.findByDynamicWhere( " ID_ALMACEN_EVC = " + quaC.getIdAlmacenEvc() + " AND ID_ALMACEN_SISTEM_TERCERO = " + quaC.getIdAlmacenSistemTercero()  + " AND ID_EMPRESA = " + idEmpresa , null)[0];}catch(Exception e){/*e.printStackTrace();*/}
                }else if(quaC.getClave() != null && !quaC.getClave().equals("")){
                    try{quartzAlmacen = quartzAlmacenDaoImpl.findByDynamicWhere( " ID_ALMACEN_EVC = " + quaC.getIdAlmacenEvc() + " AND CLAVE = " + quaC.getClave()  + " AND ID_EMPRESA = " + idEmpresa , null)[0];}catch(Exception e){/*e.printStackTrace();*/}
                }

                if(quartzAlmacen == null){//si no existe lo insertamos
                    quartzAlmacen = new QuartzAlmacen();
                    quartzAlmacen.setIdAlmacenEvc(quaC.getIdAlmacenEvc());
                    if(quaC.getIdAlmacenSistemTercero() > 0){
                        quartzAlmacen.setIdAlmacenSistemTercero(quaC.getIdAlmacenSistemTercero());
                    }else{
                        quartzAlmacen.setIdAlmacenSistemTercero(0);
                    }
                    if(quaC.getClave() != null && !quaC.getClave().equals("")){
                        quartzAlmacen.setClave(quaC.getClave());
                    }else{
                        quartzAlmacen.setClave("");
                    }
                    quartzAlmacen.setIdEmpresa(idEmpresa);
                    quartzIdSincronizacion.add(quartzAlmacen);
                }else{//si existe, borramos e insertamos el registro para que sea el ultimo registro insertado y así al recuperar los ID sea el mas actual.
                    try{quartzAlmacenDaoImpl.delete(quartzAlmacen.createPk());
                    QuartzAlmacen quartzAlmacenClon = new QuartzAlmacen();
                    quartzAlmacenClon.setIdAlmacenEvc(quartzAlmacen.getIdAlmacenEvc());
                    quartzAlmacenClon.setIdAlmacenSistemTercero(quartzAlmacen.getIdAlmacenSistemTercero());
                    quartzAlmacenClon.setClave(quartzAlmacen.getClave());
                    quartzAlmacenClon.setIdSistemaTercero(quartzAlmacen.getIdSistemaTercero());
                    quartzAlmacenClon.setIdEmpresa(quartzAlmacen.getIdEmpresa());
                    quartzAlmacenDaoImpl.insert(quartzAlmacenClon);
                    }catch(Exception e){System.out.println("------------ Error al actualizar Identificadores de quartz de almacen: "+e.getMessage());}
                }
                
                //actualizamos la bandera de almacens a sincronizado:
                try{
                    Almacen almacen = new AlmacenBO(quaC.getIdAlmacenEvc(), conn).getAlmacen();
                    almacen.setSincronizacionMicrosip(1);
                    almacenDao.update(almacen.createPk(), almacen);
                }catch(Exception e){e.printStackTrace();}
                
            }
            insertarActualizacion = 0;
            try{
                QuartzAlmacenDaoImpl qcDaoImp = new QuartzAlmacenDaoImpl(conn);
                for(QuartzAlmacen insertDto : quartzIdSincronizacion){//insertamos los registros:
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
    
    public ControlBean setAlmacenMicrosip(List<AlmacenMicrosip> almacensMicrosipBean, int idEmpresa , String acceso){
        ControlBean controlBean = new ControlBean();
        String mensajeAlmacen = "";
        Connection conn = null;
        try{            
            conn = ResourceManager.getConnection();
            AlmacenDaoImpl almacensDaoImpl = new AlmacenDaoImpl(conn);
            QuartzAlmacenDaoImpl almacenDaoImpl = new QuartzAlmacenDaoImpl(conn);
            QuartzAlmacen almacenExistente = null;
            
            int contadorRegistrosNuevos = 0; //variable auxiliar para el numero de registros insertados
            int contadorRegistrosActualizados = 0; //variable auxiliar para el numero de registros insertados
            int idAlmacenRelacionado = 0; 
            ExistenciaAlmacenDaoImpl existenciaAlmacenDaoImpl = new ExistenciaAlmacenDaoImpl(conn);
            
            int idSistematercero  = 0; //Se obtiene del token acceso 1er token
           
            try{                
                
                StringTokenizer st = new StringTokenizer(acceso, "|");
                if(st.hasMoreElements()){
                    idSistematercero = Integer.parseInt(st.nextElement().toString().trim());                
                }
            }catch(Exception e){
                System.out.println("Error al obtener IdSitemaTercero desde Token");
                
            }
            
            for(AlmacenMicrosip almacenMicrosip : almacensMicrosipBean){
                //validamos si el almacen existe o no en el evc
                try{
                    //almacenExistente = almacenDaoImpl.findByDynamicWhere("ID_ALMACEN_SISTEM_TERCERO = " + almacenMicrosip.getIdMicrosip() + " AND ID_EMPRESA = " + idEmpresa+ " ORDER BY ID_QUARTZ DESC ", null)[0];
                    String queryOpcional = "";
                    if (almacenMicrosip.getIdMicrosip()>0){
                        queryOpcional = " ID_ALMACEN_SISTEM_TERCERO = " + almacenMicrosip.getIdMicrosip();
                    }else{
                        if (StringManage.getValidString(almacenMicrosip.getClave()).length()>0)
                            queryOpcional = " CLAVE='" + StringManage.getValidString(almacenMicrosip.getClave()) + "'";
                    }
                    almacenExistente = almacenDaoImpl.findByDynamicWhere(queryOpcional +" AND ID_EMPRESA = " + idEmpresa, null)[0];
                }catch(Exception e1){almacenExistente = null;}
                if(almacenExistente == null){//almacen nuevo
                    try{
                        com.tsp.microsip.bean.Almacen almacenMicroSip = almacenMicrosip.getAlmacen();
                        //Almacen ultimoRegistroAlmacens = almacensDaoImpl.findLast();
                        //int idAlmacenNuevo = ultimoRegistroAlmacens.getIdAlmacen() + 1;            
                        //almacenMicroSip.setIdAlmacen(idAlmacenNuevo);
                        
                        almacenMicroSip.setIdEstatus(1);
                        almacenMicroSip.setSincronizacionMicrosip(1);
                        
                        //--/-/-/
                        Almacen almacenEvc = new Almacen();
                        //if(almacenMicroSip.getIdAlmacen() > 0)
                        //    almacenEvc.setIdAlmacen(almacenMicroSip.getIdAlmacen());                        
                        //if(almacenMicroSip.getIdEmpresa() > 0)
                        almacenEvc.setIdEmpresa(idEmpresa);                                               
                        almacenEvc.setIdEstatus(almacenMicroSip.getIdEstatus());
                        almacenEvc.setNombre(almacenMicroSip.getNombre());
                        almacenEvc.setDireccion(almacenMicroSip.getDireccion());
                        almacenEvc.setAreaAlmacen(almacenMicroSip.getAreaAlmacen());
                        almacenEvc.setResponsable(almacenMicroSip.getResponsable());
                        almacenEvc.setPuesto(almacenMicroSip.getPuesto());
                        almacenEvc.setTelefono(almacenMicroSip.getTelefono());
                        almacenEvc.setCorreo(almacenMicroSip.getCorreo());
                        almacenEvc.setIdEstatus(almacenMicroSip.getIdEstatus());
                        almacenEvc.setSincronizacionMicrosip(almacenMicroSip.getSincronizacionMicrosip());
                        almacenEvc.setIsPrincipal(almacenMicroSip.getIsPrincipal());
                        //--/-/-/ 
                        
                        AlmacenPk almacenPk = almacensDaoImpl.insert(almacenEvc);
                        idAlmacenRelacionado = almacenPk.getIdAlmacen();

                        ////insertamos la relacion con los Id's
                        QuartzAlmacen qc = new QuartzAlmacen();
                        qc.setClave( (almacenMicrosip.getClave()!=null?almacenMicrosip.getClave():null) );
                        qc.setIdAlmacenEvc(almacenPk.getIdAlmacen());
                        qc.setIdAlmacenSistemTercero(almacenMicrosip.getIdMicrosip());
                        qc.setIdSistemaTercero(idSistematercero);
                        qc.setIdEmpresa(idEmpresa);
                        almacenDaoImpl.insert(qc);
                        contadorRegistrosNuevos++;
                    }catch(Exception e){
                        mensajeAlmacen += " Error al insertar almacen con ID: "+almacenMicrosip.getIdMicrosip() + "; ERROR: " + e.getMessage() + " , ";
                    }  
                    
                    /////--insertamos la relacion del almacen con los prodcutos en el almacen:
                    try{
                        if(almacenMicrosip.getListRelacProdAlmacen() != null ){
                            for(ExistenciaAlmacen existencia : almacenMicrosip.getListRelacProdAlmacen()){
                                ExistenciaAlmacen nuevaExistencia = new ExistenciaAlmacen();
                                nuevaExistencia.setIdAlmacen(idAlmacenRelacionado);
                                nuevaExistencia.setExistencia(existencia.getExistencia());
                                nuevaExistencia.setEstatus(1);
                                ///*buscamos el conceptos relacionado:
                                    QuartzConceptoDaoImpl conceptoDaoImpl = new QuartzConceptoDaoImpl(conn);
                                    QuartzConcepto conceptoExistente = null;
                                    try{
                                        conceptoExistente = conceptoDaoImpl.findByDynamicWhere("ID_CONCEPTO_SISTEM_TERCERO = " + existencia.getIdConcepto() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                                        nuevaExistencia.setIdConcepto(conceptoExistente.getIdConceptoEvc());
                                    }catch(Exception e1){}
                                    
                                ///*
                                existenciaAlmacenDaoImpl.insert(nuevaExistencia);
                            }
                        }
                    }catch(Exception e){
                        mensajeAlmacen += " Error al insertar la relacion de almacen-prodcutos con el almacen con ID: "+almacenMicrosip.getIdMicrosip() + "; ERROR: " + e.getMessage() + " , ";
                    }
                    /////--
                    
                }else{//actualizar
                    try{
                        com.tsp.microsip.bean.Almacen almacenMicroSip = almacenMicrosip.getAlmacen();                     
                        almacenMicroSip.setIdAlmacen(almacenExistente.getIdAlmacenEvc());
                        almacenMicroSip.setSincronizacionMicrosip(1);
                        almacenMicroSip.setIdEstatus(1);
                        //--/-/-/
                        Almacen almacenEvc = new Almacen();
                        if(almacenMicroSip.getIdAlmacen() > 0)
                            almacenEvc.setIdAlmacen(almacenMicroSip.getIdAlmacen());
                        almacenEvc.setIdEmpresa(idEmpresa);                                               
                        almacenEvc.setIdEstatus(almacenMicroSip.getIdEstatus());
                        almacenEvc.setNombre(almacenMicroSip.getNombre());
                        almacenEvc.setDireccion(almacenMicroSip.getDireccion());
                        almacenEvc.setAreaAlmacen(almacenMicroSip.getAreaAlmacen());
                        almacenEvc.setResponsable(almacenMicroSip.getResponsable());
                        almacenEvc.setPuesto(almacenMicroSip.getPuesto());
                        almacenEvc.setTelefono(almacenMicroSip.getTelefono());
                        almacenEvc.setCorreo(almacenMicroSip.getCorreo());
                        almacenEvc.setIdEstatus(almacenMicroSip.getIdEstatus());
                        almacenEvc.setSincronizacionMicrosip(almacenMicroSip.getSincronizacionMicrosip());
                        //--/-/-/ 
                        almacensDaoImpl.update(almacenEvc.createPk(), almacenEvc);
                        contadorRegistrosActualizados++;
                        
                            /////--insertamos la relacion del almacen con los prodcutos en el almacen:
                            try{
                                if(almacenMicrosip.getListRelacProdAlmacen() != null ){
                                    int conceptoIdEVC = 0;
                                    for(ExistenciaAlmacen existencia : almacenMicrosip.getListRelacProdAlmacen()){
                                        //validamos si existe la existencia en la relacion concepto - almacen
                                        ///*buscamos el conceptos relacionado:
                                            QuartzConceptoDaoImpl conceptoDaoImpl = new QuartzConceptoDaoImpl(conn);
                                            QuartzConcepto conceptoExistente = null;
                                            try{
                                                conceptoExistente = conceptoDaoImpl.findByDynamicWhere("ID_CONCEPTO_SISTEM_TERCERO = " + existencia.getIdConcepto() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                                                conceptoIdEVC = conceptoExistente.getIdConceptoEvc();
                                            }catch(Exception e1){}
                                        ///*
                                        ExistenciaAlmacen existenciaAlmacen = null;
                                        try{
                                            existenciaAlmacen = existenciaAlmacenDaoImpl.findByDynamicWhere( "ID_ALMACEN = " + almacenExistente.getIdAlmacenEvc() + " AND ID_CONCEPTO = " + conceptoIdEVC, null)[0];
                                        }catch(Exception e){}
                                        
                                        if(existenciaAlmacen != null){// actualizacmos la existencia
                                            existenciaAlmacen.setEstatus(1);
                                            existenciaAlmacen.setExistencia(existencia.getExistencia());
                                            existenciaAlmacenDaoImpl.update(existenciaAlmacen.createPk(), existenciaAlmacen);
                                        }else{//nueva existencia
                                            existenciaAlmacen = new ExistenciaAlmacen();
                                            existenciaAlmacen.setIdAlmacen(almacenExistente.getIdAlmacenEvc());
                                            existenciaAlmacen.setExistencia(existencia.getExistencia());
                                            existenciaAlmacen.setEstatus(1);
                                            existenciaAlmacen.setIdConcepto(conceptoIdEVC);                                             
                                            ///*
                                            existenciaAlmacenDaoImpl.insert(existenciaAlmacen);
                                        }
                                    }
                                }
                            }catch(Exception e){
                                mensajeAlmacen += " Error al insertar-actualizar la relacion de almacen-prodcutos con el almacen con ID: "+almacenMicrosip.getIdMicrosip() + "; ERROR: " + e.getMessage() + " , ";
                            }
                            /////--
                        
                    }catch(Exception e2){
                         mensajeAlmacen += " Error al actualizar almacen con ID: "+almacenMicrosip.getIdMicrosip() + "; ERROR: " + e2.getMessage() + " , ";
                    }
        
                }
            }
            
            if(mensajeAlmacen.trim().equals("")){
                controlBean.setExito(true);
            }
            controlBean.setMensajeError(mensajeAlmacen);
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
