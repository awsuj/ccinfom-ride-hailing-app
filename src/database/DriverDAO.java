package com.src.database;

import java.sql.Driver;
import java.util.List;

public interface DriverDAO {

    void addDriver(Driver driver);

    Driver findByEmail(String email);

    Driver findByID(int driverID);

    List<Driver> getAllDrivers();

    void updateDriver(Driver driver);

}
