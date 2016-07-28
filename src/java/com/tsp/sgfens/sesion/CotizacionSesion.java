/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.sesion;

import com.tsp.sct.dao.dto.Cliente;
import com.tsp.sct.dao.dto.Empleado;
import com.tsp.sct.dao.dto.SgfensProspecto;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 *
 * @author ISCesarMartinez
 */
public class CotizacionSesion extends FormatoSesion {
    
    private int idCotizacion = -1;
    
    private SgfensProspecto prospecto = null;
    
    private String folioCotizacionMovil = null;
    
    private double latitud=0;
    private double longitud=0;

    private Empleado empleadoVendedor = null;

    public CotizacionSesion() {
        super();
    }

    public int getIdCotizacion() {
        return idCotizacion;
    }

    public void setIdCotizacion(int idCotizacion) {
        this.idCotizacion = idCotizacion;
    }

    public SgfensProspecto getProspecto() {
        return prospecto;
    }

    public void setProspecto(SgfensProspecto prospecto) {
        this.prospecto = prospecto;
    }

    public String getFolioCotizacionMovil() {
        return folioCotizacionMovil;
    }

    public void setFolioCotizacionMovil(String folioCotizacionMovil) {
        this.folioCotizacionMovil = folioCotizacionMovil;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public Empleado getEmpleadoVendedor() {
        return empleadoVendedor;
    }

    public void setEmpleadoVendedor(Empleado empleadoVendedor) {
        this.empleadoVendedor = empleadoVendedor;
    }

  
    
    
   
}
