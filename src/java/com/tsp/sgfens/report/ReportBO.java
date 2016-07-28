/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sgfens.report;

import com.tsp.sct.bo.*;
import com.tsp.sct.dao.dto.*;
import com.tsp.sct.dao.exceptions.AlmacenDaoException;
import com.tsp.sct.dao.exceptions.DatosUsuarioDaoException;
import com.tsp.sct.dao.exceptions.EmpleadoInventarioRepartidorDaoException;
import com.tsp.sct.dao.exceptions.EmpresaDaoException;
import com.tsp.sct.dao.exceptions.NominaComprobanteDescripcionPercepcionDeduccionDaoException;
import com.tsp.sct.dao.exceptions.SgfensPedidoDaoException;
import com.tsp.sct.dao.jdbc.AlmacenDaoImpl;
import com.tsp.sct.dao.jdbc.ClienteCampoContenidoDaoImpl;
import com.tsp.sct.dao.jdbc.ClienteDaoImpl;
import com.tsp.sct.dao.jdbc.ComodatoDaoImpl;
import com.tsp.sct.dao.jdbc.DatosUsuarioDaoImpl;
import com.tsp.sct.dao.jdbc.EmpleadoDaoImpl;
import com.tsp.sct.dao.jdbc.EmpleadoInventarioRepartidorDaoImpl;
import com.tsp.sct.dao.jdbc.EmpresaDaoImpl;
import com.tsp.sct.dao.jdbc.InventarioHistoricoVendedorDaoImpl;
import com.tsp.sct.dao.jdbc.NominaComprobanteDescripcionPercepcionDeduccionDaoImpl;
import com.tsp.sct.dao.jdbc.RutaMarcadorDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensPedidoDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensPedidoProductoDaoImpl;
import com.tsp.sct.dao.jdbc.SgfensVisitaClienteDaoImpl;
import com.tsp.sct.util.Abcdario;
import com.tsp.sct.util.Converter;
import com.tsp.sct.util.ConvertidorCoordenadasVerificadorPoligonos;
import com.tsp.sct.util.DateManage;
import com.tsp.sct.util.Encrypter;
import com.tsp.sct.util.StringManage;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.StringTokenizer;
import com.tsp.sct.util.FormatUtil;
import com.tsp.sct.util.GenericMethods;

/**
 *
 * @author ISCesarMartinez  poseidon24@hotmail.com
 * @date 17-dic-2012 
 */
public class ReportBO {
    
    public static final int DATA_STRING = 1;
    public static final int DATA_INT = 2;
    public static final int DATA_DECIMAL = 3;
    public static final int DATA_DATE = 4;
    public static final int DATA_DATETIME = 5;
    public static final int DATA_BOOLEAN = 6;
    
    
    public static final int CUSTOM_REPORT = -1;
    public static final int USER_REPORT = 1;
    public static final int CERTIFICADO_CSD_REPORT = 2;
    public static final int SUCURSAL_REPORT = 3;
    public static final int CLIENTE_REPORT = 4;
    public static final int IMPUESTO_REPORT = 5;
    public static final int PROVEEDOR_REPORT = 6;
    public static final int ALMACEN_REPORT = 7;
    public static final int CATEGORIA_REPORT = 8;
    public static final int EMBALAJE_REPORT = 9;
    public static final int MARCA_REPORT = 10;
    public static final int PRODUCTO_REPORT = 11;
    public static final int SERVICIO_REPORT = 12;
    public static final int PROSPECTO_REPORT = 13;
    public static final int USUARIO_ACCESOS = 14;
    public static final int USUARIO_ACCIONES = 15;
    public static final int PRODUCTO_MOVIMIENTOS = 16;
    public static final int COBRANZA_ABONOS = 17;
    public static final int COTIZACION_REPORT = 20;
    public static final int PEDIDO_REPORT = 21;
    public static final int FACTURA_REPORT = 22;
    public static final int COTIZACION_REPRESENTACION_IMPRESA = 23;
    public static final int PEDIDO_REPRESENTACION_IMPRESA = 24;
    public static final int COBRANZA_EDO_CUENTA = 25;
    public static final int FACTURA_TOTAL_X_SUCURSAL_REPORT = 26;
    public static final int COBRANZA_ABONO_REPORT = 27;
    public static final int FACTURA_NOMINA_REPORT = 28;
    public static final int PRODUCTO_INVENTARIO_REPORT = 29;
    public static final int CLIENTE_SIN_COMPRA_REPORT = 30;
    public static final int EMPLEADO_CORTECAJA_REPORT = 31;
    public static final int PRODUCTOS_VENTAS_REPORT = 32;   
    public static final int PEDIDO_DEVOLUCION_CAMBIO_REPORT = 33;
    public static final int PRODUCTOS_MAS_VENTAS_REPORT = 34; 
    public static final int PRODUCTOS_MENOS_VENTAS_REPORT = 35; 
    public static final int ARQUEO_DE_CAJA_REPORT = 36; 
    public static final int PAGOS_TARJETA_REPORT = 37; 
    public static final int BITACORA_POSICION_REPORT = 38; 
    public static final int MENSAJES_REPORT = 39; 
    public static final int EMPLEADO_INVENTARIO_REPORT = 40; 
    public static final int EMPLEADO_CORTE_INVENTARIO_REPORT = 41;
    public static final int FACTURA_SAR_REPORT = 42;
    public static final int CXC_LIST_REPORT = 43;
    public static final int CXP_LIST_REPORT = 44;
    public static final int EMPLEADO_PRODUCTIVIDAD = 45;
    public static final int BITACORA_CR_OPERACION_REPORT = 46;
    public static final int CXC_FACTURAS_LIST_REPORT = 47;
    public static final int CXC_PEDIDOS_LIST_REPORT = 48;
    public static final int POS_ESTACION_REPORT = 49;
    public static final int DICCIONARIO_I_E_REPORT = 50;
    public static final int PRECIOS_ESPECIALES_REPORT =51;
    public static final int EXISTENCIA_ALMACENES_REPORT =52;
    public static final int GASTOS_REPORT =53;
    public static final int REFERENCIA_REPORT = 54;
    
    public static final int PEDIDO_CONSIGNA_REPORT = 55;
    public static final int PEDIDO_CONSIGNA_INVENTARIO_REPORT = 56;
    public static final int VISITAS_A_CLIENTES_REPORT = 57;
    
    public static final int EMPLEADO_CORTE_INVENTARIO_HISTORICO_REPORT = 58;
    public static final int CRONOMETRO_REPORT = 59;
    
    public static final int RUTAS_REPORT = 60;
    
    public static final int SMS_PLANTILLAS_REPORT = 65;
    public static final int SMS_AGENDA_GRUPOS_REPORT = 66;
    public static final int SMS_AGENDA_DESTINATARIOS_REPORT = 67;
    
    public static final int COMODATO_REPORT = 68;
    public static final int COMODATO_MANTENIMIENTO_REPORT = 69;
    public static final int COMODATO_CONTRATO_REPORT = 70;
    
    public static final int CR_FORMULARIOS_REPORT = 75;
    public static final int CR_FORMULARIO_VALIDACIONES_REPORT = 76;
    public static final int CR_GRUPOS_FORMULARIO_REPORT = 77;
    public static final int CR_SCORE_REPORT = 78;
    public static final int CR_SCORE_DETALLE_REPORT = 79;
    public static final int CR_PRODUCTO_CREDITO_REPORT = 80;
    public static final int CR_DOC_IMPRIMIBLE_REPORT = 81;
    public static final int CR_DOC_IMP_PARAMETRO_REPORT = 82;
    
    private int tipoReporte = 0;
   
    private UsuarioBO usuarioBO = null;

    private Connection conn = null;
    
    //Flag para Indicar si al generar los reportes al final se genera una fila 
    // con totales de acuerdo al tipo de Campo específicado en el reporte
    private boolean totalDecimalFields = false;
    private boolean totalIntegerFields = false;

    /*
    public ReportBO() {
    }*/

    public ReportBO(Connection conn) {
        this.conn = conn;
    }
    
