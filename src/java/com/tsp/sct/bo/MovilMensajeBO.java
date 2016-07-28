/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.MovilMensaje;
import com.tsp.sct.dao.jdbc.MovilMensajeDaoImpl;
import com.tsp.sct.util.DateManage;
import java.sql.Connection;
import java.util.Date;

/**
 *
 * @author ISCesarMartinez
 */
public class MovilMensajeBO {
    
    private boolean userConsola = false;

    public MovilMensaje[] getMovilMensajesByFilter(boolean recibidos, Date minFecha, Date maxFecha, 
            boolean filtroNoRecibidos, boolean filtroSoloComunicacionConsola, 
            int idReceptor, int idEmisor) {
        
        MovilMensaje[] resultado = new MovilMensaje[0];
        
        String strFechaMin =  DateManage.formatDateToSQL(minFecha);
        String strFechaMax = DateManage.formatDateToSQL(maxFecha);
        
        String strWhereRangoFechas="";
        
        String strWhereFiltroSoloRecibidos = "";
        String strWhereFiltroSoloComConsola = "";
        String strWhereFiltroReceptor = "";
        String strWhereFiltroEmisor = "";
        if(recibidos){
            strWhereFiltroSoloRecibidos = filtroNoRecibidos?"RECIBIDO=0":"";
            
            if (userConsola){
                if (filtroSoloComunicacionConsola){
                    //strWhereFiltroSoloComConsola = filtroSoloComunicacionConsola?"RECEPTOR_TIPO=2":"";
                    if(idEmisor > 0 )
                        strWhereFiltroSoloComConsola = filtroSoloComunicacionConsola?"RECEPTOR_TIPO=2 AND (ID_EMPLEADO_EMISOR IN (SELECT ID_EMPLEADO FROM EMPLEADO WHERE ID_EMPRESA = "+idEmisor+"))":"";
                        //strWhereFiltroSoloComConsola = filtroSoloComunicacionConsola?"RECEPTOR_TIPO=2 AND ID_EMPLEADO_EMISOR = "+idEmisor:"";
                        
                    else
                        strWhereFiltroSoloComConsola = filtroSoloComunicacionConsola?"RECEPTOR_TIPO=2":"";
                }else{
                    strWhereFiltroEmisor = idEmisor>0?"ID_EMPLEADO_EMISOR="+idEmisor:"";
                    strWhereFiltroReceptor = idReceptor>0?"ID_EMPLEADO_RECEPTOR="+idReceptor:"";
                }
            }else{
                if (filtroSoloComunicacionConsola){
                    strWhereFiltroSoloComConsola = filtroSoloComunicacionConsola?"EMISOR_TIPO=2":"";
                }else{
                    strWhereFiltroEmisor = idEmisor>0?"ID_EMPLEADO_EMISOR="+idEmisor:"";
                    strWhereFiltroReceptor = idReceptor>0?"ID_EMPLEADO_RECEPTOR="+idReceptor:"";
                }
            }
            
            if (minFecha!=null && maxFecha!=null){
                strWhereRangoFechas="(CAST(FECHA_EMISION AS DATE) BETWEEN '"+strFechaMin+"' AND '"+strFechaMax+"')";
            }
            if (minFecha!=null && maxFecha==null){
                strWhereRangoFechas="(CAST(FECHA_EMISION AS DATE)  >= '"+strFechaMin+"')";
            }
            if (minFecha==null && maxFecha!=null){
                strWhereRangoFechas="(CAST(FECHA_EMISION AS DATE)  <= '"+strFechaMax+"')";
            }
        
        }else{
            //enviados
            strWhereFiltroEmisor = idEmisor>0?"ID_EMPLEADO_EMISOR="+idEmisor:"";
            strWhereFiltroSoloRecibidos = filtroNoRecibidos?"RECIBIDO=0":"";
            
            if (filtroSoloComunicacionConsola){
                strWhereFiltroSoloComConsola = filtroSoloComunicacionConsola?"RECEPTOR_TIPO=2":"";
            }else{
                strWhereFiltroReceptor = idReceptor>0?"ID_EMPLEADO_RECEPTOR="+idReceptor:"";
            }
            
            /*
            if (minFecha!=null && maxFecha!=null){
                strWhereRangoFechas+="(CAST(FECHA_RECEPCION AS DATE) BETWEEN '"+strFechaMin+"' AND '"+strFechaMax+"')";
            }
            if (minFecha!=null && maxFecha==null){
                strWhereRangoFechas="(CAST(FECHA_RECEPCION AS DATE)  >= '"+strFechaMin+"')";
            }
            if (minFecha==null && maxFecha!=null){
                strWhereRangoFechas="(CAST(FECHA_RECEPCION AS DATE)  <= '"+strFechaMax+"')";
            }*/
            if (minFecha!=null && maxFecha!=null){
                strWhereRangoFechas="(CAST(FECHA_EMISION AS DATE) BETWEEN '"+strFechaMin+"' AND '"+strFechaMax+"')";
            }
            if (minFecha!=null && maxFecha==null){
                strWhereRangoFechas="(CAST(FECHA_EMISION AS DATE)  >= '"+strFechaMin+"')";
            }
            if (minFecha==null && maxFecha!=null){
                strWhereRangoFechas="(CAST(FECHA_EMISION AS DATE)  <= '"+strFechaMax+"')";
            }
        }
        
        String strWhere = "ID_MOVIL_MENSAJE>0";
        
        if (strWhere.trim().replaceAll(" ", "").equals("")){
            strWhere = strWhereRangoFechas;
        }else if(!strWhereRangoFechas.equals("")){
            strWhere+=" AND "+strWhereRangoFechas;
        }
        
        if (strWhere.trim().replaceAll(" ", "").equals("")){
            strWhere = strWhereFiltroSoloRecibidos;
        }else if(!strWhereFiltroSoloRecibidos.equals("")){
            strWhere+=" AND "+strWhereFiltroSoloRecibidos;
        }
         
        if (strWhere.trim().replaceAll(" ", "").equals("")){
            strWhere = strWhereFiltroSoloComConsola;
        }else if(!strWhereFiltroSoloComConsola.equals("")){
            strWhere+=" AND "+strWhereFiltroSoloComConsola;
        }
        
        if (strWhere.trim().replaceAll(" ", "").equals("")){
            strWhere = strWhereFiltroReceptor;
        }else if(!strWhereFiltroReceptor.equals("")){
            strWhere+=" AND "+strWhereFiltroReceptor;
        }
        
        if (strWhere.trim().replaceAll(" ", "").equals("")){
            strWhere = strWhereFiltroEmisor;
        }else if(!strWhereFiltroEmisor.equals("")){
            strWhere+=" AND "+strWhereFiltroEmisor;
        }
        
        try{
            resultado = new MovilMensajeDaoImpl(this.conn).findByDynamicWhere(strWhere, new Object[0]);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return resultado;
    }
    
    /**
     * Realiza una búsqueda por ID MovilMensaje en busca de
     * coincidencias
     * @param idMovilMensaje ID Del Usuario para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar MovilMensajes, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO MovilMensaje
     */
    public MovilMensaje[] findMovilMensajes(long idMovilMensaje, long idEmpresa, int minLimit,int maxLimit, String filtroBusqueda, long idEmpleadoEmisor, long idEmpleadoReceptor) {
        MovilMensaje[] movilMensajeDto = new MovilMensaje[0];
        MovilMensajeDaoImpl movilMensajeDao = new MovilMensajeDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idMovilMensaje>0){
                sqlFiltro ="ID_MOVIL_MENSAJE=" + idMovilMensaje + " ";
            }else{
                sqlFiltro ="ID_MOVIL_MENSAJE>0 ";
            }                   
            if(idEmpleadoEmisor>0){
                sqlFiltro =" ID_EMPLEADO_EMISOR = " + idEmpleadoEmisor + " OR ID_EMPLEADO_RECEPTOR = "+ idEmpleadoReceptor;
            }
            /*if(idEmpleadoReceptor>0){
                sqlFiltro =" AND ID_EMPLEADO_RECEPTOR = " + idEmpleadoEmisor;
            }*/
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            movilMensajeDto = movilMensajeDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_MOVIL_MENSAJE DESC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return movilMensajeDto;
    }
    
