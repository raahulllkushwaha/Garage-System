package entity;

public class Vehicle {
    private int id;
    private int customerId;
    private String numberPlate;
    private String model;

    public Vehicle() {}

    public Vehicle(int id, int customerId, String numberPlate, String model) {
        this.id = id;
        this.customerId = customerId;
        this.numberPlate = numberPlate;
        this.model = model;
    }

    public Vehicle(int customerId, String numberPlate, String model) {
        this.customerId = customerId;
        this.numberPlate = numberPlate;
        this.model = model;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getNumberPlate() { return numberPlate; }
    public void setNumberPlate(String numberPlate) { this.numberPlate = numberPlate; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    @Override
    public String toString() {
        return String.format("Vehicle{id=%d, customerId=%d, numberPlate='%s', model='%s'}",
                id, customerId, numberPlate, model);
    }
}
