-- Create database
CREATE DATABASE notes_app;

-- Connect to the database
\c notes_app;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    college_name VARCHAR(255),
    branch VARCHAR(255),
    semester INTEGER,
    role VARCHAR(50) NOT NULL DEFAULT 'REGISTERED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create subjects table
CREATE TABLE IF NOT EXISTS subjects (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create materials table
CREATE TABLE IF NOT EXISTS materials (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    file_type VARCHAR(50),
    file_path VARCHAR(500) NOT NULL,
    module_number INTEGER,
    avg_rating DECIMAL(3,2) DEFAULT 0.0,
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    uploader_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    subject_id UUID NOT NULL REFERENCES subjects(id) ON DELETE CASCADE
);

-- Create ratings table
CREATE TABLE IF NOT EXISTS ratings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    score INTEGER NOT NULL CHECK (score >= 1 AND score <= 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    material_id UUID NOT NULL REFERENCES materials(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE(material_id, user_id)
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_materials_status ON materials(status);
CREATE INDEX IF NOT EXISTS idx_materials_subject ON materials(subject_id);
CREATE INDEX IF NOT EXISTS idx_materials_uploader ON materials(uploader_id);
CREATE INDEX IF NOT EXISTS idx_ratings_material ON ratings(material_id);
CREATE INDEX IF NOT EXISTS idx_ratings_user ON ratings(user_id);

-- Create function to update material average rating
CREATE OR REPLACE FUNCTION update_material_avg_rating()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE materials 
    SET avg_rating = (
        SELECT COALESCE(AVG(score), 0)
        FROM ratings 
        WHERE material_id = COALESCE(NEW.material_id, OLD.material_id)
    )
    WHERE id = COALESCE(NEW.material_id, OLD.material_id);
    
    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

-- Create trigger to automatically update average rating
CREATE TRIGGER trigger_update_avg_rating
    AFTER INSERT OR UPDATE OR DELETE ON ratings
    FOR EACH ROW
    EXECUTE FUNCTION update_material_avg_rating();

-- Insert sample data
INSERT INTO subjects (id, name, description) VALUES 
('11111111-1111-1111-1111-111111111111', 'Data Structures', 'Fundamental data structures and algorithms'),
('22222222-2222-2222-2222-222222222222', 'Algorithms', 'Algorithm design and analysis'),
('33333333-3333-3333-3333-333333333333', 'Database Systems', 'Database design and management'),
('44444444-4444-4444-4444-444444444444', 'Computer Networks', 'Network protocols and communication'),
('55555555-5555-5555-5555-555555555555', 'Operating Systems', 'OS concepts and implementation')
ON CONFLICT (id) DO NOTHING;

-- Insert sample admin user (password: admin123)
INSERT INTO users (id, full_name, email, password_hash, college_name, branch, semester, role) VALUES 
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Admin User', 'admin@note0.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'KTU Main Campus', 'Computer Science', 8, 'ADMIN')
ON CONFLICT (id) DO NOTHING;
