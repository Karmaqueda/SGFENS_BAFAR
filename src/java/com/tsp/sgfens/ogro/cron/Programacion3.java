/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ogro.cron;

import com.tsp.sct.config.Configuration;
import java.util.Date;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author 578
 */
public class Programacion3 {
   //El horario de la tarea
    private Scheduler horario;

    //Metodo que crea el horario
    private void crearProgramacio() {

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
            this.crearProgramacio();
        }
        //Informacion sobre la tarea
        //La informacion que se pide un nombre, a que grupo pertenece 
        //y a que clase que implemente de Job va a ser llamado
        JobDetail job = new JobDetail("TareaAutomatizar3", null, TareaInvocar3.class);
        //Creamos un trigger y le decimos cada cuanto queremos que se invoque la tarea o Job
        //Tenemos los metodos como los siguientes
        //TriggerUtils.makeSecondlyTrigger(tiempo) --> Para invocar una tarea cada cierto segundos
        //TriggerUtils.makeMinutelyTrigger(tiempo) --> Para invocar una tarea cada cierto minutos
        //TriggerUtils.makeHourlyTrigger(tiempo) --> Para invocar una tarea cada cierta hora
        //TriggerUtils.makeWeeklyTrigger(dayOfWeek, hour, minute); -> Dia de la semana
        //Entre otros mas que pueden revisar en la documentacion
        
        //CARGAMOS LA CONFIGURACION
        Configuration configuration = new Configuration();        
        
        System.out.println("CADA TIEMPO A EJECUTAR CRON DE TAREAS AUTOMATICAS, hora: "+configuration.getHoraCronTareas() +"minuto: "+configuration.getMinutoCronTareas());
        //Trigger trigger = TriggerUtils.makeDailyTrigger(configuration.getHora(),configuration.getMinuto());
        Trigger trigger = TriggerUtils.makeDailyTrigger(configuration.getHoraCronTareas(),configuration.getMinutoCronTareas());
        //En que momento va a iniciar la tarea
        trigger.setStartTime(new Date());
        //El nombre del trigger que debe ser unico
        trigger.setName("CronTareasAutomaticos");
        try {
            this.horario.scheduleJob(job, trigger);
        } catch (SchedulerException ex) {
            System.out.println(ex.getMessage());
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
