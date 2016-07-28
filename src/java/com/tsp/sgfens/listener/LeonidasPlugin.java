/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.listener;

import com.tsp.sgfens.ogro.cron.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author leonardo
 */
public class LeonidasPlugin implements ServletContextListener{

    ServletContext context;
    Programacion programa1 = null;
    Programacion2 programa2 = null;
    Programacion3 programa3 = null;
    Programacion5 programa5 = null;
    ProgramacionBafar1 programaBafar1 = null;
    
    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {        
        context = contextEvent.getServletContext();
        // set variable to servlet context
        //context.setAttribute("TEST", "TEST_VALUE");
        
        
        //inicializamos Cron Bafar 1
        //Tareas incluidas:
        //   - consulta de dispersiones del dia anterior
        try {
            System.out.println("CARGANDO CRON DE BAFAR-SAP 1");
            programaBafar1 = new ProgramacionBafar1();
            programaBafar1.iniciarTarea();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent contextEvent) {
        context = contextEvent.getServletContext();
        
        System.out.println("FINALIZANDO CRON BAFAR SAP 1");
        if (programaBafar1!=null){
            try{
                programaBafar1.terminarTarea();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
    }
    
}
