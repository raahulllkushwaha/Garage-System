CREATE DATABASE garage;
USE garage;

CREATE TABLE customer (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE vehicle (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    customer_id INT NOT NULL,
    number_plate VARCHAR(20) NOT NULL UNIQUE,
    model VARCHAR(50) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE
);

CREATE TABLE services (
    id INT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(100) NOT NULL,
    cost DECIMAL(10,2) NOT NULL
);

INSERT INTO services (description, cost) VALUES
('Oil Change', 1500.00),
('Engine Repair', 5000.00),
('Tyre Replacement', 2500.00),
('Car Washing', 500.00),
('Brake Service', 2000.00),
('AC Service', 1800.00);

CREATE TABLE invoices (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    customer_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    service_ids TEXT NOT NULL, -- Store comma-separated service IDs
    total_amount DECIMAL(10,2) NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE,
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(id) ON DELETE CASCADE
);


INSERT INTO customer (name, phone) VALUES ('John Doe', '9876543210');

INSERT INTO vehicle (customer_id, number_plate, model) VALUES (1, 'MP15UN9645', 'Honda City 2022');