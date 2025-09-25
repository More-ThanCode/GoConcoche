--roles
INSERT INTO roles (role) VALUES ('ADMIN');
INSERT INTO roles (role) VALUES ('RENTER');
INSERT INTO roles (role) VALUES ('OWNER');

--registered_users
INSERT INTO registered_users (first_name, last_name, date_of_birth, phone_number, username, email, password) VALUES ('Adam', 'Han', '1999-09-09', '+34111111111', 'admin', 'admin@gmail.com', '$2a$10$OQOEjXFqmFTpX2WtgWqi..myO2nS1BMC0jaFm52ydH3gR.XbWLQVe');

INSERT INTO registered_users (first_name, last_name, date_of_birth, phone_number, username, email, password) VALUES ('Ren', 'Raw', '1989-09-09', '+34111111112', 'renter', 'renter@gmail.com', '$2a$10$OQOEjXFqmFTpX2WtgWqi..myO2nS1BMC0jaFm52ydH3gR.XbWLQVe');

INSERT INTO registered_users (first_name, last_name, date_of_birth, phone_number, username, email, password) VALUES ('Owen', 'Tak', '1979-09-09', '+34111111113', 'owner', 'owner@gmail.com', '$2a$10$OQOEjXFqmFTpX2WtgWqi..myO2nS1BMC0jaFm52ydH3gR.XbWLQVe');

--user_role
INSERT INTO user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO user_role (user_id, role_id) VALUES (2, 2);
INSERT INTO user_role (user_id, role_id) VALUES (3, 3);

--owner_profiles
INSERT INTO owner_profiles (registered_user_id, image_url) VALUES (3, 'https://somexample.com/images/prof.jpg');

--renter_profiles
INSERT INTO renter_profiles (registered_user_id, type_license, licence_number, expired_date, image_url) VALUES (2, 'B', 'LIC123456', '2026-12-31', 'https://somexample.com/images/anna.jpg');

--locations
INSERT INTO locations (city, district) VALUES ('Valencia', 'El Carmen');
INSERT INTO locations (city, district) VALUES ('Valencia', 'Eixample');

--vehicles
INSERT INTO vehicles (vin, plate_number, insurance_number, model, brand, year, color, seater, child_seats_number, fuel_type_car, fuel_consumption, image_url, owner_id) VALUES ('WVWZZZ1JZXW000001','AA11111BB', 'INS-987654321', 'Golf', 'Volkswagen', 2020, 'Black', 'SEDAN', 1,'PETROL', '7.5L/100km','https://someexample.com/images/car.jpg', 1);