/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.bo;

import static com.tsp.sct.bo.RolesBO.*;

/**
 *
 * @author leonardo
 */
public class RolAutorizacionBO {
    
    public static boolean permisoNuevoElemento(int idRol){
        
        switch (idRol){
            case ROL_DESARROLLO:
                return true;
            case ROL_ADMINISTRADOR:
                return true;
            case ROL_GERENTE:
                return true;
            default:
                return false;
        }
        
    }
    
    public static boolean permisoAccionesElemento(int idRol){
        
        switch (idRol){
            case ROL_DESARROLLO:
                return true;
            case ROL_ADMINISTRADOR:
                return true;
            case ROL_GERENTE:
                return true;
            default:
                return false;
        }
        
    }
    
    public static boolean permisoAccionesElementoConceptoServicio(int idRol){
        
        switch (idRol){
            case ROL_DESARROLLO:
                return true;
            case ROL_ADMINISTRADOR:
                return true;
            default:
                return false;
        }
        
    }
    
}
