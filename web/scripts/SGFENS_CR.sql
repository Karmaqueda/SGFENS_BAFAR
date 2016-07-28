
CREATE DATABASE `cr_bafar`;
USE `cr_bafar`;


-- copiamos contenido de bd facturav3
-- a nueva bd cr_bafar


-- Cesar Martinez 09-06-2016
-- Tablas para formularios dinámicos
CREATE TABLE cr_grupo_formulario( 
    `id_grupo_formulario` INT NOT NULL AUTO_INCREMENT COMMENT 'ID unico de tabla', 
    `nombre` VARCHAR(50) NOT NULL COMMENT 'Nombre del grupo de formulario', 
    `descripcion` VARCHAR(500) NOT NULL COMMENT 'Descripcion del grupo de formulario', 
    `fecha_hr_creacion` DATETIME NOT NULL COMMENT 'Fecha y hora de creacion del registro', 
    `id_estatus` INT NOT NULL DEFAULT '1' COMMENT 'ID de estatus del registro. 1 = activo, 2 =inactivo', 
    `id_empresa` INT NOT NULL COMMENT 'ID de empresa a la que pertenece el formulario', 
    PRIMARY KEY (`id_grupo_formulario`)
);

CREATE TABLE `cr_formulario`( 
    `id_formulario` INT NOT NULL AUTO_INCREMENT COMMENT 'ID unico del formulario', 
    `id_grupo_formulario` INT NOT NULL COMMENT 'ID de grupo formulario al que pertenece', 
    `orden_grupo` INT COMMENT 'Orden del formulario en el grupo', 
    `nombre` VARCHAR(50) NOT NULL COMMENT 'Nombre del formulario', 
    `descripcion` VARCHAR(500) COMMENT 'Descripcion del formulario', 
    `fecha_hr_creacion` DATETIME NOT NULL COMMENT 'Fecha y hr de creacion del registro', 
    `fecha_hr_ultima_edicion` DATETIME COMMENT 'Fecha y hr de ultima edicion', 
    `id_usuario_edicion` INT COMMENT 'ID del usuario que edito', 
    `id_estatus` INT NOT NULL COMMENT 'ID de estatus del registro. 1 = activo, 2 = inactivo', 
    `id_empresa` INT NOT NULL COMMENT 'ID de la empresa a la que pertenece el registro', 
    PRIMARY KEY (`id_formulario`)
);

CREATE TABLE `cr_tipo_campo`( 
    `id_tipo_campo` INT NOT NULL AUTO_INCREMENT COMMENT 'ID unico de tabla', 
    `nombre` VARCHAR(50) NOT NULL COMMENT 'Nombre del tipo de campo', 
    `descripcion` VARCHAR(500) NOT NULL COMMENT 'Descripcion del tipo de pago', 
    `img_vista_previa` VARCHAR(1000) COMMENT 'Nombre del archivo imagen de Vista previa del tipo de campo', 
    `icono_nombre` VARBINARY(1000) COMMENT 'Nombre del archivo imagen icono del tipo de campo', 
    `id_empresa` INT COMMENT 'ID de empresa a la que pertenece el registro', 
    `id_estatus` INT NOT NULL COMMENT 'ID de estatus del registro. 1 = activo, 2= inactivo', 
    `is_creado_sistema` INT(1) COMMENT 'Flag para indicar si el registro es creado por el sistema', 
    PRIMARY KEY (`id_tipo_campo`)
);

CREATE TABLE `cr_formulario_validacion`( 
    `id_formulario_validacion` INT NOT NULL AUTO_INCREMENT COMMENT 'ID unico de tabla', 
    `nombre` VARCHAR(50) NOT NULL COMMENT 'Nombre de la validacion', 
    `descripcion` VARCHAR(500) COMMENT 'Descripcion de la validacion', 
    `regex_java` VARCHAR(500) COMMENT 'Expresion regular para comparacion en formato para lenguaje JAVA', 
    `regex_lenguaje_ext` VARCHAR(500) COMMENT 'Expresion regular aplicable para otro lenguaje externo', 
    `is_creado_sistema` INT(1) NOT NULL DEFAULT '0' COMMENT 'Flag para indicar si el registro es general del sistema y aplica para todas las empresas. 0 = no, 1=si', 
    `id_empresa` INT COMMENT 'ID de empresa a la que pertenece el registro', 
    `id_estatus` INT NOT NULL COMMENT 'ID de estatus del registro. 1=activo, 2=inactivo.', 
    PRIMARY KEY (`id_formulario_validacion`)
);


CREATE TABLE `cr_formulario_campo`( 
    `id_formulario_campo` INT NOT NULL AUTO_INCREMENT COMMENT 'ID unico de tabla', 
    `id_formulario` INT NOT NULL COMMENT 'ID de formulario al que pertenece', 
    `id_tipo_campo` INT NOT NULL COMMENT 'ID del tipo de campo al que corresponde', 
    `orden_formulario` INT COMMENT 'Orden en el formulario', 
    `no_seccion` INT NOT NULL DEFAULT '1' COMMENT 'No de Seccion en el formulario', 
    `etiqueta` VARCHAR(100) NOT NULL COMMENT 'Nombre o etiqueta del control', 
    `descripcion` VARCHAR(500) COMMENT 'Descripcion a mostrar en el control. Ayuda o sugerencia', 
    `valor_defecto` VARCHAR(1000) COMMENT 'Valor por defecto para el campo', 
    `valor_sugerencia` VARCHAR(500) COMMENT 'Valor para mostrar como sugerencia en el campo', 
    `opciones` TEXT NULL COMMENT 'Valores para campos de opciones, radio, checkbox o select.',
    `is_requerido` INT(1) NOT NULL COMMENT 'Flag para indicar si el campo es requerido u opcional', 
    `id_formulario_validacion` INT COMMENT 'ID de validacion a utilizar para el campo', 
    `variable_formula` VARCHAR(25) NULL COMMENT 'Nombre de Variable para usar en formulas, no se repite el valor, llave unica.',
    `id_estatus` INT NOT NULL COMMENT 'ID  de estatus del registro. 1=activo, 2=inactivo', 
    `id_empresa` INT NOT NULL COMMENT 'ID de empresa a la que pertenece el registro', 
    PRIMARY KEY (`id_formulario_campo`)
);
ALTER TABLE `cr_formulario_campo` ADD UNIQUE `key_unique_variable` (`variable_formula`);

