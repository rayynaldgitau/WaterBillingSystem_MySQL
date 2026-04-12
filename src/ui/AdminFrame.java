package ui;

import javax.swing.*;
import java.awt.*;
import service.ComplaintService;
import service.ReportingService;
import model.Complaint;
import model.User;
import javax.swing.DefaultListModel;
import javax.swing.JList;

public class AdminFrame extends JFrame {
    private ComplaintService service = new ComplaintService();
    // ADD THE NEW LINE RIGHT HERE:
    private ReportingService reportingService = new ReportingService(); 
    
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> complaintList = new JList<>(listModel);
    private JLabel totalLabel = new JLabel("Total Complaints: 0");
     private JButton resolveBtn = new JButton("Mark as Resolved");
    public AdminFrame() {
        
        // Professional Look
        try {
            UIManager.setLookAndFeel(new javax.swing.plaf.nimbus.NimbusLookAndFeel());
        } catch (Exception e) { }

        setTitle("Water Billing System - Admin Dashboard (Modules 1 & 9)");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        // 1. Dashboard Tab
        tabs.addTab("Dashboard", createDashboardPanel());

        // 2. Complaints Tab (Module 9)
        tabs.addTab("Complaints", createComplaintPanel());
        

        

        // 4. Users Tab
        tabs.addTab("Users", createUserPanel());

        // 5. Usage Tab
        tabs.addTab("Usage", createUsagePanel());

        // 6. Reports Tab (Module 1)
        tabs.addTab("Reports", createReportPanel());

        // 7. Settings Tab
        
        tabs.addTab("Billing", createBillingPanel());

        add(tabs);
        setVisible(true);
    }
    private JPanel createDashboardPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(Color.WHITE);

    JLabel welcomeLabel = new JLabel("Water Billing Management System", JLabel.CENTER);
    welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
    welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

    JTextArea infoArea = new JTextArea();
    infoArea.setEditable(false);
    infoArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
    infoArea.setText(
        " System Status: ONLINE\n" +
        " Connected to: MySQL (water_billing_db)\n" +
        " --------------------------------------\n" +
        " Quick Guide:\n" +
        " 1. Use 'Usage' to enter meter readings.\n" +
        " 2. Use 'Billing' to generate invoices.\n" +
        " 3. Use 'Complaints' to track customer issues.\n" +
        " 4. Use 'Reports' to see total revenue."
    );
    infoArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

    panel.add(welcomeLabel, BorderLayout.NORTH);
    panel.add(new JScrollPane(infoArea), BorderLayout.CENTER);

