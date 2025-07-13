package entity;

public class Customer {
    private int id;
    private String name;
    private String phone; // Changed to String for better phone number handling

    public Customer() {}

    public Customer(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public Customer(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return String.format("Customer{id=%d, name='%s', phone='%s'}", id, name, phone);
    }
}