CREATE TABLE `cr_formulario_evento`( 
    `id_formulario_evento` INT NOT NULL AUTO_INCREMENT COMMENT 'ID unico de tabla', 
    `id_grupo_formulario` INT NOT NULL COMMENT 'ID de grupo formulario del que se deriva este evento', 
    `fecha_hr_creacion` DATETIME NOT NULL COMMENT 'Fecha y hr de creacion del registrro', 
    `fecha_hr_edicion` DATETIME COMMENT 'Fecha y hr de ultima edicion del registro', 
    `id_usuario_capturo` INT NOT NULL COMMENT 'ID del usuario que capturo el registro, debiendo ser normalmente el empleado', 
    `tipo_entidad_respondio` VARCHAR(30) DEFAULT 'CLIENTE' NOT NULL COMMENT 'Tipo de entidad que respondio el formulario o a quien se le aplico. Valores posibles: CLIENTE, PROSPECTO, EMPLEADO',
    `id_entidad_respondio` INT NOT NULL COMMENT 'ID de la entidad al que se le aplico el formulario', 
    `id_empresa` INT NOT NULL COMMENT 'ID de la empresa a la que pertenece el registro', 
    `id_estatus` INT NOT NULL DEFAULT '1' COMMENT 'ID de estatus del registro, 1 = activo, 2 = inactivo', 
    PRIMARY KEY (`id_formulario_evento`)
);

CREATE TABLE `cr_formulario_respuesta`( 
    `id_formulario_respuesta` INT NOT NULL AUTO_INCREMENT COMMENT 'ID unico de tabla', 
    `id_formulario_evento` INT NOT NULL COMMENT 'ID del evento de formulario aplicado al que pertenece esta respuesta', 
    `id_formulario` INT NOT NULL COMMENT 'ID del formulario del que se derivo la respuesta', 
    `id_formulario_campo` INT NOT NULL COMMENT 'ID del campo de formulario al que corresponde esta respuesta', 
    `valor` VARCHAR(1000) COMMENT 'Valor de la respuesta', 
    `descripcion` VARCHAR(1000) COMMENT 'Descripcion o etiqueta adicional de la respuesta', 
    `id_empresa` INT NOT NULL COMMENT 'ID de la empresa a la que pertenece el registro', 
    `id_estatus` INT NOT NULL DEFAULT '1' COMMENT 'ID de estatus del registro. 1=activo, 2=inactivo', 
    PRIMARY KEY (`id_formulario_respuesta`)
);

-- Valores por defecto de tipos de campo
INSERT INTO `cr_tipo_campo`(`id_tipo_campo`,`nombre`,`descripcion`,`img_vista_previa`,`icono_nombre`,`id_empresa`,`id_estatus`,`is_creado_sistema`) VALUES ( 1,'Campo de Texto','Campo de texto general. Text.',NULL,NULL,0,1,1);
INSERT INTO `cr_tipo_campo`(`id_tipo_campo`,`nombre`,`descripcion`,`img_vista_previa`,`icono_nombre`,`id_empresa`,`id_estatus`,`is_creado_sistema`) VALUES ( 2,'Opción Múltiple','Opción múltiple, selección múltiple. Radiobutton.',NULL,NULL,0,1,1);
INSERT INTO `cr_tipo_campo`(`id_tipo_campo`,`nombre`,`descripcion`,`img_vista_previa`,`icono_nombre`,`id_empresa`,`id_estatus`,`is_creado_sistema`) VALUES ( 3,'Opción Única','Opción única o múltiple, selección única. Checkbox.',NULL,NULL,0,1,1);
INSERT INTO `cr_tipo_campo`(`id_tipo_campo`,`nombre`,`descripcion`,`img_vista_previa`,`icono_nombre`,`id_empresa`,`id_estatus`,`is_creado_sistema`) VALUES ( 4,'Lista de Opciones','Lista de opciónes, selección única. Select.',NULL,NULL,0,1,1);
INSERT INTO `cr_tipo_campo`(`id_tipo_campo`,`nombre`,`descripcion`,`img_vista_previa`,`icono_nombre`,`id_empresa`,`id_estatus`,`is_creado_sistema`) VALUES ( 5,'Subtítulo','Etiqueta de subtítulo o sección. Label.',NULL,NULL,0,1,1);
INSERT INTO `cr_tipo_campo`(`id_tipo_campo`,`nombre`,`descripcion`,`img_vista_previa`,`icono_nombre`,`id_empresa`,`id_estatus`,`is_creado_sistema`) VALUES ( 6,'Capturar Fotografía','Fotografía. File-Img.',NULL,NULL,0,1,1);
INSERT INTO `cr_tipo_campo`(`id_tipo_campo`,`nombre`,`descripcion`,`img_vista_previa`,`icono_nombre`,`id_empresa`,`id_estatus`,`is_creado_sistema`) VALUES ( 7,'Capturar Firma','Firma autográfa. File-Img.',NULL,NULL,0,1,1);
INSERT INTO `cr_tipo_campo`(`id_tipo_campo`,`nombre`,`descripcion`,`img_vista_previa`,`icono_nombre`,`id_empresa`,`id_estatus`,`is_creado_sistema`) VALUES ( 8,'Calendario','Calendario para fecha con selección de día, mes y año. Date.',NULL,NULL,0,1,1);
INSERT INTO `cr_tipo_campo`(`id_tipo_campo`,`nombre`,`descripcion`,`img_vista_previa`,`icono_nombre`,`id_empresa`,`id_estatus`,`is_creado_sistema`) VALUES ( 9,'Hora del Día','Selector para una hora del día, con datos de hora y minuto. Time.',NULL,NULL,0,1,1);
INSERT INTO `cr_tipo_campo`(`id_tipo_campo`,`nombre`,`descripcion`,`img_vista_previa`,`icono_nombre`,`id_empresa`,`id_estatus`,`is_creado_sistema`) VALUES ( 10,'Fórmula','Campo de texto para captura de expresiones algebraicas. Text. Referirse a http://www.objecthunter.net/exp4j/',NULL,NULL,0,1,1);


