package service;

import config.DbConfig;
import entity.Vehicle;
import exception.GarageException;
import exception.VehicleNotFoundException;
import util.InputValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehicleService {

    public void addVehicle(Vehicle vehicle) throws GarageException {
        // Validate input
        InputValidator.validateIds(vehicle.getCustomerId());
        InputValidator.validateVehicleData(vehicle.getNumberPlate(), vehicle.getModel());

        String sql = "INSERT INTO vehicle (customer_id, number_plate, model) VALUES (?, ?, ?)";

        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, vehicle.getCustomerId());
            ps.setString(2, vehicle.getNumberPlate().toUpperCase().trim());
            ps.setString(3, vehicle.getModel().trim());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new GarageException("Failed to add vehicle");
            }

            // Get the generated ID
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vehicle.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Duplicate entry
                throw new GarageException("Vehicle with this number plate already exists");
            }
            if (e.getErrorCode() == 1452) { // Foreign key constraint
                throw new GarageException("Customer ID does not exist");
            }
            throw new GarageException("Error adding vehicle: " + e.getMessage(), e);
        }
    }

    public List<Vehicle> getAllVehicles() throws GarageException {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicle ORDER BY number_plate";

        try (Connection conn = DbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                vehicles.add(new Vehicle(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getString("number_plate"),
                        rs.getString("model")
                ));
            }

        } catch (SQLException e) {
            throw new GarageException("Error retrieving vehicles: " + e.getMessage(), e);
        }

        return vehicles;
    }

    public List<Vehicle> getVehiclesByCustomerId(int customerId) throws GarageException {
        InputValidator.validateIds(customerId);

        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicle WHERE customer_id = ? ORDER BY number_plate";

        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    vehicles.add(new Vehicle(
                            rs.getInt("id"),
                            rs.getInt("customer_id"),
                            rs.getString("number_plate"),
                            rs.getString("model")
                    ));
                }
            }

        } catch (SQLException e) {
            throw new GarageException("Error retrieving vehicles by customer ID: " + e.getMessage(), e);
        }

        return vehicles;
    }

    public Vehicle getVehicleById(int id) throws GarageException {
        InputValidator.validateIds(id);

        String sql = "SELECT * FROM vehicle WHERE id = ?";

        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Vehicle(
                            rs.getInt("id"),
                            rs.getInt("customer_id"),
                            rs.getString("number_plate"),
                            rs.getString("model")
                    );
                }
            }

        } catch (SQLException e) {
            throw new GarageException("Error finding vehicle by ID: " + e.getMessage(), e);
        }

        throw new VehicleNotFoundException("Vehicle not found with ID: " + id);
    }

    public Optional<Vehicle> getVehicleByNumberPlate(String numberPlate) throws GarageException {
        String sql = "SELECT * FROM vehicle WHERE number_plate = ?";

        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, numberPlate.toUpperCase().trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Vehicle(
                            rs.getInt("id"),
                            rs.getInt("customer_id"),
                            rs.getString("number_plate"),
                            rs.getString("model")
                    ));
                }
            }

        } catch (SQLException e) {
            throw new GarageException("Error finding vehicle by number plate: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    public void updateVehicle(Vehicle vehicle) throws GarageException {
        InputValidator.validateIds(vehicle.getId(), vehicle.getCustomerId());
        InputValidator.validateVehicleData(vehicle.getNumberPlate(), vehicle.getModel());

        String sql = "UPDATE vehicle SET customer_id = ?, number_plate = ?, model = ? WHERE id = ?";

        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vehicle.getCustomerId());
            ps.setString(2, vehicle.getNumberPlate().toUpperCase().trim());
            ps.setString(3, vehicle.getModel().trim());
            ps.setInt(4, vehicle.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new VehicleNotFoundException("Vehicle not found with ID: " + vehicle.getId());
            }

        } catch (SQLException e) {
            throw new GarageException("Error updating vehicle: " + e.getMessage(), e);
        }
    }

    public void deleteVehicle(int id) throws GarageException {
        InputValidator.validateIds(id);

        String sql = "DELETE FROM vehicle WHERE id = ?";

        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new VehicleNotFoundException("Vehicle not found with ID: " + id);
            }

        } catch (SQLException e) {
            throw new GarageException("Error deleting vehicle: " + e.getMessage(), e);
        }
    }
}