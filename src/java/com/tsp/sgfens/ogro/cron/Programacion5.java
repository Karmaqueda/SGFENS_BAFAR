/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ogro.cron;

//mesografia: http://www.programandoconcafe.com/2011/02/java-automatizacion-de-procesos-o.html

/**
 *
 * @author Leonardo
 */
import com.tsp.sct.config.Configuration;
import java.util.Date;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

// @author Henry Joe Wong Urquiza

public class Programacion5 {
    //El horario de la tarea
    private Scheduler horario;

    //Metodo que crea el horario
    private void crearProgramacion() {

        try {
            //Creando la programacion
            SchedulerFactory factoria = new StdSchedulerFactory();
            //Obteniendo el horario
            horario = factoria.getScheduler();
            //Damos inicio al horario
            horario.start();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
    //Metodo que da la informacion mas detallada sobre el horario, como hora de inicio de la tarea y cada
    //cuanto tiempo se ejecute la tarea
    public void iniciarTarea() {
        if (this.horario == null) {
            this.crearProgramacion();
        }
        //Informacion sobre la tarea
        //La informacion que se pide un nombre, a que grupo pertenece 
        //y a que clase que implemente de Job va a ser llamado
        JobDetail job = new JobDetail("TareaAutomatizarModuloSMS", null, TareaInvocar5.class);
        
        //CARGAMOS LA CONFIGURACION
        Configuration configuration = new Configuration();        
        
        System.out.println("CADA TIEMPO A EJECUTAR CRON DE MODULO SMS, minuto: "+configuration.getMinutoCronModuloSms());
        //Trigger trigger = TriggerUtils.makeDailyTrigger(configuration.getHora(),configuration.getMinuto());
        Trigger trigger = TriggerUtils.makeMinutelyTrigger(configuration.getMinutoCronModuloSms());
        //En que momento va a iniciar la tarea
        trigger.setStartTime(new Date());
        //El nombre del trigger que debe ser unico
        trigger.setName("CronModuloSMS");
        
        try {
            //tarea de Geocercas
            this.horario.scheduleJob(job, trigger);
        } catch (SchedulerException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public void terminarTarea(){
        if (this.horario!=null){
            try{
                horario.shutdown();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    
}