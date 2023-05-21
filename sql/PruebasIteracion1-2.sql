INSERT INTO OUTFIT_ENTITY VALUES(1, '1', '1', 'https://cdn.discordapp.com/attachments/1071941314809172089/1108031052607389819/J0FR_3d_render_of_a_fashion_girl_20b6852b-3da1-4e44-bdb6-6efa78267b5c.png', 'Outfit #1', '1', '1500', '1', 'XS', 'Este outfit es genial!');
INSERT INTO OUTFIT_ENTITY VALUES(2, '2', '0', 'https://cdn.discordapp.com/attachments/1071941314809172089/1108031052607389819/J0FR_3d_render_of_a_fashion_girl_20b6852b-3da1-4e44-bdb6-6efa78267b5c.png', 'Outfit #2', '2', '2500', '2', 'XS', 'Este outfit es genial!');
INSERT INTO OUTFIT_ENTITY VALUES(3, '3', '1', 'https://cdn.discordapp.com/attachments/1071941314809172089/1108031052607389819/J0FR_3d_render_of_a_fashion_girl_20b6852b-3da1-4e44-bdb6-6efa78267b5c.png', 'Outfit #3', '3', '3500', '3', 'XS', 'Este outfit es genial!');

INSERT INTO MARCA_ENTITY  VALUES (1,'Marca Chevere', 'https://upload.wikimedia.org/wikipedia/commons/thumb/f/fd/Zara_Logo.svg/480px-Zara_Logo.svg.png','ZARA #1','WWW.UWU.COM');
INSERT INTO MARCA_ENTITY  VALUES (2,'Marca Cool', 'https://upload.wikimedia.org/wikipedia/commons/thumb/f/fd/Zara_Logo.svg/480px-Zara_Logo.svg.png','ZARA #2','WWW.UWU.COM');
INSERT INTO MARCA_ENTITY  VALUES (3,'Marca Local', 'https://upload.wikimedia.org/wikipedia/commons/thumb/f/fd/Zara_Logo.svg/480px-Zara_Logo.svg.png','ZARA #3','WWW.UWU.COM');

INSERT INTO PRENDA_ENTITY VALUES (1,1,0,'https://cdn.discordapp.com/attachments/1071941314809172089/1108034971517603890/J0FR_3d_render_a_shirt_e018df48-e9c8-41c2-bb2f-3f4d96582daa.png','Prenda #1',1,10,1,1,'https://www.zara.com/co/es/vestido-halter-cut-out-con-lino-p07707939.html?v1=272729065&v2=2184058',1);    
INSERT INTO PRENDA_ENTITY VALUES (2,1,0,'https://cdn.discordapp.com/attachments/1071941314809172089/1108034971517603890/J0FR_3d_render_a_shirt_e018df48-e9c8-41c2-bb2f-3f4d96582daa.png','Prenda #2',1,10,1,1,'https://www.zara.com/co/es/vestido-halter-cut-out-con-lino-p07707939.html?v1=272729065&v2=2184058',2);    
INSERT INTO PRENDA_ENTITY VALUES (3,1,0,'https://cdn.discordapp.com/attachments/1071941314809172089/1108034971517603890/J0FR_3d_render_a_shirt_e018df48-e9c8-41c2-bb2f-3f4d96582daa.png','Prenda #3',1,10,1,1,'https://www.zara.com/co/es/vestido-halter-cut-out-con-lino-p07707939.html?v1=272729065&v2=2184058',3);    

INSERT INTO PRENDA_ENTITY_OUTFITS VALUES(1, 1);
INSERT INTO PRENDA_ENTITY_OUTFITS VALUES(2, 2);
INSERT INTO PRENDA_ENTITY_OUTFITS VALUES(3, 3);

INSERT INTO UBICACION_ENTITY VALUES(1, 1.1, 1.1);
INSERT INTO UBICACION_ENTITY VALUES(2, 2.2, -2.2);
INSERT INTO UBICACION_ENTITY VALUES(3, -3.3, 3.3);
INSERT INTO UBICACION_ENTITY VALUES(4, -4.4, -4.4);

INSERT INTO TIENDA_FISICA_ENTITY VALUES(1, 'Tienda 1', 'Direccion 1', 1, 1);
INSERT INTO TIENDA_FISICA_ENTITY VALUES(2, 'Tienda 2', 'Direccion 2', 2, 2);
INSERT INTO TIENDA_FISICA_ENTITY VALUES(3, 'Tienda 3', 'Direccion 3', 3, 3);
INSERT INTO TIENDA_FISICA_ENTITY VALUES(4, 'Tienda 4', 'Direccion 4', 1, 4);

INSERT INTO USUARIO_ENTITY VALUES(1, 60, 'edna.mode@supersecret.com', 'M', 'Edna Mode');
INSERT INTO USUARIO_ENTITY VALUES(2, 45, 'mrincredible@supersecret.com', 'H', 'Bob Parr');
INSERT INTO USUARIO_ENTITY VALUES(3, 40, 'elasti_girl@supermail.us', 'M', 'Helen Parr');

INSERT INTO COMENTARIO_ENTITY VALUES(1, 4, 'I never look back, darling. It distracts from the now.', 'Ednas pep talk', 1, 2, null);
INSERT INTO COMENTARIO_ENTITY VALUES(2, 5, 'The best for the best', 'Bomb voyage!', 2, 3, null);
INSERT INTO COMENTARIO_ENTITY VALUES(3, 1, 'Meta-Man. Express elevator. Dynaguy, snag on takeoff. Splashdown, sucked into a vortex.', 'No capes!', 1, null, 3);
