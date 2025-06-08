const pool = require('../../config/db'); // Adjust path to go up two levels
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

const registerUser = async (req, res) => {
  // Destructure the expected data from the request body
  const { fullName, email, password, collegeName, branch, semester } = req.body;

  // --- Basic Validation ---
  if (!fullName || !email || !password) {
    return res.status(400).json({ message: 'Full name, email, and password are required.' });
  }

  try {
    // --- Check if user already exists ---
    const userExists = await pool.query('SELECT * FROM users WHERE email = $1', [email]);
    if (userExists.rows.length > 0) {
      return res.status(409).json({ message: 'User with this email already exists.' });
    }

    // --- Hash the password ---
    const salt = await bcrypt.genSalt(10);
    const passwordHash = await bcrypt.hash(password, salt);

    // --- Insert new user into the database ---
    const newUser = await pool.query(
      'INSERT INTO users (full_name, email, password_hash, college_name, branch, semester) VALUES ($1, $2, $3, $4, $5, $6) RETURNING id, email, full_name, role',
      [fullName, email, passwordHash, collegeName, branch, semester]
    );

    // --- Send a success response ---
    res.status(201).json({
      message: 'User registered successfully!',
      user: newUser.rows[0]
    });

  } catch (error) {
    console.error('Registration Error:', error.stack);
    res.status(500).json({ message: 'Server error during registration.' });
  }
};

// ... after the registerUser function ...

const loginUser = async (req, res) => {
  const { email, password } = req.body;

  // --- Basic Validation ---
  if (!email || !password) {
    return res.status(400).json({ message: 'Email and password are required.' });
  }

  try {
    // --- Find the user by email ---
    const userResult = await pool.query('SELECT * FROM users WHERE email = $1', [email]);
    if (userResult.rows.length === 0) {
      // Use a generic message to avoid revealing which emails are registered
      return res.status(401).json({ message: 'Invalid credentials.' });
    }

    const user = userResult.rows[0];

    // --- Compare the provided password with the stored hash ---
    const isMatch = await bcrypt.compare(password, user.password_hash);
    if (!isMatch) {
      return res.status(401).json({ message: 'Invalid credentials.' });
    }

    // --- User is valid, create a JWT ---
    const payload = {
      user: {
        id: user.id,
        role: user.role
      }
    };

    jwt.sign(
      payload,
      process.env.JWT_SECRET,
      { expiresIn: '7d' }, // Token expires in 7 days
      (err, token) => {
        if (err) throw err;
        res.status(200).json({
          message: 'Login successful!',
          token: token
        });
      }
    );

  } catch (error) {
    console.error('Login Error:', error.stack);
    res.status(500).json({ message: 'Server error during login.' });
  }
};

module.exports = {
  registerUser,
  loginUser,
};