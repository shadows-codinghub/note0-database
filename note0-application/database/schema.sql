-- Create tables with MySQL-compatible syntax
CREATE TABLE IF NOT EXISTS users (
    id CHAR(36) PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    college_name VARCHAR(255),
    branch VARCHAR(255),
    semester INTEGER,
    role VARCHAR(50) NOT NULL DEFAULT 'REGISTERED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS subjects (
    id CHAR(36) PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS materials (
    id CHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    file_type VARCHAR(50),
    file_path VARCHAR(500) NOT NULL,
    module_number INTEGER,
    avg_rating DECIMAL(3,2) DEFAULT 0.0,
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    uploader_id CHAR(36) NOT NULL,
    subject_id CHAR(36) NOT NULL,
    FOREIGN KEY (uploader_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ratings (
    id CHAR(36) PRIMARY KEY,
    score INTEGER NOT NULL CHECK (score >= 1 AND score <= 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    material_id CHAR(36) NOT NULL,
    user_id CHAR(36) NOT NULL,
    FOREIGN KEY (material_id) REFERENCES materials(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE(material_id, user_id)
);

-- Create indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_materials_status ON materials(status);
CREATE INDEX idx_materials_subject ON materials(subject_id);
CREATE INDEX idx_materials_uploader ON materials(uploader_id);
CREATE INDEX idx_ratings_material ON ratings(material_id);
CREATE INDEX idx_ratings_user ON ratings(user_id);

-- Create trigger to update material average rating
DELIMITER //
CREATE TRIGGER update_material_avg_rating_insert AFTER INSERT ON ratings
FOR EACH ROW
BEGIN
    UPDATE materials 
    SET avg_rating = (
        SELECT COALESCE(AVG(score), 0)
        FROM ratings 
        WHERE material_id = NEW.material_id
    )
    WHERE id = NEW.material_id;
END;//

CREATE TRIGGER update_material_avg_rating_update AFTER UPDATE ON ratings
FOR EACH ROW
BEGIN
    UPDATE materials 
    SET avg_rating = (
        SELECT COALESCE(AVG(score), 0)
        FROM ratings 
        WHERE material_id = NEW.material_id
    )
    WHERE id = NEW.material_id;
END;//

CREATE TRIGGER update_material_avg_rating_delete AFTER DELETE ON ratings
FOR EACH ROW
BEGIN
    UPDATE materials 
    SET avg_rating = (
        SELECT COALESCE(AVG(score), 0)
        FROM ratings 
        WHERE material_id = OLD.material_id
    )
    WHERE id = OLD.material_id;
END;//
DELIMITER ;

-- Insert sample data
INSERT INTO subjects (id, name, description) VALUES 
(UUID(), 'Data Structures', 'Fundamental data structures and algorithms'),
(UUID(), 'Algorithms', 'Algorithm design and analysis'),
(UUID(), 'Database Systems', 'Database design and management'),
(UUID(), 'Computer Networks', 'Network protocols and communication'),
(UUID(), 'Operating Systems', 'OS concepts and implementation')
ON DUPLICATE KEY UPDATE name=name;

-- Insert sample admin user (password: admin123)
INSERT INTO users (id, full_name, email, password_hash, college_name, branch, semester, role) VALUES 
(UUID(), 'Admin User', 'admin@note0.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'KTU Main Campus', 'Computer Science', 8, 'ADMIN')
ON DUPLICATE KEY UPDATE email=email;
