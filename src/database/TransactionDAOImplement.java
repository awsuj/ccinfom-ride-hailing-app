package com.src.database;

import com.src.dao.TransactionDAO;
import com.src.enumerations.TransactionStatus;
import com.src.model.Transaction;
import com.src.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImplement implements TransactionDAO {

    @Override
    public void addTransaction(Transaction transaction) {
        String sqlDataInsert = "INSERT INTO transactions (customer_id, vehicle_id, date, time, pickup_point, dropoff_point, cost, fulfillment_status)VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sqlDataInsert, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, transaction.getPassengerID());
            stmt.setInt(2, transaction.getVehicleID());
            stmt.setDate(3, Date.valueOf(transaction.getDate()));
            stmt.setTime(4, Time.valueOf(transaction.getTime()));
            stmt.setString(5, transaction.getPickupLocation());
            stmt.setString(6, transaction.getDropoffLocation());
            stmt.setDouble(7, transaction.getCost());
            stmt.setString(8, transaction.getStatus().name());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    transaction.setTransactionID(rs.getInt(1));  // ← THIS LINE
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Transaction getTransactionByID(int id) {
        String sqlDataInsert = "SELECT * FROM transactions WHERE transaction_id = ?";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sqlDataInsert)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return returnNewTransaction(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        List<Transaction> list = new ArrayList<>();
        String sqlDataInsert = "SELECT * FROM transactions";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sqlDataInsert); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(returnNewTransaction(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void updateTransactionStatus(int id, String status) {
        String sqlDataInsert = "UPDATE transactions SET fulfillment_status = ? WHERE transaction_id = ?";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sqlDataInsert)) {

            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTransaction(int id) {
        String sqlDataInsert = "DELETE FROM transactions WHERE transaction_id = ?";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sqlDataInsert)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // A helper I used to return the Transaction
    private Transaction returnNewTransaction(ResultSet rs) throws SQLException {
        int id = rs.getInt("transaction_id");
        int customerID = rs.getInt("customer_id");
        int vehicleID = rs.getInt("vehicle_id");

        LocalDate date = rs.getDate("date").toLocalDate();
        LocalDateTime time = rs.getTime("time").toLocalTime().atDate(date);

        String pickup = rs.getString("pickup_point");
        String dropoff = rs.getString("dropoff_point");
        double cost = rs.getDouble("cost");

        TransactionStatus status = TransactionStatus.valueOf(rs.getString("fulfillment_status").toUpperCase());

        return new Transaction(id, customerID, vehicleID, pickup, dropoff, date, time, cost, status);
    }
}
