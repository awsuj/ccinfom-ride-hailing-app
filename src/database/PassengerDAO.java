package com.src.database;

import com.src.model.Passenger;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import static com.src.database.DBConnection.getConnection;

public class PassengerDAO {

    public void addPassenger(Passenger passenger) {
        String sql = "INSERT INTO customer (customer_name, gender, age, occupation, phone_number, email, password) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, passenger.getName());
            stmt.setString(2, passenger.getGender());
            stmt.setInt(3, passenger.getAge());
            stmt.setString(4, passenger.getOccupation());
            stmt.setString(5, passenger.getPhoneNumber());
            stmt.setString(6, passenger.getEmail());
            stmt.setString(7, passenger.getPassword());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    passenger.setPassengerID(rs.getInt(1));
                    createWallet(passenger.getPassengerID());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createWallet(int passengerID) {
        String sql = "INSERT INTO customer_wallet (customer_id, balance) VALUES (?, 0.00)";
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, passengerID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Passenger findByEmail(String email) {
        String sql = "SELECT * FROM customer c LEFT JOIN customer_wallet w ON c.customer_id = w.customer_id WHERE c.email = ?";
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRowToPassenger(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public Passenger findByID(int passengerID) {
        String sql = "SELECT * FROM customer c LEFT JOIN customer_wallet w ON c.customer_id = w.customer_id WHERE c.customer_id = ?";
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, passengerID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRowToPassenger(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Passenger> getAllPassengers() {
        List<Passenger> passengers = new ArrayList<>();
        String sql = "SELECT * FROM customer c LEFT JOIN customer_wallet w ON c.customer_id = w.customer_id";
        try (Connection c = getConnection(); PreparedStatement stmt = c.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) passengers.add(mapRowToPassenger(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return passengers;
    }

    public void updatePassenger(Passenger passenger) {
        // FIXED: Added password = ?
        String sql = "UPDATE customer SET customer_name = ?, gender = ?, age = ?, occupation = ?, phone_number = ?, email = ?, password = ? WHERE customer_id = ?";

        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setString(1, passenger.getName());
            stmt.setString(2, passenger.getGender());
            stmt.setInt(3, passenger.getAge());
            stmt.setString(4, passenger.getOccupation());
            stmt.setString(5, passenger.getPhoneNumber());
            stmt.setString(6, passenger.getEmail());
            stmt.setString(7, passenger.getPassword()); // Save the password
            stmt.setInt(8, passenger.getPassengerID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePassengerBalance(Passenger passenger) {
        String sql = "UPDATE customer_wallet SET balance = ? WHERE customer_id = ?";
        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setDouble(1, passenger.getBalance());
            stmt.setInt(2, passenger.getPassengerID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Passenger mapRowToPassenger(ResultSet rs) throws SQLException {
        Passenger p = new Passenger(
                rs.getInt("customer_id"),
                rs.getString("customer_name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("phone_number"),
                rs.getDouble("balance")
        );
        p.setGender(rs.getString("gender"));
        p.setAge(rs.getInt("age"));
        p.setOccupation(rs.getString("occupation"));
        return p;
    }
}