package model;

public class Bill {
    private String customerName;
    private double units;
    private double totalAmount;

    public Bill(String customerName, double units, double totalAmount) {
        this.customerName = customerName;
        this.units = units;
        this.totalAmount = totalAmount;
    }

    // Getters
    public String getCustomerName() { return customerName; }
    public double getUnits() { return units; }
    public double getTotalAmount() { return totalAmount; }
}