-- Valores por defecto para validaciones de formulario
INSERT INTO `cr_formulario_validacion`(`id_formulario_validacion`,`nombre`,`descripcion`,`regex_java`,`regex_lenguaje_ext`,`is_creado_sistema`,`id_empresa`,`id_estatus`) VALUES ( 1,'Número Entero','Números enteros sin decimales','\\b\\d+\\b',NULL,'1','0','1');
INSERT INTO `cr_formulario_validacion`(`id_formulario_validacion`,`nombre`,`descripcion`,`regex_java`,`regex_lenguaje_ext`,`is_creado_sistema`,`id_empresa`,`id_estatus`) VALUES ( 2,'Número 2 Decimales','Número con 2 decimales máximo, puede no llevar decimales o tener solo uno','\\d+(\\.\\d{1,2})?',NULL,'1','0','1');
INSERT INTO `cr_formulario_validacion`(`id_formulario_validacion`,`nombre`,`descripcion`,`regex_java`,`regex_lenguaje_ext`,`is_creado_sistema`,`id_empresa`,`id_estatus`) VALUES ( 3,'Número 4 Decimales','Número con 4 decimales máximo. Puede no tener decimales o tener menos de 4.','\\d+(\\.\\d{1,4})?',NULL,'1','0','1');
INSERT INTO `cr_formulario_validacion`(`id_formulario_validacion`,`nombre`,`descripcion`,`regex_java`,`regex_lenguaje_ext`,`is_creado_sistema`,`id_empresa`,`id_estatus`) VALUES ( 4,'Fecha','Fecha con formato dd/mm/yyyy','(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)',NULL,'1','0','1');
INSERT INTO `cr_formulario_validacion`(`id_formulario_validacion`,`nombre`,`descripcion`,`regex_java`,`regex_lenguaje_ext`,`is_creado_sistema`,`id_empresa`,`id_estatus`) VALUES ( 5,'Fecha y Hora','Fecha y hora a 24 hrs con formato dd/mm/yyyy hh:mm','(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)+\\s+([01]?[0-9]|2[0-3]):[0-5][0-9]',NULL,'1','0','1');
INSERT INTO `cr_formulario_validacion`(`id_formulario_validacion`,`nombre`,`descripcion`,`regex_java`,`regex_lenguaje_ext`,`is_creado_sistema`,`id_empresa`,`id_estatus`) VALUES ( 6,'Hora','Hora a 24 hrs con formato hh:mm','([01]?[0-9]|2[0-3]):[0-5][0-9]',NULL,'1','0','1');
INSERT INTO `cr_formulario_validacion`(`id_formulario_validacion`,`nombre`,`descripcion`,`regex_java`,`regex_lenguaje_ext`,`is_creado_sistema`,`id_empresa`,`id_estatus`) VALUES ( 7,'e-Mail','Correo electrónico','^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$',NULL,'1','0','1');
INSERT INTO `cr_formulario_validacion`(`id_formulario_validacion`,`nombre`,`descripcion`,`regex_java`,`regex_lenguaje_ext`,`is_creado_sistema`,`id_empresa`,`id_estatus`) VALUES ( 8,'Código Postal','Código Postal México (5 dígitos)','[0-9]{5}',NULL,'1','0','1');


CREATE TABLE `cr_score` (                                                                                   
    `id_score` INT NOT NULL AUTO_INCREMENT COMMENT 'ID unico de tabla',                                   
    `nombre` VARCHAR(50) NOT NULL COMMENT 'Nombre del Score',                                                 
    `descripcion` VARCHAR(500) DEFAULT NULL COMMENT 'Descripcion extensa del Score',                          
    `fecha_hr_creacion` DATETIME NOT NULL COMMENT 'Fecha y hr de creacion del registro',                      
    `fecha_hr_ultima_edicion` DATETIME DEFAULT NULL COMMENT 'Fecha y hr de la ultima edicion del registoro',  
    `id_usuario_edicion` INT DEFAULT NULL COMMENT 'ID del usuario que edito el registro por ultima vez',  
    `id_empresa` INT NOT NULL COMMENT 'ID de la empresa a la que corresponde el registro',                
    `id_estatus` INT NOT NULL DEFAULT '1' COMMENT 'ID de estatus del registro. 1=activo, 2=inactivo',             
    PRIMARY KEY (`id_score`)                                                                                  
);

CREATE TABLE `cr_score_detalle` (                                                                                               
    `id_score_detalle` INT NOT NULL AUTO_INCREMENT COMMENT 'ID unico de tabla',                                               
    `id_formulario_campo` INT NOT NULL COMMENT 'ID del campo de formulario asociado al detalle de score',                     
    `id_score` INT NOT NULL COMMENT 'ID de score maestro al que pertenece este detalle',                                      
    `valor_exacto` VARCHAR(500) DEFAULT NULL COMMENT 'Valor exacto a comparar. Opcional, puede sustituirse por rango_min y max',  
    `rango_min` DOUBLE DEFAULT NULL COMMENT 'Valor minimo en rango para comparar',                                                
    `rango_max` DOUBLE DEFAULT NULL COMMENT 'Valor maximo en rango para comparar',                                                
    `puntos_score` INT NOT NULL DEFAULT '0' COMMENT 'Puntos añadidos o restados a Score por este detalle.',                  
    `id_empresa` INT NOT NULL COMMENT 'ID de empresa a la que pertenece el registro',                                         
    `id_estatus` INT NOT NULL DEFAULT '1' COMMENT 'ID de estatus del registro. 1=activo, 2=inactivo',                                     
    PRIMARY KEY (`id_score_detalle`)                                                                                              
);

-- Cesar Martinez 20-06-2016
-- nuevos modulos para vista en left_content
INSERT INTO preto_modulo(`ID_PRETO_MODULO`,`ID_ESTATUS`,`NOMBRE_MODULO`,`IDENTIFICADOR`) VALUES ( 98,'1','MODULO_CR_FRM_FORMULARIOS','MCR_FRM_FORMULARIOS');
INSERT INTO preto_modulo(`ID_PRETO_MODULO`,`ID_ESTATUS`,`NOMBRE_MODULO`,`IDENTIFICADOR`) VALUES ( 99,'1','MODULO_CR_FRM_GRUPO','MCR_FRM_GRUPO');
INSERT INTO preto_modulo(`ID_PRETO_MODULO`,`ID_ESTATUS`,`NOMBRE_MODULO`,`IDENTIFICADOR`) VALUES ( 100,'1','MODULO_CR_FRM_FORMULARIO','MCR_FRM_FORMULARIO');
INSERT INTO preto_modulo(`ID_PRETO_MODULO`,`ID_ESTATUS`,`NOMBRE_MODULO`,`IDENTIFICADOR`) VALUES ( 101,'1','MODULO_CR_FRM_VALIDACION','MCR_FRM_VALIDACION');
INSERT INTO preto_modulo(`ID_PRETO_MODULO`,`ID_ESTATUS`,`NOMBRE_MODULO`,`IDENTIFICADOR`) VALUES ( 102,'1','MODULO_CR_SCORE','MCR_SCORE');

