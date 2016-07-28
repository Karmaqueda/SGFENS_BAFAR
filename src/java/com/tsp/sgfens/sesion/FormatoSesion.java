/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.sesion;

import com.tsp.sct.bo.ConceptoBO;
import com.tsp.sct.bo.EmpleadoBO;
import com.tsp.sct.bo.EmpleadoInventarioRepartidorBO;
import com.tsp.sct.bo.EmpresaBO;
import com.tsp.sct.bo.ExistenciaAlmacenBO;
import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.dto.Concepto;
import com.tsp.sct.dao.dto.EmpleadoInventarioRepartidor;
import com.tsp.sct.dao.dto.EmpresaPermisoAplicacion;
import com.tsp.sct.dao.dto.ExistenciaAlmacen;
import com.tsp.sct.dao.dto.Movimiento;
import com.tsp.sct.dao.jdbc.EmpleadoInventarioRepartidorDaoImpl;
import com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl;
import com.tsp.sct.dao.jdbc.MovimientoDaoImpl;
import com.tsp.sct.util.Converter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ISCesarMartinez
 */
public class FormatoSesion {
    
    private String comentarios="";
    private Cliente  cliente = null;
    private ArrayList<ProductoSesion> listaProducto = new ArrayList<ProductoSesion>();
    private ArrayList<ServicioSesion> listaServicio = new ArrayList<ServicioSesion>();
    private ArrayList<ImpuestoSesion> listaImpuesto = new ArrayList<ImpuestoSesion>();
    private ArrayList<PercepcionSesion> listaPercepcion = new ArrayList<PercepcionSesion>();
    private ArrayList<DeduccionSesion> listaDeduccion = new ArrayList<DeduccionSesion>();
    private double descuento_tasa = 0; 
    private String descuento_motivo = "";
    private String tipo_moneda = "MXN";
    private Connection conn = null;
    
    public FormatoSesion() {
        descuento_tasa = 0;
    }

    public String getDescuento_motivo() {
        return descuento_motivo;
    }

    public void setDescuento_motivo(String descuento_motivo) {
        this.descuento_motivo = descuento_motivo;
    }

    public String getTipo_moneda() {
        return tipo_moneda;
    }

