/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.microsip.bo;

import com.tsp.microsip.bean.ConceptoMicrosip;
import com.tsp.microsip.bean.ControlBean;
import com.tsp.sct.bo.ConceptoBO;
import com.tsp.sct.bo.ExistenciaAlmacenBO;
import com.tsp.sct.dao.dto.Concepto;
import com.tsp.sct.dao.dto.ConceptoPk;
import com.tsp.sct.dao.dto.QuartzConcepto;
import com.tsp.sct.dao.jdbc.ConceptoDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzConceptoDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.util.Encrypter;
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
public class ConceptoMicrosipBO {

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
            
            filtroBusqueda += " AND ID_ESTATUS = 1 AND ID_CONCEPTO > 0 ";
            
            ConceptoBO conceptoBO = new ConceptoBO(conn);
            cantidad = conceptoBO.findCantidadConceptos(-1, idEmpresa, 0, 0, filtroBusqueda);  
        }catch(Exception e){
            e.printStackTrace();
            cantidad =  0;            
        }finally{
            try{ if (conn!=null) conn.close(); }catch(Exception ex){}
        }
        return cantidad;  
    }
    
    public List<ConceptoMicrosip> getRegistros(int tipo, int idEmpresa){
        List<ConceptoMicrosip> listaRegistros = new ArrayList<ConceptoMicrosip>();
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
            filtroBusqueda += " AND ID_ESTATUS = 1 AND ID_CONCEPTO > 0 ";
            
            ConceptoBO conceptoBO = new ConceptoBO(conn);
            Concepto[] registrosEncontrados = conceptoBO.findConceptos(-1, idEmpresa, 0, 0, filtroBusqueda);  
            
            for(Concepto item : registrosEncontrados ){
                
                
                double stockGral = 0;
                try{
                    if (item.getIdConcepto() > 0) {
                        
                        conceptoBO = new ConceptoBO(item.getIdConcepto(),conn);                        
                        //Obtenemos stock gral del prod
                        ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(conn);
                        stockGral = exisAlmBO.getExistenciaGeneralByEmpresaProducto(idEmpresa, item.getIdConcepto());
                    }
                }catch(Exception e){}      
                
                
                //--/-/-/
                com.tsp.microsip.bean.Concepto conceptoMicro = new com.tsp.microsip.bean.Concepto();
                conceptoMicro.setIdConcepto(item.getIdConcepto());
                conceptoMicro.setIdEmpresa(item.getIdEmpresa());
                conceptoMicro.setNombre(item.getNombreDesencriptado());
                conceptoMicro.setDescripcion(item.getDescripcion());
                conceptoMicro.setPrecio(item.getPrecio());
                conceptoMicro.setIdEstatus(item.getIdEstatus());
                conceptoMicro.setIdentificacion(item.getIdentificacion());
                conceptoMicro.setIdCategoria(item.getIdCategoria());
                conceptoMicro.setIdMarca(item.getIdMarca());
                conceptoMicro.setIdSubcategoria(item.getIdSubcategoria());
                conceptoMicro.setIdEmbalaje(item.getIdEmbalaje());
                conceptoMicro.setIdImpuesto(item.getIdImpuesto());
                conceptoMicro.setPrecioCompra(item.getPrecioCompra());
                conceptoMicro.setNumeroLote(item.getNumeroLote());
                conceptoMicro.setFechaCaducidad(item.getFechaCaducidad());
                conceptoMicro.setNumArticulosDisponibles(stockGral);
                conceptoMicro.setGenerico(item.getGenerico());
                conceptoMicro.setImagenCarpetaArchivo(item.getImagenCarpetaArchivo());
                conceptoMicro.setImagenNombreArchivo(item.getImagenNombreArchivo());
                conceptoMicro.setDescripcionCorta(item.getDescripcionCorta());
                conceptoMicro.setIdAlmacen(item.getIdAlmacen());
                conceptoMicro.setStockMinimo(item.getStockMinimo());
                conceptoMicro.setStockAvisoMin(item.getStockAvisoMin());
                conceptoMicro.setDetalle(item.getDetalle());
                conceptoMicro.setFechaAlta(item.getFechaAlta());
                conceptoMicro.setVolumen(item.getVolumen());
                conceptoMicro.setPeso(item.getPeso());
                conceptoMicro.setObservaciones(item.getObservaciones());
                conceptoMicro.setPrecioDocena(item.getPrecioDocena());
                conceptoMicro.setPrecioMayoreo(item.getPrecioMayoreo());
                conceptoMicro.setPrecioEspecial(item.getPrecioEspecial());
                conceptoMicro.setImpuestoXConcepto(item.getImpuestoXConcepto());
                conceptoMicro.setClaveartSae(item.getClaveartSae());
                conceptoMicro.setDescuentoPorcentaje(item.getDescuentoPorcentaje());
                conceptoMicro.setDescuentoMonto(item.getDescuentoMonto());
                conceptoMicro.setPrecioMedioMayoreo(item.getPrecioMedioMayoreo());
                conceptoMicro.setMaxMenudeo(item.getMaxMenudeo());
                conceptoMicro.setMinMedioMayoreo(item.getMinMedioMayoreo());
                conceptoMicro.setMaxMedioMayoreo(item.getMaxMedioMayoreo());
                conceptoMicro.setMinMayoreo(item.getMinMayoreo());
                conceptoMicro.setIdSubcategoria2(item.getIdSubcategoria2());
                conceptoMicro.setIdSubcategoria3(item.getIdSubcategoria3());
                conceptoMicro.setIdSubcategoria4(item.getIdSubcategoria4());
                conceptoMicro.setPrecioMinimoVenta(item.getPrecioMinimoVenta());
                conceptoMicro.setNombreDesencriptado(item.getNombreDesencriptado());
                conceptoMicro.setRutaImagen(item.getRutaImagen());
                conceptoMicro.setRutaVideo(item.getRutaVideo());
                conceptoMicro.setCaracteristiscas(item.getCaracteristiscas());
                conceptoMicro.setComisionPorcentaje(item.getComisionPorcentaje());
                conceptoMicro.setComisionMonto(item.getComisionMonto());
                conceptoMicro.setSincronizacionMicrosip(item.getSincronizacionMicrosip());
                
                //--/-/-/
                
                ConceptoMicrosip wsBean = new ConceptoMicrosip();
                
                if(item.getClave() != null){
                    conceptoMicro.setClave(item.getClave());
                    wsBean.setClave(item.getClave());
                }
                
                wsBean.setConcepto(conceptoMicro);
                if(tipo == 2){//si es una actualización, envamos el id o clave del sistema de microsip (identificador)
                    try{
                        QuartzConcepto qc = new QuartzConceptoDaoImpl(conn).findByDynamicWhere(" ID_CONCEPTO_EVC = " + item.getIdConcepto()  + " AND ID_EMPRESA = " + idEmpresa+ " ORDER BY ID_QUARTZ DESC ", null)[0]; //buscamos el registro correspondiente
                        if(qc != null){
                            if(qc.getIdConceptoSistemTercero() > 0){
                                wsBean.setIdMicrosip(qc.getIdConceptoSistemTercero());
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
    
    public int setIdentificadores(List<QuartzConcepto> quartzDto, int idEmpresa, String acceso){
        int insertarActualizacion = 0;        
        Connection conn = null;
        
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
            conn = ResourceManager.getConnection();
            ConceptoDaoImpl conceptoDao = new ConceptoDaoImpl(conn);
            List<QuartzConcepto> quartzIdSincronizacion = new ArrayList<QuartzConcepto>();
            QuartzConceptoDaoImpl quartzConceptoDaoImpl = new QuartzConceptoDaoImpl(conn);
            for(QuartzConcepto quaC : quartzDto){
                //Vemos si ya existe el registro:
                QuartzConcepto quartzConcepto = null;
                if(quaC.getIdConceptoSistemTercero() > 0){ //verificamos si se buscara por id o por clave
                    try{quartzConcepto = quartzConceptoDaoImpl.findByDynamicWhere( " ID_CONCEPTO_EVC = " + quaC.getIdConceptoEvc() + " AND ID_CONCEPTO_SISTEM_TERCERO = " + quaC.getIdConceptoSistemTercero()  + " AND ID_EMPRESA = " + idEmpresa , null)[0];}catch(Exception e){/*e.printStackTrace();*/}
                }else if(quaC.getClave() != null && !quaC.getClave().equals("")){
                    try{quartzConcepto = quartzConceptoDaoImpl.findByDynamicWhere( " ID_CONCEPTO_EVC = " + quaC.getIdConceptoEvc() + " AND CLAVE = " + quaC.getClave()  + " AND ID_EMPRESA = " + idEmpresa , null)[0];}catch(Exception e){/*e.printStackTrace();*/}
                }

                if(quartzConcepto == null){//si no existe lo insertamos
                    quartzConcepto = new QuartzConcepto();
                    quartzConcepto.setIdConceptoEvc(quaC.getIdConceptoEvc());
                    if(quaC.getIdConceptoSistemTercero() > 0){
                        quartzConcepto.setIdConceptoSistemTercero(quaC.getIdConceptoSistemTercero());
                    }else{
                        quartzConcepto.setIdConceptoSistemTercero(0);
                    }
                    if(quaC.getClave() != null && !quaC.getClave().equals("")){
                        quartzConcepto.setClave(quaC.getClave());
                    }else{
                        quartzConcepto.setClave("");
                    }
                    quartzConcepto.setIdEmpresa(idEmpresa);
                    quartzIdSincronizacion.add(quartzConcepto);
                }else{//si existe, borramos e insertamos el registro para que sea el ultimo registro insertado y así al recuperar los ID sea el mas actual.
                    try{quartzConceptoDaoImpl.delete(quartzConcepto.createPk());
                    QuartzConcepto quartzConceptoClon = new QuartzConcepto();
                    quartzConceptoClon.setIdConceptoEvc(quartzConcepto.getIdConceptoEvc());
                    quartzConceptoClon.setIdConceptoSistemTercero(quartzConcepto.getIdConceptoSistemTercero());
                    quartzConceptoClon.setClave(quartzConcepto.getClave());
                    quartzConceptoClon.setIdSistemaTercero(idSistematercero);
                    quartzConceptoClon.setIdEmpresa(quartzConcepto.getIdEmpresa());
                    quartzConceptoDaoImpl.insert(quartzConceptoClon);
                    }catch(Exception e){System.out.println("------------ Error al actualizar Identificadores de quartz de concepto: "+e.getMessage());}
                }
                
                //actualizamos la bandera de conceptos a sincronizado:
                try{
                    Concepto concepto = new ConceptoBO(conn).findConceptobyId(quaC.getIdConceptoEvc());
                    concepto.setSincronizacionMicrosip(1);
                    conceptoDao.update(concepto.createPk(), concepto);
                }catch(Exception e){e.printStackTrace();}
                
            }
            insertarActualizacion = 0;
            try{
                QuartzConceptoDaoImpl qcDaoImp = new QuartzConceptoDaoImpl(conn);
                for(QuartzConcepto insertDto : quartzIdSincronizacion){//insertamos los registros:
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
    
    public ControlBean setConceptoMicrosip(List<ConceptoMicrosip> conceptosMicrosipBean, int idEmpresa , String acceso){
        ControlBean controlBean = new ControlBean();
        String mensajeConcepto = "";
        Connection conn = null;
        try{            
            conn = ResourceManager.getConnection();
            ConceptoDaoImpl conceptosDaoImpl = new ConceptoDaoImpl(conn);
            QuartzConceptoDaoImpl conceptoDaoImpl = new QuartzConceptoDaoImpl(conn);
            QuartzConcepto conceptoExistente = null;
            
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
            
            Encrypter encripter = new Encrypter();          
            
            for(ConceptoMicrosip conceptoMicrosip : conceptosMicrosipBean){
                //validamos si el concepto existe o no en el evc
                conceptoExistente = null;
                try{
                    //conceptoExistente = conceptoDaoImpl.findByDynamicWhere("ID_CONCEPTO_SISTEM_TERCERO = " + conceptoMicrosip.getIdMicrosip() + " AND ID_EMPRESA = " + idEmpresa, null)[0];
                    String queryOpcional = "";
                    if (conceptoMicrosip.getIdMicrosip()>0){
                        queryOpcional = " ID_CONCEPTO_SISTEM_TERCERO = " + conceptoMicrosip.getIdMicrosip();
                    }else{
                        if (StringManage.getValidString(conceptoMicrosip.getClave()).length()>0)
                            queryOpcional = " CLAVE='" + StringManage.getValidString(conceptoMicrosip.getClave()) + "'";
                    }
                    conceptoExistente = conceptoDaoImpl.findByDynamicWhere(queryOpcional +" AND ID_EMPRESA = " + idEmpresa, null)[0];
                }catch(Exception e1){conceptoExistente = null;}
                
                if(conceptoExistente == null){//concepto nuevo
                    try{
                        com.tsp.microsip.bean.Concepto conceptoMicroSip = conceptoMicrosip.getConcepto();
                        Concepto ultimoRegistroConceptos = conceptosDaoImpl.findLast();
                        int idConceptoNuevo = ultimoRegistroConceptos.getIdConcepto() + 1;            
                        conceptoMicroSip.setIdConcepto(idConceptoNuevo);
                        
                        conceptoMicroSip.setIdEstatus(1);
                        conceptoMicroSip.setSincronizacionMicrosip(1);
                        
                        //--/-/-/
                        Concepto conceptoEvc = new Concepto();
                        if(conceptoMicroSip.getIdConcepto() > 0)
                            conceptoEvc.setIdConcepto(conceptoMicroSip.getIdConcepto());                        
                        //if(conceptoMicroSip.getIdEmpresa() > 0)
                        conceptoEvc.setIdEmpresa(idEmpresa);                        
                        if(conceptoMicroSip.getNombre() != null)
                            conceptoEvc.setNombre(encripter.encodeString2(conceptoMicroSip.getNombre()));
                        else
                            conceptoEvc.setNombre("");
                        if(conceptoMicroSip.getDescripcion() != null && !conceptoMicroSip.getDescripcion().trim().equals(""))
                            conceptoEvc.setDescripcion(conceptoMicroSip.getDescripcion());
                        else
                            conceptoEvc.setDescripcion(conceptoMicroSip.getNombre());
                        conceptoEvc.setPrecio(conceptoMicroSip.getPrecio());                        
                        conceptoEvc.setIdEstatus(conceptoMicroSip.getIdEstatus());
                        conceptoEvc.setIdentificacion(conceptoMicroSip.getIdentificacion());
                        conceptoEvc.setPrecioCompra(conceptoMicroSip.getPrecioCompra());
                        conceptoEvc.setNumeroLote(conceptoMicroSip.getNumeroLote());
                        conceptoEvc.setFechaCaducidad(conceptoMicroSip.getFechaCaducidad());
                        //conceptoEvc.setNumArticulosDisponibles(conceptoMicroSip.getNumArticulosDisponibles());
                        conceptoEvc.setDescripcionCorta(conceptoMicroSip.getDescripcionCorta());
                        //recuperamos el Id de almancen de microsip
                        //conceptoEvc.setIdAlmacen(conceptoMicroSip.getIdAlmacen());
                        if(conceptoMicroSip.getStockMinimo() > 0)
                            conceptoEvc.setStockMinimo(conceptoMicroSip.getStockMinimo());
                        if(conceptoMicroSip.getStockAvisoMin() > 0)
                            conceptoEvc.setStockAvisoMin(conceptoMicroSip.getStockAvisoMin());
                        conceptoEvc.setDetalle(conceptoMicroSip.getDetalle());
                        if(conceptoMicroSip.getFechaAlta() != null)
                            conceptoEvc.setFechaAlta(conceptoMicroSip.getFechaAlta());
                        if(conceptoMicroSip.getVolumen() > 0)
                            conceptoEvc.setVolumen(conceptoMicroSip.getVolumen());
                        if(conceptoMicroSip.getPeso() > 0)
                            conceptoEvc.setPeso(conceptoMicroSip.getPeso());
                        conceptoEvc.setObservaciones(conceptoMicroSip.getObservaciones());
                        conceptoEvc.setPrecioDocena(conceptoMicroSip.getPrecioDocena());
                        conceptoEvc.setPrecioMayoreo(conceptoMicroSip.getPrecioMayoreo());
                        conceptoEvc.setPrecioEspecial(conceptoMicroSip.getPrecioEspecial());
                        conceptoEvc.setDescuentoPorcentaje(conceptoMicroSip.getDescuentoPorcentaje());
                        conceptoEvc.setDescuentoMonto(conceptoMicroSip.getDescuentoMonto());
                        conceptoEvc.setPrecioMedioMayoreo(conceptoMicroSip.getPrecioMedioMayoreo());
                        conceptoEvc.setMaxMenudeo(conceptoMicroSip.getMaxMenudeo());
                        conceptoEvc.setMinMedioMayoreo(conceptoMicroSip.getMinMedioMayoreo());
                        conceptoEvc.setMaxMedioMayoreo(conceptoMicroSip.getMaxMedioMayoreo());
                        conceptoEvc.setMinMayoreo(conceptoMicroSip.getMinMayoreo());
                        conceptoEvc.setPrecioMinimoVenta(conceptoMicroSip.getPrecioMinimoVenta());
                        conceptoEvc.setMaxMenudeo(conceptoMicroSip.getMaxMenudeo());
                        conceptoEvc.setNombreDesencriptado(conceptoMicroSip.getNombreDesencriptado());
                        conceptoEvc.setSincronizacionMicrosip(conceptoMicroSip.getSincronizacionMicrosip());
                        
                        if(conceptoMicrosip.getClave() != null){
                            conceptoEvc.setClave(conceptoMicrosip.getClave());                            
                        }
                        
                        //--/-/-/ 
                        
                        ConceptoPk conceptoPk = conceptosDaoImpl.insert(conceptoEvc);

                        ////insertamos la relacion con los Id's
                        QuartzConcepto qc = new QuartzConcepto();
                        qc.setClave( (conceptoMicrosip.getClave()!=null?conceptoMicrosip.getClave():null) );
                        qc.setIdConceptoEvc(conceptoPk.getIdConcepto());
                        qc.setIdConceptoSistemTercero(conceptoMicrosip.getIdMicrosip());
                        qc.setIdSistemaTercero(idSistematercero);
                        qc.setIdEmpresa(idEmpresa);
                        conceptoDaoImpl.insert(qc);
                        contadorRegistrosNuevos++;
                    }catch(Exception e){
                        mensajeConcepto += " Error al insertar concepto con ID: "+conceptoMicrosip.getIdMicrosip() + "; ERROR: " + e.getMessage() + " , ";
                    }  
                }else{//actualizar
                    try{
                        com.tsp.microsip.bean.Concepto conceptoMicroSip = conceptoMicrosip.getConcepto();                     
                        conceptoMicroSip.setIdConcepto(conceptoExistente.getIdConceptoEvc());
                        conceptoMicroSip.setSincronizacionMicrosip(1);
                        conceptoMicroSip.setIdEstatus(1);
                        //--/-/-/
                        Concepto conceptoEvc = new Concepto();
                        if(conceptoMicroSip.getIdConcepto() > 0)
                            conceptoEvc.setIdConcepto(conceptoMicroSip.getIdConcepto());                        
                        conceptoEvc.setIdEmpresa(idEmpresa);                        
                        if(conceptoMicroSip.getNombre() != null)
                            conceptoEvc.setNombre(encripter.encodeString2(conceptoMicroSip.getNombre()));
                        else
                            conceptoEvc.setNombre("");
                        if(conceptoMicroSip.getDescripcion() != null && !conceptoMicroSip.getDescripcion().trim().equals(""))
                            conceptoEvc.setDescripcion(conceptoMicroSip.getDescripcion());
                        else
                            conceptoEvc.setDescripcion(conceptoMicroSip.getNombre());
                        conceptoEvc.setPrecio(conceptoMicroSip.getPrecio());                        
                        conceptoEvc.setIdEstatus(conceptoMicroSip.getIdEstatus());
                        conceptoEvc.setIdentificacion(conceptoMicroSip.getIdentificacion());
                        conceptoEvc.setPrecioCompra(conceptoMicroSip.getPrecioCompra());
                        conceptoEvc.setNumeroLote(conceptoMicroSip.getNumeroLote());
                        conceptoEvc.setFechaCaducidad(conceptoMicroSip.getFechaCaducidad());
                        conceptoEvc.setNumArticulosDisponibles(conceptoMicroSip.getNumArticulosDisponibles());
                        conceptoEvc.setDescripcionCorta(conceptoMicroSip.getDescripcionCorta());
                        //recuperamos el Id de almancen de microsip
                        //conceptoEvc.setIdAlmacen(conceptoMicroSip.getIdAlmacen());
                        if(conceptoMicroSip.getStockMinimo() > 0)
                            conceptoEvc.setStockMinimo(conceptoMicroSip.getStockMinimo());
                        if(conceptoMicroSip.getStockAvisoMin() > 0)
                            conceptoEvc.setStockAvisoMin(conceptoMicroSip.getStockAvisoMin());
                        conceptoEvc.setDetalle(conceptoMicroSip.getDetalle());
                        if(conceptoMicroSip.getFechaAlta() != null)
                            conceptoEvc.setFechaAlta(conceptoMicroSip.getFechaAlta());
                        if(conceptoMicroSip.getVolumen() > 0)
                            conceptoEvc.setVolumen(conceptoMicroSip.getVolumen());
                        if(conceptoMicroSip.getPeso() > 0)
                            conceptoEvc.setPeso(conceptoMicroSip.getPeso());
                        conceptoEvc.setObservaciones(conceptoMicroSip.getObservaciones());
                        conceptoEvc.setPrecioDocena(conceptoMicroSip.getPrecioDocena());
                        conceptoEvc.setPrecioMayoreo(conceptoMicroSip.getPrecioMayoreo());
                        conceptoEvc.setPrecioEspecial(conceptoMicroSip.getPrecioEspecial());
                        conceptoEvc.setDescuentoPorcentaje(conceptoMicroSip.getDescuentoPorcentaje());
                        conceptoEvc.setDescuentoMonto(conceptoMicroSip.getDescuentoMonto());
                        conceptoEvc.setPrecioMedioMayoreo(conceptoMicroSip.getPrecioMedioMayoreo());
                        conceptoEvc.setMaxMenudeo(conceptoMicroSip.getMaxMenudeo());
                        conceptoEvc.setMinMedioMayoreo(conceptoMicroSip.getMinMedioMayoreo());
                        conceptoEvc.setMaxMedioMayoreo(conceptoMicroSip.getMaxMedioMayoreo());
                        conceptoEvc.setMinMayoreo(conceptoMicroSip.getMinMayoreo());
                        conceptoEvc.setPrecioMinimoVenta(conceptoMicroSip.getPrecioMinimoVenta());
                        conceptoEvc.setMaxMenudeo(conceptoMicroSip.getMaxMenudeo());
                        conceptoEvc.setNombreDesencriptado(conceptoMicroSip.getNombreDesencriptado());
                        conceptoEvc.setClave(conceptoMicrosip.getClave());
                        conceptoEvc.setSincronizacionMicrosip(conceptoMicroSip.getSincronizacionMicrosip());
                        if(conceptoMicrosip.getClave() != null){
                            conceptoEvc.setClave(conceptoMicrosip.getClave());                            
                        }
                        //--/-/-/ 
                        conceptosDaoImpl.update(conceptoEvc.createPk(), conceptoEvc);
                        contadorRegistrosActualizados++;
                    }catch(Exception e2){
                         mensajeConcepto += " Error al actualizar concepto con ID: "+conceptoMicrosip.getIdMicrosip() + "; ERROR: " + e2.getMessage() + " , ";
                    }
        
                }
            }
            
            if(mensajeConcepto.trim().equals("")){
                controlBean.setExito(true);
            }
            controlBean.setMensajeError(mensajeConcepto);
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
