package entity;

import java.sql.Timestamp;
import java.util.List;

public class Invoice {
    private int id;
    private int customerId;
    private int vehicleId;
    private List<Integer> serviceIds;
    private double totalAmount;
    private Timestamp createdDate;

    public Invoice() {}

    public Invoice(int customerId, int vehicleId, List<Integer> serviceIds) {
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.serviceIds = serviceIds;
    }

    public Invoice(int id, int customerId, int vehicleId, List<Integer> serviceIds,
                   double totalAmount, Timestamp createdDate) {
        this.id = id;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.serviceIds = serviceIds;
        this.totalAmount = totalAmount;
        this.createdDate = createdDate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public List<Integer> getServiceIds() { return serviceIds; }
    public void setServiceIds(List<Integer> serviceIds) { this.serviceIds = serviceIds; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public Timestamp getCreatedDate() { return createdDate; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }

    @Override
    public String toString() {
        return String.format("Invoice{id=%d, customerId=%d, vehicleId=%d, serviceIds=%s, totalAmount=%.2f, createdDate=%s}",
                id, customerId, vehicleId, serviceIds, totalAmount, createdDate);
    }
}
