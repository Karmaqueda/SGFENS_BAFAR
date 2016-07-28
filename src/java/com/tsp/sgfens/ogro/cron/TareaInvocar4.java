/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.ogro.cron;

//mesografia: http://www.programandoconcafe.com/2011/02/java-automatizacion-de-procesos-o.html
/**
 *
 * @author ISCesarMartinez
 */
import com.tsp.sct.bo.EmpAsignacionInventarioBO;
import com.tsp.sct.dao.dto.EmpAsignacionInventario;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.util.GenericMethods;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.quartz.InterruptableJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

public class TareaInvocar4 implements Job {

    //Metodo que se ejecutara cada cierto tiempo que lo programemos despues
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        SimpleDateFormat formato = new SimpleDateFormat("hh:mm:ss");
        System.out.println("Tarea invocada a la hora: " + formato.format(new Date()));
        System.out.println("ASIGNACIÓN AUTOMÁTICA DE INVENTARIO A EMPLEADOS. . . " + new Date());

        
        //Buscamos todos los registros en emp_asignacion_inventario
        // que esten activos
        EmpAsignacionInventarioBO empAsignacionInventarioBO = new EmpAsignacionInventarioBO(getConn());
        EmpAsignacionInventario[] asignacionesActivas = 
                empAsignacionInventarioBO.findEmpAsignacionInventarios(-1, -1, 0, 0, 
                    " AND id_estatus=1 AND num_dias_repeticion>0 ");
        
        Date ahora = new Date();
        for (EmpAsignacionInventario asignacionInventario : asignacionesActivas){
            if (asignacionInventario.getUltimaRepFechaHr()!=null){
                
                //Calculamos siguiente ejecución programada de la Asignación
                Calendar calSiguienteEjecucion = Calendar.getInstance();
                calSiguienteEjecucion.setTime(asignacionInventario.getUltimaRepFechaHr());
                calSiguienteEjecucion.add(Calendar.DAY_OF_MONTH, asignacionInventario.getNumDiasRepeticion());
                Date dateSiguienteEjecucion = calSiguienteEjecucion.getTime();
                
                //Si ya pasa de la Fecha/Hr programada o es igual
                if (ahora.compareTo(dateSiguienteEjecucion)>=0){
                    //repetimos la asignacion de inventario
                    System.out.println(" Repitiendo asignación de Inventario a Empleado. ID emp_asignacion_inventario: " + asignacionInventario.getIdAsignacionInventario());
                    empAsignacionInventarioBO.setEmpAsignacionInventario(asignacionInventario);
                    try{
                        empAsignacionInventarioBO.repiteAsignacion();
                    }catch(Exception ex){
                        System.out.println(" Error inesperado al repetir Asignación automática a Empleado: " + ex.toString());
                        ex.printStackTrace();
                    }
                }
                
            }
        }
        
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
