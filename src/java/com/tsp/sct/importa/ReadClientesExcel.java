/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.importa;

import com.tsp.sct.bo.ClienteBO;
import com.tsp.sct.bo.ClienteCampoAdicionalBO;
import com.tsp.sct.bo.EmpresaBO;
import com.tsp.sct.bo.SGClienteVendedorBO;
import com.tsp.sct.bo.UsuarioBO;
import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.dto.ClienteCampoAdicional;
import com.tsp.sct.dao.dto.ClienteCampoContenido;
import com.tsp.sct.dao.dto.ClientePk;
import com.tsp.sct.dao.dto.Concepto;
import com.tsp.sct.dao.dto.Empleado;
import com.tsp.sct.dao.dto.EmpleadoPk;
import com.tsp.sct.dao.dto.EmpresaPermisoAplicacion;
import com.tsp.sct.dao.dto.SgfensClienteVendedor;
import com.tsp.sct.dao.exceptions.EmpleadoDaoException;
import com.tsp.sct.dao.exceptions.EmpresaPermisoAplicacionDaoException;
import com.tsp.sct.dao.jdbc.ClienteCampoContenidoDaoImpl;
import com.tsp.sct.dao.jdbc.ClienteDaoImpl;
import com.tsp.sct.dao.jdbc.ConceptoDaoImpl;
import com.tsp.sct.dao.jdbc.EmpleadoDaoImpl;
import com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import com.tsp.sct.dao.jdbc.SgfensClienteVendedorDaoImpl;
import com.tsp.sct.util.GenericValidator;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

/**
 *
 * @author HpPyme
 */
public class ReadClientesExcel {
    
    
    public String logActualizacionInsertado = "";    
    
    //Lista para cargar datos desde excel   
    //List<Cliente> listClientes = new ArrayList<Cliente>(); 
    List<ClienteYcamposAdicionales> listClientes = new ArrayList<ClienteYcamposAdicionales>();
    
    
    //Columna correspondiente en excel
    private final int RFC = 0;
    private final int RAZON_SOCIAL = 1;
    private final int NOMBRE_COMERCIAL = 2;
    private final int NOMBRE = 3;
    private final int APELLIDO_PATERNO = 4;
    private final int APELLIDO_MATERNO = 5;
    private final int LADA = 6;
    private final int TELEFONO = 7;
    private final int EXTENSION = 8;
    private final int CELULAR = 9;
    private final int CORREO_ELECTRONICO = 10;
    private final int CONTACTO = 11;
    private final int CALLE = 12;
    private final int NUMERO_EXTERIOR = 13;
    private final int NUMERO_INTERIOR = 14;
    private final int COLONIA = 15;
    private final int CODIGO_POSTAL = 16;
    private final int MUNICIPIO_DELEGACION = 17;
    private final int ESTADO = 18;   
    private final int PAIS = 19;
    private final int DIAS_VISITA = 20;
    private final int PERIORICIDAD_VISITA = 21;
    private final int PERMISO_VENTA_CREDITO = 22;
    private final int LATITUD = 23;
    private final int LONGITUD = 24;
    private final int NOMBRE_VENDEDOR = 25;
    private final int CLAVE = 26;
    
    
    //Nombres para log
    public static final String RFC_NAME = "RFC";
    public static final String RAZON_SOCIAL_NAME = "RAZON SOCIAL";
    public static final String NOMBRE_COMERCIAL_NAME = "NOMBRE COMERCIAL";
    public static final String NOMBRE_NAME = "NOMBRE";
    public static final String APELLIDO_PATERNO_NAME = "APELLIDO PATERNO";
    public static final String APELLIDO_MATERNO_NAME = "APELLIDO MATERNO";
    public static final String LADA_NAME = "LADA";
    public static final String TELEFONO_NAME = "TELEFONO";
    public static final String EXTENSION_NAME = "EXTENSION";
    public static final String CELULAR_NAME = "CELULAR";
    public static final String CORREO_ELECTRONICO_NAME = "CORREO ELECTRONICO";
    public static final String CONTACTO_NAME = "CONTACTO";
    public static final String CALLE_NAME = "CALLE";
    public static final String NUMERO_EXTERIOR_NAME = "NUMERO EXTERIOR";
    public static final String NUMERO_INTERIOR_NAME = "NUMERO INTERIOR";
    public static final String COLONIA_NAME = "COLONIA";
    public static final String CODIGO_POSTAL_NAME = "CODIGO POSTAL";
    public static final String MUNICIPIO_DELEGACION_NAME = "MUNICIPIO / DELEGACION";
    public static final String ESTADO_NAME = "ESTADO";   
    public static final String PAIS_NAME = "PAIS";
    public static final String DIAS_VISITA_NAME = "DIAS VISITA";
    public static final String PERIORICIDAD_VISITA_NAME = "PERIORICIDAD VISITA";
    public static final String PERMISO_VENTA_CREDITO_NAME = "PERMISO VENTA CREDITO";
    public static final String LATITUD_NAME = "LATITUD";
    public static final String LONGITUD_NAME = "LONGITUD";
    public static final String NOMBRE_VENDEDOR_NAME = "NOMBRE VENDEDOR";
    public static final String CLAVE_NAME = "CLAVE";
    
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
    
    
    
    
    
