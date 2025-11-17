package com.src.database;

import com.src.model.Vehicle;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import static java.sql.DriverManager.getConnection;

public class VehicleDAO {
    
    /**
     * Adds a new vehicle into the database
     * @param vehicle the vehicle to add
     */
    public void addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicle (driver_id, registration_num, plate_num, car_brand, model_name, color, number_of_seats_, fuel_type, year_acquired) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, vehicle.getDriverID());
            stmt.setString(2, vehicle.getRegistrationNum());
            stmt.setString(3, vehicle.getPlateNum());
            stmt.setString(4, vehicle.getCarBrand());
            stmt.setString(5, vehicle.getModelName());
            stmt.setString(6, vehicle.getColor());
            stmt.setInt(7, vehicle.getNumberOfSeats());
            stmt.setString(8, vehicle.getFuelType());
            stmt.setString(9, vehicle.getYearAcquired());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    vehicle.setVehicleID(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds the vehicle based on vehicle_id
     * @param vehicleID the vehicle ID to search
     * @return the vehicle being searched
     */
    public Vehicle findByID(int vehicleID) {
        String sql = "SELECT * FROM vehicle WHERE vehicle_id = ?";

        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setInt(1, vehicleID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToVehicle(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns the vehicle owned by a driver
     * @param driverID the driver that owns the vehicle
     * @return the vehicle that is owned or assigned to that driver
     */
    public Vehicle findByDriver(int driverID) {
        String sql = "SELECT * FROM vehicle WHERE driver_id = ?";
        
        try (Connection c = getConnection();
            PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setInt(1, driver_id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToVehicle(rs);
                    
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Retrieves all vehicles recorded in the table
     * @return list of all vehicles 
     */
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicle";

        try (Connection c = getConnection();
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                vehicles.add(mapRowToVehicle(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicles;
    }

    /**
     * Helper method
     * Maps the current row of a ResultSet to a vehicle object
     * 
     * @param rs result set positioned at a valid row
     * @return identified vehicle
     * @throws SQLException if reading a column fails
     */

     private Vehicle mapRowToVehicle(ResultSet rs) throws SQLException {
        Vehicle v = new Vehicle();

        v.setVehicleID(rs.getInt("vehicle_id"));
        v.setDriverID(rs.getInt("driver_id"));
        v.setRegistrationNum(rs.getString("registration_num"));
        v.setPlateNum(rs.getString("plate_num"));
        v.setCarBrand(rs.getString("car_brand"));
        v.setModelName(rs.getString("model_name"));
        v.setColor(rs.getString("color"));
        v.setNumberOfSeats(rs.getInt("number_of_seats"));
        v.setFuelType(rs.getString("fuel_type"));
        v.setYearAcquired(rs.getInt("year_acquired"));

        return v;
     }
}
