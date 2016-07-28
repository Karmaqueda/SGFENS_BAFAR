/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Parametros;
import com.tsp.sct.dao.jdbc.ParametrosDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISCesar
 */
public class ParametrosBO {
    
    private Parametros parametros =  null;
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Parametros getParametros() {
        return parametros;
    }

    public void setParametros(Parametros parametros) {
        this.parametros = parametros;
    }

    public ParametrosBO(Connection conn) {
        this.conn = conn;
    }
    
    public ParametrosBO(int idParametro, Connection conn){ 
        this.conn = conn;
        try{
            ParametrosDaoImpl parametrosDaoImpl = new ParametrosDaoImpl(this.conn);
            this.parametros = parametrosDaoImpl.findByPrimaryKey(idParametro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public String getParametroString(String nombreParametro){
        String valor =  null;
        try{
            ParametrosDaoImpl parametrosDaoImpl = new ParametrosDaoImpl(this.conn);
            Parametros parametro = parametrosDaoImpl.findWhereNombreEquals(nombreParametro)[0];
            if (parametro!=null)
                valor = parametro.getDescripcion();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return valor;
    }
    
    public double getParametroDouble(String nombreParametro){
        double valor =  0;
        try{
            ParametrosDaoImpl parametrosDaoImpl = new ParametrosDaoImpl(this.conn);
            Parametros parametro = parametrosDaoImpl.findWhereNombreEquals(nombreParametro)[0];
            if (parametro!=null)
                valor = Double.parseDouble(parametro.getDescripcion());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return valor;
    }
    
    public int getParametroInt(String nombreParametro){
        int valor =  0;
        try{
            ParametrosDaoImpl parametrosDaoImpl = new ParametrosDaoImpl(this.conn);
            Parametros parametro = parametrosDaoImpl.findWhereNombreEquals(nombreParametro)[0];
            if (parametro!=null)
                valor = Integer.parseInt(parametro.getDescripcion());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return valor;
    }
    
    public void actualizaParametro(String nombreParametro, String valorParametro) throws Exception{
        ParametrosDaoImpl parametrosDaoImpl = new ParametrosDaoImpl(this.conn);
        Parametros parametro = parametrosDaoImpl.findWhereNombreEquals(nombreParametro)[0];
        if (parametro!=null){
            parametro.setDescripcion(valorParametro);
        }else{
            throw new Exception("El parametro '" + nombreParametro + "' no existe. ");
        }
    }
}