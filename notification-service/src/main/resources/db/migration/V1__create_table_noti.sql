CREATE TABLE buzones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL UNIQUE,
    nombre_jugador VARCHAR(100) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE notificaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    mensaje VARCHAR(255) NOT NULL,
    leida BOOLEAN DEFAULT FALSE,
    fecha_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    buzon_id INT NOT NULL,
    CONSTRAINT fk_notificacion_buzon
        FOREIGN KEY (buzon_id) REFERENCES buzones(id)
            ON DELETE CASCADE
);