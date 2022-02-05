package com.salanb.webapp.repositories;

import com.salanb.orm.utillities.ResourceNotFoundException;
import com.salanb.webapp.models.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductRepo {

    public Product addProduct(Product p) throws SQLException;
    public Product getProduct(int id);
    public List<Product> getAllProducts();
    public Product updateProduct(Product change);
    public Product deleteProduct(int id) throws ResourceNotFoundException;
}
