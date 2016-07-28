/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.util;

/**
 *
 * @author ISCesar
 */
public class MapUtil {
    
    public static class MarcadorMapa{
        private String tipo;
        private String latitud;
        private String longitud;
        private String titulo;
        private String contenidoDialogo;
        private String rutaRelativaIcono;
        private String fechaHr;
        private double velocidadKmHr;
        private double direccionGrados;

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getLatitud() {
            return latitud;
        }

        public void setLatitud(String latitud) {
            this.latitud = latitud;
        }

        public String getLongitud() {
            return longitud;
        }

        public void setLongitud(String longitud) {
            this.longitud = longitud;
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getContenidoDialogo() {
            return contenidoDialogo;
        }

        public void setContenidoDialogo(String contenidoDialogo) {
            this.contenidoDialogo = contenidoDialogo;
        }

        public String getRutaRelativaIcono() {
            return rutaRelativaIcono;
        }

        public void setRutaRelativaIcono(String rutaRelativaIcono) {
            this.rutaRelativaIcono = rutaRelativaIcono;
        }

        public double getVelocidadKmHr() {
            return velocidadKmHr;
        }

        public void setVelocidadKmHr(double velocidadKmHr) {
            this.velocidadKmHr = velocidadKmHr;
        }

        public double getDireccionGrados() {
            return direccionGrados;
        }

        public void setDireccionGrados(double direccionGrados) {
            this.direccionGrados = direccionGrados;
        }

        public String getFechaHr() {
            return fechaHr;
        }

        public void setFechaHr(String fechaHr) {
            this.fechaHr = fechaHr;
        }
        
        
    }
    
}
