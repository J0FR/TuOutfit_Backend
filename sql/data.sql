--OUTFIT_ENTITY
--ID, COLORES, GENERO, IMAGEN, NOMBRE, OCASIONES, PRECIO, RANGO_EDAD, TALLA, DESCRIPCION
--INSERT INTO OUTFIT_ENTITY VALUES (ID, COLORES, GENERO, IMAGEN, NOMBRE, OCASIONES, PRECIO, RANGO_EDAD, TALLA, DESCRIPCION);

--PRENDA_ENTITY
--ID, COLORES, GENERO, IMAGEN, NOMBRE, OCASIONES, PRECIO, RANGO_EDAD, TALLA, URL_SITIO_WEB_COMPRA, MARCA_ID
--INSERT INTO PRENDA_ENTITY VALUES (ID, COLORES, GENERO, IMAGEN, NOMBRE, OCASIONES, PRECIO, RANGO_EDAD, TALLA, URL_SITIO_WEB_COMPRA, MARCA_ID);

--PRENDA_ENTITY_OUTFITS
--PRENDAS_ID, OUTFITS_ID
--INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (PRENDAS_ID, OUTFITS_ID);

--MARCA_ENTITY
--ID, DETALLE_DE_MARCA, LOGO, NOMBRE, URL_SITIO_WEB
--INSERT INTO MARCA_ENTITY VALUES (ID, DETALLE_DE_MARCA, LOGO, NOMBRE, URL_SITIO_WEB);

--UBICACION_ENTITY
--ID, LATITUD, LONGITUD
--INSERT INTO UBICACION_ENTITY VALUES (ID, LATITUD, LONGITUD);

--TIENDA_FISICA_ENTITY
--ID, HORARIOS, NOMBRE, MARCA_ID, UBICACION_ID
--INSERT INTO TIENDA_FISICA_ENTITY VALUES (ID, HORARIOS, NOMBRE, MARCA_ID, UBICACION_ID);


    --COLORES:
    --0: ROJO, 1: AZUL, 2: DORADO, 3: VERDE, 4: PLATA, 5: AMARILLO, 6: CAFE, 7: BLANCO, 8:NEGRO

    --GENERO:
    --0: HOMBRE, 1: MUJER, 2: UNISEX

    --OCASIONES:
    --0: BODA, 1: FIESTA, 2: CENA, 3: CASUAL, 4: FORMAL, 5: GRADO

    --RANGO_EDAD:
    --0: BEBE, 1: NINHO, 2: ADOLESCENTE, 3: JOVEN, 4: ADULTO, 5: ABUELOS


--Outfit 1

--Marca 1
INSERT INTO MARCA_ENTITY VALUES (1, 'Ropa mágica que convierte tus sueños en estilo.', 'https://www.shopdisney.com/on/demandware.static/-/Sites-shopDisney-Library/default/dwc4d33617/images/default-logo.svg', 'Disney', 'https://www.shopdisney.com/franchises/disney/clothing/');
--Tiendas
INSERT INTO UBICACION_ENTITY VALUES (1, 35.46285592290404, -97.64799256441759);
INSERT INTO TIENDA_FISICA_ENTITY VALUES (1, '10:00-20:00', 'Disney Outlet Store - Shoppes At Oklahoma City', 1, 1);

INSERT INTO UBICACION_ENTITY VALUES (2, 40.758252965455284, -73.98504954622754);
INSERT INTO TIENDA_FISICA_ENTITY VALUES (2, '9:00-21:00', 'Disney Store - Times Square', 1, 2);

INSERT INTO UBICACION_ENTITY VALUES (3, 36.163702742881014, -115.15772774677949);
INSERT INTO TIENDA_FISICA_ENTITY VALUES (3, '10:00-20:00', 'Disney Outlet Store - Las Vegas North Premium Outlets', 1, 3);

--Marca 2
INSERT INTO MARCA_ENTITY VALUES (2, 'Creamos diseños atemporales que te hacen lucir y sentirte genial todos los días.', 'https://www.esprit.com/-img/common/Esprit_Logo_2023_Red_RGB.svg', 'ESPRIT', 'https://www.esprit.com.co');
--Tiendas
INSERT INTO UBICACION_ENTITY VALUES (4, 4.666727888556293, -74.05374773428223);
INSERT INTO TIENDA_FISICA_ENTITY VALUES (4, '10:00-20:00', 'Esprit Andino Bogotá', 2, 4);

