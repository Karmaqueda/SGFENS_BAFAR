/*
 * To change this template, choose DateManage | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author ISC César Ulises Martínez García
 */
public class DateManage {

    /**
     * Método para convertir de una fecha java.util.Date a una fecha en formato
     * XMLGregorianCalendar contenida en javax.xml.datatype.XMLGregorianCalendar
     * @param dateToTransform
     * @return
     * @throws DatatypeConfigurationException
     */
    public static XMLGregorianCalendar dateToXMLGregorianCalendar(java.util.Date dateToTransform) throws DatatypeConfigurationException{
        XMLGregorianCalendar dateXMLGregorian = javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar();
        GregorianCalendar cal = new GregorianCalendar();

        cal.setTime(dateToTransform);
        dateXMLGregorian.setDay(cal.get(Calendar.DATE));
        dateXMLGregorian.setMonth(cal.get(Calendar.MONTH) + 1);
        dateXMLGregorian.setYear(cal.get(Calendar.YEAR));
        dateXMLGregorian.setHour(cal.get(Calendar.HOUR_OF_DAY));
        dateXMLGregorian.setMinute(cal.get(Calendar.MINUTE));
        dateXMLGregorian.setSecond(cal.get(Calendar.SECOND));

        return dateXMLGregorian;
    }
    
    public static XMLGregorianCalendar dateToXMLGregorianCalendar2(java.util.Date dateToTransform) throws DatatypeConfigurationException{
        XMLGregorianCalendar dateXMLGregorian = javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar();
        GregorianCalendar cal = new GregorianCalendar();

        cal.setTime(dateToTransform);
        dateXMLGregorian.setDay(cal.get(Calendar.DATE));
        dateXMLGregorian.setMonth(cal.get(Calendar.MONTH) + 1);
        dateXMLGregorian.setYear(cal.get(Calendar.YEAR));
        //dateXMLGregorian.setHour(cal.get(Calendar.HOUR_OF_DAY));
        //dateXMLGregorian.setMinute(cal.get(Calendar.MINUTE));
        //dateXMLGregorian.setSecond(cal.get(Calendar.SECOND));

        return dateXMLGregorian;
    }


    /**
     * Método para expresar en String la fecha y hora actual incluyendo hasta milisegundos
     * La cadena retornada tiene el formato: yyyyMMddHHmmssSSS
     * @return String con la fecha y hora con el formato yyyyMMddHHmmssSSS
     */
    public static String getDateHourString(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date date = new Date();
        return dateFormat.format(date);
    }
    
    /**
     * Método para convertir Fecha de JAVA a
     * DateTime para SQL Formato yyyy-MM-dd HH:mm:ss
     */
    public static String dateToSQLDateTime(Date dateTime){
        if (dateTime==null)
            return null;
                    
        java.text.SimpleDateFormat sdf =  new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        return sdf.format(dateTime);
    }
    
    /**
     * Método para convertir Fecha de JAVA a
     * Time para SQL Formato HH:mm:ss
     */
    public static String timeToSQLDateTime(Date time){
        if (time==null)
            return null;
                    
        java.text.SimpleDateFormat sdf =  new java.text.SimpleDateFormat("HH:mm:ss");
        
        return sdf.format(time);
    }
    
    /**
     * Convierte una cadena en formato Time SQL (HH:mm:ss) a Date de JAVA
     * @param timeStr
     */
    public static Date parseTimeSQL(String timeStr) {
    	if (timeStr==null)
    		return null;
    	if (timeStr.trim().equals(""))
    		return null;
    	
    	Date parsedDate = null;
    	try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            parsedDate = dateFormat.parse(timeStr);
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	
    	return parsedDate;
    }
    
    /**
     * Método para expresar en String la fecha indicada
     * La cadena retornada tiene el formato para sentencias SQL: yyyy-MM-dd
     * @return String con la fecha con el formato SQL yyyy-MM-dd
     */
    public static String formatDateToSQL(Date dateTime) {
        if (dateTime==null)
            return null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(dateTime);
    }
    
