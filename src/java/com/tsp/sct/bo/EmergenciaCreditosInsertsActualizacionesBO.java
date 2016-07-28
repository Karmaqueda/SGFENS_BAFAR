package com.tsp.sct.bo;

import java.sql.Connection;
import java.util.Date;

import com.tsp.sct.dao.dto.EmergenciaCredito;
import com.tsp.sct.dao.dto.EmergenciaCreditoFacturaRealizada;
import com.tsp.sct.dao.dto.EmergenciaCreditoPk;
import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.dao.dto.EmpresaPermisoAplicacion;
import com.tsp.sct.dao.exceptions.EmergenciaCreditoFacturaRealizadaDaoException;
import com.tsp.sct.dao.exceptions.EmpresaPermisoAplicacionDaoException;
import com.tsp.sct.dao.jdbc.EmergenciaCreditoDaoImpl;
import com.tsp.sct.dao.jdbc.EmergenciaCreditoFacturaRealizadaDaoImpl;
import com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl;
import com.tsp.sgfens.sesion.ComprobanteFiscalSesion;

public class EmergenciaCreditosInsertsActualizacionesBO {
	
	 private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public EmergenciaCreditosInsertsActualizacionesBO(){}
    
    public EmergenciaCreditosInsertsActualizacionesBO(Connection conn){
    	this.conn = conn;
    }
    
    public void actualizaCreditosRegistros(EmpresaPermisoAplicacion empresaPermiso, ComprobanteFiscalSesion comp, Empresa empresa, float montoTotalComprobante){
    	
    	//ACTUALIZAMOS EL CONTADOR DE CREDITOS DE EMERGENCIA
    	empresaPermiso.setCreditosEmergenciaXPagar(empresaPermiso.getCreditosEmergenciaXPagar() + 1);    	
    	try {
            new EmpresaPermisoAplicacionDaoImpl(this.conn).update(empresaPermiso.createPk(), empresaPermiso);
        } catch (EmpresaPermisoAplicacionDaoException e) {			
            e.printStackTrace();
        }
		
		//RECUPERAMOS EL REGISTRO VIGENTE DE "EMERGENCIA_CREDITO" PARA OBTENER EL ID
		int idEmergenciaCreditoPK = 0;
		try {
			EmergenciaCredito credito = new EmergenciaCreditoBO(this.conn).getEmergenciaCreditoRegistroContador(empresa.getIdEmpresa());
			if(credito != null){
				System.out.println("++++++++++++++++++++ HAY REGISTRO VIGENTE DE EMERGENCIA_CREDITO!!!");
				idEmergenciaCreditoPK = credito.getIdEmergencia();
				//actualizamos los créditos que va consumiendo:
				credito.setCreditosOcupados(credito.getCreditosOcupados() + 1);
				new EmergenciaCreditoDaoImpl(this.conn).update(credito.createPk(), credito);
			}else{
				System.out.println("++++++++++++++++++++ NO HAY REGISTRO VIGENTE DE EMERGENCIA_CREDITO!!!, INSERTANDO UNO NUEVO");
				EmergenciaCredito creditoNuevo = new EmergenciaCredito();
				creditoNuevo.setIdEmpresa(empresa.getIdEmpresaPadre());
				creditoNuevo.setFechaInicio(new Date());
				creditoNuevo.setMontoPagado(0);
				creditoNuevo.setCreditosOcupados(1);
				creditoNuevo.setRfcEmpresa(empresa.getRfc());
				EmergenciaCreditoPk emergenciaCreditoPk = new EmergenciaCreditoDaoImpl(this.conn).insert(creditoNuevo);
				idEmergenciaCreditoPK = emergenciaCreditoPk.getIdEmergencia();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//INSERTAMOS EL REGISTRO DE "EMERGENCIA_CREDITO_FACTURA_REALIZADA"
		EmergenciaCreditoFacturaRealizada facturaRealizada = new EmergenciaCreditoFacturaRealizada();
		facturaRealizada.setIdEmergencia(idEmergenciaCreditoPK);
		facturaRealizada.setFecha(new Date());
		facturaRealizada.setIdEmpresa(empresa.getIdEmpresaPadre());
		facturaRealizada.setRfcEmisor(empresa.getRfc());
		facturaRealizada.setIdClienteReceptor(comp.getCliente().getIdCliente());
		facturaRealizada.setRfcReceptor(comp.getCliente().getRfcCliente());
		facturaRealizada.setMontoFactura(montoTotalComprobante);
		facturaRealizada.setUuid(comp.getUuid());
		facturaRealizada.setIdEstatus(1);//1 Activo, 2 Cancelado
		try {
			new EmergenciaCreditoFacturaRealizadaDaoImpl(this.conn).insert(facturaRealizada);
			System.out.println("++++++++++++++++++++ REGISTRO INSERTANDO DE CREDITO UTILIZADO");
		} catch (EmergenciaCreditoFacturaRealizadaDaoException e) {			
			e.printStackTrace();
		}
		
		
		
    }

}
