/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.importa;

import com.tsp.sct.bo.ClientePrecioConceptoBO;
import com.tsp.sct.bo.ConceptoBO;
import com.tsp.sct.bo.ExistenciaAlmacenBO;
import com.tsp.sct.bo.UsuarioBO;
import com.tsp.sct.dao.dto.Almacen;
import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.dto.ClientePrecioConcepto;
import com.tsp.sct.dao.dto.Concepto;
import com.tsp.sct.dao.dto.ConceptoPk;
import com.tsp.sct.dao.dto.ExistenciaAlmacen;
import com.tsp.sct.dao.dto.Movimiento;
import com.tsp.sct.dao.jdbc.AlmacenDaoImpl;
import com.tsp.sct.dao.jdbc.ClienteDaoImpl;
import com.tsp.sct.dao.jdbc.ClientePrecioConceptoDaoImpl;
import com.tsp.sct.dao.jdbc.ConceptoDaoImpl;
import com.tsp.sct.dao.jdbc.ExistenciaAlmacenDaoImpl;
import com.tsp.sct.dao.jdbc.MovimientoDaoImpl;
import com.tsp.sct.dao.jdbc.ResourceManager;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

/**
 *
 * @author HpPyme
 */
public class ReadConceptosExcel {
    
    
    public String logActualizacionInsertado = "";
    
    
    //Lista para cargar datos desde excel   
    //List<Concepto> listConceptos = new ArrayList<Concepto>(); 
    List<ConceptoYClientePrecioConcepto> listConceptos = new ArrayList<ConceptoYClientePrecioConcepto>(); 
    //List<ExistenciaAlmacen> listExistenciaAlmacen = new ArrayList<ExistenciaAlmacen>(); 
    
    //Columna correspondiente en excel
    private final int NOMBRE = 0;
    private final int DESCRIPCION = 1;
    private final int CODIGO = 2;
    private final int CLAVE = 3;
    private final int PRECIO_UNITARIO = 4;
    private final int UNITARIO_HASTA_X_UNIDADES = 5;
    private final int PRECIO_MEDIO_MAYOREO = 6;
    private final int MEDIO_DESDE_X_UNIDADES = 7;
    private final int MEDIO_HASTA_Y_UNIDADES = 8;
    private final int PRECIO_MAYOREO = 9;
    private final int MAYOREO_DESDE_Y_UNIDADES = 10;
    private final int PRECIO_DOCENA = 11;
    private final int PRECIO_ESPECIAL = 12;
    private final int PRECIO_COMPRA = 13;
    private final int PRECIO_MINIMO_VENTA = 14;
    private final int STOCK_MINIMO = 15;
    private final int VOLUMEN = 16;
    private final int PESO = 17;
    private final int OBSERVACIONES = 18;   
    private final int PRECIO_UNITARIO_GRANEL = 19;
    private final int PRECIO_MEDIO_MAYOREO_GRANEL = 20;
    private final int PRECIO_MAYOREO_GRANEL = 21;
    private final int PRECIO_ESPECIAL_GRANEL = 22;
    private final int STOCK_INICIAL = 23;
    private final int NOMBRE_ALMACEN = 24;
    private final int CLIENTE = 25;
    

    
    //Nombres para log
    public static final String NOMBRE_NAME = "NOMBRE";
    public static final String DESCRIPCION_NAME = "DESCRIPCION";
    public static final String CODIGO_NAME = "CODIGO";
    public static final String CLAVE_NAME = "CLAVE";
    public static final String PRECIO_UNITARIO_NAME = "PRECIO UNITARIO";
    public static final String UNITARIO_HASTA_X_UNIDADES_NAME = "HASTA X (UNIDADES)";
    public static final String PRECIO_MEDIO_MAYOREO_NAME = "PRECIO MEDIO MAYOREO";
    public static final String MEDIO_DESDE_X_UNIDADES_NAME = "DESDE X+1  (UNIDADES)";
    public static final String MEDIO_HASTA_Y_UNIDADES_NAME = "HASTA Y (UNIDADES)";
    public static final String PRECIO_MAYOREO_NAME = "PRECIO MAYOREO";
    public static final String MAYOREO_DESDE_Y_UNIDADES_NAME = "DESDE Y+1 (UNIDADES)";
    public static final String PRECIO_DOCENA_NAME = "PRECIO DOCENA";
    public static final String PRECIO_ESPECIAL_NAME = "PRECIO ESPECIAL";
    public static final String PRECIO_COMPRA_NAME = "PRECIO COMPRA";
    public static final String PRECIO_MINIMO_VENTA_NAME = "PRECIO MINIMO VENTA";
    public static final String STOCK_MINIMO_NAME = "STOCK MINIMO";
    public static final String VOLUMEN_NAME = "VOLUMEN";
    public static final String PESO_NAME = "PESO";
    public static final String OBSERVACIONES_NAME = "OBSERVACIONES";   
    public static final String PRECIO_UNITARIO_GRANEL_NAME = "PRECIO UNITARIO GRANEL";
    public static final String PRECIO_MEDIO_MAYOREO_GRANEL_NAME = "PRECIO MEDIO MAYOREO GRANEL";
    public static final String PRECIO_MAYOREO_GRANEL_NAME = "PRECIO MAYOREO GRANEL";
    public static final String PRECIO_ESPECIAL_GRANEL_NAME = "PRECIO ESPECIAL GRANEL";
    public static final String STOCK_INICIAL_NAME = "STOCK INICIAL";
    public static final String NOMBRE_ALMACEN_NAME = "NOMBRE ALMACEN";
    public static final String CLIENTE_NAME = "CLIENTE";
    
    
    
    
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

