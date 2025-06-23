// Load environment variables from a .env file
require('dotenv').config();
const pool = require('./src/config/db');

const express = require('express');
const app = express();

// Middleware to parse JSON request bodies
app.use(express.json());

// --- API Routes ---
const authRoutes = require('./src/api/routes/authRoutes');
app.use('/api/auth', authRoutes); // All routes in authRoutes will be prefixed with /api/auth

const userRoutes = require('./src/api/routes/userRoutes');
app.use('/api/users', userRoutes); 

const materialRoutes = require('./src/api/routes/materialRoutes'); 
app.use('/api/materials', materialRoutes);

const adminRoutes = require('./src/api/routes/adminRoutes'); 
app.use('/api/admin', adminRoutes);

// Use the PORT from environment variables, or 3000 if it's not defined
const PORT = process.env.PORT || 3000;

// A simple test route to make sure everything is working
app.get('/', (req, res) => {
  res.send('note0 API is running!');
});

app.listen(PORT, () => {
  console.log(`Server is listening on port ${PORT}`);
});