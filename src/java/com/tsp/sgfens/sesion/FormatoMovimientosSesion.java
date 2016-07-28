/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.sesion;

import com.tsp.sct.dao.dto.Almacen;
import com.tsp.sct.dao.dto.SgfensProveedor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 *
 * @author ISCesar
 */
public class FormatoMovimientosSesion {
    
    private ArrayList<MovimientoSesion> listaMovimiento = new ArrayList<MovimientoSesion>();
    private SgfensProveedor proveedor = null;
    /**
     * Para Movimientos de ALMACEN
     *  1- ENTRADA
     *  2- SALIDA
     *  3- TRASPASO
     * 
     * Para Asignacion de Inventario a Empleados
     *  1- CARGA (Asignación a empleado desde almacen)
     *  2- DEVOLUCION (Devolución De Empleado hacia Almacen)
     */
    private int tipoMovimiento = 0;
    private Almacen almacen = null;
    private Almacen almacenDestino = null;
    private int idConceptoUltimoAgregado = 0;
    private boolean modoValidacion = false;
    
    
    public final static int TIPO_MOV_ALMACEN_ENTRADA = 1;
    public final static int TIPO_MOV_ALMACEN_SALIDA = 2;
    public final static int TIPO_MOV_ALMACEN_TRASPASO = 3;
    
    public final static int TIPO_MOV_EMP_CARGA = 1;
    public final static int TIPO_MOV_EMP_DEVOLUCION = 2;
    public final static int TIPO_MOV_EMP_MERMA = 3;

    public void setListaMovimiento(ArrayList<MovimientoSesion> listaMovimiento) {
        this.listaMovimiento = listaMovimiento;
    }

    public ArrayList<MovimientoSesion> getListaMovimiento() {
        if (listaMovimiento==null)
            listaMovimiento = new ArrayList<MovimientoSesion>();
        return listaMovimiento;
    }

    public SgfensProveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(SgfensProveedor proveedor) {
        this.proveedor = proveedor;
    }

    public int getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(int tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public Almacen getAlmacenDestino() {
        return almacenDestino;
    }

    public void setAlmacenDestino(Almacen almacenDestino) {
        this.almacenDestino = almacenDestino;
    }

    public int getIdConceptoUltimoAgregado() {
        return idConceptoUltimoAgregado;
    }

    public void setIdConceptoUltimoAgregado(int idConceptoUltimoAgregado) {
        this.idConceptoUltimoAgregado = idConceptoUltimoAgregado;
    }

    public boolean isModoValidacion() {
        return modoValidacion;
    }

    public void setModoValidacion(boolean modoValidacion) {
        this.modoValidacion = modoValidacion;
    }
    
    public BigDecimal calculaTotalCantidad(){
        BigDecimal montoProductos = BigDecimal.ZERO;
        
        for (MovimientoSesion item : getListaMovimiento()){
            BigDecimal itemMonto = new BigDecimal(item.getCantidad()).setScale(2, RoundingMode.HALF_UP);
            
            montoProductos = montoProductos.add(itemMonto).setScale(2, RoundingMode.HALF_UP);
        }
        
        return montoProductos;
    }
    
    public BigDecimal calculaTotalPrecioVenta(){
        BigDecimal montoProductos = BigDecimal.ZERO;
        
        for (MovimientoSesion item : getListaMovimiento()){
            BigDecimal itemMonto = new BigDecimal(item.getTotal()).setScale(2, RoundingMode.HALF_UP);
            
            montoProductos = montoProductos.add(itemMonto).setScale(2, RoundingMode.HALF_UP);
        }
        
        return montoProductos;
    }
    
    
}
