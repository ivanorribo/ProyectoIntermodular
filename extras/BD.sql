CREATE DATABASE IF NOT EXISTS hidragri_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hidragri_db;

CREATE TABLE USUARIOS (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN', 'AGRICULTOR') NOT NULL
) ENGINE=InnoDB;

CREATE TABLE FINCAS (
    id_finca INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(255),
    ref_catastral VARCHAR(50) UNIQUE,
    hectareas DECIMAL(10,2),
    estado_activa TINYINT DEFAULT 1,
    CONSTRAINT fk_fincas_usuario FOREIGN KEY (id_usuario) 
        REFERENCES USUARIOS(id_usuario) ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE PARCELAS (
    id_parcela INT AUTO_INCREMENT PRIMARY KEY,
    id_finca INT NOT NULL,
    codigo_sector VARCHAR(50) NOT NULL,
    tipo_suelo VARCHAR(100),
    CONSTRAINT fk_parcelas_finca FOREIGN KEY (id_finca) 
        REFERENCES FINCAS(id_finca) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE CULTIVOS (
    id_cultivo INT AUTO_INCREMENT PRIMARY KEY,
    id_parcela INT NOT NULL,
    especie VARCHAR(100) NOT NULL,
    fecha_siembra DATE NOT NULL,
    fase_actual ENUM('SIEMBRA', 'CRECIMIENTO', 'COSECHA') NOT NULL,
    densidad_plantas INT,
    CONSTRAINT fk_cultivos_parcela FOREIGN KEY (id_parcela) 
        REFERENCES PARCELAS(id_parcela) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE RIEGOS_CONSUMOS (
    id_registro INT AUTO_INCREMENT PRIMARY KEY,
    id_parcela INT NOT NULL,
    fecha_riego DATETIME NOT NULL,
    metros_cubicos DECIMAL(10,2) NOT NULL,
    fuente_agua ENUM('AGUA_REGENERADA', 'POZO', 'GALERIA', 'RED_PUBLICA') NOT NULL,
    CONSTRAINT fk_riegos_parcela FOREIGN KEY (id_parcela) 
        REFERENCES PARCELAS(id_parcela) ON DELETE CASCADE
) ENGINE=InnoDB;