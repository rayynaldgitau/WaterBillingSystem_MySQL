package dao;

import model.Administrator;

public interface AdminDAO {
    // This defines the two things an Admin system MUST do
    boolean authenticate(String username, String password);
    Administrator getAdminDetails(String username);
}