INSERT INTO UBICACION_ENTITY VALUES (5, 50.042361479186404, 10.228776837549606);
INSERT INTO TIENDA_FISICA_ENTITY VALUES (5, '9:00-21:00', 'Esprit GALERIA Karstadt Kaufhof', 2, 5);

--Marca 3
INSERT INTO MARCA_ENTITY VALUES (3, 'Creemos que la moda es una forma de expresión personal y se refleja en la amplia gama de estilos que ofrecemos.', 'https://theme.zdassets.com/theme_assets/36268/b0ce7503bf380deff58ba15c456fa1e5e5c024dd.svg', 'Only', 'https://www.only.com/es-es');
--Tiendas
INSERT INTO UBICACION_ENTITY VALUES (6, 19.086417708301614, 72.88897940632177);
INSERT INTO TIENDA_FISICA_ENTITY VALUES (6, '10:00-21:00', 'ONLY Store - Phoenix Marketcity - Mumbai', 3, 6);

--Marca 4
INSERT INTO MARCA_ENTITY VALUES (4, 'Mustang es reconocida por su estilo audaz y contemporáneo, combinando a la perfección moda y calidad. “Sin ilusión no hay proyecto”', 'https://www.mtngshoes.com/on/demandware.static/Sites-Mustang-Site/-/default/dw4b94f89a/images/logo.svg', 'Mustang', 'https://www.mtngshoes.com');
--Tiendas
INSERT INTO UBICACION_ENTITY VALUES (7, 39.469148035567, -0.4874632089642139);
INSERT INTO TIENDA_FISICA_ENTITY VALUES (7, '10:00-22:00', 'Mustang Valencia', 4, 7);

--Marca 5
INSERT INTO MARCA_ENTITY VALUES (5, 'El destino integral de moda y estilo de vida, que inspira a las personas a expresar su verdadero yo a través del estilo.', 'https://www.ynap.com/wp-content/uploads/2022/09/yoox.png', 'Yoox', 'https://www.yoox.com');


INSERT INTO OUTFIT_ENTITY VALUES (1, '1', '1', 'https://img.bantoa.com/images/large/1636710/outfit-basic-8338.jpg', 'Women Basic 1', '3', 128, '3', 'M', 'Outfit hecho para aquellas mujeres que quieren vestirse casual, sin complicaciones.');

--Prendas Outfit 1
--Prenda 1
INSERT INTO PRENDA_ENTITY VALUES (1, '1', '1', 'https://img.bantoa.com/squareimage/7110239.jpg', 'Disney Mickey Years', '3', 17, '3', 'M', 'https://www.amazon.es/dp/B07D48YG9F/', 1);
--Prenda 2
INSERT INTO PRENDA_ENTITY VALUES (2, '1', '1', 'https://img.bantoa.com/squareimage/7749617.jpg', 'Jeans mujer azul luz lavado', '3', 33, '3', 'M', 'https://www.amazon.es/dp/B099S6SXVK', 2);
--Prenda 3
INSERT INTO PRENDA_ENTITY VALUES (3, '4', '2', 'https://img.bantoa.com/squareimage/7963008.jpg', 'Gorra de beisbol gris', '3', 14, '3', 'M', 'https://www.amazon.es/dp/B09NW7Y1DL/', 3);
--Prenda 4
INSERT INTO PRENDA_ENTITY VALUES (4, '1', '2', 'https://m.media-amazon.com/images/I/71KfEErpnRL._AC_UY575_.jpg', 'Zapatillas Bajas', '3', 40, '3', 'M', 'https://www.amazon.es/dp/B076ZL5CCV/?th=1', 4);
--Prenda 5
INSERT INTO PRENDA_ENTITY VALUES (5, '1', '1', 'https://img.bantoa.com/squareimage/8066032.jpg', 'SMMR', '3', 24, '3', 'M', 'https://www.yoox.com/es/45743793MK/item', 5);

INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (1, 1);
INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (2, 1);
INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (3, 1);
INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (4, 1);
INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (5, 1);

--Outfit 2

--Marca 6
INSERT INTO MARCA_ENTITY VALUES (6, 'Oodji es una compañía de moda que ofrece prendas contemporáneas y asequibles para hombres, mujeres y niños.', 'https://www.oodji.com/local/templates/.default/images/logo_white.svg', 'oodji', 'https://www.oodji.com');
--Tiendas
INSERT INTO UBICACION_ENTITY VALUES (8, 55.782252976802305, 37.70439237321012);
INSERT INTO TIENDA_FISICA_ENTITY VALUES (8, '10:00-22:00', 'oodji в ТЦ Серебряный Дом (м. Электрозаводская)', 6, 8);