-- Cesar Martinez 23-06-2016
-- Tablas para producto
CREATE TABLE `cr_producto_credito`( 
    `id_producto_credito` INT NOT NULL AUTO_INCREMENT COMMENT 'ID unico de tabla', 
    `id_producto_credito_padre` INT COMMENT 'ID de cr_producto_credito padre', 
    `nombre` VARCHAR(50) NOT NULL COMMENT 'Nombre del productio', 
    `descripcion` VARCHAR(500) COMMENT 'Descripcion detallada del producto', 
    `fecha_hr_creacion` DATETIME NOT NULL COMMENT 'Fecha y hr de creacion del registro', 
    `fecha_hr_ultima_edicion` DATETIME COMMENT 'Fecha y hr de ultima edicion del registro', 
    `id_usuario_edicion` INT NOT NULL COMMENT 'ID del usuario que creo o edito por ultima vez el regsitro', 
    `id_score` INT COMMENT 'ID de score asociado a producto para validacion de puntaje', 
    `id_grupo_formulario_solic` INT COMMENT 'ID del grupo formulario para Solicitud asociado', 
    `id_grupo_formulario_verif` INT COMMENT 'ID del grupo formulario para Verificacion asociado', 
    `tipo_amortizacion` VARCHAR(50) COMMENT 'Tipo de amortizacion aplicada a producto. Cadena de texto', 
    `monto` DOUBLE COMMENT 'Monto del producto', `plazo` DOUBLE COMMENT 'Plazo del producto', 
    `tasa_interes_anual` DOUBLE COMMENT 'Tasa de interes anual del producto', 
    `tasa_interes_mora` DOUBLE COMMENT 'Tasa de interes moratoria del producto', 
    `gastos_cobranza` DOUBLE COMMENT 'Gastos de cobranza aplicables a producto', 
    `id_empresa` INT NOT NULL COMMENT 'ID de empresa a la que pertenece el registro', 
    `id_estatus` INT NOT NULL COMMENT 'ID de estatus del registro. 1=activo, 2=inactivo', 
    PRIMARY KEY (`id_producto_credito`)
);

CREATE TABLE `cr_producto_regla`( 
    `id_producto_regla` INT NOT NULL AUTO_INCREMENT COMMENT 'ID unico de tabla', 
    `id_producto_credito` INT NOT NULL COMMENT 'ID de producto credito al que pertenece esta regla', 
    `etiqueta` VARCHAR(100) NOT NULL COMMENT 'Etiqueta de regla', 
    `rango_min` DOUBLE COMMENT 'Rango minimo. Mayor o igual que', 
    `rango_max` DOUBLE COMMENT 'Rango maximo. Menor o igual a', 
    `valor_exacto` DOUBLE COMMENT 'Valor exacto. Igual a', 
    `id_formulario_campo` INT COMMENT 'ID de formulario campo relacionado.', 
    `etiqueta_campo_relacion` VARCHAR(100) COMMENT 'Relacion a campo a comparar mediante etiqueta', 
    `is_regla_aplicacion_score` INT(1) NOT NULL DEFAULT '0' COMMENT 'Bandera para indicar si es una Regla de aplicacion de score. 0=no, 1=si', 
    `is_regla_rechazo` INT(1) NOT NULL DEFAULT '0' COMMENT 'Bandera para indicar si es una regla de rechazo. 0=no, 1=si', 
    `clave_tipo_regla` VARCHAR(25) DEFAULT NULL COMMENT 'Clave para identificar el tipo fijo de regla en código',                              
    `id_empresa` INT NOT NULL COMMENT 'ID de empresa a la que pertenece el registro', 
    `id_estatus` INT NOT NULL COMMENT 'ID de estatus del registro. 1=activo, 2=inactivo', 
    PRIMARY KEY (`id_producto_regla`)
);

CREATE TABLE `cr_producto_puntaje_monto`( 
    `id_producto_puntaje_monto` INT NOT NULL AUTO_INCREMENT COMMENT 'ID unico de tabla', 
    `id_producto_credito` INT NOT NULL COMMENT 'ID de producto credito asociado', 
    `rango_min` DOUBLE NOT NULL DEFAULT '0' COMMENT 'Rango minimo de puntos score. Mayor o igual a', 
    `rango_max` DOUBLE NOT NULL DEFAULT '0' COMMENT 'Rango maximo de puntos score. Menor o igual a', 
    `pct_autorizado` DOUBLE NOT NULL DEFAULT '100' COMMENT 'Porcentaje autorizado, expresado en base 100. p. ej. 20 = 20 pct', 
    `id_empresa` INT NOT NULL COMMENT 'ID de empresa a la que pertenece el registro ', 
    `id_estatus` INT NOT NULL COMMENT 'ID de estatus del registro. 1=activo, 2=inactivo', 
    PRIMARY KEY (`id_producto_puntaje_monto`)
);

-- Leonardo Montes de Oca, 27-06-2016
-- Solo roles que se usaran en bafar.
UPDATE ROLES SET OCULTO = TRUE, MOSTRAR_PRETORIANO = 0;
-- Administrador, ID 2
UPDATE ROLES SET OCULTO = FALSE, MOSTRAR_PRETORIANO = 1 WHERE ID_ROLES = 2;
-- Gerente, ID 40
INSERT INTO ROLES (ID_ROLES,NOMBRE,DESCRIPCION,OCULTO,ID_EMPRESA,MOSTRAR_PRETORIANO) VALUES (40, 'GERENTE', 'Gerente, Control Total de altas, bajas y cambios', false, -1, 1);
-- Promotor, ID 17
UPDATE ROLES SET OCULTO = FALSE, MOSTRAR_PRETORIANO = 1 WHERE ID_ROLES = 17;
-- Gestor de crédito, ID 29
UPDATE ROLES SET OCULTO = FALSE, MOSTRAR_PRETORIANO = 1, NOMBRE = 'GESTOR DE CREDITO' WHERE ID_ROLES = 29;
-- Verificador, ID 41
INSERT INTO ROLES (ID_ROLES,NOMBRE,DESCRIPCION,OCULTO,ID_EMPRESA,MOSTRAR_PRETORIANO) VALUES (41, 'VERIFICADOR', 'Solo vista de lectura, delimitado a sus clientes y prospectos', false, -1, 1);
-- Mesa de Control, ID 42
INSERT INTO ROLES (ID_ROLES,NOMBRE,DESCRIPCION,OCULTO,ID_EMPRESA,MOSTRAR_PRETORIANO) VALUES (42, 'MESA DE CONTROL', 'Acceso a todos los clientes, promotores, verificadores, gestores de cobranza. Tiene delimitaciones', false, -1, 1);
-- Dirección, ID 43
INSERT INTO ROLES (ID_ROLES,NOMBRE,DESCRIPCION,OCULTO,ID_EMPRESA,MOSTRAR_PRETORIANO) VALUES (43, 'DIRECCION', 'Acceso solo al dashboard principal, Acceso a los reportes', false, -1, 1);


