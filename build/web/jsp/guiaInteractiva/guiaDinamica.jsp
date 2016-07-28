<%-- 
    Document   : guiaDinamica
    Created on : 29/06/2015, 05:07:27 PM
    Author     : leonardo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../include/jsFunctions.jsp"/>
        
        <title>JSP Page</title>
        
        
        <style type="text/css">
            
            .content-box-blue {
            background-color: #d8ecf7;
            border: 1px solid #afcde3;
            }
            
            #globo{
                width: 340px;
                height: 130px;
                right: -70px;
                padding: 15px;
                border-radius: 5px;
                //box-shadow: 0 2px 5px #555;
                //box-shadow: 0 2px 7px #69A16C;
                background-color: #FFFFFF;
                position: relative;
                top: -188px;
                opacity: 0.8;
                /*content: "";
                width: 0;
                position: absolute;
                border-style: solid;
                border-width: 0 35px 50px 35px;
                border-color: #000 transparent #000 transparent;*/
                
            }
            
            #globo:before{
                content: "";
                width: 0;
                position: absolute;
                border-style: solid;
                border-width: 10px 5px 0 5px;
                border-color: #69A16C transparent;
                bottom: -10px;
                left: 50px;
            }

            .fadebox {
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 100%;
                height: 100%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.8;
                opacity:.80;
                filter: alpha(opacity=80);
            }
            .overbox {
                display: none;
                position: absolute;
                top: 25%;
                left: 25%;
                width: 50%;
                height: 50%;
                z-index:1002;
                overflow: auto;
            }
            
        </style>
                
        <script type="text/javascript">
            
            function showLightbox() {
                document.getElementById('over').style.display='block';
                document.getElementById('fade').style.display='block';
            }
            function hideLightbox() {
                document.getElementById('over').style.display='none';
                document.getElementById('fade').style.display='none';
            }
            
            function aplicarEstilos(elemento, listaEstilos){
                //recorrer el objeto de listaEstilos
                for(var estilo in listaEstilos){
                        elemento.style[estilo]= listaEstilos[estilo];
                }
            }
            
            //definicion de identificadores de modulos y el proceso:
            var clienteNuevo = "clienteNuevo";
            var clienteForm = "clienteForm";
            var conceptoNuevo = "conceptoNuevo"
            var conceptoForm = "conceptoForm";
            var empleadoNuevo = "empleadoNuevo"
            var empleadoForm = "empleadoForm";
            var empleadoInventarioNuevo = "empleadoInventarioNuevo";
            var empleadoInventarioAgregar = "empleadoInventarioAgregar";
            var empleadoGeolocalizarNew = "empleadoGeolocalizarNew";
            var empleadoCrearRutaNueva = "empleadoCrearRutaNueva";
            
            var textoInicial = "Bienvenido a Equipo de Ventas en Campo,</br> Yo soy Juan y seré tu guía para el uso del sistema.</br></br>Podrás visualizar un teme especifico y si requieres apoyo recuerda que siempre puedes recurrir a nosotos. </br></br>Márcanos al (55) 84889034 o mándanos un correo a ventas@movilpyme.com para agendar una demo personalizada sin ningún compromiso.";
            var textoInicial2 = "Da clic en un tema específico o en siguiente para comenzar.";
            
            var clienteInicial = "<img src='../../images/guiaDinamica/crearCliente.png' title='Crear un cliente' style='height: 20px;'/></br></br>"
                            + "Hemos cargado en automático algunos clientes en la consola, para que te sea más fácil iniciar, sin embargo tu puedes crear y modificar lo que gustes a placer. Vamos a crear un nuevo cliente.</br></br>"
                            + "<div style='width: 350px;'>"
                            + "    <li> <center> <a title='No Mostrar' onclick='noVolverMostrarGuia();'><img src='../../images/guiaDinamica/NoVolverMostrar.png' title='No Volver a Mostrar la Guía' style='height: 44px;'/></a> &nbsp;&nbsp;"
                                                        + "    <a title='Siguiente' onclick='siguiente(\""+clienteNuevo+"\");' style='color: green;'><img src='../../images/guiaDinamica/siguiente.png' title='Siguiente' style='height: 44px;'/></a> &nbsp;&nbsp;"
                                                        + "    <a title='Cerrar' onclick='cerrarGuia();' style='color: red;'><img src='../../images/guiaDinamica/cerrar.png' title='Cerrar' style='height: 44px;'/></a></center></li>"                        
                            + "</div>";
            var conceptoInicial = "<img src='../../images/guiaDinamica/crearProducto.png' title='Crear un producto' style='height: 20px;'/></br></br>"
                            + "Hemos cargado en automático algunos productos en la consola, para que te sea más fácil iniciar, sin embargo tu puedes crear y modificar lo que gustes a placer. Vamos a crear un nuevo producto.</br></br>"
                            + "<div style='width: 350px;'>"
                            + "    <li> <center> <a title='No Mostrar' onclick='noVolverMostrarGuia();'><img src='../../images/guiaDinamica/NoVolverMostrar.png' title='No Volver a Mostrar la Guía' style='height: 44px;'/></a> &nbsp;&nbsp;"
                                                        + "    <a title='Siguiente' onclick='siguiente(\""+conceptoNuevo+"\");' style='color: green;'><img src='../../images/guiaDinamica/siguiente.png' title='Siguiente' style='height: 44px;'/></a> &nbsp;&nbsp;"
                                                        + "    <a title='Cerrar' onclick='cerrarGuia();' style='color: red;'><img src='../../images/guiaDinamica/cerrar.png' title='Cerrar' style='height: 44px;'/></a></center></li>"                        
                            + "</div>";
                    
            var empleadoInicial = "<img src='../../images/guiaDinamica/crearVendedorMovil.png' title='Crear un vendedor móvil' style='height: 20px;'/></br></br>"
                            + "Vamos a iniciar creando un empleado y le vamos a dar el rol de Vendedor.</br></br>"
                            + "<div style='width: 350px;'>"
                            + "    <li> <center> <a title='No Mostrar' onclick='noVolverMostrarGuia();'><img src='../../images/guiaDinamica/NoVolverMostrar.png' title='No Volver a Mostrar la Guía' style='height: 44px;'/></a> &nbsp;&nbsp;"
                                                        + "    <a title='Siguiente' onclick='siguiente(\""+empleadoNuevo+"\");' style='color: green;'><img src='../../images/guiaDinamica/siguiente.png' title='Siguiente' style='height: 44px;'/></a> &nbsp;&nbsp;"
                                                        + "    <a title='Cerrar' onclick='cerrarGuia();' style='color: red;'><img src='../../images/guiaDinamica/cerrar.png' title='Cerrar' style='height: 44px;'/></a></center></li>"                        
                            + "</div>";
                    
            var empleadoInvetarioInicial = "<img src='../../images/guiaDinamica/AsignarProducto.png' title='Asignar producto a un vendedor' style='height: 20px'/></br></br>"
                            + "Asignaremos inventario a un empleado. </br></br>"
                            + "<div style='width: 350px;'>"
                            + "    <li> <center> <a title='No Mostrar' onclick='noVolverMostrarGuia();'><img src='../../images/guiaDinamica/NoVolverMostrar.png' title='No Volver a Mostrar la Guía' style='height: 44px;'/></a> &nbsp;&nbsp;"
                                                        + "    <a title='Siguiente' onclick='siguiente(\""+empleadoInventarioNuevo+"\");' style='color: green;'><img src='../../images/guiaDinamica/siguiente.png' title='Siguiente' style='height: 44px;'/></a> &nbsp;&nbsp;"
                                                        + "    <a title='Cerrar' onclick='cerrarGuia();' style='color: red;'><img src='../../images/guiaDinamica/cerrar.png' title='Cerrar' style='height: 44px;'/></a></center></li>"                        
                            + "</div>";
                    
            var empleadoInvetarioAgregarNewConcep = "</br></br>"
                            + "<div style='width: 350px;'>"
                            + "    <li> <center> <a title='No Mostrar' onclick='noVolverMostrarGuia();'><img src='../../images/guiaDinamica/NoVolverMostrar.png' title='No Volver a Mostrar la Guía' style='height: 44px;'/></a> &nbsp;&nbsp;"
                                                        + "    <a title='Siguiente' onclick='siguiente(\""+empleadoInventarioAgregar+"\");' style='color: green;'><img src='../../images/guiaDinamica/siguiente.png' title='Siguiente' style='height: 44px;'/></a> &nbsp;&nbsp;"
                                                        + "    <a title='Cerrar' onclick='cerrarGuia();' style='color: red;'><img src='../../images/guiaDinamica/cerrar.png' title='Cerrar' style='height: 44px;'/></a></center></li>"                        
                            + "</div>";
                    
            var empleadoGeolocalizar = "<img src='../../images/guiaDinamica/geolocalizarEmpleado.png' title='Geo localizar a un empleado en el mapa' style='height: 20px;'/></br></br>"+"Geolocalizaremos a un empleado dentro de un mapa.</br></br>"
                            + "<div style='width: 350px;'>"
                            + "    <li> <center> <a title='No Mostrar' onclick='noVolverMostrarGuia();'><img src='../../images/guiaDinamica/NoVolverMostrar.png' title='No Volver a Mostrar la Guía' style='height: 44px;'/></a> &nbsp;&nbsp;"
                                                        + "    <a title='Siguiente' onclick='siguiente(\""+empleadoGeolocalizarNew+"\");' style='color: green;'><img src='../../images/guiaDinamica/siguiente.png' title='Siguiente' style='height: 44px;'/></a> &nbsp;&nbsp;"
                                                        + "    <a title='Cerrar' onclick='cerrarGuia();' style='color: red;'><img src='../../images/guiaDinamica/cerrar.png' title='Cerrar' style='height: 44px;'/></a></center></li>"                        
                            + "</div>";
                    
            var empleadoCrearRuta = "<img src='../../images/guiaDinamica/crearRutaLibre.png' title='Crear una ruta libre y asignarla a un empleado' style='height: 20px;'/></br></br>"
                            + "Con aquipo de ventas en campo puedes crear rutas de trabajo para tus empleados.</br></br>"
                            + "<div style='width: 350px;'>"
                            + "    <li> <center> <a title='No Mostrar' onclick='noVolverMostrarGuia();'><img src='../../images/guiaDinamica/NoVolverMostrar.png' title='No Volver a Mostrar la Guía' style='height: 44px;'/></a> &nbsp;&nbsp;"
                                                        + "    <a title='Siguiente' onclick='siguiente(\""+empleadoCrearRutaNueva+"\");' style='color: green;'><img src='../../images/guiaDinamica/siguiente.png' title='Siguiente' style='height: 44px;'/></a> &nbsp;&nbsp;"
                                                        + "    <a title='Cerrar' onclick='cerrarGuia();' style='color: red;'><img src='../../images/guiaDinamica/cerrar.png' title='Cerrar' style='height: 44px;'/></a></center></li>"                        
                            + "</div>";
                    
            var solicitarDemo = "<img src='../../images/guiaDinamica/SolicitarDemo.png' title='Solicitar una demo personalizada' style='width: 207px;'/></br></br>"                    
                            +"En movilpyme nuestra prioridad son nuestros clientes. \n Si necesitas una demo personalizada no dudes en contactarnos: \n Contáctanos: (55) 84 88 90 34 \n ventas@movilpyme.com";
                    
            var footerBotones = "</br></br>"
                            + "<div style='width: 350px;'>"
                            + "    <li> <center> <a title='No Mostrar' onclick='noVolverMostrarGuia();'><img src='../../images/guiaDinamica/NoVolverMostrar.png' title='No Volver a Mostrar la Guía' style='height: 44px;'/></a> &nbsp;&nbsp;"
                                                        + "    <a title='Siguiente' onclick='mostrarInicio();' style='color: green;'><img src='../../images/guiaDinamica/siguiente.png' title='Siguiente' style='height: 44px;'/></a> &nbsp;&nbsp;"
                                                        + "    <a title='Cerrar' onclick='cerrarGuia();' style='color: red;'><img src='../../images/guiaDinamica/cerrar.png' title='Cerrar' style='height: 44px;'/></a></center></li>"                        
                            + "</div>";
                 
            var solicitudClicModuloMensaje = "";
            var solicitudClicCatalogosMensaje = "<img src='../../images/guiaDinamica/crearCliente.png' title='Crear un cliente' style='height: 20px;'/></br></br>" + "Da Clic en el apartado de \"Catalogos\" ";
            var solicitudClicAlmacenMensaje = "<img src='../../images/guiaDinamica/crearProducto.png' title='Crear un producto' style='height: 20px;'/></br></br>" + "Da Clic en el apartado de \"Almacén\" ";
            var solicitudClicClienteMensaje = "<img src='../../images/guiaDinamica/crearCliente.png' title='Crear un cliente' style='height: 20px;'/></br></br>" + "Da Clic en el apartado de \"Clientes\" ";
            var solicitudClicNuevoMensaje = "Da Clic en el botón de \"Crear Nuevo\" ";
            var solicitudClicConceptoMensaje = "<img src='../../images/guiaDinamica/crearProducto.png' title='Crear un producto' style='height: 20px;'/></br></br>" + "Da Clic en el apartado de \"Productos\" ";
            
            var solicitudClicPretoMovilMensaje = "Da Clic en el apartado de \"Pretoriano Móvil\" ";
            var solicitudClicEmpleadoMensaje = "Da Clic en el apartado de \"Empleados\" ";
            var solicitudClicLogisticaMensaje = "<img src='../../images/guiaDinamica/crearRutaLibre.png' title='Crear una ruta libre y asignarla a un empleado' style='height: 20px;'/></br></br>" + "Da Clic en el apartado de \"Logística\" ";
            
            var solicitudClicInventarioAgregarConcepto = "<img src='../../images/guiaDinamica/AsignarProducto.png' title='Asignar producto a un vendedor' style='height: 20px'/></br></br>" + "Da Clic en la acción de \"Agregar Concepto\" ";
            
            
            var contentString = ""
                        + "<div class='map_dialog'>"
                        /*+ "    <b>Promotor: </b><%="Crear un cliente"%><br/>"
                        + "    <b>Fecha: </b><%=""%><br>"
                        + "    <b>Estado:</b><%=""%><br/>"*/
                        + "    <li> <a title='cliente' onclick='presentacionModulo(1);'><img src='../../images/guiaDinamica/crearCliente.png' title='Crear un cliente' style='height: 20px;'/></a> </li> "
                        + "    <li> <a title='Producto' onclick='presentacionModulo(2);'><img src='../../images/guiaDinamica/crearProducto.png' title='Crear un producto' style='height: 20px;'/></a> </li>"
                        + "    <li> <a title='Vendedor Movil' onclick='presentacionModulo(3);'><img src='../../images/guiaDinamica/crearVendedorMovil.png' title='Crear un vendedor móvil' style='height: 20px;'/></a> </li>"
                        + "    <li> <a title='Asignar Producto' onclick='presentacionModulo(4);'><img src='../../images/guiaDinamica/AsignarProducto.png' title='Asignar producto a un vendedor' style='height: 20px'/></a> </li>"
                        + "    <li> <a title='Geolocalizar' onclick='presentacionModulo(5);'><img src='../../images/guiaDinamica/geolocalizarEmpleado.png' title='Geo localizar a un empleado en el mapa' style='height: 20px;'/></a> </li>"
                        + "    <li> <a title='Crear Rura' onclick='presentacionModulo(6);'><img src='../../images/guiaDinamica/crearRutaLibre.png' title='Crear una ruta libre y asignarla a un empleado' style='height: 20px;'/></a> </li>"
                        + "    <li> <a title='Solicitar Demo' onclick='presentacionModulo(7);'><img src='../../images/guiaDinamica/SolicitarDemo.png' title='Solicitar una demo personalizada' style='width: 207px;'/></a> </li>  <br/><br/><br/><br/>"                        
                        + "    <li style='width: 450px;'> <center> <a title='No Mostrar' onclick='noVolverMostrarGuia();'><img src='../../images/guiaDinamica/NoVolverMostrar.png' title='No Volver a Mostrar la Guía' style='width: 159px;'/></a> &nbsp;&nbsp;"
                                                        //+ "    <a title='Siguiente' onclick='muestraDetallesPromotor(<%="9"%>,9);' style='color: green;'><img src='../../images/guiaDinamica/siguiente.png' title='Siguient' style='width: 159px;'/></a> &nbsp;&nbsp;&nbsp;&nbsp;"
                                                        + "    <a title='Cerrar' onclick='cerrarGuia();' style='color: red;'><img src='../../images/guiaDinamica/cerrar2.png' title='Cerrar' style='width: 159px;'/></a></center></li>"
                        + "</div>";
                
            
            function inicializar(){
                <%String mostrarBienvenida = (String)session.getAttribute("mostrarBienvenida");
                if(mostrarBienvenida == null){%>
                    redisenarTamanoGlobo("muyGrande");
                    bienvenida();   
                <%}else{%>
                    bienvenida2();
                <%}%>
            }
            
            function bienvenida(){
                //hideLightbox();
///               showLightbox();
                //$('#dfbn').css('width', '550px');
                $("#dfbn").attr("src","../../images/guiaDinamica/fondo_s1.png");
                document.getElementById('globo').innerHTML = textoInicial;                
                modoTipoAjax(5,"");//enviamos a sesion de que ya mostramos el mensaje de bienvenida y se debera omitir ya en la sesion
                setTimeout('bienvenida2()',3000);                
            }
            
            function bienvenida2(){
///                //showLightbox();
                //$('#dfbn').css('width', '550px');
                $("#dfbn").attr("src","../../images/guiaDinamica/fondo_s1.png");
                redisenarTamanoGlobo("extraGrande")
                document.getElementById('globo').innerHTML = textoInicial2+contentString;                
            }
            
            function presentacionModulo(modulo){
                redisenarTamanoGlobo("grande");
                if(modulo == 1){ //para clientes
                    document.getElementById('globo').innerHTML = clienteInicial;
                }else if(modulo == 2){//para conceptos
                    document.getElementById('globo').innerHTML = conceptoInicial;
                }else if(modulo == 3){//para Empleados                    
                    document.getElementById('globo').innerHTML = empleadoInicial;
                }else if(modulo == 4){//para Asignacion de Inventarios a Vendedor                    
                    document.getElementById('globo').innerHTML = empleadoInvetarioInicial;
                }else if(modulo == 5){//para Geolocalizar a un empleado                   
                    document.getElementById('globo').innerHTML = empleadoGeolocalizar;
                }else if(modulo == 6){//para crear una ruta y asignarla               
                    document.getElementById('globo').innerHTML = empleadoCrearRuta;
                }else if(modulo == 7){//para crear una ruta y asignarla               
                    document.getElementById('globo').innerHTML = solicitarDemo + footerBotones;
                }
                //setTimeout('solicitudClic()',3000);
                
            }
            
            var idLeftContent = "";//variable para saber donde parpadeara la alerta
            var valorIdModulo = "";
            function siguiente(valorDeSiguiente){
                valorIdModulo = valorDeSiguiente;                
                if(valorIdModulo == clienteNuevo){
                    redisenarTamanoGlobo("chico");
                    solicitudClicModuloMensaje = solicitudClicCatalogosMensaje;
                    idLeftContent = "idCatalogosLeftContent";
                    solicitudClic();                    
                }else if(valorIdModulo == conceptoNuevo){
                    redisenarTamanoGlobo("chico");                    
                    solicitudClicModuloMensaje = solicitudClicAlmacenMensaje;
                    idLeftContent = "idAlmacenLeftContent";
                    solicitudClic();
                }else if(valorIdModulo == empleadoNuevo){
                    redisenarTamanoGlobo("chico");                    
                    solicitudClicModuloMensaje = "<img src='../../images/guiaDinamica/crearVendedorMovil.png' title='Crear un vendedor móvil' style='height: 20px;'/></br></br>" + solicitudClicPretoMovilMensaje;
                    idLeftContent = "idPretorianoMovilLeftContent";
                    solicitudClic();
                }else if(valorIdModulo == empleadoInventarioNuevo){
                    redisenarTamanoGlobo("chico");                    
                    solicitudClicModuloMensaje = "<img src='../../images/guiaDinamica/AsignarProducto.png' title='Asignar producto a un vendedor' style='height: 20px'/></br></br>"+solicitudClicPretoMovilMensaje;
                    idLeftContent = "idPretorianoMovilLeftContent";
                    solicitudClic();
                }else if(valorIdModulo == empleadoInventarioAgregar){
                    redisenarTamanoGlobo("semiMedianoGrande");                    
                    solicitudClicModuloMensaje = solicitudClicInventarioAgregarConcepto + footerBotones;
                    idLeftContent = "nuevo";
                    solicitudClic();
                }else if(valorIdModulo == empleadoGeolocalizarNew){
                    redisenarTamanoGlobo("chico");                    
                    solicitudClicModuloMensaje = "<img src='../../images/guiaDinamica/geolocalizarEmpleado.png' title='Geo localizar a un empleado en el mapa' style='height: 20px;'/></br></br>"+solicitudClicPretoMovilMensaje;
                    idLeftContent = "idPretorianoMovilLeftContent";
                    solicitudClic();
                }else if(valorIdModulo == empleadoCrearRutaNueva){
                    redisenarTamanoGlobo("chico");                    
                    solicitudClicModuloMensaje = "<img src='../../images/guiaDinamica/crearRutaLibre.png' title='Crear una ruta libre y asignarla a un empleado' style='height: 20px;'/></br></br>" + solicitudClicPretoMovilMensaje;
                    idLeftContent = "idPretorianoMovilLeftContent";
                    solicitudClic();
                }
                
                
                    
                    
            }            
            
            function solicitudClic(){                
                //if(valorIdModulo == clienteNuevo){
                    document.getElementById('globo').innerHTML = solicitudClicModuloMensaje;
                //}
                bordeColorido();
                //window.document.getElementById("idInicioLeftContent").style.borderWidht = "97%";                
                //setInterval('solicitdClicCliente()',4000);
            }
                        
            function bordeColorido(){
                //window.document.getElementById(idLeftContent).style.border = "3px solid red";                
                $("#"+idLeftContent).css('border', '3px solid red');                
                $("#"+idLeftContent).css('z-index','99999');//para mostrar arriba de la pantalla de fondo negro y habilitar su funcionamiento
            }
            
            /**********************************var contadorParpadeante = 0;            
            function bordeParpadeante(){
                if(contadorParpadeante == 0){
                    window.document.getElementById(idLeftContent).style.border = "3px solid blue";
                    contadorParpadeante = 1;
                }else if(contadorParpadeante == 1){
                    window.document.getElementById(idLeftContent).style.border = "3px solid red";
                    contadorParpadeante =0;
                }
                
            }*/
            
            function mensajeMostarFunction(mensaje, tamanoGlobo, concatenarMensajeX, mode, modulo){            
                if(concatenarMensajeX == ""){
                    //document.getElementById('globo').innerHTML = mensaje + footerBotones;
                    var mesajeArmado1 = mensaje;
                    redisenarTamanoGlobo(tamanoGlobo);
                    if(mode != 6 && mode == 1){                        
                        idLeftContent = "nuevo";
                        bordeColorido();
                    }else if(mode == 2 || mode == 7){
                        //mandamos a llamar la function para colocar el form arriba del fondo negro:                        
                        arribaDeFondoNegro("leito");                        
                        mesajeArmado1 += footerBotones;
                    }else{                        
                        //mesajeArmado1 += footerBotones;
                    }
                    document.getElementById('globo').innerHTML = mesajeArmado1;
                    if(modulo == "empleadoInventarioNuevo"){//para validar si mostramos arriba del fondo negro las acciones de Inventario del empleado                        
                        arribaDeFondoNegro("consultaInventarioEmpleado");
                    }else if(modulo == "empleadoGeolocalizarNew"){//para validar si mostramos arriba del fondo negro las acciones de Inventario del empleado                        
                        arribaDeFondoNegro("consultaGeoLocalizacionEmpleado");
                    }
                }else{
                    if(concatenarMensajeX == "mensaje1"){
                        document.getElementById('globo').innerHTML = mensaje + empleadoInvetarioAgregarNewConcep;
                        arribaDeFondoNegro("leito");
                        //................
                    }
                }
                
                
            }
            
            function redisenarTamanoGlobo(tamanoGlobo){                
                //obtenemos el elemento
                var elemento = document.getElementById('globo');
                //creamos la lista de estilos
                var estilos;
                if(tamanoGlobo == "chico"){                    
                    estilos = {
                            'height':'60px'/*,
                            'top':'-188px'*/,
                            'width':'340px',
                            'right':'-70px'
                    };
                }else if(tamanoGlobo == "mediano"){                    
                    estilos = {
                            'height':'50px'/*,
                            'top':'-188px'*/,
                            'width':'280px'
                    };
                }else if(tamanoGlobo == "semiMedianoGrande"){                    
                    estilos = {
                            'height':'120px'/*,
                            'top':'-188px'*/,
                            'width':'340px',
                            'right':'-70px'
                    };
                }else if(tamanoGlobo == "medianoGrande"){
                    estilos = {
                            'height':'158px'/*,
                            'top':'-188px'*/,
                            'width':'350px',
                            'right':'-70px'
                    };
                }else if(tamanoGlobo == "grande"){                    
                    estilos = {
                            'height':'175px'/*,
                            'top':'-188px'*/,
                            'width':'340px',
                            'right':'-70px'
                    };
                }else if(tamanoGlobo == "muyGrande"){                    
                    estilos = {
                            'height':'175px'/*,
                            'top':'-188px'*/,
                            'width':'280px'
                    };
                }else if(tamanoGlobo == "extraGrande"){                    
                    estilos = {
                            'height':'215px'/*,
                            'top':'-188px'*/,
                            'width':'280px'
                    };
                }else if(tamanoGlobo == "gigante"){
                    estilos = {
                            'height':'245px',
                            'width':'280px'
                            /*'top':'-188px'*/
                    };
                }else{//tamano por default
                    estilos = {
                            'height':'50px'/*,
                            'top':'-188px'*/,
                            'width':'280px'
                    };
                }
                aplicarEstilos(elemento, estilos);
            }            
            
            function cerrarGuia(){
                $("#juanGuiaDinamica").css('display','none');                                
                modoTipoAjax(3,"");
            }
            
            function noVolverMostrarGuia(){
                $("#juanGuiaDinamica").css('display','none');
                modoTipoAjax(4,"");
            }
        </script>
        
        <script type='text/javascript'>
        $(document).ready(function(){
          $("#nuevo").click(function () {//dio clic en nuevo
            modoTipoAjax(2,"");
            //document.getElementById('globo').innerHTML = solicitudClicClienteNuevoMensaje;
          });
          $("#idCatalogosLeftContent").click(function () {//dio clic en catalogos
              if(valorIdModulo == clienteNuevo){//control para ver donde solicitar el clic de catalogos
                  document.getElementById('globo').innerHTML = solicitudClicClienteMensaje;//solicita dar clic en conceptos
                  idLeftContent = "idCatalogosLeftContentCliente";//le decimos donde poner la marca de dar clic (borde Colorido)
                  bordeColorido();
              }
          });
          $("#idCatalogosLeftContentCliente").click(function () {//dio clic en catalogo cliente
            if(valorIdModulo == clienteNuevo){//control para ver donde solicitar el clic de concepto
                modoTipoAjax(1,valorIdModulo);
                //document.getElementById('globo').innerHTML = solicitudClicClienteNuevoMensaje;
            }
          });
          $("#idAlmacenLeftContent").click(function () {//dio clic en almacen              
              if(valorIdModulo == conceptoNuevo){//control para ver donde solicitar el clic de concepto                  
                  document.getElementById('globo').innerHTML = solicitudClicConceptoMensaje;//solicita dar clic en clientes
                  idLeftContent = "idAlmacenLeftContentProductos";//le decimos donde poner la marca de dar clic (borde Colorido)
                  bordeColorido();
              }
          });
          $("#idAlmacenLeftContentProductos").click(function () {//dio clic en catalogo cliente
              if(valorIdModulo == conceptoNuevo){//control para ver donde solicitar el clic de concepto
                modoTipoAjax(1, valorIdModulo);
                //document.getElementById('globo').innerHTML = "daludines";
            }
          });
          $("#idPretorianoMovilLeftContent").click(function () {//dio clic en Pretoriano movil              
              if(valorIdModulo == empleadoNuevo || valorIdModulo == empleadoInventarioNuevo || valorIdModulo == empleadoGeolocalizarNew){//control para ver donde solicitar el clic preto movil                 
                  document.getElementById('globo').innerHTML = solicitudClicEmpleadoMensaje;//solicita dar clic en empleados
                  idLeftContent = "idPretorianoMovilLeftContentEmpleados";//le decimos donde poner la marca de dar clic (borde Colorido)
                  bordeColorido();
              }else if(valorIdModulo == empleadoCrearRutaNueva){
                  document.getElementById('globo').innerHTML = solicitudClicLogisticaMensaje;//solicita dar clic en empleados
                  idLeftContent = "idPretorianoMovilLeftContentLogistica";//le decimos donde poner la marca de dar clic (borde Colorido)
                  bordeColorido();
              }
          });
          $("#idPretorianoMovilLeftContentEmpleados").click(function () {//dio clic en Pretoriano movil -> Empleados
              if(valorIdModulo == empleadoNuevo){//control para ver donde solicitar el clic de empleado
                modoTipoAjax(1, valorIdModulo);
                //document.getElementById('globo').innerHTML = "daludines";
              }else if(valorIdModulo == empleadoInventarioNuevo){//control para ver donde solicitar el clic en inventario del empleado
                modoTipoAjax(6, valorIdModulo);
                //document.getElementById('globo').innerHTML = "daludines";
              }else if(valorIdModulo == empleadoGeolocalizarNew){//control para ver donde solicitar el clic en Localizacion del empleado
                modoTipoAjax(6, valorIdModulo);
                //document.getElementById('globo').innerHTML = "daludines";
              }
              
          });
          $("#consultaInventarioEmpleado").click(function () {//dio clic en la accion de Inventario del empleado              
            modoTipoAjax(7,"consultaInventarioEmpleado");
            //document.getElementById('globo').innerHTML = solicitudClicClienteNuevoMensaje;
          });
          
          $("#consultaGeoLocalizacionEmpleado").click(function () {//dio clic en la accion de Inventario del empleado              
            modoTipoAjax(7,"consultaGeoLocalizacionEmpleado");            
          });
          
           $("#idPretorianoMovilLeftContentLogistica").click(function () {//dio clic en catalogo cliente
            if(valorIdModulo == empleadoCrearRutaNueva){//control para ver donde solicitar el clic de concepto
                modoTipoAjax(1,valorIdModulo);                
            }
          });
          
          
          $("#idValidaXMLLeftContent").click(function () {//dio clic en catalogo cliente
              modoTipoAjax(0, "");////////////reiniciar variables en sesion
          });
          
          
        });
        
       function modoTipoAjax(mode, modulo){
           $.ajax({
                type: "POST",
                url: "../guiaInteractiva/guiaDinamica_ajax.jsp",
                data: { mode: mode , modulo : modulo},                       
                beforeSend: function(objeto){

                },
                success: function(datos){
                    if(datos.indexOf("--EXITO-->", 0)>0){
                        if(mode == 3 || mode == 4){//validamos que sea el modo de cerrar guia.
                            $("#overlay").css('display','none');//para ocultar el div que oscurece la pantalla.
                            $("#idInicioLeftContent").click();
                        }
                   }else{

                   }
                }
            });
       }
       
       function mostrarInicio(){
           modoTipoAjax(0, "");////////////reiniciar variables en sesion
           bienvenida2();
       }
       
       function arribaDeFondoNegro(idPonerArribaDelFondo){
           $("#"+idPonerArribaDelFondo).css('z-index','99999');//para mostrar arriba de la pantalla de fondo negro y habilitar su funcionamiento
       }
    </script>
        
    </head>
    
    
    <body> 
        
