package service;

import config.DbConfig;
import entity.Invoice;
import exception.GarageException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InvoiceService {

    public void addInvoice(Invoice invoice) throws GarageException {
        String sql = "INSERT INTO invoices (customer_id, vehicle_id, service_ids, total_amount) VALUES (?, ?, ?, ?)";

        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, invoice.getCustomerId());
            ps.setInt(2, invoice.getVehicleId());
            ps.setString(3, invoice.getServiceIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")));
            ps.setDouble(4, invoice.getTotalAmount());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new GarageException("Failed to create invoice");
            }

            // Get the generated ID
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    invoice.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new GarageException("Error creating invoice: " + e.getMessage(), e);
        }
    }

    public List<Invoice> getAllInvoices() throws GarageException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices ORDER BY date DESC";

        try (Connection conn = DbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String serviceIdsStr = rs.getString("service_ids");
                List<Integer> serviceIds = Arrays.stream(serviceIdsStr.split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());

                invoices.add(new Invoice(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getInt("vehicle_id"),
                        serviceIds,
                        rs.getDouble("total_amount"),
                        rs.getTimestamp("date")
                ));
            }

        } catch (SQLException e) {
            throw new GarageException("Error retrieving invoices: " + e.getMessage(), e);
        }

        return invoices;
    }
}