    public Connection getConn() {
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

    public boolean isTotalDecimalFields() {
        return totalDecimalFields;
    }

    public void setTotalDecimalFields(boolean totalDecimalFields) {
        this.totalDecimalFields = totalDecimalFields;
    }

    public boolean isTotalIntegerFields() {
        return totalIntegerFields;
    }

    public void setTotalIntegerFields(boolean totalIntegerFields) {
        this.totalIntegerFields = totalIntegerFields;
    }
    
    public static String getTitle(int REPORT){
        String title = "Reporte";
        switch(REPORT){
            case USER_REPORT:
                title = "Reporte de Usuarios";
                break;
            case CERTIFICADO_CSD_REPORT:
                title = "Reporte de Certificados CSD";
                break;
            case SUCURSAL_REPORT:
                title = "Reporte de Sucursales";
                break;
            case CLIENTE_REPORT:
                title = "Reporte de Clientes";
                break;
            case IMPUESTO_REPORT:
                title = "Reporte de Impuestos";
                break;
            case PROVEEDOR_REPORT:
                title = "Reporte de Proveedores";
                break;
            case ALMACEN_REPORT:
                title = "Reporte de Almacenes Físicos";
                break;
            case CATEGORIA_REPORT:
                title = "Reporte de Categorías";
                break;
            case EMBALAJE_REPORT:
                title = "Reporte de Embalajes";
                break;
            case MARCA_REPORT:
                title = "Reporte de Marcas";
                break;
            case PRODUCTO_REPORT:
                title = "Reporte de Productos";
                break;
            case SERVICIO_REPORT:
                title = "Reporte de Servicios";
                break;
            case PROSPECTO_REPORT:
                title = "Reporte de Prospectos";
                break;
            case COTIZACION_REPORT:
                title = "Reporte de Cotizaciones";
                break;
            case PEDIDO_REPORT:
                title = "Reporte de Pedidos";
                break;
            case FACTURA_REPORT:
                title = "Reporte de Facturas";
                break;
            case COTIZACION_REPRESENTACION_IMPRESA:
                title = "COTIZACIÓN";
                break;
            case PEDIDO_REPRESENTACION_IMPRESA:
                title = "PEDIDO";
                break;
            case USUARIO_ACCESOS:
                title = "ACCESOS DE USUARIO LOG-IN/OUT";
                break;
            case USUARIO_ACCIONES:
                title = "ACCIONES DE USUARIO (NAVEGACIÓN)";
                break;
            case PRODUCTO_MOVIMIENTOS:
                title = "MOVIMIENTOS (PRODUCTO)";
                break;
            case COBRANZA_EDO_CUENTA:
                title= "ESTADO DE CUENTA (COBRANZA-ABONOS)";
                break;
            case FACTURA_TOTAL_X_SUCURSAL_REPORT:
                title= "Facturas - Consumos por Sucursal";
                break;
            case COBRANZA_ABONO_REPORT:
                title= "Cobranza Abonos";
                break;
            case FACTURA_NOMINA_REPORT:
                title = "Reporte de Nómina";
                break;
            case PRODUCTO_INVENTARIO_REPORT:
                title = "Reporte de Inventario";
                break;
            case CLIENTE_SIN_COMPRA_REPORT:
                title = "Reporte de Clientes Sin Compra";
                break;
            case EMPLEADO_CORTECAJA_REPORT:
                title = "Reporte de Corte de Caja";
                break;            
            case PRODUCTOS_VENTAS_REPORT:
                title = "Reporte de Productos Vendidos";
                break;
            case PEDIDO_DEVOLUCION_CAMBIO_REPORT:
                title = "Reporte Devoluciones - Cambios";
                break;
            case PRODUCTOS_MAS_VENTAS_REPORT:
                title = "Reporte de Productos Mas Vendidos";
                break;
            case PRODUCTOS_MENOS_VENTAS_REPORT:
                title = "Reporte de Productos Menos Vendidos";
                break;
            case ARQUEO_DE_CAJA_REPORT:
                title = "Reporte de Arqueo de Caja";
                break;
            case PAGOS_TARJETA_REPORT:
                title = "Reporte de Pagos con Tarjeta";
                break;
            case BITACORA_POSICION_REPORT:
                title = "Reporte de Historial de Posiciones";
                break;
            case MENSAJES_REPORT:
                title = "Reporte de Mensajes";
                break;
            case EMPLEADO_INVENTARIO_REPORT:
                title = "Reporte Inventario de Empleado";
                break;
            case EMPLEADO_CORTE_INVENTARIO_REPORT:
                title = "Reporte Corte de Inventario de Empleado";
                break;
            case FACTURA_SAR_REPORT:
                title = "Reporte Facturas con Conexion SAR";
                break;
            case CXC_LIST_REPORT:
                title = "Reporte CxC";
                break;
            case CXP_LIST_REPORT:
                title = "Reporte CxP";
                break;
            case EMPLEADO_PRODUCTIVIDAD:
                title = "Reporte Productividad de Empleado";
                break;
            case BITACORA_CR_OPERACION_REPORT:
                title = "Reporte Bitácora Créditos de Operación";
                break;
            case CXC_FACTURAS_LIST_REPORT:
                title = "Reporte CxC Facturas";
                break;
            case CXC_PEDIDOS_LIST_REPORT:
                title = "Reporte CxC Pedidos";
                break;
            case POS_ESTACION_REPORT:
                title = "Reporte de Estaciones Punto de Venta";
                break;
            case DICCIONARIO_I_E_REPORT:
                title = "Reporte de Diccionario Ingresos y Egresos de Caja";
                break;
            case PRECIOS_ESPECIALES_REPORT:
                title = "Reporte de Precios por Cliente";
                break;
            case EXISTENCIA_ALMACENES_REPORT:
                title = "Reporte de Existencias por Almacén";
                break;                
            case GASTOS_REPORT:
                title = "Reporte de Gastos";
                break;
            case REFERENCIA_REPORT:
                title = "Reporte de Referencias";
                break;
            case PEDIDO_CONSIGNA_REPORT:
                title = "Reporte de Pedidos en Consigna";
                break;
            case PEDIDO_CONSIGNA_INVENTARIO_REPORT:
                title = "Reporte de Productos en Consigna";
                break;
            case VISITAS_A_CLIENTES_REPORT:
                title = "Reporte de Visitas a Clientes";
                break;
            case EMPLEADO_CORTE_INVENTARIO_HISTORICO_REPORT:
                title = "Reporte Corte de Inventario Historico de Empleado";
                break;
            case CRONOMETRO_REPORT:
                title = "Reporte Cronometro";
                break;
            case RUTAS_REPORT:
                title = "Reporte de Rutas";
                break;
            case SMS_PLANTILLAS_REPORT:
                title = "Reporte Plantillas SMS";
                break;
            case SMS_AGENDA_GRUPOS_REPORT:
                title = "Reporte Agenda - Grupos";
                break;
            case SMS_AGENDA_DESTINATARIOS_REPORT:
                title = "Reporte Agenda - Destinatarios";
                break;
            case COMODATO_REPORT:
                title = "Reporte de Comodatos";
                break;
            case COMODATO_MANTENIMIENTO_REPORT:
                title = "Reporte de Mantenimiento de Comodatos";
                break;
            case COMODATO_CONTRATO_REPORT:
                title = "Reporte de Contratos de Comodatos";
                break;
            case CR_FORMULARIOS_REPORT:
                title = "Reporte Formularios";
                break;
            case CR_FORMULARIO_VALIDACIONES_REPORT:
                title = "Reporte Validaciones para Formularios";
                break;
            case CR_GRUPOS_FORMULARIO_REPORT:
                title = "Reporte Grupos de Formularios";
                break;
            case CR_SCORE_REPORT:
                title = "Reporte Score";
                break;
            case CR_SCORE_DETALLE_REPORT:
                title = "Reporte Detalles de Score";
                break;
            case CR_PRODUCTO_CREDITO_REPORT:
                title = "Reporte Producto Crédito";
                break;
            case CR_DOC_IMPRIMIBLE_REPORT:
                title = "Reporte Documentos Imprimibles";
                break;
            case CR_DOC_IMP_PARAMETRO_REPORT:
                title = "Reporte Parametros de Doc Imprimibles";
                break;
        }
        
        return title;
    }
    
    /**
     * 
     * 
     * @return String realData
     */
    private String getRealData(HashMap hashField,String data){
        String realData = "";
        try {
            switch(Integer.parseInt(hashField.get("type").toString())){
                case DATA_BOOLEAN:
                    realData = "" + (data.equals("1")?"TRUE":"FALSE");
                    if(hashField.get("mask")!=null && !hashField.get("mask").toString().equals("")){
                        String[] mask = hashField.get("mask").toString().split("\\|");
                        if(realData.equals("TRUE"))
                            realData = mask[0];
                        else
                            realData = mask[1];
                    }
                    break;
                case DATA_DATE:
                    realData = "" + new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(data));
                    if(hashField.get("mask")!=null && !hashField.get("mask").toString().equals("")){
                        realData = "" + new SimpleDateFormat(hashField.get("mask").toString()).format(new SimpleDateFormat("yyyy-MM-dd").parse(data));
                    }
                    break;
                case DATA_DATETIME:
                    if (data!=null){
                        try{
                            realData = "" + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(data));
                            if(hashField.get("mask")!=null && !hashField.get("mask").toString().equals("")){
                                realData = "" + new SimpleDateFormat(hashField.get("mask").toString()).format(new SimpleDateFormat("yyyy-MM-dd").parse(data));
                            }
                        }catch(Exception ex){}
                    }
                    break;
                case DATA_DECIMAL:
                    //realData = "" + Float.parseFloat(data);
                    realData = "" + new BigDecimal(data).setScale(2, RoundingMode.HALF_UP);
                    break;
                case DATA_INT:
                    realData = "" + Integer.parseInt(data);
                    break;
                case DATA_STRING:
                    realData = data;
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return realData;
    }

    /**
     * Devuelve un hash con los datos para el encabezado del reporte
     * 
     * @param String field
     * @param String label
     * @param String fkTable
     * @param String fkField
     * @param String type
     * @param String mask
     * 
     * @return Hash<String,String>
     */
    public HashMap getDataInfo(String field, String label, String fkTable, String fkField, String type, String mask){

        HashMap<String,String> dataMap = new HashMap<String, String>();

        dataMap.put("field", field);
        dataMap.put("label", label);
        dataMap.put("fkTable", fkTable);
        dataMap.put("fkField", fkField);
        dataMap.put("type",type);
        dataMap.put("mask",mask);

        return dataMap;
    }
    
    /**
     * Devuelve un arreglo de hash con los encabezados del reporte
     * 
     * @param int REPORT
     * 
     * @return ArrayList<HashMap>
     */
    public ArrayList<HashMap> getFieldList(int REPORT){
        ArrayList<HashMap> fieldList = new ArrayList<HashMap>();
        int idEmpresa = usuarioBO!=null?usuarioBO.getUser().getIdEmpresa():-1;
        switch(REPORT){
            case USER_REPORT:
                fieldList.add(getDataInfo("ID_USUARIOS","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("SUCURSAL","Sucursal","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("USER_NAME","Usuario","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_ROL","Rol","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break;
            case CERTIFICADO_CSD_REPORT:
                fieldList.add(getDataInfo("ID_CERTIFICADO_DIGITAL","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("NOMBRE_CER","Nombre .Cer","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NOMBRE_KEY","Nombre .Key","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NO_CERTIFICADO","Numero Certificado","","",""+DATA_STRING,""));
                break;
            case SUCURSAL_REPORT:
                fieldList.add(getDataInfo("ID_EMPRESA","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("RFC","RFC","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("RAZON_SOCIAL","Razón Social","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NOMBRE_COMERCIAL","Nombre Comercial","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FOLIOS_DISPONIBLES","Créditos Disponibles","","",""+DATA_INT,""));
                break;
            case CLIENTE_REPORT:
                ClienteCampoContenidoDaoImpl clienteCampoContenidoDaoImpl = new ClienteCampoContenidoDaoImpl(this.conn);
                ClienteCampoAdicional[] clienteCampoAdicionalsDto = new ClienteCampoAdicional[0];
                ClienteCampoAdicionalBO clienteCampoAdicionalBO = new ClienteCampoAdicionalBO(this.conn);
                try{
                    clienteCampoAdicionalsDto = clienteCampoAdicionalBO.findClienteCampoAdicionals(0, idEmpresa , 0, 0, " AND ID_ESTATUS = 1 ");            
                }catch(Exception e){}                
                int camposPersonalizados = 1;
                fieldList.add(getDataInfo("ID_CLIENTE","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("CLAVE","CLAVE","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("RFC","RFC","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("RAZON_SOCIAL","Razón Social","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CONTACTO","Contacto","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CORREO","Correo","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                fieldList.add(getDataInfo("TIPO_CLIENTE","Tipo Cliente","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_REGISTRO","Fecha Registro","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("TELEFONO","Teléfono","","",""+DATA_STRING,""));
                for(ClienteCampoAdicional cliCampo: clienteCampoAdicionalsDto){
                    fieldList.add(getDataInfo("CAMPO_"+camposPersonalizados,cliCampo.getLabelNombre(),"","",""+DATA_STRING,""));
                    camposPersonalizados++;
                }
                
                break;
            case IMPUESTO_REPORT:
                fieldList.add(getDataInfo("ID_IMPUESTO","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("DESCRIPCION","Descripcion","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("PORCENTAJE","Porcentaje %","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("TRASLADADO","Tipo","","",""+DATA_BOOLEAN,"Trasladado|Retenido"));
                fieldList.add(getDataInfo("IMPUESTO_LOCAL","Aplicación","","",""+DATA_BOOLEAN,"Local|Federal"));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break;
            case PROVEEDOR_REPORT:
                fieldList.add(getDataInfo("ID_PROVEEDOR","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("NUMERO_PROVEEDOR","No. Proveedor","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("RFC","RFC","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("RAZON_SOCIAL","Razón Social","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CONTACTO","Contacto","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CORREO","Correo","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break;
             case ALMACEN_REPORT:
                fieldList.add(getDataInfo("ID_ALMACEN","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("AREA_ALMACEN","Área (m2)","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("RESPONSABLE","Responsable","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("TELEFONO","Teléfono","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break;
             case CATEGORIA_REPORT:
                fieldList.add(getDataInfo("ID_CATEGORIA","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("DESCRIPCION","Descripcion","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_CAT_PADRE","Categoría Padre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break;
             case EMBALAJE_REPORT:
                fieldList.add(getDataInfo("ID_EMBALAJE","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("DESCRIPCION","Descripcion","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break;
              case MARCA_REPORT:
                fieldList.add(getDataInfo("ID_MARCA","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("DESCRIPCION","Descripcion","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break;
             case PRODUCTO_REPORT:
                fieldList.add(getDataInfo("ID_CONCEPTO","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("CODIGO","Código","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("DESCRIPCION","Descripcion","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_MARCA","Marca","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_CATEGORIA","Categoria","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_SUBCATEGORIA","Subcategoria","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("PRECIO","Precio","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("STOCK","Stock General","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break;
              case SERVICIO_REPORT:
                fieldList.add(getDataInfo("ID_SERVICIO","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("CODIGO","Código","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("DESCRIPCION","Descripcion","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("PRECIO","Precio","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break;
              case COTIZACION_REPORT:
                fieldList.add(getDataInfo("ID","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("FOLIO","Folio","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_VENDEDOR","Vendedor","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_CLIENTE_PROSPECTO","Cliente/Prospecto","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA","Fecha","","",""+DATA_DATE,""));
                fieldList.add(getDataInfo("TOTAL","Total","","",""+DATA_DECIMAL,""));
                break;
              case PEDIDO_REPORT:
                fieldList.add(getDataInfo("ID","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("FOLIO","Folio","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_VENDEDOR","Vendedor","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_CLIENTE","Cliente","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_PEDIDO","Fecha/Hora Pedido","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_ENTREGA","Fecha Entrega","","",""+DATA_DATE,""));
                fieldList.add(getDataInfo("FK_ESTATUS_PEDIDO","Estatus","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CANTIDAD_PRODUCTOS","Cantidad Productos","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("DIAS_DE_CREDITO","Días Crédito","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("TOTAL","Total","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("PAGADO","Pagado","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ADEUDO","Adeudo","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("COMISION_VENVEDOR","Comisión Vendedor","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("UTILIDAD","Utilidad","","",""+DATA_STRING,""));
                break;
              case PROSPECTO_REPORT:
                fieldList.add(getDataInfo("ID_PROSPECTO","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("RAZON_SOCIAL","Razón Social","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CONTACTO","Contacto","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CORREO","Correo","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_REGISTRO","Fecha Registro","","",""+DATA_STRING,""));
                break;
              case FACTURA_REPORT:
                fieldList.add(getDataInfo("ID","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("UUID","UUID","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FOLIO","Folio","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_CLIENTE","Cliente","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_CAPTURA","Fecha Captura","","",""+DATA_DATE,""));
                fieldList.add(getDataInfo("FK_ESTATUS_COMPROBANTE","Estatus","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("TOTAL","Total","","",""+DATA_DECIMAL,""));
                break;
              case USUARIO_ACCESOS:
                fieldList.add(getDataInfo("FECHA","Fecha Acción","","",""+DATA_DATETIME,""));
                fieldList.add(getDataInfo("FK_USER","Usuario-Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_EMPRESA","Sucursal","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_tipo_bitacora_accion_tipo","Tipo Acción","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("COMENTARIOS","Comentarios","","",""+DATA_STRING,""));
                break;
              case USUARIO_ACCIONES:
                fieldList.add(getDataInfo("FECHA","Fecha Acción","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_USER","Usuario-Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_EMPRESA","Sucursal","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_tipo_bitacora_accion_tipo","Tipo Acción","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("COMENTARIOS","Comentarios","","",""+DATA_STRING,""));
                break;
              case PRODUCTO_MOVIMIENTOS:
                fieldList.add(getDataInfo("ID_MOVIMIENTO","Id Movimiento","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("TIPO_MOVIMIENTO","Tipo Movimiento","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA","Fecha","","",""+DATA_DATETIME,""));                               
                fieldList.add(getDataInfo("PRODUCTO","Producto","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CANTIDAD","Cantidad","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("ALMACEN_ORIGEN","Almacén Origen","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ALMACEN DESTINO","Almacén Destino","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("EMPLEADO","Empleado","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("MOTIVO","Motivo","","",""+DATA_STRING,""));
                break;
              case FACTURA_TOTAL_X_SUCURSAL_REPORT:
                fieldList.add(getDataInfo("EMPRESA","Sucursal","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ESTATUS","Estatus","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NUMERO_COMPROBANTES","No. Facturas","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("SUBTOTALES","Suma SubTotal","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("TOTALES","Suma Total","","",""+DATA_DECIMAL,""));
                break;
              case COBRANZA_ABONO_REPORT:
                fieldList.add(getDataInfo("ID_USUARIO_VENDEDOR","Vendedor","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_CLIENTE","Cliente","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_ABONO","Fecha","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_COBRANZA_METODO_PAGO","Metodo de Pago","","",""+DATA_STRING,""));  
                fieldList.add(getDataInfo("REFERENCIA","Referencia","","",""+DATA_STRING,""));  
                fieldList.add(getDataInfo("ESTATUS","Estatus","","",""+DATA_STRING,""));  
                fieldList.add(getDataInfo("MONTO_ABONO","Monto Cobranza","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("SALDO_PAGADO","Monto Cubierto","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("TOTAL","Monto a Cubrir Total","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("TIPO_PAGO","Tipo Pago","","",""+DATA_STRING,""));
                break;
              case FACTURA_NOMINA_REPORT:
                fieldList.add(getDataInfo("ID","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("FECHA_PAGO","Fecha Pago","","",""+DATA_DATE,""));
                fieldList.add(getDataInfo("FK_NUM_EMPLEADO","# Empleado","","",""+DATA_STRING,""));                
                fieldList.add(getDataInfo("FK_CLIENTE","Empleado","","",""+DATA_STRING,""));                
                fieldList.add(getDataInfo("TOTAL_PERCEPCIONES","Total Percepciones","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("TOTAL_DEDUCCIONES","Total Deducciones","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("FK_ESTATUS_COMPROBANTE","Estatus","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("TOTAL","Total","","",""+DATA_DECIMAL,""));
                break;
              case PRODUCTO_INVENTARIO_REPORT:
                fieldList.add(getDataInfo("ID_CONCEPTO","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("CODIGO","Código","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("PRECIO","Precio","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("STOCK","Stock","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("STOCK_REPARTIDOR","Stock en Repartidores","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("STOCK_TOTAL","Stock Total","","",""+DATA_STRING,""));                
                break;
              case CLIENTE_SIN_COMPRA_REPORT:                
                fieldList.add(getDataInfo("FK_CLIENTE","Cliente","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_VENDEDOR","Vendedor","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_ENTREGA","Fecha Entrega","","",""+DATA_DATE,""));
                fieldList.add(getDataInfo("FECHA_PEDIDO","Fecha Ultimo Pedido","","",""+DATA_DATE,""));
                fieldList.add(getDataInfo("TOTAL","Total","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("SALDO_PAGADO","Saldo Pagado","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("SALDO_PENDIENTE","Saldo Pendiente","","",""+DATA_STRING,""));
                break;
              case EMPLEADO_CORTECAJA_REPORT:
                fieldList.add(getDataInfo("ID_EMPLEADO","ID","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("MUMERO_EMPLEADO","Número de Empleado","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("TOTAL_VENDIDO","Total Vendido","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("COBRANZA","Cobranza","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CREDITO","Crédito","","",""+DATA_STRING,""));   
                break;
              case PRODUCTOS_VENTAS_REPORT:
                fieldList.add(getDataInfo("ID_PEDIDO","Id Pedido","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_VENDEDOR","Vendedor","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_CLIENTE","cliente","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA","Fecha","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CODIGO","Código","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("UNIDADES VENDIDAS","Unidades Vendidas","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("PRECIO_COMPRA_UNITARIO","Precio de Compra Unitario","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("PRECIO_COMPRA_TOTAL","Precio de Compra Total","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("PRECIO_VENTA_UNITARIO","Precio de Venta Unitario","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("PRECIO_VENTA_TOTAL","Precio de Venta Total","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("UTILIDAD","Utilidad","","",""+DATA_STRING,""));   
                break;
              case PRODUCTOS_MAS_VENTAS_REPORT:
                fieldList.add(getDataInfo("ID_PEDIDO","Pedidos que Tienen el Producto","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_VENDEDOR","Vendedor","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_CLIENTE","cliente","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA","Fecha","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CODIGO","Código","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("UNIDADES VENDIDAS","Unidades Vendidas","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("PRECIO_COMPRA_UNITARIO","Precio de Compra Unitario","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("PRECIO_COMPRA_TOTAL","Precio de Compra Total","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("PRECIO_VENTA_UNITARIO","Precio de Venta Unitario","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("PRECIO_VENTA_TOTAL","Precio de Venta Total","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("UTILIDAD","Utilidad","","",""+DATA_STRING,""));   
                break;
              case PRODUCTOS_MENOS_VENTAS_REPORT:
                fieldList.add(getDataInfo("ID_PEDIDO","Pedidos que Tienen el Producto","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_VENDEDOR","Vendedor","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_CLIENTE","cliente","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA","Fecha","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CODIGO","Código","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("UNIDADES VENDIDAS","Unidades Vendidas","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("PRECIO_COMPRA_UNITARIO","Precio de Compra Unitario","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("PRECIO_COMPRA_TOTAL","Precio de Compra Total","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("PRECIO_VENTA_UNITARIO","Precio de Venta Unitario","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("PRECIO_VENTA_TOTAL","Precio de Venta Total","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("UTILIDAD","Utilidad","","",""+DATA_STRING,""));   
                break;
              case PEDIDO_DEVOLUCION_CAMBIO_REPORT:
                fieldList.add(getDataInfo("ID_PEDIDO_DEVOL_CAMBIO","ID","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_EMPLEADO","Vendedor","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_CONCEPTO","Concepto Devuelvo-Cambio","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_TIPO","Tipo","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("APTO_PARA_VENTA","Cantidad Apta Venta","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NO_APTO_PARA_VENTA","Cantidad No Apta Venta","","",""+DATA_STRING,""));                
                fieldList.add(getDataInfo("ID_CLASIFICACION","Clasificación","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_CONCEPTO_ENTREGADO","Concepto Entregado","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("MONTO_RESULTANTE","Monto Diferencia","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("DIFERENCIA_FAVOR","Diferencia","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_PEDIDO_ORIGEN","Pedido Origen: ID / Folio","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA","Fecha","","",""+DATA_STRING,""));
                break;
              case ARQUEO_DE_CAJA_REPORT:
                fieldList.add(getDataInfo("NUMERO_EMPLEADO","Numero de Empleado","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ZONA","Zona","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("TOTAL_VENDIDO","Total Vendido","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("MONTO_COBRADO","Monto Cobrado","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("MONTO_PENDIENTE_POR_COBRAR","Monto Pendiente por Cobrar","","",""+DATA_STRING,"")); 
                fieldList.add(getDataInfo("GASTOS","Gastos","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("DEVOLUCION_A_CLIENTE","Devolución a Cliente","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("MONTO_ENTREGADO_A_EMPRESA","Monto Entregado a Empresa","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ADEUDO_A_EMPRESA","Adeudo a Empresa","","",""+DATA_STRING,""));              
                break;
              case PAGOS_TARJETA_REPORT:
                fieldList.add(getDataInfo("ID","Id","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NO_TARJETA","No. Tarjeta","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NOMBRE_TITULAR","Nombre Titular","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("MONTO","Monto","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("ORDER_ID","Order Id","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_OPERACION","Fecha Operación","","",""+DATA_STRING,""));                
                fieldList.add(getDataInfo("ESTATUS","Estatus","","",""+DATA_STRING,""));                  
                break;
             case BITACORA_POSICION_REPORT:
                fieldList.add(getDataInfo("ID","Id","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("EMPLEADO","Empleado","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA","Fecha","","",""+DATA_DATE,""));
                fieldList.add(getDataInfo("LATITUD","Latitud","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("LONGITUD","Longitud","","",""+DATA_STRING,""));                               
                break;
             case MENSAJES_REPORT:
                fieldList.add(getDataInfo("ID","Id","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("EMISOR_TIPO","Emisor Tipos","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("EMPLEADO_EMISOR","Empleado Emisor","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("EMPLEADO_RECEPTOR","Empleado Receptor","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_EMISION","Fecha Emision","","",""+DATA_STRING,""));                               
                fieldList.add(getDataInfo("FECHA_RECEPCION","Fecha Recepcion","","",""+DATA_STRING,""));  
                fieldList.add(getDataInfo("MENSAJE","Mensaje","","",""+DATA_STRING,""));  
                fieldList.add(getDataInfo("RECIBIDO","Recibido","","",""+DATA_STRING,"")); 
                break;
             case EMPLEADO_INVENTARIO_REPORT:
                fieldList.add(getDataInfo("ID","Id","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("DESCRIPCION","Descripcion","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CODIGO","Código","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CANTIDAD DISPONIBLE","Cantidad Disponible","","",""+DATA_STRING,""));  
                break;
             case EMPLEADO_CORTE_INVENTARIO_REPORT:
                fieldList.add(getDataInfo("ID","Id","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("DESCRIPCION","Descripcion","","",""+DATA_STRING,""));                
                fieldList.add(getDataInfo("CANTIDAD_ASIGNADA","Cantidad Asignada","","",""+DATA_STRING,""));  
                fieldList.add(getDataInfo("CANTIDAD_ENTREGADA","Cantidad Entregada","","",""+DATA_STRING,""));  
                //
                //fieldList.add(getDataInfo("ENTREGADO_POR_PEDIDOS","Entregado por Pedidos","","",""+DATA_STRING,""));  
                fieldList.add(getDataInfo("CANTIDAD_DEVUELTA","Cantidad Devuleta","","",""+DATA_STRING,""));  
                //fieldList.add(getDataInfo("CANTIDAD_CAMBIO","Cantidad a Cambio","","",""+DATA_STRING,""));  
                fieldList.add(getDataInfo("CANTIDAD_CAMBIO_MISMO","Cambio Mismo Producto","","",""+DATA_STRING,""));  
                fieldList.add(getDataInfo("CANTIDAD_CAMBIO_DISTINTO","Cambio Producto Distinto","","",""+DATA_STRING,""));  
                fieldList.add(getDataInfo("CANTIDAD_MERMA","Cantidad en Merma","","",""+DATA_STRING,"")); 
                fieldList.add(getDataInfo("CANTIDAD_VENDIDA","Cantidad Vendida","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("INVENTARIO_FINAL_APTO","Inventario Final/Apto","","",""+DATA_STRING,"")); 
                fieldList.add(getDataInfo("INVENTARIO_FISICO_TOTAL","Inventario Físico Total","","",""+DATA_STRING,""));
                break;
            case FACTURA_SAR_REPORT:
                fieldList.add(getDataInfo("ID","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("UUID","UUID","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_ESTATUS_COMPROBANTE","Estado Preto","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_CLIENTE","Cliente","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_CAPTURA","Fecha Captura","","",""+DATA_DATE,""));
                fieldList.add(getDataInfo("FK_ESTATUS_SAR","Estatus SAR","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("TOTAL","Total","","",""+DATA_DECIMAL,""));
                break;
            case CXC_LIST_REPORT:
                fieldList.add(getDataInfo("TIPO","Tipo","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("FOLIO","Folio","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("SERIE","Serie","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_EMISION","Fecha Emision","","",""+DATA_DATE,""));
                fieldList.add(getDataInfo("FK_CLIENTE","Cliente","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("IMPORTE","Importe","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("SALDO","Saldo","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("DIAS_CREDITO","Dias Cr.","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("ESTATUS_PAGO","E. Pago","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ESTATUS_SEMAFORO","Semaforo","","",""+DATA_STRING,""));
                break;
            case CXP_LIST_REPORT:
                fieldList.add(getDataInfo("TIPO","Tipo","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("FOLIO","Folio","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("SERIE","Serie","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_EMISION","Fecha Emision","","",""+DATA_DATE,""));
                fieldList.add(getDataInfo("EMISOR","Emisor","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("IMPORTE","Importe","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("SALDO","Saldo","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("DIAS_CREDITO","Dias Cr.","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("ESTATUS_PAGO","E. Pago","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ESTATUS_SEMAFORO","Semaforo","","",""+DATA_STRING,""));
                break;
            case EMPLEADO_PRODUCTIVIDAD :               
                fieldList.add(getDataInfo("VENDEDOR","Vendedor","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NO_VENTAS","No. Ventas","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("VENTA_TOTAL","Venta Total","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("COBRANZA","Cobranza Realizada","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("VISITAS_SIN_COBRO","Visitas sin Cobro","","",""+DATA_INT,""));                
                break;
            case BITACORA_CR_OPERACION_REPORT:
                fieldList.add(getDataInfo("ID","ID","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("EMPLEADO","Empleado","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CLIENTE","Cliente","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_HR","Fecha-Hr","","",""+DATA_DATETIME,""));
                fieldList.add(getDataInfo("MONTO","Monto ($)","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("CREDITOS","Créditos","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("COMENTARIOS","Información","","",""+DATA_STRING,""));
                break;
            case CXC_FACTURAS_LIST_REPORT:
                fieldList.add(getDataInfo("ID","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("FOLIO","Folio","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("SERIE","Serie","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_EMISION","Fecha Emision","","",""+DATA_DATE,""));
                fieldList.add(getDataInfo("FK_CLIENTE","Cliente","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("IMPORTE","Importe","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("SALDO","Saldo","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("DIAS_CREDITO","Dias Cr.","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("ESTATUS_PAGO","E. Pago","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ESTATUS_SEMAFORO","Semaforo","","",""+DATA_STRING,""));
                break;
            case CXC_PEDIDOS_LIST_REPORT:
                fieldList.add(getDataInfo("ID","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("FOLIO","Folio","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_EMISION","Fecha Emision","","",""+DATA_DATE,""));
                fieldList.add(getDataInfo("FK_CLIENTE","Cliente","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("IMPORTE","Importe","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("SALDO","Saldo","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("DIAS_CREDITO","Dias Cr.","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("ESTATUS_PAGO","E. Pago","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ESTATUS_SEMAFORO","Semaforo","","",""+DATA_STRING,""));
                break;
            case POS_ESTACION_REPORT:
                fieldList.add(getDataInfo("ID_POS_ESTACION","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("IDENTIFICACION","Identificacion","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_HR","Fecha/Hr Ult Conexion","","",""+DATA_DATETIME,""));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break;
            case DICCIONARIO_I_E_REPORT:
                fieldList.add(getDataInfo("ID","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("DESCRIPCION","Descripcion","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ES_INGRESO","Tipo","","",""+DATA_BOOLEAN,"Ingreso|Egreso"));
                fieldList.add(getDataInfo("ES_GENERAL","General","","",""+DATA_BOOLEAN,"SI|NO"));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break;
            case PRECIOS_ESPECIALES_REPORT:                
                fieldList.add(getDataInfo("CLIENTE","Cliente","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CONCEPTO","Concepto","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("PRECIO_UNITARIO","Precio Unitario","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("PRECIO_MEDIO","Precio Medio","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("PRECIO_MAYOREO","Precio Mayoreo","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("PRECIO_DOCENA","Precio Docena","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("PRECIO_ESPECIAL","Precio Especial","","",""+DATA_DECIMAL,""));
                break;
            case EXISTENCIA_ALMACENES_REPORT:
                fieldList.add(getDataInfo("ALMACEN","Almacén","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CONCEPTO","Concepto","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("EXISTENCIA","Existencia","","",""+DATA_DECIMAL,""));                
                break;                
            case GASTOS_REPORT:
                fieldList.add(getDataInfo("ID","Id","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("CONCEPTO","Concepto","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("EMPLEADO","Empleado","","",""+DATA_STRING,""));                
                fieldList.add(getDataInfo("MONTO","Monto","","",""+DATA_DECIMAL,""));   
                fieldList.add(getDataInfo("FECHA","Fecha","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("COMENTARIO","Comentario","","",""+DATA_STRING,""));                  
                break;
            case REFERENCIA_REPORT:     
                fieldList.add(getDataInfo("SUCURSAL","Sucursal","","",""+DATA_STRING,"")) ;
                fieldList.add(getDataInfo("EMPLEADO","Empleado","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("REFERENCIA","Referencia","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA","Fecha","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("TOTAL","Total","","",""+DATA_DECIMAL,""));
                fieldList.add(getDataInfo("NUMERO_DE_PAGOS","Número de Pagos","","",""+DATA_INT,""));                
                break;
            case PEDIDO_CONSIGNA_REPORT:
                fieldList.add(getDataInfo("ID","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("FECHA","Fecha de Entrega","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_VENDEDOR","Vendedor","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_CLIENTE","Cliente","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CANTIDAD_PRODUCTOS","Cantidad Productos","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("TOTAL","Total","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("COMISION_VENVEDOR","Comisión Consignatario","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("UTILIDAD","Utilidad","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ESTADO","Estado","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ESTATUS","Estatus","","",""+DATA_STRING,""));                
                break;
            case PEDIDO_CONSIGNA_INVENTARIO_REPORT:
                fieldList.add(getDataInfo("ID","ID","","",""+DATA_INT,""));
                //fieldList.add(getDataInfo("FECHA","Fecha de Entrega","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_VENDEDOR","Vendedor","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_CLIENTE","Cliente","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("PRODUCTO","Producto","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CANTIDAD","Cantidad","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("PRECIOUNI","Precio Unitario","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("TOTAL","Total","","",""+DATA_STRING,""));                              
                break;
            case VISITAS_A_CLIENTES_REPORT:
                fieldList.add(getDataInfo("ID","ID","","",""+DATA_INT,""));              
                fieldList.add(getDataInfo("FK_VENDEDOR","Vendedor","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_CLIENTE","Cliente","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("REGISTRO","Registro","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA","Fecha","","",""+DATA_STRING,""));    
                fieldList.add(getDataInfo("COMENTARIOS","Comentarios","","",""+DATA_STRING,""));
                break;
            case EMPLEADO_CORTE_INVENTARIO_HISTORICO_REPORT:
                fieldList.add(getDataInfo("ID","Id","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("DESCRIPCION","Descripcion","","",""+DATA_STRING,""));                
                fieldList.add(getDataInfo("CANTIDAD_ASIGNADA_0","Inventario Inicial","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CANTIDAD_ASIGNADA","Inventario Asignado","","",""+DATA_STRING,""));  
                fieldList.add(getDataInfo("CANTIDAD_ASIGNADA_2","Inventario Total","","",""+DATA_STRING,""));  
                fieldList.add(getDataInfo("CANTIDAD_ENTREGADA1","Cantidad Entregada","","",""+DATA_STRING,"")); 
                fieldList.add(getDataInfo("CANTIDAD_DEVUELTA","Cantidad Devuleta","","",""+DATA_STRING,"")); 
                fieldList.add(getDataInfo("CANTIDAD_CAMBIO_MISMO","Cambio Mismo Producto","","",""+DATA_STRING,""));  
                fieldList.add(getDataInfo("CANTIDAD_CAMBIO_DISTINTO","Cambio Producto Distinto","","",""+DATA_STRING,""));  
                fieldList.add(getDataInfo("INVENTARIO_FINAL_APTO","Inventario Final/Apto","","",""+DATA_STRING,""));                 
                fieldList.add(getDataInfo("INVENTARIO_FISICO_TOTAL","Inventario Físico Total","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CANTIDAD_MERMA","Cantidad en Merma","","",""+DATA_STRING,""));                 
                fieldList.add(getDataInfo("DEVOLUCION_APTOS","Devueltos/Cambio Aptos","","",""+DATA_STRING,"")); 
                fieldList.add(getDataInfo("ENTREGA_PENDIENTE","Pendiente de Entrega","","",""+DATA_STRING,"")); 
                fieldList.add(getDataInfo("CANTIDAD_VENDIDA","Entregado por Ventas","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("MONTOS_TOTALES","Monto de Venta","","",""+DATA_STRING,"")); 
                break;    
            case CRONOMETRO_REPORT:
                fieldList.add(getDataInfo("ID","ID","","",""+DATA_INT,""));              
                fieldList.add(getDataInfo("FK_VENDEDOR","Vendedor","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_CLIENTE","Cliente","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_INICIO","Fecha Inicio","","",""+DATA_STRING,""));    
                fieldList.add(getDataInfo("FECHA_FIN","Fecha Fin","","",""+DATA_STRING,""));    
                fieldList.add(getDataInfo("TIEMPO","TIEMPO","","",""+DATA_STRING,""));
                break;
            case RUTAS_REPORT:
                fieldList.add(getDataInfo("ID_RUTA","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_CREACION","Fecha de Creación/Modificación","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("EJECUTIVO","Ejecutivo Asignado","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CANTIDAD_PUNTOS","Cantidad de Puntos","","",""+DATA_STRING,""));
                
                break;
            case SMS_PLANTILLAS_REPORT:
                fieldList.add(getDataInfo("ID_PLANTILLA","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("ASUNTO","Asunto","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_CREACION","Fecha de Creación","","",""+DATA_DATETIME,""));
                fieldList.add(getDataInfo("ULTIMO ENVIO","Último Envío","","",""+DATA_DATETIME,""));
                fieldList.add(getDataInfo("ENVIADOS","Enviados","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break;
            case SMS_AGENDA_GRUPOS_REPORT:
                fieldList.add(getDataInfo("ID_SMS_GRUPO","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("DESCRIPCION","Descripción","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NO_INTEGRANTES","Numéro de Integrantes","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break;
            case SMS_AGENDA_DESTINATARIOS_REPORT:
                fieldList.add(getDataInfo("ID_SMS_DEST","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NUM_CEL","Num. Celular","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("C_EXTRA_1","Campo 1","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("C_EXTRA_2","Campo 2","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("C_EXTRA_3","Campo 3","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("C_EXTRA_4","Campo 4","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_GRUPO","Grupo","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break;
            case COMODATO_REPORT:
                fieldList.add(getDataInfo("ID_COMODATO","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NUMERO_SERIE","Numero de Serie","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("MARCA","Marca","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("MODELO","Modelo","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("TIPO","Tipo","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CAPACIDAD","Capacidad","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_ALMACEN","Almacén","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ESTATUS","Estatus","","",""+DATA_STRING,""));
                break;
            case COMODATO_MANTENIMIENTO_REPORT:
                fieldList.add(getDataInfo("ID_COMODATO_MANTENIMIENTO","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("ID_CLIENTE","Nombre de Cliente","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_COMODATO","Equipo","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_REALIZACION_MANTENIMIENTO","Fecha Realización","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("TECNICO","Técnico","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_PROX_MANTENIMIENTO","Fecha Proximo Mantenimiento","","",""+DATA_STRING,""));
                break;
            case COMODATO_CONTRATO_REPORT:
                fieldList.add(getDataInfo("ID_COMODATO","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("ID_CLIENTE","Cliente","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NOMBRE","Comodato","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_ASIGNACION_CLIENTE","Fecha Asignación","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ESTATUS","Estatus","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FECHA_SUBIDA_CONTRATO","Fecha Subida Contrato","","",""+DATA_STRING,""));
                break;
            case CR_FORMULARIOS_REPORT:
                fieldList.add(getDataInfo("ID_FORMULARIO","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("FK_CR_GRUPO_FRM","Pertenece a","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ORDEN","Orden","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("DESCRIPCION","Descripción","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CREADO","Creado","","",""+DATA_DATE,""));
                fieldList.add(getDataInfo("EDITADO","Editado","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break;
            case CR_FORMULARIO_VALIDACIONES_REPORT:
                fieldList.add(getDataInfo("ID_FRM_VALIDACION","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("DESCRIPCION","Descripción","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("REGEX_JAVA","Expresión Reg. JAVA","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("IS_SISTEMA","De Sistema","","",""+DATA_BOOLEAN,"Si|-"));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break;
            case CR_GRUPOS_FORMULARIO_REPORT:
                fieldList.add(getDataInfo("ID_GRUPO_FORMULARIO","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("DESCRIPCION","Descripción","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CREADO","Creado","","",""+DATA_DATE,""));
                fieldList.add(getDataInfo("NO_FORMULARIOS","No. Formularios","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break; 
            case CR_SCORE_REPORT:
                fieldList.add(getDataInfo("ID_SCORE","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("DESCRIPCION","Descripción","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CREADO","Creado","","",""+DATA_DATE,""));
                fieldList.add(getDataInfo("EDITADO","Editado","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break;
            case CR_SCORE_DETALLE_REPORT:
                fieldList.add(getDataInfo("ID_SCORE_DETALLE","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("FK_FORMULARIO","Formulario","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_CAMPO","Campo","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CRITERIO","Criterio","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("PUNTOS","Puntos","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break;
            case CR_PRODUCTO_CREDITO_REPORT:
                fieldList.add(getDataInfo("ID_PRODUCTO_CREDITO","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("FK_PRODUCTO_PADRE","Padre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_FORMULARIOS","Formularios","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_SCORE","Score","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("FK_IMPRIMIBLES","Imprimibles","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CREADO","Creado","","",""+DATA_DATE,""));
                fieldList.add(getDataInfo("EDITADO","Editado","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break;
            case CR_DOC_IMPRIMIBLE_REPORT:
                fieldList.add(getDataInfo("ID_DOC_IMP","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("NOMBRE","Nombre","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("DESCRIPCION","Descripción","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("TIPO","Tipo","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("NOMBRE_ARCHIVO","Archivo Plantilla","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("CREADO","Creado","","",""+DATA_DATE,""));
                fieldList.add(getDataInfo("EDITADO","Editado","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break;
            case CR_DOC_IMP_PARAMETRO_REPORT:
                fieldList.add(getDataInfo("ID_DOC_IMP_PARAMETRO","ID","","",""+DATA_INT,""));
                fieldList.add(getDataInfo("PARAMETRO","Parámetro","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("DESCRIPCION","Descripción","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("VALOR_DEFECTO","Valor Defecto","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ASOCIA_VARIABLE_FORMULA","Variable Form Asociada","","",""+DATA_STRING,""));
                fieldList.add(getDataInfo("ID_ESTATUS","Estatus","","",""+DATA_BOOLEAN,"Activo|Inactivo"));
                break;
        }
        return fieldList;
    }
    
    /**
     * Devuelve una lista con los valores del reporte seleccionado
     * 
     * @param int report - Tipo de reporte
     * @param String params - Parámetros de búsqueda
     * 
     * @return ArrayList<HashMap> - Arreglo de hash con los datos
     */
    public ArrayList<HashMap> getDataReport(int report, String params, String paramsExtra) throws Exception{
        tipoReporte = report;
        int idEmpresa = usuarioBO!=null?usuarioBO.getUser().getIdEmpresa():-1;
        String paramsDefault ="";
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        switch(report){
            case USER_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new UsuariosBO(this.conn).findUsuarios(-1, idEmpresa, 0, 0, params));
                    //dataList = this.getDataList(new UsuariosDaoImpl().findByDynamicWhere(params, new Object[0]));
                else
                    dataList = this.getDataList(new UsuariosBO(this.conn).findUsuarios(-1, idEmpresa, 0, 0, ""));
                    //dataList = this.getDataList(new UsuariosDaoImpl().findAll());
                break;
           case CERTIFICADO_CSD_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new CertificadoDigitalBO(this.conn).findCertificadosDigitales(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new CertificadoDigitalBO(this.conn).findCertificadosDigitales(-1, idEmpresa, 0, 0, ""));
                break;
            case SUCURSAL_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new EmpresaBO(this.conn).findEmpresas(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new EmpresaBO(this.conn).findEmpresas(-1, idEmpresa, 0, 0, ""));
                break;   
            case CLIENTE_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new ClienteBO(this.conn).findClientes(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new ClienteBO(this.conn).findClientes(-1, idEmpresa, 0, 0, ""));
                break;
            case IMPUESTO_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new ImpuestoBO(this.conn).findImpuestos(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new ImpuestoBO(this.conn).findImpuestos(-1, idEmpresa, 0, 0, ""));
                break;
            case PROVEEDOR_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new SGProveedorBO(this.conn).findProveedor(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new SGProveedorBO(this.conn).findProveedor(-1, idEmpresa, 0, 0, ""));
                break;
            case ALMACEN_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new AlmacenBO(this.conn).findAlmacens(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new AlmacenBO(this.conn).findAlmacens(-1, idEmpresa, 0, 0, ""));
                break;
            case CATEGORIA_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new CategoriaBO(this.conn).findCategorias(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new CategoriaBO(this.conn).findCategorias(-1, idEmpresa, 0, 0, ""));
                break;
            case EMBALAJE_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new EmbalajeBO(this.conn).findEmbalajes(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new EmbalajeBO(this.conn).findEmbalajes(-1, idEmpresa, 0, 0, ""));
                break;
            case MARCA_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new MarcaBO(this.conn).findMarcas(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new MarcaBO(this.conn).findMarcas(-1, idEmpresa, 0, 0, ""));
                break;
            case PRODUCTO_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new ConceptoBO(this.conn).findConceptos(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new ConceptoBO(this.conn).findConceptos(-1, idEmpresa, 0, 0, ""));
                break;
            case SERVICIO_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new ServicioBO(this.conn).findServicios(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new ServicioBO(this.conn).findServicios(-1, idEmpresa, 0, 0, ""));
                break;
            case COTIZACION_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new SGCotizacionBO(this.conn).findCotizacion(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new SGCotizacionBO(this.conn).findCotizacion(-1, idEmpresa, 0, 0, ""));
                break;
            case PEDIDO_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new SGPedidoBO(this.conn).findPedido(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new SGPedidoBO(this.conn).findPedido(-1, idEmpresa, 0, 0, ""));
                break;
            case PROSPECTO_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new SGProspectoBO(this.conn).findProspecto(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new SGProspectoBO(this.conn).findProspecto(-1, idEmpresa, 0, 0, ""));
                break;
            case FACTURA_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new ComprobanteFiscalBO(this.conn).findComprobanteFiscal(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new ComprobanteFiscalBO(this.conn).findComprobanteFiscal(-1, idEmpresa, 0, 0, ""));
                break;
            case USUARIO_ACCESOS:
                paramsDefault = " AND (id_tipo_bitacora_accion_tipo="+SGAccionBitacoraBO.ACCION_LOGIN+" OR id_tipo_bitacora_accion_tipo=" + SGAccionBitacoraBO.ACCION_LOGOUT + " )";
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new SGAccionBitacoraBO(this.conn).findAccionBitacora(-1, idEmpresa, 0, 0, params + paramsDefault));
                else
                    dataList = this.getDataList(new SGAccionBitacoraBO(this.conn).findAccionBitacora(-1, idEmpresa, 0, 0, paramsDefault ));
                break;
            case USUARIO_ACCIONES:
                paramsDefault = " AND (id_tipo_bitacora_accion_tipo="+SGAccionBitacoraBO.ACCION_NAVEGACION+" OR id_tipo_bitacora_accion_tipo=" + SGAccionBitacoraBO.ACCION_DESCARGA + " )";
                //paramsDefault = " AND id_tipo_bitacora_accion_tipo="+SGAccionBitacoraBO.ACCION_NAVEGACION;
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new SGAccionBitacoraBO(this.conn).findAccionBitacora(-1, idEmpresa, 0, 0, params + paramsDefault));
                else
                    dataList = this.getDataList(new SGAccionBitacoraBO(this.conn).findAccionBitacora(-1, idEmpresa, 0, 0, paramsDefault));
                break;
            case PRODUCTO_MOVIMIENTOS:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new MovimientoBO(this.conn).findMovimientos(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new MovimientoBO(this.conn).findMovimientos(-1, idEmpresa, 0, 0, ""));
                break;
           case FACTURA_TOTAL_X_SUCURSAL_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new VistaResumenFacturasPorSucursalBO(this.conn).findVistaResumenFacturasPorSucursals(idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new VistaResumenFacturasPorSucursalBO(this.conn).findVistaResumenFacturasPorSucursals(idEmpresa, 0, 0, ""));
                break;
           case COBRANZA_ABONO_REPORT:
               if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new SGCobranzaAbonoBO(this.conn).findCobranzaAbono(-1, idEmpresa, 0, 0, params));
               else
                    dataList = this.getDataList(new SGCobranzaAbonoBO(this.conn).findCobranzaAbono(-1, idEmpresa, 0, 0, ""));
               break;
           case FACTURA_NOMINA_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataListNomina(new ComprobanteFiscalBO(this.conn).findComprobanteFiscal(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataListNomina(new ComprobanteFiscalBO(this.conn).findComprobanteFiscal(-1, idEmpresa, 0, 0, ""));
                break;
           case PRODUCTO_INVENTARIO_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataListInventario(new ConceptoBO(this.conn).findConceptos(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataListInventario(new ConceptoBO(this.conn).findConceptos(-1, idEmpresa, 0, 0, ""));
                break;
           case CLIENTE_SIN_COMPRA_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataListClientesSinCompra(new SGPedidoBO(this.conn).findPedido(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataListClientesSinCompra(new SGPedidoBO(this.conn).findPedido(-1, idEmpresa, 0, 0, ""));
                break;
           case EMPLEADO_CORTECAJA_REPORT:
               if(params!=null && !params.equals(""))
                    dataList = this.getDataListCorteCaja(new EmpleadoBO(this.conn).findEmpleadosActivos(-1, idEmpresa, 0, 0, params),params,paramsExtra);
               else
                    dataList = this.getDataListCorteCaja(new EmpleadoBO(this.conn).findEmpleadosActivos(-1, idEmpresa, 0, 0,""),params,paramsExtra);
               break;      
           case PRODUCTOS_VENTAS_REPORT:
               if(params!=null && !params.equals(""))
                    dataList = this.getDataListConceptosVendidos(new SGPedidoProductoBO(this.conn).findByIdPedido(-1, idEmpresa, 0, 0, params));
               else
                    dataList = this.getDataListConceptosVendidos(new SGPedidoProductoBO(this.conn).findByIdPedido(-1, idEmpresa, 0, 0,""));
               break;
           case PRODUCTOS_MAS_VENTAS_REPORT:
               if(params!=null && !params.equals(""))
                    dataList = this.getDataListConceptosVendidos(new SGPedidoProductoBO(this.conn).findByIdPedido(-1, idEmpresa, 0, 0, params, 1, "DESC"));
               else
                    dataList = this.getDataListConceptosVendidos(new SGPedidoProductoBO(this.conn).findByIdPedido(-1, idEmpresa, 0, 0,"", 1, "DESC"));
               break;
           case PRODUCTOS_MENOS_VENTAS_REPORT:
               if(params!=null && !params.equals(""))
                    dataList = this.getDataListConceptosVendidos(new SGPedidoProductoBO(this.conn).findByIdPedido(-1, idEmpresa, 0, 0, params, 2, "ASC"));
               else
                    dataList = this.getDataListConceptosVendidos(new SGPedidoProductoBO(this.conn).findByIdPedido(-1, idEmpresa, 0, 0,"", 2, "ASC"));
               break;
           case PEDIDO_DEVOLUCION_CAMBIO_REPORT:
               if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new SgfensPedidoDevolucionCambioBO(this.conn).findSgfensPedidoDevolucionCambios(-1, idEmpresa, 0, 0, params));
               else
                    dataList = this.getDataList(new SgfensPedidoDevolucionCambioBO(this.conn).findSgfensPedidoDevolucionCambios(-1, idEmpresa, 0, 0,""));
               break;
               
           case ARQUEO_DE_CAJA_REPORT:
               if(params!=null && !params.equals(""))
                    dataList = this.getDataListArqueoCaja(new EmpleadoBO(this.conn).findEmpleadosActivos(-1, idEmpresa, 0, 0, params),params,paramsExtra);
               else
                    dataList = this.getDataListArqueoCaja(new EmpleadoBO(this.conn).findEmpleadosActivos(-1, idEmpresa, 0, 0,""),params,paramsExtra);
               break;
           case PAGOS_TARJETA_REPORT:
               if(params!=null && !params.equals(""))
                    dataList = this.getDataListPagosTarjeta(new BancoOperacionBO(this.conn).findBancoOperaciones(-1, idEmpresa, 0, 0, params));
               else
                    dataList = this.getDataListPagosTarjeta(new BancoOperacionBO(this.conn).findBancoOperaciones(-1, idEmpresa, 0, 0,""));
               break;
           case BITACORA_POSICION_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataListBitacoraPosicion(new EmpleadoBitacoraPosicionBO(this.conn).findEmpleadoBitacoraPosicions(-1, Integer.parseInt(paramsExtra), 0, 0, params));
                else
                    dataList = this.getDataListBitacoraPosicion(new EmpleadoBitacoraPosicionBO(this.conn).findEmpleadoBitacoraPosicions(-1, Integer.parseInt(paramsExtra), 0, 0, ""));
                break;
           case MENSAJES_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataListMovilMensaje(new MovilMensajeBO(this.conn).findMovilMensajes(-1,idEmpresa, -1,-1,params,Long.parseLong(paramsExtra), Long.parseLong(paramsExtra)));
                else
                    dataList = this.getDataListMovilMensaje(new MovilMensajeBO(this.conn).findMovilMensajes(-1,idEmpresa, -1,-1,params,Long.parseLong(paramsExtra), Long.parseLong(paramsExtra)));
                break;
           case EMPLEADO_INVENTARIO_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataListEmpleadoInventarioRepartidor(new EmpleadoInventarioRepartidorBO(this.conn).findEmpleadoInventarioRepartidorsOrderBy(-1,Integer.parseInt(paramsExtra),0,0,params, usuarioBO!=null?usuarioBO.getOrdenamientoConceptos().getSqlOrderByCortes():null));
                else
                    dataList = this.getDataListEmpleadoInventarioRepartidor(new EmpleadoInventarioRepartidorBO(this.conn).findEmpleadoInventarioRepartidorsOrderBy(-1, Integer.parseInt(paramsExtra), 0, 0, "", usuarioBO!=null?usuarioBO.getOrdenamientoConceptos().getSqlOrderByCortes():null));
                break;
           case EMPLEADO_CORTE_INVENTARIO_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataListInventarioInicialVendedor(new InventarioInicialVendedorBO(this.conn).findInventarioInicialByIdEmpleadoOrderBy(-1,0,0,params, usuarioBO!=null?usuarioBO.getOrdenamientoConceptos().getSqlOrderByCortes():null ),Integer.parseInt(paramsExtra));
                else
                    dataList = this.getDataListInventarioInicialVendedor(new InventarioInicialVendedorBO(this.conn).findInventarioInicialByIdEmpleadoOrderBy(-1,0, 0, "", usuarioBO!=null?usuarioBO.getOrdenamientoConceptos().getSqlOrderByCortes():null ),Integer.parseInt(paramsExtra));
                break;
           case FACTURA_SAR_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataListSar(new SarComprobanteBO(this.conn).findComprobanteFiscal(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataListSar(new SarComprobanteBO(this.conn).findComprobanteFiscal(-1, idEmpresa, 0, 0, ""));
                break;
           case CXC_LIST_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataListCxC(new VistaCxcBO(this.conn).findCxc(-1, -1, idEmpresa, 0, 0, false, params));
                else
                    dataList = this.getDataListCxC(new VistaCxcBO(this.conn).findCxc(-1, -1, idEmpresa, 0, 0, false, ""));
                break;
           case CXP_LIST_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataListCxP(new VistaCxpBO(this.conn).findCxp(-1, -1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataListCxP(new VistaCxpBO(this.conn).findCxp(-1, -1, idEmpresa, 0, 0, ""));
                break;
           case EMPLEADO_PRODUCTIVIDAD:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataListProductividad(new SGPedidoBO(this.conn).findByPedidosPorVendedor(-1, idEmpresa, 0, 0, params,""),paramsExtra);
                else
                    dataList = this.getDataListProductividad(new SGPedidoBO(this.conn).findByPedidosPorVendedor(-1, idEmpresa, 0, 0, "",""),paramsExtra);
                break;
            case BITACORA_CR_OPERACION_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new BitacoraCreditosOperacionBO(this.conn).findBitacoraCreditosOperacion(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new BitacoraCreditosOperacionBO(this.conn).findBitacoraCreditosOperacion(-1, idEmpresa, 0, 0, ""));
                break;
            case CXC_FACTURAS_LIST_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataListCxCFacturas(new ComprobanteFiscalBO(this.conn).findComprobanteFiscal(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataListCxCFacturas(new ComprobanteFiscalBO(this.conn).findComprobanteFiscal(-1, idEmpresa, 0, 0, ""));
                break;
            case CXC_PEDIDOS_LIST_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataListCxCPedidos(new SGPedidoBO(this.conn).findPedido(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataListCxCPedidos(new SGPedidoBO(this.conn).findPedido(-1, idEmpresa, 0, 0, ""));
                break;
            case POS_ESTACION_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new PosEstacionBO(this.conn).findPosEstacion(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new PosEstacionBO(this.conn).findPosEstacion(-1, idEmpresa, 0, 0, ""));
                break;
            case DICCIONARIO_I_E_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new DiccionarioIngresosEgresosBO(this.conn).findDiccionarioIngresosEgresos(-1, true, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new DiccionarioIngresosEgresosBO(this.conn).findDiccionarioIngresosEgresos(-1, true, idEmpresa, 0, 0, ""));
                break;
            case PRECIOS_ESPECIALES_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataListClienteConcepto(new ClientePrecioConceptoBO(this.conn).findClienteConceptos(-1, -1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataListClienteConcepto(new ClientePrecioConceptoBO(this.conn).findClienteConceptos(-1, -1, idEmpresa, 0, 0, ""));
                break;
            case EXISTENCIA_ALMACENES_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataListExistenciaAlmacen(new ExistenciaAlmacenBO(this.conn).findExistencias(-1, -1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataListExistenciaAlmacen(new ExistenciaAlmacenBO(this.conn).findExistencias(-1, -1, idEmpresa, 0, 0, ""));
                break;
            case GASTOS_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new GastosEvcBO(this.conn).findGastosEvc(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new GastosEvcBO(this.conn).findGastosEvc(-1, idEmpresa, 0, 0, ""));
                break;
            case REFERENCIA_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new VistaCobranzaReferenciaGruposBO(this.conn).findVistaCobranzaReferenciaGrupos(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new VistaCobranzaReferenciaGruposBO(this.conn).findVistaCobranzaReferenciaGrupos(-1, idEmpresa, 0, 0, ""));
                break;
            case PEDIDO_CONSIGNA_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataListConsigna(new SGPedidoBO(this.conn).findPedido(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataListConsigna(new SGPedidoBO(this.conn).findPedido(-1, idEmpresa, 0, 0, ""));
                break;
            case PEDIDO_CONSIGNA_INVENTARIO_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataListConsignaInventario(new SGPedidoBO(this.conn).findPedido(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataListConsignaInventario(new SGPedidoBO(this.conn).findPedido(-1, idEmpresa, 0, 0, ""));
                break;
            case VISITAS_A_CLIENTES_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataListVisitaClientes(new SGVisitaClienteBO(this.conn).findSgfensVisitaClientes(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataListVisitaClientes(new SGVisitaClienteBO(this.conn).findSgfensVisitaClientes(-1, idEmpresa, 0, 0, ""));
                break;
            case EMPLEADO_CORTE_INVENTARIO_HISTORICO_REPORT:                
                StringTokenizer tokens = new StringTokenizer(paramsExtra,"|");               
                String idEmpleadillo = tokens.nextToken().intern();
                String fechaFiltroInicial = null;
                try{
                    fechaFiltroInicial = tokens.nextToken().intern();
                    if(fechaFiltroInicial != null && fechaFiltroInicial.trim().equals("null")){
                        fechaFiltroInicial = null;
                    }
                }catch(Exception e){}
                String fechaFiltroFinal = null;
                try{
                    fechaFiltroFinal = tokens.nextToken().intern();
                    if(fechaFiltroFinal != null && fechaFiltroFinal.trim().equals("null")){
                        fechaFiltroFinal = null;
                    }
                }catch(Exception e){}
                
                String fechaFiltroInicial_B = null;
                try{
                    fechaFiltroInicial_B = tokens.nextToken().intern();
                    if(fechaFiltroInicial_B != null && fechaFiltroInicial_B.trim().equals("null")){
                        fechaFiltroInicial_B = null;
                    }
                }catch(Exception e){}
                String fechaFiltroFinal_B = null;
                try{
                    fechaFiltroFinal_B = tokens.nextToken().intern();
                    if(fechaFiltroFinal_B != null && fechaFiltroFinal_B.trim().equals("null")){
                        fechaFiltroFinal_B = null;
                    }
                }catch(Exception e){}
                
		//dataList = this.getDataListInventarioInicialVendedor(new InventarioInicialVendedorBO(this.conn).findInventarioInicialByIdEmpleadoOrderBy(-1,0, 0, "", usuarioBO!=null?usuarioBO.getOrdenamientoConceptos().getSqlOrderByCortes():null ),Integer.parseInt(paramsExtra));
                InventarioHistoricoVendedorBO inventarioHistoricoVendedorBO = new InventarioHistoricoVendedorBO(this.conn);
                dataList = this.getDataListInventarioHistoricoVendedor(
                            (new InventarioHistoricoVendedorDaoImpl(this.conn).findByDynamicSelect(
                                inventarioHistoricoVendedorBO.querySumaCantidad 
                                + " WHERE inv.ID_CONCEPTO = concepto.ID_CONCEPTO AND ID_EMPLEADO > 0 " +  StringManage.getValidString(params)
                                + " GROUP BY ID_CONCEPTO, ID_EMPLEADO"
                                + " ORDER BY " + (usuarioBO!=null?usuarioBO.getOrdenamientoConceptos().getSqlOrderByCorteHistorico(): "FECHA_INICIAL_CORTE DESC")
                                , null)),
                            Integer.parseInt(idEmpleadillo), fechaFiltroInicial, fechaFiltroFinal, fechaFiltroInicial_B, fechaFiltroFinal_B);
                //if(params!=null && !params.equals(""))
                //    dataList = this.getDataListInventarioHistoricoVendedor(new InventarioHistoricoVendedorBO(this.conn).findInventarioHistoricoByIdEmpleado(-1,0,0,params),Integer.parseInt(idEmpleadillo), fechaFiltroInicial, fechaFiltroFinal, fechaFiltroInicial_B, fechaFiltroFinal_B);
                //else
                //    dataList = this.getDataListInventarioHistoricoVendedor(new InventarioHistoricoVendedorBO(this.conn).findInventarioHistoricoByIdEmpleado(-1,0, 0, ""),Integer.parseInt(idEmpleadillo), fechaFiltroInicial, fechaFiltroFinal, fechaFiltroInicial_B, fechaFiltroFinal_B);
                
                break;
                
            case CRONOMETRO_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataListCronometro(new CronometroBO(this.conn).findCronometros(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataListCronometro(new CronometroBO(this.conn).findCronometros(-1, idEmpresa, 0, 0, ""));
                break;
            case RUTAS_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new RutaBO(this.conn).findMarcas(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new RutaBO(this.conn).findMarcas(-1, idEmpresa, 0, 0, ""));
                break;
            case SMS_PLANTILLAS_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new SmsPlantillaBO(this.conn).findSmsPlantillas(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new SmsPlantillaBO(this.conn).findSmsPlantillas(-1, idEmpresa, 0, 0, ""));
                break;
            case SMS_AGENDA_GRUPOS_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new SmsAgendaGrupoBO(this.conn).findSmsAgendaGrupos(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new SmsAgendaGrupoBO(this.conn).findSmsAgendaGrupos(-1, idEmpresa, 0, 0, ""));
                break;
            case SMS_AGENDA_DESTINATARIOS_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new SmsAgendaDestinatarioBO(this.conn).findSmsAgendaDestinatarios(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new SmsAgendaDestinatarioBO(this.conn).findSmsAgendaDestinatarios(-1, idEmpresa, 0, 0, ""));
                break; 
            case COMODATO_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new ComodatoBO(this.conn).findComodatos(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new ComodatoBO(this.conn).findComodatos(-1, idEmpresa, 0, 0, ""));
                break;
            case COMODATO_MANTENIMIENTO_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new ComodatoMantenimientoBO(this.conn).findComodatoMantenimientos(-1, -1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new ComodatoMantenimientoBO(this.conn).findComodatoMantenimientos(-1, -1, idEmpresa, 0, 0, ""));
                break;
            case COMODATO_CONTRATO_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new ComodatoBO(this.conn).findComodatos(-1, idEmpresa, 0, 0, params), "contratos");
                else
                    dataList = this.getDataList(new ComodatoBO(this.conn).findComodatos(-1, idEmpresa, 0, 0, ""), "contratos");
                break;
            case CR_FORMULARIOS_REPORT:
                CrFormularioBO crFormularioBO = new CrFormularioBO(this.conn);
                crFormularioBO.setOrderBy("ORDER BY id_grupo_formulario DESC, orden_grupo ASC");
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(crFormularioBO.findCrFormularios(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(crFormularioBO.findCrFormularios(-1, idEmpresa, 0, 0, ""));
                break; 
            case CR_FORMULARIO_VALIDACIONES_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new CrFormularioValidacionBO(this.conn).findCrFormularioValidacions(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new CrFormularioValidacionBO(this.conn).findCrFormularioValidacions(-1, idEmpresa, 0, 0, ""));
                break; 
            case CR_GRUPOS_FORMULARIO_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new CrGrupoFormularioBO(this.conn).findCrGrupoFormularios(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new CrGrupoFormularioBO(this.conn).findCrGrupoFormularios(-1, idEmpresa, 0, 0, ""));
                break; 
            case CR_SCORE_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new CrScoreBO(this.conn).findCrScores(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new CrScoreBO(this.conn).findCrScores(-1, idEmpresa, 0, 0, ""));
                break; 
            case CR_SCORE_DETALLE_REPORT:
                CrScoreDetalleBO crScoreDetalleBO = new CrScoreDetalleBO(this.conn);
                crScoreDetalleBO.setOrderBy(" ORDER BY id_score DESC, id_formulario_campo ASC ");
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(crScoreDetalleBO.findCrScoreDetalles(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(crScoreDetalleBO.findCrScoreDetalles(-1, idEmpresa, 0, 0, ""));
                break;
            case CR_PRODUCTO_CREDITO_REPORT:
                CrProductoCreditoBO crProductoCreditoBO = new CrProductoCreditoBO(this.conn);
                crProductoCreditoBO.setOrderBy(" ORDER BY id_producto_credito_padre ASC, nombre ASC ");
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(crProductoCreditoBO.findCrProductoCreditos(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(crProductoCreditoBO.findCrProductoCreditos(-1, idEmpresa, 0, 0, ""));
                break;
            case CR_DOC_IMPRIMIBLE_REPORT:
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(new CrDocImprimibleBO(this.conn).findCrDocImprimibles(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(new CrDocImprimibleBO(this.conn).findCrDocImprimibles(-1, idEmpresa, 0, 0, ""));
                break;
            case CR_DOC_IMP_PARAMETRO_REPORT:
                CrDocImpParametroBO crDocImpParametroBO = new CrDocImpParametroBO(this.conn);
                crDocImpParametroBO.setOrderBy(" ORDER BY id_doc_imprimible DESC, id_doc_imp_parametro ASC ");
                if(params!=null && !params.equals(""))
                    dataList = this.getDataList(crDocImpParametroBO.findCrDocImpParametros(-1, idEmpresa, 0, 0, params));
                else
                    dataList = this.getDataList(crDocImpParametroBO.findCrDocImpParametros(-1, idEmpresa, 0, 0, ""));
                break;
        }
        return dataList;
    }    

    /**
     *  FACTURA_TOTAL_X_SUCURSAL_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(VistaResumenFacturasPorSucursal[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(FACTURA_TOTAL_X_SUCURSAL_REPORT);

        for(VistaResumenFacturasPorSucursal dto:objectDto){

            String estatus="";
            String sucursal="";
            Empresa empresaDto = null;
            try {
                empresaDto = new EmpresaBO(dto.getIdEmpresa(), this.conn).getEmpresa();
                if (empresaDto!=null)
                    sucursal = empresaDto.getNombreComercial();
                
                estatus =  EstatusComprobanteBO.getEstatusName(dto.getIdEstatus());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + sucursal ));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + estatus ));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getNumeroComprobantes() ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getSumaSubtotales() ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getSumaTotales() ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  PRODUCTO_MOVIMIENTOS
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(Movimiento[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(PRODUCTO_MOVIMIENTOS);
        
        
        EmpleadoBO empleadoBO = new EmpleadoBO(this.conn);
        Empleado empleadoDto = null;
        String nombreEmpleado = "";

        for(Movimiento dto:objectDto){

            String almacen="";
            Almacen almacenDto = null;
            Almacen almacenDtoDestino = null;
            String almacenDestino="";
            try {
                almacenDto = new AlmacenDaoImpl(this.conn).findByPrimaryKey(dto.getIdAlmacen());
                almacenDtoDestino =  new AlmacenBO(dto.getIdAlmacenDestino(),this.conn).getAlmacen();
                if (almacenDto!=null)
                    almacen = almacenDto.getNombre();
                if (almacenDtoDestino!=null)
                    almacenDestino = almacenDtoDestino.getNombre();
            } catch (AlmacenDaoException ex) {
                ex.printStackTrace();
            }
            
            nombreEmpleado = "";
            if (dto.getIdEmpleado() > 0){
                empleadoBO = new EmpleadoBO(dto.getIdEmpleado(),this.conn);
                empleadoDto = empleadoBO.getEmpleado();         

                nombreEmpleado = empleadoDto!=null?((empleadoDto.getNombre()!=null?empleadoDto.getNombre():"") 
                + (empleadoDto.getApellidoPaterno()!=null?" " + empleadoDto.getApellidoPaterno():"") 
                + (empleadoDto.getApellidoMaterno()!=null?" " + empleadoDto.getApellidoMaterno():"")):"";

            }
            

            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdMovimiento()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getTipoMovimiento() ));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getFechaRegistro()));            
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getNombreProducto() ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getContabilidad() ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + almacen ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + almacenDestino ));
            hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + nombreEmpleado ));
            hashData.put((String)dataInfo.get(8).get("field"), getRealData(dataInfo.get(8), "" + dto.getConceptoMovimiento()));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  USER_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(Usuarios[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(USER_REPORT);

        for(Usuarios dto:objectDto){

            DatosUsuario datosUsuarioDto = new DatosUsuario();
            Empresa empresaDto = new Empresa();
            try {
                datosUsuarioDto = new DatosUsuarioDaoImpl(this.conn).findByPrimaryKey(dto.getIdDatosUsuario());
                empresaDto = new EmpresaDaoImpl(this.conn).findByPrimaryKey(dto.getIdEmpresa());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdUsuarios()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + empresaDto.getNombreComercial()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getUserName()));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + RolesBO.getRolName(dto.getIdRoles()) ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + datosUsuarioDto.getNombre()+" "+ datosUsuarioDto.getApellidoPat() + " " +datosUsuarioDto.getApellidoMat() ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getIdEstatus() ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  CERTIFICADO_CSD_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(CertificadoDigital[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(CERTIFICADO_CSD_REPORT);

        for(CertificadoDigital dto:objectDto){

            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdCertificadoDigital()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getNombreCer()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getNombreKey() ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getNoCertificado() ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  SUCURSAL_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(Empresa[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(SUCURSAL_REPORT);

        for(Empresa dto:objectDto){

            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdEmpresa()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getRfc()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getRazonSocial() ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getNombreComercial() ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getFoliosDisponibles() ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    
    /**
     *  CLIENTE_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(Cliente[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(CLIENTE_REPORT);
        int idEmpresa = usuarioBO!=null?usuarioBO.getUser().getIdEmpresa():-1;
        
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy" );
        
        String fechaReg ="";        
        
                ClienteCampoContenidoDaoImpl clienteCampoContenidoDaoImpl = new ClienteCampoContenidoDaoImpl(this.conn);
                ClienteCampoAdicional[] clienteCampoAdicionalsDto = new ClienteCampoAdicional[0];
                ClienteCampoAdicionalBO clienteCampoAdicionalBO = new ClienteCampoAdicionalBO(this.conn);
                try{
                    clienteCampoAdicionalsDto = clienteCampoAdicionalBO.findClienteCampoAdicionals(0, idEmpresa , 0, 0, " AND ID_ESTATUS = 1 ");            
                }catch(Exception e){}                
                      
        ClienteCampoContenidoDaoImpl campoContenidoDaoImpl = new ClienteCampoContenidoDaoImpl(this.conn);
                
        for(Cliente dto:objectDto){
            
            fechaReg ="";
            try{            
               fechaReg = format.format(dto.getFechaRegistro());
            }catch(Exception e){}

            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdCliente()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + (dto.getClave()!=null&&!dto.getClave().trim().equals("")?dto.getClave():"")));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getRfcCliente()));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getRazonSocial() ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getContacto()!=null?dto.getContacto():"" ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getCorreo() ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + dto.getIdEstatus() ));
            hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + (dto.getIdVendedorConsigna()>0?"Consigna":"Normal") ));
            hashData.put((String)dataInfo.get(8).get("field"), getRealData(dataInfo.get(8), "" + fechaReg));
            hashData.put((String)dataInfo.get(9).get("field"), getRealData(dataInfo.get(9), "" + ((dto.getTelefono()!=null&&!dto.getTelefono().trim().equals(""))?dto.getTelefono():"") ));
            int camposPersonalizados = 9;//variable para el control de la posicion del dato en la columna correspondiente  
            for(ClienteCampoAdicional cliCampo: clienteCampoAdicionalsDto){
                //recuperamos el contenido del campo personalizado:
                ClienteCampoContenido campoContenido = null;
                try{
                    campoContenido = campoContenidoDaoImpl.findByDynamicWhere(" ID_CLIENTE_CAMPO = " + cliCampo.getIdClienteCampo() + " AND ID_CLIENTE = " + dto.getIdCliente(), null)[0];}catch(Exception e){}
                if(campoContenido!=null){                
                    hashData.put((String)dataInfo.get(camposPersonalizados).get("field"), getRealData(dataInfo.get(camposPersonalizados), "" +campoContenido.getValorLabel()));
                }else{
                    hashData.put((String)dataInfo.get(camposPersonalizados).get("field"), getRealData(dataInfo.get(camposPersonalizados), ""));
                }
                camposPersonalizados++;
            }

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  IMPUESTO_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(Impuesto[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(IMPUESTO_REPORT);

        for(Impuesto dto:objectDto){

            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdImpuesto()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getNombre()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getDescripcion() ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getPorcentaje() ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getTrasladado() ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getImpuestoLocal() ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + dto.getIdEstatus() ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  PROVEEDOR_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(SgfensProveedor[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(PROVEEDOR_REPORT);

        for(SgfensProveedor dto:objectDto){

            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdProveedor()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getNumeroProveedor()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getRfc()));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getRazonSocial() ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getContacto()!=null?dto.getContacto():"" ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getCorreo() ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + dto.getIdEstatus() ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  ALMACEN_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(Almacen[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(ALMACEN_REPORT);

        for(Almacen dto:objectDto){

            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdAlmacen()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getNombre()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getAreaAlmacen() ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getResponsable() ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getTelefono() ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getIdEstatus() ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  CATEGORIA_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(Categoria[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(CATEGORIA_REPORT);

        for(Categoria dto:objectDto){
            Categoria categoriaPadre = null;
            try{ categoriaPadre = new CategoriaBO(dto.getIdCategoriaPadre(), this.conn).getCategoria(); }catch(Exception ex){}
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdCategoria()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getNombreCategoria()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getDescripcionCategoria() ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "[" + dto.getIdCategoriaPadre() + "] " + (categoriaPadre!=null?categoriaPadre.getNombreCategoria():"") ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getIdEstatus() ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  EMBALAJE_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(Embalaje[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(EMBALAJE_REPORT);

        for(Embalaje dto:objectDto){
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdEmbalaje()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getNombre()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getDescripcion() ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getIdEstatus() ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    
    /**
     *  MARCA_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(Marca[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(MARCA_REPORT);

        for(Marca dto:objectDto){
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdMarca()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getNombre()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getDescripcion() ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getIdEstatus() ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  PRODUCTO_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(Concepto[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(PRODUCTO_REPORT);

        ConceptoBO conceptoBO = new ConceptoBO(this.conn);
        ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(this.conn);
        for(Concepto dto:objectDto){
            
            Marca marcaDto =null;
            Categoria categoriaDto = null;
            Categoria subcategoriaDto = null;
            double stockGral = 0;
            try{
                marcaDto = new MarcaBO(dto.getIdMarca(),this.conn).getMarca();
                categoriaDto = new CategoriaBO(dto.getIdCategoria(),this.conn).getCategoria();
                subcategoriaDto = new CategoriaBO(dto.getIdSubcategoria(),this.conn).getCategoria();
                stockGral = exisAlmBO.getExistenciaGeneralByEmpresaProducto(dto.getIdEmpresa(), dto.getIdConcepto());
            }catch(Exception ex){
                ex.printStackTrace();
            }
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdConcepto()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getIdentificacion()));
            try {
                hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getNombreDesencriptado() ));//conceptoBO.desencripta(dto.getNombre()) ));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getDescripcion() ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + (dto.getIdMarca()>0?"[" + dto.getIdMarca() +"]":"") + (marcaDto!=null? marcaDto.getNombre():"") ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + (dto.getIdCategoria()>0?"[" + dto.getIdCategoria() +"]":"") + (categoriaDto!=null? categoriaDto.getNombreCategoria():"") ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + (dto.getIdSubcategoria()>0?"[" + dto.getIdSubcategoria() +"]":"") + (subcategoriaDto!=null? subcategoriaDto.getNombreCategoria():"") ));
            hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + dto.getPrecio() ));
            hashData.put((String)dataInfo.get(8).get("field"), getRealData(dataInfo.get(8), "" + stockGral));
            hashData.put((String)dataInfo.get(9).get("field"), getRealData(dataInfo.get(9), "" + dto.getIdEstatus() ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    
    /**
     *  SERVICIO_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(Servicio[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(SERVICIO_REPORT);

        for(Servicio dto:objectDto){
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdServicio()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getSku()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getNombre()));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getDescripcion() ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getPrecio() ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getIdEstatus() ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    
    /**
     *  COTIZACION_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(SgfensCotizacion[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(COTIZACION_REPORT);

        for(SgfensCotizacion dto:objectDto){
            
            DatosUsuario datosUsuarioVendedor = new UsuarioBO(this.conn,dto.getIdUsuarioVendedor()).getDatosUsuario();
            Cliente cliente = null;
            SgfensProspecto prospecto = null;
            try{ cliente = new ClienteBO(dto.getIdCliente(),this.conn).getCliente(); }catch(Exception ex){}
            try{ prospecto = new SGProspectoBO(dto.getIdProspecto(),this.conn).getSgfensProspecto(); }catch(Exception ex){}
            String cliente_prospectoStr = "Sin cliente o prospecto asignado.";
            
            if (dto.getIdCliente()>0 && cliente!=null){
                //Cliente
                cliente_prospectoStr = cliente.getRazonSocial() + " [Cliente]";
            }else if (dto.getIdProspecto()>0 && prospecto!=null){
                cliente_prospectoStr = prospecto.getRazonSocial() + " [Prospecto]";
            }
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdCotizacion()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getFolioCotizacion()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + (datosUsuarioVendedor!=null? (datosUsuarioVendedor.getNombre() +" " + datosUsuarioVendedor.getApellidoPat()) :"Sin vendedor asignado") ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + cliente_prospectoStr ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getFechaCotizacion() ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getTotal() ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  PEDIDO_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(SgfensPedido[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(PEDIDO_REPORT);
        
        NumberFormat formatMoneda = new DecimalFormat("###,###,###,##0.00");
        
        double ventasTotales = 0;
        double pagosTotales = 0;
        double adeudosTotales  = 0;
        double comisionTotalVendedor = 0;
        double utilidadTotal = 0;
        
        Date hoy = new Date();//fecha de hoy
        final long MILLSECS_POR_DAY = 24 * 60 * 60 * 1000; //Milisegundos al día

        String vendedorCreadorAsignado = "";
        for(SgfensPedido dto:objectDto){
            if(dto.getIdEstatusPedido()!=3){            
                vendedorCreadorAsignado = "Sin vendedor asignado";
                ventasTotales += dto.getTotal();
                pagosTotales += dto.getSaldoPagado();
                adeudosTotales  += (dto.getTotal() - dto.getSaldoPagado());

                if(dto.getBonificacionDevolucion()<0){
                    ventasTotales += Math.abs(dto.getBonificacionDevolucion());
                    adeudosTotales  += Math.abs(dto.getBonificacionDevolucion());
                }            
            }
            
            //DatosUsuario datosUsuarioVendedor = new UsuarioBO(this.conn,dto.getIdUsuarioVendedor()).getDatosUsuario();
            try{
                DatosUsuario datosUsuarioVendedor = new UsuarioBO(dto.getIdUsuarioVendedor()).getDatosUsuario();
                if(datosUsuarioVendedor != null){
                    vendedorCreadorAsignado = datosUsuarioVendedor.getNombre() +" " + datosUsuarioVendedor.getApellidoPat();
                }}catch(Exception e){}
            try{
                if(dto.getIdUsuarioVendedor() != dto.getIdUsuarioVendedorAsignado()){
                    DatosUsuario datosUsuarioVendedorAsignado = new UsuarioBO(dto.getIdUsuarioVendedorAsignado()).getDatosUsuario();
                    vendedorCreadorAsignado += " / Vendedor Asignado: " + datosUsuarioVendedorAsignado.getNombre() +" " + datosUsuarioVendedorAsignado.getApellidoPat();
                }}catch(Exception e){}
            try{
                if(dto.getIdUsuarioVendedor() != dto.getIdUsuarioVendedorAsignado() && dto.getIdUsuarioVendedor() != dto.getIdUsuarioConductorAsignado() && dto.getIdUsuarioVendedorAsignado() != dto.getIdUsuarioConductorAsignado()){
                    DatosUsuario datosUsuarioConductorAsignado = new UsuarioBO(dto.getIdUsuarioConductorAsignado()).getDatosUsuario();
                    vendedorCreadorAsignado += " / Conductor Asignado: " + datosUsuarioConductorAsignado.getNombre() +" " + datosUsuarioConductorAsignado.getApellidoPat();
                }}catch(Exception e){}
            
            
            Cliente cliente = null;
            SgfensProspecto prospecto = null;
            try{ cliente = new ClienteBO(dto.getIdCliente(),this.conn).getCliente(); }catch(Exception ex){}
            String clienteStr = "Sin cliente asignado.";
            
            if (dto.getIdCliente()>0 && cliente!=null){
                //Cliente
                //clienteStr = cliente.getRazonSocial() + " [Cliente]";
                
                if(dto.getConsigna()==1){
                    //Cliente consigna
                    clienteStr = cliente.getRazonSocial() + (cliente.getClave()!=null&&!cliente.getClave().trim().equals("")?(", Clave: "+cliente.getClave()):"") + " [Cliente Consigna]";                 
                }else{
                    //Cliente
                    clienteStr = cliente.getRazonSocial() + (cliente.getClave()!=null&&!cliente.getClave().trim().equals("")?(", Clave: "+cliente.getClave()):"") + " [Cliente]";
                }
                
            }
            
            //recuperamos el ultimo abono del pedido para ver cuantos dias han transcurrido del ultimo pago que se considera como días de crédito.
            long diasDeCredito = 0;
            if(dto.getTotal() > (dto.getAdelanto() + dto.getSaldoPagado()) ){//validamos si aun existe monto por pagar, para calcular los dias de crédito
                //obtenemos el ultimo abono del pedido:
                try{
                    SgfensCobranzaAbono abono = new SGCobranzaAbonoBO(this.conn).findCobranzaAbono(0, 0, 0, 1, " AND ID_PEDIDO = " + dto.getIdPedido())[0];
                    diasDeCredito = (hoy.getTime() - abono.getFechaAbono().getTime())/MILLSECS_POR_DAY;
                }catch(Exception e){//si no hay un abono aun, calculamos los dias transcurridos de la fecha realizada del pedido al día de hoy
                    diasDeCredito = (hoy.getTime() - dto.getFechaPedido().getTime())/MILLSECS_POR_DAY;
                };
            }
            
            //Verificamos la cantidad de productos que tiene el pedido
            int cantidadProductos = 0;
            double comisionVendedor = 0;
            double utilidadNeta = 0;
            double costoVentaProducto = 0;
            double ventaTotal = 0;
            double adeudo = 0;
            try{
                SgfensPedidoProducto[] spp = new SgfensPedidoProductoDaoImpl(this.conn).findWhereIdPedidoEquals(dto.getIdPedido());
                cantidadProductos = spp.length;
                for(SgfensPedidoProducto product : spp){
                    comisionVendedor += ( product.getPorcentajeComisionEmpleado() * 0.01 * product.getSubtotal() );
                    costoVentaProducto += (product.getCostoUnitario() * product.getCantidad() );// + comisionVendedor);
                }
                comisionTotalVendedor += comisionVendedor;
            }catch(Exception e){}

            if(dto.getBonificacionDevolucion()>0){
               utilidadNeta = (dto.getTotal() - costoVentaProducto - comisionVendedor);
               ventaTotal = dto.getTotal();
               adeudo = dto.getTotal() - dto.getSaldoPagado();
            }else{
               utilidadNeta = ((dto.getTotal() + Math.abs(dto.getBonificacionDevolucion())) - costoVentaProducto - comisionVendedor);
               ventaTotal = dto.getTotal() + Math.abs(dto.getBonificacionDevolucion());
               adeudo = (dto.getTotal() + Math.abs(dto.getBonificacionDevolucion())) - dto.getSaldoPagado();
            }
            
            if(dto.getIdEstatusPedido() == 3){//pedido cancelado, no sumamos la utilidad
                utilidadNeta = 0;
            }
            
            utilidadTotal += utilidadNeta;            
            
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdPedido()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getFolioPedido()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + (vendedorCreadorAsignado) ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + clienteStr ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + DateManage.formatDateTimeToNormalMinutes(dto.getFechaPedido() )));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getFechaEntrega() ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + SGEstatusPedidoBO.getEstatusName(dto.getIdEstatusPedido()) ));
            hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + cantidadProductos ));
            hashData.put((String)dataInfo.get(8).get("field"), getRealData(dataInfo.get(8), "" + diasDeCredito ));
            hashData.put((String)dataInfo.get(9).get("field"), getRealData(dataInfo.get(9), "" + ventaTotal ));
            hashData.put((String)dataInfo.get(10).get("field"), getRealData(dataInfo.get(10), "" + dto.getSaldoPagado() ));
            hashData.put((String)dataInfo.get(11).get("field"), getRealData(dataInfo.get(11), "" + formatMoneda.format(adeudo)));
            hashData.put((String)dataInfo.get(12).get("field"), getRealData(dataInfo.get(12), "" + formatMoneda.format(comisionVendedor) ));
            hashData.put((String)dataInfo.get(13).get("field"), getRealData(dataInfo.get(13), "" + formatMoneda.format(utilidadNeta) ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }
        
            
            hashData.put((String)dataInfo.get(8).get("field"), getRealData(dataInfo.get(7), "TOTALES" ));
            hashData.put((String)dataInfo.get(9).get("field"), getRealData(dataInfo.get(8), "" + formatMoneda.format(ventasTotales) ));
            hashData.put((String)dataInfo.get(10).get("field"), getRealData(dataInfo.get(9), "" + formatMoneda.format(pagosTotales) ));
            hashData.put((String)dataInfo.get(11).get("field"), getRealData(dataInfo.get(10), "" + formatMoneda.format(adeudosTotales) ));
            hashData.put((String)dataInfo.get(12).get("field"), getRealData(dataInfo.get(11), "" + formatMoneda.format(comisionTotalVendedor) ));
            hashData.put((String)dataInfo.get(13).get("field"), getRealData(dataInfo.get(12), "" + formatMoneda.format(utilidadTotal) ));
            
            
            dataList.add(hashData);
         

        return dataList;
    }
    
    /**
     *  PROSPECTO_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(SgfensProspecto[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(PROSPECTO_REPORT);
        
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy" );

        String fechaReg ="";
        
        for(SgfensProspecto dto:objectDto){

            fechaReg ="";
            try{            
               fechaReg = format.format(dto.getFechaRegistro());
            }catch(Exception e){}
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdProspecto()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getRazonSocial() ));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getContacto()!=null?dto.getContacto():"" ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getCorreo() ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + fechaReg));

            
            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  FACTURA_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(ComprobanteFiscal[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(FACTURA_REPORT);

        for(ComprobanteFiscal dto:objectDto){
            
            Cliente cliente = null;
            SgfensProspecto prospecto = null;
            try{ cliente = new ClienteBO(dto.getIdCliente(),this.conn).getCliente(); }catch(Exception ex){}
            String clienteStr = "Sin cliente asignado.";
            
            if (dto.getIdCliente()>0 && cliente!=null){
                //Cliente
                clienteStr = cliente.getRazonSocial() + "";
            }
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdComprobanteFiscal()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getUuid()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getFolioGenerado()));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + clienteStr ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getFechaCaptura() ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + EstatusComprobanteBO.getEstatusName(dto.getIdEstatus()) ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + dto.getImporteNeto() ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  FACTURA_SAR_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListSar(ComprobanteFiscal[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(FACTURA_SAR_REPORT);

        for(ComprobanteFiscal dto:objectDto){
            
            Cliente cliente = null;
            SgfensProspecto prospecto = null;
            try{ cliente = new ClienteBO(dto.getIdCliente(),this.conn).getCliente(); }catch(Exception ex){}
            String clienteStr = "Sin cliente asignado.";
            
            if (dto.getIdCliente()>0 && cliente!=null){
                //Cliente
                clienteStr = cliente.getRazonSocial() + "";
            }
            
            SarComprobante sarComprobanteDto = new SarComprobanteBO(dto.getIdComprobanteFiscal(), this.conn).getSarComprobante();
            String estatusSAR = "No enviada";
            if (sarComprobanteDto.getExtSarIdFactura()>0){
                estatusSAR = sarComprobanteDto.getExtSarDescEstatus();
            }
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdComprobanteFiscal()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getUuid()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + EstatusComprobanteBO.getEstatusName(dto.getIdEstatus())  ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + clienteStr ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getFechaCaptura() ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + estatusSAR ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + dto.getImporteNeto() ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    private ArrayList<HashMap> getDataListNomina(ComprobanteFiscal[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(FACTURA_NOMINA_REPORT);

        double totalPercepciones = 0;
        double totalDeducciones = 0;
        for(ComprobanteFiscal dto:objectDto){
            
            NominaEmpleado cliente = null;
            SgfensProspecto prospecto = null;
            try{ cliente = new NominaEmpleadoBO(this.conn,dto.getIdCliente()).getNominaEmpleado(); }catch(Exception ex){}
            String clienteStr = "Sin empleado asignado.";
            
            if (dto.getIdCliente()>0 && cliente!=null){
                //Cliente
                clienteStr = cliente.getNombre() + " " + cliente.getApellidoPaterno() + " " + cliente.getApellidoMaterno();
            }
            
            //recuperamomos todas las percepciones y deducciones:
            NominaComprobanteDescripcionPercepcionDeduccion[] ncdpdList = new NominaComprobanteDescripcionPercepcionDeduccion[0];
            try {
                ncdpdList = new NominaComprobanteDescripcionPercepcionDeduccionDaoImpl(this.conn).findWhereIdCromprobanteFiscalEquals(dto.getIdComprobanteFiscal());
            } catch (NominaComprobanteDescripcionPercepcionDeduccionDaoException ex) {
                ex.printStackTrace();
            }
            //realizamos las sumas de percepciones y deducciones
            totalPercepciones = 0;
            totalDeducciones = 0;
            if(ncdpdList != null && ncdpdList.length>0){
                for(NominaComprobanteDescripcionPercepcionDeduccion ncd : ncdpdList){
                    if(ncd.getIdPercepcionDeduccion() == 1){//si es percepcion
                        //totalPercepciones += ncd.getImporteExcepto();
                        totalPercepciones += ncd.getImporteGravado();
                    }else if(ncd.getIdPercepcionDeduccion() == 2){//si es deduccion
                        //totalDeducciones += ncd.getImporteExcepto();
                        totalDeducciones += ncd.getImporteGravado();
                    }
                }
            }
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdComprobanteFiscal()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getFechaPago() ));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + cliente.getNumEmpleado() ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + clienteStr ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + totalPercepciones ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + totalDeducciones ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + EstatusComprobanteBO.getEstatusName(dto.getIdEstatus()) ));
            hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + dto.getImporteNeto() ));
            
            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  USUARIO_ACCESOS
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(SgfensAccionBitacora[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(USUARIO_ACCESOS);

        for(SgfensAccionBitacora dto:objectDto){

            String userData = "";
            String sucursal ="";
            String tipoAccion;
            
            DatosUsuario datosUsuarioDto;
            Usuarios usuarioDto;
            Empresa empresaDto;
            try {
                UsuarioBO usuarioBO = new UsuarioBO(this.conn,(int)dto.getIdUser());
                datosUsuarioDto = usuarioBO.getDatosUsuario();
                usuarioDto = usuarioBO.getUser();
                empresaDto = new EmpresaBO(usuarioDto.getIdEmpresa(),this.conn).getEmpresa();
                
                userData = usuarioDto.getUserName() + " - "+datosUsuarioDto.getNombre() + " " +datosUsuarioDto.getApellidoPat();
                sucursal = empresaDto.getNombreComercial();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            tipoAccion = SGAccionBitacoraBO.getAccionBitacoraName(dto.getIdTipoBitacoraAccionTipo());

            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getFechaHoraBitacoraAccion() ));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + userData));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + sucursal ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + tipoAccion));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getComentariosBitacoraAccion() ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  COBRANZA_ABONO_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(SgfensCobranzaAbono[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(COBRANZA_ABONO_REPORT);
        
        NumberFormat formatMoneda = new DecimalFormat("###,###,###,##0.00");
        
        String vendedorStr ="";
        String clienteStr ="";
        String metodoPagoStr="No identificado";
        double sumaAbonos = 0;
        double sumaAbonosCancelaciones = 0;
        double sumaAbonosActivos = 0;
        double sumaEfeDevuelto = 0;
        double sumaEfeDevueltoCancelado = 0;//Solo para futuro uso
        
        for(SgfensCobranzaAbono dto:objectDto){
            
            
            
                                                    
            if (dto.getIdEstatus()==1){
                if(dto.getIdCobranzaMetodoPago()!=VentaMetodoPagoBO.METODO_PAGO_DEVOLUCION_EFE){//Diferente de Devolucion efectivo
                    sumaAbonos+=dto.getMontoAbono();
                    sumaAbonosActivos += dto.getMontoAbono();
                }            
                if(dto.getIdCobranzaMetodoPago() == VentaMetodoPagoBO.METODO_PAGO_DEVOLUCION_EFE){
                    sumaEfeDevuelto += dto.getMontoAbono();
                }             
            }else{
               if(dto.getIdCobranzaMetodoPago()!=VentaMetodoPagoBO.METODO_PAGO_DEVOLUCION_EFE){//Diferente de Devolucion efectivo
                   sumaAbonosCancelaciones += dto.getMontoAbono();
                }
                if(dto.getIdCobranzaMetodoPago() == VentaMetodoPagoBO.METODO_PAGO_DEVOLUCION_EFE){
                    sumaEfeDevueltoCancelado += dto.getMontoAbono();
                }
            }
        
        
            vendedorStr ="";
            clienteStr ="";
            metodoPagoStr="No identificado";
            DatosUsuario datosUsuarioVendedorDto = new UsuarioBO(this.conn, dto.getIdUsuarioVendedor()).getDatosUsuario();
            Cliente clienteDto = new ClienteBO(dto.getIdCliente(),this.conn).getCliente();
            SgfensCobranzaMetodoPago metodoPagoDto = new SGCobranzaMetodoPagoBO(dto.getIdCobranzaMetodoPago(),this.conn).getCobranzaMetodoPago();
            if (datosUsuarioVendedorDto!=null)
                vendedorStr = datosUsuarioVendedorDto.getNombre() + " " + datosUsuarioVendedorDto.getApellidoPat();
            if (clienteDto!=null)
                clienteStr = clienteDto.getRazonSocial() + (clienteDto.getClave()!=null&&!clienteDto.getClave().trim().equals("")?(", Clave: "+clienteDto.getClave()):"");
            if (metodoPagoDto!=null)
                metodoPagoStr = metodoPagoDto.getNombreMetodoPago();
            
            ///*obtenemos el pedido al que se relaciona:
            SgfensPedido pedido = null;
            double montoCubierto = 0;
            double montoPagarTotal = 0;
            try{
                pedido = new SGPedidoBO(dto.getIdPedido(), this.conn).getPedido();
                montoCubierto = (pedido.getSaldoPagado() + pedido.getAdelanto());
                montoPagarTotal = pedido.getTotal();
            }catch(Exception e){}
            ///*
            
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + vendedorStr));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + clienteStr));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + DateManage.formatDateTimeToNormalMinutes(dto.getFechaAbono() )));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + metodoPagoStr ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getReferencia()));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + (dto.getIdEstatus()==1?"Activo":"Cancelado") ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + dto.getMontoAbono() ));
            hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + montoCubierto ));
            hashData.put((String)dataInfo.get(8).get("field"), getRealData(dataInfo.get(8), "" + montoPagarTotal ));
            try{
                hashData.put((String)dataInfo.get(9).get("field"), getRealData(dataInfo.get(9), "" + pedido!= null?(pedido.getConsigna()==0?"Normal":"Consigna"):"" ));
            }catch(Exception e){
                hashData.put((String)dataInfo.get(9).get("field"), getRealData(dataInfo.get(9), "" + "" ));
            }
            dataList.add(hashData);
            hashData = new HashMap<String, String>();
        }
        
                         
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "(+) Subtotal Cobros" ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + formatMoneda.format(sumaAbonos)));           
            dataList.add(hashData);
            
            hashData = new HashMap<String, String>();
            
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "(-) Subtotal Cancelados" ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + formatMoneda.format(sumaAbonosCancelaciones)));           
            dataList.add(hashData);
            
             hashData = new HashMap<String, String>();
             
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "(-) Subtotal Devolución EFE" ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + formatMoneda.format(sumaEfeDevuelto)));           
            dataList.add(hashData);
            
            hashData = new HashMap<String, String>();
            
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "TOTAL:" ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + formatMoneda.format(sumaAbonosActivos-sumaEfeDevuelto)));           
            dataList.add(hashData);

        return dataList;
    }
    
    /**
     * PRODUCTO_INVENTARIO_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListInventario(Concepto[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(PRODUCTO_INVENTARIO_REPORT);

        ConceptoBO conceptoBO = new ConceptoBO(this.conn);
        ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(this.conn);
        double cantidadTotalArticulos = 0;
        double stockGral = 0;
        for(Concepto dto:objectDto){
             
             
            //OBTENERMOS EL NUMERO DE ARTICULOS QUE TIENEN LOS REPARTIDORES
            stockGral = 0;
            cantidadTotalArticulos = 0;
            EmpleadoInventarioRepartidor[] eir;
            try {
                
                stockGral = exisAlmBO.getExistenciaGeneralByEmpresaProducto(dto.getIdEmpresa(), dto.getIdConcepto());
                
                eir = new EmpleadoInventarioRepartidorDaoImpl(this.conn).findByDynamicWhere(" ID_CONCEPTO = "+dto.getIdConcepto()+" AND ID_ESTATUS = 1 ", null);
                for(EmpleadoInventarioRepartidor em: eir){
                    cantidadTotalArticulos += em.getCantidad();
                }
            } catch (EmpleadoInventarioRepartidorDaoException ex) {
                ex.printStackTrace();
            }
           
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdConcepto()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getIdentificacion()!=null?dto.getIdentificacion():""));
            try {
                hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + conceptoBO.desencripta(dto.getNombre()) ));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getPrecio() ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + stockGral ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + cantidadTotalArticulos ));           
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + (cantidadTotalArticulos + stockGral) ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  CLIENTE_SIN_COMPRA_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListClientesSinCompra(SgfensPedido[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(CLIENTE_SIN_COMPRA_REPORT);
        //EN EL OBJETO "objectDto" ESTAN LOS CLIENTES QUE TIENEN UN ULTIMO PEDIDO DENTRO DEL PERIODO DEL FILTRO
        
        List<SgfensPedido> sgfensPedidos = new ArrayList<SgfensPedido>(); //EN ESTE OBJETO VAMOS A GUARDAR LOS PEDIDOS 
        
        //TODOS LOS PEDIDOS DE LA EMPRESA EN BASE AL FILTRO
        System.out.println("////////////EMPRESA A BUSCAR: "+objectDto[0].getIdEmpresa());
        //TODOS LOS CLIENTES QUE NO HAN REALIZADO UN PEDIDO EN ESE RANGO DEL FILTRO DE FECHAS:
        String menosClientes = "";       
        List<Integer> idsClientesExcluir = new ArrayList<Integer>();
        for(SgfensPedido dto:objectDto ){ 
            //menosClientes += " AND ID_CLIENTE != "+ dto.getIdCliente();
            if (!idsClientesExcluir.contains(dto.getIdCliente())) //evitamos repetidos
                idsClientesExcluir.add(dto.getIdCliente());
            System.out.println("////////////BUSCAR CLIENTES MENOS ESTE: "+dto.getIdCliente());
        }       
        if (idsClientesExcluir.size()>0)
            menosClientes = " AND ID_CLIENTE NOT IN (" + GenericMethods.concatInts(idsClientesExcluir, ',') + ")";
        //OBTENGO LOS CLIENTES QUE NO HAN REALIZADO PEDIDO ENTRE ESE TIEMPO ESPECIFICO DEL FILTRO
        Cliente[] clientes = new ClienteBO(this.conn).findClientes(0, objectDto[0].getIdEmpresa(), 0, 0, menosClientes);
        
        //RECUPERAMOS EL ULTIMO REGISTRO QUE SE TIENE SE PEDIDO DEL TOTAL DE CLIENTES QUE NO HAN PEDIDO EN UN TIEMPO DETERMINADO        
        
        System.out.println("//////////// CLIENTES QUE SE VANA BUSCAR SU ULTIMO PEDIDO . . .");
        for(Cliente cli : clientes){
            System.out.println("//////////// CLIENTE CON ID: "+cli.getIdCliente());
            try{//SI SE ENCUENTRA EL PEDIDO, SE AGREGA AL LISTADO EL ULTIMO PEDIDO QUE REALIZO
                SgfensPedido pedidoUltimo = new SgfensPedidoDaoImpl(this.conn).findByDynamicWhere("ID_CLIENTE = "+cli.getIdCliente()+" order by ID_PEDIDO DESC limit 1", null)[0];
                sgfensPedidos.add(pedidoUltimo);//agregamos al listado el ultimo pedido que ha realizado.
                System.out.println("////////////.............................. SE AGREGO UN PEDIDO!!!!");
            }catch(Exception e){ //SI NO TIENES PEDIDOS MENCIONAMOS QUE NO HA REALIZADO NINGUN PEDIDO
                System.out.println("//////////// NO HAY PEDIDO DE CLIENTE!!!!");
                SgfensPedido noTieneUltimoPedido = new SgfensPedido();
                noTieneUltimoPedido.setIdCliente(cli.getIdCliente());
                noTieneUltimoPedido.setIdUsuarioVendedor(0);                
                noTieneUltimoPedido.setTotal(0);
                noTieneUltimoPedido.setSaldoPagado(0);
                sgfensPedidos.add(noTieneUltimoPedido);
            }
        }
        
        
        for(SgfensPedido dto:sgfensPedidos){
            
            DatosUsuario datosUsuarioVendedor = new UsuarioBO(this.conn,dto.getIdUsuarioVendedor()).getDatosUsuario();
            Cliente cliente = null;
            SgfensProspecto prospecto = null;
            try{ cliente = new ClienteBO(dto.getIdCliente(),this.conn).getCliente(); }catch(Exception ex){}
            String clienteStr = "Sin cliente asignado.";
            
            if (dto.getIdCliente()>0 && cliente!=null){
                //Cliente
                clienteStr = cliente.getRazonSocial() + " [Cliente]";
            }
            
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + clienteStr ));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + (datosUsuarioVendedor!=null? (datosUsuarioVendedor.getNombre() +" " + datosUsuarioVendedor.getApellidoPat()) :"Sin vendedor asignado") ));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + (dto.getFechaEntrega()!=null?dto.getFechaEntrega():"") ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + (dto.getFechaPedido()!=null?dto.getFechaPedido():"") ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getTotal() ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getSaldoPagado() ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + (dto.getTotal() - dto.getSaldoPagado()) ));
                       
            /*
            fieldList.add(getDataInfo("ID","ID","","",""+DATA_INT,""));
            fieldList.add(getDataInfo("FK_CLIENTE","Cliente","","",""+DATA_STRING,""));
            fieldList.add(getDataInfo("FK_VENDEDOR","Vendedor","","",""+DATA_STRING,""));
            fieldList.add(getDataInfo("FECHA_ENTREGA","Fecha Entrega","","",""+DATA_DATE,""));
            fieldList.add(getDataInfo("FECHA_PEDIDO","Fecha Ultimo Pedido","","",""+DATA_DATE,""));
            fieldList.add(getDataInfo("TOTAL","Total","","",""+DATA_DECIMAL,""));
            fieldList.add(getDataInfo("SALDO_PAGADO","Saldo Pagado Ultimo Pedido","","",""+DATA_STRING,""));
            fieldList.add(getDataInfo("SALDO_PENDIENTE","Saldo Pendiente Ultimo Pedido","","",""+DATA_STRING,""));*/            
            
            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    
    
    /**
     *  CORTECAJA_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListCorteCaja(Empleado[] objectDto , String params , String paramsExtra ) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(EMPLEADO_CORTECAJA_REPORT);
        int idEmpresa = usuarioBO!=null?usuarioBO.getUser().getIdEmpresa():-1;
        
        NumberFormat formatMoneda = new DecimalFormat("###,###,###,##0.00");
        
        
        String strWhereRangoFechasAbonos = paramsExtra.replace("FECHA_PEDIDO", "FECHA_ABONO");
        strWhereRangoFechasAbonos = strWhereRangoFechasAbonos.replace("ID_ESTATUS_PEDIDO", "ID_ESTATUS");
        
        /*
     *  Totales pie pag
     *  Obetenemos todos los registros
     */
     
      double totalVendido = 0;
      double totalCobrado = 0;
      double totalAdeudo = 0;
       try{             
          
           Empleado[] empleadosDto2 = objectDto;
            for (Empleado item:empleadosDto2){
                SGPedidoBO pedidoBO= new SGPedidoBO(this.conn);
                SgfensPedido[] pedidosDts = pedidoBO.findPedido(-1,(int)(long)idEmpresa , -1, -1 , " AND ID_USUARIO_VENDEDOR ="+item.getIdUsuarios()+ " AND " + paramsExtra );        

                if(pedidosDts!=null){

                    for(SgfensPedido pedido : pedidosDts ){
                        totalVendido += pedido.getTotal();
                        
                        if(pedido.getBonificacionDevolucion()<0){
                            totalVendido += Math.abs(pedido.getBonificacionDevolucion());
                        }

                    }
                }

                SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(this.conn);
                SgfensCobranzaAbono[] abonosDts = cobranzaAbonoBO.findCobranzaAbono(-1, idEmpresa, -1, -1, " AND ID_USUARIO_VENDEDOR ="+item.getIdUsuarios() + " AND " + strWhereRangoFechasAbonos);

                if(abonosDts!=null){
                    for(SgfensCobranzaAbono abono : abonosDts ){
                        totalCobrado += abono.getMontoAbono();
                    }                                                                
                }
            }   


            totalAdeudo += (totalVendido - totalCobrado);
         }catch(Exception ex){
             ex.printStackTrace();
         }
        
        
        
        for(Empleado dto:objectDto){
           
            SGPedidoBO pedidoBO= new SGPedidoBO(this.conn);
            SgfensPedido[] pedidosDts = pedidoBO.findPedido(-1, idEmpresa , -1, -1 , " AND ID_USUARIO_VENDEDOR ="+dto.getIdUsuarios() + " AND " + paramsExtra );        

            double totalVendidoProm = 0;
            double totalCobradoProm = 0;
            double totalAdeudoProm = 0;

            if(pedidosDts!=null){

                for(SgfensPedido pedido : pedidosDts ){
                    totalVendidoProm += pedido.getTotal();

                }
            }
            
            
            
            
            SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(this.conn);
            SgfensCobranzaAbono[] abonosDts = cobranzaAbonoBO.findCobranzaAbono(-1, idEmpresa, -1, -1, " AND ID_USUARIO_VENDEDOR ="+dto.getIdUsuarios() + " AND " + strWhereRangoFechasAbonos);

            if(abonosDts!=null){
                for(SgfensCobranzaAbono abono : abonosDts ){
                    totalCobradoProm += abono.getMontoAbono();
                }                                                                
            }


            totalAdeudoProm += (totalVendidoProm - totalCobradoProm);
            

            String nombreCompleto = dto.getNombre() + " " + dto.getApellidoPaterno() + " " + dto.getApellidoMaterno();
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdEmpleado()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getNumEmpleado()));         
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + nombreCompleto));  
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + totalVendidoProm ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + totalCobradoProm ));           
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + totalAdeudoProm ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "TOTALES" ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + formatMoneda.format(totalVendido)));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + formatMoneda.format(totalCobrado) ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + formatMoneda.format(totalAdeudo) ));
            dataList.add(hashData);

        return dataList;
    }
    
    
    
    
    /**
     *  PRODUCTOS_VENTAS_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListConceptosVendidos(SgfensPedidoProducto[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(PRODUCTOS_VENTAS_REPORT);
        int idEmpresa = usuarioBO!=null?usuarioBO.getUser().getIdEmpresa():-1;
        
        NumberFormat formatMoneda = new DecimalFormat("###,###,###,##0.00");        
        
        
          /*
         *  Totales pie pag
         *  Obetenemos todos los registros
         */

         double unidadesVendidasSuma = 0;
         double costoUnitarioSuma = 0;
         double costoTotalSuma = 0;
         double precioUnitarioSuma = 0;
         double precioTotalSuma = 0;
         double utilidadTotalSuma = 0;

         try{       
             SgfensPedidoProducto[] pedidoProductoDtoAcums = objectDto;
             for (SgfensPedidoProducto item: pedidoProductoDtoAcums){

                 Concepto conceptoDto = null;
                try{
                    ConceptoBO  conceptoBO = new ConceptoBO(this.conn);
                    conceptoDto =conceptoBO.findConceptobyId(item.getIdConcepto());
                }catch(Exception e){} 

                double costoTotal = 0;
                double precioTotal = 0;



                if(conceptoDto!=null){
                    //si es a granel tomamos el peso
                    if(conceptoDto.getPrecioUnitarioGranel()>0 || conceptoDto.getPrecioMedioGranel()>0
                            || conceptoDto.getPrecioMayoreoGranel()>0 || conceptoDto.getPrecioEspecialGranel()>0){

                        costoTotal = item.getCostoUnitario() * item.getCantidadPeso();
                        precioTotal = item.getPrecioUnitarioGranel() * item.getCantidadPeso();
                        costoTotalSuma += item.getCostoUnitario() * item.getCantidadPeso();
                        precioTotalSuma +=  item.getPrecioUnitarioGranel() * item.getCantidadPeso();
                        precioUnitarioSuma +=item.getPrecioUnitarioGranel();
                    }else{
                        costoTotal  = item.getCostoUnitario() * item.getCantidad();
                        precioTotal = item.getPrecioUnitario() * item.getCantidad();
                        costoTotalSuma += item.getCostoUnitario() * item.getCantidad();
                        precioTotalSuma += item.getPrecioUnitario() * item.getCantidad();
                        precioUnitarioSuma += item.getPrecioUnitario();
                    }

                }else{
                    costoTotal  = item.getCostoUnitario() * item.getCantidad();
                    precioTotal = item.getPrecioUnitario() * item.getCantidad();
                    costoTotalSuma += item.getCostoUnitario() * item.getCantidad();
                    precioTotalSuma +=  item.getPrecioUnitario() * item.getCantidad();
                    precioUnitarioSuma += item.getPrecioUnitario();
                }



                unidadesVendidasSuma +=  item.getCantidad();
                costoUnitarioSuma += item.getCostoUnitario();




                if(costoTotal>0){
                   utilidadTotalSuma += precioTotal - costoTotal;
                }
             }

         }catch(Exception ex){
             ex.printStackTrace();
         }
        
        try{
            
            for(SgfensPedidoProducto dto:objectDto){
                
                Concepto conceptoDto = null;
                try{
                    ConceptoBO  conceptoBO = new ConceptoBO(this.conn);
                    conceptoDto =conceptoBO.findConceptobyId(dto.getIdConcepto());
                }catch(Exception e){} 
                
                                
                
                double costoTotal = 0;
                double precioTotal = 0;
                double precioUnitario = 0;
                
                if(conceptoDto!=null){
                    //si es a granel tomamos el peso
                    if(conceptoDto.getPrecioUnitarioGranel()>0 || conceptoDto.getPrecioMedioGranel()>0
                            || conceptoDto.getPrecioMayoreoGranel()>0 || conceptoDto.getPrecioEspecialGranel()>0){

                        costoTotal = dto.getCostoUnitario() * dto.getCantidadPeso();
                        precioTotal = dto.getPrecioUnitarioGranel() * dto.getCantidadPeso();
                        precioUnitario =dto.getPrecioUnitarioGranel();
                    }else{
                        costoTotal = dto.getCostoUnitario() * dto.getCantidad();
                        precioTotal = dto.getPrecioUnitario() * dto.getCantidad();
                        precioUnitario = dto.getPrecioUnitario();
                    }

                }else{
                    costoTotal = dto.getCostoUnitario() * dto.getCantidad();
                    precioTotal = dto.getPrecioUnitario() * dto.getCantidad();
                    precioUnitario = dto.getPrecioUnitario();
                }
                
                
                double utilidad = 0;
                
                if(costoTotal==0){
                    utilidad = 0;
                }else{
                    utilidad = precioTotal - costoTotal;
                }
                
                ///*
                String fechaPedido = "";
                DatosUsuario datosUsuarioVendedor =  null;
                Cliente cliente = null;
                SgfensPedido pedido = null;
                String clienteStr = "Multiples Clientes";
                try{
                    if(tipoReporte == PRODUCTOS_VENTAS_REPORT){
                        ////**Recuperamos los datos del pedido:
                        pedido = new SGPedidoBO(dto.getIdPedido(), this.conn).getPedido();                                                    
                        //PARA RECUPERAR LOS DATOS DE CLIENTE Y VENDEDOR
                        datosUsuarioVendedor = new UsuarioBO(this.conn, pedido.getIdUsuarioVendedor()).getDatosUsuario();                                                    
                        try{ cliente = new ClienteBO(pedido.getIdCliente(),this.conn).getCliente(); }catch(Exception ex){}
                        try{fechaPedido = DateManage.formatDateToNormal(pedido.getFechaEntrega());}catch(Exception e){}

                        if (pedido!=null && cliente!=null){
                        //Cliente 
                           clienteStr = cliente.getRazonSocial() + (cliente.getClave()!=null&&!cliente.getClave().trim().equals("")?(", Clave: "+cliente.getClave()):"") + "";
                       }else{
                           clienteStr = "Multiples Clientes";
                       }
                    }
                }catch(Exception e){e.printStackTrace();}
                ///*

                if(tipoReporte == PRODUCTOS_VENTAS_REPORT){
                    hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdPedido()));
                }else{
                    hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdConcepto()));
                }
                
                String nombreVendedor = datosUsuarioVendedor!=null? (datosUsuarioVendedor.getNombre() +" " + datosUsuarioVendedor.getApellidoPat()) :"Multiples Vendedores";
                
                hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + nombreVendedor ));         
                hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + clienteStr ));         
                hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + fechaPedido ));         
                
                hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getIdentificacion()!=null?dto.getIdentificacion():"" ));         
                hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getDescripcion()));  
                hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + dto.getCantidad() ));
                hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + formatMoneda.format(dto.getCostoUnitario()) ));           
                hashData.put((String)dataInfo.get(8).get("field"), getRealData(dataInfo.get(8), "" + formatMoneda.format(costoTotal) ));
                hashData.put((String)dataInfo.get(9).get("field"), getRealData(dataInfo.get(9), "" + precioUnitario));           
                hashData.put((String)dataInfo.get(10).get("field"), getRealData(dataInfo.get(10), "" + precioTotal ));
                hashData.put((String)dataInfo.get(11).get("field"), getRealData(dataInfo.get(11), "" + formatMoneda.format(utilidad) ));
        
                dataList.add(hashData);

                hashData = new HashMap<String, String>();
            }
            
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "TOTALES" ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + unidadesVendidasSuma));
            hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + formatMoneda.format(costoUnitarioSuma) ));
            hashData.put((String)dataInfo.get(8).get("field"), getRealData(dataInfo.get(8), "" + formatMoneda.format(costoTotalSuma) ));
            hashData.put((String)dataInfo.get(9).get("field"), getRealData(dataInfo.get(9), "" + formatMoneda.format(precioUnitarioSuma)));
            hashData.put((String)dataInfo.get(10).get("field"), getRealData(dataInfo.get(10), "" + formatMoneda.format(precioTotalSuma) ));
            hashData.put((String)dataInfo.get(11).get("field"), getRealData(dataInfo.get(11), "" + formatMoneda.format(utilidadTotalSuma) ));
          
            dataList.add(hashData);
            
            
       }catch(Exception ex){
                    ex.printStackTrace();
                }

        return dataList;
    }
    
    /**
     *  PEDIDO_DEVOLUCION_CAMBIO_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(SgfensPedidoDevolucionCambio[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(PEDIDO_DEVOLUCION_CAMBIO_REPORT);

        SgfensPedido sgfensPedido = null;
        SgfensPedidoDaoImpl sgfensPedidoDaoImpl = new SgfensPedidoDaoImpl(this.conn);
        for(SgfensPedidoDevolucionCambio dto:objectDto){
            
            Empleado emp = null;
            String nombreEmpleado = "";
            try{
            emp = new EmpleadoBO(dto.getIdEmpleado(), this.conn).getEmpleado();
            nombreEmpleado = emp.getNombre() + " " + emp.getApellidoPaterno() + " " + emp.getApellidoMaterno();
            }catch(Exception e){}

            Concepto con = null;
            ConceptoBO conBO =  null;
            try{
                conBO = new ConceptoBO(dto.getIdConcepto(), this.conn);
                con = conBO.getConcepto();                                                        
            }catch(Exception e){}

            Concepto conEntregado = null;
            try{
                conBO = new ConceptoBO(dto.getIdConceptoEntregado(), this.conn);
                conEntregado = conBO.getConcepto();
            }catch(Exception e){}

            String strDiferencia ="";
            
            //Text diferencia
            if(dto.getDiferenciaFavor()==0){
                strDiferencia ="";
            }else if(dto.getDiferenciaFavor()==1){
                strDiferencia ="Bonificado";
            }else if(dto.getDiferenciaFavor()==2){
                strDiferencia ="En Contra de Cliente";
            }else if(dto.getDiferenciaFavor()==3){
                strDiferencia ="Liquidado";
            }else if(dto.getDiferenciaFavor()==4){
                strDiferencia ="Pago parcial";
            }
            
            try{
                sgfensPedido = sgfensPedidoDaoImpl.findByPrimaryKey(dto.getIdPedido());
            }catch(Exception e){}

            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdPedidoDevolCambio() ));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + nombreEmpleado ));
            try{
                hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + (con!=null?conBO.desencripta(con.getNombre()):"") ));
            }catch(Exception e){
                hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + "" ));
            }
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + (dto.getIdTipo()==1?"Devolución":dto.getIdTipo()==2?"Cambio":"") ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getAptoParaVenta() ));            
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getNoAptoParaVenta() ));            
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + (dto.getIdClasificacion()==1?"No Solicitado por Cliente":dto.getIdClasificacion()==2?"No Vendido":dto.getIdClasificacion()==3?(dto.getDescripcionClasificacion()!=null?dto.getDescripcionClasificacion():""):dto.getIdClasificacion()==4?"Producto Caduco":dto.getIdClasificacion()==5?"Producto Mal Estado":"") ));                        
            try{
                hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + (conEntregado!=null?conBO.desencripta(conEntregado.getNombre()):"") ));
            }catch(Exception e){
                hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + "" ));
            }
            hashData.put((String)dataInfo.get(8).get("field"), getRealData(dataInfo.get(8), "" + dto.getMontoResultante() ));
            hashData.put((String)dataInfo.get(9).get("field"), getRealData(dataInfo.get(9), "" + strDiferencia ));
            hashData.put((String)dataInfo.get(10).get("field"), getRealData(dataInfo.get(10), "" + dto.getIdPedido() + (sgfensPedido!=null?(" / " + (sgfensPedido.getFolioPedido()!=null?sgfensPedido.getFolioPedido():"")):"" ) ));
            hashData.put((String)dataInfo.get(11).get("field"), getRealData(dataInfo.get(11), "" + dto.getFecha() ));            

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        System.out.println(". . .  GENERANDO REPORTE DE DEVOLUCIONES Y CAMBIOS . . .");
        return dataList;
    }
    
    
    /**
     *  ARQUEO_DE_CAJA_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListArqueoCaja(Empleado[] objectDto , String params , String paramsExtra ) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(ARQUEO_DE_CAJA_REPORT);
        int idEmpresa = usuarioBO!=null?usuarioBO.getUser().getIdEmpresa():-1;
        
        NumberFormat formatMoneda = new DecimalFormat("###,###,###,##0.00");
           
        
        for(Empleado dto:objectDto){                      
            String regionProm = "";                                         
            //Obtenemos Region
            try{
                regionProm = new RegionBO(dto.getIdRegion(),this.conn).getRegion().getNombre();
            }catch(Exception e){}

            SGPedidoBO pedidoBO= new SGPedidoBO(this.conn);
            SgfensPedido[] pedidosDts = pedidoBO.findPedido(-1,(int)(long)idEmpresa , -1, -1 , " AND ID_USUARIO_VENDEDOR ="+dto.getIdUsuarios() + " AND ID_ESTATUS_PEDIDO != 3 ");

            EmpleadoArqueoBO empleadoArqueoBO= new EmpleadoArqueoBO(this.conn);
            EmpleadoArqueo[] arqueosDts = empleadoArqueoBO.findEmpleadoArqueo(-1, (int) (long)idEmpresa ,dto.getIdEmpleado() , -1, -1 , "");

            double totalVendido = 0;
            double totalCobrado = 0;
            double totalAdeudo = 0;
            double totalPagadoEmpresa = 0;
            double totalDevolucionEfectivo = 0;

            if(pedidosDts!=null){
                
                for(SgfensPedido pedido : pedidosDts ){
                    totalVendido += pedido.getTotal();

                    if(pedido.getBonificacionDevolucion()<0){
                        totalVendido += Math.abs(pedido.getBonificacionDevolucion());
                    }

                    totalCobrado += pedido.getSaldoPagado();

                    if(pedido.getBonificacionDevolucion()>0){
                        totalAdeudo += (pedido.getTotal() - pedido.getSaldoPagado());
                    }else{
                        totalAdeudo = (pedido.getTotal() + Math.abs(pedido.getBonificacionDevolucion())) - pedido.getSaldoPagado();
                    }
                    
                    
                    try{
                        SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(this.conn);
                        SgfensCobranzaAbono[] cobranzaAbonoDtos = new SgfensCobranzaAbono[0];

                        String filtroBusquedaCobros = " AND ID_PEDIDO = "  + pedido.getIdPedido() +  " ";                                                                
                        cobranzaAbonoDtos = cobranzaAbonoBO.findCobranzaAbono(-1, (int)idEmpresa , 0, 0, filtroBusquedaCobros);

                        for (SgfensCobranzaAbono itemCob:cobranzaAbonoDtos){       


                            if (itemCob.getIdEstatus()==1){                                                                                    
                                if(itemCob.getIdCobranzaMetodoPago() == VentaMetodoPagoBO.METODO_PAGO_DEVOLUCION_EFE){
                                    totalDevolucionEfectivo += itemCob.getMontoAbono();
                                }
                            }
                        }


                    }catch(Exception e){
                        System.out.println("Problema al recuperar Devoluciones de EFECTIVO");
                    }

                }
            }

            for(EmpleadoArqueo arqueo : arqueosDts ){
                totalPagadoEmpresa += arqueo.getMonto(); 
            }
                            
            //Obtenemos gastos
                                                    
            double totalGastos =0;
            try{

                GastosEvcBO gastosEvcBO = new GastosEvcBO(this.conn);
                GastosEvc[] gastosEvcDtos = new GastosEvc[0];
                String filtroGastos =  " AND ID_EMPLEADO=" + dto.getIdEmpleado() +" AND VALIDACION = 1 " ;
                gastosEvcDtos = gastosEvcBO.findGastosEvc(-1,dto.getIdEmpresa(),-1,-1 , filtroGastos);
                for(GastosEvc gastos:gastosEvcDtos){

                    totalGastos += gastos.getMonto() ;
                }

            }catch(Exception e){e.printStackTrace();}
            
            double totalGralEmp = totalCobrado-totalPagadoEmpresa-totalGastos-totalDevolucionEfectivo;
            String nombreCompleto = dto.getNombre() + " " + dto.getApellidoPaterno() + " " + dto.getApellidoMaterno();            
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getNumEmpleado()));         
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + nombreCompleto));  
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + regionProm ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + formatMoneda.format(totalVendido) ));           
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + formatMoneda.format(totalCobrado) ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + formatMoneda.format(totalAdeudo) ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + formatMoneda.format(totalGastos) ));
            hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + formatMoneda.format(totalDevolucionEfectivo) ));
            hashData.put((String)dataInfo.get(8).get("field"), getRealData(dataInfo.get(8), "" + formatMoneda.format(totalPagadoEmpresa) ));
            hashData.put((String)dataInfo.get(9).get("field"), getRealData(dataInfo.get(9), "" + formatMoneda.format(Converter.roundDouble(totalGralEmp)) ));
            

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }
           

        return dataList;
    }
    
    
    
    /**
     *  PAGOS_TARJETA_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListPagosTarjeta(BancoOperacion[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(PAGOS_TARJETA_REPORT);
        int idEmpresa = usuarioBO!=null?usuarioBO.getUser().getIdEmpresa():-1;
        
        NumberFormat formatMoneda = new DecimalFormat("###,###,###,##0.00");
           
        
        for(BancoOperacion dto:objectDto){                      
            String estatus ="";
            try{
               estatus = dto.getIdEstatus() == 1 ? "Pagado" : dto.getIdEstatus() == 2 ? "Cancelado" : "" ;
            }catch(Exception e){}
                
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdOperacionBancaria()));         
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getNoTarjeta()));  
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getNombreTitular()));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getMonto()));           
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getBancoOrderId() ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getBancoOperFecha()));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + estatus ));
            
            

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }
           

        return dataList;
    }
    
    
    /**
     *  BITACORA_POSICION_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListBitacoraPosicion(EmpleadoBitacoraPosicion[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(BITACORA_POSICION_REPORT);

        for(EmpleadoBitacoraPosicion dto:objectDto){
                        
            Empleado emp = null;
            String nombreEmpleado = "";
            
            try{
            emp = new EmpleadoBO(dto.getIdEmpleado(), this.conn).getEmpleado();
            nombreEmpleado = emp.getNombre() + " " + emp.getApellidoPaterno() + " " + emp.getApellidoMaterno();
            }catch(Exception e){}
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdBitacoraPosicion()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + nombreEmpleado));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getFecha()));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getLatitud()));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getLongitud()));
           

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    
    
    /**
     *  MENSAJES_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListMovilMensaje(MovilMensaje[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(MENSAJES_REPORT);

        
        
        EmpleadoBO ebo = null;
        String emisorEmpleado = "";
        String receptorEmpleado = "";
        Empleado emplea = null;
        
        for(MovilMensaje dto:objectDto){
                        
            try{
                if(dto.getEmisorTipo()==1){
                    ebo = new EmpleadoBO(dto.getIdEmpleadoEmisor(),this.conn);
                    emplea = ebo.getEmpleado();
                    emisorEmpleado = emplea.getNumEmpleado();                                                                                                               
                }else
                    emisorEmpleado = "";
                if(dto.getReceptorTipo()==1){
                    ebo = new EmpleadoBO(dto.getIdEmpleadoReceptor(),this.conn);
                    emplea = ebo.getEmpleado();
                    receptorEmpleado = emplea.getNumEmpleado();
                }else
                    receptorEmpleado = "";
                                
            }catch(Exception e){}
            
            String emisor = (dto.getEmisorTipo()==1)? "Empleado" : (dto.getEmisorTipo()==2)? "Consola" : "No Identificado";
            String receptor = (dto.getReceptorTipo()==1)? "Empleado" : (dto.getReceptorTipo()==2)? "Consola" : "No Identificado";  
            String fechaRecepcion = (dto.getFechaRecepcion()==null)? "" : dto.getFechaRecepcion().toString();
            String recibido  = (dto.getRecibido() == 0)?"No Entregado" : (dto.getRecibido() == 1)?"Recibido" : "No Identificado";
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdMovilMensaje()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + emisor));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + receptor));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + receptorEmpleado));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getFechaEmision()));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + fechaRecepcion));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + dto.getMensaje()));
            hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + recibido ));
           

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    
    
    /**
     *  EMPLEADO_INVENTARIO_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListEmpleadoInventarioRepartidor(EmpleadoInventarioRepartidor[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(EMPLEADO_INVENTARIO_REPORT);

        List<Concepto> conceptoAdd = new ArrayList<Concepto>();
        ConceptoBO conceptoBO = new ConceptoBO(this.conn);
        Concepto concepto = null;
        
        try{
            for(EmpleadoInventarioRepartidor inventario : objectDto){
                 //concepto = new Concepto();
                 concepto = conceptoBO.findConceptobyId(inventario.getIdConcepto());
                 concepto.setNumArticulosDisponibles(inventario.getCantidad());// se reusa objto
                 conceptoAdd.add(concepto);
             }
        }catch(Exception e){}
       
        Encrypter encripDesencri = new Encrypter();
        
        for(Concepto dto:conceptoAdd){    
            
           String nomConcepto =""; 
           String sku ="";
           try{               
               nomConcepto= encripDesencri.decodeString(dto.getNombre());  
               sku = dto.getIdentificacion()!=null?!dto.getIdentificacion().equals("null")?dto.getIdentificacion():"":"" ;
           }catch(Exception ex){
                ex.printStackTrace();
            }
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdConcepto()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + nomConcepto));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getDescripcion()));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + sku));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getNumArticulosDisponibles()));           
           

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    
    
    /**
     *  EMPLEADO_INVENTARIO_CORTE_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListInventarioInicialVendedor(InventarioInicialVendedor[] objectDto, int idEmpleado) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(EMPLEADO_CORTE_INVENTARIO_REPORT);       
        
        NumberFormat formatMoneda = new DecimalFormat("###,###,###,##0.00");
        int idEmpresa = usuarioBO!=null?usuarioBO.getUser().getIdEmpresa():-1;
        
            //Obtenemos empleado
         Empleado empleadoDto = new Empleado();
         try{
             EmpleadoBO empleadoBO = new EmpleadoBO(this.conn);     
             empleadoDto = empleadoBO.findEmpleadobyId(idEmpleado);
         }catch(Exception e){} 
        
        Encrypter encripDesencri = new Encrypter();
        SGPedidoBO pedidosBO = new SGPedidoBO(this.conn); 
        SGPedidoProductoBO partidasPedidoBO = new SGPedidoProductoBO(this.conn);
        SGPedidoDevolucionesCambioBO camDevBO = new SGPedidoDevolucionesCambioBO(this.conn);

        double cantidadVendida = 0;
        double cantidadDevoluciones = 0;
        double cantidadMerma = 0;
        double cantidadCambios = 0;
        double invFinal = 0;
        double totalProdsVendidos = 0;
        double totalProdsMerma = 0;
        double totalProdsVendidosDinero = 0;
        double totalProdsMermaDinero = 0;
        String fechaInicial = "";
        
        BigDecimal cantidadEntregadoPorPedidos = new BigDecimal("0.0");////
        SgfensPedidoProducto[] sgfensPedidoProducto = null;////
        SgfensPedidoProductoDaoImpl sgfensPedidoProductoDaoImpl = new SgfensPedidoProductoDaoImpl(this.conn);////                                        
        double cantidadMismoProducto = 0;////
        double cantidadDistitoProducto = 0;////
        
        
            for(InventarioInicialVendedor item : objectDto){  
                
                fechaInicial = item.getFechaRegistro().toString();
                
                cantidadEntregadoPorPedidos = new BigDecimal("0.0");////
                sgfensPedidoProducto = null;////
                cantidadMismoProducto = 0;////
                cantidadDistitoProducto = 0;////

                try{////
                    sgfensPedidoProducto = sgfensPedidoProductoDaoImpl.findWhereIdConceptoEquals(item.getIdConcepto());}catch(Exception e){}////
                if(sgfensPedidoProducto != null){////
                    for(SgfensPedidoProducto productos : sgfensPedidoProducto){////
                        cantidadEntregadoPorPedidos = cantidadEntregadoPorPedidos.add(new BigDecimal(productos.getCantidadEntregada()));////
                    }////
                }////
                
                Concepto concepto = null;
                
                //Limpiamos variables
                cantidadVendida = 0 ;
                cantidadDevoluciones = 0;
                cantidadMerma = 0;
                cantidadCambios = 0;
                invFinal = 0;
                
                try{  
                    
                       concepto = new ConceptoBO(item.getIdConcepto(),this.conn).getConcepto();
                                                    
                                             
                       
                        //Ventas
                        //Obtenemos Pedidos del vendedor
                        System.out.println("******PEDIDOS*******");
                        String filtroPedidosVendedor = " AND ID_USUARIO_VENDEDOR ='" + empleadoDto.getIdUsuarios() + "'  AND FECHA_PEDIDO >= '" +item.getFechaRegistro() + "' AND (ID_ESTATUS_PEDIDO <> 3 AND ID_ESTATUS_PEDIDO <> 1) ";                                                                                                                                                        
                        SgfensPedido[] pedidos = pedidosBO.findPedido(-1, idEmpresa, -1, -1, filtroPedidosVendedor);

                        if(pedidos.length>0){

                            for(SgfensPedido pedido : pedidos){
                                //Obtenemos partidas del pedido
                                System.out.println("******PRODUCTOS*******");
                                SgfensPedidoProducto[] partidasPedido = partidasPedidoBO.findByIdConcepto(item.getIdConcepto(), -1, -1," ID_PEDIDO="+pedido.getIdPedido() );
                                if(partidasPedido.length>0){
                                    for(SgfensPedidoProducto partida:partidasPedido ){
                                          
                                            //Sumamos cantidades
                                            cantidadVendida += partida.getCantidadEntregada();
                                            totalProdsVendidosDinero += (partida.getCantidad() * partida.getPrecioUnitario());
                                        
                                    }
                                }
                            }  
                        }   


                        //Obtenemos devoluciones y cambios del pedido
                        System.out.println("******DEVOLUCIONES Y CAMBIOS*******");
                        String filtroDevs  = " AND FECHA  >= '"+ item.getFechaRegistro()+"' ";
                        SgfensPedidoDevolucionCambio[] devoluciones = camDevBO.findCambioDevByEmpleado(this.conn, empleadoDto.getIdEmpleado(), filtroDevs);
                       
                        
                        
                        if(devoluciones.length>0){
                            for(SgfensPedidoDevolucionCambio dev:devoluciones){

                                 //Precio de venta del pedido 
                                SgfensPedidoProducto[] conceptoPedidoDto = partidasPedidoBO.findByIdPedido(dev.getIdPedido(), -1, -1, -1, " AND ID_CONCEPTO="+dev.getIdConcepto());

                                //if(conceptoPedidoDto.length>0){

                                    if(item.getIdConcepto()==dev.getIdConcepto()){

                                        //cantidadDevoluciones += dev.getAptoParaVenta();
                                        cantidadMerma += dev.getNoAptoParaVenta();
                                        try{
                                            totalProdsMermaDinero += ( conceptoPedidoDto[0].getPrecioUnitario());
                                        }catch(Exception e){
                                            totalProdsMermaDinero += 0;
                                        }
                                        if(dev.getIdTipo() == 2){////cambio
                                            if(item.getIdConcepto() == dev.getIdConceptoEntregado()){////
                                                cantidadMismoProducto += (dev.getAptoParaVenta() + dev.getNoAptoParaVenta());////
                                            }else{////
                                                cantidadDistitoProducto += (dev.getAptoParaVenta() + dev.getNoAptoParaVenta());////
                                            }///
                                        }else if(dev.getIdTipo() == 1){//devolucion                                                                        
                                               cantidadDevoluciones = cantidadDevoluciones + dev.getAptoParaVenta() + dev.getNoAptoParaVenta();                                                                       
                                        }

                                    }/*else{
                                        if(dev.getIdTipo() == 1){//devolucion                                                                        
                                               cantidadDevoluciones = cantidadDevoluciones + dev.getAptoParaVenta() + dev.getNoAptoParaVenta();                                                                       
                                        } 
                                    }*/     

                                    if(item.getIdConcepto()==dev.getIdConceptoEntregado()){
                                        if(dev.getCantidadDevuelta()>0){
                                            cantidadCambios += dev.getCantidadDevuelta();
                                        }
                                    }
                               // }                                                          

                            }
                        }
                                                
                        //invFinal = item.getCantidad() - cantidadVendida + cantidadDevoluciones - cantidadCambios + cantidadMerma;
                        //invFinal = item.getCantidad() - cantidadVendida + cantidadDevoluciones - cantidadCambios;
                        if(cantidadMerma != 0){
                            //invFinal = item.getCantidad() - cantidadVendida + (cantidadMerma) - cantidadCambios;
                            invFinal = item.getCantidad() - (cantidadVendida + cantidadCambios) + cantidadDevoluciones + cantidadMismoProducto + cantidadDistitoProducto - cantidadMerma;
                        }else{
                            //invFinal = item.getCantidad() - cantidadVendida - cantidadMismoProducto + cantidadDistitoProducto - cantidadCambios;
                            //invFinal = item.getCantidad() - cantidadVendida - cantidadCambios + cantidadMismoProducto;                                                        
                            invFinal = item.getCantidad() - (cantidadVendida + cantidadCambios) + cantidadDevoluciones + cantidadMismoProducto + cantidadDistitoProducto;
                        }
                                                    

                        //totalProdsVendidos += cantidadVendida;
                        totalProdsVendidos += cantidadVendida;
                        totalProdsMerma += cantidadMerma;
                    
                    
                    
                    
                }catch(Exception ex){
                    ex.printStackTrace();
                }    
                 
                String conceptoDescript = "";
                try{
                    conceptoDescript =  encripDesencri.decodeString(concepto.getNombre());   
                }catch(Exception e){}
                    
                hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + item.getIdConcepto()));
                hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + conceptoDescript ));
                hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + concepto.getDescripcion()));
                hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + item.getCantidad()));
                hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + (cantidadVendida + cantidadCambios)));           
                //hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + cantidadEntregadoPorPedidos.setScale(2, RoundingMode.HALF_UP)));////
                hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + (cantidadDevoluciones)));
                //hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + cantidadCambios));
                hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + cantidadMismoProducto));////
                hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + cantidadDistitoProducto));////
                hashData.put((String)dataInfo.get(8).get("field"), getRealData(dataInfo.get(8), "" + cantidadMerma));
                hashData.put((String)dataInfo.get(9).get("field"), getRealData(dataInfo.get(9), "" + (cantidadVendida))); 
                hashData.put((String)dataInfo.get(10).get("field"), getRealData(dataInfo.get(10), "" + invFinal));
                hashData.put((String)dataInfo.get(11).get("field"), getRealData(dataInfo.get(11), "" + (invFinal + cantidadMerma)));
           
                
                

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }
            
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "Articulos Vendidos:" ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + totalProdsVendidos));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + "Total:"));            
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "$" + formatMoneda.format(totalProdsVendidosDinero) ));            
          
            dataList.add(hashData);
            hashData = new HashMap<String, String>();
            
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "Articulos en Merma:" ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + totalProdsMerma));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + "Total:")); 
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "$" + formatMoneda.format(totalProdsMermaDinero)));            
          
            dataList.add(hashData);
            hashData = new HashMap<String, String>();
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "Inicio de Inventario:"));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), fechaInicial ));
            dataList.add(hashData);
            hashData = new HashMap<String, String>();

        return dataList;
    }
    
    /**
     *  CXC_LIST_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListCxC(VistaCxc[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(CXC_LIST_REPORT);

        SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(getConn());
        ComprobanteFiscalBO compBO;
        SGPedidoBO pedidoBO;
        
        for (VistaCxc dto : objectDto) {
            try {
                String tipo = "";
                int id = 0;
                String folio = "";
                String serie = " - ";
                Date fechaEmision = null;
                String clienteNombre = "Sin cliente asignado";
                long diasCredito = 0;
                long diasMora = 0;
                double porcentajeTiempoTranscurridoCredito = 0;

                int idFoliosSerie = 0;
                int idCliente = 0;
                double importe = 0;
                double saldo = 0;
                if (dto.getCIdComprobanteFiscal()>0){
                    //Comprobante Fiscal
                    compBO = new ComprobanteFiscalBO(dto.getCIdComprobanteFiscal(), getConn());
                    tipo = "CF";
                    id = dto.getCIdComprobanteFiscal();
                    folio = StringManage.getValidString(dto.getFolioGenerado());
                    idFoliosSerie = dto.getIdFolio();
                    fechaEmision =  dto.getFechaCaptura();
                    idCliente = dto.getCIdCliente();
                    importe = dto.getImporteNeto();
                    if (dto.getIdPedido()>0){
                        //Comprobante Fiscal dependiente de Pedido
                        tipo ="CF+P";
                        pedidoBO = new SGPedidoBO(dto.getIdPedido(), getConn());
                        saldo = dto.getBonificacionDevolucion()>0? (dto.getTotal() - dto.getSaldoPagado()):(dto.getTotal() + Math.abs(dto.getBonificacionDevolucion()) - dto.getSaldoPagado());
                        diasCredito = pedidoBO.calculaDiasMora();
                        diasMora = pedidoBO.calculaDiasMora();
                        porcentajeTiempoTranscurridoCredito = pedidoBO.calculaPorcentajeTranscurridoCredito();
                    }else{
                        //Comprobante Fiscal SIN pedido
                        saldo = dto.getImporteNeto() - cobranzaAbonoBO.getSaldoPagadoComprobanteFiscal(dto.getCIdComprobanteFiscal()).doubleValue();
                        diasCredito = compBO.calculaDiasMora();
                        diasMora = compBO.calculaDiasMora();
                        porcentajeTiempoTranscurridoCredito = compBO.calculaPorcentajeTranscurridoCredito();
                    }
                }else if (dto.getIdPedido()>0){
                    //Pedido sin CompFiscal
                    pedidoBO = new SGPedidoBO(dto.getIdPedido(), getConn());
                    tipo = "P";
                    id = dto.getIdPedido();
                    folio = StringManage.getValidString(dto.getFolioPedido());
                    idFoliosSerie = 0; //Los Pedidos no tienen Series
                    fechaEmision =  dto.getFechaPedido();
                    idCliente = dto.getIdCliente();
                    importe = dto.getTotal();
                    saldo = dto.getBonificacionDevolucion()>0? (dto.getTotal() - dto.getSaldoPagado()):(dto.getTotal() + Math.abs(dto.getBonificacionDevolucion()) - dto.getSaldoPagado());
                    diasCredito = pedidoBO.calculaDiasMora();
                    diasMora = pedidoBO.calculaDiasMora();
                    porcentajeTiempoTranscurridoCredito = pedidoBO.calculaPorcentajeTranscurridoCredito();
                }

                if (idFoliosSerie>0){
                    Folios foliosDto = null;
                    foliosDto = new FoliosBO(idFoliosSerie, getConn()).getFolios();
                    if (foliosDto!=null)
                        serie = foliosDto.getSerie();
                }

                if (idCliente>0){
                    Cliente cliente = null;
                    try{ cliente = new ClienteBO(idCliente,getConn()).getCliente(); }catch(Exception ex){}
                    if (cliente!=null)
                        clienteNombre = cliente.getRazonSocial();
                }

                String estatusFinanciero = "Activa";
                if (saldo<=0){
                    estatusFinanciero = "Pagada";
                }

                String colorSemaforo = "green";
                String nombreSemaforo =  !estatusFinanciero.equals("Pagada")?"En Tiempo":"Pagado";
                boolean cancelada = false;
                if (dto.getCIdEstatus()==EstatusComprobanteBO.ESTATUS_CANCELACION
                        || dto.getCIdEstatus()==EstatusComprobanteBO.ESTATUS_CANCELADA
                        || dto.getIdEstatusPedido()==SGEstatusPedidoBO.ESTATUS_CANCELADO){
                    //Si esta cancelada
                    colorSemaforo = "grey";
                    nombreSemaforo = "Cancelada";
                    estatusFinanciero = "Cancelada";
                    cancelada = true;
                }else{
                    if (diasMora>0){
                        //Si excede del plazo de crédito y tiene dias de mora
                        colorSemaforo = "red";
                        nombreSemaforo = "Plazo Vencido";
                    }else if (porcentajeTiempoTranscurridoCredito>=80){
                        //Si resta 20% para que se venza el plazo de crédito
                        colorSemaforo = "yellow";
                        nombreSemaforo = "Por Vencer";
                    }
                }

                hashData.put((String) dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + tipo ));
                hashData.put((String) dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + id ));
                hashData.put((String) dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + folio));
                hashData.put((String) dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + serie ));
                hashData.put((String) dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + fechaEmision ));
                hashData.put((String) dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + clienteNombre));
                hashData.put((String) dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + importe));
                hashData.put((String) dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + saldo));
                hashData.put((String) dataInfo.get(8).get("field"), getRealData(dataInfo.get(8), "" + diasCredito));
                hashData.put((String) dataInfo.get(9).get("field"), getRealData(dataInfo.get(9), "" + estatusFinanciero));
                hashData.put((String) dataInfo.get(10).get("field"), getRealData(dataInfo.get(10), "" + nombreSemaforo));

                dataList.add(hashData);

                hashData = new HashMap<String, String>();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return dataList;
    }
    
    /**
     *  CXP_LIST_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListCxP(VistaCxp[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(CXP_LIST_REPORT);

        CxpValeAzulBO cxpValeAzulBO = new CxpValeAzulBO(getConn());
        CxpComprobanteFiscalBO cxpComprobanteFiscalBO = new CxpComprobanteFiscalBO(getConn());
        
        for (VistaCxp dto : objectDto) {
            try {
                CxpValeAzul cxpValeAzulDto = null;
                CxpComprobanteFiscal cxpComprobanteFiscalDto = null;
                if (dto.getIdCxpComprobanteFiscal()>0){
                    cxpComprobanteFiscalBO = new CxpComprobanteFiscalBO(dto.getIdCxpComprobanteFiscal(), getConn());
                    cxpComprobanteFiscalDto = cxpComprobanteFiscalBO.getCxpComprobanteFiscal();
                }else{
                    cxpValeAzulBO = new CxpValeAzulBO(dto.getIdCxpValeAzul(), getConn());
                    cxpValeAzulDto = cxpValeAzulBO.getCxpValeAzul();
                }
                
                double saldo = 0;
                double importe = 0;
                boolean cancelado = false;
                String folio = "";
                String serie = "";
                int idRow = 0;
                Date fechaEmision = null;
                String nombreEmisor =  "-No identificado-";
                String tipo = "";
                if (cxpComprobanteFiscalDto!= null){
                    tipo = "CF";
                    idRow = cxpComprobanteFiscalDto.getIdCxpComprobanteFiscal();
                    importe = cxpComprobanteFiscalDto.getTotal();
                    saldo = cxpComprobanteFiscalDto.getTotal() - cxpComprobanteFiscalDto.getImportePagado();
                    cancelado = cxpComprobanteFiscalDto.getIdEstatus() == 2;
                    folio = StringManage.getValidString(cxpComprobanteFiscalDto.getFolio());
                    serie = StringManage.getValidString(cxpComprobanteFiscalDto.getSerie());
                    fechaEmision = cxpComprobanteFiscalDto.getFechaHoraSello();
                    nombreEmisor = StringManage.getValidString(cxpComprobanteFiscalDto.getEmisorNombre());
                }else if (cxpValeAzulDto!=null){
                    tipo = "VA";
                    idRow = cxpValeAzulDto.getIdCxpValeAzul();
                    importe = cxpValeAzulDto.getImporte();
                    saldo = cxpValeAzulDto.getImporte()- cxpValeAzulDto.getImportePagado();
                    cancelado = cxpValeAzulDto.getIdEstatus() == 2;
                    folio = StringManage.getValidString(cxpValeAzulDto.getFolioGenerado());

                    //Buscamos información de Serie
                    if (cxpValeAzulDto.getIdFolio()>0){
                        Folios foliosDto = new FoliosBO(cxpValeAzulDto.getIdFolio(), getConn()).getFolios();
                        if (foliosDto!=null)
                            serie = StringManage.getValidString(foliosDto.getSerie());
                    }
                    fechaEmision = cxpValeAzulDto.getFechaHoraControl();
                }
                if (saldo<0 && saldo>-0.001)
                    saldo = 0;
                
                long diasCredito;
                long diasMora;
                double porcentajeTiempoTranscurridoCredito = 0;
                if (cxpComprobanteFiscalDto!=null){
                    diasCredito = cxpComprobanteFiscalBO.calculaDiasCredito();
                    diasMora = cxpComprobanteFiscalBO.calculaDiasMora();
                    porcentajeTiempoTranscurridoCredito = cxpComprobanteFiscalBO.calculaPorcentajeTranscurridoCredito();
                }else{
                    diasCredito = cxpValeAzulBO.calculaDiasCredito();
                    diasMora = cxpValeAzulBO.calculaDiasMora();
                    porcentajeTiempoTranscurridoCredito = cxpValeAzulBO.calculaPorcentajeTranscurridoCredito();
                }

                String estatusFinanciero = "Activa";
                if (saldo<=0){
                    estatusFinanciero = "Pagada";
                }

                String nombreSemaforo =  !estatusFinanciero.equals("Pagada")?"En Tiempo":"Pagado";
                if (cancelado) {
                    //Si esta cancelada
                    nombreSemaforo = "Cancelada";
                    estatusFinanciero = "Cancelada";
                }else{
                    if (diasMora>0){
                        //Si excede del plazo de crédito y tiene dias de mora
                        nombreSemaforo = "Plazo Vencido";
                    }else if (porcentajeTiempoTranscurridoCredito>=80){
                        //Si resta 20% para que se venza el plazo de crédito
                        nombreSemaforo = "Por Vencer";
                    }
                }

                hashData.put((String) dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + tipo ));
                hashData.put((String) dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + idRow ));
                hashData.put((String) dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + folio ));
                hashData.put((String) dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + serie ));
                hashData.put((String) dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + fechaEmision ));
                hashData.put((String) dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + nombreEmisor ));
                hashData.put((String) dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + importe ));
                hashData.put((String) dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + saldo));
                hashData.put((String) dataInfo.get(8).get("field"), getRealData(dataInfo.get(8), "" + diasCredito));
                hashData.put((String) dataInfo.get(9).get("field"), getRealData(dataInfo.get(9), "" + estatusFinanciero));
                hashData.put((String) dataInfo.get(10).get("field"), getRealData(dataInfo.get(10), "" + nombreSemaforo));

                dataList.add(hashData);

                hashData = new HashMap<String, String>();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return dataList;
    }
    
    
    /**
     *  EMPLEADO_PRODUCTIVIDAD
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListProductividad(SgfensPedido[] objectDto, String paramExtra) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(EMPLEADO_PRODUCTIVIDAD);
        
        NumberFormat formatMoneda = new DecimalFormat("###,###,###,##0.00");
        int idEmpresa = usuarioBO!=null?usuarioBO.getUser().getIdEmpresa():-1;
        
        
       System.out.println("**********************");
       System.out.println(paramExtra);
        System.out.println("**********************");


        for(SgfensPedido dto:objectDto){
            
            String nombreVendedor ="";
            double totalCobros = 0;     
            
            DatosUsuario datosUsuarioVendedor = null;
            Empleado empleadoDto = null;
            try {
                datosUsuarioVendedor = new UsuarioBO(this.conn, dto.getIdUsuarioVendedor()).getDatosUsuario();
                empleadoDto = new EmpleadoBO(this.conn).findEmpleadoByUsuario(dto.getIdUsuarioVendedor());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            

            if(datosUsuarioVendedor!=null){
                nombreVendedor = (datosUsuarioVendedor.getNombre()!=null?datosUsuarioVendedor.getNombre():"") + " " 
                        + (datosUsuarioVendedor.getApellidoPat()!=null?datosUsuarioVendedor.getApellidoPat():"") + " " 
                        + (datosUsuarioVendedor.getApellidoMat()!=null?datosUsuarioVendedor.getApellidoMat():"");
            }
                    
            SGCobranzaAbonoBO cobranzaAbonoBO = new SGCobranzaAbonoBO(this.conn);
            SgfensCobranzaAbono[] cobranzaAbonoDto = new SgfensCobranzaAbono[0];   
                 
            cobranzaAbonoDto = cobranzaAbonoBO.findCobranzaAbono(-1, idEmpresa , 0, 0, " AND ID_ESTATUS = 1 AND ID_USUARIO_VENDEDOR = " + dto.getIdUsuarioVendedor() +" " + paramExtra);
            
            if(cobranzaAbonoDto.length>0){
                for(SgfensCobranzaAbono item:cobranzaAbonoDto){
                    totalCobros += item.getMontoAbono();
                }
            }
            
            
            String paramsExtraVisitas="";
            paramsExtraVisitas = paramExtra.replace("FECHA_ABONO","FECHA_HORA");
            
            SGVisitaClienteBO visitaClienteBO = new SGVisitaClienteBO(this.conn);
            SgfensVisitaCliente[] visitaClienteDtos = new SgfensVisitaCliente[0];   
                 
            int visitas = 0;
            visitaClienteDtos = visitaClienteBO.findSgfensVisitaClientes(-1, idEmpresa , 0, 0, " AND ID_EMPLEADO_VENDEDOR = " + empleadoDto.getIdEmpleado() +" " + paramsExtraVisitas);
            if(visitaClienteDtos.length>0){
                visitas = visitaClienteDtos.length;
            }
            
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + nombreVendedor ));            
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getIdCliente()));  
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getTotal() ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + totalCobros ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + visitas));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }                    
        
        return dataList;
    }
    
    /**
     *  BITACORA_CR_OPERACION_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(BitacoraCreditosOperacion[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(BITACORA_CR_OPERACION_REPORT);
        
        totalDecimalFields = true;
        totalIntegerFields = true;

        for(BitacoraCreditosOperacion dto : objectDto){
            String nombreEmpleado = "";
            String nombreCliente = "";
            Date fechaHora;
            double monto = 0;
            int creditos = 0;

            if (dto.getIdUserRegistra()>0){
                EmpleadoBO empleadoBO = new EmpleadoBO(getConn());
                try{
                    Empleado  empleadoDto = empleadoBO.findEmpleadoByUsuario(dto.getIdUserRegistra());
                    if (empleadoDto!=null){
                        nombreEmpleado = StringManage.getValidString(empleadoDto.getNombre()) + " "
                                + StringManage.getValidString(empleadoDto.getApellidoPaterno()) +  " "
                                + StringManage.getValidString(empleadoDto.getApellidoMaterno());
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            if (dto.getIdCliente()>0){
                ClienteBO clienteBO = new ClienteBO(dto.getIdCliente(), getConn());
                Cliente clienteDto = clienteBO.getCliente();
                if (StringManage.getValidString(clienteDto.getRazonSocial()).length()>0){
                    nombreCliente = StringManage.getValidString(clienteDto.getRazonSocial());
                }else{
                    nombreCliente =  StringManage.getValidString(clienteDto.getNombreCliente()) + " "
                            + StringManage.getValidString(clienteDto.getApellidoPaternoCliente()) +  " "
                            + StringManage.getValidString(clienteDto.getApellidoMaternoCliente());
                }
            }
            fechaHora =  dto.getFechaHora();
            if (dto.getMontoOperacion()!=0)
                monto = dto.getMontoOperacion();
            if (dto.getTipo()==2){
                //Descuento/Consumo
                creditos = (-1*dto.getCantidad());
            }else{
                creditos = dto.getCantidad();
            }

            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdBitacoraCreditosOperacion()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + nombreEmpleado ));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + nombreCliente ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + fechaHora ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + monto ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + creditos ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + StringManage.getValidString(dto.getComentarios()) ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    
    /**
     *  CXC_FACTURAS_LIST_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListCxCFacturas(ComprobanteFiscal[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(CXC_FACTURAS_LIST_REPORT);
        
        //Mostraremos totales de Columnas tipo Decimal
        totalDecimalFields = true;

        ComprobanteFiscalBO compBO = new ComprobanteFiscalBO(getConn());
        SGPedidoBO pedidoBO = new SGPedidoBO(getConn());
        
        for (ComprobanteFiscal dto : objectDto) {
            try {
                int id = dto.getIdComprobanteFiscal();
                String folio = StringManage.getValidString(dto.getFolioGenerado());
                String serie = " - ";
                Date fechaEmision = dto.getFechaCaptura();
                String clienteNombre = "Sin cliente asignado";
                long diasCredito;
                long diasMora;
                double porcentajeTiempoTranscurridoCredito;
                double importe = dto.getImporteNeto();
                double saldo;
                
                compBO.setComprobanteFiscal(dto);
                                                    
                Cliente cliente = null;
                try{ cliente = new ClienteBO(dto.getIdCliente(),getConn()).getCliente(); }catch(Exception ex){}
                if (cliente!=null)
                    clienteNombre = cliente.getRazonSocial();

                boolean isFacturaFromPedido = false;
                SgfensPedido pedidoDto = null;
                {
                    SgfensPedido[] facturaFromPedidos = new SgfensPedidoDaoImpl(getConn()).findWhereIdComprobanteFiscalEquals(dto.getIdComprobanteFiscal());
                    if (facturaFromPedidos!=null){
                        if (facturaFromPedidos.length>0){
                            isFacturaFromPedido=true;
                            pedidoDto = facturaFromPedidos[0]; 
                            pedidoBO.setPedido(pedidoDto);
                        }
                    }
                }

                Folios foliosDto = null;
                if (dto.getIdFolio()>0){
                    foliosDto = new FoliosBO(dto.getIdFolio(), getConn()).getFolios();
                    if (foliosDto!=null)
                        serie = foliosDto.getSerie();
                }
                
                if (isFacturaFromPedido && pedidoDto != null){
                    saldo = pedidoDto.getBonificacionDevolucion()>0? (pedidoDto.getTotal() - pedidoDto.getAdelanto() - pedidoDto.getSaldoPagado()):(pedidoDto.getTotal() + Math.abs(pedidoDto.getBonificacionDevolucion()) - pedidoDto.getAdelanto() - pedidoDto.getSaldoPagado());
                }else{
                    saldo =dto.getImporteNeto() - dto.getSaldoPagado();
                }
                if (saldo<0 && saldo>-0.001)
                    saldo = 0;
                
                if (pedidoDto!=null){
                    diasCredito = pedidoBO.calculaDiasCredito();
                    diasMora = pedidoBO.calculaDiasMora();
                    porcentajeTiempoTranscurridoCredito = pedidoBO.calculaPorcentajeTranscurridoCredito();
                }else{
                    diasCredito = compBO.calculaDiasCredito();
                    diasMora = compBO.calculaDiasMora();
                    porcentajeTiempoTranscurridoCredito = compBO.calculaPorcentajeTranscurridoCredito();
                }

                String estatusFinanciero = "Activa";
                if (saldo<=0){
                    estatusFinanciero = "Pagada";
                }

                String nombreSemaforo =  !estatusFinanciero.equals("Pagada")?"En Tiempo":"Pagado";
                if (dto.getIdEstatus()==EstatusComprobanteBO.ESTATUS_CANCELACION
                        || dto.getIdEstatus()==EstatusComprobanteBO.ESTATUS_CANCELADA){
                    //Si esta cancelada
                    nombreSemaforo = "Cancelada";
                    estatusFinanciero = "Cancelada";
                }else{
                    if (diasMora>0){
                        //Si excede del plazo de crédito y tiene dias de mora
                        nombreSemaforo = "Plazo Vencido";
                    }else if (porcentajeTiempoTranscurridoCredito>=80){
                        //Si resta 20% para que se venza el plazo de crédito
                        nombreSemaforo = "Por Vencer";
                    }
                }

                hashData.put((String) dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + id ));
                hashData.put((String) dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + folio));
                hashData.put((String) dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + serie ));
                hashData.put((String) dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + fechaEmision ));
                hashData.put((String) dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + clienteNombre));
                hashData.put((String) dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + importe));
                hashData.put((String) dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + saldo));
                hashData.put((String) dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + diasCredito));
                hashData.put((String) dataInfo.get(8).get("field"), getRealData(dataInfo.get(8), "" + estatusFinanciero));
                hashData.put((String) dataInfo.get(9).get("field"), getRealData(dataInfo.get(9), "" + nombreSemaforo));

                dataList.add(hashData);

                hashData = new HashMap<String, String>();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return dataList;
    }
    
    /**
     *  CXC_PEDIDOS_LIST_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListCxCPedidos(SgfensPedido[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(CXC_PEDIDOS_LIST_REPORT);

        //Mostraremos totales de Columnas tipo Decimal
        totalDecimalFields = true;
        
        SGPedidoBO pedidoBO = new SGPedidoBO(getConn());
        
        for (SgfensPedido dto : objectDto) {
            try {
                int id = dto.getIdPedido();
                String folio = StringManage.getValidString(dto.getFolioPedido());
                Date fechaEmision = dto.getFechaPedido();
                String clienteNombre = "Sin cliente asignado";
                long diasCredito;
                long diasMora;
                double porcentajeTiempoTranscurridoCredito;
                double importe;
                double saldo;
                
                pedidoBO.setPedido(dto);
                                                    
                Cliente cliente = null;
                try{ cliente = new ClienteBO(dto.getIdCliente(),getConn()).getCliente(); }catch(Exception ex){}
                if (cliente!=null)
                    clienteNombre = cliente.getRazonSocial();

                importe = dto.getTotal();
                saldo = dto.getBonificacionDevolucion()>0? (dto.getTotal() - dto.getAdelanto() - dto.getSaldoPagado()):(dto.getTotal() + Math.abs(dto.getBonificacionDevolucion()) - dto.getAdelanto()  - dto.getSaldoPagado());
                diasCredito = pedidoBO.calculaDiasCredito();
                diasMora = pedidoBO.calculaDiasMora();
                porcentajeTiempoTranscurridoCredito = pedidoBO.calculaPorcentajeTranscurridoCredito();
                
                if (saldo<0 && saldo>-0.001)
                    saldo = 0;
                if (saldo==0){
                    diasMora = 0;
                    porcentajeTiempoTranscurridoCredito = -1;
                }

                String estatusFinanciero = "Activa";
                if (saldo<=0){
                    estatusFinanciero = "Pagada";
                }

                String nombreSemaforo =  !estatusFinanciero.equals("Pagada")?"En Tiempo":"Pagado";
                if (dto.getIdEstatusPedido()==SGEstatusPedidoBO.ESTATUS_CANCELADO){
                    //Si esta cancelada
                    nombreSemaforo = "Cancelada";
                    estatusFinanciero = "Cancelada";
                }else{
                    if (diasMora>0){
                        //Si excede del plazo de crédito y tiene dias de mora
                        nombreSemaforo = "Plazo Vencido";
                    }else if (porcentajeTiempoTranscurridoCredito>=80){
                        //Si resta 20% para que se venza el plazo de crédito
                        nombreSemaforo = "Por Vencer";
                    }
                }

                hashData.put((String) dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + id ));
                hashData.put((String) dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + folio));
                hashData.put((String) dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + fechaEmision ));
                hashData.put((String) dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + clienteNombre));
                hashData.put((String) dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + importe));
                hashData.put((String) dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + saldo));
                hashData.put((String) dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + diasCredito));
                hashData.put((String) dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + estatusFinanciero));
                hashData.put((String) dataInfo.get(8).get("field"), getRealData(dataInfo.get(8), "" + nombreSemaforo));

                dataList.add(hashData);

                hashData = new HashMap<String, String>();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return dataList;
    }
    
    /**
     * POS_ESTACION_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(PosEstacion[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(POS_ESTACION_REPORT);

        for(PosEstacion dto : objectDto){
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdPosEstacion()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getIdentificacion()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getNombre()));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getFechaHrUltimaConexion()));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getIdEstatus() ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     * DICCIONARIO_I_E_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(DiccionarioIngresosEgresos[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(DICCIONARIO_I_E_REPORT);

        for(DiccionarioIngresosEgresos dto : objectDto){
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdDiccionarioIE()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getNombre()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getDescripcion()));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getEsIngreso()));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getEsGeneral()));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getIdEstatus() ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    
    /**
     * PRECIOS_ESPECIALES_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListClienteConcepto(ClientePrecioConcepto[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(PRECIOS_ESPECIALES_REPORT);
        
        ClienteBO cliBO = new ClienteBO(this.conn);
        ConceptoBO concepBO = new ConceptoBO(this.conn);
        
        for(ClientePrecioConcepto dto : objectDto){
            
            Cliente item2 = null;
            Concepto conceptoDto = null;
            try{
                item2 = cliBO.findClientebyId(dto.getIdCliente());
                conceptoDto = concepBO.findConceptobyId(dto.getIdConcepto());
            }catch(Exception e){e.printStackTrace();}
            
            String nombreCliente = ""; 
            if(item2.getNombreCliente()!=null){
                nombreCliente += item2.getNombreCliente();
            }
            if((item2.getApellidoPaternoCliente()!=null)&&(!item2.getApellidoPaternoCliente().toUpperCase().equals("NULL"))&&(!item2.getApellidoPaternoCliente().toUpperCase().equals("CAMPO POR LLENAR"))){
                nombreCliente += " " + item2.getApellidoPaternoCliente();
            }
            if((item2.getApellidoMaternoCliente()!=null)&&(!item2.getApellidoMaternoCliente().toUpperCase().equals("NULL"))&&(!item2.getApellidoMaternoCliente().toUpperCase().equals("CAMPO POR LLENAR"))){
                nombreCliente += " " + item2.getApellidoPaternoCliente();
            }
            
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + nombreCliente));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + conceptoDto!=null?conceptoDto.getNombreDesencriptado():""));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getPrecioUnitarioCliente()));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getPrecioMedioCliente()));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getPrecioMayoreoCliente()));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getPrecioDocenaCliente()));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + dto.getPrecioEspecialCliente()));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    
    
    /**
     * EXISTENCIA_ALMACEN_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListExistenciaAlmacen(ExistenciaAlmacen[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(EXISTENCIA_ALMACENES_REPORT);
        
        AlmacenBO almBO = new AlmacenBO(this.conn);
        ConceptoBO concepBO = new ConceptoBO(this.conn);
        
        for(ExistenciaAlmacen dto : objectDto){
            
          
            Concepto conceptoDto = null;
            Almacen almacenDto = null;
            
            try{
                conceptoDto = concepBO.findConceptobyId(dto.getIdConcepto());
                almacenDto = almBO.findAlmacenbyId(dto.getIdAlmacen());
            }catch(Exception e){e.printStackTrace();}
            
            
            
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + almacenDto!=null?almacenDto.getNombre():""));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + conceptoDto!=null?conceptoDto.getNombreDesencriptado():""));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getExistencia()));
            

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  GASTOS_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(GastosEvc[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(GASTOS_REPORT);
        
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy" );

        EmpleadoBO empleadoBO = null;
        Empleado empleadoDto = null;
        //Catalogo
        CatalogoGastosBO catalogoGastosBO = null;
        CatalogoGastos catalogoGastosDto = null;
        
        double totalGastos = 0;
        
        for(GastosEvc dto:objectDto){
            
            totalGastos += dto.getMonto();
            
            empleadoBO = new EmpleadoBO(dto.getIdEmpleado(),this.conn);
            empleadoDto = empleadoBO.getEmpleado();

            catalogoGastosBO = new CatalogoGastosBO(dto.getIdConcepto(),this.conn);
            catalogoGastosDto = catalogoGastosBO.getCatalogoGastos();

            String nombreEmpleado = "";

            if(empleadoDto!=null){
                nombreEmpleado += empleadoDto.getNombre()!=null?empleadoDto.getNombre():"";
                nombreEmpleado += " " + (empleadoDto.getApellidoPaterno()!=null?empleadoDto.getApellidoPaterno():"");
                nombreEmpleado += " " + (empleadoDto.getApellidoMaterno()!=null?empleadoDto.getApellidoMaterno():"");
            }
            
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdGastos()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + catalogoGastosDto.getNombre()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + (nombreEmpleado.equals("")?"GASTO DE EMPRESA":nombreEmpleado) ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getMonto()));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + format.format(dto.getFecha())));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getComentario()));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }
        
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "TOTAL:"));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + totalGastos ));
            dataList.add(hashData);
            hashData = new HashMap<String, String>();

        return dataList;
    }
    
    
    /**
     *  REFERENCIA_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(VistaCobranzaReferenciaGrupos[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(REFERENCIA_REPORT);
        
        
        Empleado empleado = null;
        EmpleadoBO empleadoBO = null;
        Empresa empresa = null;
        EmpresaBO empresaBO = null;

        for(VistaCobranzaReferenciaGrupos dato:objectDto){            
            
                try{
                    //PARA BUSCAR EL EMPLEADO-PROMOTOR AL QUE PERTENECE
                    empleadoBO = new EmpleadoBO(this.conn);
                    empleado = empleadoBO.findEmpleadoByUsuario(dato.getIdUsuarioVendedor());
                }catch(Exception ex){
                    empleado.setNombre("");
                    empleado.setApellidoPaterno("");
                    empleado.setApellidoMaterno(""); 
                }

                try{
                    //PARA BUSCAR EL EMPLEADO-PROMOTOR AL QUE PERTENECE
                    empresaBO = new EmpresaBO(this.conn);
                    empresa = empresaBO.findEmpresabyId(dato.getIdEmpresa());
                }catch(Exception ex){
                    empresa.setRazonSocial("");
                }
            

                hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + empresa.getRazonSocial()));
                hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + empleado.getNombre()+" "+empleado.getApellidoPaterno()+" "+empleado.getApellidoMaterno() ));            
                hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dato.getReferencia() ));
                hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dato.getFechaAbono() ));
                hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dato.getTotal() ));
                hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dato.getNumPagos()));    

            
            dataList.add(hashData);

            hashData = new HashMap<String, String>();
            }      
        

        return dataList;
    }
    
    /**
     *  PEDIDO_CONSIGNA_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListConsigna(SgfensPedido[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(PEDIDO_CONSIGNA_REPORT);
        
        NumberFormat formatMoneda = new DecimalFormat("###,###,###,##0.00");
        
        double ventasTotales = 0;
        double pagosTotales = 0;
        double adeudosTotales  = 0;
        double comisionTotalVendedor = 0;
        double utilidadTotal = 0;
        
        Date hoy = new Date();//fecha de hoy
        final long MILLSECS_POR_DAY = 24 * 60 * 60 * 1000; //Milisegundos al día
        double porcentajeComisionConsigna = 0;

        for(SgfensPedido dto:objectDto){
            
            ventasTotales += dto.getTotal();
            pagosTotales += dto.getSaldoPagado();
            adeudosTotales  += (dto.getTotal() - dto.getSaldoPagado());
            
            if(dto.getBonificacionDevolucion()<0){
                ventasTotales += Math.abs(dto.getBonificacionDevolucion());
                adeudosTotales  += Math.abs(dto.getBonificacionDevolucion());
            }
            
            
            DatosUsuario datosUsuarioVendedor = new UsuarioBO(this.conn, dto.getIdUsuarioVendedor()).getDatosUsuario();
            Cliente cliente = null;
            SgfensProspecto prospecto = null;
            try{ cliente = new ClienteBO(dto.getIdCliente(),this.conn).getCliente(); 
                porcentajeComisionConsigna = cliente.getComisionConsigna();
            }catch(Exception ex){}
            String clienteStr = "Sin cliente asignado.";
            
            if (dto.getIdCliente()>0 && cliente!=null){
                //Cliente
                clienteStr = cliente.getRazonSocial() + " [Cliente]";
                
                if(dto.getConsigna()==1){
                    //Cliente consigna
                    clienteStr = cliente.getRazonSocial() + " [Cliente Consigna]";                 
                }else{
                    //Cliente
                    clienteStr = cliente.getRazonSocial() + " [Cliente]";
                }
                
            }
            
            //recuperamos el ultimo abono del pedido para ver cuantos dias han transcurrido del ultimo pago que se considera como días de crédito.
            long diasDeCredito = 0;
            if(dto.getTotal() > (dto.getAdelanto() + dto.getSaldoPagado()) ){//validamos si aun existe monto por pagar, para calcular los dias de crédito
                //obtenemos el ultimo abono del pedido:
                try{
                    SgfensCobranzaAbono abono = new SGCobranzaAbonoBO(this.conn).findCobranzaAbono(0, 0, 0, 1, " AND ID_PEDIDO = " + dto.getIdPedido())[0];
                    diasDeCredito = (hoy.getTime() - abono.getFechaAbono().getTime())/MILLSECS_POR_DAY;
                }catch(Exception e){//si no hay un abono aun, calculamos los dias transcurridos de la fecha realizada del pedido al día de hoy
                    diasDeCredito = (hoy.getTime() - dto.getFechaPedido().getTime())/MILLSECS_POR_DAY;
                };
            }
            
            //Verificamos la cantidad de productos que tiene el pedido
            int cantidadProductos = 0;
            double comisionVendedor = 0;
            double utilidadNeta = 0;
            double costoVentaProducto = 0;
            double ventaTotal = 0;
            double adeudo = 0;
            double comisionConsignatario = 0;
            try{
                SgfensPedidoProducto[] spp = new SgfensPedidoProductoDaoImpl(this.getConn()).findWhereIdPedidoEquals(dto.getIdPedido());
                cantidadProductos = spp.length;
                for(SgfensPedidoProducto product : spp){
                    comisionVendedor += ( product.getPorcentajeComisionEmpleado() * 0.01 * product.getSubtotal() );
                    costoVentaProducto += (product.getCostoUnitario() * product.getCantidad() );// + comisionVendedor);
                    comisionConsignatario += ( porcentajeComisionConsigna * 0.01 * product.getSubtotal() );
                }
                comisionTotalVendedor += comisionVendedor;
            }catch(Exception e){}

            if(dto.getBonificacionDevolucion()>0){
               //utilidadNeta = (dto.getTotal() - costoVentaProducto - comisionConsignatario);
               utilidadNeta = (dto.getTotal());
               //ventaTotal = dto.getTotal();
               ventaTotal = dto.getSubtotal();
               adeudo = dto.getTotal() - dto.getSaldoPagado();
            }else{
               //utilidadNeta = ((dto.getTotal() + Math.abs(dto.getBonificacionDevolucion())) - costoVentaProducto - comisionConsignatario);
               utilidadNeta = (dto.getTotal() + Math.abs(dto.getBonificacionDevolucion()));
               //ventaTotal = dto.getTotal() + Math.abs(dto.getBonificacionDevolucion());
               ventaTotal = dto.getSubtotal() + Math.abs(dto.getBonificacionDevolucion());
               adeudo = (dto.getTotal() + Math.abs(dto.getBonificacionDevolucion())) - dto.getSaldoPagado();
            }
            
            utilidadTotal += utilidadNeta;            
            
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdPedido()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getFechaEntrega()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + (datosUsuarioVendedor!=null? (datosUsuarioVendedor.getNombre() +" " + datosUsuarioVendedor.getApellidoPat()) :"Sin vendedor asignado") ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + clienteStr ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + cantidadProductos ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + ventaTotal ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + formatMoneda.format(comisionConsignatario) ));
            hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + formatMoneda.format(utilidadNeta) ));
            hashData.put((String)dataInfo.get(8).get("field"), getRealData(dataInfo.get(8), "" + (dto.getIdEstatusPedido()==5?"Entregado":(dto.getIdEstatusPedido()==6?"Pendiente":"Entregado")) ));
            hashData.put((String)dataInfo.get(9).get("field"), getRealData(dataInfo.get(9), "" + ((dto.getIdEstatusPedido()!=5&&dto.getIdEstatusPedido()!=6)?"Cerrado":"Abierto" ) ));
            /*hashData.put((String)dataInfo.get(10).get("field"), getRealData(dataInfo.get(10), "" + formatMoneda.format(adeudo)));
            hashData.put((String)dataInfo.get(11).get("field"), getRealData(dataInfo.get(11), "" + formatMoneda.format(comisionConsignatario) ));
            hashData.put((String)dataInfo.get(12).get("field"), getRealData(dataInfo.get(12), "" + formatMoneda.format(utilidadNeta) ));*/

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }
        
            
            /*hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "TOTALES" ));
            hashData.put((String)dataInfo.get(8).get("field"), getRealData(dataInfo.get(8), "" + formatMoneda.format(ventasTotales) ));
            hashData.put((String)dataInfo.get(9).get("field"), getRealData(dataInfo.get(9), "" + formatMoneda.format(pagosTotales) ));
            hashData.put((String)dataInfo.get(10).get("field"), getRealData(dataInfo.get(10), "" + formatMoneda.format(adeudosTotales) ));
            hashData.put((String)dataInfo.get(11).get("field"), getRealData(dataInfo.get(11), "" + formatMoneda.format(comisionTotalVendedor) ));
            hashData.put((String)dataInfo.get(12).get("field"), getRealData(dataInfo.get(12), "" + formatMoneda.format(utilidadTotal) ));
            */
            
            //dataList.add(hashData);
         

        return dataList;
    }
    
    /**
     *  PEDIDO_CONSIGNA_INVENTARIO_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListConsignaInventario(SgfensPedido[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(PEDIDO_CONSIGNA_INVENTARIO_REPORT);
        
        NumberFormat formatMoneda = new DecimalFormat("###,###,###,##0.00");
        
        double ventasTotales = 0;
        double pagosTotales = 0;
        double adeudosTotales  = 0;
        double comisionTotalVendedor = 0;
        double utilidadTotal = 0;
        
        Date hoy = new Date();//fecha de hoy
        final long MILLSECS_POR_DAY = 24 * 60 * 60 * 1000; //Milisegundos al día
        double porcentajeComisionConsigna = 0;

        for(SgfensPedido dto:objectDto){
            
            ventasTotales += dto.getTotal();
            pagosTotales += dto.getSaldoPagado();
            adeudosTotales  += (dto.getTotal() - dto.getSaldoPagado());
            
            if(dto.getBonificacionDevolucion()<0){
                ventasTotales += Math.abs(dto.getBonificacionDevolucion());
                adeudosTotales  += Math.abs(dto.getBonificacionDevolucion());
            }
            
            
            DatosUsuario datosUsuarioVendedor = new UsuarioBO(this.conn, dto.getIdUsuarioVendedor()).getDatosUsuario();
            Cliente cliente = null;
            SgfensProspecto prospecto = null;
            try{ cliente = new ClienteBO(dto.getIdCliente(),this.conn).getCliente(); 
                porcentajeComisionConsigna = cliente.getComisionConsigna();
            }catch(Exception ex){}
            String clienteStr = "Sin cliente asignado.";
            
            if (dto.getIdCliente()>0 && cliente!=null){
                //Cliente
                clienteStr = cliente.getRazonSocial() + " [Cliente]";
                
                if(dto.getConsigna()==1){
                    //Cliente consigna
                    clienteStr = cliente.getRazonSocial() + " [Cliente Consigna]";                 
                }else{
                    //Cliente
                    clienteStr = cliente.getRazonSocial() + " [Cliente]";
                }
                
            }
            
            //recuperamos el ultimo abono del pedido para ver cuantos dias han transcurrido del ultimo pago que se considera como días de crédito.
            long diasDeCredito = 0;
            if(dto.getTotal() > (dto.getAdelanto() + dto.getSaldoPagado()) ){//validamos si aun existe monto por pagar, para calcular los dias de crédito
                //obtenemos el ultimo abono del pedido:
                try{
                    SgfensCobranzaAbono abono = new SGCobranzaAbonoBO(this.conn).findCobranzaAbono(0, 0, 0, 1, " AND ID_PEDIDO = " + dto.getIdPedido())[0];
                    diasDeCredito = (hoy.getTime() - abono.getFechaAbono().getTime())/MILLSECS_POR_DAY;
                }catch(Exception e){//si no hay un abono aun, calculamos los dias transcurridos de la fecha realizada del pedido al día de hoy
                    diasDeCredito = (hoy.getTime() - dto.getFechaPedido().getTime())/MILLSECS_POR_DAY;
                };
            }
            
            //Verificamos la cantidad de productos que tiene el pedido
            int cantidadProductos = 0;
            double comisionVendedor = 0;
            double utilidadNeta = 0;
            double costoVentaProducto = 0;
            //double ventaTotal = 0;
            //double adeudo = 0;
            double comisionConsignatario = 0;
            try{
                SgfensPedidoProducto[] spp = new SgfensPedidoProductoDaoImpl(this.getConn()).findWhereIdPedidoEquals(dto.getIdPedido());
                cantidadProductos = spp.length;
                for(SgfensPedidoProducto product : spp){
                    comisionVendedor += ( product.getPorcentajeComisionEmpleado() * 0.01 * product.getSubtotal() );
                    costoVentaProducto += (product.getCostoUnitario() * product.getCantidad() );// + comisionVendedor);
                    comisionConsignatario += ( porcentajeComisionConsigna * 0.01 * product.getSubtotal() );
                    
                    hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + product.getIdPedido()));
                    hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + (datosUsuarioVendedor!=null? (datosUsuarioVendedor.getNombre() +" " + datosUsuarioVendedor.getApellidoPat()) :"Sin vendedor asignado") ));
                    hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + clienteStr ));
                    hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + product.getDescripcion() ));
                    hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + product.getCantidad() ));
                    hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + product.getPrecioUnitario() ));
                    hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + formatMoneda.format(product.getSubtotal()) ));
                    
                    dataList.add(hashData);

                    hashData = new HashMap<String, String>();
                    
                }
                comisionTotalVendedor += comisionVendedor;
            }catch(Exception e){}

/*            if(dto.getBonificacionDevolucion()>0){
               utilidadNeta = (dto.getTotal() - costoVentaProducto - comisionConsignatario);
               ventaTotal = dto.getTotal();
               adeudo = dto.getTotal() - dto.getSaldoPagado();
            }else{
               utilidadNeta = ((dto.getTotal() + Math.abs(dto.getBonificacionDevolucion())) - costoVentaProducto - comisionConsignatario);
               ventaTotal = dto.getTotal() + Math.abs(dto.getBonificacionDevolucion());
               adeudo = (dto.getTotal() + Math.abs(dto.getBonificacionDevolucion())) - dto.getSaldoPagado();
            }
*/            
            utilidadTotal += utilidadNeta;            
                        
            
        }
        
            
            /*hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "TOTALES" ));
            hashData.put((String)dataInfo.get(8).get("field"), getRealData(dataInfo.get(8), "" + formatMoneda.format(ventasTotales) ));
            hashData.put((String)dataInfo.get(9).get("field"), getRealData(dataInfo.get(9), "" + formatMoneda.format(pagosTotales) ));
            hashData.put((String)dataInfo.get(10).get("field"), getRealData(dataInfo.get(10), "" + formatMoneda.format(adeudosTotales) ));
            hashData.put((String)dataInfo.get(11).get("field"), getRealData(dataInfo.get(11), "" + formatMoneda.format(comisionTotalVendedor) ));
            hashData.put((String)dataInfo.get(12).get("field"), getRealData(dataInfo.get(12), "" + formatMoneda.format(utilidadTotal) ));
            */
            
            //dataList.add(hashData);
         

        return dataList;
    }
    
    
    /**
     * DICCIONARIO_I_E_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListVisitaClientes(SgfensVisitaCliente[] objectDto) {
        
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(VISITAS_A_CLIENTES_REPORT);

        
        String nombreEmpleado = "";
        String nombreCliente = "";
        String opcionRegistro = "";
        
        
        for(SgfensVisitaCliente dto : objectDto){
            
            Empleado emp =  null;
            Cliente cliente = null;
            
            try{ emp = new EmpleadoBO(dto.getIdEmpleadoVendedor(), this.conn).getEmpleado(); }catch(Exception ex){}                                              
            nombreEmpleado = emp.getNombre()+" "+emp.getApellidoPaterno() + " " + emp.getApellidoMaterno();
            
            
            try{ cliente = new ClienteBO(dto.getIdCliente(),this.conn).getCliente(); }catch(Exception ex){}            
            if (cliente!=null){             
                nombreCliente = cliente.getRazonSocial();
            }       
            
            opcionRegistro = (dto.getIdOpcion()==1?"Local Cerrado":dto.getIdOpcion()==2?"Sin Dinero":dto.getIdOpcion()==3?"Aún cuenta con mercancía":dto.getIdOpcion()==4?"Otro":"");
            
            
            
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdVisita()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + nombreEmpleado));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + nombreCliente));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + opcionRegistro));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + (dto.getFechaHora()!=null?DateManage.formatDateTimeToNormalMinutes(dto.getFechaHora()):"")));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getComentarios()));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  EMPLEADO_CORTE_INVENTARIO_HISTORICO_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListInventarioHistoricoVendedor(InventarioHistoricoVendedor[] objectDto, int idEmpleado, String fechaFiltroInicial, String fechaFiltroFinal, String fechaFiltroInicial_B, String fechaFiltroFinal_B) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(EMPLEADO_CORTE_INVENTARIO_HISTORICO_REPORT);       
        
        NumberFormat formatMoneda = new DecimalFormat("###,###,###,##0.00");
        int idEmpresa = usuarioBO!=null?usuarioBO.getUser().getIdEmpresa():-1;
        
            //Obtenemos empleado
         Empleado empleadoDto = new Empleado();
         try{
             EmpleadoBO empleadoBO = new EmpleadoBO(this.conn);     
             empleadoDto = empleadoBO.findEmpleadobyId(idEmpleado);
         }catch(Exception e){} 
         
        String mensajeIntervaloFechas = "";
        if(fechaFiltroInicial_B != null && fechaFiltroFinal_B != null){
            mensajeIntervaloFechas = " Del " + fechaFiltroInicial_B + " al " + fechaFiltroFinal_B;
        }else if(fechaFiltroInicial_B == null && fechaFiltroFinal_B != null){
            mensajeIntervaloFechas = " Hasta el " + fechaFiltroFinal_B;
        }else if(fechaFiltroInicial_B != null && fechaFiltroFinal_B == null){
            mensajeIntervaloFechas = " Desde " + fechaFiltroInicial_B;
        }
        
        Encrypter encripDesencri = new Encrypter();
                                        SGPedidoBO pedidosBO = new SGPedidoBO(this.conn); 
                                        SGPedidoProductoBO partidasPedidoBO = new SGPedidoProductoBO(this.conn);
                                        SGPedidoDevolucionesCambioBO camDevBO = new SGPedidoDevolucionesCambioBO(this.conn);
                                         
                                        double cantidadVendida = 0;
                                        double cantidadDevoluciones = 0;
                                        double cantidadMerma = 0;
                                        double cantidadCambios = 0;
                                        double invFinal = 0;
                                        double totalProdsVendidos = 0;
                                        double totalProdsMerma = 0;
                                        double totalProdsVendidosDinero = 0;
                                        double totalProdsMermaDinero = 0;
                                        String fechaInicial = "";
                                        double cantidadDevolucionAptos = 0;
                                        BigDecimal montoVentaTotalProducto = new BigDecimal("0.0");////
                                        double montoVentaTotalProductoAyuda = 0;
                                        BigDecimal montoVentaTotalProductoTotales = new BigDecimal("0.0");////
                                        
                                        BigDecimal cantidadEntregadoPorPedidos = new BigDecimal("0.0");////
                                        SgfensPedidoProducto[] sgfensPedidoProducto = null;////
                                        SgfensPedidoProductoDaoImpl sgfensPedidoProductoDaoImpl = new SgfensPedidoProductoDaoImpl(this.conn);////                                        
                                        double cantidadMismoProducto = 0;////
                                        double cantidadDistitoProducto = 0;////
                                        double cantidadPendienteEntrega = 0;/////
                                        int cantidadPedidos = 0;
                                        
                                                                                
                                            for (InventarioHistoricoVendedor item : objectDto){//////
                                                if(item.getIdEmpleado() > 0 && item.getIdConcepto() > 0){
                                                    //Limpiamos variables
                                                    cantidadVendida = 0 ;
                                                    cantidadDevoluciones = 0;
                                                    cantidadMerma = 0;
                                                    cantidadCambios = 0;
                                                    invFinal = 0;
                                                    fechaInicial = item.getFechaRegistro().toString();

                                                    cantidadEntregadoPorPedidos = new BigDecimal("0.0");////
                                                    sgfensPedidoProducto = null;////
                                                    cantidadMismoProducto = 0;////
                                                    cantidadDistitoProducto = 0;////
                                                    cantidadDevolucionAptos = 0;/////
                                                    cantidadPendienteEntrega = 0;/////
                                                    montoVentaTotalProducto = new BigDecimal("0.0");/////
                                                    montoVentaTotalProductoAyuda = 0;/////
                                                    cantidadPedidos = 0;/////

                                                    try{////
                                                        sgfensPedidoProducto = sgfensPedidoProductoDaoImpl.findWhereIdConceptoEquals(item.getIdConcepto());}catch(Exception e){}////
                                                    if(sgfensPedidoProducto != null){////
                                                        for(SgfensPedidoProducto productos : sgfensPedidoProducto){////
                                                            cantidadEntregadoPorPedidos = cantidadEntregadoPorPedidos.add(new BigDecimal(productos.getCantidadEntregada()));////
                                                        }////
                                                    }////



                                                    try{                                                    
                                                        Concepto concepto = new ConceptoBO(item.getIdConcepto(),this.conn).getConcepto();


                                                        //Ventas
                                                        //Obtenemos Pedidos del vendedor
                                                        System.out.println("******PEDIDOS*******");
                                                        //String filtroPedidosVendedor = " AND ID_USUARIO_VENDEDOR ='" + empleadoDto.getIdUsuarios() + "'  AND FECHA_PEDIDO >= '" +item.getFechaRegistro() + "' AND (ID_ESTATUS_PEDIDO <> 3 AND ID_ESTATUS_PEDIDO <> 1) ";
                                                        //String filtroPedidosVendedor = " AND ID_USUARIO_VENDEDOR ='" + empleadoDto.getIdUsuarios() + "'  AND FECHA_PEDIDO >= '" +DateManage.formatDateToSQL(fechaFiltroInicial) + "' AND FECHA_PEDIDO < '" + DateManage.formatDateToSQL(fechaFiltroFinal) + "' AND (ID_ESTATUS_PEDIDO <> 3 AND ID_ESTATUS_PEDIDO <> 1) ";//////BUSCAMOS LOS PEDIDOS QUE CAEN DENTRO DEL RANGO DEL PERIODO DE BUSQUEDA
                                                        String filtroPedidosVendedor = "";
                                                        if(fechaFiltroInicial != null && fechaFiltroFinal != null){
                                                            filtroPedidosVendedor = " AND ID_USUARIO_VENDEDOR ='" + empleadoDto.getIdUsuarios() + "'  AND FECHA_PEDIDO >= '" +fechaFiltroInicial + "' AND FECHA_PEDIDO < '" + fechaFiltroFinal + "' AND (ID_ESTATUS_PEDIDO <> 3 AND ID_ESTATUS_PEDIDO <> 1) ";//////BUSCAMOS LOS PEDIDOS QUE CAEN DENTRO DEL RANGO DEL PERIODO DE BUSQUEDA
                                                        }else if(fechaFiltroInicial == null && fechaFiltroFinal != null){
                                                            filtroPedidosVendedor = " AND ID_USUARIO_VENDEDOR ='" + empleadoDto.getIdUsuarios() + "'  AND FECHA_PEDIDO < '" + fechaFiltroFinal + "' AND (ID_ESTATUS_PEDIDO <> 3 AND ID_ESTATUS_PEDIDO <> 1) ";//////BUSCAMOS LOS PEDIDOS QUE CAEN DENTRO DEL RANGO DEL PERIODO DE BUSQUEDA
                                                        }else if(fechaFiltroInicial != null && fechaFiltroFinal == null){
                                                            filtroPedidosVendedor = " AND ID_USUARIO_VENDEDOR ='" + empleadoDto.getIdUsuarios() + "'  AND FECHA_PEDIDO >= '" +fechaFiltroInicial + "' AND (ID_ESTATUS_PEDIDO <> 3 AND ID_ESTATUS_PEDIDO <> 1) ";//////BUSCAMOS LOS PEDIDOS QUE CAEN DENTRO DEL RANGO DEL PERIODO DE BUSQUEDA
                                                        }
                                                        SgfensPedido[] pedidos = pedidosBO.findPedido(-1, idEmpresa, -1, -1, filtroPedidosVendedor);
                                                        cantidadPedidos = pedidos.length;
                                                        if(pedidos.length>0){

                                                            for(SgfensPedido pedido : pedidos){
                                                                //Obtenemos partidas del pedido
                                                                System.out.println("******PRODUCTOS*******");
                                                                SgfensPedidoProducto[] partidasPedido = partidasPedidoBO.findByIdConcepto(item.getIdConcepto(), -1, -1," ID_PEDIDO="+pedido.getIdPedido() );
                                                                if(partidasPedido.length>0){
                                                                    for(SgfensPedidoProducto partida:partidasPedido ){
                                                                        //Sumamos cantidades

                                                                        cantidadVendida += partida.getCantidadEntregada();
                                                                        totalProdsVendidosDinero += (partida.getCantidadEntregada()* partida.getPrecioUnitario());
                                                                        cantidadPendienteEntrega += (partida.getCantidad() - partida.getCantidadEntregada() );/////
                                                                        montoVentaTotalProductoAyuda = 0;
                                                                        montoVentaTotalProductoAyuda = ( (partida.getCantidadEntregada() ) * partida.getPrecioUnitario());/////
                                                                        montoVentaTotalProducto =  montoVentaTotalProducto.add(new BigDecimal(montoVentaTotalProductoAyuda));
                                                                    }
                                                                }  
                                                            }                                                       

                                                        }   
                                                        montoVentaTotalProductoTotales = montoVentaTotalProductoTotales.add(montoVentaTotalProducto);


                                                        //Obtenemos devoluciones y cambios del pedido

                                                        System.out.println("******DEVOLUCIONES Y CAMBIOS*******");
                                                        //String filtroDevs  = " AND FECHA  >= '"+ item.getFechaRegistro()+"' ";
                                                        //String filtroDevs  = " AND FECHA  >= '"+ DateManage.formatDateToSQL(fechaFiltroInicial)+"' AND FECHA < '" + DateManage.formatDateToSQL(fechaFiltroFinal) + "'";
                                                        String filtroDevs  = "";
                                                        if(fechaFiltroInicial != null && fechaFiltroFinal != null){
                                                            filtroDevs  = " AND FECHA  >= '"+ fechaFiltroInicial+"' AND FECHA < '" + fechaFiltroFinal + "'";
                                                        }else if(fechaFiltroInicial == null && fechaFiltroFinal != null){
                                                            filtroDevs  = " AND FECHA < '" + fechaFiltroFinal + "'";
                                                        }else if(fechaFiltroInicial != null && fechaFiltroFinal == null){
                                                            filtroDevs  = " AND FECHA  >= '"+ fechaFiltroInicial+"'";
                                                        }
                                                        SgfensPedidoDevolucionCambio[] devoluciones = camDevBO.findCambioDevByEmpleado(this.conn, empleadoDto.getIdEmpleado(), filtroDevs);

                                                        if(devoluciones.length>0){
                                                            for(SgfensPedidoDevolucionCambio dev:devoluciones){

                                                                 //Precio de venta del pedido 
                                                                SgfensPedidoProducto[] conceptoPedidoDto = partidasPedidoBO.findByIdPedido(dev.getIdPedido(), -1, -1, -1, " AND ID_CONCEPTO="+dev.getIdConcepto());

                                                                //if(conceptoPedidoDto.length>0){

                                                                    if(item.getIdConcepto()==dev.getIdConcepto()){

                                                                        //cantidadDevoluciones += dev.getAptoParaVenta();
                                                                        cantidadMerma += dev.getNoAptoParaVenta();
                                                                        try{
                                                                            totalProdsMermaDinero += ( conceptoPedidoDto[0].getPrecioUnitario());
                                                                        }catch(Exception e){
                                                                            totalProdsMermaDinero += 0;
                                                                        }
                                                                        if(dev.getIdTipo() == 2){////cambio
                                                                            if(item.getIdConcepto() == dev.getIdConceptoEntregado()){////
                                                                                cantidadMismoProducto += (dev.getAptoParaVenta() + dev.getNoAptoParaVenta());////
                                                                            }else{////
                                                                                cantidadDistitoProducto += (dev.getAptoParaVenta() + dev.getNoAptoParaVenta());////
                                                                            }///                                                                            
                                                                        }else if(dev.getIdTipo() == 1){//devolucion                                                                        
                                                                               cantidadDevoluciones = cantidadDevoluciones + dev.getAptoParaVenta() + dev.getNoAptoParaVenta();                                                                       
                                                                        }
                                                                        cantidadDevolucionAptos += dev.getAptoParaVenta();/////
                                                                    }/*else{
                                                                        if(dev.getIdTipo() == 1){//devolucion                                                                        
                                                                               cantidadDevoluciones = cantidadDevoluciones + dev.getAptoParaVenta() + dev.getNoAptoParaVenta();                                                                       
                                                                        } 
                                                                    }*/     

                                                                    if(item.getIdConcepto()==dev.getIdConceptoEntregado()){
                                                                        if(dev.getCantidadDevuelta()>0){
                                                                            cantidadCambios += dev.getCantidadDevuelta();
                                                                        }
                                                                    }
                                                               // }                                                          

                                                            }
                                                            }

                                                        //cantidadVendida += cantidadMismoProducto;////


                                                        //invFinal = item.getCantidad() - cantidadVendida + cantidadDevoluciones - cantidadCambios + cantidadMerma;
                                                        //invFinal = item.getCantidad() - cantidadVendida + cantidadDevoluciones - cantidadCambios;
                                                        //invFinal = item.getCantidad() - cantidadVendida + (cantidadDevoluciones !=0? cantidadDevoluciones : cantidadMerma) - cantidadMismoProducto + cantidadDistitoProducto;
                                                        if(cantidadMerma != 0){
                                                            //invFinal = item.getCantidad() - cantidadVendida + (cantidadMerma) - cantidadCambios;
                                                            //////invFinal = item.getCantidad() - cantidadVendida + (cantidadMerma) - cantidadCambios;
                                                            //invFinal = item.getCantidad() - cantidadVendida - cantidadCambios + cantidadMismoProducto;                                                        
                                                            //invFinal = item.getCantidad() - cantidadVendida - cantidadCambios;
                                                            invFinal = item.getCantidadAsignada()- (cantidadVendida + cantidadCambios) + cantidadDevoluciones + cantidadMismoProducto + cantidadDistitoProducto;
                                                        }else{
                                                            //invFinal = item.getCantidad() - cantidadVendida - cantidadMismoProducto + cantidadDistitoProducto - cantidadCambios;
                                                            /////////invFinal = item.getCantidad() - cantidadVendida - cantidadCambios + cantidadMismoProducto;                                                        
                                                            invFinal = item.getCantidadAsignada() - (cantidadVendida + cantidadCambios) + cantidadDevoluciones + cantidadMismoProducto + cantidadDistitoProducto;
                                                        }

                                                        totalProdsVendidos += cantidadVendida;
                                                        totalProdsMerma += cantidadMerma;
                    
                    
                    
                    
                   
                 
                String conceptoDescript = "";
                try{
                    conceptoDescript =  encripDesencri.decodeString(concepto.getNombre());   
                }catch(Exception e){}
                    
                hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + item.getIdConcepto()));
                hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + conceptoDescript ));
                hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + concepto.getDescripcion()));
                hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + item.getCantidadTerminno()));
                hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + ( item.getCantidadAsignada() - item.getCantidadTerminno())));           
                hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + item.getCantidadAsignada()));                
                hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + (cantidadVendida + cantidadCambios)));////
                hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + (cantidadDevoluciones)));
                //hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + cantidadCambios));
                hashData.put((String)dataInfo.get(8).get("field"), getRealData(dataInfo.get(8), "" + cantidadMismoProducto));////
                hashData.put((String)dataInfo.get(9).get("field"), getRealData(dataInfo.get(9), "" + cantidadDistitoProducto));////                
                hashData.put((String)dataInfo.get(10).get("field"), getRealData(dataInfo.get(10), "" + invFinal));
                hashData.put((String)dataInfo.get(11).get("field"), getRealData(dataInfo.get(11), "" + (invFinal + cantidadMerma)));
                hashData.put((String)dataInfo.get(12).get("field"), getRealData(dataInfo.get(12), "" + cantidadMerma));
                hashData.put((String)dataInfo.get(13).get("field"), getRealData(dataInfo.get(13), "" + cantidadDevolucionAptos));
                hashData.put((String)dataInfo.get(14).get("field"), getRealData(dataInfo.get(14), "" + cantidadPendienteEntrega));
                hashData.put((String)dataInfo.get(15).get("field"), getRealData(dataInfo.get(15), "" + (cantidadVendida)));////
                hashData.put((String)dataInfo.get(16).get("field"), getRealData(dataInfo.get(16), "" + formatMoneda.format(montoVentaTotalProducto.setScale(2, RoundingMode.HALF_UP))));
           
            dataList.add(hashData);
            }catch(Exception ex){
                ex.printStackTrace();
            } 

            hashData = new HashMap<String, String>();
        }
        }
            
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "Articulos Vendidos:" ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + totalProdsVendidos));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + "Total:"));            
            //hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "$" + formatMoneda.format(totalProdsVendidosDinero) ));            
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "$" + formatMoneda.format(montoVentaTotalProductoTotales) ));            
          
            dataList.add(hashData);
            hashData = new HashMap<String, String>();
            
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "Articulos en Merma:" ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + totalProdsMerma));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + "Total:")); 
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "$" + formatMoneda.format(totalProdsMermaDinero)));            
          
            dataList.add(hashData);
            hashData = new HashMap<String, String>();
            
            SgfensVisitaCliente[] sgfensVisitaCliente = new SgfensVisitaCliente[0];
            try{
                if(fechaFiltroInicial != null && fechaFiltroFinal != null){
                    sgfensVisitaCliente = new SgfensVisitaClienteDaoImpl(this.conn).findByDynamicWhere("ID_EMPLEADO_VENDEDOR = " + idEmpleado + " AND FECHA_HORA > '" + fechaFiltroInicial + "' AND FECHA_HORA < '" + fechaFiltroFinal + "' " , null);
                }else if(fechaFiltroInicial == null && fechaFiltroFinal != null){
                    sgfensVisitaCliente = new SgfensVisitaClienteDaoImpl(this.conn).findByDynamicWhere("ID_EMPLEADO_VENDEDOR = " + idEmpleado + " AND FECHA_HORA < '" + fechaFiltroFinal + "' " , null);
                }else if(fechaFiltroInicial != null && fechaFiltroFinal == null){
                    sgfensVisitaCliente = new SgfensVisitaClienteDaoImpl(this.conn).findByDynamicWhere("ID_EMPLEADO_VENDEDOR = " + idEmpleado + " AND FECHA_HORA > '" + fechaFiltroInicial + "' " , null);
                }
            }catch(Exception e){}
            
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "Visitas NO venta:"));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), sgfensVisitaCliente!=null?sgfensVisitaCliente.length+"":"0" ));
            dataList.add(hashData);
            
            hashData = new HashMap<String, String>();
            
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "Cantidad de Ventas:"));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), cantidadPedidos+"" ));
            dataList.add(hashData);
            
            hashData = new HashMap<String, String>();
            
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "Visitas Realizadas:"));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), ((cantidadPedidos + (sgfensVisitaCliente!=null?sgfensVisitaCliente.length:0)))+"" ));
            dataList.add(hashData);
            
            hashData = new HashMap<String, String>();
            
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "Eficacia:"));
            try{
                hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), FormatUtil.doubleToString(((cantidadPedidos * 100) / ((cantidadPedidos + (sgfensVisitaCliente!=null?sgfensVisitaCliente.length:0)))))+"%" ));
            }catch(Exception e){
                hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), FormatUtil.doubleToString(0)+"%" ));
            }
            dataList.add(hashData);
            
            /*hashData = new HashMap<String, String>();
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "Venta Total:"));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), formatMoneda.format(montoVentaTotalProductoTotales) ));
            dataList.add(hashData);*/
            
            hashData = new HashMap<String, String>();
            
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "Periodo de Inventario:"));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), mensajeIntervaloFechas ));
            dataList.add(hashData);
            hashData = new HashMap<String, String>();

        return dataList;
    }
    
    
    /**
     * 
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataListCronometro(Cronometro[] objectDto) {
        
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(CRONOMETRO_REPORT);

        
        String nombreEmpleado = "";
        String nombreCliente = "";
        long totalMinutos = 0;
        
        
        for(Cronometro dto : objectDto){
            
            Empleado emp =  null;
            Cliente cliente = null;
            
            try{ emp = new EmpleadoBO(dto.getIdEmpleado(), this.conn).getEmpleado(); }catch(Exception ex){}                                              
            nombreEmpleado = emp.getNombre()+" "+emp.getApellidoPaterno() + " " + emp.getApellidoMaterno();
            
            
            try{ cliente = new ClienteBO(dto.getIdCliente(),this.conn).getCliente(); }catch(Exception ex){}            
            if (cliente!=null){             
                nombreCliente = cliente.getRazonSocial();
            }       
            
            //Tiempo cronometrado en min
            long ini = dto.getFechaInicio().getTime();
            long fin = dto.getFechaFin().getTime(); 

            long diferencia = (fin - ini)/(1000*60);    

            totalMinutos+= diferencia;             
            
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdCronometro()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + nombreEmpleado));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + nombreCliente));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + (dto.getFechaInicio()!=null?DateManage.formatDateTimeToNormalMinutes(dto.getFechaInicio()):"")));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + (dto.getFechaFin()!=null?DateManage.formatDateTimeToNormalMinutes(dto.getFechaFin()):"")));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + diferencia));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
                       
            
        }
        
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "Min Totales:"));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), ""+totalMinutos ));
            dataList.add(hashData);
            
            hashData = new HashMap<String, String>();

        return dataList;
    }
    
    /**
     *  RUTAS_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(Ruta[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(RUTAS_REPORT);
        
        int idEmpresa = usuarioBO!=null?usuarioBO.getUser().getIdEmpresa():-1;
                
        EmpleadoDaoImpl empleadoDaoImpl = new EmpleadoDaoImpl(this.conn);
        RutaMarcadorDaoImpl rutaMarcadorDaoImpl = new RutaMarcadorDaoImpl(this.conn);
        
        ConvertidorCoordenadasVerificadorPoligonos validador = null;
                
        for(Ruta dto:objectDto){

            Empleado empleado = null;
            try{
                empleado = empleadoDaoImpl.findByPrimaryKey(dto.getIdEmpleado());
            }catch(Exception e){}
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdRuta()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getNombreRuta()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + (dto.getFhModificaRuta()==null?DateManage.formatDateTimeToNormalMinutes(dto.getFhRegRuta()):DateManage.formatDateTimeToNormalMinutes(dto.getFhModificaRuta()) ) ) );
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + (empleado!=null?(empleado.getNombre()+" "+empleado.getApellidoPaterno()+" "+empleado.getApellidoMaterno()):"") ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getParadasRuta() ));
            
            dataList.add(hashData);

            hashData = new HashMap<String, String>();
            
            //agregamos los puntos de la ruta
            RutaMarcador[] rutaMarcador = null;
            try{
                rutaMarcador = rutaMarcadorDaoImpl.findWhereIdRutaEquals(dto.getIdRuta());}catch(Exception e){}
            if(rutaMarcador != null){
                
                hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "ITEM"));
                hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "PUNTO/CLIENTE" ));           
                hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "DIRECCION" ));
                dataList.add(hashData);

                hashData = new HashMap<String, String>();
               
                Abcdario abcdario = new Abcdario();
                String[] mrca = abcdario.createAbcdario2(dto.getParadasRuta());
                int contadorMarcador = 1;
                
                for(RutaMarcador marcador : rutaMarcador){
                    
                    //verificamos si el marcador corresponde a un cliente:
                    Cliente[] clientes = null;
                    Cliente cliente = null;
                    //cargamos las coordenadas con el marcador, para ver a su alrededor si el cliente esta cerca y se maca como marcador de cliente
                    validador = new ConvertidorCoordenadasVerificadorPoligonos();
                    validador.ordenadorCoordenadas("30.0,"+marcador.getLatitudMarcador()+","+marcador.getLongitudMarcador(), 1);
                    //Primero verificamos si hay un cliente con las coordenadas del marcador:
                    try{
                        clientes = new ClienteBO(this.conn).findClientes(0, idEmpresa, 0, 0, " AND LATITUD = '"+ marcador.getLatitudMarcador() +"'  AND LONGITUD = '"+ marcador.getLongitudMarcador() +"'");
                        cliente = clientes[0];
                        System.out.println("++++++++++++ CARGANDO A CLIENTES POR BUSQUEDA IGUALITA DE LATITUD Y LONGITUD . . .");
                    }catch(Exception e){}
                    try{//si no existio cliente con las coordenadas buscadas, buscaremos su alguno da en el punto
                        if(clientes != null && clientes.length <= 0){//si no trae registro de algun cliente, entonces empezaremos a verificar a que cliente podria corresponder el punto
                            clientes = new ClienteBO(this.conn).findClientes(0, idEmpresa, 0, 0, " AND LATITUD IS NOT NULL AND LONGITUD IS NOT NULL ");
                            System.out.println("++++++++++++ CARGANDO A TODOS LOS CLIENTES, YA QUE NINGUN PUNTO EXACTO DE CLIENTE . . .");
                            for(Cliente cli : clientes){//verificamos si hay algun cliente que corresponda al marcador
                                boolean contenido = false;
                                contenido = validador.puntoContenidoEnPoligono(String.valueOf(cli.getLatitud()), String.valueOf(cli.getLongitud()), 1);
                                if(contenido){//si es verdadero es que esta dentro de la zona que colocamos para considerar que es marcador de cliente
                                    System.out.println("++++++++++++ SE CARGO UN CLIENTE QUE SE INTERSECTO CON LAS COORDENADAS DE MARCA");
                                    cliente = cli;
                                }
                            }
                        }
                    }catch(Exception e){System.out.println(" ---- ERROR AL CONSULTAR CLIENTES CON LONGITUD Y LATITUD, PARA GENERAR EL REPORTE");
                    e.printStackTrace();
                    }
                   
                    hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + contadorMarcador ));
                    if(cliente != null){
                        hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + cliente.getRazonSocial()!=null&&!cliente.getRazonSocial().trim().equals("")?cliente.getRazonSocial():cliente.getNombreCliente()+" "+cliente.getApellidoPaternoCliente()+" "+cliente.getApellidoMaternoCliente() ));
                        hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + cliente.getCalle() + " " + cliente.getNumero() + (cliente.getColonia()!=null&&!cliente.getColonia().trim().equals("")?(", Col. " + cliente.getColonia()):"") + (cliente.getMunicipio()!=null&&!cliente.getMunicipio().trim().equals("")?(", Del. " + cliente.getMunicipio()):"") + (cliente.getCodigoPostal()!=null&&!cliente.getCodigoPostal().trim().equals("")?(", C.P. " + cliente.getCodigoPostal()):"") + (cliente.getEstado()!=null&&!cliente.getEstado().trim().equals("")?(", Estado " + cliente.getEstado()):"") + (cliente.getPais()!=null&&!cliente.getPais().trim().equals("")?(", " + cliente.getPais()):"") ));
                    }else{
                        hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + mrca[contadorMarcador-1] ));
                        hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + "-" )); //https://developers.google.com/api-client-library/java/apis/mapsengine/v1
                    }                    
                    dataList.add(hashData);
                    
                    contadorMarcador++;

                    hashData = new HashMap<String, String>();
                }
                
                //--metemos un salto de linea para que sirva como separador
                hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), ""));
                dataList.add(hashData);
                hashData = new HashMap<String, String>();
                //--
                
            }
            
        }
        
        return dataList;
    }
    
    
    /**
     *  SMS_PLANTILLAS_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(SmsPlantilla[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(SMS_PLANTILLAS_REPORT);
        
        SmsEnvioDetalleBO smsEnvioDetalleBO = new SmsEnvioDetalleBO(this.conn);
        for(SmsPlantilla dto : objectDto){
            Date ultimaFechaEnvio = null;
            int cantidadEnviados = 0;
            try{
                int idEmpresa = usuarioBO!=null?usuarioBO.getUser().getIdEmpresa():-1;
                cantidadEnviados = smsEnvioDetalleBO.findCantidadSmsEnvioDetalles(0, idEmpresa, 0, 0, " AND enviado = 1 AND id_sms_envio_lote IN (SELECT DISTINCT id_sms_envio_lote FROM sms_envio_lote WHERE id_sms_plantilla = " + dto.getIdSmsPlantilla() + ")");
                SmsEnvioDetalle[] ultimoEnviadoPlantilla = smsEnvioDetalleBO.findSmsEnvioDetalles(0, idEmpresa, 0, 1, " AND enviado = 1 AND id_sms_envio_lote IN (SELECT DISTINCT id_sms_envio_lote FROM sms_envio_lote WHERE id_sms_plantilla = " + dto.getIdSmsPlantilla() + ")");
                if (ultimoEnviadoPlantilla.length>0)
                    ultimaFechaEnvio = ultimoEnviadoPlantilla[0].getFechaHrEnvio();
            }catch(Exception e){}
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdSmsPlantilla()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getAsunto()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getFechaHrCreado()));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), ultimaFechaEnvio!=null?("" + ultimaFechaEnvio) : null ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + cantidadEnviados ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getIdEstatus()));
           

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  SMS_AGENDA_GRUPOS_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(SmsAgendaGrupo[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(SMS_AGENDA_GRUPOS_REPORT);
        
        for(SmsAgendaGrupo dto : objectDto){
            
            int noIntegrantes = 0;
            try{
                noIntegrantes = new SmsAgendaDestinatarioBO(this.conn).findCantidadSmsAgendaDestinatarios(-1, usuarioBO.getUser().getIdEmpresa(), 0, 0, " AND id_sms_agenda_grupo=" + dto.getIdSmsAgendaGrupo());
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdSmsAgendaGrupo()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getNombreGrupo()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + StringManage.getValidString(dto.getDescripcionGrupo()) ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + noIntegrantes ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getIdEstatus() ));
           

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  SMS_AGENDA_DESTINATARIOS_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(SmsAgendaDestinatario[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(SMS_AGENDA_DESTINATARIOS_REPORT);
        
        SmsAgendaGrupoBO smsAgendaGrupoBO = new SmsAgendaGrupoBO(this.conn);
        for(SmsAgendaDestinatario dto : objectDto){
            String nombreGrupo = "";
            try{
                if (dto.getIdSmsAgendaGrupo()>0){
                    SmsAgendaGrupo grupo = smsAgendaGrupoBO.findSmsAgendaGrupobyId(dto.getIdSmsAgendaGrupo());
                    nombreGrupo = grupo.getNombreGrupo();
                }
            }catch(Exception e){}
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdSmsAgendaDest()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + StringManage.getValidString(dto.getNombre()) ));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getNumeroCelular()));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + StringManage.getValidString(dto.getCampoExtra1()) ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + StringManage.getValidString(dto.getCampoExtra2()) ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + StringManage.getValidString(dto.getCampoExtra3()) ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + StringManage.getValidString(dto.getCampoExtra4()) ));
            hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + nombreGrupo ));
            hashData.put((String)dataInfo.get(8).get("field"), getRealData(dataInfo.get(8), "" + dto.getIdEstatus()));
           

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  COMODATO_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(Comodato[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(COMODATO_REPORT);

        Almacen almacen = null;
        AlmacenDaoImpl almacenDaoImpl = new AlmacenDaoImpl(this.conn);
        String nombreAlamcen = "";
        for(Comodato dto:objectDto){
            almacen = null;
            nombreAlamcen = "";
            try{
                almacen = almacenDaoImpl.findByPrimaryKey(almacen.getIdAlmacen());
                nombreAlamcen = almacen.getNombre();
            }catch(Exception e){}
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdComodato()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + dto.getNombre()));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getNumeroSerie() ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getMarca() ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getModelo() ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getTipo() ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + dto.getCapacidad() ));
            hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + nombreAlamcen ));
            hashData.put((String)dataInfo.get(8).get("field"), getRealData(dataInfo.get(8), "" + (dto.getEstatus()==1?"Comodato":dto.getEstatus()==2?"Disponible":dto.getEstatus()==3?"Inactivo":dto.getEstatus()==4?"Descompuesto":"No Definido" )));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  COMODATO_MANTENIMIENTO_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(ComodatoMantenimiento[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(COMODATO_MANTENIMIENTO_REPORT);

        Cliente cliente = null;
        ClienteDaoImpl clienteDaoImpl = new ClienteDaoImpl(this.conn);
        ComodatoDaoImpl comodatoDaoImpl = new ComodatoDaoImpl(this.conn);
        String nombreCliente = "";
        Comodato comodato = null;
        for(ComodatoMantenimiento dto:objectDto){
            cliente = null;
            nombreCliente = "";
            comodato = null;
            try{
                cliente = clienteDaoImpl.findByPrimaryKey(dto.getIdCliente());
                nombreCliente = cliente.getRazonSocial();
            }catch(Exception e){}
            try{
                comodato = comodatoDaoImpl.findByPrimaryKey(dto.getIdComodato());
            }catch(Exception e){}
            
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdComodatoMantenimiento()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + nombreCliente));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + comodato!=null?comodato.getNombre():"" ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getFechaRealizacionMantenimiento()!=null?DateManage.formatDateToNormal(dto.getFechaRealizacionMantenimiento()):"" ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getTecnico() ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getFechaProxMantenimiento()!=null?DateManage.formatDateToNormal(dto.getFechaProxMantenimiento()):"" ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  COMODATO_CONTRATO_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(Comodato[] objectDto, String comodatoContrato) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(COMODATO_CONTRATO_REPORT);

        Cliente cliente = null;
        ClienteDaoImpl clienteDaoImpl = new ClienteDaoImpl(this.conn);
        String nombreCliente = "";
        for(Comodato dto:objectDto){
            cliente = null;
            nombreCliente = "";
            try{
                cliente = clienteDaoImpl.findByPrimaryKey(dto.getIdCliente());
                nombreCliente = cliente.getRazonSocial();
            }catch(Exception e){}
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdComodato()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + nombreCliente));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + dto.getNombre() ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getFechaAsignacionCliente()!=null?DateManage.formatDateToNormal(dto.getFechaAsignacionCliente()):"" ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + (dto.getEstatus()==1?"Comodato":dto.getEstatus()==2?"Disponible":dto.getEstatus()==3?"Inactivo":dto.getEstatus()==4?"Descompuesto":"No Definido") ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getFechaSubidaContrato()!=null?DateManage.formatDateToNormal(dto.getFechaSubidaContrato()):"" ));
            
            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     *  CR_FORMULARIOS_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(CrFormulario[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(CR_FORMULARIOS_REPORT);
        
        CrGrupoFormularioBO crGrupoFormularioBO = new CrGrupoFormularioBO(this.getConn());
        EmpleadoBO empleadoBO = new EmpleadoBO(this.getConn());
        for(CrFormulario dto : objectDto){
            String nombreGrupoFormulario = " - No asignado - ";
            String nombreEmpleadoEdito = " - ";
            try{
                nombreGrupoFormulario = crGrupoFormularioBO.findCrGrupoFormulariobyId(dto.getIdGrupoFormulario()).getNombre();
            }catch(Exception ex){}
            try{
                Empleado empleadoDto = empleadoBO.findEmpleadoByUsuario(dto.getIdUsuarioEdicion());
                nombreEmpleadoEdito = StringManage.getValidString(empleadoDto.getNombre()) + " " + StringManage.getValidString(empleadoDto.getApellidoPaterno());
            }catch(Exception ex){}
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdFormulario()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + nombreGrupoFormulario));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + (dto.getOrdenGrupo()>0 ? "" + dto.getOrdenGrupo() : "?") ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + StringManage.getValidString(dto.getNombre()) ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + StringManage.getValidString(dto.getDescripcion()) ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getFechaHrCreacion() ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + StringManage.getValidString( DateManage.formatDateTimeToNormalMinutes(dto.getFechaHrUltimaEdicion()) ) + "\n" + nombreEmpleadoEdito ));
            hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + dto.getIdEstatus()));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     * CR_FORMULARIO_VALIDACIONES_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(CrFormularioValidacion[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(CR_FORMULARIO_VALIDACIONES_REPORT);
        
        for(CrFormularioValidacion dto : objectDto){
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdFormularioValidacion()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + StringManage.getValidString(dto.getNombre())));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + StringManage.getValidString(dto.getDescripcion()) ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + StringManage.getValidString(dto.getRegexJava()) ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getIsCreadoSistema() ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getIdEstatus()));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     * CR_GRUPOS_FORMULARIO_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(CrGrupoFormulario[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(CR_GRUPOS_FORMULARIO_REPORT);
        
        CrFormularioBO crFormularioBO = new CrFormularioBO(this.getConn());
        for(CrGrupoFormulario dto : objectDto){
            
            int noFormularios =  crFormularioBO.findCantidadCrFormularios(-1, -1, 0, 0, " AND id_grupo_formulario="+dto.getIdGrupoFormulario());
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdGrupoFormulario()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + StringManage.getValidString(dto.getNombre())));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + StringManage.getValidString(dto.getDescripcion()) ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getFechaHrCreacion() ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + noFormularios ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getIdEstatus()));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     * CR_SCORE_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(CrScore[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(CR_SCORE_REPORT);
        
        EmpleadoBO empleadoBO = new EmpleadoBO(this.getConn());
        for(CrScore dto : objectDto){
            String nombreEmpleadoEdito = " - ";
            try{
                Empleado empleadoDto = empleadoBO.findEmpleadoByUsuario(dto.getIdUsuarioEdicion());
                nombreEmpleadoEdito = StringManage.getValidString(empleadoDto.getNombre()) + " " + StringManage.getValidString(empleadoDto.getApellidoPaterno());
            }catch(Exception ex){}
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdScore() ));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + StringManage.getValidString(dto.getNombre())));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + StringManage.getValidString(dto.getDescripcion()) ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + dto.getFechaHrCreacion() ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + StringManage.getValidString( DateManage.formatDateTimeToNormalMinutes(dto.getFechaHrUltimaEdicion()) ) + "\n" + nombreEmpleadoEdito ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getIdEstatus()));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     * CR_SCORE_DETALLE_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(CrScoreDetalle[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(CR_SCORE_DETALLE_REPORT);
        
        CrGrupoFormularioBO crGrupoFormularioBO = new CrGrupoFormularioBO(this.getConn());
        CrFormularioBO crFormularioBO = new CrFormularioBO(this.getConn());
        CrFormularioCampoBO crFormularioCampoBO = new CrFormularioCampoBO(this.getConn());
        for(CrScoreDetalle dto : objectDto){
            try{
                String nombreFormulario;
                String nombreCampo;
                String criterio = " - ";

                CrFormularioCampo crFormularioCampo = crFormularioCampoBO.findCrFormularioCampobyId(dto.getIdFormularioCampo());
                CrFormulario crFormulario = crFormularioBO.findCrFormulariobyId(crFormularioCampo.getIdFormulario());
                CrGrupoFormulario crGrupoFormulario = crGrupoFormularioBO.findCrGrupoFormulariobyId(crFormulario.getIdGrupoFormulario());

                nombreFormulario = crFormulario.getNombre() + "\n" + crGrupoFormulario.getNombre() + "";
                nombreCampo = crFormularioCampo.getEtiqueta();
                if (StringManage.getValidString(dto.getValorExacto()).length()>0){
                    criterio = " = '" + dto.getValorExacto() + "'";
                }else if (dto.getRangoMin()>0 || dto.getRangoMax()>0){
                    if (dto.getRangoMin()>0 && dto.getRangoMax()>0){
                        criterio = dto.getRangoMin() + " - " + dto.getRangoMax();
                    }else if (dto.getRangoMin()>0 && dto.getRangoMax()==0){
                        criterio = " >= " + dto.getRangoMin();
                    }else if (dto.getRangoMin()==0 && dto.getRangoMax()>0){
                        criterio = " <= " + dto.getRangoMax();
                    }
                }

                hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdScoreDetalle()));
                hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + nombreFormulario ));
                hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + nombreCampo ));
                hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + criterio ));
                hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + dto.getPuntosScore() ));
                hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getIdEstatus()));

                dataList.add(hashData);

                hashData = new HashMap<String, String>();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        return dataList;
    }
    
    /**
     * CR_PRODUCTO_CREDITO_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(CrProductoCredito[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(CR_PRODUCTO_CREDITO_REPORT);
        
        EmpleadoBO empleadoBO = new EmpleadoBO(this.getConn());
        CrGrupoFormularioBO crGrupoFormularioBO = new CrGrupoFormularioBO(this.getConn());
        CrProductoCreditoBO crProductoCreditoBO = new CrProductoCreditoBO(this.getConn());
        CrScoreBO crScoreBO = new CrScoreBO(this.getConn());
        for(CrProductoCredito dto : objectDto){
            String nombreEmpleadoEdito = " - ";
            String nombreProductoPadre = " - ";
            String formularioSolicitud = " - ";
            String formularioVerificación = " - ";
            String score = " - ";
            String nombresImprimibles = " ";
            try{
                Empleado empleadoDto = empleadoBO.findEmpleadoByUsuario(dto.getIdUsuarioEdicion());
                nombreEmpleadoEdito = StringManage.getValidString(empleadoDto.getNombre()) + " " + StringManage.getValidString(empleadoDto.getApellidoPaterno());
            }catch(Exception ex){}
            if (dto.getIdProductoCreditoPadre()>0){
                try{
                    CrProductoCredito productoCreditoPadre = crProductoCreditoBO.findCrProductoCreditobyId(dto.getIdProductoCreditoPadre());
                    nombreProductoPadre = productoCreditoPadre.getNombre();
                }catch(Exception ex){}
            }
            if (dto.getIdGrupoFormularioSolic()>0){
                try{
                    CrGrupoFormulario grupoFormulario = crGrupoFormularioBO.findCrGrupoFormulariobyId(dto.getIdGrupoFormularioSolic());
                    formularioSolicitud = grupoFormulario.getNombre();
                }catch(Exception ex){}
            }
            if (dto.getIdGrupoFormularioVerif()>0){
                try{
                    CrGrupoFormulario grupoFormulario = crGrupoFormularioBO.findCrGrupoFormulariobyId(dto.getIdGrupoFormularioVerif());
                    formularioVerificación = grupoFormulario.getNombre();
                }catch(Exception ex){}
            }
            if (dto.getIdScore()>0){
                try{
                    CrScore crScore = crScoreBO.findCrScorebyId(dto.getIdScore());
                    score = crScore.getNombre();
                }catch(Exception ex){}
            }
            crProductoCreditoBO.setCrProductoCredito(dto);
            CrDocImprimible[] docImprimibles = crProductoCreditoBO.consultaImprimiblesAsociados();
            if (docImprimibles.length>0){
                for (CrDocImprimible docImprimible : docImprimibles){
                    nombresImprimibles += ""+docImprimible.getNombre() + "\n";
                }
            }
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdProductoCredito()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + nombreProductoPadre ));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + StringManage.getValidString(dto.getNombre()) ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + "Solicitud: " + formularioSolicitud + "\nVerificación:" + formularioVerificación ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + score ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + nombresImprimibles ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + dto.getFechaHrCreacion() ));
            hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + StringManage.getValidString( DateManage.formatDateTimeToNormalMinutes(dto.getFechaHrUltimaEdicion()) ) + "\n" + nombreEmpleadoEdito ));
            hashData.put((String)dataInfo.get(8).get("field"), getRealData(dataInfo.get(8), "" + dto.getIdEstatus() ));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     * CR_DOC_IMPRIMIBLE_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(CrDocImprimible[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(CR_DOC_IMPRIMIBLE_REPORT);
        
        EmpleadoBO empleadoBO = new EmpleadoBO(this.getConn());
        for(CrDocImprimible dto : objectDto){
            String nombreEmpleadoEdito = " - ";
            try{
                Empleado empleadoDto = empleadoBO.findEmpleadoByUsuario(dto.getIdUsuarioEdicion());
                nombreEmpleadoEdito = StringManage.getValidString(empleadoDto.getNombre()) + " " + StringManage.getValidString(empleadoDto.getApellidoPaterno());
            }catch(Exception ex){}
            
            hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdDocImprimible()));
            hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + StringManage.getValidString(dto.getNombre())));
            hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + StringManage.getValidString(dto.getDescripcion()) ));
            hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + StringManage.getValidString(dto.getTipoImprimible()) ));
            hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + StringManage.getValidString(dto.getNombreArchivoJasper()) ));
            hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getFechaHrCreacion() ));
            hashData.put((String)dataInfo.get(6).get("field"), getRealData(dataInfo.get(6), "" + StringManage.getValidString( DateManage.formatDateTimeToNormalMinutes(dto.getFechaHrUltimaEdicion()) ) + "\n" + nombreEmpleadoEdito ));
            hashData.put((String)dataInfo.get(7).get("field"), getRealData(dataInfo.get(7), "" + dto.getIdEstatus()));

            dataList.add(hashData);

            hashData = new HashMap<String, String>();
        }

        return dataList;
    }
    
    /**
     * CR_DOC_IMP_PARAMETRO_REPORT
     * @param objectDto Arreglo de objetos tipo DTO para fabricar reporte.
     * @return ArrayList<HashMap> con todos los datos para el reporte.
     */
    private ArrayList<HashMap> getDataList(CrDocImpParametro[] objectDto) {
        ArrayList<HashMap> dataList = new ArrayList<HashMap>();
        HashMap<String,String> hashData = new HashMap<String, String>();
        ArrayList<HashMap> dataInfo = getFieldList(CR_DOC_IMP_PARAMETRO_REPORT);
        
        for(CrDocImpParametro dto : objectDto){
            try{
                hashData.put((String)dataInfo.get(0).get("field"), getRealData(dataInfo.get(0), "" + dto.getIdDocImpParametro()));
                hashData.put((String)dataInfo.get(1).get("field"), getRealData(dataInfo.get(1), "" + StringManage.getValidString(dto.getParametroClave()) ));
                hashData.put((String)dataInfo.get(2).get("field"), getRealData(dataInfo.get(2), "" + StringManage.getValidString(dto.getDescripcion()) ));
                hashData.put((String)dataInfo.get(3).get("field"), getRealData(dataInfo.get(3), "" + StringManage.getValidString(dto.getValorDefecto()) ));
                hashData.put((String)dataInfo.get(4).get("field"), getRealData(dataInfo.get(4), "" + StringManage.getValidString(dto.getAsociaVariableFormula()) ));
                hashData.put((String)dataInfo.get(5).get("field"), getRealData(dataInfo.get(5), "" + dto.getIdEstatus()));

                dataList.add(hashData);

                hashData = new HashMap<String, String>();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        return dataList;
    }
    
}
