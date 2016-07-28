/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.cr;

import com.tsp.sct.bo.CrEstadoSolicitudBO;
import com.tsp.sct.bo.CrFormularioEventoBO;
import com.tsp.sct.bo.CrProductoReglaBO;
import com.tsp.sct.bo.CrScoreBO;
import com.tsp.sct.bo.CrScoreDetalleBO;
import com.tsp.sct.bo.UsuarioBO;
import com.tsp.sct.bo.ZonaHorariaBO;
import com.tsp.sct.dao.dto.CrFormularioCampo;
import com.tsp.sct.dao.dto.CrFormularioRespuesta;
import com.tsp.sct.dao.dto.CrFrmEventoSolicitud;
import com.tsp.sct.dao.dto.CrProductoCredito;
import com.tsp.sct.dao.dto.CrProductoRegla;
import com.tsp.sct.dao.dto.CrScore;
import com.tsp.sct.dao.dto.CrScoreDetalle;
import com.tsp.sct.dao.dto.CrSolicitudBitacora;
import com.tsp.sct.dao.jdbc.CrFormularioCampoDaoImpl;
import com.tsp.sct.dao.jdbc.CrFormularioRespuestaDaoImpl;
import com.tsp.sct.dao.jdbc.CrFrmEventoSolicitudDaoImpl;
import com.tsp.sct.dao.jdbc.CrProductoCreditoDaoImpl;
import com.tsp.sct.dao.jdbc.CrProductoReglaDaoImpl;
import com.tsp.sct.dao.jdbc.CrSolicitudBitacoraDaoImpl;
import java.sql.Connection;
import java.util.Date;

/**
 *
 * @author leonardo
 * Clase que contiene las revisiones del prospecto/cliente en Buró de Crédito, Listas Negras y Créditos Previos
 * 
 */
public class RevisionesCr {
    
    private CrFrmEventoSolicitud crFrmEventoSolicitud;
    
    public boolean BuroCredito(String rfc, String curp, String nombre1Validar, String nombre2Validar, String apellidoPaternoValidar, String apellidoMaternoValidar, String razonSocial_o_nombreCompleto){        
        boolean aprobado = false;
        
        System.out.println("rfc: " + rfc);
        System.out.println("curp: " + curp);
        System.out.println("nombre1Validar: " + nombre1Validar);
        System.out.println("nombre2Validar: " + nombre2Validar);
        System.out.println("apellidoPaternoValidar: " + apellidoPaternoValidar);
        System.out.println("apellidoMaternoValidar: " + apellidoMaternoValidar);
        System.out.println("razonSocial_o_nombreCompleto: " + razonSocial_o_nombreCompleto);
        
        //se valida en el WS de buro de credito, por ahora se aprueba todo 
        aprobado = true;
        return aprobado;
    }
    
    private CrProductoCredito crProductoCreditoDto = null;
    private CrProductoCredito crProductoCreditoPadreDto = null;
    
    public String reglasRechazo(Connection conn, int idEmpresa, int id_producto_solicitado, double ingresosEgresosCapacidadPago, double montoSolicitado, double edad){
        String mensajeRechazo = "";
        //recuperamos el producto solicitado y su padre:
        System.out.println("---- MONTO SOLICITADO: " +montoSolicitado);
        
        CrProductoCreditoDaoImpl crProductoCreditoDaoImpl = new CrProductoCreditoDaoImpl(conn);
        try{
            crProductoCreditoDto = crProductoCreditoDaoImpl.findByPrimaryKey(id_producto_solicitado);
        }catch(Exception e){}
        if(crProductoCreditoDto != null){
            try{
                crProductoCreditoPadreDto = crProductoCreditoDaoImpl.findByPrimaryKey(crProductoCreditoDto.getIdProductoCreditoPadre());                             
            }catch(Exception e){e.printStackTrace();}
        }
        CrProductoRegla[] crProductoReglas = new CrProductoRegla[0];
        CrProductoReglaDaoImpl crProductoReglaDaoImpl = new CrProductoReglaDaoImpl(conn);
        try{
            crProductoReglas = crProductoReglaDaoImpl.findByDynamicWhere(" id_producto_credito = " + crProductoCreditoPadreDto.getIdProductoCredito() + " AND is_regla_rechazo = 1 AND ID_EMPRESA = " + idEmpresa + " AND ID_ESTATUS = 1 ", null);
        }catch(Exception e){e.printStackTrace();}
        double edadMayorIgual = 0;
        double edadMenorIgual = 0;
        double antiguedadDomicilio = 0;
        double antiguedadEmpleo = 0; 
        double ingresosMenorIgual = 0;
        for(CrProductoRegla regla : crProductoReglas){
            if(regla.getClaveTipoRegla() != null && regla.getClaveTipoRegla().equals("RE_EDAD_MAYOR_IGUAL")){
                edadMayorIgual = regla.getRangoMin();
            }else if(regla.getClaveTipoRegla() != null && regla.getClaveTipoRegla().equals("RE_EDAD_MENOR_IGUAL")){
                edadMenorIgual = regla.getRangoMax();
            }else if(regla.getClaveTipoRegla() != null && regla.getClaveTipoRegla().equals("RE_ANTIGUEDAD_DOMICILIO_MENOR_IGUAL")){
                antiguedadDomicilio = regla.getRangoMax();
            }else if(regla.getClaveTipoRegla() != null && regla.getClaveTipoRegla().equals("RE_ANTIGUEDAD_EMPLEO_MENOR_IGUAL")){
                antiguedadEmpleo = regla.getRangoMax();
            }else if(regla.getClaveTipoRegla() != null && regla.getClaveTipoRegla().equals("RE_INGRESOS_MENOR_IGUAL")){
                ingresosMenorIgual = regla.getRangoMax();
            }
        }
        if(edadMayorIgual > 0 || edadMenorIgual > 0){
            if(edadMayorIgual <= edad && edadMenorIgual >= edad){
                mensajeRechazo += "</br>La edad difiere del rango de edad del producto.";
            }
        }
        if(ingresosMenorIgual >= ingresosEgresosCapacidadPago){
            mensajeRechazo += "</br>Ingresos insuficientes.";
        }        
        return mensajeRechazo;
    }
    
