DELETE FROM user_role;
DELETE FROM owner_profiles;
DELETE FROM renter_profiles;
DELETE FROM locations;
DELETE FROM registered_users;
DELETE FROM roles;

-- roles
INSERT INTO roles (id, role) VALUES (1, 'ADMIN');
INSERT INTO roles (id, role) VALUES (2, 'RENTER');
INSERT INTO roles (id, role) VALUES (3, 'OWNER');

-- registered_users
INSERT INTO registered_users (id, first_name, last_name, date_of_birth, phone_number, username, email, password) 
VALUES (1, 'Adam', 'Han', '1999-09-09', '+34111111111', 'admin', 'admin@gmail.com', '$2a$10$OQOEjXFqmFTpX2WtgWqi..myO2nS1BMC0jaFm52ydH3gR.XbWLQVe');

INSERT INTO registered_users (id, first_name, last_name, date_of_birth, phone_number, username, email, password) 
VALUES (2, 'Ren', 'Raw', '1989-09-09', '+34111111112', 'renter', 'renter@gmail.com', '$2a$10$OQOEjXFqmFTpX2WtgWqi..myO2nS1BMC0jaFm52ydH3gR.XbWLQVe');

INSERT INTO registered_users (id, first_name, last_name, date_of_birth, phone_number, username, email, password) 
VALUES (3, 'Owen', 'Tak', '1979-09-09', '+34111111113', 'owner', 'owner@gmail.com', '$2a$10$OQOEjXFqmFTpX2WtgWqi..myO2nS1BMC0jaFm52ydH3gR.XbWLQVe');

-- user_role
INSERT INTO user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO user_role (user_id, role_id) VALUES (2, 2);
INSERT INTO user_role (user_id, role_id) VALUES (3, 3);

-- owner_profiles
INSERT INTO owner_profiles (registered_user_id, image_url) 
VALUES (3, 'https://somexample.com/images/prof.jpg');

-- renter_profiles
INSERT INTO renter_profiles (registered_user_id, type_license, licence_number, expired_date, image_url) 
VALUES (2, 'B', 'LIC123456', '2026-12-31', 'https://somexample.com/images/anna.jpg');

-- locations
--INSERT INTO locations (city, district) VALUES ('Valencia', 'El Carmen')
