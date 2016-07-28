/*
* ************************* I M P O R T A N T E *******************************
* 
*       Este script esta basado en el script previo de base de datos
*       para el sistema SCT. Dicho script debe ser ejecutado antes
*       para que el actual funcione correctamente añadiendo nuevas
*       tablas y funcionalidades.
*
*******************************************************************************
*/


CREATE TABLE `SGFENS_TOPIC` (                     
                `ID_TOPIC` BIGINT(20) NOT NULL AUTO_INCREMENT,  
                `TITLE_TOPIC` VARCHAR(100) NOT NULL,            
                `DESCRIPTION_TOPIC` TEXT,                       
                `ORDER_TOPIC` VARCHAR(5) DEFAULT NULL,          
                `URL_TOPIC` VARCHAR(200) NOT NULL,              
                `IS_PUBLIC_TOPIC` TINYINT(4) DEFAULT '0',       
                `ID_PARENT_TOPIC` BIGINT(20) DEFAULT NULL,      
                `IS_ACTIVE_TOPIC` TINYINT(4) DEFAULT '1',       
                PRIMARY KEY (`ID_TOPIC`)                        
              )

CREATE TABLE `SGFENS_ACCION_BITACORA_TIPO` (                  
	    `ID_TIPO` INT(11) NOT NULL AUTO_INCREMENT,               
	    `NOMBRE_BITACORA_ACCION_TIPO` VARCHAR(30) DEFAULT NULL,  
	    PRIMARY KEY (`ID_TIPO`)                                  
);
	  
INSERT  INTO `SGFENS_ACCION_BITACORA_TIPO`(`ID_TIPO`,`NOMBRE_BITACORA_ACCION_TIPO`) VALUES (1,'LOGIN');
INSERT  INTO `SGFENS_ACCION_BITACORA_TIPO`(`ID_TIPO`,`NOMBRE_BITACORA_ACCION_TIPO`) VALUES (2,'LOGOUT');
INSERT  INTO `SGFENS_ACCION_BITACORA_TIPO`(`ID_TIPO`,`NOMBRE_BITACORA_ACCION_TIPO`) VALUES (3,'NAVEGACION');
INSERT  INTO `SGFENS_ACCION_BITACORA_TIPO`(`ID_TIPO`,`NOMBRE_BITACORA_ACCION_TIPO`) VALUES (4,'DESCARGA');
	  

CREATE TABLE `SGFENS_ACCION_BITACORA` (                                      
	   `ID_BITACORA_ACCION` BIGINT(20) NOT NULL AUTO_INCREMENT,                
	   `FECHA_HORA_BITACORA_ACCION` DATETIME DEFAULT NULL,                     
	   `ID_USER` BIGINT(20) NOT NULL,                                          
	   `COMENTARIOS_BITACORA_ACCION` TEXT,                                     
	   `ID_TIPO_BITACORA_ACCION_TIPO` INT(11) NOT NULL,                        
	   `ID_TOPIC_NAVEGACION` BIGINT(20) DEFAULT '0',                           
	   PRIMARY KEY (`ID_BITACORA_ACCION`),                                     
	   KEY `ID_USER` (`ID_USER`),                                              
	   KEY `INDEX_ID_TITPO_BITACORA_ACCION` (`ID_TIPO_BITACORA_ACCION_TIPO`),  
	   KEY `INDEX_ID_TOPIC_NAVEGACION` (`ID_TOPIC_NAVEGACION`)                 
);
 
 CREATE TABLE `SGFENS_CLIENTE_VENDEDOR`( 
	`ID_CLIENTE` INT NOT NULL COMMENT 'ID del cliente', 
	`ID_USUARIO_VENDEDOR` INT NOT NULL COMMENT 'ID del usuario con rol vendedor al que pertenece el cliente', 
	PRIMARY KEY (`ID_CLIENTE`)
);

