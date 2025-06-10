const pool = require('../../config/db');
const path = require('path');

// --- Upload a new material ---
const uploadMaterial = async (req, res) => {
  // 'upload' middleware from multer will have already processed the file.
  // If the file is invalid, it will throw an error before reaching this controller.

  if (!req.file) {
    return res.status(400).json({ message: 'No file was uploaded.' });
  }

  // Data from the form fields
  const { title, description, subjectId, moduleNumber } = req.body;
  // User ID from our 'protect' middleware
  const uploaderId = req.user.id;
  // File path from multer
  const filePath = req.file.path;
  // Get the file extension to determine file type
  const fileType = path.extname(req.file.originalname).substring(1);

  if (!title || !subjectId) {
    return res.status(400).json({ message: 'Title and subjectId are required.' });
  }

  try {
    const newMaterial = await pool.query(
      `INSERT INTO materials (title, description, file_type, file_path, module_number, uploader_id, subject_id)
       VALUES ($1, $2, $3, $4, $5, $6, $7)
       RETURNING *`,
      [title, description, fileType, filePath, moduleNumber, uploaderId, subjectId]
    );

    res.status(201).json({
      message: 'File uploaded and record created successfully!',
      material: newMaterial.rows[0]
    });

  } catch (error) {
    console.error('Upload Material Error:', error.stack);
    res.status(500).json({ message: 'Server error while creating material record.' });
  }
};


// --- Get all materials ---
const getMaterials = async (req, res) => {
  try {
    // We can add filtering later (e.g., by subject, semester)
    // For now, let's get all of them and join with subject and user data.
    const allMaterials = await pool.query(
      `SELECT
         m.id, m.title, m.description, m.file_type, m.module_number, m.avg_rating, m.upload_date,
         s.name AS subject_name,
         u.full_name AS uploader_name
       FROM materials m
       JOIN subjects s ON m.subject_id = s.id
       JOIN users u ON m.uploader_id = u.id
       WHERE m.status = 'approved'
       ORDER BY m.upload_date DESC`
    );

    res.status(200).json(allMaterials.rows);

  } catch (error) {
    console.error('Get Materials Error:', error.stack);
    res.status(500).json({ message: 'Server error while fetching materials.' });
  }
};

// --- Rate a specific material ---
const rateMaterial = async (req, res) => {
  const { score } = req.body;
  const materialId = req.params.id;
  const userId = req.user.id;

  // --- Validation ---
  if (!score || score < 1 || score > 5) {
    return res.status(400).json({ message: 'A score between 1 and 5 is required.' });
  }

  try {
    // "UPSERT" logic: INSERT a new rating, but if it already exists (user rated before),
    // UPDATE their existing score instead.
    const newRating = await pool.query(
      `INSERT INTO ratings (material_id, user_id, score)
       VALUES ($1, $2, $3)
       ON CONFLICT (material_id, user_id)
       DO UPDATE SET score = EXCLUDED.score
       RETURNING *`,
      [materialId, userId, score]
    );

    // Our database trigger will automatically handle updating the avg_rating in the materials table.
    res.status(201).json({
      message: 'Thank you for your rating!',
      rating: newRating.rows[0]
    });

  } catch (error) {
    // Check for foreign key violation (e.g., materialId doesn't exist)
    if (error.code === '23503') {
      return res.status(404).json({ message: 'Material not found.' });
    }
    console.error('Rate Material Error:', error.stack);
    res.status(500).json({ message: 'Server error while submitting rating.' });
  }
};


module.exports = {
  uploadMaterial,
  getMaterials,
  rateMaterial,
};