--Marca 7
INSERT INTO MARCA_ENTITY VALUES (7, 'Contribuir al mundo con productos únicos de buena calidad, diseño atractivo y precio accesible que además traigan a los consumidores emociones y experiencias positivas en cualquiera de sus relaciones con nuestra marca.', 'https://gioseppo.com/en-us/static/version1684911625/frontend/Gioseppo/default/en_US/images/logo.svg', 'Gioseppo', 'https://gioseppo.com');
--Tiendas
INSERT INTO UBICACION_ENTITY VALUES (9, 38.53853977970572, -0.13672610431761997);
INSERT INTO TIENDA_FISICA_ENTITY VALUES (9, '10:00-21:00', 'Gioseppo Benidorm', 7, 9);

INSERT INTO UBICACION_ENTITY VALUES (10, 39.98672982445997, -0.038741965872845514);
INSERT INTO TIENDA_FISICA_ENTITY VALUES (10, '10:00-20:30', 'Gioseppo Castellón', 7, 10);

INSERT INTO UBICACION_ENTITY VALUES (11, 38.29672315162442, -0.6218229043314537);
INSERT INTO TIENDA_FISICA_ENTITY VALUES (11, '10:00-21:00', 'Gioseppo Elche (Parque Empresarial)', 7, 11);

INSERT INTO UBICACION_ENTITY VALUES (12, 38.26523086267503, -0.6956597690706368);
INSERT INTO TIENDA_FISICA_ENTITY VALUES (12, '10:00-14:00 / 17:00-20:30', 'Gioseppo Elche (C.C. L’Aljub)', 7, 12);

INSERT INTO UBICACION_ENTITY VALUES (13, 40.425776541126154, -3.6863101361595674);
INSERT INTO TIENDA_FISICA_ENTITY VALUES (13, '10:00-20:30', 'Gioseppo Madrid', 7, 13);

INSERT INTO UBICACION_ENTITY VALUES (14, 37.98561550004942, -1.131753486062916);
INSERT INTO TIENDA_FISICA_ENTITY VALUES (14, '10:00-14:00 / 17:00-21:00', 'Gioseppo Murcia', 7, 14);

INSERT INTO UBICACION_ENTITY VALUES (15, 37.39027608881924, -5.9935971547131865);
INSERT INTO TIENDA_FISICA_ENTITY VALUES (15, '10:00 a 20:30', 'Gioseppo Sevilla', 7, 15);


INSERT INTO OUTFIT_ENTITY VALUES (2, '6', '1', 'https://img.bantoa.com/images/large/1636684/outfit-casual-11285.jpg', 'Women Casual 1', '3', 164, '3', 'M', 'Outfit casual, pero con inclinaciones formales. Ideal para una salida con amigos.');

--Prendas Outfit 2
--Prenda 1
--prenda 2
--Prenda 2
INSERT INTO PRENDA_ENTITY VALUES (6, '1', '1', 'https://img.bantoa.com/squareimage/7884901.jpg', 'Suéter de Cuello Alto Ajustado de Viscosa', '3', 10, '4', 'M', 'https://www.amazon.es/dp/B0B4DHNW2V', 6);
--Prenda 3
INSERT INTO PRENDA_ENTITY VALUES (7, '6', '1', 'https://img.bantoa.com/squareimage/8173994.jpg', 'Botas mujer café poliéster', '3', 70, '4', 'M', 'https://www.amazon.es/dp/B09R7WB2YL', 7);
--Prenda 4
INSERT INTO PRENDA_ENTITY VALUES (8, '6', '1', 'https://img.bantoa.com/squareimage/7513507.jpg', 'Chaqueta mujer cuello clasico, cremallera, manga larga', '3', 27, '4', 'M', 'https://www.yoox.com/es/16150903MC/item', 5);
--Prenda 5
INSERT INTO PRENDA_ENTITY VALUES (9, '6', '1', 'https://img.bantoa.com/squareimage/8185257.jpg', 'Bolso de mano mujer animalier con cremallera ecopiel', '3', 24, '4', 'M', 'https://www.yoox.com/es/45721627CE/item', 5);

INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (2, 2);
INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (6, 2);
INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (7, 2);
INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (8, 2);
INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (9, 2);

