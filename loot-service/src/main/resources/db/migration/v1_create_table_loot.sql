CREATE TABLE categorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);
CREATE TABLE items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    rareza VARCHAR(50) NOT NULL,
    peso DOUBLE NOT NULL,
    valor_oro INT NOT NULL,
    danio_o_efecto VARCHAR(100),
    categoria_id INT NOT NULL,
    CONSTRAINT fk_item_categoria
        FOREIGN KEY (categoria_id) REFERENCES categorias(id)
            ON DELETE CASCADE
);