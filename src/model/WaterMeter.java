package model;
import java.time.LocalDate;

public class WaterMeter {
    private int meterID, customerID;
    private LocalDate installationDate;
    private String status;
    public WaterMeter(int meterID, int customerID, LocalDate installationDate, String status) {
        this.meterID = meterID; this.customerID = customerID; this.installationDate = installationDate; this.status = status;
    }
    public WaterMeter(int meterID, int customerID) { this(meterID, customerID, LocalDate.now(), "Active"); }
    public int getMeterID() { return meterID; }
    public int getCustomerID() { return customerID; }
    public LocalDate getInstallationDate() { return installationDate; }
    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }
}
