package service;

import config.DbConfig;
import entity.Service;
import exception.GarageException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceService {

    public List<Service> getAllServices() throws GarageException {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services";

        try (Connection conn = DbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                services.add(new Service(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getDouble("cost")
                ));
            }

        } catch (SQLException e) {
            throw new GarageException("Error retrieving services: " + e.getMessage(), e);
        }

        return services;
    }

    public Service getServiceById(int id) throws GarageException {
        String sql = "SELECT * FROM services WHERE id = ?";

        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Service(
                            rs.getInt("id"),
                            rs.getString("description"),
                            rs.getDouble("cost")
                    );
                }
            }

        } catch (SQLException e) {
            throw new GarageException("Error finding service by ID: " + e.getMessage(), e);
        }

        throw new GarageException("Service not found with ID: " + id);
    }

    public double calculateTotalCost(List<Integer> serviceIds) throws GarageException {
        if (serviceIds.isEmpty()) {
            return 0.0;
        }

        StringBuilder sql = new StringBuilder("SELECT SUM(cost) as total FROM services WHERE id IN (");
        for (int i = 0; i < serviceIds.size(); i++) {
            sql.append("?");
            if (i < serviceIds.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(")");

        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < serviceIds.size(); i++) {
                ps.setInt(i + 1, serviceIds.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }

        } catch (SQLException e) {
            throw new GarageException("Error calculating total cost: " + e.getMessage(), e);
        }

        return 0.0;
    }
}