CREATE TABLE `SGFENS_PROVEEDOR` (
	`ID_PROVEEDOR` INT(10) NOT NULL AUTO_INCREMENT,
	`ID_EMPRESA` INT(10) NOT NULL,
	`NUMERO_PROVEEDOR` VARCHAR(15) DEFAULT NULL,
	`RFC` VARCHAR(15) NOT NULL,
	`RAZON_SOCIAL` VARCHAR(500) DEFAULT NULL,
	`CALLE` VARCHAR(100) DEFAULT NULL,
	`NUMERO` VARCHAR(30) DEFAULT NULL,
	`NUMERO_INTERIOR` VARCHAR(30) DEFAULT NULL,
	`COLONIA` VARCHAR(100) DEFAULT NULL,
	`CODIGO_POSTAL` VARCHAR(10) DEFAULT NULL,
	`PAIS` VARCHAR(100) DEFAULT NULL,
	`ESTADO` VARCHAR(100) DEFAULT NULL,
	`MUNICIPIO` VARCHAR(100) DEFAULT NULL,
	`LADA` VARCHAR(8) DEFAULT NULL,
	`TELEFONO` VARCHAR(20) DEFAULT NULL,
	`EXTENSION` VARCHAR(5) DEFAULT NULL,
	`CELULAR` VARCHAR(14) DEFAULT NULL,
	`CORREO` VARCHAR(100) DEFAULT NULL,
	`CONTACTO` VARCHAR(100) DEFAULT NULL,
	`ID_ESTATUS` INT(10) DEFAULT NULL,
	`DESCRIPCION` TEXT DEFAULT NULL,
	PRIMARY KEY (`ID_PROVEEDOR`)
);

CREATE TABLE `SGFENS_PROSPECTO` (
	`ID_PROSPECTO` INT(10) NOT NULL AUTO_INCREMENT,
	`ID_EMPRESA` INT(10) NOT NULL,
	`RAZON_SOCIAL` VARCHAR(500) DEFAULT NULL,
	`LADA` VARCHAR(8) DEFAULT NULL,
	`TELEFONO` VARCHAR(20) DEFAULT NULL,
	`CELULAR` VARCHAR(14) DEFAULT NULL,
	`CORREO` VARCHAR(100) DEFAULT NULL,
	`CONTACTO` VARCHAR(100) DEFAULT NULL,
	`ID_ESTATUS` INT(10) DEFAULT NULL,
	`DESCRIPCION` TEXT DEFAULT NULL,
	PRIMARY KEY (`ID_PROSPECTO`)
);
					 
ALTER TABLE SERVICIO ADD COLUMN SKU VARCHAR(50) NULL AFTER PRECIO;



CREATE TABLE ALMACEN(
	ID_ALMACEN INT(10) NOT NULL AUTO_INCREMENT,
	ID_EMPRESA INT(10) NOT NULL,
	ID_ESTATUS INT(10),
	NOMBRE VARCHAR(110),
	DIRECCION VARCHAR(400),
	AREA_ALMACEN DOUBLE DEFAULT '0',
	RESPONSABLE VARCHAR(150),
	PUESTO VARCHAR(50),
	TELEFONO VARCHAR(25),
	CORREO VARCHAR(100),
	PRIMARY KEY(ID_ALMACEN)
);


CREATE TABLE `CONCEPTO_PROVEEDOR`( 
	`ID_CONCEPTO` INT NOT NULL , 
	`ID_PROVEEDORES` INT NOT NULL
);

ALTER TABLE `CONCEPTO` 
	ADD COLUMN `IMAGEN_CARPETA_ARCHIVO` VARCHAR(500) NULL COMMENT 'Ruta a la carpeta contenedora de la imagen' AFTER `GENERICO`, 
	ADD COLUMN `IMAGEN_NOMBRE_ARCHIVO` VARCHAR(500) NULL COMMENT 'Nombre del archivo de imagen, incluyendo extension' AFTER `IMAGEN_CARPETA_ARCHIVO`, 
	ADD COLUMN `DESCRIPCION_CORTA` VARCHAR(50) DEFAULT 'Sin descripcion' NULL COMMENT 'descripción corta del producto, esta descripción es la que se desplegará en el móvil' AFTER `IMAGEN_NOMBRE_ARCHIVO`, 
	ADD COLUMN `ID_ALMACEN` INT NULL COMMENT 'Almacen en el que se ubicara físicamente el producto.' AFTER `DESCRIPCION_CORTA`, 
	ADD COLUMN `STOCK_MINIMO` DOUBLE DEFAULT '1' NULL COMMENT 'Es la cantidad de producto mínimo que se permite tener del producto en almacén, esto con la idea de no caer en un desabasto.' AFTER `ID_ALMACEN`, 
	ADD COLUMN `STOCK_AVISO_MIN` TINYINT(1) DEFAULT '0' NULL COMMENT 'Indica si se notificara por medio de un correo al cliente del desabasto de producto basado en STOCK_MINIMO' AFTER `STOCK_MINIMO`, 
	ADD COLUMN `DETALLE` TEXT NULL COMMENT 'En esta sección se contempla un detalle más a fondo de las características y cualidades de dicho producto' AFTER `STOCK_AVISO_MIN`, 
	ADD COLUMN `FECHA_ALTA` DATETIME NULL COMMENT 'Esta es la fecha en la que se desea quede registrado en sistema la entrada del producto, por default si no se llena, se tomará la fecha del día' AFTER `DETALLE`, 
	ADD COLUMN `VOLUMEN` DOUBLE DEFAULT '0' NULL COMMENT 'espacio ocupado por el producto' AFTER `FECHA_ALTA`, 
	ADD COLUMN `PESO` DOUBLE DEFAULT '0' NULL COMMENT 'Peso del producto' AFTER `VOLUMEN`, 
	ADD COLUMN `OBSERVACIONES` TEXT NULL COMMENT 'Este espacio esta destinado para hacer alguna anotación específica por ejemplo: No apilar más de 3 cajas, refrigerar a 5°, etc.' AFTER `PESO`;
	
	
	
