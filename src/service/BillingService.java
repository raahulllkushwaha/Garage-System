package service;

import entity.Customer;
import entity.Invoice;
import entity.Vehicle;
import exception.GarageException;

import java.util.List;
import java.util.Optional;

public class BillingService {
    private final CustomerService customerService;
    private final VehicleService vehicleService;
    private final ServiceService serviceService;
    private final InvoiceService invoiceService;

    public BillingService() {
        this.customerService = new CustomerService();
        this.vehicleService = new VehicleService();
        this.serviceService = new ServiceService();
        this.invoiceService = new InvoiceService();
    }

    public Customer addCustomerWithVehicle(String name, String phone, String numberPlate, String model) throws GarageException {
        // Check if customer already exists
        Optional<Customer> existingCustomer = customerService.getCustomerByPhone(phone);
        Customer customer;

        if (existingCustomer.isPresent()) {
            customer = existingCustomer.get();
            System.out.println("Customer already exists: " + customer);
        } else {
            customer = new Customer(name, phone);
            customerService.addCustomer(customer);
            System.out.println("New customer added: " + customer);
        }

        // Add vehicle for the customer
        Vehicle vehicle = new Vehicle(customer.getId(), numberPlate, model);
        vehicleService.addVehicle(vehicle);
        System.out.println("Vehicle added: " + vehicle);

        return customer;
    }

    public void createInvoice(int customerId, int vehicleId, List<Integer> serviceIds) throws GarageException {
        // Validate customer exists
        Customer customer = customerService.getCustomerById(customerId);

        // Validate vehicle exists
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);

        // Calculate total cost
        double totalCost = serviceService.calculateTotalCost(serviceIds);

        // Create invoice
        Invoice invoice = new Invoice(customerId, vehicleId, serviceIds);
        invoice.setTotalAmount(totalCost);

        invoiceService.addInvoice(invoice);

        System.out.println("Invoice created successfully!");
        System.out.println("Customer: " + customer.getName());
        System.out.println("Vehicle: " + vehicle.getNumberPlate());
        System.out.println("Total Amount: ₹" + String.format("%.2f", totalCost));
    }

    public void showAllInvoices() throws GarageException {
        List<Invoice> invoices = invoiceService.getAllInvoices();

        if (invoices.isEmpty()) {
            System.out.println("No invoices found.");
            return;
        }

        System.out.println("\n=== ALL INVOICES ===");
        for (Invoice invoice : invoices) {
            Customer customer = customerService.getCustomerById(invoice.getCustomerId());
            Vehicle vehicle = vehicleService.getVehicleById(invoice.getVehicleId());

            System.out.println("Invoice ID: " + invoice.getId());
            System.out.println("Customer: " + customer.getName() + " (" + customer.getPhone() + ")");
            System.out.println("Vehicle: " + vehicle.getNumberPlate() + " - " + vehicle.getModel());
            System.out.println("Services: " + getServiceDetails(invoice.getServiceIds()));
            System.out.println("Total Amount: ₹" + String.format("%.2f", invoice.getTotalAmount()));
            System.out.println("Date: " + invoice.getCreatedDate());
            System.out.println("---");
        }
    }

    public void showAllServices() throws GarageException {
        List<entity.Service> services = serviceService.getAllServices();

        System.out.println("\n=== AVAILABLE SERVICES ===");
        for (entity.Service service : services) {
            System.out.println(service.getId() + ". " + service.getDescription() + " - ₹" + String.format("%.2f", service.getCost()));
        }
    }

    public void showCustomerVehicles(int customerId) throws GarageException {
        Customer customer = customerService.getCustomerById(customerId);
        List<Vehicle> vehicles = vehicleService.getVehiclesByCustomerId(customerId);

        System.out.println("\n=== VEHICLES FOR " + customer.getName() + " ===");
        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle.getId() + ". " + vehicle.getNumberPlate() + " - " + vehicle.getModel());
        }
    }

    private String getServiceDetails(List<Integer> serviceIds) throws GarageException {
        StringBuilder details = new StringBuilder();
        for (int i = 0; i < serviceIds.size(); i++) {
            entity.Service service = serviceService.getServiceById(serviceIds.get(i));
            details.append(service.getDescription()).append(" (₹").append(String.format("%.2f", service.getCost())).append(")");
            if (i < serviceIds.size() - 1) {
                details.append(", ");
            }
        }
        return details.toString();
    }

    public void showAllCustomers() throws GarageException {
        List<Customer> customers = customerService.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }

        System.out.println("\n=== ALL CUSTOMERS ===");
        for (Customer customer : customers) {
            System.out.println("ID: " + customer.getId() +
                    ", Name: " + customer.getName() +
                    ", Phone: " + customer.getPhone());
        }
    }

    public void showAllVehicles() throws GarageException {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();

        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
            return;
        }

        System.out.println("\n=== ALL VEHICLES ===");
        for (Vehicle vehicle : vehicles) {
            Customer customer = customerService.getCustomerById(vehicle.getCustomerId());
            System.out.println("ID: " + vehicle.getId() +
                    ", Number Plate: " + vehicle.getNumberPlate() +
                    ", Model: " + vehicle.getModel() +
                    ", Owner: " + customer.getName() +
                    " (" + customer.getPhone() + ")");
        }
    }

}
