const multer = require('multer');
const path = require('path');

// Set up storage engine
const storage = multer.diskStorage({
  destination: './uploads/', // The folder to save files in
  filename: function(req, file, cb) {
    // Create a unique filename to avoid overwriting files
    // Format: fieldname-timestamp.extension
    cb(null, `${file.fieldname}-${Date.now()}${path.extname(file.originalname)}`);
  }
});

// Initialize upload variable
const upload = multer({
  storage: storage,
  limits: { fileSize: 10000000 }, // Limit file size to 10MB
  fileFilter: function(req, file, cb) {
    checkFileType(file, cb);
  }
}).single('noteFile'); // This expects the file to be sent in a form field named 'noteFile'

// Check File Type function
function checkFileType(file, cb) {
  // Allowed extensions
  const filetypes = /jpeg|jpg|png|gif|pdf|doc|docx|ppt|pptx/;
  // Check extension
  const extname = filetypes.test(path.extname(file.originalname).toLowerCase());
  // Check mime type
  const mimetype = filetypes.test(file.mimetype);

  if (mimetype && extname) {
    return cb(null, true);
  } else {
    cb('Error: Only images and documents are allowed!');
  }
}

module.exports = upload;