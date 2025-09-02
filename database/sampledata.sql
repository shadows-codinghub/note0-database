USE notes_app; 
INSERT INTO notes (title, content) 
VALUES 
('First Note', 'This is a sample note for testing.'),
('Second Note', 'Another test note with more content.');
INSERT INTO files (filename, filepath) 
VALUES 
('example.pdf', 'uploads/example.pdf'),
('image.jpeg', 'uploads/image.jpeg');