/*TABLA COTIZACION*/
CREATE TABLE `SGFENS_COTIZACION` (                                                                                                                                  
        `ID_COTIZACION` INT NOT NULL AUTO_INCREMENT UNIQUE,                                                                                                                  
        `ID_USUARIO_VENDEDOR` INT(11) NOT NULL COMMENT 'ID del usuario con rol vendedor que genera la cotización',                                                       
        `ID_EMPRESA` INT(10) NOT NULL,                                                                                                                                    
        `ID_CLIENTE` INT(11) DEFAULT '0' COMMENT 'ID unico del Cliente que solicito la cotización, puede estar nulo y ser relacionado con un Prospecto en su lugar.',    
        `ID_PROSPECTO` INT(11) DEFAULT '0' COMMENT 'ID unico del Prospecto que solicito la cotización, puede estar nulo y ser relacionado con un Cliente en su lugar.',  
        `CONSECUTIVO_COTIZACION` INT(11) NOT NULL COMMENT 'Consecutivo de cotización para la empresa',                                                                   
        `FOLIO_COTIZACION` VARCHAR(30) DEFAULT NULL COMMENT 'Dicho folio estará compuesto por el prefijo C , seguido del  consecutivo comenzando por el 0001',           
        `FECHA_COTIZACION` DATETIME NOT NULL,                                                                                                                             
        `TIPO_MONEDA` VARCHAR(10) NOT NULL,                                                                                                                               
        `TIEMPO_ENTREGA_DIAS` INT(11) NOT NULL DEFAULT '0',                                                                                                               
        `COMENTARIOS` TEXT,                                                                                                                                               
        `DESCUENTO_TASA` DOUBLE NOT NULL DEFAULT '0' COMMENT 'Porcentaje de descuento',                                                                                   
        `DESCUENTO_MONTO` DOUBLE NOT NULL DEFAULT '0' COMMENT 'Monto calculado del descuento',                                                                            
        `SUBTOTAL` DOUBLE NOT NULL,                                                                                                                                       
        `TOTAL` DOUBLE NOT NULL,
        `DESCUENTO_MOTIVO` VARCHAR(200) DEFAULT NULL COMMENT 'Motivo del descuento aplicado, solo en caso de existir',
        PRIMARY KEY (`ID_COTIZACION`)                                                                                                                   
);

/*TABLA PARA CONJUNTO DE PRODUCTOS DE COTIZACIÓN*/ 
CREATE TABLE `SGFENS_COTIZACION_PRODUCTO` (            
	  `ID_COTIZACION` INT(11) NOT NULL,                    
	  `ID_CONCEPTO` INT(11) NOT NULL,                      
	  `DESCRIPCION` TEXT NOT NULL,                         
	  `UNIDAD` VARCHAR(100) NOT NULL DEFAULT 'No Aplica',  
	  `IDENTIFICACION` VARCHAR(50) DEFAULT NULL,           
	  `CANTIDAD` DOUBLE NOT NULL,                          
	  `PRECIO_UNITARIO` DOUBLE NOT NULL,                   
	  `DESCUENTO_PORCENTAJE` DOUBLE DEFAULT '0',           
	  `DESCUENTO_MONTO` DOUBLE DEFAULT '0',                
	  `SUBTOTAL` DOUBLE NOT NULL,                          
	  PRIMARY KEY (`ID_COTIZACION`,`ID_CONCEPTO`)          
);

