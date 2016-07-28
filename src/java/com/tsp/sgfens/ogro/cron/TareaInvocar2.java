/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.ogro.cron;

/**
 *
 * @author leonardo
 */
import com.tsp.sct.bo.ReporteConfigurableAutomatizacionBO;
import com.tsp.sct.dao.jdbc.ResourceManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

//@autor Henry Joe Wong Urquiza

//La clase que tiene la tarea debe de implementar de la clase Job de Quartz
//ya que el programador de la tarea va a buscar una clase que implemente de ella
//y buscara el metodo execute para ejecutar la tarea
public class TareaInvocar2 implements Job {

  //Metodo que se ejecutara cada cierto tiempo que lo programemos despues
  @Override
  public void execute(JobExecutionContext jec) throws JobExecutionException {
    //Aca pueden poner la tarea o el job que desean automatizar
    //Por ejemplo enviar correo, revisar ciertos datos, etc
    SimpleDateFormat formato = new SimpleDateFormat("hh:mm:ss");
    System.out.println( "Tarea invocada a la hora: " + formato.format(new Date()));
    
    ///**invocamos la actualizacion y descarga:
     System.out.println("________GENERANDO REPORTES AUTOMATICOS");
    ///**
     
    ReporteConfigurableAutomatizacionBO reBO = new ReporteConfigurableAutomatizacionBO(getConn());
    reBO.verificadorDeEmpresaYReportesAEnviar();
     
     
     
  }
  
  private Connection conn = null;

    public Connection getConn() {
        if (this.conn == null) {
            try {
                this.conn = ResourceManager.getConnection();
            } catch (SQLException ex) {
            }
        } else {
            try {
                if (this.conn.isClosed()) {
                    this.conn = ResourceManager.getConnection();
                }
            } catch (SQLException ex) {
            }
        }
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
}
