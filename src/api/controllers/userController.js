const pool = require('../../config/db');

// This function will only be reached if the 'protect' middleware succeeds
const getUserProfile = async (req, res) => {
  try {
    // The user ID is attached to the request object by our middleware
    const userId = req.user.id;

    // Fetch user data from the database, excluding the password hash
    const userResult = await pool.query(
      'SELECT id, full_name, email, role, college_name, branch, semester, created_at FROM users WHERE id = $1',
      [userId]
    );

    if (userResult.rows.length === 0) {
      return res.status(404).json({ message: 'User not found.' });
    }

    res.status(200).json(userResult.rows[0]);

  } catch (error) {
    console.error('Get User Profile Error:', error.stack);
    res.status(500).json({ message: 'Server error.' });
  }
};

module.exports = {
  getUserProfile,
};