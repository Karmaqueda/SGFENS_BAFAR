/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.cr;

import com.tsp.sct.bo.ZonaHorariaBO;
import com.tsp.sct.dao.dto.SgfensProspecto;
import com.tsp.sct.dao.dto.SgfensProspectoPk;
import com.tsp.sct.dao.jdbc.SgfensProspectoDaoImpl;
import java.sql.Connection;
import java.util.Date;

/**
 *
 * @author leonardo
 */
public class CrearProspecto {
    
    private SgfensProspecto prospecto = new SgfensProspecto();
    
    private String nombre1Prospecto = "";
    private String nombre2Prospecto = "";
    private String apellidoPaternoProspecto = "";
    private String apellidoMaternoProspecto = "";
    boolean existeCampoRazonSocial = false;
    
    public void cargaDatos(String nombreCampo, String valorCampo){
        
        if(nombreCampo.toUpperCase().indexOf("RAZON SOCIAL") > -1){
            prospecto.setRazonSocial(valorCampo);
            existeCampoRazonSocial = true;
        }else if(nombreCampo.toUpperCase().indexOf("SOL_LADA") > -1){
            prospecto.setLada(valorCampo);
        }else if(nombreCampo.toUpperCase().indexOf("TELEFONO") > -1){
            prospecto.setTelefono(valorCampo);
        }else if(nombreCampo.toUpperCase().indexOf("SOL_TELEFONO") > -1){
            prospecto.setTelefono(valorCampo);
        }else if(nombreCampo.toUpperCase().indexOf("SOL_CELULAR") > -1){
            prospecto.setCelular(valorCampo);
        }else if(nombreCampo.toUpperCase().indexOf("CORREO") > -1){
            prospecto.setCorreo(valorCampo);
        }else if(nombreCampo.toUpperCase().indexOf("SOL_EMAIL") > -1){
            prospecto.setCorreo(valorCampo);
        }else if(nombreCampo.toUpperCase().indexOf("SOL_DOM_NUM_EXT") > -1){
            prospecto.setDirNumeroExterior(valorCampo);
        }else if(nombreCampo.toUpperCase().indexOf("SOL_DOM_NUM_INT") > -1){
            prospecto.setDirNumeroInterior(valorCampo);
        }else if(nombreCampo.toUpperCase().indexOf("SOL_DOM_CP") > -1){
            prospecto.setDirCodigoPostal(valorCampo);
        }else if(nombreCampo.toUpperCase().indexOf("SOL_DOM_COLONIA") > -1){
            prospecto.setDirColonia(valorCampo);
        }else if(nombreCampo.toUpperCase().indexOf("SOL_NOMBRE_PRIMER") > -1){
            nombre1Prospecto = valorCampo;
        }else if(nombreCampo.toUpperCase().indexOf("SOL_NOMBRE_SEGUNDO") > -1){
            nombre2Prospecto = valorCampo;
        }else if(nombreCampo.toUpperCase().indexOf("SOL_APELLIDO_PAT") > -1){
            apellidoPaternoProspecto = valorCampo;
        }else if(nombreCampo.toUpperCase().indexOf("SOL_APELLIDO_MAT") > -1){
            apellidoMaternoProspecto = valorCampo;
        }
    }
    
    public SgfensProspectoPk creaProspecto(int idEmpresa, int idUsuarioVendedor, Connection conn){
        SgfensProspectoPk sgfensProspectoPk = null;
        prospecto.setIdEmpresa(idEmpresa);
        prospecto.setIdEstatus(1);
        
        prospecto.setIdUsuarioVendedor(idUsuarioVendedor);
        
        prospecto.setDescripcion("Creado para un crédito");
        try{
            prospecto.setFechaRegistro(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(conn, new Date(), (int)idEmpresa).getTime());
        }catch(Exception e){}
        //VALIDAMOS SI NO TIENE UNA RAZON SOCIAL LA CREAMOS CON LOS DATOS DE NOMBRES Y APELLIDOS:
        if(!existeCampoRazonSocial){
            String nombreCompleto = (!nombre1Prospecto.trim().equals("")?nombre1Prospecto:"") + (!nombre2Prospecto.trim().equals("")?" "+nombre2Prospecto:"") + (!apellidoPaternoProspecto.trim().equals("")?" "+apellidoPaternoProspecto:"") + (!apellidoMaternoProspecto.trim().equals("")?" "+apellidoMaternoProspecto:"");
            prospecto.setRazonSocial(nombreCompleto);
        }
        if(!nombre1Prospecto.trim().equals("")){
            prospecto.setContacto(nombre1Prospecto);
        }
        try{//realizamos el insert del prospecto
            sgfensProspectoPk = new SgfensProspectoDaoImpl(conn).insert(prospecto);
        }catch(Exception e){}
        
        return sgfensProspectoPk; 
    }
    
    public SgfensProspectoPk modificarProspecto(int idEmpresa, int idUsuarioVendedor, Connection conn){
        SgfensProspectoPk sgfensProspectoPk = null;
        prospecto.setIdEmpresa(idEmpresa);
        prospecto.setIdEstatus(1);
        
        prospecto.setIdUsuarioVendedor(idUsuarioVendedor);
        
        //prospecto.setDescripcion("Creado para un crédito");
        /*try{
            prospecto.setFechaRegistro(ZonaHorariaBO.DateZonaHorariaByIdEmpresa(conn, new Date(), (int)idEmpresa).getTime());
        }catch(Exception e){}*/
        //VALIDAMOS SI NO TIENE UNA RAZON SOCIAL LA CREAMOS CON LOS DATOS DE NOMBRES Y APELLIDOS:
        if(!existeCampoRazonSocial){
            String nombreCompleto = (!nombre1Prospecto.trim().equals("")?nombre1Prospecto:"") + (!nombre2Prospecto.trim().equals("")?" "+nombre2Prospecto:"") + (!apellidoPaternoProspecto.trim().equals("")?" "+apellidoPaternoProspecto:"") + (!apellidoMaternoProspecto.trim().equals("")?" "+apellidoMaternoProspecto:"");
            prospecto.setRazonSocial(nombreCompleto);
        }
        if(!nombre1Prospecto.trim().equals("")){
            prospecto.setContacto(nombre1Prospecto);
        }
        try{//actualizamos el prospecto
            new SgfensProspectoDaoImpl(conn).update(prospecto.createPk(),prospecto);
        }catch(Exception e){}
        
        return sgfensProspectoPk; 
    }

    /**
     * @return the prospecto
     */
    public SgfensProspecto getProspecto() {
        return prospecto;
    }

    /**
     * @param prospecto the prospecto to set
     */
    public void setProspecto(SgfensProspecto prospecto) {
        this.prospecto = prospecto;
    }
    
}
