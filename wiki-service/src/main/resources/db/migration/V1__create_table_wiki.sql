CREATE TABLE articulos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL UNIQUE,
    contenido TEXT NOT NULL,
    autor VARCHAR(100) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE comentarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    texto VARCHAR(500) NOT NULL,
    autor_comentario VARCHAR(100) NOT NULL,
    fecha_comentario TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    articulo_id INT NOT NULL,
    CONSTRAINT fk_comentario_articulo
        FOREIGN KEY (articulo_id) REFERENCES articulos(id)
            ON DELETE CASCADE
);