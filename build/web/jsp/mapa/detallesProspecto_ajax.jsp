<%-- 
    Document   : detallesProspecto_ajax
    Created on : 14/01/2013, 02:49:13 PM
    Author     : Luis
--%>


<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.tsp.sct.dao.factory.EstatusDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.Estatus"%>
<%@page import="com.tsp.sct.dao.factory.SgfensProspectoDaoFactory"%>
<%@page import="com.tsp.sct.dao.dto.SgfensProspecto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    long id = 0l;
    try{
        id = Long.parseLong(request.getParameter("id"));
    }catch(Exception e){}
    if(id > 0){
        SgfensProspecto prospectoDto = null;
        try{
            prospectoDto = SgfensProspectoDaoFactory.create(user.getConn()).findByPrimaryKey(Integer.parseInt(request.getParameter("id")));
        }catch(Exception e){}
        if(prospectoDto!=null){
            Estatus estatusDto = null;
            try{
                estatusDto = EstatusDaoFactory.create(user.getConn()).findByPrimaryKey((int) prospectoDto.getIdEstatus());
            }catch(Exception e){}
            
            /*ProspectoSeguimiento[] seguimientosDto = ProspectoSeguimientoDaoFactory.create().findByDynamicWhere(""
                    + "ID_PROSPECTO = ? ORDER BY ID_PROSPECTO_SEGUIMIENTO DESC", new Object[]{prospectoDto.getIdProspecto()});
            
            String htmlSeguimiento = "";
            if(seguimientosDto.length>0){
                htmlSeguimiento = "<table class=\"data\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" >"
                        + "  <thead>"
                        + "        <tr>"
                        + "            <th>Comentario</th>"
                        + "        </tr>"
                        + "    </thead>";

                htmlSeguimiento +=  "    <tbody>";
                for(ProspectoSeguimiento seguimientoDto:seguimientosDto){
                    htmlSeguimiento += "<tr>"
                        + "<td>"
                        + seguimientoDto.getComentario()
                        + "</td>"
                        + "<tr>";
                }
                htmlSeguimiento +=  "    </tbody>"
                 + "</table>";
            }*/
            
            out.print(""
                    + "<p>"
                    + "      <label>Nombre:</label><br/>" 
                    + "      " + prospectoDto.getRazonSocial()
                    + "</p>"
                    + "<p>"
                    + "      <label>Estado:</label><br/>" 
                    + "      " + (estatusDto!=null?estatusDto.getNombre():"")
                    + "</p>"
                   /* + "<p>"
                    + "      <label>Monto interesado:</label><br/>" 
                    + "      " + prospectoDto.getMontoInteresado()
                    + "</p>" */
                    + "<p>"
                    + "      <label>Fecha de registro:</label><br/>" 
                    + "      " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(prospectoDto.getFechaRegistro())
                    + "</p>"
                 /*   + "<p>"
                    + "      <label>Seguimiento:</label><br/>" 
                    + "      " + htmlSeguimiento
                    + "</p>" */
                    );                                       
            
            
            
            out.print("<!--EXITO-->");
        }else{
            out.print("<!--ERROR-->No se encontr&oacute; informaci&oacute;n.");
        }
    }else{
        out.print("<!--ERROR-->No se encontr&oacute; informaci&oacute;n.");
    }
    
%>
