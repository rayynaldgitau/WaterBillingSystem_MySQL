package dao;

import model.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDAO {
    private Map<Integer, Customer> data = new HashMap<>();
    public void save(Customer c) { data.put(c.getCustomerID(), c); }
    public Customer findById(int id) { return data.get(id); }
    public Customer findByEmail(String email) { for (Customer c : data.values()) if (c.getEmail().equalsIgnoreCase(email)) return c; return null; }
    public List<Customer> findAll() { return new ArrayList<>(data.values()); }
    public boolean delete(int id) { return data.remove(id) != null; }
    public int count() { return data.size(); }
}
