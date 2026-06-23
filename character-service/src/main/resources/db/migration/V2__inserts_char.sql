INSERT INTO personajes
(nombre_personaje, raza, clase, nivel, puntos_golpe, trasfondo, vivo)
VALUES
    ('León', 'Humano', 'Hechicero', 20, 350,
     'Portador del fuego de destrucción y creación. Sobreviviente de una tragedia olvidada.', TRUE),
    ('Mira', 'Elfa', 'Exploradora', 5, 42,
     'Arquera viajera vinculada a las primeras campanas de DragonForge.', TRUE)
;
INSERT INTO equipamientos
(nombre, tipo, cantidad, personaje_id)
VALUES
    ('Grillete del Sol Caído', 'Arma', 1, 1),
    ('Carbon', 'Consumible', 1, 1)
;
