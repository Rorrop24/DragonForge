CREATE TABLE historial_tiradas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    caras_dado INT NOT NULL,
    cantidad_dados INT NOT NULL,
    modificador INT NOT NULL,
    resultado_total INT NOT NULL,
    fecha_tirada TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);