const jwt = require('jsonwebtoken');

const protect = (req, res, next) => {
  // Get the token from the request header
  // The standard format is "Bearer <token>"
  const authHeader = req.header('Authorization');

  // Check if there's no token
  if (!authHeader) {
    return res.status(401).json({ message: 'No token, authorization denied.' });
  }

  // Check if the header format is correct
  if (!authHeader.startsWith('Bearer ')) {
    return res.status(401).json({ message: 'Token format is incorrect, must be "Bearer <token>".' });
  }

  const token = authHeader.split(' ')[1];

  // Verify the token
  try {
    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    // Attach the user's payload to the request object
    req.user = decoded.user;
    // Move on to the next function (the actual route controller)
    next();
  } catch (err) {
    res.status(401).json({ message: 'Token is not valid.' });
  }
};

const isAdmin = (req, res, next) => {
  // This middleware should run AFTER the 'protect' middleware,
  // so req.user will be available.
  if (req.user && req.user.role === 'admin') {
    next(); // User is an admin, proceed to the next function
  } else {
    res.status(403).json({ message: 'Forbidden. Admin access required.' });
  }
};

module.exports = { protect, isAdmin };