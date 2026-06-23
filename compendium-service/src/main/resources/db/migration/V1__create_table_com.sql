CREATE TABLE categorias_compendio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);
CREATE TABLE entradas_compendio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    descripcion_detallada TEXT NOT NULL,
    estadisticas VARCHAR(255),
    categoria_id INT NOT NULL,
    CONSTRAINT fk_entrada_categoria
        FOREIGN KEY (categoria_id) REFERENCES categorias_compendio(id)
            ON DELETE CASCADE
);