    public void setTipo_moneda(String tipo_moneda) {
        this.tipo_moneda = tipo_moneda;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public ArrayList<ProductoSesion> getListaProducto() {
        if (listaProducto==null)
            listaProducto = new ArrayList<ProductoSesion>();
        return listaProducto;
    }

    public void setListaProducto(ArrayList<ProductoSesion> listaProducto) {
        this.listaProducto = listaProducto;
    }

    public ArrayList<PercepcionSesion> getListaPercepcion() {
        if (listaPercepcion==null)
            listaPercepcion = new ArrayList<PercepcionSesion>();
        return listaPercepcion;
    }

    public void setListaDeduccion(ArrayList<DeduccionSesion> listaDeduccion) {
        this.listaDeduccion = listaDeduccion;
    }

    public ArrayList<DeduccionSesion> getListaDeduccion() {
        if (listaDeduccion==null)
            listaDeduccion = new ArrayList<DeduccionSesion>();
        return listaDeduccion;
    }

    public void setListaPercepcion(ArrayList<PercepcionSesion> listaPercepcion) {
        this.listaPercepcion = listaPercepcion;
    }   

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ArrayList<ServicioSesion> getListaServicio() {
        if (listaServicio==null)
            listaServicio = new ArrayList<ServicioSesion>();
        return listaServicio;
    }

    public void setListaServicio(ArrayList<ServicioSesion> listaServicio) {
        this.listaServicio = listaServicio;
    }

    public ArrayList<ImpuestoSesion> getListaImpuesto() {
        if (listaImpuesto==null)
            listaImpuesto = new ArrayList<ImpuestoSesion>();
        return listaImpuesto;
    }

    public void setListaImpuesto(ArrayList<ImpuestoSesion> listaImpuesto) {
        this.listaImpuesto = listaImpuesto;
    }

    public double getDescuento_tasa() {
        return descuento_tasa;
    }

    public void setDescuento_tasa(double descuento_tasa) {
        if (descuento_tasa<0)
            descuento_tasa = 0;
        if (descuento_tasa>100)
            descuento_tasa = 0;
        this.descuento_tasa = descuento_tasa;
    }
    
    public BigDecimal getSubTotalProductos(){
        BigDecimal montoProductos = BigDecimal.ZERO;
        
        for (ProductoSesion item : getListaProducto()){
            BigDecimal itemMonto = new BigDecimal(item.getMonto()).setScale(2, RoundingMode.HALF_UP);
            
            montoProductos = montoProductos.add(itemMonto).setScale(2, RoundingMode.HALF_UP);
        }
        
        return montoProductos;
    }
    
    public BigDecimal getSubTotalServicios(){
        BigDecimal montoServicios = BigDecimal.ZERO;
        
        for (ServicioSesion item : getListaServicio()){
            BigDecimal itemMonto = new BigDecimal(item.getMonto()).setScale(2, RoundingMode.HALF_UP);
            
            montoServicios = montoServicios.add(itemMonto).setScale(2, RoundingMode.HALF_UP);
        }
        
        return montoServicios;
    }
    
    public BigDecimal getSubTotalImpuestos_Traslados(){
        
        BigDecimal montoImpuestos = BigDecimal.ZERO;
        
        BigDecimal subtotal = calculaSubTotal().subtract(getDescuentoImporte());//subtotalProductos.add(subtotalServicios).setScale(2, RoundingMode.HALF_UP);
        
        for (ImpuestoSesion item : getListaImpuesto()){
            if (item.isTrasladado()){
                BigDecimal item_porcentaje = new BigDecimal( (item.getPorcentaje()/100) ).setScale(2, RoundingMode.HALF_UP);
                BigDecimal item_monto =  subtotal.multiply(item_porcentaje).setScale(2, RoundingMode.HALF_UP);

                montoImpuestos = montoImpuestos.add(item_monto).setScale(2, RoundingMode.HALF_UP);
            }
        }
        
        return montoImpuestos;
    }
    
    public BigDecimal getSubTotalImpuestos_Retenciones(){
        
        BigDecimal montoImpuestos = BigDecimal.ZERO;
        
        BigDecimal subtotal = calculaSubTotal().subtract(getDescuentoImporte());//subtotalProductos.add(subtotalServicios).setScale(2, RoundingMode.HALF_UP);
        
        for (ImpuestoSesion item : getListaImpuesto()){
            if (!item.isTrasladado()){
                BigDecimal item_porcentaje = new BigDecimal( (item.getPorcentaje()/100) ).setScale(2, RoundingMode.HALF_UP);
                BigDecimal item_monto =  subtotal.multiply(item_porcentaje).setScale(2, RoundingMode.HALF_UP);

                montoImpuestos = montoImpuestos.add(item_monto).setScale(2, RoundingMode.HALF_UP);
            }
        }
        
        return montoImpuestos;
    }
    
    /**
     * Obtiene el monto calculado para el impuesto indicado de la lista en sesion
     * @param indexListImpuestos index del impuesto en la lista en sesion
     * @return BigDecimal con el monto calculado
     */
    public BigDecimal getMontoImpuesto(int indexListImpuestos){
        BigDecimal montoImpuesto = BigDecimal.ZERO;
        
        BigDecimal subtotal = calculaSubTotal().subtract(getDescuentoImporte());//subtotalProductos.add(subtotalServicios).setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal item_porcentaje = new BigDecimal( (listaImpuesto.get(indexListImpuestos).getPorcentaje()/100) ).setScale(2, RoundingMode.HALF_UP);
        montoImpuesto =  subtotal.multiply(item_porcentaje).setScale(2, RoundingMode.HALF_UP);
        
        return montoImpuesto;
    }
    
    /**
     * Calcula el subtotal (productos + servicios)
     * @return BigDecimal, importe del subtotal
     */
    public BigDecimal calculaSubTotal(){
        BigDecimal subtotalProductos = getSubTotalProductos();
        BigDecimal subtotalServicios = getSubTotalServicios();
        
        BigDecimal subtotal = subtotalProductos.add(subtotalServicios).setScale(2, RoundingMode.HALF_UP);
        
        return subtotal;
    }
    
    /**
     * Calcula y retorna el valor del descuento aplicable 
     * al subtotal (productos + servicios)
     * @return BigDecimal, importe de descuento
     */
    public BigDecimal getDescuentoImporte(){
        BigDecimal descuentoImporte = BigDecimal.ZERO;
        
        BigDecimal subtotal = calculaSubTotal();
        BigDecimal descuentoPorcentaje = new BigDecimal( this.descuento_tasa / 100 ).setScale(2, RoundingMode.HALF_UP);
         
        descuentoImporte = subtotal.multiply(descuentoPorcentaje).setScale(2, RoundingMode.HALF_UP);
        
        return descuentoImporte;
    }
    
    /**
     * Calcula el total de la cotizacion
     * Total = Subtotal – Descuentos + Impuestos Trasladados – Impuestos Retenidos.
     * @param BigDecimal Total de la Cotizacion
     */
    public BigDecimal calculaTotal(){
        BigDecimal total = BigDecimal.ZERO;
        
        // a = subtotal - descuentos
        BigDecimal subTotalConDescuento = calculaSubTotal().subtract(getDescuentoImporte()).setScale(2, RoundingMode.HALF_UP);
        
        // b = a + impuestos trasladados
        total = subTotalConDescuento.add(getSubTotalImpuestos_Traslados()).setScale(2, RoundingMode.HALF_UP);
        
        // b = b - impuestos retenidos
        total = total.subtract(getSubTotalImpuestos_Retenciones()).setScale(2, RoundingMode.HALF_UP);
        
        return total;
    }
    
    public BigDecimal calculaTotalNomina(List<ImpuestoSesion> impuestosSesion){
        BigDecimal total = BigDecimal.ZERO;
        
        // a = subtotal - descuentos
        BigDecimal subTotalConDescuento = calculaSubTotal().subtract(getDescuentoImporte()).setScale(2, RoundingMode.HALF_UP);
        
        // b = a + impuestos trasladados
        total = subTotalConDescuento.add(getSubTotalImpuestos_Traslados()).setScale(2, RoundingMode.HALF_UP);
        
        // b = b - impuestos retenidos
        //total = total.subtract(getSubTotalImpuestos_Retenciones()).setScale(2, RoundingMode.HALF_UP);
        
        double montoTotalRetencionISR = 0;
         for (ImpuestoSesion impuestoSesion : impuestosSesion){
             if(!impuestoSesion.isTrasladado()){
                 if(impuestoSesion.getNombre().equals("ISR"))
                 montoTotalRetencionISR += impuestoSesion.getMonto();
             }
         }
        total = total.subtract(new BigDecimal(montoTotalRetencionISR)).setScale(2, RoundingMode.HALF_UP);
        return total;
    }
    
    
    /**
     * Verifica si los productos seleccionados en la sesion
     * tienen suficiente stock para ser surtidos en un pedido o factura
     * @return boolean, true en caso de tener existencia, false en caso contrario
     */
    public boolean verificaExistenciaProductos() throws Exception{
        boolean exito = false;
        
        String msgError = "";
        try{
            ConceptoBO conceptoBO;
            Concepto conceptoDto;
            
            //Recorremos por primera vez para asegurar integridad
            //Solo en caso de que todos los productos tengan stock suficiente se daran de baja TODOS
            //  de lo contrario, no se dara de baja ninguno.
            for (ProductoSesion prodSesion : this.getListaProducto()){
                conceptoBO = new ConceptoBO(prodSesion.getIdProducto(), this.conn);
                conceptoDto = conceptoBO.getConcepto();
                
                ExistenciaAlmacen almPrincipal = null;
                ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(this.conn);
                
                if (conceptoDto!=null){
                    //Verificamos si se consulta el stock o no
                    EmpresaBO empresaBO = new EmpresaBO(this.conn);
                    EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl().findByPrimaryKey(empresaBO.getEmpresaMatriz(conceptoBO.getConcepto().getIdEmpresa()).getIdEmpresa());     
                    if (empresaPermisoAplicacionDto!=null){
                        if(empresaPermisoAplicacionDto.getRevisionCantidadProducto() == 1){
                            
                            almPrincipal = exisAlmBO.getExistenciaProductoAlmacen(prodSesion.getIdAlmacen(), conceptoDto.getIdConcepto());
                            
                            BigDecimal numArticulosDisponibles = (new BigDecimal(almPrincipal!=null?almPrincipal.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal prodSesionCantidad = (new BigDecimal(prodSesion.getCantidad())).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal stockTotal = numArticulosDisponibles.subtract(prodSesionCantidad);
                            
                            double nuevoStock = stockTotal.doubleValue();
                            if (nuevoStock<0){
                                msgError+="<ul>El stock del articulo '" + conceptoBO.getNombreConceptoLegible() + "' es insuficiente para cubrir la operación.<br/>No. de Articulos disponibles: " + (almPrincipal!=null?almPrincipal.getExistencia():0);
                            }
                        }
                    }                    
                }
            }
            
            if (msgError.trim().equals("")){
                exito = true;
            }else{
                exito = false;
                throw new Exception(msgError);
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        
        return exito;
    }
    
    /**
     * Da salida del almacen a los productos específicados en la factura
     * Los resta del stock actual
     * 
     */
    public boolean salidaAlmacenProductos() throws Exception{
        boolean exito = false;
        
        String msgError = "";
        try{
            ConceptoBO conceptoBO;
            Concepto conceptoDto;
            ArrayList<Concepto> listaConceptos = new ArrayList<Concepto>();
            ExistenciaAlmacen almPrincipal = null;
            ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(this.conn);
            
            
            //Recorremos por primera vez para asegurar integridad
            //Solo en caso de que todos los productos tengan stock suficiente se daran de baja TODOS
            //  de lo contrario, no se dara de baja ninguno.
            for (ProductoSesion prodSesion : this.getListaProducto()){
                conceptoBO = new ConceptoBO(prodSesion.getIdProducto(),this.conn);
                conceptoDto = conceptoBO.getConcepto();
                
                if (conceptoDto!=null){
                     //Verificamos si se consulta el stock o no
                    EmpresaBO empresaBO = new EmpresaBO(this.conn);
                    EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl().findByPrimaryKey(empresaBO.getEmpresaMatriz(conceptoBO.getConcepto().getIdEmpresa()).getIdEmpresa());     
                    if (empresaPermisoAplicacionDto != null) {
                        if (empresaPermisoAplicacionDto.getRevisionCantidadProducto() == 1) {
                            
                            almPrincipal = exisAlmBO.getExistenciaProductoAlmacen(prodSesion.getIdAlmacen(), conceptoDto.getIdConcepto());
                            
                            BigDecimal numArticulosDisponibles = (new BigDecimal(almPrincipal!=null?almPrincipal.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal prodSesionCantidad = (new BigDecimal(prodSesion.getCantidad())).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal stockTotal = numArticulosDisponibles.subtract(prodSesionCantidad);
                            
                            double nuevoStock = stockTotal.doubleValue();
                            if (nuevoStock < 0) {
                                msgError += "<ul>El stock del articulo '" + conceptoBO.getNombreConceptoLegible() + "' en almacen Principal es insuficiente para cubrir la operación.<br/>No. de Articulos disponibles: " + (almPrincipal!=null?almPrincipal.getExistencia():0);
                            } else {
                                listaConceptos.add(conceptoDto);
                            }
                        }
                    }
                }
            }
            
            //Se recorre por segunda ocasion
            //  Una vez que aseguramos la integridad y que todos los productos tienen stock suficiente
            //  procedemos a darlos de baja del almacen (movimientos) y a actualizar su registro único.
            if (listaConceptos.size()>0 && msgError.equals("") ){
                for (ProductoSesion prodSesion : this.getListaProducto()){
                    conceptoBO = new ConceptoBO(prodSesion.getIdProducto(),this.conn);
                    conceptoDto = conceptoBO.getConcepto();

                    if (conceptoDto!=null){
                        
                        almPrincipal = exisAlmBO.getExistenciaProductoAlmacen(prodSesion.getIdAlmacen(), conceptoDto.getIdConcepto());
                        
                        BigDecimal numArticulosDisponibles = (new BigDecimal(almPrincipal!=null?almPrincipal.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal prodSesionCantidad = (new BigDecimal(prodSesion.getCantidad())).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal stockTotal = numArticulosDisponibles.subtract(prodSesionCantidad);
                        
                        double nuevoStock = stockTotal.doubleValue();                
                        if (nuevoStock<0){
                            msgError+="<ul>El stock del articulo '" + conceptoBO.getNombreConceptoLegible() + "' es insuficiente para cubrir la operación.<br/>No. de Articulos disponibles: " + (almPrincipal!=null?almPrincipal.getExistencia():0);
                        }else{
                            //Creamos registro de movimiento de almacen
                            Movimiento movimientoDto = new Movimiento();
                            MovimientoDaoImpl movimientosDaoImpl = new MovimientoDaoImpl();
                            int idEmpresa = conceptoDto.getIdEmpresa();

                            movimientoDto.setIdEmpresa(idEmpresa);
                            movimientoDto.setTipoMovimiento("SALIDA");
                            movimientoDto.setNombreProducto(conceptoBO.getNombreConceptoLegible());
                            movimientoDto.setContabilidad(prodSesion.getCantidad());
                            movimientoDto.setIdProveedor(-1);
                            movimientoDto.setOrdenCompra("");
                            movimientoDto.setNumeroGuia("");
                            //movimientoDto.setIdAlmacen(alamacenMovimiento);
                            movimientoDto.setIdAlmacen(prodSesion.getIdAlmacen());
                            movimientoDto.setConceptoMovimiento("Operación de Venta (Pedido/Factura)");                
                            movimientoDto.setFechaRegistro(new Date());
                            movimientoDto.setIdConcepto(conceptoDto.getIdConcepto());

                            /**
                            * Realizamos el insert
                            */
                            movimientosDaoImpl.insert(movimientoDto);

                            //Actualizamos registro único de concepto
                            
                            almPrincipal.setExistencia(nuevoStock);
                            new ExistenciaAlmacenBO(this.conn).updateBD(almPrincipal);
                            
                            /*conceptoDto.setNumArticulosDisponibles(nuevoStock);
                            conceptoBO.updateBD(conceptoDto);*/
                        }
                    }else{
                        msgError += "<ul>El producto con id " + prodSesion.getIdProducto() + "no existe en la base de datos, probablemente fue eliminado en otro sesion alterna.";
                    }

                }
            }
            
            if (msgError.trim().equals("")){
                exito = true;
            }else{
                exito = false;
                throw new Exception(msgError);
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        
        return exito;
    }
    
    /**
     * Cancela la salida del almacen de los productos específicados en la factura
     * Los suma al stock actual
     * @return true en caso de éxito, false en caso contrario
     */
    public boolean cancelaSalidaAlmacenProductos() throws Exception{
        boolean exito = false;
        
        String msgError="";
        try{
            ConceptoBO conceptoBO;
            Concepto conceptoDto;
            ExistenciaAlmacen almPrincipal = null;
            ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(this.conn);
            
            for (ProductoSesion prodSesion : this.getListaProducto()){
                conceptoBO = new ConceptoBO(prodSesion.getIdProducto(),this.conn);
                conceptoDto = conceptoBO.getConcepto();

                if (conceptoDto!=null){
                    
                    almPrincipal = exisAlmBO.getExistenciaProductoAlmacenPrincipal(conceptoDto.getIdEmpresa(), conceptoDto.getIdConcepto());
                    
                    BigDecimal numArticulosDisponibles = (new BigDecimal(almPrincipal!=null?almPrincipal.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal prodSesionCantidad = (new BigDecimal(prodSesion.getCantidad())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal stockTotal = numArticulosDisponibles.add(prodSesionCantidad);
                    
                    double nuevoStock = stockTotal.doubleValue();                
                    {
                        //Creamos registro de movimiento de almacen
                        Movimiento movimientoDto = new Movimiento();
                        MovimientoDaoImpl movimientosDaoImpl = new MovimientoDaoImpl();
                        int idEmpresa = conceptoDto.getIdEmpresa();

                        movimientoDto.setIdEmpresa(idEmpresa);
                        movimientoDto.setTipoMovimiento("ENTRADA");
                        movimientoDto.setNombreProducto(conceptoBO.getNombreConceptoLegible());
                        movimientoDto.setContabilidad((int)prodSesion.getCantidad());
                        movimientoDto.setIdProveedor(-1);
                        movimientoDto.setOrdenCompra("");
                        movimientoDto.setNumeroGuia("");
                        //movimientoDto.setIdAlmacen(alamacenMovimiento);
                        movimientoDto.setIdAlmacen(almPrincipal.getIdAlmacen());
                        movimientoDto.setConceptoMovimiento("Cancelación de Venta (Pedido/Factura)");                
                        movimientoDto.setFechaRegistro(new Date());
                        movimientoDto.setIdConcepto(conceptoDto.getIdConcepto());

                        /**
                        * Realizamos el insert
                        */
                        movimientosDaoImpl.insert(movimientoDto);

                        //Actualizamos registro único de concepto                                            
                        almPrincipal.setExistencia(nuevoStock);
                        new ExistenciaAlmacenBO(this.conn).updateBD(almPrincipal);
                        
                        //Actualizamos registro único de concepto
                        /*conceptoDto.setNumArticulosDisponibles(nuevoStock);
                        conceptoBO.updateBD(conceptoDto);*/
                    }
                }else{
                    msgError += "<ul>El producto con id " + prodSesion.getIdProducto() + "no existe en la base de datos, probablemente fue eliminado en otro sesion alterna.";
                }
            }
            
            if (msgError.trim().equals("")){
                exito = true;
            }else{
                exito = false;
                throw new Exception(msgError);
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        
        return exito;
    }
    
    
    /**
     * Da salida del inventario personalizado del Empleado Vendedor/Repartidor
     * los productos solicitados
     * @return
     * @throws Exception 
     */
    public boolean salidaAlmacenProductosInventarioEmpleado(int idEmpleado) throws Exception{
        boolean exito = false;
        
        String msgError = "";
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(idEmpleado, this.conn);
            EmpleadoInventarioRepartidorBO emInventarioRepartidorBO = new EmpleadoInventarioRepartidorBO(this.conn);
            EmpleadoInventarioRepartidor emInventarioRepartidorDto;
            EmpleadoInventarioRepartidorDaoImpl emInventarioRepartidorDao = new EmpleadoInventarioRepartidorDaoImpl(this.conn);
            ConceptoBO conceptoBO;
            
            int idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
            int productosEncontrados = 0;
            
            //Recorremos por primera vez para asegurar integridad
            //Solo en caso de que todos los productos tengan stock suficiente se daran de baja TODOS
            //  de lo contrario, no se dara de baja ninguno.
            for (ProductoSesion prodSesion : this.getListaProducto()){
                boolean setearInfoPeso = false;
                EmpleadoInventarioRepartidor[] emInventarios = null;
                if(prodSesion.getIdInventarioEmpleado() <=0){
                    emInventarios = emInventarioRepartidorBO.findEmpleadoInventarioRepartidors(-1, idEmpleado, 0, 0, " AND ID_CONCEPTO = " + prodSesion.getIdProducto());
                }else{
                    emInventarios = emInventarioRepartidorBO.findEmpleadoInventarioRepartidors(-1, idEmpleado, 0, 0, " AND ID_CONCEPTO = " + prodSesion.getIdProducto() + " AND ID_INVENTARIO = " + prodSesion.getIdInventarioEmpleado());
                    setearInfoPeso = true;
                }
                conceptoBO = new ConceptoBO(prodSesion.getIdProducto(), this.conn);
                
                if (emInventarios != null && emInventarios.length>0){
                    emInventarioRepartidorDto = emInventarios[0];
                    
                     //Verificamos si se verifica el stock o no
                    EmpresaBO empresaBO = new EmpresaBO(this.conn);
                    EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl().findByPrimaryKey(empresaBO.getEmpresaMatriz(idEmpresa).getIdEmpresa());     
                    if (empresaPermisoAplicacionDto != null) {
                        if (empresaPermisoAplicacionDto.getRevisionCantidadProducto() == 1) {

                            double nuevoStock = emInventarioRepartidorDto.getCantidad() - prodSesion.getCantidadEntregada();
                            double nuevoStockPeso = emInventarioRepartidorDto.getExistenciaGranel()- prodSesion.getCantidadEntregadaPeso();
                            if (nuevoStock < 0 || nuevoStockPeso<0 ) {
                                msgError += "<ul>El stock del empleado, del articulo '" + conceptoBO.getNombreConceptoLegible() + "' es insuficiente para cubrir la operación."
                                        + "<br/>No. de Articulos disponibles del repartidor: " + emInventarioRepartidorDto.getCantidad()
                                        + (prodSesion.getCantidadPeso()>0?"<br/>Peso Neto Stock:" + emInventarioRepartidorDto.getExistenciaGranel():"");
                                conceptoBO.enviaNotificacionStockMinimoSincronizaPedido(conceptoBO.getConcepto(),empleadoBO.getEmpleado());
                                
                            } else {
                                productosEncontrados++;
                            }
                            
                        }
                    }
                }
            }
            
            //Se recorre por segunda ocasion
            //  Una vez que aseguramos la integridad y que todos los productos tienen stock suficiente
            //  procedemos a darlos de baja del inventario del empleado.
            if (productosEncontrados>0 && msgError.equals("") ){
                for (ProductoSesion prodSesion : this.getListaProducto()){
                    //boolean setearInfoPeso = false;
                    EmpleadoInventarioRepartidor[] emInventarios = null;
                    if(prodSesion.getIdInventarioEmpleado() <=0){
                        emInventarios = emInventarioRepartidorBO.findEmpleadoInventarioRepartidors(-1, idEmpleado, 0, 0, " AND ID_CONCEPTO = " + prodSesion.getIdProducto());
                    }else{
                        emInventarios = emInventarioRepartidorBO.findEmpleadoInventarioRepartidors(-1, idEmpleado, 0, 0, " AND ID_CONCEPTO = " + prodSesion.getIdProducto() + " AND ID_INVENTARIO = " + prodSesion.getIdInventarioEmpleado());
                        //setearInfoPeso = true;
                    }
                    conceptoBO = new ConceptoBO(prodSesion.getIdProducto(), this.conn);

                    if (emInventarios != null && emInventarios.length>0){
                        emInventarioRepartidorDto = emInventarios[0];
                        
                        System.out.println("-------------a id producto" + emInventarioRepartidorDto.getIdConcepto() + "cantidad actual : "+emInventarioRepartidorDto.getCantidad());
                        System.out.println("-------------a id producto" + prodSesion.getIdProducto() + "cantidad restar : "+prodSesion.getCantidad());
                        
                        double nuevoStock = emInventarioRepartidorDto.getCantidad() - prodSesion.getCantidadEntregada();
                        double nuevoStockPeso = emInventarioRepartidorDto.getExistenciaGranel();//////////////////////
                        
                        System.out.println("-------------a id producto" + emInventarioRepartidorDto.getIdConcepto() + "Peso actual : "+emInventarioRepartidorDto.getPeso());
                        System.out.println("-------------a id producto" + emInventarioRepartidorDto.getIdConcepto() + "Peso restar : "+prodSesion.getCantidadEntregadaPeso());
                        
                        //if (setearInfoPeso)////////////////////////////////////                            
                        //nuevoPeso = emInventarioRepartidorDto.getExistenciaGranel()- prodSesion.getCantidadEntregadaPeso();//////////
                        if (prodSesion.getCantidadPeso()>0)
                            nuevoStockPeso = Converter.roundDouble(emInventarioRepartidorDto.getExistenciaGranel()) - Converter.roundDouble(prodSesion.getCantidadEntregadaPeso());
                        
                        if (nuevoStock<0){
                            msgError += "<ul>El stock del empleado, del articulo '" + conceptoBO.getNombreConceptoLegible() + "' es insuficiente para cubrir la operación."
                                        + "<br/>No. de Articulos disponibles del repartidor: " + emInventarioRepartidorDto.getCantidad()
                                        + (prodSesion.getCantidadPeso()>0?"<br/>Peso Neto Stock:" + emInventarioRepartidorDto.getExistenciaGranel():"");
                            conceptoBO.enviaNotificacionStockMinimoSincronizaPedido(conceptoBO.getConcepto(),empleadoBO.getEmpleado());
                        }else{
                            //--Creamos registro de movimiento de almacen
                            //--Para inventario de empleado NO aplica
                            //...
                            
                            //Actualizamos registro único de inventario de empleado
                            emInventarioRepartidorDto.setCantidad(nuevoStock);
                            //if(setearInfoPeso)////////////
                            //emInventarioRepartidorDto.setPeso(nuevoPeso);///////////////////
                            emInventarioRepartidorDto.setExistenciaGranel(Converter.roundDouble(nuevoStockPeso));
                            emInventarioRepartidorDao.update(emInventarioRepartidorDto.createPk(), emInventarioRepartidorDto);
                            
                            
                        }
                        
                    }else{
                        msgError += "<ul>El producto con id " + prodSesion.getIdProducto() + "no existe en el stock del empleado repartidor, posiblemente fue restado en una sesion alterna.";
                    }

                }
            }
            
            if (msgError.trim().equals("")){
                exito = true;
            }else{
                exito = false;
                throw new Exception(msgError);
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        
        return exito;
    }
    
    /**
     * Cancela la salida del inventario personalizado del Empleado Vendedor/Repartidor
     * los productos solicitados.
     * * Los suma al stock actual del empleado.
     * @return
     * @throws Exception 
     */
    public boolean cancelaSalidaAlmacenProductosInventarioEmpleado(int idEmpleado) throws Exception{
        boolean exito = false;
        
        String msgError = "";
        try{
            EmpleadoInventarioRepartidorBO emInventarioRepartidorBO = new EmpleadoInventarioRepartidorBO(this.conn);
            EmpleadoInventarioRepartidor emInventarioRepartidorDto;
            EmpleadoInventarioRepartidorDaoImpl emInventarioRepartidorDao = new EmpleadoInventarioRepartidorDaoImpl(this.conn);
            ConceptoBO conceptoBO;
            
            for (ProductoSesion prodSesion : this.getListaProducto()){

                EmpleadoInventarioRepartidor[] emInventarios = null;
                if(prodSesion.getIdInventarioEmpleado() <=0){
                    emInventarios = emInventarioRepartidorBO.findEmpleadoInventarioRepartidors(-1, idEmpleado, 0, 0, " AND ID_CONCEPTO = " + prodSesion.getIdProducto());
                }else{
                    emInventarios = emInventarioRepartidorBO.findEmpleadoInventarioRepartidors(-1, idEmpleado, 0, 0, " AND ID_CONCEPTO = " + prodSesion.getIdProducto() + " AND ID_INVENTARIO = " + prodSesion.getIdInventarioEmpleado());
                    //setearInfoPeso = true;
                }
                conceptoBO = new ConceptoBO(prodSesion.getIdProducto(), this.conn);
                
                if (emInventarios.length>0){
                    emInventarioRepartidorDto = emInventarios[0];

                    double nuevoStock = emInventarioRepartidorDto.getCantidad() + prodSesion.getCantidadEntregada();
                    emInventarioRepartidorDto.setCantidad(nuevoStock);

                    if (prodSesion.getCantidadEntregadaPeso()>0){
                    //Datos Granel
                        double nuevoStockPeso = Converter.roundDouble(emInventarioRepartidorDto.getExistenciaGranel()) + Converter.roundDouble(prodSesion.getCantidadEntregadaPeso());
                        emInventarioRepartidorDto.setExistenciaGranel(Converter.roundDouble(nuevoStockPeso));
                    }
                    emInventarioRepartidorDao.update(emInventarioRepartidorDto.createPk(), emInventarioRepartidorDto);
                    
                }

            }
            
            if (msgError.trim().equals("")){
                exito = true;
            }else{
                exito = false;
                throw new Exception(msgError);
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        
        return exito;
    }
    
    /////PARA EL CALCULO DE IMPUESTO EN LOS CONCEPTOS:
    private BigDecimal totalImpuestoTrasladadoEnConcepto = BigDecimal.ZERO;
    private BigDecimal totalImpuestoRetenidoEnConcepto = BigDecimal.ZERO;

    public BigDecimal getTotalImpuestoRetenidoEnConcepto() {
        return totalImpuestoRetenidoEnConcepto.setScale(2, RoundingMode.HALF_UP);
        //*return totalImpuestoRetenidoEnConcepto;
    }
    public void setTotalImpuestoRetenidoEnConcepto(BigDecimal totalImpuestoRetenidoEnConcepto) {
        this.totalImpuestoRetenidoEnConcepto = totalImpuestoRetenidoEnConcepto;
    }
    public BigDecimal getTotalImpuestoTrasladadoEnConcepto() {
        return totalImpuestoTrasladadoEnConcepto.setScale(2, RoundingMode.HALF_UP);
        //*return totalImpuestoTrasladadoEnConcepto;
    }
    public void setTotalImpuestoTrasladadoEnConcepto(BigDecimal totalImpuestoTrasladadoEnConcepto) {
        this.totalImpuestoTrasladadoEnConcepto = totalImpuestoTrasladadoEnConcepto;
    }
    
    public boolean facturaConConceptosConImpuestos = false;

    public boolean isFacturaConConceptosConImpuestos() {
        return facturaConConceptosConImpuestos;
    }

    public void setFacturaConConceptosConImpuestos(boolean facturaConConceptosConImpuestos) {
        this.facturaConConceptosConImpuestos = facturaConConceptosConImpuestos;
    }
    
   
    /**
     * Calcula el total de la cotizacion
     * Total = Subtotal – Descuentos + Impuestos Trasladados – Impuestos Retenidos.
     * @param BigDecimal Total de la Cotizacion
     */
    public BigDecimal calculaTotalImpuestoEnConceptos(){
        BigDecimal total = BigDecimal.ZERO;
        
        // a = subtotal - descuentos
        BigDecimal subTotalConDescuento = calculaSubTotal().subtract(getDescuentoImporte()).setScale(2, RoundingMode.HALF_UP);
        
        // b = a + impuestos trasladados
        total = subTotalConDescuento.add(getTotalImpuestoTrasladadoEnConcepto()).setScale(2, RoundingMode.HALF_UP);
        
        // b = b - impuestos retenidos
        total = total.subtract(getTotalImpuestoRetenidoEnConcepto()).setScale(2, RoundingMode.HALF_UP);
        
        return total;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    
    
    /**
     * Da salida parcial del inventario personalizado del Empleado Vendedor/Repartidor
     * los productos solicitados
     * @return
     * @throws Exception 
     */
    public boolean salidaParcialAlmacenProductosInventarioEmpleado(int idEmpleado) throws Exception{
        boolean exito = false;
        
        String msgError = "";
        try{
            EmpleadoBO empleadoBO = new EmpleadoBO(idEmpleado, this.conn);
            EmpleadoInventarioRepartidorBO emInventarioRepartidorBO = new EmpleadoInventarioRepartidorBO(this.conn);
            EmpleadoInventarioRepartidor emInventarioRepartidorDto;
            EmpleadoInventarioRepartidorDaoImpl emInventarioRepartidorDao = new EmpleadoInventarioRepartidorDaoImpl(this.conn);
            ConceptoBO conceptoBO;
            
            int idEmpresa = empleadoBO.getEmpleado().getIdEmpresa();
            int productosEncontrados = 0;
                        
            
            //Recorremos por primera vez para asegurar integridad
            //Solo en caso de que todos los productos tengan stock suficiente se daran de baja TODOS
            //  de lo contrario, no se dara de baja ninguno.
            for (ProductoSesion prodSesion : this.getListaProducto()){
                boolean setearInfoPeso = false;
                EmpleadoInventarioRepartidor[] emInventarios = null;
                if(prodSesion.getIdInventarioEmpleado() <=0){
                    emInventarios = emInventarioRepartidorBO.findEmpleadoInventarioRepartidors(-1, idEmpleado, 0, 0, " AND ID_CONCEPTO = " + prodSesion.getIdProducto());
                }else{
                    emInventarios = emInventarioRepartidorBO.findEmpleadoInventarioRepartidors(-1, idEmpleado, 0, 0, " AND ID_CONCEPTO = " + prodSesion.getIdProducto() + " AND ID_INVENTARIO = " + prodSesion.getIdInventarioEmpleado());
                    setearInfoPeso = true;
                }
                conceptoBO = new ConceptoBO(prodSesion.getIdProducto(), this.conn);
                
                if (emInventarios != null && emInventarios.length>0){
                    emInventarioRepartidorDto = emInventarios[0];
                    
                     //Verificamos si se verifica el stock o no
                    EmpresaBO empresaBO = new EmpresaBO(this.conn);
                    EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl().findByPrimaryKey(empresaBO.getEmpresaMatriz(idEmpresa).getIdEmpresa());     
                    if (empresaPermisoAplicacionDto != null) {
                        if (empresaPermisoAplicacionDto.getRevisionCantidadProducto() == 1) {
                            
                            double nuevoStock = emInventarioRepartidorDto.getCantidad() - prodSesion.getCantidadEntregada();
                            double nuevoStockPeso = emInventarioRepartidorDto.getExistenciaGranel() - prodSesion.getCantidadEntregadaPeso();
                            if (nuevoStock < 0 || nuevoStockPeso<0 ) {
                                msgError += "<ul>El stock del empleado, del articulo '" + conceptoBO.getNombreConceptoLegible() + "' es insuficiente para cubrir la operación."
                                        + "<br/>No. de Articulos disponibles del repartidor: " + emInventarioRepartidorDto.getCantidad()
                                        + (prodSesion.getCantidadPeso()>0?"<br/>Peso Neto Stock:" + emInventarioRepartidorDto.getExistenciaGranel():"");
                                conceptoBO.enviaNotificacionStockMinimoSincronizaPedido(conceptoBO.getConcepto(),empleadoBO.getEmpleado());
                                
                            } else {
                                productosEncontrados++;
                            }
                            
                        }
                    }
                }
            }
            
            
            //Se recorre por segunda ocasion
            //  Una vez que aseguramos la integridad y que todos los productos tienen stock suficiente
            //  procedemos a darlos de baja del inventario del empleado.
            if (productosEncontrados>0 && msgError.equals("") ){
                for (ProductoSesion prodSesion : this.getListaProducto()){
                    //boolean setearInfoPeso = false;
                    //EmpleadoInventarioRepartidor[] emInventarios = emInventarioRepartidorBO.findEmpleadoInventarioRepartidors(-1, idEmpleado, 0, 0, " AND ID_CONCEPTO = " + prodSesion.getIdProducto());
                    EmpleadoInventarioRepartidor[] emInventarios = null;
                    if(prodSesion.getIdInventarioEmpleado() <=0){
                        emInventarios = emInventarioRepartidorBO.findEmpleadoInventarioRepartidors(-1, idEmpleado, 0, 0, " AND ID_CONCEPTO = " + prodSesion.getIdProducto());
                    }else{
                        emInventarios = emInventarioRepartidorBO.findEmpleadoInventarioRepartidors(-1, idEmpleado, 0, 0, " AND ID_CONCEPTO = " + prodSesion.getIdProducto() + " AND ID_INVENTARIO = " + prodSesion.getIdInventarioEmpleado());
                        //setearInfoPeso = true;
                    }
                    conceptoBO = new ConceptoBO(prodSesion.getIdProducto(), this.conn);

                    if (emInventarios != null && emInventarios.length>0){
                        emInventarioRepartidorDto = emInventarios[0];
                                                
                        System.out.println("-------------b id producto" + emInventarioRepartidorDto.getIdConcepto() + "cantidad actual : "+emInventarioRepartidorDto.getCantidad());
                        System.out.println("-------------b id producto" + prodSesion.getIdProducto() + "cantidad restar : "+prodSesion.getCantidadEntregada());
                        
                        double nuevoStock = emInventarioRepartidorDto.getCantidad() - prodSesion.getCantidadEntregada();
                        double nuevoStockPeso = emInventarioRepartidorDto.getExistenciaGranel();//////////////////////
                        
                        System.out.println("-------------b id producto" + emInventarioRepartidorDto.getIdConcepto() + "Peso actual : "+emInventarioRepartidorDto.getPeso());
                        System.out.println("-------------b id producto" + emInventarioRepartidorDto.getIdConcepto() + "Peso restar : "+prodSesion.getCantidadEntregadaPeso());
                        
                        
                        //if (setearInfoPeso)////////////////////////////////////
                        if (prodSesion.getCantidadEntregadaPeso()>0)
                            nuevoStockPeso = Converter.roundDouble(emInventarioRepartidorDto.getExistenciaGranel())- Converter.roundDouble(prodSesion.getCantidadEntregadaPeso());
                        
                        if (nuevoStock<0){
                            msgError += "<ul>El stock del empleado, del articulo '" + conceptoBO.getNombreConceptoLegible() + "' es insuficiente para cubrir la operación."
                                        + "<br/>No. de Articulos disponibles del repartidor: " + emInventarioRepartidorDto.getCantidad();
                            conceptoBO.enviaNotificacionStockMinimoSincronizaPedido(conceptoBO.getConcepto(),empleadoBO.getEmpleado());
                        }else{
                            //--Creamos registro de movimiento de almacen
                            //--Para inventario de empleado NO aplica
                            //...
                            
                            //Actualizamos registro único de inventario de empleado
                            emInventarioRepartidorDto.setCantidad(nuevoStock);
                            //if(setearInfoPeso)////////////
                            emInventarioRepartidorDto.setExistenciaGranel(Converter.roundDouble(nuevoStockPeso));///////////////////
                            emInventarioRepartidorDao.update(emInventarioRepartidorDto.createPk(), emInventarioRepartidorDto);
                        }
                        
                    }else{
                        //msgError += "<ul>El producto con id " + prodSesion.getIdProducto() + "no existe en el stock del empleado repartidor, posiblemente fue restado en una sesion alterna.";
                    }

                }
            }
            
            if (msgError.trim().equals("")){
                exito = true;
            }else{
                exito = false;
                throw new Exception(msgError);
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        
        return exito;
    }
    
    
    /**
     * Da entrada del almacen a los productos específicados en la nota de credito
     * Los suma del stock actual
     * 
     */
    public boolean entradaAlmacenProductos() throws Exception{
        boolean exito = false;
        
        String msgError = "";
        try{
            ConceptoBO conceptoBO;
            Concepto conceptoDto;
            ArrayList<Concepto> listaConceptos = new ArrayList<Concepto>();
            ExistenciaAlmacen almPrincipal = null;
            ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(this.conn);
            
            
            //Recorremos por primera vez para asegurar integridad
            //Solo en caso de que todos los productos tengan stock suficiente se daran de baja TODOS
            //  de lo contrario, no se dara de baja ninguno.
            for (ProductoSesion prodSesion : this.getListaProducto()){
                conceptoBO = new ConceptoBO(prodSesion.getIdProducto(),this.conn);
                conceptoDto = conceptoBO.getConcepto();
                
                if (conceptoDto!=null){
                     //Verificamos si se consulta el stock o no
                    EmpresaBO empresaBO = new EmpresaBO(this.conn);
                    EmpresaPermisoAplicacion empresaPermisoAplicacionDto = new EmpresaPermisoAplicacionDaoImpl().findByPrimaryKey(empresaBO.getEmpresaMatriz(conceptoBO.getConcepto().getIdEmpresa()).getIdEmpresa());     
                    if (empresaPermisoAplicacionDto != null) {
                        if (empresaPermisoAplicacionDto.getRevisionCantidadProducto() == 1) {                            
                            
                                listaConceptos.add(conceptoDto);
                            
                        }
                    }
                }
            }
            
            //Se recorre por segunda ocasion
            //  Una vez que aseguramos la integridad y que todos los productos tienen stock suficiente
            //  procedemos a darlos de baja del almacen (movimientos) y a actualizar su registro único.
            if (listaConceptos.size()>0 && msgError.equals("") ){
                for (ProductoSesion prodSesion : this.getListaProducto()){
                    conceptoBO = new ConceptoBO(prodSesion.getIdProducto(),this.conn);
                    conceptoDto = conceptoBO.getConcepto();

                    if (conceptoDto!=null){
                        
                        almPrincipal = exisAlmBO.getExistenciaProductoAlmacen(prodSesion.getIdAlmacen(), conceptoDto.getIdConcepto());
                        
                        BigDecimal numArticulosDisponibles = (new BigDecimal(almPrincipal!=null?almPrincipal.getExistencia():0)).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal prodSesionCantidad = (new BigDecimal(prodSesion.getCantidad())).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal stockTotal = numArticulosDisponibles.add(prodSesionCantidad);
                        
                        double nuevoStock = stockTotal.doubleValue();                
                        if (nuevoStock<0){
                            msgError+="<ul>El stock del articulo '" + conceptoBO.getNombreConceptoLegible() + "' es insuficiente para cubrir la operación.<br/>No. de Articulos disponibles: " + (almPrincipal!=null?almPrincipal.getExistencia():0);
                        }else{
                            //Creamos registro de movimiento de almacen
                            Movimiento movimientoDto = new Movimiento();
                            MovimientoDaoImpl movimientosDaoImpl = new MovimientoDaoImpl();
                            int idEmpresa = conceptoDto.getIdEmpresa();

                            movimientoDto.setIdEmpresa(idEmpresa);
                            movimientoDto.setTipoMovimiento("ENTRADA");
                            movimientoDto.setNombreProducto(conceptoBO.getNombreConceptoLegible());
                            movimientoDto.setContabilidad(prodSesion.getCantidad());
                            movimientoDto.setIdProveedor(-1);
                            movimientoDto.setOrdenCompra("");
                            movimientoDto.setNumeroGuia("");
                            //movimientoDto.setIdAlmacen(alamacenMovimiento);
                            movimientoDto.setIdAlmacen(prodSesion.getIdAlmacen());
                            movimientoDto.setConceptoMovimiento("Operación de Nota de Crédito");                
                            movimientoDto.setFechaRegistro(new Date());
                            movimientoDto.setIdConcepto(conceptoDto.getIdConcepto());

                            /**
                            * Realizamos el insert
                            */
                            movimientosDaoImpl.insert(movimientoDto);

                            //Actualizamos registro único de concepto
                            
                            almPrincipal.setExistencia(nuevoStock);
                            new ExistenciaAlmacenBO(this.conn).updateBD(almPrincipal);
                            
                            /*conceptoDto.setNumArticulosDisponibles(nuevoStock);
                            conceptoBO.updateBD(conceptoDto);*/
                        }
                    }else{
                        msgError += "<ul>El producto con id " + prodSesion.getIdProducto() + "no existe en la base de datos, probablemente fue eliminado en otro sesion alterna.";
                    }

                }
            }
            
            if (msgError.trim().equals("")){
                exito = true;
            }else{
                exito = false;
                throw new Exception(msgError);
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        
        return exito;
    }
    
    
    
}
