/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.importa;

import com.tsp.sct.bo.UsuarioBO;
import com.tsp.sct.dao.dto.SmsAgendaDestinatario;
import com.tsp.sct.dao.dto.SmsAgendaDestinatarioPk;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.dao.jdbc.SmsAgendaDestinatarioDaoImpl;
import com.tsp.sct.util.GenericValidator;
import com.tsp.sct.util.StringManage;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

/**
 *
 * @author ISCesar
 */
public class ReadDestinatariosSMS {

    private String logActualizacionInsertado = "";    
    public static final int MAX_FILAS_CARGA = 10000;
    //Lista para cargar datos desde excel   
    private List<SmsAgendaDestinatario> listAgendaDestinatarios = new ArrayList<SmsAgendaDestinatario>();
    
    //Columna correspondiente en excel
    private final int NUMERO_CEL = 0;
    private final int NOMBRE = 1;
    private final int CAMPO_EXTRA_1 = 2;
    private final int CAMPO_EXTRA_2 = 3;
    private final int CAMPO_EXTRA_3 = 4;
    private final int CAMPO_EXTRA_4 = 5;

    //Nombres para log
    public static final String NUMERO_CEL_NAME = "NUMERO_CEL";
    public static final String NOMBRE_NAME = "NOMBRE";
    public static final String CAMPO_EXTRA_1_NAME = "CAMPO_EXTRA_1";
    public static final String CAMPO_EXTRA_2_NAME = "CAMPO_EXTRA_2";
    public static final String CAMPO_EXTRA_3_NAME = "CAMPO_EXTRA_3";
    public static final String CAMPO_EXTRA_4_NAME = "CAMPO_EXTRA_4";
    
    private UsuarioBO usuarioBO = null;
    private Connection conn = null;

