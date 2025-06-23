const express = require('express');
const router = express.Router();
const materialController = require('../controllers/materialController');
const { protect } = require('../middleware/authMiddleware');
const upload = require('../middleware/uploadMiddleware'); // Our multer middleware

// @route   POST /api/materials/upload
// @desc    Upload a new academic material
// @access  Private
router.post('/upload', protect, upload, materialController.uploadMaterial);

// @route   GET /api/materials
// @desc    Get a list of all materials
// @access  Public
router.get('/', materialController.getMaterials);

// @route   POST /api/materials/:id/rate
// @desc    Rate a specific material
// @access  Private
router.post('/:id/rate', protect, materialController.rateMaterial);

module.exports = router;