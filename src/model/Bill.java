package model;

public class Bill {
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
}