/*TABLA PARA CONJUNTO DE SERVICIOS DE COTIZACIÓN*/ 
CREATE TABLE `SGFENS_COTIZACION_SERVICIO` (            
	  `ID_COTIZACION` INT(11) NOT NULL,                    
	  `ID_SERVICIO` INT(11) NOT NULL,                      
	  `DESCRIPCION` TEXT NOT NULL,                         
	  `UNIDAD` VARCHAR(100) NOT NULL DEFAULT 'No Aplica',  
	  `IDENTIFICACION` VARCHAR(50) DEFAULT NULL,           
	  `CANTIDAD` DOUBLE NOT NULL,                          
	  `PRECIO_UNITARIO` DOUBLE NOT NULL,                   
	  `DESCUENTO_PORCENTAJE` DOUBLE DEFAULT '0',           
	  `DESCUENTO_MONTO` DOUBLE DEFAULT '0',                
	  `SUBTOTAL` DOUBLE NOT NULL,                          
	  PRIMARY KEY (`ID_COTIZACION`,`ID_SERVICIO`)          
);

/*TABLA PARA CONJUNTO DE IMPUESTOS DE COTIZACIÓN*/ 
CREATE TABLE `SGFENS_COTIZACION_IMPUESTO` (            
	  `ID_COTIZACION` INT(11) NOT NULL,                    
	  `ID_IMPUESTO` INT(11) NOT NULL,                                    
	  PRIMARY KEY (`ID_COTIZACION`,`ID_IMPUESTO`)          
);

/*Cambios a tabla categoría*/
ALTER TABLE `CATEGORIA` ADD COLUMN `ID_ESTATUS` INT DEFAULT '1' NOT NULL AFTER `ID_EMPRESA`;


ALTER TABLE `MOVIMIENTO` 
	ADD COLUMN `FECHA_REGISTRO` DATETIME NULL AFTER `CONTABILIDAD`, 
	ADD COLUMN `ID_PROVEEDOR` INT NULL AFTER `FECHA_REGISTRO`, 
	ADD COLUMN `ORDEN_COMPRA` VARCHAR(30) NULL AFTER `ID_PROVEEDOR`, 
	ADD COLUMN `NUMERO_GUIA` VARCHAR(30) NULL AFTER `ORDEN_COMPRA`, 
	ADD COLUMN `ID_ALMACEN` INT NULL AFTER `NUMERO_GUIA`, 
	ADD COLUMN `CONCEPTO_MOVIMIENTO` VARCHAR(50) NULL AFTER `ID_ALMACEN`;
	
ALTER TABLE `CONCEPTO` 
	ADD COLUMN `PRECIO_DOCENA` DOUBLE DEFAULT '0' AFTER `OBSERVACIONES`, 
	ADD COLUMN `PRECIO_MAYOREO` DOUBLE DEFAULT '0' AFTER `PRECIO_DOCENA`, 
	ADD COLUMN `PRECIO_ESPECIAL` DOUBLE DEFAULT '0' AFTER `PRECIO_MAYOREO`;


CREATE TABLE `SGFENS_CORREO_BANDEJA_ENVIO`( 
    `ID_CORREO` INT NOT NULL AUTO_INCREMENT , 
    `ID_EMPRESA` INT NOT NULL COMMENT 'ID de la empresa que genero este correo', 
    `DESTINATARIO` TEXT COMMENT 'Lista de correos destinatarios separados por coma', 
    `DESTINATARIO_CC` TEXT COMMENT 'Lista de correos destinatarios Con Copia separados por coma', 
    `DESTINATARIO_CCO` TEXT COMMENT 'Lista de correos destinatarios Con Copia Oculta separados por coma', 
    `ASUNTO` VARCHAR(200) NOT NULL COMMENT 'Asunto del Correo', 
    `CONTENIDO` TEXT NOT NULL COMMENT 'Contenido del Correo', 
    `ARCHIVOS_ADJUNTOS_ID` VARCHAR(100) COMMENT 'Lista de IDs de archivos adjuntos separados por coma (p. ej: 25,156,158)', 
    `FECHA_ORIGINAL` DATETIME NOT NULL COMMENT 'Fecha en que se genero el correo', 
    `IS_ENVIADO` TINYINT NOT NULL DEFAULT '0' COMMENT 'Flag para indicar si el correo ya fue enviado o no. NO = En cola de envío.', 
    `FECHA_ENVIO` DATETIME COMMENT 'Fecha en la que se envio el correo', 
    PRIMARY KEY (`ID_CORREO`)
);

