-- Create Customer Table
CREATE TABLE customer (
	customer_id INT(11) PRIMARY KEY,
    customer_name VARCHAR(45),
    gender VARCHAR(10),
    age INT(2),
    occupation VARCHAR(45),
    address VARCHAR(100),
    phone_number BIGINT,
    payment_details VARCHAR(20),
    average_rating DOUBLE,
    current_transaction INT(11) -- Added this one, HOWEVER - it is better to not add the foreign key yet to avoid circular referencing.
);

-- Create Driver Table
CREATE TABLE driver (
	driver_id INT(11) PRIMARY KEY,
    license_num VARCHAR(13),
    driver_name VARCHAR(45),
    gender VARCHAR(10),
    age INT(2),
    address VARCHAR(100),
    phone_number BIGINT,
    date_of_employment DATE,
    date_of_resignation DATE,
    total_income DOUBLE,
    average_rating DOUBLE,
    current_transaction INT(11) -- Added this one, HOWEVER - it is better to not add the foreign key yet to avoid circular referencing.
);

-- Create Vehicle Table
CREATE TABLE vehicle (
	vehicle_id INT(11) PRIMARY KEY,
    driver_id INT(11),
    registration_num VARCHAR(50),
    plate_num VARCHAR(7),
    car_brand VARCHAR(50),
    model_name VARCHAR(50),
    color VARCHAR(30),
    number_of_seats INT,
    service_record TEXT,
    fuel_type VARCHAR(20),
    year_acquired YEAR,
    FOREIGN KEY (driver_id) REFERENCES driver(driver_id)
);
    
-- Create Transactions Table
CREATE TABLE transactions (
	transaction_id INT(11) PRIMARY KEY,
    customer_id INT(11),
    driver_id INT(11),
    vehicle_id INT(11),
    date DATE, 	
    time TIME,
    pickup_point VARCHAR(100),
    dropoff_point VARCHAR(100),
    balance DOUBLE, -- CHANGED
    cost DECIMAL(10, 2),
    fulfillment_status VARCHAR(50),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY (driver_id) REFERENCES driver(driver_id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id)
);

-- NEW: References the "current_transaction" to the transactions_id. This avoids circular reasoning. 
ALTER TABLE driver ADD FOREIGN KEY (current_transaction) REFERENCES transactions (transaction_id);
ALTER TABLE customer ADD FOREIGN KEY (current_transaction) REFERENCES transactions (transaction_id);


