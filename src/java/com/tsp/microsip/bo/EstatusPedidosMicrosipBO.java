/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.microsip.bo;

import com.tsp.microsip.bean.ControlBean;
import com.tsp.microsip.bean.EstatusPedidoSistemaTerceroMicrosip;
import com.tsp.sct.dao.dto.EstatusPedidoSistemaTercero;
import com.tsp.sct.dao.dto.EstatusPedidoSistemaTerceroPk;
import com.tsp.sct.dao.dto.QuartzEstatusPedido;
import com.tsp.sct.dao.jdbc.EstatusPedidoSistemaTerceroDaoImpl;
import com.tsp.sct.dao.jdbc.QuartzEstatusPedidoDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.dao.jdbc.SgfensEstatusPedidoDaoImpl;
import com.tsp.sct.util.StringManage;
import java.sql.Connection;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author HpPyme
 */
public class EstatusPedidosMicrosipBO {
    
    public ControlBean setEstatusPedidosMicrosip(List<EstatusPedidoSistemaTerceroMicrosip> estatusPedidoMicrosipBean, int idEmpresa , String acceso){
        ControlBean controlBean = new ControlBean();
        String mensajeEstatusPedido = "";
        Connection conn = null;
        try{            
            conn = ResourceManager.getConnection();
            EstatusPedidoSistemaTerceroDaoImpl estatusPedidoSistemaTerceroDaoImpl = new EstatusPedidoSistemaTerceroDaoImpl(conn);
            QuartzEstatusPedidoDaoImpl quartzEstatusPedidoDaoImpl = new QuartzEstatusPedidoDaoImpl(conn);
            QuartzEstatusPedido estatusPedidoExistente = null;
            
            int contadorRegistrosNuevos = 0; //variable auxiliar para el numero de registros insertados
            int contadorRegistrosActualizados = 0; //variable auxiliar para el numero de registros insertados
            int idEstatusPedidoTerceroRelacionado = 0; 
           
            
            int idSistematercero  = 0; //Se obtiene del token acceso 1er token
           
            try{                
                
                StringTokenizer st = new StringTokenizer(acceso, "|");
                if(st.hasMoreElements()){
                    idSistematercero = Integer.parseInt(st.nextElement().toString().trim());                
                }
            }catch(Exception e){
                System.out.println("Error al obtener IdSitemaTercero desde Token");
                
            }
            
            for(EstatusPedidoSistemaTerceroMicrosip estastusPedidoMicrosip : estatusPedidoMicrosipBean){
                //validamos si el almacen existe o no en el evc
                try{
                   
                    String queryOpcional = "";
                    if (estastusPedidoMicrosip.getIdMicrosip()>0){
                        queryOpcional = " ID_ESTATUS_SISTEMA_TERCERO = " + estastusPedidoMicrosip.getIdMicrosip();
                    }else{
                        if (StringManage.getValidString(estastusPedidoMicrosip.getClave()).length()>0)
                            queryOpcional = " CLAVE='" + StringManage.getValidString(estastusPedidoMicrosip.getClave()) + "'";
                    }
                    estatusPedidoExistente = quartzEstatusPedidoDaoImpl.findByDynamicWhere(queryOpcional +" AND ID_EMPRESA = " + idEmpresa, null)[0];
                }catch(Exception e1){estatusPedidoExistente = null;}
                if(estatusPedidoExistente == null){//almacen nuevo
                    try{
                        com.tsp.microsip.bean.EstatusPedidoSistemaTerceroBean estatusPedidoMicroSip = estastusPedidoMicrosip.getEstatusPedido();
                       
                        estatusPedidoMicroSip.setSincronizacionMicrosip(1);
                        
                        //--/-/-/
                        EstatusPedidoSistemaTercero estatusPedidoEVC = new EstatusPedidoSistemaTercero();
                        
                                               
                        estatusPedidoEVC.setIdEmpresa(idEmpresa);            
                        estatusPedidoEVC.setNombreEstatus(estatusPedidoMicroSip.getNombreEstatus());
                        estatusPedidoEVC.setDescripcion(estatusPedidoMicroSip.getDescripcion());
                        estatusPedidoEVC.setSincronizacionMicrosip(1);
                        
                        //--/-/-/ 
                        
                        EstatusPedidoSistemaTerceroPk statusPedidoPk = estatusPedidoSistemaTerceroDaoImpl.insert(estatusPedidoEVC);
                        idEstatusPedidoTerceroRelacionado = statusPedidoPk.getIdEstatusPedidoTercero();

                        ////insertamos la relacion con los Id's
                        QuartzEstatusPedido qc = new QuartzEstatusPedido();
                        qc.setClave( (estastusPedidoMicrosip.getClave()!=null?estastusPedidoMicrosip.getClave():null) );
                        qc.setIdEstatusTerceroEvc(statusPedidoPk.getIdEstatusPedidoTercero());
                        qc.setIdEstatusSistemaTercero(estastusPedidoMicrosip.getIdMicrosip());
                        qc.setIdSistemaTercero(idSistematercero);
                        qc.setIdEmpresa(idEmpresa);
                        quartzEstatusPedidoDaoImpl.insert(qc);
                        contadorRegistrosNuevos++;
                    }catch(Exception e){
                        mensajeEstatusPedido += " Error al insertar almacen con ID: "+estastusPedidoMicrosip.getIdMicrosip() + "; ERROR: " + e.getMessage() + " , ";
                    }  
                    
                    
                }else{//actualizar
                    try{
                        com.tsp.microsip.bean.EstatusPedidoSistemaTerceroBean estatusPedidoMicroSip = estastusPedidoMicrosip.getEstatusPedido();
                        
                        
                        estatusPedidoMicroSip.setSincronizacionMicrosip(1);
                        //--/-/-/
                        EstatusPedidoSistemaTercero estatusPedidoEVC = new EstatusPedidoSistemaTercero();
                        if(estatusPedidoMicroSip.getIdEstatusPedidoTercero()> 0)
                            estatusPedidoEVC.setIdEstatusPedidoTercero(estatusPedidoMicroSip.getIdEstatusPedidoTercero());
                        estatusPedidoEVC.setIdEmpresa(idEmpresa); 
                        estatusPedidoEVC.setNombreEstatus(estatusPedidoMicroSip.getNombreEstatus());
                        estatusPedidoEVC.setDescripcion(estatusPedidoMicroSip.getDescripcion() );
                        estatusPedidoEVC.setSincronizacionMicrosip(1);
                        //--/-/-/ 
                        estatusPedidoSistemaTerceroDaoImpl.update(estatusPedidoEVC.createPk(), estatusPedidoEVC);
                        contadorRegistrosActualizados++;
                          
                        
                    }catch(Exception e2){
                         mensajeEstatusPedido += " Error al actualizar Estatus de Pedidos con ID: "+estastusPedidoMicrosip.getClave()+ "; ERROR: " + e2.getMessage() + " , ";
                    }
        
                }
            }
            
            if(mensajeEstatusPedido.trim().equals("")){
                controlBean.setExito(true);
            }
            controlBean.setMensajeError(mensajeEstatusPedido);
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
