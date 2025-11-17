package com.src.database;

import com.src.model.Passenger;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import static java.sql.DriverManager.getConnection;

public class PassengerDAO {

    /**
     * Add a new passenger into the database
     * @param passenger the passenger to add
     */
    public void addPassenger(Passenger passenger) {
        String sql = "INSERT INTO customer (customer_name, gender, age, occupation, phone_number, email) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, passenger.getName());
            stmt.setString(2, passenger.getGender());
            stmt.setInt(3, passenger.getAge());
            stmt.setString(4, passenger.getOccupation());
            stmt.setLong(5, passenger.getPhoneNumber());
            stmt.setString(6, passenger.getEmail());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    passenger.setPassengerID(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds the passenger based on email 
     * @param email the email of the passenger
     * @return the passenger that owns the email
     */
    public Passenger findByEmail(String email) {
        String sql = "SELECT * FROM customer WHERE email = ?";

        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToPassenger(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Finds the passenger based on ID
     * @param passengerID the ID of the passenger
     * @return the passenger that owns the ID
     */
    public Passenger findByID(int passengerID) {
        String sql = "SELECT * FROM customer WHERE customer_id = ?";

        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setInt(1, passengerID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToPassenger(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Gets the list of all passengers 
     * @return all exisiting passengers
     */
    public List<Passenger> getAllPassengers() {
        List<Passenger> passengers = new ArrayList<>();
        String sql = "SELECT * FROM customer";

        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(sql);
             Result rs = stmt.executeQuery()) {

            while (rs.next()) {
                passengers.add(mapRowToPassenger(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return passengers;
    }

    /**
     * Update the passenger data 
     * @param passenger the passenger to be updated
     */
    public void updatePassenger(Passenger passenger) {
        String sql = "UPDATE customer SET customer_name = ?, gender = ?, age = ?, occupation = ?, phone_number = ?, email = ? WHERE customer_id = ?";

        try (Connection c = getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {

            stmt.setString(1, passenger.getName());
            stmt.setString(2, passenger.getGender());
            stmt.setInt(3, passenger.getAge());
            stmt.setString(4, passenger.getOccupation());
            stmt.setLong(5, passenger.getPhoneNumber());
            stmt.setString(6, passenger.getEmail());
            stmt.setInt(7, passenger.getPassengerID()); //use the exisiting customer_id to locate the row

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method
     * Maps a row from the customer database and converts it into a Passenger object
     */
    private Passenger mapRowToPassenger(ResultSet rs) throws SQLException {
        Passenger p = new Passenger();
        p.setPassengerID(rs.getInt("customer_id"));
        p.setName(rs.getString("customer_name"));
        p.setGender(rs.getString("gender"));
        p.setAge(rs.getInt("age"));
        p.setOccupation(rs.getString("occupation"));
        p.setPhoneNumber(rs.getLong("phone_number"));
        p.setEmail(rs.getString("email"));

        return p;
    }
}



