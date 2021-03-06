/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.tsp.sct.dao.dao;

import java.util.Date;
import com.tsp.sct.dao.dto.*;
import com.tsp.sct.dao.exceptions.*;

public interface VistaCxcDao
{
	/** 
	 * Returns all rows from the vista_cxc table that match the criteria ''.
	 */
	public VistaCxc[] findAll() throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'C_ID_COMPROBANTE_FISCAL = :cIdComprobanteFiscal'.
	 */
	public VistaCxc[] findWhereCIdComprobanteFiscalEquals(int cIdComprobanteFiscal) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'ID_TIPO_COMPROBANTE = :idTipoComprobante'.
	 */
	public VistaCxc[] findWhereIdTipoComprobanteEquals(int idTipoComprobante) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'C_ID_EMPRESA = :cIdEmpresa'.
	 */
	public VistaCxc[] findWhereCIdEmpresaEquals(int cIdEmpresa) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'C_ID_CLIENTE = :cIdCliente'.
	 */
	public VistaCxc[] findWhereCIdClienteEquals(int cIdCliente) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'C_ID_ESTATUS = :cIdEstatus'.
	 */
	public VistaCxc[] findWhereCIdEstatusEquals(int cIdEstatus) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'ID_FOLIO = :idFolio'.
	 */
	public VistaCxc[] findWhereIdFolioEquals(int idFolio) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'ID_FORMA_PAGO = :idFormaPago'.
	 */
	public VistaCxc[] findWhereIdFormaPagoEquals(int idFormaPago) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'ID_TIPO_PAGO = :idTipoPago'.
	 */
	public VistaCxc[] findWhereIdTipoPagoEquals(int idTipoPago) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'CONDICIONES_PAGO = :condicionesPago'.
	 */
	public VistaCxc[] findWhereCondicionesPagoEquals(String condicionesPago) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'FOLIO_GENERADO = :folioGenerado'.
	 */
	public VistaCxc[] findWhereFolioGeneradoEquals(String folioGenerado) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'FECHA_IMPRESION = :fechaImpresion'.
	 */
	public VistaCxc[] findWhereFechaImpresionEquals(Date fechaImpresion) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'FECHA_CAPTURA = :fechaCaptura'.
	 */
	public VistaCxc[] findWhereFechaCapturaEquals(Date fechaCaptura) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'FECHA_PAGO = :fechaPago'.
	 */
	public VistaCxc[] findWhereFechaPagoEquals(Date fechaPago) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'IMPORTE_SUBTOTAL = :importeSubtotal'.
	 */
	public VistaCxc[] findWhereImporteSubtotalEquals(float importeSubtotal) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'IMPUESTOS = :impuestos'.
	 */
	public VistaCxc[] findWhereImpuestosEquals(float impuestos) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'IMPORTE_NETO = :importeNeto'.
	 */
	public VistaCxc[] findWhereImporteNetoEquals(float importeNeto) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'PARCIALIDAD = :parcialidad'.
	 */
	public VistaCxc[] findWhereParcialidadEquals(String parcialidad) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'CONCEPTO = :concepto'.
	 */
	public VistaCxc[] findWhereConceptoEquals(String concepto) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'ARCHIVO_CFD = :archivoCfd'.
	 */
	public VistaCxc[] findWhereArchivoCfdEquals(String archivoCfd) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'CADENA_ORIGINAL = :cadenaOriginal'.
	 */
	public VistaCxc[] findWhereCadenaOriginalEquals(String cadenaOriginal) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'SELLO_DIGITAL = :selloDigital'.
	 */
	public VistaCxc[] findWhereSelloDigitalEquals(String selloDigital) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'C_COMENTARIOS = :cComentarios'.
	 */
	public VistaCxc[] findWhereCComentariosEquals(String cComentarios) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'ID_DIVISAS = :idDivisas'.
	 */
	public VistaCxc[] findWhereIdDivisasEquals(int idDivisas) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'ID_TIPO_MONEDA = :idTipoMoneda'.
	 */
	public VistaCxc[] findWhereIdTipoMonedaEquals(int idTipoMoneda) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'TIPO_DE_CAMBIO = :tipoDeCambio'.
	 */
	public VistaCxc[] findWhereTipoDeCambioEquals(float tipoDeCambio) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'UUID = :uuid'.
	 */
	public VistaCxc[] findWhereUuidEquals(String uuid) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'SELLO_SAT = :selloSat'.
	 */
	public VistaCxc[] findWhereSelloSatEquals(String selloSat) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'ACUSE_CANCELACION = :acuseCancelacion'.
	 */
	public VistaCxc[] findWhereAcuseCancelacionEquals(String acuseCancelacion) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'DESCUENTO = :descuento'.
	 */
	public VistaCxc[] findWhereDescuentoEquals(float descuento) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'MOTIVO_DESCUENTO = :motivoDescuento'.
	 */
	public VistaCxc[] findWhereMotivoDescuentoEquals(String motivoDescuento) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'FECHA_VIGENCIA = :fechaVigencia'.
	 */
	public VistaCxc[] findWhereFechaVigenciaEquals(String fechaVigencia) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'SECOFI = :secofi'.
	 */
	public VistaCxc[] findWhereSecofiEquals(String secofi) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'NUMERO_FACTURA = :numeroFactura'.
	 */
	public VistaCxc[] findWhereNumeroFacturaEquals(String numeroFactura) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'NO_CERTIFICADO_SAT = :noCertificadoSat'.
	 */
	public VistaCxc[] findWhereNoCertificadoSatEquals(String noCertificadoSat) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'ID_PRODUCTOR = :idProductor'.
	 */
	public VistaCxc[] findWhereIdProductorEquals(int idProductor) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'UNIDAD = :unidad'.
	 */
	public VistaCxc[] findWhereUnidadEquals(String unidad) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'FECHA_TIMBRADO = :fechaTimbrado'.
	 */
	public VistaCxc[] findWhereFechaTimbradoEquals(Date fechaTimbrado) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'CREADO_AUTOMATICAMENTE = :creadoAutomaticamente'.
	 */
	public VistaCxc[] findWhereCreadoAutomaticamenteEquals(int creadoAutomaticamente) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'ID_PEDIDO = :idPedido'.
	 */
	public VistaCxc[] findWhereIdPedidoEquals(int idPedido) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'ID_USUARIO_VENDEDOR = :idUsuarioVendedor'.
	 */
	public VistaCxc[] findWhereIdUsuarioVendedorEquals(int idUsuarioVendedor) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'ID_EMPRESA = :idEmpresa'.
	 */
	public VistaCxc[] findWhereIdEmpresaEquals(int idEmpresa) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'ID_CLIENTE = :idCliente'.
	 */
	public VistaCxc[] findWhereIdClienteEquals(int idCliente) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'CONSECUTIVO_PEDIDO = :consecutivoPedido'.
	 */
	public VistaCxc[] findWhereConsecutivoPedidoEquals(int consecutivoPedido) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'FOLIO_PEDIDO = :folioPedido'.
	 */
	public VistaCxc[] findWhereFolioPedidoEquals(String folioPedido) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'FECHA_PEDIDO = :fechaPedido'.
	 */
	public VistaCxc[] findWhereFechaPedidoEquals(Date fechaPedido) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'TIPO_MONEDA = :tipoMoneda'.
	 */
	public VistaCxc[] findWhereTipoMonedaEquals(String tipoMoneda) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'TIEMPO_ENTREGA_DIAS = :tiempoEntregaDias'.
	 */
	public VistaCxc[] findWhereTiempoEntregaDiasEquals(int tiempoEntregaDias) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'COMENTARIOS = :comentarios'.
	 */
	public VistaCxc[] findWhereComentariosEquals(String comentarios) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'DESCUENTO_TASA = :descuentoTasa'.
	 */
	public VistaCxc[] findWhereDescuentoTasaEquals(double descuentoTasa) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'DESCUENTO_MONTO = :descuentoMonto'.
	 */
	public VistaCxc[] findWhereDescuentoMontoEquals(double descuentoMonto) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'SUBTOTAL = :subtotal'.
	 */
	public VistaCxc[] findWhereSubtotalEquals(double subtotal) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'TOTAL = :total'.
	 */
	public VistaCxc[] findWhereTotalEquals(double total) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'DESCUENTO_MOTIVO = :descuentoMotivo'.
	 */
	public VistaCxc[] findWhereDescuentoMotivoEquals(String descuentoMotivo) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'FECHA_ENTREGA = :fechaEntrega'.
	 */
	public VistaCxc[] findWhereFechaEntregaEquals(Date fechaEntrega) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'FECHA_TENTATIVA_PAGO = :fechaTentativaPago'.
	 */
	public VistaCxc[] findWhereFechaTentativaPagoEquals(Date fechaTentativaPago) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'SALDO_PAGADO = :saldoPagado'.
	 */
	public VistaCxc[] findWhereSaldoPagadoEquals(double saldoPagado) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'ADELANTO = :adelanto'.
	 */
	public VistaCxc[] findWhereAdelantoEquals(double adelanto) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'ID_COMPROBANTE_FISCAL = :idComprobanteFiscal'.
	 */
	public VistaCxc[] findWhereIdComprobanteFiscalEquals(int idComprobanteFiscal) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'ID_ESTATUS_PEDIDO = :idEstatusPedido'.
	 */
	public VistaCxc[] findWhereIdEstatusPedidoEquals(short idEstatusPedido) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'LATITUD = :latitud'.
	 */
	public VistaCxc[] findWhereLatitudEquals(double latitud) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'LONGITUD = :longitud'.
	 */
	public VistaCxc[] findWhereLongitudEquals(double longitud) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'FOLIO_PEDIDO_MOVIL = :folioPedidoMovil'.
	 */
	public VistaCxc[] findWhereFolioPedidoMovilEquals(String folioPedidoMovil) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'NOMBRE_IMAGEN_FIRMA = :nombreImagenFirma'.
	 */
	public VistaCxc[] findWhereNombreImagenFirmaEquals(String nombreImagenFirma) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'IS_MODIFICADO_CONSOLA = :isModificadoConsola'.
	 */
	public VistaCxc[] findWhereIsModificadoConsolaEquals(short isModificadoConsola) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'BONIFICACION_DEVOLUCION = :bonificacionDevolucion'.
	 */
	public VistaCxc[] findWhereBonificacionDevolucionEquals(double bonificacionDevolucion) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'ID_USUARIO_CONDUCTOR_ASIGNADO = :idUsuarioConductorAsignado'.
	 */
	public VistaCxc[] findWhereIdUsuarioConductorAsignadoEquals(int idUsuarioConductorAsignado) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'ID_USUARIO_VENDEDOR_ASIGNADO = :idUsuarioVendedorAsignado'.
	 */
	public VistaCxc[] findWhereIdUsuarioVendedorAsignadoEquals(int idUsuarioVendedorAsignado) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'ID_USUARIO_VENDEDOR_REASIGNADO = :idUsuarioVendedorReasignado'.
	 */
	public VistaCxc[] findWhereIdUsuarioVendedorReasignadoEquals(int idUsuarioVendedorReasignado) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the criteria 'FECHA_LIMITE_REASIGANCION = :fechaLimiteReasigancion'.
	 */
	public VistaCxc[] findWhereFechaLimiteReasigancionEquals(Date fechaLimiteReasigancion) throws VistaCxcDaoException;

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows);

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows();

	/** 
	 * Returns all rows from the vista_cxc table that match the specified arbitrary SQL statement
	 */
	public VistaCxc[] findByDynamicSelect(String sql, Object[] sqlParams) throws VistaCxcDaoException;

	/** 
	 * Returns all rows from the vista_cxc table that match the specified arbitrary SQL statement
	 */
	public VistaCxc[] findByDynamicWhere(String sql, Object[] sqlParams) throws VistaCxcDaoException;

}