    return panel;
}
   private JPanel createComplaintPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // 1. Input area setup
        JPanel top = new JPanel();
        JTextField input = new JTextField(20);
        JButton addBtn = new JButton("Log New Complaint");
        JButton resolveBtn = new JButton("Mark as Resolved"); // The new button

        top.add(new JLabel("Description:"));
        top.add(input);
        top.add(addBtn);
        top.add(resolveBtn); // ADDED: Putting the button on the screen

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(complaintList), BorderLayout.CENTER);

        // 2. Action for Adding
        addBtn.addActionListener(e -> {
            if (!input.getText().isEmpty()) {
                service.addComplaint(input.getText());
                updateUI();
                input.setText("");
            }
        });

        // 3. Action for Resolving
        resolveBtn.addActionListener(e -> {
            String selected = complaintList.getSelectedValue();
            if (selected != null) {
                // Logic: Extract the description from the list item
                // This removes the "[Status] " prefix before sending to MySQL
                String description = selected.substring(selected.indexOf("] ") + 2);
                
                service.resolveComplaint(description);
                updateUI(); // Refresh the list and the total count
                JOptionPane.showMessageDialog(this, "Complaint marked as Resolved!");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a complaint from the list first.");
            }
        });

        return panel;
    }
   
    private JPanel createBillingPanel() {
    JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
    
    JTextField nameField = new JTextField();
    JTextField unitField = new JTextField();
    JLabel resultLabel = new JLabel("Total: KES 0.00");
    JButton calcBtn = new JButton("Calculate & Save Bill");

    panel.add(new JLabel("Customer Name:"));
    panel.add(nameField);
    panel.add(new JLabel("Units Consumed (m3):"));
    panel.add(unitField);
    panel.add(new JLabel("Final Amount:"));
    panel.add(resultLabel);
    panel.add(new JLabel("")); // Spacer
    panel.add(calcBtn);

    
        calcBtn.addActionListener(e -> {
        try {
            // 1. Get input from the text fields
            String name = nameField.getText();
            String unitText = unitField.getText();

            // Validation: Ensure fields aren't empty
            if (name.isEmpty() || unitText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }
            double units = Double.parseDouble(unitText);
            double total = 0;

            // 2. TIERED LOGIC (The "Math" for the examiners)
            if (units <= 10) {
                total = units * 45; // Tier 1: 0-10 units
            } else if (units <= 20) {
                total = (10 * 45) + ((units - 10) * 60); // Tier 2: 11-20 units
            } else {
                total = (10 * 45) + (10 * 60) + ((units - 20) * 80); // Tier 3: 20+ units
            }

            // 3. Update the UI Label
            resultLabel.setText("Total: KES " + String.format("%.2f", total));

            // 4. CALL THE DAO: Save to MySQL
            // (Make sure you created the MySQLBillDAO class first!)
            dao.MySQLBillDAO billDAO = new dao.MySQLBillDAO();
            billDAO.saveBill(new model.Bill(name, units, total));

            JOptionPane.showMessageDialog(this, "Bill Saved Successfully for " + name);
            
            // Clear fields for next entry
            nameField.setText("");
            unitField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Enter a valid numeric value for units.");
        }
    });
    return panel;
}
    
    
   private JPanel createReportPanel() {
    JPanel panel = new JPanel(new GridLayout(3, 1, 20, 20));
    panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
    panel.setBackground(Color.WHITE);

    // Create the service to get real data
    service.ReportingService reporting = new service.ReportingService();

    // Create labels with real data from MySQL
    JLabel compLabel = new JLabel("Total Active Complaints: " + reporting.getTotalComplaints());
    JLabel userLabel = new JLabel("Registered System Users: " + reporting.getTotalUsers());
    JLabel revLabel = new JLabel("Total Revenue Collected: KES " + String.format("%.2f", reporting.getTotalRevenue()));

    // Styling
    Font reportFont = new Font("SansSerif", Font.BOLD, 22);
    compLabel.setFont(reportFont);
    userLabel.setFont(reportFont);
    revLabel.setFont(reportFont);
    revLabel.setForeground(new Color(0, 102, 0)); // Professional Green for Revenue

    panel.add(compLabel);
    panel.add(userLabel);
    panel.add(revLabel);

    return panel;
}
    private JPanel createUserPanel() {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    DefaultListModel<User> userListModel = new DefaultListModel<>();
    JList<User> userList = new JList<>(userListModel);

    JPanel inputPanel = new JPanel();
    JTextField userField = new JTextField(10);
    JTextField passField = new JTextField(10);
    JButton addBtn = new JButton("Add User");

    inputPanel.add(new JLabel("User:"));
    inputPanel.add(userField);
    inputPanel.add(new JLabel("Pass:"));
    inputPanel.add(passField);
    inputPanel.add(addBtn);

    panel.add(inputPanel, BorderLayout.NORTH);
    panel.add(new JScrollPane(userList), BorderLayout.CENTER);

    addBtn.addActionListener(e -> {
        String u = userField.getText();
        String p = passField.getText();
        if (!u.isEmpty() && !p.isEmpty()) {
            // Logic to save to database would go here
            userListModel.addElement(new User(u, "Admin"));
            userField.setText("");
            passField.setText("");
            JOptionPane.showMessageDialog(this, "User Created!");
        }
    });

    return panel;
}
    private JPanel createUsagePanel() {
    JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JTextField custIdField = new JTextField();
    JTextField prevField = new JTextField();
    JTextField currField = new JTextField();
    JLabel consumptionLabel = new JLabel("Consumption: 0 m3");
    JButton saveBtn = new JButton("Record Reading");

    panel.add(new JLabel("Customer ID:"));
    panel.add(custIdField);
    panel.add(new JLabel("Previous Reading:"));
    panel.add(prevField);
    panel.add(new JLabel("Current Reading:"));
    panel.add(currField);
    panel.add(new JLabel("Calculated Use:"));
    panel.add(consumptionLabel);
    panel.add(new JLabel(""));
    panel.add(saveBtn);

    saveBtn.addActionListener(e -> {
        try {
            double prev = Double.parseDouble(prevField.getText());
            double curr = Double.parseDouble(currField.getText());
            double consumption = curr - prev;

            if (consumption < 0) {
                JOptionPane.showMessageDialog(this, "Error: Current reading cannot be less than previous.");
                return;
            }

            consumptionLabel.setText("Consumption: " + consumption + " m3");

            // MySQL Save Logic
            java.sql.Connection conn = java.sql.DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/water_billing_db", "root", "");
            String sql = "INSERT INTO usage_records (customer_id, previous_reading, current_reading, consumption) VALUES (?, ?, ?, ?)";
            java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, custIdField.getText());
            pstmt.setDouble(2, prev);
            pstmt.setDouble(3, curr);
            pstmt.setDouble(4, consumption);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Usage Recorded Successfully!");
            conn.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    });

    return panel;
}
    private JPanel createLabelPanel(String text) {
        JPanel p = new JPanel(new GridBagLayout());
        p.add(new JLabel(text));
        return p;
    }

    private void updateUI() {
        listModel.clear();
        for (Complaint c : service.getAll()) {
            listModel.addElement("[" + c.getStatus() + "] " + c.getDescription());
        }
        totalLabel.setText("Total Complaints: " + reportingService.getTotalComplaintsCount());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminFrame::new);
    }
}

