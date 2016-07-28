/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.microsip.bo;

import com.tsp.microsip.bean.ControlBean;
import com.tsp.microsip.bean.ExistenciaAlmacenMicrosip;
import com.tsp.sct.bo.ConceptoBO;
import com.tsp.sct.bo.ExistenciaAlmacenBO;
import com.tsp.sct.dao.dto.Concepto;
import com.tsp.sct.dao.dto.ExistenciaAlmacen;
import com.tsp.sct.dao.dto.Movimiento;
import com.tsp.sct.dao.dto.QuartzAlmacen;
import com.tsp.sct.dao.dto.QuartzConcepto;
import com.tsp.sct.dao.jdbc.ConceptoDaoImpl;
import com.tsp.sct.dao.jdbc.ExistenciaAlmacenDaoImpl;
import com.tsp.sct.dao.jdbc.MovimientoDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzAlmacenDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzConceptoDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.util.Encrypter;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 *
 * @author HpPyme
 */
public class ExistenciaAlmacenMicrosipBO {
    
    public ControlBean setExistenciaAlmacenMicrosip(List<ExistenciaAlmacenMicrosip> existenciaAlmacenMicrosipBean, int idEmpresa, String acceso){
        ControlBean controlBean = new ControlBean();
        String mensajeInvActual = "";
        Connection conn = null;
        try{            
            conn = ResourceManager.getConnection();
            ExistenciaAlmacenDaoImpl existenciaAlmacenDaoImpl = new ExistenciaAlmacenDaoImpl(conn);
            QuartzConceptoDaoImpl conceptoDaoImpl = new QuartzConceptoDaoImpl(conn);
            QuartzConcepto conceptoExistente = null;
            QuartzAlmacenDaoImpl almacenDaoImpl = new QuartzAlmacenDaoImpl(conn);
            QuartzAlmacen almacenExistente = null;
               
            int contadorRegistrosNuevos = 0; //variable auxiliar para el numero de registros insertados
            int contadorRegistrosActualizados = 0; //variable auxiliar para el numero de registros insertados
            
            Encrypter encripter = new Encrypter();          
            
            for(ExistenciaAlmacenMicrosip existAlmMicrosip : existenciaAlmacenMicrosipBean){
             
                
                //validamos si el almacen existe o no en el evc
                almacenExistente = null;
                try{
                    //almacenExistente = almacenDaoImpl.findByDynamicWhere("ID_ALMACEN_SISTEM_TERCERO = " + almacenMicrosip.getIdMicrosip() + " AND ID_EMPRESA = " + idEmpresa+ " ORDER BY ID_QUARTZ DESC ", null)[0];
                    String queryOpcional = "";
                    if (existAlmMicrosip.getIdMicrosip()>0){
                        queryOpcional = " ID_ALMACEN_SISTEM_TERCERO = " + existAlmMicrosip.getExistenciaAlmacen().getIdAlmacen();
                    }
                    almacenExistente = almacenDaoImpl.findByDynamicWhere(queryOpcional +" AND ID_EMPRESA = " + idEmpresa, null)[0];
                }catch(Exception e1){almacenExistente = null;}                
                
                
                //validamos si el concepto existe o no en el evc
                conceptoExistente = null;
                try{
                    
                    String queryOpcional = "";
                    if (existAlmMicrosip.getIdMicrosip()>0){
                        queryOpcional = " ID_CONCEPTO_SISTEM_TERCERO = " + existAlmMicrosip.getExistenciaAlmacen().getIdConcepto();
                    }
                    conceptoExistente = conceptoDaoImpl.findByDynamicWhere(queryOpcional +" AND ID_EMPRESA = " + idEmpresa, null)[0];
                }catch(Exception e1){conceptoExistente = null;}
                                
          
                
                
                if(conceptoExistente != null && almacenExistente != null){
                    try{
                        
                        ExistenciaAlmacen almacenExistsEVC = null;
                        boolean nuevo = false;
                        try{
                            almacenExistsEVC  = new ExistenciaAlmacenBO(conn).getExistenciaProductoAlmacen(almacenExistente.getIdAlmacenEvc(), conceptoExistente.getIdConceptoEvc());
                        }catch(Exception e){}
                        if(almacenExistsEVC==null){
                            nuevo = true;
                            almacenExistsEVC = new ExistenciaAlmacen();
                            almacenExistsEVC.setIdAlmacen(almacenExistente.getIdAlmacenEvc());
                            almacenExistsEVC.setIdConcepto(conceptoExistente.getIdConceptoEvc());
                            
                        }
                                                        
                        almacenExistsEVC.setExistencia(existAlmMicrosip.getExistenciaAlmacen().getExistencia());    
                        almacenExistsEVC.setEstatus(1);
                       

                        if(nuevo){//verificaoms si actualizamos el registro o creamos unos nuevo
                            existenciaAlmacenDaoImpl.insert(almacenExistsEVC); 
                            contadorRegistrosNuevos++;
                       }else{
                            existenciaAlmacenDaoImpl.update(almacenExistsEVC.createPk(), almacenExistsEVC);
                            contadorRegistrosActualizados++;
                       }

                        /**
                         * Insertamos movimiento entrada inicial*
                         */
                        MovimientoDaoImpl movimientosDaoImpl = new MovimientoDaoImpl(conn);
                        ConceptoBO conceptoBO = new ConceptoBO(conn);
                        Concepto concep = null;
                        String nombreConcepto = "";

                        try{
                            concep = conceptoBO.findConceptobyId(conceptoExistente.getIdConceptoEvc());      
                            nombreConcepto =concep.getNombreDesencriptado();
                        }catch(Exception e){                           
                        }
                        
                        
                        Movimiento movimientoDto = new Movimiento();                    

                        movimientoDto.setIdEmpresa((int)idEmpresa);
                        movimientoDto.setTipoMovimiento("ENTRADA");
                        movimientoDto.setNombreProducto(nombreConcepto);
                        movimientoDto.setContabilidad(existAlmMicrosip.getExistenciaAlmacen().getExistencia());
                        movimientoDto.setIdProveedor(-1);
                        movimientoDto.setOrdenCompra("");
                        movimientoDto.setNumeroGuia("");
                        movimientoDto.setIdAlmacen(almacenExistente.getIdAlmacenEvc());
                        movimientoDto.setConceptoMovimiento("Inventario Inicial (Importado)");
                        movimientoDto.setFechaRegistro(new Date());
                        movimientoDto.setIdConcepto(conceptoExistente.getIdConceptoEvc());
                        movimientosDaoImpl.insert(movimientoDto);    
                                               
                        
                    }catch(Exception e){
                        mensajeInvActual += " Error al insertar Inventario Actual con ID: "+existAlmMicrosip.getIdMicrosip() + "; ERROR: " + e.getMessage() + " , ";
                    }  
                }else{            
                    mensajeInvActual += " Error al actualizar concepto con ID: "+ existAlmMicrosip.getExistenciaAlmacen().getIdConcepto() + "; ERROR: Concepto no sincronizado con EVC , ";
                           
                }
                
                
            }
            
            if(mensajeInvActual.trim().equals("")){
                controlBean.setExito(true);
            }
            controlBean.setMensajeError(mensajeInvActual);
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
