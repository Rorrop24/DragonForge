CREATE TABLE carpetas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);
CREATE TABLE archivos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_archivo VARCHAR(150) NOT NULL,
    url_recurso VARCHAR(255) NOT NULL,
    tipo_mime VARCHAR(50) NOT NULL,
    peso_kb DOUBLE NOT NULL,
    carpeta_id INT NOT NULL,
    CONSTRAINT fk_archivo_carpeta
    FOREIGN KEY (carpeta_id) REFERENCES carpetas(id)
        ON DELETE CASCADE
);