<!--///        <div id="fade" class="fadebox" ></div>
        <div id="over" class="overbox" >-->

        <div style="position:fixed; bottom:-7px; right:10px;">
            <img id="dfbn" src="../../images/guiaDinamica/fondo_s1.png" style="width: 650px;">
            <!--<div class="content-box-blue" style="  position: relative; top: -303px;"> 
                I.
                <textarea form="inputDinamicoTexto" name = "inputDinamicoTexto" id="inputDinamicoTexto" style="height: 80px; width: 250px;"> </textarea>
            </div>-->
            
            
            <!--<input id="globo" style="top: -387px;" value="Texto dentro dentro del globo"/>-->
            
        </div>
        <div style="position:fixed; bottom:-17px; right:300px;">
             <div id="globo">Hola Yo soy Juan</div>
        </div>
<!--///        </div>-->
        <!--<input type="button" onclick="pruebaInicio();" value="Prueba Inicio" />-->
        
        
        
        <script>
            <%String mensajeEnviar = (String)session.getAttribute("mesajeActual");
            
            //si trae el mensaje comillas, las respetamos, concatenandole la secuencia de escape:
            if(mensajeEnviar != null){
                mensajeEnviar = mensajeEnviar.replaceAll("\"", "\\\\\"");
            }
            int mode = -1;//variable de ayuda en sesion para ver si marcamos o no el boton de nuevo.
                    try{ mode = Integer.parseInt((String)session.getAttribute("mode").toString()); }catch(Exception e){}
            String tamanoGlobo = (String)session.getAttribute("tamanoGlobo");
            String concatenarMensajeX = (String)session.getAttribute("concatenarMensajeX");
            String modulo = (String)session.getAttribute("modulo");
            
            %>
            
            <%if(mensajeEnviar != null && !mensajeEnviar.equals("")){System.out.println("------------ NO ES NULL NI VACIO");%>
                mensajeMostarFunction('<%=mensajeEnviar%>','<%=tamanoGlobo%>','<%=concatenarMensajeX!=null?concatenarMensajeX:""%>','<%=mode%>','<%=modulo%>');
            <%}else{System.out.println("------------  VACIO");%>
                inicializar();
            <%}%>
        </script>
    </body>
    
    
    
</html>