--Outfit 3

--Marca 8
INSERT INTO MARCA_ENTITY VALUES (8, 'Desigual es una marca de moda que se caracteriza por sus diseños coloridos y originales.', 'https://cdn.palbincdn.com/users/9531/images/Desigual_logo_black-2-1679566142.jpg', 'Desigual', 'https://www.desigual.com');
--Tiendas
INSERT INTO UBICACION_ENTITY VALUES (16, 4.667778068453636, -74.05415431943962);
INSERT INTO TIENDA_FISICA_ENTITY VALUES (16, '10:00-20:30', 'Desigual Zona Rosa - Bogotá', 8, 16);

INSERT INTO UBICACION_ENTITY VALUES (17, 4.733035108409972, -74.06673581943991);
INSERT INTO TIENDA_FISICA_ENTITY VALUES (17, '10:00-20:30', 'Desigual Parque La Colina - Bogotá', 8, 17);

--Marca 9
INSERT INTO MARCA_ENTITY VALUES (9, 'Como Marco Tozzi, somos una marca de calzado líder que combina estilo, calidad y comodidad en cada uno de nuestros diseños. ¡Descubre nuestra amplia gama de estilos modernos y atemporales y encuentra el par perfecto que refleje tu estilo único!', 'https://marcotozzi.com/on/demandware.static/Sites-MARCOTOZZI-CON-Site/-/default/dwa9da2760/images/logo-marcotozzi-white.svg', 'Marco Tozzi', 'https://marcotozzi.com');
--Tiendas
INSERT INTO UBICACION_ENTITY VALUES (18, 51.944442024548444, 8.855658470798273);
INSERT INTO TIENDA_FISICA_ENTITY VALUES (18, '-', 'Marco Tozzi Klingenbergstraße', 9, 18);

--Marca 10
INSERT INTO MARCA_ENTITY VALUES (10, 'Somos una empresa de moda internacional con sede en Hamburgo, en Alemania. Interpretamos las últimas tendencias de las pasarelas y las llevamos al armario de todas las mujeres, transformándolas en prendas que se adaptan a cualquier ocasión, estilo y talla', 'https://esres.bonprix.es/mstatic/dist/gfx/bonprix_logo.svg', 'Bonprix', 'https://www.bonprix.es');
--Tiendas
INSERT INTO UBICACION_ENTITY VALUES (19, 53.59905883139042, 10.073254142050786);
INSERT INTO TIENDA_FISICA_ENTITY VALUES (19, '-', 'Bonprix Handelsgesellschaft', 10, 19);

INSERT INTO OUTFIT_ENTITY VALUES (3, '1', '1', 'https://cdn.discordapp.com/attachments/1071941314809172089/1115130142881226802/outfit3.png', 'Women Casual 2', '3', 129, '3', 'M', 'Outfit casual si tu color favorito es el azul oscuro!');

--Prendas Outfit 3
--Prenda 1
INSERT INTO PRENDA_ENTITY VALUES (10, '1', '1', 'https://img.bantoa.com/squareimage/7813871.jpg', 'Chaqueta mujer jean', '3', 57, '3', 'M', 'https://www.amazon.es/dp/B09R7XCQKK', 8);
--Prenda 2
--Prenda 6
--Prenda 3
INSERT INTO PRENDA_ENTITY VALUES (11, '1', '1', 'https://img.bantoa.com/squareimage/6958466.jpg', 'Bolso de cuero azul mujer', '3', 42, '3', 'M', 'https://www.amazon.es/dp/B08Z5PM1Z2', 9);
--Prenda 4
INSERT INTO PRENDA_ENTITY VALUES (12, '1', '1', 'https://img.bantoa.com/squareimage/6787291.jpg', 'Pantalón elástico mujer', '3', 20, '3', 'M', 'https://www.bonprix.es/style/pantalon-elastico-1437860017/', 10);

INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (10, 3);
INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (6, 3);
INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (11, 3);
INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (12, 3);

--Outfit 4

INSERT INTO OUTFIT_ENTITY VALUES (4, '0', '1', 'https://cdn.discordapp.com/attachments/1071941314809172089/1115150151951056907/outfit4.png', 'Women summertime 1', '3', 51, '3', 'M', 'Outfit casual para el verano!');