    public String leerArchivoExcelConceptos(String archivoOrigen , int idEmpresa) {           
        
        
        //1 crear lista tipo Cliente importar
        //2 cargar lista con datos leidos del excel     
        //3 Mapear a obj propios e insercion a bd                
             
        //Concepto conceptoExcel = null;  
        ConceptoYClientePrecioConcepto conceptoExcel = null;
        
        
        try{ 
            
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("ISO-8859-1");
            
            Workbook archivoExcel = Workbook.getWorkbook(new File(archivoOrigen),ws); 
            System.out.println("Número de Hojas\t" + archivoExcel.getNumberOfSheets()); 
            
            AlmacenDaoImpl almacenDaoImpl = new AlmacenDaoImpl(this.conn);
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
              
                ClienteDaoImpl clienteDaoImpl = new ClienteDaoImpl(this.conn);
                 
                for (int fila = 1; fila < numFilas; fila++) { // Recorre cada fila de la hoja 
                    
                    
                     System.out.println("----------------");
                    //Creamos obj cliente                                      
                    //conceptoExcel =  new Concepto();
                    conceptoExcel =  new ConceptoYClientePrecioConcepto();
                    
                    
                    for (int columna = 0; columna < numColumnas; columna++) { // Recorre cada columna de la fila 
                    
                        
                        
                        //Data =  info dentro de celda
                        data = hoja.getCell(columna,fila ).getContents(); 
                        
                        //System.out.println(" |  FILA: " + fila + "   --  Columna:  " + columna + "  ***Data: " + data );
                        
                        try{
                            switch(columna){

                                case NOMBRE:
                                    columnName = NOMBRE_NAME;//para log
                                    
                                    if(!data.trim().equals("")){
                                        conceptoExcel.setNombre(data);
                                    }else{
                                         throw new Exception("For input string: '"+data+"'. No puede quedar vacio.");
                                    } 
                                break;
                                case DESCRIPCION:
                                    columnName = DESCRIPCION_NAME;//para log  
                                    if(!data.trim().equals("")){
                                        conceptoExcel.setDescripcion(data);       
                                    }else{
                                         throw new Exception("For input string: '"+data+"'. No puede quedar vacio.");
                                    }
                                break;
                                case CODIGO:
                                    columnName = CODIGO_NAME ;//para log
                                    try{
                                        conceptoExcel.setIdentificacion(data);
                                    }catch(Exception e){
                                        conceptoExcel.setIdentificacion("");
                                    }                                    
                                break;
                                case CLAVE:
                                    columnName = CLAVE_NAME;//para log
                                    try{
                                        conceptoExcel.setClave(data);
                                    }catch(Exception e){   
                                        conceptoExcel.setClave("");
                                    }                                    
                                break;
                                case PRECIO_UNITARIO:
                                    columnName = PRECIO_UNITARIO_NAME;//para log
                                    if(!data.trim().equals("")){
                                         if(Float.parseFloat(data)>0 ){ 
                                            conceptoExcel.setPrecio(Float.parseFloat(data));
                                         }else{
                                             throw new Exception("For input string: '"+data+"'. Debe ser mayor a 0");
                                         }                                        
                                    }else{
                                        throw new Exception("For input string: '"+data+"'. No puede quedar vacio");
                                    }
                                    
                                break;
                                case UNITARIO_HASTA_X_UNIDADES:
                                    columnName = UNITARIO_HASTA_X_UNIDADES_NAME;//para log
                                    try{
                                        conceptoExcel.setMaxMenudeo(Double.parseDouble(data));
                                    }catch(Exception e){
                                        conceptoExcel.setMaxMenudeo(0);
                                    }                                    
                                break;
                                case PRECIO_MEDIO_MAYOREO:
                                    columnName = PRECIO_MEDIO_MAYOREO_NAME;//para log
                                    try{
                                        conceptoExcel.setPrecioMedioMayoreo(Double.parseDouble(data));
                                    }catch(Exception e){
                                        conceptoExcel.setPrecioMedioMayoreo(0);
                                    }                                    
                                break;
                                case MEDIO_DESDE_X_UNIDADES:
                                    columnName = MEDIO_DESDE_X_UNIDADES_NAME;//para log
                                    try{
                                        conceptoExcel.setMinMedioMayoreo(Double.parseDouble(data));
                                    }catch(Exception e){
                                        conceptoExcel.setMinMedioMayoreo(0);
                                    }                                    
                                break;
                                case MEDIO_HASTA_Y_UNIDADES:
                                    columnName = MEDIO_HASTA_Y_UNIDADES_NAME;//para log
                                    try{
                                        conceptoExcel.setMaxMedioMayoreo(Double.parseDouble(data));
                                    }catch(Exception e){
                                        conceptoExcel.setMaxMedioMayoreo(0);
                                    }                                        
                                break;
                                case PRECIO_MAYOREO:
                                    columnName = PRECIO_MAYOREO_NAME;//para log
                                    try{
                                        conceptoExcel.setPrecioMayoreo(Double.parseDouble(data));
                                    }catch(Exception e){
                                        conceptoExcel.setPrecioMayoreo(0);
                                    }
                                break;
                                case MAYOREO_DESDE_Y_UNIDADES:
                                    columnName = MAYOREO_DESDE_Y_UNIDADES_NAME;//para log
                                    try{
                                        conceptoExcel.setMinMayoreo(Double.parseDouble(data)); 
                                    }catch(Exception e){
                                        conceptoExcel.setMinMayoreo(0); 
                                    }
                                break; 
                                case PRECIO_DOCENA:
                                    columnName = PRECIO_DOCENA_NAME;//para log
                                    try{
                                        conceptoExcel.setPrecioDocena(Double.parseDouble(data));                                    
                                    }catch(Exception e){
                                        conceptoExcel.setPrecioDocena(0); 
                                    }
                                break;
                                case PRECIO_ESPECIAL:
                                    columnName = PRECIO_ESPECIAL_NAME;//para log
                                    try{
                                        conceptoExcel.setPrecioEspecial(Double.parseDouble(data));                                    
                                    }catch(Exception e){
                                        conceptoExcel.setPrecioEspecial(0);        
                                    }
                                break;
                                case PRECIO_COMPRA:
                                    columnName = PRECIO_COMPRA_NAME;//para log
                                    try{
                                        conceptoExcel.setPrecioCompra(Float.parseFloat(data));                                    
                                    }catch(Exception e){
                                        conceptoExcel.setPrecioCompra(0);  
                                    }
                                break;
                                case PRECIO_MINIMO_VENTA:
                                    columnName = PRECIO_MINIMO_VENTA_NAME;//para log
                                    try{
                                        conceptoExcel.setPrecioMinimoVenta(Double.parseDouble(data));   
                                    }catch(Exception e){
                                        conceptoExcel.setPrecioMinimoVenta(0);    
                                    }                                        
                                break;
                                case STOCK_MINIMO:
                                    columnName = STOCK_MINIMO_NAME;//para log
                                    try{
                                        conceptoExcel.setStockMinimo(Double.parseDouble(data)); 
                                    }catch(Exception e){
                                        conceptoExcel.setStockMinimo(0);     
                                    }
                                break;
                                case VOLUMEN:
                                    columnName = VOLUMEN_NAME;//para log
                                    try{
                                        conceptoExcel.setVolumen(Double.parseDouble(data));                                    
                                    }catch(Exception e){
                                        conceptoExcel.setVolumen(0); 
                                    }
                                break;
                                case PESO:
                                    columnName = PESO_NAME;//para log
                                    try{
                                        conceptoExcel.setPeso(Double.parseDouble(data));                                    
                                    }catch(Exception e){
                                        conceptoExcel.setPeso(0);     
                                    }
                                break;
                                case OBSERVACIONES:
                                    columnName = OBSERVACIONES_NAME;//para log
                                    try{
                                        conceptoExcel.setObservaciones(data);  
                                    }catch(Exception e){
                                        conceptoExcel.setObservaciones("");   
                                    }
                                break;                                
                                case PRECIO_UNITARIO_GRANEL:
                                    columnName = PRECIO_UNITARIO_GRANEL_NAME;//para log
                                    try{
                                        conceptoExcel.setPrecioUnitarioGranel(Double.parseDouble(data));                                    
                                    }catch(Exception e){
                                        conceptoExcel.setPrecioUnitarioGranel(0);      
                                    }                                        
                                break;
                                case PRECIO_MEDIO_MAYOREO_GRANEL:
                                    columnName = PRECIO_MEDIO_MAYOREO_GRANEL_NAME;//para log
                                    try{
                                        conceptoExcel.setPrecioMedioGranel(Double.parseDouble(data));                                    
                                    }catch(Exception e){
                                         conceptoExcel.setPrecioMedioGranel(0);         
                                    } 
                                break;
                                case PRECIO_MAYOREO_GRANEL:
                                    columnName = PRECIO_MAYOREO_GRANEL_NAME;//para log
                                    try{
                                        conceptoExcel.setPrecioMayoreoGranel(Double.parseDouble(data)); 
                                    }catch(Exception e){
                                         conceptoExcel.setPrecioMayoreoGranel(0);       
                                    }
                                break;
                                case PRECIO_ESPECIAL_GRANEL:
                                    columnName = PRECIO_ESPECIAL_GRANEL_NAME;//para log
                                    try{
                                        conceptoExcel.setPrecioEspecialGranel(Double.parseDouble(data));     
                                    }catch(Exception e){
                                        conceptoExcel.setPrecioEspecialGranel(0);      
                                    }
                                break;
                                case STOCK_INICIAL:
                                    columnName = STOCK_INICIAL_NAME;//para log
                                    try{
                                        conceptoExcel.setNumArticulosDisponibles(Double.parseDouble(data));     
                                    }catch(Exception e){
                                        conceptoExcel.setNumArticulosDisponibles(0);      
                                    }
                                break;
                                case NOMBRE_ALMACEN: //verificamos que exista el amacen y de ser asi agregamos el registro de existencia en almacen y el movimiento de entrada
                                    columnName = NOMBRE_ALMACEN_NAME;//para log
                                    try{
                                        //conceptoExcel.setNumArticulosDisponibles(Double.parseDouble(data));                                             
                                        Almacen alma = null;
                                        String nombreAlmacen = "";
                                        try{
                                            nombreAlmacen = data.trim();
                                        }catch(Exception e){}
                                        if(nombreAlmacen != null && !nombreAlmacen.equals("")){
                                            try{
                                                alma = almacenDaoImpl.findByDynamicWhere(" NOMBRE = '" + nombreAlmacen + "' AND ID_EMPRESA = " +idEmpresa+ " ",null)[0];
                                                if(alma == null){
                                                    logActualizacionInsertado += "El producto no tiene almacen registrado o no existe el almacen ingresado. Registro numero: " + (fila+1) +" Columna: " + columnName + "  <br/>";
                                                }else{
                                                    conceptoExcel.setIdAlmacen(alma.getIdAlmacen());
                                                }
                                            }catch(Exception e){
                                                logActualizacionInsertado += "El producto no tiene almacen registrado o no existe el almacen ingresado. Registro numero: " + (fila+1) +" Columna: " + columnName + "  <br/>";
                                            }                                            
                                        }else{
                                            logActualizacionInsertado += "El producto no tiene almacen registrado. Registro numero: " + (fila+1) +" Columna: " + columnName + "  <br/>";
                                        }
                                    }catch(Exception e){
                                        logActualizacionInsertado += "El producto no tiene almacen registrado o no existe el almacen ingresado. Registro numero: " + (fila+1) +" Columna: " + columnName + "  <br/>";
                                    }
                                break;
                                case CLIENTE:
                                    columnName = CLIENTE_NAME;//para log
                                    if(!data.trim().equals("")){
                                        try{
                                            System.out.println("------- data trae dato");
                                            //buscamos el cliente:
                                            String buscarNombreCompletoCliente = data.replaceAll("\\s", "%");
                                            Cliente clien = null;
                                            try{
                                                clien = clienteDaoImpl.findByDynamicWhere(" (ID_ESTATUS = 1 OR ID_ESTATUS = 3) AND ID_EMPRESA = " + idEmpresa + " AND CONCAT(NOMBRE_CLIENTE,APELLIDO_PATERNO_CLIENTE,APELLIDO_MATERNO_CLIENTE) LIKE '%" + buscarNombreCompletoCliente +"%'", null)[0];
                                                System.out.println("------- cliente encontrado");
                                            }catch(Exception e){
                                                try{//si no esta concatenando el nombre, lo buscamos en el campo de razon social
                                                    clien = clienteDaoImpl.findByDynamicWhere(" (ID_ESTATUS = 1 OR ID_ESTATUS = 3) AND ID_EMPRESA = " + idEmpresa + " AND RAZON_SOCIAL LIKE '%" + buscarNombreCompletoCliente +"%'", null)[0];
                                                    System.out.println("------- cliente encontrado en razon social");
                                                }catch(Exception e2){e2.printStackTrace();}
                                            }
                                            if(clien != null){
                                                conceptoExcel.getClientePrecioConcepto().setIdCliente(clien.getIdCliente());
                                                System.out.println("------- CLIENTE CARGADO CON ID: "+ clien.getIdCliente());
                                            }else{
                                                conceptoExcel.getClientePrecioConcepto().setIdCliente(0);
                                            }
                                            
                                        }catch(Exception e){
                                            System.out.println("------- CLIENTE VALOR 0!!!!!!!!!!!");
                                            conceptoExcel.getClientePrecioConcepto().setIdCliente(0);
                                        }
                                    }else{
                                        System.out.println("------- CLIENTE 2 VALOR 0!!!!!!!!!!!");
                                        conceptoExcel.getClientePrecioConcepto().setIdCliente(0);
                                    }
                                break; 
                                
                            }//switch   
                        }catch(Exception e){
                            //Aqui log
                            //e.printStackTrace();
                            logActualizacionInsertado += "No se pudo leer el Producto.  Registro numero: "+ (fila+1) +" Columna: " + columnName + ", Error: "+e.getMessage()+" <br/>";            
                        }                        
                        

                    }//for columna 
                    
                    System.out.println("\n"); 
                    listConceptos.add(conceptoExcel);
                }//for fila
                
            }//for hoja
            
        }catch (Exception ioe){ 
            ioe.printStackTrace(); 
        }
        
