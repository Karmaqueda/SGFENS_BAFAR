/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

/**
 *
 * @author ISCesarMartinez
 */
public class RolesBO {
    
    public static final int ROL_DESARROLLO = 1;
    public static final int ROL_ADMINISTRADOR = 2;
    public static final int ROL_PROMOTOR = 17;
    public static final int ROL_GESTOR = 29;
    public static final int ROL_GERENTE = 40;
    public static final int ROL_VERIFICADOR = 41;
    public static final int ROL_MESA_DE_CONTROL = 42;
    public static final int ROL_DIRECCION = 43;
    
    public static final String ROL_DESARROLLO_NAME = "Desarrollo";
    public static final String ROL_ADMINISTRADOR_NAME = "Administrador";
    public static final String ROL_PROMOTOR_NAME = "Promotor";
    public static final String ROL_GESTOR_NAME = "Gestor de Crédito";
    public static final String ROL_GERENTE_NAME = "Gerente";
    public static final String ROL_VERIFICADOR_NAME = "Verificador";
    public static final String ROL_MESA_DE_CONTROL_NAME = "Mesa de Control";
    public static final String ROL_DIRECCION_NAME = "Dirección";
        
    /**
     * Obtiene el nombre escrito (cadena) de un Rol por medio de su ID
     * @return Cadena con el nombre descriptivo del Rol
     */
    public static String getRolName(int idRol){
        String rolName="";
        switch (idRol){
            case ROL_DESARROLLO:
                return ROL_DESARROLLO_NAME;
            case ROL_ADMINISTRADOR:
                return ROL_ADMINISTRADOR_NAME;            
            case ROL_PROMOTOR:
                return ROL_PROMOTOR_NAME;                     
            case ROL_GESTOR:
                return ROL_GESTOR_NAME;
            case ROL_GERENTE:
                return ROL_GERENTE_NAME;
            case ROL_VERIFICADOR:
                return ROL_VERIFICADOR_NAME;
            case ROL_MESA_DE_CONTROL:
                return ROL_MESA_DE_CONTROL_NAME;
            case ROL_DIRECCION:
                return ROL_DIRECCION_NAME;
            default:
                rolName ="Rol no Existente";
        }
        return rolName;
    }
    
}
