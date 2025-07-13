package entity;

public class Service {
    private int id;
    private String description;
    private double cost;

    public Service() {}

    public Service(int id, String description, double cost) {
        this.id = id;
        this.description = description;
        this.cost = cost;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    @Override
    public String toString() {
        return String.format("Service{id=%d, description='%s', cost=%.2f}", id, description, cost);
    }
}
