package com.salanb.webapp.services;

import com.salanb.orm.utillities.ResourceNotFoundException;
import com.salanb.webapp.models.Customer;
import com.salanb.webapp.models.Product;

import java.util.List;

public interface CustomerService {
    public Customer addCustomer(Customer c);
    public Customer getCustomer(int id);
    public List<Customer> getAllCustomers();
    public Customer updateCustomer(Customer change);
    public Customer deleteCustomer(int id) throws ResourceNotFoundException;

}