--Prendas Outfit 4
--Prenda 1
INSERT INTO PRENDA_ENTITY VALUES (13, '0', '1', 'https://pix.bonprix.es/imgc/0/0/2/3/0/0/0/6/6/7/_640/23000667/camiseta-amapola.jpg', 'Camiseta Amapola', '3', 10, '3', 'M', 'https://www.bonprix.es/producto/camiseta-1341365119', 10);
--Prenda 2
INSERT INTO PRENDA_ENTITY VALUES (14, '0', '2', 'https://cdn-ssl.s7.disneystore.com/is/image/DisneyShopping/2016058385627?fmt=webp&qlt=70&wid=1280&hei=1280', 'Pixar Fuzzy Fun Bucket Hat', '3', 20, '3', 'M', 'https://www.shopdisney.com/pixar-fuzzy-fun-bucket-hat-for-adults-by-spirit-jersey-420164087370.html', 1);
--Prenda 3
INSERT INTO PRENDA_ENTITY VALUES (15, '7', '1', 'https://m.media-amazon.com/images/I/61oAnIiaAhL._AC_UY606_.jpg', 'Pantalón beige', '3', 21, '3', 'M', 'https://www.amazon.es/dp/B09RLLVG8P', 2);

INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (13, 4);
INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (14, 4);
INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (15, 4);

--Outfit 5

INSERT INTO OUTFIT_ENTITY VALUES (5, '8', '0', 'https://cdn.discordapp.com/attachments/1071941314809172089/1115150152257257593/outfit5.png', 'Men Formal 1', '4', 81, '4', 'M', 'Outfit formal para el trabajo!');

--Prendas Outfit 5
--Prenda 1
INSERT INTO PRENDA_ENTITY VALUES (16, '0', '0', 'https://pix.bonprix.es/imgc/0/0/2/1/0/4/9/9/7/9/_640/21049979/pantalon-chino-azul-oscuro.jpg', 'Pantalón chino', '4', 40, '4', 'M', 'https://www.bonprix.es/producto/pantalon-chino-1830693688', 10);
--Prenda 2
INSERT INTO PRENDA_ENTITY VALUES (17, '4', '0', 'https://pix.bonprix.es/imgc/0/0/2/1/2/2/5/8/9/4/_640/21225894/americana-de-punto-gris.jpg', 'Americana de punto', '4', 28, '4', 'M', 'https://www.bonprix.es/producto/americana-de-punto-2021202417', 10);
--Prenda 3
INSERT INTO PRENDA_ENTITY VALUES (18, '7', '0', 'https://pix.bonprix.es/imgc/0/0/2/3/0/4/8/3/5/3/_640/23048353/camiseta-de-tipo-polo-blanco.jpg', 'Camiseta de tipo polo', '4', 13, '4', 'M', 'https://www.bonprix.es/producto/camiseta-de-tipo-polo-2585283803', 10);

INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (16, 5);
INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (17, 5);
INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (18, 5);

--Outfit 6

INSERT INTO OUTFIT_ENTITY VALUES (6, '1', '0', 'https://cdn.discordapp.com/attachments/1071941314809172089/1115150152546648065/outfit6.png', 'Men beach 1', '3', 63, '3', 'M', 'Outfit casual, excelente para la playa y el verano');

--Prendas Outfit 6
--Prenda 1
INSERT INTO PRENDA_ENTITY VALUES (19, '4', '0', 'https://images.only.com/22015623/3246317/001/onlysons-regularfitsweatshort-graa.jpg', 'Shorts corte regular', '3', 20, '3', 'M', 'https://www.only.com/es-es/product/22015623_2381/shorts-corte-regular', 3);
--Prenda 2
INSERT INTO PRENDA_ENTITY VALUES (20, '1', '0', 'https://pix.bonprix.es/imgc/0/0/2/3/0/4/9/6/6/8/_640/23049668/camisa-de-manga-corta-azul-marino-con-flores.jpg', 'Camisa de manga corta azul marino con flores', '3', 18, '3', 'M', 'https://www.bonprix.es/producto/camisa-de-manga-corta-2585624369', 10);
--Prenda 3
INSERT INTO PRENDA_ENTITY VALUES (21, '8', '0', 'https://cdn.discordapp.com/attachments/1071941314809172089/1115150152827682857/sandalias.png', 'Sandalias para senderismo', '3', 25, '3', 'M', 'https://www.bonprix.es/producto/sandalias-para-senderismo-1522635947', 10);

INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (19, 6);
INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (20, 6);
INSERT INTO PRENDA_ENTITY_OUTFITS VALUES (21, 6);