    public int idProductoAceptado = 0; //variable de ayuda para saber que producto es el posible autorizado
    public CrProductoCredito crProductoCreditoPropuesto = null;
            
    public String creditoAplicable(double ingresosEgresosCapacidadPago, Connection conn, int idEmpresa){
        String mensajeRechazo = "";                
        double cuotaMensual = CrUtilCalculos.calcCuotaMensual(crProductoCreditoDto.getMonto(), crProductoCreditoDto.getTasaInteresAnual(), (int)crProductoCreditoDto.getPlazo(), 1);
        //validamos si el producto seleccionado tiene una cuota que puede cubrir el prospecto
        //if(ingresosEgresosCapacidadPago < crProductoCreditoDto.getMonto()){ /////****CAMBIAR getMonto POR EL MONTO DE CUOTA MENSUAL
        if(ingresosEgresosCapacidadPago < cuotaMensual){ 
            mensajeRechazo += "</br>El prospecto tiene una capacidad menor a la requerida por el producto seleccionado.";
        }else{//si cumple con la cuota mensual, el producto que se selecciono es el aceptado
            idProductoAceptado = crProductoCreditoDto.getIdProductoCredito();
        }
        if(!mensajeRechazo.equals("")){//si el producto seleccionado no se adecua a la capacidad de pago del prospecto, vemos cual si y enviamos el Id del producto sugerido
            //obtenemos todos los subproducto ligados a la familia del producto seleccionado, omitiendo el producto por el cual ya se valido, lor ordenamos por el importe de cuota mensual, para saber cual es el primero que vamos a validar
            CrProductoCredito[] crProductoCreditoTodos = new CrProductoCredito[0];
            try{
                crProductoCreditoTodos = new CrProductoCreditoDaoImpl(conn).findByDynamicWhere(" id_producto_credito_padre = " + crProductoCreditoDto.getIdProductoCreditoPadre() + " AND id_producto_credito != " + crProductoCreditoDto.getIdProductoCredito() +" AND id_empresa = " + idEmpresa + " AND id_estatus = 1 " + " AND id_producto_credito != id_producto_credito_padre AND id_producto_credito_padre IS NOT NULL ORDER BY monto DESC ", null); /////****CAMBIAR getMonto POR EL MONTO DE CUOTA MENSUAL
            }catch(Exception e){e.printStackTrace();}
            if(crProductoCreditoTodos != null){
                for(CrProductoCredito producto: crProductoCreditoTodos){
                    double cuotaMensualProductoSugerido = CrUtilCalculos.calcCuotaMensual(producto.getMonto(), producto.getTasaInteresAnual(), (int)producto.getPlazo(), 1);
                    //if(ingresosEgresosCapacidadPago >= crProductoCreditoDto.getMonto()){/////****CAMBIAR getMonto POR EL MONTO DE CUOTA MENSUAL
                    if(ingresosEgresosCapacidadPago >= cuotaMensualProductoSugerido){
                        idProductoAceptado = producto.getIdProductoCredito();//cargamos el ID del producto sugerido
                        crProductoCreditoPropuesto = producto;
                    }
                }
            }
        }

       return mensajeRechazo;
    }
    
