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

import com.tsp.sct.bo.EmpleadoBO;
import com.tsp.sct.bo.GeocercaBO;
import com.tsp.sct.bo.GeocercaNotificacionBO;
import com.tsp.sct.dao.dto.Empleado;
import com.tsp.sct.dao.dto.Geocerca;
import com.tsp.sct.dao.dto.GeocercasNotificaciones;
import com.tsp.sct.dao.dto.MovilMensaje;
import com.tsp.sct.dao.exceptions.EmpleadoDaoException;
import com.tsp.sct.dao.exceptions.MovilMensajeDaoException;
import com.tsp.sct.dao.jdbc.EmpleadoDaoImpl;
import com.tsp.sct.dao.jdbc.MovilMensajeDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.util.ConvertidorCoordenadasVerificadorPoligonos;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

//@autor Henry Joe Wong Urquiza

//La clase que tiene la tarea debe de implementar de la clase Job de Quartz
//ya que el programador de la tarea va a buscar una clase que implemente de ella
//y buscara el metodo execute para ejecutar la tarea
public class TareaInvocar implements Job {

  //Metodo que se ejecutara cada cierto tiempo que lo programemos despues
  public void execute(JobExecutionContext jec) throws JobExecutionException {
    //Aca pueden poner la tarea o el job que desean automatizar
    //Por ejemplo enviar correo, revisar ciertos datos, etc
    SimpleDateFormat formato = new SimpleDateFormat("hh:mm:ss");
    System.out.println( "Tarea invocada a la hora: " + formato.format(new Date()));
    
    ///**invocamos la actualizacion y descarga:
    // testInsertadoActualizacionPrestoFastS2Credit credit = new testInsertadoActualizacionPrestoFastS2Credit();
    //credit.InicioInsertadoActualizacion();
    System.out.println("BUSQUEDA DE GEOCERCAS . . . "+ new Date());
    //obtenemos todas las geocercas
    Geocerca[] geocercas = new GeocercaBO(this.conn).findGeocercas(0, 0, 0, 0, " AND ID_ESTATUS = 1 ");
    ConvertidorCoordenadasVerificadorPoligonos validador = null;
    
                //recuperamos la hora actual:
                DateFormat dateFormat = new SimpleDateFormat ("hh:mm");
                Calendar c1 = Calendar.getInstance();
                int horaActual, minutosActual;
                horaActual =c1.get(Calendar.HOUR_OF_DAY);
                minutosActual = c1.get(Calendar.MINUTE);
                String horaActual2 = horaActual+":"+minutosActual;
                Date dateHoraActual = null;
                Date dateHoraInicial = null;
                Date dateHoraFinal = null;
                
                try{
                    dateHoraActual = dateFormat.parse(horaActual2);
                }catch(Exception e){}
    
    for(Geocerca geo : geocercas){
        Empleado[] empleados = null;
        try {
            //obtenemos todos los empleados que tienen las geocerca asignada
            empleados = new EmpleadoDaoImpl(this.conn).findWhereIdGeocercaEquals(geo.getIdGeocerca());
        } catch (EmpleadoDaoException ex) {
            ex.printStackTrace();
        }
        
        //vemos si esta dentro de la geocerca aun:
        validador = new ConvertidorCoordenadasVerificadorPoligonos();
        validador.ordenadorCoordenadas(geo.getCoordenadas(), geo.getTipoGeocerca());
        String cuerpoCorreo = "";
        boolean hayEmpleados = false;
        for(Empleado emp : empleados){
            if(emp.getLatitud() > 0 ){                
                //boolean contenido = validador.puntoContenidoEnPoligono(String.valueOf(emp.getLatitud()), String.valueOf(emp.getLongitud()), geo.getTipoGeocerca());
                boolean contenido = true;
                
                ///**para validar si tiene un rango de horario que aplique la geocerca
                if(emp.getHoraInicio() != null && emp.getHoraFin() != null){
                    try{
                        dateHoraInicial = dateFormat.parse(emp.getHoraInicio()+"");
                        dateHoraFinal = dateFormat.parse(emp.getHoraFin()+"");                        
                        if(dateHoraActual.after(dateHoraInicial) && dateHoraActual.before(dateHoraFinal)){
                            //System.out.println("LA HORA ACTUAL ESTA EN EL INTERVALO DE TIEMPO");
                            //como esta dentro del horario, se verifica si esta dentro o fuera de la geocerca
                            contenido = validador.puntoContenidoEnPoligono(String.valueOf(emp.getLatitud()), String.valueOf(emp.getLongitud()), geo.getTipoGeocerca());
                        }                        
                    }catch(Exception e){}                    
                }else{
                    //como no tiene horario de aplicacion de geocerca, se valida si esta dentro o fuera de la misma
                    contenido = validador.puntoContenidoEnPoligono(String.valueOf(emp.getLatitud()), String.valueOf(emp.getLongitud()), geo.getTipoGeocerca());
                }                
                ///**
                
                //validamos si se salio de la geocerca; de ser asi mandamos mensajes para alertar
                if(!contenido){
                    hayEmpleados = true;
                    MovilMensaje mensaje = new MovilMensaje();
                    try {
                        //Armamos el mensaje para la consola:

                        mensaje.setEmisorTipo(2);
                        mensaje.setReceptorTipo(2);
                        mensaje.setFechaEmision(new Date());
                        mensaje.setMensaje("El empleado: "+emp.getNombre() +" "+emp.getApellidoPaterno() + " "+emp.getApellidoMaterno() +", Salio de su Geocerca ");
                        mensaje.setRecibido(0);
                        mensaje.setIdEmpleadoReceptor(0);
                        mensaje.setIdEmpleadoEmisor(emp.getIdEmpresa());
                        new MovilMensajeDaoImpl().insert(mensaje);
                    } catch (MovilMensajeDaoException ex) {
                        ex.printStackTrace();
                    }
                        //Armamos el mensaje para el movil:
                        MovilMensaje mMovilMensajeDto = new MovilMensaje();
                        MovilMensajeDaoImpl mMovilMensajesDaoImpl = new MovilMensajeDaoImpl(this.conn);

                        int idMovilMensajeNuevo;             

                        mMovilMensajeDto.setEmisorTipo(2);
                        mMovilMensajeDto.setIdEmpleadoEmisor(0);
                        mMovilMensajeDto.setReceptorTipo(1);
                        mMovilMensajeDto.setIdEmpleadoReceptor(emp.getIdEmpleado());
                        mMovilMensajeDto.setFechaEmision(new Date());
                        mMovilMensajeDto.setFechaRecepcion(null);
                        mMovilMensajeDto.setMensaje("Salio de su área de trabajo");
                        mMovilMensajeDto.setRecibido(0);

                        /**
                         * Realizamos el insert
                         */
                        try {
                            mMovilMensajesDaoImpl.insert(mMovilMensajeDto);
                        } catch (MovilMensajeDaoException ex) {
                            ex.printStackTrace();
                        }

                        System.out.println("*****************************"+mensaje.getMensaje() +", La geocerca es ID: "+geo.getIdGeocerca());
                        cuerpoCorreo += "<br/>";
                        cuerpoCorreo += mensaje.getMensaje()+", La geocerca es ID: "+geo.getIdGeocerca();
                        cuerpoCorreo += "<br/>";
                }else{
                    System.out.println("*****************************El empleado "+emp.getNombre()+" "+emp.getApellidoPaterno()+" "+emp.getApellidoMaterno()+" esta dentro de su geocerca con ID: "+geo.getIdGeocerca());
                }
                
            }
        }
        if(hayEmpleados){
            //Armamos la notificación por correo electrónico:
            GeocercasNotificaciones geoNotifi = null;
            try {
                geoNotifi = new GeocercaNotificacionBO(this.conn).getGeocercasNotificacionesGenericoByEmpresa(geo.getIdEmpresa());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            GeocercaBO geocercaBO = new GeocercaBO();
            if(geoNotifi != null)
                geocercaBO.mensajeCorreoGeocerca(cuerpoCorreo, geoNotifi.getCorreos());
        }
    }  
  }
  
  private Connection conn = null;
   
  public Connection getConn() {
        if (this.conn==null){
            try {
                this.conn = ResourceManager.getConnection();
            } catch (SQLException ex) {}
        }else{
            try {
                if (this.conn.isClosed()){
                    this.conn = ResourceManager.getConnection();
                }
            } catch (SQLException ex) {}
        }
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
}
