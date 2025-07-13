package service;

import config.DbConfig;
import entity.Customer;
import exception.CustomerNotFoundException;
import exception.GarageException;
import util.InputValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerService {

    public void addCustomer(Customer customer) throws GarageException {
        // Validate input
        InputValidator.validateCustomerData(customer.getName(), customer.getPhone());

        String sql = "INSERT INTO customer (name, phone) VALUES (?, ?)";

        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, customer.getName().trim());
            ps.setString(2, customer.getPhone().trim());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new GarageException("Failed to add customer");
            }

            // Get the generated ID
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Duplicate entry
                throw new GarageException("Customer with this phone number already exists");
            }
            throw new GarageException("Error adding customer: " + e.getMessage(), e);
        }
    }

    public List<Customer> getAllCustomers() throws GarageException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customer ORDER BY name";

        try (Connection conn = DbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                customers.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone")
                ));
            }

        } catch (SQLException e) {
            throw new GarageException("Error retrieving customers: " + e.getMessage(), e);
        }

        return customers;
    }

    public Optional<Customer> getCustomerByPhone(String phone) throws GarageException {
        String sql = "SELECT * FROM customer WHERE phone = ?";

        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Customer(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("phone")
                    ));
                }
            }

        } catch (SQLException e) {
            throw new GarageException("Error finding customer by phone: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    public Customer getCustomerById(int id) throws GarageException {
        InputValidator.validateIds(id);

        String sql = "SELECT * FROM customer WHERE id = ?";

        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("phone")
                    );
                }
            }

        } catch (SQLException e) {
            throw new GarageException("Error finding customer by ID: " + e.getMessage(), e);
        }

        throw new CustomerNotFoundException("Customer not found with ID: " + id);
    }

    public void updateCustomer(Customer customer) throws GarageException {
        InputValidator.validateIds(customer.getId());
        InputValidator.validateCustomerData(customer.getName(), customer.getPhone());

        String sql = "UPDATE customer SET name = ?, phone = ? WHERE id = ?";

        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getName().trim());
            ps.setString(2, customer.getPhone().trim());
            ps.setInt(3, customer.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new CustomerNotFoundException("Customer not found with ID: " + customer.getId());
            }

        } catch (SQLException e) {
            throw new GarageException("Error updating customer: " + e.getMessage(), e);
        }
    }

    public void deleteCustomer(int id) throws GarageException {
        InputValidator.validateIds(id);

        String sql = "DELETE FROM customer WHERE id = ?";

        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new CustomerNotFoundException("Customer not found with ID: " + id);
            }

        } catch (SQLException e) {
            throw new GarageException("Error deleting customer: " + e.getMessage(), e);
        }
    }
}
