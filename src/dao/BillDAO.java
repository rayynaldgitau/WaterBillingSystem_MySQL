package dao;

import java.util.ArrayList;
import java.util.List;
import model.Bill;

public class BillDAO {
    
    private List<Bill> savedBills;

    public BillDAO() {
        this.savedBills = new ArrayList<>();
    }

    public void addBill(Bill bill) {
        savedBills.add(bill);
    }

    public List<Bill> getAllSavedBills() {
        return savedBills;
    }

    public int totalBillsGenerated() {
        return savedBills.size();
    }
}