    /**
     * Método para expresar en String la fecha indicada
     * La cadena retornada tiene el formato de calendario en Mexico: dd/MM/yyyy
     * @return String con la fecha con el formato SQL dd/MM/yyyy
     */
    public static String formatDateToNormal(Date dateTime) {
        if (dateTime==null)
            return null;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(dateTime);
    }
    
    
    

    /**
     * Recibe una cadena de fecha con formato dd/MM/yyyy
     * y regresa el objeto Date correspondiente
     * @return Date
     */
    public static Date stringToDate(String stringTime) {
        try{
            String dia = stringTime.substring(0, 2);//dd
            String mes = stringTime.substring(3, 5);//MM
            String anio = stringTime.substring(6, 10);//yyyy
        

            Calendar cal = new GregorianCalendar();
            cal.set(Integer.parseInt(anio), Integer.parseInt(mes) - 1, Integer.parseInt(dia));
            //Date f = new Date(Integer.parseInt(anio),Integer.parseInt(mes)-1, Integer.parseInt(dia));
            return cal.getTime();
        }catch(Exception ex){
            return null;
        }
    }
    
    /**
     * Recibe un objeto Date y regresa un String con el formato dd 'de' MMMM 'de' yyyy
     * por ejemplo: Martes, 12 de Junio de 2009
     * @param date Date a transformar
     * @return Cadena con formato dd 'de' MMMM 'de' yyyy
     */
    public static String dateToStringEspanol(Date date) {
        String fecha = "";
        SimpleDateFormat formateador = new SimpleDateFormat(
                "EEEE ', ' dd 'de' MMMM 'de' yyyy", new Locale("ES"));
        fecha = formateador.format(date);
        return fecha;
    }
    
    /**
     * Recibe un objeto Date y regresa un String con el formato dd 'de' MMMM 'de' yyyy
     * por ejemplo: 12 de Junio de 2009
     * @param date Date a transformar
     * @return Cadena con formato dd 'de' MMMM 'de' yyyy hh:mm:ss
     */
    public static String dateTimeToStringEspanol(Date date) {
        String fecha = "";
        SimpleDateFormat formateador = new SimpleDateFormat(
                "EEEE ', ' dd 'de' MMMM 'de' yyyy hh':'mm':'ss", new Locale("ES"));
        fecha = formateador.format(date);
        return fecha;
    }
    