-- Cesar Martinez 27-06-2016
-- Cambio en nombre de columna por error tipografico
ALTER TABLE `cr_producto_regla` 
    CHANGE `id_regla_rechazo` `is_regla_rechazo` INT(1) DEFAULT '0' NOT NULL COMMENT 'Bandera para indicar si es una regla de rechazo. 0=no, 1=si';

INSERT INTO preto_modulo(`ID_PRETO_MODULO`,`ID_ESTATUS`,`NOMBRE_MODULO`,`IDENTIFICADOR`) VALUES ( 103,'1','MODULO_CR_PRODUCTO_CR','MCR_PROD_CR');


-- Cesar Martinez 30-06-2016
-- Cambio en tabla formulario_evento para agregar campos para coordenadas
ALTER TABLE `cr_formulario_evento` 
    ADD COLUMN `latitud` DOUBLE NULL COMMENT 'Coordenada de Latitud, ubicacion donde se levanto el registro de este evento' AFTER `id_entidad_respondio`, 
    ADD COLUMN `longitud` DOUBLE NULL COMMENT 'Coordenada de Longitud, ubicacion donde se levanto el registro de este evento' AFTER `latitud`;


CREATE TABLE `cr_doc_imprimible`( 
    `id_doc_imprimible` INT NOT NULL AUTO_INCREMENT COMMENT 'ID unico de tabla', 
    `nombre` VARCHAR(50) NOT NULL COMMENT 'Nombre del imprimible', 
    `descripcion` VARCHAR(500) COMMENT 'Descripción del imprimible', 
    `fecha_hr_creacion` DATETIME NOT NULL COMMENT 'Fecha y hr de creacion del registro', 
    `fecha_hr_ultima_edicion` DATETIME COMMENT 'Fecha y hr de la ultima edicion del registro', 
    `id_usuario_edicion` INT NOT NULL COMMENT 'ID del usuario que creo o edito por ultima vez el registro', 
    `tipo_imprimible` VARCHAR(50) NOT NULL COMMENT 'Tipo imprimible: CONTRATO, SOLICITUD, TABLA_AMORTIZACION, CODIGO_BARRAS, OTRO', 
    `nombre_archivo_jasper` VARCHAR(500) NOT NULL COMMENT 'Nombre del archivo jasper a utilizar', 
    `id_empresa` INT NOT NULL COMMENT 'ID de empresa a la que pertenece el registro', 
    `id_estatus` INT NOT NULL COMMENT 'ID de estatus del registro. 1=activo, 2=inactivo', 
    PRIMARY KEY (`id_doc_imprimible`)
);

CREATE TABLE `cr_doc_imp_parametro`( 
    `id_doc_imp_parametro` INT NOT NULL AUTO_INCREMENT COMMENT 'ID unico de tabla', 
    `id_doc_imprimible` INT NOT NULL COMMENT 'ID de documento imprimible al que pertenece este parametro', 
    `parametro_clave` VARCHAR(50) NOT NULL COMMENT 'Nombre del parametro a enviar a jasper', 
    `descripcion` VARCHAR(500) COMMENT 'Descripcion del parametro, texto libre', 
    `valor_defecto` VARCHAR(500) COMMENT 'Valor por defecto que adoptara este parametro', 
    `asocia_variable_formula` VARCHAR(25) COMMENT 'Nombre de variable_formula de la que se obtendra el valor para usar en este parametro y mapearlo hacia jasper.', 
    `id_empresa` INT NOT NULL COMMENT 'ID de empresa a la que pertenece el registro', 
    `id_estatus` INT NOT NULL COMMENT 'ID de estatus del registro 1= activo, 2=inactivo', 
    PRIMARY KEY (`id_doc_imp_parametro`)
);

CREATE TABLE `cr_producto_x_imprimible`( 
    `id_producto_x_imprimible` INT NOT NULL AUTO_INCREMENT COMMENT 'ID unico de la tabla', 
    `id_doc_imprimible` INT NOT NULL COMMENT 'ID de documento imprimible asociado a relacion N x N', 
    `id_producto_credito` INT NOT NULL COMMENT 'ID de producto credito asociado en relacion N x N.', 
    PRIMARY KEY (`id_producto_x_imprimible`)
);

-- Cesar Martinez 01-07-2016
ALTER TABLE cr_formulario_evento
 ADD COLUMN `folio_movil` VARCHAR(50) DEFAULT NULL COMMENT 'Folio movil unico de evento generado en app movil.' AFTER longitud;

-- Leonardo Montes de Oca, 04-07-2016
-- Tabla para el control y relacion del producto adquirido. Así como para saber el proceso en que se encuentra la solicitud.
CREATE TABLE cr_frm_evento_solicitud (
    `id_frm_evento_solicitud` INT NOT NULL AUTO_INCREMENT COMMENT 'ID unico de tabla',
    `id_formulario_evento` INT NOT NULL COMMENT 'ID del evento de formulario aplicado al que pertenece esta informacion',
    `id_producto_credito` INT NOT NULL COMMENT 'ID de producto credito al que pertenece esta informacion',     
    `fecha_hr_creacion` DATETIME NOT NULL COMMENT 'Fecha y hr de creacion/modificacion del registro', 
    `id_usuario_edicion` INT NOT NULL COMMENT 'ID del usuario que creo o edito por ultima vez el registro',
    `id_estado_solicitud` INT NOT NULL COMMENT 'ID del proceso en el cual se encuentra la solicitud',
    PRIMARY KEY (`id_frm_evento_solicitud`)
);

CREATE TABLE cr_estado_solicitud (
    `id_estado_solicitud` INT NOT NULL AUTO_INCREMENT COMMENT 'ID unico de tabla',
    `nombre` VARCHAR(50) NOT NULL COMMENT 'Nombre del estado de soicitud', 
    `descripcion` VARCHAR(500) NOT NULL COMMENT 'Descripcion del estado de soicitud', 
    `id_empresa` INT COMMENT 'ID de empresa a la que pertenece el registro', 
    `id_estatus` INT NOT NULL COMMENT 'ID de estatus del registro. 1 = activo, 2= inactivo', 
    `is_creado_sistema` INT(1) COMMENT 'Flag para indicar si el registro es creado por el sistema', 
    PRIMARY KEY (`id_estado_solicitud`)
);

