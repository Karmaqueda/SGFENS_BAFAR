/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.util.test;

import com.tsp.sct.bo.ConceptoBO;
import com.tsp.sct.bo.ExistenciaAlmacenBO;
import com.tsp.sct.dao.dto.Almacen;
import com.tsp.sct.dao.dto.Concepto;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.dao.dto.ExistenciaAlmacen;
import com.tsp.sct.dao.exceptions.EmpresaDaoException;
import com.tsp.sct.dao.jdbc.AlmacenDaoImpl;
import com.tsp.sct.dao.jdbc.ConceptoDaoImpl;
import com.tsp.sct.dao.jdbc.EmpresaDaoImpl;
import com.tsp.sct.dao.jdbc.ExistenciaAlmacenDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author HpPyme
 */
public class qryAlmacenProductos {   
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void queryprods(){
        // TODO code application logic here
        
        
        
      try{
 
           Connection conn = ResourceManager.getConnection();
          
           EmpresaDaoImpl empresaDao = new EmpresaDaoImpl();
           Empresa[] empresasDtos = empresaDao.findByDynamicWhere(" ID_ESTATUS = 1 ", null);

          System.out.println("Empresa.....Length.........." + empresasDtos.length);

           for(Empresa emp:empresasDtos){
               
               System.out.println("Empresa..............." + emp.getIdEmpresa());

               AlmacenDaoImpl almDao = new AlmacenDaoImpl();
               Almacen[] almMinimosDto = almDao.findByDynamicSelect(" "
                       + " SELECT MIN(ID_ALMACEN) AS ID_ALMACEN ,ID_EMPRESA ,ID_ESTATUS ,NOMBRE ,DIRECCION ,AREA_ALMACEN ,RESPONSABLE ,PUESTO , TELEFONO , CORREO ,SINCRONIZACION_MICROSIP ,isPrincipal "
                       + " FROM ALMACEN "
                       + " WHERE ISPRINCIPAL = 1 AND ID_ESTATUS = 1 "
                       + " AND ID_EMPRESA = " + emp.getIdEmpresa() , null);
               
              ConceptoDaoImpl cancpetoDao =  new ConceptoDaoImpl(conn);
              ExistenciaAlmacenDaoImpl almdao = new ExistenciaAlmacenDaoImpl(conn);
              for(Almacen alm:almMinimosDto){
                  
                  
                  Concepto[] conceptosDtos = cancpetoDao.findByDynamicWhere(" "
                          + " ID_EMPRESA =  " + alm.getIdEmpresa()
                          + " AND ID_ESTATUS =  1 ", null);
                  
                  
                  for(Concepto prod:conceptosDtos){
                      
                      ConceptoBO conceptoBO = new ConceptoBO(prod.getIdConcepto(), conn );
                      Concepto conceptoDto = conceptoBO.getConcepto();
                      
                      ExistenciaAlmacen almacenExists = null;
                    
                    try{
                        
                        almacenExists = new ExistenciaAlmacen();
                        almacenExists.setIdAlmacen(alm.getIdAlmacen());
                        almacenExists.setIdConcepto(conceptoDto.getIdConcepto());
                        almacenExists.setExistencia(conceptoDto.getNumArticulosDisponibles());
                        almacenExists.setEstatus(1);      
                        
                       
                        almdao.insert(almacenExists); 
                       

                    }catch(Exception e){  
                        System.out.println("Almacen o concepto no encontrado");
                        e.printStackTrace();
                    }
                      
                      
                  }
                  
                  
              }

           }
      }catch(Exception e){}
        
    }
    
}