        return logActualizacionInsertado;
    }
    
    
    
    public String insertaConceptosExcel(long idEmpresa){
        
        logActualizacionInsertado = "";
        int i = 1; 
        ConceptoDaoImpl conceptoDaoImpl = new ConceptoDaoImpl(this.conn);
        
        ExistenciaAlmacenDaoImpl existenciaAlmacenDaoImpl = new ExistenciaAlmacenDaoImpl(this.conn);
        MovimientoDaoImpl movimientosDaoImpl = new MovimientoDaoImpl(this.conn);
        
        ClientePrecioConceptoDaoImpl clientePrecioConceptoDaoImpl = new ClientePrecioConceptoDaoImpl(this.conn);
        
        //for(Concepto item:listConceptos){
        for(ConceptoYClientePrecioConcepto item:listConceptos){
            
            i ++;
            System.out.println("-------- registro"+ i);
            
            
            //************INSERTAMOS LOS REGISTROS DE LOS CONCEPTOS
            
            try{
                ConceptoBO conceptoBO = new ConceptoBO(this.conn);
                Concepto conceptoExiste = null;


                try{
                    conceptoExiste = conceptoBO.getConceptoByNombre(idEmpresa,item.getNombre());                
                }catch(Exception e){
                   //e.printStackTrace();
                   //logActualizacionInsertado += "No se pudo actualizar el regitro del Concepto.  Registro numero: "+ i +", Error: "+e.getMessage()+". <br/>";            
                }

                //SI NO EXISTE LO INSERTAMOS
                if(conceptoExiste==null){                
                  System.out.println("CREAR PRODUCTO...");    

                  Concepto conceptoNuevo = new Concepto();

                  String nombreEncriptado = "";
                  try{
                      nombreEncriptado = conceptoBO.encripta(item.getNombre());
                  }catch(Exception e){}


                  conceptoNuevo.setIdEmpresa((int)idEmpresa);
                  conceptoNuevo.setIdEstatus(1);
                  conceptoNuevo.setNombreDesencriptado(item.getNombre());
                  conceptoNuevo.setDesglosePiezas(1);
                  conceptoNuevo.setFechaAlta(new Date());
                  conceptoNuevo.setIdMarca(-1);                  
                  conceptoNuevo.setIdEmbalaje(-1);
                  conceptoNuevo.setIdImpuesto(-1);
                  conceptoNuevo.setIdCategoria(-1);
                  conceptoNuevo.setIdSubcategoria(-1);
                  conceptoNuevo.setIdSubcategoria2(-1);
                  conceptoNuevo.setIdSubcategoria3(-1);
                  conceptoNuevo.setIdSubcategoria4(-1);
                  conceptoNuevo.setNumeroLote("");
                  conceptoNuevo.setFechaCaducidad(null);

                  conceptoNuevo.setNombre(nombreEncriptado);
                  conceptoNuevo.setDescripcion(item.getDescripcion());
                  conceptoNuevo.setIdentificacion(item.getIdentificacion());
                  conceptoNuevo.setClave(item.getClave());
                  conceptoNuevo.setPrecio(item.getPrecio());
                  conceptoNuevo.setMaxMenudeo(item.getMaxMenudeo());
                  conceptoNuevo.setPrecioMedioMayoreo(item.getPrecioMedioMayoreo());
                  conceptoNuevo.setMinMedioMayoreo(item.getMinMedioMayoreo());
                  conceptoNuevo.setMaxMedioMayoreo(item.getMaxMedioMayoreo());
                  conceptoNuevo.setPrecioMayoreo(item.getPrecioMayoreo());
                  conceptoNuevo.setMinMayoreo(item.getMinMayoreo());
                  conceptoNuevo.setPrecioDocena(item.getPrecioDocena());
                  conceptoNuevo.setPrecioEspecial(item.getPrecioEspecial());
                  conceptoNuevo.setPrecioCompra(item.getPrecioCompra());
                  conceptoNuevo.setPrecioMinimoVenta(item.getPrecioMinimoVenta());
                  conceptoNuevo.setStockMinimo(item.getStockMinimo());
                  if(item.getStockMinimo()>0){
                      conceptoNuevo.setStockAvisoMin((short)1);
                  }else{
                      conceptoNuevo.setStockAvisoMin((short)-1);
                  }
                  conceptoNuevo.setVolumen(item.getVolumen());
                  conceptoNuevo.setPeso(item.getPeso());
                  conceptoNuevo.setObservaciones(item.getObservaciones());
                  conceptoNuevo.setPrecioUnitarioGranel(item.getPrecioUnitarioGranel());
                  conceptoNuevo.setPrecioMedioGranel(item.getPrecioMedioGranel());
                  conceptoNuevo.setPrecioMayoreoGranel(item.getPrecioMayoreoGranel());
                  conceptoNuevo.setPrecioEspecialGranel(item.getPrecioEspecialGranel());
                  conceptoNuevo.setNumArticulosDisponibles(item.getNumArticulosDisponibles());

                  //insert
                  ConceptoPk conceptoInsertado = conceptoDaoImpl.insert(conceptoNuevo);
                  
                  ///////
                    ExistenciaAlmacen almacenExists = new ExistenciaAlmacen();
                    almacenExists.setIdAlmacen(item.getIdAlmacen());
                    almacenExists.setIdConcepto(conceptoInsertado.getIdConcepto());
                    almacenExists.setExistencia(item.getNumArticulosDisponibles());
                    almacenExists.setEstatus(1);
                    existenciaAlmacenDaoImpl.insert(almacenExists);
                    /**
                     * Insertamos movimiento entrada inicial*
                     */
                    Movimiento movimientoDto = new Movimiento();                    

                    movimientoDto.setIdEmpresa((int)idEmpresa);
                    movimientoDto.setTipoMovimiento("ENTRADA");
                    movimientoDto.setNombreProducto(item.getNombre());
                    movimientoDto.setContabilidad(item.getNumArticulosDisponibles());
                    movimientoDto.setIdProveedor(-1);
                    movimientoDto.setOrdenCompra("");
                    movimientoDto.setNumeroGuia("");
                    movimientoDto.setIdAlmacen(item.getIdAlmacen());
                    movimientoDto.setConceptoMovimiento("Alta de Producto");
                    movimientoDto.setFechaRegistro(new Date());
                    movimientoDto.setIdConcepto(conceptoInsertado.getIdConcepto());

                    movimientosDaoImpl.insert(movimientoDto);
                  ///////
                  
                  ///--/-// ADEMAS DE INSERTA EL CONCEPTO, DE QUE NO EXISTIO, SI TRAE UN CLIENTE RELACIONADO, LO RELACIONAMOS:
                    if(item.getClientePrecioConcepto().getIdCliente() > 0){
                        //verificamos si ya existe la relacion del precio de concepto y cliente
                        ClientePrecioConcepto clientePrecioConcepto = null;                        
                        try{                            
                            clientePrecioConcepto = new ClientePrecioConceptoBO(item.getClientePrecioConcepto().getIdCliente(), conceptoInsertado.getIdConcepto(), this.conn).getClientePrecioConcepto();
                        }catch(Exception e){}
                        if(clientePrecioConcepto != null){//actualizamos el registro:
                            clientePrecioConcepto.setIdConcepto(conceptoInsertado.getIdConcepto());
                            clientePrecioConcepto.setIdCliente(item.getClientePrecioConcepto().getIdCliente());
                            clientePrecioConcepto.setPrecioUnitarioCliente(item.getPrecio());
                            clientePrecioConcepto.setPrecioMedioCliente(item.getPrecioMedioMayoreo());
                            clientePrecioConcepto.setPrecioMayoreoCliente(item.getPrecioMayoreo());
                            clientePrecioConcepto.setPrecioEspecialCliente(item.getPrecioEspecial());
                            clientePrecioConcepto.setPrecioDocenaCliente(item.getPrecioDocena());
                            clientePrecioConcepto.setPrecioUnitarioCliente(item.getPrecio());
                            clientePrecioConcepto.setIdEstatus(1);
                            clientePrecioConcepto.setPrecioUnitarioGranelCliente(item.getPrecioUnitarioGranel());
                            clientePrecioConcepto.setPrecioMedioGranelCliente(item.getPrecioMedioGranel());
                            clientePrecioConcepto.setPrecioMayoreoGranelCliente(item.getPrecioMayoreoGranel());
                            clientePrecioConcepto.setPrecioEspecialGranelCliente(item.getPrecioEspecialGranel());
                            clientePrecioConceptoDaoImpl.update(clientePrecioConcepto.createPk(), clientePrecioConcepto);
                        }else{
                            clientePrecioConcepto = new ClientePrecioConcepto();
                            clientePrecioConcepto.setIdConcepto(conceptoInsertado.getIdConcepto());
                            clientePrecioConcepto.setIdCliente(item.getClientePrecioConcepto().getIdCliente());
                            clientePrecioConcepto.setPrecioUnitarioCliente(item.getPrecio());
                            clientePrecioConcepto.setPrecioMedioCliente(item.getPrecioMedioMayoreo());
                            clientePrecioConcepto.setPrecioMayoreoCliente(item.getPrecioMayoreo());
                            clientePrecioConcepto.setPrecioEspecialCliente(item.getPrecioEspecial());
                            clientePrecioConcepto.setPrecioDocenaCliente(item.getPrecioDocena());
                            clientePrecioConcepto.setPrecioUnitarioCliente(item.getPrecio());
                            clientePrecioConcepto.setIdEstatus(1);
                            clientePrecioConcepto.setPrecioUnitarioGranelCliente(item.getPrecioUnitarioGranel());
                            clientePrecioConcepto.setPrecioMedioGranelCliente(item.getPrecioMedioGranel());
                            clientePrecioConcepto.setPrecioMayoreoGranelCliente(item.getPrecioMayoreoGranel());
                            clientePrecioConcepto.setPrecioEspecialGranelCliente(item.getPrecioEspecialGranel());
                            clientePrecioConceptoDaoImpl.insert(clientePrecioConcepto);
                        }
                        
                    }
                  ///--/-// FIN DE RELACIONAR EL CLIENTE Y EL PRECIO DEL CONCEPTO
                  
                }else{//SI EXISTE LO ACTUALIZAMOS
                    if(item.getClientePrecioConcepto().getIdCliente() <= 0){ // si viene vacio el cliente, entonces se trata de una actualización de concepto puro, de ser que venga ID de 
                                                                            // cliente entonces solo se trata de insertar/actualizar los precios del concepto en relación al cliente

                        System.out.println("ACTUALIZAR PRODUCTO...");     


                        if(conceptoExiste != null){
                                  String nombreEncriptado = "";
                            try{
                                nombreEncriptado = conceptoBO.encripta(item.getNombre());
                            }catch(Exception e){}


                            //conceptoExiste.setIdEmpresa((int)idEmpresa);
                            conceptoExiste.setIdEstatus(1);
                            //conceptoExiste.setNombreDesencriptado(item.getNombre());
                            //conceptoExiste.setDesglosePiezas(1);
                            //conceptoNuevo.setFechaAlta(new Date());


                            //conceptoNuevo.setNombre(nombreEncriptado);
                            conceptoExiste.setDescripcion(item.getDescripcion());
                            conceptoExiste.setIdentificacion(item.getIdentificacion());
                            conceptoExiste.setClave(item.getClave());
                            conceptoExiste.setPrecio(item.getPrecio());
                            conceptoExiste.setMaxMenudeo(item.getMaxMenudeo());
                            conceptoExiste.setPrecioMedioMayoreo(item.getPrecioMedioMayoreo());
                            conceptoExiste.setMinMedioMayoreo(item.getMinMedioMayoreo());
                            conceptoExiste.setMaxMedioMayoreo(item.getMaxMedioMayoreo());
                            conceptoExiste.setPrecioMayoreo(item.getPrecioMayoreo());
                            conceptoExiste.setMinMayoreo(item.getMinMayoreo());
                            conceptoExiste.setPrecioDocena(item.getPrecioDocena());
                            conceptoExiste.setPrecioEspecial(item.getPrecioEspecial());
                            conceptoExiste.setPrecioCompra(item.getPrecioCompra());
                            conceptoExiste.setPrecioMinimoVenta(item.getPrecioMinimoVenta());
                            conceptoExiste.setStockMinimo(item.getStockMinimo());
                            if(item.getStockMinimo()>0){
                                conceptoExiste.setStockAvisoMin((short)1);
                            }else{
                                conceptoExiste.setStockAvisoMin((short)-1);
                            }
                            conceptoExiste.setVolumen(item.getVolumen());
                            conceptoExiste.setPeso(item.getPeso());
                            conceptoExiste.setObservaciones(item.getObservaciones());
                            conceptoExiste.setPrecioUnitarioGranel(item.getPrecioUnitarioGranel());
                            conceptoExiste.setPrecioMedioGranel(item.getPrecioMedioGranel());
                            conceptoExiste.setPrecioMayoreoGranel(item.getPrecioMayoreoGranel());
                            conceptoExiste.setPrecioEspecialGranel(item.getPrecioEspecialGranel());


                            //update
                            conceptoDaoImpl.update(conceptoExiste.createPk(), conceptoExiste);

                            /////// ACTUALIZAMOS EL REGISTRO DE EXISTENCIA DE ALMACEN COMO SI FUERA NUEVO MOVIMIENTO DE ENTRADA, SI NO EXISTE EL REGISTRO DE EXISTENCIA LO CREAMOS
                                  ExistenciaAlmacen almacenExists = null;
                                  boolean nuevo = false;
                                  try{
                                      almacenExists  = new ExistenciaAlmacenBO(this.conn).getExistenciaProductoAlmacen(item.getIdAlmacen(), conceptoExiste.getIdConcepto());
                                  }catch(Exception e){}
                                  if(almacenExists==null){
                                      nuevo = true;
                                      almacenExists = new ExistenciaAlmacen();
                                      almacenExists.setIdAlmacen(item.getIdAlmacen());
                                      almacenExists.setIdConcepto(conceptoExiste.getIdConcepto());
                                      almacenExists.setExistencia(0);
                                  }

                                  BigDecimal numArticulosDisponibles = (new BigDecimal(almacenExists!=null?almacenExists.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                                  BigDecimal contaMovimiento = (new BigDecimal(item.getNumArticulosDisponibles())).setScale(2, RoundingMode.HALF_UP);
                                  BigDecimal stockTotal = numArticulosDisponibles.add(contaMovimiento);

                                  almacenExists.setEstatus(1);
                                  almacenExists.setExistencia(stockTotal.doubleValue());

                                  if(nuevo){//verificaoms si actualizamos el registro o creamos unos nuevo
                                      existenciaAlmacenDaoImpl.insert(almacenExists); 
                                 }else{
                                      existenciaAlmacenDaoImpl.update(almacenExists.createPk(), almacenExists);
                                 }

                                  /**
                                   * Insertamos movimiento entrada inicial*
                                   */
                                  Movimiento movimientoDto = new Movimiento();                    

                                  movimientoDto.setIdEmpresa((int)idEmpresa);
                                  movimientoDto.setTipoMovimiento("ENTRADA");
                                  movimientoDto.setNombreProducto(item.getNombre());
                                  movimientoDto.setContabilidad(item.getNumArticulosDisponibles());
                                  movimientoDto.setIdProveedor(-1);
                                  movimientoDto.setOrdenCompra("");
                                  movimientoDto.setNumeroGuia("");
                                  movimientoDto.setIdAlmacen(item.getIdAlmacen());
                                  movimientoDto.setConceptoMovimiento("Actualizacion de Producto");
                                  movimientoDto.setFechaRegistro(new Date());
                                  movimientoDto.setIdConcepto(conceptoExiste.getIdConcepto());
                                  movimientosDaoImpl.insert(movimientoDto);                                
                            ///////

                           }
                    }else{//como trae ID de cliente se trata de una actualización de precios
                        ///--/-// ADEMAS DE INSERTA EL CONCEPTO, DE QUE NO EXISTIO, SI TRAE UN CLIENTE RELACIONADO, LO RELACIONAMOS:
                            if(item.getClientePrecioConcepto().getIdCliente() > 0){
                                //verificamos si ya existe la relacion del precio de concepto y cliente
                                ClientePrecioConcepto clientePrecioConcepto = null;                        
                                try{                            
                                    clientePrecioConcepto = new ClientePrecioConceptoBO(item.getClientePrecioConcepto().getIdCliente(), conceptoExiste.getIdConcepto(), this.conn).getClientePrecioConcepto();
                                }catch(Exception e){}
                                if(clientePrecioConcepto != null){//actualizamos el registro:
                                    clientePrecioConcepto.setIdConcepto(conceptoExiste.getIdConcepto());
                                    clientePrecioConcepto.setIdCliente(item.getClientePrecioConcepto().getIdCliente());
                                    clientePrecioConcepto.setPrecioUnitarioCliente(item.getPrecio());
                                    clientePrecioConcepto.setPrecioMedioCliente(item.getPrecioMedioMayoreo());
                                    clientePrecioConcepto.setPrecioMayoreoCliente(item.getPrecioMayoreo());
                                    clientePrecioConcepto.setPrecioEspecialCliente(item.getPrecioEspecial());
                                    clientePrecioConcepto.setPrecioDocenaCliente(item.getPrecioDocena());
                                    clientePrecioConcepto.setPrecioUnitarioCliente(item.getPrecio());
                                    clientePrecioConcepto.setIdEstatus(1);
                                    clientePrecioConcepto.setPrecioUnitarioGranelCliente(item.getPrecioUnitarioGranel());
                                    clientePrecioConcepto.setPrecioMedioGranelCliente(item.getPrecioMedioGranel());
                                    clientePrecioConcepto.setPrecioMayoreoGranelCliente(item.getPrecioMayoreoGranel());
                                    clientePrecioConcepto.setPrecioEspecialGranelCliente(item.getPrecioEspecialGranel());
                                    clientePrecioConceptoDaoImpl.update(clientePrecioConcepto.createPk(), clientePrecioConcepto);
                                }else{
                                    clientePrecioConcepto = new ClientePrecioConcepto();
                                    clientePrecioConcepto.setIdConcepto(conceptoExiste.getIdConcepto());
                                    clientePrecioConcepto.setIdCliente(item.getClientePrecioConcepto().getIdCliente());
                                    clientePrecioConcepto.setPrecioUnitarioCliente(item.getPrecio());
                                    clientePrecioConcepto.setPrecioMedioCliente(item.getPrecioMedioMayoreo());
                                    clientePrecioConcepto.setPrecioMayoreoCliente(item.getPrecioMayoreo());
                                    clientePrecioConcepto.setPrecioEspecialCliente(item.getPrecioEspecial());
                                    clientePrecioConcepto.setPrecioDocenaCliente(item.getPrecioDocena());
                                    clientePrecioConcepto.setPrecioUnitarioCliente(item.getPrecio());
                                    clientePrecioConcepto.setIdEstatus(1);
                                    clientePrecioConcepto.setPrecioUnitarioGranelCliente(item.getPrecioUnitarioGranel());
                                    clientePrecioConcepto.setPrecioMedioGranelCliente(item.getPrecioMedioGranel());
                                    clientePrecioConcepto.setPrecioMayoreoGranelCliente(item.getPrecioMayoreoGranel());
                                    clientePrecioConcepto.setPrecioEspecialGranelCliente(item.getPrecioEspecialGranel());
                                    clientePrecioConceptoDaoImpl.insert(clientePrecioConcepto);
                                }

                            }
                        ///--/-// FIN DE RELACIONAR EL CLIENTE Y EL PRECIO DEL CONCEPTO
                    }

                }
            
            }catch(Exception ex){
                System.out.println("Problemas al guardar Concepto");
                logActualizacionInsertado += "No se pudo insertar / actualizar el regitro del Concepto.  Registro numero: "+ i +", Error: "+ex.getMessage()+". <br/>";            
            }
            
            //************FIN REGISTROS DE LOS CONCEPTOS 
            
            
            
            
        }//for
        
        
          return logActualizacionInsertado;
    }
    
    
}