-- Tabla para el manejo de historial de la solicitud
CREATE TABLE cr_solicitud_bitacora(
    `id_solicitud_bitacora` INT NOT NULL AUTO_INCREMENT COMMENT 'ID unico de tabla',
    `id_formulario_evento` INT NOT NULL COMMENT 'ID del evento de formulario aplicado al que pertenece esta informacion',
    `id_estado_solicitud` INT NOT NULL COMMENT 'ID del proceso en el cual se encuentra la solicitud',
    `id_usuario` INT NOT NULL COMMENT 'ID del usuario que creo o edito por ultima vez el registro',
    `id_empresa` INT COMMENT 'ID de empresa a la que pertenece el registro', 
    `descripcion_evento` VARCHAR(500) COMMENT 'Detalle del proceso aplicado al evento',
    `fecha_hr_creacion` DATETIME NOT NULL COMMENT 'Fecha y hr de creacion del registro',
    PRIMARY KEY (`id_solicitud_bitacora`)
);

-- César Martinez, 04-07-2016
ALTER TABLE `cr_producto_regla` CHANGE `clave_tipo_regla` `clave_tipo_regla` VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL  COMMENT 'Clave para identificar el tipo fijo de regla en código';
UPDATE `cr_tipo_campo` 
 SET `id_tipo_campo`='2',`nombre`='Opción Múltiple',`descripcion`='Opción múltiple, selección múltiple. Checkbox.',`img_vista_previa`=NULL,`icono_nombre`=NULL,`id_empresa`='0',`id_estatus`='1',`is_creado_sistema`='1' WHERE `id_tipo_campo`='2';

UPDATE `cr_tipo_campo` 
 SET `id_tipo_campo`='3',`nombre`='Opción Única',`descripcion`='Opción única o múltiple, selección única. Radiobutton.',`img_vista_previa`=NULL,`icono_nombre`=NULL,`id_empresa`='0',`id_estatus`='1',`is_creado_sistema`='1' WHERE `id_tipo_campo`='3';

INSERT INTO preto_modulo(`ID_PRETO_MODULO`,`ID_ESTATUS`,`NOMBRE_MODULO`,`IDENTIFICADOR`) VALUES ( 104,'1','MODULO_CR_IMPRIMIBLE','MCR_IMPRIMIBLE');
INSERT INTO preto_modulo(`ID_PRETO_MODULO`,`ID_ESTATUS`,`NOMBRE_MODULO`,`IDENTIFICADOR`) VALUES ( 105,'1','MODULO_CR_CTRL_CONTROL','MCR_CONTROL');
INSERT INTO preto_modulo(`ID_PRETO_MODULO`,`ID_ESTATUS`,`NOMBRE_MODULO`,`IDENTIFICADOR`) VALUES ( 106,'1','MODULO_CR_CTRL_SOLICITUD','MCR_CTRL_SOLICITUD');

ALTER TABLE `cr_formulario_campo` CHANGE `variable_formula` `variable_formula` VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL  COMMENT 'Nombre de Variable para usar en formulas, no se repite el valor, llave unica.';

-- Leonardo Montes de Oca, 06-07-2016
-- Atributo para el control de que campos tienen que ser revisados:
ALTER TABLE cr_formulario_respuesta
ADD COLUMN `revisar` INT(1) DEFAULT 0 COMMENT 'Campo para el control de revisar su informacion, 0 no revisar, 1 revisar';

-- Leonardo Montes de Oca, 05-07-2016
-- Estatus de solicitudes:
INSERT INTO `cr_estado_solicitud`(`id_estado_solicitud`,`nombre`,`descripcion`,`id_empresa`,`id_estatus`,`is_creado_sistema`) 
VALUES (1, 'Rechazado','Rechazado, ya sea por regla de aprobación o no dispone de producto seleccionable, algun rol la marco como no aprobada.',0,1,1);
INSERT INTO `cr_estado_solicitud`(`id_estado_solicitud`,`nombre`,`descripcion`,`id_empresa`,`id_estatus`,`is_creado_sistema`) 
VALUES (2, 'Por Revisar','Enviado a Revisión a Mesa de Control.La solicitud cumplió con reglas de aprobación y montos de cuota.',0,1,1);
INSERT INTO `cr_estado_solicitud`(`id_estado_solicitud`,`nombre`,`descripcion`,`id_empresa`,`id_estatus`,`is_creado_sistema`) 
VALUES (3, 'En Revisión','Solicitud que fue revisada y presenta algún detalle por corregir de parte del promotor en campo.',0,1,1);
INSERT INTO `cr_estado_solicitud`(`id_estado_solicitud`,`nombre`,`descripcion`,`id_empresa`,`id_estatus`,`is_creado_sistema`) 
VALUES (4, 'Aprobada por Mesa de Control','Solicitud que fue revisada y aprobada por Mesa de Control.',0,1,1);
INSERT INTO `cr_estado_solicitud`(`id_estado_solicitud`,`nombre`,`descripcion`,`id_empresa`,`id_estatus`,`is_creado_sistema`) 
VALUES (5, 'Aprobada por Verificación','Solicitud que fue revisada y aprobada por Verificación.',0,1,1);
INSERT INTO `cr_estado_solicitud`(`id_estado_solicitud`,`nombre`,`descripcion`,`id_empresa`,`id_estatus`,`is_creado_sistema`) 
VALUES (6, 'Aprobada','Solicitud que fue revisada y aprobada por Verificación y por Mesa de Control.',0,1,1);
INSERT INTO `cr_estado_solicitud`(`id_estado_solicitud`,`nombre`,`descripcion`,`id_empresa`,`id_estatus`,`is_creado_sistema`) 
VALUES (7, 'Impresión Liberada','Solicitud que cuenta con la verificación de documentos, así como la validación del verificador en sitio, por lo que ya se encuentra en posición de liberar la documentación para su firma en físico.',0,1,1);
INSERT INTO `cr_estado_solicitud`(`id_estado_solicitud`,`nombre`,`descripcion`,`id_empresa`,`id_estatus`,`is_creado_sistema`) 
VALUES (8, 'Dispersada','Solicitud que fue aprobada y que los fondos han sido depositados en la cuenta del cliente.',0,1,1);
INSERT INTO `cr_estado_solicitud`(`id_estado_solicitud`,`nombre`,`descripcion`,`id_empresa`,`id_estatus`,`is_creado_sistema`) 
VALUES (9, 'Cancelada','Solicitud que fue Cancelada.',0,1,1);
INSERT INTO `cr_estado_solicitud`(`id_estado_solicitud`,`nombre`,`descripcion`,`id_empresa`,`id_estatus`,`is_creado_sistema`) 
VALUES (99, 'Borrador','Es un registro borrador.',0,1,1);


