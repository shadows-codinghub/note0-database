const express = require('express');
const router = express.Router();
const adminController = require('../controllers/adminController');
const { protect, isAdmin } = require('../middleware/authMiddleware');

// All routes in this file are protected and require admin access
router.use(protect, isAdmin);

// @route   GET /api/admin/users
// @desc    Get a list of all users
// @access  Admin
router.get('/users', adminController.getAllUsers);

// @route   PUT /api/admin/users/:id/verify
// @desc    Verify a user, changing their role to 'verified'
// @access  Admin
router.put('/users/:id/verify', adminController.verifyUser);

module.exports = router;