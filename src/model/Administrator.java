package model;

public class Administrator {
    private String username;
    private String role;

    public Administrator(String username, String role) {
        this.username = username;
        this.role = role;
    }

    // Getters
    public String getUsername() { return username; }
    public String getRole() { return role; }
}
