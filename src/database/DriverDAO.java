package com.src.database;

import com.src.model.Driver;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class DriverDAO {

    public void addDriver(com.src.model.Driver driver) {
        String sqlDataInsert = "INSERT INTO driver (license_num, driver_name, gender, age, phone_number, email, password)" + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlDataInsert, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, driver.getLicenseNum()); // Use LicenseNum
            stmt.setString(2, driver.getName());
            stmt.setString(3, driver.getGender());
            stmt.setInt(4, driver.getAge());
            stmt.setString(5, driver.getPhoneNumber());
            stmt.setString(6, driver.getEmail());
            stmt.setString(7, "DUMMY_PASSWORD"); // TODO: Password should be passed in

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    driver.setDriverID(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public com.src.model.Driver findByEmail(String email) {
        String sql = "SELECT * FROM driver d " +
                "LEFT JOIN driver_wallet w ON d.driver_id = w.driver_id " +
                "WHERE d.email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapToDriver(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public com.src.model.Driver findByID(int driverID) {
        String sql = "SELECT * FROM driver d " +
                "LEFT JOIN driver_wallet w ON d.driver_id = w.driver_id " +
                "WHERE d.driver_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, driverID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapToDriver(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<com.src.model.Driver> getAllDrivers() {
        String sql = "SELECT * FROM driver d " +
                "LEFT JOIN driver_wallet w ON d.driver_id = w.driver_id";
        List<com.src.model.Driver> drivers = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                drivers.add(mapToDriver(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }

    public void updateDriver(com.src.model.Driver driver) {
        // FIXED: Removed date columns, updated phone to String, fixed WHERE clause
        String sql = "UPDATE driver SET " +
                "driver_name=?, gender=?, age=?, phone_number=?, email=?, license_num=? " +
                "WHERE driver_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, driver.getName());
            stmt.setString(2, driver.getGender());
            stmt.setInt(3, driver.getAge());
            stmt.setString(4, driver.getPhoneNumber()); // Use setString
            stmt.setString(5, driver.getEmail());
            stmt.setString(6, driver.getLicenseNum());
            stmt.setInt(7, driver.getDriverID()); // Use driver_id

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * ADDED: Updates only the driver's balance
     * @param driver the driver to be updated
     */
    public void updateDriverBalance(Driver driver) {
        String sql = "UPDATE driver_wallet SET balance = ? WHERE driver_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, driver.getTotalEarnings());
            stmt.setInt(2, driver.getDriverID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * FIXED: Reads password, balance, and license_num. Removed date fields.
     */
    private com.src.model.Driver mapToDriver(ResultSet rs) throws SQLException {
        Driver temp_driver = new Driver(
                rs.getInt("driver_id"),
                rs.getString("driver_name"),
                rs.getString("email"),
                rs.getString("password"), // Reads the real password
                rs.getString("phone_number"),
                rs.getDouble("balance"), // From JOINED wallet
                true // Default availability
        );
        temp_driver.setLicenseNum(rs.getString("license_num"));
        temp_driver.setGender(rs.getString("gender"));
        temp_driver.setAge(rs.getInt("age"));

        // Date fields removed, as they are not in the new SQL schema
        return temp_driver;
    }
}