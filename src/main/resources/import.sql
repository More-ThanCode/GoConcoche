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
INSERT INTO renter_profiles (registered_user_id, type_license, license_number, expired_date, image_url) VALUES (2, 'B', 'LIC123456', '2026-12-31', 'https://somexample.com/images/anna.jpg');

--locations
INSERT INTO locations (city, district, address) VALUES ('Valencia', 'El Carmen', 'Carrer Guillem de Castro, 4');
INSERT INTO locations (city, district, address) VALUES ('Valencia', 'Eixample', 'Carrer Ruzafa, 24');
INSERT INTO locations (city, district, address) VALUES ('Barcelona', 'Ciutat Vella', 'Carrer dels Escudellers, 33');
INSERT INTO locations (city, district, address) VALUES ('Barcelona', 'Les Corts', 'Carrer de Bordeus, 35');

--vehicles
INSERT INTO vehicles (vin, plate_number, insurance_number, model, brand, year, color, seater, child_seats_number, fuel_type_car, fuel_consumption, image_url, owner_id) VALUES ('WVWZZZ1JZXW000001','AA11111BB', 'INS-987654321', 'Golf', 'Volkswagen', 2021, 'Black', 'SEDAN', 1,'PETROL', '7.5L/100km','/images/default-car.jpg', 1);
INSERT INTO vehicles (vin, plate_number, insurance_number, model, brand, year, color, seater, child_seats_number, fuel_type_car, fuel_consumption, image_url, owner_id) VALUES ('WVWZZZ1JZXW000002','AA11112BB', 'INS-987654323', 'Coup', 'Smart', 2020, 'White', 'SMART', 1,'ELECTRIC', '4 kWh/100 km','/images/default-car.jpg', 1);

--rental offers
INSERT INTO vehicle_rental_offers (vehicle_id, location_id, start_date_time, end_date_time, available, price_hour, owner_id) VALUES (1, 1, '2025-10-13 16:00:00', '2025-10-14 16:00:00', 1, 10.0, 1);
INSERT INTO vehicle_rental_offers (vehicle_id, location_id, start_date_time, end_date_time, available, price_hour, owner_id) VALUES (2, 2, '2025-10-13 16:00:00', '2025-10-14 16:00:00', 1, 10.0, 1);
INSERT INTO vehicle_rental_offers (vehicle_id, location_id, start_date_time, end_date_time, available, price_hour, owner_id) VALUES (1, 3, '2025-10-15 16:00:00', '2025-10-16 16:00:00', 1, 10.0, 1);
INSERT INTO vehicle_rental_offers (vehicle_id, location_id, start_date_time, end_date_time, available, price_hour, owner_id) VALUES (2, 4, '2025-10-15 16:00:00', '2025-10-16 16:00:00', 1, 10.0, 1);


--offer slots
INSERT INTO rental_offer_slots (offer_id, slot_start, slot_end, available) VALUES (1, '2025-10-13 16:00:00', '2025-10-14 00:00:00', 1);
INSERT INTO rental_offer_slots (offer_id, slot_start, slot_end, available) VALUES (1, '2025-10-14 00:00:00', '2025-10-14 08:00:00', 1);
INSERT INTO rental_offer_slots (offer_id, slot_start, slot_end, available) VALUES (1, '2025-10-14 08:00:00', '2025-10-14 16:00:00', 1);
INSERT INTO rental_offer_slots (offer_id, slot_start, slot_end, available) VALUES (2, '2025-10-13 16:00:00', '2025-10-14 00:00:00', 1);
INSERT INTO rental_offer_slots (offer_id, slot_start, slot_end, available) VALUES (2, '2025-10-14 00:00:00', '2025-10-14 08:00:00', 1);
INSERT INTO rental_offer_slots (offer_id, slot_start, slot_end, available) VALUES (2, '2025-10-14 08:00:00', '2025-10-14 16:00:00', 1);
INSERT INTO rental_offer_slots (offer_id, slot_start, slot_end, available) VALUES (3, '2025-10-15 16:00:00', '2025-10-16 00:00:00', 1);
INSERT INTO rental_offer_slots (offer_id, slot_start, slot_end, available) VALUES (3, '2025-10-16 00:00:00', '2025-10-16 08:00:00', 1);
INSERT INTO rental_offer_slots (offer_id, slot_start, slot_end, available) VALUES (3, '2025-10-16 08:00:00', '2025-10-16 16:00:00', 1);
INSERT INTO rental_offer_slots (offer_id, slot_start, slot_end, available) VALUES (4, '2025-10-15 16:00:00', '2025-10-16 00:00:00', 1);
INSERT INTO rental_offer_slots (offer_id, slot_start, slot_end, available) VALUES (4, '2025-10-16 00:00:00', '2025-10-16 08:00:00', 1);
INSERT INTO rental_offer_slots (offer_id, slot_start, slot_end, available) VALUES (4, '2025-10-16 08:00:00', '2025-10-16 16:00:00', 1);