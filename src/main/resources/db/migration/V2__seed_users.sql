INSERT INTO users (name, email, password, role, created_date, modified_date, created_by, modified_by)
VALUES (
    'Admin User',
    'admin@gestoria.com',
    '$2a$10$w8P3yZ8L2uK5xXqYtZ0pU.eM8Y3x7VfLh3x7w9K2j5x1Z9Y8x7VfL',
    'ADMIN',
    NOW(),
    NOW(),
    'SYSTEM',
    'SYSTEM'
);