-- Cesar Martinez 11-07-2016
-- Datos adicionales de producto credito
ALTER TABLE `cr_producto_credito` 
    ADD COLUMN `costo_anual_total` DOUBLE NULL COMMENT 'Costo Anual Total (CAT)' AFTER `gastos_cobranza`, 
    ADD COLUMN `garantias_descripcion` VARCHAR(500) NULL COMMENT 'Descripción de garantías solicitadas en el crédito' AFTER `costo_anual_total`;

CREATE TABLE `cr_producto_seguro`( 
    `id_producto_seguro` INT NOT NULL AUTO_INCREMENT COMMENT 'ID unico de tabla', 
    `id_producto_credito` INT NOT NULL COMMENT 'ID de producto al que esta relacionado este registro de seguro', 
    `is_obligatorio` INT NOT NULL DEFAULT '1' COMMENT 'Flag para indicar si es obligatorio el seguro', 
    `tipo_seguro` VARCHAR(100) COMMENT 'Tipo de seguro, descripcion o nombre', 
    `aseguradora_nombre` VARCHAR(100) COMMENT 'Nombre de la aseguradora que presta el servicio.', 
    `id_estatus` INT NOT NULL DEFAULT '1' COMMENT 'Estatus del registro, 1=activo, 2=inactivo', 
    `id_empresa` INT NOT NULL COMMENT 'ID de la empresa a la que pertenece este registro', 
    PRIMARY KEY (`id_producto_seguro`)
);


ALTER TABLE `cr_frm_evento_solicitud` 
    ADD COLUMN `id_usuario_verificador` INT NULL COMMENT 'ID del usuario Verificador de la solicitud' AFTER `id_estado_solicitud`, 
    ADD COLUMN `sap_bp` VARCHAR(100) NULL COMMENT 'Dato Interlocutor comercial (Business Partner) de SAP' AFTER `id_usuario_verificador`, 
    ADD COLUMN `sap_no_contrato` VARCHAR(100) NULL COMMENT 'Dato de SAP numero de contrato' AFTER `sap_bp`, 
    ADD COLUMN `sap_fecha_apertura` DATE NULL COMMENT 'Dato de SAP fecha de apertura u operacion de credito' AFTER `sap_no_contrato`, 
    ADD COLUMN `sap_fecha_amortizacion` DATE NULL COMMENT 'Dato de SAP fecha de amortizacion' AFTER `sap_fecha_apertura`, 
    ADD COLUMN `sap_inf_plazo_contrato` VARCHAR(150) NULL COMMENT 'Dato de SAP informativo plazo de contrato. P. ej: 48 Quincenas' AFTER `sap_fecha_amortizacion`, 
    ADD COLUMN `sap_inf_fecha_corte` VARCHAR(150) NULL COMMENT 'Dato de SAP informativo para Fechas de corte. P. ej:5 dias anterior a la fecha de pago ' AFTER `sap_inf_plazo_contrato`, 
    ADD COLUMN `sap_inf_fecha_pago` VARCHAR(150) NULL COMMENT 'Dato de SAP informativo para fecha de pago. P. ej: Días 15 y último de mes.' AFTER `sap_inf_fecha_corte`;


-- Cesar Martinez 16-07-2016
INSERT INTO `cr_estado_solicitud`(`id_estado_solicitud`,`nombre`,`descripcion`,`id_empresa`,`id_estatus`,`is_creado_sistema`) VALUES ( '10','Por Dispersar','Solicitud que fue aprobada pero que los fondos NO han sido depositados al cliente..','0','1','1');

ALTER TABLE `cr_frm_evento_solicitud` 
    ADD COLUMN `sap_tabla_amortizacion` TEXT NULL COMMENT 'Dato de SAP tabla de amortizacion en formato JSON, correspondiente a clase en codigo com.tsp.sct.cr.CrTablaAmortizacion' AFTER `sap_inf_fecha_pago`;


ALTER TABLE `cr_producto_credito` 
    ADD COLUMN `id_grupo_formulario_fotos` INT NULL COMMENT 'ID del grupo formulario para evidencia fotografica asociado' AFTER `id_estatus`;


ALTER TABLE `cr_frm_evento_solicitud` 
    ADD COLUMN `monto_solicitado` DOUBLE NULL COMMENT 'Monto solicitado del credito, corresponderia al monto definido en el sub producto asociado' AFTER `sap_tabla_amortizacion`, 
    ADD COLUMN `monto_aprobado` DOUBLE NULL COMMENT 'Monto aprobado, corresponderia al monto que le fue aprobado despues de parametricos a la solicitud' AFTER `monto_solicitado`, 
    ADD COLUMN `fecha_solicitado` DATETIME NULL COMMENT 'Fecha en que fue solicitado el credito (cuando llego a estatus 2 Por Revisar)' AFTER `monto_aprobado`, 
    ADD COLUMN `fecha_aprobado` DATETIME NULL COMMENT 'Fecha en que se designo como listo la solicitud para ser contrato (estatus 6 Aprobado)' AFTER `fecha_solicitado`;

-- Leonardo Montes de Oca, 20-07-2016
-- atributo para colocar algun comentario en caso de que exista un detalle con la captura de la informacion
-- Atributo para el control de que campos tienen que ser revisados:
ALTER TABLE cr_formulario_respuesta
ADD COLUMN `revisar_comentario` VARCHAR(200) DEFAULT '' COMMENT 'Campo para colocar el comentario del control de revisar su informacion';

-- Cesar Martinez, 21-07-2016
-- Nuevo modulo para Gestor de cobranza
INSERT INTO preto_modulo(`ID_PRETO_MODULO`,`ID_ESTATUS`,`NOMBRE_MODULO`,`IDENTIFICADOR`) VALUES ( 107,'1','MODULO_CR_COB_COBRANZA','MCR_COBRANZA');
INSERT INTO preto_modulo(`ID_PRETO_MODULO`,`ID_ESTATUS`,`NOMBRE_MODULO`,`IDENTIFICADOR`) VALUES ( 108,'1','MODULO_CR_COB_CLIENTES','MCR_COB_CLIENTES');