    private MovilMensaje movilMensaje = null;

    public MovilMensaje getMovilMensaje() {
        return movilMensaje;
    }

    public void setMovilMensaje(MovilMensaje movilMensaje) {
        this.movilMensaje = movilMensaje;
    }
    
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public MovilMensajeBO(Connection conn){
        this.conn = conn;
    }
    
    public MovilMensajeBO(int idMovilMensaje, Connection conn){
        this.conn = conn;
         try{
            MovilMensajeDaoImpl MovilMensajeDaoImpl = new MovilMensajeDaoImpl(this.conn);
            this.movilMensaje = MovilMensajeDaoImpl.findByPrimaryKey(idMovilMensaje);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean isUserConsola() {
        return userConsola;
    }

    public void setUserConsola(boolean userConsola) {
        this.userConsola = userConsola;
    }
    
    
}



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*package com.tsp.sct.bo;

import com.tsp.sct.dao.dto.MovilMensaje;
import com.tsp.sct.dao.jdbc.MovilMensajeDaoImpl;
import com.tsp.sct.util.DateManage;
import java.util.Date;

/**
 *
 * @author ISCesarMartinez
 */
/*public class MovilMensajeBO {

    public MovilMensaje[] getMovilMensajesByFilter(boolean recibidos, Date minFecha, Date maxFecha, 
            boolean filtroNoRecibidos, boolean filtroSoloComunicacionConsola, 
            int idReceptor, int idEmisor) {
        
        MovilMensaje[] resultado = new MovilMensaje[0];
        
        String strFechaMin =  DateManage.formatDateToSQL(minFecha);
        String strFechaMax = DateManage.formatDateToSQL(maxFecha);
        
        String strWhereRangoFechas="";
        
        String strWhereFiltroSoloRecibidos = "";
        String strWhereFiltroSoloComConsola = "";
        String strWhereFiltroReceptor = "";
        String strWhereFiltroEmisor = "";
        if(recibidos){
            strWhereFiltroReceptor = idReceptor>0?"ID_EMPLEADO_RECEPTOR="+idReceptor:"";
            strWhereFiltroSoloRecibidos = filtroNoRecibidos?"RECIBIDO=0":"";
            
            if (filtroSoloComunicacionConsola){
                strWhereFiltroSoloComConsola = filtroSoloComunicacionConsola?"EMISOR_TIPO=2":"";
            }else{
                strWhereFiltroEmisor = idEmisor>0?"ID_EMPLEADO_EMISOR="+idEmisor:"";
            }
            
            if (minFecha!=null && maxFecha!=null){
                strWhereRangoFechas="(CAST(FECHA_EMISION AS DATE) BETWEEN '"+strFechaMin+"' AND '"+strFechaMax+"')";
            }
            if (minFecha!=null && maxFecha==null){
                strWhereRangoFechas="(CAST(FECHA_EMISION AS DATE)  >= '"+strFechaMin+"')";
            }
            if (minFecha==null && maxFecha!=null){
                strWhereRangoFechas="(CAST(FECHA_EMISION AS DATE)  <= '"+strFechaMax+"')";
            }
        
        }else{
            //enviados
            strWhereFiltroEmisor = idEmisor>0?"ID_EMPLEADO_EMISOR="+idEmisor:"";
            strWhereFiltroSoloRecibidos = filtroNoRecibidos?"RECIBIDO=0":"";
            
            if (filtroSoloComunicacionConsola){
                strWhereFiltroSoloComConsola = filtroSoloComunicacionConsola?"RECEPTOR_TIPO=2":"";
            }else{
                strWhereFiltroReceptor = idReceptor>0?"ID_EMPLEADO_RECEPTOR="+idReceptor:"";
            }
            
            /*
            if (minFecha!=null && maxFecha!=null){
                strWhereRangoFechas+="(CAST(FECHA_RECEPCION AS DATE) BETWEEN '"+strFechaMin+"' AND '"+strFechaMax+"')";
            }
            if (minFecha!=null && maxFecha==null){
                strWhereRangoFechas="(CAST(FECHA_RECEPCION AS DATE)  >= '"+strFechaMin+"')";
            }
            if (minFecha==null && maxFecha!=null){
                strWhereRangoFechas="(CAST(FECHA_RECEPCION AS DATE)  <= '"+strFechaMax+"')";
            }*/
/*            if (minFecha!=null && maxFecha!=null){
                strWhereRangoFechas="(CAST(FECHA_EMISION AS DATE) BETWEEN '"+strFechaMin+"' AND '"+strFechaMax+"')";
            }
            if (minFecha!=null && maxFecha==null){
                strWhereRangoFechas="(CAST(FECHA_EMISION AS DATE)  >= '"+strFechaMin+"')";
            }
            if (minFecha==null && maxFecha!=null){
                strWhereRangoFechas="(CAST(FECHA_EMISION AS DATE)  <= '"+strFechaMax+"')";
            }
        }
        
        String strWhere = "ID_MOVIL_MENSAJE>0";
        
        if (strWhere.trim().replaceAll(" ", "").equals("")){
            strWhere = strWhereRangoFechas;
        }else if(!strWhereRangoFechas.equals("")){
            strWhere+=" AND "+strWhereRangoFechas;
        }
        
        if (strWhere.trim().replaceAll(" ", "").equals("")){
            strWhere = strWhereFiltroSoloRecibidos;
        }else if(!strWhereFiltroSoloRecibidos.equals("")){
            strWhere+=" AND "+strWhereFiltroSoloRecibidos;
        }
         
        if (strWhere.trim().replaceAll(" ", "").equals("")){
            strWhere = strWhereFiltroSoloComConsola;
        }else if(!strWhereFiltroSoloComConsola.equals("")){
            strWhere+=" AND "+strWhereFiltroSoloComConsola;
        }
        
        if (strWhere.trim().replaceAll(" ", "").equals("")){
            strWhere = strWhereFiltroReceptor;
        }else if(!strWhereFiltroReceptor.equals("")){
            strWhere+=" AND "+strWhereFiltroReceptor;
        }
        
        if (strWhere.trim().replaceAll(" ", "").equals("")){
            strWhere = strWhereFiltroEmisor;
        }else if(!strWhereFiltroEmisor.equals("")){
            strWhere+=" AND "+strWhereFiltroEmisor;
        }
        
        try{
            resultado = new MovilMensajeDaoImpl(this.conn).findByDynamicWhere(strWhere, new Object[0]);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return resultado;
    }
}*/
