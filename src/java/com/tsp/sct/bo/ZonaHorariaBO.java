/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.Empresa;
import com.tsp.sct.dao.dto.ZonaHoraria;
import com.tsp.sct.dao.dto.ZonaHorariaCatalogo;
import com.tsp.sct.dao.jdbc.ZonaHorariaCatalogoDaoImpl;
import com.tsp.sct.dao.jdbc.ZonaHorariaDaoImpl;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author leonardo
 */
public class ZonaHorariaBO {
    
    public static Calendar DateZonaHorariaByIdCatalogo(Connection conn, Date date, int idZonaHorariaCatalogo) throws ParseException{
        
        ZonaHorariaCatalogo zonaHorariaCatalogoDto = null;
        ZonaHorariaCatalogoDaoImpl zonaHorariaCatalogoDaoImpl = new ZonaHorariaCatalogoDaoImpl(conn);
        
        try{
            zonaHorariaCatalogoDto = zonaHorariaCatalogoDaoImpl.findByPrimaryKey(idZonaHorariaCatalogo);
        }catch(Exception e){}
        
        SimpleDateFormat sdfZona = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); 
        
        SimpleDateFormat sdfNuevaHoraZona = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); //este otro formato lo uso para que me respete la hora de la zona horaria, al no ponerle XXX no me hace la conversion de sumor o restar horas
                
        if(zonaHorariaCatalogoDto != null){              
            sdfZona.setTimeZone(TimeZone.getTimeZone(zonaHorariaCatalogoDto.getZonaHoraria()));
            sdfZona.format(calendar.getTime());
            Date dateDeZona = sdfNuevaHoraZona.parse(sdfZona.format(calendar.getTime()));
            calendar.setTime(dateDeZona);
        }
        return calendar;
    }
    
    public static Calendar DateZonaHorariaByIdEmpresa(Connection conn, Date date, int idEmpresa) throws Exception{
        
        ZonaHoraria zonaHorariaDto = null;
        ZonaHorariaDaoImpl zonaHorariaDaoImpl = new ZonaHorariaDaoImpl(conn);

        try{
            zonaHorariaDto = zonaHorariaDaoImpl.findByDynamicWhere(" ID_EMPRESA = " + idEmpresa , null)[0];
        }catch(Exception e){//si no se encuentra por la empresa, por si es sucursal, buscamos por Id de empresa padre; la matriz
            Empresa empresaMatriz = null;
            EmpresaBO empresaBO = new EmpresaBO(conn);
            try{
                empresaMatriz = empresaBO.getEmpresaMatriz((long)idEmpresa);                
            }catch(Exception e2){}
            if(empresaMatriz != null){
                try{
                    zonaHorariaDto = zonaHorariaDaoImpl.findByDynamicWhere(" ID_EMPRESA = " + empresaMatriz.getIdEmpresaPadre() , null)[0];
                }catch(Exception e3){}
            }
        }
        
        ZonaHorariaCatalogo zonaHorariaCatalogoDto = null;
        ZonaHorariaCatalogoDaoImpl zonaHorariaCatalogoDaoImpl = new ZonaHorariaCatalogoDaoImpl(conn);
        
        try{
            if(zonaHorariaDto != null){
                zonaHorariaCatalogoDto = zonaHorariaCatalogoDaoImpl.findByPrimaryKey(zonaHorariaDto.getIdZonaHorariaCatalogo());
            }else{
                System.out.println(" -- No Hay Zona Horaria para la Empresa con ID: " + idEmpresa);
            }
        }catch(Exception e){}
        
        SimpleDateFormat sdfZona = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); 
                
        SimpleDateFormat sdfNuevaHoraZona = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); //este otro formato lo uso para que me respete la hora de la zona horaria, al no ponerle XXX no me hace la conversion de sumor o restar horas
        
        if(zonaHorariaCatalogoDto != null){              
            sdfZona.setTimeZone(TimeZone.getTimeZone(zonaHorariaCatalogoDto.getZonaHoraria()));
            sdfZona.format(calendar.getTime());            
            Date dateDeZona = sdfNuevaHoraZona.parse(sdfZona.format(calendar.getTime()));            
            calendar.setTime(dateDeZona);
        }
        return calendar;
    }
    
}
