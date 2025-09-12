-- Insert sample subjects
INSERT INTO subjects (id, name, description) VALUES 
('550e8400-e29b-41d4-a716-446655440001', 'Data Structures', 'Fundamental data structures and algorithms'),
('550e8400-e29b-41d4-a716-446655440002', 'Algorithms', 'Algorithm design and analysis'),
('550e8400-e29b-41d4-a716-446655440003', 'Database Systems', 'Database design and management'),
('550e8400-e29b-41d4-a716-446655440004', 'Computer Networks', 'Network protocols and communication'),
('550e8400-e29b-41d4-a716-446655440005', 'Operating Systems', 'OS concepts and implementation')
ON CONFLICT (id) DO NOTHING;

-- Insert sample admin user (password: admin123)
INSERT INTO users (id, full_name, email, password_hash, college_name, branch, semester, role) VALUES 
('550e8400-e29b-41d4-a716-446655440000', 'Admin User', 'admin@note0.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'KTU Main Campus', 'Computer Science', 8, 'ADMIN')
ON CONFLICT (id) DO NOTHING;
