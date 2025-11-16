-- This file contains all the mock data that will be used. 
USE ridehailing;

-- Customer Data Table
INSERT INTO customer(customer_id, customer_name, gender, age, occupation, address, phone_number, payment_details, average_rating, current_transaction) VALUES
(1, 'John Doe', 'Male', 28, 'Computer Scientist', 'Brgy. 105 Vitas, Las Piñas, Metro Manila', '09958554011', 'Cash', 4.5, NULL),
(2, 'Juan Dela Cruz', 'Male', 24, 'Student', 'Brgy. 715, Malate, Manila, NCR', '09174567890', 'GCash', 4.6, NULL),
(3, 'Maria Santos', 'Female', 31, 'Nurse', 'Brgy. Little Baguio, San Juan City, NCR', '09223456781','Credit Card', 4.9, NULL),
(4, 'Ramon Reyes', 'Male', 43, 'Family Driver', 'Brgy. Bangkal, Makati City, NCR', '09052345678', 'Cash', 4.1, NULL),
(5, 'Aira Villanueva', 'Female', 28, 'Teacher', 'Brgy. Buhangin, Mandaluyong City, NCR', '09368123456', 'Maya', 4.3, NULL);

-- Driver Data Table
INSERT INTO driver(driver_id, license_num, driver_name, gender, age, address, phone_number, date_of_employment, date_of_resignation, total_income, average_rating, current_transaction) VALUES
(1, 'N03-12,123456', 'Alexander Mejia', 'Male', 19, '24A J. Ruiz St. Brg. Salapan, San Juan City', '09190000123', '2025-11-11', NULL, 42000.00, 4.7, NULL),
(2, 'N12-34-567890', 'Mark Angelo Cruz', 'Male', 34, 'Brgy. San Antonio, Pasig City, NCR', '09175557890', '2023-02-15', NULL,385000.00, 4.7, NULL),
(3, 'P98-76-543210', 'Liza Mae Navarro', 'Female', 29, 'Brgy. Poblacion, Makati City, NCR', '09612345678', '2024-05-10', NULL, 212500.00, 4.5, NULL), 
(4, 'R55-21-334455', 'Jerome D. Alcantara', 'Male', 41,'Brgy. Dolores, Taytay, Rizal', '09083456789', '2021-11-01', '2025-07-31', 640000.00, 4.2, NULL), 
(5, 'Q33-44-556677', 'Sheila Ann Dela Pena', 'Female', 37, 'Brgy. Dagat, Valenzuela City, NCR', '09771234567', '2022-08-20', NULL, 498300.00, 4.8, NULL);

-- Vechicle Data Table 
INSERT INTO vehicle(vehicle_id, driver_id, registration_num, plate_num, car_brand, model_name, color, service_record, fuel_type, year_acquired) VALUES
(1, 1, 'REG-2023-001', 'NBK3149', 'Dodge', 'Challenger', 'Black', 'Regular maintenance up to date', 'Gasoline', 2024),
(2, 2, 'REG-2023-002', 'NDX4521', 'Toyota', 'Vios 1.3 XE', 'Silver', '10-km PMS at Toyota Manila Bay (2024-05-12)', 'Gasoline', 2022),
(3, 3, 'REG-2024-003', 'NAH3186', 'Honda', 'City 1.5 S', 'Crystal Black', 'Brake pads replaced (2025-01-18)', 'Gasoline', 2023),
(4, 4, 'REG-2022-004', 'NBM7724', 'Mitsubishi', 'Xpander GLS', 'Titanium Gray', '40k-km service (2025-03-08)','Gasoline', 2021),
(5, 5, 'REG-2023-005', 'NCP6409', 'Hyundai', 'Accent 1.6 CRDi', 'Polar White', 'Fuel filter change (2024-11-02)', 'Diesel', 2022);

-- Transactions Data Table 
INSERT INTO transactions (transaction_id, customer_id, driver_id, vehicle_id, date, time, pickup_point, dropoff_point, balance, cost, fulfillment_status) VALUES
(1, 1, 1, 1, '2024-12-12', '07:42:00', 'Universidad ng Pilipinas', 'De La Salle University', 560, 242.00, 'Completed'),
(2, 2, 2, 2, '2025-11-05', '08:15:00', 'DLSU Taft Gate, Malate, Manila', 'SM Mall of Asia, Pasay', 800, 165.00, 'Completed'),
(3, 3, 3, 3, '2025-11-06', '18:42:00', 'Ayala Malls Manila Bay, Pasay City', 'Solaire Manila', 707, 120.00, 'Completed'),
(4, 4, 4, 4, '2025-11-07', '22:05:00', 'Glorietta 4, Makati', 'BGC High Street, Taguig', 660, 220.00, 'Completed'),
(5, 5, 5, 5, '2025-11-08', '07:30:00', 'Robinsons Manila', 'SM City Manila',580.00, 140.00, 'Completed');  




