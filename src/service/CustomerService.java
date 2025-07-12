package service;

import config.DbConfig;
import entity.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {
    public void addCustomer(Customer customer) throws SQLException {
        Connection conn = DbConfig.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO customer (name, phone) VALUES (?, ?)");
        ps.setString(1, customer.getName());
        ps.setInt(2, customer.getPhone());
        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> list = new ArrayList<>();
        Connection conn = DbConfig.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM customer");
        while (rs.next()) {
            list.add(new Customer(rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("phone")
            ));
        }
        return list;
    }
    public Customer getCustomersBasedOnPhoneNum(String number) throws SQLException {
        Customer customer = new Customer();
        Connection conn = DbConfig.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM customer where phone = " + number);
        while (rs.next()) {
          customer = new Customer(rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("phone")
            );
        }
        return customer;
    }
}
