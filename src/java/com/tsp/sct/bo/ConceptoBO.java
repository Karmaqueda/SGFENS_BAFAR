/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsp.sct.bo;

import com.tsp.sct.config.Configuration;
import com.tsp.sct.dao.dto.Concepto;
import com.tsp.sct.dao.dto.Empleado;
import com.tsp.sct.dao.dto.EmpleadoInventarioRepartidor;
import com.tsp.sct.dao.dto.EmpresaPermisoAplicacion;
import com.tsp.sct.dao.dto.Marca;
import com.tsp.sct.dao.dto.MovilMensaje;
import com.tsp.sct.dao.dto.Usuarios;
import com.tsp.sct.dao.exceptions.ConceptoDaoException;
import com.tsp.sct.dao.jdbc.ConceptoDaoImpl;
import com.tsp.sct.dao.jdbc.EmpresaPermisoAplicacionDaoImpl;
import com.tsp.sct.dao.jdbc.MarcaDaoImpl;
import com.tsp.sct.dao.jdbc.MovilMensajeDaoImpl;
import com.tsp.sct.mail.TspMailBO;
import com.tsp.sct.util.DateManage;
import com.tsp.sct.util.Encrypter;
import com.tsp.sct.util.GenericValidator;
import com.tsp.sct.util.StringManage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ISCesarMartinez
 */
public class ConceptoBO {
    
    private Concepto concepto = null;

