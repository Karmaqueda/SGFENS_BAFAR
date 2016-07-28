/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.cr;

import com.sap.bafar.ws.aceptacontrato.ZfmtrAcceptContract;
import com.sap.bafar.ws.aceptacontrato.ZfmtrAcceptContractResponse;
import com.sap.bafar.ws.consultadispersion.TableOfZcmlOrdPago;
import com.sap.bafar.ws.consultadispersion.ZcmlOrdPago;
import com.sap.bafar.ws.consultadispersion.ZfmtrDisper;
import com.sap.bafar.ws.consultadispersion.ZfmtrDisperResponse;
import com.sap.bafar.ws.creacontrato.*;
import com.sap.bafar.ws.validainterlocutor.ZfmtrValidaInterlocu;
import com.sap.bafar.ws.validainterlocutor.ZfmtrValidaInterlocuResponse;
import com.tsp.sct.bo.CrFormularioBO;
import com.tsp.sct.bo.CrFormularioCampoBO;
import com.tsp.sct.bo.CrFormularioEventoBO;
import com.tsp.sct.bo.CrFormularioRespuestaBO;
import com.tsp.sct.bo.CrFrmEventoSolicitudBO;
import com.tsp.sct.bo.ZonaHorariaBO;
import com.tsp.sct.config.Configuration;
import com.tsp.sct.dao.dto.CrFormulario;
import com.tsp.sct.dao.dto.CrFormularioCampo;
import com.tsp.sct.dao.dto.CrFormularioEvento;
import com.tsp.sct.dao.dto.CrFormularioRespuesta;
import com.tsp.sct.dao.dto.CrFrmEventoSolicitud;
import com.tsp.sct.dao.dto.CrProductoCredito;
import com.tsp.sct.dao.dto.CrSolicitudBitacora;
import com.tsp.sct.dao.jdbc.CrProductoCreditoDaoImpl;
import com.tsp.sct.util.Converter;
import com.tsp.sct.util.DateManage;
import com.tsp.sct.util.GenericMethods;
import com.tsp.sct.util.StringManage;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author ISCesar
 */
public class CrUtilConectaSAPBafar {
    
    private Connection conn =  null;
    
    private HashMap<String, String> respuestasSolicitud = new HashMap<String, String>();
    
    private final static String SAP_SOCIEDAD = "1018";

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public HashMap<String, String> getRespuestasSolicitud() {
        return respuestasSolicitud;
    }

    public void setRespuestasSolicitud(HashMap<String, String> respuestasSolicitud) {
        this.respuestasSolicitud = respuestasSolicitud;
    }

    public CrUtilConectaSAPBafar(Connection conn) {
        this.conn = conn;
    }    

// <editor-fold defaultstate="collapsed" desc="Métodos para Analisis de Solicitud y extraer informacion de ella"> 
    public HashMap<String, String> analizaSolicitud(int idFrmEventoSolicitud) throws Exception{
        CrFrmEventoSolicitudBO crFrmEventoSolicitudBO = new CrFrmEventoSolicitudBO(idFrmEventoSolicitud, conn);
        return analizaSolicitud(crFrmEventoSolicitudBO.getCrFrmEventoSolicitud());
    }
    
    public HashMap<String, String> analizaSolicitud(CrFrmEventoSolicitud crFrmEventoSolicitud) throws Exception{
        CrFormularioEventoBO crFormularioEventoBO = new CrFormularioEventoBO(this.conn);
        CrFormularioEvento crFormularioEvento = crFormularioEventoBO.findCrFormularioEventos(-1, 0, 0, 0, " AND id_formulario_evento=" + crFrmEventoSolicitud.getIdFormularioEvento())[0];
        return analizaSolicitud(crFormularioEvento);
    }
    
    public HashMap<String, String> analizaSolicitud(CrFormularioEvento crFormularioEvento)throws Exception {
        respuestasSolicitud = new HashMap<String, String>();
        
        int idEmpresa = crFormularioEvento.getIdEmpresa();
        CrFormularioEventoBO crFormularioEventoBO = new CrFormularioEventoBO(this.conn);
        crFormularioEventoBO.setCrFormularioEvento(crFormularioEvento);
        
        CrFormularioCampoBO crFormularioCampoBO = new CrFormularioCampoBO(this.conn);
        CrFormularioRespuestaBO crFormularioRespuestaBO = new CrFormularioRespuestaBO(this.conn);
        
        CrFormularioBO crFormularioBO = new CrFormularioBO(conn);
        CrFormulario[] formulariosSolicitud = crFormularioBO.findCrFormularios(-1, idEmpresa, 0, 0, " AND id_grupo_formulario=" + crFormularioEvento.getIdGrupoFormulario());
        
        // for - formularios
        for (CrFormulario formulario : formulariosSolicitud){
            // for - campos formulario
            CrFormularioCampo[] crFormularioCampos = crFormularioCampoBO.findCrFormularioCampos(-1, idEmpresa, 0, 0, " ANd id_formulario = " + formulario.getIdFormulario());
            for (CrFormularioCampo campo : crFormularioCampos){
                String varNombre = campo.getVariableFormula();
                String varValor = "";
                
                // respuesta asociada a evento y campo
                CrFormularioRespuesta[] frmRespuesta = crFormularioRespuestaBO.findCrFormularioRespuestas(-1, idEmpresa, 0, 0, " AND id_formulario_evento=" + crFormularioEvento.getIdFormularioEvento() + " AND id_formulario_campo=" + campo.getIdFormularioCampo());
                if (frmRespuesta.length>0){
                    varValor = frmRespuesta[0].getValor();
                }
                
                respuestasSolicitud.put(varNombre, varValor);
            }
        }
        
        
        return respuestasSolicitud;
    }    

     
    public String getValorVariable(String variable, int maxLength, boolean nuloSiVacio, boolean ceroSiVacio){
        //String valor = respuestasSolicitud.getOrDefault(variable, "");
        String valor = StringManage.getValidString( respuestasSolicitud.get(variable) );
        if (maxLength>0){
            valor = StringManage.getStringMaxLength(valor, maxLength);
        }
        valor = StringManage.getValidString(valor); //quitamos espacios en blanco al final
        valor = valor.toUpperCase();
        if (ceroSiVacio && valor.length()==0)
            valor = "0";
        if (nuloSiVacio && valor.length()==0)
            valor = null;
        return valor;
    }
    
    public String getValorVariableFecha(String variable, String formatoSalida){
        String aux = getValorVariable(variable, 0, false, false);
        
        String valor = "";
        Date fecha = DateManage.parseDate(aux, "yyyy-MM-dd");
        if (fecha!=null){
            valor = DateManage.formatDate(fecha, formatoSalida);
        }
        
        return valor;
    }
    
