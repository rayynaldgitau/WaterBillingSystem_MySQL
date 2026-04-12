package model;

public class Bill {
<<<<<<< HEAD
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
=======
    private int unitsUsed;
    private double rate = 45.0;
    private double vat = 0.16;

    public Bill(int unitsUsed) {
        this.unitsUsed = unitsUsed;
    }

    public double calculateFinalTotal() {
        double subtotal = unitsUsed * rate;
        return subtotal + (subtotal * vat);
    }

    public int getUnitsUsed() {
        return unitsUsed;
    }
>>>>>>> e4a00967315035fa6cf55cf7096ccc97d6e88e40
}
