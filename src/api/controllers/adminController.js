const pool = require('../../config/db');

// --- Get all users ---
const getAllUsers = async (req, res) => {
  try {
    // Fetch all users, excluding their passwords for security
    const allUsers = await pool.query(
      'SELECT id, full_name, email, role, college_name, branch, semester, created_at FROM users ORDER BY created_at DESC'
    );
    res.status(200).json(allUsers.rows);
  } catch (error) {
    console.error('Get All Users Error:', error.stack);
    res.status(500).json({ message: 'Server error while fetching users.' });
  }
};

// --- Verify a user ---
const verifyUser = async (req, res) => {
  const { id } = req.params; // The ID of the user to verify

  try {
    const result = await pool.query(
      "UPDATE users SET role = 'verified' WHERE id = $1 RETURNING id, full_name, email, role",
      [id]
    );

    if (result.rows.length === 0) {
      return res.status(404).json({ message: 'User not found.' });
    }

    res.status(200).json({
      message: 'User has been successfully verified.',
      user: result.rows[0]
    });

  } catch (error) {
    console.error('Verify User Error:', error.stack);
    res.status(500).json({ message: 'Server error while verifying user.' });
  }
};

module.exports = {
  getAllUsers,
  verifyUser,
};