    public boolean almacenaRegistroSolicitudBitacora(Connection conn, int idCrFormularioEvento, int id_producto_solicitado, int idEmpresa, int IdUsuario, int idEstadoSolicitud, String mensajeDescripcionEvento, boolean insertaNuevo){
        boolean errorGuardado = false;
        
        CrFrmEventoSolicitud crFrmEventoSolicitud = null;
        if(insertaNuevo){//nuevo registro
            crFrmEventoSolicitud = new CrFrmEventoSolicitud();        
            crFrmEventoSolicitud.setIdFormularioEvento(idCrFormularioEvento);
            crFrmEventoSolicitud.setIdProductoCredito(id_producto_solicitado);
            try{
                crFrmEventoSolicitud.setFechaHrCreacion(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(conn, new Date(), (int)idEmpresa).getTime());
            }catch(Exception e){
                crFrmEventoSolicitud.setFechaHrCreacion(new Date());
                e.printStackTrace();
            }

            crFrmEventoSolicitud.setIdUsuarioEdicion(IdUsuario);
            crFrmEventoSolicitud.setIdEstadoSolicitud(idEstadoSolicitud);//1: Rechazado    
            try{
                new CrFrmEventoSolicitudDaoImpl(conn).insert(crFrmEventoSolicitud);                                    
            }catch(Exception e){e.printStackTrace();}
            
        }else{//actualizar registro
            try{
                crFrmEventoSolicitud = new CrFrmEventoSolicitudDaoImpl(conn).findByDynamicWhere(" id_formulario_evento = " + idCrFormularioEvento, null)[0];        
            }catch(Exception e){e.printStackTrace();}
            //crFrmEventoSolicitud.setIdFormularioEvento(idCrFormularioEvento);
            if(id_producto_solicitado > 0){
                crFrmEventoSolicitud.setIdProductoCredito(id_producto_solicitado);
            }
            try{
                crFrmEventoSolicitud.setFechaHrCreacion(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(conn, new Date(), (int)idEmpresa).getTime());
            }catch(Exception e){
                crFrmEventoSolicitud.setFechaHrCreacion(new Date());
                e.printStackTrace();
            }

            crFrmEventoSolicitud.setIdUsuarioEdicion(IdUsuario);
            boolean aprobacionCompleta = false;
            if( (idEstadoSolicitud == CrEstadoSolicitudBO.S_APROBADA_MESAC && crFrmEventoSolicitud.getIdEstadoSolicitud() == CrEstadoSolicitudBO.S_APROBADA_VERIF) 
                        || (idEstadoSolicitud == CrEstadoSolicitudBO.S_APROBADA_VERIF && crFrmEventoSolicitud.getIdEstadoSolicitud() == CrEstadoSolicitudBO.S_APROBADA_MESAC) ){
                aprobacionCompleta = true;
                crFrmEventoSolicitud.setIdEstadoSolicitud(CrEstadoSolicitudBO.S_APROBADA);
            }else{
                crFrmEventoSolicitud.setIdEstadoSolicitud(idEstadoSolicitud);   
            }
            
            try{
                new CrFrmEventoSolicitudDaoImpl(conn).update(crFrmEventoSolicitud.createPk(), crFrmEventoSolicitud);                                    
                this.crFrmEventoSolicitud = crFrmEventoSolicitud;
            }catch(Exception e){e.printStackTrace();}
            
            // Si acaba de ser aprobada por ambos roles (Mesa control y Verificador)
            // inmediatamente registramos en SAP (para creacion de BP y Contrato)
            if (aprobacionCompleta){
                CrFormularioEventoBO crFormularioEventoBO = new CrFormularioEventoBO(conn);
                try{
                    UsuarioBO user = new UsuarioBO(conn, IdUsuario);
                    crFormularioEventoBO.registraSolicitudEnSAP(idCrFormularioEvento, user);
                }catch(Exception ex){
                    System.out.println("Error al registrar solicitud en SAP.");
                    ex.printStackTrace();
                }
            }
        }

        CrSolicitudBitacora crSolicitudBitacora = new CrSolicitudBitacora();
        crSolicitudBitacora.setIdFormularioEvento(idCrFormularioEvento);
        crSolicitudBitacora.setIdEstadoSolicitud(idEstadoSolicitud);
        crSolicitudBitacora.setIdUsuario(IdUsuario);
        crSolicitudBitacora.setIdEmpresa(idEmpresa);
        crSolicitudBitacora.setDescripcionEvento(mensajeDescripcionEvento);
        try{
            crSolicitudBitacora.setFechaHrCreacion(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(conn, new Date(), (int)idEmpresa).getTime());
        }catch(Exception e){
            e.printStackTrace();
            crSolicitudBitacora.setFechaHrCreacion(new Date());
        }
        try{
            new CrSolicitudBitacoraDaoImpl(conn).insert(crSolicitudBitacora);
        }catch(Exception e){e.printStackTrace();}
        
        return errorGuardado;
    }
    
