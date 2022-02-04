package com.salanb.webapp.services;

import com.salanb.orm.utillities.ResourceNotFoundException;
import com.salanb.webapp.models.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    public Product addProduct(Product p);
    public Product getProduct(int id);
    public List<Product> getAllProducts();
    public Product updateProduct(Product change);
    public Product deleteProduct(int id) throws ResourceNotFoundException;

    public List<Product> getALLProductsAbovePrice(BigDecimal price);
    public List<Product> getAllProductsBelowPrice(BigDecimal price);
    public List<Product> getAllProductsBetweenPrice(BigDecimal price);
    public List<Product> getAllAvailableProducts();
    public List<Product> getAllUnavailableProducts();

}
