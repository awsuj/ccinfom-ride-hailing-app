package com.src.database;

import com.src.database.DBConnection;
import com.src.enumerations.TransactionStatus;
import com.src.model.Transaction;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // Define the time format used by your controllers
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void addTransaction(Transaction transaction) {
        // NOTE: driver_id is NOT in the query because it was removed from the 'transactions' table
        String sqlDataInsert = "INSERT INTO transactions (customer_id, vehicle_id, date, time, pickup_point, dropoff_point, cost, fulfillment_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlDataInsert, Statement.RETURN_GENERATED_KEYS)) {

            // Parse the String time from the model to LocalDateTime
            LocalDateTime ldt = LocalDateTime.parse(transaction.getTime(), formatter);

            stmt.setInt(1, transaction.getPassengerID());
            stmt.setInt(2, transaction.getVehicleID());
            stmt.setDate(3, Date.valueOf(ldt.toLocalDate())); // Set SQL DATE
            stmt.setTime(4, Time.valueOf(ldt.toLocalTime())); // Set SQL TIME
            stmt.setString(5, transaction.getPickupLocation());
            stmt.setString(6, transaction.getDropoffLocation());
            stmt.setDouble(7, transaction.getCost());
            stmt.setString(8, transaction.getStatus().name());

            stmt.executeUpdate();

            // Retrieve the auto-generated ID
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    transaction.setTransactionID(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Transaction getTransactionByID(int id) {
        String sqlDataInsert = "SELECT * FROM transactions WHERE transaction_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlDataInsert)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToTransaction(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Finds all transactions for a specific passenger.
     */
    public List<Transaction> findByPassenger(int passengerID) {
        List<Transaction> list = new ArrayList<>();
        String sqlDataInsert = "SELECT * FROM transactions WHERE customer_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlDataInsert)) {

            stmt.setInt(1, passengerID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRowToTransaction(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Finds all transactions for a specific driver.
     * Since 'transactions' table has no driver_id, we JOIN with 'vehicle' table.
     */
    public List<Transaction> findByDriver(int driverID) {
        List<Transaction> list = new ArrayList<>();
        String sqlDataInsert = "SELECT t.* FROM transactions t " +
                "JOIN vehicle v ON t.vehicle_id = v.vehicle_id " +
                "WHERE v.driver_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlDataInsert)) {

            stmt.setInt(1, driverID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRowToTransaction(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> list = new ArrayList<>();
        String sqlDataInsert = "SELECT * FROM transactions";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlDataInsert);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapRowToTransaction(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateTransaction(Transaction transaction) {
        String sqlDataInsert = "UPDATE transactions SET fulfillment_status = ?, cost = ? WHERE transaction_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlDataInsert)) {

            stmt.setString(1, transaction.getStatus().name());
            stmt.setDouble(2, transaction.getCost());
            stmt.setInt(3, transaction.getTransactionID());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTransaction(int id) {
        String sqlDataInsert = "DELETE FROM transactions WHERE transaction_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlDataInsert)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to map a ResultSet row to a Transaction object.
     */
    private Transaction mapRowToTransaction(ResultSet rs) throws SQLException {
        int id = rs.getInt("transaction_id");
        int customerID = rs.getInt("customer_id");
        int vehicleID = rs.getInt("vehicle_id");
        double cost = rs.getDouble("cost");
        String pickup = rs.getString("pickup_point");
        String dropoff = rs.getString("dropoff_point");

        // Combine SQL DATE and TIME columns into one LocalDateTime
        LocalDateTime ldt = rs.getTimestamp("time").toLocalDateTime().with(rs.getDate("date").toLocalDate());
        // Format it back into the String your model expects
        String timeString = ldt.format(formatter);

        TransactionStatus status = TransactionStatus.valueOf(rs.getString("fulfillment_status").toUpperCase());

        // Use the constructor that matches your updated Transaction.java (no driverID)
        Transaction transaction = new Transaction(id, customerID, vehicleID, pickup, dropoff, cost, timeString);
        transaction.setStatus(status);

        return transaction;
    }
}