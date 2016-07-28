/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sgfens.sesion;

import com.tsp.sct.util.StringManage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ISCesar
 */
public class CrFormularioCampoSesion implements Serializable, Comparable<CrFormularioCampoSesion> {
    
    private int identificadorCampoSesion;
    private int orden;
    private int idTipoCampo;
    private int idFormularioCampo;
    private String nombreIdCampo;
    private String etiqueta;
    private String descripcion;
    private String sugerencia;
    private boolean isRequerido;
    private int idFormularioValidacion;
    private List<String> opciones;
    private String valorDefecto;
    private String variableFormula;

    public int getIdentificadorCampoSesion() {
        return identificadorCampoSesion;
    }

    public void setIdentificadorCampoSesion(int identificadorCampoSesion) {
        this.identificadorCampoSesion = identificadorCampoSesion;
    }
    
    public void setOpciones(String opciones){
        List<String> listaOpciones = new ArrayList<String>();
        String[] arrayOpciones = new String[0];
        try{
            arrayOpciones = opciones.split("\n");//  \r\n
        }catch(Exception ex){
            ex.printStackTrace();
        }
        for (String opcion :  arrayOpciones){
            if (!StringManage.getValidString(opcion).equals("")){
                listaOpciones.add(opcion);
            }
        }
        
        this.opciones = listaOpciones;
    }
    
    public String getOpcionesTexto(){
        String opcionesTexto = "";
        for (String str : getOpciones()){
            if (StringManage.getValidString(str).length()>0)
                opcionesTexto += str + "\n";
        }
        return opcionesTexto;
    }
    
    public void asignaValoresDefecto(){
        descripcion = "";
        switch (idTipoCampo){
            case 1: //Texto
                etiqueta = "Campo de Texto";
                break;
            case 2: //Opción multiple (checkbox)
                etiqueta = "Opción Múltiple";
                getOpciones().add("Opción 1");
                getOpciones().add("Opción 2");
                break;
            case 3: //Opción unica (radio)
                etiqueta = "Opción Única";
                getOpciones().add("Opción 1");
                getOpciones().add("Opción 2");
                break;
            case 4: //Lista de opciones
                etiqueta = "Lista de Opciones";
                getOpciones().add("Opción 1");
                getOpciones().add("Opción 2");
                break;
            case 5: //Subtítulo
                etiqueta = "Agrega un subtítulo";
                break;
            case 6: //Capturar Fotografía
                etiqueta = "Capturar Fotografía";
                descripcion = "Descripción";
                break;
            case 7: //Capturar Firma
                etiqueta = "Firma";
                descripcion = "Pon tu firma";
                break;
            case 8: //Calendario
                etiqueta = "Calendario";
                break;
            case 9: //Hora del día
                etiqueta = "Hora";
                break;
            case 10: //Fórmula
                etiqueta = "Fórmula";
                break;
        }
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public int getIdTipoCampo() {
        return idTipoCampo;
    }

    public void setIdTipoCampo(int idTipoCampo) {
        this.idTipoCampo = idTipoCampo;
    }

    public int getIdFormularioCampo() {
        return idFormularioCampo;
    }

    public void setIdFormularioCampo(int idFormularioCampo) {
        this.idFormularioCampo = idFormularioCampo;
    }

    public String getNombreIdCampo() {
        return nombreIdCampo;
    }

    public void setNombreIdCampo(String nombreIdCampo) {
        this.nombreIdCampo = nombreIdCampo;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSugerencia() {
        return sugerencia;
    }

    public void setSugerencia(String sugerencia) {
        this.sugerencia = sugerencia;
    }

    public boolean isIsRequerido() {
        return isRequerido;
    }

    public void setIsRequerido(boolean isRequerido) {
        this.isRequerido = isRequerido;
    }

    public int getIdFormularioValidacion() {
        return idFormularioValidacion;
    }

    public void setIdFormularioValidacion(int idFormularioValidacion) {
        this.idFormularioValidacion = idFormularioValidacion;
    }

    public List<String> getOpciones() {
        if (opciones==null)
            opciones = new ArrayList<String>();
        return opciones;
    }

    public void setOpciones(List<String> opciones) {
        this.opciones = opciones;
    }

    public String getValorDefecto() {
        return valorDefecto;
    }

    public void setValorDefecto(String valorDefecto) {
        this.valorDefecto = valorDefecto;
    }

    public String getVariableFormula() {
        return variableFormula;
    }

    public void setVariableFormula(String variableFormula) {
        this.variableFormula = variableFormula;
    }
    
    @Override
    public int compareTo(CrFormularioCampoSesion o) {
        CrFormularioCampoSesion crFormularioCampoSesion = (CrFormularioCampoSesion)  o;
        Integer a = this.getOrden();
        Integer b = crFormularioCampoSesion.getOrden();
        
        return a.compareTo(b);
    }
}
