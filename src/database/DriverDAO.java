package com.src.database;

import com.src.model.Driver;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import static com.src.database.DBConnection.getConnection;

public class DriverDAO {

    public void addDriver(Driver driver) {
        String sqlDataInsert = "INSERT INTO driver (license_num, driver_name, gender, age, phone_number, email, password) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlDataInsert, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, driver.getLicenseNum());
            stmt.setString(2, driver.getName());
            stmt.setString(3, driver.getGender());
            stmt.setInt(4, driver.getAge());
            stmt.setString(5, driver.getPhoneNumber());
            stmt.setString(6, driver.getEmail());
            stmt.setString(7, driver.getPassword());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    driver.setDriverID(rs.getInt(1));
                    createWallet(driver.getDriverID());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createWallet(int driverID) {
        String sql = "INSERT INTO driver_wallet (driver_id, balance) VALUES (?, 0.00)";
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, driverID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Driver findByEmail(String email) {
        String sql = "SELECT * FROM driver d LEFT JOIN driver_wallet w ON d.driver_id = w.driver_id WHERE d.email = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapToDriver(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public Driver findByID(int driverID) {
        String sql = "SELECT * FROM driver d LEFT JOIN driver_wallet w ON d.driver_id = w.driver_id WHERE d.driver_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, driverID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapToDriver(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Driver> getAllDrivers() {
        String sql = "SELECT * FROM driver d LEFT JOIN driver_wallet w ON d.driver_id = w.driver_id";
        List<Driver> drivers = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) drivers.add(mapToDriver(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return drivers;
    }

    public void updateDriver(Driver driver) {
        String sql = "UPDATE driver SET driver_name=?, gender=?, age=?, phone_number=?, email=?, license_num=?, password=? WHERE driver_id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, driver.getName());
            stmt.setString(2, driver.getGender());
            stmt.setInt(3, driver.getAge());
            stmt.setString(4, driver.getPhoneNumber());
            stmt.setString(5, driver.getEmail());
            stmt.setString(6, driver.getLicenseNum());
            stmt.setString(7, driver.getPassword());
            stmt.setInt(8, driver.getDriverID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDriverBalance(Driver driver) {
        String sql = "UPDATE driver_wallet SET balance = ? WHERE driver_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, driver.getTotalEarnings());
            stmt.setInt(2, driver.getDriverID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Driver mapToDriver(ResultSet rs) throws SQLException {
        Driver temp_driver = new Driver(
                rs.getInt("driver_id"),
                rs.getString("driver_name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("phone_number"),
                rs.getDouble("balance"),
                true
        );
        temp_driver.setLicenseNum(rs.getString("license_num"));
        temp_driver.setGender(rs.getString("gender"));
        temp_driver.setAge(rs.getInt("age"));
        return temp_driver;
    }
}