    public Connection getConn() {
        if (conn==null){
            if (usuarioBO!=null){
                conn = usuarioBO.getConn();
            }else{
                try {
                    conn = ResourceManager.getConnection();
                } catch (SQLException ex) {}
            }
        }else{
            try {
                if (conn.isClosed()){
                    conn = ResourceManager.getConnection();
                }
            } catch (SQLException ex) {}
        }
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public UsuarioBO getUsuarioBO() {
        return usuarioBO;
    }

    public void setUsuarioBO(UsuarioBO usuarioBO) {
        this.usuarioBO = usuarioBO;
    }

    public ReadDestinatariosSMS(UsuarioBO usuarioBO) {
        this.usuarioBO = usuarioBO;
    }
    
    public String leerArchivoExcelDestinatariosSMS(String archivoOrigen, int idEmpresa) {            
        SmsAgendaDestinatario agendaDestinatarioExcel = null;
        
        try{
            
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("ISO-8859-1");
            
            Workbook archivoExcel = Workbook.getWorkbook(new File(archivoOrigen),ws); 
           
            System.out.println("Número de Hojas\t" + archivoExcel.getNumberOfSheets()); 
            for (int sheetNo = 0; sheetNo < archivoExcel.getNumberOfSheets(); sheetNo++) // Recorre cada hoja                                                                                                                                                       
            { 
                
                Sheet hoja = archivoExcel.getSheet(sheetNo); 
                int numColumnas = hoja.getColumns(); 
                int numFilas = hoja.getRows(); 
                String data; 
                String columnName = "";
                
                System.out.println("Nombre de la Hoja\t" + archivoExcel.getSheet(sheetNo).getName()); 
                System.out.println("Filas\t" + numFilas); 
                System.out.println("Columnas\t" + numColumnas); 
              
                if (numFilas > MAX_FILAS_CARGA) {
                    numFilas = MAX_FILAS_CARGA;
                    System.out.println("Se limita a leer " + MAX_FILAS_CARGA + " filas"); 
                }
                 
                for (int fila = 1; fila < numFilas; fila++) { // Recorre cada fila de la hoja 
                    
                    System.out.println("----------------");
                    //Creamos obj                                      
                    agendaDestinatarioExcel =  new SmsAgendaDestinatario();
                    agendaDestinatarioExcel.setIdEstatus(1);
                    agendaDestinatarioExcel.setIdEmpresa(idEmpresa);
                    boolean renglonErrores = false;
                    
                    for (int columna = 0; columna < numColumnas; columna++) { // Recorre cada columna de la fila 
                        
                        //Data =  info dentro de celda
                        data = hoja.getCell(columna,fila ).getContents(); 
                        
                        try{                    
                            validaAsignaCelda(columna, columnName, data, agendaDestinatarioExcel);
                        }catch(Exception e){
                            //Aqui log
                            //e.printStackTrace();
                            renglonErrores = true;
                            logActualizacionInsertado += "No se pudo leer el Registro.  Registro número: "+ (fila+1) +" Columna: " + columnName + ", Error: "+e.getMessage()+" <br/>";            
                        }
                        
                        
                    }//for columnas
                    
                    if (!renglonErrores)
                        listAgendaDestinatarios.add(agendaDestinatarioExcel);
                    
                }//for filas
                
            }//for sheets
            
        }catch (Exception ioe){ 
            ioe.printStackTrace(); 
        }
        
        
        return logActualizacionInsertado;
        
    }
    
    public void validaAsignaCelda(int column, String columnName, String data, SmsAgendaDestinatario agendaDestinatarioTemp) throws Exception{
        GenericValidator gc = new GenericValidator();
        
        switch(column){
            case NUMERO_CEL:
                columnName = NUMERO_CEL_NAME;//para log

                if(gc.isValidString(data, 10, 10)){
                    //if(gc.validarConRegex("[0-9]{10}", data)){
                    if (gc.isCelularMexico(data)){
                        agendaDestinatarioTemp.setNumeroCelular(StringManage.getValidString(data));
                    }else{
                        throw new Exception("For input string: '"+data+"'. Debe contener 10 digitos, y no debe incluir ningun simbolo, puntuación, ni espacio en blanco.");
                    }
                }else{
                    throw new Exception("For input string: '"+data+"'. Debe tener 10 dígitos exactos.");
                }

                break;
            case NOMBRE:
                columnName = NOMBRE_NAME;//para log

                if (!StringManage.getValidString(data).equals("")){
                    if(gc.isValidString(data, 1, 100)){
                        agendaDestinatarioTemp.setNombre(StringManage.getValidString(data));
                    }else{
                        throw new Exception("For input string: '"+data+"'. Máximo 100 caracteres.");
                    }
                }else{
                    agendaDestinatarioTemp.setNombre("");
                }

                break;
            case CAMPO_EXTRA_1:
                columnName = CAMPO_EXTRA_1_NAME;//para log                                    

                if (!StringManage.getValidString(data).equals("")){
                    if(gc.isValidString(data, 1, 100)){
                         agendaDestinatarioTemp.setCampoExtra1(data);
                    }else{
                        throw new Exception("For input string: '"+data+"'. Máximo 100 caracteres.");
                    }
                }else{
                     agendaDestinatarioTemp.setCampoExtra1("");
                }                                     

                break;
            case CAMPO_EXTRA_2:
                columnName = CAMPO_EXTRA_2_NAME;//para log                                    

                if (!StringManage.getValidString(data).equals("")){
                    if(gc.isValidString(data, 1, 100)){
                         agendaDestinatarioTemp.setCampoExtra2(data);
                    }else{
                        throw new Exception("For input string: '"+data+"'. Máximo 100 caracteres.");
                    }
                }else{
                     agendaDestinatarioTemp.setCampoExtra2("");
                }                                     

                break;
            case CAMPO_EXTRA_3:
                columnName = CAMPO_EXTRA_3_NAME;//para log                                    

                if (!StringManage.getValidString(data).equals("")){
                    if(gc.isValidString(data, 1, 100)){
                         agendaDestinatarioTemp.setCampoExtra3(data);
                    }else{
                        throw new Exception("For input string: '"+data+"'. Máximo 100 caracteres.");
                    }
                }else{
                     agendaDestinatarioTemp.setCampoExtra3("");
                }                                     

                break;
            case CAMPO_EXTRA_4:
                columnName = CAMPO_EXTRA_4_NAME;//para log                                    

                if (!StringManage.getValidString(data).equals("")){
                    if(gc.isValidString(data, 1, 100)){
                         agendaDestinatarioTemp.setCampoExtra4(data);
                    }else{
                        throw new Exception("For input string: '"+data+"'. Máximo 100 caracteres.");
                    }
                }else{
                     agendaDestinatarioTemp.setCampoExtra4("");
                }                                     

                break;

        }//switch  
    }
    
    /**
     * Metodo que inserta los registros leidos desde excel
     * @param archivoOrigen
     * @return 
     */
    public String insertaDestinatariosSMSExcel(int idEmpresa, int idSmsAgendaGrupo){
        
        int i = 1; 
        SmsAgendaDestinatarioDaoImpl smsAgendaDestinatarioDao = new SmsAgendaDestinatarioDaoImpl(getConn());
        GenericValidator gc = new GenericValidator();        
        
        for(SmsAgendaDestinatario item : listAgendaDestinatarios){
        
            i ++;
            
            //************INSERTAMOS LOS REGISTROS DE LOS CLIENTES
            try{  
                if (idSmsAgendaGrupo>0)
                    item.setIdSmsAgendaGrupo(idSmsAgendaGrupo);
                
                //insert
                SmsAgendaDestinatarioPk smsAgendaDestinatarioPk = smsAgendaDestinatarioDao.insert(item);

            }catch(Exception ex){
                System.out.println("Problemas al guardar Destinatario SMS." + ex.toString());
                logActualizacionInsertado += "No se pudo insertar / actualizar el regitro del Cliente.  Registro número: "+ i +", Error: "+ex.getMessage()+". <br/>";            
            }
            //************FIN REGISTROS 
        }

        
        return logActualizacionInsertado;
    }

    public String getLogActualizacionInsertado() {
        return logActualizacionInsertado;
    }

    public void setLogActualizacionInsertado(String logActualizacionInsertado) {
        this.logActualizacionInsertado = logActualizacionInsertado;
    }

    public List<SmsAgendaDestinatario> getListAgendaDestinatarios() {
        return listAgendaDestinatarios;
    }

    public void setListAgendaDestinatarios(List<SmsAgendaDestinatario> listAgendaDestinatarios) {
        this.listAgendaDestinatarios = listAgendaDestinatarios;
    }
    
}