    /**
     * Metodo que inserta los registros leidos desde excel
     * @param archivoOrigen
     * @return int Estatus Carga 1- CARGA COMPLETA, 2- CARGA INCOMPLETA (CON ERRORES), 3- OTRO
     */

    public String leerArchivoExcelClientes(String archivoOrigen, int idEmpresa) {
        
        //1 crear lista tipo Cliente importar
        //2 cargar lista con datos leidos del excel     
        //3 Mapear a obj propios e insercion a bd                
             
        //Cliente clienteExcel = null;  
        ClienteYcamposAdicionales clienteExcel = null;
        GenericValidator gc = new GenericValidator();
        
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
                /*int dataInt =0;
                double dataDouble = 0;*/
                
                System.out.println("Nombre de la Hoja\t" + archivoExcel.getSheet(sheetNo).getName()); 
                System.out.println("Filas\t" + numFilas); 
                System.out.println("Columnas\t" + numColumnas); 
              
                 
                for (int fila = 1; fila < numFilas; fila++) { // Recorre cada fila de la hoja 
                    
                    System.out.println("----------------");
                    //Creamos obj cliente                                      
                    //clienteExcel =  new Cliente();
                    clienteExcel =  new ClienteYcamposAdicionales();
                    
                    //campos adicionales:
                    ClienteCampoAdicional[] clienteCampoAdicionalsDto = new ClienteCampoAdicional[0];
                    ClienteCampoAdicionalBO clienteCampoAdicionalBO = new ClienteCampoAdicionalBO(this.conn);
                    
                    try{
                        clienteCampoAdicionalsDto = clienteCampoAdicionalBO.findClienteCampoAdicionals(0, idEmpresa , 0, 0, " AND ID_ESTATUS = 1 ");            
                    }catch(Exception e){}
                    
                    for (int columna = 0; columna < numColumnas; columna++) { // Recorre cada columna de la fila 
                        
                        //Data =  info dentro de celda
                        data = hoja.getCell(columna,fila ).getContents(); 
                        
                        
                       //System.out.println(" |  FILA: " + fila + "   --  Columna:  " + columna + "  ***Data: " + data );                        
                        
                        try{
                            
                            switch(columna){

                                case RFC:
                                    columnName = RFC_NAME;//para log
                                    
                                    if(!data.trim().equals("")){
                                        
                                        if(gc.isRFC(data)){
                                            clienteExcel.setRfcCliente(data);
                                        }else{
                                            clienteExcel.setRfcCliente("AAA010101");
                                        }                                        
                                        
                                    }else{
                                        clienteExcel.setRfcCliente("AAA010101");
                                    }   
                                break;
                                case RAZON_SOCIAL:
                                    columnName = RAZON_SOCIAL_NAME;//para log
                                    
                                    if(!data.trim().equals("")){ 
                                        if(gc.isValidString(data, 1, 200)){
                                            clienteExcel.setRazonSocial(data);
                                        }else{
                                            throw new Exception("For input string: '"+data+"'. Mínimo 1 y máximo 200 caracteres.");
                                        }
                                    }else{
                                         throw new Exception("For input string: '"+data+"'. No puede quedar vacio.");
                                    } 
                                break;
                                case NOMBRE_COMERCIAL:
                                    columnName = NOMBRE_COMERCIAL_NAME;//para log                                    
                                    
                                    if(!data.trim().equals("")){ 
                                        if(gc.isValidString(data, 1, 200)){
                                             clienteExcel.setNombreComercial(data);
                                        }else{
                                            throw new Exception("For input string: '"+data+"'. Mínimo 1 y máximo 200 caracteres.");
                                        }
                                    }else{
                                         clienteExcel.setNombreComercial("");
                                    }                                     
                                    
                                break;
                                    case NOMBRE:
                                    columnName = NOMBRE_NAME;//para log
                                    
                                    if(!data.trim().equals("")){ 
                                        if(gc.isValidString(data, 1, 100)){
                                             clienteExcel.setNombreCliente(data);
                                        }else{
                                            throw new Exception("For input string: '"+data+"'. Mínimo 1 y máximo 100 caracteres.");
                                        }
                                    }else{
                                         clienteExcel.setNombreCliente("");
                                    }
                                    
                                break;
                                case APELLIDO_PATERNO:
                                    columnName = APELLIDO_PATERNO_NAME;//para log
                                    
                                    
                                    if(!data.trim().equals("")){ 
                                        if(gc.isValidString(data, 1, 100)){
                                             clienteExcel.setApellidoPaternoCliente(data);
                                        }else{
                                            throw new Exception("For input string: '"+data+"'. Mínimo 1 y máximo 100 caracteres.");
                                        }
                                    }else{
                                         clienteExcel.setApellidoPaternoCliente("Campo por llenar");
                                    }
                                    
                                     
                                break;
                                case APELLIDO_MATERNO:
                                    columnName = APELLIDO_MATERNO_NAME;//para log
                                    
                                    if(!data.trim().equals("")){ 
                                        if(gc.isValidString(data, 1, 100)){
                                             clienteExcel.setApellidoMaternoCliente(data);
                                        }else{
                                            throw new Exception("For input string: '"+data+"'. Mínimo 1 y máximo 100 caracteres.");
                                        }
                                    }else{
                                         clienteExcel.setApellidoMaternoCliente("Campo por llenar");
                                    } 
                                break;
                                case LADA:
                                    columnName = LADA_NAME;//para log
                                    
                                    if(!data.trim().equals("")){                                          
                                        if(gc.isNumeric(data, 0, 3)){
                                            clienteExcel.setLada(data);
                                        }else{
                                             throw new Exception("For input string: '"+data+"'. Máximo 3 números.");
                                        }                                        
                                    }else{
                                        //throw new Exception("For input string: '"+data+"'. No puede quedar vacio.");
                                        clienteExcel.setLada("01");
                                    }                                         
                                break;
                                case TELEFONO:
                                    columnName = TELEFONO_NAME;//para log
                                    
                                    if(!data.trim().equals("")){
                                        if(gc.isNumeric(data, 7, 10)){
                                            clienteExcel.setTelefono(data);                                         
                                        }else{
                                            throw new Exception("For input string: '"+data+"'. Mínimo 7 y máximo 10 números.");   
                                        }
                                        
                                    }else{
                                         throw new Exception("For input string: '"+data+"'. No puede quedar vacio.");
                                    } 
                                break;
                                case EXTENSION:
                                    columnName = EXTENSION_NAME;//para log
                                    
                                    
                                    if(!data.trim().equals("")){ 
                                        if(gc.isValidString(data, 0, 5)){
                                             clienteExcel.setExtension(data);
                                        }else{
                                            throw new Exception("For input string: '"+data+"'. Mínimo 0 y máximo 5 caracteres.");
                                        }
                                    }else{
                                         clienteExcel.setExtension("01");
                                    }                                    
                                    
                                    
                                break;
                                case CELULAR:
                                    columnName = CELULAR_NAME;//para log
                                    
                                    if(!data.trim().equals("")){ 
                                        if(gc.isValidString(data,0, 11)){
                                             clienteExcel.setCelular(data);
                                        }else{
                                            throw new Exception("For input string: '"+data+"'. Máximo 11 caracteres.");
                                        }
                                    }else{
                                         clienteExcel.setCelular("0000000000");
                                    }
                                    
                                   
                                break;    
                                case CORREO_ELECTRONICO:
                                    columnName = CORREO_ELECTRONICO_NAME;//para log
                                    
                                    
                                    if(!data.trim().equals("")){ 
                                        if(gc.isEmail(data)){
                                             clienteExcel.setCorreo(data);
                                        }else{
                                            throw new Exception("For input string: '"+data+"'. Estructura incorrecta (correo@generico.com)" );
                                        }
                                    }else{
                                         clienteExcel.setCorreo("correo@generico.com");
                                    }                                    
                                     
                                break; 
                                case CONTACTO:
                                    columnName = CONTACTO_NAME;//para log
                                    
                                    if(!data.trim().equals("")){ 
                                        if(gc.isValidString(data, 0, 100)){
                                             clienteExcel.setContacto(data);
                                        }else{
                                            throw new Exception("For input string: '"+data+"'. Máximo 100 caracteres.");
                                        }
                                    }else{
                                         clienteExcel.setContacto("Campo por llenar");
                                    }   
                                    
                                break;     
                                case CALLE:
                                    columnName = CALLE_NAME;//para log
                                    
                                    
                                    if(!data.trim().equals("")){ 
                                        if(gc.isValidString(data, 1, 100)){
                                             clienteExcel.setCalle(data);
                                        }else{
                                            throw new Exception("For input string: '"+data+"'. Mínimo 1 y máximo 100 caracteres.");
                                        }
                                    }else{
                                          throw new Exception("For input string: '"+data+"'. No puede quedar vacio.");
                                    }
                                    
                                     
                                break;
                                case NUMERO_EXTERIOR:
                                    columnName = NUMERO_EXTERIOR_NAME;//para log
                                    
                                    if(!data.trim().equals("")){ 
                                        if(gc.isValidString(data, 1, 30)){
                                             clienteExcel.setNumero(data);
                                        }else{
                                            throw new Exception("For input string: '"+data+"'. Mínimo 1 y máximo 30 caracteres.");
                                        }
                                    }else{
                                         clienteExcel.setNumero("S/N");
                                    }                                    
                                  
                                break;
                                case NUMERO_INTERIOR:
                                    columnName = NUMERO_INTERIOR_NAME;//para log
                                    
                                    
                                    if(!data.trim().equals("")){ 
                                        if(gc.isValidString(data, 0, 30)){
                                             clienteExcel.setNumeroInterior(data);
                                        }else{
                                            throw new Exception("For input string: '"+data+"'. Máximo 30 caracteres.");
                                        }
                                    }else{
                                         clienteExcel.setNumeroInterior("S/N");
                                    }                                    
                                     
                                break;
                                case COLONIA:
                                    columnName = COLONIA_NAME;//para log
                                    
                                    if(!data.trim().equals("")){ 
                                        if(gc.isValidString(data, 1, 100)){
                                              clienteExcel.setColonia(data);
                                        }else{
                                            throw new Exception("For input string: '"+data+"'. Mínimo 1 y máximo 100 caracteres.");
                                        }
                                    }else{
                                         clienteExcel.setColonia("Campo por llenar");
                                    }                                    
                                    
                                break;
                                case CODIGO_POSTAL:
                                    columnName = CODIGO_POSTAL_NAME;//para log
                                                                                                           
                                    if(!data.trim().equals("")){ 
                                        
                                        if(gc.isCodigoPostal(data)){
                                              clienteExcel.setCodigoPostal(data);
                                        }else{
                                            throw new Exception("For input string: '"+data+"'. Máximo 5 números.");
                                        }
                                    }else{
                                         clienteExcel.setCodigoPostal("11111");
                                    }                                             
                                    
                                break;
                                case MUNICIPIO_DELEGACION:
                                    columnName = MUNICIPIO_DELEGACION_NAME;//para log
                                    
                                    
                                    if(!data.trim().equals("")){ 
                                        if(gc.isValidString(data, 1, 100)){
                                              clienteExcel.setMunicipio(data);
                                        }else{
                                            throw new Exception("For input string: '"+data+"'. Mínimo 1 y máximo 100 caracteres.");
                                        }
                                    }else{
                                         clienteExcel.setMunicipio("Campo por llenar");
                                    }                                     
                                    
                                break;    
                                case ESTADO:
                                    columnName = ESTADO_NAME;//para log
                                    
                                    if(!data.trim().equals("")){ 
                                        if(gc.isValidString(data, 1, 100)){
                                              clienteExcel.setEstado(data);
                                        }else{
                                            throw new Exception("For input string: '"+data+"'. Mínimo 1 y máximo 100 caracteres.");
                                        }
                                    }else{
                                         clienteExcel.setEstado("Campo por llenar");
                                    }
                                    
                                    
                                break;    
                                case PAIS:
                                    columnName = PAIS_NAME;//para log
                                    
                                    if(!data.trim().equals("")){ 
                                        if(gc.isValidString(data, 1, 100)){
                                              clienteExcel.setPais(data);
                                        }else{
                                            throw new Exception("For input string: '"+data+"'. Mínimo 1 y máximo 100 caracteres.");
                                        }
                                    }else{
                                         clienteExcel.setPais("Campo por llenar");
                                    }   
                                    
                                break;    
                                case DIAS_VISITA:
                                    columnName = DIAS_VISITA_NAME;//para log
                                    
                                    try{
                                        clienteExcel.setDiasVisita(data);
                                    }catch(Exception e){
                                         clienteExcel.setDiasVisita("");
                                    } 
                                    
                                break; 
                                case PERIORICIDAD_VISITA:
                                    columnName = PERIORICIDAD_VISITA_NAME;//para log
                                    
                                    try{                                                                                 
                                        clienteExcel.setPerioricidad(Integer.parseInt(data));
                                    }catch(Exception e){
                                        clienteExcel.setPerioricidad(1);
                                    } 
                                break;   
                                case PERMISO_VENTA_CREDITO:
                                    columnName = PERMISO_VENTA_CREDITO_NAME;//para log
                                    
                                    try{
                                        clienteExcel.setPermisoVentaCredito(Integer.parseInt(data));
                                    }catch(Exception e){
                                        clienteExcel.setPermisoVentaCredito(2);
                                    } 
                                break;
                                case LATITUD:
                                columnName = LATITUD_NAME;//para log

                                try{
                                    clienteExcel.setLatitud(""+(Double.parseDouble(data)));
                                }catch(Exception e){
                                    clienteExcel.setLatitud("0");
                                } 
                                break;
                                case LONGITUD:
                                columnName = LONGITUD_NAME;//para log

                                try{
                                    clienteExcel.setLongitud(""+(Double.parseDouble(data)));
                                }catch(Exception e){
                                    clienteExcel.setLongitud("0");
                                } 
                                break;
                                case NOMBRE_VENDEDOR:
                                    columnName = NOMBRE_VENDEDOR_NAME;//para log
                                    //buscamos el vendedor al que ha sido relacionado el cliente
                                    
                                    try{
                                        //conceptoExcel.setNumArticulosDisponibles(Double.parseDouble(data)); 
                                                                                
                                        Empleado empleadoExiste = null;
                                        String nombreEmpleado = "";
                                        try{
                                            nombreEmpleado = data.trim();
                                        }catch(Exception e){}
                                        if(nombreEmpleado != null && !nombreEmpleado.equals("")){
                                            try{
                                                empleadoExiste = getEmpleadoGenericoByEmpleadoNombre(nombreEmpleado, idEmpresa);                                               
                                            }catch(Exception e){
                                                empleadoExiste = null;
                                            }
                                            if(empleadoExiste != null){
                                                clienteExcel.setIdEstatus(empleadoExiste.getIdUsuarios());//USAMOS EL ATRIBUTO DE ID ESTATUS PARA CARGARLE EL ID DE VENDEDOR Y PODAMOS RECUPERARLO CUANDO INSERTEMOS/ACTUALICEMOS EL REGISTRO
                                            }else{
                                                clienteExcel.setIdEstatus(0);//USAMOS EL ATRIBUTO DE ID ESTATUS PARA CARGARLE EL ID DE VENDEDOR Y PODAMOS RECUPERARLO CUANDO INSERTEMOS/ACTUALICEMOS EL REGISTRO
                                            }
                                        }else{
                                            clienteExcel.setIdEstatus(0);//USAMOS EL ATRIBUTO DE ID ESTATUS PARA CARGARLE EL ID DE VENDEDOR Y PODAMOS RECUPERARLO CUANDO INSERTEMOS/ACTUALICEMOS EL REGISTRO
                                        }                                        
                                    }catch(Exception e){
                                        clienteExcel.setIdEstatus(0);//USAMOS EL ATRIBUTO DE ID ESTATUS PARA CARGARLE EL ID DE VENDEDOR Y PODAMOS RECUPERARLO CUANDO INSERTEMOS/ACTUALICEMOS EL REGISTRO
                                    }                                    
                                break;
                                case CLAVE:
                                    columnName = CLAVE_NAME;//para log
                                    
                                    if(!data.trim().equals("")){ 
                                        if(gc.isValidString(data, 1, 50)){
                                              clienteExcel.setClave(data);
                                        }else{
                                            throw new Exception("For input string: '"+data+"'. Mínimo 1 y máximo 50 caracteres.");
                                        }
                                    }else{
                                         clienteExcel.setClave("");
                                    }                                    
                                    
                                break;
                                
                                default:
                                    columnName = "DATO PERSONALIZADO";//para log
                                    System.out.println("--------");
                                    System.out.println("-------- DATO PERSONALIZADO");
                                    if(!data.trim().equals("")){ 
                                        System.out.println("-------- DATO PERSONALIZADO CON VALOR");
                                        for(ClienteCampoAdicional campoAdicional : clienteCampoAdicionalsDto){
                                            System.out.println("-------- COMPARANDO: " + campoAdicional.getLabelNombre() + ", CON: " + hoja.getCell(columna,0).getContents());
                                            if(campoAdicional.getLabelNombre().equals(hoja.getCell(columna,0).getContents())){//verificamos si el nombre de la columna es igual que el del label dado de alta
                                                System.out.println("-------- COLUMNA Y CAMPO PERSONALZIADO ENCONTRADO");
                                                if(gc.isValidString(data, 1, 100)){
                                                    //cargamos la info del dato personalizado
                                                    ClienteCampoContenido clienteCampoContenido = new ClienteCampoContenido();
                                                    clienteCampoContenido.setIdClienteCampo(campoAdicional.getIdClienteCampo());
                                                    clienteCampoContenido.setValorLabel(data);
                                                    System.out.println("------- CARGANDO INFO D FATO PERSONALIZADO . . . ");
                                                    System.out.println("------- CAMPO PERSONALIZADO: " + campoAdicional.getLabelNombre() + " , VALOR DEL CAMPO: " + data + " . . . ");
                                                    //añadimos al la lista los datos personalizados del cliente en question
                                                    clienteExcel.listClienteCampoContenido.add(clienteCampoContenido);
                                                }else{
                                                    throw new Exception("For input string: '"+data+"'. Mínimo 1 y máximo 100 caracteres.");
                                                }
                                            }
                                        }
                                                                                
                                        
                                    }
                                break;
                                    
                            }//switch                           
                            
                            
                        }catch(Exception e){
                            //Aqui log
                            //e.printStackTrace();
                            logActualizacionInsertado += "No se pudo leer el Cliente.  Registro número: "+ (fila+1) +" Columna: " + columnName + ", Error: "+e.getMessage()+" <br/>";            
                        }
                        
                        
                    }//for columnas
                    
                    System.out.println("\n"); 
                    listClientes.add(clienteExcel);
                    
                }//for filas
                
            }//for sheets
            
        }catch (Exception ioe){ 
            ioe.printStackTrace(); 
        }
        
        
        return logActualizacionInsertado;
        
    }
    
    
    public String insertaClientesExcel(long idEmpresa){
        
        String logActualizacionInsertadoLocal = "";
        int i = 1; 
        ClienteDaoImpl clienteDaoImpl = new ClienteDaoImpl();
        GenericValidator gc = new GenericValidator();        
        EmpresaBO empresaBO = new EmpresaBO(this.conn);
        int paisMatriz = 0;
        try {     
            EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl(this.conn).findByPrimaryKey(empresaBO.getEmpresaMatriz(idEmpresa).getIdEmpresa());
            paisMatriz = empresaPermisoAplicacionDto.getRfcPorNipCodigo(); //0 SI NO ESTA ACTIVO Y SERA RFC DE CLIENTE, 1 ACTIVO Y ES NIP (COLOMBIA), 2 RUC DE ECUADOR 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //for(Cliente item:listClientes){
        for(ClienteYcamposAdicionales item:listClientes){
        
            
            i ++;
            System.out.println("-------- registro"+ i);
            
            
            //************INSERTAMOS LOS REGISTROS DE LOS CLIENTES
            try{
                
                
                ClienteBO clienteBO = new ClienteBO(this.conn);
                Cliente clienteExiste = null;


                try{
                     System.out.println("BUSCANDO.-....................");    
                    clienteExiste = clienteBO.getClienteByRazonSocial(idEmpresa,item.getRazonSocial());                
                }catch(Exception e){
                   //e.printStackTrace();
                   //logActualizacionInsertado += "No se pudo actualizar el regitro del Concepto.  Registro número: "+ i +", Error: "+e.getMessage()+". <br/>";            
                }
                
                SgfensClienteVendedorDaoImpl sgfensClienteVendedorDaoImpl = new SgfensClienteVendedorDaoImpl(this.conn);
                //SI NO EXISTE LO INSERTAMOS
                if(clienteExiste==null){ 
                    
                    
                  System.out.println("CREAR CLIENTE...");    

                  
                 
                    if((!item.getRfcCliente().trim().equals("") && gc.isRFC(item.getRfcCliente())) &&  paisMatriz == 0){//cliente normal
                        System.out.println("Normal...");

                        Cliente clienteNuevo = new Cliente();                            
                            
                        clienteNuevo.setIdEmpresa((int)idEmpresa);

                        clienteNuevo.setIdEstatus(1);                           
                        clienteNuevo.setRazonSocial(item.getRazonSocial());
                        if(!item.getNombreCliente().equals("")){
                            clienteNuevo.setNombreCliente(item.getNombreCliente());
                        }else{
                            clienteNuevo.setNombreCliente(item.getRazonSocial());
                        }
                        clienteNuevo.setCalle(item.getCalle()); 
                        clienteNuevo.setLada(item.getLada());
                        clienteNuevo.setTelefono(item.getTelefono());
                        clienteNuevo.setPerioricidad(item.getPerioricidad());
                        clienteNuevo.setDiasVisita(item.getDiasVisita());
                        clienteNuevo.setRfcCliente(item.getRfcCliente());                            
                        clienteNuevo.setApellidoPaternoCliente(item.getApellidoPaternoCliente());
                        clienteNuevo.setApellidoMaternoCliente(item.getApellidoMaternoCliente());                            
                        clienteNuevo.setNumero(item.getNumero());
                        clienteNuevo.setNumeroInterior(item.getNumeroInterior());
                        clienteNuevo.setCodigoPostal(item.getCodigoPostal());
                        clienteNuevo.setColonia(item.getColonia());
                        clienteNuevo.setMunicipio(item.getMunicipio());
                        clienteNuevo.setEstado(item.getEstado());
                        clienteNuevo.setPais(item.getPais());                            
                        clienteNuevo.setExtension(item.getExtension());
                        clienteNuevo.setCelular(item.getCelular());
                        clienteNuevo.setCorreo(item.getCorreo());
                        clienteNuevo.setContacto(item.getContacto());
                        clienteNuevo.setGenerico(0);                           
                        clienteNuevo.setPermisoVentaCredito(item.getPermisoVentaCredito());
                        clienteNuevo.setNombreComercial(item.getNombreComercial()); 
                        clienteNuevo.setReferencia("");   
                        clienteNuevo.setLatitud(item.getLatitud());
                        clienteNuevo.setLongitud(item.getLongitud());
                        clienteNuevo.setClave(item.getClave());

                        //insert
                        ClientePk clientePk = clienteDaoImpl.insert(clienteNuevo);
                        
                        /////relacionamos al vendedor con el cliente, en caso de que lo hayan capturado
                        //Relacion con vendedor
                        if (item.getIdEstatus()>0){
                            SgfensClienteVendedor clienteVendedorDto = new SgfensClienteVendedor();

                            // si no existe registro, lo creamos
                            clienteVendedorDto.setIdCliente(clientePk.getIdCliente());
                            clienteVendedorDto.setIdUsuarioVendedor(item.getIdEstatus());
                            sgfensClienteVendedorDaoImpl.insert(clienteVendedorDto);
                        }
                        /////
                        

                    }else{//cliente Express                    
                        System.out.println("Express...");                          

                            Cliente clienteNuevo = new Cliente();                            
                            
                            clienteNuevo.setIdEmpresa((int)idEmpresa);
                            
                            clienteNuevo.setIdEstatus(3);                           
                            clienteNuevo.setRazonSocial(item.getRazonSocial());
                            if(!item.getNombreCliente().equals("")){
                                clienteNuevo.setNombreCliente(item.getNombreCliente());
                            }else{
                                clienteNuevo.setNombreCliente(item.getRazonSocial());
                            }
                            clienteNuevo.setCalle(item.getCalle()); 
                            clienteNuevo.setLada(item.getLada());
                            clienteNuevo.setTelefono(item.getTelefono());
                            clienteNuevo.setPerioricidad(item.getPerioricidad());
                            clienteNuevo.setDiasVisita(item.getDiasVisita());
                            clienteNuevo.setRfcCliente(item.getRfcCliente());                            
                            clienteNuevo.setApellidoPaternoCliente(item.getApellidoPaternoCliente());
                            clienteNuevo.setApellidoMaternoCliente(item.getApellidoMaternoCliente());                            
                            clienteNuevo.setNumero(item.getNumero());
                            clienteNuevo.setNumeroInterior(item.getNumeroInterior());
                            clienteNuevo.setCodigoPostal(item.getCodigoPostal());
                            clienteNuevo.setColonia(item.getColonia());
                            clienteNuevo.setMunicipio(item.getMunicipio());
                            clienteNuevo.setEstado(item.getEstado());
                            clienteNuevo.setPais(item.getPais());                            
                            clienteNuevo.setExtension(item.getExtension());
                            clienteNuevo.setCelular(item.getCelular());
                            clienteNuevo.setCorreo(item.getCorreo());
                            clienteNuevo.setContacto(item.getContacto());
                            clienteNuevo.setGenerico(0);                           
                            clienteNuevo.setPermisoVentaCredito(item.getPermisoVentaCredito());
                            clienteNuevo.setNombreComercial(item.getNombreComercial()); 
                            clienteNuevo.setReferencia("");   
                            clienteNuevo.setLatitud(item.getLatitud());
                            clienteNuevo.setLongitud(item.getLongitud());
                            clienteNuevo.setClave(item.getClave());
                            
                            
                            //insert
                            ClientePk clientePk = clienteDaoImpl.insert(clienteNuevo);
                            
                            /////relacionamos al vendedor con el cliente, en caso de que lo hayan capturado
                            //Relacion con vendedor
                            if (item.getIdEstatus()>0){
                                SgfensClienteVendedor clienteVendedorDto = new SgfensClienteVendedor();

                                // si no existe registro, lo creamos
                                clienteVendedorDto.setIdCliente(clientePk.getIdCliente());
                                clienteVendedorDto.setIdUsuarioVendedor(item.getIdEstatus());
                                sgfensClienteVendedorDaoImpl.insert(clienteVendedorDto);
                            }
                            /////
                            
                            ///---
                            //insertamos datos personalizados
                            /////**--Insertamos los registros de datos personalizados:
                            if(clientePk.getIdCliente() > 0){
                                ClienteCampoContenidoDaoImpl campoContenidoDaoImpl = new ClienteCampoContenidoDaoImpl(this.conn);
                                ClienteCampoAdicionalBO clienteCampoContenidoBO = new ClienteCampoAdicionalBO(this.conn);
                                clienteCampoContenidoBO.deleteCamposAdicionalesCliente(clientePk.getIdCliente());
                                for(ClienteCampoContenido cliPersonalizados : item.getListClienteCampoContenido()){
                                    cliPersonalizados.setIdCliente(clientePk.getIdCliente());
                                    campoContenidoDaoImpl.insert(cliPersonalizados);
                                }
                            }
                            ///---

                    }                
                  
                    
                    
                }else{//SI EXISTE LO ACTUALIZAMOS
                    
                     System.out.println("ACTUALIZAR CLIENTE...");                                             
                            
                        //clienteExiste.setIdEmpresa((int)idEmpresa);
                        //clienteExiste.setIdEstatus(3);                           
                        //clienteExiste.setRazonSocial(item.getRazonSocial());
                        if(!item.getNombreCliente().equals("")){
                            clienteExiste.setNombreCliente(item.getNombreCliente());
                        }else{
                            clienteExiste.setNombreCliente(item.getRazonSocial());
                        }
                        clienteExiste.setCalle(item.getCalle()); 
                        clienteExiste.setLada(item.getLada());
                        clienteExiste.setTelefono(item.getTelefono());
                        clienteExiste.setPerioricidad(item.getPerioricidad());
                        clienteExiste.setDiasVisita(item.getDiasVisita());
                        //clienteExiste.setRfcCliente(item.getRfcCliente());                            
                        clienteExiste.setApellidoPaternoCliente(item.getApellidoPaternoCliente());
                        clienteExiste.setApellidoMaternoCliente(item.getApellidoMaternoCliente());                            
                        clienteExiste.setNumero(item.getNumero());
                        clienteExiste.setNumeroInterior(item.getNumeroInterior());
                        clienteExiste.setCodigoPostal(item.getCodigoPostal());
                        clienteExiste.setColonia(item.getColonia());
                        clienteExiste.setMunicipio(item.getMunicipio());
                        clienteExiste.setEstado(item.getEstado());
                        clienteExiste.setPais(item.getPais());                            
                        clienteExiste.setExtension(item.getExtension());
                        clienteExiste.setCelular(item.getCelular());
                        clienteExiste.setCorreo(item.getCorreo());
                        clienteExiste.setContacto(item.getContacto());
                        clienteExiste.setGenerico(0);                           
                        clienteExiste.setPermisoVentaCredito(item.getPermisoVentaCredito());
                        clienteExiste.setNombreComercial(item.getNombreComercial()); 
                        clienteExiste.setReferencia("");
                        if(item.getLatitud()!= null && !item.getLatitud().trim().equals("") && !item.getLatitud().trim().equals("0") && !item.getLatitud().trim().equals("0.0")){
                            clienteExiste.setLatitud(item.getLatitud());
                        }
                        if(item.getLongitud()!= null && !item.getLongitud().trim().equals("") && !item.getLongitud().trim().equals("0") && !item.getLongitud().trim().equals("0.0")){
                            clienteExiste.setLongitud(item.getLongitud());
                        }
                        clienteExiste.setClave(item.getClave());
                     
                    
                        //insert
                        clienteDaoImpl.update(clienteExiste.createPk(),clienteExiste );
                        
                        /////
                        //Relacion con vendedor
                        if (item.getIdEstatus()>0){
                            SGClienteVendedorBO clienteVendedorBO = new SGClienteVendedorBO(clienteExiste.getIdCliente(),this.conn);
                            SgfensClienteVendedor clienteVendedorDto = clienteVendedorBO.getClienteVendedor();

                           if (clienteVendedorDto!=null){
                                //si ya existe registro, lo actualizamos
                                clienteVendedorDto.setIdUsuarioVendedor(item.getIdEstatus());
                                sgfensClienteVendedorDaoImpl.update(clienteVendedorDto.createPk(), clienteVendedorDto);
                           }else{
                                // si no existe registro, lo creamos
                                clienteVendedorDto = new SgfensClienteVendedor();

                                clienteVendedorDto.setIdCliente(clienteExiste.getIdCliente());
                                clienteVendedorDto.setIdUsuarioVendedor(item.getIdEstatus());
                                sgfensClienteVendedorDaoImpl.insert(clienteVendedorDto);
                           }
                        }
                        /////
                        
                        ///----
                        //campos personalizados:
                        /////**--Insertamos los registros de datos personalizados:                    
                        ClienteCampoContenidoDaoImpl campoContenidoDaoImpl = new ClienteCampoContenidoDaoImpl(this.conn);
                        ClienteCampoAdicionalBO clienteCampoContenidoBO = new ClienteCampoAdicionalBO(this.conn);
                        clienteCampoContenidoBO.deleteCamposAdicionalesCliente(clienteExiste.getIdCliente());
                        for(ClienteCampoContenido cliPersonalizados : item.getListClienteCampoContenido()){
                            cliPersonalizados.setIdCliente(clienteExiste.getIdCliente());
                            campoContenidoDaoImpl.insert(cliPersonalizados);
                        }
                        ///----
                }
                
            }catch(Exception ex){
                System.out.println("Problemas al guardar Cliente");
                logActualizacionInsertado += "No se pudo insertar / actualizar el regitro del Cliente.  Registro número: "+ i +", Error: "+ex.getMessage()+". <br/>";            
            }
            
            //************FIN REGISTROS DE LOS CONCEPTOS 
            
            
            
        }
        
        
        
        return logActualizacionInsertado;
    }
    
    public Empleado getEmpleadoGenericoByEmpleadoNombre(String nombreEmpleado, int idEmpresa) throws Exception{
        Empleado empleado = null;
        nombreEmpleado = nombreEmpleado.trim();
        nombreEmpleado = nombreEmpleado.replaceAll("  "," ");
        nombreEmpleado = nombreEmpleado.replaceAll("   "," ");
        //"CONCAT(NOMBRE,APELLIDO_PATERNO,APELLIDO_MATERNO) LIKE '%" + nombreEmpleado + "%';
        
        //nombreEmpleado = nombreEmpleado.replaceAll("\\s", "%");
        
        try{
            EmpleadoDaoImpl empleadoDaoImpl = new EmpleadoDaoImpl();
            empleado = empleadoDaoImpl.findByDynamicWhere("CONCAT_WS(' ',NOMBRE,APELLIDO_PATERNO,APELLIDO_MATERNO) LIKE '%" +nombreEmpleado+"%' AND ID_EMPRESA = " + idEmpresa, new Object[0])[0];
            /*if (empleado==null){
                throw new Exception("No hay empleado con ese nombre");
            }*/
        }catch(EmpleadoDaoException  e){
            e.printStackTrace();
            throw new Exception("No hay empleado con ese nombre");
        }
        
        return empleado;
    }
    
}
