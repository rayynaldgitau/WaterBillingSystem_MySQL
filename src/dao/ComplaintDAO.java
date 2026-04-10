package dao;

import model.Complaint;
import java.util.List;

public interface ComplaintDAO {
    void addComplaint(Complaint complaint);
    List<Complaint> getAllComplaints();
}