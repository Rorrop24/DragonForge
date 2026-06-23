CREATE TABLE diarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_campana VARCHAR(150) NOT NULL,
    dm_asignado VARCHAR(100) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE entradas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_evento VARCHAR(50) NOT NULL,
    descripcion TEXT NOT NULL,
    fecha_evento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    diario_id INT NOT NULL,
    CONSTRAINT fk_entrada_diario
        FOREIGN KEY (diario_id) REFERENCES diarios(id)
            ON DELETE CASCADE
);