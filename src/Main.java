import entity.Customer;
import service.BillingService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        BillingService service = new BillingService();

        while (true) {
            System.out.println("1. Add Customers \n2. Generate Invoice \n3. Show Invoice \n4. Exit");
            int ch = sc.nextInt();
            switch (ch) {
                case 1:
                    System.out.print("Customer name: ");
                    String name = sc.next();
                    System.out.print("Phone number: ");
                    int phone = sc.nextInt();
                    service.customerService.addCustomer(new Customer(0, name, phone));
                    break;

                case 2:
                    System.out.print("Enter Customer ID: ");
                    int cid = sc.nextInt();
                    System.out.print("Enter Vehicle ID: ");
                    int vid = sc.nextInt();
                    System.out.println("Enter number of services: ");
                    int n = sc.nextInt();

                    List<Integer> sids = new ArrayList<>();
                    for (int i = 0; i < n; i++) {
                        System.out.println("Enter the service ID: ");
                        sids.add(sc.nextInt());
                    }
                    service.createInvoice(cid, vid, sids);
                    break;


                case 3:
                    service.showAllInvoices();
                    break;


                case 4:
                    System.exit(0);
                    break;

                default:
                    System.out.println("Enter valid choice: ");
                    break;
            }
        }
    }
}
