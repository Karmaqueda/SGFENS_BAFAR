<%-- 
    Document   : metodosPagoSelect
    Created on : 2/06/2016, 02:48:23 PM
    Author     : ISCesar
--%>
<%@page import="com.tsp.sct.util.StringManage"%>
<%@page import="com.tsp.sct.dao.dto.TipoPago"%>
<%@page import="com.tsp.sct.bo.TipoPagoBO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="session" class="com.tsp.sct.bo.UsuarioBO"/>
<%
    String idNameTipoPago = request.getParameter("id_name_tipo_pago") != null ? new String(request.getParameter("id_name_tipo_pago").getBytes("ISO-8859-1"), "UTF-8") : "id_tipo_pago";
    String idNameSelectMetodoPago = request.getParameter("id_name_select_metodo_pago") != null ? new String(request.getParameter("id_name_select_metodo_pago").getBytes("ISO-8859-1"), "UTF-8") : "metodo_pago";
    String idNameDescMetodo = request.getParameter("id_name_desc_metodo") != null ? new String(request.getParameter("id_name_desc_metodo").getBytes("ISO-8859-1"), "UTF-8") : "tipo_pago_descripcion";
    
    int valueIdTipoPago = -1;
    try {
        valueIdTipoPago = Integer.parseInt(request.getParameter("value_id_tipo_pago"));
    } catch (Exception ex) {}
    
    String claveMetodoPagoSAT  = "";
    String descTipoPago = "";
    TipoPago tipoPago =  new TipoPagoBO(valueIdTipoPago, user.getConn()).getTipoPago();
    if (valueIdTipoPago>0){
        if (tipoPago!=null){
            descTipoPago = tipoPago.getDescTipoPago();
            if (StringManage.getValidString(tipoPago.getClaveMetodoSat()).length()==2){
                claveMetodoPagoSAT = StringManage.getValidString(tipoPago.getClaveMetodoSat());
            }
        }
    }
%>
    <script>
        function actualizaDescMetodo(){
            var valueSelected = $("#<%= idNameSelectMetodoPago %>").val();
            var textSelected = $("#<%= idNameSelectMetodoPago %> option:selected").text();
            if (valueSelected != '-1'){ //si se eligio un valor válido
                $("#<%=idNameDescMetodo%>").val(textSelected);
            }else{
                $("#<%=idNameDescMetodo%>").val('');
            }
        }
    </script>
    <input type="hidden" id="<%= idNameTipoPago %>" name="<%= idNameTipoPago %>" value="<%= valueIdTipoPago %>"/>
    <input type="hidden" id="<%= idNameDescMetodo %>" name="<%= idNameDescMetodo %>" value="<%= descTipoPago %>"/>
    <select name="<%= idNameSelectMetodoPago %>" id="<%= idNameSelectMetodoPago %>" onchange="actualizaDescMetodo();">
        <option value="-1">-Seleccione método-</option>
        <option value="01" <%= claveMetodoPagoSAT.equals("01")?"selected":"" %>>01 - Efectivo</option>
        <option value="02" <%= claveMetodoPagoSAT.equals("02")?"selected":"" %>>02 - Cheque</option>
        <option value="03" <%= claveMetodoPagoSAT.equals("03")?"selected":"" %>>03 - Transferencia</option>
        <option value="04" <%= claveMetodoPagoSAT.equals("04")?"selected":"" %>>04 - Tarjetas de cr&eacute;dito</option>
        <option value="05" <%= claveMetodoPagoSAT.equals("05")?"selected":"" %>>05 - Monederos electr&oacute;nicos</option>
        <option value="06" <%= claveMetodoPagoSAT.equals("06")?"selected":"" %>>06 - Dinero electr&oacute;nico</option>
        <option value="07" <%= claveMetodoPagoSAT.equals("07")?"selected":"" %>>07 - Tarjetas digitales</option>
        <option value="08" <%= claveMetodoPagoSAT.equals("08")?"selected":"" %>>08 - Vales de despensa</option>
        <option value="09" <%= claveMetodoPagoSAT.equals("09")?"selected":"" %>>09 - Bienes</option>
        <option value="10" <%= claveMetodoPagoSAT.equals("10")?"selected":"" %>>10 - Servicio</option>
        <option value="11" <%= claveMetodoPagoSAT.equals("11")?"selected":"" %>>11 - Por cuenta de tercero</option>
        <option value="12" <%= claveMetodoPagoSAT.equals("12")?"selected":"" %>>12 - Daci&oacute;n en pago</option>
        <option value="13" <%= claveMetodoPagoSAT.equals("13")?"selected":"" %>>13 - Pago por subrogaci&oacute;n</option>
        <option value="14" <%= claveMetodoPagoSAT.equals("14")?"selected":"" %>>14 - Pago por consignaci&oacute;n</option>
        <option value="15" <%= claveMetodoPagoSAT.equals("15")?"selected":"" %>>15 - Condonaci&oacute;n</option>
        <option value="16" <%= claveMetodoPagoSAT.equals("16")?"selected":"" %>>16 - Cancelaci&oacute;n</option>
        <option value="17" <%= claveMetodoPagoSAT.equals("17")?"selected":"" %>>17 - Compensaci&oacute;n</option>
        <option value="98" <%= claveMetodoPagoSAT.equals("98")?"selected":"" %>>98 - NA</option>
        <option value="99" <%= claveMetodoPagoSAT.equals("99")?"selected":"" %>>99 - Otros</option>
    </select>