    public Concepto getConcepto() {
        return concepto;
    }

    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
    }
        
    private Connection conn = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
        
    public ConceptoBO(Connection conn){
        this.conn = conn;
    }
    
     public ConceptoBO(int idConcepto, Connection conn){ 
        this.conn = conn;
        try{
            ConceptoDaoImpl ConceptoDaoImpl = new ConceptoDaoImpl(this.conn);
            this.concepto = ConceptoDaoImpl.findByPrimaryKey(idConcepto);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Concepto findConceptobyId(int idConcepto) throws Exception{
        Concepto concepto = null;
        
        try{            
            ConceptoDaoImpl conceptoDaoImpl = new ConceptoDaoImpl(this.conn);
            concepto = conceptoDaoImpl.findByPrimaryKey(idConcepto);
            if (concepto==null){
                throw new Exception("No se encontro ningun Concepto que corresponda con los parámetros específicados.");
            }
            if (concepto.getIdConcepto()<=0){
                throw new Exception("No se encontro ningun Concepto que corresponda con los parámetros específicados.");
            }
        }catch(ConceptoDaoException e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información de Concepto del usuario. Error: " + e.getMessage());
        }
        
        return concepto;
    }
    
    public Concepto getConceptoGenericoByEmpresa(int idEmpresa) throws Exception{
        Concepto Concepto = null;
        
        try{
            ConceptoDaoImpl ConceptoDaoImpl = new ConceptoDaoImpl(this.conn);
            //Concepto = ConceptoDaoImpl.findByDynamicWhere("ID_EMPRESA=" +idEmpresa + " AND GENERICO = 1", new Object[0])[0];
            
            Configuration appConfig = new Configuration();
            int idConceptoGenericoApp = Integer.parseInt(appConfig.getBd_sct_idconceptogenerico());
            Concepto = ConceptoDaoImpl.findByDynamicWhere("ID_CONCEPTO=" + idConceptoGenericoApp, new Object[0])[0];
            
            if (Concepto==null){
                throw new Exception("La empresa no tiene creado un producto Génerico");
            }
        }catch(ConceptoDaoException e){
            e.printStackTrace();
            throw new Exception("La empresa no tiene creado un producto Génerico");
        }
        
        return Concepto;
    }

    public void updateBD(Concepto concepto){
        //Checamos primero si se ha alcanzado el stock minimo
        if (concepto !=null){
            double stockNuevo = concepto.getNumArticulosDisponibles();
            double stockMinimo = concepto.getStockMinimo();
            if (concepto.getStockAvisoMin()==(short)1){
                if (stockNuevo<=stockMinimo)
                    this.enviaCorreoNotificacionStockMinimo(concepto);
            }
        }
        try{
            new ConceptoDaoImpl(this.conn).update(concepto.createPk(), concepto);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * Realiza una búsqueda por ID Concepto y otros filtros en busca de
     * coincidencias
     * @param idConcepto ID Del Concepto para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public Concepto[] findConceptos(int idConcepto, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        Concepto[] conceptoDto = new Concepto[0];
        ConceptoDaoImpl conceptoDao = new ConceptoDaoImpl(this.conn);
        try {
            String sqlFiltro="";
            if (idConcepto>0){
                sqlFiltro ="ID_CONCEPTO=" + idConcepto + " AND ";
            }else{
                sqlFiltro ="ID_CONCEPTO>0 AND";
            }
            if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro +=" ID_EMPRESA>0";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            conceptoDto = conceptoDao.findByDynamicWhere( 
                    sqlFiltro
                   // + " ORDER BY NOMBRE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return conceptoDto;
    }
    
    /**
     * Busca el numero de coincidencias por ID Concepto y otros filtros
     * @param idConcepto ID Del Concepto para filtrar, -1 para mostrar todos los registros
     * @param idEmpresa ID de la Empresa a filtrar marcas, -1 para evitar filtro
     * @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public int findCantidadConceptos(int idConcepto, int idEmpresa, int minLimit,int maxLimit, String filtroBusqueda) {
        int cantidad = 0;
        try {
            String sqlFiltro="";
            if (idConcepto>0){
                sqlFiltro ="ID_CONCEPTO=" + idConcepto + " AND ";
            }else{
                sqlFiltro ="ID_CONCEPTO>0 AND";
            }
            if (idEmpresa>0){                
                sqlFiltro += " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")";
            }else{
                sqlFiltro +=" ID_EMPRESA>0";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(ID_CONCEPTO) as cantidad FROM CONCEPTO WHERE " + 
                    sqlFiltro
                   // + " ORDER BY NOMBRE ASC"
                    + sqlLimit);
            if(rs.next()){
                cantidad = rs.getInt("cantidad");
            }
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return cantidad;
    }
    
    public String getConceptosByIdHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";
        Encrypter encripDesencri = new Encrypter();
        ConceptoBO conceptoBO = new ConceptoBO(this.conn);
        ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(this.conn);
        double stockGral = 0;
        try{
            Concepto[] conceptosDto = findConceptos(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (Concepto concepto:conceptosDto){
                try{
                    stockGral = 0;
                    stockGral = exisAlmBO.getExistenciaGeneralByEmpresaProducto(concepto.getIdEmpresa(), concepto.getIdConcepto());
                    
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==concepto.getIdConcepto())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+concepto.getIdConcepto()+"' "
                            + selectedStr
                            + "title='"+"Disponibles: "+(stockGral)+"\nDescripción: "+concepto.getDescripcion()+"'>"
                            + encripDesencri.decodeString(concepto.getNombre())
                            +"</option>";
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return strHTMLCombo;
    }
    
    public String getConceptosByIdHTMLCombo(int idEmpresa){
        String strHTMLCombo ="";
        Encrypter encripDesencri = new Encrypter();

        try{
            Concepto[] conceptosDto = findConceptos(-1, idEmpresa, 0, 0, " AND ID_ESTATUS!=2 ");
            
            for (Concepto concepto:conceptosDto){
                try{
                    //Categoria datosCategoria = new CategoriaDaoImpl().findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    //if (idSeleccionado==concepto.getIdConcepto())
                    //    selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+concepto.getIdConcepto()+"' "
                            + selectedStr
                            + "title='"+encripDesencri.decodeString(concepto.getNombre())+"'>"
                            + encripDesencri.decodeString(concepto.getNombre())
                            +"</option>";
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return strHTMLCombo;
    }
    
    public String getConceptoByIdEspecificoHTMLCombo(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";
        Encrypter encripDesencri = new Encrypter();
        ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(this.conn);
        double stockGral = 0;

        try{
            Concepto conceptosDto = findConceptobyId(idSeleccionado);
            
            
                try{
                    
                    stockGral = 0;
                    stockGral = exisAlmBO.getExistenciaGeneralByEmpresaProducto(concepto.getIdEmpresa(), concepto.getIdConcepto());
                    
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==conceptosDto.getIdConcepto())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+conceptosDto.getIdConcepto()+"' "
                            + selectedStr
                            + "title='"+"Disponibles: "+(stockGral)+"\nDescricion: "+conceptosDto.getDescripcion()+"'>"
                            + encripDesencri.decodeString(conceptosDto.getNombre())
                            +"</option>";
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            
        }catch(Exception e){
            e.printStackTrace();
        }

        return strHTMLCombo;
    }
    
    public String getNombreConceptoLegible(){
        String nombre = "";
        try{
            nombre = desencripta(this.concepto.getNombre());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return nombre;
    }
    
    public String encripta(String dato) throws IOException{
         Encrypter encripter = new Encrypter();
         return encripter.encodeString2(dato);        
    }
    
    public String desencripta(String dato) throws IOException{
         Encrypter desencripta = new Encrypter();
         return desencripta.decodeString(dato);        
    }
    
    public boolean enviaCorreoNotificacionStockMinimo(Concepto conceptoDto){
         //Validamos si es clienete de EVC
        int idEmpresa = conceptoDto.getIdEmpresa();
        EmpresaPermisoAplicacion empresaPermisoDto = null;
        ConceptoBO conceptoBO = new ConceptoBO(this.conn);
        ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(this.conn);
        double stockGral = 0;
        
        try{
            EmpresaPermisoAplicacionDaoImpl  empresaPermisoDao = new EmpresaPermisoAplicacionDaoImpl();
            empresaPermisoDto = empresaPermisoDao.findWhereIdEmpresaEquals(idEmpresa)[0];
        }catch(Exception e){e.printStackTrace();}
        
        boolean enviado = false;
        
        if (conceptoDto!=null){
            try {
                TspMailBO mailBO = new TspMailBO();
                
                if(empresaPermisoDto.getAccesoSgfensPretomovil()==1){
                    mailBO.setConfigurationMovilpyme();
                } else{
                    mailBO.setConfiguration();              
                }
                
                
                GenericValidator genericValidator = new GenericValidator();
                
                ArrayList<String> destinatarios = new ArrayList<String>();
                String nombreProducto = this.desencripta(conceptoDto.getNombre());
                
                
                
                try{
                    UsuariosBO usuariosBO = new UsuariosBO();
                    Usuarios[] desarrolladores = usuariosBO.findUsuariosByRol(conceptoDto.getIdEmpresa(), RolesBO.ROL_DESARROLLO);
                    Usuarios[] administradores = usuariosBO.findUsuariosByRol(conceptoDto.getIdEmpresa(), RolesBO.ROL_ADMINISTRADOR);
                    
                    for (Usuarios usuario:desarrolladores){
                        UsuarioBO usuarioBO = new UsuarioBO(usuario.getIdUsuarios());
                        try{destinatarios.add(usuarioBO.getLdap().getEmail());}catch(Exception ex){}
                    }
                    for (Usuarios usuario:administradores){
                        UsuarioBO usuarioBO = new UsuarioBO(usuario.getIdUsuarios());
                        try{destinatarios.add(usuarioBO.getLdap().getEmail());}catch(Exception ex){}
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                
                //Agregamos todos los correos de los usuarios Cuentas Por Pagar
                for (String destinatario : destinatarios){
                    try{                           
                        if(genericValidator.isEmail(destinatario)){
                            try{
                                mailBO.addTo(destinatario, "Administradores");
                            }catch(Exception ex){}
                        }
                    }catch(Exception ex){}
                }
                
                try{
                    //Obtenemos existencia general
                    stockGral = exisAlmBO.getExistenciaGeneralByEmpresaProducto(conceptoDto.getIdEmpresa(), conceptoDto.getIdConcepto());
                }catch(Exception e){}
                
                String msg = "<b>Buen día!</b><br/> "
                            + "<br/> El producto <b>'" + nombreProducto + "'</b> presenta desabasto."
                            + "<br/> ID producto: <i>"+conceptoDto.getIdConcepto()+"</i>"
                            + "<br/> SKU/Identificación: <i>"+conceptoDto.getIdentificacion()+"</i>"
                            + "<br/> Descripción: <i>"+conceptoDto.getDescripcion()+"</i>"
                            + "<br/> Stock mínimo: <i>"+conceptoDto.getStockMinimo()+"</i>"
                            + "<br/> Stock actual: <i>"+stockGral+"</i>";
                
                if(empresaPermisoDto.getAccesoSgfensPretomovil()==1){
                    mailBO.addMessageMovilpyme(msg ,1);
                } else{
                    mailBO.addMessage(msg ,1);
                 }
                
                mailBO.send("IMPORTANTE Stock Mínimo de producto alcanzado");
                
                enviado = true;
            } catch (Exception ex) {
                enviado = false;
                System.out.print("Ocurrio un error al intentar enviar el correo: " + ex.toString());
                ex.printStackTrace();
            }            
        }
        
        return enviado;
    }
    
    public Concepto[] conceptoDesencriptarNombre(Concepto[] conceptosDto){        
        for (Concepto item : conceptosDto){
            try {           
                item.setNombre(desencripta(item.getNombre()));} catch (IOException ex) {}
        }  
        return conceptosDto;
    }
    
    public Concepto[] conceptoOrdenaListaEnBaseNombre(Concepto[] conceptosDto){
        java.util.Arrays.sort(conceptosDto);
        return conceptosDto;
    }
    
    
    public String getConceptosByIdHTMLComboReload(int idEmpresa, int idSeleccionado, int minLimit , int maxLimit , String nombreConcepto, String filtroBusqueda){
        String strHTMLCombo ="";
        Encrypter encripDesencri = new Encrypter();
        ConceptoBO conceptoBO = new ConceptoBO(this.conn);
        ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(this.conn);
        double stockGral = 0;
        try{
            Concepto[] conceptosDto = findConceptos(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 AND  (NOMBRE_DESENCRIPTADO LIKE '%" + nombreConcepto + "%' "
                    + " OR CLAVE LIKE '%" + nombreConcepto + "%' OR IDENTIFICACION LIKE '%" + nombreConcepto + "%' OR ID_MARCA IN(SELECT ID_MARCA FROM MARCA WHERE NOMBRE LIKE '%" + nombreConcepto + "%') )"
                    + filtroBusqueda);
            if(conceptosDto.length<=0){
                conceptosDto = findConceptos(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 ");
            }
            String marca = "";
            MarcaDaoImpl marcaDao = new MarcaDaoImpl();
            for (Concepto concepto:conceptosDto){
                try{
                    marca = "";
                    stockGral = 0;
                    stockGral = exisAlmBO.getExistenciaGeneralByEmpresaProducto(concepto.getIdEmpresa(), concepto.getIdConcepto());
                    try{
                        marca =  marcaDao.findByPrimaryKey(concepto.getIdMarca()).getNombre();
                    }catch(Exception e){}
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==concepto.getIdConcepto())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+concepto.getIdConcepto()+"' "
                            + selectedStr
                            + "title='"+"Disponibles: "+(stockGral)+"\nDescripción: "+concepto.getDescripcion()+"'>"
                            + encripDesencri.decodeString(concepto.getNombre());
                            if(!concepto.getClave().equals("") && concepto.getClave()!=null){
                                strHTMLCombo += " , " + concepto.getClave();
                            }
                            if(!marca.equals("")){
                                strHTMLCombo += " , " + marca;
                            }           
                            strHTMLCombo += "</option>";
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return strHTMLCombo;
    }
    
    public String getConceptosGranelByIdHTMLComboReload(int idEmpresa, int idSeleccionado, int minLimit , int maxLimit , String nombreConcepto, String filtro){
        String strHTMLCombo ="";
        Encrypter encripDesencri = new Encrypter();
        ConceptoBO conceptoBO = new ConceptoBO(this.conn);
        ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(this.conn);
        double stockGral = 0;
        try{
            Concepto[] conceptosDto = findConceptos(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 AND  (NOMBRE_DESENCRIPTADO LIKE '%" + nombreConcepto + "%' "
                    + " OR CLAVE LIKE '%" + nombreConcepto + "%' OR IDENTIFICACION LIKE '%" + nombreConcepto + "%' OR ID_MARCA IN(SELECT ID_MARCA FROM MARCA WHERE NOMBRE LIKE '%" + nombreConcepto + "%') ) " + filtro);
            if(conceptosDto.length<=0){
                conceptosDto = findConceptos(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 ");
            }
            String marca = "";
            MarcaDaoImpl marcaDao = new MarcaDaoImpl();
            for (Concepto concepto:conceptosDto){
                try{
                    marca = "";
                    stockGral = 0;
                    stockGral = exisAlmBO.getExistenciaGeneralByEmpresaProducto(concepto.getIdEmpresa(), concepto.getIdConcepto());
                    try{
                        marca =  marcaDao.findByPrimaryKey(concepto.getIdMarca()).getNombre();
                    }catch(Exception e){}
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==concepto.getIdConcepto())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+concepto.getIdConcepto()+"' "
                            + selectedStr
                            + "title='"+"Disponibles: "+(stockGral)+"\nDescripción: "+concepto.getDescripcion()+"'>"
                            + encripDesencri.decodeString(concepto.getNombre());
                            if(!concepto.getClave().equals("") && concepto.getClave()!=null){
                                strHTMLCombo += " , " + concepto.getClave();
                            }
                            if(!marca.equals("")){
                                strHTMLCombo += " , " + marca;
                            }           
                            strHTMLCombo += "</option>";
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return strHTMLCombo;
    }
    
    
    public String getConceptoByIdEspecificoHTMLComboReload(int idEmpresa, int idSeleccionado){
        String strHTMLCombo ="";
        Encrypter encripDesencri = new Encrypter();
        ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(this.conn);
        double stockGral = 0;
        String marca = "";
        MarcaDaoImpl marcaDao = new MarcaDaoImpl();

        try{
            Concepto conceptosDto = findConceptobyId(idSeleccionado);
            
            
                try{
                    marca = "";
                    stockGral = 0;
                    stockGral = exisAlmBO.getExistenciaGeneralByEmpresaProducto(conceptosDto.getIdEmpresa(), conceptosDto.getIdConcepto());
                    
                    try{
                        marca =  marcaDao.findByPrimaryKey(conceptosDto.getIdMarca()).getNombre();
                    }catch(Exception e){}
                    
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==conceptosDto.getIdConcepto())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+conceptosDto.getIdConcepto()+"' "
                            + selectedStr
                            + "title='"+"Disponibles: "+(stockGral)+"\nDescripción: "+conceptosDto.getDescripcion()+"'>"
                            + encripDesencri.decodeString(conceptosDto.getNombre());
                            if(!conceptosDto.getClave().equals("") && conceptosDto.getClave()!=null){
                                strHTMLCombo += " , " + conceptosDto.getClave();
                            }
                            if(!marca.equals("")){
                                strHTMLCombo += " , " + marca;
                            }           
                            strHTMLCombo += "</option>";
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            
        }catch(Exception e){
            e.printStackTrace();
        }

        return strHTMLCombo;
    }
    
    /**
    * Genera un folio con los siguientes parametros:
    * 
    * letras CON, id_empresa a 3 dígitos, 1 guion, "CON" (de Consola), 1 guion, l número d folio
    * de acuerdo a fecha y hora
    * 
    * p. ejem:  CON001-CON-20130131100101111
    * 
    * @param idEmpresa
    * @param idEmpleado
    * @return
    */
    public static String generaFolioMovil(int idEmpresa){
        String folio;
        String empresa = StringManage.getExactString(""+idEmpresa, 3, '0', StringManage.FILL_DIRECTION_LEFT);

        try{Thread.sleep(100);}catch(Exception ex){}
        String folioConsecutivo = DateManage.getDateHourString();

        folio = "CON" + empresa + "-CON-" + folioConsecutivo;

        return folio;
    }
            
    
    public String getConceptosByIdHTMLCombo(int idEmpresa, int idSeleccionado , int minLimit , int maxLimit , String filtro){
        String strHTMLCombo ="";
        Encrypter encripDesencri = new Encrypter();
        ConceptoBO conceptoBO = new ConceptoBO(this.conn);
        ExistenciaAlmacenBO exisAlmBO = new ExistenciaAlmacenBO(this.conn);
        double stockGral = 0;
        try{
            Concepto[] conceptosDto = findConceptos(-1, idEmpresa, minLimit, maxLimit, " AND ID_ESTATUS!=2 " + filtro );
            
            for (Concepto concepto:conceptosDto){
                try{
                    stockGral = 0;
                    stockGral = exisAlmBO.getExistenciaGeneralByEmpresaProducto(concepto.getIdEmpresa(), concepto.getIdConcepto());
                    
                    //Categoria datosCategoria = new CategoriaDaoImpl(this.conn).findByPrimaryKey(categoria.getIdCategoria());
                    String selectedStr="";

                    if (idSeleccionado==concepto.getIdConcepto())
                        selectedStr = " selected ";

                    strHTMLCombo += "<option value='"+concepto.getIdConcepto()+"' "
                            + selectedStr
                            + "title='"+"Disponibles: "+(stockGral)+"\nDescripción: "+concepto.getDescripcion()+"'>"
                            + encripDesencri.decodeString(concepto.getNombre())
                            +"</option>";
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return strHTMLCombo;
    }
    
    
    public boolean enviaNotificacionStockMinimoSincronizaPedido(Concepto conceptoDto, Empleado empleadoDto){
         //Validamos si es clienete de EVC
        int idEmpresa = conceptoDto.getIdEmpresa();
        EmpresaPermisoAplicacion empresaPermisoDto = null;
        ConceptoBO conceptoBO = new ConceptoBO(this.conn);
        EmpleadoInventarioRepartidorBO emInventarioRepartidorBO = new EmpleadoInventarioRepartidorBO(this.conn);
        EmpleadoInventarioRepartidor emInventarioRepartidorDto = null;
        
        try{
            EmpresaPermisoAplicacionDaoImpl  empresaPermisoDao = new EmpresaPermisoAplicacionDaoImpl();
            empresaPermisoDto = empresaPermisoDao.findWhereIdEmpresaEquals(idEmpresa)[0];
        }catch(Exception e){e.printStackTrace();}
        
        boolean enviado = false;
        
        if (conceptoDto!=null){
            try {
                TspMailBO mailBO = new TspMailBO();
                
                if(empresaPermisoDto.getAccesoSgfensPretomovil()==1){
                    mailBO.setConfigurationMovilpyme();
                } else{
                    mailBO.setConfiguration();              
                }
                
                
                GenericValidator genericValidator = new GenericValidator();
                
                ArrayList<String> destinatarios = new ArrayList<String>();
                String nombreProducto = this.desencripta(conceptoDto.getNombre());
                
                
                
                try{
                    UsuariosBO usuariosBO = new UsuariosBO();
                    Usuarios[] desarrolladores = usuariosBO.findUsuariosByRol(conceptoDto.getIdEmpresa(), RolesBO.ROL_DESARROLLO);
                    Usuarios[] administradores = usuariosBO.findUsuariosByRol(conceptoDto.getIdEmpresa(), RolesBO.ROL_ADMINISTRADOR);
                    
                    for (Usuarios usuario:desarrolladores){
                        UsuarioBO usuarioBO = new UsuarioBO(usuario.getIdUsuarios());
                        try{destinatarios.add(usuarioBO.getLdap().getEmail());}catch(Exception ex){}
                    }
                    for (Usuarios usuario:administradores){
                        UsuarioBO usuarioBO = new UsuarioBO(usuario.getIdUsuarios());
                        try{destinatarios.add(usuarioBO.getLdap().getEmail());}catch(Exception ex){}
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                
                //Agregamos todos los correos de los usuarios Cuentas Por Pagar
                for (String destinatario : destinatarios){
                    try{                           
                        if(genericValidator.isEmail(destinatario)){
                            try{
                                mailBO.addTo(destinatario, "Administradores");
                            }catch(Exception ex){}
                        }
                    }catch(Exception ex){}
                }
                
                try{
                    //Obtenemos existencia 
                    emInventarioRepartidorDto = emInventarioRepartidorBO.findEmpleadoInventarioRepartidors(-1, empleadoDto.getIdEmpleado(), 0, 0, " AND ID_CONCEPTO = " + conceptoDto.getIdConcepto())[0];
                    
                }catch(Exception e){}
                
                String msg = "<b>Buen día!</b><br/> "
                            + "<br/> El producto <b>'" + nombreProducto + "'</b> presenta desabasto en el Inventario Asignado al Vendedor."
                            + "<br/> ID producto: <i>"+conceptoDto.getIdConcepto()+"</i>"
                            + "<br/> SKU/Identificación: <i>"+conceptoDto.getIdentificacion()+"</i>"
                            + "<br/> Descripción: <i>"+conceptoDto.getDescripcion()+"</i>"                           
                            + "<br/> Stock actual del Vendedor: <i>"+ (emInventarioRepartidorDto!=null?emInventarioRepartidorDto.getCantidad():0) +"</i>";
                
                if(empresaPermisoDto.getAccesoSgfensPretomovil()==1){
                    mailBO.addMessageMovilpyme(msg ,1);
                } else{
                    mailBO.addMessage(msg ,1);
                 }
                
                mailBO.send("IMPORTANTE Stock insuficiente de producto asignado al Vendedor");
                
                enviado = true;
                
                
                //Envia msj a consola y user por chat
                
                try{                

                    /**
                     * Creamos el registro de MovilMensaje
                     */
                    MovilMensaje mMovilMensajeDto = new MovilMensaje();
                    MovilMensajeDaoImpl mMovilMensajesDaoImpl = new MovilMensajeDaoImpl(this.conn);


                    //Empleado
                    mMovilMensajeDto.setEmisorTipo(2);
                    mMovilMensajeDto.setIdEmpleadoEmisor(0);
                    mMovilMensajeDto.setReceptorTipo(1);
                    mMovilMensajeDto.setIdEmpleadoReceptor(empleadoDto.getIdEmpleado());
                    mMovilMensajeDto.setFechaEmision(new Date());
                    mMovilMensajeDto.setFechaRecepcion(null);
                    mMovilMensajeDto.setMensaje("No cuentas con la cantidad suficiente de producto '" + nombreProducto +"' asignado en consola para sincronizar pedidos. Consulte a su Administrador.");
                    mMovilMensajeDto.setRecibido(0);
                    /**
                     * Realizamos el insert
                     */
                    mMovilMensajesDaoImpl.insert(mMovilMensajeDto);
                
                    //Consola
                    mMovilMensajeDto = new MovilMensaje();
                   
                    mMovilMensajeDto.setEmisorTipo(2);
                    mMovilMensajeDto.setIdEmpleadoEmisor(0);
                    mMovilMensajeDto.setReceptorTipo(2);
                    mMovilMensajeDto.setIdEmpleadoReceptor(0);
                    mMovilMensajeDto.setFechaEmision(new Date());
                    mMovilMensajeDto.setFechaRecepcion(null);
                    mMovilMensajeDto.setMensaje("El vendedor "+ empleadoDto.getNombre() +" " +empleadoDto.getApellidoPaterno() + " " + empleadoDto.getApellidoMaterno()
                            + " no cuenta con la cantidad suficiente de producto '" + nombreProducto +"' asignado en consola para sincronizar pedidos.");
                    mMovilMensajeDto.setRecibido(0);
                    
                    
                    mMovilMensajesDaoImpl.insert(mMovilMensajeDto);

                }catch(Exception e){
                    e.printStackTrace();
                   
                }               
                
                
                
            } catch (Exception ex) {
                enviado = false;
                System.out.print("Ocurrio un error al intentar enviar el correo: " + ex.toString());
                ex.printStackTrace();
            }            
        }
        
        return enviado;
    }
    
    public Concepto getConceptoByNombre(long idEmpresa, String nombre) throws Exception{
        Concepto Concepto = null;
        
        
        try{
            ConceptoDaoImpl ConceptoDaoImpl = new ConceptoDaoImpl(this.conn);   
            
            Concepto = ConceptoDaoImpl.findByDynamicWhere(" ( NOMBRE_DESENCRIPTADO ='" + nombre + "' OR NOMBRE ='"+ encripta(nombre)+"' ) AND "
                    + " ID_EMPRESA IN (SELECT ID_EMPRESA FROM EMPRESA WHERE ID_EMPRESA_PADRE = " + idEmpresa + " OR ID_EMPRESA= " + idEmpresa + ")", new Object[0])[0];
            
            if (Concepto==null){
                throw new Exception("La empresa no tiene creado un producto con los datos especificados");
            }
        }catch(ConceptoDaoException e){
            //e.printStackTrace();
            throw new Exception("La empresa no tiene creado un producto con los datos especificados");
        }
        
        return Concepto;
    }
    
    /**
     * Realiza la copia a bajo nivel de un DTO de concepto
     * por lo cual copia los valores y ademas internamente el DTO
     * setea los valores "modified" a true, con lo cual se consigue
     * que en caso de hacer un Insert de este nuevo Objeto, los datos
     * si sean incluidos en la sentencia SQL.
     * Ademas se evita que el nuevo concepto tenga referencia en memoria al
     * concepto original y por lo tanto se sobreescriban valores si es que el DTO
     * original se quiere seguir usando para otros fines.
     * @param conceptoOrigen
     * @return 
     */
    public Concepto clonar(Concepto conceptoOrigen){
        Concepto conceptoClon = new Concepto();
        
        conceptoClon.setCaracteristiscas(conceptoOrigen.getCaracteristiscas());
        conceptoClon.setClave(conceptoOrigen.getClave());
        conceptoClon.setClaveartSae(conceptoOrigen.getClaveartSae());
        conceptoClon.setComisionMonto(conceptoOrigen.getComisionMonto());
        conceptoClon.setComisionPorcentaje(conceptoOrigen.getComisionPorcentaje());
        conceptoClon.setDescripcion(conceptoOrigen.getDescripcion());
        conceptoClon.setDescripcionCorta(conceptoOrigen.getDescripcionCorta());
        conceptoClon.setDescuentoMonto(conceptoOrigen.getDescuentoMonto());
        conceptoClon.setDescuentoPorcentaje(conceptoOrigen.getDescuentoPorcentaje());
        conceptoClon.setDesglosePiezas(conceptoOrigen.getDesglosePiezas());
        conceptoClon.setDetalle(conceptoOrigen.getDetalle());
        conceptoClon.setFechaAlta(conceptoOrigen.getFechaAlta());
        conceptoClon.setFechaCaducidad(conceptoOrigen.getFechaCaducidad());
        conceptoClon.setFolioConceptoMovil(conceptoOrigen.getFolioConceptoMovil());
        conceptoClon.setGenerico(conceptoOrigen.getGenerico());
        conceptoClon.setIdAlmacen(conceptoOrigen.getIdAlmacen());
        conceptoClon.setIdCategoria(conceptoOrigen.getIdCategoria());
        conceptoClon.setIdConcepto(conceptoOrigen.getIdConcepto());
        conceptoClon.setIdConceptoPadre(conceptoOrigen.getIdConceptoPadre());
        conceptoClon.setIdEmbalaje(conceptoOrigen.getIdEmbalaje());
        conceptoClon.setIdEmpresa(conceptoOrigen.getIdEmpresa());
        conceptoClon.setIdEstatus(conceptoOrigen.getIdEstatus());
        conceptoClon.setIdImpuesto(conceptoOrigen.getIdImpuesto());
        conceptoClon.setIdMarca(conceptoOrigen.getIdMarca());
        conceptoClon.setIdSubcategoria(conceptoOrigen.getIdSubcategoria());
        conceptoClon.setIdSubcategoria2(conceptoOrigen.getIdSubcategoria2());
        conceptoClon.setIdSubcategoria3(conceptoOrigen.getIdSubcategoria3());
        conceptoClon.setIdSubcategoria4(conceptoOrigen.getIdSubcategoria4());
        conceptoClon.setIdentificacion(conceptoOrigen.getIdentificacion());
        conceptoClon.setImagenCarpetaArchivo(conceptoOrigen.getImagenCarpetaArchivo());
        conceptoClon.setImagenNombreArchivo(conceptoOrigen.getImagenNombreArchivo());
        conceptoClon.setImpuestoXConcepto(conceptoOrigen.getImpuestoXConcepto());
        conceptoClon.setMaxMedioMayoreo(conceptoOrigen.getMaxMedioMayoreo());
        conceptoClon.setMaxMenudeo(conceptoOrigen.getMaxMenudeo());
        conceptoClon.setMinMayoreo(conceptoOrigen.getMinMayoreo());
        conceptoClon.setMinMedioMayoreo(conceptoOrigen.getMinMedioMayoreo());
        conceptoClon.setNombre(conceptoOrigen.getNombre());
        conceptoClon.setNombreDesencriptado(conceptoOrigen.getNombreDesencriptado());
        conceptoClon.setNumArticulosDisponibles(conceptoOrigen.getNumArticulosDisponibles());
        conceptoClon.setNumeroLote(conceptoOrigen.getNumeroLote());
        conceptoClon.setObservaciones(conceptoOrigen.getObservaciones());
        conceptoClon.setPeso(conceptoOrigen.getPeso());
        conceptoClon.setPrecio(conceptoOrigen.getPrecio());
        conceptoClon.setPrecioCompra(conceptoOrigen.getPrecioCompra());
        conceptoClon.setPrecioDocena(conceptoOrigen.getPrecioDocena());
        conceptoClon.setPrecioEspecial(conceptoOrigen.getPrecioEspecial());
        conceptoClon.setPrecioEspecialGranel(conceptoOrigen.getPrecioEspecialGranel());
        conceptoClon.setPrecioMayoreo(conceptoOrigen.getPrecioMayoreo());
        conceptoClon.setPrecioMayoreoGranel(conceptoOrigen.getPrecioMayoreoGranel());
        conceptoClon.setPrecioMedioGranel(conceptoOrigen.getPrecioMedioGranel());
        conceptoClon.setPrecioMedioMayoreo(conceptoOrigen.getPrecioMedioMayoreo());
        conceptoClon.setPrecioMinimoVenta(conceptoOrigen.getPrecioMinimoVenta());
        conceptoClon.setPrecioUnitarioGranel(conceptoOrigen.getPrecioUnitarioGranel());
        conceptoClon.setRutaImagen(conceptoOrigen.getRutaImagen());
        conceptoClon.setRutaVideo(conceptoOrigen.getRutaVideo());
        conceptoClon.setSincronizacionMicrosip(conceptoOrigen.getSincronizacionMicrosip());
        conceptoClon.setStockAvisoMin(conceptoOrigen.getStockAvisoMin());
        conceptoClon.setStockMinimo(conceptoOrigen.getStockMinimo());
        conceptoClon.setVolumen(conceptoOrigen.getVolumen());
        conceptoClon.setMateriaPrima(conceptoOrigen.getMateriaPrima());
                
        return conceptoClon;
    }
    
    public void actualizaDatosSubProductos(Concepto conceptoPadre){
        if (conceptoPadre.getIdConcepto()<=0)
            return;
        
        Concepto[] subProductos = findConceptos(-1, conceptoPadre.getIdEmpresa(), 0, 0, " AND ID_CONCEPTO_PADRE = " + conceptoPadre.getIdConcepto() );
        
        ConceptoDaoImpl conceptoDao =  new ConceptoDaoImpl(this.conn);
        for (Concepto conceptoHijo : subProductos){
            conceptoHijo.setCaracteristiscas(conceptoPadre.getCaracteristiscas());
            conceptoHijo.setClave(conceptoPadre.getClave());
            conceptoHijo.setClaveartSae(conceptoPadre.getClaveartSae());
            conceptoHijo.setComisionMonto(conceptoPadre.getComisionMonto());
            conceptoHijo.setComisionPorcentaje(conceptoPadre.getComisionPorcentaje());
            //conceptoHijo.setDescripcion(conceptoPadre.getDescripcion());
            //conceptoHijo.setDescripcionCorta(conceptoPadre.getDescripcionCorta());
            conceptoHijo.setDescuentoMonto(conceptoPadre.getDescuentoMonto());
            conceptoHijo.setDescuentoPorcentaje(conceptoPadre.getDescuentoPorcentaje());
            conceptoHijo.setDesglosePiezas(conceptoPadre.getDesglosePiezas());
            conceptoHijo.setDetalle(conceptoPadre.getDetalle());
            //conceptoHijo.setFechaAlta(conceptoPadre.getFechaAlta());
            //conceptoHijo.setFechaCaducidad(conceptoPadre.getFechaCaducidad());
            //conceptoHijo.setFolioConceptoMovil(conceptoPadre.getFolioConceptoMovil());
            //conceptoHijo.setGenerico(conceptoPadre.getGenerico());
            //conceptoHijo.setIdAlmacen(conceptoPadre.getIdAlmacen());
            conceptoHijo.setIdCategoria(conceptoPadre.getIdCategoria());
            conceptoHijo.setIdEmbalaje(conceptoPadre.getIdEmbalaje());
            //conceptoHijo.setIdEmpresa(conceptoPadre.getIdEmpresa());
            //conceptoHijo.setIdEstatus(conceptoPadre.getIdEstatus());
            conceptoHijo.setIdImpuesto(conceptoPadre.getIdImpuesto());
            conceptoHijo.setIdMarca(conceptoPadre.getIdMarca());
            conceptoHijo.setIdSubcategoria(conceptoPadre.getIdSubcategoria());
            conceptoHijo.setIdSubcategoria2(conceptoPadre.getIdSubcategoria2());
            conceptoHijo.setIdSubcategoria3(conceptoPadre.getIdSubcategoria3());
            conceptoHijo.setIdSubcategoria4(conceptoPadre.getIdSubcategoria4());
            conceptoHijo.setIdentificacion(conceptoPadre.getIdentificacion());//codigo de barras
            conceptoHijo.setImagenCarpetaArchivo(conceptoPadre.getImagenCarpetaArchivo());
            conceptoHijo.setImagenNombreArchivo(conceptoPadre.getImagenNombreArchivo());
            conceptoHijo.setImpuestoXConcepto(conceptoPadre.getImpuestoXConcepto());
            conceptoHijo.setMaxMedioMayoreo(conceptoPadre.getMaxMedioMayoreo());
            conceptoHijo.setMaxMenudeo(conceptoPadre.getMaxMenudeo());
            conceptoHijo.setMinMayoreo(conceptoPadre.getMinMayoreo());
            conceptoHijo.setMinMedioMayoreo(conceptoPadre.getMinMedioMayoreo());
            //conceptoHijo.setNombre(conceptoPadre.getNombre());
            //conceptoHijo.setNombreDesencriptado(conceptoPadre.getNombreDesencriptado());
            //conceptoHijo.setNumArticulosDisponibles(conceptoPadre.getNumArticulosDisponibles());
            //conceptoHijo.setNumeroLote(conceptoPadre.getNumeroLote());
            conceptoHijo.setObservaciones(conceptoPadre.getObservaciones());
            //conceptoHijo.setPeso(conceptoPadre.getPeso());
            conceptoHijo.setPrecio(conceptoPadre.getPrecio());
            conceptoHijo.setPrecioCompra(conceptoPadre.getPrecioCompra());
            conceptoHijo.setPrecioDocena(conceptoPadre.getPrecioDocena());
            conceptoHijo.setPrecioEspecial(conceptoPadre.getPrecioEspecial());
            conceptoHijo.setPrecioEspecialGranel(conceptoPadre.getPrecioEspecialGranel());
            conceptoHijo.setPrecioMayoreo(conceptoPadre.getPrecioMayoreo());
            conceptoHijo.setPrecioMayoreoGranel(conceptoPadre.getPrecioMayoreoGranel());
            conceptoHijo.setPrecioMedioGranel(conceptoPadre.getPrecioMedioGranel());
            conceptoHijo.setPrecioMedioMayoreo(conceptoPadre.getPrecioMedioMayoreo());
            conceptoHijo.setPrecioMinimoVenta(conceptoPadre.getPrecioMinimoVenta());
            conceptoHijo.setPrecioUnitarioGranel(conceptoPadre.getPrecioUnitarioGranel());
            conceptoHijo.setRutaImagen(conceptoPadre.getRutaImagen());
            conceptoHijo.setRutaVideo(conceptoPadre.getRutaVideo());
            //conceptoHijo.setSincronizacionMicrosip(conceptoPadre.getSincronizacionMicrosip());
            //conceptoHijo.setStockAvisoMin(conceptoPadre.getStockAvisoMin());
            //conceptoHijo.setStockMinimo(conceptoPadre.getStockMinimo());
            //conceptoHijo.setVolumen(conceptoPadre.getVolumen());
            conceptoHijo.setMateriaPrima(conceptoPadre.getMateriaPrima());
            
            try{
                conceptoDao.update(conceptoHijo.createPk(), conceptoHijo);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    
    
    /**
     * Busca los conceptos de una empresa, pudiendo filtrar por solo los que no
     * tienen nombre desencriptado.
     * Desencripta la columna 'nombre' de Concepto y coloca el resultado
     * en la columna 'nombreDesencriptado'. Después actualiza el registro en BD.
     * @param idEmpresa idEmpresa a filtrar los conceptos
     * @param todos true indica que se actualizan todos los conceptos sin ningun filtro mas que de empresa, 
     *              false indica que se actualizan solo los conceptos de la empresa que no tengan valor en el campo 'nombre_desencriptado'
     */
    public void desencriptaActualizaNombresBD(int idEmpresa, boolean todos){
        Concepto[] conceptosActualizar = findConceptos(-1, idEmpresa, 0, 0, todos? "" : " AND (NOMBRE_DESENCRIPTADO IS NULL OR NOMBRE_DESENCRIPTADO = '') ");
        if (conceptosActualizar.length>0){
            desencriptaActualizaNombresBD(conceptosActualizar);
        }
    }
    
    /**
     * Desencripta la columna 'nombre' de Concepto y coloca el resultado
     * en la columna 'nombreDesencriptado'. Después actualiza el registro en BD.
     * @param conceptos Arreglo de objetos DTO de tipo Concepto que seran actualizados en BD 
     */
    private void desencriptaActualizaNombresBD(Concepto[] conceptos){
        ConceptoDaoImpl conceptoDao = new ConceptoDaoImpl(conn);
        for (Concepto item : conceptos){
            try {           
                item.setNombreDesencriptado(desencripta(item.getNombre()));
                conceptoDao.update(item.createPk(), item);
            } catch (Exception ex) {
            }
        }
    }
    
    public static enum Ordenamiento {
        CANTIDAD_ASIGNADA_MAYOR_MENOR (1, null, " CANTIDAD DESC ", " CANTIDAD_ASIGNADA DESC "),
        ALFABETICO_A_Z (2, " NOMBRE_DESENCRIPTADO ASC ", " NOMBRE_DESENCRIPTADO ASC ", " NOMBRE_DESENCRIPTADO ASC "),
        ALFABETICO_Z_A (3, " NOMBRE_DESENCRIPTADO DESC ", " NOMBRE_DESENCRIPTADO DESC ", " NOMBRE_DESENCRIPTADO DESC "),
        CODIGO_MAYOR_MENOR (4, " IDENTIFICACION DESC ", " IDENTIFICACION DESC ", " IDENTIFICACION DESC "),
        ID_MAYOR_MENOR (5, " ID_CONCEPTO DESC ", " ID_CONCEPTO DESC ", " ID_CONCEPTO DESC "),
        FECHA_CREACION_MAS_RECIENTE (6, " FECHA_ALTA DESC ", " FECHA_ALTA DESC ", " FECHA_ALTA DESC "),
        //VENDIDO_MAYOR_MENOR (7, null, null, null),
        //VENDIDO_MENOR_MAYOR (8, null, null, null),
        FECHA_INICIAL_CORTE_DESC (9, null, null, " FECHA_INICIAL_CORTE DESC "),
        ;
        
        private final int id;
        private final String sqlOrderBy;
        private final String sqlOrderByCortes;
        private final String sqlOrderByCorteHistorico;
        
        private Ordenamiento(int id, String sqlOrderBy, String sqlOrderByCortes, String sqlOrderByCorteHistorico){
            this.id = id;
            this.sqlOrderBy = sqlOrderBy;
            this.sqlOrderByCortes = sqlOrderByCortes;
            this.sqlOrderByCorteHistorico = sqlOrderByCorteHistorico;
        }
        
        public String getSqlOrderBy(){return sqlOrderBy;}
        public int getId(){return id;}
        public String getSqlOrderByCortes() {return sqlOrderByCortes;}
        public String getSqlOrderByCorteHistorico() {return sqlOrderByCorteHistorico;}
        
        
    }
    
}
