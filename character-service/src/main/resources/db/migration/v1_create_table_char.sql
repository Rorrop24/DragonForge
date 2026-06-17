CREATE TABLE personajes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_personaje VARCHAR(100) NOT NULL,
    raza VARCHAR(50) NOT NULL,
    clase VARCHAR(50) NOT NULL,
    nivel INT NOT NULL DEFAULT 1,
    puntos_golpe INT NOT NULL,
    trasfondo VARCHAR(255),
    vivo BOOLEAN DEFAULT TRUE
);
CREATE TABLE equipamientos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    personaje_id INT NOT NULL,
    CONSTRAINT fk_equipamiento_personaje
        FOREIGN KEY (personaje_id) REFERENCES personajes(id)
            ON DELETE CASCADE
);