package model;
public class Customer {
    private int customerID;
    private String name, address, phoneNumber, email, password;
    private boolean active;
    public Customer(int customerID, String name, String address, String phoneNumber, String email, String password) {
        setCustomerID(customerID); setName(name); setAddress(address); setPhoneNumber(phoneNumber); setEmail(email); setPassword(password); this.active = true;
    }
    public int getCustomerID() { return customerID; }
    public void setCustomerID(int id) { if (id <= 0) throw new IllegalArgumentException("ID must be positive."); this.customerID = id; }
    public String getName() { return name; }
    public void setName(String n) { if (n == null || n.trim().isEmpty()) throw new IllegalArgumentException("Name required."); this.name = n.trim(); }
    public String getAddress() { return address; }
    public void setAddress(String a) { this.address = a != null ? a.trim() : ""; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String p) { if (p == null || p.trim().isEmpty()) throw new IllegalArgumentException("Phone required."); this.phoneNumber = p.trim(); }
    public String getEmail() { return email; }
    public void setEmail(String e) { if (e == null || !e.contains("@")) throw new IllegalArgumentException("Valid email required."); this.email = e.trim(); }
    public String getPassword() { return password; }
    public void setPassword(String p) { if (p == null || p.length() < 4) throw new IllegalArgumentException("Password min 4 chars."); this.password = p; }
    public boolean isActive() { return active; }
    public void setActive(boolean a) { this.active = a; }
    public String toString() { return "Customer[" + customerID + "," + name + "]"; }
}
