package com.salanb.webapp.repositories;

import com.salanb.orm.utillities.ResourceNotFoundException;
import com.salanb.webapp.models.Customer;

import java.util.List;

public interface CustomerRepo {
    public Customer addCustomer(Customer c);
    public Customer getCustomer(int id);
    public List<Customer> getAllCustomers();
    public Customer updateCustomer(Customer change);
    public Customer deleteCustomer(int id) throws ResourceNotFoundException;
}
