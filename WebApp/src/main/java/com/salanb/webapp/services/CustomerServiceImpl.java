package com.salanb.webapp.services;

import com.salanb.orm.utillities.ResourceNotFoundException;
import com.salanb.webapp.models.Customer;
import com.salanb.webapp.repositories.CustomerRepo;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private CustomerRepo cr;

    public CustomerServiceImpl(CustomerRepo cr) {
        this.cr = cr;
    }

    @Override
    public Customer addCustomer(Customer c) {
        return cr.addCustomer(c);
    }

    @Override
    public Customer getCustomer(int id) {
        return cr.getCustomer(id);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return cr.getAllCustomers();
    }

    @Override
    public Customer updateCustomer(Customer change) {
        return cr.updateCustomer(change);
    }

    @Override
    public Customer deleteCustomer(int id) throws ResourceNotFoundException {
        return cr.deleteCustomer(id);
    }
}
