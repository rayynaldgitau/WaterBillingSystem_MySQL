package service;

import dao.CustomerDAO;
import dao.WaterMeterDAO;
import model.Customer;
import model.WaterMeter;
import util.IDGenerator;

/**
 * User Management Module (Module 1).
 * Handles user login and authentication, controls access, manages roles.
 * Supports use cases: Login, Register, Password Reset from your Use Case diagram.
 */
public class AuthenticationService {
    private final CustomerDAO customerDAO;
    private final AdminDAO adminDAO;
    private final WaterMeterDAO meterDAO;

    public AuthenticationService(CustomerDAO customerDAO, AdminDAO adminDAO, WaterMeterDAO meterDAO) {
        this.customerDAO = customerDAO;
        this.adminDAO = adminDAO;
        this.meterDAO = meterDAO;
    }

    public Customer authenticateCustomer(String email, String password) {
        if (email == null || password == null) return null;
        Customer c = customerDAO.findByEmail(email);
        if (c != null && c.getPassword().equals(password) && c.isActive()) return c;
        return null;
    }

    public Administrator authenticateAdmin(String username, String password) {
        if (username == null || password == null) return null;
        Administrator a = adminDAO.findByUsername(username);
        if (a != null && a.getPassword().equals(password)) return a;
        return null;
    }

    /** Register a new customer and automatically assign a water meter */
    public Customer registerCustomer(String name, String email, String phone,
                                     String address, String password) {
        if (customerDAO.findByEmail(email) != null)
            throw new IllegalArgumentException("Email already registered.");

        int custID = IDGenerator.nextCustomerID();
        Customer customer = new Customer(custID, name, address, phone, email, password);
        customerDAO.save(customer);

        // Auto-assign a new water meter (Water Meter Management Module)
        int meterID = IDGenerator.nextMeterID();
        WaterMeter meter = new WaterMeter(meterID, custID);
        meterDAO.save(meter);

        return customer;
    }

    /** Reset password */
    public boolean resetPassword(String email, String newPassword) {
        Customer c = customerDAO.findByEmail(email);
        if (c == null) return false;
        c.setPassword(newPassword);
        customerDAO.save(c);
        return true;
    }
}