    public int puntosScore(Connection conn, int idEmpresa, int id_producto_solicitado, int idCrFormularioEvento){
        int puntosAcumuladosScore = 0;
        //si es nulo obtenemos el producto padre
        if(crProductoCreditoDto == null){
            CrProductoCreditoDaoImpl crProductoCreditoDaoImpl = new CrProductoCreditoDaoImpl(conn);
            try{
                crProductoCreditoDto = crProductoCreditoDaoImpl.findByPrimaryKey(id_producto_solicitado);
            }catch(Exception e){}
            if(crProductoCreditoDto != null){
                try{
                    crProductoCreditoPadreDto = crProductoCreditoDaoImpl.findByPrimaryKey(crProductoCreditoDto.getIdProductoCreditoPadre());                             
                }catch(Exception e){e.printStackTrace();}
            }
        }
        
        CrScore crScore = null;
        CrScoreDetalle[] crScoreDetalles = new CrScoreDetalle[0];
        CrScoreBO crScoreBO = new CrScoreBO(conn);
        CrScoreDetalleBO crScoreDetalleBO = new CrScoreDetalleBO(conn);
        
        if(crProductoCreditoPadreDto != null && crProductoCreditoPadreDto.getIdScore() > 0){
            try{
                crScore = crScoreBO.findCrScorebyId(crProductoCreditoPadreDto.getIdScore());
            }catch(Exception e){}
            if(crScore != null){//obtenemos el detalle
                crScoreDetalles = crScoreDetalleBO.findCrScoreDetalles(0, idEmpresa, 0, 0, " AND id_score = " + crScore.getIdScore() + " AND id_estatus = 1 ");
            }
        }
        
        CrFormularioCampo crFormularioCampo = null;
        CrFormularioRespuesta crFormularioRespuesta = null;
        CrFormularioCampoDaoImpl crFormularioCampoDaoImpl = new CrFormularioCampoDaoImpl(conn);
        CrFormularioRespuestaDaoImpl crFormularioRespuestaDaoImpl = new CrFormularioRespuestaDaoImpl(conn);
        for(CrScoreDetalle detalle : crScoreDetalles){
            //recuperamos el campo de formulario al cual va a evaluar y su respuesta:
            crFormularioCampo = null;
            crFormularioRespuesta = null;
            try{
                crFormularioCampo = crFormularioCampoDaoImpl.findByPrimaryKey(detalle.getIdFormularioCampo());//recuperamos el registro del campo
            }catch(Exception e){}
            
            try{                                
                crFormularioRespuesta = crFormularioRespuestaDaoImpl.findByDynamicWhere(" id_formulario_evento = " + idCrFormularioEvento + " AND id_formulario_campo = " + detalle.getIdFormularioCampo(), null)[0];
            }catch(Exception e){}
                                
            if(crFormularioRespuesta != null){
                if(detalle.getValorExacto() != null && !detalle.getValorExacto().trim().equals("")){
                    if(detalle.getValorExacto().trim().equals( (crFormularioRespuesta.getValor()!=null?crFormularioRespuesta.getValor().trim():"") )){
                        puntosAcumuladosScore += detalle.getPuntosScore();
                    }
                }else if(detalle.getRangoMin() != detalle.getRangoMax()){
                    //convertimos a double el valor del campo para comparar rangos
                    double campoValor = Double.parseDouble(crFormularioRespuesta.getValor()!=null?crFormularioRespuesta.getValor().trim():"0");
                    if(campoValor >= detalle.getRangoMin() && campoValor <= detalle.getRangoMax()){
                        puntosAcumuladosScore += detalle.getPuntosScore();
                    }
                }
            }
        }
        
        return puntosAcumuladosScore;
    }

    public CrFrmEventoSolicitud getCrFrmEventoSolicitud() {
        return crFrmEventoSolicitud;
    }

    public void setCrFrmEventoSolicitud(CrFrmEventoSolicitud crFrmEventoSolicitud) {
        this.crFrmEventoSolicitud = crFrmEventoSolicitud;
    }
    
    
}
