CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE campanas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    dungeon_master_id INT NOT NULL,
    CONSTRAINT fk_campana_usuario
        FOREIGN KEY (dungeon_master_id) REFERENCES usuarios(id)
            ON DELETE CASCADE
);