    public String getValorVariableDecimal(String variable, int maxLength){
        String aux = getValorVariable(variable, maxLength, false, true);
        
        String valor = aux;
        try{
            valor = "" + Converter.stringToDoubleFormatMexico(aux);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return valor;
    }
    
    public String getValorVariableEntero(String variable, int maxLength){
        String aux = getValorVariable(variable, maxLength, false, true);
        
        String valor = aux;
        try{
            valor = "" + (new Double(Converter.stringToDoubleFormatMexico(aux)).intValue());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return valor;
    }
    
    public String getValorVariableBooleano(String variable, String valorVerdadero, String valorFalso, String resultadoVerdadero, String resultadoFalso){
        String aux = getValorVariable(variable, 0, false, false);
        
        String valor = aux;
        if (aux.equalsIgnoreCase(valorVerdadero)){
            valor = resultadoVerdadero;
        }else if (aux.equalsIgnoreCase(valorFalso)){
            valor = resultadoFalso;
        }
        
        return valor;
    }
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Métodos para WS Crea Interlocutor-Contrato"> 
    // <editor-fold defaultstate="collapsed" desc="Métodos Parse datos Request WS Crea Interlocutor-Contrato"> 
    private ZstAvalPerson parseAvalPerson(){
        // Aval
        ZstAvalPerson iAval = new ZstAvalPerson();
        iAval.setFechaNac(getValorVariableFecha("AVAL_FECHA_NAC", "yyyy-MM-dd"));
        iAval.setRfc(getValorVariable("AVAL_RFC", 15, false, false));
        iAval.setApPat(getValorVariable("AVAL_APELLIDO_PAT", 40, false, false));
        iAval.setApMat(getValorVariable("AVAL_APELLIDO_MAT", 40, false, false));
        iAval.setNombre1(getValorVariable("AVAL_NOMBRE_PRIMER", 40, false, false));
        iAval.setNobre2(getValorVariable("AVAL_NOMBRE_SEGUNDO", 40, false, false));
        iAval.setRelacion(getValorVariable("AVAL_RELACION", 3, false, false));
        iAval.setSexo(getValorVariable("AVAL_SEXO", 10, false, false));
        iAval.setIdentif(getValorVariable("AVAL_IDE_TIPO", 3, false, false));
        iAval.setNoIden(getValorVariable("AVAL_IDE_VALOR", 30, false, false));
        iAval.setCelular(getValorVariable("AVAL_CELULAR", 10, false, false));
        iAval.setCalle(getValorVariable("AVAL_DOM_CALLE", 40, false, false));
        iAval.setNoExt(getValorVariable("AVAL_DOM_NUM_EXT", 10, false, false));
        iAval.setNoInt(getValorVariable("AVAL_DOM_NUM_INT", 5, false, false));
        iAval.setColonia(getValorVariable("AVAL_DOM_COLONIA", 40, false, false));
        iAval.setPais(getValorVariable("AVAL_DOM_PAIS", 2, false, false)); // "MX"
        iAval.setCiudad(getValorVariable("AVAL_DOM_CIUDAD", 40, false, false));
        iAval.setLocalid(getValorVariable("AVAL_DOM_MUNICIPIO", 40, false, false));
        iAval.setEstado(getValorVariable("AVAL_DOM_ESTADO", 3, false, false)); // "DF"
        iAval.setCp(getValorVariable("AVAL_DOM_CP", 5, false, false));
        
        return iAval;
    }
    
    private ZstConyPerson parseConyuguePersona(){
        ZstConyPerson iCony = new ZstConyPerson();
        
        iCony.setNombre1(getValorVariable("CNY_NOMBRE_A", 40, false, false));
        iCony.setNombre2(getValorVariable("CNY_NOMBRE_B", 40, false, false));
        iCony.setApMat(getValorVariable("CNY_AP_MATERNO", 40, false, false));
        iCony.setApPat(getValorVariable("CNY_AP_PATERNO", 40, false, false));
        iCony.setEmpresa(getValorVariable("CNY_LAB_NOMBRE", 40, false, false));
        iCony.setPuesto(getValorVariable("CNY_LAB_PUESTO", 40, false, false));
        iCony.setAntig(getValorVariableEntero("CNY_LAB_ANTIGUEDAD", 3));
        iCony.setSueldo(getValorVariableDecimal("CNY_LAB_SUELDO", 15));
        iCony.setCalleEmpr(getValorVariable("CNY_LAB_DOM_CALLE", 40, false, false));
        iCony.setNoExt(getValorVariable("CNY_LAB_DOM_NO_EXT", 10, false, false));
        iCony.setNoInt(getValorVariable("CNY_LAB_DOM_NO_INT", 5, false, false));
        iCony.setColonia(getValorVariable("CNY_LAB_DOM_COLONIA", 40, false, false));
        iCony.setCp(getValorVariable("CNY_LAB_DOM_CP", 5, false, false));
        iCony.setCiudad(getValorVariable("CNY_LAB_DOM_CIUDAD", 40, false, false));
        iCony.setEstado(getValorVariable("CNY_LAB_DOM_ESTADO", 3, false, false));
        iCony.setTelOf(getValorVariable("CNY_LAB_TELEFONO", 10, false, false));
        iCony.setCelular(getValorVariable("CNY_CELULAR", 10, false, false));
        
        return iCony;
    }
    
    private ZstDatosPerson parseDatosPersona(){
        ZstDatosPerson iDatos = new ZstDatosPerson();
        
        iDatos.setRfc(getValorVariable("SOL_RFC", 15, false, false));
        iDatos.setCurp(getValorVariable("SOL_CURP", 20, false, false));
        iDatos.setFechaNac(getValorVariableFecha("SOL_FECHA_NAC", "yyyy-MM-dd"));
        iDatos.setDepend(getValorVariableEntero("SOL_DEP_NO", 40));
        iDatos.setEdadDepen(getValorVariable("SOL_DEP_EDADES", 40, false, false));
        iDatos.setRedSocial(getValorVariable("SOL_REDES_SOCIALES", 20, false, false));
        iDatos.setTipoViv(getValorVariable("SOL_VIVIENDA_TIPO", 3, false, false));
        iDatos.setValViv(getValorVariable("SOL_VIVIENDA_VALOR", 15, false, true));
        iDatos.setRenta(getValorVariable("SOL_VIVIENDA_RENTA", 15, false, true));
        iDatos.setTiempRes(getValorVariableEntero("SOL_DOM_ANTIGUEDAD", 3));
        iDatos.setEmpresa(getValorVariable("LAB_EMPRESA", 40, false, false));
        iDatos.setTipoEmpr(getValorVariable("LAB_TIPO_EMPRESA", 3, false, false));
        iDatos.setRelEmp(getValorVariable("LAB_RELACION", 3, false, false));
        iDatos.setContr(getValorVariable("LAB_TIPO_CONTRATO", 3, false, false));
        iDatos.setSueldo(getValorVariableDecimal("LAB_SUELDO_NETO", 15));
        iDatos.setFrecPag(getValorVariable("LAB_FRECUENCIA_PAGO", 3, false, false));
        iDatos.setFechIngr(getValorVariableFecha("LAB_FECHA_INGRESO", "yyyy-MM-dd"));
        iDatos.setAntig(getValorVariableEntero("LAB_ANTIGUEDAD", 2));
        iDatos.setFormPag(getValorVariable("LAB_FORMA_PAGO", 2, false, false));
        iDatos.setPuesto(getValorVariable("LAB_PUESTO", 40, false, false));
        iDatos.setTelTrab(getValorVariable("LAB_TELEFONO", 10, false, false));
        iDatos.setExt(getValorVariable("LAB_TELEFONO_EXT", 5, false, false));
        iDatos.setTelAlt(getValorVariable("LAB_TELEFONO_ALT", 10, false, false));
        iDatos.setMailTrab(getValorVariable("LAB_CORREO", 40, false, false));
        iDatos.setCalleTrab(getValorVariable("LAB_DOM_CALLE", 40, false, false));
        iDatos.setNoExt(getValorVariable("LAB_DOM_NUM_EXT", 10, false, false));
        iDatos.setNoInt(getValorVariable("LAB_DOM_NUM_INT", 5, false, false));
        iDatos.setColonia(getValorVariable("LAB_DOM_COLONIA", 40, false, false));
        iDatos.setCp(getValorVariable("LAB_DOM_CP", 5, false, false));
        iDatos.setCiudad(getValorVariable("LAB_DOM_CIUDAD", 40, false, false));
        iDatos.setEstado(getValorVariable("LAB_DOM_ESTADO", 3, false, false));
        iDatos.setNombreJefe(getValorVariable("LAB_NOMBRE_JEFE", 40, false, false));
        iDatos.setPuestoJefe(getValorVariable("LAB_PUESTO_JEFE", 30, false, false));
        iDatos.setTelOfic(getValorVariable("LAB_TELEFONO_OFICINA", 10, false, false));
        iDatos.setSexo(getValorVariable("SOL_SEXO", 10, false, false));
        iDatos.setEcivil(getValorVariable("SOL_EDO_CIVIL", 1, false, false));
        iDatos.setNacion(getValorVariable("SOL_NACIONALIDAD", 2, false, false));//"MX");
        iDatos.setProf(getValorVariable("LAB_PROFESION", 4, false, false));
        iDatos.setLugnac(getValorVariable("SOL_LUGAR_NAC", 50, false, false));
        iDatos.setPais(getValorVariable("SOL_DOM_PAIS", 2, false, false));//"MX");
        iDatos.setPersf("X");//getValorVariable("-", 1, false, false)); // - Persona Fisica siempre "X"
        
        return iDatos;
    }
    
    private ZstDirecPerson parseDirecPersona(){
        ZstDirecPerson iDirec = new ZstDirecPerson();
        
        iDirec.setNombre(getValorVariable("SOL_NOMBRE_PRIMER", 50, false, false));
        iDirec.setNombre2(getValorVariable("SOL_NOMBRE_SEGUNDO", 50, false, false));
        iDirec.setApellidop(getValorVariable("SOL_APELLIDO_PAT", 50, false, false));
        iDirec.setApellidom(getValorVariable("SOL_APELLIDO_MAT", 50, false, false));
        
        iDirec.setCalle(getValorVariable("SOL_DOM_CALLE", 50, false, false));
        iDirec.setNumero(getValorVariable("SOL_DOM_NUM_EXT", 10, false, false));
        iDirec.setCp(getValorVariable("SOL_DOM_CP", 5, false, false));
        iDirec.setPoblac(getValorVariable("SOL_DOM_CIUDAD", 50, false, false));
        iDirec.setPais(getValorVariable("SOL_DOM_PAIS", 2, false, false));//"MX");
        iDirec.setRegion(getValorVariable("SOL_DOM_ESTADO", 3, false, false));
        iDirec.setTelef(getValorVariable("SOL_CELULAR", 15, false, false));
        iDirec.setExt(getValorVariable("FALTA", 5, false, false));
        iDirec.setEmail(getValorVariable("SOL_EMAIL", 50, false, false));
        iDirec.setCelular(getValorVariable("SOL_CELULAR", 15, false, false));
        
        return iDirec;
    }
    
    private ZstGastoPerson parseGastoPerson(){
        ZstGastoPerson iGasto = new ZstGastoPerson();
        
        iGasto.setAliment(getValorVariableDecimal("GFAM_ALIMENTACION", 15));
        iGasto.setRenta(getValorVariableDecimal("GFAM_RENTA", 15));
        iGasto.setServicios(getValorVariableDecimal("GFAM_SERVICIOS_PUB", 15));
        iGasto.setSalud(getValorVariableDecimal("GFAM_SALUD", 15));
        iGasto.setEducac(getValorVariableDecimal("GFAM_EDUCACION", 15));
        iGasto.setDeudas(getValorVariableDecimal("GFAM_PAGO_DEUDAS", 15));
        iGasto.setOtros(getValorVariableDecimal("GFAM_OTROS", 15));
        
        return iGasto;
    }
    
    private ZstIngresoPerson parseIngresoPerson(){
        ZstIngresoPerson iIngreso = new ZstIngresoPerson();
        
        iIngreso.setTitular(getValorVariableDecimal("ING_TITULAR", 15));
        iIngreso.setFamiliar(getValorVariableDecimal("ING_FAMILIAR", 15));
        iIngreso.setConyugue(getValorVariableDecimal("ING_CONYUGUE", 15));
        
        return iIngreso;
    }
    
    private ZstLiquidezPerson parseLiquidezPerson(){
        ZstLiquidezPerson iLiquidez = new ZstLiquidezPerson();
        
        iLiquidez.setGastoFam(getValorVariableDecimal("GFAM_TOTAL_EGRESOS", 15));
        iLiquidez.setIngresoFam(getValorVariableDecimal("GFAM_TOTAL_INGRESOS", 15));
        iLiquidez.setGranTot(getValorVariableDecimal("GFAM_INGRESOS_MENOS_EGRESOS", 15));
        iLiquidez.setMontoSolic(getValorVariableDecimal("GFAM_MONTO_SOLICITADO", 15));
        iLiquidez.setPlazoProp(getValorVariableEntero("LIQ_PLAZO_PROP", 3));
        iLiquidez.setMontoAut(getValorVariableDecimal("LIQ_MONTO_AUT", 15));
        iLiquidez.setPlazoAut(getValorVariableEntero("LIQ_PLAZO_AUT", 3));
        iLiquidez.setCapPago(getValorVariableDecimal("LIQ_CAP_PAGO", 15));
        
        return iLiquidez;
    }
    
    private ZstEntrev parseEntrev(){
        ZstEntrev iEntrev = new ZstEntrev();
        
        iEntrev.setPolit(getValorVariableBooleano("PLD_QA", "Si", "No", "X", ""));//1 char  ("" = NO, "X" = SI)
        iEntrev.setPoliNombre(getValorVariable("PLD_QA_NOMBRE", 50, false, false));
        iEntrev.setPoliCargo(getValorVariable("PLD_QA_CARGO", 40, false, false));
        iEntrev.setOtraPer(getValorVariableBooleano("PLD_QC", "Si", "No", "X", "")); //1 char  ("" = NO, "X" = SI)
        iEntrev.setOtrpNombre(getValorVariable("PLD_QC_NOMBRE", 50, false, false));
        iEntrev.setOtrpRelac(getValorVariable("PLD_QC_RELACION", 3, false, false));
        iEntrev.setOtrpJust(getValorVariable("PLD_QC_JUSTIFICA", 100, false, false));
        iEntrev.setProfNeg(getValorVariableBooleano("PLD_QF", "Si", "No", "X", ""));// 1 char  ("" = NO, "X" = SI)
        iEntrev.setPrngJust(getValorVariable("PLD_QF_JUSTIFICA", 100, false, false));
        iEntrev.setFuncPubl(getValorVariableBooleano("PLD_QA", "Si", "No", "X", ""));// 1 char ("" = NO, "X" = SI)
        iEntrev.setFamFunc(getValorVariableBooleano("PLD_QB", "Si", "No", "X", "")); // 1 char ("" = NO, "X" = SI)
        iEntrev.setIngAdic(getValorVariableDecimal("OTIN_UTILIDAD_MENS", 15).equals("0")?"":"X"); // 1 char ("" = NO, "X" = SI)
        iEntrev.setMontoAdic(getValorVariableDecimal("OTIN_UTILIDAD_MENS", 15));
        iEntrev.setOrigen(getValorVariable("OTIN_GIRO", 50, false, false));
        
        return iEntrev;        
    }
    
    private ZstPldPerson parsePld(){
        ZstPldPerson iPld = new ZstPldPerson();
        
        iPld.setMaxDiar("500");//
        iPld.setMaxMens("10000");//
        iPld.setPagMax("4");//
        iPld.setRiesgo("B"); // B= Bajo
        iPld.setEntId("");// "ID"
        iPld.setMonedas("MXN");// MONEDAS (DEFAULT "MXN")
        iPld.setVias("01");//VIAS (DEFAULT "01" = EFECTIVO)
        iPld.setBancos("04");// BANCOS (DEFAULT "04" = Banamex)
        iPld.setOrigenRec(getValorVariable("PLD_ORIGEN_REC", 4, false, false)); // ORIGEN DE RECURSOS (Catalogo de SAP, PROP = Propios, CRED = Credito)
        iPld.setDestCred(getValorVariable("PLD_DEST_CRED", 3, false, false)); // DESTINO DE CREDITO (Catalogo de SAP)
        iPld.setDestOtro(getValorVariable("PLD_DEST_CRED_DESC", 40, false, false));// JUSTIFICACION EN CASO DE OPCION OTRO EN DESTINO CRED
        
        return iPld;
    }
    
    /**
    *   > Fecha de solicitud (fecha en que entro por ultima vez al estatus 2 Por Aprobar)
    *	> Fecha de suscripcion (fecha en que se aprueba por verificador y mesa de control, bitacora order by 
    *        fecha desc, filtrando por estatus = 6 (aprobada))
    *   > Vencimiento (decenal, catorcenal,... Crear metodo que haga el equiparable entre los datos la 
    *        solicitud a este dato para contrato)
    *	> Monto solicitado = GFAM_MONTO_SOLICITADO
    *   > Monto aprobado = LIQ_MONTO_AUT
    *   > Periodo de Validez Desde = Fecha de suscripcion
    *   > Periodo de Validez Hasta = Fecha de suscripcion+ Plazo
    *   > Periodo de Validez Fin de Periodo = ? Fecha de suscripcion+ Plazo) ó fin de mes de (Fecha de 
    *       suscripcion+ Plazo)
    *   > Valido Desde =  Hoy
    *   > Condiciones:
    *       > Valido desde = Fecha de suscripcion
    *       > Vence el = Fecha de suscripcion + Plazo
    *       > Fecha Calculo = Fecha de suscripcion
    *           cond 1: (Interes Ordinario)
    *			Porcentaje = SubProducto.getTasaAnual
    *		cond 2: (Amortizacion Pagos iguales)
    *			> Importe = calcCuotaMensual(monto aprobado, vencimiento)
    */
    private ZstContract parseContract(CrFormularioEvento crFormularioEvento, CrCreaInterlocutorSapBafarResponse ccisbr) throws Exception{
        ZstContract iContract = new ZstContract();
        
        CrFormularioEventoBO crFormularioEventoBO = new CrFormularioEventoBO(this.conn);
        crFormularioEventoBO.setCrFormularioEvento(crFormularioEvento);
        CrFrmEventoSolicitud crFrmEventoSolicitud = crFormularioEventoBO.getFrmEventoSolicitud();
        CrSolicitudBitacora bitacoraSolicitado = crFormularioEventoBO.getSolicitudBitacoraEventoSolicitado(crFormularioEvento.getIdFormularioEvento());
        CrSolicitudBitacora bitacoraAprobado = crFormularioEventoBO.getSolicitudBitacoraEventoAprobado(crFormularioEvento.getIdFormularioEvento());
        CrProductoCreditoDaoImpl crProductoCreditoDao = new CrProductoCreditoDaoImpl(this.conn);
        CrProductoCredito subProductoCredito = crProductoCreditoDao.findByPrimaryKey(crFrmEventoSolicitud.getIdProductoCredito());
        
        
        Date hoy = new Date();
        try { hoy = ZonaHorariaBO.DateZonaHorariaByIdEmpresa(this.conn, new Date(), crFormularioEvento.getIdEmpresa()).getTime(); }catch(Exception ex){ }
        Date fechaSolicitud = bitacoraSolicitado!=null?bitacoraSolicitado.getFechaHrCreacion() : hoy ;
        Date fechaSuscripcion = bitacoraAprobado!=null?bitacoraAprobado.getFechaHrCreacion() : hoy ;
        Date fechaInicioCredito = fechaSuscripcion; // ?
        double montoSolicitado = subProductoCredito.getMonto(); 
        double montoAprobado = montoSolicitado;
        try{
            montoSolicitado = new BigDecimal(getValorVariableDecimal("GFAM_MONTO_SOLICITADO", 15)).doubleValue();
            montoAprobado =  new BigDecimal(getValorVariableDecimal("LIQ_MONTO_AUT", 15)).doubleValue();
            
            montoSolicitado = Converter.roundDouble(montoSolicitado);
            montoAprobado = Converter.roundDouble(montoAprobado);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        int plazoMeses = new Double(subProductoCredito.getPlazo()).intValue();
        int noCuotasPlazo = 0;
        CrUtilCalculos.TipoPlazo tipoPlazo = CrUtilCalculos.TipoPlazo.MENSUAL;
        try {
            // LAB_FRECUENCIA_PAGO
            // El tipo de plazo se basa en la periodicidad de pago
            //  del solicitante (su pago de nomina o ingresos)
            String varFrecuenciaPago = getValorVariable("LAB_FRECUENCIA_PAGO", 50, false, false);
            if (varFrecuenciaPago.toUpperCase().contains("SEMANA")){
                tipoPlazo = CrUtilCalculos.TipoPlazo.SEMANAL;
            }else if (varFrecuenciaPago.toUpperCase().contains("DECENA")){
                tipoPlazo = CrUtilCalculos.TipoPlazo.DECENAL;
            }else if (varFrecuenciaPago.toUpperCase().contains("CATORCENA")){
                tipoPlazo = CrUtilCalculos.TipoPlazo.CATORCENAL;
            }else if (varFrecuenciaPago.toUpperCase().contains("QUINCENA")){
                tipoPlazo = CrUtilCalculos.TipoPlazo.QUINCENAL;
            }else if (varFrecuenciaPago.toUpperCase().contains("MENSUAL") || varFrecuenciaPago.toUpperCase().contains("MES")){
                tipoPlazo = CrUtilCalculos.TipoPlazo.MENSUAL;
            }
            noCuotasPlazo = plazoMeses * tipoPlazo.getEquivalenciaMes();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        Date fechaPrimerPago = CrUtilCalculos.calcFechaFinPeriodo(fechaInicioCredito, 1, tipoPlazo);
        if (tipoPlazo == CrUtilCalculos.TipoPlazo.MENSUAL){
            fechaPrimerPago = CrUtilCalculos.calcUltimoDiaMes(fechaInicioCredito);
        }
        Date fechaFinCredito = CrUtilCalculos.calcFechaFinPeriodo(fechaInicioCredito, noCuotasPlazo, tipoPlazo);
        double porcentajeTasaOrdinaria = subProductoCredito.getTasaInteresAnual();
        double importePrimerCuota = CrUtilCalculos.calcCuota(montoAprobado, subProductoCredito.getTasaInteresAnual(), noCuotasPlazo);
        
        iContract.setBukrs001("");// ?
        iContract.setGsart002("");// ?
        iContract.setSsuch003("");// ?
        iContract.setSantwhr008("MXN"); // MONEDA (FIJO MXN)
        iContract.setXdantrag009(DateManage.formatDate(fechaSolicitud, "yyyy-MM-dd")); //FECHA DE SOLICITUD
        iContract.setXbantrag010(""+montoSolicitado); //CAPITAL SOLICITADO (MONTO SOLICITADO)
        iContract.setSfrist011(tipoPlazo.getValorVarWSCreaInterlocu()); // VENCIMIENTO 07 = Semanal, 10 = Decenal, 14 = Catorcenal, 15 = Quincenal, 30 = Mensual
        iContract.setSbilk012(""); // ?
        iContract.setSgr2014(""); // ?
        iContract.setSgrp3015(""); // ?
        iContract.setXalkz016("PRUEBAS"); // TEXTO CORTO
        iContract.setXallb017("TEXTO DE PRUEBAS"); // TEXTO LARGO
        iContract.setSchemeId018(""); // ?
        iContract.setXbzusage019(""+montoAprobado); //CAPITAL SUSCRITO (MONTO APROBADO)
        iContract.setSdisein020(""); // ?
        iContract.setSzbmeth021(""); // ?
        iContract.setXdguelKk022(DateManage.formatDate(fechaSuscripcion, "yyyy-MM-dd")); // PERIODO DE VALIDEZ DESDE
        iContract.setSeffmeth023(""); // ?
        iContract.setXdefsz024(DateManage.formatDate(fechaFinCredito, "yyyy-MM-dd")); // PERIODO DE VALIDEZ HASTA
        iContract.setXdelfz026(DateManage.formatDate(fechaFinCredito, "yyyy-MM-dd")); // PERIODO DE VALIDEZ FIN DE PERIODO
        // CONDICIONES (CARGOS EXTRAS, SEGURO, ETC.)
        iContract.setDguelKp01036(DateManage.formatDate(fechaInicioCredito, "yyyy-MM-dd")); // CONDICION 1 - VALIDO DESDE
        iContract.setDguelKp02037(DateManage.formatDate(fechaInicioCredito, "yyyy-MM-dd")); // CONDICION 2 - VALIDO DESDE
        iContract.setPkond01038(""+porcentajeTasaOrdinaria); // CONDICION 1 - PORCENTAJE %
        iContract.setPkond02039(""); // CONDICION 2 - PORCENTAJE %
        iContract.setBkond01(""); // CONDICION 1 - IMPORTE
        iContract.setBkond02(""+importePrimerCuota); // CONDICION 2 - IMPORTE
        iContract.setAmmrhy01041(""); // ?
        iContract.setAmmrhy02042(""); // ?
        iContract.setDfaell01043(DateManage.formatDate(fechaPrimerPago, "yyyy-MM-dd")); // CONDICION 1 - VENCE EL
        iContract.setDfaell02044(DateManage.formatDate(fechaPrimerPago, "yyyy-MM-dd")); // CONDICION 2 - VENCE EL
        iContract.setDvalut01045(DateManage.formatDate(fechaPrimerPago, "yyyy-MM-dd")); // CONDICION 1 FECHA CALCULO
        iContract.setDvalut02046(DateManage.formatDate(fechaPrimerPago, "yyyy-MM-dd")); // CONDICION 2 FECHA CALCULO
        iContract.setSfult01047(""); // ?
        iContract.setSfult02048(""); // ?
        iContract.setSvult01049(""); // ?
        iContract.setSvult02050(""); // ?
        // FIN CONDICIONES
        iContract.setDguelKp060(DateManage.formatDate(fechaInicioCredito, "yyyy-MM-dd")); // VALIDO DESDE
        iContract.setZlsch066(""); // VIA DE PAGO (DEFAULT: VACIO O "T")
        iContract.setMwskz067("A9"); // INDICADOR DE IMPUESTOS (DEFAULT: "A9")
        iContract.setSbust068("1"); // CONTROL DE CONTABILIZACION (DEFAULT "1")
        iContract.setSeffmeth087(""); // ?
        iContract.setRole091("A100"); // CLASE ROL (DEFAULT "100")
        
        ccisbr.setFechaSolicitud(fechaSolicitud);
        ccisbr.setFechaSuscripcion(fechaSuscripcion);
        ccisbr.setFechaInicioCredito(fechaInicioCredito);
        ccisbr.setMontoSolicitado(montoSolicitado);
        ccisbr.setMontoAprobado(montoAprobado);
        ccisbr.setNoCuotasPlazo(noCuotasPlazo);
        ccisbr.setTipoPlazo(tipoPlazo);
        ccisbr.setFechaFinCredito(fechaFinCredito);
        ccisbr.setPorcentajeTasaOrdinaria(porcentajeTasaOrdinaria);
        ccisbr.setFechaPrimerPago(fechaPrimerPago);
        ccisbr.setImportePrimerCuota(importePrimerCuota);
        ccisbr.setPlazoMeses(plazoMeses);
        ccisbr.setCuotaRegular(importePrimerCuota);
        
        /*
        iContract.setBukrs001("");// ?
        iContract.setGsart002("");// ?
        iContract.setSsuch003("");// ?
        iContract.setSantwhr008("MXN"); // MONEDA (FIJO MXN)
        iContract.setXdantrag009("2016-06-01"); //FECHA DE SOLICITUD
        iContract.setXbantrag010("10000"); //CAPITAL SOLICITADO (MONTO SOLICITADO)
        iContract.setSfrist011("30"); // VENCIMIENTO 07 = Semanal, 10 = Decenal, 14 = Catorcenal, 15 = Quincenal, 30 = Mensual
        iContract.setSbilk012(""); // ?
        iContract.setSgr2014(""); // ?
        iContract.setSgrp3015(""); // ?
        iContract.setXalkz016("PRUEBAS"); // TEXTO CORTO
        iContract.setXallb017("TEXTO DE PRUEBAS"); // TEXTO LARGO
        iContract.setSchemeId018(""); // ?
        iContract.setXbzusage019("10000"); //CAPITAL SUSCRITO (MONTO APROBADO)
        iContract.setSdisein020(""); // ?
        iContract.setSzbmeth021(""); // ?
        iContract.setXdguelKk022("2016-06-01"); // PERIODO DE VALIDEZ DESDE
        iContract.setSeffmeth023(""); // ?
        iContract.setXdefsz024("2017-06-01"); // PERIODO DE VALIDEZ HASTA
        iContract.setXdelfz026("2017-06-01"); // PERIODO DE VALIDEZ FIN DE PERIODO
        // CONDICIONES (CARGOS EXTRAS, SEGURO, ETC.)
        iContract.setDguelKp01036("2016-06-01"); // CONDICION 1 - VALIDO DESDE
        iContract.setDguelKp02037("2016-06-01"); // CONDICION 2 - VALIDO DESDE
        iContract.setPkond01038("13.5"); // CONDICION 1 - PORCENTAJE %
        iContract.setPkond02039(""); // CONDICION 2 - PORCENTAJE %
        iContract.setBkond01(""); // CONDICION 1 - IMPORTE
        iContract.setBkond02("895.52"); // CONDICION 2 - IMPORTE
        iContract.setAmmrhy01041(""); // ?
        iContract.setAmmrhy02042(""); // ?
        iContract.setDfaell01043("2016-06-30"); // CONDICION 1 - VENCE EL
        iContract.setDfaell02044("2016-06-30"); // CONDICION 2 - VENCE EL
        iContract.setDvalut01045("2016-06-30"); // CONDICION 1 FECHA CALCULO
        iContract.setDvalut02046("2016-06-30"); // CONDICION 2 FECHA CALCULO
        iContract.setSfult01047(""); // ?
        iContract.setSfult02048(""); // ?
        iContract.setSvult01049(""); // ?
        iContract.setSvult02050(""); // ?
        // FIN CONDICIONES
        iContract.setDguelKp060("2016-06-01"); // VALIDO DESDE
        iContract.setZlsch066(""); // VIA DE PAGO (DEFAULT: VACIO O "T")
        iContract.setMwskz067("A9"); // INDICADOR DE IMPUESTOS (DEFAULT: "A9")
        iContract.setSbust068("1"); // CONTROL DE CONTABILIZACION (DEFAULT "1")
        iContract.setSeffmeth087(""); // ?
        iContract.setRole091("A100"); // CLASE ROL (DEFAULT "100")
        */
        
        return iContract;
    }
    
    private ZstContPerson parseContactoReferencia(String prefixData){
        
        ZstContPerson contacto = new ZstContPerson();
        contacto.setApPat(getValorVariable("REF_" + prefixData + "_AP_PATERNO", 40, false, false));
        contacto.setApMat(getValorVariable("REF_" + prefixData + "_AP_MATERNO", 40, false, false));
        contacto.setNombre1(getValorVariable("REF_" + prefixData + "_NOMBRE_A", 40, false, false));
        contacto.setNobre2(getValorVariable("REF_" + prefixData + "_NOMBRE_B", 40, false, false));
        contacto.setTelef(getValorVariable("REF_" + prefixData + "_TELEFONO", 10, false, false));
        contacto.setRelac(getValorVariable("REF_" + prefixData + "_RELACION", 3, false, false));
        contacto.setCalle(getValorVariable("REF_" + prefixData + "_DOM_CALLE", 40, false, false));
        contacto.setColonia(getValorVariable("REF_" + prefixData + "_DOM_COLONIA", 40, false, false));
        contacto.setNoExt(getValorVariable("REF_" + prefixData + "_DOM_NO_EXT", 10, false, false));
        contacto.setNoInt(getValorVariable("REF_" + prefixData + "_DOM_NO_INT", 5, false, false));
        contacto.setCp(getValorVariable("REF_" + prefixData + "_DOM_CP", 5, false, false));
        contacto.setCiudad(getValorVariable("REF_" + prefixData + "_DOM_CIUDAD", 40, false, false));
        
        return contacto;
        
    }
    
    // </editor-fold>
 
    // <editor-fold defaultstate="collapsed" desc="Métodos Envío y Parse Respuesta WS Crea Interlocutor-Contrato"> 
    
    public CrCreaInterlocutorSapBafarResponse registraSolicitudCreditoSAP(CrFormularioEvento crFormularioEvento) throws Exception{
        
        CrCreaInterlocutorSapBafarResponse ccisbr = new CrCreaInterlocutorSapBafarResponse();
        
        analizaSolicitud(crFormularioEvento);
        
        ZstAvalPerson iAval;
        ZstConyPerson iCony;
        ZstDatosPerson iDatos;
        ZstDirecPerson iDirec;
        ZstGastoPerson iGasto;
        ZstIngresoPerson iIngreso;
        ZstLiquidezPerson iLiquidez;
        ZstPldPerson iPld;
        TableOfZstContPerson tableOfZstContPerson = new TableOfZstContPerson();
        ZstContract iContract;
        ZstEntrev iEntrev;
        
        
        // Aval
        iAval = parseAvalPerson();        
        //Conyuge
        iCony = parseConyuguePersona();
        // Solicitante Datos
        iDatos = parseDatosPersona();
        // Solicitante Domicilio
        iDirec = parseDirecPersona();
        // Gastos Familiares
        iGasto = parseGastoPerson();
        // Ingresos
        iIngreso = parseIngresoPerson();
        // Liquidez
        iLiquidez = parseLiquidezPerson();
        // PLD
        iPld = parsePld();
        
        // Referencias - Contactos
        tableOfZstContPerson.getItem().add(parseContactoReferencia("A"));
        tableOfZstContPerson.getItem().add(parseContactoReferencia("B"));
        tableOfZstContPerson.getItem().add(parseContactoReferencia("C"));
        tableOfZstContPerson.getItem().add(parseContactoReferencia("D"));
        
        // iContract
        iContract = parseContract(crFormularioEvento, ccisbr);
        
        // Entrevista PLD
        iEntrev = parseEntrev();
        
        // Creamos objeto Request
        ZfmtrCreateInterlocu parameters = new ZfmtrCreateInterlocu();
        parameters.setIAval(iAval);
        parameters.setICony(iCony);
        parameters.setIDatos(iDatos);
        parameters.setIDirec(iDirec);
        parameters.setIGasto(iGasto);
        parameters.setIIngreso(iIngreso);
        parameters.setILiquidez(iLiquidez);
        parameters.setIPld(iPld);
        parameters.setIContract(iContract);
        parameters.setTContac(tableOfZstContPerson);
        parameters.setIEntrev(iEntrev);
        parameters.setMesstab(new TableOfBdcmsgcoll());
        parameters.setTAmort(new TableOfZlmTablaAmort());
        
        return parseCreateInterlocuResponse(parameters, ccisbr);
    }
    
    private CrCreaInterlocutorSapBafarResponse parseCreateInterlocuResponse(ZfmtrCreateInterlocu parameters, CrCreaInterlocutorSapBafarResponse ccisbr){
        
        ZfmtrCreateInterlocuResponse resp;
        try{
            // Invocamos web service
            resp  = zfmtrCreateInterlocu(parameters);
        }catch(Exception ex){
            ex.printStackTrace();
            ccisbr.setError(true);
            ccisbr.setNumError("0");
            ccisbr.setMsgError("Error inesperado al conectar a servicio SAP (zfmtrCreateInterlocu). Descripción: " + GenericMethods.exceptionStackTraceToString(ex));
            
            return ccisbr;
        }
        
        
        // Procesamos respuesta
        if (resp.getMesstab()!=null
                && resp.getMesstab().getItem()!=null
                && resp.getMesstab().getItem().size()>0){
            
            for (Bdcmsgcoll bdcmsgcoll : resp.getMesstab().getItem()){ 
                
                //Interlocutor R1-214
                if (bdcmsgcoll.getMsgid().equals("R1") && bdcmsgcoll.getMsgnr().equals("214")){
                    ccisbr.setBusinessPartner(StringManage.getValidString(bdcmsgcoll.getMsgv2()));
                }
                
                //Contrato 67-106
                if (bdcmsgcoll.getMsgid().equals("67") && bdcmsgcoll.getMsgnr().equals("106")){
                    ccisbr.setNoContrato(StringManage.getValidString(bdcmsgcoll.getMsgv2()));
                }
                
                switch (StringManage.getValidString(bdcmsgcoll.getMsgtyp())) {
                    case "S":
                        ccisbr.setError(false);
                        ccisbr.setNumError("0");
                        ccisbr.setMsgError("Proceso Exitoso. Creación de Interlocutor y Contrato en SAP exitoso.");
                        break;
                    case "E":
                        ccisbr.setError(true);
                        ccisbr.setNumError(StringManage.getValidString(bdcmsgcoll.getMsgnr()));
                        ccisbr.setMsgError("Proceso con Problemas SAP. <br/>" + GenericMethods.objectToString( bdcmsgcoll) );
//                        ccisbr.setMsgError("Proceso con Problemas SAP. |" 
//                                + "Msgv1: " + StringManage.getValidString(bdcmsgcoll.getMsgv1()) + "|"
//                                + "Msgv2: " + StringManage.getValidString(bdcmsgcoll.getMsgv2()) + "|"
//                                + "Msgv3: " + StringManage.getValidString(bdcmsgcoll.getMsgv3()) + "|"
//                                + "Msgv4: " + StringManage.getValidString(bdcmsgcoll.getMsgv4()));
                        return ccisbr;
                    default:
                        // do nothing
                }
            }
            
        }else{
            ccisbr.setError(true);
            ccisbr.setNumError("0");
            ccisbr.setMsgError("No se recibió una respuesta exitosa del servicio SAP (zfmtrCreateInterlocu). Dato Messtab vacío. ");
            
            return ccisbr;
        }
        
        // Tabla de amortizacion
        if (resp.getTAmort()!=null
                && resp.getTAmort().getItem()!=null
                && resp.getTAmort().getItem().size()>0){
            CrTablaAmortizacion crTablaAmortizacion = new CrTablaAmortizacion();
            for (ZlmTablaAmort zta : resp.getTAmort().getItem()){
                CrTablaAmortizacionDetalle ctadetalle = new CrTablaAmortizacionDetalle();
                ctadetalle.setComisiones(zta.getComisiones());
                ctadetalle.setFecha(zta.getFecha());
                ctadetalle.setIntereses(zta.getIntereses());
                ctadetalle.setIva(zta.getIva());
                ctadetalle.setNoPago(zta.getNoPago());
                ctadetalle.setPagoCapital(zta.getPagoCapital());
                ctadetalle.setPagoEspecial(zta.getPagoEspecial());
                ctadetalle.setSaldoCapFin(zta.getSaldoCapFin());
                ctadetalle.setSaldoCapIni(zta.getSaldoCapIni());
                ctadetalle.setTotalPago(zta.getTotalPago());
                
                
                crTablaAmortizacion.getListaTablaAmortizacionDetalles().add(ctadetalle);
            }
            ccisbr.setCrTablaAmortizacion(crTablaAmortizacion);
        }
        
        return ccisbr;
    }

    // </editor-fold>

// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Métodos para WS Acepta Contrato">    
    
    public CrAceptaContratoSapBafarResponse registraAceptacionCreditoSAP(String sapNoContrato){
        
        ZfmtrAcceptContract params = new ZfmtrAcceptContract();
        params.setMesstab(new com.sap.bafar.ws.aceptacontrato.TableOfBdcmsgcoll());
        params.setXranl002(sapNoContrato);
        
        return parseAcceptContractResponse(params);
    }
    
    private CrAceptaContratoSapBafarResponse parseAcceptContractResponse(ZfmtrAcceptContract parameters){
        CrAceptaContratoSapBafarResponse cacsbr = new CrAceptaContratoSapBafarResponse();
        ZfmtrAcceptContractResponse resp = null;
        try{
            // Invocamos web service
            resp  = zfmtrAcceptContract(parameters);
        }catch(Exception ex){
            ex.printStackTrace();
            cacsbr.setError(true);
            cacsbr.setNumError("0");
            cacsbr.setMsgError("Error inesperado al conectar a servicio SAP (zfmtrAcceptContract). Descripción: " + GenericMethods.exceptionStackTraceToString(ex));
            
            return cacsbr;
        }
        
        
        // procesamos respuesta
        if (resp.getMesstab()!=null
                && resp.getMesstab().getItem()!=null
                && resp.getMesstab().getItem().size()>0){
            
            for (com.sap.bafar.ws.aceptacontrato.Bdcmsgcoll bdcmsgcoll : resp.getMesstab().getItem()){ 
                
                switch (StringManage.getValidString(bdcmsgcoll.getMsgtyp())) {
                    case "S":
                        cacsbr.setError(false);
                        cacsbr.setNumError("0");
                        cacsbr.setMsgError("Aceptación de contrato registrada en SAP exitosamente.");
                        break;
                    case "E":
                        cacsbr.setError(true);
                        cacsbr.setNumError(StringManage.getValidString(bdcmsgcoll.getMsgnr()));
                        cacsbr.setMsgError("Proceso con Problemas SAP. <br/>" + GenericMethods.objectToString( bdcmsgcoll) );
//                        ccdsbr.setMsgError("Error SAP. |" 
//                                + "Msgv1: " + StringManage.getValidString(bdcmsgcoll.getMsgv1()) + "|"
//                                + "Msgv2: " + StringManage.getValidString(bdcmsgcoll.getMsgv2()) + "|"
//                                + "Msgv3: " + StringManage.getValidString(bdcmsgcoll.getMsgv3()) + "|"
//                                + "Msgv4: " + StringManage.getValidString(bdcmsgcoll.getMsgv4()));
                        return cacsbr;
                    default:
                        // do nothing
                }
            }
            
        }else{
            cacsbr.setError(true);
            cacsbr.setNumError("0");
            cacsbr.setMsgError("No se recibió una respuesta exitosa del servicio SAP (zfmtrAcceptContract). Dato Messtab vacío. ");
            
            return cacsbr;
        }
        
        return cacsbr;
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Métodos para WS Valida Interlocutor Listas Negras">    
    
    
    /**
     * Consulta si el Cliente se encuentra en Listas Negras, Buro interno de BAFAR
     * o si ya tiene un contrato de crédito vigente.
     * @param rfc
     * @param curp
     * @param apellidoPaterno
     * @param apellidoMaterno
     * @param primerNombre
     * @param segundoNombre
     * @return 
     */
    public CrValidaListasNegrasSapBafarResponse consultaClienteListasNegrasSAP(String rfc, String curp, String apellidoPaterno, String apellidoMaterno, String primerNombre, String segundoNombre){
        
        ZfmtrValidaInterlocu params = new ZfmtrValidaInterlocu();
        params.setMesstab(new com.sap.bafar.ws.validainterlocutor.TableOfBdcmsgcoll());
        params.setIRfc("");
        params.setICurp("");
        params.setIApellidop("");
        params.setIApellidom("");
        params.setINombre("");
        params.setINombre2("");
        
        return parseValidaInterlocuResponse(params);
    }
    
    private CrValidaListasNegrasSapBafarResponse parseValidaInterlocuResponse(ZfmtrValidaInterlocu parameters){
        CrValidaListasNegrasSapBafarResponse cacsbr = new CrValidaListasNegrasSapBafarResponse();
        ZfmtrValidaInterlocuResponse resp;
        try{
            // Invocamos web service
            resp  = zfmtrValidaInterlocu(parameters);
        }catch(Exception ex){
            ex.printStackTrace();
            cacsbr.setError(true);
            cacsbr.setNumError("0");
            cacsbr.setMsgError("Error inesperado al conectar a servicio SAP (zfmtrValidaInterlocu). Descripción: " + GenericMethods.exceptionStackTraceToString(ex));
            
            return cacsbr;
        }
        
        // en este método si el valor de subrc == 0 indica éxito
        if (resp.getSubrc().equals("00") || resp.getSubrc().equals("0")){
            cacsbr.setError(false);
            cacsbr.setNumError("0");
            cacsbr.setMsgError("Validación de Historial Crediticio interno SAP superada exitosamente.");
            
            return cacsbr;
        }
        
        // procesamos respuesta
        if (resp.getMesstab()!=null
                && resp.getMesstab().getItem()!=null
                && resp.getMesstab().getItem().size()>0){
            
            for (com.sap.bafar.ws.validainterlocutor.Bdcmsgcoll bdcmsgcoll : resp.getMesstab().getItem()){ 
                
                switch (StringManage.getValidString(bdcmsgcoll.getMsgtyp())) {
                    case "S":
                        cacsbr.setError(false);
                        cacsbr.setNumError("0");
                        cacsbr.setMsgError("Validación de Historial Crediticio interno SAP superada exitosamente.");
                        break;
                    case "E":
                        cacsbr.setError(true);
                        cacsbr.setNumError(StringManage.getValidString(bdcmsgcoll.getMsgnr()));
                        cacsbr.setMsgError("Validación de Historial Crediticio interno SAP NO superada. <br/>" + GenericMethods.objectToString( bdcmsgcoll) );
//                        ccdsbr.setMsgError("Validación de Historial Crediticio interno SAP NO superada. |" 
//                                + "Msgv1: " + StringManage.getValidString(bdcmsgcoll.getMsgv1()) + "|"
//                                + "Msgv2: " + StringManage.getValidString(bdcmsgcoll.getMsgv2()) + "|"
//                                + "Msgv3: " + StringManage.getValidString(bdcmsgcoll.getMsgv3()) + "|"
//                                + "Msgv4: " + StringManage.getValidString(bdcmsgcoll.getMsgv4()));
                        return cacsbr;
                    default:
                        // do nothing
                }
            }
            
        }else{
            cacsbr.setError(true);
            cacsbr.setNumError("0");
            cacsbr.setMsgError("No se recibió una respuesta exitosa del servicio SAP (zfmtrValidaInterlocu). Dato Messtab vacío. ");
            
            return cacsbr;
        }
        
        return cacsbr;
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Métodos para WS Consulta Dispersion">    
    
    public CrConsultaDispersionSapBafarResponse consultaDispersionSAP(String sapNoContrato, Date fechaHoraInicial, Date fechaHoraFinal){
        
        ZfmtrDisper params = new ZfmtrDisper();
        
        String noContrato =  StringManage.getValidString(sapNoContrato) ;
        String rangoFechaInicio = "";
        String rangoFechaFin = "";
        
        if (fechaHoraInicial!=null){
            rangoFechaInicio = DateManage.formatDate(fechaHoraInicial, "yyyy-MM-dd");
        }
        if (fechaHoraFinal!=null){
            rangoFechaFin = DateManage.formatDate(fechaHoraFinal, "yyyy-MM-dd");
        }
        
        params.setICredi(noContrato);
        params.setIFecin(rangoFechaInicio);
        params.setIFecfn(rangoFechaFin);
        params.setISocie(SAP_SOCIEDAD);
        params.setMesstab(new com.sap.bafar.ws.consultadispersion.TableOfBdcmsgcoll());
        params.setTDisp(new TableOfZcmlOrdPago());
        
        return parseReadDispersionResponse(params);
    }
    
    private CrConsultaDispersionSapBafarResponse parseReadDispersionResponse(ZfmtrDisper parameters){
        CrConsultaDispersionSapBafarResponse ccdsbr = new CrConsultaDispersionSapBafarResponse();
        ZfmtrDisperResponse resp = null;
        try{
            // Invocamos web service
            resp  = zfmtrDisper(parameters);
        }catch(Exception ex){
            ex.printStackTrace();
            ccdsbr.setError(true);
            ccdsbr.setNumError("0");
            ccdsbr.setMsgError("Error inesperado al conectar a servicio SAP (zfmtrDisper). Descripción: " + GenericMethods.exceptionStackTraceToString(ex));
            
            return ccdsbr;
        }
    
        // procesamos respuesta - verificamos si no hubo error
        if (resp.getMesstab()!=null
                && resp.getMesstab().getItem()!=null
                && resp.getMesstab().getItem().size()>0){
            
            for (com.sap.bafar.ws.consultadispersion.Bdcmsgcoll bdcmsgcoll : resp.getMesstab().getItem()){ 
                
                switch (StringManage.getValidString(bdcmsgcoll.getMsgtyp())) {
                    case "E":
                        if (!StringManage.getValidString(bdcmsgcoll.getMsgnr()).equals("999")){
                            ccdsbr.setError(true);
                            ccdsbr.setNumError(StringManage.getValidString(bdcmsgcoll.getMsgnr()));
                            ccdsbr.setMsgError("Respuesta SAP: \n<br/>"  +
                                    GenericMethods.objectToString(bdcmsgcoll));
                            return ccdsbr;
                        }
                    default:
                        // do nothing
                }
            }
            
        }
        
        // si no hubo error extraemos los datos de las ordenes de pago encontradas
        List<CrOrdenPago> listaOrdenesPago = new ArrayList<CrOrdenPago>();
        if (resp.getTDisp()!=null
                && resp.getTDisp().getItem()!=null
                && resp.getTDisp().getItem().size()>0){
            for (ZcmlOrdPago op : resp.getTDisp().getItem()){
                CrOrdenPago crOrdenPago = new CrOrdenPago();
                crOrdenPago.setAcreditado(StringManage.getValidString(op.getAcreditado()));
                crOrdenPago.setCantidad(StringManage.getValidString(op.getCantidad()));
                crOrdenPago.setCredito(StringManage.getValidString(op.getCredito()));
                crOrdenPago.setEmisor(StringManage.getValidString(op.getEmisor()));
                crOrdenPago.setFecha(StringManage.getValidString(op.getFecha()));
                crOrdenPago.setImporte(StringManage.getValidString(op.getImporte()));
                crOrdenPago.setNombre(StringManage.getValidString(op.getNombre()));
                crOrdenPago.setOrdenDePago("");
                crOrdenPago.setProceso("");
                crOrdenPago.setReferencia(StringManage.getValidString(op.getCredito()));
                crOrdenPago.setStatus(StringManage.getValidString(op.getStatus()));
                
                listaOrdenesPago.add(crOrdenPago);
            }
            
            ccdsbr.setListaOrdenesPago(listaOrdenesPago);
            
            ccdsbr.setError(false);
            ccdsbr.setNumError("0");
            ccdsbr.setMsgExito("Consulta de dispersión exitosa.");
        }else{
            ccdsbr.setError(true);
            ccdsbr.setNumError("901");
            ccdsbr.setMsgError("No se encontro ninguna dispersion (Orden de Pago).");
        }
        
        return ccdsbr;
    }
    
// </editor-fold>    

// <editor-fold defaultstate="collapsed" desc="Métodos de consumo Web Service">    

    /**
     * Puerto para conexión a Servicio Web SAP - Creación de Interlocutor y Contrato
     * @param parameters
     * @return Respuesta del servicio
     */
    private ZfmtrCreateInterlocuResponse zfmtrCreateInterlocu(com.sap.bafar.ws.creacontrato.ZfmtrCreateInterlocu parameters) {
        
        /**
         * En la definicion del servicio definimos una variable estatica auto-definida
         * en la que colocamos los datos de autenticacion, ya que previo a enviar el request con datos,
         * JAX-WS intenta obtener la definición del Web Service (WSDL) para verificar que no haya cambios
         * y que sea equivalente a la usada en la creación del cliente WS. La variable se define asi:
         * 
         * static {
                java.net.Authenticator.setDefault(new java.net.Authenticator() {

                    @Override
                    protected java.net.PasswordAuthentication getPasswordAuthentication() {
                        Configuration appConfiguration = new Configuration();
                        return new java.net.PasswordAuthentication("myUser", "myPassword".toCharArray());
                    }
                });
            }
         */
        com.sap.bafar.ws.creacontrato.ZwsCreaInter_Service service = new com.sap.bafar.ws.creacontrato.ZwsCreaInter_Service();
        
        com.sap.bafar.ws.creacontrato.ZwsCreaInter port = service.getZBinInter();
        Configuration appConfiguration = new Configuration();
        //Autenticación básica por HTTP - Header (esta autenticacion ya es al hacer el request con datos)
        BindingProvider prov = (BindingProvider)port;
        prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, appConfiguration.getSapWsBafarUser());
        prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, appConfiguration.getSapWsBafarPass());
        
        return port.zfmtrCreateInterlocu(parameters);
    }
    
    /**
     * Puerto para conexion a servicio Web SAP - Acepta Contrato
     * @param parameters
     * @return 
     */
    private static ZfmtrAcceptContractResponse zfmtrAcceptContract(com.sap.bafar.ws.aceptacontrato.ZfmtrAcceptContract parameters) {
        com.sap.bafar.ws.aceptacontrato.WsAcceptContract service = new com.sap.bafar.ws.aceptacontrato.WsAcceptContract();
        com.sap.bafar.ws.aceptacontrato.ZWSACEPTAContrat port = service.getWsBinAccep();
        
        Configuration appConfiguration = new Configuration();
        //Autenticación básica por HTTP - Header (esta autenticacion ya es al hacer el request con datos)
        BindingProvider prov = (BindingProvider)port;
        prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, appConfiguration.getSapWsBafarUser());
        prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, appConfiguration.getSapWsBafarPass());
        
        return port.zfmtrAcceptContract(parameters);
    }
    
    /**
     * Puerto para conexión a Servicio Web SAP -Validacion de Interlocutor en Listas Negras y Buro interno
     * @param parameters
     * @return 
     */
    private static ZfmtrValidaInterlocuResponse zfmtrValidaInterlocu(com.sap.bafar.ws.validainterlocutor.ZfmtrValidaInterlocu parameters) {
        com.sap.bafar.ws.validainterlocutor.ZwsValidaInter_Service service = new com.sap.bafar.ws.validainterlocutor.ZwsValidaInter_Service();
        com.sap.bafar.ws.validainterlocutor.ZwsVALIDAInter port = service.getZBinValidaint();

        Configuration appConfiguration = new Configuration();
        //Autenticación básica por HTTP - Header (esta autenticacion ya es al hacer el request con datos)
        BindingProvider prov = (BindingProvider)port;
        prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, appConfiguration.getSapWsBafarUser());
        prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, appConfiguration.getSapWsBafarPass());

        return port.zfmtrValidaInterlocu(parameters);
    }
    
    /**
     * Puerto para conexión a Servicio Web SAP - Consulta de Dispersion
     * @param parameters
     * @return 
     */
    private static ZfmtrDisperResponse zfmtrDisper(com.sap.bafar.ws.consultadispersion.ZfmtrDisper parameters) {
        com.sap.bafar.ws.consultadispersion.WsDispersion service = new com.sap.bafar.ws.consultadispersion.WsDispersion();
        com.sap.bafar.ws.consultadispersion.ZwsDispersion port = service.getZbDisp();
        
        Configuration appConfiguration = new Configuration();
        //Autenticación básica por HTTP - Header (esta autenticacion ya es al hacer el request con datos)
        BindingProvider prov = (BindingProvider)port;
        prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, appConfiguration.getSapWsBafarUser());
        prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, appConfiguration.getSapWsBafarPass());
        
        return port.zfmtrDisper(parameters);
    }
    
// </editor-fold>
    
}
