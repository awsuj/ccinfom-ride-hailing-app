package com.src.database;

import com.src.model.Vehicle;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
// FIXED: Import
import static com.src.database.DBConnection.getConnection;

public class VehicleDAO {

    public void addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicle (driver_id, registration_num, plate_num, car_brand, model_name, color, number_of_seats, fuel_type, year_acquired) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            stmt.setInt(9, vehicle.getYearAcquired()); // FIXED: Was setString

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
     * Returns *one* vehicle owned by a driver.
     * Note: Your new schema allows only one.
     */
    public Vehicle findByDriver(int driverID) {
        String sql = "SELECT * FROM vehicle WHERE driver_id = ?";

        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setInt(1, driverID); // FIXED: Was driver_id

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
     * ADDED: Finds *all* vehicles for a driver.
     * This assumes you have run the SQL command to drop the unique constraints.
     */
    public List<Vehicle> findAllByDriverID(int driverID) {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicle WHERE driver_id = ?";

        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, driverID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vehicles.add(mapRowToVehicle(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

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

    private Vehicle mapRowToVehicle(ResultSet rs) throws SQLException {
        // Uses the no-arg constructor and setters
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