ALTER TABLE `EMPRESA` ADD COLUMN `ID_ESTATUS` INT DEFAULT '1' NOT NULL AFTER `REGIMEN_FISCAL`;



/*TABLA PEDIDO*/
CREATE TABLE `SGFENS_PEDIDO` (                                                                                                                                  
        `ID_PEDIDO` INT NOT NULL AUTO_INCREMENT UNIQUE,                                                                                                                  
        `ID_USUARIO_VENDEDOR` INT(11) NOT NULL COMMENT 'ID del usuario con rol vendedor que genera el pedido',                                                       
        `ID_EMPRESA` INT(10) NOT NULL,                                                                                                                                    
        `ID_CLIENTE` INT(11) NOT NULL COMMENT 'ID unico del Cliente que solicito el pedido',    
        `CONSECUTIVO_PEDIDO` INT(11) NOT NULL COMMENT 'Consecutivo de pedido para la empresa',                                                                   
        `FOLIO_PEDIDO` VARCHAR(30) DEFAULT NULL COMMENT 'Dicho folio estará compuesto por el prefijo P , seguido del  consecutivo comenzando por el 0001',           
        `FECHA_PEDIDO` DATETIME NOT NULL COMMENT 'Fecha en la que se levanto el pedido, default por el sistema',                                                                                                                           
        `TIPO_MONEDA` VARCHAR(10) NOT NULL,                                                                                                                               
        `TIEMPO_ENTREGA_DIAS` INT(11) NOT NULL DEFAULT '0',                                                                                                               
        `COMENTARIOS` TEXT,                                                                                                                                               
        `DESCUENTO_TASA` DOUBLE NOT NULL DEFAULT '0' COMMENT 'Porcentaje de descuento',                                                                                   
        `DESCUENTO_MONTO` DOUBLE NOT NULL DEFAULT '0' COMMENT 'Monto calculado del descuento',                                                                            
        `SUBTOTAL` DOUBLE NOT NULL,                                                                                                                                       
        `TOTAL` DOUBLE NOT NULL,
        `DESCUENTO_MOTIVO` VARCHAR(200) DEFAULT NULL COMMENT 'Motivo del descuento aplicado, solo en caso de existir',
        `FECHA_ENTREGA` DATETIME NOT NULL COMMENT 'Fecha pactada para la entrega del pedido',
        `FECHA_TENTATIVA_PAGO` DATETIME NOT NULL COMMENT 'Fecha tentativa del pago toal del pedido',
        `SALDO_PAGADO` DOUBLE NOT NULL DEFAULT '0' COMMENT 'Saldo pagado a la fecha respecto al total del pedido.',
        `ADELANTO` DOUBLE NOT NULL DEFAULT '0' COMMENT 'Esto pensando que se pueda entregar un producto a crédito sin necesidad de facturarlo aún y que como comprobante se entregue la hoja del pedido.',
        `ID_COMPROBANTE_FISCAL` INT(11) DEFAULT '0' COMMENT 'ID del Comprobante Fiscal generado a partir del pedido.',
        `ID_ESTATUS_PEDIDO` SMALLINT(6) DEFAULT '1' COMMENT 'ID del estatus del pedido',
        PRIMARY KEY (`ID_PEDIDO`)                                                                                                                   
);

/*TABLA PARA CONJUNTO DE PRODUCTOS DE PEDIDO*/ 
CREATE TABLE `SGFENS_PEDIDO_PRODUCTO` (            
	  `ID_PEDIDO` INT(11) NOT NULL,                    
	  `ID_CONCEPTO` INT(11) NOT NULL,                      
	  `DESCRIPCION` TEXT NOT NULL,                         
	  `UNIDAD` VARCHAR(100) NOT NULL DEFAULT 'No Aplica',  
	  `IDENTIFICACION` VARCHAR(50) DEFAULT NULL,           
	  `CANTIDAD` DOUBLE NOT NULL,                          
	  `PRECIO_UNITARIO` DOUBLE NOT NULL,                   
	  `DESCUENTO_PORCENTAJE` DOUBLE DEFAULT '0',           
	  `DESCUENTO_MONTO` DOUBLE DEFAULT '0',                
	  `SUBTOTAL` DOUBLE NOT NULL,                          
	  PRIMARY KEY (`ID_PEDIDO`,`ID_CONCEPTO`)          
);

