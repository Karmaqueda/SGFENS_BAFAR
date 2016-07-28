/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.util.test;

import com.tsp.sct.bo.UsuariosBO;
import com.tsp.sct.dao.dto.Concepto;
import com.tsp.sct.dao.dto.Usuarios;
import com.tsp.sct.dao.exceptions.ConceptoDaoException;
import com.tsp.sct.dao.jdbc.ConceptoDaoImpl;
import com.tsp.sct.util.Encrypter;
import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HpPyme
 */
public class encripta_concepto {
    
    
        private Connection conn = null;    
        private Usuarios usuario = null;
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
            try {
                // TODO code application logic here
                int idEmpresa = 2183;
                Encrypter datoEnc =  new Encrypter();
                
                UsuariosBO usuBO =  new UsuariosBO();
                //Concepto[] conceptoDtos = new ConceptoDaoImpl().findWhereIdEmpresaEquals(idEmpresa);
                Concepto[] conceptoDtos = new ConceptoDaoImpl().findByDynamicWhere("ID_EMPRESA = " + idEmpresa ,null);
                for(Concepto con :conceptoDtos ){
                    
                    con.setNombre(datoEnc.encodeString2(con.getNombre()));
                    
                    new ConceptoDaoImpl().update(con.createPk(), con);
                    
                }                
                
            } catch (ConceptoDaoException ex) {
               ex.printStackTrace();
            }        
        
    }
    
}
