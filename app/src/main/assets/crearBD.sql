CREATE TABLE Usuarios (id INTEGER PRIMARY KEY, nombreUsuario TEXT, idDeporte INTEGER);
INSERT INTO Usuarios (nombreUsuario, idDeporte) VALUES('Tizon', 1);
INSERT INTO Usuarios (nombreUsuario, idDeporte) VALUES('vrgx', 2);

CREATE TABLE Eventos (id INTEGER PRIMARY KEY, fechaHora DATETIME, idZona INTEGER, idDeporte INTEGER, idOrganizador INTEGER, fechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP);
INSERT INTO Eventos(fechaHora, idZona, idDeporte, idOrganizador, fechaCreacion) VALUES('2015-03-10 11:30:00', 1, 1, 1,'2015-03-09 21:33:18');
INSERT INTO Eventos(fechaHora, idZona, idDeporte, idOrganizador, fechaCreacion) VALUES('2015-03-09 16:30:00', 3, 2, 2,'2015-03-08 19:12:14');

CREATE TABLE InscritosEvento (idEvento INTEGER, idInscrito INTEGER, fechaInscripcion DATETIME DEFAULT CURRENT_TIMESTAMP);
INSERT INTO InscritosEvento(idEvento, idInscrito, fechaInscripcion) VALUES(1,1,'2015-03-09 21:33:22');
INSERT INTO InscritosEvento(idEvento, idInscrito, fechaInscripcion) VALUES(1,2,'2015-03-10 11:00:00');
INSERT INTO InscritosEvento(idEvento, idInscrito, fechaInscripcion) VALUES(2,2,'2015-03-08 19:12:24');

CREATE TABLE Deportes (id INTEGER PRIMARY KEY, nombre TEXT, nombreArchivoImagen TEXT, descripcion TEXT);
INSERT INTO Deportes (nombre, nombreArchivoImagen, descripcion) VALUES('Basket', 'basket', 'descripción para BASKET guardada en la BD');
INSERT INTO Deportes (nombre, nombreArchivoImagen, descripcion) VALUES('Futbol', 'futbol', 'descripción para FUTBOL guardada en la BD');
INSERT INTO Deportes (nombre, nombreArchivoImagen, descripcion) VALUES('Voleibol', 'voleibol', 'descripción para VOLEIBOL guardada en la BD');

CREATE TABLE Zonas (id INTEGER PRIMARY KEY, nombre TEXT, latlongzoom TEXT, radio INT);
INSERT INTO Zonas (nombre, latlongzoom, radio) VALUES('Hayuelos Lantana', '4.660594:-74.133360:17', 30);
INSERT INTO Zonas (nombre, latlongzoom, radio) VALUES('Uniandes La Caneca', '4.6003125:-74.0635405:19', 30);
INSERT INTO Zonas (nombre, latlongzoom, radio) VALUES('Uniandes Cancha CEDE', '4.600913:-74.0645779:17', 30);