-- Tabla de clientes para modulo CR
CREATE TABLE `cr_cred_cliente`( 
    `id_cred_cliente` INT NOT NULL AUTO_INCREMENT COMMENT 'ID unico autoincrementable de registro', 
    `id_formulario_evento` INT COMMENT 'ID relacionado de cr_formulario_evento, que origino al cliente de credito', 
    `id_estatus` INT NOT NULL DEFAULT '1' COMMENT 'ID de estatus del registro. 1=activo, 2=inactivo', 
    `id_empresa` INT NOT NULL COMMENT 'ID de la empresa a la que pertenece el registro', 
    `fecha_hr_creacion` DATETIME NOT NULL COMMENT 'Fecha y hr de creacion del registro', 
    `fecha_hr_edicion` DATETIME COMMENT 'Fecha y hr de ultima edicion del registro', 
    `id_usuario_edicion` INT NULL COMMENT 'ID del usuario que creo o edito el registro por ultima vez', 
    `rfc` VARCHAR(15) COMMENT 'RFC del cliente', 
    `nombre_primer` VARCHAR(50) NOT NULL COMMENT 'Primer nombre del cliente', 
    `nombre_segundo` VARCHAR(50) COMMENT 'Segundo nombre del cliente',
    `apellido_paterno` VARCHAR(50) NOT NULL COMMENT 'Apellido paterno del cliente', 
    `apellido_materno` VARCHAR(50) COMMENT 'Apellido materno del cliente', 
    `latitud` DOUBLE COMMENT 'Ubicacion latitud del cliente', 
    `longitud` DOUBLE COMMENT 'Ubicacion longitud del cliente', 
    `correos_electronicos` VARCHAR(200) COMMENT 'Multiples correos electronicos de contacto separados por coma', 
    `id_cliente_s_tercero` VARCHAR(40) COMMENT 'ID o clave del cliente en sistema tercero. En SAP representa el BP (interlocutor)', 
    `id_credito_s_tercero` VARCHAR(40) COMMENT 'ID o clave del credito asignado al cliente en sistema tercero. En SAP representa el No. de Contrato', 
    `fecha_inicio_credito` DATE COMMENT 'Fecha de inicio del credito', 
    `fecha_fin_credito` DATE COMMENT 'Fecha de termino del credito', 
    `plazo_meses` INT COMMENT 'Plazo en meses del crédito. P. ej: 2 meses', 
    `plazo_vencimiento` INT COMMENT 'Plazo en terminos del vencimiento del crédito. P. ej: 8 semanas', 
    `vencimiento` VARCHAR(30) COMMENT 'Tipo de vencimiento del crédito: Semanal, Quincenal, Mensual, Decenal, Catorcenal', 
    `monto_prestado` DOUBLE COMMENT 'Monto del prestamo original.', 
    `monto_adeudado` DOUBLE COMMENT 'Monto del saldo remanente por pagar del credito.', 
    `dias_mora` INT COMMENT 'Numero de dias que tiene la cuenta en mora', 
    `saldo_vencido` DOUBLE COMMENT 'Monto del saldo vencido', 
    `importe_pagar_vencimiento` DOUBLE COMMENT 'Importe a pagar en el vencimiento (semana, quincena, mensual...)', 
    `fecha_hora_agenda` DATETIME COMMENT 'Fecha y hora en la que se agenda el cobro. Opcional', 
    `num_pagos_realizados_vencimiento` INT COMMENT 'Numero de pagos realizados en el vencimiento', 
    `suma_monto_pagado_vencimiento` DOUBLE COMMENT 'Suma de montos de los pagos realizados en el vencimiento', 
    `cuotas_devengadas` INT COMMENT 'Numero de cuotas devengadas', 
    `cuotas_pagadas` INT COMMENT 'Numero de cuotas pagadas', 
    `cuotas_vencidas` INT COMMENT 'Numero de cuotas vencidas', 
    PRIMARY KEY (`id_cred_cliente`)
);

-- Campo adicional para tabla de notas, para relacionarlo con Clientes de credito
ALTER TABLE `cxc_nota` 
    ADD COLUMN `ID_CR_CRED_CLIENTE` INT NULL COMMENT 'ID HEREDADO DE TABLA CR_CRED_CLIENTE, OPCIONAL' AFTER `ID_CXP_COMPROBANTE_FISCAL`;

-- Cesar Martinez 22-07-2016
ALTER TABLE `cr_frm_evento_solicitud` 
    ADD COLUMN `sap_fecha_fin_credito` DATE NULL COMMENT 'Fecha de fin de credito pactado' AFTER `sap_fecha_amortizacion`,
    ADD COLUMN `plazo_meses` INT NULL COMMENT 'Plazo en meses.' AFTER `fecha_aprobado`, 
    ADD COLUMN `plazo_vencimiento` INT NULL COMMENT 'Plazo en terminos del vencimiento elegido (semanas, quincenas, catorcenas, meses, decenas)' AFTER `plazo_meses`, 
    ADD COLUMN `tipo_vencimiento` VARCHAR(100) NULL COMMENT 'Tipo de vencimiento otorgado: SEMANAL, DECENAL, CATORCENAL, QUINCENAL, MENSUAL' AFTER `plazo_vencimiento`,
    CHANGE `sap_inf_plazo_contrato` `sap_inf_plazo_contrato` VARCHAR(150) CHARACTER SET utf8 COLLATE utf8_general_ci NULL  COMMENT 'Dato de SAP informativo plazo de contrato. P. ej: 48 Quincenas', 
    CHANGE `sap_inf_fecha_corte` `sap_inf_fecha_corte` VARCHAR(150) CHARACTER SET utf8 COLLATE utf8_general_ci NULL  COMMENT 'Dato de SAP informativo para Fechas de corte. P. ej:5 dias anterior a la fecha de pago ', 
    CHANGE `sap_inf_fecha_pago` `sap_inf_fecha_pago` VARCHAR(150) CHARACTER SET utf8 COLLATE utf8_general_ci NULL  COMMENT 'Dato de SAP informativo para fecha de pago. P. ej: Días 15 y último de mes.';


-- Cesar Martinez 24-07-2016
-- Nueva columna para almacenar los datos de la Orden de Pago otorgada por SAP, en formato JSON
ALTER TABLE `cr_frm_evento_solicitud` 
    ADD COLUMN `sap_orden_pago` VARCHAR(1000) NULL COMMENT 'Objeto de SAP Orden de Pago en formato JSON, correspondiente a clase en codigo com.tsp.sct.cr.CrOrdenPago' AFTER `tipo_vencimiento`;

-- Cesar Martinez 26-07-2016
-- Nuevas columnas para almacenar datos necesarios para imprimible contrato
ALTER TABLE `cr_frm_evento_solicitud` 
    ADD COLUMN `cuota_regular` DOUBLE NULL COMMENT 'Cuota regular o correspondiente a primer pago de amortizacion.' AFTER `sap_orden_pago`, 
    ADD COLUMN `sap_monto_total_pagar` DOUBLE NULL COMMENT 'Monto total a pagar del credito con intereses incluidos, suma de pagos de tabla amortizacion.' AFTER `cuota_regular`;