/*TABLA PARA CONJUNTO DE SERVICIOS DE PEDIDO*/ 
CREATE TABLE `SGFENS_PEDIDO_SERVICIO` (            
	  `ID_PEDIDO` INT(11) NOT NULL,                    
	  `ID_SERVICIO` INT(11) NOT NULL,                      
	  `DESCRIPCION` TEXT NOT NULL,                         
	  `UNIDAD` VARCHAR(100) NOT NULL DEFAULT 'No Aplica',  
	  `IDENTIFICACION` VARCHAR(50) DEFAULT NULL,           
	  `CANTIDAD` DOUBLE NOT NULL,                          
	  `PRECIO_UNITARIO` DOUBLE NOT NULL,                   
	  `DESCUENTO_PORCENTAJE` DOUBLE DEFAULT '0',           
	  `DESCUENTO_MONTO` DOUBLE DEFAULT '0',                
	  `SUBTOTAL` DOUBLE NOT NULL,                          
	  PRIMARY KEY (`ID_PEDIDO`,`ID_SERVICIO`)          
);

/*TABLA PARA CONJUNTO DE IMPUESTOS DE PEDIDO*/ 
CREATE TABLE `SGFENS_PEDIDO_IMPUESTO` (            
	  `ID_PEDIDO` INT(11) NOT NULL,                    
	  `ID_IMPUESTO` INT(11) NOT NULL,                                    
	  PRIMARY KEY (`ID_PEDIDO`,`ID_IMPUESTO`)          
);

/*DATOS ADICIONALES DE USUARIOS CON ROL VENDEDOR*/
CREATE TABLE `SGFENS_VENDEDOR_DATOS`( 
	`ID_USUARIO` INT NOT NULL COMMENT 'ID del usuario (empleado) con rol de vendedor.', 
	`SUELDO_MENSUAL` DOUBLE NOT NULL DEFAULT '0' COMMENT 'Sueldo mensual del empleado en PESOS (MXN)', 
	`PORCENTAJE_COMISIONES` DOUBLE NOT NULL DEFAULT '0' COMMENT 'Porcentaje de comisiones del empleado.', 
	PRIMARY KEY (`ID_USUARIO`)
);

/* CATALOGO DE ESTATUS DEL PEDIDO */
CREATE TABLE `SGFENS_ESTATUS_PEDIDO`( 
	`ID_ESTATUS_PEDIDO` INT NOT NULL COMMENT 'ID unico del estatus', 
	`NOMBRE` VARCHAR(50) NOT NULL COMMENT 'Nombre del estatus', 
	`DESCRIPCION` VARCHAR(250) NOT NULL DEFAULT 'Sin descripcion' COMMENT 'Descripcion del estatus', 
	PRIMARY KEY (`ID_ESTATUS_PEDIDO`)
);

INSERT INTO `SGFENS_ESTATUS_PEDIDO`(`ID_ESTATUS_PEDIDO`,`NOMBRE`,`DESCRIPCION`) VALUES ( '1','PENDIENTE','Pendiente por Entregar');
INSERT INTO `SGFENS_ESTATUS_PEDIDO`(`ID_ESTATUS_PEDIDO`,`NOMBRE`,`DESCRIPCION`) VALUES ( '2','ENTREGADO','Entregado al Cliente');
INSERT INTO `SGFENS_ESTATUS_PEDIDO`(`ID_ESTATUS_PEDIDO`,`NOMBRE`,`DESCRIPCION`) VALUES ( '3','CANCELADO','Cancelado');


/*DATOS ADICIONALES COMPROBANTE FISCAL DESCRIPCION PARA RELACIONAR CON SERVICIOS*/
ALTER TABLE `COMPROBANTE_DESCRIPCION` 
	ADD COLUMN `ID_SERVICIO` INT DEFAULT '-1' NULL COMMENT 'ID unico del servicio relacionado a esta tupla de concepto. Si es un producto, concepto normal, el valor sera -1 por defecto.' AFTER `ORDEN`, 
	ADD COLUMN `NOMBRE_CONCEPTO` VARCHAR(200) NULL COMMENT 'Nombre desencriptado del concepto, ya sea producto o servicio' AFTER `ID_SERVICIO`;


