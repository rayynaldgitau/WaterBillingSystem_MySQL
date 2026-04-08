import dao.CustomerDAO;
import dao.WaterMeterDAO;
import dao.mysql.MySQLCustomerDAO;
import dao.mysql.MySQLWaterMeterDAO;
import model.Customer;
import model.WaterMeter;
import service.AuthenticationService;
import ui.LoginFrame;
import util.*;

import javax.swing.*;

/**
 * Water Billing Management System - Main Entry Point
 * Group 9 - APT3040VC | USIU-Africa | Prof. Ernest Madara
 *
 * Supports TWO storage modes (auto-detected):
 *   1. MySQL  - if mysql-connector-j.jar is in lib/ and MySQL is running
 *   2. In-Memory (HashMap) - automatic fallback if MySQL is unavailable
 *
 * To use MySQL:
 *   1. Install MySQL, run: mysql -u root -p < schema.sql
 *   2. Download mysql-connector-j.jar into lib/
 *   3. Run: java -cp "bin:lib/*" WaterBillingApp       (Linux/Mac)
 *       or: java -cp "bin;lib/*" WaterBillingApp       (Windows)
 */
public class WaterBillingApp {

    public static void main(String[] args) {
        // Try MySQL first, fall back to in-memory
        boolean useMySQL = DatabaseManager.initialize();

        // Create DAOs - MySQL or in-memory depending on availability
        CustomerDAO customerDAO;
        WaterMeterDAO meterDAO;
        MeterReadingDAO readingDAO;
        BillDAO billDAO;
        PaymentDAO paymentDAO;
        ComplaintDAO complaintDAO;
        AdminDAO adminDAO;
        TariffDAO tariffDAO;

        if (useMySQL) {
            System.out.println("[SYSTEM] Using MySQL database storage.");
            customerDAO = new MySQLCustomerDAO();
            meterDAO = new MySQLWaterMeterDAO();
            readingDAO = new MySQLMeterReadingDAO();
            billDAO = new MySQLBillDAO();
            paymentDAO = new MySQLPaymentDAO();
            complaintDAO = new MySQLComplaintDAO();
            adminDAO = new MySQLAdminDAO();
            tariffDAO = new MySQLTariffDAO();
        } else {
            System.out.println("[SYSTEM] Using in-memory storage (data resets on restart).");
            customerDAO = new CustomerDAO();
            meterDAO = new WaterMeterDAO();
            readingDAO = new MeterReadingDAO();
            billDAO = new BillDAO();
            paymentDAO = new PaymentDAO();
            complaintDAO = new ComplaintDAO();
            adminDAO = new AdminDAO();
            tariffDAO = new TariffDAO();
        }

        // Default tariff
        Tariff defaultTariff;
        if (useMySQL && tariffDAO.count() > 0) {
            defaultTariff = tariffDAO.findAll().get(0);
        } else {
            defaultTariff = new Tariff(IDGenerator.nextTariffID(), "Standard Rate", 50.0, 150.0);
            tariffDAO.save(defaultTariff);
        }

        // Services (same regardless of storage mode - Dependency Inversion Principle)
        NotificationService notifService = new NotificationService();
        BillingService billingService = new BillingService(customerDAO, meterDAO, readingDAO,
                billDAO, tariffDAO, notifService, defaultTariff);
        PaymentService paymentService = new PaymentService(paymentDAO, billDAO, customerDAO, notifService);
        AuthenticationService authService = new AuthenticationService(customerDAO, adminDAO, meterDAO);
        ComplaintService complaintService = new ComplaintService(complaintDAO, customerDAO);
        ReportingService reportingService = new ReportingService(customerDAO, billDAO, paymentDAO, readingDAO, meterDAO);

        // Load sample data only for in-memory mode (MySQL uses schema.sql data)
        if (!useMySQL) {
            loadSampleData(customerDAO, meterDAO, adminDAO, billingService);
        }

        // Shutdown hook to close DB connection
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseManager::close));

        // Launch Swing GUI
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

            String storageLabel = useMySQL ? " [MySQL]" : " [In-Memory]";
            LoginFrame loginFrame = new LoginFrame(authService, billingService, paymentService,
                    complaintService, reportingService, notifService,
                    customerDAO, billDAO, meterDAO, tariffDAO);
            loginFrame.setTitle(loginFrame.getTitle() + storageLabel);
            loginFrame.setVisible(true);
        });
    }

    private static void loadSampleData(CustomerDAO customerDAO, WaterMeterDAO meterDAO,
                                       AdminDAO adminDAO, BillingService billingService) {
        adminDAO.save(new Administrator(1, "System Administrator", "admin", "admin123", "Admin"));
        adminDAO.save(new Administrator(2, "Jane Billing Clerk", "clerk", "clerk123", "Billing Clerk"));
        adminDAO.save(new Administrator(3, "John Meter Reader", "reader", "reader123", "Meter Reader"));

        Object[][] custs = {
            {1001, "Raynald Gitau", "Nairobi, Westlands", "0712345678", "raynald@email.com", "pass123"},
            {1002, "Maida H Mohammed", "Nairobi, Kilimani", "0723456789", "maida@email.com", "pass123"},
            {1003, "Sanaipei Tenkes", "Nairobi, Lavington", "0734567890", "sanaipei@email.com", "pass123"},
            {1004, "Ramla Jama", "Nairobi, South B", "0745678901", "ramla@email.com", "pass123"},
            {1005, "Mohamed Dahir", "Nairobi, Eastleigh", "0756789012", "mohamed@email.com", "pass123"},
        };
        for (Object[] c : custs) {
            customerDAO.save(new Customer((int)c[0], (String)c[1], (String)c[2], (String)c[3], (String)c[4], (String)c[5]));
            meterDAO.save(new WaterMeter(IDGenerator.nextMeterID(), (int)c[0]));
        }
        billingService.submitReadingAndGenerateBill(1001, 150.0);
        billingService.submitReadingAndGenerateBill(1002, 85.0);
        billingService.submitReadingAndGenerateBill(1003, 200.0);
        billingService.submitReadingAndGenerateBill(1004, 45.0);
        billingService.submitReadingAndGenerateBill(1005, 120.0);
    }
}
