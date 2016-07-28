package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.dto.Empleado;
import com.tsp.sct.dao.dto.EmpresaPermisoAplicacion;
import com.tsp.sct.dao.dto.ReporteConfigurable;
import com.tsp.sct.dao.dto.TipoReporte;
import com.tsp.sct.dao.dto.Usuarios;
import com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl;
import java.sql.Connection;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.mail.TspMailBO;
import com.tsp.sct.util.DateManage;
import com.tsp.sct.util.FileManage;
import com.tsp.sct.util.StringManage;
import com.tsp.sgfens.report.ReportBO;
import com.tsp.sgfens.report.pdf.ReportExportablePDF;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 *
 * @author leonardo
 */
public class ReporteConfigurableAutomatizacionBO {
    
    private Connection conn = null;

    public ReporteConfigurableAutomatizacionBO(Connection conn) {
        this.conn = conn;
    }

    
    
    public void verificadorDeEmpresaYReportesAEnviar(){
        //RECUPERAMOS TODAS LOS REPORTES QUE VAYAN A SER EJECUTADOS:
        ReporteConfigurable[] reportes = new ReporteConfigurableBO(getConn()).findReporteConfigurables(0, 0, 0, 0, " AND ID_ESTATUS=1");
        
        //String parametrosCustom = "";
        String infoTitle;
        
       
        
        for(ReporteConfigurable report : reportes){
            
           
            infoTitle = "";
            if(report.getIdTipoReporte() > 0){
                //VALIDAMOS SI EL DIA DE HOY SE ENVIARA UN REPORTE
                boolean enviarHoy = enviarReporteHoy(report.getIdDias(), report.getFiltros());
                if(enviarHoy){//si toca enviar hoy el reporte
                    System.out.println("---------------------Conf   " + report.getIdConfiguracion() );
                    System.out.println("---------------------idtipoReport   " + report.getIdTipoReporte() );
                    
                    Usuarios usuario = new UsuariosBO(getConn()).getUsuariosByEmpresa(report.getIdEmpresa())[0];
                    UsuarioBO user = new UsuarioBO(getConn(), usuario.getIdUsuarios());

                    ByteArrayOutputStream bPDF = new ByteArrayOutputStream();

                    ReportExportablePDF toPdf = new ReportExportablePDF(getConn());
                    toPdf.setUser(user);
                    String params = "";
                    String paramsExtra = "";

                    // Filtro Fecha de consulta max_tiempo_atras
                    Calendar diaMin = Calendar.getInstance();
                    if (StringManage.getValidString(report.getMaxTiempoAtras()).length()>0){
                        //Los valores posibles son (D-1, S-1, M-1)
                        // D: Dias, S: Semanas, M: Meses
                        // el segundo dato corresponde al numero
                        String[] data = report.getMaxTiempoAtras().split("-");
                        int restarDias = 0;
                        if (data.length==2){ //debe tener 2 datos
                            try{
                                int valorInt = Integer.parseInt(data[1]);
                                if (data[0].equals("D")){
                                    restarDias = -1 * valorInt;
                                }else if (data[0].equals("S")){
                                    restarDias = -1 * valorInt * 7; //1 semana = 7 dias
                                }else if (data[0].equals("M")){
                                    restarDias = -1 * valorInt * 30; // 1 mes = 30 días
                                }
                            }catch(Exception ex){
                                ex.printStackTrace();
                            }
                        }
                        diaMin.add(Calendar.DAY_OF_MONTH, restarDias);
                    }else{
                        //por defecto, se aplica filtro de 1 día antes
                        diaMin.add(Calendar.DAY_OF_MONTH, -1);
                    }
                    //params = " AND FECHA_X >= '"+DateManage.formatDateToSQL(diaMin.getTime())+"' ";
                    if(report.getIdTipoReporte() == ReportBO.CLIENTE_REPORT){
                        params += " AND FECHA_REGISTRO >= '"+DateManage.formatDateToSQL(diaMin.getTime())+"' ";
                    }else if(report.getIdTipoReporte() == ReportBO.COTIZACION_REPORT){
                        params += " AND FECHA_COTIZACION >= '"+DateManage.formatDateToSQL(diaMin.getTime())+"' ";
                    }else if(report.getIdTipoReporte() == ReportBO.FACTURA_REPORT){
                        params = " AND (ID_TIPO_COMPROBANTE = 2 OR ID_TIPO_COMPROBANTE = 41) AND (ID_ESTATUS = 3 OR ID_ESTATUS = 4) ";
                        params += " AND FECHA_CAPTURA >= '"+DateManage.formatDateToSQL(diaMin.getTime())+"' ";
                    }else if(report.getIdTipoReporte() == ReportBO.FACTURA_NOMINA_REPORT){
                        params = " AND ID_TIPO_COMPROBANTE = 40 AND (ID_ESTATUS = 3 OR ID_ESTATUS = 4) ";
                        params += " AND FECHA_CAPTURA >= '"+DateManage.formatDateToSQL(diaMin.getTime())+"' ";
                    }else if(report.getIdTipoReporte() == ReportBO.CLIENTE_SIN_COMPRA_REPORT){
                        
                        params += " AND FECHA_PEDIDO >= '"+DateManage.formatDateToSQL(diaMin.getTime())+"' ";
                        
                        /*
                        //EXTRAEMOS EL FILTRO DE FECHAS:
                        StringTokenizer tokens = new StringTokenizer(report.getFiltros(),",");
                        String inicialFec = tokens.nextToken().intern().trim(); //"30/05/2014";
                        String finalFec = tokens.nextToken().intern().trim(); //"15/07/2014";

                        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
                        Date fechaIni = null;
                        Date fechaFin = null;
                        try {
                            fechaIni = formatoDelTexto.parse(inicialFec);
                        }catch(Exception e){}
                        try{
                            fechaFin = formatoDelTexto.parse(finalFec);
                        }catch(Exception e){}
                        if(fechaIni != null && fechaFin != null){
                            System.out.println("-------------- AMBAS FECHAS, FINAL E INICIAL: "+fechaIni+", "+fechaFin);
                            SimpleDateFormat formatoDelTexto2 = new SimpleDateFormat("yyyy-MM-dd");
                            params = " AND FECHA_PEDIDO BETWEEN '"+ formatoDelTexto2.format(fechaIni) + "' AND '" + formatoDelTexto2.format(fechaFin) + " 23:59:59.99' ";
                        }else if(fechaIni != null){
                            System.out.println("-------------- FECHA INICIO: "+fechaIni);
                            SimpleDateFormat formatoDelTexto2 = new SimpleDateFormat("yyyy-MM-dd");
                            params = " AND FECHA_PEDIDO >= '"+formatoDelTexto2.format(fechaIni)+"' ";
                        }else if(fechaFin != null){
                            System.out.println("-------------- FECHA FIN: "+fechaFin);
                            SimpleDateFormat formatoDelTexto2 = new SimpleDateFormat("yyyy-MM-dd");
                            params = " AND FECHA_PEDIDO <= '"+formatoDelTexto2.format(fechaFin)+"' ";
                        }     
                        */
                    }else if(report.getIdTipoReporte() == ReportBO.COBRANZA_ABONO_REPORT){
                        
                        params += " AND FECHA_ABONO >= '"+DateManage.formatDateToSQL(diaMin.getTime())+"' ";
                        
                        //EXTRAEMOS EL FILTRO DE FECHAS:
                        StringTokenizer tokens = new StringTokenizer(report.getFiltros(),",");
                        String inicialFec = tokens.nextToken().intern().trim(); //"30/05/2014";
                        String finalFec = tokens.nextToken().intern().trim(); //"15/07/2014";
                        int idVendedor = -1;
                        int idCliente = -1;
                        try{
                            idVendedor = tokens.hasMoreTokens()!=false?Integer.parseInt(tokens.nextToken().intern().trim()):-1;
                        }catch(Exception e){}
                        
                        try{
                            idCliente = tokens.hasMoreTokens()!=false?Integer.parseInt(tokens.nextToken().intern().trim()):-1;
                        }catch(Exception e){}

                        /*
                        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
                        Date fechaIni = null;
                        Date fechaFin = null;
                        try {
                            fechaIni = formatoDelTexto.parse(inicialFec);
                        }catch(Exception e){} 
                        try{
                            fechaFin = formatoDelTexto.parse(finalFec);           
                        }catch(Exception e){}
                        if(fechaIni != null && fechaFin != null){
                            System.out.println("-------------- AMBAS FECHAS, FINAL E INICIAL: "+fechaIni+", "+fechaFin);
                            SimpleDateFormat formatoDelTexto2 = new SimpleDateFormat("yyyy-MM-dd");
                            params = " AND FECHA_ABONO BETWEEN '"+ formatoDelTexto2.format(fechaIni) + "' AND '" + formatoDelTexto2.format(fechaFin) + " 23:59:59.99' ";
                        }else if(fechaIni != null){
                            System.out.println("-------------- FECHA INICIO: "+fechaIni);
                            SimpleDateFormat formatoDelTexto2 = new SimpleDateFormat("yyyy-MM-dd");
                            params = " AND FECHA_ABONO >= '"+formatoDelTexto2.format(fechaIni)+"' ";
                        }else if(fechaFin != null){
                            System.out.println("-------------- FECHA FIN: "+fechaFin);
                            SimpleDateFormat formatoDelTexto2 = new SimpleDateFormat("yyyy-MM-dd");
                            params = " AND FECHA_ABONO <= '"+formatoDelTexto2.format(fechaFin)+ "' ";
                        }  
                        */
                        
                        if (idCliente>0){            
                            params += " AND ID_CLIENTE="+idCliente+"";
                            Cliente cli = new ClienteBO(idCliente, this.conn).getCliente();
                            infoTitle += "Cliente : ";
                            infoTitle += cli.getNombreCliente()!=null?cli.getNombreCliente():"" + " " + cli.getApellidoPaternoCliente()!=null?cli.getApellidoPaternoCliente():"" + " " + cli.getApellidoMaternoCliente()!=null?cli.getApellidoMaternoCliente():"";
                        }
                        if (idVendedor>0){            
                            params += " AND ID_USUARIO_VENDEDOR="+idVendedor+"";
                            try {
                                Empleado emp = new EmpleadoBO(this.conn).findEmpleadoByUsuario(idVendedor);
                                infoTitle += " Vendedor : ";
                                infoTitle += emp.getNombre()!=null?emp.getNombre():"" + " " + emp.getApellidoPaterno()!=null?emp.getApellidoPaterno():"" + " " + emp.getApellidoMaterno()!=null?emp.getApellidoMaterno():"";
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            
                            
                        }                        
                        
                    }else if(report.getIdTipoReporte() == ReportBO.PEDIDO_REPORT){
                        params += " AND FECHA_PEDIDO >= '"+DateManage.formatDateToSQL(diaMin.getTime())+"' ";
                        
                        //EXTRAEMOS EL FILTRO DE FECHAS:
                        StringTokenizer tokens = new StringTokenizer(report.getFiltros(),",");
                        String inicialFec = tokens.nextToken().intern().trim(); //"30/05/2014";
                        String finalFec = tokens.nextToken().intern().trim(); //"15/07/2014";
                        int idVendedor = -1;
                         int idCliente = -1;
                        try{
                            idVendedor = tokens.hasMoreTokens()!=false?Integer.parseInt(tokens.nextToken().intern().trim()):-1;
                        }catch(Exception e){}
                        
                        try{
                            idCliente = tokens.hasMoreTokens()!=false?Integer.parseInt(tokens.nextToken().intern().trim()):-1;
                        }catch(Exception e){}
                        /*
                        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
                        Date fechaIni = null;
                        Date fechaFin = null;
                        try {
                            fechaIni = formatoDelTexto.parse(inicialFec);
                        }catch(Exception e){} 
                        try{
                            fechaFin = formatoDelTexto.parse(finalFec);
                        }catch(Exception e){}
                        if(fechaIni != null && fechaFin != null){
                            System.out.println("-------------- AMBAS FECHAS, FINAL E INICIAL: "+fechaIni+", "+fechaFin);
                            SimpleDateFormat formatoDelTexto2 = new SimpleDateFormat("yyyy-MM-dd");
                            params = " AND FECHA_PEDIDO BETWEEN '"+ formatoDelTexto2.format(fechaIni) + "' AND '" + formatoDelTexto2.format(fechaFin) + " 23:59:59.99' ";
                        }else if(fechaIni != null){
                            System.out.println("-------------- FECHA INICIO: "+fechaIni);
                            SimpleDateFormat formatoDelTexto2 = new SimpleDateFormat("yyyy-MM-dd");
                            params = " AND FECHA_PEDIDO >= '"+formatoDelTexto2.format(fechaIni)+"' ";
                        }else if(fechaFin != null){
                            System.out.println("-------------- FECHA FIN: "+fechaFin);
                            SimpleDateFormat formatoDelTexto2 = new SimpleDateFormat("yyyy-MM-dd");
                            params = " AND FECHA_PEDIDO <= '"+formatoDelTexto2.format(fechaFin)+"' ";
                        }  
                        */
                        
                        if (idCliente>0){            
                            params += " AND ID_CLIENTE='"+idCliente+"'";
                            Cliente cli = new ClienteBO(idCliente, getConn()).getCliente();
                            infoTitle += "Cliente : ";
                            infoTitle += cli.getNombreCliente()!=null?cli.getNombreCliente():"" + " " + cli.getApellidoPaternoCliente()!=null?cli.getApellidoPaternoCliente():"" + " " + cli.getApellidoMaternoCliente()!=null?cli.getApellidoMaternoCliente():"";
                        }
                        if (idVendedor>0){            
                            params += " AND ID_USUARIO_VENDEDOR='"+idVendedor+"'";
                            try {
                                Empleado emp = new EmpleadoBO(getConn()).findEmpleadoByUsuario(idVendedor);
                                infoTitle += " Vendedor : ";
                                infoTitle += emp.getNombre()!=null?emp.getNombre():"" + " " + emp.getApellidoPaterno()!=null?emp.getApellidoPaterno():"" + " " + emp.getApellidoMaterno()!=null?emp.getApellidoMaterno():"";
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }                        
                        
                    }else if(report.getIdTipoReporte() == ReportBO.EMPLEADO_INVENTARIO_REPORT){                        
                        //EXTRAEMOS EL FILTRO DE FECHAS:
                        StringTokenizer tokens = new StringTokenizer(report.getFiltros(),",");
                        String inicialFec = tokens.nextToken().intern().trim(); //"30/05/2014";
                        String finalFec = tokens.nextToken().intern().trim(); //"15/07/2014";
                        int idVendedor = -1;
                      
                        try{
                            idVendedor = tokens.hasMoreTokens()!=false?Integer.parseInt(tokens.nextToken().intern().trim()):-1;
                        }catch(Exception e){e.printStackTrace();}
                        
                        if (idVendedor>0){    
                            EmpleadoBO empBO = new EmpleadoBO(getConn());
                            Empleado empDto = null;
                            try {
                                empDto = empBO.findEmpleadoByUsuario(idVendedor);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            params = " AND empleado_inventario_repartidor.ID_ESTATUS != 2 ";
                            paramsExtra = ""+empDto.getIdEmpleado();
                            
                            try {
                                Empleado emp = new EmpleadoBO(getConn()).findEmpleadoByUsuario(idVendedor);
                                infoTitle += " Vendedor : ";
                                infoTitle += emp.getNombre()!=null?emp.getNombre():"" + " " + emp.getApellidoPaterno()!=null?emp.getApellidoPaterno():"" + " " + emp.getApellidoMaterno()!=null?emp.getApellidoMaterno():"";
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }                        
                        
                    }else if(report.getIdTipoReporte() == ReportBO.EMPLEADO_PRODUCTIVIDAD){
                        params += " AND FECHA_PEDIDO >= '"+DateManage.formatDateToSQL(diaMin.getTime())+"' ";
                        paramsExtra += " AND FECHA_ABONO >= '"+DateManage.formatDateToSQL(diaMin.getTime())+"' ";
                        
                        //EXTRAEMOS EL FILTRO DE FECHAS:
                        StringTokenizer tokens = new StringTokenizer(report.getFiltros(),",");
                        String inicialFec = tokens.nextToken().intern().trim(); //"30/05/2014";
                        String finalFec = tokens.nextToken().intern().trim(); //"15/07/2014";
                        int idVendedor = -1;
                        int idCliente = -1;
                        try{
                            idVendedor = tokens.hasMoreTokens()!=false?Integer.parseInt(tokens.nextToken().intern().trim()):-1;
                        }catch(Exception e){}
                        try{
                            idCliente = tokens.hasMoreTokens()!=false?Integer.parseInt(tokens.nextToken().intern().trim()):-1;
                        }catch(Exception e){}

                        /*
                        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
                        Date fechaIni = null;
                        Date fechaFin = null;
                        try {
                            fechaIni = formatoDelTexto.parse(inicialFec);
                        }catch(Exception e){} 
                        try{
                            fechaFin = formatoDelTexto.parse(finalFec);
                        }catch(Exception e){}
                        
                        if(fechaIni != null && fechaFin != null){
                            System.out.println("-------------- AMBAS FECHAS, FINAL E INICIAL: "+fechaIni+", "+fechaFin);
                            SimpleDateFormat formatoDelTexto2 = new SimpleDateFormat("yyyy-MM-dd");
                            params += " AND FECHA_PEDIDO BETWEEN '"+ formatoDelTexto2.format(fechaIni) + "' AND '" + formatoDelTexto2.format(fechaFin) + " 23:59:59.99' ";
                            paramsExtra += " AND FECHA_ABONO BETWEEN '"+ formatoDelTexto2.format(fechaIni) + "' AND '" + formatoDelTexto2.format(fechaFin) + " 23:59:59.99' ";
                        }else if(fechaIni != null){
                            System.out.println("-------------- FECHA INICIO: "+fechaIni);
                            SimpleDateFormat formatoDelTexto2 = new SimpleDateFormat("yyyy-MM-dd");
                            params += " AND FECHA_PEDIDO >= '"+formatoDelTexto2.format(fechaIni)+"' ";
                            paramsExtra += " AND FECHA_ABONO >= '"+formatoDelTexto2.format(fechaIni)+"' ";
                        }else if(fechaFin != null){
                            System.out.println("-------------- FECHA FIN: "+fechaFin);
                            SimpleDateFormat formatoDelTexto2 = new SimpleDateFormat("yyyy-MM-dd");
                            params += " AND FECHA_PEDIDO <= '"+formatoDelTexto2.format(fechaFin)+"' ";
                            paramsExtra += " AND FECHA_ABONO <= '"+formatoDelTexto2.format(fechaFin)+"' ";
                        }  
                        */
                        
                        params += " AND ID_ESTATUS_PEDIDO <> 3 ";

                        if (idVendedor>0){                
                            params += " AND ID_USUARIO_VENDEDOR = " + idVendedor;     
                            try {
                                Empleado emp = new EmpleadoBO(getConn()).findEmpleadoByUsuario(idVendedor);
                                infoTitle += " Vendedor : ";
                                infoTitle += emp.getNombre()!=null?emp.getNombre():"" + " " + emp.getApellidoPaterno()!=null?emp.getApellidoPaterno():"" + " " + emp.getApellidoMaterno()!=null?emp.getApellidoMaterno():"";
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }                   
                        
                    } else if(report.getIdTipoReporte() == ReportBO.EXISTENCIA_ALMACENES_REPORT){
                        
                    }
                    
                    

                    /*
                    * Imagen Logo para reporte
                    */
                    //Recuperar archivo de imagen logo de la empresa (usuario en sesion)
                    try{
                        File fileImagenPersonal = new ImagenPersonalBO(getConn()).getFileImagenPersonalByEmpresa(report.getIdEmpresa());
                        if (fileImagenPersonal!=null)
                            toPdf.setFileImageLogo(fileImagenPersonal);

                    }catch(Exception eIm){toPdf.setFileImageLogo(null);}
                    try {
                        
                        System.out.println("--------------- GENERANDO REPORTE DE TIPO: " + report.getIdTipoReporte() + " . . . ");
                        bPDF = toPdf.generarReporte(report.getIdTipoReporte(), params,paramsExtra,infoTitle);
                        System.out.println("---------------" + " . . . FIN DE GENERACION DE REPORTE DE TIPO: " + report.getIdTipoReporte());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    if (bPDF!=null && bPDF.size()>0){
                        String nombreArchivo = "";
                        TipoReporte tipoReporte = new TipoReporteBO(report.getIdTipoReporte(), getConn()).getTipoReporte();
                        try {
                            nombreArchivo += (tipoReporte.getDescripcion()+((new Date()).getTime()))+".pdf";
                            File file = FileManage.createFileFromByteArray(bPDF.toByteArray(), "C:\\temp\\", nombreArchivo);

                            try{
                                System.out.println("--------------- ENVIANDO REPORTE DE TIPO: " + report.getIdTipoReporte());
                                mensajeCorreoGeocerca("Reporte autogenerado de: "+tipoReporte.getDescripcion(), report.getCorreos(), file,report.getIdEmpresa());
                                System.out.println("--------------- CORREO ENVIADO REPORTE DE TIPO: " + report.getIdTipoReporte());
                            }catch(Exception e){
                               e.printStackTrace();;
                            }

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }else{
                        System.out.println("_________El reporte que se intenta descargar esta vacío. Intente con otro filtro de busqueda.");
                    }
                }
            }
        }
    }
    
    public boolean enviarReporteHoy(String diasEnviar, String filtros){
        boolean enviarHoy = false;        
        //Domingo = 1   //Lunes = 2     //Martes = 3    //Miercoles = 4     //jueves = 5    //viernes = 6   //sabado = 7  //todos los dias = 8
        
        Calendar c = Calendar.getInstance(); //OBTENGO LA FECHA DE HOY                    
        //c.add(Calendar.DATE, 0);//Manaña
        Date date = c.getTime();//OBTENGO EL TIEMPO  
        //date.getDay();
        
        StringTokenizer tokens = new StringTokenizer(diasEnviar,",");
        String seleccion;
        int diasConteo;
        while (tokens.hasMoreTokens()) {                
            seleccion = tokens.nextToken().intern().trim();
            diasConteo = Integer.parseInt(seleccion);
            //diasConteo = diasConteo - 1;
            //if(diasConteo > -1 && diasConteo < 7){//si el día es mayor a 0, que comienza a partir del domingo = 0
            if(diasConteo > 0 && diasConteo < 8){//si el día es mayor a 0, que comienza a partir del domingo = 0
                if(diasConteo == c.get(Calendar.DAY_OF_WEEK) ){ // date.getDay()){ //SI ES EL MISMO DIA, SE REALIZA EL ENVIO DE CORREO
                    enviarHoy = true;
                    break;
                }
            }else if(diasConteo == 8){//7){
                enviarHoy = true;
                break;
            }                
        }
        
        
        //Revisamos condiciones de fechas de envio (rango)
        if (enviarHoy && StringManage.getValidString(filtros).length()>0){
            String[] data = filtros.split(",");
            if (data.length>=2){ //si al menos tiene 2 valores (fecha ini, fecha fin)
                String fechaIni = data[0];
                String fechaFin = data[1];
                Date dateIni = DateManage.stringToDate(fechaIni);
                Date dateFin = DateManage.stringToDate(fechaFin);
                
                if (dateIni!=null && dateFin!=null){
                    if ( (!date.after(dateIni) && !DateManage.isOnDate(date, dateIni)) 
                        || (!date.before(dateFin) && !DateManage.isOnDate(date, dateFin)) 
                        ){
                        enviarHoy = false;
                    }
                }else if (dateIni!=null && dateFin==null){
                    if (!date.after(dateIni) && !DateManage.isOnDate(date, dateIni)){
                        enviarHoy = false;
                    }
                }else if (dateIni==null && dateFin!=null){
                    if (!date.before(dateFin) && !DateManage.isOnDate(date, dateFin)){
                        enviarHoy = false;
                    }
                }
                
            }
        }
        
        return enviarHoy;
    }
    
        /**
     * Envia un correo usando la plantilla básica para Reestablecimiento
     * @param usuario
     * @param contenidoMail
     * @return
     */
    public boolean mensajeCorreoGeocerca(String contenidoMail, String correos, File file, int idEmpresa){
        
        //Validamos si es clienete de EVC
        EmpresaPermisoAplicacion empresaPermisoDto = null;
        try{
            EmpresaPermisoAplicacionDaoImpl  empresaPermisoDao = new EmpresaPermisoAplicacionDaoImpl(getConn());
            empresaPermisoDto = empresaPermisoDao.findWhereIdEmpresaEquals(idEmpresa)[0];
        }catch(Exception e){e.printStackTrace();}
        
        boolean exito = false;
        try {
            TspMailBO mail = new TspMailBO();
            if(empresaPermisoDto!=null && empresaPermisoDto.getAccesoSgfensPretomovil()==1){
                mail.setConfigurationMovilpyme();
                mail.addMessageMovilpyme(contenidoMail,1);
            }else{
                mail.setConfiguration();
                mail.addMessage(contenidoMail,1);
            }
            
            try {
                StringTokenizer tokens = new StringTokenizer(correos,",");
		while (tokens.hasMoreTokens()) {
                    String correoContacto = tokens.nextToken().intern().trim();
                    mail.addTo(correoContacto, correoContacto);
		}
                
            }catch(Exception e){e.printStackTrace();}
            mail.setFrom(mail.getUSER(), mail.getFROM_NAME());            
            mail.send("Reporte programado. ");
            mail.addFile(file.getPath(), file.getName());
            exito=true;
        } catch (Exception ex) {
            System.out.println("No se pudo enviar el correo del Reporte automático. Error: "+ ex.getMessage());
            exito=false;
        }

        return exito;
    }
    
    
    
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
