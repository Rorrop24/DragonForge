CREATE TABLE mapas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    region VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);
CREATE TABLE ubicaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    nivel_peligro INT NOT NULL,
    mapa_id INT NOT NULL,
    CONSTRAINT fk_ubicacion_mapa
        FOREIGN KEY (mapa_id) REFERENCES mapas(id)
            ON DELETE CASCADE
);