/* TABLA PARA CONTROL DE COBRANZA (ABONOS) */
CREATE TABLE `SGFENS_COBRANZA_ABONO`( 
    `ID_COBRANZA_ABONO` INT NOT NULL AUTO_INCREMENT , 
    `ID_EMPRESA` INT NOT NULL COMMENT 'ID de la empresa a la que pertenece el registro',
    `ID_PEDIDO` INT NOT NULL DEFAULT '-1' COMMENT 'ID del Pedido relacionado al cobro, -1 en caso de estar relacionado a factura', 
    `ID_COMPROBANTE_FISCAL` INT NOT NULL DEFAULT '-1' COMMENT 'ID del Comprobante Fiscal relacionado al cobro, -1 en caso de estar relacionado a pedido', 
    `ID_USUARIO_VENDEDOR` INT NOT NULL COMMENT 'ID del usuario vendedor que registra el cobro', 
    `ID_CLIENTE` INT NOT NULL COMMENT 'ID del cliente que efectua el pago, se obtiene automáticamente del pedido o factura', 
    `FECHA_ABONO` DATETIME NOT NULL COMMENT 'Fecha y hora del registro del cobro', 
    `MONTO_ABONO` DOUBLE NOT NULL COMMENT 'Monto del abono', 
    `ID_ESTATUS` INT NOT NULL DEFAULT '1' COMMENT 'ID del estatus del abono, 1-Activo, 2-Inactivo/Cancelado', 
    `ID_COBRANZA_METODO_PAGO` INT NOT NULL COMMENT 'ID del metodo de pago relacionado a cobranza', 
    `IDENTIFICADOR_OPERACION` VARCHAR(100) COMMENT 'Identificador de la operación, p. ej. número de operación bancaria/No. de documento/No. de Credito', 
    `COMENTARIOS` VARCHAR(250) COMMENT 'Comentarios adicionales y notas del cobro', 
    PRIMARY KEY (`ID_COBRANZA_ABONO`)
);

/* TABLA PARA LOS DIFERENTES METODOS DE PAGO A SELECCIONAR EN LA COBRANZA (ABONOS)*/
CREATE TABLE `SGFENS_COBRANZA_METODO_PAGO`( 
    `ID_COBRANZA_METODO_PAGO` INT NOT NULL AUTO_INCREMENT , 
    `NOMBRE_METODO_PAGO` VARCHAR(50) NOT NULL , 
    `DESCRIPCION_METODO_PAGO` VARCHAR(250) NOT NULL , 
    PRIMARY KEY (`ID_COBRANZA_METODO_PAGO`)
);

INSERT INTO `SGFENS_COBRANZA_METODO_PAGO` (`ID_COBRANZA_METODO_PAGO`, `NOMBRE_METODO_PAGO`, `DESCRIPCION_METODO_PAGO`) VALUES('1','Tarjeta Crédito/Débito','Tarjeta de Crédito o Débito, se hace uso de Operación Bancaria');
INSERT INTO `SGFENS_COBRANZA_METODO_PAGO` (`ID_COBRANZA_METODO_PAGO`, `NOMBRE_METODO_PAGO`, `DESCRIPCION_METODO_PAGO`) VALUES('2','Efectivo','Pago en Efectivo');
INSERT INTO `SGFENS_COBRANZA_METODO_PAGO` (`ID_COBRANZA_METODO_PAGO`, `NOMBRE_METODO_PAGO`, `DESCRIPCION_METODO_PAGO`) VALUES('3','Documento (Cheque/Vale)','Pago con documento, Cheque o Vale');
INSERT INTO `SGFENS_COBRANZA_METODO_PAGO` (`ID_COBRANZA_METODO_PAGO`, `NOMBRE_METODO_PAGO`, `DESCRIPCION_METODO_PAGO`) VALUES('4','Transferencia Interbancaria','Pago interbancario (CLABE)');

/* Tabla para establecer permisos de acceso a los distintos aplicativos*/
CREATE TABLE `EMPRESA_PERMISO_APLICACION`( 
    `ID_EMPRESA` INT NOT NULL , 
    `ACCESO_SGFENS_PRETORIANO` TINYINT NOT NULL DEFAULT '0' COMMENT 'Flag para indicar si la empresa, sucursales y usuarios tienen acceso al sistema SGFENS Pretoriano Soft.', 
    PRIMARY KEY (`ID_EMPRESA`)
);


