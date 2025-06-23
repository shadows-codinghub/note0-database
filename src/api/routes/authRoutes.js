const express = require('express');
const router = express.Router();
const authController = require('../controllers/authController');

// Route for user registration
// POST /api/auth/register
router.post('/register', authController.registerUser);

// Route for user login
// POST /api/auth/login
router.post('/login', authController.loginUser);

module.exports = router;