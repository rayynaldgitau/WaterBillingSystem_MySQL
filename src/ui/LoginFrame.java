package ui;

<<<<<<< HEAD
import javax.swing.*;
import java.awt.*;
import dao.MySQLAdminDAO;

public class LoginFrame extends JFrame {
    private JTextField userField = new JTextField(15);
    private JPasswordField passField = new JPasswordField(15);
    private JButton loginBtn = new JButton("Login");

    public LoginFrame() {
        setTitle("Water Billing System - Login");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(loginBtn, gbc);

        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            
            MySQLAdminDAO auth = new MySQLAdminDAO();
            if (auth.authenticate(user, pass)) {
                new AdminFrame().setVisible(true); // Opens the dashboard
                this.dispose(); // Closes the login window
            } else {
                JOptionPane.showMessageDialog(this, "Access Denied: Check Credentials");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
=======
import model.Customer;
import service.AuthenticationService;

import javax.swing.*;
import java.awt.*;

/**
 * Login screen - entry point for the Swing GUI.
 * Supports Customer Login, Admin Login, and New Registration.
 * Maps to Use Case: Login, Register from Customer Portal.
 */
public class LoginFrame extends JFrame {
    private final AuthenticationService authService;
    private final BillingService billingService;
    private final PaymentService paymentService;
    private final ComplaintService complaintService;
    private final ReportingService reportingService;
    private final NotificationService notificationService;
    private final dao.CustomerDAO customerDAO;
    private final dao.BillDAO billDAO;
    private final dao.WaterMeterDAO meterDAO;
    private final dao.TariffDAO tariffDAO;

    public LoginFrame(AuthenticationService authService, BillingService billingService,
                      PaymentService paymentService, ComplaintService complaintService,
                      ReportingService reportingService, NotificationService notificationService,
                      dao.CustomerDAO customerDAO, dao.BillDAO billDAO,
                      dao.WaterMeterDAO meterDAO, dao.TariffDAO tariffDAO) {
        this.authService = authService;
        this.billingService = billingService;
        this.paymentService = paymentService;
        this.complaintService = complaintService;
        this.reportingService = reportingService;
        this.notificationService = notificationService;
        this.customerDAO = customerDAO;
        this.billDAO = billDAO;
        this.meterDAO = meterDAO;
        this.tariffDAO = tariffDAO;

        setTitle("Water Billing Management System - Group 9");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 420);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        main.setBackground(new Color(255, 255, 255));

        // Title
        JLabel title = new JLabel("Water Billing Management System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(31, 78, 121));
        JLabel subtitle = new JLabel("Group 9 - APT3040VC | USIU-Africa", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);

        // Tabbed pane for Customer / Admin login
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.PLAIN, 13));
        tabs.addTab("Customer Login", createCustomerLoginPanel());
        tabs.addTab("Admin Login", createAdminLoginPanel());
        tabs.addTab("Register", createRegistrationPanel());

        main.add(titlePanel, BorderLayout.NORTH);
        main.add(tabs, BorderLayout.CENTER);
        setContentPane(main);
    }

    private JPanel createCustomerLoginPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField emailField = new JTextField(20);
        JPasswordField passField = new JPasswordField(20);
        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(46, 117, 182));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setOpaque(true);
        loginBtn.setBorderPainted(false);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 13));

        g.gridx = 0; g.gridy = 0; p.add(new JLabel("Email:"), g);
        g.gridx = 1; p.add(emailField, g);
        g.gridx = 0; g.gridy = 1; p.add(new JLabel("Password:"), g);
        g.gridx = 1; p.add(passField, g);
        g.gridx = 0; g.gridy = 2; g.gridwidth = 2; g.fill = GridBagConstraints.NONE;
        p.add(loginBtn, g);

        loginBtn.addActionListener(e -> {
            Customer customer = authService.authenticateCustomer(
                    emailField.getText().trim(), new String(passField.getPassword()));
            if (customer != null) {
                new CustomerFrame(customer, billingService, paymentService,
                        complaintService, billDAO, this).setVisible(true);
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        return p;
    }

    private JPanel createAdminLoginPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField userField = new JTextField(20);
        JPasswordField passField = new JPasswordField(20);
        JButton loginBtn = new JButton("Admin Login");
        loginBtn.setBackground(new Color(31, 78, 121));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setOpaque(true);
        loginBtn.setBorderPainted(false);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 13));

        g.gridx = 0; g.gridy = 0; p.add(new JLabel("Username:"), g);
        g.gridx = 1; p.add(userField, g);
        g.gridx = 0; g.gridy = 1; p.add(new JLabel("Password:"), g);
        g.gridx = 1; p.add(passField, g);
        g.gridx = 0; g.gridy = 2; g.gridwidth = 2; g.fill = GridBagConstraints.NONE;
        p.add(loginBtn, g);

        loginBtn.addActionListener(e -> {
            Administrator admin = authService.authenticateAdmin(
                    userField.getText().trim(), new String(passField.getPassword()));
            if (admin != null) {
                new AdminFrame(admin, customerDAO, billDAO, meterDAO, tariffDAO,
                        billingService, paymentService, complaintService,
                        reportingService, notificationService, authService, this).setVisible(true);
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        return p;
    }

    private JPanel createRegistrationPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JPasswordField passField = new JPasswordField(20);
        JButton regBtn = new JButton("Register");
        regBtn.setBackground(new Color(0, 128, 0));
        regBtn.setForeground(Color.WHITE);
        regBtn.setOpaque(true);
        regBtn.setBorderPainted(false);
        regBtn.setFont(new Font("Arial", Font.BOLD, 13));

        String[] labels = {"Full Name:", "Email:", "Phone:", "Address:", "Password:"};
        JComponent[] fields = {nameField, emailField, phoneField, addressField, passField};
        for (int i = 0; i < labels.length; i++) {
            g.gridx = 0; g.gridy = i; p.add(new JLabel(labels[i]), g);
            g.gridx = 1; p.add(fields[i], g);
        }
        g.gridx = 0; g.gridy = labels.length; g.gridwidth = 2; g.fill = GridBagConstraints.NONE;
        p.add(regBtn, g);

        regBtn.addActionListener(e -> {
            try {
                Customer c = authService.registerCustomer(
                        nameField.getText().trim(), emailField.getText().trim(),
                        phoneField.getText().trim(), addressField.getText().trim(),
                        new String(passField.getPassword()));
                JOptionPane.showMessageDialog(this,
                        "Registration successful!\nCustomer ID: " + c.getCustomerID() +
                        "\nA water meter has been assigned automatically.\nYou can now login with your email.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                // Clear fields
                for (JComponent f : fields) {
                    if (f instanceof JTextField) ((JTextField) f).setText("");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        return p;
>>>>>>> e4a00967315035fa6cf55cf7096ccc97d6e88e40
    }
}
