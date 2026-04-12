package service;

import dao.ComplaintDAO;
import dao.MySQLComplaintDAO;
import model.Complaint;
import java.util.List;

public class ComplaintService {
    // Connect to the MySQL version instead of an ArrayList
    private ComplaintDAO dao = new MySQLComplaintDAO();

    public void addComplaint(String desc) {
        dao.addComplaint(new Complaint(desc));
    }

    public List<Complaint> getAll() {
        return dao.getAllComplaints();
    }

    // This connects the 'Resolve' button to the database
    public void updateStatus(String desc) {
        ((MySQLComplaintDAO) dao).updateStatus(desc);
    }
    public void resolveComplaint(String description) {
    // We cast to MySQLComplaintDAO to access the updateStatus method
    ((dao.MySQLComplaintDAO) dao).updateStatus(description);
}
}