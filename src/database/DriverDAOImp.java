// CHANGE THE IMPORTS THOUGH, CUZ IDK I CHANGED IT TO FIT MY COMPUTER

import com.ridehailing.model.Driver;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

// this took me longer than necessary...
public class DriverDAOImpl implements DriverDAO {

    @Override
    public void addDriver(Driver driver) {
        String sqlDataInsert = "INSERT INTO driver (license_num, driver_name, gender, age, phone_number, email, date_of_employment, date_of_resignation)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sqlDataInsert, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, driver.getDriverID());
            stmt.setString(2, driver.getName());
            stmt.setString(3, driver.getGender());
            stmt.setInt(4, driver.getAge());
            stmt.setLong(5, Long.parseLong(driver.getPhoneNumber()));
            stmt.setString(6, driver.getEmail());
            stmt.setDate(8, Date.valueOf(driver.getDateOfResignation()));

            if (driver.getDateOfResignation() != null) {
                stmt.setDate(8, new java.sql.Date(driver.getDateOfEmployment().getTime()));
            } else {
                stmt.setNull(8, Types.DATE);
            }

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

    @Override
    public Driver findByEmail(String email) {
        String sql = "SELECT * FROM driver WHERE email = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

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

    @Override
    public Driver findByID(int driverID) {
        String sql = "SELECT * FROM driver WHERE driver_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

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

    @Override
    public List<Driver> getAllDrivers() {
        String sql = "SELECT * FROM driver";
        List<Driver> drivers = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            `
            while (rs.next()) {
                drivers.add(mapToDriver(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return drivers;
    }

    @Override
    public void updateDriver(Driver driver) {
        String sql = "UPDATE driver SET " + "driver_name=?, gender=?, age=?, phone_number=?, email=?, " + "date_of_employment=?, date_of_resignation=? " + "WHERE license_num=?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, driver.getName());
            stmt.setString(2, driver.getGender());
            stmt.setInt(3, driver.getAge());
            stmt.setLong(4, Long.parseLong(driver.getPhoneNumber()));
            stmt.setString(5, driver.getEmail());
            stmt.setDate(6, Date.valueOf(driver.getDateOfEmployment()));

            if (driver.getDateOfResignation() != null) {
                stmt.setDate(7, Date.valueOf(driver.getDateOfResignation()));
            } else {
                stmt.setNull(7, Types.DATE);
            }

            stmt.setString(8, driver.getDriverID());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Driver mapToDriver(ResultSet rs) throws SQLException {
        Driver temp_driver = new Driver();

        temp_driver.setDriverID(rs.getString("license_num"));
        temp_driver.setName(rs.getString("driver_name"));
        temp_driver.setGender(rs.getString("gender"));
        temp_driver.setAge(rs.getInt("age"));
        temp_driver.setPhoneNumber(String.valueOf(rs.getLong("phone_number")));
        temp_driver.setEmail(rs.getString("email"));
        temp_driver.setDateOfEmployment(rs.getDate("date_of_employment").toLocalDate());
        Date resign = rs.getDate("date_of_resignation");
        temp_driver.setDateOfResignation(resign != null ? resign.toLocalDate() : null);

        return temp_driver;
    }
}
