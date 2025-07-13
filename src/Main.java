import config.DbConfig;
import entity.Customer;
import service.BillingService;
import exception.GarageException;
import util.InputValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final BillingService billingService = new BillingService();

    public static void main(String[] args) {
        // Test database connection first
        System.out.println("=== GARAGE MANAGEMENT SYSTEM ===");
        System.out.println("Testing database connection...");
        DbConfig.testConnection();

        while (true) {
            try {
                showMenu();
                int choice = getIntInput("Enter your choice: ");

                switch (choice) {
                    case 1:
                        handleAddCustomerWithVehicle();
                        break;
                    case 2:
                        handleShowServices();
                        break;
                    case 3:
                        handleShowCustomerVehicles();
                        break;
                    case 4:
                        handleGenerateInvoice();
                        break;
                    case 5:
                        handleShowAllInvoices();
                        break;
                    case 6:
                        handleShowAllCustomers();
                        break;
                    case 7:
                        handleShowAllVehicles();
                        break;
                    case 8:
                        System.out.println("Thank you for using Garage Management System!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice! Please select between 1-8.");
                }

            } catch (GarageException e) {
                System.err.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected error: " + e.getMessage());
                e.printStackTrace();
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    private static void showMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Add Customer with Vehicle");
        System.out.println("2. Show Available Services");
        System.out.println("3. Show Customer Vehicles");
        System.out.println("4. Generate Invoice");
        System.out.println("5. Show All Invoices");
        System.out.println("6. Show All Customers");
        System.out.println("7. Show All Vehicles");
        System.out.println("8. Exit");
        System.out.println("==================");
    }

    private static void handleAddCustomerWithVehicle() throws GarageException {
        System.out.println("\n=== ADD CUSTOMER WITH VEHICLE ===");

        String name = getStringInput("Enter customer name: ");
        String phone = getStringInput("Enter phone number (10 digits): ");
        String numberPlate = getStringInput("Enter vehicle number plate (e.g., MP15UN9645): ");
        String model = getStringInput("Enter vehicle model: ");

        Customer customer = billingService.addCustomerWithVehicle(name, phone, numberPlate, model);
        System.out.println("Customer and vehicle added successfully!");
        System.out.println("Customer ID: " + customer.getId());
    }

    private static void handleShowServices() throws GarageException {
        billingService.showAllServices();
    }

    private static void handleShowCustomerVehicles() throws GarageException {
        System.out.println("\n=== SHOW CUSTOMER VEHICLES ===");

        // Show all customers first
        billingService.showAllCustomers();

        int customerId = getIntInput("Enter customer ID: ");
        billingService.showCustomerVehicles(customerId);
    }

    private static void handleGenerateInvoice() throws GarageException {
        System.out.println("\n=== GENERATE INVOICE ===");

        // Show all customers
        billingService.showAllCustomers();
        int customerId = getIntInput("Enter customer ID: ");

        // Show vehicles for selected customer
        billingService.showCustomerVehicles(customerId);
        int vehicleId = getIntInput("Enter vehicle ID: ");

        // Show available services
        billingService.showAllServices();

        int serviceCount = getIntInput("Enter number of services: ");
        List<Integer> serviceIds = new ArrayList<>();

        for (int i = 0; i < serviceCount; i++) {
            int serviceId = getIntInput("Enter service ID " + (i + 1) + ": ");
            serviceIds.add(serviceId);
        }

        billingService.createInvoice(customerId, vehicleId, serviceIds);
    }

    private static void handleShowAllInvoices() throws GarageException {
        billingService.showAllInvoices();
    }

    private static void handleShowAllCustomers() throws GarageException {
        billingService.showAllCustomers();
    }

    private static void handleShowAllVehicles() throws GarageException {
        billingService.showAllVehicles();
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }
}