    /**
     * Compara si dos Objetos Date corresponden al mismo día
     * sin tomar en cuenta su hora, minuto, segundo, milisegundos...
     * @param date1 Fecha 1
     * @param date2 Fecha 2
     * @return true en caso de Ser el mismo día, false en caso contrario
     */
    public static boolean isOnDate(Date date1, Date date2){
        boolean isOnDate = false;
        
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        
        cal1.setTime(date1);
        cal2.setTime(date2);
        
        if ( cal1.get(Calendar.YEAR)== cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.MONTH)== cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.DAY_OF_MONTH)== cal2.get(Calendar.DAY_OF_MONTH)
                ){
            isOnDate = true;
        }
        
        return isOnDate;
    }
    
    /**
     * Método para expresar en String la fecha indicada
     * La cadena retornada tiene el formato para sentencias SQL: dd/MM/yyyy HH:mm
     * @return String con la fecha con el formato SQL dd/MM/yyyy HH:mm
     */
    public static String formatDateTimeToNormalMinutes(Date dateTime) {
        if (dateTime==null)
            return null;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return dateFormat.format(dateTime);
    }
    /**
     * Método para expresar en String la fecha indicada, con el formato indicado
     * @return String con la fecha con el formato indicado
     */
    public static String formatDate(Date dateTime, String formato) {
        if (dateTime==null)
            return null;
        DateFormat dateFormat = new SimpleDateFormat(formato, new Locale("ES"));
        return dateFormat.format(dateTime);
    }
    
    /**
     * Método para hacer parse de una cadena a Date, con el formato indicado
     * @param dateTime Fecha en cadena
     * @param formato formato que tiene la cadena
     * @return 
     */
    public static Date parseDate(String dateTime, String formato) {
        if (dateTime==null)
            return null;
        DateFormat dateFormat = new SimpleDateFormat(formato, new Locale("ES"));
        Date date = null;
        try{
            date = dateFormat.parse(dateTime);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return date;
    }
    
    
    /**
     * Suma o Resta días a una fecha
     * @param date Fecha 1
     * @param dias numero de dias a sumar o restar a la fecha indicada, usar valores negativos para resta
     * @return
     */
    public static Date sumaRestaDias(Date date, int dias){
        if (date==null)
            return null;
        
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        
        cal1.add(Calendar.DAY_OF_MONTH, dias);
        
        return cal1.getTime();
    }
    
    /**
     * Suma o Resta minutos a una fecha y hr
     * @param date Fecha Hora original
     * @param minutos numero de minutos a sumar o restar a la fecha indicada, usar valores negativos para resta
     * @return
     */
    public static Date sumaRestaMinutos(Date date, int minutos){
        if (date==null)
            return null;
        
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        
        cal1.add(Calendar.MINUTE, minutos);
        
        return cal1.getTime();
    }
    
    public static List<ParDate> obtenPrimerUltimoDiaSemanas(Date fechaBase, int semanasAtras, int semanasAdelante){
        Date fechaMin;
        Date fechaMax;
        
        Calendar cMin = Calendar.getInstance();
        cMin.setTime(fechaBase);
        
        Calendar cMax = Calendar.getInstance();
        cMax.setTime(fechaBase);
        
        cMin.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //movemos a primer dia de la semana LUNES
        if (semanasAtras>0)
            cMin.add(Calendar.WEEK_OF_YEAR, (-1 * semanasAtras) ); //Restamos semanas
        fechaMin = cMin.getTime();
        
        cMax.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); //Movemos a ultimo de de la semana DOMINGO
        if (semanasAdelante>0)
            cMax.add(Calendar.WEEK_OF_YEAR, semanasAdelante ); //Sumamos semanas
        fechaMax = cMax.getTime();
        
        return obtenPrimerUltimoDiaSemanas(fechaMin, fechaMax);
    }
    
    /**
     * Obtiene los primeros y ultimos dias de la semana en Par, en un rango de fechas
     * Se utilizara el día Lunes como primer dia de la semana y el Domingo como ultimo
     * @param fechaMin Rango Fecha minima
     * @param fechaMax Rango Fecha maxima
     * @return Lista de Pares de dias, primero y ultimo de cada semana en el rango solicitado
     */
    public static List<ParDate> obtenPrimerUltimoDiaSemanas(Date fechaMin, Date fechaMax){
        List<ParDate> data = new ArrayList<ParDate>();
        
        if (fechaMin==null || fechaMax==null)
            return data;
        
        //Primero obtenemos cada Dia Lunes que se encuentre en el rango de fechas
        List<Date> primerDiaSemanalEnRango = new ArrayList<Date>();
        {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(fechaMin);

            Calendar c2 = Calendar.getInstance();
            c2.setTime(fechaMax);

            while(c2.after(c1)) {
                if(c1.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY) //Usaremos LUNES como primer dia de semana
                    primerDiaSemanalEnRango.add(c1.getTime());
                c1.add(Calendar.DATE,1);
            }
        }
        
        for (Date primerDiaSemana : primerDiaSemanalEnRango){
            ParDate parDate = new ParDate();
            
            //Primer dia de la semana (lunes)
            parDate.setDiaA(primerDiaSemana);
            
            // Asignamos a calendario el primer dia de la semana
            Calendar c = Calendar.getInstance();
            c.setTime(primerDiaSemana);
            // Cambiamos el calendario al Domingo de la semana indicada
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);//Usamos DOMINGO como ultimo dia de semana
            
            //Ultimo día de la semana (domingo)
            parDate.setDiaB(c.getTime());
            
            data.add(parDate);
        }
        
        return data;
    }
    
    public static class ParDate{
    
        Date diaA = null;
        Date diaB = null;

        public Date getDiaA() {
            return diaA;
        }

        public void setDiaA(Date diaA) {
            this.diaA = diaA;
        }

        public Date getDiaB() {
            return diaB;
        }

        public void setDiaB(Date diaB) {
            this.diaB = diaB;
        }        
    
    }
}
