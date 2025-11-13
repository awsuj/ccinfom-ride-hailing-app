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
    average_rating DOUBLE
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
    average_rating DOUBLE
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

-- Create Bookings table
CREATE TABLE booking (
	booking_id INT(11) PRIMARY KEY,
    booking_date DATE,
    booking_time TIME,
    customer_id INT(11),
    customer_name VARCHAR(45),
    driver_id INT(11),
    driver_name VARCHAR(45),
    plate_num VARCHAR(7),
    pickup_point VARCHAR(100),
    dropoff_point VARCHAR(100),
    duration DOUBLE,
    payment_status VARCHAR(20),
    payment_mode VARCHAR(20),
    driver_rating DOUBLE,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
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
    payment_method VARCHAR(20),
    cost DECIMAL(10, 2),
    fulfillment_status VARCHAR(50),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY (driver_id) REFERENCES driver(driver_id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id)
);
    



