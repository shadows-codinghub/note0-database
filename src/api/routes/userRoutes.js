const express = require('express');
const router = express.Router();
const userController = require('../controllers/userController');
const { protect } = require('../middleware/authMiddleware');

// @route   GET /api/users/me
// @desc    Get current logged-in user's profile
// @access  Private (because we use the 'protect' middleware)
router.get('/me', protect, userController.getUserProfile);

module.exports = router;