/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.PretoModulo;
import com.tsp.sct.dao.jdbc.PretoModuloDaoImpl;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class PretoModuloBeanBO {
    
    public PretoModulo inicio = null;
    public PretoModulo admin = null;
    public PretoModulo adminPerfil = null;
    public PretoModulo adminiCertificado = null;
    public PretoModulo adminiLogo = null;
    public PretoModulo adminLicencia = null;
    public PretoModulo adminCuenta = null;
    public PretoModulo adminUsuario = null;
    
    public PretoModulo adminSucursal = null;
    public PretoModulo adminFolio = null;
    public PretoModulo adminMovil = null;
    public PretoModulo adminCampoCliente = null;
    public PretoModulo cat = null;
    public PretoModulo catCliente = null;
    public PretoModulo catCliCategoria = null;
    public PretoModulo catImpuesto = null;
    public PretoModulo catProveedor = null;
    public PretoModulo catProspecto = null;
    public PretoModulo catBanco = null;
    public PretoModulo almacen = null;
    public PretoModulo almacenAlmacen = null;
    public PretoModulo almacenCategoria = null;
    public PretoModulo almacenEmbalaje = null;
    public PretoModulo almacenMarca = null;
    public PretoModulo almacenMovimiento = null;
    public PretoModulo almacenProducto = null;
    public PretoModulo almacenPaquete = null;
    public PretoModulo almacenServicio = null;
    public PretoModulo almacenInventario = null;
    public PretoModulo almacenProDesMovil = null;
    public PretoModulo venta = null;
    public PretoModulo ventaPedido = null;
    public PretoModulo ventaCobranza = null;
    public PretoModulo ventaProVendido = null;
    public PretoModulo ventaCotizacion = null;
    public PretoModulo ventaCorCaja = null;
    public PretoModulo ventaArqCaja = null;
    public PretoModulo ventaDepoBancario = null;
    public PretoModulo ventaGesDevCam = null;
    public PretoModulo ventaGesVenDevCam = null;
    public PretoModulo facturacion = null;
    public PretoModulo facturacionFactura = null;
    public PretoModulo facturacionExpress = null;
    public PretoModulo facturacionNcredito = null;
    public PretoModulo facturacionNdebito = null;
    public PretoModulo facturacionSar = null;
    public PretoModulo facturacionSarConfig = null;
    public PretoModulo facturacionSarFacSar = null;
    public PretoModulo finanza = null;
    public PretoModulo finanzaDashboard = null;
    public PretoModulo finanzaCxcobrar = null;
    public PretoModulo finanzaCxPagar = null;
    public PretoModulo pretoMov = null;
    public PretoModulo pretoMovLocalizacion = null;
    public PretoModulo pretoMovDispositivo = null;
    public PretoModulo pretoMovEmpleado = null;
    public PretoModulo pretoMovClienteAsignado = null;
    public PretoModulo pretoMovMensaje = null;
    public PretoModulo pretoMovZonas = null;
    public PretoModulo pretoMovSeguimiento = null;
    public PretoModulo pretoMovVisorMapa = null;
    public PretoModulo pretoMovLogistica = null;
    public PretoModulo pretoMovRutaEmpleado = null;
    public PretoModulo pretoMovGeocerca = null;
    public PretoModulo pretoMovVisita = null;
    public PretoModulo reporte = null;
    public PretoModulo reporteGeneral = null;
    public PretoModulo reporteConfig = null;
    public PretoModulo nomina = null;
    public PretoModulo nominaRegistroPatron = null;
    public PretoModulo nominaDepartamento = null;
    public PretoModulo nominaPuesto = null;
    public PretoModulo nominaJornada = null;
    public PretoModulo nominaContrato = null;
    public PretoModulo nominaPeriodicidad = null;
    public PretoModulo nominaEmpleado = null;
    public PretoModulo nominaPercepcion = null;
    public PretoModulo nominaDeduccion = null;
    public PretoModulo nominaCFDI = null;
    public PretoModulo validacionXML = null;    
    public PretoModulo pretoMovGastos = null;
    public PretoModulo ventaReferencias = null;
    public PretoModulo adminExcel = null;
    public PretoModulo ventaMetasVenta = null;
    public PretoModulo catGastos = null;
    public PretoModulo catHorarios = null;
    public PretoModulo adminFolioMovil = null;
    public PretoModulo pretoMovCronometro = null;
    public PretoModulo pretoMovConQPay = null;
    public PretoModulo callCenter = null;
    public PretoModulo smsPlantillas = null;
    public PretoModulo smsAgenda = null;
    public PretoModulo smsEnvio = null;
    public PretoModulo smsConfig = null;
    public PretoModulo smsCompra = null;
    public PretoModulo crFrmModulo = null;
    public PretoModulo crFrmGrupo = null;
    public PretoModulo crFrmFormulario = null;
    public PretoModulo crFrmValidacion = null;
    public PretoModulo crScore = null;
    public PretoModulo crProductoCredito = null;
    public PretoModulo crImprimible = null;
    public PretoModulo crCtrlControl = null;
    public PretoModulo crCtrlSolicitud = null;
    public PretoModulo crCobCobranza = null;
    public PretoModulo crCobClientes = null;
    
    //RECORDAR RESTARLE -1 AL INDEX A RECUPERAR, YA QUE SE CARGAN DESDE EL ELEMENTO CERO EN EL ARRAY LIST
    
    public void cargaModulos(Connection conn){
        
        PretoModulo[] modulos = null;
        try{
            modulos = new PretoModuloDaoImpl(conn).findAll();
            List<PretoModulo> modul = new ArrayList<PretoModulo>();    
            for(PretoModulo m : modulos){
                modul.add(m);
            }
            inicio = modul.get(0); 
            admin = modul.get(1);
            adminPerfil = modul.get(2);
            adminiCertificado = modul.get(3);
            adminiLogo = modul.get(4);
            adminLicencia = modul.get(5);
            adminCuenta = modul.get(6);
            adminUsuario = modul.get(7);            
            adminSucursal = modul.get(8);
            adminFolio = modul.get(9);
            adminMovil = modul.get(10);
            adminCampoCliente = modul.get(11);
            cat = modul.get(12);
            catCliente = modul.get(13);
            catCliCategoria = modul.get(14);
            catImpuesto = modul.get(15);
            catProveedor = modul.get(16);
            catProspecto = modul.get(17);
            catBanco = modul.get(18);
            almacen = modul.get(19);
            almacenAlmacen = modul.get(20);
            almacenCategoria = modul.get(21);
            almacenEmbalaje = modul.get(22);
            almacenMarca = modul.get(23);
            almacenMovimiento = modul.get(24);
            almacenProducto = modul.get(25);
            almacenPaquete = modul.get(26);
            almacenServicio = modul.get(27);
            almacenInventario = modul.get(28);
            almacenProDesMovil = modul.get(29);
            venta = modul.get(30);
            ventaPedido = modul.get(31);
            ventaCobranza = modul.get(32);
            ventaProVendido = modul.get(33);
            ventaCotizacion = modul.get(34);
            ventaCorCaja = modul.get(35);
            ventaArqCaja = modul.get(36);
            ventaDepoBancario = modul.get(37);
            ventaGesDevCam = modul.get(38);
            ventaGesVenDevCam = modul.get(39);
            facturacion = modul.get(40);
            facturacionFactura = modul.get(41);
            facturacionExpress = modul.get(42);
            facturacionNcredito = modul.get(43);
            facturacionNdebito = modul.get(44);
            facturacionSar = modul.get(45);
            facturacionSarConfig = modul.get(46);
            facturacionSarFacSar = modul.get(47);
            finanza = modul.get(48);
            finanzaDashboard = modul.get(49);
            finanzaCxcobrar = modul.get(50);
            finanzaCxPagar = modul.get(51);
            pretoMov = modul.get(52);
            pretoMovLocalizacion = modul.get(53);
            pretoMovDispositivo = modul.get(54);
            pretoMovEmpleado = modul.get(55);
            pretoMovClienteAsignado = modul.get(56);
            pretoMovMensaje = modul.get(57);
            pretoMovZonas = modul.get(58);
            pretoMovSeguimiento = modul.get(59);
            pretoMovVisorMapa = modul.get(60);
            pretoMovLogistica = modul.get(61);
            pretoMovRutaEmpleado = modul.get(62);
            pretoMovGeocerca = modul.get(63);
            pretoMovVisita = modul.get(64);
            reporte = modul.get(65);
            reporteGeneral = modul.get(66);
            reporteConfig = modul.get(67);
            nomina = modul.get(68);
            nominaRegistroPatron = modul.get(69);
            nominaDepartamento = modul.get(70);
            nominaPuesto = modul.get(71);
            nominaJornada = modul.get(72);
            nominaContrato = modul.get(73);
            nominaPeriodicidad = modul.get(74);
            nominaEmpleado = modul.get(75);
            nominaPercepcion = modul.get(76);
            nominaDeduccion = modul.get(77);
            nominaCFDI = modul.get(78);
            validacionXML = modul.get(79);
            pretoMovGastos = modul.get(80);
            ventaReferencias = modul.get(81);
            adminExcel = modul.get(82);
            ventaMetasVenta = modul.get(83);
            catGastos =  modul.get(84);
            catHorarios =  modul.get(85);
            adminFolioMovil =  modul.get(86);
            pretoMovCronometro =  modul.get(87);
            pretoMovConQPay = modul.get(88);
            callCenter = modul.get(89);
            
            smsPlantillas = modul.get(91);
            smsAgenda = modul.get(92);
            smsEnvio = modul.get(93);
            smsConfig = modul.get(94);
            smsCompra = modul.get(95);
            
            crFrmModulo = modul.get(97);
            crFrmGrupo = modul.get(98);
            crFrmFormulario = modul.get(99);
            crFrmValidacion = modul.get(100);
            crScore = modul.get(101);
            crProductoCredito = modul.get(102);
            crImprimible = modul.get(103);
            crCtrlControl = modul.get(104);
            crCtrlSolicitud = modul.get(105);
            crCobCobranza = modul.get(106);
            crCobClientes = modul.get(107);
        }catch(Exception e){}
        
    }
    
}
