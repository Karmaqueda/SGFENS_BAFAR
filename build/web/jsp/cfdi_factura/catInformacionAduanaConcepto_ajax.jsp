<%-- 
    Document   : catInformacionAduanaConcepto_ajax
    Created on : 10/02/2016, 01:37:12 PM
    Author     : leonardo
--%>

<%@page import="com.tsp.sgfens.sesion.ComprobanteFiscalSesion"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.bo.AduanaBO"%>
<%@page import="java.util.Date"%>
<%@page import="com.tsp.sct.dao.jdbc.AduanaDaoImpl"%>
<%@page import="com.tsp.sct.dao.dto.Aduana"%>
<%@page import="com.tsp.sct.util.GenericValidator"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<jsp:useBean id="cfdiSesion" scope="session" class="com.tsp.sgfens.sesion.ComprobanteFiscalSesion"/>
<%
    String mode = "";
    
    int idEmpresa = user.getUser().getIdEmpresa();
    
    /*
    * Parámetros
    */
    int idAduana = -1;
    String nombreAduana ="";
    String numeroAduana ="";
    Date fecha = null;  
    
    int index_lista_producto = -1;
        
    /*
    * Recepción de valores
    */
    mode = request.getParameter("mode")!=null?request.getParameter("mode"):"";
    try{
        idAduana = Integer.parseInt(request.getParameter("idInfoAduana"));
    }catch(NumberFormatException ex){}
    try{
        index_lista_producto = Integer.parseInt(request.getParameter("index_lista_producto"));
    }catch(NumberFormatException ex){}
    nombreAduana = request.getParameter("nombreAduana")!=null?new String(request.getParameter("nombreAduana").getBytes("ISO-8859-1"),"UTF-8"):"";
    numeroAduana = request.getParameter("numeroAduana")!=null?new String(request.getParameter("numeroAduana").getBytes("ISO-8859-1"),"UTF-8"):"";
    try{ fecha = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fechaAduana"));}catch(Exception e){}  
       
    String msgError="";    
    if (cfdiSesion==null){
            cfdiSesion = new ComprobanteFiscalSesion();
    }
    
    System.out.println("... mode: "+ mode);
    
    /*
    * Validaciones del servidor
    */
    GenericValidator gc = new GenericValidator(); 
    if(!mode.equals("1") && !mode.equals("3")){
        if(!gc.isValidString(nombreAduana, 1, 30))
            msgError += "<ul>El dato 'nombre' es requerido.";
        if (fecha==null)
                msgError+="<ul> Debe seleccionar una fecha. ";
        if(idAduana <= 0 && (!mode.equals("")))
            msgError += "<ul>El dato ID 'Aduana' es requerido";
    }
    /*
    if(idVendedor<=0)
        msgError += "<ul>El dato 'Vendedor' es requerido";
 * */

    if(msgError.equals("")){
        if(idAduana>0){
            if (mode.equals("1")){
            /*
            * Editar
            */
/*                AduanaBO aduanaBO = new AduanaBO(idAduana,user.getConn());
                Aduana aduanaDto = aduanaBO.getAduana();
                
                aduanaDto.setIdEstatus(estatus);
                aduanaDto.setNombre(nombreAduana);
                aduanaDto.setDescripcion(descripcion);
               
                
                try{
                    new AduanaDaoImpl(user.getConn()).update(aduanaDto.createPk(), aduanaDto);
*/
                    out.print("<!--EXITO-->Registro actualizado satisfactoriamente");
/*                }catch(Exception ex){
                    out.print("<!--ERROR-->No se pudo actualizar el registro. Informe del error al administrador del sistema: " + ex.toString());
                    ex.printStackTrace();
                }
*/                
            }else if (mode.equals("3")){//eliminamos de sesion la informacion aduanera del concepto
                cfdiSesion.getListaProducto().get(index_lista_producto).getAduanas().remove((idAduana-1));//quitamos la aduana a la sesion, le restamos 1 porque lo inicializamos en 0 para que entrara a estos if, ademas de que la lista tiene a partir del index 0
                out.print("<!--EXITO-->Registro eliminado satisfactoriamente");
            }else{
                out.print("<!--ERROR-->Acción no válida.");
            }
        }else{
            /*
            *  Nuevo
            */
            
            try {                
                
                /**
                 * Creamos el registro de la aduana
                 */
                
/*                List<Aduana> aduanas = new ArrayList<Aduana>();
                try{
                    aduanas = cfdiSesion.getListaProducto().get(index_lista_producto).getAduanas();
                }catch(Exception ex){
                    msgError+="El producto especificado no se encontro en el listado en sesion. Intente de nuevo.";
                }
*/
                
                Aduana aduanaDto = new Aduana();
                AduanaDaoImpl aduanasDaoImpl = new AduanaDaoImpl(user.getConn());
               
               
                aduanaDto.setAduana(nombreAduana);
                aduanaDto.setNumDocumento(numeroAduana);
                aduanaDto.setFechaExpedicion(fecha);
                
                cfdiSesion.getListaProducto().get(index_lista_producto).getAduanas().add(aduanaDto);//agregamos la aduana a la sesion

                /**
                 * Realizamos el insert
                 */
                //aduanasDaoImpl.insert(aduanaDto);

                out.print("<!--EXITO-->Registro creado satisfactoriamente.<br/>");
            
            }catch(Exception e){
                e.printStackTrace();
                msgError += "Ocurrio un error al guardar el registro: " + e.toString() ;
            }
        }
    }else{
        out.print("<!--ERROR-->"+msgError);
    }

%>