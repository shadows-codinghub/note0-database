## Database Setup

This project uses **MySQL/MariaDB** to store notes and file metadata.

### Quick Start
Run the following commands to set up the database:

```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/sample_data.sql
